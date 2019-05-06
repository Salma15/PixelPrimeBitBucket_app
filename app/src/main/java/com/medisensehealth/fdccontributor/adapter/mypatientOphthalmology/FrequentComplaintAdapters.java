package com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.medisensehealth.fdccontributor.DataModel.FrequentChiefComplaints;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.views.CustomTextView;

import java.util.List;

/**
 * Created by HP on 28-05-2018.
 */

public class FrequentComplaintAdapters  extends RecyclerView.Adapter<FrequentComplaintAdapters.MyViewHolder>  {
    private List<FrequentChiefComplaints> freqcompList;
    private Context mContext;
    private static OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public CustomTextView freq_name;
        public LinearLayout list_item;

        public MyViewHolder(View view) {
            super(view);
            freq_name = (CustomTextView) view.findViewById(R.id.frquent_item_name);
            list_item = (LinearLayout) view.findViewById(R.id.frquent_item);

        }

    }

    public FrequentComplaintAdapters(Context context, List<FrequentChiefComplaints> fcompList) {
        this.mContext = context;
        this.freqcompList = fcompList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frequent_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final FrequentChiefComplaints list = freqcompList.get(position);

        holder.freq_name.setText(list.getFreqSymptomsName());

        holder.list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onItemClick(view, position,list.getFreqSymptomsID(),list.getFreqSymptomsName().toString(),list.getFreqDocID(),list.getFreqDocType());
            }
        });

        holder.list_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder dialog1 = new AlertDialog.Builder(mContext, R.style.CustomDialog);
                dialog1.setTitle("Frequent Complaint Information");
                dialog1.setMessage("Name: " + list.getFreqSymptomsName().toString() +"\n\nID: "+ list.getFreqSymptomsID());
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


    }

    @Override
    public int getItemCount() {
        return freqcompList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position, int freqSymptomsID, String s, int freqDocID, int freqDocType);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
