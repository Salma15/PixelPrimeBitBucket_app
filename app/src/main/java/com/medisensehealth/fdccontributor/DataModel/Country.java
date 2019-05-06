package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by lenovo on 24/06/2017.
 */

public class Country {

    /* Country Table Details*/
    public static final String COUNTRY_TABLE_NAME = "COUNTRY";
    public static final String COUNTRY_AUTO_ID = "ID";                     // Local Auto increment ID
    public static final String COUNTRY_ID = "COUNTRY_ID";
    public static final String COUNTRY_NAME = "COUNTRY_NAME";
    public static final String COUNTRY_CREATED_DATE = "COUNTRY_CREATED_DATE";
    public static final String COUNTRY_USER_ID = "COUNTRY_USER_ID";
    public static final String COUNTRY_LOGIN_TYPE = "COUNTRY_LOGIN_TYPE";
    public static final String COUNTRY_BSYNC = "COUNTRY_BSYNC";

    int country_id;
    String country_name;

    public int getCountryID() {
        return country_id;
    }
    public void setCountryID(int country_id) {
        this.country_id = country_id;
    }

    public String getCountryName() {
        return country_name;
    }
    public void setCountryName(String country_name) {
        this.country_name = country_name;
    }

    /* State Table Details*/
    public static final String STATE_TABLE_NAME = "STATE";
    public static final String STATE_AUTO_ID = "ID";                     // Local Auto increment ID
    public static final String STATE_ID = "STATE_ID";
    public static final String STATE_COUNTRY_ID = "STATE_COUNTRY_ID";
    public static final String STATE_NAME = "STATE_NAME";
    public static final String STATE_CREATED_DATE = "STATE_CREATED_DATE";
    public static final String STATE_USER_ID = "STATE_USER_ID";
    public static final String STATE_LOGIN_TYPE = "STATE_LOGIN_TYPE";
    public static final String STATE_BSYNC = "STATE_BSYNC";

    int state_id, state_country_id;
    String state_name;

    public int getStateID() {
        return state_id;
    }
    public void setStateID(int state_id) {
        this.state_id = state_id;
    }

    public int getStateCountryID() {
        return state_country_id;
    }
    public void setStateCountryID(int state_country_id) { this.state_country_id = state_country_id; }

    public String getStateName() {
        return state_name;
    }
    public void setStateName(String state_name) {
        this.state_name = state_name;
    }

}
