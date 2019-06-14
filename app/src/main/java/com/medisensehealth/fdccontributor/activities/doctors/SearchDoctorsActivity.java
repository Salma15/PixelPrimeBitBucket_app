package com.medisensehealth.fdccontributor.activities.doctors;

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
import com.medisensehealth.fdccontributor.DataModel.DoctorList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.adapter.doctors.DoctorsListAdapter;
import com.medisensehealth.fdccontributor.adapter.doctors.DoctorsSeachAdapter;
import com.medisensehealth.fdccontributor.database.MedisensePracticeDB;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.utils.AppController;
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

public class SearchDoctorsActivity extends AppCompatActivity implements View.OnClickListener {
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE, APPOINTMENT_LIST;

    AutoCompleteTextView _header_seach_autotext;
    ImageView _header_search_cancel;

    List<DoctorList> doctorListArraylist = new ArrayList<>();
    DoctorsListAdapter adapter;
    DoctorList doctors;
    RecyclerView recyclerView_doctorlist;
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

        shareadPreferenceClass = new ShareadPreferenceClass(SearchDoctorsActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(SearchDoctorsActivity.this);

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


        Log.d(Utils.TAG , " *********** Search Doctors ****************");
        Log.d(Utils.TAG +"LoginType: ", Utils.USER_LOGIN_TYPE);
        Log.d(Utils.TAG +"Userid: ", String.valueOf(Utils.USER_LOGIN_ID));

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
        doctorListArraylist = new ArrayList<>();
        _header_seach_autotext = (AutoCompleteTextView) findViewById(R.id.search_autocompletetext);
        _header_seach_autotext.setHint("Search Doctor Here...");
        _header_search_cancel = (ImageView) findViewById(R.id.search_cancel);
        _header_search_cancel.setOnClickListener(this);
        _header_search_cancel.setVisibility(View.GONE);


        recyclerView_doctorlist = (RecyclerView) findViewById(R.id.search_recycleriew);
        no_data = (CustomTextViewItalic) findViewById(R.id.search_noresult);
        no_data.setVisibility(View.GONE);

        adapter = new DoctorsListAdapter(SearchDoctorsActivity.this,doctorListArraylist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SearchDoctorsActivity.this);
        recyclerView_doctorlist.setLayoutManager(mLayoutManager);
        recyclerView_doctorlist.setItemAnimator(new DefaultItemAnimator());
        recyclerView_doctorlist.setHasFixedSize(true);
        recyclerView_doctorlist.setItemViewCacheSize(20);
        recyclerView_doctorlist.setDrawingCacheEnabled(true);
        recyclerView_doctorlist.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        doctorListArraylist = MedisensePracticeDB.getAllDoctors(SearchDoctorsActivity.this, USER_LOGIN_TYPE,USER_ID);

        _header_seach_autotext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    _header_search_cancel.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    String search_result = _header_seach_autotext.getText().toString();
                    collectDoctorsSearchResultFromServer(search_result);
                    return true;
                }
                return false;
            }
        });

        _header_seach_autotext.setThreshold(1);//will start working from first character
        _header_seach_autotext.setTextColor(getResources().getColor(R.color.colorPrimary));
        final DoctorsSeachAdapter adapter = new DoctorsSeachAdapter(SearchDoctorsActivity.this, R.layout.fragment_doctors, R.id.lbl_name, doctorListArraylist);
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

                adapter = new DoctorsListAdapter(SearchDoctorsActivity.this, doctorListArraylist);
                recyclerView_doctorlist.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void collectDoctorsSearchResultFromServer(final String SEARCH_QUERY) {

        final ProgressDialog progressDialog = new ProgressDialog(SearchDoctorsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
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
                                    no_data.setVisibility(View.VISIBLE);
                                    no_data.setText("No Doctor Records Found !!!");
                                    // AppUtils.showCustomErrorMessage(getActivity(), "Search !!", "No records found !!!", "OK", null, null);
                                } else {
                                    no_data.setVisibility(View.GONE);
                                    try {
                                        jsonArray = jsonObject.getJSONArray("page_result");
                                        if (jsonArray.length() > 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {

                                                doctors = new DoctorList(jsonArray.getJSONObject(i).getInt("ref_id"), jsonArray.getJSONObject(i).getString("doc_name"), jsonArray.getJSONObject(i).getInt("doc_age"),
                                                        jsonArray.getJSONObject(i).getString("doc_exp"), jsonArray.getJSONObject(i).getString("doc_photo"), jsonArray.getJSONObject(i).getString("doc_qual"),
                                                        jsonArray.getJSONObject(i).getInt("spec_id"), jsonArray.getJSONObject(i).getString("spec_name"),
                                                        0, USER_LOGIN_TYPE,USER_ID, jsonArray.getJSONObject(i).getString("ref_address"));
                                                doctorListArraylist.add(doctors);
                                            }
                                            adapter = new DoctorsListAdapter(SearchDoctorsActivity.this, doctorListArraylist);
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

        RequestQueue requestQueue = AppController.getInstance(SearchDoctorsActivity.this).
                getRequestQueue();
        AppController.getInstance(SearchDoctorsActivity.this).addToRequestQueue(stringRequest);
    }
}
