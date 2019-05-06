package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by HP on 29-05-2018.
 */

public class InvestigationGroupTests {

    public static final String INVEST_GROUP_TABLE_NAME = "INVESTIGATION_GROUPTESTS";   // TABLE: patient_diagnosis_group_tests
    public static final String INVESTI_GROUP_AUTO_ID = "ID";
    public static final String INVEST_GROUP_ID = "GROUP_TEST_ID";
    public static final String INVEST_GROUP_TEST_ID = "TEST_ID";
    public static final String INVEST_GROUP_SUBTEST_ID = "SUBTEST_ID";
    public static final String INVEST_GROUP_ORDER_NUM = "ORDER_NUMBER";
    public static final String INVEST_GROUP_USERID = "INVEST_GROUP_USERID";
    public static final String INVEST_GROUP_USER_LOGINTYPE = "INVEST_GROUP_USER_LOGINTYPE";

    int group_testid, user_id;
    String login_type, test_id, subtest_id, order_num;

    public InvestigationGroupTests(int group_test_id, String test_id, String sub_test_id, String order_no,
                                   int user_id, String user_login_type) {
        this.group_testid = group_test_id;
        this.test_id = test_id;
        this.subtest_id = sub_test_id;
        this.order_num = order_no;
        this.user_id = user_id;
        this.login_type = user_login_type;
    }

    public InvestigationGroupTests() {

    }

    public int getGroupTestId() { return group_testid; }
    public void setGroupTestId(int group_testid) { this.group_testid = group_testid; }

    public int getUserId() { return user_id; }
    public void setUserId(int user_id) { this.user_id = user_id; }

    public String getTestId() { return test_id; }
    public void setTestId(String test_id) { this.test_id = test_id; }

    public String getSubTestId() { return subtest_id; }
    public void setSubTestId(String subtest_id) { this.subtest_id = subtest_id; }

    public String getOrderNumber() { return order_num; }
    public void setOrderNumber(String order_num) { this.order_num = order_num; }

    public String getLoginType() { return login_type; }
    public void setLoginType(String login_type) { this.login_type = login_type; }
}
