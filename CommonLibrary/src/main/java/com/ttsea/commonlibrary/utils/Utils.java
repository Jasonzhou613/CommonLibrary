package com.ttsea.commonlibrary.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ttsea.commonlibrary.debug.JLog;

import java.util.List;

/**
 * Utils <br>
 * <p>
 * <b>date:</b> 2017/7/12 10:29 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
public class Utils {
    private final static String TAG = "Utils";

    /** 隐藏软键盘 */
    public static void hideInput(Context context, View view) {
        if (view == null) {
            JLog.w(TAG, "hideInput, view is null");
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /** 显示软键盘 */
    public static void showInput(final Context context, final EditText editText) {
        try {
            editText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(editText, 0);
                }
            }, 500);
        } catch (Exception e) {
            JLog.e(TAG, "Exception e:" + e.toString());
        }
    }

    /** 判断str是否为空 */
    public static boolean isEmpty(String str) {
        if (str == null || str.length() < 1) {
            return true;
        }
        return false;
    }

    /**
     * 将EditText里的光标自动选择到最后
     *
     * @param editText
     */
    public static void selectionLast(EditText editText) {
        if (editText == null || editText.getText() == null) {
            return;
        }
        editText.setSelection(editText.getText().length());
    }

    /** 确保 value 不为空 */
    public static String checkNotNull(String value) {
        if (value == null) {
            value = "";
        }
        return value;
    }

    /**
     * 判断Service是否正在运行
     *
     * @param context     上下文
     * @param serviceName Service 类全名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfoList = manager.getRunningServices(200);
        if (serviceInfoList.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo info : serviceInfoList) {
            if (info.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断进程是否正在运行
     *
     * @param context     上下文
     * @param processName 进程名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isProcessRunning(Context context, String processName) {
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        // 获取运行服务再启动
        for (ActivityManager.RunningAppProcessInfo info : lists) {
            if (info.processName.equals(processName)) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    /**
     * 判断app是否运行在最前端
     *
     * @param context     上下文
     * @param packageName 包名
     * @return true or false
     */
    public static boolean isAppRunForeground(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName)
                && currentPackageName.equals(packageName)) {
            return true;
        }
        return false;
    }

    /**
     * 数组变成String
     *
     * @param array
     * @return
     */
    public static String array2String(String[] array) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            if (i < (array.length - 1)) {
                builder.append(array[i] + ",");
            } else {
                builder.append(array[i]);
            }
        }
        builder.append("]");

        return builder.toString();
    }

    /**
     * 将时间转换成 xx分xx秒
     *
     * @param duration
     * @return
     */
    public static String transformDuration(String duration) {
        if (Utils.isEmpty(duration)) {
            return "";
        }

        try {
            int hours = 0;
            int min = 0;
            int sec = 0;
            int value = Integer.parseInt(duration);

            if (value >= 3600) {
                hours = value / 3600;
                value = value % 3600;
            }

            min = value / 60;
            sec = value % 60;

            if (hours > 0) {
                return hours + "小时" + min + "分" + sec + "秒";
            } else if (min > 0) {
                return min + "分" + sec + "秒";
            } else {
                return sec + "秒";
            }

        } catch (Exception e) {
            JLog.e(TAG, "Exception e:" + e.getMessage());
        }
        return duration;
    }
}
