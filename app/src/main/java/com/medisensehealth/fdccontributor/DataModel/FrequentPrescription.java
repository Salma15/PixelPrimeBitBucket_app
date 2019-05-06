package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by HP on 01-06-2018.
 */

public class FrequentPrescription implements Serializable {

    public static final String FREQ_PRESCRIPTION_TABLE_NAME = "PRESCRIPTIONS";
    public static final String FREQ_PRESCRIPTION_AUTO_ID = "ID";
    public static final String FREQ_PRESCRIPTION_FREQ_ID = "PRESCRIPTION_FREQ_ID";
    public static final String FREQ_PRESCRIPTION_PRODUCT_ID = "PRESCRIPTION_ID";
    public static final String FREQ_PRESCRIPTION_TRADE_NAME = "PRESCRIPTION_TRADE_NAME";
    public static final String FREQ_PRESCRIPTION_GENERIC_ID = "PRESCRIPTION_GENERIC_ID";
    public static final String FREQ_PRESCRIPTION_GENERIC_NAME = "PRESCRIPTION_GENERIC_NAME";
    public static final String FREQ_PRESCRIPTION_DOASGE = "PRESCRIPTION_DOSAGE";
    public static final String FREQ_PRESCRIPTION_TIMING = "PRESCRIPTION_TIMINGS";
    public static final String FREQ_PRESCRIPTION_DURATION = "PRESCRIPTION_DURATION";
    public static final String FREQ_PRESCRIPTION_DOCID = "PRESCRIPTION_DOCID";
    public static final String FREQ_PRESCRIPTION_DOCTYPE = "PRESCRIPTION_DOCTYPE";
    public static final String FREQ_PRESCRIPTION_FREQUENT_COUNT = "PRESCRIPTION_FREQ_COUNT";
    public static final String FREQ_PRESCRIPTION_USERID = "PRESCRIPTION_USERID";
    public static final String FREQ_PRESCRIPTION_USER_LOGINTYPE = "PRESCRIPTION_USER_LOGINTYPE";

    public static final String REPEAT_PRESCRIPTION_TABLE_NAME = "REPEAT_PRESCRIPTIONS";
    public static final String REPEAT_PRESCRIPTION_AUTO_ID = "ID";
    public static final String REPEAT_PRESCRIPTION_FREQ_ID = "REPEAT_PRESCRIPTION_FREQ_ID";
    public static final String REPEAT_PRESCRIPTION_PRODUCT_ID = "REPEAT_PRESCRIPTION_ID";
    public static final String REPEAT_PRESCRIPTION_TRADE_NAME = "REPEAT_PRESCRIPTION_TRADE_NAME";
    public static final String REPEAT_PRESCRIPTION_GENERIC_ID = "REPEAT_PRESCRIPTION_GENERIC_ID";
    public static final String REPEAT_PRESCRIPTION_GENERIC_NAME = "REPEAT_PRESCRIPTION_GENERIC_NAME";
    public static final String REPEAT_PRESCRIPTION_DOASGE = "REPEAT_PRESCRIPTION_DOSAGE";
    public static final String REPEAT_PRESCRIPTION_TIMING = "REPEAT_PRESCRIPTION_TIMINGS";
    public static final String REPEAT_PRESCRIPTION_DURATION = "REPEAT_PRESCRIPTION_DURATION";
    public static final String REPEAT_PRESCRIPTION_DOCID = "REPEAT_PRESCRIPTION_DOCID";
    public static final String REPEAT_PRESCRIPTION_DOCTYPE = "REPEAT_PRESCRIPTION_DOCTYPE";
    public static final String REPEAT_PRESCRIPTION_FREQUENT_COUNT = "REPEAT_PRESCRIPTION_FREQ_COUNT";
    public static final String REPEAT_PRESCRIPTION_USERID = "REPEAT_PRESCRIPTION_USERID";
    public static final String REPEAT_PRESCRIPTION_USER_LOGINTYPE = "REPEAT_PRESCRIPTION_USER_LOGINTYPE";

