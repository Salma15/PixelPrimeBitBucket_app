package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by HP on 31-05-2018.
 */

public class Diagnosis implements Serializable {

    public static final String DIAGNOSIS_TABLE_NAME = "DIAGNOSIS";
    public static final String DIAGNOSIS_AUTO_ID = "ID";
    public static final String DIAGNOSIS_FREQ_ID = "DIAGNOSIS_FREQ_ID";
    public static final String DIAGNOSIS_ICD_ID = "DIAGNOSIS_ICD_ID";
    public static final String DIAGNOSIS_ICD_NAME = "DIAGNOSIS_ICD_NAME";
    public static final String DIAGNOSIS_DOCID = "DIAGNOSIS_DOCID";
    public static final String DIAGNOSIS_DOCTYPE = "DIAGNOSIS_DOCTYPE";
    public static final String DIAGNOSIS_FREQUENT_COUNT = "DIAGNOSIS_FREQ_COUNT";
    public static final String DIAGNOSIS_USERID = "DIAGNOSIS_USERID";
    public static final String DIAGNOSIS_USER_LOGINTYPE = "DIAGNOSIS_USER_LOGINTYPE";

    int diagno_frqID, icd_id, doc_id, doc_type, freq_count, user_id;
    String icd_name, loginType;

    public Diagnosis(int dfd_id, int icd_id, String icd_code, int doc_id, int doc_type, int freq_count,
                     int user_id, String user_login_type) {
        this.diagno_frqID = dfd_id;
        this.icd_id = icd_id;
        this.icd_name = icd_code;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.freq_count = freq_count;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public Diagnosis() {

    }


    public Diagnosis(int icd_id, String icd_code, int user_id, String user_login_type) {
        this.icd_id = icd_id;
        this.icd_name = icd_code;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public int getDiagnoFreqId() { return diagno_frqID; }
    public void setDiagnoFreqId(int diagno_frqID) { this.diagno_frqID = diagno_frqID; }

    public int getICDId() { return icd_id; }
    public void setICDId(int icd_id) { this.icd_id = icd_id; }

    public String getICDName() { return icd_name; }
    public void setICDName(String icd_name) { this.icd_name = icd_name; }

    public int getDocId() { return doc_id; }
    public void setDocId(int doc_id) { this.doc_id = doc_id; }

    public int getDocType() { return doc_type; }
    public void setDocType(int doc_type) { this.doc_type = doc_type; }

    public int getFreqCount() { return freq_count; }
    public void setFreqCount(int freq_count) { this.freq_count = freq_count; }

    public int getUserId() { return user_id; }
    public void setUserId(int user_id) { this.user_id = user_id; }

    public String getLoginType() { return loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }
}
