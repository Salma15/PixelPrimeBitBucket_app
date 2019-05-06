package com.medisensehealth.fdccontributor.activities.mypatientOphthalmology;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.medisensehealth.fdccontributor.DataModel.FundusImage;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.FundusImageAdapter;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.FundusImageGalleryAdapter;
import com.medisensehealth.fdccontributor.parser.JSONParser;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by SALMA on 22-12-2018.
 */
public class FundusImagesFragment extends Fragment {

    int USER_ID, PATIENT_ID;
    String USER_NAME,USER_LOGIN_TYPE;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;

    String PATIENT_NAME, PATIENT_AGE, PATIENT_GENDER, PATIENT_MOBILE, PATIENT_EMAIL, PATIENT_CITY, PATIENT_ADDRESS, PATIENT_STATE, PATIENT_COUNTRY,
            PATIENT_HEIGHT, PATIENT_WEIGHT, PATIENT_HYPERTENSION, PATIENT_DIABETES, PATIENT_SMOKING, PATIENT_ALCOHOL, PATIENT_DRUG_ABUSE, PATIENT_OTHER_DETAILS,
            PATIENT_FAMILY_HISTORY, PATIENT_PREV_INTERVENTIONS, PATIENT_NEURO_ISSUES, PATIENT_KIDNEY_ISSUES;

