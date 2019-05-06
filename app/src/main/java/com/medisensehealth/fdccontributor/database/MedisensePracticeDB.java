package com.medisensehealth.fdccontributor.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.medisensehealth.fdccontributor.DataModel.ChiefMedicalComplaint;
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
import com.medisensehealth.fdccontributor.utils.Utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lenovo on 02/06/2017.
 */

public class MedisensePracticeDB {

    private Context mcontext;
    static MedisensePracticeDBHelper mDBHelper;
    static Long uID;
    static String uName;

    public static void getDB(Context context) {
        if (mDBHelper == null)
            mDBHelper = new MedisensePracticeDBHelper(context);
    }

    public static void setUID(Long UID, String name) {
        uID = UID;
        uName = name;
    }

    public MedisensePracticeDB(Context context) {
        this.mcontext = context;
    }

    public static String getuName() {
        return uName;
    }

    /* Start of Patients List Screen*/
    public static long patientInsert(Context context, int patientId, String patient_name,String patient_age, String patient_location, int patient_status, String patient_docname, int patient_doctorId, String loginType, String referredBy, int login_userid, String ststus_time)
    {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PatientsList.PATIENT_ID, patientId);
        values.put(PatientsList.PATIENT_NAME, patient_name);
        values.put(PatientsList.PATIENT_AGE, patient_age);
        values.put(PatientsList.PATIENT_LOCATION, patient_location);
        values.put(PatientsList.PATIENT_STATUS, patient_status);
        values.put(PatientsList.PATIENT_DOCNAME, patient_docname);
        values.put(PatientsList.PATIENT_DOCTORID, patient_doctorId);
        values.put(PatientsList.PATIENT_LOGINTYPE, loginType);
        values.put(PatientsList.PATIENT_REFERBY, referredBy);
        values.put(PatientsList.PATIENT_LOGINUSERID, login_userid);
        values.put(PatientsList.PATIENT_STATUS_TIME, ststus_time);

