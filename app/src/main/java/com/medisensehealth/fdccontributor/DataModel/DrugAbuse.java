package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by HP on 10-07-2018.
 */

public class DrugAbuse {

    public static final String FREQ_DRUG_ABUSE_TABLE_NAME = "FREQ_DRUG_ABUSE";
    public static final String FREQ_DRUG_ABUSE_AUTO_ID = "FREQ_ID";
    public static final String FREQ_DRUG_ABUSE_FREQID = "FREQ_ABUSE_FRQID";
    public static final String FREQ_DRUG_ABUSE_ID = "FREQ_ABUSE_ID";
    public static final String FREQ_DRUG_ABUSE_NAME = "FREQ_ABUSE_NAME";
    public static final String FREQ_DRUG_ABUSE_DOCID = "FREQ_ABUSE_DOCID";
    public static final String FREQ_DRUG_ABUSE_DOCTYPE = "FREQ_ABUSE_DOCTYPE";
    public static final String FREQ_DRUG_ABUSE_FREQ_COUNT = "FREQ_ABUSE_FREQ_COUNT";
    public static final String FREQ_DRUG_ABUSE_USERID = "FREQ_ABUSE_USERID";
    public static final String FREQ_DRUG_ABUSE_USER_LOGINTYPE = "FREQ_ABUSE_USER_LOGINTYPE";

    public static final String DRUG_ABUSE_TABLE_NAME = "DRUG_ABUSE";
    public static final String DRUG_ABUSE_AUTO_ID = "ID";
    public static final String DRUG_ABUSE_ID = "ABUSE_ID";
    public static final String DRUG_ABUSE_NAME = "ABUSE_NAME";
    public static final String DRUG_ABUSE_DOCID = "ABUSE_DOCID";
    public static final String DRUG_ABUSE_DOCTYPE = "ABUSE_DOCTYPE";
    public static final String DRUG_ABUSE_USERID = "ABUSE_USERID";
    public static final String DRUG_ABUSE_USER_LOGINTYPE = "ABUSE_USER_LOGINTYPE";

    int freq_id, abuse_id, doc_id, doc_type, frq_count, user_id;
    String abuse_name, login_type;

    public DrugAbuse(int fda_id, int drug_abuse_id, String drug_abuse, int doc_id, int doc_type,
                     int freq_count, int user_id, String user_login_type) {
        this.freq_id = fda_id;
        this.abuse_id = drug_abuse_id;
        this.abuse_name = drug_abuse;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.frq_count =  freq_count;
        this.user_id = user_id;
        this.login_type = user_login_type;
    }

    public DrugAbuse() {

    }

    public DrugAbuse(int drug_abuse_id, String drug_abuse, int doc_id, int doc_type, int user_id, String user_login_type) {
        this.abuse_id = drug_abuse_id;
        this.abuse_name = drug_abuse;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.user_id = user_id;
        this.login_type = user_login_type;
    }

    public int getAbuseFrequentId() { return freq_id; }
    public void setAbuseFrequentId(int freq_id) { this.freq_id = freq_id; }

    public int getAbuseId() { return abuse_id; }
    public void setAbuseId(int abuse_id) { this.abuse_id = abuse_id; }

    public String getAbuseName() { return abuse_name; }
    public void setAbuseName(String abuse_name) { this.abuse_name = abuse_name; }

    public int getDocId() { return doc_id; }
    public void setDocId(int doc_id) { this.doc_id = doc_id; }

    public int getDocType() { return doc_type; }
    public void setDocType(int doc_type) { this.doc_type = doc_type; }

    public int getFreqCount() { return frq_count; }
    public void setFreqCount(int frq_count) { this.frq_count = frq_count; }

    public int getUserId() { return user_id; }
    public void setUserId(int user_id) { this.user_id = user_id; }

    public String getLoginType() { return login_type; }
    public void setLoginType(String login_type) { this.login_type = login_type; }


}
