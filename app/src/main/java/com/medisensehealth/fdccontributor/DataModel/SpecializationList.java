package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by lenovo on 07-03-2017.
 */

public class SpecializationList {

    public static final String SPECIALIZATION_TABLE_NAME = "SPECIALIZATIONLIST";
    public static final String SPECIALIZATION_AUTO_ID = "ID";
    public static final String SPECIALIZATION_ID = "SPECIALIZATIONID";
    public static final String SPECIALIZATION_NAME = "SPECIALIZATION";

    int specialization_id, user_id, specialization_group_id;
    String specialization_name, login_type;

    public SpecializationList() {
    }

    public SpecializationList(int specId, String speacNmae, int userid, String loginType, int spec_group_id) {
        this.specialization_id = specId;
        this.specialization_name = speacNmae;
        this.user_id = userid;
        this.login_type = loginType;
        this.specialization_group_id = spec_group_id;
    }

    public SpecializationList(int specId, String speacNmae, int userid, String loginType) {
        this.specialization_id = specId;
        this.specialization_name = speacNmae;
        this.user_id = userid;
        this.login_type = loginType;
    }

    public SpecializationList(String speacNmae) {
        this.specialization_name = speacNmae;
    }

    public int getSpecializationId() { return specialization_id; }
    public void setSpecializationId(int specialization_id) {
        this.specialization_id = specialization_id;
    }

    public String getSpecializationName() {
        return specialization_name;
    }
    public void setSpecializationName(String specialization_name) {
        this.specialization_name = specialization_name;
    }

    public int getSpecializationUserId() { return user_id; }
    public void setSpecializationUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getSpecializationLoginType() {
        return login_type;
    }
    public void setSpecializationLoginType(String login_type) {
        this.login_type = login_type;
    }

    public int getSpecializationGroupId() { return specialization_group_id; }
    public void setSpecializationGroupId(int specialization_group_id) {
        this.specialization_group_id = specialization_group_id;
    }

}
