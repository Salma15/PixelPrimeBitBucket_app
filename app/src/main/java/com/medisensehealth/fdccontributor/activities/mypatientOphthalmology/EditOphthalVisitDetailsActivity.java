package com.medisensehealth.fdccontributor.activities.mypatientOphthalmology;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
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
import com.medisensehealth.fdccontributor.DataModel.ChiefMedicalComplaint;
import com.medisensehealth.fdccontributor.DataModel.Diagnosis;
import com.medisensehealth.fdccontributor.DataModel.FrequentChiefComplaints;
import com.medisensehealth.fdccontributor.DataModel.FrequentInvestigations;
import com.medisensehealth.fdccontributor.DataModel.FrequentPrescription;
import com.medisensehealth.fdccontributor.DataModel.InvestigationGroupTests;
import com.medisensehealth.fdccontributor.DataModel.InvestigationTemplates;
import com.medisensehealth.fdccontributor.DataModel.Investigations;
import com.medisensehealth.fdccontributor.DataModel.Lids;
import com.medisensehealth.fdccontributor.DataModel.MyTemplates;
import com.medisensehealth.fdccontributor.DataModel.OldVisitsOphthalList;
import com.medisensehealth.fdccontributor.DataModel.OphthalAngleAnteriorChamber;
import com.medisensehealth.fdccontributor.DataModel.OphthalAnteriorChamber;
import com.medisensehealth.fdccontributor.DataModel.OphthalConjuctiva;
import com.medisensehealth.fdccontributor.DataModel.OphthalCornearAnteriorSurface;
import com.medisensehealth.fdccontributor.DataModel.OphthalCornearPosteriorSurface;
import com.medisensehealth.fdccontributor.DataModel.OphthalFundus;
import com.medisensehealth.fdccontributor.DataModel.OphthalIris;
import com.medisensehealth.fdccontributor.DataModel.OphthalLens;
import com.medisensehealth.fdccontributor.DataModel.OphthalPupil;
import com.medisensehealth.fdccontributor.DataModel.OphthalSclera;
import com.medisensehealth.fdccontributor.DataModel.OphthalViterous;
import com.medisensehealth.fdccontributor.DataModel.OtherSettings;
import com.medisensehealth.fdccontributor.DataModel.PatientEducation;
import com.medisensehealth.fdccontributor.DataModel.Treatments;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.settings.ProfileActivity;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.ChiefMedicalAdapters;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.CustomDiagnosisFilterAdapter;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.CustomInvestigationFilterAdapter;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.CustomMedicalComplaintFilterAdapter;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.CustomPrescriptionFilterAdapter;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.CustomTreatmentFilterAdapter;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.DiagnosisAdapters;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.FrequentComplaintAdapters;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.FrequentDiagnosisAdapters;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.FrequentInvestigationAdapters;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.FrequentPrescriptionsAdapters;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.FrequentTreatmentAdapters;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.InvestigationAdapters;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.InvestigationClinicalAdapters;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.InvestigationOphthalAdapters;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.InvestigationOtherAdapters;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.InvestigationRadiologyAdapters;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.InvestigationTemplateAdapters;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.PrescriptionAdapters;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.TreatmentsAdapters;
import com.medisensehealth.fdccontributor.database.MedisensePracticeDB;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.parser.JSONParser;
import com.medisensehealth.fdccontributor.utils.AppController;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by SALMA on 16-09-2018.
 */
