package com.sise.graduation.common.web.component;

import com.alibaba.fastjson.JSONObject;
import com.sise.graduation.common.redis.cache.RedisUtil;
import com.sise.graduation.constant.common.constant.RedisConstant;
import com.sise.graduation.constant.common.constant.TimeConstant;
import com.sise.graduation.constant.common.vo.LoginAccountInfo;
import org.springframework.util.StringUtils;

/**
 * 登陆账号管理器
 * @author admin
 *
 */
public class LoginAccountManager {

	/**
	 * 根据cookie-token信息获取私有登陆账号信息
	 * step1: 去本地LRU缓存中寻找，如果本地LRU缓存中存在登陆信息，直接返回账号信息
	 * step2: 去redis缓存中寻找，如果存在直接返回
	 * @param token
	 * @return
	 */
	public static LoginAccountInfo getLoginAccountInfo(String token) {
		if (StringUtils.isEmpty(token)) 
			return null;
		
		LoginAccountInfo accountInfo = null;

		String redisKey = RedisConstant.KEY_LOGIN_TOKEN.replace("${token}", token);
		RedisUtil redisUtil = (RedisUtil) SpringContext.getBeanById("redisUtil");
		Object value = redisUtil.get(redisKey);
		if (null == value) 
			return null;
		
		String loginInfo = value.toString();
		if (StringUtils.isEmpty(loginInfo)) 
			return null;
		
		accountInfo = JSONObject.parseObject(loginInfo, LoginAccountInfo.class);
		accountInfo.setBuildTimestamp(System.currentTimeMillis());

		return accountInfo;
	}

	public static void setLoginAccountInfo(LoginAccountInfo accountInfo, String loginToken, RedisUtil redisUtil) {
		// set 到redis
		String userInfo = JSONObject.toJSONString(accountInfo);
		String redisKey = RedisConstant.KEY_LOGIN_TOKEN.replace("${token}", loginToken);
		redisUtil.set(redisKey, userInfo, TimeConstant.SERVEN_DAY_SECOND);
	}
}
