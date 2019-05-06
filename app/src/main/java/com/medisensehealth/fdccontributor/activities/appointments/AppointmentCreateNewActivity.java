package com.medisensehealth.fdccontributor.activities.appointments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.medisensehealth.fdccontributor.DataModel.AppointmentList;
import com.medisensehealth.fdccontributor.DataModel.HospitalList;
import com.medisensehealth.fdccontributor.DataModel.OtherSettings;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomEditText;
import com.medisensehealth.fdccontributor.views.CustomTextViewItalicBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 20-07-2018.
 */

public class AppointmentCreateNewActivity extends AppCompatActivity implements View.OnClickListener {
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID, HOSPITAL_ID;
    String USER_NAME,USER_LOGIN_TYPE,DOC_HOSIPTAL_LIST, CONSULTATION_CHARGES, COLLECT_CONSULT_BEFORE, OTHER_SETTINGS_LIST, USER_CITY, USER_STATE, USER_COUNTRY;
    ArrayList<HospitalList> hospitalDocArraylist;
    List<OtherSettings> otherSettingsListArraylist = new ArrayList<>();

    AutoCompleteTextView search_autocomplete;
    ImageView search_submit;
    AppCompatCheckBox direct_appoint_checkbox;
    LinearLayout appoint_contents;
    Spinner appoint_time_spinner, appoint_date_spinner;
    CustomEditText _edt_name, _edt_mobile, _edt_email, _edt_pincode, _edt_city, _edt_state, _edt_country, _edt_address, _edt_consultaion_charges;
    private RadioGroup radioGenderGroup;
    private RadioButton rb_male, rb_female, rb_trans;
    CustomTextViewItalicBold no_slots_text;
    Button submit_appointment;
    int is_walking_appointment = 1;
    int is_no_slots = 0;
    String PATIENT_APPOINTMENT_DATE, PATIENT_APPOINTMENT_TIME,PATIENT_GENDER;
    int PATIENT_ID = 0;

