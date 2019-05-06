package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by lenovo on 10/05/2017.
 */

public class NotifyMessages {

    public static final String NOTIFICATION_TABLE_NAME = "NOTIFICATIONS";
    public static final String NOTIFICATION_AUTO_ID = "ID";
    public static final String NOTIFICATION_USER_ID = "USERID";
    public static final String NOTIFICATION_LOGIN_TYPE = "USER_LOGINTYPE";
    public static final String NOTIFICATION_POST_ID = "POST_ID";
    public static final String NOTIFICATION_POST_IMAGE = "POST_IMAGE";
    public static final String NOTIFICATION_TITLE = "POST_TITLE";
    public static final String NOTIFICATION_AUTHOR = "POST_AUTHOR";
    public static final String NOTIFICATION_POST_DATE = "POST_DATE";
    public static final String NOTIFICATION_POST_MESSAGE = "POST_MESSAGE";
    public static final String NOTIFICATION_POST_TYPE = "POST_TYPE";
    public static final String NOTFICATION_ENTRY_TYPE = "ENTRY_TYPE";
    public static final String NOTIFICATION_PATIENT_ID = "PATIENTID";
    public static final String NOTIFICATION_DOC_ID = "DOCID";
    public static final String NOTIFICATION_POST_KEY = "POSTKEY";

    int notify_id, post_id, user_id;
    String npost_image, npost_title, npoat_author, npost_date, npost_message, npost_type, npost_entry, patientID, refID, login_type, post_key;

    public NotifyMessages(int notify_id, int post_id, String npost_image, String npost_title, String npoat_author, String npost_date, String npost_message, String npost_type, String npost_entry, String patientID, String refID, int userid, String loginType, String post_key ) {
        this.notify_id = notify_id;
        this.post_id = post_id;
        this.npost_image = npost_image;
        this.npost_title = npost_title;
        this.npoat_author = npoat_author;
        this.npost_date = npost_date;
        this.npost_message = npost_message;
        this.npost_type = npost_type;
        this.npost_entry = npost_entry;
        this.patientID = patientID;
        this.refID = refID;
        this.user_id = userid;
        this.login_type = loginType;
        this.post_key = post_key;
    }


    public int getNotifyId() { return notify_id; }
    public void setNotifyId(int notify_id) { this.notify_id = notify_id; }

    public int getNotifyUserId() { return user_id; }
    public void setNotifyUserId(int user_id) { this.user_id = user_id; }

    public String getNotifyLoginType() { return login_type; }
    public void setNotifyLoginType(String login_type) { this.login_type = login_type; }

    public String getNotifyPostKey() { return post_key; }
    public void setNotifyPostKey(String post_key) { this.post_key = post_key; }

    public String getNotifyPatientId() { return patientID; }
    public void setNotifyPatientId(String patientID) { this.patientID = patientID; }

    public String getNotifyRefId() { return refID; }
    public void setNotifyRefId(String refID) { this.refID = refID; }

    public int getNotifyPostId() { return post_id; }
    public void setNotifyPostId(int post_id) { this.post_id = post_id; }

    public String getNotifyPostImage() { return npost_image; }
    public void setNotifyPostImage(String npost_image) { this.npost_image = npost_image; }

    public String getNotifyPostTitle() { return npost_title; }
    public void setNotifyPostTitle(String npost_title) { this.npost_title = npost_title; }

    public String getNotifyPostAuthor() { return npoat_author; }
    public void setNotifyPostAuthor(String npoat_author) { this.npoat_author = npoat_author; }

    public String getNotifyPostDate() { return npost_date; }
    public void setNotifyPostDate(String npost_date) { this.npost_date = npost_date; }

    public String getNotifyPostMessage() { return npost_message; }
    public void setNotifyPostMessage(String npost_message) { this.npost_message = npost_message; }

    public String getNotifyPostType() { return npost_type; }
    public void setNotifyPostType(String npost_type) { this.npost_type = npost_type; }

    public String getNotifyPostEntry() { return npost_entry; }
    public void setNotifyPostEntry(String npost_entry) { this.npost_entry = npost_entry; }

}
