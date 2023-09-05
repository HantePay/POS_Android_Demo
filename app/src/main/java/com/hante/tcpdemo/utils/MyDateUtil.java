package com.hante.tcpdemo.utils;

import android.text.TextUtils;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MyDateUtil {
    /**
     * 2016-11-08 14:39:38
     * pattern yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String showDate(Date date, String pattern) {
        String dateStr = format(date, pattern);
        String year = dateStr.substring(0, 4);
        Long yearNum = Long.parseLong(year);
        int month = Integer.parseInt(dateStr.substring(5, 7));
        int day = Integer.parseInt(dateStr.substring(8, 10));
        String hour = dateStr.substring(11, 13);
        String minute = dateStr.substring(14, 16);

        long addtime = date.getTime();
        long today = System.currentTimeMillis();//当前时间的毫秒数
        Date now = new Date(today);
        String nowStr = format(now, pattern);
        int nowDay = Integer.parseInt(nowStr.substring(8, 10));
        String result = "";
        long l = today - addtime;//当前时间与给定时间差的毫秒数
        long days = l / (24 * 60 * 60 * 1000);//这个时间相差的天数整数，大于1天为前天的时间了，小于24小时则为昨天和今天的时间
        long hours = (l / (60 * 60 * 1000) - days * 24);//这个时间相差的减去天数的小时数
        long min = ((l / (60 * 1000)) - days * 24 * 60 - hours * 60);//
        long s = (l / 1000 - days * 24 * 60 * 60 - hours * 60 * 60 - min * 60);
        if (days > 0) {
            if (days > 0 && days < 2) {
//                result ="前天"+hour+"点"+minute+"分";
                result = "前天" + hour + ":" + minute;
            } else {
                result = yearNum % 100 + "年" + month + "月 " + day + "日" + hour + "点" + minute + "分";
            }
        } else if (hours > 0) {
            if (day != nowDay) {
//                result = "昨天"+hour+"点"+minute+"分";
                result = "昨天" + hour + ":" + minute;
            } else {
                result = hours + "小时 前";
            }
        } else if (min > 0) {
            if (min > 0 && min < 15) {
                result = "刚刚";
            } else {
                result = min + "分 前";
            }
        } else {
            result = s + "秒 前";
        }
        return result;
    }

    public static String showDateInterval(String dateStr, String timeZone, boolean showMinute) {
        //2019-08-22 00:40:00
        String result = "";
        if (dateStr.length() == 19) {
            result = dateStr.substring(0, 16);
        } else {
            result = dateStr;
        }
        long yearNum = 0;
        int month = 0;
        int day = 0;
        String hour = "";
        String minute = "";

        //获得指定时区当天结束时间
        SimpleDateFormat sdf_end = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf_start = new SimpleDateFormat("yyyy-MM-dd");
        sdf_end.setTimeZone(TimeZone.getTimeZone(timeZone));
        sdf_start.setTimeZone(TimeZone.getTimeZone(timeZone));
        String StartDate = sdf_start.format(new Date());
//        String EndDate = sdf_end.format(new Date()) + " 23:59:59";
        String EndDate = sdf_end.format(new Date());
        String pattern = "";
        if (showMinute) {
            yearNum = Long.parseLong(dateStr.substring(0, 4));
            month = Integer.parseInt(dateStr.substring(5, 7));
            day = Integer.parseInt(dateStr.substring(8, 10));
            hour = dateStr.substring(11, 13);
            minute = dateStr.substring(14, 16);
//            StartDate = StartDate + " 00:00:00";
//            EndDate = EndDate + " 23:59:59";
            StartDate = StartDate + " 00:00";
            EndDate = EndDate + " 23:59";
//            pattern = "yyyy-MM-dd HH:mm:ss";
            pattern = "yyyy-MM-dd HH:mm";
        } else {
            pattern = "yyyy-MM-dd";
        }

//        LogUtil.e("StartDate======" + StartDate);
//        LogUtil.e("EndDate======" + EndDate);

        //昨天
        String yesterday_startDate = "";
        String yesterday_endDate = "";
        try {
            long start_long = DateChangedUtil.stringToLong(StartDate, pattern);
            long end_long = DateChangedUtil.stringToLong(EndDate, pattern);
            yesterday_startDate = DateChangedUtil.longToString(start_long - 86400000L, pattern);
            yesterday_endDate = DateChangedUtil.longToString(end_long - 86400000L, pattern);
//            LogUtil.e("yesterday_startDate======" + yesterday_startDate);
//            LogUtil.e("yesterday_endDate======" + yesterday_endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //比较日期大小
        //今天
        String today ="today";
        String yesterday ="yesterday";
        String setLanguageLocale = "en";
        if ("en".equals(setLanguageLocale) || "en_US".equals(setLanguageLocale)) {
            today = "Today";
            yesterday = "Yesterday ";
        } else if ("zh_CN".equals(setLanguageLocale)) {
            today = "今天 ";
            yesterday = "昨天 ";
        }
        if (isDate2Bigger(StartDate, dateStr, pattern) &&
                isDate2Bigger(dateStr, EndDate, pattern)) {
            if (showMinute) {
                return today + " " + hour + ":" + minute;
            } else {

                return today;
            }
        } else if (isDate2Bigger(yesterday_startDate, dateStr, pattern) &&
                isDate2Bigger(dateStr, yesterday_endDate, pattern)) {
            if (showMinute) {
                result = yesterday + " " + hour + ":" + minute;
            } else {
                result = yesterday;
            }

        }
        return result;
    }




    /**
     * 日期格式化
     *
     * @param date    需要格式化的日期
     * @param pattern 时间格式，如：yyyy-MM-dd HH:mm:ss
     * @return 返回格式化后的时间字符串
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 根据毫秒时间戳来格式化字符串
     * 今天显示今天、昨天显示昨天、前天显示前天.
     * 早于前天的显示具体年-月-日，如2017-06-12；
     *
     * @param timeStamp 毫秒值
     * @return 今天 昨天 前天 或者 yyyy-MM-dd HH:mm:ss类型字符串
     */
    public static String format(long timeStamp) {
        long curTimeMillis = System.currentTimeMillis();
        Date curDate = new Date(curTimeMillis);
        int todayHoursSeconds = curDate.getHours() * 60 * 60;
        int todayMinutesSeconds = curDate.getMinutes() * 60;
        int todaySeconds = curDate.getSeconds();
        int todayMillis = (todayHoursSeconds + todayMinutesSeconds + todaySeconds) * 1000;
        long todayStartMillis = curTimeMillis - todayMillis;
        if (timeStamp >= todayStartMillis) {
            return "今天";
        }
        int oneDayMillis = 24 * 60 * 60 * 1000;
        long yesterdayStartMilis = todayStartMillis - oneDayMillis;
        if (timeStamp >= yesterdayStartMilis) {
            return "昨天";
        }
        long yesterdayBeforeStartMilis = yesterdayStartMilis - oneDayMillis;
        if (timeStamp >= yesterdayBeforeStartMilis) {
            return "前天";
        }
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(timeStamp));
    }

    /**
     * 根据时间戳来判断当前的时间是几天前,几分钟,刚刚
     *
     * @param long_time
     * @return
     */
    public static String getTimeStateNew(String long_time) {
        String long_by_13 = "1000000000000";
        String long_by_10 = "1000000000";
        if (Long.valueOf(long_time) / Long.valueOf(long_by_13) < 1) {
            if (Long.valueOf(long_time) / Long.valueOf(long_by_10) >= 1) {
                long_time = long_time + "000";
            }
        }
        Timestamp time = new Timestamp(Long.valueOf(long_time));
        Timestamp now = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//    System.out.println("传递过来的时间:"+format.format(time));
//    System.out.println("现在的时间:"+format.format(now));
        long day_conver = 1000 * 60 * 60 * 24;
        long hour_conver = 1000 * 60 * 60;
        long min_conver = 1000 * 60;
        long time_conver = now.getTime() - time.getTime();
        long temp_conver;
//    System.out.println("天数:"+time_conver/day_conver);
        if ((time_conver / day_conver) < 3) {
            temp_conver = time_conver / day_conver;
            if (temp_conver <= 2 && temp_conver >= 1) {
                return temp_conver + "天前";
            } else {
                temp_conver = (time_conver / hour_conver);
                if (temp_conver >= 1) {
                    return temp_conver + "小时前";
                } else {
                    temp_conver = (time_conver / min_conver);
                    if (temp_conver >= 1) {
                        return temp_conver + "分钟前";
                    } else {
                        return "刚刚";
                    }
                }
            }
        } else {
            return format.format(time);
        }
    }

    /**
     * 转制定时区的时间
     *
     * @param dateStr
     */
    public static String Date2TimeZone(String dateStr, String timeZone) {
        String timeZoneStr = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//默认
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));//转换为当前时区的时间
        SimpleDateFormat newSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//需要转换的模式
        newSdf.setTimeZone(TimeZone.getTimeZone(timeZone));//需要转换的时区
        try {
            if(!TextUtils.isEmpty(dateStr)){
                Date date = sdf.parse(dateStr);
                //转换为utc时间
//            Date date2= new Date(RxTimeTool.string2Milliseconds(dateStr));
//            LogUtil.e("date2=="+date2);
                timeZoneStr = newSdf.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeZoneStr;
    }

    /**
     * 转制定时区的时间
     *
     * @param dateStr
     */
    public static String Date2TimeZone(String dateStr, String timeZone, String needPattern) {
        String timeZoneStr = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//默认
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));//转换为当前时区的时间
        SimpleDateFormat newSdf = new SimpleDateFormat(needPattern);//需要转换的模式
        newSdf.setTimeZone(TimeZone.getTimeZone(timeZone));//需要转换的时区
        try {
            Date date = sdf.parse(dateStr);
            //转换为utc时间
//            Date date2 = new Date(RxTimeTool.string2Milliseconds(dateStr));
//            LogUtil.e(" date2 == "+date2);
            timeZoneStr = newSdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeZoneStr;
    }

    //指定时区当天的开始时间转UTC
    public static String getUTCDayBegin(String timeZone) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        String text = sdf.format(new Date()) + "000000";
