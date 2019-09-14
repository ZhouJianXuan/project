package com.sise.graduation.common.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.sise.graduation.common.web.annotation.AccessRolePermission;
import com.sise.graduation.common.web.component.LoginAccountManager;
import com.sise.graduation.common.web.util.CookieUtil;
import com.sise.graduation.constant.common.constant.InterceptorConstant;
import com.sise.graduation.constant.common.constant.RedisConstant;
import com.sise.graduation.constant.common.enums.AccountEnum;
import com.sise.graduation.constant.common.vo.HttpBody;
import com.sise.graduation.constant.common.vo.LoginAccountInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName RoleHandlerInterceptor
 * @Description
 * @Author CCJ
 * @Date 2019/9/14 17:58
 **/
@Component(InterceptorConstant.ROLE_AUTH_INTERCEPTOR)
public class RoleHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieUtil.getCookieToken(request, RedisConstant.COOKIE_TOKEN);
        LoginAccountInfo accountInfo = LoginAccountManager.getLoginAccountInfo(token);
        if (accountInfo == null){
            return false;
        }
        HandlerMethod method = (HandlerMethod) handler;

        return !accessRolePermission(response, method, accountInfo);
    }

    private boolean accessRolePermission(HttpServletResponse response, HandlerMethod handlerMethod, LoginAccountInfo accountInfo) {
        if (handlerMethod.hasMethodAnnotation(AccessRolePermission.class) || handlerMethod.getBeanType().isAnnotationPresent(AccessRolePermission.class)) {
            if (accountInfo.getRole() != AccountEnum.ADMIN.getCode()) {
                HttpBody<Object> body = HttpBody.getInstance("权限不足，请联系管理员");
                String message = JSONObject.toJSONString(body);
                writeHttpBody(response, message);
                return true;
            }
        }
        return false;
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
}
