package com.ttsea.library.common.interfaces;

import android.view.View;

/**
 * 防止view多次重复点击<br>
 * <p>
 * <b>more:</b>更多请点 <a href="http://www.ttsea.com" target="_blank">这里</a> <br>
 * <b>date:</b> 2017/4/10 9:55 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
public abstract class OnSingleClickListener implements View.OnClickListener {

    /** 默认连续两次点击事件间隔 */
    private static final long DEFAULT_CLICK_INTERVAL = 300;

    private long clickInterval = DEFAULT_CLICK_INTERVAL;
    private long clickPointTime = 0;
    private long clickPointId = 0;

    /**
     * 默认构成函数，连续两次点击时间间隔为{@link #clickInterval}
     */
    public OnSingleClickListener() {
        this(DEFAULT_CLICK_INTERVAL);
    }

    /**
     * 构造函数
     *
     * @param clickInterval 连续两次点击的事件间隔
     */
    public OnSingleClickListener(long clickInterval) {
        this.clickInterval = clickInterval;
    }

    @Override
    public void onClick(View v) {
        long timeMillis = System.currentTimeMillis();
        //连续点击的事件小于指定时间，并且连续点击的是同一个view
        if (timeMillis - clickPointTime < clickInterval
                && clickPointId == v.getId()) {
            return;
        }
        clickPointTime = timeMillis;
        clickPointId = v.getId();
        onSingleClick(v);
    }

    /**
     * 执行点击事件
     *
     * @param v 被点击的View
     */
    public abstract void onSingleClick(View v);
}
