package com.medisensehealth.fdccontributor.network;

/**
 * Created by hp on 15/01/2018.
 */

public class APIClass {

    public static String API_KEY = "AIzaSyAbJc6srTVmzg1ZLOSFr5l0E7Lxk7cOez8";
    public static String DRS_HOST = "https://pixeleyecare.com/fdc_api/";
    public static String BASE_URL = "https://pixeleyecare.com";

    public static String DRS_HOST_LOGIN = DRS_HOST + "LOGIN_DOC";
    public static String DRS_PATIENT_LIST = DRS_HOST + "PATIENT_LIST";
    public static String DRS_PATIENT_LIST_ALL = DRS_HOST + "PATIENT_LIST_ALL";
    public static String DRS_PATIENT_DETAILS = DRS_HOST + "PATIENT_DETAILS";
    public static String DRS_PATIENT_ATTACHMENT = DRS_HOST + "PATIENT_ATTACHMENTS";
    public static String DRS_SPECIALIZATION = DRS_HOST + "SPECIALIZATION";
    public static String DRS_CREATEPATIENT = DRS_HOST + "CREATEPATIENT";
    public static String DRS_CHATHISTORY = DRS_HOST + "CHATHISTORY";
    public static String DRS_CHATMESSAGEINSERT = DRS_HOST + "CHATMESSAGES";
    public static String DRS_DOCTOR_LIST = DRS_HOST + "DOCTOR_LIST";
    public static String DRS_DOCTOR_LIST_ALL = DRS_HOST + "DOCTOR_LIST_ALL";
    public static String DRS_HOSPITAL_LIST = DRS_HOST + "HOSPITAL_LIST";
    public static String DRS_HOSPITAL_LIST_ALL = DRS_HOST + "HOSPITAL_LIST_ALL";
    public static String DRS_DOCTORS_SEARCH = DRS_HOST + "DOCTOR_SEARCH";
    public static String DRS_DOCTORS_PROFILE = DRS_HOST + "DOCTOR_PROFILE";
    public static String DRS_BLOGS = DRS_HOST + "BLOGS";
    public static String DRS_OFFERS_EVENTS = DRS_HOST + "OFFERS_EVENTS";
    public static String DRS_PARTNER_SIGNUP = DRS_HOST + "PARTNER_SIGNUP";
    public static String DRS_LOGIN_GCMUPDATE = DRS_HOST + "LOGIN_GCMUPDATE";
    public static String DRS_ADD_DOCTOR_FAVOURITE = DRS_HOST + "ADD_FAVOURITE_DOCTOR";
    public static String DRS_FAVOURITE_DOCTOR_REULT = DRS_HOST + "FAVOURITE_DOCTOR_RESULT";
    public static String DRS_FAVOURITE_DOCTOR_UPDATE = DRS_HOST + "FAVOURITE_DOCTOR_UPDATE";
    public static String DRS_ADD_PARTNER = DRS_HOST + "ADD_PARTNER";
    public static String DRS_PATIENT_SEARCH = DRS_HOST + "PATIENT_SEARCH";
    public static String DRS_PATIENT_SAVE_REFER = DRS_HOST + "PATIENT_SAVE_REFER";
    public static String DRS_PATIENT_SAVE_REFER_STATUS = DRS_HOST + "PATIENT_SAVE_REFER_STATUS";
    public static String DRS_BLOGS_DETAIL_VIEWS = DRS_HOST + "BLOG_DETAIL_EVENT_OFFERS_VIEWS";
    public static String DRS_EVENT_UPDATES = DRS_HOST + "UPDATE_EVENT";
    public static String DRS_UPDATE_PASSWORD = DRS_HOST + "UPDATE_PASSWORD";
    public static String DRS_SIGNUP = DRS_HOST + "SIGNUP";
    public static String DRS_SPEC_DOCTOR_LIST = DRS_HOST + "SPEC_DOCTORS";
    public static String DRS_HOSPITAL_DOCTOR_LIST = DRS_HOST + "HOSPITAL_DOCTORS";
    public static String DRS_HOST_DOCTORS_LOGIN = DRS_HOST + "DOCTORS_LOGIN";
    public static String DRS_HOST_STATE = DRS_HOST + "GET_STATE";
    public static String DRS_HOST_DOCTORS_SIGNUP = DRS_HOST + "DOCTORS_SIGNUP";
    public static String DRS_HOST_DOCTORS_FORGOTPASSWORD = DRS_HOST + "DOCTORS_FORGOT_PASSWORD";
    public static String DRS_BLOGS_LIST = DRS_HOST + "BLOGS_LIST";
    public static String DRS_BLOGS_FILTER = DRS_HOST + "BLOGS_FILTER";
    public static String DRS_ADD_PARTNERS = DRS_HOST + "ADD_PARTNERS_LEAP";
    public static String DRS_CARE_PARTNERS_LIST = DRS_HOST + "CARE_PARTNER_LIST";
    public static String DRS_CARE_PARTNERS_LIST_ALL = DRS_HOST + "CARE_PARTNERS_LIST_ALL";
    public static String DRS_PEERS_LIST = DRS_HOST + "PEERS_LIST";
    public static String DRS_PEERS_LIST_ALL = DRS_HOST + "PEERS_LIST_ALL";

    public static String DRS_PATIENT_UPLOAD = DRS_HOST + "PATIENT_UPLOADS";

    // Patient attchment Uplaods
    public static String DRS_PATIENT_ATTCHMENT_UPLOADS = DRS_HOST + "PATIENT_ATTACHMENT_UPLOADS";
    public static String DRS_PATIENT_STATUS_CHANGE = DRS_HOST + "PATIENT_STATUS_CHANGE";

    public static final String KEY_API_KEY = "API_KEY";
    // For patient list
    public static final String KEY_USERID = "userid";
    public static final String KEY_LOGINTYPE = "login_type";
    // For patient DEATILS
    public static final String KEY_PATIENTID = "patient_id";
    // For bLOGS NUM OF VIEWS
    public static final String KEY_EVENTID = "eventid";
    public static final String KEY_EVENT_LISTINGTYPE = "listing_type";
    public static final String KEY_EVENT_TYPE = "event_type";

