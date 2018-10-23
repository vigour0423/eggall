package com.ddl.egg.log.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by mark.huang on 2017-01-05.
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    /**
     * 返回当前时间.
     *
     * @return 返回当前时间
     */
    public static Date getCurrentDateTime() {
        Calendar calNow = Calendar.getInstance();
        Date dtNow = calNow.getTime();
        return dtNow;
    }

    /**
     * Gets the today.
     *
     * @return 返回今天日期，时间部分为0。例如：2006-4-8 00:00:00
     */
    public static Date getToday() {
        return truncate(new Date(), Calendar.DATE);
    }

    /**
     * /** Gets the today statrt.
     *
     * @return 返回今天日期，时间部分为00:00:00.000。例如：2006-4-19 00:00:00.000
     */
    public static Date getTodayStart() {
        return getDayStart(new Date());
    }

    /**
     * Gets the today end.
     *
     * @return 返回今天日期，时间部分为23:59:59.999。例如：2006-4-19 23:59:59.999
     */
    public static Date getTodayEnd() {
        return getDayEnd(new Date());
    }

    /**
     * 将字符串转换为日期
     *
     * @param dateString
     * @param format
     * @return
     */
    public static Date convertToDate(String dateString, String format) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format, java.util.Locale.CHINA);
            return df.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将字符串转换为日期（不包括时间）.
     *
     * @param dateString "yyyy-MM-dd"格式的日期字符串
     * @return 日期
     */
    public static Date convertToDate(String dateString) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.CHINA);
            return df.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 检查字符串是否为日期格式yyyy-MM-dd.
     *
     * @param dateString the date string
     * @return true=是；false=否
     */
    public static boolean checkDateString(String dateString) {
        return (convertToDate(dateString) != null);
    }

    /**
     * 将字符串转换为日期（包括时间）.
     *
     * @param dateTimeString the date time string
     * @return 日期时间
     */
    public static Date convertToDateTime(String dateTimeString) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.CHINA);
            return df.parse(dateTimeString);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将字符串转换为日期（包括时间）.
     *
     * @param dateTimeString 字符串格式：yyyyMMddHHmmss the date time string
     * @return 日期时间
     */
    public static Date convertToDateTimeImage(String dateTimeString) {
        try {
            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.CHINA);
            return df.parse(dateTimeString);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 检查字符串是否为日期时间格式yyyy-MM-dd HH:mm:ss.
     *
     * @param dateTimeString the date time string
     * @return true=是；false=否
     */
    public static boolean checkDateTimeString(String dateTimeString) {
        return (convertToDateTime(dateTimeString) != null);
    }

    /**
     * 获取月底.
     *
     * @param year  年 4位年度
     * @param month 月 1~12
     * @return 月底的Date对象。例如：2006-3-31 23:59:59.999
     */
    public static Date getMonthEnd(int year, int month) {
        StringBuffer sb = new StringBuffer(10);
        Date date;
        if (month < 12) {
            sb.append(Integer.toString(year));
            sb.append("-");
            sb.append(month + 1);
            sb.append("-1");
            date = convertToDate(sb.toString());
        } else {
            sb.append(Integer.toString(year + 1));
            sb.append("-1-1");
            date = convertToDate(sb.toString());
        }
        date.setTime(date.getTime() - 1);
        return date;
    }

    /**
     * 获取月底.
     *
     * @param when 要计算月底的日期
     * @return 月底的Date对象。例如：2006-3-31 23:59:59.999
     */
    public static Date getMonthEnd(Date when) {
        Assert.notNull(when, "date must not be null !");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(when);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return getMonthEnd(year, month);
    }

    /**
     * 获取日期
     *
     * @param when 要计算日期
     * @return 月底的Date对象。例如：2015-9-14 23:00:00返回14
     */
    public static int getMonthOfDay(Date when) {
        Assert.notNull(when, "date must not be null !");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(when);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 是否月末
     *
     * @param date
     * @return
     */
    public static boolean isMonthEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            return true;
        }
        return false;
    }

    /**
     * 获取给定日的最后一刻。.
     *
     * @param when 给定日
     * @return 最后一刻。例如：2006-4-19 23:59:59.999
     */
    public static Date getDayEnd(Date when) {
        Date date = truncate(when, Calendar.DATE);
        date = addDays(date, 1);
        date.setTime(date.getTime() - 1);
        return date;
    }

    /**
     * 获取给定日的第一刻。.
     *
     * @param when 给定日
     * @return 第一刻。例如：2006-4-19 00:00:00.000
     */
    public static Date getDayStart(Date when) {
        Date date = truncate(when, Calendar.DATE);
        return date;
    }

    /**
     * 日期加法.
     *
     * @param when   被计算的日期
     * @param field  the time field. 在Calendar中定义的常数，例如Calendar.DATE
     * @param amount 加数
     * @return 计算后的日期
     */
    public static Date add(Date when, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(when);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * 计算给定的日期加上给定的天数。.
     *
     * @param when   给定的日期
     * @param amount 给定的天数
     * @return 计算后的日期
     */
    public static Date addDays(Date when, int amount) {
        return add(when, Calendar.DAY_OF_YEAR, amount);
    }

    /**
     * 计算给定的日期加上给定的周数.
     *
     * @param when   给定的日期
     * @param amount 给定的月数
     * @return 计算后的日期
     */
    public static Date addWeeks(Date when, int amount) {
        return add(when, Calendar.WEEK_OF_MONTH, amount);
    }

    /**
     * 计算给定的日期加上给定的月数。.
     *
     * @param when   给定的日期
     * @param amount 给定的月数
     * @return 计算后的日期
     */
    public static Date addMonths(Date when, int amount) {
        return add(when, Calendar.MONTH, amount);
    }

    /**
     * Format date.
     *
     * @param date   the date
     * @param format the format
     * @return the string
     */
    public static String formatDate(Date date, String format) {
        if (StringUtils.isNotBlank(format)) {
            DateFormat DATE_TIME_FORMAT = new SimpleDateFormat(format, java.util.Locale.CHINA);
            return DATE_TIME_FORMAT.format(date);
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.CHINA);
        return df.format(date);
    }

    /**
     * 获取月份：如：“一月、二月”
     *
     * @param date
     * @return
     */
    public static String getChineseMonth(Date date) {
        String[] months = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int w = calendar.get(Calendar.MONTH);
        if (w < 0) {
            w = 0;
        }
        return months[w];
    }

    /**
     * 获取周几：如“周一、周二”
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 获取星期数字并转换为中文数字 如：“日，一，二”
     *
     * @param date
     * @return
     */
    public static String getChineseWeek(Date date) {
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 获取int类型周期，周日是1,周六是7
     *
     * @param date
     * @return
     */
    public static int getIntWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int w = calendar.get(Calendar.DAY_OF_WEEK);
        return w;
    }

    /**
     * 获取本周周一
     *
     * @param date
     * @return
     */
    public static Date getMonDay(Date date) {
        date = truncate(date, Calendar.DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            calendar.add(Calendar.DAY_OF_MONTH, -7);
        }
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

    /**
     * 获取本周周日:中国习惯
     *
     * @param date
     * @return
     */
    public static Date getSumDay(Date date) {
        date = truncate(date, Calendar.DATE);
        date = addDays(date, 7);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar.getTime();
    }

    /**
     * 获取月初
     *
     * @param date 例如
     *             ：2014-06-01 00:00:00.000
     * @return
     */
    public static Date getMonthStart(Date date) {
        date = truncate(date, Calendar.DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取月初
     *
     * @param year  年 4位年度
     * @param month 月 1~12
     * @return 月底的Date对象。例如：2014-06-01 00:00:00.000
     */
    public static Date getMonthStart(int year, int month) {
        Date date = convertToDate(Integer.toString(year) + "-" + Integer.toString(month) + "-1");
        return getMonthStart(date);
    }

    /**
     * 是否月初
     *
     * @param date
     * @return
     */
    public static boolean isMonthStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 计算周期
     *
     * @param when
     * @param cycleNum
     * @param field    日周期=0，周周期=1，月周期=2
     * @return
     */
    public static List<Date> countCycle(Date when, int cycleNum, int field) {
        List<Date> list = new ArrayList<>();
        switch (field) {
            case 0:
                list = countCycleOfDay(when, cycleNum, 1);
                break;
            case 1:
                list = countCycleOfWeek(when, cycleNum, 1);
                break;
            case 2:
                list = countCycleOfMonth(when, cycleNum, 1);
                break;
        }
        return list;
    }

    /**
     * 计算日周期
     *
     * @param when     起始日期
     * @param cycleNum 周期数
     * @param step
     * @return
     */
    public static List<Date> countCycleOfDay(Date when, int cycleNum, int step) {
        List<Date> list = new ArrayList<>();
        Date date = truncate(when, Calendar.DATE);
        for (int i = 0; i < cycleNum; i = i + step) {
            Date nDate = addDays(date, i);
            list.add(nDate);
        }
        return list;
    }

    /**
     * 计算周周期
     *
     * @param when     起始日期
     * @param cycleNum 周期数
     * @param step
     * @return
     */
    public static List<Date> countCycleOfWeek(Date when, int cycleNum, int step) {
        List<Date> list = new ArrayList<>();
        Date date = getMonDay(when);
        for (int i = 0; i < cycleNum; i = i + step) {
            Date nDate = addWeeks(date, i);
            list.add(nDate);
        }
        return list;
    }

    /**
     * 计算月周期
     *
     * @param when     起始日期
     * @param cycleNum 周期数
     * @param step
     * @return
     */
    public static List<Date> countCycleOfMonth(Date when, int cycleNum, int step) {
        List<Date> list = new ArrayList<>();
        Date date = getMonthStart(when);
        for (int i = 0; i < cycleNum; i = i + step) {
            Date nDate = addMonths(date, i);
            list.add(nDate);
        }
        return list;
    }

    /**
     * 通过起始日期数组按step周一次规则返回count数量日期数组
     *
     * @param whenList 起始日期数组
     * @param count    循环次数
     * @param step     周期
     * @return
     */
    public static List<Date> dateCycleOfWeek(List<Date> whenList, int count, int step) {
        List<Date> list = new ArrayList<>();
        int circle = whenList.size();
        for (int i = 0; i < count; i++) {
            Date date = whenList.get(i % circle);
            date = addWeeks(date, step * (i / circle));
            list.add(date);
        }
        return list;
    }

    /**
     * 通过起始日期数组按每月一次规则返回count数量日期数组
     *
     * @param whenList 起始日期数组
     * @param count    循环次数
     * @return
     */
    public static List<Date> dateCycleOfMonth(List<Date> whenList, int count) {
        List<Date> list = new ArrayList<>();
        int circle = whenList.size();
        for (int i = 0; i < count; i++) {
            Date date = whenList.get(i % circle);
            Date nDate = addMonths(date, i / circle);
            list.add(nDate);
        }
        return list;
    }

    /**
     * 与当前时间进行对比，看是否过期
     *
     * @param referDate
     * @return
     */
    public static boolean isTimeover(Date referDate) {
        Date date = new Date();
        return date.getTime() - referDate.getTime() > 0;
    }

    /**
     * 计算日期差
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int daysBetween(Date startDate, Date endDate) {
        startDate = truncate(startDate, Calendar.DATE);
        endDate = truncate(endDate, Calendar.DATE);
        long between_days = (endDate.getTime() - startDate.getTime()) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * @param date
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean isBetween(Date date, Date startDate, Date endDate) {
        boolean isBetween = false;
        long a = date.getTime() - startDate.getTime();
        long b = date.getTime() - endDate.getTime();
        if (a >= 0 && b <= 0) {
            isBetween = true;
        }
        return isBetween;
    }

    /**
     * 计算指定日期和当前日期差年数
     *
     * @param startDate
     * @return
     */
    public static int getYears(Date startDate) {
        if (startDate == null) {
            return 0;
        }
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        born.setTime(startDate);
        if (born.after(now)) {
            return -1;
        }
        int age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
        if (now.get(Calendar.DAY_OF_YEAR) < born.get(Calendar.DAY_OF_YEAR)) {
            age -= 1;
        }
        return age;
    }

    /**
     * 根据相差年份计算日期
     *
     * @param years
     * @return
     */
    public static Date getDateByBetweenYears(int years) {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.add(Calendar.YEAR, -years);
        return now.getTime();
    }

    /**
     * 计算两个时间点相差的分钟数
     */
    public static int countMinutes(Date smallTime, Date bigTime) {
        long time = bigTime.getTime() - smallTime.getTime();
        String minutes = time / 1000 / 60 + "";
        return Integer.parseInt(minutes);
    }

    public static String getHourMin(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }

    /**
     * java.util.Date --> java.time.LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * java.util.Date --> java.time.LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDate dateToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    /**
     * java.util.Date --> java.time.LocalTime
     *
     * @param date
     * @return
     */
    public static LocalTime dateToLocalTime(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalTime();
    }

    /**
     * java.time.LocalDateTime --> java.util.Date
     *
     * @param localDateTime
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * java.time.LocalDate --> java.util.Date
     *
     * @param localDate
     * @return
     */
    public static Date localDateToDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * java.time.LocalTime --> java.util.Date
     */
    public static Date localTimeToDate(LocalDate localDate, LocalTime localTime) {
        if (localTime == null || localTime == null) {
            return null;
        }
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }
}
