package com.github.tianma8023.ssv;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.github.tianma8023.formatter.SimpleSunriseSunsetLabelFormatter;
import com.github.tianma8023.formatter.SunriseSunsetLabelFormatter;
import com.github.tianma8023.model.Time;

import java.util.Calendar;
import java.util.Locale;

/**
 * A view which can show sunrise and sunset animation
 */
public class SunriseSunsetView extends View {

    private static final int DEFAULT_SUN_RADIUS_PX = 20;
    private static final int DEFAULT_LABEL_VERTICAL_GAP_PX = 10;
    private static final int DEFAULT_LABEL_HORIZONTAL_GAP_PX = 20;

    /**
     * 当前日出日落比率, mRatio < 0: 未日出, mRatio > 1 已日落
     */
    private float mRatio;
    /**
     * 半圆的半径
     */
    private float mRadius;
    /**
     * 太阳的半径
     */
    private float mSunRadius = DEFAULT_SUN_RADIUS_PX;

    private Paint mCirclePaint; // 绘制圆的Paint
    private Paint mShadowPaint; // 绘制日出日落阴影的Paint
    private Paint mSunPaint;    // 绘制太阳的Paint
    private TextPaint mTextPaint;   // 绘制日出日落时间的Paint

    /**
     * 日出时间
     */
    private Time mSunriseTime;
    /**
     * 日落时间
     */
    private Time mSunsetTime;

    /**
     * 绘图区域
     */
    private RectF mBoardRectF = new RectF();

    private SunriseSunsetLabelFormatter mLabelConverter = new SimpleSunriseSunsetLabelFormatter();

    public SunriseSunsetView(Context context) {
        super(context);
        init();
    }

    public SunriseSunsetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SunriseSunsetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int paddingRight = getPaddingRight();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        mRadius = 1.0f * (measuredWidth - paddingLeft - paddingRight) / 2;
        int expectedHeight = (int) (mRadius + paddingBottom + paddingTop);
        mBoardRectF.set(paddingLeft, paddingTop, measuredWidth - paddingRight, expectedHeight - paddingBottom);
        setMeasuredDimension(measuredWidth, expectedHeight);
    }

    private void init() {
        // 初始化半圆的画笔
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.WHITE); // 圆周的颜色为白色
        mCirclePaint.setStyle(Paint.Style.STROKE); // 画笔的样式为线条
        mCirclePaint.setStrokeWidth(4); // 边框宽度
        PathEffect effect = new DashPathEffect(new float[]{15, 15}, 1); // 虚线
        mCirclePaint.setPathEffect(effect);

        // 初始化日出日落阴影的画笔
        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setColor(Color.parseColor("#32FFFFFF"));
        mShadowPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        // 初始化太阳的Paint
        mSunPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSunPaint.setColor(Color.WHITE);
        mSunPaint.setStyle(Paint.Style.STROKE);
        mSunPaint.setStrokeWidth(4);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(40);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawSunTrack(canvas);
        drawShadow(canvas);
        drawSun(canvas);
        drawSunriseSunsetLabel(canvas);
    }

    // 绘制太阳轨道（半圆）
    private void drawSunTrack(Canvas canvas) {
        canvas.save();
        RectF rectF = new RectF(mBoardRectF.left, mBoardRectF.top, mBoardRectF.right, mBoardRectF.bottom + mBoardRectF.height());
        canvas.drawArc(rectF, 180, 180, false, mCirclePaint);
        canvas.restore();
    }

    // 绘制日出日落阴影部分
    private void drawShadow(Canvas canvas) {
        canvas.save();
        Path path = new Path();
        float endY = mBoardRectF.bottom;
        RectF rectF = new RectF(mBoardRectF.left, mBoardRectF.top, mBoardRectF.right, mBoardRectF.bottom + mBoardRectF.height());
        float curPointX = mBoardRectF.left + mRadius - mRadius * (float) Math.cos(Math.PI * mRatio);

        path.moveTo(0, endY);
        path.arcTo(rectF, 180, 180 * mRatio);
        path.lineTo(curPointX, endY);
        path.close();
        canvas.drawPath(path, mShadowPaint);
        canvas.restore();
    }

    // 绘制太阳
    private void drawSun(Canvas canvas) {
        canvas.save();

        float curPointX = mBoardRectF.left + mRadius - mRadius * (float) Math.cos(Math.PI * mRatio);
        float curPointY = mBoardRectF.bottom - mRadius * (float) Math.sin(Math.PI * mRatio);
        canvas.drawCircle(curPointX, curPointY, mSunRadius, mSunPaint);

        canvas.restore();
    }

    // 绘制日出日落标签
    private void drawSunriseSunsetLabel(Canvas canvas) {
        if (mSunriseTime == null || mSunsetTime == null) {
            return;
        }
        canvas.save();
        // 绘制日出时间
        String sunriseStr = mLabelConverter.formatSunriseLabel(mSunriseTime);

        mTextPaint.setTextAlign(Paint.Align.LEFT);
        Paint.FontMetricsInt metricsInt = mTextPaint.getFontMetricsInt();
        float baseLineX = mBoardRectF.left + DEFAULT_LABEL_HORIZONTAL_GAP_PX;
        float baseLineY = mBoardRectF.bottom - metricsInt.bottom - DEFAULT_LABEL_VERTICAL_GAP_PX;
        canvas.drawText(sunriseStr, baseLineX, baseLineY, mTextPaint);

        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        String sunsetStr = mLabelConverter.formatSunsetLabel(mSunsetTime);
        baseLineX = mBoardRectF.right - DEFAULT_LABEL_HORIZONTAL_GAP_PX;
        canvas.drawText(sunsetStr, baseLineX, baseLineY, mTextPaint);
        canvas.restore();
    }

    public void setRatio(float ratio) {
        mRatio = ratio;
        invalidate();
    }

    public void setSunriseTime(Time sunriseTime) {
        mSunriseTime = sunriseTime;
    }

    public Time getSunriseTime() {
        return mSunriseTime;
    }

    public void setSunsetTime(Time sunsetTime) {
        mSunsetTime = sunsetTime;
    }

    public Time getSunsetTime() {
        return mSunsetTime;
    }

    public float getSunRadius() {
        return mSunRadius;
    }

    public void setSunRadius(float sunRadius) {
        mSunRadius = sunRadius;
    }

    public SunriseSunsetLabelFormatter getLabelConverter() {
        return mLabelConverter;
    }

    public void setLabelConverter(SunriseSunsetLabelFormatter labelConverter) {
        mLabelConverter = labelConverter;
    }

    public void startAnimate() {
        if (mSunriseTime == null || mSunsetTime == null) {
            throw new RuntimeException("You need to set both sunrise and sunset time before start animation");
        }
        int sunrise = mSunriseTime.transformToMinutes();
        int sunset = mSunsetTime.transformToMinutes();
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        int currentTime = currentHour * Time.MINUTES_PER_HOUR + currentMinute;
        float ratio = 1.0f * (currentTime - sunrise) / (sunset - sunrise);
        ratio = ratio <= 0 ? 0 : (ratio > 1.0f ? 1 : ratio);
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "ratio", 0f, ratio);
        animator.setDuration(1500L);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

}
