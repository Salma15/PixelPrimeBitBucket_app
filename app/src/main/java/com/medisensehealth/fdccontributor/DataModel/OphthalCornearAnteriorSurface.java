package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by SALMA on 12-09-2018.
 */
public class OphthalCornearAnteriorSurface implements Serializable {

    public static final String CORNEA_ANTERIOR_TABLE_NAME = "CORNEA_ANTERIOR";
    public static final String CORNEA_ANTERIOR_AUTO_ID = "ID";
    public static final String CORNEA_ANTERIOR_ID = "CORNEA_ANTERIOR_ID";
    public static final String CORNEA_ANTERIOR_NAME = "CORNEA_ANTERIOR_NAME";
    public static final String CORNEA_ANTERIOR_DOCID = "CORNEA_ANTERIOR_DOCID";
    public static final String CORNEA_ANTERIOR_DOCTYPE = "CORNEA_ANTERIOR_DOCTYPE";
    public static final String CORNEA_ANTERIOR_LEFT_EYE = "CORNEA_ANTERIOR_LEFT_EYE";
    public static final String CORNEA_ANTERIOR_RIGHT_EYE = "CORNEA_ANTERIOR_RIGHT_EYE";
    public static final String CORNEA_ANTERIOR_USERID = "CORNEA_ANTERIOR_USERID";
    public static final String CORNEA_ANTERIOR_USER_LOGINTYPE = "CORNEA_ANTERIOR_USER_LOGINTYPE";

    int cornea_anterior_id, doc_id, doc_type,  user_id;
    String cornea_anterior_name, left_eye, right_eye, loginType;

    public OphthalCornearAnteriorSurface(int cornea_anterior_id, String cornea_anterior_name, int doc_id, int doc_type, String left_eye,
                         String right_eye, int user_id, String user_login_type) {
        this.cornea_anterior_id = cornea_anterior_id;
        this.cornea_anterior_name = cornea_anterior_name;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.left_eye = left_eye;
        this.right_eye = right_eye;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public OphthalCornearAnteriorSurface() {

    }

    public int getCorneaAnteriorId() { return cornea_anterior_id; }
    public void setCorneaAnteriorId(int cornea_anterior_id) { this.cornea_anterior_id = cornea_anterior_id; }

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

    public String getCorneaAnteriorName() { return cornea_anterior_name; }
    public void setCorneaAnteriorName(String cornea_anterior_name) { this.cornea_anterior_name = cornea_anterior_name; }

    public String getLoginType() { return loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }
}
