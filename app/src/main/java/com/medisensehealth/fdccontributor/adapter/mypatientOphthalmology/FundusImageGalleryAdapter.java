package com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.medisensehealth.fdccontributor.DataModel.FundusImage;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.mypatientOphthalmology.EditFundusImageActivity;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.TouchImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.Y;

/**
 * Created by SALMA on 22-12-2018.
 */
public class FundusImageGalleryAdapter extends RecyclerView.Adapter<FundusImageGalleryAdapter.RecyclerViewHolder>  {
    private List<FundusImage> arrayList;
    private Context context;
    boolean isImageFitToScreen;
    public ArrayList<String> GET_EPISODES_PHOTOS = new ArrayList<String>();
    String ENTRY_TYPE;

    public FundusImageGalleryAdapter(Context context,List<FundusImage> arrayList, ArrayList<String> photos) {
        this.context = context;
        this.arrayList = arrayList;
        this.GET_EPISODES_PHOTOS = photos;
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        final FundusImage model = arrayList.get(position);

        final RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder

        Log.d(Utils.TAG + " image: ", String.valueOf(model.getFundusImage()));

        mainHolder.title.setVisibility(View.VISIBLE);
        Bitmap bm = BitmapFactory.decodeFile(model.getFundusImage().trim());
        mainHolder.imageview.setImageBitmap(bm);
        mainHolder.imageview.setRotation(90);

        mainHolder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DisplayMetrics metrics = context.getResources().getDisplayMetrics();

                int DeviceTotalWidth = metrics.widthPixels;
                int DeviceTotalHeight = metrics.heightPixels;

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.fragment_atachview_fundus);
                dialog.getWindow().setLayout(DeviceTotalWidth ,DeviceTotalHeight);

                final TouchImageView imageZoom = dialog.findViewById(R.id.image_attachview_fundus);
                mainHolder.title.setVisibility(View.VISIBLE);
                final Bitmap bm = BitmapFactory.decodeFile(model.getFundusImage().trim());
                imageZoom.setImageBitmap(bm);
                imageZoom.setRotation(90);
                dialog.show();

                Button edit_image = dialog.findViewById(R.id.image_edit_fundus);
                edit_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(Utils.TAG + " selImage: ", String.valueOf(model.getFundusImage()));

                        Intent intent=new Intent(context, EditFundusImageActivity.class);
                        intent.putExtra("title", "Edit");
                        intent.putExtra("IMAGE_URL", model.getFundusImage());
                        context.startActivity(intent);

                        dialog.cancel();
                    }
                });
            }
        });



        mainHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemDismiss(position);
                GET_EPISODES_PHOTOS.remove(position);
            }
        });

    }

    public void onItemDismiss(int position) {
        if(position!=-1 && position<arrayList.size())
        {
            arrayList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.fundus_image_item_row, viewGroup, false);
        RecyclerViewHolder listHolder = new RecyclerViewHolder(mainGroup);
        return listHolder;

    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder  {
        // View holder for gridview recycler view as we used in listview
        public TextView title;
        public ImageView imageview;

        public RecyclerViewHolder(View view) {
            super(view);
            // Find all views ids

            this.title = (TextView) view.findViewById(R.id.title);
            this.imageview = (ImageView) view.findViewById(R.id.image);
        }
    }

}
