package com.medisensehealth.fdccontributor.activities.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.medisensehealth.fdccontributor.activities.appointments.AppointmentsNewActivity;
import com.medisensehealth.fdccontributor.activities.doctors.DoctorsActivity;
import com.medisensehealth.fdccontributor.activities.emr.EMRActivity;
import com.medisensehealth.fdccontributor.activities.feeds.ViewAllFeedsActivity;
import com.medisensehealth.fdccontributor.activities.mypatientOphthalmology.RefreshOphthalEMRActivity;
import com.medisensehealth.fdccontributor.activities.patients.PatientsActivity;
import com.medisensehealth.fdccontributor.activities.waitingRoom.WaitingRoomActivity;
import com.medisensehealth.fdccontributor.adapter.home.HomeAdapter;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;
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
 * Created by medisense on 16/01/18.
 */

public class HomeFragment  extends Fragment implements View.OnClickListener {
    Toolbar toolbar;
    FragmentManager fragManager;
    FragmentTransaction fragTransaction;

    private List<BlogsList> blogsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HomeAdapter mAdapter;
    ProgressDialog mProgressDialog;
    LinearLayout blogs_layout, events_layouts, videos_layout, fellowship_layout, appointment_layout, mypatients_layout, casesreceived_layout, connections_layout, waitingroom_layout;
    CustomTextView view_more_btn;

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID, SPECIALIZATION_GROUP_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE, FILTER_TYPE, BLOG_LIST, APPOINTMENT_COUNT;
    Gson gson;

    LinearLayout footer_home, footer_favourites, footer_quiz, footer_account, footer_help, footer_connections;
    ImageView footer_home_img, footer_favourites_img, footer_quiz_img, footer_account_img, footer_help_img, footer_connections_img;
    CustomTextViewBold footer_home_txt, footer_favourites_txt, footer_quiz_txt, footer_account_txt, footer_help_txt, footer_connections_text;
    View footer_tabs;
    SwipyRefreshLayout swipeRefreshLayout_bloglist;
    LinearLayoutManager mLayoutManager;
    CustomTextViewBold blogsEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, parentViewGroup, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        blogsList = new ArrayList<>();

        footer_tabs = (View) getActivity().findViewById(R.id.footer);
        footer_tabs.setVisibility(View.VISIBLE);

        fragManager = getActivity().getSupportFragmentManager();
        shareadPreferenceClass = new ShareadPreferenceClass(getActivity());
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(getActivity());

