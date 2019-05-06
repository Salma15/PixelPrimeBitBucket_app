package com.medisensehealth.fdccontributor.activities.mypatientOphthalmology;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medisensehealth.fdccontributor.DataModel.DiagnosticCentreList;
import com.medisensehealth.fdccontributor.DataModel.OldVisitsOphthalList;
import com.medisensehealth.fdccontributor.DataModel.OpticalCentreList;
import com.medisensehealth.fdccontributor.DataModel.PharmaCentreList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.settings.OpticalsListActivity;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.CustomDiagnosticCenterFilterAdapter;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.CustomPharmaCenterFilterAdapter;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.parser.JSONParser;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomEditText;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;
import com.medisensehealth.fdccontributor.views.CustomTextViewSemiBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SALMA on 15-09-2018.
 */
public class OldVisitOphthalListViewAdapter extends RecyclerView.Adapter<OldVisitOphthalListViewAdapter.MyViewHolder> {
    private List<OldVisitsOphthalList> episodeList;
    Context mContext;
    String URL = "https://pixeleyecare.com/Contributors/print-emr/mobile_emr.php?pid=";
    int USER_ID;
    String USER_NAME,USER_LOGIN_TYPE, DIAGNOSTIC_CENTRE_LIST, PHARMA_CENTRE_LIST, OPTICAL_CENTRE_LIST;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;

    List<DiagnosticCentreList> diagnosticsListArraylist = new ArrayList<>();
    List<DiagnosticCentreList> selectedDiagnoCentreArraylist = new ArrayList<>();
    List<PharmaCentreList> pharmaListArraylist = new ArrayList<>();
    List<PharmaCentreList> selectedPharmaArraylist = new ArrayList<>();
    List<OpticalCentreList> opticalsListArraylist = new ArrayList<>();
    List<OpticalCentreList> selectedOpticalCentreArraylist = new ArrayList<>();

    Gson gson = new Gson();
    LinearLayout addNew_contents, list_contents;
    AlertDialog alert11;

    MultiSpinnerSearch diagnostic_multispinner, pharmacy_multispinner, opticals_multispinner;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView title, year, genre;
        public ImageView edit_btn, print_btn, share_emr_btn;

