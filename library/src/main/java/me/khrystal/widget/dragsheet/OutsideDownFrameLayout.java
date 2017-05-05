package me.khrystal.widget.dragsheet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;


import java.util.ArrayList;

/**
 * author: kHRYSTAL
 * create time: 17/4/1
 * update time:
 * email: 723526676@qq.com
 */

public class OutsideDownFrameLayout extends FrameLayout {

    private static final String TAG = OutsideDownFrameLayout.class.getSimpleName();

    private Scroller mScroller;
    private InsideHeaderLayout mInsideLayout;
    private ArrayList<View> animViews = new ArrayList<>();

    private float downY;
    private float mLastY;
    private int mMoveY;
    private static int DRAG_Y_MAX = 220;

    public static final int DRAG_STATE_SHOW = 1;
    public static final int DRAG_STATE_HIDE = 2;
    public static final int DRAG_STATE_MOVE = 3;

    public int mCurrentState = DRAG_STATE_SHOW;
    // 设置scrollview在顶部时 是否支持下拉
    private boolean enableDragDown = true;

    public OutsideDownFrameLayout(Context context) {
        this(context, null);
    }

    public OutsideDownFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OutsideDownFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OutsideDownFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
    }

    public void setCurrentState(int currentState) {
        mCurrentState = currentState;
    }

    public int getCurrentState() {
        return mCurrentState;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downY = ev.getY();
        }

        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (getScrollY() <= 0 && ev.getY() - downY > DisplayUtil.dp2px(getContext(), 15)) {
                mLastY = ev.getY();
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mCurrentState == DRAG_STATE_HIDE || !enableDragDown)
            return false;
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) (mLastY - y);
                if (getScrollY() <= 0) {
                    mCurrentState = DRAG_STATE_MOVE;
                    this.scrollBy(0, moveY / 2);
                }
                mLastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                process();
                break;
        }
        return true;
    }

    /**
     * 通过Scroller执行拖拽松手后的事件
     */
    protected void process() {
        if (-getScrollY() > DRAG_Y_MAX) {
            // 执行隐藏
            mCurrentState = DRAG_STATE_HIDE;
            // 获取该容器送手后在屏幕仍然可见的高度
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            int visiableHeight = rect.height() == 0 ? DisplayUtil.getHeight(getContext()) : rect.height();
            mMoveY = Math.abs(-visiableHeight - getScrollY());

            if (mInsideLayout != null) {
                // header 滑动到中央
                mInsideLayout.moveToCenter();
            }

            if (!animViews.isEmpty()) {
                // 隐藏底部操作栏
//                bottomLayout.setVisibility(GONE);
                for (View view : animViews) {
                    hideAlphaAnim(view);
                }


            }
            // 向下滑动隐藏可见的高度
            mScroller.startScroll(0, getScrollY(), 0, -mMoveY, 1000);
        } else {
            // 还原位置
            mCurrentState = DRAG_STATE_SHOW;
            mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), (int) (Math.abs(getScaleY()) / 2));
        }
        invalidate();
    }

    /**
     * 收起动画
     */
    public void showWithAnim() {
        mCurrentState = DRAG_STATE_SHOW;
        mScroller.startScroll(0, -mMoveY, 0, mMoveY, 1000);
        invalidate();
        if (!animViews.isEmpty()) {
            for (View view : animViews) {
                showAlphaAnim(view);
            }
        }
    }

    public void setInsideLayout(InsideHeaderLayout layout) {
        mInsideLayout = layout;
    }

    public void setAnimViews(View... views) {
        animViews.clear();
        for (int i = 0; i < views.length; i++) {
            animViews.add(views[i]);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void setEnableDragDown(boolean enableDragDown) {
        this.enableDragDown = enableDragDown;
    }

    private void showAlphaAnim(final View view) {

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha",
                0f, 1f);
        objectAnimator.setDuration(1000);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });
        objectAnimator.start();
    }

    private void hideAlphaAnim(final View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha",
                1f, 0f);
        objectAnimator.setDuration(500);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(GONE);
            }
        });
        objectAnimator.start();
    }
}
