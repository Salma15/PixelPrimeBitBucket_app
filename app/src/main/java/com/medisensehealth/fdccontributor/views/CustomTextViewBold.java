package com.medisensehealth.fdccontributor.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by lenovo on 10-03-2017.
 */

public class CustomTextViewBold extends AppCompatTextView {

    public CustomTextViewBold(Context context) {
        super(context);
        setFont();
    }
    public CustomTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public CustomTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/TCB_____.TTF");
        setTypeface(font, Typeface.NORMAL);
    }
}