    int presc_frqID, pp_id, generic_id, doc_id, doc_type, freq_count, user_id, prod_priority;
    String trade_name, generic_name, dosage, timings, duration, loginType, presc_given_date;

    public FrequentPrescription(int freq_medicine_id, int pp_id, String med_trade_name, int generic_id, String med_generic_name,
                                String med_frequency, String med_timing, String med_duration, int doc_id, int doc_type,
                                int freq_count, int user_id, String user_login_type) {
        this.presc_frqID = freq_medicine_id;
        this.pp_id = pp_id;
        this.trade_name = med_trade_name;
        this.generic_id = generic_id;
        this.generic_name = med_generic_name;
        this.dosage = med_frequency;
        this.timings = med_timing;
        this.duration = med_duration;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.freq_count = freq_count;
        this.user_id = user_id;
        this.loginType = user_login_type;

    }

    public FrequentPrescription() {

    }

    public FrequentPrescription(int freq_medicine_id, int pp_id, String med_trade_name, int generic_id,
                                String pharma_generic, int pharma_priority, String med_frequency, String med_timing,
                                String med_duration, int doc_id, int doc_type, int freq_count, int user_id, String user_login_type) {
        this.presc_frqID = freq_medicine_id;
        this.pp_id = pp_id;
        this.trade_name = med_trade_name;
        this.generic_id = generic_id;
        this.generic_name = pharma_generic;
        this.prod_priority = pharma_priority;
        this.dosage = med_frequency;
        this.timings = med_timing;
        this.duration = med_duration;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.freq_count = freq_count;
        this.user_id = user_id;
        this.loginType = user_login_type;

    }

    public FrequentPrescription(int episode_prescription_id, int pp_id, String prescription_trade_name, int generic_id,
                                String prescription_generic_name, String prescription_frequency, String timing, String duration,
                                int doc_id, int doc_type, int freq_count, int user_id, String user_login_type,
                                String prescription_date_time) {
        this.presc_frqID = episode_prescription_id;
        this.pp_id = pp_id;
        this.trade_name = prescription_trade_name;
        this.generic_id = generic_id;
        this.generic_name = prescription_generic_name;
        this.dosage = prescription_frequency;
        this.timings = timing;
        this.duration = duration;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.freq_count = freq_count;
        this.user_id = user_id;
        this.loginType = user_login_type;
        this.presc_given_date = prescription_date_time;
    }

    public int getPrescFreqId() { return presc_frqID; }
    public void setPrescFreqId(int presc_frqID) { this.presc_frqID = presc_frqID; }

    public int getPrescriptionId() { return pp_id; }
    public void setPresciptionId(int pp_id) { this.pp_id = pp_id; }

    public int getGenericId() { return generic_id; }
    public void setGenericId(int generic_id) { this.generic_id = generic_id; }

    public int getDocId() { return doc_id; }
    public void setDocId(int doc_id) { this.doc_id = doc_id; }

    public int getDocType() { return doc_type; }
    public void setDocType(int doc_type) { this.doc_type = doc_type; }

    public int getFreqCount() { return freq_count; }
    public void setFreqCount(int freq_count) { this.freq_count = freq_count; }

    public int getUserId() { return user_id; }
    public void setUserId(int user_id) { this.user_id = user_id; }

    public String getLoginType() { return loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }

    public String getTradeName() { return trade_name; }
    public void setTradeName(String trade_name) { this.trade_name = trade_name; }

    public String getGenericName() { return generic_name; }
    public void setGenericName(String generic_name) { this.generic_name = generic_name; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getTimings() { return timings; }
    public void setTimings(String timings) { this.timings = timings; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public int getPrescPriority() { return prod_priority; }
    public void setPrescPriority(int prod_priority) { this.prod_priority = prod_priority; }

    public String getPrescGivenDate() { return presc_given_date; }
    public void setPrescGivenDate(String presc_given_date) { this.presc_given_date = presc_given_date; }

}
