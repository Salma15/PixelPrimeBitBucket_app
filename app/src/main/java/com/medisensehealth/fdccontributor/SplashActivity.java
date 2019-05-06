package com.medisensehealth.fdccontributor;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.medisensehealth.fdccontributor.activities.DashboardActivity;
import com.medisensehealth.fdccontributor.activities.LoginActivity;
import com.medisensehealth.fdccontributor.activities.ShowCaseViewActivity;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewSemiBold;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        shareadPreferenceClass = new ShareadPreferenceClass(getApplicationContext());
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivityScreen();
            }
        }, SPLASH_TIME_OUT);
    }

    private void startActivityScreen() {
        String UserType;
        final int Userid;

        if(sharedPreferences != null ) {
            UserType = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, "0");

            if (UserType.equals("1")) {

                final String login_type = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, "0");
                String string_contactNum = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_CONTACT_NUM, "");
                String string_password = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_PASSWORD, "");
                Userid =  sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);

                if (string_contactNum.length() > 0 && string_password.length() > 4) {

                    if (NetworkUtil.getConnectivityStatusString(this).equalsIgnoreCase("enabled")) {

                        boolean isFirstRunShow = sharedPreferences.getBoolean("FIRSTRUNSHOECASE", true);
                        if (isFirstRunShow)
                        {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                            builder.setTitle("Welcome!");
                            builder.setMessage("Would you like to go through a quick tour which explains all the features?");
                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    Intent intent = new Intent(SplashActivity.this, ShowCaseViewActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                    finish();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("FIRSTRUNSHOECASE", false);
                                    editor.commit();
                                }
                            });
                            builder.setNegativeButton("SKIP", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                                    intent.putExtra("LOGIN_TYPE", login_type);
                                    intent.putExtra("USER_ID", Userid);
                                    intent.putExtra("ENTRY_TYPE", "NORMAL");
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                    finish();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else {
                            Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                            intent.putExtra("LOGIN_TYPE", login_type);
                            intent.putExtra("USER_ID", Userid);
                            intent.putExtra("ENTRY_TYPE", "NORMAL");
                            startActivity(intent);
                            overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            finish();
                        }
                    }else {
                        final Dialog dialog = new Dialog(SplashActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.custom_dialog_message);
                        CustomTextView text_title  = (CustomTextView) dialog.findViewById(R.id.title);
                        CustomTextView text_message = (CustomTextView) dialog.findViewById(R.id.message);
                        text_title.setText(HCConstants.INTERNET);
                        text_message.setText(HCConstants.INTERNET_CHECK);

                        CustomTextView dialogButton = (CustomTextView) dialog.findViewById(R.id.dialogButtonOK);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        dialog.show();
                    }

                } else {

                    boolean isFirstRun = sharedPreferences.getBoolean("FIRSTRUN", true);
                    if (isFirstRun)
                    {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("FIRSTRUN", false);
                        editor.commit();
                    }
                    else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                    }

                }
            } else if (UserType.equals("2")) {
                String login_type = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, "0");
                String string_contactNum = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_CONTACTNUM1, "");
                String string_password = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_PASSWORD, "");
                Userid =  sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
                if (string_contactNum.length() > 0 && string_password.length() > 4) {

                    if (NetworkUtil.getConnectivityStatusString(this).equalsIgnoreCase("enabled")) {
                        Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                        intent.putExtra("LOGIN_TYPE", login_type);
                        intent.putExtra("USER_ID", Userid);
                        intent.putExtra("ENTRY_TYPE", "NORMAL");
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                    }else {
                        final Dialog dialog = new Dialog(SplashActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.custom_dialog_message);
                        CustomTextViewSemiBold text_title  = (CustomTextViewSemiBold) dialog.findViewById(R.id.title);
                        CustomTextView text_message = (CustomTextView) dialog.findViewById(R.id.message);
                        text_title.setText(HCConstants.INTERNET);
                        text_message.setText(HCConstants.INTERNET_CHECK);

                        CustomTextView dialogButton = (CustomTextView) dialog.findViewById(R.id.dialogButtonOK);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        dialog.show();
                    }

                } else {
                    boolean isFirstRun = sharedPreferences.getBoolean("FIRSTRUN", true);
                    if (isFirstRun)
                    {
                       /* Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();*/

                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("FIRSTRUN", false);
                        editor.commit();
                    }
                    else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                    }
                }


            }
            else if (UserType.equals("3")) {
                final String login_type = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, "0");
                String string_contactNum = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_MOBILE, "");
                String string_password = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_PASSWORD, "");
                Userid =  sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);

                if (string_contactNum.length() > 0 && string_password.length() > 4) {

                    if (NetworkUtil.getConnectivityStatusString(this).equalsIgnoreCase("enabled")) {
                        boolean isFirstRunShow = sharedPreferences.getBoolean("FIRSTRUNSHOECASE", true);
                        if (isFirstRunShow)
                        {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                            builder.setTitle("Welcome!");
                            builder.setMessage("Would you like to go through a quick tour which explains all the features?");
                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    Intent intent = new Intent(SplashActivity.this, ShowCaseViewActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                    finish();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("FIRSTRUNSHOECASE", false);
                                    editor.commit();
                                }
                            });
                            builder.setNegativeButton("SKIP", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                                    intent.putExtra("LOGIN_TYPE", login_type);
                                    intent.putExtra("USER_ID", Userid);
                                    intent.putExtra("ENTRY_TYPE", "NORMAL");
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                    finish();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else {
                            Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                            intent.putExtra("LOGIN_TYPE", login_type);
                            intent.putExtra("USER_ID", Userid);
                            intent.putExtra("ENTRY_TYPE", "NORMAL");
                            startActivity(intent);
                            overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            finish();
                        }
                    }else {
                        final Dialog dialog = new Dialog(SplashActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.custom_dialog_message);
                        CustomTextViewSemiBold text_title  = (CustomTextViewSemiBold) dialog.findViewById(R.id.title);
                        CustomTextView text_message = (CustomTextView) dialog.findViewById(R.id.message);
                        text_title.setText(HCConstants.INTERNET);
                        text_message.setText(HCConstants.INTERNET_CHECK);

                        CustomTextView dialogButton = (CustomTextView) dialog.findViewById(R.id.dialogButtonOK);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        dialog.show();
                    }

                } else {
                    boolean isFirstRun = sharedPreferences.getBoolean("FIRSTRUN", true);
                    if (isFirstRun)
                    {
                       /* Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();*/

                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("FIRSTRUN", false);
                        editor.commit();
                    }
                    else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                    }
                }

            }
            else {
                boolean isFirstRun = sharedPreferences.getBoolean("FIRSTRUN", true);
                if (isFirstRun)
                {
                   /* Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();*/

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("FIRSTRUN", false);
                    editor.commit();
                }
                else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish();
                }
            }
        }

    }
}
