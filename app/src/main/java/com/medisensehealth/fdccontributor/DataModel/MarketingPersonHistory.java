package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by lenovo on 30-03-2017.
 */

public class MarketingPersonHistory {
    private int person_id,	hosp_id;
    private String person_name,	person_mobile, person_email, password, gcm_tokenid;

    public MarketingPersonHistory(int person_id,String person_name,int hosp_id, String person_mobile,String person_email,
                         String password,String gcm_tokenid )
    {
        this.person_id = person_id;
        this.hosp_id = hosp_id;
        this.person_name = person_name;
        this.person_mobile = person_mobile;
        this.person_email = person_email;
        this.password = password;
        this.gcm_tokenid = gcm_tokenid;
    }

    public int getMarketPersonId() { return person_id; }
    public void setMarketPersonId(int person_id) {
        this.person_id = person_id;
    }

    public int getMarketPersonHospitalId() { return hosp_id; }
    public void setMarketPersonHospitalId(int hosp_id) {
        this.hosp_id = hosp_id;
    }

    public String getMarketPersonName() { return person_name; }
    public void setRMarketPersonName(String person_name) {
        this.person_name = person_name;
    }

    public String getMarketPersonMobile() { return person_mobile; }
    public void setRMarketPersonMobile(String person_mobile) {
        this.person_mobile = person_mobile;
    }

    public String getMarketPersonEmail() { return person_email; }
    public void setRMarketPersonEmail(String person_email) {
        this.person_email = person_email;
    }

    public String getMarketPersonPassword() { return password; }
    public void setRMarketPersonPassword(String password) { this.password = password; }

    public String getMarketPersonGCMTokenId() { return gcm_tokenid; }
    public void setRMarketPersonGCMTokenId(String gcm_tokenid) { this.gcm_tokenid = gcm_tokenid; }

}
