package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by lenovo on 24-02-2017.
 */

public class PartnerHistory {

    private int partner_id;
    private String partner_name, history,Address,Email_id,Email_id1,Email_id2,contact_person,person_position,landline_num,cont_num1,
                cont_num2,website,location,state,country,Type,password,reg_date,partner_logo, partner_gcmtokenid, user_encryprty_id, specialization_name;

    public PartnerHistory(int partner_id,String partner_name, String history,String Address, String Email_id, String Email_id1,
                         String Email_id2,String contact_person,String person_position,String landline_num,String cont_num1,
                         String cont_num2,String website,String location,String state,String country,String Type,String password,
                         String reg_date, String partner_logo, String partner_gcmtokenid, String encrypt_id, String spec_name) {

        this.partner_id = partner_id;
        this.partner_name = partner_name;
        this.history = history;
        this.Address = Address;
        this.Email_id = Email_id;
        this.Email_id1 = Email_id1;
        this.Email_id2 = Email_id2;
        this.contact_person = contact_person;
        this.person_position = person_position;
        this.landline_num = landline_num;
        this.cont_num1 = cont_num1;
        this.cont_num2 = cont_num2;
        this.website = website;
        this.location = location;
        this.state = state;
        this.country = country;
        this.Type = Type;
        this.password = password;
        this.reg_date = reg_date;
        this.partner_logo = partner_logo;
        this.partner_gcmtokenid = partner_gcmtokenid;
        this.user_encryprty_id = encrypt_id;
        this.specialization_name = spec_name;
    }

    public String getPartSpecializationName() { return specialization_name; }
    public void setPartSpecializationName(String specialization_name) {
        this.specialization_name = specialization_name;
    }

    public String getPartEncyptID() { return user_encryprty_id; }
    public void setPartEncyptID(String user_encryprty_id) {
        this.user_encryprty_id = user_encryprty_id;
    }


    public int getPartnerId() { return partner_id; }
    public void setPartnerId(int ref_id) {
        this.partner_id = ref_id;
    }

    public String getPartnername() { return partner_name; }
    public void setPartnername(String ref_name) {
        this.partner_name = ref_name;
    }

    public String getPartnerHistory() { return history; }
    public void setPartnerHistory(String history) {
        this.history = history;
    }

    public String getPartnerAddress() { return Address; }
    public void setPartnerAddress(String Address) {
        this.Address = Address;
    }

    public String getPartnerEmail() { return Email_id; }
    public void setPartnerEmail(String Email_id) {
        this.Email_id = Email_id;
    }

    public String getPartnerEmail1() { return Email_id1; }
    public void setPartnerEmail1(String Email_id1) {
        this.Email_id1 = Email_id1;
    }

    public String getPartnerEmail2() { return Email_id2; }
    public void setPartnerEmail2(String Email_id2) {
        this.Email_id2 = Email_id2;
    }

    public String getPartnerContactPerson() { return contact_person; }
    public void setPartnerContactPerson(String contact_person) {this.contact_person = contact_person;}

    public String getPartnerPersonPosition() { return person_position; }
    public void setPartnerPersonPosition(String person_position) {
        this.person_position = person_position;
    }

    public String getPartnerLandline() { return landline_num; }
    public void setPartnerLandline(String landline_num) {
        this.landline_num = landline_num;
    }

    public String getPartnerContactnum1() { return cont_num1; }
    public void setPartnerContactnum1(String cont_num1) {
        this.cont_num1 = cont_num1;
    }

    public String getPartnerContactnum2() { return cont_num2; }
    public void setPartnerContactnum2(String cont_num2) {
        this.cont_num2 = cont_num2;
    }

    public String getPartnerWebsite() { return website; }
    public void setPartnerWebsite(String website) {
        this.website = website;
    }

    public String getPartnerLocation() { return location; }
    public void setPartnerLocation(String location) {
        this.location = location;
    }

    public String getPartnerState() { return state; }
    public void setPartnerState(String state) {
        this.state = state;
    }

    public String getPartnerCountry() { return country; }
    public void setPartnerCountry(String country) {
        this.country = country;
    }

    public String getPartnerType() { return Type; }
    public void setPartnerType(String Type) {
        this.Type = Type;
    }

    public String getPartnerPassword() { return password; }
    public void setPartnerPassword(String password) {
        this.password = password;
    }

    public String getPartnerRegDate() { return reg_date; }
    public void setPartnerRegDate(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getPartnerLogo() { return partner_logo; }
    public void setPartnerLogo(String partner_logo) {
        this.partner_logo = partner_logo;
    }

    public String getPartnerGCMTokenId() { return partner_gcmtokenid; }
    public void setPartnerGCMTokenId(String partner_gcmtokenid) {
        this.partner_gcmtokenid = partner_gcmtokenid;
    }
}
