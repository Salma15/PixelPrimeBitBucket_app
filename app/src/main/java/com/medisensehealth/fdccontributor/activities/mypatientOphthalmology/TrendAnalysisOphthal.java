package com.medisensehealth.fdccontributor.activities.mypatientOphthalmology;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.medisensehealth.fdccontributor.DataModel.FrequentPrescription;
import com.medisensehealth.fdccontributor.DataModel.TrendOphthalAnalysisList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.parser.JSONParser;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.CustomMarkerView;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomEditText;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by SALMA on 07-09-2018.
 */
public class TrendAnalysisOphthal extends Fragment implements View.OnClickListener {

    int USER_ID, PATIENT_ID;
    String USER_NAME,USER_LOGIN_TYPE;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;

    String PATIENT_NAME, PATIENT_AGE, PATIENT_GENDER, PATIENT_MOBILE, PATIENT_EMAIL, PATIENT_CITY, PATIENT_ADDRESS, PATIENT_STATE, PATIENT_COUNTRY,
            PATIENT_HEIGHT, PATIENT_WEIGHT, PATIENT_HYPERTENSION, PATIENT_DIABETES, PATIENT_SMOKING, PATIENT_ALCOHOL, PATIENT_DRUG_ABUSE, PATIENT_OTHER_DETAILS,
            PATIENT_FAMILY_HISTORY, PATIENT_PREV_INTERVENTIONS, PATIENT_NEURO_ISSUES, PATIENT_KIDNEY_ISSUES;

