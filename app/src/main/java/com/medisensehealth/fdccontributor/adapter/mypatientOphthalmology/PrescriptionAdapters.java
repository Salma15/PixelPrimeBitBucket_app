package com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.medisensehealth.fdccontributor.DataModel.DrugAllery;
import com.medisensehealth.fdccontributor.DataModel.FrequentPrescription;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.database.MedisensePracticeDB;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 02-06-2018.
 */

public class PrescriptionAdapters extends RecyclerView.Adapter<PrescriptionAdapters.MyViewHolder>  {
    private List<FrequentPrescription> prescList;
    private Context mContext;
    List<DrugAllery> drugAlleryListArraylist;
    int USER_ID;
    String USER_NAME,USER_LOGIN_TYPE;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView trade_name, generic_name;
        public CustomTextView dosage;
        public LinearLayout list_item;
        public ImageView delete_item, danger_icon;

        public MyViewHolder(View view) {
            super(view);
            trade_name = (CustomTextView) view.findViewById(R.id.presc_trade_name);
            generic_name = (CustomTextView) view.findViewById(R.id.presc_gen_name);
            dosage = (CustomTextView) view.findViewById(R.id.presc_dosage);
            list_item = (LinearLayout) view.findViewById(R.id.presc_list_item);
            delete_item = (ImageView) view.findViewById(R.id.presc_list_delete);
            danger_icon = (ImageView) view.findViewById(R.id.presc_danger_image);
        }
    }

    public PrescriptionAdapters(Context context, List<FrequentPrescription> prescList) {
        this.mContext = context;
        this.prescList = prescList;
        this.drugAlleryListArraylist = new ArrayList<>();
        this.shareadPreferenceClass = new ShareadPreferenceClass(context);
        this.sharedPreferences = shareadPreferenceClass.getSharedPreferences(context);
        if(( this.sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
        }
        else  if(( this.sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
        }
        else  if(( this.sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prescription_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final FrequentPrescription list = prescList.get(position);

        if(position%2 == 0) {
            holder.list_item.setBackgroundColor(mContext.getResources().getColor(R.color.row_even_color));
        }
        else {
            holder.list_item.setBackgroundColor(mContext.getResources().getColor(R.color.table_row_odd));
        }

        List<DrugAllery> freqAllergy = MedisensePracticeDB.getAllDrugAllery(mContext, USER_LOGIN_TYPE, USER_ID);
        if(freqAllergy.size() > 0) {
            drugAlleryListArraylist = new ArrayList<>();
            for (int j = 0; j < freqAllergy.size(); j++) {
                drugAlleryListArraylist.add(new DrugAllery(freqAllergy.get(j).getAllergyId(),freqAllergy.get(j).getGenericId(),
                        freqAllergy.get(j).getGenericName(),freqAllergy.get(j).getPatientId(),
                        freqAllergy.get(j).getDocId(),freqAllergy.get(j).getDocType(),
                        freqAllergy.get(j).getUserId(),freqAllergy.get(j).getLoginType()));

                if(freqAllergy.get(j).getGenericId() == list.getGenericId()) {
                    holder.danger_icon.setVisibility(View.VISIBLE);

                    Animation animation = new AlphaAnimation(1, 0);
                    animation.setDuration(1500);
                    animation.setInterpolator(new LinearInterpolator());
                    animation.setRepeatCount(Animation.INFINITE);
                    animation.setRepeatMode(Animation.REVERSE);
                    holder.danger_icon.startAnimation(animation);
                }
                else {
                    holder.danger_icon.setVisibility(View.GONE);
                }

            }
            Log.d(Utils.TAG, "total freqAllergy: " + freqAllergy.size());
        }

        holder.trade_name.setText(list.getTradeName());
        holder.generic_name.setText(list.getGenericName());
        holder.dosage.setText(list.getDosage());

        holder.list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           customAddPrescriptionViews(list,position);
            }
        });

        holder.list_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(mContext, R.style.CustomDialog);
                dialog1.setTitle("Prescription Information");
                dialog1.setMessage("Product ID: " + list.getPrescriptionId() +"\n\nTrade Name: "+ list.getTradeName()
                        +"\n\nGeneric Name: "+ list.getTradeName()+"\n\nFrequency: "+ list.getDosage()
                        +"\n\nTimings: "+ list.getTimings()+"\n\nDuration: "+ list.getDuration());
                dialog1.setPositiveButton("Done!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                final AlertDialog alert = dialog1.create();
                alert.show();
                return false;
            }
        });

        holder.delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext, R.style.CustomDialog);
                dialog.setTitle("Remove Prescription?");
                dialog.setMessage("Do you want to remove from Prescription list?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        onItemDismiss(position);
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
        });
    }

    private void customAddPrescriptionViews(final FrequentPrescription list, final int position) {

        final Dialog dialog = new Dialog(mContext, R.style.DialogAnimationView);
        dialog.setContentView(R.layout.custom_precription_add);
        dialog.setTitle("Add Prescriptions");
        dialog.setCanceledOnTouchOutside(true);

        final AutoCompleteTextView productName = (AutoCompleteTextView)  dialog.findViewById(R.id.add_presc_prodname);
        final AutoCompleteTextView genericName = (AutoCompleteTextView)  dialog.findViewById(R.id.add_presc_genname);
        final AutoCompleteTextView frequency = (AutoCompleteTextView)  dialog.findViewById(R.id.add_presc_frequency);
        final AutoCompleteTextView timings = (AutoCompleteTextView)  dialog.findViewById(R.id.add_presc_timings);
        final AutoCompleteTextView durations = (AutoCompleteTextView)  dialog.findViewById(R.id.add_presc_duration);
        final AutoCompleteTextView note = (AutoCompleteTextView)  dialog.findViewById(R.id.add_presc_note);
        final CustomTextViewBold prodDescription = (CustomTextViewBold)  dialog.findViewById(R.id.add_presc_description);
        Spinner timings_spinner = (Spinner) dialog.findViewById(R.id.timings_spinner);
        final Button submit_btn = (Button)  dialog.findViewById(R.id.add_presc_submit);

        productName.setText(list.getTradeName());
        genericName.setText(list.getGenericName());
        frequency.setText(list.getDosage());
        timings.setText(list.getTimings());
        durations.setText(list.getDuration());

        ArrayAdapter<String> timingsPrescArray = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, mContext.getResources().getStringArray(R.array.prescTimingsArray) );
        timingsPrescArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timings_spinner.setAdapter(timingsPrescArray);

        String compareValue = "";
        if(list.getTimings().equalsIgnoreCase("0")) {
            compareValue = "--Select--";
        }
        else if(list.getTimings().equalsIgnoreCase("1")) {
            compareValue = "After Dinner";
        }
        else if(list.getTimings().equalsIgnoreCase("2")) {
            compareValue = "After Lunch";
        }
        else if(list.getTimings().equalsIgnoreCase("3")) {
            compareValue = "Before Breakfast";
        }
        else if(list.getTimings().equalsIgnoreCase("4")) {
            compareValue = "When needed";
        }
        else {
            compareValue = "--Select--";
        }

       /* if (compareValue != null) {
            int spinnerPosition = timingsPrescArray.getPosition(compareValue);
            timings_spinner.setSelection(spinnerPosition);
        }*/

        timings_spinner.setSelection(getIndex(timings_spinner, compareValue));

        timings_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              //  Toast.makeText(mContext, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                String timings_id = "0";
                if(adapterView.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    timings_id = "0";
                }
                else if(adapterView.getSelectedItem().toString().equalsIgnoreCase("After Dinner")) {
                    timings_id = "1";
                }
                else if(adapterView.getSelectedItem().toString().equalsIgnoreCase("After Lunch")) {
                    timings_id = "2";
                }
                else if(adapterView.getSelectedItem().toString().equalsIgnoreCase("Before Breakfast")) {
                    timings_id = "3";
                }
                else if(adapterView.getSelectedItem().toString().equalsIgnoreCase("When needed")) {
                    timings_id = "4";
                }

                if(position!=-1 && position<prescList.size()) {
                    if(prescList.get(position).getTimings() == list.getTimings()) {
                        list.setTimings(timings_id);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final ImageView close_btn = (ImageView) dialog.findViewById(R.id.add_presc_close);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm1 = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(close_btn.getWindowToken(), 0);
                dialog.dismiss();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm1 = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(submit_btn.getWindowToken(), 0);
                notifyItemRangeChanged(position, getItemCount());
                dialog.dismiss();
            }
        });

        frequency.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(position!=-1 && position<prescList.size()) {
                    if(prescList.get(position).getDosage() == list.getDosage()) {
                        list.setDosage(s.toString());
                    }
                }
            }
        });

        productName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(position!=-1 && position<prescList.size()) {
                    if(prescList.get(position).getTradeName() == list.getTradeName()) {
                        list.setTradeName(s.toString());
                    }
                }
            }
        });

        genericName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(position!=-1 && position<prescList.size()) {
                    if(prescList.get(position).getGenericName() == list.getGenericName()) {
                        list.setGenericName(s.toString());
                    }
                }
            }
        });

        timings.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(position!=-1 && position<prescList.size()) {
                    if(prescList.get(position).getTimings() == list.getTimings()) {
                        list.setTimings(s.toString());
                    }
                }
            }
        });

        durations.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(position!=-1 && position<prescList.size()) {
                    if(prescList.get(position).getDuration() == list.getDuration()) {
                        list.setDuration(s.toString());
                    }
                }
            }
        });


        dialog.show();
    }

    private int getIndex(Spinner timings_spinner, String compareValue) {
        for (int i=0;i<timings_spinner.getCount();i++){
            if (timings_spinner.getItemAtPosition(i).toString().equalsIgnoreCase(compareValue)){
                return i;
            }
        }

        return 0;
    }

    public void onItemDismiss(int position) {
        if (position != -1 && position < prescList.size()) {
            prescList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }


    @Override
    public int getItemCount() {
        return prescList.size();
    }
}
