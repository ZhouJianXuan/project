package com.sise.graduation.common.web.component;

import com.alibaba.fastjson.JSONObject;
import com.sise.graduation.constant.common.exception.ServiceException;
import com.sise.graduation.constant.common.vo.LoginAccountInfo;
import org.springframework.util.StringUtils;

/**
 * TODO 暂时写一个holder来保存会话的上下文信息。
 * 实际上这一部分功能应该由aiforce提供，后续他们提供插件后再做迁移
 * 暂时保存在threadlocal中
 *
 * @author jjluo
 * @date 2018/7/9
 */
public class SessionContextHolder {

    private static ThreadLocal<JSONObject> context = ThreadLocal.withInitial(JSONObject::new);
  
    public static void setToken(String token) {
    	context.get().put("token", token);
    }
    
    public static void setLogPrefix(String logPrefix) {
    	context.get().put("logPrefix", logPrefix);
    }
    
    public static void setInstanceId(int instanceId) {
    	context.get().put("instanceId", instanceId);
    }
    
    public static void setProductId(int productId) {
    	context.get().put("productId", productId);
    }
    
    public static JSONObject get() {
    	return context.get();
    }
    
    public static void remove() {
    	context.remove();
    }
    
    public static String getToken() {
    	return context.get().getString("token");
    }
    
    public static String getLogPrefix() {
    	return context.get().getString("logPrefix");
    }
    
    public static int getInstanceId () {
    	return context.get().getIntValue("instanceId");
    }
    
    public static int getProductId() {
    	return context.get().getIntValue("productId");
    }
    
    public static LoginAccountInfo getAccountAndValid() {
    	String token = getToken();
    	if (StringUtils.isEmpty(token)) {
    		throw new ServiceException("login token is empty");
    	}
    	
    	LoginAccountInfo accountInfo = LoginAccountManager.getLoginAccountInfo(token);
    	if (null == accountInfo) {
    		throw new ServiceException("login throught check but cannot find accountInfo");
    	}
    	return accountInfo;
    }
}
