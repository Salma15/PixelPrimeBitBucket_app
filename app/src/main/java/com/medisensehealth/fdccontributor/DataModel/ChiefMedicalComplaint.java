package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by HP on 26-05-2018.
 */

public class ChiefMedicalComplaint implements Serializable {

    public static final String CHIEF_MEDICALCOMPLAINT_TABLE_NAME = "CHIEF_MEDICAL_COMPLAINT";
    public static final String CHIEF_MEDICALCOMPLAINT_AUTO_ID = "ID";
    public static final String CHIEF_COMPLAINT_ID = "COMPLAINT_ID";
    public static final String CHIEF_SYMPTOMS_NAME = "SYMPTOMS_NAME";
    public static final String CHIEF_DOCID = "CHIEF_DOCID";
    public static final String CHIEF_DOCTYPE = "CHIEF_DOCTYPE";
    public static final String CHIEF_USERID = "CHIEF_USERID";
    public static final String CHIEF_USER_LOGINTYPE = "CHIEF_USER_LOGINTYPE";

    int complaint_id, doc_id, doc_type, user_id;
    String symptoms_name, loginType;

    public ChiefMedicalComplaint(int complaint_id, String symptoms, int doc_id, int doc_type,
                                 int user_id, String user_login_type) {
        this.complaint_id = complaint_id;
        this.symptoms_name = symptoms;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public ChiefMedicalComplaint() {

    }

    public int getComplaintId() { return complaint_id; }
    public void setComplaintId(int complaint_id) { this.complaint_id = complaint_id; }

    public int getDocId() { return doc_id; }
    public void setDocId(int doc_id) { this.doc_id = doc_id; }

    public int getDocType() { return doc_type; }
    public void setDocType(int doc_type) { this.doc_type = doc_type; }

    public String getSymptomsName() { return symptoms_name; }
    public void setSymptomsName(String symptoms_name) { this.symptoms_name = symptoms_name; }

    public int getUserId() { return user_id; }
    public void setUserId(int user_id) { this.user_id = user_id; }

    public String getLoginType() { return loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }
}
