package com.medisensehealth.fdccontributor.activities.settings;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
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
import com.hbb20.CountryCodePicker;
import com.medisensehealth.fdccontributor.DataModel.HospitalList;
import com.medisensehealth.fdccontributor.DataModel.SpecializationList;
import com.medisensehealth.fdccontributor.DataModel.StateList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.parser.JSONParser;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.SpaceTokenizer;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomEditText;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 11-04-2018.
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME, USER_LOGIN_TYPE;
    List<SpecializationList> specializationListArraylist;
    SpecializationList specialization;
    List<StateList> stateListArraylist;
    StateList state;

    ImageView profile_img, image_upload;
    Button update_profile_btn;
    CustomEditText name, mobile_num, mobile_num2, city, qualification, experience, email, website, areaof_interest, profile_contribution,
            research, publications, opinion_cost, consultation_fees, secretary_phone, secretary_email, teleop_number, videoop_number, timings_opinion;
    AppCompatCheckBox teleop_checkbox, videoop_checkbox;
    CountryCodePicker ccp_country;
    Spinner state_spinner;
    String compareStateValue, compareSpecializationValue;
    int compareSpecializationID;

    String GET_COUNTRY_CODE, GET_COUNTRY_NAME, GET_COUNTRY_NAME_CODE, GET_SPECIALIZATION_NAME, GET_STATE, DOWNLOAD_PROFILE, GET_IMAGEPATH = "";
    String GET_NAME, GET_MOBILE, GET_CITY, GET_SELECT_HOSPITAL, GET_QUALIFICATION, GET_EXPERIENCE, GET_EMAIL, GET_WEBSITE, GET_EXPERTISE,
            GET_CONTRIBUTION, GET_RESEARCH, GET_PUBLICATION;
    int GET_SPEC_ID, GET_HOSPITAL_ID;
    String GET_MOBILE2, GET_ONLINE_OPINION_COST, GET_CONSULTATION_FESS, SECRETARY_PHONE, SECRETARY_EMAIL, READDY_TELE_OP, TELEOP_NUM,
            READY_VIDEO_OP, VIDEO_OP_NUM, AVAILABLE_TIMINGS;

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    private String userChoosenTask;
    private static final int PICK_IMAGE = 1;
    private Bitmap bitmap;

    MultiAutoCompleteTextView multiAutoCompleteTextView, multiAutoCompleteTextViewHospital;
    public ArrayList<String> GET_SPECIALIZATION_ARRAY = new ArrayList<String>();
    String selectedSpecialization;
    ArrayList<String> dataSpec = new ArrayList<String>();
    public ArrayList<String> GET_HOSPITAL_ARRAY = new ArrayList<String>();
    ArrayList<String> dataHospital = new ArrayList<String>();
    List<HospitalList> hospitalListArraylist;
    int TELE_OPINION_STATUS = 0, VIDEO_OPINION_STATUS = 0;

    private static final int SELECT_FILE = 123;
    String GET_IMAGE_FILENAME = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle b = getIntent().getExtras();
        if( b != null) {
            String title = b.getString("title");
            setTitle(title);

        }

        shareadPreferenceClass = new ShareadPreferenceClass(ProfileActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(ProfileActivity.this);
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

            Log.d(Utils.TAG , "******************* My Profile *************");
            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        Log.d(Utils.TAG+"USER_ID: ", String.valueOf(USER_ID));
        Log.d(Utils.TAG+"LOGIN_TYPE: ", USER_LOGIN_TYPE);

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
        GET_SPECIALIZATION_ARRAY = new ArrayList<String>();
        GET_HOSPITAL_ARRAY = new ArrayList<String>();
        TELE_OPINION_STATUS = 0;
        VIDEO_OPINION_STATUS = 0;
        GET_SPEC_ID = 0;
        GET_HOSPITAL_ID = 0;

        permissionStatus = getSharedPreferences("permissionStatus", Context.MODE_PRIVATE);

        profile_img = (ImageView) findViewById(R.id.profile_image);
        image_upload = (ImageView) findViewById(R.id.profile_upload);
        profile_img.setOnClickListener(this);
        image_upload.setOnClickListener(this);
        name = (CustomEditText) findViewById(R.id.profile_name);
        ccp_country = (CountryCodePicker) findViewById(R.id.profile_country);
        mobile_num = (CustomEditText) findViewById(R.id.profile_mobile);
        mobile_num2 = (CustomEditText) findViewById(R.id.profile_mobile2);
        state_spinner = (Spinner) findViewById(R.id.profile_state_spinner);
        city = (CustomEditText) findViewById(R.id.profile_city);
        qualification  = (CustomEditText) findViewById(R.id.profile_qualification);
        experience = (CustomEditText) findViewById(R.id.profile_years_exp);
        email = (CustomEditText) findViewById(R.id.profile_email);
        website = (CustomEditText) findViewById(R.id.profile_website);
        areaof_interest = (CustomEditText) findViewById(R.id.profile_areaofexp);
        profile_contribution = (CustomEditText) findViewById(R.id.profile_prof_contribution);
        research = (CustomEditText) findViewById(R.id.profile_research_details);
        publications = (CustomEditText) findViewById(R.id.profile_publications);
        update_profile_btn = (Button) findViewById(R.id.profile_submit);
        update_profile_btn.setOnClickListener(this);

        opinion_cost = (CustomEditText) findViewById(R.id.profile_online_opinion_cost);
        consultation_fees = (CustomEditText) findViewById(R.id.profile_consultation_charge);
        secretary_phone = (CustomEditText) findViewById(R.id.profile_secretary_phone);
        secretary_email = (CustomEditText) findViewById(R.id.profile_secretary_email);
        teleop_number  = (CustomEditText) findViewById(R.id.profile_teleop_num);
        videoop_number = (CustomEditText) findViewById(R.id.profile_videoop_num);
        timings_opinion = (CustomEditText) findViewById(R.id.profile_timing_op);
        teleop_checkbox = (AppCompatCheckBox) findViewById(R.id.profile_teleop_checkbox);
        videoop_checkbox = (AppCompatCheckBox) findViewById(R.id.profile_videoop_checkbox);


        teleop_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(teleop_checkbox.isChecked())
                {
                    TELE_OPINION_STATUS = 1;
                   // Toast.makeText(ProfileActivity.this,"teleop_checkbox checked", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    TELE_OPINION_STATUS = 0;
                   // Toast.makeText(ProfileActivity.this,"teleop_checkbox Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });

        videoop_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoop_checkbox.isChecked())
                {
                    VIDEO_OPINION_STATUS = 1;
                   // Toast.makeText(ProfileActivity.this,"videoop_checkbox checked", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    VIDEO_OPINION_STATUS = 0;
                   // Toast.makeText(ProfileActivity.this,"videoop_checkbox Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Find the MultiAutoCompleteTextView object.
        multiAutoCompleteTextView = (MultiAutoCompleteTextView)findViewById(R.id.profile_specialty);
        // Must set tokenizer for MultiAutoCompleteTextView object, otherwise it will not take effect.
        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        // Set multiAutoCompleteTextView related attribute value in java code.
        multiAutoCompleteTextView.setPadding(15,15,15,15);
        //  multiAutoCompleteTextView.setBackgroundColor(Color.GREEN);

        multiAutoCompleteTextViewHospital = (MultiAutoCompleteTextView)findViewById(R.id.profile_hospital);
        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        multiAutoCompleteTextView.setPadding(15,15,15,15);

        name.setCursorVisible(false);
        name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                name.setCursorVisible(true);
                return false;
            }
        });

        ccp_country.setContentColor(R.color.white);
        ccp_country.setBackgroundColor(getResources().getColor(R.color.white));
        GET_COUNTRY_CODE = ccp_country.getDefaultCountryCodeWithPlus();
        GET_COUNTRY_NAME = ccp_country.getDefaultCountryName();
        GET_COUNTRY_NAME_CODE = ccp_country.getDefaultCountryNameCode();
        //  new getJsonState().execute(ccp_country.getDefaultCountryNameCode());
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

        getProfileInformation();
    }

    private void getProfileInformation() {
        selectedSpecialization = "";
        GET_SPECIALIZATION_ARRAY = new ArrayList<String>();
        GET_HOSPITAL_ARRAY = new ArrayList<String>();


        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading profile...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_EDIT_PROFILE_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "result: "+ response.toString());
                        if (response != null) try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray JAdocSpecArray = jsonObject.getJSONArray("docSpecResult");
                            dataSpec=new ArrayList<String>();
                            for (int j = 0; j < JAdocSpecArray.length(); j++) {
                                JSONObject JOStuffSpec = JAdocSpecArray.getJSONObject(j);
                                dataSpec.add(JOStuffSpec.getString("spec_name").toString());
                            }

                            JSONArray JAdocHospArray = jsonObject.getJSONArray("docHospResult");
                            dataHospital = new ArrayList<String>();
                            for (int k = 0; k < JAdocHospArray.length(); k++) {
                                JSONObject JOStuffHosp = JAdocHospArray.getJSONObject(k);
                                dataHospital.add(JOStuffHosp.getString("hosp_name").toString()+" ["+JOStuffHosp.getString("hosp_city").toString()+"] ");
                            }

                            JSONArray JAStuff = jsonObject.getJSONArray("docInfoResult");
                            for (int i = 0; i < JAStuff.length(); i++) {
                                JSONObject JOStuff = JAStuff.getJSONObject(i);
                                name.setText(JOStuff.getString("partner_name"));
                                name.setSelection(name.getText().length());
                                mobile_num.setText(JOStuff.getString("cont_num1"));
                                mobile_num2.setText(JOStuff.getString("secretary_phone"));
                                city.setText(JOStuff.getString("location"));
                                qualification.setText(JOStuff.getString("doc_qual"));
                                experience.setText(JOStuff.getString("ref_exp"));
                                email.setText(JOStuff.getString("Email_id"));
                                website.setText(JOStuff.getString("website"));
                                areaof_interest.setText(JOStuff.getString("doc_interest"));
                                profile_contribution.setText(JOStuff.getString("doc_contribute"));
                                research.setText(JOStuff.getString("doc_research"));
                                publications.setText(JOStuff.getString("doc_pub"));

                                opinion_cost.setText(JOStuff.getString("on_op_cost"));
                                consultation_fees.setText(JOStuff.getString("cons_charge"));
                                secretary_phone.setText(JOStuff.getString("secretary_phone"));
                                secretary_email.setText(JOStuff.getString("secretary_email"));
                                teleop_number.setText(JOStuff.getString("tele_op_contact"));
                                videoop_number.setText(JOStuff.getString("video_op_contact"));
                                timings_opinion.setText(JOStuff.getString("tele_video_op_timing"));

                                if(JOStuff.getString("tele_op").equals("1")) {
                                    teleop_checkbox.setChecked(true);
                                    TELE_OPINION_STATUS = 1;
                                }

                                if(JOStuff.getString("video_op").equals("1")) {
                                    videoop_checkbox.setChecked(true);
                                    VIDEO_OPINION_STATUS = 1;
                                }

                                compareStateValue = JOStuff.getString("state").toString();
                                if (JOStuff.getString("doc_photo").equals("")) {
                                    DOWNLOAD_PROFILE = APIClass.DRS_PROFILE_URL;
                                    Picasso.with(ProfileActivity.this).load(DOWNLOAD_PROFILE).fit().into(profile_img);
                                } else {
                                    DOWNLOAD_PROFILE = APIClass.DRS_DOCTOR_PROFILE_URL + String.valueOf(JOStuff.getString("partner_id")) + "/" + JOStuff.getString("doc_photo").trim();
                                    String urlStr = DOWNLOAD_PROFILE;
                                    URL url = null;
                                    try {
                                        url = new URL(urlStr);
                                        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                                        url = uri.toURL();
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (URISyntaxException e) {
                                        e.printStackTrace();
                                    }
                                    Picasso.with(ProfileActivity.this).load(DOWNLOAD_PROFILE)
                                            .placeholder(R.drawable.user_profile)
                                            .error(R.drawable.user_profile)
                                            .fit()
                                            .into(profile_img, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                }

                                                @Override
                                                public void onError() {
                                                }
                                            });
                                }

                                if (NetworkUtil.getConnectivityStatusString(ProfileActivity.this).equalsIgnoreCase("enabled")) {
                                    BackgroundTask task = new BackgroundTask(ProfileActivity.this);
                                    task.execute();

                                    BackgroundTaskHospital taskHospital = new BackgroundTaskHospital(ProfileActivity.this);
                                    taskHospital.execute();

                                    // new getJsonState().execute(ccp_country.getSelectedCountryNameCode());
                                    new getJsonState().execute(ccp_country.getDefaultCountryNameCode());


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
                        AppUtils.showCustomAlertMessage(ProfileActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                        Log.d(Utils.TAG+"ERR",error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(Utils.USER_LOGIN_ID));
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(ProfileActivity.this).
                getRequestQueue();
        AppController.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profile_image:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(profile_img.getWindowToken(), 0);

                requestAttachmentPermissions();
                break;
            case R.id.profile_submit:
                InputMethodManager imm1 = (InputMethodManager) ProfileActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(update_profile_btn.getWindowToken(), 0);

                validateSubmitForm();
                break;
        }
    }

   /* private void requestAttachmentPermissions() {
        if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                AlertDialog.Builder builder1 = builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", ProfileActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(ProfileActivity.this, "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,true);
            editor.commit();

        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }

    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
        //   Toast.makeText(EditProfileActivity.this, "We got the Storage Permission", Toast.LENGTH_LONG).show();
        uplaodImage();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(ProfileActivity.this,"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImageUri = null;
        String filePath = null;
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }

        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    selectedImageUri = data.getData();
                }
                break;
        }

        if(selectedImageUri != null){
            try {
                // OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri);
                GET_IMAGEPATH = getPath(selectedImageUri);
                Log.e(Utils.TAG, "GET_IMAGEPATH: "+ GET_IMAGEPATH);

                if (selectedImagePath != null) {
                    filePath = selectedImagePath;
                } else if (filemanagerstring != null) {
                    filePath = filemanagerstring;
                } else {
                    Toast.makeText(ProfileActivity.this, "Unknown path",Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                if (filePath != null) {
                    decodeFile(filePath);
                } else {
                    bitmap = null;
                }
            } catch (Exception e) {
                Toast.makeText(ProfileActivity.this, "Internal error", Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index;
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        profile_img.setImageBitmap(bitmap);

    }

    private void uplaodImage() {
        try {
            Intent gintent = new Intent();
            gintent.setType("image/*");
            gintent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(gintent, "Select Picture"),
                    PICK_IMAGE);
        } catch (Exception e) {
            Toast.makeText(ProfileActivity.this, e.getMessage(),  Toast.LENGTH_SHORT).show();
            Log.e(e.getClass().getName(), e.getMessage(), e);
        }
    }*/

    class getJsonState extends AsyncTask<String,String,String> {
        String country_shortname;
        @Override
        protected String doInBackground(String... key) {
            String stateText = key[0];
            stateText = stateText.trim();
            country_shortname = stateText.trim();
            Log.d(Utils.TAG, "State selected1: " + stateText);
            stateText = stateText.replace(" ", "+");

            stateListArraylist = new ArrayList<>();
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
                                            stateArray.add("--Select--");
                                            for (int i = 0; i < jsonArray1.length(); i++) {
                                                stateArray.add(jsonArray1.getJSONObject(i).getString("StateName"));
                                            }
                                        }


                                        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(ProfileActivity.this, R.layout.spinner_text, stateArray );
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

                                      /*  if (!compareStateValue.equals(null)) {
                                            Log.d(Utils.TAG, " compareStateValue not null " + String.valueOf(compareStateValue));
                                            int spinnerPosition = locationAdapter.getPosition(compareStateValue);
                                            state_spinner.setSelection(spinnerPosition);
                                            GET_STATE = compareStateValue;
                                        }*/

                                        if(compareStateValue.equals("") || compareStateValue.equals(null)) {

                                        }
                                        else {
                                            Log.d(Utils.TAG, " compareStateValue not null " + String.valueOf(compareStateValue));
                                            int spinnerPosition = locationAdapter.getPosition(compareStateValue);
                                            state_spinner.setSelection(spinnerPosition);
                                            GET_STATE = compareStateValue;
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
                    map.put("shortname",country_shortname);
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(ProfileActivity.this).
                    getRequestQueue();
            AppController.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }
    }

    private class BackgroundTaskHospital extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        public BackgroundTaskHospital(Activity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading Hospital...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if( (dialog != null) || (dialog.isShowing())) {
                dialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            hospitalListArraylist = new ArrayList<>();
            int socketTimeout = 10000; // 10 seconds.
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOSPITAL_LIST_ALL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG, response.toString());
                            if (response != null) {
                                JSONObject jsonObject = null;   JSONArray jsonArray = null;
                                ArrayList<String> hospitalArray = new ArrayList<String>();
                                try {
                                    jsonObject = new JSONObject(response);
                                    String staus_res = jsonObject.getString("status");
                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray = jsonObject.getJSONArray("hospital_details");
                                        if (jsonArray.length() > 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                hospitalListArraylist.add(new HospitalList(jsonArray.getJSONObject(i).getInt("hosp_id"),jsonArray.getJSONObject(i).getString("hosp_name"),
                                                        jsonArray.getJSONObject(i).getString("hosp_city"),jsonArray.getJSONObject(i).getString("hosp_state"),
                                                        USER_ID, USER_LOGIN_TYPE));
                                                hospitalArray.add(jsonArray.getJSONObject(i).getString("hosp_name")+" ["+jsonArray.getJSONObject(i).getString("hosp_city")+"] ");
                                            }
                                        }

                                        // Create a new data adapter object.
                                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_dropdown_item_1line, hospitalArray);
                                        // Connect the data source with AutoCompleteTextView through adapter.
                                        multiAutoCompleteTextViewHospital.setAdapter(arrayAdapter);
                                        // Must set tokenizer for MultiAutoCompleteTextView object, otherwise it will not take effect.
                                        multiAutoCompleteTextViewHospital.setTokenizer(new SpaceTokenizer());

                                        multiAutoCompleteTextViewHospital.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapter, View view, int position, long l) {
                                                // Toast.makeText(EditProfileActivity.this, adapter.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                                                Log.d(Utils.TAG, "hospName: "+ (String) adapter.getItemAtPosition(position));
                                                String hospName = (String) adapter.getItemAtPosition(position);

                                            }
                                        });

                                        multiAutoCompleteTextViewHospital.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                            }

                                            @Override
                                            public void afterTextChanged(Editable editable) {
                                                String text = String.valueOf(editable);
                                                // System.out.println(Utils.TAG+ " -->"+editable);
                                                GET_HOSPITAL_ARRAY = new ArrayList<>();
                                                ArrayList aList= new ArrayList(Arrays.asList(text.split(" , ")));
                                                for(int i=0;i<aList.size();i++)
                                                {

                                                    System.out.println(Utils.TAG+ " -->"+aList.get(i));
                                                    for(int j=0;j<hospitalListArraylist.size();j++){
                                                        if(aList.get(i).toString().trim().equals(hospitalListArraylist.get(j).getHospitalName()+" ["+hospitalListArraylist.get(j).getHospitalCity()+"] ".trim())) {
                                                            GET_HOSPITAL_ID = hospitalListArraylist.get(j).getHospitalId();
                                                            GET_HOSPITAL_ARRAY.add(String.valueOf(GET_HOSPITAL_ID));
                                                            // Log.d(Utils.TAG, "hospID: "+ GET_HOSPITAL_ID);
                                                        }

                                                    }
                                                }

                                            }
                                        });

                                        if(dataHospital.size() > 0) {
                                            StringBuffer strBuf=new StringBuffer();
                                            for(int k=0;k<dataHospital.size();k++) {
                                                strBuf.append(dataHospital.get(k).toString().trim()+" , ");
                                            }
                                            multiAutoCompleteTextViewHospital.setText(strBuf);
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
                    map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                    map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                    return map;
                }
            };
            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = AppController.getInstance(ProfileActivity.this).
                    getRequestQueue();
            AppController.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);

            return null;
        }
    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        public BackgroundTask(Activity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if( (dialog != null) || (dialog.isShowing())) {
                dialog.dismiss();
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
                                JSONObject jsonObject = null;   JSONArray jsonArray = null;
                                ArrayList<String> specializationArray = new ArrayList<String>();
                                try {
                                    jsonObject = new JSONObject(response);
                                    String staus_res = jsonObject.getString("status");
                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray = jsonObject.getJSONArray("specialization_details");
                                        if (jsonArray.length() > 0) {
                                            specialization = new SpecializationList(0,"--Select--", 0, "0");
                                            specializationListArraylist.add(specialization);
                                            specializationArray.add("--Select--");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                specialization = new SpecializationList(jsonArray.getJSONObject(i).getInt("spec_id"), jsonArray.getJSONObject(i).getString("spec_name"), 0, "0");
                                                specializationListArraylist.add(specialization);
                                                specializationArray.add(jsonArray.getJSONObject(i).getString("spec_name"));
                                            }
                                        }

                                        // Create a new data adapter object.
                                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_dropdown_item_1line, specializationArray);
                                        // Connect the data source with AutoCompleteTextView through adapter.
                                        multiAutoCompleteTextView.setAdapter(arrayAdapter);
                                        // Must set tokenizer for MultiAutoCompleteTextView object, otherwise it will not take effect.
                                        multiAutoCompleteTextView.setTokenizer(new SpaceTokenizer());

                                        multiAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapter, View view, int position, long l) {
                                                // Toast.makeText(EditProfileActivity.this, adapter.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                                                Log.d(Utils.TAG, "specName: "+ (String) adapter.getItemAtPosition(position));
                                                String specialization_name = (String) adapter.getItemAtPosition(position);
                                                GET_SPECIALIZATION_NAME = (String) adapter.getItemAtPosition(position);

                                            }
                                        });

                                        multiAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                            }

                                            @Override
                                            public void afterTextChanged(Editable editable) {
                                                String text = String.valueOf(editable);
                                                System.out.println(Utils.TAG+ " -->"+editable);
                                                GET_SPECIALIZATION_ARRAY = new ArrayList<>();
                                                ArrayList aList= new ArrayList(Arrays.asList(text.split(" , ")));
                                                for(int i=0;i<aList.size();i++)
                                                {

                                                    System.out.println(Utils.TAG+ " -->"+aList.get(i));
                                                    for(int j=0;j<specializationListArraylist.size();j++){
                                                        if(aList.get(i).toString().trim().equals( specializationListArraylist.get(j).getSpecializationName().trim())) {
                                                            GET_SPEC_ID = specializationListArraylist.get(j).getSpecializationId();
                                                            // Log.d(Utils.TAG, "SpecId " + GET_SPEC_ID);
                                                            GET_SPECIALIZATION_ARRAY.add(String.valueOf(GET_SPEC_ID));
                                                        }
                                                    }
                                                }

                                            }
                                        });

                                        if(dataSpec.size() > 0) {
                                            StringBuffer strBuf=new StringBuffer();
                                            for(int k=0;k<dataSpec.size();k++) {
                                                strBuf.append(dataSpec.get(k).toString().trim()+" , ");
                                            }
                                            multiAutoCompleteTextView.setText(strBuf);
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
                    return map;
                }
            };
            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = AppController.getInstance(ProfileActivity.this).
                    getRequestQueue();
            AppController.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);

            return null;
        }
    }

    private void validateSubmitForm() {

        GET_NAME = name.getText().toString();
        GET_MOBILE = mobile_num.getText().toString();
        GET_MOBILE2 = mobile_num2.getText().toString();
        GET_CITY = city.getText().toString();
        GET_QUALIFICATION = qualification.getText().toString();
        GET_EXPERIENCE = experience.getText().toString();
        GET_EMAIL = email.getText().toString();
        GET_WEBSITE = website.getText().toString();
        GET_EXPERTISE = areaof_interest.getText().toString();
        GET_CONTRIBUTION = profile_contribution.getText().toString();
        GET_RESEARCH = research.getText().toString();
        GET_PUBLICATION = publications.getText().toString();

        GET_ONLINE_OPINION_COST = opinion_cost.getText().toString();
        GET_CONSULTATION_FESS = consultation_fees.getText().toString();
        SECRETARY_PHONE = secretary_phone.getText().toString();
        SECRETARY_EMAIL = secretary_email.getText().toString();
        READDY_TELE_OP = String.valueOf(TELE_OPINION_STATUS);
        TELEOP_NUM  = teleop_number.getText().toString();
        READY_VIDEO_OP = String.valueOf(VIDEO_OPINION_STATUS);
        VIDEO_OP_NUM  = videoop_number.getText().toString();
        AVAILABLE_TIMINGS = timings_opinion.getText().toString();

        if(GET_NAME.equals("")) {
            Toast.makeText(ProfileActivity.this, "Enter name !!!", Toast.LENGTH_SHORT).show();
        }
        else if((GET_MOBILE.length() < 10) || GET_MOBILE.length() > 10) {
            Toast.makeText(ProfileActivity.this, "Invalid Mobile Number !!!", Toast.LENGTH_SHORT).show();
        }
        else if(GET_STATE.equalsIgnoreCase("--Select--")) {
            GET_STATE = "";
        }
        else {

            Log.d(Utils.TAG + " size: ", String.valueOf(GET_SPECIALIZATION_ARRAY.size()));
            for (int i = 0; i < GET_SPECIALIZATION_ARRAY.size(); i++) {
                Log.d(Utils.TAG + " Specs: ", GET_SPECIALIZATION_ARRAY.get(i).toString());
            }


            Log.d(Utils.TAG + " hospsize: ", String.valueOf(GET_HOSPITAL_ARRAY.size()));
            for (int i = 0; i < GET_HOSPITAL_ARRAY.size(); i++) {
                Log.d(Utils.TAG + " hosp: ", GET_HOSPITAL_ARRAY.get(i).toString());
            }

            if( (GET_CONSULTATION_FESS != null) || (!GET_CONSULTATION_FESS.equals(""))) {
                shareadPreferenceClass.clearDocConsultationCharges();
                shareadPreferenceClass.setDocConsultationCharges(GET_CONSULTATION_FESS);
            }

         //   Log.d(Utils.TAG + " COUNTRY_NAME: ", GET_COUNTRY_NAME);
            sendProfileRequestToServer(GET_NAME,GET_MOBILE,GET_COUNTRY_CODE, GET_COUNTRY_NAME,GET_COUNTRY_NAME_CODE, GET_STATE,GET_SPECIALIZATION_NAME, GET_SPEC_ID,
                    GET_CITY,GET_SELECT_HOSPITAL,GET_QUALIFICATION,GET_EXPERIENCE,GET_EMAIL,GET_WEBSITE, GET_EXPERTISE,GET_CONTRIBUTION,GET_RESEARCH,
                    GET_PUBLICATION, GET_IMAGEPATH,GET_SPECIALIZATION_ARRAY,GET_HOSPITAL_ARRAY,
                    GET_MOBILE2, GET_ONLINE_OPINION_COST, GET_CONSULTATION_FESS, SECRETARY_PHONE, SECRETARY_EMAIL,
                    READDY_TELE_OP, TELEOP_NUM, READY_VIDEO_OP, VIDEO_OP_NUM, AVAILABLE_TIMINGS);

        }

        }

    private void sendProfileRequestToServer(final String get_name, final String get_mobile, final String get_country_code,
                                            final String get_country_name, final String get_country_name_code,
                                            final String get_state, final String get_specialization_name, final int get_spec_id,
                                            final String get_city, final String get_select_hospital, final String get_qualification,
                                            final String get_experience, final String get_email, final String get_website, final String get_expertise,
                                            final String get_contribution, final String get_research, final String get_publication,
                                            final String get_imagepath, final ArrayList<String> get_specialization_array,
                                            final ArrayList<String> get_hospital_array, final String get_mobile2,
                                            final String get_online_opinion_cost, final String get_consultation_fess,
                                            final String secretary_phone, final String secretary_email, final String readdy_tele_op,
                                            final String teleop_num, final String ready_video_op, final String video_op_num,
                                            final String available_timings) {

        new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;
            ProgressDialog progressDialog1;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog1 = new ProgressDialog(ProfileActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                progressDialog1.setIndeterminate(true);
                progressDialog1.setMessage("Please Wait....");
                progressDialog1.show();


            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {
                    JSONObject jsonObject = JSONParser.updateEditProfile(get_name, get_mobile, get_country_code, get_country_name,
                            get_country_name_code,  get_state,  get_specialization_name,
                            get_spec_id, get_city, get_select_hospital, get_qualification,
                            get_experience, get_email, get_website, get_expertise,
                            get_contribution, get_research, get_publication, get_imagepath,
                            get_specialization_array, get_hospital_array, USER_ID, USER_LOGIN_TYPE,
                            get_mobile2, get_online_opinion_cost, get_consultation_fess, secretary_phone,secretary_email,
                            readdy_tele_op, teleop_num, ready_video_op, video_op_num, available_timings);

                    if (jsonObject != null) {
                        Log.e(Utils.TAG, " GET: " + jsonObject.getString("result"));
                        if(jsonObject.getString("result").equals("success")) {
                            status = true;
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

                if (sharedPreferences != null) {
                    if( (get_imagepath != null) || (!get_imagepath.equals(""))) {
                        shareadPreferenceClass.clearDocProfileImage();
                        shareadPreferenceClass.setDocProfileImage(GET_IMAGE_FILENAME);
                    }

                    if( (get_name != null) || (!get_name.equals(""))) {
                        shareadPreferenceClass.clearDocProfileName();
                        shareadPreferenceClass.setDocProfileName(get_name);
                    }
                }


                if (aBoolean) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProfileActivity.this, R.style.CustomDialog);
                    alertDialogBuilder.setMessage("Profile updated successfully. ");
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
                    Toast.makeText(ProfileActivity.this, "Failed to update profile. \nTry later !!!", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }

    private void requestAttachmentPermissions() {
        if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                AlertDialog.Builder builder1 = builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(ProfileActivity.this, "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            editor.commit();

        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }

    private void proceedAfterPermission() {
        selectAttachementFromDevice();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
        else  if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
//            else if (requestCode == REQUEST_CAMERA)
//                onCaptureImageResult(data);
        }
    }

    private void selectAttachementFromDevice() {
        final CharSequence[] items = {"Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Upload Attachments");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            Log.d(Utils.TAG +" data: ", String.valueOf(data));
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), bm);

                //  Uri selectedImageUri = data.getData();
                //  GET_IMAGEPATH =   getPath(selectedImageUri);
                GET_IMAGEPATH =   getRealPathFromURI(tempUri);
                Log.e(Utils.TAG, "GET_IMAGEPATH: "+ GET_IMAGEPATH);

                File file = new File(GET_IMAGEPATH);
                GET_IMAGE_FILENAME = file.getName();
                Log.e(Utils.TAG, " GET_IMAGE_FILENAME: "+ GET_IMAGE_FILENAME);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        profile_img.setImageBitmap(bm);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index;
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
}