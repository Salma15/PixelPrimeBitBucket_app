package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by hp on 16/02/2017.
 */

public class DoctorHistory {

    private int ref_id,Total_Referred,Tot_responded,doc_type_val,doc_spec,doc_age,anonymous_status,company_id;
    private String ref_name,doc_type,ref_web,doc_gen,doc_qual,doc_city,doc_state,doc_country,
            ref_address,ref_exp,doc_interest,doc_research,doc_contribute,doc_pub,in_op_cost,on_op_cost,cons_charge,
            doc_keywords,contact_num,doc_photo,doc_password,doc_gcmtoken, user_encryprty_id, specialization_name;

    public DoctorHistory(int ref_id,String ref_name,String doc_type,String ref_web,String doc_gen,String doc_qual,
                         String doc_city,String doc_state,String doc_country,String ref_address,String ref_exp,
                         String doc_interest,String doc_research,String doc_contribute,String doc_pub,String in_op_cost,
                         String on_op_cost,String cons_charge,String doc_keywords,String contact_num,String doc_photo,
                         String doc_password,int Total_Referred,int Tot_responded,int doc_type_val,int doc_spec,
                         int doc_age,int anonymous_status, String doc_gcmtoken, String encrypt_id, String spec_name) {
        this.ref_id = ref_id;
        this.Total_Referred = Total_Referred;
        this.Tot_responded = Tot_responded;
        this.doc_type_val = doc_type_val;
        this.doc_spec = doc_spec;
        this.doc_age = doc_age;
        this.anonymous_status = anonymous_status;
        this.company_id = company_id;
        this.ref_name = ref_name;
        this.doc_type = doc_type;
        this.ref_web = ref_web;this.doc_gen = doc_gen;this.doc_qual = doc_qual;this.doc_city = doc_city;
        this.doc_state = doc_state;this.doc_country = doc_country;this.ref_address = ref_address;this.ref_exp = ref_exp;
        this.doc_interest = doc_interest;this.doc_research = doc_research;this.doc_contribute = doc_contribute;this.doc_pub = doc_pub;
        this.in_op_cost = in_op_cost;this.on_op_cost = on_op_cost;this.cons_charge = cons_charge;this.doc_keywords = doc_keywords;
        this.contact_num = contact_num;this.doc_photo = doc_photo;this.doc_password = doc_password;
        this.doc_gcmtoken = doc_gcmtoken;
        this.user_encryprty_id = encrypt_id;
        this.specialization_name = spec_name;
    }

    public DoctorHistory(int ref_id,String contact_num,String doc_password) {
        this.ref_id = ref_id;
        this.doc_password = doc_password;
        this.contact_num = contact_num;
    }

    public String getRefSpecializationName() { return specialization_name; }
    public void setRefSpecializationName(String specialization_name) {
        this.specialization_name = specialization_name;
    }

    public String getRefEncyptID() { return user_encryprty_id; }
    public void setRefEncyptID(String user_encryprty_id) {
        this.user_encryprty_id = user_encryprty_id;
    }

    public int getRefId() { return ref_id; }
    public void setRefId(int ref_id) {
        this.ref_id = ref_id;
    }

    public String getRefname() { return ref_name; }
    public void setRefname(String ref_name) {
        this.ref_name = ref_name;
    }

    public String getDocType() {
        return doc_type;
    }
    public void setDocType(String doc_type) {
        this.doc_type = doc_type;
    }

    public String getRefweb() {
        return ref_web;
    }
    public void setRefweb(String ref_web) {
        this.ref_web = ref_web;
    }

    public String getDocgen() {
        return doc_gen;
    }
    public void setDocgen(String doc_gen) {
        this.doc_gen = doc_gen;
    }

    public String getDocqual() {
        return doc_qual;
    }
    public void setDocqual(String doc_qual) {
        this.doc_qual = doc_qual;
    }

    public String getDoccity() {
        return doc_city;
    }
    public void setDoccity(String doc_city) {
        this.doc_city = doc_city;
    }

    public String getDocstate() {
        return doc_state;
    }
    public void setDocstate(String doc_state) { this.doc_state = doc_state; }

    public String getDocscountry() {
        return doc_country;
    }
    public void setDoccountry(String doc_country) { this.doc_country = doc_country; }

    public String getRefaddress() {
        return ref_address;
    }
    public void setRefaddress(String ref_address) { this.ref_address = ref_address; }

    public String getRefexp() {
        return ref_exp;
    }
    public void setRefexp(String ref_exp) { this.ref_exp = ref_exp; }

    public String getDocinterest() {
        return doc_interest;
    }
    public void setDocinterest(String doc_interest) { this.doc_interest = doc_interest; }

    public String getDocresearch() {
        return doc_research;
    }
    public void setDocresearch(String doc_research) { this.doc_research = doc_research; }

    public String getDoccontribute() {
        return doc_contribute;
    }
    public void setDoccontribute(String doc_contribute) { this.doc_contribute = doc_contribute; }

    public String getDocpub() {
        return doc_pub;
    }
    public void setDocpub(String doc_pub) { this.doc_pub = doc_pub; }

    public String getInopcost() {
        return in_op_cost;
    }
    public void setInopcost(String in_op_cost) { this.in_op_cost = in_op_cost; }

    public String getOnopcost() {
        return on_op_cost;
    }
    public void setOnopcost(String on_op_cost) { this.on_op_cost = on_op_cost; }

    public String getConscharge() {
        return cons_charge;
    }
    public void setConscharge(String cons_charge) { this.cons_charge = cons_charge; }

    public String getDockeywords() {
        return doc_keywords;
    }
    public void setDockeywords(String doc_keywords) { this.doc_keywords = doc_keywords; }

    public String getContactnum() {
        return contact_num;
    }
    public void setContactnum(String contact_num) { this.contact_num = contact_num; }

    public String getDocphoto() {
        return doc_photo;
    }
    public void setDocphoto(String doc_photo) { this.doc_photo = doc_photo; }

    public String getDocpassword() {
        return doc_password;
    }
    public void setDocpassword(String doc_password) { this.doc_password = doc_password; }

    public int getTotalreferred() {
        return Total_Referred;
    }
    public void setTotalreferred(int Total_Referred) { this.Total_Referred = Total_Referred; }

    public int getTotresponded() {
        return Tot_responded;
    }
    public void setTotresponded(int Tot_responded) { this.Tot_responded = Tot_responded; }

    public int getDoctypeval() {
        return doc_type_val;
    }
    public void setDoctypeval(int doc_type_val) { this.doc_type_val = doc_type_val; }

    public int getDocspec() {
        return doc_spec;
    }
    public void setDocspec(int doc_spec) { this.doc_spec = doc_spec; }

    public int getDocage() {
        return doc_age;
    }
    public void setDocage(int doc_age) { this.doc_age = doc_age; }

    public int getAnanymousstatus() {
        return anonymous_status;
    }
    public void setAnalymousstatus(int anonymous_status) { this.anonymous_status = anonymous_status; }

    public int getCompanyid() {
        return company_id;
    }
    public void setCompanyid(int company_id) { this.company_id = company_id; }

    public String getDocGCMTokenId() {
        return doc_gcmtoken;
    }
    public void setDocGCMTokenId(String doc_gcmtoken) { this.doc_gcmtoken = doc_gcmtoken; }
}
