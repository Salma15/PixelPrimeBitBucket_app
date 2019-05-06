package com.medisensehealth.fdccontributor.activities.settings;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP on 24-03-2018.
 */

public class ChangePasswordActivity extends AppCompatActivity {

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME, USER_LOGIN_TYPE;

    CustomEditText _txt_mobileNum, _txt_password, _txt_confirmPassword;
    Button _btn_updatepassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_change_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);

            Log.d(Utils.TAG, " ************ About Us Activity *********** ");
        }

        shareadPreferenceClass = new ShareadPreferenceClass(ChangePasswordActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(ChangePasswordActivity.this);

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

        Log.d(Utils.TAG+"USER_ID: ", String.valueOf(USER_ID));
        Log.d(Utils.TAG+"LOG_TYPE: ", USER_LOGIN_TYPE);

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
        _txt_mobileNum = (CustomEditText) findViewById(R.id.forgotpass_mobilenum);
        _txt_password = (CustomEditText) findViewById(R.id.forgotpass_password);
        _txt_confirmPassword = (CustomEditText) findViewById(R.id.forgotpass_confirmpassword);
        _btn_updatepassword = (Button) findViewById(R.id.forgotpass_update);

        _btn_updatepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectUpdateDetails();
            }
        });

    }

    private void collectUpdateDetails() {
      /*  if((_txt_mobileNum.getText().toString().length()>0) && (_txt_password.getText().toString().length()>0) && (_txt_confirmPassword.getText().toString().length()>0))
        {
            if((_txt_mobileNum.getText().toString().length() <10) || (_txt_mobileNum.getText().toString().length() >10)) {

            }

            if(_txt_password.getText().toString().equals(_txt_confirmPassword.getText().toString())) {
                updateNewPasswordToServer(_txt_mobileNum.getText().toString().trim(),_txt_password.getText().toString().trim(),_txt_confirmPassword.getText().toString().trim());
            }
            else {
                AppUtils.showCustomErrorMessage(getActivity(), "Update Password", "Password doesn't match", "OK", null, null);
            }

        }
        else {
            Toast.makeText(getActivity(), "Please enter all details", Toast.LENGTH_SHORT).show();
        }*/

        if(_txt_password.getText().toString().equals("")) {
            Toast.makeText(ChangePasswordActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
        }
        else if(_txt_confirmPassword.getText().toString().equals("")) {
            Toast.makeText(ChangePasswordActivity.this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
        }
        else {
            if(_txt_password.getText().toString().equals(_txt_confirmPassword.getText().toString())) {
                updateNewPasswordToServer(_txt_password.getText().toString().trim(),_txt_confirmPassword.getText().toString().trim());
            }
            else {
                AppUtils.showCustomErrorMessage(ChangePasswordActivity.this, "Update Password", "Password doesn't match", "OK", null, null);
            }
        }
    }

    private void updateNewPasswordToServer(final String password, String confirm_password) {
        //  Toast.makeText(getActivity(), "update password !!", Toast.LENGTH_SHORT).show();

        final ProgressDialog progressDialog = new ProgressDialog(ChangePasswordActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating Password...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_UPDATE_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Log.d(Utils.TAG, response.toString());

                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                String update_result = jsonObject.getString("update_status");
                                Log.d(Utils.TAG, staus_res.toString());
                                AppUtils.showCustomSuccessMessage(ChangePasswordActivity.this, "Success",update_result.toString(), "OK", null, null);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        AppUtils.showCustomAlertMessage(ChangePasswordActivity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                        Log.d(Utils.TAG+"ERR",error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(Utils.USER_LOGIN_ID));
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                map.put(APIClass.KEY_UPDATE_PASSWORD_PASSWORD, password);

                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(ChangePasswordActivity.this).
                getRequestQueue();
        AppController.getInstance(ChangePasswordActivity.this).addToRequestQueue(stringRequest);

    }
}
