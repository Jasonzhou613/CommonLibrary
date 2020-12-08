package com.ttsea.library.common;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 给RecyclerView item设置间距，适用于{@link LinearLayoutManager}和{@link GridLayoutManager}
 * 且orientation为{@link LinearLayoutManager#HORIZONTAL}的情况<br>
 * 注：需要主动设置每个item的宽度和高度<br>
 * <b>more:</b>更多请点 <a href="http://www.ttsea.com" target="_blank">这里</a> <br>
 * <b>date:</b> 2020/11/11 11:28 <br>
 * <b>author:</b> Jasonzhou <br>
 * <b>version:</b> 1.0 <br>
 * <b>last modified date:</b> 2020/11/11 11:28.
 */
public class ItemDecorationHorizontal extends ItemDecorationVertical {
    private final Rect mRect = new Rect();

    /**
     * @param spanCount       列数
     * @param horizontalSpace 水平间距
     * @param verticalSpace   垂直间距
     */
    public ItemDecorationHorizontal(int spanCount, int horizontalSpace, int verticalSpace) {
        super(spanCount, horizontalSpace, verticalSpace);
    }


    /**
     * @param spanCount          列数
     * @param firstRowTopSpace   第一行与头部间距
     * @param lastRowBottomSpace 最后一行与底部的间距
     * @param horizontalSpace    水平间距
     * @param verticalSpace      垂直间距
     */
    public ItemDecorationHorizontal(int spanCount, int firstRowTopSpace, int lastRowBottomSpace, int horizontalSpace, int verticalSpace) {
        super(spanCount, firstRowTopSpace, lastRowBottomSpace, horizontalSpace, verticalSpace);
    }

    /**
     * @param spanCount            列数
     * @param firstRowTopSpace     第一行与头部间距
     * @param lastRowBottomSpace   最后一行与底部的间距
     * @param firstColumnLeftSpace 第一列与左边的距离
     * @param lastColumnRightSpace 最后一列与右边的距离
     * @param horizontalSpace      水平间距
     * @param verticalSpace        垂直间距
     */
    public ItemDecorationHorizontal(int spanCount, int firstRowTopSpace, int lastRowBottomSpace,
                                    int firstColumnLeftSpace, int lastColumnRightSpace, int horizontalSpace, int verticalSpace) {
        super(spanCount, firstColumnLeftSpace, lastColumnRightSpace,
                firstRowTopSpace, lastRowBottomSpace, verticalSpace, horizontalSpace);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.Adapter<?> adapter = parent.getAdapter();
        if (adapter == null || adapter.getItemCount() == 0) {
            return;
        }

        outRect = getRectOffsets(outRect, view, parent, state);
        mRect.set(outRect);

        outRect.setEmpty();
        outRect.left = mRect.top;
        outRect.right = mRect.bottom;
        outRect.top = mRect.left;
        outRect.bottom = mRect.right;
    }
}
