package com.medisensehealth.fdccontributor.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by salma on 17/04/2019.
 */

public class BMJQRScanActivity extends AppCompatActivity  implements View.OnClickListener {
    Toolbar toolbar;
    FragmentManager fragManager;
    FragmentTransaction fragTransaction;

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE;

    //View Objects
    private ImageView buttonScan;
    private TextView textViewName, textViewAddress;
    CustomTextViewBold buttonSkip;

    //qr code scanner object
    private IntentIntegrator qrScan;

    WebView webView;
    LinearLayout footer_layout;
    ProgressDialog pd;

    //public String BMJ_URL = "http://bjo.bmj.com/user";
    public String BMJ_URL = "http://lms.bmj.com/bmjindia/cep/bjo";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);

        shareadPreferenceClass = new ShareadPreferenceClass(this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(this);

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


        webView = (WebView) findViewById(R.id.web_view);
        footer_layout = (LinearLayout) findViewById(R.id.bmj_footer);
        webView.setVisibility(View.GONE);
        buttonSkip = (CustomTextViewBold) findViewById(R.id.buttonSkip);
        buttonSkip.setOnClickListener(this);

        //View objects
        buttonScan = (ImageView) findViewById(R.id.buttonScan);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        //attaching onclick listener
        buttonScan.setOnClickListener(this);

        //intializing scan object
        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(true);
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        qrScan.setBarcodeImageEnabled(false);
        qrScan.setCameraId(0);

        initToolBar();
    }

    public void initToolBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle b = getIntent().getExtras();
        if( b != null) {
            String title = b.getString("title");
            setTitle(title);

        }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonScan:
                //initiating the qr code scan
                qrScan.initiateScan();
                break;
            case R.id.buttonSkip:
                webView.setVisibility(View.VISIBLE);
                footer_layout.setVisibility(View.GONE);
                buttonScan.setVisibility(View.GONE);
                continueWebViewBMJ();
                break;
        }

    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found \nInvalid QR Code !!!", Toast.LENGTH_LONG).show();
                buttonScan.setVisibility(View.VISIBLE);
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    textViewName.setText(obj.getString("organization"));
                    textViewAddress.setText(obj.getString("address"));

                    webView.setVisibility(View.VISIBLE);
                    footer_layout.setVisibility(View.GONE);
                    buttonScan.setVisibility(View.GONE);
                    continueWebViewBMJ();
                } catch (JSONException e) {
                    e.printStackTrace();

                    // Toast.makeText(this, "Invalid QR Code !!!", Toast.LENGTH_LONG).show();

                    webView.setVisibility(View.VISIBLE);
                    footer_layout.setVisibility(View.GONE);
                    buttonScan.setVisibility(View.GONE);
                    continueWebViewBMJ();
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void continueWebViewBMJ() {
        pd = new ProgressDialog(BMJQRScanActivity.this);
        pd.setMessage("Please wait loading...");
        pd.show();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(BMJ_URL);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            if (!pd.isShowing()) {
                pd.show();
            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            System.out.println("on finish");
            if (pd.isShowing()) {
                pd.dismiss();
            }

        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

}
