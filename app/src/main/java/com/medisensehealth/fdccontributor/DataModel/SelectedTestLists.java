package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by HP on 27-03-2018.
 */

public class SelectedTestLists implements Serializable {
    String subtest_id, investigation_id, patient_id, episode_id, doc_id, test_id, test_name, doc_type;
    private String subtest_name, test_value, test_normal;

    public SelectedTestLists() {
    }

    public SelectedTestLists(String test_id,String test_name, String subtest_id, String subtest_name, String test_normal, String test_value) {
        this.test_id = test_id;
        this.test_name = test_name;
        this.subtest_id = subtest_id;
        this.subtest_name = subtest_name;
        this.test_value = test_value;
        this.test_normal = test_normal;
    }

    public SelectedTestLists(String investigation_id, String subtest_id, String main_test_id, String patient_id, String episode_id, String doc_id, String doc_type) {
        this.investigation_id = investigation_id;
        this.subtest_id = subtest_id;
        this.test_id = main_test_id;
        this.patient_id = patient_id;
        this.episode_id = episode_id;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
    }

    public String getInvestigationID() {
        return investigation_id;
    }
    public void setInvestigationID(String investigation_id) {
        this.investigation_id = investigation_id;
    }

    public String getTestName() {
        return test_name;
    }
    public void setTestName(String test_name) {
        this.test_name = test_name;
    }

    public String getTestID() {
        return test_id;
    }
    public void setTestID(String test_id) {
        this.test_id = test_id;
    }

    public String getSubTestID() {
        return subtest_id;
    }
    public void setSubTestID(String subtest_id) {
        this.subtest_id = subtest_id;
    }

    public String getSubTestName() {
        return subtest_name;
    }
    public void setSubTestName(String subtest_name) {
        this.subtest_name = subtest_name;
    }

    public String getTestValue() {
        return test_value;
    }
    public void setTestValue(String test_value) {
        this.test_value = test_value;
    }

    public String getTestNormal() {
        return test_normal;
    }
    public void setTestNormal(String test_normal) {
        this.test_normal = test_normal;
    }
}
