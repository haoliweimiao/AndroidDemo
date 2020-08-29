package com.hlw.demo.view.viewgroup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * @author hlw
 * @date 2020-03-22 16:37:32
 */
public class HorizontalView extends ViewGroup {

    private int mChildWidth = 0;

    private int mLastX, mLastY;
    private int mLastInterceptX = 0, mLastInterceptY = 0;

    private Scroller mScroller;

    /**
     * 当前子元素
     */
    private int mCurrentIndex = 0;

    private VelocityTracker mTracker;

    public HorizontalView(Context context) {
        super(context);
        init(context, null);
    }

    public HorizontalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HorizontalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HorizontalView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mScroller = new Scroller(getContext());
        mTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        if (childCount == 0) {
            //子元素为0，设置宽高为0
            setMeasuredDimension(0, 0);
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            //宽高都是AT_MOST,则宽度为所有子元素之和，高度为第一个子元素高度
            View childView = getChildAt(0);
            int height = childView.getMeasuredHeight();
            int width = childView.getMeasuredWidth();
            setMeasuredDimension(width * childCount, height);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //如果宽度是AT_MOST，则宽度是所有子元素之和
            View childView = getChildAt(0);
            int width = childView.getMeasuredWidth();
            setMeasuredDimension(width * childCount, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //如果高度是AT_MOST，则高度是第一个子元素的高度
            View childView = getChildAt(0);
            int height = childView.getMeasuredHeight();
            setMeasuredDimension(widthSize, height);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left = 0;
        View childView;
        for (int i = 0; i < childCount; i++) {
            childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                int width = childView.getMeasuredWidth();
                mChildWidth = width;
                childView.layout(left, 0, left + width, childView.getMeasuredHeight());
                left += width;
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastInterceptX;
                int deltaY = y - mLastInterceptY;
                intercept = Math.abs(deltaX) - Math.abs(deltaY) > 0;
                break;
            default:
                break;
        }

        mLastX = x;
        mLastY = y;
        mLastInterceptX = x;
        mLastInterceptY = y;
        return intercept;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mTracker.addMovement(ev);
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                scrollBy(-deltaX, 0);
                break;
            case MotionEvent.ACTION_UP:
                int distance = getScrollX() - mCurrentIndex * mChildWidth;
                if (Math.abs(distance) > mChildWidth / 2) {
                    if (distance > 0) {
                        mCurrentIndex++;
                    } else {
                        mCurrentIndex--;
                    }
                } else {
                    mTracker.computeCurrentVelocity(1000);
                    float xv = mTracker.getXVelocity();
                    if (Math.abs(xv) > 50) {
                        if (xv > 0) {
                            //切换到上一个界面
                            mCurrentIndex--;
                        } else {
                            //切换到下一个界面
                            mCurrentIndex++;
                        }
                    }
                }
                mCurrentIndex = mCurrentIndex < 0 ? 0 : mCurrentIndex > getChildCount() - 1 ? getChildCount() - 1 : mCurrentIndex;
                smoothScrollTo(mCurrentIndex * mChildWidth, 0);
                mTracker.clear();
                break;
            default:
                break;
        }

        mLastX = x;
        mLastY = y;
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    private void smoothScrollTo(int destX, int destY) {
        mScroller.startScroll(getScrollX(), getScrollY(), destX - getScrollX(), destY - getScrollY(), 1000);
        invalidate();
    }
}