    // Patient Attachment Download URL
    public static String DRS_PATIENT_ATTACHMENT_URL = "https://pixeleyecare.com/Attach/";    // main server

    // Doctor Profile Pic Download URL
    public static String DRS_DOCTOR_PROFILE_URL = "https://pixeleyecare.com/Contributors/docProfilePic/";               // main server
    public static String DRS_PROFILE_URL = "https://medisensehealth.com/new_assets/img/doc_icon.jpg";  // main server

    //  public static String DRS_DOCTOR_PROFILE_URL = "httpss://beta.referralio.com/Doc/";
    //   public static String DRS_PROFILE_URL = "https://beta.referralio.com/Refer/images/anonymus_docimg.jpg";

    // Blogs Images Download URL
    public static String DRS_BLOGS_IMAGE_URL = "https://pixeleyecare.com/Contributors/Postimages/";
    // public static String DRS_BLOGS_IMAGE_URL = "https://beta.referralio.com/Hospital/Postimages/";       // beta server
    //  public static String DRS_BLOGS_IMAGE_URL_SMALL = "https://beta.referralio.com/Hospital/images/leap.png";
    public static String DRS_BLOGS_IMAGE_URL_SMALL = "httpss://medisensecrm.com/Hospital/images/leap.png";

    // Offers & Events Download URL
    //  public static String DRS_OFFERS_EVENTS_URL = "httpss://medisensecrm.com/Hospital/";
    // Eventimages/{event-id}  https://beta.referralio.com
    //  public static String DRS_OFFERS_EVENTS_URL = "https://beta.referralio.com/Hospital/Eventimages/";   // for beta url
    public static String DRS_OFFERS_EVENTS_URL = "https://pixeleyecare.com/Contributors/Eventimages/";

    // Patient Create details
    public static final String KEY_PATIENT_NAME = "patient_name";
    public static final String KEY_PATIENT_AGE = "patient_age";
    public static final String KEY_PATIENT_GENDER = "patient_gender";
    public static final String KEY_PATIENTL_OCATION = "patient_location";
    public static final String KEY_PATIENT_SPECIALIZATION = "patient_specialization";
    public static final String KEY_PATIENT_MOBILE = "patient_mobile";
    public static final String KEY_PATIENT_CHIEFMEDCOMPLAINT = "patient_chiefmedcomplaint";
    public static final String KEY_PATIENT_WEIGHT = "patient_weight";
    public static final String KEY_PATIENT_MARITALSTATUS = "patient_maritalstatus";
    public static final String KEY_PATIENT_PROFESSION = "patient_profession";
    public static final String KEY_PATIENT_HYPERTENSION = "patient_hypertesnsion";
    public static final String KEY_PATIENT_DIABETES = "patient_diabetes";
    public static final String KEY_PATIENT_CONTACTPERSON = "patient_contactperson";
    public static final String KEY_PATIENT_EMAIL = "patient_email";
    public static final String KEY_PATIENT_ADDRESS = "patient_address";
    public static final String KEY_PATIENT_CITY = "patient_city";
    public static final String KEY_PATIENT_STATE = "patient_state";
    public static final String KEY_PATIENT_COUNTRY = "patient_country";
    public static final String KEY_PATIENT_CURRENTTREATINGDOCTOR = "patient_current_treating_doctor";
    public static final String KEY_PATIENT_CURRENTTREATINGHOSPITAL = "patient_current_treating_hospital";
    public static final String KEY_PATIENT_BRIEFDESCRIPTION = "patient_brief_description";
    public static final String KEY_PATIENT_QUERYTODOCTOR = "patient_querytodoctor";

    // Patient CHAT hISTORY Window
    public static final String KEY_REFID = "doc_refId";

    // Patient CHAT mESSAGE eNTER
    public static final String KEY_CHAT_PATIENTID = "chat_patientid";
    public static final String KEY_CHAT_DOCTORID = "chat_doctorid";
    public static final String KEY_CHAT_MESSAGE = "chat_message";
    public static final String KEY_CHAT_PARTNER_ID = "chat_partnerid";
    public static final String KEY_CHAT_PATRESPONSE_SEND = "patient_response_send";

    // Doctors List URL parameters
    public static final String KEY_DOCTOR_PARTNER_ID = "doctor_partnerid";

    // Search parameter
    public static final String KEY_SEARCH_DATA = "search_string";

    // dOCTOR profile parameter
    public static final String KEY_DOCTOR_ID = "doctor_id";

    // Attachment parameter
    public static final String KEY_ATTACHMENT_NAME = "attach_name";

    // Partner SignUp parameters
    public static final String KEY_PARTNER_TYPE = "partner_type";
    public static final String KEY_PARTNER_NAME = "partner_name";
    public static final String KEY_PARTNER_CONTACTPERSON = "partner_contactperson";
    public static final String KEY_PARTNER_POSITION = "partner_contactpersonposition";
    public static final String KEY_PARTNER_EMAIL = "partner_email";
    public static final String KEY_PARTNER_PASSWORD = "partner_password";
    public static final String KEY_PARTNER_LANDLINE = "partner_landline";
    public static final String KEY_PARTNER_MOBILE = "partner_mobile";
    public static final String KEY_PARTNER_ALTERNATE_MIBILE = "partner_alternatemobile";
    public static final String KEY_PARTNER_WEBSITE = "partner_website";
    public static final String KEY_PARTNER_ADDRESS = "partner_address";
    public static final String KEY_PARTNER_LOCATION = "partner_location";
    public static final String KEY_PARTNER_STATE = "partner_state";
    public static final String KEY_PARTNER_COUNTRY = "partner_country";
    public static final String KEY_PARTNER_GCMTOKENID = "gcm_tokenid";
    public static final String KEY_PARTNER_PROFILEPIC = "partner_profilepic";

