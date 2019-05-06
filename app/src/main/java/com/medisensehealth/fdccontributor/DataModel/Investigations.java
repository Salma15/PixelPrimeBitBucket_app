package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by HP on 28-05-2018.
 */

public class Investigations implements Serializable {

    public static final String INVESTIGATION_TABLE_NAME = "INVESTIGATIONS";     // TABLE: patient_diagnosis_tests
    public static final String INVESTIGATION_AUTO_ID = "ID";
    public static final String INVESTIGATION_ID = "INVESTIGATION_ID";
    public static final String INVESTIGATION_TEST_ID = "TEST_ID";
    public static final String INVESTIGATION_TEST_NAME = "TEST_NAME";
    public static final String INVESTIGATION_TEST_DEPARTMENT = "TEST_DEPARTMENT";
    public static final String INVESTIGATION_GROUP_TEST = "GROUP_TEST";
    public static final String INVESTIGATION_MFRANGE = "MF_RANGE";
    public static final String INVESTIGATION_REPORTABLE = "REPORTABLE";
    public static final String INVESTIGATION_NORMAL_RANGE = "NORMAL_RANGE";
    public static final String INVESTIGATION_TEST_CHARGES = "TEST_CHARGES";
    public static final String INVESTIGATION_MIN_RANGE = "MIN_RANGE";
    public static final String INVESTIGATION_MAX_RANGE = "MAX_RANGE";
    public static final String INVESTIGATION_TEST_UNITS = "TEST_UNITS";
    public static final String INVESTIGATION_CRIT_LOW_RANGE = "CRIT_LOW_RANGE";
    public static final String INVESTIGATION_CRIT_HIGH_RANGE = "CRIT_HIGH_RANGE";
    public static final String INVESTIGATION_NORMAL_MIN_RANGE = "NORMAL_MIN_RANGE";
    public static final String INVESTIGATION_NORMAL_MAX_RANGE = "NORMAL_MAX_RANGE";
    public static final String INVESTIGATION_DEPT_NAME = "DEPRATMENT_NAME";
    public static final String INVESTIGATION_DEPT_TYPE = "DEPARTMENT_TYPE";
    public static final String INVESTIGATION_USERID = "INVESTIGATION_USERID";
    public static final String INVESTIGATION_USER_LOGINTYPE = "INVESTIGATION_USER_LOGINTYPE";

    int investigation_id, user_id, department;
    String test_id, test_name, group_test, mf_range, reportable, normal_range, test_charges, min_range, max_range, test_units,
            crit_low_range, crit_high_range, mormal_min_range, normal_max_range, department_name, department_type, login_type;
    String group_testid, test_actual_value;
    String right_eye_value, left_eye_value;

    public Investigations(int id, String test_id, String test_name_site_name, int department, String group_test, String is_mref_range,
                          String is_reportable, String normal_range, String test_charges, String min_range, String max_range,
                          String test_units, String crit_low_range, String crit_high_range, String normal_min_range,
                          String normal_max_range, String dept_name, String type, int user_id, String user_login_type) {
        this.investigation_id = id;
        this.test_id = test_id;
        this.test_name = test_name_site_name;
        this.department = department;
        this.group_test = group_test;
        this.mf_range = is_mref_range;
        this.reportable = is_reportable;
        this.normal_range = normal_range;
        this.test_charges = test_charges;
        this.min_range = min_range;
        this.max_range = max_range;
        this.test_units = test_units;
        this.crit_low_range = crit_low_range;
        this.crit_high_range = crit_high_range;
        this.mormal_min_range = normal_min_range;
        this.normal_max_range = normal_max_range;
        this.department_name = dept_name;
        this.department_type = type;
        this.user_id = user_id;
        this.login_type = user_login_type;
    }

    public Investigations() {

    }

    public Investigations(int investigation_id, String mainTestId, String grouptestId, String testName,
                          String normalRange, String test_value, String right_eye, String left_eye,
                          int department, int user_id, String user_login_type) {
        this.investigation_id = investigation_id;
        this.test_id = mainTestId;
        this.group_testid = grouptestId;
        this.test_name = testName;
        this.normal_range = normalRange;
        this.test_actual_value = test_value;
        this.right_eye_value = right_eye;
        this.left_eye_value = left_eye;
        this.department = department;
        this.user_id = user_id;
        this.login_type = user_login_type;
    }

    public int getInvestDepartment() { return department; }
    public void setInvestDepartment(int department) { this.department = department; }


    public String getTestActualValue() { return test_actual_value; }
    public void setTestActualValue(String test_actual_value) { this.test_actual_value = test_actual_value; }

    public String getGroupTestId() { return group_testid; }
    public void setGroupTestId(String group_testid) { this.group_testid = group_testid; }

    public int getInvestigationId() { return investigation_id; }
    public void setInvestigationId(int investigation_id) { this.investigation_id = investigation_id; }

    public int getUserId() { return user_id; }
    public void setUserId(int user_id) { this.user_id = user_id; }

    public String getTestId() { return test_id; }
    public void setTestId(String test_id) { this.test_id = test_id; }

    public String getTestName() { return test_name; }
    public void setTestName(String test_name) { this.test_name = test_name; }

    public String getGroupTest() { return group_test; }
    public void setGroupTest(String group_test) { this.group_test = group_test; }

    public String getMFRange() { return mf_range; }
    public void setMFRange(String mf_range) { this.mf_range = mf_range; }

    public String getReportable() { return reportable; }
    public void setReportable(String reportable) { this.reportable = reportable; }

    public String getNormalRange() { return normal_range; }
    public void setNormalRange(String normal_range) { this.normal_range = normal_range; }

    public String getTestCharges() { return test_charges; }
    public void setTestCharges(String test_charges) { this.test_charges = test_charges; }

    public String getMinRange() { return min_range; }
    public void setMinRange(String min_range) { this.min_range = min_range; }

    public String getMaxRange() { return max_range; }
    public void setMaxRange(String max_range) { this.max_range = max_range; }

    public String getTestUnits() { return test_units; }
    public void setTestUnits(String test_units) { this.test_units = test_units; }

    public String getCritLowRange() { return crit_low_range; }
    public void setCritLowRange(String crit_low_range) { this.crit_low_range = crit_low_range; }

    public String getCritHighRange() { return crit_high_range; }
    public void setCritHighRange(String crit_high_range) { this.crit_high_range = crit_high_range; }

    public String getNormalMinRange() { return mormal_min_range; }
    public void setNormalMinRange(String mormal_min_range) { this.mormal_min_range = mormal_min_range; }

    public String getNormalMaxRange() { return normal_max_range; }
    public void setNormalMaxRange(String normal_max_range) { this.normal_max_range = normal_max_range; }

    public String getDepartmentName() { return department_name; }
    public void setDepartmentName(String department_name) { this.department_name = department_name; }

    public String getDepartmentType() { return department_type; }
    public void setDepartmentType(String department_type) { this.department_type = department_type; }

    public String getLoginType() { return login_type; }
    public void setLoginType(String login_type) { this.login_type = login_type; }

    public String getRightEye() { return right_eye_value; }
    public void setRightEye(String right_eye_value) { this.right_eye_value = right_eye_value; }

    public String getLeftEye() { return left_eye_value; }
    public void setLeftEye(String left_eye_value) { this.left_eye_value = left_eye_value; }

}
