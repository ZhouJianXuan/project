package com.sise.graduation.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * @author jjluo
 * @date 2018/6/29
 */
public class DateHelper {

    private static final DateTimeFormatter PATTERN_YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static final String HH__mm__ss = "HH:mm:ss";

    private static final String yyyyMMdd = "yyyyMMdd";

    private static final String yyyy_MM_dd = "yyyy-MM-dd";

    private static final String yyyy_MM_dd_HH_mm_SS = "yyyy-MM-dd HH:mm:ss";

    private static final String yyyyMMddHHmmSS = "yyyyMMddHHmmss";
    
    private static final String HH_mm = "HH:mm";

    /**
     * 界面，其他输出统一标准格式
     */
    private static final String STANDARD_YYYY__MM__DD_HH_MM = "yyyy.MM.dd HH:mm:ss";

    private static final String STANDARD_YYYY__MM__DD = "yyyy.MM.dd";

    public static Date DATE_1970_01_01;

    static {
        try {
            DATE_1970_01_01 = new SimpleDateFormat(yyyy_MM_dd_HH_mm_SS).parse("1970-01-01 00:00:00");
        } catch (ParseException e) {
            // ignore
        }
    }
    
    public static Date parseHHmm(String hhmm) {
    	if (StringUtils.isEmpty(hhmm)) {
    		return null;
    	}
    	SimpleDateFormat sdf = new SimpleDateFormat(HH_mm);
    	try {
    		return sdf.parse(hhmm);
    	} catch (Exception e) {
    		return null;
    	}
    }

    public static String toHH__mm__ss(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(HH__mm__ss);
        return format.format(date);
    }

    public static String toStandard(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(STANDARD_YYYY__MM__DD_HH_MM);
        return format.format(date);
    }

    public static String toSTANDARD_YYYY__MM__DD(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(STANDARD_YYYY__MM__DD);
        return format.format(date);
    }



    public static String toYYYYMMDD(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(yyyyMMdd);
        return format.format(date);
    }

    public static String toYYYYMMDD(LocalDate date) {
        if (date == null) {
            return "";
        }
        return PATTERN_YYYYMMDD.format(date);
    }

    public static String toYYYY_MM_DD(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(yyyy_MM_dd);
        return format.format(date);
    }

    public static String toYYYY_MM_DD_HH_MM_SS(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(yyyy_MM_dd_HH_mm_SS);
        return format.format(date);
    }
    
