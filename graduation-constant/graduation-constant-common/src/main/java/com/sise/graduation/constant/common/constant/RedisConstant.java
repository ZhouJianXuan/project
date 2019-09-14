package com.sise.graduation.constant.common.constant;

public interface RedisConstant {

    String Separtor = "-";

    /**
     * yicall登陆token-session信息存储
     */
    String KEY_LOGIN_TOKEN = "graduation:wt:tmp:lt:private:${token}";

    /**
     * graduation静默坐席私有登陆的token信息
     */
    String COOKIE_TOKEN = "graduation-token";

    String KEY_REQUEST_IP_FAIL_COUNT = "graduation:wt:tmp:rip:failCount:private:${ip}";
    /**
     * graduation 静默坐席在线负载信息
     */
    String KEY_ONLINE_SEAT = "graduation:sn:service:static:manual:online:set";
    /**
     * graduation 静默坐席任务队列
     */
    String KEY_OPERATING_TASK = "graduation:sn:filter:temp:silence:{accountId}";

    String KEY_SESSION_INFO =  "graduation:sn:filter:temp:dial:{sessionId}";

    String MAX_LOAD_NUM = "graduation:max:load:num:temp";
    /**
     * 预登陆的seqNo-session信息存储
     */
    String KEY_PRE_LOGIN_SEQ = "graduation:wt:tmp:plt:private:${seqNo}";

    String PRE_VERIFY_CODE = "graduation_PRE_VERIFY_CODE";

    String KEY_REQUEST_IP_VALID = "graduation:wt:tmp:rip:failCount:private:${nowTime}:${ip}:${username}";
}
