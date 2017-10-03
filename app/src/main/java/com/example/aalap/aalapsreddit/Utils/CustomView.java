package com.example.aalap.aalapsreddit.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.aalap.aalapsreddit.R;

import org.w3c.dom.Attr;

/**
 * Created by Aalap on 2017-10-02.
 */

public class CustomView extends View {

    private int color, border;
    private float radius, size;
    private static final String TAG = "CustomView";
    int circleRadius=100;

    void init(@Nullable AttributeSet set){

        TypedArray typedArray = getContext().obtainStyledAttributes(set, R.styleable.CustomView);
        radius = typedArray.getDimension(R.styleable.CustomView_radius, 5);
        size = typedArray.getDimension(R.styleable.CustomView_size, 300);
        border = typedArray.getColor(R.styleable.CustomView_border, Color.CYAN);
        color = typedArray.getColor(R.styleable.CustomView_color, Color.BLUE);
        typedArray.recycle();
    }

    public CustomView(Context context) {
        super(context);
        init(null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect rect = new Rect();
        rect.top = 50;
        rect.left = 50;
        rect.right = (int) (rect.top + size);
        rect.bottom = (int) (rect.top + size);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);

        canvas.drawCircle(150, 150, circleRadius, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_HOVER_ENTER){
            Log.d(TAG, "onTouchEvent: hoverEnter");
        } else if(event.getAction() == MotionEvent.ACTION_DOWN){
            Log.d(TAG, "onTouchEvent: actionDown");

            circleRadius+=50;
            postInvalidate();

        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            Log.d(TAG, "onTouchEvent: actionMove");
        }
        return super.onTouchEvent(event);
    }
}
