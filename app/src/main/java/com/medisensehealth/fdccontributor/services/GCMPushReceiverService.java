package com.medisensehealth.fdccontributor.services;

/**
 * Created by lenovo on 22-03-2017.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.DashboardActivity;
import com.medisensehealth.fdccontributor.database.MedisensePracticeDB;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by Belal on 4/15/2016.
 */

//Class is extending GcmListenerService
public class GCMPushReceiverService extends GcmListenerService {

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_LOGIN_TYPE;
    int pendingNotificationsCount;
    String entrytype;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String postType = data.getString("postType");
        String message = data.getString("message");
        String title = data.getString("title");
        String subtitle = data.getString("subtitle");
        String tickerText = data.getString("tickerText");
        String vibrate = data.getString("vibrate");
        String sound = data.getString("sound");
        String largeIcon = data.getString("largeIcon");
        String smallIcon = data.getString("smallIcon");
        String postId = data.getString("postId");
        String patient_id =  data.getString("patientId");
        String doc_id =  data.getString("docId");
        String postKey =  data.getString("postKey");

        Log.d(Utils.TAG, "postType: " + postType);
        Log.d(Utils.TAG, "message: " + message);
        Log.d(Utils.TAG, "title: " + title);
        Log.d(Utils.TAG, "subtitle: " + subtitle);
        Log.d(Utils.TAG, "tickerText: " + tickerText);
        Log.d(Utils.TAG, "vibrate: " + vibrate);
        Log.d(Utils.TAG, "sound: " + sound);
        Log.d(Utils.TAG, "largeIcon: " + largeIcon);
        Log.d(Utils.TAG, "smallIcon: " + smallIcon);
        Log.d(Utils.TAG, "postId: " + postId);
        Log.d(Utils.TAG, "patient_id: " + patient_id);
        Log.d(Utils.TAG, "doc_id: " + doc_id);
        Log.d(Utils.TAG, "postKey: " + postKey);

        message =  stripHtml(message);
        subtitle =  stripHtml(subtitle);

        shareadPreferenceClass = new ShareadPreferenceClass(getApplicationContext());
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(getApplicationContext());

        if(sharedPreferences != null) {
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
        }

        if(USER_LOGIN_TYPE.equals("1")) {
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
        }
        else if(USER_LOGIN_TYPE.equals("2")) {
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
        }
        else if(USER_LOGIN_TYPE.equals("3")) {
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
        }

        if(postType.equals("1")) {
            Log.d(Utils.TAG, "BLOGS");
            entrytype = "BLOGPUSH";
           new generatePictureStyleNotification(this,title, message, subtitle,tickerText,largeIcon, smallIcon,USER_LOGIN_TYPE,postId,patient_id,postType,doc_id, postKey ).execute();
        }
        else  if(postType.equals("2")) {
            Log.d(Utils.TAG, "EVENTS");
            entrytype = "EVENTPUSH";
            new generatEventPictureStyleNotification(this,title, message, subtitle,tickerText,largeIcon, smallIcon,USER_LOGIN_TYPE,postId,patient_id,postType,doc_id,postKey).execute();
        }
        else  if(postType.equals("3")) {
            Log.d(Utils.TAG, "OFFERS");
            entrytype = "OFFERPUSH";
            new generatOfferPictureStyleNotification(this,title, message, subtitle,tickerText,largeIcon, smallIcon,USER_LOGIN_TYPE,postId,patient_id,postType,doc_id,postKey).execute();
        }

        else  if(postType.equals("4")) {
            Log.d(Utils.TAG, "RESPONSE");
            entrytype = "RESPONSEPUSH";
            new generatResponsePictureStyleNotification(this,title, message, subtitle,tickerText,largeIcon, smallIcon,USER_LOGIN_TYPE,postId,patient_id,postType,doc_id,postKey).execute();
        }

        else  if(postType.equals("5")) {
            Log.d(Utils.TAG, "JOBS");
            entrytype = "JOBSPUSH";
            new generatEventPictureStyleNotification(this,title, message, subtitle,tickerText,largeIcon, smallIcon,USER_LOGIN_TYPE,postId,patient_id,postType,doc_id,postKey).execute();
        }

