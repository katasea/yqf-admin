/*
 * Copyright (c) 2005, 2015, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

/**
 * 
 * 描述
 * 
 * @author Fandy Liu
 * @created 2015年4月8日 上午11:49:22
 */
public class DateTimeUtil {
    /**
     * 
     */
    private static int MaxDate;// 一月最大天数
    /**
     * 
     */
    private static int MaxYear;// 一年最大天数

    /**
     * 获取当前年份
     * 
     * @return
     */
    public static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取当前月
     * 
     * @return
     */
    public static int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取传入月份所处季度的第一天
     * 
     * @param month
     *            :传入的月份
     * @return
     */
    public static String getThisSeasonFirstDay(int month) {
        String array[][] = { { "01", "02", "03" }, { "04", "05", "06" }, { "07", "08", "09" }, { "10", "11", "12" } };
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        String start_month = array[season - 1][0];
        String end_month = array[season - 1][2];
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        String start_days = "01";// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        String seasonDate = years_value + "-" + start_month + "-" + start_days;
        return seasonDate;
    }

    /**
     * 获取传入月份所处季度的最后一天
     * 
     * @param month
     *            :传入月份
     * @return
     */
    public static String getThisSeasonEndDay(int month) {
        int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        int start_month = array[season - 1][0];
        int end_month = array[season - 1][2];
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        int start_days = 1;// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        int end_days = getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + end_month + "-" + end_days;
        return seasonDate;
    }

    /**
     * 获取某季度的第一天
     * 
     * @param years_value
     *            年份
     * @param season
     *            季度
     * @return
     */
    public static String getFirstDayBySeason(String years_value, String season) {
        String array[][] = { { "01", "02", "03" }, { "04", "05", "06" }, { "07", "08", "09" }, { "10", "11", "12" } };
        String start_month = array[Integer.valueOf(season) - 1][0];
        String start_days = "01";
        String seasonDate = years_value + "-" + start_month + "-" + start_days;
        return seasonDate;
    }

    /**
     * 获取某季度的第一天
     * 
     * @param years_value
     *            年份
     * @param season
     *            季度
     * @return
     */
    public static String getEndDayBySeason(String years_value, String season) {
        int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
        int end_month = array[Integer.valueOf(season) - 1][2];
        int end_days = getLastDayOfMonth(Integer.valueOf(years_value), end_month);
        String seasonDate = years_value + "-" + end_month + "-" + end_days;
        return seasonDate;
    }

    /**
     * 获取传入月份所处季度的起始时间和结束时间
     * 
     * @param month
     * @return
     */
    public static String getThisSeasonTime(int month) {
        int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        int start_month = array[season - 1][0];
        int end_month = array[season - 1][2];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);

