package com.test.helpme.test;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 22937 on 2018/4/4.
 */

public class DragCallBack extends ItemTouchHelper.Callback {

    private View mDragView;
    private ItemMoveListener mItemMoveListener;
    private float eventX, eventY, dx, dy;
    private float scale;
    private AnimatorSet animatorSet;

    public void setScale(float scale) {
        this.scale = scale;
    }


    public void setEvent(MotionEvent event) {
        eventX = event.getX();
        eventY = event.getY();
    }


    public DragCallBack(ItemMoveListener mItemMoveListener) {
        this.mItemMoveListener = mItemMoveListener;
        animatorSet = new AnimatorSet();
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = 0;
        //最终的动作标识（flags）必须要用makeMovementFlags()方法生成
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
        int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
        return mItemMoveListener.onItemMove(fromPosition, toPosition);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }


    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        if (toPos == 0) {
            animatorSet.cancel();
            viewHolder.itemView.setScaleX(scale);
            viewHolder.itemView.setScaleY(scale);
        }
        if (fromPos == 0) {
            animatorSet.cancel();
            viewHolder.itemView.setScaleX(0.8f);
            viewHolder.itemView.setScaleY(0.8f);
        }
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
    }


    @Override
    public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        ValueAnimator anim = ValueAnimator.ofFloat(1.0f, 0.0f);
        final float baseX = mDragView.getTranslationX();
        final float baseY = mDragView.getTranslationY();
        anim.setDuration(DEFAULT_DRAG_ANIMATION_DURATION);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = baseX * (float) animation.getAnimatedValue();
                dy = baseY * (float) animation.getAnimatedValue();
            }
        });
        anim.start();
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (!isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder,  dx, dy, actionState, isCurrentlyActive);
        } else {
            dX = eventX - (viewHolder.itemView.getLeft() + viewHolder.itemView.getWidth() / 2);
            dY = eventY - (viewHolder.itemView.getTop() + viewHolder.itemView.getHeight() / 2);
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (!isCurrentlyActive) {
            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        } else {
            dX = eventX - (viewHolder.itemView.getLeft() + viewHolder.itemView.getWidth() / 2);
            dY = eventY - (viewHolder.itemView.getTop() + viewHolder.itemView.getHeight() / 2);
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        float mScale;
        if (viewHolder != null)
            mScale = viewHolder.getLayoutPosition() == 0 ? scale : 0.8f;
        else
            mScale = 0.8f;
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(viewHolder.itemView, "scaleX", 1.0f, mScale);
            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(viewHolder.itemView, "scaleY", 1.0f, mScale);
            animatorSet = new AnimatorSet();
            animatorSet.setDuration(200);
            animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
            animatorSet.start();
            mDragView = viewHolder.itemView;
        }
        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(mDragView, "scaleX", mScale, 1.0f);
            ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(mDragView, "scaleY", mScale, 1.0f);
            animatorSet = new AnimatorSet();
            animatorSet.setDuration(250);
            animatorSet.playTogether(objectAnimatorX, objectAnimatorY);
            animatorSet.start();
        }
    }

}
