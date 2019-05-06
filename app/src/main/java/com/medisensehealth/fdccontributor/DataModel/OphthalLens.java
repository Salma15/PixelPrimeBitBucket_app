package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by SALMA on 14-09-2018.
 */
public class OphthalLens implements Serializable {

    public static final String LENS_TABLE_NAME = "LENS";
    public static final String LENS_AUTO_ID = "ID";
    public static final String LENS_ID = "LENS_ID";
    public static final String LENS_NAME = "LENS_NAME";
    public static final String LENS_DOCID = "LENS_DOCID";
    public static final String LENS_DOCTYPE = "LENS_DOCTYPE";
    public static final String LENS_LEFT_EYE = "LENS_LEFT_EYE";
    public static final String LENS_RIGHT_EYE = "LENS_RIGHT_EYE";
    public static final String LENS_USERID = "LENS_USERID";
    public static final String LENS_USER_LOGINTYPE = "LENS_USER_LOGINTYPE";

    int lens_id, doc_id, doc_type,  user_id;
    String lens_name, left_eye, right_eye, loginType;

    public OphthalLens(int lens_id, String lens_name, int doc_id, int doc_type, String left_eye,
                                       String right_eye, int user_id, String user_login_type) {
        this.lens_id = lens_id;
        this.lens_name = lens_name;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.left_eye = left_eye;
        this.right_eye = right_eye;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public OphthalLens() {

    }

    public int getLensId() { return lens_id; }
    public void setLensId(int lens_id) { this.lens_id = lens_id; }

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

    public String getLensName() { return lens_name; }
    public void setLensName(String lens_name) { this.lens_name = lens_name; }

    public String getLoginType() { return loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }
}
