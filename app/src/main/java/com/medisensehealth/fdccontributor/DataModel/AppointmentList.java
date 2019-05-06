package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by lenovo on 17/08/2017.
 */

public class AppointmentList {

    int app_id, doc_id, dept_id, appt_userid;
    String transaction_id, visit_date, visit_time, patient_name, mobile, email, pay_status, visit_status, appt_logintype, ref_name;

    int token_id, token_number, patient_id, doc_type,hosp_id;
    String status, created_date;

    public AppointmentList(int app_id, String trans_id, int pref_doc, int dept, String visit_date, String visit_time,
                           String patient_name, String mobile, String email, String pay_status, String visit_status,
                           int user_id, String user_login_type, String doc_name) {
        this.app_id = app_id;
        this.transaction_id = trans_id;
        this.doc_id = pref_doc;
        this.dept_id = dept;
        this.visit_date = visit_date;
        this.visit_time = visit_time;
        this.patient_name = patient_name;
        this.mobile = mobile;
        this.email = email;
        this.pay_status = pay_status;
        this.visit_status = visit_status;
        this.appt_userid = user_id;
        this.appt_logintype = user_login_type;
        this.ref_name = doc_name;
    }

    public AppointmentList(int token_id, int token_no, int patient_id, String appoint_trans_id,
                           String patient_name, int doc_id, int doc_type, int hosp_id, String status, String app_date,
                           String app_time, String created_date, String email, String mobile) {

        this.token_id = token_id;
        this.token_number = token_no;
        this.patient_id = patient_id;
        this.transaction_id = appoint_trans_id;
        this.patient_name = patient_name;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.hosp_id = hosp_id;
        this.status = status;
        this.visit_date = app_date;
        this.visit_time = app_time;
        this.created_date = created_date;
        this.email = email;
        this.mobile = mobile;
    }

    public String getCreatedDate() {
        return created_date;
    }
    public void setCreatedDate(String created_date) { this.created_date = created_date; }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) { this.status = status; }

    public int getHospitalID() {
        return hosp_id;
    }
    public void setHospitalID(int hosp_id) { this.hosp_id = hosp_id; }

    public int getDocType() {
        return doc_type;
    }
    public void setDocType(int doc_type) { this.doc_type = doc_type; }

    public int getPatientID() {
        return patient_id;
    }
    public void setPatientID(int patient_id) { this.patient_id = patient_id; }

    public int getTokenNumber() {
        return token_number;
    }
    public void setTokenNumber(int token_number) { this.token_number = token_number; }

    public int getTokenID() {
        return token_id;
    }
    public void setTokenID(int token_id) { this.token_id = token_id; }

    public String getApptRefName() {
        return ref_name;
    }
    public void setApptRefName(String ref_name) { this.ref_name = ref_name; }

    public int getAppointID() {
        return app_id;
    }
    public void setAppointID(int app_id) { this.app_id = app_id; }

    public int getAppointDoctorID() {
        return doc_id;
    }
    public void setAppointDoctorID(int doc_id) { this.doc_id = doc_id; }

    public int getAppointDeptID() {
        return dept_id;
    }
    public void setAppointDeptID(int dept_id) { this.dept_id = dept_id; }

    public int getAppointUserId() {
        return appt_userid;
    }
    public void setAppointUserId(int appt_userid) { this.appt_userid = appt_userid; }

    public String getAppointLoginType() {
        return appt_logintype;
    }
    public void setAppointLoginType(String appt_logintype) { this.appt_logintype = appt_logintype; }

    public String getTransactionID() {
        return transaction_id;
    }
    public void setTransactionID(String transaction_id) { this.transaction_id = transaction_id; }

    public String getVisitDate() {
        return visit_date;
    }
    public void setVisitDate(String visit_date) { this.visit_date = visit_date; }

    public String getVisitTime() {
        return visit_time;
    }
    public void setVisitTime(String visit_time) { this.visit_time = visit_time; }

    public String getPatientName() {
        return patient_name;
    }
    public void setPatientName(String patient_name) { this.patient_name = patient_name; }

    public String getAppointMobile() {
        return mobile;
    }
    public void setAppointMobile(String mobile) { this.mobile = mobile; }

    public String getAppointEmail() { return email; }
    public void setAppointEmail(String email) { this.email = email; }

    public String getAppointPayStatus() {
        return pay_status;
    }
    public void setAppointPayStatus(String pay_status) { this.pay_status = pay_status; }

    public String getAppointVisitStatus() {
        return visit_status;
    }
    public void setAppointVisitStatus(String visit_status) { this.visit_status = visit_status; }

}
