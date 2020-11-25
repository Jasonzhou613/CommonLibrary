package com.ttsea.commonlibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ttsea.commonlibrary.debug.JLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;

/**
 * SharedPreferences工具 <br>
 * <p>
 * <b>more:</b>更多请点 <a href="http://www.ttsea.com" target="_blank">这里</a> <br>
 * <b>date:</b> 2017/4/10 9:55 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
final public class SharedPreferencesUtils {
    private final static String TAG = "SharedPreferencesUtils";

    /**
     * 保存数据的方法，拿到保存数据的具体类型，然后根据类型调用不同的保存方法<br>
     * SharedPreferences name 为
     * {@link #getDefaultSharedPreferencesName(Context)}
     *
     * @param context 上下文
     * @param key     SharedPreferences key
     * @param object  要保存的数据
     * @return 是否保存成功
     */
    public static boolean put(Context context, String key, Object object) {
        return put(context, getDefaultSharedPreferencesName(context), key, object);
    }

    /**
     * 保存数据的方法，拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context  上下文
     * @param fileName SharedPreferences name
     * @param key      SharedPreferences key
     * @param object   要保存的数据
     * @return 是否保存成功
     */
    public static boolean put(Context context, String fileName, String key, Object object) {
        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }

        return editor.commit();
    }

    /**
     * 得到保存数据的方法，默认值得到保存的数据的具体类型，然后调用相对于的方法获取值<br>
     * SharedPreferences name 为
     * {@link #getDefaultSharedPreferencesName(Context)}
     *
     * @param context       上下文
     * @param key           SharedPreferences key
     * @param defaultObject 默认值
     * @return 返回取到的值或者null
     */
    public static Object get(Context context, String key, Object defaultObject) {
        return get(context, getDefaultSharedPreferencesName(context), key, defaultObject);
    }

    /**
     * 得到保存数据的方法，默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context       上下文
     * @param fileName      SharedPreferences name
     * @param key           SharedPreferences key
     * @param defaultObject 默认值
     * @return 返回取到的值或者null
     */
    public static Object get(Context context, String fileName, String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 保存数据String类型数据<br>
     * SharedPreferences name 为
     * {@link #getDefaultSharedPreferencesName(Context)}
     *
     * @param context 上下文
     * @param key     SharedPreferences key
     * @param value   要保存的数据
     * @return 是否保存成功
     */
    public static boolean putString(Context context, String key, String value) {
        return putString(context, getDefaultSharedPreferencesName(context), key, value);
    }

    /**
     * 保存数据String类型数据
     *
     * @param context  上下文
     * @param fileName SharedPreferences name
     * @param key      SharedPreferences key
     * @param value    要保存的数据
     * @return 是否保存成功
     */
    public static boolean putString(Context context, String fileName, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);

        return editor.commit();
    }

    /**
     * 根据key获取String<br>
     * SharedPreferences name 为
     * {@link #getDefaultSharedPreferencesName(Context)}
     *
     * @param context      上下文
     * @param key          SharedPreferences key
     * @param defaultValue 默认值
     * @return 返回取到的值
     */
    public static String getString(Context context, String key, String defaultValue) {
        return getString(context, getDefaultSharedPreferencesName(context), key, defaultValue);
    }

    /**
     * 根据key获取String
     *
     * @param context      上下文
     * @param fileName     SharedPreferences name
     * @param key          SharedPreferences key
     * @param defaultValue 默认值
     * @return 返回取到的值
     */
    public static String getString(Context context, String fileName, String key, String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        return sp.getString(key, defaultValue);
    }

    /**
     * 保存数据int类型数据<br>
     * SharedPreferences name 为
     * {@link #getDefaultSharedPreferencesName(Context)}
     *
     * @param context 上下文
     * @param key     SharedPreferences key
     * @param value   要保存的数据
     * @return 是否保存成功
     */
    public static boolean putInt(Context context, String key, int value) {
        return putInt(context, getDefaultSharedPreferencesName(context), key, value);
    }

    /**
     * 保存数据int类型数据
     *
     * @param context  上下文
     * @param fileName SharedPreferences name
     * @param key      SharedPreferences key
     * @param value    要保存的数据
     * @return 是否保存成功
     */
    public static boolean putInt(Context context, String fileName, String key, int value) {

        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);

        return editor.commit();
    }

    /**
     * 根据key获取int<br>
     * SharedPreferences name 为
     * {@link #getDefaultSharedPreferencesName(Context)}
     *
     * @param context      上下文
     * @param key          SharedPreferences key
     * @param defaultValue 默认值
     * @return 返回取到的值
     */
    public static int getInt(Context context, String key, int defaultValue) {
        return getInt(context, getDefaultSharedPreferencesName(context), key, defaultValue);
    }

    /**
     * 根据key获取int
     *
     * @param context      上下文
     * @param fileName     SharedPreferences name
     * @param key          SharedPreferences key
     * @param defaultValue 默认值
     * @return 返回取到的值
     */
    public static int getInt(Context context, String fileName, String key, int defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    /**
     * 保存数据Boolean类型数据<br>
     * SharedPreferences name 为
     * {@link #getDefaultSharedPreferencesName(Context)}
     *
     * @param context 上下文
     * @param key     SharedPreferences key
     * @param value   要保存的数据
     * @return 是否保存成功
     */
    public static boolean putBoolean(Context context, String key, boolean value) {
        return putBoolean(context, getDefaultSharedPreferencesName(context), key, value);
    }

    /**
     * 保存数据Boolean类型数据
     *
     * @param context  上下文
     * @param fileName SharedPreferences name
     * @param key      SharedPreferences key
     * @param value    要保存的数据
     * @return 是否保存成功
     */
    public static boolean putBoolean(Context context, String fileName, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);

        return editor.commit();
    }

    /**
     * 根据key获取Boolean<br>
     * SharedPreferences name 为
     * {@link #getDefaultSharedPreferencesName(Context)}
     *
     * @param context      上下文
     * @param key          SharedPreferences key
     * @param defaultValue 默认值
     * @return 返回取到的值
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getBoolean(context, getDefaultSharedPreferencesName(context), key, defaultValue);
    }

    /**
     * 根据key获取Boolean
     *
     * @param context      上下文
     * @param fileName     SharedPreferences name
     * @param key          SharedPreferences key
     * @param defaultValue 默认值
     * @return 返回取到的值
     */
    public static boolean getBoolean(Context context, String fileName, String key, boolean defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    /**
     * 保存数据Float类型数据<br>
     * SharedPreferences name 为
     * {@link #getDefaultSharedPreferencesName(Context)}
     *
     * @param context 上下文
     * @param key     SharedPreferences key
     * @param value   要保存的数据
     * @return 是否保存成功
     */
    public static boolean putFloat(Context context, String key, float value) {
        return putFloat(context, getDefaultSharedPreferencesName(context), key, value);
    }

    /**
     * 保存数据Float类型数据
     *
     * @param context  上下文
     * @param fileName SharedPreferences name
     * @param key      SharedPreferences key
     * @param value    要保存的数据
     * @return 是否保存成功
     */
    public static boolean putFloat(Context context, String fileName, String key, float value) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);

        return editor.commit();
    }

    /**
     * 根据key获取Float<br>
     * SharedPreferences name 为
     * {@link #getDefaultSharedPreferencesName(Context)}
     *
     * @param context      上下文
     * @param key          SharedPreferences key
     * @param defaultValue 默认值
     * @return 返回取到的值
     */
    public static float getFloat(Context context, String key, float defaultValue) {
        return getFloat(context, getDefaultSharedPreferencesName(context), key, defaultValue);
    }

    /**
     * 根据key获取Float
     *
     * @param context      上下文
     * @param fileName     SharedPreferences name
     * @param key          SharedPreferences key
     * @param defaultValue 默认值
     * @return 返回取到的值
     */
    public static float getFloat(Context context, String fileName, String key, float defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        return sp.getFloat(key, defaultValue);
    }

    /**
     * 保存数据Long类型数据<br>
     * SharedPreferences name 为
     * {@link #getDefaultSharedPreferencesName(Context)}
     *
     * @param context 上下文
     * @param key     SharedPreferences key
     * @param value   要保存的数据
     * @return 是否保存成功
     */
    public static boolean putLong(Context context, String key, long value) {
        return putLong(context, getDefaultSharedPreferencesName(context), key, value);
    }

    /**
     * 保存数据Long类型数据
     *
     * @param context  上下文
     * @param fileName SharedPreferences name
     * @param key      SharedPreferences key
     * @param value    要保存的数据
     * @return 是否保存成功
     */
    public static boolean putLong(Context context, String fileName, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);

        return editor.commit();
    }

    /**
     * 根据key获取Long<br>
     * SharedPreferences name 为
     * {@link #getDefaultSharedPreferencesName(Context)}
     *
     * @param context      上下文
     * @param key          SharedPreferences key
     * @param defaultValue 默认值
     * @return 返回取到的值
     */
    public static long getLong(Context context, String key, long defaultValue) {
        return getLong(context, getDefaultSharedPreferencesName(context), key, defaultValue);
    }

    /**
     * 根据key获取Long
     *
     * @param context      上下文
     * @param fileName     SharedPreferences name
     * @param key          SharedPreferences key
     * @param defaultValue 默认值
     * @return 返回取到的值
     */
    public static long getLong(Context context, String fileName, String key, long defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        return sp.getLong(key, defaultValue);
    }

    /**
     * 保存数据Set<String>类型数据<br>
     * SharedPreferences name 为
     * {@link #getDefaultSharedPreferencesName(Context)}
     *
     * @param context 上下文
     * @param key     SharedPreferences key
     * @param value   要保存的数据
     * @return 是否保存成功
     */
    public static boolean putStringSet(Context context, String key, Set<String> value) {
        return putStringSet(context, getDefaultSharedPreferencesName(context), key, value);
    }

    /**
     * 保存数据Set<String>类型数据
     *
     * @param context  上下文
     * @param fileName SharedPreferences name
     * @param key      SharedPreferences key
     * @param value    要保存的数据
     * @return 是否保存成功
     */
    public static boolean putStringSet(Context context, String fileName, String key, Set<String> value) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(key, value);

        return editor.commit();
    }

    /**
     * 根据key获取Set<String><br>
     * SharedPreferences name 为
     * {@link #getDefaultSharedPreferencesName(Context)}
     *
     * @param context      上下文
     * @param key          SharedPreferences key
     * @param defaultValue 默认值
     * @return 返回取到的值
     */
    public static Set<String> getStringSet(Context context, String key, Set<String> defaultValue) {
        return getStringSet(context, getDefaultSharedPreferencesName(context), key, defaultValue);
    }

    /**
     * 根据key获取Set<String>
     *
     * @param context      上下文
     * @param fileName     SharedPreferences name
     * @param key          SharedPreferences key
     * @param defaultValue 默认值
     * @return 返回取到的值
     */
    public static Set<String> getStringSet(Context context, String fileName, String key, Set<String> defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        return sp.getStringSet(key, defaultValue);
    }

    /** 默认的SharedPreferences name: context.getPackageName() + "_preferences" */
    private static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }

    /**
     * 将对象序列化后以16进制保存到SharedPreferences中
     *
     * @param context 上下文
     * @param obj     要保存的对象，obj必须完成Serializable接口
     */
    public static void putObject(Context context, Object obj) {
        if (obj == null) {
            throw new NullPointerException("obj is null");
        }
        if (!(obj instanceof Serializable)) {
            throw new IllegalArgumentException(obj.getClass().getSimpleName() + " must implements Serializable");
        }

        // 这个文件名不要改动，是与getObject中的fileName对应的
        String fileName = "_j_sp_obj";
        String key = obj.getClass().getSimpleName();
        try {
            // 保存对象
            SharedPreferences.Editor sharedata = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
            // 先将序列化结果写到byte缓存中，其实就分配一个内存空间
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            // 将对象序列化写入byte缓存
            os.writeObject(obj);
            // 将序列化的数据转为16进制保存
            String bytesToHexString = bytesToHexString(bos.toByteArray());
            // 保存该16进制数组
            sharedata.putString(key, bytesToHexString);
            sharedata.apply();

        } catch (IOException e) {
            e.printStackTrace();
            JLog.e(TAG, "IOException e:" + e.getMessage());
        }
    }

    /**
     * 读取保存的数据
     *
     * @param context 上下文
     * @param clazz   要返回的类型
     * @return T 或者 null
     */
    public static <T> T getObject(Context context, final Class<T> clazz) {
        // 这个文件名不要改动，是与putObject中的fileName对应的
        String fileName = "_j_sp_obj";
        Foo<T> foo = new Foo<T>(clazz);
        String key = foo.getGenericType();

        try {
            SharedPreferences shareData = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
            if (shareData.contains(key)) {
                String data = shareData.getString(key, "");
                if (TextUtils.isEmpty(data)) {
                    return null;
                } else {
                    // 将16进制的数据转为数组，准备反序列化
                    byte[] stringToBytes = StringToBytes(data);
                    ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                    ObjectInputStream is = new ObjectInputStream(bis);
                    // 返回反序列化得到的对象
                    Object obj = is.readObject();

                    return (T) obj;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            JLog.e(TAG, "IOException e:" + e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            JLog.e(TAG, "Exception e:" + e.getMessage());
        }
        // 所有异常返回null
        return null;
    }

    /**
     * 将byte数组转换成String
     *
     * @param bArray 需要转换的数组
     * @return String or null
     */
    private static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将String转换成byte数据
     *
     * @param data String
     * @return byte数组 or null
     */
    private static byte[] StringToBytes(String data) {
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch; // 两位16进制数转化后的10进制数
            char hex_char1 = hexString.charAt(i); // 两位16进制数中的第一位(高位*16)
            int int_ch3;
            if (hex_char1 >= '0' && hex_char1 <= '9')
                int_ch3 = (hex_char1 - 48) * 16; // 0 的Ascll - 48
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch3 = (hex_char1 - 55) * 16; // A 的Ascll - 65
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i); // 两位16进制数中的第二位(低位)
            int int_ch4;
            if (hex_char2 >= '0' && hex_char2 <= '9')
                int_ch4 = (hex_char2 - 48); // 0 的Ascll - 48
            else if (hex_char2 >= 'A' && hex_char2 <= 'F')
                int_ch4 = hex_char2 - 55; // A 的Ascll - 65
            else
                return null;
            int_ch = int_ch3 + int_ch4;
            retData[i / 2] = (byte) int_ch;// 将转化后的数放入Byte里
        }
        return retData;
    }

    private static class Foo<T> {
        final Class<T> typeParameterClass;

        public Foo(Class<T> typeParameterClass) {
            this.typeParameterClass = typeParameterClass;
        }

        public String getGenericType() {
            return typeParameterClass.getSimpleName();
        }
    }
}
