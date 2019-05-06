package com.medisensehealth.fdccontributor.activities.waitingRoom;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medisensehealth.fdccontributor.DataModel.AppointmentList;
import com.medisensehealth.fdccontributor.DataModel.HospitalList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.adapter.waitingRoom.WaitingRoomAdapter;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextViewItalicBold;
import com.medisensehealth.fdccontributor.views.CustomTextViewSemiBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 24-07-2018.
 */

public class WaitingRoomActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    int USER_ID,HOSPITAL_ID;
    String USER_NAME, USER_LOGIN_TYPE, APPOINTMENT_LIST, DOC_HOSIPTAL_LIST;
    ArrayList<HospitalList> hospitalDocArraylist;

    CustomTextViewSemiBold current_date_text;
    private SwipeRefreshLayout swipeContainer;
    private WaitingRoomAdapter mAdapter;
    private List<AppointmentList> appointmentArryList = new ArrayList<>();
    private RecyclerView recyclerView_waiting;
    CustomTextViewItalicBold no_data;
    ImageView refresh_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitingroom);

        hospitalDocArraylist = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle b = getIntent().getExtras();
        if( b != null) {
            String title = b.getString("title");
            setTitle(title);
        }

        shareadPreferenceClass = new ShareadPreferenceClass(WaitingRoomActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(WaitingRoomActivity.this);

        shareadPreferenceClass = new ShareadPreferenceClass(WaitingRoomActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(WaitingRoomActivity.this);

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
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
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
            APPOINTMENT_LIST = sharedPreferences.getString(HCConstants.PREF_APPOINTMENT_LISTS, "");
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        Log.d(Utils.TAG, " ******************** WaitingRoomActivity ******************** ");
        Log.d(Utils.TAG+" USER_ID: ", String.valueOf(USER_ID));
        Log.d(Utils.TAG+" LOG_TYPE: ", USER_LOGIN_TYPE);
        Log.d(Utils.TAG+" HOSPITAL_ID: ", String.valueOf(HOSPITAL_ID));

        initializationView();
    }

    private void initializationView() {

        appointmentArryList = new ArrayList<>();

        current_date_text = (CustomTextViewSemiBold) findViewById(R.id.waiting_currentdate);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        current_date_text.setText(formattedDate);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.waiting_swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(true);
                onRefreshClear();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recyclerView_waiting = (RecyclerView) findViewById(R.id.waiting_recycler);
        no_data = (CustomTextViewItalicBold)  findViewById(R.id.waiting_nodata);
        refresh_btn = (ImageView)  findViewById(R.id.waiting_refresh);

        mAdapter = new WaitingRoomAdapter(WaitingRoomActivity.this, appointmentArryList);
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView_waiting.setLayoutManager(manager);
        recyclerView_waiting.setItemAnimator(new DefaultItemAnimator());
        recyclerView_waiting.setAdapter(mAdapter);


        Gson gson = new Gson();
        if (APPOINTMENT_LIST.equals("")) {
            if (NetworkUtil.getConnectivityStatusString(WaitingRoomActivity.this).equalsIgnoreCase("enabled")) {
                collectAppointmentDetails();
            } else {
                AppUtils.showCustomAlertMessage(WaitingRoomActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }
        } else {
            appointmentArryList = gson.fromJson(APPOINTMENT_LIST, new TypeToken<List<AppointmentList>>() {
            }.getType());
            if(appointmentArryList.size() > 0 ) {
                recyclerView_waiting.setVisibility(View.VISIBLE);
                no_data.setVisibility(View.GONE);
                mAdapter = new WaitingRoomAdapter(WaitingRoomActivity.this, appointmentArryList);
                recyclerView_waiting.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
            else {
                recyclerView_waiting.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
            }
        }

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sharedPreferences != null) {
                    HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
                    Log.d(Utils.TAG+" HOSPITAL_ID:", String.valueOf(HOSPITAL_ID));
                }

                if (NetworkUtil.getConnectivityStatusString(WaitingRoomActivity.this).equalsIgnoreCase("enabled")) {
                    collectAppointmentDetails();
                } else {
                    AppUtils.showCustomAlertMessage(WaitingRoomActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
            }
        });
    }

    private void onRefreshClear() {
        swipeContainer.setRefreshing(false);
    }

    private void collectAppointmentDetails() {
        appointmentArryList = new ArrayList<>();

        final ProgressDialog progressDialog2 = new ProgressDialog(WaitingRoomActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog2.setIndeterminate(true);
        progressDialog2.setMessage("Loading....");
        progressDialog2.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_APPOINT_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "appt list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("appointment_today_details");
                                    if (jsonArray.length() > 0) {
                                        recyclerView_waiting.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            appointmentArryList.add(new AppointmentList(jsonArray.getJSONObject(i).getInt("token_id"),
                                                    jsonArray.getJSONObject(i).getInt("token_no"),jsonArray.getJSONObject(i).getInt("patient_id"),
                                                    jsonArray.getJSONObject(i).getString("appoint_trans_id"),jsonArray.getJSONObject(i).getString("patient_name"),
                                                    jsonArray.getJSONObject(i).getInt("doc_id"),jsonArray.getJSONObject(i).getInt("doc_type"),
                                                    jsonArray.getJSONObject(i).getInt("hosp_id"),jsonArray.getJSONObject(i).getString("status"),
                                                    jsonArray.getJSONObject(i).getString("app_date"),jsonArray.getJSONObject(i).getString("app_time"),
                                                    jsonArray.getJSONObject(i).getString("created_date"),jsonArray.getJSONObject(i).getString("patient_email"),
                                                    jsonArray.getJSONObject(i).getString("patient_mob")));
                                        }
                                        mAdapter = new WaitingRoomAdapter(WaitingRoomActivity.this, appointmentArryList);
                                        recyclerView_waiting.setAdapter(mAdapter);
                                        mAdapter.notifyDataSetChanged();

                                        //Set the values
                                        Gson gson = new Gson();
                                        String jsonText = gson.toJson(appointmentArryList);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearAppointmentLists();
                                            shareadPreferenceClass.setAppointmentList(jsonText);
                                        }
                                    }
                                    else {
                                        recyclerView_waiting.setVisibility(View.GONE);
                                        no_data.setVisibility(View.VISIBLE);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearAppointmentLists();
                                        }
                                    }
                                }
                                progressDialog2.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog2.dismiss();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog2.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(Utils.USER_LOGIN_ID));
                map.put(APIClass.KEY_LOGINTYPE, Utils.USER_LOGIN_TYPE);
                map.put(APIClass.KEY_HOSPITAL_ID, String.valueOf(HOSPITAL_ID));
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(WaitingRoomActivity.this).
                getRequestQueue();
        AppController.getInstance(WaitingRoomActivity.this).addToRequestQueue(stringRequest);
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
        final Dialog dialog = new Dialog(WaitingRoomActivity.this, R.style.CustomDialog);
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
            RadioButton rb=new RadioButton(WaitingRoomActivity.this); // dynamically creating RadioButton and adding to RadioGroup.
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

                dialog.dismiss();

                if (NetworkUtil.getConnectivityStatusString(WaitingRoomActivity.this).equalsIgnoreCase("enabled")) {
                    collectAppointmentDetails();
                } else {
                    AppUtils.showCustomAlertMessage(WaitingRoomActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }

            }
        });

        dialog.show();
    }

}
