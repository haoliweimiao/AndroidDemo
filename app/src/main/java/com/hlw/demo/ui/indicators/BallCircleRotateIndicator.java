package com.hlw.demo.ui.indicators;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.hlw.demo.ui.Indicator;

import java.util.ArrayList;

public class BallCircleRotateIndicator extends Indicator {


    private int mAlpha;

    private int mShowAnimationTime = 3600;

    private int mStartAngle = 135;
    private int mSweepAngle = 0;

    @Override
    public void draw(Canvas canvas, Paint paint) {
        int width = getWidth();
        int height = getHeight();

        paint.setAlpha(mAlpha);
        canvas.drawCircle(width / 2, height / 2, width / 10, paint);
        //restore alpha
        paint.setAlpha(255);

        RectF rect = new RectF();
        rect.left = 0;
        rect.top = 0;
        rect.right = width;
        rect.bottom = height;
        canvas.drawArc(rect, mStartAngle, mSweepAngle, true, paint);
    }

    @Override
    public ArrayList<ValueAnimator> onCreateAnimators() {

        ArrayList<ValueAnimator> valueAnimators = new ArrayList<>();

        ValueAnimator transAnim = ValueAnimator.ofInt(255, 0, 233);
        transAnim.setDuration(mShowAnimationTime);
        transAnim.setRepeatCount(ValueAnimator.INFINITE);
        addUpdateListener(transAnim, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAlpha = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        ValueAnimator sweepAnim = ValueAnimator.ofInt(0, 360);
        sweepAnim.setDuration(mShowAnimationTime);
        sweepAnim.setRepeatCount(-1);
        addUpdateListener(sweepAnim, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSweepAngle = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        ValueAnimator startAnim = ValueAnimator.ofInt(135, 360 + 135);
        startAnim.setDuration(mShowAnimationTime);
        startAnim.setRepeatCount(-1);
        addUpdateListener(startAnim, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStartAngle = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });


        valueAnimators.add(transAnim);
        valueAnimators.add(sweepAnim);
        valueAnimators.add(startAnim);
        return valueAnimators;
    }
}
