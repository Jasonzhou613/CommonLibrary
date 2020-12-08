package com.ttsea.library.common.imageloader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ttsea.commonlibrary.debug.JLog;
import com.ttsea.library.R;

/**
 * 图片加载器，该工程中所有的图片都将同过这个类进行加载 <br>
 * <p>
 * <b>more:</b>更多请点 <a href="http://www.ttsea.com" target="_blank">这里</a> <br>
 * <b>date:</b> 2017/4/10 9:55 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
public class ImageLoader {
    private final String TAG = "Common.HPImageLoader";

    private static ImageLoader loader;

    protected ImageLoader() {
    }

    public static void init(Context appContext) {

    }

    public static ImageLoader getInstance() {
        if (loader == null) {
            synchronized (ImageLoader.class) {
                if (loader == null) {
                    loader = new ImageLoader();
                }
            }
        }
        return loader;
    }

    public void displayImage(Context context, String path, ImageView imageView) {
        displayImage(context, path, imageView, new Transformation[]{});
    }

    public void displayImage(Context context, String path, ImageView imageView, @Nullable Transformation<Bitmap>... transformations) {
        displayImage(context, path, imageView, 1f, R.color.transparent,
                R.mipmap.photo_loading_error, false, DiskCacheStrategy.ALL,
                Priority.NORMAL, null, transformations);
    }

    @SuppressLint("CheckResult")
    public void displayImage(Context context, final String path, ImageView imageView,
                             float thumbnail, int placeholderResId, int errorResId,
                             boolean skipMemoryCache, DiskCacheStrategy strategy, Priority priority,
                             @Nullable RequestListener<Drawable> listener,
                             @Nullable Transformation<Bitmap>... transformations) {

        if (listener == null) {
            listener = new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    String errorMsg = null;
                    if (e != null) {
                        errorMsg = "class:" + e.getClass() + ", errorMsg:" + e.getMessage();
                    }
                    JLog.e("displayImage, Exception e:" + errorMsg + ", path:" + path);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                               DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            };
        }

        RequestBuilder<?> builder = Glide.with(context)
                .load(path)
                .thumbnail(thumbnail)// 一开始大小
                .timeout(30000)
                .fitCenter()
                .placeholder(placeholderResId)// 默认显示
                .error(errorResId)// 错误显示
                .skipMemoryCache(skipMemoryCache)// 是否跳过内存缓存
                .diskCacheStrategy(strategy)// 本地磁盘缓存规则
                .priority(priority)// 显示优先级
                .listener(listener);

        if (transformations != null) {
            builder.transform(transformations);
        }

        builder.into(imageView);
    }

    private void displayImageFitWidth(Context context, String path, ImageView imageView, final int minHeight) {
        RequestListener<Drawable> listener = new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                String errorMsg = null;
                if (e != null) {
                    errorMsg = "class:" + e.getClass() + ", errorMsg:" + e.getMessage();
                }
                JLog.e("displayImage, Exception e:" + errorMsg + ", path:" + path);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                           DataSource dataSource, boolean isFirstResource) {
                if (imageView == null || resource == null || resource.getBounds() == null
                        || resource.getBounds().height() == 0) {
                    return false;
                }

                float ratio = resource.getBounds().width() * 1.0f / resource.getBounds().height();
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                int height = (int) (imageView.getWidth() / ratio);

                if (minHeight > 0 && height < minHeight) {
                    height = minHeight;
                }

                params.height = height;

                imageView.setLayoutParams(params);
                return false;
            }
        };
    }

    /** 适用图片浏览 */
    public void displayImageForGallery(final Context context,
                                       final String path, final ImageView imageView,
                                       final ImageLoadingListener listener) {
        RequestListener<Drawable> RequestListener = new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                String errorMsg = null;
                if (e != null) {
                    errorMsg = "class:" + e.getClass() + ", errorMsg:" + e.getMessage();
                }
                JLog.e("displayImage, Exception e:" + errorMsg + ", path:" + path);

                if (listener != null) {
                    listener.onLoadingFailed(model, imageView, errorMsg);
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                           DataSource dataSource, boolean isFirstResource) {
                if (listener != null) {
                    listener.onLoadingComplete(model, imageView, resource.getCurrent());
                }
                return false;
            }
        };

        if (listener != null) {
            listener.onLoadingStarted(path, imageView);
        }

        Glide.with(context).load(path)
                .thumbnail(0.2f)
                .listener(RequestListener)
                .error(R.mipmap.photo_loading_error)
                .into(imageView);
    }

    public void pause(Context context) {
        JLog.d(TAG, "HPImageLoader pause...");
        Glide.with(context).pauseRequests();
    }

    public void resume(Context context) {
        JLog.d(TAG, "HPImageLoader resume...");
        Glide.with(context).resumeRequests();
    }

    public void destroy(Context context) {
        JLog.d(TAG, "HPImageLoader destroy...");
        Glide.with(context).onDestroy();
    }

    public interface ImageLoadingListener {

        void onLoadingStarted(Object model, View view);

        void onLoadingFailed(Object model, View view, String failReason);

        void onLoadingComplete(Object model, View view, Drawable drawable);

        void onLoadingCancelled(Object model, View view);

    }
}