    // Partner Profile Picture Uploads
    public static String DRS_PARTNER_PROFILE_PICTURE = DRS_HOST + "PARTNER_PROFILE_UPLOAD";

    // Update GCM Token ID url - parameters
    public static final String KEY_USER_LOGINTYPE = "login_type";
    public static final String KEY_USER_USERID = "user_id";
    public static final String KEY_USER_GCMTOKEN = "gcm_token";

    // Add Doctor Favourites url - parameters
    public static final String KEY_FAVOURITE_USERID = "user_id";
    public static final String KEY_FAVOURITE_DOCID = "doc_id";
    public static final String KEY_FAVOURITE_TYPE = "user_type";
    public static final String KEY_FAVOURITE_STATUS = "fav_status";
    public static final String KEY_FAVOURITE_ID = "fav_id";

    // Patient Staus Change Parameter
    public static final String KEY_STATUS_CHANGE = "status_change";

    // Update Password
    public static final String KEY_UPDATE_PASSWORD_MOBILE = "mobile_num";
    public static final String KEY_UPDATE_PASSWORD_PASSWORD = "update_password";

    // Registration
    public static final String KEY_SIGNUP_USERNAME = "username";
    public static final String KEY_SIGNUP_EMAIL = "email";
    public static final String KEY_SIGNUP_MOBILE = "mobile_num";
    public static final String KEY_SIGNUP_COMMENTS = "comments";
    public static final String KEY_SIGNUP_MEMBERTYPE = "member_type";

    // Specialization doctors
    public static final String KEY_SPEC_ID = "spec_id";

    // Hospital doctors
    public static final String KEY_HOSPITAL_ID = "hosp_id";

    // Referaal  Patient Param
    public static final String KEY_PATIENT_RESPOND = "patient_response_condition";

    // Doctors SignUp Param
    public static final String KEY_REGISTER_NAME = "doc_name";
    public static final String KEY_REGISTER_PROFILE_IMAGE = "txtPhoto";
    public static final String KEY_REGISTER_SPECID = "specialization";
    public static final String KEY_REGISTER_GENDER = "doc_gender";
    public static final String KEY_REGISTER_AGE = "doc_age";
    public static final String KEY_REGISTER_QUALIFICATION = "doc_qual";
    public static final String KEY_REGISTER_EXPERIENCE = "doc_exp";
    public static final String KEY_REGISTER_EMAIL = "doc_mail";
    public static final String KEY_REGISTER_COUNTRY_CODE = "doc_country_code";
    public static final String KEY_REGISTER_COUNTRY_NAME = "doc_country_name";
    public static final String KEY_REGISTER_MOBILE = "doc_contact";
    public static final String KEY_REGISTER_STATE = "doc_state";
    public static final String KEY_REGISTER_CITY = "doc_city";
    public static final String KEY_REGISTER_WEBSITE = "doc_website";
    public static final String KEY_REGISTER_ONLINEOP_CHARGE = "online_charge";
    public static final String KEY_REGISTER_INPERSONOP_CHARGE = "inper_charge";
    public static final String KEY_REGISTER_CONSULT_CHARGE = "cons_charge";
    public static final String KEY_REGISTER_HOSPITAL_NAME = "hosp_name";
    public static final String KEY_REGISTER_HOSPITAL_CONTACT_NUM = "hosp_phone";
    public static final String KEY_REGISTER_HOSPITAL_EMAIL = "hosp_email";
    public static final String KEY_REGISTER_ADDRESS = "doc_hosp_address";
    public static final String KEY_REGISTER_AREA_OF_INTEREST = "doc_expert";
    public static final String KEY_REGISTER_CONTRIBUTION = "doc_contrubute";
    public static final String KEY_REGISTER_RESEARCH = "doc_research";
    public static final String KEY_REGISTER_PUBLICATION = "doc_publication";
    public static final String KEY_REGISTER_TELEOP_COND = "chkTeleOp";
    public static final String KEY_REGISTER_TELEOP_CONTACT = "tele_contact";
    public static final String KEY_REGISTER_VIDOP_COND = "chkVideoOp";
    public static final String KEY_REGISTER_VIDOP_CONTACT = "video_contact";
    public static final String KEY_REGISTER_PASSWORD = "doc_passwd";
    public static final String KEY_REGISTER_MEDCOUNCIL = "council_name";
    public static final String KEY_REGISTER_REGNUM = "reg_num";
    public static final String KEY_REGISTER_DATE_REG = "date_registration";
    public static final String KEY_REGISTER_CERIFICATE = "uploadCertificate";

    // Doctor Forgot Password
    public static final String KEY_DOCTOR_FORGOT_EMAIL = "doctor_email";

    // Add Partners
    public static final String KEY_ADD_PARTNER_NAME = "partner_name";
    public static final String KEY_ADD_PARTNER_EMAIL = "partner_email";
    public static final String KEY_ADD_PARTNER_MOBILE = "partner_mobile";
    public static final String KEY_ADD_PARTNER_MEMBERTYPE = "partner_membertype";

    // Care Partner
    public static final String KEY_CARE_PARTNER_USERID = "user_id";
    public static final String KEY_CARE_PARTNER_LOGINTYPE = "login_type";

    // Send Appointment
    public static String DRS_SEND_APPOINTMENT = DRS_HOST + "SEND_APPOINTMENT";
    public static final String KEY_SEND_APPOINT_USERID = "user_id";
    public static final String KEY_SEND_APPOINT_MOBILE = "pat_mobile";
    public static final String KEY_SEND_APPOINT_EMAIL = "pat_email";

    // Get All Appointments
    public static String DRS_APPOINTMENT_LIST = DRS_HOST + "APPOINTMENT_LIST";
    public static final String KEY_APPOINT_LIST_USERID = "userid";
    public static final String KEY_APPOINT_LIST_LOGINTYPE = "login_type";

