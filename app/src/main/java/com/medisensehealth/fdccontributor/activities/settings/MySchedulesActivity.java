package com.medisensehealth.fdccontributor.activities.settings;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;
import com.medisensehealth.fdccontributor.DataModel.Holidays;
import com.medisensehealth.fdccontributor.DataModel.HospitalList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.DashboardActivity;
import com.medisensehealth.fdccontributor.adapter.settings.HolidaysAdapter;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.parser.JSONParser;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by HP on 11-04-2018.
 */

public class MySchedulesActivity extends AppCompatActivity implements View.OnClickListener {

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID, HOSPITAL_ID;
    String USER_NAME, USER_LOGIN_TYPE, DOC_HOSIPTAL_LIST;
    Button appointment_btn, holiday_btn;
    int count = 0;
    LinearLayout appointment_layout, holidays_layout;
    ArrayList<HospitalList> hospitalDocArraylist;

    public ArrayList<String> GET_TIME_ARRAY = new ArrayList<String>();
    public ArrayList<String> GET_DAY_ARRAY = new ArrayList<String>();

    CheckBox check1_1, check1_2, check1_3, check1_4, check1_5, check1_6, check1_7;
    CheckBox check2_1, check2_2, check2_3, check2_4, check2_5, check2_6, check2_7;
    CheckBox check3_1, check3_2, check3_3, check3_4, check3_5, check3_6, check3_7;
    CheckBox check4_1, check4_2, check4_3, check4_4, check4_5, check4_6, check4_7;
    CheckBox check5_1, check5_2, check5_3, check5_4, check5_5, check5_6, check5_7;
    CheckBox check6_1, check6_2, check6_3, check6_4, check6_5, check6_6, check6_7;
    CheckBox check7_1, check7_2, check7_3, check7_4, check7_5, check7_6, check7_7;
    CheckBox check8_1, check8_2, check8_3, check8_4, check8_5, check8_6, check8_7;
    CheckBox check9_1, check9_2, check9_3, check9_4, check9_5, check9_6, check9_7;
    CheckBox check10_1, check10_2, check10_3, check10_4, check10_5, check10_6, check10_7;
    CheckBox check11_1, check11_2, check11_3, check11_4, check11_5, check11_6, check11_7;
    CheckBox check12_1, check12_2, check12_3, check12_4, check12_5, check12_6, check12_7;
    CheckBox check13_1, check13_2, check13_3, check13_4, check13_5, check13_6, check13_7;

    Spinner hospital_spinner;
    ImageView add_hospital_btn, send_num_patients;
    String GET_COUNTRY_CODE, GET_COUNTRY_NAME, GET_COUNTRY_NAME_CODE, GET_STATE;
    Spinner state_spinner;
    CustomEditText _edt_num_patients_hour;

