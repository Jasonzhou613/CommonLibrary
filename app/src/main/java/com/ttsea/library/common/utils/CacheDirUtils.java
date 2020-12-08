package com.ttsea.library.common.utils;

import android.content.Context;

import com.ttsea.commonlibrary.debug.JLog;
import com.ttsea.commonlibrary.utils.SdStatusUtils;
import com.ttsea.library.debug.Config;

import java.io.File;

/**
 * 缓存目录单元，这里可以获取app的缓存目录 <br>
 * <p>
 * <b>more:</b>更多请点 <a href="http://www.ttsea.com" target="_blank">这里</a> <br>
 * <b>date:</b> 2017/4/10 9:55 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
public final class CacheDirUtils {
    private static String TAG = "Utils.CacheDirUtils";

    private static final String DIR_DATAS = "datas";
    private static final String DIR_IMAGES = "images";
    private static final String DIR_LOGS = "logs";
    private static final String DIR_TMP = "tmp";

    /**
     * 获取缓存的根目录，返回的是绝对地址<br/>
     * {@link Config#DEBUG}=true : /sd/Android/data/[packagename]/cache<br>
     * {@link Config#DEBUG}=false : /data/data/[packagename]/cache<br>
     *
     * @param context 上下文
     * @return String
     */
    public static String getCacheDir(Context context) {

        if (Config.DEBUG) {
            File cacheDir = context.getExternalCacheDir();
            if (cacheDir != null) {
                return cacheDir.getAbsolutePath();
            }
        }

        return context.getCacheDir().getAbsolutePath();
    }

    /**
     * 获取图片的缓存目录，返回的是绝对地址<br/>
     *
     * @param context 上下文
     * @return {@link #getCacheDir(Context)}/{@link #DIR_IMAGES}<br>
     */
    public static String getImageCacheDir(Context context) {
        String imageDir = getCacheDir(context) + File.separator + DIR_IMAGES;
        createDirIfNeed(imageDir);
        return imageDir;
    }

    /**
     * 获取数据缓存的目录，返回的是绝对地址<br/>
     *
     * @param context 上下文
     * @return {@link #getCacheDir(Context)}/{@link #DIR_DATAS}<br>
     */
    public static String getDataCacheDir(Context context) {
        String imageDir = getCacheDir(context) + File.separator + DIR_DATAS;
        createDirIfNeed(imageDir);
        return imageDir;
    }

    /**
     * log存放目录
     *
     * @param context 上下文
     * @return /sd/Android/data/[packagename]/cache/logs<br>
     */
    public static String getLogDir(Context context) {
        String dir = context.getExternalCacheDir() + File.separator + DIR_LOGS;
        createDirIfNeed(dir);
        return dir;
    }

    /**
     * 获取需要存放在SD卡里根目录
     *
     * @param context 上下文
     * @return 1.SD卡可用：/sd/[app_name]<br>
     * 2.SD卡不可用：null
     */
    public static String getSdRootDir(Context context) {
        String rootDir = SdStatusUtils.getExternalStorageAbsoluteDir();
        if (rootDir == null) {
            return null;
        }

        rootDir = rootDir + File.separator + context.getPackageName();
        createDirIfNeed(rootDir);

        return rootDir;
    }

    /**
     * 获取存放在SD卡里图片的目录
     *
     * @param context 上下文
     * @return 1.SD卡可用：{@link #getSdRootDir(Context)}/{@link #DIR_IMAGES}<br>
     * 2.SD卡不可用：null
     */
    public static String getSdImageDir(Context context) {
        String rootDir = getSdRootDir(context);
        if (rootDir == null) {
            return null;
        }

        String imageDir = rootDir + File.separator + DIR_IMAGES;
        createDirIfNeed(imageDir);

        return imageDir;
    }

    /**
     * 获取存放在SD卡里数据的目录
     *
     * @param context 上下文
     * @return 1.SD卡可用：{@link #getSdRootDir(Context)}/{@link #DIR_DATAS}<br>
     * 2.SD卡不可用：null
     */
    public static String getSdDataDir(Context context) {
        String rootDir = getSdRootDir(context);
        if (rootDir == null) {
            return null;
        }

        String dataDir = rootDir + File.separator + DIR_DATAS;
        createDirIfNeed(dataDir);

        return dataDir;
    }

    /**
     * 获取SD卡临时目录
     *
     * @param context 上下文
     * @return 1.SD卡可用：{@link #getSdRootDir(Context)}/{@link #DIR_TMP}<br>
     * 2.SD卡不可用：null
     */
    public static String getSdTempDir(Context context) {
        String rootDir = getSdRootDir(context);
        if (rootDir == null) {
            return null;
        }

        String tmpDir = rootDir + File.separator + DIR_TMP;
        createDirIfNeed(tmpDir);

        return tmpDir;
    }

    /**
     * 如果该目录不存在，则创建
     *
     * @param dirPath 目录路径
     * @return 创建成功:true，创建失败:false
     */
    private static boolean createDirIfNeed(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                JLog.d(TAG, "createDirIfNeed, create success, dir:" + dirPath);
                return true;
            } else {
                JLog.w(TAG, "createDirIfNeed, create failed, dir:" + dirPath);
            }
            return false;
        }
        return true;
    }
}
