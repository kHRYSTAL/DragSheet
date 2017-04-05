package me.khrystal.widget.dragsheet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * usage: 内部头部容器
 * author: kHRYSTAL
 * create time: 17/4/3
 * update time:
 * email: 723526676@qq.com
 */

public class InsideHeaderLayout extends LinearLayout {

    private static final String TAG = InsideHeaderLayout.class.getSimpleName();



    private OutSideScrollView mScrollView;
    private OutsideDownFrameLayout mDownFrameLayout;
    private boolean isInAnim;

    public InsideHeaderLayout(Context context) {
        this(context, null);
    }

    public InsideHeaderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InsideHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public InsideHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击移动至顶部
                moveToTop();
            }
        });
    }

    public void moveToCenter() {
        if (!isInAnim) {
            ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(InsideHeaderLayout.this, "translationY",
                    (Util.getScreenHeight(getContext()) - getMeasuredHeight()) / 2);
            objectAnimator.setDuration(1000);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isInAnim = true;
                    mScrollView.setVisibility(INVISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isInAnim = false;
                }
            });
            objectAnimator.start();
        }
    }

    public void moveToTop() {
        if (!isInAnim) {
            ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(InsideHeaderLayout.this, "translationY", 0);
            objectAnimator.setDuration(1000);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isInAnim = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isInAnim = false;
                    if (mScrollView != null) {
                        mScrollView.setVisibility(VISIBLE);
                        mScrollView.scrollTo(0, 0);
                    }
                    if (mDownFrameLayout != null) {
                        mDownFrameLayout.showWithAnim();
                    }
                }
            });
            objectAnimator.start();
        }
    }

    public void setOutsideLayout(OutsideDownFrameLayout layout) {
        mDownFrameLayout = layout;
    }

    public void setScrollView(OutSideScrollView scrollView) {
        mScrollView = scrollView;
    }

}
