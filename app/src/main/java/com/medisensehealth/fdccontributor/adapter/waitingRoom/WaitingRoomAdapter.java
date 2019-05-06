package com.medisensehealth.fdccontributor.adapter.waitingRoom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medisensehealth.fdccontributor.DataModel.AppointmentList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.mypatientOphthalmology.MyPatientOphthalHomeActivity;
import com.medisensehealth.fdccontributor.activities.mypatientOphthalmology.RefreshOphthalEMRActivity;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextViewItalic;
import com.medisensehealth.fdccontributor.views.CustomTextViewSemiBold;

import java.util.List;

/**
 * Created by HP on 24-07-2018.
 */

public class WaitingRoomAdapter extends RecyclerView.Adapter<WaitingRoomAdapter.MyViewHolder>  {
    private List<AppointmentList> apptList;
    private Context mContext;
    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    int SPECIALIZATION_GROUP_ID;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextViewSemiBold token_num, patient_name;
        public CustomTextViewItalic visit_time, status;
        public CardView item_row;

        public MyViewHolder(View view) {
            super(view);
            token_num = (CustomTextViewSemiBold) view.findViewById(R.id.wait_token_num);
            patient_name = (CustomTextViewSemiBold) view.findViewById(R.id.wait_name);
            visit_time = (CustomTextViewItalic) view.findViewById(R.id.wait_visittime);
            status = (CustomTextViewItalic) view.findViewById(R.id.wait_status);
            item_row  = (CardView) view.findViewById(R.id.waiting_cardView);
        }
    }

    public WaitingRoomAdapter(Context context, List<AppointmentList> apptListe) {
        this.apptList = apptListe;
        this.mContext = context;

        this.shareadPreferenceClass = new ShareadPreferenceClass(context);
        this.sharedPreferences = shareadPreferenceClass.getSharedPreferences(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.waitingroom_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final AppointmentList appoint = apptList.get(position);

        holder.patient_name.setText(appoint.getPatientName());

        holder.status.setText(appoint.getStatus());

        if(appoint.getVisitTime().equals("")) {
            holder.visit_time.setVisibility(View.INVISIBLE);
        }
        else {
            holder.visit_time.setVisibility(View.VISIBLE);
            holder.visit_time.setText(appoint.getVisitTime());
        }

        if(appoint.getTokenNumber() == 555) {
            holder.token_num.setText("Online");
            holder.token_num.setTextSize(12);
        }
        else {
            holder.token_num.setText(String.valueOf(appoint.getTokenNumber()));
        }

        holder.item_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isFirstRunShow = sharedPreferences.getBoolean("FIRSTRUNLOADEMR", true);
                if (isFirstRunShow)
                {
                    Log.d(Utils.TAG, " isFirstRunShow TRUE - - RefreshOphthalEMRActivity");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("FIRSTRUNLOADEMR", false);
                    editor.commit();

                    Intent bundle = new Intent(mContext, RefreshOphthalEMRActivity.class);
                    bundle.putExtra("title","Refresh EMR");
                    bundle.putExtra("PATIENT_ID",  appoint.getPatientID());
                    bundle.putExtra("PATIENT_NAME", appoint.getPatientName());
                    bundle.putExtra("PATIENT_AGE", "");
                    bundle.putExtra("PATIENT_GENDER", "");
                    bundle.putExtra("PATIENT_MOBILE", appoint.getAppointMobile());
                    bundle.putExtra("PATIENT_EMAIL", appoint.getAppointEmail());
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
                    bundle.putExtra("PATIENT_ID", appoint.getPatientID());
                    bundle.putExtra("PATIENT_NAME", appoint.getPatientName());
                    bundle.putExtra("PATIENT_AGE", "");
                    bundle.putExtra("PATIENT_GENDER", "");
                    bundle.putExtra("PATIENT_MOBILE", appoint.getAppointMobile());
                    bundle.putExtra("PATIENT_EMAIL", appoint.getAppointEmail());
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

    }

    @Override
    public int getItemCount() {
        return apptList.size();
    }
}
