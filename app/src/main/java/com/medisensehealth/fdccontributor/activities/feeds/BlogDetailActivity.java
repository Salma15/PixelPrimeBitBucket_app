package com.medisensehealth.fdccontributor.activities.feeds;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomEditText;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewItalicBold;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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

public class BlogDetailActivity extends AppCompatActivity implements View.OnClickListener {

    int BLOG_ID, BLOG_COMPANY_ID;
    String BLOG_IMAGE, BLOG_TITLE, BLOG_AUTHOR, BLOG_DATE, BLOG_DESCRIPTION, BLOG_TYPE, BLOG_USERNAME, BLOG_PROFESSION, BLOG_USERIMAGE;
    String DOWNLOAD_BLOG_IMAGE_URL, BLOG_CONTACTINFO, BLOG_ATTACHMENTS, BLOG_TRANSACTIONID, BLOG_NUM_VIEWS;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME, USER_LOGIN_TYPE;

    ImageView send_comments, blog_Image, blog_like, blog_comment, send_email, share_facebook, share_twitter, share_whatsapp, share_googleplus, share_linkedin;
    CustomTextView blog_like_count, blog_comment_count;
    CustomTextViewItalicBold blogdetail_desc_readmore;
    LinearLayout blog_writelayout;
    CustomEditText blog_write_comment, blog_email;
    boolean comment_visible = false;
    CustomTextView blog_title, blog_description, blog_postdate, blog_numviews, blog_username;

