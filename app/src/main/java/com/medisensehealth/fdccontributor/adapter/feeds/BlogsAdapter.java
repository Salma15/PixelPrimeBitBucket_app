package com.medisensehealth.fdccontributor.adapter.feeds;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.medisensehealth.fdccontributor.views.CustomTextViewItalic;
import com.medisensehealth.fdccontributor.views.CustomTextViewItalicBold;
import com.medisensehealth.fdccontributor.views.CustomTextViewSemiBold;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Created by lenovo on 17-03-2017.
 */

public class BlogsAdapter extends RecyclerView.Adapter<BlogsAdapter.MyViewHolder>  {

    private List<BlogsList> blogsList;
    Context mContext;
    FragmentManager fragManager;
    FragmentTransaction fragTransaction;
    String DOWNLOAD_BLOG_IMAGE_URL, DOWNLOAD_AUTHOR_PROFILE;
   /* BlogDetailFragment blogdetail;
    BlogEventsFragment eventsdetail;
    BlogJobsFragment jobdetail;
    BlogSurgicalFragment surgicaldetail;*/
    private static final int RECOVERY_DIALOG_REQUEST = 10;
    private static final String YOUTUBE_API_KEY = "AIzaSyCt2SVtu_410CXOoM5K4n_mwIp5Zpz6kso";
    int item_position = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView cardViewBlogs;
        RelativeLayout blogslist;
        public ImageView author_image, blogs_image;
        public CustomTextViewItalic post_date;
        CustomTextViewItalicBold author_name;
        public CustomTextViewBold title;
        public CustomTextViewItalic shortDescription;
        public CustomTextViewSemiBold blog_types;
        public CustomTextViewSemiBold num_views;
        public CustomTextViewSemiBold posted_by;

        public RelativeLayout relativeLayoutOverYouTubeThumbnailView;
        YouTubeThumbnailView youTubeThumbnailView;
        public ImageView playButton;

