package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by HP on 10-07-2018.
 */

public class DrugAllery {

    public static final String DRUG_ALLERY_TABLE_NAME = "DRUG_ALLERY";
    public static final String DRUG_ALLERY_AUTO_ID = "ID";
    public static final String DRUG_ALLERY_ID = "ALLERGY_ID";
    public static final String DRUG_ALLERY_GENERIC_ID = "ALLERGY_GENERIC_ID";
    public static final String DRUG_ALLERY_GENERIC_NAME = "ALLERGY_GENERIC_NAME";
    public static final String DRUG_ALLERY_PATIENTID = "ALLERGY_PATIENT_ID";
    public static final String DRUG_ALLERY_DOCID = "ALLERGY_DOCID";
    public static final String DRUG_ALLERY_DOCTYPE = "PALLERGY_DOCTYPE";
    public static final String DRUG_ALLERY_USERID = "ALLERGY_USERID";
    public static final String DRUG_ALLERY_USER_LOGINTYPE = "ALLERGY_USER_LOGINTYPE";

    int allery_id, generic_id, patient_id, doc_id, doc_type, user_id;
    String loginType, generic_name;

    public DrugAllery(int allergy_id, int generic_id, String generic_name, int patient_id, int doc_id,
                      int doc_type, int user_id, String user_login_type) {
        this.allery_id = allergy_id;
        this.generic_id = generic_id;
        this.generic_name = generic_name;
        this.patient_id = patient_id;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public DrugAllery() {

    }

    public int getAllergyId() { return allery_id; }
    public void setAllergyId(int allery_id) { this.allery_id = allery_id; }

    public int getGenericId() { return generic_id; }
    public void setGenericId(int generic_id) { this.generic_id = generic_id; }

    public int getDocId() { return doc_id; }
    public void setDocId(int doc_id) { this.doc_id = doc_id; }

    public int getDocType() { return doc_type; }
    public void setDocType(int doc_type) { this.doc_type = doc_type; }

    public int getPatientId() { return patient_id; }
    public void setPatientId(int patient_id) { this.patient_id = patient_id; }

    public int getUserId() { return user_id; }
    public void setUserId(int user_id) { this.user_id = user_id; }

    public String getLoginType() { return loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }

    public String getGenericName() { return generic_name; }
    public void setGenericName(String generic_name) { this.generic_name = generic_name; }


}