        int start_days = 1;// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        int end_days = getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + start_month + "-" + start_days + ";" + years_value + "-" + end_month
                + "-" + end_days;
        return seasonDate;

    }

    /**
     * 获取某年某月的最后一天
     * 
     * @param year
     *            年
     * @param month
     *            月
     * @return 最后一天
     */
    private static int getLastDayOfMonth(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        }
        return 0;
    }

    /**
     * 是否闰年
     * 
     * @param year
     *            年
     * @return
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * 根据日期获取季度
     * 
     * @param cal
     * @return
     */
    public static int getSeason(Date date) {
        int month = date.getMonth();
        return (month + 3) / 3;
    }

    /**
     * 日期格式化
     * 
     * @param c
     * @param pattern
     * @return
     */
    public static Date format(String strDate, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        ParsePosition pos = new ParsePosition(0);
        // Date strtodate = formatter.parse(strDate, pos);
        Date strtodate = null;
        try {
            strtodate = formatter.parse(strDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strtodate;
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     * 
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        // Date strtodate = formatter.parse(strDate, pos);
        Date strtodate = null;
        try {
            strtodate = formatter.parse(strDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strtodate;
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     * 
     * @param dateDate
     * @param k
     * @return
     */
    public static String dateToStr(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 取得两个时间段的间隔天数
     * 
     * @param beginDate
     *            开始时间
     * @param endDate
     *            结束时间
     * @return 间隔天数
     * @throws ParseException
     */
    public static int getDaysBetween(String beginDate, String endDate) {
        Date bDate = strToDate(beginDate);
        Date eDate = strToDate(endDate);
        Calendar d1 = new GregorianCalendar();
        Calendar d2 = new GregorianCalendar();
        if (bDate.before(eDate)) {
            d1.setTime(bDate);
            d2.setTime(eDate);
        } else {
            d1.setTime(eDate);
            d2.setTime(bDate);
        }
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);// 得到当年的实际天数
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        if (bDate.after(eDate)) {
            days = -days;
        }
        return days;
    }

    /**
     * 描述 根据格式化类型将指定日期时间的格式化成相应的字符串形式的(y-年,M-月,d-日,H-时,m-分，s-秒,S-毫秒)日期时间;
     * 
     * @author Session Zhang
     * @created Nov 4, 2014 10:20:39 AM
     * @param date
     *            Date 指定的日期
     * @param format
     *            String 格式字符串
     * @return
     */
    public static String getCntDtStr(Date date, String format) {
        SimpleDateFormat dtFmt = new SimpleDateFormat(format);
        String nowDtStr = dtFmt.format(date);
        return nowDtStr;
    }

    /**
     * 输出两个间隔时间段的所有季度的开始时间和结束时间
     * 
     * @param beginYear
     *            开始年份 yyyy
     * @param beginDate
     *            开始日期 MM-dd
     * @param endYear
     *            结束年份 yyyy
     * @param endDate
     *            结束日期 MM-dd
     * @return
     * @throws ParseException
     */
    public static List<String> getSeasonBeginTime(String beginYear, String beginDate, String endYear, String endDate)
            throws ParseException {

        String array[] = { "01-01", "04-01", "07-01", "10-01" };
        String begin = beginYear + "-" + beginDate;
        String end = endYear + "-" + endDate;
        // 开始时间在第几季度
        int bQuar = getSeason(strToDate(begin));

        // 结束时间在第几季度
        int eQuar = getSeason(strToDate(end));

        List<String> times = new ArrayList<String>();

        int days = 0;// 时间段的天数
        days = getDaysBetween(begin, end);

        // 大于一个季度的天数
        if (days > 91) {
            // 计算间两时间段相隔几年
            int years = Integer.parseInt(endYear) - Integer.parseInt(beginYear);

            if (years == 0) {// 间隔年份在一年内
                for (int i = bQuar; i < array.length; i++) {
                    Date d1 = strToDate(endYear + "-" + array[i]);// 季度时间
                    if (d1.getTime() > strToDate(end).getTime()) {// 剩余季度的开始时间如果大于结束的时间
                        times.add(end);
                        return times;
                    } else {
                        times.add(endYear + "-" + array[i]);
                    }
                    ;

                }
                times.add(end);
                return times;
            } else {// 间隔年份超过1年

                // 添加开始时间所在年份的剩余季度开始时间
                for (int i = bQuar; i < array.length; i++) {
                    times.add(beginYear + "-" + array[i]);
                }
                // 添加间隔年份的所有季度开始时间
                int flag = years;
                for (int i = 0; i < years; i++, flag--) {
                    int temp = array.length;
                    if (flag == 1) {
                        temp = eQuar;
                    }
                    for (int j = 0; j < temp; j++) {
                        Integer aTemp = Integer.parseInt(beginYear) + i + 1;
                        times.add(aTemp.toString() + "-" + array[j]);
                    }
                }
                boolean check = true;
                for (int i = 0; i < array.length; i++) {
                    if (endDate.equals(array[i])) {
                        check = false;
                        break;
                    }
                }
                if (check)
                    times.add(end);

                return times;
            }

        } else {// 小于一个季度
            times.add(beginYear + "-" + array[bQuar]);
            return times;
        }

    }

    /**
     * 得到一个时间延后或前移几天的时间
     * 
     * @param nowdate
     *            当前时间
     * @param delay
     *            前移或后延的天数
     * @return
     */
    public static Date getNextDay(Date nowdate, int delay) {
        try {
            long myTime = (nowdate.getTime() / 1000) + delay * 24 * 60 * 60;
            nowdate.setTime(myTime * 1000);
            return nowdate;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 得到一个时间前或后n 分钟/小时/小时的时间（前时传递n的参数为负数）
     * 
     * @param date
     * @param n
     *            偏移量
     * @param type
     *            偏移类型：m 分钟 h小时 d天数
     * @return
     */
    public static Date getBeforeNDate(Date date, int n, String type) {
        // 偏移量，给定n天的毫秒数
        long offset = n * 24 * 60 * 60 * 1000; // 默认是天

        if ("m".equals(type)) { // 分钟
            offset = n * 60 * 1000;
        } else if ("h".equals(type)) {// 小时
            offset = n * 60 * 60 * 1000;
        } else if ("d".equals(type)) { // 天
            offset = n * 24 * 60 * 60 * 1000;
        }
        Calendar c = Calendar.getInstance();

        if (date != null) {
            c.setTime(date);
            c.setTimeInMillis(c.getTimeInMillis() + offset);
        }
        return c.getTime();
    }

    /**
     * 返回提醒间隔的时间长整型
     * 
     * @param cycleInterval
     *            间隔时间（数字）
     * @param intervalType
     *            分 小时 天
     * @return
     */
    public static Long getInterval(String cycleInterval, String intervalType) {
        // 计算时间间隔
        long interval = 0;
        if ("m".equals(intervalType)) {// 分钟
            interval = NumberUtils.toLong(cycleInterval) * 1000 * 60;
        } else if ("h".equals(intervalType)) {// 小时
            interval = NumberUtils.toLong(cycleInterval) * 1000 * 60 * 60;
        } else if ("d".equals(intervalType)) {// 天
            interval = NumberUtils.toLong(cycleInterval) * 1000 * 60 * 60 * 24;
        }
        return interval;
    }

    /**
     * 根据一个日期，返回是星期几的字符串
     * 
     * @param sdate
     * @return
     */
    public static String getWeek(String sdate) {
        // 再转换为时间
        Date date = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    /**
     * 获取当前月的第一天
     * 
     * @return
     */
    public static String getCurrentMonthFirstDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 计算当月最后一天,返回字符串
     * 
     * @return
     */
    public static String getCurrentMonthLastDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }
    
    
    /**
     * 某一个月第一天和最后一天
     * @param date
     * @return
     */
    public static Map<String, String> getFirstday_Lastday_Month(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //calendar.add(Calendar.MONTH, -1);
        Date theDate = calendar.getTime();
        
        //上个月第一天
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
        day_first = str.toString();

        //上个月最后一天
        calendar.add(Calendar.MONTH, 1);    //加一个月
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
        String day_last = df.format(calendar.getTime());
        StringBuffer endStr = new StringBuffer().append(day_last).append(" 23:59:59");
        day_last = endStr.toString();

        Map<String, String> map = new HashMap<String, String>();
        map.put("first", day_first);
        map.put("last", day_last);
        return map;
    }

    /**
     * 获取上月第一天
     * 
     * @return
     */
    public static String getPreviousMonthFirstDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, -1);// 减一个月，变为下月的1号
        // lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获得当前日期与本周日相差的天数
     * 
     * @return
     */
    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    /**
     * 获得本周星期日的日期
     * 
     * @return
     */
    public static String getSundayOfCurrentWeekday() {
        int weeks = 0;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获得本周一的日期
     * 
     * @return
     */
    public String getMondayOfCurrentWeek() {
        int weeks = 0;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获得上周星期日的日期
     * 
     * @return
     */
    public static String getSundayOfPreviousWeek() {
        int weeks = 0;
        weeks--;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获得上周星期一的日期
     * 
     * @return
     */
    public static String getMondayOfPreviousWeek() {
        int weeks = 0;
        weeks--;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获得下周星期一的日期
     * 
     * @return
     */
    public static String getMondayOfNextWeek() {
        int weeks = 0;
        weeks++;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获得下周星期日的日期
     * 
     * @return
     */
    public static String getSundayOfNextWeek() {

        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获得上月最后一天的日期
     * 
     * @return
     */
    public static String getPreviousMonthEndDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获得下个月第一天的日期
     * 
     * @return
     */
    public static String getNextMonthFirstDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获得下个月最后一天的日期
     * 
     * @return
     */
    public static String getNextMonthEndDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);// 加一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获得明年最后一天的日期
     * 
     * @return
     */
    public static String getNextYearEndDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);// 加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        lastDate.roll(Calendar.DAY_OF_YEAR, -1);
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获得明年第一天的日期
     * 
     * @return
     */
    public static String getNextYearFirstDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);// 加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        str = sdf.format(lastDate.getTime());
        return str;

    }

    /**
     * 获得本年有多少天
     * 
     * @return
     */
    private static int getMaxYear() {
        Calendar cd = Calendar.getInstance();
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        return MaxYear;
    }

    private static int getYearPlus() {
        Calendar cd = Calendar.getInstance();
        int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// 获得当天是一年中的第几天
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        if (yearOfNumber == 1) {
            return -MaxYear;
        } else {
            return 1 - yearOfNumber;
        }
    }

    /**
     * 获得本年第一天的日期
     * 
     * @return
     */
    public static String getCurrentYearFirstDay() {
        int yearPlus = getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus);
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        return preYearDay;
    }

    /**
     * 获得本年最后一天的日期
     * 
     * @return
     */
    public static String getCurrentYearEndDay() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        return years + "-12-31";
    }

    /**
     * 获得去年第一天的日期
     * 
     * @return
     */
    public static String getPreviousYearFirstDay() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        years_value--;
        return years_value + "-1-1";
    }

    /**
     * 获得上年最后一天的日期
     * 
     * @return
     */
    public static String getPreviousYearEndDay() {
        int weeks = 0;
        weeks--;
        int yearPlus = getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus + MaxYear * weeks + (MaxYear - 1));
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        getThisSeasonTime(11);
        return preYearDay;
    }

    /**
     * 获取当前月的上一季度的第一天
     * 
     * @param month
     * @return
     */
    public static String getLastSeasonFirstDay(int month) {
        int targetYear = DateTimeUtil.getCurrentYear();
        String targetMonth = null;
        if (month >= 1 && month <= 3) {
            targetYear -= 1;
            targetMonth = "10";
        } else if (month >= 4 && month <= 6) {
            targetMonth = "01";

        } else if (month >= 7 && month <= 10) {
            targetMonth = "04";
        } else {
            targetMonth = "07";
        }
        return targetYear + "-" + targetMonth + "-01";
    }

    /**
     * 获取Date类型格式化后的字符串 time:传入的时间 format:传入的格式化样式:比如:yyyy-MM-dd HH:mm:ss
     */
    public static String getStrOfDate(Date time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }

    /**
     * 根据传入的时间和格式获取而得到时间类型
     * 
     * @param time
     * @param format
     * @return
     */
    public static Date getDateOfStr(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取两个时间的间隔分钟
     * 
     * @param beginTime
     * @param endTime
     * @return
     */
    public static long getIntervalMinute(String beginTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date beginDate = null;
        Date endDate = null;
        try {
            beginDate = sdf.parse(beginTime);
            endDate = sdf.parse(endTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 默认为毫秒，除以1000是为了转换成秒
        long interval = (endDate.getTime() - beginDate.getTime()) / 1000;// 秒
        long hour = interval % (24 * 3600) / 3600;// 小时
        long minute = interval / 60;// 分钟
        long day = interval / (24 * 3600);// 天
        long second = interval % 60;// 秒
        return minute;
    }

    /**
     * 根据字符串时间类型获取年份
     * 
     * @param date
     * @return
     */
    public static int getYear(String date) {
        Date time = getDateOfStr(date, "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 根据日期获取月份
     * 
     * @param date
     * @return
     */
    public static int getMonth(String date) {
        Date time = getDateOfStr(date, "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 根据字符串时间获取日
     * 
     * @param date
     * @return
     */
    public static int getDay(String date) {
        Date time = getDateOfStr(date, "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return cal.get(Calendar.DATE);
    }

    /**
     * 获取某年第一天日期
     * 
     * @param year
     *            年份
     * @return Date
     */
    public static Date getCurrYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     * 
     * @param year
     *            年份
     * @return Date
     */
    public static Date getCurrYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }

    public static String getHours(Long minutes) {
        Long hours = minutes / 60;
        Long minu = minutes % 60;
        if (minu != 0) {
            return hours + "小时" + minu + "分钟";
        } else {
            return hours + "小时";
        }
    }

    /***
     * 获取两个时间的间隔时间,以字符串返回
     * 
     * @author huyu
     * @param beginTime
     * @param endTime
     * @return
     */
    public static String getInternalTime(String beginTime, String endTime) {
        String timeConsuming = "";
        Long internal = null;
        internal = DateTimeUtil.getIntervalMinute(beginTime, endTime);
        if (internal >= 0 && internal < 60) {
            timeConsuming = internal + "分钟";
        } else if (internal >= 60 && internal < 1440) {
            // 求多少小时
            int hours = internal.intValue() / 60;
            // 求多少分钟
            int minutes = internal.intValue() % 60;
            timeConsuming = hours + "小时" + minutes + "分钟";
        } else if (internal >= 1440) {
            // 求出天数
            int days = internal.intValue() / 1440;
            // 求出小时数量
            int hours = (internal.intValue() - (days * 1440)) / 60;
            // 求出分钟数量
            int minutes = (internal.intValue() - (days * 1440)) % 60;
            timeConsuming = days + "天" + hours + "小时" + minutes + "分钟";
        }
        return timeConsuming;
    }
}
