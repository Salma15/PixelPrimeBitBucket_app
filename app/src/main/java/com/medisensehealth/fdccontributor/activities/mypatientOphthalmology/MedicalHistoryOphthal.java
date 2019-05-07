package com.medisensehealth.fdccontributor.activities.mypatientOphthalmology;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.medisensehealth.fdccontributor.DataModel.DrugAbuse;
import com.medisensehealth.fdccontributor.DataModel.DrugAllery;
import com.medisensehealth.fdccontributor.DataModel.FamilyHistory;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.CustomDrugAbuseFilterAdapter;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.CustomDrugAllergyFilterAdapter;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.CustomFamilyHistoryFilterAdapter;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.DrugAbuseAdapters;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.DrugAllergyAdapters;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.FamilyHistoryAdapters;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.FrequentDrugAbuseAdapters;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.FrequentDrugAllergyAdapters;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.FrequentFamilyHistoryAdapters;
import com.medisensehealth.fdccontributor.database.MedisensePracticeDB;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.parser.JSONParser;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomEditText;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SALMA on 07-09-2018.
 */
public class MedicalHistoryOphthal  extends Fragment implements View.OnClickListener {
    int USER_ID, PATIENT_ID;
    String USER_NAME,USER_LOGIN_TYPE;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;

    String PATIENT_NAME, PATIENT_AGE, PATIENT_GENDER, PATIENT_MOBILE, PATIENT_EMAIL, PATIENT_CITY, PATIENT_ADDRESS, PATIENT_STATE, PATIENT_COUNTRY,
            PATIENT_HEIGHT, PATIENT_WEIGHT, PATIENT_HYPERTENSION, PATIENT_DIABETES, PATIENT_SMOKING, PATIENT_ALCOHOL, PATIENT_DRUG_ABUSE, PATIENT_OTHER_DETAILS,
            PATIENT_FAMILY_HISTORY, PATIENT_PREV_INTERVENTIONS, PATIENT_NEURO_ISSUES, PATIENT_KIDNEY_ISSUES, PATIENT_CHOLESTEROL = "", PATIENT_ARTHRITIS = "", PATIENT_IHD = "", PATIENT_ASTHAMA = "", PATIENT_THY = "";

    CustomEditText _edt_weight, _edt_height, _edt_height_inch, _edt_prev_intervention, _edt_stroke, _edt_kidney_issues, _edt_otherdetails;
    CustomTextView _txt_bmi, _txt_bmi_status, submit_btn;
    RadioGroup rg_hypertension, rg_diabetes, rg_cholesterol, rg_arthritis, rg_ihd, rg_asthama, rg_thy;
    RadioButton rb_hyperYes, rb_hyperNo, rb_diabetesYes, rb_diabetesNo, rb_cholesterolYes, rb_cholesterolNo, rb_arthritisYes, rb_arthritisNo, rb_ihdYes, rb_ihdNo, rb_asthamaYes, rb_asthamaNo, rb_thyYes, rb_thyNo;
    Spinner smoking_spinner, alcohol_spinner;

    // Drug Allery
    LinearLayout drug_allery_btn, drug_allery_contents, drug_allery_clear;
    boolean view_drug_allery_status = false;
    ImageView drug_allery_image;

    int numberOfFrequentColumns = 4;

    List<DrugAllery> drugAlleryListArraylist;
    List<DrugAllery> PATIENT_DRUG_ALLERTY_ARRAY;
    private FrequentDrugAllergyAdapters frequentDrugAlleryAdapter;
    RecyclerView frequentDrugAllergyRecyclerview;
    private DrugAllergyAdapters drugAllergyAdapter;
    RecyclerView drugAllergyRecyclerview;
    ListView drug_allergy_listview;

    // Drug Abuse
    LinearLayout drug_abuse_btn, drug_abuse_contents;
    boolean view_drug_abuse_status = false;
    ImageView drug_abuse_image;

    List<DrugAbuse> freqdrugAbuseListArraylist;
    List<DrugAbuse> PATIENT_DRUG_ABUSE_ARRAY;
    private FrequentDrugAbuseAdapters frequentDrugAbuseAdapter;
    RecyclerView frequentDrugAbuseRecyclerview;
    List<DrugAbuse> drugAbuseListArraylist;
    private DrugAbuseAdapters drugAbuseAdapter;
    RecyclerView drugAbuseRecyclerview;

    // Family History
    LinearLayout family_history_btn, family_history_contents;
    boolean view_family_history_status = false;
    ImageView family_history_image, refresh_btn;

    List<FamilyHistory> freqfamilyHistoryListArraylist;
    List<FamilyHistory> PATIENT_FAMILY_HISTORY_ARRAY;
    List<FamilyHistory> familyHistoryListArraylist;
    private FrequentFamilyHistoryAdapters frequentFamilyHistoryAdapter;
    RecyclerView frequentFamilyHistoryRecyclerview;
    private FamilyHistoryAdapters familyHistoryAdapter;
    RecyclerView familyHistoryRecyclerview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicalhistory, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            PATIENT_ID = bundle.getInt("PATIENT_ID", 0);
            PATIENT_NAME = bundle.getString("PATIENT_NAME");
            PATIENT_AGE = bundle.getString("PATIENT_AGE");
            PATIENT_GENDER = bundle.getString("PATIENT_GENDER");
            PATIENT_MOBILE = bundle.getString("PATIENT_MOBILE");
            PATIENT_EMAIL = bundle.getString("PATIENT_EMAIL");
            PATIENT_CITY = bundle.getString("PATIENT_CITY");
            PATIENT_ADDRESS = bundle.getString("PATIENT_ADDRESS");
            PATIENT_STATE = bundle.getString("PATIENT_STATE");
            PATIENT_COUNTRY = bundle.getString("PATIENT_COUNTRY_NAME");

            PATIENT_HEIGHT = bundle.getString("PATIENT_HEIGHT");
            PATIENT_WEIGHT = bundle.getString("PATIENT_WEIGHT");
            PATIENT_HYPERTENSION = bundle.getString("PATIENT_HYPERTENSION");
            PATIENT_DIABETES = bundle.getString("PATIENT_DIABETES");
            PATIENT_SMOKING = bundle.getString("PATIENT_SMOKING");
            PATIENT_ALCOHOL = bundle.getString("PATIENT_ALCOHOL");
            PATIENT_DRUG_ABUSE = bundle.getString("PATIENT_DRUG_ABUSE");
            PATIENT_OTHER_DETAILS = bundle.getString("PATIENT_OTHER_DETAILS");
            PATIENT_FAMILY_HISTORY  = bundle.getString("PATIENT_FAMILY_HISTORY");
            PATIENT_PREV_INTERVENTIONS  = bundle.getString("PATIENT_PREV_INTERVENTIONS");
            PATIENT_NEURO_ISSUES = bundle.getString("PATIENT_NEURO_ISSUES");
            PATIENT_KIDNEY_ISSUES = bundle.getString("PATIENT_KIDNEY_ISSUES");

            Log.d(Utils.TAG, " ********* MedicalHistoryOphthal ************ ");
            Log.d(Utils.TAG, " PATIENT_ID: "+PATIENT_ID);
            Log.d(Utils.TAG, " PATIENT_NAME: "+PATIENT_NAME);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        view_drug_allery_status = false;
        view_drug_abuse_status = false;
        view_family_history_status = false;

        drugAlleryListArraylist = new ArrayList<>();
        PATIENT_DRUG_ALLERTY_ARRAY = new ArrayList<>();

        freqdrugAbuseListArraylist = new ArrayList<>();
        PATIENT_DRUG_ABUSE_ARRAY = new ArrayList<>();
        drugAbuseListArraylist = new ArrayList<>();

        freqfamilyHistoryListArraylist = new ArrayList<>();
        PATIENT_FAMILY_HISTORY_ARRAY = new ArrayList<>();
        familyHistoryListArraylist = new ArrayList<>();

        shareadPreferenceClass = new ShareadPreferenceClass(getActivity());
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(getActivity());

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {

            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
        }
        Log.d(Utils.TAG, " USER_ID: "+USER_ID);
        Log.d(Utils.TAG, " USER_NAME: "+USER_NAME);
        Log.d(Utils.TAG, " LOGIN_TYPE: "+USER_LOGIN_TYPE);

