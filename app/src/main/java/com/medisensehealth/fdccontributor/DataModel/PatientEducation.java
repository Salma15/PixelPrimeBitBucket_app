package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by SALMA on 06-10-2018.
 */
public class PatientEducation {

    public static final String EDUCATION_TEMPLATE_TABLE_NAME = "PATIENT_EDUCATION";
    public static final String EDUCATION_AUTO_ID = "ID";
    public static final String EDUCATION_ID = "EDUCATION_ID";
    public static final String EDUCATION_TITLE = "EDUCATION_NAME";
    public static final String EDUCATION_DESCRIPTION = "EDUCATION_DESCRIPTION";
    public static final String EDUCATION_DOCID = "EDUCATION_DOCID";
    public static final String EDUCATION_DOCTYPE = "EDUCATION_DOCTYPE";
    public static final String EDUCATION_USERID = "EDUCATION_USERID";
    public static final String EDUCATION_USER_LOGINTYPE = "EDUCATION_USER_LOGINTYPE";

    private int edu_id, doc_id, doc_type, user_id;
    private String edu_title, edu_description, login_type;

    public PatientEducation(int edu_id, String edu_title, String edu_description, int doc_id,
                            int doc_type, int user_id, String user_login_type) {
        this.edu_id = edu_id;
        this.edu_title = edu_title;
        this.edu_description = edu_description;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.user_id = user_id;
        this.login_type = user_login_type;
    }

    public PatientEducation() {

    }

    public int getEducationID() { return edu_id; }
    public void setEducationID(int edu_id) {
        this.edu_id = edu_id;
    }

    public int getEducationDocID() { return doc_id; }
    public void setEducationDocID(int doc_id) {
        this.doc_id = doc_id;
    }

    public int getEducationDocType() { return doc_type; }
    public void setEducationDocType(int doc_type) {
        this.doc_type = doc_type;
    }

    public int getEducationUserID() { return user_id; }
    public void setEducationUserID(int user_id) {
        this.user_id = user_id;
    }

    public String getEducationTitle() { return edu_title; }
    public void setEducationTitle(String edu_title) {
        this.edu_title = edu_title;
    }

    public String getEducationDescription() { return edu_description; }
    public void setEducationDescription(String edu_description) { this.edu_description = edu_description; }

    public String getEducationLoginType() { return login_type; }
    public void setEducationLoginType(String login_type) {
        this.login_type = login_type;
    }
}