    // Get  Appointments Details
    public static String DRS_APPOINTMENT_DETAILS = DRS_HOST + "APPOINTMENT_DETAILS";
    public static final String KEY_APPOINT_USERID = "userid";
    public static final String KEY_APPOINT_LOGINTYPE = "login_type";
    public static final String KEY_APPOINT_TRANSACTIONID = "transaction_id";
    // Appointment Status Change
    public static String DRS_APPOINTMENT_STATUS_CHANGE = DRS_HOST + "APPOINTMENT_STATUS_CHANGE";
    public static final String KEY_APPOINT_STATUS = "status_change";
    // Appointment Reschedule
    public static String DRS_APPOINTMENT_RESCHEDULE = DRS_HOST + "APPOINTMENT_RESCHEDULE";
    public static final String KEY_APPOINT_DATE = "reschedule_date";
    public static final String KEY_APPOINT_TIME = "selectTime";

    // Reassign to Doctor
    public static String DRS_REASSIGN_DOCTOR = DRS_HOST + "REASSIGN_DOCTOR";
    public static final String KEY_REASSIGN_OLD_REFID = "userid";
    public static final String KEY_REASSIGN_LOGINTYPE = "login_type";
    public static final String KEY_REASSIGN_SELECTED_DOCID = "selectref";
    public static final String KEY_REASSIGN_PATIENTID = "patientid";
    public static final String KEY_REASSIGN_PATIENTNAME = "patname";
    public static final String KEY_REASSIGN_PATIENTMOBILE = "patmobile";
    public static final String KEY_REASSIGN_PATIENT_EMAIL = "patemail";

    // Premium Patient List
    public static String DRS_PATIENT_LIST_PREM = DRS_HOST + "PATIENT_LIST_PREMIUM";
    public static String DRS_PATIENT_LIST_ALL_PREM = DRS_HOST + "PATIENT_LIST_ALL_PREMIUM";
    public static final String KEY_PATIENT_FILTER = "patient_filter";

    // Premium My Patients
    public static final String DRS_MY_PATIENT_LIST_PREM = DRS_HOST + "MY_PATIENT_LIST_PRACTICE";
    // Templates List My Patients - Premium
    public static final String DRS_PRACTICE_MY_TEMPLATES = DRS_HOST + "MY_PATIENT_TEMPLATES_PRACTICE";

    // My Patient - Template Load
    public static final String DRS_HOST_TEMPLATE_LIST = DRS_HOST + "MY_PATIENT_TEMPLATE_PREM";
    public static final String KEY_MY_PATIENT_TEMPLATE_ID = "template_id";

    // My Patient - Template Upload
    public static final String DRS_PRACTICE_CREATE_MYPATIENT_TEMPLATE = DRS_HOST + "MY_PATIENT_TEMPLATE_SAVE_PREM";
    public static final String KEY_MY_PATIENT_TEMPLATE_INAME = "template_name";

    // my patient Create - Premium
    public static final String DRS_PREM_CREATE_MYPATIENT = DRS_HOST + "MY_PATIENT_CREATE_PREM";

    // My Patient - Episodes Lists
   // public static final String DRS_PRACTICE_MYPATIENT_EPISODE_LIST = DRS_HOST + "MY_PATIENT_EPISODE_LIST_PREM";
    public static final String DRS_PRACTICE_MYPATIENT_EPISODE_LIST = DRS_HOST + "MY_PATIENT_EPISODE_LIST_PRACTICE";

    // My Patient - Prescription Views
   // public static final String DRS_PRACTICE_MY_PRESCRIPTIONS = DRS_HOST + "MY_PATIENT_PRESCRIPTION_LIST_PREM";
    public static final String DRS_PRACTICE_MY_PRESCRIPTIONS = DRS_HOST + "MY_PATIENT_PRESCRIPTION_LIST_PRACTICE";
    public static final String KEY_MY_PATIENT_EPISODE_ID = "episode_id";

    // My Patient - Prescription Views
 //   public static final String DRS_PRACTICE_MYPATIENT_ADDEPISODE = DRS_HOST + "MY_PATIENT_ADD_EPISODE_PREM";
    public static final String DRS_PRACTICE_MYPATIENT_ADDEPISODE = DRS_HOST + "MY_PATIENT_ADD_EPISODE";


    // Premium Doctors List
    public static String DRS_PREM_DOCTOR_LIST = DRS_HOST + "DOCTOR_LIST_NEW_PREM";
    public static final String KEY_DOCTOR_TYPE = "doctor_type";
    public static String DRS_PREM_DOCTOR_LIST_ALL = DRS_HOST + "DOCTOR_LIST_ALL_NEW_PREM";
    public static final String KEY_DOCTOR_SPECID = "spec_id";

    // Blogs Filter Types
    public static final String KEY_BLOG_TYPE = "blog_type";
    public static final String KEY_BLOG_ID = "event_id";

    // Share Post by Email
    public static final String DRS_PRACTICE_SHARE_POST = DRS_HOST + "SHARE_POST";
    public static final String KEY_SHARE_EMAIL = "receiverMail";
    public static final String KEY_SHARE_TRANSACTION_ID = "postTransId";
    public static final String KEY_SHARE_TYPE = "postType";

    // Feeds Share
    public static final String DRS_PRACTICE_SURGICAL_SHARE = "https://pixeleyecare.com/Partners/share-blogs/";
    public static final String DRS_PRACTICE_JOB_SHARE = "https://pixeleyecare.com/Partners/share-post/";

    // Blogs Send Comments
    public static final String DRS_PRACTICE_COMMENTS = DRS_HOST + "COMMENTS_POST";
    public static final String KEY_PRACTICE_COMMENTS_TEXT = "comment_text";
    public static final String KEY_PRACTICE_COMMENTS_USERID = "user_id";
    public static final String KEY_PRACTICE_COMMENTS_POSTID = "post_id";
    public static final String KEY_PRACTICE_COMMENTS_LOGINTYPE = "login_type";
    public static final String KEY_PRACTICE_COMMENTS_POST_TYPE = "post_type";

