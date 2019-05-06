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
import com.medisensehealth.fdccontributor.DataModel.PharmaCentreList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.CustomPharmaCenterFilterAdapter;
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
import com.medisensehealth.fdccontributor.views.CustomTextViewSemiBold;

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

public class PharmacyListActivity extends AppCompatActivity implements View.OnClickListener {
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE, PHARMA_CENTRE_LIST;

    ImageView refresh_btn;

    List<PharmaCentreList> pharmaListArraylist = new ArrayList<>();
    List<PharmaCentreList> selectedPharmaArraylist = new ArrayList<>();
    Gson gson = new Gson();
    ListView pharma_listview;
    CustomTextViewItalicBold no_data;
    SearchView pharma_searchView;
    CustomPharmaCenterFilterAdapter pharmaAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pharmacy_list);

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

        shareadPreferenceClass = new ShareadPreferenceClass(PharmacyListActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(PharmacyListActivity.this);


        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            PHARMA_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_PHARMA_CENTRES,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            PHARMA_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_PHARMA_CENTRES,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            PHARMA_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_PHARMA_CENTRES,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        Log.d(Utils.TAG , " *********** PharmacyListActivity ****************");
        Log.d(Utils.TAG +" loginType: ", Utils.USER_LOGIN_TYPE);
        Log.d(Utils.TAG +" UserId: ", String.valueOf(Utils.USER_LOGIN_ID));

        initializationViews();
    }

    private void initializationViews() {

        pharmaListArraylist = new ArrayList<>();
        selectedPharmaArraylist = new ArrayList<>();

        pharma_searchView = (SearchView) findViewById(R.id.searchview_pharma);
        pharma_listview = (ListView) findViewById(R.id.pharma_list);
        no_data = (CustomTextViewItalicBold) findViewById(R.id.pharmacy_empty);

        pharmaAdapter = new CustomPharmaCenterFilterAdapter(PharmacyListActivity.this,
                R.layout.activity_listview, pharmaListArraylist);
        pharma_listview.setAdapter(pharmaAdapter);

        refresh_btn = (ImageView)findViewById(R.id.pharma_refresh);
        refresh_btn.setOnClickListener(this);


        pharmaListArraylist = new ArrayList<>();
        if (PHARMA_CENTRE_LIST.equals("")) {
            Log.d(Utils.TAG, "PHARMA_CENTRE_LIST EMPTY ");

            if (NetworkUtil.getConnectivityStatusString(PharmacyListActivity.this).equalsIgnoreCase("enabled")) {
                collectPharmaListFromServer();
            } else {
                AppUtils.showCustomAlertMessage(PharmacyListActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            pharmaListArraylist = gson.fromJson(PHARMA_CENTRE_LIST, new TypeToken<List<PharmaCentreList>>() {
            }.getType());
            if(pharmaListArraylist.size() > 0 ) {
                Log.d(Utils.TAG, "PHARMA_CENTRE_LIST > 0 " + pharmaListArraylist.size());
                preparePharmaCentreData(pharmaListArraylist);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pharma_refresh:
                if (NetworkUtil.getConnectivityStatusString(PharmacyListActivity.this).equalsIgnoreCase("enabled")) {
                    collectPharmaListFromServer();
                } else {
                    AppUtils.showCustomAlertMessage(PharmacyListActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
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
                customAddNewPharmaCenter();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void collectPharmaListFromServer() {
        pharmaListArraylist = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(PharmacyListActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Pharmacy lists...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PHARMA_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "pharma list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("pharma_details");
                                    if (jsonArray.length() > 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            pharmaListArraylist.add(new PharmaCentreList(jsonArray.getJSONObject(i).getInt("pharma_id"),
                                                    jsonArray.getJSONObject(i).getString("pharma_name"),jsonArray.getJSONObject(i).getString("pharma_city"),
                                                    jsonArray.getJSONObject(i).getString("pharma_state"),jsonArray.getJSONObject(i).getString("pharma_country"),
                                                    jsonArray.getJSONObject(i).getString("phrama_contact_person"),jsonArray.getJSONObject(i).getString("pharma_contact_num"),
                                                    jsonArray.getJSONObject(i).getString("pharma_email"),USER_ID, USER_LOGIN_TYPE));

                                        }

                                        //Set the values
                                        gson = new Gson();
                                        String jsonPharmaText = gson.toJson(pharmaListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearPharmaCentres();
                                            shareadPreferenceClass.setPharmaCentres(jsonPharmaText);
                                        }

                                        preparePharmaCentreData(pharmaListArraylist);
                                    }
                                    else {
                                        //Set the values
                                        gson = new Gson();
                                        String jsonPharmaText = gson.toJson(pharmaListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearPharmaCentres();
                                            shareadPreferenceClass.setPharmaCentres(jsonPharmaText);
                                        }
                                        preparePharmaCentreData(pharmaListArraylist);
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

        RequestQueue requestQueue = AppController.getInstance(PharmacyListActivity.this).
                getRequestQueue();
        AppController.getInstance(PharmacyListActivity.this).addToRequestQueue(stringRequest);
    }

    private void preparePharmaCentreData(List<PharmaCentreList> pharmaListArraylist) {

        if(pharmaListArraylist.size()>0) {
            no_data.setVisibility(View.GONE);
            pharma_listview.setVisibility(View.VISIBLE);
        }
        else {
            pharma_listview.setVisibility(View.GONE);
            no_data.setVisibility(View.VISIBLE);
        }


        final CustomPharmaCenterFilterAdapter pharmaAdapter = new CustomPharmaCenterFilterAdapter(PharmacyListActivity.this,
                R.layout.activity_listview, pharmaListArraylist);
        pharma_listview.setAdapter(pharmaAdapter);
        pharma_listview.setTextFilterEnabled(true);

        pharma_searchView.setIconifiedByDefault(false);
        pharma_searchView.setQueryHint("Search Pharmacy");
        pharma_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                pharma_searchView.setQuery("", false);
                pharma_searchView.clearFocus();
                pharma_searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Utils.TAG, "SEARCH: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    Log.d(Utils.TAG, "is empty " );
                    pharmaAdapter.filter(newText.toString());
                    // specialization_listview.clearTextFilter();
                } else {
                    //  specialization_listview.setFilterText(newText.toString());
                    pharmaAdapter.filter(newText.toString());
                }
                return true;
            }
        });

        pharma_searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                pharma_searchView.setQuery("", false);
                pharma_searchView.setIconified(false);
                pharma_searchView.clearFocus();
                return false;
            }
        });

        pharma_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PharmaCentreList myCustomAdapterItem = (PharmaCentreList) parent.getItemAtPosition(position);
              /* Toast.makeText(getActivity(),  myCustomAdapterItem.getSymptomsName() + " added.",
                        Toast.LENGTH_SHORT)
                        .show();*/
                pharma_searchView.setQuery("", false);
                pharma_searchView.setIconified(false);
                pharma_searchView.clearFocus();

                selectedPharmaArraylist = new ArrayList<>();
                selectedPharmaArraylist.add(new PharmaCentreList(myCustomAdapterItem.getPharmaId(),myCustomAdapterItem.getPharmaName()));

                AlertDialog.Builder dialog1 = new AlertDialog.Builder(PharmacyListActivity.this, R.style.CustomDialog);
                dialog1.setTitle("Pharmacy Center Information");
                dialog1.setMessage("\n\nName: " + myCustomAdapterItem.getPharmaName()+"\n\nContact Person: " + myCustomAdapterItem.getPharmaContactPerson()
                        +"\n\nMobile No: " + myCustomAdapterItem.getPharmaMobile()
                        +"\n\nEmail: " + myCustomAdapterItem.getPharmaEmail()+"\n\nCity: " + myCustomAdapterItem.getPharmaCity()
                        +"\n\nState: " + myCustomAdapterItem.getPharmaState()+"\n\nCountry: " + myCustomAdapterItem.getPharmaCountry());
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

    private void customAddNewPharmaCenter() {
        final Dialog dialog = new Dialog(PharmacyListActivity.this, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_diagnostic_center);
        dialog.setTitle("Add New Pharma Center");

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
        CustomTextViewSemiBold title = (CustomTextViewSemiBold)dialog.findViewById(R.id.diagnostic_addNew_title);
        title.setText("Add New Pharmacy");
        _edit_name.setHint("Pharmacy Name");

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

                String pharma_name = _edit_name.getText().toString().trim();
                String pharma_email = _edit_email.getText().toString().trim();
                String pharma_mobile = _edit_mobile.getText().toString().trim();
                String pharma_city = _edit_city.getText().toString().trim();

                if(pharma_name.equals("")) {
                    Toast.makeText(PharmacyListActivity.this, "Enter Pharmacy Names", Toast.LENGTH_SHORT).show();
                }
                else  if((pharma_mobile.equals("")) || (pharma_mobile.length() <10)) {
                    Toast.makeText(PharmacyListActivity.this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendPharmaNewToServer(pharma_name,pharma_email,pharma_mobile,pharma_city);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void sendPharmaNewToServer(final String pharma_name, final String pharma_email, final String pharma_mobile, final String pharma_city) {
        pharmaListArraylist = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(PharmacyListActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding New Pharmacy...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PHARMA_ADD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "pharma list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("pharma_details");
                                    if (jsonArray.length() > 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            pharmaListArraylist.add(new PharmaCentreList(jsonArray.getJSONObject(i).getInt("pharma_id"),
                                                    jsonArray.getJSONObject(i).getString("pharma_name"),jsonArray.getJSONObject(i).getString("pharma_city"),
                                                    jsonArray.getJSONObject(i).getString("pharma_state"),jsonArray.getJSONObject(i).getString("pharma_country"),
                                                    jsonArray.getJSONObject(i).getString("phrama_contact_person"),jsonArray.getJSONObject(i).getString("pharma_contact_num"),
                                                    jsonArray.getJSONObject(i).getString("pharma_email"),USER_ID, USER_LOGIN_TYPE));

                                        }

                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(pharmaListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearPharmaCentres();
                                            shareadPreferenceClass.setPharmaCentres(jsonDiagnoText);
                                        }

                                        preparePharmaCentreData(pharmaListArraylist);
                                    }
                                    else {
                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(pharmaListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearPharmaCentres();
                                            shareadPreferenceClass.setPharmaCentres(jsonDiagnoText);
                                        }
                                        preparePharmaCentreData(pharmaListArraylist);
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
                map.put(APIClass.KEY_PHARMA_NAME, pharma_name);
                map.put(APIClass.KEY_PHARMA_EMAIL, pharma_email);
                map.put(APIClass.KEY_PHARMA_MOBILE, pharma_mobile);
                map.put(APIClass.KEY_PHARMA_CITY, pharma_city);
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(PharmacyListActivity.this).
                getRequestQueue();
        AppController.getInstance(PharmacyListActivity.this).addToRequestQueue(stringRequest);
    }
}
