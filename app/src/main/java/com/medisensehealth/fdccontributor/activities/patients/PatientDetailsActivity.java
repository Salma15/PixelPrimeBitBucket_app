package com.medisensehealth.fdccontributor.activities.patients;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.DashboardActivity;
import com.medisensehealth.fdccontributor.database.MedisensePracticeDB;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextView;

/**
 * Created by HP on 24-03-2018.
 */

public class PatientDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    int Patient_ID;
    int DOCREF_ID;
    String Patient_View;
    FragmentManager fragManager;
    FragmentTransaction fragTransaction;

    FrameLayout frame_container;
    Button detail_btn;
    Button chat_btn;
    Button attachment_btn;
    int count = 0;
    int STATUS_CHANGE_VALUE;
    String  STATUS_CHANGE_NAME;

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE, PATIENT_DETAIL;
    static int mNotifCount = 0;
    static CustomTextView notifCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_patient_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle b = getIntent().getExtras();
        if( b != null) {
            String title = b.getString("title");
            setTitle(title);

            Patient_ID = b.getInt("PATIENT_ID", 0);
            DOCREF_ID =  b.getInt("DOCREF_ID", 0);
            Patient_View = b.getString("PATIENT_VIEW", "VIEW");
            int VERIFY_ID =  b.getInt("NOTIFY_KEY", 0);

            Log.d(Utils.TAG + " Patient Id:", String.valueOf(Patient_ID));
            Log.d(Utils.TAG + " Doctor Id:", String.valueOf(DOCREF_ID));
            Log.d(Utils.TAG + " Pat_View:", Patient_View);
            Log.d(Utils.TAG + " NOTIFY_KEY:", String.valueOf(VERIFY_ID));

            if(Patient_View.equals("PUSHVIEW")) {

                String delete_msg = MedisensePracticeDB.deleteNotificationContents(PatientDetailsActivity.this, USER_ID, USER_LOGIN_TYPE, VERIFY_ID);
                Log.d(Utils.TAG + " del_msg: ", delete_msg);
                int notifyCount = MedisensePracticeDB.getNotificationCount(PatientDetailsActivity.this, USER_ID, USER_LOGIN_TYPE);
                Log.d(Utils.TAG + " notCount: ", String.valueOf(notifyCount));
                if(notifyCount > 0) {
                    DashboardActivity.setPendingNotificationsCount(notifyCount);
                }

                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();

            }
        }

        shareadPreferenceClass = new ShareadPreferenceClass(this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(this);

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {

            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        Log.d(Utils.TAG, " USER_ID: "+USER_ID);
        Log.d(Utils.TAG, " USER_NAME: "+USER_NAME);
        Log.d(Utils.TAG, " LOGIN_TYPE: "+USER_LOGIN_TYPE);

        initializationViews(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void initializationViews(Bundle savedInstanceState) {

        frame_container = (FrameLayout)findViewById(R.id.viewpager);
        detail_btn = (Button)findViewById(R.id.btn_details);
        chat_btn = (Button)findViewById(R.id.btn_chats);
        attachment_btn = (Button)findViewById(R.id.btn_attachments);
        detail_btn.setOnClickListener(this);
        chat_btn.setOnClickListener(this);
        attachment_btn.setOnClickListener(this);

        detail_btn.setBackgroundColor(getResources().getColor(R.color.header_tab_bg));
        chat_btn.setBackgroundColor(getResources().getColor(R.color.white));
        attachment_btn.setBackgroundColor(getResources().getColor(R.color.white));
        detail_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
        chat_btn.setTextColor(getResources().getColor(R.color.black));
        attachment_btn.setTextColor(getResources().getColor(R.color.black));

        if(savedInstanceState == null)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putInt("PATIENT_ID", Patient_ID);
            bundle1.putInt("DOCREF_ID", DOCREF_ID);
            bundle1.putString("PATIENT_VIEW", Patient_View);
            PatientsViewFragment homeFragment = new PatientsViewFragment();
            fragManager = getSupportFragmentManager();
            fragTransaction = fragManager.beginTransaction();
            homeFragment.setArguments(bundle1);
            fragTransaction.add(R.id.viewpager,homeFragment,"DETAILS");
            fragTransaction.commit();
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle;
        switch(v.getId()) {
            case R.id.btn_details:
                count = 1;
                detail_btn.setBackgroundColor(getResources().getColor(R.color.header_tab_bg));
                chat_btn.setBackgroundColor(getResources().getColor(R.color.white));
                attachment_btn.setBackgroundColor(getResources().getColor(R.color.white));
                detail_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                chat_btn.setTextColor(getResources().getColor(R.color.black));
                attachment_btn.setTextColor(getResources().getColor(R.color.black));

                //   Toast.makeText(getActivity(), "Details", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_chats:
                count = 2;
                chat_btn.setBackgroundColor(getResources().getColor(R.color.header_tab_bg));
                detail_btn.setBackgroundColor(getResources().getColor(R.color.white));
                attachment_btn.setBackgroundColor(getResources().getColor(R.color.white));
                chat_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                detail_btn.setTextColor(getResources().getColor(R.color.black));
                attachment_btn.setTextColor(getResources().getColor(R.color.black));
                // Toast.makeText(getActivity(), "Chat Window", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_attachments:
                count = 3;
                attachment_btn.setBackgroundColor(getResources().getColor(R.color.header_tab_bg));
                chat_btn.setBackgroundColor(getResources().getColor(R.color.white));
                detail_btn.setBackgroundColor(getResources().getColor(R.color.white));
                attachment_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                chat_btn.setTextColor(getResources().getColor(R.color.black));
                detail_btn.setTextColor(getResources().getColor(R.color.black));
                // Toast.makeText(getActivity(), "Chat Window", Toast.LENGTH_SHORT).show();
                break;
        }

        if(count == 1) {
            bundle = new Bundle();
            bundle.putInt("PATIENT_ID", Patient_ID);
            bundle.putInt("DOCREF_ID", DOCREF_ID);
            bundle.putString("PATIENT_VIEW", Patient_View);
            PatientsViewFragment homeFragment = new PatientsViewFragment();
            fragManager = getSupportFragmentManager();
            fragTransaction = fragManager.beginTransaction();
            homeFragment.setArguments(bundle);
            fragTransaction.replace(R.id.viewpager,homeFragment,"DETAILS");
            fragTransaction.commit();
        }
        else if(count == 2) {
            bundle = new Bundle();
            bundle.putInt("PATIENT_ID", Patient_ID);
            bundle.putInt("DOCREF_ID", DOCREF_ID);
            bundle.putString("PATIENT_VIEW", Patient_View);
            ChatWindowFragment chatFragment = new ChatWindowFragment();
            fragManager = getSupportFragmentManager();
            fragTransaction = fragManager.beginTransaction();
            chatFragment.setArguments(bundle);
            fragTransaction.replace(R.id.viewpager,chatFragment,"CHAT");
            fragTransaction.commit();
        }
        else if(count == 3) {
            bundle = new Bundle();
            bundle.putInt("PATIENT_ID", Patient_ID);
            bundle.putInt("DOCREF_ID", DOCREF_ID);
            bundle.putString("PATIENT_VIEW", Patient_View);
            PatientAttachments attachFragment = new PatientAttachments();
            fragManager = getSupportFragmentManager();
            fragTransaction = fragManager.beginTransaction();
            attachFragment.setArguments(bundle);
            fragTransaction.replace(R.id.viewpager,attachFragment,"ATTACH");
            fragTransaction.commit();
        }
    }

}