        initializeViews();
    }

    private void initializeViews() {

        // InitializeViews
        _edt_weight = (CustomEditText) getActivity().findViewById(R.id.medhist_weight);
        _edt_height = (CustomEditText) getActivity().findViewById(R.id.medhist_height_cms);
        _edt_height_inch = (CustomEditText) getActivity().findViewById(R.id.medhist_height_inches);
        _txt_bmi = (CustomTextView) getActivity().findViewById(R.id.medhist_bmi);
        _txt_bmi_status = (CustomTextView) getActivity().findViewById(R.id.medhist_bmi_status);
        rg_hypertension = (RadioGroup) getActivity().findViewById(R.id.radioHypertension);
        rb_hyperYes = (RadioButton) getActivity().findViewById(R.id.radiohyperYes);
        rb_hyperNo = (RadioButton) getActivity().findViewById(R.id.radiohyperNo);
        rg_diabetes = (RadioGroup) getActivity().findViewById(R.id.radioDiabetes);
        rb_diabetesYes = (RadioButton) getActivity().findViewById(R.id.radiodiabetesYes);
        rb_diabetesNo = (RadioButton) getActivity().findViewById(R.id.radiodiabetesNo);
        smoking_spinner = (Spinner) getActivity().findViewById(R.id.medhist_smoking_spinner);
        alcohol_spinner = (Spinner) getActivity().findViewById(R.id.medhist_alcohol_spinner);
        _edt_prev_intervention = (CustomEditText) getActivity().findViewById(R.id.medhist_interventions);
        _edt_stroke = (CustomEditText) getActivity().findViewById(R.id.medhist_neuro);
        _edt_kidney_issues = (CustomEditText) getActivity().findViewById(R.id.medhist_kidney_issues);
        _edt_otherdetails = (CustomEditText) getActivity().findViewById(R.id.medhist_otherdetails);
        submit_btn = (CustomTextView) getActivity().findViewById(R.id.medhist_submit);
        submit_btn.setOnClickListener(this);
        refresh_btn = (ImageView)  getActivity().findViewById(R.id.medhist_refresh);
        refresh_btn.setOnClickListener(this);

        // Cholesterol
        rg_cholesterol = (RadioGroup) getActivity().findViewById(R.id.radioCholesterol);
        rb_cholesterolYes = (RadioButton) getActivity().findViewById(R.id.radioCholesterolYes);
        rb_cholesterolNo = (RadioButton) getActivity().findViewById(R.id.radioCholesterolNo);

        // Arthritis
        rg_arthritis = (RadioGroup) getActivity().findViewById(R.id.radioArthritis);
        rb_arthritisYes = (RadioButton) getActivity().findViewById(R.id.radioArthritisYes);
        rb_arthritisNo = (RadioButton) getActivity().findViewById(R.id.radioArthritisNo);

        // IHD
        rg_ihd = (RadioGroup) getActivity().findViewById(R.id.radioIHD);
        rb_ihdYes = (RadioButton) getActivity().findViewById(R.id.radioIHDYes);
        rb_ihdNo = (RadioButton) getActivity().findViewById(R.id.radioIHDNo);

        // ASTHAMA
        rg_asthama = (RadioGroup) getActivity().findViewById(R.id.radioAsthama);
        rb_asthamaYes = (RadioButton) getActivity().findViewById(R.id.radioAsthamaYes);
        rb_asthamaNo = (RadioButton) getActivity().findViewById(R.id.radioAsthamaNo);

        // THY
        rg_thy = (RadioGroup) getActivity().findViewById(R.id.radioTHY);
        rb_thyYes = (RadioButton) getActivity().findViewById(R.id.radioTHYYes);
        rb_thyNo = (RadioButton) getActivity().findViewById(R.id.radioTHYNo);

        // Drug Allery Views
        drug_allery_btn = (LinearLayout) getActivity().findViewById(R.id.medhist_drugallery_btn);
        drug_allery_btn.setOnClickListener(this);
        drug_allery_contents = (LinearLayout) getActivity().findViewById(R.id.drug_allery_contents);
        drug_allery_image = (ImageView) getActivity().findViewById(R.id.medhist_drugallery_down_image);

        // Drug Abuse Views
        drug_abuse_btn = (LinearLayout) getActivity().findViewById(R.id.medhist_drugabuse_btn);
        drug_abuse_btn.setOnClickListener(this);
        drug_abuse_contents = (LinearLayout) getActivity().findViewById(R.id.drug_abuse_contents);
        drug_abuse_image = (ImageView) getActivity().findViewById(R.id.medhist_drugabuse_down_image);

        // Family History Views
        family_history_btn = (LinearLayout) getActivity().findViewById(R.id.medhist_familyhistory_btn);
        family_history_btn.setOnClickListener(this);
        family_history_contents = (LinearLayout) getActivity().findViewById(R.id.familyhistory_contents);
        family_history_image = (ImageView) getActivity().findViewById(R.id.medhist_familyhistory_down_image);

        _edt_weight.setText(PATIENT_WEIGHT);
        _edt_height.setText(PATIENT_HEIGHT);
        _edt_prev_intervention.setText(PATIENT_PREV_INTERVENTIONS);
        _edt_stroke.setText(PATIENT_NEURO_ISSUES);
        _edt_kidney_issues.setText(PATIENT_KIDNEY_ISSUES);
        _edt_otherdetails.setText(PATIENT_OTHER_DETAILS);


        if(PATIENT_HYPERTENSION.equalsIgnoreCase("1")) {
            rb_hyperYes.setChecked(true);
        }
        else if(PATIENT_HYPERTENSION.equalsIgnoreCase("2")) {
            rb_hyperNo.setChecked(true);
        }
        else if(PATIENT_HYPERTENSION.equalsIgnoreCase("0")) {
            rb_hyperYes.setChecked(false);
            rb_hyperNo.setChecked(false);
        }
        else {
            rb_hyperYes.setChecked(false);
            rb_hyperNo.setChecked(false);
        }

        if(PATIENT_DIABETES.equalsIgnoreCase("1")) {
            rb_diabetesYes.setChecked(true);
        }
        else if(PATIENT_DIABETES.equalsIgnoreCase("2")) {
            rb_diabetesNo.setChecked(true);
        }
        else if(PATIENT_DIABETES.equalsIgnoreCase("0")) {
            rb_diabetesYes.setChecked(false);
            rb_diabetesNo.setChecked(false);
        }
        else {
            rb_diabetesYes.setChecked(false);
            rb_diabetesNo.setChecked(false);
        }

        //Cholesterol
        if(PATIENT_CHOLESTEROL.equalsIgnoreCase("1")) {
            rb_cholesterolYes.setChecked(true);
        }
        else if(PATIENT_CHOLESTEROL.equalsIgnoreCase("2")) {
            rb_cholesterolNo.setChecked(true);
        }
        else if(PATIENT_CHOLESTEROL.equalsIgnoreCase("0")) {
            rb_cholesterolYes.setChecked(false);
            rb_cholesterolNo.setChecked(false);
        }
        else {
            rb_cholesterolYes.setChecked(false);
            rb_cholesterolNo.setChecked(false);
        }

        // ARTHRITIS
        if(PATIENT_ARTHRITIS.equalsIgnoreCase("1")) {
            rb_arthritisYes.setChecked(true);
        }
        else if(PATIENT_ARTHRITIS.equalsIgnoreCase("2")) {
            rb_arthritisNo.setChecked(true);
        }
        else if(PATIENT_ARTHRITIS.equalsIgnoreCase("0")) {
            rb_arthritisYes.setChecked(false);
            rb_arthritisNo.setChecked(false);
        }
        else {
            rb_arthritisYes.setChecked(false);
            rb_arthritisNo.setChecked(false);
        }

        // IHD
        if(PATIENT_IHD.equalsIgnoreCase("1")) {
            rb_ihdYes.setChecked(true);
        }
        else if(PATIENT_IHD.equalsIgnoreCase("2")) {
            rb_ihdNo.setChecked(true);
        }
        else if(PATIENT_IHD.equalsIgnoreCase("0")) {
            rb_ihdYes.setChecked(false);
            rb_ihdNo.setChecked(false);
        }
        else {
            rb_ihdYes.setChecked(false);
            rb_ihdNo.setChecked(false);
        }

        // ASTHAMA
        if(PATIENT_ASTHAMA.equalsIgnoreCase("1")) {
            rb_asthamaYes.setChecked(true);
        }
        else if(PATIENT_ASTHAMA.equalsIgnoreCase("2")) {
            rb_asthamaNo.setChecked(true);
        }
        else if(PATIENT_ASTHAMA.equalsIgnoreCase("0")) {
            rb_asthamaYes.setChecked(false);
            rb_asthamaNo.setChecked(false);
        }
        else {
            rb_asthamaYes.setChecked(false);
            rb_asthamaNo.setChecked(false);
        }

        // THY
        if(PATIENT_THY.equalsIgnoreCase("1")) {
            rb_thyYes.setChecked(true);
        }
        else if(PATIENT_THY.equalsIgnoreCase("2")) {
            rb_thyNo.setChecked(true);
        }
        else if(PATIENT_THY.equalsIgnoreCase("0")) {
            rb_thyYes.setChecked(false);
            rb_thyNo.setChecked(false);
        }
        else {
            rb_thyYes.setChecked(false);
            rb_thyNo.setChecked(false);
        }


        ArrayAdapter<String> smokingArray = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.smokingArray) );
        smokingArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        smoking_spinner.setAdapter(smokingArray);

        if(PATIENT_SMOKING.equals("")) {
            PATIENT_SMOKING = "--Select--";
        }

        smoking_spinner.setSelection(getIndex(smoking_spinner, PATIENT_SMOKING));

        smoking_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(mContext, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                if(adapterView.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    PATIENT_SMOKING = "";
                }
                else {
                    PATIENT_SMOKING = adapterView.getSelectedItem().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if(PATIENT_ALCOHOL.equals("")) {
            PATIENT_ALCOHOL = "--Select--";
        }

        ArrayAdapter<String> alcoholArray = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.alcoholicArray) );
        alcoholArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alcohol_spinner.setAdapter(alcoholArray);

        alcohol_spinner.setSelection(getIndex(alcohol_spinner, PATIENT_ALCOHOL));

        alcohol_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(mContext, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                if(adapterView.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    PATIENT_ALCOHOL = "";
                }
                else {
                    PATIENT_ALCOHOL = adapterView.getSelectedItem().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

      /*  _edt_height_inch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if(s.length() != 0) {
                    int inchInFeet1 = Integer.parseInt(_edt_height_inch.getText().toString());
                    if(inchInFeet1 >=12 ) {
                        Toast.makeText(getActivity(), "Height in Inch should be < 12 ", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(s.length() != 0 && _edt_weight.length() !=0 && _edt_height.length() !=0) {
                            double weight = Double.parseDouble(_edt_weight.getText().toString());
                            int ht_feet = Integer.parseInt(_edt_height.getText().toString());
                            int inchInFeet = Integer.parseInt(_edt_height_inch.getText().toString());
                            double inch_to_feet = inchInFeet * 0.0833333;
                            double total_feet = ht_feet + inch_to_feet;
                            double heightInMeter = total_feet * 0.3048;
                            Log.d(Utils.TAG, " heightInMeter: "+heightInMeter);

                            double bmi_calculate = weight/(heightInMeter*heightInMeter);
                            _txt_bmi.setText(String.valueOf(bmi_calculate));
                        }
                    }
                }

            }
        });

        _edt_height.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0 && _edt_weight.length() !=0 && _edt_height_inch.length() !=0) {
                    double weight = Double.parseDouble(_edt_weight.getText().toString());
                    int ht_feet = Integer.parseInt(_edt_height.getText().toString());
                    int inchInFeet = Integer.parseInt(_edt_height_inch.getText().toString());
                    double inch_to_feet = inchInFeet * 0.0833333;
                    double total_feet = ht_feet + inch_to_feet;
                    double heightInMeter = total_feet * 0.3048;
                    Log.d(Utils.TAG, " heightInMeter: "+heightInMeter);

                    double bmi_calculate = weight/(heightInMeter*heightInMeter);
                    _txt_bmi.setText(String.valueOf(bmi_calculate));
                }

            }
        });

        _edt_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0 && _edt_height_inch.length() !=0 && _edt_height.length() !=0) {
                    double weight = Double.parseDouble(_edt_weight.getText().toString());
                    int ht_feet = Integer.parseInt(_edt_height.getText().toString());
                    int inchInFeet = Integer.parseInt(_edt_height_inch.getText().toString());
                    double inch_to_feet = inchInFeet * 0.0833333;
                    double total_feet = ht_feet + inch_to_feet;
                    double heightInMeter = total_feet * 0.3048;
                    Log.d(Utils.TAG, " heightInMeter: "+heightInMeter);

                    double bmi_calculate = weight/(heightInMeter*heightInMeter);
                    _txt_bmi.setText(String.valueOf(bmi_calculate));
                }

            }
        });

        _txt_bmi.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(_txt_bmi.length() != 0) {
                    double bmi_value = Double.parseDouble(_txt_bmi.getText().toString());
                    Log.d(Utils.TAG, " bmi_value: "+bmi_value);

                    if((bmi_value >= 18.5) && (bmi_value <= 24.9)) {
                        _txt_bmi_status.setText("Healthy");
                        _txt_bmi_status.setBackgroundColor(getResources().getColor(R.color.status_green));
                    }
                    else if((bmi_value >= 25) && (bmi_value <= 29.9)) {
                        _txt_bmi_status.setText("Over Weight");
                        _txt_bmi_status.setBackgroundColor(getResources().getColor(R.color.status_yellow));
                    }
                    else if(bmi_value >= 30) {
                        _txt_bmi_status.setText("Obese");
                        _txt_bmi_status.setBackgroundColor(getResources().getColor(R.color.status_red));
                    }
                    else {
                        _txt_bmi_status.setText("Under Weight");
                        _txt_bmi_status.setBackgroundColor(getResources().getColor(R.color.dot_dark_screen1));
                    }

                }

            }
        });*/

        /***************************** DRUG ALLERY SECTION **********************/

        LinearLayout load_drug_allery = (LinearLayout)  getActivity().findViewById(R.id.drug_allery_load);
        load_drug_allery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getJsonDrugAllergies(getActivity()).execute();
            }
        });

        drug_allery_clear = (LinearLayout) getActivity().findViewById(R.id.drug_allery_clear);
        drug_allery_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PATIENT_DRUG_ALLERTY_ARRAY = new ArrayList<>();
                refreshDrugAllergy();
            }
        });


        frequentDrugAllergyRecyclerview = (RecyclerView) getActivity().findViewById(R.id.medhist_drugallery_freq_recyclerview);
        frequentDrugAllergyRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), numberOfFrequentColumns));
        frequentDrugAlleryAdapter = new FrequentDrugAllergyAdapters(getActivity(), drugAlleryListArraylist);


        frequentDrugAlleryAdapter.setOnItemClickListener(new FrequentDrugAllergyAdapters.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int allergyId, int genericId, String genericName, int patientId, int docId, int docType) {
                Log.d(Utils.TAG, "onItemClick: ");
                Toast.makeText(getActivity(),genericName+ " is added to the list", Toast.LENGTH_SHORT).show();

                PATIENT_DRUG_ALLERTY_ARRAY.add(new DrugAllery(allergyId,genericId,genericName,patientId,docId,docType,USER_ID,USER_LOGIN_TYPE));
                refreshDrugAllergy();
            }
        });
        frequentDrugAllergyRecyclerview.setAdapter(frequentDrugAlleryAdapter);

       /* List<DrugAllery> freqAllergy = MedisensePracticeDB.getAllDrugAllery(getActivity(), USER_LOGIN_TYPE, USER_ID);
        if(freqAllergy.size() > 0) {
            drugAlleryListArraylist = new ArrayList<>();
            for (int j = 0; j < freqAllergy.size(); j++) {
                drugAlleryListArraylist.add(new DrugAllery(freqAllergy.get(j).getAllergyId(),freqAllergy.get(j).getGenericId(),
                        freqAllergy.get(j).getGenericName(),freqAllergy.get(j).getPatientId(),
                        freqAllergy.get(j).getDocId(),freqAllergy.get(j).getDocType(),
                        freqAllergy.get(j).getUserId(),freqAllergy.get(j).getLoginType()));
            }

            Log.d(Utils.TAG, "total count: " + drugAlleryListArraylist.size());
            frequentDrugAlleryAdapter = new FrequentDrugAllergyAdapters(getActivity(), drugAlleryListArraylist);
            frequentDrugAllergyRecyclerview.setAdapter(frequentDrugAlleryAdapter);
            frequentDrugAlleryAdapter.notifyDataSetChanged();
        }*/

        drugAlleryListArraylist = MedisensePracticeDB.getAllDrugAllery(getActivity(), USER_LOGIN_TYPE, USER_ID);
        if(drugAlleryListArraylist.size() > 0) {
            Log.d(Utils.TAG, " drugAllery count: " + drugAlleryListArraylist.size());
            frequentDrugAlleryAdapter = new FrequentDrugAllergyAdapters(getActivity(), drugAlleryListArraylist);
            frequentDrugAllergyRecyclerview.setAdapter(frequentDrugAlleryAdapter);
            frequentDrugAlleryAdapter.notifyDataSetChanged();
        }

        drugAllergyRecyclerview = (RecyclerView)  getActivity().findViewById(R.id.medhist_drugallery_recycler);
        drugAllergyAdapter = new DrugAllergyAdapters(getActivity(), PATIENT_DRUG_ALLERTY_ARRAY);
        RecyclerView.LayoutManager mLayoutManager6 = new LinearLayoutManager(getActivity());
        drugAllergyRecyclerview.setLayoutManager(mLayoutManager6);
        drugAllergyRecyclerview.setItemAnimator(new DefaultItemAnimator());
        drugAllergyRecyclerview.setAdapter(drugAllergyAdapter);

        LinearLayout add_drug_allergy = (LinearLayout) getActivity().findViewById(R.id.medhist_add_drug_allergy);
        add_drug_allergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareDrugAllergy(drugAlleryListArraylist);
            }
        });

        /* **************************** Drug Abuse Section Starts ****************************** */
        LinearLayout load_drug_abuse = (LinearLayout)  getActivity().findViewById(R.id.drug_abuse_load);
        load_drug_abuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getJsonDrugAbuse(getActivity()).execute();
            }
        });

        LinearLayout drug_abuse_clear = (LinearLayout) getActivity().findViewById(R.id.drug_abuse_clear);
        drug_abuse_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PATIENT_DRUG_ABUSE_ARRAY = new ArrayList<>();
                refreshDrugAbuse();
            }
        });

        frequentDrugAbuseRecyclerview = (RecyclerView) getActivity().findViewById(R.id.medhist_drugabuse_freq_recyclerview);
        frequentDrugAbuseRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), numberOfFrequentColumns));
        frequentDrugAbuseAdapter = new FrequentDrugAbuseAdapters(getActivity(), freqdrugAbuseListArraylist);

        frequentDrugAbuseAdapter.setOnItemClickListener(new FrequentDrugAbuseAdapters.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int abuseFrequentId, int abuseId, String abuseName, int docId, int docType, int freqCount) {
                Log.d(Utils.TAG, "onItemClick: ");
                Toast.makeText(getActivity(),abuseName+ " is added to the list", Toast.LENGTH_SHORT).show();

                PATIENT_DRUG_ABUSE_ARRAY.add(new DrugAbuse(abuseFrequentId,abuseId,abuseName,docId,docType,freqCount,USER_ID,USER_LOGIN_TYPE));
                refreshDrugAbuse();
            }
        });

        frequentDrugAbuseRecyclerview.setAdapter(frequentDrugAbuseAdapter);

      /*  List<DrugAbuse> freqAbuse = MedisensePracticeDB.getAllDrugAbuseFrequent(getActivity(), USER_LOGIN_TYPE, USER_ID);
        if(freqAbuse.size() > 0) {
            freqdrugAbuseListArraylist = new ArrayList<>();
            for (int j = 0; j < freqAbuse.size(); j++) {
                freqdrugAbuseListArraylist.add(new DrugAbuse(freqAbuse.get(j).getAbuseFrequentId(),freqAbuse.get(j).getAbuseId(),
                        freqAbuse.get(j).getAbuseName(),freqAbuse.get(j).getDocId(),
                        freqAbuse.get(j).getDocType(),freqAbuse.get(j).getFreqCount(),
                        freqAbuse.get(j).getUserId(),freqAbuse.get(j).getLoginType()));
            }

            Log.d(Utils.TAG, "total count: " + freqdrugAbuseListArraylist.size());
            frequentDrugAbuseAdapter = new FrequentDrugAbuseAdapters(getActivity(), freqdrugAbuseListArraylist);
            frequentDrugAbuseRecyclerview.setAdapter(frequentDrugAbuseAdapter);
            frequentDrugAbuseAdapter.notifyDataSetChanged();
        }
*/
        freqdrugAbuseListArraylist = MedisensePracticeDB.getAllDrugAbuseFrequent(getActivity(), USER_LOGIN_TYPE, USER_ID);
        if(freqdrugAbuseListArraylist.size() > 0) {
            Log.d(Utils.TAG, " freq abuse count: " + freqdrugAbuseListArraylist.size());
            frequentDrugAbuseAdapter = new FrequentDrugAbuseAdapters(getActivity(), freqdrugAbuseListArraylist);
            frequentDrugAbuseRecyclerview.setAdapter(frequentDrugAbuseAdapter);
            frequentDrugAbuseAdapter.notifyDataSetChanged();
        }

        /*List<DrugAbuse> drugAbuse = MedisensePracticeDB.getAllDrugAbuse(getActivity(), USER_LOGIN_TYPE, USER_ID);
        if(drugAbuse.size() > 0) {
            drugAbuseListArraylist = new ArrayList<>();
            for (int j = 0; j < drugAbuse.size(); j++) {
                drugAbuseListArraylist.add(new DrugAbuse(drugAbuse.get(j).getAbuseId(),
                        drugAbuse.get(j).getAbuseName(),drugAbuse.get(j).getDocId(),
                        drugAbuse.get(j).getDocType(), drugAbuse.get(j).getUserId(),drugAbuse.get(j).getLoginType()));
            }
        }*/

        drugAbuseListArraylist = MedisensePracticeDB.getAllDrugAbuse(getActivity(), USER_LOGIN_TYPE, USER_ID);
        if(drugAbuseListArraylist.size() > 0) {
            Log.d(Utils.TAG, " abuse count: " + drugAbuseListArraylist.size());
        }

        drugAbuseRecyclerview = (RecyclerView)  getActivity().findViewById(R.id.medhist_drugabuse_recycler);
        drugAbuseAdapter = new DrugAbuseAdapters(getActivity(), PATIENT_DRUG_ABUSE_ARRAY);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        drugAbuseRecyclerview.setLayoutManager(mLayoutManager2);
        drugAbuseRecyclerview.setItemAnimator(new DefaultItemAnimator());
        drugAbuseRecyclerview.setAdapter(drugAbuseAdapter);

        LinearLayout add_drug_abuse = (LinearLayout) getActivity().findViewById(R.id.medhist_add_drug_abuse);
        add_drug_abuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareDrugAbuse(drugAbuseListArraylist);
            }
        });


        /* ****************************Family History Section Starts ****************************** */
        LinearLayout load_family_history = (LinearLayout)  getActivity().findViewById(R.id.family_history_load);
        load_family_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getJsonFamilyHistory(getActivity()).execute();
            }
        });

        LinearLayout family_history_clear = (LinearLayout) getActivity().findViewById(R.id.family_history_clear);
        family_history_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PATIENT_FAMILY_HISTORY_ARRAY = new ArrayList<>();
                refreshFamilyHistory();
            }
        });

        frequentFamilyHistoryRecyclerview = (RecyclerView) getActivity().findViewById(R.id.medhist_familyhistory_freq_recyclerview);
        frequentFamilyHistoryRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), numberOfFrequentColumns));
        frequentFamilyHistoryAdapter = new FrequentFamilyHistoryAdapters(getActivity(), freqfamilyHistoryListArraylist);
        frequentFamilyHistoryAdapter.setOnItemClickListener(new FrequentFamilyHistoryAdapters.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int abuseFrequentId, int historyId, String historyName, int docId, int docType, int freqCount) {
                // Log.d(Utils.TAG, "onItemClick: ");
                Toast.makeText(getActivity(),historyName+ " is added to the list", Toast.LENGTH_SHORT).show();

                PATIENT_FAMILY_HISTORY_ARRAY.add(new FamilyHistory(abuseFrequentId,historyId,historyName,docId,docType,freqCount,USER_ID,USER_LOGIN_TYPE));
                refreshFamilyHistory();
            }
        });
        frequentFamilyHistoryRecyclerview.setAdapter(frequentFamilyHistoryAdapter);

       /* List<FamilyHistory> freqFamily = MedisensePracticeDB.getAllFamilyHistoryFrequent(getActivity(), USER_LOGIN_TYPE, USER_ID);
        if(freqFamily.size() > 0) {
            freqfamilyHistoryListArraylist = new ArrayList<>();
            for (int j = 0; j < freqFamily.size(); j++) {
                freqfamilyHistoryListArraylist.add(new FamilyHistory(freqFamily.get(j).getFamilyFrequentId(),freqFamily.get(j).getFamilyHistoryId(),
                        freqFamily.get(j).getFamilyHistoryName(),freqFamily.get(j).getDocId(),
                        freqFamily.get(j).getDocType(),freqFamily.get(j).getFreqCount(),
                        freqFamily.get(j).getUserId(),freqFamily.get(j).getLoginType()));
            }

            Log.d(Utils.TAG, "total count: " + freqfamilyHistoryListArraylist.size());
            frequentFamilyHistoryAdapter = new FrequentFamilyHistoryAdapters(getActivity(), freqfamilyHistoryListArraylist);
            frequentFamilyHistoryRecyclerview.setAdapter(frequentFamilyHistoryAdapter);
            frequentFamilyHistoryAdapter.notifyDataSetChanged();
        }*/

        freqfamilyHistoryListArraylist = MedisensePracticeDB.getAllFamilyHistoryFrequent(getActivity(), USER_LOGIN_TYPE, USER_ID);
        if(freqfamilyHistoryListArraylist.size() > 0) {
            Log.d(Utils.TAG, " freq family count: " + freqfamilyHistoryListArraylist.size());
            frequentFamilyHistoryAdapter = new FrequentFamilyHistoryAdapters(getActivity(), freqfamilyHistoryListArraylist);
            frequentFamilyHistoryRecyclerview.setAdapter(frequentFamilyHistoryAdapter);
            frequentFamilyHistoryAdapter.notifyDataSetChanged();
        }


        /*List<FamilyHistory> familyHistory = MedisensePracticeDB.getAllFamilyHistory(getActivity(), USER_LOGIN_TYPE, USER_ID);
        if(familyHistory.size() > 0) {
            familyHistoryListArraylist = new ArrayList<>();
            for (int j = 0; j < familyHistory.size(); j++) {
                familyHistoryListArraylist.add(new FamilyHistory(familyHistory.get(j).getFamilyHistoryId(),
                        familyHistory.get(j).getFamilyHistoryName(),familyHistory.get(j).getDocId(),
                        familyHistory.get(j).getDocType(), familyHistory.get(j).getUserId(),familyHistory.get(j).getLoginType()));
            }
        }*/

        familyHistoryListArraylist = MedisensePracticeDB.getAllFamilyHistory(getActivity(), USER_LOGIN_TYPE, USER_ID);
        if(familyHistoryListArraylist.size() > 0) {
            Log.d(Utils.TAG, " family count: " + familyHistoryListArraylist.size());
        }

        familyHistoryRecyclerview = (RecyclerView)  getActivity().findViewById(R.id.medhist_familyhistory_recycler);
        familyHistoryAdapter = new FamilyHistoryAdapters(getActivity(), PATIENT_FAMILY_HISTORY_ARRAY);
        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity());
        familyHistoryRecyclerview.setLayoutManager(mLayoutManager3);
        familyHistoryRecyclerview.setItemAnimator(new DefaultItemAnimator());
        familyHistoryRecyclerview.setAdapter(familyHistoryAdapter);

        LinearLayout add_family_history = (LinearLayout) getActivity().findViewById(R.id.medhist_add_familyhistory);
        add_family_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareFamilyHistory(familyHistoryListArraylist);
            }
        });


        sendMedicalHistoryRequestFromServer();


    }

    private void refreshFamilyHistory() {
        Log.d(Utils.TAG, "PATIENT_FAMILY_HISTORY_ARRAY: " + PATIENT_FAMILY_HISTORY_ARRAY.size());
        familyHistoryAdapter = new FamilyHistoryAdapters(getActivity(), PATIENT_FAMILY_HISTORY_ARRAY);
        familyHistoryRecyclerview.setAdapter(familyHistoryAdapter);
        familyHistoryAdapter.notifyDataSetChanged();
    }

    private void refreshDrugAbuse() {
        Log.d(Utils.TAG, "PATIENT_DRUG_ABUSE_ARRAY: " + PATIENT_DRUG_ABUSE_ARRAY.size());
        drugAbuseAdapter = new DrugAbuseAdapters(getActivity(), PATIENT_DRUG_ABUSE_ARRAY);
        drugAbuseRecyclerview.setAdapter(drugAbuseAdapter);
        drugAbuseAdapter.notifyDataSetChanged();
    }

    private void refreshDrugAllergy() {
        Log.d(Utils.TAG, "PATIENT_DRUG_ALLERTY_ARRAY: " + PATIENT_DRUG_ALLERTY_ARRAY.size());
        drugAllergyAdapter = new DrugAllergyAdapters(getActivity(), PATIENT_DRUG_ALLERTY_ARRAY);
        drugAllergyRecyclerview.setAdapter(drugAllergyAdapter);
        drugAllergyAdapter.notifyDataSetChanged();
    }

    private int getIndex(Spinner timings_spinner, String compareValue) {
        for (int i=0;i<timings_spinner.getCount();i++){
            if (timings_spinner.getItemAtPosition(i).toString().equalsIgnoreCase(compareValue)){
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.medhist_submit:
                InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(submit_btn.getWindowToken(), 0);

                collectMedicalHistoryDetails();
                break;
            case R.id.medhist_drugallery_btn:
                InputMethodManager imm2 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(drug_allery_btn.getWindowToken(), 0);

                if(view_drug_allery_status == false) {
                    drug_allery_contents.setVisibility(View.VISIBLE);
                    drug_allery_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_drug_allery_status = true;
                }
                else  if(view_drug_allery_status == true) {
                    drug_allery_contents.setVisibility(View.GONE);
                    drug_allery_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_drug_allery_status = false;
                }
                break;
            case R.id.medhist_drugabuse_btn:
                InputMethodManager imm3 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(drug_abuse_btn.getWindowToken(), 0);

                if(view_drug_abuse_status == false) {
                    drug_abuse_contents.setVisibility(View.VISIBLE);
                    drug_abuse_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_drug_abuse_status = true;
                }
                else  if(view_drug_abuse_status == true) {
                    drug_abuse_contents.setVisibility(View.GONE);
                    drug_abuse_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_drug_abuse_status = false;
                }
                break;
            case R.id.medhist_familyhistory_btn:
                InputMethodManager imm4 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm4.hideSoftInputFromWindow(family_history_btn.getWindowToken(), 0);

                if(view_family_history_status == false) {
                    family_history_contents.setVisibility(View.VISIBLE);
                    family_history_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_family_history_status = true;
                }
                else  if(view_family_history_status == true) {
                    family_history_contents.setVisibility(View.GONE);
                    family_history_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_family_history_status = false;
                }
                break;
            case R.id.medhist_refresh:
                InputMethodManager imm5 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm5.hideSoftInputFromWindow(refresh_btn.getWindowToken(), 0);

                sendMedicalHistoryRequestFromServer();
                break;
        }
    }


    private void collectMedicalHistoryDetails() {
        String weight = _edt_weight.getText().toString().trim();
        String height = _edt_height.getText().toString().trim();
        String bmi = _txt_bmi.getText().toString();
        String prev_intervention = _edt_prev_intervention.getText().toString();
        String stroke = _edt_stroke.getText().toString();
        String kidney_issue = _edt_kidney_issues.getText().toString();
        String other_details = _edt_otherdetails.getText().toString();

        PATIENT_WEIGHT = weight;
        PATIENT_HEIGHT = height;
        PATIENT_PREV_INTERVENTIONS = prev_intervention;
        PATIENT_STATE = stroke;
        PATIENT_KIDNEY_ISSUES = kidney_issue;
        PATIENT_OTHER_DETAILS = other_details;

        int checkedRadioButtonId = rg_hypertension.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {   // No items Selected
            PATIENT_HYPERTENSION = "0";
        }
        else{
            if (checkedRadioButtonId == R.id.radiohyperYes) {
                PATIENT_HYPERTENSION = "1";
            }
            else if (checkedRadioButtonId == R.id.radiohyperNo) {
                PATIENT_HYPERTENSION = "2";
            }
            else {
                PATIENT_HYPERTENSION = "0";
            }
        }

        int checkedRadioDiabetesButtonId = rg_diabetes.getCheckedRadioButtonId();
        if (checkedRadioDiabetesButtonId == -1) {   // No items Selected
            PATIENT_DIABETES = "0";
        }
        else{
            if (checkedRadioDiabetesButtonId == R.id.radiodiabetesYes) {
                PATIENT_DIABETES = "1";
            }
            else if (checkedRadioDiabetesButtonId == R.id.radiodiabetesNo) {
                PATIENT_DIABETES = "2";
            }
            else {
                PATIENT_DIABETES = "0";
            }
        }

        //Cholesterol
        int checkedRadioCholesterolButtonId = rg_cholesterol.getCheckedRadioButtonId();
        if (checkedRadioCholesterolButtonId == -1) {   // No items Selected
            PATIENT_CHOLESTEROL = "0";
        }
        else{
            if (checkedRadioCholesterolButtonId == R.id.radioCholesterolYes) {
                PATIENT_CHOLESTEROL = "1";
            }
            else if (checkedRadioCholesterolButtonId == R.id.radioCholesterolNo) {
                PATIENT_CHOLESTEROL = "2";
            }
            else {
                PATIENT_CHOLESTEROL = "0";
            }
        }

        //Arthritis
        int checkedRadioArthritisButtonId = rg_arthritis.getCheckedRadioButtonId();
        if (checkedRadioArthritisButtonId == -1) {   // No items Selected
            PATIENT_ARTHRITIS = "0";
        }
        else{
            if (checkedRadioArthritisButtonId == R.id.radioArthritisYes) {
                PATIENT_ARTHRITIS = "1";
            }
            else if (checkedRadioArthritisButtonId == R.id.radioArthritisNo) {
                PATIENT_ARTHRITIS = "2";
            }
            else {
                PATIENT_ARTHRITIS = "0";
            }
        }

        //IHD
        int checkedRadioIHDButtonId = rg_ihd.getCheckedRadioButtonId();
        if (checkedRadioIHDButtonId == -1) {   // No items Selected
            PATIENT_IHD = "0";
        }
        else{
            if (checkedRadioIHDButtonId == R.id.radioIHDYes) {
                PATIENT_IHD = "1";
            }
            else if (checkedRadioIHDButtonId == R.id.radioIHDNo) {
                PATIENT_IHD = "2";
            }
            else {
                PATIENT_IHD = "0";
            }
        }

        //ASTHAMA
        int checkedRadioAsthamaButtonId = rg_asthama.getCheckedRadioButtonId();
        if (checkedRadioAsthamaButtonId == -1) {   // No items Selected
            PATIENT_ASTHAMA = "0";
        }
        else{
            if (checkedRadioAsthamaButtonId == R.id.radioAsthamaYes) {
                PATIENT_ASTHAMA = "1";
            }
            else if (checkedRadioAsthamaButtonId == R.id.radioAsthamaNo) {
                PATIENT_ASTHAMA = "2";
            }
            else {
                PATIENT_ASTHAMA = "0";
            }
        }

        //THY
        int checkedRadioTHYButtonId = rg_thy.getCheckedRadioButtonId();
        if (checkedRadioTHYButtonId == -1) {   // No items Selected
            PATIENT_THY = "0";
        }
        else{
            if (checkedRadioTHYButtonId == R.id.radioTHYYes) {
                PATIENT_THY = "1";
            }
            else if (checkedRadioTHYButtonId == R.id.radioTHYNo) {
                PATIENT_THY = "2";
            }
            else {
                PATIENT_THY = "0";
            }
        }


        Log.d(Utils.TAG, " ******************* collectMedicalHistoryDetails **************** ");
        Log.d(Utils.TAG, " weight: "+weight);
        Log.d(Utils.TAG, " height: "+height);
        Log.d(Utils.TAG, " bmi: "+bmi);
        Log.d(Utils.TAG, " hypertension: "+PATIENT_HYPERTENSION);
        Log.d(Utils.TAG, " diabetes: "+PATIENT_DIABETES);
        Log.d(Utils.TAG, " smoking: "+PATIENT_SMOKING);
        Log.d(Utils.TAG, " alcohol: "+PATIENT_ALCOHOL);
        Log.d(Utils.TAG, " prevIntervention: "+prev_intervention);
        Log.d(Utils.TAG, " stroke: "+stroke);
        Log.d(Utils.TAG, " kidney: "+kidney_issue);
        Log.d(Utils.TAG, " other_details: "+other_details);

        Log.d(Utils.TAG, " PATIENT_CHOLESTEROL: "+PATIENT_CHOLESTEROL);
        Log.d(Utils.TAG, " PATIENT_ARTHRITIS: "+PATIENT_ARTHRITIS);
        Log.d(Utils.TAG, " PATIENT_IHD: "+PATIENT_IHD);
        Log.d(Utils.TAG, " PATIENT_ASTHAMA: "+PATIENT_ASTHAMA);
        Log.d(Utils.TAG, " PATIENT_THY: "+PATIENT_THY);

        for(int i=0;i<PATIENT_DRUG_ALLERTY_ARRAY.size();i++)
        {
            Log.d(Utils.TAG, " allergyId: "+ PATIENT_DRUG_ALLERTY_ARRAY.get(i).getAllergyId());
            Log.d(Utils.TAG, " genID: "+ PATIENT_DRUG_ALLERTY_ARRAY.get(i).getGenericId());
            Log.d(Utils.TAG, " genName: "+ PATIENT_DRUG_ALLERTY_ARRAY.get(i).getGenericName());
            Log.d(Utils.TAG, " patientID: "+ PATIENT_DRUG_ALLERTY_ARRAY.get(i).getPatientId());
            Log.d(Utils.TAG, " docid: "+ PATIENT_DRUG_ALLERTY_ARRAY.get(i).getDocId());
            Log.d(Utils.TAG, " doctype: "+ PATIENT_DRUG_ALLERTY_ARRAY.get(i).getDocType());
        }

        for(int i=0;i<PATIENT_DRUG_ABUSE_ARRAY.size();i++)
        {
            Log.d(Utils.TAG, " freqabuseId: "+ PATIENT_DRUG_ABUSE_ARRAY.get(i).getAbuseFrequentId());
            Log.d(Utils.TAG, " abuse ID: "+ PATIENT_DRUG_ABUSE_ARRAY.get(i).getAbuseId());
            Log.d(Utils.TAG, " abuse Name: "+ PATIENT_DRUG_ABUSE_ARRAY.get(i).getAbuseName());
            Log.d(Utils.TAG, " docid: "+ PATIENT_DRUG_ABUSE_ARRAY.get(i).getDocId());
            Log.d(Utils.TAG, " doctype: "+ PATIENT_DRUG_ABUSE_ARRAY.get(i).getDocType());
            Log.d(Utils.TAG, " abuse freqcount: "+ PATIENT_DRUG_ABUSE_ARRAY.get(i).getFreqCount());
        }


        for(int i=0;i<PATIENT_FAMILY_HISTORY_ARRAY.size();i++)
        {
            Log.d(Utils.TAG, " freqhistId: "+ PATIENT_FAMILY_HISTORY_ARRAY.get(i).getFamilyFrequentId());
            Log.d(Utils.TAG, " hist ID: "+ PATIENT_FAMILY_HISTORY_ARRAY.get(i).getFamilyHistoryId());
            Log.d(Utils.TAG, " hist Name: "+ PATIENT_FAMILY_HISTORY_ARRAY.get(i).getFamilyHistoryName());
            Log.d(Utils.TAG, " docid: "+ PATIENT_FAMILY_HISTORY_ARRAY.get(i).getDocId());
            Log.d(Utils.TAG, " doctype: "+ PATIENT_FAMILY_HISTORY_ARRAY.get(i).getDocType());
            Log.d(Utils.TAG, " hist freqcount: "+ PATIENT_FAMILY_HISTORY_ARRAY.get(i).getFreqCount());
        }

        submitHistoryToServer(weight,height,bmi,PATIENT_HYPERTENSION,PATIENT_DIABETES,PATIENT_SMOKING,PATIENT_ALCOHOL,
                prev_intervention,stroke,kidney_issue, other_details, PATIENT_DRUG_ALLERTY_ARRAY,PATIENT_DRUG_ABUSE_ARRAY,
                PATIENT_FAMILY_HISTORY_ARRAY,PATIENT_CHOLESTEROL, PATIENT_ARTHRITIS, PATIENT_IHD, PATIENT_ASTHAMA, PATIENT_THY);

    }

    class getJsonDrugAllergies extends AsyncTask<String,String,String> {

        private ProgressDialog dialog6;

        public getJsonDrugAllergies(FragmentActivity activity) {
            dialog6 = new ProgressDialog(activity, R.style.CustomDialog);
            dialog6.setMessage("Loading frequent drug allergies........");
            dialog6.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog6.setCancelable(false);
            dialog6.show();

        }

        @Override
        protected String doInBackground(String... key) {
            drugAlleryListArraylist = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_DRUG_ALLERY_FREQUENT_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG, response.toString());
                            if (response != null) {
                                JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null;
                                try {
                                    jsonObject1 = new JSONObject(response);
                                    String staus_res = jsonObject1.getString("status");
                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray1 = jsonObject1.getJSONArray("frequent_allergy_details");
                                        if (jsonArray1.length() > 0) {
                                            MedisensePracticeDB.clearDrugAllergy(getActivity(),USER_ID,USER_LOGIN_TYPE);
                                            for (int i = 0; i < jsonArray1.length(); i++) {
                                                drugAlleryListArraylist.add(new DrugAllery(jsonArray1.getJSONObject(i).getInt("allergy_id"),jsonArray1.getJSONObject(i).getInt("generic_id"),
                                                        jsonArray1.getJSONObject(i).getString("generic_name"), jsonArray1.getJSONObject(i).getInt("patient_id"),
                                                        jsonArray1.getJSONObject(i).getInt("doc_id"), jsonArray1.getJSONObject(i).getInt("doc_type"),
                                                        USER_ID, USER_LOGIN_TYPE));

                                                long freqallergy_insert = MedisensePracticeDB.drugAllergyInsert(getActivity(),jsonArray1.getJSONObject(i).getInt("allergy_id"),jsonArray1.getJSONObject(i).getInt("generic_id"),
                                                        jsonArray1.getJSONObject(i).getString("generic_name"),jsonArray1.getJSONObject(i).getInt("patient_id"),
                                                        jsonArray1.getJSONObject(i).getInt("doc_id"), jsonArray1.getJSONObject(i).getInt("doc_type"),
                                                        USER_ID, USER_LOGIN_TYPE);

                                            }

                                            Log.d(Utils.TAG, "freqAllergyListArraylist: " + drugAlleryListArraylist.size());

                                            frequentDrugAlleryAdapter = new FrequentDrugAllergyAdapters(getActivity(), drugAlleryListArraylist);
                                            frequentDrugAllergyRecyclerview.setAdapter(frequentDrugAlleryAdapter);
                                            frequentDrugAlleryAdapter.notifyDataSetChanged();

                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (dialog6.isShowing()) {
                                dialog6.dismiss();
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
                    map.put(APIClass.KEY_LOGINTYPE,USER_LOGIN_TYPE);
                    map.put(APIClass.KEY_PATIENTID, String.valueOf(PATIENT_ID));
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(getActivity()).
                    getRequestQueue();
            AppController.getInstance(getActivity()).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void prepareDrugAllergy(List<DrugAllery> drugAlleryListArraylist) {

        final Dialog dialog = new Dialog(getActivity(), R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_drug_allergy);
        dialog.setTitle("Add Drug Allergy");
        dialog.setCanceledOnTouchOutside(true);

        final SearchView drug_allergy_searchView = (SearchView) dialog.findViewById(R.id.searchview_drug_allergy);
        drug_allergy_listview = (ListView) dialog.findViewById(R.id.drug_allergy_list);
        final CustomTextViewBold close_btn = (CustomTextViewBold)  dialog.findViewById(R.id.drug_allergy_close);

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(close_btn.getWindowToken(), 0);

                dialog.dismiss();
                refreshDrugAllergy();
            }
        });

        drug_allergy_searchView.setIconifiedByDefault(false);
        drug_allergy_searchView.setQueryHint("Search Drug Allergy Here");
        drug_allergy_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                drug_allergy_searchView.setQuery("", false);
                drug_allergy_searchView.clearFocus();
                drug_allergy_searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Utils.TAG, "SERACH: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    Log.d(Utils.TAG, "is empty " );
                } else {
                    new loadDrugAllergyTask().execute(newText);
                }
                return true;
            }
        });

        drug_allergy_searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                drug_allergy_searchView.setQuery("", false);
                drug_allergy_searchView.setIconified(false);
                drug_allergy_searchView.clearFocus();
                return false;
            }
        });


        dialog.show();
    }

    private class loadDrugAllergyTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            //nothing
        }

        @Override
        protected String doInBackground(final String... strings) {
            drugAlleryListArraylist = new ArrayList<>();
            Log.d(Utils.TAG,  "Teram: "+ strings[0]);

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_DRUG_ALLERGY_SEARCH,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG, "prescInfo: "+ response.toString());
                            if (response != null) {
                                JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null;
                                try {
                                    jsonObject1 = new JSONObject(response);
                                    String staus_res = jsonObject1.getString("status");
                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray1 = jsonObject1.getJSONArray("drug_allergy_details");
                                        if(jsonArray1.length() > 0) {
                                            for (int i = 0; i < jsonArray1.length(); i++) {
                                                drugAlleryListArraylist.add(new DrugAllery(0,
                                                        jsonArray1.getJSONObject(i).getInt("generic_id"),jsonArray1.getJSONObject(i).getString("pharma_generic"),
                                                        PATIENT_ID,jsonArray1.getJSONObject(i).getInt("doc_id"),jsonArray1.getJSONObject(i).getInt("doc_type"),
                                                        USER_ID, USER_LOGIN_TYPE));
                                            }

                                            final CustomDrugAllergyFilterAdapter prescAdapter = new CustomDrugAllergyFilterAdapter(getActivity(),
                                                    R.layout.activity_listview, drugAlleryListArraylist);
                                            drug_allergy_listview.setAdapter(prescAdapter);
                                            drug_allergy_listview.setTextFilterEnabled(true);

                                            drug_allergy_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    DrugAllery myCustomAdapterItem = (DrugAllery) parent.getItemAtPosition(position);

                                                    Toast.makeText(getActivity(),myCustomAdapterItem.getGenericName()+ " is added to the list", Toast.LENGTH_SHORT).show();

                                                    PATIENT_DRUG_ALLERTY_ARRAY.add(new DrugAllery(myCustomAdapterItem.getAllergyId(),myCustomAdapterItem.getGenericId(),myCustomAdapterItem.getGenericName(),myCustomAdapterItem.getPatientId(),myCustomAdapterItem.getDocId(),myCustomAdapterItem.getDocType(),USER_ID,USER_LOGIN_TYPE));
                                                }
                                            });

                                            drugAllergyAdapter = new DrugAllergyAdapters(getActivity(), PATIENT_DRUG_ALLERTY_ARRAY);
                                            drugAllergyRecyclerview.setAdapter(drugAllergyAdapter);
                                            drugAllergyAdapter.notifyDataSetChanged();

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
                    map.put(APIClass.KEY_LOGINTYPE,USER_LOGIN_TYPE);
                    map.put("searchTerm",strings[0]);
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(getActivity()).
                    getRequestQueue();
            AppController.getInstance(getActivity()).addToRequestQueue(stringRequest1);
            return null;
        }
    }

    class getJsonDrugAbuse extends AsyncTask<String,String,String> {

        private ProgressDialog dialog6;

        public getJsonDrugAbuse(FragmentActivity activity) {
            dialog6 = new ProgressDialog(activity, R.style.CustomDialog);
            dialog6.setMessage("Loading frequent drug abuse........");
            dialog6.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog6.setCancelable(false);
            dialog6.show();

        }

        @Override
        protected String doInBackground(String... key) {
            freqdrugAbuseListArraylist = new ArrayList<>();
            drugAbuseListArraylist  = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_DRUG_ABUSE_FREQUENT_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG, response.toString());
                            if (response != null) {
                                JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null;
                                try {
                                    jsonObject1 = new JSONObject(response);
                                    String staus_res = jsonObject1.getString("status");
                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray1 = jsonObject1.getJSONArray("frequent_drug_abuse_details");
                                        if (jsonArray1.length() > 0) {
                                            MedisensePracticeDB.clearDrugAbuseFrequent(getActivity(),USER_ID,USER_LOGIN_TYPE);
                                            for (int i = 0; i < jsonArray1.length(); i++) {
                                                freqdrugAbuseListArraylist.add(new DrugAbuse(jsonArray1.getJSONObject(i).getInt("fda_id"),jsonArray1.getJSONObject(i).getInt("drug_abuse_id"),
                                                        jsonArray1.getJSONObject(i).getString("drug_abuse"), jsonArray1.getJSONObject(i).getInt("doc_id"),
                                                        jsonArray1.getJSONObject(i).getInt("doc_type"), jsonArray1.getJSONObject(i).getInt("freq_count"),
                                                        USER_ID, USER_LOGIN_TYPE));

                                                long freqabuse_insert = MedisensePracticeDB.drugAbuseFrequentInsert(getActivity(),jsonArray1.getJSONObject(i).getInt("fda_id"),jsonArray1.getJSONObject(i).getInt("drug_abuse_id"),
                                                        jsonArray1.getJSONObject(i).getString("drug_abuse"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                                        jsonArray1.getJSONObject(i).getInt("doc_type"), jsonArray1.getJSONObject(i).getInt("freq_count"),
                                                        USER_ID, USER_LOGIN_TYPE);

                                            }

                                            Log.d(Utils.TAG, "freqdrugAbuseListArraylist: " + freqdrugAbuseListArraylist.size());

                                            frequentDrugAbuseAdapter = new FrequentDrugAbuseAdapters(getActivity(), freqdrugAbuseListArraylist);
                                            frequentDrugAbuseRecyclerview.setAdapter(frequentDrugAbuseAdapter);
                                            frequentDrugAbuseAdapter.notifyDataSetChanged();
                                        }

                                        jsonArray2 = jsonObject1.getJSONArray("drug_abuse_details");
                                        if (jsonArray2.length() > 0) {
                                            MedisensePracticeDB.clearDrugAbuse(getActivity(),USER_ID,USER_LOGIN_TYPE);
                                            for (int i = 0; i < jsonArray2.length(); i++) {
                                                drugAbuseListArraylist.add(new DrugAbuse(jsonArray2.getJSONObject(i).getInt("drug_abuse_id"),
                                                        jsonArray2.getJSONObject(i).getString("drug_abuse"), jsonArray2.getJSONObject(i).getInt("doc_id"),
                                                        jsonArray2.getJSONObject(i).getInt("doc_type"), USER_ID, USER_LOGIN_TYPE));

                                                long abuse_insert = MedisensePracticeDB.drugAbuseInsert(getActivity(),jsonArray2.getJSONObject(i).getInt("drug_abuse_id"),
                                                        jsonArray2.getJSONObject(i).getString("drug_abuse"),jsonArray2.getJSONObject(i).getInt("doc_id"),
                                                        jsonArray2.getJSONObject(i).getInt("doc_type"), USER_ID, USER_LOGIN_TYPE);
                                            }
                                            Log.d(Utils.TAG, "drugAbuseListArraylist: " + drugAbuseListArraylist.size());
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (dialog6.isShowing()) {
                                dialog6.dismiss();
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
                    map.put(APIClass.KEY_LOGINTYPE,USER_LOGIN_TYPE);
                    map.put(APIClass.KEY_PATIENTID, String.valueOf(PATIENT_ID));
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(getActivity()).
                    getRequestQueue();
            AppController.getInstance(getActivity()).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void prepareDrugAbuse(List<DrugAbuse> drugAbuseListArraylist) {
        final Dialog dialog = new Dialog(getActivity(), R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_drug_abuse);
        dialog.setTitle("Add Drug Abuse");
        dialog.setCanceledOnTouchOutside(true);

        final SearchView drug_abuse_searchView = (SearchView) dialog.findViewById(R.id.searchview_drug_abuse);
        ListView drug_abuse_listview = (ListView) dialog.findViewById(R.id.drug_abuse_list);
        final ImageView add_drug_abuse = (ImageView) dialog.findViewById(R.id.drug_abuse_add);
        final CustomTextViewBold close_btn = (CustomTextViewBold)  dialog.findViewById(R.id.drug_abuse_close);

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(close_btn.getWindowToken(), 0);
                dialog.dismiss();
                refreshDrugAbuse();
            }
        });

        add_drug_abuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(add_drug_abuse.getWindowToken(), 0);

                if(drug_abuse_searchView.getQuery().toString().trim().equals("")) {
                    Toast.makeText(getActivity()," Enter Drug Abuse !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(),drug_abuse_searchView.getQuery().toString()+ " is added to the list", Toast.LENGTH_SHORT).show();
                    PATIENT_DRUG_ABUSE_ARRAY.add(new DrugAbuse(0,0,drug_abuse_searchView.getQuery().toString(),
                            USER_ID,1,0,USER_ID, USER_LOGIN_TYPE));
                }

                drug_abuse_searchView.setQuery("", false);
                drug_abuse_searchView.clearFocus();
                drug_abuse_searchView.setIconified(true);
            }
        });

        final CustomDrugAbuseFilterAdapter abuseAdapter = new CustomDrugAbuseFilterAdapter(getActivity(),
                R.layout.activity_listview, drugAbuseListArraylist);
        drug_abuse_listview.setAdapter(abuseAdapter);
        drug_abuse_listview.setTextFilterEnabled(true);

        drug_abuse_searchView.setIconifiedByDefault(false);
        drug_abuse_searchView.setQueryHint("Search Drug Abuse Here");
        drug_abuse_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                drug_abuse_searchView.setQuery("", false);
                drug_abuse_searchView.clearFocus();
                drug_abuse_searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Utils.TAG, "SERACH: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    Log.d(Utils.TAG, "is empty " );
                    abuseAdapter.filter(newText.toString());
                } else {
                    abuseAdapter.filter(newText.toString());
                }
                return true;
            }
        });

        drug_abuse_searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                drug_abuse_searchView.setQuery("", false);
                drug_abuse_searchView.setIconified(false);
                drug_abuse_searchView.clearFocus();
                return false;
            }
        });

        drug_abuse_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrugAbuse myCustomAdapterItem = (DrugAbuse) parent.getItemAtPosition(position);

                drug_abuse_searchView.setQuery("", false);
                drug_abuse_searchView.setIconified(false);
                drug_abuse_searchView.clearFocus();

                Toast.makeText(getActivity(),myCustomAdapterItem.getAbuseName()+ " is added to the list", Toast.LENGTH_SHORT).show();

                PATIENT_DRUG_ABUSE_ARRAY.add(new DrugAbuse(myCustomAdapterItem.getAbuseFrequentId(),myCustomAdapterItem.getAbuseId(),myCustomAdapterItem.getAbuseName(),
                        myCustomAdapterItem.getDocId(),myCustomAdapterItem.getDocType(),myCustomAdapterItem.getFreqCount(),USER_ID, USER_LOGIN_TYPE));

            }
        });

        dialog.show();
    }


    class getJsonFamilyHistory extends AsyncTask<String,String,String> {

        private ProgressDialog dialog6;

        public getJsonFamilyHistory(FragmentActivity activity) {
            dialog6 = new ProgressDialog(activity, R.style.CustomDialog);
            dialog6.setMessage("Loading frequent Family History........");
            dialog6.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog6.setCancelable(false);
            dialog6.show();

        }

        @Override
        protected String doInBackground(String... key) {
            freqfamilyHistoryListArraylist = new ArrayList<>();
            familyHistoryListArraylist  = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_FAMILY_HISTORY_FREQUENT_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG, response.toString());
                            if (response != null) {
                                JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null;
                                try {
                                    jsonObject1 = new JSONObject(response);
                                    String staus_res = jsonObject1.getString("status");
                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray1 = jsonObject1.getJSONArray("frequent_family_history_details");
                                        if (jsonArray1.length() > 0) {
                                            MedisensePracticeDB.clearFamilyHistoryFrequent(getActivity(),USER_ID,USER_LOGIN_TYPE);
                                            for (int i = 0; i < jsonArray1.length(); i++) {
                                                freqfamilyHistoryListArraylist.add(new FamilyHistory(jsonArray1.getJSONObject(i).getInt("ffh_id"),jsonArray1.getJSONObject(i).getInt("family_history_id"),
                                                        jsonArray1.getJSONObject(i).getString("family_history"), jsonArray1.getJSONObject(i).getInt("doc_id"),
                                                        jsonArray1.getJSONObject(i).getInt("doc_type"), jsonArray1.getJSONObject(i).getInt("freq_count"),
                                                        USER_ID, USER_LOGIN_TYPE));

                                                long freqhistory_insert = MedisensePracticeDB.familyHistoryFrequentInsert(getActivity(),jsonArray1.getJSONObject(i).getInt("ffh_id"),jsonArray1.getJSONObject(i).getInt("family_history_id"),
                                                        jsonArray1.getJSONObject(i).getString("family_history"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                                        jsonArray1.getJSONObject(i).getInt("doc_type"), jsonArray1.getJSONObject(i).getInt("freq_count"),
                                                        USER_ID, USER_LOGIN_TYPE);

                                            }

                                            Log.d(Utils.TAG, "freqfamilyHistoryListArraylist: " + freqfamilyHistoryListArraylist.size());

                                            frequentFamilyHistoryAdapter = new FrequentFamilyHistoryAdapters(getActivity(), freqfamilyHistoryListArraylist);
                                            frequentFamilyHistoryRecyclerview.setAdapter(frequentFamilyHistoryAdapter);
                                            frequentFamilyHistoryAdapter.notifyDataSetChanged();

                                        }

                                        jsonArray2 = jsonObject1.getJSONArray("family_history_details");
                                        if (jsonArray2.length() > 0) {
                                            MedisensePracticeDB.clearFamilyHistory(getActivity(),USER_ID,USER_LOGIN_TYPE);
                                            for (int i = 0; i < jsonArray2.length(); i++) {
                                                familyHistoryListArraylist.add(new FamilyHistory(jsonArray2.getJSONObject(i).getInt("family_history_id"),
                                                        jsonArray2.getJSONObject(i).getString("family_history"), jsonArray2.getJSONObject(i).getInt("doc_id"),
                                                        jsonArray2.getJSONObject(i).getInt("doc_type"), USER_ID, USER_LOGIN_TYPE));

                                                long family_insert = MedisensePracticeDB.familyHistoryInsert(getActivity(),jsonArray2.getJSONObject(i).getInt("family_history_id"),
                                                        jsonArray2.getJSONObject(i).getString("family_history"),jsonArray2.getJSONObject(i).getInt("doc_id"),
                                                        jsonArray2.getJSONObject(i).getInt("doc_type"), USER_ID, USER_LOGIN_TYPE);
                                            }
                                            Log.d(Utils.TAG, "familyHistoryListArraylist: " + familyHistoryListArraylist.size());
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (dialog6.isShowing()) {
                                dialog6.dismiss();
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
                    map.put(APIClass.KEY_LOGINTYPE,USER_LOGIN_TYPE);
                    map.put(APIClass.KEY_PATIENTID, String.valueOf(PATIENT_ID));
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(getActivity()).
                    getRequestQueue();
            AppController.getInstance(getActivity()).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void prepareFamilyHistory(List<FamilyHistory> familyHistoryListArraylist) {
        final Dialog dialog = new Dialog(getActivity(), R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_family_history);
        dialog.setTitle("Add Family History");
        dialog.setCanceledOnTouchOutside(true);

        final SearchView family_history_searchView = (SearchView) dialog.findViewById(R.id.searchview_family_history);
        ListView family_history_listview = (ListView) dialog.findViewById(R.id.family_history_list);
        final ImageView add_family_history = (ImageView) dialog.findViewById(R.id.family_history_add);
        final CustomTextViewBold close_btn = (CustomTextViewBold)  dialog.findViewById(R.id.family_history_close);

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(close_btn.getWindowToken(), 0);
                dialog.dismiss();
                refreshFamilyHistory();
            }
        });

        add_family_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(add_family_history.getWindowToken(), 0);

                if(family_history_searchView.getQuery().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), " Enter Family History !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(),family_history_searchView.getQuery().toString()+ " is added to the list", Toast.LENGTH_SHORT).show();

                    PATIENT_FAMILY_HISTORY_ARRAY.add(new FamilyHistory(0,0,family_history_searchView.getQuery().toString(),
                            USER_ID,1,0,USER_ID, USER_LOGIN_TYPE));
                }

                family_history_searchView.setQuery("", false);
                family_history_searchView.clearFocus();
                family_history_searchView.setIconified(true);
            }
        });

        final CustomFamilyHistoryFilterAdapter familyHistoryAdapter = new CustomFamilyHistoryFilterAdapter(getActivity(),
                R.layout.activity_listview, familyHistoryListArraylist);
        family_history_listview.setAdapter(familyHistoryAdapter);
        family_history_listview.setTextFilterEnabled(true);

        family_history_searchView.setIconifiedByDefault(false);
        family_history_searchView.setQueryHint("Search Family History Here");
        family_history_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                family_history_searchView.setQuery("", false);
                family_history_searchView.clearFocus();
                family_history_searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Utils.TAG, "SERACH: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    Log.d(Utils.TAG, "is empty " );
                    familyHistoryAdapter.filter(newText.toString());
                } else {
                    familyHistoryAdapter.filter(newText.toString());
                }
                return true;
            }
        });

        family_history_searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                family_history_searchView.setQuery("", false);
                family_history_searchView.setIconified(false);
                family_history_searchView.clearFocus();
                return false;
            }
        });

        family_history_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FamilyHistory myCustomAdapterItem = (FamilyHistory) parent.getItemAtPosition(position);

                family_history_searchView.setQuery("", false);
                family_history_searchView.setIconified(false);
                family_history_searchView.clearFocus();

                Toast.makeText(getActivity(),myCustomAdapterItem.getFamilyHistoryName()+ " is added to the list", Toast.LENGTH_SHORT).show();

                PATIENT_FAMILY_HISTORY_ARRAY.add(new FamilyHistory(myCustomAdapterItem.getFamilyFrequentId(),myCustomAdapterItem.getFamilyHistoryId(),myCustomAdapterItem.getFamilyHistoryName(),
                        myCustomAdapterItem.getDocId(),myCustomAdapterItem.getDocType(),myCustomAdapterItem.getFreqCount(),USER_ID, USER_LOGIN_TYPE));

            }
        });

        dialog.show();
    }

    private void submitHistoryToServer(final String weight, final String height, final String bmi, final String patient_hypertension,
                                       final String patient_diabetes, final String patient_smoking, final String patient_alcohol,
                                       final String prev_intervention, final String stroke, final String kidney_issue, final String other_details,
                                       final List<DrugAllery> patient_drug_allerty_array, final List<DrugAbuse> patient_drug_abuse_array,
                                       final List<FamilyHistory> patient_family_history_array, final String PATIENT_CHOLESTEROL, final String PATIENT_ARTHRITIS, final String PATIENT_IHD, final String PATIENT_ASTHAMA, final String PATIENT_THY) {

        new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;
            ProgressDialog progressDialog;
            JSONArray jsonArray_drug_allergy, jsonArray_drug_abuse_frequent,jsonArray_drug_abuse, jsonArray_family_history_frequent, jsonArray_family_history;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {

                    JSONObject jsonObject = JSONParser.updateMedicalHistory(PATIENT_ID, PATIENT_NAME,
                            weight, height, bmi, patient_hypertension, patient_diabetes, patient_smoking, patient_alcohol,
                            prev_intervention, stroke, kidney_issue, other_details, patient_drug_allerty_array,
                            patient_drug_abuse_array, patient_family_history_array,PATIENT_CHOLESTEROL,
                            PATIENT_ARTHRITIS, PATIENT_IHD, PATIENT_ASTHAMA, PATIENT_THY,
                            USER_ID, USER_LOGIN_TYPE);

                    if (jsonObject != null) {
                        Log.e(Utils.TAG, " GET: " + jsonObject.getString("result"));
                        if (jsonObject.getString("result").equals("success")) {
                            status = true;

                            if (jsonObject.has("frequent_allergy_details")) {
                                jsonArray_drug_allergy = jsonObject.getJSONArray("frequent_allergy_details");
                            }

                            if (jsonObject.has("frequent_drug_abuse_details")) {
                                jsonArray_drug_abuse_frequent = jsonObject.getJSONArray("frequent_drug_abuse_details");
                            }

                            if (jsonObject.has("drug_abuse_details")) {
                                jsonArray_drug_abuse = jsonObject.getJSONArray("drug_abuse_details");
                            }

                            if (jsonObject.has("frequent_family_history_details")) {
                                jsonArray_family_history_frequent = jsonObject.getJSONArray("frequent_family_history_details");
                            }

                            if (jsonObject.has("family_history_details")) {
                                jsonArray_family_history = jsonObject.getJSONArray("family_history_details");
                            }

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
                if (progressDialog != null)
                    progressDialog.dismiss();
                Log.e(Utils.TAG, "aBoolean: " + String.valueOf(aBoolean));
                if (aBoolean) {

                    if (sharedPreferences != null) {
                        shareadPreferenceClass.clearsetMyPatientEpisodes();
                    }

                    try {
                        if(jsonArray_drug_allergy.length() > 0) {
                            drugAlleryListArraylist = new ArrayList<>();
                            MedisensePracticeDB.clearDrugAllergy(getActivity(),USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_drug_allergy.length(); i++) {
                                drugAlleryListArraylist.add(new DrugAllery(jsonArray_drug_allergy.getJSONObject(i).getInt("allergy_id"),jsonArray_drug_allergy.getJSONObject(i).getInt("generic_id"),
                                        jsonArray_drug_allergy.getJSONObject(i).getString("generic_name"), jsonArray_drug_allergy.getJSONObject(i).getInt("patient_id"),
                                        jsonArray_drug_allergy.getJSONObject(i).getInt("doc_id"), jsonArray_drug_allergy.getJSONObject(i).getInt("doc_type"),
                                        USER_ID, USER_LOGIN_TYPE));


                                long freqallergy_insert = MedisensePracticeDB.drugAllergyInsert(getActivity(),jsonArray_drug_allergy.getJSONObject(i).getInt("allergy_id"),jsonArray_drug_allergy.getJSONObject(i).getInt("generic_id"),
                                        jsonArray_drug_allergy.getJSONObject(i).getString("generic_name"),jsonArray_drug_allergy.getJSONObject(i).getInt("patient_id"),
                                        jsonArray_drug_allergy.getJSONObject(i).getInt("doc_id"), jsonArray_drug_allergy.getJSONObject(i).getInt("doc_type"),
                                        USER_ID, USER_LOGIN_TYPE);
                            }

                            Log.d(Utils.TAG, "freqAllergyListArraylist: " + drugAlleryListArraylist.size());
                            frequentDrugAlleryAdapter = new FrequentDrugAllergyAdapters(getActivity(), drugAlleryListArraylist);
                            frequentDrugAllergyRecyclerview.setAdapter(frequentDrugAlleryAdapter);
                            frequentDrugAlleryAdapter.notifyDataSetChanged();

                        }

                        if(jsonArray_drug_abuse_frequent.length() > 0) {
                            freqdrugAbuseListArraylist = new ArrayList<>();
                            MedisensePracticeDB.clearDrugAbuseFrequent(getActivity(),USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_drug_abuse_frequent.length(); i++) {
                                freqdrugAbuseListArraylist.add(new DrugAbuse(jsonArray_drug_abuse_frequent.getJSONObject(i).getInt("fda_id"),jsonArray_drug_abuse_frequent.getJSONObject(i).getInt("drug_abuse_id"),
                                        jsonArray_drug_abuse_frequent.getJSONObject(i).getString("drug_abuse"), jsonArray_drug_abuse_frequent.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_drug_abuse_frequent.getJSONObject(i).getInt("doc_type"), jsonArray_drug_abuse_frequent.getJSONObject(i).getInt("freq_count"),
                                        USER_ID, USER_LOGIN_TYPE));

                                long freqabuse_insert = MedisensePracticeDB.drugAbuseFrequentInsert(getActivity(),jsonArray_drug_abuse_frequent.getJSONObject(i).getInt("fda_id"),jsonArray_drug_abuse_frequent.getJSONObject(i).getInt("drug_abuse_id"),
                                        jsonArray_drug_abuse_frequent.getJSONObject(i).getString("drug_abuse"),jsonArray_drug_abuse_frequent.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_drug_abuse_frequent.getJSONObject(i).getInt("doc_type"), jsonArray_drug_abuse_frequent.getJSONObject(i).getInt("freq_count"),
                                        USER_ID, USER_LOGIN_TYPE);

                            }

                            Log.d(Utils.TAG, "freqdrugAbuseListArraylist: " + freqdrugAbuseListArraylist.size());
                            frequentDrugAbuseAdapter = new FrequentDrugAbuseAdapters(getActivity(), freqdrugAbuseListArraylist);
                            frequentDrugAbuseRecyclerview.setAdapter(frequentDrugAbuseAdapter);
                            frequentDrugAbuseAdapter.notifyDataSetChanged();
                        }

                        if(jsonArray_drug_abuse.length() > 0) {
                            drugAbuseListArraylist = new ArrayList<>();
                            MedisensePracticeDB.clearDrugAbuse(getActivity(),USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_drug_abuse.length(); i++) {
                                drugAbuseListArraylist.add(new DrugAbuse(jsonArray_drug_abuse.getJSONObject(i).getInt("drug_abuse_id"),
                                        jsonArray_drug_abuse.getJSONObject(i).getString("drug_abuse"), jsonArray_drug_abuse.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_drug_abuse.getJSONObject(i).getInt("doc_type"), USER_ID, USER_LOGIN_TYPE));

                                long abuse_insert = MedisensePracticeDB.drugAbuseInsert(getActivity(),jsonArray_drug_abuse.getJSONObject(i).getInt("drug_abuse_id"),
                                        jsonArray_drug_abuse.getJSONObject(i).getString("drug_abuse"),jsonArray_drug_abuse.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_drug_abuse.getJSONObject(i).getInt("doc_type"), USER_ID, USER_LOGIN_TYPE);
                            }
                            Log.d(Utils.TAG, "drugAbuseListArraylist: " + drugAbuseListArraylist.size());
                        }

                        if(jsonArray_family_history_frequent.length() > 0) {
                            freqfamilyHistoryListArraylist = new ArrayList<>();
                            MedisensePracticeDB.clearFamilyHistoryFrequent(getActivity(),USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_family_history_frequent.length(); i++) {
                                freqfamilyHistoryListArraylist.add(new FamilyHistory(jsonArray_family_history_frequent.getJSONObject(i).getInt("ffh_id"),jsonArray_family_history_frequent.getJSONObject(i).getInt("family_history_id"),
                                        jsonArray_family_history_frequent.getJSONObject(i).getString("family_history"), jsonArray_family_history_frequent.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_family_history_frequent.getJSONObject(i).getInt("doc_type"), jsonArray_family_history_frequent.getJSONObject(i).getInt("freq_count"),
                                        USER_ID, USER_LOGIN_TYPE));

                                long freqhistory_insert = MedisensePracticeDB.familyHistoryFrequentInsert(getActivity(),jsonArray_family_history_frequent.getJSONObject(i).getInt("ffh_id"),jsonArray_family_history_frequent.getJSONObject(i).getInt("family_history_id"),
                                        jsonArray_family_history_frequent.getJSONObject(i).getString("family_history"),jsonArray_family_history_frequent.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_family_history_frequent.getJSONObject(i).getInt("doc_type"), jsonArray_family_history_frequent.getJSONObject(i).getInt("freq_count"),
                                        USER_ID, USER_LOGIN_TYPE);

                            }

                            Log.d(Utils.TAG, "freqfamilyHistoryListArraylist: " + freqfamilyHistoryListArraylist.size());
                            frequentFamilyHistoryAdapter = new FrequentFamilyHistoryAdapters(getActivity(), freqfamilyHistoryListArraylist);
                            frequentFamilyHistoryRecyclerview.setAdapter(frequentFamilyHistoryAdapter);
                            frequentFamilyHistoryAdapter.notifyDataSetChanged();
                        }

                        if(jsonArray_family_history.length() > 0) {
                            familyHistoryListArraylist  = new ArrayList<>();
                            MedisensePracticeDB.clearFamilyHistory(getActivity(),USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_family_history.length(); i++) {
                                familyHistoryListArraylist.add(new FamilyHistory(jsonArray_family_history.getJSONObject(i).getInt("family_history_id"),
                                        jsonArray_family_history.getJSONObject(i).getString("family_history"), jsonArray_family_history.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_family_history.getJSONObject(i).getInt("doc_type"), USER_ID, USER_LOGIN_TYPE));

                                long family_insert = MedisensePracticeDB.familyHistoryInsert(getActivity(),jsonArray_family_history.getJSONObject(i).getInt("family_history_id"),
                                        jsonArray_family_history.getJSONObject(i).getString("family_history"),jsonArray_family_history.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_family_history.getJSONObject(i).getInt("doc_type"), USER_ID, USER_LOGIN_TYPE);
                            }
                            Log.d(Utils.TAG, "familyHistoryListArraylist: " + familyHistoryListArraylist.size());
                        }

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Toast.makeText(SignUpActivity.this,"Registered Successfully", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
                    alertDialogBuilder.setMessage("Medical History Updated Successfully. ");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent bundle = new Intent(getActivity(), MyPatientOphthalHomeActivity.class);
                                    bundle.putExtra("title","Patient Details");
                                    bundle.putExtra("PATIENT_ID", PATIENT_ID);
                                    bundle.putExtra("PATIENT_ID", PATIENT_ID);
                                    bundle.putExtra("PATIENT_NAME", PATIENT_NAME);
                                    bundle.putExtra("PATIENT_AGE", PATIENT_AGE);
                                    bundle.putExtra("PATIENT_GENDER", PATIENT_GENDER);
                                    bundle.putExtra("PATIENT_MOBILE", PATIENT_MOBILE);
                                    bundle.putExtra("PATIENT_EMAIL", PATIENT_EMAIL);
                                    bundle.putExtra("PATIENT_CITY", PATIENT_CITY);
                                    bundle.putExtra("PATIENT_ADDRESS", PATIENT_ADDRESS);
                                    bundle.putExtra("PATIENT_STATE", PATIENT_STATE);
                                    bundle.putExtra("PATIENT_COUNTRY_NAME", PATIENT_COUNTRY);
                                    bundle.putExtra("PATIENT_WEIGHT", PATIENT_WEIGHT);
                                    bundle.putExtra("PATIENT_HYPERTENSION", PATIENT_HYPERTENSION);
                                    bundle.putExtra("PATIENT_DIABETES", PATIENT_DIABETES);
                                    bundle.putExtra("PATIENT_SMOKING", PATIENT_SMOKING);
                                    bundle.putExtra("PATIENT_ALCOHOL", PATIENT_ALCOHOL);
                                    bundle.putExtra("PATIENT_DRUG_ABUSE", PATIENT_DRUG_ABUSE);
                                    bundle.putExtra("PATIENT_OTHER_DETAILS", PATIENT_OTHER_DETAILS);
                                    bundle.putExtra("PATIENT_FAMILY_HISTORY", PATIENT_FAMILY_HISTORY);
                                    bundle.putExtra("PATIENT_PREV_INTERVENTIONS", PATIENT_PREV_INTERVENTIONS);
                                    bundle.putExtra("PATIENT_NEURO_ISSUES", PATIENT_NEURO_ISSUES);
                                    bundle.putExtra("PATIENT_KIDNEY_ISSUES", PATIENT_KIDNEY_ISSUES);
                                    bundle.putExtra("PATIENT_HEIGHT", PATIENT_HEIGHT);
                                    getActivity().startActivity(bundle);
                                    getActivity().finish();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(getActivity(), "Failed to update Medical History. \nTry later !!!", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }


    private void sendMedicalHistoryRequestFromServer() {
        PATIENT_DRUG_ALLERTY_ARRAY = new ArrayList<>();
        PATIENT_DRUG_ABUSE_ARRAY = new ArrayList<>();
        PATIENT_FAMILY_HISTORY_ARRAY = new ArrayList<>();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Medical History...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PATIENT_MEDICAL_HISTORY_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, " medHist: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArrayAllergy, jsonArrayDrubAbuse, jsonArrayFamilyHistory;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("patient_details");
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        _edt_weight.setText(jsonArray.getJSONObject(j).getString("weight"));
                                        _edt_height.setText(jsonArray.getJSONObject(j).getString("height"));
                                        _edt_prev_intervention.setText(jsonArray.getJSONObject(j).getString("prev_inter"));
                                        _edt_stroke.setText(jsonArray.getJSONObject(j).getString("neuro_issue"));
                                        _edt_kidney_issues.setText(jsonArray.getJSONObject(j).getString("kidney_issue"));
                                        _edt_otherdetails.setText(jsonArray.getJSONObject(j).getString("other_details"));


                                        if(jsonArray.getJSONObject(j).getString("hyper_cond").equalsIgnoreCase("1")) {
                                            rb_hyperYes.setChecked(true);
                                        }
                                        else if(jsonArray.getJSONObject(j).getString("hyper_cond").equalsIgnoreCase("2")) {
                                            rb_hyperNo.setChecked(true);
                                        }
                                        else if(jsonArray.getJSONObject(j).getString("hyper_cond").equalsIgnoreCase("0")) {
                                            rb_hyperYes.setChecked(false);
                                            rb_hyperNo.setChecked(false);
                                        }
                                        else {
                                            rb_hyperYes.setChecked(false);
                                            rb_hyperNo.setChecked(false);
                                        }

                                        if(jsonArray.getJSONObject(j).getString("diabetes_cond").equalsIgnoreCase("1")) {
                                            rb_diabetesYes.setChecked(true);
                                        }
                                        else if(jsonArray.getJSONObject(j).getString("diabetes_cond").equalsIgnoreCase("2")) {
                                            rb_diabetesNo.setChecked(true);
                                        }
                                        else if(jsonArray.getJSONObject(j).getString("diabetes_cond").equalsIgnoreCase("0")) {
                                            rb_diabetesYes.setChecked(false);
                                            rb_diabetesNo.setChecked(false);
                                        }
                                        else {
                                            rb_diabetesYes.setChecked(false);
                                            rb_diabetesNo.setChecked(false);
                                        }

                                        //Cholesterol
                                        if(jsonArray.getJSONObject(j).getString("CHOLESTEROL").equalsIgnoreCase("1")) {
                                            rb_cholesterolYes.setChecked(true);
                                        }
                                        else if(jsonArray.getJSONObject(j).getString("CHOLESTEROL").equalsIgnoreCase("2")) {
                                            rb_cholesterolNo.setChecked(true);
                                        }
                                        else if(jsonArray.getJSONObject(j).getString("CHOLESTEROL").equalsIgnoreCase("0")) {
                                            rb_cholesterolYes.setChecked(false);
                                            rb_cholesterolNo.setChecked(false);
                                        }
                                        else {
                                            rb_cholesterolYes.setChecked(false);
                                            rb_cholesterolNo.setChecked(false);
                                        }

                                        //Arthritis
                                        if(jsonArray.getJSONObject(j).getString("ARTHRITIS_cond").equalsIgnoreCase("1")) {
                                            rb_arthritisYes.setChecked(true);
                                        }
                                        else if(jsonArray.getJSONObject(j).getString("ARTHRITIS_cond").equalsIgnoreCase("2")) {
                                            rb_arthritisNo.setChecked(true);
                                        }
                                        else if(jsonArray.getJSONObject(j).getString("ARTHRITIS_cond").equalsIgnoreCase("0")) {
                                            rb_arthritisYes.setChecked(false);
                                            rb_arthritisNo.setChecked(false);
                                        }
                                        else {
                                            rb_arthritisYes.setChecked(false);
                                            rb_arthritisNo.setChecked(false);
                                        }

                                        //IHD
                                        if(jsonArray.getJSONObject(j).getString("IHD_cond").equalsIgnoreCase("1")) {
                                            rb_ihdYes.setChecked(true);
                                        }
                                        else if(jsonArray.getJSONObject(j).getString("IHD_cond").equalsIgnoreCase("2")) {
                                            rb_ihdNo.setChecked(true);
                                        }
                                        else if(jsonArray.getJSONObject(j).getString("IHD_cond").equalsIgnoreCase("0")) {
                                            rb_ihdYes.setChecked(false);
                                            rb_ihdNo.setChecked(false);
                                        }
                                        else {
                                            rb_ihdYes.setChecked(false);
                                            rb_ihdNo.setChecked(false);
                                        }

                                        //Asthama
                                        if(jsonArray.getJSONObject(j).getString("ASTHAMA").equalsIgnoreCase("1")) {
                                            rb_asthamaYes.setChecked(true);
                                        }
                                        else if(jsonArray.getJSONObject(j).getString("ASTHAMA").equalsIgnoreCase("2")) {
                                            rb_asthamaNo.setChecked(true);
                                        }
                                        else if(jsonArray.getJSONObject(j).getString("ASTHAMA").equalsIgnoreCase("0")) {
                                            rb_asthamaYes.setChecked(false);
                                            rb_asthamaNo.setChecked(false);
                                        }
                                        else {
                                            rb_asthamaYes.setChecked(false);
                                            rb_asthamaNo.setChecked(false);
                                        }

                                        //THY
                                        if(jsonArray.getJSONObject(j).getString("THY_cond").equalsIgnoreCase("1")) {
                                            rb_thyYes.setChecked(true);
                                        }
                                        else if(jsonArray.getJSONObject(j).getString("THY_cond").equalsIgnoreCase("2")) {
                                            rb_thyNo.setChecked(true);
                                        }
                                        else if(jsonArray.getJSONObject(j).getString("THY_cond").equalsIgnoreCase("0")) {
                                            rb_thyYes.setChecked(false);
                                            rb_thyNo.setChecked(false);
                                        }
                                        else {
                                            rb_thyYes.setChecked(false);
                                            rb_thyNo.setChecked(false);
                                        }

                                        if(jsonArray.getJSONObject(j).getString("smoking").equals("")) {
                                            PATIENT_SMOKING = "--Select--";
                                        }
                                        smoking_spinner.setSelection(getIndex(smoking_spinner, jsonArray.getJSONObject(j).getString("smoking")));

                                        if(jsonArray.getJSONObject(j).getString("alcoholic").equals("")) {
                                            PATIENT_ALCOHOL = "--Select--";
                                        }
                                        alcohol_spinner.setSelection(getIndex(alcohol_spinner, jsonArray.getJSONObject(j).getString("alcoholic")));
                                    }

                                    jsonArrayAllergy = jsonObject.getJSONArray("drug_allery_details");
                                    if(jsonArrayAllergy.length() > 0) {
                                        for (int i = 0; i < jsonArrayAllergy.length(); i++) {
                                            PATIENT_DRUG_ALLERTY_ARRAY.add(new DrugAllery(jsonArrayAllergy.getJSONObject(i).getInt("allergy_id"),jsonArrayAllergy.getJSONObject(i).getInt("generic_id"),jsonArrayAllergy.getJSONObject(i).getString("generic_name"),jsonArrayAllergy.getJSONObject(i).getInt("patient_id"),jsonArrayAllergy.getJSONObject(i).getInt("doc_id"),jsonArrayAllergy.getJSONObject(i).getInt("doc_type"),USER_ID,USER_LOGIN_TYPE));
                                        }
                                        refreshDrugAllergy();
//                                        drugAllergyAdapter = new DrugAllergyAdapters(getActivity(), PATIENT_DRUG_ALLERTY_ARRAY);
//                                        drugAllergyRecyclerview.setAdapter(drugAllergyAdapter);
//                                        drugAllergyAdapter.notifyDataSetChanged();
                                    }

                                    jsonArrayDrubAbuse = jsonObject.getJSONArray("drug_abuse_details");
                                    if(jsonArrayDrubAbuse.length() > 0) {
                                        for (int l = 0; l < jsonArrayDrubAbuse.length(); l++) {
                                            PATIENT_DRUG_ABUSE_ARRAY.add(new DrugAbuse(0,jsonArrayDrubAbuse.getJSONObject(l).getInt("drug_abuse_id"),jsonArrayDrubAbuse.getJSONObject(l).getString("drug_abuse"),
                                                    jsonArrayDrubAbuse.getJSONObject(l).getInt("doc_id"),jsonArrayDrubAbuse.getJSONObject(l).getInt("doc_type"),0,USER_ID, USER_LOGIN_TYPE));
                                        }
                                        refreshDrugAbuse();
                                    }

                                    jsonArrayFamilyHistory = jsonObject.getJSONArray("family_history_details");
                                    if(jsonArrayFamilyHistory.length() > 0) {
                                        for (int m = 0; m < jsonArrayFamilyHistory.length(); m++) {
                                            PATIENT_FAMILY_HISTORY_ARRAY.add(new FamilyHistory(0,jsonArrayFamilyHistory.getJSONObject(m).getInt("family_history_id"),jsonArrayFamilyHistory.getJSONObject(m).getString("family_history"),
                                                    jsonArrayFamilyHistory.getJSONObject(m).getInt("doc_id"),jsonArrayFamilyHistory.getJSONObject(m).getInt("doc_type"),0,USER_ID, USER_LOGIN_TYPE));

                                        }
                                        refreshFamilyHistory();
                                    }
                                }
                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                map.put(APIClass.KEY_PATIENTID, String.valueOf(PATIENT_ID));
                map.put(APIClass.KEY_PATIENT_NAME, PATIENT_NAME);
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(getActivity()).
                getRequestQueue();
        AppController.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}
