package com.medisensehealth.fdccontributor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomEditText;
import com.medisensehealth.fdccontributor.views.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 15/08/2018.
 */
public class ForgotPasswordActicity  extends AppCompatActivity implements View.OnClickListener {

    CustomEditText _edt_email;
    CustomTextView _btn_submit;
    CustomTextView _close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initialization(savedInstanceState);
    }

    private void initialization(Bundle savedInstanceState) {
        _edt_email = (CustomEditText) findViewById(R.id.forgotpass_email);
        _btn_submit = (CustomTextView) findViewById(R.id.forgotpass_submit);
        _btn_submit.setOnClickListener(this);
        _close = (CustomTextView) findViewById(R.id.forgotpass_close);
        _close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.forgotpass_submit:
                if(_edt_email.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Enter email address !!!", Toast.LENGTH_SHORT).show();
                }else {
                    if (NetworkUtil.getConnectivityStatusString(this).equalsIgnoreCase("enabled")) {
                        sendForgotPasswordRequestToServer();
                    }else {
                        AppUtils.showCustomAlertMessage(ForgotPasswordActicity.this, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                    }
                }
                break;
            case R.id.forgotpass_close:
                Intent back_login = new Intent(ForgotPasswordActicity.this, LoginActivity.class);
                startActivity(back_login);
                finish();
                break;
        }
    }

    private void sendForgotPasswordRequestToServer() {
        final String email_address = _edt_email.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(ForgotPasswordActicity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_DOCTORS_FORGOTPASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                          Log.d(Utils.TAG, response.toString());
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                String result = jsonObject.getString("forgot_password");
                                Log.d(Utils.TAG, staus_res.toString());

                                if(result.equals("success")) {
                                    AppUtils.showCustomSuccessMessage(ForgotPasswordActicity.this, "Forgot Password","Password is sent to your registered email address \nPlease check your email", "OK", null, null);
                                }
                                else if(result.equals("failed")) {
                                    AppUtils.showCustomErrorMessage(ForgotPasswordActicity.this, "Forgot Password","This email address has not been registered !!! ", "OK", null, null);
                                }
                                _edt_email.setText("");
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
                      //  AppUtil.showCustomAlert(ForgotPasswordActicity.this, "Server Error!!!","You Can Try it later", "OK", null);
                        Log.d(Utils.TAG+"ERR",error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_DOCTOR_FORGOT_EMAIL, email_address);
                map.put(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(ForgotPasswordActicity.this).
                getRequestQueue();
        AppController.getInstance(ForgotPasswordActicity.this).addToRequestQueue(stringRequest);
    }
}
