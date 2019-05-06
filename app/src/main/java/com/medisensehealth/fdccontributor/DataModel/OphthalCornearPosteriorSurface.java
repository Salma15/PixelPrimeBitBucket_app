package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by SALMA on 12-09-2018.
 */
public class OphthalCornearPosteriorSurface implements Serializable {

    public static final String CORNEA_POSTERIOR_TABLE_NAME = "CORNEA_POSTERIOR";
    public static final String CORNEA_POSTERIOR_AUTO_ID = "ID";
    public static final String CORNEA_POSTERIOR_ID = "CORNEA_POSTERIOR_ID";
    public static final String CORNEA_POSTERIOR_NAME = "CORNEA_POSTERIOR_NAME";
    public static final String CORNEA_POSTERIOR_DOCID = "CORNEA_POSTERIOR_DOCID";
    public static final String CORNEA_POSTERIOR_DOCTYPE = "CORNEA_POSTERIOR_DOCTYPE";
    public static final String CORNEA_POSTERIOR_LEFT_EYE = "CORNEA_POSTERIOR_LEFT_EYE";
    public static final String CORNEA_POSTERIOR_RIGHT_EYE = "CORNEA_POSTERIOR_RIGHT_EYE";
    public static final String CORNEA_POSTERIOR_USERID = "CORNEA_POSTERIOR_USERID";
    public static final String CORNEA_POSTERIOR_USER_LOGINTYPE = "CORNEA_POSTERIOR_USER_LOGINTYPE";

    int cornea_posterior_id, doc_id, doc_type,  user_id;
    String cornea_posterior_name, left_eye, right_eye, loginType;

    public OphthalCornearPosteriorSurface(int cornea_posterior_id, String cornea_posterior_name, int doc_id, int doc_type, String left_eye,
                                         String right_eye, int user_id, String user_login_type) {
        this.cornea_posterior_id = cornea_posterior_id;
        this.cornea_posterior_name = cornea_posterior_name;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.left_eye = left_eye;
        this.right_eye = right_eye;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public OphthalCornearPosteriorSurface() {

    }

    public int getCorneaPosteriorId() { return cornea_posterior_id; }
    public void setCorneaPosteriorId(int cornea_posterior_id) { this.cornea_posterior_id = cornea_posterior_id; }

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

    public String getCorneaPosteriorName() { return cornea_posterior_name; }
    public void setCorneaPosteriorName(String cornea_posterior_name) { this.cornea_posterior_name = cornea_posterior_name; }

    public String getLoginType() { return loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }
}

