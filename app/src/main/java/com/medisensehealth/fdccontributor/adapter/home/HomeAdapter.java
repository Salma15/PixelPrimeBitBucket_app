package com.medisensehealth.fdccontributor.adapter.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.medisensehealth.fdccontributor.DataModel.BlogsList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.feeds.BlogDetailActivity;
import com.medisensehealth.fdccontributor.activities.feeds.EventDetailActivity;
import com.medisensehealth.fdccontributor.activities.feeds.JobDetailActivity;
import com.medisensehealth.fdccontributor.activities.feeds.VideoDetailActivity;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;
import com.medisensehealth.fdccontributor.views.CustomTextViewItalicBold;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by medisense on 16/01/18.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private List<BlogsList> blogsList;
    private Context mContext;
    private String DOWNLOAD_BLOG_IMAGE_URL;
    URL url;
    private String YOUTUBE_API_KEY = "AIzaSyDS6HQV0IWcq1zNq7BQG1N0JlHbbeUVhLw";
    FragmentManager fragManager;
    FragmentTransaction fragTransaction;
//    BlogDetailFragment blogdetail;
//    BlogEventsFragment eventsdetail;
//    BlogJobsFragment jobdetail;
//    BlogSurgicalFragment surgicaldetail;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView posted_on, description;
        public ImageView feeds_images;
        public CustomTextViewItalicBold read_more;
        public CustomTextViewBold title;
        public LinearLayout  item_layout;
        public RelativeLayout relativeLayoutOverYouTubeThumbnailView, youtube_layout;
        YouTubeThumbnailView youTubeThumbnailView;
        public ImageView playButton;

        public MyViewHolder(View view) {
            super(view);
            title = (CustomTextViewBold) view.findViewById(R.id.feeds_title);
            posted_on = (CustomTextView) view.findViewById(R.id.feeds_date);
            description = (CustomTextView) view.findViewById(R.id.feeds_description);
            read_more = (CustomTextViewItalicBold) view.findViewById(R.id.feeds_readmore);
            feeds_images = (ImageView) view.findViewById(R.id.feeds_image);
            youtube_layout = (RelativeLayout) view.findViewById(R.id.youtube_layout);
            item_layout = (LinearLayout)  view.findViewById(R.id.home_list_layout);

            playButton=(ImageView)view.findViewById(R.id.btnYoutube_player);
            relativeLayoutOverYouTubeThumbnailView = (RelativeLayout) view.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            youTubeThumbnailView = (YouTubeThumbnailView) view.findViewById(R.id.youtube_thumbnail);

        }
    }


    public HomeAdapter(Context context, List<BlogsList> blgList) {
        this.mContext = context;
        this.blogsList = blgList;
        fragManager = ((AppCompatActivity) context).getSupportFragmentManager();
//        blogdetail =  new BlogDetailFragment();
//        eventsdetail  =  new BlogEventsFragment();
//        jobdetail =  new BlogJobsFragment();
//        surgicaldetail = new BlogSurgicalFragment();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final BlogsList blog = blogsList.get(position);
        holder.title.setText(stripHtml(blog.getBlogTitle()));
        holder.description.setText(stripHtml(blog.getBlogDescription()));

        if(blog.getBlogPostdate().equals("")) {

        }
        else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date newDate = null;
            try {
                newDate = format.parse(blog.getBlogPostdate());
                format = new SimpleDateFormat("dd MMM yyyy");
                String post_date = format.format(newDate);
                holder.posted_on.setText("Posted On: "+ post_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if(blog.getBlogPostType().equalsIgnoreCase("Surgical")) {
            holder.youTubeThumbnailView.setVisibility(View.VISIBLE);
            holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
            holder.description.setVisibility(View.GONE);
            holder.youtube_layout.setVisibility(View.VISIBLE);
            holder.feeds_images.setVisibility(View.GONE);

            String testString = blog.getBlogVideoURL();
            String[] parts = testString.split("/");
            final String BLOG_VIDEO_ID = parts[parts.length - 1];
            System.out.println(BLOG_VIDEO_ID);
            Log.d(Utils.TAG, "BLOG_VIDEO_ID: "+ String.valueOf(BLOG_VIDEO_ID));

            final YouTubeThumbnailLoader.OnThumbnailLoadedListener  onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener(){
                @Override
                public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                }

                @Override
                public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                    youTubeThumbnailView.setVisibility(View.VISIBLE);
                    holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
                }
            };

            holder.youTubeThumbnailView.initialize(YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                    youTubeThumbnailLoader.setVideo(BLOG_VIDEO_ID);
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    //write something for failure
                }
            });

            holder.playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(mContext).equals(YouTubeInitializationResult.SUCCESS)){
                        //This means that your device has the Youtube API Service (the app) and you are safe to launch it.
                        Log.d(Utils.TAG, " click: "+ String.valueOf(BLOG_VIDEO_ID));
                        Intent intent = YouTubeStandalonePlayer.createVideoIntent((AppCompatActivity)mContext, YOUTUBE_API_KEY, BLOG_VIDEO_ID);
                        mContext.startActivity(intent);
                       //   ((AppCompatActivity)mContext).finish();
                    }else{
                        // Log the outcome, take necessary measure, like playing the video in webview :)
                        Toast.makeText(mContext, "Youtube Application is not found !!!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        else {
            holder.youTubeThumbnailView.setVisibility(View.GONE);
            holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.GONE);
            holder.description.setVisibility(View.VISIBLE);
            holder.youtube_layout.setVisibility(View.GONE);
            holder.feeds_images.setVisibility(View.VISIBLE);
        }

        if(blog.getBlogPostType().equalsIgnoreCase("Blog")) {
            DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_BLOGS_IMAGE_URL+String.valueOf(blog.getBlogId())+"/"+blog.getBlogImage();
        }
        else  if(blog.getBlogPostType().equalsIgnoreCase("Offers")) {
            DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_OFFERS_EVENTS_URL+String.valueOf(blog.getBlogId())+"/"+blog.getBlogImage();
        }
        else  if(blog.getBlogPostType().equalsIgnoreCase("Jobs")) {
            DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_OFFERS_EVENTS_URL+String.valueOf(blog.getBlogId())+"/"+blog.getBlogImage();
        }
        else  if(blog.getBlogPostType().equalsIgnoreCase("Events")) {
            DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_OFFERS_EVENTS_URL+String.valueOf(blog.getBlogId())+"/"+blog.getBlogImage();
        }
        else  if(blog.getBlogPostType().equalsIgnoreCase("Surgical")) {
            DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_OFFERS_EVENTS_URL+String.valueOf(blog.getBlogId())+"/"+blog.getBlogImage();
        }

        if(blog.getBlogImage().equals("")) {
            holder.feeds_images.setVisibility(View.GONE);

            if(blog.getBlogPostType().equals("Jobs")) {
                DOWNLOAD_BLOG_IMAGE_URL = "http://pixeleyecare.com/assets/img/jobs_fellowship_default.jpg";
                loadImage(holder, DOWNLOAD_BLOG_IMAGE_URL);
            }
        }
        else {
            holder.feeds_images.setVisibility(View.VISIBLE);
            loadImage(holder, DOWNLOAD_BLOG_IMAGE_URL);
        }

        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(blog.getBlogPostType().equalsIgnoreCase("Blog")) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("BLOG_ID", blog.getBlogId());
                    bundle.putString("BLOG_IMAGE", blog.getBlogImage());
                    bundle.putString("BLOG_TITLE", blog.getBlogTitle().trim());
                    bundle.putString("BLOG_DATE", blog.getBlogPostdate());
                    bundle.putString("BLOG_DESCRIPTION", blog.getBlogDescription());
                    bundle.putString("BLOG_TYPE", blog.getBlogPostType());
                    bundle.putString("BLOG_USERNAME", blog.getBlogUserName());
                    bundle.putString("BLOG_USERIMAGE", blog.getBlogUserImage());
                    bundle.putString("BLOG_USERPROFESSION", blog.getBlogUserProf());
                    bundle.putString("BLOG_CONTACTINFO", blog.getBlogContactInfo());
                    bundle.putString("BLOG_ATTACHMENT", blog.getBlogAttachments());
                    bundle.putInt("BLOG_COMPANY_ID", blog.getBlogCompanyId());
                    bundle.putString("BLOG_TRANSACTIONID", blog.getBlogTransactionID());
                    bundle.putString("BLOG_VIEWS", blog.getBlogNumberViews());

                    Intent blog_intent = new Intent(mContext, BlogDetailActivity.class);
                    blog_intent.putExtra("title","Blog Details");
                    blog_intent.putExtra("BLOG_ID", blog.getBlogId());
                    blog_intent.putExtra("BLOG_IMAGE", blog.getBlogImage());
                    blog_intent.putExtra("BLOG_TITLE", blog.getBlogTitle().trim());
                    blog_intent.putExtra("BLOG_DATE", blog.getBlogPostdate());
                    blog_intent.putExtra("BLOG_DESCRIPTION", blog.getBlogDescription());
                    blog_intent.putExtra("BLOG_TYPE", blog.getBlogPostType());
                    blog_intent.putExtra("BLOG_USERNAME", blog.getBlogUserName());
                    blog_intent.putExtra("BLOG_USERIMAGE", blog.getBlogUserImage());
                    blog_intent.putExtra("BLOG_USERPROFESSION", blog.getBlogUserProf());
                    blog_intent.putExtra("BLOG_CONTACTINFO", blog.getBlogContactInfo());
                    blog_intent.putExtra("BLOG_ATTACHMENT", blog.getBlogAttachments());
                    blog_intent.putExtra("BLOG_COMPANY_ID", blog.getBlogCompanyId());
                    blog_intent.putExtra("BLOG_TRANSACTIONID", blog.getBlogTransactionID());
                    blog_intent.putExtra("BLOG_VIEWS", blog.getBlogNumberViews());
                    mContext.startActivity(blog_intent);

                  /*  fragTransaction = fragManager.beginTransaction();
                    blogdetail.setArguments(bundle);
                    fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragTransaction.replace(R.id.dashboard_fragment,blogdetail,"BLOG_DETAILS");
                    fragTransaction.addToBackStack("BLOG_DETAILS");
                    fragTransaction.commit();*/
                }
                else  if(blog.getBlogPostType().equalsIgnoreCase("Events")) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("BLOG_ID", blog.getBlogId());
                    bundle.putString("BLOG_IMAGE", blog.getBlogImage());
                    bundle.putString("BLOG_TITLE", blog.getBlogTitle().trim());
                    bundle.putString("BLOG_DATE", blog.getBlogFromToDate());
                    bundle.putString("BLOG_DESCRIPTION", blog.getBlogDescription());
                    bundle.putString("BLOG_TYPE", blog.getBlogPostType());
                    bundle.putString("BLOG_USERNAME", blog.getBlogUserName());
                    bundle.putString("BLOG_USERIMAGE", blog.getBlogUserImage());
                    bundle.putString("BLOG_USERPROFESSION", blog.getBlogUserProf());
                    bundle.putString("BLOG_CONTACTINFO", blog.getBlogContactInfo());
                    bundle.putString("BLOG_ATTACHMENT", blog.getBlogAttachments());
                    bundle.putInt("BLOG_COMPANY_ID", blog.getBlogCompanyId());
                    bundle.putString("BLOG_TRANSACTIONID", blog.getBlogTransactionID());
                    bundle.putString("BLOG_VIEWS", blog.getBlogNumberViews());

                    Intent event_intent = new Intent(mContext, EventDetailActivity.class);
                    event_intent.putExtra("title","Event Details");
                    event_intent.putExtra("BLOG_ID", blog.getBlogId());
                    event_intent.putExtra("BLOG_IMAGE", blog.getBlogImage());
                    event_intent.putExtra("BLOG_TITLE", blog.getBlogTitle().trim());
                    event_intent.putExtra("BLOG_DATE", blog.getBlogFromToDate());
                    event_intent.putExtra("BLOG_DESCRIPTION", blog.getBlogDescription());
                    event_intent.putExtra("BLOG_TYPE", blog.getBlogPostType());
                    event_intent.putExtra("BLOG_USERNAME", blog.getBlogUserName());
                    event_intent.putExtra("BLOG_USERIMAGE", blog.getBlogUserImage());
                    event_intent.putExtra("BLOG_USERPROFESSION", blog.getBlogUserProf());
                    event_intent.putExtra("BLOG_CONTACTINFO", blog.getBlogContactInfo());
                    event_intent.putExtra("BLOG_ATTACHMENT", blog.getBlogAttachments());
                    event_intent.putExtra("BLOG_COMPANY_ID", blog.getBlogCompanyId());
                    event_intent.putExtra("BLOG_TRANSACTIONID", blog.getBlogTransactionID());
                    event_intent.putExtra("BLOG_VIEWS", blog.getBlogNumberViews());
                    mContext.startActivity(event_intent);

                   /* fragTransaction = fragManager.beginTransaction();
                    eventsdetail.setArguments(bundle);
                    fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragTransaction.replace(R.id.dashboard_fragment,eventsdetail,"EVENTS_DETAILS");
                    fragTransaction.addToBackStack("EVENTS_DETAILS");
                    fragTransaction.commit();*/
                }
                else if(blog.getBlogPostType().equalsIgnoreCase("Jobs")) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("BLOG_ID", blog.getBlogId());
                    bundle.putString("BLOG_IMAGE", blog.getBlogImage());
                    bundle.putString("BLOG_TITLE", blog.getBlogTitle().trim());
                    bundle.putString("BLOG_DATE", blog.getBlogFromToDate());
                    bundle.putString("BLOG_DESCRIPTION", blog.getBlogDescription());
                    bundle.putString("BLOG_TYPE", blog.getBlogPostType());
                    bundle.putString("BLOG_USERNAME", blog.getBlogUserName());
                    bundle.putString("BLOG_USERIMAGE", blog.getBlogUserImage());
                    bundle.putString("BLOG_USERPROFESSION", blog.getBlogUserProf());
                    bundle.putString("BLOG_CONTACTINFO", blog.getBlogContactInfo());
                    bundle.putString("BLOG_ATTACHMENT", blog.getBlogAttachments());
                    bundle.putInt("BLOG_COMPANY_ID", blog.getBlogCompanyId());
                    bundle.putString("BLOG_TRANSACTIONID", blog.getBlogTransactionID());
                    bundle.putString("BLOG_VIEWS", blog.getBlogNumberViews());

                    Intent job_intent = new Intent(mContext, JobDetailActivity.class);
                    job_intent.putExtra("title","Fellowship Details");
                    job_intent.putExtra("BLOG_ID", blog.getBlogId());
                    job_intent.putExtra("BLOG_IMAGE", blog.getBlogImage());
                    job_intent.putExtra("BLOG_TITLE", blog.getBlogTitle().trim());
                    job_intent.putExtra("BLOG_DATE", blog.getBlogFromToDate());
                    job_intent.putExtra("BLOG_DESCRIPTION", blog.getBlogDescription());
                    job_intent.putExtra("BLOG_TYPE", blog.getBlogPostType());
                    job_intent.putExtra("BLOG_USERNAME", blog.getBlogUserName());
                    job_intent.putExtra("BLOG_USERIMAGE", blog.getBlogUserImage());
                    job_intent.putExtra("BLOG_USERPROFESSION", blog.getBlogUserProf());
                    job_intent.putExtra("BLOG_CONTACTINFO", blog.getBlogContactInfo());
                    job_intent.putExtra("BLOG_ATTACHMENT", blog.getBlogAttachments());
                    job_intent.putExtra("BLOG_COMPANY_ID", blog.getBlogCompanyId());
                    job_intent.putExtra("BLOG_TRANSACTIONID", blog.getBlogTransactionID());
                    job_intent.putExtra("BLOG_VIEWS", blog.getBlogNumberViews());
                    mContext.startActivity(job_intent);

                   /* fragTransaction = fragManager.beginTransaction();
                    jobdetail.setArguments(bundle);
                    fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragTransaction.replace(R.id.dashboard_fragment,jobdetail,"JOB_DETAILS");
                    fragTransaction.addToBackStack("JOB_DETAILS");
                    fragTransaction.commit();*/
                }
                else  if(blog.getBlogPostType().equalsIgnoreCase("Surgical")) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("BLOG_ID", blog.getBlogId());
                    bundle.putString("BLOG_IMAGE", blog.getBlogImage());
                    bundle.putString("BLOG_TITLE", blog.getBlogTitle().trim());
                    bundle.putString("BLOG_DATE", blog.getBlogPostdate());
                    bundle.putString("BLOG_DESCRIPTION", blog.getBlogDescription());
                    bundle.putString("BLOG_TYPE", blog.getBlogPostType());
                    bundle.putString("BLOG_USERNAME", blog.getBlogUserName());
                    bundle.putString("BLOG_USERIMAGE", blog.getBlogUserImage());
                    bundle.putString("BLOG_USERPROFESSION", blog.getBlogUserProf());
                    bundle.putString("BLOG_CONTACTINFO", blog.getBlogContactInfo());
                    bundle.putString("BLOG_ATTACHMENT", blog.getBlogAttachments());
                    bundle.putInt("BLOG_COMPANY_ID", blog.getBlogCompanyId());
                    bundle.putString("BLOG_TRANSACTIONID", blog.getBlogTransactionID());
                    bundle.putString("BLOG_VIDEO_URL", blog.getBlogVideoURL());
                    bundle.putString("BLOG_VIEWS", blog.getBlogNumberViews());

                    Intent video_intent = new Intent(mContext, VideoDetailActivity.class);
                    video_intent.putExtra("title","Video Details");
                    video_intent.putExtra("BLOG_ID", blog.getBlogId());
                    video_intent.putExtra("BLOG_IMAGE", blog.getBlogImage());
                    video_intent.putExtra("BLOG_TITLE", blog.getBlogTitle().trim());
                    video_intent.putExtra("BLOG_DATE", blog.getBlogPostdate());
                    video_intent.putExtra("BLOG_DESCRIPTION", blog.getBlogDescription());
                    video_intent.putExtra("BLOG_TYPE", blog.getBlogPostType());
                    video_intent.putExtra("BLOG_USERNAME", blog.getBlogUserName());
                    video_intent.putExtra("BLOG_USERIMAGE", blog.getBlogUserImage());
                    video_intent.putExtra("BLOG_USERPROFESSION", blog.getBlogUserProf());
                    video_intent.putExtra("BLOG_CONTACTINFO", blog.getBlogContactInfo());
                    video_intent.putExtra("BLOG_ATTACHMENT", blog.getBlogAttachments());
                    video_intent.putExtra("BLOG_COMPANY_ID", blog.getBlogCompanyId());
                    video_intent.putExtra("BLOG_TRANSACTIONID", blog.getBlogTransactionID());
                    video_intent.putExtra("BLOG_VIDEO_URL", blog.getBlogVideoURL());
                    video_intent.putExtra("BLOG_VIEWS", blog.getBlogNumberViews());
                    mContext.startActivity(video_intent);

                  /*  fragTransaction = fragManager.beginTransaction();
                    surgicaldetail.setArguments(bundle);
                    fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragTransaction.replace(R.id.dashboard_fragment,surgicaldetail,"SURGICAL_DETAILS");
                    fragTransaction.addToBackStack("SURGICAL_DETAILS");
                    fragTransaction.commit();*/
                }
            }
        });

    }

    private void loadImage(final MyViewHolder holder, String DOWNLOAD_BLOG_IMAGE_URL) {
        try {
            url = new URL(DOWNLOAD_BLOG_IMAGE_URL);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Log.d(Utils.TAG, " pic: "+DOWNLOAD_BLOG_IMAGE_URL);
        Picasso.with(mContext).load(String.valueOf(url))
                .placeholder(R.drawable.blogs_empty_img)
                .error(R.drawable.blogs_empty_img)
                .resize(240, 200)
                .centerCrop()
                .into(holder.feeds_images, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.feeds_images.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onError() {
                        holder.feeds_images.setVisibility(View.VISIBLE);
                    }
                });
        Picasso.with(mContext).invalidate(String.valueOf(url));
    }

    @Override
    public int getItemCount() {
        return blogsList.size();
    }

    private String stripHtml(String response) {
        return Html.fromHtml(response).toString();
    }


}
