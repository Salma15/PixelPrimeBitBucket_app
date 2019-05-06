package com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.medisensehealth.fdccontributor.DataModel.Examinations;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.views.CustomEditText;
import com.medisensehealth.fdccontributor.views.CustomTextView;

import java.util.List;

/**
 * Created by HP on 31-05-2018.
 */

public class ExaminationsAdapters extends RecyclerView.Adapter<ExaminationsAdapters.MyViewHolder> {

    private List<Examinations> examsList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView name, normal;
        public CustomEditText test_list_findings;
        public LinearLayout list_item;
        public ImageView delete_item;
        public Spinner result_spinner;

        public MyViewHolder(View view) {
            super(view);
            name = (CustomTextView) view.findViewById(R.id.exam_list_name);
            result_spinner = (Spinner) view.findViewById(R.id.exam_list_reult);
            test_list_findings = (CustomEditText) view.findViewById(R.id.test_list_findings);
            list_item = (LinearLayout) view.findViewById(R.id.examlist_item);
            delete_item = (ImageView) view.findViewById(R.id.exam_list_delete);
        }
    }

    public ExaminationsAdapters(Context context, List<Examinations> examsList) {
        this.mContext = context;
        this.examsList = examsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.examination_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Examinations list = examsList.get(position);

        if(position%2 == 0) {
            holder.list_item.setBackgroundColor(mContext.getResources().getColor(R.color.row_even_color));
        }
        else {
            holder.list_item.setBackgroundColor(mContext.getResources().getColor(R.color.table_row_odd));
        }

        holder.name.setText(list.getExaminationName());
        holder.name.setVisibility(View.VISIBLE);
        holder.test_list_findings.setText(list.getExaminationFindings());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.examination_results, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.result_spinner.setAdapter(adapter);


        if (list.getExaminationResults() != null) {
            String comparedValue = "";
            if(list.getExaminationResults().equals("")) {
                comparedValue = "--Select--";
            }
            else {
                comparedValue = list.getExaminationResults();
            }
            int spinnerPosition = adapter.getPosition(comparedValue);
            holder.result_spinner.setSelection(spinnerPosition);
        }

        holder.result_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               // Toast.makeText(mContext, "This is " + adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();

                if(position!=-1 && position<examsList.size()) {
                    if(examsList.get(position).getExaminationName().equals(list.getExaminationName())) {
                        list.setExaminationResults(adapterView.getItemAtPosition(i).toString());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        holder.test_list_findings.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(position!=-1 && position<examsList.size()) {
                    if(examsList.get(position).getExaminationName().equals(list.getExaminationName())) {
                        list.setExaminationFindings(s.toString());
                    }
                }
            }
        });


        holder.list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(mContext, R.style.CustomDialog);
                dialog1.setTitle("Examination Information");
                dialog1.setMessage("Name: " + list.getExaminationName().toString() +"\n\nID: "+ list.getExaminationID());
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

        holder.delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext, R.style.CustomDialog);
                dialog.setTitle("Remove Examination?");
                dialog.setMessage("Do you want to remove from Examination list?");
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

    public void onItemDismiss(int position) {
        if (position != -1 && position < examsList.size()) {
            examsList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }


    @Override
    public int getItemCount() {
        return examsList.size();
    }
}
