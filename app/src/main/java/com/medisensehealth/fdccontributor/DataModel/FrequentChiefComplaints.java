package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by HP on 28-05-2018.
 */

public class FrequentChiefComplaints {

    public static final String FREQUENT_COMPLAINT_TABLE_NAME = "FREQUENT_COMPLAINT";
    public static final String FREQUENT_COMPLAINT_AUTO_ID = "ID";
    public static final String FREQUENT_COMPLAINT_ID = "FREQUENT_COMPLAINT_ID";
    public static final String FREQUENT_SYMPTOMS_ID = "FREQUENT_SYMPTOMS_ID";
    public static final String FREQUENT_SYMPTOMS_NAME = "FREQUENT_SYMPTOMS_NAME";
    public static final String FREQUENT_DOCID = "FREQUENT_DOCID";
    public static final String FREQUENT_DOCTYPE = "FREQUENT_DOCTYPE";
    public static final String FREQUENT_COUNT = "FREQUENT_COUNT";
    public static final String FREQUENT_USERID = "FREQUENT_USERID";
    public static final String FREQUENT_USER_LOGINTYPE = "FREQUENT_USER_LOGINTYPE";


    int freqComp_id, symptoms_id, doc_id, doc_type, freq_count, user_id;
    String symptoms_name, login_type;

    public FrequentChiefComplaints(int dfs_id, int symptoms_id, int doc_id, int doc_type, int freq_count, String symp_name, int user_id,
                                   String user_login_type) {
        this.freqComp_id = dfs_id;
        this.symptoms_id = symptoms_id;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.freq_count = freq_count;
        this.symptoms_name = symp_name;
        this.user_id = user_id;
        this.login_type = user_login_type;
    }

    public FrequentChiefComplaints() {

    }

    public int getFreqComplaintID() { return freqComp_id; }
    public void setFreqComplaintID(int freqComp_id) {
        this.freqComp_id = freqComp_id;
    }

    public int getFreqSymptomsID() { return symptoms_id; }
    public void setFreqSymptomsID(int symptoms_id) {
        this.symptoms_id = symptoms_id;
    }

    public int getFreqDocID() { return doc_id; }
    public void setFreqDocID(int doc_id) {
        this.doc_id = doc_id;
    }

    public int getFreqDocType() { return doc_type; }
    public void setFreqDocType(int doc_type) {
        this.doc_type = doc_type;
    }

    public int getFreqCount() { return freq_count; }
    public void setFreqCount(int freq_count) {
        this.freq_count = freq_count;
    }

    public int getFreqUserID() { return user_id; }
    public void setFreqUserID(int user_id) {
        this.user_id = user_id;
    }

    public String getFreqLoginType() { return login_type; }
    public void setFreqLoginType(String login_type) {
        this.login_type = login_type;
    }

    public String getFreqSymptomsName() { return symptoms_name; }
    public void setFreqSymptomsName(String symptoms_name) {
        this.symptoms_name = symptoms_name;
    }

}
