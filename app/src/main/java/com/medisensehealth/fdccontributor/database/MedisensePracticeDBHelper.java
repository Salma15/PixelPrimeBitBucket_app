package com.medisensehealth.fdccontributor.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.medisensehealth.fdccontributor.DataModel.ChiefMedicalComplaint;
import com.medisensehealth.fdccontributor.DataModel.Country;
import com.medisensehealth.fdccontributor.DataModel.Diagnosis;
import com.medisensehealth.fdccontributor.DataModel.DoctorList;
import com.medisensehealth.fdccontributor.DataModel.DrugAbuse;
import com.medisensehealth.fdccontributor.DataModel.DrugAllery;
import com.medisensehealth.fdccontributor.DataModel.FamilyHistory;
import com.medisensehealth.fdccontributor.DataModel.FrequentChiefComplaints;
import com.medisensehealth.fdccontributor.DataModel.FrequentInvestigations;
import com.medisensehealth.fdccontributor.DataModel.FrequentPrescription;
import com.medisensehealth.fdccontributor.DataModel.HospitalList;
import com.medisensehealth.fdccontributor.DataModel.InvestigationGroupTests;
import com.medisensehealth.fdccontributor.DataModel.InvestigationTemplates;
import com.medisensehealth.fdccontributor.DataModel.Investigations;
import com.medisensehealth.fdccontributor.DataModel.Lids;
import com.medisensehealth.fdccontributor.DataModel.MyPatientsList;
import com.medisensehealth.fdccontributor.DataModel.NotifyMessages;
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
import com.medisensehealth.fdccontributor.DataModel.PatientEducation;
import com.medisensehealth.fdccontributor.DataModel.PatientsList;
import com.medisensehealth.fdccontributor.DataModel.SpecializationList;
import com.medisensehealth.fdccontributor.DataModel.Treatments;

/**
 * Created by lenovo on 02/06/2017.
 */

