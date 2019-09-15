package com.sise.graduation.common.web.controller;
import com.sise.graduation.common.redis.cache.RedisUtil;
import com.sise.graduation.common.util.DateHelper;
import com.sise.graduation.common.util.RSAEncrypt;
import com.sise.graduation.common.util.ServerUtil;
import com.sise.graduation.common.web.annotation.AccessAuthority;
import com.sise.graduation.common.web.component.LoginAccountManager;
import com.sise.graduation.common.web.component.SessionContextHolder;
import com.sise.graduation.config.common.auth.WebAuthConfig;
import com.sise.graduation.constant.common.constant.RedisConstant;
import com.sise.graduation.constant.common.exception.ServiceException;
import com.sise.graduation.constant.common.vo.HttpBody;
import com.sise.graduation.constant.common.vo.LoginAccountInfo;
import com.sise.graduation.constant.common.vo.LoginVo;
import com.sise.graduation.service.common.service.ILoginValidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
	@Autowired
	private WebAuthConfig webAuthConfig;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private ILoginValidService loginValidService;

	//预登陆
	@GetMapping("/preLoginin")
	@AccessAuthority
	@ResponseBody
	public HttpBody<Object> preLoginin(HttpServletRequest request) {
		String seqNo = UUID.randomUUID().toString();
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("seqNo", seqNo);
		response.put("key", webAuthConfig.getRsaPublicKey());
		LoginVo loginVo = new LoginVo();
		loginVo.setSeqNo(seqNo);
		String requestIp = getRealIP(loginVo,request);
		String ipFailCntKey = RedisConstant.KEY_REQUEST_IP_FAIL_COUNT.replace("${ip}", requestIp);
		Object failVal = redisUtil.get(ipFailCntKey);
		int ipFailCnt = ServerUtil.convert2Int(failVal+"", 0);
		if (ipFailCnt >= 3) {
			response.put("needVerifyCode", true);
		} else {
			response.put("needVerifyCode", false);
		}
		return HttpBody.getInstance(response);
	}
	
	//验证码
	@GetMapping("/getVerifyCode")
	@AccessAuthority
	public void getVerifyCode(@RequestParam("seqNo") String seqNo,HttpServletResponse response) {
		try {
			if (StringUtils.isEmpty(seqNo)) {
				throw new ServiceException("seqNo不能为空");
			}
			BufferedImage verifyImg = loginValidService.getVerifyCode(seqNo);
			response.setContentType("image/png");//必须设置响应内容类型为图片，否则前台不识别
			OutputStream os = response.getOutputStream(); //获取文件输出流    
			ImageIO.write(verifyImg,"png",os);//输出图片流
			response.getOutputStream().flush();
			response.getOutputStream().close();//关闭流		
		} catch (IOException e) {
			log.error("", e);
		}
	}
	
	@PostMapping("/loginin")
	@AccessAuthority
	public HttpBody<Object> loginin(@RequestBody LoginVo loginVo,HttpServletRequest request,HttpServletResponse response) throws Exception {
		String loginToken = SessionContextHolder.getToken();
		LoginAccountInfo accountInfo = LoginAccountManager.getLoginAccountInfo(loginToken);
		if (null != accountInfo) {
			//当前所带token有效，直接登陆成功
			Map<String, Object> resp = new HashMap<>();
			resp.put("url", webAuthConfig.getIndexUrl());
			return HttpBody.getInstance(resp);
		}
		//参数校验
		HttpBody<Object> checkValid = checkLoginValid(loginVo, request);
		if (null != checkValid) {
			//参数校验失败会返回一个httpBody
			return checkValid;
		}
		String decyptPasswd;
		if (webAuthConfig.getIsEncodePassword() == null || webAuthConfig.getIsEncodePassword()){
            //rsa解密操作
			decyptPasswd = RSAEncrypt.decrypt(loginVo.getPassword(), webAuthConfig.getRsaPrivateKey()).toUpperCase();
		}else{
			decyptPasswd = loginVo.getPassword();
		}
		loginVo.setDecyptPasswd(decyptPasswd);

		//token验证
		loginToken = loginValidService.handleLoginin(loginVo);	
		if (StringUtils.isEmpty(loginToken)) {
			//账号或者用户名错误，获取loginToken失败
			incrFailCnt(loginVo.getIpValidKey(), loginVo.getIpFailCountKey());
			throw new ServiceException("帐号或者密码错误");
		} else {//登陆成功则删除ip限制
			if (!StringUtils.isEmpty(loginVo.getIpValidKey())) {
				redisUtil.remove(loginVo.getIpValidKey());
			}
		}
		Cookie loginCookie = new Cookie(RedisConstant.COOKIE_TOKEN, loginToken);
		loginCookie.setMaxAge(7 * 24 * 60 * 60);
		loginCookie.setPath("/");
		response.addCookie(loginCookie);
		Map<String, Object> resp = new HashMap<>(1);
		resp.put("url", webAuthConfig.getIndexUrl());
		return HttpBody.getInstance(resp);
	}
	
	@GetMapping("/loginout")
	@ResponseBody
	public HttpBody<Object> loginout(HttpServletRequest request,HttpServletResponse response) {
		LoginAccountInfo accountInfo = SessionContextHolder.getAccountAndValid();
		loginValidService.loginout(accountInfo,request.getHeader("cookie"));
		Cookie[] cookies = request.getCookies();
		if (null != cookies && 0 < cookies.length) {
			for (Cookie cookie : cookies) {
				if (RedisConstant.COOKIE_TOKEN.equalsIgnoreCase(cookie.getName())) {
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
		Map<String, Object> resp = new HashMap<>(1);
		String redirectUrl = webAuthConfig.getLoginUrl();
		log.info("redirectUrl="+redirectUrl);
		resp.put("url", redirectUrl);
		return HttpBody.getInstance(resp);
	}
	
	/**
	 * 校验登陆参数是否正常
	 * @param loginVo
	 * @param request
	 * @return
	 */
	private HttpBody<Object> checkLoginValid(LoginVo loginVo,HttpServletRequest request) throws ServiceException {
		//ip短时间校验失败次数
		String requestIp = getRealIP(loginVo,request);
		String ipValidKey = "";
		String ipFailCntKey = "";
		int ipFailCnt = 0;
		if (!StringUtils.isEmpty(requestIp)) {
			ipFailCntKey = RedisConstant.KEY_REQUEST_IP_FAIL_COUNT.replace("${ip}", requestIp);
			Object failVal = redisUtil.get(ipFailCntKey);
			ipFailCnt = ServerUtil.convert2Int(failVal+"", 0);
			String nowTime = DateHelper.getDateDimHHMMSS();
			ipValidKey = RedisConstant.KEY_REQUEST_IP_VALID
					.replace("${nowTime}", nowTime)
					.replace("${ip}", requestIp)
					.replace("${username}", loginVo.getUsername());
			Object object = redisUtil.get(ipValidKey);
			int failedCount = ServerUtil.convert2Int(object+"", 0);
			log.info(loginVo.getSeqNo() + " loginVo="+loginVo+",ipValidKey="+ipValidKey+",failedCount="+failedCount);
			if (failedCount >= 3) {
				//5秒内失败次数超过3次则弹出去
				log.info(loginVo.getSeqNo() + " get requestIp="+requestIp + " failedCount = " + failedCount +" over 3.");
				throw new ServiceException("失败次数过多");
			}
		}
		if (StringUtils.isEmpty(loginVo.getSeqNo())) {
			incrFailCnt(ipValidKey, "");
			throw new ServiceException("seqNo不能为空");
		}

		//用户名密码校验
		if (StringUtils.isEmpty(loginVo.getUsername()) || StringUtils.isEmpty(loginVo.getPassword())) {
			log.error(loginVo.getSeqNo() + " loginVo=" + loginVo + " login in without username or passwd..");
			incrFailCnt(ipValidKey, ipFailCntKey);
			throw new ServiceException("用户名或密码错误");
		}
		//验证码验证
		//单ip30分钟内累计触发3次密码输入错误时增加验证码输入要求
		if (ipFailCnt >= 3) {
			if (StringUtils.isEmpty(loginVo.getVerifyCode())) {
				incrFailCnt(ipValidKey, "");
				throw new ServiceException("verifyCode为空");
			}
			String seqPreRedisKey = RedisConstant.KEY_PRE_LOGIN_SEQ.replace("${seqNo}", loginVo.getSeqNo());
			Object seqVerifyCode = redisUtil.hmGet(seqPreRedisKey, RedisConstant.PRE_VERIFY_CODE);
			//不管成没成功都要删除对应的验证码
			redisUtil.hremove(seqPreRedisKey, RedisConstant.PRE_VERIFY_CODE);
			if (null == seqVerifyCode || !loginVo.getVerifyCode().equalsIgnoreCase(seqVerifyCode+"")) {
				incrFailCnt(ipValidKey, "");
				throw new ServiceException("验证码错误");
			}
		}
		//校验完成后将数据放回去
		loginVo.setRequestIp(requestIp);
		loginVo.setIpFailCountKey(ipFailCntKey);
		loginVo.setIpValidKey(ipValidKey);
		return null;
	}
	
	/**
	 * 登陆失败次数增加
	 * @param ipValidKey
	 * @param ipFailCntKey
	 */
	private void incrFailCnt(String ipValidKey, String ipFailCntKey) {
		if (!StringUtils.isEmpty(ipValidKey)) {
			redisUtil.incrementAndGet(ipValidKey, 1, 10);
		}
		if (!StringUtils.isEmpty(ipFailCntKey)) {
				redisUtil.incrementAndGet(ipFailCntKey, 1, 30 * 60);
		}
	}
	
	/**
	 * 获取对应请求的真实IP
	 * @param loginVo
	 * @param request
	 * @return
	 */
	private static String getRealIP(LoginVo loginVo,HttpServletRequest request) {
		String realIp = request.getHeader("X-Real-IP");
		String forwardIp = request.getHeader("X-Forwarded-For");
		String remoteAddr = request.getRemoteAddr();
		String remoteHost = request.getRemoteHost();
		log.info(loginVo.getSeqNo() + " loginVo="+loginVo +",realIp="+realIp+",forwardIp="+forwardIp+",remoteAddr="+remoteAddr+",remoteHost="+remoteHost);
		if (!StringUtils.isEmpty(forwardIp)) {
			String[] forwardIpArray = forwardIp.split(",");
			if (0 < forwardIpArray.length) {
				realIp = forwardIpArray[0];
			}
		}
		if (StringUtils.isEmpty(realIp)) {
			realIp = request.getRemoteHost();
		}
		return realIp;
	}
}
