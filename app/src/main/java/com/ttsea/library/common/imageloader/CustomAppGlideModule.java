package com.ttsea.library.common.imageloader;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.ttsea.commonlibrary.debug.JLog;

import java.io.File;

/**
 * //描述
 * <b>more:</b>更多请点 <a href="http://www.ttsea.com" target="_blank">这里</a> <br>
 * <b>date:</b> 2020/7/25 16:56 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
@GlideModule
public class CustomAppGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //设置Glide缓存目录
        int cacheSizeBytes = 20971520;// 20M
        String diskCacheName = "glideCache";
        String cacheDir = context.getCacheDir().getAbsolutePath();
        builder.setDiskCache(new DiskLruCacheFactory(cacheDir, diskCacheName, cacheSizeBytes));
        JLog.d("cacheDir:" + cacheDir + File.separator + diskCacheName);
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        //do nothing
    }
}
