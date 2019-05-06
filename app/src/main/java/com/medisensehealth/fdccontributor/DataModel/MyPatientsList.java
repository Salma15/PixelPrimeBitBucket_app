package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by lenovo on 23/06/2017.
 */

public class MyPatientsList {

    /* My Patients*/
    public static final String MYPATIENT_TABLE_NAME = "MYPATIENT";
    public static final String MYPATIENT_AUTO_ID = "ID";                     // Local Auto increment ID
    public static final String MYPATIENT_ID = "MYPATIENT_ID";
    public static final String MYPATIENT_NAME = "MYPATIENT_NAME";
    public static final String MYPATIENT_AGE = "MYPATIENT_AGE";
    public static final String MYPATIENT_EMAIL = "MYPATIENT_EMAIL";
    public static final String MYPATIENT_GEN = "MYPATIENT_GEN";                     // integer
    public static final String MYPATIENT_MERITAL_STATUS = "MYPATIENT_MERITAL_STATUS";
    public static final String MYPATIENT_QUALIFICATION = "MYPATIENT_QUAL";
    public static final String MYPATIENT_WEIGHT = "MYPATIENT_WEIGHT";               //integer
    public static final String MYPATIENT_HYPERCONDITION = "MYPATIENT_HYPER";        // integer
    public static final String MYPATIENT_DIABETES = "MYPATIENT_DIABETES";           // integer
    public static final String MYPATIENT_BLOOD = "MYPATIENT_BLOOD";
    public static final String MYPATIENT_CONTACT_PERSON = "MYPATIENT_CONTACT_PERSON";
    public static final String MYPATIENT_PROFESSION = "MYPATIENT_PROFESSION";
    public static final String MYPATIENT_MOBILE = "MYPATIENT_MOBILE";
    public static final String MYPATIENT_LOCATION = "MYPATIENT_LOCATION";
    public static final String MYPATIENT_STATE = "MYPATIENT_STATE";
    public static final String MYPATIENT_COUNTRY = "MYPATIENT_COUNTRY";
    public static final String MYPATIENT_ADDRESS = "MYPATIENT_ADDRESS";
    public static final String MYPATIENT_REGDATE = "MYPATIENT_REGDATE";
    public static final String MYPATIENT_PARTNER_ID = "MYPATIENT_PARTNER_ID";
    public static final String MYPATIENT_CREATED_DATE = "MYPATIENT_CREATED_DATE";
    public static final String MYPATIENT_USER_ID = "MYPATIENT_USER_ID";
    public static final String MYPATIENT_LOGIN_TYPE = "MYPATIENT_LOGIN_TYPE";
    public static final String MYPATIENT_BSYNC = "MYPATIENT_BSYNC";

    int patient_id, gender,  hypercondition, diabetes, partner_id, user_id, bsync, doc_id;
    String pantient_name, age, email, merital_status, qualification, blood, contact_person, marital_status,
            profession, mobile, location, state, country, address, created_date, login_type, regDate,smoking, alocoholic,
            drug_abuse, other_details, family_history, previous_invention, neuro_issues, kidney_issues, weight, height;

    public MyPatientsList(int patient_id, String patient_name, String patient_age, String patient_email, int patient_gen,
                          String weight, int hyper_cond, int diabetes_cond, String smoking, String alcoholic, String drug_abuse,
                          String other_details, String family_history, String prev_inter, String neuro_issue, String kidney_issue,
                          String patient_mob, String patient_loc, String pat_state, String pat_country, String patient_addrs,
                          String tImestamp, String user_id, int doc_id, String system_date, int user_id1, String user_login_type,
                          String height) {

        this.patient_id = patient_id;
        this.pantient_name = patient_name;
        this.age = patient_age;
        this.email = patient_email;
        this.gender = patient_gen;
        this.weight = weight;
        this.hypercondition = hyper_cond;
        this.diabetes = diabetes_cond;
        this.smoking = smoking;
        this.alocoholic = alcoholic;
        this.drug_abuse = drug_abuse;
        this.other_details = other_details;
        this.family_history = family_history;
        this.previous_invention = prev_inter;
        this.neuro_issues = neuro_issue;
        this.kidney_issues = kidney_issue;
        this.mobile = patient_mob;
        this.location = patient_loc;
        this.state = pat_state;
        this.country = pat_country;
        this.address = patient_addrs;
        this.regDate = tImestamp;
        this.user_id = user_id1;
        this.login_type = user_login_type;
        this.doc_id = doc_id;
        this.height = height;
    }

