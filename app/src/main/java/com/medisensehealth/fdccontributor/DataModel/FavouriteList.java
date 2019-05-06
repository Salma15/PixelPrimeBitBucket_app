package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by lenovo on 07-04-2017.
 */

public class FavouriteList {

    int fav_id, refid, doc_age, spec_id;
    String doc_name, doc_exp, spec_name, doc_photo, doc_qualification, doc_logintype;

    public FavouriteList() {

    }

    public FavouriteList(int favid, int refid, String refname, int ref_age, String ref_exp, String doc_photo,String doc_qualification, int spec_id, String spec_name, int favourite, String doc_logintype)
    {
        this.fav_id = favid;
        this.refid = refid;
        this.doc_name = refname;
        this.doc_exp = ref_exp;
        this.doc_age = ref_age;
        this.spec_id = spec_id;
        this.spec_name = spec_name;
        this.doc_photo = doc_photo;
        this.doc_qualification= doc_qualification;
        this.doc_logintype = doc_logintype;
    }

    public FavouriteList(int favid, int refid, String refname, int ref_age, String ref_exp, String doc_photo,String doc_qualification, int spec_id, String spec_name, String doc_logintype)
    {
        this.fav_id = favid;
        this.refid = refid;
        this.doc_name = refname;
        this.doc_exp = ref_exp;
        this.doc_age = ref_age;
        this.spec_id = spec_id;
        this.spec_name = spec_name;
        this.doc_photo = doc_photo;
        this.doc_qualification= doc_qualification;
        this.doc_logintype = doc_logintype;
    }

    public int getFavouriteId() { return fav_id; }
    public void setFavouriteId(int fav_id) {
        this.fav_id = fav_id;
    }

    public int getFavDoctorId() { return refid; }
    public void setFavDoctorId(int refid) {
        this.refid = refid;
    }

    public String getFavDoctorName() {
        return doc_name;
    }
    public void setFavDoctorName(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getFavDoctorExperience() {
        return doc_exp;
    }
    public void setFavDoctorExperience(String doc_exp) {
        this.doc_exp = doc_exp;
    }

    public int getFavDoctorAge() { return doc_age; }
    public void setFavDoctorAge(int doc_age) {
        this.doc_age = doc_age;
    }

    public int getFavDoctorSpecializationId() { return spec_id; }
    public void setFavDoctorSpecializationId(int spec_id) {
        this.spec_id = spec_id;
    }

    public String getFavDoctorSpecificationName() {
        return spec_name;
    }
    public void setFavDoctorSpecificationName(String spec_name) {
        this.spec_name = spec_name;
    }

    public String getFavDoctorPhoto() {
        return doc_photo;
    }
    public void setFavDoctorPhoto(String doc_photo) {
        this.doc_photo = doc_photo;
    }

    public String getFavDoctorQualification() {
        return doc_qualification;
    }
    public void setFavDoctorQualification(String doc_qualification) {
        this.doc_qualification = doc_qualification;
    }

//    public int getFavDoctorFavoutite() { return favourite; }
//    public void setFavDoctorFavourite(int favourite) {
//        this.favourite = favourite;
//    }

    public String getFavDoctorLoginType() { return doc_logintype; }
    public void setFavDoctorLoginType(String doc_logintype) {
        this.doc_logintype = doc_logintype;
    }
}