        else  if(postType.equals("6")) {
            Log.d(Utils.TAG, "SURGERY");
            entrytype = "SURGERYPUSH";
            new generatEventPictureStyleNotification(this,title, message, subtitle,tickerText,largeIcon, smallIcon,USER_LOGIN_TYPE,postId,patient_id,postType,doc_id,postKey).execute();
        }
        else  if(postType.equals("7")) {        // Appointments
            Log.d(Utils.TAG, "APPOINTMENTPUSH");
            entrytype = "APPOINTMENTPUSH";
            new generatAppointmentPictureStyleNotification(this,title, message, subtitle,tickerText,largeIcon, smallIcon,USER_LOGIN_TYPE,postId,patient_id,postType,doc_id,postKey).execute();
        }

        String fileTypeArray[] = largeIcon.split("/");
        String post_image = "";
        if(fileTypeArray != null && fileTypeArray.length > 0) {
            post_image = fileTypeArray[fileTypeArray.length - 1];
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = sdf.format(c.getTime());

        MedisensePracticeDB.insertNotification(getApplicationContext(), USER_ID,USER_LOGIN_TYPE,postId,post_image,title.trim(),"Practice",currentDate,message.trim(),postType,entrytype, patient_id, doc_id, postKey );

    }


    //This method is generating a notification and displaying the notification
    private void sendNotification(String message, String title, String subtitle, String tickerText, String largeIcon) {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;

        message =  stripHtml(message);

        //get the bitmap to show in notification bar
        Bitmap bitmap_image = BitmapFactory.decodeResource(this.getResources(), R.mipmap.attach_icon);
        NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(bitmap_image);
        s.setSummaryText("Summary text appears on expanding the notification");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Practice")
                .setContentText(message)
                .setAutoCancel(true)
                .setTicker(tickerText)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.prime_launher_icon))
                .setStyle(s)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
    }

    public class generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {
        Bitmap largeImage, smallImage;
        private Context mContext;
        private String title, message, subtitle,largeIcon, imageUrl, ticketText, smallIcon, USER_LOGIN_TYPE, postId, patient_id, postType,doc_id,postKey;
        public generatePictureStyleNotification(Context context, String title, String message, String subtitle,String tickerText, String largeIcon, String smallIcon, String USER_LOGIN_TYPE, String postId, String patient_id, String postType, String doc_id, String postKey) {
            super();
            this.mContext = context;
            this.title = title;
            this.message = message;
            this.imageUrl = imageUrl;
            this.subtitle = subtitle;
            this.largeIcon = largeIcon;
            this.ticketText = tickerText;
            this.smallIcon = smallIcon;
            this.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            this.postId = postId;
            this.patient_id = patient_id;
            this.postType = postType;
            this.doc_id = doc_id;
            this.postKey = postKey;
        }


        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in, in1;
            try {
                URL url = new URL(smallIcon);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                smallImage = myBitmap;

                //Large Image
                URL url1 = new URL(largeIcon);
                HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                connection1.setDoInput(true);
                connection1.connect();
                in1 = connection1.getInputStream();
                largeImage = BitmapFactory.decodeStream(in1);


                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

     //   @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);


            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDate = sdf.format(c.getTime());

            String fileTypeArray[] = largeIcon.split("/");
            String image_name = "";
            if(fileTypeArray != null && fileTypeArray.length > 0) {
                image_name = fileTypeArray[fileTypeArray.length - 1];
            }
            Log.d(Utils.TAG, "image_name: " + image_name);

            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);

            intent.putExtra("LOGIN_TYPE", USER_LOGIN_TYPE);
            intent.putExtra("ENTRY_TYPE", "BLOGPUSH");
            intent.putExtra("BLOG_ID", postId);
            intent.putExtra("BLOG_IMAGE", image_name);
            intent.putExtra("BLOG_TITLE", title.trim());
            intent.putExtra("BLOG_AUTHOR", "By: Dr. Encrypted_Doc_Name");
            intent.putExtra("BLOG_DATE", currentDate);
            intent.putExtra("BLOG_DESCRIPTION", message.trim());
            intent.putExtra("BLOG_TYPE", postType);
            intent.putExtra("BLOG_PATIENT_ID", patient_id);
            intent.putExtra("BLOG_REF_ID", doc_id);
            intent.putExtra("BLOG_KEY", postKey);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int requestID = (int) System.currentTimeMillis();

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestID, intent, 0);
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(title.trim())
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.prime_launher_icon)
                    .setTicker(ticketText)
                    .setLargeIcon(smallImage)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(new long[] { 100, 1000})
                    .setLights(Color.RED, 3000, 3000)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

            if(largeIcon.equalsIgnoreCase("large_icon")) {
                noBuilder.setLargeIcon(smallImage);
            }
            else {
                noBuilder.setLargeIcon(smallImage);
                noBuilder.setStyle( new NotificationCompat.BigPictureStyle().bigPicture(largeImage));
            }

            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(requestID, noBuilder.build());


        }
    }

    public class generatEventPictureStyleNotification extends AsyncTask<String, Void, Bitmap> {
        Bitmap largeImage, smallImage;
        private Context mContext;
        private String title, message, subtitle,largeIcon, imageUrl, ticketText, smallIcon, smallImageUrl, USER_LOGIN_TYPE, postId, patient_id, postType, doc_id,postKey ;
        public generatEventPictureStyleNotification(Context context, String title, String message, String subtitle,String tickerText, String largeIcon, String smallIcon,String USER_LOGIN_TYPE, String postId, String patient_id, String postType, String doc_id, String postKey) {
            super();
            this.mContext = context;
            this.title = title;
            this.message = message;
            this.imageUrl = imageUrl;
            this.subtitle = subtitle;
            this.largeIcon = largeIcon;
            this.ticketText = tickerText;
            this.smallIcon = smallIcon;
            this.smallImageUrl = smallImageUrl;
            this.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            this.postId = postId;
            this.patient_id = patient_id;
            this.postType = postType;
            this.doc_id = doc_id;
            this.postKey = postKey;
        }


        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in, in1;
            try {
                URL url = new URL(smallIcon);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                smallImage = myBitmap;

                //Large Image
                URL url1 = new URL(largeIcon);
                HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                connection1.setDoInput(true);
                connection1.connect();
                in1 = connection1.getInputStream();
                largeImage = BitmapFactory.decodeStream(in1);


                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        //   @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDate = sdf.format(c.getTime());

            String fileTypeArray[] = largeIcon.split("/");
            String image_name = "";
            if(fileTypeArray != null && fileTypeArray.length > 0) {
                image_name = fileTypeArray[fileTypeArray.length - 1];
            }
            Log.d(Utils.TAG, "image_name: " + image_name);

            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.putExtra("LOGIN_TYPE", USER_LOGIN_TYPE);
            intent.putExtra("ENTRY_TYPE", "EVENTPUSH");
            intent.putExtra("BLOG_ID", postId);
            intent.putExtra("BLOG_IMAGE", image_name);
            intent.putExtra("BLOG_TITLE", title.trim());
            intent.putExtra("BLOG_AUTHOR", "By: Dr. Encrypted_Doc_Name");
            intent.putExtra("BLOG_DATE", currentDate);
            intent.putExtra("BLOG_DESCRIPTION", message.trim());
            intent.putExtra("BLOG_TYPE", postType);
            intent.putExtra("BLOG_PATIENT_ID", patient_id);
            intent.putExtra("BLOG_REF_ID", doc_id);
            intent.putExtra("BLOG_KEY", postKey);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int requestID = (int) System.currentTimeMillis();

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestID, intent, 0);
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(title.trim())
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.prime_launher_icon)
                    .setTicker(ticketText)
                    .setLargeIcon(smallImage)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(new long[] { 100, 1000})
                    .setLights(Color.RED, 3000, 3000)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

            if(largeIcon.equalsIgnoreCase("large_icon")) {
                noBuilder.setLargeIcon(smallImage);
            }
            else {
                noBuilder.setLargeIcon(smallImage);
                noBuilder.setStyle( new NotificationCompat.BigPictureStyle().bigPicture(largeImage));
            }

            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(requestID, noBuilder.build());


        }
    }

    public class generatOfferPictureStyleNotification extends AsyncTask<String, Void, Bitmap> {
        Bitmap largeImage, smallImage;
        private Context mContext;
        private String title, message, subtitle,largeIcon, imageUrl, ticketText, smallIcon, smallImageUrl, USER_LOGIN_TYPE, postId, patient_id, postType, doc_id,postKey ;
        public generatOfferPictureStyleNotification(Context context, String title, String message, String subtitle,String tickerText, String largeIcon, String smallIcon,String USER_LOGIN_TYPE, String postId, String patient_id, String postType, String doc_id, String postKey) {
            super();
            this.mContext = context;
            this.title = title;
            this.message = message;
            this.subtitle = subtitle;
            this.largeIcon = largeIcon;
            this.ticketText = tickerText;
            this.smallIcon = smallIcon;
            this.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            this.postId = postId;
            this.patient_id = patient_id;
            this.postType = postType;
            this.doc_id = doc_id;
            this.postKey = postKey;
        }


        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in, in1;
            try {
                URL url = new URL(smallIcon);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                smallImage = myBitmap;

                //Large Image
                URL url1 = new URL(largeIcon);
                HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                connection1.setDoInput(true);
                connection1.connect();
                in1 = connection1.getInputStream();
                largeImage = BitmapFactory.decodeStream(in1);


                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDate = sdf.format(c.getTime());

            String fileTypeArray[] = largeIcon.split("/");
            String image_name = "";
            if(fileTypeArray != null && fileTypeArray.length > 0) {
                image_name = fileTypeArray[fileTypeArray.length - 1];
            }
            Log.d(Utils.TAG, "image_name: " + image_name);

            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.putExtra("LOGIN_TYPE", USER_LOGIN_TYPE);
            intent.putExtra("ENTRY_TYPE", "OFFERPUSH");
            intent.putExtra("BLOG_ID", postId);
            intent.putExtra("BLOG_IMAGE", image_name);
            intent.putExtra("BLOG_TITLE", title.trim());
            intent.putExtra("BLOG_AUTHOR", "By: Dr. Encrypted_Doc_Name");
            intent.putExtra("BLOG_DATE", currentDate);
            intent.putExtra("BLOG_DESCRIPTION", message.trim());
            intent.putExtra("BLOG_TYPE", postType);
            intent.putExtra("BLOG_PATIENT_ID", patient_id);
            intent.putExtra("BLOG_REF_ID", doc_id);
            intent.putExtra("BLOG_KEY", postKey);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int requestID = (int) System.currentTimeMillis();

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestID, intent, 0);
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(title.trim())
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.prime_launher_icon)
                    .setTicker(ticketText)
                    .setLargeIcon(smallImage)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(new long[] { 100, 1000})
                    .setLights(Color.RED, 3000, 3000)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

            if(largeIcon.equalsIgnoreCase("large_icon")) {
                noBuilder.setLargeIcon(smallImage);
            }
            else {
                noBuilder.setLargeIcon(smallImage);
                noBuilder.setStyle( new NotificationCompat.BigPictureStyle().bigPicture(largeImage));
            }
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(requestID, noBuilder.build());
        }
    }

    public class generatResponsePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {
        Bitmap largeImage, smallImage;
        private Context mContext;
        private String title, message, subtitle,largeIcon, imageUrl, ticketText, smallIcon, smallImageUrl, USER_LOGIN_TYPE, postId, patient_id, postType,doc_id,postKey ;
        public generatResponsePictureStyleNotification(Context context, String title, String message, String subtitle,String tickerText, String largeIcon, String smallIcon,String USER_LOGIN_TYPE, String postId, String patient_id, String postType, String doc_id, String postKey) {
            super();
            this.mContext = context;
            this.title = title;
            this.message = message;
            this.subtitle = subtitle;
            this.largeIcon = largeIcon;
            this.ticketText = tickerText;
            this.smallIcon = smallIcon;
            this.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            this.postId = postId;
            this.patient_id = patient_id;
            this.postType = postType;
            this.doc_id = doc_id;
            this.postKey = postKey;
        }


        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in, in1;
            try {
                URL url = new URL(smallIcon);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                smallImage = myBitmap;

                //Large Image
                URL url1 = new URL(largeIcon);
                HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                connection1.setDoInput(true);
                connection1.connect();
                in1 = connection1.getInputStream();
                largeImage = BitmapFactory.decodeStream(in1);


                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDate = sdf.format(c.getTime());

            String fileTypeArray[] = largeIcon.split("/");
            String image_name = "";
            if(fileTypeArray != null && fileTypeArray.length > 0) {
                image_name = fileTypeArray[fileTypeArray.length - 1];
            }
            Log.d(Utils.TAG, "image_name: " + image_name);

            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.putExtra("LOGIN_TYPE", USER_LOGIN_TYPE);
            intent.putExtra("ENTRY_TYPE", "RESPONSEPUSH");
            intent.putExtra("BLOG_ID", postId);
            intent.putExtra("BLOG_IMAGE", image_name);
            intent.putExtra("BLOG_TITLE", title.trim());
            intent.putExtra("BLOG_AUTHOR", "By: Dr. Encrypted_Doc_Name");
            intent.putExtra("BLOG_DATE", currentDate);
            intent.putExtra("BLOG_DESCRIPTION", message.trim());
            intent.putExtra("BLOG_TYPE", postType);
            intent.putExtra("BLOG_PATIENT_ID", patient_id);
            intent.putExtra("BLOG_REF_ID", doc_id);
            intent.putExtra("BLOG_KEY", postKey);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int requestID = (int) System.currentTimeMillis();

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestID, intent, 0);
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(title.trim())
                    .setContentText(subtitle)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.prime_launher_icon)
                    .setTicker(ticketText)
                    .setLargeIcon(smallImage)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(new long[] { 100, 1000})
                    .setLights(Color.RED, 3000, 3000)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

            if(largeIcon.equalsIgnoreCase("large_icon")) {
                noBuilder.setLargeIcon(smallImage);
            }
            else {
                noBuilder.setLargeIcon(smallImage);
                noBuilder.setStyle( new NotificationCompat.BigPictureStyle().bigPicture(largeImage));
            }
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(requestID, noBuilder.build());
        }
    }

    public class generatAppointmentPictureStyleNotification extends AsyncTask<String, Void, Bitmap> {
        Bitmap largeImage, smallImage;
        private Context mContext;
        private String title, message, subtitle,largeIcon, imageUrl, ticketText, smallIcon, smallImageUrl, USER_LOGIN_TYPE, postId, patient_id, postType,doc_id,postKey ;
        public generatAppointmentPictureStyleNotification(Context context, String title, String message, String subtitle,String tickerText, String largeIcon, String smallIcon,String USER_LOGIN_TYPE, String postId, String patient_id, String postType, String doc_id, String postKey) {
            super();
            this.mContext = context;
            this.title = title;
            this.message = message;
            this.subtitle = subtitle;
            this.largeIcon = largeIcon;
            this.ticketText = tickerText;
            this.smallIcon = smallIcon;
            this.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            this.postId = postId;
            this.patient_id = patient_id;
            this.postType = postType;
            this.doc_id = doc_id;
            this.postKey = postKey;
        }


        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in, in1;
            try {
                URL url = new URL(smallIcon);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                smallImage = myBitmap;

                //Large Image
                URL url1 = new URL(largeIcon);
                HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                connection1.setDoInput(true);
                connection1.connect();
                in1 = connection1.getInputStream();
                largeImage = BitmapFactory.decodeStream(in1);


                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDate = sdf.format(c.getTime());

            String fileTypeArray[] = largeIcon.split("/");
            String image_name = "";
            if(fileTypeArray != null && fileTypeArray.length > 0) {
                image_name = fileTypeArray[fileTypeArray.length - 1];
            }
            Log.d(Utils.TAG, "image_name: " + image_name);

            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.putExtra("LOGIN_TYPE", USER_LOGIN_TYPE);
            intent.putExtra("ENTRY_TYPE", "APPOINTMENTPUSH");
            intent.putExtra("BLOG_ID", postId);
            intent.putExtra("BLOG_IMAGE", image_name);
            intent.putExtra("BLOG_TITLE", title.trim());
            intent.putExtra("BLOG_AUTHOR", "By: Dr. Encrypted_Doc_Name");
            intent.putExtra("BLOG_DATE", currentDate);
            intent.putExtra("BLOG_DESCRIPTION", message.trim());
            intent.putExtra("BLOG_TYPE", postType);
            intent.putExtra("BLOG_PATIENT_ID", patient_id);
            intent.putExtra("BLOG_REF_ID", doc_id);
            intent.putExtra("BLOG_KEY", postKey);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int requestID = (int) System.currentTimeMillis();

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestID, intent, 0);
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(title.trim())
                    .setContentText(subtitle)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.prime_launher_icon)
                    .setTicker(ticketText)
                    .setLargeIcon(smallImage)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(new long[] { 100, 1000})
                    .setLights(Color.RED, 3000, 3000)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

            if(largeIcon.equalsIgnoreCase("large_icon")) {
                noBuilder.setLargeIcon(smallImage);
            }
            else {
                noBuilder.setLargeIcon(smallImage);
                noBuilder.setStyle( new NotificationCompat.BigPictureStyle().bigPicture(largeImage));
            }
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(requestID, noBuilder.build());
        }
    }

    private String stripHtml(String response) {
        return Html.fromHtml(response).toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
