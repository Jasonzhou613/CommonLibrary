package com.ttsea.commonlibrary.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ttsea.commonlibrary.debug.JLog;

/**
 * 检测网络是否可用、获取网络链类型<br>
 * need "android.permission.ACCESS_NETWORK_STATE" permission
 * <p>
 * <b>more:</b>更多请点 <a href="http://www.ttsea.com" target="_blank">这里</a> <br>
 * <b>date:</b> 2017/4/10 9:55 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
final public class NetWorkUtils {
    private final static String TAG = "Utils.NetWorkUtils";

    /**
     * 获取网络链接类型<br>
     * status的值是 -1,
     * {@link ConnectivityManager#TYPE_MOBILE},
     * {@link ConnectivityManager#TYPE_WIFI},
     * {@link ConnectivityManager#TYPE_MOBILE_MMS},
     * {@link ConnectivityManager#TYPE_MOBILE_SUPL},
     * {@link ConnectivityManager#TYPE_MOBILE_DUN},
     * {@link ConnectivityManager#TYPE_MOBILE_HIPRI},
     * {@link ConnectivityManager#TYPE_WIMAX},
     * {@link ConnectivityManager#TYPE_BLUETOOTH},
     * {@link ConnectivityManager#TYPE_DUMMY},
     * {@link ConnectivityManager#TYPE_ETHERNET},
     * {@link ConnectivityManager#TYPE_VPN},
     *
     * @param context 上下文
     * @return 网络链接类型，-1表示网络不可用
     */
    public static int getNetWorkStatus(Context context) {
        int status = -1;
        // 获取手机所有连接管理对象（包括对wifi,cmnet,cmwap等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取网络连接管理的对象
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                status = networkInfo.getType();
            }

        } catch (Exception e) {
            status = -1;
            JLog.e(TAG, "getNetWorkStatus, Exception: " + e.getMessage());
        }
        JLog.d(TAG, "NetWorkUtils status:" + String.valueOf(status) + ":" + getNetWorkStatusStr(status));

        return status;
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return 网络可以用，则返回true；否则返回false
     */
    public static boolean isAvailable(Context context) {
        if (getNetWorkStatus(context) == -1) {
            JLog.d(TAG, "Network is not available.");
            return false;
        }
        JLog.d(TAG, "Network is available.");
        return true;
    }

    /**
     * wifi是否可用
     *
     * @param context
     * @return wifi可用，则返回true；否则返回false
     */
    public static boolean isWiFiAvailable(Context context) {
        if (getNetWorkStatus(context) == ConnectivityManager.TYPE_WIFI) {
            JLog.d(TAG, "wifi is available.");
            return true;
        }
        JLog.d(TAG, "wifi is not available.");
        return false;
    }

    /**
     * @param status 网络状态
     */
    public static String getNetWorkStatusStr(int status) {
        String statusStr = "UNKNOWN";

        switch (status) {
            case -1:
                statusStr = "UNAVAILABLE";
                break;
            case ConnectivityManager.TYPE_MOBILE:
                statusStr = "TYPE_MOBILE";
                break;
            case ConnectivityManager.TYPE_WIFI:
                statusStr = "TYPE_WIFI";
                break;
            case ConnectivityManager.TYPE_MOBILE_MMS:
                statusStr = "TYPE_MOBILE_MMS";
                break;
            case ConnectivityManager.TYPE_MOBILE_SUPL:
                statusStr = "TYPE_MOBILE_SUPL";
                break;
            case ConnectivityManager.TYPE_MOBILE_DUN:
                statusStr = "TYPE_MOBILE_DUN";
                break;
            case ConnectivityManager.TYPE_MOBILE_HIPRI:
                statusStr = "TYPE_MOBILE_HIPRI";
                break;
            case ConnectivityManager.TYPE_WIMAX:
                statusStr = "TYPE_WIMAX";
                break;
            case ConnectivityManager.TYPE_BLUETOOTH:
                statusStr = "TYPE_BLUETOOTH";
                break;
            case ConnectivityManager.TYPE_DUMMY:
                statusStr = "TYPE_DUMMY";
                break;
            case ConnectivityManager.TYPE_ETHERNET:
                statusStr = "TYPE_ETHERNET";
                break;
//            case ConnectivityManager.TYPE_VPN:
//                statusStr = "TYPE_VPN";
//                break;
        }
        return statusStr;
    }
}