    private List<Comments> commentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommentsAdapter mAdapter;
    Comments comment;
    View footertabs;
    ImageView userProfile_image;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_blog_details);

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

            Log.d(Utils.TAG , "******************* Blog Details *************");
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

        shareadPreferenceClass = new ShareadPreferenceClass(BlogDetailActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(BlogDetailActivity.this);
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
        blog_Image = (ImageView) findViewById(R.id.blogdetail_image);
        blog_title = (CustomTextView) findViewById(R.id.blogdetail_title);
        blog_description = (CustomTextView) findViewById(R.id.blogdetail_desc);
        blog_like = (ImageView) findViewById(R.id.blogdetail_like);
        blog_comment = (ImageView) findViewById(R.id.blogdetail_comment);
        blog_writelayout = (LinearLayout) findViewById(R.id.blogdetail_comment_layout);
        blog_write_comment = (CustomEditText) findViewById(R.id.blogdetail_write_comment);
        send_comments = (ImageView) findViewById(R.id.blogdetail_send_comment);
        blog_email = (CustomEditText) findViewById(R.id.blogdetail_email);
        send_email = (ImageView) findViewById(R.id.blogdetail_send_email);
        share_facebook = (ImageView) findViewById(R.id.blogdetail_facebook);
        share_twitter = (ImageView) findViewById(R.id.blogdetail_twitter);
        share_whatsapp = (ImageView) findViewById(R.id.blogdetail_whatsapp);
        share_linkedin = (ImageView) findViewById(R.id.blogdetail_linkedin);
        blog_like_count = (CustomTextView) findViewById(R.id.blogdetail_like_count);
        blog_comment_count  = (CustomTextView) findViewById(R.id.blogdetail_comment_count);
        blog_postdate = (CustomTextView) findViewById(R.id.blogdetail_postedon);
        blog_numviews = (CustomTextView) findViewById(R.id.blogdetail_views);
        blog_username = (CustomTextView) findViewById(R.id.blogdetail_userName);
        blog_like.setOnClickListener(this);
        blog_comment.setOnClickListener(this);
        send_comments.setOnClickListener(this);
        send_email.setOnClickListener(this);
        share_facebook.setOnClickListener(this);
        share_twitter.setOnClickListener(this);
        share_whatsapp.setOnClickListener(this);
        share_linkedin.setOnClickListener(this);
        blogdetail_desc_readmore = (CustomTextViewItalicBold) findViewById(R.id.blogdetail_desc_readmore);
        blogdetail_desc_readmore.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.blogdetail_recyclerview);

        userProfile_image = (ImageView) findViewById(R.id.blogdetail_userprofileImg);

        mAdapter = new CommentsAdapter(this, commentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
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

            Picasso.with(this).load(String.valueOf(url_pof))
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
            blog_Image.setVisibility(View.GONE);
        }
        else {
            blog_Image.setVisibility(View.VISIBLE);
            if(BLOG_TYPE.equalsIgnoreCase("Blog")) {
                DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_BLOGS_IMAGE_URL+String.valueOf(BLOG_ID)+"/"+BLOG_IMAGE;
            }
            else  if(BLOG_TYPE.equalsIgnoreCase("Offers")) {
                DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_OFFERS_EVENTS_URL+String.valueOf(BLOG_ID)+"/"+BLOG_IMAGE;
            }
            else  if(BLOG_TYPE.equalsIgnoreCase("Jobs")) {
                DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_OFFERS_EVENTS_URL+String.valueOf(BLOG_ID)+"/"+BLOG_IMAGE;
            }
            else  if(BLOG_TYPE.equalsIgnoreCase("Events")) {
                DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_OFFERS_EVENTS_URL+String.valueOf(BLOG_ID)+"/"+BLOG_IMAGE;
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

            Picasso.with(this).load(String.valueOf(url))
                    .placeholder(R.drawable.blogs_empty_img)
                    .error(R.drawable.blogs_empty_img)
                    .resize(400, 400)
                    .centerInside()
                    .into(blog_Image, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }

        String Tile_Desc = stripHtml(BLOG_TITLE);
        blog_title.setText(Tile_Desc);
        String Description = stripHtml(BLOG_DESCRIPTION);
        blog_description.setText(Description);
        blog_numviews.setText("Views: "+BLOG_NUM_VIEWS);
        blog_username.setText(BLOG_USERNAME);

        if(BLOG_DATE.equals("")) {

        }
        else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date newDate = null;
            try {
                newDate = format.parse(BLOG_DATE);
                format = new SimpleDateFormat("dd MMM yy");
                String post_date = format.format(newDate);
                blog_postdate.setText(post_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.blogdetail_like:
                String EVENT_TYPE = "1";
                blog_like.setImageResource(R.mipmap.like_select);
                int count = Integer.parseInt(blog_like_count.getText().toString());
                blog_like_count.setText(String.valueOf(count+1));
                sendEventRequsetToServer(EVENT_TYPE);
                blog_like.setEnabled(false);
                break;
            case R.id.blogdetail_comment:
                viewAllComments(commentList);
                break;
            case R.id.blogdetail_send_comment:
                if(blog_write_comment.getText().toString().equals("")) {
                    Toast.makeText(this, "Please write comments !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendBlogCommmentLink(blog_write_comment.getText().toString());
                }
                break;
            case R.id.blogdetail_send_email:
                if(blog_email.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendBlogEmailLink(blog_email.getText().toString());
                }
                break;
            case R.id.blogdetail_facebook:
                shareViaFacebookLink();
                break;
            case R.id.blogdetail_twitter:
                shareViaTwiterLink();
                break;
            case R.id.blogdetail_whatsapp:
                shareViaWhatsAppLink();
                break;
            case R.id.blogdetail_linkedin:
                shareViaLinkedinLink();
                break;
            case R.id.blogdetail_desc_readmore:
                if (blogdetail_desc_readmore.getText().toString().equals("Read More >>")) {
                    blogdetail_desc_readmore.setText("<< Read Less");
                    blog_description.setSingleLine(false);
                    blog_description.setMaxLines(5000);
                } else if (blogdetail_desc_readmore.getText().toString().equals("<< Read Less")) {
                    blogdetail_desc_readmore.setText("Read More >>");
                    blog_description.setSingleLine(false);
                    blog_description.setEllipsize(TextUtils.TruncateAt.END);
                    blog_description.setLines(5);
                }
                break;
        }
    }

    private void viewAllComments(List<Comments> commentList) {
        final Dialog dialogViewCard = new Dialog(BlogDetailActivity.this);
        dialogViewCard.setContentView(R.layout.cooments_view);
        dialogViewCard.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationView; //style id
        dialogViewCard.show();
        ProgressBar loadingBar  = (ProgressBar) dialogViewCard.findViewById(R.id.comments_progress_bar);
        final CustomEditText _edt_comment_msg = (CustomEditText) dialogViewCard.findViewById(R.id.comments_write_comment);
        ImageView send_comment = (ImageView) dialogViewCard.findViewById(R.id.comments_send_comment);

        RecyclerView recyclerView = (RecyclerView) dialogViewCard.findViewById(R.id.comments_recyclerview);

        mAdapter = new CommentsAdapter(BlogDetailActivity.this, commentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(BlogDetailActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        if(commentList.size() > 0 ) {
            mAdapter = new CommentsAdapter(BlogDetailActivity.this, commentList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_edt_comment_msg.getText().toString().equals("")) {
                    Toast.makeText(BlogDetailActivity.this, "Please write comments !!!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Linkedin app isn't found", Toast.LENGTH_LONG).show();
        }
    }

    private void shareViaWhatsAppLink() {
        Log.d(Utils.TAG + "TITLE:", BLOG_TITLE);
        Log.d(Utils.TAG + "TRANS:", BLOG_TRANSACTIONID);

        String WHATSAPP_SHARE_LINK = APIClass.DRS_PRACTICE_SURGICAL_SHARE+BLOG_TITLE+"/"+BLOG_TRANSACTIONID;
        String WHATSAPP_LINK = WHATSAPP_SHARE_LINK.replace(" ", "-");
        Log.d(Utils.TAG + "SHARE:", WHATSAPP_LINK);

        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, BLOG_TITLE +" \nFor more info visit link \n"+ WHATSAPP_LINK);
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Whatsapp have not been installed", Toast.LENGTH_LONG).show();
        }
    }

    private void shareVialGooglePlusLink() {
        Log.d(Utils.TAG + "TITLE:", BLOG_TITLE);
        Log.d(Utils.TAG + "TRANS:", BLOG_TRANSACTIONID);

        String GPLUS_SHARE_LINK = APIClass.DRS_PRACTICE_SURGICAL_SHARE+BLOG_TITLE+"/"+BLOG_TRANSACTIONID;
        String GPLUS_LINK = GPLUS_SHARE_LINK.replace(" ", "-");
        Log.d(Utils.TAG + "SHARE:", GPLUS_LINK);

        Intent googleIntent = new Intent(Intent.ACTION_SEND);
        googleIntent.setType("text/plain");
        googleIntent.putExtra(Intent.EXTRA_TEXT, BLOG_TITLE +" \nFor more info visit link \n"+ GPLUS_LINK);

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(googleIntent,  PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for(ResolveInfo resolveInfo: resolvedInfoList){
            if(resolveInfo.activityInfo.packageName.startsWith("com.google.android.apps.plus")){
                googleIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name );
                resolved = true;
                break;
            }
        }
        if(resolved){
            startActivity(googleIntent);
        }else{
            Toast.makeText(this, "Google Plus app isn't found", Toast.LENGTH_LONG).show();
        }

    }

    private void shareViaTwiterLink() {

        Log.d(Utils.TAG + "TITLE:", BLOG_TITLE);
        Log.d(Utils.TAG + "TRANS:", BLOG_TRANSACTIONID);

        String TWITTER_SHARE_LINK = APIClass.DRS_PRACTICE_SURGICAL_SHARE+BLOG_TITLE+"/"+BLOG_TRANSACTIONID;
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
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    }

    private void shareViaFacebookLink() {
        Log.d(Utils.TAG + "TITLE:", BLOG_TITLE);
        Log.d(Utils.TAG + "TRANS:", BLOG_TRANSACTIONID);

        String FACEBOOK_SHARE_LINK = APIClass.DRS_PRACTICE_SURGICAL_SHARE+BLOG_TITLE+"/"+BLOG_TRANSACTIONID;
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
            Toast.makeText(this, "Facebook app isn't found", Toast.LENGTH_LONG).show();
        }
    }

    private void sendBlogEmailLink(final String email_address) {

        Log.d(Utils.TAG, "email: "+email_address);
        Log.d(Utils.TAG, "trans: "+BLOG_TRANSACTIONID);
        Log.d(Utils.TAG, "type: "+BLOG_TYPE);

        final ProgressDialog progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
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
                                    Toast.makeText(BlogDetailActivity.this, "Email sent successfully.", Toast.LENGTH_SHORT).show();
                                    blog_email.setText("");
                                }
                                else   if(staus_res.equalsIgnoreCase("failure")) {
                                    Toast.makeText(BlogDetailActivity.this, "Failed to send an email.", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = AppController.getInstance(BlogDetailActivity.this).
                getRequestQueue();
        AppController.getInstance(BlogDetailActivity.this).addToRequestQueue(stringRequest);
    }

    private String stripHtml(String response) {
        return Html.fromHtml(response).toString();
    }

    private void setRequestToServer() {
        commentList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_FEEDS_LIKE_COMMENT_COUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, " blog likes: "+ response.toString());

                        if (response != null) {
                            // String success;
                            int likes_check, likes_count, commentCount;
                            JSONObject jsonObject;
                            JSONArray jsonArray;
                            try {
                                jsonObject = new JSONObject(response);
                                JSONObject  success = jsonObject.getJSONObject("like");
                                Log.d(Utils.TAG, "status: " + success);
                                likes_check = success.getInt("checkLike");
                                likes_count = success.getInt("countLike");
                                commentCount  = success.getInt("countComment");
                                if(likes_check > 0 ) {
                                    blog_like_count.setText(String.valueOf(likes_count));
                                    blog_comment_count.setText(String.valueOf(commentCount));
                                    blog_like.setImageResource(R.mipmap.like_select);
                                    blog_like.setEnabled(false);
                                }
                                else {
                                    blog_like_count.setText(String.valueOf(likes_count));
                                    blog_comment_count.setText(String.valueOf(commentCount));
                                    blog_like.setImageResource(R.mipmap.like_empty);
                                    blog_like.setEnabled(true);
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
                                        mAdapter = new CommentsAdapter(BlogDetailActivity.this, commentList);
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

        RequestQueue requestQueue = AppController.getInstance(BlogDetailActivity.this).
                getRequestQueue();
        AppController.getInstance(BlogDetailActivity.this).addToRequestQueue(stringRequest);
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

        RequestQueue requestQueue = AppController.getInstance(BlogDetailActivity.this).
                getRequestQueue();
        AppController.getInstance(BlogDetailActivity.this).addToRequestQueue(stringRequest);
    }

    private void sendBlogCommmentLink(final String comment_text) {
        Log.d(Utils.TAG, " comment: "+comment_text);

        final ProgressDialog progressDialog = new ProgressDialog(BlogDetailActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
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
                                    Toast.makeText(BlogDetailActivity.this, "Comments added successfully.", Toast.LENGTH_SHORT).show();
                                    blog_write_comment.setText("");
                                    setRequestToServer();
                                }
                                else   if(staus_res.equalsIgnoreCase("failure")) {
                                    Toast.makeText(BlogDetailActivity.this, "Failed to add comment.", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = AppController.getInstance(BlogDetailActivity.this).
                getRequestQueue();
        AppController.getInstance(BlogDetailActivity.this).addToRequestQueue(stringRequest);
    }

}
