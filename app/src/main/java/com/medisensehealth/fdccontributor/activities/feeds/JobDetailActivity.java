package com.medisensehealth.fdccontributor.activities.feeds;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medisensehealth.fdccontributor.DataModel.Comments;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.adapter.feeds.CommentsAdapter;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.parser.JSONParser;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.FileDownloader;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomEditText;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewItalicBold;
import com.medisensehealth.fdccontributor.views.CustomTextViewSemiBold;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by salma on 07/03/18.
 */

public class JobDetailActivity extends AppCompatActivity implements View.OnClickListener {

    int BLOG_ID, BLOG_COMPANY_ID;
    String BLOG_IMAGE, BLOG_TITLE, BLOG_AUTHOR, BLOG_DATE, BLOG_DESCRIPTION, BLOG_TYPE, BLOG_USERNAME, BLOG_PROFESSION, BLOG_USERIMAGE;
    String DOWNLOAD_BLOG_IMAGE_URL, BLOG_CONTACTINFO, BLOG_ATTACHMENTS, BLOG_TRANSACTIONID, BLOG_NUM_VIEWS;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME, USER_LOGIN_TYPE;

    //Pdf request code
    private int PICK_PDF_REQUEST = 1;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Uri to store the image uri
    private Uri selectedImageUri;

