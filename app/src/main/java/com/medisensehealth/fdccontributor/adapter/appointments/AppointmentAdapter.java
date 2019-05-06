package com.medisensehealth.fdccontributor.adapter.appointments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.medisensehealth.fdccontributor.DataModel.AppointmentList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.mypatientOphthalmology.MyPatientOphthalHomeActivity;
import com.medisensehealth.fdccontributor.activities.mypatientOphthalmology.RefreshOphthalEMRActivity;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewSemiBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by lenovo on 17/08/2017.
 */

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder>  {

    private List<AppointmentList> apptList;
    private Context mContext;
    private List<AppointmentList> appointmentArryList = new ArrayList<>();
    private AppointmentList appointmemts;
    Gson gson;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int token_visible = 0;
    String PATIENT_APPOINTMENT_DATE = "",PATIENT_APPOINTMENT_TIME = "";
    Spinner appoint_time_spinner, appoint_date_spinner;
    int SPECIALIZATION_GROUP_ID;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView patient_name,  status;
        public CardView table_row;
        public CustomTextView visit_date, visit_time, transaction_id;
        public ImageView delete;
        public Spinner spinner;
        public CustomTextViewSemiBold token_num, token_online;
        public LinearLayout reschedule_btn;

        public MyViewHolder(View view) {
            super(view);
            patient_name = (CustomTextView) view.findViewById(R.id.appt_patientname);
            //   transaction_id = (CustomTextViewItalic) view.findViewById(R.id.appt_trans_id);
            visit_date = (CustomTextView) view.findViewById(R.id.appt_visit_date);
            visit_time = (CustomTextView) view.findViewById(R.id.appt_time);
            status = (CustomTextView) view.findViewById(R.id.appt_status);
            table_row = (CardView) view.findViewById(R.id.appt_tableRow);
            delete = (ImageView) view.findViewById(R.id.appt_delete);
            spinner = (Spinner) view.findViewById(R.id.status_spinner);
            token_num = (CustomTextViewSemiBold) view.findViewById(R.id.appt_token_num);
            reschedule_btn = (LinearLayout) view.findViewById(R.id.appt_reschedule);
            token_online = (CustomTextViewSemiBold) view.findViewById(R.id.appt_token_online);
        }
    }

    // OLD APPOINTMENTS - DON'T USE
    public AppointmentAdapter(Context context, List<AppointmentList> apptListe) {
        this.apptList = apptList;
        this.mContext = context;
        appointmentArryList = new ArrayList<>();
        shareadPreferenceClass = new ShareadPreferenceClass(context);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(context);
    }// OLD APPOINTMENTS - DON'T USE

    public AppointmentAdapter(Context context, List<AppointmentList> apptList, int view_type) {
        this.apptList = apptList;
        this.mContext = context;
        appointmentArryList = new ArrayList<>();
        this.shareadPreferenceClass = new ShareadPreferenceClass(context);
        this.sharedPreferences = shareadPreferenceClass.getSharedPreferences(context);
        this.token_visible = view_type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AppointmentList appt = apptList.get(position);

        if(position % 2==0)
            holder.table_row.setCardBackgroundColor(mContext.getResources().getColor(R.color.row_even_color));
        else
            holder.table_row.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));



      //  String status[] = {"At reception","Consulted","Cancelled", "Missed"};
        String status[] = {"At reception","Consulted","Cancelled", "Missed","Pending","Confirmed","Waiting"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_text, status);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        holder.spinner.setAdapter(spinnerArrayAdapter);

        if (appt.getStatus() != null) {
            int spinnerPosition = spinnerArrayAdapter.getPosition(appt.getStatus());
            holder.spinner.setSelection(spinnerPosition);
        }

        final int spinPosition =  holder.spinner.getSelectedItemPosition();
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long l) {
                Log.d(Utils.TAG, " spinPosition: "+spinPosition+" position: "+position);
                if(spinPosition != position) {
                    Log.d(Utils.TAG, " Load ");
                    new setJsonAppointmentStatus(parentView.getItemAtPosition(position).toString().trim(),String.valueOf(appt.getAppointDoctorID()),String.valueOf(appt.getDocType()),String.valueOf(appt.getHospitalID()),String.valueOf(appt.getTransactionID())).execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        holder.patient_name.setText(appt.getPatientName());
        holder.visit_time.setText(appt.getVisitTime());
        holder.status.setText(appt.getStatus());

        if(token_visible == 0) {        // 0 - VIEW Token, 1- don't

            holder.reschedule_btn.setVisibility(View.GONE);

            if(appt.getTokenNumber() == 555) {
                holder.token_online.setVisibility(View.VISIBLE);
                holder.token_num.setVisibility(View.GONE);
                holder.token_num.setText(String.valueOf(appt.getTokenNumber()));
            }
            else {
                holder.token_online.setVisibility(View.GONE);
                holder.token_num.setVisibility(View.VISIBLE);
                holder.token_num.setText(String.valueOf(appt.getTokenNumber()));
            }

        }
        else {
            holder.token_num.setVisibility(View.GONE);
            holder.token_num.setText(String.valueOf(appt.getTokenNumber()));
            holder.reschedule_btn.setVisibility(View.VISIBLE);
        }

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = null;
        try {
            date = fmt.parse(appt.getVisitDate().trim());
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
            String visit_date_convt = fmtOut.format(date);
            holder.visit_date.setText(visit_date_convt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.table_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isFirstRunShow = sharedPreferences.getBoolean("FIRSTRUNLOADEMR", true);
                if (isFirstRunShow)
                {
                    Log.d(Utils.TAG, " isFirstRunShow TRUE - - RefreshOphthalEMRActivity");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("FIRSTRUNLOADEMR", false);
                    editor.commit();

                    Intent bundle = new Intent(mContext, RefreshOphthalEMRActivity.class);
                    bundle.putExtra("title","Refresh EMR");
                    bundle.putExtra("PATIENT_ID",  appt.getPatientID());
                    bundle.putExtra("PATIENT_NAME", appt.getPatientName());
                    bundle.putExtra("PATIENT_AGE", "");
                    bundle.putExtra("PATIENT_GENDER", "");
                    bundle.putExtra("PATIENT_MOBILE", appt.getAppointMobile());
                    bundle.putExtra("PATIENT_EMAIL", appt.getAppointEmail());
                    bundle.putExtra("PATIENT_CITY", "");
                    bundle.putExtra("PATIENT_ADDRESS", "");
                    bundle.putExtra("PATIENT_STATE", "");
                    bundle.putExtra("PATIENT_COUNTRY_NAME","");
                    bundle.putExtra("PATIENT_WEIGHT", "");
                    bundle.putExtra("PATIENT_HYPERTENSION", "");
                    bundle.putExtra("PATIENT_DIABETES","");
                    bundle.putExtra("PATIENT_SMOKING", "");
                    bundle.putExtra("PATIENT_ALCOHOL", "");
                    bundle.putExtra("PATIENT_DRUG_ABUSE", "");
                    bundle.putExtra("PATIENT_OTHER_DETAILS", "");
                    bundle.putExtra("PATIENT_FAMILY_HISTORY", "");
                    bundle.putExtra("PATIENT_PREV_INTERVENTIONS", "");
                    bundle.putExtra("PATIENT_NEURO_ISSUES", "");
                    bundle.putExtra("PATIENT_KIDNEY_ISSUES", "");
                    bundle.putExtra("PATIENT_HEIGHT", "");
                    mContext.startActivity(bundle);
                }
                else {
                    Log.d(Utils.TAG, " isFirstRunShow FALSE - - MyPatientOphthalHomeActivity");
                    Intent bundle = new Intent(mContext, MyPatientOphthalHomeActivity.class);
                    bundle.putExtra("title","Patient Details");
                    bundle.putExtra("PATIENT_ID", appt.getPatientID());
                    bundle.putExtra("PATIENT_NAME", appt.getPatientName());
                    bundle.putExtra("PATIENT_AGE", "");
                    bundle.putExtra("PATIENT_GENDER", "");
                    bundle.putExtra("PATIENT_MOBILE", appt.getAppointMobile());
                    bundle.putExtra("PATIENT_EMAIL", appt.getAppointEmail());
                    bundle.putExtra("PATIENT_CITY", "");
                    bundle.putExtra("PATIENT_ADDRESS","");
                    bundle.putExtra("PATIENT_STATE", "");
                    bundle.putExtra("PATIENT_COUNTRY_NAME", "");
                    bundle.putExtra("PATIENT_WEIGHT", "");
                    bundle.putExtra("PATIENT_HYPERTENSION", "");
                    bundle.putExtra("PATIENT_DIABETES", "");
                    bundle.putExtra("PATIENT_SMOKING", "");
                    bundle.putExtra("PATIENT_ALCOHOL", "");
                    bundle.putExtra("PATIENT_DRUG_ABUSE", "");
                    bundle.putExtra("PATIENT_OTHER_DETAILS", "");
                    bundle.putExtra("PATIENT_FAMILY_HISTORY", "");
                    bundle.putExtra("PATIENT_PREV_INTERVENTIONS","");
                    bundle.putExtra("PATIENT_NEURO_ISSUES", "");
                    bundle.putExtra("PATIENT_KIDNEY_ISSUES", "");
                    bundle.putExtra("PATIENT_HEIGHT", "");
                    mContext.startActivity(bundle);
                }


            }
        });

    /*    holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("Delete Appointment");
                dialog.setMessage("Are you sure you want to delete this appointment?" );
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        onItemDismiss(position);
                        new sendAppointmentDelete().execute(String.valueOf(appt.getTransactionID()),String.valueOf(appt.getAppointUserId()),String.valueOf(appt.getAppointLoginType()));
                        }
                     })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Action for "Cancel".
                                dialog.cancel();
                            }
                        });

                final AlertDialog alert = dialog.create();
                alert.show();
            }
        }); */

        holder.reschedule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date strDate = null;
                try {
                    strDate = sdf.parse(appt.getVisitDate());
                    if (System.currentTimeMillis() > strDate.getTime()) {
                       // your_date_is_outdated = true;
                        Toast.makeText(mContext, "Sorry, only future appointments can be rescheduled !!!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                      //  your_date_is_outdated = false;
                        openCustomReschedule(appt.getAppointDoctorID(),appt.getDocType(),appt.getHospitalID(),appt.getTransactionID(),appt.getTokenNumber());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    public void onItemDismiss(int position) {
        if(position!=-1 && position<apptList.size())
        {
            apptList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    @Override
    public int getItemCount() {
        return apptList.size();
    }

    class setJsonAppointmentStatus extends AsyncTask<String,String,String> {
        String appoint_status, appoint_docid, appoint_doctype, appoint_hospid, appoint_transactionID;
        ProgressDialog pd1;

        public setJsonAppointmentStatus(String status, String docid, String doctype, String hospid, String tran_id) {
            appoint_status = status;
            appoint_docid = docid;
            appoint_doctype = doctype;
            appoint_hospid = hospid;
            appoint_transactionID = tran_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd1 = new ProgressDialog(mContext);
            pd1.setMessage("Status Change...");
         //   pd1.show();
        }

        @Override
        protected String doInBackground(String... key) {


            Log.d(Utils.TAG, " appoint_status: " + appoint_status);
            Log.d(Utils.TAG, " appoint_docid: " + appoint_docid + " appoint_doctype: "+ appoint_doctype);
            Log.d(Utils.TAG, " appoint_hospid: " + appoint_hospid);
            Log.d(Utils.TAG, " appoint_transactionID: " + appoint_transactionID);

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_APPOINT_STATUS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {

                            ((Activity)mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(Utils.TAG, " resStatus: " + response.toString());
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response);
                                        String staus_res = jsonObject.getString("result");
                                        if(staus_res.equals("success")) {
                                            List<AppointmentList> appointmentArryList  = new ArrayList<>();
                                            JSONArray  jsonArray = jsonObject.getJSONArray("appointment_today_details");
                                            if (jsonArray.length() > 0) {
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

                                                //Set the values
                                                Gson gson = new Gson();
                                                String jsonText = gson.toJson(appointmentArryList);
                                                if (sharedPreferences != null) {
                                                    Log.d(Utils.TAG , " clear appoint"+appointmentArryList.size());
                                                    shareadPreferenceClass.clearAppointmentLists();
                                                    shareadPreferenceClass.setAppointmentList(jsonText);
                                                }
                                            }

                                            Toast.makeText(mContext, "Appointment status changed !!!", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(mContext, "Failed to change appointment status. ", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    if(pd1 != null && pd1.isShowing()){
                                        pd1.dismiss();
                                    }
                                }
                            });
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(pd1 != null && pd1.isShowing()){
                                pd1.dismiss();
                            }
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                    map.put(APIClass.KEY_APPOINT_TRANS_ID,appoint_transactionID);
                    map.put(APIClass.KEY_USERID, appoint_docid);
                    map.put(APIClass.KEY_LOGINTYPE, appoint_doctype);
                    map.put(APIClass.KEY_HOSPITAL_ID, appoint_hospid);
                    map.put(APIClass.KEY_APPOINT_STATUS_VAL, appoint_status);
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(mContext).
                    getRequestQueue();
            AppController.getInstance(mContext).addToRequestQueue(stringRequest1);

            return null;
        }
    }

    private void clearAppointmentList(final String appoint_userid, final String appoint_logintype, final String appoint_hospid) {
        appointmentArryList = new ArrayList<>();

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
                                    }
                                    else {
                                        appointmentArryList = new ArrayList<>();
                                    }
                                    //Set the values
                                    gson = new Gson();
                                    String jsonText = gson.toJson(appointmentArryList);
                                    if (sharedPreferences != null) {
                                        shareadPreferenceClass.clearAppointmentLists();
                                        shareadPreferenceClass.setAppointmentList(jsonText);
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
                map.put(APIClass.KEY_USERID, appoint_userid);
                map.put(APIClass.KEY_LOGINTYPE, appoint_logintype);
                map.put(APIClass.KEY_HOSPITAL_ID, appoint_hospid);
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(mContext).
                getRequestQueue();
        AppController.getInstance(mContext).addToRequestQueue(stringRequest);
    }


    private void openCustomReschedule(final int appointDoctorID, final int docType, final int hospitalID, final String transactionID, final int tokenNumber) {
        final Dialog dialog = new Dialog(mContext, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.appointment_reschedule);

        appoint_date_spinner = (Spinner) dialog.findViewById(R.id.spinner_appt_date);
        appoint_time_spinner = (Spinner) dialog.findViewById(R.id.spinner_appt_time);
        Button submit_btn = (Button)  dialog.findViewById(R.id.appt_reschedule_submit);

        new getJsonAppointmentDates().execute(String.valueOf(appointDoctorID),String.valueOf(docType),String.valueOf(hospitalID));

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(PATIENT_APPOINTMENT_DATE.equals("")||PATIENT_APPOINTMENT_DATE.equals("--Select--")) {
                    Toast.makeText(mContext, "Choose appointment date !!!", Toast.LENGTH_SHORT).show();
                }
                if(PATIENT_APPOINTMENT_TIME.equals("")||PATIENT_APPOINTMENT_TIME.equals("--Select--")) {
                    Toast.makeText(mContext, "Choose appointment time !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d(Utils.TAG, " DATE: " + PATIENT_APPOINTMENT_DATE);
                    Log.d(Utils.TAG, " TIME: " + PATIENT_APPOINTMENT_TIME);

                    submitReschedAppointmentToServer(PATIENT_APPOINTMENT_DATE,PATIENT_APPOINTMENT_TIME,appointDoctorID,
                            docType, hospitalID, transactionID, tokenNumber);
                 //   dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    class getJsonAppointmentDates extends AsyncTask<String,String,String> {
        String appoint_docid, appoint_doctype, appoint_hospid;
        @Override
        protected String doInBackground(String... key) {
            appoint_docid = key[0];
            appoint_doctype = key[1];
            appoint_hospid = key[2];

            Log.d(Utils.TAG, " appoint_docid: " + appoint_docid);
            Log.d(Utils.TAG, " appoint_doctype: " + appoint_doctype);
            Log.d(Utils.TAG, " appoint_hospid: " + appoint_hospid);

            int socketTimeout = 30000; // 30 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_APPOINT_PATIENT_SEARCH,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            Log.d(Utils.TAG, " patient list: "+ response.toString());
                            if(response != null) {
                                JSONObject jsonObject1 = null;  JSONArray jsonArray3 = null;
                                try {
                                    jsonObject1 = new JSONObject(response);
                                    String staus_res = jsonObject1.getString("status");

                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray3 = jsonObject1.getJSONArray("appoint_details");
                                        ArrayList<String> appoint_dateArray = new ArrayList<String>();
                                        final ArrayList<String> appoint_dateIDArray = new ArrayList<String>();
                                        if (jsonArray3.length() > 0) {
                                            appoint_dateArray.add("--Select--");
                                            appoint_dateIDArray.add("0");
                                            for (int i = 0; i < jsonArray3.length(); i++) {
                                                appoint_dateArray.add(jsonArray3.getJSONObject(i).getString("appt_date"));
                                                appoint_dateIDArray.add(jsonArray3.getJSONObject(i).getString("appt_id"));
                                            }

                                            ArrayAdapter<String> apptdateAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_text, appoint_dateArray );
                                            appoint_date_spinner.setAdapter(apptdateAdapter);

                                            appoint_date_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view,
                                                                           int position, long id) {
                                                    Log.d(Utils.TAG, " appoint_date: "+ (String) parent.getItemAtPosition(position));
                                                    String get_state = (String) parent.getItemAtPosition(position);
                                                    Log.d(Utils.TAG, "appt_date_ID: "+ appoint_dateIDArray.get(position).toString());

                                                    if(appoint_dateIDArray.get(position).toString().equals("0")) {
                                                        PATIENT_APPOINTMENT_DATE = "";
                                                    }
                                                    else {
                                                        PATIENT_APPOINTMENT_DATE = appoint_dateIDArray.get(position).toString();
                                                        new getJsonAppointmentTimings().execute(appoint_dateIDArray.get(position).toString(),appoint_docid, appoint_doctype, appoint_hospid);
                                                    }
                                                }
                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {
                                                    PATIENT_APPOINTMENT_DATE = "";
                                                }
                                            });
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
                    map.put(APIClass.KEY_USERID, appoint_docid);
                    map.put(APIClass.KEY_LOGINTYPE, appoint_doctype);
                    map.put(APIClass.KEY_HOSPITAL_ID, appoint_hospid);
                    return map;
                }
            };

            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = AppController.getInstance(mContext).
                    getRequestQueue();
            AppController.getInstance(mContext).addToRequestQueue(stringRequest);

            return null;
        }
    }

    class getJsonAppointmentTimings extends AsyncTask<String,String,String> {
        String appoint_date_id;
        String appoint_docid, appoint_doctype, appoint_hospid;
        @Override
        protected String doInBackground(String... key) {
            appoint_date_id = key[0];
            Log.d(Utils.TAG, " selected appoint_date_id: " + appoint_date_id);

            appoint_docid = key[1];
            appoint_doctype = key[2];
            appoint_hospid = key[3];


            Log.d(Utils.TAG, " time_docid: " + appoint_docid);
            Log.d(Utils.TAG, " time_doctype: " + appoint_doctype);
            Log.d(Utils.TAG, " time_hospid: " + appoint_hospid);


            int socketTimeout = 30000; // 30 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_APPOINT_TIMINGS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG, " timings info: "+ response.toString());
                            if(response != null) {
                                JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null;
                                try {
                                    jsonObject1 = new JSONObject(response);
                                    String staus_res = jsonObject1.getString("status");

                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray1 = jsonObject1.getJSONArray("appoint_timing_details");
                                        ArrayList<String> appoint_TimeArray = new ArrayList<String>();
                                        final ArrayList<String> appoint_timeIDArray = new ArrayList<String>();
                                        if (jsonArray1.length() > 0) {
                                            appoint_TimeArray.add("--Select--");
                                            appoint_timeIDArray.add("0");
                                            for (int i = 0; i < jsonArray1.length(); i++) {
                                                appoint_TimeArray.add(jsonArray1.getJSONObject(i).getString("aapt_time"));
                                                appoint_timeIDArray.add(jsonArray1.getJSONObject(i).getString("aapt_time_id"));
                                            }

                                            ArrayAdapter<String> appttimeAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_text, appoint_TimeArray );
                                            appoint_time_spinner.setAdapter(appttimeAdapter);

                                            appoint_time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view,
                                                                           int position, long id) {
                                                    Log.d(Utils.TAG, " appoint_time: "+ (String) parent.getItemAtPosition(position));
                                                    String get_state = (String) parent.getItemAtPosition(position);
                                                    Log.d(Utils.TAG, "appt_time_ID: "+ appoint_timeIDArray.get(position).toString());

                                                    if(appoint_timeIDArray.get(position).toString().equals("0")) {
                                                        PATIENT_APPOINTMENT_TIME = "";
                                                    }
                                                    else {
                                                        PATIENT_APPOINTMENT_TIME = appoint_timeIDArray.get(position).toString();
                                                    }
                                                }
                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {
                                                    PATIENT_APPOINTMENT_TIME = "";
                                                }
                                            });
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
                    map.put(APIClass.KEY_USERID, appoint_docid);
                    map.put(APIClass.KEY_LOGINTYPE, appoint_doctype);
                    map.put(APIClass.KEY_APPOINT_DATE_ID, appoint_date_id);
                    map.put(APIClass.KEY_HOSPITAL_ID, appoint_hospid);
                    return map;
                }
            };

            stringRequest.setRetryPolicy(policy);

            RequestQueue requestQueue = AppController.getInstance(mContext).
                    getRequestQueue();
            AppController.getInstance(mContext).addToRequestQueue(stringRequest);

            return null;
        }
    }

    private void submitReschedAppointmentToServer(final String patient_appointment_date, final String patient_appointment_time,
                                                  final int appointDoctorID, final int docType, final int hospitalID, final String transactionID,
                                                  int tokenNumber) {

        Log.d(Utils.TAG, " date: "+ patient_appointment_date);
        Log.d(Utils.TAG, " times: "+ patient_appointment_time);
        Log.d(Utils.TAG, " userid: "+ appointDoctorID);
        Log.d(Utils.TAG, " docType: "+ docType);
        Log.d(Utils.TAG, " hospitalID: "+ hospitalID);
        Log.d(Utils.TAG, " transactionID: "+ transactionID);

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_APPOINT_RESCHEDULE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, " reschedlist: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("result");
                                String description_res = jsonObject.getString("appt_reschedule_status");
                               // Toast.makeText(mContext, description_res, Toast.LENGTH_SHORT).show();

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext, R.style.CustomDialog);
                                alertDialogBuilder.setMessage(description_res);
                                alertDialogBuilder.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                            }
                                        });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();

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
                map.put(APIClass.KEY_USERID, String.valueOf(appointDoctorID));
                map.put(APIClass.KEY_LOGINTYPE, String.valueOf(docType));
                map.put(APIClass.KEY_HOSPITAL_ID, String.valueOf(hospitalID));
                map.put(APIClass.KEY_APPOINT_TRANS_ID, transactionID);
                map.put(APIClass.KEY_APPOINT_DATE_VAL, patient_appointment_date);
                map.put(APIClass.KEY_APPOINT_TIME_VAL, patient_appointment_time);
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(mContext).
                getRequestQueue();
        AppController.getInstance(mContext).addToRequestQueue(stringRequest);
    }

}
