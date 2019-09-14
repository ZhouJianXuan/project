package com.sise.graduation.impl.common.impl;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.UUID;

import com.sise.graduation.common.redis.cache.RedisUtil;
import com.sise.graduation.common.util.VerifyCode;
import com.sise.graduation.constant.common.constant.RedisConstant;
import com.sise.graduation.constant.common.vo.LoginAccountInfo;
import com.sise.graduation.constant.common.vo.LoginVo;
import com.sise.graduation.service.common.service.ILoginValidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class LoginValidServiceImpl implements ILoginValidService {

	@Autowired
	private RedisUtil redisUtil;


	@Override
	public void loginout(LoginAccountInfo accountInfo, String cookie) {
		String token = accountInfo.getToken();
		String redisKey = RedisConstant.KEY_LOGIN_TOKEN.replace("${token}", token);
		redisUtil.remove(redisKey); 
	}
	@Override
	public String handleLoginin(LoginVo loginVo) throws Exception {
//		AccountPO account = accountDao.selectByUserPass(loginVo.getUsername(), loginVo.getDecyptPasswd());
//		if (null == account) {
//			log.info(loginVo.getSeqNo() + " loginVo="+loginVo + ",get decyptPasswd="+loginVo.getDecyptPasswd()+" cannot find privateUser");
//			return null;
//		}
//		//生成对应的token信息
		UUID uuid = UUID.randomUUID();
		String loginToken = uuid.toString();
//		LoginAccountInfo accountInfo = new LoginAccountInfo();
//		accountInfo.setToken(loginToken);
//		accountInfo.setUserName(account.getAccount());
//		accountInfo.setNickName(account.getName());
//		accountInfo.setUserId(account.getAccountId());
//		accountInfo.setMaxLoad(account.getMaxLoad());
//		accountInfo.setAvatar(account.getAvatar());
//		accountInfo.setRole(account.getRole());
//		accountInfo.setAccountStatus(account.getStatus());
//		accountInfo.setAutoPlay(account.getAutoPlay());
//		LoginAccountManager.setLoginAccountInfo(accountInfo, loginToken, redisUtil);
//		account.setUpdateTime(new Date());
//		accountDao.updateLastLoginTime(account);
		return loginToken;
	}


	@Override
	public BufferedImage getVerifyCode(String seqNo) {
		int width=200;
		int height=69;
		BufferedImage verifyImg = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		//生成对应宽高的初始图片
		String randomText = VerifyCode.drawRandomText(width,height,verifyImg);
		String redisKey = RedisConstant.KEY_PRE_LOGIN_SEQ.replace("${seqNo}", seqNo);
		redisUtil.hmSet(redisKey, RedisConstant.PRE_VERIFY_CODE, randomText, 60L);
		return verifyImg;
	}
}