    // Feeds -Events
    public static String DRS_BLOGS_EVENT_VIEW = DRS_HOST + "EVENT_VIEW";
    // Event Send Feedback
    public static String DRS_BLOGS_EVENT_SEND_FEEDBACK = DRS_HOST + "SEND_FEEDBACK";
    public static final String KEY_EVENT_USERID = "partner_id";
    public static final String KEY_EVENT_USERNAME = "user_name";
    public static final String KEY_EVENT_SPEAKER_ID = "speaker";
    public static final String KEY_EVENT_RATINGS = "rating";
    public static final String KEY_EVENT_COMMENTS = "comment";
    public static final String KEY_EVENT_ID = "event_id";

    // Feeds- Apply Jobs
    public static final String DRS_PRACTICE_APPLYJOB = DRS_HOST + "APPLYJOB_PREM";
    // Job Description Path
    public static final String DRS_PRACTICE_JOB_DESCRIPTION = "https://pixeleyecare.com/Partners/download-Attachments.php?";

    // GET Profile - Practice
    public static final String DRS_PREM_PROFILE = DRS_HOST + "GET_PROFILE_PREM";

    // Edit Profile - Practice
    public static final String DRS_PREM_EDIT_PROFILE = DRS_HOST + "EDIT_PROFILE_PREM";
    public static final String DRS_PREM_SET_APPOINTMENT = DRS_HOST + "SET_APPOINTMENT_PREM";
    public static final String KEY_PREM_SET_APPOINTMENT_TIMEID = "time_id";
    public static final String KEY_PREM_SET_APPOINTMENT_DAYID = "day_id";
    public static final String KEY_PREM_SET_APPOINTMENT_STATUS = "status";
    public static final String KEY_PREM_SET_APPOINTMENT_USERID = "user_id";

    // Create Appointment - Practice - Choose Date/Time
    public static final String DRS_PREM_CREATE_AAPOINTMENT = DRS_HOST + "CREATE_APPOINTMENT_PREM";

    public static final String DRS_PREM_CREATE_AAPOINTMENT_TIMINGS = DRS_HOST + "CREATE_APPOINTMENT_TIMINGS_PREM";
    public static final String KEY_PREM_CREATE_AAPOINTMENT_DATE = "day_val";

    public static final String DRS_PREM_CREATE_AAPOINTMENT_SUBMIT = DRS_HOST + "CREATE_APPOINTMENT_SUBMIT_PREM";
    public static final String KEY_PREM_CREATE_AAPOINTMENT_USERNAME= "user_name";
    public static final String KEY_PREM_CREATE_AAPOINTMENT_CHECKDATE= "check_date";
    public static final String KEY_PREM_CREATE_AAPOINTMENT_CHECKTIME= "check_time";
    public static final String KEY_PREM_CREATE_AAPOINTMENT_PATIENT_NAME= "se_pat_name";
    public static final String KEY_PREM_CREATE_AAPOINTMENT_PATIENT_MOBILE= "se_phone_no";
    public static final String KEY_PREM_CREATE_AAPOINTMENT_PATIENT_COUNTRY_NAME= "se_country_name";
    public static final String KEY_PREM_CREATE_AAPOINTMENT_PATIENT_COUTRY_CODE= "se_country_code";
    public static final String KEY_PREM_CREATE_AAPOINTMENT_PATIENT_COUNTRY_NAMECODE= "se_country_namecode";

    // Book Appointment - Patient Details Premium
    public static String DRS_PRACTICE_BOOK_APPOINTMENT = DRS_HOST + "BOOK_APPOINTMENT";
    // Patient View Book Appointment
    public static final String KEY_BOOK_APPOINT_DOCID = "selectref2";
    public static final String KEY_BOOK_APPOINT_PATIENTID = "Pat_Id";
    public static final String KEY_BOOK_APPOINT_SELTIME = "selectTime";
    public static final String KEY_BOOK_APPOINT_SELDATE = "check_date";
    public static final String KEY_BOOK_APPOINT_NAME = "patient_name";
    public static final String KEY_BOOK_APPOINT_CONTACT_NAME = "contactperson_name";
    public static final String KEY_BOOK_APPOINT_MOBILE = "pat_contact_num";
    public static final String KEY_BOOK_APPOINT_EMAIL = "pat_email";
    public static final String KEY_BOOK_APPOINT_ADDRESS = "pat_address";

    // Send Case - Practice Premium
    public static String DRS_PREM_SEND_CASE = DRS_HOST + "PATIENT_SEND_CASE_PREM";
    public static final String KEY_SELECTED_DOCTOR = "selectref1";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_PATIENT_ID = "Pat_Id";
    public static final String KEY_PATIENT_CONTACT = "contatInclude";   // 1 for sharing patient email and phone number

    // Feedback Send
    public static String FEEDBACK_SEND_PREM = DRS_HOST + "FEEDBACK_SEND_PREM";
    public static final String KEY_FEEDBACK_TEXT = "txtFeedback";

    // Support/Help Send
    public static String SUPPORT_SEND_PREM = DRS_HOST + "SUPPORT_SEND_PREM";
    public static final String KEY_SUPPORT_TEXT = "txtSupport";

    // Delete Appointment - Practice
    public static final String DRS_PREM_DELETE_APPOINTMENT = DRS_HOST + "DELETE_APPOINTMENT_PREM";
    public static final String KEY_PRREM_APPOINT_TRANS_ID = "appoint_transaction_id";

    // Appointment Filter
    public static String DRS_APPOINTMENT_FILTER = DRS_HOST + "APPOINTMENT_FILTERS";
    public static final String KEY_APPT_TODAY = "appt_today";
    public static final String KEY_APPT_FROMDATE = "appt_date_from";
    public static final String KEY_APPT_TODATE = "appt_date_to";
    public static final String KEY_APPT_FILTERTYPE = "appt_filter_type";

