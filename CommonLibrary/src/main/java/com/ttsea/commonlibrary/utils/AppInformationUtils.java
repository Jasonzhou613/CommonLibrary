package com.ttsea.commonlibrary.utils;

import android.content.Context;

import com.ttsea.commonlibrary.debug.JLog;

/**
 * 获取应用的版本号和版本代码 <br>
 * getVersionName()方法得到应用的Version name <br>
 * getVersionCode()方法得到应用的Version code
 * <p>
 * <b>more:</b>更多请点 <a href="http://www.ttsea.com" target="_blank">这里</a> <br>
 * <b>date:</b> 2017/4/10 9:55 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
final public class AppInformationUtils {
    private static final String TAG = "Utils.AppInformationUtils";

    /**
     * 得到应用程序的包名
     *
     * @return 包名
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 得到应用的version name，如：1.0.0
     *
     * @return version name，发生异常时返回null;
     */
    public static String getVersionName(Context context) {
        String versionName = null;
        String pkName = "";

        try {
            pkName = context.getPackageName();
            versionName = context.getPackageManager().getPackageInfo(pkName, 0).versionName;

        } catch (Exception e) {
            JLog.e(TAG, "getVersionName, Exception: " + e.getMessage());
        }
        JLog.d(TAG, "App package name:" + pkName + ", App version name:" + versionName);

        return versionName;
    }

    /**
     * 得到应用的version code，如：131125
     *
     * @return version code，发生异常时返回-1
     */
    public static int getVersionCode(Context context) {
        int versionCode = -1;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;

        } catch (Exception e) {
            JLog.e(TAG, "getVersionCode, Exception: " + e.getMessage());
        }
        JLog.d(TAG, "App version code:" + String.valueOf(versionCode));

        return versionCode;
    }
}
