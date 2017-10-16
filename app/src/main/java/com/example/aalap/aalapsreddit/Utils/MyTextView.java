package com.example.aalap.aalapsreddit.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Aalap on 2017-10-15.
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView {

    Typeface typeface;

    public MyTextView(Context context) {
        super(context);
        init(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        typeface = Typeface.createFromAsset(context.getAssets(), "Knowing How.ttf");
        setTypeface(typeface);
    }
}

