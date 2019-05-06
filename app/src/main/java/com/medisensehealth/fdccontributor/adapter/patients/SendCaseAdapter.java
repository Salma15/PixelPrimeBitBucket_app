package com.medisensehealth.fdccontributor.adapter.patients;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medisensehealth.fdccontributor.DataModel.DoctorList;
import com.medisensehealth.fdccontributor.DataModel.PatientsList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.database.MedisensePracticeDB;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 30/08/2017.
 */

public class SendCaseAdapter  extends RecyclerView.Adapter<SendCaseAdapter.MyViewHolder> {
    private List<DoctorList> doctorList;
    Context mContext;
    FragmentManager fragManager;
    FragmentTransaction fragTransaction;
    String DOWNLOAD_PROFILE;
    int USER_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int PATIENT_ID, DOCREF_ID;
    AsyncPatientsListUpdate asynPatdetailsUpdate;
    List<PatientsList> PatientsListArraylist;
    PatientsList patients;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView doc_name;
        public CustomTextView doc_exp, doc_spec, doc_qualification, doc_address;
        public ImageView doc_favourite, doc_profile;
        RelativeLayout doclist;
        CardView cardlist;

        public MyViewHolder(View view) {
            super(view);
            doc_name = (CustomTextView) view.findViewById(R.id.doclist_name);
            doc_address = (CustomTextView) view.findViewById(R.id.doc_address);
            //  doc_exp = (CustomTextView) view.findViewById(R.id.doclist_experience);
            doc_spec = (CustomTextView) view.findViewById(R.id.doclist_specialization);
            //  doc_qualification = (CustomTextView) view.findViewById(R.id.doclist_qualification);
            // doclist = (RelativeLayout)view.findViewById(R.id.relativeLayout_doclist);
            cardlist = (CardView) view.findViewById(R.id.doctor_list_cardview);
            doc_profile = (ImageView)view.findViewById(R.id.doclist_profile);
            doc_favourite = (ImageView)view.findViewById(R.id.doclist_favourite);
        }
    }

    public SendCaseAdapter(Context context, List<DoctorList> docList, int patient_id, int DOC_REFiD) {
        this.doctorList = docList;
        this.mContext = context;
        fragManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
        shareadPreferenceClass = new ShareadPreferenceClass(context);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(context);
        this.PATIENT_ID = patient_id;
        PatientsListArraylist = new ArrayList<>();
        this.DOCREF_ID  = DOC_REFiD;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctors_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final DoctorList doctor = doctorList.get(position);

        holder.doc_name.setText(doctor.getDoctorName().trim());
        // holder.doc_exp.setText(doctor.getDoctorExperience().trim() + " Yrs");
        holder.doc_spec.setText(doctor.getDoctorSpecificationName().trim());
        holder.doc_address.setText(doctor.getDoctorAddress().trim());

        Log.d(Utils.TAG, "Pic: " + doctor.getDoctorPhoto().trim());
        if(doctor.getDoctorPhoto().trim().equals("")) {
            DOWNLOAD_PROFILE = APIClass.DRS_PROFILE_URL;
            Picasso.with(mContext).load(DOWNLOAD_PROFILE).fit().into(holder.doc_profile);
        }
        else {
            DOWNLOAD_PROFILE = APIClass.DRS_DOCTOR_PROFILE_URL+String.valueOf(doctor.getDoctorId())+"/"+doctor.getDoctorPhoto().trim();
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
            Picasso.with(mContext).load(String.valueOf(url))
                    .placeholder(R.drawable.user_profile)
                    .error(R.drawable.user_profile)
                    .resize(200, 200)
                    .centerInside()
                    .into(holder.doc_profile, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {

            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
        }

        holder.doc_favourite.setVisibility(View.GONE);

        holder.cardlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Utils.TAG, "********* Send case Adapter **************");
                Log.d(Utils.TAG + " SEL_DOCID:", String.valueOf(doctor.getDoctorId()));
                Log.d(Utils.TAG + " PATIENT_ID:", String.valueOf(PATIENT_ID));

                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("Send case to doctor");
                dialog.setMessage("Are you sure you want to send this case?" );
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        sendCaseToDoctor(doctor.getDoctorId(), PATIENT_ID, USER_ID);
                         }
                    })
                        .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Action for "Cancel".
                            }
                        });

                final AlertDialog alert = dialog.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }


    private void sendCaseToDoctor(final int SELECTED_DOCTOR_ID, final int PATIENT_ID, final int USER_ID) {

        final ProgressDialog progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending case to another doctor...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PREM_SEND_CASE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                Log.d(Utils.TAG, " RES: " + response.toString());
                                JSONObject jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("result");
                                Log.d(Utils.TAG, " resDoc: " + response.toString());
                                if(staus_res.equals("success")) {

                                    asynPatdetailsUpdate = new AsyncPatientsListUpdate();
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                        asynPatdetailsUpdate.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                    } else {
                                        asynPatdetailsUpdate.execute(PatientsListArraylist);
                                    }

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                                    alertDialogBuilder.setMessage("Your medical case has been sent successfully.");
                                    alertDialogBuilder.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {

                                                    /*Bundle bundle = new Bundle();
                                                    bundle.putInt("PATIENT_ID", PATIENT_ID);
                                                    bundle.putInt("DOCREF_ID", DOCREF_ID);
                                                    PatientsViewFragment homeFragment = new PatientsViewFragment();
                                                    fragTransaction = fragManager.beginTransaction();
                                                    homeFragment.setArguments(bundle);
                                                    fragTransaction.replace(R.id.viewpager,homeFragment,"PATIENT_VIEW");
                                                    fragTransaction.commit();*/
                                                    ((AppCompatActivity) mContext).finish();
                                                }
                                            });

                                    alertDialogBuilder.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                } else {
                                    AppUtils.showCustomAlertMessage(mContext,  "Send Case", "This patient already referred to the same doctor.", "OK", null, null);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();
                        }else {
                            progressDialog.dismiss();
                            //  AppUtil.showCustomAlert(PatientReferDoctor.this, "Server Error!!!","Unable to reach Server", "OK", null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        AppUtils.showCustomErrorMessage(mContext, "Send Case", "Failed to send. \nTry again later", "OK", null, null);
                     }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_SELECTED_DOCTOR, String.valueOf(SELECTED_DOCTOR_ID));
                map.put(APIClass.KEY_USER_ID, String.valueOf(USER_ID));
                map.put(APIClass.KEY_PATIENT_ID, String.valueOf(PATIENT_ID));
                map.put(APIClass.KEY_PATIENT_CONTACT, "1");     // 1- to send patient email and phone to doctor
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(mContext).
                getRequestQueue();
        AppController.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    private class AsyncPatientsListUpdate extends AsyncTask<Object, Object, List<PatientsList>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected List<PatientsList> doInBackground(Object... params) {
            Log.d(Utils.TAG, "DO IN BACKGROUND AsyncPatientsListUpdate");
            PatientsListArraylist = new ArrayList<>();
            int socketTimeout = 30000; // 30 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PATIENT_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG, response.toString());
                            if (response != null) {
                                JSONArray jsonArray = null;
                                String status = null;
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String staus_res = jsonObject.getString("status");
                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray = jsonObject.getJSONArray("patient_details");
                                        Log.d(Utils.TAG, String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            patients = new PatientsList(jsonArray.getJSONObject(i).getInt("pat_id"), jsonArray.getJSONObject(i).getString("pat_name"),
                                                    jsonArray.getJSONObject(i).getString("pat_age"), jsonArray.getJSONObject(i).getString("pat_loc"), jsonArray.getJSONObject(i).getInt("pat_status"),jsonArray.getJSONObject(i).getString("pat_doc_name"),jsonArray.getJSONObject(i).getInt("pat_doc_id"),Utils.USER_LOGIN_TYPE,jsonArray.getJSONObject(i).getString("pat_refered_by"),USER_ID, jsonArray.getJSONObject(i).getString("pat_status_time"));
                                            PatientsListArraylist.add(patients);
                                        }

                                        if(PatientsListArraylist.size() > 0 ) {
                                            MedisensePracticeDB.clearPatientEntries(mContext,USER_ID,USER_LOGIN_TYPE);

                                                for (int i = 0; i < PatientsListArraylist.size(); i++) {
                                                    long res_add = MedisensePracticeDB.patientInsert(mContext, PatientsListArraylist.get(i).getPatientId(), PatientsListArraylist.get(i).getPatientName(),
                                                            PatientsListArraylist.get(i).getPatientAge(),PatientsListArraylist.get(i).getPatientCity(),PatientsListArraylist.get(i).getPatientStatus(),
                                                            PatientsListArraylist.get(i).getPatientDocName(),PatientsListArraylist.get(i).getPatientDocId(), Utils.USER_LOGIN_TYPE,PatientsListArraylist.get(i).getPatientReferBy(),
                                                            USER_ID,PatientsListArraylist.get(i).getPatientStatusTime());
                                                }
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
                    map.put(APIClass.KEY_USERID, String.valueOf(Utils.USER_LOGIN_ID));
                    map.put(APIClass.KEY_LOGINTYPE, Utils.USER_LOGIN_TYPE);
                    return map;
                }
            };

            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = AppController.getInstance(mContext).
                    getRequestQueue();
            AppController.getInstance(mContext).addToRequestQueue(stringRequest);

            return PatientsListArraylist;
        }

        @Override
        protected void onPostExecute(List<PatientsList> PatientsLists) {
            super.onPostExecute(PatientsLists);

            if (PatientsListArraylist.size() > 0) {

                List<PatientsList> patLists = MedisensePracticeDB.getAllPatients(mContext, Utils.USER_LOGIN_TYPE, USER_ID);
                Log.d(Utils.TAG + " postSize", String.valueOf(patLists.size()));
            }
        }
    }
}
