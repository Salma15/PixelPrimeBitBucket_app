package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by SALMA on 14-09-2018.
 */
public class OphthalViterous implements Serializable {

    public static final String VITEROUS_TABLE_NAME = "VITEROUS";
    public static final String VITEROUS_AUTO_ID = "ID";
    public static final String VITEROUS_ID = "VITEROUS_ID";
    public static final String VITEROUS_NAME = "VITEROUS_NAME";
    public static final String VITEROUS_DOCID = "VITEROUS_DOCID";
    public static final String VITEROUS_DOCTYPE = "VITEROUS_DOCTYPE";
    public static final String VITEROUS_LEFT_EYE = "VITEROUS_LEFT_EYE";
    public static final String VITEROUS_RIGHT_EYE = "VITEROUS_RIGHT_EYE";
    public static final String VITEROUS_USERID = "VITEROUS_USERID";
    public static final String VITEROUS_USER_LOGINTYPE = "VITEROUS_USER_LOGINTYPE";

    int viterous_id, doc_id, doc_type,  user_id;
    String viterous_name, left_eye, right_eye, loginType;

    public OphthalViterous(int viterous_id, String viterous_name, int doc_id, int doc_type, String left_eye,
                       String right_eye, int user_id, String user_login_type) {
        this.viterous_id = viterous_id;
        this.viterous_name = viterous_name;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.left_eye = left_eye;
        this.right_eye = right_eye;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public OphthalViterous() {

    }

    public int getViterousId() { return viterous_id; }
    public void setViterousId(int viterous_id) { this.viterous_id = viterous_id; }

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

    public String getViterousName() { return viterous_name; }
    public void setViterousName(String viterous_name) { this.viterous_name = viterous_name; }

    public String getLoginType() { return loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }
}
