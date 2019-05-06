package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by lenovo on 28/08/2017.
 */

public class OrganiserList {

    int org_id;
    String org_name, org_photo;

    public OrganiserList(int ref_id, String ref_name, String doc_photo) {
        this.org_id = ref_id;
        this.org_name = ref_name;
        this.org_photo = doc_photo;
    }

    public int getOrganiserID() {
        return org_id;
    }
    public void setOrganiserID(int org_id) { this.org_id = org_id; }

    public String getOrganiserName() {
        return org_name;
    }
    public void setOrganiserName(String org_name) { this.org_name = org_name; }

    public String getOrganiserPhoto() {
        return org_photo;
    }
    public void setOrganiserPhoto(String org_photo) { this.org_photo = org_photo; }

}
