package com.qk.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 进度条
 */
public class ProcessImageView extends android.support.v7.widget.AppCompatImageView {

    // 画笔
    private Paint mPaint;
    int width = 0;
    int height = 0;
    Context context = null;
    int progress = 0;

    public ProcessImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ProcessImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProcessImageView(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mPaint = new Paint();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 消除锯齿
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        // 半透明
        mPaint.setColor(Color.parseColor("#70000000"));
        canvas.drawRect(0, 0, getWidth(), getHeight()- getHeight() * progress
                / 100, mPaint);
        // 全透明
        mPaint.setColor(Color.parseColor("#00000000"));
        canvas.drawRect(0, getHeight() - getHeight() * progress / 100,
                getWidth(), getHeight(), mPaint);

        mPaint.setTextSize(30);
        mPaint.setColor(Color.parseColor("#FFFFFF"));
        mPaint.setStrokeWidth(2);
        Rect rect = new Rect();
        // 确定文字的宽度
        mPaint.getTextBounds("100%", 0, "100%".length(), rect);
        canvas.drawText(progress + "%", getWidth() / 2 - rect.width() / 2,
                getHeight() / 2, mPaint);

    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

}