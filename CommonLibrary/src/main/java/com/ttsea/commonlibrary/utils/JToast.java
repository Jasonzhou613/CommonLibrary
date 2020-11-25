package com.ttsea.commonlibrary.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 用来Toast想要输出的数据 <br>
 * <p>
 * <b>more:</b>更多请点 <a href="http://www.ttsea.com" target="_blank">这里</a> <br>
 * <b>date:</b> 2017/4/10 9:55 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
final public class JToast {
    public static int LIMITS = -0x1010;

    public static void makeText(Context context, CharSequence text) {
        makeText(context, text, Toast.LENGTH_SHORT, JToast.LIMITS,
                JToast.LIMITS, JToast.LIMITS, JToast.LIMITS, JToast.LIMITS);
    }

    public static void makeTextCenter(Context context, CharSequence text) {
        makeText(context, text, Toast.LENGTH_SHORT, Gravity.CENTER, 0, 0,
                JToast.LIMITS, JToast.LIMITS);
    }

    public static void makeTextCenter(Context context, CharSequence text, int yOffset) {
        makeText(context, text, Toast.LENGTH_SHORT, Gravity.CENTER, 0, yOffset,
                JToast.LIMITS, JToast.LIMITS);
    }

    public static void makeTextTop(Context context, CharSequence text, int yOffset) {
        makeText(context, text, Toast.LENGTH_SHORT, Gravity.TOP, 0, yOffset,
                JToast.LIMITS, JToast.LIMITS);
    }

    public static void makeTextBottom(Context context, CharSequence text, int yOffset) {
        makeText(context, text, Toast.LENGTH_SHORT, Gravity.BOTTOM, 0, yOffset,
                JToast.LIMITS, JToast.LIMITS);
    }

    public static void makeTextLeft(Context context, CharSequence text) {
        makeText(context, text, Toast.LENGTH_SHORT, Gravity.LEFT, 0, 0,
                JToast.LIMITS, JToast.LIMITS);
    }

    public static void makeTextRight(Context context, CharSequence text) {
        makeText(context, text, Toast.LENGTH_SHORT, Gravity.RIGHT, 0, 0,
                JToast.LIMITS, JToast.LIMITS);
    }

    /**
     * 如果gravity xOffset yOffset horizontalMargin verticalMargin等于
     * {@link JToast JToast#JToast.limitss JToast.limitss}，则是默认的
     *
     * @param context
     * @param text
     * @param duration
     * @param gravity
     * @param xOffset
     * @param yOffset
     * @param horizontalMargin
     * @param verticalMargin
     */
    public static void makeText(Context context, CharSequence text,
                                int duration, int gravity, int xOffset, int yOffset,
                                float horizontalMargin, float verticalMargin) {

        Toast toast = Toast.makeText(context, text, duration);
        toast.setDuration(duration);
        if (gravity == JToast.LIMITS)
            gravity = toast.getGravity();
        if (xOffset == JToast.LIMITS)
            xOffset = toast.getXOffset();
        if (yOffset == JToast.LIMITS)
            yOffset = toast.getYOffset();
        if ((int) horizontalMargin == JToast.LIMITS)
            horizontalMargin = toast.getHorizontalMargin();
        if ((int) verticalMargin == JToast.LIMITS)
            verticalMargin = toast.getVerticalMargin();

        toast.setGravity(gravity, xOffset, yOffset);
        toast.setMargin(horizontalMargin, verticalMargin);

        toast.show();
    }
}
