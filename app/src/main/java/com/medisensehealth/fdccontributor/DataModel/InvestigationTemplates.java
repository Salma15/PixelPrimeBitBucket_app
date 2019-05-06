package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by SALMA on 04-10-2018.
 */
public class InvestigationTemplates {

    public static final String INVESTIGATION_TEMPLATE_TABLE_NAME = "INVESTIGATION_TEMPLATES";
    public static final String INVESTIGATION_TEMPLATE_AUTO_ID = "ID";
    public static final String INVESTIGATION_TEMPLATE_ID = "TEMPLATE_ID";
    public static final String INVESTIGATION_TEMPLATE_NAME = "TEMPLATE_NAME";
    public static final String INVESTIGATION_TEMPLATE_DEFAULT_VISIBLE = "TEMPLATE_DEFAULT_VISIBLE";
    public static final String INVESTIGATION_TEMPLATE_TEST_ID = "TEMPLATE_INVEST_TEST_ID";
    public static final String INVESTIGATION_TEMPLATE_GROUP_TEST_ID = "TEMPLATE_INVEST_GROUP_TEST_ID";
    public static final String INVESTIGATION_TEMPLATE_TEST_NAME = "TEMPLATE_INVEST_TEST_NAME";
    public static final String INVESTIGATION_TEMPLATE_TEST_DEPARTMENT = "TEMPLATE_INVEST_TEST_DEPARTMENT";
    public static final String INVESTIGATION_TEMPLATE_NORMAL_VALUE = "TEMPLATE_NORMAL_VALUE";
    public static final String INVESTIGATION_TEMPLATE_ACTUAL_VALUE = "TEMPLATE_ACTUAL_VALUE";
    public static final String INVESTIGATION_TEMPLATE_RIGHT_EYE = "TEMPLATE_RIGHT_EYE";
    public static final String INVESTIGATION_TEMPLATE_LEFT_EYE = "TEMPLATE_LEFT_EYE";
    public static final String INVESTIGATION_TEMPLATE_DOCID = "TEMPLATE_INVEST_DOCID";
    public static final String INVESTIGATION_TEMPLATE_DOCTYPE = "TEMPLATE_INVEST_DOCTYPE";
    public static final String INVESTIGATION_TEMPLATE_USERID = "TEMPLATE_INVEST_USERID";
    public static final String INVESTIGATION_TEMPLATE_USER_LOGINTYPE = "TEMPLATE_INVEST_USER_LOGINTYPE";

    private int template_id,  doc_id, doc_type, user_id, department, default_visible;
    private String test_id, group_test_id, test_name, actual_value, normal_value, right_eye, left_eye, login_type, template_name;

    public InvestigationTemplates(int invest_template_id, int doc_id, int doc_type, int default_visible,
                                  String template_name, String main_test_id, String group_test_id,
                                  String test_name_site_name, int department, String normal_range,
                                  String test_actual_value, String right_eye, String left_eye, int user_id, String user_login_type) {
        this.template_id = invest_template_id;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.default_visible = default_visible;
        this.template_name = template_name;
        this.test_id = main_test_id;
        this.group_test_id = group_test_id;
        this.test_name = test_name_site_name;
        this.department = department;
        this.normal_value = normal_range;
        this.actual_value = test_actual_value;
        this.right_eye = right_eye;
        this.left_eye = left_eye;
        this.user_id = user_id;
        this.login_type = user_login_type;
    }

    public InvestigationTemplates() {

    }

    public int getTemplateDefaultVisible() { return default_visible; }
    public void setTemplateDefaultVisible(int default_visible) {
        this.default_visible = default_visible;
    }

    public String getTemplateName() { return template_name; }
    public void setTemplateName(String template_name) {
        this.template_name = template_name;
    }

    public String getTemplateLeftEye() { return left_eye; }
    public void setTemplateLeftEye(String left_eye) {
        this.left_eye = left_eye;
    }

    public String getTemplateRightEye() { return right_eye; }
    public void setTemplateRightEye(String right_eye) {
        this.right_eye = right_eye;
    }

    public int getTemplateID() { return template_id; }
    public void setTemplateID(int template_id) {
        this.template_id = template_id;
    }

    public String getTemplateNormalValue() { return normal_value; }
    public void setTemplateNormalValue(String normal_value) {
        this.normal_value = normal_value;
    }

    public String getTemplateActualValue() { return actual_value; }
    public void setTemplateActualValue(String actual_value) {
        this.actual_value = actual_value;
    }

    public String getTemplateGroupTestID() { return group_test_id; }
    public void setTemplateGroupTestID(String group_test_id) {
        this.group_test_id = group_test_id;
    }

    public String getTemplateTestID() { return test_id; }
    public void setTemplateTestID(String test_id) {
        this.test_id = test_id;
    }

    public int getTemplateDocID() { return doc_id; }
    public void setTemplateDocID(int doc_id) {
        this.doc_id = doc_id;
    }

    public int getTemplateDocType() { return doc_type; }
    public void setTemplateDocType(int doc_type) {
        this.doc_type = doc_type;
    }

    public int getTemplateUserID() { return user_id; }
    public void setTemplateUserID(int user_id) {
        this.user_id = user_id;
    }

    public String getTemplateLoginType() { return login_type; }
    public void setTemplateLoginType(String login_type) {
        this.login_type = login_type;
    }

    public String getTemplateTestName() { return test_name; }
    public void setTemplateTestName(String test_name) {
        this.test_name = test_name;
    }

    public int getTemplateTestDepartment() { return department; }
    public void setTemplateTestDepartment(int department) { this.department = department; }

}
