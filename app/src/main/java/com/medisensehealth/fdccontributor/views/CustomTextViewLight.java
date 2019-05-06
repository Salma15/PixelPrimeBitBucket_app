package com.medisensehealth.fdccontributor.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by lenovo on 28/09/2017.
 */

public class CustomTextViewLight extends AppCompatTextView {

    public CustomTextViewLight(Context context) {
        super(context);
        setFont();
    }
    public CustomTextViewLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public CustomTextViewLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/TCCM____.TTF");
        setTypeface(font, Typeface.NORMAL);
    }
}
