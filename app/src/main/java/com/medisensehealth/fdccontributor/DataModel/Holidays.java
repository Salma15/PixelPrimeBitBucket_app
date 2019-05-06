package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by HP on 20-07-2018.
 */

public class Holidays {

    int holiday_id, doc_id, doc_type, hospital_id;
    String holiday_date, holiday_reason;

    public Holidays(int holiday_id, int doc_id, int doc_type, int hosp_id, String holiday_date, String reason) {
        this.holiday_id = holiday_id;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.hospital_id = hosp_id;
        this.holiday_date = holiday_date;
        this.holiday_reason = reason;
    }

    public int getHolidayId() { return holiday_id; }
    public void setHolidayId(int holiday_id) {
        this.holiday_id = holiday_id;
    }

    public int getDocId() { return doc_id; }
    public void setDocId(int doc_id) {
        this.doc_id = doc_id;
    }

    public int getDocType() { return doc_type; }
    public void setDocType(int doc_type) {
        this.doc_type = doc_type;
    }

    public int getHospitalID() { return hospital_id; }
    public void setHospitalID(int hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getHolidayDate() { return holiday_date; }
    public void setHolidayDate(String holiday_date) {
        this.holiday_date = holiday_date;
    }

    public String getHolidayReason() { return holiday_reason; }
    public void setHolidayReason(String holiday_reason) {
        this.holiday_reason = holiday_reason;
    }

}
