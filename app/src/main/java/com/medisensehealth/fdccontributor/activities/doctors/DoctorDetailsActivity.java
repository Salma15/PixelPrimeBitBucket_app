package com.medisensehealth.fdccontributor.activities.doctors;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP on 24-03-2018.
 */

public class DoctorDetailsActivity extends AppCompatActivity {
    int DOCTOR_ID, DOCTOR_SPECIALIZATION_ID;
    String DOCTOR_NAME;
    ImageView _doc_profile;
    CustomTextViewBold _doc_name;
    Button _doc_refer_btn, _doc_appointment_btn;
    RatingBar _doc_rate;
    Button _doc_refer;
    ProgressDialog progressDialog;
    String DOWNLOAD_PROFILE;

    CustomTextView _doc_specialization, _doc_qualification, _hospital_address, _doc_area_interest, _doc_experience, _doc_contribution;
    CustomTextViewBold experience_label, interestlabel, contributionlabel;
    View experience_divider, interestdivider;
    RelativeLayout experience_layout, interest_layout, contribution_layout;

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID, HOSPITAL_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_doctor_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);

            DOCTOR_ID = bundle.getInt("DOCTOR_ID", 0);
            DOCTOR_NAME = bundle.getString("DOCTOR_NAME", "");
            DOCTOR_SPECIALIZATION_ID = bundle.getInt("DOCTOR_SPECIALIZATION_ID", 0);

            Log.d(Utils.TAG , " *********** doctor details ************ ");
            Log.d(Utils.TAG + "Doc Id:", String.valueOf(DOCTOR_ID));
            Log.d(Utils.TAG + "Doc Name:", DOCTOR_NAME);
            Log.d(Utils.TAG + "Doc SpecID:", String.valueOf(DOCTOR_SPECIALIZATION_ID));
        }

        shareadPreferenceClass = new ShareadPreferenceClass(DoctorDetailsActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(DoctorDetailsActivity.this);
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

        Log.d(Utils.TAG, " USER_ID: "+USER_ID);
        Log.d(Utils.TAG, " USER_NAME: "+USER_NAME);
        Log.d(Utils.TAG, " LOGIN_TYPE: "+USER_LOGIN_TYPE);
        initializeViews();
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

    private void initializeViews() {
        _doc_profile = (ImageView) findViewById(R.id.doc_profile_image);
        _doc_name = (CustomTextViewBold) findViewById(R.id.doc_profile_name);
        _doc_specialization = (CustomTextView) findViewById(R.id.doc_profile_specialization);
        _doc_qualification = (CustomTextView) findViewById(R.id.doc_profile_qualification);
        _hospital_address = (CustomTextView) findViewById(R.id.doc_profile_address);
        _doc_area_interest = (CustomTextView) findViewById(R.id.doc_profile_interest);
        _doc_experience = (CustomTextView) findViewById(R.id.doc_profile_experience);
        _doc_contribution = (CustomTextView) findViewById(R.id.doc_profile_contribution);
        _doc_refer_btn = (Button) findViewById(R.id.doc_profile_refer);
        _doc_appointment_btn = (Button) findViewById(R.id.doc_profile_book_appoint);

        experience_label = (CustomTextViewBold) findViewById(R.id.doc_profile_experiencelabel);
        experience_divider = (View) findViewById(R.id.doc_profile_experiencedivider);
        interestlabel = (CustomTextViewBold) findViewById(R.id.doc_profile_interestlabel);
        interestdivider = (View) findViewById(R.id.doc_profile_interestdivider);
        contributionlabel = (CustomTextViewBold) findViewById(R.id.doc_profile_contributionlabel);
        experience_layout = (RelativeLayout) findViewById(R.id.doc_profile_experiencelay);
        interest_layout = (RelativeLayout) findViewById(R.id.doc_profile_interestlay);
        contribution_layout = (RelativeLayout) findViewById(R.id.doc_profile_contributelay);

        collectDoctorResultfromServer();

        _doc_refer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.getConnectivityStatusString(DoctorDetailsActivity.this).equalsIgnoreCase("enabled")) {
                  /*  Bundle bundle = new Bundle();
                    bundle.putInt("PATIENT_ID", 0);
                    bundle.putInt("DOCTOR_ID", DOCTOR_ID);
                    bundle.putString("DOCTOR_NAME",DOCTOR_NAME);
                    bundle.putInt("DOCTOR_SPECIALIZATION_ID", DOCTOR_SPECIALIZATION_ID);
                    bundle.putString("PATIENT_VIEW","ADD");
                    ReferPatientFragment referFragment = new ReferPatientFragment();
                    fragTransaction = fragManager.beginTransaction();
                    referFragment.setArguments(bundle);
                    fragTransaction.replace(R.id.dashboard_fragment,referFragment,"REFER_PATIENT");
                  //  fragTransaction.addToBackStack("REFER_PATIENT");
                    fragTransaction.commit();*/

                } else {
                    AppUtils.showCustomAlertMessage(DoctorDetailsActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
            }
        });

        _doc_appointment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.getConnectivityStatusString(DoctorDetailsActivity.this).equalsIgnoreCase("enabled")) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("PATIENT_ID", 0);
                    bundle.putInt("DOCTOR_ID", DOCTOR_ID);
                    bundle.putString("DOCTOR_NAME",DOCTOR_NAME);
                    bundle.putInt("DOCTOR_SPECIALIZATION_ID", DOCTOR_SPECIALIZATION_ID);
                    bundle.putString("PATIENT_VIEW","ADD");
                 /*   BookAppointmentFragment bookFragment = new BookAppointmentFragment();
                    fragTransaction = fragManager.beginTransaction();
                    bookFragment.setArguments(bundle);
                    fragTransaction.replace(R.id.dashboard_fragment,bookFragment,"BOOK_APPOINTMENT");
                    fragTransaction.addToBackStack("BOOK_APPOINTMENT");
                    fragTransaction.commit();*/

                } else {
                    AppUtils.showCustomAlertMessage(DoctorDetailsActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
            }
        });
    }

    private void collectDoctorResultfromServer() {

        final ProgressDialog progressDialog = new ProgressDialog(DoctorDetailsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading details...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_DOCTORS_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, " Doc detail: "+ response.toString());
                        if (response != null) {
                            String response_filter = stripHtml(response);
                            try {
                                JSONObject jsonObject = new JSONObject(response_filter);
                                JSONArray jsonArray, jsonArray1;
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("false")) {
                                  /* WarningFragment warn = new WarningFragment();
                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.dashboard_fragment, warn,"WARNING")
                                            .commit();*/
                                } else {
                                    jsonArray = jsonObject.getJSONArray("doc_profile");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            if(jsonArray.getJSONObject(i).getString("doc_photo").trim().equals("")) {
                                                DOWNLOAD_PROFILE = APIClass.DRS_PROFILE_URL;
                                                Picasso.with(DoctorDetailsActivity.this).load(DOWNLOAD_PROFILE).fit().into(_doc_profile);
                                            }
                                            else {
                                                DOWNLOAD_PROFILE = APIClass.DRS_DOCTOR_PROFILE_URL+String.valueOf(jsonArray.getJSONObject(i).getInt("ref_id"))+"/"+jsonArray.getJSONObject(i).getString("doc_photo").trim();

                                                String urlStr = DOWNLOAD_PROFILE;
                                                URL url = null;
                                                try {
                                                    url = new URL(urlStr);
                                                    URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                                                    url = uri.toURL();
                                                } catch (MalformedURLException e) {
                                                    e.printStackTrace();
                                                } catch (URISyntaxException e) {
                                                    e.printStackTrace();
                                                }

                                                Picasso.with(DoctorDetailsActivity.this).load(String.valueOf(url))
                                                        .placeholder(R.drawable.user_profile)
                                                        .error(R.drawable.user_profile)
                                                        .fit()
                                                        .into(_doc_profile, new Callback() {
                                                            @Override
                                                            public void onSuccess() {
                                                            }
                                                            @Override
                                                            public void onError() {
                                                            }
                                                        });
                                            }


                                            _doc_name.setText(jsonArray.getJSONObject(i).getString("ref_name").trim());
                                            _doc_specialization.setText(jsonArray.getJSONObject(i).getString("spec_name").trim());
                                            _doc_qualification.setText(jsonArray.getJSONObject(i).getString("doc_qual").trim());

//                                            String hospital_address = jsonArray.getJSONObject(i).getString("ref_address").trim()+"\n"+ jsonArray.getJSONObject(i).getString("doc_city").trim()+"\n"+ jsonArray.getJSONObject(i).getString("doc_state").trim()+"\n"+ jsonArray.getJSONObject(i).getString("doc_country").trim();
//                                            _hospital_address.setText(hospital_address);

                                            if(jsonArray.getJSONObject(i).getString("doc_research").trim().equals("")) {
                                                experience_label.setVisibility(View.GONE);
                                                _doc_experience.setVisibility(View.GONE);
                                                experience_divider.setVisibility(View.GONE);
                                                experience_layout.setVisibility(View.GONE);
                                            }
                                            else {
                                                experience_layout.setVisibility(View.VISIBLE);
                                                experience_label.setVisibility(View.VISIBLE);
                                                _doc_experience.setVisibility(View.VISIBLE);
                                                experience_divider.setVisibility(View.VISIBLE);
                                                _doc_experience.setText(jsonArray.getJSONObject(i).getString("doc_research").trim());

                                            }

                                            if(jsonArray.getJSONObject(i).getString("doc_interest").trim().equals("")) {
                                                interest_layout.setVisibility(View.GONE);
                                                interestlabel.setVisibility(View.GONE);
                                                _doc_area_interest.setVisibility(View.GONE);
                                                interestdivider.setVisibility(View.GONE);
                                            }
                                            else {
                                                interest_layout.setVisibility(View.VISIBLE);
                                                interestlabel.setVisibility(View.VISIBLE);
                                                _doc_area_interest.setVisibility(View.VISIBLE);
                                                interestdivider.setVisibility(View.VISIBLE);
                                                _doc_area_interest.setText(jsonArray.getJSONObject(i).getString("doc_interest").trim());
                                            }

                                            if(jsonArray.getJSONObject(i).getString("doc_contribute").trim().equals("")) {
                                                contribution_layout.setVisibility(View.GONE);
                                                contributionlabel.setVisibility(View.GONE);
                                                _doc_contribution.setVisibility(View.GONE);
                                            }
                                            else {
                                                contribution_layout.setVisibility(View.VISIBLE);
                                                contributionlabel.setVisibility(View.VISIBLE);
                                                _doc_contribution.setVisibility(View.VISIBLE);
                                                _doc_contribution.setText(jsonArray.getJSONObject(i).getString("doc_contribute").trim());
                                            }
                                        }
                                    }
                                }

                                jsonArray1 = jsonObject.getJSONArray("hosp_address");
                                if (jsonArray1.length() > 0) {
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        String hospital_address = jsonArray1.getJSONObject(j).getString("hosp_name").trim()+"\n"+ jsonArray1.getJSONObject(j).getString("hosp_addrs").trim()+"\n"+ jsonArray1.getJSONObject(j).getString("hosp_city").trim()+"\n"+ jsonArray1.getJSONObject(j).getString("hosp_state").trim()+"\n"+ jsonArray1.getJSONObject(j).getString("hosp_country").trim();
                                        _hospital_address.setText(hospital_address);
                                    }
                                }
                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }
                        else {
                            progressDialog.dismiss();
                            //   AppUtil.showCustomAlert(getContext(), "Server Error!!!","Unable to reach Server", "OK", null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //  AppUtil.showCustomAlert(getContext(), "Server Error!!!","You Can Try it later", "OK", null);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_DOCTOR_ID, String.valueOf(DOCTOR_ID));
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(DoctorDetailsActivity.this).
                getRequestQueue();
        AppController.getInstance(DoctorDetailsActivity.this).addToRequestQueue(stringRequest);
    }

    private String stripHtml(String response) {
        return Html.fromHtml(response).toString();
    }
}