    LineChart lineChart;
    Calendar myCalendar;
    CustomTextViewBold add_trend, graph_trend, list_trend;
    CustomTextView trend_add_date;
    List<TrendOphthalAnalysisList> trendsListArraylist = new ArrayList<>();
    List<FrequentPrescription> prescriptionListArraylist = new ArrayList<>();
    CustomTextView DvSphereRE_graph, DvCylRE_graph, DvAxisRE_graph, DvSpeherLE_graph, DvCylLE_graph, DvAxisLE_graph, NvSpeherRE_graph,
            NvCylRE_graph, NvAxisRE_graph, NvSpeherLE_graph, NvCylLE_graph, NvAxisLE_graph, IpdRE_graph, IpdLE_graph;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trend_analysis_ophthal, container, false);

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

            Log.d(Utils.TAG, " ********* TrendAnalysis ************ ");
            Log.d(Utils.TAG, " PATIENT_ID: "+PATIENT_ID);
            Log.d(Utils.TAG, " PATIENT_NAME: "+PATIENT_NAME);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        trendsListArraylist = new ArrayList<>();
        prescriptionListArraylist = new ArrayList<>();

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

        initializationViews();
    }

    private void initializationViews() {
        myCalendar = Calendar.getInstance();
        //  lineChart = (LineChart) findViewById(R.id.line_charts);

        lineChart = (LineChart) getActivity().findViewById(R.id.chart1);

        add_trend = (CustomTextViewBold) getActivity().findViewById(R.id.trend_add);
        add_trend.setOnClickListener(this);
        graph_trend  = (CustomTextViewBold) getActivity().findViewById(R.id.trend_graph);
        graph_trend.setOnClickListener(this);
        list_trend  = (CustomTextViewBold) getActivity().findViewById(R.id.trend_list);
        list_trend.setOnClickListener(this);

        DvSphereRE_graph = (CustomTextView)  getActivity().findViewById(R.id.graph_DvSphereRE);
        DvCylRE_graph = (CustomTextView)  getActivity().findViewById(R.id.graph_DvCylRE);
        DvAxisRE_graph = (CustomTextView)  getActivity().findViewById(R.id.graph_DvAxisRE);
        DvSpeherLE_graph = (CustomTextView)  getActivity().findViewById(R.id.graph_DvSpeherLE);
        DvCylLE_graph = (CustomTextView) getActivity().findViewById(R.id.graph_DvCylLE);
        DvAxisLE_graph = (CustomTextView) getActivity().findViewById(R.id.graph_DvAxisLE);
        NvSpeherRE_graph = (CustomTextView)  getActivity().findViewById(R.id.graph_NvSpeherRE);
        NvCylRE_graph = (CustomTextView)  getActivity().findViewById(R.id.graph_NvCylRE);
        NvAxisRE_graph = (CustomTextView)  getActivity().findViewById(R.id.graph_NvAxisRE);
        NvSpeherLE_graph = (CustomTextView)  getActivity().findViewById(R.id.graph_NvSpeherLE);
        NvCylLE_graph = (CustomTextView)  getActivity().findViewById(R.id.graph_NvCylLE);
        NvAxisLE_graph = (CustomTextView)  getActivity().findViewById(R.id.graph_NvAxisLE);
        IpdRE_graph = (CustomTextView)  getActivity().findViewById(R.id.graph_IpdRE);
        IpdLE_graph = (CustomTextView)  getActivity().findViewById(R.id.graph_IpdLE);
        DvSphereRE_graph.setOnClickListener(this);
        DvCylRE_graph.setOnClickListener(this);
        DvAxisRE_graph.setOnClickListener(this);
        DvSpeherLE_graph.setOnClickListener(this);
        DvCylLE_graph.setOnClickListener(this);
        DvAxisLE_graph.setOnClickListener(this);
        NvSpeherRE_graph.setOnClickListener(this);
        NvCylRE_graph.setOnClickListener(this);
        NvAxisRE_graph.setOnClickListener(this);
        NvSpeherLE_graph.setOnClickListener(this);
        NvCylLE_graph.setOnClickListener(this);
        NvAxisLE_graph.setOnClickListener(this);
        IpdRE_graph.setOnClickListener(this);
        IpdLE_graph.setOnClickListener(this);

        collectTrendChartFromServer();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.trend_add:
                InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(add_trend.getWindowToken(), 0);
                customAddTrendData();
                break;
            case R.id.trend_graph:
                InputMethodManager imm2 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(graph_trend.getWindowToken(), 0);
                if(trendsListArraylist.size() > 0) {
                    showGraph(trendsListArraylist, prescriptionListArraylist, "ALL");
                }
                else {
                    Toast.makeText(getActivity(), " No data found to view !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.trend_list:
                InputMethodManager imm3 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(list_trend.getWindowToken(), 0);

                if(trendsListArraylist.size() > 0) {
                    customAddTrendListsView();
                }
                else {
                    Toast.makeText(getActivity(), " No data found to view !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.graph_DvSphereRE:
                if(trendsListArraylist.size() > 0) {
                    showGraph(trendsListArraylist, prescriptionListArraylist, "DvSphereRE");
                }
                else {
                    Toast.makeText(getActivity(), " No data found to view !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.graph_DvCylRE:
                if(trendsListArraylist.size() > 0) {
                    showGraph(trendsListArraylist, prescriptionListArraylist, "DvCylRE");
                }
                else {
                    Toast.makeText(getActivity(), " No data found to view !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.graph_DvAxisRE:
                if(trendsListArraylist.size() > 0) {
                    showGraph(trendsListArraylist, prescriptionListArraylist, "DvAxisRE");
                }
                else {
                    Toast.makeText(getActivity(), " No data found to view !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.graph_DvSpeherLE:
                if(trendsListArraylist.size() > 0) {
                    showGraph(trendsListArraylist, prescriptionListArraylist, "DvSpeherLE");
                }
                else {
                    Toast.makeText(getActivity(), " No data found to view !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.graph_DvCylLE:
                if(trendsListArraylist.size() > 0) {
                    showGraph(trendsListArraylist, prescriptionListArraylist, "DvCylLE");
                }
                else {
                    Toast.makeText(getActivity(), " No data found to view !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.graph_DvAxisLE:
                if(trendsListArraylist.size() > 0) {
                    showGraph(trendsListArraylist, prescriptionListArraylist, "DvAxisLE");
                }
                else {
                    Toast.makeText(getActivity(), " No data found to view !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.graph_NvSpeherRE:
                if(trendsListArraylist.size() > 0) {
                    showGraph(trendsListArraylist, prescriptionListArraylist, "NvSpeherRE");
                }
                else {
                    Toast.makeText(getActivity(), " No data found to view !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.graph_NvCylRE:
                if(trendsListArraylist.size() > 0) {
                    showGraph(trendsListArraylist, prescriptionListArraylist, "NvCylRE");
                }
                else {
                    Toast.makeText(getActivity(), " No data found to view !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.graph_NvAxisRE:
                if(trendsListArraylist.size() > 0) {
                    showGraph(trendsListArraylist, prescriptionListArraylist, "NvAxisRE");
                }
                else {
                    Toast.makeText(getActivity(), " No data found to view !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.graph_NvSpeherLE:
                if(trendsListArraylist.size() > 0) {
                    showGraph(trendsListArraylist, prescriptionListArraylist, "NvSpeherLE");
                }
                else {
                    Toast.makeText(getActivity(), " No data found to view !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.graph_NvCylLE:
                if(trendsListArraylist.size() > 0) {
                    showGraph(trendsListArraylist, prescriptionListArraylist, "NvCylLE");
                }
                else {
                    Toast.makeText(getActivity(), " No data found to view !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.graph_NvAxisLE:
                if(trendsListArraylist.size() > 0) {
                    showGraph(trendsListArraylist, prescriptionListArraylist, "NvAxisLE");
                }
                else {
                    Toast.makeText(getActivity(), " No data found to view !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.graph_IpdRE:
                if(trendsListArraylist.size() > 0) {
                    showGraph(trendsListArraylist, prescriptionListArraylist, "IpdRE");
                }
                else {
                    Toast.makeText(getActivity(), " No data found to view !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.graph_IpdLE:
                if(trendsListArraylist.size() > 0) {
                    showGraph(trendsListArraylist, prescriptionListArraylist, "IpdLE");
                }
                else {
                    Toast.makeText(getActivity(), " No data found to view !!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void customAddTrendData() {

        final Dialog dialog = new Dialog(getActivity(), R.style.DialogAnimationView);
        dialog.setContentView(R.layout.custom_health_trends_add_ophthal);
        dialog.setTitle("Add Health Trend Analysis");
        dialog.setCanceledOnTouchOutside(true);

        trend_add_date = (CustomTextView) dialog.findViewById(R.id.trend_add_date);
        final Button submit_btn = (Button)  dialog.findViewById(R.id.trend_submit);
        final CustomEditText _edt_DvSphereRE = (CustomEditText) dialog.findViewById(R.id.spect_dv_sphere_right);
        final CustomEditText _edt_DvCylRE = (CustomEditText) dialog.findViewById(R.id.spect_dv_cyl_right);
        final CustomEditText _edt_DvAxisRE = (CustomEditText) dialog.findViewById(R.id.spect_dv_axis_right);

        final CustomEditText _edt_DvSpeherLE = (CustomEditText) dialog.findViewById(R.id.spect_dv_sphere_left);
        final CustomEditText _edt_DvCylLE = (CustomEditText) dialog.findViewById(R.id.spect_dv_cyl_left);
        final CustomEditText _edt_DvAxisLE = (CustomEditText) dialog.findViewById(R.id.spect_dv_axis_left);

        final CustomEditText _edt_NvSpeherRE = (CustomEditText) dialog.findViewById(R.id.spect_nv_sphere_right);
        final CustomEditText _edt_NvCylRE = (CustomEditText) dialog.findViewById(R.id.spect_nv_cyl_right);
        final CustomEditText _edt_NvAxisRE = (CustomEditText) dialog.findViewById(R.id.spect_nv_axis_right);

        final CustomEditText _edt_NvSpeherLE = (CustomEditText) dialog.findViewById(R.id.spect_nv_sphere_left);
        final CustomEditText _edt_NvCylLE = (CustomEditText) dialog.findViewById(R.id.spect_nv_cyl_left);
        final CustomEditText _edt_NvAxisLE = (CustomEditText) dialog.findViewById(R.id.spect_nv_axis_left);

        final CustomEditText _edt_IpdRE = (CustomEditText) dialog.findViewById(R.id.spect_ipd_right);
        final CustomEditText _edt_IpdLE = (CustomEditText) dialog.findViewById(R.id.spect_ipd_left);

        myCalendar = Calendar.getInstance();
        Date c = myCalendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        trend_add_date.setText(String.valueOf(formattedDate));

        trend_add_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateHyper, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(submit_btn.getWindowToken(), 0);

                String trend_add_Date = trend_add_date.getText().toString().trim();
                String DvSphereRE_val = _edt_DvSphereRE.getText().toString().trim();
                String DvCylRE_val = _edt_DvCylRE.getText().toString().trim();
                String DvAxisRE_val = _edt_DvAxisRE.getText().toString().trim();
                String DvSpeherLE_val = _edt_DvSpeherLE.getText().toString().trim();
                String DvCylLE_val = _edt_DvCylLE.getText().toString().trim();
                String DvAxisLE_val = _edt_DvAxisLE.getText().toString().trim();
                String NvSpeherRE_val = _edt_NvSpeherRE.getText().toString().trim();
                String NvCylRE_val = _edt_NvCylRE.getText().toString().trim();
                String NvAxisRE_val = _edt_NvAxisRE.getText().toString().trim();
                String NvSpeherLE_val = _edt_NvSpeherLE.getText().toString().trim();
                String NvCylLE_val = _edt_NvCylLE.getText().toString().trim();
                String NvAxisLE_val = _edt_NvAxisLE.getText().toString().trim();
                String IpdRE_val = _edt_IpdRE.getText().toString().trim();
                String IpdLE_val = _edt_IpdLE.getText().toString().trim();

                submitTrendsAnalysisToServer(trend_add_Date,  DvSphereRE_val, DvCylRE_val, DvAxisRE_val,
                        DvSpeherLE_val, DvCylLE_val, DvAxisLE_val, NvSpeherRE_val, NvCylRE_val, NvAxisRE_val, NvSpeherLE_val,
                        NvCylLE_val, NvAxisLE_val, IpdRE_val, IpdLE_val );

                dialog.dismiss();
            }
        });

        dialog.show();
    }
    DatePickerDialog.OnDateSetListener dateHyper = new DatePickerDialog.OnDateSetListener() {
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
            trend_add_date.setText(sdf.format(myCalendar.getTime()));
        }
    };

    private void submitTrendsAnalysisToServer(final String trend_add_date, final String dvSphereRE_val, final String dvCylRE_val,
                                              final String dvAxisRE_val, final String dvSpeherLE_val, final String dvCylLE_val,
                                              final String dvAxisLE_val, final String nvSpeherRE_val, final String nvCylRE_val,
                                              final String nvAxisRE_val, final String nvSpeherLE_val, final String nvCylLE_val,
                                              final String nvAxisLE_val, final String ipdRE_val, final String ipdLE_val) {

        new AsyncTask<Void, Integer, Boolean>() {
            boolean status = false;
            ProgressDialog progressDialog;
            JSONArray jsonArrayTrends;

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
                    JSONObject jsonObject = JSONParser.addTrendAnalysis(PATIENT_ID, trend_add_date, dvSphereRE_val, dvCylRE_val,
                            dvAxisRE_val, dvSpeherLE_val, dvCylLE_val, dvAxisLE_val, nvSpeherRE_val, nvCylRE_val,
                            nvAxisRE_val, nvSpeherLE_val, nvCylLE_val, nvAxisLE_val, ipdRE_val, ipdLE_val,
                            USER_ID, USER_LOGIN_TYPE );

                    if (jsonObject != null) {
                        Log.e(Utils.TAG, " GET: " + jsonObject.getString("result"));
                        if(jsonObject.getString("result").equals("success")) {
                            if (jsonObject.has("trends_result")) {
                                jsonArrayTrends = jsonObject.getJSONArray("trends_result");
                            }
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

                  /*  if (sharedPreferences != null) {
                        shareadPreferenceClass.clearMyPatientLists();
                    }*/

                    if(jsonArrayTrends != null) {
                        trendsListArraylist = new ArrayList<>();
                        for(int n=0; n<jsonArrayTrends.length(); n++) {
                            try {
                                trendsListArraylist.add(new TrendOphthalAnalysisList(jsonArrayTrends.getJSONObject(n).getInt("trend_id"),
                                        jsonArrayTrends.getJSONObject(n).getString("date_added"),jsonArrayTrends.getJSONObject(n).getString("DvSphereRE"),
                                        jsonArrayTrends.getJSONObject(n).getString("DvCylRE"),jsonArrayTrends.getJSONObject(n).getString("DvAxisRE"),
                                        jsonArrayTrends.getJSONObject(n).getString("DvSpeherLE"),jsonArrayTrends.getJSONObject(n).getString("DvCylLE"),
                                        jsonArrayTrends.getJSONObject(n).getString("DvAxisLE"),jsonArrayTrends.getJSONObject(n).getString("NvSpeherRE"),
                                        jsonArrayTrends.getJSONObject(n).getString("NvCylRE"),jsonArrayTrends.getJSONObject(n).getString("NvAxisRE"),
                                        jsonArrayTrends.getJSONObject(n).getString("NvSpeherLE"),jsonArrayTrends.getJSONObject(n).getString("NvCylLE"),
                                        jsonArrayTrends.getJSONObject(n).getString("NvAxisLE"),jsonArrayTrends.getJSONObject(n).getString("IpdRE"),
                                        jsonArrayTrends.getJSONObject(n).getString("IpdLE"),jsonArrayTrends.getJSONObject(n).getInt("patient_id"),
                                        jsonArrayTrends.getJSONObject(n).getString("patient_type"),USER_ID, USER_LOGIN_TYPE));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    // Toast.makeText(SignUpActivity.this,"Registered Successfully", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
                    alertDialogBuilder.setMessage("Updated Successfully. ");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    showGraph(trendsListArraylist, prescriptionListArraylist, "ALL");
                                    // finish();
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
                    Toast.makeText(getActivity(), "Failed to update. \nTry later !!!", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }

    private void collectTrendChartFromServer() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_MY_PATIENT_OPTHAL_TRENDS_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "trend list: "+ response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            JSONArray jsonArray, jsonArray1;

                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                if (staus_res.equals("true")) {
                                    jsonArray = jsonObject.getJSONArray("trends_details");
                                    jsonArray1 = jsonObject.getJSONArray("prescription_details");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            String DvSphereRE = "0", DvCylRE = "0", DvAxisRE = "0", DvSpeherLE = "0", DvCylLE = "0", DvAxisLE = "0";
                                            String NvSpeherRE = "0", NvCylRE = "0", NvAxisRE = "0", NvSpeherLE = "0", NvCylLE = "0", NvAxisLE = "0";
                                            String IpdRE = "0", IpdLE = "0";
                                            if(!jsonArray.getJSONObject(i).getString("DvSphereRE").equals("")) {
                                                DvSphereRE = jsonArray.getJSONObject(i).getString("DvSphereRE");
                                            }

                                            if(!jsonArray.getJSONObject(i).getString("DvCylRE").equals("")) {
                                                DvCylRE = jsonArray.getJSONObject(i).getString("DvCylRE");
                                            }

                                            if(!jsonArray.getJSONObject(i).getString("DvAxisRE").equals("")) {
                                                DvAxisRE = jsonArray.getJSONObject(i).getString("DvAxisRE");
                                            }

                                            if(!jsonArray.getJSONObject(i).getString("DvSpeherLE").equals("")) {
                                                DvSpeherLE = jsonArray.getJSONObject(i).getString("DvSpeherLE");
                                            }

                                            if(!jsonArray.getJSONObject(i).getString("DvCylLE").equals("")) {
                                                DvCylLE = jsonArray.getJSONObject(i).getString("DvCylLE");
                                            }

                                            if(!jsonArray.getJSONObject(i).getString("DvAxisLE").equals("")) {
                                                DvAxisLE = jsonArray.getJSONObject(i).getString("DvAxisLE");
                                            }

                                            if(!jsonArray.getJSONObject(i).getString("NvSpeherRE").equals("")) {
                                                NvSpeherRE = jsonArray.getJSONObject(i).getString("NvSpeherRE");
                                            }

                                            if(!jsonArray.getJSONObject(i).getString("NvCylRE").equals("")) {
                                                NvCylRE = jsonArray.getJSONObject(i).getString("NvCylRE");
                                            }

                                            if(!jsonArray.getJSONObject(i).getString("NvAxisRE").equals("")) {
                                                NvAxisRE = jsonArray.getJSONObject(i).getString("NvAxisRE");
                                            }
                                            if(!jsonArray.getJSONObject(i).getString("NvSpeherLE").equals("")) {
                                                NvSpeherLE = jsonArray.getJSONObject(i).getString("NvSpeherLE");
                                            }
                                            if(!jsonArray.getJSONObject(i).getString("NvCylLE").equals("")) {
                                                NvCylLE = jsonArray.getJSONObject(i).getString("NvCylLE");
                                            }
                                            if(!jsonArray.getJSONObject(i).getString("NvAxisLE").equals("")) {
                                                NvAxisLE = jsonArray.getJSONObject(i).getString("NvAxisLE");
                                            }
                                            if(!jsonArray.getJSONObject(i).getString("IpdRE").equals("")) {
                                                IpdRE = jsonArray.getJSONObject(i).getString("IpdRE");
                                            }
                                            if(!jsonArray.getJSONObject(i).getString("IpdLE").equals("")) {
                                                IpdLE = jsonArray.getJSONObject(i).getString("IpdLE");
                                            }

                                            trendsListArraylist.add(new TrendOphthalAnalysisList(jsonArray.getJSONObject(i).getInt("trend_id"),
                                                    jsonArray.getJSONObject(i).getString("date_added"),DvSphereRE,
                                                    DvCylRE, DvAxisRE, DvSpeherLE, DvCylLE, DvAxisLE, NvSpeherRE, NvCylRE, NvAxisRE,
                                                    NvSpeherLE, NvCylLE, NvAxisLE, IpdRE, IpdLE, jsonArray.getJSONObject(i).getInt("patient_id"),
                                                    jsonArray.getJSONObject(i).getString("patient_type"),USER_ID, USER_LOGIN_TYPE));

                                        }

                                        if (jsonArray1.length() > 0) {
                                            /*for (int j = 0; j < jsonArray1.length(); j++) {
                                                prescriptionListArraylist.add(new FrequentPrescription(jsonArray1.getJSONObject(j).getInt("episode_prescription_id"),
                                                        jsonArray1.getJSONObject(j).getInt("pp_id"), jsonArray1.getJSONObject(j).getString("prescription_trade_name"),
                                                        0,jsonArray1.getJSONObject(j).getString("prescription_generic_name"),jsonArray1.getJSONObject(j).getString("prescription_frequency"),
                                                        jsonArray1.getJSONObject(j).getString("timing"), jsonArray1.getJSONObject(j).getString("duration"),
                                                        jsonArray1.getJSONObject(j).getInt("doc_id"),1,0,USER_ID, USER_LOGIN_TYPE,
                                                        jsonArray1.getJSONObject(j).getString("prescription_date_time")));

                                            }*/
                                        }

                                        if(trendsListArraylist.size() > 0) {
                                            showGraph(trendsListArraylist, prescriptionListArraylist, "ALL");
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
                map.put(APIClass.KEY_USERID, String.valueOf(Utils.USER_LOGIN_ID));
                map.put(APIClass.KEY_LOGINTYPE, Utils.USER_LOGIN_TYPE);
                map.put(APIClass.KEY_PATIENTID, String.valueOf(PATIENT_ID));
                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(getActivity()).
                getRequestQueue();
        AppController.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void showGraph(List<TrendOphthalAnalysisList> trendsListArraylist, List<FrequentPrescription> prescriptionListArraylist, String MODE) {

        lineChart.animateY(1000);
        lineChart.setTouchEnabled(true);
        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);

        // to draw any grid
        lineChart.setDrawGridBackground(true);

        lineChart.invalidate();
        lineChart.clear();

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        ArrayList<Entry> DvSphereRE = new ArrayList<>();
        ArrayList<Entry> DvCylRE = new ArrayList<>();
        ArrayList<Entry> DvAxisRE = new ArrayList<>();
        ArrayList<Entry> DvSpeherLE = new ArrayList<>();
        ArrayList<Entry> DvCylLE = new ArrayList<>();
        ArrayList<Entry> DvAxisLE = new ArrayList<>();
        ArrayList<Entry> NvSpeherRE = new ArrayList<>();
        ArrayList<Entry> NvCylRE = new ArrayList<>();
        ArrayList<Entry> NvAxisRE = new ArrayList<>();
        ArrayList<Entry> NvSpeherLE = new ArrayList<>();
        ArrayList<Entry> NvCylLE = new ArrayList<>();
        ArrayList<Entry> NvAxisLE = new ArrayList<>();
        ArrayList<Entry> IpdRE = new ArrayList<>();
        ArrayList<Entry> IpdLE = new ArrayList<>();
        final String[] xAxisValue = new String[trendsListArraylist.size()];

        for (int i = 0; i < trendsListArraylist.size(); i++) {
            DvSphereRE.add(new Entry(i, Float.parseFloat(trendsListArraylist.get(i).getDvSphereRE())));
            DvCylRE.add(new Entry(i, Float.parseFloat(trendsListArraylist.get(i).getDvCylRE())));
            DvAxisRE.add(new Entry(i, Float.parseFloat(trendsListArraylist.get(i).getDvAxisRE())));
            DvSpeherLE.add(new Entry(i, Float.parseFloat(trendsListArraylist.get(i).getDvSpeherLE())));
            DvCylLE.add(new Entry(i, Float.parseFloat(trendsListArraylist.get(i).getDvCylLE())));
            DvAxisLE.add(new Entry(i, Float.parseFloat(trendsListArraylist.get(i).getDvAxisLE())));
            NvSpeherRE.add(new Entry(i, Float.parseFloat(trendsListArraylist.get(i).getNvSpeherRE())));
            NvCylRE.add(new Entry(i, Float.parseFloat(trendsListArraylist.get(i).getNvCylRE())));
            NvAxisRE.add(new Entry(i, Float.parseFloat(trendsListArraylist.get(i).getNvAxisRE())));
            NvSpeherLE.add(new Entry(i, Float.parseFloat(trendsListArraylist.get(i).getNvSpeherLE())));
            NvCylLE.add(new Entry(i, Float.parseFloat(trendsListArraylist.get(i).getNvCylLE())));
            NvAxisLE.add(new Entry(i, Float.parseFloat(trendsListArraylist.get(i).getNvAxisLE())));
            IpdRE.add(new Entry(i, Float.parseFloat(trendsListArraylist.get(i).getIpdRE())));
            IpdLE.add(new Entry(i, Float.parseFloat(trendsListArraylist.get(i).getIpdLE())));
            xAxisValue[i] = trendsListArraylist.get(i).getTrendDate();
        }

        LineDataSet lDataSet1 = new LineDataSet(DvSphereRE, "DvSphereRE");
        lDataSet1.setDrawFilled(false);
        lDataSet1.setColor(Color.rgb(255, 159, 64));
        lDataSet1.setCircleColor(Color.rgb(255, 159, 64));
        lDataSet1.setLineWidth(2.5f);
        lDataSet1.setCircleSize(6.5f);
        lDataSet1.setCircleColorHole(Color.rgb(255, 159, 64));
        lDataSet1.setValueTextColor(Color.rgb(255, 159, 64));
        lDataSet1.setValueTextSize(10f);
        //    dataSets.add(lDataSet1);

        LineDataSet lDataSet2 = new LineDataSet(DvCylRE, "DvCylRE");
        lDataSet2.setDrawFilled(false);
        lDataSet2.setColor(Color.rgb(102, 205, 170));
        lDataSet2.setCircleColor(Color.rgb(102, 205, 170));
        lDataSet2.setLineWidth(2.5f);
        lDataSet2.setCircleSize(6.5f);
        lDataSet2.setCircleColorHole(Color.rgb(102, 205, 170));
        lDataSet2.setValueTextColor(Color.rgb(102, 205, 170));
        lDataSet2.setValueTextSize(10f);
        //  dataSets.add(lDataSet2);

        LineDataSet lDataSet3 = new LineDataSet(DvAxisRE, "DvAxisRE");
        lDataSet3.setDrawFilled(false);
        lDataSet3.setColor(Color.rgb(255, 0, 0));
        lDataSet3.setCircleColor(Color.rgb(255, 0, 0));
        lDataSet3.setLineWidth(2.5f);
        lDataSet3.setCircleSize(6.5f);
        lDataSet3.setCircleColorHole(Color.rgb(255, 0, 0));
        lDataSet3.setValueTextColor(Color.rgb(255, 0, 0));
        lDataSet3.setValueTextSize(10f);
        //   dataSets.add(lDataSet3);

        LineDataSet lDataSet4 = new LineDataSet(DvSpeherLE, "DvSpeherLE");
        lDataSet4.setDrawFilled(false);
        lDataSet4.setColor(Color.rgb(54, 162, 235));
        lDataSet4.setCircleColor(Color.rgb(54, 162, 235));
        lDataSet4.setLineWidth(2.5f);
        lDataSet4.setCircleSize(6.5f);
        lDataSet4.setCircleColorHole(Color.rgb(54, 162, 235));
        lDataSet4.setValueTextColor(Color.rgb(54, 162, 235));
        lDataSet4.setValueTextSize(10f);
        //   dataSets.add(lDataSet4);

        LineDataSet lDataSet5 = new LineDataSet(DvCylLE, "DvCylLE");
        lDataSet5.setDrawFilled(false);
        lDataSet5.setColor(Color.rgb(153, 102, 255));
        lDataSet5.setCircleColor(Color.rgb(153, 102, 255));
        lDataSet5.setLineWidth(2.5f);
        lDataSet5.setCircleSize(6.5f);
        lDataSet5.setCircleColorHole(Color.rgb(153, 102, 255));
        lDataSet5.setValueTextColor(Color.rgb(153, 102, 255));
        lDataSet5.setValueTextSize(10f);
        //  dataSets.add(lDataSet5);

        LineDataSet lDataSet6 = new LineDataSet(DvAxisLE, "DvAxisLE");
        lDataSet6.setDrawFilled(false);
        lDataSet6.setColor(Color.rgb(255, 205, 86));
        lDataSet6.setCircleColor(Color.rgb(255, 205, 86));
        lDataSet6.setLineWidth(2.5f);
        lDataSet6.setCircleSize(6.5f);
        lDataSet6.setCircleColorHole(Color.rgb(255, 205, 86));
        lDataSet6.setValueTextColor(Color.rgb(255, 205, 86));
        lDataSet6.setValueTextSize(10f);
        //  dataSets.add(lDataSet6);

        LineDataSet lDataSet7 = new LineDataSet(NvSpeherRE, "NvSpeherRE");
        lDataSet7.setDrawFilled(false);
        lDataSet7.setColor(Color.rgb(75, 192, 192));
        lDataSet7.setCircleColor(Color.rgb(75, 192, 192));
        lDataSet7.setLineWidth(2.5f);
        lDataSet7.setCircleSize(6.5f);
        lDataSet7.setCircleColorHole(Color.rgb(75, 192, 192));
        lDataSet7.setValueTextColor(Color.rgb(75, 192, 192));
        lDataSet7.setValueTextSize(10f);
        //  dataSets.add(lDataSet7);

        LineDataSet lDataSet8 = new LineDataSet(NvCylRE, "NvCylRE");
        lDataSet8.setDrawFilled(false);
        lDataSet8.setColor(Color.rgb(216,191,216));
        lDataSet8.setCircleColor(Color.rgb(216,191,216));
        lDataSet8.setLineWidth(2.5f);
        lDataSet8.setCircleSize(6.5f);
        lDataSet8.setCircleColorHole(Color.rgb(216,191,216));
        lDataSet8.setValueTextColor(Color.rgb(216,191,216));
        lDataSet8.setValueTextSize(10f);
        //  dataSets.add(lDataSet8);

        LineDataSet lDataSet9 = new LineDataSet(NvAxisRE, "NvAxisRE");
        lDataSet9.setDrawFilled(false);
        lDataSet9.setColor(Color.rgb(201, 203, 207));
        lDataSet9.setCircleColor(Color.rgb(201, 203, 207));
        lDataSet9.setLineWidth(2.5f);
        lDataSet9.setCircleSize(6.5f);
        lDataSet9.setCircleColorHole(Color.rgb(201, 203, 207));
        lDataSet9.setValueTextColor(Color.rgb(201, 203, 207));
        lDataSet9.setValueTextSize(10f);
        //  dataSets.add(lDataSet9);

        LineDataSet lDataSet10 = new LineDataSet(NvSpeherLE, "NvSpeherLE");
        lDataSet10.setDrawFilled(false);
        lDataSet10.setColor(Color.rgb(160, 82, 45));
        lDataSet10.setCircleColor(Color.rgb(160, 82, 45));
        lDataSet10.setLineWidth(2.5f);
        lDataSet10.setCircleSize(6.5f);
        lDataSet10.setCircleColorHole(Color.rgb(160, 82, 45));
        lDataSet10.setValueTextColor(Color.rgb(160, 82, 45));
        lDataSet10.setValueTextSize(10f);
        // dataSets.add(lDataSet10);

        LineDataSet lDataSet11 = new LineDataSet(NvCylLE, "NvCylLE");
        lDataSet11.setDrawFilled(false);
        lDataSet11.setColor(Color.rgb(0, 100, 0));
        lDataSet11.setCircleColor(Color.rgb(0, 100, 0));
        lDataSet11.setLineWidth(2.5f);
        lDataSet11.setCircleSize(6.5f);
        lDataSet11.setCircleColorHole(Color.rgb(0, 100, 0));
        lDataSet11.setValueTextColor(Color.rgb(0, 100, 0));
        lDataSet11.setValueTextSize(10f);
        // dataSets.add(lDataSet11);

        LineDataSet lDataSet12 = new LineDataSet(NvAxisLE, "NvAxisLE");
        lDataSet12.setDrawFilled(false);
        lDataSet12.setColor(Color.rgb(0, 139, 139));
        lDataSet12.setCircleColor(Color.rgb(0, 139, 139));
        lDataSet12.setLineWidth(2.5f);
        lDataSet12.setCircleSize(6.5f);
        lDataSet12.setCircleColorHole(Color.rgb(0, 139, 139));
        lDataSet12.setValueTextColor(Color.rgb(0, 139, 139));
        lDataSet12.setValueTextSize(10f);
        // dataSets.add(lDataSet12);

        LineDataSet lDataSet13 = new LineDataSet(IpdRE, "IpdRE");
        lDataSet13.setDrawFilled(false);
        lDataSet13.setColor(Color.rgb(0, 0, 128));
        lDataSet13.setCircleColor(Color.rgb(0, 0, 128));
        lDataSet13.setLineWidth(2.5f);
        lDataSet13.setCircleSize(6.5f);
        lDataSet13.setCircleColorHole(Color.rgb(0, 0, 128));
        lDataSet13.setValueTextColor(Color.rgb(0, 0, 128));
        lDataSet13.setValueTextSize(10f);
        // dataSets.add(lDataSet13);

        LineDataSet lDataSet14 = new LineDataSet(IpdLE, "IpdLE");
        lDataSet14.setDrawFilled(false);
        lDataSet14.setColor(Color.rgb(139, 0, 139));
        lDataSet14.setCircleColor(Color.rgb(139, 0, 139));
        lDataSet14.setLineWidth(2.5f);
        lDataSet14.setCircleSize(6.5f);
        lDataSet14.setCircleColorHole(Color.rgb(139, 0, 139));
        lDataSet14.setValueTextColor(Color.rgb(139, 0, 139));
        lDataSet14.setValueTextSize(10f);
        // dataSets.add(lDataSet14);

        if(MODE.equalsIgnoreCase("ALL")) {
            lineChart.notifyDataSetChanged();
            dataSets.add(lDataSet1);
            dataSets.add(lDataSet2);
            dataSets.add(lDataSet3);
            dataSets.add(lDataSet4);
            dataSets.add(lDataSet5);
            dataSets.add(lDataSet6);
            dataSets.add(lDataSet7);
            dataSets.add(lDataSet8);
            dataSets.add(lDataSet9);
            dataSets.add(lDataSet10);
            dataSets.add(lDataSet11);
            dataSets.add(lDataSet12);
            dataSets.add(lDataSet13);
            dataSets.add(lDataSet14);
        }
        else if(MODE.equalsIgnoreCase("DvSphereRE")) {
            lineChart.notifyDataSetChanged();
            dataSets.add(lDataSet1);
        }
        else if(MODE.equalsIgnoreCase("DvCylRE")) {
            lineChart.notifyDataSetChanged();
            dataSets.add(lDataSet2);
        }
        else if(MODE.equalsIgnoreCase("DvAxisRE")) {
            lineChart.notifyDataSetChanged();
            dataSets.add(lDataSet3);
        }
        else if(MODE.equalsIgnoreCase("DvSpeherLE")) {
            lineChart.notifyDataSetChanged();
            dataSets.add(lDataSet4);
        }
        else if(MODE.equalsIgnoreCase("DvCylLE")) {
            lineChart.notifyDataSetChanged();
            dataSets.add(lDataSet5);
        }
        else if(MODE.equalsIgnoreCase("DvAxisLE")) {
            lineChart.notifyDataSetChanged();
            dataSets.add(lDataSet6);
        }
        else if(MODE.equalsIgnoreCase("NvSpeherRE")) {
            lineChart.notifyDataSetChanged();
            dataSets.add(lDataSet7);
        }
        else if(MODE.equalsIgnoreCase("NvCylRE")) {
            lineChart.notifyDataSetChanged();
            dataSets.add(lDataSet8);
        }
        else if(MODE.equalsIgnoreCase("NvAxisRE")) {
            lineChart.notifyDataSetChanged();
            dataSets.add(lDataSet9);
        }
        else if(MODE.equalsIgnoreCase("NvSpeherLE")) {
            lineChart.notifyDataSetChanged();
            dataSets.add(lDataSet10);
        }
        else if(MODE.equalsIgnoreCase("NvCylLE")) {
            lineChart.notifyDataSetChanged();
            dataSets.add(lDataSet11);
        }
        else if(MODE.equalsIgnoreCase("NvAxisLE")) {
            lineChart.notifyDataSetChanged();
            dataSets.add(lDataSet12);
        }
        else if(MODE.equalsIgnoreCase("IpdRE")) {
            lineChart.notifyDataSetChanged();
            dataSets.add(lDataSet13);
        }
        else if(MODE.equalsIgnoreCase("IpdLE")) {
            lineChart.notifyDataSetChanged();
            dataSets.add(lDataSet14);
        }

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //   return xAxisValue[(int) value];
                try {
                    return xAxisValue[(int) value];
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    return "";
                }
            }
        };

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(formatter);
        xAxis.setLabelRotationAngle(-45);

        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setEnabled(false);

        Legend l = lineChart.getLegend();
        lineChart.getLegend().setWordWrapEnabled(true);
        l.setDrawInside(false);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setWordWrapEnabled(true);

        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        CustomMarkerView mv = new CustomMarkerView(getActivity(), formatter, prescriptionListArraylist, dataSets);
        mv.setChartView(lineChart); // For bounds control
        lineChart.setMarker(mv); // Set the marker to the chart

        LineData data = new LineData(dataSets);
        lineChart.setData(data);

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.i("VAL SELECTED",
                        "Value: " + e.getY() + ", xIndex: " + e.getX()
                                + ", DataSet index: " + h.getDataSetIndex());
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void customAddTrendListsView() {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogAnimationView);
        dialog.setContentView(R.layout.custom_health_trends_list);
        dialog.setTitle("Health Trend Analysis List");
        dialog.setCanceledOnTouchOutside(true);

        TableLayout table = new TableLayout(getActivity());

        table.setStretchAllColumns(true);
        table.setShrinkAllColumns(true);

        TableRow rowTitle = new TableRow(getActivity());
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TableRow rowMedLabels = new TableRow(getActivity());
       // TableRow rowPresc = new TableRow(getActivity());
        TableRow rowDvSphereRE = new TableRow(getActivity());
        TableRow rowDvCylRE = new TableRow(getActivity());
        TableRow rowDvAxisRE = new TableRow(getActivity());
        TableRow rowDvSpeherLE = new TableRow(getActivity());
        TableRow rowDvCylLE = new TableRow(getActivity());
        TableRow rowDvAxisLE = new TableRow(getActivity());
        TableRow rowNvSpeherRE = new TableRow(getActivity());
        TableRow rowNvCylRE = new TableRow(getActivity());
        TableRow rowNvAxisRE = new TableRow(getActivity());
        TableRow rowNvSpeherLE = new TableRow(getActivity());
        TableRow rowNvCylLE = new TableRow(getActivity());
        TableRow rowNvAxisLE = new TableRow(getActivity());
        TableRow rowIpdRE = new TableRow(getActivity());
        TableRow rowIpdLE = new TableRow(getActivity());


        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1f);

        lp.setMargins(10,30,10,10);
        rowMedLabels.setLayoutParams(lp);
        rowMedLabels.setBackgroundColor(getResources().getColor(R.color.light_grey));

//        rowPresc.setLayoutParams(lp);
//        rowPresc.setBackgroundColor(getResources().getColor(R.color.white));

        rowDvSphereRE.setLayoutParams(lp);
        rowDvSphereRE.setBackgroundColor(getResources().getColor(R.color.light_grey));

        rowDvCylRE.setLayoutParams(lp);
        rowDvCylRE.setBackgroundColor(getResources().getColor(R.color.white));

        rowDvAxisRE.setLayoutParams(lp);
        rowDvAxisRE.setBackgroundColor(getResources().getColor(R.color.light_grey));

        rowDvSpeherLE.setLayoutParams(lp);
        rowDvSpeherLE.setBackgroundColor(getResources().getColor(R.color.white));

        rowDvCylLE.setLayoutParams(lp);
        rowDvCylLE.setBackgroundColor(getResources().getColor(R.color.light_grey));

        rowDvAxisLE.setLayoutParams(lp);
        rowDvAxisLE.setBackgroundColor(getResources().getColor(R.color.white));

        rowNvSpeherRE.setLayoutParams(lp);
        rowNvSpeherRE.setBackgroundColor(getResources().getColor(R.color.light_grey));

        rowNvCylRE.setLayoutParams(lp);
        rowNvCylRE.setBackgroundColor(getResources().getColor(R.color.white));

        rowNvAxisRE.setLayoutParams(lp);
        rowNvAxisRE.setBackgroundColor(getResources().getColor(R.color.light_grey));

        rowNvSpeherLE.setLayoutParams(lp);
        rowNvSpeherLE.setBackgroundColor(getResources().getColor(R.color.white));

        rowNvCylLE.setLayoutParams(lp);
        rowNvCylLE.setBackgroundColor(getResources().getColor(R.color.light_grey));

        rowNvAxisLE.setLayoutParams(lp);
        rowNvAxisLE.setBackgroundColor(getResources().getColor(R.color.white));

        rowIpdRE.setLayoutParams(lp);
        rowIpdRE.setBackgroundColor(getResources().getColor(R.color.light_grey));

        rowIpdLE.setLayoutParams(lp);
        rowIpdLE.setBackgroundColor(getResources().getColor(R.color.white));

        TextView empty = new TextView(getActivity());

        // title column/row
        TextView title = new TextView(getActivity());
        // title.setText("Trend Analysis Table");
        title.setText("");

        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0,20,0,20);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 20;

        rowTitle.addView(title, params);

        // labels column
        TextView TestLabel = new TextView(getActivity());
        TestLabel.setText("Medical Tests");
        TestLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        TestLabel.setTypeface(Typeface.DEFAULT_BOLD);
        TestLabel.setGravity(Gravity.CENTER_VERTICAL);
        TestLabel.setPadding(0,10,0,10);

        TextView prescriptionsLabel = new TextView(getActivity());
        prescriptionsLabel.setText("Prescriptions");
        prescriptionsLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        prescriptionsLabel.setTypeface(Typeface.DEFAULT_BOLD);
        prescriptionsLabel.setPadding(0,10,0,10);

        TextView DvSphereRELabel = new TextView(getActivity());
        DvSphereRELabel.setText("DvSphereRE");
        DvSphereRELabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        DvSphereRELabel.setTypeface(Typeface.DEFAULT_BOLD);
        DvSphereRELabel.setPadding(0,10,0,10);

        TextView DvCylRELabel = new TextView(getActivity());
        DvCylRELabel.setText("DvCylRE");
        DvCylRELabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        DvCylRELabel.setTypeface(Typeface.DEFAULT_BOLD);
        DvCylRELabel.setPadding(0,10,0,10);

        TextView DvAxisRELabel = new TextView(getActivity());
        DvAxisRELabel.setText("DvAxisRE");
        DvAxisRELabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        DvAxisRELabel.setTypeface(Typeface.DEFAULT_BOLD);
        DvAxisRELabel.setPadding(0,10,0,10);

        TextView DvSpeherLELabel = new TextView(getActivity());
        DvSpeherLELabel.setText("DvSpeherLE");
        DvSpeherLELabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        DvSpeherLELabel.setTypeface(Typeface.DEFAULT_BOLD);
        DvSpeherLELabel.setPadding(0,10,0,10);

        TextView DvCylLELabel = new TextView(getActivity());
        DvCylLELabel.setText("DvCylLE");
        DvCylLELabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        DvCylLELabel.setTypeface(Typeface.DEFAULT_BOLD);
        DvCylLELabel.setPadding(0,10,0,10);

        TextView DvAxisLELabel = new TextView(getActivity());
        DvAxisLELabel.setText("DvAxisLE");
        DvAxisLELabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        DvAxisLELabel.setTypeface(Typeface.DEFAULT_BOLD);
        DvAxisLELabel.setPadding(0,10,0,10);

        TextView NvSpeherRELabel = new TextView(getActivity());
        NvSpeherRELabel.setText("NvSpeherRE");
        NvSpeherRELabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        NvSpeherRELabel.setTypeface(Typeface.DEFAULT_BOLD);
        NvSpeherRELabel.setPadding(0,10,0,10);

        TextView NvCylRELabel = new TextView(getActivity());
        NvCylRELabel.setText("NvCylRE");
        NvCylRELabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        NvCylRELabel.setTypeface(Typeface.DEFAULT_BOLD);
        NvCylRELabel.setPadding(0,10,0,10);

        TextView NvAxisRELabel = new TextView(getActivity());
        NvAxisRELabel.setText("NvAxisRE");
        NvAxisRELabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        NvAxisRELabel.setTypeface(Typeface.DEFAULT_BOLD);
        NvAxisRELabel.setPadding(0,10,0,10);

        TextView NvSpeherLELabel = new TextView(getActivity());
        NvSpeherLELabel.setText("NvSpeherLE");
        NvSpeherLELabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        NvSpeherLELabel.setTypeface(Typeface.DEFAULT_BOLD);
        NvSpeherLELabel.setPadding(0,10,0,10);

        TextView NvCylLELabel = new TextView(getActivity());
        NvCylLELabel.setText("NvCylLE");
        NvCylLELabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        NvCylLELabel.setTypeface(Typeface.DEFAULT_BOLD);
        NvCylLELabel.setPadding(0,10,0,10);

        TextView NvAxisLELabel = new TextView(getActivity());
        NvAxisLELabel.setText("NvAxisLE");
        NvAxisLELabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        NvAxisLELabel.setTypeface(Typeface.DEFAULT_BOLD);
        NvAxisLELabel.setPadding(0,10,0,10);

        TextView IpdRELabel = new TextView(getActivity());
        IpdRELabel.setText("IpdRE");
        IpdRELabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        IpdRELabel.setTypeface(Typeface.DEFAULT_BOLD);
        IpdRELabel.setPadding(0,10,0,10);

        TextView IpdLELabel = new TextView(getActivity());
        IpdLELabel.setText("IpdLE");
        IpdLELabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        IpdLELabel.setTypeface(Typeface.DEFAULT_BOLD);
        IpdLELabel.setPadding(0,10,0,10);

        rowMedLabels.addView(TestLabel);
       // rowPresc.addView(prescriptionsLabel);
        rowDvSphereRE.addView(DvSphereRELabel);
        rowDvCylRE.addView(DvCylRELabel);
        rowDvAxisRE.addView(DvAxisRELabel);
        rowDvSpeherLE.addView(DvSpeherLELabel);
        rowDvCylLE.addView(DvCylLELabel);
        rowDvAxisLE.addView(DvAxisLELabel);
        rowNvSpeherRE.addView(NvSpeherRELabel);
        rowNvCylRE.addView(NvCylRELabel);
        rowNvAxisRE.addView(NvAxisRELabel);
        rowNvSpeherLE.addView(NvSpeherLELabel);
        rowNvCylLE.addView(NvCylLELabel);
        rowNvAxisLE.addView(NvAxisLELabel);
        rowIpdRE.addView(IpdRELabel);
        rowIpdLE.addView(IpdLELabel);

        table.addView(rowTitle);
        table.addView(rowMedLabels);
      //  table.addView(rowPresc);
        table.addView(rowDvSphereRE);
        table.addView(rowDvCylRE);
        table.addView(rowDvAxisRE);
        table.addView(rowDvSpeherLE);
        table.addView(rowDvCylLE);
        table.addView(rowDvAxisLE);
        table.addView(rowNvSpeherRE);
        table.addView(rowNvCylRE);
        table.addView(rowNvAxisRE);
        table.addView(rowNvSpeherLE);
        table.addView(rowNvCylLE);
        table.addView(rowNvAxisLE);
        table.addView(rowIpdRE);
        table.addView(rowIpdLE);

        ScrollView sv = new ScrollView(getActivity());
        sv.addView(table);

        HorizontalScrollView container = (HorizontalScrollView) dialog.findViewById(R.id.trend_list_container);
        container.addView(sv);

        for(int i=0;i<trendsListArraylist.size();i++){
            boolean presc_status = false;

            TextView titleLabel = new TextView(getActivity());

            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            try {
                date = inputFormat.parse(" "+trendsListArraylist.get(i).getTrendDate());
                String hyperDate_new = outputFormat.format(date);
                titleLabel.setText(hyperDate_new);
                titleLabel.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                titleLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                titleLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
                titleLabel.setPadding(0,10,0,10);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            TextView getDvSphereREText = new TextView(getActivity());
            getDvSphereREText.setText(trendsListArraylist.get(i).getDvSphereRE());
            getDvSphereREText.setGravity(Gravity.CENTER_HORIZONTAL);
            getDvSphereREText.setPadding(0,10,0,10);

            TextView getDvCylREText = new TextView(getActivity());
            getDvCylREText.setText(trendsListArraylist.get(i).getDvCylRE());
            getDvCylREText.setGravity(Gravity.CENTER_HORIZONTAL);
            getDvCylREText.setPadding(0,10,0,10);

            TextView getDvAxisREText = new TextView(getActivity());
            getDvAxisREText.setText(trendsListArraylist.get(i).getDvAxisRE());
            getDvAxisREText.setGravity(Gravity.CENTER_HORIZONTAL);
            getDvAxisREText.setPadding(0,10,0,10);

            TextView getDvSpeherLEText = new TextView(getActivity());
            getDvSpeherLEText.setText(trendsListArraylist.get(i).getDvSpeherLE());
            getDvSpeherLEText.setGravity(Gravity.CENTER_HORIZONTAL);
            getDvSpeherLEText.setPadding(0,10,0,10);

            TextView getDvCylLEText = new TextView(getActivity());
            getDvCylLEText.setText(trendsListArraylist.get(i).getDvCylLE());
            getDvCylLEText.setGravity(Gravity.CENTER_HORIZONTAL);
            getDvCylLEText.setPadding(0,10,0,10);

            TextView getDvAxisLEText = new TextView(getActivity());
            getDvAxisLEText.setText(trendsListArraylist.get(i).getDvAxisLE());
            getDvAxisLEText.setGravity(Gravity.CENTER_HORIZONTAL);
            getDvAxisLEText.setPadding(0,10,0,10);

            TextView getNvSpeherREText = new TextView(getActivity());
            getNvSpeherREText.setText(trendsListArraylist.get(i).getNvSpeherRE());
            getNvSpeherREText.setGravity(Gravity.CENTER_HORIZONTAL);
            getNvSpeherREText.setPadding(0,10,0,10);

            TextView getNvCylREText = new TextView(getActivity());
            getNvCylREText.setText(trendsListArraylist.get(i).getNvCylRE());
            getNvCylREText.setGravity(Gravity.CENTER_HORIZONTAL);
            getNvCylREText.setPadding(0,10,0,10);

            TextView getNvAxisREText = new TextView(getActivity());
            getNvAxisREText.setText(trendsListArraylist.get(i).getNvAxisRE());
            getNvAxisREText.setGravity(Gravity.CENTER_HORIZONTAL);
            getNvAxisREText.setPadding(0,10,0,10);

            TextView getNvSpeherLEText = new TextView(getActivity());
            getNvSpeherLEText.setText(trendsListArraylist.get(i).getNvSpeherLE());
            getNvSpeherLEText.setGravity(Gravity.CENTER_HORIZONTAL);
            getNvSpeherLEText.setPadding(0,10,0,10);

            TextView getNvCylLEText = new TextView(getActivity());
            getNvCylLEText.setText(trendsListArraylist.get(i).getNvCylLE());
            getNvCylLEText.setGravity(Gravity.CENTER_HORIZONTAL);
            getNvCylLEText.setPadding(0,10,0,10);

            TextView getNvAxisLEText = new TextView(getActivity());
            getNvAxisLEText.setText(trendsListArraylist.get(i).getNvAxisLE());
            getNvAxisLEText.setGravity(Gravity.CENTER_HORIZONTAL);
            getNvAxisLEText.setPadding(0,10,0,10);

            TextView getIpdREText = new TextView(getActivity());
            getIpdREText.setText(trendsListArraylist.get(i).getIpdRE());
            getIpdREText.setGravity(Gravity.CENTER_HORIZONTAL);
            getIpdREText.setPadding(0,10,0,10);

            TextView getIpdLEText = new TextView(getActivity());
            getIpdLEText.setText(trendsListArraylist.get(i).getIpdLE());
            getIpdLEText.setGravity(Gravity.CENTER_HORIZONTAL);
            getIpdLEText.setPadding(0,10,0,10);

            rowMedLabels.addView(titleLabel);
            rowDvSphereRE.addView(getDvSphereREText);
            rowDvCylRE.addView(getDvCylREText);
            rowDvAxisRE.addView(getDvAxisREText);
            rowDvSpeherLE.addView(getDvSpeherLEText);
            rowDvCylLE.addView(getDvCylLEText);
            rowDvAxisLE.addView(getDvAxisLEText);
            rowNvSpeherRE.addView(getNvSpeherREText);
            rowNvCylRE.addView(getNvCylREText);
            rowNvAxisRE.addView(getNvAxisREText);
            rowNvSpeherLE.addView(getNvSpeherLEText);
            rowNvCylLE.addView(getNvCylLEText);
            rowNvAxisLE.addView(getNvAxisLEText);
            rowIpdRE.addView(getIpdREText);
            rowIpdLE.addView(getIpdLEText);

            final StringBuilder sb = new StringBuilder();
            if(prescriptionListArraylist.size() > 0) {

                Log.d(Utils.TAG, "size : " + prescriptionListArraylist.size());
                for(int k=0; k<prescriptionListArraylist.size();k++) {
                    String[] status_date_arrray = prescriptionListArraylist.get(k).getPrescGivenDate().split(" ");
                    Log.d(Utils.TAG, "inputDateStr : " +status_date_arrray[0]);
                    Log.d(Utils.TAG, "dat : " + trendsListArraylist.get(i).getTrendDate());
                    if (status_date_arrray[0].equals(trendsListArraylist.get(i).getTrendDate())) {
                        sb.append("\nProduct Name: "+prescriptionListArraylist.get(k).getTradeName());
                        sb.append("\nGeneric Name: "+prescriptionListArraylist.get(k).getGenericName());
                        sb.append("\nFrequency: "+prescriptionListArraylist.get(k).getDosage());
                        sb.append("\nDuration: "+prescriptionListArraylist.get(k).getDuration());
                        sb.append("\nTimings: "+prescriptionListArraylist.get(k).getTimings());
                        sb.append("\nGiven Date: "+prescriptionListArraylist.get(k).getPrescGivenDate());
                        sb.append("\n");

                        presc_status = true;

                    }
                    else {
                        presc_status = false;
                    }
                }
            }

            if(presc_status) {
                TextView prescText = new TextView(getActivity());
                prescText.setText("View");
                prescText.setGravity(Gravity.CENTER_HORIZONTAL);
             //   rowPresc.addView(prescText);

                prescText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //  Toast.makeText(TrendAnalysisActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
                        alertDialogBuilder.setTitle("Prescription Details");
                        alertDialogBuilder.setMessage(sb.toString());
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                });
            }
            else {
                TextView prescText = new TextView(getActivity());
                prescText.setText("");
                prescText.setGravity(Gravity.CENTER_HORIZONTAL);
              //  rowPresc.addView(prescText);
            }
        }

        dialog.show();
    }

}
