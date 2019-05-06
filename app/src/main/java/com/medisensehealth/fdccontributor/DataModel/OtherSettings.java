package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by HP on 27-07-2018.
 */

public class OtherSettings {

    int setting_id, doc_id, doc_type, payment_option, prescription_padheight,consultation_fee;
    String presc_header_height, presc_footer_height,  doc_logo, flash_message;

    public OtherSettings(int setting_id, int doc_id, int doc_type, int payment_opt, int prescription_pad,
                         String presc_pad_header_height, String presc_pad_footer_height, int before_consultation_fee,
                         String doc_logo, String doc_flash_msg) {
        this.setting_id = setting_id;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.payment_option = payment_opt;
        this.prescription_padheight = prescription_pad;
        this.presc_header_height = presc_pad_header_height;
        this.presc_footer_height = presc_pad_footer_height;
        this.consultation_fee = before_consultation_fee;
        this.doc_logo = doc_logo;
        this.flash_message = doc_flash_msg;
    }

    public int getSettingID() {
        return setting_id;
    }
    public void setSettingID(int setting_id) {
        this.setting_id = setting_id;
    }

    public int getDocID() {
        return doc_id;
    }
    public void setDocID(int doc_id) {
        this.doc_id = doc_id;
    }

    public int getDocType() {
        return doc_type;
    }
    public void setDocType(int doc_type) {
        this.doc_type = doc_type;
    }

    public int getPaymentOption() {
        return payment_option;
    }
    public void setPaymentOption(int payment_option) {
        this.payment_option = payment_option;
    }

    public int getPrescPadHeight() {
        return prescription_padheight;
    }
    public void setPrescPadHeight(int prescription_padheight) {
        this.prescription_padheight = prescription_padheight;
    }

    public String getPrescHeaderHeight() {
        return presc_header_height;
    }
    public void setPrescHeaderHeight(String presc_header_height) {
        this.presc_header_height = presc_header_height;
    }

    public String getPrescFooterHeight() {
        return presc_footer_height;
    }
    public void setPrescFooterHeight(String presc_footer_height) {
        this.presc_footer_height = presc_footer_height;
    }

    public int getConsultationFee() {
        return consultation_fee;
    }
    public void setConsultationFee(int consultation_fee) {
        this.consultation_fee = consultation_fee;
    }

    public String getDocLogo() {
        return doc_logo;
    }
    public void setDocLogo(String doc_logo) {
        this.doc_logo = doc_logo;
    }

    public String getFlashMessage() {
        return flash_message;
    }
    public void setFlashMessage(String flash_message) {
        this.flash_message = flash_message;
    }
}
