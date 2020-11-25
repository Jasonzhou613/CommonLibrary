package com.ttsea.commonlibrary.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

import com.ttsea.commonlibrary.debug.JLog;

/**
 * 获取SD卡的状态和剩余容量等 <br>
 * <p>
 * <b>more:</b>更多请点 <a href="http://www.ttsea.com" target="_blank">这里</a> <br>
 * <b>date:</b> 2017/4/10 9:55 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
final public class SdStatusUtils {
    private final static String TAG = "Utils.SdStatusUtils";

    /**
     * 判断SD卡的状态是否可用
     *
     * @return SD卡可用则返回true，否则返回false
     */
    public static boolean isSDAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            JLog.d(TAG, "SD card is available.");
            return true;

        } else {
            JLog.d(TAG, "SD card is not available or not exist.");
            return false;
        }
    }

    /**
     * 获取外置sd卡的绝对路径
     *
     * @return 返回外置SD卡的绝对路径，如果SD卡没用，则返回null
     */
    public static String getExternalStorageAbsoluteDir() {
        if (isSDAvailable()) {
            File sdFile = Environment.getExternalStorageDirectory();
            String rootDir = sdFile.getAbsolutePath();
            JLog.d(TAG, "External storage root dir=" + rootDir);
            return rootDir;
        }
        return null;
    }

    /**
     * 获取SD卡的剩余容量，单位MB
     *
     * @return 返回SD卡的剩余容量，若SD卡不可用则返回0
     */
    public static long getAvailableBlockMB() {
        long availableBlock = 0;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 取得SD卡文件路径
            File sdFile = Environment.getExternalStorageDirectory();
            // StatFs看文件系统空间的使用情况
            StatFs statFs = new StatFs(sdFile.getPath());
            // Block的size
            long blockSize = statFs.getBlockSize();
            // 已使用的Block数
            availableBlock = statFs.getAvailableBlocks();
            availableBlock = (availableBlock * blockSize) / (1024 * 1024);// 换算成MB

        } else {
            // Toast.makeText(mContext,
            // mResource.getString(R.string.sdcard_not_available),
            // Toast.LENGTH_SHORT).show();
            availableBlock = 0;
            JLog.d(TAG, "SD card is not available or not exist.");
        }
        JLog.d(TAG, "The residual capacity for SD card is: "
                + String.valueOf(availableBlock) + " MB");

        return availableBlock;
    }

    /**
     * 判读SD卡是否存在且剩余的容量是否大于minAvailableBlock，单位MB
     *
     * @param minAvailableBlockMB SD的最小可用容量
     * @return 如果SD卡的剩余容量大于minAvailableBlock则返回true，否则返回false
     */
    public static boolean isABlockEnough(long minAvailableBlockMB) {
        long availableBlock = getAvailableBlockMB();

        if (availableBlock > minAvailableBlockMB) {
            return true;

        } else {
            JLog.d(TAG, "SDcard space not enough, the min available block is "
                    + String.valueOf(minAvailableBlockMB) + " MB");
            return false;
        }
    }
}
