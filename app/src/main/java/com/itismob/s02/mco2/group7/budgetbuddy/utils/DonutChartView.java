package com.itismob.s02.mco2.group7.budgetbuddy.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class DonutChartView extends View {

    private final Paint arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rect = new RectF();

    private float[] values = new float[0];
    private int[] colors = new int[0];

    public DonutChartView(Context context) {
        this(context, null);
    }

    public DonutChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        arcPaint.setStyle(Paint.Style.STROKE);
        trackPaint.setStyle(Paint.Style.STROKE);
        trackPaint.setColor(Color.parseColor("#D5E2DC"));
    }

    public void setData(float[] values, int[] colors) {
        this.values = values;
        this.colors = colors;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float size = Math.min(getWidth(), getHeight());
        float stroke = size * 0.16f;
        float pad = stroke / 2f + 2;
        float left = (getWidth() - size) / 2f;
        float top = (getHeight() - size) / 2f;
        rect.set(left + pad, top + pad, left + size - pad, top + size - pad);

        arcPaint.setStrokeWidth(stroke);
        trackPaint.setStrokeWidth(stroke);

        canvas.drawArc(rect, 0, 360, false, trackPaint);

        float total = 0;
        for (float v : values) total += v;
        if (total <= 0) return;

        float gap = values.length > 1 ? 2f : 0f;
        float start = -90f;
        for (int i = 0; i < values.length; i++) {
            float sweep = values[i] / total * 360f;
            arcPaint.setColor(colors[i]);
            canvas.drawArc(rect, start + gap / 2f, Math.max(sweep - gap, 0.5f), false, arcPaint);
            start += sweep;
        }
    }
}