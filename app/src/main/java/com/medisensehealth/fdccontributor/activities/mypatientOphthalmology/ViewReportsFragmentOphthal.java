package com.medisensehealth.fdccontributor.activities.mypatientOphthalmology;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medisensehealth.fdccontributor.DataModel.FundusImage;
import com.medisensehealth.fdccontributor.DataModel.ViewReports;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.multiImageUpload.ImageAdapter;
import com.medisensehealth.fdccontributor.activities.multiImageUpload.ItemOffsetDecoration;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.FundusImageAdapter;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.ReportFolderAdapter;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.parser.JSONParser;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextViewSemiBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by SALMA on 07-09-2018.
 */
public class ViewReportsFragmentOphthal extends Fragment implements View.OnClickListener {

    int USER_ID, PATIENT_ID;
    String USER_NAME,USER_LOGIN_TYPE, GET_EPISODE_LIST_TEMP;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;

    String PATIENT_NAME, PATIENT_AGE, PATIENT_GENDER, PATIENT_MOBILE, PATIENT_EMAIL, PATIENT_CITY, PATIENT_ADDRESS, PATIENT_STATE, PATIENT_COUNTRY,
            PATIENT_HEIGHT, PATIENT_WEIGHT, PATIENT_HYPERTENSION, PATIENT_DIABETES, PATIENT_SMOKING, PATIENT_ALCOHOL, PATIENT_DRUG_ABUSE, PATIENT_OTHER_DETAILS,
            PATIENT_FAMILY_HISTORY, PATIENT_PREV_INTERVENTIONS, PATIENT_NEURO_ISSUES, PATIENT_KIDNEY_ISSUES;

    LinearLayout upload_report_btn, send_link_btn;
    ImageView refresh_btn;

    private ImageAdapter imageAdapter;
    private static final int REQUEST_FOR_STORAGE_PERMISSION = 123;
    ArrayList<String> selectedItems;
    Dialog dialogUploadImage;
    public ArrayList<String> REPORT_PHOTOS = new ArrayList<String>();
    List<FundusImage> fundusImageList = new ArrayList<>();
    private FundusImageAdapter fundusImageAdapter;
    RecyclerView reports_recyclerview;

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private String userChoosenTask;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    LinearLayout upload_report_content;
    RecyclerView reportsView_recyclerview;
    CustomTextViewSemiBold submit_report_btn, cancel_report_btn;

