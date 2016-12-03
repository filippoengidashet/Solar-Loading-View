package org.dalol.solarloadingview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;


/**
 * @author Filippo Engidashet <filippo.eng@gmail.com>
 * @version 1.0.0
 * @since 12/3/2016
 */
public class SolarLoadingView extends View {

    private static final String TAG = SolarLoadingView.class.getSimpleName();
    private Paint paint;
    private int angle, angle2, angle3;
    private double oldY, oldX;
    private AnimatorSet set;
    private boolean animating;

    public SolarLoadingView(Context context) {
        super(context);
        init(context, null);
    }

    public SolarLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SolarLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            return;
        }

        paint.setColor(Color.RED);

        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 4, paint);

        int i = ((getWidth() / 2) - 90);

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (i / 3) * 3, paint);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (i / 3), paint);

        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, 90, paint);

        paint.setColor(Color.BLUE);

//        X := originX + cos(angle)*radius;
//        Y := originY + sin(angle)*radius;

        float originX = getWidth() / 2;
        float originY = getHeight() / 2;

        float radius = (i / 3) * 3;
        float radius2 = (i / 3);
        float radius3 = getWidth() / 4;

        oldX = originX + radius * Math.sin(Math.toRadians(angle));
        oldY = originY + radius * Math.cos(Math.toRadians(angle));

        Log.d(TAG, "Current Angle -> oldX :: " + oldX + " -- y :: " + oldY + " angle ---> " + angle);
        //float y = getHeight() /2;

        canvas.drawCircle((float) oldX, (float) oldY, 30, paint);

        double oldX1 = originX + radius2 * Math.sin(Math.toRadians(angle2));
        double oldY1 = originY + radius2 * Math.cos(Math.toRadians(angle2));
        canvas.drawCircle((float) oldX1, (float) oldY1, 30, paint);

        double oldX2 = originX + radius3 * Math.sin(Math.toRadians(angle3));
        double oldY2 = originY + radius3 * Math.cos(Math.toRadians(angle3));
        canvas.drawCircle((float) oldX2, (float) oldY2, 30, paint);
    }

    public boolean animateNow() {

        if (animating && set != null) {
            set.cancel();
            animating = false;

            reset();

            return animating;
        }

        //animator.setInterpolator(new LinearInterpolator());

        //ObjectAnimator[] objectAnimators = new ObjectAnimator[3];

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(this, "angle", 360, 0);
        animator1.setDuration(3000);
        animator1.setInterpolator(new LinearInterpolator());
        animator1.setRepeatCount(Animation.INFINITE);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(this, "angle2", 360, 0);
        animator2.setDuration(1000);
        animator2.setInterpolator(new LinearInterpolator());
        animator2.setRepeatCount(Animation.INFINITE);

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(this, "angle3", 360, 0);
        animator3.setDuration(2000);
        animator3.setInterpolator(new LinearInterpolator());
        animator3.setRepeatCount(Animation.INFINITE);

        ObjectAnimator[] objectAnimators = {animator1, animator2, animator3};

        set = new AnimatorSet();
        set.playTogether(objectAnimators);

        set.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                animating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                reset();
            }
        });
        set.start();

        //new Counter(0, 360, 5000).start();
        return animating;
    }

    private void reset() {
        angle = 0;
        angle2 = 0;
        angle3 = 0;
        invalidate();
    }

    private void setAngle(float angle) {
        this.angle = (int) angle;
        Log.d(TAG, "Current Angle -> " + this.angle);
        invalidate();
    }

    public void setAngle2(float angle2) {
        this.angle2 = (int) angle2;
        invalidate();
    }

    public void setAngle3(float angle3) {
        this.angle3 = (int) angle3;
        invalidate();
    }

    public static class ConstantInterpolator implements TimeInterpolator {

        private int counter;

        @Override
        public float getInterpolation(float input) {
            counter++;
            Log.d(TAG, counter + "Current Angle -> INTERPOLATION TIME ConstantInterpolator ---> " + input);
            return input;
        }
    }


    public static class Counter extends Thread {

        private int initialAngle;
        private int endAngle;
        private long duration;
        private int counter;

        public Counter(int initialAngle, int endAngle, long duration) {
            this.initialAngle = initialAngle;
            this.endAngle = endAngle;
            this.duration = duration;
        }

        @Override
        public void run() {
            int difference = endAngle - initialAngle;
            long timeToWait = duration / difference;


            double a = 0.3 - 0.1;
            System.out.println(a);

            Log.d(TAG, counter + "Current Angle -> INTERPOLATION TIME Counter ---> " + a);

            for (int i = initialAngle; i <= endAngle; i++) {
                counter++;
                try {
                    double input = ((i * 100) / difference);
                    Log.d(TAG, counter + "Current Angle -> INTERPOLATION TIME Counter ---> " + input / 100);
                    Thread.sleep(timeToWait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
