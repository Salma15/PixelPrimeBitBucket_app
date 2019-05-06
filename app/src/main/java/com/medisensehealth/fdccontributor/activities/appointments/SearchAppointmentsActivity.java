package com.medisensehealth.fdccontributor.activities.appointments;

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
import com.medisensehealth.fdccontributor.DataModel.AppointmentList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.adapter.appointments.AppointmentAdapter;
import com.medisensehealth.fdccontributor.adapter.appointments.AppointmentSeachAdapter;
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

public class SearchAppointmentsActivity extends AppCompatActivity implements View.OnClickListener {
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID, HOSPITAL_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE, APPOINTMENT_LIST;
    View header_tabs, footertabs;

    AutoCompleteTextView _header_seach_autotext;
    ImageView _header_search_cancel;

    private AppointmentAdapter apptAdapter;
    private AppointmentList appointmemts;
    private List<AppointmentList> appointmentArryList = new ArrayList<>();
    RecyclerView recyclerView_appoint;
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

        shareadPreferenceClass = new ShareadPreferenceClass(SearchAppointmentsActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(SearchAppointmentsActivity.this);

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            APPOINTMENT_LIST = sharedPreferences.getString(HCConstants.PREF_APPOINTMENT_LISTS, "");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            APPOINTMENT_LIST = sharedPreferences.getString(HCConstants.PREF_APPOINTMENT_LISTS, "");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            APPOINTMENT_LIST = sharedPreferences.getString(HCConstants.PREF_APPOINTMENT_LISTS, "");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        Log.d(Utils.TAG , " *********** Search Appointments ****************");
        Log.d(Utils.TAG +" LoginType: ", Utils.USER_LOGIN_TYPE);
        Log.d(Utils.TAG +" UserId: ", String.valueOf(Utils.USER_LOGIN_ID));
        Log.d(Utils.TAG +" HOSPITAL_ID:", String.valueOf(HOSPITAL_ID));

        initializationViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               // InputMethodManager mImm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
               // mImm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                hideKeyboard(SearchAppointmentsActivity.this);
                finish();
                break;
        }
        return true;
    }

    private void hideKeyboard(SearchAppointmentsActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void initializationViews() {
        appointmentArryList = new ArrayList<>();


        _header_seach_autotext = (AutoCompleteTextView) findViewById(R.id.search_autocompletetext);
        _header_search_cancel = (ImageView) findViewById(R.id.search_cancel);
        _header_search_cancel.setOnClickListener(this);
        _header_search_cancel.setVisibility(View.GONE);


        recyclerView_appoint = (RecyclerView) findViewById(R.id.search_recycleriew);
        no_data = (CustomTextViewItalic) findViewById(R.id.search_noresult);
        no_data.setVisibility(View.GONE);

        apptAdapter = new AppointmentAdapter(SearchAppointmentsActivity.this, appointmentArryList,1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SearchAppointmentsActivity.this);
        recyclerView_appoint.setLayoutManager(mLayoutManager);
        recyclerView_appoint.setItemAnimator(new DefaultItemAnimator());
        recyclerView_appoint.setAdapter(apptAdapter);

        gson = new Gson();
        if (APPOINTMENT_LIST.equals("")) {

        } else {
            appointmentArryList = gson.fromJson(APPOINTMENT_LIST, new TypeToken<List<AppointmentList>>() {
            }.getType());

        }

        _header_seach_autotext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    _header_search_cancel.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    String search_result = _header_seach_autotext.getText().toString();
                    collectAppointmentSerachResults(search_result);
                    return true;
                }
                return false;
            }
        });

        _header_seach_autotext.setThreshold(1);//will start working from first character
        _header_seach_autotext.setTextColor(getResources().getColor(R.color.colorPrimary));
        final AppointmentSeachAdapter adapter = new AppointmentSeachAdapter(SearchAppointmentsActivity.this, R.layout.fragment_appointments, R.id.lbl_name, appointmentArryList);
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

                apptAdapter = new AppointmentAdapter(SearchAppointmentsActivity.this, appointmentArryList,1);
                recyclerView_appoint.setAdapter(apptAdapter);
                apptAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void collectAppointmentSerachResults(final String SEARCH_QUERY) {

        appointmentArryList = new ArrayList<>();
        appointmentArryList.clear();
        apptAdapter.notifyDataSetChanged();

        final ProgressDialog progressDialog4 = new ProgressDialog(SearchAppointmentsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog4.setIndeterminate(true);
        progressDialog4.setMessage("Searching Result...");
        progressDialog4.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_APPOINTMENTS_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "search appt: "+ response.toString());
                        if (response != null) {
                            //   GetJsonArrayResponseSearch(response);

                            try {
                                appointmentArryList = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");

                                if (staus_res.equals("false")) {
                                    recyclerView_appoint.setVisibility(View.GONE);
                                    no_data.setVisibility(View.VISIBLE);
                                    no_data.setText("No appointments !!!");
                                    AppUtils.showCustomAlertMessage(SearchAppointmentsActivity.this, "Appointments","No appointments set yet !!!", "OK", null, null);
                                } else {
                                    JSONArray jsonArray = null;
                                    String status = null;
                                    try {
                                        jsonArray = jsonObject.getJSONArray("appointment_details");
                                        if (jsonArray.length() == 0) {
                                            recyclerView_appoint.setVisibility(View.GONE);
                                            no_data.setVisibility(View.VISIBLE);
                                            no_data.setText("No appointments !!!");
                                        }
                                        else if (jsonArray.length() > 0) {
                                            no_data.setVisibility(View.GONE);
                                            recyclerView_appoint.setVisibility(View.VISIBLE);
                                            for (int i = 0; i < jsonArray.length(); i++) {

                                             /*   appointmemts = new AppointmentList(jsonArray.getJSONObject(i).getInt("App_ID"), jsonArray.getJSONObject(i).getString("Trans_ID"),jsonArray.getJSONObject(i).getInt("Pref_Doc"),jsonArray.getJSONObject(i).getInt("Dept"),
                                                        jsonArray.getJSONObject(i).getString("Visit_Date"),jsonArray.getJSONObject(i).getString("Visit_Timings"), jsonArray.getJSONObject(i).getString("Patient_name"), jsonArray.getJSONObject(i).getString("Mobile"),
                                                        jsonArray.getJSONObject(i).getString("Email"),jsonArray.getJSONObject(i).getString("Pay_Status"),jsonArray.getJSONObject(i).getString("Visit_Status"),USER_ID,USER_LOGIN_TYPE,jsonArray.getJSONObject(i).getString("ref_name"));
                                                appointmentArryList.add(appointmemts);*/

                                                appointmentArryList.add(new AppointmentList(jsonArray.getJSONObject(i).getInt("token_id"),
                                                        jsonArray.getJSONObject(i).getInt("token_no"),jsonArray.getJSONObject(i).getInt("patient_id"),
                                                        jsonArray.getJSONObject(i).getString("appoint_trans_id"),jsonArray.getJSONObject(i).getString("patient_name"),
                                                        jsonArray.getJSONObject(i).getInt("doc_id"),jsonArray.getJSONObject(i).getInt("doc_type"),
                                                        jsonArray.getJSONObject(i).getInt("hosp_id"),jsonArray.getJSONObject(i).getString("status"),
                                                        jsonArray.getJSONObject(i).getString("app_date"),jsonArray.getJSONObject(i).getString("app_time"),
                                                        jsonArray.getJSONObject(i).getString("created_date"),jsonArray.getJSONObject(i).getString("patient_email"),
                                                        jsonArray.getJSONObject(i).getString("patient_mob")));

                                            }

                                            apptAdapter = new AppointmentAdapter(SearchAppointmentsActivity.this, appointmentArryList,1);
                                            recyclerView_appoint.setAdapter(apptAdapter);
                                            apptAdapter.notifyDataSetChanged();
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
                map.put(APIClass.KEY_HOSPITAL_ID, String.valueOf(HOSPITAL_ID));
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(SearchAppointmentsActivity.this).
                getRequestQueue();
        AppController.getInstance(SearchAppointmentsActivity.this).addToRequestQueue(stringRequest);

    }
}
