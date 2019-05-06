package com.medisensehealth.fdccontributor.activities.feeds;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.medisensehealth.fdccontributor.activities.home.HomeFragment;
import com.medisensehealth.fdccontributor.adapter.feeds.BlogsAdapter;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewItalic;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SALMA on 10-10-2018.
 */
public class ViewAllFeedsActivity extends AppCompatActivity implements View.OnClickListener {

    FragmentManager fragManager;
    FragmentTransaction fragTransaction;
    String FILTER_TYPE;

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME, USER_LOGIN_TYPE, FEEDS_FILTER_BLOGLIST, FEEDS_FILTER_EVENTLIST, FEEDS_FILTER_VIDEOLIST, FEEDS_FILTER_JOBLIST;

    ImageView _header_filter, _header_create;
    LinearLayout _header_sub_search_layout;
    CustomTextView _header_sub_text;

    CoordinatorLayout cardiLayout;
    SwipyRefreshLayout swipeRefreshLayout_bloglist;
    RecyclerView recyclerView_bloglist;

    List<BlogsList> blogsListArraylist;
    List<BlogsList> blogsListArraylistUpdated;
    BlogsList blogs;
    BlogsAdapter blogAdapter;
    CustomTextViewItalic blogsEmpty;
    public static int index = -1;
    public static int top = -1;
    LinearLayoutManager mLayoutManager;
    View footertabs;
    Gson gson;
    HomeFragment home_frag;
    String FEEDS_TITLE;
    String change_filter_result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feeds);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle b = getIntent().getExtras();
        if( b != null) {
            String title = b.getString("title");
            FILTER_TYPE = b.getString("FILTER_TYPE", "");
            setTitle(title);

            Log.d(Utils.TAG , "******************* View All Details *************");
            Log.d(Utils.TAG + " Type:", String.valueOf(FILTER_TYPE));
        }

        shareadPreferenceClass = new ShareadPreferenceClass(ViewAllFeedsActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(ViewAllFeedsActivity.this);
        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            FEEDS_FILTER_BLOGLIST = sharedPreferences.getString(HCConstants.PREF_FEEDS_FILTER_BLOGLIST, "");
            FEEDS_FILTER_EVENTLIST = sharedPreferences.getString(HCConstants.PREF_FEEDS_FILTER_EVENTLIST, "");
            FEEDS_FILTER_VIDEOLIST = sharedPreferences.getString(HCConstants.PREF_FEEDS_FILTER_VIDEOLIST, "");
            FEEDS_FILTER_JOBLIST = sharedPreferences.getString(HCConstants.PREF_FEEDS_FILTER_JOBSLIST, "");


            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            FEEDS_FILTER_BLOGLIST = sharedPreferences.getString(HCConstants.PREF_FEEDS_FILTER_BLOGLIST, "");
            FEEDS_FILTER_EVENTLIST = sharedPreferences.getString(HCConstants.PREF_FEEDS_FILTER_EVENTLIST, "");
            FEEDS_FILTER_VIDEOLIST = sharedPreferences.getString(HCConstants.PREF_FEEDS_FILTER_VIDEOLIST, "");
            FEEDS_FILTER_JOBLIST = sharedPreferences.getString(HCConstants.PREF_FEEDS_FILTER_JOBSLIST, "");


            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            FEEDS_FILTER_BLOGLIST = sharedPreferences.getString(HCConstants.PREF_FEEDS_FILTER_BLOGLIST, "");
            FEEDS_FILTER_EVENTLIST = sharedPreferences.getString(HCConstants.PREF_FEEDS_FILTER_EVENTLIST, "");
            FEEDS_FILTER_VIDEOLIST = sharedPreferences.getString(HCConstants.PREF_FEEDS_FILTER_VIDEOLIST, "");
            FEEDS_FILTER_JOBLIST = sharedPreferences.getString(HCConstants.PREF_FEEDS_FILTER_JOBSLIST, "");


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
        blogsListArraylist = new ArrayList<>();

        if(FILTER_TYPE.equalsIgnoreCase("Jobs")) {
            FEEDS_TITLE = "Fellowships";
        }
        else if(FILTER_TYPE.equalsIgnoreCase("Blog")) {
            FEEDS_TITLE = "Blogs";
        }
        else if(FILTER_TYPE.equalsIgnoreCase("Surgical")) {
            FEEDS_TITLE = "Videos";
        }
        else {
            FEEDS_TITLE = FILTER_TYPE;
        }

        // Header Tabs
        _header_filter = (ImageView) findViewById(R.id.header_sub_filter);
        _header_create = (ImageView) findViewById(R.id.header_sub_create);
        //    _header_create.setImageResource(R.mipmap.add_blog_icon1);


        if(FILTER_TYPE.equalsIgnoreCase("Jobs") || (FILTER_TYPE.equalsIgnoreCase("Events"))) {
            _header_create.setVisibility(View.GONE);
        }
        else if(FILTER_TYPE.equalsIgnoreCase("Blog")) {
            _header_create.setVisibility(View.VISIBLE);
        }
        else if(FILTER_TYPE.equalsIgnoreCase("Surgical")) {
            _header_create.setVisibility(View.VISIBLE);
        }
        else {
            _header_create.setVisibility(View.GONE);
        }

        _header_filter.setVisibility(View.VISIBLE);
        _header_filter.setOnClickListener(this);
        _header_create.setOnClickListener(this);
        _header_sub_search_layout = (LinearLayout) findViewById(R.id.header_sub_layout);
        _header_sub_search_layout.setOnClickListener(this);
        _header_sub_text = (CustomTextView)  findViewById(R.id.header_sub_searchtext);
        _header_sub_text.setText("Search "+FEEDS_TITLE+" Here...");


        //  cardiLayout = (CoordinatorLayout) getActivity().findViewById(R.id.feedslist_cardinator);
        swipeRefreshLayout_bloglist = (SwipyRefreshLayout) findViewById(R.id.blog_swipeRefreshLayout);
        recyclerView_bloglist = (RecyclerView)findViewById(R.id.blog_RecyclerView);
        blogsEmpty = (CustomTextViewItalic)findViewById(R.id.feedslist_nofeeds);
        blogsEmpty.setVisibility(View.GONE);

        blogAdapter = new BlogsAdapter(this, blogsListArraylist);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView_bloglist.setLayoutManager(mLayoutManager);
        recyclerView_bloglist.setItemAnimator(new DefaultItemAnimator());
        recyclerView_bloglist.setHasFixedSize(true);
        recyclerView_bloglist.setItemViewCacheSize(20);
        recyclerView_bloglist.setDrawingCacheEnabled(true);

        recyclerView_bloglist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    //  Toast.makeText(getActivity()," Last position reached !!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        swipeRefreshLayout_bloglist.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(direction == SwipyRefreshLayoutDirection.TOP) {
                    refreshItems();
                }
                else  if(direction == SwipyRefreshLayoutDirection.BOTTOM) {
                    getBlogRequestServerType(FILTER_TYPE, "DIRECT");
                    onItemsLoadComplete();
                }
            }
        });

        //  getBlogRequestServerType(FILTER_TYPE);
        if(FILTER_TYPE.equalsIgnoreCase("Blog")) {
            gson = new Gson();
            if (FEEDS_FILTER_BLOGLIST.equals("")) {
                Log.d(Utils.TAG ," BLOG_LIST empty ");
                if (NetworkUtil.getConnectivityStatusString(this).equalsIgnoreCase("enabled")) {
                    getBlogRequestServerType(FILTER_TYPE, "DIRECT");
                } else {
                    AppUtils.showCustomAlertMessage(ViewAllFeedsActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }

            } else {
                blogsListArraylist = gson.fromJson(FEEDS_FILTER_BLOGLIST, new TypeToken<List<BlogsList>>() {
                }.getType());
                Log.d(Utils.TAG +" BLOG_LIST: ", String.valueOf(blogsListArraylist.size()));
                if(blogsListArraylist.size() > 0 ) {
                    blogAdapter = new BlogsAdapter(this, blogsListArraylist);
                    recyclerView_bloglist.setAdapter(blogAdapter);
                    blogAdapter.notifyDataSetChanged();
                }
            }
        }
        else  if(FILTER_TYPE.equalsIgnoreCase("Events")) {
            gson = new Gson();
            if (FEEDS_FILTER_EVENTLIST.equals("")) {
                Log.d(Utils.TAG ," EVENT_LIST empty ");
                if (NetworkUtil.getConnectivityStatusString(this).equalsIgnoreCase("enabled")) {
                    getBlogRequestServerType(FILTER_TYPE, "DIRECT");
                } else {
                    AppUtils.showCustomAlertMessage(this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }

            } else {
                blogsListArraylist = gson.fromJson(FEEDS_FILTER_EVENTLIST, new TypeToken<List<BlogsList>>() {
                }.getType());
                Log.d(Utils.TAG +" EVENT_LIST: ", String.valueOf(blogsListArraylist.size()));
                if(blogsListArraylist.size() > 0 ) {
                    blogAdapter = new BlogsAdapter(this, blogsListArraylist);
                    recyclerView_bloglist.setAdapter(blogAdapter);
                    blogAdapter.notifyDataSetChanged();
                }
            }

        }
        else  if(FILTER_TYPE.equalsIgnoreCase("Surgical")) {
            gson = new Gson();
            if (FEEDS_FILTER_VIDEOLIST.equals("")) {
                Log.d(Utils.TAG ," VID_LIST empty ");
                if (NetworkUtil.getConnectivityStatusString(this).equalsIgnoreCase("enabled")) {
                    getBlogRequestServerType(FILTER_TYPE, "DIRECT");
                } else {
                    AppUtils.showCustomAlertMessage(this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }

            } else {
                blogsListArraylist = gson.fromJson(FEEDS_FILTER_VIDEOLIST, new TypeToken<List<BlogsList>>() {
                }.getType());
                Log.d(Utils.TAG +" VID_LIST: ", String.valueOf(blogsListArraylist.size()));
                if(blogsListArraylist.size() > 0 ) {
                    blogAdapter = new BlogsAdapter(this, blogsListArraylist);
                    recyclerView_bloglist.setAdapter(blogAdapter);
                    blogAdapter.notifyDataSetChanged();
                }
            }
        }
        else  if(FILTER_TYPE.equalsIgnoreCase("Jobs")) {
            gson = new Gson();
            if (FEEDS_FILTER_JOBLIST.equals("")) {
                Log.d(Utils.TAG ," JOB_LIST empty ");
                if (NetworkUtil.getConnectivityStatusString(this).equalsIgnoreCase("enabled")) {
                    getBlogRequestServerType(FILTER_TYPE, "DIRECT");
                } else {
                    AppUtils.showCustomAlertMessage(this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }

            } else {
                blogsListArraylist = gson.fromJson(FEEDS_FILTER_JOBLIST, new TypeToken<List<BlogsList>>() {
                }.getType());
                Log.d(Utils.TAG +" JOB_LIST: ", String.valueOf(blogsListArraylist.size()));
                if(blogsListArraylist.size() > 0 ) {
                    blogAdapter = new BlogsAdapter(this, blogsListArraylist);
                    recyclerView_bloglist.setAdapter(blogAdapter);
                    blogAdapter.notifyDataSetChanged();
                }
            }
        }
        else {
            getBlogRequestServerType(FILTER_TYPE, "DIRECT");
        }
    }

    void refreshItems() {
//        ShowCountAsyncTask myTask = new ShowCountAsyncTask(getActivity());
//        myTask.execute();
        getBlogRequestServerType(FILTER_TYPE, "DIRECT");
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        swipeRefreshLayout_bloglist.setRefreshing(false);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        //read current recyclerview position
        index = mLayoutManager.findFirstVisibleItemPosition();
        View v = recyclerView_bloglist.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - recyclerView_bloglist.getPaddingTop());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //set recyclerview position
        if(index != -1)
        {
            mLayoutManager.scrollToPositionWithOffset( index, top);
        }
    }

    private void getBlogRequestServerType(final String change_filter_result, final String entery_type) {

        final ProgressDialog progressDialog3 = new ProgressDialog(ViewAllFeedsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog3.setIndeterminate(true);
        progressDialog3.setMessage("Loading...");
        progressDialog3.show();

        blogsListArraylist.clear();
        blogsListArraylist = new ArrayList<>();
        //  blogsListArraylistUpdated = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_BLOGS_FILTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "filter res:"+ response.toString());

                        if (response != null) {
                            //  String response_filter =  stripHtml(response);
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
                                        recyclerView_bloglist.setVisibility(View.VISIBLE);
                                        blogsEmpty.setVisibility(View.GONE);
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
                                            blogAdapter = new BlogsAdapter(ViewAllFeedsActivity.this, blogsListArraylist);
                                            recyclerView_bloglist.setAdapter(blogAdapter);
                                            blogAdapter.notifyDataSetChanged();

                                            if(entery_type.equalsIgnoreCase("ENTRY_FILTER")) {

                                            }
                                            else {
                                                if(FILTER_TYPE.equalsIgnoreCase("Blog")) {
                                                    Gson gson = new Gson();
                                                    String json_data = gson.toJson(blogsListArraylist);
                                                    if (sharedPreferences != null) {
                                                        shareadPreferenceClass.clearFeedsFilterBlogDetailsLists();
                                                        shareadPreferenceClass.setFeedsFilterBlogDetailsList(json_data);
                                                    }

                                                }
                                                else  if(FILTER_TYPE.equalsIgnoreCase("Events")) {
                                                    Gson gson = new Gson();
                                                    String json_data = gson.toJson(blogsListArraylist);
                                                    if (sharedPreferences != null) {
                                                        shareadPreferenceClass.clearFeedsFilterEventsDetailsLists();
                                                        shareadPreferenceClass.setFeedsFilterEventsDetailsList(json_data);
                                                    }

                                                }
                                                else  if(FILTER_TYPE.equalsIgnoreCase("Surgical")) {
                                                    Gson gson = new Gson();
                                                    String json_data = gson.toJson(blogsListArraylist);
                                                    if (sharedPreferences != null) {
                                                        shareadPreferenceClass.clearFeedsFilterVideoDetailsLists();
                                                        shareadPreferenceClass.setFeedsFilterVideoDetailsList(json_data);
                                                    }
                                                }
                                                else  if(FILTER_TYPE.equalsIgnoreCase("Jobs")) {
                                                    Gson gson = new Gson();
                                                    String json_data = gson.toJson(blogsListArraylist);
                                                    if (sharedPreferences != null) {
                                                        shareadPreferenceClass.clearFeedsFilterJobsDetailsLists();
                                                        shareadPreferenceClass.setFeedsFilterJobsDetailsList(json_data);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        blogsListArraylist = new ArrayList<>();
                                        recyclerView_bloglist.setVisibility(View.GONE);
                                        blogsEmpty.setVisibility(View.VISIBLE);

                                        if(entery_type.equalsIgnoreCase("ENTRY_FILTER")) {

                                        }
                                        else {
                                            if(FILTER_TYPE.equalsIgnoreCase("Blog")) {
                                                Gson gson = new Gson();
                                                String json_data = gson.toJson(blogsListArraylist);
                                                if (sharedPreferences != null) {
                                                    shareadPreferenceClass.clearFeedsFilterBlogDetailsLists();
                                                    shareadPreferenceClass.setFeedsFilterBlogDetailsList(json_data);
                                                }

                                            }
                                            else  if(FILTER_TYPE.equalsIgnoreCase("Events")) {
                                                Gson gson = new Gson();
                                                String json_data = gson.toJson(blogsListArraylist);
                                                if (sharedPreferences != null) {
                                                    shareadPreferenceClass.clearFeedsFilterEventsDetailsLists();
                                                    shareadPreferenceClass.setFeedsFilterEventsDetailsList(json_data);
                                                }

                                            }
                                            else  if(FILTER_TYPE.equalsIgnoreCase("Surgical")) {
                                                Gson gson = new Gson();
                                                String json_data = gson.toJson(blogsListArraylist);
                                                if (sharedPreferences != null) {
                                                    shareadPreferenceClass.clearFeedsFilterVideoDetailsLists();
                                                    shareadPreferenceClass.setFeedsFilterVideoDetailsList(json_data);
                                                }
                                            }
                                            else  if(FILTER_TYPE.equalsIgnoreCase("Jobs")) {
                                                Gson gson = new Gson();
                                                String json_data = gson.toJson(blogsListArraylist);
                                                if (sharedPreferences != null) {
                                                    shareadPreferenceClass.clearFeedsFilterJobsDetailsLists();
                                                    shareadPreferenceClass.setFeedsFilterJobsDetailsList(json_data);
                                                }
                                            }
                                        }
                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog3.dismiss();
                            }
                        }
                        else {
                            progressDialog3.dismiss();
                        }

                        progressDialog3.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Utils.TAG, "on error");
                        Log.d(Utils.TAG, error.toString());
                        progressDialog3.dismiss();
                        //     Toast.makeText(getActivity(), HCConstants.INTERNET+ "\n"+HCConstants.INTERNET_CHECK, Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                map.put(APIClass.KEY_LOGINTYPE, Utils.USER_LOGIN_TYPE);
                map.put(APIClass.KEY_USERID, String.valueOf(Utils.USER_LOGIN_ID));
                map.put(APIClass.KEY_BLOG_TYPE, change_filter_result);
                map.put(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(ViewAllFeedsActivity.this).
                getRequestQueue();
        AppController.getInstance(ViewAllFeedsActivity.this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_sub_filter:
                filterBlogsLits();
                break;
            case R.id.header_sub_layout:

                Bundle bundle = new Bundle();
                bundle.putString("FILTER_TYPE", FILTER_TYPE);

                Intent i4 = new Intent(ViewAllFeedsActivity.this, SearchFeedsActivity.class);
                i4.putExtra("title","Search");
                i4.putExtra("FILTER_TYPE", FILTER_TYPE);
                startActivity(i4);

            /*    SearchBlogsFragment searchBlogs =  new SearchBlogsFragment();
                fragTransaction = fragManager.beginTransaction();
                searchBlogs.setArguments(bundle);
                fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                fragTransaction.replace(R.id.dashboard_fragment,searchBlogs,"SEARCH_BLOGS");
                fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                // fragTransaction.addToBackStack("SEARCH_APPOINT");
                fragTransaction.commit();*/
                break;
            case R.id.header_sub_create:

                createNewPostUpload();

                break;
        }
    }

    private void createNewPostUpload() {
        if(FILTER_TYPE.equalsIgnoreCase("Blog")) {

            Bundle bundle1 = new Bundle();
            bundle1.putString("FILTER_TYPE", FILTER_TYPE);

            Intent i1 = new Intent(ViewAllFeedsActivity.this, AddBlogActivity.class);
            i1.putExtra("title","New Blog");
            i1.putExtra("FILTER_TYPE", FILTER_TYPE);
            startActivity(i1);

            /*CreateFeedsFragment createFeeds =  new CreateFeedsFragment();
            fragTransaction = fragManager.beginTransaction();
            createFeeds.setArguments(bundle1);
            fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
            fragTransaction.replace(R.id.dashboard_fragment,createFeeds,"CREATE_FEEDS");
            fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // fragTransaction.addToBackStack("CREATE_APPOINT");
            fragTransaction.commit();*/

        }
        else  if(FILTER_TYPE.equalsIgnoreCase("Events")) {
            Toast.makeText(ViewAllFeedsActivity.this, "Upload Later !!!", Toast.LENGTH_SHORT).show();
        }
        else  if(FILTER_TYPE.equalsIgnoreCase("Surgical")) {
            Bundle bundle1 = new Bundle();
            bundle1.putString("FILTER_TYPE", FILTER_TYPE);


            Intent i1 = new Intent(ViewAllFeedsActivity.this, AddVideosActivity.class);
            i1.putExtra("title","New Blog");
            i1.putExtra("FILTER_TYPE", FILTER_TYPE);
            startActivity(i1);

            /*CreateVideosFragment createVideos =  new CreateVideosFragment();
            fragTransaction = fragManager.beginTransaction();
            createVideos.setArguments(bundle1);
            fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
            fragTransaction.replace(R.id.dashboard_fragment,createVideos,"CREATE_VIDEOS");
            fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // fragTransaction.addToBackStack("CREATE_VIDEOS");
            fragTransaction.commit();*/
        }
        else  if(FILTER_TYPE.equalsIgnoreCase("Jobs") || FILTER_TYPE.equalsIgnoreCase("Fellowships")) {
            Toast.makeText(ViewAllFeedsActivity.this, "Upload Later !!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void filterBlogsLits() {
        final CharSequence str[] = {"Blogs","Events","Fellowships", "Videos","All"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewAllFeedsActivity.this, R.style.CustomDialog);
        builder.setTitle("Filter By");
        // builder.setCancelable(true);
        int position = -1;
        Dialog dialog = builder.create();
        builder.setSingleChoiceItems(str, position, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //  Toast.makeText(getActivity(), String.valueOf(str[whichButton]), Toast.LENGTH_SHORT).show();
                change_filter_result = String.valueOf(str[whichButton]);
                if (change_filter_result.equals("")) {
                    Toast.makeText(ViewAllFeedsActivity.this, "Choose filter to update !!!", Toast.LENGTH_LONG).show();
                } else {

                    if(change_filter_result.equalsIgnoreCase("Videos")) {
                        change_filter_result = "Surgical";
                    }
                    else if(change_filter_result.equalsIgnoreCase("Fellowships")) {
                        change_filter_result = "Jobs";
                    }
                    else if(change_filter_result.equalsIgnoreCase("Blogs")) {
                        change_filter_result = "Blog";
                    }

                    getBlogRequestServerType(change_filter_result, "ENTRY_FILTER");
                    dialog.cancel();
                }

            }
        });
        builder.show();
    }
}
