package com.sise.graduation.constant.common.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginAccountInfo {
	public static final int TOKEN_INVALID = -1;
	public static final int CALL_CAN_USE = 1;
	public static final int CALL_NOT_INIT_SUCCESS = 2;
	public static final int CALL_NOT_ACTIVE = 3;

	//该账号资源是否初始成功，主要是针对aiforce对接的账号数据库初始
	private int accountStatus; // 1 -- call系统可用， 2 -- call中数据库资源未初始成功， 3 -- call中用户状态被未激活

	private String socketId;

	private String token;

	private String userName;

	/**
	 * 昵称
	 */
	private String nickName;

	private String avatar;
	/**
	 * 头像地址
	 */
	private int userId;
	/**
	 * 角色
	 */
	private int role;

	private int autoPlay;

	private int maxLoad;

	private long buildTimestamp;

	//权限信息等
}
