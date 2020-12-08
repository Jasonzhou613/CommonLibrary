package com.ttsea.commonlibrary.debug;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.ttsea.commonlibrary.utils.RegexUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

/**
 * 打印数据工具类<br>
 * <p>
 * <b>date:</b> 2018/5/18 15:37 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
final public class PrintUtils {
    private static String TAG = "PrintUtils";

    /**
     * 将content中的内容保存至外置SD卡的指定文件夹中，保存名如：2015-07-21_21_55_01_log<br/>
     * 需要读写SD卡的权限: android.permission.WRITE_EXTERNAL_STORAGE
     *
     * @param context  上下文
     * @param content  保存的内容
     * @param fileName 保存的文件名
     */
    public static void saveStringForTest(Context context, String content, String dir, String fileName) {
        JLog.d(TAG, "Start save content...");
        String filePath = dir + "/" + fileName;
        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            out.write(content.getBytes());
            out.close();
            JLog.d(TAG, "End save content. The file name is: " + filePath);

        } catch (Exception e) {
            JLog.e(TAG, "Save content error, Exception:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 将文件拷贝到制定目录
     *
     * @param scrFilePath 源文件(包括路径)
     * @param desDirPath  要拷贝到的目录
     */
    public static void copyFile(String scrFilePath, String desDirPath) {
        File srcFile = new File(scrFilePath);
        File desDir = new File(desDirPath);
        InputStream fis = null;
        OutputStream fos = null;
        if (!srcFile.exists()) {
            JLog.e(TAG, "srcFile not exist, srcFile=" + srcFile);
            return;
        }
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        String fileName = srcFile.getName();
        File desFile = new File(desDirPath + File.separator + fileName);
        if (desFile.exists()) {
            desFile.delete();
        }
        try {
            desFile.createNewFile();
        } catch (IOException e) {
            JLog.e(TAG, "copyFile, IOException e:" + e.toString());
        }

        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(desFile);
            byte[] b = new byte[1024];
            int i;
            while ((i = fis.read(b)) != -1) {
                fos.write(b, 0, i);
            }
            JLog.d(TAG, "copy file successful, desFile=" + desFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            JLog.e(TAG, "Exception e:" + e.getMessage());

        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void printCursor(Cursor c) {
        JLog.d(TAG, "cursorCount:" + c.getCount());
        StringBuilder builder = new StringBuilder("[");
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            builder.setLength(0);
            for (int i = 0; i < c.getColumnCount(); i++) {
                builder.append(c.getColumnName(i))
                        .append(":")
                        .append(c.getString(i))
                        .append(", ");
            }
            JLog.d(TAG, builder.toString());
        }
    }

    public static void printStringArray(String[] array) {
        JLog.d(TAG, "array length:" + array.length);
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            if (i < (array.length - 1)) {
                builder.append(array[i] + ", ");
            } else {
                builder.append(array[i]);
            }
        }
        builder.append("]");
        JLog.d(builder.toString());
    }

    /**
     * 遍历Bundle,将其转换成String，如 bundle{key:value, key:value}
     *
     * @param bundle 要遍历的bundle
     * @return String or null
     */
    public static String bundle2String(Bundle bundle) {
        if (bundle == null) {
            return "bundle is null";
        }

        StringBuffer buffer = new StringBuffer();
        Set<String> keySet = bundle.keySet(); // 获取所有的Key,
        for (String key : keySet) { // bundle.get(key);来获取对应的value
            buffer.append(key).append(":").append(bundle.get(key)).append(", ");
        }
        String result = "bundle{ " + buffer.toString() + "}";
        result = RegexUtils.replaceLast(result, ", ", " ");
        return result;
    }

    /**
     * 将map打印出来：[{key1:value2, key2:value2}]
     *
     * @param map
     * @return
     */
    public static <K, T> String map2String(Map<K, T> map) {
        StringBuffer buffer = new StringBuffer();
        if (map == null) {
            return null;
        }
        buffer.append("Map[{");
        for (Map.Entry<K, T> entry : map.entrySet()) {
            K key = entry.getKey();
            T value = entry.getValue();
            buffer.append(key).append(":").append(value).append(", ");

        }
        buffer.append("}]");

        return RegexUtils.replaceLast(buffer.toString(), ", ", "");
    }
}