    LinearLayout view_more_btn, view_more_contents, consultion_fee_contents;
    ImageView view_more_image;
    boolean view_more_status = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_appointment_create_new);

        hospitalDocArraylist = new ArrayList<>();
        otherSettingsListArraylist = new ArrayList<>();
        HOSPITAL_ID = 0;
        is_walking_appointment = 1;
        is_no_slots = 0;
        PATIENT_ID = 0;
        COLLECT_CONSULT_BEFORE = "0";
        view_more_status = false;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);
        }

        shareadPreferenceClass = new ShareadPreferenceClass(AppointmentCreateNewActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(AppointmentCreateNewActivity.this);

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
            OTHER_SETTINGS_LIST = sharedPreferences.getString(HCConstants.PREF_OTHER_SETTINGS,"");
            CONSULTATION_CHARGES  = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_CONS_CHARGE,"");
            USER_CITY = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_CITY,"");
            USER_STATE  = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_STATE,"");
            USER_COUNTRY = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_COUNTRY,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
            OTHER_SETTINGS_LIST = sharedPreferences.getString(HCConstants.PREF_OTHER_SETTINGS,"");
            CONSULTATION_CHARGES  = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_CONS_CHARGE,"");
            USER_CITY = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_LOCATION,"");
            USER_STATE  = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_STATE,"");
            USER_COUNTRY = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_COUNTRY,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
            OTHER_SETTINGS_LIST = sharedPreferences.getString(HCConstants.PREF_OTHER_SETTINGS,"");
            CONSULTATION_CHARGES  = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_CONS_CHARGE,"");
            USER_CITY = "";
            USER_STATE  = "";
            USER_COUNTRY = "";

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        Log.d(Utils.TAG , " *********** AppointmentCreateNewActivity ****************");
        Log.d(Utils.TAG +" LOGIN_ID: ", String.valueOf(Utils.USER_LOGIN_ID));
        Log.d(Utils.TAG +" LOGIN_TYPE: ", Utils.USER_LOGIN_TYPE);
        Log.d(Utils.TAG +" HOSP_ID: ", String.valueOf(HOSPITAL_ID));
        Log.d(Utils.TAG +" CONS_CHA: ", String.valueOf(CONSULTATION_CHARGES));

        initializeViews();
    }

    private void initializeViews() {
        otherSettingsListArraylist = new ArrayList<>();
        // Initialize Views
        search_autocomplete = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView_search);
        search_autocomplete.setThreshold(2);
        search_submit = (ImageView) findViewById(R.id.search_submit);
        direct_appoint_checkbox = (AppCompatCheckBox) findViewById(R.id.appoint_direct_checkbox);
        appoint_contents = (LinearLayout) findViewById(R.id.appoint_direct_contents);
        appoint_contents.setVisibility(View.GONE);
        appoint_date_spinner = (Spinner) findViewById(R.id.appoint_date_spinner);
        appoint_time_spinner = (Spinner) findViewById(R.id.appoint_time_spinner);
        _edt_name = (CustomEditText) findViewById(R.id.appoint_name);
        radioGenderGroup = (RadioGroup) findViewById(R.id.radioGender);
        rb_male = (RadioButton) findViewById(R.id.radioMale);
        rb_female = (RadioButton) findViewById(R.id.radioFemale);
        rb_trans = (RadioButton) findViewById(R.id.radioTrans);
        _edt_mobile = (CustomEditText) findViewById(R.id.appoint_mobile);
        _edt_email = (CustomEditText) findViewById(R.id.appoint_email);
        _edt_pincode = (CustomEditText) findViewById(R.id.appoint_pincode);
        _edt_city = (CustomEditText) findViewById(R.id.appoint_city);
        _edt_state = (CustomEditText) findViewById(R.id.appoint_state);
        _edt_country = (CustomEditText) findViewById(R.id.appoint_country);
        _edt_address = (CustomEditText) findViewById(R.id.appoint_address);
        _edt_consultaion_charges = (CustomEditText) findViewById(R.id.appoint_consultaion_charges);
        submit_appointment = (Button) findViewById(R.id.appoint_submit);
        submit_appointment.setOnClickListener(this);
        no_slots_text = (CustomTextViewItalicBold)  findViewById(R.id.appoint_noslots);
        no_slots_text.setVisibility(View.GONE);

        view_more_btn = (LinearLayout) findViewById(R.id.appoint_view_more_btn);
        view_more_btn.setOnClickListener(this);
        view_more_image = (ImageView) findViewById(R.id.appoint_view_more_image);
        view_more_contents  = (LinearLayout) findViewById(R.id.appoint_view_more_content);
        view_more_contents.setVisibility(View.GONE);
        consultion_fee_contents = (LinearLayout)  findViewById(R.id.appoint_consultaion_charges_layout);
        consultion_fee_contents.setVisibility(View.GONE);

        _edt_city.setText(USER_CITY);
        _edt_state.setText(USER_STATE);
        _edt_country.setText(USER_COUNTRY);
        _edt_consultaion_charges.setText(CONSULTATION_CHARGES);

        Gson gson = new Gson();
        if (!OTHER_SETTINGS_LIST.equals("")) {
            otherSettingsListArraylist = gson.fromJson(OTHER_SETTINGS_LIST, new TypeToken<List<OtherSettings>>() {
            }.getType());
            if(otherSettingsListArraylist.size() > 0 ) {
                Log.d(Utils.TAG, "OTHER_SETTINGS_LIST > 0 " + otherSettingsListArraylist.size());
                for(int i=0;i<otherSettingsListArraylist.size();i++) {
                    COLLECT_CONSULT_BEFORE = String.valueOf(otherSettingsListArraylist.get(i).getConsultationFee());
                    Log.d(Utils.TAG +" CONS_BEFORE:", COLLECT_CONSULT_BEFORE);

                    if(COLLECT_CONSULT_BEFORE.equals("1")) {
                        consultion_fee_contents.setVisibility(View.VISIBLE);
                    }
                    else  if(COLLECT_CONSULT_BEFORE.equals("2")) {
                        consultion_fee_contents.setVisibility(View.GONE);
                    }
                    else {
                        consultion_fee_contents.setVisibility(View.GONE);
                    }
                }
            }
        }

        direct_appoint_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // update your model (or other business logic) based on isChecked
                if (buttonView.isChecked()) {
                    appoint_contents.setVisibility(View.VISIBLE);  // checked
                    is_walking_appointment = 2;
                    no_slots_text.setVisibility(View.GONE);
                    submit_appointment.setVisibility(View.VISIBLE);
                }
                else
                {
                    appoint_contents.setVisibility(View.GONE);   // not checked
                    is_walking_appointment = 1;

                    if( is_no_slots == 1) {
                        no_slots_text.setVisibility(View.VISIBLE);
                        submit_appointment.setVisibility(View.GONE);
                    }
                    else {
                        no_slots_text.setVisibility(View.GONE);
                        submit_appointment.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        new getJsonPatientsInfo().execute("PATIENT_SEARCH");
        search_autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View arg1, int pos,
                                    long id) {

                Log.d(Utils.TAG, " patient: "+ (String) adapter.getItemAtPosition(pos));
                String selected_patient = (String) adapter.getItemAtPosition(pos);
               // Toast.makeText(AppointmentCreateNewActivity.this," selected: "+(String) adapter.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();

                String[] separated = selected_patient.split(" - ");
                String patient_id =  separated[0];
                Log.d(Utils.TAG, " selctedID: "+ patient_id);
                PATIENT_ID = Integer.parseInt(patient_id);
                new getJsonPatientsDetails().execute(patient_id);
            }
        });

        search_autocomplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(search_autocomplete.length()<=0) {
                    _edt_name.setText("");
                    _edt_mobile.setText("");
                    _edt_email.setText("");
                    _edt_city.setText("");
                    _edt_address.setText("");
                    _edt_state.setText("");
                    _edt_country.setText("");
                    rb_male.setChecked(false);
                    rb_female.setChecked(false);
                    rb_trans.setChecked(false);
                    PATIENT_ID = 0;
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
            case R.id.item_swich_user:
                if(sharedPreferences != null) {
                    HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
                }
                showRadioButtonDialog();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void showRadioButtonDialog() {
        Gson gson = new Gson();
        if (!DOC_HOSIPTAL_LIST.equals("")) {
            hospitalDocArraylist = gson.fromJson(DOC_HOSIPTAL_LIST, new TypeToken<List<HospitalList>>() {
            }.getType());
            if(hospitalDocArraylist.size() > 0 ) {
                openCustomHospitalSwitchDialog(hospitalDocArraylist);
            }
        }
    }

    private void openCustomHospitalSwitchDialog(ArrayList<HospitalList> hospitalDocArraylist) {
        final Dialog dialog = new Dialog(AppointmentCreateNewActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_hospital_switch);
        List<String> stringList=new ArrayList<>();  // here is list
        List<Integer> strinIDList=new ArrayList<>();
        for(int i=0;i<hospitalDocArraylist.size();i++) {
            stringList.add(hospitalDocArraylist.get(i).getHospitalName());
            strinIDList.add(hospitalDocArraylist.get(i).getHospitalId());
        }
        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);

        for(int i=0;i<stringList.size();i++){
            RadioButton rb=new RadioButton(AppointmentCreateNewActivity.this); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(stringList.get(i));
            rb.setPadding(10, 40, 10, 40);
            rb.setTag(strinIDList.get(i));
            rb.setTextSize(18);
            rg.addView(rb);

            if(HOSPITAL_ID == strinIDList.get(i)) {
                Log.d(Utils.TAG, " strinID: "+strinIDList.get(i));
                rb.setChecked(true);
            }
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                int mySelectedIndex = (int) radioButton.getTag();
                // Toast.makeText(getApplicationContext()," id: "+ mySelectedIndex +" name: "+radioButton.getText(),Toast.LENGTH_LONG).show();
                if(sharedPreferences != null) {
                    shareadPreferenceClass.clearDocHoapitalID();
                    shareadPreferenceClass.setDocHoapitalID(mySelectedIndex);
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.appoint_submit:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(submit_appointment.getWindowToken(), 0);

                validateAppointmentForm();
                break;
            case R.id.appoint_view_more_btn:
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(view_more_btn.getWindowToken(), 0);
                if(view_more_status == false) {
                    view_more_contents.setVisibility(View.VISIBLE);
                    view_more_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_more_status = true;
                }
                else  if(view_more_status == true) {
                    view_more_contents.setVisibility(View.GONE);
                    view_more_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_more_status = false;
                }
                break;
        }
    }

    class getJsonPatientsInfo extends AsyncTask<String,String,String> {
        String patient_name;
        @Override
        protected String doInBackground(String... key) {
            patient_name = key[0];
            Log.d(Utils.TAG, " selected patient_name: " + patient_name);
            Log.d(Utils.TAG, " selected USER_ID: " + USER_ID);
            Log.d(Utils.TAG, " selected USER_LOGIN_TYPE: " + USER_LOGIN_TYPE);
            Log.d(Utils.TAG, " selected HOSPITAL_ID: " + HOSPITAL_ID);

            int socketTimeout = 30000; // 30 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_APPOINT_PATIENT_SEARCH,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            Log.d(Utils.TAG, " patient list: "+ response.toString());
                            if(response != null) {
                                JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null;  JSONArray jsonArray2 = null; JSONArray jsonArray3 = null;
                                ArrayList<String> patientArray = new ArrayList<String>();

                                try {
                                    jsonObject1 = new JSONObject(response);
                                    String staus_res = jsonObject1.getString("status");

                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray1 = jsonObject1.getJSONArray("patient_details");
                                        if (jsonArray1.length() > 0) {
                                            patientArray.add("--Select--");
                                            for (int i = 0; i < jsonArray1.length(); i++) {
                                                patientArray.add(jsonArray1.getJSONObject(i).getInt("patient_id")+" - "+jsonArray1.getJSONObject(i).getString("patient_name")+" ["+jsonArray1.getJSONObject(i).getString("patient_mob")+"]");
                                            }
                                        }
                                        //Creating the instance of ArrayAdapter containing list of fruit names
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String> (AppointmentCreateNewActivity.this, android.R.layout.select_dialog_item, patientArray);
                                        search_autocomplete.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();

                                        jsonArray2 = jsonObject1.getJSONArray("todays_appoint_slots");
                                        if (jsonArray2.length() > 0) {
                                            no_slots_text.setVisibility(View.GONE);
                                            submit_appointment.setVisibility(View.VISIBLE);
                                            is_no_slots = 0;
                                        }
                                        else {
                                            no_slots_text.setVisibility(View.VISIBLE);
                                            submit_appointment.setVisibility(View.GONE);
                                            is_no_slots = 1;
                                        }

                                        jsonArray3 = jsonObject1.getJSONArray("appoint_details");
                                        ArrayList<String> appoint_dateArray = new ArrayList<String>();
                                        final ArrayList<String> appoint_dateIDArray = new ArrayList<String>();
                                        if (jsonArray3.length() > 0) {
                                            appoint_dateArray.add("--Select--");
                                            appoint_dateIDArray.add("0");
                                            for (int i = 0; i < jsonArray3.length(); i++) {
                                                appoint_dateArray.add(jsonArray3.getJSONObject(i).getString("appt_date"));
                                                appoint_dateIDArray.add(jsonArray3.getJSONObject(i).getString("appt_id"));
                                              }

                                            ArrayAdapter<String> apptdateAdapter = new ArrayAdapter<String>(AppointmentCreateNewActivity.this, R.layout.spinner_text, appoint_dateArray );
                                            appoint_date_spinner.setAdapter(apptdateAdapter);

                                            appoint_date_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view,
                                                                           int position, long id) {
                                                    Log.d(Utils.TAG, " appoint_date: "+ (String) parent.getItemAtPosition(position));
                                                    String get_state = (String) parent.getItemAtPosition(position);
                                                    Log.d(Utils.TAG, "appt_date_ID: "+ appoint_dateIDArray.get(position).toString());

                                                    if(appoint_dateIDArray.get(position).toString().equals("0")) {
                                                        PATIENT_APPOINTMENT_DATE = "";
                                                    }
                                                    else {
                                                        PATIENT_APPOINTMENT_DATE = appoint_dateIDArray.get(position).toString();
                                                        new getJsonAppointmentTimings().execute(appoint_dateIDArray.get(position).toString());
                                                    }
                                                }
                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {
                                                    PATIENT_APPOINTMENT_DATE = "";
                                                }
                                            });
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

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                    map.put(APIClass.KEY_USERID, String.valueOf(Utils.USER_LOGIN_ID));
                    map.put(APIClass.KEY_LOGINTYPE, Utils.USER_LOGIN_TYPE);
                    map.put(APIClass.KEY_HOSPITAL_ID, String.valueOf(HOSPITAL_ID));
                    return map;
                }
            };

            stringRequest.setRetryPolicy(policy);

            RequestQueue requestQueue = AppController.getInstance(AppointmentCreateNewActivity.this).
                    getRequestQueue();
            AppController.getInstance(AppointmentCreateNewActivity.this).addToRequestQueue(stringRequest);

            return null;
        }
    }

    class getJsonPatientsDetails extends AsyncTask<String,String,String> {
        String patient_id;
        @Override
        protected String doInBackground(String... key) {
            patient_id = key[0];
            Log.d(Utils.TAG, " selected patient_id: " + patient_id);

            int socketTimeout = 30000; // 30 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_APPOINT_PATIENT_DETAIL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG, " patient info: "+ response.toString());
                            if(response != null) {
                                JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null;
                                ArrayList<String> patientArray = new ArrayList<String>();

                                try {
                                    jsonObject1 = new JSONObject(response);
                                    String staus_res = jsonObject1.getString("status");

                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray1 = jsonObject1.getJSONArray("patient_details");
                                        if (jsonArray1.length() > 0) {
                                            for (int i = 0; i < jsonArray1.length(); i++) {
                                                JSONObject JOStuff = jsonArray1.getJSONObject(i);
                                                _edt_name.setText(JOStuff.getString("patient_name"));
                                                _edt_mobile.setText(JOStuff.getString("patient_mob"));
                                                _edt_email.setText(JOStuff.getString("patient_email"));
                                                _edt_city.setText(JOStuff.getString("patient_loc"));
                                                _edt_address.setText(JOStuff.getString("patient_addrs"));
                                                _edt_state.setText(JOStuff.getString("pat_state"));
                                                _edt_country.setText(JOStuff.getString("pat_country"));

                                                if(JOStuff.getString("patient_gen").equals("1")) {
                                                    rb_male.setChecked(true);
                                                }
                                                else if(JOStuff.getString("patient_gen").equals("1")) {
                                                    rb_female.setChecked(true);
                                                }
                                                else if(JOStuff.getString("patient_gen").equals("1")) {
                                                    rb_trans.setChecked(true);
                                                }
                                                else {
                                                    rb_male.setChecked(false);
                                                    rb_female.setChecked(false);
                                                    rb_trans.setChecked(false);
                                                }
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

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                    map.put(APIClass.KEY_USERID, String.valueOf(Utils.USER_LOGIN_ID));
                    map.put(APIClass.KEY_LOGINTYPE, Utils.USER_LOGIN_TYPE);
                    map.put(APIClass.KEY_PATIENT_ID, patient_id);
                    map.put(APIClass.KEY_HOSPITAL_ID, String.valueOf(HOSPITAL_ID));
                    return map;
                }
            };

            stringRequest.setRetryPolicy(policy);

            RequestQueue requestQueue = AppController.getInstance(AppointmentCreateNewActivity.this).
                    getRequestQueue();
            AppController.getInstance(AppointmentCreateNewActivity.this).addToRequestQueue(stringRequest);

            return null;
        }
    }

    class getJsonAppointmentTimings extends AsyncTask<String,String,String> {
        String appoint_date_id;
        @Override
        protected String doInBackground(String... key) {
            appoint_date_id = key[0];
            Log.d(Utils.TAG, " selected appoint_date_id: " + appoint_date_id);

            int socketTimeout = 30000; // 30 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_APPOINT_TIMINGS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG, " timings info: "+ response.toString());
                            if(response != null) {
                                JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null;
                                try {
                                    jsonObject1 = new JSONObject(response);
                                    String staus_res = jsonObject1.getString("status");

                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray1 = jsonObject1.getJSONArray("appoint_timing_details");
                                        ArrayList<String> appoint_TimeArray = new ArrayList<String>();
                                        final ArrayList<String> appoint_timeIDArray = new ArrayList<String>();
                                        if (jsonArray1.length() > 0) {
                                            appoint_TimeArray.add("--Select--");
                                            appoint_timeIDArray.add("0");
                                            for (int i = 0; i < jsonArray1.length(); i++) {
                                                appoint_TimeArray.add(jsonArray1.getJSONObject(i).getString("aapt_time"));
                                                appoint_timeIDArray.add(jsonArray1.getJSONObject(i).getString("aapt_time_id"));
                                            }

                                            ArrayAdapter<String> appttimeAdapter = new ArrayAdapter<String>(AppointmentCreateNewActivity.this, R.layout.spinner_text, appoint_TimeArray );
                                            appoint_time_spinner.setAdapter(appttimeAdapter);

                                            appoint_time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view,
                                                                           int position, long id) {
                                                    Log.d(Utils.TAG, " appoint_time: "+ (String) parent.getItemAtPosition(position));
                                                    String get_state = (String) parent.getItemAtPosition(position);
                                                    Log.d(Utils.TAG, "appt_time_ID: "+ appoint_timeIDArray.get(position).toString());

                                                    if(appoint_timeIDArray.get(position).toString().equals("0")) {
                                                        PATIENT_APPOINTMENT_TIME = "";
                                                    }
                                                    else {
                                                        PATIENT_APPOINTMENT_TIME = appoint_timeIDArray.get(position).toString();
                                                     }
                                                }
                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {
                                                    PATIENT_APPOINTMENT_TIME = "";
                                                }
                                            });
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

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                    map.put(APIClass.KEY_USERID, String.valueOf(Utils.USER_LOGIN_ID));
                    map.put(APIClass.KEY_LOGINTYPE, Utils.USER_LOGIN_TYPE);
                    map.put(APIClass.KEY_APPOINT_DATE_ID, appoint_date_id);
                    map.put(APIClass.KEY_HOSPITAL_ID, String.valueOf(HOSPITAL_ID));
                    return map;
                }
            };

            stringRequest.setRetryPolicy(policy);

            RequestQueue requestQueue = AppController.getInstance(AppointmentCreateNewActivity.this).
                    getRequestQueue();
            AppController.getInstance(AppointmentCreateNewActivity.this).addToRequestQueue(stringRequest);

            return null;
        }
    }

    private void validateAppointmentForm() {

        String name = _edt_name.getText().toString().trim();
        String mobile = _edt_mobile.getText().toString().trim();
        String city = _edt_city.getText().toString().trim();
        String email = _edt_email.getText().toString().trim();
        String address = _edt_address.getText().toString().trim();
        String pincode = _edt_pincode.getText().toString().trim();
        String state = _edt_state.getText().toString().trim();
        String country = _edt_country.getText().toString().trim();
        String consultaion_fees = _edt_consultaion_charges.getText().toString().trim();

        int checkedRadioButtonId = radioGenderGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {   // No items Selected
            PATIENT_GENDER = "0";
        }
        else{
            if (checkedRadioButtonId == R.id.radioMale) {
                PATIENT_GENDER = "1";
            }
            else if (checkedRadioButtonId == R.id.radioFemale) {
                PATIENT_GENDER = "2";
            }
            else if (checkedRadioButtonId == R.id.radioTrans) {
                PATIENT_GENDER = "3";
            }
            else {
                PATIENT_GENDER = "0";
            }
        }


        if(name.equals("")) {
            Toast.makeText(AppointmentCreateNewActivity.this, "Enter patient name !!!", Toast.LENGTH_SHORT).show();
        }
        else if(PATIENT_GENDER.equals("0")) {
            Toast.makeText(AppointmentCreateNewActivity.this, "Select patient gender !!!", Toast.LENGTH_SHORT).show();
        }
        else if((mobile.equals("")) || (mobile.length()<10)) {
            Toast.makeText(AppointmentCreateNewActivity.this, "Enter valid 10 digits mobile number !!!", Toast.LENGTH_SHORT).show();
        }
        else if(city.equals("")) {
            Toast.makeText(AppointmentCreateNewActivity.this, "Enter patient city !!!", Toast.LENGTH_SHORT).show();
        }
        else if((is_walking_appointment == 2) && (PATIENT_APPOINTMENT_DATE.equals(""))) {
            Toast.makeText(AppointmentCreateNewActivity.this, "Select appointment date !!!", Toast.LENGTH_SHORT).show();
        }
        else if((is_walking_appointment == 2) && (PATIENT_APPOINTMENT_TIME.equals(""))) {
            Toast.makeText(AppointmentCreateNewActivity.this, "Select appointment time !!!", Toast.LENGTH_SHORT).show();
        }
        else {
            if (NetworkUtil.getConnectivityStatusString(AppointmentCreateNewActivity.this).equalsIgnoreCase("enabled")) {
                submitBookAppointmentToServer(name,PATIENT_GENDER,PATIENT_APPOINTMENT_DATE,PATIENT_APPOINTMENT_TIME,is_walking_appointment,
                        mobile,city,email,address,pincode, state, country, consultaion_fees);
            } else {
                AppUtils.showCustomAlertMessage(AppointmentCreateNewActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }
        }


    }

    private void submitBookAppointmentToServer(final String name, final String patient_gender, final String patient_appointment_date,
                                               final String patient_appointment_time, final int is_walking_appointment, final String mobile,
                                               final String city, final String email, final String address, final String pincode, final String state,
                                               final String country, final String consultaion_fees) {

        Log.d(Utils.TAG, " ********************* submitBookAppointmentToServer ********************** ");
        Log.d(Utils.TAG, " name: "+name);
        Log.d(Utils.TAG, " gender: "+patient_gender);
        Log.d(Utils.TAG, " appointment_date: "+patient_appointment_date);
        Log.d(Utils.TAG, " appointment_time: "+patient_appointment_time);
        Log.d(Utils.TAG, " is_walking_appointment: "+is_walking_appointment);   // 1- walk in appointment, 2 - future appointment
        Log.d(Utils.TAG, " mobile: "+mobile);
        Log.d(Utils.TAG, " city: "+city);
        Log.d(Utils.TAG, " email: "+email);
        Log.d(Utils.TAG, " address: "+address);
        Log.d(Utils.TAG, " pincode: "+pincode);
        Log.d(Utils.TAG, " state: "+state);
        Log.d(Utils.TAG, " country: "+country);
        Log.d(Utils.TAG, " consultaion_fees: "+consultaion_fees);
        Log.d(Utils.TAG, " PATIENT_ID: "+PATIENT_ID);

        final ProgressDialog progressDialog2 = new ProgressDialog(AppointmentCreateNewActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog2.setIndeterminate(true);
        progressDialog2.setMessage("Booking Appointment...");
        progressDialog2.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_APPOINT_BOOK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "result: "+ response.toString());
                        if (response != null) {
                            try {
                                final List<AppointmentList> appointmentArryList  = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("result");
                                if(staus_res.equals("success")) {

                                    JSONArray  jsonArray = jsonObject.getJSONArray("appointment_today_details");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            appointmentArryList.add(new AppointmentList(jsonArray.getJSONObject(i).getInt("token_id"),
                                                    jsonArray.getJSONObject(i).getInt("token_no"),jsonArray.getJSONObject(i).getInt("patient_id"),
                                                    jsonArray.getJSONObject(i).getString("appoint_trans_id"),jsonArray.getJSONObject(i).getString("patient_name"),
                                                    jsonArray.getJSONObject(i).getInt("doc_id"),jsonArray.getJSONObject(i).getInt("doc_type"),
                                                    jsonArray.getJSONObject(i).getInt("hosp_id"),jsonArray.getJSONObject(i).getString("status"),
                                                    jsonArray.getJSONObject(i).getString("app_date"),jsonArray.getJSONObject(i).getString("app_time"),
                                                    jsonArray.getJSONObject(i).getString("created_date"),jsonArray.getJSONObject(i).getString("patient_email"),
                                                    jsonArray.getJSONObject(i).getString("patient_mob")));
                                        }

                                        //Set the values
                                        Gson gson = new Gson();
                                        String jsonText = gson.toJson(appointmentArryList);
                                        if (sharedPreferences != null) {
                                            Log.d(Utils.TAG , " clear appoint"+appointmentArryList.size());
                                            shareadPreferenceClass.clearAppointmentLists();
                                            shareadPreferenceClass.setAppointmentList(jsonText);
                                        }
                                    }

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AppointmentCreateNewActivity.this, R.style.CustomDialog);
                                    alertDialogBuilder.setMessage("The appointment has been booked successfully. ");
                                    alertDialogBuilder.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                  finish();
                                                }
                                            });

                                    alertDialogBuilder.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                                else {
                                    AppUtils.showCustomErrorMessage(AppointmentCreateNewActivity.this, "Create Appointment", "Failed to create patient appointment. \nTry again later", "OK", null, null);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        progressDialog2.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog2.dismiss();
                      //  AppUtils.showCustomAlertMessage(AppointmentCreateNewActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                        Toast.makeText(AppointmentCreateNewActivity.this, "Failed to book an appointment", Toast.LENGTH_SHORT).show();
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
                map.put(APIClass.KEY_APPOINT_NAME, name);
                map.put(APIClass.KEY_APPOINT_GENDER, patient_gender);
                map.put(APIClass.KEY_APPOINT_MOBILE, mobile);
                map.put(APIClass.KEY_APPOINT_CITY, city);
                map.put(APIClass.KEY_APPOINT_CHECK, String.valueOf(is_walking_appointment));
                map.put(APIClass.KEY_PATIENT_ID, String.valueOf(PATIENT_ID));

                if(patient_appointment_date != null){
                    map.put(APIClass.KEY_APPOINT_DATE_VAL, patient_appointment_date);
                }

                if(patient_appointment_time != null){
                    map.put(APIClass.KEY_APPOINT_TIME_VAL, patient_appointment_time);
                }

                if(email != null){
                    map.put(APIClass.KEY_APPOINT_EMAIL, email);
                }

                if(address != null){
                    map.put(APIClass.KEY_APPOINT_ADDRESS, address);
                }

                if(pincode != null){
                    map.put(APIClass.KEY_APPOINT_PINCODE, pincode);
                }

                if(state != null){
                    map.put(APIClass.KEY_APPOINT_STATE, state);
                }

                if(country != null){
                    map.put(APIClass.KEY_APPOINT_COUNTRY, country);
                }

                if(consultaion_fees != null){
                    map.put(APIClass.KEY_APPOINT_CONSULTAION_CHARGE, consultaion_fees);
                }

                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(AppointmentCreateNewActivity.this).
                getRequestQueue();
        AppController.getInstance(AppointmentCreateNewActivity.this).addToRequestQueue(stringRequest);

    }
}