//        LogUtil.e("begin====="+text);
        String utcBegin = switchTimeZone(text, timeZone, "UTC");
        return utcBegin;
    }

    public static String getUTCDayBegin2(String timeZone) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        String text = sdf.format(new Date());
//        LogUtil.e("begin====="+text);
        String utcBegin = switchTimeZone(text, timeZone, "UTC","yyyy-MM-dd");
        return utcBegin;
    }
    //指定时区某天的开始时间转UTC
    public static String getUTCDayBegin(String date, String timeZone) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
//        String text = sdf.format(new Date()) + "000000";
        String text = date + "000000";
        String utcBegin = switchTimeZone(text, timeZone, "UTC");
        return utcBegin;
    }

    //指定时区某天的结束时间转UTC
    public static String getUTCDayEnd(String date, String timeZone) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
//        String text = sdf.format(new Date()) + "235959";
        String text = date + "235959";
        String utcEnd = switchTimeZone(text, timeZone, "UTC");

        return utcEnd;

    }

    //指定时区当天的结束时间转UTC
    public static String getUTCDayEnd(String timeZone) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        String text = sdf.format(new Date()) + "235959";
        String utcEnd = switchTimeZone(text, timeZone, "UTC");

        return utcEnd;

    }
    public static String getUTCDayEnd2(String timeZone) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        String text = sdf.format(new Date()) ;
        String utcEnd = switchTimeZone(text, timeZone, "UTC","yyyy-MM-dd");

        return utcEnd;

    }
    //指定时区某天的结束时间转UTC   指定格式"yyyyMMdd"
    public static String getTimeZoneTheDay(String timeZone, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        return sdf.format(new Date());

    }


    /**
     * 时间时区转换
     *
     * @param time
     * @param localTimeZone 需转换时间的时区
     * @param needTimeZone  转到哪个时区
     * @return
     * @throws ParseException
     */
    public static String switchTimeZone(String time, String localTimeZone, String needTimeZone) throws ParseException {
        SimpleDateFormat locSdf = new SimpleDateFormat("yyyyMMddHHmmss");
        locSdf.setTimeZone(TimeZone.getTimeZone(localTimeZone));
        SimpleDateFormat ndSdf = new SimpleDateFormat("yyyyMMddHHmmss");
        ndSdf.setTimeZone(TimeZone.getTimeZone(needTimeZone));
        Date a = locSdf.parse(time);
        String switchTime = ndSdf.format(a);
        return switchTime;
    }

    public static String switchTimeZone(String time, String localTimeZone, String needTimeZone, String pattern) throws ParseException {
        SimpleDateFormat locSdf = new SimpleDateFormat(pattern);
        locSdf.setTimeZone(TimeZone.getTimeZone(localTimeZone));
        SimpleDateFormat ndSdf = new SimpleDateFormat(pattern);
        ndSdf.setTimeZone(TimeZone.getTimeZone(needTimeZone));
        Date a = locSdf.parse(time);
        String switchTime = ndSdf.format(a);
        return switchTime;
    }

    public static String switchTimeZone(String time, String localTimeZone, String needTimeZone, String Oldpattern, String Newpattern) throws ParseException {
        SimpleDateFormat locSdf = new SimpleDateFormat(Oldpattern);
        locSdf.setTimeZone(TimeZone.getTimeZone(localTimeZone));
        SimpleDateFormat ndSdf = new SimpleDateFormat(Newpattern);
        ndSdf.setTimeZone(TimeZone.getTimeZone(needTimeZone));
        Date a = locSdf.parse(time);
        String switchTime = ndSdf.format(a);
        return switchTime;
    }

    /**
     * 当地时间 ---> UTC时间
     *
     * @return
     */
    public static String Local2UTC() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = sdf.format(new Date());
        return gmtTime;
    }

    /**
     * UTC时间 ---> 当地时间
     *
     * @param utcTime UTC时间
     * @return
     */
    public static String utc2Local(String utcTime) {
        SimpleDateFormat utcFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//UTC时间格式
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//当地时间格式
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUTCDate.getTime());
        return localTime;
    }

    /**
     * 比较两个日期的大小，日期格式为yyyy-MM-dd
     *
     * @param str1 the first date
     * @param str2 the second date
     * @return true <br/>false
     */
    public static boolean isDateOneBigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

    /**
     * 比较两个日期的大小，日期格式为yyyy-MM-dd
     *
     * @param str1 the first date
     * @param str2 the second date
     * @return true <br/>false
     */
    public static boolean isDate2Bigger(String str1, String str2, String pattenr) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat(pattenr);
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = false;
        } else if (dt1.getTime() <= dt2.getTime()) {
            isBigger = true;
        }
        return isBigger;
    }


    private static long string2Milliseconds(String time, SimpleDateFormat format){
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
