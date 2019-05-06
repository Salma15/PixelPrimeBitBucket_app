package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by SALMA on 14-09-2018.
 */
public class OphthalAngleAnteriorChamber  implements Serializable {

    public static final String ANGLE_TABLE_NAME = "ANGLE_ANTERIOR_CHAMBER";
    public static final String ANGLE_AUTO_ID = "ID";
    public static final String ANGLE_ID = "ANGLE_ID";
    public static final String ANGLE_NAME = "ANGLE_NAME";
    public static final String ANGLE_DOCID = "ANGLE_DOCID";
    public static final String ANGLE_DOCTYPE = "ANGLE_DOCTYPE";
    public static final String ANGLE_LEFT_EYE = "ANGLE_LEFT_EYE";
    public static final String ANGLE_RIGHT_EYE = "ANGLE_RIGHT_EYE";
    public static final String ANGLE_USERID = "ANGLE_USERID";
    public static final String ANGLE_USER_LOGINTYPE = "ANGLE_USER_LOGINTYPE";

    int angle_id, doc_id, doc_type,  user_id;
    String angle_name, left_eye, right_eye, loginType;

    public OphthalAngleAnteriorChamber(int angle_id, String angle_name, int doc_id, int doc_type, String left_eye,
                        String right_eye, int user_id, String user_login_type) {
        this.angle_id = angle_id;
        this.angle_name = angle_name;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.left_eye = left_eye;
        this.right_eye = right_eye;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public OphthalAngleAnteriorChamber() {

    }

    public int getAngleId() { return angle_id; }
    public void setAngleId(int angle_id) { this.angle_id = angle_id; }

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

    public String getAngleName() { return angle_name; }
    public void setAngleName(String angle_name) { this.angle_name = angle_name; }

    public String getLoginType() { return loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }
}
