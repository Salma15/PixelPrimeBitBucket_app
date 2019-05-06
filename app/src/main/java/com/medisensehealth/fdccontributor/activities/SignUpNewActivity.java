package com.medisensehealth.fdccontributor.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.medisensehealth.fdccontributor.DataModel.SpecializationList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.parser.JSONParser;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomEditText;
import com.medisensehealth.fdccontributor.views.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by salma on 19/02/18.
 */

public class SignUpNewActivity extends AppCompatActivity implements View.OnClickListener {

    CustomTextView signup_close_btn;
    Button submit_btn;

    TextInputLayout _name_TL, _email_TL, _mobile_TL, _city_TL, _hospital_name_TL, _password_TL, _confirmpassword_TL;
    CustomEditText _edt_designation, _edt_name, _edt_email, _edt_mobile, _edt_city, _edt_hospital_name, _edt_password, _edt_confirmpassword;
    String get_designation, get_name, get_email, get_mobile, get_city, get_hospital_name, get_password, get_confirm_password, get_country_name,get_country_code ;
    String NEXT_USERNAME;
    String get_specializationName;
    int get_specializationId;
    List<SpecializationList> specializationListArraylist;
    SpecializationList specialization;
    Spinner specialization_spinner;
    CheckBox checkbox_terms;
    String TERMS_CONDITION = "0";
    CustomTextView txt_termsCon;
    String TERMS_CONDITION_URL = "https://pixeleyecare.com/Terms-And-Conditions";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_new);

        initialization(savedInstanceState);
    }

    private void initialization(Bundle savedInstanceState) {
        signup_close_btn = (CustomTextView) findViewById(R.id.signupnew_close);
        signup_close_btn.setOnClickListener(this);

        submit_btn = (Button) findViewById(R.id.signupnew_submit);
        submit_btn.setOnClickListener(this);

        checkbox_terms = (CheckBox)  findViewById(R.id.checkBox_tems);
        txt_termsCon = (CustomTextView)  findViewById(R.id.signup_new_terms);
        txt_termsCon.setOnClickListener(this);


        _name_TL = (TextInputLayout) findViewById(R.id.signupnew_name_layout);
        _email_TL = (TextInputLayout) findViewById(R.id.signupnew_email_layout);
        _mobile_TL = (TextInputLayout) findViewById(R.id.signupnew_mobile_layout);
        _city_TL = (TextInputLayout) findViewById(R.id.signupnew_city_layout);
        _hospital_name_TL = (TextInputLayout) findViewById(R.id.signupnew_hospitalName_layout);

        _edt_designation = (CustomEditText) findViewById(R.id.signupnew_designation);
        _edt_name = (CustomEditText) findViewById(R.id.signupnew_name);
        _edt_email = (CustomEditText) findViewById(R.id.signupnew_email);
        _edt_mobile = (CustomEditText) findViewById(R.id.signupnew_mobile);
        _edt_city = (CustomEditText) findViewById(R.id.signupnew_city);
        _edt_hospital_name = (CustomEditText) findViewById(R.id.signupnew_hospitalName);

        specialization_spinner = (Spinner) findViewById(R.id.signupnew_specialization_spinner);

        if (NetworkUtil.getConnectivityStatusString(SignUpNewActivity.this).equalsIgnoreCase("enabled")) {
            BackgroundTask task = new BackgroundTask(SignUpNewActivity.this);
            task.execute();
        } else {
            AppUtils.showCustomAlertMessage(SignUpNewActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
        }

        checkbox_terms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    TERMS_CONDITION = "1";
                    //  Toast.makeText(SignUpNewActivity.this, " Clicked ", Toast.LENGTH_SHORT).show();
                } else {
                    //not checked
                    TERMS_CONDITION = "0";
                    //  Toast.makeText(SignUpNewActivity.this, " Not Clicked ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signupnew_close:
                Intent back_login = new Intent(SignUpNewActivity.this, LoginActivity.class);
                startActivity(back_login);
                finish();
                break;
            case R.id.signupnew_submit:

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(submit_btn.getWindowToken(), 0);

                signupFormValidation();
                break;
            case R.id.signup_new_terms:

                InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(txt_termsCon.getWindowToken(), 0);

                Intent getWebPage = new Intent(Intent.ACTION_VIEW, Uri.parse(TERMS_CONDITION_URL));
                try {
                    startActivity(getWebPage);
                }catch (Exception e){

                }
                break;
        }
    }

    private void signupFormValidation() {

        final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(

                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        );

        get_designation = _edt_designation.getText().toString();
        get_name = _edt_name.getText().toString();
        get_email = _edt_email.getText().toString();
        get_mobile = _edt_mobile.getText().toString();
        get_city = _edt_city.getText().toString();
        get_hospital_name = _edt_hospital_name.getText().toString();

        if ((get_name.equals(""))) {
            _name_TL.setError("Username is required");
        } else if ((get_mobile.equals("")) || (get_mobile.length() < 10)) {
            _name_TL.setError(null);
            _mobile_TL.setError("10 digits Mobile Number is required");
        }  else if (get_email.equals("")) {
            _name_TL.setError(null);
            _mobile_TL.setError(null);
            _email_TL.setError("Email is required");
        } else if (get_hospital_name.equals("")) {
            _name_TL.setError(null);
            _email_TL.setError(null);
            _mobile_TL.setError(null);
            _hospital_name_TL.setError("Hospital Name is required");
        }  else if (get_city.equals("")) {
            _name_TL.setError(null);
            _email_TL.setError(null);
            _mobile_TL.setError(null);
            _hospital_name_TL.setError(null);
            _city_TL.setError("City Name is required");
        } else if ((get_specializationName.equals("--Select--") || (get_specializationId == 0))) {
            _name_TL.setError(null);
            _email_TL.setError(null);
            _mobile_TL.setError(null);
            _hospital_name_TL.setError(null);
            _city_TL.setError(null);
            Toast.makeText(SignUpNewActivity.this, "Select Specialization", Toast.LENGTH_SHORT).show();
        }
        else if(TERMS_CONDITION.equals("0")) {
            _name_TL.setError(null);
            _email_TL.setError(null);
            _mobile_TL.setError(null);
            _hospital_name_TL.setError(null);
            _city_TL.setError(null);
            Toast.makeText(SignUpNewActivity.this, "Accept Terms and Conditions", Toast.LENGTH_SHORT).show();
        }
        else {
            _name_TL.setError(null);
            _email_TL.setError(null);
            _mobile_TL.setError(null);
            _city_TL.setError(null);
            _hospital_name_TL.setError(null);

            //   Toast.makeText(SignUpNewActivity.this, "Success " +get_designation+get_name, Toast.LENGTH_SHORT).show();
            get_name = get_designation+get_name;
            NEXT_USERNAME = get_name;

            if(EMAIL_ADDRESS_PATTERN.matcher(get_email).matches()) {
                if (NetworkUtil.getConnectivityStatusString(SignUpNewActivity.this).equalsIgnoreCase("enabled")) {
                    sendSignUpRequestToServer(get_name, get_mobile, get_email, get_hospital_name, get_city, get_specializationId, get_specializationName);
                } else {
                    AppUtils.showCustomAlertMessage(SignUpNewActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
            }
            else {
                Toast.makeText(SignUpNewActivity.this, "Invalid Email ID", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;

        public BackgroundTask(Activity activity) {
            progressDialog = new ProgressDialog(SignUpNewActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
            progressDialog.setIndeterminate(true);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading Specialization...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if ((progressDialog != null) || (progressDialog.isShowing())) {
                progressDialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            specializationListArraylist = new ArrayList<>();
            int socketTimeout = 10000; // 10 seconds.
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_SPECIALIZATION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG, response.toString());
                            if (response != null) {
                                JSONObject jsonObject = null;
                                JSONArray jsonArray = null;
                                ArrayList<String> specializationArray = new ArrayList<String>();
                                try {
                                    jsonObject = new JSONObject(response);
                                    String staus_res = jsonObject.getString("status");
                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray = jsonObject.getJSONArray("specialization_details");
                                        if (jsonArray.length() > 0) {
                                            specializationArray.add("--Select--");
                                            specialization = new SpecializationList(0, "--Select--", 0, "0");
                                            specializationListArraylist.add(specialization);
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                specialization = new SpecializationList(jsonArray.getJSONObject(i).getInt("spec_id"), jsonArray.getJSONObject(i).getString("spec_name"), 0, "0");
                                                specializationListArraylist.add(specialization);
                                                specializationArray.add(jsonArray.getJSONObject(i).getString("spec_name"));
                                            }
                                        }


                                        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(SignUpNewActivity.this, R.layout.spinner_text_white, specializationArray);
                                        locationAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                                        specialization_spinner.setAdapter(locationAdapter);
                                        specialization_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view,
                                                                       int position, long id) {
                                                Log.d(Utils.TAG, "specName: " + (String) parent.getItemAtPosition(position));
                                                String specialization_name = (String) parent.getItemAtPosition(position);
                                                get_specializationName = (String) parent.getItemAtPosition(position);
                                                get_specializationId = specializationListArraylist.get(position).getSpecializationId();
                                                Log.d(Utils.TAG, "SpecId " + get_specializationId);

                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {
                                                get_specializationName = "--Select--";
                                                get_specializationId = 0;
                                                Log.d(Utils.TAG, "SpecIdNothing " + get_specializationId);
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
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                    map.put(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);
                    return map;
                }
            };
            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = AppController.getInstance(SignUpNewActivity.this).
                    getRequestQueue();
            AppController.getInstance(SignUpNewActivity.this).addToRequestQueue(stringRequest);

            return null;
        }
    }

    private void sendSignUpRequestToServer(final String get_name, final String get_mobile, final String get_email, final String get_hospital_name, final String get_city, final int get_specializationId, final String get_specializationName) {

        new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;
            ProgressDialog progressDialog1;
            String mobile_number, otp_number;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog1 = new ProgressDialog(SignUpNewActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                progressDialog1.setIndeterminate(true);
                progressDialog1.setMessage("Please wait...");
                progressDialog1.show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {
                    JSONObject jsonObject = JSONParser.uploadSignUpNew(get_name,get_mobile, get_email, get_hospital_name, get_city, get_specializationId, get_specializationName);

                    if (jsonObject != null) {
                        Log.e(Utils.TAG, " GET: " + jsonObject.getString("result"));
                        if (jsonObject.getString("result").equals("success")) {
                            status = true;
                        } else {
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
                if (progressDialog1 != null)
                    progressDialog1.dismiss();
                Log.e(Utils.TAG, "aBoolean: " + String.valueOf(aBoolean));
                if (aBoolean) {
                    AppUtils.showCustomSuccessMessage(SignUpNewActivity.this, "Congratulations!", "Your registration has been successful. \nPixel team shall verify and send the username and password to your Email ID & SMS ", "OK", null, null);
                    get_designation = _edt_designation.getText().toString();
                    _edt_name.setText("");
                    _edt_email.setText("");
                    _edt_mobile.setText("");
                    _edt_city.setText("");
                    _edt_hospital_name.setText("");

                    //  Toast.makeText(SignUpNewActivity.this,"Registered Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpNewActivity.this, "User already exists !!! \nPlease login to your account.", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();

    }

}
