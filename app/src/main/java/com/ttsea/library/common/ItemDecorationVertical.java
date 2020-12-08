package com.ttsea.library.common;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ttsea.commonlibrary.utils.DisplayUtils;

/**
 * 给RecyclerView item设置间距，适用于{@link LinearLayoutManager}和{@link GridLayoutManager}
 * 且orientation为{@link LinearLayoutManager#VERTICAL}的情况<br>
 * 1.可以给RecyclerView item设置水平间距和垂直间距<br>
 * 2.第一行的top间距和最后一行bottom间距根据需要再额外设置<br>
 * 3.第一列的left间距和最后一列right间距根据需要再额外设置<br>
 * 4.可以不用主动设置每个item的宽度，该类会根据设置好的各种间距自动计算出剩余宽度，然后每个item平分这个宽度。
 * item高度需要根据实际情况自己去设置<br>
 * <p>
 * <b>more:</b>更多请点 <a href="http://www.ttsea.com" target="_blank">这里</a> <br>
 * <b>date:</b> 2020/11/11 11:28 <br>
 * <b>author:</b> Jasonzhou <br>
 * <b>version:</b> 1.0 <br>
 * <b>last modified date:</b> 2020/11/11 11:28.
 */
public class ItemDecorationVertical extends RecyclerView.ItemDecoration {
    private int mSpanCount;//列数

    /** 水平间距 */
    private int horizontalSpace;
    /** 垂直间距 */
    private int verticalSpace;

    /** 第一行与头部间距 */
    private int firstRowTopSpace;
    /** 最后一行与底部的间距 */
    private int lastRowBottomSpace;

    /** 第一列与左边的距离 */
    private int firstColumnLeftSpace;
    /** 最后一列与右边的距离 */
    private int lastColumnRightSpace;

    /**
     * @param spanCount       列数
     * @param horizontalSpace 水平间距
     * @param verticalSpace   垂直间距
     */
    public ItemDecorationVertical(int spanCount, int horizontalSpace, int verticalSpace) {
        this(spanCount, (0), (0), horizontalSpace, verticalSpace);
    }

    /**
     * @param spanCount          列数
     * @param firstRowTopSpace   第一行与头部间距
     * @param lastRowBottomSpace 最后一行与底部的间距
     * @param horizontalSpace    水平间距
     * @param verticalSpace      垂直间距
     */
    public ItemDecorationVertical(int spanCount, int firstRowTopSpace, int lastRowBottomSpace, int horizontalSpace, int verticalSpace) {
        this(spanCount, firstRowTopSpace, lastRowBottomSpace, (0), (0), horizontalSpace, verticalSpace);
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
    public ItemDecorationVertical(int spanCount, int firstRowTopSpace, int lastRowBottomSpace,
                                  int firstColumnLeftSpace, int lastColumnRightSpace, int horizontalSpace, int verticalSpace) {
        this.mSpanCount = spanCount;
        this.firstRowTopSpace = firstRowTopSpace;
        this.lastRowBottomSpace = lastRowBottomSpace;

        this.firstColumnLeftSpace = firstColumnLeftSpace;
        this.lastColumnRightSpace = lastColumnRightSpace;

        this.horizontalSpace = horizontalSpace;
        this.verticalSpace = verticalSpace;

        if (mSpanCount <= 0) {
            throw new IllegalArgumentException("mSpanCount should be larger than 0");
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        getRectOffsets(outRect, view, parent, state);
    }

    protected Rect getRectOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        // 0 1-2 3 4-5 6
        // 2

        RecyclerView.Adapter<?> adapter = parent.getAdapter();
        if (adapter == null || adapter.getItemCount() == 0) {
            return outRect;
        }

        int screenWidth = DisplayUtils.getWindowWidth(parent.getContext());
        int itemWidth = screenWidth - getFirstColumnLeftSpace() - getLastColumnRightSpace();
        if (getSpanCount() > 1) {
            itemWidth = (itemWidth - (getSpanCount() - 1) * getHorizontalSpace()) / getSpanCount();
        }
        int averageWidth = screenWidth / getSpanCount();

        int position = parent.getChildAdapterPosition(view); // 获取当前view在adapter中的位置。
        int rowIndex = position / mSpanCount; // 当前所在的行
        int columnIndex = position % mSpanCount; // 当前所在的列

        int rowCount = adapter.getItemCount() / mSpanCount;//行数
        if (adapter.getItemCount() % mSpanCount != 0) {
            rowCount += 1;
        }
        int columnCount = mSpanCount;

        //JLog.d("rowIndex:" + rowIndex + ", columnIndex:" + columnIndex + ", rowCount:" + rowCount + ", columnCount:" + columnCount);

        //设置垂直距离，这里涉及到一行和多行的情况
        if (rowCount == 1) {
            //只有一行的情况下
            outRect.top = firstRowTopSpace;
            outRect.bottom = lastRowBottomSpace;
        } else {
            //多行的情况下
            if (rowIndex == 0) {
                //第一行
                outRect.top = firstRowTopSpace;
                outRect.bottom = verticalSpace / 2;

            } else if (rowIndex == (rowCount - 1)) {
                //最后一行
                outRect.top = verticalSpace / 2;
                outRect.bottom = lastRowBottomSpace;

            } else {
                //非第一和最后一行
                outRect.top = verticalSpace / 2;
                outRect.bottom = verticalSpace / 2;
            }
        }

        //设置水平距离，这里涉及到一列和多列的情况
        if (columnCount == 1) {
            //只有一列的情况下
            outRect.left = firstColumnLeftSpace;
            outRect.right = lastColumnRightSpace;
        } else {
            //多列的情况下
            //记录没设置水平间距的时候的左右坐标
            int oldLeft = columnIndex * averageWidth;
            int oldRight = oldLeft + averageWidth;

            //记录设置了水平间距的时候的左右坐标
            int newLeft = firstColumnLeftSpace + columnIndex * (horizontalSpace + itemWidth);
            int newRight = newLeft + itemWidth;

            //计算老坐标和新坐标的偏移
            outRect.left = -(oldLeft - newLeft);
            outRect.right = (oldRight - newRight);
        }

        return outRect;
    }

    public int getHorizontalSpace() {
        return horizontalSpace;
    }

    public void setHorizontalSpace(int horizontalSpace) {
        this.horizontalSpace = horizontalSpace;
    }

    public int getVerticalSpace() {
        return verticalSpace;
    }

    public void setVerticalSpace(int verticalSpace) {
        this.verticalSpace = verticalSpace;
    }

    public int getFirstRowTopSpace() {
        return firstRowTopSpace;
    }

    public void setFirstRowTopSpace(int firstRowTopSpace) {
        this.firstRowTopSpace = firstRowTopSpace;
    }

    public int getLastRowBottomSpace() {
        return lastRowBottomSpace;
    }

    public void setLastRowBottomSpace(int lastRowBottomSpace) {
        this.lastRowBottomSpace = lastRowBottomSpace;
    }

    public int getFirstColumnLeftSpace() {
        return firstColumnLeftSpace;
    }

    public void setFirstColumnLeftSpace(int firstColumnLeftSpace) {
        this.firstColumnLeftSpace = firstColumnLeftSpace;
    }

    public int getLastColumnRightSpace() {
        return lastColumnRightSpace;
    }

    public void setLastColumnRightSpace(int lastColumnRightSpace) {
        this.lastColumnRightSpace = lastColumnRightSpace;
    }

    public int getSpanCount() {
        return mSpanCount;
    }
}
