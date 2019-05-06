package com.medisensehealth.fdccontributor.activities.patients;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medisensehealth.fdccontributor.DataModel.DoctorList;
import com.medisensehealth.fdccontributor.DataModel.PatientHistory;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.DashboardActivity;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by lenovo on 10-03-2017.
 */
public class PatientsViewFragment extends Fragment {
    int Patient_ID;
    int DOCREF_ID;
    String Patient_View;
    ArrayList<PatientHistory> patientArraylist;
    ArrayList<PatientHistory> patientArraylist_shared;
    FragmentManager fragManager;
    FragmentTransaction fragTransaction;

    CustomTextView _enqirydate, _patient_id, _status, _name, _age, _gender, _location, _mobile, _chief_medcomp, _weight, _mertal_status, _profession;
    CustomTextView _hypertension, _diabetes, _contactperson, _email, _address, _city, _state, _country, _specialization, _treating_doctor;
    CustomTextView _treating_hopital, _brief_description, _query_todoc;

    int _int_patient_id, _int_gender, _int_weight, _int_hyper_cond, _int_diabetes_cond, _int_medDept, _int_status;
    String _string_name, _string_age,  _string_email,  _string_marital_status,  _string_qualification;
    String _string_contact_person,  _string_profession,  _string_patient_mob,  _string_patient_loc, _string_pat_state, _string_pat_country;
    String _string_patient_addrs, _string_patient_complaint, _string_patient_desc,  _string_pat_query,  _string_date;
    LinearLayout send_case_btn, book_appointment_btn;
    View detail_header;

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID, HOSPITAL_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE;
    String patient_name, contact_person_name, contact_num, email_addrs, patient_address;
    Calendar myCalendar;
    String get_appoint_date = "", get_appoint_time = "";
    int get_appoint_timeId =0;
    CustomTextView choose_date;
    AutoCompleteTextView autoDoctor;
    List<DoctorList> doctorListArraylist;
    DoctorList doctors;
    public List<String> suggest_doc;
    public ArrayAdapter<String> aAdapter_doc;
    String PATIENT_DETAILS, DOCTORS_DETAILS;
    Gson gson, gson1;
    LinearLayout parentView_linearLayout;

    String[] appointment_timeArray = {
            "-- Select Timing -- ",
            "08:00 AM To 09:00 AM",
            "09:00 AM To 10:00 AM",
            "10:00 AM To 11:00 AM",
            "11:00 AM To 12:00 PM",
            "12:00 PM To 01:00 PM",
            "01:00 PM To 02:00 PM",
            "02:00 PM To 03:00 PM",
            "03:00 PM To 04:00 PM",
            "04:00 PM To 05:00 PM",
            "05:00 PM To 06:00 PM",
            "06:00 PM To 07:00 PM",
            "07:00 PM To 08:00 PM",
            "08:00 PM To 09:00 PM"
    };

