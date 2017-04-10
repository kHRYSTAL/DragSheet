package me.khrystal.widget.dragsheet;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Scroller;
import android.widget.Space;

/**
 * usage: 外层ScrollView中的下方布局 用于内容显示
 * author: kHRYSTAL
 * create time: 17/4/1
 * update time:
 * email: 723526676@qq.com
 */

public class OutsideDownFrameLayout extends FrameLayout {

    private static final String TAG = OutsideDownFrameLayout.class.getSimpleName();

    private Scroller mScroller;
    private InsideHeaderLayout mInsideLayout;
    private Space mSpace;

    private float mLastY;
    private int mMoveY;
    private static int DRAG_Y_MAX = 220;
    private float downY;

    public static final int DRAG_STATE_SHOW = 1;
    public static final int DRAG_STATE_HIDE = 2;
    public static final int DRAG_STATE_MOVE = 3;

    public int mCurrentState = DRAG_STATE_SHOW;

    public OutsideDownFrameLayout(Context context) {
        this(context,null);
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
        if (mCurrentState == DRAG_STATE_HIDE)
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
            if (mSpace != null) {
                mSpace.setVisibility(INVISIBLE);
            }
            // 获取该容器送手后在屏幕仍然可见的高度
            mMoveY = Math.abs(-getMeasuredHeight() - getScrollY());
            // 向下滑动隐藏可见的高度
            mScroller.startScroll(0, getScrollY(), 0, -mMoveY, 1000);
        } else {
            // 还原位置
            if (mSpace != null) {
                mSpace.setVisibility(VISIBLE);
            }
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
        mSpace.setVisibility(VISIBLE);
        mScroller.startScroll(0, -mMoveY, 0, mMoveY, 1000);
        invalidate();
    }

    public void setInsideLayout(InsideHeaderLayout layout) {
        mInsideLayout = layout;
    }

    public void setSpace(Space space) {
        mSpace = space;
        if (mSpace != null) {
            mSpace.setVisibility(VISIBLE);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }

        if (mScroller.isFinished() && mCurrentState == DRAG_STATE_HIDE) {
            if (mInsideLayout != null) {
                mInsideLayout.moveToCenter();
            }
        }
    }
}
