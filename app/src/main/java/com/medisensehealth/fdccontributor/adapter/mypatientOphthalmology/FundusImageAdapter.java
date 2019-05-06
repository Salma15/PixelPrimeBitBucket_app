package com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.medisensehealth.fdccontributor.DataModel.FundusImage;
import com.medisensehealth.fdccontributor.R;
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

/**
 * Created by lenovo on 03/02/2018.
 */

public class FundusImageAdapter  extends RecyclerView.Adapter<FundusImageAdapter.RecyclerViewHolder> {
    private List<FundusImage> arrayList;
    private Context context;
    boolean isImageFitToScreen;
    public ArrayList<String> GET_EPISODES_PHOTOS = new ArrayList<String>();
    String ENTRY_TYPE;

    public FundusImageAdapter(Context context,List<FundusImage> arrayList, ArrayList<String> photos, String types) {
        this.context = context;
        this.arrayList = arrayList;
        this.GET_EPISODES_PHOTOS = photos;
        this.ENTRY_TYPE = types;
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        final FundusImage model = arrayList.get(position);

        final RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder

       // Bitmap image = BitmapFactory.decodeResource(context.getResources(), model.getImage());// This will convert drawbale image into
        // bitmap

        // setting title
       // mainHolder.title.setText(model.getTitle());
      //  mainHolder.imageview.setImageBitmap(image);

         Log.d(Utils.TAG + " image: ", String.valueOf(model.getFundusImage()));

        if(ENTRY_TYPE.equalsIgnoreCase("VIEW")) {
            mainHolder.title.setVisibility(View.GONE);
            String urlStr = APIClass.DRS_MYPATIENT_ATTACHMENT_URL_PREM+model.getFundusImage().trim();
            Log.d(Utils.TAG + " urlStr: ", urlStr);
            URL url = null;
            try {
                url = new URL(urlStr);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                url = uri.toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            Picasso.with(context).load(String.valueOf(url))
                    .placeholder(R.drawable.blogs_empty_img)
                    .error(R.drawable.blogs_empty_img)
                    .fit()
                    .into(mainHolder.imageview, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });

        }
        else {
            mainHolder.title.setVisibility(View.VISIBLE);
            Bitmap bm = BitmapFactory.decodeFile(model.getFundusImage().trim());
            mainHolder.imageview.setImageBitmap(bm);

        }

        mainHolder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Log.d(Utils.TAG + " ZOOM: ", String.valueOf(isImageFitToScreen));
              /*  if(isImageFitToScreen) {
                    isImageFitToScreen=false;
                    mainHolder.imageview.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
                  //  mainHolder.imageview.setAdjustViewBounds(true);
                    mainHolder.imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }else{
                    isImageFitToScreen=true;
                    mainHolder.imageview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    mainHolder.imageview.setScaleType(ImageView.ScaleType.FIT_XY);
                }*/

                DisplayMetrics metrics = context.getResources().getDisplayMetrics();

                int DeviceTotalWidth = metrics.widthPixels;
                int DeviceTotalHeight = metrics.heightPixels;

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.fragment_atachview);
                dialog.getWindow().setLayout(DeviceTotalWidth ,DeviceTotalHeight);
              //  dialog.addContentView(new View(context), (new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)));
                TouchImageView imageZoom = dialog.findViewById(R.id.image_attachview);
                if(ENTRY_TYPE.equalsIgnoreCase("VIEW")) {
                    mainHolder.title.setVisibility(View.GONE);
                    String urlStr =  APIClass.DRS_MYPATIENT_ATTACHMENT_URL_PREM+model.getFundusImage().trim();
                    URL url = null;
                    try {
                        url = new URL(urlStr);
                        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                        url = uri.toURL();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    Picasso.with(context).load(String.valueOf(url))
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

                }
                else {
                    mainHolder.title.setVisibility(View.VISIBLE);
                    Bitmap bm = BitmapFactory.decodeFile(model.getFundusImage().trim());
                    imageZoom.setImageBitmap(bm);
                }
                dialog.show();
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
