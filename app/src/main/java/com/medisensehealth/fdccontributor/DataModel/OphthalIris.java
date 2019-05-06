package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by SALMA on 12-09-2018.
 */
public class OphthalIris implements Serializable {

    public static final String IRIS_TABLE_NAME = "IRIS";
    public static final String IRIS_AUTO_ID = "ID";
    public static final String IRIS_ID = "IRIS_ID";
    public static final String IRIS_NAME = "IRIS_NAME";
    public static final String IRIS_DOCID = "IRIS_DOCID";
    public static final String IRIS_DOCTYPE = "IRIS_DOCTYPE";
    public static final String IRIS_LEFT_EYE = "IRIS_LEFT_EYE";
    public static final String IRIS_RIGHT_EYE = "IRIS_RIGHT_EYE";
    public static final String IRIS_USERID = "IRIS_USERID";
    public static final String IRIS_USER_LOGINTYPE = "IRIS_USER_LOGINTYPE";

    int iris_id, doc_id, doc_type,  user_id;
    String iris_name, left_eye, right_eye, loginType;

    public OphthalIris(int iris_id, String iris_name, int doc_id, int doc_type, String left_eye,
                                  String right_eye, int user_id, String user_login_type) {
        this.iris_id = iris_id;
        this.iris_name = iris_name;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.left_eye = left_eye;
        this.right_eye = right_eye;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public OphthalIris() {

    }

    public int getIrisId() { return iris_id; }
    public void setIrisId(int iris_id) { this.iris_id = iris_id; }

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

    public String getIrisName() { return iris_name; }
    public void setIrisName(String iris_name) { this.iris_name = iris_name; }

    public String getLoginType() { return loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }
}