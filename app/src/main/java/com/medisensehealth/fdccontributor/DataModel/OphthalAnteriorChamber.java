package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by SALMA on 12-09-2018.
 */
public class OphthalAnteriorChamber implements Serializable {

    public static final String ANTERIOR_CHAMBER_TABLE_NAME = "ANTERIOR_CHAMBER";
    public static final String ANTERIOR_CHAMBER_AUTO_ID = "ID";
    public static final String ANTERIOR_CHAMBER_ID = "ANTERIOR_CHAMBER_ID";
    public static final String ANTERIOR_CHAMBER_NAME = "ANTERIOR_CHAMBER_NAME";
    public static final String ANTERIOR_CHAMBER_DOCID = "ANTERIOR_CHAMBER_DOCID";
    public static final String ANTERIOR_CHAMBER_DOCTYPE = "ANTERIOR_CHAMBER_DOCTYPE";
    public static final String ANTERIOR_CHAMBER_LEFT_EYE = "ANTERIOR_CHAMBER_LEFT_EYE";
    public static final String ANTERIOR_CHAMBER_RIGHT_EYE = "ANTERIOR_CHAMBER_RIGHT_EYE";
    public static final String ANTERIOR_CHAMBER_USERID = "ANTERIOR_CHAMBER_USERID";
    public static final String ANTERIOR_CHAMBER_USER_LOGINTYPE = "ANTERIOR_CHAMBER_USER_LOGINTYPE";

    int anterior_chamber_id, doc_id, doc_type,  user_id;
    String anterior_chamber_name, left_eye, right_eye, loginType;

    public OphthalAnteriorChamber(int anterior_chamber_id, String anterior_chamber_name, int doc_id, int doc_type, String left_eye,
                                          String right_eye, int user_id, String user_login_type) {
        this.anterior_chamber_id = anterior_chamber_id;
        this.anterior_chamber_name = anterior_chamber_name;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.left_eye = left_eye;
        this.right_eye = right_eye;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public OphthalAnteriorChamber() {

    }

    public int getAnteriorChamberId() { return anterior_chamber_id; }
    public void setAnteriorChamberId(int anterior_chamber_id) { this.anterior_chamber_id = anterior_chamber_id; }

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

    public String getAnteriorChamberName() { return anterior_chamber_name; }
    public void setAnteriorChamberName(String anterior_chamber_name) { this.anterior_chamber_name = anterior_chamber_name; }

    public String getLoginType() { return loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }
}


