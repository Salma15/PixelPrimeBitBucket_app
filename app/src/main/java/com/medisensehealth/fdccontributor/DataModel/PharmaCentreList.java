package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by HP on 12-04-2018.
 */

public class PharmaCentreList {
    int pharma_id, user_id;
    String pharma_name, pharma_city, pharma_state, pharma_country, pharma_contact_person, pharma_mobile, pharma_email, loginType;

    public PharmaCentreList(int ph_id, String ph_name, String ph_city, String ph_state,
                                String ph_country, String ph_contact_person, String ph_contact_num,
                                String ph_email, int user_id, String user_login_type) {
        this.pharma_id = ph_id;
        this.pharma_name = ph_name;
        this.pharma_city = ph_city;
        this.pharma_state = ph_state;
        this.pharma_country = ph_country;
        this.pharma_contact_person = ph_contact_person;
        this.pharma_mobile = ph_contact_num;
        this.pharma_email = ph_email;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public PharmaCentreList(int pharma_id, String pharma_name) {
        this.pharma_id = pharma_id;
        this.pharma_name = pharma_name;
    }

    public int getPharmaId() { return pharma_id; }
    public void setPharmaId(int pharma_id) {
        this.pharma_id = pharma_id;
    }

    public String getPharmaName() { return pharma_name; }
    public void setPharmaName(String pharma_name) {
        this.pharma_name = pharma_name;
    }

    public String getPharmaCity() { return pharma_city; }
    public void setPharmaCity(String pharma_city) {
        this.pharma_city = pharma_city;
    }

    public String getPharmaState() { return pharma_state; }
    public void setPharmaState(String pharma_state) {
        this.pharma_state = pharma_state;
    }

    public String getPharmaCountry() { return pharma_country; }
    public void setPharmaCountry(String pharma_country) {
        this.pharma_country = pharma_country;
    }

    public String getPharmaContactPerson() { return pharma_contact_person; }
    public void setPharmaContactPerson(String pharma_contact_person) {
        this.pharma_contact_person = pharma_contact_person;
    }

    public String getPharmaMobile() { return pharma_mobile; }
    public void setPharmaMobile(String pharma_mobile) {
        this.pharma_mobile = pharma_mobile;
    }

    public String getPharmaEmail() { return pharma_email; }
    public void setPharmaEmail(String pharma_email) {
        this.pharma_email = pharma_email;
    }

    public int getPharmaUserId() { return user_id; }
    public void setPharmaUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getPharmaLoginType() { return loginType; }
    public void setPharmaLoginType(String loginType) {
        this.loginType = loginType;
    }

}
