package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by HP on 11-07-2018.
 */

public class FamilyHistory {

    public static final String FREQ_FAMILY_HISTORY_TABLE_NAME = "FREQ_FAMILY_HISTORY";
    public static final String FREQ_FAMILY_HISTORY_AUTO_ID = "FLY_HIST_AUTO_ID";
    public static final String FREQ_FAMILY_HISTORY_FREQID = "FREQ_FLY_HIST_FRQID";
    public static final String FREQ_FAMILY_HISTORY_ID = "FREQ_FLY_HIST_ID";
    public static final String FREQ_FAMILY_HISTORY_NAME = "FREQ_FLY_HIST_NAME";
    public static final String FREQ_FAMILY_HISTORY_DOCID = "FREQ_FLY_HIST_DOCID";
    public static final String FREQ_FAMILY_HISTORY_DOCTYPE = "FREQ_FLY_HIST_DOCTYPE";
    public static final String FREQ_FAMILY_HISTORY_FREQ_COUNT = "FREQ_FLY_HIST_FREQ_COUNT";
    public static final String FREQ_FAMILY_HISTORY_USERID = "FREQ_FLY_HIST_USERID";
    public static final String FREQ_FAMILY_HISTORY_USER_LOGINTYPE = "FREQ_FLY_HIST_USER_LOGINTYPE";

    public static final String FAMILY_HISTORY_TABLE_NAME = "FAMILY_HISTORY";
    public static final String FAMILY_HISTORY_AUTO_ID = "ID";
    public static final String FAMILY_HISTORY_ID = "FLY_HIST_ID";
    public static final String FAMILY_HISTORY_NAME = "FLY_HIST_NAME";
    public static final String FAMILY_HISTORY_DOCID = "FLY_HIST_DOCID";
    public static final String FAMILY_HISTORY_DOCTYPE = "FLY_HIST_DOCTYPE";
    public static final String FAMILY_HISTORY_USERID = "FLY_HIST_USERID";
    public static final String FAMILY_HISTORY_USER_LOGINTYPE = "FLY_HIST_USER_LOGINTYPE";

    int freq_id, family_history_id, doc_id, doc_type, frq_count, user_id;
    String family_history_name, login_type;

    public FamilyHistory(int ffh_id, int family_history_id, String family_history, int doc_id,
                         int doc_type, int freq_count, int user_id, String user_login_type) {
        this.freq_id = ffh_id;
        this.family_history_id = family_history_id;
        this.family_history_name = family_history;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.frq_count = freq_count;
        this.user_id = user_id;
        this.login_type = user_login_type;
    }

    public FamilyHistory() {

    }

    public FamilyHistory(int family_history_id, String family_history, int doc_id, int doc_type,
                         int user_id, String user_login_type) {
        this.family_history_id = family_history_id;
        this.family_history_name = family_history;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.user_id = user_id;
        this.login_type = user_login_type;
    }

    public int getFamilyFrequentId() { return freq_id; }
    public void setFamilyFrequentId(int freq_id) { this.freq_id = freq_id; }

    public int getFamilyHistoryId() { return family_history_id; }
    public void setFamilyHistoryId(int family_history_id) { this.family_history_id = family_history_id; }

    public String getFamilyHistoryName() { return family_history_name; }
    public void setFamilyHistoryName(String family_history_name) { this.family_history_name = family_history_name; }

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