    public String getHeight() {
        return height;
    }
    public void setHeight(String height) {
        this.height = height;
    }

    public int getMyPatientDocId() {
        return doc_id;
    }
    public void setMyPatientDocId(int doc_id) {
        this.doc_id = doc_id;
    }

    public String getMyPatientKidneyIssues() {
        return kidney_issues;
    }
    public void setMyPatientKidneyIssues(String kidney_issues) {
        this.kidney_issues = kidney_issues;
    }

    public String getMyPatientNeuroIssues() {
        return neuro_issues;
    }
    public void setMyPatientNeuroIssues(String neuro_issues) {
        this.neuro_issues = neuro_issues;
    }

    public String getMyPatientPreviousInterventions() {
        return previous_invention;
    }
    public void setMyPatientPreviousInterventions(String previous_invention) {
        this.previous_invention = previous_invention;
    }

    public String getMyPatientFamilyHistory() {
        return family_history;
    }
    public void setMyPatientFamilyHistory(String family_history) {
        this.family_history = family_history;
    }

    public String getMyPatientOtherDetails() {
        return other_details;
    }
    public void setMyPatientOtherDetails(String other_details) {
        this.other_details = other_details;
    }

    public String getMyPatientDrugAbuse() {
        return drug_abuse;
    }
    public void setMyPatientDrugAbuse(String drug_abuse) {
        this.drug_abuse = drug_abuse;
    }

    public String getMyPatientAlcohol() {
        return alocoholic;
    }
    public void setMyPatientAlcohol(String alocoholic) {
        this.alocoholic = alocoholic;
    }

    public String getMyPatientSmoke() {
        return smoking;
    }
    public void setMyPatientSmoke(String smoking) {
        this.smoking = smoking;
    }

    public String getMyPatientRegDate() {
        return regDate;
    }
    public void setMyPatientRegDate(String regDate) {
        this.regDate = regDate;
    }

    public int getMyPatientID() {
        return patient_id;
    }

    public void setMyPatientID(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getHyperCondition() {
        return hypercondition;
    }

    public void setHyperCondition(int hypercondition) {
        this.hypercondition = hypercondition;
    }

    public int getDiabetes() {
        return diabetes;
    }

    public void setDiabetes(int diabetes) {
        this.diabetes = diabetes;
    }

    public int getPartnerID() {
        return partner_id;
    }

    public void setPartnerID(int partner_id) {
        this.partner_id = partner_id;
    }

    public int getUserID() {
        return user_id;
    }

    public void setUserID(int user_id) {
        this.user_id = user_id;
    }

    public int getBSync() {
        return bsync;
    }

    public void setBSync(int bsync) {
        this.bsync = bsync;
    }

    public String getPatientName() {
        return pantient_name;
    }
    public void setPatientName(String pantient_name) {
        this.pantient_name = pantient_name;
    }

    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getMeritalStatus() {
        return merital_status;
    }
    public void setMeritalStatus(String merital_status) {
        this.email = merital_status;
    }

    public String getQualification() {
        return qualification;
    }
    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getBloodGroup() {
        return blood;
    }
    public void setBloodGroup(String blood) {
        this.blood = blood;
    }

    public String getContactPerson() {
        return contact_person;
    }

    public void setContactPerson(String contact_person) {
        this.contact_person = contact_person;
    }
    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedDate() {
        return created_date;
    }
    public void setCreatedDate(String created_date) {
        this.created_date = created_date;
    }

    public String geLoginType() {
        return login_type;
    }
    public void setLoginType(String login_type) {
        this.login_type = login_type;
    }

}