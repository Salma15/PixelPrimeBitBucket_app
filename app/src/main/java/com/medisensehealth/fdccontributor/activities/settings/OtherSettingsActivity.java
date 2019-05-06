package com.medisensehealth.fdccontributor.activities.settings;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medisensehealth.fdccontributor.DataModel.OtherSettings;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.parser.JSONParser;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomEditText;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 27-07-2018.
 */

public class OtherSettingsActivity extends AppCompatActivity implements View.OnClickListener {
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME, USER_LOGIN_TYPE, OTHER_SETTINGS_LIST;

    RadioGroup rg_payment, rg_preprint, rg_consult_fees;
    RadioButton rb_paymentEnable, rb_paymentDisable, rb_preprintEnable, rb_preprintDisable, rb_consultFeesYes,rb_consultFeesNo;
    CustomTextViewBold upload_image_btn;
    ImageView upload_image, refresh_btn;
    CustomEditText _edt_flash_message, _edt_header_height, _edt_footer_height;
    Button submit_btn;
    LinearLayout preprint_padlayout;

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    private String userChoosenTask;
    private static final int PICK_IMAGE = 1;
    private Bitmap bitmap;
    String GET_IMAGEPATH = "", GET_PAYMENT_STATUS, GET_PREPRINT, GET_CONSULT_FEES;

    List<OtherSettings> otherSettingsListArraylist = new ArrayList<>();
    Gson gson;

