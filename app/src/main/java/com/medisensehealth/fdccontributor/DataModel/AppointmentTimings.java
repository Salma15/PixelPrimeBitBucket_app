package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by lenovo on 07/09/2017.
 */

public class AppointmentTimings {

    String appt_date, appt_date_id;

    public AppointmentTimings(String appt_date, String appt_id) {
        this.appt_date = appt_date;
        this.appt_date_id = appt_id;
    }

    public String getApptDate() {
        return appt_date;
    }
    public void setApptDate(String appt_date) { this.appt_date = appt_date; }

    public String getApptDateID() {
        return appt_date_id;
    }
    public void setApptDateID(String appt_date_id) { this.appt_date_id = appt_date_id; }

}
