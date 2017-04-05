package me.khrystal.widget.dragsheet;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * usage: 最上层ScrollView
 *  应包含一个{@link OutsideDownFrameLayout}
 * author: kHRYSTAL
 * create time: 17/4/1
 * update time:
 * email: 723526676@qq.com
 */

public class OutSideScrollView extends ScrollView {

    private int mDownY;
    private int mMoveY;
    private OutsideDownFrameLayout mBodyLayout;

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
        if (offset > 0)
            return super.onInterceptTouchEvent(ev);
        if (getScrollY() == 0 && mBodyLayout != null && (mBodyLayout.getCurrentState()) == OutsideDownFrameLayout.DRAG_STATE_SHOW
                || mBodyLayout.getCurrentState() == OutsideDownFrameLayout.DRAG_STATE_MOVE)
            return false;
        return super.onInterceptTouchEvent(ev);
    }

    public void setOutsideLayout(OutsideDownFrameLayout bodyLayout) {
        mBodyLayout = bodyLayout;
    }

}
