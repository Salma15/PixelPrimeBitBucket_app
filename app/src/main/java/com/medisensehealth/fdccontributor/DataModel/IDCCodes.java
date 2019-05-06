package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by HP on 31-03-2018.
 */

public class IDCCodes {

    String idc_id;
    private String idc_name;

    public IDCCodes() {
    }

    public IDCCodes(String idc_id, String idc_name) {
        this.idc_id = idc_id;
        this.idc_name = idc_name;
    }

    public String getIDCID() {
        return idc_id;
    }
    public void setIDCID(String idc_id) {
        this.idc_id = idc_id;
    }

    public String getIDCName() {
        return idc_name;
    }
    public void setIDCName(String idc_name) {
        this.idc_name = idc_name;
    }
}