    List<ViewReports> reportFolderList = new ArrayList<>();
    List<ViewReports> attachmentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ReportFolderAdapter mAdapter;
    ArrayList<String> report_listArray = new ArrayList<>();
    ArrayList<String> report_created_dateArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_reports, container, false);

        REPORT_PHOTOS = new ArrayList<String>();
        reportFolderList = new ArrayList<>();
        attachmentList = new ArrayList<>();
        report_listArray = new ArrayList<>();
        report_created_dateArray = new ArrayList<>();

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

            Log.d(Utils.TAG, " ********* ViewReportsFragment ************ ");
            Log.d(Utils.TAG, " PATIENT_ID: "+PATIENT_ID);
            Log.d(Utils.TAG, " PATIENT_NAME: "+PATIENT_NAME);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeViews();
    }

    private void initializeViews() {
        permissionStatus = getActivity().getSharedPreferences("permissionStatus", Context.MODE_PRIVATE);

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

        getReportsListsFromServer();

        upload_report_btn = (LinearLayout) getActivity().findViewById(R.id.view_reports_upload);
        upload_report_btn.setOnClickListener(this);
        send_link_btn = (LinearLayout) getActivity().findViewById(R.id.view_reports_sendlink);
        send_link_btn.setOnClickListener(this);
        refresh_btn = (ImageView)  getActivity().findViewById(R.id.view_reports_refresh);
        refresh_btn.setOnClickListener(this);

        reports_recyclerview = (RecyclerView) getActivity().findViewById(R.id.view_reports_upload_recyclerview);
        fundusImageAdapter = new FundusImageAdapter(getActivity(), fundusImageList, REPORT_PHOTOS, "ADD");
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        reports_recyclerview.setLayoutManager(horizontalLayoutManagaer);
        reports_recyclerview.setAdapter(fundusImageAdapter);

        upload_report_content = (LinearLayout)getActivity().findViewById(R.id.upload_report_content);
        upload_report_content.setVisibility(View.GONE);
        submit_report_btn = (CustomTextViewSemiBold)getActivity().findViewById(R.id.view_reports_submit);
        submit_report_btn.setOnClickListener(this);
        cancel_report_btn = (CustomTextViewSemiBold)getActivity().findViewById(R.id.view_reports_cancel);
        cancel_report_btn.setOnClickListener(this);
        reportsView_recyclerview = (RecyclerView)getActivity().findViewById(R.id.view_reports_recyclerview);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.view_reports_recyclerview);
        mAdapter = new ReportFolderAdapter(report_listArray,attachmentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.view_reports_upload:
                InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(upload_report_btn.getWindowToken(), 0);
                upload_report_content.setVisibility(View.VISIBLE);
                reportsView_recyclerview.setVisibility(View.GONE);
                requestAttachmentPermissions();
                break;
            case R.id.view_reports_sendlink:
                shareAttchmentLink();
                break;
            case R.id.view_reports_submit:
                InputMethodManager imm2 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(submit_report_btn.getWindowToken(), 0);
                upload_report_content.setVisibility(View.GONE);
                reportsView_recyclerview.setVisibility(View.VISIBLE);

                collectReportDetails();
                break;
            case R.id.view_reports_cancel:
                InputMethodManager imm3 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(cancel_report_btn.getWindowToken(), 0);
                upload_report_content.setVisibility(View.GONE);
                reportsView_recyclerview.setVisibility(View.VISIBLE);
                REPORT_PHOTOS = new ArrayList<String>();
                fundusImageList = new ArrayList<>();
                fundusImageAdapter = new FundusImageAdapter(getActivity(), fundusImageList, REPORT_PHOTOS, "ADD");
                reports_recyclerview.setAdapter(fundusImageAdapter);
                fundusImageAdapter.notifyDataSetChanged();
                break;
            case R.id.view_reports_refresh:
                InputMethodManager imm4 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm4.hideSoftInputFromWindow(refresh_btn.getWindowToken(), 0);
                upload_report_content.setVisibility(View.GONE);
                reportsView_recyclerview.setVisibility(View.VISIBLE);
                getReportsListsFromServer();
                break;
        }
    }

    private void shareAttchmentLink() {
        if(PATIENT_MOBILE.length()>=10) {
            String msg= "";
            try {
                Log.d(Utils.TAG, " PATIENT_ID: "+md5(String.valueOf(PATIENT_ID))+" : "+PATIENT_ID);
                String encypt_patient_id = md5(String.valueOf(PATIENT_ID));

                msg = "Hello "+PATIENT_NAME+",\nIf you have any medical reports upload here:  "+"https://medisensecrm.com/premium/Patient-Attachments?d="+encypt_patient_id+" \n\nThank You,\n"+USER_NAME;
                Log.d(Utils.TAG, " msg: "+msg);

                Intent sharingIntent = new Intent();
                sharingIntent.setAction(Intent.ACTION_SEND);
                sharingIntent.putExtra(Intent.EXTRA_TEXT,msg);
                sharingIntent.setType("text/plain");
                startActivity(Intent.createChooser(sharingIntent, "Share link via"));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        }
        else {
            Toast.makeText(getActivity(),"Patient mobile number does not exists !!!", Toast.LENGTH_SHORT).show();
        }
    }

    public static String md5(String input) throws NoSuchAlgorithmException {
        String result = input;
        if(input != null) {
            MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while(result.length() < 32) { //31 position string
                result = "0" + result;
            }
        }
        return result;
    }

    private void collectReportDetails() {
        if (REPORT_PHOTOS != null && REPORT_PHOTOS.size() > 0) {
            Log.d(Utils.TAG, " reports: "+ REPORT_PHOTOS.size());
            for(int i=0;i<REPORT_PHOTOS.size();i++) {
                Log.d(Utils.TAG, " photo: "+ REPORT_PHOTOS.get(i).toString());
            }

            submitUploadReportsToServer(REPORT_PHOTOS);
        }
    }


    private void requestAttachmentPermissions() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                AlertDialog.Builder builder1 = builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getActivity(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            editor.commit();

        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }

    private void proceedAfterPermission() {
        selectAttachementFromDevice();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getActivity(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void selectAttachementFromDevice() {
        final CharSequence[] items = {"Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Upload Attachments");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    customAttachReportsDialog();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void customAttachReportsDialog() {
        selectedItems = new ArrayList<String>();
        dialogUploadImage = new Dialog(getActivity(), R.style.DialogAnimationView);
        dialogUploadImage.setContentView(R.layout.layout_multi_photo_select);
        dialogUploadImage.setTitle("Attach Reports");
        dialogUploadImage.setCanceledOnTouchOutside(true);

        populateImagesFromGallery();

        Button select_image = (Button) dialogUploadImage.findViewById(R.id.button1);
        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChoosePhotosClick();
            }
        });

        dialogUploadImage.show();
    }

    private void populateImagesFromGallery() {

        ArrayList<String> imageUrls = loadPhotosFromNativeGallery();
        initializeRecyclerView(imageUrls);
    }

    private ArrayList<String> loadPhotosFromNativeGallery() {
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor imagecursor = getActivity().managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");

        ArrayList<String> imageUrls = new ArrayList<String>();

        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imagecursor.getString(dataColumnIndex));
            System.out.println("=====> Array path => "+imageUrls.get(i));
        }
        return imageUrls;
    }
    private void initializeRecyclerView(ArrayList<String> imageUrls) {
        imageAdapter = new ImageAdapter(getActivity(), imageUrls);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),4);
        RecyclerView recyclerView = (RecyclerView) dialogUploadImage.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ItemOffsetDecoration(getActivity(), R.dimen.item_offset));
        recyclerView.setAdapter(imageAdapter);
    }
    private void showPermissionRationaleSnackBar() {
        Snackbar.make(getActivity().findViewById(R.id.button1), "Storage permission is needed for fetching images from Gallery.",
                Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{READ_EXTERNAL_STORAGE},
                        REQUEST_FOR_STORAGE_PERMISSION);
            }
        }).show();
    }

    public void btnChoosePhotosClick(){

        selectedItems = imageAdapter.getCheckedItems();

        if (selectedItems!= null && selectedItems.size() > 0) {

            for(int i=0; i<selectedItems.size(); i++) {
                if(REPORT_PHOTOS!=null) {
                    REPORT_PHOTOS.add(selectedItems.get(i).toString());
                }
                else {
                    REPORT_PHOTOS = new ArrayList<String>();
                    REPORT_PHOTOS.add(selectedItems.get(i).toString());
                }
            }
            Toast.makeText(getActivity(),  selectedItems.size() + " Reports Selected", Toast.LENGTH_SHORT).show();
            Log.d(Utils.TAG, "Selected Items: " + selectedItems.toString());
        }
        else {
            Toast.makeText(getActivity(), "Please Select Reports", Toast.LENGTH_SHORT).show();
        }

        if (REPORT_PHOTOS != null && REPORT_PHOTOS.size() > 0) {
            fundusImageList = new ArrayList<>();
            Log.d(Utils.TAG + "PH SIZE:", String.valueOf(REPORT_PHOTOS.size()));
            ArrayList aList = new ArrayList(Arrays.asList(REPORT_PHOTOS.toString().substring(1, REPORT_PHOTOS.toString().length() - 1).toString().split(",")));
            for (int i = 0; i < aList.size(); i++) {
                System.out.println(Utils.TAG + " -->" + aList.get(i));
                fundusImageList.add(new FundusImage(aList.get(i).toString()));
            }

            fundusImageAdapter = new FundusImageAdapter(getActivity(), fundusImageList, REPORT_PHOTOS, "ADD");
            reports_recyclerview.setAdapter(fundusImageAdapter);
            fundusImageAdapter.notifyDataSetChanged();
        }

        dialogUploadImage.dismiss();
    }


    private void getReportsListsFromServer() {
        reportFolderList = new ArrayList<>();
        attachmentList = new ArrayList<>();
        report_listArray = new ArrayList<>();
        report_created_dateArray = new ArrayList<>();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading attachments...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_MYPATIENT_REPORTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // progress_bar.setVisibility(View.GONE);
                        Log.d(Utils.TAG, "reports: "+ response.toString());
                        JSONArray jsonArray, jsonArray1;
                        ArrayList<String> list = new ArrayList<>();
                        ArrayList<String> list_date = new ArrayList<>();
                        if (response != null) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("result");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("reports_details");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        attachmentList.add(new ViewReports(jsonArray.getJSONObject(i).getInt("report_id"),
                                                jsonArray.getJSONObject(i).getInt("patient_id"),jsonArray.getJSONObject(i).getString("report_folder"),
                                                jsonArray.getJSONObject(i).getString("attachments"),jsonArray.getJSONObject(i).getInt("user_id"),
                                                jsonArray.getJSONObject(i).getInt("user_type"),jsonArray.getJSONObject(i).getString("date_added"),
                                                jsonArray.getJSONObject(i).getString("username")));

                                        Log.d(Utils.TAG, " not contains: "+ jsonArray.getJSONObject(i).getString("report_folder"));

                                        list.add(jsonArray.getJSONObject(i).getString("report_folder"));

                                    }
                                    Log.d(Utils.TAG, " attachmentList: "+ attachmentList.size());

                                    // Remove duplicates from ArrayList of Strings.
                                    report_listArray = removeDuplicates(list);
                                    for (String element : report_listArray) {
                                        System.out.println(element);
                                    }

                                    mAdapter = new ReportFolderAdapter(report_listArray,attachmentList);
                                    recyclerView.setAdapter(mAdapter);
                                    mAdapter.notifyDataSetChanged();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.d(Utils.TAG, "error: "+ error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                map.put(APIClass.KEY_PATIENTID, String.valueOf(PATIENT_ID));
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(getContext()).
                getRequestQueue();
        AppController.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    static ArrayList<String> removeDuplicates(ArrayList<String> list) {

        // Store unique items in result.
        ArrayList<String> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        HashSet<String> set = new HashSet<>();

        // Loop over argument list.
        for (String item : list) {

            // If String is not in set, add it to the list and the set.
            if (!set.contains(item)) {
                result.add(item);
                set.add(item);
            }
        }

        return result;
    }

    private void submitUploadReportsToServer(ArrayList<String> report_photos) {
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

                    JSONObject jsonObject = JSONParser.uploadVisitReports(PATIENT_ID, REPORT_PHOTOS, USER_ID, USER_LOGIN_TYPE);

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
                    alertDialogBuilder.setMessage("Reports uploaded successfully. ");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    REPORT_PHOTOS = new ArrayList<String>();
                                    fundusImageList = new ArrayList<>();
                                    fundusImageAdapter = new FundusImageAdapter(getActivity(), fundusImageList, REPORT_PHOTOS, "ADD");
                                    reports_recyclerview.setAdapter(fundusImageAdapter);
                                    fundusImageAdapter.notifyDataSetChanged();
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
}
