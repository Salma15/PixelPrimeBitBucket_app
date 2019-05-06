package com.medisensehealth.fdccontributor.activities.feeds;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.parser.JSONParser;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomEditText;
import com.medisensehealth.fdccontributor.views.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by salma on 07/03/18.
 */

public class AddBlogActivity extends AppCompatActivity implements View.OnClickListener {

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE, BLOG_LIST, LOGIN_SPEC_NAME;
    View header_tabs, footertabs;
    String FILTER_TYPE;

    CustomEditText _edt_title, _edt_descriptions;
    MultiAutoCompleteTextView _edt_tags;
    CustomTextView upload_btn;
    ImageView blog_image;
    Button submit_btn;
    private SharedPreferences permissionStatus;

    private String userChoosenTask, blogImagePath;
    Calendar myCalendar;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1, IMAGE_UPLOAD_FROM = 0;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;

    public List<String> suggest_category;
    public ArrayList<String> GET_BUSINESS_TYPE_ARRAY = new ArrayList<String>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_blog);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);
        }

        shareadPreferenceClass = new ShareadPreferenceClass(this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(this);

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            BLOG_LIST = sharedPreferences.getString(HCConstants.PREF_BLOG_LISTS, "");
            LOGIN_SPEC_NAME  = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_SPECIALIZATION_NAME, "SPEC_NAME");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            BLOG_LIST = sharedPreferences.getString(HCConstants.PREF_BLOG_LISTS, "");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            BLOG_LIST = sharedPreferences.getString(HCConstants.PREF_BLOG_LISTS, "");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }


        Log.d(Utils.TAG , " *********** Create ****************");
        Log.d(Utils.TAG +" LoginType: ", Utils.USER_LOGIN_TYPE);
        Log.d(Utils.TAG +" UserId: ", String.valueOf(Utils.USER_LOGIN_ID));

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
        suggest_category = new ArrayList<String>();
        permissionStatus = getSharedPreferences("permissionStatus", Context.MODE_PRIVATE);

        _edt_title = (CustomEditText) findViewById(R.id.blog_create_title);
        _edt_descriptions = (CustomEditText) findViewById(R.id.blog_create_description);
        _edt_tags = (MultiAutoCompleteTextView) findViewById(R.id.blog_create_tags);
        upload_btn = (CustomTextView) findViewById(R.id.blog_create_uploadbtn);
        upload_btn.setOnClickListener(this);
        blog_image = (ImageView) findViewById(R.id.blog_create_image);
        submit_btn = (Button) findViewById(R.id.blog_create_submit);
        submit_btn.setOnClickListener(this);

        _edt_tags.setThreshold(1);
        _edt_tags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        _edt_tags.addTextChangedListener(new TextWatcher(){

            public void afterTextChanged(Editable editable) {
                // TODO Auto-generated method stub
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                suggest_category = new ArrayList<String>();
                String newText = s.toString();
            }

        });

        _edt_tags.setText(USER_NAME+","+LOGIN_SPEC_NAME+",");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.blog_create_uploadbtn:
                requestAttachmentPermissions();
                break;
            case R.id.blog_create_submit:
                collectBlogPostDetails();
                break;
        }
    }

    private void collectBlogPostDetails() {
        String blog_title = _edt_title.getText().toString();
        String blog_description = _edt_descriptions.getText().toString();
        String blog_tags = _edt_tags.getText().toString();

        if(blog_title.equals("")) {
            Toast.makeText(AddBlogActivity.this, "Enter Title", Toast.LENGTH_SHORT).show();
        }
        else  if(blog_description.equals("")) {
            Toast.makeText(AddBlogActivity.this, "Enter Description", Toast.LENGTH_SHORT).show();
        }
        else {
            submitBlogPostToServer(blog_title, blog_description, blog_tags, blogImagePath);
        }
    }

    private int submitBlogPostToServer(final String blog_title, final String blog_description, final String blog_tags, final String blogImagePath) {
        Log.d(Utils.TAG, "***************** Post Blog *************");
        Log.d(Utils.TAG, " blog_title: " +  blog_title);
        Log.d(Utils.TAG, " blog_description: " +  blog_description);
        Log.d(Utils.TAG, " blog_tags: " +  blog_tags);
        Log.d(Utils.TAG, " blogImagePath: " +  blogImagePath);

        new AsyncTask<Void, Integer, Boolean>() {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(AddBlogActivity.this);
                progressDialog.setMessage("please wait...");
                progressDialog.show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {
                    JSONObject jsonObject = JSONParser.blog_post(blog_title,blog_description, blog_tags,
                            blogImagePath, USER_ID, USER_LOGIN_TYPE);

                    //   JSONObject jsonObject = JSONParser.uploadAlbumImage("10",num_attachments);
                    if (jsonObject != null)
                        return jsonObject.getString("blog_status").equals("success");
                    return true;
                } catch (JSONException e) {
                    Log.i("TAG", "Error : " + e.getLocalizedMessage());
                    return false;
                }

            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (progressDialog != null)
                    progressDialog.dismiss();

                if (aBoolean) {
                    //   Toast.makeText(SignUpActivity.this,"Registered Successfully", Toast.LENGTH_LONG).show();
                    AppUtils.showCustomSuccessMessage(AddBlogActivity.this, "Blog Post","Blog posted successfully", "OK", null, null);
                    _edt_title.setText("");
                    _edt_descriptions.setText("");
                    blog_image.setImageResource(R.drawable.blogs_empty_img);

                    if (sharedPreferences != null) {
                        shareadPreferenceClass.clearFeedsFilterBlogDetailsLists();
                    }
                }
                else
                    // Toast.makeText(SignUpActivity.this, "This user is already registered. \n Please click on Forgot Password to reset your password", Toast.LENGTH_LONG).show();
                    AppUtils.showCustomAlertMessage(AddBlogActivity.this, "Blog Post","Failed to post blog !!!", "OK", null, null);
            }
        }.execute();

        return 1;
    }

    private void requestAttachmentPermissions() {
        if (ActivityCompat.checkSelfPermission(AddBlogActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddBlogActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(AddBlogActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(AddBlogActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddBlogActivity.this);
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
                        Toast.makeText(AddBlogActivity.this, "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
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
                ActivityCompat.requestPermissions(AddBlogActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
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
        selectImage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(AddBlogActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddBlogActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(AddBlogActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);


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
                    Toast.makeText(AddBlogActivity.this,"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(AddBlogActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
        else if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Choose from Library", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(AddBlogActivity.this);
        builder.setTitle("Upload Image!!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
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
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select File");
        startActivityForResult(chooserIntent, SELECT_FILE);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        String gallery_path = "";
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                Uri selectedImageUri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

                if (cursor != null) {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    gallery_path = cursor.getString(columnIndex);

                    cursor.close();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        blog_image.setImageBitmap(bm);
        blogImagePath = gallery_path;
        Log.d(Utils.TAG, "blogImagePath: " + blogImagePath );
    }
}
