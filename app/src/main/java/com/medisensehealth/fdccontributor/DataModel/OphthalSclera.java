package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by SALMA on 11-09-2018.
 */
public class OphthalSclera implements Serializable {

    public static final String SCLERA_TABLE_NAME = "SCLERA";
    public static final String SCLERA_AUTO_ID = "ID";
    public static final String SCLERA_ID = "SCLERA_ID";
    public static final String SCLERA_NAME = "SCLERA_NAME";
    public static final String SCLERA_DOCID = "SCLERA_DOCID";
    public static final String SCLERA_DOCTYPE = "SCLERA_DOCTYPE";
    public static final String SCLERA_LEFT_EYE = "SCLERA_LEFT_EYE";
    public static final String SCLERA_RIGHT_EYE = "SCLERA_RIGHT_EYE";
    public static final String SCLERA_USERID = "SCLERA_USERID";
    public static final String SCLERA_USER_LOGINTYPE = "SCLERA_USER_LOGINTYPE";

    int sclera_id, doc_id, doc_type,  user_id;
    String sclera_name, left_eye, right_eye, loginType;

    public OphthalSclera(int sclera_id, String sclera_name, int doc_id, int doc_type, String left_eye,
                         String right_eye, int user_id, String user_login_type) {
        this.sclera_id = sclera_id;
        this.sclera_name = sclera_name;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.left_eye = left_eye;
        this.right_eye = right_eye;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public OphthalSclera() {

    }

    public int getScleraId() { return sclera_id; }
    public void setScleraId(int sclera_id) { this.sclera_id = sclera_id; }

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

    public String getScleraName() { return sclera_name; }
    public void setScleraName(String sclera_name) { this.sclera_name = sclera_name; }

    public String getLoginType() { return loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }
}