    public static String toHH_MM_SS(LocalTime localTime) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(HH__mm__ss);
    	return localTime.format(formatter);
    }

    public static Date parseYYYY_MM_DD_HH_MM_SS(String source){
    	SimpleDateFormat format = new SimpleDateFormat(yyyy_MM_dd_HH_mm_SS);
    	try {
    		return format.parse(source);
    	} catch (Exception e) {
    		return null;
    	}
    }

    public static String toYYYYMMDDHHMMSS(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(yyyyMMddHHmmSS);
        return format.format(date);
    }

    public static String toMMSS(long diff) {
        long min = diff / (60 * 1000);
        long sec = diff/1000 - min*60;
        return min == 0 ? String.format("%s秒", sec) : String.format("%s分%s秒", min, sec);
    }

  public static int minus(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return -1;
        }
        return (int) ((date1.getTime() - date2.getTime()) / (1000*3600*24));
    }

    public static boolean isDefaultDate(Date date) {
        if (date == null) {
            return true;
        }
        return DATE_1970_01_01.compareTo(date) == 0;
    }

    @Deprecated
    public static Date getDate0Clock(long now) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    /**
     * 获取指定时间的凌晨时间
     * @param date
     * @return
     */
    public static Date getDateAtMidnight(Date date) {
        if (date == null) {
            return null;
        }
        LocalDateTime localDateTime = date2LocalDateTime(date);
        LocalDateTime  dateTimeAtMidnight = LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIDNIGHT);
        return localDateTime2Date(dateTimeAtMidnight);
    }

    /**
     * date string 转 LocalDate, pattern为string的样式，如yyyy-MM-dd
     * @param dateStr
     * @param pattern
     * @return
     */
    public static LocalDate parseLocalDate(String dateStr, String pattern) {
        if (StringUtils.isEmpty(dateStr) || StringUtils.isEmpty(pattern)) {
            return null;
        }
        try {
           return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            return null;
        }
    }


    public static LocalDateTime date2LocalDateTime(Date date, ZoneId zoneId) {
        if (date == null) {
            return null;
        }
        Instant timestamp =date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(timestamp, zoneId);
        return localDateTime;
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        return date2LocalDateTime(date, ZoneId.systemDefault());
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        return localDateTime2Date(localDateTime, null);
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime, ZoneId zoneId) {
        if (localDateTime == null) {
            return null;
        }
        if (zoneId == null) {
            zoneId = ZoneId.systemDefault();
        }
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }


    /**
     * 获取当天剩余秒数
     * @return
     */
    public static long getRemainSecondsOfToday() {
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return  ChronoUnit.SECONDS.between(LocalDateTime.now(), endOfDay);
    }

    public static String getTodayStartTime() {
    	Calendar c = Calendar.getInstance();
    	c.set(Calendar.HOUR_OF_DAY, 0);
    	c.set(Calendar.MINUTE, 0);
    	c.set(Calendar.SECOND, 0);
    	return toYYYY_MM_DD_HH_MM_SS(c.getTime());
    }

    public static String getTodayEndTime() {
    	Calendar c = Calendar.getInstance();
    	c.set(Calendar.HOUR_OF_DAY, 23);
    	c.set(Calendar.MINUTE, 59);
    	c.set(Calendar.SECOND, 59);
    	return toYYYY_MM_DD_HH_MM_SS(c.getTime());
    }
    
    /**
     * 获取时间对应的统计维度所在
     * 00:00 - 00:15 - 00:30 ---- 23:45
     * @param date
     * @return
     */
    public static String getDateDimHHMM(Date date) {
    	Calendar c = Calendar.getInstance();
    	c.setTime(date);
    	
    	int hh = c.get(Calendar.HOUR_OF_DAY);
    	int mm = c.get(Calendar.MINUTE);
    	
    	String hhString = "";
    	if (hh < 10) {
    		hhString = "0" + hh;
    	} else {
    		hhString = hh + "";
    	}
    	String mmString = "";
    	if (mm < 15) {
    		mmString = "00";
    	} else  if (mm < 30) {
    		mmString = "15";
    	} else if (mm < 45) {
    		mmString = "30";
    	} else {
    		mmString = "45";
    	}
    	return hhString + ":" + mmString;
    }
    
    public static String getDateDimHHMMSS() {
    	Calendar c = Calendar.getInstance();
    	int ss = c.get(Calendar.SECOND);
    	String ssString = "";
    	if (ss < 5) {
    		ssString = "00";
    	} else if (ss < 10) {
    		ssString = "05";
    	} else if (ss < 15) {
    		ssString = "10";
    	} else if (ss < 20) {
    		ssString = "15";
    	} else if (ss < 25) {
    		ssString = "20";
    	} else if (ss < 30) {
    		ssString = "25";
    	} else if (ss < 35) {
    		ssString = "30";
    	} else if (ss < 40) {
    		ssString = "35";
    	} else if (ss < 45) {
    		ssString = "40";
    	} else if (ss < 50) {
    		ssString = "45";
    	} else if (ss < 55) {
    		ssString = "50";
    	} else if (ss < 60) {
    		ssString = "55";
    	}
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
    	String time = sdf.format(c.getTime());
    	return time + ssString;
    }
    
    public static Date getErrorRecallTime(int nowRecallTime) {
    	if (0 == nowRecallTime) {
    		Calendar calendar = Calendar.getInstance();
    		calendar.add(Calendar.MINUTE, 3);
    		return calendar.getTime();
    	} else if (1 == nowRecallTime) {
    		Calendar calendar = Calendar.getInstance();
    		calendar.add(Calendar.MINUTE, 5);
    		return calendar.getTime();
    	} else if (2 == nowRecallTime) {
    		Calendar calendar = Calendar.getInstance();
    		calendar.add(Calendar.MINUTE, 10);
    		return calendar.getTime();
    	} else {
    		//cannot be happened
    		throw new RuntimeException("nowRecallTime is not in 0, 1, 2.. nowRecallTime="+nowRecallTime);
    	}
    }
}
