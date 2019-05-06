package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by HP on 14-07-2018.
 */

public class ViewReports {

    int report_id, patient_id, user_id, user_type;
    String report_folder, attachments, date_added, user_name;

    public ViewReports(int report_id, int patient_id, String report_folder, String attachments,
                       int user_id, int user_type, String date_added, String username) {
        this.report_id = report_id;
        this.patient_id = patient_id;
        this.report_folder = report_folder;
        this.attachments = attachments;
        this.user_id = user_id;
        this.user_type = user_type;
        this.date_added = date_added;
        this.user_name = username;
    }

    public ViewReports(int reportID, String attachments, int patientID, String report_folder) {
        this.report_id = reportID;
        this.attachments = attachments;
        this.patient_id = patientID;
        this.report_folder = report_folder;
    }

    public int getReportID() { return report_id; }
    public void setReportID(int report_id) {
        this.report_id = report_id;
    }

    public int getPatientID() { return patient_id; }
    public void setPatientID(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getUserID() { return user_id; }
    public void setUserID(int user_id) {
        this.user_id = user_id;
    }

    public int getUserType() { return user_type; }
    public void setUserType(int user_type) {
        this.user_type = user_type;
    }

    public String getReportFolder() { return report_folder; }
    public void setReportFolder(String report_folder) {
        this.report_folder = report_folder;
    }

    public String getAttachments() { return attachments; }
    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getDateAdded() { return date_added; }
    public void setDateAdded(String date_added) {
        this.date_added = date_added;
    }

    public String getUserName() { return user_name; }
    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

}
