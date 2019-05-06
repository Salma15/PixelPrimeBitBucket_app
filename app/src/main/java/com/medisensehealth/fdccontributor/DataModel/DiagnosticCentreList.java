package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by HP on 12-04-2018.
 */

public class DiagnosticCentreList {

    int diagno_id, user_id;
    String diagno_name, diagno_city, diagno_state, diagno_country, diagno_contact_person, diagno_mobile, diagno_email, loginType;

    public DiagnosticCentreList(int diagnostic_id, String diagnosis_name, String diagnosis_city, String diagnosis_state,
                                String diagnosis_country, String diagnosis_contact_person, String diagnosis_contact_num,
                                String diagnosis_email, int user_id, String user_login_type) {
        this.diagno_id = diagnostic_id;
        this.diagno_name = diagnosis_name;
        this.diagno_city = diagnosis_city;
        this.diagno_state = diagnosis_state;
        this.diagno_country = diagnosis_country;
        this.diagno_contact_person = diagnosis_contact_person;
        this.diagno_mobile = diagnosis_contact_num;
        this.diagno_email = diagnosis_email;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public DiagnosticCentreList(int diagno_id, String diagno_name) {
        this.diagno_id = diagno_id;
        this.diagno_name = diagno_name;
    }

    public int getDiagnoId() { return diagno_id; }
    public void setDiagnoId(int diagno_id) {
        this.diagno_id = diagno_id;
    }

    public String getDiagnoName() { return diagno_name; }
    public void setDiagnoName(String diagno_name) {
        this.diagno_name = diagno_name;
    }

    public String getDiagnoCity() { return diagno_city; }
    public void setDiagnoCity(String diagno_city) {
        this.diagno_city = diagno_city;
    }

    public String getDiagnoState() { return diagno_state; }
    public void setDiagnoState(String diagno_state) {
        this.diagno_state = diagno_state;
    }

    public String getDiagnoCountry() { return diagno_country; }
    public void setDiagnoCountry(String diagno_country) {
        this.diagno_country = diagno_country;
    }

    public String getDiagnoContactPerson() { return diagno_contact_person; }
    public void setDiagnoContactPerson(String diagno_contact_person) {
        this.diagno_contact_person = diagno_contact_person;
    }

    public String getDiagnoMobile() { return diagno_mobile; }
    public void setDiagnoMobile(String diagno_mobile) {
        this.diagno_mobile = diagno_mobile;
    }

    public String getDiagnoEmail() { return diagno_email; }
    public void setDiagnoEmail(String diagno_email) {
        this.diagno_email = diagno_email;
    }

    public int getDiagnoUserId() { return user_id; }
    public void setDiagnoUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getDiagnoLoginType() { return loginType; }
    public void setDiagnoLoginType(String loginType) {
        this.loginType = loginType;
    }

}
