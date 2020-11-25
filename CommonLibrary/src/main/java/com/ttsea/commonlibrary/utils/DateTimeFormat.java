package com.ttsea.commonlibrary.utils;

/**
 * 时间格式类<br>
 * <p>
 * <b>date:</b> 2018/5/18 15:37 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
public final class DateTimeFormat {

    /**
     * yyyy-MM-dd HH:mm
     */
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd'T'HH:mm:ss
     */
    public static final String DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * HH:mm:ss
     */
    public static final String DATE_FORMAT_HH_MM_SS = "HH:mm:ss";

    /**
     * HH:mm
     */
    public static final String DATE_FORMAT_HH_MM = "HH:mm";

    /**
     * yyyy-MM-ddHH
     */
    public static final String DATE_FORMAT_YYYY_MM_DDHH = "yyyy-MM-ddHH";

    /**
     * yyyy-MM-dd
     */
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * yyyy年MM月dd日
     */
    public static final String DATE_FORMAT_YYYY_MM_DD_CH = "yyyy年MM月dd日";

    // RFC 822 Date Format
    public static final String RFC822_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss 'GMT'";

    // ISO 8601 format
    public static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    // Alternate ISO 8601 format without fractional seconds
    public static final String ALTERNATIVE_ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static final String LOG_FORMAT = "MM_dd-HH";

    public static final String QR_CODE_FORMAT = "MMddHHmm";

    /**
     * "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"
     */
    public static final String[] DAYS_IN_WEEK = {"星期日", "星期一", "星期二", "星期三",
            "星期四", "星期五", "星期六"};

    /**
     * 0L, 600000L, 1800000L, 3600000L, 7200000L, 86400000L
     */
    public static final long[] ALARM_TIME_POINT = {0L, 600000L, 1800000L,
            3600000L, 7200000L, 86400000L};
}
