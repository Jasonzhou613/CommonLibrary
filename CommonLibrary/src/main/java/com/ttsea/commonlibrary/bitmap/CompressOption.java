package com.ttsea.commonlibrary.bitmap;

import android.graphics.Bitmap;

/**
 * 图片压缩选项 <br>
 *
 * <p>
 * <b>date:</b> 2018/5/18 15:37 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
public class CompressOption {
    /** 压缩后最大的宽度，<=0的时候表示不压缩宽度，默认为：0 */
    private int maxHeight;
    /** 压缩后最大的高度，<=0的时候表示不压缩高度，默认为：0 */
    private int maxWidth;
    /** 压缩后最大的大小，<=0的时候表示不压缩大小，默认为：0 */
    private long maxSizeInKB;

    /** 压缩格式，默认为{@link Bitmap.Config#ARGB_8888} */
    private Bitmap.Config config = Bitmap.Config.ARGB_8888;
    /** 压缩长宽的时候，是否锁定比例，默认：true */
    private boolean lockRatio = true;

    /**
     * see {@link #maxHeight}
     */
    public int getMaxHeight() {
        return maxHeight;
    }

    /**
     * see {@link #maxHeight}
     *
     * @param maxHeight
     */
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
     * see {@link #maxWidth}
     */
    public int getMaxWidth() {
        return maxWidth;
    }

    /**
     * see {@link #maxWidth}
     *
     * @param maxWidth
     */
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    /**
     * see {@link #maxSizeInKB}
     */
    public long getMaxSizeInKB() {
        return maxSizeInKB;
    }

    /**
     * see {@link #maxSizeInKB}
     *
     * @param maxSizeInKB
     */
    public void setMaxSizeInKB(long maxSizeInKB) {
        this.maxSizeInKB = maxSizeInKB;
    }

    /**
     * see {@link #config}
     */
    public Bitmap.Config getConfig() {
        return config;
    }

    /**
     * see {@link #config}
     *
     * @param config
     */
    public void setConfig(Bitmap.Config config) {
        this.config = config;
    }

    /**
     * see {@link #lockRatio}
     */
    public boolean isLockRatio() {
        return lockRatio;
    }

    /**
     * see {@link #lockRatio}
     *
     * @param lockRatio
     */
    public void setLockRatio(boolean lockRatio) {
        this.lockRatio = lockRatio;
    }

    @Override
    public String toString() {
        return "CompressOption{" +
                "maxHeight=" + maxHeight +
                ", maxWidth=" + maxWidth +
                ", maxSizeInKB=" + maxSizeInKB +
                ", config=" + config +
                ", lockRatio=" + lockRatio +
                '}';
    }
}
