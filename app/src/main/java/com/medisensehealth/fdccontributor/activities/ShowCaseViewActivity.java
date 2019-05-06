package com.medisensehealth.fdccontributor.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

/**
 * Created by lenovo on 20/11/2017.
 */
public class ShowCaseViewActivity extends AppCompatActivity {
    private static final String SHOWCASE_ID = "sequence example";

    LinearLayout blogs_layout, events_layouts, videos_layout, fellowship_layout, appointment_layout, mypatients_layout, casesreceived_layout, connections_layout, waiting_room_layout;
    LinearLayout footer_home, footer_favourites, footer_quiz, footer_account, footer_help;
    ImageView footer_home_img, footer_favourites_img, footer_quiz_img, footer_account_img, footer_help_img;
    CustomTextViewBold footer_home_txt, footer_favourites_txt, footer_quiz_txt, footer_account_txt, footer_help_txt;
    View footer_tabs;

    String ENTRY_TYPE = "NORMAL_TEST", USER_LOGIN_TYPE;
    int LOGIN_USER_ID = 0, USER_HOSPITAL_ID;
    String USER_LOGIN_NAME;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    LinearLayout rootView = null;
    ImageView bmj_icon, share_icon, notify_icon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcase_view);

        MaterialShowcaseView.resetSingleUse(ShowCaseViewActivity.this, SHOWCASE_ID);
        initialization(savedInstanceState);
    }

    private void initialization(Bundle savedInstanceState) {

        shareadPreferenceClass = new ShareadPreferenceClass(getApplicationContext());
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(getApplicationContext());
        if(sharedPreferences != null ) {
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, "0");

            if (USER_LOGIN_TYPE.equals("1")) {
                USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, "0");
                LOGIN_USER_ID =  sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            }
            else if (USER_LOGIN_TYPE.equals("2")) {
                USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, "0");
                LOGIN_USER_ID =  sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            }
            else if (USER_LOGIN_TYPE.equals("3")) {
                USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, "0");
                LOGIN_USER_ID =  sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            }
        }

        // Header Tabs
        blogs_layout = (LinearLayout) findViewById(R.id.home_blogs);
        events_layouts = (LinearLayout) findViewById(R.id.home_events);
        videos_layout = (LinearLayout) findViewById(R.id.home_videos);
        fellowship_layout = (LinearLayout) findViewById(R.id.home_fellowships);
        appointment_layout = (LinearLayout) findViewById(R.id.home_appointments);
        mypatients_layout = (LinearLayout) findViewById(R.id.home_mypatients);
        casesreceived_layout = (LinearLayout) findViewById(R.id.home_cases_reveived);

        waiting_room_layout = (LinearLayout) findViewById(R.id.home_waitingroom);

        footer_tabs = (View) findViewById(R.id.footer);
        // Initialize footer views
        footer_home = (LinearLayout) findViewById(R.id.footer_home_layout);
        footer_favourites = (LinearLayout) findViewById(R.id.footer_favourites_layout);
        footer_quiz = (LinearLayout) findViewById(R.id.footer_quiz_layout);
        footer_account = (LinearLayout) findViewById(R.id.footer_myaccount_layout);
        footer_help = (LinearLayout) findViewById(R.id.footer_help_layout);
        connections_layout = (LinearLayout) findViewById(R.id.footer_connections_layout);

        footer_home_img = (ImageView) findViewById(R.id.footer_home_img);
        footer_favourites_img = (ImageView) findViewById(R.id.footer_favourites_img);
        footer_quiz_img = (ImageView) findViewById(R.id.footer_quiz_img);
        footer_account_img = (ImageView) findViewById(R.id.footer_myaccount_img);
        footer_help_img = (ImageView) findViewById(R.id.footer_help_img);

        footer_home_txt = (CustomTextViewBold) findViewById(R.id.footer_home_text);
        footer_favourites_txt = (CustomTextViewBold) findViewById(R.id.footer_favourites_text);
        footer_quiz_txt = (CustomTextViewBold) findViewById(R.id.footer_quiz_text);
        footer_account_txt = (CustomTextViewBold) findViewById(R.id.footer_myaccount_text);
        footer_help_txt = (CustomTextViewBold) findViewById(R.id.footer_help_text);

        footer_home_img.setImageResource(R.mipmap.home_icon_select);
        footer_favourites_img.setImageResource(R.mipmap.favourite_icon_unselct);
        footer_quiz_img.setImageResource(R.mipmap.quiz_icon_unselect);
        footer_account_img.setImageResource(R.mipmap.my_account_unselect);
        footer_help_img.setImageResource(R.mipmap.help_icon_unselect);

        footer_home_txt.setTextColor(getResources().getColor(R.color.textPrimary));
        footer_favourites_txt.setTextColor(getResources().getColor(R.color.textColor));
        footer_quiz_txt.setTextColor(getResources().getColor(R.color.textColor));
        footer_account_txt.setTextColor(getResources().getColor(R.color.textColor));
        footer_help_txt.setTextColor(getResources().getColor(R.color.textColor));

        bmj_icon = (ImageView)  findViewById(R.id.showcase_bmj);
        share_icon = (ImageView)  findViewById(R.id.showcase_share);
        notify_icon = (ImageView)  findViewById(R.id.showcase_notify);

      /*  rootView = (LinearLayout) findViewById(R.id.showview_root);
        boolean isAttachedToWindow = ViewCompat.isAttachedToWindow(rootView);
        if(isAttachedToWindow) {
            presentShowcaseSequence();
        }
        else {
            ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        presentShowcaseSequence();
                        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        }*/
       /* if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            boolean isAttachedToWindow = ViewCompat.isAttachedToWindow(rootView);
            if(isAttachedToWindow) {
                presentShowcaseSequence();
            }
            else {
                ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onGlobalLayout() {
                            presentShowcaseSequence();
                            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
                }
            }
        }
        else {
            presentShowcaseSequence();
        }*/

        rootView = (LinearLayout) findViewById(R.id.showview_root);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            boolean isAttachedToWindow = ViewCompat.isAttachedToWindow(rootView);
            if(isAttachedToWindow) {
                presentShowcaseSequence();
            }
            else {
                ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onGlobalLayout() {
                            presentShowcaseSequence();
                            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
                }
            }
        }
        else {
            presentShowcaseSequence();
        }
    }


    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
      //  config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
                // Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
            }
        });

        sequence.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
            @Override
            public void onDismiss(MaterialShowcaseView materialShowcaseView, int i) {
                if(i == 14) {
                    // Toast.makeText(materialShowcaseView.getContext(), "Item #" + i + " Last Item", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ShowCaseViewActivity.this, DashboardActivity.class);
                            intent.putExtra("LOGIN_TYPE", USER_LOGIN_TYPE);
                            intent.putExtra("USER_ID", LOGIN_USER_ID);
                            intent.putExtra("ENTRY_TYPE", "NORMAL");
                            startActivity(intent);
                            overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            finish();
                }
            }
        });

        sequence.setConfig(config);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(bmj_icon)
                        .setDismissText("Next")
                        .setTitleText("BMJ")
                        .setContentText("Free access to British Journals of Ophthalmology")
                        .withCircleShape()
                        .setMaskColour(R.color.colorPrimaryDark)
                        .setMaskColour((ContextCompat.getColor(this, R.color.textPrimary) & 0x00FFFFFF) | 0xdd000000)
                       // .setDismissOnTouch(true) // ontouch screen next move
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(share_icon)
                        .setDismissText("Next")
                        .setTitleText("Share Profile")
                        .setContentText("Share your profile link with patients")
                        .withCircleShape()
                        .setMaskColour(R.color.colorPrimaryDark)
                        .setMaskColour((ContextCompat.getColor(this, R.color.textPrimary) & 0x00FFFFFF) | 0xdd000000)
                      //  .setDismissOnTouch(true) // ontouch screen next move
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(notify_icon)
                        .setDismissText("Next")
                        .setTitleText("Notifications")
                        .setContentText("Here you will receive notifications")
                        .withCircleShape()
                        .setMaskColour(R.color.colorPrimaryDark)
                        .setMaskColour((ContextCompat.getColor(this, R.color.textPrimary) & 0x00FFFFFF) | 0xdd000000)
                       // .setDismissOnTouch(true) // ontouch screen next move
                        .build()
        );


        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(waiting_room_layout)
                        .setDismissText("Next")
                        .setTitleText("Waiting Room")
                        .setContentText("View and manage your today's appointment schedule. ")
                        .withCircleShape()
                        .setMaskColour(R.color.colorPrimaryDark)
                        .setMaskColour((ContextCompat.getColor(this, R.color.textPrimary) & 0x00FFFFFF) | 0xdd000000)
                        //   .setDismissOnTouch(true) // ontouch screen next move
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(appointment_layout)
                        .setDismissText("Next")
                        .setTitleText("Appointments")
                        .setContentText("View and manage your daily appointment schedule. Book new appointment and reschedule the existing ones.")
                        .withCircleShape()
                        .setMaskColour(R.color.colorPrimaryDark)
                        .setMaskColour((ContextCompat.getColor(this, R.color.textPrimary) & 0x00FFFFFF) | 0xdd000000)
                        // .setDismissOnTouch(true) // ontouch screen next move
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(mypatients_layout)
                        .setDismissText("Next")
                        .setTitleText("My Patients")
                        .setContentText("Add new patient details or update profile of existing ones. View and manage patient database.")
                        .withCircleShape()
                        .setMaskColour(R.color.colorPrimaryDark)
                        .setMaskColour((ContextCompat.getColor(this, R.color.textPrimary) & 0x00FFFFFF) | 0xdd000000)
                     //   .setDismissOnTouch(true) // ontouch screen next move
                        .build()
        );



        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(casesreceived_layout)
                        .setDismissText("Next")
                        .setTitleText("Cases Received")
                        .setContentText("You can view the list of cases received along with the status. Also chat with the fellow doctors.")
                        .withCircleShape()
                        .setMaskColour(R.color.colorPrimaryDark)
                        .setMaskColour((ContextCompat.getColor(this, R.color.textPrimary) & 0x00FFFFFF) | 0xdd000000)
                      //  .setDismissOnTouch(true) // ontouch screen next move
                        .build()
        );


        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(blogs_layout)
                        .setDismissText("Next")
                        .setTitleText("Blogs")
                        .setContentText("You can write and publish your blogs or articles or achievements etc.")
                        .withCircleShape()
                        .setMaskColour(R.color.colorPrimaryDark)
                        .setMaskColour((ContextCompat.getColor(this, R.color.textPrimary) & 0x00FFFFFF) | 0xdd000000)
                      //  .setDismissOnTouch(true) // ontouch screen next move
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(events_layouts)
                        .setDismissText("Next")
                        .setTitleText("Events")
                        .setContentText("You can explore and register for all upcoming conferences, CME's and more.")
                        .withCircleShape()
                        .setMaskColour(R.color.colorPrimaryDark)
                        .setMaskColour((ContextCompat.getColor(this, R.color.textPrimary) & 0x00FFFFFF) | 0xdd000000)
                      //  .setDismissOnTouch(true) // ontouch screen next move
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(videos_layout)
                        .setDismissText("Next")
                        .setTitleText("Videos")
                        .setContentText("Upload surgical videos or any educational videos just by adding youtube link.")
                        .withCircleShape()
                        .setMaskColour(R.color.colorPrimaryDark)
                        .setMaskColour((ContextCompat.getColor(this, R.color.textPrimary) & 0x00FFFFFF) | 0xdd000000)
                       // .setDismissOnTouch(true) // ontouch screen next move
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(fellowship_layout)
                        .setDismissText("Next")
                        .setTitleText("Fellowships")
                        .setContentText("You can explore and register for the fellowship programs.")
                        .withCircleShape()
                        .setMaskColour(R.color.colorPrimaryDark)
                        .setMaskColour((ContextCompat.getColor(this, R.color.textPrimary) & 0x00FFFFFF) | 0xdd000000)
                      //  .setDismissOnTouch(true) // ontouch screen next move
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(footer_home)
                        .setDismissText("Next")
                        .setTitleText("Home")
                        .setContentText("Home page")
                        .withCircleShape()
                        .setMaskColour(R.color.colorPrimaryDark)
                        .setMaskColour((ContextCompat.getColor(this, R.color.textPrimary) & 0x00FFFFFF) | 0xdd000000)
                     //   .setDismissOnTouch(true) // ontouch screen next move
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(connections_layout)
                        .setDismissText("Next")
                        .setTitleText("Connections")
                        .setContentText("View all the doctors in your network.")
                        .withCircleShape()
                        .setMaskColour(R.color.colorPrimaryDark)
                        .setMaskColour((ContextCompat.getColor(this, R.color.textPrimary) & 0x00FFFFFF) | 0xdd000000)
                        //  .setDismissOnTouch(true) // ontouch screen next move
                        .build()
        );


       /* sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(footer_favourites)
                        .setDismissText("Next")
                        .setTitleText("Favourites")
                        .setContentText("You can view all your favourite doctors")
                        .withCircleShape()
                        .setMaskColour(R.color.colorPrimaryDark)
                        .setDismissOnTouch(true) // ontouch screen next move
                        .build()
        );*/

      /*  sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(footer_quiz)
                        .setDismissText("Next")
                        .setTitleText("Quiz")
                        .setContentText("Test your expertise with the quiz competition held by industry experts.")
                        .withCircleShape()
                        .setMaskColour(R.color.colorPrimaryDark)
                        .setMaskColour((ContextCompat.getColor(this, R.color.textPrimary) & 0x00FFFFFF) | 0xdd000000)
                     //   .setDismissOnTouch(true) // ontouch screen next move
                        .build()
        );*/


        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(footer_help)
                        .setDismissText("Next")
                        .setTitleText("Tour")
                        .setContentText("Take a quick tour to understand the features.")
                        .withCircleShape()
                        .setMaskColour(R.color.colorPrimaryDark)
                        .setMaskColour((ContextCompat.getColor(this, R.color.textPrimary) & 0x00FFFFFF) | 0xdd000000)
                      //  .setDismissOnTouch(true) // ontouch screen next move
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(footer_account)
                        .setDismissText("Get Started")
                        .setTitleText("Account")
                        .setContentText("View and manage your account.")
                        .withCircleShape()
                        .setMaskColour(R.color.colorPrimaryDark)
                        .setMaskColour((ContextCompat.getColor(this, R.color.textPrimary) & 0x00FFFFFF) | 0xdd000000)
                        //    .setDismissOnTouch(true) // ontouch screen next move
                        .build()
        );

        sequence.start();

    }

}
