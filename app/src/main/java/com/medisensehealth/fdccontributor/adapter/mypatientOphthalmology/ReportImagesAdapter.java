package com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.medisensehealth.fdccontributor.DataModel.ViewReports;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.TouchImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Created by HP on 15-07-2018.
 */

public class ReportImagesAdapter extends RecyclerView.Adapter<ReportImagesAdapter.MyViewHolder>  {
    private List<ViewReports> imagesList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView report_images;

        public MyViewHolder(View view) {
            super(view);
            report_images = (ImageView)view.findViewById(R.id.report_images);
        }
    }

    public ReportImagesAdapter(Context context, List<ViewReports> imagesList) {
        this.mContext = context;
        this.imagesList = imagesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_images_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final String DOWNLOAD_REPORTS = "https://pixeleyecare.com/Contributors/patientAttachments/"+imagesList.get(position).getPatientID()+"/"+imagesList.get(position).getReportFolder()+"/"+imagesList.get(position).getAttachments().trim();
        Log.d(Utils.TAG, " DOWNLOAD_REPORTS: "+DOWNLOAD_REPORTS);
        URL url = null;
        try {
            url = new URL(DOWNLOAD_REPORTS);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Picasso.with(mContext).load(String.valueOf(url))
                .placeholder(R.drawable.user_profile)
                .error(R.drawable.user_profile)
                //.resize(800, 800)
                .fit()
                .into( holder.report_images, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                    }
                });

        holder.report_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(view.getContext(),"jdfsgt",Toast.LENGTH_SHORT).show();

                DisplayMetrics metrics = view.getContext().getResources().getDisplayMetrics();

                int DeviceTotalWidth = metrics.widthPixels;
                int DeviceTotalHeight = metrics.heightPixels;
                final Dialog dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.fragment_atachview);
                dialog.getWindow().setLayout(DeviceTotalWidth ,DeviceTotalHeight);
                TouchImageView imageZoom = dialog.findViewById(R.id.image_attachview);

                URL url = null;
                try {
                    url = new URL(DOWNLOAD_REPORTS);
                    URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                    url = uri.toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                Picasso.with(view.getContext()).load(String.valueOf(url))
                        .placeholder(R.drawable.blogs_empty_img)
                        .error(R.drawable.blogs_empty_img)
                        .fit()
                        .into(imageZoom, new Callback() {
                            @Override
                            public void onSuccess() {
                            }
                            @Override
                            public void onError() {
                            }
                        });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

}