    // Feeds like & comments API
    public static String DRS_FEEDS_LIKE_COMMENT_COUNT = DRS_HOST + "LIKE_COMMENTS";

    // Feeds Cooment Post API
    public static String DRS_FEEDS_COMMENT_POST = DRS_HOST + "COMMENTS_POST_API";

    // Feeds List API
    public static String DRS_FEEDS_LIST = DRS_HOST + "FEEDS_LIST_API";
    public static final String KEY_FEEDS_INIT = "initial_value";
    public static final String KEY_FEEDS_OFFSET = "offset_value";

    // Event Registration
    public static String DRS_EVENT_REGISTER = DRS_HOST + "EVENT_REGISTER";

    // My Patient Attcahments
    public static String DRS_MYPATIENT_ATTACHMENT = DRS_HOST + "MYPATIENT_ATTACHMENTS_LIST";
    public static String DRS_MYPATIENT_ATTACHMENT_URL_PREM = "https://pixeleyecare.com/Partners/episodeAttach/";    // main server

    // Add Partners Premium
    public static String DRS_ADD_CAREPARTNERS_PREM = DRS_HOST + "DRS_ADD_CAREPARTNERS_PREM";
    // Add Partner url - parameters
    public static final String KEY_ADDPART_NAME = "partner_name";
    public static final String KEY_ADDPART_MOBILE = "partner_mobile";
    public static final String KEY_ADDPART_EMAIL = "partner_email";
    public static final String KEY_ADDPART_TYPE = "partner_membertype";

    // cARE Partners Premium
    public static String DRS_CAREPARTNERS_LIST_PREM = DRS_HOST + "DRS_CAREPARTNERS_LIST_PREM";
    public static String DRS_CAREPARTNERS_LIST_ALL_PREM = DRS_HOST + "DRS_CAREPARTNERS_LIST_ALL_PREM";

    // Appointment Search
    public static String DRS_APPOINTMENTS_SEARCH = DRS_HOST + "APPOINTMENTS_SEARCH";

    // MyPatient Search
    public static String DRS_MYPATIENTS_SEARCH = DRS_HOST + "MYPATIENTS_SEARCH";

    // MyPatient Filter
    public static String DRS_MYPATIENT_FILTER = DRS_HOST + "MYPATIENT_FILTERS";
//    public static final String KEY_APPT_TODAY = "appt_today";
//    public static final String KEY_APPT_FROMDATE = "appt_date_from";
//    public static final String KEY_APPT_TODATE = "appt_date_to";
//    public static final String KEY_APPT_FILTERTYPE = "appt_filter_type";

    // Get Count
    public static String GET_COUNT_ALL = DRS_HOST + "GET_COUNT_ALL";

    public static String DRS_QUIZ_SETS = DRS_HOST + "QUIZ_SET_LISTS";

    public static String DRS_QUIZ_QUESTIONS_LIST = DRS_HOST + "QUIZ_QUESTIONS_LIST";
    public static final String KEY_QUIZ_SETID = "quiz_set_id";

    // Quiz Submit
    public static final String DRS_QUIZ_SUBMIT = DRS_HOST + "QUIZ_DATA_SUBMIT";

    public static String DRS_MYPATIENT_ATTACHMENT_URL_PRACTICE = "https://pixeleyecare.com/Contributors/episodeAttach/";    // main server

    // my patient Create - Practice
    public static final String DRS_PRACTICE_CREATE_MYPATIENT = DRS_HOST + "MY_PATIENT_CREATE_PRACTICE";
    public static final String DRS_PRACTICE_CREATE_MYPATIENT_NEW = DRS_HOST + "MY_PATIENT_CREATE_NEW";

    // my patient Create - Submit
    public static final String DRS_MYPATIENT_CREATE_MEMBER = DRS_HOST + "MY_PATIENTS_CREATE_MEMBER";


    // Blog Posts Upload
    public static String DRS_BLOG_POST_UPLOAD = DRS_HOST + "BLOG_POST_UPLOAD";
    public static final String KEY_BLOG_POST_TITLE = "blog_title";
    public static final String KEY_BLOG_POST_DESCRIPTION = "blog_description";
    public static final String KEY_BLOG_POST_TAGS = "blog_tags";
    public static final String KEY_BLOG_POST_PHOTO = "txtPhoto";

    // Video Posts Upload
    public static String DRS_VIDEO_POST_UPLOAD = DRS_HOST + "VIDEO_POST_UPLOAD";
    public static final String KEY_VIDEO_POST_TITLE = "video_title";
    public static final String KEY_VIDEO_POST_DESCRIPTION = "video_description";
    public static final String KEY_VIDEO_POST_TAGS = "video_tags";
    public static final String KEY_VIDEO_POST_URL = "video_url";

    // Blog Search
    public static String DRS_BLOGS_SEARCH = DRS_HOST + "BLOG_SEARCH";

    // SignUp New
    public static final String DRS_SIGNUP_NEW = DRS_HOST + "PRIME_SIGNUP_REQUEST";

    // Share Profile Link
    public static final String DRS_SHARE_PROFILE_LINK = DRS_HOST + "SHARE_PROFILE_LINK";
    public static final String SHARE_EMAIL_ID = "email_id";
    public static final String SHARE_MOBILE_NUM = "mobile_num";
    public static final String SHARE_USER_NAME = "user_name";
    public static final String SHARE_SPEC_NAME = "spec_name";
    public static final String SHARE_USER_LOCATION = "user_location";

    // Android Analytic Key in all API'S
    public static final String KEY_DEVICE_KEY = "device_type";
    public static final String KEY_DEVICE_VALUE = "android";


