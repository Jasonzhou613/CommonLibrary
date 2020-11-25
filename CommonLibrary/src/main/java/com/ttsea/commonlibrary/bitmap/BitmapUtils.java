package com.ttsea.commonlibrary.bitmap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;


import com.ttsea.commonlibrary.debug.JLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Bitmap处理类 <br>
 *
 * <p>
 * <b>date:</b> 2018/5/18 15:37 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
public class BitmapUtils {
    private static boolean DEBUG = false;

    /**
     * 设置是否为调试模式
     *
     * @param debug true:会打印log，false:不会打印log
     */
    public static void enableDebug(boolean debug) {
        DEBUG = debug;
    }

    /**
     * 压缩图片文件<br>
     * 1.使用采样率压缩方式，读取图片<br>
     * 2.如果option设置了最大宽高，如果符合条件，则缩小图片<br>
     * 3.如果option设置了文件最大值，则才用质量压缩方式对图片进行压缩<br>
     * 4.如果原图是被旋转了，则会尝试旋转回来<br>
     *
     * @param path   图片路径
     * @param option 压缩选项
     * @return Bitmap
     * @throws Exception
     */
    public static Bitmap compressPicFile(String path, CompressOption option) throws Exception {

        File picFile = new File(path);
        if (!picFile.exists()) {
            throw new FileNotFoundException("file not found, path:" + path);
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //只读文件的轮廓（长宽）
        BitmapFactory.decodeFile(path, options);

        if (DEBUG) {
            JLog.d("origin pic W*H:" + options.outWidth + "x" + options.outHeight
                    + ", pic file size:" + picFile.length() / 1024 + "kb");
        }

        //采样率压缩
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options, option.getMaxWidth(), option.getMaxHeight());
        options.inPreferredConfig = option.getConfig();

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        if (DEBUG) {
            JLog.d("inSampleSize:" + options.inSampleSize
                    + ", bitmap W*H:" + bitmap.getWidth() + "x" + bitmap.getHeight()
                    + ", bitmap size:" + bitmap.getByteCount() / 1024 + "kb"
            );
        }

        bitmap = compressBitmap(bitmap, option);

        //确认图片是否有被旋转，如果有，则将其旋转回来
        int degree = readPicDegree(path);
        if (degree != 0) {
            bitmap = rotateBitmap(bitmap, degree);

        } else {
            if (DEBUG) {
                JLog.d("pic has not rotated, do not need to rotated...");
            }
        }

        return bitmap;
    }

    /**
     * 压缩图片（采用质量压缩）<br>
     * 如果定义了最大宽高，则首先会判断是否要对图片缩小
     *
     * @param bitmap 待压缩的位图
     * @param option 压缩选项
     * @return Bitmap
     * @throws Exception
     */
    public static Bitmap compressBitmap(Bitmap bitmap, CompressOption option) throws Exception {

        // 如果定义了最大宽高，则首先会判断是否要对图片缩小
        bitmap = zoomOutBitmap(bitmap, option.getMaxWidth(), option.getMaxHeight(), option.isLockRatio());

        //表示没有设置最大值，不使用质量压缩
        if (option.getMaxSizeInKB() <= 0) {
            if (DEBUG) {
                JLog.d("MaxSizeInKB not defined, do not need compress bitmap...");
            }
            return bitmap;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        int bitmapSize = baos.toByteArray().length / 1024;

        if (bitmapSize <= option.getMaxSizeInKB()) {
            if (DEBUG) {
                JLog.d("file size is less than maxSizeInKB, do not need to compress bitmap, "
                        + "bitmapSize:" + bitmapSize + ", maxSizeInKB:" + option.getMaxSizeInKB());
            }
            return bitmap;
        }

        int quality = 90;

        // 循环判断如果压缩后图片是否大于maxSizeInKB,大于继续压缩
        while (bitmapSize > option.getMaxSizeInKB()) {
            baos.reset();// 重置baos即清空baos
            // 这里压缩options%，把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

            //质量压缩到一定值后，bitmap返回的大小会一直一样（可能是不能再压缩了）
            // 所以这里有可能是bitmapSize会一直大于option.getMaxSizeInKB()
            //这时候会导致死循环，所以这里需要判断下，如果上次的结果与这次一下，则跳出循环
            if (bitmapSize == baos.toByteArray().length / 1024) {
                JLog.d("compressBitmap, break");
                break;
            }

            bitmapSize = baos.toByteArray().length / 1024;

            if (DEBUG) {
                JLog.d("compressing bitmap, file size:" + bitmapSize
                        + ", maxSizeInKB:" + option.getMaxSizeInKB());
            }
        }

        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());

        // 把ByteArrayInputStream数据生成图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outWidth = bitmap.getWidth();
        options.outHeight = bitmap.getHeight();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 1;
        options.inPreferredConfig = option.getConfig();

        bitmap = BitmapFactory.decodeStream(isBm, null, options);

        baos.flush();
        baos.close();
        isBm.reset();
        isBm.close();

        if (DEBUG && bitmap != null) {
            JLog.d("compressBitmap finish"
                    + ", bitmap W*H:" + bitmap.getWidth() + "x" + bitmap.getHeight()
                    + ", bitmap size:" + bitmap.getByteCount() / 1024 + "kb"
            );
        }

        return bitmap;
    }


