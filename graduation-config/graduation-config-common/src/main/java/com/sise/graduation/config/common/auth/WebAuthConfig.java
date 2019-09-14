package com.sise.graduation.config.common.auth;

import lombok.Data;

/**
 * @ClassName WebAuthConfig
 * @Description
 * @Author CCJ
 * @Date 2019/9/14 14:51
 **/
@Data
public class WebAuthConfig {

    private String indexUrl;

    private String loginUrl;

    private String rsaPublicKey;

    private String rsaPrivateKet;

    private Boolean isEncodePassword;
}