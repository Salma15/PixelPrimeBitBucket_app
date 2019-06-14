package com.medisensehealth.fdccontributor.activities.emr;

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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medisensehealth.fdccontributor.DataModel.FamilyMember;
import com.medisensehealth.fdccontributor.DataModel.HospitalList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.parser.JSONParser;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomEditText;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 25-07-2018.
 */

public class AddNewPatientActivity extends AppCompatActivity implements View.OnClickListener {

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID, HOSPITAL_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE, MYPATIENT_LIST, MYPATIENT_COUNT, APPOINTMENT_LIST, DOC_HOSIPTAL_LIST;
    ArrayList<HospitalList> hospitalDocArraylist;

    CustomEditText _edt_name, _edt_mobile, _edt_city, _edt_email, _edt_age, _edt_height, _edt_weight, _edt_address, _edt_pincode, _edt_state, _edt_country;
    RadioGroup radioGroupGender;
    RadioButton rb_male, rb_female, rb_others;
    LinearLayout view_more_btn, view_more_contents;
    ImageView view_more_image;
    CustomTextViewBold submit_btn;
    boolean view_more_status = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_emr_new_patient);

        hospitalDocArraylist = new ArrayList<>();
        view_more_status = false;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);
            Log.d(Utils.TAG, " ************ AddNewPatientActivity *********** ");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Utils.TAG, " On Resume");

        shareadPreferenceClass = new ShareadPreferenceClass(AddNewPatientActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(AddNewPatientActivity.this);

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            MYPATIENT_LIST = sharedPreferences.getString(HCConstants.PREF_MYPATIENT_LISTS, "");
            MYPATIENT_COUNT = sharedPreferences.getString(HCConstants.PREF_COUNT_LISTS_MYPATIENT, "0");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
            APPOINTMENT_LIST = sharedPreferences.getString(HCConstants.PREF_APPOINTMENT_LISTS, "");
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            MYPATIENT_LIST = sharedPreferences.getString(HCConstants.PREF_MYPATIENT_LISTS, "");
            MYPATIENT_COUNT = sharedPreferences.getString(HCConstants.PREF_COUNT_LISTS_MYPATIENT, "0");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
            APPOINTMENT_LIST = sharedPreferences.getString(HCConstants.PREF_APPOINTMENT_LISTS, "");
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {

            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            MYPATIENT_LIST = sharedPreferences.getString(HCConstants.PREF_MYPATIENT_LISTS, "");
            MYPATIENT_COUNT = sharedPreferences.getString(HCConstants.PREF_COUNT_LISTS_MYPATIENT, "0");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
            APPOINTMENT_LIST = sharedPreferences.getString(HCConstants.PREF_APPOINTMENT_LISTS, "");
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        Log.d(Utils.TAG+" LoginType: ", Utils.USER_LOGIN_TYPE);
        Log.d(Utils.TAG+" LoginId: ", String.valueOf(Utils.USER_LOGIN_ID));
        Log.d(Utils.TAG+" HOSPITAL_ID:", String.valueOf(HOSPITAL_ID));

        initializationViews();
    }

    private void initializationViews() {

        _edt_name = (CustomEditText) findViewById(R.id.add_name);
        _edt_mobile = (CustomEditText) findViewById(R.id.add_mobile);
        radioGroupGender = (RadioGroup) findViewById(R.id.radioGender);
        rb_male = (RadioButton) findViewById(R.id.radioMale);
        rb_female = (RadioButton) findViewById(R.id.radioFemale);
        rb_others = (RadioButton) findViewById(R.id.radioTrans);
        _edt_city = (CustomEditText) findViewById(R.id.add_city);
        view_more_btn = (LinearLayout) findViewById(R.id.add_view_more_btn);
        view_more_btn.setOnClickListener(this);
        view_more_image = (ImageView) findViewById(R.id.add_view_more_image);
        view_more_contents  = (LinearLayout) findViewById(R.id.add_view_more_content);
        view_more_contents.setVisibility(View.GONE);
        _edt_email = (CustomEditText) findViewById(R.id.add_email);
        _edt_age = (CustomEditText) findViewById(R.id.add_age);
        _edt_height = (CustomEditText) findViewById(R.id.add_height);
        _edt_weight = (CustomEditText) findViewById(R.id.add_weight);
        _edt_address = (CustomEditText) findViewById(R.id.add_address);
        _edt_pincode = (CustomEditText) findViewById(R.id.add_pincode);
        _edt_state  = (CustomEditText) findViewById(R.id.add_state);
        _edt_country  = (CustomEditText) findViewById(R.id.add_country);
        submit_btn = (CustomTextViewBold) findViewById(R.id.add_submit);
        submit_btn.setOnClickListener(this);
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
                //  Toast.makeText(getApplicationContext(),"Switch Hospital",Toast.LENGTH_LONG).show();
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
            //  Log.d(Utils.TAG, "DOC_HOSIPTAL_LIST NOT EMPTY ");
            hospitalDocArraylist = gson.fromJson(DOC_HOSIPTAL_LIST, new TypeToken<List<HospitalList>>() {
            }.getType());
            if(hospitalDocArraylist.size() > 0 ) {
                //  Log.d(Utils.TAG, "hospitalDocArraylist size: " + hospitalDocArraylist.size());
                openCustomHospitalSwitchDialog(hospitalDocArraylist);
            }
        }
    }

    private void openCustomHospitalSwitchDialog(ArrayList<HospitalList> hospitalDocArraylist) {
        // custom dialog
        final Dialog dialog = new Dialog(AddNewPatientActivity.this, R.style.CustomDialog);
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
            RadioButton rb=new RadioButton(AddNewPatientActivity.this); // dynamically creating RadioButton and adding to RadioGroup.
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
            case R.id.add_view_more_btn:
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
            case R.id.add_submit:
                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(submit_btn.getWindowToken(), 0);

                ValidateSubmitForm();
                break;
        }
    }

    private void ValidateSubmitForm() {

        String name = _edt_name.getText().toString().trim();
        String mobile = _edt_mobile.getText().toString().trim();
        String city = _edt_city.getText().toString().trim();
        String email = _edt_email.getText().toString();
        String age = _edt_age.getText().toString();
        String height = _edt_height.getText().toString();
        String weight = _edt_weight.getText().toString();
        String address = _edt_address.getText().toString();
        String pincode = _edt_pincode.getText().toString();
        String state = _edt_state.getText().toString();
        String country = _edt_country.getText().toString();
        String gender = "0";
        int checkedRadioButtonId = radioGroupGender.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {   // No items Selected
            gender = "0";
        }
        else{
            if (checkedRadioButtonId == R.id.radioMale) {
                gender = "1";
            }
            else if (checkedRadioButtonId == R.id.radioFemale) {
                gender = "2";
            }
            else if (checkedRadioButtonId == R.id.radioTrans) {
                gender = "3";
            }
            else {
                gender = "0";
            }
        }

        // ********************* Validations *************************
        if(name.equals("")) {
            Toast.makeText(AddNewPatientActivity.this, " Enter patient name !!!", Toast.LENGTH_SHORT).show();
        }
        else if(gender.equals("0")) {
            Toast.makeText(AddNewPatientActivity.this, " Select gender !!!", Toast.LENGTH_SHORT).show();
        }
        else if(mobile.equals("") || mobile.length() < 10) {
            Toast.makeText(AddNewPatientActivity.this, " Enter valid 10 digits mobile no. !!!", Toast.LENGTH_SHORT).show();
        }
        if(city.equals("")) {
            Toast.makeText(AddNewPatientActivity.this, " Enter city !!!", Toast.LENGTH_SHORT).show();
        }
        else {

            if (NetworkUtil.getConnectivityStatusString(AddNewPatientActivity.this).equalsIgnoreCase("enabled")) {
                submitPatientIndoToServer(name,gender,mobile,city,email,age,height,weight,address,pincode,state,country);
            } else {
                AppUtils.showCustomAlertMessage(AddNewPatientActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }
        }
    }

    private void submitPatientIndoToServer(final String name, final String gender, final String mobile, final String city, final String email, final String age,
                                           final String height, final String weight, final String address, final String pincode, final String state,
                                           final String country) {

        new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;
            ProgressDialog progressDialog;
            JSONArray jsonArrayFamily;
            ArrayList<String> memberNameArray;
            ArrayList<FamilyMember> memberArraylist = new ArrayList<>();
            int member_id = 0, user_id=0;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(AddNewPatientActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {
                    JSONObject jsonObject = JSONParser.createMyPatientNew(name,age,gender, mobile,
                            email, city, address, "", country, "",height, weight, USER_ID, USER_LOGIN_TYPE );

                    if (jsonObject != null) {
                        Log.e(Utils.TAG, " GET: " + jsonObject.getString("result"));
                        if(jsonObject.getString("result").equals("success")) {
                            jsonArrayFamily = jsonObject.getJSONArray("family_details");
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
                if (  progressDialog != null)
                    progressDialog.dismiss();
                Log.e(Utils.TAG, "aBoolean: " + String.valueOf(aBoolean));
                if (aBoolean) {

                    if(jsonArrayFamily != null) {
                        memberArraylist = new ArrayList<>();
                        memberNameArray = new ArrayList<String>();

                        if (jsonArrayFamily.length() > 0) {
                            for (int i = 0; i < jsonArrayFamily.length(); i++) {
                                try {
                                    memberArraylist.add(new FamilyMember(jsonArrayFamily.getJSONObject(i).getInt("member_id"),jsonArrayFamily.getJSONObject(i).getInt("user_id"),
                                            jsonArrayFamily.getJSONObject(i).getString("gender"), jsonArrayFamily.getJSONObject(i).getString("member_name"),
                                            jsonArrayFamily.getJSONObject(i).getString("relationship"),jsonArrayFamily.getJSONObject(i).getString("dob")));
                                    memberNameArray.add(jsonArrayFamily.getJSONObject(i).getString("member_name"));
                                    user_id = jsonArrayFamily.getJSONObject(i).getInt("user_id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            memberArraylist.add(new FamilyMember(0,user_id,
                                    "", "",
                                    "",""));
                            memberNameArray.add("New");

                            final Dialog dialog = new Dialog(AddNewPatientActivity.this, R.style.CustomDialog);
                            dialog.setContentView(R.layout.custom_family_members);
                            dialog.setTitle("Choose Family Member / Patient");
                            dialog.setCanceledOnTouchOutside(false);

                            ListView familyListView = (ListView) dialog.findViewById(R.id.family_member_listView);
                            CustomTextViewBold submit = (CustomTextViewBold) dialog.findViewById(R.id.family_member_submit);

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddNewPatientActivity.this,android.R.layout.simple_list_item_single_choice,
                                    memberNameArray);
                            familyListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // Enables single selection
                            familyListView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            familyListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
                                {
                                  //  Toast.makeText(AddNewPatientActivity.this, "" + position +" mem" + memberArraylist.get(position).getMemberid()+" user" + memberArraylist.get(position).getUserid() +" Name: "+memberArraylist.get(position).getMemberName(), Toast.LENGTH_SHORT).show();

                                    member_id = memberArraylist.get(position).getMemberid();
                                    user_id =  memberArraylist.get(position).getUserid();
                                }
                            });

                            submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Log.d(Utils.TAG," PATIENT_NAME: "+name);
                                    Log.d(Utils.TAG," PATIENT_AGE: "+age);
                                    Log.d(Utils.TAG," PATIENT_GENDER: "+gender);
                                    Log.d(Utils.TAG," PATIENT_MOBILE: "+mobile);
                                    Log.d(Utils.TAG," PATIENT_EMAIL: "+email);
                                    Log.d(Utils.TAG," PATIENT_ADDRESS: "+address);
                                    Log.d(Utils.TAG," PATIENT_STATE: "+state);
                                    Log.d(Utils.TAG," PATIENT_COUNTRY_NAME: "+country);
                                    Log.d(Utils.TAG," PATIENT_MEMBER_ID: "+member_id);
                                    Log.d(Utils.TAG," PATIENT_USER_ID: "+user_id);

                                    dialog.dismiss();

                                    submitMyPatientCreateFinalToServer(name, age, gender, mobile, email,
                                            address, _edt_city.getText().toString().trim(), state,
                                            _edt_country.getText().toString().trim(),height, weight, member_id, user_id);

                                }
                            });

                            Log.d(Utils.TAG,"you have "+memberArraylist.size()+" members in list");
                            dialog.show();
                        }
                        else {
                            submitMyPatientCreateFinalToServer(name, age, gender, mobile, email,
                                    address, city, state,
                                    country, height, weight, 0, 0);
                        }
                    }
                    else {
                        // Toast.makeText(SignUpActivity.this,"Registered Successfully", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddNewPatientActivity.this, R.style.CustomDialog);
                        alertDialogBuilder.setMessage("Patient Created Successfully. ");
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

//                                    ShowCountAsyncTask myTask = new ShowCountAsyncTask(getActivity());
//                                    myTask.execute();
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

                }
                else {
                    Toast.makeText(AddNewPatientActivity.this, "Failed to create patient. \nTry later !!!", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }

    private void submitMyPatientCreateFinalToServer(final String patient_name, final String patient_age, final String patient_gender,
                                                    final String patient_mobile, final String patient_email, final String patient_address,
                                                    final String city, final String state, final String country, final String height, final String weight, final int member_id, final int pat_user_id) {

        new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(AddNewPatientActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {
                    JSONObject jsonObject = JSONParser.createMyPatientSubmit(patient_name,patient_age,patient_gender, patient_mobile,
                            patient_email, patient_address, city, state, country, member_id, pat_user_id, USER_ID, USER_LOGIN_TYPE,height, weight );

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
                if (  progressDialog != null)
                    progressDialog.dismiss();
                Log.e(Utils.TAG, "aBoolean: " + String.valueOf(aBoolean));
                if (aBoolean) {

                    if (sharedPreferences != null) {
                        shareadPreferenceClass.clearMyPatientLists();     // Clear My Patient details
                    }
                    // Toast.makeText(SignUpActivity.this,"Registered Successfully", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddNewPatientActivity.this, R.style.CustomDialog);
                    alertDialogBuilder.setMessage("Patient Created Successfully. ");
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
                    Toast.makeText(AddNewPatientActivity.this, "Failed to create patient. \nTry later !!!", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();

    }
}
