package com.zxn.camerabutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;

/**
 * Updated by zxn on 2020/2/12.
 */
public class CameraButton extends View {

    /**
     * The CameraButton shape(default).
     */
    public static final int INNER_TYPE_CIRCLE = 0;

    /**
     * 内形状为矩形.
     */
    public static final int INNER_TYPE_RECT = 1;

    private float circleWidth;//外圆环宽度
    private int outCircleColor;//外圆颜色
    private int innerCircleColor;//内圆颜色
    private int progressColor;//进度条颜色

    private Paint outRoundPaint = new Paint(); //外圆画笔
    private Paint mCPaint = new Paint();//进度画笔
    private Paint innerRoundPaint = new Paint();
    private float width; //自定义view的宽度
    private float height; //自定义view的高度
    private float outRaduis; //外圆半径
    private float innerRaduis;//内圆半径
    private GestureDetectorCompat mDetector;//手势识别
    private boolean isLongClick;//是否长按
    private float startAngle = -90;//开始角度
    private float mmSweepAngleStart = 0f;//起点
    private float mmSweepAngleEnd = 360f;//终点
    private float mSweepAngle;//扫过的角度
    private int mLoadingTime;
    private int mInnerType;
    private boolean mScaleEnabled;


    public CameraButton(Context context) {
        this(context, null);
    }

    public CameraButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public CameraButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CameraButton);
        outCircleColor = array.getColor(R.styleable.CameraButton_outCircleColor, Color.parseColor("#E0E0E0"));
        innerCircleColor = array.getColor(R.styleable.CameraButton_innerCircleColor, Color.WHITE);
        progressColor = array.getColor(R.styleable.CameraButton_readColor, Color.GREEN);
        mLoadingTime = array.getInteger(R.styleable.CameraButton_maxSeconds, 10);
        mInnerType = array.getInt(R.styleable.CameraButton_innerType, INNER_TYPE_CIRCLE);
        mScaleEnabled = array.getBoolean(R.styleable.CameraButton_scaleEnabled, true);
        mDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                //单击
                isLongClick = false;
                if (listener != null) {
                    listener.onClick(CameraButton.this);
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                //长按
                isLongClick = true;
                postInvalidate();
                if (listener != null) {
                    listener.onLongClick(CameraButton.this);
                }
            }
        });
        mDetector.setIsLongpressEnabled(true);
    }

    private void resetParams() {
        width = getWidth();
        height = getHeight();
        //circleWidth = width * 0.13f;
        circleWidth = width * 0.10f;
        //outRaduis = (float) (Math.min(width, height) / 2.4);
        outRaduis = (float) (Math.min(width, height) / 3.0);
        innerRaduis = outRaduis - circleWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (width > height) {
            setMeasuredDimension(height, height);
        } else {
            setMeasuredDimension(width, width);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        resetParams();

        //画外圆
        outRoundPaint.setAntiAlias(true);
        outRoundPaint.setColor(outCircleColor);

        if (isLongClick && mScaleEnabled) {
            setMinimumWidth((int) (width * 1.2));
            canvas.scale(1.2f, 1.2f, width / 2, width / 2);
        } else {
            setMinimumWidth((int) width);
        }
        canvas.drawCircle(width / 2, height / 2, outRaduis, outRoundPaint);
        //画内圆
        innerRoundPaint.setAntiAlias(true);
        innerRoundPaint.setColor(innerCircleColor);

        if (isLongClick) {
            //画外原环
            mCPaint.setAntiAlias(true);
            mCPaint.setColor(progressColor);
            mCPaint.setStyle(Paint.Style.STROKE);
            //mCPaint.setStrokeWidth(circleWidth / 2);
            mCPaint.setStrokeWidth(circleWidth / 4);
            float circlePadding = 0;
            float left = 0 + circleWidth - circlePadding;
            float top = 0 + circleWidth - circlePadding;
            float right = width - circleWidth + circlePadding;
            float bottom = height - circleWidth + circlePadding;
            RectF rectF = new RectF(left, top, right, bottom);
            canvas.drawArc(rectF, startAngle, mSweepAngle, false, mCPaint);
        }

        //画内部形状
        drawInnerShape(canvas);
    }

    private void drawInnerShape(Canvas canvas) {
        if (mInnerType == INNER_TYPE_RECT) {
            if (isLongClick && mScaleEnabled) {
                float longClickInnerRaduis = innerRaduis / 1.5f;
                float innerLeft = (width - longClickInnerRaduis) / 2;
                float innerTop = (height - longClickInnerRaduis) / 2;
                float innerRight = width - (width - longClickInnerRaduis) / 2;
                float innerBottom = height - (height - longClickInnerRaduis) / 2;
                canvas.drawRect(innerLeft, innerTop, innerRight, innerBottom, innerRoundPaint);
            } else {
                float innerLeft = (width - innerRaduis) / 2;
                float innerTop = (height - innerRaduis) / 2;
                float innerRight = width - (width - innerRaduis) / 2;
                float innerBottom = height - (height - innerRaduis) / 2;
                canvas.drawRect(innerLeft, innerTop, innerRight, innerBottom, innerRoundPaint);
            }
        } else {
            if (isLongClick && mScaleEnabled) {
                canvas.drawCircle(width / 2, height / 2, innerRaduis / 1.5f, innerRoundPaint);
            } else {
                canvas.drawCircle(width / 2, height / 2, innerRaduis, innerRoundPaint);
            }
        }
    }

    public void start() {
        ValueAnimator animator = ValueAnimator.ofFloat(mmSweepAngleStart, mmSweepAngleEnd);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSweepAngle = (float) valueAnimator.getAnimatedValue();
                //获取到需要绘制的角度，重新绘制
                invalidate();
            }
        });
        //这里是时间获取和赋值
        ValueAnimator animator1 = ValueAnimator.ofInt(mLoadingTime, 0);
        animator1.setInterpolator(new LinearInterpolator());
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int time = (int) valueAnimator.getAnimatedValue();
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator, animator1);
        set.setDuration(mLoadingTime * 1000);
        set.setInterpolator(new LinearInterpolator());
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clearAnimation();
                isLongClick = false;
                postInvalidate();
                if (listener != null) {
                    listener.onFinish();
                }
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
                isLongClick = false;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isLongClick) {
                    isLongClick = false;
                    postInvalidate();
                    if (this.listener != null) {
                        this.listener.onLongClickUp(this);
                    }
                }
                break;
        }
        return true;
    }

    private OnProgressTouchListener listener;

    public void setOnProgressTouchListener(OnProgressTouchListener listener) {
        this.listener = listener;
    }

    /**
     * 进度触摸监听
     */
    public interface OnProgressTouchListener {
        /**
         * 单击
         *
         * @param photoButton
         */
        void onClick(CameraButton photoButton);

        /**
         * 长按
         *
         * @param photoButton
         */
        void onLongClick(CameraButton photoButton);

        /**
         * 长按抬起
         *
         * @param photoButton
         */
        void onLongClickUp(CameraButton photoButton);


        void onFinish();
    }

}
