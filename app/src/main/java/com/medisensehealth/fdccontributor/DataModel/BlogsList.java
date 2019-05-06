package com.medisensehealth.fdccontributor.DataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 16-03-2017.
 */

public class BlogsList {
    int post_id, postkey, company_id, num_views, anonymous_status;
    String post_tittle, post_description, post_image, post_type, Login_User_Id, Login_User_Type, post_date, user_image, user_prof, user_name;
    String contact_info, attachments, transaction_id, number_views, video_url, from_to_date;
    public BlogsList() {
    }

    public BlogsList(int post_id, String title, String description, String post_image, String created_date, String listing_type, String username, String userprof, String userimg, String contactInfo, String attach, int companyId, String transaction_id, String number_views, String video_url, String fromToDate) {

        this.post_id = post_id;
        this.post_tittle = title;
        this.post_description = description;
        this.post_image = post_image;
        this.post_date = created_date;
        this.post_type = listing_type;
        this.user_image = userimg;
        this.user_prof = userprof;
        this.user_name = username;
        this.contact_info = contactInfo;
        this.attachments = attach;
        this.company_id = companyId;
        this.transaction_id = transaction_id;
        this.number_views = number_views;
        this.video_url = video_url;
        this.from_to_date = fromToDate;
    }

    public String getBlogFromToDate() {
        return from_to_date;
    }
    public void setBlogFromToDate(String from_to_date) { this.from_to_date = from_to_date; }

    public String getBlogVideoURL() {
        return video_url;
    }
    public void setBlogVideoURL(String video_url) { this.video_url = video_url; }

    public String getBlogNumberViews() {
        return number_views;
    }
    public void setBlogNumberViews(String number_views) { this.number_views = number_views; }

    public String getBlogTransactionID() {
        return transaction_id;
    }
    public void setBlogTransactionID(String transaction_id) { this.transaction_id = transaction_id; }

    public String getBlogUserName() {
        return user_name;
    }
    public void setBlogUserName(String user_name) { this.user_name = user_name; }

    public String getBlogUserProf() {
        return user_prof;
    }
    public void setBlogUserProf(String user_prof) { this.user_prof = user_prof; }

    public String getBlogUserImage() {
        return user_image;
    }
    public void setBlogUserImage(String user_image) { this.user_image = user_image; }


    public int getBlogId() { return post_id; }
    public void setBlogId(int post_id) { this.post_id = post_id; }

    public int getBlogPostKey() { return postkey; }
    public void setBlogPostKey(int postkey) { this.postkey = postkey; }

    public String getBlogTitle() {
        return post_tittle;
    }
    public void setBlogTitle(String post_tittle) { this.post_tittle = post_tittle; }

    public String getBlogDescription() {
        return post_description;
    }
    public void setBlogDescription(String post_description) { this.post_description = post_description; }

    public String getBlogImage() {
        return post_image;
    }
    public void setBlogImage(String post_image) { this.post_image = post_image; }

    public String getBlogPostType() {
        return post_type;
    }
    public void setBlogPostType(String post_type) { this.post_type = post_type; }

    public String getBlogLoginUserId() {return Login_User_Id; }
    public void setBlogLoginUserId(String Login_User_Id) { this.Login_User_Id = Login_User_Id; }

    public String getBlogLoginUserType() {return Login_User_Type; }
    public void setBlogLoginUserType(String Login_User_Id) { this.Login_User_Type = Login_User_Type; }

    public String getBlogPostdate() {return post_date; }
    public void setBlogPostdate(String post_date) { this.post_date = post_date; }

    public int getBlogCompanyId() {return company_id; }
    public void setBlogCompanyId(int company_id) { this.company_id = company_id; }

    public int getBlogNumViews() {return num_views; }
    public void setBlogNumViews(int num_views) { this.num_views = num_views; }

    public int getBlogAnonymousStatus() {return anonymous_status; }
    public void setBlogAnonymousStatus(int anonymous_status) { this.anonymous_status = anonymous_status; }

    public String getBlogContactInfo() {return contact_info; }
    public void setBlogContactInfo(String contact_info) { this.contact_info = contact_info; }

    public String getBlogAttachments() {return attachments; }
    public void setBlogAttachments(String attachments) { this.attachments = attachments; }

}
