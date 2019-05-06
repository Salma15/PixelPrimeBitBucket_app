package com.medisensehealth.fdccontributor.adapter.emr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medisensehealth.fdccontributor.DataModel.MyPatientsList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.mypatientOphthalmology.MyPatientOphthalHomeActivity;
import com.medisensehealth.fdccontributor.activities.mypatientOphthalmology.RefreshOphthalEMRActivity;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by HP on 26-03-2018.
 */

public class MyPatientsAdapter extends RecyclerView.Adapter<MyPatientsAdapter.MyViewHolder> {

    private List<MyPatientsList> mypatientsList;
    private Context mContext;
    SharedPreferences sharedPreferences;
    ShareadPreferenceClass shareadPreferenceClass;
    int SPECIALIZATION_GROUP_ID;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView pat_name, reg_date, pat_id;
        public CustomTextView pat_mob, pat_place;
        public CardView pat_layout;

        public MyViewHolder(View view) {
            super(view);
            pat_name = (CustomTextView) view.findViewById(R.id.mypat_list_pname);
            reg_date = (CustomTextView) view.findViewById(R.id.mypat_list_regdate);
            pat_mob = (CustomTextView) view.findViewById(R.id.mypat_list_phone);
            pat_layout = (CardView) view.findViewById(R.id.mypat_layout);
            //  pat_id  = (TextView) view.findViewById(R.id.mypat_list_pid);
            pat_place = (CustomTextView) view.findViewById(R.id.mypat_list_place);
        }
    }

    public MyPatientsAdapter(Context context, List<MyPatientsList> pList) {
        this.mypatientsList = pList;
        this.mContext = context;

        this.shareadPreferenceClass = new ShareadPreferenceClass(context);
        this.sharedPreferences = shareadPreferenceClass.getSharedPreferences(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mypatient_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MyPatientsList mypatient = mypatientsList.get(position);
        holder.pat_name.setText(mypatient.getPatientName());
       // holder.reg_date.setText(mypatient.getMyPatientRegDate());
        holder.pat_mob.setText(mypatient.getMobile());
     //   holder.pat_id.setText("#"+mypatient.getMyPatientID());
        holder.pat_place.setText(mypatient.getLocation());

        if(position % 2==0)
            holder.pat_layout.setCardBackgroundColor(mContext.getResources().getColor(R.color.row_even_color));
        else
            holder.pat_layout.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));



        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String inputDateStr= mypatient.getMyPatientRegDate();
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
            String outputDateStr = outputFormat.format(date);
            holder.reg_date.setText(outputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.pat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Toast.makeText(mContext, mypatient.getMyPatientID(), Toast.LENGTH_SHORT ).show();
                boolean isFirstRunShow = sharedPreferences.getBoolean("FIRSTRUNLOADEMR", true);
                if (isFirstRunShow)
                {
                    Log.d(Utils.TAG, " isFirstRunShow TRUE - - RefreshOphthalEMRActivity");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("FIRSTRUNLOADEMR", false);
                    editor.commit();

                    Intent bundle = new Intent(mContext, RefreshOphthalEMRActivity.class);
                    bundle.putExtra("title","Refresh EMR");
                    bundle.putExtra("PATIENT_ID", mypatient.getMyPatientID());
                    bundle.putExtra("PATIENT_NAME", mypatient.getPatientName());
                    bundle.putExtra("PATIENT_AGE",  mypatient.getAge());
                    bundle.putExtra("PATIENT_GENDER", String.valueOf(mypatient.getGender()));
                    bundle.putExtra("PATIENT_MOBILE", mypatient.getMobile());
                    bundle.putExtra("PATIENT_EMAIL", mypatient.getEmail());
                    bundle.putExtra("PATIENT_CITY", mypatient.getLocation());
                    bundle.putExtra("PATIENT_ADDRESS", mypatient.getAddress());
                    bundle.putExtra("PATIENT_STATE", mypatient.getState());
                    bundle.putExtra("PATIENT_COUNTRY_NAME",mypatient.getCountry());
                    bundle.putExtra("PATIENT_WEIGHT", String.valueOf(mypatient.getWeight()));
                    bundle.putExtra("PATIENT_HYPERTENSION", String.valueOf(mypatient.getHyperCondition()));
                    bundle.putExtra("PATIENT_DIABETES", String.valueOf(mypatient.getDiabetes()));
                    bundle.putExtra("PATIENT_SMOKING", mypatient.getMyPatientSmoke());
                    bundle.putExtra("PATIENT_ALCOHOL", mypatient.getMyPatientAlcohol());
                    bundle.putExtra("PATIENT_DRUG_ABUSE", mypatient.getMyPatientDrugAbuse());
                    bundle.putExtra("PATIENT_OTHER_DETAILS", mypatient.getMyPatientOtherDetails());
                    bundle.putExtra("PATIENT_FAMILY_HISTORY", mypatient.getMyPatientFamilyHistory());
                    bundle.putExtra("PATIENT_PREV_INTERVENTIONS", mypatient.getMyPatientPreviousInterventions());
                    bundle.putExtra("PATIENT_NEURO_ISSUES", mypatient.getMyPatientNeuroIssues());
                    bundle.putExtra("PATIENT_KIDNEY_ISSUES", mypatient.getMyPatientKidneyIssues());
                    bundle.putExtra("PATIENT_HEIGHT", String.valueOf(mypatient.getHeight()));
                    mContext.startActivity(bundle);
                }
                else {
                    Log.d(Utils.TAG, " isFirstRunShow FALSE - - MyPatientOphthalHomeActivity");
                    Intent bundle = new Intent(mContext, MyPatientOphthalHomeActivity.class);
                    bundle.putExtra("title","Patient Details");
                    bundle.putExtra("PATIENT_ID", mypatient.getMyPatientID());
                    bundle.putExtra("PATIENT_NAME", mypatient.getPatientName());
                    bundle.putExtra("PATIENT_AGE", mypatient.getAge());
                    bundle.putExtra("PATIENT_GENDER", String.valueOf(mypatient.getGender()));
                    bundle.putExtra("PATIENT_MOBILE", mypatient.getMobile());
                    bundle.putExtra("PATIENT_EMAIL", mypatient.getEmail());
                    bundle.putExtra("PATIENT_CITY", mypatient.getLocation());
                    bundle.putExtra("PATIENT_ADDRESS", mypatient.getAddress());
                    bundle.putExtra("PATIENT_STATE", mypatient.getState());
                    bundle.putExtra("PATIENT_COUNTRY_NAME", mypatient.getCountry());
                    bundle.putExtra("PATIENT_WEIGHT", String.valueOf(mypatient.getWeight()));
                    bundle.putExtra("PATIENT_HYPERTENSION", String.valueOf(mypatient.getHyperCondition()));
                    bundle.putExtra("PATIENT_DIABETES", String.valueOf(mypatient.getDiabetes()));
                    bundle.putExtra("PATIENT_SMOKING", mypatient.getMyPatientSmoke());
                    bundle.putExtra("PATIENT_ALCOHOL", mypatient.getMyPatientAlcohol());
                    bundle.putExtra("PATIENT_DRUG_ABUSE", mypatient.getMyPatientDrugAbuse());
                    bundle.putExtra("PATIENT_OTHER_DETAILS", mypatient.getMyPatientOtherDetails());
                    bundle.putExtra("PATIENT_FAMILY_HISTORY", mypatient.getMyPatientFamilyHistory());
                    bundle.putExtra("PATIENT_PREV_INTERVENTIONS", mypatient.getMyPatientPreviousInterventions());
                    bundle.putExtra("PATIENT_NEURO_ISSUES", mypatient.getMyPatientNeuroIssues());
                    bundle.putExtra("PATIENT_KIDNEY_ISSUES", mypatient.getMyPatientKidneyIssues());
                    bundle.putExtra("PATIENT_HEIGHT", String.valueOf(mypatient.getHeight()));
                    mContext.startActivity(bundle);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mypatientsList.size();
    }
}
