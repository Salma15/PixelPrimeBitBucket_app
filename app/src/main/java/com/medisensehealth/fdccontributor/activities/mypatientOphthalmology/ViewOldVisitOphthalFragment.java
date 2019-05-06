package com.medisensehealth.fdccontributor.activities.mypatientOphthalmology;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medisensehealth.fdccontributor.DataModel.ChiefMedicalComplaint;
import com.medisensehealth.fdccontributor.DataModel.Diagnosis;
import com.medisensehealth.fdccontributor.DataModel.FrequentPrescription;
import com.medisensehealth.fdccontributor.DataModel.Investigations;
import com.medisensehealth.fdccontributor.DataModel.Lids;
import com.medisensehealth.fdccontributor.DataModel.OldVisitsOphthalList;
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
import com.medisensehealth.fdccontributor.DataModel.Treatments;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextViewItalicBold;

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
public class ViewOldVisitOphthalFragment extends Fragment {

    FragmentManager manager;

    int USER_ID, PATIENT_ID;
    String USER_NAME,USER_LOGIN_TYPE, GET_EPISODE_LIST_TEMP;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;

    String PATIENT_NAME, PATIENT_AGE, PATIENT_GENDER, PATIENT_MOBILE, PATIENT_EMAIL, PATIENT_CITY, PATIENT_ADDRESS, PATIENT_STATE, PATIENT_COUNTRY,
            PATIENT_HEIGHT, PATIENT_WEIGHT, PATIENT_HYPERTENSION, PATIENT_DIABETES, PATIENT_SMOKING, PATIENT_ALCOHOL, PATIENT_DRUG_ABUSE, PATIENT_OTHER_DETAILS,
            PATIENT_FAMILY_HISTORY, PATIENT_PREV_INTERVENTIONS, PATIENT_NEURO_ISSUES, PATIENT_KIDNEY_ISSUES;

    List<OldVisitsOphthalList> EPISODE_LIST = new ArrayList<>();
    List<OldVisitsOphthalList> EPISODE_LIST_TEMP = new ArrayList<>();
    Gson gson = new Gson();
    RecyclerView episodeRecyclerView;
    OldVisitOphthalListViewAdapter episodeAdapter;
    CustomTextViewItalicBold no_data;

    List<ChiefMedicalComplaint> PATIENT_CHIEF_MEDCOMPLAINT_ARRAY;
    List<Investigations> PATIENT_INVESTIGATION_GENERAL_ARRAY, PATIENT_INVESTIGATION_OPHTHAL_ARRAY, PATIENT_INVESTIGATION_RADIOLOGY_ARRAY, PATIENT_INVESTIGATION_CLINICAL_ARRAY, PATIENT_INVESTIGATION_OTHER_ARRAY;
    List<Diagnosis> PATIENT_DAIGNOSIS_ARRAY;
    List<Treatments> PATIENT_TREATMENT_ARRAY;
    List<FrequentPrescription> PATIENT_PRESCRIPTION_ARRAY;
    List<Lids> PATIENT_LIDS_ARRAY;
    List<OphthalConjuctiva> PATIENT_CONJUCTIVA_ARRAY;
    List<OphthalSclera> PATIENT_SCLERA_ARRAY;
    List<OphthalCornearAnteriorSurface> PATIENT_CORNEA_ANTERIOR_ARRAY;
    List<OphthalCornearPosteriorSurface> PATIENT_CORNEA_POSTERIOR_ARRAY;
    List<OphthalAnteriorChamber> PATIENT_ANTERIOR_CHAMBER_ARRAY;
    List<OphthalIris> PATIENT_IRIS_ARRAY;
    List<OphthalPupil> PATIENT_PUPIL_ARRAY;
    List<OphthalAngleAnteriorChamber> PATIENT_ANGLE_ARRAY;
    List<OphthalLens> PATIENT_LENS_ARRAY;
    List<OphthalViterous> PATIENT_VITEROUS_ARRAY;
    List<OphthalFundus> PATIENT_FUNDUS_ARRAY;

