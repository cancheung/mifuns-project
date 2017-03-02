package com.mifuns.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Stony on 2016/3/29.
 */
public abstract class DateUtils {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final String SHORT_DATE_PATTERN = "yyyyMMdd";

    /** 返回yyyy-MM-dd HH:mm:ss格式的字符串时间 */
    public static final SimpleDateFormat DATE_TIME_PATTERN_FORMAT = new SimpleDateFormat(DATE_TIME_PATTERN);
    /** 返回yyyy-MM-dd格式的字符串时间 */
    public static final SimpleDateFormat DATE_PATTERN_FORMAT = new SimpleDateFormat(DATE_PATTERN);
    /** 返回yyyyMMdd格式的字符串时间 */
    public static final SimpleDateFormat SHORT_DATE_PATTERN_FORMAT = new SimpleDateFormat(SHORT_DATE_PATTERN);
    /** 返回HH:mm:ss格式的字符串时间 */
    public static final SimpleDateFormat TIME_FORMAT_FORMAT = new SimpleDateFormat(TIME_PATTERN);

    public static final ConcurrentHashMap<String,SimpleDateFormat> PATTERN_CACHES = new ConcurrentHashMap<String,SimpleDateFormat>();
    static {
        PATTERN_CACHES.put(DATE_TIME_PATTERN, DATE_TIME_PATTERN_FORMAT);
        PATTERN_CACHES.put(DATE_PATTERN, DATE_PATTERN_FORMAT);
        PATTERN_CACHES.put(SHORT_DATE_PATTERN, SHORT_DATE_PATTERN_FORMAT);
        PATTERN_CACHES.put(TIME_PATTERN,TIME_FORMAT_FORMAT);
    }

    /**
     * 返回yyyyMMdd格式的字符串时间
     */
    public static String shortDate2string(Date date){
        return SHORT_DATE_PATTERN_FORMAT.format(date);
    }
    /**
     * 返回当天yyyyMMdd格式的字符串时间
     */
    public static String shortDate2string(){
        return shortDate2string(now());
    }
    /** 返回yyyy-MM-dd格式的字符串时间 */
    public static String date2string(Date date){
        return DATE_PATTERN_FORMAT.format(date);
    }
    /** 返回当天yyyy-MM-dd格式的字符串时间 */
    public static String date2string(){
        return date2string(now());
    }
    /** 当前时间返回yyyy-MM-dd格式的字符串时间 */
    public static String dateString(){
        return DATE_PATTERN_FORMAT.format(now());
    }

    /** 返回yyyy-MM-dd HH:mm:ss格式的字符串时间 */
    public static String dateTime2string(Date date){
        return DATE_TIME_PATTERN_FORMAT.format(date);
    }

    /**
     * 返回当前时间yyyy-MM-dd HH:mm:ss格式的字符串时间
     */
    public static String dateTimeString(){
        return DATE_TIME_PATTERN_FORMAT.format(now());
    }

    /**
     * 返回HH:mm:ss格式的字符串时间
     */
    public String time2string(Date date) {
        return TIME_FORMAT_FORMAT.format(date);
    }

    public static Date yesterday(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }
    /** 当前日期 **/
    public static Date now(){
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }
    /**
     * 下一天
     * @param date
     * @return
     */
    public static Date nextDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }
    public static Date addDay(Date date, int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
    public static Date subDay(Date date, int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, (0 - days));
        return cal.getTime();
    }

    /**
     * 减去N个小时
     * @param date
     * @param hours
     * @return
     */
    public static Date subHour(Date date, int hours){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, (0 - hours));
        return cal.getTime();
    }

    /**
     * 加N个小时
     * @param date
     * @param hours
     * @return
     */
    public static Date addHour(Date date, int hours){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, (hours));
        return cal.getTime();
    }
    /**
     *
     * @param source
     * @return yyyy-MM-dd
     */
    public static Date string2date(String source){
        try {
            return DATE_PATTERN_FORMAT.parse(source);
        } catch (ParseException e) {
            return now();
        }
    }
    /**
     *
     * @param source
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static Date string2dateTime(String source){
        try {
            return DATE_TIME_PATTERN_FORMAT.parse(source);
        } catch (ParseException e) {
            return now();
        }
    }

    /**
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat sdf = PATTERN_CACHES.get(pattern);
        if (sdf == null){
            sdf = new SimpleDateFormat(pattern);
            PATTERN_CACHES.put(pattern,sdf);
        }
        return sdf.format(date);
    }

    /**
     *
     * @param beginDate yyyy-MM-dd
     * @param endDate  yyyy-MM-dd
     * @param includeEndDate 是否包含结束日期
     * @return {@link List}
     */
    public static List<String> getDateRangeList(String beginDate, String endDate,boolean includeEndDate){
        List<String> list = new ArrayList<String>();
        if(beginDate == null || beginDate.length() == 0){
            return list;
        }
        SimpleDateFormat format = DATE_PATTERN_FORMAT;
        try {
            Date beginD = format.parse(beginDate);
            Date endD = null;
            if(endDate == null || endDate.length() == 0){
                endD = now();
            }else{
                endD = format.parse(endDate);
            }

            Calendar cal = Calendar.getInstance();
            //倒叙 不包含 endDate
            if(beginD.getTime() > endD.getTime()){
                cal.setTime(beginD);
                while(cal.getTime().getTime() > endD.getTime()){
                    list.add(date2string(cal.getTime()));
                    cal.add(Calendar.DAY_OF_MONTH, -1);
                }
                if(includeEndDate){
                    list.add(endDate);
                }
            }else{
                cal.setTime(beginD);
                while(cal.getTime().getTime() < endD.getTime()){
                    list.add(date2string(cal.getTime()));
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                }
                if(includeEndDate){
                    list.add(endDate);
                }
            }
        } catch (Exception e) {
            if(list.size() > 0) list.clear();
        }
        return list;
    }
    /**
     *
     * @param beginDate yyyy-MM-dd
     * @param endDate yyyy-MM-dd default {@link #now() }
     * @return 步进为天，不包含 endDate
     */
    public static List<String> getDateRangeList(String beginDate, String endDate){
        return getDateRangeList(beginDate,endDate,false);
    }

    public static Timestamp getCurrentTime() {
        return new Timestamp(new Date().getTime());
    }

    /**
     * 算两个日期相差的小时数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public static long dateDiff(Date startTime,Date endTime){
        //按照传入的格式生成一个simpledateformate对象
        long hour = 0;
        try {
            long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
            long nh = 1000 * 60 * 60;//一小时的毫秒数
            //long nm = 1000*60;//一分钟的毫秒数
            //long ns = 1000;//一秒钟的毫秒数long diff;try {
            //获得两个时间的毫秒时间差异
            long diff = startTime.getTime() - endTime.getTime();
            //long day = diff / nd;//计算差多少天
            hour = diff % nd / nh;//计算差多少小时
            //long min = diff % nd % nh / nm;//计算差多少分钟
            //long sec = diff % nd % nh % nm / ns;//计算差多少秒//输出结果
        }catch (Exception e){
            e.printStackTrace();
        }
        return hour;
    }
    /**
     * 方法的功能描述：获取两个日期相差的年份
     * @param
     * @return
     * @throws
     * @since v1.0
     * @auther miguangying
     * @date 2016/9/8
     */
    public static int dateDiffYear(Date startTime,Date endTime){
        Calendar c = Calendar.getInstance();
        c.setTime(startTime);
        int startYear = c.get(Calendar.YEAR);
        c.setTime(endTime);
        int endYear = c.get(Calendar.YEAR);
        return endYear-startYear;
    }
}
