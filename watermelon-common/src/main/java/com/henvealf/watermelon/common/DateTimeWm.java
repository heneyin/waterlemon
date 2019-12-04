package com.henvealf.watermelon.common;


import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * base on java7
 * 日期时间 watermelon
 * @author hongliang.yin/Henvealf on 2018/7/22
 */
public class DateTimeWm {

    public static final String DATE_FORMAT_STRING = "yyyy-MM-dd";
    public static final String DATE_SECONDS_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_Millisecond_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss.S";

    public static final Pattern DATE_REGULAR_COMPILE = Pattern.compile("^\\\\d{4}-((0[1-9])|(1[0-2]))-((0[1-9]|[1-2][0-9])|(3[0-1]))$");

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STRING);
    public static final SimpleDateFormat SIMPLE_DATE_SECONDS_FORMAT = new SimpleDateFormat(DATE_SECONDS_FORMAT_STRING);

    public static final FastDateFormat FAST_DATE_FORMAT = FastDateFormat.getInstance(DATE_FORMAT_STRING);
    public static final FastDateFormat FAST_DATE_SECONDS_FORMAT = FastDateFormat.getInstance(DATE_SECONDS_FORMAT_STRING);

    //-------------------------------------------------------------------------
    /**
     * 获取本周星期一零点的Date对象
     * @return Date 类型
     */
    public static Date getCurrentWeekMondayZeroDate() {
        // 设置为French之后，每个星期的第一天就为星期一。
        Calendar cal = Calendar.getInstance(Locale.FRENCH);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        setTimeZone(cal);
        return cal.getTime();
    }

    /**
     * 获取本周星期一的日期。
     *
     * @return 默认格式 yyyy-MM-dd 的字符串
     */
    public static String getCurrentWeekMondayZeroDateString() {
        return FAST_DATE_FORMAT.format(getCurrentWeekMondayZeroDate());
    }

    /**
     * 获取本周星期一的日期,精确到秒。
     *
     * @return 默认格式 yyyy-MM-dd HH:mm:ss 的字符串
     */
    public static String getCurrentWeekMondayZeroDateSecondString() {
        return FAST_DATE_SECONDS_FORMAT.format(getCurrentWeekMondayZeroDate());
    }

    /**
     * 获取本周星期一,自定义格式。
     *
     * @param dateFormat 日期格式化格式
     * @return dateFormat 格式化之后的日期字符串
     */
    public static String getCurrentWeekMondayZeroDateString(String dateFormat) {
        FastDateFormat aDateFormat = FastDateFormat.getInstance(DATE_FORMAT_STRING);
        return aDateFormat.format(getCurrentWeekMondayZeroDate());
    }

    //-------------------------------------------------------------------------
    /**
     * 获得本月第一天凌晨时的Date对象。
     *
     * @return Date
     */
    public static Date getCurrentMonthFirstDayZeroDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        setTimeZone(cal);
        return cal.getTime();
    }

    /**
     * 获得本月第一天凌晨的日期字符串。
     *
     * @return 默认格式 yyyy-MM-dd 的字符串
     */
    public static String getCurrentMonthFirstDayZeroDateString() {
        return FAST_DATE_FORMAT.format(getCurrentMonthFirstDayZeroDate());
    }

    /**
     * 获得本月第一天凌晨的日期字符串。
     *
     * @return 默认格式 yyyy-MM-dd 的字符串
     */
    public static String getCurrentMonthFirstDayZeroDateSecondString() {
        return FAST_DATE_SECONDS_FORMAT.format(getCurrentMonthFirstDayZeroDate());
    }

    /**
     * 获得本月第一天凌晨的日期或者时间字符串。
     *
     * @return 格式化规则
     */
    public static String getCurrentMonthFirstDayZeroDate(String dateFormat) {
        FastDateFormat aDateFormat = FastDateFormat.getInstance(DATE_FORMAT_STRING);
        return aDateFormat.format(getCurrentMonthFirstDayZeroDate());
    }

    //-------------------------------------------------------------------------
    /**
     * 获得本季度第一天凌晨的Date对象
     */
    public static Date getCurrentQuarterFirstDayZeroDate() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        if (currentMonth >= 1 && currentMonth <= 3) {
            c.set(Calendar.MONTH, 0);
        } else if (currentMonth >= 4 && currentMonth <= 6) {
            c.set(Calendar.MONTH, 3);
        } else if (currentMonth >= 7 && currentMonth <= 9) {
            c.set(Calendar.MONTH, 6);
        } else if (currentMonth >= 10 && currentMonth <= 12) {
            c.set(Calendar.MONTH, 9);
        }
        c.set(Calendar.DATE, 1);
        setTimeZone(c);

        return c.getTime();
    }

    /**
     * 获取本季度第一天凌晨的时间字符串
     * @return
     */
    public static String getCurrentQuarterFirstDayZeroDateString() {
        return FAST_DATE_FORMAT.format(getCurrentQuarterFirstDayZeroDate());
    }

    /**
     * 获取本季度第一天凌晨的时间字符串
     * @return
     */
    public static String getCurrentQuarterFirstDayZeroDateSecondString() {
        return FAST_DATE_SECONDS_FORMAT.format(getCurrentQuarterFirstDayZeroDate());
    }

    /**
     * 获取本季度第一天凌晨的时间字符串，需要自定义时间格式
     * @return
     */
    public static String getCurrentQuarterFirstDayZeroDateString(String dateFormat) {
        FastDateFormat aDateFormat = FastDateFormat.getInstance(DATE_FORMAT_STRING);
        return aDateFormat.format(getCurrentQuarterFirstDayZeroDate());
    }

    //-------------------------------------------------------------------------
    /**
     * 获取本年的第一天凌晨的日期对象
     */
    public static Date getCurrentYearFirstDayZeroDate() {
        Calendar currCal = Calendar.getInstance();
        currCal.set(Calendar.DAY_OF_YEAR, 1);
        setTimeZone(currCal);
        return currCal.getTime();
    }

    /**
     * 获得本年第一天凌晨的日期字符串。
     *
     * @return 默认格式 yyyy-MM-dd 的字符串
     */
    public static String getCurrentYearFirstDayZeroDateString() {
        return FAST_DATE_FORMAT.format(getCurrentYearFirstDayZeroDate());
    }

    /**
     * 获得本年第一天凌晨的日期字符串。
     *
     * @return 默认格式 yyyy-MM-dd 的字符串
     */
    public static String getCurrentYearFirstDayZeroDateSecondString() {
        return FAST_DATE_SECONDS_FORMAT.format(getCurrentYearFirstDayZeroDate());
    }


    /**
     * 获得本年第一天凌晨的日期字符串。
     *
     * @return 默认格式 yyyy-MM-dd 的字符串
     */
    public static String getCurrentYearFirstDayZeroDateString(String dateFormat) {
        FastDateFormat aDateFormat = FastDateFormat.getInstance(DATE_FORMAT_STRING);
        return aDateFormat.format(getCurrentYearFirstDayZeroDate());
    }

    //-------------------------------------------------------------------------
    /**
     * 返回一个增加了 amount 个 calendarFiled 的日期。
     * amount 为负数，即为减少。
     * 传入的 Date 不会发生改变.
     * @param date date 不能为空
     * @param calendarFiled 要增加的日历单位
     * @param amount 增加的量
     * @param isZero 转换后是否将时间调节到凌晨。为 true 则时间会调节到凌晨。
     * @return 修改后的 date 对象
     * @since 1.0.0-jre1.7
     */
    public static Date add(final Date date, int calendarFiled, int amount, boolean isZero) {
        if (date == null) {
            throw new IllegalArgumentException("date object can not be null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendarFiled, amount);
        if (isZero) {
            setTimeZone(calendar);
        }
        return calendar.getTime();
    }

    public static Date add(final Date date, int calendarFiled, int amount) {
        return add(date, calendarFiled, amount, false);
    }


    public static Date addDaysAndZero(Date date, int amount) {
        return add(date, Calendar.DATE, amount, true);
    }

    public static Date addDays(Date date, int amount) {
        return add(date, Calendar.DATE, amount);
    }

    //-------------------------------------------------------------------------
    /**
     * 方便调节日期字符串，返回同样格式的字符串。
     * @param dateStr 日期字符串
     * @param dateFormat 字符串格式
     * @param calendarFiled 要增加的calendar周期
     * @param amount 增加的量
     * @return 增加后的字符串。
     */
    public static String addDateString(String dateStr, String dateFormat, int calendarFiled, int amount) {
        return DateTimeWm.formatDateToString(
                addDateStringToDate(dateStr, dateFormat, calendarFiled, amount),
                dateFormat);
    }

    /**
     * 方便调节日期字符串，并将时钟部分设置为0，返回同样格式的字符串。
     * @param dateStr 日期字符串
     * @param dateFormat 字符串格式
     * @param calendarFiled 要增加的calendar周期
     * @param amount 增加的量
     * @return 增加后的字符串。
     */
    public static String addDateStringAndZero(String dateStr, String dateFormat, int calendarFiled, int amount) {
        return DateTimeWm.formatDateToString(
                addDateStringToDateAndZero(dateStr, dateFormat, calendarFiled, amount),
                dateFormat);
    }

    /**
     * 方便调节日期字符串，并返回日期格式的数据。
     * @param dateStr 日期字符串
     * @param dateFormat 字符串格式
     * @param calendarFiled 要增加的calendar周期
     * @param amount 增加的量
     * @return 增加响应时间后的date对象。
     */
    public static Date addDateStringToDate(String dateStr, String dateFormat, int calendarFiled, int amount) {
        return DateTimeWm.add(DateTimeWm.parseDateFromString(dateStr, dateFormat), calendarFiled,  amount);
    }

    /**
     * 方便调节日期字符串，并将时钟部分设置为0, 日期格式的数据。
     * @param dateStr 日期字符串
     * @param dateFormat 字符串格式
     * @param calendarFiled 要增加的calendar周期
     * @param amount 增加的量
     * @return 增加响应时间后的date对象。
     */
    public static Date addDateStringToDateAndZero(String dateStr, String dateFormat, int calendarFiled, int amount) {
        return DateTimeWm.add(DateTimeWm.parseDateFromString(dateStr, dateFormat), calendarFiled,  amount, true);
    }

    /**
     * 方便调节 日期字符串，为日期字符串增加一些天数，并将时钟部分设置为0，返回同样格式的字符串。
     * @param dateStr 日期字符串
     * @param dateFormat 字符串格式
     * @param amount 增加的天数
     * @return 增加后的字符串。
     */
    public static String addDaysForDateStringAndZero(String dateStr, String dateFormat, int amount) {
        return DateTimeWm.formatDateToString(
                addDateStringToDateAndZero(dateStr, dateFormat, Calendar.DATE, amount),
                dateFormat);
    }

    //-------------------------------------------------------------------------
    /**
     * 获取昨天的凌晨时刻的date
     */
    public static Date getYesterdayZeroDate() {
        return addDaysAndZero(new Date(), -1);
    }

    /**
     * 获取昨天凌晨时刻日期字符串
     * @return 格式为 yyyy-MM-dd
     */
    public static String getYesterdayZeroDateString() {
        return FAST_DATE_FORMAT.format(getYesterdayZeroDate());
    }

    /**
     * 获取昨天凌晨时刻日期字符串
     * @return
     */
    public static String getYesterdayZeroDateSecondString() {
        return FAST_DATE_SECONDS_FORMAT.format(getYesterdayZeroDate());
    }

    public static String getYesterdayZeroDateString(String dateFormat) {
        FastDateFormat aDateFormat = FastDateFormat.getInstance(DATE_FORMAT_STRING);
        return aDateFormat.format(getYesterdayZeroDate());
    }

    //-------------------------------------------------------------------------
    /**
     * 解析默认yyyy-MM-dd格式的日期字符串
     * @param dateStr 要被解析的字符创
     * @return 解析后的Date对象
     * @throws IllegalArgumentException 如果日期字符串与默认的日期字符串不相符，就会抛出该异常
     */
    public static Date parseDateFromString(String dateStr) {
        try {
            return SIMPLE_DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException(String.format("can not parse date string: %s, need format: %s", dateStr, DATE_FORMAT_STRING));
        }
    }


    /**
     * 解析默认格式yyyy-MM-dd HH:mm:ss的日期字符串
     * @param dateStr 要被解析的字符创
     * @return 解析后的Date对象
     * @throws IllegalArgumentException 如果日期字符串与默认的日期字符串不相符，就会抛出该异常
     */
    public static Date parseDateFromSecondString(String dateStr) {
        try {
            return SIMPLE_DATE_SECONDS_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException(String.format("can not parse date string: %s, need format: %s",
                    dateStr, DATE_SECONDS_FORMAT_STRING));
        }
    }

    /**
     * 解析默认指定格式的日期字符串
     * @param dateStr 要被解析的字符创
     * @return 解析后的Date对象
     * @throws IllegalArgumentException 如果日期字符串与默认的日期字符串不相符，就会抛出该异常
     */
    public static Date parseDateFromString(String dateStr, String dateFormat) {
        try {
            SimpleDateFormat aDateFormat = new SimpleDateFormat(dateFormat);
            return aDateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException(String.format("can not parse date string: %s, need format: %s",
                    dateStr, dateFormat));
        }
    }

    //-------------------------------------------------------------------------
    /**
     * 将 date 对象转为默认格式 yyyy-MM-dd 的字符串
     * @param date date对象，不能为空
     * @return 转换后的字符串
     * @throws IllegalArgumentException date 对象为空则抛出
     */
    public static String formatDateToString(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date can not be null");
        }
        return FAST_DATE_FORMAT.format(date);
    }

    /**
     * 将 date 对象转为默认格式 yyyy-MM-dd HH:mm:ss 的字符串
     * @param date date对象，不能为空
     * @return 转换后的字符串
     * @throws IllegalArgumentException date 对象为空则抛出
     */
    public static String formatDateToSecondString(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date can not be null");
        }
        return FAST_DATE_SECONDS_FORMAT.format(date);
    }

    /**
     * 将 date 对象转为指定格式的字符串
     * @param date date对象，不能为空
     * @return 转换后的字符串
     * @throws IllegalArgumentException date 对象为空则抛出
     */
    public static String formatDateToString(Date date, String dateFormat) {
        if (date == null) {
            throw new IllegalArgumentException("date can not be null");
        }
        return FastDateFormat.getInstance(dateFormat).format(date);
    }

    /**
     * 检查是否为符合默认日期yyyy-MM-dd格式的字符串
     * @return 是返回 true
     */
    public static boolean isFormattedString(String dateString) {
        try {
            SIMPLE_DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * 检查是否为符合默认日期yyyy-MM-dd HH:mm:ss格式的字符串
     * @return 是返回 true
     */
    public static boolean isFormattedSecondString(String dateString) {
        try {
            SIMPLE_DATE_SECONDS_FORMAT.parse(dateString);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * 检查是否为指定格式的时间字符串
     * @param dateString 时间字符串
     * @param dateFormat 指定的时间格式
     * @return 是返回 true
     */
    public static boolean isFormattedString(String dateString, String dateFormat) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            SIMPLE_DATE_SECONDS_FORMAT.parse(dateString);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public List<String> setAllMondayStringInDateRange(String startDate, String endDate) {
        return new ArrayList<>();
    }


    //-------------------------------------------------------------------------
    public static final String DATE_UNIT_HOUR = "hour";
    public static final String DATE_UNIT_WEEK = "week";
    public static final String DATE_UNIT_DAY = "day";
    public static final String DATE_UNIT_MONTH = "month";
    public static final String DATE_UNIT_YEAR = "year";



    /**
     * 计算两个时间的间隔单位数。
     * @param unit 间隔单位。
     * @param startDate 开始时间。
     * @param endDate 结束时间。
     *
     * @return 间隔单位
     */
    public static long dateDiff(String unit, Date startDate, Date endDate) {
        switch (unit) {
            case DATE_UNIT_HOUR:
                return (endDate.getTime() - startDate.getTime()) / (60000 * 60);
            case DATE_UNIT_DAY:
                return (endDate.getTime() - startDate.getTime()) / (60000 * 60 * 24);
            case DATE_UNIT_WEEK:
                return (endDate.getTime() - startDate.getTime()) / (60000 * 60 * 24 * 7);
            case DATE_UNIT_MONTH:
                return getDateDiffBase(DATE_UNIT_MONTH, startDate, endDate );
            case DATE_UNIT_YEAR:
                return getDateDiffBase(DATE_UNIT_YEAR, startDate, endDate);
        }
        return 0;
    }

    public static long dateDiff(String unit, String startDateStr, String endDateStr) {
        try {
            Date startDate = SIMPLE_DATE_FORMAT.parse(startDateStr);
            Date endDate = SIMPLE_DATE_FORMAT.parse(endDateStr);
            return dateDiff(unit, startDate, endDate);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static long dateDiff(String unit, String startDateStr, String endDateStr, String dateFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            Date startDate = format.parse(startDateStr);
            Date endDate = format.parse(endDateStr);
            return dateDiff(unit, startDate, endDate);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static long dateDiff(String unit,
                                String startDateStr, String startDateFormat,
                                String endDateStr, String endDateFormat) {
        try {
            SimpleDateFormat startFormat = new SimpleDateFormat(startDateFormat);
            SimpleDateFormat endFormat = new SimpleDateFormat(endDateFormat);
            Date startDate = startFormat.parse(startDateStr);
            Date endDate = endFormat.parse(endDateStr);
            return dateDiff(unit, startDate, endDate);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }



    public static Calendar getCalendarByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private static long getDateDiffBase(String unit, Date startDate, Date endDate) {
        Calendar startCalendar = getCalendarByDate(startDate);
        Calendar endCalendar = getCalendarByDate(endDate);

        long startMillSecond = startCalendar.get(Calendar.MILLISECOND);
        long endMillSecond = endCalendar.get(Calendar.MILLISECOND);

        long startSecond = startCalendar.get(Calendar.SECOND);
        long endSecond = endCalendar.get(Calendar.SECOND);

        long startMinute = startCalendar.get(Calendar.MINUTE);
        long endMinute = endCalendar.get(Calendar.MINUTE);

        long startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
        long endHour = endCalendar.get(Calendar.HOUR_OF_DAY);

        long startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
        long endDay = endCalendar.get(Calendar.DAY_OF_MONTH);

        long startMonth = startCalendar.get(Calendar.MONTH);
        long endMonth = endCalendar.get(Calendar.MONTH);

        long startYear = startCalendar.get(Calendar.YEAR);
        long endYear = endCalendar.get(Calendar.YEAR);

        long yearDis = endYear - startYear;
        long monthDis = endMonth - startMonth;
        long dayDis = endDay - startDay;
        long hourDis = endHour - startHour;
        long minuteDis = endMinute - startMinute;
        long secondDis = endSecond - startSecond;
        long millSecondDis = endMillSecond - startMillSecond;
        long midResult;

        switch (unit) {
            case DATE_UNIT_MONTH:
                midResult = yearDis * 12 + monthDis;
                break;
            case DATE_UNIT_YEAR:
                if (monthDis < 0) return yearDis - 1;
                if (monthDis > 0) return yearDis;
                midResult = yearDis;
                break;
            default:
                throw new IllegalArgumentException("not support unit: " + unit);
        }

        if (dayDis < 0) return midResult - 1;
        if (dayDis > 0) return midResult;

        if (hourDis < 0) return midResult -1 ;
        if (hourDis > 0) return midResult;

        if (minuteDis < 0) return midResult - 1;
        if (minuteDis > 0) return midResult;

        if (secondDis < 0) return midResult - 1;
        if (secondDis > 0) return midResult;

        if (millSecondDis < 0) return midResult - 1;
        if (millSecondDis > 0) return midResult;

        return midResult;
    }

    // 获取时间计算用到的进位。
    private static long getCarry(long value) {
        if (value < 0) return -1;
        return 0;
    }

//    // 获取前一天
//    public static String getPreviousDay() {
//        return DateFormatUtils.format(
//                DateUtils.addDaysAndZero(new Date(), -1),
//                DATE_FORMAT_STRING);
//    }

//    // 获取前一天
//    public static String getNextDay(Date date, String dateFormat) {
//        return DateFormatUtils.format(
//                DateUtils.addDaysAndZero(date, 1),
//                dateFormat);
//    }

//    // 获取前一天
//    public static String getAddedDay(Date date, int amount, String dateFormat) {
//        return DateFormatUtils.format(
//                DateUtils.addDaysAndZero(date, amount),
//                dateFormat);
//    }
//
//
//    // 使用时间字符串获得前一天凌晨的日期字符串
//    public static String getPreviousDayZeroTime(String dateString, String dateFormat) {
//        try {
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
//            Date nowDate = null;
//            nowDate = simpleDateFormat.parse(dateString);
//            return getPreviousDay(nowDate, dateFormat);
//        } catch (ParseException e) {
//            // 格式不对，返回原日期字符串。没啥好的方式了。
//            e.printStackTrace();
//            return dateString;
//        }
//    }
//
//    // 使用时间字符串获得下一天凌晨的日期字符串
//    public static String getNextDayZeroTime(String dateString) {
//        try {
//            if (dateString.length() == 19) {
//                if (!dateString.endsWith("00:00:00")) {
//                    dateString = dateString.substring(0, 10) + " 00:00:00";
//                }
//            } else if (dateString.length() == 16) {
//                dateString = dateString.substring(0, 10) + " 00:00:00";
//            } else if (dateString.length() == 10) {
//                dateString = dateString + " 00:00:00";
//            }
//            // 时间格式有误，这里会报错
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_SECONDS_FORMAT_STRING);
//            Date nowDate = null;
//            nowDate = simpleDateFormat.parse(dateString);
//            return getNextDay(nowDate, DATE_SECONDS_FORMAT_STRING);
//        } catch (ParseException e) {
//            // 格式不对，返回原日期字符串。没啥好的方式了。
//            e.printStackTrace();
//            throw new IllegalArgumentException("时间的格式不对 " + dateString);
//        }
//    }
//
//    public static String getMouthAgoTime(String dateStr) {
//        try {
//            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_STRING);
//            Date nowDate = format.parse(dateStr);
//            Date mouthAgoDate = DateUtils.addMonths(nowDate, -1);
//            return format.format(mouthAgoDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return dateStr;
//    }
//
//    public static String getTodayDateStr(String format) {
//        return DateFormatUtils.format(new Date(), format);
//    }
//
//    public static String getTodayDateStr() {
//        return DateFormatUtils.format(new Date(), DATE_FORMAT_STRING);
//    }
//
//    public static boolean isDateStr(String dataStr) {
//        if (dataStr != null && dataStr.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
//            return true;
//        }
//        return false;
//    }
//
//
//    /**
//     * 判断多个日期字符串是否符合指定格式的日期
//     *
//     * @return
//     */
//    public static boolean validTimeString(SimpleDateFormat formatString, String... timeList) {
//        boolean convertSuccess = true;
//        try {
//            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
//            formatString.setLenient(false);
//            for (String time :
//                    timeList) {
//                formatString.parse(time);
//            }
//        } catch (ParseException e) {
//            throw new BizException(BizExceptionEnum.BIZ_EXCEPTION.getErrorCode(), "日期格式不正确");
//        }
//        return convertSuccess;
//    }
//
//    /**
//     * 判断多个日期字符串是否符合DATE_FORMAT格式的日期
//     * 在Controller层使用
//     *
//     * @param dateStringList 不为空
//     * @return
//     */
//    public static boolean validDateString(String... dateStringList) {
//        return validTimeString(FAST_DATE_FORMAT, dateStringList);
//    }
//
//    /**
//     * 判断starDate,endDate是否符合合适
//     * 在Controller层使用
//     *
//     * @param
//     * @return
//     */
//    public static boolean validDateString(String startDate, String endDate) {
//        return (StringUtil.isEmpty(startDate) || BfdDateGetUtil.validDateString(startDate))
//                && (StringUtil.isEmpty(endDate) || BfdDateGetUtil.validDateString(endDate))
//                && (!(StringUtil.isEmpty(startDate) && StringUtil.isEmpty(endDate)))
//                ;
//    }
//
//
//    /**
//     * 检测开始结束日期字符串
//     * 在Controller层使用
//     *
//     * @return
//     */
//    public static boolean validStartEndDateString(String startDate, String endDate) {
//        // 这里的空是指 null 或者 空字符
//        // 若非空则必须满足时间格式，并且startDate要小于等于今天的日期
//        return (StringUtil.isEmpty(startDate) || (validTimeString(FAST_DATE_FORMAT, startDate) && getTodayDateStr().compareTo(startDate) >= 0))
//                // 若非空则必须满足时间格式
//                && (StringUtil.isEmpty(endDate) || (validTimeString(FAST_DATE_FORMAT, endDate) && getTodayDateStr().compareTo(endDate) >= 0))
//                // 至少一个不为空
//                && (!StringUtil.isEmpty(startDate) || !StringUtil.isEmpty(endDate))
//                // 若2个都不为空，endDate必须大于等于startDate
//                && ( StringUtil.isEmpty(startDate) || StringUtil.isEmpty(endDate) || endDate.compareTo(startDate) >= 0);
//    }
//
//    /**
//     * 是否包含实时和历史的数据
//     *
//     * @param startDate
//     * @param endDate
//     * @return 1表示只有今天，2表示只有历史，3表示包含今天和历史的数据
//     */
//    public static int containsTodayHistoryData(String startDate, String endDate) {
//        int flag = -1;
//        String todayDateStr = getTodayDateStr();
//        if (!StringUtil.isEmpty(startDate) && !StringUtil.isEmpty(endDate) && todayDateStr.compareTo(startDate) == 0 && todayDateStr.compareTo(endDate) == 0) {
//            flag = TODAY_FLAG;
//        } else if ((StringUtil.isEmpty(startDate) || todayDateStr.compareTo(startDate) > 0) && (!StringUtil.isEmpty(endDate) && todayDateStr.compareTo(endDate) > 0)) {
//            flag = HISTORY_FLAG;
//        } else {
//            flag = TODAY_AND_HISTORY_FLAG;
//        }
//        return flag;
//    }
//
//    /**
//     * 判断多个日期字符串是否符合DATE_FORMAT_MINUTE格式的日期
//     * 在Controller层使用
//     *
//     * @param dateStringList
//     * @return
//     */
//    public static boolean validDateTimeString(String... dateStringList) {
//        return validTimeString(FAST_DATE_SECONDS_FORMAT, dateStringList);
//    }
//
//    /**
//     * 判断多个日期字符串是否符合DATE_FORMAT_MINUTE格式的日期
//     * 在Controller层使用
//     *
//     * @param dateStringList
//     * @return
//     */
//
//
//    /**
//     * 判断时间段是否包含今天
//     * 在Service层使用
//     *
//     * @param startDate 开始时间
//     * @param endDate   结束时间
//     * @return
//     */
//    public static boolean containToday(String startDate, String endDate) {
//        String today = getTodayDateStr();
//        return (startDate == null || startDate.isEmpty() || startDate.compareTo(today) <= 0)
//                && (endDate == null || endDate.isEmpty() || endDate.compareTo(today) >= 0);
//    }
//
//    /**
//     * 判断时间段是否只为今天
//     * 在Service层使用
//     *
//     * @param startDate 开始时间
//     * @param endDate   结束时间
//     * @return
//     */
//    public static boolean isToday(String startDate, String endDate) {
//        String today = getTodayDateStr();
//        return (startDate.equals(today)) && (endDate == null || endDate.isEmpty() || endDate.equals(today));
//    }
//
//
//    /**
//     * 获取字符串类型日期的时间戳
//     *
//     * @param date 日期
//     * @return time 时间戳
//     */
//    public static Long getTimeFromDate(String date) {
//        try {
//            return FAST_DATE_FORMAT.parse(date).getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//            throw new IllegalArgumentException("时间的格式不对 " + date);
//        }
//    }
//
//    /**
//     * 获取字符串类型日期的时间戳
//     *
//     * @param date 日期
//     * @return time 时间戳
//     */
//    public static Long getTimeFromDateTime(String date) {
//        try {
//            return FAST_DATE_SECONDS_FORMAT.parse(date).getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//            throw new IllegalArgumentException("时间的格式不对 " + date);
//        }
//    }
//    /**
//     * 获取字符串类型日期
//     *
//     * @param time 时间戳
//     * @return date 日期
//     */
//    public static String getTimeToDate(Long time) {
//        try {
//            return FAST_DATE_FORMAT.format(time);
//        } catch (Exception e) {
//
//            e.printStackTrace();
//            throw new IllegalArgumentException("时间的格式不对 " + time);
//        }
//    }
//
//    /**
//     * 获取字符串类型日期
//     *
//     * @param date date类型
//     * @return date 日期
//     */
//    public static String getDateToString(Date date) {
//        try {
//            return FAST_DATE_FORMAT.format(date);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new IllegalArgumentException("时间的格式不对 " + date);
//        }
//    }
//
//    /**
//     * 获取小时类型的时间戳
//     *
//     * @param date 日期  比如：2017-12-12
//     * @return map 数组  比如：(0:1513008000000,1:1513011600000,2:1513015200000,3:1513018800000)
//     */
//    public static Map<Integer, String> getTimeHour(String date) {
//        HashMap<Integer, String> map = new HashMap<>();
//        for (int i = 1; i <= 24; i++) {
//            String dateTime = date + " " + i + ":00:00";
//            try {
//                Date dates = FAST_DATE_SECONDS_FORMAT.parse(dateTime);
//                long time = dates.getTime();
//                map.put(i, String.valueOf(time));
//            } catch (ParseException e) {
//                e.printStackTrace();
//                throw new IllegalArgumentException("时间的格式不对");
//            }
//        }
//        return map;
//    }
//    /**
//     * @param startDate 开始时间
//     * @param endDate 结束时间
//     * @return List<String>  返回时间范围内每周一的日期
//     */
//    public static List<String> getMondays(String startDate, String endDate) {
//        ArrayList<String> list;
//        try {
//            Date start = FAST_DATE_FORMAT.parse(startDate);
//            Date end = FAST_DATE_FORMAT.parse(endDate);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(start);
//            list = new ArrayList<>();
//
//            while (true) {
//                String date = FAST_DATE_FORMAT.format(calendar.getTime());
//                if (calendar.get(Calendar.DAY_OF_WEEK) == 2) {
//                    list.add(date);
//                }
//
//                if (date.equals(endDate)) {
//                    break;
//                } else {
//                    calendar.add(Calendar.DATE, 1);
//                }
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//            throw new IllegalArgumentException("时间的格式不对");
//        }
//        return list;
//    }
//    /**
//     * @param startDate 开始时间
//     * @param endDate 结束时间
//     * @return List<String>  返回时间范围内每月一号的日期
//     */
//    public static List<String> getFirstDayOfMonths(String startDate, String endDate) {
//        ArrayList<String> list;
//        try {
//            Date start = FAST_DATE_FORMAT.parse(startDate);
//            Date end = FAST_DATE_FORMAT.parse(endDate);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(start);
//            list = new ArrayList<>();
//
//            while (true) {
//                String date = FAST_DATE_FORMAT.format(calendar.getTime());
//                if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
//                    list.add(date);
//                }
//
//                if (date.equals(endDate)) {
//                    break;
//                } else {
//                    calendar.add(Calendar.DATE, 1);
//                }
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//            throw new IllegalArgumentException("时间的格式不对");
//        }
//        return list;
//    }
//    /**
//     * @param date 某个时间  比如：2018-12-12
//     * @return date  返回前一天的日期
//     */
//    public static String getBeforeOneDate(String date) {
//        String beforeOneDate;
//        try {
//            Date start = FAST_DATE_FORMAT.parse(date);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(start);
//            calendar.add(Calendar.DAY_OF_YEAR,-1);
//            beforeOneDate = FAST_DATE_FORMAT.format(calendar.getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//            throw new IllegalArgumentException("时间的格式不对");
//        }
//        return beforeOneDate;
//    }
//
//    /**
//     *
//     * @param date
//     * @param n
//     * @return 返回前后n天的日期
//     */
//    public static String getBeforeNDate(String date, int n) {
//        String beforeDate;
//        try {
//            Date start = FAST_DATE_FORMAT.parse(date);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(start);
//            calendar.add(Calendar.DAY_OF_YEAR,n);
//            beforeDate = FAST_DATE_FORMAT.format(calendar.getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//            throw new IllegalArgumentException("时间的格式不对");
//        }
//        return beforeDate;
//    }
//*/

    private static void setTimeZone(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }
}
