package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by lenovo on 11/09/2017.
 */
public class MyTemplates {

    int template_id, admin_id, user_id;
    String template_name, login_type;

    public MyTemplates(int template_id, int admin_id, String template_name, int user_id, String user_login_type) {
        this.template_id = template_id;
        this.admin_id = admin_id;
        this.template_name = template_name;
        this.user_id = user_id;
        this.login_type = user_login_type;
    }

    public int getTemplateID() {
        return template_id;
    }
    public void setTemplateID(int template_id) { this.template_id = template_id; }

    public int getTemplateUserID() {
        return user_id;
    }
    public void setTemplateUserID(int user_id) { this.user_id = user_id; }

    public int getTemplateAdminID() {
        return admin_id;
    }
    public void setTemplateAdminID(int admin_id) { this.admin_id = admin_id; }

    public String getTemplateName() {
        return template_name;
    }
    public void setTemplateName(String template_name) { this.template_name = template_name; }

    public String getTemplateLoginType() {
        return login_type;
    }
    public void setTemplateLoginType(String login_type) { this.login_type = login_type; }


}
