package com.medisensehealth.fdccontributor.activities.feeds;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medisensehealth.fdccontributor.DataModel.BlogsList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.adapter.feeds.BlogSeachAdapter;
import com.medisensehealth.fdccontributor.adapter.feeds.BlogsAdapter;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextViewItalic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by salma on 07/03/18.
 */

public class SearchFeedsActivity extends AppCompatActivity implements View.OnClickListener {

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE, BLOG_LIST;
    View header_tabs, footertabs;

    AutoCompleteTextView _header_seach_autotext;
    ImageView _header_search_cancel;

    List<BlogsList> blogListArraylist = new ArrayList<>();
    BlogsAdapter adapter;
    BlogsList blogs;
    RecyclerView recyclerView_bloglist;
    CustomTextViewItalic no_data;
    Gson gson;
    String FILTER_TYPE;

    List<BlogsList> blogsListArraylist = new ArrayList<>();
    BlogsAdapter blogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle b = getIntent().getExtras();
        if( b != null) {
            String title = b.getString("title");
            FILTER_TYPE = b.getString("FILTER_TYPE", "");
            setTitle(title);

            Log.d(Utils.TAG , "******************* Search Feeds *************");
            Log.d(Utils.TAG + " Type:", String.valueOf(FILTER_TYPE));
        }

        shareadPreferenceClass = new ShareadPreferenceClass(SearchFeedsActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(SearchFeedsActivity.this);

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            BLOG_LIST = sharedPreferences.getString(HCConstants.PREF_BLOG_LISTS, "");

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


        Log.d(Utils.TAG , " *********** Search ****************");
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
        blogsListArraylist = new ArrayList<>();

        _header_seach_autotext = (AutoCompleteTextView) findViewById(R.id.search_autocompletetext);
        _header_seach_autotext.setHint("Search Here...");
        _header_search_cancel = (ImageView) findViewById(R.id.search_cancel);
        _header_search_cancel.setOnClickListener(this);
        _header_search_cancel.setVisibility(View.GONE);


        recyclerView_bloglist = (RecyclerView) findViewById(R.id.search_recycleriew);
        no_data = (CustomTextViewItalic) findViewById(R.id.search_noresult);
        no_data.setVisibility(View.GONE);

        adapter = new BlogsAdapter(this,blogListArraylist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView_bloglist.setLayoutManager(mLayoutManager);
        recyclerView_bloglist.setItemAnimator(new DefaultItemAnimator());
        recyclerView_bloglist.setHasFixedSize(true);
        recyclerView_bloglist.setItemViewCacheSize(20);
        recyclerView_bloglist.setDrawingCacheEnabled(true);
        recyclerView_bloglist.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        Gson gson = new Gson();
        if (BLOG_LIST.equals("")) {

        } else {
            blogListArraylist = gson.fromJson(BLOG_LIST, new TypeToken<List<BlogsList>>() {
            }.getType());
            Log.d(Utils.TAG +" BLOG_LIST: ", String.valueOf(blogListArraylist.size()));

        }

        _header_seach_autotext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    _header_search_cancel.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    String search_result = _header_seach_autotext.getText().toString();
                    collectDoctorsSearchResultFromServer(search_result);
                    return true;
                }
                return false;
            }
        });

        _header_seach_autotext.setThreshold(1);//will start working from first character
        _header_seach_autotext.setTextColor(getResources().getColor(R.color.colorPrimary));
        final BlogSeachAdapter adapter = new BlogSeachAdapter(this, R.layout.fragment_feeds, R.id.lbl_name, blogListArraylist);
        _header_seach_autotext.setAdapter(adapter);

        _header_seach_autotext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.notifyDataSetChanged();
                _header_search_cancel.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_cancel:
                _header_search_cancel.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

//                appointmentArryList = gson.fromJson(APPOINTMENT_LIST, new TypeToken<List<AppointmentList>>() {
//                }.getType());
//                Log.d(Utils.TAG, " size: " + appointmentArryList.size());

                adapter = new BlogsAdapter(this, blogListArraylist);
                recyclerView_bloglist.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void collectDoctorsSearchResultFromServer(final String SEARCH_QUERY) {

        final ProgressDialog progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Searching Result...");
        progressDialog.show();

        blogsListArraylist = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_BLOGS_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, response.toString());
                        if (response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;
                            try {
                                jsonObject = new JSONObject(response);

                                int success = jsonObject.getInt("status");
                                Log.d(Utils.TAG, "status: " + success);

                                if (success == 1) {
                                    JSONArray JAStuff = jsonObject.getJSONArray("blog_details");

                                  /*  * CHECK THE NUMBER OF RECORDS **/
                                    int intStuff = JAStuff.length();
                                    Log.d(Utils.TAG, "intStuff filter: " + intStuff);

                                    if (intStuff != 0)  {
                                        for (int i = 0; i < JAStuff.length(); i++) {
                                            JSONObject JOStuff = JAStuff.getJSONObject(i);
                                            //       Log.d(Utils.TAG, "Res: " + JOStuff.toString());
                                            //    Log.d(Utils.TAG, "Res_detail: " + JOStuff.getString("listing_type"));
                                            blogs = new BlogsList(JOStuff.getInt("post_id"),JOStuff.getString("title"),
                                                    JOStuff.getString("description"),JOStuff.getString("post_image"),
                                                    JOStuff.getString("created_date"),JOStuff.getString("listing_type"),
                                                    JOStuff.getString("username"),JOStuff.getString("userprof"),
                                                    JOStuff.getString("userimg"), JOStuff.getString("contactInfo"),
                                                    JOStuff.getString("attachments"), JOStuff.getInt("company_id"),
                                                    JOStuff.getString("transaction_id"), JOStuff.getString("num_views"),
                                                    JOStuff.getString("video_url"),JOStuff.getString("from_to_date"));
                                            blogsListArraylist.add(blogs);

                                        }

                                        if(blogsListArraylist.size() > 0 ) {
                                            adapter = new BlogsAdapter(SearchFeedsActivity.this, blogsListArraylist);
                                            recyclerView_bloglist.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }else {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_SEARCH_DATA, SEARCH_QUERY);
                map.put(APIClass.KEY_LOGINTYPE, String.valueOf(USER_LOGIN_TYPE));
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(this).getRequestQueue();
        AppController.getInstance(this).addToRequestQueue(stringRequest);

    }

    private String stripHtml(String response) {
        return Html.fromHtml(response).toString();
    }

}
