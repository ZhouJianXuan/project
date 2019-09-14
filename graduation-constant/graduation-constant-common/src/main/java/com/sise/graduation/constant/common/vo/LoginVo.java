package com.sise.graduation.constant.common.vo;

import lombok.Data;

@Data
public class LoginVo {

	private String seqNo;
	
	private String username;
	
	private String password;

	private String decyptPasswd;
	
	private String verifyCode;
	
	private String requestIp; //checkLoginValid
	private String ipFailCountKey; //checkLoginValid
	private String ipValidKey;
	
//	public void setPassword(String password) {
//		this.password = password.toUpperCase();
//	}
}


