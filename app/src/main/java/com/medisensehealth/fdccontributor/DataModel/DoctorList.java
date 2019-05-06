package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by lenovo on 11-03-2017.
 */

public class DoctorList {

    public static final String DOCTORS_TABLE_NAME = "DOCTORLIST";
    public static final String DOCTORS_AUTO_ID = "ID";
    public static final String DOCTORS_ID = "DOCTOR_ID";
    public static final String DOCTORS_NAME = "DOCTOR_NAME";
    public static final String DOCTORS_AGE = "DOCTOR_AGE";
    public static final String DOCTORS_EXPERIENCE = "DOCTOR_EXP";
    public static final String DOCTORS_PHOTO = "DOC_PHOTO";
    public static final String DOCTORS_QUALIFICATION = "DOC_QUALIFICATION";
    public static final String DOCTORS_SPEC_ID = "DOC_SPEC_ID";
    public static final String DOCTORS_SPEC_NAME = "DOC_SPEC_NAME";
    public static final String DOCTORS_FAVOURITES = "DOC_FAVOURITE";
    public static final String DOCTORS_LOGIN_TYPE = "DOC_LOGIN_TYPE";
    public static final String DOCTORS_LOGIN_USERID = "DOC_LOGINUSERID";
    public static final String DOCTORS_ADDRESS = "DOCTOR_ADDRESS";


    int refid, doc_age, favourite, spec_id, login_userid;
    String doc_name, doc_exp, spec_name, doc_photo, doc_qualification, doc_logintype, doc_address;

    public DoctorList() {

    }

    public DoctorList(int refid, String refname, int ref_age, String ref_exp, String doc_photo,String doc_qualification, int spec_id, String spec_name, int favourite, String doc_logintype, int login_userid, String doc_address)
    {
        this.refid = refid;
        this.doc_name = refname;
        this.doc_exp = ref_exp;
        this.doc_age = ref_age;
        this.spec_id = spec_id;
        this.spec_name = spec_name;
        this.doc_photo = doc_photo;
        this.doc_qualification= doc_qualification;
        this.favourite = favourite;
        this.doc_logintype = doc_logintype;
        this.login_userid = login_userid;
        this.doc_address = doc_address;
    }

    public DoctorList(int refid, String refname, int ref_age, String ref_exp, String doc_photo,String doc_qualification, int spec_id, String spec_name, String doc_logintype, int login_userid, String doc_address)
    {
        this.refid = refid;
        this.doc_name = refname;
        this.doc_exp = ref_exp;
        this.doc_age = ref_age;
        this.spec_id = spec_id;
        this.spec_name = spec_name;
        this.doc_photo = doc_photo;
        this.doc_qualification= doc_qualification;
        this.doc_logintype = doc_logintype;
        this.login_userid = login_userid;
        this.doc_address = doc_address;
    }

    public String getDoctorAddress() {
        return doc_address;
    }
    public void setDoctorAddress(String doc_address) {
        this.doc_address = doc_address;
    }

    public int getDoctorId() { return refid; }
    public void setDoctorId(int refid) {
        this.refid = refid;
    }

    public String getDoctorName() {
        return doc_name;
    }
    public void setDoctorName(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getDoctorExperience() {
        return doc_exp;
    }
    public void setDoctorExperience(String doc_exp) {
        this.doc_exp = doc_exp;
    }

    public int getDoctorAge() { return doc_age; }
    public void setDoctorAge(int doc_age) {
        this.doc_age = doc_age;
    }

    public int getDoctorSpecializationId() { return spec_id; }
    public void setDoctorSpecializationId(int spec_id) {
        this.spec_id = spec_id;
    }

    public String getDoctorSpecificationName() {
        return spec_name;
    }
    public void setDoctorSpecificationName(String spec_name) {
        this.spec_name = spec_name;
    }

    public String getDoctorPhoto() {
        return doc_photo;
    }
    public void setDoctorPhoto(String doc_photo) {
        this.doc_photo = doc_photo;
    }

    public String getDoctorQualification() {
        return doc_qualification;
    }
    public void setDoctorQualification(String doc_qualification) {
        this.doc_qualification = doc_qualification;
    }

    public int getDoctorFavoutite() { return favourite; }
    public void setDoctorFavourite(int favourite) {
        this.favourite = favourite;
    }

    public String getDoctorLoginType() { return doc_logintype; }
    public void setDoctorLoginType(String doc_logintype) {
        this.doc_logintype = doc_logintype;
    }

    public int getDoctorLoginUserId() { return login_userid; }
    public void setDoctorLoginUserId(int login_userid) {
        this.login_userid = login_userid;
    }
}