    ImageView refresh_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_old_visits, container, false);

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

            Log.d(Utils.TAG, " ********* ViewOldVisitOphthalFragment ************ ");
            Log.d(Utils.TAG, " PATIENT_ID: "+PATIENT_ID);
            Log.d(Utils.TAG, " PATIENT_NAME: "+PATIENT_NAME);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EPISODE_LIST = new ArrayList<>();
        EPISODE_LIST_TEMP = new ArrayList<>();
        PATIENT_CHIEF_MEDCOMPLAINT_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_GENERAL_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_OPHTHAL_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_RADIOLOGY_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_CLINICAL_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_OTHER_ARRAY = new ArrayList<>();
        PATIENT_DAIGNOSIS_ARRAY = new ArrayList<>();
        PATIENT_TREATMENT_ARRAY = new ArrayList<>();
        PATIENT_PRESCRIPTION_ARRAY = new ArrayList<>();
        PATIENT_LIDS_ARRAY = new ArrayList<>();
        PATIENT_CONJUCTIVA_ARRAY = new ArrayList<>();
        PATIENT_SCLERA_ARRAY = new ArrayList<>();
        PATIENT_CORNEA_ANTERIOR_ARRAY = new ArrayList<>();
        PATIENT_CORNEA_POSTERIOR_ARRAY = new ArrayList<>();
        PATIENT_ANTERIOR_CHAMBER_ARRAY = new ArrayList<>();
        PATIENT_IRIS_ARRAY = new ArrayList<>();
        PATIENT_PUPIL_ARRAY = new ArrayList<>();
        PATIENT_ANGLE_ARRAY = new ArrayList<>();
        PATIENT_LENS_ARRAY = new ArrayList<>();
        PATIENT_VITEROUS_ARRAY = new ArrayList<>();
        PATIENT_FUNDUS_ARRAY = new ArrayList<>();

    }

    @Override
    public void onResume() {
        super.onResume();

        shareadPreferenceClass = new ShareadPreferenceClass(getActivity());
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(getActivity());

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

        Log.d(Utils.TAG, " OnResume ");
        initializationViews();
    }

    private void initializationViews() {

        episodeRecyclerView = (RecyclerView) getActivity().findViewById(R.id.old_visit_episodelist);
        episodeAdapter = new OldVisitOphthalListViewAdapter(getActivity(), EPISODE_LIST);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        episodeRecyclerView.setLayoutManager(mLayoutManager);
        episodeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        episodeRecyclerView.setAdapter(episodeAdapter);

        refresh_btn = (ImageView)getActivity().findViewById(R.id.old_visit_episoderefresh);
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadEpisodesFromServer();
            }
        });
        no_data = (CustomTextViewItalicBold)getActivity().findViewById(R.id.old_visit_nodata);
        no_data.setVisibility(View.GONE);


        boolean status_res = false;
        if (GET_EPISODE_LIST_TEMP.equals("")) {
            Log.d(Utils.TAG, "EPISODE_LIST < 0 " + GET_EPISODE_LIST_TEMP);
            loadEpisodesFromServer();
        } else {
            EPISODE_LIST_TEMP = gson.fromJson(GET_EPISODE_LIST_TEMP, new TypeToken<List<OldVisitsOphthalList>>() {
            }.getType());
            if(EPISODE_LIST_TEMP.size() > 0){
                Log.d(Utils.TAG, "EPISODE_LIST > 0 " + EPISODE_LIST_TEMP.size());
                List<OldVisitsOphthalList> EPISODE_LIST_TEMP_NEW = new ArrayList<>();
                for(int p=0; p<EPISODE_LIST_TEMP.size();p++) {
                    if (PATIENT_ID == EPISODE_LIST_TEMP.get(p).getEpisodePatientID()) {

                        EPISODE_LIST_TEMP_NEW.add(new OldVisitsOphthalList(EPISODE_LIST_TEMP.get(p).getEpisodeID(),EPISODE_LIST_TEMP.get(p).getEpisodeUserID(),
                                EPISODE_LIST_TEMP.get(p).getEpisodePatientID(),EPISODE_LIST_TEMP.get(p).getEpisodeFollowupDate(),
                                EPISODE_LIST_TEMP.get(p).getEpisodeDiagnosisDetails(),EPISODE_LIST_TEMP.get(p).getEpisodeTreatmentDetails(),EPISODE_LIST_TEMP.get(p).getEpisodePrescriptionNotes(),
                                EPISODE_LIST_TEMP.get(p).getEpisodeCreatedDate(),EPISODE_LIST_TEMP.get(p).getEpisodeConsultaionFees(),
                                EPISODE_LIST_TEMP.get(p).getEpisodePatientEducationID(),EPISODE_LIST_TEMP.get(p).getEpisodeChiefMedComplaintSufferings(),
                                EPISODE_LIST_TEMP.get(p).getChiefMedicalList(),EPISODE_LIST_TEMP.get(p).getInvestigationGeneralList(),
                                EPISODE_LIST_TEMP.get(p).getInvestigationOphthalList(),
                                EPISODE_LIST_TEMP.get(p).getInvestigationRadiologyList(),
                                EPISODE_LIST_TEMP.get(p).getInvestigationClinicalList(), EPISODE_LIST_TEMP.get(p).getInvestigationOtherList(),
                                EPISODE_LIST_TEMP.get(p).getDiagnosisList(),EPISODE_LIST_TEMP.get(p).getTreatmentList(),
                                EPISODE_LIST_TEMP.get(p).getPrescriptionList(),
                                EPISODE_LIST_TEMP.get(p).getLidsList(),EPISODE_LIST_TEMP.get(p).getConjuctivaList(),EPISODE_LIST_TEMP.get(p).getScleraList(),
                                EPISODE_LIST_TEMP.get(p).getCorneaAnteriorList(),EPISODE_LIST_TEMP.get(p).getCorneaPosteriorList(),EPISODE_LIST_TEMP.get(p).getAnteriorChamberList(),
                                EPISODE_LIST_TEMP.get(p).getIrisList(),EPISODE_LIST_TEMP.get(p).getPupilList(),EPISODE_LIST_TEMP.get(p).getAngleList(),
                                EPISODE_LIST_TEMP.get(p).getLensList(),EPISODE_LIST_TEMP.get(p).getViterousList(),EPISODE_LIST_TEMP.get(p).getFundusList(),
                                EPISODE_LIST_TEMP.get(p).getEpisodeRefractionRE1(),EPISODE_LIST_TEMP.get(p).getEpisodeRefractionRE2(),
                                EPISODE_LIST_TEMP.get(p).getEpisodeRefractionLE1(),EPISODE_LIST_TEMP.get(p).getEpisodeRefractionLE2(),
                                EPISODE_LIST_TEMP.get(p).getEpisodeDistanceVisionRE(),EPISODE_LIST_TEMP.get(p).getEpisodeDistanceVisionLE(),
                                EPISODE_LIST_TEMP.get(p).getEpisodeNearVisionRE(),EPISODE_LIST_TEMP.get(p).getEpisodeNearVisionLE(),
                                EPISODE_LIST_TEMP.get(p).getEpisodeDvSphereRE(),EPISODE_LIST_TEMP.get(p).getEpisodeDvCycleRE(),EPISODE_LIST_TEMP.get(p).getEpisodeDvAxisRE(),
                                EPISODE_LIST_TEMP.get(p).getEpisodeDvSphereLE(),EPISODE_LIST_TEMP.get(p).getEpisodeDvCycleLE(),EPISODE_LIST_TEMP.get(p).getEpisodeDvAxisLE(),
                                EPISODE_LIST_TEMP.get(p).getEpisodeNvSphereRE(),EPISODE_LIST_TEMP.get(p).getEpisodeNvCycleRE(),EPISODE_LIST_TEMP.get(p).getEpisodeNvAxisRE(),
                                EPISODE_LIST_TEMP.get(p).getEpisodeNvSphereLE(),EPISODE_LIST_TEMP.get(p).getEpisodeNvCycleLE(),EPISODE_LIST_TEMP.get(p).getEpisodeNvAxisLE(),
                                EPISODE_LIST_TEMP.get(p).getEpisodeIPDRE(),EPISODE_LIST_TEMP.get(p).getEpisodeIPDLE(),
                                EPISODE_LIST_TEMP.get(p).getEpisodeDistanceVisionUnaidedRE(),EPISODE_LIST_TEMP.get(p).getEpisodeDistanceVisionUnaidedLE(),
                                EPISODE_LIST_TEMP.get(p).getEpisodeNearVisionUnaidedRE(),EPISODE_LIST_TEMP.get(p).getEpisodeNearVisionUnaidedLE(),
                                EPISODE_LIST_TEMP.get(p).getEpisodeIOPRE(),EPISODE_LIST_TEMP.get(p).getEpisodeIOPLE()));
                        status_res = true;
                    }
                }
                if(status_res) {
                    // Log.d(Utils.TAG, "EPISODE_LIST same patient"+ String.valueOf(status_res) +" size: "+EPISODE_LIST_TEMP_NEW.size());
                    episodeAdapter = new OldVisitOphthalListViewAdapter(getActivity(), EPISODE_LIST_TEMP_NEW);
                    episodeRecyclerView.setAdapter(episodeAdapter);
                    episodeAdapter.notifyDataSetChanged();
                }
                else {
                    //  Log.d(Utils.TAG, "EPISODE_LIST different patient"+ String.valueOf(status_res)+" size: "+EPISODE_LIST_TEMP_NEW.size());
                    loadEpisodesFromServer();
                }

            }

        }
    }

    private void loadEpisodesFromServer() {
        EPISODE_LIST = new ArrayList<>();
        EPISODE_LIST_TEMP = new ArrayList<>();
        PATIENT_CHIEF_MEDCOMPLAINT_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_GENERAL_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_OPHTHAL_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_RADIOLOGY_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_CLINICAL_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_OTHER_ARRAY = new ArrayList<>();
        PATIENT_DAIGNOSIS_ARRAY = new ArrayList<>();
        PATIENT_TREATMENT_ARRAY = new ArrayList<>();
        PATIENT_PRESCRIPTION_ARRAY = new ArrayList<>();
        PATIENT_LIDS_ARRAY = new ArrayList<>();
        PATIENT_CONJUCTIVA_ARRAY = new ArrayList<>();
        PATIENT_SCLERA_ARRAY = new ArrayList<>();
        PATIENT_CORNEA_ANTERIOR_ARRAY = new ArrayList<>();
        PATIENT_CORNEA_POSTERIOR_ARRAY = new ArrayList<>();
        PATIENT_ANTERIOR_CHAMBER_ARRAY = new ArrayList<>();
        PATIENT_IRIS_ARRAY = new ArrayList<>();
        PATIENT_PUPIL_ARRAY = new ArrayList<>();
        PATIENT_ANGLE_ARRAY = new ArrayList<>();
        PATIENT_LENS_ARRAY = new ArrayList<>();
        PATIENT_VITEROUS_ARRAY = new ArrayList<>();
        PATIENT_FUNDUS_ARRAY = new ArrayList<>();

        final ProgressDialog dialog2 = new ProgressDialog(getContext(), ProgressDialog.THEME_HOLO_LIGHT);
        dialog2.setIndeterminate(true);
        dialog2.setMessage("Loading Episodes...");
        dialog2.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_OLD_VISITS_OPHTHAL_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray jsonArray = null;
                        String status = null;
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Log.d(Utils.TAG, " old_visit list: "+ response.toString());
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("false")) {
                                    episodeRecyclerView.setVisibility(View.GONE);
                                    no_data.setVisibility(View.VISIBLE);
                                } else {
                                    jsonArray = jsonObject.getJSONArray("old_visit_details");
                                    if (jsonArray.length() > 0) {
                                        no_data.setVisibility(View.GONE);
                                        episodeRecyclerView.setVisibility(View.VISIBLE);

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            PATIENT_CHIEF_MEDCOMPLAINT_ARRAY = new ArrayList<>();
                                            PATIENT_INVESTIGATION_GENERAL_ARRAY = new ArrayList<>();
                                            PATIENT_INVESTIGATION_OPHTHAL_ARRAY = new ArrayList<>();
                                            PATIENT_INVESTIGATION_RADIOLOGY_ARRAY = new ArrayList<>();
                                            PATIENT_INVESTIGATION_CLINICAL_ARRAY = new ArrayList<>();
                                            PATIENT_INVESTIGATION_OTHER_ARRAY = new ArrayList<>();
                                            PATIENT_DAIGNOSIS_ARRAY = new ArrayList<>();
                                            PATIENT_TREATMENT_ARRAY = new ArrayList<>();
                                            PATIENT_PRESCRIPTION_ARRAY = new ArrayList<>();
                                            PATIENT_LIDS_ARRAY = new ArrayList<>();
                                            PATIENT_CONJUCTIVA_ARRAY = new ArrayList<>();
                                            PATIENT_SCLERA_ARRAY = new ArrayList<>();
                                            PATIENT_CORNEA_ANTERIOR_ARRAY = new ArrayList<>();
                                            PATIENT_CORNEA_POSTERIOR_ARRAY = new ArrayList<>();
                                            PATIENT_ANTERIOR_CHAMBER_ARRAY = new ArrayList<>();
                                            PATIENT_IRIS_ARRAY = new ArrayList<>();
                                            PATIENT_PUPIL_ARRAY = new ArrayList<>();
                                            PATIENT_ANGLE_ARRAY = new ArrayList<>();
                                            PATIENT_LENS_ARRAY = new ArrayList<>();
                                            PATIENT_VITEROUS_ARRAY = new ArrayList<>();
                                            PATIENT_FUNDUS_ARRAY = new ArrayList<>();

                                            JSONObject epObj = jsonArray.getJSONObject(i);
                                            Log.d(Utils.TAG, " epID: "+ epObj.getString("episode_id"));

                                            JSONArray jsonArrayChiefMed = epObj.getJSONArray("chief_medical_complaint_result");
                                            for (int j = 0; j < jsonArrayChiefMed.length(); j++) {
                                                Log.d(Utils.TAG, " ChiefMedID: "+ jsonArrayChiefMed.getJSONObject(j).getString("symptoms_id"));

                                                PATIENT_CHIEF_MEDCOMPLAINT_ARRAY.add(new ChiefMedicalComplaint(jsonArrayChiefMed.getJSONObject(j).getInt("symptoms_id"),
                                                        jsonArrayChiefMed.getJSONObject(j).getString("symptoms_name"),jsonArrayChiefMed.getJSONObject(j).getInt("doc_id"),
                                                        jsonArrayChiefMed.getJSONObject(j).getInt("doc_type"),USER_ID, USER_LOGIN_TYPE));

                                            }

                                            JSONArray jsonArrayInvestigation = epObj.getJSONArray("investigation_result");
                                            for (int k = 0; k < jsonArrayInvestigation.length(); k++) {
                                                Log.d(Utils.TAG, " invest departmentID: "+ jsonArrayInvestigation.getJSONObject(k).getInt("department"));

                                                if(jsonArrayInvestigation.getJSONObject(k).getInt("department") == 1) {
                                                    PATIENT_INVESTIGATION_GENERAL_ARRAY.add(new Investigations(jsonArrayInvestigation.getJSONObject(k).getInt("pti_id"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("main_test_id"),jsonArrayInvestigation.getJSONObject(k).getString("group_test_id"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("test_name"),jsonArrayInvestigation.getJSONObject(k).getString("normal_range"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("test_actual_value"),jsonArrayInvestigation.getJSONObject(k).getString("right_eye"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("left_eye"),jsonArrayInvestigation.getJSONObject(k).getInt("department"),
                                                            USER_ID, USER_LOGIN_TYPE));
                                                }
                                                else  if(jsonArrayInvestigation.getJSONObject(k).getInt("department") == 2) {
                                                    PATIENT_INVESTIGATION_OPHTHAL_ARRAY.add(new Investigations(jsonArrayInvestigation.getJSONObject(k).getInt("pti_id"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("main_test_id"),jsonArrayInvestigation.getJSONObject(k).getString("group_test_id"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("test_name"),jsonArrayInvestigation.getJSONObject(k).getString("normal_range"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("test_actual_value"),jsonArrayInvestigation.getJSONObject(k).getString("right_eye"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("left_eye"),jsonArrayInvestigation.getJSONObject(k).getInt("department"),
                                                            USER_ID, USER_LOGIN_TYPE));
                                                }
                                                else  if(jsonArrayInvestigation.getJSONObject(k).getInt("department") == 3) {
                                                    PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.add(new Investigations(jsonArrayInvestigation.getJSONObject(k).getInt("pti_id"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("main_test_id"),jsonArrayInvestigation.getJSONObject(k).getString("group_test_id"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("test_name"),jsonArrayInvestigation.getJSONObject(k).getString("normal_range"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("test_actual_value"),jsonArrayInvestigation.getJSONObject(k).getString("right_eye"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("left_eye"),jsonArrayInvestigation.getJSONObject(k).getInt("department"),
                                                            USER_ID, USER_LOGIN_TYPE));
                                                }
                                                else  if(jsonArrayInvestigation.getJSONObject(k).getInt("department") == 4) {
                                                    PATIENT_INVESTIGATION_CLINICAL_ARRAY.add(new Investigations(jsonArrayInvestigation.getJSONObject(k).getInt("pti_id"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("main_test_id"),jsonArrayInvestigation.getJSONObject(k).getString("group_test_id"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("test_name"),jsonArrayInvestigation.getJSONObject(k).getString("normal_range"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("test_actual_value"),jsonArrayInvestigation.getJSONObject(k).getString("right_eye"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("left_eye"),jsonArrayInvestigation.getJSONObject(k).getInt("department"),
                                                            USER_ID, USER_LOGIN_TYPE));
                                                }
                                                else  if(jsonArrayInvestigation.getJSONObject(k).getInt("department") == 5) {
                                                    PATIENT_INVESTIGATION_OTHER_ARRAY.add(new Investigations(jsonArrayInvestigation.getJSONObject(k).getInt("pti_id"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("main_test_id"),jsonArrayInvestigation.getJSONObject(k).getString("group_test_id"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("test_name"),jsonArrayInvestigation.getJSONObject(k).getString("normal_range"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("test_actual_value"),jsonArrayInvestigation.getJSONObject(k).getString("right_eye"),
                                                            jsonArrayInvestigation.getJSONObject(k).getString("left_eye"),jsonArrayInvestigation.getJSONObject(k).getInt("department"),
                                                            USER_ID, USER_LOGIN_TYPE));
                                                }
                                            }

                                            JSONArray jsonArrayDiagnosis = epObj.getJSONArray("diagnosis_result");
                                            for (int m = 0; m < jsonArrayDiagnosis.length(); m++) {
                                                PATIENT_DAIGNOSIS_ARRAY.add(new Diagnosis(jsonArrayDiagnosis.getJSONObject(m).getInt("diagnosis_autoid"),jsonArrayDiagnosis.getJSONObject(m).getInt("icd_id"),
                                                        jsonArrayDiagnosis.getJSONObject(m).getString("icd_code_name"),jsonArrayDiagnosis.getJSONObject(m).getInt("doc_id"),
                                                        jsonArrayDiagnosis.getJSONObject(m).getInt("doc_type"),0,USER_ID, USER_LOGIN_TYPE));
                                            }

                                            JSONArray jsonArrayTreatment = epObj.getJSONArray("treatment_result");
                                            for (int n = 0; n < jsonArrayTreatment.length(); n++) {
                                                PATIENT_TREATMENT_ARRAY.add(new Treatments(jsonArrayTreatment.getJSONObject(n).getInt("treatment_id"),
                                                        jsonArrayTreatment.getJSONObject(n).getString("treatment_name"),jsonArrayTreatment.getJSONObject(n).getInt("doc_id"),
                                                        jsonArrayTreatment.getJSONObject(n).getInt("doc_type"),0,USER_ID, USER_LOGIN_TYPE));
                                            }

                                            JSONArray jsonArrayPresc = epObj.getJSONArray("prescription_result");
                                            for (int p = 0; p < jsonArrayPresc.length(); p++) {
                                                Log.d(Utils.TAG, " prescID: "+ jsonArrayPresc.getJSONObject(p).getInt("episode_prescription_id"));
                                                PATIENT_PRESCRIPTION_ARRAY.add(new FrequentPrescription(jsonArrayPresc.getJSONObject(p).getInt("episode_prescription_id"),
                                                        jsonArrayPresc.getJSONObject(p).getInt("pp_id"), jsonArrayPresc.getJSONObject(p).getString("prescription_trade_name"),
                                                        0,jsonArrayPresc.getJSONObject(p).getString("prescription_generic_name"),jsonArrayPresc.getJSONObject(p).getString("prescription_frequency"),
                                                        jsonArrayPresc.getJSONObject(p).getString("timing"), jsonArrayPresc.getJSONObject(p).getString("duration"),
                                                        jsonArrayPresc.getJSONObject(p).getInt("doc_id"),1,0,USER_ID, USER_LOGIN_TYPE));
                                            }

                                            JSONArray jsonArrayLids = epObj.getJSONArray("lids_result");
                                            for (int a = 0; a < jsonArrayLids.length(); a++) {
                                                PATIENT_LIDS_ARRAY.add(new Lids(jsonArrayLids.getJSONObject(a).getInt("lids_id"),
                                                        jsonArrayLids.getJSONObject(a).getString("lids_name"),jsonArrayLids.getJSONObject(a).getInt("doc_id"),
                                                        jsonArrayLids.getJSONObject(a).getInt("doc_type"),jsonArrayLids.getJSONObject(a).getString("left_eye"),
                                                        jsonArrayLids.getJSONObject(a).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));
                                            }

                                            JSONArray jsonArrayConjuctiva = epObj.getJSONArray("conjuctiva_result");
                                            for (int b = 0; b < jsonArrayConjuctiva.length(); b++) {
                                                PATIENT_CONJUCTIVA_ARRAY.add(new OphthalConjuctiva(jsonArrayConjuctiva.getJSONObject(b).getInt("conjuctiva_id"),
                                                        jsonArrayConjuctiva.getJSONObject(b).getString("conjuctiva_name"),jsonArrayConjuctiva.getJSONObject(b).getInt("doc_id"),
                                                        jsonArrayConjuctiva.getJSONObject(b).getInt("doc_type"),jsonArrayConjuctiva.getJSONObject(b).getString("left_eye"),
                                                        jsonArrayConjuctiva.getJSONObject(b).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));
                                            }

                                            JSONArray jsonArraySclera = epObj.getJSONArray("sclera_result");
                                            for (int c = 0; c < jsonArraySclera.length(); c++) {
                                                PATIENT_SCLERA_ARRAY.add(new OphthalSclera(jsonArraySclera.getJSONObject(c).getInt("sclera_id"),
                                                        jsonArraySclera.getJSONObject(c).getString("scelra_name"),jsonArraySclera.getJSONObject(c).getInt("doc_id"),
                                                        jsonArraySclera.getJSONObject(c).getInt("doc_type"),jsonArraySclera.getJSONObject(c).getString("left_eye"),
                                                        jsonArraySclera.getJSONObject(c).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));
                                            }

                                            JSONArray jsonArrayCorneaAnt = epObj.getJSONArray("cornea_anterior_result");
                                            for (int d = 0; d < jsonArrayCorneaAnt.length(); d++) {
                                                PATIENT_CORNEA_ANTERIOR_ARRAY.add(new OphthalCornearAnteriorSurface(jsonArrayCorneaAnt.getJSONObject(d).getInt("cornea_ant_id"),
                                                        jsonArrayCorneaAnt.getJSONObject(d).getString("cornea_ant_name"),jsonArrayCorneaAnt.getJSONObject(d).getInt("doc_id"),
                                                        jsonArrayCorneaAnt.getJSONObject(d).getInt("doc_type"),jsonArrayCorneaAnt.getJSONObject(d).getString("left_eye"),
                                                        jsonArrayCorneaAnt.getJSONObject(d).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));
                                            }

                                            JSONArray jsonArrayCorneaPost = epObj.getJSONArray("cornea_posterior_result");
                                            for (int e = 0; e < jsonArrayCorneaPost.length(); e++) {
                                                PATIENT_CORNEA_POSTERIOR_ARRAY.add(new OphthalCornearPosteriorSurface(jsonArrayCorneaPost.getJSONObject(e).getInt("cornea_post_id"),
                                                        jsonArrayCorneaPost.getJSONObject(e).getString("cornea_post_name"),jsonArrayCorneaPost.getJSONObject(e).getInt("doc_id"),
                                                        jsonArrayCorneaPost.getJSONObject(e).getInt("doc_type"),jsonArrayCorneaPost.getJSONObject(e).getString("left_eye"),
                                                        jsonArrayCorneaPost.getJSONObject(e).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));
                                            }

                                            JSONArray jsonArrayAntChamber = epObj.getJSONArray("anterior_chamber_result");
                                            for (int f = 0; f < jsonArrayAntChamber.length(); f++) {
                                                PATIENT_ANTERIOR_CHAMBER_ARRAY.add(new OphthalAnteriorChamber(jsonArrayAntChamber.getJSONObject(f).getInt("chamber_id"),
                                                        jsonArrayAntChamber.getJSONObject(f).getString("chamber_name"),jsonArrayAntChamber.getJSONObject(f).getInt("doc_id"),
                                                        jsonArrayAntChamber.getJSONObject(f).getInt("doc_type"),jsonArrayAntChamber.getJSONObject(f).getString("left_eye"),
                                                        jsonArrayAntChamber.getJSONObject(f).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));
                                            }

                                            JSONArray jsonArrayIris = epObj.getJSONArray("iris_result");
                                            for (int g = 0; g < jsonArrayIris.length(); g++) {
                                                PATIENT_IRIS_ARRAY.add(new OphthalIris(jsonArrayIris.getJSONObject(g).getInt("iris_id"),
                                                        jsonArrayIris.getJSONObject(g).getString("iris_name"),jsonArrayIris.getJSONObject(g).getInt("doc_id"),
                                                        jsonArrayIris.getJSONObject(g).getInt("doc_type"),jsonArrayIris.getJSONObject(g).getString("left_eye"),
                                                        jsonArrayIris.getJSONObject(g).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));
                                            }

                                            JSONArray jsonArrayPupil = epObj.getJSONArray("pupil_result");
                                            for (int h = 0; h < jsonArrayPupil.length(); h++) {
                                                PATIENT_PUPIL_ARRAY.add(new OphthalPupil(jsonArrayPupil.getJSONObject(h).getInt("pupil_id"),
                                                        jsonArrayPupil.getJSONObject(h).getString("pupil_name"),jsonArrayPupil.getJSONObject(h).getInt("doc_id"),
                                                        jsonArrayPupil.getJSONObject(h).getInt("doc_type"),jsonArrayPupil.getJSONObject(h).getString("left_eye"),
                                                        jsonArrayPupil.getJSONObject(h).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));
                                            }

                                            JSONArray jsonArrayAngle = epObj.getJSONArray("angle_result");
                                            for (int q = 0; q < jsonArrayAngle.length(); q++) {
                                                PATIENT_ANGLE_ARRAY.add(new OphthalAngleAnteriorChamber(jsonArrayAngle.getJSONObject(q).getInt("angle_id"),
                                                        jsonArrayAngle.getJSONObject(q).getString("angle_name"),jsonArrayAngle.getJSONObject(q).getInt("doc_id"),
                                                        jsonArrayAngle.getJSONObject(q).getInt("doc_type"),jsonArrayAngle.getJSONObject(q).getString("left_eye"),
                                                        jsonArrayAngle.getJSONObject(q).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));
                                            }

                                            JSONArray jsonArrayLens = epObj.getJSONArray("lens_result");
                                            for (int r = 0; r < jsonArrayLens.length(); r++) {
                                                PATIENT_LENS_ARRAY.add(new OphthalLens(jsonArrayLens.getJSONObject(r).getInt("lens_id"),
                                                        jsonArrayLens.getJSONObject(r).getString("lens_name"),jsonArrayLens.getJSONObject(r).getInt("doc_id"),
                                                        jsonArrayLens.getJSONObject(r).getInt("doc_type"),jsonArrayLens.getJSONObject(r).getString("left_eye"),
                                                        jsonArrayLens.getJSONObject(r).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));
                                            }

                                            JSONArray jsonArrayViterous = epObj.getJSONArray("viterous_result");
                                            for (int s = 0; s < jsonArrayViterous.length(); s++) {
                                                PATIENT_VITEROUS_ARRAY.add(new OphthalViterous(jsonArrayViterous.getJSONObject(s).getInt("viterous_id"),
                                                        jsonArrayViterous.getJSONObject(s).getString("viterous_name"),jsonArrayViterous.getJSONObject(s).getInt("doc_id"),
                                                        jsonArrayViterous.getJSONObject(s).getInt("doc_type"),jsonArrayViterous.getJSONObject(s).getString("left_eye"),
                                                        jsonArrayViterous.getJSONObject(s).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));
                                            }

                                            JSONArray jsonArrayFundus = epObj.getJSONArray("fundus_result");
                                            for (int t = 0; t < jsonArrayFundus.length(); t++) {
                                                PATIENT_FUNDUS_ARRAY.add(new OphthalFundus(jsonArrayFundus.getJSONObject(t).getInt("fundus_id"),
                                                        jsonArrayFundus.getJSONObject(t).getString("fundus_name"),jsonArrayFundus.getJSONObject(t).getInt("doc_id"),
                                                        jsonArrayFundus.getJSONObject(t).getInt("doc_type"),jsonArrayFundus.getJSONObject(t).getString("left_eye"),
                                                        jsonArrayFundus.getJSONObject(t).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));
                                            }

                                            EPISODE_LIST.add(new OldVisitsOphthalList(epObj.getInt("episode_id"),epObj.getInt("admin_id"),
                                                    epObj.getInt("patient_id"),epObj.getString("next_followup_date"),epObj.getString("diagnosis_details"),
                                                    epObj.getString("treatment_details"),epObj.getString("prescription_note"),epObj.getString("date_time"),epObj.getString("consultation_fees"),
                                                    epObj.getInt("patient_education"),epObj.getString("episode_medical_complaint"),
                                                    PATIENT_CHIEF_MEDCOMPLAINT_ARRAY, PATIENT_INVESTIGATION_GENERAL_ARRAY, PATIENT_INVESTIGATION_OPHTHAL_ARRAY,
                                                    PATIENT_INVESTIGATION_RADIOLOGY_ARRAY, PATIENT_INVESTIGATION_CLINICAL_ARRAY, PATIENT_INVESTIGATION_OTHER_ARRAY,
                                                    PATIENT_DAIGNOSIS_ARRAY,PATIENT_TREATMENT_ARRAY,PATIENT_PRESCRIPTION_ARRAY, PATIENT_LIDS_ARRAY, PATIENT_CONJUCTIVA_ARRAY,
                                                    PATIENT_SCLERA_ARRAY, PATIENT_CORNEA_ANTERIOR_ARRAY, PATIENT_CORNEA_POSTERIOR_ARRAY, PATIENT_ANTERIOR_CHAMBER_ARRAY,
                                                    PATIENT_IRIS_ARRAY, PATIENT_PUPIL_ARRAY, PATIENT_ANGLE_ARRAY, PATIENT_LENS_ARRAY, PATIENT_VITEROUS_ARRAY, PATIENT_FUNDUS_ARRAY,
                                                    epObj.getString("refractionRE_value1"),epObj.getString("refractionRE_value2"),epObj.getString("refractionLE_value1"),
                                                    epObj.getString("refractionLE_value2"),epObj.getString("distVisionRE"),epObj.getString("distVisionLE"),
                                                    epObj.getString("nearVisionRE"),epObj.getString("nearVisionLE"),epObj.getString("DvSphereRE"),
                                                    epObj.getString("DvCylRE"),epObj.getString("DvAxisRE"),epObj.getString("DvSpeherLE"),
                                                    epObj.getString("DvCylLE"),epObj.getString("DvAxisLE"),epObj.getString("NvSpeherRE"),
                                                    epObj.getString("NvCylRE"),epObj.getString("NvAxisRE"),epObj.getString("NvSpeherLE"),
                                                    epObj.getString("NvCylLE"),epObj.getString("NvAxisLE"),epObj.getString("IpdRE"),
                                                    epObj.getString("IpdLE"),epObj.getString("distVisionUnaidedRE"),epObj.getString("distVisionUnaidedLE"),epObj.getString("nearVisionUnaidedRE"),
                                                    epObj.getString("nearVisionUnaidedLE"),epObj.getString("iopRE"),epObj.getString("iopLE")));

                                            EPISODE_LIST_TEMP.add(new OldVisitsOphthalList(epObj.getInt("episode_id"),epObj.getInt("admin_id"),
                                                    epObj.getInt("patient_id"),epObj.getString("next_followup_date"),epObj.getString("diagnosis_details"),
                                                    epObj.getString("treatment_details"),epObj.getString("prescription_note"),epObj.getString("date_time"),epObj.getString("consultation_fees"),
                                                    epObj.getInt("patient_education"),epObj.getString("episode_medical_complaint"),
                                                    PATIENT_CHIEF_MEDCOMPLAINT_ARRAY, PATIENT_INVESTIGATION_GENERAL_ARRAY, PATIENT_INVESTIGATION_OPHTHAL_ARRAY,
                                                    PATIENT_INVESTIGATION_RADIOLOGY_ARRAY, PATIENT_INVESTIGATION_CLINICAL_ARRAY, PATIENT_INVESTIGATION_OTHER_ARRAY,
                                                    PATIENT_DAIGNOSIS_ARRAY,PATIENT_TREATMENT_ARRAY,PATIENT_PRESCRIPTION_ARRAY, PATIENT_LIDS_ARRAY, PATIENT_CONJUCTIVA_ARRAY,
                                                    PATIENT_SCLERA_ARRAY, PATIENT_CORNEA_ANTERIOR_ARRAY, PATIENT_CORNEA_POSTERIOR_ARRAY, PATIENT_ANTERIOR_CHAMBER_ARRAY,
                                                    PATIENT_IRIS_ARRAY, PATIENT_PUPIL_ARRAY, PATIENT_ANGLE_ARRAY, PATIENT_LENS_ARRAY, PATIENT_VITEROUS_ARRAY, PATIENT_FUNDUS_ARRAY,
                                                    epObj.getString("refractionRE_value1"),epObj.getString("refractionRE_value2"),epObj.getString("refractionLE_value1"),
                                                    epObj.getString("refractionLE_value2"),epObj.getString("distVisionRE"),epObj.getString("distVisionLE"),
                                                    epObj.getString("nearVisionRE"),epObj.getString("nearVisionLE"),epObj.getString("DvSphereRE"),
                                                    epObj.getString("DvCylRE"),epObj.getString("DvAxisRE"),epObj.getString("DvSpeherLE"),
                                                    epObj.getString("DvCylLE"),epObj.getString("DvAxisLE"),epObj.getString("NvSpeherRE"),
                                                    epObj.getString("NvCylRE"),epObj.getString("NvAxisRE"),epObj.getString("NvSpeherLE"),
                                                    epObj.getString("NvCylLE"),epObj.getString("NvAxisLE"),epObj.getString("IpdRE"),
                                                    epObj.getString("IpdLE"),epObj.getString("distVisionUnaidedRE"),epObj.getString("distVisionUnaidedLE"),epObj.getString("nearVisionUnaidedRE"),
                                                    epObj.getString("nearVisionUnaidedLE"),epObj.getString("iopRE"),epObj.getString("iopLE")));
                                        }

                                        String json_data = gson.toJson(EPISODE_LIST_TEMP);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearsetMyPatientEpisodes();
                                            shareadPreferenceClass.setMyPatientEpisodes(json_data);
                                        }

                                        episodeAdapter = new OldVisitOphthalListViewAdapter(getActivity(), EPISODE_LIST);
                                        episodeRecyclerView.setAdapter(episodeAdapter);
                                        episodeAdapter.notifyDataSetChanged();

                                    }
                                    else {
                                        episodeRecyclerView.setVisibility(View.GONE);
                                        no_data.setVisibility(View.VISIBLE);
                                    }
                                }
                                dialog2.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                dialog2.dismiss();
                            }
                        } else {
                            dialog2.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog2.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                map.put(APIClass.KEY_PATIENTID, String.valueOf(PATIENT_ID));
                map.put(APIClass.KEY_PATIENT_NAME, PATIENT_NAME);
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(getContext()).
                getRequestQueue();
        AppController.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

}