        public MyViewHolder(View view) {
            super(view);
            title = (CustomTextView) view.findViewById(R.id.title);
            edit_btn = (ImageView) view.findViewById(R.id.edt_btn);
            print_btn = (ImageView) view.findViewById(R.id.print_btn);
            share_emr_btn  = (ImageView) view.findViewById(R.id.share_emr_btn);
        }
    }

    public OldVisitOphthalListViewAdapter(Context context, List<OldVisitsOphthalList> epsList) {
        this.episodeList = epsList;
        this.mContext = context;
        this.shareadPreferenceClass = new ShareadPreferenceClass(context);
        this.sharedPreferences = shareadPreferenceClass.getSharedPreferences(context);
        this.diagnosticsListArraylist = new ArrayList<>();
        this.selectedDiagnoCentreArraylist = new ArrayList<>();
        this.pharmaListArraylist = new ArrayList<>();
        this.selectedPharmaArraylist = new ArrayList<>();
        this.opticalsListArraylist = new ArrayList<>();
        this.selectedOpticalCentreArraylist = new ArrayList<>();

        if(( this.sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            DIAGNOSTIC_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_DIAGNOSTIC_CENTRES,"");
            PHARMA_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_PHARMA_CENTRES,"");
            OPTICAL_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_OPTICAL_CENTRES,"");
        }
        else  if(( this.sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            DIAGNOSTIC_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_DIAGNOSTIC_CENTRES,"");
            PHARMA_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_PHARMA_CENTRES,"");
            OPTICAL_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_OPTICAL_CENTRES,"");
        }
        else  if(( this.sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            DIAGNOSTIC_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_DIAGNOSTIC_CENTRES,"");
            PHARMA_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_PHARMA_CENTRES,"");
            OPTICAL_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_OPTICAL_CENTRES,"");
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mypatient_episode_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final OldVisitsOphthalList eps = episodeList.get(position);

        int num_pos = position;
        num_pos = Integer.valueOf(episodeList.size()) - num_pos;

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String inputDateStr= eps.getEpisodeCreatedDate();
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
            String outputDateStr = outputFormat.format(date);
            holder.title.setText("Visit:  #"+num_pos + " ( "+outputDateStr +" )");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent bundle2 = new Intent(mContext, ViewOphthalVisitDetailsActivity.class);
                bundle2.putExtra("title","Visit Details");
                bundle2.putExtra("EPISODE_ID", eps.getEpisodeID());
                bundle2.putExtra("PATIENT_ID", eps.getEpisodePatientID());
                bundle2.putExtra("FOLLOWUP_DATE", eps.getEpisodeFollowupDate());
                bundle2.putExtra("DIAGNOSIS_DETAILS", eps.getEpisodeDiagnosisDetails());
                bundle2.putExtra("TREATMENT_DETAILS", eps.getEpisodeTreatmentDetails());
                bundle2.putExtra("PRESCRIPTION_NOTES", eps.getEpisodePrescriptionNotes());
                bundle2.putExtra("EPISODE_CREATED_DATE", eps.getEpisodeCreatedDate());
                bundle2.putExtra("CONSULTATION_FEES", eps.getEpisodeConsultaionFees());
                bundle2.putExtra("PATIENT_EDUCATION", eps.getEpisodePatientEducationID());
                bundle2.putExtra("PATIENT_CHIEFCOMPALINT_SUFFERINGS", eps.getEpisodeChiefMedComplaintSufferings());
                bundle2.putExtra("CHIEF_MEDICAL_LIST", (Serializable) eps.getChiefMedicalList());
                bundle2.putExtra("INVESTIGATION_GENERAL_LIST",  (Serializable) eps.getInvestigationGeneralList());
                bundle2.putExtra("INVESTIGATION_OPHTHAL_LIST", (Serializable) eps.getInvestigationOphthalList());
                bundle2.putExtra("INVESTIGATION_RADIOLOGY_LIST", (Serializable) eps.getInvestigationRadiologyList());
                bundle2.putExtra("INVESTIGATION_CLINICAL_LIST", (Serializable) eps.getInvestigationClinicalList());
                bundle2.putExtra("INVESTIGATION_OTHERS_LIST", (Serializable) eps.getInvestigationOtherList());
                bundle2.putExtra("DIAGNOSIS_LIST", (Serializable) eps.getDiagnosisList());
                bundle2.putExtra("TREATMENT_LIST", (Serializable) eps.getTreatmentList());
                bundle2.putExtra("PRESCRIPTION_LIST", (Serializable) eps.getPrescriptionList());
                bundle2.putExtra("LIDS_LIST", (Serializable) eps.getLidsList());
                bundle2.putExtra("CONJUCTIVA_LIST", (Serializable) eps.getConjuctivaList());
                bundle2.putExtra("SCLERA_LIST", (Serializable) eps.getScleraList());
                bundle2.putExtra("CORNEA_ANTERIOR_LIST", (Serializable) eps.getCorneaAnteriorList());
                bundle2.putExtra("CORNEA_POSTERIOR_LIST", (Serializable) eps.getCorneaPosteriorList());
                bundle2.putExtra("ANTERIOR_CHAMBER_LIST", (Serializable) eps.getAnteriorChamberList());
                bundle2.putExtra("IRIS_LIST", (Serializable) eps.getIrisList());
                bundle2.putExtra("PUPIL_LIST", (Serializable) eps.getPupilList());
                bundle2.putExtra("ANGLE_LIST", (Serializable) eps.getAngleList());
                bundle2.putExtra("LENS_LIST", (Serializable) eps.getLensList());
                bundle2.putExtra("VITERIOUS_LIST", (Serializable) eps.getViterousList());
                bundle2.putExtra("FUNDUS_LIST", (Serializable) eps.getFundusList());
                bundle2.putExtra("REFRACTION_RE_VALUE1",  eps.getEpisodeRefractionRE1());
                bundle2.putExtra("REFRACTION_RE_VALUE2",  eps.getEpisodeRefractionRE2());
                bundle2.putExtra("REFRACTION_LE_VALUE1",  eps.getEpisodeRefractionLE1());
                bundle2.putExtra("REFRACTION_LE_VALUE2",  eps.getEpisodeRefractionLE2());
                bundle2.putExtra("DISTANCE_VISION_RE", eps.getEpisodeDistanceVisionRE());
                bundle2.putExtra("DISTANCE_VISION_LE",  eps.getEpisodeDistanceVisionLE());
                bundle2.putExtra("NEAR_VISION_RE", eps.getEpisodeNearVisionRE());
                bundle2.putExtra("NEAR_VISION_LE",  eps.getEpisodeNearVisionLE());
                bundle2.putExtra("DV_SPHERE_RIGHT",  eps.getEpisodeDvSphereRE());
                bundle2.putExtra("DV_CYCLE_RIGHT", eps.getEpisodeDvCycleRE());
                bundle2.putExtra("DV_AXIS_RIGHT",  eps.getEpisodeDvAxisRE());
                bundle2.putExtra("NV_SPHERE_RIGHT",  eps.getEpisodeNvSphereRE());
                bundle2.putExtra("NV_CYCLE_RIGHT", eps.getEpisodeNvCycleRE());
                bundle2.putExtra("NV_AXIS_RIGHT",  eps.getEpisodeNvAxisRE());
                bundle2.putExtra("IPD_RIGHT", eps.getEpisodeIPDRE());
                bundle2.putExtra("DV_SPHERE_LEFT", eps.getEpisodeDvSphereLE());
                bundle2.putExtra("DV_CYCLE_LEFT", eps.getEpisodeDvCycleLE());
                bundle2.putExtra("DV_AXIS_LEFT", eps.getEpisodeDvAxisLE());
                bundle2.putExtra("NV_SPHERE_LEFT", eps.getEpisodeNvSphereLE());
                bundle2.putExtra("NV_CYCLE_LEFT", eps.getEpisodeNvCycleLE());
                bundle2.putExtra("NV_AXIS_LEFT", eps.getEpisodeNvAxisLE());
                bundle2.putExtra("IPD_LEFT", eps.getEpisodeIPDLE());
                bundle2.putExtra("DISTANCE_VISION_UNAIDED_RE", eps.getEpisodeDistanceVisionUnaidedRE());
                bundle2.putExtra("DISTANCE_VISION_UNAIDED_LE",  eps.getEpisodeDistanceVisionUnaidedLE());
                bundle2.putExtra("NEAR_VISION_UNAIDED_RE", eps.getEpisodeNearVisionUnaidedRE());
                bundle2.putExtra("NEAR_VISION_UNAIDED_LE",  eps.getEpisodeNearVisionUnaidedLE());
                bundle2.putExtra("IOP_RE", eps.getEpisodeIOPRE());
                bundle2.putExtra("IOP_LE", eps.getEpisodeIOPLE());

                mContext.startActivity(bundle2);
            }
        });

        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Toast.makeText(mContext, "Coming Soon !!!", Toast.LENGTH_SHORT).show();
                Intent bundle3 = new Intent(mContext, EditOphthalVisitDetailsActivity.class);
                bundle3.putExtra("title","Edit Visit");
                bundle3.putExtra("EPISODE_ID", eps.getEpisodeID());
                bundle3.putExtra("PATIENT_ID", eps.getEpisodePatientID());
                bundle3.putExtra("FOLLOWUP_DATE", eps.getEpisodeFollowupDate());
                bundle3.putExtra("DIAGNOSIS_DETAILS", eps.getEpisodeDiagnosisDetails());
                bundle3.putExtra("TREATMENT_DETAILS", eps.getEpisodeTreatmentDetails());
                bundle3.putExtra("PRESCRIPTION_NOTES", eps.getEpisodePrescriptionNotes());
                bundle3.putExtra("EPISODE_CREATED_DATE", eps.getEpisodeCreatedDate());
                bundle3.putExtra("CONSULTATION_FEES", eps.getEpisodeConsultaionFees());
                bundle3.putExtra("PATIENT_EDUCATION", eps.getEpisodePatientEducationID());
                bundle3.putExtra("PATIENT_CHIEFCOMPALINT_SUFFERINGS", eps.getEpisodeChiefMedComplaintSufferings());
                bundle3.putExtra("CHIEF_MEDICAL_LIST", (Serializable) eps.getChiefMedicalList());
                bundle3.putExtra("INVESTIGATION_GENERAL_LIST",  (Serializable) eps.getInvestigationGeneralList());
                bundle3.putExtra("INVESTIGATION_OPHTHAL_LIST", (Serializable) eps.getInvestigationOphthalList());
                bundle3.putExtra("INVESTIGATION_RADIOLOGY_LIST", (Serializable) eps.getInvestigationRadiologyList());
                bundle3.putExtra("INVESTIGATION_CLINICAL_LIST", (Serializable) eps.getInvestigationClinicalList());
                bundle3.putExtra("INVESTIGATION_OTHERS_LIST", (Serializable) eps.getInvestigationOtherList());
                bundle3.putExtra("DIAGNOSIS_LIST", (Serializable) eps.getDiagnosisList());
                bundle3.putExtra("TREATMENT_LIST", (Serializable) eps.getTreatmentList());
                bundle3.putExtra("PRESCRIPTION_LIST", (Serializable) eps.getPrescriptionList());
                bundle3.putExtra("LIDS_LIST", (Serializable) eps.getLidsList());
                bundle3.putExtra("CONJUCTIVA_LIST", (Serializable) eps.getConjuctivaList());
                bundle3.putExtra("SCLERA_LIST", (Serializable) eps.getScleraList());
                bundle3.putExtra("CORNEA_ANTERIOR_LIST", (Serializable) eps.getCorneaAnteriorList());
                bundle3.putExtra("CORNEA_POSTERIOR_LIST", (Serializable) eps.getCorneaPosteriorList());
                bundle3.putExtra("ANTERIOR_CHAMBER_LIST", (Serializable) eps.getAnteriorChamberList());
                bundle3.putExtra("IRIS_LIST", (Serializable) eps.getIrisList());
                bundle3.putExtra("PUPIL_LIST", (Serializable) eps.getPupilList());
                bundle3.putExtra("ANGLE_LIST", (Serializable) eps.getAngleList());
                bundle3.putExtra("LENS_LIST", (Serializable) eps.getLensList());
                bundle3.putExtra("VITERIOUS_LIST", (Serializable) eps.getViterousList());
                bundle3.putExtra("FUNDUS_LIST", (Serializable) eps.getFundusList());
                bundle3.putExtra("REFRACTION_RE_VALUE1",  eps.getEpisodeRefractionRE1());
                bundle3.putExtra("REFRACTION_RE_VALUE2",  eps.getEpisodeRefractionRE2());
                bundle3.putExtra("REFRACTION_LE_VALUE1",  eps.getEpisodeRefractionLE1());
                bundle3.putExtra("REFRACTION_LE_VALUE2",  eps.getEpisodeRefractionLE2());
                bundle3.putExtra("DISTANCE_VISION_RE", eps.getEpisodeDistanceVisionRE());
                bundle3.putExtra("DISTANCE_VISION_LE",  eps.getEpisodeDistanceVisionLE());
                bundle3.putExtra("NEAR_VISION_RE", eps.getEpisodeNearVisionRE());
                bundle3.putExtra("NEAR_VISION_LE",  eps.getEpisodeNearVisionLE());
                bundle3.putExtra("DV_SPHERE_RIGHT",  eps.getEpisodeDvSphereRE());
                bundle3.putExtra("DV_CYCLE_RIGHT", eps.getEpisodeDvCycleRE());
                bundle3.putExtra("DV_AXIS_RIGHT",  eps.getEpisodeDvAxisRE());
                bundle3.putExtra("NV_SPHERE_RIGHT",  eps.getEpisodeNvSphereRE());
                bundle3.putExtra("NV_CYCLE_RIGHT", eps.getEpisodeNvCycleRE());
                bundle3.putExtra("NV_AXIS_RIGHT",  eps.getEpisodeNvAxisRE());
                bundle3.putExtra("IPD_RIGHT", eps.getEpisodeIPDRE());
                bundle3.putExtra("DV_SPHERE_LEFT", eps.getEpisodeDvSphereLE());
                bundle3.putExtra("DV_CYCLE_LEFT", eps.getEpisodeDvCycleLE());
                bundle3.putExtra("DV_AXIS_LEFT", eps.getEpisodeDvAxisLE());
                bundle3.putExtra("NV_SPHERE_LEFT", eps.getEpisodeNvSphereLE());
                bundle3.putExtra("NV_CYCLE_LEFT", eps.getEpisodeNvCycleLE());
                bundle3.putExtra("NV_AXIS_LEFT", eps.getEpisodeNvAxisLE());
                bundle3.putExtra("IPD_LEFT", eps.getEpisodeIPDLE());
                bundle3.putExtra("DISTANCE_VISION_UNAIDED_RE", eps.getEpisodeDistanceVisionUnaidedRE());
                bundle3.putExtra("DISTANCE_VISION_UNAIDED_LE",  eps.getEpisodeDistanceVisionUnaidedLE());
                bundle3.putExtra("NEAR_VISION_UNAIDED_RE", eps.getEpisodeNearVisionUnaidedRE());
                bundle3.putExtra("NEAR_VISION_UNAIDED_LE",  eps.getEpisodeNearVisionUnaidedLE());
                bundle3.putExtra("IOP_RE", eps.getEpisodeIOPRE());
                bundle3.putExtra("IOP_LE", eps.getEpisodeIOPLE());
                mContext.startActivity(bundle3);
            }
        });

        holder.print_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customPrintWebview(eps.getEpisodeID(), eps.getEpisodePatientID());
            }
        });

        holder.share_emr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

           /*     final String[] items = {"Complete EMR","Prescription Only","With Diagnostic Center","With Pharmacy"};
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Would you like to share the following details?");
                 builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d(Utils.TAG,"The button was tapped: " + items[whichButton]);

                        if(items[whichButton].equals("Complete EMR")) {
                            shareEMRLink(eps.getEpisodeID(), eps.getEpisodePatientID());
                        }
                        else if(items[whichButton].equals("Prescription Only")) {
                            sharePrescriptions(eps.getEpisodePatientID());
                        }
                        else if(items[whichButton].equals("With Diagnostic Center")) {
                            collectDiagnosticCentreList(eps.getEpisodeID(), eps.getEpisodePatientID());
                        }
                        else if(items[whichButton].equals("With Pharmacy")) {
                            collectPharmaCentreList(eps.getEpisodeID(), eps.getEpisodePatientID());
                        }
                        alert11.dismiss();
                    }
                });
                alert11 = builder.create();
                alert11.show();*/

                prepareCustomShareLink(eps.getEpisodeID(), eps.getEpisodePatientID());
            }
        });
    }

    private void sharePrescriptions(int episodeID, int episodePatientID) {
        try {
            //  String shareURL = APIClass.DRS_SHARE_PRESCRIPTION_LINK + md5(String.valueOf(episodePatientID))+ ".jpg";
            String shareURL = URL+md5(String.valueOf(episodePatientID))+"&episode="+md5(String.valueOf(episodeID))+"&s=3";

            Log.d(Utils.TAG, " shareURL: "+ shareURL);
            Intent i=new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT,"Premium EMR");
            i.putExtra(Intent.EXTRA_TEXT, "This is your digitalized prescription sent by Dr. "+USER_NAME+" \n\nPlease visit the following Link: \n"+shareURL+ "\n\nThank You, \n"+USER_NAME);
            mContext.startActivity(Intent.createChooser(i,"Share via"));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private void shareEMRLink(int episodeID, int PatientID) {
        String WEB_URL = "";
        try {
            WEB_URL = URL+md5(String.valueOf(PatientID))+"&episode="+md5(String.valueOf(episodeID))+"&s=2";
            Log.d(Utils.TAG, " ep_WEB_URL: "+ WEB_URL);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        // sendIntent.putExtra(Intent.EXTRA_TEXT, "Patient ID: "+PatientID +" \n\nYour EMR link: "+WEB_URL +"\n\nThanks, \n"+USER_NAME);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is your digitalized EMR sent by Dr. "+USER_NAME+" \n\nPlease visit the following EMR Link: \n"+WEB_URL+ "\n\nThank You, \n"+USER_NAME);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT,"Premium EMR");
        mContext.startActivity(Intent.createChooser(sendIntent, "Premium EMR"));
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    private void customPrintWebview(int episodeID, int PatientID) {
        final Dialog dialog = new Dialog(mContext, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_webview);
        dialog.setTitle("Print Visit");
        dialog.setCanceledOnTouchOutside(true);


        WebView pdf_webview = (WebView) dialog.findViewById(R.id.webview);
        CustomTextViewBold share_emr = (CustomTextViewBold)  dialog.findViewById(R.id.share_emr);
        final LinearLayout contentView = (LinearLayout) dialog.findViewById(R.id.share_container_layout_id);

        pdf_webview.getSettings().setJavaScriptEnabled(true);
        pdf_webview.getSettings().setLoadWithOverviewMode(true);
        pdf_webview.getSettings().setUseWideViewPort(true);
        pdf_webview.setWebChromeClient(new WebChromeClient());
        pdf_webview.setWebViewClient(new WebViewClient());
        pdf_webview.getSettings().setBuiltInZoomControls(true);
        pdf_webview.getSettings().setDisplayZoomControls(false);

        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage("Please wait Loading...");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
        pd.show();

        pdf_webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                if (!pd.isShowing()) {
                    pd.show();
                }

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                createWebPrintJob(view);

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                dialog.dismiss();
            }
        });

        try {

            String WEB_URL = URL+md5(String.valueOf(PatientID))+"&episode="+md5(String.valueOf(episodeID));
            pdf_webview.loadUrl(WEB_URL);

            Log.d(Utils.TAG+ " md5 patid ",md5(String.valueOf(PatientID)) +"\n url: "+WEB_URL);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        dialog.show();
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

    private void createWebPrintJob(WebView webView) {

        PrintManager printManager = (PrintManager) mContext.getSystemService(Context.PRINT_SERVICE);

        PrintDocumentAdapter printAdapter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            printAdapter = webView.createPrintDocumentAdapter();

            String jobName = mContext.getString(R.string.app_name) + " Print Test";

            printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
        }
    }

    private void collectDiagnosticCentreList(int episodeID, int episodePatientID) {
        diagnosticsListArraylist = new ArrayList<>();
        selectedDiagnoCentreArraylist = new ArrayList<>();
        if (DIAGNOSTIC_CENTRE_LIST.equals("")) {
            Log.d(Utils.TAG, "DIAGNOSTIC_CENTRE_LIST EMPTY ");

            if (NetworkUtil.getConnectivityStatusString(mContext).equalsIgnoreCase("enabled")) {
                collectDiagnosticsListFromServer(episodeID, episodePatientID);
            } else {
                AppUtils.showCustomAlertMessage(mContext, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            diagnosticsListArraylist = gson.fromJson(DIAGNOSTIC_CENTRE_LIST, new TypeToken<List<DiagnosticCentreList>>() {
            }.getType());
            if(diagnosticsListArraylist.size() > 0 ) {
                Log.d(Utils.TAG, "DIAGNOSTIC_CENTRE_LIST > 0 " + diagnosticsListArraylist.size());
                prepareDiagnostiCentreData(diagnosticsListArraylist,episodeID, episodePatientID);
            }
        }
    }

    private void collectDiagnosticsListFromServer(final int episodeID, final int episodePatientID) {
        diagnosticsListArraylist = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Diagnostics...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_DIAGNOSTICS_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "diagno list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("diagnostics_details");
                                    if (jsonArray.length() > 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            diagnosticsListArraylist.add(new DiagnosticCentreList(jsonArray.getJSONObject(i).getInt("diagnostic_id"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_name"),jsonArray.getJSONObject(i).getString("diagnosis_city"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_state"),jsonArray.getJSONObject(i).getString("diagnosis_country"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_contact_person"),jsonArray.getJSONObject(i).getString("diagnosis_contact_num"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_email"),USER_ID, USER_LOGIN_TYPE));

                                        }

                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(diagnosticsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearDiagnosticCentres();
                                            shareadPreferenceClass.setDiagnosticCentres(jsonDiagnoText);
                                        }

                                        //   prepareDiagnostiCentreData(diagnosticsListArraylist,episodeID, episodePatientID);
                                        prepareDiagnostiCentreDataNew(diagnosticsListArraylist,episodeID, episodePatientID);
                                    }
                                    else {
                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(diagnosticsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearDiagnosticCentres();
                                            shareadPreferenceClass.setDiagnosticCentres(jsonDiagnoText);
                                        }
                                        //  prepareDiagnostiCentreData(diagnosticsListArraylist,episodeID, episodePatientID);
                                        prepareDiagnostiCentreDataNew(diagnosticsListArraylist,episodeID, episodePatientID);
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

        RequestQueue requestQueue = AppController.getInstance(mContext).
                getRequestQueue();
        AppController.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    private void prepareDiagnostiCentreData(List<DiagnosticCentreList> diagnosticsListArraylist, final int episodeID, final int episodePatientID) {
        selectedDiagnoCentreArraylist = new ArrayList<>();
        final Dialog dialog = new Dialog(mContext, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_diagnostic_center);
        dialog.setTitle("Refer to Diagnosis");
        dialog.setCanceledOnTouchOutside(true);

        addNew_contents = dialog.findViewById(R.id.diagnostic_addNew_content);
        addNew_contents.setVisibility(View.GONE);


        list_contents = dialog.findViewById(R.id.diagnostic_list_content);
        list_contents.setVisibility(View.VISIBLE);

        final ImageView close_btn = (ImageView)  dialog.findViewById(R.id.diagnostic_close);
        final ImageView refresh_btn = (ImageView)  dialog.findViewById(R.id.diagnostic_refresh);
        CustomTextViewBold add_new_btn = (CustomTextViewBold) dialog.findViewById(R.id.diagnostic_add_new);
        final CustomTextViewBold submit_new_btn = (CustomTextViewBold) dialog.findViewById(R.id.edt_diagnostic_submit);
        final CustomTextViewBold cancel_new_btn = (CustomTextViewBold) dialog.findViewById(R.id.edt_diagnostic_cancel);

        final CustomEditText _edit_name = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_name);
        final CustomEditText _edit_email = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_email);
        final CustomEditText _edit_mobile = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_mobile);
        final CustomEditText _edit_city = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_city);

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                InputMethodManager imm1 = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(close_btn.getWindowToken(), 0);
            }
        });

        add_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNew_contents.setVisibility(View.VISIBLE);
                list_contents.setVisibility(View.GONE);
            }
        });

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectDiagnosticsListFromServer(episodeID,episodePatientID);
            }
        });

        submit_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm2 = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(submit_new_btn.getWindowToken(), 0);

                String diagno_name = _edit_name.getText().toString().trim();
                String diagno_email = _edit_email.getText().toString().trim();
                String diagno_mobile = _edit_mobile.getText().toString().trim();
                String diagno_city = _edit_city.getText().toString().trim();

                if(diagno_name.equals("")) {
                    Toast.makeText(mContext, "Enter Diagnostic Names", Toast.LENGTH_SHORT).show();
                }
                else  if((diagno_mobile.equals("")) || (diagno_mobile.length() <10)) {
                    Toast.makeText(mContext, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendDiagnosticNewToServer(diagno_name,diagno_email,diagno_mobile,diagno_city,episodeID,episodePatientID );
                    addNew_contents.setVisibility(View.GONE);
                    list_contents.setVisibility(View.VISIBLE);
                }
            }
        });

        cancel_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm3 = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(cancel_new_btn.getWindowToken(), 0);
                addNew_contents.setVisibility(View.GONE);
                list_contents.setVisibility(View.VISIBLE);
            }
        });

        final SearchView diagnostic_searchView = (SearchView) dialog.findViewById(R.id.searchview_diagnostic);
        ListView diagnostic_listview = (ListView) dialog.findViewById(R.id.diagnostic_list);

        final CustomDiagnosticCenterFilterAdapter diagnoAdapter = new CustomDiagnosticCenterFilterAdapter(mContext,
                R.layout.activity_listview, diagnosticsListArraylist);
        diagnostic_listview.setAdapter(diagnoAdapter);
        diagnostic_listview.setTextFilterEnabled(true);

        diagnostic_searchView.setIconifiedByDefault(false);
        diagnostic_searchView.setQueryHint("Search Diagnostic Center");
        diagnostic_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                diagnostic_searchView.setQuery("", false);
                diagnostic_searchView.clearFocus();
                diagnostic_searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Utils.TAG, "SERACH: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    Log.d(Utils.TAG, "is empty " );
                    diagnoAdapter.filter(newText.toString());
                    // specialization_listview.clearTextFilter();
                } else {
                    //  specialization_listview.setFilterText(newText.toString());
                    diagnoAdapter.filter(newText.toString());
                }
                return true;
            }
        });

        diagnostic_searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                diagnostic_searchView.setQuery("", false);
                diagnostic_searchView.setIconified(false);
                diagnostic_searchView.clearFocus();
                return false;
            }
        });

        diagnostic_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DiagnosticCentreList myCustomAdapterItem = (DiagnosticCentreList) parent.getItemAtPosition(position);
              /* Toast.makeText(getActivity(),  myCustomAdapterItem.getSymptomsName() + " added.",
                        Toast.LENGTH_SHORT)
                        .show();*/
                diagnostic_searchView.setQuery("", false);
                diagnostic_searchView.setIconified(false);
                diagnostic_searchView.clearFocus();

                selectedDiagnoCentreArraylist = new ArrayList<>();
                selectedDiagnoCentreArraylist.add(new DiagnosticCentreList(myCustomAdapterItem.getDiagnoId(),myCustomAdapterItem.getDiagnoName()));

                sendReferDiagnoRequestToserver(selectedDiagnoCentreArraylist,episodeID, episodePatientID);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void sendDiagnosticNewToServer(final String diagno_name, final String diagno_email, final String diagno_mobile, final String diagno_city, final int episodeID, final int episodePatientID) {
        diagnosticsListArraylist = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding New Diagnostic Center...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_DIAGNOSTICS_ADD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "diagno list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("diagnostics_details");
                                    if (jsonArray.length() > 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            diagnosticsListArraylist.add(new DiagnosticCentreList(jsonArray.getJSONObject(i).getInt("diagnostic_id"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_name"),jsonArray.getJSONObject(i).getString("diagnosis_city"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_state"),jsonArray.getJSONObject(i).getString("diagnosis_country"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_contact_person"),jsonArray.getJSONObject(i).getString("diagnosis_contact_num"),
                                                    jsonArray.getJSONObject(i).getString("diagnosis_email"),USER_ID, USER_LOGIN_TYPE));

                                        }

                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(diagnosticsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearDiagnosticCentres();
                                            shareadPreferenceClass.setDiagnosticCentres(jsonDiagnoText);
                                        }

                                        //  prepareDiagnostiCentreData(diagnosticsListArraylist,episodeID, episodePatientID);
                                    }
                                    else {
                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(diagnosticsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearDiagnosticCentres();
                                            shareadPreferenceClass.setDiagnosticCentres(jsonDiagnoText);
                                        }
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
                map.put(APIClass.KEY_DIAGNO_NAME, diagno_name);
                map.put(APIClass.KEY_DIAGNO_EMAIL, diagno_email);
                map.put(APIClass.KEY_DIAGNO_MOBILE, diagno_mobile);
                map.put(APIClass.KEY_DIAGNO_CITY, diagno_city);
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(mContext).
                getRequestQueue();
        AppController.getInstance(mContext).addToRequestQueue(stringRequest);
    }


    private void sendReferDiagnoRequestToserver(final List<DiagnosticCentreList> selectedDiagnoCentreArraylist, final int episodeID, final int episodePatientID) {

        if(selectedDiagnoCentreArraylist.size() > 0) {
            for (int i = 0; i < selectedDiagnoCentreArraylist.size(); i++) {
                Log.i(Utils.TAG, selectedDiagnoCentreArraylist.get(i).getDiagnoId() + " : " + selectedDiagnoCentreArraylist.get(i).getDiagnoName());
            }

            Log.d(Utils.TAG, " diagnoSize " + selectedDiagnoCentreArraylist.size());
            new AsyncTask<Void, Integer, Boolean>() {
                boolean status = false;
                ProgressDialog progressDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();

                }

                @Override
                protected Boolean doInBackground(Void... params) {

                    try {

                        JSONObject jsonObject = JSONParser.sendDiagnosticReferal(String.valueOf(episodePatientID), String.valueOf(episodeID),
                                selectedDiagnoCentreArraylist, USER_ID, USER_LOGIN_TYPE);

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

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext, R.style.CustomDialog);
                        alertDialogBuilder.setMessage("Referred to diagnostics successfully !!!. ");
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {
                        Toast.makeText(mContext, "Failed to send referal. \nTry later !!!", Toast.LENGTH_SHORT).show();
                    }

                }
            }.execute();
        }
    }


    private void collectPharmaCentreList(int episodeID, int episodePatientID) {
        pharmaListArraylist = new ArrayList<>();
        if (PHARMA_CENTRE_LIST.equals("")) {
            Log.d(Utils.TAG, "PHARMA_CENTRE_LIST EMPTY ");

            if (NetworkUtil.getConnectivityStatusString(mContext).equalsIgnoreCase("enabled")) {
                collectPharmaListFromServer(episodeID, episodePatientID);
            } else {
                AppUtils.showCustomAlertMessage(mContext, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            pharmaListArraylist = gson.fromJson(PHARMA_CENTRE_LIST, new TypeToken<List<PharmaCentreList>>() {
            }.getType());
            if(pharmaListArraylist.size() > 0 ) {
                Log.d(Utils.TAG, "PHARMA_CENTRE_LIST > 0 " + pharmaListArraylist.size());
                preparePharmaCentreData(pharmaListArraylist,episodeID, episodePatientID);
            }
        }
    }

    private void collectPharmaListFromServer(final int episodeID, final int episodePatientID) {
        pharmaListArraylist = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Phamacy lists...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PHARMA_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "pharma list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("pharma_details");
                                    if (jsonArray.length() > 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            pharmaListArraylist.add(new PharmaCentreList(jsonArray.getJSONObject(i).getInt("pharma_id"),
                                                    jsonArray.getJSONObject(i).getString("pharma_name"),jsonArray.getJSONObject(i).getString("pharma_city"),
                                                    jsonArray.getJSONObject(i).getString("pharma_state"),jsonArray.getJSONObject(i).getString("pharma_country"),
                                                    jsonArray.getJSONObject(i).getString("phrama_contact_person"),jsonArray.getJSONObject(i).getString("pharma_contact_num"),
                                                    jsonArray.getJSONObject(i).getString("pharma_email"),USER_ID, USER_LOGIN_TYPE));

                                        }

                                        //Set the values
                                        gson = new Gson();
                                        String jsonPharmaText = gson.toJson(pharmaListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearPharmaCentres();
                                            shareadPreferenceClass.setPharmaCentres(jsonPharmaText);
                                        }

                                        preparePharmaCentreDataNew(pharmaListArraylist,episodeID, episodePatientID);
                                    }
                                    else {
                                        //Set the values
                                        gson = new Gson();
                                        String jsonPharmaText = gson.toJson(pharmaListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearPharmaCentres();
                                            shareadPreferenceClass.setPharmaCentres(jsonPharmaText);
                                        }
                                        preparePharmaCentreDataNew(pharmaListArraylist,episodeID, episodePatientID);
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

        RequestQueue requestQueue = AppController.getInstance(mContext).
                getRequestQueue();
        AppController.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    private void preparePharmaCentreData(List<PharmaCentreList> pharmaListArraylist, final int episodeID, final int episodePatientID) {
        selectedPharmaArraylist = new ArrayList<>();

        final Dialog dialog = new Dialog(mContext, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_diagnostic_center);
        dialog.setTitle("Refer to Pharmacy");
        dialog.setCanceledOnTouchOutside(true);

        final LinearLayout addNew_contents = dialog.findViewById(R.id.diagnostic_addNew_content);
        addNew_contents.setVisibility(View.GONE);


        final LinearLayout list_contents = dialog.findViewById(R.id.diagnostic_list_content);
        list_contents.setVisibility(View.VISIBLE);

        final ImageView close_btn = (ImageView)  dialog.findViewById(R.id.diagnostic_close);
        final ImageView refresh_btn = (ImageView)  dialog.findViewById(R.id.diagnostic_refresh);
        CustomTextViewBold add_new_btn = (CustomTextViewBold) dialog.findViewById(R.id.diagnostic_add_new);
        final CustomTextViewBold submit_new_btn = (CustomTextViewBold) dialog.findViewById(R.id.edt_diagnostic_submit);
        final CustomTextViewBold cancel_new_btn = (CustomTextViewBold) dialog.findViewById(R.id.edt_diagnostic_cancel);

        final CustomEditText _edit_name = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_name);
        final CustomEditText _edit_email = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_email);
        final CustomEditText _edit_mobile = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_mobile);
        final CustomEditText _edit_city = (CustomEditText) dialog.findViewById(R.id.edt_diagnostic_city);
        CustomTextViewSemiBold title = (CustomTextViewSemiBold)dialog.findViewById(R.id.diagnostic_addNew_title);
        title.setText("Add New Pharmacy");
        _edit_name.setHint("Pharmacy Name");
        add_new_btn.setText("Add New Pharmacy");

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                InputMethodManager imm1 = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(close_btn.getWindowToken(), 0);
            }
        });

        add_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNew_contents.setVisibility(View.VISIBLE);
                list_contents.setVisibility(View.GONE);
            }
        });

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectPharmaListFromServer(episodeID,episodePatientID );
            }
        });

        submit_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm2 = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(submit_new_btn.getWindowToken(), 0);

                String pharma_name = _edit_name.getText().toString().trim();
                String pharma_email = _edit_email.getText().toString().trim();
                String pharma_mobile = _edit_mobile.getText().toString().trim();
                String pharma_city = _edit_city.getText().toString().trim();

                if(pharma_name.equals("")) {
                    Toast.makeText(mContext, "Enter Pharmacy Names", Toast.LENGTH_SHORT).show();
                }
                else  if((pharma_mobile.equals("")) || (pharma_mobile.length() <10)) {
                    Toast.makeText(mContext, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendPharmaNewToServer(pharma_name,pharma_email,pharma_mobile,pharma_city, episodeID, episodePatientID);
                    addNew_contents.setVisibility(View.GONE);
                    list_contents.setVisibility(View.VISIBLE);
                }
            }
        });

        cancel_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm3 = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(cancel_new_btn.getWindowToken(), 0);
                addNew_contents.setVisibility(View.GONE);
                list_contents.setVisibility(View.VISIBLE);
            }
        });

        final SearchView pharma_searchView = (SearchView) dialog.findViewById(R.id.searchview_diagnostic);
        ListView pharma_listview = (ListView) dialog.findViewById(R.id.diagnostic_list);

        final CustomPharmaCenterFilterAdapter pharmaAdapter = new CustomPharmaCenterFilterAdapter(mContext,
                R.layout.activity_listview, pharmaListArraylist);
        pharma_listview.setAdapter(pharmaAdapter);
        pharma_listview.setTextFilterEnabled(true);

        pharma_searchView.setIconifiedByDefault(false);
        pharma_searchView.setQueryHint("Search Pharmacy");
        pharma_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                pharma_searchView.setQuery("", false);
                pharma_searchView.clearFocus();
                pharma_searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Utils.TAG, "SERACH: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    Log.d(Utils.TAG, "is empty " );
                    pharmaAdapter.filter(newText.toString());
                    // specialization_listview.clearTextFilter();
                } else {
                    //  specialization_listview.setFilterText(newText.toString());
                    pharmaAdapter.filter(newText.toString());
                }
                return true;
            }
        });

        pharma_searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                pharma_searchView.setQuery("", false);
                pharma_searchView.setIconified(false);
                pharma_searchView.clearFocus();
                return false;
            }
        });

        pharma_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PharmaCentreList myCustomAdapterItem = (PharmaCentreList) parent.getItemAtPosition(position);
              /* Toast.makeText(getActivity(),  myCustomAdapterItem.getSymptomsName() + " added.",
                        Toast.LENGTH_SHORT)
                        .show();*/
                pharma_searchView.setQuery("", false);
                pharma_searchView.setIconified(false);
                pharma_searchView.clearFocus();

                selectedPharmaArraylist = new ArrayList<>();
                selectedPharmaArraylist.add(new PharmaCentreList(myCustomAdapterItem.getPharmaId(),myCustomAdapterItem.getPharmaName()));

                sendPharmacyReferralToServer(selectedPharmaArraylist, episodeID, episodePatientID);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void sendPharmaNewToServer(final String pharma_name, final String pharma_email, final String pharma_mobile, final String pharma_city, final int episodeID, final int episodePatientID) {
        pharmaListArraylist = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding New Pharmacy...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PHARMA_ADD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "pharma list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("pharma_details");
                                    if (jsonArray.length() > 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            pharmaListArraylist.add(new PharmaCentreList(jsonArray.getJSONObject(i).getInt("pharma_id"),
                                                    jsonArray.getJSONObject(i).getString("pharma_name"),jsonArray.getJSONObject(i).getString("pharma_city"),
                                                    jsonArray.getJSONObject(i).getString("pharma_state"),jsonArray.getJSONObject(i).getString("pharma_country"),
                                                    jsonArray.getJSONObject(i).getString("phrama_contact_person"),jsonArray.getJSONObject(i).getString("pharma_contact_num"),
                                                    jsonArray.getJSONObject(i).getString("pharma_email"),USER_ID, USER_LOGIN_TYPE));

                                        }

                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(pharmaListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearPharmaCentres();
                                            shareadPreferenceClass.setPharmaCentres(jsonDiagnoText);
                                        }

                                        preparePharmaCentreDataNew(pharmaListArraylist,episodeID, episodePatientID);
                                    }
                                    else {
                                        //Set the values
                                        gson = new Gson();
                                        String jsonDiagnoText = gson.toJson(pharmaListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearPharmaCentres();
                                            shareadPreferenceClass.setPharmaCentres(jsonDiagnoText);
                                        }
                                        preparePharmaCentreDataNew(pharmaListArraylist,episodeID, episodePatientID);
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
                map.put(APIClass.KEY_PHARMA_NAME, pharma_name);
                map.put(APIClass.KEY_PHARMA_EMAIL, pharma_email);
                map.put(APIClass.KEY_PHARMA_MOBILE, pharma_mobile);
                map.put(APIClass.KEY_PHARMA_CITY, pharma_city);
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(mContext).
                getRequestQueue();
        AppController.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    private void sendPharmacyReferralToServer(final List<PharmaCentreList> selectedPharmaArraylist, final int episodeID, final int episodePatientID) {
        new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {

                    JSONObject jsonObject = JSONParser.sendPharmaReferal(String.valueOf(episodePatientID), String.valueOf(episodeID),
                            selectedPharmaArraylist, USER_ID, USER_LOGIN_TYPE);

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

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext, R.style.CustomDialog);
                    alertDialogBuilder.setMessage("Referred to pharmacy successfully !!!. ");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(mContext, "Failed to send referal. \nTry later !!!", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }


    private void prepareCustomShareLink(final int episodeID, final int episodePatientID) {

        final Dialog dialog = new Dialog(mContext, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_emr_share);
        dialog.setTitle("Share EMR");
        dialog.setCanceledOnTouchOutside(true);

        ImageView close_btn = (ImageView) dialog.findViewById(R.id.emr_close);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        final RadioGroup radioGroupShare = (RadioGroup) dialog.findViewById(R.id.emr_share_rg);
        final RadioGroup radioGroupShare1 = (RadioGroup) dialog.findViewById(R.id.emr_share1_rg);
        final AppCompatRadioButton rd_patients = (AppCompatRadioButton) dialog.findViewById(R.id.emr_share_rb_patient);
        final AppCompatRadioButton rd_diagnostic = (AppCompatRadioButton) dialog.findViewById(R.id.emr_share_rb_diagno);
        final AppCompatRadioButton rd_pharmacy = (AppCompatRadioButton) dialog.findViewById(R.id.emr_share_rb_pharma);
        final AppCompatRadioButton rd_others = (AppCompatRadioButton) dialog.findViewById(R.id.emr_share_rb_others);
        final AppCompatRadioButton rd_opticals = (AppCompatRadioButton) dialog.findViewById(R.id.emr_share_rb_optical);

        final LinearLayout share_what_content = (LinearLayout) dialog.findViewById(R.id.emr_share_what_content);
        share_what_content.setVisibility(View.GONE);
        final LinearLayout share_patient_content = (LinearLayout) dialog.findViewById(R.id.emr_share_patient_content);
        share_patient_content.setVisibility(View.GONE);
        final LinearLayout share_other_content = (LinearLayout) dialog.findViewById(R.id.emr_share_other_content);
        share_other_content.setVisibility(View.GONE);
        final LinearLayout share_opticals_content = (LinearLayout) dialog.findViewById(R.id.emr_share_opticals_content);
        share_opticals_content.setVisibility(View.GONE);
        final LinearLayout share_diagnostic_content = (LinearLayout) dialog.findViewById(R.id.emr_share_diagnosis_content);
        share_diagnostic_content.setVisibility(View.GONE);
        diagnostic_multispinner = (MultiSpinnerSearch) dialog.findViewById(R.id.searchMultiSpinner_diagnostics);
        opticals_multispinner = (MultiSpinnerSearch) dialog.findViewById(R.id.searchMultiSpinner_opticals);
        final LinearLayout share_pharma_content = (LinearLayout) dialog.findViewById(R.id.emr_share_pharma_content);
        pharmacy_multispinner  = (MultiSpinnerSearch) dialog.findViewById(R.id.searchMultiSpinner_pharmacy);

        ImageView diagnosis_refresh = (ImageView) dialog.findViewById(R.id.emr_share_diagnosis_refresh);
        diagnosis_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtil.getConnectivityStatusString(mContext).equalsIgnoreCase("enabled")) {
                    collectDiagnosticsListFromServer(episodeID, episodePatientID);
                } else {
                    AppUtils.showCustomAlertMessage(mContext, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
            }
        });

        ImageView pharmacy_refresh = (ImageView) dialog.findViewById(R.id.emr_share_pharmacy_refresh);
        pharmacy_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtil.getConnectivityStatusString(mContext).equalsIgnoreCase("enabled")) {
                    collectPharmaListFromServer(episodeID, episodePatientID);
                } else {
                    AppUtils.showCustomAlertMessage(mContext, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
            }
        });

        ImageView opticals_refresh = (ImageView) dialog.findViewById(R.id.emr_share_opticals_refresh);
        opticals_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtil.getConnectivityStatusString(mContext).equalsIgnoreCase("enabled")) {
                    collectOpticalsListFromServer(episodeID, episodePatientID);
                } else {
                    AppUtils.showCustomAlertMessage(mContext, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                }
            }
        });

        rd_patients.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    if (compoundButton.getId() == R.id.emr_share_rb_patient) {
                        rd_patients.setChecked(true);
                        rd_diagnostic.setChecked(false);
                        rd_pharmacy.setChecked(false);
                        rd_others.setChecked(false);
                        rd_opticals.setChecked(false);
                        share_what_content.setVisibility(View.VISIBLE);
                        share_patient_content.setVisibility(View.VISIBLE);
                        share_diagnostic_content.setVisibility(View.GONE);
                        share_pharma_content.setVisibility(View.GONE);
                        share_other_content.setVisibility(View.GONE);
                        share_opticals_content.setVisibility(View.GONE);
                    }
                }
            }
        });

        rd_diagnostic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    if (compoundButton.getId() == R.id.emr_share_rb_diagno) {
                        rd_patients.setChecked(false);
                        rd_diagnostic.setChecked(true);
                        rd_pharmacy.setChecked(false);
                        rd_others.setChecked(false);
                        rd_opticals.setChecked(false);
                        share_what_content.setVisibility(View.VISIBLE);
                        share_patient_content.setVisibility(View.GONE);
                        share_pharma_content.setVisibility(View.GONE);
                        share_other_content.setVisibility(View.GONE);
                        share_opticals_content.setVisibility(View.GONE);
                        share_diagnostic_content.setVisibility(View.VISIBLE);
                        getDiagnosticDetails(episodeID, episodePatientID);
                    }
                }
            }
        });

        rd_pharmacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    if (compoundButton.getId() == R.id.emr_share_rb_pharma) {
                        rd_patients.setChecked(false);
                        rd_diagnostic.setChecked(false);
                        rd_pharmacy.setChecked(true);
                        rd_others.setChecked(false);
                        rd_opticals.setChecked(false);
                        share_what_content.setVisibility(View.VISIBLE);
                        share_patient_content.setVisibility(View.GONE);
                        share_diagnostic_content.setVisibility(View.GONE);
                        share_other_content.setVisibility(View.GONE);
                        share_opticals_content.setVisibility(View.GONE);
                        share_pharma_content.setVisibility(View.VISIBLE);
                        getPharmacyDetails(episodeID, episodePatientID);
                    }
                }
            }
        });

        rd_others.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    if (compoundButton.getId() == R.id.emr_share_rb_others) {
                        rd_patients.setChecked(false);
                        rd_diagnostic.setChecked(false);
                        rd_pharmacy.setChecked(false);
                        rd_opticals.setChecked(false);
                        rd_others.setChecked(true);
                        share_what_content.setVisibility(View.VISIBLE);
                        share_patient_content.setVisibility(View.GONE);
                        share_diagnostic_content.setVisibility(View.GONE);
                        share_pharma_content.setVisibility(View.GONE);
                        share_opticals_content.setVisibility(View.GONE);
                        share_other_content.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        rd_opticals.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    if (compoundButton.getId() == R.id.emr_share_rb_optical) {
                        rd_patients.setChecked(false);
                        rd_diagnostic.setChecked(false);
                        rd_pharmacy.setChecked(false);
                        rd_others.setChecked(false);
                        rd_opticals.setChecked(true);
                        share_what_content.setVisibility(View.VISIBLE);
                        share_patient_content.setVisibility(View.GONE);
                        share_diagnostic_content.setVisibility(View.GONE);
                        share_pharma_content.setVisibility(View.GONE);
                        share_other_content.setVisibility(View.GONE);
                        share_opticals_content.setVisibility(View.VISIBLE);
                        getOpticalCenterDetails(episodeID, episodePatientID);
                    }
                }
            }
        });

     /*   radioGroupShare.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.emr_share_rb_patient) {
                    share_what_content.setVisibility(View.VISIBLE);
                    share_patient_content.setVisibility(View.VISIBLE);
                    share_diagnostic_content.setVisibility(View.GONE);
                    share_pharma_content.setVisibility(View.GONE);
//                    rd_patients.setChecked(true);
//                    rd_diagnostic.setChecked(false);
//                    rd_pharmacy.setChecked(false);
//                    rd_others.setChecked(false);
                } else  if (checkedId == R.id.emr_share_rb_diagno) {
                    share_what_content.setVisibility(View.VISIBLE);
                    share_patient_content.setVisibility(View.GONE);
                    share_pharma_content.setVisibility(View.GONE);
                    share_diagnostic_content.setVisibility(View.VISIBLE);
//                    rd_patients.setChecked(false);
//                    rd_diagnostic.setChecked(true);
//                    rd_pharmacy.setChecked(false);
//                    rd_others.setChecked(false);
                    getDiagnosticDetails(episodeID, episodePatientID);
                } *//*else  if (checkedId == R.id.emr_share_rb_pharma) {
                    share_what_content.setVisibility(View.VISIBLE);
                    share_patient_content.setVisibility(View.GONE);
                    share_diagnostic_content.setVisibility(View.GONE);
                    share_pharma_content.setVisibility(View.VISIBLE);
                    getPharmacyDetails(episodeID, episodePatientID);
                }
                else  if (checkedId == R.id.emr_share_rb_others) {
                    share_what_content.setVisibility(View.VISIBLE);
                    share_patient_content.setVisibility(View.VISIBLE);
                    share_diagnostic_content.setVisibility(View.GONE);
                    share_pharma_content.setVisibility(View.GONE);
                }*//*

            }
        });

        radioGroupShare1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                if (checkedId == R.id.emr_share_rb_pharma) {
                    share_what_content.setVisibility(View.VISIBLE);
                    share_patient_content.setVisibility(View.GONE);
                    share_diagnostic_content.setVisibility(View.GONE);
                    share_pharma_content.setVisibility(View.VISIBLE);

//                    rd_patients.setChecked(false);
//                    rd_diagnostic.setChecked(false);
//                    rd_pharmacy.setChecked(true);
//                    rd_others.setChecked(false);

                    getPharmacyDetails(episodeID, episodePatientID);

                }
                else  if (checkedId == R.id.emr_share_rb_others) {
                    share_what_content.setVisibility(View.VISIBLE);
                    share_patient_content.setVisibility(View.VISIBLE);
                    share_diagnostic_content.setVisibility(View.GONE);
                    share_pharma_content.setVisibility(View.GONE);
//                    rd_patients.setChecked(false);
//                    rd_diagnostic.setChecked(false);
//                    rd_pharmacy.setChecked(false);
//                    rd_others.setChecked(true);
                }
            }
        });*/

        // Share Patient Contents
        RadioGroup radioGroupPatientShare = (RadioGroup) dialog.findViewById(R.id.emr_share_patient_rg);
        radioGroupPatientShare.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.emr_share_rb_emr_only) {
                    //  shareEMRLink( episodeID,  episodePatientID);
                    sendPrescriptionToPatient(1, episodeID,  episodePatientID); // 1- Send SMS
                } else  if (checkedId == R.id.emr_share_rb_presc_only) {
                    //  sharePrescriptions(episodeID, episodePatientID);
                    sendPrescriptionToPatient(2, episodeID,  episodePatientID); // 2- Send EMAIL
                }
                dialog.dismiss();
            }
        });

        // Share Others Contents
        RadioGroup radioGroupOthersShare = (RadioGroup) dialog.findViewById(R.id.emr_share_other_rg);
        radioGroupOthersShare.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.emr_share_other_rb_emr_only) {
                    shareEMRLink( episodeID,  episodePatientID);
                } else  if (checkedId == R.id.emr_share_other_rb_presc_only) {
                    sharePrescriptions(episodeID, episodePatientID);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getDiagnosticDetails(int episodeID, int episodePatientID) {
        diagnosticsListArraylist = new ArrayList<>();
        selectedDiagnoCentreArraylist = new ArrayList<>();
        if (DIAGNOSTIC_CENTRE_LIST.equals("")) {
            Log.d(Utils.TAG, "DIAGNOSTIC_CENTRE_LIST EMPTY ");

            if (NetworkUtil.getConnectivityStatusString(mContext).equalsIgnoreCase("enabled")) {
                collectDiagnosticsListFromServer(episodeID, episodePatientID);
            } else {
                AppUtils.showCustomAlertMessage(mContext, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            diagnosticsListArraylist = gson.fromJson(DIAGNOSTIC_CENTRE_LIST, new TypeToken<List<DiagnosticCentreList>>() {
            }.getType());
            if(diagnosticsListArraylist.size() > 0 ) {
                Log.d(Utils.TAG, "DIAGNOSTIC_CENTRE_LIST > 0 " + diagnosticsListArraylist.size());
                prepareDiagnostiCentreDataNew(diagnosticsListArraylist,episodeID, episodePatientID);
            }
        }
    }

    private void prepareDiagnostiCentreDataNew(List<DiagnosticCentreList> diagnosticsListArraylist, final int episodeID, final int episodePatientID) {
        selectedDiagnoCentreArraylist = new ArrayList<>();
        final List<KeyPairBoolData> listArray = new ArrayList<KeyPairBoolData>();
        for(int i=0; i<diagnosticsListArraylist.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(diagnosticsListArraylist.get(i).getDiagnoId());
            h.setName(diagnosticsListArraylist.get(i).getDiagnoName());
            h.setSelected(false);
            listArray.add(h);
        }

        diagnostic_multispinner.setItems(listArray,  -1, new SpinnerListener() {

            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        selectedDiagnoCentreArraylist = new ArrayList<>();
                        Log.i(Utils.TAG, i + " name: " + items.get(i).getName() + " id: " + items.get(i).getId());
                        selectedDiagnoCentreArraylist.add(new DiagnosticCentreList(Integer.parseInt(String.valueOf(items.get(i).getId())), items.get(i).getName()));
                    }
                }

               /* Log.i(Utils.TAG, "selectedDiagnoCentreArraylist: "+selectedDiagnoCentreArraylist.size());
                for (int i = 0; i < selectedDiagnoCentreArraylist.size(); i++) {
                    Log.i(Utils.TAG, " id: "+ selectedDiagnoCentreArraylist.get(i).getDiagnoId()+" name: "+ selectedDiagnoCentreArraylist.get(i).getDiagnoName());
                }*/

                if(selectedDiagnoCentreArraylist.size() > 0) {
                    sendReferDiagnoRequestToserver(selectedDiagnoCentreArraylist,episodeID, episodePatientID);
                }
            }
        });
    }

    private void getPharmacyDetails(int episodeID, int episodePatientID) {
        pharmaListArraylist = new ArrayList<>();
        if (PHARMA_CENTRE_LIST.equals("")) {
            Log.d(Utils.TAG, "PHARMA_CENTRE_LIST EMPTY ");

            if (NetworkUtil.getConnectivityStatusString(mContext).equalsIgnoreCase("enabled")) {
                collectPharmaListFromServer(episodeID, episodePatientID);
            } else {
                AppUtils.showCustomAlertMessage(mContext, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            pharmaListArraylist = gson.fromJson(PHARMA_CENTRE_LIST, new TypeToken<List<PharmaCentreList>>() {
            }.getType());
            if(pharmaListArraylist.size() > 0 ) {
                Log.d(Utils.TAG, "PHARMA_CENTRE_LIST > 0 " + pharmaListArraylist.size());
                preparePharmaCentreDataNew(pharmaListArraylist,episodeID, episodePatientID);
            }
        }
    }

    private void preparePharmaCentreDataNew(List<PharmaCentreList> pharmaListArraylist, final int episodeID, final int episodePatientID) {
        selectedPharmaArraylist = new ArrayList<>();
        final List<KeyPairBoolData> listArray = new ArrayList<KeyPairBoolData>();
        for(int i=0; i<pharmaListArraylist.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(pharmaListArraylist.get(i).getPharmaId());
            h.setName(pharmaListArraylist.get(i).getPharmaName());
            h.setSelected(false);
            listArray.add(h);
        }

        pharmacy_multispinner.setItems(listArray,  -1, new SpinnerListener() {

            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        selectedPharmaArraylist = new ArrayList<>();
                        Log.i(Utils.TAG, i + " name: " + items.get(i).getName() + " id: " + items.get(i).getId());
                        selectedPharmaArraylist.add(new PharmaCentreList(Integer.parseInt(String.valueOf(items.get(i).getId())), items.get(i).getName()));
                    }
                }

               /* Log.i(Utils.TAG, "selectedPharmaArraylist: "+selectedPharmaArraylist.size());
                for (int i = 0; i < selectedPharmaArraylist.size(); i++) {
                    Log.i(Utils.TAG, " id: "+ selectedPharmaArraylist.get(i).getPharmaId()+" name: "+ selectedPharmaArraylist.get(i).getPharmaName());
                }*/

                if(selectedPharmaArraylist.size() > 0) {
                    sendPharmacyReferralToServer(selectedPharmaArraylist, episodeID, episodePatientID);
                }
            }
        });
    }

    private void sendPrescriptionToPatient(final int send_presc_type, final int episodeID, final int episodePatientID) {   // 1- SMS, 2- EMAIL

        Log.d(Utils.TAG, " send_presc_type: "+send_presc_type);

        new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {

                    JSONObject jsonObject = JSONParser.sendPrescToPatient(episodePatientID,episodeID,
                            USER_ID, USER_LOGIN_TYPE,send_presc_type, USER_NAME);

                    if (jsonObject != null) {
                        Log.e(Utils.TAG, " GET: " + jsonObject.getString("result"));
                        if (jsonObject.getString("result").equals("true")) {
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
                    String success_msg = "";
                    if(send_presc_type == 1) {
                        success_msg = "EMR sent successfully !!!.";
                    }
                    else  if(send_presc_type == 2) {
                        success_msg = "Prescription sent successfully !!!.";
                    }
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext, R.style.CustomDialog);
                    alertDialogBuilder.setMessage(success_msg);
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(mContext, "Failed to send. \nTry later !!!", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }

    private void getOpticalCenterDetails(int episodeID, int episodePatientID) {
        opticalsListArraylist = new ArrayList<>();
        selectedOpticalCentreArraylist = new ArrayList<>();
        if (DIAGNOSTIC_CENTRE_LIST.equals("")) {
            Log.d(Utils.TAG, "OPTICAL_CENTRE_LIST EMPTY ");

            if (NetworkUtil.getConnectivityStatusString(mContext).equalsIgnoreCase("enabled")) {
                collectOpticalsListFromServer(episodeID, episodePatientID);
            } else {
                AppUtils.showCustomAlertMessage(mContext, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            opticalsListArraylist = gson.fromJson(OPTICAL_CENTRE_LIST, new TypeToken<List<OpticalCentreList>>() {
            }.getType());
            if(opticalsListArraylist.size() > 0 ) {
                Log.d(Utils.TAG, "OPTICAL_CENTRE_LIST > 0 " + opticalsListArraylist.size());
                prepareOpticalCentreDataNew(opticalsListArraylist,episodeID, episodePatientID);
            }
        }
    }

    private void prepareOpticalCentreDataNew(List<OpticalCentreList> opticalsListArraylist, final int episodeID, final int episodePatientID) {
        selectedOpticalCentreArraylist = new ArrayList<>();

        final List<KeyPairBoolData> listArray = new ArrayList<KeyPairBoolData>();
        for(int i=0; i<opticalsListArraylist.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(opticalsListArraylist.get(i).getOpticalId());
            h.setName(opticalsListArraylist.get(i).getOpticalName());
            h.setSelected(false);
            listArray.add(h);
        }

        opticals_multispinner.setItems(listArray,  -1, new SpinnerListener() {

            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        selectedOpticalCentreArraylist = new ArrayList<>();
                        Log.i(Utils.TAG, i + " name: " + items.get(i).getName() + " id: " + items.get(i).getId());
                        selectedOpticalCentreArraylist.add(new OpticalCentreList(Integer.parseInt(String.valueOf(items.get(i).getId())), items.get(i).getName()));
                    }
                }

                if(selectedOpticalCentreArraylist.size() > 0) {
                    sendReferOpticalsRequestToserver(selectedOpticalCentreArraylist,episodeID, episodePatientID);
                }
            }
        });

    }

    private void collectOpticalsListFromServer(final int episodeID, final int episodePatientID) {
        opticalsListArraylist = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Optical Centers...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_OPTICALS_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "optical list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("opticals_details");
                                    if (jsonArray.length() > 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            opticalsListArraylist.add(new OpticalCentreList(jsonArray.getJSONObject(i).getInt("optical_id"),
                                                    jsonArray.getJSONObject(i).getString("optical_name"),jsonArray.getJSONObject(i).getString("optical_city"),
                                                    jsonArray.getJSONObject(i).getString("optical_state"),jsonArray.getJSONObject(i).getString("optical_country"),
                                                    jsonArray.getJSONObject(i).getString("optical_contact_person"),jsonArray.getJSONObject(i).getString("optical_contact_num"),
                                                    jsonArray.getJSONObject(i).getString("optical_email"),USER_ID, USER_LOGIN_TYPE));

                                        }
                                        //Set the values
                                        gson = new Gson();
                                        String jsonOpticalText = gson.toJson(opticalsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearOpticalCentres();
                                            shareadPreferenceClass.setOpticalCentres(jsonOpticalText);
                                        }

                                        prepareOpticalCentreDataNew(opticalsListArraylist,episodeID, episodePatientID);

                                    }
                                    else {
                                        //Set the values
                                        gson = new Gson();
                                        String jsonOpticalText = gson.toJson(opticalsListArraylist);
                                        if (sharedPreferences != null) {
                                            shareadPreferenceClass.clearOpticalCentres();
                                            shareadPreferenceClass.setOpticalCentres(jsonOpticalText);
                                        }
                                        prepareOpticalCentreDataNew(opticalsListArraylist,episodeID, episodePatientID);
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

        RequestQueue requestQueue = AppController.getInstance(mContext).
                getRequestQueue();
        AppController.getInstance(mContext).addToRequestQueue(stringRequest);
    }


    private void sendReferOpticalsRequestToserver(final List<OpticalCentreList> selectedOpticalCentreArraylist, final int episodeID, final int episodePatientID) {

        if(selectedOpticalCentreArraylist.size() > 0) {
            for (int i = 0; i < selectedOpticalCentreArraylist.size(); i++) {
                Log.i(Utils.TAG, selectedOpticalCentreArraylist.get(i).getOpticalId() + " : " + selectedOpticalCentreArraylist.get(i).getOpticalName());
            }
            Log.d(Utils.TAG, " opticsSize " + selectedOpticalCentreArraylist.size());

            new AsyncTask<Void, Integer, Boolean>() {
                boolean status = false;
                ProgressDialog progressDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();

                }

                @Override
                protected Boolean doInBackground(Void... params) {

                    try {

                        JSONObject jsonObject = JSONParser.sendOpticalsReferal(String.valueOf(episodePatientID), String.valueOf(episodeID),
                                selectedOpticalCentreArraylist, USER_ID, USER_LOGIN_TYPE);

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

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext, R.style.CustomDialog);
                        alertDialogBuilder.setMessage("Referred to optical center successfully !!!. ");
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {
                        Toast.makeText(mContext, "Failed to refer optical center. \nTry later !!!", Toast.LENGTH_SHORT).show();
                    }

                }
            }.execute();
        }
    }
}
