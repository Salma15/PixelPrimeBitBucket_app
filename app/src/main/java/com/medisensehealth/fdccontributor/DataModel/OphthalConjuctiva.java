package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by SALMA on 11-09-2018.
 */
public class OphthalConjuctiva  implements Serializable {

    public static final String CONJUCTIVA_TABLE_NAME = "CONJUCTIVA";
    public static final String CONJUCTIVA_AUTO_ID = "ID";
    public static final String CONJUCTIVA_ID = "CONJUCTIVA_ID";
    public static final String CONJUCTIVA_NAME = "CONJUCTIVA_NAME";
    public static final String CONJUCTIVA_DOCID = "CONJUCTIVA_DOCID";
    public static final String CONJUCTIVA_DOCTYPE = "CONJUCTIVA_DOCTYPE";
    public static final String CONJUCTIVA_LEFT_EYE = "CONJUCTIVA_LEFT_EYE";
    public static final String CONJUCTIVA_RIGHT_EYE = "CONJUCTIVA_RIGHT_EYE";
    public static final String CONJUCTIVA_USERID = "CONJUCTIVA_USERID";
    public static final String CONJUCTIVA_USER_LOGINTYPE = "CONJUCTIVA_USER_LOGINTYPE";

    int conjuctiva_id, doc_id, doc_type,  user_id;
    String conjuctiva_name, left_eye, right_eye, loginType;

    public OphthalConjuctiva(int conjuctiva_id, String conjuctiva_name, int doc_id, int doc_type, String left_eye,
                String right_eye, int user_id, String user_login_type) {
        this.conjuctiva_id = conjuctiva_id;
        this.conjuctiva_name = conjuctiva_name;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.left_eye = left_eye;
        this.right_eye = right_eye;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public OphthalConjuctiva() {

    }


    public int getConjuctivaId() { return conjuctiva_id; }
    public void setConjuctivaId(int conjuctiva_id) { this.conjuctiva_id = conjuctiva_id; }

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

    public String getConjuctivaName() { return conjuctiva_name; }
    public void setConjuctivaName(String conjuctiva_name) { this.conjuctiva_name = conjuctiva_name; }

    public String getLoginType() { return loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }
}
