package com.medisensehealth.fdccontributor.DataModel;

public class OpticalCentreList {

    int optical_id, user_id;
    String optical_name, optical_city, optical_state, optical_country, optical_contact_person, optical_mobile, optical_email, loginType;

    public OpticalCentreList(int optical_id, String optical_name, String optical_city, String optical_state, String optical_country,
                             String optical_contact_person, String optical_contact_num, String optical_email, int user_id, String user_login_type) {
        this.optical_id = optical_id;
        this.optical_name = optical_name;
        this.optical_city = optical_city;
        this.optical_state  = optical_state;
        this.optical_country = optical_country;
        this.optical_contact_person = optical_contact_person;
        this.optical_mobile = optical_contact_num;
        this.optical_email = optical_email;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public OpticalCentreList(int optical_id, String optical_name) {
        this.optical_id = optical_id;
        this.optical_name = optical_name;
    }

    public int getOpticalId() { return optical_id; }
    public void setOpticalId(int optical_id) {
        this.optical_id = optical_id;
    }

    public String getOpticalName() { return optical_name; }
    public void setOpticalName(String optical_name) {
        this.optical_name = optical_name;
    }

    public String getOpticalCity() { return optical_city; }
    public void setOpticalCity(String optical_city) { this.optical_city = optical_city; }

    public String getOpticalState() { return optical_state; }
    public void setOpticalState(String optical_state) { this.optical_state = optical_state; }

    public String getOpticalCountry() { return optical_country; }
    public void setOpticalCountry(String optical_country) { this.optical_country = optical_country; }

    public String getOpticalContactPerson() { return optical_contact_person; }
    public void setOpticalContactPerson(String optical_contact_person) { this.optical_contact_person = optical_contact_person; }

    public String getOpticalMobile() { return optical_mobile; }
    public void setOpticalMobile(String optical_mobile) { this.optical_mobile = optical_mobile; }

    public String getOpticalEmail() { return optical_email; }
    public void setOpticalEmail(String optical_email) { this.optical_email = optical_email; }

    public String getOpticalLoginType() { return loginType; }
    public void setOpticalLoginType(String loginType) { this.loginType = loginType; }

    public int getOpticalUserId() { return user_id; }
    public void setOpticalUserId(int user_id) {
        this.user_id = user_id;
    }
}