public class MedisensePracticeDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "FDCContributorDatabase.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_PATIENT_TABLE =
            "CREATE TABLE " + PatientsList.PATIENTS_TABLE_NAME + " (" +
                    PatientsList.PATIENT_AUTO_ID + "  integer primary key autoincrement, "+
                    PatientsList.PATIENT_ID + INTEGER_TYPE + COMMA_SEP +
                    PatientsList.PATIENT_NAME + TEXT_TYPE + COMMA_SEP +
                    PatientsList.PATIENT_AGE + TEXT_TYPE + COMMA_SEP +
                    PatientsList.PATIENT_LOCATION + TEXT_TYPE + COMMA_SEP +
                    PatientsList.PATIENT_STATUS + INTEGER_TYPE + COMMA_SEP +
                    PatientsList.PATIENT_DOCNAME + TEXT_TYPE + COMMA_SEP +
                    PatientsList.PATIENT_DOCTORID + INTEGER_TYPE + COMMA_SEP +
                    PatientsList.PATIENT_LOGINTYPE + TEXT_TYPE + COMMA_SEP +
                    PatientsList.PATIENT_REFERBY + TEXT_TYPE + COMMA_SEP +
                    PatientsList.PATIENT_LOGINUSERID + INTEGER_TYPE + COMMA_SEP +
                    PatientsList.PATIENT_STATUS_TIME + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_PATIENT_TABLE =
            "DROP TABLE IF EXISTS " + PatientsList.PATIENTS_TABLE_NAME;

    private static final String SQL_CREATE_DOCTORS_TABLE =
            "CREATE TABLE " + DoctorList.DOCTORS_TABLE_NAME + " (" +
                    DoctorList.DOCTORS_AUTO_ID + "  integer primary key autoincrement, "+
                    DoctorList.DOCTORS_ID + INTEGER_TYPE + COMMA_SEP +
                    DoctorList.DOCTORS_NAME + TEXT_TYPE + COMMA_SEP +
                    DoctorList.DOCTORS_AGE + INTEGER_TYPE + COMMA_SEP +
                    DoctorList.DOCTORS_EXPERIENCE + TEXT_TYPE + COMMA_SEP +
                    DoctorList.DOCTORS_PHOTO + TEXT_TYPE + COMMA_SEP +
                    DoctorList.DOCTORS_QUALIFICATION + TEXT_TYPE + COMMA_SEP +
                    DoctorList.DOCTORS_SPEC_ID + INTEGER_TYPE + COMMA_SEP +
                    DoctorList.DOCTORS_SPEC_NAME + TEXT_TYPE + COMMA_SEP +
                    DoctorList.DOCTORS_FAVOURITES + INTEGER_TYPE + COMMA_SEP +
                    DoctorList.DOCTORS_LOGIN_TYPE + TEXT_TYPE + COMMA_SEP +
                    DoctorList.DOCTORS_LOGIN_USERID + INTEGER_TYPE + COMMA_SEP +
                    DoctorList.DOCTORS_ADDRESS + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_DOCTORS_TABLE =
            "DROP TABLE IF EXISTS " + DoctorList.DOCTORS_TABLE_NAME;

    private static final String SQL_CREATE_SPECIALIZATION_TABLE =
            "CREATE TABLE " + SpecializationList.SPECIALIZATION_TABLE_NAME + " (" +
                    SpecializationList.SPECIALIZATION_AUTO_ID + "  integer primary key autoincrement, "+
                    SpecializationList.SPECIALIZATION_ID + INTEGER_TYPE + COMMA_SEP +
                    SpecializationList.SPECIALIZATION_NAME + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_SPECIALIZATION_TABLE =
            "DROP TABLE IF EXISTS " + SpecializationList.SPECIALIZATION_TABLE_NAME;

    private static final String SQL_CREATE_HOSPITAL_TABLE =
            "CREATE TABLE " + HospitalList.HOSPITAL_TABLE_NAME + " (" +
                    HospitalList.HOSPITAL_AUTO_ID + "  integer primary key autoincrement, "+
                    HospitalList.HOSPITAL_ID + INTEGER_TYPE + COMMA_SEP +
                    HospitalList.HOSPITAL_NAME + TEXT_TYPE + COMMA_SEP +
                    HospitalList.HOSPITAL_CITY + TEXT_TYPE + COMMA_SEP +
                    HospitalList.HOSPITAL_STATE + TEXT_TYPE + COMMA_SEP +
                    HospitalList.HOSPITAL_USER_ID + INTEGER_TYPE + COMMA_SEP +
                    HospitalList.HOSPITAL_LOGIN_TYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_HOSPITAL_TABLE =
            "DROP TABLE IF EXISTS " + HospitalList.HOSPITAL_TABLE_NAME;

    private static final String SQL_CREATE_NOTIFICATION_TABLE =
            "CREATE TABLE " + NotifyMessages.NOTIFICATION_TABLE_NAME + " (" +
                    NotifyMessages.NOTIFICATION_AUTO_ID + "  integer primary key autoincrement, "+
                    NotifyMessages.NOTIFICATION_USER_ID + INTEGER_TYPE + COMMA_SEP +
                    NotifyMessages.NOTIFICATION_LOGIN_TYPE + TEXT_TYPE + COMMA_SEP +
                    NotifyMessages.NOTIFICATION_POST_ID + TEXT_TYPE + COMMA_SEP +
                    NotifyMessages.NOTIFICATION_POST_IMAGE + TEXT_TYPE + COMMA_SEP +
                    NotifyMessages.NOTIFICATION_TITLE + TEXT_TYPE + COMMA_SEP +
                    NotifyMessages.NOTIFICATION_AUTHOR + TEXT_TYPE + COMMA_SEP +
                    NotifyMessages.NOTIFICATION_POST_DATE + TEXT_TYPE + COMMA_SEP +
                    NotifyMessages.NOTIFICATION_POST_MESSAGE + TEXT_TYPE + COMMA_SEP +
                    NotifyMessages.NOTIFICATION_POST_TYPE + TEXT_TYPE + COMMA_SEP +
                    NotifyMessages.NOTFICATION_ENTRY_TYPE + TEXT_TYPE + COMMA_SEP +
                    NotifyMessages.NOTIFICATION_PATIENT_ID + INTEGER_TYPE + COMMA_SEP +
                    NotifyMessages.NOTIFICATION_DOC_ID + INTEGER_TYPE + COMMA_SEP +
                    NotifyMessages.NOTIFICATION_POST_KEY + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_NOTIFICATION_TABLE =
            "DROP TABLE IF EXISTS " + NotifyMessages.NOTIFICATION_TABLE_NAME;

    private static final String SQL_CREATE_MYPATIENT_TABLE =
            "CREATE TABLE " + MyPatientsList.MYPATIENT_TABLE_NAME + " (" +
                    MyPatientsList.MYPATIENT_AUTO_ID + "  integer primary key autoincrement, "+
                    MyPatientsList.MYPATIENT_ID + INTEGER_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_NAME + TEXT_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_AGE + TEXT_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_EMAIL + TEXT_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_GEN + INTEGER_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_MERITAL_STATUS + TEXT_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_QUALIFICATION + TEXT_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_WEIGHT + INTEGER_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_HYPERCONDITION + INTEGER_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_DIABETES + INTEGER_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_BLOOD + TEXT_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_CONTACT_PERSON + TEXT_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_PROFESSION + TEXT_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_MOBILE + TEXT_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_LOCATION + TEXT_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_STATE + TEXT_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_COUNTRY + TEXT_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_PARTNER_ID + INTEGER_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_CREATED_DATE + TEXT_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_USER_ID + INTEGER_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_LOGIN_TYPE + TEXT_TYPE + COMMA_SEP +
                    MyPatientsList.MYPATIENT_BSYNC + INTEGER_TYPE +
                    " )";

    private static final String SQL_DELETE_MYPATIENT_TABLE =
            "DROP TABLE IF EXISTS " + MyPatientsList.MYPATIENT_TABLE_NAME;

    private static final String SQL_CREATE_COUNTRY_TABLE =
            "CREATE TABLE " + Country.COUNTRY_TABLE_NAME + " (" +
                    Country.COUNTRY_AUTO_ID + "  integer primary key autoincrement, "+
                    Country.COUNTRY_ID + INTEGER_TYPE + COMMA_SEP +
                    Country.COUNTRY_NAME + TEXT_TYPE + COMMA_SEP +
                    Country.COUNTRY_CREATED_DATE + TEXT_TYPE + COMMA_SEP +
                    Country.COUNTRY_USER_ID + INTEGER_TYPE + COMMA_SEP +
                    Country.COUNTRY_LOGIN_TYPE + TEXT_TYPE + COMMA_SEP +
                    Country.COUNTRY_BSYNC + INTEGER_TYPE +
                    " )";

    private static final String SQL_DELETE_COUNTRY_TABLE =
            "DROP TABLE IF EXISTS " + Country.COUNTRY_TABLE_NAME;


    private static final String SQL_CREATE_STATE_TABLE =
            "CREATE TABLE " + Country.STATE_TABLE_NAME + " (" +
                    Country.STATE_AUTO_ID + "  integer primary key autoincrement, "+
                    Country.STATE_ID + INTEGER_TYPE + COMMA_SEP +
                    Country.STATE_COUNTRY_ID + INTEGER_TYPE + COMMA_SEP +
                    Country.STATE_NAME + TEXT_TYPE + COMMA_SEP +
                    Country.STATE_CREATED_DATE + TEXT_TYPE + COMMA_SEP +
                    Country.STATE_USER_ID + INTEGER_TYPE + COMMA_SEP +
                    Country.STATE_LOGIN_TYPE + TEXT_TYPE + COMMA_SEP +
                    Country.STATE_BSYNC + INTEGER_TYPE +
                    " )";

    private static final String SQL_DELETE_STATE_TABLE =
            "DROP TABLE IF EXISTS " + Country.STATE_TABLE_NAME;

    private static final String SQL_CREATE_CHIEF_MEDCOMPLAINT_TABLE =
            "CREATE TABLE " + ChiefMedicalComplaint.CHIEF_MEDICALCOMPLAINT_TABLE_NAME + " (" +
                    ChiefMedicalComplaint.CHIEF_MEDICALCOMPLAINT_AUTO_ID + "  integer primary key autoincrement, "+
                    ChiefMedicalComplaint.CHIEF_COMPLAINT_ID + INTEGER_TYPE + COMMA_SEP +
                    ChiefMedicalComplaint.CHIEF_SYMPTOMS_NAME + TEXT_TYPE + COMMA_SEP +
                    ChiefMedicalComplaint.CHIEF_DOCID + INTEGER_TYPE + COMMA_SEP +
                    ChiefMedicalComplaint.CHIEF_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    ChiefMedicalComplaint.CHIEF_USERID + INTEGER_TYPE + COMMA_SEP +
                    ChiefMedicalComplaint.CHIEF_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_CHIEF_MEDCOMPLAINT_TABLE =
            "DROP TABLE IF EXISTS " + ChiefMedicalComplaint.CHIEF_MEDICALCOMPLAINT_TABLE_NAME;

    private static final String SQL_CREATE_FREQUENT_MEDCOMPLAINT_TABLE =
            "CREATE TABLE " + FrequentChiefComplaints.FREQUENT_COMPLAINT_TABLE_NAME + " (" +
                    FrequentChiefComplaints.FREQUENT_COMPLAINT_AUTO_ID + "  integer primary key autoincrement, "+
                    FrequentChiefComplaints.FREQUENT_COMPLAINT_ID + INTEGER_TYPE + COMMA_SEP +
                    FrequentChiefComplaints.FREQUENT_SYMPTOMS_ID + INTEGER_TYPE + COMMA_SEP +
                    FrequentChiefComplaints.FREQUENT_SYMPTOMS_NAME + TEXT_TYPE + COMMA_SEP +
                    FrequentChiefComplaints.FREQUENT_DOCID + INTEGER_TYPE + COMMA_SEP +
                    FrequentChiefComplaints.FREQUENT_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    FrequentChiefComplaints.FREQUENT_COUNT + INTEGER_TYPE + COMMA_SEP +
                    FrequentChiefComplaints.FREQUENT_USERID + INTEGER_TYPE + COMMA_SEP +
                    FrequentChiefComplaints.FREQUENT_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_FREQUENT_MEDCOMPLAINT_TABLE =
            "DROP TABLE IF EXISTS " + FrequentChiefComplaints.FREQUENT_COMPLAINT_TABLE_NAME;

    private static final String SQL_CREATE_INVESTIGATION_TABLE =
            "CREATE TABLE " + Investigations.INVESTIGATION_TABLE_NAME + " (" +
                    Investigations.INVESTIGATION_AUTO_ID + "  integer primary key autoincrement, "+
                    Investigations.INVESTIGATION_ID + INTEGER_TYPE + COMMA_SEP +
                    Investigations.INVESTIGATION_TEST_ID + TEXT_TYPE + COMMA_SEP +
                    Investigations.INVESTIGATION_TEST_NAME + TEXT_TYPE + COMMA_SEP +
                    Investigations.INVESTIGATION_TEST_DEPARTMENT + INTEGER_TYPE + COMMA_SEP +
                    Investigations.INVESTIGATION_GROUP_TEST + TEXT_TYPE + COMMA_SEP +
                    Investigations.INVESTIGATION_MFRANGE + TEXT_TYPE + COMMA_SEP +
                    Investigations.INVESTIGATION_REPORTABLE + TEXT_TYPE + COMMA_SEP +
                    Investigations.INVESTIGATION_NORMAL_RANGE + TEXT_TYPE + COMMA_SEP +
                    Investigations.INVESTIGATION_TEST_CHARGES + TEXT_TYPE + COMMA_SEP +
                    Investigations.INVESTIGATION_MIN_RANGE + TEXT_TYPE + COMMA_SEP +
                    Investigations.INVESTIGATION_MAX_RANGE + TEXT_TYPE + COMMA_SEP +
                    Investigations.INVESTIGATION_TEST_UNITS + TEXT_TYPE + COMMA_SEP +
                    Investigations.INVESTIGATION_CRIT_LOW_RANGE + TEXT_TYPE + COMMA_SEP +
                    Investigations.INVESTIGATION_CRIT_HIGH_RANGE + TEXT_TYPE + COMMA_SEP +
                    Investigations.INVESTIGATION_NORMAL_MIN_RANGE + TEXT_TYPE + COMMA_SEP +
                    Investigations.INVESTIGATION_NORMAL_MAX_RANGE + TEXT_TYPE + COMMA_SEP +
                    Investigations.INVESTIGATION_DEPT_NAME + TEXT_TYPE + COMMA_SEP +
                    Investigations.INVESTIGATION_DEPT_TYPE + TEXT_TYPE + COMMA_SEP +
                    Investigations.INVESTIGATION_USERID + INTEGER_TYPE + COMMA_SEP +
                    Investigations.INVESTIGATION_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_INVESTIGATION_TABLE =
            "DROP TABLE IF EXISTS " + Investigations.INVESTIGATION_TABLE_NAME;

    private static final String SQL_CREATE_FREQUENT_INVESTIGATION_TABLE =
            "CREATE TABLE " + FrequentInvestigations.FREQUENT_INVESTIGATION_TABLE_NAME + " (" +
                    FrequentInvestigations.FREQUENT_INVESTIGATION_AUTO_ID + "  integer primary key autoincrement, "+
                    FrequentInvestigations.FREQUENT_INVESTIGATION_ID + INTEGER_TYPE + COMMA_SEP +
                    FrequentInvestigations.FREQUENT_TEST_ID + INTEGER_TYPE + COMMA_SEP +
                    FrequentInvestigations.FREQUENT_TEST_NAME + TEXT_TYPE + COMMA_SEP +
                    FrequentInvestigations.FREQUENT_TEST_DEPARTMENT + INTEGER_TYPE + COMMA_SEP +
                    FrequentInvestigations.FREQUENT_DOCID + INTEGER_TYPE + COMMA_SEP +
                    FrequentInvestigations.FREQUENT_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    FrequentInvestigations.FREQUENT_COUNT + INTEGER_TYPE + COMMA_SEP +
                    FrequentInvestigations.FREQUENT_USERID + INTEGER_TYPE + COMMA_SEP +
                    FrequentInvestigations.FREQUENT_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_FREQUENT_INVESTIGATION_TABLE =
            "DROP TABLE IF EXISTS " + FrequentInvestigations.FREQUENT_INVESTIGATION_TABLE_NAME;

    private static final String SQL_CREATE_INVESTIGATION_GROUPTEST_TABLE =
            "CREATE TABLE " + InvestigationGroupTests.INVEST_GROUP_TABLE_NAME + " (" +
                    InvestigationGroupTests.INVESTI_GROUP_AUTO_ID + "  integer primary key autoincrement, "+
                    InvestigationGroupTests.INVEST_GROUP_ID + INTEGER_TYPE + COMMA_SEP +
                    InvestigationGroupTests.INVEST_GROUP_TEST_ID + TEXT_TYPE + COMMA_SEP +
                    InvestigationGroupTests.INVEST_GROUP_SUBTEST_ID + TEXT_TYPE + COMMA_SEP +
                    InvestigationGroupTests.INVEST_GROUP_ORDER_NUM + TEXT_TYPE + COMMA_SEP +
                    InvestigationGroupTests.INVEST_GROUP_USERID + INTEGER_TYPE + COMMA_SEP +
                    InvestigationGroupTests.INVEST_GROUP_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_INVESTIGATION_GROUPTEST_TABLE =
            "DROP TABLE IF EXISTS " + InvestigationGroupTests.INVEST_GROUP_TABLE_NAME;



    private static final String SQL_CREATE_FREQUENT_DIAGNOSIS_TABLE =
            "CREATE TABLE " + Diagnosis.DIAGNOSIS_TABLE_NAME + " (" +
                    Diagnosis.DIAGNOSIS_AUTO_ID + "  integer primary key autoincrement, "+
                    Diagnosis.DIAGNOSIS_FREQ_ID + INTEGER_TYPE + COMMA_SEP +
                    Diagnosis.DIAGNOSIS_ICD_ID + INTEGER_TYPE + COMMA_SEP +
                    Diagnosis.DIAGNOSIS_ICD_NAME + TEXT_TYPE + COMMA_SEP +
                    Diagnosis.DIAGNOSIS_DOCID + INTEGER_TYPE + COMMA_SEP +
                    Diagnosis.DIAGNOSIS_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    Diagnosis.DIAGNOSIS_FREQUENT_COUNT + INTEGER_TYPE + COMMA_SEP +
                    Diagnosis.DIAGNOSIS_USERID + INTEGER_TYPE + COMMA_SEP +
                    Diagnosis.DIAGNOSIS_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_FREQUENT_DIAGNOSIS_TABLE =
            "DROP TABLE IF EXISTS " + Diagnosis.DIAGNOSIS_TABLE_NAME;

    private static final String SQL_CREATE_FREQUENT_TREATMENT_TABLE =
            "CREATE TABLE " + Treatments.FREQ_TREATMENT_TABLE_NAME + " (" +
                    Treatments.FREQ_TREATMENT_AUTO_ID + "  integer primary key autoincrement, "+
                    Treatments.FREQ_TREATMENT_ID + INTEGER_TYPE + COMMA_SEP +
                    Treatments.FREQ_TREATMENT_NAME + TEXT_TYPE + COMMA_SEP +
                    Treatments.FREQ_TREATMENT_DOCID + INTEGER_TYPE + COMMA_SEP +
                    Treatments.FREQ_TREATMENT_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    Treatments.FREQ_TREATMENT_COUNT + INTEGER_TYPE + COMMA_SEP +
                    Treatments.FREQ_TREATMENT_USERID + INTEGER_TYPE + COMMA_SEP +
                    Treatments.FREQ_TREATMENT_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_FREQUENT_TREATMENT_TABLE =
            "DROP TABLE IF EXISTS " + Treatments.FREQ_TREATMENT_TABLE_NAME;

    private static final String SQL_CREATE_TREATMENT_TABLE =
            "CREATE TABLE " + Treatments.TREATMENT_TABLE_NAME + " (" +
                    Treatments.TREATMENT_AUTO_ID + "  integer primary key autoincrement, "+
                    Treatments.TREATMENT_ID + INTEGER_TYPE + COMMA_SEP +
                    Treatments.TREATMENT_NAME + TEXT_TYPE + COMMA_SEP +
                    Treatments.TREATMENT_DOCID + INTEGER_TYPE + COMMA_SEP +
                    Treatments.TREATMENT_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    Treatments.TREATMENT_COUNT + INTEGER_TYPE + COMMA_SEP +
                    Treatments.TREATMENT_USERID + INTEGER_TYPE + COMMA_SEP +
                    Treatments.TREATMENT_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_TREATMENT_TABLE =
            "DROP TABLE IF EXISTS " + Treatments.TREATMENT_TABLE_NAME;

    private static final String SQL_CREATE_FREQ_PRESCRIPTION_TABLE =
            "CREATE TABLE " + FrequentPrescription.FREQ_PRESCRIPTION_TABLE_NAME + " (" +
                    FrequentPrescription.FREQ_PRESCRIPTION_AUTO_ID + "  integer primary key autoincrement, "+
                    FrequentPrescription.FREQ_PRESCRIPTION_FREQ_ID + INTEGER_TYPE + COMMA_SEP +
                    FrequentPrescription.FREQ_PRESCRIPTION_PRODUCT_ID + INTEGER_TYPE + COMMA_SEP +
                    FrequentPrescription.FREQ_PRESCRIPTION_TRADE_NAME + TEXT_TYPE + COMMA_SEP +
                    FrequentPrescription.FREQ_PRESCRIPTION_GENERIC_ID + INTEGER_TYPE + COMMA_SEP +
                    FrequentPrescription.FREQ_PRESCRIPTION_GENERIC_NAME + TEXT_TYPE + COMMA_SEP +
                    FrequentPrescription.FREQ_PRESCRIPTION_DOASGE + TEXT_TYPE + COMMA_SEP +
                    FrequentPrescription.FREQ_PRESCRIPTION_TIMING + TEXT_TYPE + COMMA_SEP +
                    FrequentPrescription.FREQ_PRESCRIPTION_DURATION + TEXT_TYPE + COMMA_SEP +
                    FrequentPrescription.FREQ_PRESCRIPTION_DOCID + INTEGER_TYPE + COMMA_SEP +
                    FrequentPrescription.FREQ_PRESCRIPTION_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    FrequentPrescription.FREQ_PRESCRIPTION_FREQUENT_COUNT + INTEGER_TYPE + COMMA_SEP +
                    FrequentPrescription.FREQ_PRESCRIPTION_USERID + INTEGER_TYPE + COMMA_SEP +
                    FrequentPrescription.FREQ_PRESCRIPTION_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_FREQ_PRESCRIPTION_TABLE =
            "DROP TABLE IF EXISTS " + FrequentPrescription.FREQ_PRESCRIPTION_TABLE_NAME;

    private static final String SQL_CREATE_REPEAT_PRESCRIPTION_TABLE =
            "CREATE TABLE " + FrequentPrescription.REPEAT_PRESCRIPTION_TABLE_NAME + " (" +
                    FrequentPrescription.REPEAT_PRESCRIPTION_AUTO_ID + "  integer primary key autoincrement, "+
                    FrequentPrescription.REPEAT_PRESCRIPTION_FREQ_ID + INTEGER_TYPE + COMMA_SEP +
                    FrequentPrescription.REPEAT_PRESCRIPTION_PRODUCT_ID + INTEGER_TYPE + COMMA_SEP +
                    FrequentPrescription.REPEAT_PRESCRIPTION_TRADE_NAME + TEXT_TYPE + COMMA_SEP +
                    FrequentPrescription.REPEAT_PRESCRIPTION_GENERIC_ID + INTEGER_TYPE + COMMA_SEP +
                    FrequentPrescription.REPEAT_PRESCRIPTION_GENERIC_NAME + TEXT_TYPE + COMMA_SEP +
                    FrequentPrescription.REPEAT_PRESCRIPTION_DOASGE + TEXT_TYPE + COMMA_SEP +
                    FrequentPrescription.REPEAT_PRESCRIPTION_TIMING + TEXT_TYPE + COMMA_SEP +
                    FrequentPrescription.REPEAT_PRESCRIPTION_DURATION + TEXT_TYPE + COMMA_SEP +
                    FrequentPrescription.REPEAT_PRESCRIPTION_DOCID + INTEGER_TYPE + COMMA_SEP +
                    FrequentPrescription.REPEAT_PRESCRIPTION_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    FrequentPrescription.REPEAT_PRESCRIPTION_FREQUENT_COUNT + INTEGER_TYPE + COMMA_SEP +
                    FrequentPrescription.REPEAT_PRESCRIPTION_USERID + INTEGER_TYPE + COMMA_SEP +
                    FrequentPrescription.REPEAT_PRESCRIPTION_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_REPEAT_PRESCRIPTION_TABLE =
            "DROP TABLE IF EXISTS " + FrequentPrescription.REPEAT_PRESCRIPTION_TABLE_NAME;

    private static final String SQL_CREATE_DRUG_ALLERGY_TABLE =
            "CREATE TABLE " + DrugAllery.DRUG_ALLERY_TABLE_NAME + " (" +
                    DrugAllery.DRUG_ALLERY_AUTO_ID + "  integer primary key autoincrement, "+
                    DrugAllery.DRUG_ALLERY_ID + INTEGER_TYPE + COMMA_SEP +
                    DrugAllery.DRUG_ALLERY_GENERIC_ID + INTEGER_TYPE + COMMA_SEP +
                    DrugAllery.DRUG_ALLERY_GENERIC_NAME + TEXT_TYPE + COMMA_SEP +
                    DrugAllery.DRUG_ALLERY_PATIENTID + INTEGER_TYPE + COMMA_SEP +
                    DrugAllery.DRUG_ALLERY_DOCID + INTEGER_TYPE + COMMA_SEP +
                    DrugAllery.DRUG_ALLERY_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    DrugAllery.DRUG_ALLERY_USERID + INTEGER_TYPE + COMMA_SEP +
                    DrugAllery.DRUG_ALLERY_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_DRUG_ALLERY_TABLE =
            "DROP TABLE IF EXISTS " + DrugAllery.DRUG_ALLERY_TABLE_NAME;

    private static final String SQL_CREATE_FREQ_DRUG_ABUSE_TABLE =
            "CREATE TABLE " + DrugAbuse.FREQ_DRUG_ABUSE_TABLE_NAME + " (" +
                    DrugAbuse.FREQ_DRUG_ABUSE_AUTO_ID + "  integer primary key autoincrement, "+
                    DrugAbuse.FREQ_DRUG_ABUSE_FREQID + INTEGER_TYPE + COMMA_SEP +
                    DrugAbuse.FREQ_DRUG_ABUSE_ID + INTEGER_TYPE + COMMA_SEP +
                    DrugAbuse.FREQ_DRUG_ABUSE_NAME + TEXT_TYPE + COMMA_SEP +
                    DrugAbuse.FREQ_DRUG_ABUSE_DOCID + INTEGER_TYPE + COMMA_SEP +
                    DrugAbuse.FREQ_DRUG_ABUSE_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    DrugAbuse.FREQ_DRUG_ABUSE_FREQ_COUNT + INTEGER_TYPE + COMMA_SEP +
                    DrugAbuse.FREQ_DRUG_ABUSE_USERID + INTEGER_TYPE + COMMA_SEP +
                    DrugAbuse.FREQ_DRUG_ABUSE_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_FREQ_DRUG_ABUSE_TABLE =
            "DROP TABLE IF EXISTS " + DrugAbuse.FREQ_DRUG_ABUSE_TABLE_NAME;

    private static final String SQL_CREATE_DRUG_ABUSE_TABLE =
            "CREATE TABLE " + DrugAbuse.DRUG_ABUSE_TABLE_NAME + " (" +
                    DrugAbuse.DRUG_ABUSE_AUTO_ID + "  integer primary key autoincrement, "+
                    DrugAbuse.DRUG_ABUSE_ID + INTEGER_TYPE + COMMA_SEP +
                    DrugAbuse.DRUG_ABUSE_NAME + TEXT_TYPE + COMMA_SEP +
                    DrugAbuse.DRUG_ABUSE_DOCID + INTEGER_TYPE + COMMA_SEP +
                    DrugAbuse.DRUG_ABUSE_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    DrugAbuse.DRUG_ABUSE_USERID + INTEGER_TYPE + COMMA_SEP +
                    DrugAbuse.DRUG_ABUSE_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_DRUG_ABUSE_TABLE =
            "DROP TABLE IF EXISTS " + DrugAbuse.DRUG_ABUSE_TABLE_NAME;

    private static final String SQL_CREATE_FREQ_FAMILY_HISTORY_TABLE =
            "CREATE TABLE " + FamilyHistory.FREQ_FAMILY_HISTORY_TABLE_NAME + " (" +
                    FamilyHistory.FREQ_FAMILY_HISTORY_AUTO_ID + "  integer primary key autoincrement, "+
                    FamilyHistory.FREQ_FAMILY_HISTORY_FREQID + INTEGER_TYPE + COMMA_SEP +
                    FamilyHistory.FREQ_FAMILY_HISTORY_ID + INTEGER_TYPE + COMMA_SEP +
                    FamilyHistory.FREQ_FAMILY_HISTORY_NAME + TEXT_TYPE + COMMA_SEP +
                    FamilyHistory.FREQ_FAMILY_HISTORY_DOCID + INTEGER_TYPE + COMMA_SEP +
                    FamilyHistory.FREQ_FAMILY_HISTORY_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    FamilyHistory.FREQ_FAMILY_HISTORY_FREQ_COUNT + INTEGER_TYPE + COMMA_SEP +
                    FamilyHistory.FREQ_FAMILY_HISTORY_USERID + INTEGER_TYPE + COMMA_SEP +
                    FamilyHistory.FREQ_FAMILY_HISTORY_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_FREQ_FAMILY_HISTORY_TABLE =
            "DROP TABLE IF EXISTS " + FamilyHistory.FREQ_FAMILY_HISTORY_TABLE_NAME;

    private static final String SQL_CREATE_FAMILY_HISTORY_TABLE =
            "CREATE TABLE " + FamilyHistory.FAMILY_HISTORY_TABLE_NAME + " (" +
                    FamilyHistory.FAMILY_HISTORY_AUTO_ID + "  integer primary key autoincrement, "+
                    FamilyHistory.FAMILY_HISTORY_ID + INTEGER_TYPE + COMMA_SEP +
                    FamilyHistory.FAMILY_HISTORY_NAME + TEXT_TYPE + COMMA_SEP +
                    FamilyHistory.FAMILY_HISTORY_DOCID + INTEGER_TYPE + COMMA_SEP +
                    FamilyHistory.FAMILY_HISTORY_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    FamilyHistory.FAMILY_HISTORY_USERID + INTEGER_TYPE + COMMA_SEP +
                    FamilyHistory.FAMILY_HISTORY_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_FAMILY_HISTORY_TABLE =
            "DROP TABLE IF EXISTS " + FamilyHistory.FAMILY_HISTORY_TABLE_NAME;

    private static final String SQL_CREATE_LIDS_TABLE =
            "CREATE TABLE " + Lids.LIDS_TABLE_NAME + " (" +
                    Lids.LIDS_AUTO_ID + "  integer primary key autoincrement, "+
                    Lids.LIDS_ID + INTEGER_TYPE + COMMA_SEP +
                    Lids.LIDS_NAME + TEXT_TYPE + COMMA_SEP +
                    Lids.LIDS_DOCID + INTEGER_TYPE + COMMA_SEP +
                    Lids.LIDS_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    Lids.LIDS_LEFT_EYE + TEXT_TYPE + COMMA_SEP +
                    Lids.LIDS_RIGHT_EYE + TEXT_TYPE + COMMA_SEP +
                    Lids.LIDS_USERID + INTEGER_TYPE + COMMA_SEP +
                    Lids.LIDS_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_LIDS_TABLE =
            "DROP TABLE IF EXISTS " + Lids.LIDS_TABLE_NAME;

    private static final String SQL_CREATE_CONJUCTIVA_TABLE =
            "CREATE TABLE " + OphthalConjuctiva.CONJUCTIVA_TABLE_NAME + " (" +
                    OphthalConjuctiva.CONJUCTIVA_AUTO_ID + "  integer primary key autoincrement, "+
                    OphthalConjuctiva.CONJUCTIVA_ID + INTEGER_TYPE + COMMA_SEP +
                    OphthalConjuctiva.CONJUCTIVA_NAME + TEXT_TYPE + COMMA_SEP +
                    OphthalConjuctiva.CONJUCTIVA_DOCID + INTEGER_TYPE + COMMA_SEP +
                    OphthalConjuctiva.CONJUCTIVA_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    OphthalConjuctiva.CONJUCTIVA_LEFT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalConjuctiva.CONJUCTIVA_RIGHT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalConjuctiva.CONJUCTIVA_USERID + INTEGER_TYPE + COMMA_SEP +
                    OphthalConjuctiva.CONJUCTIVA_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_CONJUCTIVA_TABLE =
            "DROP TABLE IF EXISTS " + OphthalConjuctiva.CONJUCTIVA_TABLE_NAME;

    private static final String SQL_CREATE_SCLERA_TABLE =
            "CREATE TABLE " + OphthalSclera.SCLERA_TABLE_NAME + " (" +
                    OphthalSclera.SCLERA_AUTO_ID + "  integer primary key autoincrement, "+
                    OphthalSclera.SCLERA_ID + INTEGER_TYPE + COMMA_SEP +
                    OphthalSclera.SCLERA_NAME + TEXT_TYPE + COMMA_SEP +
                    OphthalSclera.SCLERA_DOCID + INTEGER_TYPE + COMMA_SEP +
                    OphthalSclera.SCLERA_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    OphthalSclera.SCLERA_LEFT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalSclera.SCLERA_RIGHT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalSclera.SCLERA_USERID + INTEGER_TYPE + COMMA_SEP +
                    OphthalSclera.SCLERA_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_SCLERA_TABLE =
            "DROP TABLE IF EXISTS " + OphthalSclera.SCLERA_TABLE_NAME;

    private static final String SQL_CREATE_CORNEA_ANTERIOR_TABLE =
            "CREATE TABLE " + OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_TABLE_NAME + " (" +
                    OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_AUTO_ID + "  integer primary key autoincrement, "+
                    OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_ID + INTEGER_TYPE + COMMA_SEP +
                    OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_NAME + TEXT_TYPE + COMMA_SEP +
                    OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_DOCID + INTEGER_TYPE + COMMA_SEP +
                    OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_LEFT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_RIGHT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_USERID + INTEGER_TYPE + COMMA_SEP +
                    OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_CORNEA_ANTERIOR_TABLE =
            "DROP TABLE IF EXISTS " + OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_TABLE_NAME;

    private static final String SQL_CREATE_CORNEA_POSTERIOR_TABLE =
            "CREATE TABLE " + OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_TABLE_NAME + " (" +
                    OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_AUTO_ID + "  integer primary key autoincrement, "+
                    OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_ID + INTEGER_TYPE + COMMA_SEP +
                    OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_NAME + TEXT_TYPE + COMMA_SEP +
                    OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_DOCID + INTEGER_TYPE + COMMA_SEP +
                    OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_LEFT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_RIGHT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_USERID + INTEGER_TYPE + COMMA_SEP +
                    OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_CORNEA_POSTERIOR_TABLE =
            "DROP TABLE IF EXISTS " + OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_TABLE_NAME;

    private static final String SQL_CREATE_ANTERIOR_CHAMBER_TABLE =
            "CREATE TABLE " + OphthalAnteriorChamber.ANTERIOR_CHAMBER_TABLE_NAME + " (" +
                    OphthalAnteriorChamber.ANTERIOR_CHAMBER_AUTO_ID + "  integer primary key autoincrement, "+
                    OphthalAnteriorChamber.ANTERIOR_CHAMBER_ID + INTEGER_TYPE + COMMA_SEP +
                    OphthalAnteriorChamber.ANTERIOR_CHAMBER_NAME + TEXT_TYPE + COMMA_SEP +
                    OphthalAnteriorChamber.ANTERIOR_CHAMBER_DOCID + INTEGER_TYPE + COMMA_SEP +
                    OphthalAnteriorChamber.ANTERIOR_CHAMBER_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    OphthalAnteriorChamber.ANTERIOR_CHAMBER_LEFT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalAnteriorChamber.ANTERIOR_CHAMBER_RIGHT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalAnteriorChamber.ANTERIOR_CHAMBER_USERID + INTEGER_TYPE + COMMA_SEP +
                    OphthalAnteriorChamber.ANTERIOR_CHAMBER_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ANTERIOR_CHAMBER_TABLE =
            "DROP TABLE IF EXISTS " + OphthalAnteriorChamber.ANTERIOR_CHAMBER_TABLE_NAME;

    private static final String SQL_CREATE_IRIS_TABLE =
            "CREATE TABLE " + OphthalIris.IRIS_TABLE_NAME + " (" +
                    OphthalIris.IRIS_AUTO_ID + "  integer primary key autoincrement, "+
                    OphthalIris.IRIS_ID + INTEGER_TYPE + COMMA_SEP +
                    OphthalIris.IRIS_NAME + TEXT_TYPE + COMMA_SEP +
                    OphthalIris.IRIS_DOCID + INTEGER_TYPE + COMMA_SEP +
                    OphthalIris.IRIS_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    OphthalIris.IRIS_LEFT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalIris.IRIS_RIGHT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalIris.IRIS_USERID + INTEGER_TYPE + COMMA_SEP +
                    OphthalIris.IRIS_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_IRIS_TABLE =
            "DROP TABLE IF EXISTS " + OphthalIris.IRIS_TABLE_NAME;

    private static final String SQL_CREATE_PUPIL_TABLE =
            "CREATE TABLE " + OphthalPupil.PUPIL_TABLE_NAME + " (" +
                    OphthalPupil.PUPIL_AUTO_ID + "  integer primary key autoincrement, "+
                    OphthalPupil.PUPIL_ID + INTEGER_TYPE + COMMA_SEP +
                    OphthalPupil.PUPIL_NAME + TEXT_TYPE + COMMA_SEP +
                    OphthalPupil.PUPIL_DOCID + INTEGER_TYPE + COMMA_SEP +
                    OphthalPupil.PUPIL_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    OphthalPupil.PUPIL_LEFT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalPupil.PUPIL_RIGHT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalPupil.PUPIL_USERID + INTEGER_TYPE + COMMA_SEP +
                    OphthalPupil.PUPIL_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_PUPIL_TABLE =
            "DROP TABLE IF EXISTS " + OphthalPupil.PUPIL_TABLE_NAME;

    private static final String SQL_CREATE_ANGLE_TABLE =
            "CREATE TABLE " + OphthalAngleAnteriorChamber.ANGLE_TABLE_NAME + " (" +
                    OphthalAngleAnteriorChamber.ANGLE_AUTO_ID + "  integer primary key autoincrement, "+
                    OphthalAngleAnteriorChamber.ANGLE_ID + INTEGER_TYPE + COMMA_SEP +
                    OphthalAngleAnteriorChamber.ANGLE_NAME + TEXT_TYPE + COMMA_SEP +
                    OphthalAngleAnteriorChamber.ANGLE_DOCID + INTEGER_TYPE + COMMA_SEP +
                    OphthalAngleAnteriorChamber.ANGLE_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    OphthalAngleAnteriorChamber.ANGLE_LEFT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalAngleAnteriorChamber.ANGLE_RIGHT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalAngleAnteriorChamber.ANGLE_USERID + INTEGER_TYPE + COMMA_SEP +
                    OphthalAngleAnteriorChamber.ANGLE_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ANGLE_TABLE =
            "DROP TABLE IF EXISTS " + OphthalAngleAnteriorChamber.ANGLE_TABLE_NAME;

    private static final String SQL_CREATE_LENS_TABLE =
            "CREATE TABLE " + OphthalLens.LENS_TABLE_NAME + " (" +
                    OphthalLens.LENS_AUTO_ID + "  integer primary key autoincrement, "+
                    OphthalLens.LENS_ID + INTEGER_TYPE + COMMA_SEP +
                    OphthalLens.LENS_NAME + TEXT_TYPE + COMMA_SEP +
                    OphthalLens.LENS_DOCID + INTEGER_TYPE + COMMA_SEP +
                    OphthalLens.LENS_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    OphthalLens.LENS_LEFT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalLens.LENS_RIGHT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalLens.LENS_USERID + INTEGER_TYPE + COMMA_SEP +
                    OphthalLens.LENS_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_LENS_TABLE =
            "DROP TABLE IF EXISTS " + OphthalLens.LENS_TABLE_NAME;

    private static final String SQL_CREATE_VITEROUS_TABLE =
            "CREATE TABLE " + OphthalViterous.VITEROUS_TABLE_NAME + " (" +
                    OphthalViterous.VITEROUS_AUTO_ID + "  integer primary key autoincrement, "+
                    OphthalViterous.VITEROUS_ID + INTEGER_TYPE + COMMA_SEP +
                    OphthalViterous.VITEROUS_NAME + TEXT_TYPE + COMMA_SEP +
                    OphthalViterous.VITEROUS_DOCID + INTEGER_TYPE + COMMA_SEP +
                    OphthalViterous.VITEROUS_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    OphthalViterous.VITEROUS_LEFT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalViterous.VITEROUS_RIGHT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalViterous.VITEROUS_USERID + INTEGER_TYPE + COMMA_SEP +
                    OphthalViterous.VITEROUS_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_VITEROUS_TABLE =
            "DROP TABLE IF EXISTS " + OphthalViterous.VITEROUS_TABLE_NAME;

    private static final String SQL_CREATE_FUNDUS_TABLE =
            "CREATE TABLE " + OphthalFundus.FUNDUS_TABLE_NAME + " (" +
                    OphthalFundus.FUNDUS_AUTO_ID + "  integer primary key autoincrement, "+
                    OphthalFundus.FUNDUS_ID + INTEGER_TYPE + COMMA_SEP +
                    OphthalFundus.FUNDUS_NAME + TEXT_TYPE + COMMA_SEP +
                    OphthalFundus.FUNDUS_DOCID + INTEGER_TYPE + COMMA_SEP +
                    OphthalFundus.FUNDUS_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    OphthalFundus.FUNDUS_LEFT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalFundus.FUNDUS_RIGHT_EYE + TEXT_TYPE + COMMA_SEP +
                    OphthalFundus.FUNDUS_USERID + INTEGER_TYPE + COMMA_SEP +
                    OphthalFundus.FUNDUS_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_FUNDUS_TABLE =
            "DROP TABLE IF EXISTS " + OphthalFundus.FUNDUS_TABLE_NAME;

    private static final String SQL_CREATE_INVESTIGATIONS_TEMPLATE_TABLE =
            "CREATE TABLE " + InvestigationTemplates.INVESTIGATION_TEMPLATE_TABLE_NAME + " (" +
                    InvestigationTemplates.INVESTIGATION_TEMPLATE_AUTO_ID + "  integer primary key autoincrement, "+
                    InvestigationTemplates.INVESTIGATION_TEMPLATE_ID + INTEGER_TYPE + COMMA_SEP +
                    InvestigationTemplates.INVESTIGATION_TEMPLATE_NAME + TEXT_TYPE + COMMA_SEP +
                    InvestigationTemplates.INVESTIGATION_TEMPLATE_DEFAULT_VISIBLE + INTEGER_TYPE + COMMA_SEP +
                    InvestigationTemplates.INVESTIGATION_TEMPLATE_TEST_ID + TEXT_TYPE + COMMA_SEP +
                    InvestigationTemplates.INVESTIGATION_TEMPLATE_GROUP_TEST_ID + TEXT_TYPE + COMMA_SEP +
                    InvestigationTemplates.INVESTIGATION_TEMPLATE_TEST_NAME + TEXT_TYPE + COMMA_SEP +
                    InvestigationTemplates.INVESTIGATION_TEMPLATE_TEST_DEPARTMENT + INTEGER_TYPE + COMMA_SEP +
                    InvestigationTemplates.INVESTIGATION_TEMPLATE_NORMAL_VALUE + TEXT_TYPE + COMMA_SEP +
                    InvestigationTemplates.INVESTIGATION_TEMPLATE_ACTUAL_VALUE + TEXT_TYPE + COMMA_SEP +
                    InvestigationTemplates.INVESTIGATION_TEMPLATE_RIGHT_EYE + TEXT_TYPE + COMMA_SEP +
                    InvestigationTemplates.INVESTIGATION_TEMPLATE_LEFT_EYE + TEXT_TYPE + COMMA_SEP +
                    InvestigationTemplates.INVESTIGATION_TEMPLATE_DOCID + INTEGER_TYPE + COMMA_SEP +
                    InvestigationTemplates.INVESTIGATION_TEMPLATE_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    InvestigationTemplates.INVESTIGATION_TEMPLATE_USERID + INTEGER_TYPE + COMMA_SEP +
                    InvestigationTemplates.INVESTIGATION_TEMPLATE_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_INVESTIGATIONS_TEMPLATE_TABLE =
            "DROP TABLE IF EXISTS " + InvestigationTemplates.INVESTIGATION_TEMPLATE_TABLE_NAME;

    private static final String SQL_CREATE_PATIENT_EDUCATION_TABLE =
            "CREATE TABLE " + PatientEducation.EDUCATION_TEMPLATE_TABLE_NAME + " (" +
                    PatientEducation.EDUCATION_AUTO_ID + "  integer primary key autoincrement, "+
                    PatientEducation.EDUCATION_ID + INTEGER_TYPE + COMMA_SEP +
                    PatientEducation.EDUCATION_TITLE + TEXT_TYPE + COMMA_SEP +
                    PatientEducation.EDUCATION_DESCRIPTION + INTEGER_TYPE + COMMA_SEP +
                    PatientEducation.EDUCATION_DOCID + INTEGER_TYPE + COMMA_SEP +
                    PatientEducation.EDUCATION_DOCTYPE + INTEGER_TYPE + COMMA_SEP +
                    PatientEducation.EDUCATION_USERID + INTEGER_TYPE + COMMA_SEP +
                    PatientEducation.EDUCATION_USER_LOGINTYPE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_PATIENT_EDUCATION_TABLE =
            "DROP TABLE IF EXISTS " + PatientEducation.EDUCATION_TEMPLATE_TABLE_NAME;

    public MedisensePracticeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_PATIENT_TABLE);
        db.execSQL(SQL_CREATE_DOCTORS_TABLE);
        db.execSQL(SQL_CREATE_SPECIALIZATION_TABLE);
        db.execSQL(SQL_CREATE_HOSPITAL_TABLE);
        db.execSQL(SQL_CREATE_NOTIFICATION_TABLE);
        db.execSQL(SQL_CREATE_MYPATIENT_TABLE);
        db.execSQL(SQL_CREATE_COUNTRY_TABLE);
        db.execSQL(SQL_CREATE_STATE_TABLE);
        db.execSQL(SQL_CREATE_CHIEF_MEDCOMPLAINT_TABLE);
        db.execSQL(SQL_CREATE_FREQUENT_MEDCOMPLAINT_TABLE);
        db.execSQL(SQL_CREATE_INVESTIGATION_TABLE);
        db.execSQL(SQL_CREATE_FREQUENT_INVESTIGATION_TABLE);
        db.execSQL(SQL_CREATE_INVESTIGATION_GROUPTEST_TABLE);
        db.execSQL(SQL_CREATE_FREQUENT_DIAGNOSIS_TABLE);
        db.execSQL(SQL_CREATE_FREQUENT_TREATMENT_TABLE);
        db.execSQL(SQL_CREATE_TREATMENT_TABLE);
        db.execSQL(SQL_CREATE_FREQ_PRESCRIPTION_TABLE);
        db.execSQL(SQL_CREATE_REPEAT_PRESCRIPTION_TABLE);
        db.execSQL(SQL_CREATE_DRUG_ALLERGY_TABLE);
        db.execSQL(SQL_CREATE_FREQ_DRUG_ABUSE_TABLE);
        db.execSQL(SQL_CREATE_DRUG_ABUSE_TABLE);
        db.execSQL(SQL_CREATE_FREQ_FAMILY_HISTORY_TABLE);
        db.execSQL(SQL_CREATE_FAMILY_HISTORY_TABLE);
        db.execSQL(SQL_CREATE_LIDS_TABLE);
        db.execSQL(SQL_CREATE_CONJUCTIVA_TABLE);
        db.execSQL(SQL_CREATE_SCLERA_TABLE);
        db.execSQL(SQL_CREATE_CORNEA_ANTERIOR_TABLE);
        db.execSQL(SQL_CREATE_CORNEA_POSTERIOR_TABLE);
        db.execSQL(SQL_CREATE_ANTERIOR_CHAMBER_TABLE);
        db.execSQL(SQL_CREATE_IRIS_TABLE);
        db.execSQL(SQL_CREATE_PUPIL_TABLE);
        db.execSQL(SQL_CREATE_ANGLE_TABLE);
        db.execSQL(SQL_CREATE_LENS_TABLE);
        db.execSQL(SQL_CREATE_VITEROUS_TABLE);
        db.execSQL(SQL_CREATE_FUNDUS_TABLE);
        db.execSQL(SQL_CREATE_INVESTIGATIONS_TEMPLATE_TABLE);
        db.execSQL(SQL_CREATE_PATIENT_EDUCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PATIENT_TABLE);
        db.execSQL(SQL_DELETE_DOCTORS_TABLE);
        db.execSQL(SQL_DELETE_SPECIALIZATION_TABLE);
        db.execSQL(SQL_DELETE_HOSPITAL_TABLE);
        db.execSQL(SQL_DELETE_NOTIFICATION_TABLE);
        db.execSQL(SQL_DELETE_MYPATIENT_TABLE);
        db.execSQL(SQL_DELETE_COUNTRY_TABLE);
        db.execSQL(SQL_DELETE_STATE_TABLE);
        db.execSQL(SQL_DELETE_CHIEF_MEDCOMPLAINT_TABLE);
        db.execSQL(SQL_DELETE_FREQUENT_MEDCOMPLAINT_TABLE);
        db.execSQL(SQL_DELETE_INVESTIGATION_TABLE);
        db.execSQL(SQL_DELETE_FREQUENT_INVESTIGATION_TABLE);
        db.execSQL(SQL_DELETE_INVESTIGATION_GROUPTEST_TABLE);
        db.execSQL(SQL_DELETE_FREQUENT_DIAGNOSIS_TABLE);
        db.execSQL(SQL_DELETE_FREQUENT_TREATMENT_TABLE);
        db.execSQL(SQL_DELETE_TREATMENT_TABLE);
        db.execSQL(SQL_DELETE_FREQ_PRESCRIPTION_TABLE);
        db.execSQL(SQL_DELETE_REPEAT_PRESCRIPTION_TABLE);
        db.execSQL(SQL_DELETE_DRUG_ALLERY_TABLE);
        db.execSQL(SQL_DELETE_FREQ_DRUG_ABUSE_TABLE);
        db.execSQL(SQL_DELETE_DRUG_ABUSE_TABLE);
        db.execSQL(SQL_DELETE_FREQ_FAMILY_HISTORY_TABLE);
        db.execSQL(SQL_DELETE_FAMILY_HISTORY_TABLE);
        db.execSQL(SQL_DELETE_LIDS_TABLE);
        db.execSQL(SQL_DELETE_CONJUCTIVA_TABLE);
        db.execSQL(SQL_DELETE_SCLERA_TABLE);
        db.execSQL(SQL_DELETE_CORNEA_ANTERIOR_TABLE);
        db.execSQL(SQL_DELETE_CORNEA_POSTERIOR_TABLE);
        db.execSQL(SQL_DELETE_ANTERIOR_CHAMBER_TABLE);
        db.execSQL(SQL_DELETE_IRIS_TABLE);
        db.execSQL(SQL_DELETE_PUPIL_TABLE);
        db.execSQL(SQL_DELETE_ANGLE_TABLE);
        db.execSQL(SQL_DELETE_LENS_TABLE);
        db.execSQL(SQL_DELETE_VITEROUS_TABLE);
        db.execSQL(SQL_DELETE_FUNDUS_TABLE);
        db.execSQL(SQL_DELETE_INVESTIGATIONS_TEMPLATE_TABLE);
        db.execSQL(SQL_DELETE_PATIENT_EDUCATION_TABLE);
        onCreate(db);
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
