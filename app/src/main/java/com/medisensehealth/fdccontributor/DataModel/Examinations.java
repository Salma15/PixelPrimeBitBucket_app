package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by HP on 30-05-2018.
 */

public class Examinations implements Serializable {

    public static final String FREQ_EXAMINATION_TABLE_NAME = "FREQ_EXAMINATIONS";
    public static final String FREQ_EXAMINATION_AUTO_ID = "ID";
    public static final String FREQ_EXAM_FREQ_ID = "FREQ_EXAM_FREQ_ID";
    public static final String FREQ_EXAMINATION_ID = "FREQ_EXAMINATION_ID";
    public static final String FREQ_EXAMINATION_NAME = "FREQ_EXAMINATION_NAME";
    public static final String FREQ_EXAMINATION_DOCID = "FREQ_EXAMINATION_DOCID";
    public static final String FREQ_EXAMINATION_DOCTYPE = "FREQ_EXAMINATION_DOCTYPE";
    public static final String FREQ_EXAMINATION_COUNT = "FREQ_EXAMINATION_COUNT";
    public static final String FREQ_EXAMINATION_USERID = "FREQ_EXAMINATION_USERID";
    public static final String FREQ_EXAMINATION_USER_LOGINTYPE = "FREQ_EXAMINATION_USER_LOGINTYPE";

    public static final String EXAMINATION_TABLE_NAME = "EXAMINATIONS";
    public static final String EXAMINATION_AUTO_ID = "ID";
    public static final String EXAMINATION_ID = "EXAMINATION_ID";
    public static final String EXAMINATION_NAME = "EXAMINATION_NAME";
    public static final String EXAMINATION_RESULTS = "EXAMINATION_RESULTS";
    public static final String EXAMINATION_FINDINGS = "EXAMINATION_FINDINGS";
    public static final String EXAMINATION_DOCID = "EXAMINATION_DOCID";
    public static final String EXAMINATION_DOCTYPE = "EXAMINATION_DOCTYPE";
    public static final String EXAMINATION_USERID = "EXAMINATION_USERID";
    public static final String EXAMINATION_USER_LOGINTYPE = "EXAMINATION_USER_LOGINTYPE";

    int exam_freqId, examination_id, doc_id, doc_type, freq_count, user_id;
    String examination_name, login_type, exam_results, exam_findings;

    public Examinations(int dfe_id, int examination_id, String examination, int doc_id, int doc_type, int freq_count,
                        int user_id, String user_login_type) {
        this.exam_freqId = dfe_id;
        this.examination_id = examination_id;
        this.examination_name = examination;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.freq_count = freq_count;
        this.user_id = user_id;
        this.login_type = user_login_type;
    }

    public Examinations() {

    }

    public Examinations(int examination_id, String examination, String result, String findings, int doc_id, int doc_type, int user_id, String user_login_type) {
        this.examination_id = examination_id;
        this.examination_name = examination;
        this.exam_results  = result;
        this.exam_findings = findings;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.user_id = user_id;
        this.login_type = user_login_type;
    }

    public int getExaminationFreqID() { return exam_freqId; }
    public void setExaminationFreqID(int exam_freqId) {
        this.exam_freqId = exam_freqId;
    }

    public int getExaminationID() { return examination_id; }
    public void setExaminationID(int examination_id) {
        this.examination_id = examination_id;
    }

    public String getExaminationName() { return examination_name; }
    public void setExaminationName(String examination_name) {
        this.examination_name = examination_name;
    }

    public int getExaminationDocID() { return doc_id; }
    public void setExaminationDocID(int doc_id) {
        this.doc_id = doc_id;
    }

    public int getExaminationDocType() { return doc_type; }
    public void setExaminationDocType(int doc_type) {
        this.doc_type = doc_type;
    }

    public int getExaminationFreqCount() { return freq_count; }
    public void setExaminationFreqCount(int freq_count) {
        this.freq_count = freq_count;
    }

    public int getExaminationUserID() { return user_id; }
    public void setExaminationUserID(int user_id) {
        this.user_id = user_id;
    }

    public String getExaminationLoginType() { return login_type; }
    public void setExaminationLoginType(String login_type) {
        this.login_type = login_type;
    }

    public String getExaminationResults() { return exam_results; }
    public void setExaminationResults(String exam_results) {
        this.exam_results = exam_results;
    }

    public String getExaminationFindings() { return exam_findings; }
    public void setExaminationFindings(String exam_findings) {
        this.exam_findings = exam_findings;
    }
}
