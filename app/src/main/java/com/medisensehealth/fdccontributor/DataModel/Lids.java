package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by SALMA on 08-09-2018.
 */
public class Lids  implements Serializable {

    public static final String LIDS_TABLE_NAME = "LIDS";
    public static final String LIDS_AUTO_ID = "ID";
    public static final String LIDS_ID = "LIDS_ID";
    public static final String LIDS_NAME = "LIDS_NAME";
    public static final String LIDS_DOCID = "LIDS_DOCID";
    public static final String LIDS_DOCTYPE = "LIDS_DOCTYPE";
    public static final String LIDS_LEFT_EYE = "LIDS_LEFT_EYE";
    public static final String LIDS_RIGHT_EYE = "LIDS_RIGHT_EYE";
    public static final String LIDS_USERID = "LIDS_USERID";
    public static final String LIDS_USER_LOGINTYPE = "LIDS_USER_LOGINTYPE";

    int lids_id, doc_id, doc_type,  user_id;
    String lids_name, left_eye, right_eye, loginType;

    public Lids(int lids_id, String lids_name, int doc_id, int doc_type, String left_eye,
                String right_eye, int user_id, String user_login_type) {
        this.lids_id = lids_id;
        this.lids_name = lids_name;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.left_eye = left_eye;
        this.right_eye = right_eye;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public Lids() {

    }


    public int getLidsId() { return lids_id; }
    public void setLidsId(int lids_id) { this.lids_id = lids_id; }

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

    public String getLidsName() { return lids_name; }
    public void setLidsName(String lids_name) { this.lids_name = lids_name; }

    public String getLoginType() { return loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }
}
