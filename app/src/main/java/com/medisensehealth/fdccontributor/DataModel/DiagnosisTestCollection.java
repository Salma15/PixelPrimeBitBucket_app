package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by HP on 27-03-2018.
 */

public class DiagnosisTestCollection {

    String test_id, test_name, subtest_name, group_check, min_range;

    public DiagnosisTestCollection(String testId, String testnam, String gpname, String check, String min) {
        this.test_id = testId;
        this.test_name = testnam;
        this.subtest_name = gpname;
        this.group_check = check;
        this.min_range = min;
    }

    public String getTestID() {
        return test_id;
    }
    public void setTestID(String test_id) {
        this.test_id = test_id;
    }

    public String getTestName() {
        return test_name;
    }
    public void setTestName(String test_name) {
        this.test_name = test_name;
    }

    public String getSubTestName() {
        return subtest_name;
    }
    public void setSubTestName(String subtest_name) {
        this.subtest_name = subtest_name;
    }

    public String getGroupCheck() {
        return group_check;
    }
    public void setGroupCheck(String group_check) {
        this.group_check = group_check;
    }

    public String getMinRange() {
        return min_range;
    }
    public void setMinRange(String min_range) {
        this.min_range = min_range;
    }

}
