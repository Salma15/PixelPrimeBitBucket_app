package com.medisensehealth.fdccontributor.activities.appointments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
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
import com.medisensehealth.fdccontributor.DataModel.AppointmentList;
import com.medisensehealth.fdccontributor.DataModel.HospitalList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.adapter.appointments.AppointmentAdapter;
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
 * Created by HP on 20-07-2018.
 */

public class AppointmentsNewActivity extends AppCompatActivity implements View.OnClickListener {
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID, HOSPITAL_ID = 0;;
    String USER_NAME;
    String USER_LOGIN_TYPE, APPOINTMENT_LIST, DOC_HOSIPTAL_LIST;
    ArrayList<HospitalList> hospitalDocArraylist;

    ImageView _header_filter, _header_create;
    LinearLayout _header_sub_search_layout;

    private AppointmentAdapter apptAdapter;
    private List<AppointmentList> appointmentArryList = new ArrayList<>();
    Calendar myCalendar;
    RecyclerView recyclerView_appoint;
    CustomTextViewItalic no_data;
    SwipyRefreshLayout swipeRefreshLayout_appoint;
    ImageView fab_refresh;
    CustomTextView choose_date_from, choose_date_to;
    int filter_selType = 0;
    String get_appoint_date_from ="", get_appoint_date_to = "";


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
        setContentView(R.layout.fragment_appointments);

