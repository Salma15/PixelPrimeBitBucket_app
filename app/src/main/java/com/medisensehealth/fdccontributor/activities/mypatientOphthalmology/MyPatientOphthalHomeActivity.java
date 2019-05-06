package com.medisensehealth.fdccontributor.activities.mypatientOphthalmology;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medisensehealth.fdccontributor.DataModel.DiagnosisTestLists;
import com.medisensehealth.fdccontributor.DataModel.EpisodesList;
import com.medisensehealth.fdccontributor.DataModel.HospitalList;
import com.medisensehealth.fdccontributor.DataModel.PrescriptionList;
import com.medisensehealth.fdccontributor.DataModel.SelectedTestLists;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology.MenuAdapter;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

/**
 * Created by SALMA on 07-09-2018.
 */
public class MyPatientOphthalHomeActivity extends AppCompatActivity  implements DuoMenuView.OnMenuClickListener  {
    int PATIENT_ID, HOSPITAL_ID;
    String PATIENT_NAME, PATIENT_AGE, PATIENT_GENDER, PATIENT_MOBILE, PATIENT_EMAIL, PATIENT_CITY, PATIENT_ADDRESS, PATIENT_STATE, PATIENT_COUNTRY,
            PATIENT_HEIGHT, PATIENT_WEIGHT, PATIENT_HYPERTENSION, PATIENT_DIABETES, PATIENT_SMOKING, PATIENT_ALCOHOL, PATIENT_DRUG_ABUSE, PATIENT_OTHER_DETAILS,
            PATIENT_FAMILY_HISTORY, PATIENT_PREV_INTERVENTIONS, PATIENT_NEURO_ISSUES, PATIENT_KIDNEY_ISSUES;

    int USER_ID;
    String USER_NAME,USER_LOGIN_TYPE, GET_EPISODE_LIST_TEMP, DOC_HOSIPTAL_LIST;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;

    ArrayList<HospitalList> hospitalDocArraylist;

    List<EpisodesList> EPISODE_LIST = new ArrayList<>();
    List<SelectedTestLists> INVESTIGATION_LIST = new ArrayList<>();
    List<DiagnosisTestLists> IDC_LIST = new ArrayList<>();
    ArrayList<String> REPORT_PHOTOS = new ArrayList<String>();
    List<PrescriptionList> PRESCRIPTION_LIST = new ArrayList<>();
    List<EpisodesList> EPISODE_LIST_TEMP = new ArrayList<>();
    Gson gson = new Gson();

