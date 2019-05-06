package com.medisensehealth.fdccontributor.activities.settings;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.LinearLayout;
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
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.CustomDiagnosticCenterFilterAdapter;
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
 * Created by HP on 26-07-2018.
 */

public class DiagnosisListActivity extends AppCompatActivity implements View.OnClickListener {
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE, DIAGNOSTIC_CENTRE_LIST;

    ImageView refresh_btn;

    List<DiagnosticCentreList> diagnosticsListArraylist = new ArrayList<>();
    List<DiagnosticCentreList> selectedDiagnoCentreArraylist = new ArrayList<>();
    Gson gson = new Gson();
    ListView diagnostic_listview;
    CustomTextViewItalicBold no_data;
    SearchView diagnostic_searchView;
    CustomDiagnosticCenterFilterAdapter diagnoAdapter;

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

            Log.d(Utils.TAG, " ************ DiagnosisListActivity *********** ");
        }

        shareadPreferenceClass = new ShareadPreferenceClass(DiagnosisListActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(DiagnosisListActivity.this);


        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            DIAGNOSTIC_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_DIAGNOSTIC_CENTRES,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            DIAGNOSTIC_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_DIAGNOSTIC_CENTRES,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            DIAGNOSTIC_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_DIAGNOSTIC_CENTRES,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        Log.d(Utils.TAG , " *********** DiagnosisListActivity ****************");
        Log.d(Utils.TAG +" loginType: ", Utils.USER_LOGIN_TYPE);
        Log.d(Utils.TAG +" UserId: ", String.valueOf(Utils.USER_LOGIN_ID));

        initializationViews();
    }

    private void initializationViews() {

        diagnosticsListArraylist = new ArrayList<>();
        selectedDiagnoCentreArraylist = new ArrayList<>();


        diagnostic_searchView = (SearchView) findViewById(R.id.searchview_diagnos);
        diagnostic_listview = (ListView) findViewById(R.id.diagno_list);
        no_data = (CustomTextViewItalicBold) findViewById(R.id.diagno_empty);

        diagnoAdapter = new CustomDiagnosticCenterFilterAdapter(DiagnosisListActivity.this,
                R.layout.activity_listview, diagnosticsListArraylist);
        diagnostic_listview.setAdapter(diagnoAdapter);

        refresh_btn = (ImageView)findViewById(R.id.diagno_refresh);
        refresh_btn.setOnClickListener(this);

        if (DIAGNOSTIC_CENTRE_LIST.equals("")) {
            Log.d(Utils.TAG, "DIAGNOSTIC_CENTRE_LIST EMPTY ");

            if (NetworkUtil.getConnectivityStatusString(DiagnosisListActivity.this).equalsIgnoreCase("enabled")) {
                collectDiagnosticsListFromServer();
            } else {
                AppUtils.showCustomAlertMessage(DiagnosisListActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            diagnosticsListArraylist = gson.fromJson(DIAGNOSTIC_CENTRE_LIST, new TypeToken<List<DiagnosticCentreList>>() {
            }.getType());
            if(diagnosticsListArraylist.size() > 0 ) {
                Log.d(Utils.TAG, "DIAGNOSTIC_CENTRE_LIST > 0 " + diagnosticsListArraylist.size());
                prepareDiagnostiCentreData(diagnosticsListArraylist);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.diagno_refresh:
                if (NetworkUtil.getConnectivityStatusString(DiagnosisListActivity.this).equalsIgnoreCase("enabled")) {
                    collectDiagnosticsListFromServer();
                } else {
                    AppUtils.showCustomAlertMessage(DiagnosisListActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
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
               customAddNewDiagnosticCenter();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void collectDiagnosticsListFromServer() {
        diagnosticsListArraylist = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(DiagnosisListActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Diagnostics...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_DIAGNOSTICS_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "diagno list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("diagnostics_details");
                                    if (jsonArray.length() > 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            diagnosticsListArraylist.add(new DiagnosticCentreList(jsonArray.getJSONObject(i).getInt("diagnostic_id"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_name"),jsonArray.getJSONObject(i).getString("diagnosis_city"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_state"),jsonArray.getJSONObject(i).getString("diagnosis_country"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_contact_person"),jsonArray.getJSONObject(i).getString("diagnosis_contact_num"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_email"),USER_ID, USER_LOGIN_TYPE));

                                        }

                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(diagnosticsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearDiagnosticCentres();
                                            shareadPreferenceClass.setDiagnosticCentres(jsonDiagnoText);
                                        }

                                        prepareDiagnostiCentreData(diagnosticsListArraylist);
                                    }
                                    else {
                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(diagnosticsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearDiagnosticCentres();
                                            shareadPreferenceClass.setDiagnosticCentres(jsonDiagnoText);
                                        }
                                        prepareDiagnostiCentreData(diagnosticsListArraylist);
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

        RequestQueue requestQueue = AppController.getInstance(DiagnosisListActivity.this).
                getRequestQueue();
        AppController.getInstance(DiagnosisListActivity.this).addToRequestQueue(stringRequest);
    }

    private void prepareDiagnostiCentreData(List<DiagnosticCentreList> diagnosticsListArraylist) {

        if(diagnosticsListArraylist.size()>0) {
            no_data.setVisibility(View.GONE);
            diagnostic_listview.setVisibility(View.VISIBLE);
        }
        else {
            diagnostic_listview.setVisibility(View.GONE);
            no_data.setVisibility(View.VISIBLE);
        }

        diagnoAdapter = new CustomDiagnosticCenterFilterAdapter(DiagnosisListActivity.this,
                R.layout.activity_listview, diagnosticsListArraylist);
        diagnostic_listview.setAdapter(diagnoAdapter);
        diagnostic_listview.setTextFilterEnabled(true);

        diagnostic_searchView.setIconifiedByDefault(false);
        diagnostic_searchView.setQueryHint("Search Diagnostic Center");
        diagnostic_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                diagnostic_searchView.setQuery("", false);
                diagnostic_searchView.clearFocus();
                diagnostic_searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Utils.TAG, "SERACH: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    Log.d(Utils.TAG, "is empty " );
                    diagnoAdapter.filter(newText.toString());
                    // specialization_listview.clearTextFilter();
                } else {
                    //  specialization_listview.setFilterText(newText.toString());
                    diagnoAdapter.filter(newText.toString());
                }
                return true;
            }
        });

        diagnostic_searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                diagnostic_searchView.setQuery("", false);
                diagnostic_searchView.setIconified(false);
                diagnostic_searchView.clearFocus();
                return false;
            }
        });

        diagnostic_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DiagnosticCentreList myCustomAdapterItem = (DiagnosticCentreList) parent.getItemAtPosition(position);
                diagnostic_searchView.setQuery("", false);
                diagnostic_searchView.setIconified(false);
                diagnostic_searchView.clearFocus();

                AlertDialog.Builder dialog1 = new AlertDialog.Builder(DiagnosisListActivity.this, R.style.CustomDialog);
                dialog1.setTitle("Diagnostic Center Information");
                dialog1.setMessage("\n\nName: " + myCustomAdapterItem.getDiagnoName()+"\n\nContact Person: " + myCustomAdapterItem.getDiagnoContactPerson()
                        +"\n\nMobile No: " + myCustomAdapterItem.getDiagnoMobile()
                        +"\n\nEmail: " + myCustomAdapterItem.getDiagnoEmail()+"\n\nCity: " + myCustomAdapterItem.getDiagnoCity()
                        +"\n\nState: " + myCustomAdapterItem.getDiagnoState()+"\n\nCountry: " + myCustomAdapterItem.getDiagnoCountry());
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


    private void customAddNewDiagnosticCenter() {

        final Dialog dialog = new Dialog(DiagnosisListActivity.this, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_diagnostic_center);
        dialog.setTitle("Add New Diagnostic Center");

        LinearLayout addNew_contents = dialog.findViewById(R.id.diagnostic_addNew_content);
        addNew_contents.setVisibility(View.VISIBLE);


        LinearLayout list_contents = dialog.findViewById(R.id.diagnostic_list_content);
        list_contents.setVisibility(View.GONE);

        final ImageView close_btn = (ImageView)  dialog.findViewById(R.id.diagnostic_close);
        final ImageView refresh_btn = (ImageView)  dialog.findViewById(R.id.diagnostic_refresh);
        CustomTextViewBold add_new_btn = (CustomTextViewBold) dialog.findViewById(R.id.diagnostic_add_new);
        final CustomTextViewBold submit_new_btn = (CustomTextViewBold) dialog.findViewById(R.id.edt_diagnostic_submit);
        final CustomTextViewBold cancel_new_btn = (CustomTextViewBold) dialog.findViewById(R.id.edt_diagnostic_cancel);
        refresh_btn.setVisibility(View.GONE);
        cancel_new_btn.setVisibility(View.GONE);
        add_new_btn.setVisibility(View.GONE);

        final CustomEditText _edit_name = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_name);
        final CustomEditText _edit_email = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_email);
        final CustomEditText _edit_mobile = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_mobile);
        final CustomEditText _edit_city = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_city);

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

                String diagno_name = _edit_name.getText().toString().trim();
                String diagno_email = _edit_email.getText().toString().trim();
                String diagno_mobile = _edit_mobile.getText().toString().trim();
                String diagno_city = _edit_city.getText().toString().trim();

                if(diagno_name.equals("")) {
                    Toast.makeText(DiagnosisListActivity.this, "Enter Diagnostic Names", Toast.LENGTH_SHORT).show();
                }
                else  if((diagno_mobile.equals("")) || (diagno_mobile.length() <10)) {
                    Toast.makeText(DiagnosisListActivity.this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendDiagnosticNewToServer(diagno_name,diagno_email,diagno_mobile,diagno_city);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void sendDiagnosticNewToServer(final String diagno_name, final String diagno_email, final String diagno_mobile, final String diagno_city) {
        diagnosticsListArraylist = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(DiagnosisListActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding New Diagnostic Center...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_DIAGNOSTICS_ADD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "diagno list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("diagnostics_details");
                                    if (jsonArray.length() > 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            diagnosticsListArraylist.add(new DiagnosticCentreList(jsonArray.getJSONObject(i).getInt("diagnostic_id"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_name"),jsonArray.getJSONObject(i).getString("diagnosis_city"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_state"),jsonArray.getJSONObject(i).getString("diagnosis_country"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_contact_person"),jsonArray.getJSONObject(i).getString("diagnosis_contact_num"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_email"),USER_ID, USER_LOGIN_TYPE));

                                        }

                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(diagnosticsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearDiagnosticCentres();
                                            shareadPreferenceClass.setDiagnosticCentres(jsonDiagnoText);
                                        }

                                        prepareDiagnostiCentreData(diagnosticsListArraylist);
                                    }
                                    else {
                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(diagnosticsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearDiagnosticCentres();
                                            shareadPreferenceClass.setDiagnosticCentres(jsonDiagnoText);
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
                map.put(APIClass.KEY_DIAGNO_NAME, diagno_name);
                map.put(APIClass.KEY_DIAGNO_EMAIL, diagno_email);
                map.put(APIClass.KEY_DIAGNO_MOBILE, diagno_mobile);
                map.put(APIClass.KEY_DIAGNO_CITY, diagno_city);
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(DiagnosisListActivity.this).
                getRequestQueue();
        AppController.getInstance(DiagnosisListActivity.this).addToRequestQueue(stringRequest);
    }
}
