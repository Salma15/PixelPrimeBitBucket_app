package com.medisensehealth.fdccontributor.activities.patients;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.DashboardActivity;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.TouchImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by lenovo on 23/05/2017.
 */
public class PatientsAttchmentFragment extends Fragment {
    FragmentManager fragManager;
    FragmentTransaction fragTransaction;
    int PATIENT_ID, ATTACH_ID, DOCREF_ID;
    String DOWNLOAD_URL;
    String Patient_View;
    TouchImageView imageView;
    ProgressDialog progressDialog;

    public static String USER_NAME, USER_LOGIN_TYPE;
    int USER_ID;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;

    boolean isImageFitToScreen;


    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_atachview, vg, false);

        root.setFocusableInTouchMode(true);
        root.requestFocus();

        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if(Patient_View.equals("PUSHVIEW")) {
                            Intent intent = new Intent(getActivity(), DashboardActivity.class);
                            intent.putExtra("LOGIN_TYPE", USER_LOGIN_TYPE);
                            intent.putExtra("USER_ID", USER_ID);
                            intent.putExtra("ENTRY_TYPE", "NORMAL");
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else {
                            getActivity().finish();
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initialize all the view
        fragManager = getActivity().getSupportFragmentManager();
        Bundle bundle = getArguments();
        if (bundle != null) {
            PATIENT_ID = bundle.getInt("PATIENT_ID", 0);
            ATTACH_ID = bundle.getInt("ATTTACH_ID", 0);
            DOCREF_ID  = bundle.getInt("DOCREF_ID", 0);
            DOWNLOAD_URL  = bundle.getString("DOWNLOAD_URL", "");
            Patient_View = bundle.getString("PATIENT_VIEW", "VIEW");

            Log.d(Utils.TAG,"PATIENT_ID: "+ String.valueOf(PATIENT_ID));
            Log.d(Utils.TAG,"ATTACH_ID: "+ String.valueOf(ATTACH_ID));
            Log.d(Utils.TAG,"DOCREF_ID: "+ String.valueOf(DOCREF_ID));
            Log.d(Utils.TAG,"DOWNLOAD_URL: "+ String.valueOf(DOWNLOAD_URL));
            Log.d(Utils.TAG,"Patient_View: "+ String.valueOf(Patient_View));
        }

        shareadPreferenceClass = new ShareadPreferenceClass(getActivity());
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(getActivity());
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

        initislizations();
    }

    private void initislizations() {
        Log.d(Utils.TAG,"************* initislizations ********************* ");
        imageView = (TouchImageView) getActivity().findViewById(R.id.image_attachview);

        progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading attachment...");
        progressDialog.show();


      //  Picasso.with(getActivity()).load(DOWNLOAD_URL).fit().into(imageView);
     //   new DownloadFileFromURL().execute(DOWNLOAD_URL);

        Picasso.with(getActivity()).load(String.valueOf(DOWNLOAD_URL))
                .placeholder(R.drawable.blogs_empty_img)
                .error(R.drawable.blogs_empty_img)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError() {
                        progressDialog.dismiss();
                    }
                });

      /*  imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isImageFitToScreen) {
                    isImageFitToScreen=false;
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    imageView.setAdjustViewBounds(true);
                }else{
                    isImageFitToScreen=true;
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        });*/
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        String downloaded_path;
        /**
         * Before starting background thread
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");
            progressDialog.show();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            URL url = null;
            try {
                url = new URL(f_url[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();
                String[] path = url.getPath().split("/");
                String attachments = path[path.length- 1];
                int lengthOfFile = c.getContentLength();

                String PATH = Environment.getExternalStorageDirectory()+ "/DownLoad/" ;
                Log.v(Utils.TAG, "PATH: " + PATH);
                File file = new File(PATH);
                file.mkdirs();

                String fileName = attachments;
                downloaded_path = PATH+fileName;

                File outputFile = new File(file , fileName);
                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {

                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (ProtocolException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }



            return downloaded_path;
        }



        /**
         * After completing background task
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            System.out.println(Utils.TAG+ "Downloaded");
            System.out.println(Utils.TAG+ "file_url " + file_url);
            System.out.println(Utils.TAG+ "length " + file_url.length());


            File imgFile = new  File(downloaded_path);

            if((file_url.equals("")) || (file_url == null) || (file_url.length() == 0)) {
                progressDialog.dismiss();
                AppUtils.showCustomErrorMessage(getActivity(),"Attachment","Unable to download image", "OK", null, null);
            }
            else {

                if(imgFile.exists()){

               //     Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    imageView.setVisibility(View.VISIBLE);
                   // imageView.setImageBitmap(myBitmap);
                  //  Picasso.with(getActivity()).load(DOWNLOAD_URL).fit().into(imageView);

                    String urlStr = DOWNLOAD_URL;
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

                    Picasso.with(getActivity()).load(String.valueOf(url))
                            .placeholder(R.drawable.blogs_empty_img)
                            .error(R.drawable.blogs_empty_img)
                            .fit()
                            .into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    progressDialog.dismiss();
                                }
                                @Override
                                public void onError() {
                                    progressDialog.dismiss();
                                }
                            });

                   /* Picasso.with(getActivity())
                            .load(String.valueOf(url))
                            .into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    dialog.dismiss();
                                    Picasso.with(getActivity()).load(DOWNLOAD_URL).fit().into(imageView);
                                }

                                @Override
                                public void onError() {
                                    dialog.dismiss();
                                }
                            });*/
                }
            }

        }

    }
}