    ImageView job_image;
    CustomTextView job_title, job_numviews, job_username, job_postdate;
    CustomTextView  job_like_count, job_comment_count;
    CustomTextView job_description, interview_date, contact_info;
    CustomTextViewItalicBold jobdetail_desc_readmore;
    Button apply_job_button;
    CustomTextView resume_name;
    String get_imagepath;
    EditText edt_mobile;
    CustomTextViewSemiBold download_job_btn;
    CustomEditText job_write_comment, _edt_email_share;
    ImageView job_like, job_comment, share_via_email, share_via_facebook, share_via_twitter, share_via_googleplus, share_via_whatsapp, share_via_linkedin;
    LinearLayout job_writelayout;
    ImageView  send_comments;
    boolean comment_visible = false;
    private List<Comments> commentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommentsAdapter mAdapter;
    Comments comment;
    View footertabs;
    ImageView userProfile_image;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_job_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);

            BLOG_ID = bundle.getInt("BLOG_ID", 0);
            BLOG_IMAGE = bundle.getString("BLOG_IMAGE", "");
            BLOG_TITLE = bundle.getString("BLOG_TITLE", "");
            BLOG_DATE = bundle.getString("BLOG_DATE", "");
            BLOG_DESCRIPTION = bundle.getString("BLOG_DESCRIPTION", "");
            BLOG_TYPE = bundle.getString("BLOG_TYPE", "");
            BLOG_USERNAME  = bundle.getString("BLOG_USERNAME", "");
            BLOG_PROFESSION = bundle.getString("BLOG_USERPROFESSION", "");
            BLOG_USERIMAGE  = bundle.getString("BLOG_USERIMAGE", "");
            BLOG_CONTACTINFO = bundle.getString("BLOG_CONTACTINFO", "");
            BLOG_ATTACHMENTS = bundle.getString("BLOG_ATTACHMENT", "");
            BLOG_COMPANY_ID  = bundle.getInt("BLOG_COMPANY_ID", 0);
            BLOG_TRANSACTIONID  = bundle.getString("BLOG_TRANSACTIONID", "");
            BLOG_NUM_VIEWS = bundle.getString("BLOG_VIEWS", "");

            Log.d(Utils.TAG , "******************* Job Details *************");
            Log.d(Utils.TAG + "Blog ID:", String.valueOf(BLOG_ID));
            Log.d(Utils.TAG + "Blog Image:", BLOG_IMAGE);
            Log.d(Utils.TAG + "Blog Title:", BLOG_TITLE);
            Log.d(Utils.TAG + "Blog Type:", BLOG_TYPE);
            Log.d(Utils.TAG + "Blog Date:", BLOG_DATE);
            Log.d(Utils.TAG + "Blog Desc:", BLOG_DESCRIPTION);
            Log.d(Utils.TAG + "Blog uname:", BLOG_USERNAME);
            Log.d(Utils.TAG + "Blog prof:", BLOG_PROFESSION);
            Log.d(Utils.TAG + "Blog image:", BLOG_USERIMAGE);
            Log.d(Utils.TAG + "Blog COMPANY:", String.valueOf(BLOG_COMPANY_ID));
            Log.d(Utils.TAG + "Blog transId:", BLOG_TRANSACTIONID);
        }

        shareadPreferenceClass = new ShareadPreferenceClass(JobDetailActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(JobDetailActivity.this);

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        Log.d(Utils.TAG+"USER_ID: ", String.valueOf(USER_ID));
        Log.d(Utils.TAG+"LOGIN_TYPE: ", USER_LOGIN_TYPE);

        initializationViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void initializationViews() {
        comment_visible = false;
        //Requesting storage permission
        requestStoragePermission();

        job_image = (ImageView) findViewById(R.id.jobdetail_image);
        job_title = (CustomTextView) findViewById(R.id.jobdetail_title);
        job_description = (CustomTextView) findViewById(R.id.jobdetail_desc);
        interview_date = (CustomTextView) findViewById(R.id.jobdetail_date);
        apply_job_button = (Button) findViewById(R.id.jobdetail_applyjob);
        apply_job_button.setOnClickListener(this);
        contact_info = (CustomTextView) findViewById(R.id.jobdetail_contact);
        download_job_btn = (CustomTextViewSemiBold) findViewById(R.id.jobdetail_download);
        download_job_btn.setOnClickListener(this);
        _edt_email_share = (CustomEditText) findViewById(R.id.jobdetail_email);
        jobdetail_desc_readmore = (CustomTextViewItalicBold) findViewById(R.id.jobdetail_desc_readmore);
        jobdetail_desc_readmore.setOnClickListener(this);

        job_like = (ImageView) findViewById(R.id.jobdetail_like);
        job_comment = (ImageView) findViewById(R.id.jobdetail_comment);
        job_writelayout = (LinearLayout) findViewById(R.id.jobdetail_comment_layout);
        job_write_comment = (CustomEditText) findViewById(R.id.jobdetail_write_comment);
        send_comments = (ImageView) findViewById(R.id.jobdetail_send_comment);
        job_like_count = (CustomTextView)  findViewById(R.id.jobdetail_like_count);
        job_comment_count  = (CustomTextView) findViewById(R.id.jobdetail_comment_count);
        job_numviews = (CustomTextView) findViewById(R.id.jobdetail_views);
        job_username = (CustomTextView) findViewById(R.id.jobdetail_userName);
        job_postdate = (CustomTextView) findViewById(R.id.jobdetail_postedon);
        job_like.setOnClickListener(this);
        job_comment.setOnClickListener(this);
        send_comments.setOnClickListener(this);

        share_via_email = (ImageView) findViewById(R.id.jobdetail_send_email);
        share_via_facebook  = (ImageView) findViewById(R.id.jobdetail_facebook);
        share_via_twitter = (ImageView) findViewById(R.id.jobdetail_twitter);
        share_via_whatsapp = (ImageView) findViewById(R.id.jobdetail_whatsapp);
        share_via_linkedin = (ImageView) findViewById(R.id.jobdetail_linkedin);
        share_via_email.setOnClickListener(this);
        share_via_facebook.setOnClickListener(this);
        share_via_twitter.setOnClickListener(this);
        share_via_whatsapp.setOnClickListener(this);
        share_via_linkedin.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.jobdetail_recyclerview);
        userProfile_image = (ImageView) findViewById(R.id.jobdetail_userprofileImg);

        mAdapter = new CommentsAdapter(JobDetailActivity.this, commentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(JobDetailActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        setRequestToServer();

        if(BLOG_USERIMAGE.equals("")) {

        }
        else {
            String urlProStr = BLOG_USERIMAGE;
            URL url_pof = null;
            try {
                url_pof = new URL(urlProStr);
                URI uri = new URI(url_pof.getProtocol(), url_pof.getUserInfo(), url_pof.getHost(), url_pof.getPort(), url_pof.getPath(), url_pof.getQuery(), url_pof.getRef());
                url_pof = uri.toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            Picasso.with(JobDetailActivity.this).load(String.valueOf(url_pof))
                    .placeholder(R.drawable.user_profile)
                    .error(R.drawable.user_profile)
                    .fit()
                    .into(userProfile_image, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }

        if(BLOG_IMAGE.equals("")) {
            job_image.setVisibility(View.GONE);
        }
        else {

            if(BLOG_TYPE.equalsIgnoreCase("Blog")) {
                DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_BLOGS_IMAGE_URL+String.valueOf(BLOG_ID)+"/"+BLOG_IMAGE.trim();
            }
            else  if(BLOG_TYPE.equalsIgnoreCase("Offers")) {
                DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_OFFERS_EVENTS_URL+String.valueOf(BLOG_ID)+"/"+BLOG_IMAGE.trim();
            }
            else  if(BLOG_TYPE.equalsIgnoreCase("Jobs")) {
                DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_OFFERS_EVENTS_URL+String.valueOf(BLOG_ID)+"/"+BLOG_IMAGE.trim();
            }
            else  if(BLOG_TYPE.equalsIgnoreCase("Events")) {
                DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_OFFERS_EVENTS_URL+String.valueOf(BLOG_ID)+"/"+BLOG_IMAGE.trim();
            }

            //  Picasso.with(getActivity()).load(DOWNLOAD_BLOG_IMAGE_URL).into(_image);
            String urlBlogStr = DOWNLOAD_BLOG_IMAGE_URL;
            URL url = null;
            try {
                url = new URL(urlBlogStr);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                url = uri.toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            job_image.setVisibility(View.VISIBLE);
            Picasso.with(JobDetailActivity.this).load(String.valueOf(url))
                    .placeholder(R.drawable.blogs_empty_img)
                    .error(R.drawable.blogs_empty_img)
                    .into(job_image, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }
        String Tile_Desc = stripHtml(BLOG_TITLE);
        job_title.setText(Tile_Desc);
        String Description = stripHtml(BLOG_DESCRIPTION);
        job_description.setText(Description);
        // interview_date.setText(BLOG_DATE);
        String CONTACT = stripHtml(BLOG_CONTACTINFO);
        contact_info.setText(CONTACT);
        job_numviews.setText("Views: "+BLOG_NUM_VIEWS);
        job_username.setText(BLOG_USERNAME);

        if(BLOG_DATE.equals("")) {

        }
        else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date newDate = null;
            try {
                newDate = format.parse(BLOG_DATE);
                format = new SimpleDateFormat("dd MMM yy");
                String post_date = format.format(newDate);
                job_postdate.setText(post_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        String CurrentString = BLOG_DATE;
        String[] separated = CurrentString.split(" / ");
        String from_date = separated[0];
        String to_date = separated[1];
        //  interview_date.setText("From: " +from_date +"\n" + "To: "+to_date);

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String inputDateStr = from_date;
        String inputToDateStr = to_date;
        Date date = null, date1 = null;
        try {
            date = inputFormat.parse(inputDateStr);
            date1  = inputFormat.parse(inputToDateStr);
            interview_date.setText("From:   " +outputFormat.format(date) +"\n" + "To:   "+outputFormat.format(date1));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private String stripHtml(String response) {
        return Html.fromHtml(response).toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jobdetail_applyjob:
                openApplyJobDialog();
                break;
            case R.id.apply_job_upload:
                //  Toast.makeText(getActivity(), "Upload ", Toast.LENGTH_SHORT).show();
                showFileChooser();
                break;
            case R.id.jobdetail_download:
                //  downloadJobDescription();
                if(BLOG_ATTACHMENTS != null && BLOG_ATTACHMENTS.length() != 0 ){
                    downloadJobDescription();
                }
                else {
                    Toast.makeText(JobDetailActivity.this, "No attachment/description to display !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.jobdetail_send_email:
                if(_edt_email_share.getText().toString().trim().equals("")) {
                    Toast.makeText(JobDetailActivity.this, "Please enter email address", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendJobEmailLink(_edt_email_share.getText().toString());
                }
                break;
            case R.id.jobdetail_facebook:
                shareViaFacebookLink();
                break;
            case R.id.jobdetail_twitter:
                shareViaTwiterLink();
                break;
            case R.id.jobdetail_whatsapp:
                shareViaWhatsAppLink();
                break;
            case R.id.jobdetail_linkedin:
                shareViaLinkedinLink();
                break;
            case R.id.jobdetail_like:
                String EVENT_TYPE = "1";
                job_like.setImageResource(R.mipmap.like_select);
                int count = Integer.parseInt(job_like_count.getText().toString());
                job_like_count.setText(String.valueOf(count+1));
                sendEventRequsetToServer(EVENT_TYPE);
                job_like.setEnabled(false);
                break;
            case R.id.jobdetail_comment:
                viewAllComments(commentList);
               /* if(comment_visible == false) {
                    job_writelayout.setVisibility(View.VISIBLE);
                    comment_visible = true;
                }
                else if(comment_visible == true) {
                    job_writelayout.setVisibility(View.GONE);
                    comment_visible = false;
                }*/
                break;
            case R.id.jobdetail_send_comment:
                if(job_write_comment.getText().toString().equals("")) {
                    Toast.makeText(JobDetailActivity.this, "Please write comments !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendBlogCommmentLink(job_write_comment.getText().toString());
                }
                break;
            case R.id.jobdetail_desc_readmore:
                if (jobdetail_desc_readmore.getText().toString().equals("Read More >>")) {
                    jobdetail_desc_readmore.setText("<< Read Less");
                    job_description.setSingleLine(false);
                    job_description.setMaxLines(5000);
                } else if (jobdetail_desc_readmore.getText().toString().equals("<< Read Less")) {
                    jobdetail_desc_readmore.setText("Read More >>");
                    job_description.setSingleLine(false);
                    job_description.setEllipsize(TextUtils.TruncateAt.END);
                    job_description.setLines(5);
                }
                break;
        }
    }

    private void viewAllComments(List<Comments> commentList) {
        final Dialog dialogViewCard = new Dialog(JobDetailActivity.this);
        dialogViewCard.setContentView(R.layout.cooments_view);
        dialogViewCard.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationView; //style id
        dialogViewCard.show();
        ProgressBar loadingBar  = (ProgressBar) dialogViewCard.findViewById(R.id.comments_progress_bar);
        final CustomEditText _edt_comment_msg = (CustomEditText) dialogViewCard.findViewById(R.id.comments_write_comment);
        ImageView send_comment = (ImageView) dialogViewCard.findViewById(R.id.comments_send_comment);

        RecyclerView recyclerView = (RecyclerView) dialogViewCard.findViewById(R.id.comments_recyclerview);

        mAdapter = new CommentsAdapter(JobDetailActivity.this, commentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(JobDetailActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        if(commentList.size() > 0 ) {
            mAdapter = new CommentsAdapter(JobDetailActivity.this, commentList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_edt_comment_msg.getText().toString().equals("")) {
                    Toast.makeText(JobDetailActivity.this, "Please write comments !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialogViewCard.cancel();
                    sendBlogCommmentLink(_edt_comment_msg.getText().toString());
                }
            }
        });
    }

    private void shareViaLinkedinLink() {
        Log.d(Utils.TAG + "TITLE:", BLOG_TITLE);
        Log.d(Utils.TAG + "TRANS:", BLOG_TRANSACTIONID);

        String LINKEDIN_SHARE_LINK = APIClass.DRS_PRACTICE_SURGICAL_SHARE+BLOG_TITLE+"/"+BLOG_TRANSACTIONID;
        String LINKEDIN_LINK = LINKEDIN_SHARE_LINK.replace(" ", "-");
        Log.d(Utils.TAG + "SHARE:", LINKEDIN_LINK);

        Intent linkedinIntent = new Intent(Intent.ACTION_SEND);
        linkedinIntent.putExtra(Intent.EXTRA_TEXT, BLOG_TITLE +" \nFor more info visit link \n"+ LINKEDIN_LINK);
        linkedinIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(linkedinIntent,  PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for(ResolveInfo resolveInfo: resolvedInfoList){
            if(resolveInfo.activityInfo.packageName.startsWith("com.linkedin.android")){
                linkedinIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name );
                resolved = true;
                break;
            }
        }
        if(resolved){
            startActivity(linkedinIntent);
        }else{
            Toast.makeText(JobDetailActivity.this, "Linkedin app isn't found", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareViaWhatsAppLink() {
        Log.d(Utils.TAG + "TITLE:", BLOG_TITLE);
        Log.d(Utils.TAG + "TRANS:", BLOG_TRANSACTIONID);

        String WHATSAPP_SHARE_LINK = APIClass.DRS_PRACTICE_JOB_SHARE+BLOG_TITLE+"/"+BLOG_TRANSACTIONID;
        String WHATSAPP_LINK = WHATSAPP_SHARE_LINK.replace(" ", "-");
        Log.d(Utils.TAG + "SHARE:", WHATSAPP_LINK);

        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, BLOG_TITLE +" \nFor more info visit link \n"+ WHATSAPP_LINK);
        try {
            startActivity(whatsappIntent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(JobDetailActivity.this, "Whatsapp have not been installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareViaTwiterLink() {

        Log.d(Utils.TAG + "TITLE:", BLOG_TITLE);
        Log.d(Utils.TAG + "TRANS:", BLOG_TRANSACTIONID);

        String TWITTER_SHARE_LINK = APIClass.DRS_PRACTICE_JOB_SHARE+BLOG_TITLE+"/"+BLOG_TRANSACTIONID;
        String TWITTER_LINK = TWITTER_SHARE_LINK.replace(" ", "-");
        Log.d(Utils.TAG + "SHARE:", TWITTER_LINK);

        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, BLOG_TITLE +" \nFor more info visit link \n"+ TWITTER_LINK);
        tweetIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent,  PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for(ResolveInfo resolveInfo: resolvedInfoList){
            if(resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")){
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name );
                resolved = true;
                break;
            }
        }
        if(resolved){
            startActivity(tweetIntent);
        }else{
            Toast.makeText(JobDetailActivity.this, "Twitter app isn't found", Toast.LENGTH_SHORT).show();
        }

    }

    private void shareViaFacebookLink() {
        Log.d(Utils.TAG + "TITLE:", BLOG_TITLE);
        Log.d(Utils.TAG + "TRANS:", BLOG_TRANSACTIONID);

        String FACEBOOK_SHARE_LINK = APIClass.DRS_PRACTICE_JOB_SHARE+BLOG_TITLE+"/"+BLOG_TRANSACTIONID;
        String FACEBOOK_LINK = FACEBOOK_SHARE_LINK.replace(" ", "-");
        Log.d(Utils.TAG + "SHARE:", FACEBOOK_LINK);

        Intent facebkIntent = new Intent(Intent.ACTION_SEND);
        facebkIntent.putExtra(Intent.EXTRA_TEXT, BLOG_TITLE +" \nFor more info visit link \n"+ FACEBOOK_LINK);
        facebkIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(facebkIntent,  PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for(ResolveInfo resolveInfo: resolvedInfoList){
            if(resolveInfo.activityInfo.packageName.startsWith("com.facebook.katana")){
                facebkIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name );
                resolved = true;
                break;
            }
        }
        if(resolved){
            startActivity(facebkIntent);
        }else{
            Toast.makeText(JobDetailActivity.this, "Facebook app isn't found", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendJobEmailLink(final String email_address) {

        Log.d(Utils.TAG, "email: "+email_address);
        Log.d(Utils.TAG, "trans: "+BLOG_TRANSACTIONID);
        Log.d(Utils.TAG, "type: "+BLOG_TYPE);

        final ProgressDialog progressDialog = new ProgressDialog(JobDetailActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending Email...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PRACTICE_SHARE_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, response.toString());
                        if (response != null) {
                            JSONObject jsonObject = null;
                            progressDialog.dismiss();
                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("result");

                                if(staus_res.equalsIgnoreCase("success")) {
                                    Toast.makeText(JobDetailActivity.this, "Email sent successfully.", Toast.LENGTH_SHORT).show();
                                    _edt_email_share.setText("");
                                }
                                else   if(staus_res.equalsIgnoreCase("failure")) {
                                    Toast.makeText(JobDetailActivity.this, "Failed to send an email.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                map.put(APIClass.KEY_SHARE_EMAIL, email_address);
                map.put(APIClass.KEY_SHARE_TRANSACTION_ID, BLOG_TRANSACTIONID);
                map.put(APIClass.KEY_SHARE_TYPE, BLOG_TYPE);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(JobDetailActivity.this).
                getRequestQueue();
        AppController.getInstance(JobDetailActivity.this).addToRequestQueue(stringRequest);
    }

    private void sendEventRequsetToServer(final String event_type) {

        Log.d(Utils.TAG, "event_type: " + event_type);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_EVENT_UPDATES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Utils.TAG, error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                map.put(APIClass.KEY_EVENTID, String.valueOf(BLOG_ID));
                map.put(APIClass.KEY_EVENT_LISTINGTYPE, BLOG_TYPE);
                map.put(APIClass.KEY_EVENT_TYPE, event_type);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(JobDetailActivity.this).
                getRequestQueue();
        AppController.getInstance(JobDetailActivity.this).addToRequestQueue(stringRequest);
    }

    private void sendBlogCommmentLink(final String comment_text) {
        Log.d(Utils.TAG, " comment: "+comment_text);

        final ProgressDialog progressDialog = new ProgressDialog(JobDetailActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_FEEDS_COMMENT_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, response.toString());
                        if (response != null) {
                            JSONObject jsonObject = null;
                            progressDialog.dismiss();
                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("result");

                                if(staus_res.equalsIgnoreCase("success")) {
                                    Toast.makeText(JobDetailActivity.this, "Comments added successfully.", Toast.LENGTH_SHORT).show();
                                    job_write_comment.setText("");
                                    setRequestToServer();
                                }
                                else   if(staus_res.equalsIgnoreCase("failure")) {
                                    Toast.makeText(JobDetailActivity.this, "Failed to add comment.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                map.put(APIClass.KEY_PRACTICE_COMMENTS_TEXT, comment_text);
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                map.put(APIClass.KEY_PRACTICE_COMMENTS_POSTID, String.valueOf(BLOG_ID));
                map.put(APIClass.KEY_PRACTICE_COMMENTS_POST_TYPE, BLOG_TYPE);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(JobDetailActivity.this).
                getRequestQueue();
        AppController.getInstance(JobDetailActivity.this).addToRequestQueue(stringRequest);
    }

    private void setRequestToServer() {
        commentList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_FEEDS_LIKE_COMMENT_COUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, " jobs likes: "+ response.toString());

                        if (response != null) {
                            int likes_check, likes_count, commentCount;
                            JSONObject jsonObject;
                            JSONArray jsonArray;
                            try {
                                jsonObject = new JSONObject(response);
                                JSONObject  success = jsonObject.getJSONObject("like");
                                likes_check = success.getInt("checkLike");
                                likes_count = success.getInt("countLike");
                                commentCount  = success.getInt("countComment");
                                if(likes_check > 0 ) {
                                    job_like_count.setText(String.valueOf(likes_count));
                                    job_comment_count.setText(String.valueOf(commentCount));
                                    job_like.setImageResource(R.mipmap.like_select);
                                    job_like.setEnabled(false);
                                }
                                else {
                                    job_like_count.setText(String.valueOf(likes_count));
                                    job_comment_count.setText(String.valueOf(commentCount));
                                    job_like.setImageResource(R.mipmap.like_empty);
                                    job_like.setEnabled(true);
                                }

                                jsonArray = jsonObject.getJSONArray("comments");
                                Log.d(Utils.TAG, "array: " + jsonArray.length());
                                if(jsonArray.length() > 0) {
                                    for(int i=0;i<jsonArray.length(); i++)
                                    {
                                        JSONObject JOStuff = jsonArray.getJSONObject(i);
                                        comment = new Comments(JOStuff.getString("username"),JOStuff.getString("user_image"),
                                                JOStuff.getString("comments"),JOStuff.getString("post_date"),JOStuff.getString("topic_id"),
                                                JOStuff.getString("topic_type"));
                                        commentList.add(comment);
                                    }

                                    Log.d(Utils.TAG, "size: " + commentList.size());
                                    if(commentList.size() > 0 ) {
                                        mAdapter = new CommentsAdapter(JobDetailActivity.this, commentList);
                                        recyclerView.setAdapter(mAdapter);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Utils.TAG, error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                map.put(APIClass.KEY_EVENTID, String.valueOf(BLOG_ID));
                map.put(APIClass.KEY_EVENT_LISTINGTYPE, BLOG_TYPE);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(JobDetailActivity.this).
                getRequestQueue();
        AppController.getInstance(JobDetailActivity.this).addToRequestQueue(stringRequest);
    }

    private void openApplyJobDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(JobDetailActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.apply_job_dialog, null);
        dialogBuilder.setView(dialogView);
        //   dialogBuilder.setTitle("Introduce Yourself");

        TextView title = new TextView(JobDetailActivity.this);
        // You Can Customise your Title here
        title.setText("About yourself");
        title.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(22);

        dialogBuilder.setPositiveButton("Submit", null);
        dialogBuilder.setNegativeButton("Cancel", null);
        CustomTextViewSemiBold attach_resume = (CustomTextViewSemiBold)dialogView.findViewById(R.id.apply_job_upload);
        attach_resume.setOnClickListener(this);
        resume_name = (CustomTextView) dialogView.findViewById(R.id.appy_job_file);

        final TextInputLayout _mobile_TL = (TextInputLayout) dialogView.findViewById(R.id.apply_job_coverTL);
        edt_mobile = (EditText) dialogView.findViewById(R.id.apply_joc_text);


        final AlertDialog b = dialogBuilder.create();
        b.show();

        // Override the button's on-click so it doesn't close by default
        b.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile_naum = edt_mobile.getEditableText().toString().trim();

                if((edt_mobile.getText().toString().equals(""))) {
                    //  Toast.makeText(getActivity(), "Enter either Mobile or Email Address", Toast.LENGTH_SHORT).show();
                    _mobile_TL.setError("Cover Note is required");
                    return;
                }
                else if((resume_name.getText().toString().equals(""))) {
                    Toast.makeText(JobDetailActivity.this, "Attach Resume is required !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    _mobile_TL.setError(null);
                    b.dismiss();
                    sendJobRequestToServer(edt_mobile.getText().toString(), resume_name.getText().toString());
                }
            }
        });

        b.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

    }

    private void sendJobRequestToServer(final String job_conernote, final String get_attachpath) {

        new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(JobDetailActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {
                    JSONObject jsonObject = JSONParser.applyJob( job_conernote,  get_attachpath, USER_ID, BLOG_ID);

                    if (jsonObject != null) {
                        Log.e(Utils.TAG, " GET: " + jsonObject.getString("result"));
                        if(jsonObject.getString("result").equals("success")) {
                            status = true;
                        }
                        else {
                            status = false;
                        }
                    }
                    // return true;
                } catch (JSONException e) {
                    Log.i(Utils.TAG, "Error : " + e.getLocalizedMessage());
                    return false;
                }
                return status;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (  progressDialog != null)
                    progressDialog.dismiss();
                Log.e(Utils.TAG, "aBoolean: " + String.valueOf(aBoolean));
                if (aBoolean) {
                    // Toast.makeText(SignUpActivity.this,"Registered Successfully", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(JobDetailActivity.this);
                    alertDialogBuilder.setMessage("Resume sent successfully ");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });

                    alertDialogBuilder.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else {
                    Toast.makeText(JobDetailActivity.this, "You have already applied to the job.", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();

    }

    private void downloadJobDescription() {

        String DOWNLOAD_JIB_URL = APIClass.DRS_PRACTICE_JOB_DESCRIPTION+"comp_id="+BLOG_COMPANY_ID+"&attach_name="+BLOG_ATTACHMENTS;

        String job_url = DOWNLOAD_JIB_URL;
        URL url = null;
        try {
            url = new URL(job_url);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Log.d(Utils.TAG + "jOB_URL:", DOWNLOAD_JIB_URL);
     /*   int pos = DOWNLOAD_JIB_URL.lastIndexOf('/') + 1;
        String  urlString = DOWNLOAD_JIB_URL.substring(0, pos) + Uri.encode(DOWNLOAD_JIB_URL.substring(pos));
        Log.d(Utils.TAG, "urlString: " + urlString);*/

        new DownloadFile().execute(String.valueOf(url), BLOG_ATTACHMENTS+".pdf");
    }

    private class DownloadFile extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "FDCPixelPrime");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            showPdf();
            return null;
        }
    }

    public void showPdf()
    {
        File pdfFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/FDCPixelPrime/" + BLOG_ATTACHMENTS+".pdf");  // -> filename = maven.pdf

        if(pdfFile.exists()) {
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(JobDetailActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(JobDetailActivity.this, "File path is incorrect." , Toast.LENGTH_SHORT).show();
        }
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent =  new Intent();
        intent.setType("application/word|application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Attachments"), PICK_PDF_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            String FilePath = data.getData().getPath();
            resume_name.setText(FilePath);

            if(selectedImageUri != null){
                try {
                    // OI FILE Manager
                    String filemanagerstring = selectedImageUri.getPath();

                    // MEDIA GALLERY
                    String selectedImagePath = getPath(selectedImageUri);
                    get_imagepath = getPath(selectedImageUri);
                    String filePath = null;

                    if (selectedImagePath != null) {
                        filePath = selectedImagePath;
                    } else if (filemanagerstring != null) {
                        filePath = filemanagerstring;
                    } else {
                        Toast.makeText(JobDetailActivity.this, "Unknown path",Toast.LENGTH_LONG).show();
                        Log.e("Bitmap", "Unknown path");
                    }

                    if (filePath != null) {
                        resume_name.setText(filePath);
                    }
                } catch (Exception e) {
                    Toast.makeText(JobDetailActivity.this, "Internal error", Toast.LENGTH_LONG).show();
                    Log.e(e.getClass().getName(), e.getMessage(), e);
                }
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(JobDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(JobDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(JobDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(JobDetailActivity.this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(JobDetailActivity.this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
