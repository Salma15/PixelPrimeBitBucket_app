package com.medisensehealth.fdccontributor.activities.feeds;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.parser.JSONParser;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomEditText;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SALMA on 11-10-2018.
 */
public class AddVideosActivity extends AppCompatActivity {
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE, BLOG_LIST, LOGIN_SPEC_NAME;
    View header_tabs, footertabs;
    String FILTER_TYPE;

    CustomEditText _edt_title, _edt_descriptions, _edt_url;
    MultiAutoCompleteTextView _edt_tags;
    Button submit_btn;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_videos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);
        }

        shareadPreferenceClass = new ShareadPreferenceClass(this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(this);

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            BLOG_LIST = sharedPreferences.getString(HCConstants.PREF_BLOG_LISTS, "");
            LOGIN_SPEC_NAME  = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_SPECIALIZATION_NAME, "SPEC_NAME");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            BLOG_LIST = sharedPreferences.getString(HCConstants.PREF_BLOG_LISTS, "");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            BLOG_LIST = sharedPreferences.getString(HCConstants.PREF_BLOG_LISTS, "");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }


        Log.d(Utils.TAG , " *********** AddVideosActivity ****************");
        Log.d(Utils.TAG +" LoginType: ", Utils.USER_LOGIN_TYPE);
        Log.d(Utils.TAG +" UserId: ", String.valueOf(Utils.USER_LOGIN_ID));

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

        _edt_title = (CustomEditText) findViewById(R.id.blog_vidcreate_title);
        _edt_descriptions = (CustomEditText) findViewById(R.id.blog_vidcreate_description);
        _edt_url = (CustomEditText) findViewById(R.id.blog_vidcreate_url);
        _edt_tags = (MultiAutoCompleteTextView) findViewById(R.id.blog_vidcreate_tags);
        submit_btn = (Button) findViewById(R.id.blog_vidcreate_submit);

        _edt_tags.setText(USER_NAME+","+LOGIN_SPEC_NAME+",");

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String blog_title = _edt_title.getText().toString();
                String blog_description = _edt_descriptions.getText().toString();
                String blog_tags = _edt_tags.getText().toString();
                String blog_url = _edt_url.getText().toString();

                if(blog_title.equals("")) {
                    Toast.makeText(AddVideosActivity.this, "Enter Title", Toast.LENGTH_SHORT).show();
                }
                else  if(blog_description.equals("")) {
                    Toast.makeText(AddVideosActivity.this, "Enter Description", Toast.LENGTH_SHORT).show();
                }
                else  if(blog_url.equals("")) {
                    Toast.makeText(AddVideosActivity.this, "Enter Video URL", Toast.LENGTH_SHORT).show();
                }
                else {
                    submitVideoPostToServer(blog_title, blog_description, blog_tags, blog_url);
                }
            }
        });
    }

    private int submitVideoPostToServer(final String blog_title, final String blog_description, final String blog_tags, final String blog_url) {
        Log.d(Utils.TAG, "***************** Post Blog *************");
        Log.d(Utils.TAG, " blog_title: " +  blog_title);
        Log.d(Utils.TAG, " blog_description: " +  blog_description);
        Log.d(Utils.TAG, " blog_tags: " +  blog_tags);
        Log.d(Utils.TAG, " blog_url: " +  blog_url);

        new AsyncTask<Void, Integer, Boolean>() {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(AddVideosActivity.this);
                progressDialog.setMessage("please wait...");
                progressDialog.show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {
                    JSONObject jsonObject = JSONParser.video_post(blog_title,blog_description, blog_tags,
                            blog_url, USER_ID, USER_LOGIN_TYPE);

                    //   JSONObject jsonObject = JSONParser.uploadAlbumImage("10",num_attachments);
                    if (jsonObject != null)
                        return jsonObject.getString("video_status").equals("success");
                    return true;
                } catch (JSONException e) {
                    Log.i("TAG", "Error : " + e.getLocalizedMessage());
                    return false;
                }

            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (progressDialog != null)
                    progressDialog.dismiss();

                if (aBoolean) {
                    //   Toast.makeText(SignUpActivity.this,"Registered Successfully", Toast.LENGTH_LONG).show();
                    AppUtils.showCustomSuccessMessage(AddVideosActivity.this, "Video Post","Video posted successfully", "OK", null, null);
                    _edt_title.setText("");
                    _edt_descriptions.setText("");
                    _edt_url.setText("");

                    if (sharedPreferences != null) {
                        shareadPreferenceClass.clearFeedsFilterVideoDetailsLists();
                    }
                }
                else
                    // Toast.makeText(SignUpActivity.this, "This user is already registered. \n Please click on Forgot Password to reset your password", Toast.LENGTH_LONG).show();
                    AppUtils.showCustomAlertMessage(AddVideosActivity.this, "Video Post","Failed to post video !!!", "OK", null, null);
            }
        }.execute();

        return 1;
    }
}
