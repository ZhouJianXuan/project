package com.sise.graduation.common.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.sise.graduation.common.web.annotation.AccessAuthority;
import com.sise.graduation.common.web.annotation.AccessRolePermission;
import com.sise.graduation.common.web.component.LoginAccountManager;
import com.sise.graduation.common.web.component.SessionContextHolder;
import com.sise.graduation.common.web.util.CookieUtil;
import com.sise.graduation.config.common.auth.WebAuthConfig;
import com.sise.graduation.constant.common.constant.InterceptorConstant;
import com.sise.graduation.constant.common.constant.RedisConstant;
import com.sise.graduation.constant.common.enums.AccountEnum;
import com.sise.graduation.constant.common.vo.HttpBody;
import com.sise.graduation.constant.common.vo.LoginAccountInfo;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 私有登陆版本拦截校验
 *
 * @author stan zhang
 * @date 2018/11/1
 */
@Slf4j
@Component(InterceptorConstant.ACCESS_AUTH_INTERCEPTOR)
public class PrivateHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private WebAuthConfig webAuthConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = CookieUtil.getCookieToken(request, RedisConstant.COOKIE_TOKEN);
        log.info(request.getRequestURI() + "," + handler.getClass() + "," + request.getRemoteAddr() + "," + token);

        String uri = request.getRequestURI();
        if (isPrivateLogin(uri)) {
            //直接访问登陆页面，判断是否带有有效的token信息
            boolean isTokenValid = CookieUtil.validCookieToken(token);
            if (isTokenValid) {
                //当前token有效的话，直接重定向到yicall产品页面
                htmlWriteRedirect(response, webAuthConfig.getIndexUrl());
                return false;
            } else {
                return true;
            }
        }

        // 类似于/yicall/ 这种无指定路径重定向访问
        if (isParameterizableViewController(handler)) {
            boolean isTokenValid = CookieUtil.validCookieToken(token);
            if (isTokenValid) {
                //当前token有效的话，直接重定向到yicall产品页面
                htmlWriteRedirect(response, webAuthConfig.getIndexUrl());
                return false;
            } else {
                htmlWriteRedirect(response, webAuthConfig.getLoginUrl());
                return false;
            }
        }
        //静态资源访问
        if (isResource(handler)) {
            return true;
        }
        //后端请求
        HandlerMethod method = (HandlerMethod) handler;
        if (isHandlerMethod(handler)) {
            if (isAccessAuthorityPublic(method)) {
                return true;
            }
        }
        boolean isTokenValid = CookieUtil.validCookieToken(token);
        if (!isTokenValid) {
            ajaxWriteRedirect(response, webAuthConfig.getLoginUrl());
            return false;
        }
        return true;
    }



    /**
     * 校验私有登陆的token是否有效
     *
     * @param token
     * @return
     */

    /**
     * 是否静态资源访问
     */
    public boolean isResource(Object handler) {
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }
        return false;
    }

    /**
     * ajax请求发送重定向
     *
     * @param response
     */
    public void ajaxWriteRedirect(HttpServletResponse response, String url) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("redirectUrl", url);
        HttpBody<Object> httpBody = HttpBody.getInstance("登录超时");
        String message = JSONObject.toJSONString(httpBody);
        writeHttpBody(response, message);
    }

    private void writeHttpBody(HttpServletResponse response, String message) {
        try {
            byte[] array = message.getBytes(StandardCharsets.UTF_8);
            response.getOutputStream().write(array);
            response.getOutputStream().flush();
            //response.sendRedirect(authUrl);
        } catch (IOException e) {
            // ignore
        }
    }

    /**
     * 浏览器请求发送重定向
     *
     * @param response
     */
    public void htmlWriteRedirect(HttpServletResponse response, String url) {
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            // ignore
        }
    }


    public boolean isAccessAuthorityPublic(HandlerMethod method) {
        if (method == null) {
            return false;
        }
        if (method.hasMethodAnnotation(AccessAuthority.class) ||
            method.getBeanType().isAnnotationPresent(AccessAuthority.class)) {
            AccessAuthority accessAuthority = method.getMethodAnnotation(AccessAuthority.class);
            String authority = accessAuthority.value();
            return AccessAuthority.PUBLIC.equalsIgnoreCase(authority);
        }
        return false;
    }

    /**
     * // 类似于/yicall/ 这种无指定路径重定向访问
     *
     * @param handler
     * @return
     */
    public boolean isParameterizableViewController(Object handler) {
        if (handler instanceof ParameterizableViewController) {
            return true;
        }
        return false;
    }


    /**
     * 后端请求
     *
     * @param handler
     * @return
     */
    public boolean isHandlerMethod(Object handler) {
        if (handler instanceof HandlerMethod) {
            return true;
        }
        return false;
    }

    public boolean isPrivateLogin(String uri) {
        if (StringUtils.isEmpty(uri)) {
            return false;
        }
        if (uri.contains("login.html")) {
            return true;
        }
        return false;
    }

}
