package com.medisensehealth.fdccontributor.activities.notifications;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.DashboardActivity;
import com.medisensehealth.fdccontributor.database.MedisensePracticeDB;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;
import com.medisensehealth.fdccontributor.views.CustomTextViewItalic;
import com.medisensehealth.fdccontributor.views.CustomTextViewItalicBold;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by SALMA on 11-10-2018.
 */
public class NotifyMessageDetailActivity extends AppCompatActivity {

    int VERIFY_ID, POST_ID;
    String POST_IMAGE, POST_TITLE, POST_AUTHOR, POST_DATE, POST_DESCRIPTION, POST_TYPE, POST_ENTRY;
    String DOWNLOAD_BLOG_IMAGE_URL;

    ImageView _image, _author_profile;
    CustomTextViewBold title;
    final ThreadLocal<CustomTextView> _full_description = new ThreadLocal<>();
    CustomTextViewItalicBold _author_name;
    CustomTextViewItalic _post_date;

    ImageView post_like, post_share, post_comment;
    TextView post_likes_label, post_comment_label, post_share_label;

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME, USER_LOGIN_TYPE;
    String EVENT_TYPE, BLOG_TYPE;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);

            VERIFY_ID = bundle.getInt("NOTIFY_ID", 0);
            POST_ID = bundle.getInt("POST_ID", 0);
            POST_IMAGE = bundle.getString("POST_IMAGE", "");
            POST_TITLE = bundle.getString("POST_TITLE", "");
            POST_AUTHOR = bundle.getString("POST_AUTHOR", "");
            POST_DATE = bundle.getString("POST_DATE", "");
            POST_DESCRIPTION = bundle.getString("POST_MESSAGE", "");
            POST_TYPE = bundle.getString("POST_TYPE", "");
            POST_ENTRY = bundle.getString("POST_ENTRY", "");

            Log.d(Utils.TAG, " ************ NotifyMessageDetailActivity *********** ");
            Log.d(Utils.TAG + " VERIFY_ID: ", String.valueOf(VERIFY_ID));
            Log.d(Utils.TAG + " POST_ID: ", String.valueOf(POST_ID));
            Log.d(Utils.TAG + " POST_TITLE: ", POST_TITLE);
            Log.d(Utils.TAG + " POST_IMAGE: ", POST_IMAGE);
            Log.d(Utils.TAG + " POST_ATOR: ", POST_AUTHOR);
            Log.d(Utils.TAG + " POST_DATE: ", POST_DATE);
            Log.d(Utils.TAG + " POST_DESC: ", POST_DESCRIPTION);
            Log.d(Utils.TAG + " POST_TYPE: ", POST_TYPE);
            Log.d(Utils.TAG + " POST_ENTRY: ", POST_ENTRY);
        }

        shareadPreferenceClass = new ShareadPreferenceClass(NotifyMessageDetailActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(NotifyMessageDetailActivity.this);

        if ((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, "0").equals("1"))) {
            USER_NAME = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, "0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        } else if ((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, "0").equals("2"))) {
            USER_NAME = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, "0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        } else if ((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, "0").equals("3"))) {

            USER_NAME = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, "0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        String delete_msg = MedisensePracticeDB.deleteNotificationContents(NotifyMessageDetailActivity.this, USER_ID, USER_LOGIN_TYPE, VERIFY_ID);
        Log.d(Utils.TAG + " del_msg: ", delete_msg);
        int notifyCount = MedisensePracticeDB.getNotificationCount(NotifyMessageDetailActivity.this, USER_ID, USER_LOGIN_TYPE);
        Log.d(Utils.TAG + " notCount: ", String.valueOf(notifyCount));
        if(notifyCount > 0) {
            DashboardActivity.setPendingNotificationsCount(notifyCount);
        }

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

        _image = (ImageView) findViewById(R.id.notifydetail_image);
        title = (CustomTextViewBold) findViewById(R.id.notify_detail_title);
        _full_description.set((CustomTextView) findViewById(R.id.notify_detail_description));
        _author_profile = (ImageView) findViewById(R.id.notify_author_imag);
        _author_name = (CustomTextViewItalicBold) findViewById(R.id.notify_detail_author);
        _post_date = (CustomTextViewItalic) findViewById(R.id.notify_detail_date);
        post_like = (ImageView) findViewById(R.id.notify_like_img);
        post_share = (ImageView) findViewById(R.id.notify_share);
        post_comment = (ImageView) findViewById(R.id.notify_message);
        post_likes_label = (TextView) findViewById(R.id.notify_likes_label);
        post_comment_label = (TextView) findViewById(R.id.notify_comment_label);
        post_share_label = (TextView) findViewById(R.id.notify_share_label);

        if(POST_ENTRY.equalsIgnoreCase("BLOGPUSH")) {
            DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_BLOGS_IMAGE_URL+String.valueOf(POST_ID)+"/"+POST_IMAGE.trim();
            BLOG_TYPE = "Blog";
            post_likes_label.setText("Likes");
            post_comment_label.setText("Comments");
            post_share_label.setText("Share");
            //   setRequestToServer();
        }
        else  if(POST_ENTRY.equalsIgnoreCase("EVENTPUSH")) {
            DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_OFFERS_EVENTS_URL+String.valueOf(POST_ID)+"/"+POST_IMAGE.trim();
            BLOG_TYPE = "Events";
            post_likes_label.setText("Going");
            post_comment_label.setText("Maybe");
            post_share_label.setText("Can't Go");
            //  setRequestToServer();
        }
        else  if(POST_ENTRY.equalsIgnoreCase("OFFERPUSH")) {
            DOWNLOAD_BLOG_IMAGE_URL = APIClass.DRS_OFFERS_EVENTS_URL+String.valueOf(POST_ID)+"/"+POST_IMAGE.trim();
          /*  post_like.setImageResource(R.drawable.event_going_unselect);
            post_share.setImageResource(R.drawable.event_maybe_unselect);
            post_comment.setImageResource(R.drawable.event_cantgo_unselct);*/
            BLOG_TYPE = "Offers";
            post_like.setVisibility(View.INVISIBLE);
            post_share.setVisibility(View.INVISIBLE);
            post_comment.setVisibility(View.INVISIBLE);
            post_likes_label.setVisibility(View.INVISIBLE);
            post_comment_label.setVisibility(View.INVISIBLE);
            post_share_label.setVisibility(View.INVISIBLE);

        }

        if(POST_IMAGE.equals("") || POST_IMAGE.equals("large_icon")) {
            _image.setVisibility(View.GONE);
        }
        else {
            _image.setVisibility(View.VISIBLE);
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
            //  Picasso.with(getActivity()).load(String.valueOf(url)).into(_image);
            Picasso.with(NotifyMessageDetailActivity.this).load(String.valueOf(url))
                    .placeholder(R.drawable.blogs_empty_img)
                    .error(R.drawable.blogs_empty_img)
                    .fit()
                    .into(_image, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }

        String Tile_Desc = stripHtml(POST_TITLE);
        title.setText(Tile_Desc);
        _author_name.setText(POST_AUTHOR);
        _post_date.setText(POST_DATE);
        String Full_Desc = stripHtml(POST_DESCRIPTION);
        _full_description.get().setText(Full_Desc);

        post_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BLOG_TYPE.equalsIgnoreCase("Events")) {
                    EVENT_TYPE = "1";
                    post_like.setEnabled(false);
                    post_share.setEnabled(true);
                    post_comment.setEnabled(true);
                }
                else  if(BLOG_TYPE.equalsIgnoreCase("Blog")) {
                    EVENT_TYPE = "1";
                    post_like.setEnabled(false);
                    post_share.setEnabled(true);
                    post_comment.setEnabled(true);
                }


            }
        });

        post_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BLOG_TYPE.equalsIgnoreCase("Events")) {
                    EVENT_TYPE = "2";
                    post_share.setEnabled(false);
                    post_like.setEnabled(true);
                    post_comment.setEnabled(true);
                }
                else  if(BLOG_TYPE.equalsIgnoreCase("Blog")) {
                    EVENT_TYPE = "2";
                    post_share.setEnabled(false);
                    post_like.setEnabled(true);
                    post_comment.setEnabled(true);
                }

            }
        });

        post_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BLOG_TYPE.equalsIgnoreCase("Events")) {
                    EVENT_TYPE = "3";
                    post_comment.setEnabled(false);
                    post_share.setEnabled(true);
                    post_like.setEnabled(true);
                }

                else  if(BLOG_TYPE.equalsIgnoreCase("Blog")) {
                    EVENT_TYPE = "3";
                    post_comment.setEnabled(false);
                    post_share.setEnabled(true);
                    post_like.setEnabled(true);
                }

            }
        });

    }

    private String stripHtml(String response) {
        return Html.fromHtml(response).toString();
    }
}
