package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by medisense on 30/01/18.
 */

public class QuizQuestion  implements Serializable {
    private int ID;
    private String QUESTION;
    private String OPTA;
    private String OPTB;
    private String OPTC;
    private String OPTD;
    private String OPTE;
    private String ANSWER;
    private int OPTA_ID;
    private int OPTB_ID;
    private int OPTC_ID;
    private int OPTD_ID;
    private int OPTE_ID;

    private int qa_section_id, quiz_setid, orginal_answer_id, optional_answer_id, question_id, user_id;
    private String questions, images, answers, login_type;

    public QuizQuestion()
    {
        ID=0;
        QUESTION="";
        OPTA="";
        OPTB="";
        OPTC="";
        OPTD="";
        OPTE="";
        ANSWER="";
    }
    public QuizQuestion(String qUESTION, String images, int questID, String oPTA, String oPTB, String oPTC,String oPTD,String oPTE,
                        int opt1_ans,int opt2_ans,int opt3_ans,int opt4_ans,int opt5_ans, String aNSWER) {

        this.QUESTION = qUESTION;
        this.OPTA = oPTA;
        this.OPTB = oPTB;
        this.OPTC = oPTC;
        this.OPTD = oPTD;
        this.OPTE = oPTE;
        this.ANSWER = aNSWER;
        this.images = images;
        this.qa_section_id = questID;
        this.OPTA_ID = opt1_ans;
        this.OPTB_ID = opt2_ans;
        this.OPTC_ID = opt3_ans;
        this.OPTD_ID = opt4_ans;
        this.OPTE_ID = opt5_ans;
    }

    public QuizQuestion(int qa_section_id, int question_setid, String question, String image, int org_answer_id,
                        int answer_id, int question_id, String answers, int user_id, String login_type) {
        this.qa_section_id = qa_section_id;
        this.quiz_setid = question_setid;
        this.questions = question;
        this.images = image;
        this.orginal_answer_id = org_answer_id;
        this.optional_answer_id = answer_id;
        this.question_id = question_id;
        this.answers = answers;
        this.user_id = user_id;
        this.login_type = login_type;
    }

    public QuizQuestion(int qaSectionID, int optAnsID, int user_id, String user_login_type) {
        this.qa_section_id = qaSectionID;
        this.optional_answer_id = optAnsID;
        this.user_id = user_id;
        this.login_type = user_login_type;
    }

    public int getOpt5ID(){ return OPTE_ID;}
    public void setOpt5ID(int OPTE_ID)
    {
        OPTE_ID=OPTE_ID;
    }

    public int getOpt4ID(){ return OPTD_ID;}
    public void setOpt4ID(int OPTD_ID)
    {
        OPTD_ID=OPTD_ID;
    }

    public int getOpt3ID(){ return OPTC_ID;}
    public void setOpt3ID(int OPTC_ID)
    {
        OPTC_ID=OPTC_ID;
    }

    public int getOpt2ID(){ return OPTB_ID;}
    public void setOpt2ID(int OPTB_ID)
    {
        OPTB_ID=OPTB_ID;
    }


    public int getOpt1ID(){ return OPTA_ID;}
    public void setOpt1ID(int OPTA_ID)
    {
        OPTA_ID=OPTA_ID;
    }


    public String getLoginType()
    {
        return login_type;
    }
    public void setLoginType(String login_type)
    {
        login_type=login_type;
    }

    public int getUserID(){ return user_id;}
    public void setUserID(int user_id)
    {
        user_id=user_id;
    }

    public String getAnswers()
    {
        return answers;
    }
    public void setAnswers(String answers)
    {
        answers=answers;
    }

    public int getQuestionID()
    {
        return question_id;
    }
    public void setQuestionID(int question_id)
    {
        question_id=question_id;
    }

    public int getOptAnsID()
    {
        return optional_answer_id;
    }
    public void setOptAnsID(int optional_answer_id)
    {
        optional_answer_id=optional_answer_id;
    }


    public int getQASectionID()
    {
        return qa_section_id;
    }
    public void setQASectionID(int qa_section_id)
    {
        qa_section_id=qa_section_id;
    }

    public int getQuizSetID()
    {
        return quiz_setid;
    }
    public void setQuizSetID(int quiz_setid)
    {
        quiz_setid=quiz_setid;
    }

    public String getQuestions() {
        return questions;
    }
    public void setQuestions(String questions) {
        questions = questions;
    }

    public String getImages() {
        return images;
    }
    public void setImages(String images) {
        images = images;
    }

    public int getOrginalAnswerID()
    {
        return orginal_answer_id;
    }
    public void setOrginalAnswerID(int orginal_answer_id) { orginal_answer_id=orginal_answer_id; }

    public int getID()
    {
        return ID;
    }
    public String getQUESTION() {
        return QUESTION;
    }
    public String getOPTA() {
        return OPTA;
    }
    public String getOPTB() {
        return OPTB;
    }
    public String getOPTC() {
        return OPTC;
    }
    public String getOPTD() {
        return OPTD;
    }
    public String getOPTE() {
        return OPTE;
    }
    public String getANSWER() {
        return ANSWER;
    }
    public void setID(int id)
    {
        ID=id;
    }
    public void setQUESTION(String qUESTION) {
        QUESTION = qUESTION;
    }
    public void setOPTA(String oPTA) {
        OPTA = oPTA;
    }
    public void setOPTB(String oPTB) {
        OPTB = oPTB;
    }
    public void setOPTC(String oPTC) {
        OPTC = oPTC;
    }
    public void setOPTD(String oPTD) {
        OPTD = oPTD;
    }
    public void setOPTE(String oPTE) {
        OPTE = oPTE;
    }
    public void setANSWER(String aNSWER) {
        ANSWER = aNSWER;
    }
}
