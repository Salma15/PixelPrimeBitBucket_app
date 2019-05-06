package com.medisensehealth.fdccontributor.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by lenovo on 11-03-2017.
 */

public class CustomTextViewItalic  extends AppCompatTextView {

    public CustomTextViewItalic(Context context) {
        super(context);
        setFont();
    }
    public CustomTextViewItalic(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public CustomTextViewItalic(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/TCMI____.TTF");
        setTypeface(font, Typeface.NORMAL);
    }
}
