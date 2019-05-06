package com.medisensehealth.fdccontributor.adapter.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.medisensehealth.fdccontributor.DataModel.NotifyMessages;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.DashboardActivity;
import com.medisensehealth.fdccontributor.activities.notifications.NotificationActivity;
import com.medisensehealth.fdccontributor.activities.notifications.NotifyMessageDetailActivity;
import com.medisensehealth.fdccontributor.activities.patients.PatientDetailsActivity;
import com.medisensehealth.fdccontributor.database.MedisensePracticeDB;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;
import com.medisensehealth.fdccontributor.views.CustomTextViewItalic;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Created by lenovo on 10/05/2017.
 */

public class NotifyMessageAdapter  extends RecyclerView.Adapter<NotifyMessageAdapter.MyViewHolder>  {

    private List<NotifyMessages> messageList;
    Context mContext;
    FragmentManager fragManager;
    FragmentTransaction fragTransaction;
    String DOWNLOAD_IMAGE_URL;
    NotifyMessageDetailActivity messagedetail;

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME, USER_LOGIN_TYPE;

    public class MyViewHolder extends RecyclerView.ViewHolder {

         CardView cardMessage;
         CustomTextViewBold title;
         CustomTextViewItalic description;
         ImageView post_image;
         RelativeLayout post_linear;

        public MyViewHolder(View view) {
            super(view);

            cardMessage = (CardView) view.findViewById(R.id.message_list_cardview);
            title = (CustomTextViewBold) view.findViewById(R.id.message_title);
            description = (CustomTextViewItalic) view.findViewById(R.id.message_description);
            post_image = (ImageView) view.findViewById(R.id.message_image);
            post_linear = (RelativeLayout) view.findViewById(R.id.messsage_linearLayout);

        }
    }


