package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by lenovo on 02/06/2017.
 */

public class PatientsList {

    public static final String PATIENTS_TABLE_NAME = "PATIENTLIST";
    public static final String PATIENT_AUTO_ID = "ID";
    public static final String PATIENT_ID = "PATIENTID";
    public static final String PATIENT_NAME = "NAME";
    public static final String PATIENT_AGE = "AGE";
    public static final String PATIENT_LOCATION = "LOCATION";
    public static final String PATIENT_STATUS = "STATUS";
    public static final String PATIENT_DOCNAME = "DOCNAME";
    public static final String PATIENT_DOCTORID = "DOCTORID";
    public static final String PATIENT_LOGINTYPE = "LOGINTYPE";
    public static final String PATIENT_REFERBY = "REFERBY";
    public static final String PATIENT_LOGINUSERID = "LOGINUSERID";
    public static final String PATIENT_STATUS_TIME = "STATUS_TIME";

    int patient_id,patient_status,pat_doc_id, login_userid;
    String patient_name, patient_age, patient_city, patient_docname, loginType, referBy, statusTime;

    public PatientsList() {
    }

    public PatientsList(int pId,String pName, String pAge, String pCity, int pStatus, String pDocname, int pat_doc_id, String loginType, String referBy, int loginUserid, String status_time) {
        this.patient_id = pId;
        this.patient_status = pStatus;
        this.patient_name = pName;
        this.patient_age = pAge;
        this.patient_city = pCity;
        this.patient_docname = pDocname;
        this.pat_doc_id = pat_doc_id;
        this.loginType = loginType;
        this.referBy = referBy;
        this.login_userid = loginUserid;
        this.statusTime = status_time;
    }

    public int getPatientId() { return patient_id; }
    public void setPatientId(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getPatientStatus() { return patient_status; }
    public void setPatientStatus(int patient_status) {
        this.patient_status = patient_status;
    }

    public String getPatientName() {
        return patient_name;
    }
    public void setPatientName(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPatientAge() {
        return patient_age;
    }
    public void setPatientAge(String patient_age) {
        this.patient_age = patient_age;
    }

    public String getPatientCity() {
        return patient_city;
    }
    public void setPatientCity(String patient_city) {
        this.patient_city = patient_city;
    }

    public String getPatientDocName() {
        return patient_docname;
    }
    public void setPatientDocName(String patient_docname) { this.patient_docname = patient_docname; }

    public int getPatientDocId() { return pat_doc_id; }
    public void setPatienDocId(int pat_doc_id) {
        this.pat_doc_id = pat_doc_id;
    }

    public String getPatientLoginType() {
        return loginType;
    }
    public void setPatientLoginType(String loginType) { this.loginType = loginType; }

    public String getPatientReferBy() {
        return referBy;
    }
    public void setPatientReferBy(String referBy) { this.referBy = referBy; }

    public int getPatientLoginUserId() { return login_userid; }
    public void setPatientLoginUserId(int login_userid) {
        this.login_userid = login_userid;
    }

    public String getPatientStatusTime() { return statusTime; }
    public void setPatientStatusTime(String statusTime) {
        this.statusTime = statusTime;
    }

}
