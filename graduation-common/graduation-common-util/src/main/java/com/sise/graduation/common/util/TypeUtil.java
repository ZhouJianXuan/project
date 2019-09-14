package com.sise.graduation.common.util;

import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeUtil {

    public static final String SENTENCE_SEP = ";";

    public static final String COLON = ":";
    public static final String COMMA = ",";

    public static final String SET_STRING_SPLIT = ",";

    private static final String SEP = ";|,";

    private static Pattern IS_NUMBER = Pattern.compile("[0-9]*");

    private static Pattern IS_MOBILE = Pattern.compile("^1[3|4|5|7|8|9][0-9]\\d{8}$");

    public static boolean isNumber(String string) {
        if (isEmpty(string)) {
            return false;
        }
        return IS_NUMBER.matcher(string).matches();
    }


    public static boolean isNumber(char chr) {
        if (chr >= '0' && chr <= '9') {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMobile(String mobile) {
        return IS_MOBILE.matcher(mobile).matches();
    }

    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEmpty(Collection<?> list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isEmpty(Set<?> set) {
        return set == null || set.isEmpty();
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean notEmpty(List<?> list) {
        return !isEmpty(list);
    }

    public static boolean notEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    public static boolean notEmpty(String string) {
        return !isEmpty(string);
    }

    public static boolean notEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean notEmpty(Set<?> set) {
        return !isEmpty(set);
    }

    public static List<String> split(String word) {
        return split(word, SEP);
    }

    public static List<String> split(String word, String sep) {
        return isEmpty(word) ? null : Arrays.asList(word.split(sep));
    }

    public static String concat(String origin, String appender) {
        if(appender == null || appender.isEmpty()) {
            return origin;
        }

        if(origin == null || origin.isEmpty()) {
            return appender + SENTENCE_SEP;
        }

        return origin + appender + SENTENCE_SEP;
    }

    public static String getRealMobileNum(String mobileNum) {
        // 号码带前缀的情况截取最后11位
        if (!isEmpty(mobileNum) && mobileNum.length() >= 11) {
            return mobileNum.substring(mobileNum.length()-11);
        }
        return mobileNum;
    }
    
    public static String nvl (String val, String defaultVal) {
        if (isEmpty(val)) {
            return defaultVal;
        } else {
            return val;
        }
    }

    public static Object nvl (Object val, Object defaultVal) {
        if (isEmpty(val)) {
            return defaultVal;
        } else {
            return val;
        }
    }

    public static boolean isEmpty(Object obj){
        if (obj ==  null) {
            return true;
        }
        if (obj instanceof String) {
            return isEmpty((String)obj);
        } else if (obj instanceof List) {
            return isEmpty((List) obj);
        } else if (obj instanceof Map) {
            return isEmpty((Map) obj);
        } else if (obj instanceof Set) {
            return isEmpty((Set) obj);
        } else if (obj instanceof Array) {
            return isEmpty((Object[])obj);
        } else {
            return false;
        }
    }


    public static Date nvl (Date val, Date defaultVal) {
        if (val == null) {
            return defaultVal;
        } else {
            return val;
        }
    }

    public static String calculateDialogDuration(int millis) {
        int second = millis / 1000;
        if (second < 60) {
            return second + "秒";
        } else if (second < 3600) {
            int min = second / 60;
            int minSec = second % 60;
            return min + "分" + minSec + "秒";
        } else {
            int hour = second / 3600 ;
            int hourSec = second % 3600 ;
            int min = hourSec / 60;
            int sec = hourSec % 60;
            return hour + "小时" + min + "分" + sec + "秒";
        }
    }

    /**
     * byte 1,0 转 boolean
     * @param is
     * @return
     */
    public static boolean isTrue(Byte is) {
        if (is == null || is == (byte)0) {
            return false;
        } else if (is == (byte)1){
            return true;
        }
        return false;
    }

    /**
     * boolean true or false 转 byte 1,0
     * @param is
     * @return
     */
    public static byte boolean2Byte(Boolean is) {
        if (is == null || !is) {
            return (byte)0;
        } else {
            return (byte)1;
        }
    }


    public static String appendString(String origin, String append) {
        if (isEmpty(origin)) {
            return append;
        } else {
            return origin + append;
        }
    }


    public static boolean isOnlyNumOrLetterOrUnderline(String str) {
        if (TypeUtil.isEmpty(str)) {
            return false;
        }
        List<String> rs = getMatchers("^[A-Za-z0-9_]+$", str);
        return rs.size() > 0 ;
    }

    public static List<String> getMatchers(String regex, String source){
        List<String> list = new ArrayList<>();
        if (isEmpty(regex) || isEmpty(source)) {
            return list;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }


    /**
     * 将毫秒数格式化成 hh:mm:ss:mill
     * @param millis
     * @return
     */
    public static String formatMillis(Integer millis) {
        int mill = millis % 1000;
        int second = millis / 1000;

        int minute = 0;
        int hour = 0;
        if (second >= 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute >= 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        StringBuilder formatRs = new StringBuilder();
        formatRs.append(String.format("%02d", hour)).append(":");
        formatRs.append(String.format("%02d", minute)).append(":");
        formatRs.append(String.format("%02d", second)).append(":");
        formatRs.append(String.format("%03d", mill));
        return formatRs.toString();

    }


    public static String removeAllBlank(String str) {
        if (TypeUtil.isEmpty(str)) {
            return "";
        }
        char[] newStr = new char[str.length()];
        int readIndex = 0;
        int writeIndex = 0;
        while (readIndex < str.length()){
            char chr = str.charAt(readIndex++);
            if (chr == ' ') {
                continue;
            }
            newStr[writeIndex++] = chr;
        }
        return new String(newStr, 0, writeIndex);
    }

    public static String upper(String str) {
        if (isEmpty(str)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for(char chr: str.toCharArray()) {
            if (chr >= 'a' && chr <= 'z') {
                char upperChr= (char)(chr + ('A' - 'a'));
                builder.append(upperChr);
            } else {
                builder.append(chr);
            }
        }
        return builder.toString();
    }


    public static String lower(String str) {
        if (isEmpty(str)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for(char chr: str.toCharArray()) {
            if (chr >= 'A' && chr <= 'Z') {
                char upperChr= (char)(chr + ('a' - 'A'));
                builder.append(upperChr);
            } else {
                builder.append(chr);
            }
        }
        return builder.toString();
    }


    /**
     * 获取float的小数位数
     */
    public static int getFractionalPartLength(float num) {
        String numStr = new Float(num).toString();
        int radixPointIndex = numStr.indexOf(".");
        if (radixPointIndex == -1) {
            return 0;
        }
        if (radixPointIndex == numStr.length() - 1) {
            return 0;
        }
        String  fractionalPart =  numStr.substring(radixPointIndex + 1);
        return fractionalPart.length();
    }

    /**
     * 求百分比，2位小数
     * @param dividend
     * @param divisor
     * @return
     */
    public static float getPercentWithDecimalPlaces(int dividend, int divisor) {
        if (divisor == 0) {
            return 0;
        }
        float rs = (float)dividend/divisor;
        return Math.round(rs * 10000)/100f;

    }

}