    private static final int SELECT_FILE = 123;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_other_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);

            Log.d(Utils.TAG, " ************ OtherSettingsActivity *********** ");
        }

        shareadPreferenceClass = new ShareadPreferenceClass(OtherSettingsActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(OtherSettingsActivity.this);


        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            OTHER_SETTINGS_LIST = sharedPreferences.getString(HCConstants.PREF_OTHER_SETTINGS,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            OTHER_SETTINGS_LIST = sharedPreferences.getString(HCConstants.PREF_OTHER_SETTINGS,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            OTHER_SETTINGS_LIST = sharedPreferences.getString(HCConstants.PREF_OTHER_SETTINGS,"");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        Log.d(Utils.TAG , " *********** ReceptionistActivity ****************");
        Log.d(Utils.TAG +" UserId: ", String.valueOf(Utils.USER_LOGIN_ID));
        Log.d(Utils.TAG +" loginType: ", Utils.USER_LOGIN_TYPE);

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
        otherSettingsListArraylist = new ArrayList<>();
        permissionStatus = getSharedPreferences("permissionStatus", Context.MODE_PRIVATE);

        rg_payment = (RadioGroup) findViewById(R.id.rg_PaymentSection);
        rb_paymentEnable = (RadioButton) findViewById(R.id.rb_paymentEnable);
        rb_paymentDisable = (RadioButton) findViewById(R.id.rb_paymentDisable);

        rg_preprint = (RadioGroup) findViewById(R.id.rg_PrescPad);
        rb_preprintEnable = (RadioButton) findViewById(R.id.rb_PrescPadEnable);
        rb_preprintDisable = (RadioButton) findViewById(R.id.rb_PrescPadDisable);

        rg_consult_fees = (RadioGroup) findViewById(R.id.rg_ConsultFees);
        rb_consultFeesYes = (RadioButton) findViewById(R.id.rb_ConsultFeesYes);
        rb_consultFeesNo = (RadioButton) findViewById(R.id.rb_ConsultFeesNo);

        upload_image_btn = (CustomTextViewBold) findViewById(R.id.other_uploadimage);
        upload_image_btn.setOnClickListener(this);
        upload_image = (ImageView)findViewById(R.id.other_image);
        _edt_flash_message = (CustomEditText) findViewById(R.id.other_flash_messgae);
        submit_btn = (Button) findViewById(R.id.other_submit);
        submit_btn.setOnClickListener(this);

        preprint_padlayout = (LinearLayout) findViewById(R.id.preprint_pad_layout);
        preprint_padlayout.setVisibility(View.GONE);
        _edt_header_height = (CustomEditText) findViewById(R.id.preprint_header_height);
        _edt_footer_height= (CustomEditText) findViewById(R.id.preprint_footer_height);

        refresh_btn = (ImageView) findViewById(R.id.refresh_btn);
        refresh_btn.setOnClickListener(this);

        rg_preprint.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_PrescPadEnable:
                        preprint_padlayout.setVisibility(View.VISIBLE);
                        return;
                    case R.id.rb_PrescPadDisable:
                        preprint_padlayout.setVisibility(View.GONE);
                        return;
                    default:
                        return;
                }
            }
        });

        gson = new Gson();
        if (OTHER_SETTINGS_LIST.equals("")) {
            Log.d(Utils.TAG, "OTHER_SETTINGS_LIST EMPTY ");

            if (NetworkUtil.getConnectivityStatusString(OtherSettingsActivity.this).equalsIgnoreCase("enabled")) {
                collectOtherSettingsListFromServer();
            } else {
                AppUtils.showCustomAlertMessage(OtherSettingsActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }


        } else {
            otherSettingsListArraylist = gson.fromJson(OTHER_SETTINGS_LIST, new TypeToken<List<OtherSettings>>() {
            }.getType());
            if(otherSettingsListArraylist.size() > 0 ) {
                Log.d(Utils.TAG, "OTHER_SETTINGS_LIST > 0 " + otherSettingsListArraylist.size());
                prepareSettingsData(otherSettingsListArraylist);
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.other_submit:
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(submit_btn.getWindowToken(), 0);
                validateSubmitForm();
                break;
            case R.id.other_uploadimage:
                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(upload_image_btn.getWindowToken(), 0);
                upload_image.setVisibility(View.VISIBLE);
                requestAttachmentPermissions();
                break;
            case R.id.refresh_btn:
                InputMethodManager imm3 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(refresh_btn.getWindowToken(), 0);
                if (NetworkUtil.getConnectivityStatusString(OtherSettingsActivity.this).equalsIgnoreCase("enabled")) {
                    collectOtherSettingsListFromServer();
                } else {
                    AppUtils.showCustomAlertMessage(OtherSettingsActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
                break;
        }
    }

/*    private void requestAttachmentPermissions() {
        if (ActivityCompat.checkSelfPermission(OtherSettingsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(OtherSettingsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(OtherSettingsActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(OtherSettingsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(OtherSettingsActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                AlertDialog.Builder builder1 = builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", OtherSettingsActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(OtherSettingsActivity.this, "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
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
                ActivityCompat.requestPermissions(OtherSettingsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,true);
            editor.commit();

        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }

    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
        //   Toast.makeText(EditProfileActivity.this, "We got the Storage Permission", Toast.LENGTH_LONG).show();
        uplaodImage();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(OtherSettingsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(OtherSettingsActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(OtherSettingsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);


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
                    Toast.makeText(OtherSettingsActivity.this,"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImageUri = null;
        String filePath = null;
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(OtherSettingsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }

        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    selectedImageUri = data.getData();
                }
                break;
        }

        if(selectedImageUri != null){
            try {
                // OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri);
                GET_IMAGEPATH = getPath(selectedImageUri);
                Log.e(Utils.TAG, "GET_IMAGEPATH: "+ GET_IMAGEPATH);

                if (selectedImagePath != null) {
                    filePath = selectedImagePath;
                } else if (filemanagerstring != null) {
                    filePath = filemanagerstring;
                } else {
                    Toast.makeText(OtherSettingsActivity.this, "Unknown path",Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                if (filePath != null) {
                    decodeFile(filePath);
                } else {
                    bitmap = null;
                }
            } catch (Exception e) {
                Toast.makeText(OtherSettingsActivity.this, "Internal error", Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index;
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        upload_image.setImageBitmap(bitmap);
    }

    private void uplaodImage() {
        try {
            Intent gintent = new Intent();
            gintent.setType("image*//*");
            gintent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(gintent, "Select Picture"),
                    PICK_IMAGE);
        } catch (Exception e) {
            Toast.makeText(OtherSettingsActivity.this, e.getMessage(),  Toast.LENGTH_SHORT).show();
            Log.e(e.getClass().getName(), e.getMessage(), e);
        }
    }*/

    private void validateSubmitForm() {
        Log.d(Utils.TAG, " ****************** validateSubmitForm ************************");

        int checkedRadioButtonId = rg_payment.getCheckedRadioButtonId();        // 1- ENABLE, 2 -DISABLE
        if (checkedRadioButtonId == -1) {   // No items Selected
            GET_PAYMENT_STATUS = "0";
        }
        else{
            if (checkedRadioButtonId == R.id.rb_paymentEnable) {
                GET_PAYMENT_STATUS = "1";
            }
            else if (checkedRadioButtonId == R.id.rb_paymentDisable) {
                GET_PAYMENT_STATUS = "2";
            }
            else {
                GET_PAYMENT_STATUS = "0";
            }
        }

        int checkedRadioButtonId1 = rg_preprint.getCheckedRadioButtonId();        // 1- ENABLE, 2 -DISABLE
        if (checkedRadioButtonId1 == -1) {   // No items Selected
            GET_PREPRINT = "0";
        }
        else{
            if (checkedRadioButtonId1 == R.id.rb_PrescPadEnable) {
                GET_PREPRINT = "1";
            }
            else if (checkedRadioButtonId1 == R.id.rb_PrescPadDisable) {
                GET_PREPRINT = "2";
            }
            else {
                GET_PREPRINT = "0";
            }
        }

        int checkedRadioButtonId2 = rg_consult_fees.getCheckedRadioButtonId();        // 1- YES, 2 -NO
        if (checkedRadioButtonId2 == -1) {   // No items Selected
            GET_CONSULT_FEES = "0";
        }
        else{
            if (checkedRadioButtonId2 == R.id.rb_ConsultFeesYes) {
                GET_CONSULT_FEES = "1";
            }
            else if (checkedRadioButtonId2 == R.id.rb_ConsultFeesNo) {
                GET_CONSULT_FEES = "2";
            }
            else {
                GET_CONSULT_FEES = "0";
            }
        }

        String flash_message = _edt_flash_message.getText().toString().trim();
        String header_height = _edt_header_height.getText().toString();
        String footer_height = _edt_footer_height.getText().toString();

        Log.d(Utils.TAG, " GET_PAYMENT_STATUS: "+GET_PAYMENT_STATUS);
        Log.d(Utils.TAG, " GET_PREPRINT: "+GET_PREPRINT);
        Log.d(Utils.TAG, " GET_CONSULT_FEES: "+GET_CONSULT_FEES);
        Log.d(Utils.TAG, " GET_IMAGEPATH: "+GET_IMAGEPATH);
        Log.d(Utils.TAG, " flash_message: "+flash_message);

        if (NetworkUtil.getConnectivityStatusString(this).equalsIgnoreCase("enabled")) {
            submitRequestToServer(GET_PAYMENT_STATUS, GET_PREPRINT, GET_CONSULT_FEES, GET_IMAGEPATH, flash_message,header_height,footer_height);
        } else {
            AppUtils.showCustomAlertMessage(OtherSettingsActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
        }
    }

    private void submitRequestToServer(final String get_payment_status, final String get_preprint, final String get_consult_fees,
                                       final String get_imagepath, final String flash_message, final String header_height, final String footer_height) {

        new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;
            ProgressDialog progressDialog1;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog1 = new ProgressDialog(OtherSettingsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                progressDialog1.setIndeterminate(true);
                progressDialog1.setMessage("Please Wait....");
                progressDialog1.show();


            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {
                    JSONObject jsonObject = JSONParser.updateSettings(get_payment_status,get_preprint,get_consult_fees,
                            get_imagepath,flash_message,header_height,footer_height, USER_ID, USER_LOGIN_TYPE);

                    if (jsonObject != null) {
                        Log.e(Utils.TAG, " GET: " + jsonObject.getString("status"));
                        if(jsonObject.getString("status").equals("true")) {
                            status = true;
                        }
                        else {
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
                if (  progressDialog1 != null)
                    progressDialog1.dismiss();
                Log.e(Utils.TAG, "aBoolean: " + String.valueOf(aBoolean));
                if (aBoolean) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OtherSettingsActivity.this, R.style.CustomDialog);
                    alertDialogBuilder.setMessage("Settings updated successfully. ");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    finish();
                                }
                            });

                    alertDialogBuilder.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else {
                    Toast.makeText(OtherSettingsActivity.this, "Failed to update settings. \nTry later !!!", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }


    private void collectOtherSettingsListFromServer() {
        otherSettingsListArraylist = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(OtherSettingsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Settings...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_SETTINGS_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "settings list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("settings_details");
                                    if (jsonArray.length() > 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            otherSettingsListArraylist.add(new OtherSettings(jsonArray.getJSONObject(i).getInt("setting_id"),
                                                    jsonArray.getJSONObject(i).getInt("doc_id"),jsonArray.getJSONObject(i).getInt("doc_type"),
                                                    jsonArray.getJSONObject(i).getInt("payment_opt"),jsonArray.getJSONObject(i).getInt("prescription_pad"),
                                                    jsonArray.getJSONObject(i).getString("presc_pad_header_height"),jsonArray.getJSONObject(i).getString("presc_pad_footer_height"),
                                                    jsonArray.getJSONObject(i).getInt("before_consultation_fee"), jsonArray.getJSONObject(i).getString("doc_logo"),
                                                    jsonArray.getJSONObject(i).getString("doc_flash_msg")));
                                        }

                                        //Set the values
                                        gson = new Gson();
                                        String jsonSettingsText = gson.toJson(otherSettingsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearOtherSettings();
                                            shareadPreferenceClass.setOtherSettings(jsonSettingsText);
                                        }

                                        prepareSettingsData(otherSettingsListArraylist);
                                    }
                                    else {
                                        //Set the values
                                        gson = new Gson();
                                        String jsonSettingsText = gson.toJson(otherSettingsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearOtherSettings();
                                            shareadPreferenceClass.setOtherSettings(jsonSettingsText);
                                        }
                                        prepareSettingsData(otherSettingsListArraylist);
                                    }
                                }
                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }

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
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(OtherSettingsActivity.this).
                getRequestQueue();
        AppController.getInstance(OtherSettingsActivity.this).addToRequestQueue(stringRequest);
    }

    private void prepareSettingsData(List<OtherSettings> otherSettingsListArraylist) {

        if(otherSettingsListArraylist.size() > 0) {
            for(int i=0;i<otherSettingsListArraylist.size();i++) {

                if(otherSettingsListArraylist.get(i).getPaymentOption() == 1) {
                    rb_paymentEnable.setChecked(true);
                    rb_paymentDisable.setChecked(false);
                }
                else if(otherSettingsListArraylist.get(i).getPaymentOption() == 2) {
                    rb_paymentEnable.setChecked(false);
                    rb_paymentDisable.setChecked(true);
                }
                else {
                    rb_paymentEnable.setChecked(false);
                    rb_paymentDisable.setChecked(false);
                }

                if(otherSettingsListArraylist.get(i).getPrescPadHeight() == 1) {
                    preprint_padlayout.setVisibility(View.VISIBLE);
                    rb_preprintEnable.setChecked(true);
                    rb_preprintDisable.setChecked(false);
                }
                else if(otherSettingsListArraylist.get(i).getPrescPadHeight() == 2) {
                    preprint_padlayout.setVisibility(View.GONE);
                    rb_preprintEnable.setChecked(false);
                    rb_preprintDisable.setChecked(true);
                }
                else {
                    preprint_padlayout.setVisibility(View.GONE);
                    rb_preprintEnable.setChecked(false);
                    rb_preprintDisable.setChecked(false);
                }

                if(otherSettingsListArraylist.get(i).getConsultationFee() == 1) {
                    rb_consultFeesYes.setChecked(true);
                    rb_consultFeesNo.setChecked(false);

                }
                else if(otherSettingsListArraylist.get(i).getConsultationFee() == 2) {
                    rb_consultFeesYes.setChecked(false);
                    rb_consultFeesNo.setChecked(true);
                }
                else {
                    rb_consultFeesYes.setChecked(false);
                    rb_consultFeesNo.setChecked(false);
                }

                _edt_header_height.setText(otherSettingsListArraylist.get(i).getPrescHeaderHeight());
                _edt_footer_height.setText(otherSettingsListArraylist.get(i).getPrescFooterHeight());
                _edt_flash_message.setText(otherSettingsListArraylist.get(i).getFlashMessage());

                if(otherSettingsListArraylist.get(i).getDocLogo().equals("")) {
                    upload_image.setVisibility(View.GONE);
                }
                else {
                    upload_image.setVisibility(View.VISIBLE);
                    String DOWNLOAD_PROFILE = APIClass.DRS_SETTINGS_DOCLOGO_URL + String.valueOf(otherSettingsListArraylist.get(i).getDocID()) + "/" + otherSettingsListArraylist.get(i).getDocLogo().trim();
                    String urlStr = DOWNLOAD_PROFILE;
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
                    Picasso.with(OtherSettingsActivity.this).load(DOWNLOAD_PROFILE)
                            .placeholder(R.drawable.user_profile)
                            .error(R.drawable.user_profile)
                            .fit()
                            .into(upload_image, new Callback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError() {
                                }
                            });
                }
            }
        }
    }

    private void requestAttachmentPermissions() {
        if (ActivityCompat.checkSelfPermission(OtherSettingsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(OtherSettingsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(OtherSettingsActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(OtherSettingsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(OtherSettingsActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                AlertDialog.Builder builder1 = builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(OtherSettingsActivity.this, "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
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
                ActivityCompat.requestPermissions(OtherSettingsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
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
                if (ActivityCompat.shouldShowRequestPermissionRationale(OtherSettingsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(OtherSettingsActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(OtherSettingsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);


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
                    Toast.makeText(OtherSettingsActivity.this, "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(OtherSettingsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
        else  if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
//            else if (requestCode == REQUEST_CAMERA)
//                onCaptureImageResult(data);
        }
    }

    private void selectAttachementFromDevice() {
        final CharSequence[] items = {"Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(OtherSettingsActivity.this);
        builder.setTitle("Upload Attachments");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            Log.d(Utils.TAG +" data: ", String.valueOf(data));
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), bm);

               //  Uri selectedImageUri = data.getData();
              //  GET_IMAGEPATH =   getPath(selectedImageUri);
                GET_IMAGEPATH =   getRealPathFromURI(tempUri);
                Log.e(Utils.TAG, "GET_IMAGEPATH: "+ GET_IMAGEPATH);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        upload_image.setImageBitmap(bm);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index;
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
}