    // My Patient New General
    public static final String DRS_CHIEF_MEDICAL_COMPLAINT_LIST = DRS_HOST + "CHIEF_MEDICAL_COMPLAINT_LIST";
    public static final String DRS_INVESTIGATIONS_LIST = DRS_HOST + "INVESTIGATIONS_LIST";
    public static final String DRS_EXAMINATIONS_LIST = DRS_HOST + "EXAMINATION_LIST";
    public static final String DRS_DIAGNOSIS_FREQUENT_LIST = DRS_HOST + "DIAGNOSIS_FREQUENT_LIST";
    public static final String DRS_DIAGNOSIS_SEARCH = DRS_HOST + "DIAGNOSIS_SEARCH";
    public static final String DRS_TREATMENTS_LIST = DRS_HOST + "TREATMENT_LIST";
    public static final String DRS_PRESCRIPTION_FREQUENT_LIST = DRS_HOST + "PRESCRIPTION_FREQUENT_LIST";
    public static final String DRS_PRESCRIPTION_SEARCH = DRS_HOST + "PTRESCRIPTION_SEARCH";

    public static final String DRS_PATIENT_ADD_VISIT = DRS_HOST + "PATIENT_ADD_VISIT";

    public static final String DRS_DRUG_ALLERY_FREQUENT_LIST = DRS_HOST + "DRUG_ALLEGY_FREQUENT_LIST";
    public static final String DRS_DRUG_ALLERGY_SEARCH = DRS_HOST + "DRUG_ALLERGY_SEARCH";
    public static final String DRS_DRUG_ABUSE_FREQUENT_LIST = DRS_HOST + "DRUG_ABUSE_FREQUENT_LIST";
    public static final String DRS_FAMILY_HISTORY_FREQUENT_LIST = DRS_HOST + "FAMILY_HISTORY_FREQUENT_LIST";
    public static final String DRS_PATIENT_MEDICAL_HISTORY_UPDATE = DRS_HOST + "PATIENT_MEDICAL_HISTORY_UPDATE";

    // Ophthalmology URL'S
    public static final String DRS_PATIENT_ADD_VISIT_OPHTHAL = DRS_HOST + "ADD_VISIT_OPHTHAL";
    public static final String DRS_LIDS_LIST = DRS_HOST + "LIDS_LIST";
    public static final String DRS_CONJUCTIVA_LIST = DRS_HOST + "CONJUCTIVA_LIST";
    public static final String DRS_SCLERA_LIST = DRS_HOST + "SCLERA__LIST";
    public static final String DRS_CORNEA_ANTERIOR_LIST = DRS_HOST + "CORNEA_ANTERIOR_LIST";
    public static final String DRS_CORNEA_POSTERIOR_LIST = DRS_HOST + "CORNEA_POSTERIOR_LIST";
    public static final String DRS_ANTERIOR_CHAMBER_LIST = DRS_HOST + "ANTERIOR_CHAMBER_LIST";
    public static final String DRS_IRIS_LIST = DRS_HOST + "IRIS_LIST";
    public static final String DRS_PUPIL_LIST = DRS_HOST + "PUPIL_LIST";
    public static final String DRS_ANGLE_LIST = DRS_HOST + "ANGLE_LIST";
    public static final String DRS_LENS_LIST = DRS_HOST + "LENS_LIST";
    public static final String DRS_VITEROUS_LIST = DRS_HOST + "VITEROUS_LIST";
    public static final String DRS_FUNDUS_LIST = DRS_HOST + "FUNDUS_LIST";
    public static final String DRS_OLD_VISITS_OPHTHAL_LIST = DRS_HOST + "OLD_VISIT_OPHTHAL_LIST";

    public static final String DRS_PATIENT_EDIT_VISIT_OPHTHAL = DRS_HOST + "EDIT_VISIT_OPHTHAL";
    public static final String DRS_MY_PATIENT_OPTHAL_TRENDS_LIST = DRS_HOST + "OPTHAL_TRENDS_LIST";
    public static final String DRS_MYPATIENT_ADD_TRENDS_OPHTHAL = DRS_HOST + "OPHTHAL_ADD_TRENDS";

    // Diagnostic Lists
    public static final String DRS_DIAGNOSTICS_LIST = DRS_HOST + "DIAGNOSTICS_LIST";
    public static final String DRS_DIAGNOSTICS_REFER = DRS_HOST + "DIAGNOSTICS_REFER";
    public static final String DRS_DIAGNOSTICS_ADD = DRS_HOST + "DIAGNOSTICS_ADD";
    public static final String KEY_DIAGNO_NAME = "txtDiagnosticName";
    public static final String KEY_DIAGNO_EMAIL = "txtDiagnosticEmail";
    public static final String KEY_DIAGNO_MOBILE = "txtDiagnosticMobile";
    public static final String KEY_DIAGNO_CITY = "txtDiagnosticCity";

    // Pharma Centre Lists
    public static final String DRS_PHARMA_LIST = DRS_HOST + "PHARMA_LIST";
    public static final String DRS_PHARMA_REFER = DRS_HOST + "PHARMA_REFER";
    public static final String DRS_PHARMA_ADD = DRS_HOST + "PHARMA_ADD";
    public static final String KEY_PHARMA_NAME = "txtPharmaName";
    public static final String KEY_PHARMA_EMAIL = "txtPharmaEmail";
    public static final String KEY_PHARMA_MOBILE = "txtPharmaMobile";
    public static final String KEY_PHARMA_CITY = "txtPharmaCity";

    public static final String DRS_SETTINGS_UPDATE = DRS_HOST + "OTHER_SETTINGS_UPDATE";
    public static final String DRS_SETTINGS_LIST = DRS_HOST + "OTHER_SETTINGS_LIST";
    public static final String DRS_SETTINGS_DOCLOGO_URL = "https://pixeleyecare.com/Contributors/docLogo/";

    // Add Hospital
    public static final String DRS_ADD_HOAPITAL = DRS_HOST + "ADD_HOSPITAL";
    public static final String DRS_SETTINGS_HOSP_LIST = DRS_HOST + "SETTINGS_HOSP_LIST";
    public static final String DRS_SETTINGS_HOSP_UPDATE = DRS_HOST + "SETTINGS_HOSP_UPDATE";

