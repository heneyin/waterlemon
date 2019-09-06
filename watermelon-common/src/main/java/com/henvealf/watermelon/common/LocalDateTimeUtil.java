package com.henvealf.watermelon.common;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-07-10
 */
public class LocalDateTimeUtil {

    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static DateTimeFormatter formatterDate = DateTimeFormatter.ISO_LOCAL_DATE;
    private static DateTimeFormatter formatterMinute = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
    private static DateTimeFormatter formatterSecond = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    /**
     * 解析为 yyyy-MM-dd HH:mm:ss 的字符串
     * @param dateTime LocalDateTime
     * @return
     */
    public static String getSecondStr(LocalDateTime dateTime) {
        return dateTime.format(formatterSecond);
    }

    public static String getDayStr(LocalDateTime dateTime) {
        return dateTime.format(formatterDay);
    }

    /**
     * 解析为 yyyy-MM-dd HH:mm:ss 的字符串
     * @param timestamp 时间搓
     * @return
     */
    public static String getSecondStr(long timestamp) {
        return getSecondStr(getDateTimeByTimestamp(timestamp));
    }

    public static String getDayStr(long timestamp) {
        return getDayStr(getDateTimeByTimestamp(timestamp));
    }

    /**
     * 获取 LocalDateTime 所表示的时间戳，毫秒
     * @param dateTime
     * @return
     */
    public static long getTimestamp(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取 LocalDate 所表示的时间戳， 毫秒
     * @param date
     * @return
     */
    public static long getTimestamp(LocalDate date) {
        return date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static LocalDateTime getDateTimeByTimestamp(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

}
