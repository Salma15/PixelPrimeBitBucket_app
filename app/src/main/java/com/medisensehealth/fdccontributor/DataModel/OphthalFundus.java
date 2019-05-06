package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by SALMA on 14-09-2018.
 */
public class OphthalFundus implements Serializable {

    public static final String FUNDUS_TABLE_NAME = "FUNDUS";
    public static final String FUNDUS_AUTO_ID = "ID";
    public static final String FUNDUS_ID = "FUNDUS_ID";
    public static final String FUNDUS_NAME = "FUNDUS_NAME";
    public static final String FUNDUS_DOCID = "FUNDUS_DOCID";
    public static final String FUNDUS_DOCTYPE = "FUNDUS_DOCTYPE";
    public static final String FUNDUS_LEFT_EYE = "FUNDUS_LEFT_EYE";
    public static final String FUNDUS_RIGHT_EYE = "FUNDUS_RIGHT_EYE";
    public static final String FUNDUS_USERID = "FUNDUS_USERID";
    public static final String FUNDUS_USER_LOGINTYPE = "FUNDUS_USER_LOGINTYPE";

    int fundus_id, doc_id, doc_type,  user_id;
    String fundus_name, left_eye, right_eye, loginType;

    public OphthalFundus(int fundus_id, String fundus_name, int doc_id, int doc_type, String left_eye,
                           String right_eye, int user_id, String user_login_type) {
        this.fundus_id = fundus_id;
        this.fundus_name = fundus_name;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.left_eye = left_eye;
        this.right_eye = right_eye;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public OphthalFundus() {

    }

    public int getFundusId() { return fundus_id; }
    public void setFundusId(int fundus_id) { this.fundus_id = fundus_id; }

    public int getDocId() { return doc_id; }
    public void setDocId(int doc_id) { this.doc_id = doc_id; }

    public int getDocType() { return doc_type; }
    public void setDocType(int doc_type) { this.doc_type = doc_type; }

    public String getLeftEye() { return left_eye; }
    public void setLeftEye(String left_eye) { this.left_eye = left_eye; }

    public String getRightEye() { return right_eye; }
    public void setRightEye(String right_eye) { this.right_eye = right_eye; }

    public int getUserId() { return user_id; }
    public void setUserId(int user_id) { this.user_id = user_id; }

    public String getFundusName() { return fundus_name; }
    public void setFundusName(String fundus_name) { this.fundus_name = fundus_name; }

    public String getLoginType() { return loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }
}
