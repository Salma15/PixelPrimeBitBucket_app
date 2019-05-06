package com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.medisensehealth.fdccontributor.DataModel.Treatments;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.views.CustomEditText;
import com.medisensehealth.fdccontributor.views.CustomTextView;

import java.util.List;

/**
 * Created by HP on 30-07-2018.
 */

public class TreatmentsViewAdapters extends RecyclerView.Adapter<TreatmentsViewAdapters.MyViewHolder> {

    private List<Treatments> treatmentList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView name, normal;
        public CustomEditText value_enter;
        public LinearLayout list_item;
        public ImageView delete_item;

        public MyViewHolder(View view) {
            super(view);
            name = (CustomTextView) view.findViewById(R.id.test_list_name);
            normal = (CustomTextView) view.findViewById(R.id.test_list_normal);
            value_enter = (CustomEditText) view.findViewById(R.id.test_list_value);
            list_item = (LinearLayout) view.findViewById(R.id.testlist_item);
            delete_item = (ImageView) view.findViewById(R.id.test_list_delete);
        }
    }

    public TreatmentsViewAdapters(Context context, List<Treatments> treatmentList) {
        this.mContext = context;
        this.treatmentList = treatmentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Treatments list = treatmentList.get(position);

        if(position%2 == 0) {
            holder.list_item.setBackgroundColor(mContext.getResources().getColor(R.color.row_even_color));
        }
        else {
            holder.list_item.setBackgroundColor(mContext.getResources().getColor(R.color.table_row_odd));
        }

        holder.name.setText(list.getTreatmentName());
        holder.name.setVisibility(View.VISIBLE);
        holder.value_enter.setVisibility(View.GONE);
        holder.normal.setVisibility(View.GONE);

        holder.list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(mContext, R.style.CustomDialog);
                dialog1.setTitle("Treatment Information");
                dialog1.setMessage("Name: " + list.getTreatmentName().toString() +"\n\nID: "+ list.getTreatmentID());
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

        holder.delete_item.setVisibility(View.INVISIBLE);
        holder.delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext, R.style.CustomDialog);
                dialog.setTitle("Remove Treatment?");
                dialog.setMessage("Do you want to remove from treatment list?");
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
        if (position != -1 && position < treatmentList.size()) {
            treatmentList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }


    @Override
    public int getItemCount() {
        return treatmentList.size();
    }
}
