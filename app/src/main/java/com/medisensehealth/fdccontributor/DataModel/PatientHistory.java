package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by lenovo on 28-02-2017.
 */

public class PatientHistory {
    int patient_id,weight,hyper_cond,diabetes_cond,medDept,status, gender;
    String name,age,email,marital_status,qualification,contact_person,profession,patient_mob,patient_loc,pat_state,
            pat_country,patient_addrs,patient_complaint,patient_desc,pat_query,date;

    int spec_id; String spec_name; int ref_id; String ref_name;
    String currentTreatDoc; String currentTreatHosp;

    public PatientHistory(int patient_id,String name,String age, int gender, String email, String marital_status, String qualification,
                          String contact_person, String profession, String patient_mob, String patient_loc, String pat_state,String pat_country,
                          String patient_addrs,String patient_complaint,String patient_desc, String pat_query, String date,int weight, int hyper_cond,
                          int diabetes_cond, int medDept, int status) {
        this.patient_id = patient_id;
        this.weight = weight;
        this.hyper_cond = hyper_cond;
        this.diabetes_cond = diabetes_cond;
        this.medDept = medDept;
        this.status = status;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.marital_status = marital_status;
        this.qualification = qualification;
        this.contact_person = contact_person;
        this.profession = profession;
        this.patient_mob = patient_mob;
        this.patient_loc = patient_loc;
        this.pat_state = pat_state;
        this.pat_country = pat_country;
        this.patient_addrs = patient_addrs;
        this.patient_complaint = patient_complaint;
        this.patient_desc = patient_desc;
        this.pat_query = pat_query;
        this.date = date;
    }

    public PatientHistory(int patient_id,String name,String age, int gender, String email, String marital_status, String qualification,
                          String contact_person, String profession, String patient_mob, String patient_loc, String pat_state,String pat_country,
                          String patient_addrs,String patient_complaint,String patient_desc, String pat_query, String date,int weight, int hyper_cond,
                          int diabetes_cond, int medDept, int status, int spec_id, String spec_name, int ref_id, String ref_name,
                          String currentTreatDoc, String currentTreatHosp) {
        this.patient_id = patient_id;
        this.weight = weight;
        this.hyper_cond = hyper_cond;
        this.diabetes_cond = diabetes_cond;
        this.medDept = medDept;
        this.status = status;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.marital_status = marital_status;
        this.qualification = qualification;
        this.contact_person = contact_person;
        this.profession = profession;
        this.patient_mob = patient_mob;
        this.patient_loc = patient_loc;
        this.pat_state = pat_state;
        this.pat_country = pat_country;
        this.patient_addrs = patient_addrs;
        this.patient_complaint = patient_complaint;
        this.patient_desc = patient_desc;
        this.pat_query = pat_query;
        this.date = date;
        this.spec_id = spec_id;
        this.spec_name = spec_name;
        this.ref_id = ref_id;
        this.ref_name = ref_name;
        this.currentTreatDoc = currentTreatDoc;
        this.currentTreatHosp = currentTreatHosp;
    }



    public int getPatientId() { return patient_id; }
    public void setPatientId(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getPatientWeight() { return weight; }
    public void setPatientWeight(int weight) {
        this.weight = weight;
    }

    public int getPatientHyperCond() { return hyper_cond; }
    public void setPatientHyperCond(int hyper_cond) {
        this.hyper_cond = hyper_cond;
    }

    public int getPatientDiabetes() { return diabetes_cond; }
    public void setPatientDiabetesd(int diabetes_cond) {
        this.diabetes_cond = diabetes_cond;
    }

    public int getPatientMedDept() { return medDept; }
    public void setPatientMedDept(int medDept) {
        this.medDept = medDept;
    }

    public int getPatientStatus() { return status; }
    public void setPatientStatus(int status) {
        this.status = status;
    }

    public String getPatientName() { return name; }
    public void setPatientName(String name) {
        this.name = name;
    }

    public String getPatientAge() { return age; }
    public void setPatientAge(String age) {
        this.age = age;
    }

    public int getPatientGender() { return gender; }
    public void setPatientGender(int gender) {
        this.gender = gender;
    }

    public String getPatientEmail() { return email; }
    public void setPatientEmail(String email) {
        this.email = email;
    }

    public String getPatientMaritalStatus() { return marital_status; }
    public void setPatientMaritalStatus(String marital_status) {this.marital_status = marital_status; }

    public String getPatientQualification() { return qualification; }
    public void setPatientQualification(String qualification) {this.qualification = qualification; }

    public String getPatientContactPerson() { return contact_person; }
    public void setPatientContactPerson(String contact_person) {this.contact_person = contact_person; }

    public String getPatientProfession() { return profession; }
    public void setPatientProfession(String profession) {this.profession = profession; }

    public String getPatientMobile() { return patient_mob; }
    public void setPatientMobile(String patient_mob) {this.patient_mob = patient_mob; }

    public String getPatientLocation() { return patient_loc; }
    public void setPatientLocation(String patient_loc) {this.patient_loc = patient_loc; }

    public String getPatientState() { return pat_state; }
    public void setPatientState(String pat_state) {this.pat_state = pat_state; }

    public String getPatientCountry() { return pat_country; }
    public void setPatientCountry(String pat_country) {this.pat_country = pat_country; }

    public String getPatientAddress() { return patient_addrs; }
    public void setPatientAddress(String patient_addrs) {this.patient_addrs = patient_addrs; }

    public String getPatientComplaint() { return patient_complaint; }
    public void setPatientComplaint(String patient_complaint) {this.patient_complaint = patient_complaint; }

    public String getPatientDesc() { return patient_desc; }
    public void setPatientDesc(String patient_desc) {this.patient_desc = patient_desc; }

    public String getPatientQuery() { return pat_query; }
    public void setPatientQuery(String pat_query) {this.pat_query = pat_query; }

    public String getPatientDate() { return date; }
    public void setPatientDate(String date) {this.date = date; }

    public int getSpecId() { return spec_id; }
    public void setSpecId(int spec_id) {
        this.spec_id = spec_id;
    }

    public String getSpecializationName() { return spec_name; }
    public void setSpecializationName(String spec_name) {this.spec_name = spec_name; }

    public int getRefId() { return ref_id; }
    public void setRefId(int ref_id) {
        this.ref_id = ref_id;
    }

    public String getRefDocName() { return ref_name; }
    public void setRefDocName(String ref_name) {this.ref_name = ref_name; }

    public String getCurrentTreatedDoctor() { return currentTreatDoc; }
    public void setCurrentTreatedDoctor(String currentTreatDoc) {this.currentTreatDoc = currentTreatDoc; }

    public String getCurrentTreatedHospital() { return currentTreatHosp; }
    public void setCurrentTreatedHospital(String currentTreatHosp) {this.currentTreatHosp = currentTreatHosp; }
}
