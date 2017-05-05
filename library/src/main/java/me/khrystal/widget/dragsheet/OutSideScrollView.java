package me.khrystal.widget.dragsheet;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * 最上层ScrollView
 * 应包含一个{@link OutsideDownFrameLayout}
 * author: kHRYSTAL
 * create time: 17/4/1
 * update time:
 * email: 723526676@qq.com
 */

public class OutSideScrollView extends ScrollView {
    private static final String TAG = OutSideScrollView.class.getSimpleName();

    private int mDownY;
    private int mMoveY;
    private OutsideDownFrameLayout mBodyLayout;
    // 滑动事件监听器
    private List<OnScrollChangedListener> listeners = new ArrayList<OnScrollChangedListener>();
    private OnSizeChangedListener mChangedListener;
    private boolean mShowKeyboard = false;

    public OutSideScrollView(Context context) {
        this(context, null);
    }

    public OutSideScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OutSideScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OutSideScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mDownY = 0;
                mMoveY = 0;
                break;
        }
        int offset = mDownY - mMoveY;
        //scroll up
        if (offset > 0) {
            return super.onInterceptTouchEvent(ev);
        }
        if (getScrollY() == 0 && mBodyLayout != null && (mBodyLayout.getCurrentState() == OutsideDownFrameLayout.DRAG_STATE_SHOW
                || mBodyLayout.getCurrentState() == OutsideDownFrameLayout.DRAG_STATE_MOVE))
            return false;
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //body布局隐藏不处理
        if (mBodyLayout.getCurrentState() == OutsideDownFrameLayout.DRAG_STATE_HIDE) {
            return false;
        }

        return super.onTouchEvent(ev);
    }

    public void setOutsideLayout(OutsideDownFrameLayout bodyLayout) {
        mBodyLayout = bodyLayout;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (null != mChangedListener && 0 != oldw && 0 != oldh) {
            if (oldh - h > 300) {
                mShowKeyboard = true;
                mChangedListener.onChanged(mShowKeyboard);
            } else if (h - oldh > 300) {
                mShowKeyboard = false;
                mChangedListener.onChanged(mShowKeyboard);
            }
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (!listeners.isEmpty()) {
            for (OnScrollChangedListener listener : listeners)
                listener.onScrollChanged(l, t, oldl, oldt);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void setOnSizeChangedListener(OnSizeChangedListener listener) {
        mChangedListener = listener;
    }

    /**
     * 增加监听器
     *
     * @param onScrollChangedListener
     */
    public void addScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        listeners.add(onScrollChangedListener);
    }

    /**
     * 移除不用的滑动监听器
     *
     * @param onScrollChangedListener
     */
    public void removeScrollListener(
            OnScrollChangedListener onScrollChangedListener) {
        if (listeners.contains(onScrollChangedListener)) {
            listeners.remove(onScrollChangedListener);
        }
    }


    public interface OnSizeChangedListener {
        void onChanged(boolean showKeyboard);
    }

    /**
     * 移动滑动监听器
     *
     * @param listener
     */
    public void removeOnScrollChangedListener(OnScrollChangedListener listener) {
        removeScrollListener(listener);
    }
}