    // Holidays Lists
    Calendar myCalendar;
    LinearLayout holiday_set_date;
    CustomTextView holiday_date;
    CustomEditText holiday_reason;
    ImageView holiday_submit;
    public List<Holidays> HOLIDAYS_ARRAY = new ArrayList<>();
    RecyclerView holiday_recyclerView;
    private HolidaysAdapter mHolidayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedules);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle b = getIntent().getExtras();
        if( b != null) {
            String title = b.getString("title");
            setTitle(title);

        }

        shareadPreferenceClass = new ShareadPreferenceClass(MySchedulesActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(MySchedulesActivity.this);
        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");

            Log.d(Utils.TAG , "******************* My Schedules *************");
            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        Log.d(Utils.TAG+"USER_ID: ", String.valueOf(USER_ID));
        Log.d(Utils.TAG+"LOGIN_TYPE: ", USER_LOGIN_TYPE);

        initializationViews(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
              //  finish();
                Intent intent_Doc = new Intent(MySchedulesActivity.this, DashboardActivity.class);
                intent_Doc.putExtra("LOGIN_TYPE", USER_LOGIN_TYPE);
                intent_Doc.putExtra("USER_ID", USER_ID);
                intent_Doc.putExtra("ENTRY_TYPE", "NORMAL");
                startActivity(intent_Doc);
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent_Doc = new Intent(MySchedulesActivity.this, DashboardActivity.class);
        intent_Doc.putExtra("LOGIN_TYPE", USER_LOGIN_TYPE);
        intent_Doc.putExtra("USER_ID", USER_ID);
        intent_Doc.putExtra("ENTRY_TYPE", "NORMAL");
        startActivity(intent_Doc);
        finish();
    }

    private void initializationViews(Bundle savedInstanceState) {
        GET_TIME_ARRAY = new ArrayList<String>();
        GET_DAY_ARRAY = new ArrayList<String>();
        hospitalDocArraylist = new ArrayList<>();
        HOSPITAL_ID = 0;
        myCalendar = Calendar.getInstance();
        HOLIDAYS_ARRAY = new ArrayList<>();

        Gson gson = new Gson();
        if (!DOC_HOSIPTAL_LIST.equals("")) {
            Log.d(Utils.TAG, "DOC_HOSIPTAL_LIST NOT EMPTY ");
            hospitalDocArraylist = gson.fromJson(DOC_HOSIPTAL_LIST, new TypeToken<List<HospitalList>>() {
            }.getType());
            if(hospitalDocArraylist.size() > 0 ) {
                Log.d(Utils.TAG, "hospitalDocArraylist size: " + hospitalDocArraylist.size());
            }
        }

        appointment_btn = (Button)findViewById(R.id.btn_appointment);
        holiday_btn = (Button)findViewById(R.id.btn_holidays);
        appointment_btn.setOnClickListener(this);
        holiday_btn.setOnClickListener(this);
        appointment_layout = (LinearLayout)findViewById(R.id.set_appintment_layout);
        holidays_layout = (LinearLayout)findViewById(R.id.set_holiday_layout);
        appointment_layout.setVisibility(View.VISIBLE);
        holidays_layout.setVisibility(View.GONE);

        hospital_spinner = (Spinner) findViewById(R.id.hospital_spinner);
        add_hospital_btn = (ImageView) findViewById(R.id.hospital_add);
        add_hospital_btn.setOnClickListener(this);
        _edt_num_patients_hour = (CustomEditText)  findViewById(R.id.hospital_numpatients);
        send_num_patients = (ImageView)  findViewById(R.id.hospital_send_numpatients);
        send_num_patients.setOnClickListener(this);

        check1_1 = (CheckBox) findViewById(R.id.profile_r1_c1);
        check1_2 = (CheckBox) findViewById(R.id.profile_r1_c2);
        check1_3 = (CheckBox) findViewById(R.id.profile_r1_c3);
        check1_4 = (CheckBox) findViewById(R.id.profile_r1_c4);
        check1_5 = (CheckBox) findViewById(R.id.profile_r1_c5);
        check1_6 = (CheckBox) findViewById(R.id.profile_r1_c6);
        check1_7 = (CheckBox) findViewById(R.id.profile_r1_c7);
        check2_1 = (CheckBox) findViewById(R.id.profile_r2_c1);
        check2_2 = (CheckBox) findViewById(R.id.profile_r2_c2);
        check2_3 = (CheckBox) findViewById(R.id.profile_r2_c3);
        check2_4 = (CheckBox) findViewById(R.id.profile_r2_c4);
        check2_5 = (CheckBox) findViewById(R.id.profile_r2_c5);
        check2_6 = (CheckBox) findViewById(R.id.profile_r2_c6);
        check2_7 = (CheckBox) findViewById(R.id.profile_r2_c7);
        check3_1 = (CheckBox) findViewById(R.id.profile_r3_c1);
        check3_2 = (CheckBox) findViewById(R.id.profile_r3_c2);
        check3_3 = (CheckBox) findViewById(R.id.profile_r3_c3);
        check3_4 = (CheckBox) findViewById(R.id.profile_r3_c4);
        check3_5 = (CheckBox) findViewById(R.id.profile_r3_c5);
        check3_6 = (CheckBox) findViewById(R.id.profile_r3_c6);
        check3_7 = (CheckBox) findViewById(R.id.profile_r3_c7);
        check4_1 = (CheckBox) findViewById(R.id.profile_r4_c1);
        check4_2 = (CheckBox) findViewById(R.id.profile_r4_c2);
        check4_3 = (CheckBox) findViewById(R.id.profile_r4_c3);
        check4_4 = (CheckBox) findViewById(R.id.profile_r4_c4);
        check4_5 = (CheckBox) findViewById(R.id.profile_r4_c5);
        check4_6 = (CheckBox) findViewById(R.id.profile_r4_c6);
        check4_7 = (CheckBox) findViewById(R.id.profile_r4_c7);
        check5_1 = (CheckBox) findViewById(R.id.profile_r5_c1);
        check5_2 = (CheckBox) findViewById(R.id.profile_r5_c2);
        check5_3 = (CheckBox) findViewById(R.id.profile_r5_c3);
        check5_4 = (CheckBox) findViewById(R.id.profile_r5_c4);
        check5_5 = (CheckBox) findViewById(R.id.profile_r5_c5);
        check5_6 = (CheckBox) findViewById(R.id.profile_r5_c6);
        check5_7 = (CheckBox) findViewById(R.id.profile_r5_c7);
        check6_1 = (CheckBox) findViewById(R.id.profile_r6_c1);
        check6_2 = (CheckBox) findViewById(R.id.profile_r6_c2);
        check6_3 = (CheckBox) findViewById(R.id.profile_r6_c3);
        check6_4 = (CheckBox) findViewById(R.id.profile_r6_c4);
        check6_5 = (CheckBox) findViewById(R.id.profile_r6_c5);
        check6_6 = (CheckBox) findViewById(R.id.profile_r6_c6);
        check6_7 = (CheckBox) findViewById(R.id.profile_r6_c7);
        check7_1 = (CheckBox) findViewById(R.id.profile_r7_c1);
        check7_2 = (CheckBox) findViewById(R.id.profile_r7_c2);
        check7_3 = (CheckBox) findViewById(R.id.profile_r7_c3);
        check7_4 = (CheckBox) findViewById(R.id.profile_r7_c4);
        check7_5 = (CheckBox) findViewById(R.id.profile_r7_c5);
        check7_6 = (CheckBox) findViewById(R.id.profile_r7_c6);
        check7_7 = (CheckBox) findViewById(R.id.profile_r7_c7);
        check8_1 = (CheckBox) findViewById(R.id.profile_r8_c1);
        check8_2 = (CheckBox) findViewById(R.id.profile_r8_c2);
        check8_3 = (CheckBox) findViewById(R.id.profile_r8_c3);
        check8_4 = (CheckBox) findViewById(R.id.profile_r8_c4);
        check8_5 = (CheckBox) findViewById(R.id.profile_r8_c5);
        check8_6 = (CheckBox) findViewById(R.id.profile_r8_c6);
        check8_7 = (CheckBox) findViewById(R.id.profile_r8_c7);
        check9_1 = (CheckBox) findViewById(R.id.profile_r9_c1);
        check9_2 = (CheckBox) findViewById(R.id.profile_r9_c2);
        check9_3 = (CheckBox) findViewById(R.id.profile_r9_c3);
        check9_4 = (CheckBox) findViewById(R.id.profile_r9_c4);
        check9_5 = (CheckBox) findViewById(R.id.profile_r9_c5);
        check9_6 = (CheckBox) findViewById(R.id.profile_r9_c6);
        check9_7 = (CheckBox) findViewById(R.id.profile_r9_c7);
        check10_1 = (CheckBox) findViewById(R.id.profile_r10_c1);
        check10_2 = (CheckBox) findViewById(R.id.profile_r10_c2);
        check10_3 = (CheckBox) findViewById(R.id.profile_r10_c3);
        check10_4 = (CheckBox) findViewById(R.id.profile_r10_c4);
        check10_5 = (CheckBox) findViewById(R.id.profile_r10_c5);
        check10_6 = (CheckBox) findViewById(R.id.profile_r10_c6);
        check10_7 = (CheckBox) findViewById(R.id.profile_r10_c7);
        check11_1 = (CheckBox) findViewById(R.id.profile_r11_c1);
        check11_2 = (CheckBox) findViewById(R.id.profile_r11_c2);
        check11_3 = (CheckBox) findViewById(R.id.profile_r11_c3);
        check11_4 = (CheckBox) findViewById(R.id.profile_r11_c4);
        check11_5 = (CheckBox) findViewById(R.id.profile_r11_c5);
        check11_6 = (CheckBox) findViewById(R.id.profile_r11_c6);
        check11_7 = (CheckBox) findViewById(R.id.profile_r11_c7);
        check12_1 = (CheckBox) findViewById(R.id.profile_r12_c1);
        check12_2 = (CheckBox) findViewById(R.id.profile_r12_c2);
        check12_3 = (CheckBox) findViewById(R.id.profile_r12_c3);
        check12_4 = (CheckBox) findViewById(R.id.profile_r12_c4);
        check12_5 = (CheckBox) findViewById(R.id.profile_r12_c5);
        check12_6 = (CheckBox) findViewById(R.id.profile_r12_c6);
        check12_7 = (CheckBox) findViewById(R.id.profile_r12_c7);
        check13_1 = (CheckBox) findViewById(R.id.profile_r13_c1);
        check13_2 = (CheckBox) findViewById(R.id.profile_r13_c2);
        check13_3 = (CheckBox) findViewById(R.id.profile_r13_c3);
        check13_4 = (CheckBox) findViewById(R.id.profile_r13_c4);
        check13_5 = (CheckBox) findViewById(R.id.profile_r13_c5);
        check13_6 = (CheckBox) findViewById(R.id.profile_r13_c6);
        check13_7 = (CheckBox) findViewById(R.id.profile_r13_c7);



        ArrayList<String> hospitalArray = new ArrayList<String>();
        for (int i = 0; i < hospitalDocArraylist.size(); i++) {
            hospitalArray.add(hospitalDocArraylist.get(i).getHospitalName());
        }

        ArrayAdapter<String> hospAdapter = new ArrayAdapter<String>(MySchedulesActivity.this, R.layout.spinner_text, hospitalArray );
        hospAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        hospital_spinner.setAdapter(hospAdapter);

        hospital_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.d(Utils.TAG, "hospital_name: "+ (String) parent.getItemAtPosition(position));
                String hosp_name = (String) parent.getItemAtPosition(position);
                for(int j=0;j<hospitalDocArraylist.size();j++){
                    if(hosp_name.trim().equals(hospitalDocArraylist.get(j).getHospitalName().trim())) {
                        HOSPITAL_ID = hospitalDocArraylist.get(j).getHospitalId();
                        Log.d(Utils.TAG, "HOSPITAL_ID: "+ HOSPITAL_ID);
                    }
                }

                getProfileInformation();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                HOSPITAL_ID = 0;
            }
        });


      //  getProfileInformation();
        loadScheduleExists();


        // ***************** Holidays Lists **************************
        holiday_set_date = (LinearLayout) findViewById(R.id.holiday_set_date);
        holiday_date = (CustomTextView) findViewById(R.id.holiday_date);
        holiday_reason = (CustomEditText) findViewById(R.id.holiday_reason);
        holiday_submit  = (ImageView) findViewById(R.id.holiday_submit);
        holiday_submit.setOnClickListener(this);

        holiday_recyclerView = (RecyclerView) findViewById(R.id.set_holiday_recyclerview);
        mHolidayAdapter = new HolidaysAdapter(MySchedulesActivity.this, HOLIDAYS_ARRAY);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        holiday_recyclerView.setLayoutManager(mLayoutManager);
        holiday_recyclerView.setItemAnimator(new DefaultItemAnimator());
        holiday_recyclerView.setAdapter(mHolidayAdapter);

        holiday_set_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holiday_date.setText("");
                new DatePickerDialog(MySchedulesActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Calendar dareSelected = Calendar.getInstance();
            dareSelected.set(year, monthOfYear, dayOfMonth);
            Calendar currentDate = Calendar.getInstance();

            int a = dareSelected.compareTo(currentDate);
            Log.d(Utils.TAG, " a: " + String.valueOf(a));   // -1: past, 0: current, 1: future
            if(a<0){
                Toast.makeText(MySchedulesActivity.this, "Invalid Date", Toast.LENGTH_SHORT).show();
            }else{
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                holiday_date.setText(sdf.format(dareSelected.getTime()));

                String outFormat = "yyyy/MM/dd";
                SimpleDateFormat sdf_out = new SimpleDateFormat(outFormat, Locale.US);
             //   get_appoint_date = sdf_out.format(dareSelected.getTime());
            }
        }
    };

    @Override
    public void onClick(View view) {
        Bundle bundle;
        switch(view.getId()) {
            case R.id.btn_appointment:
                InputMethodManager imm4 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm4.hideSoftInputFromWindow(appointment_btn.getWindowToken(), 0);

                appointment_btn.setBackgroundColor(getResources().getColor(R.color.contrast));
                holiday_btn.setBackgroundColor(getResources().getColor(R.color.light_grey));
                appointment_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                holiday_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                appointment_layout.setVisibility(View.VISIBLE);
                holidays_layout.setVisibility(View.GONE);

                break;
            case R.id.btn_holidays:
                InputMethodManager imm5 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm5.hideSoftInputFromWindow(holiday_btn.getWindowToken(), 0);

                appointment_btn.setBackgroundColor(getResources().getColor(R.color.light_grey));
                holiday_btn.setBackgroundColor(getResources().getColor(R.color.contrast));
                appointment_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                holiday_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                appointment_layout.setVisibility(View.GONE);
                holidays_layout.setVisibility(View.VISIBLE);

                break;
            case R.id.hospital_add:
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(add_hospital_btn.getWindowToken(), 0);
               // Toast.makeText(MySchedulesActivity.this, " Add Hospital", Toast.LENGTH_SHORT).show();
                customAddHospital();
                break;
            case R.id.hospital_send_numpatients:
                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(send_num_patients.getWindowToken(), 0);
                if(_edt_num_patients_hour.getText().toString().equals("")) {
                    Toast.makeText(MySchedulesActivity.this, "Enter No. of patients per hour !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    new setJsonNumberPatients(_edt_num_patients_hour.getText().toString().trim()).execute();
                }
                break;
            case R.id.holiday_submit:
                InputMethodManager imm3 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(holiday_submit.getWindowToken(), 0);
                if(holiday_date.getText().toString().equals("")) {
                    Toast.makeText(MySchedulesActivity.this, "Choose Holiday date !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    new setJsonSetHolidays(holiday_date.getText().toString().trim(),holiday_reason.getText().toString().trim()).execute();
                }
                break;
        }
    }

    private void loadScheduleExists() {

        check1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    Log.d(Utils.TAG, " check1_1 CHECKED ");
                    new setJsonAppintment("1", "1", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    Log.d(Utils.TAG, " check11_1 NOT CHECKED ");
                    new setJsonAppintment("1", "1", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });

        check1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("1", "2", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("1", "2", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });

        check1_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("1", "3", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("1", "3", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });

        check1_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("1", "4", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("1", "4", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });

        check1_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("1", "5", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("1", "5", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });

        check1_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("1", "6", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("1", "6", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });

        check1_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("1", "7", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("1", "7", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });

        check2_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("2", "1", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("2", "1", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("2", "2", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("2", "2", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check2_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("2", "3", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("2", "3", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check2_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("2", "4", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("2", "4", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check2_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("2", "5", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("2", "5", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check2_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("2", "6", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("2", "6", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check2_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("2", "7", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("2", "7", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });

        check3_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("3", "1", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("3", "1", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check3_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("3", "2", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("3", "2", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check3_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("3", "3", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("3", "3", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check3_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("3", "4", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("3", "4", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check3_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("3", "5", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("3", "5", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check3_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("3", "6", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("3", "6", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check3_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("3", "7", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("3", "7", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });

        check4_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("4", "1", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("4", "1", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check4_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("4", "2", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("4", "2", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check4_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("4", "3", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("4", "3", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check4_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("4", "4", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("4", "4", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check4_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("4", "5", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("4", "5", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check4_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("4", "6", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("4", "6", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check4_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("4", "7", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("4", "7", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });

        check5_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("5", "1", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("5", "1", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check5_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("5", "2", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("5", "2", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check5_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("5", "3", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("5", "3", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check5_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("5", "4", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("5", "4", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check5_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("5", "5", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("5", "5", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check5_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("5", "6", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("5", "6", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check5_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("5", "7", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("5", "7", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });

        check6_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("6", "1", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("6", "1", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check6_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("6", "2", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("6", "2", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check6_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("6", "3", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("6", "3", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check6_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("6", "4", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("6", "4", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check6_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("6", "5", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("6", "5", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check6_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("6", "6", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("6", "6", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check6_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("6", "7", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("6", "7", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });

        check7_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("7", "1", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("7", "1", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check7_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("7", "2", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("7", "2", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check7_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("7", "3", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("7", "3", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check7_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("7", "4", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("7", "4", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check7_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("7", "5", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("7", "5", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check7_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("7", "6", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("7", "6", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check7_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("7", "7", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("7", "7", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });

        check8_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("8", "1", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("8", "1", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check8_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("8", "2", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("8", "2", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check8_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("8", "3", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("8", "3", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check8_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("8", "4", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("8", "4", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check8_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("8", "5", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("8", "5", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check8_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("8", "6", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("8", "6", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check8_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("8", "7", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("8", "7", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });

        check9_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("9", "1", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("9", "1", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check9_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("9", "2", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("9", "2", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check9_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("9", "3", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("9", "3", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check9_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("9", "4", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("9", "4", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check9_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("9", "5", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("9", "5", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check9_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("9", "6", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("9", "6", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check9_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("9", "7", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("9", "7", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });

        check10_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("10", "1", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("10", "1", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check10_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("10", "2", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("10", "2", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check10_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("10", "3", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("10", "3", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check10_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("10", "4", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("10", "4", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check10_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("10", "5", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("10", "5", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check10_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("10", "6", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("10", "6", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check10_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("10", "7", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("10", "7", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });

        check11_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("11", "1", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("11", "1", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check11_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("11", "2", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("11", "2", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check11_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("11", "3", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("11", "3", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check11_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("11", "4", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("11", "4", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check11_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("11", "5", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("11", "5", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check11_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("11", "6", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("11", "6", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check11_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("11", "7", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("11", "7", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });

        check12_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("12", "1", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("12", "1", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check12_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("12", "2", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("12", "2", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check12_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("12", "3", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("12", "3", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check12_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("12", "4", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("12", "4", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check12_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("12", "5", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("12", "5", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check12_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("12", "6", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("12", "6", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check12_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("12", "7", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("12", "7", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });

        check13_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("13", "1", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("13", "1", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check13_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("13", "2", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("13", "2", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check13_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("13", "3", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("13", "3", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check13_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("13", "4", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("13", "4", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check13_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("13", "5", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("13", "5", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check13_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("13", "6", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("13", "6", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
        check13_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    new setJsonAppintment("13", "7", "1", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }
                else {
                    new setJsonAppintment("13", "7", "0", USER_ID).execute();   // Time ID, Day ID, Set-1(Not Set-0), User ID
                }

            }
        });
    }

    private void getProfileInformation() {

        final ProgressDialog progressDialog = new ProgressDialog(MySchedulesActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading appointments...");
        progressDialog.show();

        check1_1.setChecked(false);
        check1_2.setChecked(false);
        check1_3.setChecked(false);
        check1_4.setChecked(false);
        check1_5.setChecked(false);
        check1_6.setChecked(false);
        check1_7.setChecked(false);
        check2_1.setChecked(false);
        check2_2.setChecked(false);
        check2_3.setChecked(false);
        check2_4.setChecked(false);
        check2_5.setChecked(false);
        check2_6.setChecked(false);
        check2_7.setChecked(false);
        check3_1.setChecked(false);
        check3_2.setChecked(false);
        check3_3.setChecked(false);
        check3_4.setChecked(false);
        check3_5.setChecked(false);
        check3_6.setChecked(false);
        check3_7.setChecked(false);
        check4_1.setChecked(false);
        check4_2.setChecked(false);
        check4_3.setChecked(false);
        check4_4.setChecked(false);
        check4_5.setChecked(false);
        check4_6.setChecked(false);
        check4_7.setChecked(false);
        check5_1.setChecked(false);
        check5_2.setChecked(false);
        check5_3.setChecked(false);
        check5_4.setChecked(false);
        check5_5.setChecked(false);
        check5_6.setChecked(false);
        check5_7.setChecked(false);
        check6_1.setChecked(false);
        check6_2.setChecked(false);
        check6_3.setChecked(false);
        check6_4.setChecked(false);
        check6_5.setChecked(false);
        check6_6.setChecked(false);
        check6_7.setChecked(false);
        check7_1.setChecked(false);
        check7_2.setChecked(false);
        check7_3.setChecked(false);
        check7_4.setChecked(false);
        check7_5.setChecked(false);
        check7_6.setChecked(false);
        check7_7.setChecked(false);
        check8_1.setChecked(false);
        check8_2.setChecked(false);
        check8_3.setChecked(false);
        check8_4.setChecked(false);
        check8_5.setChecked(false);
        check8_6.setChecked(false);
        check8_7.setChecked(false);
        check9_1.setChecked(false);
        check9_2.setChecked(false);
        check9_3.setChecked(false);
        check9_4.setChecked(false);
        check9_5.setChecked(false);
        check9_6.setChecked(false);
        check9_7.setChecked(false);
        check10_1.setChecked(false);
        check10_2.setChecked(false);
        check10_3.setChecked(false);
        check10_4.setChecked(false);
        check10_5.setChecked(false);
        check10_6.setChecked(false);
        check10_7.setChecked(false);
        check11_1.setChecked(false);
        check11_2.setChecked(false);
        check11_3.setChecked(false);
        check11_4.setChecked(false);
        check11_5.setChecked(false);
        check11_6.setChecked(false);
        check11_7.setChecked(false);
        check12_1.setChecked(false);
        check12_2.setChecked(false);
        check12_3.setChecked(false);
        check12_4.setChecked(false);
        check12_5.setChecked(false);
        check12_6.setChecked(false);
        check12_7.setChecked(false);
        check13_1.setChecked(false);
        check13_2.setChecked(false);
        check13_3.setChecked(false);
        check13_4.setChecked(false);
        check13_5.setChecked(false);
        check13_6.setChecked(false);
        check13_7.setChecked(false);
        _edt_num_patients_hour.setText("");
        holiday_date.setText("");
        holiday_reason.setText("");
        HOLIDAYS_ARRAY = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_APPOINTMENT_SLOT_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "result: "+ response.toString());
                        if (response != null) try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray JASlots = jsonObject.getJSONArray("slots_details");
                            for (int i = 0; i < JASlots.length(); i++) {
                                JSONObject JOSlots = JASlots.getJSONObject(i);
                                _edt_num_patients_hour.setText(JOSlots.getString("num_patient_hour"));
                            }

                            JSONArray JAHolidays = jsonObject.getJSONArray("holiday_details");
                            if(JAHolidays.length() > 0 ) {
                                for (int k = 0; k < JAHolidays.length(); k++) {
                                    HOLIDAYS_ARRAY.add(new Holidays(JAHolidays.getJSONObject(k).getInt("holiday_id"),
                                            JAHolidays.getJSONObject(k).getInt("doc_id"),JAHolidays.getJSONObject(k).getInt("doc_type"),
                                            JAHolidays.getJSONObject(k).getInt("hosp_id"),JAHolidays.getJSONObject(k).getString("holiday_date"),
                                            JAHolidays.getJSONObject(k).getString("reason")));
                                }
                                mHolidayAdapter = new HolidaysAdapter(MySchedulesActivity.this, HOLIDAYS_ARRAY);
                                holiday_recyclerView.setAdapter(mHolidayAdapter);
                                mHolidayAdapter.notifyDataSetChanged();
                            }


                            JSONArray JASchedule = jsonObject.getJSONArray("schedules");
                            for (int j = 0; j < JASchedule.length(); j++) {
                                JSONObject JOSchedule = JASchedule.getJSONObject(j);
                                if ((JOSchedule.getString("time_id").equals("1")) && (JOSchedule.getString("day_id").equals("1"))) {
                                    check1_1.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("1")) && (JOSchedule.getString("day_id").equals("2"))) {
                                    check1_2.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("1")) && (JOSchedule.getString("day_id").equals("3"))) {
                                    check1_3.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("1")) && (JOSchedule.getString("day_id").equals("4"))) {
                                    check1_4.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("1")) && (JOSchedule.getString("day_id").equals("5"))) {
                                    check1_5.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("1")) && (JOSchedule.getString("day_id").equals("6"))) {
                                    check1_6.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("1")) && (JOSchedule.getString("day_id").equals("7"))) {
                                    check1_7.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("2")) && (JOSchedule.getString("day_id").equals("1"))) {
                                    check2_1.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("2")) && (JOSchedule.getString("day_id").equals("2"))) {
                                    check2_2.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("2")) && (JOSchedule.getString("day_id").equals("3"))) {
                                    check2_3.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("2")) && (JOSchedule.getString("day_id").equals("4"))) {
                                    check2_4.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("2")) && (JOSchedule.getString("day_id").equals("5"))) {
                                    check2_5.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("2")) && (JOSchedule.getString("day_id").equals("6"))) {
                                    check2_6.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("2")) && (JOSchedule.getString("day_id").equals("7"))) {
                                    check2_7.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("3")) && (JOSchedule.getString("day_id").equals("1"))) {
                                    check3_1.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("3")) && (JOSchedule.getString("day_id").equals("2"))) {
                                    check3_2.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("3")) && (JOSchedule.getString("day_id").equals("3"))) {
                                    check3_3.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("3")) && (JOSchedule.getString("day_id").equals("4"))) {
                                    check3_4.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("3")) && (JOSchedule.getString("day_id").equals("5"))) {
                                    check3_5.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("3")) && (JOSchedule.getString("day_id").equals("6"))) {
                                    check3_6.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("3")) && (JOSchedule.getString("day_id").equals("7"))) {
                                    check3_7.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("4")) && (JOSchedule.getString("day_id").equals("1"))) {
                                    check4_1.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("4")) && (JOSchedule.getString("day_id").equals("2"))) {
                                    check4_2.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("4")) && (JOSchedule.getString("day_id").equals("3"))) {
                                    check4_3.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("4")) && (JOSchedule.getString("day_id").equals("4"))) {
                                    check4_4.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("4")) && (JOSchedule.getString("day_id").equals("5"))) {
                                    check4_5.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("4")) && (JOSchedule.getString("day_id").equals("6"))) {
                                    check4_6.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("4")) && (JOSchedule.getString("day_id").equals("7"))) {
                                    check4_7.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("5")) && (JOSchedule.getString("day_id").equals("1"))) {
                                    check5_1.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("5")) && (JOSchedule.getString("day_id").equals("2"))) {
                                    check5_2.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("5")) && (JOSchedule.getString("day_id").equals("3"))) {
                                    check5_3.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("5")) && (JOSchedule.getString("day_id").equals("4"))) {
                                    check5_4.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("5")) && (JOSchedule.getString("day_id").equals("5"))) {
                                    check5_5.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("5")) && (JOSchedule.getString("day_id").equals("6"))) {
                                    check5_6.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("5")) && (JOSchedule.getString("day_id").equals("7"))) {
                                    check5_7.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("6")) && (JOSchedule.getString("day_id").equals("1"))) {
                                    check6_1.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("6")) && (JOSchedule.getString("day_id").equals("2"))) {
                                    check6_2.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("6")) && (JOSchedule.getString("day_id").equals("3"))) {
                                    check6_3.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("6")) && (JOSchedule.getString("day_id").equals("4"))) {
                                    check6_4.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("6")) && (JOSchedule.getString("day_id").equals("5"))) {
                                    check6_5.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("6")) && (JOSchedule.getString("day_id").equals("6"))) {
                                    check6_6.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("6")) && (JOSchedule.getString("day_id").equals("7"))) {
                                    check6_7.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("7")) && (JOSchedule.getString("day_id").equals("1"))) {
                                    check7_1.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("7")) && (JOSchedule.getString("day_id").equals("2"))) {
                                    check7_2.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("7")) && (JOSchedule.getString("day_id").equals("3"))) {
                                    check7_3.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("7")) && (JOSchedule.getString("day_id").equals("4"))) {
                                    check7_4.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("7")) && (JOSchedule.getString("day_id").equals("5"))) {
                                    check7_5.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("7")) && (JOSchedule.getString("day_id").equals("6"))) {
                                    check7_6.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("7")) && (JOSchedule.getString("day_id").equals("7"))) {
                                    check7_7.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("8")) && (JOSchedule.getString("day_id").equals("1"))) {
                                    check8_1.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("8")) && (JOSchedule.getString("day_id").equals("2"))) {
                                    check8_2.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("8")) && (JOSchedule.getString("day_id").equals("3"))) {
                                    check8_3.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("8")) && (JOSchedule.getString("day_id").equals("4"))) {
                                    check8_4.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("8")) && (JOSchedule.getString("day_id").equals("5"))) {
                                    check8_5.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("8")) && (JOSchedule.getString("day_id").equals("6"))) {
                                    check8_6.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("8")) && (JOSchedule.getString("day_id").equals("7"))) {
                                    check8_7.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("9")) && (JOSchedule.getString("day_id").equals("1"))) {
                                    check9_1.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("9")) && (JOSchedule.getString("day_id").equals("2"))) {
                                    check9_2.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("9")) && (JOSchedule.getString("day_id").equals("3"))) {
                                    check9_3.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("9")) && (JOSchedule.getString("day_id").equals("4"))) {
                                    check9_4.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("9")) && (JOSchedule.getString("day_id").equals("5"))) {
                                    check9_5.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("9")) && (JOSchedule.getString("day_id").equals("6"))) {
                                    check9_6.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("9")) && (JOSchedule.getString("day_id").equals("7"))) {
                                    check9_7.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("10")) && (JOSchedule.getString("day_id").equals("1"))) {
                                    check10_1.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("10")) && (JOSchedule.getString("day_id").equals("2"))) {
                                    check10_2.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("10")) && (JOSchedule.getString("day_id").equals("3"))) {
                                    check10_3.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("10")) && (JOSchedule.getString("day_id").equals("4"))) {
                                    check10_4.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("10")) && (JOSchedule.getString("day_id").equals("5"))) {
                                    check10_5.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("10")) && (JOSchedule.getString("day_id").equals("6"))) {
                                    check10_6.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("10")) && (JOSchedule.getString("day_id").equals("7"))) {
                                    check10_7.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("11")) && (JOSchedule.getString("day_id").equals("1"))) {
                                    check11_1.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("11")) && (JOSchedule.getString("day_id").equals("2"))) {
                                    check11_2.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("11")) && (JOSchedule.getString("day_id").equals("3"))) {
                                    check11_3.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("11")) && (JOSchedule.getString("day_id").equals("4"))) {
                                    check11_4.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("11")) && (JOSchedule.getString("day_id").equals("5"))) {
                                    check11_5.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("11")) && (JOSchedule.getString("day_id").equals("6"))) {
                                    check11_6.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("11")) && (JOSchedule.getString("day_id").equals("7"))) {
                                    check11_7.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("12")) && (JOSchedule.getString("day_id").equals("1"))) {
                                    check12_1.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("12")) && (JOSchedule.getString("day_id").equals("2"))) {
                                    check12_2.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("12")) && (JOSchedule.getString("day_id").equals("3"))) {
                                    check12_3.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("12")) && (JOSchedule.getString("day_id").equals("4"))) {
                                    check12_4.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("12")) && (JOSchedule.getString("day_id").equals("5"))) {
                                    check12_5.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("12")) && (JOSchedule.getString("day_id").equals("6"))) {
                                    check12_6.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("12")) && (JOSchedule.getString("day_id").equals("7"))) {
                                    check12_7.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("13")) && (JOSchedule.getString("day_id").equals("1"))) {
                                    check13_1.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("13")) && (JOSchedule.getString("day_id").equals("2"))) {
                                    check13_2.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("13")) && (JOSchedule.getString("day_id").equals("3"))) {
                                    check13_3.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("13")) && (JOSchedule.getString("day_id").equals("4"))) {
                                    check13_4.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("13")) && (JOSchedule.getString("day_id").equals("5"))) {
                                    check13_5.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("13")) && (JOSchedule.getString("day_id").equals("6"))) {
                                    check13_6.setChecked(true);
                                } else if ((JOSchedule.getString("time_id").equals("13")) && (JOSchedule.getString("day_id").equals("7"))) {
                                    check13_7.setChecked(true);
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        AppUtils.showCustomAlertMessage(MySchedulesActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                        Log.d(Utils.TAG+"ERR",error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(Utils.USER_LOGIN_ID));
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                map.put(APIClass.KEY_HOSPITAL_ID, String.valueOf(HOSPITAL_ID));
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(MySchedulesActivity.this).
                getRequestQueue();
        AppController.getInstance(MySchedulesActivity.this).addToRequestQueue(stringRequest);

    }

    public class setJsonAppintment extends AsyncTask<Void, Void, Void> {
        String  timeID, dayID, setValue;
        int userID;

        public setJsonAppintment(String timeID,String dayID, String setValue, int userID){
            this.timeID = timeID;
            this.dayID = dayID;
            this.setValue = setValue;
            this.userID = userID;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(Utils.TAG, "timeID: " + String.valueOf(timeID));
            Log.d(Utils.TAG, "dayID: " + String.valueOf(dayID));
            Log.d(Utils.TAG, "setValue: " + String.valueOf(setValue));
            Log.d(Utils.TAG, "userID: " + String.valueOf(userID));
            Log.d(Utils.TAG, "HOSPITAL_ID: " + String.valueOf(HOSPITAL_ID));

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_PREM_SET_APPOINTMENT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG," set appt: "+ response.toString());

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                    map.put(APIClass.KEY_PREM_SET_APPOINTMENT_TIMEID,timeID);
                    map.put(APIClass.KEY_PREM_SET_APPOINTMENT_DAYID,dayID);
                    map.put(APIClass.KEY_PREM_SET_APPOINTMENT_STATUS,setValue);
                    map.put(APIClass.KEY_PREM_SET_APPOINTMENT_USERID, String.valueOf(userID));
                    map.put(APIClass.KEY_HOSPITAL_ID, String.valueOf(HOSPITAL_ID));
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(MySchedulesActivity.this).
                    getRequestQueue();
            AppController.getInstance(MySchedulesActivity.this).addToRequestQueue(stringRequest1);
            return null;
        }
    }

    private void customAddHospital() {
        final Dialog dialog = new Dialog(MySchedulesActivity.this, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_hospital);
        dialog.setTitle("Add Hospital");
        dialog.setCanceledOnTouchOutside(true);

        ImageView close_btn = (ImageView) dialog.findViewById(R.id.hosp_close);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        final CustomEditText _edt_hospname = (CustomEditText) dialog.findViewById(R.id.hosp_name);
        final CustomEditText _edt_hosp_email = (CustomEditText) dialog.findViewById(R.id.hosp_email);
        final CustomEditText _edt_hosp_mobile = (CustomEditText) dialog.findViewById(R.id.hosp_mobile);
        final CustomEditText _edt_hosp_address = (CustomEditText) dialog.findViewById(R.id.hosp_address);
        final CustomEditText _edt_hosp_city = (CustomEditText) dialog.findViewById(R.id.hosp_city);
        final CountryCodePicker ccp_country = (CountryCodePicker) dialog.findViewById(R.id.hosp_country);
        state_spinner = (Spinner)  dialog.findViewById(R.id.hosp_state_spinner);
        final CustomTextViewBold submit_btn = (CustomTextViewBold)dialog.findViewById(R.id.hosp_submit);

        ccp_country.setContentColor(R.color.white);
        ccp_country.setBackgroundColor(getResources().getColor(R.color.white));
        GET_COUNTRY_CODE = ccp_country.getDefaultCountryCodeWithPlus();
        GET_COUNTRY_NAME = ccp_country.getDefaultCountryName();
        GET_COUNTRY_NAME_CODE = ccp_country.getDefaultCountryNameCode();
        new getJsonState().execute(ccp_country.getDefaultCountryNameCode());
        ccp_country.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //  Toast.makeText(SignUpLeapActicity.this, "Updated " + ccp.getSelectedCountryName() +"code: "+ccp.getSelectedCountryNameCode() + "co: "+ccp.getSelectedCountryCode(), Toast.LENGTH_SHORT).show();
                GET_COUNTRY_CODE = ccp_country.getSelectedCountryCodeWithPlus();
                GET_COUNTRY_NAME = ccp_country.getSelectedCountryName();
                GET_COUNTRY_NAME_CODE = ccp_country.getSelectedCountryNameCode();
                new getJsonState().execute(ccp_country.getSelectedCountryNameCode());
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(submit_btn.getWindowToken(), 0);

                if(_edt_hospname.getText().toString().trim().equals("")) {
                    Toast.makeText(MySchedulesActivity.this, "Enter hospital name !!!", Toast.LENGTH_SHORT).show();
                }
                else  if(_edt_hosp_email.getText().toString().trim().equals("")) {
                    Toast.makeText(MySchedulesActivity.this, "Enter email address !!!", Toast.LENGTH_SHORT).show();
                }
                else  if((_edt_hosp_mobile.getText().toString().trim().equals("")) || (_edt_hosp_mobile.getText().toString().trim().length()<10)) {
                    Toast.makeText(MySchedulesActivity.this, "Enter valid 10 digits mobile number !!!", Toast.LENGTH_SHORT).show();
                }
                else  if(_edt_hosp_address.getText().toString().trim().equals("")) {
                    Toast.makeText(MySchedulesActivity.this, "Enter address !!!", Toast.LENGTH_SHORT).show();
                }
                else  if(_edt_hosp_city.getText().toString().trim().equals("")) {
                    Toast.makeText(MySchedulesActivity.this, "Enter city !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.dismiss();
                    submitAddHospitalRequestToServer(_edt_hospname.getText().toString().trim(),_edt_hosp_email.getText().toString().trim(),
                            _edt_hosp_mobile.getText().toString().trim(),_edt_hosp_address.getText().toString().trim(),
                            _edt_hosp_city.getText().toString().trim(),GET_STATE, GET_COUNTRY_NAME);
                }
            }
        });

        dialog.show();
    }

    private void submitAddHospitalRequestToServer(final String hosp_name, final String hosp_email, final String hosp_mobile, final String hosp_address,
                                                  final String hosp_city, final String get_state, final String get_country_name) {
        new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;
            ProgressDialog progressDialog1;
            JSONArray jsonHospArray;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog1 = new ProgressDialog(MySchedulesActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                progressDialog1.setIndeterminate(true);
                progressDialog1.setMessage("Please Wait....");
                progressDialog1.show();


            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {
                    JSONObject jsonObject = JSONParser.addHospital(hosp_name,hosp_email,hosp_mobile,hosp_address,
                            hosp_city, get_state,get_country_name,  USER_ID, USER_LOGIN_TYPE);

                    if (jsonObject != null) {
                        Log.e(Utils.TAG, " GET: " + jsonObject.getString("result"));
                        if(jsonObject.getString("result").equals("success")) {
                            status = true;
                            if (jsonObject.has("doc_hospital")) {
                                jsonHospArray = jsonObject.getJSONArray("doc_hospital");
                            }

                        }
                        else {
                            status = false;
                        }
                    }
                    // return true;
                } catch (JSONException e) {
                    Log.i(Utils.TAG, "Error : " + e.getLocalizedMessage());
                    return false;
                }
                return status;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (  progressDialog1 != null)
                    progressDialog1.dismiss();
                Log.e(Utils.TAG, "aBoolean: " + String.valueOf(aBoolean));
                if (aBoolean) {

                    if (sharedPreferences != null) {
                        shareadPreferenceClass.clearDocHoapitals();
                    }

                    try {
                    if(jsonHospArray.length() >0) {
                        hospitalDocArraylist = new ArrayList<>();
                        for (int k = 0; k < jsonHospArray.length(); k++) {
                             hospitalDocArraylist.add(new HospitalList(jsonHospArray.getJSONObject(k).getInt("hosp_id"),
                                        jsonHospArray.getJSONObject(k).getString("hosp_name"),
                                        jsonHospArray.getJSONObject(k).getString("hosp_addrs"),jsonHospArray.getJSONObject(k).getString("hosp_city"),
                                        jsonHospArray.getJSONObject(k).getString("hosp_state"),jsonHospArray.getJSONObject(k).getString("hosp_country"),
                                        jsonHospArray.getJSONObject(k).getInt("doc_id"),"1"));
                            }

                        if (sharedPreferences != null) {
                            Gson gson2 = new Gson();
                            String jsonText1 = gson2.toJson(hospitalDocArraylist);
                            shareadPreferenceClass.clearDocHoapitals();
                            shareadPreferenceClass.setDocHoapitals(jsonText1);
                        }

                        ArrayList<String> hospitalArray = new ArrayList<String>();
                        for (int i = 0; i < hospitalDocArraylist.size(); i++) {
                            hospitalArray.add(hospitalDocArraylist.get(i).getHospitalName());
                        }

                        ArrayAdapter<String> hospAdapter = new ArrayAdapter<String>(MySchedulesActivity.this, R.layout.spinner_text, hospitalArray );
                        hospital_spinner.setAdapter(hospAdapter);
                        hospAdapter.notifyDataSetChanged();
                       }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MySchedulesActivity.this, R.style.CustomDialog);
                    alertDialogBuilder.setMessage("Hospital Added successfully. ");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else {
                    Toast.makeText(MySchedulesActivity.this, "Failed to add hospital !!!", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }

    class getJsonState extends AsyncTask<String,String,String> {
        String country_shortname;
        @Override
        protected String doInBackground(String... key) {
            String stateText = key[0];
            stateText = stateText.trim();
            country_shortname = stateText.trim();
            Log.d(Utils.TAG, "State selected1: " + stateText);
            stateText = stateText.replace(" ", "+");

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_STATE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG, response.toString());
                            if (response != null) {
                                JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null;
                                ArrayList<String> stateArray = new ArrayList<String>();
                                try {
                                    jsonObject1 = new JSONObject(response);
                                    String staus_res = jsonObject1.getString("status");
                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray1 = jsonObject1.getJSONArray("page_result");
                                        if (jsonArray1.length() > 0) {
                                          //  stateArray.add("--Select--");
                                            for (int i = 0; i < jsonArray1.length(); i++) {
                                                stateArray.add(jsonArray1.getJSONObject(i).getString("StateName"));
                                            }
                                        }


                                        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(MySchedulesActivity.this, R.layout.spinner_text, stateArray );
                                        locationAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                                        state_spinner.setAdapter(locationAdapter);

                                        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view,
                                                                       int position, long id) {
                                                Log.d(Utils.TAG, "stateName: "+ (String) parent.getItemAtPosition(position));
                                                String get_state = (String) parent.getItemAtPosition(position);
                                                GET_STATE = (String) parent.getItemAtPosition(position);
                                            }
                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {
                                                GET_STATE = "--Select--";
                                            }
                                        });

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
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                    map.put("shortname",country_shortname);
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(MySchedulesActivity.this).
                    getRequestQueue();
            AppController.getInstance(MySchedulesActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }
    }

    public class setJsonNumberPatients extends AsyncTask<Void, Void, Void> {
        String  timings;
        int userID;

        public setJsonNumberPatients(String timeings){
            this.timings = timeings;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(Utils.TAG, "No.of Patients: " + String.valueOf(timings));

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_SET_APPOINTMENT_SLOTS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG," set slots: "+ response.toString());
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("result");
                                if(staus_res.equals("success")) {
                                    AppUtils.showCustomSuccessMessage(MySchedulesActivity.this, "Done!","No. of Slots updated", "OK", null, null);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                    map.put(APIClass.KEY_PATIENTS_NUMBER,timings);
                    map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                    map.put(APIClass.KEY_LOGINTYPE, String.valueOf(USER_LOGIN_TYPE));
                    map.put(APIClass.KEY_HOSPITAL_ID, String.valueOf(HOSPITAL_ID));
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(MySchedulesActivity.this).
                    getRequestQueue();
            AppController.getInstance(MySchedulesActivity.this).addToRequestQueue(stringRequest1);
            return null;
        }
    }

    public class setJsonSetHolidays extends AsyncTask<Void, Void, Void> {
        String  holid_date, holi_reason, holiday_date_new;

        public setJsonSetHolidays(String holi_date, String holi_reason) {
            this.holid_date = holi_date;
            this.holi_reason = holi_reason;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(Utils.TAG, " holiday_date: " + String.valueOf(holid_date));
            Log.d(Utils.TAG, " holiday_reason: " + String.valueOf(holiday_reason));
            HOLIDAYS_ARRAY = new ArrayList<>();

            if(holid_date != null){
                DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = inputFormat.parse(holid_date);
                    holiday_date_new = outputFormat.format(date);
                    Log.d(Utils.TAG, " holiday_date_new: " + String.valueOf(holiday_date_new));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_SET_HOLIDAYS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG," set holiday: "+ response.toString());
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("result");

                                JSONArray JAHolidays = jsonObject.getJSONArray("holiday_details");
                                if(JAHolidays.length() > 0 ) {
                                    for (int k = 0; k < JAHolidays.length(); k++) {
                                        HOLIDAYS_ARRAY.add(new Holidays(JAHolidays.getJSONObject(k).getInt("holiday_id"),
                                                JAHolidays.getJSONObject(k).getInt("doc_id"),JAHolidays.getJSONObject(k).getInt("doc_type"),
                                                JAHolidays.getJSONObject(k).getInt("hosp_id"),JAHolidays.getJSONObject(k).getString("holiday_date"),
                                                JAHolidays.getJSONObject(k).getString("reason")));
                                    }
                                    mHolidayAdapter = new HolidaysAdapter(MySchedulesActivity.this, HOLIDAYS_ARRAY);
                                    holiday_recyclerView.setAdapter(mHolidayAdapter);
                                    mHolidayAdapter.notifyDataSetChanged();
                                }

                                if(staus_res.equals("success")) {
                                    AppUtils.showCustomSuccessMessage(MySchedulesActivity.this, "Done!","Holiday date updated successfully !!!", "OK", null, null);
                                    holiday_date.setText("");
                                    holiday_reason.setText("");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                    map.put(APIClass.KEY_HOLIDAY_DATE,holiday_date_new);
                    map.put(APIClass.KEY_HOLIDAY_REASON,holi_reason);
                    map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                    map.put(APIClass.KEY_LOGINTYPE, String.valueOf(USER_LOGIN_TYPE));
                    map.put(APIClass.KEY_HOSPITAL_ID, String.valueOf(HOSPITAL_ID));
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(MySchedulesActivity.this).
                    getRequestQueue();
            AppController.getInstance(MySchedulesActivity.this).addToRequestQueue(stringRequest1);
            return null;
        }
    }
}
