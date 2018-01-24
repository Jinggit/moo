package com.moocall.moocall.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import com.moocall.moocall.C0530R;
import java.util.ArrayList;

public class DonutChart extends View {
    private static final int DEGREE_360 = 360;
    public static final String ERROR_NOT_EQUAL_TO_100 = "NOT_EQUAL_TO_100";
    private static String[] PIE_COLORS = null;
    private static final String TAG = DonutChart.class.getName();
    private static int iColorListSize = 0;
    private ArrayList<Double> alPercentage = new ArrayList();
    private double fEndAngle = 0.0d;
    private double fStartAngle = 0.0d;
    private int iCenterWidth = 0;
    private int iDataSize = 0;
    private int iSelectedIndex = -1;
    RectF innerCircle;
    Path myPath;
    RectF outterCircle;
    Paint paint;
    private float radius;

    public DonutChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, C0530R.styleable.DonutChart, 0, 0);
        try {
            this.radius = a.getDimension(0, 20.0f);
            PIE_COLORS = getResources().getStringArray(C0530R.array.colors);
            iColorListSize = PIE_COLORS.length;
            this.paint = new Paint();
            this.paint.setDither(true);
            this.paint.setStyle(Style.FILL);
            this.paint.setStrokeJoin(Join.ROUND);
            this.paint.setStrokeCap(Cap.ROUND);
            this.paint.setAntiAlias(true);
            this.paint.setStrokeWidth(this.radius / 14.0f);
            this.myPath = new Path();
            this.outterCircle = new RectF();
            this.innerCircle = new RectF();
            float adjust = 0.038f * this.radius;
            this.outterCircle.set(adjust, adjust, (this.radius * 2.0f) - adjust, (this.radius * 2.0f) - adjust);
            adjust = 0.376f * this.radius;
            this.innerCircle.set(adjust, adjust, (this.radius * 2.0f) - adjust, (this.radius * 2.0f) - adjust);
        } finally {
            a.recycle();
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < this.iDataSize; i++) {
            if (i >= iColorListSize) {
                this.paint.setColor(Color.parseColor(PIE_COLORS[i % iColorListSize]));
            } else {
                this.paint.setColor(Color.parseColor(PIE_COLORS[i]));
            }
            this.fEndAngle = ((Double) this.alPercentage.get(i)).doubleValue();
            this.fEndAngle = (this.fEndAngle / 100.0d) * 360.0d;
            drawDonut(canvas, this.paint, (float) this.fStartAngle, (float) this.fEndAngle);
            this.fStartAngle += this.fEndAngle;
        }
    }

    public void drawDonut(Canvas canvas, Paint paint, float start, float sweep) {
        this.myPath.reset();
        this.myPath.arcTo(this.outterCircle, start, sweep, false);
        this.myPath.arcTo(this.innerCircle, start + sweep, -sweep, false);
        this.myPath.close();
        canvas.drawPath(this.myPath, paint);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int desiredWidth = ((int) this.radius) * 2;
        int desiredHeight = ((int) this.radius) * 2;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == 1073741824) {
            width = widthSize;
        } else if (widthMode == Integer.MIN_VALUE) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }
        if (heightMode == 1073741824) {
            height = heightSize;
        } else if (heightMode == Integer.MIN_VALUE) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }
        this.iCenterWidth = width / 2;
        setMeasuredDimension(width, height);
    }

    public void setAdapter(ArrayList<Double> alPercentage) throws Exception {
        this.alPercentage = alPercentage;
        this.iDataSize = alPercentage.size();
        float fSum = 0.0f;
        for (int i = 0; i < this.iDataSize; i++) {
            fSum = (float) (((Double) alPercentage.get(i)).doubleValue() + ((double) fSum));
        }
        if (fSum != 100.0f) {
            Log.e(TAG, ERROR_NOT_EQUAL_TO_100);
            this.iDataSize = 0;
            throw new Exception(ERROR_NOT_EQUAL_TO_100);
        }
    }
}
