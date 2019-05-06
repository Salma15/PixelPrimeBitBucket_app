package com.medisensehealth.fdccontributor.activities.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.LoginActivity;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;

/**
 * Created by HP on 26-07-2018.
 */

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE;
    CardView _text_diagnosis, _text_pharmacy, _text_hospital, _text_receptionist, _text_settings, _text_shareapp, _texr_changepassword,
                _text_emr_settings, _text_signout_settings, _text_profile_settings, _text_consultation_settings, _text_opticals;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);

            Log.d(Utils.TAG, " ************ SettingsActivity *********** ");
        }

        shareadPreferenceClass = new ShareadPreferenceClass(SettingsActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(SettingsActivity.this);


        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        Log.d(Utils.TAG , " *********** SettingsActivity ****************");
        Log.d(Utils.TAG +" loginType: ", Utils.USER_LOGIN_TYPE);
        Log.d(Utils.TAG +" UserId: ", String.valueOf(Utils.USER_LOGIN_ID));

        initializationViews();
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


    private void initializationViews() {

        _text_diagnosis = (CardView) findViewById(R.id.settings_diagnosis);
        _text_pharmacy = (CardView) findViewById(R.id.settings_pharmacy);
        _text_hospital = (CardView) findViewById(R.id.settings_hospital);
     //   _text_receptionist = (CardView) findViewById(R.id.settings_receptionist);
        _text_settings = (CardView) findViewById(R.id.settings_others);
        _text_shareapp = (CardView) findViewById(R.id.settings_shareapp);
        _texr_changepassword = (CardView) findViewById(R.id.settings_change_password);
        _text_emr_settings = (CardView) findViewById(R.id.settings_emr);
        _text_signout_settings = (CardView) findViewById(R.id.settings_signout);
        _text_profile_settings = (CardView) findViewById(R.id.settings_profile);
        _text_consultation_settings = (CardView) findViewById(R.id.settings_consultation_hours);
        _text_opticals = (CardView) findViewById(R.id.settings_opticals);

        _text_diagnosis.setOnClickListener(this);
        _text_pharmacy.setOnClickListener(this);
        _text_hospital.setOnClickListener(this);
      //  _text_receptionist.setOnClickListener(this);
        _text_settings.setOnClickListener(this);
        _text_shareapp.setOnClickListener(this);
        _texr_changepassword.setOnClickListener(this);
        _text_emr_settings.setOnClickListener(this);
        _text_signout_settings.setOnClickListener(this);
        _text_profile_settings.setOnClickListener(this);
        _text_consultation_settings.setOnClickListener(this);
        _text_opticals.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settings_shareapp:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Pixel Prime");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "I would like to invite you to try 'Pixel Prime' - a web & smartphone based application designed for Doctors. \nThis app is accessible through Android devices, as well as online. \n\nDownload the app using - Android: https://play.google.com/store/apps/details?id=com.medisensehealth.fdccontributor&hl=en");
                startActivity(Intent.createChooser(sharingIntent, "Share App via"));
                break;

            case R.id.settings_change_password:
                Intent i1 = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                i1.putExtra("title","Change Password");
                startActivity(i1);
                break;
            case R.id.settings_diagnosis:
                Intent i2 = new Intent(SettingsActivity.this, DiagnosisListActivity.class);
                i2.putExtra("title","Diagnosis Lists");
                startActivity(i2);
                break;
            case R.id.settings_pharmacy:
                Intent i3 = new Intent(SettingsActivity.this, PharmacyListActivity.class);
                i3.putExtra("title","Pharmacy Lists");
                startActivity(i3);
                break;
         /*   case R.id.settings_receptionist:
               *//* Intent i4 = new Intent(SettingsActivity.this, ReceptionistActivity.class);
                i4.putExtra("title","Add Receptionist");
                startActivity(i4);*//*
                break;*/
            case R.id.settings_others:
                Intent i5 = new Intent(SettingsActivity.this, OtherSettingsActivity.class);
                i5.putExtra("title","Other Settings");
                startActivity(i5);
                break;
            case R.id.settings_hospital:
                Intent i6 = new Intent(SettingsActivity.this, HospitalListActivity.class);
                i6.putExtra("title","Hospital List");
                startActivity(i6);
                break;
           /* case R.id.settings_emr:
                Intent i7 = new Intent(SettingsActivity.this, EMRSettingsActivity.class);
                i7.putExtra("title","EMR Settings");
                startActivity(i7);
                break;*/
            case R.id.settings_signout:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SettingsActivity.this, R.style.CustomDialog);
                alertDialogBuilder.setTitle("Sign Out");
                alertDialogBuilder
                        .setMessage("Are you sure want to Sign Out?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                signOutFromApp();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case R.id.settings_profile:
                InputMethodManager imm7 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm7.hideSoftInputFromWindow(_text_profile_settings.getWindowToken(), 0);
                Intent i7 = new Intent(SettingsActivity.this, ProfileActivity.class);
                i7.putExtra("title","My Profile");
                startActivity(i7);
                break;
            case R.id.settings_consultation_hours:
                InputMethodManager imm8 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm8.hideSoftInputFromWindow(_text_consultation_settings.getWindowToken(), 0);
                Intent i8 = new Intent(SettingsActivity.this, MySchedulesActivity.class);
                i8.putExtra("title","My Consulting Hours");
                startActivity(i8);
                break;
            case R.id.settings_opticals:
                InputMethodManager imm9 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm9.hideSoftInputFromWindow(_text_opticals.getWindowToken(), 0);
                Intent i9 = new Intent(SettingsActivity.this, OpticalsListActivity.class);
                i9.putExtra("title","Optical Centers");
                startActivity(i9);
                break;
        }
    }

    private void signOutFromApp() {

        if (sharedPreferences != null) {
            shareadPreferenceClass.clearData();
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }
}
