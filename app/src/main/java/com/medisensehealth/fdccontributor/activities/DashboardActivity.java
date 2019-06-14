package com.medisensehealth.fdccontributor.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.doctors.DoctorsActivity;
import com.medisensehealth.fdccontributor.activities.home.HomeFragment;
import com.medisensehealth.fdccontributor.activities.notifications.NotificationActivity;
import com.medisensehealth.fdccontributor.activities.patients.PatientDetailsActivity;
import com.medisensehealth.fdccontributor.activities.settings.SettingsActivity;
import com.medisensehealth.fdccontributor.database.MedisensePracticeDB;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.services.GCMRegistrationIntentService;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomEditText;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by medisense on 15/01/18.
 */

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG_FRAGMENT = "TAG_DASH";
    Toolbar toolbar;
    static CustomTextView notifCount;
    static int mNotifCount = 0;

    LinearLayout footer_home, footer_favourites, footer_quiz, footer_account, footer_help, footer_connections;
    ImageView footer_home_img, footer_favourites_img, footer_quiz_img, footer_account_img, footer_help_img, footer_connections_img;
    CustomTextViewBold footer_home_txt, footer_favourites_txt, footer_quiz_txt, footer_account_txt, footer_help_txt, footer_connections_text;

    FrameLayout dasboard_frame;
    FragmentManager fragManager;
    FragmentTransaction fragTransaction;
    String ENTRY_TYPE = "NORMAL_TEST", USER_LOGIN_TYPE;
    int LOGIN_USER_ID = 0, HOSPITAL_ID=0;
    String USERNAME, USER_LOCATION, LOGIN_ENCRYPT_USERID, LOGIN_SPEC_NAME;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    View footer_tabs;
    String USER_GCM_TOKENID = "";
    // CGM Registration
    public static String GCM_SenderID;
    public BroadcastReceiver mRegistrationBroadcastReceiver;

    String blogid_subtitle, blogimage_largeicon, blog_title, blog_author, blog_date, blog_desc, blog_type, blog_patientId, blog_refId, blog_postKey;

    public static int pendingNotificationsCount = 0;

    public static int getPendingNotificationsCount() {
        return pendingNotificationsCount;
    }

    public static void setPendingNotificationsCount(int pendingNotifications) {
        pendingNotificationsCount = pendingNotifications;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        fragManager = getSupportFragmentManager();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Utils.USER_LOGIN_TYPE = extras.getString("LOGIN_TYPE");

            ENTRY_TYPE = extras.getString("ENTRY_TYPE");
            LOGIN_USER_ID =  extras.getInt("USER_ID");

            Log.d(Utils.TAG, " ************** Dashboard ***************** ");
            Log.d(Utils.TAG+" loginType: ", Utils.USER_LOGIN_TYPE);
            Log.d(Utils.TAG+" entryType: ", ENTRY_TYPE);
            Log.d(Utils.TAG+" Userid: ", String.valueOf(LOGIN_USER_ID));

            blogid_subtitle = extras.getString("BLOG_ID");
            blogimage_largeicon  = extras.getString("BLOG_IMAGE");
            blog_title = extras.getString("BLOG_TITLE");
            blog_author  = extras.getString("BLOG_AUTHOR");
            blog_date = extras.getString("BLOG_DATE");
            blog_desc = extras.getString("BLOG_DESCRIPTION");
            blog_type = extras.getString("BLOG_TYPE");
            blog_patientId = extras.getString("BLOG_PATIENT_ID");
            blog_refId = extras.getString("BLOG_REF_ID");
            blog_postKey = extras.getString("BLOG_KEY");
        }

        initToolBar();
        initialization(savedInstanceState);
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getText(R.string.app_name));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(null);
    }

    private void initialization(Bundle savedInstanceState) {

        setUpFooter();

        // FrameLayout Container
        dasboard_frame = (FrameLayout)findViewById(R.id.dashboard_fragment);
        footer_tabs = (View) findViewById(R.id.footer);

        shareadPreferenceClass = new ShareadPreferenceClass(getApplicationContext());
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(getApplicationContext());
        if(Utils.USER_LOGIN_TYPE.equals("1")) {
            Utils.USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, "0");
            Utils.USER_LOGIN_NAME = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            Utils.USER_LOGIN_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOCATION = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_CITY, "CITY");
            LOGIN_ENCRYPT_USERID  = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_ENCRYPT_USERID, "0");
            LOGIN_SPEC_NAME  = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_SPECIALIZATION_NAME, "SPEC_NAME");
            USER_GCM_TOKENID  = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_GCM_TOKENID, "");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);

            USERNAME = Utils.USER_LOGIN_NAME;
            LOGIN_USER_ID = Utils.USER_LOGIN_ID;
            USER_LOGIN_TYPE =  Utils.USER_LOGIN_TYPE;
        }
        else if(Utils.USER_LOGIN_TYPE.equals("2")) {
            Utils.USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, "0");
            Utils.USER_LOGIN_NAME = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            Utils.USER_LOGIN_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOCATION = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_LOCATION, "CITY");
            USER_GCM_TOKENID  = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_GCM_TOKENID, "");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);

            USERNAME = Utils.USER_LOGIN_NAME;
            LOGIN_USER_ID = Utils.USER_LOGIN_ID;
            USER_LOGIN_TYPE =  Utils.USER_LOGIN_TYPE;
        }
        else if(Utils.USER_LOGIN_TYPE.equals("3")) {
            Utils.USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, "0");
            Utils.USER_LOGIN_NAME = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            Utils.USER_LOGIN_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_GCM_TOKENID  = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_GCM_TOKENID, "");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);

            USERNAME = Utils.USER_LOGIN_NAME;
            LOGIN_USER_ID = Utils.USER_LOGIN_ID;
            USER_LOGIN_TYPE =  Utils.USER_LOGIN_TYPE;
        }

        Log.d(Utils.TAG+" TYPE: ", Utils.USER_LOGIN_TYPE);
        Log.d(Utils.TAG+" NAME: ", Utils.USER_LOGIN_NAME);
        Log.d(Utils.TAG+" LOGIN_ID: ", String.valueOf(Utils.USER_LOGIN_ID));

        if(USER_GCM_TOKENID.equals("")) {
            Log.d(Utils.TAG, "USER_GCM_TOKENID  empty");

            GCMRegistrationProcess();
        }
        else {
            Log.d(Utils.TAG, "USER_GCM_TOKENID  not empty");
        }


    /*    if(savedInstanceState == null) {
            Log.d(Utils.TAG, " savedInstanceState is null ");
            fragManager = getSupportFragmentManager();
            HomeFragment homeFrag = new HomeFragment();
            fragTransaction = fragManager.beginTransaction();
           // fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
            fragTransaction.add(R.id.dashboard_fragment,homeFrag,"HOME");
          //  fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // fragTransaction.addToBackStack(TAG_FRAGMENT);
            fragTransaction.commit();
        }
        else if (savedInstanceState != null) {

            Log.d(Utils.TAG, " savedInstanceState not null ");
            fragManager = getSupportFragmentManager();
            HomeFragment homeFrag = new HomeFragment();
            fragTransaction = fragManager.beginTransaction();
          //  fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
            fragTransaction.replace(R.id.dashboard_fragment,homeFrag,"HOME");
          //  fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
           // fragTransaction.addToBackStack(TAG_FRAGMENT);
            fragTransaction.commit();
        }*/

        if(ENTRY_TYPE.equalsIgnoreCase("NORMAL")) {
            if(savedInstanceState == null) {
                Log.d(Utils.TAG, " savedInstanceState is null ");

                HomeFragment homeFrag = new HomeFragment();
                fragTransaction = fragManager.beginTransaction();
             //   fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                fragTransaction.add(R.id.dashboard_fragment,homeFrag,"HOME");
             //   fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                // fragTransaction.addToBackStack(TAG_FRAGMENT);
                fragTransaction.commit();
            }
            else if (savedInstanceState != null) {

                Log.d(Utils.TAG, " savedInstanceState not null ");
                HomeFragment homeFrag = new HomeFragment();
                fragTransaction = fragManager.beginTransaction();
             //   fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                fragTransaction.replace(R.id.dashboard_fragment,homeFrag,"HOME");
             //   fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                // fragTransaction.addToBackStack(TAG_FRAGMENT);
                fragTransaction.commit();
            }
        }
        else if(ENTRY_TYPE.equalsIgnoreCase("RESPONSEPUSH")) {

            Cursor cursor_msg = MedisensePracticeDB.getAllNotification(getApplicationContext(), LOGIN_USER_ID, Utils.USER_LOGIN_TYPE);
            int notify_id = 0;
            cursor_msg.moveToFirst();
            if (cursor_msg != null) {
                do {
                    notify_id = cursor_msg.getInt(cursor_msg.getColumnIndex("ID"));
                }while (cursor_msg.moveToNext());
            }
            cursor_msg.close();

            Intent theintent = new Intent(DashboardActivity.this, PatientDetailsActivity.class);
            theintent.putExtra("PATIENT_ID", Integer.parseInt(blog_patientId));
            theintent.putExtra("DOCREF_ID", Integer.parseInt(blog_refId));
            theintent.putExtra("NOTIFY_KEY", notify_id);
            theintent.putExtra("PATIENT_VIEW","PUSHVIEW");
            startActivity(theintent);
            finish();
        }
        else if(ENTRY_TYPE.equalsIgnoreCase("APPOINTMENTPUSH")) {

            Cursor cursor_msg = MedisensePracticeDB.getAllNotification(getApplicationContext(), LOGIN_USER_ID, Utils.USER_LOGIN_TYPE);
            int notify_id = 0;
            cursor_msg.moveToFirst();
            if (cursor_msg != null) {
                do {
                    notify_id = cursor_msg.getInt(cursor_msg.getColumnIndex("ID"));
                }while (cursor_msg.moveToNext());
            }
            cursor_msg.close();

           /* AppointmentDetailsFragment appointmentDetails = new AppointmentDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("APPT_TRANSACTION_ID", blog_patientId);
            bundle.putString("APPT_TIME", "");
            fragTransaction = fragManager.beginTransaction();
            appointmentDetails.setArguments(bundle);
            fragTransaction.replace(R.id.dashboard_fragment,appointmentDetails,"APPOINTMENT_DETAIL");
            // fragTransaction.addToBackStack("APPOINTMENT_DETAIL");
            fragTransaction.commit();*/
        }
        else {
          /*  NotifyMessageFragment messsagesFrag = new NotifyMessageFragment();
            fragTransaction = fragManager.beginTransaction();
            //  fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
            fragTransaction.replace(R.id.dashboard_fragment,messsagesFrag,"MESSAGES");
            //    fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            //   fragTransaction.addToBackStack(TAG_FRAGMENT);
            fragTransaction.commit();*/

            Intent i1 = new Intent(DashboardActivity.this, NotificationActivity.class);
            i1.putExtra("title","Notifications");
            startActivity(i1);
        }

    }

    private void setUpFooter() {

        // Initialize footer views
        footer_home = (LinearLayout) findViewById(R.id.footer_home_layout);
        footer_favourites = (LinearLayout) findViewById(R.id.footer_favourites_layout);
        footer_quiz = (LinearLayout) findViewById(R.id.footer_quiz_layout);
        footer_account = (LinearLayout) findViewById(R.id.footer_myaccount_layout);
        footer_help = (LinearLayout) findViewById(R.id.footer_help_layout);
        footer_connections  = (LinearLayout) findViewById(R.id.footer_connections_layout);

        footer_home.setOnClickListener(this);
        footer_favourites.setOnClickListener(this);
        footer_quiz.setOnClickListener(this);
        footer_account.setOnClickListener(this);
        footer_help.setOnClickListener(this);
        footer_connections.setOnClickListener(this);

        footer_home_img = (ImageView) findViewById(R.id.footer_home_img);
        footer_favourites_img = (ImageView) findViewById(R.id.footer_favourites_img);
        footer_quiz_img = (ImageView) findViewById(R.id.footer_quiz_img);
        footer_account_img = (ImageView) findViewById(R.id.footer_myaccount_img);
        footer_help_img = (ImageView) findViewById(R.id.footer_help_img);
        footer_connections_img = (ImageView) findViewById(R.id.footer_connections_img);

        footer_home_txt = (CustomTextViewBold) findViewById(R.id.footer_home_text);
        footer_favourites_txt = (CustomTextViewBold) findViewById(R.id.footer_favourites_text);
        footer_quiz_txt = (CustomTextViewBold) findViewById(R.id.footer_quiz_text);
        footer_account_txt = (CustomTextViewBold) findViewById(R.id.footer_myaccount_text);
        footer_help_txt = (CustomTextViewBold) findViewById(R.id.footer_help_text);
        footer_connections_text = (CustomTextViewBold) findViewById(R.id.footer_connections_text);

        footer_home_img.setImageResource(R.mipmap.home_icon_select);
        footer_favourites_img.setImageResource(R.mipmap.favourite_icon_unselct);
        footer_quiz_img.setImageResource(R.mipmap.quiz_icon_unselect);
        footer_account_img.setImageResource(R.mipmap.my_account_unselect);
        footer_help_img.setImageResource(R.mipmap.help_icon_unselect);
        footer_connections_img.setImageResource(R.mipmap.connections_unselect);

        footer_home_txt.setTextColor(getResources().getColor(R.color.textPrimary));
        footer_favourites_txt.setTextColor(getResources().getColor(R.color.textColor));
        footer_quiz_txt.setTextColor(getResources().getColor(R.color.textColor));
        footer_account_txt.setTextColor(getResources().getColor(R.color.textColor));
        footer_help_txt.setTextColor(getResources().getColor(R.color.textColor));
        footer_connections_text.setTextColor(getResources().getColor(R.color.textColor));
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        MenuItem item = menu.findItem(R.id.action_notify);
        MenuItem item_share = menu.findItem(R.id.action_share);
        MenuItem item_bmj = menu.findItem(R.id.action_bmj);
       /* SpannableString s = new SpannableString("BMJ");
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        item_bmj.setTitle(s);*/

        if(mNotifCount == 0) {

        }
        else {
            MenuItemCompat.setActionView(item, R.layout.feed_update_count);
            View view = MenuItemCompat.getActionView(item);
            notifCount = (CustomTextView)view.findViewById(R.id.badge_notify);
            notifCount.setText(String.valueOf(mNotifCount));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(DashboardActivity.this, "Badge Notify msg", Toast.LENGTH_SHORT);
                    /*NotifyMessageFragment notFrag = new NotifyMessageFragment();
                    fragTransaction = fragManager.beginTransaction();
                    fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragTransaction.replace(R.id.dashboard_fragment,notFrag,"NOTIFICATION");
                    fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragTransaction.addToBackStack(TAG_FRAGMENT);
                    fragTransaction.commit();*/

                    Intent i1 = new Intent(DashboardActivity.this, NotificationActivity.class);
                    i1.putExtra("title","Notifications");
                    startActivity(i1);
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    private void setNotifCount(int count){
        mNotifCount = count;
        invalidateOptionsMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNotifCount = MedisensePracticeDB.getNotificationCount(getApplicationContext(), LOGIN_USER_ID, USER_LOGIN_TYPE);
        Log.d(Utils.TAG, " dash notifyCount: " + String.valueOf(mNotifCount));
        setNotifCount(mNotifCount);

        try {
            if (mRegistrationBroadcastReceiver!=null) {
                LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                        new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
                LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                        new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:

                if(Utils.USER_LOGIN_TYPE.equals("1")) {
                    Utils.USER_LOGIN_NAME = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
                    USER_LOCATION = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_CITY, "CITY");
                    LOGIN_SPEC_NAME  = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_SPECIALIZATION_NAME, "SPEC_NAME");
                    USERNAME = Utils.USER_LOGIN_NAME;
                }
                else if(Utils.USER_LOGIN_TYPE.equals("2")) {
                    Utils.USER_LOGIN_NAME = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
                    LOGIN_SPEC_NAME  = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_SPECIALIZATION_NAME, "SPEC_NAME");
                    USER_LOCATION = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_LOCATION, "CITY");
                    USERNAME = Utils.USER_LOGIN_NAME;
                }
                else if(Utils.USER_LOGIN_TYPE.equals("3")) {
                    Utils.USER_LOGIN_NAME = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
                    USERNAME = Utils.USER_LOGIN_NAME;
                }

                boolean isFirstRun = sharedPreferences.getBoolean("UPDATESHAREPROFILEPRIME", true);
                if (isFirstRun)
                {
                    getProfileInformation();
                }
                else {

                    customDialogShareProfile(USERNAME, LOGIN_SPEC_NAME, USER_LOCATION);
                    /*Intent sharingProfileIntent = new Intent(Intent.ACTION_SEND);
                    sharingProfileIntent.setType("text/plain");
                    sharingProfileIntent.putExtra(Intent.EXTRA_SUBJECT, "Pixel Prime");
                    sharingProfileIntent.putExtra(Intent.EXTRA_TEXT, USERNAME+"\n" + LOGIN_SPEC_NAME + "\n" + USER_LOCATION + "\n\n"+ "You can connect with me & also book an appointment by visiting the link below: "+"\n"+"https://pixeleyecare.com/Doctor-Profile/?id="+LOGIN_ENCRYPT_USERID);
                    startActivity(Intent.createChooser(sharingProfileIntent, "Share Profile via"));*/
                }
                break;
            case R.id.action_bmj:
                if (NetworkUtil.getConnectivityStatusString(this).equalsIgnoreCase("enabled")) {
                    Intent theintent = new Intent(this, BMJQRScanActivity.class);
                    theintent.putExtra("title","BMJ");
                    this.startActivity(theintent);
                } else {
                    AppUtils.showCustomAlertMessage(this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
                break;
            case R.id.action_notify:
                Toast.makeText(DashboardActivity.this, "Notify msg", Toast.LENGTH_SHORT);
               /* NotifyMessageFragment notifyFrag = new NotifyMessageFragment();
                fragTransaction = fragManager.beginTransaction();
                fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                fragTransaction.replace(R.id.dashboard_fragment,notifyFrag,"NOTIFICATION");
                fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragTransaction.addToBackStack(TAG_FRAGMENT);
                fragTransaction.commit();*/

                Intent i1 = new Intent(DashboardActivity.this, NotificationActivity.class);
                i1.putExtra("title","Notifications");
                startActivity(i1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void customDialogShareProfile(final String USERNAME, final String LOGIN_SPEC_NAME, final String USER_LOCATION) {

        final Dialog dialog = new Dialog(DashboardActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_share_profile);

        final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(

                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        );

        final TextInputLayout mobile_TL = (TextInputLayout) dialog.findViewById(R.id.share_profile_mobile_layout);
        final TextInputLayout email_TL = (TextInputLayout) dialog.findViewById(R.id.share_profile_email_layout);
        final CustomEditText _edt_mobile = (CustomEditText)  dialog.findViewById(R.id.share_profile_mobile);
        final CustomEditText _edt_email = (CustomEditText)  dialog.findViewById(R.id.share_profile_email);
        LinearLayout share_mycontact = (LinearLayout) dialog.findViewById(R.id.share_profile_mycontact);

        share_mycontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                String hosp_id = "";
                try {
                    hosp_id = md5(String.valueOf(HOSPITAL_ID));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                Intent sharingProfileIntent = new Intent(Intent.ACTION_SEND);
                sharingProfileIntent.setType("text/plain");
                sharingProfileIntent.putExtra(Intent.EXTRA_SUBJECT, "Pixel Prime");
                sharingProfileIntent.putExtra(Intent.EXTRA_TEXT, USERNAME+"\n" + LOGIN_SPEC_NAME + "\n" + USER_LOCATION + "\n\n"+ "You can connect with me & also book an appointment by visiting the link below: "+"\n\n"+APIClass.BASE_URL+"/Doctor-Profile?id="+LOGIN_ENCRYPT_USERID+"&hid="+hosp_id);
                startActivity(Intent.createChooser(sharingProfileIntent, "Share Profile Via"));


              /*  boolean isFirstRun = sharedPreferences.getBoolean("UPDATESHAREPROFILE", true);
                if (isFirstRun)
                {
                    getProfileInformation();
                }
                else {
                    Intent sharingProfileIntent = new Intent(Intent.ACTION_SEND);
                    sharingProfileIntent.setType("text/plain");
                    sharingProfileIntent.putExtra(Intent.EXTRA_SUBJECT, "FDC Pixel");
                    sharingProfileIntent.putExtra(Intent.EXTRA_TEXT, USERNAME+"\n" + LOGIN_SPEC_NAME + "\n" + USER_LOCATION + "\n\n"+ "You can connect with me & also book an appointment by visiting the link below: "+"\n"+"https://pixeleyecare.com/SendRequestLink/RefLink?d="+LOGIN_ENCRYPT_USERID);
                    startActivity(Intent.createChooser(sharingProfileIntent, "Share Profile Via"));
                }*/
            }
        });

        CustomTextView send_btn = (CustomTextView) dialog.findViewById(R.id.share_profile_submit);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((_edt_mobile.getText().toString().equals("")) && (_edt_email.getText().toString().equals(""))) {
                    mobile_TL.setError("Enter Mobile No.");
                    email_TL.setError("Enter Email Address");
                }
                else {
                    mobile_TL.setError(null);
                    email_TL.setError(null);

                    String get_mobile = "", get_email = "";

                    if(_edt_mobile.getText().toString()!= null && _edt_mobile.getText().toString().length() != 0 ) {

                        if(_edt_mobile.getText().toString().length() < 10)
                            mobile_TL.setError("10 digit mobile no.");
                        else {
                            get_mobile = _edt_mobile.getText().toString().trim();
                            mobile_TL.setError(null);
                            // Toast.makeText(DashboardActivity.this, " Mobile Success", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            submitShareProfileLink(get_mobile, get_email, USERNAME, LOGIN_SPEC_NAME, USER_LOCATION);
                        }

                    }
                    else if(_edt_email.getText().toString()!= null && _edt_email.getText().toString().length() != 0 ) {

                        if(EMAIL_ADDRESS_PATTERN.matcher(_edt_email.getText().toString()).matches()) {
                            get_email = _edt_email.getText().toString().trim();
                            email_TL.setError(null);
                            //  Toast.makeText(DashboardActivity.this, " Email Success", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            submitShareProfileLink(get_mobile, get_email, USERNAME, LOGIN_SPEC_NAME, USER_LOCATION);
                        }
                        else {
                            email_TL.setError("Invalid Email Address");
                        }
                    }
                    // Toast.makeText(DashboardActivity.this, " Success", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void submitShareProfileLink(final String get_mobile, final String get_email, final String USERNAME, final String LOGIN_SPEC_NAME, final String  USER_LOCATION) {

        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sharing your profile.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_SHARE_PROFILE_LINK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "result: "+ response.toString());
                        if (response != null) {
                            AppUtils.showCustomAlertMessage(DashboardActivity.this, "Success", "Your profile link was successfully shared. Now, they can book your appointment or request for medical opinion.","OK", null, null);
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        AppUtils.showCustomAlertMessage(DashboardActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                        Log.d(Utils.TAG+"ERR",error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(Utils.USER_LOGIN_ID));
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                map.put(APIClass.SHARE_EMAIL_ID, get_email);
                map.put(APIClass.SHARE_MOBILE_NUM, get_mobile);
                map.put(APIClass.SHARE_USER_NAME, USERNAME);
                map.put(APIClass.SHARE_SPEC_NAME, LOGIN_SPEC_NAME);
                map.put(APIClass.SHARE_USER_LOCATION, USER_LOCATION);
                map.put(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(DashboardActivity.this).
                getRequestQueue();
        AppController.getInstance(DashboardActivity.this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.footer_home_layout:
                footer_home_img.setImageResource(R.mipmap.home_icon_select);
                footer_favourites_img.setImageResource(R.mipmap.favourite_icon_unselct);
                footer_quiz_img.setImageResource(R.mipmap.quiz_icon_unselect);
                footer_account_img.setImageResource(R.mipmap.my_account_unselect);
                footer_help_img.setImageResource(R.mipmap.help_icon_unselect);
                footer_connections_img.setImageResource(R.mipmap.connections_unselect);

                footer_home_txt.setTextColor(getResources().getColor(R.color.textTitle));
                footer_favourites_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_quiz_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_account_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_help_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_connections_text.setTextColor(getResources().getColor(R.color.textColor));

                collectHomePage(1);
                break;
            case R.id.footer_favourites_layout:
                footer_home_img.setImageResource(R.mipmap.home_icon_unselect);
                footer_favourites_img.setImageResource(R.mipmap.favourite_icon_select);
                footer_quiz_img.setImageResource(R.mipmap.quiz_icon_unselect);
                footer_account_img.setImageResource(R.mipmap.my_account_unselect);
                footer_help_img.setImageResource(R.mipmap.help_icon_unselect);
                footer_connections_img.setImageResource(R.mipmap.connections_unselect);

                footer_home_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_favourites_txt.setTextColor(getResources().getColor(R.color.textTitle));
                footer_quiz_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_account_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_help_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_connections_text.setTextColor(getResources().getColor(R.color.textColor));

                collectHomePage(2);
                break;
            case R.id.footer_quiz_layout:
                footer_home_img.setImageResource(R.mipmap.home_icon_unselect);
                footer_favourites_img.setImageResource(R.mipmap.favourite_icon_unselct);
                footer_quiz_img.setImageResource(R.mipmap.quiz_icon_select);
                footer_account_img.setImageResource(R.mipmap.my_account_unselect);
                footer_help_img.setImageResource(R.mipmap.help_icon_unselect);
                footer_connections_img.setImageResource(R.mipmap.connections_unselect);

                footer_home_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_favourites_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_quiz_txt.setTextColor(getResources().getColor(R.color.textTitle));
                footer_account_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_help_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_connections_text.setTextColor(getResources().getColor(R.color.textColor));

                collectHomePage(3);
                break;
            case R.id.footer_myaccount_layout:
                footer_home_img.setImageResource(R.mipmap.home_icon_unselect);
                footer_favourites_img.setImageResource(R.mipmap.favourite_icon_unselct);
                footer_quiz_img.setImageResource(R.mipmap.quiz_icon_unselect);
                footer_account_img.setImageResource(R.mipmap.my_account_select);
                footer_help_img.setImageResource(R.mipmap.help_icon_unselect);
                footer_connections_img.setImageResource(R.mipmap.connections_unselect);

                footer_home_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_favourites_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_quiz_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_account_txt.setTextColor(getResources().getColor(R.color.textTitle));
                footer_help_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_connections_text.setTextColor(getResources().getColor(R.color.textColor));

                collectHomePage(4);
                break;
            case R.id.footer_help_layout:
                footer_home_img.setImageResource(R.mipmap.home_icon_unselect);
                footer_favourites_img.setImageResource(R.mipmap.favourite_icon_unselct);
                footer_quiz_img.setImageResource(R.mipmap.quiz_icon_unselect);
                footer_account_img.setImageResource(R.mipmap.my_account_unselect);
                footer_help_img.setImageResource(R.mipmap.help_icon_select);
                footer_connections_img.setImageResource(R.mipmap.connections_unselect);

                footer_home_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_favourites_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_quiz_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_account_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_help_txt.setTextColor(getResources().getColor(R.color.textTitle));
                footer_connections_text.setTextColor(getResources().getColor(R.color.textColor));
                collectHomePage(5);
                break;
            case R.id.footer_connections_layout:
                footer_home_img.setImageResource(R.mipmap.home_icon_unselect);
                footer_favourites_img.setImageResource(R.mipmap.favourite_icon_unselct);
                footer_quiz_img.setImageResource(R.mipmap.quiz_icon_unselect);
                footer_account_img.setImageResource(R.mipmap.my_account_unselect);
                footer_help_img.setImageResource(R.mipmap.help_icon_unselect);
                footer_connections_img.setImageResource(R.mipmap.connections_select);

                footer_home_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_favourites_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_quiz_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_account_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_help_txt.setTextColor(getResources().getColor(R.color.textColor));
                footer_connections_text.setTextColor(getResources().getColor(R.color.textTitle));

                collectHomePage(6);
                break;
        }
    }

    private void collectHomePage(int mode) {
        if(mode == 1) {
            fragManager = getSupportFragmentManager();
            HomeFragment homeFrag = new HomeFragment();
            fragTransaction = fragManager.beginTransaction();
          //  fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
            fragTransaction.replace(R.id.dashboard_fragment,homeFrag,"HOME");
          //  fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragTransaction.addToBackStack(TAG_FRAGMENT);
            fragTransaction.commit();
        }
        else  if(mode == 2) {
            if (NetworkUtil.getConnectivityStatusString(this).equalsIgnoreCase("enabled")) {
                fragManager = getSupportFragmentManager();
               /* FavouriteFragment favouriteFrag = new FavouriteFragment();
                fragTransaction = fragManager.beginTransaction();
                fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                fragTransaction.replace(R.id.dashboard_fragment,favouriteFrag,"FAVOURITE");
                fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragTransaction.addToBackStack(TAG_FRAGMENT);
                fragTransaction.commit();*/
            }else {
                AppUtils.showCustomAlertMessage(DashboardActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }
        }
        else  if(mode == 3) {
          /*  QuizFragment quizFrag = new QuizFragment();
            fragTransaction = fragManager.beginTransaction();
            fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
            fragTransaction.replace(R.id.dashboard_fragment,quizFrag,"QUIZ");
            fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragTransaction.addToBackStack(TAG_FRAGMENT);
            fragTransaction.commit();*/
        }
        else  if(mode == 4) {
            fragManager = getSupportFragmentManager();
           /* MyAccountFragment favouriteFrag = new MyAccountFragment();
            fragTransaction = fragManager.beginTransaction();
            fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
            fragTransaction.replace(R.id.dashboard_fragment,favouriteFrag,"MY_ACCOUNT");
            fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragTransaction.addToBackStack(TAG_FRAGMENT);
            fragTransaction.commit();*/

            Intent i9 = new Intent(DashboardActivity.this, SettingsActivity.class);
            i9.putExtra("title","Settings");
            startActivity(i9);
        }
        else  if(mode == 5) {
            userguideDisplay();
        }
        else if(mode == 6) {
            Intent i7 = new Intent(DashboardActivity.this, DoctorsActivity.class);
            i7.putExtra("title","Connections");
            i7.putExtra("DOCTOR_TYPE", "1");
            startActivity(i7);
        }
    }

    private void userguideDisplay() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);

        builder.setTitle("Welcome!");
        builder.setMessage("Would you like to go through a quick tour which explains all the features?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(DashboardActivity.this, ShowCaseViewActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }

        });

        builder.setNegativeButton("SKIP", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        // Toast.makeText(DashboardActivity.this, " count back: "+fragManager.getBackStackEntryCount(), Toast.LENGTH_SHORT).show();
       /* if (fragManager.getBackStackEntryCount() > 0) {
            fragManager.popBackStack();
        } else {
            super.onBackPressed();
        }*/

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DashboardActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    private void getProfileInformation() {

        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching profile info...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PREM_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "result: "+ response.toString());
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray JAStuff = jsonObject.getJSONArray("result");
                                JSONArray JASchedule = jsonObject.getJSONArray("schedules");
                                if(JASchedule.length() > 0) {
                                    Log.d(Utils.TAG, " timings updated ");
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("UPDATESHAREPROFILEPRIME", false);
                                    editor.commit();

                                    customDialogShareProfile(USERNAME, LOGIN_SPEC_NAME, USER_LOCATION);

                                    /*Intent sharingProfileIntent = new Intent(Intent.ACTION_SEND);
                                    sharingProfileIntent.setType("text/plain");
                                    sharingProfileIntent.putExtra(Intent.EXTRA_SUBJECT, "Pixel Prime");
                                    sharingProfileIntent.putExtra(Intent.EXTRA_TEXT, USERNAME+"\n" + LOGIN_SPEC_NAME + "\n" + USER_LOCATION + "\n\n"+ "You can connect with me & also book an appointment by visiting the link below: "+"\n"+"https://pixeleyecare.com/Doctor-Profile/?id="+LOGIN_ENCRYPT_USERID);
                                    startActivity(Intent.createChooser(sharingProfileIntent, "Share Profile via"));*/
                                }
                                else {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("UPDATESHAREPROFILEPRIME", true);
                                    editor.commit();
                                    Log.d(Utils.TAG, " timings not updated ");

                                    //   AppUtils.showCustomAlertMessage(DashboardActivity.this, "Update your Profile", "Please update your profile including appointment timings before sharing your profile. \nThe patients can view your profile, book appointment, request opinion & also share your profile.","OK", null, null);

                                    final Dialog dialog = new Dialog(DashboardActivity.this, R.style.CustomDialog);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(false);
                                    dialog.setContentView(R.layout.custom_dialog_message);

                                    CustomTextView text = (CustomTextView) dialog.findViewById(R.id.title);
                                    text.setText("Update your Profile");

                                    CustomTextView message = (CustomTextView) dialog.findViewById(R.id.message);
                                    message.setText("Please update your profile including appointment timings before sharing your profile. \nThe patients can view your profile, book appointment, request opinion & also share your profile.");

                                    CustomTextView dialogButton = (CustomTextView) dialog.findViewById(R.id.dialogButtonOK);
                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            if (NetworkUtil.getConnectivityStatusString(DashboardActivity.this).equalsIgnoreCase("enabled")) {
                                               /* EditProfileFragment profileFrag = new EditProfileFragment();
                                                fragTransaction = fragManager.beginTransaction();
                                                fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                                                fragTransaction.replace(R.id.dashboard_fragment,profileFrag,"EDIT_PROFILE");
                                                fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                                fragTransaction.addToBackStack("EDIT_PROFILE");
                                                fragTransaction.commit();*/
                                            } else {
                                                AppUtils.showCustomAlertMessage(DashboardActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                                            }
                                        }
                                    });

                                    dialog.show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        AppUtils.showCustomAlertMessage(DashboardActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                        Log.d(Utils.TAG+"ERR",error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(Utils.USER_LOGIN_ID));
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                map.put(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(DashboardActivity.this).
                getRequestQueue();
        AppController.getInstance(DashboardActivity.this).addToRequestQueue(stringRequest);

    }

    private void GCMRegistrationProcess() {
        //Initializing our broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    String token = intent.getStringExtra("token");
                    GCM_SenderID = token;
                    Log.d(Utils.TAG, "GCM_SenderID: " + GCM_SenderID);

                     new UpdateGCMToken().execute("");

                 } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    GCM_SenderID = "";
                } else {
                    GCM_SenderID = "";
                }
            }
        };

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        Log.d(Utils.TAG, "resultCode: " + resultCode);
        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
            //If play service is available
        } else {
            Log.d(Utils.TAG, "resultCode INSIDE " + resultCode);
            //Starting intent to register device
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            if (mRegistrationBroadcastReceiver!=null) {
                LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private class UpdateGCMToken extends AsyncTask<String, Void, String> {
        String result_data;
        @Override
        protected String doInBackground(String... params) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_LOGIN_GCMUPDATE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG, response.toString());
                            if (response != null) {
                                //  GetJsonArrayResponse(response);
                                result_data = response.toString();

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if(jsonObject.getString("status").equals("true")) {
                                        SharedPreferences.Editor editor;
                                        if(Utils.USER_LOGIN_TYPE.equals("1")) {
                                            if (sharedPreferences != null) {
                                                editor = sharedPreferences.edit();
                                                editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_GCM_TOKENID, GCM_SenderID);
                                                editor.commit();
                                            }
                                        }
                                        else if(Utils.USER_LOGIN_TYPE.equals("2")) {
                                            if (sharedPreferences != null) {
                                                editor = sharedPreferences.edit();
                                                editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_GCM_TOKENID, GCM_SenderID);
                                                editor.commit();
                                            }
                                        }
                                        else if(Utils.USER_LOGIN_TYPE.equals("3")) {
                                            if (sharedPreferences != null) {
                                                editor = sharedPreferences.edit();
                                                editor.putString(HCConstants.PREF_LOGINACTIVITY_MARKET_GCM_TOKENID, GCM_SenderID);
                                                editor.commit();
                                            }

                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            result_data = "Error";
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                    map.put(APIClass.KEY_USER_LOGINTYPE,USER_LOGIN_TYPE);
                    map.put(APIClass.KEY_USER_USERID, String.valueOf(LOGIN_USER_ID));
                    map.put(APIClass.KEY_USER_GCMTOKEN,GCM_SenderID);
                    map.put(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);
                    return map;
                }
            };

            RequestQueue requestQueue = AppController.getInstance(DashboardActivity.this).
                    getRequestQueue();
            AppController.getInstance(DashboardActivity.this).addToRequestQueue(stringRequest);
            return result_data;
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    public static String md5(String input) throws NoSuchAlgorithmException {
        String result = input;
        if(input != null) {
            MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while(result.length() < 32) { //31 position string
                result = "0" + result;
            }
        }
        return result;
    }
}