    public static final String DRS_APPOINT_LIST = DRS_HOST + "APPOINT_LIST";
    public static final String DRS_APPOINT_STATUS = DRS_HOST + "APPOINT_STATUS";
    public static final String KEY_APPOINT_TRANS_ID = "transaction_id";
    public static final String KEY_APPOINT_STATUS_VAL = "status";
    public static final String DRS_APPOINT_FILTER = DRS_HOST + "APPOINT_FILTER";
    public static final String KEY_APPOINT_FROMDATE = "appt_date_from";
    public static final String KEY_APPOINT_TODATE = "appt_date_to";
    public static final String KEY_APPOINT_FILTERTYPE = "appt_filter_type";
    public static final String DRS_APPOINT_PATIENT_SEARCH = DRS_HOST + "APPOINT_SEARCH_PATIENT";
    public static final String DRS_APPOINT_TIMINGS = DRS_HOST + "APPOINT_TIMINGS";
    public static final String KEY_APPOINT_DATE_ID = "day_val";
    public static final String DRS_APPOINT_RESCHEDULE = DRS_HOST + "APPOINT_RESCHEDULE";
    public static final String KEY_APPOINT_DATE_VAL = "check_date";
    public static final String KEY_APPOINT_TIME_VAL = "check_time";
    public static final String DRS_APPOINT_PATIENT_DETAIL = DRS_HOST + "APPOINT_DETAILS_PATIENT";

    public static final String DRS_APPOINT_BOOK = DRS_HOST + "APPOINT_BOOK";
    public static final String KEY_APPOINT_NAME = "se_pat_name";
    public static final String KEY_APPOINT_GENDER = "se_gender";
    public static final String KEY_APPOINT_CHECK = "appoint_type";
    public static final String KEY_APPOINT_MOBILE = "se_phone_no";
    public static final String KEY_APPOINT_CITY = "se_city";
    public static final String KEY_APPOINT_EMAIL = "se_email";
    public static final String KEY_APPOINT_ADDRESS= "se_address";
    public static final String KEY_APPOINT_PINCODE = "se_pincode";
    public static final String KEY_APPOINT_STATE = "se_state";
    public static final String KEY_APPOINT_COUNTRY = "se_country";
    public static final String KEY_APPOINT_CONSULTAION_CHARGE = "consult_charge";

    public static final String DRS_EMR_LIST = DRS_HOST + "EMR_LIST";
    public static final String KEY_VIEW_ALL = "view_more";
    public static final String KEY_FILTER_TYPE = "filter_type";

    // my patient Create - New
    public static final String DRS_MYPATIENT_CREATE = DRS_HOST + "MY_PATIENTS_CREATE";

    // My Patients Medical History Details
    public static final String DRS_PATIENT_MEDICAL_HISTORY_DETAILS = DRS_HOST + "MEDICAL_HISTORY_DETAILS";

    // My Patients Reports
    public static final String DRS_MYPATIENT_REPORTS = DRS_HOST + "MYPATIENT_REPORTS";
    public static final String DRS_MYPATIENT_UPLOAD_REPORTS = DRS_HOST + "MYPATIENT_UPLOAD_REPORTS";

    // Share EMR Prescription URL
    public static final String DRS_SHARE_PRESCRIPTION_LINK = "https://pixeleyecare.com/Contributors/print-emr/prescs/";

    //  My Patient Update Investigations
    public static final String DRS_UPDATE_INVESTIGATIONS = DRS_HOST + "MYPATIENT_UPDATE_INVESTIGATIONS";

    //  My Patient Prescription Send SMS/EMAIL
    public static final String DRS_SEND_PRESCRIPTION = DRS_HOST + "SEND_PRESCRIPTION";

    // Edit Profile
    public static final String DRS_EDIT_PROFILE_DETAILS = DRS_HOST + "EDIT_PROFILE_DETAILS";
    public static final String DRS_EDIT_PROFILE_UPDATES = DRS_HOST + "EDIT_PROFILE_UPDATES";

    // Set Holiday Date
    public static final String DRS_SET_HOLIDAYS = DRS_HOST + "SET_HOLIDAYS";
    public static final String KEY_HOLIDAY_DATE = "holiday_date";
    public static final String KEY_HOLIDAY_REASON = "holiday_reason";
    public static final String DRS_CANCEL_HOLIDAYS = DRS_HOST + "CANCEL_HOLIDAYS";
    public static final String KEY_HOLIDAY_ID = "holiday_id";

    // Number of Appointment Slots / No. of patients per hour
    public static final String DRS_SET_APPOINTMENT_SLOTS = DRS_HOST + "SET_APPOINTMENT_SLOTS";
    public static final String KEY_PATIENTS_NUMBER = "num_slots";
    public static final String DRS_APPOINTMENT_SLOT_LIST = DRS_HOST + "APPOINTMENT_SLOT_LIST";

    // Optical Center Lists
    public static final String DRS_OPTICALS_LIST = DRS_HOST + "OPTICALS_LIST";
    public static final String DRS_OPTICALS_REFER = DRS_HOST + "OPTICALS_REFER";
    public static final String DRS_OPTICALS_ADD = DRS_HOST + "OPTICALS_ADD";
    public static final String KEY_OPTICALS_NAME = "txtOpticalName";
    public static final String KEY_OPTICALS_EMAIL = "txtOpticalEmail";
    public static final String KEY_OPTICALS_MOBILE = "txtOpticalMobile";
    public static final String KEY_OPTICALS_CITY = "txtOpticalCity";
    public static final String KEY_OPTICALS_STATE = "txtOpticalState";
    public static final String KEY_OPTICALS_COUNTRY = "txtOpticalCountry";
    public static final String KEY_OPTICALS_CONTACT_PERSON = "txtOpticalContactPerson";



    public static String DRS_HOST_TESTING_LOGIN = DRS_HOST + "TESTING_LOGIN";
}