        public MyViewHolder(View view) {
            super(view);
              title = (CustomTextViewBold) view.findViewById(R.id.blogs_title);
              blogs_image = (ImageView) view.findViewById(R.id.blogs_image);
              shortDescription = (CustomTextViewItalic) view.findViewById(R.id.blog_short_desc);
            cardViewBlogs = (CardView) view.findViewById(R.id.blogs_list_cardview);
            blog_types = (CustomTextViewSemiBold)  view.findViewById(R.id.blogs_type);
            blogslist = (RelativeLayout)view.findViewById(R.id.relativeLayout_blogslist);
            num_views = (CustomTextViewSemiBold)  view.findViewById(R.id.blog_num_views);
            posted_by = (CustomTextViewSemiBold)  view.findViewById(R.id.blog_postedby);

            playButton=(ImageView)view.findViewById(R.id.btnYoutube_player);
            relativeLayoutOverYouTubeThumbnailView = (RelativeLayout) view.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            youTubeThumbnailView = (YouTubeThumbnailView) view.findViewById(R.id.youtube_thumbnail);
        }
    }

    public BlogsAdapter(Context context, List<BlogsList> blogList) {
        this.blogsList = blogList;
        this.mContext = context;
       /* fragManager = ((AppCompatActivity) context).getSupportFragmentManager();
        jobdetail =  new BlogJobsFragment();
        blogdetail =  new BlogDetailFragment();
        eventsdetail  =  new BlogEventsFragment();
        surgicaldetail = new BlogSurgicalFragment();*/
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blogs_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BlogsList blogs = blogsList.get(position);

        if(position % 2==0)
            holder.cardViewBlogs.setCardBackgroundColor(mContext.getResources().getColor(R.color.row_even_color));
        else
            holder.cardViewBlogs.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));


        if(blogs.getBlogPostType().equalsIgnoreCase("Surgical")) {
            holder.youTubeThumbnailView.setVisibility(View.VISIBLE);
            holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
            holder.shortDescription.setVisibility(View.GONE);

            String testString = blogs.getBlogVideoURL();
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
                      //  ((AppCompatActivity)mContext).finish();
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
            holder.shortDescription.setVisibility(View.VISIBLE);
        }

        if(blogs.getBlogPostType().equalsIgnoreCase("Blog")) {
            DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_BLOGS_IMAGE_URL+String.valueOf(blogs.getBlogId())+"/"+blogs.getBlogImage();
        }
        else  if(blogs.getBlogPostType().equalsIgnoreCase("Offers")) {
            DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_OFFERS_EVENTS_URL+String.valueOf(blogs.getBlogId())+"/"+blogs.getBlogImage();
        }
        else  if(blogs.getBlogPostType().equalsIgnoreCase("Jobs")) {
            DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_OFFERS_EVENTS_URL+String.valueOf(blogs.getBlogId())+"/"+blogs.getBlogImage();
        }
        else  if(blogs.getBlogPostType().equalsIgnoreCase("Events")) {
            DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_OFFERS_EVENTS_URL+String.valueOf(blogs.getBlogId())+"/"+blogs.getBlogImage();
        }

        if(blogs.getBlogImage().equals("")) {
            holder.blogs_image.setVisibility(View.GONE);
        }
        else {
            holder.blogs_image.setVisibility(View.VISIBLE);

            String urlStr = DOWNLOAD_BLOG_IMAGE_URL;
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

            Log.d(Utils.TAG, "path: "+ String.valueOf(url));
            Picasso.with(mContext).load(String.valueOf(url))
                    .placeholder(R.drawable.blogs_empty_img)
                    .error(R.drawable.blogs_empty_img)
                    .resize(400, 400)
                    .centerInside()
                    .into(holder.blogs_image, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });
            Picasso.with(mContext).invalidate(String.valueOf(url));

        }

        if(blogs.getBlogPostType().trim().equalsIgnoreCase("Surgical")) {
            holder.blog_types.setText("Surgical Video");
        }
        else  if(blogs.getBlogPostType().equalsIgnoreCase("Jobs")) {
            holder.blog_types.setText("Fellowships");
        }
        else {
            holder.blog_types.setText(blogs.getBlogPostType().trim());
        }

        holder.blog_types.setVisibility(View.GONE);

        holder.title.setText( stripHtml(blogs.getBlogTitle().trim()));
        holder.shortDescription.setText( stripHtml(blogs.getBlogDescription().trim()));
        holder.num_views.setText("Views: "+ blogs.getBlogNumberViews());
        holder.posted_by.setText("Posted By: "+blogs.getBlogUserName());

        Log.d(Utils.TAG, "ATTACH: " + blogs.getBlogAttachments());
        Log.d(Utils.TAG, "COMP: " + blogs.getBlogCompanyId());

        holder.blogslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(blogs.getBlogPostType().equalsIgnoreCase("Blog")) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("BLOG_ID", blogs.getBlogId());
                    bundle.putString("BLOG_IMAGE", blogs.getBlogImage());
                    bundle.putString("BLOG_TITLE", blogs.getBlogTitle().trim());
                    bundle.putString("BLOG_DATE", blogs.getBlogPostdate());
                    bundle.putString("BLOG_DESCRIPTION", blogs.getBlogDescription());
                    bundle.putString("BLOG_TYPE", blogs.getBlogPostType());
                    bundle.putString("BLOG_USERNAME", blogs.getBlogUserName());
                    bundle.putString("BLOG_USERIMAGE", blogs.getBlogUserImage());
                    bundle.putString("BLOG_USERPROFESSION", blogs.getBlogUserProf());
                    bundle.putString("BLOG_CONTACTINFO", blogs.getBlogContactInfo());
                    bundle.putString("BLOG_ATTACHMENT", blogs.getBlogAttachments());
                    bundle.putInt("BLOG_COMPANY_ID", blogs.getBlogCompanyId());
                    bundle.putString("BLOG_TRANSACTIONID", blogs.getBlogTransactionID());
                    bundle.putString("BLOG_VIEWS", blogs.getBlogNumberViews());

                    Intent blog_intent = new Intent(mContext, BlogDetailActivity.class);
                    blog_intent.putExtra("title","Blog Details");
                    blog_intent.putExtra("BLOG_ID", blogs.getBlogId());
                    blog_intent.putExtra("BLOG_IMAGE", blogs.getBlogImage());
                    blog_intent.putExtra("BLOG_TITLE", blogs.getBlogTitle().trim());
                    blog_intent.putExtra("BLOG_DATE", blogs.getBlogPostdate());
                    blog_intent.putExtra("BLOG_DESCRIPTION", blogs.getBlogDescription());
                    blog_intent.putExtra("BLOG_TYPE", blogs.getBlogPostType());
                    blog_intent.putExtra("BLOG_USERNAME", blogs.getBlogUserName());
                    blog_intent.putExtra("BLOG_USERIMAGE", blogs.getBlogUserImage());
                    blog_intent.putExtra("BLOG_USERPROFESSION", blogs.getBlogUserProf());
                    blog_intent.putExtra("BLOG_CONTACTINFO", blogs.getBlogContactInfo());
                    blog_intent.putExtra("BLOG_ATTACHMENT", blogs.getBlogAttachments());
                    blog_intent.putExtra("BLOG_COMPANY_ID", blogs.getBlogCompanyId());
                    blog_intent.putExtra("BLOG_TRANSACTIONID", blogs.getBlogTransactionID());
                    blog_intent.putExtra("BLOG_VIEWS", blogs.getBlogNumberViews());
                    mContext.startActivity(blog_intent);

                 /*   fragTransaction = fragManager.beginTransaction();
                    blogdetail.setArguments(bundle);
                    fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragTransaction.replace(R.id.dashboard_fragment,blogdetail,"BLOG_DETAILS");
                    fragTransaction.addToBackStack("BLOG_DETAILS");
                    fragTransaction.commit();*/
                }
                else  if(blogs.getBlogPostType().equalsIgnoreCase("Events")) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("BLOG_ID", blogs.getBlogId());
                    bundle.putString("BLOG_IMAGE", blogs.getBlogImage());
                    bundle.putString("BLOG_TITLE", blogs.getBlogTitle().trim());
                    bundle.putString("BLOG_DATE", blogs.getBlogFromToDate());
                    bundle.putString("BLOG_DESCRIPTION", blogs.getBlogDescription());
                    bundle.putString("BLOG_TYPE", blogs.getBlogPostType());
                    bundle.putString("BLOG_USERNAME", blogs.getBlogUserName());
                    bundle.putString("BLOG_USERIMAGE", blogs.getBlogUserImage());
                    bundle.putString("BLOG_USERPROFESSION", blogs.getBlogUserProf());
                    bundle.putString("BLOG_CONTACTINFO", blogs.getBlogContactInfo());
                    bundle.putString("BLOG_ATTACHMENT", blogs.getBlogAttachments());
                    bundle.putInt("BLOG_COMPANY_ID", blogs.getBlogCompanyId());
                    bundle.putString("BLOG_TRANSACTIONID", blogs.getBlogTransactionID());
                    bundle.putString("BLOG_VIEWS", blogs.getBlogNumberViews());

                    Intent event_intent = new Intent(mContext, EventDetailActivity.class);
                    event_intent.putExtra("title","Event Details");
                    event_intent.putExtra("BLOG_ID", blogs.getBlogId());
                    event_intent.putExtra("BLOG_IMAGE", blogs.getBlogImage());
                    event_intent.putExtra("BLOG_TITLE", blogs.getBlogTitle().trim());
                    event_intent.putExtra("BLOG_DATE", blogs.getBlogFromToDate());
                    event_intent.putExtra("BLOG_DESCRIPTION", blogs.getBlogDescription());
                    event_intent.putExtra("BLOG_TYPE", blogs.getBlogPostType());
                    event_intent.putExtra("BLOG_USERNAME", blogs.getBlogUserName());
                    event_intent.putExtra("BLOG_USERIMAGE", blogs.getBlogUserImage());
                    event_intent.putExtra("BLOG_USERPROFESSION", blogs.getBlogUserProf());
                    event_intent.putExtra("BLOG_CONTACTINFO", blogs.getBlogContactInfo());
                    event_intent.putExtra("BLOG_ATTACHMENT", blogs.getBlogAttachments());
                    event_intent.putExtra("BLOG_COMPANY_ID", blogs.getBlogCompanyId());
                    event_intent.putExtra("BLOG_TRANSACTIONID", blogs.getBlogTransactionID());
                    event_intent.putExtra("BLOG_VIEWS", blogs.getBlogNumberViews());
                    mContext.startActivity(event_intent);

                  /*  fragTransaction = fragManager.beginTransaction();
                    eventsdetail.setArguments(bundle);
                    fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragTransaction.replace(R.id.dashboard_fragment,eventsdetail,"EVENTS_DETAILS");
                    fragTransaction.addToBackStack("EVENTS_DETAILS");
                    fragTransaction.commit();*/
                }
                else if(blogs.getBlogPostType().equalsIgnoreCase("Jobs")) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("BLOG_ID", blogs.getBlogId());
                    bundle.putString("BLOG_IMAGE", blogs.getBlogImage());
                    bundle.putString("BLOG_TITLE", blogs.getBlogTitle().trim());
                    bundle.putString("BLOG_DATE", blogs.getBlogFromToDate());
                    bundle.putString("BLOG_DESCRIPTION", blogs.getBlogDescription());
                    bundle.putString("BLOG_TYPE", blogs.getBlogPostType());
                    bundle.putString("BLOG_USERNAME", blogs.getBlogUserName());
                    bundle.putString("BLOG_USERIMAGE", blogs.getBlogUserImage());
                    bundle.putString("BLOG_USERPROFESSION", blogs.getBlogUserProf());
                    bundle.putString("BLOG_CONTACTINFO", blogs.getBlogContactInfo());
                    bundle.putString("BLOG_ATTACHMENT", blogs.getBlogAttachments());
                    bundle.putInt("BLOG_COMPANY_ID", blogs.getBlogCompanyId());
                    bundle.putString("BLOG_TRANSACTIONID", blogs.getBlogTransactionID());
                    bundle.putString("BLOG_VIEWS", blogs.getBlogNumberViews());

                    Intent job_intent = new Intent(mContext, JobDetailActivity.class);
                    job_intent.putExtra("title","Fellowship Details");
                    job_intent.putExtra("BLOG_ID", blogs.getBlogId());
                    job_intent.putExtra("BLOG_IMAGE", blogs.getBlogImage());
                    job_intent.putExtra("BLOG_TITLE", blogs.getBlogTitle().trim());
                    job_intent.putExtra("BLOG_DATE", blogs.getBlogFromToDate());
                    job_intent.putExtra("BLOG_DESCRIPTION", blogs.getBlogDescription());
                    job_intent.putExtra("BLOG_TYPE", blogs.getBlogPostType());
                    job_intent.putExtra("BLOG_USERNAME", blogs.getBlogUserName());
                    job_intent.putExtra("BLOG_USERIMAGE", blogs.getBlogUserImage());
                    job_intent.putExtra("BLOG_USERPROFESSION", blogs.getBlogUserProf());
                    job_intent.putExtra("BLOG_CONTACTINFO", blogs.getBlogContactInfo());
                    job_intent.putExtra("BLOG_ATTACHMENT", blogs.getBlogAttachments());
                    job_intent.putExtra("BLOG_COMPANY_ID", blogs.getBlogCompanyId());
                    job_intent.putExtra("BLOG_TRANSACTIONID", blogs.getBlogTransactionID());
                    job_intent.putExtra("BLOG_VIEWS", blogs.getBlogNumberViews());
                    mContext.startActivity(job_intent);


                   /* fragTransaction = fragManager.beginTransaction();
                    jobdetail.setArguments(bundle);
                    fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragTransaction.replace(R.id.dashboard_fragment,jobdetail,"JOB_DETAILS");
                    fragTransaction.addToBackStack("JOB_DETAILS");
                    fragTransaction.commit();*/
                }
                else  if(blogs.getBlogPostType().equalsIgnoreCase("Surgical")) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("BLOG_ID", blogs.getBlogId());
                    bundle.putString("BLOG_IMAGE", blogs.getBlogImage());
                    bundle.putString("BLOG_TITLE", blogs.getBlogTitle().trim());
                    bundle.putString("BLOG_DATE", blogs.getBlogPostdate());
                    bundle.putString("BLOG_DESCRIPTION", blogs.getBlogDescription());
                    bundle.putString("BLOG_TYPE", blogs.getBlogPostType());
                    bundle.putString("BLOG_USERNAME", blogs.getBlogUserName());
                    bundle.putString("BLOG_USERIMAGE", blogs.getBlogUserImage());
                    bundle.putString("BLOG_USERPROFESSION", blogs.getBlogUserProf());
                    bundle.putString("BLOG_CONTACTINFO", blogs.getBlogContactInfo());
                    bundle.putString("BLOG_ATTACHMENT", blogs.getBlogAttachments());
                    bundle.putInt("BLOG_COMPANY_ID", blogs.getBlogCompanyId());
                    bundle.putString("BLOG_TRANSACTIONID", blogs.getBlogTransactionID());
                    bundle.putString("BLOG_VIDEO_URL", blogs.getBlogVideoURL());
                    bundle.putString("BLOG_VIEWS", blogs.getBlogNumberViews());

                    Intent video_intent = new Intent(mContext, VideoDetailActivity.class);
                    video_intent.putExtra("title","Video Details");
                    video_intent.putExtra("BLOG_ID", blogs.getBlogId());
                    video_intent.putExtra("BLOG_IMAGE", blogs.getBlogImage());
                    video_intent.putExtra("BLOG_TITLE", blogs.getBlogTitle().trim());
                    video_intent.putExtra("BLOG_DATE", blogs.getBlogPostdate());
                    video_intent.putExtra("BLOG_DESCRIPTION", blogs.getBlogDescription());
                    video_intent.putExtra("BLOG_TYPE", blogs.getBlogPostType());
                    video_intent.putExtra("BLOG_USERNAME", blogs.getBlogUserName());
                    video_intent.putExtra("BLOG_USERIMAGE", blogs.getBlogUserImage());
                    video_intent.putExtra("BLOG_USERPROFESSION", blogs.getBlogUserProf());
                    video_intent.putExtra("BLOG_CONTACTINFO", blogs.getBlogContactInfo());
                    video_intent.putExtra("BLOG_ATTACHMENT", blogs.getBlogAttachments());
                    video_intent.putExtra("BLOG_COMPANY_ID", blogs.getBlogCompanyId());
                    video_intent.putExtra("BLOG_TRANSACTIONID", blogs.getBlogTransactionID());
                    video_intent.putExtra("BLOG_VIDEO_URL", blogs.getBlogVideoURL());
                    video_intent.putExtra("BLOG_VIEWS", blogs.getBlogNumberViews());
                    mContext.startActivity(video_intent);

                 /*   fragTransaction = fragManager.beginTransaction();
                    surgicaldetail.setArguments(bundle);
                    fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragTransaction.replace(R.id.dashboard_fragment,surgicaldetail,"SURGICAL_DETAILS");
                    fragTransaction.addToBackStack("SURGICAL_DETAILS");
                    fragTransaction.commit();*/
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogsList.size();
    }

    private String stripHtml(String response) {
        return Html.fromHtml(response).toString();
    }
}
