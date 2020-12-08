package com.ttsea.library.common.interfaces;

import android.view.View;

/**
 * 可用于为ListView item中的某个view设置监听 <br>
 * <p>
 * <b>more:</b>更多请点 <a href="http://www.ttsea.com" target="_blank">这里</a> <br>
 * <b>date:</b> 2017/4/10 9:55 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
public interface OnItemViewClickListener {

    /**
     * listitem中view被点击
     *
     * @param v        被点击的view
     * @param position v所处位置
     */
    void onItemViewClick(View v, int position);
}
