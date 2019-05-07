package com.medisensehealth.fdccontributor.activities.mypatientOphthalmology;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.arbelkilani.bicoloredprogress.BiColoredProgress;
import com.google.gson.Gson;
import com.medisensehealth.fdccontributor.DataModel.ChiefMedicalComplaint;
import com.medisensehealth.fdccontributor.DataModel.Diagnosis;
import com.medisensehealth.fdccontributor.DataModel.DiagnosisTestLists;
import com.medisensehealth.fdccontributor.DataModel.DrugAbuse;
import com.medisensehealth.fdccontributor.DataModel.DrugAllery;
import com.medisensehealth.fdccontributor.DataModel.EpisodesList;
import com.medisensehealth.fdccontributor.DataModel.FamilyHistory;
import com.medisensehealth.fdccontributor.DataModel.FrequentChiefComplaints;
import com.medisensehealth.fdccontributor.DataModel.FrequentInvestigations;
import com.medisensehealth.fdccontributor.DataModel.FrequentPrescription;
import com.medisensehealth.fdccontributor.DataModel.InvestigationGroupTests;
import com.medisensehealth.fdccontributor.DataModel.InvestigationTemplates;
import com.medisensehealth.fdccontributor.DataModel.Investigations;
import com.medisensehealth.fdccontributor.DataModel.Lids;
import com.medisensehealth.fdccontributor.DataModel.MyTemplates;
import com.medisensehealth.fdccontributor.DataModel.OphthalAngleAnteriorChamber;
import com.medisensehealth.fdccontributor.DataModel.OphthalAnteriorChamber;
import com.medisensehealth.fdccontributor.DataModel.OphthalConjuctiva;
import com.medisensehealth.fdccontributor.DataModel.OphthalCornearAnteriorSurface;
import com.medisensehealth.fdccontributor.DataModel.OphthalCornearPosteriorSurface;
import com.medisensehealth.fdccontributor.DataModel.OphthalFundus;
import com.medisensehealth.fdccontributor.DataModel.OphthalIris;
import com.medisensehealth.fdccontributor.DataModel.OphthalLens;
import com.medisensehealth.fdccontributor.DataModel.OphthalPupil;
import com.medisensehealth.fdccontributor.DataModel.OphthalSclera;
import com.medisensehealth.fdccontributor.DataModel.OphthalViterous;
import com.medisensehealth.fdccontributor.DataModel.PatientEducation;
import com.medisensehealth.fdccontributor.DataModel.PrescriptionList;
import com.medisensehealth.fdccontributor.DataModel.SelectedTestLists;
import com.medisensehealth.fdccontributor.DataModel.Treatments;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.database.MedisensePracticeDB;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
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
public class RefreshOphthalEMRActivity extends AppCompatActivity {

    int PATIENT_ID;
    String PATIENT_NAME, PATIENT_AGE, PATIENT_GENDER, PATIENT_MOBILE, PATIENT_EMAIL, PATIENT_CITY, PATIENT_ADDRESS, PATIENT_STATE, PATIENT_COUNTRY,
            PATIENT_HEIGHT, PATIENT_WEIGHT, PATIENT_HYPERTENSION, PATIENT_DIABETES, PATIENT_SMOKING, PATIENT_ALCOHOL, PATIENT_DRUG_ABUSE, PATIENT_OTHER_DETAILS,
            PATIENT_FAMILY_HISTORY, PATIENT_PREV_INTERVENTIONS, PATIENT_NEURO_ISSUES, PATIENT_KIDNEY_ISSUES;

    int USER_ID;
    String USER_NAME,USER_LOGIN_TYPE, GET_EPISODE_LIST_TEMP;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;

    List<EpisodesList> EPISODE_LIST = new ArrayList<>();
    List<SelectedTestLists> INVESTIGATION_LIST = new ArrayList<>();
    List<DiagnosisTestLists> IDC_LIST = new ArrayList<>();
    ArrayList<String> REPORT_PHOTOS = new ArrayList<String>();
    List<PrescriptionList> PRESCRIPTION_LIST = new ArrayList<>();
    List<EpisodesList> EPISODE_LIST_TEMP = new ArrayList<>();
    Gson gson = new Gson();

    List<ChiefMedicalComplaint> chiefcomplaintListArraylist;
    List<FrequentChiefComplaints> freqComplaintListArraylist;

    /* Investigation Lists*/
    List<Investigations> investigationsListArraylist;
    List<FrequentInvestigations> freqInvestigationsListArraylist;
    List<InvestigationGroupTests> grouptestListArraylist;
    List<InvestigationTemplates> investigationTemplateListArraylist;


    /* Diagnosis Lists*/
    List<Diagnosis> freqDiagnosisListArraylist;
    List<Diagnosis> diagnosisListArraylist;

    /* Treatments Lists*/
    List<Treatments> freqTreatmentListArraylist;
    List<Treatments> treatmentListArraylist;

    /* Prescription Lists*/
    List<MyTemplates> templateListArraylist = new ArrayList<>();
    List<FrequentPrescription> freqPrescriptionsListArraylist;
    List<FrequentPrescription> repeatPrescriptionsListArraylist;
    List<FrequentPrescription> prescriptionListArraylist;
    List<PatientEducation> patientEducationListArraylist;

    /* Drug Allergy Lists*/
    List<DrugAllery> drugAlleryListArraylist;

    /* Drug Abusr Lists */
    List<DrugAbuse> freqdrugAbuseListArraylist;
    List<DrugAbuse> drugAbuseListArraylist;

    /* Family History Lists*/
    List<FamilyHistory> freqfamilyHistoryListArraylist;
    List<FamilyHistory> familyHistoryListArraylist;

    private BiColoredProgress mBiColoredProgress, mBiColoredProgressLids, mBiColoredProgressConjuctiva, mBiColoredProgressSclera,
                        mBiColoredProgressCorneaAnterior, mBiColoredProgressCorneaPosterior,  mBiColoredProgressAnteriorChamber,
                        mBiColoredProgressIris, mBiColoredProgressPupil, mBiColoredProgressAngle, mBiColoredProgressLens,
                        mBiColoredProgressViterous,  mBiColoredProgressFundus,  mBiColoredProgressInvestigation,
                        mBiColoredProgressDiagnosis, mBiColoredProgressTreatment, mBiColoredProgressPrescription, mBiColoredProgressDrugAllergy, mBiColoredProgressDrugAbuse, mBiColoredProgressFamilyHistory;
    CustomTextViewBold medcomplaint_done, lids_done,  conjuctiva_done, sclera_done, cornea_anterior_done, cornea_posterior_done,
                        anterior_chamber_done, iris_done, pupil_done, angle_done, lens_done, viterous_done, fundus_done, investigation_done,
                        diagnosis_done, treatment_done, prescription_done, drugallergy_done, drugabuse_done, familyhistory_done;
    Button continue_btn;

    /* Examinations -  Lids List */
    List<Lids> lidsListArraylist;
    List<OphthalConjuctiva> conjuctivaListArraylist;          /* Examinations -  Conjuctiva List */
    List<OphthalSclera> scleraListArraylist;          /* Examinations -  Sclera List */
    List<OphthalCornearAnteriorSurface> corneaAnteriorListArraylist;          /* Examinations -  Cornea Anterior List */
    List<OphthalCornearPosteriorSurface> corneaPosteriorListArraylist;          /* Examinations -  Cornea Posterior List */
    List<OphthalAnteriorChamber> anteriorChamberListArraylist;          /* Examinations -  Cornea Posterior List */
    List<OphthalIris> irisListArraylist;          /* Examinations -  Iris List */
    List<OphthalPupil> pupilListArraylist;          /* Examinations -  Iris List */
    List<OphthalAngleAnteriorChamber> angleListArraylist;          /* Examinations -  Angle of Anterior Chamber List */
    List<OphthalLens> lensListArraylist;          /* Examinations -  Lens List */
    List<OphthalViterous> viterousListArraylist;          /* Examinations -  Viterous List */
    List<OphthalFundus> fundusListArraylist;          /* Examinations -  Fundus List */


