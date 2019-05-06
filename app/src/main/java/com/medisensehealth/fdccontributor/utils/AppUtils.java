package com.medisensehealth.fdccontributor.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.views.CustomTextView;

/**
 * Created by lenovo on 05/01/2018.
 */

public class AppUtils {

    public interface OnAlertClickListener {
        void onButtonClick();
    }

    public static void showCustomAlertMessage(Context context, String title, String description, String positiveButtonText, String response_msg, final OnAlertClickListener listener) {

        if (!((Activity) context).isFinishing()) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog_message);
            CustomTextView text_title  = (CustomTextView) dialog.findViewById(R.id.title);
            CustomTextView text_message = (CustomTextView) dialog.findViewById(R.id.message);
            text_title.setText(title);
            text_message.setText(description);

            CustomTextView dialogButton = (CustomTextView) dialog.findViewById(R.id.dialogButtonOK);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public static void showCustomErrorMessage(Context context, String title, String description, String positiveButtonText, String response_msg, final OnAlertClickListener listener) {

        if (!((Activity) context).isFinishing()) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_error_message);
            CustomTextView text_title  = (CustomTextView) dialog.findViewById(R.id.title);
            CustomTextView text_message = (CustomTextView) dialog.findViewById(R.id.message);
            text_title.setText(title);
            text_message.setText(description);

            CustomTextView dialogButton = (CustomTextView) dialog.findViewById(R.id.dialogButtonOK);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public static void showCustomSuccessMessage(Context context, String title, String description, String positiveButtonText, String response_msg, final OnAlertClickListener listener) {

        if (!((Activity) context).isFinishing()) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_success_message);
            CustomTextView text_title  = (CustomTextView) dialog.findViewById(R.id.title);
            CustomTextView text_message = (CustomTextView) dialog.findViewById(R.id.message);
            text_title.setText(title);
            text_message.setText(description);

            CustomTextView dialogButton = (CustomTextView) dialog.findViewById(R.id.dialogButtonOK);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}
