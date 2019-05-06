package com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medisensehealth.fdccontributor.DataModel.ViewReports;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;
import com.medisensehealth.fdccontributor.views.CustomTextViewItalicBold;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 15-07-2018.
 */

public class ReportFolderAdapter  extends RecyclerView.Adapter<ReportFolderAdapter.MyViewHolder>  {
    private List<String> reportList;
    private Context mContext;
    List<ViewReports> attachmentList = new ArrayList<>();
    public RecyclerView mRecyclerView;
    private ReportImagesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ViewReports> report_imagesArray = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextViewBold username;
        public CustomTextViewItalicBold created_date;


        public MyViewHolder(View view) {
            super(view);
            username = (CustomTextViewBold) view.findViewById(R.id.report_folder_username);
            created_date = (CustomTextViewItalicBold) view.findViewById(R.id.report_folder_date);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.report_folder_recyclerview);
        }
    }

    public ReportFolderAdapter(List<String> reportList,List<ViewReports> attachmentList) {
        this.reportList = reportList;
        this.attachmentList = attachmentList;
        this.report_imagesArray = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_folder_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        this.report_imagesArray = new ArrayList<>();
        // holder.username.setText(reportList.get(position).toString());
        String user = "", created_date = "";
         for(int i=0;i<attachmentList.size();i++) {
             if (reportList.get(position).toString().contains(attachmentList.get(i).getReportFolder())) {
                 Log.d(Utils.TAG, " exists: "+attachmentList.get(i).getUserName());
                 user = attachmentList.get(i).getUserName();
                 created_date = attachmentList.get(i).getDateAdded();
                 report_imagesArray.add(new ViewReports(attachmentList.get(i).getReportID(),attachmentList.get(i).getAttachments(),attachmentList.get(i).getPatientID(),reportList.get(position).toString()));
             }
         }
         holder.username.setText(user);
         holder.created_date.setText(" ["+created_date+"]");

        mLayoutManager = new GridLayoutManager(mContext,3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Initialize a new instance of RecyclerView Adapter instance
        mAdapter = new ReportImagesAdapter(mContext,report_imagesArray);

        // Set the adapter for RecyclerView
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
