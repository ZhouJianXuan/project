package com.sise.graduation.common.util;

import org.apache.commons.lang3.StringUtils;

public class ServerUtil {

	/**
	 * 对source字符串进行去空格，包括中间的空格
	 * @param source
	 * @return
	 */
	public static String trimAll(String source) {
		if (StringUtils.isEmpty(source)) return null;
		String[] array = source.split(" ");
		StringBuilder sb = new StringBuilder();
		for (String s : array) {
			sb.append(s);
		}	
		return sb.toString();
	}
	
	public static int convert2Int(String value,int defaultValue) {
		try {
			return Integer.valueOf(value);
		} catch (Exception e){
			return defaultValue;
		}
	}

	public static long convert2Long(String value, long defaultValue) {
		try {
			return Long.valueOf(value);
		} catch (Exception e){
			return defaultValue;
		}
	}
}