        hospitalDocArraylist = new ArrayList<>();
        HOSPITAL_ID = 0;
        appointmentArryList = new ArrayList<>();
        myCalendar = Calendar.getInstance();
        filter_selType = 0;      // 1=Upcoming, 2=All,3=Range in between dates

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);
        }

        Log.d(Utils.TAG , " *********** Appointments ****************");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Utils.TAG , " ** onResume **");

        shareadPreferenceClass = new ShareadPreferenceClass(this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(this);

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            APPOINTMENT_LIST = sharedPreferences.getString(HCConstants.PREF_APPOINTMENT_LISTS, "");
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            APPOINTMENT_LIST = sharedPreferences.getString(HCConstants.PREF_APPOINTMENT_LISTS, "");
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {

            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            APPOINTMENT_LIST = sharedPreferences.getString(HCConstants.PREF_APPOINTMENT_LISTS, "");
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }


        Log.d(Utils.TAG +" LOGIN_ID: ", String.valueOf(Utils.USER_LOGIN_ID));
        Log.d(Utils.TAG +" LOGIN_TYPE: ", Utils.USER_LOGIN_TYPE);
        Log.d(Utils.TAG +" HOSPITAL_ID:", String.valueOf(HOSPITAL_ID));

        initializationViews();
    }

    private void initializationViews() {

        // Header Tabs
        _header_filter = (ImageView) findViewById(R.id.header_sub_filter);
        _header_filter.setOnClickListener(this);
        _header_create = (ImageView) findViewById(R.id.header_sub_create);
       // _header_create.setOnClickListener(this);
        _header_create.setVisibility(View.GONE);
        _header_sub_search_layout = (LinearLayout) findViewById(R.id.header_sub_layout);
        _header_sub_search_layout.setOnClickListener(this);

        recyclerView_appoint = (RecyclerView) findViewById(R.id.appoint_recycleriew);
        no_data = (CustomTextViewItalic) findViewById(R.id.appoint_novalue);
        swipeRefreshLayout_appoint = (SwipyRefreshLayout) findViewById(R.id.appoint_swipeRefreshLayout);

        Spanned spanned = Html.fromHtml(
                "Today, no appointments are scheduled !!! <br> Tap <img src='" + getResources().getDrawable(R.mipmap.filter_icon_primary) + "'/> icon at the top of your screen to view all/upcoming appointments", imageGetter, null);
        no_data.setText(spanned);


        fab_refresh = (ImageView) findViewById(R.id.appoint_refresh);
        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtil.getConnectivityStatusString(AppointmentsNewActivity.this).equalsIgnoreCase("enabled")) {
                    collectAppointmentDetails();
                } else {
                    AppUtils.showCustomAlertMessage(AppointmentsNewActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
            }
        });

        swipeRefreshLayout_appoint.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                swipeRefreshLayout_appoint.setRefreshing(false);
            }
        });

        Gson gson = new Gson();
        if (APPOINTMENT_LIST.equals("")) {
            Log.d(Utils.TAG, "APPOINTMENT_LIST EMPTY ");

            if (NetworkUtil.getConnectivityStatusString(AppointmentsNewActivity.this).equalsIgnoreCase("enabled")) {
                collectAppointmentDetails();
            } else {
                AppUtils.showCustomAlertMessage(AppointmentsNewActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            appointmentArryList = gson.fromJson(APPOINTMENT_LIST, new TypeToken<List<AppointmentList>>() {
            }.getType());
            if(appointmentArryList.size() > 0 ) {
                Log.d(Utils.TAG, "APPOINTMENT_LIST > 0 " + appointmentArryList.size());
                recyclerView_appoint.setVisibility(View.VISIBLE);
                no_data.setVisibility(View.GONE);
                apptAdapter = new AppointmentAdapter(AppointmentsNewActivity.this, appointmentArryList, 0);
                recyclerView_appoint.setAdapter(apptAdapter);
                apptAdapter.notifyDataSetChanged();
            }
            else {
                recyclerView_appoint.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
            }
        }

        apptAdapter = new AppointmentAdapter(AppointmentsNewActivity.this, appointmentArryList, 0);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AppointmentsNewActivity.this);
        recyclerView_appoint.setLayoutManager(mLayoutManager);
        recyclerView_appoint.setItemAnimator(new DefaultItemAnimator());
        recyclerView_appoint.setAdapter(apptAdapter);
        apptAdapter.notifyDataSetChanged();

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
                if (NetworkUtil.getConnectivityStatusString(AppointmentsNewActivity.this).equalsIgnoreCase("enabled")) {
                    Intent i1 = new Intent(AppointmentsNewActivity.this, AppointmentCreateNewActivity.class);
                    i1.putExtra("title","Book Appointment");
                    startActivity(i1);
                } else {
                    AppUtils.showCustomAlertMessage(AppointmentsNewActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
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
        final Dialog dialog = new Dialog(AppointmentsNewActivity.this, R.style.CustomDialog);
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
            RadioButton rb=new RadioButton(AppointmentsNewActivity.this); // dynamically creating RadioButton and adding to RadioGroup.
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

                if (NetworkUtil.getConnectivityStatusString(AppointmentsNewActivity.this).equalsIgnoreCase("enabled")) {
                    collectAppointmentDetails();
                } else {
                    AppUtils.showCustomAlertMessage(AppointmentsNewActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }

            }
        });

        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_sub_filter:
                 customAppointmentFilter();
                break;
            case R.id.header_sub_create:
                if (NetworkUtil.getConnectivityStatusString(AppointmentsNewActivity.this).equalsIgnoreCase("enabled")) {
                    Intent i1 = new Intent(AppointmentsNewActivity.this, AppointmentCreateNewActivity.class);
                    i1.putExtra("title","Book Appointment");
                    startActivity(i1);
                } else {
                    AppUtils.showCustomAlertMessage(AppointmentsNewActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
                break;
            case R.id.header_sub_layout:
                Intent i2 = new Intent(AppointmentsNewActivity.this, SearchAppointmentsActivity.class);
                i2.putExtra("title","Search");
                startActivity(i2);
                break;
        }
    }

    private void collectAppointmentDetails() {
        appointmentArryList = new ArrayList<>();

        final ProgressDialog progressDialog2 = new ProgressDialog(AppointmentsNewActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog2.setIndeterminate(true);
        progressDialog2.setMessage("Loading Appointments...");
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
                                        recyclerView_appoint.setVisibility(View.VISIBLE);
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
                                        apptAdapter = new AppointmentAdapter(AppointmentsNewActivity.this, appointmentArryList,0);
                                        recyclerView_appoint.setAdapter(apptAdapter);
                                        apptAdapter.notifyDataSetChanged();

                                        //Set the values
                                        Gson gson = new Gson();
                                        String jsonText = gson.toJson(appointmentArryList);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearAppointmentLists();
                                            shareadPreferenceClass.setAppointmentList(jsonText);
                                        }
                                    }
                                    else {
                                        recyclerView_appoint.setVisibility(View.GONE);
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

        RequestQueue requestQueue = AppController.getInstance(AppointmentsNewActivity.this).
                getRequestQueue();
        AppController.getInstance(AppointmentsNewActivity.this).addToRequestQueue(stringRequest);
    }

    private void customAppointmentFilter() {
        final Dialog dialog = new Dialog(AppointmentsNewActivity.this, R.style.CustomDialog);
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
                submitAppointmetFilterToServer(1,"","");        // 1=Upcoming, 2=All,3=Range in between dates
            }
        });

        all_appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(), formattedDate , Toast.LENGTH_SHORT ).show();
                dialog.dismiss();
                filter_selType = 1;
                submitAppointmetFilterToServer(2,"","");         // 1=Upcoming, 2=All,3=Range in between dates
            }
        });

        choose_date_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_date_from.setText("");
                filter_selType = 3;
                new DatePickerDialog(AppointmentsNewActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        choose_date_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_date_to.setText("");
                filter_selType = 3;
                new DatePickerDialog(AppointmentsNewActivity.this, dateTill, myCalendar
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
                    Toast.makeText(AppointmentsNewActivity.this, "Select from date" , Toast.LENGTH_SHORT ).show();
                }
                else if(get_appoint_date_to.equals("")) {
                    Toast.makeText(AppointmentsNewActivity.this, "Select till date" , Toast.LENGTH_SHORT ).show();
                }
                else  if (convertedDate2.after(convertedDate)) {
                    Toast.makeText(AppointmentsNewActivity.this, "Select valid dates" , Toast.LENGTH_SHORT ).show();
                }
                else {
                    submitAppointmetFilterToServer(3,get_appoint_date_from,get_appoint_date_to);
                    dialog.dismiss();
                }

            }
        });

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

    private void submitAppointmetFilterToServer(final int filter_type, final String get_appoint_date_from, final String get_appoint_date_to) {
        // filter_type = 1 -> Upcoming, 2->All, 3->Filter Range
        final ProgressDialog progressDialog3 = new ProgressDialog(AppointmentsNewActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog3.setIndeterminate(true);
        progressDialog3.setMessage("Loading appointments...");
        progressDialog3.show();

        appointmentArryList = new ArrayList<>();

        int socketTimeout = 20000; // 20 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_APPOINT_FILTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, " apptFilter: " + response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("appointment_details");
                                    if (jsonArray.length() > 0) {
                                        recyclerView_appoint.setVisibility(View.VISIBLE);
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
                                        apptAdapter = new AppointmentAdapter(AppointmentsNewActivity.this, appointmentArryList,1);
                                        recyclerView_appoint.setAdapter(apptAdapter);
                                        apptAdapter.notifyDataSetChanged();

                                    }
                                    else {
                                        recyclerView_appoint.setVisibility(View.GONE);
                                        no_data.setVisibility(View.VISIBLE);
                                        no_data.setText("No appointments are scheduled !!!");
                                    }
                                }
                                progressDialog3.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog3.dismiss();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog3.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(Utils.USER_LOGIN_ID));
                map.put(APIClass.KEY_LOGINTYPE, Utils.USER_LOGIN_TYPE);
                map.put(APIClass.KEY_HOSPITAL_ID, String.valueOf(HOSPITAL_ID));
                map.put(APIClass.KEY_APPOINT_FILTERTYPE, String.valueOf(filter_type));

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

        RequestQueue requestQueue = AppController.getInstance(AppointmentsNewActivity.this).
                getRequestQueue();
        AppController.getInstance(AppointmentsNewActivity.this).addToRequestQueue(stringRequest);

    }
}
