package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by lenovo on 04/09/2017.
 */

public class Comments {

    int user_id;
    String user_name, comment_desc, user_img, post_date, post_type, event_id;

    public Comments(String username, String user_image, String comments, String post_date, String topic_id, String topic_type) {
        this.user_name = username;
        this.user_img = user_image;
        this.comment_desc = comments;
        this.post_date = post_date;
        this.event_id = topic_id;
        this.post_type = topic_type;
    }

    public int getCommentUserId() {
        return user_id;
    }
    public void setCommentUserId(int user_id) { this.user_id = user_id; }

    public String getCommentPostId() {
        return event_id;
    }
    public void setCommentPostId(String event_id) { this.event_id = event_id; }

    public String getCommentUserImage() {
        return user_img;
    }
    public void setCommentUserImage(String user_img) { this.user_img = user_img; }

    public String getCommentUsername() {
        return user_name;
    }
    public void setCommentUsername(String user_name) { this.user_name = user_name; }

    public String getCommentDescription() {
        return comment_desc;
    }
    public void setCommentDescription(String comment_desc) { this.comment_desc = comment_desc; }

    public String getCommentPostDate() {
        return post_date;
    }
    public void setCommentPostDate(String post_date) { this.post_date = post_date; }

    public String getCommentPostType() {
        return post_type;
    }
    public void setCommentPostType(String post_type) { this.post_type = post_type; }
}
