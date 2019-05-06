package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by HP on 01-06-2018.
 */

public class Treatments implements Serializable {

    public static final String FREQ_TREATMENT_TABLE_NAME = "FREQ_TREATMENTS";
    public static final String FREQ_TREATMENT_AUTO_ID = "ID";
    public static final String FREQ_TREATMENT_ID = "FREQ_TREATMENT_ID";
    public static final String FREQ_TREATMENT_NAME = "FREQ_TREATMENT_NAME";
    public static final String FREQ_TREATMENT_DOCID = "FREQ_TREATMENT_DOCID";
    public static final String FREQ_TREATMENT_DOCTYPE = "FREQ_TREATMENT_DOCTYPE";
    public static final String FREQ_TREATMENT_COUNT = "FREQ_TREATMENT_COUNT";
    public static final String FREQ_TREATMENT_USERID = "FREQ_TREATMENT_USERID";
    public static final String FREQ_TREATMENT_USER_LOGINTYPE = "FREQ_TREATMENT_USER_LOGINTYPE";

    public static final String TREATMENT_TABLE_NAME = "TREATMENTS";
    public static final String TREATMENT_AUTO_ID = "ID";
    public static final String TREATMENT_ID = "TREATMENT_ID";
    public static final String TREATMENT_NAME = "TREATMENT_NAME";
    public static final String TREATMENT_DOCID = "TREATMENT_DOCID";
    public static final String TREATMENT_DOCTYPE = "TREATMENT_DOCTYPE";
    public static final String TREATMENT_COUNT = "TREATMENT_COUNT";
    public static final String TREATMENT_USERID = "TREATMENT_USERID";
    public static final String TREATMENT_USER_LOGINTYPE = "TREATMENT_USER_LOGINTYPE";

    int treatment_id, doc_id, doc_type, freq_count, user_id;
    String treatment_name, login_type;

    public Treatments(int dft_id, String treatment, int doc_id, int doc_type, int freq_count,
                      int user_id, String user_login_type) {
        this.treatment_id = dft_id;
        this.treatment_name = treatment;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.freq_count = freq_count;
        this.user_id = user_id;
        this.login_type = user_login_type;
    }

    public Treatments() {

    }

    public int getTreatmentID() { return treatment_id; }
    public void setTreatmentID(int treatment_id) {
        this.treatment_id = treatment_id;
    }

    public String getTreatmentName() { return treatment_name; }
    public void setTreatmentName(String treatment_name) {
        this.treatment_name = treatment_name;
    }

    public int getTreatmentDocID() { return doc_id; }
    public void setTreatmentDocID(int doc_id) {
        this.doc_id = doc_id;
    }

    public int getTreatmentDocType() { return doc_type; }
    public void setTreatmentDocType(int doc_type) {
        this.doc_type = doc_type;
    }

    public int getTreatmentFreqCount() { return freq_count; }
    public void setTreatmentFreqCount(int freq_count) {
        this.freq_count = freq_count;
    }

    public int getTreatmentUserID() { return user_id; }
    public void setTreatmentUserID(int user_id) {
        this.user_id = user_id;
    }

    public String getTreatmentLoginType() { return login_type; }
    public void setTreatmentLoginType(String login_type) {
        this.login_type = login_type;
    }
}
