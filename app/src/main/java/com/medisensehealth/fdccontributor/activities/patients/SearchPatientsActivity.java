package com.medisensehealth.fdccontributor.activities.patients;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.medisensehealth.fdccontributor.DataModel.PatientsList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.adapter.patients.PatientsAdapter;
import com.medisensehealth.fdccontributor.adapter.patients.PatientsSeachAdapter;
import com.medisensehealth.fdccontributor.database.MedisensePracticeDB;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextViewItalic;

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

public class SearchPatientsActivity extends AppCompatActivity implements View.OnClickListener {
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE;

    AutoCompleteTextView _header_seach_autotext;
    ImageView _header_search_cancel;

    PatientsList patients;
    RecyclerView recyclerView_patient;
    PatientsAdapter adapter;
    List<PatientsList> PatientsListArraylist = new ArrayList<>();
    CustomTextViewItalic no_data;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle b = getIntent().getExtras();
        if( b != null) {
            String title = b.getString("title");
            setTitle(title);
        }

        shareadPreferenceClass = new ShareadPreferenceClass(SearchPatientsActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(SearchPatientsActivity.this);

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


        Log.d(Utils.TAG , " *********** Search Patients ****************");
        Log.d(Utils.TAG +" PatId: ", Utils.USER_LOGIN_TYPE);
        Log.d(Utils.TAG +" PatLogType: ", String.valueOf(Utils.USER_LOGIN_ID));

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
        PatientsListArraylist = new ArrayList<>();
        _header_seach_autotext = (AutoCompleteTextView) findViewById(R.id.search_autocompletetext);
        _header_search_cancel = (ImageView) findViewById(R.id.search_cancel);
        _header_search_cancel.setOnClickListener(this);
        _header_search_cancel.setVisibility(View.GONE);

        recyclerView_patient = (RecyclerView) findViewById(R.id.search_recycleriew);
        no_data = (CustomTextViewItalic) findViewById(R.id.search_noresult);
        no_data.setVisibility(View.GONE);

        adapter = new PatientsAdapter(SearchPatientsActivity.this,PatientsListArraylist);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SearchPatientsActivity.this);
        recyclerView_patient.setLayoutManager(mLayoutManager);
        recyclerView_patient.setItemAnimator(new DefaultItemAnimator());

        PatientsListArraylist = MedisensePracticeDB.getAllPatients(SearchPatientsActivity.this,Utils.USER_LOGIN_TYPE, USER_ID );

        _header_seach_autotext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    _header_search_cancel.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    String search_result = _header_seach_autotext.getText().toString();
                    collectPatientSerachResults(search_result);
                    return true;
                }
                return false;
            }
        });

        _header_seach_autotext.setThreshold(1);//will start working from first character
        _header_seach_autotext.setTextColor(getResources().getColor(R.color.colorPrimary));
        final PatientsSeachAdapter adapter = new PatientsSeachAdapter(SearchPatientsActivity.this, R.layout.fragment_patients, R.id.lbl_name, PatientsListArraylist);
        _header_seach_autotext.setAdapter(adapter);

        _header_seach_autotext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.notifyDataSetChanged();
                _header_search_cancel.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_cancel:
                _header_search_cancel.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                adapter = new PatientsAdapter(SearchPatientsActivity.this, PatientsListArraylist);
                recyclerView_patient.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void collectPatientSerachResults(final String SEARCH_QUERY) {
        PatientsListArraylist = new ArrayList<>();
        PatientsListArraylist.clear();
        adapter.notifyDataSetChanged();

        Log.d(Utils.TAG +" PatId: ", String.valueOf(USER_ID));
        Log.d(Utils.TAG +" PatLogType: ", USER_LOGIN_TYPE);

        final ProgressDialog progressDialog = new ProgressDialog(SearchPatientsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Searching Result...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PATIENT_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, response.toString());
                        if (response != null) {
                            //   GetJsonArrayResponseSearch(response);

                            try {
                                PatientsListArraylist = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");

                                if (staus_res.equals("false")) {
                                    AppUtils.showCustomErrorMessage(SearchPatientsActivity.this, "Patient Records","No results found !!", "OK", null, null);
                                } else {
                                    JSONArray jsonArray = null;
                                    String status = null;
                                    try {
                                        jsonArray = jsonObject.getJSONArray("page_result");
                                        if (jsonArray.length() == 0) {
                                            no_data.setVisibility(View.VISIBLE);
                                            no_data.setText("No patient records found !!!");
                                        }
                                        else if (jsonArray.length() > 0) {
                                            no_data.setVisibility(View.GONE);
                                            for (int i = 0; i < jsonArray.length(); i++) {

                                                patients = new PatientsList(jsonArray.getJSONObject(i).getInt("pat_id"), jsonArray.getJSONObject(i).getString("pat_name"),
                                                        jsonArray.getJSONObject(i).getString("pat_age"), jsonArray.getJSONObject(i).getString("pat_loc"), jsonArray.getJSONObject(i).getInt("pat_status"),jsonArray.getJSONObject(i).getString("pat_doc_name"),jsonArray.getJSONObject(i).getInt("pat_doc_id"),Utils.USER_LOGIN_TYPE,jsonArray.getJSONObject(i).getString("pat_refered_by"),USER_ID,jsonArray.getJSONObject(i).getString("pat_status_time"));
                                                PatientsListArraylist.add(patients);
                                            }

                                            // Initialize a new instance of RecyclerView Adapter instance
                                            adapter = new PatientsAdapter(SearchPatientsActivity.this, PatientsListArraylist);
                                            // Set the adapter for RecyclerView
                                            recyclerView_patient.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                progressDialog.dismiss();
                            } catch (Exception e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                            }
                        }else {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //  AppUtil.showCustomAlert(getContext(), "Server Error!!!","You Can Try it later", "OK", null);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_SEARCH_DATA, SEARCH_QUERY);
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(SearchPatientsActivity.this).
                getRequestQueue();
        AppController.getInstance(SearchPatientsActivity.this).addToRequestQueue(stringRequest);
    }
}
