package com.medisensehealth.fdccontributor.activities.emr;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import com.medisensehealth.fdccontributor.DataModel.HospitalList;
import com.medisensehealth.fdccontributor.DataModel.MyPatientsList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.adapter.emr.MyPatientsAdapter;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by HP on 24-07-2018.
 */

public class EMRActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView _header_filter, _header_create;
    LinearLayout _header_sub_search_layout;

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID, HOSPITAL_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE, MYPATIENT_LIST, MYPATIENT_COUNT, APPOINTMENT_LIST, DOC_HOSIPTAL_LIST;
    ArrayList<HospitalList> hospitalDocArraylist;

    SwipyRefreshLayout swipeRefreshLayout_mypatient;
    ImageView fab_refresh;
    RecyclerView recyclerView;
    MyPatientsAdapter adapter;
    CustomTextViewItalic no_data;
    List<MyPatientsList> patientsListArraylist = new ArrayList<>();

    CustomTextView choose_date_from, choose_date_to;
    int filter_selType = 0;
    String get_appoint_date_from ="", get_appoint_date_to = "";
    Calendar myCalendar;
    int view_more = 0;
    int filter_type = 0;

    Html.ImageGetter imageGetter = new Html.ImageGetter() {

        @Override
        public Drawable getDrawable(String source) {
            Drawable d = getResources().getDrawable(R.mipmap.filter_icon_primary);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            return d;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_emr);

        hospitalDocArraylist = new ArrayList<>();
        patientsListArraylist = new ArrayList<>();
        myCalendar = Calendar.getInstance();
        filter_selType = 0;      // 1=Upcoming, 2=All,3=Range in between dates
        view_more = 0;           // 0 - initial load, 1- load all data
        filter_type = 0;         // 0 - Normal, 1- Upcoming, 2 - All, 3 - Range of dates

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);
            Log.d(Utils.TAG, " ************ EMRActivity *********** ");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Utils.TAG, " On Resume");

        shareadPreferenceClass = new ShareadPreferenceClass(EMRActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(EMRActivity.this);

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            MYPATIENT_LIST = sharedPreferences.getString(HCConstants.PREF_MYPATIENT_LISTS, "");
            MYPATIENT_COUNT = sharedPreferences.getString(HCConstants.PREF_COUNT_LISTS_MYPATIENT, "0");
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
            MYPATIENT_LIST = sharedPreferences.getString(HCConstants.PREF_MYPATIENT_LISTS, "");
            MYPATIENT_COUNT = sharedPreferences.getString(HCConstants.PREF_COUNT_LISTS_MYPATIENT, "0");
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
            MYPATIENT_LIST = sharedPreferences.getString(HCConstants.PREF_MYPATIENT_LISTS, "");
            MYPATIENT_COUNT = sharedPreferences.getString(HCConstants.PREF_COUNT_LISTS_MYPATIENT, "0");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
            APPOINTMENT_LIST = sharedPreferences.getString(HCConstants.PREF_APPOINTMENT_LISTS, "");
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        Log.d(Utils.TAG+" LoginType: ", Utils.USER_LOGIN_TYPE);
        Log.d(Utils.TAG+" LoginId: ", String.valueOf(Utils.USER_LOGIN_ID));
        Log.d(Utils.TAG+" HOSPITAL_ID:", String.valueOf(HOSPITAL_ID));

        initializationViews();
    }

    private void initializationViews() {

        // Header Tabs
        _header_filter = (ImageView) findViewById(R.id.header_sub_filter);
        _header_filter.setOnClickListener(this);
        _header_create = (ImageView) findViewById(R.id.header_sub_create);
        _header_create.setVisibility(View.GONE);
        _header_sub_search_layout = (LinearLayout) findViewById(R.id.header_sub_layout);
        _header_sub_search_layout.setOnClickListener(this);

        no_data = (CustomTextViewItalic) findViewById(R.id.emr_norefer);
        recyclerView = (RecyclerView) findViewById(R.id.emr_recyclerview);
        swipeRefreshLayout_mypatient = (SwipyRefreshLayout) findViewById(R.id.emr_swipeRefreshLayout);
        fab_refresh = (ImageView) findViewById(R.id.emr_refresh);
        fab_refresh.setOnClickListener(this);

        adapter = new MyPatientsAdapter(EMRActivity.this, patientsListArraylist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(EMRActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        Spanned spanned = Html.fromHtml(
                "As there are no appointments today no records are preloaded. If you want to search for a patient click on search above or use <img src='" + getResources().getDrawable(R.mipmap.filter_icon_primary) + "'/> to see all patient records. ", imageGetter, null);
        no_data.setText(spanned);



        Gson gson = new Gson();

        if (MYPATIENT_LIST.equals("")) {
            Log.d(Utils.TAG, "MYPATIENT_LIST EMPTY ");

            if (NetworkUtil.getConnectivityStatusString(EMRActivity.this).equalsIgnoreCase("enabled")) {
                view_more = 0;
                filter_type = 0;
                collectMyPatientDetails(view_more, filter_type,"","");
            } else {
                AppUtils.showCustomAlertMessage(EMRActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            patientsListArraylist = gson.fromJson(MYPATIENT_LIST, new TypeToken<List<MyPatientsList>>() {
            }.getType());
            Log.d(Utils.TAG, "MYPATIENT_LIST > 0 ");
            if(patientsListArraylist.size() > 0 ) {
                adapter = new MyPatientsAdapter(EMRActivity.this, patientsListArraylist);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            else {
                recyclerView.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
            }
        }

        swipeRefreshLayout_mypatient.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                Log.d(Utils.TAG, "Refresh triggered at "
                        + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));

                if(direction == SwipyRefreshLayoutDirection.TOP) {
                    if (NetworkUtil.getConnectivityStatusString(EMRActivity.this).equalsIgnoreCase("enabled")) {
                        view_more = 0;
                      //  filter_type = 0;
                        collectMyPatientDetails(view_more, filter_type,"","");
                        swipeRefreshLayout_mypatient.setRefreshing(false);
                    } else {
                        AppUtils.showCustomAlertMessage(EMRActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                    }
                }
                else {

                  //  Toast.makeText(EMRActivity.this, "Load All", Toast.LENGTH_SHORT).show();
                    if (NetworkUtil.getConnectivityStatusString(EMRActivity.this).equalsIgnoreCase("enabled")) {
                        view_more = 1;
                        collectMyPatientDetails(view_more, filter_type,"","");
                        swipeRefreshLayout_mypatient.setRefreshing(false);
                    } else {
                        AppUtils.showCustomAlertMessage(EMRActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_emr, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
            case R.id.item_emr_swich_user:
                //  Toast.makeText(getApplicationContext(),"Switch Hospital",Toast.LENGTH_LONG).show();
                if(sharedPreferences != null) {
                    HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
                }
                showRadioButtonDialog();
                break;
            case R.id.item_emr_add:
                Intent i1 = new Intent(EMRActivity.this, AddNewPatientActivity.class);
                i1.putExtra("title","Add New Patient");
                startActivity(i1);
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
        final Dialog dialog = new Dialog(EMRActivity.this, R.style.CustomDialog);
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
            RadioButton rb=new RadioButton(EMRActivity.this); // dynamically creating RadioButton and adding to RadioGroup.
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

            }
        });

        dialog.show();
    }

    private void collectMyPatientDetails(final int views_more, final int filters_type, String from_date, String to_date) {
        patientsListArraylist = new ArrayList<>();

        Log.d(Utils.TAG, " views_more: "+ views_more);
        Log.d(Utils.TAG, " filters_type: "+ filters_type);

        final ProgressDialog progressDialog = new ProgressDialog(EMRActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading EMR lists...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_EMR_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "my list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    recyclerView.setVisibility(View.GONE);
                                    no_data.setVisibility(View.VISIBLE);
                                    jsonArray = jsonObject.getJSONArray("mypatient_details");
                                    if (jsonArray.length() > 0) {
                                        recyclerView.setVisibility(View.VISIBLE);
                                        no_data.setVisibility(View.GONE);
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            patientsListArraylist.add(new MyPatientsList(jsonArray.getJSONObject(i).getInt("patient_id"), jsonArray.getJSONObject(i).getString("patient_name"),jsonArray.getJSONObject(i).getString("patient_age"),jsonArray.getJSONObject(i).getString("patient_email"),
                                                    jsonArray.getJSONObject(i).getInt("patient_gen"),jsonArray.getJSONObject(i).getString("weight"),
                                                    jsonArray.getJSONObject(i).getInt("hyper_cond"),jsonArray.getJSONObject(i).getInt("diabetes_cond"),jsonArray.getJSONObject(i).getString("smoking"),
                                                    jsonArray.getJSONObject(i).getString("alcoholic"),jsonArray.getJSONObject(i).getString("drug_abuse"),jsonArray.getJSONObject(i).getString("other_details"),
                                                    jsonArray.getJSONObject(i).getString("family_history"),jsonArray.getJSONObject(i).getString("prev_inter"),jsonArray.getJSONObject(i).getString("neuro_issue"),
                                                    jsonArray.getJSONObject(i).getString("kidney_issue"),jsonArray.getJSONObject(i).getString("patient_mob"),
                                                    jsonArray.getJSONObject(i).getString("patient_loc"), jsonArray.getJSONObject(i).getString("pat_state"), jsonArray.getJSONObject(i).getString("pat_country"),
                                                    jsonArray.getJSONObject(i).getString("patient_addrs"),jsonArray.getJSONObject(i).getString("TImestamp"),
                                                    jsonArray.getJSONObject(i).getString("user_id"), jsonArray.getJSONObject(i).getInt("doc_id"),
                                                    jsonArray.getJSONObject(i).getString("system_date"),USER_ID,USER_LOGIN_TYPE,
                                                    jsonArray.getJSONObject(i).getString("height")));

                                        }
                                        adapter = new MyPatientsAdapter(EMRActivity.this, patientsListArraylist);
                                        recyclerView.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();

                                        if(views_more == 0 && filters_type == 0) {
                                            //Set the values
                                            Gson gson = new Gson();
                                            String jsonText = gson.toJson(patientsListArraylist);
                                            if (sharedPreferences != null) {
                                                shareadPreferenceClass.clearMyPatientLists();
                                                shareadPreferenceClass.setMyPatientList(jsonText);
                                            }
                                        }

                                    }
                                    else {
                                        recyclerView.setVisibility(View.GONE);
                                        no_data.setVisibility(View.VISIBLE);

                                        if(views_more == 0 && filters_type == 0) {
                                            //Set the values
                                            Gson gson = new Gson();
                                            patientsListArraylist= new ArrayList<>();
                                            String jsonText = gson.toJson(patientsListArraylist);
                                            if (sharedPreferences != null) {
                                                shareadPreferenceClass.clearMyPatientLists();
                                                shareadPreferenceClass.setMyPatientList(jsonText);
                                            }
                                        }

                                    }
                                }
                                else {
                                    recyclerView.setVisibility(View.GONE);
                                    no_data.setVisibility(View.VISIBLE);
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
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(Utils.USER_LOGIN_ID));
                map.put(APIClass.KEY_LOGINTYPE, Utils.USER_LOGIN_TYPE);
                map.put(APIClass.KEY_VIEW_ALL, String.valueOf(views_more));
                map.put(APIClass.KEY_FILTER_TYPE, String.valueOf(filters_type));

                if(get_appoint_date_from != null){
                    map.put(APIClass.KEY_APPOINT_FROMDATE, get_appoint_date_from);
                }
                if(get_appoint_date_to != null){
                    map.put(APIClass.KEY_APPOINT_TODATE, get_appoint_date_to);
                }

                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(EMRActivity.this).
                getRequestQueue();
        AppController.getInstance(EMRActivity.this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_sub_filter:
                customMyPatientFilter();
                break;
            case R.id.emr_refresh:
                if (NetworkUtil.getConnectivityStatusString(EMRActivity.this).equalsIgnoreCase("enabled")) {
                    view_more = 0;
                    filter_type = 0;
                    collectMyPatientDetails(view_more, filter_type,"","");
                } else {
                    AppUtils.showCustomAlertMessage(EMRActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
                break;
            case R.id.header_sub_layout:
                Intent i2 = new Intent(EMRActivity.this, SearchMyPatientsActivity.class);
                i2.putExtra("title","Search Patients");
                startActivity(i2);
                break;
        }
    }

    private void customMyPatientFilter() {
        final Dialog dialog = new Dialog(EMRActivity.this, R.style.CustomDialog);
        dialog.setContentView(R.layout.appointment_filter);
        dialog.setCanceledOnTouchOutside(true);

        choose_date_from = (CustomTextView) dialog.findViewById(R.id.appt_filter_from);
        choose_date_to = (CustomTextView) dialog.findViewById(R.id.appt_filter_to);
        CustomTextViewSemiBold upcoming_appointments = (CustomTextViewSemiBold) dialog.findViewById(R.id.appt_filter_todays);
        CustomTextViewSemiBold all_appointments = (CustomTextViewSemiBold) dialog.findViewById(R.id.appt_filter_all);
        Button submit_filter =  (Button) dialog.findViewById(R.id.appt_submit_filter);

        upcoming_appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(), formattedDate , Toast.LENGTH_SHORT ).show();
                dialog.dismiss();
                filter_selType = 1;
                filter_type = 1;
                view_more = 0;
                collectMyPatientDetails(view_more,1,"","");        // 1=Upcoming, 2=All,3=Range in between dates
            }
        });

        all_appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(), formattedDate , Toast.LENGTH_SHORT ).show();
                dialog.dismiss();
                filter_selType = 1;
                filter_type = 2;
                view_more = 0;
                collectMyPatientDetails(view_more,2,"","");         // 1=Upcoming, 2=All,3=Range in between dates
            }
        });

        choose_date_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_date_from.setText("");
                filter_selType = 3;
                new DatePickerDialog(EMRActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        choose_date_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_date_to.setText("");
                filter_selType = 3;
                new DatePickerDialog(EMRActivity.this, dateTill, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submit_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date convertedDate = new Date();
                Date convertedDate2 = new Date();
                try {
                    convertedDate = dateFormat.parse(get_appoint_date_to);
                    convertedDate2 = dateFormat.parse(get_appoint_date_from);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if(get_appoint_date_from.equals("")) {
                    Toast.makeText(EMRActivity.this, "Select from date" , Toast.LENGTH_SHORT ).show();
                }
                else if(get_appoint_date_to.equals("")) {
                    Toast.makeText(EMRActivity.this, "Select till date" , Toast.LENGTH_SHORT ).show();
                }
                else  if (convertedDate2.after(convertedDate)) {
                    Toast.makeText(EMRActivity.this, "Select valid dates" , Toast.LENGTH_SHORT ).show();
                }
                else {
                    filter_type = 3;
                    view_more = 0;
                    collectMyPatientDetails(view_more,3,get_appoint_date_from,get_appoint_date_to);
                    dialog.dismiss();
                }

            }
        });

        dialog.show();

        dialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Calendar dareSelected = Calendar.getInstance();
            dareSelected.set(year, monthOfYear, dayOfMonth);
            Calendar currentDate = Calendar.getInstance();

            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            choose_date_from.setText(sdf.format(dareSelected.getTime()));

            String outFormat = "yyyy/MM/dd";
            SimpleDateFormat sdf_out = new SimpleDateFormat(outFormat, Locale.US);
            get_appoint_date_from = sdf_out.format(dareSelected.getTime());

            if(choose_date_to.getText().toString().equals("")) {
                choose_date_to.setText(sdf.format(dareSelected.getTime()));
                get_appoint_date_to = sdf_out.format(dareSelected.getTime());
            }

        }
    };

    DatePickerDialog.OnDateSetListener dateTill = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Calendar dareSelected = Calendar.getInstance();
            dareSelected.set(year, monthOfYear, dayOfMonth);
            Calendar currentDate = Calendar.getInstance();

            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            choose_date_to.setText(sdf.format(dareSelected.getTime()));

            String outFormat = "yyyy/MM/dd";
            SimpleDateFormat sdf_out = new SimpleDateFormat(outFormat, Locale.US);
            get_appoint_date_to = sdf_out.format(dareSelected.getTime());

        }
    };

}
