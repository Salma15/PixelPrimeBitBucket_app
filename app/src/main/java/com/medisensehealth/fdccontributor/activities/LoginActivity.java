package com.medisensehealth.fdccontributor.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import com.medisensehealth.fdccontributor.DataModel.DoctorHistory;
import com.medisensehealth.fdccontributor.DataModel.HospitalList;
import com.medisensehealth.fdccontributor.DataModel.MarketingPersonHistory;
import com.medisensehealth.fdccontributor.DataModel.PartnerHistory;
import com.medisensehealth.fdccontributor.DataModel.SpecializationList;
import com.medisensehealth.fdccontributor.R;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by medisense on 15/01/18.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String ENDPOINT = "https://kylewbanks.com/rest/posts.json";
    private RequestQueue requestQueue;

    TextInputLayout _username_TL, _pssword_TL;
    CustomEditText _edit_mobileNum, _edit_password;
    CustomTextView _forgot_password, _signup_btn, _signin_btn;
    ImageView password_imagebtn;

    HashMap<String, String> hashMap_login;
    public static String login_type;
    public static int login_userid;

    private int partner_id_int, partner_hospitalID_int;
    private String partner_name_string, history_string,Address_string,Email_id_string,Email_id1_string,Email_id2_string,contact_person_string,person_position_string,
            landline_num_string,cont_num1_string,cont_num2_string,website_string,location_string,state_string,country_string,Type_string,password_string,reg_date_string,
            partner_logo_string, partner_gcmtoken_string, partner_encryprt_userid, partner_specialization_name;

    public static int ref_id_int,Total_Referred_int,Tot_responded_int,doc_type_val_int,doc_spec_int,doc_age_int,
            anonymous_status_int,company_id_int, doc_hospitalID_int;
    public static String ref_name_string,doc_type_string,ref_web_string,doc_gen_string,doc_qual_string,doc_city_string,
            doc_state_string,doc_country_string, ref_address_string,ref_exp_string,doc_interest_string,
            doc_research_string,doc_contribute_string,doc_pub_string,in_op_cost_string,on_op_cost_string,
            cons_charge_string, doc_keywords_string,contact_num_string,doc_photo_string,doc_password_string,
            doc_gcmtoken_string, doc_encryprt_userid, doc_specialization_name;

    public static int market_person_id_int, matket_person_hospitalid_int;
    public static String market_person_name_string, market_person_email_string, market_person_mobile_string, market_person_password_string, market_person_gcmtoken_string;


    public static final String KEY_MOBILENUM="contact_num";
    public static final String KEY_PASSWORD="doc_password";
    public static final String KEY_API_KEY = "API_KEY";
    ArrayList<DoctorHistory> doctorArraylist;
    ArrayList<PartnerHistory> partnerArraylist;
    ArrayList<MarketingPersonHistory> marketPersonArraylist;
    ArrayList<SpecializationList> specilizationDocArraylist;
    ArrayList<HospitalList> hospitalDocArraylist;

    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;

    // CGM Registration
    public static String GCM_SenderID;
    //Creating a broadcast receiver for gcm registration
    public BroadcastReceiver mRegistrationBroadcastReceiver;
    int passwordFlag = 0;
    ProgressDialog mProgressDialog;
    int HOSPITAL_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        requestQueue = Volley.newRequestQueue(getApplicationContext());
