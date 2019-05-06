package com.medisensehealth.fdccontributor.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lenovo on 05/01/2018.
 */

public class ShareadPreferenceClass {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public ShareadPreferenceClass(Context mContext) {
        this.context = mContext;
        sharedPreferences = context.getSharedPreferences(HCConstants.PREF_FDC_SYSTEM, Context.MODE_PRIVATE);

    }

    //----Start Set and Get loginPre---
    public void loginPre(String login_type, String Doctor_refName, String Doctor_type, String Doctor_refWeb, String Doctor_gender, String Doctor_qualification,
                         String Doctor_city, String Doctor_state, String Doctor_country, String Doctor_reference_address,
                         String Doctor_refExp, String Doctor_interest, String Doctor_research, String Doctor_contribute, String Doctor_pub,
                         String Doctor_inop_cost,String Doctor_onop_cost, String Doctor_cons_charges,String Doctor_keyword,String Doctor_contactNumber,String Doctor_photo,
                         String Doctor_password, int Doctor_refId,int Doctor_totalReferred, int Doctor_total_responsed, int Doctor_typeVal, int Doctor_spec,
                         int Doctor_age, int Doctor_anonymousStatus, int Doctor_companyId) {
        if (sharedPreferences != null) {

            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, login_type);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, Doctor_refName);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_TYPE, Doctor_type);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_REF_WEB, Doctor_refWeb);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_GEN, Doctor_gender);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_QUAL, Doctor_qualification);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_CITY, Doctor_city);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_STATE, Doctor_state);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_COUNTRY, Doctor_country);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_ADDRESS, Doctor_reference_address);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_REF_EXP, Doctor_refExp);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_INTEREST, Doctor_interest);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_RESEARCH, Doctor_research);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_CONTRIBUTE, Doctor_contribute);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_PUB, Doctor_pub);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_INOP_COST, Doctor_inop_cost);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_ONOP_COST, Doctor_onop_cost);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_CONS_CHARGE, Doctor_cons_charges);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_KEYWORDS, Doctor_keyword);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_CONTACT_NUM, Doctor_contactNumber);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_PHOTO, Doctor_photo);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_PASSWORD, Doctor_password);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, Doctor_refName);
            editor.putInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID,Doctor_refId);
            editor.putInt(HCConstants.PREF_LOGINACTIVITY_DOC_TOATALREFERED,Doctor_totalReferred);
            editor.putInt(HCConstants.PREF_LOGINACTIVITY_DOC_TOTALRESPONDED,Doctor_total_responsed);
            editor.putInt(HCConstants.PREF_LOGINACTIVITY_DOC_TYPEVAL,Doctor_typeVal);
            editor.putInt(HCConstants.PREF_LOGINACTIVITY_DOC_SPEC,Doctor_spec);
            editor.putInt(HCConstants.PREF_LOGINACTIVITY_DOC_AGE,Doctor_age);
            editor.putInt(HCConstants.PREF_LOGINACTIVITY_DOC_ANONYMOUS_STATUS,Doctor_anonymousStatus);
            editor.putInt(HCConstants.PREF_LOGINACTIVITY_DOC_COMPANYID,Doctor_companyId);
            editor.commit();
        }
    }
    //----End Set and Get loginPre---

    //----Start Set and Get hospitalDoctor_PreLogin---
    public void hospitalDoctor_PreLogin(String login_type, String Doctor_refName, String Doctor_type, String Doctor_refWeb, String Doctor_gender, String Doctor_qualification,
                                        String Doctor_city, String Doctor_state, String Doctor_country, String Doctor_reference_address,
                                        String Doctor_refExp, String Doctor_interest, String Doctor_research, String Doctor_contribute, String Doctor_pub,
                                        String Doctor_inop_cost,String Doctor_onop_cost, String Doctor_cons_charges,String Doctor_keyword,String Doctor_contactNumber,String Doctor_photo,
                                        String Doctor_password, int Doctor_refId,int Doctor_totalReferred, int Doctor_total_responsed, int Doctor_typeVal, int Doctor_spec,
                                        int Doctor_age, int Doctor_anonymousStatus, int Doctor_companyId, String Doctor_gcmtoken_string, String user_encryptID, String specializaton_name ) {
        if (sharedPreferences != null) {

            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, login_type);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, Doctor_refName);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_TYPE, Doctor_type);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_REF_WEB, Doctor_refWeb);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_GEN, Doctor_gender);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_QUAL, Doctor_qualification);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_CITY, Doctor_city);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_STATE, Doctor_state);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_COUNTRY, Doctor_country);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_ADDRESS, Doctor_reference_address);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_REF_EXP, Doctor_refExp);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_INTEREST, Doctor_interest);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_RESEARCH, Doctor_research);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_CONTRIBUTE, Doctor_contribute);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_PUB, Doctor_pub);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_INOP_COST, Doctor_inop_cost);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_ONOP_COST, Doctor_onop_cost);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_CONS_CHARGE, Doctor_cons_charges);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_KEYWORDS, Doctor_keyword);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_CONTACT_NUM, Doctor_contactNumber);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_PHOTO, Doctor_photo);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_PASSWORD, Doctor_password);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, Doctor_refName);
            editor.putInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID,Doctor_refId);
            editor.putInt(HCConstants.PREF_LOGINACTIVITY_DOC_TOATALREFERED,Doctor_totalReferred);
            editor.putInt(HCConstants.PREF_LOGINACTIVITY_DOC_TOTALRESPONDED,Doctor_total_responsed);
            editor.putInt(HCConstants.PREF_LOGINACTIVITY_DOC_TYPEVAL,Doctor_typeVal);
            editor.putInt(HCConstants.PREF_LOGINACTIVITY_DOC_SPEC,Doctor_spec);
            editor.putInt(HCConstants.PREF_LOGINACTIVITY_DOC_AGE,Doctor_age);
            editor.putInt(HCConstants.PREF_LOGINACTIVITY_DOC_ANONYMOUS_STATUS,Doctor_anonymousStatus);
            editor.putInt(HCConstants.PREF_LOGINACTIVITY_DOC_COMPANYID,Doctor_companyId);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_GCM_TOKENID,Doctor_gcmtoken_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_ENCRYPT_USERID,user_encryptID);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_SPECIALIZATION_NAME,specializaton_name);
            editor.commit();
        }
    }
    //----End Set and Get hospitalDoctor_PreLogin---

    //----Start Set and Get referringPartner_PreLogin---
    public void referPartnerPreLogin(String login_type, int partner_id_int, String partner_name_string, String history_string, String Address_string, String Email_id_string, String Email_id1_string,
                                String Email_id2_string, String contact_person_string, String person_position_string, String landline_num_string,
                                String cont_num1_string, String cont_num2_string, String website_string, String location_string, String state_string,
                                String country_string,String Type_string, String password_string,String reg_date_string,String partner_logo_string,
                                String partner_gcmtoken_string, String user_encryptID, String specializaton_name) {
        if (sharedPreferences != null) {

            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, login_type);
            editor.putInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, partner_id_int);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, partner_name_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_HISTORY, history_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_ADDRESS, Address_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_EMAIL, Email_id_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_EMAIL1, Email_id1_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_EMAIL2, Email_id2_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_CONTACTPERSON, contact_person_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_PERPOSITION, person_position_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_LANDLINE, landline_num_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_CONTACTNUM1, cont_num1_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_CONTACTNUM2, cont_num2_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_WEBSITE, website_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_LOCATION, location_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_STATE, state_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_COUNTRY, country_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_TYPE, Type_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_PASSWORD, password_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_REGDATE, reg_date_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_LOGO, partner_logo_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_GCM_TOKENID, partner_gcmtoken_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_ENCRYPT_USERID,user_encryptID);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_PART_SPECIALIZATION_NAME,specializaton_name);
            editor.commit();
        }
    }
    //----End Set and Get referringPartner_PreLogin---

    //----Start Set and Get marketingPerson_PreLogin---
    public void marketingPersonPreLogin(String login_type, int market_id_int, String marketing_name_string, int market_hosp_id,
                                        String market_Mobile_string, String market_Email_string,
                                     String market_Password_string, String market_GCMToken_string) {
        if (sharedPreferences != null) {

            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE, login_type);
            editor.putInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, market_id_int);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, marketing_name_string);
            editor.putInt(HCConstants.PREF_LOGINACTIVITY_MARKET_HOSPITALID, market_hosp_id);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_MARKET_MOBILE, market_Mobile_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_MARKET_EMAIL, market_Email_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_MARKET_PASSWORD, market_Password_string);
            editor.putString(HCConstants.PREF_LOGINACTIVITY_MARKET_GCM_TOKENID, market_GCMToken_string);

            editor.commit();
        }
    }
    //----End Set and Get marketingPerson_PreLogin---

    public void clearData() {
        if (sharedPreferences != null) {

            editor = sharedPreferences.edit();
            editor.clear().commit();

        }
    }

    public SharedPreferences getSharedPreferences(Context mContext) {
        this.context = mContext;
        sharedPreferences = context.getSharedPreferences(HCConstants.PREF_FDC_SYSTEM, Context.MODE_PRIVATE);

        return sharedPreferences;
    }

    public void setBlogDetailsList(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_BLOG_LISTS, json);
            editor.commit();
        }
    }

    public void clearBlogDetailsLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_BLOG_LISTS).commit();

        }
    }

    public void setAppointmentList(String json_appt) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_APPOINTMENT_LISTS, json_appt);
            editor.commit();
        }
    }

    public void clearAppointmentLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_APPOINTMENT_LISTS).apply();

        }
    }

    public void setMyPatientList(String json_appt) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_MYPATIENT_LISTS, json_appt);
            editor.commit();
        }
    }
    public void clearMyPatientLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_MYPATIENT_LISTS).apply();

        }
    }

    /* Start of Patient Details */
    public void setPatientDetails(String json_patient_info) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_PATIENTS_DETAILS, json_patient_info);
            editor.commit();
        }
    }
    public void clearPatientDetails() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_PATIENTS_DETAILS).apply();

        }
    }
    /* End of patient info */

    /* Start of Doctors Details */
    public void setDoctorsDetails(String json_doc_info) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_DOCTORS_DETAILS, json_doc_info);
            editor.commit();
        }
    }
    public void clearDoctorsDetails() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_DOCTORS_DETAILS).apply();

        }
    }
    /* End of Doctors info */


    /* Get Count of Screen screen lists */
    public void setCountList(String appointment_count, String mypatient_count, String cases_count, String doctors_count,
                             String blogs_count, String events_count, String jobs_count, String videos_count) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_COUNT_LISTS_APPOINT, appointment_count);
            editor.putString(HCConstants.PREF_COUNT_LISTS_MYPATIENT, mypatient_count);
            editor.putString(HCConstants.PREF_COUNT_LISTS_CASES, cases_count);
            editor.putString(HCConstants.PREF_COUNT_LISTS_DOCTORS, doctors_count);
            editor.putString(HCConstants.PREF_COUNT_LISTS_BLOGS, blogs_count);
            editor.putString(HCConstants.PREF_COUNT_LISTS_EVENTS, events_count);
            editor.putString(HCConstants.PREF_COUNT_LISTS_JOBS, jobs_count);
            editor.putString(HCConstants.PREF_COUNT_LISTS_VIDEOS, videos_count);
            editor.commit();
        }
    }

    public void clearCountLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_COUNT_LISTS_APPOINT).apply();
            editor.remove(HCConstants.PREF_COUNT_LISTS_MYPATIENT).apply();
            editor.remove(HCConstants.PREF_COUNT_LISTS_CASES).apply();
            editor.remove(HCConstants.PREF_COUNT_LISTS_DOCTORS).apply();
            editor.remove(HCConstants.PREF_COUNT_LISTS_BLOGS).apply();
            editor.remove(HCConstants.PREF_COUNT_LISTS_EVENTS).apply();
            editor.remove(HCConstants.PREF_COUNT_LISTS_JOBS).apply();
            editor.remove(HCConstants.PREF_COUNT_LISTS_VIDEOS).apply();
        }
    }

    public void setHomeFeedsTypes(String json_appt) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_HOME_FEEDS_TYPE, json_appt);
            editor.commit();
        }
    }

    public void clearHomeFeedsTypes() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_HOME_FEEDS_TYPE).apply();

        }
    }

    public void setMyPatientsTemplates(String json_templates) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_MYPATIENT_TEMPLATES, json_templates);
            editor.commit();
        }
    }

    public void clearMyPatientsTemplates() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_MYPATIENT_TEMPLATES).apply();

        }
    }

    public void setFeedsFilterBlogDetailsList(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_FEEDS_FILTER_BLOGLIST, json);
            editor.commit();
        }
    }

    public void clearFeedsFilterBlogDetailsLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_FEEDS_FILTER_BLOGLIST).commit();

        }
    }

    public void setFeedsFilterEventsDetailsList(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_FEEDS_FILTER_EVENTLIST, json);
            editor.commit();
        }
    }

    public void clearFeedsFilterEventsDetailsLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_FEEDS_FILTER_EVENTLIST).commit();

        }
    }

    public void setFeedsFilterVideoDetailsList(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_FEEDS_FILTER_VIDEOLIST, json);
            editor.commit();
        }
    }

    public void clearFeedsFilterVideoDetailsLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_FEEDS_FILTER_VIDEOLIST).commit();

        }
    }

    public void setFeedsFilterJobsDetailsList(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_FEEDS_FILTER_JOBSLIST, json);
            editor.commit();
        }
    }

    public void clearFeedsFilterJobsDetailsLists() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_FEEDS_FILTER_JOBSLIST).commit();

        }
    }

    public void setMyPatientEpisodes(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_MYPATIENT_EPISODE_LIST, json);
            editor.commit();
        }
    }

    public void clearsetMyPatientEpisodes() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_MYPATIENT_EPISODE_LIST).commit();

        }
    }

    public void setDocSpecializationsList(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_DOC_SPECIALIZATIONS, json);
            editor.commit();
        }
    }

    public void clearDocSpecializations() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_DOC_SPECIALIZATIONS).commit();

        }
    }

    public void setDocHoapitals(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_DOC_HOSPITALS, json);
            editor.commit();
        }
    }

    public void clearDocHoapitals() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_DOC_HOSPITALS).commit();

        }
    }

    public void setDocHoapitalID(int json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putInt(HCConstants.PREF_DOC_HOSPITAL_ID, json);
            editor.commit();
        }
    }

    public void clearDocHoapitalID() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_DOC_HOSPITAL_ID).commit();

        }
    }

    public void setDocReceptionistUsername(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_DOC_RECEPTIONIST_USERNAME, json);
            editor.commit();
        }
    }

    public void clearDocReceptionistUsername() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_DOC_RECEPTIONIST_USERNAME).commit();

        }
    }

    public void setDocReceptionistPassword(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_DOC_RECEPTIONIST_PASSWORD, json);
            editor.commit();
        }
    }

    public void clearDocReceptionistPassword() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_DOC_RECEPTIONIST_PASSWORD).commit();

        }
    }

    public void setDocSpecializationGroupID(int json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putInt(HCConstants.PREF_SPEC_GROUP_ID, json);
            editor.commit();
        }
    }

    public void clearDocSpecializationGroupID() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_SPEC_GROUP_ID).commit();

        }
    }

    public void setDiagnosticCentres(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_DIAGNOSTIC_CENTRES, json);
            editor.commit();
        }
    }

    public void clearDiagnosticCentres() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_DIAGNOSTIC_CENTRES).commit();

        }
    }

    public void setPharmaCentres(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_PHARMA_CENTRES, json);
            editor.commit();
        }
    }

    public void clearPharmaCentres() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_PHARMA_CENTRES).commit();

        }
    }

    public void setOtherSettings(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_OTHER_SETTINGS, json);
            editor.commit();
        }
    }

    public void clearOtherSettings() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_OTHER_SETTINGS).commit();

        }
    }

    public void setDocProfileImage(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_PHOTO, json);
            editor.commit();
        }
    }

    public void clearDocProfileImage() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_LOGINACTIVITY_DOC_PHOTO).commit();

        }
    }

    public void setDocProfileName(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, json);
            editor.commit();
        }
    }

    public void clearDocProfileName() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_LOGINACTIVITY_DOC_NAME).commit();

        }
    }

    public void setDocConsultationCharges(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_LOGINACTIVITY_DOC_CONS_CHARGE, json);
            editor.commit();
        }
    }

    public void clearDocConsultationCharges() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_LOGINACTIVITY_DOC_CONS_CHARGE).commit();

        }
    }

    public void setOpticalCentres(String json) {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.putString(HCConstants.PREF_OPTICAL_CENTRES, json);
            editor.commit();
        }
    }

    public void clearOpticalCentres() {
        if (sharedPreferences != null) {
            editor = sharedPreferences.edit();
            editor.remove(HCConstants.PREF_OPTICAL_CENTRES).commit();

        }
    }
}
