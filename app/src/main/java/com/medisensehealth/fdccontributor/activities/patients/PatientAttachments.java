package com.medisensehealth.fdccontributor.activities.patients;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medisensehealth.fdccontributor.DataModel.PatientAttachmentHistory;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.DashboardActivity;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.FileDownloader;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.TouchImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 08-03-2017.
 */
public class PatientAttachments extends Fragment {
    int Patient_ID, DOCREF_ID;
    String Patient_View;
    TextView attach_status;
    ListView attach_list;
    ArrayList<PatientAttachmentHistory> patientAttachmentArraylist;
    int count_attach = 0, attach_id, patientid;
    String attachments;
    private String DOWNLOAD_URL;
    ArrayAdapter<String> adapter;
    private ArrayList<String> data;

    TouchImageView attached_file_image;
    FragmentManager fragManager;
    FragmentTransaction fragTransaction;
    Toolbar toolbar;
    Bitmap bitmap_image;
    private boolean zoomOut = false;
    ProgressDialog progressDialog;

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    public static String USER_NAME, USER_LOGIN_TYPE;
    int USER_ID;

    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_patient_attach, vg, false);

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
        fragManager = getActivity().getSupportFragmentManager();


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Patient_ID = bundle.getInt("PATIENT_ID", 0);
            DOCREF_ID = bundle.getInt("DOCREF_ID", 0);
            Patient_View = bundle.getString("PATIENT_VIEW", "VIEW");
            Log.d(Utils.TAG + " Att Pat Id:", String.valueOf(Patient_ID));
            Log.d(Utils.TAG + " Att Ref Id:", String.valueOf(DOCREF_ID));
            Log.d(Utils.TAG + " Att Pat :", String.valueOf(Patient_View));
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

        initislization();
    }

    private void initislization() {
        attach_status = (TextView) getActivity().findViewById(R.id.pat_attach_status);
        attach_list = (ListView) getActivity().findViewById(R.id.pat_attach_lists);
        //  progress_bar = (ProgressBar) getActivity().findViewById(R.id.progress_bar);
        attached_file_image = (TouchImageView) getActivity().findViewById(R.id.image_attach);
        attach_status.setVisibility(View.GONE);
        attach_list.setVisibility(View.GONE);
        attached_file_image.setVisibility(View.GONE);

        setRequsetAttachmenetServer();
    }

    private void setRequsetAttachmenetServer() {
        //  progress_bar.setVisibility(View.VISIBLE);

        progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading attachments...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PATIENT_ATTACHMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // progress_bar.setVisibility(View.GONE);
                        Log.d(Utils.TAG, response.toString());
                        if (response != null) {
                            GetJsonArrayResponse(response);
                        } else {
                            progressDialog.dismiss();
                            // progress_bar.setVisibility(View.GONE);
                            //	AppUtil.showCustomAlert(getContext(), "Server Error!!!","You Can Try it later", "OK", null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                     //   AppUtil.showCustomAlert(getContext(), "Warning!!!", "You Can Try it later", "OK", null);
                        //   Log.d(Utils.TAG+"ERR",error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                map.put(APIClass.KEY_PATIENTID, String.valueOf(Patient_ID));
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(getContext()).
                getRequestQueue();
        AppController.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void GetJsonArrayResponse(String response) {

        try {
            patientAttachmentArraylist = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(response);
            String staus_res = jsonObject.getString("status");
            Log.d(Utils.TAG, staus_res);

            if (staus_res.equals("false")) {
                attach_list.setVisibility(View.GONE);
                attach_status.setVisibility(View.VISIBLE);
                attach_status.setText("No reports attached !!!");
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("attachment_count");
                count_attach = jsonArray.getJSONObject(0).getInt("count");
                Log.d(Utils.TAG + " count: ", String.valueOf(jsonArray.length()));

                if (count_attach >= 1) {

                    JSONArray jsonArray1 = jsonObject.getJSONArray("attachment_details");
                    if (jsonArray1.length() > 0) {
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            attach_id = jsonArray1.getJSONObject(i).getInt("attch_id");
                            patientid = jsonArray1.getJSONObject(i).getInt("patient_id");
                            attachments = jsonArray1.getJSONObject(i).getString("attachments");

                            attach_status.setVisibility(View.GONE);
                            attach_list.setVisibility(View.VISIBLE);
                            Log.d(Utils.TAG, String.valueOf(attach_id));
                            Log.d(Utils.TAG, String.valueOf(patientid));
                            Log.d(Utils.TAG, attachments);

                            patientAttachmentArraylist.add(new PatientAttachmentHistory(jsonArray1.getJSONObject(i).getInt("attch_id"), jsonArray1.getJSONObject(i).getString("attachments"), jsonArray1.getJSONObject(i).getInt("patient_id"),Patient_View));

                        }
                    }
                } else {
                    // No records
                    attach_status.setVisibility(View.VISIBLE);
                    attach_status.setText("No Attachments Found !!!");

                }
            }

            progressDialog.dismiss();


        } catch (JSONException e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }


        attach_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) attach_list.getItemAtPosition(position);

                // Show Alert
                //   Toast.makeText(getActivity(),"Position :"+itemPosition+"Attach Id: "+patientAttachmentArraylist.get(position).getAttachId()+"File : " +itemValue , Toast.LENGTH_SHORT).show();
                Log.d(Utils.TAG, "Attach id:" + patientAttachmentArraylist.get(position).getAttachId());
                attach_id = patientAttachmentArraylist.get(position).getAttachId();
                DOWNLOAD_URL = APIClass.DRS_PATIENT_ATTACHMENT_URL + attach_id + "/" + itemValue;
                Log.d(Utils.TAG, "Downloading URL: " + DOWNLOAD_URL);

                String filepath = DOWNLOAD_URL.substring(DOWNLOAD_URL.lastIndexOf("."));
                Log.d(Utils.TAG, "filepath: " + filepath);

                if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {

                    if ((filepath.equalsIgnoreCase(".jpg")) || (filepath.equalsIgnoreCase(".jpeg")) || (filepath.equalsIgnoreCase(".png"))) {

                        Bundle bundle = new Bundle();
                        bundle.putInt("PATIENT_ID", Patient_ID);
                        bundle.putInt("DOCREF_ID", DOCREF_ID);
                        bundle.putInt("ATTTACH_ID", patientAttachmentArraylist.get(position).getAttachId());
                        bundle.putString("DOWNLOAD_URL", DOWNLOAD_URL);
                        bundle.putString("PATIENT_VIEW", patientAttachmentArraylist.get(position).getAttachViewType());
                        PatientsAttchmentFragment attachFragment = new PatientsAttchmentFragment();
                        fragManager = getFragmentManager();
                        fragTransaction = fragManager.beginTransaction();
                        attachFragment.setArguments(bundle);
                        fragTransaction.replace(R.id.viewpager, attachFragment, "ATTACH");
                        fragTransaction.commit();

                    }
                    else if ((filepath.equals(".pdf"))) {
                        downloadPDFfiles(DOWNLOAD_URL);
                    }
                    else {
                        AppUtils.showCustomAlertMessage(getContext(), "Attachment", "Unsupported File Format", "OK", null, null);
                    }
                } else {
                    AppUtils.showCustomAlertMessage(getContext(), HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }

            }
        });
        CollectAttachments();
    }

    private void CollectAttachments() {
        Log.d(Utils.TAG, "CollectAttachments: ");
        Log.d(Utils.TAG, "Size: " + String.valueOf(patientAttachmentArraylist.size()));

        //   data = new ArrayList<PatientAttachmentHistory>();
        data = new ArrayList<String>();

        for (int i = 0; i < patientAttachmentArraylist.size(); i++) {
            //  data = new PatientAttachmentHistory(patientAttachmentArraylist.get(i).getAttachName());
            data.add(patientAttachmentArraylist.get(i).getAttachName());
            Log.d(Utils.TAG, patientAttachmentArraylist.get(i).getAttachName().toString());
        }


        // specify the list adaptor
        adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_list_item_1, data) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.BLACK);

                // Generate ListView Item using TextView
                return view;
            }
        };

        attach_list.setAdapter(adapter);
    }

    private void downloadPDFfiles(final String download_url) {

        Log.d(Utils.TAG, "downloadPDFfiles: " + download_url);

       // new DownloadFile().execute("http://maven.apache.org/maven-1.x/maven.pdf", "maven.pdf");

        int pos = download_url.lastIndexOf('/') + 1;
        String  urlString = download_url.substring(0, pos) + Uri.encode(download_url.substring(pos));
        Log.d(Utils.TAG, "urlString: " + urlString);

        new DownloadFile().execute(urlString, "patientAttach.pdf");


    }

    private class DownloadFile extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "Medisense");
            folder.mkdir();

//            File pdfFile = new File(folder, fileName);

            try{
                File pdfFile = new File(folder, fileName);

                if(pdfFile!= null){
                    pdfFile.createNewFile();
                }
                FileDownloader.downloadFile(fileUrl, pdfFile);
                showPdf();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }


    public void showPdf()
    {
        File pdfFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Medisense/" + "patientAttach.pdf");  // -> filename = maven.pdf

        if(pdfFile.exists()) {
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(getActivity(), "File path is incorrect." , Toast.LENGTH_LONG).show();
    }

}