//        fetchPosts();
        
        initializwViews(savedInstanceState);
    }

    private void initializwViews(Bundle savedInstanceState) {
        shareadPreferenceClass = new ShareadPreferenceClass(this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(this);
        passwordFlag = 0;
        HOSPITAL_ID = 0;

        _username_TL = (TextInputLayout) findViewById(R.id.input_layout_username);
        _pssword_TL = (TextInputLayout) findViewById(R.id.input_layout_password);
        _edit_mobileNum = (CustomEditText) findViewById(R.id.login_username);
        _edit_password = (CustomEditText) findViewById(R.id.login_password);
        _forgot_password = (CustomTextView) findViewById(R.id.login_forgotpassword);
        _signup_btn = (CustomTextView) findViewById(R.id.login_signup);
        _signin_btn = (CustomTextView) findViewById(R.id.login_signin);
        password_imagebtn = (ImageView) findViewById(R.id.login_password_img);
        _forgot_password.setOnClickListener(this);
        _signup_btn.setOnClickListener(this);
        _signin_btn.setOnClickListener(this);
        password_imagebtn.setOnClickListener(this);
        _username_TL.requestFocusFromTouch();
        _pssword_TL.requestFocusFromTouch();

        GCMRegistrationProcess();

        _edit_mobileNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        _edit_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        _edit_mobileNum.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                _edit_mobileNum.setFocusable(true);
                _edit_mobileNum.setFocusableInTouchMode(true);
                return false;
            }
        });

        _edit_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                _edit_password.setFocusable(true);
                _edit_password.setFocusableInTouchMode(true);
                return false;
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_forgotpassword:
                Intent forgotpass = new Intent(LoginActivity.this, ForgotPasswordActicity.class);
                startActivity(forgotpass);
                finish();
                break;
            case R.id.login_signup:
                if (NetworkUtil.getConnectivityStatusString(LoginActivity.this).equalsIgnoreCase("enabled")) {
                    Intent signup = new Intent(LoginActivity.this, SignUpNewActivity.class);
                    startActivity(signup);
                } else {
                    AppUtils.showCustomAlertMessage(LoginActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
                finish();
                break;
            case R.id.login_signin:

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(_signin_btn.getWindowToken(), 0);

                submitFormValidation();
                break;
            case R.id.login_password_img:
                if(passwordFlag==1) {
                    passwordFlag = 0;
                    password_imagebtn.setImageResource(R.mipmap.password_visible);
                    _edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    _edit_password.setSelection(_edit_password.getText().length());
                }
                else if(passwordFlag==0) {
                    passwordFlag = 1;
                    password_imagebtn.setImageResource(R.mipmap.password_invisible);
                    _edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    _edit_password.setSelection(_edit_password.getText().length());
                }
                break;
        }
    }

    private void submitFormValidation() {
        String username = _edit_mobileNum.getText().toString();
        String password = _edit_password.getText().toString();

        if((username.equals("")))
        {
            _username_TL.setError("Mobile number or Email ID is required");
        }
        else if(password.equals("")) {
            _username_TL.setError(null);
            _pssword_TL.setError("Password is required");
        }
        else {
            _username_TL.setError(null);
            _pssword_TL.setError(null);
//            hashMap_login = new HashMap<String, String>();
//            hashMap_login.put("API_KEY", APIClass.API_KEY);
//            hashMap_login.put("contact_num",_edit_mobileNum.getText().toString());
//            hashMap_login.put("doc_password", _edit_password.getText().toString());

            if (NetworkUtil.getConnectivityStatusString(this).equalsIgnoreCase("enabled")) {
                setLoginRequestToServer(username, password);
            } else {
                AppUtils.showCustomAlertMessage(LoginActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }
        }

    }

    private void setLoginRequestToServer(String username, String password) {
      //  requestQueue = Volley.newRequestQueue(getApplicationContext());

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Signing........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_DOCTORS_LOGIN, onPostsLoaded, onPostsError) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(KEY_API_KEY,APIClass.API_KEY);
                map.put(KEY_MOBILENUM,_edit_mobileNum.getText().toString().trim());
                map.put(KEY_PASSWORD,_edit_password.getText().toString().trim());
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(this).getRequestQueue();
        AppController.getInstance(this).addToRequestQueue(stringRequest);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            mProgressDialog.dismiss();
            Log.d(Utils.TAG, " login: "+ response);
            if (response != null) {
                GetJsonArrayResponse(response);
            }
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(Utils.TAG, " loginErr: "+ error.toString());
            mProgressDialog.dismiss();
        }
    };

    private void GetJsonArrayResponse(String response) {
        try{
            doctorArraylist = new ArrayList<>();
            partnerArraylist = new ArrayList<>();
            marketPersonArraylist = new ArrayList<>();
            specilizationDocArraylist = new ArrayList<>();
            hospitalDocArraylist = new ArrayList<>();
            HOSPITAL_ID = 0;

            JSONObject jsonObject = new JSONObject(response);

            String staus_res = jsonObject.getString("status");
            String USER_ENCRYPT_ID = jsonObject.getString("user_encrypt_id");
            String login_type_res = jsonObject.getString("usertype");

            if( (staus_res.equals("false")) || ( (staus_res.equals("true") && (login_type_res.equals("0")))))
            {
                AppUtils.showCustomAlertMessage(LoginActivity.this, "SIGN IN ","Invalid Mobile Number/Email ID or Password", "OK", null, null);
            }
            else {

                JSONArray jsonArray = jsonObject.getJSONArray("doc_details");
                if(login_type_res.equals("1")) {
                    //   Log.d(Utils.TAG, "In Hospital Login");
                    login_type = login_type_res;
                    String receptionist_username = "", receptionist_password = "";
                    if(jsonArray.length() >0)
                    {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            doctorArraylist.add(new DoctorHistory(jsonArray.getJSONObject(i).getInt("ref_id"),jsonArray.getJSONObject(i).getString("ref_name")
                                    ,jsonArray.getJSONObject(i).getString("doc_type"),jsonArray.getJSONObject(i).getString("ref_web"),jsonArray.getJSONObject(i).getString("doc_gen")
                                    ,jsonArray.getJSONObject(i).getString("doc_qual"),jsonArray.getJSONObject(i).getString("doc_city"),jsonArray.getJSONObject(i).getString("doc_state")
                                    ,jsonArray.getJSONObject(i).getString("doc_country"),jsonArray.getJSONObject(i).getString("ref_address"),jsonArray.getJSONObject(i).getString("ref_exp")
                                    ,jsonArray.getJSONObject(i).getString("doc_interest"),jsonArray.getJSONObject(i).getString("doc_research"),jsonArray.getJSONObject(i).getString("doc_contribute")
                                    ,jsonArray.getJSONObject(i).getString("doc_pub"),jsonArray.getJSONObject(i).getString("in_op_cost"),jsonArray.getJSONObject(i).getString("on_op_cost")
                                    ,jsonArray.getJSONObject(i).getString("cons_charge"),jsonArray.getJSONObject(i).getString("doc_keywords"),jsonArray.getJSONObject(i).getString("contact_num")
                                    ,jsonArray.getJSONObject(i).getString("doc_photo"),jsonArray.getJSONObject(i).getString("doc_password"),jsonArray.getJSONObject(i).getInt("Total_Referred")
                                    ,jsonArray.getJSONObject(i).getInt("Tot_responded"),jsonArray.getJSONObject(i).getInt("doc_type_val"),jsonArray.getJSONObject(i).getInt("doc_spec")
                                    ,jsonArray.getJSONObject(i).getInt("doc_age"),jsonArray.getJSONObject(i).getInt("anonymous_status"),
                                    jsonArray.getJSONObject(i).getString("gcm_tokenid"), USER_ENCRYPT_ID, jsonArray.getJSONObject(i).getString("spec_name")));

                            receptionist_username = jsonArray.getJSONObject(i).getString("secretary_username");
                            receptionist_password = jsonArray.getJSONObject(i).getString("secretary_password");
                        }
                    }

                    JSONArray jsonArray1 = jsonObject.getJSONArray("doc_specialization");
                    if(jsonArray1.length() >0) {
                        for (int j = 0; j < jsonArray1.length(); j++) {
                            specilizationDocArraylist.add(new SpecializationList(jsonArray1.getJSONObject(j).getInt("spec_id"),jsonArray1.getJSONObject(j).getString("spec_name"),
                                    jsonArray1.getJSONObject(j).getInt("doc_id"),"1",jsonArray1.getJSONObject(j).getInt("spec_group_id")));
                        }

                    }

                    JSONArray jsonArray2 = jsonObject.getJSONArray("doc_hospital");
                    if(jsonArray2.length() >0) {
                        for (int k = 0; k < jsonArray2.length(); k++) {
                            hospitalDocArraylist.add(new HospitalList(jsonArray2.getJSONObject(k).getInt("hosp_id"),
                                    jsonArray2.getJSONObject(k).getString("hosp_name"),
                                    jsonArray2.getJSONObject(k).getString("hosp_addrs"),jsonArray2.getJSONObject(k).getString("hosp_city"),
                                    jsonArray2.getJSONObject(k).getString("hosp_state"),jsonArray2.getJSONObject(k).getString("hosp_country"),
                                    jsonArray2.getJSONObject(k).getInt("doc_id"),"1"));
                        }

                        HOSPITAL_ID = hospitalDocArraylist.get(0).getHospitalId();
                    }

                    if(doctorArraylist.size() > 0) {
                        for (int i = 0; i < doctorArraylist.size(); i++) {
                            ref_name_string = doctorArraylist.get(i).getRefname();
                            doc_type_string = doctorArraylist.get(i).getDocType();
                            ref_web_string = doctorArraylist.get(i).getRefweb();
                            doc_gen_string = doctorArraylist.get(i).getDocgen();
                            doc_qual_string = doctorArraylist.get(i).getDocqual();
                            doc_city_string = doctorArraylist.get(i).getDoccity();
                            doc_state_string = doctorArraylist.get(i).getDocstate();
                            doc_country_string = doctorArraylist.get(i).getDocscountry();
                            ref_address_string = doctorArraylist.get(i).getRefaddress();
                            ref_exp_string = doctorArraylist.get(i).getRefexp();
                            doc_interest_string = doctorArraylist.get(i).getDocinterest();
                            doc_research_string = doctorArraylist.get(i).getDocresearch();
                            doc_contribute_string = doctorArraylist.get(i).getDoccontribute();
                            doc_pub_string = doctorArraylist.get(i).getDocpub();
                            in_op_cost_string = doctorArraylist.get(i).getInopcost();
                            on_op_cost_string = doctorArraylist.get(i).getOnopcost();
                            cons_charge_string = doctorArraylist.get(i).getConscharge();
                            doc_keywords_string = doctorArraylist.get(i).getDockeywords();
                            contact_num_string = doctorArraylist.get(i).getContactnum();
                            doc_photo_string = doctorArraylist.get(i).getDocphoto();
                            doc_password_string = doctorArraylist.get(i).getDocpassword();
                            ref_id_int = doctorArraylist.get(i).getRefId();
                            Total_Referred_int = doctorArraylist.get(i).getTotalreferred();
                            Tot_responded_int = doctorArraylist.get(i).getTotresponded();
                            doc_type_val_int = doctorArraylist.get(i).getDoctypeval();
                            doc_spec_int = doctorArraylist.get(i).getDocspec();
                            doc_age_int = doctorArraylist.get(i).getDocage();
                            anonymous_status_int = doctorArraylist.get(i).getAnanymousstatus();
                            company_id_int = doctorArraylist.get(i).getCompanyid();
                            doc_gcmtoken_string =  doctorArraylist.get(i).getDocGCMTokenId();
                            doc_encryprt_userid = doctorArraylist.get(i).getRefEncyptID();
                            doc_specialization_name = doctorArraylist.get(i).getRefSpecializationName();
                            System.out.println(Utils.TAG +" gcm doc: " + doc_gcmtoken_string);
                            login_userid = ref_id_int;
                            if(doc_gcmtoken_string.equals("")) {
                                Log.d(Utils.TAG, "Hospital Doc empty GCM ID");
                                new UpdateGCMToken().execute("");
                            }
                            else {
                                if(doc_gcmtoken_string.equalsIgnoreCase(GCM_SenderID)) {
                                    Log.d(Utils.TAG, "Hospital DocGCM equals");
                                }
                                else {
                                    Log.d(Utils.TAG, "Hospital Doc Update GCM Token");
                                    new UpdateGCMToken().execute("");
                                }
                            }
                        }
                    }
                    if (sharedPreferences != null) {
                        //	Log.d(Utils.TAG,sharedPreferences.toString());
                        shareadPreferenceClass.hospitalDoctor_PreLogin(login_type,ref_name_string,doc_type_string,ref_web_string,doc_gen_string,doc_qual_string,
                                doc_city_string,doc_state_string,doc_country_string,ref_address_string,ref_exp_string,doc_interest_string,
                                doc_research_string,doc_contribute_string,doc_pub_string,in_op_cost_string,on_op_cost_string,cons_charge_string,
                                doc_keywords_string,contact_num_string,doc_photo_string,doc_password_string,ref_id_int,Total_Referred_int,Tot_responded_int,
                                doc_type_val_int,doc_spec_int,doc_age_int,anonymous_status_int,company_id_int, doc_gcmtoken_string, doc_encryprt_userid, doc_specialization_name);

                        //Set Doctor Specializations
                        Gson gson = new Gson();
                        String jsonText = gson.toJson(specilizationDocArraylist);
                        shareadPreferenceClass.clearDocSpecializations();
                        shareadPreferenceClass.setDocSpecializationsList(jsonText);

                        Gson gson2 = new Gson();
                        String jsonText1 = gson2.toJson(hospitalDocArraylist);
                        shareadPreferenceClass.clearDocHoapitals();
                        shareadPreferenceClass.setDocHoapitals(jsonText1);

                        shareadPreferenceClass.clearDocHoapitalID();
                        shareadPreferenceClass.setDocHoapitalID(HOSPITAL_ID);

                        shareadPreferenceClass.clearDocReceptionistUsername();
                        shareadPreferenceClass.setDocReceptionistUsername(receptionist_username);

                        shareadPreferenceClass.clearDocReceptionistPassword();
                        shareadPreferenceClass.setDocReceptionistPassword(receptionist_password);


                        boolean isFirstRunShow = sharedPreferences.getBoolean("FIRSTRUNSHOECASE", true);
                        if (isFirstRunShow)
                        {

                          /*  Intent intent = new Intent(LoginActivity.this, ShowCaseViewActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            finish();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("FIRSTRUNSHOECASE", false);
                            editor.commit();*/
                            final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Welcome!");
                            builder.setMessage("Would you like to go through a quick tour which explains all the features?");
                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    Intent intent = new Intent(LoginActivity.this, ShowCaseViewActivity.class);
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
                                    Intent intent_Doc = new Intent(LoginActivity.this, DashboardActivity.class);
                                    intent_Doc.putExtra("LOGIN_TYPE", login_type);
                                    intent_Doc.putExtra("USER_ID", login_userid);
                                    intent_Doc.putExtra("ENTRY_TYPE", "NORMAL");
                                    startActivity(intent_Doc);
                                    finish();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("FIRSTRUNSHOECASE", false);
                                    editor.commit();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else {
                            //	Log.d(Utils.TAG, "Goto Dashboard Activity - - Hospital Doctors Activity");
                            Intent intent_Doc = new Intent(LoginActivity.this, DashboardActivity.class);
                            intent_Doc.putExtra("LOGIN_TYPE", login_type);
                            intent_Doc.putExtra("USER_ID", login_userid);
                            intent_Doc.putExtra("ENTRY_TYPE", "NORMAL");
                            startActivity(intent_Doc);
                            overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            finish();
                        }
                    }
                }
                else if(login_type_res.equals("2")) {
                    //    Log.d(Utils.TAG, "In Referring Partner Login");
                    login_type = login_type_res;

                    if(jsonArray.length() >0)
                    {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            partnerArraylist.add(new PartnerHistory(jsonArray.getJSONObject(i).getInt("partner_id"),jsonArray.getJSONObject(i).getString("partner_name")
                                    ,jsonArray.getJSONObject(i).getString("history"),jsonArray.getJSONObject(i).getString("Address"),jsonArray.getJSONObject(i).getString("Email_id")
                                    ,jsonArray.getJSONObject(i).getString("Email_id1"),jsonArray.getJSONObject(i).getString("Email_id2"),jsonArray.getJSONObject(i).getString("contact_person")
                                    ,jsonArray.getJSONObject(i).getString("person_position"),"",jsonArray.getJSONObject(i).getString("cont_num1")
                                    ,jsonArray.getJSONObject(i).getString("cont_num2"),jsonArray.getJSONObject(i).getString("website"),jsonArray.getJSONObject(i).getString("location")
                                    ,jsonArray.getJSONObject(i).getString("state"),jsonArray.getJSONObject(i).getString("country"),jsonArray.getJSONObject(i).getString("Type")
                                    ,jsonArray.getJSONObject(i).getString("password"),jsonArray.getJSONObject(i).getString("reg_date"),jsonArray.getJSONObject(i).getString("partner_logo")
                                    ,jsonArray.getJSONObject(i).getString("gcm_tokenid"), USER_ENCRYPT_ID, jsonArray.getJSONObject(i).getString("spec_name")));

                        }

                    }
                    if(partnerArraylist.size() > 0) {
                        for (int i = 0; i < partnerArraylist.size(); i++) {
                            partner_id_int = partnerArraylist.get(i).getPartnerId();
                            partner_name_string = partnerArraylist.get(i).getPartnername();
                            history_string = partnerArraylist.get(i).getPartnerHistory();
                            Address_string = partnerArraylist.get(i).getPartnerAddress();
                            Email_id_string = partnerArraylist.get(i).getPartnerEmail();
                            Email_id1_string = partnerArraylist.get(i).getPartnerEmail1();
                            Email_id2_string = partnerArraylist.get(i).getPartnerEmail2();
                            contact_person_string = partnerArraylist.get(i).getPartnerContactPerson();
                            person_position_string = partnerArraylist.get(i).getPartnerPersonPosition();
                            landline_num_string = partnerArraylist.get(i).getPartnerLandline();
                            cont_num1_string = partnerArraylist.get(i).getPartnerContactnum1();
                            cont_num2_string = partnerArraylist.get(i).getPartnerContactnum2();
                            website_string = partnerArraylist.get(i).getPartnerWebsite();
                            location_string = partnerArraylist.get(i).getPartnerLocation();
                            state_string = partnerArraylist.get(i).getPartnerState();
                            country_string = partnerArraylist.get(i).getPartnerCountry();
                            Type_string = partnerArraylist.get(i).getPartnerType();
                            password_string = partnerArraylist.get(i).getPartnerPassword();
                            reg_date_string = partnerArraylist.get(i).getPartnerRegDate();
                            partner_logo_string = partnerArraylist.get(i).getPartnerLogo();
                            partner_gcmtoken_string = partnerArraylist.get(i).getPartnerGCMTokenId();
                            System.out.println(Utils.TAG +" gcm part: " + partner_gcmtoken_string);
                            login_userid = partner_id_int;

                            if(partner_gcmtoken_string.equals("")) {
                                Log.d(Utils.TAG, "Partner empty GCM ID");
                                new UpdateGCMToken().execute("");
                            }
                            else {
                                if(partner_gcmtoken_string.equalsIgnoreCase(GCM_SenderID)) {
                                    Log.d(Utils.TAG, "Partner GCM equals");
                                }
                                else {
                                    Log.d(Utils.TAG, "Partner Update GCM Token");
                                    new UpdateGCMToken().execute("");
                                }
                            }
                        }
                    }
                    if (sharedPreferences != null) {

                        shareadPreferenceClass.referPartnerPreLogin(login_type,partner_id_int,partner_name_string,history_string,Address_string,
                                Email_id_string,Email_id1_string,Email_id2_string,contact_person_string,person_position_string,
                                landline_num_string,cont_num1_string,cont_num2_string,website_string,location_string,state_string,
                                country_string,Type_string,password_string,reg_date_string,partner_logo_string,partner_gcmtoken_string, partner_encryprt_userid, partner_specialization_name);


                        //	Log.d(Utils.TAG, "Goto Dashboard Activity - Referring Doctors Activity");
                        Intent intent_partner = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent_partner.putExtra("LOGIN_TYPE", login_type);
                        intent_partner.putExtra("USER_ID", login_userid);
                        intent_partner.putExtra("ENTRY_TYPE", "NORMAL");
                        startActivity(intent_partner);
                        finish();
                    }
                }
                else if(login_type_res.equals("3")) {
                    //   Log.d(Utils.TAG, "In Marketing Login");
                    login_type = login_type_res;
                    if(jsonArray.length() >0)
                    {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            marketPersonArraylist.add(new MarketingPersonHistory(jsonArray.getJSONObject(i).getInt("person_id"),jsonArray.getJSONObject(i).getString("person_name")
                                    ,jsonArray.getJSONObject(i).getInt("hosp_id"),jsonArray.getJSONObject(i).getString("person_mobile"),jsonArray.getJSONObject(i).getString("person_email")
                                    ,jsonArray.getJSONObject(i).getString("password"),jsonArray.getJSONObject(i).getString("gcm_tokenid")));

                        }
                    }

                    if(marketPersonArraylist.size() > 0) {
                        for (int i = 0; i < marketPersonArraylist.size(); i++) {
                            market_person_id_int = marketPersonArraylist.get(i).getMarketPersonId();
                            market_person_name_string = marketPersonArraylist.get(i).getMarketPersonName();
                            matket_person_hospitalid_int = marketPersonArraylist.get(i).getMarketPersonHospitalId();
                            market_person_email_string = marketPersonArraylist.get(i).getMarketPersonEmail();
                            market_person_mobile_string = marketPersonArraylist.get(i).getMarketPersonMobile();
                            market_person_password_string = marketPersonArraylist.get(i).getMarketPersonPassword();
                            market_person_gcmtoken_string = marketPersonArraylist.get(i).getMarketPersonGCMTokenId();
                            System.out.println(Utils.TAG +" gcm market: " + market_person_gcmtoken_string);
                            Log.d(Utils.TAG, "GCM_SenderID: "+GCM_SenderID);
                            login_userid = market_person_id_int;

                            if(market_person_gcmtoken_string.equals("")) {
                                Log.d(Utils.TAG, "Merketing empty GCM ID");
                                new UpdateGCMToken().execute("");
                            }
                            else {
                                if(market_person_gcmtoken_string.equalsIgnoreCase(GCM_SenderID)) {
                                    Log.d(Utils.TAG, "Marketing GCM equals");
                                }
                                else {
                                    Log.d(Utils.TAG, "Marketing Update GCM Token");
                                    new UpdateGCMToken().execute("");
                                }
                            }
                        }
                    }
                    if (sharedPreferences != null) {

                        shareadPreferenceClass.marketingPersonPreLogin(login_type,market_person_id_int,market_person_name_string,
                                matket_person_hospitalid_int,market_person_mobile_string,market_person_email_string,
                                market_person_password_string,market_person_gcmtoken_string);

                        boolean isFirstRunShow = sharedPreferences.getBoolean("FIRSTRUNSHOECASE", true);
                        if (isFirstRunShow)
                        {

                           /* Intent intent = new Intent(LoginActivity.this, ShowCaseViewActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            finish();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("FIRSTRUNSHOECASE", false);
                            editor.commit();*/
                            final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Welcome!");
                            builder.setMessage("Would you like to go through a quick tour which explains all the features?");
                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    Intent intent = new Intent(LoginActivity.this, ShowCaseViewActivity.class);
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
                                    Intent intent_Doc = new Intent(LoginActivity.this, DashboardActivity.class);
                                    intent_Doc.putExtra("LOGIN_TYPE", login_type);
                                    intent_Doc.putExtra("USER_ID", login_userid);
                                    intent_Doc.putExtra("ENTRY_TYPE", "NORMAL");
                                    startActivity(intent_Doc);
                                    finish();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("FIRSTRUNSHOECASE", false);
                                    editor.commit();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else {
                            //	Log.d(Utils.TAG, "Goto Dashboard Activity - - Hospital Doctors Activity");
                            Intent intent_market = new Intent(LoginActivity.this, DashboardActivity.class);
                            intent_market.putExtra("LOGIN_TYPE", login_type);
                            intent_market.putExtra("USER_ID", login_userid);
                            intent_market.putExtra("ENTRY_TYPE", "NORMAL");
                            startActivity(intent_market);
                            overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            finish();
                        }
                    }
                }
            }
        }
        catch (Exception e){
            AppUtils.showCustomAlertMessage(LoginActivity.this, HCConstants.INTERNET,HCConstants.INTERNET_CHECK, "OK", null, null);
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
                    map.put(APIClass.KEY_USER_LOGINTYPE,login_type);
                    map.put(APIClass.KEY_USER_USERID, String.valueOf(login_userid));
                    map.put(APIClass.KEY_USER_GCMTOKEN,GCM_SenderID);
                    map.put(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);
                    return map;
                }
            };

            RequestQueue requestQueue = AppController.getInstance(LoginActivity.this).
                    getRequestQueue();
            AppController.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
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

    private void GCMRegistrationProcess() {
        //Initializing our broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    //Getting the registration token from the intent
                    String token = intent.getStringExtra("token");
                    //    Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();
                    GCM_SenderID = token;
                    Log.d(Utils.TAG, "GCM_SenderID: " + GCM_SenderID);

                    //if the intent is not with success then displaying error messages
                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    //  Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                    GCM_SenderID = "";
                } else {
                    //    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
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
    protected void onResume() {
        super.onResume();
        Log.w(Utils.TAG, "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        Log.w(Utils.TAG, "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
}
