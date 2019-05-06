package com.medisensehealth.fdccontributor.activities.notifications;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.medisensehealth.fdccontributor.DataModel.NotifyMessages;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.doctors.DoctorsActivity;
import com.medisensehealth.fdccontributor.adapter.notifications.NotifyMessageAdapter;
import com.medisensehealth.fdccontributor.database.MedisensePracticeDB;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SALMA on 11-10-2018.
 */
public class NotificationActivity extends AppCompatActivity {

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE;

    SwipyRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    CustomTextViewBold warning_msg;

    private List<NotifyMessages> messageArrayList;
    private NotifyMessageAdapter mAdapter;
    NotifyMessages messages;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);
            Log.d(Utils.TAG, " ************ NotificationActivity *********** ");
        }

        shareadPreferenceClass = new ShareadPreferenceClass(NotificationActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(NotificationActivity.this);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {

            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        Log.d(Utils.TAG+"Messages: ", Utils.USER_LOGIN_TYPE);
        Log.d(Utils.TAG+"Messages: ", String.valueOf(Utils.USER_LOGIN_ID));

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
        swipeRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.messages_swipeRefreshLayout);
        recyclerView = (RecyclerView)findViewById(R.id.messages_RecyclerView);
        warning_msg = (CustomTextViewBold) findViewById(R.id.messages_warning);
        messageArrayList = new ArrayList<>();
        getMessageNotifications();

        mAdapter = new NotifyMessageAdapter(NotificationActivity.this, messageArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NotificationActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void getMessageNotifications() {

        int notifyCount = MedisensePracticeDB.getNotificationCount(NotificationActivity.this, USER_ID, USER_LOGIN_TYPE);
        if(notifyCount > 0) {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            warning_msg.setVisibility(View.GONE);
            getMessageList();
        }
        else {
            swipeRefreshLayout.setVisibility(View.GONE);
            warning_msg.setVisibility(View.VISIBLE);
        }

    }

    private void getMessageList() {
        messageArrayList = new ArrayList<>();
        Cursor cursor_msg = MedisensePracticeDB.getAllNotification(NotificationActivity.this, USER_ID, USER_LOGIN_TYPE);
        Log.d(Utils.TAG, "cursor_msg: " + String.valueOf(cursor_msg.getCount()));
        Log.d(Utils.TAG, " len: " +String.valueOf(cursor_msg.getCount()));

        if (cursor_msg != null) {
            do {
                int notify_id = cursor_msg.getInt(cursor_msg.getColumnIndex("ID"));
                int post_id = cursor_msg.getInt(cursor_msg.getColumnIndex("POST_ID"));
                String post_image = cursor_msg.getString(cursor_msg.getColumnIndex("POST_IMAGE"));
                String post_title = cursor_msg.getString(cursor_msg.getColumnIndex("POST_TITLE"));
                String post_author = cursor_msg.getString(cursor_msg.getColumnIndex("POST_AUTHOR"));
                String post_date = cursor_msg.getString(cursor_msg.getColumnIndex("POST_DATE"));
                String post_message = cursor_msg.getString(cursor_msg.getColumnIndex("POST_MESSAGE"));
                String post_type = cursor_msg.getString(cursor_msg.getColumnIndex("POST_TYPE"));
                String post_entry = cursor_msg.getString(cursor_msg.getColumnIndex("ENTRY_TYPE"));
                String post_patientID = cursor_msg.getString(cursor_msg.getColumnIndex("PATIENTID"));
                String post_docID = cursor_msg.getString(cursor_msg.getColumnIndex("DOCID"));
                String post_key = cursor_msg.getString(cursor_msg.getColumnIndex("POSTKEY"));

/*                Log.d(Utils.TAG, "notify_id: " + String.valueOf(notify_id));
                Log.d(Utils.TAG, "post_id: " + String.valueOf(post_id));
                Log.d(Utils.TAG, "post_image: " +post_image);
                Log.d(Utils.TAG, "post_title: " +post_title);
                Log.d(Utils.TAG, "post_author: " +post_author);
                Log.d(Utils.TAG, "post_date: " +post_date);
                Log.d(Utils.TAG, "post_message: " +post_message);
                Log.d(Utils.TAG, "post_type: " +post_type);
                Log.d(Utils.TAG, "post_entry: " +post_entry);*/

                messages = new NotifyMessages(notify_id, post_id, post_image, post_title, post_author, post_date, post_message, post_type, post_entry, post_patientID, post_docID, USER_ID, USER_LOGIN_TYPE, post_key );
                messageArrayList.add(messages);
            }while (cursor_msg.moveToNext());
        }
        cursor_msg.close();

        Log.d(Utils.TAG, " Arr len: " +String.valueOf(messageArrayList.size()));
        if(messageArrayList.size() > 0 ) {
            mAdapter = new NotifyMessageAdapter(NotificationActivity.this, messageArrayList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }


    }
}
