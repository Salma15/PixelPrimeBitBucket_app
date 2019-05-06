package com.medisensehealth.fdccontributor.adapter.patients;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.medisensehealth.fdccontributor.DataModel.PatientsList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.patients.PatientDetailsActivity;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 20-02-2017.
 */
public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.MyViewHolder>  {

    private List<PatientsList> patientList;
    Context mContext;
    FragmentManager fragManager;
    FragmentTransaction fragTransaction;
    Date status_date;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView pName, pAge, pCity, pStatus, pDocName, pID, pStatusDate, pStatusLabel;
        public CardView cardViewPatient;
        RelativeLayout plist;
        public ImageView doc_icon;

        public MyViewHolder(View view) {
            super(view);
            pName = (CustomTextView) view.findViewById(R.id.edit_patient_name);
            //   pAge = (CustomTextView) view.findViewById(R.id.edit_patient_age);
            pCity = (CustomTextView) view.findViewById(R.id.edit_patient_city);
            pStatus = (CustomTextView) view.findViewById(R.id.edit_patient_status);
            cardViewPatient = (CardView) view.findViewById(R.id.patient_list_cardview);
            //   plist = (RelativeLayout)view.findViewById(R.id.relativeLayout_plist);
            pDocName = (CustomTextView) view.findViewById(R.id.edit_patient_docname);
            //  doc_icon = (ImageView) view.findViewById(R.id.doctor_icon);
            pID = (CustomTextView) view.findViewById(R.id.edit_patient_id);
            pStatusDate = (CustomTextView) view.findViewById(R.id.edit_patient_refondate);
            //   pStatusLabel = (CustomTextView) view.findViewById(R.id.text_respondedOn);
        }
    }

    public PatientsAdapter(Context context, List<PatientsList> psList) {
        this.patientList = psList;
        this.mContext = context;
        fragManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patients_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final PatientsList patient = patientList.get(position);
        String status = null;
        holder.pName.setText(patient.getPatientName().trim());

        if(position % 2==0)
            holder.cardViewPatient.setCardBackgroundColor(mContext.getResources().getColor(R.color.row_even_color));
        else
            holder.cardViewPatient.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));


        /*if(patient.getPatientAge().equals("")) {
            holder.pAge.setVisibility(View.GONE);
        }
        else {
            holder.pAge.setText("Age: " +patient.getPatientAge().trim());
        }*/

        if(patient.getPatientCity().equals("")) {
            holder.pCity.setVisibility(View.GONE);
        }
        else {
            holder.pCity.setText(patient.getPatientCity().trim());
        }

        holder.pDocName.setText(patient.getPatientDocName().trim());
        holder.pID.setText("Reg ID: "+String.valueOf(patient.getPatientId()));

        final int patient_id = patient.getPatientId();
        final int doc_refid = patient.getPatientDocId();

        if(patient.getPatientStatus() == 1){
            status = "New";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_red);
        }
        else if(patient.getPatientStatus() == 2){
            status = "Pending";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_yellow);
        }
        else if(patient.getPatientStatus() == 3){
            status = "Awaiting";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_red);
        }
        else if(patient.getPatientStatus() == 4 ){
            status = "Not Qualified";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_red);
        }
        else if(patient.getPatientStatus() == 5){
            status = "Responded";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_green);
        }
        else if(patient.getPatientStatus() == 6){
            status = "Response P-Failed";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_green);
        }
        else if(patient.getPatientStatus() == 7){
            status = "Staged";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_green);
        }
        else if(patient.getPatientStatus() == 8){
            status = "OP Desired";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_green);
        }
        else if(patient.getPatientStatus() == 9){
            status = "IP Treated";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_green);
        }
        else if(patient.getPatientStatus() == 10){
            status = "Not Converted";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_red);
        }
        else if(patient.getPatientStatus() == 11){
            status = "Invoiced";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_safron);
        }
        else if(patient.getPatientStatus() == 12){
            status = "Payment Received";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_safron);
        }
        else if(patient.getPatientStatus() == 13){
            status = "OP Visited";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_green);
        }
        else if(patient.getPatientStatus() == 14){
            status = "Not Responded";
            holder.pStatus.setBackgroundResource(R.drawable.round_edge_layout_red);
        }

        holder.pStatus.setText(status);

        String[] status_date_arrray = patient.getPatientStatusTime().split(" ");
        status_date_arrray[0] = status_date_arrray[0].replace("-", "/");

        DateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String inputDateStr= status_date_arrray[0];
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
            String outputDateStr = outputFormat.format(date);
            holder.pStatusDate.setText(outputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

      /*  String[] status_date_arrray = patient.getPatientStatusTime().split(" ");
        status_date_arrray[0] = status_date_arrray[0].replace("-", "/");

        if(patient.getPatientLoginType().equals("1")) {
            holder.pStatusLabel.setText("Referred On");
            holder.pStatusDate.setText(status_date_arrray[0]);
        }
        else  if(patient.getPatientLoginType().equals("2")) {
            holder.pStatusLabel.setText("Created On");
            holder.pStatusDate.setText(status_date_arrray[0]);
        }
        else  if(patient.getPatientLoginType().equals("3")) {
            holder.pStatusLabel.setText("Referred On");
            holder.pStatusDate.setText(status_date_arrray[0]);
        }*/


        if(Utils.USER_LOGIN_TYPE.equals("1")) {
            holder.pDocName.setText(patient.getPatientReferBy().trim());
       //     holder.doc_icon.setVisibility(View.INVISIBLE);
        }

        holder.cardViewPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.getConnectivityStatusString(mContext).equalsIgnoreCase("enabled")) {
                   /* Intent theintent = new Intent(mContext, PatientDetailActivity.class);
                    theintent.putExtra("PATIENT_ID",patient_id);
                    theintent.putExtra("DOCREF_ID",doc_refid);
                    theintent.putExtra("PATIENT_VIEW","VIEW");
                    mContext.startActivity(theintent);*/

                    Intent i1 = new Intent(mContext, PatientDetailsActivity.class);
                    i1.putExtra("title","Patient Details");
                    i1.putExtra("PATIENT_ID", patient_id);
                    i1.putExtra("DOCREF_ID", doc_refid);
                    i1.putExtra("PATIENT_VIEW", "VIEW");
                    mContext.startActivity(i1);
                } else {
                    AppUtils.showCustomAlertMessage(mContext, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

}
