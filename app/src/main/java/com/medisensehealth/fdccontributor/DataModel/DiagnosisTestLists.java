package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by HP on 26-03-2018.
 */

public class DiagnosisTestLists implements Serializable {
    int  user_id;
    String test_name, login_type, test_id, diagnosis_id, episode_id;

    public DiagnosisTestLists() {
    }

    public DiagnosisTestLists(String test_id, String test_name, int userid, String loginType) {
        this.test_id = test_id;
        this.test_name = test_name;
        this.user_id = userid;
        this.login_type = loginType;
    }

    public DiagnosisTestLists(String test_name) {
        this.test_name = test_name;
    }

    public DiagnosisTestLists(String patient_diagnosis_id, String icd_id, String patient_id, String doc_id, String episode_id) {
        this.diagnosis_id = patient_diagnosis_id;
        this.test_id = icd_id;
        this.episode_id = episode_id;
    }

    public String getDiagnosisId() { return diagnosis_id; }
    public void setDiagnosisId(String diagnosis_id) {
        this.diagnosis_id = diagnosis_id;
    }

    public String getTestId() { return test_id; }
    public void setTestId(String test_id) {
        this.test_id = test_id;
    }

    public String getTestName() {
        return test_name;
    }
    public void setTestName(String test_name) {
        this.test_name = test_name;
    }

    public int getUserId() { return user_id; }
    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getLoginType() {
        return login_type;
    }
    public void setLoginType(String login_type) {
        this.login_type = login_type;
    }

}