public class EditOphthalVisitDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    int EPISODE_ID, PATIENT_ID, HOSPITAL_ID;
    String FOLLOWUP_DATE, DIAGNOSIS_DETAILS, TREATMENT_DETAILS, EPISODE_CREATED_DATE, CONSULTATION_FEES, PRESCRIPTION_NOTES, CHIEFCOMPALINT_SUFFERINGS;
    String TEMPLATES_LIST, CONSULTATION_CHARGES, COLLECT_CONSULT_BEFORE, OTHER_SETTINGS_LIST;
    List<OldVisitsOphthalList> EPISODE_LIST = new ArrayList<>();
    List<ChiefMedicalComplaint> PATIENT_CHIEF_MEDCOMPLAINT_ARRAY;
    List<Investigations> PATIENT_INVESTIGATION_GENERAL_ARRAY, PATIENT_INVESTIGATION_OPHTHAL_ARRAY,PATIENT_INVESTIGATION_ARRAY,PATIENT_INVESTIGATION_RADIOLOGY_ARRAY, PATIENT_INVESTIGATION_CLINICAL_ARRAY, PATIENT_INVESTIGATION_OTHER_ARRAY;
    List<Diagnosis> PATIENT_DAIGNOSIS_ARRAY;
    List<Treatments> PATIENT_TREATMENT_ARRAY;
    List<FrequentPrescription> PATIENT_PRESCRIPTION_ARRAY;

    String GET_DV_SPHERE_RIGHT, GET_DV_CYCLE_RIGHT, GET_DV_AXIS_RIGHT;
    String GET_NV_SPHERE_RIGHT, GET_NV_CYCLE_RIGHT, GET_NV_AXIS_RIGHT;
    String GET_IPD_RIGHT;
    String GET_DV_SPHERE_LEFT, GET_DV_CYCLE_LEFT, GET_DV_AXIS_LEFT;
    String GET_NV_SPHERE_LEFT, GET_NV_CYCLE_LEFT, GET_NV_AXIS_LEFT;
    String GET_IPD_LEFT;
    String REFRACTION_RE_VALUE1, REFRACTION_RE_VALUE2, REFRACTION_LE_VALUE1, REFRACTION_LE_VALUE2;

    int USER_ID;
    String USER_NAME,USER_LOGIN_TYPE, DIAGNOSTIC_CENTRE_LIST, PHARMA_CENTRE_LIST;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;

    int numberOfFrequentColumns = 4;

    LinearLayout chief_medcomp_btn, chief_medcomp_contents;
    boolean view_chief_medcomp_status = false;
    List<ChiefMedicalComplaint> chiefcomplaintListArraylist;
    SearchView medicalComplaint_searchView;
    ListView medicalComplaint_listview;
    ImageView add_chiefMedCompalint;
   // List<ChiefMedicalComplaint> PATIENT_CHIEF_MEDCOMPLAINT_ARRAY;
    private ChiefMedicalAdapters medcomplaintAdapter;
    RecyclerView chiefComplaintRecyclerview;
    List<FrequentChiefComplaints> freqComplaintListArraylist;
    private FrequentComplaintAdapters frequentcomplaintAdapter;
    RecyclerView frequentComplaintRecyclerview;
    CustomEditText chiefMedComplaint_sufferings;

    CustomTextViewBold add_visit_btn, save_pdf_btn;
    CustomEditText _edt_consultency_fee, _edt_diagnosis_deatils, _edt_treatment_details;
    CustomTextView _edt_followup_date, _edt_visit_date;
    Calendar myCalendar;
    String get_followup_date;
    Button submit_btn;

    Spinner distance_vision_re_spinner,  distance_vision_le_spinner, near_vision_re_spinner,  near_vision_le_spinner;
    Spinner distance_vision_unaided_re_spinner,  distance_vision_unaided_le_spinner, near_vision_unaided_re_spinner,  near_vision_unaided_le_spinner, iop_re_spinner, iop_le_spinner;
    String PATIENT_DISTANCE_VISION_RE = "", PATIENT_DISTANCE_VISION_LE = "", PATIENT_NEAR_VISION_RE = "", PATIENT_NEAR_VISION_LE = "";
    String PATIENT_DISTANCE_VISION_UNAIDED_RE= "", PATIENT_DISTANCE_VISION_UNAIDED_LE= "", PATIENT_NEAR_VISION_UNAIDED_RE= "", PATIENT_NEAR_VISION_UNAIDED_LE= "", PATIENT_IOP_RE= "", PATIENT_IOP_LE= "";

    LinearLayout examinations_btn, examinations_contents;
    boolean view_examinations_status = false;
    LinearLayout lids_gridview_RE, lids_gridview_LE;

    List<Lids> lidsListArraylist, PATIENT_LIDS_ARRAY, PATIENT_LIDS_INIT_ARRAY;
    LinearLayout lids_btn, lids_contents;
    boolean view_lids_status = false;
    ImageView add_lids, ophthal_lids_image;
    AutoCompleteTextView _edt_add_lids;

    LinearLayout conjuctiva_btn, conjuctiva_contents;
    boolean view_conjuctiva_status = false;
    LinearLayout conjuctiva_gridview_RE, conjuctiva_gridview_LE;
    ImageView add_conjuctiva, ophthal_conjuctiva_image;
    AutoCompleteTextView _edt_add_conjuctiva;
    List<OphthalConjuctiva> conjuctivaListArraylist, PATIENT_CONJUCTIVA_ARRAY, PATIENT_CONJUCTIVA_INIT_ARRAY;

    LinearLayout sclera_btn, sclera_contents;
    boolean view_sclera_status = false;
    LinearLayout sclera_gridview_RE, sclera_gridview_LE;
    ImageView add_sclera, ophthal_sclera_image;
    AutoCompleteTextView _edt_add_sclera;
    List<OphthalSclera> scleraListArraylist, PATIENT_SCLERA_ARRAY, PATIENT_SCLERA_INIT_ARRAY;

    LinearLayout cornea_anterior_btn, cornea_anterior_contents;
    boolean view_cornea_anterior_status = false;
    LinearLayout cornea_anterior_gridview_RE, cornea_anterior_gridview_LE;
    ImageView add_cornea_anterior, ophthal_cornea_anterior_image;
    AutoCompleteTextView _edt_add_cornea_anterior;
    List<OphthalCornearAnteriorSurface> corneaAnteriorListArraylist, PATIENT_CORNEA_ANTERIOR_ARRAY, PATIENT_CORNEA_ANTERIOR_INIT_ARRAY;

    LinearLayout cornea_posterior_btn, cornea_posterior_contents;
    boolean view_cornea_posterior_status = false;
    LinearLayout cornea_posterior_gridview_RE, cornea_posterior_gridview_LE;
    ImageView add_cornea_posterior, ophthal_cornea_posterior_image;
    AutoCompleteTextView _edt_add_cornea_posterior;
    List<OphthalCornearPosteriorSurface> corneaPosteriorListArraylist, PATIENT_CORNEA_POSTERIOR_ARRAY, PATIENT_CORNEA_POSTERIOR_INIT_ARRAY;

    LinearLayout anterior_chamber_btn, anterior_chamber_contents;
    boolean view_anterior_chamber_status = false;
    LinearLayout anterior_chamber_gridview_RE, anterior_chamber_gridview_LE;
    ImageView add_anterior_chamber, ophthal_anterior_chamber_image;
    AutoCompleteTextView _edt_add_anterior_chamber;
    List<OphthalAnteriorChamber> anteriorChamberListArraylist, PATIENT_ANTERIOR_CHAMBER_ARRAY, PATIENT_ANTERIOR_CHAMBER_INIT_ARRAY;

    LinearLayout iris_btn, iris_contents;
    boolean view_iris_status = false;
    LinearLayout iris_gridview_RE, iris_gridview_LE;
    ImageView add_iris, ophthal_iris_image;
    AutoCompleteTextView _edt_add_iris;
    List<OphthalIris> irisListArraylist, PATIENT_IRIS_ARRAY, PATIENT_IRIS_INIT_ARRAY;

    LinearLayout pupil_btn, pupil_contents;
    boolean view_pupil_status = false;
    LinearLayout pupil_gridview_RE, pupil_gridview_LE;
    ImageView add_pupil, ophthal_pupil_image;
    AutoCompleteTextView _edt_add_pupil;
    List<OphthalPupil> pupilListArraylist, PATIENT_PUPIL_ARRAY, PATIENT_PUPIL_INIT_ARRAY;

    LinearLayout angle_btn, angle_contents;
    boolean view_angle_status = false;
    LinearLayout angle_gridview_RE, angle_gridview_LE;
    ImageView add_angle, ophthal_angle_image;
    AutoCompleteTextView _edt_add_angle;
    List<OphthalAngleAnteriorChamber> angleListArraylist, PATIENT_ANGLE_ARRAY, PATIENT_ANGLE_INIT_ARRAY;

    LinearLayout lens_btn, lens_contents;
    boolean view_lens_status = false;
    LinearLayout lens_gridview_RE, lens_gridview_LE;
    ImageView add_lens, ophthal_lens_image;
    AutoCompleteTextView _edt_add_lens;
    List<OphthalLens> lensListArraylist, PATIENT_LENS_ARRAY, PATIENT_LENS_INIT_ARRAY;

    LinearLayout viterous_btn, viterous_contents;
    boolean view_viterous_status = false;
    LinearLayout viterous_gridview_RE, viterous_gridview_LE;
    ImageView add_viterous, ophthal_viterous_image;
    AutoCompleteTextView _edt_add_viterous;
    List<OphthalViterous> viterousListArraylist, PATIENT_VITEROUS_ARRAY, PATIENT_VITEROUS_INIT_ARRAY;

    LinearLayout fundus_btn,fundus_contents;
    boolean view_fundus_status = false;
    LinearLayout fundus_gridview_RE, fundus_gridview_LE;
    ImageView add_fundus, ophthal_fundus_image;
    AutoCompleteTextView _edt_add_fundus;
    List<OphthalFundus> fundusListArraylist, PATIENT_FUNDUS_ARRAY, PATIENT_FUNDUS_INIT_ARRAY;

    LinearLayout refraction_btn,refraction_contents;
    boolean view_refraction_status = false;
    ImageView  ophthal_refraction_image;

    CustomEditText _edt_refraction_RE_top, _edt_refraction_RE_bottom, _edt_refraction_LE_top, _edt_refraction_LE_bottom;

    // Investigation Tests
    LinearLayout investigation_btn, investigation_contents;
    boolean view_investigation_status = false;
    List<Investigations> investigationsListArraylist;
    List<FrequentInvestigations> freqInvestigationsListArraylist;
    List<InvestigationGroupTests> grouptestListArraylist;
   // List<Investigations> PATIENT_INVESTIGATION_ARRAY;
  //  List<Investigations> PATIENT_INVESTIGATION_GENERAL_ARRAY, PATIENT_INVESTIGATION_OPHTHAL_ARRAY;
    private FrequentInvestigationAdapters frequentInvestAdapter;
    RecyclerView frequentInvestRecyclerview;
    private InvestigationAdapters investigationAdapter;
    RecyclerView investigationRecyclerview;
    LinearLayout investigation_tab_header;
    LinearLayout investigation_ophthal_tab_header, investigation_radiology_tab_header, investigation_clinical_tab_header, investigation_oother_tab_header;
    RecyclerView investigationOphthalRecyclerview;
    private InvestigationOphthalAdapters investigationOphthalAdapter;
    RecyclerView investigationRadiologyRecyclerview;
    private InvestigationRadiologyAdapters investigationRadiologyAdapter;
    RecyclerView investigationClinicalRecyclerview;
    private InvestigationClinicalAdapters investigationClinicalAdapter;
    RecyclerView investigationOtherRecyclerview;
    private InvestigationOtherAdapters investigationOtherAdapter;

    LinearLayout investigation_template_layout;
    RecyclerView investigationTemplateRecyclerview;
    List<InvestigationTemplates> investigationTemplateListArraylist;
    private InvestigationTemplateAdapters investigationTemplateAdapter;
    AppCompatCheckBox investigationaddtemplate_checkbox;
    CustomEditText investigation_addtemplate_name;
    int investigation_template_save = 0;

    // Diagnosis
    LinearLayout diagnosis_btn, diagnosis_contents;
    boolean view_diagnosis_status = false;
    List<Diagnosis> freqDiagnosisListArraylist;
   // List<Diagnosis> PATIENT_DAIGNOSIS_ARRAY;
    private FrequentDiagnosisAdapters frequentDiagnosisAdapter;
    RecyclerView frequentDiagnosisRecyclerview;
    ListView diagnosis_listview;
    List<Diagnosis> diagnosisListArraylist;
    private DiagnosisAdapters diagnosisAdapter;
    RecyclerView diagnosisRecyclerview;

    // Treatments Section
    LinearLayout treatment_btn, treatment_contents;
    boolean view_treatment_status = false;
    List<Treatments> freqTreatmentListArraylist;
  //  List<Treatments> PATIENT_TREATMENT_ARRAY;
    private FrequentTreatmentAdapters frequenttreatmentAdapter;
    RecyclerView frequentTreatmentRecyclerview;
    List<Treatments> treatmentListArraylist;
    private TreatmentsAdapters treatmentAdapter;
    RecyclerView treatmentRecyclerview;

    // Prescriptions Section
    LinearLayout prescription_btn, prescription_contents;
    boolean view_prescription_status = false;
    LinearLayout load_templates;
    List<MyTemplates> templateListArraylist = new ArrayList<>();
    List<Integer> selectedTemplateID = new ArrayList<>();
    List<FrequentPrescription> freqPrescriptionsListArraylist;
    List<FrequentPrescription> repeatPrescriptionsListArraylist;
   // List<FrequentPrescription> PATIENT_PRESCRIPTION_ARRAY;
    private FrequentPrescriptionsAdapters frequentPrescriptionAdapter;
    RecyclerView frequentPrescriptionRecyclerview;
    List<FrequentPrescription> prescriptionListArraylist;
    private PrescriptionAdapters prescriptionAdapter;
    RecyclerView prescriptionRecyclerview;
    ListView prescription_listview;
    LinearLayout prescription_header;
    CustomEditText _edt_template_name, _edt_prescription_note;
    ImageView save_template;
    CustomTextViewBold repeat_prescription_btn;

    Spinner patient_education_spinner;
    ImageView patientedu_info_image;
    int PATIENT_EDUCATION = 0;
    List<PatientEducation> patientEducationListArraylist  = new ArrayList<>();

    WebView pdf_webview;
    NestedScrollView visit_relativelayout;
    List<OtherSettings> otherSettingsListArraylist = new ArrayList<>();
    CustomTextViewSemiBold consultion_fee_contents;
    CustomTextView consultfees_Set_btn;

    LinearLayout spectacle_prescription_btn, spectacle_prescription_contents;
    boolean view_spectacle_prescription_status = false;
    private CustomEditText _edt_right_dv_sphere, _edt_right_dv_cyl, _edt_right_dv_axis;
    private CustomEditText _edt_right_nv_sphere, _edt_right_nv_cyl, _edt_right_nv_axis;
    private CustomEditText _edt_right_ipd;

    private CustomEditText _edt_left_dv_sphere, _edt_left_dv_cyl, _edt_left_dv_axis;
    private CustomEditText _edt_left_nv_sphere, _edt_left_nv_cyl, _edt_left_nv_axis;
    private CustomEditText _edt_left_ipd;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_ophthal_visit_details);

        EPISODE_LIST = new ArrayList<>();
        PATIENT_CHIEF_MEDCOMPLAINT_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_GENERAL_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_OPHTHAL_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_RADIOLOGY_ARRAY  = new ArrayList<>();
        PATIENT_INVESTIGATION_CLINICAL_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_OTHER_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_ARRAY = new ArrayList<>();
        PATIENT_DAIGNOSIS_ARRAY = new ArrayList<>();
        PATIENT_TREATMENT_ARRAY = new ArrayList<>();
        PATIENT_PRESCRIPTION_ARRAY = new ArrayList<>();
        PATIENT_LIDS_ARRAY = new ArrayList<>();
        PATIENT_LIDS_INIT_ARRAY = new ArrayList<>();
        PATIENT_CONJUCTIVA_ARRAY = new ArrayList<>();
        PATIENT_CONJUCTIVA_INIT_ARRAY = new ArrayList<>();
        PATIENT_SCLERA_ARRAY = new ArrayList<>();
        PATIENT_SCLERA_INIT_ARRAY = new ArrayList<>();
        PATIENT_CORNEA_ANTERIOR_ARRAY = new ArrayList<>();
        PATIENT_CORNEA_ANTERIOR_INIT_ARRAY = new ArrayList<>();
        PATIENT_CORNEA_POSTERIOR_ARRAY = new ArrayList<>();
        PATIENT_CORNEA_POSTERIOR_INIT_ARRAY = new ArrayList<>();
        PATIENT_ANTERIOR_CHAMBER_ARRAY = new ArrayList<>();
        PATIENT_ANTERIOR_CHAMBER_INIT_ARRAY = new ArrayList<>();
        PATIENT_IRIS_ARRAY = new ArrayList<>();
        PATIENT_IRIS_INIT_ARRAY  = new ArrayList<>();
        PATIENT_PUPIL_ARRAY = new ArrayList<>();
        PATIENT_PUPIL_INIT_ARRAY = new ArrayList<>();
        PATIENT_ANGLE_ARRAY = new ArrayList<>();
        PATIENT_ANGLE_INIT_ARRAY = new ArrayList<>();
        PATIENT_LENS_ARRAY = new ArrayList<>();
        PATIENT_LENS_INIT_ARRAY = new ArrayList<>();
        PATIENT_VITEROUS_ARRAY = new ArrayList<>();
        PATIENT_VITEROUS_INIT_ARRAY  = new ArrayList<>();
        PATIENT_FUNDUS_ARRAY = new ArrayList<>();
        PATIENT_FUNDUS_INIT_ARRAY = new ArrayList<>();

        PATIENT_EDUCATION = 0;
        HOSPITAL_ID = 0;

        view_chief_medcomp_status = false;
        view_examinations_status = false;
        view_lids_status = false;
        view_conjuctiva_status = false;
        view_sclera_status = false;
        view_cornea_anterior_status = false;
        view_cornea_posterior_status = false;
        view_anterior_chamber_status = false;
        view_iris_status = false;
        view_pupil_status = false;
        view_angle_status = false;
        view_lens_status = false;
        view_viterous_status = false;
        view_fundus_status = false;
        view_refraction_status = false;
        view_investigation_status = false;
        view_diagnosis_status = false;
        view_treatment_status = false;
        view_prescription_status = false;
        view_spectacle_prescription_status = false;
        otherSettingsListArraylist = new ArrayList<>();

        chiefcomplaintListArraylist = new ArrayList<>();
        freqComplaintListArraylist = new ArrayList<>();
        PATIENT_CHIEF_MEDCOMPLAINT_ARRAY = new ArrayList<>();

        lidsListArraylist = new ArrayList<>();
        PATIENT_LIDS_ARRAY = new ArrayList<>();
        PATIENT_LIDS_INIT_ARRAY = new ArrayList<>();

        conjuctivaListArraylist = new ArrayList<>();
        PATIENT_CONJUCTIVA_ARRAY = new ArrayList<>();
        PATIENT_CONJUCTIVA_INIT_ARRAY = new ArrayList<>();

        scleraListArraylist = new ArrayList<>();
        PATIENT_SCLERA_ARRAY = new ArrayList<>();
        PATIENT_SCLERA_INIT_ARRAY = new ArrayList<>();

        corneaAnteriorListArraylist = new ArrayList<>();
        PATIENT_CORNEA_ANTERIOR_ARRAY = new ArrayList<>();
        PATIENT_CORNEA_ANTERIOR_INIT_ARRAY = new ArrayList<>();

        corneaPosteriorListArraylist = new ArrayList<>();
        PATIENT_CORNEA_POSTERIOR_ARRAY = new ArrayList<>();
        PATIENT_CORNEA_POSTERIOR_INIT_ARRAY = new ArrayList<>();

        anteriorChamberListArraylist = new ArrayList<>();
        PATIENT_ANTERIOR_CHAMBER_ARRAY = new ArrayList<>();
        PATIENT_ANTERIOR_CHAMBER_INIT_ARRAY = new ArrayList<>();

        irisListArraylist = new ArrayList<>();
        PATIENT_IRIS_ARRAY = new ArrayList<>();
        PATIENT_IRIS_INIT_ARRAY = new ArrayList<>();

        pupilListArraylist = new ArrayList<>();
        PATIENT_PUPIL_ARRAY = new ArrayList<>();
        PATIENT_PUPIL_INIT_ARRAY = new ArrayList<>();

        angleListArraylist = new ArrayList<>();
        PATIENT_ANGLE_ARRAY = new ArrayList<>();
        PATIENT_ANGLE_INIT_ARRAY = new ArrayList<>();

        lensListArraylist = new ArrayList<>();
        PATIENT_LENS_ARRAY = new ArrayList<>();
        PATIENT_LENS_INIT_ARRAY = new ArrayList<>();

        viterousListArraylist = new ArrayList<>();
        PATIENT_VITEROUS_ARRAY = new ArrayList<>();
        PATIENT_VITEROUS_INIT_ARRAY = new ArrayList<>();

        fundusListArraylist = new ArrayList<>();
        PATIENT_FUNDUS_ARRAY = new ArrayList<>();
        PATIENT_FUNDUS_INIT_ARRAY = new ArrayList<>();

        investigationsListArraylist = new ArrayList<>();
        freqInvestigationsListArraylist  = new ArrayList<>();
        grouptestListArraylist = new ArrayList<>();
        investigationTemplateListArraylist = new ArrayList<>();
        PATIENT_INVESTIGATION_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_GENERAL_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_OPHTHAL_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_RADIOLOGY_ARRAY  = new ArrayList<>();
        PATIENT_INVESTIGATION_CLINICAL_ARRAY = new ArrayList<>();
        PATIENT_INVESTIGATION_OTHER_ARRAY = new ArrayList<>();

        freqDiagnosisListArraylist = new ArrayList<>();
        PATIENT_DAIGNOSIS_ARRAY = new ArrayList<>();
        diagnosisListArraylist = new ArrayList<>();

        freqTreatmentListArraylist = new ArrayList<>();
        PATIENT_TREATMENT_ARRAY = new ArrayList<>();
        treatmentListArraylist  = new ArrayList<>();

        templateListArraylist = new ArrayList<>();
        freqPrescriptionsListArraylist = new ArrayList<>();
        repeatPrescriptionsListArraylist = new ArrayList<>();
        PATIENT_PRESCRIPTION_ARRAY = new ArrayList<>();
        prescriptionListArraylist = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);
            Log.d(Utils.TAG, " ************ EditOphthalVisitDetailsActivity *********** ");

            EPISODE_ID = bundle.getInt("EPISODE_ID");
            PATIENT_ID = bundle.getInt("PATIENT_ID");
            FOLLOWUP_DATE = bundle.getString("FOLLOWUP_DATE");
            DIAGNOSIS_DETAILS = bundle.getString("DIAGNOSIS_DETAILS");
            TREATMENT_DETAILS = bundle.getString("TREATMENT_DETAILS");
            PRESCRIPTION_NOTES = bundle.getString("PRESCRIPTION_NOTES");
            EPISODE_CREATED_DATE = bundle.getString("EPISODE_CREATED_DATE");
            CONSULTATION_FEES = bundle.getString("CONSULTATION_FEES");
            PATIENT_EDUCATION = bundle.getInt("PATIENT_EDUCATION");
            CHIEFCOMPALINT_SUFFERINGS = bundle.getString("PATIENT_CHIEFCOMPALINT_SUFFERINGS");

            PATIENT_CHIEF_MEDCOMPLAINT_ARRAY =  (List<ChiefMedicalComplaint>) bundle.getSerializable("CHIEF_MEDICAL_LIST");
            PATIENT_INVESTIGATION_GENERAL_ARRAY =  (List<Investigations>) bundle.getSerializable("INVESTIGATION_GENERAL_LIST");
            PATIENT_INVESTIGATION_OPHTHAL_ARRAY =  (List<Investigations>) bundle.getSerializable("INVESTIGATION_OPHTHAL_LIST");
            PATIENT_INVESTIGATION_RADIOLOGY_ARRAY  =  (List<Investigations>) bundle.getSerializable("INVESTIGATION_RADIOLOGY_LIST");
            PATIENT_INVESTIGATION_CLINICAL_ARRAY =  (List<Investigations>) bundle.getSerializable("INVESTIGATION_CLINICAL_LIST");
            PATIENT_INVESTIGATION_OTHER_ARRAY =  (List<Investigations>) bundle.getSerializable("INVESTIGATION_OTHERS_LIST");
            PATIENT_DAIGNOSIS_ARRAY =  (List<Diagnosis>) bundle.getSerializable("DIAGNOSIS_LIST");
            PATIENT_TREATMENT_ARRAY =  (List<Treatments>) bundle.getSerializable("TREATMENT_LIST");
            PATIENT_PRESCRIPTION_ARRAY =  (List<FrequentPrescription>) bundle.getSerializable("PRESCRIPTION_LIST");
            PATIENT_LIDS_INIT_ARRAY =  (List<Lids>) bundle.getSerializable("LIDS_LIST");
            PATIENT_CONJUCTIVA_INIT_ARRAY =  (List<OphthalConjuctiva>) bundle.getSerializable("CONJUCTIVA_LIST");
            PATIENT_SCLERA_INIT_ARRAY =  (List<OphthalSclera>) bundle.getSerializable("SCLERA_LIST");
            PATIENT_CORNEA_ANTERIOR_INIT_ARRAY =  (List<OphthalCornearAnteriorSurface>) bundle.getSerializable("CORNEA_ANTERIOR_LIST");
            PATIENT_CORNEA_POSTERIOR_INIT_ARRAY =  (List<OphthalCornearPosteriorSurface>) bundle.getSerializable("CORNEA_POSTERIOR_LIST");
            PATIENT_ANTERIOR_CHAMBER_INIT_ARRAY =  (List<OphthalAnteriorChamber>) bundle.getSerializable("ANTERIOR_CHAMBER_LIST");
            PATIENT_IRIS_INIT_ARRAY =  (List<OphthalIris>) bundle.getSerializable("IRIS_LIST");
            PATIENT_PUPIL_INIT_ARRAY =  (List<OphthalPupil>) bundle.getSerializable("PUPIL_LIST");
            PATIENT_ANGLE_INIT_ARRAY =  (List<OphthalAngleAnteriorChamber>) bundle.getSerializable("ANGLE_LIST");
            PATIENT_LENS_INIT_ARRAY =  (List<OphthalLens>) bundle.getSerializable("LENS_LIST");
            PATIENT_VITEROUS_INIT_ARRAY =  (List<OphthalViterous>) bundle.getSerializable("VITERIOUS_LIST");
            PATIENT_FUNDUS_INIT_ARRAY =  (List<OphthalFundus>) bundle.getSerializable("FUNDUS_LIST");

            REFRACTION_RE_VALUE1 = bundle.getString("REFRACTION_RE_VALUE1");
            REFRACTION_RE_VALUE2 = bundle.getString("REFRACTION_RE_VALUE2");
            REFRACTION_LE_VALUE1 = bundle.getString("REFRACTION_LE_VALUE1");
            REFRACTION_LE_VALUE2 = bundle.getString("REFRACTION_LE_VALUE2");
            PATIENT_DISTANCE_VISION_RE = bundle.getString("DISTANCE_VISION_RE");
            PATIENT_DISTANCE_VISION_LE = bundle.getString("DISTANCE_VISION_LE");
            PATIENT_NEAR_VISION_RE = bundle.getString("NEAR_VISION_RE");
            PATIENT_NEAR_VISION_LE = bundle.getString("NEAR_VISION_LE");
            GET_DV_SPHERE_RIGHT = bundle.getString("DV_SPHERE_RIGHT");
            GET_DV_CYCLE_RIGHT = bundle.getString("DV_CYCLE_RIGHT");
            GET_DV_AXIS_RIGHT = bundle.getString("DV_AXIS_RIGHT");
            GET_NV_SPHERE_RIGHT = bundle.getString("NV_SPHERE_RIGHT");
            GET_NV_CYCLE_RIGHT = bundle.getString("NV_CYCLE_RIGHT");
            GET_NV_AXIS_RIGHT = bundle.getString("DV_AXIS_RIGHT");
            GET_IPD_RIGHT = bundle.getString("IPD_RIGHT");
            GET_DV_SPHERE_LEFT = bundle.getString("DV_SPHERE_LEFT");
            GET_DV_CYCLE_LEFT = bundle.getString("DV_CYCLE_LEFT");
            GET_DV_AXIS_LEFT = bundle.getString("DV_AXIS_LEFT");
            GET_NV_SPHERE_LEFT = bundle.getString("NV_SPHERE_LEFT");
            GET_NV_CYCLE_LEFT = bundle.getString("NV_CYCLE_LEFT");
            GET_NV_AXIS_LEFT = bundle.getString("NV_AXIS_LEFT");
            GET_IPD_LEFT = bundle.getString("IPD_LEFT");

            PATIENT_DISTANCE_VISION_UNAIDED_RE = bundle.getString("DISTANCE_VISION_UNAIDED_RE");
            PATIENT_DISTANCE_VISION_UNAIDED_LE = bundle.getString("DISTANCE_VISION_UNAIDED_LE");
            PATIENT_NEAR_VISION_UNAIDED_RE = bundle.getString("NEAR_VISION_UNAIDED_RE");
            PATIENT_NEAR_VISION_UNAIDED_LE = bundle.getString("NEAR_VISION_UNAIDED_LE");
            PATIENT_IOP_RE = bundle.getString("IOP_RE");
            PATIENT_IOP_LE = bundle.getString("IOP_LE");


            Log.d(Utils.TAG, " EPISODE_ID: "+EPISODE_ID);

            if (PATIENT_CHIEF_MEDCOMPLAINT_ARRAY != null && PATIENT_CHIEF_MEDCOMPLAINT_ARRAY.size() > 0) {
                Log.d(Utils.TAG + " MEDCOMPL:", String.valueOf(PATIENT_CHIEF_MEDCOMPLAINT_ARRAY.size()));
            }

            if (PATIENT_INVESTIGATION_GENERAL_ARRAY != null && PATIENT_INVESTIGATION_GENERAL_ARRAY.size() > 0) {
                Log.d(Utils.TAG + " INVEST_GEN:", String.valueOf(PATIENT_INVESTIGATION_GENERAL_ARRAY.size()));
            }

            if (PATIENT_INVESTIGATION_OPHTHAL_ARRAY != null && PATIENT_INVESTIGATION_OPHTHAL_ARRAY.size() > 0) {
                Log.d(Utils.TAG + " INVEST_OPHT:", String.valueOf(PATIENT_INVESTIGATION_OPHTHAL_ARRAY.size()));
            }

            if (PATIENT_INVESTIGATION_RADIOLOGY_ARRAY != null && PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.size() > 0) {
                Log.d(Utils.TAG + " INVEST_RAD:", String.valueOf(PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.size()));
            }

            if (PATIENT_INVESTIGATION_CLINICAL_ARRAY != null && PATIENT_INVESTIGATION_CLINICAL_ARRAY.size() > 0) {
                Log.d(Utils.TAG + " INVEST_CLIN:", String.valueOf(PATIENT_INVESTIGATION_CLINICAL_ARRAY.size()));
            }

            if (PATIENT_INVESTIGATION_OTHER_ARRAY != null && PATIENT_INVESTIGATION_OTHER_ARRAY.size() > 0) {
                Log.d(Utils.TAG + " INVEST_OTH:", String.valueOf(PATIENT_INVESTIGATION_OTHER_ARRAY.size()));
            }

            if (PATIENT_DAIGNOSIS_ARRAY != null && PATIENT_DAIGNOSIS_ARRAY.size() > 0) {
                Log.d(Utils.TAG + " DAIGN:", String.valueOf(PATIENT_DAIGNOSIS_ARRAY.size()));
            }

            if (PATIENT_TREATMENT_ARRAY != null && PATIENT_TREATMENT_ARRAY.size() > 0) {
                Log.d(Utils.TAG + " TREAT:", String.valueOf(PATIENT_TREATMENT_ARRAY.size()));
            }

            if (PATIENT_PRESCRIPTION_ARRAY != null && PATIENT_PRESCRIPTION_ARRAY.size() > 0) {
                Log.d(Utils.TAG + " PRESC:", String.valueOf(PATIENT_PRESCRIPTION_ARRAY.size()));
            }

        }

        shareadPreferenceClass = new ShareadPreferenceClass(EditOphthalVisitDetailsActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(EditOphthalVisitDetailsActivity.this);

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            DIAGNOSTIC_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_DIAGNOSTIC_CENTRES,"");
            PHARMA_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_PHARMA_CENTRES,"");
            TEMPLATES_LIST = sharedPreferences.getString(HCConstants.PREF_MYPATIENT_TEMPLATES, "");
            OTHER_SETTINGS_LIST = sharedPreferences.getString(HCConstants.PREF_OTHER_SETTINGS,"");
            CONSULTATION_CHARGES  = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_CONS_CHARGE,"");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            DIAGNOSTIC_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_DIAGNOSTIC_CENTRES,"");
            PHARMA_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_PHARMA_CENTRES,"");
            TEMPLATES_LIST = sharedPreferences.getString(HCConstants.PREF_MYPATIENT_TEMPLATES, "");
            OTHER_SETTINGS_LIST = sharedPreferences.getString(HCConstants.PREF_OTHER_SETTINGS,"");
            CONSULTATION_CHARGES  = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_CONS_CHARGE,"");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            DIAGNOSTIC_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_DIAGNOSTIC_CENTRES,"");
            PHARMA_CENTRE_LIST = sharedPreferences.getString(HCConstants.PREF_PHARMA_CENTRES,"");
            TEMPLATES_LIST = sharedPreferences.getString(HCConstants.PREF_MYPATIENT_TEMPLATES, "");
            OTHER_SETTINGS_LIST = sharedPreferences.getString(HCConstants.PREF_OTHER_SETTINGS,"");
            CONSULTATION_CHARGES  = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_CONS_CHARGE,"");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
        }
        Log.d(Utils.TAG, " USER_ID: "+USER_ID);
        Log.d(Utils.TAG, " USER_NAME: "+USER_NAME);
        Log.d(Utils.TAG, " LOGIN_TYPE: "+USER_LOGIN_TYPE);

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

        chief_medcomp_btn = (LinearLayout) findViewById(R.id.addvisit_ophthal_chief_medcomp_btn);
        chief_medcomp_btn.setOnClickListener(this);
        chief_medcomp_contents = (LinearLayout) findViewById(R.id.addvisit_ophthal_chief_medcomp_contents);
        chief_medcomp_contents.setVisibility(View.VISIBLE);
        view_chief_medcomp_status = true;
        submit_btn = (Button) findViewById(R.id.add_ophthal_visit_submit);
        submit_btn.setOnClickListener(this);
        submit_btn.setText("Update Visit");

        // Start Chief Medical Complaints - Ophthal
        LinearLayout add_chiefMedComplaint = (LinearLayout) findViewById(R.id.ophthal_add_chief_medcomplaint);
        chiefComplaintRecyclerview = (RecyclerView) findViewById(R.id.add_vistit_ophthal_chiefcomp_recycler);
        medcomplaintAdapter = new ChiefMedicalAdapters(this, PATIENT_CHIEF_MEDCOMPLAINT_ARRAY);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        chiefComplaintRecyclerview.setLayoutManager(mLayoutManager1);
        chiefComplaintRecyclerview.setItemAnimator(new DefaultItemAnimator());
        chiefComplaintRecyclerview.setAdapter(medcomplaintAdapter);

        chiefMedComplaint_sufferings = (CustomEditText) findViewById(R.id.add_visit_chiefcomp_sufferings);

        if(CHIEFCOMPALINT_SUFFERINGS.equalsIgnoreCase("NULL") || CHIEFCOMPALINT_SUFFERINGS.equals("")) {} else {
            chiefMedComplaint_sufferings.setText(CHIEFCOMPALINT_SUFFERINGS);
         }

        chiefcomplaintListArraylist = MedisensePracticeDB.getAllChiefMedicalComplaint(this, USER_LOGIN_TYPE, USER_ID);
        Log.d(Utils.TAG, " Med count: " + chiefcomplaintListArraylist.size());

        add_chiefMedComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareChiefMedicalCompalint(chiefcomplaintListArraylist);
            }
        });

        frequentComplaintRecyclerview = (RecyclerView) findViewById(R.id.add_visit_ophthal_freqcomplaint_recyclerview);
        frequentComplaintRecyclerview.setLayoutManager(new GridLayoutManager(this, numberOfFrequentColumns));
        frequentcomplaintAdapter = new FrequentComplaintAdapters(this, freqComplaintListArraylist);

        frequentcomplaintAdapter.setOnItemClickListener(new FrequentComplaintAdapters.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int adapterPosition, int freqSymptomsID, String freqSymptomsName, int freqDocID, int freqDocType) {
                // Log.d(Utils.TAG, "onItemClick ");
                Toast.makeText(EditOphthalVisitDetailsActivity.this,freqSymptomsName+ " is added to the list", Toast.LENGTH_SHORT).show();
                PATIENT_CHIEF_MEDCOMPLAINT_ARRAY.add(new ChiefMedicalComplaint(freqSymptomsID,freqSymptomsName,
                        freqDocID,freqDocType,USER_ID, USER_LOGIN_TYPE));
                refreshChiefMedicalComplaint();
            }
        });
        frequentComplaintRecyclerview.setAdapter(frequentcomplaintAdapter);

        freqComplaintListArraylist = MedisensePracticeDB.getAllFrequentMedicalComplaint(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        if(freqComplaintListArraylist.size() > 0) {
            Log.d(Utils.TAG, " freqMed count: " + freqComplaintListArraylist.size());
            frequentcomplaintAdapter = new FrequentComplaintAdapters(EditOphthalVisitDetailsActivity.this, freqComplaintListArraylist);
            frequentComplaintRecyclerview.setAdapter(frequentcomplaintAdapter);
            frequentcomplaintAdapter.notifyDataSetChanged();
        }


        /******************************* EXAMINATION SECTION ***************************/
        distance_vision_re_spinner = (Spinner) findViewById(R.id.ophthal_distance_vision_re_spinner);
        distance_vision_le_spinner = (Spinner) findViewById(R.id.ophthal_distance_vision_le_spinner);
        near_vision_re_spinner = (Spinner) findViewById(R.id.ophthal_near_vision_re_spinner);
        near_vision_le_spinner = (Spinner) findViewById(R.id.ophthal_near_vision_le_spinner);

        distance_vision_unaided_re_spinner = (Spinner) findViewById(R.id.ophthal_distance_vision_re_unaided_spinner);
        distance_vision_unaided_le_spinner = (Spinner) findViewById(R.id.ophthal_distance_vision_le_unaided_spinner);
        near_vision_unaided_re_spinner = (Spinner) findViewById(R.id.ophthal_near_vision_unaided_re_spinner);
        near_vision_unaided_le_spinner = (Spinner) findViewById(R.id.ophthal_near_vision_unaided_le_spinner);
        iop_re_spinner = (Spinner) findViewById(R.id.ophthal_iop_re_spinner);
        iop_le_spinner = (Spinner)findViewById(R.id.ophthal_iop_le_spinner);

        ArrayAdapter<String> distance_vision_RE_Array = new ArrayAdapter<String>(EditOphthalVisitDetailsActivity.this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.distance_vision_results) );
        distance_vision_RE_Array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distance_vision_re_spinner.setAdapter(distance_vision_RE_Array);

        if(PATIENT_DISTANCE_VISION_RE.equals("")) {
            PATIENT_DISTANCE_VISION_RE = "--Select--";
        }
        distance_vision_re_spinner.setSelection(getIndex(distance_vision_re_spinner, PATIENT_DISTANCE_VISION_RE));
        distance_vision_re_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(mContext, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                if(adapterView.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    PATIENT_DISTANCE_VISION_RE = "";
                }
                else {
                    PATIENT_DISTANCE_VISION_RE = adapterView.getSelectedItem().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if(PATIENT_DISTANCE_VISION_RE.equals("") || PATIENT_DISTANCE_VISION_RE != null) {
            int spinnerPosition = distance_vision_RE_Array.getPosition(PATIENT_DISTANCE_VISION_RE);
            distance_vision_re_spinner.setSelection(spinnerPosition);
        }

        ArrayAdapter<String> distance_vision_unaided_RE_Array = new ArrayAdapter<String>(EditOphthalVisitDetailsActivity.this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.distance_vision_results) );
        distance_vision_unaided_RE_Array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distance_vision_unaided_re_spinner.setAdapter(distance_vision_unaided_RE_Array);

        if(PATIENT_DISTANCE_VISION_UNAIDED_RE.equals("")) {
            PATIENT_DISTANCE_VISION_UNAIDED_RE = "--Select--";
        }
        distance_vision_unaided_re_spinner.setSelection(getIndex(distance_vision_unaided_re_spinner, PATIENT_DISTANCE_VISION_UNAIDED_RE));
        distance_vision_unaided_re_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(mContext, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                if(adapterView.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    PATIENT_DISTANCE_VISION_UNAIDED_RE = "";
                }
                else {
                    PATIENT_DISTANCE_VISION_UNAIDED_RE = adapterView.getSelectedItem().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if(PATIENT_DISTANCE_VISION_UNAIDED_RE.equals("") || PATIENT_DISTANCE_VISION_UNAIDED_RE != null) {
            int spinnerPosition = distance_vision_unaided_RE_Array.getPosition(PATIENT_DISTANCE_VISION_UNAIDED_RE);
            distance_vision_unaided_re_spinner.setSelection(spinnerPosition);
        }



        ArrayAdapter<String> distance_vision_LE_Array = new ArrayAdapter<String>(EditOphthalVisitDetailsActivity.this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.distance_vision_results) );
        distance_vision_LE_Array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distance_vision_le_spinner.setAdapter(distance_vision_LE_Array);

        if(PATIENT_DISTANCE_VISION_LE.equals("")) {
            PATIENT_DISTANCE_VISION_LE = "--Select--";
        }
        distance_vision_le_spinner.setSelection(getIndex(distance_vision_le_spinner, PATIENT_DISTANCE_VISION_LE));
        distance_vision_le_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(mContext, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                if(adapterView.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    PATIENT_DISTANCE_VISION_LE = "";
                }
                else {
                    PATIENT_DISTANCE_VISION_LE = adapterView.getSelectedItem().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if(PATIENT_DISTANCE_VISION_LE.equals("") || PATIENT_DISTANCE_VISION_LE != null) {
            int spinnerPosition = distance_vision_LE_Array.getPosition(PATIENT_DISTANCE_VISION_LE);
            distance_vision_le_spinner.setSelection(spinnerPosition);
        }

        ArrayAdapter<String> distance_vision_unaided_LE_Array = new ArrayAdapter<String>(EditOphthalVisitDetailsActivity.this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.distance_vision_results) );
        distance_vision_unaided_LE_Array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distance_vision_unaided_le_spinner.setAdapter(distance_vision_unaided_LE_Array);

        if(PATIENT_DISTANCE_VISION_UNAIDED_LE.equals("")) {
            PATIENT_DISTANCE_VISION_UNAIDED_LE = "--Select--";
        }
        distance_vision_unaided_le_spinner.setSelection(getIndex(distance_vision_unaided_le_spinner, PATIENT_DISTANCE_VISION_UNAIDED_LE));
        distance_vision_unaided_le_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(mContext, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                if(adapterView.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    PATIENT_DISTANCE_VISION_UNAIDED_LE = "";
                }
                else {
                    PATIENT_DISTANCE_VISION_UNAIDED_LE = adapterView.getSelectedItem().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if(PATIENT_DISTANCE_VISION_UNAIDED_LE.equals("") || PATIENT_DISTANCE_VISION_UNAIDED_LE != null) {
            int spinnerPosition = distance_vision_unaided_LE_Array.getPosition(PATIENT_DISTANCE_VISION_UNAIDED_LE);
            distance_vision_unaided_le_spinner.setSelection(spinnerPosition);
        }

        ArrayAdapter<String> near_vision_RE_Array = new ArrayAdapter<String>(EditOphthalVisitDetailsActivity.this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.near_vision_results) );
        near_vision_RE_Array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        near_vision_re_spinner.setAdapter(near_vision_RE_Array);

        if(PATIENT_NEAR_VISION_RE.equals("")) {
            PATIENT_NEAR_VISION_RE = "--Select--";
        }
        near_vision_re_spinner.setSelection(getIndex(near_vision_re_spinner, PATIENT_NEAR_VISION_RE));
        near_vision_re_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(mContext, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                if(adapterView.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    PATIENT_NEAR_VISION_RE = "";
                }
                else {
                    PATIENT_NEAR_VISION_RE = adapterView.getSelectedItem().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if(PATIENT_NEAR_VISION_RE.equals("") || PATIENT_NEAR_VISION_RE != null) {
            int spinnerPosition = near_vision_RE_Array.getPosition(PATIENT_NEAR_VISION_RE);
            near_vision_re_spinner.setSelection(spinnerPosition);
        }

        ArrayAdapter<String> near_vision_unaided_RE_Array = new ArrayAdapter<String>(EditOphthalVisitDetailsActivity.this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.near_vision_results) );
        near_vision_unaided_RE_Array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        near_vision_unaided_re_spinner.setAdapter(near_vision_unaided_RE_Array);

        if(PATIENT_NEAR_VISION_UNAIDED_RE.equals("")) {
            PATIENT_NEAR_VISION_UNAIDED_RE = "--Select--";
        }
        near_vision_unaided_re_spinner.setSelection(getIndex(near_vision_unaided_re_spinner, PATIENT_NEAR_VISION_UNAIDED_RE));
        near_vision_unaided_re_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(mContext, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                if(adapterView.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    PATIENT_NEAR_VISION_UNAIDED_RE = "";
                }
                else {
                    PATIENT_NEAR_VISION_UNAIDED_RE = adapterView.getSelectedItem().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if(PATIENT_NEAR_VISION_UNAIDED_RE.equals("") || PATIENT_NEAR_VISION_UNAIDED_RE != null) {
            int spinnerPosition = near_vision_unaided_RE_Array.getPosition(PATIENT_NEAR_VISION_UNAIDED_RE);
            near_vision_unaided_re_spinner.setSelection(spinnerPosition);
        }

        ArrayAdapter<String> near_vision_LE_Array = new ArrayAdapter<String>(EditOphthalVisitDetailsActivity.this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.near_vision_results) );
        near_vision_LE_Array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        near_vision_le_spinner.setAdapter(near_vision_LE_Array);

        if(PATIENT_NEAR_VISION_LE.equals("")) {
            PATIENT_NEAR_VISION_LE = "--Select--";
        }
        near_vision_le_spinner.setSelection(getIndex(near_vision_le_spinner, PATIENT_NEAR_VISION_LE));
        near_vision_le_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(mContext, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                if(adapterView.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    PATIENT_NEAR_VISION_LE = "";
                }
                else {
                    PATIENT_NEAR_VISION_LE = adapterView.getSelectedItem().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if(PATIENT_NEAR_VISION_LE.equals("") || PATIENT_NEAR_VISION_LE != null) {
            int spinnerPosition = near_vision_LE_Array.getPosition(PATIENT_NEAR_VISION_LE);
            near_vision_le_spinner.setSelection(spinnerPosition);
        }

        ArrayAdapter<String> near_vision_unaided_LE_Array = new ArrayAdapter<String>(EditOphthalVisitDetailsActivity.this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.near_vision_results) );
        near_vision_unaided_LE_Array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        near_vision_unaided_le_spinner.setAdapter(near_vision_unaided_LE_Array);

        if(PATIENT_NEAR_VISION_UNAIDED_LE.equals("")) {
            PATIENT_NEAR_VISION_UNAIDED_LE = "--Select--";
        }
        near_vision_unaided_le_spinner.setSelection(getIndex(near_vision_unaided_le_spinner, PATIENT_NEAR_VISION_UNAIDED_LE));
        near_vision_unaided_le_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(mContext, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                if(adapterView.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    PATIENT_NEAR_VISION_UNAIDED_LE = "";
                }
                else {
                    PATIENT_NEAR_VISION_UNAIDED_LE = adapterView.getSelectedItem().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if(PATIENT_NEAR_VISION_UNAIDED_LE.equals("") || PATIENT_NEAR_VISION_UNAIDED_LE != null) {
            int spinnerPosition = near_vision_unaided_LE_Array.getPosition(PATIENT_NEAR_VISION_UNAIDED_LE);
            near_vision_unaided_le_spinner.setSelection(spinnerPosition);
        }

        ArrayAdapter<String> iop_RE_Array = new ArrayAdapter<String>(EditOphthalVisitDetailsActivity.this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.IOP_results) );
        iop_RE_Array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        iop_re_spinner.setAdapter(iop_RE_Array);

        if(PATIENT_IOP_RE.equals("")) {
            PATIENT_IOP_RE = "--Select--";
        }
        iop_re_spinner.setSelection(getIndex(iop_re_spinner, PATIENT_IOP_RE));
        iop_re_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(mContext, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                if(adapterView.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    PATIENT_IOP_RE = "";
                }
                else {
                    PATIENT_IOP_RE = adapterView.getSelectedItem().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if(PATIENT_IOP_RE.equals("") || PATIENT_IOP_RE != null) {
            int spinnerPosition = iop_RE_Array.getPosition(PATIENT_IOP_RE);
            iop_re_spinner.setSelection(spinnerPosition);
        }

        ArrayAdapter<String> iop_LE_Array = new ArrayAdapter<String>(EditOphthalVisitDetailsActivity.this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.IOP_results) );
        iop_LE_Array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        iop_le_spinner.setAdapter(iop_LE_Array);

        if(PATIENT_IOP_LE.equals("")) {
            PATIENT_IOP_LE = "--Select--";
        }
        iop_le_spinner.setSelection(getIndex(iop_le_spinner, PATIENT_IOP_LE));
        iop_le_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(mContext, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                if(adapterView.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    PATIENT_IOP_LE = "";
                }
                else {
                    PATIENT_IOP_LE = adapterView.getSelectedItem().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if(PATIENT_IOP_LE.equals("") || PATIENT_IOP_LE != null) {
            int spinnerPosition = iop_LE_Array.getPosition(PATIENT_IOP_LE);
            iop_le_spinner.setSelection(spinnerPosition);
        }


        examinations_btn = (LinearLayout) findViewById(R.id.addvisit_ophthal_examinations_btn);
        examinations_btn.setOnClickListener(this);
        examinations_contents = (LinearLayout) findViewById(R.id.addvisit_ophthal_examinations_contents);
        lids_gridview_RE = (LinearLayout) findViewById(R.id.lids_RE_grids);
        lids_gridview_LE = (LinearLayout) findViewById(R.id.lids_LE_grids);
        lids_btn = (LinearLayout) findViewById(R.id.ophthal_lids_btn);
        lids_btn.setOnClickListener(this);
        lids_contents = (LinearLayout) findViewById(R.id.ophthal_lids_contents);
        lids_contents.setVisibility(View.GONE);
        add_lids  = (ImageView) findViewById(R.id.lids_add_submit);
        add_lids.setOnClickListener(this);
        ophthal_lids_image = (ImageView) findViewById(R.id.ophthal_lids_image);
        _edt_add_lids = (AutoCompleteTextView) findViewById(R.id.lids_add_edit);

        lidsListArraylist = MedisensePracticeDB.getAllLids(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        prepareLidsCheckBoxes(lidsListArraylist);

        // ***************** CONJUCTIVA SECTION ************************* /
        conjuctiva_btn = (LinearLayout) findViewById(R.id.ophthal_conjuctiva_btn);
        conjuctiva_btn.setOnClickListener(this);
        conjuctiva_contents = (LinearLayout) findViewById(R.id.ophthal_conjuctiva_contents);
        conjuctiva_contents.setVisibility(View.GONE);
        add_conjuctiva = (ImageView) findViewById(R.id.conjuctiva_add_submit);
        add_conjuctiva.setOnClickListener(this);
        ophthal_conjuctiva_image = (ImageView)  findViewById(R.id.ophthal_conjuctiva_image);
        _edt_add_conjuctiva = (AutoCompleteTextView) findViewById(R.id.conjuctiva_add_edit);
        conjuctiva_gridview_RE = (LinearLayout) findViewById(R.id.conjuctiva_RE_grids);
        conjuctiva_gridview_LE = (LinearLayout) findViewById(R.id.conjuctiva_LE_grids);

        conjuctivaListArraylist = MedisensePracticeDB.getAllConjuctiva(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        prepareConjuctivaCheckBoxes(conjuctivaListArraylist);

        /* *************** SCLERA SECTION *************************** */
        sclera_btn = (LinearLayout) findViewById(R.id.ophthal_sclera_btn);
        sclera_btn.setOnClickListener(this);
        sclera_contents  = (LinearLayout) findViewById(R.id.ophthal_sclera_contents);
        sclera_contents.setVisibility(View.GONE);
        add_sclera = (ImageView) findViewById(R.id.sclera_add_submit);
        add_sclera.setOnClickListener(this);
        ophthal_sclera_image = (ImageView)  findViewById(R.id.ophthal_sclera_image);
        _edt_add_sclera = (AutoCompleteTextView) findViewById(R.id.sclera_add_edit);
        sclera_gridview_RE = (LinearLayout) findViewById(R.id.sclera_RE_grids);
        sclera_gridview_LE = (LinearLayout) findViewById(R.id.sclera_LE_grids);

        scleraListArraylist = MedisensePracticeDB.getAllSclera(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        prepareScleraCheckBoxes(scleraListArraylist);

        /* ******************* CORNEA ANTERIOR SURFACE SECTION **************************** */
        cornea_anterior_btn = (LinearLayout) findViewById(R.id.ophthal_cornea_anterior_btn);
        cornea_anterior_btn.setOnClickListener(this);
        cornea_anterior_contents  = (LinearLayout) findViewById(R.id.ophthal_cornea_anterior_contents);
        cornea_anterior_contents.setVisibility(View.GONE);
        add_cornea_anterior = (ImageView) findViewById(R.id.cornea_anterior_add_submit);
        add_cornea_anterior.setOnClickListener(this);
        ophthal_cornea_anterior_image = (ImageView) findViewById(R.id.ophthal_cornea_anterior_image);
        _edt_add_cornea_anterior = (AutoCompleteTextView) findViewById(R.id.cornea_anterior_add_edit);
        cornea_anterior_gridview_RE = (LinearLayout) findViewById(R.id.cornea_anterior_RE_grids);
        cornea_anterior_gridview_LE = (LinearLayout) findViewById(R.id.cornea_anterior_LE_grids);

        corneaAnteriorListArraylist = MedisensePracticeDB.getAllCorneaAnterior(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        prepareCorneaAnteriorCheckBoxes(corneaAnteriorListArraylist);

        /* ******************* CORNEA POSTERIOR SURFACE SECTION **************************** */
        cornea_posterior_btn = (LinearLayout) findViewById(R.id.ophthal_cornea_posterior_btn);
        cornea_posterior_btn.setOnClickListener(this);
        cornea_posterior_contents  = (LinearLayout) findViewById(R.id.ophthal_cornea_posterior_contents);
        cornea_posterior_contents.setVisibility(View.GONE);
        add_cornea_posterior = (ImageView) findViewById(R.id.cornea_posterior_add_submit);
        add_cornea_posterior.setOnClickListener(this);
        ophthal_cornea_posterior_image = (ImageView) findViewById(R.id.ophthal_cornea_posterior_image);
        _edt_add_cornea_posterior = (AutoCompleteTextView) findViewById(R.id.cornea_posterior_add_edit);
        cornea_posterior_gridview_RE = (LinearLayout) findViewById(R.id.cornea_posterior_RE_grids);
        cornea_posterior_gridview_LE = (LinearLayout) findViewById(R.id.cornea_posterior_LE_grids);

        corneaPosteriorListArraylist = MedisensePracticeDB.getAllCorneaPosterior(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        prepareCorneaPosteriorCheckBoxes(corneaPosteriorListArraylist);

        /* *************************** ANTERIOR CHAMBER******************************** */
        anterior_chamber_btn = (LinearLayout) findViewById(R.id.ophthal_anterior_chamber_btn);
        anterior_chamber_btn.setOnClickListener(this);
        anterior_chamber_contents  = (LinearLayout) findViewById(R.id.ophthal_anterior_chamber_contents);
        anterior_chamber_contents.setVisibility(View.GONE);
        add_anterior_chamber = (ImageView) findViewById(R.id.anterior_chamber_add_submit);
        add_anterior_chamber.setOnClickListener(this);
        ophthal_anterior_chamber_image = (ImageView) findViewById(R.id.ophthal_anterior_chamber_image);
        _edt_add_anterior_chamber = (AutoCompleteTextView) findViewById(R.id.anterior_chamber_add_edit);
        anterior_chamber_gridview_RE = (LinearLayout) findViewById(R.id.anterior_chamber_RE_grids);
        anterior_chamber_gridview_LE = (LinearLayout) findViewById(R.id.anterior_chamber_LE_grids);

        anteriorChamberListArraylist = MedisensePracticeDB.getAllAnteriorChamber(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        prepareAnteriorChamberCheckBoxes(anteriorChamberListArraylist);

        /* *************************** IRIS SECTION ****************************** */
        iris_btn = (LinearLayout) findViewById(R.id.ophthal_iris_btn);
        iris_btn.setOnClickListener(this);
        iris_contents  = (LinearLayout) findViewById(R.id.ophthal_iris_contents);
        iris_contents.setVisibility(View.GONE);
        add_iris = (ImageView) findViewById(R.id.iris_add_submit);
        add_iris.setOnClickListener(this);
        ophthal_iris_image = (ImageView) findViewById(R.id.ophthal_iris_image);
        _edt_add_iris = (AutoCompleteTextView) findViewById(R.id.iris_add_edit);
        iris_gridview_RE = (LinearLayout) findViewById(R.id.iris_RE_grids);
        iris_gridview_LE = (LinearLayout) findViewById(R.id.iris_LE_grids);

        irisListArraylist = MedisensePracticeDB.getAllIris(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        prepareIrisCheckBoxes(irisListArraylist);

        /* *************************** PUPIL SECTION ************************** */
        pupil_btn = (LinearLayout) findViewById(R.id.ophthal_pupil_btn);
        pupil_btn.setOnClickListener(this);
        pupil_contents  = (LinearLayout) findViewById(R.id.ophthal_pupil_contents);
        pupil_contents.setVisibility(View.GONE);
        add_pupil = (ImageView) findViewById(R.id.pupil_add_submit);
        add_pupil.setOnClickListener(this);
        ophthal_pupil_image = (ImageView) findViewById(R.id.ophthal_pupil_image);
        _edt_add_pupil = (AutoCompleteTextView) findViewById(R.id.pupil_add_edit);
        pupil_gridview_RE = (LinearLayout) findViewById(R.id.pupil_RE_grids);
        pupil_gridview_LE = (LinearLayout) findViewById(R.id.pupil_LE_grids);

        pupilListArraylist = MedisensePracticeDB.getAllPupil(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        preparePupilCheckBoxes(pupilListArraylist);

        /* ********************* ANGLE OF ANTERIOR CHAMBER SECTION ***************************** */
        angle_btn = (LinearLayout) findViewById(R.id.ophthal_angle_btn);
        angle_btn.setOnClickListener(this);
        angle_contents  = (LinearLayout) findViewById(R.id.ophthal_angle_contents);
        angle_contents.setVisibility(View.GONE);
        add_angle = (ImageView) findViewById(R.id.angle_add_submit);
        add_angle.setOnClickListener(this);
        ophthal_angle_image = (ImageView) findViewById(R.id.ophthal_angle_image);
        _edt_add_angle = (AutoCompleteTextView) findViewById(R.id.angle_add_edit);
        angle_gridview_RE = (LinearLayout) findViewById(R.id.angle_RE_grids);
        angle_gridview_LE = (LinearLayout) findViewById(R.id.angle_LE_grids);

        angleListArraylist = MedisensePracticeDB.getAllAngle(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        prepareAngleCheckBoxes(angleListArraylist);

        /* ********************* LENS SECTION ***************************** */
        lens_btn = (LinearLayout) findViewById(R.id.ophthal_lens_btn);
        lens_btn.setOnClickListener(this);
        lens_contents  = (LinearLayout) findViewById(R.id.ophthal_lens_contents);
        lens_contents.setVisibility(View.GONE);
        add_lens = (ImageView) findViewById(R.id.lens_add_submit);
        add_lens.setOnClickListener(this);
        ophthal_lens_image = (ImageView) findViewById(R.id.ophthal_lens_image);
        _edt_add_lens = (AutoCompleteTextView) findViewById(R.id.lens_add_edit);
        lens_gridview_RE = (LinearLayout) findViewById(R.id.lens_RE_grids);
        lens_gridview_LE = (LinearLayout) findViewById(R.id.lens_LE_grids);

        lensListArraylist = MedisensePracticeDB.getAllLens(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        prepareLensCheckBoxes(lensListArraylist);

        /* *********************** VITEROUS SECTION *************************** */
        viterous_btn = (LinearLayout) findViewById(R.id.ophthal_viterous_btn);
        viterous_btn.setOnClickListener(this);
        viterous_contents  = (LinearLayout) findViewById(R.id.ophthal_viterous_contents);
        viterous_contents.setVisibility(View.GONE);
        add_viterous = (ImageView) findViewById(R.id.viterous_add_submit);
        add_viterous.setOnClickListener(this);
        ophthal_viterous_image = (ImageView) findViewById(R.id.ophthal_viterous_image);
        _edt_add_viterous = (AutoCompleteTextView) findViewById(R.id.viterous_add_edit);
        viterous_gridview_RE = (LinearLayout) findViewById(R.id.viterous_RE_grids);
        viterous_gridview_LE = (LinearLayout) findViewById(R.id.viterous_LE_grids);

        viterousListArraylist = MedisensePracticeDB.getAllViterous(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        prepareViterousCheckBoxes(viterousListArraylist);

        /* ************************* FUNDUS SECTION ************************* */
        fundus_btn = (LinearLayout) findViewById(R.id.ophthal_fundus_btn);
        fundus_btn.setOnClickListener(this);
        fundus_contents  = (LinearLayout) findViewById(R.id.ophthal_fundus_contents);
        fundus_contents.setVisibility(View.GONE);
        add_fundus = (ImageView) findViewById(R.id.fundus_add_submit);
        add_fundus.setOnClickListener(this);
        ophthal_fundus_image = (ImageView) findViewById(R.id.ophthal_fundus_image);
        _edt_add_fundus = (AutoCompleteTextView) findViewById(R.id.fundus_add_edit);
        fundus_gridview_RE = (LinearLayout) findViewById(R.id.fundus_RE_grids);
        fundus_gridview_LE = (LinearLayout) findViewById(R.id.fundus_LE_grids);

        fundusListArraylist = MedisensePracticeDB.getAllFundus(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        prepareFundusCheckBoxes(fundusListArraylist);

        /* *********************** REFRACTION SECTION *************************** */
        refraction_btn = (LinearLayout) findViewById(R.id.ophthal_refraction_btn);
        refraction_btn.setOnClickListener(this);
        refraction_contents  = (LinearLayout) findViewById(R.id.ophthal_refraction_contents);
        refraction_contents.setVisibility(View.GONE);
        ophthal_refraction_image = (ImageView) findViewById(R.id.ophthal_refraction_image);

        /* *********************** REFRACTION SECTION **************************** */
        _edt_refraction_RE_top = (CustomEditText) findViewById(R.id.ophthal_refraction_RE_top);
        _edt_refraction_RE_bottom = (CustomEditText) findViewById(R.id.ophthal_refraction_RE_bottom);
        _edt_refraction_LE_top = (CustomEditText) findViewById(R.id.ophthal_refraction_LE_top);
        _edt_refraction_LE_bottom = (CustomEditText) findViewById(R.id.ophthal_refraction_LE_bottom);

        _edt_refraction_RE_top.setText(REFRACTION_RE_VALUE1);
        _edt_refraction_RE_bottom.setText(REFRACTION_RE_VALUE2);
        _edt_refraction_LE_top.setText(REFRACTION_LE_VALUE1);
        _edt_refraction_LE_bottom.setText(REFRACTION_LE_VALUE2);

        /* ************************** INVESTIGATION SECTION *************************** */
        investigation_btn = (LinearLayout) findViewById(R.id.addvisit_investigation_btn);
        investigation_btn.setOnClickListener(this);
        investigation_contents = (LinearLayout) findViewById(R.id.addvisit_investigation_contents);
        investigation_contents.setVisibility(View.GONE);

        frequentInvestRecyclerview = (RecyclerView) findViewById(R.id.add_visit_freqinvest_recyclerview);
        frequentInvestRecyclerview.setLayoutManager(new GridLayoutManager(EditOphthalVisitDetailsActivity.this, numberOfFrequentColumns));
        frequentInvestAdapter = new FrequentInvestigationAdapters(EditOphthalVisitDetailsActivity.this, freqInvestigationsListArraylist);

        frequentInvestAdapter.setOnItemClickListener(new FrequentInvestigationAdapters.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int adapterPosition, int freqInvestID, int freqTestID, int freqDepartment, int freqDocID, int freqDocType) {
                Log.d(Utils.TAG, "onItemClick ");
                String selected_name = "";
                for(int i=0;i<investigationsListArraylist.size();i++) {
                    if(freqTestID == investigationsListArraylist.get(i).getInvestigationId()) {
                        if( investigationsListArraylist.get(i).getGroupTest().equals("Y")) {
                            for(int j=0; j<grouptestListArraylist.size();j++) {
                                if( investigationsListArraylist.get(i).getTestId().equals(grouptestListArraylist.get(j).getTestId())) {

                                    for(int k=0; k<investigationsListArraylist.size();k++) {
                                        if(grouptestListArraylist.get(j).getSubTestId().equals(investigationsListArraylist.get(k).getTestId())) {
                                            Log.d(Utils.TAG, " GYES GROUPTEST ID: "+grouptestListArraylist.get(j).getTestId()+" MAINTEST ID: "+investigationsListArraylist.get(k).getTestId()+" TEST ID: "+investigationsListArraylist.get(i).getInvestigationId()+" TEST NAME: "+investigationsListArraylist.get(k).getTestName() );
                                            selected_name = investigationsListArraylist.get(k).getTestName();
//                                            PATIENT_INVESTIGATION_ARRAY.add(new Investigations(investigationsListArraylist.get(k).getInvestigationId(),investigationsListArraylist.get(k).getTestId(),grouptestListArraylist.get(j).getTestId(),
//                                                    investigationsListArraylist.get(k).getTestName(),investigationsListArraylist.get(k).getNormalRange(),"","","",investigationsListArraylist.get(k).getInvestDepartment()));

                                            if(investigationsListArraylist.get(k).getInvestDepartment() == 1) {
                                                PATIENT_INVESTIGATION_GENERAL_ARRAY.add(new Investigations(investigationsListArraylist.get(k).getInvestigationId(),
                                                        investigationsListArraylist.get(k).getTestId(),grouptestListArraylist.get(j).getTestId(),investigationsListArraylist.get(k).getTestName(),
                                                        investigationsListArraylist.get(k).getNormalRange(),"","","",investigationsListArraylist.get(k).getInvestDepartment(),
                                                        investigationsListArraylist.get(k).getUserId(),investigationsListArraylist.get(k).getLoginType()));

                                            }
                                            else if(investigationsListArraylist.get(k).getInvestDepartment() == 2) {
                                                PATIENT_INVESTIGATION_OPHTHAL_ARRAY.add(new Investigations(investigationsListArraylist.get(k).getInvestigationId(),
                                                        investigationsListArraylist.get(k).getTestId(),grouptestListArraylist.get(j).getTestId(),investigationsListArraylist.get(k).getTestName(),
                                                        investigationsListArraylist.get(k).getNormalRange(),"","","",investigationsListArraylist.get(k).getInvestDepartment(),
                                                        investigationsListArraylist.get(k).getUserId(),investigationsListArraylist.get(k).getLoginType()));

                                            }
                                            else if(investigationsListArraylist.get(k).getInvestDepartment() == 3) {
                                                PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.add(new Investigations(investigationsListArraylist.get(k).getInvestigationId(),
                                                        investigationsListArraylist.get(k).getTestId(),grouptestListArraylist.get(j).getTestId(),investigationsListArraylist.get(k).getTestName(),
                                                        investigationsListArraylist.get(k).getNormalRange(),"","","",investigationsListArraylist.get(k).getInvestDepartment(),
                                                        investigationsListArraylist.get(k).getUserId(),investigationsListArraylist.get(k).getLoginType()));

                                            }
                                            else if(investigationsListArraylist.get(k).getInvestDepartment() == 4) {
                                                PATIENT_INVESTIGATION_CLINICAL_ARRAY.add(new Investigations(investigationsListArraylist.get(k).getInvestigationId(),
                                                        investigationsListArraylist.get(k).getTestId(),grouptestListArraylist.get(j).getTestId(),investigationsListArraylist.get(k).getTestName(),
                                                        investigationsListArraylist.get(k).getNormalRange(),"","","",investigationsListArraylist.get(k).getInvestDepartment(),
                                                        investigationsListArraylist.get(k).getUserId(),investigationsListArraylist.get(k).getLoginType()));

                                            }
                                            else if(investigationsListArraylist.get(k).getInvestDepartment() == 5) {
                                                PATIENT_INVESTIGATION_OTHER_ARRAY.add(new Investigations(investigationsListArraylist.get(k).getInvestigationId(),
                                                        investigationsListArraylist.get(k).getTestId(),grouptestListArraylist.get(j).getTestId(),investigationsListArraylist.get(k).getTestName(),
                                                        investigationsListArraylist.get(k).getNormalRange(),"","","",investigationsListArraylist.get(k).getInvestDepartment(),
                                                        investigationsListArraylist.get(k).getUserId(),investigationsListArraylist.get(k).getLoginType()));

                                            }
                                        }
                                    }
                                }

                            }
                        }
                        else {
                            Log.d(Utils.TAG, " GNO GROUPTEST ID: "+investigationsListArraylist.get(i).getTestId()+" MAINTEST ID: "+investigationsListArraylist.get(i).getTestId()+" TEST ID: "+freqTestID+" TEST NAME: "+investigationsListArraylist.get(i).getTestName() );
                            selected_name = investigationsListArraylist.get(i).getTestName();
//                            PATIENT_INVESTIGATION_ARRAY.add(new Investigations(freqTestID,investigationsListArraylist.get(i).getTestId(),investigationsListArraylist.get(i).getTestId(),
//                                    investigationsListArraylist.get(i).getTestName(),investigationsListArraylist.get(i).getNormalRange(),"","","",investigationsListArraylist.get(i).getInvestDepartment()));

                            if(investigationsListArraylist.get(i).getInvestDepartment() == 1) {
                                PATIENT_INVESTIGATION_GENERAL_ARRAY.add(new Investigations(freqTestID,
                                        investigationsListArraylist.get(i).getTestId(),investigationsListArraylist.get(i).getTestId(),investigationsListArraylist.get(i).getTestName(),
                                        investigationsListArraylist.get(i).getNormalRange(),"","","",investigationsListArraylist.get(i).getInvestDepartment(),
                                        investigationsListArraylist.get(i).getUserId(),investigationsListArraylist.get(i).getLoginType()));

                            }
                            else if(investigationsListArraylist.get(i).getInvestDepartment() == 2) {
                                PATIENT_INVESTIGATION_OPHTHAL_ARRAY.add(new Investigations(freqTestID,
                                        investigationsListArraylist.get(i).getTestId(),investigationsListArraylist.get(i).getTestId(),investigationsListArraylist.get(i).getTestName(),
                                        investigationsListArraylist.get(i).getNormalRange(),"","","",investigationsListArraylist.get(i).getInvestDepartment(),
                                        investigationsListArraylist.get(i).getUserId(),investigationsListArraylist.get(i).getLoginType()));

                            }
                            else if(investigationsListArraylist.get(i).getInvestDepartment() == 3) {
                                PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.add(new Investigations(freqTestID,
                                        investigationsListArraylist.get(i).getTestId(),investigationsListArraylist.get(i).getTestId(),investigationsListArraylist.get(i).getTestName(),
                                        investigationsListArraylist.get(i).getNormalRange(),"","","",investigationsListArraylist.get(i).getInvestDepartment(),
                                        investigationsListArraylist.get(i).getUserId(),investigationsListArraylist.get(i).getLoginType()));

                            }
                            else if(investigationsListArraylist.get(i).getInvestDepartment() == 4) {
                                PATIENT_INVESTIGATION_CLINICAL_ARRAY.add(new Investigations(freqTestID,
                                        investigationsListArraylist.get(i).getTestId(),investigationsListArraylist.get(i).getTestId(),investigationsListArraylist.get(i).getTestName(),
                                        investigationsListArraylist.get(i).getNormalRange(),"","","",investigationsListArraylist.get(i).getInvestDepartment(),
                                        investigationsListArraylist.get(i).getUserId(),investigationsListArraylist.get(i).getLoginType()));

                            }
                            else if(investigationsListArraylist.get(i).getInvestDepartment() == 5) {
                                PATIENT_INVESTIGATION_OTHER_ARRAY.add(new Investigations(freqTestID,
                                        investigationsListArraylist.get(i).getTestId(),investigationsListArraylist.get(i).getTestId(),investigationsListArraylist.get(i).getTestName(),
                                        investigationsListArraylist.get(i).getNormalRange(),"","","",investigationsListArraylist.get(i).getInvestDepartment(),
                                        investigationsListArraylist.get(i).getUserId(),investigationsListArraylist.get(i).getLoginType()));

                            }
                        }
                    }

                }
                Toast.makeText(EditOphthalVisitDetailsActivity.this,selected_name+ " is added to the list", Toast.LENGTH_SHORT).show();

                refreshInvestigationTable();
            }
        });
        frequentInvestRecyclerview.setAdapter(frequentInvestAdapter);

        freqInvestigationsListArraylist = MedisensePracticeDB.getAllFrequentInvestigations(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        if(freqInvestigationsListArraylist.size() > 0) {
            Log.d(Utils.TAG, " frqInvest Count: " + freqInvestigationsListArraylist.size());
            frequentInvestAdapter = new FrequentInvestigationAdapters(EditOphthalVisitDetailsActivity.this, freqInvestigationsListArraylist);
            frequentInvestRecyclerview.setAdapter(frequentInvestAdapter);
            frequentInvestAdapter.notifyDataSetChanged();
        }

        investigationsListArraylist = MedisensePracticeDB.getAllInvestigationTests(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        if(investigationsListArraylist.size() > 0) {
            Log.d(Utils.TAG, " investCount: " + investigationsListArraylist.size());
        }

        grouptestListArraylist = MedisensePracticeDB.getAllInvestigationGroupTest(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        if(grouptestListArraylist.size() > 0) {
            Log.d(Utils.TAG, " invest Group Count: " + grouptestListArraylist.size());
        }

        LinearLayout add_investigationtest = (LinearLayout) findViewById(R.id.add_investigation);
        add_investigationtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareInvestigationTest(investigationsListArraylist);
            }
        });

        // Investigation Recyclerview
        investigation_tab_header = (LinearLayout) findViewById(R.id.add_visit_investtab_header);
        investigation_tab_header.setVisibility(View.GONE);
        investigation_ophthal_tab_header  = (LinearLayout) findViewById(R.id.add_visit_opthalinvesttab_header);
        investigation_ophthal_tab_header.setVisibility(View.GONE);
        investigation_radiology_tab_header = (LinearLayout)  findViewById(R.id.add_visit_ophthal_readilogy_investtab_header);
        investigation_radiology_tab_header.setVisibility(View.GONE);
        investigation_clinical_tab_header = (LinearLayout) findViewById(R.id.add_visit_ophthal_clinical_investtab_header);
        investigation_clinical_tab_header.setVisibility(View.GONE);
        investigation_oother_tab_header  = (LinearLayout) findViewById(R.id.add_visit_ophthal_other_investtab_header);
        investigation_oother_tab_header.setVisibility(View.GONE);

        if(PATIENT_INVESTIGATION_GENERAL_ARRAY.size() > 0) {
            investigation_tab_header.setVisibility(View.VISIBLE);
        }
        else {
            investigation_tab_header.setVisibility(View.GONE);
        }

        if(PATIENT_INVESTIGATION_OPHTHAL_ARRAY.size() > 0) {
            investigation_ophthal_tab_header.setVisibility(View.VISIBLE);
        }
        else {
            investigation_ophthal_tab_header.setVisibility(View.GONE);
        }

        if(PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.size() > 0 ) {
            investigation_radiology_tab_header.setVisibility(View.VISIBLE);
        }
        else {
            investigation_radiology_tab_header.setVisibility(View.GONE);
        }

        if(PATIENT_INVESTIGATION_CLINICAL_ARRAY.size() > 0 ) {
            investigation_clinical_tab_header.setVisibility(View.VISIBLE);
        }
        else {
            investigation_clinical_tab_header.setVisibility(View.GONE);
        }

        if(PATIENT_INVESTIGATION_OTHER_ARRAY.size() > 0 ) {
            investigation_oother_tab_header.setVisibility(View.VISIBLE);
        }
        else {
            investigation_oother_tab_header.setVisibility(View.GONE);
        }

        investigationRecyclerview = (RecyclerView) findViewById(R.id.add_vistit_investigation_recycler);
        investigationAdapter = new InvestigationAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_INVESTIGATION_GENERAL_ARRAY);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(EditOphthalVisitDetailsActivity.this);
        investigationRecyclerview.setLayoutManager(mLayoutManager2);
        investigationRecyclerview.setItemAnimator(new DefaultItemAnimator());
        investigationRecyclerview.setAdapter(investigationAdapter);

        investigationOphthalRecyclerview = (RecyclerView) findViewById(R.id.add_vistit_ophthal_investigation_recycler);
        investigationOphthalAdapter = new InvestigationOphthalAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_INVESTIGATION_OPHTHAL_ARRAY);
        RecyclerView.LayoutManager mLayoutManager2a = new LinearLayoutManager(EditOphthalVisitDetailsActivity.this);
        investigationOphthalRecyclerview.setLayoutManager(mLayoutManager2a);
        investigationOphthalRecyclerview.setItemAnimator(new DefaultItemAnimator());
        investigationOphthalRecyclerview.setAdapter(investigationOphthalAdapter);

        investigationRadiologyRecyclerview = (RecyclerView)  findViewById(R.id.add_vistit_ophthal_radiology_investigation_recycler);
        investigationRadiologyAdapter = new InvestigationRadiologyAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_INVESTIGATION_RADIOLOGY_ARRAY);
        RecyclerView.LayoutManager mLayoutManager2b = new LinearLayoutManager(EditOphthalVisitDetailsActivity.this);
        investigationRadiologyRecyclerview.setLayoutManager(mLayoutManager2b);
        investigationRadiologyRecyclerview.setItemAnimator(new DefaultItemAnimator());
        investigationRadiologyRecyclerview.setAdapter(investigationRadiologyAdapter);

        investigationClinicalRecyclerview = (RecyclerView) findViewById(R.id.add_vistit_ophthal_clinical_investigation_recycler);
        investigationClinicalAdapter = new InvestigationClinicalAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_INVESTIGATION_CLINICAL_ARRAY);
        RecyclerView.LayoutManager mLayoutManager2c = new LinearLayoutManager(EditOphthalVisitDetailsActivity.this);
        investigationClinicalRecyclerview.setLayoutManager(mLayoutManager2c);
        investigationClinicalRecyclerview.setItemAnimator(new DefaultItemAnimator());
        investigationClinicalRecyclerview.setAdapter(investigationClinicalAdapter);

        investigationOtherRecyclerview = (RecyclerView) findViewById(R.id.add_vistit_ophthal_other_investigation_recycler);
        investigationOtherAdapter  = new InvestigationOtherAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_INVESTIGATION_OTHER_ARRAY);
        RecyclerView.LayoutManager mLayoutManager2d = new LinearLayoutManager(EditOphthalVisitDetailsActivity.this);
        investigationOtherRecyclerview.setLayoutManager(mLayoutManager2d);
        investigationOtherRecyclerview.setItemAnimator(new DefaultItemAnimator());
        investigationOtherRecyclerview.setAdapter(investigationOtherAdapter);

        CustomTextViewBold btn_submit = (CustomTextViewBold) findViewById(R.id.add_vistit_investigation_clear);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PATIENT_INVESTIGATION_ARRAY = new ArrayList<>();
                PATIENT_INVESTIGATION_GENERAL_ARRAY = new ArrayList<>();
                PATIENT_INVESTIGATION_OPHTHAL_ARRAY = new ArrayList<>();
                PATIENT_INVESTIGATION_RADIOLOGY_ARRAY = new ArrayList<>();
                PATIENT_INVESTIGATION_CLINICAL_ARRAY = new ArrayList<>();
                PATIENT_INVESTIGATION_OTHER_ARRAY = new ArrayList<>();

                investigation_tab_header.setVisibility(View.GONE);
                investigation_ophthal_tab_header.setVisibility(View.GONE);
                investigation_radiology_tab_header.setVisibility(View.GONE);
                investigation_clinical_tab_header.setVisibility(View.GONE);
                investigation_oother_tab_header.setVisibility(View.GONE);

                investigationAdapter = new InvestigationAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_INVESTIGATION_GENERAL_ARRAY);
                investigationRecyclerview.setAdapter(investigationAdapter);
                investigationAdapter.notifyDataSetChanged();

                investigationOphthalAdapter = new InvestigationOphthalAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_INVESTIGATION_OPHTHAL_ARRAY);
                investigationOphthalRecyclerview.setAdapter(investigationOphthalAdapter);
                investigationOphthalAdapter.notifyDataSetChanged();

                investigationRadiologyAdapter = new InvestigationRadiologyAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_INVESTIGATION_RADIOLOGY_ARRAY);
                investigationRadiologyRecyclerview.setAdapter(investigationRadiologyAdapter);
                investigationRadiologyAdapter.notifyDataSetChanged();

                investigationClinicalAdapter = new InvestigationClinicalAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_INVESTIGATION_CLINICAL_ARRAY);
                investigationClinicalRecyclerview.setAdapter(investigationClinicalAdapter);
                investigationClinicalAdapter.notifyDataSetChanged();

                investigationOtherAdapter  = new InvestigationOtherAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_INVESTIGATION_OTHER_ARRAY);
                investigationOtherRecyclerview.setAdapter(investigationOtherAdapter);
                investigationOtherAdapter.notifyDataSetChanged();
            }
        });

        investigation_template_layout = (LinearLayout) findViewById(R.id.add_visit_invest_template_layout);
        investigation_template_layout.setVisibility(View.GONE);
        investigationTemplateRecyclerview = (RecyclerView) findViewById(R.id.add_visit_invest_template_recyclerview);

        investigationTemplateListArraylist = MedisensePracticeDB.getInvestigationTemplateNames(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        Log.d(Utils.TAG, " investTempList: " + investigationTemplateListArraylist.size());

        investigationTemplateRecyclerview.setLayoutManager(new GridLayoutManager(EditOphthalVisitDetailsActivity.this, numberOfFrequentColumns));
        investigationTemplateAdapter = new InvestigationTemplateAdapters(EditOphthalVisitDetailsActivity.this, investigationTemplateListArraylist);

       /* if(investigationTemplateListArraylist.size() > 0) {
            investigation_template_layout.setVisibility(View.VISIBLE);
            investigationTemplateAdapter = new InvestigationTemplateAdapters(EditOphthalVisitDetailsActivity.this, investigationTemplateListArraylist);
            investigationTemplateRecyclerview.setAdapter(investigationTemplateAdapter);
            investigationTemplateAdapter.notifyDataSetChanged();

            List<InvestigationTemplates> InvestTemplateListArraylist = new ArrayList<>();
            for(int k=0;k<investigationTemplateListArraylist.size();k++) {
                if(investigationTemplateListArraylist.get(k).getTemplateDefaultVisible() == 1) {
                    InvestTemplateListArraylist = MedisensePracticeDB.getInvestigationTemplateLists(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID, investigationTemplateListArraylist.get(k).getTemplateID());
                    for(int i=0; i<InvestTemplateListArraylist.size();i++) {

                        if(InvestTemplateListArraylist.get(i).getTemplateTestDepartment() == 1) {

                            PATIENT_INVESTIGATION_GENERAL_ARRAY.add(new Investigations(0,InvestTemplateListArraylist.get(i).getTemplateTestID(),
                                    InvestTemplateListArraylist.get(i).getTemplateGroupTestID(), InvestTemplateListArraylist.get(i).getTemplateTestName(),
                                    InvestTemplateListArraylist.get(i).getTemplateNormalValue(), InvestTemplateListArraylist.get(i).getTemplateActualValue(),
                                    InvestTemplateListArraylist.get(i).getTemplateRightEye(), InvestTemplateListArraylist.get(i).getTemplateLeftEye(),
                                    InvestTemplateListArraylist.get(i).getTemplateTestDepartment(), InvestTemplateListArraylist.get(i).getTemplateUserID(),
                                    InvestTemplateListArraylist.get(i).getTemplateLoginType()));
                        }
                        else if(InvestTemplateListArraylist.get(i).getTemplateTestDepartment() == 2) {

                            PATIENT_INVESTIGATION_OPHTHAL_ARRAY.add(new Investigations(0,InvestTemplateListArraylist.get(i).getTemplateTestID(),
                                    InvestTemplateListArraylist.get(i).getTemplateGroupTestID(), InvestTemplateListArraylist.get(i).getTemplateTestName(),
                                    InvestTemplateListArraylist.get(i).getTemplateNormalValue(), InvestTemplateListArraylist.get(i).getTemplateActualValue(),
                                    InvestTemplateListArraylist.get(i).getTemplateRightEye(), InvestTemplateListArraylist.get(i).getTemplateLeftEye(),
                                    InvestTemplateListArraylist.get(i).getTemplateTestDepartment(), InvestTemplateListArraylist.get(i).getTemplateUserID(),
                                    InvestTemplateListArraylist.get(i).getTemplateLoginType()));
                        }

                    }
                }
            }
            refreshInvestigationTable();
        } */

        investigationTemplateAdapter.setOnItemClickListener(new InvestigationTemplateAdapters.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int templateID, String templateName) {
                List<InvestigationTemplates> TemplateListArraylist = new ArrayList<>();

                TemplateListArraylist = MedisensePracticeDB.getInvestigationTemplateLists(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID, templateID);
                for(int i=0; i<TemplateListArraylist.size();i++) {

                    if(TemplateListArraylist.get(i).getTemplateTestDepartment() == 1) {

                        PATIENT_INVESTIGATION_GENERAL_ARRAY.add(new Investigations(0,TemplateListArraylist.get(i).getTemplateTestID(),
                                TemplateListArraylist.get(i).getTemplateGroupTestID(), TemplateListArraylist.get(i).getTemplateTestName(),
                                TemplateListArraylist.get(i).getTemplateNormalValue(), TemplateListArraylist.get(i).getTemplateActualValue(),
                                TemplateListArraylist.get(i).getTemplateRightEye(), TemplateListArraylist.get(i).getTemplateLeftEye(),
                                TemplateListArraylist.get(i).getTemplateTestDepartment(), TemplateListArraylist.get(i).getTemplateUserID(),
                                TemplateListArraylist.get(i).getTemplateLoginType()));
                    }
                    else if(TemplateListArraylist.get(i).getTemplateTestDepartment() == 2) {

                        PATIENT_INVESTIGATION_OPHTHAL_ARRAY.add(new Investigations(0,TemplateListArraylist.get(i).getTemplateTestID(),
                                TemplateListArraylist.get(i).getTemplateGroupTestID(), TemplateListArraylist.get(i).getTemplateTestName(),
                                TemplateListArraylist.get(i).getTemplateNormalValue(), TemplateListArraylist.get(i).getTemplateActualValue(),
                                TemplateListArraylist.get(i).getTemplateRightEye(), TemplateListArraylist.get(i).getTemplateLeftEye(),
                                TemplateListArraylist.get(i).getTemplateTestDepartment(), TemplateListArraylist.get(i).getTemplateUserID(),
                                TemplateListArraylist.get(i).getTemplateLoginType()));
                    }
                    else if(TemplateListArraylist.get(i).getTemplateTestDepartment() == 3) {

                        PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.add(new Investigations(0,TemplateListArraylist.get(i).getTemplateTestID(),
                                TemplateListArraylist.get(i).getTemplateGroupTestID(), TemplateListArraylist.get(i).getTemplateTestName(),
                                TemplateListArraylist.get(i).getTemplateNormalValue(), TemplateListArraylist.get(i).getTemplateActualValue(),
                                TemplateListArraylist.get(i).getTemplateRightEye(), TemplateListArraylist.get(i).getTemplateLeftEye(),
                                TemplateListArraylist.get(i).getTemplateTestDepartment(), TemplateListArraylist.get(i).getTemplateUserID(),
                                TemplateListArraylist.get(i).getTemplateLoginType()));
                    }
                    else if(TemplateListArraylist.get(i).getTemplateTestDepartment() == 4) {

                        PATIENT_INVESTIGATION_CLINICAL_ARRAY.add(new Investigations(0,TemplateListArraylist.get(i).getTemplateTestID(),
                                TemplateListArraylist.get(i).getTemplateGroupTestID(), TemplateListArraylist.get(i).getTemplateTestName(),
                                TemplateListArraylist.get(i).getTemplateNormalValue(), TemplateListArraylist.get(i).getTemplateActualValue(),
                                TemplateListArraylist.get(i).getTemplateRightEye(), TemplateListArraylist.get(i).getTemplateLeftEye(),
                                TemplateListArraylist.get(i).getTemplateTestDepartment(), TemplateListArraylist.get(i).getTemplateUserID(),
                                TemplateListArraylist.get(i).getTemplateLoginType()));
                    }
                    else if(TemplateListArraylist.get(i).getTemplateTestDepartment() == 5) {

                        PATIENT_INVESTIGATION_OTHER_ARRAY.add(new Investigations(0,TemplateListArraylist.get(i).getTemplateTestID(),
                                TemplateListArraylist.get(i).getTemplateGroupTestID(), TemplateListArraylist.get(i).getTemplateTestName(),
                                TemplateListArraylist.get(i).getTemplateNormalValue(), TemplateListArraylist.get(i).getTemplateActualValue(),
                                TemplateListArraylist.get(i).getTemplateRightEye(), TemplateListArraylist.get(i).getTemplateLeftEye(),
                                TemplateListArraylist.get(i).getTemplateTestDepartment(), TemplateListArraylist.get(i).getTemplateUserID(),
                                TemplateListArraylist.get(i).getTemplateLoginType()));
                    }
                }

                refreshInvestigationTable();
            }
        });

        investigationaddtemplate_checkbox = (AppCompatCheckBox) findViewById(R.id.add_visit_investigation_template_checkbox);
        investigation_addtemplate_name = (CustomEditText) findViewById(R.id.add_visit_investigation_template_name);
        investigation_addtemplate_name.setVisibility(View.GONE);
        investigation_template_save = 0;
        investigationaddtemplate_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    investigation_addtemplate_name.setVisibility(View.VISIBLE);        // checked
                    investigation_template_save = 1;
                }
                else
                {
                    investigation_addtemplate_name.setVisibility(View.GONE);           // not checked
                    investigation_template_save = 0;
                    investigation_addtemplate_name.setText("");
                }
            }

        });

        /************************* DIAGNOSIS SECTION ************************************/
        diagnosis_btn = (LinearLayout) findViewById(R.id.addvisit_diagnosis_btn);
        diagnosis_btn.setOnClickListener(this);
        diagnosis_contents = (LinearLayout) findViewById(R.id.addvisit_diagnosis_contents);
        diagnosis_contents.setVisibility(View.GONE);

        frequentDiagnosisRecyclerview = (RecyclerView) findViewById(R.id.add_visit_freq_diagnosis_recyclerview);
        frequentDiagnosisRecyclerview.setLayoutManager(new GridLayoutManager(EditOphthalVisitDetailsActivity.this, numberOfFrequentColumns));
        frequentDiagnosisAdapter = new FrequentDiagnosisAdapters(EditOphthalVisitDetailsActivity.this, freqDiagnosisListArraylist);

        frequentDiagnosisAdapter.setOnItemClickListener(new FrequentDiagnosisAdapters.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position, int freqDiagnosisID, int freqICDID, String ICD_Name, int freqDocID, int freqDocType) {
                Toast.makeText(EditOphthalVisitDetailsActivity.this,ICD_Name+ " is added to the list", Toast.LENGTH_SHORT).show();

                PATIENT_DAIGNOSIS_ARRAY.add(new Diagnosis(freqICDID,ICD_Name, USER_ID, USER_LOGIN_TYPE));
                refreshDiagnosis();
            }
        });

        frequentDiagnosisRecyclerview.setAdapter(frequentDiagnosisAdapter);

        freqDiagnosisListArraylist = MedisensePracticeDB.getAllFrequentDiagnosis(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        if(freqDiagnosisListArraylist.size() > 0) {
            Log.d(Utils.TAG, " freq Diagno Count: " + freqDiagnosisListArraylist.size());
            frequentDiagnosisAdapter = new FrequentDiagnosisAdapters(EditOphthalVisitDetailsActivity.this, freqDiagnosisListArraylist);
            frequentDiagnosisRecyclerview.setAdapter(frequentDiagnosisAdapter);
            frequentDiagnosisAdapter.notifyDataSetChanged();

        }

        LinearLayout add_diagnosis = (LinearLayout) findViewById(R.id.add_diagnosis);
        add_diagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareDiagnosis(freqDiagnosisListArraylist);
            }
        });

        diagnosisRecyclerview = (RecyclerView) findViewById(R.id.add_vistit_diagnosis_recycler);
        diagnosisAdapter = new DiagnosisAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_DAIGNOSIS_ARRAY);
        RecyclerView.LayoutManager mLayoutManager4 = new LinearLayoutManager(EditOphthalVisitDetailsActivity.this);
        diagnosisRecyclerview.setLayoutManager(mLayoutManager4);
        diagnosisRecyclerview.setItemAnimator(new DefaultItemAnimator());
        diagnosisRecyclerview.setAdapter(diagnosisAdapter);

        /*************************** TREATMENTS SECTION *******************************/
        treatment_btn = (LinearLayout) findViewById(R.id.addvisit_treatment_btn);
        treatment_btn.setOnClickListener(this);
        treatment_contents = (LinearLayout) findViewById(R.id.addvisit_treatment_contents);
        treatment_contents.setVisibility(View.GONE);

        frequentTreatmentRecyclerview = (RecyclerView) findViewById(R.id.add_visit_freq_treatment_recyclerview);
        frequentTreatmentRecyclerview.setLayoutManager(new GridLayoutManager(EditOphthalVisitDetailsActivity.this, numberOfFrequentColumns));
        frequenttreatmentAdapter = new FrequentTreatmentAdapters(EditOphthalVisitDetailsActivity.this, freqTreatmentListArraylist);

        frequenttreatmentAdapter.setOnItemClickListener(new FrequentTreatmentAdapters.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position, int treatment_id, String treatment_name, int freqDocID, int freqDocType, int freqCount) {
                Toast.makeText(EditOphthalVisitDetailsActivity.this,treatment_name+ " is added to the list", Toast.LENGTH_SHORT).show();

                PATIENT_TREATMENT_ARRAY.add(new Treatments(treatment_id,treatment_name,
                        freqDocID,freqDocType, freqCount, USER_ID, USER_LOGIN_TYPE));
                refreshTreatments();
            }
        });
        frequentTreatmentRecyclerview.setAdapter(frequenttreatmentAdapter);

        freqTreatmentListArraylist = MedisensePracticeDB.getAllFrequentTreatments(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        if(freqTreatmentListArraylist.size() > 0) {
            Log.d(Utils.TAG, " freq Treat count: " + freqTreatmentListArraylist.size());
            frequenttreatmentAdapter = new FrequentTreatmentAdapters(EditOphthalVisitDetailsActivity.this, freqTreatmentListArraylist);
            frequentTreatmentRecyclerview.setAdapter(frequenttreatmentAdapter);
            frequenttreatmentAdapter.notifyDataSetChanged();
        }

        treatmentRecyclerview = (RecyclerView) findViewById(R.id.add_vistit_treatment_recycler);
        treatmentAdapter = new TreatmentsAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_TREATMENT_ARRAY);
        RecyclerView.LayoutManager mLayoutManager5 = new LinearLayoutManager(EditOphthalVisitDetailsActivity.this);
        treatmentRecyclerview.setLayoutManager(mLayoutManager5);
        treatmentRecyclerview.setItemAnimator(new DefaultItemAnimator());
        treatmentRecyclerview.setAdapter(treatmentAdapter);

        treatmentListArraylist = MedisensePracticeDB.getAllTreatments(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        if(treatmentListArraylist.size() > 0) {
            Log.d(Utils.TAG, " Treat count: " + freqTreatmentListArraylist.size());
        }

        LinearLayout add_treatment = (LinearLayout) findViewById(R.id.add_treatment);
        add_treatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareTreatments(treatmentListArraylist);
            }
        });

        /***************************** PRESCRIPTION SECTION **********************/
        prescription_btn = (LinearLayout) findViewById(R.id.addvisit_prescription_btn);
        prescription_btn.setOnClickListener(this);
        prescription_contents = (LinearLayout) findViewById(R.id.addvisit_prescription_contents);
        prescription_contents.setVisibility(View.GONE);

        prescription_header = (LinearLayout) findViewById(R.id.add_vistitprescription_header);
        prescription_header.setVisibility(View.GONE);

        _edt_template_name = (CustomEditText) findViewById(R.id.add_visit_template_name);
        save_template = (ImageView) findViewById(R.id.add_visit_template_save);
        save_template.setOnClickListener(this);

        _edt_prescription_note = (CustomEditText) findViewById(R.id.add_vistitpresc_note);

        load_templates = (LinearLayout) findViewById(R.id.load_templates);
        load_templates.setOnClickListener(this);

        Gson gson = new Gson();
        if (TEMPLATES_LIST.equals("")) { }
        else {
            templateListArraylist = gson.fromJson(TEMPLATES_LIST, new TypeToken<List<MyTemplates>>() {
            }.getType());
            if(templateListArraylist.size() > 0 ) {
                Log.d(Utils.TAG, "templateListArraylist > 0 " + templateListArraylist.size());
            }
        }

        frequentPrescriptionRecyclerview = (RecyclerView) findViewById(R.id.add_visit_freq_prescription_recyclerview);
        frequentPrescriptionRecyclerview.setLayoutManager(new GridLayoutManager(EditOphthalVisitDetailsActivity.this, numberOfFrequentColumns));
        frequentPrescriptionAdapter = new FrequentPrescriptionsAdapters(EditOphthalVisitDetailsActivity.this, freqPrescriptionsListArraylist);

        frequentPrescriptionAdapter.setOnItemClickListener(new FrequentPrescriptionsAdapters.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int prescFreqId, int prescriptionId, String tradeName, int genericId, String genericName, String dosage, String timings, String duration, int docId, int docType, int freqCount) {
                Log.d(Utils.TAG, "onItemClick: ");
                Toast.makeText(EditOphthalVisitDetailsActivity.this,tradeName+ " is added to the list", Toast.LENGTH_SHORT).show();

                PATIENT_PRESCRIPTION_ARRAY.add(new FrequentPrescription(prescFreqId,prescriptionId,tradeName,genericId,genericName,dosage,timings,duration,docId,docType,freqCount,USER_ID,USER_LOGIN_TYPE));
                refreshPrescriptions();
            }
        });
        frequentPrescriptionRecyclerview.setAdapter(frequentPrescriptionAdapter);

        freqPrescriptionsListArraylist = MedisensePracticeDB.getAllFrequentPrescriptions(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        if(freqPrescriptionsListArraylist.size() > 0) {
            Log.d(Utils.TAG, " freq Presc count: " + freqPrescriptionsListArraylist.size());
            frequentPrescriptionAdapter = new FrequentPrescriptionsAdapters(EditOphthalVisitDetailsActivity.this, freqPrescriptionsListArraylist);
            frequentPrescriptionRecyclerview.setAdapter(frequentPrescriptionAdapter);
            frequentPrescriptionAdapter.notifyDataSetChanged();
        }

        LinearLayout add_prescr = (LinearLayout) findViewById(R.id.add_prescription);
        add_prescr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preparePrescription(freqPrescriptionsListArraylist);
            }
        });

        prescriptionRecyclerview = (RecyclerView) findViewById(R.id.add_vistitprescription_recycler);
        prescriptionAdapter = new PrescriptionAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_PRESCRIPTION_ARRAY);
        RecyclerView.LayoutManager mLayoutManager6 = new LinearLayoutManager(EditOphthalVisitDetailsActivity.this);
        prescriptionRecyclerview.setLayoutManager(mLayoutManager6);
        prescriptionRecyclerview.setItemAnimator(new DefaultItemAnimator());
        prescriptionRecyclerview.setAdapter(prescriptionAdapter);

        repeatPrescriptionsListArraylist = MedisensePracticeDB.getAllRepeatPrescriptions(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        if(repeatPrescriptionsListArraylist.size() > 0) {
            Log.d(Utils.TAG, " repeat Presc count: " + repeatPrescriptionsListArraylist.size());
        }

        repeat_prescription_btn = (CustomTextViewBold) findViewById(R.id.prescription_repeat_btn);
        repeat_prescription_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int j = 0; j < repeatPrescriptionsListArraylist.size(); j++) {
                    PATIENT_PRESCRIPTION_ARRAY.add(new FrequentPrescription(repeatPrescriptionsListArraylist.get(j).getPrescFreqId(),repeatPrescriptionsListArraylist.get(j).getPrescriptionId(),repeatPrescriptionsListArraylist.get(j).getTradeName(),repeatPrescriptionsListArraylist.get(j).getGenericId(),repeatPrescriptionsListArraylist.get(j).getGenericName(),repeatPrescriptionsListArraylist.get(j).getDosage(),repeatPrescriptionsListArraylist.get(j).getTimings(),repeatPrescriptionsListArraylist.get(j).getDuration(),repeatPrescriptionsListArraylist.get(j).getDocId(),repeatPrescriptionsListArraylist.get(j).getDocType(),repeatPrescriptionsListArraylist.get(j).getFreqCount(),USER_ID,USER_LOGIN_TYPE));
                }
                refreshPrescriptions();
            }
        });

        patientedu_info_image = (ImageView) findViewById(R.id.add_visit_patientedu_info_image);
        patient_education_spinner = (Spinner)  findViewById(R.id.add_visit_patientedu_spinner);
        patientEducationListArraylist  = MedisensePracticeDB.getAllPatientEducation(EditOphthalVisitDetailsActivity.this, USER_LOGIN_TYPE, USER_ID);
        if(patientEducationListArraylist.size() > 0 ) {
            ArrayList<String> educationArray = new ArrayList<String>();
            int temp_patient_education = 0;
            String temp_patient_education_title = "";

            patientEducationListArraylist.add(0, new PatientEducation(0,"--Select--","",USER_ID, 1,USER_ID, USER_LOGIN_TYPE));
            for (int i = 0; i < patientEducationListArraylist.size(); i++) {
                educationArray.add(patientEducationListArraylist.get(i).getEducationTitle());

                if(PATIENT_EDUCATION != 0 && PATIENT_EDUCATION == patientEducationListArraylist.get(i).getEducationID()) {
                    temp_patient_education = patientEducationListArraylist.get(i).getEducationID();
                    temp_patient_education_title = patientEducationListArraylist.get(i).getEducationTitle();
                   // Log.d(Utils.TAG, " edu_id res: "  + temp_patient_education);
                  //  Log.d(Utils.TAG, " edu_desc res: "  + temp_patient_education_title);
                }
            }

            final ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(EditOphthalVisitDetailsActivity.this, R.layout.spinner_text, educationArray);
            patient_education_spinner.setAdapter(locationAdapter);
            patient_education_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    String edu_title = (String) parent.getItemAtPosition(position);

                    PATIENT_EDUCATION = patientEducationListArraylist.get(position).getEducationID();
                    Log.d(Utils.TAG, " PATIENT_EDUCATION " + PATIENT_EDUCATION);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    PATIENT_EDUCATION = 0;
                    Log.d(Utils.TAG, " PATIENT_EDUCATION "  + PATIENT_EDUCATION);
                }
            });

            if(!temp_patient_education_title.equals("")) {
                patient_education_spinner.setSelection(getIndex(patient_education_spinner, temp_patient_education_title));
            }

        }

        patientedu_info_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(EditOphthalVisitDetailsActivity.this, R.style.CustomDialog).create();
                alertDialog.setTitle("Patient Education Note");
                alertDialog.setMessage("If you want to add any instruction note for patient then goto Settings & add instruction in Patient education section in Web application");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });


        /**************** Others *******************/

        _edt_consultency_fee = (CustomEditText) findViewById(R.id.add_visit_consultationFee);
        _edt_followup_date = (CustomTextView) findViewById(R.id.add_visit_followup_date);
        _edt_visit_date = (CustomTextView) findViewById(R.id.add_visit_date);
        _edt_diagnosis_deatils = (CustomEditText) findViewById(R.id.add_vistit_diagnosis_details);
        _edt_treatment_details = (CustomEditText) findViewById(R.id.add_vistit_treatment_details);
        consultion_fee_contents = (CustomTextViewSemiBold) findViewById(R.id.add_visit_consultfees_status);
        consultion_fee_contents.setVisibility(View.GONE);
        consultfees_Set_btn = (CustomTextView) findViewById(R.id.add_visit_consultfee_set);
        consultfees_Set_btn.setOnClickListener(this);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        _edt_visit_date.setText(currentDateandTime);

        _edt_visit_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                _edt_visit_date.setText("");
                v.setOnTouchListener(null);
                return false;
            }
        });

        _edt_visit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _edt_visit_date.setText("");
                final Calendar currentDate = Calendar.getInstance();
                final Calendar date = myCalendar.getInstance();
                new DatePickerDialog(EditOphthalVisitDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(EditOphthalVisitDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                date.set(Calendar.MINUTE, minute);

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
                                String selectedDateandTime = sdf.format(date.getTime());
                                _edt_visit_date.setText(selectedDateandTime);

                                Log.v(Utils.TAG, "The choosen one " + date.getTime() + " dt: "+selectedDateandTime);
                            }
                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });

        _edt_followup_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                _edt_followup_date.setText("");
                v.setOnTouchListener(null);
                return false;
            }
        });

        _edt_followup_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _edt_followup_date.setText("");

                new DatePickerDialog(EditOphthalVisitDetailsActivity.this, dateSel, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        gson = new Gson();
        if (!OTHER_SETTINGS_LIST.equals("")) {
            otherSettingsListArraylist = gson.fromJson(OTHER_SETTINGS_LIST, new TypeToken<List<OtherSettings>>() {
            }.getType());
            if(otherSettingsListArraylist.size() > 0 ) {
                Log.d(Utils.TAG, "OTHER_SETTINGS_LIST > 0 " + otherSettingsListArraylist.size());
                for(int i=0;i<otherSettingsListArraylist.size();i++) {
                    COLLECT_CONSULT_BEFORE = String.valueOf(otherSettingsListArraylist.get(i).getConsultationFee());
                    Log.d(Utils.TAG +"CONS_BEFORE:", COLLECT_CONSULT_BEFORE);

                    if(COLLECT_CONSULT_BEFORE.equals("1")) {                // 1 - Yes. ie., Pre Consultaion Fess, 2 - NO
                        consultion_fee_contents.setVisibility(View.VISIBLE);
                        _edt_consultency_fee.setText("");
                    }
                    else  if(COLLECT_CONSULT_BEFORE.equals("2")) {
                        consultion_fee_contents.setVisibility(View.GONE);
                        _edt_consultency_fee.setText(CONSULTATION_CHARGES);
                    }
                    else {
                        consultion_fee_contents.setVisibility(View.GONE);
                        _edt_consultency_fee.setText(CONSULTATION_CHARGES);
                    }
                }
            }
        }
        else {
            consultion_fee_contents.setVisibility(View.GONE);
            _edt_consultency_fee.setText(CONSULTATION_CHARGES);
        }

        /* ************************ SPECTACLE PRESCRIPTION SECTION ************************* */
        spectacle_prescription_btn = (LinearLayout) findViewById(R.id.ophthal_spectacle_prescription_btn);
        spectacle_prescription_btn.setOnClickListener(this);
        spectacle_prescription_contents = (LinearLayout) findViewById(R.id.ophthal_spectacle_prescription_contents);
        spectacle_prescription_contents.setVisibility(View.GONE);

        _edt_right_dv_sphere = (CustomEditText) findViewById(R.id.spect_dv_sphere_right);
        _edt_right_dv_cyl = (CustomEditText) findViewById(R.id.spect_dv_cyl_right);
        _edt_right_dv_axis = (CustomEditText) findViewById(R.id.spect_dv_axis_right);
        _edt_right_nv_sphere = (CustomEditText) findViewById(R.id.spect_nv_sphere_right);
        _edt_right_nv_cyl = (CustomEditText) findViewById(R.id.spect_nv_cyl_right);
        _edt_right_nv_axis = (CustomEditText) findViewById(R.id.spect_nv_axis_right);
        _edt_right_ipd = (CustomEditText) findViewById(R.id.spect_ipd_right);

        _edt_left_dv_sphere = (CustomEditText) findViewById(R.id.spect_dv_sphere_left);
        _edt_left_dv_cyl = (CustomEditText) findViewById(R.id.spect_dv_cyl_left);
        _edt_left_dv_axis = (CustomEditText) findViewById(R.id.spect_dv_axis_left);
        _edt_left_nv_sphere = (CustomEditText) findViewById(R.id.spect_nv_sphere_left);
        _edt_left_nv_cyl = (CustomEditText) findViewById(R.id.spect_nv_cyl_left);
        _edt_left_nv_axis = (CustomEditText) findViewById(R.id.spect_nv_axis_left);
        _edt_left_ipd = (CustomEditText) findViewById(R.id.spect_ipd_left);

        _edt_right_dv_sphere.setText(GET_DV_SPHERE_RIGHT);
        _edt_right_dv_cyl.setText(GET_DV_CYCLE_RIGHT);
        _edt_right_dv_axis.setText(GET_DV_AXIS_RIGHT);
        _edt_right_nv_sphere.setText(GET_NV_SPHERE_RIGHT);
        _edt_right_nv_cyl.setText(GET_NV_CYCLE_RIGHT);
        _edt_right_nv_axis.setText(GET_NV_AXIS_RIGHT);
        _edt_right_ipd.setText(GET_IPD_RIGHT);
        _edt_left_dv_sphere.setText(GET_DV_SPHERE_LEFT);
        _edt_left_dv_cyl.setText(GET_DV_CYCLE_LEFT);
        _edt_left_dv_axis.setText(GET_DV_AXIS_LEFT);
        _edt_left_nv_sphere.setText(GET_NV_SPHERE_LEFT);
        _edt_left_nv_cyl.setText(GET_NV_CYCLE_LEFT);
        _edt_left_nv_axis.setText(GET_NV_AXIS_LEFT);
        _edt_left_ipd.setText(GET_IPD_LEFT);

        _edt_followup_date.setText(FOLLOWUP_DATE);
        _edt_diagnosis_deatils.setText(DIAGNOSIS_DETAILS);
        _edt_treatment_details.setText(TREATMENT_DETAILS);

        if((CONSULTATION_FEES.equals("null")) || (CONSULTATION_FEES.length() <=0)) {
            _edt_consultency_fee.setText("");
        }
        else {
            _edt_consultency_fee.setText(CONSULTATION_FEES);
        }

    }

    DatePickerDialog.OnDateSetListener dateSel = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Calendar dareSelected = Calendar.getInstance();
            dareSelected.set(year, monthOfYear, dayOfMonth);
            Calendar currentDate = Calendar.getInstance();

            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            _edt_followup_date.setText(sdf.format(dareSelected.getTime()));

            String outFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf_out = new SimpleDateFormat(outFormat, Locale.US);
            get_followup_date = sdf_out.format(myCalendar.getTime());
            // Toast.makeText(getActivity(), sdf_out.format(dareSelected.getTime()), Toast.LENGTH_SHORT).show();
        }
    };

    private void preparePrescription(List<FrequentPrescription> freqPrescriptionsListArraylist) {
        final Dialog dialog = new Dialog(EditOphthalVisitDetailsActivity.this, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_prescription);
        dialog.setTitle("Add Prescriptions");
        dialog.setCanceledOnTouchOutside(true);

        final SearchView prescription_searchView = (SearchView) dialog.findViewById(R.id.searchview_prescription);
        prescription_listview = (ListView) dialog.findViewById(R.id.prescription_list);
        final CustomTextViewBold close_btn = (CustomTextViewBold)  dialog.findViewById(R.id.prescription_close);
        final ImageView add_prescription = (ImageView)  dialog.findViewById(R.id.prescription_add);


        add_prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(add_prescription.getWindowToken(), 0);

                if(prescription_searchView.getQuery().toString().trim().equals("")) {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this, " Enter Prescription Trade Name !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this,prescription_searchView.getQuery().toString()+ " is added to the list", Toast.LENGTH_SHORT).show();

                    PATIENT_PRESCRIPTION_ARRAY.add(new FrequentPrescription(0,0,prescription_searchView.getQuery().toString(),0,"","","","",USER_ID,1,0,USER_ID,USER_LOGIN_TYPE));

                }

                prescription_searchView.setQuery("", false);
                prescription_searchView.clearFocus();
                prescription_searchView.setIconified(true);
            }
        });

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(close_btn.getWindowToken(), 0);

                dialog.dismiss();
                refreshPrescriptions();
            }
        });

        prescription_searchView.setIconifiedByDefault(false);
        prescription_searchView.setQueryHint("Search Prescription Here");
        prescription_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                prescription_searchView.setQuery("", false);
                prescription_searchView.clearFocus();
                prescription_searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Utils.TAG, "SERACH: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    Log.d(Utils.TAG, "is empty " );
                } else {
                    new loadPrescriptioncTask().execute(newText);
                }
                return true;
            }
        });

        prescription_searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                prescription_searchView.setQuery("", false);
                prescription_searchView.setIconified(false);
                prescription_searchView.clearFocus();
                return false;
            }
        });

        dialog.show();
    }

    private void refreshPrescriptions() {
        Log.d(Utils.TAG, "PATIENT_PRESCRIPTION_ARRAY: " + PATIENT_PRESCRIPTION_ARRAY.size());

        prescription_header.setVisibility(View.VISIBLE);
        prescriptionAdapter = new PrescriptionAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_PRESCRIPTION_ARRAY);
        prescriptionRecyclerview.setAdapter(prescriptionAdapter);
        prescriptionAdapter.notifyDataSetChanged();
    }

    private void prepareTreatments(List<Treatments> treatmentListArraylist) {
        final Dialog dialog = new Dialog(EditOphthalVisitDetailsActivity.this, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_treatments);
        dialog.setTitle("Add Treatments");
        dialog.setCanceledOnTouchOutside(true);

        final SearchView treatment_searchView = (SearchView) dialog.findViewById(R.id.searchview_treatment);
        ListView treatment_listview = (ListView) dialog.findViewById(R.id.treatment_list);
        final ImageView add_treatment = (ImageView) dialog.findViewById(R.id.treatment_add);
        final CustomTextViewBold close_btn = (CustomTextViewBold)  dialog.findViewById(R.id.treatment_close);

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(close_btn.getWindowToken(), 0);

                refreshTreatments();
            }
        });

        add_treatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(add_treatment.getWindowToken(), 0);

                if(treatment_searchView.getQuery().toString().trim().equals("")) {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this, " Enter treatment !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this,treatment_searchView.getQuery().toString()+ " is added to the list", Toast.LENGTH_SHORT).show();

                    PATIENT_TREATMENT_ARRAY.add(new Treatments(0,treatment_searchView.getQuery().toString(),
                            USER_ID,1,0,USER_ID, USER_LOGIN_TYPE));

                }

                treatment_searchView.setQuery("", false);
                treatment_searchView.clearFocus();
                treatment_searchView.setIconified(true);

            }
        });

        final CustomTreatmentFilterAdapter treatAdapter = new CustomTreatmentFilterAdapter(EditOphthalVisitDetailsActivity.this,
                R.layout.activity_listview, treatmentListArraylist);
        treatment_listview.setAdapter(treatAdapter);
        treatment_listview.setTextFilterEnabled(true);

        treatment_searchView.setIconifiedByDefault(false);
        treatment_searchView.setQueryHint("Search Examinations Here");
        treatment_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                treatment_searchView.setQuery("", false);
                treatment_searchView.clearFocus();
                treatment_searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Utils.TAG, "SERACH: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    Log.d(Utils.TAG, "is empty " );
                    treatAdapter.filter(newText.toString());
                    // specialization_listview.clearTextFilter();
                } else {
                    //  specialization_listview.setFilterText(newText.toString());
                    treatAdapter.filter(newText.toString());
                }
                return true;
            }
        });

        treatment_searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                treatment_searchView.setQuery("", false);
                treatment_searchView.setIconified(false);
                treatment_searchView.clearFocus();
                return false;
            }
        });

        treatment_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Treatments myCustomAdapterItem = (Treatments) parent.getItemAtPosition(position);
              /* Toast.makeText(getActivity(),  myCustomAdapterItem.getSymptomsName() + " added.",
                        Toast.LENGTH_SHORT)
                        .show();*/
                treatment_searchView.setQuery("", false);
                treatment_searchView.setIconified(false);
                treatment_searchView.clearFocus();

                Toast.makeText(EditOphthalVisitDetailsActivity.this,myCustomAdapterItem.getTreatmentName()+ " is added to the list", Toast.LENGTH_SHORT).show();

                PATIENT_TREATMENT_ARRAY.add(new Treatments(myCustomAdapterItem.getTreatmentID(),myCustomAdapterItem.getTreatmentName(),
                        myCustomAdapterItem.getTreatmentDocID(),myCustomAdapterItem.getTreatmentDocType(),myCustomAdapterItem.getTreatmentFreqCount(),USER_ID, USER_LOGIN_TYPE));

            }
        });
        dialog.show();
    }

    private void refreshTreatments() {
        Log.d(Utils.TAG, "PATIENT_TREATMENT_ARRAY: " + PATIENT_TREATMENT_ARRAY.size());

        treatmentAdapter = new TreatmentsAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_TREATMENT_ARRAY);
        treatmentRecyclerview.setAdapter(treatmentAdapter);
        treatmentAdapter.notifyDataSetChanged();
    }

    private void prepareDiagnosis(List<Diagnosis> freqDiagnosisListArraylist) {
        final Dialog dialog = new Dialog(EditOphthalVisitDetailsActivity.this, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_diagnosis);
        dialog.setTitle("Add Diagnosis");
        dialog.setCanceledOnTouchOutside(true);

        final SearchView diagnosis_searchView = (SearchView) dialog.findViewById(R.id.searchview_diagnosis);
        diagnosis_listview = (ListView) dialog.findViewById(R.id.diagnosis_list);
        final CustomTextViewBold close_btn = (CustomTextViewBold)  dialog.findViewById(R.id.diagnosis_close);

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(close_btn.getWindowToken(), 0);

                refreshDiagnosis();
            }
        });

        diagnosis_searchView.setIconifiedByDefault(false);
        diagnosis_searchView.setQueryHint("Search Diagnosis Here");
        diagnosis_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                diagnosis_searchView.setQuery("", false);
                diagnosis_searchView.clearFocus();
                diagnosis_searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Utils.TAG, "SERACH: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    Log.d(Utils.TAG, "is empty " );
                } else {
                    new loadDiagnosiscTask().execute(newText);
                }
                return true;
            }
        });

        diagnosis_searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                diagnosis_searchView.setQuery("", false);
                diagnosis_searchView.setIconified(false);
                diagnosis_searchView.clearFocus();
                return false;
            }
        });

        dialog.show();
    }

    private void refreshDiagnosis() {

        Log.d(Utils.TAG, "PATIENT_DAIGNOSIS_ARRAY: " + PATIENT_DAIGNOSIS_ARRAY.size());

        diagnosisAdapter = new DiagnosisAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_DAIGNOSIS_ARRAY);
        diagnosisRecyclerview.setAdapter(diagnosisAdapter);
        diagnosisAdapter.notifyDataSetChanged();
    }

    private void prepareInvestigationTest(final List<Investigations> investigationsListArraylist) {
        Log.d(Utils.TAG, "investigationsListArraylist: " + investigationsListArraylist.size());

        final Dialog dialog = new Dialog(EditOphthalVisitDetailsActivity.this, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_investigation_test);
        dialog.setTitle("Add Investigations");
        dialog.setCanceledOnTouchOutside(true);

        final SearchView investigation_searchView = (SearchView) dialog.findViewById(R.id.searchview_investigation);
        ListView investigation_listview = (ListView) dialog.findViewById(R.id.investigation_list);
        final CustomTextViewBold close_btn_invest = (CustomTextViewBold)  dialog.findViewById(R.id.investigation_close);
        final ImageView add_investigation = (ImageView) dialog.findViewById(R.id.investigation_add);

        close_btn_invest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(close_btn_invest.getWindowToken(), 0);

                refreshInvestigationTable();
            }
        });

        add_investigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(add_investigation.getWindowToken(), 0);

                if(investigation_searchView.getQuery().toString().trim().equals("")) {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this, " Enter Investigation !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this,investigation_searchView.getQuery().toString()+ " is added to the list", Toast.LENGTH_SHORT).show();

                    PATIENT_INVESTIGATION_OTHER_ARRAY.add(new Investigations(0, "","",
                            investigation_searchView.getQuery().toString(),"","","","",5,
                            USER_ID,USER_LOGIN_TYPE));
                }

                investigation_searchView.setQuery("", false);
                investigation_searchView.clearFocus();
                investigation_searchView.setIconified(true);

            }
        });

        final CustomInvestigationFilterAdapter investAdapter = new CustomInvestigationFilterAdapter(EditOphthalVisitDetailsActivity.this,
                R.layout.activity_listview, investigationsListArraylist);
        investigation_listview.setAdapter(investAdapter);
        investigation_listview.setTextFilterEnabled(true);

        investigation_searchView.setIconifiedByDefault(false);
        investigation_searchView.setQueryHint("Search Medical Complaint Here");
        investigation_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                investigation_searchView.setQuery("", false);
                investigation_searchView.clearFocus();
                investigation_searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Utils.TAG, "SERACH: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    Log.d(Utils.TAG, "is empty " );
                    investAdapter.filter(newText.toString());
                    // specialization_listview.clearTextFilter();
                } else {
                    //  specialization_listview.setFilterText(newText.toString());
                    investAdapter.filter(newText.toString());
                }
                return true;
            }
        });

        investigation_searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                investigation_searchView.setQuery("", false);
                investigation_searchView.setIconified(false);
                investigation_searchView.clearFocus();
                return false;
            }
        });

        investigation_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Investigations myCustomAdapterItem = (Investigations) parent.getItemAtPosition(position);
              /* Toast.makeText(getActivity(),  myCustomAdapterItem.getSymptomsName() + " added.",
                        Toast.LENGTH_SHORT)
                        .show();*/
                investigation_searchView.setQuery("", false);
                investigation_searchView.setIconified(false);
                investigation_searchView.clearFocus();
                String selectedName = "";
                if(myCustomAdapterItem.getGroupTest().equals("Y")) {
                    //  Toast.makeText(getActivity(), "Group Test - YES", Toast.LENGTH_SHORT).show();

                    for(int i=0; i<grouptestListArraylist.size();i++) {
                        if(myCustomAdapterItem.getTestId().equals(grouptestListArraylist.get(i).getTestId())) {
                            //  Log.d(Utils.TAG, " SUBTEST ID: "+grouptestListArraylist.get(i).getSubTestId() );

                            for(int j=0; j<investigationsListArraylist.size();j++) {
                                if(grouptestListArraylist.get(i).getSubTestId().equals(investigationsListArraylist.get(j).getTestId())) {
                                    Log.d(Utils.TAG, " GROUPTEST ID: "+myCustomAdapterItem.getTestId()+" MAINTEST ID: "+grouptestListArraylist.get(i).getSubTestId()+" TEST ID: "+myCustomAdapterItem.getInvestigationId()+" TEST NAME: "+investigationsListArraylist.get(j).getTestName() + " dept: "+investigationsListArraylist.get(j).getInvestDepartment() );
                                    selectedName = investigationsListArraylist.get(j).getTestName();
//                                    PATIENT_INVESTIGATION_ARRAY.add(new Investigations(myCustomAdapterItem.getInvestigationId(), grouptestListArraylist.get(i).getSubTestId(),myCustomAdapterItem.getTestId(),
//                                            investigationsListArraylist.get(j).getTestName(),investigationsListArraylist.get(j).getNormalRange(),"","","",investigationsListArraylist.get(j).getInvestDepartment()));

                                    if(investigationsListArraylist.get(j).getInvestDepartment() ==  1) {
                                        PATIENT_INVESTIGATION_GENERAL_ARRAY.add(new Investigations(myCustomAdapterItem.getInvestigationId(), grouptestListArraylist.get(i).getSubTestId(),myCustomAdapterItem.getTestId(),
                                                investigationsListArraylist.get(j).getTestName(),investigationsListArraylist.get(j).getNormalRange(),"","","",investigationsListArraylist.get(j).getInvestDepartment(),
                                                investigationsListArraylist.get(j).getUserId(),investigationsListArraylist.get(j).getLoginType()));
                                    }
                                    else  if(investigationsListArraylist.get(j).getInvestDepartment() == 2) {
                                        PATIENT_INVESTIGATION_OPHTHAL_ARRAY.add(new Investigations(myCustomAdapterItem.getInvestigationId(), grouptestListArraylist.get(i).getSubTestId(),myCustomAdapterItem.getTestId(),
                                                investigationsListArraylist.get(j).getTestName(),investigationsListArraylist.get(j).getNormalRange(),"","","",investigationsListArraylist.get(j).getInvestDepartment(),
                                                investigationsListArraylist.get(j).getUserId(),investigationsListArraylist.get(j).getLoginType()));

                                    }
                                    else  if(investigationsListArraylist.get(j).getInvestDepartment() == 3) {
                                        PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.add(new Investigations(myCustomAdapterItem.getInvestigationId(), grouptestListArraylist.get(i).getSubTestId(),myCustomAdapterItem.getTestId(),
                                                investigationsListArraylist.get(j).getTestName(),investigationsListArraylist.get(j).getNormalRange(),"","","",investigationsListArraylist.get(j).getInvestDepartment(),
                                                investigationsListArraylist.get(j).getUserId(),investigationsListArraylist.get(j).getLoginType()));

                                    }
                                    else  if(investigationsListArraylist.get(j).getInvestDepartment() == 4) {
                                        PATIENT_INVESTIGATION_CLINICAL_ARRAY.add(new Investigations(myCustomAdapterItem.getInvestigationId(), grouptestListArraylist.get(i).getSubTestId(),myCustomAdapterItem.getTestId(),
                                                investigationsListArraylist.get(j).getTestName(),investigationsListArraylist.get(j).getNormalRange(),"","","",investigationsListArraylist.get(j).getInvestDepartment(),
                                                investigationsListArraylist.get(j).getUserId(),investigationsListArraylist.get(j).getLoginType()));

                                    }
                                    else  if(investigationsListArraylist.get(j).getInvestDepartment() == 5) {
                                        PATIENT_INVESTIGATION_OTHER_ARRAY.add(new Investigations(myCustomAdapterItem.getInvestigationId(), grouptestListArraylist.get(i).getSubTestId(),myCustomAdapterItem.getTestId(),
                                                investigationsListArraylist.get(j).getTestName(),investigationsListArraylist.get(j).getNormalRange(),"","","",investigationsListArraylist.get(j).getInvestDepartment(),
                                                investigationsListArraylist.get(j).getUserId(),investigationsListArraylist.get(j).getLoginType()));

                                    }
                                }
                            }
                        }

                    }
                }
                else {
                    //   Toast.makeText(getActivity(), "Group Test - NO", Toast.LENGTH_SHORT).show();
                    // Log.d(Utils.TAG, " SUBTEST ID - NOT GROUP: "+myCustomAdapterItem.getTestId() );
                    Log.d(Utils.TAG, " GROUPTEST ID: "+myCustomAdapterItem.getTestId()+" MAINTEST ID: "+myCustomAdapterItem.getTestId()+" TEST ID: "+myCustomAdapterItem.getInvestigationId()+" TEST NAME: "+myCustomAdapterItem.getTestName() + " dept: "+myCustomAdapterItem.getInvestDepartment() );
                    selectedName = myCustomAdapterItem.getTestName();
//                    PATIENT_INVESTIGATION_ARRAY.add(new Investigations(myCustomAdapterItem.getInvestigationId(),myCustomAdapterItem.getTestId(),myCustomAdapterItem.getTestId(),
//                            myCustomAdapterItem.getTestName(),myCustomAdapterItem.getNormalRange(),"","","",myCustomAdapterItem.getInvestDepartment()));

                    if(myCustomAdapterItem.getInvestDepartment() ==  1) {
                        PATIENT_INVESTIGATION_GENERAL_ARRAY.add(new Investigations(myCustomAdapterItem.getInvestigationId(),myCustomAdapterItem.getTestId(),myCustomAdapterItem.getTestId(),
                                myCustomAdapterItem.getTestName(),myCustomAdapterItem.getNormalRange(),"","","",myCustomAdapterItem.getInvestDepartment(),myCustomAdapterItem.getUserId(),myCustomAdapterItem.getLoginType()));
                    }
                    else  if(myCustomAdapterItem.getInvestDepartment() == 2) {
                        PATIENT_INVESTIGATION_OPHTHAL_ARRAY.add(new Investigations(myCustomAdapterItem.getInvestigationId(),myCustomAdapterItem.getTestId(),myCustomAdapterItem.getTestId(),
                                myCustomAdapterItem.getTestName(),myCustomAdapterItem.getNormalRange(),"","","",myCustomAdapterItem.getInvestDepartment(),myCustomAdapterItem.getUserId(),myCustomAdapterItem.getLoginType()));
                    }
                    else  if(myCustomAdapterItem.getInvestDepartment() == 3) {
                        PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.add(new Investigations(myCustomAdapterItem.getInvestigationId(),myCustomAdapterItem.getTestId(),myCustomAdapterItem.getTestId(),
                                myCustomAdapterItem.getTestName(),myCustomAdapterItem.getNormalRange(),"","","",myCustomAdapterItem.getInvestDepartment(),myCustomAdapterItem.getUserId(),myCustomAdapterItem.getLoginType()));
                    }
                    else  if(myCustomAdapterItem.getInvestDepartment() == 4) {
                        PATIENT_INVESTIGATION_CLINICAL_ARRAY.add(new Investigations(myCustomAdapterItem.getInvestigationId(),myCustomAdapterItem.getTestId(),myCustomAdapterItem.getTestId(),
                                myCustomAdapterItem.getTestName(),myCustomAdapterItem.getNormalRange(),"","","",myCustomAdapterItem.getInvestDepartment(),myCustomAdapterItem.getUserId(),myCustomAdapterItem.getLoginType()));
                    }
                    else  if(myCustomAdapterItem.getInvestDepartment() == 5) {
                        PATIENT_INVESTIGATION_OTHER_ARRAY.add(new Investigations(myCustomAdapterItem.getInvestigationId(),myCustomAdapterItem.getTestId(),myCustomAdapterItem.getTestId(),
                                myCustomAdapterItem.getTestName(),myCustomAdapterItem.getNormalRange(),"","","",myCustomAdapterItem.getInvestDepartment(),myCustomAdapterItem.getUserId(),myCustomAdapterItem.getLoginType()));
                    }
                }

                Toast.makeText(EditOphthalVisitDetailsActivity.this,selectedName + " is added to the list", Toast.LENGTH_SHORT).show();

            }
        });
        dialog.show();
    }

    private void refreshInvestigationTable() {

        if(PATIENT_INVESTIGATION_GENERAL_ARRAY.size() > 0){
            Log.d(Utils.TAG, " InvestSize: "+PATIENT_INVESTIGATION_GENERAL_ARRAY.size() );
            investigation_tab_header.setVisibility(View.VISIBLE);
            investigationAdapter = new InvestigationAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_INVESTIGATION_GENERAL_ARRAY);
            investigationRecyclerview.setAdapter(investigationAdapter);
            investigationAdapter.notifyDataSetChanged();
        }

        if(PATIENT_INVESTIGATION_OPHTHAL_ARRAY.size() > 0) {
            Log.d(Utils.TAG, " InvestSize: "+PATIENT_INVESTIGATION_OPHTHAL_ARRAY.size() );
            investigation_ophthal_tab_header.setVisibility(View.VISIBLE);
            investigationOphthalAdapter = new InvestigationOphthalAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_INVESTIGATION_OPHTHAL_ARRAY);
            investigationOphthalRecyclerview.setAdapter(investigationOphthalAdapter);
            investigationOphthalAdapter.notifyDataSetChanged();
        }

        if(PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.size() > 0) {
            Log.d(Utils.TAG, " InvestRadSize: "+PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.size() );
            investigation_radiology_tab_header.setVisibility(View.VISIBLE);
            investigationRadiologyAdapter = new InvestigationRadiologyAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_INVESTIGATION_RADIOLOGY_ARRAY);
            investigationRadiologyRecyclerview.setAdapter(investigationRadiologyAdapter);
            investigationRadiologyAdapter.notifyDataSetChanged();
        }

        if(PATIENT_INVESTIGATION_CLINICAL_ARRAY.size() > 0) {
            Log.d(Utils.TAG, " InvestClinicSize: "+PATIENT_INVESTIGATION_CLINICAL_ARRAY.size() );
            investigation_clinical_tab_header.setVisibility(View.VISIBLE);
            investigationClinicalAdapter = new InvestigationClinicalAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_INVESTIGATION_CLINICAL_ARRAY);
            investigationClinicalRecyclerview.setAdapter(investigationClinicalAdapter);
            investigationClinicalAdapter.notifyDataSetChanged();
        }

        if(PATIENT_INVESTIGATION_OTHER_ARRAY.size() > 0) {
            Log.d(Utils.TAG, " InvestOtherSize: "+PATIENT_INVESTIGATION_OTHER_ARRAY.size() );
            investigation_oother_tab_header.setVisibility(View.VISIBLE);
            investigationOtherAdapter = new InvestigationOtherAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_INVESTIGATION_OTHER_ARRAY);
            investigationOtherRecyclerview.setAdapter(investigationOtherAdapter);
            investigationOtherAdapter.notifyDataSetChanged();
        }
    }

    private void prepareFundusCheckBoxes(final List<OphthalFundus> fundusListArraylist) {
        for(int c=0; c<fundusListArraylist.size(); c++){
            final int finalC = c;

            // RIGHT EYE FUNDUS Check Boxes
            CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chk.setId(fundusListArraylist.get(c).getFundusId());
            chk.setText(fundusListArraylist.get(c).getFundusName());
            chk.setTextColor(getResources().getColor(R.color.textPrimary));
            chk.setPadding(0,10,0,5);
            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int fundus_id = buttonView.getId();
                    String  fundus_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " fundus_id: "+ fundus_id+" fundus_name: "+ fundus_name+ " finalC:"+finalC);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_FUNDUS_ARRAY.add(new OphthalFundus(fundusListArraylist.get(finalC).getFundusId(),fundusListArraylist.get(finalC).getFundusName(),
                                fundusListArraylist.get(finalC).getDocId(),fundusListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_FUNDUS_ARRAY.remove(new OphthalFundus(fundusListArraylist.get(finalC).getFundusId(),fundusListArraylist.get(finalC).getFundusName(),
                                fundusListArraylist.get(finalC).getDocId(),fundusListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_FUNDUS_ARRAY.size(); j++){
                            if(PATIENT_FUNDUS_ARRAY.get(j).getFundusId() == fundusListArraylist.get(finalC).getFundusId() && PATIENT_FUNDUS_ARRAY.get(j).getFundusName().equals(fundusListArraylist.get(finalC).getFundusName())) {
                                PATIENT_FUNDUS_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            fundus_gridview_RE.addView(chk);

            // LEFT EYE FUNDUS Check Boxes
            CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chkLE.setId(fundusListArraylist.get(c).getFundusId());
            chkLE.setText(fundusListArraylist.get(c).getFundusName());
            chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
            chkLE.setPadding(0,10,0,5);
            chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int fundus_id = buttonView.getId();
                    String fundus_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " fundus_id: "+ fundus_id+" fundus_name: "+ fundus_name);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_FUNDUS_ARRAY.add(new OphthalFundus(fundusListArraylist.get(finalC).getFundusId(),fundusListArraylist.get(finalC).getFundusName(),
                                fundusListArraylist.get(finalC).getDocId(),fundusListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_FUNDUS_ARRAY.remove(new OphthalFundus(fundusListArraylist.get(finalC).getFundusId(),fundusListArraylist.get(finalC).getFundusName(),
                                fundusListArraylist.get(finalC).getDocId(),fundusListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_FUNDUS_ARRAY.size(); j++){
                            if(PATIENT_FUNDUS_ARRAY.get(j).getFundusId() == fundusListArraylist.get(finalC).getFundusId() && PATIENT_FUNDUS_ARRAY.get(j).getFundusName().equals(fundusListArraylist.get(finalC).getFundusName())) {
                                PATIENT_FUNDUS_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            fundus_gridview_LE.addView(chkLE);

            if (PATIENT_FUNDUS_INIT_ARRAY != null && PATIENT_FUNDUS_INIT_ARRAY.size() > 0) {
                for(int j=0; j<PATIENT_FUNDUS_INIT_ARRAY.size(); j++) {
                    if(PATIENT_FUNDUS_INIT_ARRAY.get(j).getRightEye().equals("1") && fundusListArraylist.get(c).getFundusId() == PATIENT_FUNDUS_INIT_ARRAY.get(j).getFundusId()) {
                        chk.setChecked(true);
                    }
                    else if(PATIENT_FUNDUS_INIT_ARRAY.get(j).getRightEye().equals("2") && fundusListArraylist.get(c).getFundusId() == PATIENT_FUNDUS_INIT_ARRAY.get(j).getFundusId()) {
                        chkLE.setChecked(true);
                    }
                }
            }
        }
    }

    private void prepareViterousCheckBoxes(final List<OphthalViterous> viterousListArraylist) {
        for(int c=0; c<viterousListArraylist.size(); c++){
            final int finalC = c;

            // RIGHT EYE VITEROUS Check Boxes
            CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chk.setId(viterousListArraylist.get(c).getViterousId());
            chk.setText(viterousListArraylist.get(c).getViterousName());
            chk.setTextColor(getResources().getColor(R.color.textPrimary));
            chk.setPadding(0,10,0,5);
            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int viterous_id = buttonView.getId();
                    String  viterous_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " viterous_id: "+ viterous_id+" viterous_name: "+ viterous_name+ " finalC:"+finalC);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_VITEROUS_ARRAY.add(new OphthalViterous(viterousListArraylist.get(finalC).getViterousId(),viterousListArraylist.get(finalC).getViterousName(),
                                viterousListArraylist.get(finalC).getDocId(),viterousListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_VITEROUS_ARRAY.remove(new OphthalViterous(viterousListArraylist.get(finalC).getViterousId(),viterousListArraylist.get(finalC).getViterousName(),
                                viterousListArraylist.get(finalC).getDocId(),viterousListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_VITEROUS_ARRAY.size(); j++){
                            if(PATIENT_VITEROUS_ARRAY.get(j).getViterousId() == viterousListArraylist.get(finalC).getViterousId() && PATIENT_VITEROUS_ARRAY.get(j).getViterousName().equals(viterousListArraylist.get(finalC).getViterousName())) {
                                PATIENT_VITEROUS_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            viterous_gridview_RE.addView(chk);

            // LEFT EYE VITEROUS Check Boxes
            CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chkLE.setId(viterousListArraylist.get(c).getViterousId());
            chkLE.setText(viterousListArraylist.get(c).getViterousName());
            chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
            chkLE.setPadding(0,10,0,5);
            chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int viterous_id = buttonView.getId();
                    String viterous_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " viterous_id: "+ viterous_id+" viterous_name: "+ viterous_name);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_VITEROUS_ARRAY.add(new OphthalViterous(viterousListArraylist.get(finalC).getViterousId(),viterousListArraylist.get(finalC).getViterousName(),
                                viterousListArraylist.get(finalC).getDocId(),viterousListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_VITEROUS_ARRAY.remove(new OphthalViterous(viterousListArraylist.get(finalC).getViterousId(),viterousListArraylist.get(finalC).getViterousName(),
                                viterousListArraylist.get(finalC).getDocId(),viterousListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_VITEROUS_ARRAY.size(); j++){
                            if(PATIENT_VITEROUS_ARRAY.get(j).getViterousId() == viterousListArraylist.get(finalC).getViterousId() && PATIENT_VITEROUS_ARRAY.get(j).getViterousName().equals(viterousListArraylist.get(finalC).getViterousName())) {
                                PATIENT_VITEROUS_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            viterous_gridview_LE.addView(chkLE);

            if (PATIENT_VITEROUS_INIT_ARRAY != null && PATIENT_VITEROUS_INIT_ARRAY.size() > 0) {
                for(int j=0; j<PATIENT_VITEROUS_INIT_ARRAY.size(); j++) {
                    if(PATIENT_VITEROUS_INIT_ARRAY.get(j).getRightEye().equals("1") && viterousListArraylist.get(c).getViterousId() == PATIENT_VITEROUS_INIT_ARRAY.get(j).getViterousId()) {
                        chk.setChecked(true);
                    }
                    else if(PATIENT_VITEROUS_INIT_ARRAY.get(j).getRightEye().equals("2") && viterousListArraylist.get(c).getViterousId() == PATIENT_VITEROUS_INIT_ARRAY.get(j).getViterousId()) {
                        chkLE.setChecked(true);
                    }
                }
            }
        }
    }

    private void prepareLensCheckBoxes(final List<OphthalLens> lensListArraylist) {
        for(int c=0; c<lensListArraylist.size(); c++){
            final int finalC = c;

            // RIGHT EYE LENS Check Boxes
            CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chk.setId(lensListArraylist.get(c).getLensId());
            chk.setText(lensListArraylist.get(c).getLensName());
            chk.setTextColor(getResources().getColor(R.color.textPrimary));
            chk.setPadding(0,10,0,5);
            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int lens_id = buttonView.getId();
                    String  lens_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " lens_id: "+ lens_id+" lens_name: "+ lens_name+ " finalC:"+finalC);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_LENS_ARRAY.add(new OphthalLens(lensListArraylist.get(finalC).getLensId(),lensListArraylist.get(finalC).getLensName(),
                                lensListArraylist.get(finalC).getDocId(),lensListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_LENS_ARRAY.remove(new OphthalLens(lensListArraylist.get(finalC).getLensId(),lensListArraylist.get(finalC).getLensName(),
                                lensListArraylist.get(finalC).getDocId(),lensListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_LENS_ARRAY.size(); j++){
                            if(PATIENT_LENS_ARRAY.get(j).getLensId() == lensListArraylist.get(finalC).getLensId() && PATIENT_LENS_ARRAY.get(j).getLensName().equals(lensListArraylist.get(finalC).getLensName())) {
                                PATIENT_LENS_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            lens_gridview_RE.addView(chk);

            // LEFT EYE LENS Check Boxes
            CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chkLE.setId(lensListArraylist.get(c).getLensId());
            chkLE.setText(lensListArraylist.get(c).getLensName());
            chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
            chkLE.setPadding(0,10,0,5);
            chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int lens_id = buttonView.getId();
                    String lens_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " lens_id: "+ lens_id+" lens_name: "+ lens_name);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_LENS_ARRAY.add(new OphthalLens(lensListArraylist.get(finalC).getLensId(),lensListArraylist.get(finalC).getLensName(),
                                lensListArraylist.get(finalC).getDocId(),lensListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_LENS_ARRAY.remove(new OphthalLens(lensListArraylist.get(finalC).getLensId(),lensListArraylist.get(finalC).getLensName(),
                                lensListArraylist.get(finalC).getDocId(),lensListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_LENS_ARRAY.size(); j++){
                            if(PATIENT_LENS_ARRAY.get(j).getLensId() == lensListArraylist.get(finalC).getLensId() && PATIENT_LENS_ARRAY.get(j).getLensName().equals(lensListArraylist.get(finalC).getLensName())) {
                                PATIENT_LENS_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            lens_gridview_LE.addView(chkLE);

            if (PATIENT_LENS_INIT_ARRAY != null && PATIENT_LENS_INIT_ARRAY.size() > 0) {
                for(int j=0; j<PATIENT_LENS_INIT_ARRAY.size(); j++) {
                    if(PATIENT_LENS_INIT_ARRAY.get(j).getRightEye().equals("1") && lensListArraylist.get(c).getLensId() == PATIENT_LENS_INIT_ARRAY.get(j).getLensId()) {
                        chk.setChecked(true);
                    }
                    else if(PATIENT_LENS_INIT_ARRAY.get(j).getRightEye().equals("2") && lensListArraylist.get(c).getLensId() == PATIENT_LENS_INIT_ARRAY.get(j).getLensId()) {
                        chkLE.setChecked(true);
                    }
                }
            }
        }
    }

    private void prepareAngleCheckBoxes(final List<OphthalAngleAnteriorChamber> angleListArraylist) {
        for(int c=0; c<angleListArraylist.size(); c++){
            final int finalC = c;

            // RIGHT EYE ANGLE OF ANTERIOR CHAMBER Check Boxes
            CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chk.setId(angleListArraylist.get(c).getAngleId());
            chk.setText(angleListArraylist.get(c).getAngleName());
            chk.setTextColor(getResources().getColor(R.color.textPrimary));
            chk.setPadding(0,10,0,5);
            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int angle_id = buttonView.getId();
                    String  angle_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " angle_id: "+ angle_id+" angle_name: "+ angle_name+ " finalC:"+finalC);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_ANGLE_ARRAY.add(new OphthalAngleAnteriorChamber(angleListArraylist.get(finalC).getAngleId(),angleListArraylist.get(finalC).getAngleName(),
                                angleListArraylist.get(finalC).getDocId(),angleListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_ANGLE_ARRAY.remove(new OphthalAngleAnteriorChamber(angleListArraylist.get(finalC).getAngleId(),angleListArraylist.get(finalC).getAngleName(),
                                angleListArraylist.get(finalC).getDocId(),angleListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_ANGLE_ARRAY.size(); j++){
                            if(PATIENT_ANGLE_ARRAY.get(j).getAngleId() == angleListArraylist.get(finalC).getAngleId() && PATIENT_ANGLE_ARRAY.get(j).getAngleName().equals(angleListArraylist.get(finalC).getAngleName())) {
                                PATIENT_ANGLE_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            angle_gridview_RE.addView(chk);

            // LEFT EYE ANGLE OF ANTERIOR CHAMBER Check Boxes
            CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chkLE.setId(angleListArraylist.get(c).getAngleId());
            chkLE.setText(angleListArraylist.get(c).getAngleName());
            chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
            chkLE.setPadding(0,10,0,5);
            chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int angle_id = buttonView.getId();
                    String angle_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " angle_id: "+ angle_id+" angle_name: "+ angle_name);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_ANGLE_ARRAY.add(new OphthalAngleAnteriorChamber(angleListArraylist.get(finalC).getAngleId(),angleListArraylist.get(finalC).getAngleName(),
                                angleListArraylist.get(finalC).getDocId(),angleListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_ANGLE_ARRAY.remove(new OphthalAngleAnteriorChamber(angleListArraylist.get(finalC).getAngleId(),angleListArraylist.get(finalC).getAngleName(),
                                angleListArraylist.get(finalC).getDocId(),angleListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_ANGLE_ARRAY.size(); j++){
                            if(PATIENT_ANGLE_ARRAY.get(j).getAngleId() == angleListArraylist.get(finalC).getAngleId() && PATIENT_ANGLE_ARRAY.get(j).getAngleName().equals(angleListArraylist.get(finalC).getAngleName())) {
                                PATIENT_ANGLE_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            angle_gridview_LE.addView(chkLE);

            if (PATIENT_ANGLE_INIT_ARRAY != null && PATIENT_ANGLE_INIT_ARRAY.size() > 0) {
                for(int j=0; j<PATIENT_ANGLE_INIT_ARRAY.size(); j++) {
                    if(PATIENT_ANGLE_INIT_ARRAY.get(j).getRightEye().equals("1") && angleListArraylist.get(c).getAngleId() == PATIENT_ANGLE_INIT_ARRAY.get(j).getAngleId()) {
                        chk.setChecked(true);
                    }
                    else if(PATIENT_ANGLE_INIT_ARRAY.get(j).getRightEye().equals("2") && angleListArraylist.get(c).getAngleId() == PATIENT_ANGLE_INIT_ARRAY.get(j).getAngleId()) {
                        chkLE.setChecked(true);
                    }
                }
            }
        }
    }

    private void preparePupilCheckBoxes(final List<OphthalPupil> pupilListArraylist) {
        for(int c=0; c<pupilListArraylist.size(); c++){
            final int finalC = c;

            // RIGHT EYE PUPIL Check Boxes
            CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chk.setId(pupilListArraylist.get(c).getPupilId());
            chk.setText(pupilListArraylist.get(c).getPupilName());
            chk.setTextColor(getResources().getColor(R.color.textPrimary));
            chk.setPadding(0,10,0,5);
            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int pupil_id = buttonView.getId();
                    String  pupil_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " pupil_id: "+ pupil_id+" pupil_name: "+ pupil_name+ " finalC:"+finalC);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_PUPIL_ARRAY.add(new OphthalPupil(pupilListArraylist.get(finalC).getPupilId(),pupilListArraylist.get(finalC).getPupilName(),
                                pupilListArraylist.get(finalC).getDocId(),pupilListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_PUPIL_ARRAY.remove(new OphthalPupil(pupilListArraylist.get(finalC).getPupilId(),pupilListArraylist.get(finalC).getPupilName(),
                                pupilListArraylist.get(finalC).getDocId(),pupilListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_PUPIL_ARRAY.size(); j++){
                            if(PATIENT_PUPIL_ARRAY.get(j).getPupilId() == pupilListArraylist.get(finalC).getPupilId() && PATIENT_PUPIL_ARRAY.get(j).getPupilName().equals(pupilListArraylist.get(finalC).getPupilName())) {
                                PATIENT_PUPIL_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            pupil_gridview_RE.addView(chk);

            // LEFT EYE PUPIL Check Boxes
            CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chkLE.setId(pupilListArraylist.get(c).getPupilId());
            chkLE.setText(pupilListArraylist.get(c).getPupilName());
            chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
            chkLE.setPadding(0,10,0,5);
            chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int pupil_id = buttonView.getId();
                    String pupil_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " pupil_id: "+ pupil_id+" pupil_name: "+ pupil_name);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_PUPIL_ARRAY.add(new OphthalPupil(pupilListArraylist.get(finalC).getPupilId(),pupilListArraylist.get(finalC).getPupilName(),
                                pupilListArraylist.get(finalC).getDocId(),pupilListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_PUPIL_ARRAY.remove(new OphthalAnteriorChamber(pupilListArraylist.get(finalC).getPupilId(),pupilListArraylist.get(finalC).getPupilName(),
                                pupilListArraylist.get(finalC).getDocId(),pupilListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_PUPIL_ARRAY.size(); j++){
                            if(PATIENT_PUPIL_ARRAY.get(j).getPupilId() == pupilListArraylist.get(finalC).getPupilId() && PATIENT_PUPIL_ARRAY.get(j).getPupilName().equals(pupilListArraylist.get(finalC).getPupilName())) {
                                PATIENT_PUPIL_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            pupil_gridview_LE.addView(chkLE);

            if (PATIENT_PUPIL_INIT_ARRAY != null && PATIENT_PUPIL_INIT_ARRAY.size() > 0) {
                for(int j=0; j<PATIENT_PUPIL_INIT_ARRAY.size(); j++) {
                    if(PATIENT_PUPIL_INIT_ARRAY.get(j).getRightEye().equals("1") && pupilListArraylist.get(c).getPupilId() == PATIENT_PUPIL_INIT_ARRAY.get(j).getPupilId()) {
                        chk.setChecked(true);
                    }
                    else if(PATIENT_PUPIL_INIT_ARRAY.get(j).getRightEye().equals("2") && pupilListArraylist.get(c).getPupilId() == PATIENT_PUPIL_INIT_ARRAY.get(j).getPupilId()) {
                        chkLE.setChecked(true);
                    }
                }
            }
        }
    }

    private void prepareIrisCheckBoxes(final List<OphthalIris> irisListArraylist) {
        for(int c=0; c<irisListArraylist.size(); c++){
            final int finalC = c;

            // RIGHT EYE SCLERA Check Boxes
            CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chk.setId(irisListArraylist.get(c).getIrisId());
            chk.setText(irisListArraylist.get(c).getIrisName());
            chk.setTextColor(getResources().getColor(R.color.textPrimary));
            chk.setPadding(0,10,0,5);
            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int iris_id = buttonView.getId();
                    String  iris_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " iris_id: "+ iris_id+" iris_name: "+ iris_name+ " finalC:"+finalC);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_IRIS_ARRAY.add(new OphthalIris(irisListArraylist.get(finalC).getIrisId(),irisListArraylist.get(finalC).getIrisName(),
                                irisListArraylist.get(finalC).getDocId(),irisListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_IRIS_ARRAY.remove(new OphthalIris(irisListArraylist.get(finalC).getIrisId(),irisListArraylist.get(finalC).getIrisName(),
                                irisListArraylist.get(finalC).getDocId(),irisListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_IRIS_ARRAY.size(); j++){
                            if(PATIENT_IRIS_ARRAY.get(j).getIrisId() == irisListArraylist.get(finalC).getIrisId() && PATIENT_IRIS_ARRAY.get(j).getIrisName().equals(irisListArraylist.get(finalC).getIrisName())) {
                                PATIENT_IRIS_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            iris_gridview_RE.addView(chk);

            // LEFT EYE SCLERA Check Boxes
            CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chkLE.setId(irisListArraylist.get(c).getIrisId());
            chkLE.setText(irisListArraylist.get(c).getIrisName());
            chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
            chkLE.setPadding(0,10,0,5);
            chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int iris_id = buttonView.getId();
                    String iris_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " iris_id: "+ iris_id+" iris_name: "+ iris_name);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_IRIS_ARRAY.add(new OphthalIris(irisListArraylist.get(finalC).getIrisId(),irisListArraylist.get(finalC).getIrisName(),
                                irisListArraylist.get(finalC).getDocId(),irisListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_IRIS_ARRAY.remove(new OphthalAnteriorChamber(irisListArraylist.get(finalC).getIrisId(),irisListArraylist.get(finalC).getIrisName(),
                                irisListArraylist.get(finalC).getDocId(),irisListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_IRIS_ARRAY.size(); j++){
                            if(PATIENT_IRIS_ARRAY.get(j).getIrisId() == irisListArraylist.get(finalC).getIrisId() && PATIENT_IRIS_ARRAY.get(j).getIrisName().equals(irisListArraylist.get(finalC).getIrisName())) {
                                PATIENT_IRIS_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            iris_gridview_LE.addView(chkLE);

            if (PATIENT_IRIS_INIT_ARRAY != null && PATIENT_IRIS_INIT_ARRAY.size() > 0) {
                for(int j=0; j<PATIENT_IRIS_INIT_ARRAY.size(); j++) {
                    if(PATIENT_IRIS_INIT_ARRAY.get(j).getRightEye().equals("1") && irisListArraylist.get(c).getIrisId() == PATIENT_IRIS_INIT_ARRAY.get(j).getIrisId()) {
                        chk.setChecked(true);
                    }
                    else if(PATIENT_IRIS_INIT_ARRAY.get(j).getRightEye().equals("2") && irisListArraylist.get(c).getIrisId() == PATIENT_IRIS_INIT_ARRAY.get(j).getIrisId()) {
                        chkLE.setChecked(true);
                    }
                }
            }
        }
    }

    private void prepareAnteriorChamberCheckBoxes(final List<OphthalAnteriorChamber> anteriorChamberListArraylist) {
        for(int c=0; c<anteriorChamberListArraylist.size(); c++){
            final int finalC = c;

            // RIGHT EYE SCLERA Check Boxes
            CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chk.setId(anteriorChamberListArraylist.get(c).getAnteriorChamberId());
            chk.setText(anteriorChamberListArraylist.get(c).getAnteriorChamberName());
            chk.setTextColor(getResources().getColor(R.color.textPrimary));
            chk.setPadding(0,10,0,5);
            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int chamber_id = buttonView.getId();
                    String  chamber_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " chamber_id: "+ chamber_id+" chamber_name: "+ chamber_name+ " finalC:"+finalC);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_ANTERIOR_CHAMBER_ARRAY.add(new OphthalAnteriorChamber(anteriorChamberListArraylist.get(finalC).getAnteriorChamberId(),anteriorChamberListArraylist.get(finalC).getAnteriorChamberName(),
                                anteriorChamberListArraylist.get(finalC).getDocId(),anteriorChamberListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_ANTERIOR_CHAMBER_ARRAY.remove(new OphthalCornearPosteriorSurface(anteriorChamberListArraylist.get(finalC).getAnteriorChamberId(),anteriorChamberListArraylist.get(finalC).getAnteriorChamberName(),
                                anteriorChamberListArraylist.get(finalC).getDocId(),anteriorChamberListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_ANTERIOR_CHAMBER_ARRAY.size(); j++){
                            if(PATIENT_ANTERIOR_CHAMBER_ARRAY.get(j).getAnteriorChamberId() == anteriorChamberListArraylist.get(finalC).getAnteriorChamberId() && PATIENT_ANTERIOR_CHAMBER_ARRAY.get(j).getAnteriorChamberName().equals(anteriorChamberListArraylist.get(finalC).getAnteriorChamberName())) {
                                PATIENT_ANTERIOR_CHAMBER_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            anterior_chamber_gridview_RE.addView(chk);

            // LEFT EYE SCLERA Check Boxes
            CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chkLE.setId(anteriorChamberListArraylist.get(c).getAnteriorChamberId());
            chkLE.setText(anteriorChamberListArraylist.get(c).getAnteriorChamberName());
            chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
            chkLE.setPadding(0,10,0,5);
            chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int chamber_id = buttonView.getId();
                    String chamber_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " chamber_id: "+ chamber_id+" chamber_name: "+ chamber_name);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_ANTERIOR_CHAMBER_ARRAY.add(new OphthalAnteriorChamber(anteriorChamberListArraylist.get(finalC).getAnteriorChamberId(),anteriorChamberListArraylist.get(finalC).getAnteriorChamberName(),
                                anteriorChamberListArraylist.get(finalC).getDocId(),anteriorChamberListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_ANTERIOR_CHAMBER_ARRAY.remove(new OphthalAnteriorChamber(anteriorChamberListArraylist.get(finalC).getAnteriorChamberId(),anteriorChamberListArraylist.get(finalC).getAnteriorChamberName(),
                                anteriorChamberListArraylist.get(finalC).getDocId(),anteriorChamberListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_ANTERIOR_CHAMBER_ARRAY.size(); j++){
                            if(PATIENT_ANTERIOR_CHAMBER_ARRAY.get(j).getAnteriorChamberId() == anteriorChamberListArraylist.get(finalC).getAnteriorChamberId() && PATIENT_ANTERIOR_CHAMBER_ARRAY.get(j).getAnteriorChamberName().equals(anteriorChamberListArraylist.get(finalC).getAnteriorChamberName())) {
                                PATIENT_ANTERIOR_CHAMBER_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            anterior_chamber_gridview_LE.addView(chkLE);

            if (PATIENT_ANTERIOR_CHAMBER_INIT_ARRAY != null && PATIENT_ANTERIOR_CHAMBER_INIT_ARRAY.size() > 0) {
                for(int j=0; j<PATIENT_ANTERIOR_CHAMBER_INIT_ARRAY.size(); j++) {
                    if(PATIENT_ANTERIOR_CHAMBER_INIT_ARRAY.get(j).getRightEye().equals("1") && anteriorChamberListArraylist.get(c).getAnteriorChamberId() == PATIENT_ANTERIOR_CHAMBER_INIT_ARRAY.get(j).getAnteriorChamberId()) {
                        chk.setChecked(true);
                    }
                    else if(PATIENT_ANTERIOR_CHAMBER_INIT_ARRAY.get(j).getRightEye().equals("2") && anteriorChamberListArraylist.get(c).getAnteriorChamberId() == PATIENT_ANTERIOR_CHAMBER_INIT_ARRAY.get(j).getAnteriorChamberId()) {
                        chkLE.setChecked(true);
                    }
                }
            }
        }
    }

    private void prepareCorneaPosteriorCheckBoxes(final List<OphthalCornearPosteriorSurface> corneaPosteriorListArraylist) {
        for(int c=0; c<corneaPosteriorListArraylist.size(); c++){
            final int finalC = c;

            // RIGHT EYE SCLERA Check Boxes
            CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chk.setId(corneaPosteriorListArraylist.get(c).getCorneaPosteriorId());
            chk.setText(corneaPosteriorListArraylist.get(c).getCorneaPosteriorName());
            chk.setTextColor(getResources().getColor(R.color.textPrimary));
            chk.setPadding(0,10,0,5);
            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int post_id = buttonView.getId();
                    String  post_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " post_id: "+ post_id+" post_name: "+ post_name+ " finalC:"+finalC);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_CORNEA_POSTERIOR_ARRAY.add(new OphthalCornearPosteriorSurface(corneaPosteriorListArraylist.get(finalC).getCorneaPosteriorId(),corneaPosteriorListArraylist.get(finalC).getCorneaPosteriorName(),
                                corneaPosteriorListArraylist.get(finalC).getDocId(),corneaPosteriorListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_CORNEA_POSTERIOR_ARRAY.remove(new OphthalCornearPosteriorSurface(corneaPosteriorListArraylist.get(finalC).getCorneaPosteriorId(),corneaPosteriorListArraylist.get(finalC).getCorneaPosteriorName(),
                                corneaPosteriorListArraylist.get(finalC).getDocId(),corneaPosteriorListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_CORNEA_POSTERIOR_ARRAY.size(); j++){
                            if(PATIENT_CORNEA_POSTERIOR_ARRAY.get(j).getCorneaPosteriorId() == corneaPosteriorListArraylist.get(finalC).getCorneaPosteriorId() && PATIENT_CORNEA_POSTERIOR_ARRAY.get(j).getCorneaPosteriorName().equals(corneaPosteriorListArraylist.get(finalC).getCorneaPosteriorName())) {
                                PATIENT_CORNEA_POSTERIOR_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            cornea_posterior_gridview_RE.addView(chk);

            // LEFT EYE SCLERA Check Boxes
            CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chkLE.setId(corneaPosteriorListArraylist.get(c).getCorneaPosteriorId());
            chkLE.setText(corneaPosteriorListArraylist.get(c).getCorneaPosteriorName());
            chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
            chkLE.setPadding(0,10,0,5);
            chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int post_id = buttonView.getId();
                    String post_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " post_id: "+ post_id+" post_name: "+ post_name);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_CORNEA_POSTERIOR_ARRAY.add(new OphthalCornearPosteriorSurface(corneaPosteriorListArraylist.get(finalC).getCorneaPosteriorId(),corneaPosteriorListArraylist.get(finalC).getCorneaPosteriorName(),
                                corneaPosteriorListArraylist.get(finalC).getDocId(),corneaPosteriorListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_CORNEA_POSTERIOR_ARRAY.remove(new OphthalCornearPosteriorSurface(corneaPosteriorListArraylist.get(finalC).getCorneaPosteriorId(),corneaPosteriorListArraylist.get(finalC).getCorneaPosteriorName(),
                                corneaPosteriorListArraylist.get(finalC).getDocId(),corneaPosteriorListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_CORNEA_POSTERIOR_ARRAY.size(); j++){
                            if(PATIENT_CORNEA_POSTERIOR_ARRAY.get(j).getCorneaPosteriorId() == corneaPosteriorListArraylist.get(finalC).getCorneaPosteriorId() && PATIENT_CORNEA_POSTERIOR_ARRAY.get(j).getCorneaPosteriorName().equals(corneaPosteriorListArraylist.get(finalC).getCorneaPosteriorName())) {
                                PATIENT_CORNEA_POSTERIOR_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            cornea_posterior_gridview_LE.addView(chkLE);

            if (PATIENT_CORNEA_POSTERIOR_INIT_ARRAY != null && PATIENT_CORNEA_POSTERIOR_INIT_ARRAY.size() > 0) {
                for(int j=0; j<PATIENT_CORNEA_POSTERIOR_INIT_ARRAY.size(); j++) {
                    if(PATIENT_CORNEA_POSTERIOR_INIT_ARRAY.get(j).getRightEye().equals("1") && corneaPosteriorListArraylist.get(c).getCorneaPosteriorId() == PATIENT_CORNEA_POSTERIOR_INIT_ARRAY.get(j).getCorneaPosteriorId()) {
                        chk.setChecked(true);
                    }
                    else if(PATIENT_CORNEA_POSTERIOR_INIT_ARRAY.get(j).getRightEye().equals("2") && corneaPosteriorListArraylist.get(c).getCorneaPosteriorId() == PATIENT_CORNEA_POSTERIOR_INIT_ARRAY.get(j).getCorneaPosteriorId()) {
                        chkLE.setChecked(true);
                    }
                }
            }
        }
    }

    private void prepareCorneaAnteriorCheckBoxes(final List<OphthalCornearAnteriorSurface> corneaAnteriorListArraylist) {
        for(int c=0; c<corneaAnteriorListArraylist.size(); c++){
            final int finalC = c;

            // RIGHT EYE SCLERA Check Boxes
            CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chk.setId(corneaAnteriorListArraylist.get(c).getCorneaAnteriorId());
            chk.setText(corneaAnteriorListArraylist.get(c).getCorneaAnteriorName());
            chk.setTextColor(getResources().getColor(R.color.textPrimary));
            chk.setPadding(0,10,0,5);
            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int ant_id = buttonView.getId();
                    String  ant_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " ant_id: "+ ant_id+" ant_name: "+ ant_name+ " finalC:"+finalC);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_CORNEA_ANTERIOR_ARRAY.add(new OphthalCornearAnteriorSurface(corneaAnteriorListArraylist.get(finalC).getCorneaAnteriorId(),corneaAnteriorListArraylist.get(finalC).getCorneaAnteriorName(),
                                corneaAnteriorListArraylist.get(finalC).getDocId(),corneaAnteriorListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_CORNEA_ANTERIOR_ARRAY.remove(new OphthalCornearAnteriorSurface(corneaAnteriorListArraylist.get(finalC).getCorneaAnteriorId(),corneaAnteriorListArraylist.get(finalC).getCorneaAnteriorName(),
                                corneaAnteriorListArraylist.get(finalC).getDocId(),corneaAnteriorListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_CORNEA_ANTERIOR_ARRAY.size(); j++){
                            if(PATIENT_CORNEA_ANTERIOR_ARRAY.get(j).getCorneaAnteriorId() == corneaAnteriorListArraylist.get(finalC).getCorneaAnteriorId() && PATIENT_CORNEA_ANTERIOR_ARRAY.get(j).getCorneaAnteriorName().equals(corneaAnteriorListArraylist.get(finalC).getCorneaAnteriorName())) {
                                PATIENT_CORNEA_ANTERIOR_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            cornea_anterior_gridview_RE.addView(chk);

            // LEFT EYE SCLERA Check Boxes
            CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chkLE.setId(corneaAnteriorListArraylist.get(c).getCorneaAnteriorId());
            chkLE.setText(corneaAnteriorListArraylist.get(c).getCorneaAnteriorName());
            chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
            chkLE.setPadding(0,10,0,5);
            chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int ant_id = buttonView.getId();
                    String ant_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " ant_id: "+ ant_id+" ant_name: "+ ant_name);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_CORNEA_ANTERIOR_ARRAY.add(new OphthalCornearAnteriorSurface(corneaAnteriorListArraylist.get(finalC).getCorneaAnteriorId(),corneaAnteriorListArraylist.get(finalC).getCorneaAnteriorName(),
                                corneaAnteriorListArraylist.get(finalC).getDocId(),corneaAnteriorListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_CORNEA_ANTERIOR_ARRAY.remove(new OphthalCornearAnteriorSurface(corneaAnteriorListArraylist.get(finalC).getCorneaAnteriorId(),corneaAnteriorListArraylist.get(finalC).getCorneaAnteriorName(),
                                corneaAnteriorListArraylist.get(finalC).getDocId(),corneaAnteriorListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_CORNEA_ANTERIOR_ARRAY.size(); j++){
                            if(PATIENT_CORNEA_ANTERIOR_ARRAY.get(j).getCorneaAnteriorId() == corneaAnteriorListArraylist.get(finalC).getCorneaAnteriorId() && PATIENT_CORNEA_ANTERIOR_ARRAY.get(j).getCorneaAnteriorName().equals(corneaAnteriorListArraylist.get(finalC).getCorneaAnteriorName())) {
                                PATIENT_CORNEA_ANTERIOR_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            cornea_anterior_gridview_LE.addView(chkLE);

            if (PATIENT_CORNEA_ANTERIOR_INIT_ARRAY != null && PATIENT_CORNEA_ANTERIOR_INIT_ARRAY.size() > 0) {
                for(int j=0; j<PATIENT_CORNEA_ANTERIOR_INIT_ARRAY.size(); j++) {
                    if(PATIENT_CORNEA_ANTERIOR_INIT_ARRAY.get(j).getRightEye().equals("1") && corneaAnteriorListArraylist.get(c).getCorneaAnteriorId() == PATIENT_CORNEA_ANTERIOR_INIT_ARRAY.get(j).getCorneaAnteriorId()) {
                        chk.setChecked(true);
                    }
                    else if(PATIENT_CORNEA_ANTERIOR_INIT_ARRAY.get(j).getRightEye().equals("2") && corneaAnteriorListArraylist.get(c).getCorneaAnteriorId() == PATIENT_CORNEA_ANTERIOR_INIT_ARRAY.get(j).getCorneaAnteriorId()) {
                        chkLE.setChecked(true);
                    }
                }
            }
        }
    }

    private void prepareScleraCheckBoxes(final List<OphthalSclera> scleraListArraylist) {
        for(int c=0; c<scleraListArraylist.size(); c++){
            final int finalC = c;

            // RIGHT EYE SCLERA Check Boxes
            CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chk.setId(scleraListArraylist.get(c).getScleraId());
            chk.setText(scleraListArraylist.get(c).getScleraName());
            chk.setTextColor(getResources().getColor(R.color.textPrimary));
            chk.setPadding(0,10,0,5);
            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int sclera_id = buttonView.getId();
                    String  sclera_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " sclera_id: "+ sclera_id+" sclera_name: "+ sclera_name+ " finalC:"+finalC);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_SCLERA_ARRAY.add(new OphthalSclera(scleraListArraylist.get(finalC).getScleraId(),scleraListArraylist.get(finalC).getScleraName(),
                                scleraListArraylist.get(finalC).getDocId(),scleraListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_SCLERA_ARRAY.remove(new OphthalSclera(scleraListArraylist.get(finalC).getScleraId(),scleraListArraylist.get(finalC).getScleraName(),
                                scleraListArraylist.get(finalC).getDocId(),scleraListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_SCLERA_ARRAY.size(); j++){
                            if(PATIENT_SCLERA_ARRAY.get(j).getScleraId() == scleraListArraylist.get(finalC).getScleraId() && PATIENT_SCLERA_ARRAY.get(j).getScleraName().equals(scleraListArraylist.get(finalC).getScleraName())) {
                                PATIENT_SCLERA_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            sclera_gridview_RE.addView(chk);

            // LEFT EYE SCLERA Check Boxes
            CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chkLE.setId(scleraListArraylist.get(c).getScleraId());
            chkLE.setText(scleraListArraylist.get(c).getScleraName());
            chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
            chkLE.setPadding(0,10,0,5);
            chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int sclera_id = buttonView.getId();
                    String sclera_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " sclera_id: "+ sclera_id+" sclera_name: "+ sclera_name);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_SCLERA_ARRAY.add(new OphthalSclera(scleraListArraylist.get(finalC).getScleraId(),scleraListArraylist.get(finalC).getScleraName(),
                                scleraListArraylist.get(finalC).getDocId(),scleraListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_SCLERA_ARRAY.remove(new OphthalSclera(scleraListArraylist.get(finalC).getScleraId(),scleraListArraylist.get(finalC).getScleraName(),
                                scleraListArraylist.get(finalC).getDocId(),scleraListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_SCLERA_ARRAY.size(); j++){
                            if(PATIENT_SCLERA_ARRAY.get(j).getScleraId() == scleraListArraylist.get(finalC).getScleraId() && PATIENT_SCLERA_ARRAY.get(j).getScleraName().equals(scleraListArraylist.get(finalC).getScleraName())) {
                                PATIENT_SCLERA_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            sclera_gridview_LE.addView(chkLE);

            if (PATIENT_SCLERA_INIT_ARRAY != null && PATIENT_SCLERA_INIT_ARRAY.size() > 0) {
                for(int j=0; j<PATIENT_SCLERA_INIT_ARRAY.size(); j++) {
                    if(PATIENT_SCLERA_INIT_ARRAY.get(j).getRightEye().equals("1") && scleraListArraylist.get(c).getScleraId() == PATIENT_SCLERA_INIT_ARRAY.get(j).getScleraId()) {
                        chk.setChecked(true);
                    }
                    else if(PATIENT_SCLERA_INIT_ARRAY.get(j).getRightEye().equals("2") && scleraListArraylist.get(c).getScleraId() == PATIENT_SCLERA_INIT_ARRAY.get(j).getScleraId()) {
                        chkLE.setChecked(true);
                    }
                }
            }
        }
    }

    private void prepareConjuctivaCheckBoxes(final List<OphthalConjuctiva> conjuctivaListArraylist) {
        for(int c=0; c<conjuctivaListArraylist.size(); c++){
            final int finalC = c;

            // RIGHT EYE LIDS Check Boxes
            CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chk.setId(conjuctivaListArraylist.get(c).getConjuctivaId());
            chk.setText(conjuctivaListArraylist.get(c).getConjuctivaName());
            chk.setTextColor(getResources().getColor(R.color.textPrimary));
            chk.setPadding(0,10,0,5);
            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int conjuctiva_id = buttonView.getId();
                    String  conjuctiva_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " conjuctiva_id: "+ conjuctiva_id+" conjuctiva_name: "+ conjuctiva_name+ " finalC:"+finalC);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_CONJUCTIVA_ARRAY.add(new OphthalConjuctiva(conjuctivaListArraylist.get(finalC).getConjuctivaId(),conjuctivaListArraylist.get(finalC).getConjuctivaName(),
                                conjuctivaListArraylist.get(finalC).getDocId(),conjuctivaListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_CONJUCTIVA_ARRAY.remove(new OphthalConjuctiva(conjuctivaListArraylist.get(finalC).getConjuctivaId(),conjuctivaListArraylist.get(finalC).getConjuctivaName(),
                                conjuctivaListArraylist.get(finalC).getDocId(),conjuctivaListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_CONJUCTIVA_ARRAY.size(); j++){
                            if(PATIENT_CONJUCTIVA_ARRAY.get(j).getConjuctivaId() == conjuctivaListArraylist.get(finalC).getConjuctivaId() && PATIENT_CONJUCTIVA_ARRAY.get(j).getConjuctivaName().equals(conjuctivaListArraylist.get(finalC).getConjuctivaName())) {
                                PATIENT_CONJUCTIVA_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            conjuctiva_gridview_RE.addView(chk);

            // LEFT EYE LIDS Check Boxes
            CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chkLE.setId(conjuctivaListArraylist.get(c).getConjuctivaId());
            chkLE.setText(conjuctivaListArraylist.get(c).getConjuctivaName());
            chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
            chkLE.setPadding(0,10,0,5);
            chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int conjuctiva_id = buttonView.getId();
                    String conjuctiva_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " conjuctiva_id: "+ conjuctiva_id+" conjuctiva_name: "+ conjuctiva_name);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_CONJUCTIVA_ARRAY.add(new OphthalConjuctiva(conjuctivaListArraylist.get(finalC).getConjuctivaId(),conjuctivaListArraylist.get(finalC).getConjuctivaName(),
                                conjuctivaListArraylist.get(finalC).getDocId(),conjuctivaListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_CONJUCTIVA_ARRAY.remove(new OphthalConjuctiva(conjuctivaListArraylist.get(finalC).getConjuctivaId(),conjuctivaListArraylist.get(finalC).getConjuctivaName(),
                                conjuctivaListArraylist.get(finalC).getDocId(),conjuctivaListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_CONJUCTIVA_ARRAY.size(); j++){
                            if(PATIENT_CONJUCTIVA_ARRAY.get(j).getConjuctivaId() == conjuctivaListArraylist.get(finalC).getConjuctivaId() && PATIENT_CONJUCTIVA_ARRAY.get(j).getConjuctivaName().equals(conjuctivaListArraylist.get(finalC).getConjuctivaName())) {
                                PATIENT_CONJUCTIVA_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            conjuctiva_gridview_LE.addView(chkLE);

            if (PATIENT_CONJUCTIVA_INIT_ARRAY != null && PATIENT_CONJUCTIVA_INIT_ARRAY.size() > 0) {
                for(int j=0; j<PATIENT_CONJUCTIVA_INIT_ARRAY.size(); j++) {
                    if(PATIENT_CONJUCTIVA_INIT_ARRAY.get(j).getRightEye().equals("1") && conjuctivaListArraylist.get(c).getConjuctivaId() == PATIENT_CONJUCTIVA_INIT_ARRAY.get(j).getConjuctivaId()) {
                        chk.setChecked(true);
                    }
                    else if(PATIENT_CONJUCTIVA_INIT_ARRAY.get(j).getRightEye().equals("2") && conjuctivaListArraylist.get(c).getConjuctivaId() == PATIENT_CONJUCTIVA_INIT_ARRAY.get(j).getConjuctivaId()) {
                        chkLE.setChecked(true);
                    }
                }
            }
        }
    }

    private void prepareLidsCheckBoxes(final List<Lids> lidsListArraylist) {
        for(int c=0; c<lidsListArraylist.size(); c++){
            final int finalC = c;

            // RIGHT EYE LIDS Check Boxes
            CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chk.setId(lidsListArraylist.get(c).getLidsId());
            chk.setText(lidsListArraylist.get(c).getLidsName());
            chk.setTextColor(getResources().getColor(R.color.textPrimary));
            chk.setPadding(0,10,0,5);
            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int lids_id = buttonView.getId();
                    String lids_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " lids_id: "+ lids_id+" lids_name: "+ lids_name+ " finalC:"+finalC);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_LIDS_ARRAY.add(new Lids(lidsListArraylist.get(finalC).getLidsId(),lidsListArraylist.get(finalC).getLidsName(),
                                lidsListArraylist.get(finalC).getDocId(),lidsListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_LIDS_ARRAY.remove(new Lids(lidsListArraylist.get(finalC).getLidsId(),lidsListArraylist.get(finalC).getLidsName(),
                                lidsListArraylist.get(finalC).getDocId(),lidsListArraylist.get(finalC).getDocType(),"0","1",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_LIDS_ARRAY.size(); j++){
                            if(PATIENT_LIDS_ARRAY.get(j).getLidsId() == lidsListArraylist.get(finalC).getLidsId() && PATIENT_LIDS_ARRAY.get(j).getLidsName().equals(lidsListArraylist.get(finalC).getLidsName())) {
                                PATIENT_LIDS_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            lids_gridview_RE.addView(chk);

            // LEFT EYE LIDS Check Boxes
            CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
            chkLE.setId(lidsListArraylist.get(c).getLidsId());
            chkLE.setText(lidsListArraylist.get(c).getLidsName());
            chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
            chkLE.setPadding(0,10,0,5);
            chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    int lids_id = buttonView.getId();
                    String lids_name = buttonView.getText().toString();
                    Log.d(Utils.TAG, " lids_id: "+ lids_id+" lids_name: "+ lids_name);

                    Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                    if(isChecked) {
                        Log.d(Utils.TAG, " isChecked: "+ isChecked);
                        PATIENT_LIDS_ARRAY.add(new Lids(lidsListArraylist.get(finalC).getLidsId(),lidsListArraylist.get(finalC).getLidsName(),
                                lidsListArraylist.get(finalC).getDocId(),lidsListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));
                    }
                    else {
                        Log.d(Utils.TAG, " not Checked: "+ isChecked);
                        PATIENT_LIDS_ARRAY.remove(new Lids(lidsListArraylist.get(finalC).getLidsId(),lidsListArraylist.get(finalC).getLidsName(),
                                lidsListArraylist.get(finalC).getDocId(),lidsListArraylist.get(finalC).getDocType(),"2","0",USER_ID,USER_LOGIN_TYPE));

                        for(int j=0; j<PATIENT_LIDS_ARRAY.size(); j++){
                            if(PATIENT_LIDS_ARRAY.get(j).getLidsId() == lidsListArraylist.get(finalC).getLidsId() && PATIENT_LIDS_ARRAY.get(j).getLidsName().equals(lidsListArraylist.get(finalC).getLidsName())) {
                                PATIENT_LIDS_ARRAY.remove(j);
                            }
                        }
                    }
                }
            });
            lids_gridview_LE.addView(chkLE);

            if (PATIENT_LIDS_INIT_ARRAY != null && PATIENT_LIDS_INIT_ARRAY.size() > 0) {
                for(int j=0; j<PATIENT_LIDS_INIT_ARRAY.size(); j++) {
                    // Log.d(Utils.TAG, " lidid: "+ lidsListArraylist.get(c).getLidsId()+" arrayid: "+ PATIENT_LIDS_INIT_ARRAY.get(j).getLidsId());
                    if(PATIENT_LIDS_INIT_ARRAY.get(j).getRightEye().equals("1") && lidsListArraylist.get(c).getLidsId() == PATIENT_LIDS_INIT_ARRAY.get(j).getLidsId()) {
                        chk.setChecked(true);
                    }
                    else if(PATIENT_LIDS_INIT_ARRAY.get(j).getRightEye().equals("2") && lidsListArraylist.get(c).getLidsId() == PATIENT_LIDS_INIT_ARRAY.get(j).getLidsId()) {
                        chkLE.setChecked(true);
                    }
                }
            }
        }
    }

    private void refreshChiefMedicalComplaint() {
        Log.d(Utils.TAG, "PATIENT_CHIEF_MEDCOMPLAINT_ARRAY: " + PATIENT_CHIEF_MEDCOMPLAINT_ARRAY.size());
        medcomplaintAdapter = new ChiefMedicalAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_CHIEF_MEDCOMPLAINT_ARRAY);
        chiefComplaintRecyclerview.setAdapter(medcomplaintAdapter);
        medcomplaintAdapter.notifyDataSetChanged();
    }

    private void prepareChiefMedicalCompalint(List<ChiefMedicalComplaint> chiefcomplaintListArraylist) {

        final Dialog dialog = new Dialog(EditOphthalVisitDetailsActivity.this, R.style.CustomDialog);
        dialog.setContentView(R.layout.custom_add_chiefmedical_complaint);
        dialog.setTitle("Add Chief Medical Complaint");
        dialog.setCanceledOnTouchOutside(true);

        medicalComplaint_searchView = (SearchView) dialog.findViewById(R.id.searchview_chiefcomplaint);
        medicalComplaint_listview = (ListView) dialog.findViewById(R.id.chiefcomplaint_list);
        add_chiefMedCompalint = (ImageView) dialog.findViewById(R.id.medcomp_add);
        final CustomTextViewBold close_btn = (CustomTextViewBold)  dialog.findViewById(R.id.medcomp_close);

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(close_btn.getWindowToken(), 0);

                refreshChiefMedicalComplaint();
            }
        });

        add_chiefMedCompalint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(add_chiefMedCompalint.getWindowToken(), 0);

                if(medicalComplaint_searchView.getQuery().toString().trim().equals("")) {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this,"Enter chief medical complaint !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this,medicalComplaint_searchView.getQuery()+ " is added to the list", Toast.LENGTH_SHORT).show();
                    PATIENT_CHIEF_MEDCOMPLAINT_ARRAY.add(new ChiefMedicalComplaint(0,medicalComplaint_searchView.getQuery().toString(),
                            USER_ID,1,USER_ID, USER_LOGIN_TYPE));
                }

                medicalComplaint_searchView.setQuery("", false);
                medicalComplaint_searchView.clearFocus();
                medicalComplaint_searchView.setIconified(true);

            }
        });

        final CustomMedicalComplaintFilterAdapter medcompAdapter = new CustomMedicalComplaintFilterAdapter(EditOphthalVisitDetailsActivity.this,
                R.layout.activity_listview, chiefcomplaintListArraylist);
        medicalComplaint_listview.setAdapter(medcompAdapter);
        medicalComplaint_listview.setTextFilterEnabled(true);

        medicalComplaint_searchView.setIconifiedByDefault(false);
        medicalComplaint_searchView.setQueryHint("Search Medical Complaint Here");
        medicalComplaint_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                medicalComplaint_searchView.setQuery("", false);
                medicalComplaint_searchView.clearFocus();
                medicalComplaint_searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Utils.TAG, "SERACH: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    Log.d(Utils.TAG, "is empty " );
                    medcompAdapter.filter(newText.toString());
                    // specialization_listview.clearTextFilter();
                } else {
                    //  specialization_listview.setFilterText(newText.toString());
                    medcompAdapter.filter(newText.toString());
                }
                return true;
            }
        });

        medicalComplaint_searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                medicalComplaint_searchView.setQuery("", false);
                medicalComplaint_searchView.setIconified(false);
                medicalComplaint_searchView.clearFocus();
                return false;
            }
        });

        medicalComplaint_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChiefMedicalComplaint myCustomAdapterItem = (ChiefMedicalComplaint) parent.getItemAtPosition(position);
              /* Toast.makeText(getActivity(),  myCustomAdapterItem.getSymptomsName() + " added.",
                        Toast.LENGTH_SHORT)
                        .show();*/
                medicalComplaint_searchView.setQuery("", false);
                medicalComplaint_searchView.setIconified(false);
                medicalComplaint_searchView.clearFocus();

                Toast.makeText(EditOphthalVisitDetailsActivity.this,myCustomAdapterItem.getSymptomsName()+ " is added to the list", Toast.LENGTH_SHORT).show();

                PATIENT_CHIEF_MEDCOMPLAINT_ARRAY.add(new ChiefMedicalComplaint(myCustomAdapterItem.getComplaintId(),myCustomAdapterItem.getSymptomsName(),
                        myCustomAdapterItem.getDocId(),myCustomAdapterItem.getDocType(),USER_ID, USER_LOGIN_TYPE));

            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addvisit_ophthal_chief_medcomp_btn:
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(chief_medcomp_btn.getWindowToken(), 0);

                if(view_chief_medcomp_status == false) {
                    chief_medcomp_contents.setVisibility(View.VISIBLE);
                    view_chief_medcomp_status = true;
                }
                else  if(view_chief_medcomp_status == true) {
                    chief_medcomp_contents.setVisibility(View.GONE);
                    view_chief_medcomp_status = false;
                }
                break;
            case R.id.addvisit_ophthal_examinations_btn:
                InputMethodManager imm3 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(examinations_btn.getWindowToken(), 0);

                if(view_examinations_status == false) {
                    examinations_contents.setVisibility(View.VISIBLE);
                    view_examinations_status = true;
                }
                else  if(view_examinations_status == true) {
                    examinations_contents.setVisibility(View.GONE);
                    view_examinations_status = false;
                }
                break;
            case R.id.add_ophthal_visit_submit:
                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(submit_btn.getWindowToken(), 0);

                submitAddVisitValidationForm();
                break;
            case R.id.ophthal_lids_btn:
                InputMethodManager imm4 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm4.hideSoftInputFromWindow(lids_btn.getWindowToken(), 0);
                if(view_lids_status == false) {
                    lids_contents.setVisibility(View.VISIBLE);
                    ophthal_lids_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_lids_status = true;
                }
                else  if(view_lids_status == true) {
                    lids_contents.setVisibility(View.GONE);
                    ophthal_lids_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_lids_status = false;
                }
                break;
            case R.id.lids_add_submit:
                InputMethodManager imm5 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm5.hideSoftInputFromWindow(add_lids.getWindowToken(), 0);
                if(_edt_add_lids.getText().toString().equals("")) {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this, "Enter lids !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    refreshLidsContents(_edt_add_lids.getText().toString().trim());
                }
                break;
            case R.id.ophthal_conjuctiva_btn:
                InputMethodManager imm6 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm6.hideSoftInputFromWindow(conjuctiva_btn.getWindowToken(), 0);
                if(view_conjuctiva_status == false) {
                    conjuctiva_contents.setVisibility(View.VISIBLE);
                    ophthal_conjuctiva_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_conjuctiva_status = true;
                }
                else  if(view_conjuctiva_status == true) {
                    conjuctiva_contents.setVisibility(View.GONE);
                    ophthal_conjuctiva_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_conjuctiva_status = false;
                }
                break;
            case R.id.conjuctiva_add_submit:
                InputMethodManager imm7 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm7.hideSoftInputFromWindow(add_conjuctiva.getWindowToken(), 0);
                if(_edt_add_conjuctiva.getText().toString().equals("")) {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this, "Enter Conjuctiva !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    refreshConjuctivaContents(_edt_add_conjuctiva.getText().toString().trim());
                }
                break;
            case R.id.ophthal_sclera_btn:
                InputMethodManager imm8 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm8.hideSoftInputFromWindow(sclera_btn.getWindowToken(), 0);
                if(view_sclera_status == false) {
                    sclera_contents.setVisibility(View.VISIBLE);
                    ophthal_sclera_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_sclera_status = true;
                }
                else  if(view_sclera_status == true) {
                    sclera_contents.setVisibility(View.GONE);
                    ophthal_sclera_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_sclera_status = false;
                }
                break;
            case R.id.sclera_add_submit:
                InputMethodManager imm9 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm9.hideSoftInputFromWindow(add_sclera.getWindowToken(), 0);
                if(_edt_add_sclera.getText().toString().equals("")) {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this, "Enter Sclera !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    refreshScleraContents(_edt_add_sclera.getText().toString().trim());
                }
                break;
            case R.id.ophthal_cornea_anterior_btn:
                InputMethodManager imm10 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm10.hideSoftInputFromWindow(cornea_anterior_btn.getWindowToken(), 0);
                if(view_cornea_anterior_status == false) {
                    cornea_anterior_contents.setVisibility(View.VISIBLE);
                    ophthal_cornea_anterior_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_cornea_anterior_status = true;
                }
                else  if(view_cornea_anterior_status == true) {
                    cornea_anterior_contents.setVisibility(View.GONE);
                    ophthal_cornea_anterior_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_cornea_anterior_status = false;
                }
                break;
            case R.id.cornea_anterior_add_submit:
                InputMethodManager imm11 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm11.hideSoftInputFromWindow(add_cornea_anterior.getWindowToken(), 0);
                if(_edt_add_cornea_anterior.getText().toString().equals("")) {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this, "Enter Cornea Anterior Surface !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    refreshCorneaAnteriorContents(_edt_add_cornea_anterior.getText().toString().trim());
                }
                break;
            case R.id.ophthal_cornea_posterior_btn:
                InputMethodManager imm12 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm12.hideSoftInputFromWindow(cornea_posterior_btn.getWindowToken(), 0);
                if(view_cornea_posterior_status == false) {
                    cornea_posterior_contents.setVisibility(View.VISIBLE);
                    ophthal_cornea_posterior_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_cornea_posterior_status = true;
                }
                else  if(view_cornea_posterior_status == true) {
                    cornea_posterior_contents.setVisibility(View.GONE);
                    ophthal_cornea_posterior_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_cornea_posterior_status = false;
                }
                break;
            case R.id.cornea_posterior_add_submit:
                InputMethodManager imm13 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm13.hideSoftInputFromWindow(add_cornea_posterior.getWindowToken(), 0);
                if(_edt_add_cornea_posterior.getText().toString().equals("")) {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this, "Enter Cornea Posterior Surface !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    refreshCorneaPosteriorContents(_edt_add_cornea_posterior.getText().toString().trim());
                }
                break;
            case R.id.ophthal_anterior_chamber_btn:
                InputMethodManager imm14 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm14.hideSoftInputFromWindow(anterior_chamber_btn.getWindowToken(), 0);
                if(view_anterior_chamber_status == false) {
                    anterior_chamber_contents.setVisibility(View.VISIBLE);
                    ophthal_anterior_chamber_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_anterior_chamber_status = true;
                }
                else  if(view_anterior_chamber_status == true) {
                    anterior_chamber_contents.setVisibility(View.GONE);
                    ophthal_anterior_chamber_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_anterior_chamber_status = false;
                }
                break;
            case R.id.anterior_chamber_add_submit:
                InputMethodManager imm15 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm15.hideSoftInputFromWindow(add_anterior_chamber.getWindowToken(), 0);
                if(_edt_add_anterior_chamber.getText().toString().equals("")) {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this, "Enter Anterior Chamber !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    refreshAnteriorChamberContents(_edt_add_anterior_chamber.getText().toString().trim());
                }
                break;
            case R.id.ophthal_iris_btn:
                InputMethodManager imm16 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm16.hideSoftInputFromWindow(iris_btn.getWindowToken(), 0);
                if(view_iris_status == false) {
                    iris_contents.setVisibility(View.VISIBLE);
                    ophthal_iris_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_iris_status = true;
                }
                else  if(view_iris_status == true) {
                    iris_contents.setVisibility(View.GONE);
                    ophthal_iris_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_iris_status = false;
                }
                break;
            case R.id.iris_add_submit:
                InputMethodManager imm17 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm17.hideSoftInputFromWindow(add_iris.getWindowToken(), 0);
                if(_edt_add_iris.getText().toString().equals("")) {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this, "Enter Iris !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    refreshIrisrContents(_edt_add_iris.getText().toString().trim());
                }
                break;
            case R.id.ophthal_pupil_btn:
                InputMethodManager imm18 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm18.hideSoftInputFromWindow(pupil_btn.getWindowToken(), 0);
                if(view_pupil_status == false) {
                    pupil_contents.setVisibility(View.VISIBLE);
                    ophthal_pupil_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_pupil_status = true;
                }
                else  if(view_pupil_status == true) {
                    pupil_contents.setVisibility(View.GONE);
                    ophthal_pupil_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_pupil_status = false;
                }
                break;
            case R.id.pupil_add_submit:
                InputMethodManager imm19 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm19.hideSoftInputFromWindow(add_pupil.getWindowToken(), 0);
                if(_edt_add_pupil.getText().toString().equals("")) {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this, "Enter Pupil !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    refreshPupilContents(_edt_add_pupil.getText().toString().trim());
                }
                break;
            case R.id.ophthal_angle_btn:
                InputMethodManager imm20 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm20.hideSoftInputFromWindow(angle_btn.getWindowToken(), 0);
                if(view_angle_status == false) {
                    angle_contents.setVisibility(View.VISIBLE);
                    ophthal_angle_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_angle_status = true;
                }
                else  if(view_angle_status == true) {
                    angle_contents.setVisibility(View.GONE);
                    ophthal_angle_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_angle_status = false;
                }
                break;
            case R.id.angle_add_submit:
                InputMethodManager imm21 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm21.hideSoftInputFromWindow(add_angle.getWindowToken(), 0);
                if(_edt_add_angle.getText().toString().equals("")) {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this, "Enter Angle of Anterior Chamber !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    refreshAngleContents(_edt_add_angle.getText().toString().trim());
                }
                break;
            case R.id.ophthal_lens_btn:
                InputMethodManager imm22 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm22.hideSoftInputFromWindow(lens_btn.getWindowToken(), 0);
                if(view_lens_status == false) {
                    lens_contents.setVisibility(View.VISIBLE);
                    ophthal_lens_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_lens_status = true;
                }
                else  if(view_lens_status == true) {
                    lens_contents.setVisibility(View.GONE);
                    ophthal_lens_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_lens_status = false;
                }
                break;
            case R.id.lens_add_submit:
                InputMethodManager imm23 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm23.hideSoftInputFromWindow(add_lens.getWindowToken(), 0);
                if(_edt_add_lens.getText().toString().equals("")) {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this, "Enter Lens !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    refreshLensContents(_edt_add_lens.getText().toString().trim());
                }
                break;
            case R.id.ophthal_viterous_btn:
                InputMethodManager imm24 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm24.hideSoftInputFromWindow(viterous_btn.getWindowToken(), 0);
                if(view_viterous_status == false) {
                    viterous_contents.setVisibility(View.VISIBLE);
                    ophthal_viterous_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_viterous_status = true;
                }
                else  if(view_viterous_status == true) {
                    viterous_contents.setVisibility(View.GONE);
                    ophthal_viterous_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_viterous_status = false;
                }
                break;
            case R.id.viterous_add_submit:
                InputMethodManager imm25 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm25.hideSoftInputFromWindow(add_viterous.getWindowToken(), 0);
                if(_edt_add_viterous.getText().toString().equals("")) {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this, "Enter Viterous !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    refreshViterousContents(_edt_add_viterous.getText().toString().trim());
                }
                break;
            case R.id.ophthal_fundus_btn:
                InputMethodManager imm26 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm26.hideSoftInputFromWindow(fundus_btn.getWindowToken(), 0);
                if(view_fundus_status == false) {
                    fundus_contents.setVisibility(View.VISIBLE);
                    ophthal_fundus_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_fundus_status = true;
                }
                else  if(view_fundus_status == true) {
                    fundus_contents.setVisibility(View.GONE);
                    ophthal_fundus_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_fundus_status = false;
                }
                break;
            case R.id.fundus_add_submit:
                InputMethodManager imm27 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm27.hideSoftInputFromWindow(add_fundus.getWindowToken(), 0);
                if(_edt_add_fundus.getText().toString().equals("")) {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this, "Enter Fundus !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    refreshFundusContents(_edt_add_fundus.getText().toString().trim());
                }
                break;
            case R.id.ophthal_refraction_btn:
                InputMethodManager imm28 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm28.hideSoftInputFromWindow(refraction_btn.getWindowToken(), 0);
                if(view_refraction_status == false) {
                    refraction_contents.setVisibility(View.VISIBLE);
                    ophthal_refraction_image.setImageResource(R.mipmap.up_arrow_circle);
                    view_refraction_status = true;
                }
                else  if(view_refraction_status == true) {
                    refraction_contents.setVisibility(View.GONE);
                    ophthal_refraction_image.setImageResource(R.mipmap.down_arrow_circle);
                    view_refraction_status = false;
                }
                break;
            case R.id.addvisit_investigation_btn:
                InputMethodManager imm29 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm29.hideSoftInputFromWindow(investigation_btn.getWindowToken(), 0);

                if(view_investigation_status == false) {
                    investigation_contents.setVisibility(View.VISIBLE);
                    view_investigation_status = true;
                }
                else  if(view_investigation_status == true) {
                    investigation_contents.setVisibility(View.GONE);
                    view_investigation_status = false;
                }
                break;
            case R.id.addvisit_diagnosis_btn:
                InputMethodManager imm30 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm30.hideSoftInputFromWindow(diagnosis_btn.getWindowToken(), 0);
                if(view_diagnosis_status == false) {
                    diagnosis_contents.setVisibility(View.VISIBLE);
                    view_diagnosis_status = true;
                }
                else  if(view_diagnosis_status == true) {
                    diagnosis_contents.setVisibility(View.GONE);
                    view_diagnosis_status = false;
                }
                break;
            case R.id.addvisit_treatment_btn:
                InputMethodManager imm31 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm31.hideSoftInputFromWindow(treatment_btn.getWindowToken(), 0);
                if(view_treatment_status == false) {
                    treatment_contents.setVisibility(View.VISIBLE);
                    view_treatment_status = true;
                }
                else  if(view_treatment_status == true) {
                    treatment_contents.setVisibility(View.GONE);
                    view_treatment_status = false;
                }
                break;
            case R.id.addvisit_prescription_btn:
                InputMethodManager imm32 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm32.hideSoftInputFromWindow(prescription_btn.getWindowToken(), 0);
                if(view_prescription_status == false) {
                    prescription_contents.setVisibility(View.VISIBLE);
                    view_prescription_status = true;
                }
                else  if(view_prescription_status == true) {
                    prescription_contents.setVisibility(View.GONE);
                    view_prescription_status = false;
                }
                break;
            case R.id.load_templates:
                InputMethodManager imm33 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm33.hideSoftInputFromWindow(load_templates.getWindowToken(), 0);
                loadTemplatesDropDown();
                break;
            case R.id.add_visit_template_save:
                InputMethodManager imm34 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm34.hideSoftInputFromWindow(save_template.getWindowToken(), 0);
                submitTemplateFormToServer();
                break;
            case R.id.add_visit_consultfee_set:
                InputMethodManager imm35 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm35.hideSoftInputFromWindow(consultfees_Set_btn.getWindowToken(), 0);
                Intent i2 = new Intent(EditOphthalVisitDetailsActivity.this, ProfileActivity.class);
                i2.putExtra("title","My Profile");
                startActivity(i2);
                break;
            case R.id.ophthal_spectacle_prescription_btn:
                InputMethodManager imm36 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm36.hideSoftInputFromWindow(spectacle_prescription_btn.getWindowToken(), 0);
                if(view_spectacle_prescription_status == false) {
                    spectacle_prescription_contents.setVisibility(View.VISIBLE);
                    view_spectacle_prescription_status = true;
                }
                else  if(view_spectacle_prescription_status == true) {
                    spectacle_prescription_contents.setVisibility(View.GONE);
                    view_spectacle_prescription_status = false;
                }
                break;
        }
    }

    private void refreshFundusContents(String fundus_name) {
        CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chk.setId(0);
        chk.setText(fundus_name);
        chk.setTextColor(getResources().getColor(R.color.textPrimary));
        chk.setPadding(0,10,0,5);
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int fundus_id = buttonView.getId();
                String fundus_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " fundus_id: "+ fundus_id+" fundus_name: "+ fundus_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_FUNDUS_ARRAY.add(new OphthalFundus(0,fundus_name,USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_FUNDUS_ARRAY.add(new OphthalFundus(0,fundus_name, USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_FUNDUS_ARRAY.size(); j++){
                        if(PATIENT_FUNDUS_ARRAY.get(j).getFundusId() == 0 && PATIENT_FUNDUS_ARRAY.get(j).getFundusName().equals(fundus_name)) {
                            PATIENT_FUNDUS_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        fundus_gridview_RE.addView(chk, 0);

        CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chkLE.setId(0);
        chkLE.setText(fundus_name);
        chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
        chkLE.setPadding(0,10,0,5);
        chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int fundus_id = buttonView.getId();
                String fundus_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " fundus_id: "+ fundus_id+" fundus_name: "+ fundus_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_FUNDUS_ARRAY.add(new OphthalFundus(0,fundus_name,USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_FUNDUS_ARRAY.add(new OphthalFundus(0,fundus_name, USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_FUNDUS_ARRAY.size(); j++){
                        if(PATIENT_FUNDUS_ARRAY.get(j).getFundusId() == 0 && PATIENT_FUNDUS_ARRAY.get(j).getFundusName().equals(fundus_name)) {
                            PATIENT_FUNDUS_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        fundus_gridview_LE.addView(chkLE, 0);
        _edt_add_fundus.setText("");
    }

    private void refreshViterousContents(String viterous_name) {
        CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chk.setId(0);
        chk.setText(viterous_name);
        chk.setTextColor(getResources().getColor(R.color.textPrimary));
        chk.setPadding(0,10,0,5);
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int viterous_id = buttonView.getId();
                String viterous_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " viterous_id: "+ viterous_id+" viterous_name: "+ viterous_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_VITEROUS_ARRAY.add(new OphthalViterous(0,viterous_name,USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_VITEROUS_ARRAY.add(new OphthalViterous(0,viterous_name, USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_VITEROUS_ARRAY.size(); j++){
                        if(PATIENT_VITEROUS_ARRAY.get(j).getViterousId() == 0 && PATIENT_VITEROUS_ARRAY.get(j).getViterousName().equals(viterous_name)) {
                            PATIENT_VITEROUS_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        viterous_gridview_RE.addView(chk, 0);

        CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chkLE.setId(0);
        chkLE.setText(viterous_name);
        chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
        chkLE.setPadding(0,10,0,5);
        chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int viterous_id = buttonView.getId();
                String viterous_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " viterous_id: "+ viterous_id+" viterous_name: "+ viterous_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_VITEROUS_ARRAY.add(new OphthalViterous(0,viterous_name,USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_VITEROUS_ARRAY.add(new OphthalViterous(0,viterous_name, USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_VITEROUS_ARRAY.size(); j++){
                        if(PATIENT_VITEROUS_ARRAY.get(j).getViterousId() == 0 && PATIENT_VITEROUS_ARRAY.get(j).getViterousName().equals(viterous_name)) {
                            PATIENT_VITEROUS_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        viterous_gridview_LE.addView(chkLE, 0);
        _edt_add_viterous.setText("");
    }

    private void refreshLensContents(String lens_name) {
        CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chk.setId(0);
        chk.setText(lens_name);
        chk.setTextColor(getResources().getColor(R.color.textPrimary));
        chk.setPadding(0,10,0,5);
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int lens_id = buttonView.getId();
                String lens_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " lens_id: "+ lens_id+" lens_name: "+ lens_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_LENS_ARRAY.add(new OphthalLens(0,lens_name,USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_LENS_ARRAY.add(new OphthalLens(0,lens_name, USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_LENS_ARRAY.size(); j++){
                        if(PATIENT_LENS_ARRAY.get(j).getLensId() == 0 && PATIENT_LENS_ARRAY.get(j).getLensName().equals(lens_name)) {
                            PATIENT_LENS_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        lens_gridview_RE.addView(chk, 0);

        CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chkLE.setId(0);
        chkLE.setText(lens_name);
        chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
        chkLE.setPadding(0,10,0,5);
        chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int lens_id = buttonView.getId();
                String lens_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " lens_id: "+ lens_id+" lens_name: "+ lens_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_LENS_ARRAY.add(new OphthalLens(0,lens_name,USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_LENS_ARRAY.add(new OphthalLens(0,lens_name, USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_LENS_ARRAY.size(); j++){
                        if(PATIENT_LENS_ARRAY.get(j).getLensId() == 0 && PATIENT_LENS_ARRAY.get(j).getLensName().equals(lens_name)) {
                            PATIENT_LENS_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        lens_gridview_LE.addView(chkLE, 0);
        _edt_add_lens.setText("");
    }

    private void refreshAngleContents(String angle_name) {
        CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chk.setId(0);
        chk.setText(angle_name);
        chk.setTextColor(getResources().getColor(R.color.textPrimary));
        chk.setPadding(0,10,0,5);
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int angle_id = buttonView.getId();
                String angle_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " angle_id: "+ angle_id+" angle_name: "+ angle_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_ANGLE_ARRAY.add(new OphthalAngleAnteriorChamber(0,angle_name,USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_ANGLE_ARRAY.add(new OphthalAngleAnteriorChamber(0,angle_name, USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_ANGLE_ARRAY.size(); j++){
                        if(PATIENT_ANGLE_ARRAY.get(j).getAngleId() == 0 && PATIENT_ANGLE_ARRAY.get(j).getAngleName().equals(angle_name)) {
                            PATIENT_ANGLE_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        angle_gridview_RE.addView(chk, 0);

        CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chkLE.setId(0);
        chkLE.setText(angle_name);
        chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
        chkLE.setPadding(0,10,0,5);
        chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int angle_id = buttonView.getId();
                String angle_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " angle_id: "+ angle_id+" angle_name: "+ angle_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_ANGLE_ARRAY.add(new OphthalAngleAnteriorChamber(0,angle_name,USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_ANGLE_ARRAY.add(new OphthalAngleAnteriorChamber(0,angle_name, USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_ANGLE_ARRAY.size(); j++){
                        if(PATIENT_ANGLE_ARRAY.get(j).getAngleId() == 0 && PATIENT_ANGLE_ARRAY.get(j).getAngleName().equals(angle_name)) {
                            PATIENT_ANGLE_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        angle_gridview_LE.addView(chkLE, 0);
        _edt_add_angle.setText("");
    }

    private void refreshPupilContents(String pupil_name) {
        CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chk.setId(0);
        chk.setText(pupil_name);
        chk.setTextColor(getResources().getColor(R.color.textPrimary));
        chk.setPadding(0,10,0,5);
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int pupil_id = buttonView.getId();
                String pupil_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " pupil_id: "+ pupil_id+" pupil_name: "+ pupil_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_PUPIL_ARRAY.add(new OphthalPupil(0,pupil_name,USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_PUPIL_ARRAY.add(new OphthalPupil(0,pupil_name, USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_PUPIL_ARRAY.size(); j++){
                        if(PATIENT_PUPIL_ARRAY.get(j).getPupilId() == 0 && PATIENT_PUPIL_ARRAY.get(j).getPupilName().equals(pupil_name)) {
                            PATIENT_PUPIL_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        pupil_gridview_RE.addView(chk, 0);

        CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chkLE.setId(0);
        chkLE.setText(pupil_name);
        chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
        chkLE.setPadding(0,10,0,5);
        chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int pupil_id = buttonView.getId();
                String pupil_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " pupil_id: "+ pupil_id+" pupil_name: "+ pupil_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_PUPIL_ARRAY.add(new OphthalPupil(0,pupil_name,USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_PUPIL_ARRAY.add(new OphthalPupil(0,pupil_name, USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_PUPIL_ARRAY.size(); j++){
                        if(PATIENT_PUPIL_ARRAY.get(j).getPupilId() == 0 && PATIENT_PUPIL_ARRAY.get(j).getPupilName().equals(pupil_name)) {
                            PATIENT_PUPIL_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        pupil_gridview_LE.addView(chkLE, 0);
        _edt_add_pupil.setText("");
    }

    private void refreshIrisrContents(String iris_name) {
        CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chk.setId(0);
        chk.setText(iris_name);
        chk.setTextColor(getResources().getColor(R.color.textPrimary));
        chk.setPadding(0,10,0,5);
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int iris_id = buttonView.getId();
                String iris_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " iris_id: "+ iris_id+" iris_name: "+ iris_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_IRIS_ARRAY.add(new OphthalIris(0,iris_name,USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_IRIS_ARRAY.add(new OphthalIris(0,iris_name, USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_IRIS_ARRAY.size(); j++){
                        if(PATIENT_IRIS_ARRAY.get(j).getIrisId() == 0 && PATIENT_IRIS_ARRAY.get(j).getIrisName().equals(iris_name)) {
                            PATIENT_IRIS_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        iris_gridview_RE.addView(chk, 0);

        CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chkLE.setId(0);
        chkLE.setText(iris_name);
        chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
        chkLE.setPadding(0,10,0,5);
        chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int iris_id = buttonView.getId();
                String iris_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " iris_id: "+ iris_id+" iris_name: "+ iris_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_IRIS_ARRAY.add(new OphthalIris(0,iris_name,USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_IRIS_ARRAY.add(new OphthalIris(0,iris_name, USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_IRIS_ARRAY.size(); j++){
                        if(PATIENT_IRIS_ARRAY.get(j).getIrisId() == 0 && PATIENT_IRIS_ARRAY.get(j).getIrisName().equals(iris_name)) {
                            PATIENT_IRIS_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        iris_gridview_LE.addView(chkLE, 0);
        _edt_add_iris.setText("");
    }

    private void refreshAnteriorChamberContents(String anterior_chamber_name) {
        CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chk.setId(0);
        chk.setText(anterior_chamber_name);
        chk.setTextColor(getResources().getColor(R.color.textPrimary));
        chk.setPadding(0,10,0,5);
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int chamber_id = buttonView.getId();
                String chamber_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " chamber_id: "+ chamber_id+" chamber_name: "+ chamber_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_ANTERIOR_CHAMBER_ARRAY.add(new OphthalAnteriorChamber(0,chamber_name,USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_ANTERIOR_CHAMBER_ARRAY.add(new OphthalAnteriorChamber(0,chamber_name, USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_ANTERIOR_CHAMBER_ARRAY.size(); j++){
                        if(PATIENT_ANTERIOR_CHAMBER_ARRAY.get(j).getAnteriorChamberId() == 0 && PATIENT_ANTERIOR_CHAMBER_ARRAY.get(j).getAnteriorChamberName().equals(chamber_name)) {
                            PATIENT_ANTERIOR_CHAMBER_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        anterior_chamber_gridview_RE.addView(chk, 0);

        CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chkLE.setId(0);
        chkLE.setText(anterior_chamber_name);
        chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
        chkLE.setPadding(0,10,0,5);
        chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int chamber_id = buttonView.getId();
                String chamber_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " chamber_id: "+ chamber_id+" chamber_name: "+ chamber_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_ANTERIOR_CHAMBER_ARRAY.add(new OphthalAnteriorChamber(0,chamber_name,USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_ANTERIOR_CHAMBER_ARRAY.add(new OphthalAnteriorChamber(0,chamber_name, USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_ANTERIOR_CHAMBER_ARRAY.size(); j++){
                        if(PATIENT_ANTERIOR_CHAMBER_ARRAY.get(j).getAnteriorChamberId() == 0 && PATIENT_ANTERIOR_CHAMBER_ARRAY.get(j).getAnteriorChamberName().equals(chamber_name)) {
                            PATIENT_ANTERIOR_CHAMBER_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        anterior_chamber_gridview_LE.addView(chkLE, 0);
        _edt_add_anterior_chamber.setText("");
    }

    private void refreshCorneaPosteriorContents(String cornea_posterior_name) {
        CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chk.setId(0);
        chk.setText(cornea_posterior_name);
        chk.setTextColor(getResources().getColor(R.color.textPrimary));
        chk.setPadding(0,10,0,5);
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int post_id = buttonView.getId();
                String post_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " post_id: "+ post_id+" post_name: "+ post_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_CORNEA_POSTERIOR_ARRAY.add(new OphthalCornearPosteriorSurface(0,post_name,USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_CORNEA_POSTERIOR_ARRAY.add(new OphthalCornearPosteriorSurface(0,post_name, USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_CORNEA_POSTERIOR_ARRAY.size(); j++){
                        if(PATIENT_CORNEA_POSTERIOR_ARRAY.get(j).getCorneaPosteriorId() == 0 && PATIENT_CORNEA_POSTERIOR_ARRAY.get(j).getCorneaPosteriorName().equals(post_name)) {
                            PATIENT_CORNEA_POSTERIOR_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        cornea_posterior_gridview_RE.addView(chk, 0);

        CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chkLE.setId(0);
        chkLE.setText(cornea_posterior_name);
        chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
        chkLE.setPadding(0,10,0,5);
        chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int post_id = buttonView.getId();
                String post_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " post_id: "+ post_id+" post_name: "+ post_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_CORNEA_POSTERIOR_ARRAY.add(new OphthalCornearPosteriorSurface(0,post_name,USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_CORNEA_POSTERIOR_ARRAY.add(new OphthalCornearPosteriorSurface(0,post_name, USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_CORNEA_POSTERIOR_ARRAY.size(); j++){
                        if(PATIENT_CORNEA_POSTERIOR_ARRAY.get(j).getCorneaPosteriorId() == 0 && PATIENT_CORNEA_POSTERIOR_ARRAY.get(j).getCorneaPosteriorName().equals(post_name)) {
                            PATIENT_CORNEA_POSTERIOR_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        cornea_posterior_gridview_LE.addView(chkLE, 0);
        _edt_add_cornea_posterior.setText("");
    }

    private void refreshCorneaAnteriorContents(String cornea_anterior_name) {
        CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chk.setId(0);
        chk.setText(cornea_anterior_name);
        chk.setTextColor(getResources().getColor(R.color.textPrimary));
        chk.setPadding(0,10,0,5);
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int ant_id = buttonView.getId();
                String ant_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " ant_id: "+ ant_id+" ant_name: "+ ant_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_CORNEA_ANTERIOR_ARRAY.add(new OphthalCornearAnteriorSurface(0,ant_name,USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_CORNEA_ANTERIOR_ARRAY.add(new OphthalCornearAnteriorSurface(0,ant_name, USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_CORNEA_ANTERIOR_ARRAY.size(); j++){
                        if(PATIENT_CORNEA_ANTERIOR_ARRAY.get(j).getCorneaAnteriorId() == 0 && PATIENT_CORNEA_ANTERIOR_ARRAY.get(j).getCorneaAnteriorName().equals(ant_name)) {
                            PATIENT_CORNEA_ANTERIOR_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        cornea_anterior_gridview_RE.addView(chk, 0);

        CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chkLE.setId(0);
        chkLE.setText(cornea_anterior_name);
        chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
        chkLE.setPadding(0,10,0,5);
        chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int ant_id = buttonView.getId();
                String ant_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " ant_id: "+ ant_id+" ant_name: "+ ant_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_CORNEA_ANTERIOR_ARRAY.add(new OphthalCornearAnteriorSurface(0,ant_name,USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_CORNEA_ANTERIOR_ARRAY.add(new OphthalCornearAnteriorSurface(0,ant_name, USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_CORNEA_ANTERIOR_ARRAY.size(); j++){
                        if(PATIENT_CORNEA_ANTERIOR_ARRAY.get(j).getCorneaAnteriorId() == 0 && PATIENT_CORNEA_ANTERIOR_ARRAY.get(j).getCorneaAnteriorName().equals(ant_name)) {
                            PATIENT_CORNEA_ANTERIOR_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        cornea_anterior_gridview_LE.addView(chkLE, 0);
        _edt_add_cornea_anterior.setText("");
    }

    private void refreshScleraContents(String sclera_name) {
        CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chk.setId(0);
        chk.setText(sclera_name);
        chk.setTextColor(getResources().getColor(R.color.textPrimary));
        chk.setPadding(0,10,0,5);
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int sclera_id = buttonView.getId();
                String sclera_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " sclera_id: "+ sclera_id+" sclera_name: "+ sclera_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_SCLERA_ARRAY.add(new OphthalSclera(0,sclera_name,USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_SCLERA_ARRAY.add(new OphthalSclera(0,sclera_name, USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_SCLERA_ARRAY.size(); j++){
                        if(PATIENT_SCLERA_ARRAY.get(j).getScleraId() == 0 && PATIENT_SCLERA_ARRAY.get(j).getScleraName().equals(sclera_name)) {
                            PATIENT_SCLERA_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        sclera_gridview_RE.addView(chk, 0);

        CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chkLE.setId(0);
        chkLE.setText(sclera_name);
        chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
        chkLE.setPadding(0,10,0,5);
        chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int sclera_id = buttonView.getId();
                String sclera_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " sclera_id: "+ sclera_id+" sclera_name: "+ sclera_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_SCLERA_ARRAY.add(new OphthalSclera(0,sclera_name,USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_SCLERA_ARRAY.add(new OphthalSclera(0,sclera_name, USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_SCLERA_ARRAY.size(); j++){
                        if(PATIENT_SCLERA_ARRAY.get(j).getScleraId() == 0 && PATIENT_SCLERA_ARRAY.get(j).getScleraName().equals(sclera_name)) {
                            PATIENT_SCLERA_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        sclera_gridview_LE.addView(chkLE, 0);
        _edt_add_sclera.setText("");
    }

    private void refreshConjuctivaContents(final String conjuctiva_name) {
        CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chk.setId(0);
        chk.setText(conjuctiva_name);
        chk.setTextColor(getResources().getColor(R.color.textPrimary));
        chk.setPadding(0,10,0,5);
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int conj_id = buttonView.getId();
                String conj_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " conj_id: "+ conj_id+" conj_name: "+ conj_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_CONJUCTIVA_ARRAY.add(new OphthalConjuctiva(0,conj_name,USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_CONJUCTIVA_ARRAY.add(new OphthalConjuctiva(0,conj_name, USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_CONJUCTIVA_ARRAY.size(); j++){
                        if(PATIENT_CONJUCTIVA_ARRAY.get(j).getConjuctivaId() == 0 && PATIENT_CONJUCTIVA_ARRAY.get(j).getConjuctivaName().equals(conjuctiva_name)) {
                            PATIENT_CONJUCTIVA_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        //  lids_gridview_RE.addView(chk);
        conjuctiva_gridview_RE.addView(chk, 0);

        CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chkLE.setId(0);
        chkLE.setText(conjuctiva_name);
        chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
        chkLE.setPadding(0,10,0,5);
        chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int conju_id = buttonView.getId();
                String conj_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " conju_id: "+ conju_id+" conju_name: "+ conj_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_CONJUCTIVA_ARRAY.add(new OphthalConjuctiva(0,conj_name,USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_CONJUCTIVA_ARRAY.add(new OphthalConjuctiva(0,conj_name, USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_CONJUCTIVA_ARRAY.size(); j++){
                        if(PATIENT_CONJUCTIVA_ARRAY.get(j).getConjuctivaId() == 0 && PATIENT_CONJUCTIVA_ARRAY.get(j).getConjuctivaName().equals(conj_name)) {
                            PATIENT_CONJUCTIVA_ARRAY.remove(j);
                        }
                    }
                }
            }
        });

        conjuctiva_gridview_LE.addView(chkLE, 0);

        _edt_add_conjuctiva.setText("");
    }

    private void refreshLidsContents(String lids_name) {
        CheckBox chk=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chk.setId(0);
        chk.setText(lids_name);
        chk.setTextColor(getResources().getColor(R.color.textPrimary));
        chk.setPadding(0,10,0,5);
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int lids_id = buttonView.getId();
                String lids_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " lids_id: "+ lids_id+" lids_name: "+ lids_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_LIDS_ARRAY.add(new Lids(0,lids_name,USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_LIDS_ARRAY.add(new Lids(0,lids_name, USER_ID,1,"0","1",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_LIDS_ARRAY.size(); j++){
                        if(PATIENT_LIDS_ARRAY.get(j).getLidsId() == 0 && PATIENT_LIDS_ARRAY.get(j).getLidsName().equals(lids_name)) {
                            PATIENT_LIDS_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        //  lids_gridview_RE.addView(chk);
        lids_gridview_RE.addView(chk, 0);

        CheckBox chkLE=new CheckBox(EditOphthalVisitDetailsActivity.this);
        chkLE.setId(0);
        chkLE.setText(lids_name);
        chkLE.setTextColor(getResources().getColor(R.color.textPrimary));
        chkLE.setPadding(0,10,0,5);
        chkLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                int lids_id = buttonView.getId();
                String lids_name = buttonView.getText().toString();
                Log.d(Utils.TAG, " lids_id: "+ lids_id+" lids_name: "+ lids_name);
                Log.d(Utils.TAG, (isChecked ? "1" : "0"));
                if(isChecked) {
                    Log.d(Utils.TAG, " isChecked: "+ isChecked);
                    PATIENT_LIDS_ARRAY.add(new Lids(0,lids_name,USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                }
                else {
                    Log.d(Utils.TAG, " not Checked: "+ isChecked);
                    PATIENT_LIDS_ARRAY.add(new Lids(0,lids_name, USER_ID,1,"2","0",USER_ID,USER_LOGIN_TYPE));
                    for(int j=0; j<PATIENT_LIDS_ARRAY.size(); j++){
                        if(PATIENT_LIDS_ARRAY.get(j).getLidsId() == 0 && PATIENT_LIDS_ARRAY.get(j).getLidsName().equals(lids_name)) {
                            PATIENT_LIDS_ARRAY.remove(j);
                        }
                    }
                }
            }
        });
        // lids_gridview_LE.addView(chkLE);
        lids_gridview_LE.addView(chkLE, 0);

        _edt_add_lids.setText("");
    }

    private void submitAddVisitValidationForm() {

        Log.d(Utils.TAG, " ********** submitAddVisitValidationForm *************** ");
        Log.d(Utils.TAG, " chiedMedComp size: "+ PATIENT_CHIEF_MEDCOMPLAINT_ARRAY.size());
        for(int i=0;i<PATIENT_CHIEF_MEDCOMPLAINT_ARRAY.size();i++)
        {
            Log.d(Utils.TAG, " compID: "+ PATIENT_CHIEF_MEDCOMPLAINT_ARRAY.get(i).getComplaintId());
            Log.d(Utils.TAG, " compName: "+ PATIENT_CHIEF_MEDCOMPLAINT_ARRAY.get(i).getSymptomsName());
            Log.d(Utils.TAG, " docID: "+ PATIENT_CHIEF_MEDCOMPLAINT_ARRAY.get(i).getDocId());
            Log.d(Utils.TAG, " docType: "+ PATIENT_CHIEF_MEDCOMPLAINT_ARRAY.get(i).getDocType());
            Log.d(Utils.TAG, " userid: "+ PATIENT_CHIEF_MEDCOMPLAINT_ARRAY.get(i).getUserId());
            Log.d(Utils.TAG, " logintype: "+ PATIENT_CHIEF_MEDCOMPLAINT_ARRAY.get(i).getLoginType());
        }

        Log.d(Utils.TAG, " **************** EXAMINATIONS RESULTS*********************** ");
        Log.d(Utils.TAG, " distanceVisionRE: "+ PATIENT_DISTANCE_VISION_RE);
        Log.d(Utils.TAG, " distanceVisionLE: "+ PATIENT_DISTANCE_VISION_LE);
        Log.d(Utils.TAG, " nearVisionRE: "+ PATIENT_NEAR_VISION_RE);
        Log.d(Utils.TAG, " nearVisionLE: "+ PATIENT_NEAR_VISION_LE);

        Log.d(Utils.TAG, " PATIENT_DISTANCE_VISION_UNAIDED_RE: "+ PATIENT_DISTANCE_VISION_UNAIDED_RE);
        Log.d(Utils.TAG, " PATIENT_DISTANCE_VISION_UNAIDED_LE: "+ PATIENT_DISTANCE_VISION_UNAIDED_LE);
        Log.d(Utils.TAG, " PATIENT_NEAR_VISION_UNAIDED_RE: "+ PATIENT_NEAR_VISION_UNAIDED_RE);
        Log.d(Utils.TAG, " PATIENT_NEAR_VISION_UNAIDED_LE: "+ PATIENT_NEAR_VISION_UNAIDED_LE);
        Log.d(Utils.TAG, " PATIENT_IOP_RE: "+ PATIENT_IOP_RE);
        Log.d(Utils.TAG, " PATIENT_IOP_LE: "+ PATIENT_IOP_LE);

        Log.d(Utils.TAG, " PATIENT_LIDS_ARRAY: "+ PATIENT_LIDS_ARRAY.size());
        if(PATIENT_LIDS_ARRAY.size() > 0 && PATIENT_LIDS_ARRAY != null) {
            for(int i=0;i<PATIENT_LIDS_ARRAY.size();i++) {
                Log.d(Utils.TAG, " lidsID: "+ PATIENT_LIDS_ARRAY.get(i).getLidsId());
                Log.d(Utils.TAG, " lidsName: "+ PATIENT_LIDS_ARRAY.get(i).getLidsName());
                Log.d(Utils.TAG, " docID: "+ PATIENT_LIDS_ARRAY.get(i).getDocId());
                Log.d(Utils.TAG, " docType: "+ PATIENT_LIDS_ARRAY.get(i).getDocType());
                Log.d(Utils.TAG, " leftEye: "+ PATIENT_LIDS_ARRAY.get(i).getLeftEye());
                Log.d(Utils.TAG, " rightEye: "+ PATIENT_LIDS_ARRAY.get(i).getRightEye());
                Log.d(Utils.TAG, " userID: "+ PATIENT_LIDS_ARRAY.get(i).getUserId());
                Log.d(Utils.TAG, " loginType: "+ PATIENT_LIDS_ARRAY.get(i).getLoginType());
            }
        }

        Log.d(Utils.TAG, " PATIENT_CONJUCTIVA_ARRAY: "+ PATIENT_CONJUCTIVA_ARRAY.size());
        if(PATIENT_CONJUCTIVA_ARRAY.size() > 0 && PATIENT_CONJUCTIVA_ARRAY != null) {
            for(int i=0;i<PATIENT_CONJUCTIVA_ARRAY.size();i++) {
                Log.d(Utils.TAG, " conjuctivaID: "+ PATIENT_CONJUCTIVA_ARRAY.get(i).getConjuctivaId());
                Log.d(Utils.TAG, " conjuctivaName: "+ PATIENT_CONJUCTIVA_ARRAY.get(i).getConjuctivaName());
                Log.d(Utils.TAG, " docID: "+ PATIENT_CONJUCTIVA_ARRAY.get(i).getDocId());
                Log.d(Utils.TAG, " docType: "+ PATIENT_CONJUCTIVA_ARRAY.get(i).getDocType());
                Log.d(Utils.TAG, " leftEye: "+ PATIENT_CONJUCTIVA_ARRAY.get(i).getLeftEye());
                Log.d(Utils.TAG, " rightEye: "+ PATIENT_CONJUCTIVA_ARRAY.get(i).getRightEye());
                Log.d(Utils.TAG, " userID: "+ PATIENT_CONJUCTIVA_ARRAY.get(i).getUserId());
                Log.d(Utils.TAG, " loginType: "+ PATIENT_CONJUCTIVA_ARRAY.get(i).getLoginType());
            }
        }

        Log.d(Utils.TAG, " PATIENT_SCLERA_ARRAY: "+ PATIENT_SCLERA_ARRAY.size());
        if(PATIENT_SCLERA_ARRAY.size() > 0 && PATIENT_SCLERA_ARRAY != null) {
            for(int i=0;i<PATIENT_SCLERA_ARRAY.size();i++) {
                Log.d(Utils.TAG, " scleraID: "+ PATIENT_SCLERA_ARRAY.get(i).getScleraId());
                Log.d(Utils.TAG, " scleraName: "+ PATIENT_SCLERA_ARRAY.get(i).getScleraName());
                Log.d(Utils.TAG, " docID: "+ PATIENT_SCLERA_ARRAY.get(i).getDocId());
                Log.d(Utils.TAG, " docType: "+ PATIENT_SCLERA_ARRAY.get(i).getDocType());
                Log.d(Utils.TAG, " leftEye: "+ PATIENT_SCLERA_ARRAY.get(i).getLeftEye());
                Log.d(Utils.TAG, " rightEye: "+ PATIENT_SCLERA_ARRAY.get(i).getRightEye());
                Log.d(Utils.TAG, " userID: "+ PATIENT_SCLERA_ARRAY.get(i).getUserId());
                Log.d(Utils.TAG, " loginType: "+ PATIENT_SCLERA_ARRAY.get(i).getLoginType());
            }
        }

        Log.d(Utils.TAG, " PATIENT_CORNEA_ANTERIOR_ARRAY: "+ PATIENT_CORNEA_ANTERIOR_ARRAY.size());
        if(PATIENT_CORNEA_ANTERIOR_ARRAY.size() > 0 && PATIENT_CORNEA_ANTERIOR_ARRAY != null) {
            for(int i=0;i<PATIENT_CORNEA_ANTERIOR_ARRAY.size();i++) {
                Log.d(Utils.TAG, " corneaAntID: "+ PATIENT_CORNEA_ANTERIOR_ARRAY.get(i).getCorneaAnteriorId());
                Log.d(Utils.TAG, " corneaAntName: "+ PATIENT_CORNEA_ANTERIOR_ARRAY.get(i).getCorneaAnteriorName());
                Log.d(Utils.TAG, " docID: "+ PATIENT_CORNEA_ANTERIOR_ARRAY.get(i).getDocId());
                Log.d(Utils.TAG, " docType: "+ PATIENT_CORNEA_ANTERIOR_ARRAY.get(i).getDocType());
                Log.d(Utils.TAG, " leftEye: "+ PATIENT_CORNEA_ANTERIOR_ARRAY.get(i).getLeftEye());
                Log.d(Utils.TAG, " rightEye: "+ PATIENT_CORNEA_ANTERIOR_ARRAY.get(i).getRightEye());
                Log.d(Utils.TAG, " userID: "+ PATIENT_CORNEA_ANTERIOR_ARRAY.get(i).getUserId());
                Log.d(Utils.TAG, " loginType: "+ PATIENT_CORNEA_ANTERIOR_ARRAY.get(i).getLoginType());
            }
        }

        Log.d(Utils.TAG, " PATIENT_CORNEA_POSTERIOR_ARRAY: "+ PATIENT_CORNEA_POSTERIOR_ARRAY.size());
        if(PATIENT_CORNEA_POSTERIOR_ARRAY.size() > 0 && PATIENT_CORNEA_POSTERIOR_ARRAY != null) {
            for(int i=0;i<PATIENT_CORNEA_POSTERIOR_ARRAY.size();i++) {
                Log.d(Utils.TAG, " corneaPostID: "+ PATIENT_CORNEA_POSTERIOR_ARRAY.get(i).getCorneaPosteriorId());
                Log.d(Utils.TAG, " corneaPostName: "+ PATIENT_CORNEA_POSTERIOR_ARRAY.get(i).getCorneaPosteriorName());
                Log.d(Utils.TAG, " docID: "+ PATIENT_CORNEA_POSTERIOR_ARRAY.get(i).getDocId());
                Log.d(Utils.TAG, " docType: "+ PATIENT_CORNEA_POSTERIOR_ARRAY.get(i).getDocType());
                Log.d(Utils.TAG, " leftEye: "+ PATIENT_CORNEA_POSTERIOR_ARRAY.get(i).getLeftEye());
                Log.d(Utils.TAG, " rightEye: "+ PATIENT_CORNEA_POSTERIOR_ARRAY.get(i).getRightEye());
                Log.d(Utils.TAG, " userID: "+ PATIENT_CORNEA_POSTERIOR_ARRAY.get(i).getUserId());
                Log.d(Utils.TAG, " loginType: "+ PATIENT_CORNEA_POSTERIOR_ARRAY.get(i).getLoginType());
            }
        }

        Log.d(Utils.TAG, " PATIENT_ANTERIOR_CHAMBER_ARRAY: "+ PATIENT_ANTERIOR_CHAMBER_ARRAY.size());
        if(PATIENT_ANTERIOR_CHAMBER_ARRAY.size() > 0 && PATIENT_ANTERIOR_CHAMBER_ARRAY != null) {
            for(int i=0;i<PATIENT_ANTERIOR_CHAMBER_ARRAY.size();i++) {
                Log.d(Utils.TAG, " chamberID: "+ PATIENT_ANTERIOR_CHAMBER_ARRAY.get(i).getAnteriorChamberId());
                Log.d(Utils.TAG, " chamberName: "+ PATIENT_ANTERIOR_CHAMBER_ARRAY.get(i).getAnteriorChamberName());
                Log.d(Utils.TAG, " docID: "+ PATIENT_ANTERIOR_CHAMBER_ARRAY.get(i).getDocId());
                Log.d(Utils.TAG, " docType: "+ PATIENT_ANTERIOR_CHAMBER_ARRAY.get(i).getDocType());
                Log.d(Utils.TAG, " leftEye: "+ PATIENT_ANTERIOR_CHAMBER_ARRAY.get(i).getLeftEye());
                Log.d(Utils.TAG, " rightEye: "+ PATIENT_ANTERIOR_CHAMBER_ARRAY.get(i).getRightEye());
                Log.d(Utils.TAG, " userID: "+ PATIENT_ANTERIOR_CHAMBER_ARRAY.get(i).getUserId());
                Log.d(Utils.TAG, " loginType: "+ PATIENT_ANTERIOR_CHAMBER_ARRAY.get(i).getLoginType());
            }
        }

        Log.d(Utils.TAG, " PATIENT_IRIS_ARRAY: "+ PATIENT_IRIS_ARRAY.size());
        if(PATIENT_IRIS_ARRAY.size() > 0 && PATIENT_IRIS_ARRAY != null) {
            for(int i=0;i<PATIENT_IRIS_ARRAY.size();i++) {
                Log.d(Utils.TAG, " irisID: "+ PATIENT_IRIS_ARRAY.get(i).getIrisId());
                Log.d(Utils.TAG, " irisName: "+ PATIENT_IRIS_ARRAY.get(i).getIrisName());
                Log.d(Utils.TAG, " docID: "+ PATIENT_IRIS_ARRAY.get(i).getDocId());
                Log.d(Utils.TAG, " docType: "+ PATIENT_IRIS_ARRAY.get(i).getDocType());
                Log.d(Utils.TAG, " leftEye: "+ PATIENT_IRIS_ARRAY.get(i).getLeftEye());
                Log.d(Utils.TAG, " rightEye: "+ PATIENT_IRIS_ARRAY.get(i).getRightEye());
                Log.d(Utils.TAG, " userID: "+ PATIENT_IRIS_ARRAY.get(i).getUserId());
                Log.d(Utils.TAG, " loginType: "+ PATIENT_IRIS_ARRAY.get(i).getLoginType());
            }
        }

        Log.d(Utils.TAG, " PATIENT_PUPIL_ARRAY: "+ PATIENT_PUPIL_ARRAY.size());
        if(PATIENT_PUPIL_ARRAY.size() > 0 && PATIENT_PUPIL_ARRAY != null) {
            for(int i=0;i<PATIENT_PUPIL_ARRAY.size();i++) {
                Log.d(Utils.TAG, " pupilID: "+ PATIENT_PUPIL_ARRAY.get(i).getPupilId());
                Log.d(Utils.TAG, " pupilName: "+ PATIENT_PUPIL_ARRAY.get(i).getPupilName());
                Log.d(Utils.TAG, " docID: "+ PATIENT_PUPIL_ARRAY.get(i).getDocId());
                Log.d(Utils.TAG, " docType: "+ PATIENT_PUPIL_ARRAY.get(i).getDocType());
                Log.d(Utils.TAG, " leftEye: "+ PATIENT_PUPIL_ARRAY.get(i).getLeftEye());
                Log.d(Utils.TAG, " rightEye: "+ PATIENT_PUPIL_ARRAY.get(i).getRightEye());
                Log.d(Utils.TAG, " userID: "+ PATIENT_PUPIL_ARRAY.get(i).getUserId());
                Log.d(Utils.TAG, " loginType: "+ PATIENT_PUPIL_ARRAY.get(i).getLoginType());
            }
        }

        Log.d(Utils.TAG, " PATIENT_ANGLE_ARRAY: "+ PATIENT_ANGLE_ARRAY.size());
        if(PATIENT_ANGLE_ARRAY.size() > 0 && PATIENT_ANGLE_ARRAY != null) {
            for(int i=0;i<PATIENT_ANGLE_ARRAY.size();i++) {
                Log.d(Utils.TAG, " angleID: "+ PATIENT_ANGLE_ARRAY.get(i).getAngleId());
                Log.d(Utils.TAG, " angleName: "+ PATIENT_ANGLE_ARRAY.get(i).getAngleName());
                Log.d(Utils.TAG, " docID: "+ PATIENT_ANGLE_ARRAY.get(i).getDocId());
                Log.d(Utils.TAG, " docType: "+ PATIENT_ANGLE_ARRAY.get(i).getDocType());
                Log.d(Utils.TAG, " leftEye: "+ PATIENT_ANGLE_ARRAY.get(i).getLeftEye());
                Log.d(Utils.TAG, " rightEye: "+ PATIENT_ANGLE_ARRAY.get(i).getRightEye());
                Log.d(Utils.TAG, " userID: "+ PATIENT_ANGLE_ARRAY.get(i).getUserId());
                Log.d(Utils.TAG, " loginType: "+ PATIENT_ANGLE_ARRAY.get(i).getLoginType());
            }
        }

        Log.d(Utils.TAG, " PATIENT_LENS_ARRAY: "+ PATIENT_LENS_ARRAY.size());
        if(PATIENT_LENS_ARRAY.size() > 0 && PATIENT_LENS_ARRAY != null) {
            for(int i=0;i<PATIENT_LENS_ARRAY.size();i++) {
                Log.d(Utils.TAG, " lensID: "+ PATIENT_LENS_ARRAY.get(i).getLensId());
                Log.d(Utils.TAG, " lensName: "+ PATIENT_LENS_ARRAY.get(i).getLensName());
                Log.d(Utils.TAG, " docID: "+ PATIENT_LENS_ARRAY.get(i).getDocId());
                Log.d(Utils.TAG, " docType: "+ PATIENT_LENS_ARRAY.get(i).getDocType());
                Log.d(Utils.TAG, " leftEye: "+ PATIENT_LENS_ARRAY.get(i).getLeftEye());
                Log.d(Utils.TAG, " rightEye: "+ PATIENT_LENS_ARRAY.get(i).getRightEye());
                Log.d(Utils.TAG, " userID: "+ PATIENT_LENS_ARRAY.get(i).getUserId());
                Log.d(Utils.TAG, " loginType: "+ PATIENT_LENS_ARRAY.get(i).getLoginType());
            }
        }

        Log.d(Utils.TAG, " PATIENT_VITEROUS_ARRAY: "+ PATIENT_VITEROUS_ARRAY.size());
        if(PATIENT_VITEROUS_ARRAY.size() > 0 && PATIENT_VITEROUS_ARRAY != null) {
            for(int i=0;i<PATIENT_VITEROUS_ARRAY.size();i++) {
                Log.d(Utils.TAG, " viterousID: "+ PATIENT_VITEROUS_ARRAY.get(i).getViterousId());
                Log.d(Utils.TAG, " viterousName: "+ PATIENT_VITEROUS_ARRAY.get(i).getViterousName());
                Log.d(Utils.TAG, " docID: "+ PATIENT_VITEROUS_ARRAY.get(i).getDocId());
                Log.d(Utils.TAG, " docType: "+ PATIENT_VITEROUS_ARRAY.get(i).getDocType());
                Log.d(Utils.TAG, " leftEye: "+ PATIENT_VITEROUS_ARRAY.get(i).getLeftEye());
                Log.d(Utils.TAG, " rightEye: "+ PATIENT_VITEROUS_ARRAY.get(i).getRightEye());
                Log.d(Utils.TAG, " userID: "+ PATIENT_VITEROUS_ARRAY.get(i).getUserId());
                Log.d(Utils.TAG, " loginType: "+ PATIENT_VITEROUS_ARRAY.get(i).getLoginType());
            }
        }

        Log.d(Utils.TAG, " PATIENT_FUNDUS_ARRAY: "+ PATIENT_FUNDUS_ARRAY.size());
        if(PATIENT_FUNDUS_ARRAY.size() > 0 && PATIENT_FUNDUS_ARRAY != null) {
            for(int i=0;i<PATIENT_FUNDUS_ARRAY.size();i++) {
                Log.d(Utils.TAG, " fundusID: "+ PATIENT_FUNDUS_ARRAY.get(i).getFundusId());
                Log.d(Utils.TAG, " fundusName: "+ PATIENT_FUNDUS_ARRAY.get(i).getFundusName());
                Log.d(Utils.TAG, " docID: "+ PATIENT_FUNDUS_ARRAY.get(i).getDocId());
                Log.d(Utils.TAG, " docType: "+ PATIENT_FUNDUS_ARRAY.get(i).getDocType());
                Log.d(Utils.TAG, " leftEye: "+ PATIENT_FUNDUS_ARRAY.get(i).getLeftEye());
                Log.d(Utils.TAG, " rightEye: "+ PATIENT_FUNDUS_ARRAY.get(i).getRightEye());
                Log.d(Utils.TAG, " userID: "+ PATIENT_FUNDUS_ARRAY.get(i).getUserId());
                Log.d(Utils.TAG, " loginType: "+ PATIENT_FUNDUS_ARRAY.get(i).getLoginType());
            }
        }

        String refraction_RE_top = _edt_refraction_RE_top.getText().toString();
        String refraction_RE_bottom = _edt_refraction_RE_bottom.getText().toString();
        String refraction_LE_top = _edt_refraction_LE_top.getText().toString();
        String refraction_LE_bottom = _edt_refraction_LE_bottom.getText().toString();

        Log.d(Utils.TAG, " *************************************** ");
        PATIENT_INVESTIGATION_ARRAY = new ArrayList<>();
        Log.d(Utils.TAG, " investigation size init: "+ PATIENT_INVESTIGATION_ARRAY.size());
        Log.d(Utils.TAG, " investigationGeneral size: "+ PATIENT_INVESTIGATION_GENERAL_ARRAY.size());
        Log.d(Utils.TAG, " investigationOphthal size: "+ PATIENT_INVESTIGATION_OPHTHAL_ARRAY.size());

        for(int i=0;i<PATIENT_INVESTIGATION_GENERAL_ARRAY.size();i++)
        {
            PATIENT_INVESTIGATION_ARRAY.add(new Investigations(PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getInvestigationId(),
                    PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getTestId(),PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getGroupTestId(),PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getTestName(),
                    PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getNormalRange(),PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getTestActualValue(),
                    PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getRightEye(),PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getLeftEye(),PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getInvestDepartment(),
                    PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getUserId(),PATIENT_INVESTIGATION_GENERAL_ARRAY.get(i).getLoginType()));

        }
        for(int i=0;i<PATIENT_INVESTIGATION_OPHTHAL_ARRAY.size();i++)
        {
            PATIENT_INVESTIGATION_ARRAY.add(new Investigations(PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getInvestigationId(),
                    PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getTestId(),PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getGroupTestId(),PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getTestName(),
                    PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getNormalRange(),PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getTestActualValue(),
                    PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getRightEye(),PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getLeftEye(),PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getInvestDepartment(),
                    PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getUserId(),PATIENT_INVESTIGATION_OPHTHAL_ARRAY.get(i).getLoginType()));

        }


        for(int i=0;i<PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.size();i++) {
            PATIENT_INVESTIGATION_ARRAY.add(new Investigations(PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.get(i).getInvestigationId(),
                    PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.get(i).getTestId(),PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.get(i).getGroupTestId(),PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.get(i).getTestName(),
                    PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.get(i).getNormalRange(),PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.get(i).getTestActualValue(),
                    PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.get(i).getRightEye(),PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.get(i).getLeftEye(),PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.get(i).getInvestDepartment(),
                    PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.get(i).getUserId(),PATIENT_INVESTIGATION_RADIOLOGY_ARRAY.get(i).getLoginType()));

        }

        for(int i=0;i<PATIENT_INVESTIGATION_CLINICAL_ARRAY.size();i++) {
            PATIENT_INVESTIGATION_ARRAY.add(new Investigations(PATIENT_INVESTIGATION_CLINICAL_ARRAY.get(i).getInvestigationId(),
                    PATIENT_INVESTIGATION_CLINICAL_ARRAY.get(i).getTestId(),PATIENT_INVESTIGATION_CLINICAL_ARRAY.get(i).getGroupTestId(),PATIENT_INVESTIGATION_CLINICAL_ARRAY.get(i).getTestName(),
                    PATIENT_INVESTIGATION_CLINICAL_ARRAY.get(i).getNormalRange(),PATIENT_INVESTIGATION_CLINICAL_ARRAY.get(i).getTestActualValue(),
                    PATIENT_INVESTIGATION_CLINICAL_ARRAY.get(i).getRightEye(),PATIENT_INVESTIGATION_CLINICAL_ARRAY.get(i).getLeftEye(),PATIENT_INVESTIGATION_CLINICAL_ARRAY.get(i).getInvestDepartment(),
                    PATIENT_INVESTIGATION_CLINICAL_ARRAY.get(i).getUserId(),PATIENT_INVESTIGATION_CLINICAL_ARRAY.get(i).getLoginType()));

        }

        for(int i=0;i<PATIENT_INVESTIGATION_OTHER_ARRAY.size();i++) {
            PATIENT_INVESTIGATION_ARRAY.add(new Investigations(PATIENT_INVESTIGATION_OTHER_ARRAY.get(i).getInvestigationId(),
                    PATIENT_INVESTIGATION_OTHER_ARRAY.get(i).getTestId(),PATIENT_INVESTIGATION_OTHER_ARRAY.get(i).getGroupTestId(),PATIENT_INVESTIGATION_OTHER_ARRAY.get(i).getTestName(),
                    PATIENT_INVESTIGATION_OTHER_ARRAY.get(i).getNormalRange(),PATIENT_INVESTIGATION_OTHER_ARRAY.get(i).getTestActualValue(),
                    PATIENT_INVESTIGATION_OTHER_ARRAY.get(i).getRightEye(),PATIENT_INVESTIGATION_OTHER_ARRAY.get(i).getLeftEye(),PATIENT_INVESTIGATION_OTHER_ARRAY.get(i).getInvestDepartment(),
                    PATIENT_INVESTIGATION_OTHER_ARRAY.get(i).getUserId(),PATIENT_INVESTIGATION_OTHER_ARRAY.get(i).getLoginType()));

        }

        Log.d(Utils.TAG, " investigation size: "+ PATIENT_INVESTIGATION_ARRAY.size());
        for(int i=0;i<PATIENT_INVESTIGATION_ARRAY.size();i++)
        {
            Log.d(Utils.TAG, " investID: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getInvestigationId());
            Log.d(Utils.TAG, " testID: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getTestId());
            Log.d(Utils.TAG, " grouptestID: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getGroupTestId());
            Log.d(Utils.TAG, " testName: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getTestName());
            Log.d(Utils.TAG, " groupTest: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getGroupTest());
            Log.d(Utils.TAG, " mfRange: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getMFRange());
            Log.d(Utils.TAG, " normalRange: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getNormalRange());
            Log.d(Utils.TAG, " actualValue: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getTestActualValue());
            Log.d(Utils.TAG, " rightEye: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getRightEye());
            Log.d(Utils.TAG, " leftEye: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getLeftEye());
            Log.d(Utils.TAG, " department: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getInvestDepartment());
            Log.d(Utils.TAG, " userid: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getUserId());
            Log.d(Utils.TAG, " logintype: "+ PATIENT_INVESTIGATION_ARRAY.get(i).getLoginType());
        }

        Log.d(Utils.TAG, " *************************************** ");
        Log.d(Utils.TAG, " diagnosis size: "+ PATIENT_DAIGNOSIS_ARRAY.size());
        for(int i=0;i<PATIENT_DAIGNOSIS_ARRAY.size();i++)
        {
            Log.d(Utils.TAG, " freqID: "+ PATIENT_DAIGNOSIS_ARRAY.get(i).getDiagnoFreqId());
            Log.d(Utils.TAG, " icdID: "+ PATIENT_DAIGNOSIS_ARRAY.get(i).getICDId());
            Log.d(Utils.TAG, " icdName: "+ PATIENT_DAIGNOSIS_ARRAY.get(i).getICDName());
            Log.d(Utils.TAG, " docID: "+ PATIENT_DAIGNOSIS_ARRAY.get(i).getDocId());
            Log.d(Utils.TAG, " docType: "+ PATIENT_DAIGNOSIS_ARRAY.get(i).getDocType());
            Log.d(Utils.TAG, " freqCount: "+ PATIENT_DAIGNOSIS_ARRAY.get(i).getFreqCount());
            Log.d(Utils.TAG, " userid: "+ PATIENT_DAIGNOSIS_ARRAY.get(i).getUserId());
            Log.d(Utils.TAG, " logintype: "+ PATIENT_DAIGNOSIS_ARRAY.get(i).getLoginType());
        }
        Log.d(Utils.TAG, " *************************************** ");
        Log.d(Utils.TAG, " treatment size: "+ PATIENT_TREATMENT_ARRAY.size());
        for(int i=0;i<PATIENT_TREATMENT_ARRAY.size();i++)
        {
            Log.d(Utils.TAG, " treatID: "+ PATIENT_TREATMENT_ARRAY.get(i).getTreatmentID());
            Log.d(Utils.TAG, " treatName: "+ PATIENT_TREATMENT_ARRAY.get(i).getTreatmentName());
            Log.d(Utils.TAG, " docID: "+ PATIENT_TREATMENT_ARRAY.get(i).getTreatmentDocID());
            Log.d(Utils.TAG, " docType: "+ PATIENT_TREATMENT_ARRAY.get(i).getTreatmentDocType());
            Log.d(Utils.TAG, " freqCount: "+ PATIENT_TREATMENT_ARRAY.get(i).getTreatmentFreqCount());
            Log.d(Utils.TAG, " userid: "+ PATIENT_TREATMENT_ARRAY.get(i).getTreatmentUserID());
            Log.d(Utils.TAG, " logintype: "+ PATIENT_TREATMENT_ARRAY.get(i).getTreatmentLoginType());
        }
        Log.d(Utils.TAG, " *************************************** ");
        Log.d(Utils.TAG, " prescription size: "+ PATIENT_PRESCRIPTION_ARRAY.size());
        for(int i=0;i<PATIENT_PRESCRIPTION_ARRAY.size();i++)
        {
            Log.d(Utils.TAG, " freqID: "+ PATIENT_PRESCRIPTION_ARRAY.get(i).getPrescFreqId());
            Log.d(Utils.TAG, " prescID: "+ PATIENT_PRESCRIPTION_ARRAY.get(i).getPrescriptionId());
            Log.d(Utils.TAG, " tradeName: "+ PATIENT_PRESCRIPTION_ARRAY.get(i).getTradeName());
            Log.d(Utils.TAG, " genericID: "+ PATIENT_PRESCRIPTION_ARRAY.get(i).getGenericId());
            Log.d(Utils.TAG, " genricName: "+ PATIENT_PRESCRIPTION_ARRAY.get(i).getGenericName());
            Log.d(Utils.TAG, " dosage: "+ PATIENT_PRESCRIPTION_ARRAY.get(i).getDosage());
            Log.d(Utils.TAG, " timings: "+ PATIENT_PRESCRIPTION_ARRAY.get(i).getTimings());
            Log.d(Utils.TAG, " duration: "+ PATIENT_PRESCRIPTION_ARRAY.get(i).getDuration());
            Log.d(Utils.TAG, " userid: "+ PATIENT_PRESCRIPTION_ARRAY.get(i).getUserId());
            Log.d(Utils.TAG, " logintype: "+ PATIENT_PRESCRIPTION_ARRAY.get(i).getLoginType());
        }

        String conultationFees = _edt_consultency_fee.getText().toString();
        String followupdate = _edt_followup_date.getText().toString();
        String diagnosis_details = _edt_diagnosis_deatils.getText().toString();
        String treatment_details = _edt_treatment_details.getText().toString();
        String prescription_note = _edt_prescription_note.getText().toString();
        String visit_entry_date = _edt_visit_date.getText().toString();

        Log.d(Utils.TAG, " *************************************** ");
        Log.d(Utils.TAG, " SPECTACLE PRESCRIPTION  ");
        String GET_DV_SPHERE_RIGHT = _edt_right_dv_sphere.getText().toString().trim();
        String GET_DV_CYL_RIGHT = _edt_right_dv_cyl.getText().toString().trim();
        String GET_DV_AXIS_RIGHT = _edt_right_dv_axis.getText().toString().trim();
        String GET_NV_SPHERE_RIGHT = _edt_right_nv_sphere.getText().toString().trim();
        String GET_NV_CYL_RIGHT = _edt_right_nv_cyl.getText().toString().trim();
        String GET_NV_AXIS_RIGHT = _edt_right_nv_axis.getText().toString().trim();
        String GET_IPD_RIGHT = _edt_right_ipd.getText().toString().trim();

        String GET_DV_SPHERE_LEFT = _edt_left_dv_sphere.getText().toString().trim();
        String GET_DV_CYL_LEFT = _edt_left_dv_cyl.getText().toString().trim();
        String GET_DV_AXIS_LEFT = _edt_left_dv_axis.getText().toString().trim();
        String GET_NV_SPHERE_LEFT = _edt_left_nv_sphere.getText().toString().trim();
        String GET_NV_CYL_LEFT = _edt_left_nv_cyl.getText().toString().trim();
        String GET_NV_AXIS_LEFT = _edt_left_nv_axis.getText().toString().trim();
        String GET_IPD_LEFT = _edt_left_ipd.getText().toString().trim();

        String visit_chiefMedComplaint_sufferings = chiefMedComplaint_sufferings.getText().toString();

        Log.d(Utils.TAG, " investigation_template_save: "+ investigation_template_save);
        String investigation_template_name = investigation_addtemplate_name.getText().toString();
        Log.d(Utils.TAG, " investigation_template_name: "+ investigation_template_name);

        Log.d(Utils.TAG, " PATIENT_EDUCATION: "+ PATIENT_EDUCATION);

        if(sharedPreferences != null) {
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
        }

        sendOphthalUpdateVisitRequestToServer(PATIENT_CHIEF_MEDCOMPLAINT_ARRAY, PATIENT_DISTANCE_VISION_RE, PATIENT_DISTANCE_VISION_LE,
                PATIENT_NEAR_VISION_RE, PATIENT_NEAR_VISION_LE, PATIENT_LIDS_ARRAY, PATIENT_CONJUCTIVA_ARRAY, PATIENT_SCLERA_ARRAY,
                PATIENT_CORNEA_ANTERIOR_ARRAY, PATIENT_CORNEA_POSTERIOR_ARRAY, PATIENT_ANTERIOR_CHAMBER_ARRAY, PATIENT_IRIS_ARRAY,
                PATIENT_PUPIL_ARRAY, PATIENT_ANGLE_ARRAY, PATIENT_LENS_ARRAY, PATIENT_VITEROUS_ARRAY, PATIENT_FUNDUS_ARRAY,
                refraction_RE_top, refraction_RE_bottom,refraction_LE_top,refraction_LE_bottom,
                PATIENT_INVESTIGATION_ARRAY, PATIENT_DAIGNOSIS_ARRAY,PATIENT_TREATMENT_ARRAY,PATIENT_PRESCRIPTION_ARRAY,
                conultationFees,get_followup_date,diagnosis_details, treatment_details,prescription_note,visit_entry_date,
                GET_DV_SPHERE_RIGHT, GET_DV_CYL_RIGHT, GET_DV_AXIS_RIGHT, GET_NV_SPHERE_RIGHT, GET_NV_CYL_RIGHT,
                GET_NV_AXIS_RIGHT,GET_IPD_RIGHT,GET_DV_SPHERE_LEFT, GET_DV_CYL_LEFT, GET_DV_AXIS_LEFT,
                GET_NV_SPHERE_LEFT, GET_NV_CYL_LEFT, GET_NV_AXIS_LEFT,GET_IPD_LEFT,
                HOSPITAL_ID, visit_chiefMedComplaint_sufferings, investigation_template_save, investigation_template_name, PATIENT_EDUCATION,
                PATIENT_DISTANCE_VISION_UNAIDED_RE, PATIENT_DISTANCE_VISION_UNAIDED_LE, PATIENT_NEAR_VISION_UNAIDED_RE, PATIENT_NEAR_VISION_UNAIDED_LE,
                PATIENT_IOP_RE, PATIENT_IOP_LE);
    }


    private int getIndex(Spinner timings_spinner, String compareValue) {
        for (int i=0;i<timings_spinner.getCount();i++){
            if (timings_spinner.getItemAtPosition(i).toString().equalsIgnoreCase(compareValue)){
                return i;
            }
        }
        return 0;
    }

    private void sendOphthalUpdateVisitRequestToServer(final List<ChiefMedicalComplaint> patient_chief_medcomplaint_array,
                                                       final String distanceVision_RE, final String distanceVision_LE, final String nearVision_RE, final String nearVision_LE,
                                                       final List<Lids> patient_lids_array, final List<OphthalConjuctiva> patient_conjuctiva_array,
                                                       final List<OphthalSclera> patient_sclera_array, final List<OphthalCornearAnteriorSurface> patient_cornea_anterior_array,
                                                       final List<OphthalCornearPosteriorSurface> patient_cornea_posterior_array,
                                                       final List<OphthalAnteriorChamber> patient_anterior_chamber_array, final List<OphthalIris> patient_iris_array,
                                                       final List<OphthalPupil> patient_pupil_array, final List<OphthalAngleAnteriorChamber> patient_angle_array,
                                                       final List<OphthalLens> patient_lens_array, final List<OphthalViterous> patient_viterous_array,
                                                       final List<OphthalFundus> patient_fundus_array, final String refraction_RE_top,
                                                       final String refraction_RE_bottom, final String refraction_LE_top, final String refraction_LE_bottom,
                                                       final List<Investigations> patient_investigation_array, final List<Diagnosis> patient_daignosis_array,
                                                       final List<Treatments> patient_treatment_array, final List<FrequentPrescription> patient_prescription_array,
                                                       final String conultationFees, final String get_followup_date, final String diagnosis_details, final String treatment_details,
                                                       final String prescription_note, final String visit_entry_date, final String get_dv_sphere_right, final String get_dv_cyl_right,
                                                       final String get_dv_axis_right, final String get_nv_sphere_right, final String get_nv_cyl_right, final String get_nv_axis_right,
                                                       final String get_ipd_right, final String get_dv_sphere_left, final String get_dv_cyl_left, final String get_dv_axis_left,
                                                       final String get_nv_sphere_left, final String get_nv_cyl_left, final String get_nv_axis_left, final String get_ipd_left,
                                                       int hospital_id, final String visit_chiefMedComplaint_sufferings, final int investigation_template_save,
                                                       final String investigation_template_name, final int patient_education,
                                                       final String distanceVision_Unaided_RE, final String distanceVision_Unaided_LE, final String NearVision_Unaided_RE, final String NearVision_Unaided_LE,
                                                       final String iop_RE, final String iop_LE) {
        new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;
            ProgressDialog progressDialog;
            JSONArray jsonArray_chiefComplaint, jsonArray_lids, jsonArray_conjuctiva, jsonArray_sclera, jsonArray_cornear_anterior,
                    jsonArray_cornear_posterior, jsonArray_anterior_chamber, jsonArray_iris, jsonArray_pupil, jsonArray_angle, jsonArray_lens,
                    jsonArray_viterous, jsonArray_fundus, jsonArray_investigations, jsonArray_diagnostics,
                    jsonArray_treatment, jsonArray_prescription, jsonArray_repeatPrescription;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(EditOphthalVisitDetailsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {

                    JSONObject jsonObject = JSONParser.ophthalEditVisit(PATIENT_ID, EPISODE_ID, patient_chief_medcomplaint_array,
                            distanceVision_RE,  distanceVision_LE,  nearVision_RE,  nearVision_LE,
                            patient_lids_array, patient_conjuctiva_array, patient_sclera_array, patient_cornea_anterior_array,
                            patient_cornea_posterior_array, patient_anterior_chamber_array, patient_iris_array,
                            patient_pupil_array, patient_angle_array, patient_lens_array, patient_viterous_array,
                            patient_fundus_array, refraction_RE_top, refraction_RE_bottom, refraction_LE_top, refraction_LE_bottom,
                            patient_investigation_array, patient_daignosis_array,
                            patient_treatment_array, patient_prescription_array,
                            conultationFees,  get_followup_date,  diagnosis_details,  treatment_details,
                            prescription_note, visit_entry_date,  get_dv_sphere_right,  get_dv_cyl_right,
                            get_dv_axis_right,  get_nv_sphere_right,  get_nv_cyl_right,  get_nv_axis_right,
                            get_ipd_right,  get_dv_sphere_left,  get_dv_cyl_left,  get_dv_axis_left,
                            get_nv_sphere_left,  get_nv_cyl_left,  get_nv_axis_left,  get_ipd_left,
                            USER_ID, USER_LOGIN_TYPE, HOSPITAL_ID, visit_chiefMedComplaint_sufferings,
                            investigation_template_save, investigation_template_name, patient_education,
                            distanceVision_Unaided_RE, distanceVision_Unaided_LE, NearVision_Unaided_RE, NearVision_Unaided_LE,
                            iop_RE, iop_LE);

                    if (jsonObject != null) {
                        Log.e(Utils.TAG, " GET: " + jsonObject.getString("result"));
                        if (jsonObject.getString("result").equals("success")) {
                            status = true;

                            if (jsonObject.has("frequent_medcomp_details")) {
                                jsonArray_chiefComplaint = jsonObject.getJSONArray("frequent_medcomp_details");
                            }

                            if (jsonObject.has("lids_details")) {
                                jsonArray_lids = jsonObject.getJSONArray("lids_details");
                            }

                            if (jsonObject.has("conjuctiva_details")) {
                                jsonArray_conjuctiva = jsonObject.getJSONArray("conjuctiva_details");
                            }

                            if (jsonObject.has("sclera_details")) {
                                jsonArray_sclera = jsonObject.getJSONArray("sclera_details");
                            }

                            if (jsonObject.has("cornea_anterior_details")) {
                                jsonArray_cornear_anterior = jsonObject.getJSONArray("cornea_anterior_details");
                            }

                            if (jsonObject.has("cornea_posterior_details")) {
                                jsonArray_cornear_posterior = jsonObject.getJSONArray("cornea_posterior_details");
                            }

                            if (jsonObject.has("anterior_chamber_details")) {
                                jsonArray_anterior_chamber = jsonObject.getJSONArray("anterior_chamber_details");
                            }

                            if (jsonObject.has("iris_details")) {
                                jsonArray_iris = jsonObject.getJSONArray("iris_details");
                            }

                            if (jsonObject.has("pupil_details")) {
                                jsonArray_pupil = jsonObject.getJSONArray("pupil_details");
                            }

                            if (jsonObject.has("angle_details")) {
                                jsonArray_angle = jsonObject.getJSONArray("angle_details");
                            }

                            if (jsonObject.has("lens_details")) {
                                jsonArray_lens = jsonObject.getJSONArray("lens_details");
                            }

                            if (jsonObject.has("viterous_details")) {
                                jsonArray_viterous = jsonObject.getJSONArray("viterous_details");
                            }

                            if (jsonObject.has("fundus_details")) {
                                jsonArray_fundus = jsonObject.getJSONArray("fundus_details");
                            }

                            if (jsonObject.has("frequent_investigation_details")) {
                                jsonArray_investigations = jsonObject.getJSONArray("frequent_investigation_details");
                            }

                            if (jsonObject.has("frequent_diagnosis_details")) {
                                jsonArray_diagnostics = jsonObject.getJSONArray("frequent_diagnosis_details");
                            }

                            if (jsonObject.has("frequent_treatment_details")) {
                                jsonArray_treatment = jsonObject.getJSONArray("frequent_treatment_details");
                            }

                            if (jsonObject.has("frequent_medicine_details")) {
                                jsonArray_prescription = jsonObject.getJSONArray("frequent_medicine_details");
                            }

                            if (jsonObject.has("repeat_precription_details")) {
                                jsonArray_repeatPrescription = jsonObject.getJSONArray("repeat_precription_details");
                            }

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

                    try {
                        if(jsonArray_chiefComplaint.length() > 0) {
                            freqComplaintListArraylist = new ArrayList<>();
                            MedisensePracticeDB.clearFrequentComplaints(EditOphthalVisitDetailsActivity.this,USER_ID,USER_LOGIN_TYPE);
                            for (int j = 0; j < jsonArray_chiefComplaint.length(); j++) {

                                freqComplaintListArraylist.add(new FrequentChiefComplaints(jsonArray_chiefComplaint.getJSONObject(j).getInt("dfs_id"),
                                        jsonArray_chiefComplaint.getJSONObject(j).getInt("symptoms_id"),jsonArray_chiefComplaint.getJSONObject(j).getInt("doc_id"),
                                        jsonArray_chiefComplaint.getJSONObject(j).getInt("doc_type"),jsonArray_chiefComplaint.getJSONObject(j).getInt("freq_count"),
                                        jsonArray_chiefComplaint.getJSONObject(j).getString("symptoms"),USER_ID, USER_LOGIN_TYPE));

                                long mfreqComp_insert = MedisensePracticeDB.frequentComplaintInsert(EditOphthalVisitDetailsActivity.this,jsonArray_chiefComplaint.getJSONObject(j).getInt("dfs_id"),
                                        jsonArray_chiefComplaint.getJSONObject(j).getInt("symptoms_id"),jsonArray_chiefComplaint.getJSONObject(j).getInt("doc_id"),
                                        jsonArray_chiefComplaint.getJSONObject(j).getInt("doc_type"),jsonArray_chiefComplaint.getJSONObject(j).getInt("freq_count"),
                                        jsonArray_chiefComplaint.getJSONObject(j).getString("symptoms"), USER_ID, USER_LOGIN_TYPE);

                            }
                            Log.d(Utils.TAG, "freqComplaintListArraylist: " + freqComplaintListArraylist.size());

                            frequentcomplaintAdapter = new FrequentComplaintAdapters(EditOphthalVisitDetailsActivity.this, freqComplaintListArraylist);
                            frequentComplaintRecyclerview.setAdapter(frequentcomplaintAdapter);
                            frequentcomplaintAdapter.notifyDataSetChanged();
                        }

                        if (jsonArray_lids.length() > 0) {
                            lidsListArraylist  = new ArrayList<>();
                            MedisensePracticeDB.clearLids(EditOphthalVisitDetailsActivity.this,USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_lids.length(); i++) {
                                lidsListArraylist.add(new Lids(jsonArray_lids.getJSONObject(i).getInt("lids_id"),
                                        jsonArray_lids.getJSONObject(i).getString("lids_name"),jsonArray_lids.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_lids.getJSONObject(i).getInt("doc_type"),jsonArray_lids.getJSONObject(i).getString("left_eye"),
                                        jsonArray_lids.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                                long lids_insert = MedisensePracticeDB.lidsInsert(EditOphthalVisitDetailsActivity.this,
                                        jsonArray_lids.getJSONObject(i).getInt("lids_id"),
                                        jsonArray_lids.getJSONObject(i).getString("lids_name"),jsonArray_lids.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_lids.getJSONObject(i).getInt("doc_type"),jsonArray_lids.getJSONObject(i).getString("left_eye"),
                                        jsonArray_lids.getJSONObject(i).getString("right_eye"),
                                        USER_ID, USER_LOGIN_TYPE);

                            }
                            Log.d(Utils.TAG, "lidsListArraylist: " + lidsListArraylist.size());

                            // prepareLidsCheckBoxes(lidsListArraylist);
                        }

                        if (jsonArray_conjuctiva.length() > 0) {
                            conjuctivaListArraylist  = new ArrayList<>();
                            MedisensePracticeDB.clearConjuctiva(EditOphthalVisitDetailsActivity.this,USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_conjuctiva.length(); i++) {
                                conjuctivaListArraylist.add(new OphthalConjuctiva(jsonArray_conjuctiva.getJSONObject(i).getInt("conjuctiva_id"),
                                        jsonArray_conjuctiva.getJSONObject(i).getString("conjuctiva_name"),jsonArray_conjuctiva.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_conjuctiva.getJSONObject(i).getInt("doc_type"),jsonArray_conjuctiva.getJSONObject(i).getString("left_eye"),
                                        jsonArray_conjuctiva.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                                long conjuctiva_insert = MedisensePracticeDB.conjuctivaInsert(EditOphthalVisitDetailsActivity.this,
                                        jsonArray_conjuctiva.getJSONObject(i).getInt("conjuctiva_id"),
                                        jsonArray_conjuctiva.getJSONObject(i).getString("conjuctiva_name"),jsonArray_conjuctiva.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_conjuctiva.getJSONObject(i).getInt("doc_type"),jsonArray_conjuctiva.getJSONObject(i).getString("left_eye"),
                                        jsonArray_conjuctiva.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                            }

                            Log.d(Utils.TAG, "conjuctivaListArraylist: " + conjuctivaListArraylist.size());
                            //  prepareConjuctivaCheckBoxes(conjuctivaListArraylist);
                        }

                        if (jsonArray_sclera.length() > 0) {
                            scleraListArraylist  = new ArrayList<>();
                            MedisensePracticeDB.clearSclera(EditOphthalVisitDetailsActivity.this,USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_sclera.length(); i++) {
                                scleraListArraylist.add(new OphthalSclera(jsonArray_sclera.getJSONObject(i).getInt("sclera_id"),
                                        jsonArray_sclera.getJSONObject(i).getString("scelra_name"),jsonArray_sclera.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_sclera.getJSONObject(i).getInt("doc_type"),jsonArray_sclera.getJSONObject(i).getString("left_eye"),
                                        jsonArray_sclera.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                                long sclera_insert = MedisensePracticeDB.scleraInsert(EditOphthalVisitDetailsActivity.this,
                                        jsonArray_sclera.getJSONObject(i).getInt("sclera_id"),
                                        jsonArray_sclera.getJSONObject(i).getString("scelra_name"),jsonArray_sclera.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_sclera.getJSONObject(i).getInt("doc_type"),jsonArray_sclera.getJSONObject(i).getString("left_eye"),
                                        jsonArray_sclera.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                            }

                            Log.d(Utils.TAG, "scleraListArraylist: " + scleraListArraylist.size());
                            //   prepareScleraCheckBoxes(scleraListArraylist);
                        }

                        if (jsonArray_cornear_anterior.length() > 0) {
                            corneaAnteriorListArraylist  = new ArrayList<>();
                            MedisensePracticeDB.clearCorneaAnterior(EditOphthalVisitDetailsActivity.this,USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_cornear_anterior.length(); i++) {
                                corneaAnteriorListArraylist.add(new OphthalCornearAnteriorSurface(jsonArray_cornear_anterior.getJSONObject(i).getInt("cornea_ant_id"),
                                        jsonArray_cornear_anterior.getJSONObject(i).getString("cornea_ant_name"),jsonArray_cornear_anterior.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_cornear_anterior.getJSONObject(i).getInt("doc_type"),jsonArray_cornear_anterior.getJSONObject(i).getString("left_eye"),
                                        jsonArray_cornear_anterior.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                                long cornea_ant_insert = MedisensePracticeDB.corneaAnteriorInsert(EditOphthalVisitDetailsActivity.this,
                                        jsonArray_cornear_anterior.getJSONObject(i).getInt("cornea_ant_id"),
                                        jsonArray_cornear_anterior.getJSONObject(i).getString("cornea_ant_name"),jsonArray_cornear_anterior.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_cornear_anterior.getJSONObject(i).getInt("doc_type"),jsonArray_cornear_anterior.getJSONObject(i).getString("left_eye"),
                                        jsonArray_cornear_anterior.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                            }
                            Log.d(Utils.TAG, "corneaAnteriorListArraylist: " + corneaAnteriorListArraylist.size());
                            //   prepareCorneaAnteriorCheckBoxes(corneaAnteriorListArraylist);
                        }

                        if (jsonArray_cornear_posterior.length() > 0) {
                            corneaPosteriorListArraylist  = new ArrayList<>();
                            MedisensePracticeDB.clearCorneaPosterior(EditOphthalVisitDetailsActivity.this,USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_cornear_posterior.length(); i++) {
                                corneaPosteriorListArraylist.add(new OphthalCornearPosteriorSurface(jsonArray_cornear_posterior.getJSONObject(i).getInt("cornea_post_id"),
                                        jsonArray_cornear_posterior.getJSONObject(i).getString("cornea_post_name"),jsonArray_cornear_posterior.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_cornear_posterior.getJSONObject(i).getInt("doc_type"),jsonArray_cornear_posterior.getJSONObject(i).getString("left_eye"),
                                        jsonArray_cornear_posterior.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                                long corneaPost_insert = MedisensePracticeDB.corneaPosteriorInsert(EditOphthalVisitDetailsActivity.this,
                                        jsonArray_cornear_posterior.getJSONObject(i).getInt("cornea_post_id"),
                                        jsonArray_cornear_posterior.getJSONObject(i).getString("cornea_post_name"),jsonArray_cornear_posterior.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_cornear_posterior.getJSONObject(i).getInt("doc_type"),jsonArray_cornear_posterior.getJSONObject(i).getString("left_eye"),
                                        jsonArray_cornear_posterior.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                            }
                            Log.d(Utils.TAG, "corneaPosteriorListArraylist: " + corneaPosteriorListArraylist.size());
                            //   prepareCorneaPosteriorCheckBoxes(corneaPosteriorListArraylist);
                        }

                        if (jsonArray_anterior_chamber.length() > 0) {
                            anteriorChamberListArraylist  = new ArrayList<>();
                            MedisensePracticeDB.clearAnteriorChamber(EditOphthalVisitDetailsActivity.this,USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_anterior_chamber.length(); i++) {
                                anteriorChamberListArraylist.add(new OphthalAnteriorChamber(jsonArray_anterior_chamber.getJSONObject(i).getInt("chamber_id"),
                                        jsonArray_anterior_chamber.getJSONObject(i).getString("chamber_name"),jsonArray_anterior_chamber.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_anterior_chamber.getJSONObject(i).getInt("doc_type"),jsonArray_anterior_chamber.getJSONObject(i).getString("left_eye"),
                                        jsonArray_anterior_chamber.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                                long chamber_insert = MedisensePracticeDB.anteriorChamberInsert(EditOphthalVisitDetailsActivity.this,
                                        jsonArray_anterior_chamber.getJSONObject(i).getInt("chamber_id"),
                                        jsonArray_anterior_chamber.getJSONObject(i).getString("chamber_name"),jsonArray_anterior_chamber.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_anterior_chamber.getJSONObject(i).getInt("doc_type"),jsonArray_anterior_chamber.getJSONObject(i).getString("left_eye"),
                                        jsonArray_anterior_chamber.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                            }
                            Log.d(Utils.TAG, "anteriorChamberListArraylist: " + anteriorChamberListArraylist.size());
                            //  prepareAnteriorChamberCheckBoxes(anteriorChamberListArraylist);
                        }

                        if (jsonArray_iris.length() > 0) {
                            irisListArraylist  = new ArrayList<>();
                            MedisensePracticeDB.clearIris(EditOphthalVisitDetailsActivity.this,USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_iris.length(); i++) {
                                irisListArraylist.add(new OphthalIris(jsonArray_iris.getJSONObject(i).getInt("iris_id"),
                                        jsonArray_iris.getJSONObject(i).getString("iris_name"),jsonArray_iris.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_iris.getJSONObject(i).getInt("doc_type"),jsonArray_iris.getJSONObject(i).getString("left_eye"),
                                        jsonArray_iris.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                                long iris_insert = MedisensePracticeDB.irisInsert(EditOphthalVisitDetailsActivity.this,
                                        jsonArray_iris.getJSONObject(i).getInt("iris_id"),
                                        jsonArray_iris.getJSONObject(i).getString("iris_name"),jsonArray_iris.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_iris.getJSONObject(i).getInt("doc_type"),jsonArray_iris.getJSONObject(i).getString("left_eye"),
                                        jsonArray_iris.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                            }
                            Log.d(Utils.TAG, "irisListArraylist: " + irisListArraylist.size());
                            //  prepareIrisCheckBoxes(irisListArraylist);
                        }

                        if (jsonArray_pupil.length() > 0) {
                            pupilListArraylist  = new ArrayList<>();
                            MedisensePracticeDB.clearPupil(EditOphthalVisitDetailsActivity.this,USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_pupil.length(); i++) {
                                pupilListArraylist.add(new OphthalPupil(jsonArray_pupil.getJSONObject(i).getInt("pupil_id"),
                                        jsonArray_pupil.getJSONObject(i).getString("pupil_name"),jsonArray_pupil.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_pupil.getJSONObject(i).getInt("doc_type"),jsonArray_pupil.getJSONObject(i).getString("left_eye"),
                                        jsonArray_pupil.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                                long pupil_insert = MedisensePracticeDB.pupilInsert(EditOphthalVisitDetailsActivity.this,
                                        jsonArray_pupil.getJSONObject(i).getInt("pupil_id"),
                                        jsonArray_pupil.getJSONObject(i).getString("pupil_name"),jsonArray_pupil.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_pupil.getJSONObject(i).getInt("doc_type"),jsonArray_pupil.getJSONObject(i).getString("left_eye"),
                                        jsonArray_pupil.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                            }
                            Log.d(Utils.TAG, "pupilListArraylist: " + pupilListArraylist.size());
                            //  preparePupilCheckBoxes(pupilListArraylist);
                        }

                        if (jsonArray_angle.length() > 0) {
                            angleListArraylist  = new ArrayList<>();
                            MedisensePracticeDB.clearAngle(EditOphthalVisitDetailsActivity.this,USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_angle.length(); i++) {
                                angleListArraylist.add(new OphthalAngleAnteriorChamber(jsonArray_angle.getJSONObject(i).getInt("angle_id"),
                                        jsonArray_angle.getJSONObject(i).getString("angle_name"),jsonArray_angle.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_angle.getJSONObject(i).getInt("doc_type"),jsonArray_angle.getJSONObject(i).getString("left_eye"),
                                        jsonArray_angle.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                                long angle_insert = MedisensePracticeDB.angleInsert(EditOphthalVisitDetailsActivity.this,
                                        jsonArray_angle.getJSONObject(i).getInt("angle_id"),
                                        jsonArray_angle.getJSONObject(i).getString("angle_name"),jsonArray_angle.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_angle.getJSONObject(i).getInt("doc_type"),jsonArray_angle.getJSONObject(i).getString("left_eye"),
                                        jsonArray_angle.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                            }
                            Log.d(Utils.TAG, "angleListArraylist: " + angleListArraylist.size());
                            //  prepareAngleCheckBoxes(angleListArraylist);
                        }

                        if (jsonArray_lens.length() > 0) {
                            lensListArraylist  = new ArrayList<>();
                            MedisensePracticeDB.clearLens(EditOphthalVisitDetailsActivity.this,USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_lens.length(); i++) {
                                lensListArraylist.add(new OphthalLens(jsonArray_lens.getJSONObject(i).getInt("lens_id"),
                                        jsonArray_lens.getJSONObject(i).getString("lens_name"),jsonArray_lens.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_lens.getJSONObject(i).getInt("doc_type"),jsonArray_lens.getJSONObject(i).getString("left_eye"),
                                        jsonArray_lens.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                                long lens_insert = MedisensePracticeDB.lensInsert(EditOphthalVisitDetailsActivity.this,
                                        jsonArray_lens.getJSONObject(i).getInt("lens_id"),
                                        jsonArray_lens.getJSONObject(i).getString("lens_name"),jsonArray_lens.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_lens.getJSONObject(i).getInt("doc_type"),jsonArray_lens.getJSONObject(i).getString("left_eye"),
                                        jsonArray_lens.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                            }
                            Log.d(Utils.TAG, "lensListArraylist: " + lensListArraylist.size());
                            //   prepareLensCheckBoxes(lensListArraylist);
                        }

                        if (jsonArray_viterous.length() > 0) {
                            viterousListArraylist  = new ArrayList<>();
                            MedisensePracticeDB.clearViterous(EditOphthalVisitDetailsActivity.this,USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_viterous.length(); i++) {
                                viterousListArraylist.add(new OphthalViterous(jsonArray_viterous.getJSONObject(i).getInt("viterous_id"),
                                        jsonArray_viterous.getJSONObject(i).getString("viterous_name"),jsonArray_viterous.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_viterous.getJSONObject(i).getInt("doc_type"),jsonArray_viterous.getJSONObject(i).getString("left_eye"),
                                        jsonArray_viterous.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                                long viterous_insert = MedisensePracticeDB.viterousnsert(EditOphthalVisitDetailsActivity.this,
                                        jsonArray_viterous.getJSONObject(i).getInt("viterous_id"),
                                        jsonArray_viterous.getJSONObject(i).getString("viterous_name"),jsonArray_viterous.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_viterous.getJSONObject(i).getInt("doc_type"),jsonArray_viterous.getJSONObject(i).getString("left_eye"),
                                        jsonArray_viterous.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                            }
                            Log.d(Utils.TAG, "viterousListArraylist: " + viterousListArraylist.size());
                            //    prepareViterousCheckBoxes(viterousListArraylist);
                        }

                        if (jsonArray_fundus.length() > 0) {
                            fundusListArraylist  = new ArrayList<>();
                            MedisensePracticeDB.clearFundus(EditOphthalVisitDetailsActivity.this,USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_fundus.length(); i++) {
                                fundusListArraylist.add(new OphthalFundus(jsonArray_fundus.getJSONObject(i).getInt("fundus_id"),
                                        jsonArray_fundus.getJSONObject(i).getString("fundus_name"),jsonArray_fundus.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_fundus.getJSONObject(i).getInt("doc_type"),jsonArray_fundus.getJSONObject(i).getString("left_eye"),
                                        jsonArray_fundus.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE));

                                long fundus_insert = MedisensePracticeDB.fundusInsert(EditOphthalVisitDetailsActivity.this,
                                        jsonArray_fundus.getJSONObject(i).getInt("fundus_id"),
                                        jsonArray_fundus.getJSONObject(i).getString("fundus_name"),jsonArray_fundus.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_fundus.getJSONObject(i).getInt("doc_type"),jsonArray_fundus.getJSONObject(i).getString("left_eye"),
                                        jsonArray_fundus.getJSONObject(i).getString("right_eye"),USER_ID, USER_LOGIN_TYPE);

                            }
                            Log.d(Utils.TAG, "fundusListArraylist: " + fundusListArraylist.size());
                            //    prepareFundusCheckBoxes(fundusListArraylist);
                        }

                        if (jsonArray_investigations.length() > 0) {
                            freqInvestigationsListArraylist  = new ArrayList<>();
                            MedisensePracticeDB.clearFrequentInvestigations(EditOphthalVisitDetailsActivity.this,USER_ID,USER_LOGIN_TYPE);
                            for (int j = 0; j < jsonArray_investigations.length(); j++) {
                                freqInvestigationsListArraylist.add(new FrequentInvestigations(jsonArray_investigations.getJSONObject(j).getInt("dfi_id"),
                                        jsonArray_investigations.getJSONObject(j).getInt("main_test_id"),jsonArray_investigations.getJSONObject(j).getInt("doc_id"),
                                        jsonArray_investigations.getJSONObject(j).getInt("doc_type"),jsonArray_investigations.getJSONObject(j).getInt("freq_test_count"),
                                        jsonArray_investigations.getJSONObject(j).getString("test_name_site_name").trim(),jsonArray_investigations.getJSONObject(j).getInt("department"),
                                        USER_ID, USER_LOGIN_TYPE));

                                long mfreqInvest_insert = MedisensePracticeDB.frequentInvestigationsInsert(EditOphthalVisitDetailsActivity.this,jsonArray_investigations.getJSONObject(j).getInt("dfi_id"),
                                        jsonArray_investigations.getJSONObject(j).getInt("main_test_id"),jsonArray_investigations.getJSONObject(j).getInt("doc_id"),
                                        jsonArray_investigations.getJSONObject(j).getInt("doc_type"),jsonArray_investigations.getJSONObject(j).getInt("freq_test_count"),
                                        jsonArray_investigations.getJSONObject(j).getString("test_name_site_name").trim(),jsonArray_investigations.getJSONObject(j).getInt("department"),
                                        USER_ID, USER_LOGIN_TYPE);

                            }
                            Log.d(Utils.TAG, "freqInvestigationsListArraylist: " + freqInvestigationsListArraylist.size());

                            frequentInvestAdapter = new FrequentInvestigationAdapters(EditOphthalVisitDetailsActivity.this, freqInvestigationsListArraylist);
                            frequentInvestRecyclerview.setAdapter(frequentInvestAdapter);
                            frequentInvestAdapter.notifyDataSetChanged();
                        }

                        if (jsonArray_diagnostics.length() > 0) {
                            freqDiagnosisListArraylist = new ArrayList<>();
                            MedisensePracticeDB.clearFrequentDiagnosis(EditOphthalVisitDetailsActivity.this,USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_diagnostics.length(); i++) {
                                freqDiagnosisListArraylist.add(new Diagnosis(jsonArray_diagnostics.getJSONObject(i).getInt("dfd_id"),jsonArray_diagnostics.getJSONObject(i).getInt("icd_id"),
                                        jsonArray_diagnostics.getJSONObject(i).getString("icd_code"),jsonArray_diagnostics.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_diagnostics.getJSONObject(i).getInt("doc_type"),jsonArray_diagnostics.getJSONObject(i).getInt("freq_count"),USER_ID, USER_LOGIN_TYPE));

                                long freqdiagno_insert = MedisensePracticeDB.frequentDiagnosisInsert(EditOphthalVisitDetailsActivity.this,jsonArray_diagnostics.getJSONObject(i).getInt("dfd_id"),jsonArray_diagnostics.getJSONObject(i).getInt("icd_id"),
                                        jsonArray_diagnostics.getJSONObject(i).getString("icd_code"),jsonArray_diagnostics.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_diagnostics.getJSONObject(i).getInt("doc_type"),jsonArray_diagnostics.getJSONObject(i).getInt("freq_count"),USER_ID, USER_LOGIN_TYPE);

                            }

                            Log.d(Utils.TAG, "freqDiagnosisListArraylist: " + freqDiagnosisListArraylist.size());

                            frequentDiagnosisAdapter = new FrequentDiagnosisAdapters(EditOphthalVisitDetailsActivity.this, freqDiagnosisListArraylist);
                            frequentDiagnosisRecyclerview.setAdapter(frequentDiagnosisAdapter);
                            frequentDiagnosisAdapter.notifyDataSetChanged();
                        }

                        if (jsonArray_treatment.length() > 0) {
                            freqTreatmentListArraylist = new ArrayList<>();
                            MedisensePracticeDB.clearFrequentTreatments(EditOphthalVisitDetailsActivity.this,USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_treatment.length(); i++) {
                                freqTreatmentListArraylist.add(new Treatments(jsonArray_treatment.getJSONObject(i).getInt("dft_id"),
                                        jsonArray_treatment.getJSONObject(i).getString("treatment"),jsonArray_treatment.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_treatment.getJSONObject(i).getInt("doc_type"),jsonArray_treatment.getJSONObject(i).getInt("freq_count"),USER_ID, USER_LOGIN_TYPE));

                                long freqtreat_insert = MedisensePracticeDB.frequentTreatmentInsert(EditOphthalVisitDetailsActivity.this,jsonArray_treatment.getJSONObject(i).getInt("dft_id"),
                                        jsonArray_treatment.getJSONObject(i).getInt("doc_id"), jsonArray_treatment.getJSONObject(i).getInt("doc_type"),
                                        jsonArray_treatment.getJSONObject(i).getInt("freq_count"), jsonArray_treatment.getJSONObject(i).getString("treatment"),
                                        USER_ID, USER_LOGIN_TYPE);

                            }

                            Log.d(Utils.TAG, "freqTreatmentListArraylist: " + freqTreatmentListArraylist.size());

                            frequenttreatmentAdapter = new FrequentTreatmentAdapters(EditOphthalVisitDetailsActivity.this, freqTreatmentListArraylist);
                            frequentTreatmentRecyclerview.setAdapter(frequenttreatmentAdapter);
                            frequenttreatmentAdapter.notifyDataSetChanged();
                        }

                        if (jsonArray_prescription.length() > 0) {
                            freqPrescriptionsListArraylist = new ArrayList<>();
                            MedisensePracticeDB.clearFrequentPrescriptions(EditOphthalVisitDetailsActivity.this,USER_ID,USER_LOGIN_TYPE);
                            for (int i = 0; i < jsonArray_prescription.length(); i++) {
                                freqPrescriptionsListArraylist.add(new FrequentPrescription(jsonArray_prescription.getJSONObject(i).getInt("freq_medicine_id"),jsonArray_prescription.getJSONObject(i).getInt("pp_id"),
                                        jsonArray_prescription.getJSONObject(i).getString("med_trade_name"),0, jsonArray_prescription.getJSONObject(i).getString("med_generic_name"),
                                        jsonArray_prescription.getJSONObject(i).getString("med_frequency"),jsonArray_prescription.getJSONObject(i).getString("med_timing"),
                                        jsonArray_prescription.getJSONObject(i).getString("med_duration"),jsonArray_prescription.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_prescription.getJSONObject(i).getInt("doc_type"),jsonArray_prescription.getJSONObject(i).getInt("freq_count"),USER_ID, USER_LOGIN_TYPE));

                                long freqpresc_insert = MedisensePracticeDB.frequentPrescriptionInsert(EditOphthalVisitDetailsActivity.this,jsonArray_prescription.getJSONObject(i).getInt("freq_medicine_id"),jsonArray_prescription.getJSONObject(i).getInt("pp_id"),
                                        jsonArray_prescription.getJSONObject(i).getString("med_trade_name"),0, jsonArray_prescription.getJSONObject(i).getString("med_generic_name"),
                                        jsonArray_prescription.getJSONObject(i).getString("med_frequency"),jsonArray_prescription.getJSONObject(i).getString("med_timing"),
                                        jsonArray_prescription.getJSONObject(i).getString("med_duration"),jsonArray_prescription.getJSONObject(i).getInt("doc_id"),
                                        jsonArray_prescription.getJSONObject(i).getInt("doc_type"),jsonArray_prescription.getJSONObject(i).getInt("freq_count"),USER_ID, USER_LOGIN_TYPE);

                            }

                            Log.d(Utils.TAG, "freqPrescriptionsListArraylist: " + freqPrescriptionsListArraylist.size());

                            frequentPrescriptionAdapter = new FrequentPrescriptionsAdapters(EditOphthalVisitDetailsActivity.this, freqPrescriptionsListArraylist);
                            frequentPrescriptionRecyclerview.setAdapter(frequentPrescriptionAdapter);
                            frequentPrescriptionAdapter.notifyDataSetChanged();
                        }


                        if (jsonArray_repeatPrescription.length() > 0) {
                            repeatPrescriptionsListArraylist = new ArrayList<>();
                            MedisensePracticeDB.clearRepeatPrescriptions(EditOphthalVisitDetailsActivity.this,USER_ID,USER_LOGIN_TYPE);
                            for (int j = 0; j < jsonArray_repeatPrescription.length(); j++) {
                                repeatPrescriptionsListArraylist.add(new FrequentPrescription(0,jsonArray_repeatPrescription.getJSONObject(j).getInt("pp_id"),
                                        jsonArray_repeatPrescription.getJSONObject(j).getString("prescription_trade_name"),0, jsonArray_repeatPrescription.getJSONObject(j).getString("prescription_generic_name"),
                                        jsonArray_repeatPrescription.getJSONObject(j).getString("prescription_dosage_name"),jsonArray_repeatPrescription.getJSONObject(j).getString("timing"),
                                        jsonArray_repeatPrescription.getJSONObject(j).getString("duration"),jsonArray_repeatPrescription.getJSONObject(j).getInt("doc_id"),
                                        1,0,USER_ID, USER_LOGIN_TYPE));

                                long repeatpresc_insert = MedisensePracticeDB.repeatPrescriptionInsert(EditOphthalVisitDetailsActivity.this,0,jsonArray_repeatPrescription.getJSONObject(j).getInt("pp_id"),
                                        jsonArray_repeatPrescription.getJSONObject(j).getString("prescription_trade_name"),0, jsonArray_repeatPrescription.getJSONObject(j).getString("prescription_generic_name"),
                                        jsonArray_repeatPrescription.getJSONObject(j).getString("prescription_dosage_name"),jsonArray_repeatPrescription.getJSONObject(j).getString("timing"),
                                        jsonArray_repeatPrescription.getJSONObject(j).getString("duration"),jsonArray_repeatPrescription.getJSONObject(j).getInt("doc_id"),
                                        1,0,USER_ID, USER_LOGIN_TYPE);

                            }
                            Log.d(Utils.TAG, "repeatPrescriptionsListArraylist: " + repeatPrescriptionsListArraylist.size());

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                    // Toast.makeText(SignUpActivity.this,"Registered Successfully", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditOphthalVisitDetailsActivity.this, R.style.CustomDialog);
                    alertDialogBuilder.setMessage("Visit updated successfully. ");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                   /* Intent bundle = new Intent(EditOphthalVisitDetailsActivity.this, MyPatientOphthalHomeActivity.class);
                                    bundle.putExtra("title","Patient Details");
                                    bundle.putExtra("PATIENT_ID", PATIENT_ID);
                                    bundle.putExtra("PATIENT_ID", PATIENT_ID);
                                    bundle.putExtra("PATIENT_NAME", PATIENT_NAME);
                                    bundle.putExtra("PATIENT_AGE", PATIENT_AGE);
                                    bundle.putExtra("PATIENT_GENDER", PATIENT_GENDER);
                                    bundle.putExtra("PATIENT_MOBILE", PATIENT_MOBILE);
                                    bundle.putExtra("PATIENT_EMAIL", PATIENT_EMAIL);
                                    bundle.putExtra("PATIENT_CITY", PATIENT_CITY);
                                    bundle.putExtra("PATIENT_ADDRESS", PATIENT_ADDRESS);
                                    bundle.putExtra("PATIENT_STATE", PATIENT_STATE);
                                    bundle.putExtra("PATIENT_COUNTRY_NAME", PATIENT_COUNTRY);
                                    bundle.putExtra("PATIENT_WEIGHT", PATIENT_WEIGHT);
                                    bundle.putExtra("PATIENT_HYPERTENSION", PATIENT_HYPERTENSION);
                                    bundle.putExtra("PATIENT_DIABETES", PATIENT_DIABETES);
                                    bundle.putExtra("PATIENT_SMOKING", PATIENT_SMOKING);
                                    bundle.putExtra("PATIENT_ALCOHOL", PATIENT_ALCOHOL);
                                    bundle.putExtra("PATIENT_DRUG_ABUSE", PATIENT_DRUG_ABUSE);
                                    bundle.putExtra("PATIENT_OTHER_DETAILS", PATIENT_OTHER_DETAILS);
                                    bundle.putExtra("PATIENT_FAMILY_HISTORY", PATIENT_FAMILY_HISTORY);
                                    bundle.putExtra("PATIENT_PREV_INTERVENTIONS", PATIENT_PREV_INTERVENTIONS);
                                    bundle.putExtra("PATIENT_NEURO_ISSUES", PATIENT_NEURO_ISSUES);
                                    bundle.putExtra("PATIENT_KIDNEY_ISSUES", PATIENT_KIDNEY_ISSUES);
                                    bundle.putExtra("PATIENT_HEIGHT", PATIENT_HEIGHT);
                                    getActivity().startActivity(bundle);
                                    getActivity().finish();*/
                                   finish();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(EditOphthalVisitDetailsActivity.this, "Failed to update visit. \nTry later !!!", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }

    private class loadDiagnosiscTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            //nothing
        }

        @Override
        protected String doInBackground(final String... strings) {
            diagnosisListArraylist = new ArrayList<>();
            Log.d(Utils.TAG,  "Teram: "+ strings[0]);

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_DIAGNOSIS_SEARCH,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG, response.toString());
                            if (response != null) {
                                JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null;
                                try {
                                    jsonObject1 = new JSONObject(response);
                                    String staus_res = jsonObject1.getString("status");
                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray1 = jsonObject1.getJSONArray("diagnosis_details");
                                        if(jsonArray1.length() > 0) {
                                            for (int i = 0; i < jsonArray1.length(); i++) {
                                                diagnosisListArraylist.add(new Diagnosis(jsonArray1.getJSONObject(i).getInt("icd_id"),
                                                        jsonArray1.getJSONObject(i).getString("icd_code"),USER_ID, USER_LOGIN_TYPE));
                                            }

                                            final CustomDiagnosisFilterAdapter examAdapter = new CustomDiagnosisFilterAdapter(EditOphthalVisitDetailsActivity.this,
                                                    R.layout.activity_listview, diagnosisListArraylist);
                                            diagnosis_listview.setAdapter(examAdapter);
                                            diagnosis_listview.setTextFilterEnabled(true);

                                            diagnosis_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    Diagnosis myCustomAdapterItem = (Diagnosis) parent.getItemAtPosition(position);
                                                      /* Toast.makeText(getActivity(),  myCustomAdapterItem.getSymptomsName() + " added.",
                                                                Toast.LENGTH_SHORT)
                                                                .show();*/
                                                    Toast.makeText(EditOphthalVisitDetailsActivity.this,myCustomAdapterItem.getICDName()+ " is added to the list", Toast.LENGTH_SHORT).show();

                                                    PATIENT_DAIGNOSIS_ARRAY.add(new Diagnosis(myCustomAdapterItem.getICDId(),myCustomAdapterItem.getICDName(),
                                                            USER_ID, USER_LOGIN_TYPE));

                                                }
                                            });

                                            diagnosisAdapter = new DiagnosisAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_DAIGNOSIS_ARRAY);
                                            diagnosisRecyclerview.setAdapter(diagnosisAdapter);
                                            diagnosisAdapter.notifyDataSetChanged();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                    map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                    map.put(APIClass.KEY_LOGINTYPE,USER_LOGIN_TYPE);
                    map.put("searchTerm",strings[0]);
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(EditOphthalVisitDetailsActivity.this).
                    getRequestQueue();
            AppController.getInstance(EditOphthalVisitDetailsActivity.this).addToRequestQueue(stringRequest1);
            return null;
        }
    }

    private class loadPrescriptioncTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            //nothing
        }

        @Override
        protected String doInBackground(final String... strings) {
            prescriptionListArraylist = new ArrayList<>();
            Log.d(Utils.TAG,  "Teram: "+ strings[0]);

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_PRESCRIPTION_SEARCH,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG, "prescInfo: "+ response.toString());
                            if (response != null) {
                                JSONObject jsonObject1 = null;   JSONArray jsonArray1 = null;
                                try {
                                    jsonObject1 = new JSONObject(response);
                                    String staus_res = jsonObject1.getString("status");
                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray1 = jsonObject1.getJSONArray("prescription_details");
                                        if(jsonArray1.length() > 0) {
                                            for (int i = 0; i < jsonArray1.length(); i++) {
                                                prescriptionListArraylist.add(new FrequentPrescription(jsonArray1.getJSONObject(i).getInt("freq_medicine_id"),
                                                        jsonArray1.getJSONObject(i).getInt("pp_id"),jsonArray1.getJSONObject(i).getString("med_trade_name"),
                                                        jsonArray1.getJSONObject(i).getInt("generic_id"),jsonArray1.getJSONObject(i).getString("pharma_generic"),
                                                        jsonArray1.getJSONObject(i).getInt("pharma_priority"),jsonArray1.getJSONObject(i).getString("med_frequency"),
                                                        jsonArray1.getJSONObject(i).getString("med_timing"),jsonArray1.getJSONObject(i).getString("med_duration"),
                                                        jsonArray1.getJSONObject(i).getInt("doc_id"),jsonArray1.getJSONObject(i).getInt("doc_type"),
                                                        jsonArray1.getJSONObject(i).getInt("freq_count"),USER_ID, USER_LOGIN_TYPE));
                                            }

                                            final CustomPrescriptionFilterAdapter prescAdapter = new CustomPrescriptionFilterAdapter(EditOphthalVisitDetailsActivity.this,
                                                    R.layout.activity_listview, prescriptionListArraylist);
                                            prescription_listview.setAdapter(prescAdapter);
                                            prescription_listview.setTextFilterEnabled(true);

                                            prescription_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    FrequentPrescription myCustomAdapterItem = (FrequentPrescription) parent.getItemAtPosition(position);
                                                    /*  Toast.makeText(getActivity(),  myCustomAdapterItem.getTradeName() + " added.",
                                                                Toast.LENGTH_SHORT)
                                                                .show();*/
                                                    Toast.makeText(EditOphthalVisitDetailsActivity.this,myCustomAdapterItem.getTradeName()+ " is added to the list", Toast.LENGTH_SHORT).show();

                                                    PATIENT_PRESCRIPTION_ARRAY.add(new FrequentPrescription(myCustomAdapterItem.getPrescFreqId(),myCustomAdapterItem.getPrescriptionId(),myCustomAdapterItem.getTradeName(),myCustomAdapterItem.getGenericId(),myCustomAdapterItem.getGenericName(),myCustomAdapterItem.getDosage(),myCustomAdapterItem.getTimings(),myCustomAdapterItem.getDuration(),myCustomAdapterItem.getDocId(),myCustomAdapterItem.getDocType(),myCustomAdapterItem.getFreqCount(),USER_ID,USER_LOGIN_TYPE));

                                                }
                                            });

                                            prescriptionAdapter = new PrescriptionAdapters(EditOphthalVisitDetailsActivity.this, PATIENT_PRESCRIPTION_ARRAY);
                                            prescriptionRecyclerview.setAdapter(prescriptionAdapter);
                                            prescriptionAdapter.notifyDataSetChanged();
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                    map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                    map.put(APIClass.KEY_LOGINTYPE,USER_LOGIN_TYPE);
                    map.put("searchTerm",strings[0]);
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(EditOphthalVisitDetailsActivity.this).
                    getRequestQueue();
            AppController.getInstance(EditOphthalVisitDetailsActivity.this).addToRequestQueue(stringRequest1);
            return null;
        }
    }

    private void loadTemplatesDropDown() {

        final ArrayList<String> options = new ArrayList<>();
        final ArrayList<Integer> options_ids = new ArrayList<>();

        String[] AlertDialogItems = new String[templateListArraylist.size()];
        final int[] AlertDialogItemsID = new int[templateListArraylist.size()];
        final boolean[] Selectedtruefalse = new boolean[templateListArraylist.size()];
        selectedTemplateID = new ArrayList<>();

        Log.d(Utils.TAG, "templist: " + templateListArraylist.size());
        if (templateListArraylist.size() > 0) {
            for (int i = 0; i < templateListArraylist.size(); i++) {
                options.add(templateListArraylist.get(i).getTemplateName());
                options_ids.add(templateListArraylist.get(i).getTemplateID());

                AlertDialogItems[i] = templateListArraylist.get(i).getTemplateName();
                Selectedtruefalse[i] = false;
                AlertDialogItemsID[i] = templateListArraylist.get(i).getTemplateID();
            }
        }
        AlertDialog.Builder alertdialogbuilder;
        final List<String> ItemsIntoList;

        alertdialogbuilder = new AlertDialog.Builder(EditOphthalVisitDetailsActivity.this, R.style.CustomDialog);
        ItemsIntoList = Arrays.asList(AlertDialogItems);
        alertdialogbuilder.setMultiChoiceItems(AlertDialogItems, Selectedtruefalse, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            }
        });

        alertdialogbuilder.setTitle("Select Templates");
        alertdialogbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int a = 0;
                while (a < Selectedtruefalse.length) {
                    boolean value = Selectedtruefalse[a];
                    if (value) {
                        selectedTemplateID.add(AlertDialogItemsID[a]);
                        new getJsonTemplateList().execute(String.valueOf(AlertDialogItemsID[a]));
                    }
                    a++;
                }
            }
        });
        alertdialogbuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = alertdialogbuilder.create();
        dialog.show();
    }

    class getJsonTemplateList extends AsyncTask<String, String, String> {
        String template_id_selected;

        @Override
        protected String doInBackground(String... key) {
            String stateText = key[0];
            stateText = stateText.trim();
            template_id_selected = stateText.trim();
            Log.d(Utils.TAG, "State selected1: " + stateText);

            //   stateListArraylist = new ArrayList<>();
            int socketTimeout1 = 20000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_HOST_TEMPLATE_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG, " TEMP: "+ response.toString());
                            if (response != null) {
                                JSONObject jsonObject1 = null;
                                JSONArray jsonArray1 = null;
                                ArrayList<String> stateArray = new ArrayList<String>();
                                try {
                                    jsonObject1 = new JSONObject(response);
                                    String staus_res = jsonObject1.getString("status");
                                    if (staus_res.equals("false")) {
                                    } else {
                                        jsonArray1 = jsonObject1.getJSONArray("template_details");
                                        for (int i = 0; i < jsonArray1.length(); i++) {
                                            JSONObject JObject = jsonArray1.getJSONObject(i);

//                                            PRECRIPTION_LIST.add(new PrescriptionList(JObject.getString("prescription_trade_name"), JObject.getString("prescription_generic_name"), JObject.getString("prescription_frequency"), JObject.getString("prescription_timing"),
//                                                    JObject.getString("prescription_duration"),  JObject.getString("note")));

                                            PATIENT_PRESCRIPTION_ARRAY.add(new FrequentPrescription(0,JObject.getInt("pp_id"),JObject.getString("prescription_trade_name"),0,JObject.getString("prescription_generic_name"),JObject.getString("prescription_frequency"),JObject.getString("prescription_timing"),JObject.getString("prescription_duration"),JObject.getInt("doc_id"),JObject.getInt("doc_type"),0,USER_ID,USER_LOGIN_TYPE));

                                        }

                                        refreshPrescriptions();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                    map.put(APIClass.KEY_MY_PATIENT_TEMPLATE_ID, template_id_selected);
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(EditOphthalVisitDetailsActivity.this).
                    getRequestQueue();
            AppController.getInstance(EditOphthalVisitDetailsActivity.this).addToRequestQueue(stringRequest1);

            return null;
        }
    }

    private void submitTemplateFormToServer() {
        String template_name = _edt_template_name.getText().toString();
        if(template_name.equals("")) {
            _edt_template_name.setError("Template Name is required !!");
        }
        else {
            _edt_template_name.setError(null);
            if(PATIENT_PRESCRIPTION_ARRAY.size() <= 0) {
                Toast.makeText(EditOphthalVisitDetailsActivity.this, "Add prescriptions to continue", Toast.LENGTH_SHORT).show();
            }
            else {
                Log.d(Utils.TAG, "presc size: "+ PATIENT_PRESCRIPTION_ARRAY.size());

                submitTemplateDetailsServer(template_name,PATIENT_PRESCRIPTION_ARRAY);

            }
        }
    }

    private void submitTemplateDetailsServer(final String template_name, final List<FrequentPrescription> GET_PRECRIPTION_LIST) {

        new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(EditOphthalVisitDetailsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

            }

            @Override
            protected Boolean doInBackground(Void... params) {

                try {

                    JSONObject jsonObject = JSONParser.createMyPatientTemplateGeneral( template_name,  GET_PRECRIPTION_LIST, USER_ID, USER_LOGIN_TYPE );

                    if (jsonObject != null) {
                        Log.e(Utils.TAG, " GET: " + jsonObject.getString("result"));
                        if(jsonObject.getString("result").equals("success")) {
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
                if (  progressDialog != null)
                    progressDialog.dismiss();
                Log.e(Utils.TAG, "aBoolean: " + String.valueOf(aBoolean));
                if (aBoolean) {

                    if (sharedPreferences != null) {
                        shareadPreferenceClass.clearMyPatientsTemplates();
                    }

                    loadTemplatesFromServer();

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditOphthalVisitDetailsActivity.this, R.style.CustomDialog);
                    alertDialogBuilder.setMessage("Template Saved Successfully. ");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

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
                    Toast.makeText(EditOphthalVisitDetailsActivity.this, "Failed to upload template. \nTry later !!!", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }

    private void loadTemplatesFromServer() {
        templateListArraylist = new ArrayList<>();

        final ProgressDialog dialog2 = new ProgressDialog(EditOphthalVisitDetailsActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        dialog2.setIndeterminate(true);
        dialog2.setMessage("Loading Templates...");
        dialog2.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PRACTICE_MY_TEMPLATES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.d(Utils.TAG, response.toString());
                        JSONArray jsonArray = null;
                        String status = null;
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Log.d(Utils.TAG, response.toString());
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("false")) {
                                } else {
                                    jsonArray = jsonObject.getJSONArray("templates_details");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            templateListArraylist.add(new MyTemplates(jsonArray.getJSONObject(i).getInt("template_id"), jsonArray.getJSONObject(i).getInt("admin_id"),
                                                    jsonArray.getJSONObject(i).getString("template_name"), USER_ID, USER_LOGIN_TYPE));
                                        }
                                        Log.d(Utils.TAG, "size: " + templateListArraylist.size());

                                    }

                                    //Set the values
                                    Gson gson = new Gson();
                                    String jsonTemplatesText = gson.toJson(templateListArraylist);
                                    if (sharedPreferences != null) {
                                        shareadPreferenceClass.clearMyPatientsTemplates();
                                        shareadPreferenceClass.setMyPatientsTemplates(jsonTemplatesText);
                                    }
                                }
                                dialog2.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                dialog2.dismiss();
                            }
                        } else {
                            dialog2.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog2.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(EditOphthalVisitDetailsActivity.this).
                getRequestQueue();
        AppController.getInstance(EditOphthalVisitDetailsActivity.this).addToRequestQueue(stringRequest);
    }
}
