package com.medisensehealth.fdccontributor.activities.emr;

import android.app.Activity;
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
import com.google.gson.reflect.TypeToken;
import com.medisensehealth.fdccontributor.DataModel.MyPatientsList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.adapter.emr.MyPatientSeachAdapter;
import com.medisensehealth.fdccontributor.adapter.emr.MyPatientsAdapter;
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
 * Created by HP on 26-03-2018.
 */

public class SearchMyPatientsActivity extends AppCompatActivity implements View.OnClickListener {
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE, MYPATIENT_LIST;
    View header_tabs, footertabs;

    AutoCompleteTextView _header_seach_autotext;
    ImageView _header_search_cancel;

    List<MyPatientsList> patientsListArraylist = new ArrayList<>();
    List<MyPatientsList> patientsSharedPrefArraylist = new ArrayList<>();;
    MyPatientsAdapter adapter;
    MyPatientsList patients;
    RecyclerView recyclerView;
    CustomTextViewItalic no_data;
    Gson gson;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);
            Log.d(Utils.TAG, " ************ Search Patients Activity *********** ");
        }

        shareadPreferenceClass = new ShareadPreferenceClass(SearchMyPatientsActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(SearchMyPatientsActivity.this);

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            MYPATIENT_LIST = sharedPreferences.getString(HCConstants.PREF_MYPATIENT_LISTS, "");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            MYPATIENT_LIST = sharedPreferences.getString(HCConstants.PREF_MYPATIENT_LISTS, "");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {

            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            MYPATIENT_LIST = sharedPreferences.getString(HCConstants.PREF_MYPATIENT_LISTS, "");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        Log.d(Utils.TAG +"LoginType: ", Utils.USER_LOGIN_TYPE);
        Log.d(Utils.TAG +"LoginId: ", String.valueOf(Utils.USER_LOGIN_ID));

        initializationViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               // InputMethodManager mImm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
               // mImm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                hideKeyboard(SearchMyPatientsActivity.this);
                finish();
                break;
        }
        return true;
    }

    private void hideKeyboard(SearchMyPatientsActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void initializationViews() {
        patientsListArraylist = new ArrayList<>();

        _header_seach_autotext = (AutoCompleteTextView) findViewById(R.id.search_autocompletetext);
        _header_search_cancel = (ImageView) findViewById(R.id.search_cancel);
        _header_search_cancel.setOnClickListener(this);
        _header_search_cancel.setVisibility(View.GONE);

        recyclerView = (RecyclerView) findViewById(R.id.search_recycleriew);
        no_data = (CustomTextViewItalic) findViewById(R.id.search_noresult);
        no_data.setVisibility(View.GONE);

        adapter = new MyPatientsAdapter(SearchMyPatientsActivity.this, patientsListArraylist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SearchMyPatientsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        gson = new Gson();

        if (MYPATIENT_LIST.equals("")) {
            Log.d(Utils.TAG, "MYPATIENT_LIST EMPTY ");

        } else {
            patientsListArraylist = gson.fromJson(MYPATIENT_LIST, new TypeToken<List<MyPatientsList>>() {
            }.getType());
            Log.d(Utils.TAG, "MYPATIENT_LIST > 0 ");
        }

        _header_seach_autotext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    _header_search_cancel.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    String search_result = _header_seach_autotext.getText().toString();
                    collectMyPatientSerachResults(search_result);
                    return true;
                }
                return false;
            }
        });

        _header_seach_autotext.setThreshold(1);//will start working from first character
        _header_seach_autotext.setTextColor(getResources().getColor(R.color.colorPrimary));
        final MyPatientSeachAdapter adapter = new MyPatientSeachAdapter(SearchMyPatientsActivity.this, R.layout.fragment_mypatients, R.id.lbl_name, patientsListArraylist);
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

                adapter = new MyPatientsAdapter(SearchMyPatientsActivity.this, patientsListArraylist);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void collectMyPatientSerachResults(final String SEARCH_QUERY) {
        patientsListArraylist = new ArrayList<>();
        patientsListArraylist.clear();
        adapter.notifyDataSetChanged();

        final ProgressDialog progressDialog4 = new ProgressDialog(SearchMyPatientsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog4.setIndeterminate(true);
        progressDialog4.setMessage("Searching Result...");
        progressDialog4.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_MYPATIENTS_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "search mypatients: "+ response.toString());
                        if (response != null) {
                            //   GetJsonArrayResponseSearch(response);

                            try {
                                patientsListArraylist = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");

                                if (staus_res.equals("false")) {
                                    recyclerView.setVisibility(View.GONE);
                                    no_data.setVisibility(View.VISIBLE);
                                    no_data.setText("No records are found !!!");
                                    AppUtils.showCustomAlertMessage(SearchMyPatientsActivity.this, "Patients","No medical cases found !!!", "OK", null, null);
                                } else {
                                    JSONArray jsonArray = null;
                                    String status = null;
                                    try {
                                        jsonArray = jsonObject.getJSONArray("mypatient_details");
                                        if (jsonArray.length() > 0) {
                                            no_data.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            for (int i = 0; i < jsonArray.length(); i++) {

                                                patients = new MyPatientsList(jsonArray.getJSONObject(i).getInt("patient_id"), jsonArray.getJSONObject(i).getString("patient_name"),jsonArray.getJSONObject(i).getString("patient_age"),jsonArray.getJSONObject(i).getString("patient_email"),
                                                        jsonArray.getJSONObject(i).getInt("patient_gen"),jsonArray.getJSONObject(i).getString("weight"),
                                                        jsonArray.getJSONObject(i).getInt("hyper_cond"),jsonArray.getJSONObject(i).getInt("diabetes_cond"),jsonArray.getJSONObject(i).getString("smoking"),
                                                        jsonArray.getJSONObject(i).getString("alcoholic"),jsonArray.getJSONObject(i).getString("drug_abuse"),jsonArray.getJSONObject(i).getString("other_details"),
                                                        jsonArray.getJSONObject(i).getString("family_history"),jsonArray.getJSONObject(i).getString("prev_inter"),jsonArray.getJSONObject(i).getString("neuro_issue"),
                                                        jsonArray.getJSONObject(i).getString("kidney_issue"),jsonArray.getJSONObject(i).getString("patient_mob"),
                                                        jsonArray.getJSONObject(i).getString("patient_loc"), jsonArray.getJSONObject(i).getString("pat_state"), jsonArray.getJSONObject(i).getString("pat_country"),
                                                        jsonArray.getJSONObject(i).getString("patient_addrs"),jsonArray.getJSONObject(i).getString("TImestamp"),
                                                        jsonArray.getJSONObject(i).getString("user_id"),
                                                        jsonArray.getJSONObject(i).getInt("doc_id"),jsonArray.getJSONObject(i).getString("system_date"),USER_ID,USER_LOGIN_TYPE,jsonArray.getJSONObject(i).getString("height"));
                                                patientsListArraylist.add(patients);


                                            }

                                            adapter = new MyPatientsAdapter(SearchMyPatientsActivity.this, patientsListArraylist);
                                            recyclerView.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                        }
                                        else {
                                            recyclerView.setVisibility(View.GONE);
                                            no_data.setVisibility(View.VISIBLE);
                                            no_data.setText("No records are found !!!");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                progressDialog4.dismiss();
                            } catch (Exception e) {
                                progressDialog4.dismiss();
                                e.printStackTrace();
                            }
                        }else {
                            progressDialog4.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog4.dismiss();
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

        RequestQueue requestQueue = AppController.getInstance(SearchMyPatientsActivity.this).
                getRequestQueue();
        AppController.getInstance(SearchMyPatientsActivity.this).addToRequestQueue(stringRequest);
    }

}
