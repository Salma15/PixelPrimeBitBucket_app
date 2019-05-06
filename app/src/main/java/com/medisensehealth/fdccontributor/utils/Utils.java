package com.medisensehealth.fdccontributor.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class Utils {

	public static final String TAG = "FDCCon.com";

	public static  String USER_LOGIN_TYPE;
	public static  String USER_LOGIN_NAME;
	public static  int USER_LOGIN_ID = 0;

	// Dashboard Header TABS Strings
	public static final int HEADER_BLOGS = 1;
	public static final int HEADER_PATIENTS = 2;
	public static final int HEADER_DOCTORS = 3;


	public static void showToast(Context context, int stringid) {
		Toast.makeText(context, context.getString(stringid), Toast.LENGTH_SHORT)
				.show();
	}

	public static void showToast(Context context, String strMessage) {
		if (!TextUtils.isEmpty(strMessage))
			Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show();
	}

}
