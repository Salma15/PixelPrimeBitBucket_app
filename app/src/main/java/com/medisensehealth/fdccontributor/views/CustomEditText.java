package com.medisensehealth.fdccontributor.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by lenovo on 28/09/2017.
 */

public class CustomEditText extends AppCompatEditText {
    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText(Context context) {
        super(context);
        init();
    }

    private void init() {

        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/TCM_____.TTF");
        setTypeface(font, Typeface.NORMAL);
    }
}
