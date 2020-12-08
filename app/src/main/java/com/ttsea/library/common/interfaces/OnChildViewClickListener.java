package com.ttsea.library.common.interfaces;

import android.view.View;

/**
 * 可用于为ExpandableListView child item中的某个view设置监听 <br>
 * <p>
 * <b>more:</b>更多请点 <a href="http://www.ttsea.com" target="_blank">这里</a> <br>
 * <b>date:</b> 2017/4/10 9:55 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
public interface OnChildViewClickListener {

    /**
     * child item中的view被点击
     *
     * @param v             被点击的View
     * @param groupPosition v所处的组位置
     * @param childPosition v所处的child位置
     */
    void onChildViewClick(View v, int groupPosition, int childPosition);
}
