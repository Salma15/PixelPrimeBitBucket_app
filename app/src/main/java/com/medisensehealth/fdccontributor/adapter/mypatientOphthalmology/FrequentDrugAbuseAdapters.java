package com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.medisensehealth.fdccontributor.DataModel.DrugAbuse;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.views.CustomTextView;

import java.util.List;

/**
 * Created by HP on 10-07-2018.
 */

public class FrequentDrugAbuseAdapters extends RecyclerView.Adapter<FrequentDrugAbuseAdapters.MyViewHolder> {
    private List<DrugAbuse> freqaabuseList;
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

    public FrequentDrugAbuseAdapters(Context context, List<DrugAbuse> freqabuselist) {
        this.mContext = context;
        this.freqaabuseList = freqabuselist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frequent_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final DrugAbuse list = freqaabuseList.get(position);

        holder.freq_name.setText(list.getAbuseName());

        holder.list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onItemClick(view, position,list.getAbuseFrequentId(),list.getAbuseId(),list.getAbuseName(),list.getDocId(),list.getDocType(),list.getFreqCount());
            }
        });

        holder.list_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder dialog1 = new AlertDialog.Builder(mContext, R.style.CustomDialog);
                dialog1.setTitle("Dug Abuse Information");
                dialog1.setMessage("Name: " + list.getAbuseName().toString() +"\n\nID: "+ list.getAbuseId());
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
        return freqaabuseList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, int abuseFrequentId, int abuseId, String abuseName, int docId, int docType, int freqCount);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