    public NotifyMessageAdapter(Context context, List<NotifyMessages> mList) {
        this.messageList = mList;
        this.mContext = context;
        fragManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
        messagedetail =  new NotifyMessageDetailActivity();

        shareadPreferenceClass = new ShareadPreferenceClass(mContext);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(mContext);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messages_list_row, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final NotifyMessages nMessages = messageList.get(position);

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

        if(nMessages.getNotifyPostImage().equals("large_icon") || nMessages.getNotifyPostImage().equals("")) {
            holder.post_image.setVisibility(View.GONE);
        }
        else {
            holder.post_image.setVisibility(View.VISIBLE);
        }

        if(nMessages.getNotifyPostType().equalsIgnoreCase("1")) {
            DOWNLOAD_IMAGE_URL = APIClass.DRS_BLOGS_IMAGE_URL+String.valueOf(nMessages.getNotifyPostId())+"/"+nMessages.getNotifyPostImage().trim();
        }
        else if(nMessages.getNotifyPostType().equalsIgnoreCase("2")) {
            DOWNLOAD_IMAGE_URL = APIClass.DRS_OFFERS_EVENTS_URL+String.valueOf(nMessages.getNotifyPostId())+"/"+nMessages.getNotifyPostImage().trim();
        }
        else   if(nMessages.getNotifyPostType().equalsIgnoreCase("4")){
            DOWNLOAD_IMAGE_URL = APIClass.DRS_OFFERS_EVENTS_URL+String.valueOf(nMessages.getNotifyPostId())+"/"+nMessages.getNotifyPostImage().trim();
        }
        else {
            DOWNLOAD_IMAGE_URL = "";
        }

       // if(DOWNLOAD_IMAGE_URL.equals("")) {
        if( (nMessages.getNotifyPostImage().trim().equals("")) || (nMessages.getNotifyPostImage().trim() != null) || (nMessages.getNotifyPostImage().trim().length() <= 0)) {
            // Picasso.with(mContext).load(DOWNLOAD_IMAGE_URL).into(holder.post_image);
        }
        else {

            String urlStr = DOWNLOAD_IMAGE_URL;
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
         //   Picasso.with(mContext).load(String.valueOf(url)).into(holder.post_image);
            Picasso.with(mContext).load(String.valueOf(url))
                    .placeholder(R.drawable.blogs_empty_img)
                    .error(R.drawable.blogs_empty_img)
                    .fit()
                    .into(holder.post_image, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }

        holder.title.setText( stripHtml(nMessages.getNotifyPostTitle().trim()));
        holder.description.setText( stripHtml(nMessages.getNotifyPostMessage().trim()));

        Log.d(Utils.TAG, "notifyId: " + nMessages.getNotifyId());
      //  Log.d(Utils.TAG, "postId: " + nMessages.getNotifyPostId());
      //  Log.d(Utils.TAG, "patId: " + nMessages.getNotifyPatientId());

        holder.post_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(mContext, "Notification" +nMessages.getNotifyPostEntry(), Toast.LENGTH_SHORT).show();

                if(nMessages.getNotifyPostEntry().equalsIgnoreCase("RESPONSEPUSH")) {


                    Log.d(Utils.TAG, "pId: " + Integer.parseInt(nMessages.getNotifyPatientId()));
                    Log.d(Utils.TAG, "dId: " + Integer.parseInt(nMessages.getNotifyRefId()));

                    Intent theintent = new Intent(mContext, PatientDetailsActivity.class);
                    theintent.putExtra("PATIENT_ID", Integer.parseInt(nMessages.getNotifyPatientId()));
                    theintent.putExtra("DOCREF_ID", Integer.parseInt(nMessages.getNotifyRefId()));
                    theintent.putExtra("NOTIFY_KEY", nMessages.getNotifyId());
                    theintent.putExtra("PATIENT_VIEW","PUSHVIEW");
                    mContext.startActivity(theintent);
                    ((AppCompatActivity) mContext).finish();

                }
                else if(nMessages.getNotifyPostEntry().equalsIgnoreCase("APPOINTMENTPUSH")) {

                  // Log.d(Utils.TAG, "TransId: " + Integer.parseInt(nMessages.getNotifyPatientId()));

                    String delete_msg = MedisensePracticeDB.deleteNotificationContents(mContext, USER_ID, USER_LOGIN_TYPE, nMessages.getNotifyId());
                    Log.d(Utils.TAG + " del_msg: ", delete_msg);
                    int notifyCount = MedisensePracticeDB.getNotificationCount(mContext, USER_ID, USER_LOGIN_TYPE);
                    Log.d(Utils.TAG + " notCount: ", String.valueOf(notifyCount));
                    if(notifyCount > 0) {
                        DashboardActivity.setPendingNotificationsCount(notifyCount);
                    }

                    /*AppointmentDetailsFragment appointmentDetails = new AppointmentDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("APPT_TRANSACTION_ID", nMessages.getNotifyPatientId());
                    bundle.putString("APPT_TIME", "");
                    fragTransaction = fragManager.beginTransaction();
                    appointmentDetails.setArguments(bundle);
                    fragTransaction.replace(R.id.dashboard_fragment,appointmentDetails,"APPOINTMENT_DETAIL");
                    // fragTransaction.addToBackStack("APPOINTMENT_DETAIL");
                    fragTransaction.commit();*/


                }
                else {
                    Log.d(Utils.TAG, "nId: " + nMessages.getNotifyId());
                    Log.d(Utils.TAG, "pId: " + nMessages.getNotifyPostId());

                    /*Bundle bundle = new Bundle();
                    bundle.putInt("NOTIFY_ID", nMessages.getNotifyId());
                    bundle.putInt("POST_ID", nMessages.getNotifyPostId());
                    bundle.putString("POST_IMAGE", nMessages.getNotifyPostImage());
                    bundle.putString("POST_TITLE", nMessages.getNotifyPostTitle().trim());
                    bundle.putString("POST_AUTHOR", nMessages.getNotifyPostAuthor());
                    bundle.putString("POST_DATE", nMessages.getNotifyPostDate());
                    bundle.putString("POST_MESSAGE", nMessages.getNotifyPostMessage());
                    bundle.putString("POST_TYPE", nMessages.getNotifyPostType());
                    bundle.putString("POST_ENTRY", nMessages.getNotifyPostEntry());
                    fragTransaction = fragManager.beginTransaction();
                    messagedetail.setArguments(bundle);
                    fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragTransaction.replace(R.id.dashboard_fragment,messagedetail,"MESSAGE_DETAILS");
                    fragTransaction.commit();*/

                    Intent i1 = new Intent(mContext, NotifyMessageDetailActivity.class);
                    i1.putExtra("title","Notifications");
                    i1.putExtra("NOTIFY_ID",nMessages.getNotifyId());
                    i1.putExtra("POST_ID",nMessages.getNotifyPostId());
                    i1.putExtra("POST_IMAGE",nMessages.getNotifyPostImage());
                    i1.putExtra("POST_TITLE",nMessages.getNotifyPostTitle().trim());
                    i1.putExtra("POST_AUTHOR",nMessages.getNotifyPostAuthor());
                    i1.putExtra("POST_DATE",nMessages.getNotifyPostDate());
                    i1.putExtra("POST_MESSAGE",nMessages.getNotifyPostMessage());
                    i1.putExtra("POST_TYPE",nMessages.getNotifyPostType());
                    i1.putExtra("POST_ENTRY",nMessages.getNotifyPostEntry());

                    mContext.startActivity(i1);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    private String stripHtml(String response) {
        return Html.fromHtml(response).toString();
    }
}
