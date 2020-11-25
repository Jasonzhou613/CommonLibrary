package com.ttsea.commonlibrary.utils;

import java.math.BigDecimal;

/**
 * 浮点类型数据加减乘除工具类 <br>
 * <p>
 * <b>more:</b>更多请点 <a href="http://www.ttsea.com" target="_blank">这里</a> <br>
 * <b>date:</b> 2017/4/10 9:55 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
final public class FloatUtils {
    private static final String TAG = "Utils.FloatUtils";

    /**
     * <p>
     * add
     * </p>
     *
     * @param a
     * @param b
     * @return
     */
    public static float add(float a, float b) {

        BigDecimal b1 = new BigDecimal(a + "");
        BigDecimal b2 = new BigDecimal(b + "");
        float f = b1.add(b2).floatValue();

        return f;
    }

    /**
     * <p>
     * subtract
     * </p>
     *
     * @param a
     * @param b
     * @return
     */
    public static float subtract(float a, float b) {

        BigDecimal b1 = new BigDecimal(a + "");
        BigDecimal b2 = new BigDecimal(b + "");
        float f = b1.subtract(b2).floatValue();

        return f;

    }

    /**
     * <p>
     * multiply
     * </p>
     *
     * @param a
     * @param b
     * @return
     */
    public static float multiply(float a, float b) {

        BigDecimal b1 = new BigDecimal(a + "");
        BigDecimal b2 = new BigDecimal(b + "");
        float f = b1.multiply(b2).floatValue();

        return f;

    }

    /**
     * <p>
     * divide
     * </p>
     * <p>
     * 当不整除，出现无限循环小数时，向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，向上舍入, 1.55保留一位小数结果为1.6
     * </p>
     *
     * @param a
     * @param b
     * @return
     */
    public static float divide(float a, float b) {

        return divide(a, b, 2, BigDecimal.ROUND_HALF_UP);

    }

    /**
     * <p>
     * divide
     * </p>
     *
     * @param a
     * @param b
     * @param scale
     * @param roundingMode
     * @return
     */
    public static float divide(float a, float b, int scale, int roundingMode) {

        /*
         * 通过BigDecimal的divide方法进行除法时就会抛异常的，异常如下： java.lang.ArithmeticException:
         * Non-terminating decimal expansion; no exact representable decimal
         * result. at java.math.BigDecimal.divide(Unknown Source)
         * 解决之道：就是给divide设置精确的小数点divide(xxxxx,2, BigDecimal.ROUND_HALF_EVEN)
         * BigDecimal.ROUND_HALF_UP : 向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，向上舍入,
         * 1.55保留一位小数结果为1.6
         */

        BigDecimal b1 = new BigDecimal(a + "");
        BigDecimal b2 = new BigDecimal(b + "");
        float f = b1.divide(b2, scale, roundingMode).floatValue();

        return f;

    }

    public static float parseFloat(String floatStr)
            throws NumberFormatException {
        float f = Float.parseFloat(floatStr);

        if (f == ((int) f)) {
            return ((int) f);
        }
        return f;
    }

    /**
     * float转为String<br>
     * 1.如：1.0 --> 1<br>
     * 2.如：1.2 --> 1.2
     *
     * @param f
     * @return String
     */
    public static String float2String(float f) {
        int i = (int) f;

        if (i == f) {
            return String.valueOf(i);
        }

        return String.valueOf(f);
    }
}
