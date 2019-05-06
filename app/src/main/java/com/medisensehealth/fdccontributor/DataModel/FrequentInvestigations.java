package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by HP on 28-05-2018.
 */

public class FrequentInvestigations {

    public static final String FREQUENT_INVESTIGATION_TABLE_NAME = "FREQ_INVEST_COMPLAINT";
    public static final String FREQUENT_INVESTIGATION_AUTO_ID = "ID";
    public static final String FREQUENT_INVESTIGATION_ID = "FREQ_INVEST_ID";
    public static final String FREQUENT_TEST_ID = "FRE_INVEST_TEST_ID";
    public static final String FREQUENT_TEST_NAME = "FREQ_INVEST_TEST_NAME";
    public static final String FREQUENT_TEST_DEPARTMENT = "FREQ_INVEST_TEST_DEPARTMENT";
    public static final String FREQUENT_DOCID = "FREQ_INVEST_DOCID";
    public static final String FREQUENT_DOCTYPE = "FREQ_INVEST_DOCTYPE";
    public static final String FREQUENT_COUNT = "FREQ_INVEST_COUNT";
    public static final String FREQUENT_USERID = "FREQ_INVEST_USERID";
    public static final String FREQUENT_USER_LOGINTYPE = "FREQ_INVEST_USER_LOGINTYPE";

    int freqInvest_id, test_id, doc_id, doc_type, freq_count, user_id, department;
    String test_name, login_type;

    public FrequentInvestigations(int dfi_id, int main_test_id, int doc_id, int doc_type, int freq_test_count,
                                  String test_name_site_name, int department, int user_id, String user_login_type) {
        this.freqInvest_id = dfi_id;
        this.test_id = main_test_id;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.freq_count = freq_test_count;
        this.test_name = test_name_site_name;
        this.department = department;
        this.user_id = user_id;
        this.login_type = user_login_type;
    }

    public FrequentInvestigations() {

    }

    public int getFreqInvestigationID() { return freqInvest_id; }
    public void setFreqInvestigationID(int freqInvest_id) {
        this.freqInvest_id = freqInvest_id;
    }

    public int getFreqTestID() { return test_id; }
    public void setFreqTestID(int test_id) {
        this.test_id = test_id;
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

    public String getFreqTestName() { return test_name; }
    public void setFreqTestName(String test_name) {
        this.test_name = test_name;
    }

    public int getFreqTestDepartment() { return department; }
    public void setFreqTestDepartment(int department) { this.department = department; }

}
