package com.medisensehealth.fdccontributor.activities.settings;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.hbb20.CountryCodePicker;
import com.medisensehealth.fdccontributor.DataModel.HospitalList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.adapter.doctors.CustomHospitalFilterAdapter;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.parser.JSONParser;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomEditText;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;
import com.medisensehealth.fdccontributor.views.CustomTextViewSemiBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 27-07-2018.
 */

public class HospitalListActivity extends AppCompatActivity implements View.OnClickListener {
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE, DOC_HOSIPTAL_LIST;

    ArrayList<HospitalList> hospitalDocArraylist;
    Gson gson = new Gson();
    ListView hospital_listview;
    SearchView hospital_searchView;
    CustomHospitalFilterAdapter hospitalAdapter;
    ImageView refresh_btn;

    String GET_COUNTRY_CODE, GET_COUNTRY_NAME, GET_COUNTRY_NAME_CODE, GET_STATE;
    Spinner state_spinner;
    String compareStateValue = "";
    boolean EDIT_HOSPITAL = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_diagosis_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);
        }

        shareadPreferenceClass = new ShareadPreferenceClass(HospitalListActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(HospitalListActivity.this);


        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        Log.d(Utils.TAG , " *********** HospitalListActivity ****************");
        Log.d(Utils.TAG +" loginType: ", Utils.USER_LOGIN_TYPE);
        Log.d(Utils.TAG +" UserId: ", String.valueOf(Utils.USER_LOGIN_ID));

        initializationViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_emr, menu);

        MenuItem shareItem = menu.findItem(R.id.item_emr_swich_user);
        shareItem.setVisible(false);

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
                break;
            case R.id.item_emr_add:
                customAddNewHospital();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    private void initializationViews() {
        hospitalDocArraylist = new ArrayList<>();
        EDIT_HOSPITAL = false;

        hospital_searchView = (SearchView) findViewById(R.id.searchview_diagnos);
        hospital_listview = (ListView) findViewById(R.id.diagno_list);
        hospitalAdapter = new CustomHospitalFilterAdapter(HospitalListActivity.this,
                R.layout.activity_listview, hospitalDocArraylist);
        hospital_listview.setAdapter(hospitalAdapter);

        refresh_btn = (ImageView)findViewById(R.id.diagno_refresh);
        refresh_btn.setOnClickListener(this);

        if (DOC_HOSIPTAL_LIST.equals("")) {
            if (NetworkUtil.getConnectivityStatusString(HospitalListActivity.this).equalsIgnoreCase("enabled")) {
                collectHospitalListFromServer();
            } else {
                AppUtils.showCustomAlertMessage(HospitalListActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }
        }
        else {
            Log.d(Utils.TAG, "DOC_HOSIPTAL_LIST NOT EMPTY ");
            hospitalDocArraylist = gson.fromJson(DOC_HOSIPTAL_LIST, new TypeToken<List<HospitalList>>() {
            }.getType());
            if(hospitalDocArraylist.size() > 0 ) {
                Log.d(Utils.TAG, "hospitalDocArraylist size: " + hospitalDocArraylist.size());
                prepareDocHospitals(hospitalDocArraylist);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.diagno_refresh:
                if (NetworkUtil.getConnectivityStatusString(HospitalListActivity.this).equalsIgnoreCase("enabled")) {
                    collectHospitalListFromServer();
                } else {
                    AppUtils.showCustomAlertMessage(HospitalListActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
                break;
        }
    }

    private void collectHospitalListFromServer() {
        hospitalDocArraylist = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(HospitalListActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Hospitals...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_SETTINGS_HOSP_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "hosp list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("doc_hospital");
                                    if (jsonArray.length() > 0) {

                                        for (int k = 0; k < jsonArray.length(); k++) {
                                            hospitalDocArraylist.add(new HospitalList(jsonArray.getJSONObject(k).getInt("hosp_id"),
                                                    jsonArray.getJSONObject(k).getString("hosp_name"),
                                                    jsonArray.getJSONObject(k).getString("hosp_addrs"),jsonArray.getJSONObject(k).getString("hosp_city"),
                                                    jsonArray.getJSONObject(k).getString("hosp_state"),jsonArray.getJSONObject(k).getString("hosp_country"),
                                                    jsonArray.getJSONObject(k).getInt("doc_id"),"1"));

                                        }

                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(hospitalDocArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearDocHoapitals();
                                            shareadPreferenceClass.setDocHoapitals(jsonDiagnoText);
                                        }

                                        prepareDocHospitals(hospitalDocArraylist);
                                    }
                                    else {
                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(hospitalDocArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearDocHoapitals();
                                            shareadPreferenceClass.setDocHoapitals(jsonDiagnoText);
                                        }
                                        prepareDocHospitals(hospitalDocArraylist);
                                    }
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
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(HospitalListActivity.this).
                getRequestQueue();
        AppController.getInstance(HospitalListActivity.this).addToRequestQueue(stringRequest);
    }

    private void prepareDocHospitals(ArrayList<HospitalList> hospitalDocArraylist) {

        hospitalAdapter = new CustomHospitalFilterAdapter(HospitalListActivity.this,
                R.layout.activity_listview, hospitalDocArraylist);
        hospital_listview.setAdapter(hospitalAdapter);
        hospital_listview.setTextFilterEnabled(true);

        hospital_searchView.setIconifiedByDefault(false);
        hospital_searchView.setQueryHint("Search Hospital");
        hospital_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                hospital_searchView.setQuery("", false);
                hospital_searchView.clearFocus();
                hospital_searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Utils.TAG, "SERACH: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    Log.d(Utils.TAG, "is empty " );
                    hospitalAdapter.filter(newText.toString());
                    // specialization_listview.clearTextFilter();
                } else {
                    //  specialization_listview.setFilterText(newText.toString());
                    hospitalAdapter.filter(newText.toString());
                }
                return true;
            }
        });

        hospital_searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                hospital_searchView.setQuery("", false);
                hospital_searchView.setIconified(false);
                hospital_searchView.clearFocus();
                return false;
            }
        });

        hospital_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final HospitalList myCustomAdapterItem = (HospitalList) parent.getItemAtPosition(position);
                hospital_searchView.setQuery("", false);
                hospital_searchView.setIconified(false);
                hospital_searchView.clearFocus();

                AlertDialog.Builder dialog1 = new AlertDialog.Builder(HospitalListActivity.this, R.style.CustomDialog);
                dialog1.setTitle("Hospital Information");
                dialog1.setMessage("\nName: " + myCustomAdapterItem.getHospitalName()
                        +"\n\nAddress: " + myCustomAdapterItem.getHospitalAddress()
                        +"\n\nCity: " + myCustomAdapterItem.getHospitalCity()
                        +"\n\nState: " + myCustomAdapterItem.getHospitalState()+"\n\nCountry: " + myCustomAdapterItem.getHospitalCountry());
                dialog1.setPositiveButton("Done!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                dialog1.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        prepareEditHospitalInfo(myCustomAdapterItem.getHospitalId(), myCustomAdapterItem.getHospitalName(),
                                myCustomAdapterItem.getHospitalAddress(),myCustomAdapterItem.getHospitalCity(),
                                myCustomAdapterItem.getHospitalState(), myCustomAdapterItem.getHospitalCountry());
                    }
                });

                final AlertDialog alert = dialog1.create();
                alert.show();
            }
        });

    }


    private void customAddNewHospital() {
        final Dialog dialog = new Dialog(HospitalListActivity.this, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_hospital);
        dialog.setTitle("Add Hospital");
        dialog.setCanceledOnTouchOutside(true);

        ImageView close_btn = (ImageView) dialog.findViewById(R.id.hosp_close);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        final CustomEditText _edt_hospname = (CustomEditText) dialog.findViewById(R.id.hosp_name);
        final CustomEditText _edt_hosp_email = (CustomEditText) dialog.findViewById(R.id.hosp_email);
        final CustomEditText _edt_hosp_mobile = (CustomEditText) dialog.findViewById(R.id.hosp_mobile);
        final CustomEditText _edt_hosp_address = (CustomEditText) dialog.findViewById(R.id.hosp_address);
        final CustomEditText _edt_hosp_city = (CustomEditText) dialog.findViewById(R.id.hosp_city);
        final CountryCodePicker ccp_country = (CountryCodePicker) dialog.findViewById(R.id.hosp_country);
        state_spinner = (Spinner)  dialog.findViewById(R.id.hosp_state_spinner);
        final CustomTextViewBold submit_btn = (CustomTextViewBold)dialog.findViewById(R.id.hosp_submit);

        ccp_country.setContentColor(R.color.white);
        ccp_country.setBackgroundColor(getResources().getColor(R.color.white));
        GET_COUNTRY_CODE = ccp_country.getDefaultCountryCodeWithPlus();
        GET_COUNTRY_NAME = ccp_country.getDefaultCountryName();
        GET_COUNTRY_NAME_CODE = ccp_country.getDefaultCountryNameCode();
        new getJsonState().execute(ccp_country.getDefaultCountryNameCode());
        ccp_country.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //  Toast.makeText(SignUpLeapActicity.this, "Updated " + ccp.getSelectedCountryName() +"code: "+ccp.getSelectedCountryNameCode() + "co: "+ccp.getSelectedCountryCode(), Toast.LENGTH_SHORT).show();
                GET_COUNTRY_CODE = ccp_country.getSelectedCountryCodeWithPlus();
                GET_COUNTRY_NAME = ccp_country.getSelectedCountryName();
                GET_COUNTRY_NAME_CODE = ccp_country.getSelectedCountryNameCode();
                new getJsonState().execute(ccp_country.getSelectedCountryNameCode());
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(submit_btn.getWindowToken(), 0);

                if(_edt_hospname.getText().toString().trim().equals("")) {
                    Toast.makeText(HospitalListActivity.this, "Enter hospital name !!!", Toast.LENGTH_SHORT).show();
                }
                else  if(_edt_hosp_email.getText().toString().trim().equals("")) {
                    Toast.makeText(HospitalListActivity.this, "Enter email address !!!", Toast.LENGTH_SHORT).show();
                }
                else  if((_edt_hosp_mobile.getText().toString().trim().equals("")) || (_edt_hosp_mobile.getText().toString().trim().length()<10)) {
                    Toast.makeText(HospitalListActivity.this, "Enter valid 10 digits mobile number !!!", Toast.LENGTH_SHORT).show();
                }
                else  if(_edt_hosp_address.getText().toString().trim().equals("")) {
                    Toast.makeText(HospitalListActivity.this, "Enter address !!!", Toast.LENGTH_SHORT).show();
                }
                else  if(_edt_hosp_city.getText().toString().trim().equals("")) {
                    Toast.makeText(HospitalListActivity.this, "Enter city !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.dismiss();
                    submitAddHospitalRequestToServer(_edt_hospname.getText().toString().trim(),_edt_hosp_email.getText().toString().trim(),
                            _edt_hosp_mobile.getText().toString().trim(),_edt_hosp_address.getText().toString().trim(),
                            _edt_hosp_city.getText().toString().trim(),GET_STATE, GET_COUNTRY_NAME);
                }
            }
        });

        dialog.show();
    }

    private void submitAddHospitalRequestToServer(final String hosp_name, final String hosp_email, final String hosp_mobile, final String hosp_address,
                                                  final String hosp_city, final String get_state, final String get_country_name) {
        new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;
            ProgressDialog progressDialog1;
            JSONArray jsonHospArray;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog1 = new ProgressDialog(HospitalListActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                progressDialog1.setIndeterminate(true);
                progressDialog1.setMessage("Please Wait....");
                progressDialog1.show();


            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {
                    JSONObject jsonObject = JSONParser.addHospital(hosp_name,hosp_email,hosp_mobile,hosp_address,
                            hosp_city, get_state,get_country_name,  USER_ID, USER_LOGIN_TYPE);

                    if (jsonObject != null) {
                        Log.e(Utils.TAG, " GET: " + jsonObject.getString("result"));
                        if(jsonObject.getString("result").equals("success")) {
                            status = true;
                            if (jsonObject.has("doc_hospital")) {
                                jsonHospArray = jsonObject.getJSONArray("doc_hospital");
                            }

                        }
                        else {
                            status = false;
                        }
                    }
                    // return true;
                } catch (JSONException e) {
                    Log.i(Utils.TAG, "Error : " + e.getLocalizedMessage());
                    return false;
                }
                return status;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (  progressDialog1 != null)
                    progressDialog1.dismiss();
                Log.e(Utils.TAG, "aBoolean: " + String.valueOf(aBoolean));
                if (aBoolean) {

                    if (sharedPreferences != null) {
                        shareadPreferenceClass.clearDocHoapitals();
                    }

                    try {
                        if(jsonHospArray.length() >0) {
                            hospitalDocArraylist = new ArrayList<>();
                            for (int k = 0; k < jsonHospArray.length(); k++) {
                                hospitalDocArraylist.add(new HospitalList(jsonHospArray.getJSONObject(k).getInt("hosp_id"),
                                        jsonHospArray.getJSONObject(k).getString("hosp_name"),
                                        jsonHospArray.getJSONObject(k).getString("hosp_addrs"),jsonHospArray.getJSONObject(k).getString("hosp_city"),
                                        jsonHospArray.getJSONObject(k).getString("hosp_state"),jsonHospArray.getJSONObject(k).getString("hosp_country"),
                                        jsonHospArray.getJSONObject(k).getInt("doc_id"),"1"));
                            }

                            if (sharedPreferences != null) {
                                Gson gson2 = new Gson();
                                String jsonText1 = gson2.toJson(hospitalDocArraylist);
                                shareadPreferenceClass.clearDocHoapitals();
                                shareadPreferenceClass.setDocHoapitals(jsonText1);
                            }

                            ArrayList<String> hospitalArray = new ArrayList<String>();
                            for (int i = 0; i < hospitalDocArraylist.size(); i++) {
                                hospitalArray.add(hospitalDocArraylist.get(i).getHospitalName());
                            }

                            prepareDocHospitals(hospitalDocArraylist);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HospitalListActivity.this, R.style.CustomDialog);
                    alertDialogBuilder.setMessage("Hospital Added successfully. ");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else {
                    Toast.makeText(HospitalListActivity.this, "Failed to add hospital !!!", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }

    class getJsonState extends AsyncTask<String,String,String> {
        String country_shortname;
        @Override
        protected String doInBackground(String... key) {
            String stateText = key[0];
            stateText = stateText.trim();
            country_shortname = stateText.trim();
            Log.d(Utils.TAG, "State selected1: " + stateText);
            stateText = stateText.replace(" ", "+");

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_STATE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG, response.toString());
                            if (response != null) {
                                JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null;
                                ArrayList<String> stateArray = new ArrayList<String>();
                                try {
                                    jsonObject1 = new JSONObject(response);
                                    String staus_res = jsonObject1.getString("status");
                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray1 = jsonObject1.getJSONArray("page_result");
                                        if (jsonArray1.length() > 0) {
                                            //  stateArray.add("--Select--");
                                            for (int i = 0; i < jsonArray1.length(); i++) {
                                                stateArray.add(jsonArray1.getJSONObject(i).getString("StateName"));
                                            }
                                        }


                                        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(HospitalListActivity.this, R.layout.spinner_text, stateArray );
                                        locationAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                                        state_spinner.setAdapter(locationAdapter);

                                        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view,
                                                                       int position, long id) {
                                                Log.d(Utils.TAG, "stateName: "+ (String) parent.getItemAtPosition(position));
                                                String get_state = (String) parent.getItemAtPosition(position);
                                                GET_STATE = (String) parent.getItemAtPosition(position);
                                            }
                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {
                                                GET_STATE = "--Select--";
                                            }
                                        });

                                        if (!compareStateValue.equals(null) &&  EDIT_HOSPITAL == true) {
                                            Log.d(Utils.TAG, " compareStateValue not null " + String.valueOf(compareStateValue));
                                            int spinnerPosition = locationAdapter.getPosition(compareStateValue);
                                            state_spinner.setSelection(spinnerPosition);
                                            GET_STATE = compareStateValue;
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                    map.put("shortname",country_shortname);
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(HospitalListActivity.this).
                    getRequestQueue();
            AppController.getInstance(HospitalListActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }
    }


    private void prepareEditHospitalInfo(final int hospitalId, String hospitalName, String hospitalAddress, String hospitalCity,
                                         String hospitalState, String hospitalCountry) {
        final Dialog dialog = new Dialog(HospitalListActivity.this, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_hospital);
        dialog.setTitle("Edit Hospital");
        dialog.setCanceledOnTouchOutside(true);
        EDIT_HOSPITAL = true;

        ImageView close_btn = (ImageView) dialog.findViewById(R.id.hosp_close);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        final CustomEditText _edt_hospname = (CustomEditText) dialog.findViewById(R.id.hosp_name);
        final CustomEditText _edt_hosp_email = (CustomEditText) dialog.findViewById(R.id.hosp_email);
        final CustomEditText _edt_hosp_mobile = (CustomEditText) dialog.findViewById(R.id.hosp_mobile);
        final CustomEditText _edt_hosp_address = (CustomEditText) dialog.findViewById(R.id.hosp_address);
        final CustomEditText _edt_hosp_city = (CustomEditText) dialog.findViewById(R.id.hosp_city);
        final CountryCodePicker ccp_country = (CountryCodePicker) dialog.findViewById(R.id.hosp_country);
        state_spinner = (Spinner)  dialog.findViewById(R.id.hosp_state_spinner);
        final CustomTextViewBold submit_btn = (CustomTextViewBold)dialog.findViewById(R.id.hosp_submit);
        submit_btn.setText("UPDATE");
        CustomTextViewSemiBold email_label = (CustomTextViewSemiBold)dialog.findViewById(R.id.hosp_email_label);
        CustomTextViewSemiBold mobile_label = (CustomTextViewSemiBold)dialog.findViewById(R.id.hosp_mobile_label);
        CustomTextViewSemiBold add_title = (CustomTextViewSemiBold)dialog.findViewById(R.id.hosp_add_label);
        add_title.setText("Edit Hospital");

        _edt_hospname.setText(hospitalName);
        email_label.setVisibility(View.GONE);
        _edt_hosp_email.setVisibility(View.GONE);
        mobile_label.setVisibility(View.GONE);
        _edt_hosp_mobile.setVisibility(View.GONE);
        _edt_hosp_address.setText(hospitalAddress);
        _edt_hosp_city.setText(hospitalCity);
        compareStateValue = hospitalState;

        ccp_country.setContentColor(R.color.white);
        ccp_country.setBackgroundColor(getResources().getColor(R.color.white));
        GET_COUNTRY_CODE = ccp_country.getDefaultCountryCodeWithPlus();
        GET_COUNTRY_NAME = ccp_country.getDefaultCountryName();
        GET_COUNTRY_NAME_CODE = ccp_country.getDefaultCountryNameCode();
        new getJsonState().execute(ccp_country.getDefaultCountryNameCode());
        ccp_country.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                GET_COUNTRY_CODE = ccp_country.getSelectedCountryCodeWithPlus();
                GET_COUNTRY_NAME = ccp_country.getSelectedCountryName();
                GET_COUNTRY_NAME_CODE = ccp_country.getSelectedCountryNameCode();
                new getJsonState().execute(ccp_country.getSelectedCountryNameCode());
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(submit_btn.getWindowToken(), 0);

                if(_edt_hospname.getText().toString().trim().equals("")) {
                    Toast.makeText(HospitalListActivity.this, "Enter hospital name !!!", Toast.LENGTH_SHORT).show();
                }
                else if(_edt_hosp_address.getText().toString().trim().equals("")) {
                    Toast.makeText(HospitalListActivity.this, "Enter address !!!", Toast.LENGTH_SHORT).show();
                }
                else  if(_edt_hosp_city.getText().toString().trim().equals("")) {
                    Toast.makeText(HospitalListActivity.this, "Enter city !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.dismiss();
                    submitUpdateHospitalRequestToServer(hospitalId, _edt_hospname.getText().toString().trim(),
                           _edt_hosp_address.getText().toString().trim(),_edt_hosp_city.getText().toString().trim(),GET_STATE, GET_COUNTRY_NAME);
                }
            }
        });

        dialog.show();
    }

    private void submitUpdateHospitalRequestToServer(final int hospitalId, final String _edt_hospname, final String _edt_hosp_address, final String _edt_hosp_city,
                                                     final String _edt_hosp_state, final String _edt_hosp_country) {

        new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;
            ProgressDialog progressDialog1;
            JSONArray jsonHospArray;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog1 = new ProgressDialog(HospitalListActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                progressDialog1.setIndeterminate(true);
                progressDialog1.setMessage("Please Wait....");
                progressDialog1.show();


            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {
                    JSONObject jsonObject = JSONParser.updateHospital(hospitalId,_edt_hospname,_edt_hosp_address,_edt_hosp_city,
                            _edt_hosp_state, _edt_hosp_country,  USER_ID, USER_LOGIN_TYPE);

                    if (jsonObject != null) {
                        Log.e(Utils.TAG, " GET: " + jsonObject.getString("result"));
                        if(jsonObject.getString("result").equals("success")) {
                            status = true;
                            if (jsonObject.has("doc_hospital")) {
                                jsonHospArray = jsonObject.getJSONArray("doc_hospital");
                            }

                        }
                        else {
                            status = false;
                        }
                    }
                    // return true;
                } catch (JSONException e) {
                    Log.i(Utils.TAG, "Error : " + e.getLocalizedMessage());
                    return false;
                }
                return status;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (  progressDialog1 != null)
                    progressDialog1.dismiss();
                Log.e(Utils.TAG, "aBoolean: " + String.valueOf(aBoolean));
                if (aBoolean) {

                    if (sharedPreferences != null) {
                        shareadPreferenceClass.clearDocHoapitals();
                    }

                    try {
                        if(jsonHospArray.length() >0) {
                            hospitalDocArraylist = new ArrayList<>();
                            for (int k = 0; k < jsonHospArray.length(); k++) {
                                hospitalDocArraylist.add(new HospitalList(jsonHospArray.getJSONObject(k).getInt("hosp_id"),
                                        jsonHospArray.getJSONObject(k).getString("hosp_name"),
                                        jsonHospArray.getJSONObject(k).getString("hosp_addrs"),jsonHospArray.getJSONObject(k).getString("hosp_city"),
                                        jsonHospArray.getJSONObject(k).getString("hosp_state"),jsonHospArray.getJSONObject(k).getString("hosp_country"),
                                        jsonHospArray.getJSONObject(k).getInt("doc_id"),"1"));
                            }

                            if (sharedPreferences != null) {
                                Gson gson2 = new Gson();
                                String jsonText1 = gson2.toJson(hospitalDocArraylist);
                                shareadPreferenceClass.clearDocHoapitals();
                                shareadPreferenceClass.setDocHoapitals(jsonText1);
                            }

                            ArrayList<String> hospitalArray = new ArrayList<String>();
                            for (int i = 0; i < hospitalDocArraylist.size(); i++) {
                                hospitalArray.add(hospitalDocArraylist.get(i).getHospitalName());
                            }

                            prepareDocHospitals(hospitalDocArraylist);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HospitalListActivity.this, R.style.CustomDialog);
                    alertDialogBuilder.setMessage("Hospital Updated successfully. ");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else {
                    Toast.makeText(HospitalListActivity.this, "Failed to update hospital !!!", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }
}
