package com.test.helpme.test;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 22937 on 2018/4/4.
 */

public class PhotoGridLayoutManger extends RecyclerView.LayoutManager {

    private int padding;
    private int firstItemLength;
    private int itemLength;
    private float scale;

    public float getScale() {
        return scale;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }
        if (getChildCount() == 0 && state.isPreLayout()) {
            return;
        }
        padding = 30;
        itemLength = (getWidth() - 4 * padding) / 3;
        firstItemLength = 2 * itemLength + padding;

        scale = itemLength * 0.800f / firstItemLength;
        detachAndScrapAttachedViews(recycler);

        int offsetX = padding;
        int offsetY = padding;
        int index = 0;

        View first = recycler.getViewForPosition(index++);
        addView(first);
        layoutDecorated(first, offsetX, offsetY, offsetX + firstItemLength, offsetY + firstItemLength);

        if (getItemCount() < index + 1)
            return;
        offsetX += padding + firstItemLength;
        View second = recycler.getViewForPosition(index++);
        addView(second);
        layoutDecorated(second, offsetX, offsetY, offsetX + itemLength, offsetY + itemLength);

        if (getItemCount() < index + 1)
            return;
        offsetY += padding + itemLength;
        View third = recycler.getViewForPosition(index++);
        addView(third);
        layoutDecorated(third, offsetX, offsetY, offsetX + itemLength, offsetY + itemLength);

        int count = getItemCount();
        if (index >= count)
            return;
        int direction = 1;
        for (; index < count; index++) {
            if (index % 3 == 0) {
                if (index / 3 % 2 == 0) {
                    direction = 1;
                    offsetX = padding;
                } else {
                    direction = -1;
                    offsetX = getWidth() - padding - itemLength;
                }
                offsetY += padding + itemLength;
            } else
                offsetX += ((padding + itemLength) * direction);
            View view = recycler.getViewForPosition(index);
            addView(view);
            layoutDecorated(view, offsetX, offsetY, offsetX + itemLength, offsetY + itemLength);
        }
    }


}
