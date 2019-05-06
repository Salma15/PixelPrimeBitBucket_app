package com.medisensehealth.fdccontributor.activities.patients;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medisensehealth.fdccontributor.DataModel.DoctorList;
import com.medisensehealth.fdccontributor.DataModel.HospitalList;
import com.medisensehealth.fdccontributor.DataModel.SpecializationList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.adapter.doctors.CustomHospitalFilterAdapter;
import com.medisensehealth.fdccontributor.adapter.doctors.CustomSpecFilterAdapter;
import com.medisensehealth.fdccontributor.adapter.patients.SendCaseAdapter;
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
import com.medisensehealth.fdccontributor.views.CustomTextViewSemiBold;
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

public class SendCaseActivity extends AppCompatActivity implements View.OnClickListener {
    int PATIENT_ID, DOCREF_ID;
    String DOCTOR_TYPE;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID, HOSPITAL_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE;

    ImageView _header_filter, _header_create;
    LinearLayout _header_sub_search_layout;
    CustomTextView _header_sub_text;

    CoordinatorLayout cardiLayout;
    SwipyRefreshLayout swipeRefreshLayout_doctorlist;
    RecyclerView recyclerView_doctorlist;
    ImageView filter_doctors;
    CustomTextViewItalic _text_noResults;

