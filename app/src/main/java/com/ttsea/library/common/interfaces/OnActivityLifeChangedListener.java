package com.ttsea.library.common.interfaces;

/**
 * // to do <br>
 * <p>
 * <b>more:</b>更多请点 <a href="http://www.ttsea.com" target="_blank">这里</a> <br>
 * <b>date:</b> 2017/4/10 9:55 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
public interface OnActivityLifeChangedListener {
    /**
     * 在Activity或者Fragment中的onCreate调用，基本用不上这个方法
     */
    void onCreate();

    /**
     * 在Activity或者Fragment中的onStart调用
     */
    void onStart();

    /**
     * 在Activity或者Fragment中的onResume调用
     */
    void onResume();

    /**
     * 在Activity或者Fragment中的onPause调用
     */
    void onPause();

    /**
     * 在Activity或者Fragment中的onStop调用
     */
    void onStop();

    /**
     * 在Activity或者Fragment中的onDestroy调用
     */
    void onDestroy();
}