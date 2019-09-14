package com.sise.graduation.common.web.util;

import com.sise.graduation.common.web.component.LoginAccountManager;
import com.sise.graduation.common.web.component.SessionContextHolder;
import com.sise.graduation.constant.common.vo.LoginAccountInfo;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName CookieUtil
 * @Description
 * @Author CCJ
 * @Date 2019/9/14 18:01
 **/
public class CookieUtil {
    public static boolean validCookieToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        //根据token信息获取对应的用户信息
        LoginAccountInfo accountInfo = LoginAccountManager.getLoginAccountInfo(token);
        if (null == accountInfo) {
            return false;
        }
        SessionContextHolder.setToken(token);
        return true;
    }

    public static String getCookieToken(HttpServletRequest request, String cookieKey) {
        //非public需要进行登陆校验
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return token;
        }

        for (Cookie cookie : cookies) {
            if (cookieKey.equalsIgnoreCase(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }
        return token;
    }
}
