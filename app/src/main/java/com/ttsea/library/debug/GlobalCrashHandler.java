package com.ttsea.library.debug;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.ttsea.commonlibrary.utils.DateUtils;
import com.ttsea.library.common.utils.CacheDirUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * 异常捕获工具类，需要SD开读写权限 <br>
 * <p>
 * <b>date:</b> 2017/11/17 16:11 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
final public class GlobalCrashHandler implements UncaughtExceptionHandler {
    private static GlobalCrashHandler instance;

    private Context context;
    private HashMap<String, String> infoMap;
    private UncaughtExceptionHandler mDefaultHandler;

    private GlobalCrashHandler(Context context) {
        this.context = context.getApplicationContext();
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public static GlobalCrashHandler init(Context context) {
        if (instance == null) {
            instance = new GlobalCrashHandler(context);
            Thread.setDefaultUncaughtExceptionHandler(instance);
        }
        return instance;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (null == infoMap) {
            infoMap = new HashMap<>();
        } else {
            infoMap.clear();
        }
        getExceptionInfo(context, ex);
        //sendEmail();// 将错误信息发送到邮箱
        saveInfo2SD();
        mDefaultHandler.uncaughtException(thread, ex);
        selfKill();
    }

    /** 退出程序 */
    private void selfKill() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    /**
     * 获取异常与设备信息
     *
     * @param context   上下文
     * @param throwable 异常
     */
    private void getExceptionInfo(Context context, Throwable throwable) {
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;

        try {
            mPackageInfo = mPackageManager.getPackageInfo(
                    context.getPackageName(), PackageManager.GET_ACTIVITIES);
            infoMap.put("packageName", mPackageInfo.packageName);
            infoMap.put("versionName", mPackageInfo.versionName);
            infoMap.put("versionCode", "" + mPackageInfo.versionCode);

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        infoMap.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        infoMap.put("MODEL", "" + Build.MODEL);
        infoMap.put("PRODUCT", "" + Build.PRODUCT);
        infoMap.put("time", DateUtils.getCurrentTime("yyyy-MM-dd-HH-mm-ss"));

        obtainExceptionInfo(throwable);
    }

    /**
     * 获取系统未捕捉的错误信息
     *
     * @param throwable 异常
     */
    private void obtainExceptionInfo(Throwable throwable) {
        Throwable cause = throwable.getCause();
        if (null != cause) {
            infoMap.put("exception_summary", cause.getMessage());
        } else {
            infoMap.put("exception_summary", throwable.getMessage());
        }
        infoMap.put("exception_detail", Log.getStackTraceString(throwable));

    }

    /** 保存信息到本地 */
    private void saveInfo2SD() {
        StringBuffer sb = new StringBuffer();

        for (Entry<String, String> entry : infoMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                // final String logDir =
                // Environment.getExternalStorageDirectory().getPath()
                // + File.separator + "LOA/crashLogs";

                final String logDir = CacheDirUtils.getLogDir(context) + File.separator + "crashLogs";

                File dir = new File(logDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File file = new File(dir.toString() + File.separator
                        + DateUtils.getCurrentTime("yyyy-MM-dd_HH-mm-ss") + ".log");
                FileOutputStream fos = new FileOutputStream(file, true);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    private void sendEmail() {
//        String versionName = "";
//        StringBuffer sb = new StringBuffer();
//
//        for (Entry<String, String> entry : infoMap.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            sb.append(key).append(" = ").append(value).append("\n");
//            if (key != null && value != null && key.equals("versionName")) {
//                versionName = value;
//            }
//        }
//        String appName = APP.mApp.getResources().getString(R.string.app_name);
//        String subject = appName + " crash_" + DateUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss")
//                + " v" + versionName;
//        if (Config.DEBUG) {
//            subject = "test " + subject + "-debug";
//        }
//
//        final String finalSubject = subject;
//        final String content = new String(sb);
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                // TttSendEmail.send(finalSubject, content);
//            }
//        }).start();
//    }
}