    boolean CHIEF_MED_COMP_STATUS = false,  LIDS_STATUS = false, CONJUCTIVA_STATUS = false, SCALERA_STATUS = false, CORNEA_ANTERIOR_STATUS = false,
            CORNEA_POSTERIOR_STATUS = false, ANT_CHAMBER_STATUS = false, IRIS_STATUS = false, PUPIL_STATUS = false, ANGLE_ANT_STATUS = false,
            LENS_STATUS = false, VITEROUS_STATUS = false, FUNDUS_STATUS = false, INVESTIGATIONS_STATUS = false, DIAGNOSIS_STATUS = false,
            TREATMENT_STATUS = false, PRESCRIPTION_STATUS = false, DRUG_ALEERGY_STATUS = false,DRUG_ABUSE_STATUS = false, FAMILY_HISTORY_STATUS = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ophthal_emr_refresh);
        PATIENT_GENDER = "0";
        PATIENT_HYPERTENSION = "0";
        PATIENT_DIABETES = "0";
        PATIENT_SMOKING = "";
        PATIENT_ALCOHOL = "";
        EPISODE_LIST = new ArrayList<>();
        INVESTIGATION_LIST = new ArrayList<>();
        IDC_LIST = new ArrayList<>();
        PRESCRIPTION_LIST = new ArrayList<>();
        REPORT_PHOTOS = new ArrayList<String>();
        EPISODE_LIST_TEMP = new ArrayList<>();
        gson = new Gson();

        CHIEF_MED_COMP_STATUS = false;  LIDS_STATUS = false; CONJUCTIVA_STATUS = false; SCALERA_STATUS = false; CORNEA_ANTERIOR_STATUS = false;
        CORNEA_POSTERIOR_STATUS = false; ANT_CHAMBER_STATUS = false; IRIS_STATUS = false; PUPIL_STATUS = false; ANGLE_ANT_STATUS = false;
        LENS_STATUS = false; VITEROUS_STATUS = false; FUNDUS_STATUS = false; INVESTIGATIONS_STATUS = false; DIAGNOSIS_STATUS = false;
        TREATMENT_STATUS = false; PRESCRIPTION_STATUS = false; DRUG_ALEERGY_STATUS = false; DRUG_ABUSE_STATUS = false; FAMILY_HISTORY_STATUS = false;

        chiefcomplaintListArraylist = new ArrayList<>();
        freqComplaintListArraylist = new ArrayList<>();

        lidsListArraylist  = new ArrayList<>();
        conjuctivaListArraylist = new ArrayList<>();
        scleraListArraylist = new ArrayList<>();
        corneaAnteriorListArraylist = new ArrayList<>();
        corneaPosteriorListArraylist = new ArrayList<>();
        anteriorChamberListArraylist = new ArrayList<>();
        irisListArraylist = new ArrayList<>();
        pupilListArraylist = new ArrayList<>();
        angleListArraylist = new ArrayList<>();
        lensListArraylist = new ArrayList<>();
        viterousListArraylist = new ArrayList<>();
        fundusListArraylist = new ArrayList<>();

        investigationsListArraylist = new ArrayList<>();
        freqInvestigationsListArraylist  = new ArrayList<>();
        grouptestListArraylist = new ArrayList<>();
        investigationTemplateListArraylist = new ArrayList<>();

        freqDiagnosisListArraylist = new ArrayList<>();
        diagnosisListArraylist = new ArrayList<>();

        freqTreatmentListArraylist = new ArrayList<>();
        treatmentListArraylist  = new ArrayList<>();

        templateListArraylist = new ArrayList<>();
        freqPrescriptionsListArraylist = new ArrayList<>();
        repeatPrescriptionsListArraylist = new ArrayList<>();
        prescriptionListArraylist = new ArrayList<>();
        patientEducationListArraylist = new ArrayList<>();

        drugAlleryListArraylist = new ArrayList<>();

        freqdrugAbuseListArraylist = new ArrayList<>();
        drugAbuseListArraylist = new ArrayList<>();

        freqfamilyHistoryListArraylist = new ArrayList<>();
        familyHistoryListArraylist = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);
            Log.d(Utils.TAG, " ************ RefreshOphthalEMRActivity *********** ");

            PATIENT_ID = bundle.getInt("PATIENT_ID");
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

            Log.d(Utils.TAG, " PATIENT_ID: "+PATIENT_ID);
            Log.d(Utils.TAG, " PATIENT_NAME: "+PATIENT_NAME);
        }

        shareadPreferenceClass = new ShareadPreferenceClass(RefreshOphthalEMRActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(RefreshOphthalEMRActivity.this);

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            GET_EPISODE_LIST_TEMP = sharedPreferences.getString(HCConstants.PREF_MYPATIENT_EPISODE_LIST, "");
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            GET_EPISODE_LIST_TEMP  = sharedPreferences.getString(HCConstants.PREF_MYPATIENT_EPISODE_LIST, "");
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {

            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            GET_EPISODE_LIST_TEMP  = sharedPreferences.getString(HCConstants.PREF_MYPATIENT_EPISODE_LIST, "");
        }
        Log.d(Utils.TAG, " USER_ID: "+USER_ID);
        Log.d(Utils.TAG, " USER_NAME: "+USER_NAME);
        Log.d(Utils.TAG, " LOGIN_TYPE: "+USER_LOGIN_TYPE);

        initializationViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                break;
        }
        return true;
    }

    private void initializationViews() {

        mBiColoredProgress = findViewById(R.id.chief_complaint_progress);
        mBiColoredProgress.setProgress(100f);
        mBiColoredProgress.setAnimated(true, 6000, new BounceInterpolator());

        mBiColoredProgressLids = findViewById(R.id.lids_progress);
        mBiColoredProgressConjuctiva = findViewById(R.id.conjuctiva_progress);
        mBiColoredProgressSclera = findViewById(R.id.sclera_progress);
        mBiColoredProgressCorneaAnterior = findViewById(R.id.cornea_anterior_progress);
        mBiColoredProgressCorneaPosterior = findViewById(R.id.cornea_posterior_progress);
        mBiColoredProgressAnteriorChamber = findViewById(R.id.anterior_chamber_progress);
        mBiColoredProgressIris = findViewById(R.id.iris_progress);
        mBiColoredProgressPupil = findViewById(R.id.pupil_progress);
        mBiColoredProgressAngle = findViewById(R.id.angle_progress);
        mBiColoredProgressLens = findViewById(R.id.lens_progress);
        mBiColoredProgressViterous = findViewById(R.id.viterous_progress);
        mBiColoredProgressFundus = findViewById(R.id.fundus_progress);
        mBiColoredProgressInvestigation = findViewById(R.id.investigation_progress);

        mBiColoredProgressDiagnosis = findViewById(R.id.diagnosis_progress);
        mBiColoredProgressTreatment = findViewById(R.id.treatment_progress);
        mBiColoredProgressPrescription = findViewById(R.id.prescription_progress);
        mBiColoredProgressDrugAllergy = findViewById(R.id.allergy_progress);
        mBiColoredProgressDrugAbuse = findViewById(R.id.abuse_progress);
        mBiColoredProgressFamilyHistory = findViewById(R.id.history_progress);

        medcomplaint_done = (CustomTextViewBold) findViewById(R.id.medcomplaint_done);
        medcomplaint_done.setVisibility(View.INVISIBLE);
        lids_done = (CustomTextViewBold) findViewById(R.id.lids_done);
        lids_done.setVisibility(View.INVISIBLE);
        conjuctiva_done = (CustomTextViewBold) findViewById(R.id.conjuctiva_done);
        conjuctiva_done.setVisibility(View.INVISIBLE);
        sclera_done = (CustomTextViewBold) findViewById(R.id.sclera_done);
        sclera_done.setVisibility(View.INVISIBLE);
        cornea_anterior_done = (CustomTextViewBold) findViewById(R.id.cornea_anterior_done);
        cornea_anterior_done.setVisibility(View.INVISIBLE);
        cornea_posterior_done = (CustomTextViewBold) findViewById(R.id.cornea_posterior_done);
        cornea_posterior_done.setVisibility(View.INVISIBLE);
        anterior_chamber_done = (CustomTextViewBold) findViewById(R.id.anterior_chamber_done);
        anterior_chamber_done.setVisibility(View.INVISIBLE);
        iris_done = (CustomTextViewBold) findViewById(R.id.iris_done);
        iris_done.setVisibility(View.INVISIBLE);
        pupil_done = (CustomTextViewBold) findViewById(R.id.pupil_done);
        pupil_done.setVisibility(View.INVISIBLE);
        angle_done = (CustomTextViewBold) findViewById(R.id.angle_done);
        angle_done.setVisibility(View.INVISIBLE);
        lens_done = (CustomTextViewBold) findViewById(R.id.lens_done);
        lens_done.setVisibility(View.INVISIBLE);
        viterous_done  = (CustomTextViewBold) findViewById(R.id.viterous_done);
        viterous_done.setVisibility(View.INVISIBLE);
        fundus_done = (CustomTextViewBold) findViewById(R.id.fundus_done);
        fundus_done.setVisibility(View.INVISIBLE);
        investigation_done = (CustomTextViewBold) findViewById(R.id.investigation_done);
        investigation_done.setVisibility(View.INVISIBLE);

        diagnosis_done  = (CustomTextViewBold) findViewById(R.id.diagnosis_done);
        diagnosis_done.setVisibility(View.INVISIBLE);
        treatment_done = (CustomTextViewBold) findViewById(R.id.treatment_done);
        treatment_done.setVisibility(View.INVISIBLE);
        prescription_done = (CustomTextViewBold) findViewById(R.id.precription_done);
        prescription_done.setVisibility(View.INVISIBLE);
        drugallergy_done  = (CustomTextViewBold) findViewById(R.id.allergy_done);
        drugallergy_done.setVisibility(View.INVISIBLE);
        drugabuse_done  = (CustomTextViewBold) findViewById(R.id.abuse_done);
        drugabuse_done.setVisibility(View.INVISIBLE);
        familyhistory_done = (CustomTextViewBold) findViewById(R.id.history_done);
        familyhistory_done.setVisibility(View.INVISIBLE);
        continue_btn = (Button)  findViewById(R.id.continue_btn);
        continue_btn.setVisibility(View.GONE);

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent bundle = new Intent(RefreshOphthalEMRActivity.this, MyPatientHomeActivity.class);
                bundle.putExtra("title","Refresh EMR");
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
                startActivity(bundle);*/
                finish();
            }
        });

        new getJsonChiefMedicalComplaint().execute();
    }

    class getJsonChiefMedicalComplaint extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... key) {
            freqComplaintListArraylist = new ArrayList<>();
            chiefcomplaintListArraylist = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_CHIEF_MEDICAL_COMPLAINT_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            CHIEF_MED_COMP_STATUS = true;
                            refreshAllContent(10000, 500, response);
                            //   Log.d(Utils.TAG, response.toString());
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
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(Utils.TAG, "onPostExecute: ");
        }
    }

    private void refreshAllContent(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgress.setProgress(incrementSeconds);
              //  Log.d(Utils.TAG, " MyCountDownTimer "+incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyProcess(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgress.setProgress(100f);
                medcomplaint_done.setVisibility(View.VISIBLE);

                new getJsonLids(RefreshOphthalEMRActivity.this).execute();

            }
        }.start();
    }

    private void volleyProcess(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    freqComplaintListArraylist = new ArrayList<>();
                    chiefcomplaintListArraylist = new ArrayList<>();
                    jsonArray1 = jsonObject1.getJSONArray("chief_medcomp_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearMedicalComplaints(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            chiefcomplaintListArraylist.add(new ChiefMedicalComplaint(jsonArray1.getJSONObject(i).getInt("complaint_id"),
                                    jsonArray1.getJSONObject(i).getString("symptoms"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),USER_ID, USER_LOGIN_TYPE));

                            long medcomp_insert = MedisensePracticeDB.chiefMedicalComplaintInsert(RefreshOphthalEMRActivity.this,jsonArray1.getJSONObject(i).getInt("complaint_id"),
                                    jsonArray1.getJSONObject(i).getString("symptoms"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),USER_ID, USER_LOGIN_TYPE);

                        }

                        Log.d(Utils.TAG, "chiefcomplaintListArraylist: " + chiefcomplaintListArraylist.size());
                    }

                    jsonArray2 = jsonObject1.getJSONArray("frequent_medcomp_details");
                    if (jsonArray2.length() > 0) {
                        MedisensePracticeDB.clearFrequentComplaints(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int j = 0; j < jsonArray2.length(); j++) {
                            freqComplaintListArraylist.add(new FrequentChiefComplaints(jsonArray2.getJSONObject(j).getInt("dfs_id"),
                                    jsonArray2.getJSONObject(j).getInt("symptoms_id"),jsonArray2.getJSONObject(j).getInt("doc_id"),
                                    jsonArray2.getJSONObject(j).getInt("doc_type"),jsonArray2.getJSONObject(j).getInt("freq_count"),
                                    jsonArray2.getJSONObject(j).getString("symptoms"),USER_ID, USER_LOGIN_TYPE));

                            long mfreqComp_insert = MedisensePracticeDB.frequentComplaintInsert(RefreshOphthalEMRActivity.this,jsonArray2.getJSONObject(j).getInt("dfs_id"),
                                    jsonArray2.getJSONObject(j).getInt("symptoms_id"),jsonArray2.getJSONObject(j).getInt("doc_id"),
                                    jsonArray2.getJSONObject(j).getInt("doc_type"),jsonArray2.getJSONObject(j).getInt("freq_count"),
                                    jsonArray2.getJSONObject(j).getString("symptoms"), USER_ID, USER_LOGIN_TYPE);

                        }
                        Log.d(Utils.TAG, "freqComplaintListArraylist: " + freqComplaintListArraylist.size());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class getJsonLids  extends AsyncTask<String,String,String>  {

        public getJsonLids(FragmentActivity activity) {
            mBiColoredProgressLids.setAnimated(true, 6000, new BounceInterpolator());
        }

        @Override
        protected String doInBackground(String... key) {
            lidsListArraylist  = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_LIDS_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                           //  Log.d(Utils.TAG, response.toString());
                            LIDS_STATUS = true;
                            refreshAllLids(10000, 500, response);
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
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(Utils.TAG, "onPostExecute: "+s);

        }
    }

    private void refreshAllLids(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgressLids.setProgress(incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyLidsProcess(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgressLids.setProgress(100f);
                lids_done.setVisibility(View.VISIBLE);

                new getJsonConjuctiva(RefreshOphthalEMRActivity.this).execute();
            }
        }.start();
    }

    private void volleyLidsProcess(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null; JSONArray jsonArray3 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    lidsListArraylist  = new ArrayList<>();

                    jsonArray1 = jsonObject1.getJSONArray("lids_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearLids(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            lidsListArraylist.add(new Lids(jsonArray1.getJSONObject(i).getInt("lids_id"),
                                    jsonArray1.getJSONObject(i).getString("lids_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                            long lids_insert = MedisensePracticeDB.lidsInsert(RefreshOphthalEMRActivity.this,
                                    jsonArray1.getJSONObject(i).getInt("lids_id"),
                                    jsonArray1.getJSONObject(i).getString("lids_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),
                                    USER_ID, USER_LOGIN_TYPE);

                        }

                        Log.d(Utils.TAG, "lidsListArraylist: " + lidsListArraylist.size());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getJsonConjuctiva extends AsyncTask<String,String,String> {

        public getJsonConjuctiva(FragmentActivity activity) {
            mBiColoredProgressConjuctiva.setAnimated(true, 6000, new BounceInterpolator());
        }

        @Override
        protected String doInBackground(String... key) {
            conjuctivaListArraylist = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_CONJUCTIVA_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Log.d(Utils.TAG, response.toString());
                            CONJUCTIVA_STATUS = true;
                            refreshAllConjuctivaContent(10000, 500, response);
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
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void refreshAllConjuctivaContent(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgressConjuctiva.setProgress(incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyProcessConjuctiva(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgressConjuctiva.setProgress(100f);
                conjuctiva_done.setVisibility(View.VISIBLE);

                new getJsonSclera(RefreshOphthalEMRActivity.this).execute();
            }
        }.start();
    }

    private void volleyProcessConjuctiva(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null; JSONArray jsonArray3 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    conjuctivaListArraylist  = new ArrayList<>();

                    jsonArray1 = jsonObject1.getJSONArray("conjuctiva_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearConjuctiva(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            conjuctivaListArraylist.add(new OphthalConjuctiva(jsonArray1.getJSONObject(i).getInt("conjuctiva_id"),
                                    jsonArray1.getJSONObject(i).getString("conjuctiva_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                            long conjuctiva_insert = MedisensePracticeDB.conjuctivaInsert(RefreshOphthalEMRActivity.this,
                                    jsonArray1.getJSONObject(i).getInt("conjuctiva_id"),
                                    jsonArray1.getJSONObject(i).getString("conjuctiva_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                        }

                        Log.d(Utils.TAG, "conjuctivaListArraylist: " + conjuctivaListArraylist.size());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getJsonSclera extends AsyncTask<String,String,String> {

        public getJsonSclera(FragmentActivity activity) {
            mBiColoredProgressSclera.setAnimated(true, 6000, new BounceInterpolator());
        }

        @Override
        protected String doInBackground(String... key) {
            scleraListArraylist = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_SCLERA_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Log.d(Utils.TAG, response.toString());
                            SCALERA_STATUS = true;
                            refreshAllScleraContent(10000, 500, response);
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
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void refreshAllScleraContent(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgressSclera.setProgress(incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyProcessSclera(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgressSclera.setProgress(100f);
                sclera_done.setVisibility(View.VISIBLE);

                new getJsonCorneaAnterior(RefreshOphthalEMRActivity.this).execute();
            }
        }.start();
    }

    private void volleyProcessSclera(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null; JSONArray jsonArray3 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    scleraListArraylist  = new ArrayList<>();

                    jsonArray1 = jsonObject1.getJSONArray("sclera_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearSclera(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            scleraListArraylist.add(new OphthalSclera(jsonArray1.getJSONObject(i).getInt("sclera_id"),
                                    jsonArray1.getJSONObject(i).getString("scelra_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                            long sclera_insert = MedisensePracticeDB.scleraInsert(RefreshOphthalEMRActivity.this,
                                    jsonArray1.getJSONObject(i).getInt("sclera_id"),
                                    jsonArray1.getJSONObject(i).getString("scelra_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                        }

                        Log.d(Utils.TAG, "scleraListArraylist: " + scleraListArraylist.size());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getJsonCorneaAnterior extends AsyncTask<String,String,String> {

        public getJsonCorneaAnterior(FragmentActivity activity) {
            mBiColoredProgressCorneaAnterior.setAnimated(true, 6000, new BounceInterpolator());
        }

        @Override
        protected String doInBackground(String... key) {
            corneaAnteriorListArraylist = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_CORNEA_ANTERIOR_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Log.d(Utils.TAG, response.toString());
                            CORNEA_ANTERIOR_STATUS = true;
                            refreshAllCorneaAnteriorContent(10000, 500, response);
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
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void refreshAllCorneaAnteriorContent(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgressCorneaAnterior.setProgress(incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyProcessCorneaAnterior(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgressCorneaAnterior.setProgress(100f);
                cornea_anterior_done.setVisibility(View.VISIBLE);

                new getJsonCorneaPosterior(RefreshOphthalEMRActivity.this).execute();
            }
        }.start();
    }

    private void volleyProcessCorneaAnterior(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null; JSONArray jsonArray3 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    corneaAnteriorListArraylist  = new ArrayList<>();

                    jsonArray1 = jsonObject1.getJSONArray("cornea_anterior_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearCorneaAnterior(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            corneaAnteriorListArraylist.add(new OphthalCornearAnteriorSurface(jsonArray1.getJSONObject(i).getInt("cornea_ant_id"),
                                    jsonArray1.getJSONObject(i).getString("cornea_ant_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                            long cornea_ant_insert = MedisensePracticeDB.corneaAnteriorInsert(RefreshOphthalEMRActivity.this,
                                    jsonArray1.getJSONObject(i).getInt("cornea_ant_id"),
                                    jsonArray1.getJSONObject(i).getString("cornea_ant_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                        }

                        Log.d(Utils.TAG, "corneaAnteriorListArraylist: " + corneaAnteriorListArraylist.size());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getJsonCorneaPosterior extends AsyncTask<String,String,String> {

        public getJsonCorneaPosterior(FragmentActivity activity) {
            mBiColoredProgressCorneaPosterior.setAnimated(true, 6000, new BounceInterpolator());
        }

        @Override
        protected String doInBackground(String... key) {
            corneaPosteriorListArraylist = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_CORNEA_POSTERIOR_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Log.d(Utils.TAG, response.toString());
                            CORNEA_POSTERIOR_STATUS = true;
                            refreshAllCorneaPosteriorContent(10000, 500, response);
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
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void refreshAllCorneaPosteriorContent(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgressCorneaPosterior.setProgress(incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyProcessCorneaPosterior(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgressCorneaPosterior.setProgress(100f);
                cornea_posterior_done.setVisibility(View.VISIBLE);

                new getJsonAnteriorChamber(RefreshOphthalEMRActivity.this).execute();
            }
        }.start();
    }

    private void volleyProcessCorneaPosterior(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null; JSONArray jsonArray3 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    corneaPosteriorListArraylist  = new ArrayList<>();

                    jsonArray1 = jsonObject1.getJSONArray("cornea_posterior_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearCorneaPosterior(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            corneaPosteriorListArraylist.add(new OphthalCornearPosteriorSurface(jsonArray1.getJSONObject(i).getInt("cornea_post_id"),
                                    jsonArray1.getJSONObject(i).getString("cornea_post_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                            long corneaPost_insert = MedisensePracticeDB.corneaPosteriorInsert(RefreshOphthalEMRActivity.this,
                                    jsonArray1.getJSONObject(i).getInt("cornea_post_id"),
                                    jsonArray1.getJSONObject(i).getString("cornea_post_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                        }

                        Log.d(Utils.TAG, "corneaPosteriorListArraylist: " + corneaPosteriorListArraylist.size());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getJsonAnteriorChamber extends AsyncTask<String,String,String> {

        public getJsonAnteriorChamber(FragmentActivity activity) {
            mBiColoredProgressAnteriorChamber.setAnimated(true, 6000, new BounceInterpolator());
        }

        @Override
        protected String doInBackground(String... key) {
            scleraListArraylist = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_ANTERIOR_CHAMBER_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Log.d(Utils.TAG, response.toString());
                            ANT_CHAMBER_STATUS = true;
                            refreshAllAnteriorChamberContent(10000, 500, response);
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
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void refreshAllAnteriorChamberContent(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgressAnteriorChamber.setProgress(incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyProcessAnteriorChamber(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgressAnteriorChamber.setProgress(100f);
                anterior_chamber_done.setVisibility(View.VISIBLE);

                new getJsonIris(RefreshOphthalEMRActivity.this).execute();
            }
        }.start();
    }

    private void volleyProcessAnteriorChamber(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null; JSONArray jsonArray3 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    anteriorChamberListArraylist  = new ArrayList<>();

                    jsonArray1 = jsonObject1.getJSONArray("anterior_chamber_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearAnteriorChamber(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            anteriorChamberListArraylist.add(new OphthalAnteriorChamber(jsonArray1.getJSONObject(i).getInt("chamber_id"),
                                    jsonArray1.getJSONObject(i).getString("chamber_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                            long chamber_insert = MedisensePracticeDB.anteriorChamberInsert(RefreshOphthalEMRActivity.this,
                                    jsonArray1.getJSONObject(i).getInt("chamber_id"),
                                    jsonArray1.getJSONObject(i).getString("chamber_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                        }

                        Log.d(Utils.TAG, "anteriorChamberListArraylist: " + anteriorChamberListArraylist.size());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getJsonIris extends AsyncTask<String,String,String> {

        public getJsonIris(FragmentActivity activity) {
            mBiColoredProgressIris.setAnimated(true, 6000, new BounceInterpolator());
        }

        @Override
        protected String doInBackground(String... key) {
            irisListArraylist = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_IRIS_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Log.d(Utils.TAG, response.toString());
                            IRIS_STATUS = true;
                            refreshAllIrisContent(10000, 500, response);
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
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void refreshAllIrisContent(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgressIris.setProgress(incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyProcessIris(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgressIris.setProgress(100f);
                iris_done.setVisibility(View.VISIBLE);

                new getJsonPupil(RefreshOphthalEMRActivity.this).execute();
            }
        }.start();
    }

    private void volleyProcessIris(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null; JSONArray jsonArray3 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    irisListArraylist  = new ArrayList<>();

                    jsonArray1 = jsonObject1.getJSONArray("iris_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearIris(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            irisListArraylist.add(new OphthalIris(jsonArray1.getJSONObject(i).getInt("iris_id"),
                                    jsonArray1.getJSONObject(i).getString("iris_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                            long iris_insert = MedisensePracticeDB.irisInsert(RefreshOphthalEMRActivity.this,
                                    jsonArray1.getJSONObject(i).getInt("iris_id"),
                                    jsonArray1.getJSONObject(i).getString("iris_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                        }

                        Log.d(Utils.TAG, "irisListArraylist: " + irisListArraylist.size());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getJsonPupil extends AsyncTask<String,String,String> {

        public getJsonPupil(FragmentActivity activity) {
            mBiColoredProgressPupil.setAnimated(true, 6000, new BounceInterpolator());
        }

        @Override
        protected String doInBackground(String... key) {
            pupilListArraylist = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_PUPIL_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Log.d(Utils.TAG, response.toString());
                            PUPIL_STATUS = true;
                            refreshAllPupilContent(10000, 500, response);
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
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void refreshAllPupilContent(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgressPupil.setProgress(incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyProcessPupil(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgressPupil.setProgress(100f);
                pupil_done.setVisibility(View.VISIBLE);

                new getJsonAngle(RefreshOphthalEMRActivity.this).execute();
            }
        }.start();
    }

    private void volleyProcessPupil(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null; JSONArray jsonArray3 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    pupilListArraylist  = new ArrayList<>();

                    jsonArray1 = jsonObject1.getJSONArray("pupil_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearPupil(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            pupilListArraylist.add(new OphthalPupil(jsonArray1.getJSONObject(i).getInt("pupil_id"),
                                    jsonArray1.getJSONObject(i).getString("pupil_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                            long pupil_insert = MedisensePracticeDB.pupilInsert(RefreshOphthalEMRActivity.this,
                                    jsonArray1.getJSONObject(i).getInt("pupil_id"),
                                    jsonArray1.getJSONObject(i).getString("pupil_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                        }

                        Log.d(Utils.TAG, "pupilListArraylist: " + pupilListArraylist.size());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getJsonAngle extends AsyncTask<String,String,String> {

        public getJsonAngle(FragmentActivity activity) {
            mBiColoredProgressAngle.setAnimated(true, 6000, new BounceInterpolator());
        }

        @Override
        protected String doInBackground(String... key) {
            angleListArraylist = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_ANGLE_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Log.d(Utils.TAG, response.toString());
                            ANGLE_ANT_STATUS = true;
                            refreshAllAngleContent(10000, 500, response);
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
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void refreshAllAngleContent(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgressAngle.setProgress(incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyProcessAngle(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgressAngle.setProgress(100f);
                angle_done.setVisibility(View.VISIBLE);

                new getJsonLens(RefreshOphthalEMRActivity.this).execute();
            }
        }.start();
    }

    private void volleyProcessAngle(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null; JSONArray jsonArray3 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    angleListArraylist  = new ArrayList<>();

                    jsonArray1 = jsonObject1.getJSONArray("angle_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearAngle(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            angleListArraylist.add(new OphthalAngleAnteriorChamber(jsonArray1.getJSONObject(i).getInt("angle_id"),
                                    jsonArray1.getJSONObject(i).getString("angle_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                            long angle_insert = MedisensePracticeDB.angleInsert(RefreshOphthalEMRActivity.this,
                                    jsonArray1.getJSONObject(i).getInt("angle_id"),
                                    jsonArray1.getJSONObject(i).getString("angle_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                        }

                        Log.d(Utils.TAG, "angleListArraylist: " + angleListArraylist.size());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getJsonLens extends AsyncTask<String,String,String> {

        public getJsonLens(FragmentActivity activity) {
            mBiColoredProgressLens.setAnimated(true, 6000, new BounceInterpolator());
        }

        @Override
        protected String doInBackground(String... key) {
            lensListArraylist = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_LENS_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Log.d(Utils.TAG, response.toString());
                            LENS_STATUS = true;
                            refreshAllLensContent(10000, 500, response);
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
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void refreshAllLensContent(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgressLens.setProgress(incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyProcessLens(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgressLens.setProgress(100f);
                lens_done.setVisibility(View.VISIBLE);

                new getJsonViterous(RefreshOphthalEMRActivity.this).execute();
            }
        }.start();
    }

    private void volleyProcessLens(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null; JSONArray jsonArray3 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    lensListArraylist  = new ArrayList<>();

                    jsonArray1 = jsonObject1.getJSONArray("lens_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearLens(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            lensListArraylist.add(new OphthalLens(jsonArray1.getJSONObject(i).getInt("lens_id"),
                                    jsonArray1.getJSONObject(i).getString("lens_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                            long lens_insert = MedisensePracticeDB.lensInsert(RefreshOphthalEMRActivity.this,
                                    jsonArray1.getJSONObject(i).getInt("lens_id"),
                                    jsonArray1.getJSONObject(i).getString("lens_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                        }

                        Log.d(Utils.TAG, "lensListArraylist: " + lensListArraylist.size());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getJsonViterous extends AsyncTask<String,String,String> {

        public getJsonViterous(FragmentActivity activity) {
            mBiColoredProgressViterous.setAnimated(true, 6000, new BounceInterpolator());
        }

        @Override
        protected String doInBackground(String... key) {
            viterousListArraylist = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_VITEROUS_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Log.d(Utils.TAG, response.toString());
                            VITEROUS_STATUS = true;
                            refreshAllViterousContent(10000, 500, response);
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
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void refreshAllViterousContent(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgressViterous.setProgress(incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyProcessViterous(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgressViterous.setProgress(100f);
                viterous_done.setVisibility(View.VISIBLE);

                 new getJsonFundus(RefreshOphthalEMRActivity.this).execute();
            }
        }.start();
    }

    private void volleyProcessViterous(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null; JSONArray jsonArray3 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    viterousListArraylist  = new ArrayList<>();

                    jsonArray1 = jsonObject1.getJSONArray("viterous_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearViterous(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            viterousListArraylist.add(new OphthalViterous(jsonArray1.getJSONObject(i).getInt("viterous_id"),
                                    jsonArray1.getJSONObject(i).getString("viterous_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                            long viterous_insert = MedisensePracticeDB.viterousnsert(RefreshOphthalEMRActivity.this,
                                    jsonArray1.getJSONObject(i).getInt("viterous_id"),
                                    jsonArray1.getJSONObject(i).getString("viterous_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                        }

                        Log.d(Utils.TAG, "viterousListArraylist: " + viterousListArraylist.size());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getJsonFundus extends AsyncTask<String,String,String> {

        public getJsonFundus(FragmentActivity activity) {
            mBiColoredProgressFundus.setAnimated(true, 6000, new BounceInterpolator());
        }

        @Override
        protected String doInBackground(String... key) {
            fundusListArraylist = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_FUNDUS_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Log.d(Utils.TAG, response.toString());
                            FUNDUS_STATUS = true;
                            refreshAllFundusContent(10000, 500, response);
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
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void refreshAllFundusContent(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgressFundus.setProgress(incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyProcessFundus(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgressFundus.setProgress(100f);
                fundus_done.setVisibility(View.VISIBLE);

                new getJsonInvestigations(RefreshOphthalEMRActivity.this).execute();
            }
        }.start();
    }

    private void volleyProcessFundus(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null; JSONArray jsonArray3 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    fundusListArraylist  = new ArrayList<>();

                    jsonArray1 = jsonObject1.getJSONArray("fundus_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearFundus(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            fundusListArraylist.add(new OphthalFundus(jsonArray1.getJSONObject(i).getInt("fundus_id"),
                                    jsonArray1.getJSONObject(i).getString("fundus_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                            long fundus_insert = MedisensePracticeDB.fundusInsert(RefreshOphthalEMRActivity.this,
                                    jsonArray1.getJSONObject(i).getInt("fundus_id"),
                                    jsonArray1.getJSONObject(i).getString("fundus_name"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getString("left_eye"),
                                    jsonArray1.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                        }

                        Log.d(Utils.TAG, "fundusListArraylist: " + fundusListArraylist.size());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class getJsonInvestigations  extends AsyncTask<String,String,String>  {

        public getJsonInvestigations(FragmentActivity activity) {
            mBiColoredProgressInvestigation.setAnimated(true, 6000, new BounceInterpolator());
        }

        @Override
        protected String doInBackground(String... key) {
            investigationsListArraylist = new ArrayList<>();
            freqInvestigationsListArraylist  = new ArrayList<>();
            grouptestListArraylist = new ArrayList<>();
            investigationTemplateListArraylist = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_INVESTIGATIONS_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Log.d(Utils.TAG, response.toString());
                            INVESTIGATIONS_STATUS = true;
                            refreshAllInvestigationContent(10000, 500, response);
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
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(Utils.TAG, "onPostExecute: "+s);

        }
    }

    private void refreshAllInvestigationContent(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgressInvestigation.setProgress(incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyInvestigationProcess(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgressInvestigation.setProgress(100f);
                investigation_done.setVisibility(View.VISIBLE);

                new getJsonDiagnosisFrequent(RefreshOphthalEMRActivity.this).execute();
            }
        }.start();
    }

    private void volleyInvestigationProcess(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null; JSONArray jsonArray3 = null; JSONArray jsonArray4 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    investigationsListArraylist = new ArrayList<>();
                    freqInvestigationsListArraylist  = new ArrayList<>();
                    grouptestListArraylist = new ArrayList<>();
                    investigationTemplateListArraylist = new ArrayList<>();
                    jsonArray1 = jsonObject1.getJSONArray("investigation_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearInvestigationTests(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            investigationsListArraylist.add(new Investigations(jsonArray1.getJSONObject(i).getInt("id"),
                                    jsonArray1.getJSONObject(i).getString("test_id"),jsonArray1.getJSONObject(i).getString("test_name_site_name").trim(),jsonArray1.getJSONObject(i).getInt("department"),
                                    jsonArray1.getJSONObject(i).getString("group_test"), jsonArray1.getJSONObject(i).getString("is_mref_range"),
                                    jsonArray1.getJSONObject(i).getString("is_reportable"),jsonArray1.getJSONObject(i).getString("normal_range"),
                                    jsonArray1.getJSONObject(i).getString("test_charges"), jsonArray1.getJSONObject(i).getString("min_range"),
                                    jsonArray1.getJSONObject(i).getString("max_range"),jsonArray1.getJSONObject(i).getString("test_units"),
                                    jsonArray1.getJSONObject(i).getString("crit_low_range"), jsonArray1.getJSONObject(i).getString("crit_high_range"),
                                    jsonArray1.getJSONObject(i).getString("normal_min_range"), jsonArray1.getJSONObject(i).getString("normal_max_range"),
                                    jsonArray1.getJSONObject(i).getString("dept_name"), jsonArray1.getJSONObject(i).getString("type"),
                                    USER_ID, USER_LOGIN_TYPE));

                            long inest_insert = MedisensePracticeDB.investigationTestsInsert(RefreshOphthalEMRActivity.this,jsonArray1.getJSONObject(i).getInt("id"),
                                    jsonArray1.getJSONObject(i).getString("test_id"),jsonArray1.getJSONObject(i).getString("test_name_site_name").trim(),
                                    jsonArray1.getJSONObject(i).getInt("department"),jsonArray1.getJSONObject(i).getString("group_test"),
                                    jsonArray1.getJSONObject(i).getString("is_mref_range"),
                                    jsonArray1.getJSONObject(i).getString("is_reportable"),jsonArray1.getJSONObject(i).getString("normal_range"),
                                    jsonArray1.getJSONObject(i).getString("test_charges"), jsonArray1.getJSONObject(i).getString("min_range"),
                                    jsonArray1.getJSONObject(i).getString("max_range"),jsonArray1.getJSONObject(i).getString("test_units"),
                                    jsonArray1.getJSONObject(i).getString("crit_low_range"), jsonArray1.getJSONObject(i).getString("crit_high_range"),
                                    jsonArray1.getJSONObject(i).getString("normal_min_range"), jsonArray1.getJSONObject(i).getString("normal_max_range"),
                                    jsonArray1.getJSONObject(i).getString("dept_name"), jsonArray1.getJSONObject(i).getString("type"),
                                    USER_ID, USER_LOGIN_TYPE);

                        }

                        Log.d(Utils.TAG, "investigationsListArraylist: " + investigationsListArraylist.size());
                    }

                    jsonArray2 = jsonObject1.getJSONArray("frequent_investigation_details");
                    if (jsonArray2.length() > 0) {
                        MedisensePracticeDB.clearFrequentInvestigations(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int j = 0; j < jsonArray2.length(); j++) {
                            freqInvestigationsListArraylist.add(new FrequentInvestigations(jsonArray2.getJSONObject(j).getInt("dfi_id"),
                                    jsonArray2.getJSONObject(j).getInt("main_test_id"),jsonArray2.getJSONObject(j).getInt("doc_id"),
                                    jsonArray2.getJSONObject(j).getInt("doc_type"),jsonArray2.getJSONObject(j).getInt("freq_test_count"),
                                    jsonArray2.getJSONObject(j).getString("test_name_site_name").trim(),jsonArray2.getJSONObject(j).getInt("department"),
                                    USER_ID, USER_LOGIN_TYPE));

                            long mfreqInvest_insert = MedisensePracticeDB.frequentInvestigationsInsert(RefreshOphthalEMRActivity.this,jsonArray2.getJSONObject(j).getInt("dfi_id"),
                                    jsonArray2.getJSONObject(j).getInt("main_test_id"),jsonArray2.getJSONObject(j).getInt("doc_id"),
                                    jsonArray2.getJSONObject(j).getInt("doc_type"),jsonArray2.getJSONObject(j).getInt("freq_test_count"),
                                    jsonArray2.getJSONObject(j).getString("test_name_site_name").trim(),jsonArray2.getJSONObject(j).getInt("department"),
                                    USER_ID, USER_LOGIN_TYPE);

                        }
                        Log.d(Utils.TAG, "freqInvestigationsListArraylist: " + freqInvestigationsListArraylist.size());

                    }

                    jsonArray3 = jsonObject1.getJSONArray("invest_grouptest_details");
                    if (jsonArray3.length() > 0) {
                        MedisensePracticeDB.clearInvestigationGroupTest(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int k = 0; k < jsonArray3.length(); k++) {
                            grouptestListArraylist.add(new InvestigationGroupTests(jsonArray3.getJSONObject(k).getInt("group_test_id"),
                                    jsonArray3.getJSONObject(k).getString("test_id"),jsonArray3.getJSONObject(k).getString("sub_test_id"),
                                    jsonArray3.getJSONObject(k).getString("order_no"),USER_ID, USER_LOGIN_TYPE));

                            long grouptest_insert = MedisensePracticeDB.investigationGroupTestInsert(RefreshOphthalEMRActivity.this,jsonArray3.getJSONObject(k).getInt("group_test_id"),
                                    jsonArray3.getJSONObject(k).getString("test_id"),jsonArray3.getJSONObject(k).getString("sub_test_id"),
                                    jsonArray3.getJSONObject(k).getString("order_no"),USER_ID, USER_LOGIN_TYPE);

                        }
                        Log.d(Utils.TAG, "grouptestListArraylist: " + grouptestListArraylist.size());
                    }

                    jsonArray4 = jsonObject1.getJSONArray("invest_template_details");
                    if(jsonArray4.length() > 0) {
                        MedisensePracticeDB.clearInvestigationTemplates(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int l = 0; l < jsonArray4.length(); l++) {
                            investigationTemplateListArraylist.add(new InvestigationTemplates(jsonArray4.getJSONObject(l).getInt("invest_template_id"),
                                    jsonArray4.getJSONObject(l).getInt("doc_id"),jsonArray4.getJSONObject(l).getInt("doc_type"),
                                    jsonArray4.getJSONObject(l).getInt("default_visible"),jsonArray4.getJSONObject(l).getString("template_name"),
                                    jsonArray4.getJSONObject(l).getString("main_test_id"),jsonArray4.getJSONObject(l).getString("group_test_id"),
                                    jsonArray4.getJSONObject(l).getString("test_name_site_name"),jsonArray4.getJSONObject(l).getInt("department"),
                                    jsonArray4.getJSONObject(l).getString("normal_range"),jsonArray4.getJSONObject(l).getString("test_actual_value"),
                                    "","",USER_ID, USER_LOGIN_TYPE));

                            long investtemplatet_insert = MedisensePracticeDB.investigationTemplatesInsert(RefreshOphthalEMRActivity.this,jsonArray4.getJSONObject(l).getInt("invest_template_id"),
                                    jsonArray4.getJSONObject(l).getInt("doc_id"),jsonArray4.getJSONObject(l).getInt("doc_type"),
                                    jsonArray4.getJSONObject(l).getInt("default_visible"),jsonArray4.getJSONObject(l).getString("template_name"),
                                    jsonArray4.getJSONObject(l).getString("main_test_id"),jsonArray4.getJSONObject(l).getString("group_test_id"),
                                    jsonArray4.getJSONObject(l).getString("test_name_site_name"),jsonArray4.getJSONObject(l).getInt("department"),
                                    jsonArray4.getJSONObject(l).getString("normal_range"),jsonArray4.getJSONObject(l).getString("test_actual_value"),
                                    "","",USER_ID, USER_LOGIN_TYPE);
                        }
                        Log.d(Utils.TAG, "investigationTemplateListArraylist: " + investigationTemplateListArraylist.size());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getJsonDiagnosisFrequent extends AsyncTask<String,String,String> {

        public getJsonDiagnosisFrequent(FragmentActivity activity) {
            mBiColoredProgressDiagnosis.setAnimated(true, 6000, new BounceInterpolator());
        }

        @Override
        protected String doInBackground(String... key) {
            freqDiagnosisListArraylist = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_DIAGNOSIS_FREQUENT_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Log.d(Utils.TAG, response.toString());
                            DIAGNOSIS_STATUS = true;
                            refreshAllDiagnosisContent(10000, 500, response);
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
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void refreshAllDiagnosisContent(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgressDiagnosis.setProgress(incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyProcessDiagnosis(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgressDiagnosis.setProgress(100f);
                diagnosis_done.setVisibility(View.VISIBLE);

                new getJsonTreatments(RefreshOphthalEMRActivity.this).execute();
            }
        }.start();
    }

    private void volleyProcessDiagnosis(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    freqDiagnosisListArraylist = new ArrayList<>();
                    jsonArray1 = jsonObject1.getJSONArray("frequent_diagnosis_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearFrequentDiagnosis(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            freqDiagnosisListArraylist.add(new Diagnosis(jsonArray1.getJSONObject(i).getInt("dfd_id"),jsonArray1.getJSONObject(i).getInt("icd_id"),
                                    jsonArray1.getJSONObject(i).getString("icd_code"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getInt("freq_count"),USER_ID, USER_LOGIN_TYPE));

                            long freqdiagno_insert = MedisensePracticeDB.frequentDiagnosisInsert(RefreshOphthalEMRActivity.this,jsonArray1.getJSONObject(i).getInt("dfd_id"),jsonArray1.getJSONObject(i).getInt("icd_id"),
                                    jsonArray1.getJSONObject(i).getString("icd_code"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getInt("freq_count"),USER_ID, USER_LOGIN_TYPE);

                        }
                        Log.d(Utils.TAG, "freqDiagnosisListArraylist: " + freqDiagnosisListArraylist.size());
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getJsonTreatments extends AsyncTask<String,String,String> {

        public getJsonTreatments(FragmentActivity activity) {
            mBiColoredProgressTreatment.setAnimated(true, 6000, new BounceInterpolator());
        }

        @Override
        protected String doInBackground(String... key) {
            freqTreatmentListArraylist = new ArrayList<>();
            treatmentListArraylist = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_TREATMENTS_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //  Log.d(Utils.TAG, response.toString());
                            TREATMENT_STATUS = true;
                            refreshAllTreatmentContent(10000, 500, response);
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
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void refreshAllTreatmentContent(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgressTreatment.setProgress(incrementSeconds);
                // Log.d(Utils.TAG, " MyCountDownTimer "+incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyProcessTreatments(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgressTreatment.setProgress(100f);
                treatment_done.setVisibility(View.VISIBLE);

                new getJsonPrescription(RefreshOphthalEMRActivity.this).execute();
            }
        }.start();
    }

    private void volleyProcessTreatments(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    freqTreatmentListArraylist = new ArrayList<>();
                    treatmentListArraylist = new ArrayList<>();
                    jsonArray1 = jsonObject1.getJSONArray("frequent_treatment_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearFrequentTreatments(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            freqTreatmentListArraylist.add(new Treatments(jsonArray1.getJSONObject(i).getInt("dft_id"),
                                    jsonArray1.getJSONObject(i).getString("treatment"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getInt("freq_count"),USER_ID, USER_LOGIN_TYPE));

                            long freqtreat_insert = MedisensePracticeDB.frequentTreatmentInsert(RefreshOphthalEMRActivity.this,jsonArray1.getJSONObject(i).getInt("dft_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_id"), jsonArray1.getJSONObject(i).getInt("doc_type"),
                                    jsonArray1.getJSONObject(i).getInt("freq_count"), jsonArray1.getJSONObject(i).getString("treatment"),
                                    USER_ID, USER_LOGIN_TYPE);

                        }
                        Log.d(Utils.TAG, "freqTreatmentListArraylist: " + freqTreatmentListArraylist.size());
                    }

                    jsonArray2 = jsonObject1.getJSONArray("treatment_details");
                    if (jsonArray2.length() > 0) {
                        MedisensePracticeDB.clearTreatments(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int j = 0; j < jsonArray2.length(); j++) {
                            treatmentListArraylist.add(new Treatments(jsonArray2.getJSONObject(j).getInt("dft_id"),
                                    jsonArray2.getJSONObject(j).getString("treatment"),jsonArray2.getJSONObject(j).getInt("doc_id"),
                                    jsonArray2.getJSONObject(j).getInt("doc_type"),jsonArray2.getJSONObject(j).getInt("freq_count"),USER_ID, USER_LOGIN_TYPE));

                            long treatment_insert = MedisensePracticeDB.treatmentInsert(RefreshOphthalEMRActivity.this,jsonArray2.getJSONObject(j).getInt("dft_id"),
                                    jsonArray2.getJSONObject(j).getString("treatment"), jsonArray2.getJSONObject(j).getInt("doc_id"),
                                    jsonArray2.getJSONObject(j).getInt("doc_type"),jsonArray2.getJSONObject(j).getInt("freq_count"),
                                    USER_ID, USER_LOGIN_TYPE);

                        }
                        Log.d(Utils.TAG, "treatmentListArraylist: " + treatmentListArraylist.size());

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getJsonPrescription extends AsyncTask<String,String,String> {

        public getJsonPrescription(FragmentActivity activity) {
            mBiColoredProgressPrescription.setAnimated(true, 6000, new BounceInterpolator());
        }

        @Override
        protected String doInBackground(String... key) {
            templateListArraylist = new ArrayList<>();
            freqPrescriptionsListArraylist = new ArrayList<>();
            repeatPrescriptionsListArraylist = new ArrayList<>();
            //  prescriptionListArraylist = new ArrayList<>();
            patientEducationListArraylist = new ArrayList<>();

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_PRESCRIPTION_FREQUENT_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                             Log.d(Utils.TAG, "presclist: "+ response.toString());
                            PRESCRIPTION_STATUS = true;
                            refreshAllPrescriptionContent(10000, 500, response);
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
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void refreshAllPrescriptionContent(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgressPrescription.setProgress(incrementSeconds);
                //  Log.d(Utils.TAG, " MyCountDownTimer "+incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyProcessPrescriptions(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgressPrescription.setProgress(100f);
                prescription_done.setVisibility(View.VISIBLE);

                new getJsonDrugAllergies(RefreshOphthalEMRActivity.this).execute();
                //    continue_btn.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void volleyProcessPrescriptions(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null;JSONArray jsonArray3 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    templateListArraylist = new ArrayList<>();
                    freqPrescriptionsListArraylist = new ArrayList<>();
                    repeatPrescriptionsListArraylist = new ArrayList<>();
                    patientEducationListArraylist = new ArrayList<>();
                    jsonArray1 = jsonObject1.getJSONArray("template_deatils");
                    if (jsonArray1.length() > 0) {
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            templateListArraylist.add(new MyTemplates(jsonArray1.getJSONObject(i).getInt("template_id"), jsonArray1.getJSONObject(i).getInt("admin_id"),
                                    jsonArray1.getJSONObject(i).getString("template_name"), USER_ID, USER_LOGIN_TYPE));
                        }
                        Log.d(Utils.TAG, "size: " + templateListArraylist.size());

                    }

                    //Set the values
                    Gson gson = new Gson();
                    String jsonTemplatesText = gson.toJson(templateListArraylist);
                    if (sharedPreferences != null) {
                        shareadPreferenceClass.clearMyPatientsTemplates();
                        shareadPreferenceClass.setMyPatientsTemplates(jsonTemplatesText);
                    }

                    jsonArray1 = jsonObject1.getJSONArray("frequent_medicine_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearFrequentPrescriptions(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            freqPrescriptionsListArraylist.add(new FrequentPrescription(jsonArray1.getJSONObject(i).getInt("freq_medicine_id"),jsonArray1.getJSONObject(i).getInt("pp_id"),
                                    jsonArray1.getJSONObject(i).getString("med_trade_name"),0, jsonArray1.getJSONObject(i).getString("med_generic_name"),
                                    jsonArray1.getJSONObject(i).getString("med_frequency"),jsonArray1.getJSONObject(i).getString("med_timing"),
                                    jsonArray1.getJSONObject(i).getString("med_duration"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getInt("freq_count"),USER_ID, USER_LOGIN_TYPE));

                            long freqpresc_insert = MedisensePracticeDB.frequentPrescriptionInsert(RefreshOphthalEMRActivity.this,jsonArray1.getJSONObject(i).getInt("freq_medicine_id"),jsonArray1.getJSONObject(i).getInt("pp_id"),
                                    jsonArray1.getJSONObject(i).getString("med_trade_name"),0, jsonArray1.getJSONObject(i).getString("med_generic_name"),
                                    jsonArray1.getJSONObject(i).getString("med_frequency"),jsonArray1.getJSONObject(i).getString("med_timing"),
                                    jsonArray1.getJSONObject(i).getString("med_duration"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"),jsonArray1.getJSONObject(i).getInt("freq_count"),USER_ID, USER_LOGIN_TYPE);

                        }

                        Log.d(Utils.TAG, "freqPrescriptionsListArraylist: " + freqPrescriptionsListArraylist.size());
                    }

                    jsonArray2 = jsonObject1.getJSONArray("repeat_precription_details");
                    if (jsonArray2.length() > 0) {
                        MedisensePracticeDB.clearRepeatPrescriptions(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int j = 0; j < jsonArray2.length(); j++) {
                            repeatPrescriptionsListArraylist.add(new FrequentPrescription(0,jsonArray2.getJSONObject(j).getInt("pp_id"),
                                    jsonArray2.getJSONObject(j).getString("prescription_trade_name"),0, jsonArray2.getJSONObject(j).getString("prescription_generic_name"),
                                    jsonArray2.getJSONObject(j).getString("prescription_dosage_name"),jsonArray2.getJSONObject(j).getString("timing"),
                                    jsonArray2.getJSONObject(j).getString("duration"),jsonArray2.getJSONObject(j).getInt("doc_id"),
                                    1,0,USER_ID, USER_LOGIN_TYPE));

                            long repeatpresc_insert = MedisensePracticeDB.repeatPrescriptionInsert(RefreshOphthalEMRActivity.this,0,jsonArray2.getJSONObject(j).getInt("pp_id"),
                                    jsonArray2.getJSONObject(j).getString("prescription_trade_name"),0, jsonArray2.getJSONObject(j).getString("prescription_generic_name"),
                                    jsonArray2.getJSONObject(j).getString("prescription_dosage_name"),jsonArray2.getJSONObject(j).getString("timing"),
                                    jsonArray2.getJSONObject(j).getString("duration"),jsonArray2.getJSONObject(j).getInt("doc_id"),
                                    1,0,USER_ID, USER_LOGIN_TYPE);

                        }
                        Log.d(Utils.TAG, "repeatPrescriptionsListArraylist: " + repeatPrescriptionsListArraylist.size());

                    }

                    jsonArray3 = jsonObject1.getJSONArray("patient_education_details");
                    if (jsonArray3.length() > 0) {
                        MedisensePracticeDB.clearPatientEducation(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int k = 0; k < jsonArray3.length(); k++) {

                           String edu_titles =  Html.fromHtml(jsonArray3.getJSONObject(k).getString("edu_title")).toString();
                           String edu_descriptions =  Html.fromHtml(jsonArray3.getJSONObject(k).getString("edu_description")).toString();
                           Log.d(Utils.TAG, " edu_titles: " + edu_titles );
                           Log.d(Utils.TAG, " edu_descriptions: " + edu_descriptions );
                            patientEducationListArraylist.add(new PatientEducation(jsonArray3.getJSONObject(k).getInt("edu_id"),
                                    edu_titles,jsonArray3.getJSONObject(k).getString("edu_description"),
                                    jsonArray3.getJSONObject(k).getInt("doc_id"),jsonArray3.getJSONObject(k).getInt("doc_type"),
                                    USER_ID, USER_LOGIN_TYPE));

                            long education_insert = MedisensePracticeDB.patientEducationInsert(RefreshOphthalEMRActivity.this,jsonArray3.getJSONObject(k).getInt("edu_id"),
                                    jsonArray3.getJSONObject(k).getString("edu_title"),jsonArray3.getJSONObject(k).getString("edu_description"),
                                    jsonArray3.getJSONObject(k).getInt("doc_id"),jsonArray3.getJSONObject(k).getInt("doc_type"),
                                    USER_ID, USER_LOGIN_TYPE);
                        }
                        Log.d(Utils.TAG, "patientEducationListArraylist: " + patientEducationListArraylist.size());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
    }

    class getJsonDrugAllergies extends AsyncTask<String,String,String> {


        public getJsonDrugAllergies(FragmentActivity activity) {
            mBiColoredProgressDrugAllergy.setAnimated(true, 6000, new BounceInterpolator());
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
                            // Log.d(Utils.TAG, response.toString());
                            DRUG_ALEERGY_STATUS = true;
                            refreshAllDrugAllergyContent(10000, 500, response);
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
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void refreshAllDrugAllergyContent(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgressDrugAllergy.setProgress(incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyProcessDrugAllergy(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgressDrugAllergy.setProgress(100f);
                drugallergy_done.setVisibility(View.VISIBLE);

                new getJsonDrugAbuse(RefreshOphthalEMRActivity.this).execute();
            }
        }.start();
    }

    private void volleyProcessDrugAllergy(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    drugAlleryListArraylist = new ArrayList<>();
                    jsonArray1 = jsonObject1.getJSONArray("frequent_allergy_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearDrugAllergy(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            drugAlleryListArraylist.add(new DrugAllery(jsonArray1.getJSONObject(i).getInt("allergy_id"),jsonArray1.getJSONObject(i).getInt("generic_id"),
                                    jsonArray1.getJSONObject(i).getString("generic_name"), jsonArray1.getJSONObject(i).getInt("patient_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_id"), jsonArray1.getJSONObject(i).getInt("doc_type"),
                                    USER_ID, USER_LOGIN_TYPE));

                            long freqallergy_insert = MedisensePracticeDB.drugAllergyInsert(RefreshOphthalEMRActivity.this,jsonArray1.getJSONObject(i).getInt("allergy_id"),jsonArray1.getJSONObject(i).getInt("generic_id"),
                                    jsonArray1.getJSONObject(i).getString("generic_name"),jsonArray1.getJSONObject(i).getInt("patient_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_id"), jsonArray1.getJSONObject(i).getInt("doc_type"),
                                    USER_ID, USER_LOGIN_TYPE);

                        }
                        Log.d(Utils.TAG, "freqAllergyListArraylist: " + drugAlleryListArraylist.size());

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getJsonDrugAbuse extends AsyncTask<String,String,String> {

        public getJsonDrugAbuse(FragmentActivity activity) {
            mBiColoredProgressDrugAbuse.setAnimated(true, 6000, new BounceInterpolator());
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
                            // Log.d(Utils.TAG, response.toString());
                            DRUG_ABUSE_STATUS = true;
                            refreshAllDrugAbuseContent(10000, 500, response);
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
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void refreshAllDrugAbuseContent(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgressDrugAbuse.setProgress(incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyProcessDrugAbuse(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgressDrugAbuse.setProgress(100f);
                drugabuse_done.setVisibility(View.VISIBLE);

                new getJsonFamilyHistory(RefreshOphthalEMRActivity.this).execute();
            }
        }.start();
    }

    private void volleyProcessDrugAbuse(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    freqdrugAbuseListArraylist = new ArrayList<>();
                    drugAbuseListArraylist  = new ArrayList<>();

                    jsonArray1 = jsonObject1.getJSONArray("frequent_drug_abuse_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearDrugAbuseFrequent(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            freqdrugAbuseListArraylist.add(new DrugAbuse(jsonArray1.getJSONObject(i).getInt("fda_id"),jsonArray1.getJSONObject(i).getInt("drug_abuse_id"),
                                    jsonArray1.getJSONObject(i).getString("drug_abuse"), jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"), jsonArray1.getJSONObject(i).getInt("freq_count"),
                                    USER_ID, USER_LOGIN_TYPE));

                            long freqabuse_insert = MedisensePracticeDB.drugAbuseFrequentInsert(RefreshOphthalEMRActivity.this,jsonArray1.getJSONObject(i).getInt("fda_id"),jsonArray1.getJSONObject(i).getInt("drug_abuse_id"),
                                    jsonArray1.getJSONObject(i).getString("drug_abuse"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"), jsonArray1.getJSONObject(i).getInt("freq_count"),
                                    USER_ID, USER_LOGIN_TYPE);

                        }

                        Log.d(Utils.TAG, "freqdrugAbuseListArraylist: " + freqdrugAbuseListArraylist.size());
                    }

                    jsonArray2 = jsonObject1.getJSONArray("drug_abuse_details");
                    if (jsonArray2.length() > 0) {
                        MedisensePracticeDB.clearDrugAbuse(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray2.length(); i++) {
                            drugAbuseListArraylist.add(new DrugAbuse(jsonArray2.getJSONObject(i).getInt("drug_abuse_id"),
                                    jsonArray2.getJSONObject(i).getString("drug_abuse"), jsonArray2.getJSONObject(i).getInt("doc_id"),
                                    jsonArray2.getJSONObject(i).getInt("doc_type"), USER_ID, USER_LOGIN_TYPE));

                            long abuse_insert = MedisensePracticeDB.drugAbuseInsert(RefreshOphthalEMRActivity.this,jsonArray2.getJSONObject(i).getInt("drug_abuse_id"),
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
    }

    class getJsonFamilyHistory extends AsyncTask<String,String,String> {

        public getJsonFamilyHistory(FragmentActivity activity) {
            mBiColoredProgressFamilyHistory.setAnimated(true, 6000, new BounceInterpolator());
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
                            FAMILY_HISTORY_STATUS = true;
                            refreshAllFamilyHistoryContent(10000, 500, response);
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
            RequestQueue requestQueue1 = AppController.getInstance(RefreshOphthalEMRActivity.this).
                    getRequestQueue();
            AppController.getInstance(RefreshOphthalEMRActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void refreshAllFamilyHistoryContent(long millisInFuture, long countDownInterval, final String response) {
        new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                // this will decrement 100 to 1
                int seconds = (int) (millisUntilFinished / 100);
                // this will increment 1 to 100
                final int incrementSeconds = 100 - seconds;
                mBiColoredProgressFamilyHistory.setProgress(incrementSeconds);
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                volleyProcessFamilyHistory(response);

                // Log.d(Utils.TAG, response.toString());
                mBiColoredProgressFamilyHistory.setProgress(100f);
                familyhistory_done.setVisibility(View.VISIBLE);

                continue_btn.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void volleyProcessFamilyHistory(String response) {
        if (response != null) {
            JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null; JSONArray jsonArray2 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String staus_res = jsonObject1.getString("status");
                if (staus_res.equals("false")) {
                } else {
                    freqfamilyHistoryListArraylist = new ArrayList<>();
                    familyHistoryListArraylist  = new ArrayList<>();

                    jsonArray1 = jsonObject1.getJSONArray("frequent_family_history_details");
                    if (jsonArray1.length() > 0) {
                        MedisensePracticeDB.clearFamilyHistoryFrequent(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            freqfamilyHistoryListArraylist.add(new FamilyHistory(jsonArray1.getJSONObject(i).getInt("ffh_id"),jsonArray1.getJSONObject(i).getInt("family_history_id"),
                                    jsonArray1.getJSONObject(i).getString("family_history"), jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"), jsonArray1.getJSONObject(i).getInt("freq_count"),
                                    USER_ID, USER_LOGIN_TYPE));

                            long freqhistory_insert = MedisensePracticeDB.familyHistoryFrequentInsert(RefreshOphthalEMRActivity.this,jsonArray1.getJSONObject(i).getInt("ffh_id"),jsonArray1.getJSONObject(i).getInt("family_history_id"),
                                    jsonArray1.getJSONObject(i).getString("family_history"),jsonArray1.getJSONObject(i).getInt("doc_id"),
                                    jsonArray1.getJSONObject(i).getInt("doc_type"), jsonArray1.getJSONObject(i).getInt("freq_count"),
                                    USER_ID, USER_LOGIN_TYPE);

                        }
                        Log.d(Utils.TAG, "freqfamilyHistoryListArraylist: " + freqfamilyHistoryListArraylist.size());
                    }

                    jsonArray2 = jsonObject1.getJSONArray("family_history_details");
                    if (jsonArray2.length() > 0) {
                        MedisensePracticeDB.clearFamilyHistory(RefreshOphthalEMRActivity.this,USER_ID,USER_LOGIN_TYPE);
                        for (int i = 0; i < jsonArray2.length(); i++) {
                            familyHistoryListArraylist.add(new FamilyHistory(jsonArray2.getJSONObject(i).getInt("family_history_id"),
                                    jsonArray2.getJSONObject(i).getString("family_history"), jsonArray2.getJSONObject(i).getInt("doc_id"),
                                    jsonArray2.getJSONObject(i).getInt("doc_type"), USER_ID, USER_LOGIN_TYPE));

                            long family_insert = MedisensePracticeDB.familyHistoryInsert(RefreshOphthalEMRActivity.this,jsonArray2.getJSONObject(i).getInt("family_history_id"),
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(Utils.TAG, " ********** Resume **************");

        if(CHIEF_MED_COMP_STATUS) {
          //  Log.d(Utils.TAG, " CHIEF_MED_COMP_STATUS TRUE");
            mBiColoredProgress.setProgress(100f);
        }

        if(LIDS_STATUS) {
            mBiColoredProgressLids.setProgress(100f);
        }

        if(CONJUCTIVA_STATUS) {
            mBiColoredProgressConjuctiva.setProgress(100f);
        }

        if(SCALERA_STATUS) {
            mBiColoredProgressSclera.setProgress(100f);
        }

        if(CORNEA_ANTERIOR_STATUS) {
            mBiColoredProgressCorneaAnterior.setProgress(100f);
        }

        if(CORNEA_POSTERIOR_STATUS) {
            mBiColoredProgressCorneaPosterior.setProgress(100f);
        }

        if(ANT_CHAMBER_STATUS) {
            mBiColoredProgressAnteriorChamber.setProgress(100f);
        }

        if(IRIS_STATUS) {
            mBiColoredProgressIris.setProgress(100f);
        }

        if(PUPIL_STATUS) {
            mBiColoredProgressPupil.setProgress(100f);
        }

        if(ANGLE_ANT_STATUS) {
            mBiColoredProgressAngle.setProgress(100f);
        }

        if(LENS_STATUS) {
            mBiColoredProgressLens.setProgress(100f);
        }

        if(VITEROUS_STATUS) {
            mBiColoredProgressViterous.setProgress(100f);
        }

        if(FUNDUS_STATUS) {
            mBiColoredProgressFundus.setProgress(100f);
        }

        if(INVESTIGATIONS_STATUS) {
            mBiColoredProgressInvestigation.setProgress(100f);
        }

        if(DIAGNOSIS_STATUS) {
            mBiColoredProgressDiagnosis.setProgress(100f);
        }

        if(TREATMENT_STATUS) {
            mBiColoredProgressTreatment.setProgress(100f);
        }

        if(PRESCRIPTION_STATUS) {
            mBiColoredProgressPrescription.setProgress(100f);
        }

        if(DRUG_ALEERGY_STATUS) {
            mBiColoredProgressDrugAllergy.setProgress(100f);
        }

        if(DRUG_ABUSE_STATUS) {
            mBiColoredProgressDrugAbuse.setProgress(100f);
        }

        if(FAMILY_HISTORY_STATUS) {
            mBiColoredProgressFamilyHistory.setProgress(100f);
        }
    }
}