        if(Utils.USER_LOGIN_TYPE.equals("1")) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            FILTER_TYPE = sharedPreferences.getString(HCConstants.PREF_HOME_FEEDS_TYPE, "");
            BLOG_LIST = sharedPreferences.getString(HCConstants.PREF_BLOG_LISTS, "");
            SPECIALIZATION_GROUP_ID = sharedPreferences.getInt(HCConstants.PREF_SPEC_GROUP_ID,0);

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if(Utils.USER_LOGIN_TYPE.equals("2")) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            FILTER_TYPE = sharedPreferences.getString(HCConstants.PREF_HOME_FEEDS_TYPE, "");
            BLOG_LIST = sharedPreferences.getString(HCConstants.PREF_BLOG_LISTS, "");
            SPECIALIZATION_GROUP_ID = sharedPreferences.getInt(HCConstants.PREF_SPEC_GROUP_ID,0);

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if(Utils.USER_LOGIN_TYPE.equals("3")) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            FILTER_TYPE = sharedPreferences.getString(HCConstants.PREF_HOME_FEEDS_TYPE, "");
            BLOG_LIST = sharedPreferences.getString(HCConstants.PREF_BLOG_LISTS, "");
            SPECIALIZATION_GROUP_ID = sharedPreferences.getInt(HCConstants.PREF_SPEC_GROUP_ID,0);

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;

        }

        Log.d(Utils.TAG , " *********** Home Fragment ****************");
        Log.d(Utils.TAG +" loginType: ", Utils.USER_LOGIN_TYPE);
        Log.d(Utils.TAG +" UserId: ", String.valueOf(Utils.USER_LOGIN_ID));
        Log.d(Utils.TAG +" Type: ", String.valueOf(FILTER_TYPE));
        Log.d(Utils.TAG +" SPECGROUP: ", String.valueOf(SPECIALIZATION_GROUP_ID));

        initToolBar();
        initialViews(savedInstanceState);
    }

    private void initToolBar() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getText(R.string.app_name));
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(null);
    }


    private void initialViews(Bundle savedInstanceState) {
        blogsList = new ArrayList<>();

        footer_tabs = (View) getActivity().findViewById(R.id.footer);
        footer_tabs.setVisibility(View.VISIBLE);
        // Initialize footer views
        footer_home = (LinearLayout) getActivity().findViewById(R.id.footer_home_layout);
        footer_favourites = (LinearLayout) getActivity().findViewById(R.id.footer_favourites_layout);
        footer_quiz = (LinearLayout) getActivity().findViewById(R.id.footer_quiz_layout);
        footer_account = (LinearLayout) getActivity().findViewById(R.id.footer_myaccount_layout);
        footer_help = (LinearLayout) getActivity().findViewById(R.id.footer_help_layout);
        footer_connections = (LinearLayout) getActivity().findViewById(R.id.footer_connections_layout);

        footer_home_img = (ImageView) getActivity().findViewById(R.id.footer_home_img);
        footer_favourites_img = (ImageView) getActivity().findViewById(R.id.footer_favourites_img);
        footer_quiz_img = (ImageView) getActivity().findViewById(R.id.footer_quiz_img);
        footer_account_img = (ImageView) getActivity().findViewById(R.id.footer_myaccount_img);
        footer_help_img = (ImageView) getActivity().findViewById(R.id.footer_help_img);
        footer_connections_img = (ImageView) getActivity().findViewById(R.id.footer_connections_img);

        footer_home_txt = (CustomTextViewBold) getActivity().findViewById(R.id.footer_home_text);
        footer_favourites_txt = (CustomTextViewBold) getActivity().findViewById(R.id.footer_favourites_text);
        footer_quiz_txt = (CustomTextViewBold) getActivity().findViewById(R.id.footer_quiz_text);
        footer_account_txt = (CustomTextViewBold) getActivity().findViewById(R.id.footer_myaccount_text);
        footer_help_txt = (CustomTextViewBold) getActivity().findViewById(R.id.footer_help_text);
        footer_connections_text = (CustomTextViewBold) getActivity().findViewById(R.id.footer_connections_text);

        footer_home_img.setImageResource(R.mipmap.home_icon_select);
        footer_favourites_img.setImageResource(R.mipmap.favourite_icon_unselct);
        footer_quiz_img.setImageResource(R.mipmap.quiz_icon_unselect);
        footer_account_img.setImageResource(R.mipmap.my_account_unselect);
        footer_help_img.setImageResource(R.mipmap.help_icon_unselect);
        footer_connections_img.setImageResource(R.mipmap.connections_unselect);

        footer_home_txt.setTextColor(getResources().getColor(R.color.textPrimary));
        footer_favourites_txt.setTextColor(getResources().getColor(R.color.textColor));
        footer_quiz_txt.setTextColor(getResources().getColor(R.color.textColor));
        footer_account_txt.setTextColor(getResources().getColor(R.color.textColor));
        footer_help_txt.setTextColor(getResources().getColor(R.color.textColor));
        footer_connections_text.setTextColor(getResources().getColor(R.color.textColor));

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.home_recyclerview);
        view_more_btn = (CustomTextView)  getActivity().findViewById(R.id.home_viewmore);
        swipeRefreshLayout_bloglist = (SwipyRefreshLayout) getActivity().findViewById(R.id.feeds_swipeRefreshLayout);
        blogsEmpty = (CustomTextViewBold) getActivity().findViewById(R.id.list_nofeeds);

        blogs_layout = (LinearLayout) getActivity().findViewById(R.id.home_blogs);
        events_layouts = (LinearLayout) getActivity().findViewById(R.id.home_events);
        videos_layout = (LinearLayout) getActivity().findViewById(R.id.home_videos);
        fellowship_layout = (LinearLayout) getActivity().findViewById(R.id.home_fellowships);
        appointment_layout = (LinearLayout) getActivity().findViewById(R.id.home_appointments);
        mypatients_layout = (LinearLayout) getActivity().findViewById(R.id.home_mypatients);
        casesreceived_layout = (LinearLayout) getActivity().findViewById(R.id.home_cases_reveived);
        connections_layout = (LinearLayout) getActivity().findViewById(R.id.home_connections);
        waitingroom_layout = (LinearLayout) getActivity().findViewById(R.id.home_waitingroom);
        blogs_layout.setOnClickListener(this);
        events_layouts.setOnClickListener(this);
        videos_layout.setOnClickListener(this);
        fellowship_layout.setOnClickListener(this);
        appointment_layout.setOnClickListener(this);
        mypatients_layout.setOnClickListener(this);
        casesreceived_layout.setOnClickListener(this);
        connections_layout.setOnClickListener(this);
        waitingroom_layout.setOnClickListener(this);

        mAdapter = new HomeAdapter(getActivity(), blogsList);
       // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
       /* mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);*/
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);

        gson = new Gson();
        if (BLOG_LIST.equals("")) {
            Log.d(Utils.TAG ," BLOG_LIST empty ");
            if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
                setRequestFromServer();
            } else {
                AppUtils.showCustomAlertMessage(getActivity(), HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            blogsList = gson.fromJson(BLOG_LIST, new TypeToken<List<BlogsList>>() {
            }.getType());
            Log.d(Utils.TAG +" BLOG_LIST: ", String.valueOf(blogsList.size()));
            if(blogsList.size() > 0 ) {
                mAdapter = new HomeAdapter(getActivity(), blogsList);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        }


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                   // Toast.makeText(getActivity()," Last position reached !!!",Toast.LENGTH_SHORT).show();

                    if(blogsList.size() >10) {
                        Log.d(Utils.TAG, " size > 10 " + blogsList.size());
                        view_more_btn.setVisibility(View.VISIBLE);
                    }
                    else {
                        view_more_btn.setVisibility(View.GONE);
                    }
                }
            }
        });

        swipeRefreshLayout_bloglist.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                //Toast.makeText(getActivity(), "Refresh triggered at "+ (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"), Toast.LENGTH_SHORT ).show();

                if(direction == SwipyRefreshLayoutDirection.TOP) {
                    refreshItems();
                }
                else if(direction == SwipyRefreshLayoutDirection.BOTTOM) {
                    setRequestFromServerAll();
                    onItemsLoadComplete();
                }
            }
        });

      /*  if((FILTER_TYPE.equals("") || FILTER_TYPE == null)) {
            prepareFeedsData("Blog");  // {"Blog","Events","Jobs", "Videos","All"}
        }
        else {
            prepareFeedsData(FILTER_TYPE);  // {"Blog","Events","Jobs", "Videos","All"}
        }*/

        view_more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {

                    Intent i1 = new Intent(getActivity(), ViewAllFeedsActivity.class);
                    i1.putExtra("title","Blogs");
                    i1.putExtra("FILTER_TYPE", FILTER_TYPE);
                    startActivity(i1);

                  /*   Bundle bundle = new Bundle();
                    bundle.putString("FILTER_TYPE", FILTER_TYPE);
                    ViewAllFeedsFragment blogsFrag = new ViewAllFeedsFragment();
                    fragTransaction = fragManager.beginTransaction();
                    blogsFrag.setArguments(bundle);
                    fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragTransaction.replace(R.id.dashboard_fragment,blogsFrag,"ALL_FEEDS");
                    fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragTransaction.addToBackStack("ALL_FEEDS");
                    fragTransaction.commit();*/

                }else {
                    AppUtils.showCustomAlertMessage(getActivity(), HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
            }
        });
    }

    void refreshItems() {
        setRequestFromServer();
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        swipeRefreshLayout_bloglist.setRefreshing(false);
    }

    private void prepareFeedsData(final String FILTER_TYPES) {
        view_more_btn.setVisibility(View.GONE);
        blogsList = new ArrayList<>();

        //Set the values
        if (sharedPreferences != null) {
            shareadPreferenceClass.clearHomeFeedsTypes();
            shareadPreferenceClass.setHomeFeedsTypes(FILTER_TYPES);
        }

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading "+FILTER_TYPES+"....");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
       // mProgressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_BLOGS_FILTER, onPostsLoaded, onPostsError) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                map.put(APIClass.KEY_LOGINTYPE, Utils.USER_LOGIN_TYPE);
                map.put(APIClass.KEY_USERID, String.valueOf(Utils.USER_LOGIN_ID));
                map.put(APIClass.KEY_BLOG_TYPE, FILTER_TYPES);
                map.put(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(getActivity()).getRequestQueue();
        AppController.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
          //  mProgressDialog.dismiss();
            Log.d(Utils.TAG, " home: "+ response);
            if (response != null) {
                GetJsonArrayResponse(response);
            }
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(Utils.TAG, " homeErr: "+ error.toString());
         //   mProgressDialog.dismiss();
        }
    };

    private void GetJsonArrayResponse(String response) {

        if (mProgressDialog != null || mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (response != null) {
            JSONObject jsonObject = null;
            JSONArray jsonArray, jsonArray1;
            try {
                jsonObject = new JSONObject(response);

                int success = jsonObject.getInt("status");
                Log.d(Utils.TAG, "status: " + success);

                if (success == 1) {
                    JSONArray JAStuff = jsonObject.getJSONArray("blog_details");
                    int intStuff = JAStuff.length();
                    Log.d(Utils.TAG, "intStuff filter: " + intStuff);
                    if (intStuff != 0)  {
                        recyclerView.setVisibility(View.VISIBLE);
                       // blogsEmpty.setVisibility(View.GONE);
                        for (int i = 0; i < JAStuff.length(); i++) {
                            JSONObject JOStuff = JAStuff.getJSONObject(i);

                            if(i <= 10 ) {
                                blogsList.add(new BlogsList(JOStuff.getInt("post_id"),JOStuff.getString("title"),
                                        JOStuff.getString("description"),JOStuff.getString("post_image"),
                                        JOStuff.getString("created_date"),JOStuff.getString("listing_type"),
                                        JOStuff.getString("username"),JOStuff.getString("userprof"),
                                        JOStuff.getString("userimg"), JOStuff.getString("contactInfo"),
                                        JOStuff.getString("attachments"), JOStuff.getInt("company_id"),
                                        JOStuff.getString("transaction_id"), JOStuff.getString("num_views"),
                                        JOStuff.getString("video_url"),JOStuff.getString("from_to_date")));
                            }

                        }

                        if(blogsList.size() > 0 ) {
                            mAdapter = new HomeAdapter(getActivity(), blogsList);
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

                            Gson gson = new Gson();
                            String json_data = gson.toJson(blogsList);
                            if (sharedPreferences != null) {
                                shareadPreferenceClass.clearBlogDetailsLists();
                                shareadPreferenceClass.setBlogDetailsList(json_data);
                            }

                        }

                    }
                    else {
                        recyclerView.setVisibility(View.GONE);
                      //  blogsEmpty.setVisibility(View.VISIBLE);

                        blogsList = new ArrayList<>();
                        Gson gson = new Gson();
                        String json_data = gson.toJson(blogsList);
                        if (sharedPreferences != null) {
                            shareadPreferenceClass.clearBlogDetailsLists();
                            shareadPreferenceClass.setBlogDetailsList(json_data);
                        }
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_blogs:
               // prepareFeedsData("Blog");
                FILTER_TYPE = "Blog";

                Intent i1 = new Intent(getActivity(), ViewAllFeedsActivity.class);
                i1.putExtra("title","Blogs");
                i1.putExtra("FILTER_TYPE", FILTER_TYPE);
                startActivity(i1);


               /* Bundle bundle1 = new Bundle();
                bundle1.putString("FILTER_TYPE", FILTER_TYPE);
                ViewAllFeedsFragment blogsFrag = new ViewAllFeedsFragment();
                fragTransaction = fragManager.beginTransaction();
                blogsFrag.setArguments(bundle1);
                fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                fragTransaction.replace(R.id.dashboard_fragment,blogsFrag,"ALL_BLOGS");
                fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragTransaction.addToBackStack("ALL_BLOGS");
                fragTransaction.commit();*/
                break;
            case R.id.home_events:

                FILTER_TYPE = "Events";
                Bundle bundle2 = new Bundle();
                bundle2.putString("FILTER_TYPE", FILTER_TYPE);

                Intent i2 = new Intent(getActivity(), ViewAllFeedsActivity.class);
                i2.putExtra("title","Events");
                i2.putExtra("FILTER_TYPE", FILTER_TYPE);
                startActivity(i2);

               // prepareFeedsData("Events");
              /*  FILTER_TYPE = "Events";
                Bundle bundle2 = new Bundle();
                bundle2.putString("FILTER_TYPE", FILTER_TYPE);
                ViewAllFeedsFragment eventsFrag = new ViewAllFeedsFragment();
                fragTransaction = fragManager.beginTransaction();
                eventsFrag.setArguments(bundle2);
                fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                fragTransaction.replace(R.id.dashboard_fragment,eventsFrag,"ALL_EVENTS");
                fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragTransaction.addToBackStack("ALL_EVENTS");
                fragTransaction.commit();*/
                break;
            case R.id.home_videos:

                FILTER_TYPE = "Surgical";
                Bundle bundle3 = new Bundle();
                bundle3.putString("FILTER_TYPE", FILTER_TYPE);

                Intent i3 = new Intent(getActivity(), ViewAllFeedsActivity.class);
                i3.putExtra("title","Videos");
                i3.putExtra("FILTER_TYPE", FILTER_TYPE);
                startActivity(i3);

              //  prepareFeedsData("Surgical");
              /*  FILTER_TYPE = "Surgical";
                Bundle bundle3 = new Bundle();
                bundle3.putString("FILTER_TYPE", FILTER_TYPE);
                ViewAllFeedsFragment videoFrag = new ViewAllFeedsFragment();
                fragTransaction = fragManager.beginTransaction();
                videoFrag.setArguments(bundle3);
                fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                fragTransaction.replace(R.id.dashboard_fragment,videoFrag,"ALL_VIDEOS");
                fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragTransaction.addToBackStack("ALL_VIDEOS");
                fragTransaction.commit();*/
                break;
            case R.id.home_fellowships:

                FILTER_TYPE = "Jobs";
                Bundle bundle4 = new Bundle();
                bundle4.putString("FILTER_TYPE", FILTER_TYPE);

                Intent i4 = new Intent(getActivity(), ViewAllFeedsActivity.class);
                i4.putExtra("title","Fellowships");
                i4.putExtra("FILTER_TYPE", FILTER_TYPE);
                startActivity(i4);

              //  prepareFeedsData("Jobs");
               /* FILTER_TYPE = "Jobs";
                Bundle bundle4 = new Bundle();
                bundle4.putString("FILTER_TYPE", FILTER_TYPE);
                ViewAllFeedsFragment jobFrag = new ViewAllFeedsFragment();
                fragTransaction = fragManager.beginTransaction();
                jobFrag.setArguments(bundle4);
                fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                fragTransaction.replace(R.id.dashboard_fragment,jobFrag,"ALL_JOBS");
                fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragTransaction.addToBackStack("ALL_JOBS");
                fragTransaction.commit();*/
                break;
            case R.id.home_appointments:
                if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
                 /*   AppointmentFragment appointmentFrag = new AppointmentFragment();
                    fragTransaction = fragManager.beginTransaction();
                    fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragTransaction.replace(R.id.dashboard_fragment,appointmentFrag,"APPOINTMENTS");
                    fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragTransaction.addToBackStack("APPOINTMENTS");
                    fragTransaction.commit();*/
                    checkEMRInitialLoad(2);
                } else {
                    AppUtils.showCustomAlertMessage(getActivity(), HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);

                }
                break;
            case R.id.home_mypatients:
              //  prepareFeedsData(FILTER_TYPE);
                if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
                   /* MyPatientsFragment mypatientsFrag = new MyPatientsFragment();
                    fragTransaction = fragManager.beginTransaction();
                    fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragTransaction.replace(R.id.dashboard_fragment,mypatientsFrag,"MY_PATIENTS");
                    fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragTransaction.addToBackStack("MY_PATIENTS");
                    fragTransaction.commit();*/

                    checkEMRInitialLoad(3);
                } else {
                    AppUtils.showCustomAlertMessage(getActivity(), HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);

                }
                break;
            case R.id.home_cases_reveived:
                // prepareFeedsData(FILTER_TYPE);
                if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
                   /* PatientListFragment patientFrag = new PatientListFragment();
                    fragTransaction = fragManager.beginTransaction();
                    fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragTransaction.replace(R.id.dashboard_fragment,patientFrag,"PATIENT_LIST");
                    fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragTransaction.addToBackStack("PATIENT_LIST");
                    fragTransaction.commit();*/

                    Intent i6 = new Intent(getActivity(), PatientsActivity.class);
                    i6.putExtra("title","Cases Received");
                    startActivity(i6);

                } else {
                    AppUtils.showCustomAlertMessage(getActivity(), HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
                break;
            case R.id.home_connections:
             //  prepareFeedsData(FILTER_TYPE);
                if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
                   /* Bundle bundle = new Bundle();
                    bundle.putString("DOCTOR_TYPE", "1");       // 1 for UNIVERSAL doctors
                    DoctorsFragment doctorsFrag = new DoctorsFragment();
                    fragTransaction = fragManager.beginTransaction();
                    doctorsFrag.setArguments(bundle);
                    fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragTransaction.replace(R.id.dashboard_fragment,doctorsFrag,"DOCTORS");
                    fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragTransaction.addToBackStack("DOCTORS");
                    fragTransaction.commit();*/

                    Intent i7 = new Intent(getActivity(), DoctorsActivity.class);
                    i7.putExtra("title","Connections");
                    i7.putExtra("DOCTOR_TYPE", "1");
                    startActivity(i7);

                } else {
                    AppUtils.showCustomAlertMessage(getActivity(), HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);

                }
                break;
            case R.id.home_waitingroom:
                if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
                    checkEMRInitialLoad(1);
                } else {
                    AppUtils.showCustomAlertMessage(getActivity(), HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);

                }
                break;
        }
    }

    private void setRequestFromServer() {

        final ProgressDialog progressDialog2 = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog2.setIndeterminate(true);
        progressDialog2.setMessage("Loading...");
        progressDialog2.show();

        blogsList.clear();
        blogsList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_BLOGS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Log.d(Utils.TAG, response.toString());

                        if (response != null) {
                            //  String response_filter =  stripHtml(response);
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;
                            try {
                                jsonObject = new JSONObject(response);

                                int success = jsonObject.getInt("status");
                                Log.d(Utils.TAG, " refresh status: " + success);

                                if (success == 1) {
                                    JSONArray JAStuff = jsonObject.getJSONArray("blog_details");

                                    /** CHECK THE NUMBER OF RECORDS **/
                                    int intStuff = JAStuff.length();
                                    Log.d(Utils.TAG, "intStuff: " + intStuff);

                                    if (intStuff != 0)  {
                                        recyclerView.setVisibility(View.VISIBLE);
                                        blogsEmpty.setVisibility(View.GONE);
                                        for (int i = 0; i < JAStuff.length(); i++) {
                                            JSONObject JOStuff = JAStuff.getJSONObject(i);
                                          //         Log.d(Utils.TAG, "Res: " + JOStuff.toString());
                                            blogsList.add(new BlogsList(JOStuff.getInt("post_id"),JOStuff.getString("title"),
                                                    JOStuff.getString("description"),JOStuff.getString("post_image"),
                                                    JOStuff.getString("created_date"),JOStuff.getString("listing_type"),
                                                    JOStuff.getString("username"),JOStuff.getString("userprof"),
                                                    JOStuff.getString("userimg"), JOStuff.getString("contactInfo"),
                                                    JOStuff.getString("attachments"), JOStuff.getInt("company_id"),
                                                    JOStuff.getString("transaction_id"), JOStuff.getString("num_views"),
                                                    JOStuff.getString("video_url"),JOStuff.getString("from_to_date")));
                                        }

                                        Log.d(Utils.TAG, " size: " + blogsList.size());
                                        if(blogsList.size() > 0 ) {
                                            // Initialize a new instance of RecyclerView Adapter instance
                                            mAdapter = new HomeAdapter(getActivity(), blogsList);
                                            // Set the adapter for RecyclerView
                                            recyclerView.setAdapter(mAdapter);
                                            mAdapter.notifyDataSetChanged();

                                            Gson gson = new Gson();
                                            String json_data = gson.toJson(blogsList);
                                            if (sharedPreferences != null) {
                                                shareadPreferenceClass.clearBlogDetailsLists();
                                                shareadPreferenceClass.setBlogDetailsList(json_data);
                                            }

                                        }
                                    }
                                    else {
                                        recyclerView.setVisibility(View.GONE);
                                        blogsEmpty.setVisibility(View.VISIBLE);
                                    }
                                }

                                progressDialog2.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog2.dismiss();
                            }
                        }
                        else {
                            progressDialog2.dismiss();
                        }

                        progressDialog2.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Utils.TAG, error.toString());
                        progressDialog2.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                map.put(APIClass.KEY_LOGINTYPE, Utils.USER_LOGIN_TYPE);
                map.put(APIClass.KEY_USERID, String.valueOf(Utils.USER_LOGIN_ID));
                map.put(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(getContext()).
                getRequestQueue();
        AppController.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private String stripHtml(String response) {
        return Html.fromHtml(response).toString();
    }

    private void setRequestFromServerAll() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading all feeds...");
        progressDialog.show();

        blogsList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_BLOGS_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   Log.d(Utils.TAG, response.toString());

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

                                    /** CHECK THE NUMBER OF RECORDS **/
                                    int intStuff = JAStuff.length();

                                    if (intStuff != 0)  {
                                        recyclerView.setVisibility(View.VISIBLE);
                                        blogsEmpty.setVisibility(View.GONE);
                                        for (int i = 0; i < JAStuff.length(); i++) {
                                            JSONObject JOStuff = JAStuff.getJSONObject(i);
                                            //       Log.d(Utils.TAG, "Res: " + JOStuff.toString());
                                            //    Log.d(Utils.TAG, " video: " + JOStuff.getString("video_url"));
                                            blogsList.add(new BlogsList(JOStuff.getInt("post_id"),JOStuff.getString("title"),
                                                    JOStuff.getString("description"),JOStuff.getString("post_image"),
                                                    JOStuff.getString("created_date"),JOStuff.getString("listing_type"),
                                                    JOStuff.getString("username"),JOStuff.getString("userprof"),
                                                    JOStuff.getString("userimg"), JOStuff.getString("contactInfo"),
                                                    JOStuff.getString("attachments"), JOStuff.getInt("company_id"),
                                                    JOStuff.getString("transaction_id"), JOStuff.getString("num_views"),
                                                    JOStuff.getString("video_url"),JOStuff.getString("from_to_date")));

                                        }

                                        if(blogsList.size() > 0 ) {
                                            // Initialize a new instance of RecyclerView Adapter instance
                                            mAdapter = new HomeAdapter(getActivity(), blogsList);
                                            // Set the adapter for RecyclerView
                                            recyclerView.setAdapter(mAdapter);
                                            mAdapter.notifyDataSetChanged();

                                        }
                                    }
                                    else {
                                        blogsEmpty.setVisibility(View.VISIBLE);
                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }
                        else {
                            progressDialog.dismiss();
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Utils.TAG, "on error");
                        Log.d(Utils.TAG, error.toString());
                        progressDialog.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                map.put(APIClass.KEY_LOGINTYPE, Utils.USER_LOGIN_TYPE);
                map.put(APIClass.KEY_USERID, String.valueOf(Utils.USER_LOGIN_ID));
                map.put(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(getContext()).
                getRequestQueue();
        AppController.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void checkEMRInitialLoad(int view_page) {      // 1- Waiting Room, 2 - Appointments, 3 - EMR

        boolean isFirstRunShow = sharedPreferences.getBoolean("FIRSTRUNLOADEMR", true);
        if (isFirstRunShow)
        {

            Log.d(Utils.TAG, " isFirstRunShow TRUE - - RefreshOphthalEMRActivity");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("FIRSTRUNLOADEMR", false);
            editor.commit();

            Intent bundle = new Intent(getActivity(), RefreshOphthalEMRActivity.class);
            bundle.putExtra("title","Refresh EMR");
            bundle.putExtra("PATIENT_ID", 0);
            bundle.putExtra("PATIENT_NAME", "");
            bundle.putExtra("PATIENT_AGE",  "");
            bundle.putExtra("PATIENT_GENDER", "");
            bundle.putExtra("PATIENT_MOBILE", "");
            bundle.putExtra("PATIENT_EMAIL", "");
            bundle.putExtra("PATIENT_CITY", "");
            bundle.putExtra("PATIENT_ADDRESS", "");
            bundle.putExtra("PATIENT_STATE", "");
            bundle.putExtra("PATIENT_COUNTRY_NAME","");
            bundle.putExtra("PATIENT_WEIGHT", "");
            bundle.putExtra("PATIENT_HYPERTENSION", String.valueOf(0));
            bundle.putExtra("PATIENT_DIABETES", String.valueOf(0));
            bundle.putExtra("PATIENT_SMOKING", "");
            bundle.putExtra("PATIENT_ALCOHOL", "");
            bundle.putExtra("PATIENT_DRUG_ABUSE", "");
            bundle.putExtra("PATIENT_OTHER_DETAILS", "");
            bundle.putExtra("PATIENT_FAMILY_HISTORY", "");
            bundle.putExtra("PATIENT_PREV_INTERVENTIONS", "");
            bundle.putExtra("PATIENT_NEURO_ISSUES", "");
            bundle.putExtra("PATIENT_KIDNEY_ISSUES", "");
            bundle.putExtra("PATIENT_HEIGHT", String.valueOf(0));
            startActivity(bundle);
        }
        else {
            Log.d(Utils.TAG, " isFirstRunShow FALSE - - OtherActivity");
            if(view_page == 1 ) {
                Intent i10 = new Intent(getActivity(), WaitingRoomActivity.class);
                i10.putExtra("title","Waiting Room");
                startActivity(i10);
            }
            else if(view_page == 2 ) {
                Intent i5 = new Intent(getActivity(), AppointmentsNewActivity.class);
                i5.putExtra("title","Appointments");
                i5.putExtra("FILTER_TYPE", FILTER_TYPE);
                startActivity(i5);
            }
            else if(view_page == 3 ) {
                Intent i10 = new Intent(getActivity(), EMRActivity.class);
                i10.putExtra("title","EMR");
                startActivity(i10);
            }

        }
    }
}
