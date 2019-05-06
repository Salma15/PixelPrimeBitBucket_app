package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by SALMA on 14-09-2018.
 */
public class OphthalPupil implements Serializable {

    public static final String PUPIL_TABLE_NAME = "PUPIL";
    public static final String PUPIL_AUTO_ID = "ID";
    public static final String PUPIL_ID = "PUPIL_ID";
    public static final String PUPIL_NAME = "PUPIL_NAME";
    public static final String PUPIL_DOCID = "PUPIL_DOCID";
    public static final String PUPIL_DOCTYPE = "PUPIL_DOCTYPE";
    public static final String PUPIL_LEFT_EYE = "PUPIL_LEFT_EYE";
    public static final String PUPIL_RIGHT_EYE = "PUPIL_RIGHT_EYE";
    public static final String PUPIL_USERID = "PUPIL_USERID";
    public static final String PUPIL_USER_LOGINTYPE = "PUPIL_USER_LOGINTYPE";

    int pupil_id, doc_id, doc_type,  user_id;
    String pupil_name, left_eye, right_eye, loginType;

    public OphthalPupil(int pupil_id, String pupil_name, int doc_id, int doc_type, String left_eye,
                       String right_eye, int user_id, String user_login_type) {
        this.pupil_id = pupil_id;
        this.pupil_name = pupil_name;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.left_eye = left_eye;
        this.right_eye = right_eye;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public OphthalPupil() {

    }

    public int getPupilId() { return pupil_id; }
    public void setPupilId(int pupil_id) { this.pupil_id = pupil_id; }

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

    public String getPupilName() { return pupil_name; }
    public void setPupilName(String pupil_name) { this.pupil_name = pupil_name; }

    public String getLoginType() { return loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }
}
