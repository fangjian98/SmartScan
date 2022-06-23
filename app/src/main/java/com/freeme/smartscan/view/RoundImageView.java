package com.freeme.smartscan.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundImageView extends ImageView {
    float width, height;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int x = 16;
        int y = 16;
        if (width > x && height > y) {
            Path path = new Path();
            path.moveTo(x, 0);
            path.lineTo(width - x, 0);
            path.quadTo(width, 0, width, y);
            path.lineTo(width, height - y);
            path.quadTo(width, height, width - x, height);
            path.lineTo(x, height);
            path.quadTo(0, height, 0, height - y);
            path.lineTo(0, y);
            path.quadTo(0, 0, x, 0);
            canvas.clipPath(path);
        }
        super.onDraw(canvas);
    }
}