    private Button takePictureButton, submitButton;
    private ImageView imageView;
    private Uri file;
    public ArrayList<String> FUNDUS_IMAGES_LIST = new ArrayList<String>();
    RecyclerView fundus_image_recyclerview;
    private FundusImageGalleryAdapter fundusImageAdapter;
    List<FundusImage> fundusImageList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fundus_image, container, false);

        FUNDUS_IMAGES_LIST = new ArrayList<String>();
        fundusImageList = new ArrayList<>();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            PATIENT_ID = bundle.getInt("PATIENT_ID", 0);
            PATIENT_NAME = bundle.getString("PATIENT_NAME");
            PATIENT_AGE = bundle.getString("PATIENT_AGE");
            PATIENT_GENDER = bundle.getString("PATIENT_GENDER");
            PATIENT_MOBILE = bundle.getString("PATIENT_MOBILE");
            PATIENT_EMAIL = bundle.getString("PATIENT_EMAIL");
            PATIENT_CITY = bundle.getString("PATIENT_CITY");
            PATIENT_ADDRESS = bundle.getString("PATIENT_ADDRESS");
            PATIENT_STATE = bundle.getString("PATIENT_STATE");
            PATIENT_COUNTRY = bundle.getString("PATIENT_COUNTRY_NAME");

            PATIENT_HEIGHT = bundle.getString("PATIENT_HEIGHT");
            PATIENT_WEIGHT = bundle.getString("PATIENT_WEIGHT");
            PATIENT_HYPERTENSION = bundle.getString("PATIENT_HYPERTENSION");
            PATIENT_DIABETES = bundle.getString("PATIENT_DIABETES");
            PATIENT_SMOKING = bundle.getString("PATIENT_SMOKING");
            PATIENT_ALCOHOL = bundle.getString("PATIENT_ALCOHOL");
            PATIENT_DRUG_ABUSE = bundle.getString("PATIENT_DRUG_ABUSE");
            PATIENT_OTHER_DETAILS = bundle.getString("PATIENT_OTHER_DETAILS");
            PATIENT_FAMILY_HISTORY  = bundle.getString("PATIENT_FAMILY_HISTORY");
            PATIENT_PREV_INTERVENTIONS  = bundle.getString("PATIENT_PREV_INTERVENTIONS");
            PATIENT_NEURO_ISSUES = bundle.getString("PATIENT_NEURO_ISSUES");
            PATIENT_KIDNEY_ISSUES = bundle.getString("PATIENT_KIDNEY_ISSUES");

            Log.d(Utils.TAG, " ********* FundusImagesFragment ************ ");
            Log.d(Utils.TAG, " PATIENT_ID: "+PATIENT_ID);
            Log.d(Utils.TAG, " PATIENT_NAME: "+PATIENT_NAME);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       // initializeViews();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    private void initializeViews() {

        shareadPreferenceClass = new ShareadPreferenceClass(getActivity());
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(getActivity());

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
        }
        Log.d(Utils.TAG, " USER_ID: "+USER_ID);
        Log.d(Utils.TAG, " USER_NAME: "+USER_NAME);
        Log.d(Utils.TAG, " LOGIN_TYPE: "+USER_LOGIN_TYPE);

        takePictureButton = (Button) getActivity().findViewById(R.id.fundus_image_takephoto);
        imageView = (ImageView) getActivity().findViewById(R.id.fundus_image_disp);

        fundus_image_recyclerview = (RecyclerView) getActivity().findViewById(R.id.fundus_image_recyclerview);
        fundusImageAdapter = new FundusImageGalleryAdapter(getActivity(), fundusImageList, FUNDUS_IMAGES_LIST);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        fundus_image_recyclerview.setLayoutManager(horizontalLayoutManagaer);
        fundus_image_recyclerview.setAdapter(fundusImageAdapter);

        if (ContextCompat.checkSelfPermission(getActivity(),  Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = Uri.fromFile(getOutputMediaFile());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

                startActivityForResult(intent, 100);
            }
        });

        submitButton = (Button) getActivity().findViewById(R.id.fundus_image_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm2 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(submitButton.getWindowToken(), 0);

                if (FUNDUS_IMAGES_LIST != null && FUNDUS_IMAGES_LIST.size() > 0) {
                    Log.d(Utils.TAG, " reports: "+ FUNDUS_IMAGES_LIST.size());
                    for(int i=0;i<FUNDUS_IMAGES_LIST.size();i++) {
                        Log.d(Utils.TAG, " photo: "+ FUNDUS_IMAGES_LIST.get(i).toString());
                    }

                    submitUploadReportsToServer(FUNDUS_IMAGES_LIST);
                }
                else {
                    Toast.makeText(getActivity(), "No Fundus Images to upload !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.d("CameraDemo", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
              //  imageView.setImageURI(file);

                FUNDUS_IMAGES_LIST.add(file.getPath().toString());

                continueRefreshIFundusImages(FUNDUS_IMAGES_LIST);
            }
        }
    }

    private void continueRefreshIFundusImages(ArrayList<String> fundus_images_list) {


        fundusImageList = new ArrayList<>();
        for(int i=0; i<fundus_images_list.size();i++) {
            Log.d(Utils.TAG, " filePath: "+fundus_images_list.get(i).toString());

            fundusImageList.add(new FundusImage(fundus_images_list.get(i).toString()));

           /* fundusImageAdapter = new FundusImageGalleryAdapter(getActivity(), fundusImageList, FUNDUS_IMAGES_LIST);
            fundus_image_recyclerview.setAdapter(fundusImageAdapter);
            fundusImageAdapter.notifyDataSetChanged();*/
        }

        fundusImageAdapter = new FundusImageGalleryAdapter(getActivity(), fundusImageList, FUNDUS_IMAGES_LIST);
        fundus_image_recyclerview.setAdapter(fundusImageAdapter);
        fundusImageAdapter.notifyDataSetChanged();
    }

    private void submitUploadReportsToServer(ArrayList<String> fundus_images_list) {

        new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {

                    JSONObject jsonObject = JSONParser.uploadVisitReports(PATIENT_ID, FUNDUS_IMAGES_LIST, USER_ID, USER_LOGIN_TYPE);

                    if (jsonObject != null) {
                        Log.e(Utils.TAG, " GET: " + jsonObject.getString("result"));
                        if (jsonObject.getString("result").equals("success")) {
                            status = true;
                        } else {
                            status = false;
                        }
                    }
                    // return true;
                } catch (JSONException e) {
                    Log.i(Utils.TAG, "Error : " + e.getLocalizedMessage());
                    return false;
                }
                return status;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (progressDialog != null)
                    progressDialog.dismiss();
                Log.e(Utils.TAG, "aBoolean: " + String.valueOf(aBoolean));
                if (aBoolean) {

                    if (sharedPreferences != null) {
                        shareadPreferenceClass.clearsetMyPatientEpisodes();
                    }
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
                    alertDialogBuilder.setMessage("Fundus Image uploaded successfully. ");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(getActivity(), "Failed to upload reports !!!", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e(Utils.TAG, " onResume: " );

        initializeViews();
    }
}
