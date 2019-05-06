package com.medisensehealth.fdccontributor.activities.patients;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medisensehealth.fdccontributor.DataModel.PatientsList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.adapter.patients.PatientsAdapter;
import com.medisensehealth.fdccontributor.database.MedisensePracticeDB;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewItalic;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 24-03-2018.
 */

public class PatientsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView _header_filter, _header_create;
    LinearLayout _header_sub_search_layout;
    CustomTextView _header_sub_text;

    SwipyRefreshLayout swipeRefreshLayout_patient;
    RecyclerView recyclerView_patient;
    CustomTextViewItalic _text_norefer;

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID, HOSPITAL_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE, BLOG_LIST, CASES_COUNT;
    List<PatientsList> PatientsListArraylist;
    PatientsAdapter adapter;
    JSONObject jsonObject;
    PatientsList patients;
    AsyncPatientsList asynPatDetails;
    AsyncPatientsListUpdate asynPatdetailsUpdate;
    ImageButton more_listdata;
    String MODE_OF_OPERATION;
    int notifyCount;
    int start = 0;
    String change_filter_result = "";
    int FILTER_RESULT;
    ProgressDialog progressDialog;
    View footertabs;
    ImageView refresh_fab;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_patients);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);
        }

        shareadPreferenceClass = new ShareadPreferenceClass(PatientsActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(PatientsActivity.this);

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

        Log.d(Utils.TAG, " ************ Patients Activity ************ ");
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
        FILTER_RESULT = 0;
        // Header Tabs
        _header_filter = (ImageView) findViewById(R.id.header_sub_filter);
        _header_filter.setOnClickListener(this);
        _header_create = (ImageView) findViewById(R.id.header_sub_create);
        _header_create.setOnClickListener(this);
        _header_create.setVisibility(View.GONE);
        _header_sub_search_layout = (LinearLayout) findViewById(R.id.header_sub_layout);
        _header_sub_search_layout.setOnClickListener(this);

        PatientsListArraylist = new ArrayList<>();
        recyclerView_patient = (RecyclerView) findViewById(R.id.patient_RecyclerView);
        _text_norefer = (CustomTextViewItalic) findViewById(R.id.patientlist_norefer);
        swipeRefreshLayout_patient = (SwipyRefreshLayout) findViewById(R.id.patient_swipeRefreshLayout);
        refresh_fab = (ImageView) findViewById(R.id.patientlist_refresh);
        refresh_fab.setOnClickListener(this);

        if (NetworkUtil.getConnectivityStatusString(PatientsActivity.this).equalsIgnoreCase("enabled")) {
            MODE_OF_OPERATION = "normal";
            requestServerdata(MODE_OF_OPERATION);
        } else {
            AppUtils.showCustomAlertMessage(PatientsActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
        }

        adapter = new PatientsAdapter(PatientsActivity.this,PatientsListArraylist);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PatientsActivity.this);
        recyclerView_patient.setLayoutManager(mLayoutManager);
        recyclerView_patient.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout_patient.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                Log.d(Utils.TAG, "Refresh triggered at "+ (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
                if(direction == SwipyRefreshLayoutDirection.TOP) {
                    //    Toast.makeText(getActivity(),direction.toString(),Toast.LENGTH_SHORT).show();
                    MODE_OF_OPERATION = "pulltorefresh";
                    refreshItems();
                }
                else  if(direction == SwipyRefreshLayoutDirection.BOTTOM) {
                    //   Toast.makeText(getActivity(),direction.toString(),Toast.LENGTH_SHORT).show();
                    MODE_OF_OPERATION = "fetchalldata";
                    requestServerdata(MODE_OF_OPERATION);
                    onItemsLoadComplete();
                }

            }
        });

    }

    void refreshItems() {
//        ShowCountAsyncTask myTask = new ShowCountAsyncTask(getActivity());
//        myTask.execute();
        requestServerdata(MODE_OF_OPERATION);
        onItemsLoadComplete();
    }
    void onItemsLoadComplete() {
        swipeRefreshLayout_patient.setRefreshing(false);
    }

    private void requestServerdata(String mode) {
        PatientsListArraylist = new ArrayList<>();
        if (mode.equals("normal")) {
            // get all patient list from database
            List<PatientsList> patientCheck = MedisensePracticeDB.getAllPatients(PatientsActivity.this,Utils.USER_LOGIN_TYPE, USER_ID );
            if (patientCheck.size() > 0) {
                Log.d(Utils.TAG, "Database is already exists - normal mode");
                for (int i = 0; i < patientCheck.size(); i++) {
                    preparePatientsDataDisplay(patientCheck.get(i).getPatientId(), patientCheck.get(i).getPatientName(),
                            patientCheck.get(i).getPatientAge(), patientCheck.get(i).getPatientCity(), patientCheck.get(i).getPatientStatus(), patientCheck.get(i).getPatientDocName(), patientCheck.get(i).getPatientDocId(),  patientCheck.get(i).getPatientReferBy(),USER_ID, patientCheck.get(i).getPatientStatusTime());
                }

            } else {
                Log.d(Utils.TAG, "No entries in the database - normal mode");
                if (NetworkUtil.getConnectivityStatusString(PatientsActivity.this).equalsIgnoreCase("enabled")) {
                    addPatientstoDatabasefromServer(mode, FILTER_RESULT);
                } else {
                    AppUtils.showCustomAlertMessage(PatientsActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }

            }
        } else if (mode.equals("pulltorefresh")) {
            List<PatientsList>  patientCheck = MedisensePracticeDB.getAllPatients(PatientsActivity.this,Utils.USER_LOGIN_TYPE,USER_ID);
            if (patientCheck.size() > 0) {
                Log.d(Utils.TAG, "  PL Database is already exists - pull to refresh mode");
                if (NetworkUtil.getConnectivityStatusString(PatientsActivity.this).equalsIgnoreCase("enabled")) {
                    addPatientstoDatabasefromServer(mode, FILTER_RESULT);
                } else {
                    AppUtils.showCustomAlertMessage(PatientsActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }

            } else {
                Log.d(Utils.TAG, " PL No entries in the database - pull to refresh mode");
                if (NetworkUtil.getConnectivityStatusString(PatientsActivity.this).equalsIgnoreCase("enabled")) {
                    addPatientstoDatabasefromServer(mode, FILTER_RESULT);
                } else {
                    AppUtils.showCustomAlertMessage(PatientsActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
            }
        } else if (mode.equals("fetchalldata")) {
            if (NetworkUtil.getConnectivityStatusString(PatientsActivity.this).equalsIgnoreCase("enabled")) {
                //  loadallDatabasefromServer(mode);
            } else {
                AppUtils.showCustomAlertMessage(PatientsActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }

        }
    }

    private void addPatientstoDatabasefromServer(final String mode, final int Filter_status) {
        progressDialog = new ProgressDialog(PatientsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Patients...");
        progressDialog.show();

        PatientsListArraylist = new ArrayList<>();
        adapter = new PatientsAdapter(PatientsActivity.this, PatientsListArraylist);
        // Set the adapter for RecyclerView
        recyclerView_patient.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PATIENT_LIST_PREM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, response.toString());
                        if (response != null) {
                            GetJsonArrayResponse(response, mode, Filter_status);
                        }else {
                            progressDialog.dismiss();
                            AppUtils.showCustomErrorMessage(PatientsActivity.this, "Patient Records","No results found !!", "OK", null, null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //  AppUtil.showCustomAlert(getContext(), "Server Error!!!","You Can Try it later", "OK", null);
                        List<PatientsList> patientCheck = MedisensePracticeDB.getAllPatients(PatientsActivity.this,Utils.USER_LOGIN_TYPE, USER_ID );
                        if (patientCheck.size() > 0) {
                            Log.d(Utils.TAG, "Database is already exists - normal mode");
                            for (int i = 0; i < patientCheck.size(); i++) {
                                preparePatientsDataDisplay(patientCheck.get(i).getPatientId(), patientCheck.get(i).getPatientName(),
                                        patientCheck.get(i).getPatientAge(), patientCheck.get(i).getPatientCity(), patientCheck.get(i).getPatientStatus(), patientCheck.get(i).getPatientDocName(), patientCheck.get(i).getPatientDocId(),  patientCheck.get(i).getPatientReferBy(),USER_ID, patientCheck.get(i).getPatientStatusTime());
                            }

                        } /*else {
                        Log.d(Utils.TAG, "No entries in the database - normal mode");
                        if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
                            addPatientstoDatabasefromServer(mode);
                        } else {
                            AppUtil.showCustomAlert(getActivity(), HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null);
                        }

                    }*/
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(Utils.USER_LOGIN_ID));
                map.put(APIClass.KEY_LOGINTYPE, Utils.USER_LOGIN_TYPE);
                map.put(APIClass.KEY_PATIENT_FILTER, String.valueOf(Filter_status));
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = AppController.getInstance(PatientsActivity.this).
                getRequestQueue();
        AppController.getInstance(PatientsActivity.this).addToRequestQueue(stringRequest);
    }

    private void GetJsonArrayResponse(String response, String mode, int filter_status) {

        try {
            PatientsListArraylist = new ArrayList<>();
            jsonObject = new JSONObject(response);
            // Log.d(Utils.TAG, response.toString());
            String staus_res = jsonObject.getString("status");

            if (staus_res.equals("false")) {
                AppUtils.showCustomErrorMessage(PatientsActivity.this, "Patient Records","No results found !!", "OK", null, null);
            } else {
                GetallPatientRecords(jsonObject, mode, filter_status);
            }

            progressDialog.dismiss();
        } catch (Exception e) {
            progressDialog.dismiss();
            // AppUtil.showCustomAlert(getContext(), "Server Error!!!","Unable to reach Server", "OK", null);
            e.printStackTrace();
        }
    }

    private void preparePatientsDataDisplay(int patientId, String patientName, String patientAge, String patientCity, int patientStatus, String patientDocName, int patientDocId, String patientReferBy, int loginUserid, String statusTime) {
        PatientsList patient = new PatientsList(patientId,patientName,patientAge,patientCity,patientStatus,patientDocName,patientDocId,Utils.USER_LOGIN_TYPE,patientReferBy,loginUserid,statusTime);
        PatientsListArraylist.add(patient);
        adapter = new PatientsAdapter(PatientsActivity.this, PatientsListArraylist);
        // Set the adapter for RecyclerView
        recyclerView_patient.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void GetallPatientRecords(JSONObject jsonObject, String mode, int filter_status) {

        JSONArray jsonArray = null;
        String status = null;
        try {
            jsonArray = this.jsonObject.getJSONArray("patient_details");
            Log.d(Utils.TAG, String.valueOf(jsonArray.length()));
            if (jsonArray.length() > 0) {
                recyclerView_patient.setVisibility(View.VISIBLE);
                _text_norefer.setVisibility(View.GONE);
                for (int i = 0; i < jsonArray.length(); i++) {

                    patients = new PatientsList(jsonArray.getJSONObject(i).getInt("pat_id"), jsonArray.getJSONObject(i).getString("pat_name"),
                            jsonArray.getJSONObject(i).getString("pat_age"), jsonArray.getJSONObject(i).getString("pat_loc"), jsonArray.getJSONObject(i).getInt("pat_status"),
                            jsonArray.getJSONObject(i).getString("pat_doc_name"),jsonArray.getJSONObject(i).getInt("pat_doc_id"),Utils.USER_LOGIN_TYPE,jsonArray.getJSONObject(i).getString("pat_refered_by"),USER_ID, jsonArray.getJSONObject(i).getString("pat_status_time"));
                    PatientsListArraylist.add(patients);
                }

                adapter = new PatientsAdapter(PatientsActivity.this, PatientsListArraylist);
                recyclerView_patient.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                //  if(PatientsListArraylist.size() > 0) {

                if(mode.equals("normal")) {
                    List<PatientsList>  patientCheck = MedisensePracticeDB.getAllPatients(PatientsActivity.this,Utils.USER_LOGIN_TYPE,USER_ID);
                    if(patientCheck.size() > 0) {
                        asynPatdetailsUpdate = new AsyncPatientsListUpdate();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            asynPatdetailsUpdate.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, PatientsListArraylist);
                        } else {
                            asynPatdetailsUpdate.execute(PatientsListArraylist);
                        }
                    }
                    else {
                        asynPatDetails = new AsyncPatientsList();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            asynPatDetails.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, PatientsListArraylist);
                        } else {
                            asynPatDetails.execute(PatientsListArraylist);
                        }
                    }
                }
                else if(mode.equals("pulltorefresh")) {
                    List<PatientsList>  patientCheck = MedisensePracticeDB.getAllPatients(PatientsActivity.this,Utils.USER_LOGIN_TYPE,USER_ID);
                    if(patientCheck.size() > 0) {
                        asynPatdetailsUpdate = new AsyncPatientsListUpdate();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            asynPatdetailsUpdate.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, PatientsListArraylist);
                        } else {
                            asynPatdetailsUpdate.execute(PatientsListArraylist);
                        }
                    }
                    else {
                        asynPatDetails = new AsyncPatientsList();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            asynPatDetails.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, PatientsListArraylist);
                        } else {
                            asynPatDetails.execute(PatientsListArraylist);
                        }
                    }
                }
                else if(mode.equals("fetchalldata")) {
                    Log.d(Utils.TAG , "Database is already exists - "+mode.toString());
                }
                //  }
            }
            else {
                recyclerView_patient.setVisibility(View.GONE);
                _text_norefer.setVisibility(View.VISIBLE);

                PatientsListArraylist.clear();
                PatientsListArraylist = new ArrayList<>();
                asynPatdetailsUpdate = new AsyncPatientsListUpdate();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    asynPatdetailsUpdate.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, PatientsListArraylist);
                } else {
                    asynPatdetailsUpdate.execute(PatientsListArraylist);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class AsyncPatientsList extends AsyncTask<Object, Object, List<PatientsList>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(Utils.TAG, "PRE EXECUTE");
        }

        @Override
        protected List<PatientsList> doInBackground(Object... params) {
            Log.d(Utils.TAG, "DO IN BACKGROUND");

            if (PatientsListArraylist.size() > 0) {
                for (int i = 0; i < PatientsListArraylist.size(); i++) {
                    Log.d(Utils.TAG + "id: ", String.valueOf(PatientsListArraylist.get(i).getPatientId()));
                    Log.d(Utils.TAG + "Name: ", PatientsListArraylist.get(i).getPatientName());
                    Log.d(Utils.TAG + "Age: ", PatientsListArraylist.get(i).getPatientAge());
                    Log.d(Utils.TAG + "Location: ", PatientsListArraylist.get(i).getPatientCity());
                    Log.d(Utils.TAG + "Status: ", String.valueOf(PatientsListArraylist.get(i).getPatientStatus()));
                    Log.d(Utils.TAG + "Doc Name: ", String.valueOf(PatientsListArraylist.get(i).getPatientDocName()));
                    Log.d(Utils.TAG + "Doc Id: ", String.valueOf(PatientsListArraylist.get(i).getPatientDocId()));

                    long res_add = MedisensePracticeDB.patientInsert(PatientsActivity.this, PatientsListArraylist.get(i).getPatientId(), PatientsListArraylist.get(i).getPatientName(),
                            PatientsListArraylist.get(i).getPatientAge(),PatientsListArraylist.get(i).getPatientCity(),PatientsListArraylist.get(i).getPatientStatus(),
                            PatientsListArraylist.get(i).getPatientDocName(),PatientsListArraylist.get(i).getPatientDocId(), Utils.USER_LOGIN_TYPE,PatientsListArraylist.get(i).getPatientReferBy(),
                            USER_ID,PatientsListArraylist.get(i).getPatientStatusTime());

                }

            }
            else {
                MedisensePracticeDB.clearPatientEntries(PatientsActivity.this,USER_ID, USER_LOGIN_TYPE);
            }
            return PatientsListArraylist;
        }

        @Override
        protected void onPostExecute(List<PatientsList> PatientsLists) {
            super.onPostExecute(PatientsLists);

            Log.d(Utils.TAG, "IN POST EXECUTE");
            if (PatientsListArraylist.size() > 0) {
                List<PatientsList>  patLists = MedisensePracticeDB.getAllPatients(PatientsActivity.this,Utils.USER_LOGIN_TYPE,USER_ID);
                Log.d(Utils.TAG +"Size", String.valueOf(patLists.size()));
                if(patLists.size() > 0) {
                    Log.d(Utils.TAG ," > 0");
                    for(int i=0;i<patLists.size();i++) {
                        Log.d(Utils.TAG + "frm db", String.valueOf(patLists.get(i).getPatientId()));
                        Log.d(Utils.TAG+ "frm db", patLists.get(i).getPatientName());
                        Log.d(Utils.TAG+ "frm db", patLists.get(i).getPatientAge());
                        Log.d(Utils.TAG+ "frm db", patLists.get(i).getPatientCity());
                        Log.d(Utils.TAG+ "frm db", String.valueOf(patLists.get(i).getPatientStatus()));
                        Log.d(Utils.TAG+ "frm db", patLists.get(i).getPatientLoginType());
                    }
                }
                else {
                    Log.d(Utils.TAG, "< 0");
                }

            }
        }
    }

    private class AsyncPatientsListUpdate extends AsyncTask<Object, Object, List<PatientsList>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  dialog.dismiss();

            Log.d(Utils.TAG, "PRE EXECUTE AsyncPatientsListUpdate");

        }


        @Override
        protected List<PatientsList> doInBackground(Object... params) {
            Log.d(Utils.TAG, "DO IN BACKGROUND AsyncPatientsListUpdate");

            //  int delete_res =   patientDataBaseAdapter.deleteEntry(USER_ID,USER_LOGIN_TYPE);
            MedisensePracticeDB.clearPatientEntries(PatientsActivity.this,USER_ID,USER_LOGIN_TYPE);

            if (PatientsListArraylist.size() > 0) {
                for (int i = 0; i < PatientsListArraylist.size(); i++) {
                    long res_add = MedisensePracticeDB.patientInsert(PatientsActivity.this, PatientsListArraylist.get(i).getPatientId(), PatientsListArraylist.get(i).getPatientName(),
                            PatientsListArraylist.get(i).getPatientAge(),PatientsListArraylist.get(i).getPatientCity(),PatientsListArraylist.get(i).getPatientStatus(),
                            PatientsListArraylist.get(i).getPatientDocName(),PatientsListArraylist.get(i).getPatientDocId(), Utils.USER_LOGIN_TYPE,PatientsListArraylist.get(i).getPatientReferBy(),
                            USER_ID,PatientsListArraylist.get(i).getPatientStatusTime());
                }
            }
            else {
                MedisensePracticeDB.clearPatientEntries(PatientsActivity.this,USER_ID, USER_LOGIN_TYPE);
            }
            return PatientsListArraylist;
        }

        @Override
        protected void onPostExecute(List<PatientsList> PatientsLists) {
            super.onPostExecute(PatientsLists);
            Log.d(Utils.TAG, "IN POST EXECUTE AsyncPatientsListUpdate");
            if (PatientsListArraylist.size() > 0) {
                List<PatientsList>  patLists = MedisensePracticeDB.getAllPatients(PatientsActivity.this,Utils.USER_LOGIN_TYPE,USER_ID);
                Log.d(Utils.TAG +"Size", String.valueOf(patLists.size()));
                if(patLists.size() > 0) {
                    Log.d(Utils.TAG ," > 0");
                }
                else {
                    Log.d(Utils.TAG, "< 0");
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_sub_filter:
                showFilterContents();
                break;
            case R.id.header_sub_create:
               /* if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("PATIENT_ID", 0);
                    bundle.putString("PATIENT_VIEW","ADD");
                    NewPatientCreateFragment pcreateFragment = new NewPatientCreateFragment();
                    fragTransaction = fragManager.beginTransaction();
                    pcreateFragment.setArguments(bundle);
                    fragTransaction.replace(R.id.dashboard_fragment,pcreateFragment,"PATIENT_CREATE");
                    fragTransaction.addToBackStack("PATIENT_CREATE");
                    fragTransaction.commit();
                } else {
                    AppUtils.showCustomAlertMessage(getActivity(), HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }*/
                break;
            case R.id.header_sub_layout:
                Intent i2 = new Intent(PatientsActivity.this, SearchPatientsActivity.class);
                i2.putExtra("title","Search Cases Received");
                startActivity(i2);
                break;
            case R.id.patientlist_refresh:
                MODE_OF_OPERATION = "pulltorefresh";
                requestServerdata(MODE_OF_OPERATION);
                break;
        }
    }

    private void showFilterContents() {
        //   final CharSequence str[] = {"All","New","Sent","Awaiting", "Not Qualified","Responded","Response P-Failed","Staged","OP Desired","IP Treated","Not Converted","Invoiced","Payment Received","OP Visited","Not Responded" };
        final CharSequence str[] = {"All","New","Pending", "Responded","Staged","OP Desired","IP Treated","OP Visited","Not Responded" };
        final AlertDialog.Builder builder = new AlertDialog.Builder(PatientsActivity.this, R.style.CustomDialog);
        builder.setTitle("Filter By Status");
        int position = -1;
        Dialog dialog = builder.create();

        builder.setSingleChoiceItems(str, position, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //  Toast.makeText(getActivity(), String.valueOf(str[whichButton]), Toast.LENGTH_SHORT).show();
                change_filter_result = String.valueOf(str[whichButton]);
                if (change_filter_result.equals("")) {
                    Toast.makeText(PatientsActivity.this, "Choose filter to update !!!", Toast.LENGTH_SHORT).show();
                } else {

                    if(change_filter_result.equalsIgnoreCase("New")) {
                        FILTER_RESULT = 1;
                        addPatientstoDatabasefromServer("normal", 1);
                    }
                    else if(change_filter_result.equalsIgnoreCase("Pending")) {
                        FILTER_RESULT = 2;
                        addPatientstoDatabasefromServer("normal", 2);
                    }
                    else if(change_filter_result.equalsIgnoreCase("Awaiting")) {
                        FILTER_RESULT = 3;
                        addPatientstoDatabasefromServer("normal", 3);
                    }
                    else if(change_filter_result.equalsIgnoreCase("Not Qualified")) {
                        FILTER_RESULT = 4;
                        addPatientstoDatabasefromServer("normal", 4);
                    }
                    else if(change_filter_result.equalsIgnoreCase("Responded")) {
                        FILTER_RESULT = 5;
                        addPatientstoDatabasefromServer("normal", 5);
                    }
                    else if(change_filter_result.equalsIgnoreCase("Response P-Failed")) {
                        FILTER_RESULT = 6;
                        addPatientstoDatabasefromServer("normal", 6);
                    }
                    else if(change_filter_result.equalsIgnoreCase("Staged")) {
                        FILTER_RESULT = 7;
                        addPatientstoDatabasefromServer("normal", 7);
                    }
                    else if(change_filter_result.equalsIgnoreCase("OP Desired")) {
                        FILTER_RESULT = 8;
                        addPatientstoDatabasefromServer("normal", 8);
                    }
                    else if(change_filter_result.equalsIgnoreCase("IP Treated")) {
                        FILTER_RESULT = 9;
                        addPatientstoDatabasefromServer("normal", 9);
                    }
                    else if(change_filter_result.equalsIgnoreCase("Not Converted")) {
                        FILTER_RESULT = 10;
                        addPatientstoDatabasefromServer("normal", 10);
                    }
                    else if(change_filter_result.equalsIgnoreCase("Invoiced")) {
                        FILTER_RESULT = 11;
                        addPatientstoDatabasefromServer("normal", 11);
                    }
                    else if(change_filter_result.equalsIgnoreCase("Payment Received")) {
                        FILTER_RESULT = 12;
                        addPatientstoDatabasefromServer("normal", 12);
                    }
                    else if(change_filter_result.equalsIgnoreCase("OP Visited")) {
                        FILTER_RESULT = 13;
                        addPatientstoDatabasefromServer("normal", 13);
                    }
                    else if(change_filter_result.equalsIgnoreCase("Not Responded")) {
                        FILTER_RESULT = 14;
                        addPatientstoDatabasefromServer("normal", 14);
                    }
                    else if(change_filter_result.equalsIgnoreCase("All")) {
                        FILTER_RESULT = 0;
                        addPatientstoDatabasefromServer("normal", 0);
                    }
                    else {
                        FILTER_RESULT = 0;
                        addPatientstoDatabasefromServer("normal", 0);
                    }
                    dialog.cancel();
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
