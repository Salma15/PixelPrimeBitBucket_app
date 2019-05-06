package com.medisensehealth.fdccontributor.DataModel;

import android.util.Log;


/**
 * Created by lenovo on 02-03-2017.
 */

public class PatientAttachmentHistory {
    int count, patient_id, attach_id;
    String attachment, view_types;

    public PatientAttachmentHistory() {
    }

    public PatientAttachmentHistory(int attach_id, String attach_name) {
        this.attach_id = attach_id;
        this.attachment = attach_name;
    }

    public PatientAttachmentHistory(int attach_id, String attachments, int patientid, String view_type) {
      //  this.count = count;
        this.patient_id = patientid;
        this.attach_id = attach_id;
        this.attachment = attachments;
        this.view_types = view_type;
    }

    public PatientAttachmentHistory(String attachments) {
        this.attachment = attachments;
    }

    public int getPatientId() { return patient_id; }
    public void setPatientId(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getAttachCount() { return count; }
    public void setAttachCount(int count) {
        this.count = count;
    }

    public int getAttachId() { return attach_id; }
    public void setAttachId(int attach_id) {
        this.attach_id = attach_id;
    }

    public String getAttachName() {
        return attachment;
    }
    public void setAttachName(String attachment) {
        this.attachment = attachment;
    }

    public String getAttachViewType() {
        return view_types;
    }
    public void setAttachViewType(String view_types) {
        this.view_types = view_types;
    }

}