    private MenuAdapter mMenuAdapter;
    private ViewHolder mViewHolder;
    private ArrayList<String> mTitles = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mypatient_home);

        PATIENT_GENDER = "0";
        PATIENT_HYPERTENSION = "0";
        PATIENT_DIABETES = "0";
        PATIENT_SMOKING = "";
        PATIENT_ALCOHOL = "";
        EPISODE_LIST = new ArrayList<>();
        INVESTIGATION_LIST = new ArrayList<>();
        IDC_LIST = new ArrayList<>();
        PRESCRIPTION_LIST = new ArrayList<>();
        REPORT_PHOTOS = new ArrayList<String>();
        EPISODE_LIST_TEMP = new ArrayList<>();
        gson = new Gson();
        HOSPITAL_ID = 0;
        hospitalDocArraylist = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            setTitle(title);
            Log.d(Utils.TAG, " ************ MyPatientOphthalHomeActivity *********** ");

            PATIENT_ID = bundle.getInt("PATIENT_ID");
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

            Log.d(Utils.TAG, " PATIENT_ID: "+PATIENT_ID);
            Log.d(Utils.TAG, " PATIENT_NAME: "+PATIENT_NAME);
        }

        shareadPreferenceClass = new ShareadPreferenceClass(MyPatientOphthalHomeActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(MyPatientOphthalHomeActivity.this);

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            GET_EPISODE_LIST_TEMP = sharedPreferences.getString(HCConstants.PREF_MYPATIENT_EPISODE_LIST, "");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            GET_EPISODE_LIST_TEMP  = sharedPreferences.getString(HCConstants.PREF_MYPATIENT_EPISODE_LIST, "");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {

            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
            GET_EPISODE_LIST_TEMP  = sharedPreferences.getString(HCConstants.PREF_MYPATIENT_EPISODE_LIST, "");
            HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
            DOC_HOSIPTAL_LIST = sharedPreferences.getString(HCConstants.PREF_DOC_HOSPITALS,"");
        }
        Log.d(Utils.TAG, " USER_ID: "+USER_ID);
        Log.d(Utils.TAG, " USER_NAME: "+USER_NAME);
        Log.d(Utils.TAG, " LOGIN_TYPE: "+USER_LOGIN_TYPE);
        Log.d(Utils.TAG +" HOSPITAL_ID:", String.valueOf(HOSPITAL_ID));

        initializationViews();
    }

    private void initializationViews() {

        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));

        // Initialize the views
        mViewHolder = new ViewHolder();

        // Handle toolbar actions
        //   handleToolbar();

        // Handle menu actions
        handleMenu();

        // Handle drawer actions
        handleDrawer();

        // Show main fragment in container
        goToFragment(new AddVisitOphthalFragment(), false);
        mMenuAdapter.setViewSelected(0, true);
        setTitle(mTitles.get(0));

    }

    private void handleDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                mViewHolder.mDuoDrawerLayout,
                mViewHolder.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();

        mViewHolder.mDuoDrawerLayout.openDrawer();


    }

    private void handleMenu() {
        mMenuAdapter = new MenuAdapter(mTitles);

        mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
        mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
        mViewHolder.pat_name.setText(PATIENT_NAME);
        mViewHolder.pat_id.setText("ID: "+PATIENT_ID);
    }

    @Override
    public void onFooterClicked() {
        //  Toast.makeText(this, "onFooterClicked", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onHeaderClicked() {
        // Toast.makeText(this, "onHeaderClicked", Toast.LENGTH_SHORT).show();
    }

    private void goToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        Bundle bundle = new Bundle();
        bundle.putInt("PATIENT_ID", PATIENT_ID);
        bundle.putString("PATIENT_NAME", PATIENT_NAME);
        bundle.putString("PATIENT_AGE", PATIENT_AGE);
        bundle.putString("PATIENT_GENDER", PATIENT_GENDER);
        bundle.putString("PATIENT_MOBILE", PATIENT_MOBILE);
        bundle.putString("PATIENT_EMAIL", PATIENT_EMAIL);
        bundle.putString("PATIENT_CITY", PATIENT_CITY);
        bundle.putString("PATIENT_ADDRESS", PATIENT_ADDRESS);
        bundle.putString("PATIENT_STATE", PATIENT_STATE);
        bundle.putString("PATIENT_COUNTRY_NAME", PATIENT_COUNTRY);
        bundle.putString("PATIENT_WEIGHT", PATIENT_WEIGHT);
        bundle.putString("PATIENT_HYPERTENSION", PATIENT_HYPERTENSION);
        bundle.putString("PATIENT_DIABETES", PATIENT_DIABETES);
        bundle.putString("PATIENT_SMOKING", PATIENT_SMOKING);
        bundle.putString("PATIENT_ALCOHOL", PATIENT_ALCOHOL);
        bundle.putString("PATIENT_DRUG_ABUSE", PATIENT_DRUG_ABUSE);
        bundle.putString("PATIENT_OTHER_DETAILS", PATIENT_OTHER_DETAILS);
        bundle.putString("PATIENT_FAMILY_HISTORY", PATIENT_FAMILY_HISTORY);
        bundle.putString("PATIENT_PREV_INTERVENTIONS", PATIENT_PREV_INTERVENTIONS);
        bundle.putString("PATIENT_NEURO_ISSUES", PATIENT_NEURO_ISSUES);
        bundle.putString("PATIENT_KIDNEY_ISSUES", PATIENT_KIDNEY_ISSUES);
        bundle.putString("PATIENT_HEIGHT", PATIENT_HEIGHT);
        fragment.setArguments(bundle);

        transaction.replace(R.id.container, fragment).commit();
    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title
        setTitle(mTitles.get(position));

        // Set the right options selected
        mMenuAdapter.setViewSelected(position, true);

        // Navigate to the right fragment
        switch (position) {
            case 0:
                goToFragment(new AddVisitOphthalFragment(), false);
                break;
            case 1:
                goToFragment(new ViewOldVisitOphthalFragment(), false);
                break;
            case 2:
                goToFragment(new MedicalHistoryOphthal(), false);
                break;
            case 3:
                goToFragment(new TrendAnalysisOphthal(), false);
                break;
            case 4:
                goToFragment(new FundusImagesFragment(), false);
                break;
            case 5:
                goToFragment(new ViewReportsFragmentOphthal(), false);
                break;
            default:
                goToFragment(new ViewReportsFragmentOphthal(), false);
                break;
        }

        // Close the drawer
        mViewHolder.mDuoDrawerLayout.closeDrawer();
    }

    private class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        private Toolbar mToolbar;
        private CustomTextViewBold pat_name;
        private CustomTextView pat_id;

        ViewHolder() {
            mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            pat_name = (CustomTextViewBold) findViewById(R.id.duo_view_header_text_title);
            pat_id = (CustomTextView)findViewById(R.id.duo_view_header_text_sub_title);
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.item_refresh:
                //   Toast.makeText(getApplicationContext(),"Refresh !!!", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder dialog = new AlertDialog.Builder(MyPatientOphthalHomeActivity.this, R.style.CustomDialog);
                dialog.setTitle("Refresh EMR Contents or Sync with Web");
                dialog.setMessage("Are you sure you want to refresh all data? \nIt may take several minutes to complete all process. \n\nIf you still want to proceed further then click 'YES' else 'NO' to cancel. " );
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent bundle = new Intent(MyPatientOphthalHomeActivity.this, RefreshOphthalEMRActivity.class);
                        bundle.putExtra("title","Refresh EMR");
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
                        startActivity(bundle);
                    }
                })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                final AlertDialog alert = dialog.create();
                alert.show();
                return true;

            case R.id.item_switch:
                if(sharedPreferences != null) {
                    HOSPITAL_ID = sharedPreferences.getInt(HCConstants.PREF_DOC_HOSPITAL_ID,0);
                }
                showRadioButtonDialog();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showRadioButtonDialog() {
        Gson gson = new Gson();
        if (!DOC_HOSIPTAL_LIST.equals("")) {
            hospitalDocArraylist = gson.fromJson(DOC_HOSIPTAL_LIST, new TypeToken<List<HospitalList>>() {
            }.getType());
            if(hospitalDocArraylist.size() > 0 ) {
                openCustomHospitalSwitchDialog(hospitalDocArraylist);
            }
        }
    }

    private void openCustomHospitalSwitchDialog(ArrayList<HospitalList> hospitalDocArraylist) {
        final Dialog dialog = new Dialog(MyPatientOphthalHomeActivity.this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_hospital_switch);
        List<String> stringList=new ArrayList<>();  // here is list
        List<Integer> strinIDList=new ArrayList<>();
        for(int i=0;i<hospitalDocArraylist.size();i++) {
            stringList.add(hospitalDocArraylist.get(i).getHospitalName());
            strinIDList.add(hospitalDocArraylist.get(i).getHospitalId());
        }
        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);

        for(int i=0;i<stringList.size();i++){
            RadioButton rb=new RadioButton(MyPatientOphthalHomeActivity.this); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(stringList.get(i));
            rb.setPadding(10, 40, 10, 40);
            rb.setTag(strinIDList.get(i));
            rb.setTextSize(18);
            rg.addView(rb);

            if(HOSPITAL_ID == strinIDList.get(i)) {
                Log.d(Utils.TAG, " strinID: "+strinIDList.get(i));
                rb.setChecked(true);
            }
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                int mySelectedIndex = (int) radioButton.getTag();
                // Toast.makeText(getApplicationContext()," id: "+ mySelectedIndex +" name: "+radioButton.getText(),Toast.LENGTH_LONG).show();
                if(sharedPreferences != null) {
                    shareadPreferenceClass.clearDocHoapitalID();
                    shareadPreferenceClass.setDocHoapitalID(mySelectedIndex);
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
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