    int[] appointment_idArray = {
            0,
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8,
            9,
            10,
            11,
            12,
            13
    };


    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_patientview, vg, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Patient_ID = bundle.getInt("PATIENT_ID", 0);
            DOCREF_ID = bundle.getInt("DOCREF_ID", 0);
            Patient_View = bundle.getString("PATIENT_VIEW", "VIEW");
            Log.d(Utils.TAG + " View Pat Id:", String.valueOf(Patient_ID));
            Log.d(Utils.TAG + " View Ref Id:", String.valueOf(DOCREF_ID));
            Log.d(Utils.TAG + " View Pat:", String.valueOf(Patient_View));
        }

        root.setFocusableInTouchMode(true);
        root.requestFocus();

        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if(Patient_View.equals("PUSHVIEW")) {
                            Intent intent = new Intent(getActivity(), DashboardActivity.class);
                            intent.putExtra("LOGIN_TYPE", USER_LOGIN_TYPE);
                            intent.putExtra("USER_ID", USER_ID);
                            intent.putExtra("ENTRY_TYPE", "NORMAL");
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else {
                            getActivity().finish();
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragManager = getActivity().getSupportFragmentManager();
        patientArraylist = new ArrayList<>();
        patientArraylist_shared = new ArrayList<>();
        doctorListArraylist = new ArrayList<>();

        shareadPreferenceClass = new ShareadPreferenceClass(getActivity());
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(getActivity());

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            PATIENT_DETAILS = sharedPreferences.getString(HCConstants.PREF_PATIENTS_DETAILS, "");
            DOCTORS_DETAILS = sharedPreferences.getString(HCConstants.PREF_DOCTORS_DETAILS, "");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            PATIENT_DETAILS = sharedPreferences.getString(HCConstants.PREF_PATIENTS_DETAILS, "");
            DOCTORS_DETAILS = sharedPreferences.getString(HCConstants.PREF_DOCTORS_DETAILS, "");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {

            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            PATIENT_DETAILS = sharedPreferences.getString(HCConstants.PREF_PATIENTS_DETAILS, "");
            DOCTORS_DETAILS = sharedPreferences.getString(HCConstants.PREF_DOCTORS_DETAILS, "");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        Log.d(Utils.TAG, "PATIENT_DETAILS " + PATIENT_DETAILS);
        Log.d(Utils.TAG, "DOCTORS_DETAILS " + DOCTORS_DETAILS);

        initialization();

    }

    private void initialization() {
        myCalendar = Calendar.getInstance();

        detail_header = (View) getActivity().findViewById(R.id.patient_view_header);
        detail_header.setVisibility(View.VISIBLE);

        _enqirydate = (CustomTextView) getActivity().findViewById(R.id.view_penquiry_date);
        _patient_id = (CustomTextView) getActivity().findViewById(R.id.view_patient_id);
        _status = (CustomTextView) getActivity().findViewById(R.id.view_pstatus);
        _name = (CustomTextView) getActivity().findViewById(R.id.view_pname);
        _age = (CustomTextView) getActivity().findViewById(R.id.view_page);
        _gender = (CustomTextView) getActivity().findViewById(R.id.view_pgender);
        _location = (CustomTextView) getActivity().findViewById(R.id.view_plocation);
        _mobile = (CustomTextView) getActivity().findViewById(R.id.view_pmobile);
        _specialization = (CustomTextView) getActivity().findViewById(R.id.view_pspecialization);
        _weight = (CustomTextView) getActivity().findViewById(R.id.view_pweight);
        _mertal_status = (CustomTextView) getActivity().findViewById(R.id.view_pmaritalstatus);
        _profession = (CustomTextView) getActivity().findViewById(R.id.view_pprofession);
        _hypertension = (CustomTextView) getActivity().findViewById(R.id.view_phypertension);
        _diabetes = (CustomTextView) getActivity().findViewById(R.id.view_pdiabetes);
        _contactperson = (CustomTextView) getActivity().findViewById(R.id.view_pcontactperson);
        _email = (CustomTextView) getActivity().findViewById(R.id.view_pemail);
        _address = (CustomTextView) getActivity().findViewById(R.id.view_paddress);
        _city = (CustomTextView) getActivity().findViewById(R.id.view_pcity);
        _state = (CustomTextView) getActivity().findViewById(R.id.view_pstate);
        _country = (CustomTextView) getActivity().findViewById(R.id.view_pcountry);
        _treating_doctor = (CustomTextView) getActivity().findViewById(R.id.view_eptreating_doc);
        _treating_hopital = (CustomTextView) getActivity().findViewById(R.id.view_ptreating_hospital);
        _chief_medcomp = (CustomTextView) getActivity().findViewById(R.id.view_pchief_medcomp);
        _brief_description = (CustomTextView) getActivity().findViewById(R.id.view_pbrief_desc);
        _query_todoc = (CustomTextView) getActivity().findViewById(R.id.view_pquery_doc);
        send_case_btn = (LinearLayout)  getActivity().findViewById(R.id.patient_send_case);
        book_appointment_btn = (LinearLayout)  getActivity().findViewById(R.id.patient_book_apppoint);

        send_case_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("PATIENT_ID", Patient_ID);
                bundle.putInt("DOCREF_ID", DOCREF_ID);
                bundle.putString("DOCTOR_TYPE", "1");
             /*   SendCaseFragment sendcaseFragment = new SendCaseFragment();
                fragTransaction = fragManager.beginTransaction();
                sendcaseFragment.setArguments(bundle);
                fragTransaction.replace(R.id.viewpager,sendcaseFragment,"SEND_CASE");
                fragTransaction.commit();*/

                Intent i1 = new Intent(getActivity(), SendCaseActivity.class);
                i1.putExtra("title","Reassign Case");
                i1.putExtra("PATIENT_ID", Patient_ID);
                i1.putExtra("DOCREF_ID", DOCREF_ID);
                i1.putExtra("DOCTOR_TYPE", "1");
                startActivity(i1);

            }
        });

        book_appointment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Utils.TAG, " ****** Book Appointment *******");
                Log.d(Utils.TAG, "patient Id: "+Patient_ID);
                Log.d(Utils.TAG, "DOCREF_ID: "+DOCREF_ID);
                Log.d(Utils.TAG, "patient_name: " + patient_name);
                Log.d(Utils.TAG, "contact_person: " + contact_person_name);
                Log.d(Utils.TAG, "contact_num: " + contact_num);
                Log.d(Utils.TAG, "email_addrs: " + email_addrs);
                Log.d(Utils.TAG, "patient_address: " + patient_address);

                gson1 = new Gson();
                if (DOCTORS_DETAILS.equals("")) {
                    Log.d(Utils.TAG, "DOCTORS_DETAILS EMPTY ");

                    if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
                        insertDoctorDetailsfromServer("normal");
                    } else {
                        AppUtils.showCustomAlertMessage(getActivity(), HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
                    }

                } else {
                    doctorListArraylist = gson1.fromJson(DOCTORS_DETAILS, new TypeToken<List<DoctorList>>() {
                    }.getType());
                    Log.d(Utils.TAG, "DOCTORS_DETAILS > 0 "+doctorListArraylist.size());
                    requestForAppointment(Patient_ID,DOCREF_ID,patient_name,contact_person_name,contact_num,email_addrs,patient_address);

                }

              }
        });

     /*   setpatientdeatilsresquesttoServer();

        if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
            insertDoctorDetailsfromServer("normal");
        } else {
            AppUtils.showCustomAlertMessage(getActivity(), HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
        }*/

        gson = new Gson();
        if (PATIENT_DETAILS.equals("")) {
            Log.d(Utils.TAG, "PATIENT_DETAILS EMPTY ");

            if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
                setpatientdeatilsresquesttoServer();
            } else {
                AppUtils.showCustomAlertMessage(getActivity(), HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            patientArraylist_shared = gson.fromJson(PATIENT_DETAILS, new TypeToken<List<PatientHistory>>() {
            }.getType());
            Log.d(Utils.TAG, "PATIENT_DETAILS > 0 " + patientArraylist_shared.size());
            if(patientArraylist_shared.size() > 0 ) {
                int count = patientArraylist_shared.size() -1;
                     if(patientArraylist_shared.get(count).getPatientId() == Patient_ID) {
                        Log.d(Utils.TAG, " same patient ");
                        preparePatientDetails(patientArraylist_shared);
                    }
                    else {
                        Log.d(Utils.TAG, " different patient ");
                        setpatientdeatilsresquesttoServer();
                    }
            }
        }

       /* gson1 = new Gson();
        if (DOCTORS_DETAILS.equals("")) {
            Log.d(Utils.TAG, "DOCTORS_DETAILS EMPTY ");

            if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
                insertDoctorDetailsfromServer("normal");
            } else {
                AppUtils.showCustomAlertMessage(getActivity(), HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }

        } else {
            doctorListArraylist = gson1.fromJson(DOCTORS_DETAILS, new TypeToken<List<DoctorList>>() {
            }.getType());
            Log.d(Utils.TAG, "DOCTORS_DETAILS > 0 ");
        }*/

    }


    private void requestForAppointment(final int patient_id, final int docref_id, final String patient_name, final String contact_person_name, final String contact_num, final String email_addrs, final String patient_address) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.patient_appointment_book, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        alertDialogBuilder.setView(promptsView);

        choose_date = (CustomTextView) promptsView.findViewById(R.id.pateditTextDialogUserInput);
        autoDoctor = (AutoCompleteTextView) promptsView.findViewById(R.id.pat_autoCompleteTextView1);
        choose_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_date.setText("");
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        autoDoctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(autoDoctor.getWindowToken(), 0);
            }
        });

        Log.d(Utils.TAG, " doc Size: " + String.valueOf(doctorListArraylist.size()));
        suggest_doc = new ArrayList<String>();
        for (int count = 0; count < doctorListArraylist.size(); count++) {
            String name = doctorListArraylist.get(count).getDoctorName();
            String specName = doctorListArraylist.get(count).getDoctorSpecificationName();
            int docID = doctorListArraylist.get(count).getDoctorId();
            suggest_doc.add(name+ ", "+specName+" ["+docID+"]");
        }

        autoDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoDoctor.setText("");
            }
        });

        aAdapter_doc = new ArrayAdapter<String>(getActivity(),  R.layout.support_simple_spinner_dropdown_item, suggest_doc);
        autoDoctor.setThreshold(1);
        autoDoctor.setTextColor(Color.BLACK);
        autoDoctor.setAdapter(aAdapter_doc);
        aAdapter_doc.notifyDataSetChanged();

       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item ,language);
        autoDoctor.setThreshold(1);//will start working from first character
        autoDoctor.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        autoDoctor.setTextColor(Color.BLACK);*/

      /*  autoDoctor.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable editable) {
                // TODO Auto-generated method stub
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cityText = s.toString();
                new getDoctorsFromServer().execute(cityText);
            }

        });*/


        final Spinner timeSpinner = (Spinner) promptsView.findViewById(R.id.pat_spinner_appt_time);
        ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(getActivity(), R.layout.spinner_text, appointment_timeArray );
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        timeSpinner.setAdapter(langAdapter);
        timeSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        int position = timeSpinner.getSelectedItemPosition();
                        get_appoint_time = appointment_timeArray[position];
                        get_appoint_timeId = appointment_idArray[position];
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
        );
        // set dialog message
        alertDialogBuilder
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                String sel_doctor = autoDoctor.getText().toString().trim();
                                if(sel_doctor.equals("")) {
                                    Toast.makeText(getActivity(), "Select Doctor", Toast.LENGTH_SHORT ).show();
                                }
                                else if(get_appoint_date.equals("")) {
                                    Toast.makeText(getActivity(), "Select Reschedule Date", Toast.LENGTH_SHORT ).show();
                                }
                                else  if(get_appoint_timeId == 0) {
                                    Toast.makeText(getActivity(), "Select Reschedule Time", Toast.LENGTH_SHORT ).show();
                                }
                                else {
                                   //   Toast.makeText(getActivity(), get_appoint_date + " : "+get_appoint_timeId, Toast.LENGTH_SHORT ).show();
                                    String doc_Id = "0";
                                    String[] separated = sel_doctor.split(" \\[");
                                    if(separated!= null && separated.length > 1 ) {

                                        String name = separated[0];
                                        doc_Id = separated[1];
                                        doc_Id = doc_Id.replace("]", "");
                                        Log.d(Utils.TAG, " docID: "+doc_Id);
                                    }

                                    Log.d(Utils.TAG, "submitRescheduleToServer");
                                    Log.d(Utils.TAG, "patient Id: "+patient_id);
                                    Log.d(Utils.TAG, "patient_name: " + patient_name);
                                    Log.d(Utils.TAG, "contact_person: " + contact_person_name);
                                    Log.d(Utils.TAG, "contact_num: " + contact_num);
                                    Log.d(Utils.TAG, "email_addrs: " + email_addrs);
                                    Log.d(Utils.TAG, "patient_address: " + patient_address);
                                    Log.d(Utils.TAG, "get_appoint_date: " + get_appoint_date);
                                    Log.d(Utils.TAG, "get_appoint_timeId: " + get_appoint_timeId);

                                    if(doc_Id.equals("0") ) {
                                        Toast.makeText(getActivity(), "Select doctor from the list", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        submitAppointmetToServer(get_appoint_date,get_appoint_timeId, patient_id, doc_Id, patient_name,
                                                contact_person_name, contact_num, email_addrs, patient_address);

                                    }

                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

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

            int a = dareSelected.compareTo(currentDate);
             Log.d(Utils.TAG, " a: " + String.valueOf(a));   // -1: past, 0: current, 1: future
            if(a<=0){
                Toast.makeText(getActivity(), "Invalid Date", Toast.LENGTH_SHORT).show();
            }else{
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                choose_date.setText(sdf.format(dareSelected.getTime()));

                String outFormat = "yyyy/MM/dd";
                SimpleDateFormat sdf_out = new SimpleDateFormat(outFormat, Locale.US);
                get_appoint_date = sdf_out.format(dareSelected.getTime());

            }

        }
    };

    private void setpatientdeatilsresquesttoServer() {
        final ProgressDialog progress = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        progress.setIndeterminate(true);
        progress.setMessage("Loading details...");
        progress.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PATIENT_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                          Log.d(Utils.TAG, " pview " + response.toString());
                        if (response != null) {
                            GetJsonArrayResponse(response);
                            progress.dismiss();
                        }else {
                            progress.dismiss();
                            //	AppUtil.showAlert(getContext(), "Server Error!!!","You Can Try it later", "OK", null);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                map.put(APIClass.KEY_PATIENTID, String.valueOf(Patient_ID));
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(getContext()).
                getRequestQueue();
        AppController.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void GetJsonArrayResponse(String response) {
        try {
            patientArraylist = new ArrayList<>();
            patientArraylist_shared = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(response);
            String staus_res = jsonObject.getString("status");
            Log.d(Utils.TAG, staus_res);

            if(staus_res.equals("false"))
            {
                AppUtils.showCustomErrorMessage(getActivity(),  "Patient Details","No results found", "OK", null, null);
            }
            else {
                JSONArray jsonArray = jsonObject.getJSONArray("patient_details");

                if (jsonArray.length() > 0) {
                    int max_count = jsonArray.length() - 1;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        patientArraylist.add(new PatientHistory(jsonArray.getJSONObject(i).getInt("patient_id"),jsonArray.getJSONObject(i).getString("patient_name"),jsonArray.getJSONObject(i).getString("patient_age"),
                                jsonArray.getJSONObject(i).getInt("patient_gen"),jsonArray.getJSONObject(i).getString("patient_email"),jsonArray.getJSONObject(i).getString("merital_status"),
                                jsonArray.getJSONObject(i).getString("qualification"),jsonArray.getJSONObject(i).getString("contact_person"),jsonArray.getJSONObject(i).getString("profession"),
                                jsonArray.getJSONObject(i).getString("patient_mob"),jsonArray.getJSONObject(i).getString("patient_loc"),jsonArray.getJSONObject(i).getString("pat_state"),
                                jsonArray.getJSONObject(i).getString("pat_country"),jsonArray.getJSONObject(i).getString("patient_addrs"),jsonArray.getJSONObject(i).getString("patient_complaint"),
                                jsonArray.getJSONObject(i).getString("patient_desc"),jsonArray.getJSONObject(i).getString("pat_query"),jsonArray.getJSONObject(i).getString("assign_date"),
                                jsonArray.getJSONObject(i).getInt("weight"),jsonArray.getJSONObject(i).getInt("hyper_cond"),jsonArray.getJSONObject(i).getInt("diabetes_cond"),
                                jsonArray.getJSONObject(i).getInt("medDept"),jsonArray.getJSONObject(i).getInt("status2"),
                                jsonArray.getJSONObject(i).getInt("spec_id"),jsonArray.getJSONObject(i).getString("spec_name"),
                                jsonArray.getJSONObject(i).getInt("ref_id"),jsonArray.getJSONObject(i).getString("ref_name"),
                                jsonArray.getJSONObject(i).getString("currentTreatDoc"),jsonArray.getJSONObject(i).getString("currentTreatHosp")));

                        patientArraylist_shared.add(new PatientHistory(jsonArray.getJSONObject(max_count).getInt("patient_id"),jsonArray.getJSONObject(max_count).getString("patient_name"),jsonArray.getJSONObject(max_count).getString("patient_age"),
                                jsonArray.getJSONObject(max_count).getInt("patient_gen"),jsonArray.getJSONObject(max_count).getString("patient_email"),jsonArray.getJSONObject(max_count).getString("merital_status"),
                                jsonArray.getJSONObject(max_count).getString("qualification"),jsonArray.getJSONObject(max_count).getString("contact_person"),jsonArray.getJSONObject(max_count).getString("profession"),
                                jsonArray.getJSONObject(max_count).getString("patient_mob"),jsonArray.getJSONObject(max_count).getString("patient_loc"),jsonArray.getJSONObject(max_count).getString("pat_state"),
                                jsonArray.getJSONObject(max_count).getString("pat_country"),jsonArray.getJSONObject(max_count).getString("patient_addrs"),jsonArray.getJSONObject(max_count).getString("patient_complaint"),
                                jsonArray.getJSONObject(max_count).getString("patient_desc"),jsonArray.getJSONObject(max_count).getString("pat_query"),jsonArray.getJSONObject(max_count).getString("assign_date"),
                                jsonArray.getJSONObject(max_count).getInt("weight"),jsonArray.getJSONObject(max_count).getInt("hyper_cond"),jsonArray.getJSONObject(max_count).getInt("diabetes_cond"),
                                jsonArray.getJSONObject(max_count).getInt("medDept"),jsonArray.getJSONObject(max_count).getInt("status2"),
                                jsonArray.getJSONObject(max_count).getInt("spec_id"),jsonArray.getJSONObject(max_count).getString("spec_name"),
                                jsonArray.getJSONObject(max_count).getInt("ref_id"),jsonArray.getJSONObject(max_count).getString("ref_name"),
                                jsonArray.getJSONObject(max_count).getString("currentTreatDoc"),jsonArray.getJSONObject(max_count).getString("currentTreatHosp")));

                    }
                }

                //Set the values
                gson = new Gson();
                String jsonText = gson.toJson(patientArraylist_shared);
                if (sharedPreferences != null) {
                    shareadPreferenceClass.clearPatientDetails();
                    shareadPreferenceClass.setPatientDetails(jsonText);
                }

                if(patientArraylist.size() > 0) {
                    preparePatientDetails(patientArraylist);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void preparePatientDetails(ArrayList<PatientHistory> patientArraylist) {
        String gender = "", hypercondition = "", diabetescondition = "", status = "";
        for (int i = 0; i < patientArraylist.size(); i++) {
            //   Log.d(Utils.TAG, String.valueOf(patientArraylist.get(i).getPatientId()));
            /*        Log.d(Utils.TAG, patientArraylist.get(i).getPatientDate());
                    Log.d(Utils.TAG, String.valueOf(patientArraylist.get(i).getPatientStatus()));
                    Log.d(Utils.TAG, patientArraylist.get(i).getPatientName());
                    Log.d(Utils.TAG, patientArraylist.get(i).getPatientAge());
                    Log.d(Utils.TAG, String.valueOf(patientArraylist.get(i).getPatientGender()));
                    Log.d(Utils.TAG, patientArraylist.get(i).getPatientMaritalStatus());
                    Log.d(Utils.TAG, String.valueOf(patientArraylist.get(i).getPatientWeight()));
                    Log.d(Utils.TAG, String.valueOf(patientArraylist.get(i).getPatientHyperCond()));
                    Log.d(Utils.TAG, String.valueOf(patientArraylist.get(i).getPatientDiabetes()));
                    Log.d(Utils.TAG, patientArraylist.get(i).getPatientQualification());
                    Log.d(Utils.TAG, patientArraylist.get(i).getPatientLocation());
                    Log.d(Utils.TAG, patientArraylist.get(i).getPatientAddress());
                    Log.d(Utils.TAG, patientArraylist.get(i).getPatientState());
                    Log.d(Utils.TAG, patientArraylist.get(i).getPatientCountry());
                    Log.d(Utils.TAG, patientArraylist.get(i).getPatientContactPerson());
                    Log.d(Utils.TAG, patientArraylist.get(i).getPatientMobile());
                    Log.d(Utils.TAG, patientArraylist.get(i).getPatientEmail());
                    Log.d(Utils.TAG, String.valueOf(patientArraylist.get(i).getPatientMedDept()));
                    Log.d(Utils.TAG, patientArraylist.get(i).getPatientProfession());
                    Log.d(Utils.TAG, patientArraylist.get(i).getPatientComplaint());
                    Log.d(Utils.TAG, patientArraylist.get(i).getPatientQuery());
                    Log.d(Utils.TAG, patientArraylist.get(i).getPatientDesc());   */

            patient_name = patientArraylist.get(i).getPatientName();
            contact_person_name = patientArraylist.get(i).getPatientContactPerson();
            contact_num = patientArraylist.get(i).getPatientMobile();
            email_addrs = patientArraylist.get(i).getPatientEmail();
            patient_address = patientArraylist.get(i).getPatientLocation()+"\n"+ patientArraylist.get(i).getPatientAddress()+ "\n"+patientArraylist.get(i).getPatientState()+"\n"+patientArraylist.get(i).getPatientCountry();


            if(patientArraylist.get(i).getPatientGender() == 1){
                gender = "Male";
            }
            else  if(patientArraylist.get(i).getPatientGender() == 2) {
                gender = "Female";
            }
            else  if(patientArraylist.get(i).getPatientGender() == 3) {
                gender = "Trans*";
            }

            if(patientArraylist.get(i).getPatientHyperCond() == 1) {
                hypercondition = "Yes";
            }
            else  if(patientArraylist.get(i).getPatientHyperCond() == 0) {
                hypercondition = "";
            }
            else  if(patientArraylist.get(i).getPatientHyperCond() == 2) {
                hypercondition = "No";
            }

            if(patientArraylist.get(i).getPatientDiabetes() == 1) {
                diabetescondition = "Yes";
            }
            else  if(patientArraylist.get(i).getPatientDiabetes() == 0) {
                diabetescondition = "";
            }
            else  if(patientArraylist.get(i).getPatientDiabetes() == 2) {
                diabetescondition = "No";
            }

            // Check Status of Patient detail
            if(patientArraylist.get(i).getPatientStatus() == 1){
                status = "New";
            }
            else if(patientArraylist.get(i).getPatientStatus() == 2){
                status = "Sent";
            }
            else if(patientArraylist.get(i).getPatientStatus() == 3){
                status = "Awaiting";
            }
            else if(patientArraylist.get(i).getPatientStatus() == 4 ){
                status = "Not Qualified";
            }
            else if(patientArraylist.get(i).getPatientStatus() == 5){
                status = "Responded";
            }
            else if(patientArraylist.get(i).getPatientStatus() == 6){
                status = "Response P-Failed";
            }
            else if(patientArraylist.get(i).getPatientStatus() == 7){
                status = "Staged";
            }
            else if(patientArraylist.get(i).getPatientStatus() == 8){
                status = "OP Desired";
            }
            else if(patientArraylist.get(i).getPatientStatus() == 9){
                status = "IP Converted";
            }
            else if(patientArraylist.get(i).getPatientStatus() == 10){
                status = "Not Converted";
            }
            else if(patientArraylist.get(i).getPatientStatus() == 11){
                status = "Invoiced";
            }
            else if(patientArraylist.get(i).getPatientStatus() == 12){
                status = "Payment Received";
            }

            //        _city, _treating_doctor, _treating_hopital;

            //      _text_patientid.setText(String.valueOf(patientArraylist.get(i).getPatientId()));
            _enqirydate.setText(patientArraylist.get(i).getPatientDate());
            _patient_id.setText(String.valueOf(patientArraylist.get(i).getPatientId()));
            _status.setText(status);
            _name.setText(patientArraylist.get(i).getPatientName());
            _age.setText(patientArraylist.get(i).getPatientAge());
            _gender.setText(gender);
            _mertal_status.setText(patientArraylist.get(i).getPatientMaritalStatus());
            _weight.setText(String.valueOf(patientArraylist.get(i).getPatientWeight()));
            _hypertension.setText(hypercondition);
            _diabetes.setText(diabetescondition);
            // _profession.setText(patientArraylist.get(i).getPatientQualification());
            _location.setText(patientArraylist.get(i).getPatientLocation());
            _city.setText(patientArraylist.get(i).getPatientLocation());
            _address.setText(patientArraylist.get(i).getPatientAddress());
            _state.setText(patientArraylist.get(i).getPatientState());
            _country.setText(patientArraylist.get(i).getPatientCountry());
            _contactperson.setText(patientArraylist.get(i).getPatientContactPerson());
            _mobile.setText(patientArraylist.get(i).getPatientMobile());
            _email.setText(patientArraylist.get(i).getPatientEmail());
            _specialization.setText(patientArraylist.get(i).getSpecializationName());
            _profession.setText(patientArraylist.get(i).getPatientProfession());
            _chief_medcomp.setText(patientArraylist.get(i).getPatientComplaint());
            _query_todoc.setText(patientArraylist.get(i).getPatientQuery());
            _brief_description.setText(patientArraylist.get(i).getPatientDesc());
            _treating_doctor.setText(patientArraylist.get(i).getCurrentTreatedDoctor());
            _treating_hopital.setText(patientArraylist.get(i).getCurrentTreatedHospital());
        }
    }

    private void insertDoctorDetailsfromServer(String mode_of_operation) {

        final ProgressDialog  progressDialog2 = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog2.setIndeterminate(true);
        progressDialog2.setMessage("Loading doctors ...");
        progressDialog2.show();

        doctorListArraylist = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PREM_DOCTOR_LIST_ALL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.d(Utils.TAG, "DOC RES"+ response.toString());
                        if (response != null) {
                            Log.d(Utils.TAG, "doc response: " + response);
                            JSONObject jsonObject = null;
                            JSONArray jsonArray;
                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");

                                if (staus_res.equals("false")) {
                                    AppUtils.showCustomErrorMessage(getActivity(), "OOPS", "No doctor records found !!", "OK", null, null);
                                } else {
                                    jsonArray = jsonObject.getJSONArray("doctor_list");
                                    Log.d(Utils.TAG, "doc length: " + String.valueOf(jsonArray.length()));
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            doctors = new DoctorList(jsonArray.getJSONObject(i).getInt("ref_id"), jsonArray.getJSONObject(i).getString("doc_name"),jsonArray.getJSONObject(i).getInt("doc_age"),
                                                    jsonArray.getJSONObject(i).getString("doc_exp"),jsonArray.getJSONObject(i).getString("doc_photo"), jsonArray.getJSONObject(i).getString("doc_qual"),
                                                    jsonArray.getJSONObject(i).getInt("spec_id"),jsonArray.getJSONObject(i).getString("spec_name"),USER_LOGIN_TYPE,USER_ID,jsonArray.getJSONObject(i).getString("ref_address"));
                                            doctorListArraylist.add(doctors);
                                        }

                                        if(doctorListArraylist.size() > 0) {
                                            //Set the values
                                            gson1 = new Gson();
                                            String jsonText1 = gson1.toJson(doctorListArraylist);
                                            if (sharedPreferences != null) {
                                                shareadPreferenceClass.clearDoctorsDetails();
                                                shareadPreferenceClass.setDoctorsDetails(jsonText1);
                                            }

                                            requestForAppointment(Patient_ID,DOCREF_ID,patient_name,contact_person_name,contact_num,email_addrs,patient_address);

                                        }
                                    }
                                }
                                progressDialog2.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog2.dismiss();
                            }

                            progressDialog2.dismiss();
                        }else {
                            progressDialog2.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog2.dismiss();
                        //   AppUtil.showCustomAlert(getContext(), "Server Error!!!","You Can Try it later", "OK", null);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                map.put(APIClass.KEY_USERID, String.valueOf(USER_ID));
                map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                map.put(APIClass.KEY_DOCTOR_TYPE, "1");
                map.put(APIClass.KEY_DOCTOR_SPECID, "0");
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(getContext()).
                getRequestQueue();
        AppController.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    class getDoctorsFromServer extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... key) {
            String cityText = key[0];
            cityText = cityText.trim();
            cityText = cityText.replace(" ", "+");
            Log.d(Utils.TAG + " cityText:", cityText);
            suggest_doc = new ArrayList<String>();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                 // suggest_doc = new ArrayList<String>();
                    for (int count = 0; count < doctorListArraylist.size(); count++) {
                        String name = doctorListArraylist.get(count).getDoctorName();
                        int docID = doctorListArraylist.get(count).getDoctorId();

                        Log.d(Utils.TAG + " name:", name);
                        Log.d(Utils.TAG + " docID:", String.valueOf(docID));
                        suggest_doc.add(name);
                    }
                    aAdapter_doc = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, suggest_doc);
                    autoDoctor.setAdapter(aAdapter_doc);
                    aAdapter_doc.notifyDataSetChanged();

                }
            });
            return null;
        }
    }

    private void submitAppointmetToServer(final String get_appoint_date, final int get_appoint_timeId, final int patient_id, final String doc_id, final String patient_name, final String contact_person_name, final String contact_num, final String email_addrs, final String patient_address) {

        final ProgressDialog progressDialog3 = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog3.setIndeterminate(true);
        progressDialog3.setMessage("Requesting Appointment...");
        progressDialog3.show();

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_PRACTICE_BOOK_APPOINTMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG, "apptReq: " + response.toString());
                        if(response != null) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                                String staus_res = jsonObject.getString("status");
                                String result_msg = jsonObject.getString("book_appointment");

                                if (staus_res.equals("true")) {
                                    AppUtils.showCustomSuccessMessage(getActivity(), "Appointment Request",result_msg, "OK", null, null);
                                }

                                progressDialog3.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog3.dismiss();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog3.dismiss();
                        Toast.makeText(getActivity(), "Unable to book an appointment. \n Please check your internet connection !!!", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_USER_USERID, String.valueOf(Utils.USER_LOGIN_ID));
                map.put(APIClass.KEY_USER_LOGINTYPE, Utils.USER_LOGIN_TYPE);
                map.put(APIClass.KEY_BOOK_APPOINT_DOCID, doc_id);
                map.put(APIClass.KEY_BOOK_APPOINT_PATIENTID, String.valueOf(patient_id));
                map.put(APIClass.KEY_BOOK_APPOINT_SELTIME, String.valueOf(get_appoint_timeId));
                map.put(APIClass.KEY_BOOK_APPOINT_SELDATE, get_appoint_date);

                if(patient_name != null){
                    map.put(APIClass.KEY_BOOK_APPOINT_NAME, patient_name);
                }
                if(contact_person_name != null){
                    map.put(APIClass.KEY_BOOK_APPOINT_CONTACT_NAME, contact_person_name);
                }
                if(contact_num != null){
                    map.put(APIClass.KEY_BOOK_APPOINT_MOBILE, contact_num);
                }
                if(email_addrs != null){
                    map.put(APIClass.KEY_BOOK_APPOINT_EMAIL, email_addrs);
                }
                if(patient_address != null){
                    map.put(APIClass.KEY_BOOK_APPOINT_ADDRESS, patient_address);
                }

                return map;
            }
        };

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = AppController.getInstance(getContext()).
                getRequestQueue();
        AppController.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}
