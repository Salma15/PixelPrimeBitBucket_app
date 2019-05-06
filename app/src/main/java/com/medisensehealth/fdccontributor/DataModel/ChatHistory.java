package com.medisensehealth.fdccontributor.DataModel;


import java.util.Date;

/**
 * Created by lenovo on 10-03-2017.
 */

public class ChatHistory {

    int doc_id, patient_id, chat_id, partner_id;
    String chat_messages, doctor_name, doctor_img;
    String message_time;

    public ChatHistory() {
    }


    public ChatHistory(int pId, int doc_id, int partner_id, String doc_name, String chat_messages, String doctor_name, String doctor_img, String message_time) {
        this.patient_id = pId;
        this.doc_id = doc_id;
        this.partner_id = partner_id;
        this.doctor_name = doc_name;
        this.chat_messages = chat_messages;
        this.doctor_name = doctor_name;
        this.doctor_img = doctor_img;
        this.message_time = message_time;
    }

    public ChatHistory(int patient_id, int chat_id, int partner_id, int doc_id, String doc_name, String chat_note, String time_stamp, String doc_photo) {
        this.patient_id = patient_id;
        this.chat_id = chat_id;
        this.partner_id = partner_id;
        this.doc_id = doc_id;
        this.doctor_name = doc_name;
        this.chat_messages = chat_note;
        this.message_time = time_stamp;
        this.doctor_img = doc_photo;
    }

    public int getChatPatId() { return patient_id; }
    public void setChatPatId(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getChatDocId() { return doc_id; }
    public void setChatDocId(int doc_id) {
        this.doc_id = doc_id;
    }

    public int getChatId() { return chat_id; }
    public void setChatId(int chat_id) {
        this.chat_id = chat_id;
    }

    public int getPartnerId() { return partner_id; }
    public void setPartnerId(int partner_id) {
        this.partner_id = partner_id;
    }

    public String getChatMessages() {
        return chat_messages;
    }
    public void setChatMessages(String chat_messages) {
        this.chat_messages = chat_messages;
    }

    public String getChatDocName() {
        return doctor_name;
    }
    public void setChatDocName(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getChatDocImage() {
        return doctor_img;
    }
    public void setChatDocImage(String doctor_img) {
        this.doctor_img = doctor_img;
    }

    public String getChatTime() {
        return message_time;
    }
    public void setChatTime(String message_time) {
        this.message_time = message_time;
    }
}
