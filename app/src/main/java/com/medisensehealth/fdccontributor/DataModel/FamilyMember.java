package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by salma on 16/03/18.
 */

public class FamilyMember {
    private int userid, member_id;
    private String member_name, relationship, dob, gender;

    public FamilyMember(int member_id, int userid, String gender, String member_name, String relationship, String dob) {
        this.member_id = member_id;
        this.userid = userid;
        this.gender = gender;
        this.member_name = member_name;
        this.relationship = relationship;
        this.dob = dob;
    }

    public int getMemberid() {
        return member_id;
    }
    public void setMemberid(int member_id) {
        this.member_id = member_id;
    }

    public int getUserid() {
        return userid;
    }
    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMemberName() {
        return member_name;
    }
    public void setMemberName(String member_name) {
        this.member_name = member_name;
    }

    public String getRelationship() {
        return relationship;
    }
    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getMemberDOB() {
        return dob;
    }
    public void setMemberDOB(String dob) {
        this.dob = dob;
    }

}