    /**
     * 缩小位图
     *
     * @param bitmap    待缩小的位图
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @param lockRatio 是否锁定长宽比例
     * @return Bitmap
     */
    public static Bitmap zoomOutBitmap(Bitmap bitmap, int maxWidth, int maxHeight, boolean lockRatio) {
        //表示没有设置最大长宽，不缩放
        if (maxWidth <= 0 && maxHeight <= 0) {
            if (DEBUG) {
                JLog.d("maxWidth and maxHeight not defined, do not need to scale bitmap...");
            }
            return bitmap;
        }

        //如果设定的长宽比位图的长宽都大，则表示也不用缩放
        if (maxWidth > bitmap.getWidth() && maxHeight > bitmap.getHeight()) {
            if (DEBUG) {
                JLog.d("maxWidth and maxHeight are larger than bitmap width and height,"
                        + " do not need to scale bitmap...");
            }
            return bitmap;
        }

        float wRatio = ((float) maxWidth) / bitmap.getWidth();
        float hRatio = ((float) maxHeight) / bitmap.getHeight();

        int newWidth = bitmap.getWidth();
        int newHeight = bitmap.getHeight();

        //不锁定长宽比
        if (!lockRatio) {
            if (0 < wRatio && wRatio < 1) {
                newWidth = maxWidth;
            }

            if (0 < hRatio && hRatio < 1) {
                newHeight = maxHeight;
            }

            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

            if (DEBUG) {
                JLog.d("scaled bitmap without ratio, width:" + bitmap.getWidth()
                        + ", height:" + bitmap.getHeight()
                        + ", bitmap size:" + (bitmap.getByteCount() / 1024) + "kb"
                );
            }

            return bitmap;
        }

        //都设置了比例，则取小的那个
        float ratio = 0;
        if (0 < wRatio && wRatio < 1 && 0 < hRatio && hRatio < 1) {
            ratio = Math.min(wRatio, hRatio);

        } else if (0 < wRatio && wRatio < 1) {
            ratio = wRatio;

        } else if (0 < hRatio && hRatio < 1) {
            ratio = hRatio;
        }

        if (ratio != 0) {
            newWidth = (int) (newWidth * ratio);
            newHeight = (int) (newHeight * ratio);
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

        if (DEBUG) {
            JLog.d("scaled bitmap with ratio, width:" + bitmap.getWidth()
                    + ", height:" + bitmap.getHeight()
                    + ", bitmap size:" + (bitmap.getByteCount() / 1024) + "kb"
            );
        }

        return bitmap;
    }


    /**
     * 通过计算得到采样率
     *
     * @param options   BitmapFactory.Options
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return int
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight) {
        int inSampleSize = 1;

        if (maxWidth <= 0 && maxHeight <= 0) {
            return inSampleSize;
        }

        float ratio = ((float) options.outWidth) / options.outHeight;
        if (maxWidth > 0 && maxHeight <= 0) {
            maxHeight = (int) (maxWidth / ratio);
        }

        if (maxWidth <= 0 && maxHeight > 0) {
            maxWidth = (int) (ratio * maxHeight);
        }

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        if (height > maxHeight || width > maxWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= maxHeight
                    && (halfWidth / inSampleSize) >= maxWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * 旋转图片
     *
     * @param bitmap  待旋转的bitmap
     * @param degrees 旋转角度
     * @return Bitmap
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, float degrees) throws Exception {
        if (bitmap == null || degrees == 0) {
            return bitmap;
        }

        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        if (bitmap == null) {
            throw new IllegalArgumentException("rotate bitmap failed,return bitmap is null");
        }

        if (DEBUG) {
            JLog.d("rotate bitmap, degrees:" + degrees + ", bitmap.width:" + bitmap.getWidth()
                    + ", bitmap.height:" + bitmap.getHeight() + ", bitmap size:" + bitmap.getByteCount() / 1024 + "kb");
        }

        return bitmap;
    }

    /**
     * 读取图片文件的旋转度
     *
     * @param picPath 图片路径
     * @return 旋转度
     */
    private static int readPicDegree(String picPath) {
        int degree = 0;

        File picFile = new File(picPath);
        if (!picFile.exists()) {
            return degree;
        }

        try {
            ExifInterface exif = new ExifInterface(picPath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;

                default:
                    degree = 0;
                    break;
            }


        } catch (IOException e) {
            JLog.w("IOException e:" + e.getMessage());
        }

        return degree;
    }

    /**
     * 将bitmap保存到指定路径
     *
     * @param context  上下文
     * @param bitmap   待保存的bitmap
     * @param savePath 保存路径
     * @param fileName 保存名称
     * @return true or false
     * @throws Exception
     */
    public static boolean saveBitmap(Context context, Bitmap bitmap,
                                     String savePath, String fileName) throws Exception {

        return saveBitmap(context, bitmap, savePath, fileName, 80, false);
    }


    /**
     * 将bitmap保存到指定路径
     *
     * @param context       上下文
     * @param bitmap        待保存的bitmap
     * @param savePath      保存路径
     * @param fileName      保存名称
     * @param quality       保存图片的质量(0-100)，建议80-90
     * @param notifyGallery 保存成功后是否通知图库更新显示
     * @return true or false
     * @throws Exception
     */
    public static boolean saveBitmap(Context context, Bitmap bitmap, String savePath,
                                     String fileName, int quality, boolean notifyGallery) throws Exception {

        if (bitmap == null || bitmap.isRecycled()) {
            return false;
        }

        File file = new File(savePath, fileName);

        JLog.d("start save bitmap to file:" + file.getAbsolutePath());

        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();

        FileOutputStream fOut = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fOut);

        fOut.flush();
        fOut.close();

        if (notifyGallery) {
            // 最后通知图库更新
            Uri imgUri = Uri.parse("file://" + file.getAbsolutePath());
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imgUri);
            context.sendBroadcast(intent);
        }

        return true;
    }
}
