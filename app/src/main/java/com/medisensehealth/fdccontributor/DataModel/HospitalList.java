package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by lenovo on 12-03-2017.
 */

public class HospitalList {

    public static final String HOSPITAL_TABLE_NAME = "HOSPITALLIST";
    public static final String HOSPITAL_AUTO_ID = "ID";
    public static final String HOSPITAL_ID = "HOSPITAL_ID";
    public static final String HOSPITAL_NAME = "HOSPITAL_NAME";
    public static final String HOSPITAL_CITY = "HOSPITAL_CITY";
    public static final String HOSPITAL_STATE = "HOSPITAL_STATE";
    public static final String HOSPITAL_USER_ID = "HOSPITAL_USER_ID";
    public static final String HOSPITAL_LOGIN_TYPE = "HOSPITAL_USER_LOGIN_TYPE";


    int hospid, user_id;
    String hosp_name, hosp_city, hosp_state, login_type, hosp_address, hosp_country;

    public HospitalList() {
    }

    public HospitalList(int hospid, String hospname, String hospCity, String hospState, int userid, String loginType)
    {
        this.hospid = hospid;
        this.hosp_name = hospname;
        this.hosp_city = hospCity;
        this.hosp_state = hospState;
        this.user_id = userid;
        this.login_type = loginType;
    }

    public HospitalList(int hosp_id, String hosp_name, String hosp_addrs, String hosp_city, String hosp_state,
                        String hosp_country, int doc_id, String login_type) {
        this.hospid = hosp_id;
        this.hosp_name = hosp_name;
        this.hosp_address = hosp_addrs;
        this.hosp_city = hosp_city;
        this.hosp_state = hosp_state;
        this.hosp_country = hosp_country;
        this.user_id = doc_id;
        this.login_type = login_type;
    }


    public int getHospitalId() { return hospid; }
    public void setHospitalId(int hospid) {
        this.hospid = hospid;
    }

    public int getHospitalUserId() { return user_id; }
    public void setHospitalUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getHospitalLoginType() {
        return login_type;
    }
    public void setHospitalLoginType(String login_type) {
        this.login_type = login_type;
    }

    public String getHospitalName() {
        return hosp_name;
    }
    public void setHospitalName(String hosp_name) {
        this.hosp_name = hosp_name;
    }

    public String getHospitalCity() { return hosp_city; }
    public void setHospitalCity(String hosp_city) {
        this.hosp_city = hosp_city;
    }

    public String getHospitalState() { return hosp_state; }
    public void setHospitalState(String hosp_state) {
        this.hosp_state = hosp_state;
    }

    public String getHospitalAddress() {
        return hosp_address;
    }
    public void setHospitalAddress(String hosp_address) {
        this.hosp_address = hosp_address;
    }

    public String getHospitalCountry() {
        return hosp_country;
    }
    public void setHospitalCountry(String hosp_country) {
        this.hosp_country = hosp_country;
    }

}