        return  writabledb.insert(PatientsList.PATIENTS_TABLE_NAME, null, values);
    }

    public static void clearPatientEntries(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM "+ PatientsList.PATIENTS_TABLE_NAME + " WHERE "+ PatientsList.PATIENT_LOGINUSERID +"= "+user_id+" AND " + PatientsList.PATIENT_LOGINTYPE +" = "+loginType);
    }

    // Get All Patient Lists
    public static List<PatientsList> getAllPatients(Context context, String loginType, int userid) {
        List<PatientsList> plist = new LinkedList<PatientsList>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " +PatientsList.PATIENTS_TABLE_NAME+ " WHERE "+PatientsList.PATIENT_LOGINTYPE+" = '"+loginType+"' and "+PatientsList.PATIENT_LOGINUSERID+ " = '" + userid+"'";
        Cursor cursor = writabledb.rawQuery(query, null);

        PatientsList patients = null;
        if (cursor.moveToFirst()) {
            do {
                patients = new PatientsList();
                patients.setPatientId(cursor.getInt(1));
                patients.setPatientName(cursor.getString(2));
                patients.setPatientAge(cursor.getString(3));
                patients.setPatientCity(cursor.getString(4));
                patients.setPatientStatus(cursor.getInt(5));
                patients.setPatientDocName(cursor.getString(6));
                patients.setPatienDocId(cursor.getInt(7));
                patients.setPatientLoginType(cursor.getString(8));
                patients.setPatientReferBy(cursor.getString(9));
                patients.setPatientLoginUserId(cursor.getInt(10));
                patients.setPatientStatusTime(cursor.getString(11));
                plist.add(patients);
            } while (cursor.moveToNext());
        }
        return plist;
    }
    /* End of Patients List Screen*/

    /* Start of Doctors List Screen*/
    public static long doctorsInsert(Context context, int doc_id, String doc_name,int doc_age, String doc_exp, String doc_photo,String doc_qualification, int doc_specid, String doc_spec_name, int doc_favourite, String loginType, int login_userid, String doc_address)
    {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put(DoctorList.DOCTORS_ID, doc_id);
        newValues.put(DoctorList.DOCTORS_NAME,doc_name);
        newValues.put(DoctorList.DOCTORS_AGE,doc_age);
        newValues.put(DoctorList.DOCTORS_EXPERIENCE,doc_exp);
        newValues.put(DoctorList.DOCTORS_PHOTO,doc_photo);
        newValues.put(DoctorList.DOCTORS_QUALIFICATION,doc_qualification);
        newValues.put(DoctorList.DOCTORS_SPEC_ID,doc_specid);
        newValues.put(DoctorList.DOCTORS_SPEC_NAME,doc_spec_name);
        newValues.put(DoctorList.DOCTORS_FAVOURITES,doc_favourite);
        newValues.put(DoctorList.DOCTORS_LOGIN_TYPE,loginType);
        newValues.put(DoctorList.DOCTORS_LOGIN_USERID,login_userid);
        newValues.put(DoctorList.DOCTORS_ADDRESS,doc_address);

       return writabledb.insert(DoctorList.DOCTORS_TABLE_NAME, null, newValues);
    }

    // Get All Doctors
    public static List<DoctorList> getAllDoctors(Context context, String loginType, int login_userid) {
        List<DoctorList> dlist = new LinkedList<DoctorList>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        // 1. build the query
        String query = "SELECT  * FROM " + DoctorList.DOCTORS_TABLE_NAME + " WHERE "+DoctorList.DOCTORS_LOGIN_TYPE+" = '"+loginType+ "' and "+DoctorList.DOCTORS_LOGIN_USERID+" = '" + login_userid+"' ORDER BY ID ASC";
        //   SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = writabledb.rawQuery(query, null);
        // 3. go over each row, build book and add it to list
        DoctorList doctors = null;
        if (cursor.moveToFirst()) {
            do {
                doctors = new DoctorList();
                doctors.setDoctorId(cursor.getInt(1));
                doctors.setDoctorName(cursor.getString(2));
                doctors.setDoctorAge(cursor.getInt(3));
                doctors.setDoctorExperience(cursor.getString(4));
                doctors.setDoctorPhoto(cursor.getString(5));
                doctors.setDoctorQualification(cursor.getString(6));
                doctors.setDoctorSpecializationId(cursor.getInt(7));
                doctors.setDoctorSpecificationName(cursor.getString(8));
                doctors.setDoctorFavourite(cursor.getInt(9));
                doctors.setDoctorLoginType(cursor.getString(10));
                doctors.setDoctorLoginUserId(cursor.getInt(11));
                doctors.setDoctorAddress(cursor.getString(12));

                dlist.add(doctors);
            } while (cursor.moveToNext());
        }
        return dlist;
    }

    // Doctors Update
    public static void doctorsUpdateEntry(Context context, int docid, String docName,int docAge, String docExp, String docPhoto, String doc_qualification, int specId, String specName, int favourite, String loginType, int login_userid, String doc_address)
    {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(DoctorList.DOCTORS_ID, docid);
        updatedValues.put(DoctorList.DOCTORS_NAME,docName);
        updatedValues.put(DoctorList.DOCTORS_AGE,docAge);
        updatedValues.put(DoctorList.DOCTORS_EXPERIENCE,docExp);
        updatedValues.put(DoctorList.DOCTORS_PHOTO,docPhoto);
        updatedValues.put(DoctorList.DOCTORS_QUALIFICATION,doc_qualification);
        updatedValues.put(DoctorList.DOCTORS_SPEC_ID,specId);
        updatedValues.put(DoctorList.DOCTORS_SPEC_NAME,specName);
        updatedValues.put(DoctorList.DOCTORS_FAVOURITES,favourite);
        updatedValues.put(DoctorList.DOCTORS_LOGIN_TYPE,loginType);
        updatedValues.put(DoctorList.DOCTORS_LOGIN_USERID,login_userid);
        updatedValues.put(DoctorList.DOCTORS_ADDRESS,doc_address);

        String where= DoctorList.DOCTORS_ID + " = ? AND "+DoctorList.DOCTORS_LOGIN_TYPE+" = ? AND "+DoctorList.DOCTORS_LOGIN_USERID+" = ? ";
        writabledb.update(DoctorList.DOCTORS_TABLE_NAME,updatedValues, where, new String[]{String.valueOf(docid), loginType, String.valueOf(login_userid)});
    }

    public static DoctorList doctorsGetExistingData(Context context, int docid, String loginType, int login_userid)
    {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        Cursor cursor = writabledb.query(DoctorList.DOCTORS_TABLE_NAME, null, DoctorList.DOCTORS_ID+" =? AND "+DoctorList.DOCTORS_LOGIN_TYPE+" = ? AND "+DoctorList.DOCTORS_LOGIN_USERID+" = ?", new String[]{String.valueOf(docid),loginType,String.valueOf(login_userid)}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            Log.d(Utils.TAG, "get: " + "NOT EXISTS");

        }
        cursor.moveToFirst();
        DoctorList doctor_row = new DoctorList(cursor.getInt(1),cursor.getString(2),cursor.getInt(3), cursor.getString(4),
                cursor.getString(5), cursor.getString(6), cursor.getInt(7),cursor.getString(8),cursor.getInt(9),cursor.getString(10),cursor.getInt(11),cursor.getString(12));
        cursor.close();
        return doctor_row;
    }

    public static int doctorsGetRowExists(Context context, int docid, String loginType, int login_userid)
    {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        Cursor cursor = writabledb.query(DoctorList.DOCTORS_TABLE_NAME, null, DoctorList.DOCTORS_ID+" =?  AND "+DoctorList.DOCTORS_LOGIN_TYPE+" = ? AND "+DoctorList.DOCTORS_LOGIN_USERID+" = ? ", new String[]{String.valueOf(docid),loginType,String.valueOf(login_userid)}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            Log.d(Utils.TAG, "get: " + "NOT EXISTS");
            return 0;
        }
        else {
            cursor.moveToFirst();
            String DoctorName= cursor.getString(cursor.getColumnIndex(DoctorList.DOCTORS_NAME));
            String Favourie= cursor.getString(cursor.getColumnIndex(DoctorList.DOCTORS_FAVOURITES));
            cursor.close();
            return 1;
        }
    }

    public static String doctorsClearContents(Context context, String loginType, int login_userid)
    {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM "+DoctorList.DOCTORS_TABLE_NAME+" WHERE "+DoctorList.DOCTORS_FAVOURITES+" = 0 AND "+DoctorList.DOCTORS_LOGIN_TYPE+" = '"+loginType+"' AND "+DoctorList.DOCTORS_LOGIN_USERID+" ='"+login_userid+"'"); //delete all rows in a table
        Log.d(Utils.TAG, "delete table rows");
        return "done";
    }

    public static void  updateDoctorFavourite(Context context, int docid, int favourite, String loginType, int login_userid)
    {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(DoctorList.DOCTORS_ID, docid);
        updatedValues.put(DoctorList.DOCTORS_FAVOURITES,favourite);
        updatedValues.put(DoctorList.DOCTORS_LOGIN_TYPE,loginType);
        updatedValues.put(DoctorList.DOCTORS_LOGIN_USERID,login_userid);

        String where=DoctorList.DOCTORS_ID+" = ?  AND "+DoctorList.DOCTORS_LOGIN_TYPE+" = ? AND "+DoctorList.DOCTORS_LOGIN_USERID+"  = ?  ";
        writabledb.update(DoctorList.DOCTORS_TABLE_NAME,updatedValues, where, new String[]{String.valueOf(docid), loginType, String.valueOf(login_userid)});

        Log.d(Utils.TAG, "uPDATED fAVOURITE"+ "docid: "+docid + "favourite: " +favourite);
    }

    public static DoctorList getDoctorsExistingData(Context context, int docid, String loginType, int login_userid)
    {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        Cursor cursor=writabledb.query(DoctorList.DOCTORS_TABLE_NAME, null, DoctorList.DOCTORS_ID +" =? AND "+DoctorList.DOCTORS_LOGIN_TYPE+" = ? AND "+DoctorList.DOCTORS_LOGIN_USERID+" = ?", new String[]{String.valueOf(docid),loginType,String.valueOf(login_userid)}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            Log.d(Utils.TAG, "get: " + "NOT EXISTS");

        }
        cursor.moveToFirst();
        DoctorList doctor_row = new DoctorList(cursor.getInt(1),cursor.getString(2),cursor.getInt(3), cursor.getString(4),
                cursor.getString(5), cursor.getString(6), cursor.getInt(7),cursor.getString(8),cursor.getInt(9),cursor.getString(10),cursor.getInt(11),cursor.getString(12));

        cursor.close();

        return doctor_row;
    }
     /* End of Doctors List Screen*/

     /* Start of Specialization List Screen*/
     public static void insertSpecialization(Context context, int specId, String specialization_name)
     {
         getDB(context);
         SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
         ContentValues newValues = new ContentValues();
         newValues.put(SpecializationList.SPECIALIZATION_ID, specId);
         newValues.put(SpecializationList.SPECIALIZATION_NAME,specialization_name);
         writabledb.insert(SpecializationList.SPECIALIZATION_TABLE_NAME, null, newValues);
     }

    public static void clearSpecialization(Context context) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM "+ SpecializationList.SPECIALIZATION_TABLE_NAME );
    }

    public static void  updateSpecialization(Context context, int spec_id, String specName)
    {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(SpecializationList.SPECIALIZATION_ID, spec_id);
        updatedValues.put(SpecializationList.SPECIALIZATION_NAME,specName);

        String where= SpecializationList.SPECIALIZATION_ID + " = ?";
        writabledb.update(SpecializationList.SPECIALIZATION_TABLE_NAME,updatedValues, where, new String[]{String.valueOf(spec_id)});
    }

    // Get All Specializations
    public static List<SpecializationList> getAllSpecialization(Context context) {
        List<SpecializationList> speclist = new LinkedList<SpecializationList>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        // 1. build the query
        String query = "SELECT  * FROM " + SpecializationList.SPECIALIZATION_TABLE_NAME +" ORDER BY SPECIALIZATION ASC";
        Cursor cursor = writabledb.rawQuery(query, null);
        // 3. go over each row, build book and add it to list
        SpecializationList specialization = null;
        if (cursor.moveToFirst()) {
            do {
                specialization = new SpecializationList();
                specialization.setSpecializationId(cursor.getInt(1));
                specialization.setSpecializationName(cursor.getString(2));

                speclist.add(specialization);
            } while (cursor.moveToNext());
        }
        return speclist;
    }
     /* End of Specialization List Screen*/

     /* Start of Hospital List Screen*/
     public static void hospitalInsert(Context context, int hosp_id, String hosp_name, String hosp_city, String hosp_state, int user_id, String loginType )
     {
         getDB(context);
         SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
         ContentValues newValues = new ContentValues();
         newValues.put(HospitalList.HOSPITAL_ID, hosp_id);
         newValues.put(HospitalList.HOSPITAL_NAME,hosp_name);
         newValues.put(HospitalList.HOSPITAL_CITY,hosp_city);
         newValues.put(HospitalList.HOSPITAL_STATE,hosp_state);
         newValues.put(HospitalList.HOSPITAL_USER_ID,user_id);
         newValues.put(HospitalList.HOSPITAL_LOGIN_TYPE,loginType);

         writabledb.insert(HospitalList.HOSPITAL_TABLE_NAME, null, newValues);
     }

    public static void clearHospital(Context context, int user_id, String login_type) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM "+HospitalList.HOSPITAL_TABLE_NAME+" WHERE " +HospitalList.HOSPITAL_USER_ID+" = '"+user_id+"' AND "+HospitalList.HOSPITAL_LOGIN_TYPE+" = '"+login_type+"'"); //delete all rows in a table
    }

    public static void  updateHospital(Context context, int hospiid, String hospName, String hospCity, String hospState, int userId, String loginType)
    {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(HospitalList.HOSPITAL_ID, hospiid);
        updatedValues.put(HospitalList.HOSPITAL_NAME,hospName);
        updatedValues.put(HospitalList.HOSPITAL_CITY,hospCity);
        updatedValues.put(HospitalList.HOSPITAL_STATE,hospState);
        updatedValues.put(HospitalList.HOSPITAL_USER_ID,userId);
        updatedValues.put(HospitalList.HOSPITAL_LOGIN_TYPE,loginType);

        String where= HospitalList.HOSPITAL_ID +" = ?";
        writabledb.update(HospitalList.HOSPITAL_TABLE_NAME,updatedValues, where, new String[]{String.valueOf(hospiid)});
    }

    // Get All Hospitals
    public static List<HospitalList> getAllHospitals(Context context) {
        List<HospitalList> hlist = new LinkedList<HospitalList>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + HospitalList.HOSPITAL_TABLE_NAME + " ORDER BY HOSPITAL_NAME ASC";
        Cursor cursor = writabledb.rawQuery(query, null);
        HospitalList hospitals = null;
        if (cursor.moveToFirst()) {
            do {
                hospitals = new HospitalList();
                hospitals.setHospitalId(cursor.getInt(1));
                hospitals.setHospitalName(cursor.getString(2));
                hospitals.setHospitalCity(cursor.getString(3));
                hospitals.setHospitalState(cursor.getString(4));
                hospitals.setHospitalUserId(cursor.getInt(5));
                hospitals.setHospitalLoginType(cursor.getString(6));

                hlist.add(hospitals);
            } while (cursor.moveToNext());
        }
        return hlist;
    }
     /* End of Hospital List Screen*/

    /* Start of Notifications Screen*/
    public static void insertNotification(Context context, int user_id, String login_type, String post_id, String image_name, String title, String author, String currDate, String message, String postType, String entry_type, String patientId, String docId, String postKey )
    {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put(NotifyMessages.NOTIFICATION_USER_ID, user_id);
        newValues.put(NotifyMessages.NOTIFICATION_LOGIN_TYPE,login_type);
        newValues.put(NotifyMessages.NOTIFICATION_POST_ID,post_id);
        newValues.put(NotifyMessages.NOTIFICATION_POST_IMAGE,image_name);
        newValues.put(NotifyMessages.NOTIFICATION_TITLE,title);
        newValues.put(NotifyMessages.NOTIFICATION_AUTHOR,author);
        newValues.put(NotifyMessages.NOTIFICATION_POST_DATE,currDate);
        newValues.put(NotifyMessages.NOTIFICATION_POST_MESSAGE,message);
        newValues.put(NotifyMessages.NOTIFICATION_POST_TYPE,postType);
        newValues.put(NotifyMessages.NOTFICATION_ENTRY_TYPE,entry_type);
        newValues.put(NotifyMessages.NOTIFICATION_PATIENT_ID, patientId);
        newValues.put(NotifyMessages.NOTIFICATION_DOC_ID, docId);
        newValues.put(NotifyMessages.NOTIFICATION_POST_KEY, postKey);

        writabledb.insert(NotifyMessages.NOTIFICATION_TABLE_NAME, null, newValues);
    }

    public static int getNotificationCount(Context context, int user_id, String login_type )
    {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + NotifyMessages.NOTIFICATION_TABLE_NAME+" WHERE "+NotifyMessages.NOTIFICATION_USER_ID+" = '"+user_id+"' AND "+NotifyMessages.NOTIFICATION_LOGIN_TYPE+" = '"+login_type+"'" ;
        Cursor cursor = writabledb.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
//        cursor.close();
        return cnt;
    }

    public static Cursor getAllNotification(Context context, int user_id, String login_type )
    {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + NotifyMessages.NOTIFICATION_TABLE_NAME +" WHERE "+NotifyMessages.NOTIFICATION_USER_ID+" = '"+user_id+"' AND "+NotifyMessages.NOTIFICATION_LOGIN_TYPE+" = '"+login_type+"'" ;
        Cursor cursor = writabledb.rawQuery(countQuery, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
        }
        cursor.moveToFirst();
        return cursor;
    }

    public static String  deleteNotificationContents(Context context, int login_userid,String loginType, int post_id)
    {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String deleteQuery = "DELETE FROM "+NotifyMessages.NOTIFICATION_TABLE_NAME+" WHERE "+NotifyMessages.NOTIFICATION_USER_ID+" = '"+login_userid+"' AND "+NotifyMessages.NOTIFICATION_LOGIN_TYPE+" = '"+loginType+"' AND "+NotifyMessages.NOTIFICATION_AUTO_ID+" ='"+post_id+"'" ;

        writabledb.execSQL(deleteQuery); //delete all rows in a table
        Log.d(Utils.TAG, "delete table rows: " + deleteQuery);
        return "done";
    }

    public static String  deleteNotificationResponseContents(Context context, int login_userid,String loginType, String post_key)
    {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String deleteQuery = "DELETE FROM "+NotifyMessages.NOTIFICATION_TABLE_NAME+" WHERE "+NotifyMessages.NOTIFICATION_USER_ID+" = '"+login_userid+"' AND "+NotifyMessages.NOTIFICATION_LOGIN_TYPE+" = '"+loginType+"' AND "+NotifyMessages.NOTIFICATION_POST_KEY+" ='"+post_key+"'" ;

        writabledb.execSQL(deleteQuery); //delete all rows in a table
        Log.d(Utils.TAG, "delete table rows: " + deleteQuery);
        return "done";
    }
    /* End of Notifications Screen*/


    /* Start My Patient Chief Medical Complaint */
    public static long chiefMedicalComplaintInsert(Context context, int complaint_id, String symptoms_name,
                                                   int doc_id, int doc_type, int user_id,
                                                   String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ChiefMedicalComplaint.CHIEF_COMPLAINT_ID, complaint_id);
        values.put(ChiefMedicalComplaint.CHIEF_SYMPTOMS_NAME, symptoms_name);
        values.put(ChiefMedicalComplaint.CHIEF_DOCID, doc_id);
        values.put(ChiefMedicalComplaint.CHIEF_DOCTYPE, doc_type);
        values.put(ChiefMedicalComplaint.CHIEF_USERID, user_id);
        values.put(ChiefMedicalComplaint.CHIEF_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(ChiefMedicalComplaint.CHIEF_MEDICALCOMPLAINT_TABLE_NAME, null, values);
    }

    public static void clearMedicalComplaints(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + ChiefMedicalComplaint.CHIEF_MEDICALCOMPLAINT_TABLE_NAME + " WHERE " + ChiefMedicalComplaint.CHIEF_USERID + "= " + user_id + " AND " + ChiefMedicalComplaint.CHIEF_USER_LOGINTYPE + " = " + loginType);
    }

    // Get All Chief Medical Complaints
    public static List<ChiefMedicalComplaint> getAllChiefMedicalComplaint(Context context, String loginType, int userid) {
        List<ChiefMedicalComplaint> complist = new LinkedList<ChiefMedicalComplaint>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + ChiefMedicalComplaint.CHIEF_MEDICALCOMPLAINT_TABLE_NAME + " WHERE " + ChiefMedicalComplaint.CHIEF_USER_LOGINTYPE + " = '" + loginType + "' and " + ChiefMedicalComplaint.CHIEF_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        ChiefMedicalComplaint complaints = null;
        if (cursor.moveToFirst()) {
            do {
                complaints = new ChiefMedicalComplaint();
                complaints.setComplaintId(cursor.getInt(1));
                complaints.setSymptomsName(cursor.getString(2));
                complaints.setDocId(cursor.getInt(3));
                complaints.setDocType(cursor.getInt(4));
                complaints.setUserId(cursor.getInt(5));
                complaints.setLoginType(cursor.getString(6));
                complist.add(complaints);
            } while (cursor.moveToNext());
        }
        return complist;
    }
    /* End of Chief Medical Compalaint Screen*/

    /* Start Frequent Chief Medical Complaint */
    public static long frequentComplaintInsert(Context context, int freq_complaint_id, int symptoms_id,
                                               int doc_id, int doc_type, int freq_count, String symp_name, int user_id,
                                               String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FrequentChiefComplaints.FREQUENT_COMPLAINT_ID, freq_complaint_id);
        values.put(FrequentChiefComplaints.FREQUENT_SYMPTOMS_ID, symptoms_id);
        values.put(FrequentChiefComplaints.FREQUENT_SYMPTOMS_NAME, symp_name);
        values.put(FrequentChiefComplaints.FREQUENT_DOCID, doc_id);
        values.put(FrequentChiefComplaints.FREQUENT_DOCTYPE, doc_type);
        values.put(FrequentChiefComplaints.FREQUENT_COUNT, freq_count);
        values.put(FrequentChiefComplaints.FREQUENT_USERID, user_id);
        values.put(FrequentChiefComplaints.FREQUENT_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(FrequentChiefComplaints.FREQUENT_COMPLAINT_TABLE_NAME, null, values);
    }

    public static void clearFrequentComplaints(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + FrequentChiefComplaints.FREQUENT_COMPLAINT_TABLE_NAME + " WHERE " + FrequentChiefComplaints.FREQUENT_USERID + "= " + user_id + " AND " + FrequentChiefComplaints.FREQUENT_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<FrequentChiefComplaints> getAllFrequentMedicalComplaint(Context context, String loginType, int userid) {
        List<FrequentChiefComplaints> freqlist = new LinkedList<FrequentChiefComplaints>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + FrequentChiefComplaints.FREQUENT_COMPLAINT_TABLE_NAME + " WHERE " + FrequentChiefComplaints.FREQUENT_USER_LOGINTYPE + " = '" + loginType + "' and " + FrequentChiefComplaints.FREQUENT_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        FrequentChiefComplaints complaints = null;
        if (cursor.moveToFirst()) {
            do {
                complaints = new FrequentChiefComplaints();
                complaints.setFreqComplaintID(cursor.getInt(1));
                complaints.setFreqSymptomsID(cursor.getInt(2));
                complaints.setFreqSymptomsName(cursor.getString(3));
                complaints.setFreqDocID(cursor.getInt(4));
                complaints.setFreqDocType(cursor.getInt(5));
                complaints.setFreqCount(cursor.getInt(6));
                complaints.setFreqUserID(cursor.getInt(7));
                complaints.setFreqLoginType(cursor.getString(8));
                freqlist.add(complaints);
            } while (cursor.moveToNext());
        }
        return freqlist;
    }
    /* End of Frequent Chief Medical Complaint */

    /* Start Investigations Tests */
    public static long investigationTestsInsert(Context context, int invest_id, String test_id, String test_name, int department,
                                                String group_test, String mf_range, String reportable, String normal_range,
                                                String test_charges, String min_range, String max_range, String test_units,
                                                String crit_low_range, String crit_high_range, String normal_min_range, String normal_max_range,
                                                String department_name, String department_type, int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Investigations.INVESTIGATION_ID, invest_id);
        values.put(Investigations.INVESTIGATION_TEST_ID, test_id);
        values.put(Investigations.INVESTIGATION_TEST_NAME, test_name);
        values.put(Investigations.INVESTIGATION_TEST_DEPARTMENT, department);
        values.put(Investigations.INVESTIGATION_GROUP_TEST, group_test);
        values.put(Investigations.INVESTIGATION_MFRANGE, mf_range);
        values.put(Investigations.INVESTIGATION_REPORTABLE, reportable);
        values.put(Investigations.INVESTIGATION_NORMAL_RANGE, normal_range);
        values.put(Investigations.INVESTIGATION_TEST_CHARGES, test_charges);
        values.put(Investigations.INVESTIGATION_MIN_RANGE, min_range);
        values.put(Investigations.INVESTIGATION_MAX_RANGE, max_range);
        values.put(Investigations.INVESTIGATION_TEST_UNITS, test_units);
        values.put(Investigations.INVESTIGATION_CRIT_LOW_RANGE, crit_low_range);
        values.put(Investigations.INVESTIGATION_CRIT_HIGH_RANGE, crit_high_range);
        values.put(Investigations.INVESTIGATION_NORMAL_MIN_RANGE, normal_min_range);
        values.put(Investigations.INVESTIGATION_NORMAL_MAX_RANGE, normal_max_range);
        values.put(Investigations.INVESTIGATION_DEPT_NAME, department_name);
        values.put(Investigations.INVESTIGATION_DEPT_TYPE, department_type);
        values.put(Investigations.INVESTIGATION_USERID, user_id);
        values.put(Investigations.INVESTIGATION_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(Investigations.INVESTIGATION_TABLE_NAME, null, values);
    }

    public static void clearInvestigationTests(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + Investigations.INVESTIGATION_TABLE_NAME + " WHERE " + Investigations.INVESTIGATION_USERID + "= " + user_id + " AND " + Investigations.INVESTIGATION_USER_LOGINTYPE + " = " + loginType);
    }

    // Get All InvestigationTests
    public static List<Investigations> getAllInvestigationTests(Context context, String loginType, int userid) {
        List<Investigations> investlist = new LinkedList<Investigations>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + Investigations.INVESTIGATION_TABLE_NAME + " WHERE " + Investigations.INVESTIGATION_USER_LOGINTYPE + " = '" + loginType + "' and " + Investigations.INVESTIGATION_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        Investigations invest_test = null;
        if (cursor.moveToFirst()) {
            do {
                invest_test = new Investigations();
                invest_test.setInvestigationId(cursor.getInt(1));
                invest_test.setTestId(cursor.getString(2));
                invest_test.setTestName(cursor.getString(3));
                invest_test.setInvestDepartment(cursor.getInt(4));
                invest_test.setGroupTest(cursor.getString(5));
                invest_test.setMFRange(cursor.getString(6));
                invest_test.setReportable(cursor.getString(7));
                invest_test.setNormalRange(cursor.getString(8));
                invest_test.setTestCharges(cursor.getString(9));
                invest_test.setMinRange(cursor.getString(10));
                invest_test.setMaxRange(cursor.getString(11));
                invest_test.setTestUnits(cursor.getString(12));
                invest_test.setCritLowRange(cursor.getString(13));
                invest_test.setCritHighRange(cursor.getString(14));
                invest_test.setNormalMinRange(cursor.getString(15));
                invest_test.setNormalMaxRange(cursor.getString(16));
                invest_test.setDepartmentName(cursor.getString(17));
                invest_test.setDepartmentType(cursor.getString(18));
                invest_test.setUserId(cursor.getInt(19));
                invest_test.setLoginType(cursor.getString(20));
                investlist.add(invest_test);
            } while (cursor.moveToNext());
        }
        return investlist;
    }
    /* End of Investigations Tests */

    /* Start Frequent Investigations */
    public static long frequentInvestigationsInsert(Context context, int freq_invest_id, int test_id,
                                                    int doc_id, int doc_type, int freq_count, String test_name, int department,
                                                    int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FrequentInvestigations.FREQUENT_INVESTIGATION_ID, freq_invest_id);
        values.put(FrequentInvestigations.FREQUENT_TEST_ID, test_id);
        values.put(FrequentInvestigations.FREQUENT_TEST_NAME, test_name);
        values.put(FrequentInvestigations.FREQUENT_TEST_DEPARTMENT, department);
        values.put(FrequentInvestigations.FREQUENT_DOCID, doc_id);
        values.put(FrequentInvestigations.FREQUENT_DOCTYPE, doc_type);
        values.put(FrequentInvestigations.FREQUENT_COUNT, freq_count);
        values.put(FrequentInvestigations.FREQUENT_USERID, user_id);
        values.put(FrequentInvestigations.FREQUENT_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(FrequentInvestigations.FREQUENT_INVESTIGATION_TABLE_NAME, null, values);
    }

    public static void clearFrequentInvestigations(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + FrequentInvestigations.FREQUENT_INVESTIGATION_TABLE_NAME + " WHERE " + FrequentInvestigations.FREQUENT_USERID + "= " + user_id + " AND " + FrequentInvestigations.FREQUENT_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<FrequentInvestigations> getAllFrequentInvestigations(Context context, String loginType, int userid) {
        List<FrequentInvestigations> freqlist = new LinkedList<FrequentInvestigations>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + FrequentInvestigations.FREQUENT_INVESTIGATION_TABLE_NAME + " WHERE " + FrequentInvestigations.FREQUENT_USER_LOGINTYPE + " = '" + loginType + "' and " + FrequentInvestigations.FREQUENT_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        FrequentInvestigations complaints = null;
        if (cursor.moveToFirst()) {
            do {
                complaints = new FrequentInvestigations();
                complaints.setFreqInvestigationID(cursor.getInt(1));
                complaints.setFreqTestID(cursor.getInt(2));
                complaints.setFreqTestName(cursor.getString(3));
                complaints.setFreqTestDepartment(cursor.getInt(4));
                complaints.setFreqDocID(cursor.getInt(5));
                complaints.setFreqDocType(cursor.getInt(6));
                complaints.setFreqCount(cursor.getInt(7));
                complaints.setFreqUserID(cursor.getInt(8));
                complaints.setFreqLoginType(cursor.getString(9));
                freqlist.add(complaints);
            } while (cursor.moveToNext());
        }
        return freqlist;
    }
    /* End of Frequent Investigations */


    /* Start Investigation Group Tests */
    public static long investigationGroupTestInsert(Context context, int group_id, String test_id,
                                                    String subtest_id, String order_num, int user_id,
                                                    String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InvestigationGroupTests.INVEST_GROUP_ID, group_id);
        values.put(InvestigationGroupTests.INVEST_GROUP_TEST_ID, test_id);
        values.put(InvestigationGroupTests.INVEST_GROUP_SUBTEST_ID, subtest_id);
        values.put(InvestigationGroupTests.INVEST_GROUP_ORDER_NUM, order_num);
        values.put(InvestigationGroupTests.INVEST_GROUP_USERID, user_id);
        values.put(InvestigationGroupTests.INVEST_GROUP_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(InvestigationGroupTests.INVEST_GROUP_TABLE_NAME, null, values);
    }

    public static void clearInvestigationGroupTest(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + InvestigationGroupTests.INVEST_GROUP_TABLE_NAME + " WHERE " + InvestigationGroupTests.INVEST_GROUP_USERID + "= " + user_id + " AND " + InvestigationGroupTests.INVEST_GROUP_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<InvestigationGroupTests> getAllInvestigationGroupTest(Context context, String loginType, int userid) {
        List<InvestigationGroupTests> grouplist = new LinkedList<InvestigationGroupTests>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + InvestigationGroupTests.INVEST_GROUP_TABLE_NAME + " WHERE " + InvestigationGroupTests.INVEST_GROUP_USER_LOGINTYPE + " = '" + loginType + "' and " + InvestigationGroupTests.INVEST_GROUP_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        InvestigationGroupTests complaints = null;
        if (cursor.moveToFirst()) {
            do {
                complaints = new InvestigationGroupTests();
                complaints.setGroupTestId(cursor.getInt(1));
                complaints.setTestId(cursor.getString(2));
                complaints.setSubTestId(cursor.getString(3));
                complaints.setOrderNumber(cursor.getString(4));
                complaints.setUserId(cursor.getInt(5));
                complaints.setLoginType(cursor.getString(6));
                grouplist.add(complaints);
            } while (cursor.moveToNext());
        }
        return grouplist;
    }
    /* End of Investigation Group Tests */

    /* Start Frequent Diagnosis */
    public static long frequentDiagnosisInsert(Context context, int freq_diagno_id, int icd_id, String icd_name,
                                               int doc_id, int doc_type, int freq_count, int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Diagnosis.DIAGNOSIS_FREQ_ID, freq_diagno_id);
        values.put(Diagnosis.DIAGNOSIS_ICD_ID, icd_id);
        values.put(Diagnosis.DIAGNOSIS_ICD_NAME, icd_name);
        values.put(Diagnosis.DIAGNOSIS_DOCID, doc_id);
        values.put(Diagnosis.DIAGNOSIS_DOCTYPE, doc_type);
        values.put(Diagnosis.DIAGNOSIS_FREQUENT_COUNT, freq_count);
        values.put(Diagnosis.DIAGNOSIS_USERID, user_id);
        values.put(Diagnosis.DIAGNOSIS_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(Diagnosis.DIAGNOSIS_TABLE_NAME, null, values);
    }

    public static void clearFrequentDiagnosis(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + Diagnosis.DIAGNOSIS_TABLE_NAME + " WHERE " + Diagnosis.DIAGNOSIS_USERID + "= " + user_id + " AND " + Diagnosis.DIAGNOSIS_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<Diagnosis> getAllFrequentDiagnosis(Context context, String loginType, int userid) {
        List<Diagnosis> freqlist = new LinkedList<Diagnosis>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + Diagnosis.DIAGNOSIS_TABLE_NAME + " WHERE " + Diagnosis.DIAGNOSIS_USER_LOGINTYPE + " = '" + loginType + "' and " + Diagnosis.DIAGNOSIS_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        Diagnosis exams = null;
        if (cursor.moveToFirst()) {
            do {
                exams = new Diagnosis();
                exams.setDiagnoFreqId(cursor.getInt(1));
                exams.setICDId(cursor.getInt(2));
                exams.setICDName(cursor.getString(3));
                exams.setDocId(cursor.getInt(4));
                exams.setDocType(cursor.getInt(5));
                exams.setFreqCount(cursor.getInt(6));
                exams.setUserId(cursor.getInt(7));
                exams.setLoginType(cursor.getString(8));
                freqlist.add(exams);
            } while (cursor.moveToNext());
        }
        return freqlist;
    }
    /* End of Frequent Diagnosis */

    /* Start Frequent Treatments */
    public static long frequentTreatmentInsert(Context context, int treatment_id, int doc_id, int doc_type,
                                               int freq_count, String etreatment_name, int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Treatments.FREQ_TREATMENT_ID, treatment_id);
        values.put(Treatments.FREQ_TREATMENT_NAME, etreatment_name);
        values.put(Treatments.FREQ_TREATMENT_DOCID, doc_id);
        values.put(Treatments.FREQ_TREATMENT_DOCTYPE, doc_type);
        values.put(Treatments.FREQ_TREATMENT_COUNT, freq_count);
        values.put(Treatments.FREQ_TREATMENT_USERID, user_id);
        values.put(Treatments.FREQ_TREATMENT_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(Treatments.FREQ_TREATMENT_TABLE_NAME, null, values);
    }

    public static void clearFrequentTreatments(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + Treatments.FREQ_TREATMENT_TABLE_NAME + " WHERE " + Treatments.FREQ_TREATMENT_USERID + "= " + user_id + " AND " + Treatments.FREQ_TREATMENT_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<Treatments> getAllFrequentTreatments(Context context, String loginType, int userid) {
        List<Treatments> freqlist = new LinkedList<Treatments>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + Treatments.FREQ_TREATMENT_TABLE_NAME + " WHERE " + Treatments.FREQ_TREATMENT_USER_LOGINTYPE + " = '" + loginType + "' and " + Treatments.FREQ_TREATMENT_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        Treatments exams = null;
        if (cursor.moveToFirst()) {
            do {
                exams = new Treatments();
                exams.setTreatmentID(cursor.getInt(1));
                exams.setTreatmentName(cursor.getString(2));
                exams.setTreatmentDocID(cursor.getInt(3));
                exams.setTreatmentDocType(cursor.getInt(4));
                exams.setTreatmentFreqCount(cursor.getInt(5));
                exams.setTreatmentUserID(cursor.getInt(6));
                exams.setTreatmentLoginType(cursor.getString(7));
                freqlist.add(exams);
            } while (cursor.moveToNext());
        }
        return freqlist;
    }
    /* End of Frequent Treatments */

    /* Start Treatmenets */
    public static long treatmentInsert(Context context, int examination_id, String examination_name,
                                       int doc_id, int doc_type, int freq_count, int user_id,
                                       String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Treatments.TREATMENT_ID, examination_id);
        values.put(Treatments.TREATMENT_NAME, examination_name);
        values.put(Treatments.TREATMENT_DOCID, doc_id);
        values.put(Treatments.TREATMENT_DOCTYPE, doc_type);
        values.put(Treatments.TREATMENT_COUNT, freq_count);
        values.put(Treatments.TREATMENT_USERID, user_id);
        values.put(Treatments.TREATMENT_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(Treatments.TREATMENT_TABLE_NAME, null, values);
    }

    public static void clearTreatments(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + Treatments.TREATMENT_TABLE_NAME + " WHERE " + Treatments.TREATMENT_USERID + "= " + user_id + " AND " + Treatments.TREATMENT_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<Treatments> getAllTreatments(Context context, String loginType, int userid) {
        List<Treatments> examlist = new LinkedList<Treatments>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + Treatments.TREATMENT_TABLE_NAME + " WHERE " + Treatments.TREATMENT_USER_LOGINTYPE + " = '" + loginType + "' and " + Treatments.TREATMENT_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        Treatments examination = null;
        if (cursor.moveToFirst()) {
            do {
                examination = new Treatments();
                examination.setTreatmentID(cursor.getInt(1));
                examination.setTreatmentName(cursor.getString(2));
                examination.setTreatmentDocID(cursor.getInt(3));
                examination.setTreatmentDocType(cursor.getInt(4));
                examination.setTreatmentFreqCount(cursor.getInt(5));
                examination.setTreatmentUserID(cursor.getInt(6));
                examination.setTreatmentLoginType(cursor.getString(7));
                examlist.add(examination);
            } while (cursor.moveToNext());
        }
        return examlist;
    }
    /* End of Treatments */

    /* Start Frequent Prescriptions */
    public static long frequentPrescriptionInsert(Context context, int freqMed_id, int pp_id, String trade_name,
                                                  int generic_id, String generic_name, String frequency, String timings, String duration,
                                                  int doc_id, int doc_type, int freq_count, int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FrequentPrescription.FREQ_PRESCRIPTION_FREQ_ID, freqMed_id);
        values.put(FrequentPrescription.FREQ_PRESCRIPTION_PRODUCT_ID, pp_id);
        values.put(FrequentPrescription.FREQ_PRESCRIPTION_TRADE_NAME, trade_name);
        values.put(FrequentPrescription.FREQ_PRESCRIPTION_GENERIC_ID, generic_id);
        values.put(FrequentPrescription.FREQ_PRESCRIPTION_GENERIC_NAME, generic_name);
        values.put(FrequentPrescription.FREQ_PRESCRIPTION_DOASGE, frequency);
        values.put(FrequentPrescription.FREQ_PRESCRIPTION_TIMING, timings);
        values.put(FrequentPrescription.FREQ_PRESCRIPTION_DURATION, duration);
        values.put(FrequentPrescription.FREQ_PRESCRIPTION_DOCID, doc_id);
        values.put(FrequentPrescription.FREQ_PRESCRIPTION_DOCTYPE, doc_type);
        values.put(FrequentPrescription.FREQ_PRESCRIPTION_FREQUENT_COUNT, freq_count);
        values.put(FrequentPrescription.FREQ_PRESCRIPTION_USERID, user_id);
        values.put(FrequentPrescription.FREQ_PRESCRIPTION_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(FrequentPrescription.FREQ_PRESCRIPTION_TABLE_NAME, null, values);
    }

    public static void clearFrequentPrescriptions(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + FrequentPrescription.FREQ_PRESCRIPTION_TABLE_NAME + " WHERE " + FrequentPrescription.FREQ_PRESCRIPTION_USERID + "= " + user_id + " AND " + FrequentPrescription.FREQ_PRESCRIPTION_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<FrequentPrescription> getAllFrequentPrescriptions(Context context, String loginType, int userid) {
        List<FrequentPrescription> freqlist = new LinkedList<FrequentPrescription>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + FrequentPrescription.FREQ_PRESCRIPTION_TABLE_NAME + " WHERE " + FrequentPrescription.FREQ_PRESCRIPTION_USER_LOGINTYPE + " = '" + loginType + "' and " + FrequentPrescription.FREQ_PRESCRIPTION_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        FrequentPrescription presc = null;
        if (cursor.moveToFirst()) {
            do {
                presc = new FrequentPrescription();
                presc.setPrescFreqId(cursor.getInt(1));
                presc.setPresciptionId(cursor.getInt(2));
                presc.setTradeName(cursor.getString(3));
                presc.setGenericId(cursor.getInt(4));
                presc.setGenericName(cursor.getString(5));
                presc.setDosage(cursor.getString(6));
                presc.setTimings(cursor.getString(7));
                presc.setDuration(cursor.getString(8));
                presc.setDocId(cursor.getInt(9));
                presc.setDocType(cursor.getInt(10));
                presc.setFreqCount(cursor.getInt(11));
                presc.setUserId(cursor.getInt(12));
                presc.setLoginType(cursor.getString(13));
                freqlist.add(presc);
            } while (cursor.moveToNext());
        }
        return freqlist;
    }
    /* End of Frequent Prescriptions */

    /* Start Repeat Prescriptions */
    public static long repeatPrescriptionInsert(Context context, int freqMed_id, int pp_id, String trade_name,
                                                int generic_id, String generic_name, String frequency, String timings, String duration,
                                                int doc_id, int doc_type, int freq_count, int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FrequentPrescription.REPEAT_PRESCRIPTION_FREQ_ID, freqMed_id);
        values.put(FrequentPrescription.REPEAT_PRESCRIPTION_PRODUCT_ID, pp_id);
        values.put(FrequentPrescription.REPEAT_PRESCRIPTION_TRADE_NAME, trade_name);
        values.put(FrequentPrescription.REPEAT_PRESCRIPTION_GENERIC_ID, generic_id);
        values.put(FrequentPrescription.REPEAT_PRESCRIPTION_GENERIC_NAME, generic_name);
        values.put(FrequentPrescription.REPEAT_PRESCRIPTION_DOASGE, frequency);
        values.put(FrequentPrescription.REPEAT_PRESCRIPTION_TIMING, timings);
        values.put(FrequentPrescription.REPEAT_PRESCRIPTION_DURATION, duration);
        values.put(FrequentPrescription.REPEAT_PRESCRIPTION_DOCID, doc_id);
        values.put(FrequentPrescription.REPEAT_PRESCRIPTION_DOCTYPE, doc_type);
        values.put(FrequentPrescription.REPEAT_PRESCRIPTION_FREQUENT_COUNT, freq_count);
        values.put(FrequentPrescription.REPEAT_PRESCRIPTION_USERID, user_id);
        values.put(FrequentPrescription.REPEAT_PRESCRIPTION_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(FrequentPrescription.REPEAT_PRESCRIPTION_TABLE_NAME, null, values);
    }

    public static void clearRepeatPrescriptions(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + FrequentPrescription.REPEAT_PRESCRIPTION_TABLE_NAME + " WHERE " + FrequentPrescription.REPEAT_PRESCRIPTION_USERID + "= " + user_id + " AND " + FrequentPrescription.REPEAT_PRESCRIPTION_USER_LOGINTYPE + " = " + loginType);
    }


    public static List<FrequentPrescription> getAllRepeatPrescriptions(Context context, String loginType, int userid) {
        List<FrequentPrescription> freqlist = new LinkedList<FrequentPrescription>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + FrequentPrescription.REPEAT_PRESCRIPTION_TABLE_NAME + " WHERE " + FrequentPrescription.REPEAT_PRESCRIPTION_USER_LOGINTYPE + " = '" + loginType + "' and " + FrequentPrescription.REPEAT_PRESCRIPTION_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        FrequentPrescription presc = null;
        if (cursor.moveToFirst()) {
            do {
                presc = new FrequentPrescription();
                presc.setPrescFreqId(cursor.getInt(1));
                presc.setPresciptionId(cursor.getInt(2));
                presc.setTradeName(cursor.getString(3));
                presc.setGenericId(cursor.getInt(4));
                presc.setGenericName(cursor.getString(5));
                presc.setDosage(cursor.getString(6));
                presc.setTimings(cursor.getString(7));
                presc.setDuration(cursor.getString(8));
                presc.setDocId(cursor.getInt(9));
                presc.setDocType(cursor.getInt(10));
                presc.setFreqCount(cursor.getInt(11));
                presc.setUserId(cursor.getInt(12));
                presc.setLoginType(cursor.getString(13));
                freqlist.add(presc);
            } while (cursor.moveToNext());
        }
        return freqlist;
    }
    /* End of Repeat Prescriptions */


    /* Start of Drug Allery */
    public static long drugAllergyInsert(Context context, int allergy_id, int generic_id, String generic_name,
                                         int patient_id, int doc_id, int doc_type, int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DrugAllery.DRUG_ALLERY_ID, allergy_id);
        values.put(DrugAllery.DRUG_ALLERY_GENERIC_ID, generic_id);
        values.put(DrugAllery.DRUG_ALLERY_GENERIC_NAME, generic_name);
        values.put(DrugAllery.DRUG_ALLERY_PATIENTID, patient_id);
        values.put(DrugAllery.DRUG_ALLERY_DOCID, doc_id);
        values.put(DrugAllery.DRUG_ALLERY_DOCTYPE, doc_type);
        values.put(DrugAllery.DRUG_ALLERY_USERID, user_id);
        values.put(DrugAllery.DRUG_ALLERY_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(DrugAllery.DRUG_ALLERY_TABLE_NAME, null, values);
    }

    public static void clearDrugAllergy(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + DrugAllery.DRUG_ALLERY_TABLE_NAME + " WHERE " + DrugAllery.DRUG_ALLERY_USERID + "= " + user_id + " AND " + DrugAllery.DRUG_ALLERY_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<DrugAllery> getAllDrugAllery(Context context, String loginType, int userid) {
        List<DrugAllery> freqlist = new LinkedList<DrugAllery>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + DrugAllery.DRUG_ALLERY_TABLE_NAME + " WHERE " + DrugAllery.DRUG_ALLERY_USER_LOGINTYPE + " = '" + loginType + "' and " + DrugAllery.DRUG_ALLERY_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        DrugAllery allergy = null;
        if (cursor.moveToFirst()) {
            do {
                allergy = new DrugAllery();
                allergy.setAllergyId(cursor.getInt(1));
                allergy.setGenericId(cursor.getInt(2));
                allergy.setGenericName(cursor.getString(3));
                allergy.setPatientId(cursor.getInt(4));
                allergy.setDocId(cursor.getInt(5));
                allergy.setDocType(cursor.getInt(6));
                allergy.setUserId(cursor.getInt(7));
                allergy.setLoginType(cursor.getString(8));
                freqlist.add(allergy);
            } while (cursor.moveToNext());
        }
        return freqlist;
    }
    /* End of Drug Allery */

    /* Start of Drug Abuse Frequent Lists */
    public static long drugAbuseFrequentInsert(Context context, int abuse_freq_id, int abuse_id, String abuse_name,
                                               int doc_id, int doc_type, int freq_count, int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DrugAbuse.FREQ_DRUG_ABUSE_FREQID, abuse_freq_id);
        values.put(DrugAbuse.FREQ_DRUG_ABUSE_ID, abuse_id);
        values.put(DrugAbuse.FREQ_DRUG_ABUSE_NAME, abuse_name);
        values.put(DrugAbuse.FREQ_DRUG_ABUSE_DOCID, doc_id);
        values.put(DrugAbuse.FREQ_DRUG_ABUSE_DOCTYPE, doc_type);
        values.put(DrugAbuse.FREQ_DRUG_ABUSE_FREQ_COUNT, freq_count);
        values.put(DrugAbuse.FREQ_DRUG_ABUSE_USERID, user_id);
        values.put(DrugAbuse.FREQ_DRUG_ABUSE_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(DrugAbuse.FREQ_DRUG_ABUSE_TABLE_NAME, null, values);
    }

    public static void clearDrugAbuseFrequent(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + DrugAbuse.FREQ_DRUG_ABUSE_TABLE_NAME + " WHERE " + DrugAbuse.FREQ_DRUG_ABUSE_USERID + "= " + user_id + " AND " + DrugAbuse.FREQ_DRUG_ABUSE_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<DrugAbuse> getAllDrugAbuseFrequent(Context context, String loginType, int userid) {
        List<DrugAbuse> freqlist = new LinkedList<DrugAbuse>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + DrugAbuse.FREQ_DRUG_ABUSE_TABLE_NAME + " WHERE " + DrugAbuse.FREQ_DRUG_ABUSE_USER_LOGINTYPE + " = '" + loginType + "' and " + DrugAbuse.FREQ_DRUG_ABUSE_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        DrugAbuse allergy = null;
        if (cursor.moveToFirst()) {
            do {
                allergy = new DrugAbuse();
                allergy.setAbuseFrequentId(cursor.getInt(1));
                allergy.setAbuseId(cursor.getInt(2));
                allergy.setAbuseName(cursor.getString(3));
                allergy.setDocId(cursor.getInt(4));
                allergy.setDocType(cursor.getInt(5));
                allergy.setFreqCount(cursor.getInt(6));
                allergy.setUserId(cursor.getInt(7));
                allergy.setLoginType(cursor.getString(8));
                freqlist.add(allergy);
            } while (cursor.moveToNext());
        }
        return freqlist;
    }

    /* End of Drug Abuse Frequent Lists */

    /* Start of Drug Abuse */
    public static long drugAbuseInsert(Context context, int abuse_id, String abuse_name,
                                       int doc_id, int doc_type, int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DrugAbuse.DRUG_ABUSE_ID, abuse_id);
        values.put(DrugAbuse.DRUG_ABUSE_NAME, abuse_name);
        values.put(DrugAbuse.DRUG_ABUSE_DOCID, doc_id);
        values.put(DrugAbuse.DRUG_ABUSE_DOCTYPE, doc_type);
        values.put(DrugAbuse.DRUG_ABUSE_USERID, user_id);
        values.put(DrugAbuse.DRUG_ABUSE_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(DrugAbuse.DRUG_ABUSE_TABLE_NAME, null, values);
    }

    public static void clearDrugAbuse(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + DrugAbuse.DRUG_ABUSE_TABLE_NAME + " WHERE " + DrugAbuse.DRUG_ABUSE_USERID + "= " + user_id + " AND " + DrugAbuse.DRUG_ABUSE_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<DrugAbuse> getAllDrugAbuse(Context context, String loginType, int userid) {
        List<DrugAbuse> freqlist = new LinkedList<DrugAbuse>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + DrugAbuse.DRUG_ABUSE_TABLE_NAME + " WHERE " + DrugAbuse.DRUG_ABUSE_USER_LOGINTYPE + " = '" + loginType + "' and " + DrugAbuse.DRUG_ABUSE_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        DrugAbuse allergy = null;
        if (cursor.moveToFirst()) {
            do {
                allergy = new DrugAbuse();
                allergy.setAbuseId(cursor.getInt(1));
                allergy.setAbuseName(cursor.getString(2));
                allergy.setDocId(cursor.getInt(3));
                allergy.setDocType(cursor.getInt(4));
                allergy.setUserId(cursor.getInt(5));
                allergy.setLoginType(cursor.getString(6));
                freqlist.add(allergy);
            } while (cursor.moveToNext());
        }
        return freqlist;
    }
    /* End of Drug Abuse */

    /* Start of DFamily History Frequent Lists */
    public static long familyHistoryFrequentInsert(Context context, int family_freq_id, int family_id, String family_name,
                                                   int doc_id, int doc_type, int freq_count, int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FamilyHistory.FREQ_FAMILY_HISTORY_FREQID, family_freq_id);
        values.put(FamilyHistory.FREQ_FAMILY_HISTORY_ID, family_id);
        values.put(FamilyHistory.FREQ_FAMILY_HISTORY_NAME, family_name);
        values.put(FamilyHistory.FREQ_FAMILY_HISTORY_DOCID, doc_id);
        values.put(FamilyHistory.FREQ_FAMILY_HISTORY_DOCTYPE, doc_type);
        values.put(FamilyHistory.FREQ_FAMILY_HISTORY_FREQ_COUNT, freq_count);
        values.put(FamilyHistory.FREQ_FAMILY_HISTORY_USERID, user_id);
        values.put(FamilyHistory.FREQ_FAMILY_HISTORY_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(FamilyHistory.FREQ_FAMILY_HISTORY_TABLE_NAME, null, values);
    }

    public static void clearFamilyHistoryFrequent(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + FamilyHistory.FREQ_FAMILY_HISTORY_TABLE_NAME + " WHERE " + FamilyHistory.FREQ_FAMILY_HISTORY_USERID + "= " + user_id + " AND " + FamilyHistory.FREQ_FAMILY_HISTORY_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<FamilyHistory> getAllFamilyHistoryFrequent(Context context, String loginType, int userid) {
        List<FamilyHistory> freqlist = new LinkedList<FamilyHistory>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + FamilyHistory.FREQ_FAMILY_HISTORY_TABLE_NAME + " WHERE " + FamilyHistory.FREQ_FAMILY_HISTORY_USER_LOGINTYPE + " = '" + loginType + "' and " + FamilyHistory.FREQ_FAMILY_HISTORY_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        FamilyHistory flyhist = null;
        if (cursor.moveToFirst()) {
            do {
                flyhist = new FamilyHistory();
                flyhist.setFamilyFrequentId(cursor.getInt(1));
                flyhist.setFamilyHistoryId(cursor.getInt(2));
                flyhist.setFamilyHistoryName(cursor.getString(3));
                flyhist.setDocId(cursor.getInt(4));
                flyhist.setDocType(cursor.getInt(5));
                flyhist.setFreqCount(cursor.getInt(6));
                flyhist.setUserId(cursor.getInt(7));
                flyhist.setLoginType(cursor.getString(8));
                freqlist.add(flyhist);
            } while (cursor.moveToNext());
        }
        return freqlist;
    }
    /* End of DFamily History Frequent Lists */

    /* Start of Family History */
    public static long familyHistoryInsert(Context context, int history_id, String history_name,
                                           int doc_id, int doc_type, int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FamilyHistory.FAMILY_HISTORY_ID, history_id);
        values.put(FamilyHistory.FAMILY_HISTORY_NAME, history_name);
        values.put(FamilyHistory.FAMILY_HISTORY_DOCID, doc_id);
        values.put(FamilyHistory.FAMILY_HISTORY_DOCTYPE, doc_type);
        values.put(FamilyHistory.FAMILY_HISTORY_USERID, user_id);
        values.put(FamilyHistory.FAMILY_HISTORY_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(FamilyHistory.FAMILY_HISTORY_TABLE_NAME, null, values);
    }

    public static void clearFamilyHistory(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + FamilyHistory.FAMILY_HISTORY_TABLE_NAME + " WHERE " + FamilyHistory.FAMILY_HISTORY_USERID + "= " + user_id + " AND " + FamilyHistory.FAMILY_HISTORY_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<FamilyHistory> getAllFamilyHistory(Context context, String loginType, int userid) {
        List<FamilyHistory> freqlist = new LinkedList<FamilyHistory>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + FamilyHistory.FAMILY_HISTORY_TABLE_NAME + " WHERE " + FamilyHistory.FAMILY_HISTORY_USER_LOGINTYPE + " = '" + loginType + "' and " + FamilyHistory.FAMILY_HISTORY_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        FamilyHistory history = null;
        if (cursor.moveToFirst()) {
            do {
                history = new FamilyHistory();
                history.setFamilyHistoryId(cursor.getInt(1));
                history.setFamilyHistoryName(cursor.getString(2));
                history.setDocId(cursor.getInt(3));
                history.setDocType(cursor.getInt(4));
                history.setUserId(cursor.getInt(5));
                history.setLoginType(cursor.getString(6));
                freqlist.add(history);
            } while (cursor.moveToNext());
        }
        return freqlist;
    }
    /* End of Family History  */

    /* Start Ophthal Lids */
    public static long lidsInsert(Context context, int lids_id, String lids_name,
                                  int doc_id, int doc_type, String left_eye, String right_eye,
                                  int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Lids.LIDS_ID, lids_id);
        values.put(Lids.LIDS_NAME, lids_name);
        values.put(Lids.LIDS_DOCID, doc_id);
        values.put(Lids.LIDS_DOCTYPE, doc_type);
        values.put(Lids.LIDS_LEFT_EYE, left_eye);
        values.put(Lids.LIDS_RIGHT_EYE, right_eye);
        values.put(Lids.LIDS_USERID, user_id);
        values.put(Lids.LIDS_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(Lids.LIDS_TABLE_NAME, null, values);
    }

    public static void clearLids(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + Lids.LIDS_TABLE_NAME + " WHERE " + Lids.LIDS_USERID + "= " + user_id + " AND " + Lids.LIDS_USER_LOGINTYPE + " = " + loginType);
    }

    // Get All lids
    public static List<Lids> getAllLids(Context context, String loginType, int userid) {
        List<Lids> complist = new LinkedList<Lids>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + Lids.LIDS_TABLE_NAME + " WHERE " + Lids.LIDS_USER_LOGINTYPE + " = '" + loginType + "' and " + Lids.LIDS_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        Lids complaints = null;
        if (cursor.moveToFirst()) {
            do {
                complaints = new Lids();
                complaints.setLidsId(cursor.getInt(1));
                complaints.setLidsName(cursor.getString(2));
                complaints.setDocId(cursor.getInt(3));
                complaints.setDocType(cursor.getInt(4));
                complaints.setLeftEye(cursor.getString(5));
                complaints.setRightEye(cursor.getString(6));
                complaints.setUserId(cursor.getInt(7));
                complaints.setLoginType(cursor.getString(8));
                complist.add(complaints);
            } while (cursor.moveToNext());
        }
        return complist;
    }
    /* End of Ophthal Lids */

    /* Start Ophthal Conjuctiva */
    public static long conjuctivaInsert(Context context, int conjuctiva_id, String conjuctiva_name,
                                        int doc_id, int doc_type, String left_eye, String right_eye,
                                        int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OphthalConjuctiva.CONJUCTIVA_ID, conjuctiva_id);
        values.put(OphthalConjuctiva.CONJUCTIVA_NAME, conjuctiva_name);
        values.put(OphthalConjuctiva.CONJUCTIVA_DOCID, doc_id);
        values.put(OphthalConjuctiva.CONJUCTIVA_DOCTYPE, doc_type);
        values.put(OphthalConjuctiva.CONJUCTIVA_LEFT_EYE, left_eye);
        values.put(OphthalConjuctiva.CONJUCTIVA_RIGHT_EYE, right_eye);
        values.put(OphthalConjuctiva.CONJUCTIVA_USERID, user_id);
        values.put(OphthalConjuctiva.CONJUCTIVA_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(OphthalConjuctiva.CONJUCTIVA_TABLE_NAME, null, values);
    }

    public static void clearConjuctiva(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + OphthalConjuctiva.CONJUCTIVA_TABLE_NAME + " WHERE " + OphthalConjuctiva.CONJUCTIVA_USERID + "= " + user_id + " AND " + OphthalConjuctiva.CONJUCTIVA_USER_LOGINTYPE + " = " + loginType);
    }

    // Get All Conjuctiva
    public static List<OphthalConjuctiva> getAllConjuctiva(Context context, String loginType, int userid) {
        List<OphthalConjuctiva> conjlist = new LinkedList<OphthalConjuctiva>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + OphthalConjuctiva.CONJUCTIVA_TABLE_NAME + " WHERE " + OphthalConjuctiva.CONJUCTIVA_USER_LOGINTYPE + " = '" + loginType + "' and " + OphthalConjuctiva.CONJUCTIVA_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        OphthalConjuctiva conj = null;
        if (cursor.moveToFirst()) {
            do {
                conj = new OphthalConjuctiva();
                conj.setConjuctivaId(cursor.getInt(1));
                conj.setConjuctivaName(cursor.getString(2));
                conj.setDocId(cursor.getInt(3));
                conj.setDocType(cursor.getInt(4));
                conj.setLeftEye(cursor.getString(5));
                conj.setRightEye(cursor.getString(6));
                conj.setUserId(cursor.getInt(7));
                conj.setLoginType(cursor.getString(8));
                conjlist.add(conj);
            } while (cursor.moveToNext());
        }
        return conjlist;
    }
    /* End of Ophthal Conjuctiva */

    /* Start Ophthal Sclera */
    public static long scleraInsert(Context context, int sclera_id, String sclera_name,
                                    int doc_id, int doc_type, String left_eye, String right_eye,
                                    int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OphthalSclera.SCLERA_ID, sclera_id);
        values.put(OphthalSclera.SCLERA_NAME, sclera_name);
        values.put(OphthalSclera.SCLERA_DOCID, doc_id);
        values.put(OphthalSclera.SCLERA_DOCTYPE, doc_type);
        values.put(OphthalSclera.SCLERA_LEFT_EYE, left_eye);
        values.put(OphthalSclera.SCLERA_RIGHT_EYE, right_eye);
        values.put(OphthalSclera.SCLERA_USERID, user_id);
        values.put(OphthalSclera.SCLERA_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(OphthalSclera.SCLERA_TABLE_NAME, null, values);
    }

    public static void clearSclera(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + OphthalSclera.SCLERA_TABLE_NAME + " WHERE " + OphthalSclera.SCLERA_USERID + "= " + user_id + " AND " + OphthalSclera.SCLERA_USER_LOGINTYPE + " = " + loginType);
    }


    // Get All Sclera
    public static List<OphthalSclera> getAllSclera(Context context, String loginType, int userid) {
        List<OphthalSclera> scleralist = new LinkedList<OphthalSclera>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + OphthalSclera.SCLERA_TABLE_NAME + " WHERE " + OphthalSclera.SCLERA_USER_LOGINTYPE + " = '" + loginType + "' and " + OphthalSclera.SCLERA_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        OphthalSclera scle = null;
        if (cursor.moveToFirst()) {
            do {
                scle = new OphthalSclera();
                scle.setScleraId(cursor.getInt(1));
                scle.setScleraName(cursor.getString(2));
                scle.setDocId(cursor.getInt(3));
                scle.setDocType(cursor.getInt(4));
                scle.setLeftEye(cursor.getString(5));
                scle.setRightEye(cursor.getString(6));
                scle.setUserId(cursor.getInt(7));
                scle.setLoginType(cursor.getString(8));
                scleralist.add(scle);
            } while (cursor.moveToNext());
        }
        return scleralist;
    }
    /* End of Ophthal Sclera */

    /* Start Ophthal Cornea Anterior */
    public static long corneaAnteriorInsert(Context context, int cornea_ant_id, String cornea_ant_name,
                                            int doc_id, int doc_type, String left_eye, String right_eye,
                                            int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_ID, cornea_ant_id);
        values.put(OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_NAME, cornea_ant_name);
        values.put(OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_DOCID, doc_id);
        values.put(OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_DOCTYPE, doc_type);
        values.put(OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_LEFT_EYE, left_eye);
        values.put(OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_RIGHT_EYE, right_eye);
        values.put(OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_USERID, user_id);
        values.put(OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_TABLE_NAME, null, values);
    }

    public static void clearCorneaAnterior(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_TABLE_NAME + " WHERE " + OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_USERID + "= " + user_id + " AND " + OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<OphthalCornearAnteriorSurface> getAllCorneaAnterior(Context context, String loginType, int userid) {
        List<OphthalCornearAnteriorSurface> antlist = new LinkedList<OphthalCornearAnteriorSurface>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_TABLE_NAME + " WHERE " + OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_USER_LOGINTYPE + " = '" + loginType + "' and " + OphthalCornearAnteriorSurface.CORNEA_ANTERIOR_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        OphthalCornearAnteriorSurface anterior = null;
        if (cursor.moveToFirst()) {
            do {
                anterior = new OphthalCornearAnteriorSurface();
                anterior.setCorneaAnteriorId(cursor.getInt(1));
                anterior.setCorneaAnteriorName(cursor.getString(2));
                anterior.setDocId(cursor.getInt(3));
                anterior.setDocType(cursor.getInt(4));
                anterior.setLeftEye(cursor.getString(5));
                anterior.setRightEye(cursor.getString(6));
                anterior.setUserId(cursor.getInt(7));
                anterior.setLoginType(cursor.getString(8));
                antlist.add(anterior);
            } while (cursor.moveToNext());
        }
        return antlist;
    }
    /* End Ophthal Cornea Anterior */

    /* Start Ophthal Cornea Posterior Surface */
    public static long corneaPosteriorInsert(Context context, int cornea_post_id, String cornea_post_name,
                                             int doc_id, int doc_type, String left_eye, String right_eye,
                                             int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_ID, cornea_post_id);
        values.put(OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_NAME, cornea_post_name);
        values.put(OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_DOCID, doc_id);
        values.put(OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_DOCTYPE, doc_type);
        values.put(OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_LEFT_EYE, left_eye);
        values.put(OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_RIGHT_EYE, right_eye);
        values.put(OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_USERID, user_id);
        values.put(OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_TABLE_NAME, null, values);
    }

    public static void clearCorneaPosterior(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_TABLE_NAME + " WHERE " + OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_USERID + "= " + user_id + " AND " + OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<OphthalCornearPosteriorSurface> getAllCorneaPosterior(Context context, String loginType, int userid) {
        List<OphthalCornearPosteriorSurface> postlist = new LinkedList<OphthalCornearPosteriorSurface>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_TABLE_NAME + " WHERE " + OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_USER_LOGINTYPE + " = '" + loginType + "' and " + OphthalCornearPosteriorSurface.CORNEA_POSTERIOR_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        OphthalCornearPosteriorSurface psterior = null;
        if (cursor.moveToFirst()) {
            do {
                psterior = new OphthalCornearPosteriorSurface();
                psterior.setCorneaPosteriorId(cursor.getInt(1));
                psterior.setCorneaPosteriorName(cursor.getString(2));
                psterior.setDocId(cursor.getInt(3));
                psterior.setDocType(cursor.getInt(4));
                psterior.setLeftEye(cursor.getString(5));
                psterior.setRightEye(cursor.getString(6));
                psterior.setUserId(cursor.getInt(7));
                psterior.setLoginType(cursor.getString(8));
                postlist.add(psterior);
            } while (cursor.moveToNext());
        }
        return postlist;
    }
    /* End of Ophthal Cornea Posterior Surface */

    /* Start Ophthal Anterior Chamber */
    public static long anteriorChamberInsert(Context context, int chamber_id, String chamber_name,
                                             int doc_id, int doc_type, String left_eye, String right_eye,
                                             int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OphthalAnteriorChamber.ANTERIOR_CHAMBER_ID, chamber_id);
        values.put(OphthalAnteriorChamber.ANTERIOR_CHAMBER_NAME, chamber_name);
        values.put(OphthalAnteriorChamber.ANTERIOR_CHAMBER_DOCID, doc_id);
        values.put(OphthalAnteriorChamber.ANTERIOR_CHAMBER_DOCTYPE, doc_type);
        values.put(OphthalAnteriorChamber.ANTERIOR_CHAMBER_LEFT_EYE, left_eye);
        values.put(OphthalAnteriorChamber.ANTERIOR_CHAMBER_RIGHT_EYE, right_eye);
        values.put(OphthalAnteriorChamber.ANTERIOR_CHAMBER_USERID, user_id);
        values.put(OphthalAnteriorChamber.ANTERIOR_CHAMBER_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(OphthalAnteriorChamber.ANTERIOR_CHAMBER_TABLE_NAME, null, values);
    }

    public static void clearAnteriorChamber(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + OphthalAnteriorChamber.ANTERIOR_CHAMBER_TABLE_NAME + " WHERE " + OphthalAnteriorChamber.ANTERIOR_CHAMBER_USERID + "= " + user_id + " AND " + OphthalAnteriorChamber.ANTERIOR_CHAMBER_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<OphthalAnteriorChamber> getAllAnteriorChamber(Context context, String loginType, int userid) {
        List<OphthalAnteriorChamber> chmaberlist = new LinkedList<OphthalAnteriorChamber>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + OphthalAnteriorChamber.ANTERIOR_CHAMBER_TABLE_NAME + " WHERE " + OphthalAnteriorChamber.ANTERIOR_CHAMBER_USER_LOGINTYPE + " = '" + loginType + "' and " + OphthalAnteriorChamber.ANTERIOR_CHAMBER_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        OphthalAnteriorChamber chambers = null;
        if (cursor.moveToFirst()) {
            do {
                chambers = new OphthalAnteriorChamber();
                chambers.setAnteriorChamberId(cursor.getInt(1));
                chambers.setAnteriorChamberName(cursor.getString(2));
                chambers.setDocId(cursor.getInt(3));
                chambers.setDocType(cursor.getInt(4));
                chambers.setLeftEye(cursor.getString(5));
                chambers.setRightEye(cursor.getString(6));
                chambers.setUserId(cursor.getInt(7));
                chambers.setLoginType(cursor.getString(8));
                chmaberlist.add(chambers);
            } while (cursor.moveToNext());
        }
        return chmaberlist;
    }
    /* End of Ophthal Anterior Chamber */

    /* Start Ophthal Iris */
    public static long irisInsert(Context context, int iris_id, String iris_name,
                                  int doc_id, int doc_type, String left_eye, String right_eye,
                                  int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OphthalIris.IRIS_ID, iris_id);
        values.put(OphthalIris.IRIS_NAME, iris_name);
        values.put(OphthalIris.IRIS_DOCID, doc_id);
        values.put(OphthalIris.IRIS_DOCTYPE, doc_type);
        values.put(OphthalIris.IRIS_LEFT_EYE, left_eye);
        values.put(OphthalIris.IRIS_RIGHT_EYE, right_eye);
        values.put(OphthalIris.IRIS_USERID, user_id);
        values.put(OphthalIris.IRIS_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(OphthalIris.IRIS_TABLE_NAME, null, values);
    }

    public static void clearIris(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + OphthalIris.IRIS_TABLE_NAME + " WHERE " + OphthalIris.IRIS_USERID + "= " + user_id + " AND " + OphthalIris.IRIS_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<OphthalIris> getAllIris(Context context, String loginType, int userid) {
        List<OphthalIris> irislist = new LinkedList<OphthalIris>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + OphthalIris.IRIS_TABLE_NAME + " WHERE " + OphthalIris.IRIS_USER_LOGINTYPE + " = '" + loginType + "' and " + OphthalIris.IRIS_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        OphthalIris iris = null;
        if (cursor.moveToFirst()) {
            do {
                iris = new OphthalIris();
                iris.setIrisId(cursor.getInt(1));
                iris.setIrisName(cursor.getString(2));
                iris.setDocId(cursor.getInt(3));
                iris.setDocType(cursor.getInt(4));
                iris.setLeftEye(cursor.getString(5));
                iris.setRightEye(cursor.getString(6));
                iris.setUserId(cursor.getInt(7));
                iris.setLoginType(cursor.getString(8));
                irislist.add(iris);
            } while (cursor.moveToNext());
        }
        return irislist;
    }
    /* End of Ophthal Iris */

    /* Start Ophthal Pupil */
    public static long pupilInsert(Context context, int pupil_id, String pupil_name,
                                   int doc_id, int doc_type, String left_eye, String right_eye,
                                   int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OphthalPupil.PUPIL_ID, pupil_id);
        values.put(OphthalPupil.PUPIL_NAME, pupil_name);
        values.put(OphthalPupil.PUPIL_DOCID, doc_id);
        values.put(OphthalPupil.PUPIL_DOCTYPE, doc_type);
        values.put(OphthalPupil.PUPIL_LEFT_EYE, left_eye);
        values.put(OphthalPupil.PUPIL_RIGHT_EYE, right_eye);
        values.put(OphthalPupil.PUPIL_USERID, user_id);
        values.put(OphthalPupil.PUPIL_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(OphthalPupil.PUPIL_TABLE_NAME, null, values);
    }

    public static void clearPupil(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + OphthalPupil.PUPIL_TABLE_NAME + " WHERE " + OphthalPupil.PUPIL_USERID + "= " + user_id + " AND " + OphthalPupil.PUPIL_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<OphthalPupil> getAllPupil(Context context, String loginType, int userid) {
        List<OphthalPupil> pupillist = new LinkedList<OphthalPupil>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + OphthalPupil.PUPIL_TABLE_NAME + " WHERE " + OphthalPupil.PUPIL_USER_LOGINTYPE + " = '" + loginType + "' and " + OphthalPupil.PUPIL_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        OphthalPupil pupil = null;
        if (cursor.moveToFirst()) {
            do {
                pupil = new OphthalPupil();
                pupil.setPupilId(cursor.getInt(1));
                pupil.setPupilName(cursor.getString(2));
                pupil.setDocId(cursor.getInt(3));
                pupil.setDocType(cursor.getInt(4));
                pupil.setLeftEye(cursor.getString(5));
                pupil.setRightEye(cursor.getString(6));
                pupil.setUserId(cursor.getInt(7));
                pupil.setLoginType(cursor.getString(8));
                pupillist.add(pupil);
            } while (cursor.moveToNext());
        }
        return pupillist;
    }
    /* End of Ophthal Pupil */

    /* Start Ophthal Angle of Anterior Chamber */
    public static long angleInsert(Context context, int angle_id, String angle_name,
                                   int doc_id, int doc_type, String left_eye, String right_eye,
                                   int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OphthalAngleAnteriorChamber.ANGLE_ID, angle_id);
        values.put(OphthalAngleAnteriorChamber.ANGLE_NAME, angle_name);
        values.put(OphthalAngleAnteriorChamber.ANGLE_DOCID, doc_id);
        values.put(OphthalAngleAnteriorChamber.ANGLE_DOCTYPE, doc_type);
        values.put(OphthalAngleAnteriorChamber.ANGLE_LEFT_EYE, left_eye);
        values.put(OphthalAngleAnteriorChamber.ANGLE_RIGHT_EYE, right_eye);
        values.put(OphthalAngleAnteriorChamber.ANGLE_USERID, user_id);
        values.put(OphthalAngleAnteriorChamber.ANGLE_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(OphthalAngleAnteriorChamber.ANGLE_TABLE_NAME, null, values);
    }

    public static void clearAngle(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + OphthalAngleAnteriorChamber.ANGLE_TABLE_NAME + " WHERE " + OphthalAngleAnteriorChamber.ANGLE_USERID + "= " + user_id + " AND " + OphthalAngleAnteriorChamber.ANGLE_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<OphthalAngleAnteriorChamber> getAllAngle(Context context, String loginType, int userid) {
        List<OphthalAngleAnteriorChamber> anglelist = new LinkedList<OphthalAngleAnteriorChamber>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + OphthalAngleAnteriorChamber.ANGLE_TABLE_NAME + " WHERE " + OphthalAngleAnteriorChamber.ANGLE_USER_LOGINTYPE + " = '" + loginType + "' and " + OphthalAngleAnteriorChamber.ANGLE_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        OphthalAngleAnteriorChamber angle = null;
        if (cursor.moveToFirst()) {
            do {
                angle = new OphthalAngleAnteriorChamber();
                angle.setAngleId(cursor.getInt(1));
                angle.setAngleName(cursor.getString(2));
                angle.setDocId(cursor.getInt(3));
                angle.setDocType(cursor.getInt(4));
                angle.setLeftEye(cursor.getString(5));
                angle.setRightEye(cursor.getString(6));
                angle.setUserId(cursor.getInt(7));
                angle.setLoginType(cursor.getString(8));
                anglelist.add(angle);
            } while (cursor.moveToNext());
        }
        return anglelist;
    }
    /* End of Ophthal Angle of Anterior Chamber  */

    /* Start Ophthal Lens */
    public static long lensInsert(Context context, int lens_id, String lens_name,
                                  int doc_id, int doc_type, String left_eye, String right_eye,
                                  int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OphthalLens.LENS_ID, lens_id);
        values.put(OphthalLens.LENS_NAME, lens_name);
        values.put(OphthalLens.LENS_DOCID, doc_id);
        values.put(OphthalLens.LENS_DOCTYPE, doc_type);
        values.put(OphthalLens.LENS_LEFT_EYE, left_eye);
        values.put(OphthalLens.LENS_RIGHT_EYE, right_eye);
        values.put(OphthalLens.LENS_USERID, user_id);
        values.put(OphthalLens.LENS_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(OphthalLens.LENS_TABLE_NAME, null, values);
    }

    public static void clearLens(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + OphthalLens.LENS_TABLE_NAME + " WHERE " + OphthalLens.LENS_USERID + "= " + user_id + " AND " + OphthalLens.LENS_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<OphthalLens> getAllLens(Context context, String loginType, int userid) {
        List<OphthalLens> lenslist = new LinkedList<OphthalLens>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + OphthalLens.LENS_TABLE_NAME + " WHERE " + OphthalLens.LENS_USER_LOGINTYPE + " = '" + loginType + "' and " + OphthalLens.LENS_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        OphthalLens lens = null;
        if (cursor.moveToFirst()) {
            do {
                lens = new OphthalLens();
                lens.setLensId(cursor.getInt(1));
                lens.setLensName(cursor.getString(2));
                lens.setDocId(cursor.getInt(3));
                lens.setDocType(cursor.getInt(4));
                lens.setLeftEye(cursor.getString(5));
                lens.setRightEye(cursor.getString(6));
                lens.setUserId(cursor.getInt(7));
                lens.setLoginType(cursor.getString(8));
                lenslist.add(lens);
            } while (cursor.moveToNext());
        }
        return lenslist;
    }
    /* End of Ophthal Lens */

    /* Start Ophthal Viterous */
    public static long viterousnsert(Context context, int viterous_id, String viterous_name,
                                     int doc_id, int doc_type, String left_eye, String right_eye,
                                     int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OphthalViterous.VITEROUS_ID, viterous_id);
        values.put(OphthalViterous.VITEROUS_NAME, viterous_name);
        values.put(OphthalViterous.VITEROUS_DOCID, doc_id);
        values.put(OphthalViterous.VITEROUS_DOCTYPE, doc_type);
        values.put(OphthalViterous.VITEROUS_LEFT_EYE, left_eye);
        values.put(OphthalViterous.VITEROUS_RIGHT_EYE, right_eye);
        values.put(OphthalViterous.VITEROUS_USERID, user_id);
        values.put(OphthalViterous.VITEROUS_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(OphthalViterous.VITEROUS_TABLE_NAME, null, values);
    }

    public static void clearViterous(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + OphthalViterous.VITEROUS_TABLE_NAME + " WHERE " + OphthalViterous.VITEROUS_USERID + "= " + user_id + " AND " + OphthalViterous.VITEROUS_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<OphthalViterous> getAllViterous(Context context, String loginType, int userid) {
        List<OphthalViterous> viterouslist = new LinkedList<OphthalViterous>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + OphthalViterous.VITEROUS_TABLE_NAME + " WHERE " + OphthalViterous.VITEROUS_USER_LOGINTYPE + " = '" + loginType + "' and " + OphthalViterous.VITEROUS_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        OphthalViterous viterous = null;
        if (cursor.moveToFirst()) {
            do {
                viterous = new OphthalViterous();
                viterous.setViterousId(cursor.getInt(1));
                viterous.setViterousName(cursor.getString(2));
                viterous.setDocId(cursor.getInt(3));
                viterous.setDocType(cursor.getInt(4));
                viterous.setLeftEye(cursor.getString(5));
                viterous.setRightEye(cursor.getString(6));
                viterous.setUserId(cursor.getInt(7));
                viterous.setLoginType(cursor.getString(8));
                viterouslist.add(viterous);
            } while (cursor.moveToNext());
        }
        return viterouslist;
    }
    /* End of Ophthal Viterous */

    /* Start Ophthal Fundus */
    public static long fundusInsert(Context context, int fundus_id, String fundus_name,
                                    int doc_id, int doc_type, String left_eye, String right_eye,
                                    int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OphthalFundus.FUNDUS_ID, fundus_id);
        values.put(OphthalFundus.FUNDUS_NAME, fundus_name);
        values.put(OphthalFundus.FUNDUS_DOCID, doc_id);
        values.put(OphthalFundus.FUNDUS_DOCTYPE, doc_type);
        values.put(OphthalFundus.FUNDUS_LEFT_EYE, left_eye);
        values.put(OphthalFundus.FUNDUS_RIGHT_EYE, right_eye);
        values.put(OphthalFundus.FUNDUS_USERID, user_id);
        values.put(OphthalFundus.FUNDUS_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(OphthalFundus.FUNDUS_TABLE_NAME, null, values);
    }

    public static void clearFundus(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + OphthalFundus.FUNDUS_TABLE_NAME + " WHERE " + OphthalFundus.FUNDUS_USERID + "= " + user_id + " AND " + OphthalFundus.FUNDUS_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<OphthalFundus> getAllFundus(Context context, String loginType, int userid) {
        List<OphthalFundus> scleralist = new LinkedList<OphthalFundus>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + OphthalFundus.FUNDUS_TABLE_NAME + " WHERE " + OphthalFundus.FUNDUS_USER_LOGINTYPE + " = '" + loginType + "' and " + OphthalFundus.FUNDUS_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        OphthalFundus scle = null;
        if (cursor.moveToFirst()) {
            do {
                scle = new OphthalFundus();
                scle.setFundusId(cursor.getInt(1));
                scle.setFundusName(cursor.getString(2));
                scle.setDocId(cursor.getInt(3));
                scle.setDocType(cursor.getInt(4));
                scle.setLeftEye(cursor.getString(5));
                scle.setRightEye(cursor.getString(6));
                scle.setUserId(cursor.getInt(7));
                scle.setLoginType(cursor.getString(8));
                scleralist.add(scle);
            } while (cursor.moveToNext());
        }
        return scleralist;
    }
    /* End of Ophthal Fundus */

    /* Start Investigation Templates */
    public static long investigationTemplatesInsert(Context context, int template_id,  int doc_id, int doc_type,
                                                    int default_visibility, String template_name, String test_id,
                                                    String group_test_id, String test_name, int department, String normal_value,
                                                    String actual_value, String right_eye, String left_eye, int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InvestigationTemplates.INVESTIGATION_TEMPLATE_ID, template_id);
        values.put(InvestigationTemplates.INVESTIGATION_TEMPLATE_NAME, template_name);
        values.put(InvestigationTemplates.INVESTIGATION_TEMPLATE_DEFAULT_VISIBLE, default_visibility);
        values.put(InvestigationTemplates.INVESTIGATION_TEMPLATE_TEST_ID, test_id);
        values.put(InvestigationTemplates.INVESTIGATION_TEMPLATE_GROUP_TEST_ID, group_test_id);
        values.put(InvestigationTemplates.INVESTIGATION_TEMPLATE_TEST_NAME, test_name);
        values.put(InvestigationTemplates.INVESTIGATION_TEMPLATE_TEST_DEPARTMENT, department);
        values.put(InvestigationTemplates.INVESTIGATION_TEMPLATE_NORMAL_VALUE, normal_value);
        values.put(InvestigationTemplates.INVESTIGATION_TEMPLATE_ACTUAL_VALUE, actual_value);
        values.put(InvestigationTemplates.INVESTIGATION_TEMPLATE_RIGHT_EYE, right_eye);
        values.put(InvestigationTemplates.INVESTIGATION_TEMPLATE_LEFT_EYE, left_eye);
        values.put(InvestigationTemplates.INVESTIGATION_TEMPLATE_DOCID, doc_id);
        values.put(InvestigationTemplates.INVESTIGATION_TEMPLATE_DOCTYPE, doc_type);
        values.put(InvestigationTemplates.INVESTIGATION_TEMPLATE_USERID, user_id);
        values.put(InvestigationTemplates.INVESTIGATION_TEMPLATE_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(InvestigationTemplates.INVESTIGATION_TEMPLATE_TABLE_NAME,null, values);
    }

    public static void clearInvestigationTemplates(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM "+ InvestigationTemplates.INVESTIGATION_TEMPLATE_TABLE_NAME + " WHERE "+ InvestigationTemplates.INVESTIGATION_TEMPLATE_USERID +"= "+user_id+" AND " + InvestigationTemplates.INVESTIGATION_TEMPLATE_USER_LOGINTYPE +" = "+loginType);
    }

    public static List<InvestigationTemplates> getAllInvestigationTemplates(Context context, String loginType, int userid) {
        List<InvestigationTemplates> investtemplist = new LinkedList<InvestigationTemplates>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " +InvestigationTemplates.INVESTIGATION_TEMPLATE_TABLE_NAME+ " WHERE "+InvestigationTemplates.INVESTIGATION_TEMPLATE_USER_LOGINTYPE+" = '"+loginType+"' and "+InvestigationTemplates.INVESTIGATION_TEMPLATE_USERID+ " = '" + userid+"'";
        Cursor cursor = writabledb.rawQuery(query, null);

        InvestigationTemplates investTemp = null;
        if (cursor.moveToFirst()) {
            do {
                investTemp = new InvestigationTemplates();
                investTemp.setTemplateID(cursor.getInt(1));
                investTemp.setTemplateName(cursor.getString(2));
                investTemp.setTemplateDefaultVisible(cursor.getInt(3));
                investTemp.setTemplateTestID(cursor.getString(4));
                investTemp.setTemplateGroupTestID(cursor.getString(5));
                investTemp.setTemplateTestName(cursor.getString(6));
                investTemp.setTemplateTestDepartment(cursor.getInt(7));
                investTemp.setTemplateNormalValue(cursor.getString(8));
                investTemp.setTemplateActualValue(cursor.getString(9));
                investTemp.setTemplateRightEye(cursor.getString(10));
                investTemp.setTemplateLeftEye(cursor.getString(11));
                investTemp.setTemplateDocID(cursor.getInt(12));
                investTemp.setTemplateDocType(cursor.getInt(13));
                investTemp.setTemplateUserID(cursor.getInt(14));
                investTemp.setTemplateLoginType(cursor.getString(15));
                investtemplist.add(investTemp);
            } while (cursor.moveToNext());
        }
        return investtemplist;
    }

    public static List<InvestigationTemplates> getInvestigationTemplateNames(Context context, String loginType, int userid) {
        List<InvestigationTemplates> examtemplist = new LinkedList<InvestigationTemplates>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT DISTINCT "+ InvestigationTemplates.INVESTIGATION_TEMPLATE_ID +","+ InvestigationTemplates.INVESTIGATION_TEMPLATE_NAME +","+ InvestigationTemplates.INVESTIGATION_TEMPLATE_DEFAULT_VISIBLE + " FROM " +InvestigationTemplates.INVESTIGATION_TEMPLATE_TABLE_NAME+ " WHERE "+InvestigationTemplates.INVESTIGATION_TEMPLATE_USER_LOGINTYPE+" = '"+loginType+"' and "+InvestigationTemplates.INVESTIGATION_TEMPLATE_USERID+ " = '" + userid+"'";
        // Log.d(Utils.TAG, " query: " + query);
        Cursor cursor = writabledb.rawQuery(query, null);

        InvestigationTemplates examinationTemp = null;
        if (cursor.moveToFirst()) {
            do {
                examinationTemp = new InvestigationTemplates();
                examinationTemp.setTemplateID(cursor.getInt(0));
                examinationTemp.setTemplateName(cursor.getString(1));
                examinationTemp.setTemplateDefaultVisible(cursor.getInt(2));
                examtemplist.add(examinationTemp);
            } while (cursor.moveToNext());
        }
        return examtemplist;
    }

    public static List<InvestigationTemplates> getInvestigationTemplateLists(Context context, String loginType, int userid, int templateID) {
        List<InvestigationTemplates> investtemplist = new LinkedList<InvestigationTemplates>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " +InvestigationTemplates.INVESTIGATION_TEMPLATE_TABLE_NAME+ " WHERE "+InvestigationTemplates.INVESTIGATION_TEMPLATE_USER_LOGINTYPE+" = '"+loginType+"' and "+InvestigationTemplates.INVESTIGATION_TEMPLATE_USERID+ " = '" + userid+"'"+" and "+InvestigationTemplates.INVESTIGATION_TEMPLATE_ID+ " = '" + templateID+"'";
        Cursor cursor = writabledb.rawQuery(query, null);

        InvestigationTemplates investTemp = null;
        if (cursor.moveToFirst()) {
            do {
                investTemp = new InvestigationTemplates();
                investTemp.setTemplateID(cursor.getInt(1));
                investTemp.setTemplateName(cursor.getString(2));
                investTemp.setTemplateDefaultVisible(cursor.getInt(3));
                investTemp.setTemplateTestID(cursor.getString(4));
                investTemp.setTemplateGroupTestID(cursor.getString(5));
                investTemp.setTemplateTestName(cursor.getString(6));
                investTemp.setTemplateTestDepartment(cursor.getInt(7));
                investTemp.setTemplateNormalValue(cursor.getString(8));
                investTemp.setTemplateActualValue(cursor.getString(9));
                investTemp.setTemplateRightEye(cursor.getString(10));
                investTemp.setTemplateLeftEye(cursor.getString(11));
                investTemp.setTemplateDocID(cursor.getInt(12));
                investTemp.setTemplateDocType(cursor.getInt(13));
                investTemp.setTemplateUserID(cursor.getInt(14));
                investTemp.setTemplateLoginType(cursor.getString(15));
                investtemplist.add(investTemp);
            } while (cursor.moveToNext());
        }
        return investtemplist;
    }
    /* End of Investigations Templates */

    /* Start Patient Education */
    public static long patientEducationInsert(Context context, int education_id, String edu_title, String edu_description,
                                              int doc_id, int doc_type, int user_id, String login_usertype) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PatientEducation.EDUCATION_ID, education_id);
        values.put(PatientEducation.EDUCATION_TITLE, edu_title);
        values.put(PatientEducation.EDUCATION_DESCRIPTION, edu_description);
        values.put(PatientEducation.EDUCATION_DOCID, doc_id);
        values.put(PatientEducation.EDUCATION_DOCTYPE, doc_type);
        values.put(PatientEducation.EDUCATION_USERID, user_id);
        values.put(PatientEducation.EDUCATION_USER_LOGINTYPE, login_usertype);

        return writabledb.insert(PatientEducation.EDUCATION_TEMPLATE_TABLE_NAME, null, values);
    }

    public static void clearPatientEducation(Context context, int user_id, String loginType) {
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        writabledb.execSQL("DELETE FROM " + PatientEducation.EDUCATION_TEMPLATE_TABLE_NAME + " WHERE " + PatientEducation.EDUCATION_USERID + "= " + user_id + " AND " + PatientEducation.EDUCATION_USER_LOGINTYPE + " = " + loginType);
    }

    public static List<PatientEducation> getAllPatientEducation(Context context, String loginType, int userid) {
        List<PatientEducation> edulist = new LinkedList<PatientEducation>();
        getDB(context);
        SQLiteDatabase writabledb = mDBHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + PatientEducation.EDUCATION_TEMPLATE_TABLE_NAME + " WHERE " + PatientEducation.EDUCATION_USER_LOGINTYPE + " = '" + loginType + "' and " + PatientEducation.EDUCATION_USERID + " = '" + userid + "'";
        Cursor cursor = writabledb.rawQuery(query, null);

        PatientEducation education = null;
        if (cursor.moveToFirst()) {
            do {
                education = new PatientEducation();
                education.setEducationID(cursor.getInt(1));
                education.setEducationTitle(cursor.getString(2));
                education.setEducationDescription(cursor.getString(3));
                education.setEducationDocID(cursor.getInt(4));
                education.setEducationDocType(cursor.getInt(5));
                education.setEducationUserID(cursor.getInt(6));
                education.setEducationLoginType(cursor.getString(7));
                edulist.add(education);
            } while (cursor.moveToNext());
        }
        return edulist;
    }
    /* End of Patient Education */
}
