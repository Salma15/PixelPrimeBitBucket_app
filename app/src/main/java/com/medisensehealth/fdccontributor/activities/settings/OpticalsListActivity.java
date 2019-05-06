package com.medisensehealth.fdccontributor.activities.settings;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.ListView;
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
import com.medisensehealth.fdccontributor.DataModel.DiagnosticCentreList;
import com.medisensehealth.fdccontributor.DataModel.OpticalCentreList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.CustomDiagnosticCenterFilterAdapter;
import com.medisensehealth.fdccontributor.adapter.settings.CustomOpticalCenterFilterAdapter;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomEditText;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;
import com.medisensehealth.fdccontributor.views.CustomTextViewItalicBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by SALMA on 04-05-2019.
 */
public class OpticalsListActivity extends AppCompatActivity implements View.OnClickListener {

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE, OPTICAL_CENTRE_LIST;

    List<OpticalCentreList> opticalsListArraylist = new ArrayList<>();
    Gson gson = new Gson();

    ImageView refresh_btn;
    ListView optical_listview;
    CustomTextViewItalicBold no_data;
    SearchView optical_searchView;
    CustomOpticalCenterFilterAdapter opticalAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_opticals_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);

            Log.d(Utils.TAG, " ************ OpticalsListActivity *********** ");
        }

        shareadPreferenceClass = new ShareadPreferenceClass(OpticalsListActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(OpticalsListActivity.this);


        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            OPTICAL_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_OPTICAL_CENTRES,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            OPTICAL_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_OPTICAL_CENTRES,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            OPTICAL_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_OPTICAL_CENTRES,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        Log.d(Utils.TAG +" loginType: ", Utils.USER_LOGIN_TYPE);
        Log.d(Utils.TAG +" UserId: ", String.valueOf(Utils.USER_LOGIN_ID));

        initializationViews();
    }

    private void initializationViews() {
        opticalsListArraylist = new ArrayList<>();
        gson = new Gson();

        optical_searchView = (SearchView) findViewById(R.id.searchview_optical);
        optical_listview = (ListView) findViewById(R.id.optical_list);
        no_data = (CustomTextViewItalicBold) findViewById(R.id.optical_empty);

        opticalAdapter = new CustomOpticalCenterFilterAdapter(OpticalsListActivity.this,
                R.layout.activity_listview, opticalsListArraylist);
        optical_listview.setAdapter(opticalAdapter);

        refresh_btn = (ImageView)findViewById(R.id.optical_refresh);
        refresh_btn.setOnClickListener(this);

        if (OPTICAL_CENTRE_LIST.equals("")) {
            Log.d(Utils.TAG, "OPTICAL_CENTRE_LIST EMPTY ");

            if (NetworkUtil.getConnectivityStatusString(OpticalsListActivity.this).equalsIgnoreCase("enabled")) {
                collectOpticalsListFromServer();
            } else {
                AppUtils.showCustomAlertMessage(OpticalsListActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            opticalsListArraylist = gson.fromJson(OPTICAL_CENTRE_LIST, new TypeToken<List<OpticalCentreList>>() {
            }.getType());
            if(opticalsListArraylist.size() > 0 ) {
                Log.d(Utils.TAG, "OPTICAL_CENTRE_LIST > 0 " + opticalsListArraylist.size());
                prepareOpticalCentreData(opticalsListArraylist);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.optical_refresh:
                if (NetworkUtil.getConnectivityStatusString(OpticalsListActivity.this).equalsIgnoreCase("enabled")) {
                    collectOpticalsListFromServer();
                } else {
                    AppUtils.showCustomAlertMessage(OpticalsListActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
                break;
        }
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
                customAddNewOpticalCenter();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void collectOpticalsListFromServer() {
        opticalsListArraylist = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(OpticalsListActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Optical Centers...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_OPTICALS_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "OPTI list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("opticals_details");
                                    if (jsonArray.length() > 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            opticalsListArraylist.add(new OpticalCentreList(jsonArray.getJSONObject(i).getInt("optical_id"),
                                                    jsonArray.getJSONObject(i).getString("optical_name"),jsonArray.getJSONObject(i).getString("optical_city"),
                                                    jsonArray.getJSONObject(i).getString("optical_state"),jsonArray.getJSONObject(i).getString("optical_country"),
                                                    jsonArray.getJSONObject(i).getString("optical_contact_person"),jsonArray.getJSONObject(i).getString("optical_contact_num"),
                                                    jsonArray.getJSONObject(i).getString("optical_email"),USER_ID, USER_LOGIN_TYPE));

                                        }

                                        //Set the values
                                        gson = new Gson();
                                        String jsonOpticalText = gson.toJson(opticalsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearOpticalCentres();
                                            shareadPreferenceClass.setOpticalCentres(jsonOpticalText);
                                        }

                                        prepareOpticalCentreData(opticalsListArraylist);
                                    }
                                    else {
                                        //Set the values
                                        gson = new Gson();
                                        String jsonOpticalText = gson.toJson(opticalsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearOpticalCentres();
                                            shareadPreferenceClass.setOpticalCentres(jsonOpticalText);
                                        }
                                        prepareOpticalCentreData(opticalsListArraylist);
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

        RequestQueue requestQueue = AppController.getInstance(OpticalsListActivity.this).
                getRequestQueue();
        AppController.getInstance(OpticalsListActivity.this).addToRequestQueue(stringRequest);
    }

    private void prepareOpticalCentreData(List<OpticalCentreList> opticalsListArraylist) {

        if(opticalsListArraylist.size()>0) {
            no_data.setVisibility(View.GONE);
            optical_listview.setVisibility(View.VISIBLE);
        }
        else {
            optical_listview.setVisibility(View.GONE);
            no_data.setVisibility(View.VISIBLE);
        }

        opticalAdapter = new CustomOpticalCenterFilterAdapter(OpticalsListActivity.this,
                R.layout.activity_listview, opticalsListArraylist);
        optical_listview.setAdapter(opticalAdapter);
        optical_listview.setTextFilterEnabled(true);

        optical_searchView.setIconifiedByDefault(false);
        optical_searchView.setQueryHint("Search Optical Center");
        optical_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                optical_searchView.setQuery("", false);
                optical_searchView.clearFocus();
                optical_searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Utils.TAG, " SEARCH: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    Log.d(Utils.TAG, "is empty " );
                    opticalAdapter.filter(newText.toString());
                } else {
                    opticalAdapter.filter(newText.toString());
                }
                return true;
            }
        });

        optical_searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                optical_searchView.setQuery("", false);
                optical_searchView.setIconified(false);
                optical_searchView.clearFocus();
                return false;
            }
        });

        optical_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpticalCentreList myCustomAdapterItem = (OpticalCentreList) parent.getItemAtPosition(position);
                optical_searchView.setQuery("", false);
                optical_searchView.setIconified(false);
                optical_searchView.clearFocus();

                AlertDialog.Builder dialog1 = new AlertDialog.Builder(OpticalsListActivity.this, R.style.CustomDialog);
                dialog1.setTitle("Optical Center Information");
                dialog1.setMessage("\n\nName: " + myCustomAdapterItem.getOpticalName()+"\n\nContact Person: " + myCustomAdapterItem.getOpticalContactPerson()
                        +"\n\nMobile No: " + myCustomAdapterItem.getOpticalMobile()
                        +"\n\nEmail: " + myCustomAdapterItem.getOpticalEmail()+"\n\nCity: " + myCustomAdapterItem.getOpticalCity()
                        +"\n\nState: " + myCustomAdapterItem.getOpticalState()+"\n\nCountry: " + myCustomAdapterItem.getOpticalCountry());
                dialog1.setPositiveButton("Done!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                final AlertDialog alert = dialog1.create();
                alert.show();
            }
        });

    }

    private void customAddNewOpticalCenter() {
        final Dialog dialog = new Dialog(OpticalsListActivity.this, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_optical_center);
        dialog.setTitle("Add New Optical Center");

        final ImageView close_btn = (ImageView)  dialog.findViewById(R.id.optical_close);
        final CustomTextViewBold submit_new_btn = (CustomTextViewBold) dialog.findViewById(R.id.edt_optical_submit);
        final CustomEditText _edit_name = (CustomEditText) dialog.findViewById(R.id.edt_optical_name);
        final CustomEditText _edit_contactperson = (CustomEditText) dialog.findViewById(R.id.edt_optical_contactperson);
        final CustomEditText _edit_email = (CustomEditText) dialog.findViewById(R.id.edt_optical_email);
        final CustomEditText _edit_mobile = (CustomEditText) dialog.findViewById(R.id.edt_optical_mobile);
        final CustomEditText _edit_city = (CustomEditText) dialog.findViewById(R.id.edt_optical_city);
        final CustomEditText _edit_country = (CustomEditText) dialog.findViewById(R.id.edt_optical_country);
        final CustomEditText _edit_state = (CustomEditText) dialog.findViewById(R.id.edt_optical_state);

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(close_btn.getWindowToken(), 0);
            }
        });

        submit_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(submit_new_btn.getWindowToken(), 0);

                String optical_name = _edit_name.getText().toString().trim();
                String optical_email = _edit_email.getText().toString().trim();
                String optical_mobile = _edit_mobile.getText().toString().trim();
                String optical_city = _edit_city.getText().toString().trim();
                String optical_contactperson = _edit_contactperson.getText().toString().trim();
                String optical_state = _edit_state.getText().toString().trim();
                String optical_country = _edit_country.getText().toString().trim();

                if(optical_name.equals("")) {
                    Toast.makeText(OpticalsListActivity.this, "Enter Optical Name !!!", Toast.LENGTH_SHORT).show();
                }
                else  if((optical_mobile.equals("")) || (optical_mobile.length() <10)) {
                    Toast.makeText(OpticalsListActivity.this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendOpticalNewToServer(optical_name,optical_contactperson,optical_email,optical_mobile,optical_city,optical_state,optical_country);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }

    private void sendOpticalNewToServer(final String optical_name, final String optical_contactperson, final String optical_email, final String optical_mobile,
                                        final String optical_city, final String optical_state, final String optical_country) {
        opticalsListArraylist = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(OpticalsListActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding New Optical Center...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_OPTICALS_ADD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "optic list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("opticals_details");
                                    if (jsonArray.length() > 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            opticalsListArraylist.add(new OpticalCentreList(jsonArray.getJSONObject(i).getInt("optical_id"),
                                                    jsonArray.getJSONObject(i).getString("optical_name"),jsonArray.getJSONObject(i).getString("optical_city"),
                                                    jsonArray.getJSONObject(i).getString("optical_state"),jsonArray.getJSONObject(i).getString("optical_country"),
                                                    jsonArray.getJSONObject(i).getString("optical_contact_person"),jsonArray.getJSONObject(i).getString("optical_contact_num"),
                                                    jsonArray.getJSONObject(i).getString("optical_email"),USER_ID, USER_LOGIN_TYPE));

                                        }

                                        //Set the values
                                        gson = new Gson();
                                        String jsonOpticalText = gson.toJson(opticalsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearOpticalCentres();
                                            shareadPreferenceClass.setOpticalCentres(jsonOpticalText);
                                        }

                                        prepareOpticalCentreData(opticalsListArraylist);
                                    }
                                    else {
                                        //Set the values
                                        gson = new Gson();
                                        String jsonOpticalText = gson.toJson(opticalsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearOpticalCentres();
                                            shareadPreferenceClass.setOpticalCentres(jsonOpticalText);
                                        }
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
                map.put(APIClass.KEY_OPTICALS_NAME, optical_name);
                map.put(APIClass.KEY_OPTICALS_EMAIL, optical_email);
                map.put(APIClass.KEY_OPTICALS_MOBILE, optical_mobile);
                map.put(APIClass.KEY_OPTICALS_CITY, optical_city);
                map.put(APIClass.KEY_OPTICALS_STATE, optical_state);
                map.put(APIClass.KEY_OPTICALS_COUNTRY, optical_country);
                map.put(APIClass.KEY_OPTICALS_CONTACT_PERSON, optical_contactperson);
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(OpticalsListActivity.this).
                getRequestQueue();
        AppController.getInstance(OpticalsListActivity.this).addToRequestQueue(stringRequest);
    }
}
