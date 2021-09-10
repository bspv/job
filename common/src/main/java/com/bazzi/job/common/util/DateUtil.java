package com.bazzi.job.common.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;

public final class DateUtil {
    public static final String YMD_FORMAT = "yyyy-MM-dd";
    public static final String FULL_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String APPEND_PATTERN = "[ [HH][:mm][:ss][.SSS]]";

    /**
     * 将日期格式化成字符串形式
     *
     * @param ldt    日期
     * @param format 字符串日期的格式
     * @return 字符串日期
     */
    public static String formatDate(LocalDateTime ldt, String format) {
        if (ldt == null || format == null || "".equals(format))
            return null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        return dtf.format(ldt);
    }

    /**
     * 将日期格式化成字符串形式
     *
     * @param date   日期
     * @param format 字符串日期的格式
     * @return 字符串日期
     */
    public static String formatDate(Date date, String format) {
        return formatDate(convertToLocalDateTime(date), format);
    }

    /**
     * 将字符串日期转换成日期类型
     *
     * @param strDate 字符串日期
     * @param format  字符串日期的格式
     * @return 日期
     */
    public static LocalDateTime getDate(String strDate, String format) {
        if (strDate == null || "".equals(strDate) || format == null || "".equals(format))
            return null;
        return LocalDateTime.parse(strDate, buildDateTimeFormatter(format));
    }

    /**
     * 获取相距日期n天的日期
     *
     * @param ldt 日期
     * @param n   正数往后，负数往前
     * @return 计算后的日期
     */
    public static LocalDateTime getNextDay(LocalDateTime ldt, int n) {
        if (ldt == null) {
            return null;
        }
        return ldt.plusDays(n);
    }

    /**
     * 获取日期的开始时间，即yyyy-MM-dd 00:00:00.000
     *
     * @param ldt 日期
     * @return 该日期的开始时间
     */
    public static LocalDateTime startOfDay(LocalDateTime ldt) {
        if (ldt == null) {
            return null;
        }
        return ldt.toLocalDate().atStartOfDay();
    }

    /**
     * 获取日期的结束时间，即yyyy-MM-dd 23:59:59.999
     *
     * @param ldt 日期
     * @return 该日期的结束时间
     */
    public static LocalDateTime endOfDay(LocalDateTime ldt) {
        if (ldt == null) {
            return null;
        }
        return LocalDateTime.of(ldt.toLocalDate(), LocalTime.MAX);
    }

    /**
     * 将Date转为LocalDateTime
     *
     * @param date Date
     * @return LocalDateTime
     */
    public static LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 将LocalDateTime转为Date
     *
     * @param ldt LocalDateTime
     * @return Date
     */
    public static Date convertToDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 构建用于打印和解析日期时间对象的格式化程序
     *
     * @param format 日期格式
     * @return DateTimeFormatter
     */
    private static DateTimeFormatter buildDateTimeFormatter(String format) {
        String curFormat;
        if (format == null || "".equals(format))
            curFormat = YMD_FORMAT;
        else {
            curFormat = format.contains(" ") ? format.substring(0, format.indexOf(" ")) : format;
        }
        curFormat = curFormat + APPEND_PATTERN;
        return new DateTimeFormatterBuilder().appendPattern(curFormat)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();
    }

}