    ProgressDialog progressDialog;
    List<DoctorList> doctorListArraylist;
    SendCaseAdapter adapter;
    int DOCTOR_FAVOURITE;
    String MODE_OF_OPERATION;
    DoctorList doctors;
    AsyncDoctorListInsert asynDocDetailInsert;
    List<SpecializationList> specializationListArraylist;
    SpecializationList specialization;
    AsyncSpecializationListUpdate asynSpecDetailUpdate;
    int SPEC_ID = 0;
    List<HospitalList> hospitalListArraylist;
    HospitalList hospitals;
    AsyncHospitalListInsert asynHospDetailInsert;
    Button loadMOre_btn;
    String first_entry = "0";
    ImageView refresh_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_doctors);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle b = getIntent().getExtras();
        if( b != null) {
            String title = b.getString("title");
            setTitle(title);

            PATIENT_ID = b.getInt("PATIENT_ID", 0);
            DOCTOR_TYPE = b.getString("DOCTOR_TYPE", "1");
            DOCREF_ID  = b.getInt("DOCREF_ID", 0);

            Log.d(Utils.TAG, "********* Send case **************");
            Log.d(Utils.TAG + " PATIENT_ID:", String.valueOf(PATIENT_ID));
            Log.d(Utils.TAG + " DOCREF_ID:", String.valueOf(DOCREF_ID));
            Log.d(Utils.TAG + " DOCTOR_TYPE:", DOCTOR_TYPE);
        }

        shareadPreferenceClass = new ShareadPreferenceClass(SendCaseActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(SendCaseActivity.this);

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
        DOCTOR_FAVOURITE = 0;
        specializationListArraylist = new ArrayList<>();
        hospitalListArraylist = new ArrayList<>();
        doctorListArraylist = new ArrayList<>();

        // Header Tabs
        _header_filter = (ImageView) findViewById(R.id.header_sub_filter);
        _header_filter.setOnClickListener(this);
        _header_create = (ImageView) findViewById(R.id.header_sub_create);
        _header_create.setOnClickListener(this);
        _header_create.setVisibility(View.GONE);
        _header_sub_search_layout = (LinearLayout) findViewById(R.id.header_sub_layout);
        _header_sub_search_layout.setOnClickListener(this);
        _header_sub_text = (CustomTextView)  findViewById(R.id.header_sub_searchtext);
        _header_sub_text.setText("Search Doctor Here...");

        swipeRefreshLayout_doctorlist = (SwipyRefreshLayout) findViewById(R.id.doctorlist_swipeRefreshLayout);
        recyclerView_doctorlist = (RecyclerView) findViewById(R.id.doctorlist_RecyclerView);
        _text_noResults = (CustomTextViewItalic) findViewById(R.id.doctorlist_norefer);

        loadMOre_btn = (Button) findViewById(R.id.doclist_viewall);
        loadMOre_btn.setVisibility(View.GONE);

        recyclerView_doctorlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    refresh_fab.setVisibility(View.GONE);
                    //  Toast.makeText(getActivity()," Last position reached !!!",Toast.LENGTH_SHORT).show();
                    if(first_entry.equals("0")) {
                        loadMOre_btn.setVisibility(View.VISIBLE);
                    }
                    else loadMOre_btn.setVisibility(View.GONE);
                }
                else {
                    refresh_fab.setVisibility(View.VISIBLE);
                    loadMOre_btn.setVisibility(View.GONE);
                }
            }
        });

        loadMOre_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first_entry = "1";
                loadMOre_btn.setVisibility(View.GONE);
                MODE_OF_OPERATION = "fetchalldata";
                requestServerdata(MODE_OF_OPERATION);
            }
        });


        swipeRefreshLayout_doctorlist.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                //    Log.d(Utils.TAG, "Refresh triggered at "+ (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
                if(direction == SwipyRefreshLayoutDirection.TOP) {
                    MODE_OF_OPERATION = "pulltorefresh";
                    refreshItems(MODE_OF_OPERATION);
                }
                else  if(direction == SwipyRefreshLayoutDirection.BOTTOM) {
                    MODE_OF_OPERATION = "fetchalldata";
                    requestServerdata(MODE_OF_OPERATION);
                    onItemsLoadComplete();
                }

            }
        });

        if (NetworkUtil.getConnectivityStatusString(SendCaseActivity.this).equalsIgnoreCase("enabled")) {
            MODE_OF_OPERATION = "normal";
            requestServerdata(MODE_OF_OPERATION);
        } else {
            AppUtils.showCustomAlertMessage(SendCaseActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
        }

        adapter = new SendCaseAdapter(SendCaseActivity.this,doctorListArraylist, PATIENT_ID, DOCREF_ID);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SendCaseActivity.this);
        recyclerView_doctorlist.setLayoutManager(mLayoutManager);
        recyclerView_doctorlist.setItemAnimator(new DefaultItemAnimator());
        refresh_fab = (ImageView) findViewById(R.id.doclist_refresh);
        refresh_fab.setOnClickListener(this);

        recyclerView_doctorlist.setHasFixedSize(true);
        recyclerView_doctorlist.setItemViewCacheSize(20);
        recyclerView_doctorlist.setDrawingCacheEnabled(true);
        recyclerView_doctorlist.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

    }

    private void getSpecializationDetails() {
        List<SpecializationList> specializationCheck = MedisensePracticeDB.getAllSpecialization(SendCaseActivity.this);
        Log.d(Utils.TAG, "size:"+ String.valueOf(specializationCheck.size()));
        if (specializationCheck.size() > 0) {
            Log.d(Utils.TAG, "Specialization already exists in db");
            for (int i = 0; i < specializationCheck.size(); i++) {
                specialization = new SpecializationList(specializationCheck.get(i).getSpecializationId(), specializationCheck.get(i).getSpecializationName(), USER_ID, USER_LOGIN_TYPE);
                specializationListArraylist.add(specialization);
            }
        } else {
            Log.d(Utils.TAG, "specialization no entries");
            if (NetworkUtil.getConnectivityStatusString(SendCaseActivity.this).equalsIgnoreCase("enabled")) {
                addSpecializationDetailsfromServer();
            } else {
                AppUtils.showCustomAlertMessage(SendCaseActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }

        }
    }

    private void addSpecializationDetailsfromServer() {

        final ProgressDialog progressDialog1 = new ProgressDialog(SendCaseActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog1.setIndeterminate(true);
        progressDialog1.setMessage("Loading Specialization...");
        progressDialog1.show();

        specializationListArraylist = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_SPECIALIZATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, response.toString());
                        if (response != null) {
                            String response_filter =  stripHtml(response);
                            JSONArray jsonArray = null;
                            try {
                                JSONObject jsonObject = new JSONObject(response_filter);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("false")) {
                                } else {
                                    jsonArray = jsonObject.getJSONArray("specialization_details");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            specialization = new SpecializationList(jsonArray.getJSONObject(i).getInt("spec_id"), jsonArray.getJSONObject(i).getString("spec_name"), USER_ID, USER_LOGIN_TYPE);
                                            specializationListArraylist.add(specialization);
                                        }
                                    }

                                    if(specializationListArraylist.size() > 0) {
                                        asynSpecDetailUpdate = new AsyncSpecializationListUpdate();
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                            asynSpecDetailUpdate.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, specializationListArraylist);
                                        } else {
                                            asynSpecDetailUpdate.execute(specializationListArraylist);
                                        }
                                    }
                                }
                                progressDialog1.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog1.dismiss();
                            }

                        }else {
                            progressDialog1.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog1.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(SendCaseActivity.this).
                getRequestQueue();
        AppController.getInstance(SendCaseActivity.this).addToRequestQueue(stringRequest);
    }

    private String stripHtml(String response) {
        return Html.fromHtml(response).toString();
    }

    void refreshItems(String MODE_OF_OPERATION) {
        requestServerdata(MODE_OF_OPERATION);
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        swipeRefreshLayout_doctorlist.setRefreshing(false);
    }

    private void requestServerdata(String mode_of_operation) {

        if (mode_of_operation.equals("normal")) {
            doctorListArraylist.clear();
            doctorListArraylist = new ArrayList<>();
           /* if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
                insertDoctorDetailsfromServer(mode_of_operation);
            } else {
                AppUtil.showCustomAlert(getActivity(), HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null);
            }
*/
            List<DoctorList> doctorCheck_normal = MedisensePracticeDB.getAllDoctors(SendCaseActivity.this, USER_LOGIN_TYPE,USER_ID);
            if (doctorCheck_normal.size() > 0) {
                Log.d(Utils.TAG, "Database is already exists - normal mode");
                for (int i = 0; i < doctorCheck_normal.size(); i++) {
                    doctors = new DoctorList(doctorCheck_normal.get(i).getDoctorId(), doctorCheck_normal.get(i).getDoctorName(),
                            doctorCheck_normal.get(i).getDoctorAge(), doctorCheck_normal.get(i).getDoctorExperience(),
                            doctorCheck_normal.get(i).getDoctorPhoto(), doctorCheck_normal.get(i).getDoctorQualification(),
                            doctorCheck_normal.get(i).getDoctorSpecializationId(), doctorCheck_normal.get(i).getDoctorSpecificationName(),
                            doctorCheck_normal.get(i).getDoctorFavoutite(),doctorCheck_normal.get(i).getDoctorLoginType(),doctorCheck_normal.get(i).getDoctorLoginUserId(),doctorCheck_normal.get(i).getDoctorAddress());
                    doctorListArraylist.add(doctors);
                }
                // Initialize a new instance of RecyclerView Adapter instance
                adapter = new SendCaseAdapter(SendCaseActivity.this, doctorListArraylist, PATIENT_ID, DOCREF_ID);
                // Set the adapter for RecyclerView
                recyclerView_doctorlist.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            else {
                Log.d(Utils.TAG, "No entries in the database - normal mode");
                if (NetworkUtil.getConnectivityStatusString(SendCaseActivity.this).equalsIgnoreCase("enabled")) {
                    insertDoctorDetailsfromServer(mode_of_operation);
                } else {
                    AppUtils.showCustomAlertMessage(SendCaseActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }

            }
        }
        else if (mode_of_operation.equals("pulltorefresh")) {
            List<DoctorList> patientCheck = MedisensePracticeDB.getAllDoctors(SendCaseActivity.this, USER_LOGIN_TYPE,USER_ID);
            if (patientCheck.size() > 0) {
                Log.d(Utils.TAG, "Database is already exists - pull to refresh mode");
                if (NetworkUtil.getConnectivityStatusString(SendCaseActivity.this).equalsIgnoreCase("enabled")) {
                    insertDoctorDetailsfromServer(mode_of_operation);
                } else {
                    AppUtils.showCustomAlertMessage(SendCaseActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }

            } else {
                Log.d(Utils.TAG, "No entries in the database - pull to refresh mode");
                if (NetworkUtil.getConnectivityStatusString(SendCaseActivity.this).equalsIgnoreCase("enabled")) {
                    insertDoctorDetailsfromServer(mode_of_operation);
                } else {
                    AppUtils.showCustomAlertMessage(SendCaseActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
            }
        }
        else if (mode_of_operation.equals("fetchalldata")) {
            if (NetworkUtil.getConnectivityStatusString(SendCaseActivity.this).equalsIgnoreCase("enabled")) {
                updateAllDoctorDetailsfromServer(mode_of_operation);
            } else {
                AppUtils.showCustomAlertMessage(SendCaseActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }
        }
    }

    private void insertDoctorDetailsfromServer(String mode_of_operation) {

        progressDialog = new ProgressDialog(SendCaseActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading doctors \nPlease wait....");
        progressDialog.show();

        doctorListArraylist = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PREM_DOCTOR_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.d(Utils.TAG, "DOC RES"+ response.toString());
                        if (response != null) {
                            Log.d(Utils.TAG, "doc response: " + response);
                            JSONObject jsonObject = null;
                            JSONArray jsonArray;
                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");

                                if (staus_res.equals("false")) {
                                    //  AppUtil.showCustomAlert(getContext(), "Server Error!!!", "You Can Try it later", "OK", null);
                                    AppUtils.showCustomErrorMessage(SendCaseActivity.this, "OOPS", "No results found !!", "OK", null, null);
                                } else {
                                    jsonArray = jsonObject.getJSONArray("doctor_list");
                                    Log.d(Utils.TAG, "doc length: " + String.valueOf(jsonArray.length()));
                                    if (jsonArray.length() > 0) {
                                        recyclerView_doctorlist.setVisibility(View.VISIBLE);
                                        _text_noResults.setVisibility(View.GONE);
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            doctors = new DoctorList(jsonArray.getJSONObject(i).getInt("ref_id"), jsonArray.getJSONObject(i).getString("doc_name"),jsonArray.getJSONObject(i).getInt("doc_age"),
                                                    jsonArray.getJSONObject(i).getString("doc_exp"),jsonArray.getJSONObject(i).getString("doc_photo"), jsonArray.getJSONObject(i).getString("doc_qual"),
                                                    jsonArray.getJSONObject(i).getInt("spec_id"),jsonArray.getJSONObject(i).getString("spec_name"),USER_LOGIN_TYPE,USER_ID,jsonArray.getJSONObject(i).getString("ref_address"));
                                            doctorListArraylist.add(doctors);
                                        }
                                        // Initialize a new instance of RecyclerView Adapter instance
                                        adapter = new SendCaseAdapter(SendCaseActivity.this, doctorListArraylist, PATIENT_ID, DOCREF_ID);
                                        recyclerView_doctorlist.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();

                                        Log.d(Utils.TAG, "doctorListArraylist Size: "+doctorListArraylist.size());
                                        asynDocDetailInsert = new AsyncDoctorListInsert();
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                            asynDocDetailInsert.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, doctorListArraylist);
                                        } else {
                                            asynDocDetailInsert.execute(doctorListArraylist);
                                        }
                                    }
                                    else {
                                        recyclerView_doctorlist.setVisibility(View.GONE);
                                        _text_noResults.setVisibility(View.VISIBLE);
                                    }

                                }
                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }

                            progressDialog.dismiss();
                        }else {
                            progressDialog.dismiss();
                            //  AppUtil.showCustomAlert(getContext(), "Server Error!!!","Unable to reach Server", "OK", null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //   AppUtil.showCustomAlert(getContext(), "Server Error!!!","You Can Try it later", "OK", null);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                map.put(APIClass.KEY_DOCTOR_TYPE, DOCTOR_TYPE);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(SendCaseActivity.this).
                getRequestQueue();
        AppController.getInstance(SendCaseActivity.this).addToRequestQueue(stringRequest);
    }

    private class AsyncDoctorListInsert extends AsyncTask<Object, Object, List<DoctorList>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //    progressDialog.dismiss();
            Log.d(Utils.TAG, "PRE EXECUTE");
        }


        @Override
        protected List<DoctorList> doInBackground(Object... params) {
            Log.d(Utils.TAG, "DO IN BACKGROUND");
            if (doctorListArraylist.size() > 0) {
                MedisensePracticeDB.doctorsClearContents(SendCaseActivity.this, USER_LOGIN_TYPE,USER_ID);
                for (int i = 0; i < doctorListArraylist.size(); i++) {
                    int count = MedisensePracticeDB.doctorsGetRowExists(SendCaseActivity.this,doctorListArraylist.get(i).getDoctorId(),USER_LOGIN_TYPE,USER_ID);
                    if(count == 1){
                        Log.d(Utils.TAG, "update: "+" i: "+String.valueOf(count));
                        DoctorList update = MedisensePracticeDB.doctorsGetExistingData(SendCaseActivity.this,doctorListArraylist.get(i).getDoctorId(),USER_LOGIN_TYPE,USER_ID);
                        MedisensePracticeDB.doctorsUpdateEntry(SendCaseActivity.this, update.getDoctorId(), update.getDoctorName(), update.getDoctorAge(),
                                update.getDoctorExperience(),update.getDoctorPhoto(),update.getDoctorQualification(),
                                update.getDoctorSpecializationId(), update.getDoctorSpecificationName(),update.getDoctorFavoutite(),
                                update.getDoctorLoginType(),update.getDoctorLoginUserId(),update.getDoctorAddress() );

                    }
                    else  if(count == 0) {
                        Log.d(Utils.TAG, "insert row : " + i + " : " + String.valueOf(count));
                        long res_add = MedisensePracticeDB.doctorsInsert(SendCaseActivity.this,doctorListArraylist.get(i).getDoctorId(),
                                doctorListArraylist.get(i).getDoctorName(), doctorListArraylist.get(i).getDoctorAge(),
                                doctorListArraylist.get(i).getDoctorExperience(),doctorListArraylist.get(i).getDoctorPhoto(),
                                doctorListArraylist.get(i).getDoctorQualification(),doctorListArraylist.get(i).getDoctorSpecializationId(),
                                doctorListArraylist.get(i).getDoctorSpecificationName(),DOCTOR_FAVOURITE,doctorListArraylist.get(i).getDoctorLoginType(),
                                doctorListArraylist.get(i).getDoctorLoginUserId(),doctorListArraylist.get(i).getDoctorAddress() );
                    }
                }
            }
            return doctorListArraylist;
        }

        @Override
        protected void onPostExecute(List<DoctorList> docLists) {
            super.onPostExecute(docLists);

            Log.d(Utils.TAG, "IN POST EXECUTE");
            if (doctorListArraylist.size() > 0) {
                List<DoctorList>  doctLists = MedisensePracticeDB.getAllDoctors(SendCaseActivity.this,Utils.USER_LOGIN_TYPE,USER_ID);
                Log.d(Utils.TAG +"Size", String.valueOf(doctLists.size()));
                if(doctLists.size() > 0) {
                    for(int i=0;i<doctLists.size();i++) {
                        //    Log.d(Utils.TAG + "frm db in ", String.valueOf(doctLists.get(i).getDoctorId()));
                    }
                }
                else {
                    //  Log.d(Utils.TAG, "< 0");
                }

            }
        }
    }

    private void updateAllDoctorDetailsfromServer(String mode_of_operation) {

        Log.d(Utils.TAG, " fetchAll: "+ DOCTOR_TYPE);
        Log.d(Utils.TAG, " fetchAll: "+ SPEC_ID);

        progressDialog = new ProgressDialog(SendCaseActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading all doctors \nPlease wait....");
        progressDialog.show();

        doctorListArraylist.clear();
        doctorListArraylist = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PREM_DOCTOR_LIST_ALL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.d(Utils.TAG, "DOC RES"+ response.toString());
                        if (response != null) {
                            Log.d(Utils.TAG, "doc response: " + response);
                            JSONObject jsonObject = null;
                            JSONArray jsonArray;
                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");

                                if (staus_res.equals("false")) {
                                    AppUtils.showCustomErrorMessage(SendCaseActivity.this, "OOPS", "No results found !!", "OK", null, null);
                                } else {
                                    jsonArray = jsonObject.getJSONArray("doctor_list");
                                    Log.d(Utils.TAG, "doc length: " + String.valueOf(jsonArray.length()));
                                    if (jsonArray.length() > 0) {
                                        recyclerView_doctorlist.setVisibility(View.VISIBLE);
                                        _text_noResults.setVisibility(View.GONE);
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            doctors = new DoctorList(jsonArray.getJSONObject(i).getInt("ref_id"), jsonArray.getJSONObject(i).getString("doc_name"),jsonArray.getJSONObject(i).getInt("doc_age"),
                                                    jsonArray.getJSONObject(i).getString("doc_exp"),jsonArray.getJSONObject(i).getString("doc_photo"), jsonArray.getJSONObject(i).getString("doc_qual"),
                                                    jsonArray.getJSONObject(i).getInt("spec_id"),jsonArray.getJSONObject(i).getString("spec_name"),USER_LOGIN_TYPE,USER_ID,jsonArray.getJSONObject(i).getString("ref_address"));
                                            doctorListArraylist.add(doctors);
                                        }
                                        // Initialize a new instance of RecyclerView Adapter instance
                                        adapter = new SendCaseAdapter(SendCaseActivity.this, doctorListArraylist, PATIENT_ID, DOCREF_ID);
                                        recyclerView_doctorlist.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    }
                                    else {
                                        recyclerView_doctorlist.setVisibility(View.GONE);
                                        _text_noResults.setVisibility(View.VISIBLE);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();
                        }else {
                            progressDialog.dismiss();
                            //  AppUtil.showCustomAlert(getContext(), "Server Error!!!","Unable to reach Server", "OK", null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //   AppUtil.showCustomAlert(getContext(), "Server Error!!!","You Can Try it later", "OK", null);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                map.put(APIClass.KEY_DOCTOR_TYPE, DOCTOR_TYPE);
                map.put(APIClass.KEY_DOCTOR_SPECID, String.valueOf(SPEC_ID));
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(SendCaseActivity.this).
                getRequestQueue();
        AppController.getInstance(SendCaseActivity.this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_sub_filter:
                specializationListArraylist = new ArrayList<>();
                hospitalListArraylist = new ArrayList<>();
                getSpecializationDetails();
                getHospitalDeatils();
                customDoctorFilter();
                break;
            case R.id.header_sub_layout:
                Bundle bundle = new Bundle();
                bundle.putInt("PATIENT_ID", PATIENT_ID);
                bundle.putInt("DOCREF_ID", DOCREF_ID);
                bundle.putString("DOCTOR_TYPE", DOCTOR_TYPE);
             /*   SearchDoctorsSendCaseFragment searchDoctors =  new SearchDoctorsSendCaseFragment();
                fragTransaction = fragManager.beginTransaction();
                searchDoctors.setArguments(bundle);
                fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                fragTransaction.replace(R.id.viewpager,searchDoctors,"SEARCH_DOCTORS");
                fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                // fragTransaction.addToBackStack("SEARCH_APPOINT");
                fragTransaction.commit();*/

                Intent i2 = new Intent(SendCaseActivity.this, SearchSendCaseDoctorsActivity.class);
                i2.putExtra("title","Search Doctors");
                i2.putExtra("PATIENT_ID",PATIENT_ID);
                i2.putExtra("DOCREF_ID",DOCREF_ID);
                i2.putExtra("DOCTOR_TYPE",DOCTOR_TYPE);
                startActivity(i2);

                break;
            case R.id.doclist_refresh:
                if (NetworkUtil.getConnectivityStatusString(SendCaseActivity.this).equalsIgnoreCase("enabled")) {
                    MODE_OF_OPERATION = "pulltorefresh";
                    refreshItems(MODE_OF_OPERATION);
                } else {
                    AppUtils.showCustomAlertMessage(SendCaseActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
                break;
        }
    }

    private void customDoctorFilter() {
        // custom dialog
        final Dialog dialog = new Dialog(SendCaseActivity.this, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_doctor_filter);
        dialog.setTitle("Filter Doctors By");
        dialog.setCanceledOnTouchOutside(true);

        final CustomTextViewSemiBold category_heading = (CustomTextViewSemiBold) dialog.findViewById(R.id.doctor_category_header);
        final CustomTextViewSemiBold specialization_heading = (CustomTextViewSemiBold) dialog.findViewById(R.id.doctor_specialization_header);
        final CustomTextViewSemiBold hospital_heading = (CustomTextViewSemiBold) dialog.findViewById(R.id.doctor_hospital_header);
        final CustomTextViewSemiBold all_doctors_list = (CustomTextViewSemiBold) dialog.findViewById(R.id.specialization_alldoctors);
        final ImageView cancel_dialog = (ImageView) dialog.findViewById(R.id.doctor_close);

        final LinearLayout category_content = (LinearLayout) dialog.findViewById(R.id.doctor_categoty_contents);
        final LinearLayout specialization_content = (LinearLayout) dialog.findViewById(R.id.doctor_specialization_contents);
        final LinearLayout hospital_content = (LinearLayout) dialog.findViewById(R.id.doctor_hospital_contents);

        specialization_content.setVisibility(View.VISIBLE);
        hospital_content.setVisibility(View.GONE);
        final ListView categoty_listview = (ListView) dialog.findViewById(R.id.category_list);
        final ListView specialization_listview = (ListView) dialog.findViewById(R.id.specialization_list);
        final ListView hospital_listview = (ListView) dialog.findViewById(R.id.hospital_list);

        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        all_doctors_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDoctorsResultFromServer("1",0);        // 1 - Universal Doctors
                dialog.dismiss();
            }
        });

        category_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_content.setVisibility(View.GONE);
                specialization_content.setVisibility(View.VISIBLE);
                hospital_content.setVisibility(View.GONE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    category_heading.setBackgroundResource(R.drawable.header_tab);
                }
                specialization_heading.setBackgroundColor(getResources().getColor(R.color.header_tab_bg));
                hospital_heading.setBackgroundColor(getResources().getColor(R.color.header_tab_bg));

                category_heading.setTextColor(getResources().getColor(R.color.colorPrimary));
                specialization_heading.setTextColor(getResources().getColor(R.color.black));
                hospital_heading.setTextColor(getResources().getColor(R.color.black));
            }
        });

        specialization_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                specialization_content.setVisibility(View.VISIBLE);
                category_content.setVisibility(View.GONE);
                hospital_content.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    specialization_heading.setBackgroundResource(R.drawable.header_tab);
                }
                category_heading.setBackgroundColor(getResources().getColor(R.color.header_tab_bg));
                hospital_heading.setBackgroundColor(getResources().getColor(R.color.header_tab_bg));

                specialization_heading.setTextColor(getResources().getColor(R.color.colorPrimary));
                category_heading.setTextColor(getResources().getColor(R.color.black));
                hospital_heading.setTextColor(getResources().getColor(R.color.black));
            }
        });

        hospital_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hospital_content.setVisibility(View.VISIBLE);
                category_content.setVisibility(View.GONE);
                specialization_content.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    hospital_heading.setBackgroundResource(R.drawable.header_tab);
                }
                category_heading.setBackgroundColor(getResources().getColor(R.color.header_tab_bg));
                specialization_heading.setBackgroundColor(getResources().getColor(R.color.header_tab_bg));

                hospital_heading.setTextColor(getResources().getColor(R.color.colorPrimary));
                category_heading.setTextColor(getResources().getColor(R.color.black));
                specialization_heading.setTextColor(getResources().getColor(R.color.black));
            }
        });

        //   final String[] mobileArray = {"My Connections","All Doctors","Diagnostics","Online Pharmacy"};
        final String[] mobileArray = {"All Doctors"};

        final ArrayAdapter categoryAdapter = new ArrayAdapter<String>(SendCaseActivity.this, R.layout.activity_listview, mobileArray);
        categoty_listview.setAdapter(categoryAdapter);

        categoty_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // Toast.makeText(getActivity(), mobileArray[position], Toast.LENGTH_SHORT).show();
                SPEC_ID = 0;
                if(mobileArray[position].equalsIgnoreCase("My Connections")) {
                    getDoctorsResultFromServer("2",0);        // 2 - My Connection Doctors
                }
                else  if(mobileArray[position].equalsIgnoreCase("All Doctors")) {
                    getDoctorsResultFromServer("1",0);        // 1 - Universal Doctors
                }
                else  if(mobileArray[position].equalsIgnoreCase("Diagnostics")) {
                    getDoctorsResultFromServer("3",0);        // 3- Diagnostic Doctors
                }
                else  if(mobileArray[position].equalsIgnoreCase("Online Pharmacy")) {
                    getDoctorsResultFromServer("4",0);        // 4 - Online Pharmacy
                }
                else {
                    getDoctorsResultFromServer("1",0);
                }

                dialog.dismiss();
            }
        });

        final SearchView category_searchView = (SearchView) dialog.findViewById(R.id.doctor_categoty_search_view);
        category_searchView.setIconifiedByDefault(false);
        category_searchView.setQueryHint("Search Category Here");
        category_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    //     categoty_listview.clearTextFilter();
                    categoty_listview.setAdapter(categoryAdapter);
                } else {
                    categoty_listview.setFilterText(newText.toString());
                }
                return true;
            }
        });

        final CustomSpecFilterAdapter specAdapter = new CustomSpecFilterAdapter(SendCaseActivity.this,
                R.layout.activity_listview, specializationListArraylist);
        specialization_listview.setAdapter(specAdapter);
        specialization_listview.setTextFilterEnabled(true);

        specialization_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SpecializationList myCustomAdapterItem = (SpecializationList) parent.getItemAtPosition(position);
              /*  Toast.makeText(getActivity(), "Clicked Position " + position + ": " + myCustomAdapterItem.getSpecializationId()+" : "+myCustomAdapterItem.getSpecializationName(),
                        Toast.LENGTH_SHORT)
                        .show();*/

                getDoctorsResultFromServer("5",myCustomAdapterItem.getSpecializationId());
                SPEC_ID = myCustomAdapterItem.getSpecializationId();
                dialog.dismiss();

            }
        });

        final SearchView specialization_searchView = (SearchView) dialog.findViewById(R.id.doctor_specialization_search_view);
        specialization_searchView.setIconifiedByDefault(false);
        specialization_searchView.setQueryHint("Search Specialization Here");
        specialization_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Utils.TAG, "SERACH: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    Log.d(Utils.TAG, "is empty " );
                    specAdapter.filter(newText.toString());
                    // specialization_listview.clearTextFilter();
                } else {
                    //  specialization_listview.setFilterText(newText.toString());
                    specAdapter.filter(newText.toString());
                }
                return true;
            }
        });

        final ArrayList<String> hospitalArray = new ArrayList<String>();
        Log.d(Utils.TAG, "hospital Size: " + hospitalListArraylist.size());
        for (int i = 0; i < hospitalListArraylist.size(); i++) {
            hospitalArray.add(hospitalListArraylist.get(i).getHospitalName());
        }

       /* ArrayAdapter hospitalAdapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, hospitalArray);
        hospital_listview.setAdapter(hospitalAdapter);*/

        final CustomHospitalFilterAdapter hospitalAdapter = new CustomHospitalFilterAdapter(SendCaseActivity.this,
                R.layout.activity_listview, hospitalListArraylist);
        hospital_listview.setAdapter(hospitalAdapter);
        hospital_listview.setTextFilterEnabled(true);

        hospital_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             /*   int  get_hospID = hospitalListArraylist.get(position).getHospitalId();
                //  Toast.makeText(getActivity(), specArray.get(position) + " id: "+ get_specID, Toast.LENGTH_SHORT).show();
                getDoctorsResultFromServer("6",get_hospID);
                SPEC_ID = get_hospID;
                dialog.dismiss();*/

                HospitalList myCustomAdapterItem = (HospitalList) parent.getItemAtPosition(position);
              /*  Toast.makeText(getActivity(), "Clicked Position " + position + ": " + myCustomAdapterItem.getSpecializationId()+" : "+myCustomAdapterItem.getSpecializationName(),
                        Toast.LENGTH_SHORT)
                        .show();*/

                getDoctorsResultFromServer("6",myCustomAdapterItem.getHospitalId());
                SPEC_ID = myCustomAdapterItem.getHospitalId();
                dialog.dismiss();
            }
        });

        final SearchView hospital_searchView = (SearchView) dialog.findViewById(R.id.doctor_hospital_search_view);
        hospital_searchView.setIconifiedByDefault(false);
        hospital_searchView.setQueryHint("Search Hospital Here");
        hospital_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    hospitalAdapter.filter(newText.toString());
                } else {
                    hospitalAdapter.filter(newText.toString());
                }
                return true;
            }
        });

        dialog.show();
    }

    private class AsyncSpecializationListUpdate extends AsyncTask<Object, Object, List<SpecializationList>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //     dialog.dismiss();
        }


        @Override
        protected List<SpecializationList> doInBackground(Object... params) {
            Log.d(Utils.TAG, "DO IN BACKGROUND AsyncdoctorListUpdate");

            if (specializationListArraylist.size() > 0) {
                MedisensePracticeDB.clearSpecialization(SendCaseActivity.this);
                for (int i = 0; i < specializationListArraylist.size(); i++) {
//                    Log.d(Utils.TAG + "id: ", String.valueOf(specializationListArraylist.get(i).getSpecializationId()));
//                    Log.d(Utils.TAG + "Name: ", specializationListArraylist.get(i).getSpecializationName());
                    //   MedisensePracticeDB.updateSpecialization(getActivity(), specializationListArraylist.get(i).getSpecializationId(), specializationListArraylist.get(i).getSpecializationName());
                    MedisensePracticeDB.insertSpecialization(SendCaseActivity.this, specializationListArraylist.get(i).getSpecializationId(), specializationListArraylist.get(i).getSpecializationName());
                }

            }
            return specializationListArraylist;
        }

        @Override
        protected void onPostExecute(List<SpecializationList> specializationLists) {
            super.onPostExecute(specializationLists);
            if (specializationListArraylist.size() > 0) {
                List<SpecializationList> specLists = MedisensePracticeDB.getAllSpecialization(SendCaseActivity.this);
                Log.d(Utils.TAG + " Execute Size", String.valueOf(specLists.size()));
            }
        }
    }
    private void getDoctorsResultFromServer(final String DOCTORS_TYPE, final int SPEC_ID) {
        DOCTOR_TYPE = DOCTORS_TYPE;

        progressDialog = new ProgressDialog(SendCaseActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading doctors \nPlease wait...");
        progressDialog.show();

        doctorListArraylist.clear();
        doctorListArraylist = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PREM_DOCTOR_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.d(Utils.TAG, "DOC RES"+ response.toString());
                        if (response != null) {
                            Log.d(Utils.TAG, "doc response: " + response);
                            JSONObject jsonObject = null;
                            JSONArray jsonArray;
                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");

                                if (staus_res.equals("false")) {
                                    AppUtils.showCustomErrorMessage(SendCaseActivity.this, "OOPS", "No results found !!", "OK", null, null);
                                } else {
                                    jsonArray = jsonObject.getJSONArray("doctor_list");
                                    Log.d(Utils.TAG, "doc length: " + String.valueOf(jsonArray.length()));
                                    if (jsonArray.length() > 0) {
                                        recyclerView_doctorlist.setVisibility(View.VISIBLE);
                                        _text_noResults.setVisibility(View.GONE);
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            doctors = new DoctorList(jsonArray.getJSONObject(i).getInt("ref_id"), jsonArray.getJSONObject(i).getString("doc_name"),jsonArray.getJSONObject(i).getInt("doc_age"),
                                                    jsonArray.getJSONObject(i).getString("doc_exp"),jsonArray.getJSONObject(i).getString("doc_photo"), jsonArray.getJSONObject(i).getString("doc_qual"),
                                                    jsonArray.getJSONObject(i).getInt("spec_id"),jsonArray.getJSONObject(i).getString("spec_name"),USER_LOGIN_TYPE,USER_ID,jsonArray.getJSONObject(i).getString("ref_address"));
                                            doctorListArraylist.add(doctors);
                                        }
                                        // Initialize a new instance of RecyclerView Adapter instance
                                        adapter = new SendCaseAdapter(SendCaseActivity.this, doctorListArraylist, PATIENT_ID, DOCREF_ID);
                                        recyclerView_doctorlist.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    }
                                    else {
                                        recyclerView_doctorlist.setVisibility(View.GONE);
                                        _text_noResults.setVisibility(View.VISIBLE);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();
                        }else {
                            progressDialog.dismiss();
                            //  AppUtil.showCustomAlert(getContext(), "Server Error!!!","Unable to reach Server", "OK", null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //   AppUtil.showCustomAlert(getContext(), "Server Error!!!","You Can Try it later", "OK", null);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                map.put(APIClass.KEY_DOCTOR_TYPE, DOCTORS_TYPE);
                map.put(APIClass.KEY_DOCTOR_SPECID, String.valueOf(SPEC_ID));
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(SendCaseActivity.this).
                getRequestQueue();
        AppController.getInstance(SendCaseActivity.this).addToRequestQueue(stringRequest);
    }

    private void collectDoctorsSearchResultFromServer(final String SEARCH_QUERY) {

        progressDialog = new ProgressDialog(SendCaseActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Searching Result...");
        progressDialog.show();


        doctorListArraylist.clear();
        doctorListArraylist = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_DOCTORS_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, response.toString());
                        JSONArray jsonArray = null;
                        String status = null;
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Log.d(Utils.TAG, response.toString());
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("false")) {
                                    AppUtils.showCustomErrorMessage(SendCaseActivity.this, "Search!!", "No records found !!!", "OK", null, null);
                                } else {
                                    try {
                                        jsonArray = jsonObject.getJSONArray("page_result");
                                        if (jsonArray.length() > 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {

                                                doctors = new DoctorList(jsonArray.getJSONObject(i).getInt("ref_id"), jsonArray.getJSONObject(i).getString("ref_name"), jsonArray.getJSONObject(i).getInt("doc_age"),
                                                        jsonArray.getJSONObject(i).getString("ref_exp"), jsonArray.getJSONObject(i).getString("doc_photo"), jsonArray.getJSONObject(i).getString("doc_qual"),
                                                        jsonArray.getJSONObject(i).getInt("spec_id"), jsonArray.getJSONObject(i).getString("spec_name"), 0, USER_LOGIN_TYPE,USER_ID, jsonArray.getJSONObject(i).getString("ref_address"));
                                                doctorListArraylist.add(doctors);
                                            }
                                            adapter = new SendCaseAdapter(SendCaseActivity.this, doctorListArraylist, PATIENT_ID, DOCREF_ID);
                                            recyclerView_doctorlist.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();
                        }else {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //   AppUtil.showCustomAlert(getContext(), "Server Error!!!","You Can Try it later", "OK", null);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_SEARCH_DATA, SEARCH_QUERY);
                map.put(APIClass.KEY_LOGINTYPE, String.valueOf(USER_LOGIN_TYPE));
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(SendCaseActivity.this).
                getRequestQueue();
        AppController.getInstance(SendCaseActivity.this).addToRequestQueue(stringRequest);
    }

    private void getHospitalDeatils() {
        List<HospitalList> hospitalCheck = MedisensePracticeDB.getAllHospitals(SendCaseActivity.this);
        //  Log.d(Utils.TAG, " hospsize:"+ String.valueOf(hospitalCheck.size()));

        if (hospitalCheck.size() > 0) {
            Log.d(Utils.TAG, "Hospital Entries already exists in db");
            for (int i = 0; i < hospitalCheck.size(); i++) {
                hospitals = new HospitalList(hospitalCheck.get(i).getHospitalId(), hospitalCheck.get(i).getHospitalName(),
                        hospitalCheck.get(i).getHospitalCity(), hospitalCheck.get(i).getHospitalState(), USER_ID, USER_LOGIN_TYPE);
                hospitalListArraylist.add(hospitals);
            }

        } else {
            Log.d(Utils.TAG, "No entries for hospital");
            if (NetworkUtil.getConnectivityStatusString(SendCaseActivity.this).equalsIgnoreCase("enabled")) {
                addHospitalDetailsfromServer();
            }
        }
    }
    private void addHospitalDetailsfromServer() {
        hospitalListArraylist = new ArrayList<>();

        final ProgressDialog progressDialog2 = new ProgressDialog(SendCaseActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog2.setIndeterminate(true);
        progressDialog2.setMessage("Loading Hospital...");
        progressDialog2.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOSPITAL_LIST_ALL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.d(Utils.TAG, response.toString());
                        JSONArray jsonArray = null;
                        String status = null;
                        if (response != null) {
                            String response_filter =  stripHtml(response);
                            try {
                                JSONObject jsonObject = new JSONObject(response_filter);
                                Log.d(Utils.TAG, response.toString());
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("false")) {
                                } else {
                                    jsonArray = jsonObject.getJSONArray("hospital_details");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            hospitals = new HospitalList(jsonArray.getJSONObject(i).getInt("hosp_id"), jsonArray.getJSONObject(i).getString("hosp_name"),
                                                    jsonArray.getJSONObject(i).getString("hosp_city"),jsonArray.getJSONObject(i).getString("hosp_state"), USER_ID, USER_LOGIN_TYPE);
                                            hospitalListArraylist.add(hospitals);
                                        }

                                        if(hospitalListArraylist.size() > 0) {
                                            asynHospDetailInsert = new AsyncHospitalListInsert();
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                                asynHospDetailInsert.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, hospitalListArraylist);
                                            } else {
                                                asynHospDetailInsert.execute(hospitalListArraylist);
                                            }
                                        }
                                    }
                                }
                                progressDialog2.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog2.dismiss();
                            }
                        }else {
                            progressDialog2.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  dialog2.dismiss();
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

        RequestQueue requestQueue = AppController.getInstance(SendCaseActivity.this).
                getRequestQueue();
        AppController.getInstance(SendCaseActivity.this).addToRequestQueue(stringRequest);
    }

    private class AsyncHospitalListInsert extends AsyncTask<Object, Object, List<HospitalList>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected List<HospitalList> doInBackground(Object... params) {
            if (hospitalListArraylist.size() > 0) {
                for (int i = 0; i < hospitalListArraylist.size(); i++) {
                    //   Log.d(Utils.TAG + "id: ", String.valueOf(hospitalListArraylist.get(i).getHospitalId()));
                    MedisensePracticeDB.hospitalInsert(SendCaseActivity.this, hospitalListArraylist.get(i).getHospitalId(), hospitalListArraylist.get(i).getHospitalName(),
                            hospitalListArraylist.get(i).getHospitalCity(),hospitalListArraylist.get(i).getHospitalState(), USER_ID, USER_LOGIN_TYPE );
                }
            }
            return hospitalListArraylist;
        }
        @Override
        protected void onPostExecute(List<HospitalList> hospitalLists) {
            super.onPostExecute(hospitalLists);
            if (hospitalListArraylist.size() > 0) {

                List<HospitalList> hospLists = MedisensePracticeDB.getAllHospitals(SendCaseActivity.this);
                Log.d(Utils.TAG +" post HSize", String.valueOf(hospLists.size()));
            }
        }
    }
}
