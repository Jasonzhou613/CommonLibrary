package com.ttsea.commonlibrary.utils;

import com.ttsea.commonlibrary.debug.JLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间类 <br>
 * <p>
 * <b>more:</b>更多请点 <a href="http://www.ttsea.com" target="_blank">这里</a> <br>
 * <b>date:</b> 2017/4/10 9:55 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
final public class DateUtils {
    private final static String TAG = "Utils.DateUtils";

    /**
     * 得到当前时间
     *
     * @param pattern 时间格式
     * @return String
     */
    public static String getCurrentTime(String pattern) {
        return getCurrentTime(pattern, Locale.getDefault());
    }

    /**
     * 得到当前时间
     *
     * @param pattern 时间格式，形如：yyyy-MM-dd 或者yyyy-MM-dd HH:mm:ss
     * @return String
     */
    public static String getCurrentTime(String pattern, Locale locale) {
        String date = null;
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        date = formatter.format(curDate);

        return date;
    }

    /**
     * 格式化时间
     *
     * @param datetime String 形如"1392566400000"
     * @param pattern  需要的时间格式，形如：yyyy-MM-dd 或者yyyy-MM-dd HH:mm:ss
     * @return 正常解析时返回时间字符串，否则返回null
     */
    public static String parseString(String datetime, String pattern) {
        return parseString(datetime, pattern, Locale.getDefault());
    }

    /**
     * 格式化时间
     *
     * @param datetime String 形如"1392566400000"
     * @param pattern  需要的时间格式，形如：yyyy-MM-dd 或者yyyy-MM-dd HH:mm:ss
     * @param locale   {@link Locale}
     * @return 正常解析时返回时间字符串，否则返回null
     */
    public static String parseString(String datetime, String pattern,
                                     Locale locale) {
        String d = null;
        if (Utils.isEmpty(datetime)) {
            JLog.d(TAG, "parseString, datetime is empty.");
            return null;
        }
        try {
            long date = Long.parseLong(datetime);
            d = parseLong(date, pattern, locale);

        } catch (Exception e) {
            d = null;
            JLog.d(TAG, "parseString, Exception e:" + e.toString());
        }

        return d;
    }

    /**
     * 格式化时间
     *
     * @param datetime long 如：1392566400000
     * @param pattern  需要的时间格式，形如：yyyy-MM-dd 或者yyyy-MM-dd HH:mm:ss
     * @return 正常解析时返回时间字符串，否则返回null
     */
    public static String parseLong(long datetime, String pattern) {
        return parseLong(datetime, pattern, Locale.getDefault());
    }

    /**
     * 格式化时间
     *
     * @param datetime long 如：1392566400000
     * @param pattern  需要的时间格式，形如：yyyy-MM-dd 或者yyyy-MM-dd HH:mm:ss
     * @param locale   {@link Locale}
     * @return 正常解析时返回时间字符串，否则返回null
     */
    public static String parseLong(long datetime, String pattern, Locale locale) {
        String d = null;
        try {
            SimpleDateFormat time = new SimpleDateFormat(pattern, locale);
            Date date = new Date(datetime);
            d = time.format(date);

        } catch (Exception e) {
            d = null;
            JLog.d(TAG, "parseString, Exception e:" + e.toString());
        }

        return d;
    }

    /**
     * 将String类型数据转换为long类型时间，time的格式需要与formatType一致
     *
     * @param time       需要转换的String类型时间
     * @param formatType 转换的格式
     * @return 需要得到的long类型时间
     * @throws ParseException
     */
    public static long stringToLong(String time, String formatType) throws ParseException {
        return stringToLong(time, formatType, Locale.getDefault());
    }

    /**
     * 将String类型数据转换为long类型时间，time的格式需要与formatType一致
     *
     * @param time       需要转换的String类型时间
     * @param formatType 转换的格式
     * @return 需要得到的long类型时间
     * @throws ParseException
     */
    public static long stringToLong(String time, String formatType, Locale locale) throws ParseException {
        Date date = stringToDate(time, formatType, locale);
        return date.getTime();
    }

    /**
     * 将String类型数据转为Date数据
     *
     * @param time       需要转换的时间
     * @param formatType 转换的String格式
     * @return Date类型时间
     * @throws ParseException
     */
    public static Date stringToDate(String time, String formatType) throws ParseException {
        return stringToDate(time, formatType, Locale.getDefault());
    }

    /**
     * 将String类型数据转为Date数据
     *
     * @param time       需要转换的时间
     * @param formatType 转换的String格式
     * @return Date类型时间
     * @throws ParseException
     */
    public static Date stringToDate(String time, String formatType, Locale locale) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(formatType, locale);
        Date date = format.parse(time);
        return date;
    }

    public static String dateToString(Date data, String formatType) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        Date date = new Date();
        String str = sdf.format(date);
        return str;
    }

    /**
     * 将Date转为String
     *
     * @param date       {@link Date}
     * @param formatType 格式
     * @return String
     */
    public static String date2String(Date date, String formatType) {
        return date2String(date, formatType, Locale.getDefault());
    }

    /**
     * 将Date转为String
     *
     * @param date       {@link Date}
     * @param formatType 格式
     * @param locale     {@link Locale}
     * @return String
     */
    public static String date2String(Date date, String formatType, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatType, locale);
        return sdf.format(date);
    }

    /**
     * 获取timeMillis当天零点的时间戳
     *
     * @param timeMillis 当前时间戳
     * @return long
     */
    public static long getZeroTimeMillis(long timeMillis) {
        Date date = new Date(timeMillis);
        long l = 24 * 60 * 60 * 1000; // 每天的毫秒数
        // date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（
        // 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
        // 减8个小时的毫秒值是为了解决时区的问题。
        return (date.getTime() - (date.getTime() % l) - 8 * 60 * 60 * 1000);
    }
}