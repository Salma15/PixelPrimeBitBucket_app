package com.medisensehealth.fdccontributor.parser;

import android.util.Log;

import com.medisensehealth.fdccontributor.DataModel.ChiefMedicalComplaint;
import com.medisensehealth.fdccontributor.DataModel.Diagnosis;
import com.medisensehealth.fdccontributor.DataModel.DiagnosticCentreList;
import com.medisensehealth.fdccontributor.DataModel.DrugAbuse;
import com.medisensehealth.fdccontributor.DataModel.DrugAllery;
import com.medisensehealth.fdccontributor.DataModel.EpisodesList;
import com.medisensehealth.fdccontributor.DataModel.EpisodesOpthoList;
import com.medisensehealth.fdccontributor.DataModel.FamilyHistory;
import com.medisensehealth.fdccontributor.DataModel.FrequentPrescription;
import com.medisensehealth.fdccontributor.DataModel.Investigations;
import com.medisensehealth.fdccontributor.DataModel.Lids;
import com.medisensehealth.fdccontributor.DataModel.OphthalAngleAnteriorChamber;
import com.medisensehealth.fdccontributor.DataModel.OphthalAnteriorChamber;
import com.medisensehealth.fdccontributor.DataModel.OphthalConjuctiva;
import com.medisensehealth.fdccontributor.DataModel.OphthalCornearAnteriorSurface;
import com.medisensehealth.fdccontributor.DataModel.OphthalCornearPosteriorSurface;
import com.medisensehealth.fdccontributor.DataModel.OphthalFundus;
import com.medisensehealth.fdccontributor.DataModel.OphthalIris;
import com.medisensehealth.fdccontributor.DataModel.OphthalLens;
import com.medisensehealth.fdccontributor.DataModel.OphthalPupil;
import com.medisensehealth.fdccontributor.DataModel.OphthalSclera;
import com.medisensehealth.fdccontributor.DataModel.OphthalViterous;
import com.medisensehealth.fdccontributor.DataModel.OpticalCentreList;
import com.medisensehealth.fdccontributor.DataModel.PharmaCentreList;
import com.medisensehealth.fdccontributor.DataModel.PrescriptionList;
import com.medisensehealth.fdccontributor.DataModel.QuizQuestion;
import com.medisensehealth.fdccontributor.DataModel.Treatments;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.utils.Utils;

import org.json.JSONObject;
import java.io.File;
import java.io.UTFDataFormatException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;


/**
 * Created by Pratik Butani
 */
public class JSONParser {

    /**
     * Upload URL of your folder with php file name...
     * You will find this file in php_upload folder in this project
     * You can copy that folder and paste in your htdocs folder...
     */
   // private static final String URL_UPLOAD_IMAGE = "http://192.168.15.4/referral_app/php_upload/upload.php";
         private static final String URL_UPLOAD_IMAGE = "php_upload/upload.php";
    /**
     * Upload Image
     *
     * @param sourceImageFile
     * @return
     */
    public static JSONObject uploadImage(String sourceImageFile, int PATIENT_ID_ATTACH) {

        try {
            File sourceFile = new File(sourceImageFile);

            Log.d("TAG", "File...::::" + sourceFile + " : " + sourceFile.exists());

            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

            String filename = sourceImageFile.substring(sourceImageFile.lastIndexOf("/")+1);
            Log.d(Utils.TAG, "filename: " + filename);
            /**
             * OKHTTP2
             */
//            RequestBody requestBody = new MultipartBuilder()
//                    .type(MultipartBuilder.FORM)
//                    .addFormDataPart("member_id", memberId)
//                    .addFormDataPart("file", "profile.png", RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
//                    .build();

            /**
             * OKHTTP3
             */
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("member_id", String.valueOf(PATIENT_ID_ATTACH))
                    .addFormDataPart("uploaded_file", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                    .addFormDataPart("result", "my_image")
                    .addFormDataPart(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE)
                    .build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_PATIENT_ATTCHMENT_UPLOADS)
                  //  .url("http://beta.referralio.com/leap_api/php_upload/upload.php")
                    .post(requestBody)
                    .build();

         //   OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, res.toString());
            Log.e(Utils.TAG, "attach response: " + res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject uploadAlbumImage(String albumId, ArrayList<String> photoCaptions) {

        try {

            MultipartBuilder multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);

            int length = photoCaptions.size();
            int noOfImageToSend = 0;
            for(int i = 0; i < length; i++) {
                /**
                 * Getting Photo Caption and URL
                 */

//                PhotoCaption photoCaptionObj = photoCaptions.get(i);
//                String photoUrl = photoCaptionObj.getPhotoUrl();
//                String photoCaption = photoCaptionObj.getPhotoCaption();

                String photoUrl = photoCaptions.get(i);
                String photoCaption = albumId;

                File sourceFile = new File(photoUrl);

                if(sourceFile.exists()) {
                    /** Changing Media Type whether JPEG or PNG **/
                    final MediaType MEDIA_TYPE = MediaType.parse(photoUrl.endsWith("png") ? "image/png" : "image/jpeg");

                    /** Adding in MultipartBuilder **/
                    multipartBuilder.addFormDataPart("member_id" + i, photoCaption);
                    multipartBuilder.addFormDataPart("uploaded_file" + i, sourceFile.getName(), RequestBody.create(MEDIA_TYPE, sourceFile));

                    /** Counting No Of Images **/
                    noOfImageToSend++;
                }
            }

            RequestBody requestBody = multipartBuilder
                    .addFormDataPart("member_id", albumId)
                    .addFormDataPart("uploaded_file", String.valueOf(noOfImageToSend))
                    .build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_HOST+URL_UPLOAD_IMAGE)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();

            /** Your Response **/
            String responseStr = response.body().string();

            Log.i(Utils.TAG, "responseStr : "+ responseStr);

            return new JSONObject(responseStr);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject uploadProfileImage(String sourceImageFile, int PARTNER_ID) {

        try {
            File sourceFile = new File(sourceImageFile);

            Log.d("TAG", "File...::::" + sourceFile + " : " + sourceFile.exists());

            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

            String filename = sourceImageFile.substring(sourceImageFile.lastIndexOf("/")+1);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("partner_id", String.valueOf(PARTNER_ID))
                    .addFormDataPart("uploaded_file", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                    .addFormDataPart("result", "my_image")
                    .build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_PARTNER_PROFILE_PICTURE)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, res.toString());
            Log.e(Utils.TAG, "Error: " + res);
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject register_doctors(String docProfilePath, String doctorName_get, int specializationId_get,
                                              int genderId_get, String age_get, String qualification_get, String experience_get,
                                              String email_get, String country_code_get, String country_name_get, String mobile_get,
                                              String state_get, String city_get, String website_get, String onlineOpCharge_get,
                                              String inpersonOpCharge_get, String consultationCharge_get, String hospitalName_get,
                                              String hospitalContactNum_get, String hospitalEmail_get, String communicationAddress_get,
                                              String areaInterest_get, String profCobtribution_get, String researchDetails_get, String publications_get,
                                              int teleOpinion_get, String teleOpinionNum_get, int videoOpinion_get, String videoOpinionNum_get,
                                              String password_get, String medCouncil_get, String registrationNumber_get, String dateOfReg_get,
                                              String docCertificatePath) {
        File docProfileFile = null, docCertificateFile = null;
        String profileName = "", certificateName = "";
        MediaType MEDIA_TYPE_PNG = null, MEDIA_TYPE_PNG2 = null;

        try {

            if(docProfilePath != null) {
                docProfileFile = new File(docProfilePath);
                Log.d(Utils.TAG, "Prof. File...::::" + docProfileFile + " : " + docProfileFile.exists());
                MEDIA_TYPE_PNG = MediaType.parse("image/*");
                profileName = docProfilePath.substring(docProfilePath.lastIndexOf("/")+1);
            }

            if(docCertificatePath != null) {
                docCertificateFile = new File(docCertificatePath);
                Log.d(Utils.TAG, "CERT. File...::::" + docCertificateFile + " : " + docCertificateFile.exists());
                MEDIA_TYPE_PNG2 = MediaType.parse("image/*");
                certificateName = docCertificatePath.substring(docCertificatePath.lastIndexOf("/")+1);
            }


//            RequestBody requestBody = new MultipartBody.Builder()
            RequestBody requestBody;
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(APIClass.KEY_API_KEY,APIClass.API_KEY)
                    .addFormDataPart(APIClass.KEY_REGISTER_NAME, doctorName_get)
                    .addFormDataPart(APIClass.KEY_REGISTER_SPECID, String.valueOf(specializationId_get))
                    .addFormDataPart(APIClass.KEY_REGISTER_GENDER, String.valueOf(genderId_get))
                    .addFormDataPart(APIClass.KEY_REGISTER_QUALIFICATION, qualification_get)
                    .addFormDataPart(APIClass.KEY_REGISTER_EXPERIENCE, experience_get)
                    .addFormDataPart(APIClass.KEY_REGISTER_EMAIL, email_get)
                    .addFormDataPart(APIClass.KEY_REGISTER_COUNTRY_CODE, country_code_get)
                    .addFormDataPart(APIClass.KEY_REGISTER_COUNTRY_NAME, country_name_get)
                    .addFormDataPart(APIClass.KEY_REGISTER_MOBILE, mobile_get)
                    .addFormDataPart(APIClass.KEY_REGISTER_STATE, state_get)
                    .addFormDataPart(APIClass.KEY_REGISTER_CITY, city_get)
                    .addFormDataPart(APIClass.KEY_REGISTER_HOSPITAL_NAME, hospitalName_get)
                    .addFormDataPart(APIClass.KEY_REGISTER_HOSPITAL_CONTACT_NUM, hospitalContactNum_get)
                    .addFormDataPart(APIClass.KEY_REGISTER_HOSPITAL_EMAIL, hospitalEmail_get)
                    .addFormDataPart(APIClass.KEY_REGISTER_ADDRESS, communicationAddress_get)
                    .addFormDataPart(APIClass.KEY_REGISTER_AREA_OF_INTEREST, areaInterest_get)
                    .addFormDataPart(APIClass.KEY_REGISTER_CONTRIBUTION, profCobtribution_get)
                    .addFormDataPart(APIClass.KEY_REGISTER_TELEOP_COND, String.valueOf(teleOpinion_get))
                    .addFormDataPart(APIClass.KEY_REGISTER_VIDOP_COND, String.valueOf(videoOpinion_get))
                    .addFormDataPart(APIClass.KEY_REGISTER_PASSWORD, password_get)
                    .addFormDataPart(APIClass.KEY_REGISTER_MEDCOUNCIL, medCouncil_get)
                    .addFormDataPart(APIClass.KEY_REGISTER_REGNUM, registrationNumber_get)
                    .addFormDataPart(APIClass.KEY_REGISTER_DATE_REG, dateOfReg_get)
                    .addFormDataPart(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE)
                    .addFormDataPart(APIClass.KEY_REGISTER_CERIFICATE, certificateName, RequestBody.create(MEDIA_TYPE_PNG2, docCertificateFile));

                if (docProfilePath != null) {
                       builder.addFormDataPart(APIClass.KEY_REGISTER_PROFILE_IMAGE, profileName, RequestBody.create(MEDIA_TYPE_PNG, docProfileFile));
                 }
                else {
                    builder.addFormDataPart(APIClass.KEY_REGISTER_PROFILE_IMAGE, "", RequestBody.create(MEDIA_TYPE_PNG, ""));
                }
                if(age_get != null) {
                    builder.addFormDataPart(APIClass.KEY_REGISTER_AGE, age_get);
                }
                 else {
                    builder.addFormDataPart(APIClass.KEY_REGISTER_AGE, "");
                }
                if(website_get != null) {
                  builder.addFormDataPart(APIClass.KEY_REGISTER_WEBSITE, website_get);
                }
                 else {
                    builder.addFormDataPart(APIClass.KEY_REGISTER_WEBSITE, "");
                }
                 if(onlineOpCharge_get != null) {
                     builder.addFormDataPart(APIClass.KEY_REGISTER_ONLINEOP_CHARGE, onlineOpCharge_get);
                 }
                else {
                     builder.addFormDataPart(APIClass.KEY_REGISTER_ONLINEOP_CHARGE, "");
                 }
                 if(inpersonOpCharge_get != null) {
                       builder.addFormDataPart(APIClass.KEY_REGISTER_INPERSONOP_CHARGE, inpersonOpCharge_get);
                 }
                 else {
                     builder.addFormDataPart(APIClass.KEY_REGISTER_INPERSONOP_CHARGE, "");
                 }
                if(consultationCharge_get != null) {
                      builder.addFormDataPart(APIClass.KEY_REGISTER_CONSULT_CHARGE, consultationCharge_get);
                }
                 else {
                    builder.addFormDataPart(APIClass.KEY_REGISTER_CONSULT_CHARGE, "");
                }
                if(researchDetails_get != null) {
                       builder.addFormDataPart(APIClass.KEY_REGISTER_RESEARCH, researchDetails_get);
                }
                else {
                    builder.addFormDataPart(APIClass.KEY_REGISTER_RESEARCH, "");
                }
                if(publications_get != null) {
                    builder.addFormDataPart(APIClass.KEY_REGISTER_PUBLICATION, publications_get);
                }
                 else {
                    builder.addFormDataPart(APIClass.KEY_REGISTER_PUBLICATION, "");
                }
                if(teleOpinionNum_get != null) {
                    builder.addFormDataPart(APIClass.KEY_REGISTER_TELEOP_CONTACT, teleOpinionNum_get);
                }
                else {
                    builder.addFormDataPart(APIClass.KEY_REGISTER_TELEOP_CONTACT, "");
                }
                if(videoOpinionNum_get != null) {
                    builder.addFormDataPart(APIClass.KEY_REGISTER_VIDOP_CONTACT, videoOpinionNum_get);
                }
                else {
                    builder.addFormDataPart(APIClass.KEY_REGISTER_VIDOP_CONTACT, "");
                }

            //  builder.build();
            requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_HOST_DOCTORS_SIGNUP)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, res.toString());
            Log.e(Utils.TAG, "Error: " + res);
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return null;

    }

    public static JSONObject createMyPatientTemplate(String template_name, List<PrescriptionList> GET_PRECRIPTION_LIST, int USER_ID, String USER_LOGIN_TYPE) {

        try {

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            ArrayList<String> tradeNameArrayList = new ArrayList<String>();
            ArrayList<String> genericNameArrayList = new ArrayList<String>();
            ArrayList<String> dosageArrayList = new ArrayList<String>();
            ArrayList<String> routeArrayList = new ArrayList<String>();
            ArrayList<String> frequencyArrayList = new ArrayList<String>();
            ArrayList<String> instructionArrayList = new ArrayList<String>();

            for(int j=0;j<GET_PRECRIPTION_LIST.size();j++) {
                tradeNameArrayList.add(GET_PRECRIPTION_LIST.get(j).getPrescTradeName());
                genericNameArrayList.add(GET_PRECRIPTION_LIST.get(j).getPrescGenericName());
                dosageArrayList.add(GET_PRECRIPTION_LIST.get(j).getPrescDosage());
                routeArrayList.add(GET_PRECRIPTION_LIST.get(j).getPrescRoute());
                frequencyArrayList.add(GET_PRECRIPTION_LIST.get(j).getPrescFrequency());
                instructionArrayList.add(GET_PRECRIPTION_LIST.get(j).getPrescInstruction());
            }


            String[] tradeName_array = new String[tradeNameArrayList.size()];
            String[] genericName_array = new String[genericNameArrayList.size()];
            String[] dosage_array = new String[dosageArrayList.size()];
            String[] route_array = new String[routeArrayList.size()];
            String[] frequency_array = new String[frequencyArrayList.size()];
            String[] instruction_array = new String[instructionArrayList.size()];

            if(tradeNameArrayList.size() > 0) {
                for (int j = 0; j < tradeNameArrayList.size(); j++) {
                    tradeName_array[j] = tradeNameArrayList.get(j);
                    genericName_array[j] = genericNameArrayList.get(j);
                    dosage_array[j]  = dosageArrayList.get(j);
                    route_array[j]  = routeArrayList.get(j);
                    frequency_array[j]  = frequencyArrayList.get(j);
                    instruction_array[j]  = instructionArrayList.get(j);
                    Log.d(Utils.TAG + " TRADENAME: ", String.valueOf(tradeName_array[j].toString()));
                    builder.addFormDataPart("prescription_trade_name[]", tradeName_array[j].toString());
                    builder.addFormDataPart("prescription_generic_name[]", genericName_array[j].toString());
                    builder.addFormDataPart("prescription_dosage_name[]", dosage_array[j].toString());
                    builder.addFormDataPart("prescription_route[]", route_array[j].toString());
                    builder.addFormDataPart("prescription_frequency[]", frequency_array[j].toString());
                    builder.addFormDataPart("prescription_instruction[]", instruction_array[j].toString());
                }
            }

            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("userid", String.valueOf(USER_ID));
            builder.addFormDataPart("login_type", USER_LOGIN_TYPE);
            builder.addFormDataPart(APIClass.KEY_MY_PATIENT_TEMPLATE_INAME, template_name);
            builder.addFormDataPart(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);

            RequestBody requestBody = builder.build();


            Request request = new Request.Builder()
                    .url(APIClass.DRS_PRACTICE_CREATE_MYPATIENT_TEMPLATE)
                    .post(requestBody)
                    .build();

            //   OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " create: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }


        return null;
    }

    public static JSONObject createMyPatient(String get_name, String get_gender, String get_mobile, String get_city, String get_age,
                                             String get_weight, String get_hypertension, String get_diabetes, String get_country_code,
                                             String get_country_name, String get_country_name_code, String get_email, String get_address,
                                             String get_chiefcomplaint, String get_detailed_description,
                                             ArrayList<String> get_attachment_photos, List<EpisodesList> get_episode_list, int user_id,
                                             String loginType) {

        try {

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for (int i = 0; i < get_attachment_photos.size(); i++) {

                File file = new File(get_attachment_photos.get(i));
                if (file.exists()) {
                    final MediaType MEDIA_TYPE = MediaType.parse("image/*");
                    builder.addFormDataPart("file-3[]", file.getName(), RequestBody.create(MEDIA_TYPE, file));
                } else {
                    Log.d(Utils.TAG, "file not exist ");
                }
            }

            ArrayList<String> tradeNameArrayList = new ArrayList<String>();
            ArrayList<String> genericNameArrayList = new ArrayList<String>();
            ArrayList<String> dosageArrayList = new ArrayList<String>();
            ArrayList<String> routeArrayList = new ArrayList<String>();
            ArrayList<String> frequencyArrayList = new ArrayList<String>();
            ArrayList<String> instructionArrayList = new ArrayList<String>();
            ArrayList<String> episode_attachment_photos = new ArrayList<String>();
            /*for (int i = 0; i < get_episode_list.size(); i++) {
                Log.d(Utils.TAG + " PRESC: ", String.valueOf(get_episode_list.get(i).getPrescriptionList().size()));
                for(int j=0;j<get_episode_list.get(i).getPrescriptionList().size();j++) {
                    tradeNameArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescTradeName());
                    genericNameArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescGenericName());
                    dosageArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescDosage());
                    routeArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescRoute());
                    frequencyArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescFrequency());
                    instructionArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescInstruction());
                }

                for(int k=0;k<get_episode_list.get(i).getEpisodePhotos().size();k++) {
                    episode_attachment_photos.add(get_episode_list.get(i).getEpisodePhotos().get(k).toString());
                    //  builder.addFormDataPart("episode_photos[]", get_episode_list.get(i).getEpisodePhotos().get(k).toString());

                    File file = new File(get_episode_list.get(i).getEpisodePhotos().get(k));
                    if (file.exists()) {
                        final MediaType MEDIA_TYPE = MediaType.parse("image/*");
                        builder.addFormDataPart("txtPhoto[]", file.getName(), RequestBody.create(MEDIA_TYPE, file));
                    } else {
                        Log.d(Utils.TAG, "file not exist ");
                    }
                }
            }*/

            String[] tradeName_array = new String[tradeNameArrayList.size()];
            String[] genericName_array = new String[genericNameArrayList.size()];
            String[] dosage_array = new String[dosageArrayList.size()];
            String[] route_array = new String[routeArrayList.size()];
            String[] frequency_array = new String[frequencyArrayList.size()];
            String[] instruction_array = new String[instructionArrayList.size()];

            if(tradeNameArrayList.size() > 0) {
                for (int j = 0; j < tradeNameArrayList.size(); j++) {
                    tradeName_array[j] = tradeNameArrayList.get(j);
                    genericName_array[j] = genericNameArrayList.get(j);
                    dosage_array[j]  = dosageArrayList.get(j);
                    route_array[j]  = routeArrayList.get(j);
                    frequency_array[j]  = frequencyArrayList.get(j);
                    instruction_array[j]  = instructionArrayList.get(j);
                    Log.d(Utils.TAG + " inst: ", String.valueOf(tradeName_array[j].toString()));
                    builder.addFormDataPart("prescription_trade_name[]", tradeName_array[j].toString());
                    builder.addFormDataPart("prescription_generic_name[]", genericName_array[j].toString());
                    builder.addFormDataPart("prescription_dosage_name[]", dosage_array[j].toString());
                    builder.addFormDataPart("prescription_route[]", route_array[j].toString());
                    builder.addFormDataPart("prescription_frequency[]", frequency_array[j].toString());
                    builder.addFormDataPart("prescription_instruction[]", instruction_array[j].toString());
                }
            }

            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", loginType);
            builder.addFormDataPart("se_pat_name", get_name);
            builder.addFormDataPart("se_gender", get_gender);
            builder.addFormDataPart("se_city", get_city);
            builder.addFormDataPart("se_country", get_country_name);
            builder.addFormDataPart("se_countryNameCode", get_country_name_code);
            builder.addFormDataPart("se_countryCode", get_country_code);
            builder.addFormDataPart(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);

            if(get_mobile != null){
                builder.addFormDataPart("se_phone_no", get_mobile);
            }

            if(get_age != null){
                builder.addFormDataPart("se_pat_age", get_age);
            }
            if(get_weight != null) {
                builder.addFormDataPart("se_weight", get_weight);
            }
            if(get_city != null) {
                builder.addFormDataPart("txtCity", get_city);
            }
            if(get_hypertension != null) {
                builder.addFormDataPart("se_hyper", get_hypertension);
            }
            if(get_diabetes != null) {
                builder.addFormDataPart("se_diabets", get_diabetes);
            }
            if(get_email != null) {
                builder.addFormDataPart("se_email", get_email);
            }
            if(get_address != null) {
                builder.addFormDataPart("se_address", get_address);
            }
            if(get_chiefcomplaint != null) {
                builder.addFormDataPart("episode_medical_complaint", get_chiefcomplaint);
            }
            if(get_detailed_description != null) {
                builder.addFormDataPart("episode_desc", get_detailed_description);
            }

            // episode_special_instruction =  $_POST['episode_special_instruction'];

            RequestBody requestBody = builder.build();


            Request request = new Request.Builder()
                    .url(APIClass.DRS_PRACTICE_CREATE_MYPATIENT_NEW)
                    .post(requestBody)
                    .build();

            //   OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " create: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }


        return null;
    }

    public static JSONObject addMyPatientEpisodes(String patient_id, String get_chiefcomplaint, String get_detailed_description,
                                                  List<EpisodesList> get_episode_list, int user_id, String loginType) {

        try {

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            ArrayList<String> tradeNameArrayList = new ArrayList<String>();
            ArrayList<String> genericNameArrayList = new ArrayList<String>();
            ArrayList<String> dosageArrayList = new ArrayList<String>();
            ArrayList<String> routeArrayList = new ArrayList<String>();
            ArrayList<String> frequencyArrayList = new ArrayList<String>();
            ArrayList<String> instructionArrayList = new ArrayList<String>();
            ArrayList<String> episode_attachment_photos = new ArrayList<String>();
            /*for (int i = 0; i < get_episode_list.size(); i++) {
                Log.d(Utils.TAG + " PRESC: ", String.valueOf(get_episode_list.get(i).getPrescriptionList().size()));
                for(int j=0;j<get_episode_list.get(i).getPrescriptionList().size();j++) {
                    tradeNameArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescTradeName());
                    genericNameArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescGenericName());
                    dosageArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescDosage());
                    routeArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescRoute());
                    frequencyArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescFrequency());
                    instructionArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescInstruction());
                }

                for(int k=0;k<get_episode_list.get(i).getEpisodePhotos().size();k++) {
                    episode_attachment_photos.add(get_episode_list.get(i).getEpisodePhotos().get(k).toString());
                  //  builder.addFormDataPart("txtPhoto[]", get_episode_list.get(i).getEpisodePhotos().get(k).toString());
                    Log.d(Utils.TAG + " txtPhoto: ", String.valueOf(get_episode_list.get(i).getEpisodePhotos().get(k).toString()));

                    File file = new File(get_episode_list.get(i).getEpisodePhotos().get(k));
                    if (file.exists()) {
                        final MediaType MEDIA_TYPE = MediaType.parse("image/*");
                        builder.addFormDataPart("txtPhoto[]", file.getName(), RequestBody.create(MEDIA_TYPE, file));
                    } else {
                        Log.d(Utils.TAG, "file not exist ");
                    }

                }
            }*/

            String[] tradeName_array = new String[tradeNameArrayList.size()];
            String[] genericName_array = new String[genericNameArrayList.size()];
            String[] dosage_array = new String[dosageArrayList.size()];
            String[] route_array = new String[routeArrayList.size()];
            String[] frequency_array = new String[frequencyArrayList.size()];
            String[] instruction_array = new String[instructionArrayList.size()];

            if(tradeNameArrayList.size() > 0) {
                for (int j = 0; j < tradeNameArrayList.size(); j++) {
                    tradeName_array[j] = tradeNameArrayList.get(j);
                    genericName_array[j] = genericNameArrayList.get(j);
                    dosage_array[j]  = dosageArrayList.get(j);
                    route_array[j]  = routeArrayList.get(j);
                    frequency_array[j]  = frequencyArrayList.get(j);
                    instruction_array[j]  = instructionArrayList.get(j);
                    Log.d(Utils.TAG + " inst: ", String.valueOf(tradeName_array[j].toString()));
                    builder.addFormDataPart("prescription_trade_name[]", tradeName_array[j].toString());
                    builder.addFormDataPart("prescription_generic_name[]", genericName_array[j].toString());
                    builder.addFormDataPart("prescription_dosage_name[]", dosage_array[j].toString());
                    builder.addFormDataPart("prescription_route[]", route_array[j].toString());
                    builder.addFormDataPart("prescription_frequency[]", frequency_array[j].toString());
                    builder.addFormDataPart("prescription_instruction[]", instruction_array[j].toString());
                }
            }

            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", loginType);
            builder.addFormDataPart("se_patient_id", patient_id);
            builder.addFormDataPart(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);

            if(get_chiefcomplaint != null) {
                builder.addFormDataPart("episode_medical_complaint", get_chiefcomplaint);
            }
            if(get_detailed_description != null) {
                builder.addFormDataPart("episode_desc", get_detailed_description);
            }

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_PRACTICE_MYPATIENT_ADDEPISODE)
                    .post(requestBody)
                    .build();

            //   OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " create: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }


        return null;
    }

    public static JSONObject applyJob(String coverNote, String attach_path, int partnerId, int eventId) {

        Log.d(Utils.TAG, " coverNote: "+ coverNote);
        Log.d(Utils.TAG, " attach_path: "+ attach_path);
        Log.d(Utils.TAG, " partnerId: "+ String.valueOf(partnerId));
        Log.d(Utils.TAG, " eventId: "+ String.valueOf(eventId));
        try {

            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);

            File file = new File(attach_path);
            if(file.exists()) {
                final MediaType MEDIA_TYPE = MediaType.parse("application/json");
                builder.addFormDataPart("txtAttach",file.getName(),RequestBody.create(MEDIA_TYPE,file));
            }
            else {
                Log.d(Utils.TAG, "file not exist ");
            }

            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("coverNote", coverNote);
            builder.addFormDataPart("event_id", String.valueOf(eventId));
            builder.addFormDataPart("partner_id", String.valueOf(partnerId));
            builder.addFormDataPart(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_PRACTICE_APPLYJOB)
                    .post(requestBody)
                    .build();

            //   OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            // Log.d(Utils.TAG, res.toString());
            Log.d(Utils.TAG, "attach response: " + res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject uploadEditProfile(String get_name, String get_mobile, String get_country_code, String get_country_name,
                                               String get_country_name_code, String get_state, String get_specialization_name,
                                               int get_spec_id, String get_city, String get_select_hospital, String get_qualification,
                                               String get_experience, String get_email, String get_website, String get_expertise,
                                               String get_contribution, String get_research, String get_publication,
                                               String get_imagepath, int user_id, String loginType) {
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);

            File file = new File(get_imagepath);
            if(file.exists()) {
                final MediaType MEDIA_TYPE = MediaType.parse("image/*");
                builder.addFormDataPart("txtPhoto",file.getName(),RequestBody.create(MEDIA_TYPE,file));
            }
            else {
                Log.d(Utils.TAG, "file not exist ");
            }

            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart(APIClass.KEY_USERID, String.valueOf(user_id));
            builder.addFormDataPart(APIClass.KEY_LOGINTYPE, loginType);
            builder.addFormDataPart("txtDoc", get_name);
            builder.addFormDataPart("txtCountry", get_country_name);
            builder.addFormDataPart("txtCountryCode", get_country_code);
            builder.addFormDataPart("txtCountryNameCode", get_country_name_code);
            builder.addFormDataPart("slctSpecName", get_specialization_name);
            builder.addFormDataPart("slctSpec", String.valueOf(get_spec_id));
            builder.addFormDataPart(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);

            if(get_mobile != null){
                builder.addFormDataPart("txtMobile", get_mobile);
            }
            if(get_state != null) {
                builder.addFormDataPart("slctState", get_state);
            }
            if(get_city != null) {
                builder.addFormDataPart("txtCity", get_city);
            }
            if(get_select_hospital != null) {
                builder.addFormDataPart("selectHosp", get_select_hospital);
            }
            if(get_qualification != null) {
                builder.addFormDataPart("txtQual", get_qualification);
            }
            if(get_experience != null) {
                builder.addFormDataPart("txtExp", get_experience);
            }
            if(get_email != null) {
                builder.addFormDataPart("txtEmail", get_email);
            }
            if(get_website != null) {
                builder.addFormDataPart("txtWebsite", get_website);
            }
            if(get_expertise != null) {
                builder.addFormDataPart("txtInterest", get_expertise);
            }
            if(get_contribution != null) {
                builder.addFormDataPart("txtContribute", get_contribution);
            }
            if(get_research != null) {
                builder.addFormDataPart("txtResearch", get_research);
            }
            if(get_publication != null) {
                builder.addFormDataPart("txtPublication", get_publication);
            }


            RequestBody requestBody = builder.build();


            Request request = new Request.Builder()
                    .url(APIClass.DRS_PREM_EDIT_PROFILE)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, "edit: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject submitQuizReport(List<QuizQuestion> submitList, int quiz_setid, int user_id, String user_login_type) {

        try {

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            ArrayList<String> questionsIDArrayList = new ArrayList<String>();
            ArrayList<String> AnsweIDArrayList = new ArrayList<String>();
            ArrayList<String> userIDArrayList = new ArrayList<String>();
            ArrayList<String> loginTyeArrayList = new ArrayList<String>();

            for (int i = 0; i < submitList.size(); i++) {
                Log.d(Utils.TAG + " QUIZ: ", String.valueOf(submitList.size()));
                questionsIDArrayList.add(String.valueOf(submitList.get(i).getQASectionID()));
                AnsweIDArrayList.add(String.valueOf(submitList.get(i).getOptAnsID()));
                userIDArrayList.add(String.valueOf(submitList.get(i).getUserID()));
                loginTyeArrayList.add(submitList.get(i).getLoginType());
            }

            String[] questionsID_array = new String[questionsIDArrayList.size()];
            String[] AnsweID_array = new String[AnsweIDArrayList.size()];
            String[] userIDA_array = new String[userIDArrayList.size()];
            String[] loginTye_array = new String[loginTyeArrayList.size()];

            if(questionsIDArrayList.size() > 0) {
                for (int j = 0; j < questionsIDArrayList.size(); j++) {
                    questionsID_array[j] = questionsIDArrayList.get(j);
                    AnsweID_array[j] = AnsweIDArrayList.get(j);
                    userIDA_array[j]  = userIDArrayList.get(j);
                    loginTye_array[j]  = loginTyeArrayList.get(j);
                    builder.addFormDataPart("question_id_array[]", questionsID_array[j].toString());
                    builder.addFormDataPart("answer_id_array[]", AnsweID_array[j].toString());
                    builder.addFormDataPart("user_id_array[]", userIDA_array[j].toString());
                    builder.addFormDataPart("login_type_array[]", loginTye_array[j].toString());
                }
            }
            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", user_login_type);
            builder.addFormDataPart("qa_setid", String.valueOf(quiz_setid));
            builder.addFormDataPart(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_QUIZ_SUBMIT)
                    .post(requestBody)
                    .build();

            //   OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " submit: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }


        return null;
    }

    public static JSONObject createMyPatientOptho(String get_name, String get_gender, String get_mobile, String get_city,
                                                  String get_age, String get_weight, String get_hypertension, String get_diabetes,
                                                  String get_country_code, String get_country_name, String get_country_name_code,
                                                  String get_email, String get_address, List<EpisodesOpthoList> get_episode_list,
                                                  int user_id, String user_login_type) {

        String GET_MEDICAL_COMPLAINT = "", GET_DIAGNOSYS = "", GET_TREATMENT ="";
        String GET_LIDS = "", GET_CONJUCTIVA = "", GET_SCLERA = "", GET_CORNEA = "", GET_AC = "", GET_IRIS = "", GET_LENS = "", GET_VITEROUS = "", GET_SPECIAL_INVESTIGATION = "";
        String GET_TENSION_RIGHT = "", GET_TENSION_LEFT = "", GET_LACRIMAL = "", GET_BP = "", GET_SUGAR = "";
        String GET_DV_SPHERE_RIGHT = "", GET_DV_CYL_RIGHT = "", GET_DV_AXIS_RIGHT = "";
        String GET_NV_SPHERE_RIGHT = "", GET_NV_CYL_RIGHT = "", GET_NV_AXIS_RIGHT = "";
        String GET_IPD_RIGHT = "";
        String GET_DV_SPHERE_LEFT = "", GET_DV_CYL_LEFT = "", GET_DV_AXIS_LEFT = "";
        String GET_NV_SPHERE_LEFT = "", GET_NV_CYL_LEFT = "", GET_NV_AXIS_LEFT = "";
        String GET_IPD_LEFT = "";
        String GET_BIFOCAL = "", GET_COLOUR = "", GET_REMARKS = "";

        try {

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            ArrayList<String> tradeNameArrayList = new ArrayList<String>();
            ArrayList<String> genericNameArrayList = new ArrayList<String>();
            ArrayList<String> dosageArrayList = new ArrayList<String>();
            ArrayList<String> routeArrayList = new ArrayList<String>();
            ArrayList<String> frequencyArrayList = new ArrayList<String>();
            ArrayList<String> instructionArrayList = new ArrayList<String>();
            ArrayList<String> episode_attachment_photos = new ArrayList<String>();
            for (int i = 0; i < get_episode_list.size(); i++) {

                GET_MEDICAL_COMPLAINT = get_episode_list.get(i).getEpisodeChiefMedComplaint().toString().trim();
                GET_DIAGNOSYS = get_episode_list.get(i).getEpDiagnosys().toString().trim();
                GET_TREATMENT = get_episode_list.get(i).getEpTreatment().toString().trim();
                GET_LIDS = get_episode_list.get(i).getEpLids().toString().trim();
                GET_CONJUCTIVA = get_episode_list.get(i).getEpConjuctiva().toString().trim();
                GET_SCLERA = get_episode_list.get(i).getEpSclera().toString().trim();
                GET_CORNEA = get_episode_list.get(i).getEpCornea().toString().trim();
                GET_AC = get_episode_list.get(i).getEpAc().toString().trim();
                GET_IRIS = get_episode_list.get(i).getEpIris().toString().trim();
                GET_LENS = get_episode_list.get(i).getEpLens().toString().trim();
                GET_VITEROUS = get_episode_list.get(i).getEpViterous().toString().trim();
                GET_SPECIAL_INVESTIGATION = get_episode_list.get(i).getEpSpecialInvestigation().toString().trim();
                GET_TENSION_RIGHT = get_episode_list.get(i).getEpTensionRight().toString().trim();
                GET_TENSION_LEFT = get_episode_list.get(i).getEpTensionLeft().toString().trim();
                GET_LACRIMAL = get_episode_list.get(i).getEpLacrimal().toString().trim();
                GET_BP = get_episode_list.get(i).getEpBp().toString().trim();
                GET_SUGAR = get_episode_list.get(i).getEpSugar().toString().trim();
                GET_DV_SPHERE_RIGHT = get_episode_list.get(i).getEpDVSphereRight().toString().trim();
                GET_DV_CYL_RIGHT  = get_episode_list.get(i).getEpDVCylRight().toString().trim();
                GET_DV_AXIS_RIGHT = get_episode_list.get(i).getEpDVAxisRight().toString().trim();
                GET_NV_SPHERE_RIGHT = get_episode_list.get(i).getEpNVSphereRight().toString().trim();
                GET_NV_CYL_RIGHT = get_episode_list.get(i).getEpNVCylRight().toString().trim();
                GET_NV_AXIS_RIGHT = get_episode_list.get(i).getEpNVAxisRight().toString().trim();
                GET_IPD_RIGHT = get_episode_list.get(i).getEpIpdRight().toString().trim();
                GET_DV_SPHERE_LEFT = get_episode_list.get(i).getEpDVSphereLeft().toString().trim();
                GET_DV_CYL_LEFT = get_episode_list.get(i).getEpDVCylLeft().toString().trim();
                GET_DV_AXIS_LEFT = get_episode_list.get(i).getEpDVAxisLeft().toString().trim();
                GET_NV_SPHERE_LEFT = get_episode_list.get(i).getEpNVSphereLeft().toString().trim();
                GET_NV_CYL_LEFT = get_episode_list.get(i).getEpNVCylLeft().toString().trim();
                GET_NV_AXIS_LEFT = get_episode_list.get(i).getEpNVAxisLeft().toString().trim();
                GET_IPD_LEFT  = get_episode_list.get(i).getEpIpdLeft().toString().trim();
                GET_BIFOCAL  = get_episode_list.get(i).getEpBifocal().toString().trim();
                GET_COLOUR = get_episode_list.get(i).getEpColour().toString().trim();
                GET_REMARKS = get_episode_list.get(i).getEpRemarks().toString().trim();

                Log.d(Utils.TAG + " PRESC: ", String.valueOf(get_episode_list.get(i).getPrescriptionList().size()));
                for(int j=0;j<get_episode_list.get(i).getPrescriptionList().size();j++) {
                    tradeNameArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescTradeName());
                    genericNameArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescGenericName());
                    dosageArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescDosage());
                    routeArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescRoute());
                    frequencyArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescFrequency());
                    instructionArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescInstruction());
                }

                if(get_episode_list.get(i).getEpisodePhotos() != null && !get_episode_list.get(i).getEpisodePhotos().isEmpty())
                {
                    for(int k=0;k<get_episode_list.get(i).getEpisodePhotos().size();k++) {
                        episode_attachment_photos.add(get_episode_list.get(i).getEpisodePhotos().get(k).toString());
                        //  builder.addFormDataPart("episode_photos[]", get_episode_list.get(i).getEpisodePhotos().get(k).toString());

                        File file = new File(get_episode_list.get(i).getEpisodePhotos().get(k));
                        if (file.exists()) {
                            final MediaType MEDIA_TYPE = MediaType.parse("image/*");
                            builder.addFormDataPart("txtPhoto[]", file.getName(), RequestBody.create(MEDIA_TYPE, file));
                        } else {
                            Log.d(Utils.TAG, "file not exist ");
                        }
                    }
                }

                /*for(int k=0;k<get_episode_list.get(i).getEpisodePhotos().size();k++) {
                    episode_attachment_photos.add(get_episode_list.get(i).getEpisodePhotos().get(k).toString());
                    //  builder.addFormDataPart("episode_photos[]", get_episode_list.get(i).getEpisodePhotos().get(k).toString());

                    File file = new File(get_episode_list.get(i).getEpisodePhotos().get(k));
                    if (file.exists()) {
                        final MediaType MEDIA_TYPE = MediaType.parse("image*//*");
                        builder.addFormDataPart("txtPhoto[]", file.getName(), RequestBody.create(MEDIA_TYPE, file));
                    } else {
                        Log.d(Utils.TAG, "file not exist ");
                    }
                }*/
            }

            String[] tradeName_array = new String[tradeNameArrayList.size()];
            String[] genericName_array = new String[genericNameArrayList.size()];
            String[] dosage_array = new String[dosageArrayList.size()];
            String[] route_array = new String[routeArrayList.size()];
            String[] frequency_array = new String[frequencyArrayList.size()];
            String[] instruction_array = new String[instructionArrayList.size()];

            if(tradeNameArrayList.size() > 0) {
                for (int j = 0; j < tradeNameArrayList.size(); j++) {
                    tradeName_array[j] = tradeNameArrayList.get(j);
                    genericName_array[j] = genericNameArrayList.get(j);
                    dosage_array[j]  = dosageArrayList.get(j);
                    route_array[j]  = routeArrayList.get(j);
                    frequency_array[j]  = frequencyArrayList.get(j);
                    instruction_array[j]  = instructionArrayList.get(j);
                    Log.d(Utils.TAG + " inst: ", String.valueOf(tradeName_array[j].toString()));
                    builder.addFormDataPart("prescription_trade_name[]", tradeName_array[j].toString());
                    builder.addFormDataPart("prescription_generic_name[]", genericName_array[j].toString());
                    builder.addFormDataPart("prescription_dosage_name[]", dosage_array[j].toString());
                    builder.addFormDataPart("prescription_route[]", route_array[j].toString());
                    builder.addFormDataPart("prescription_frequency[]", frequency_array[j].toString());
                    builder.addFormDataPart("prescription_instruction[]", instruction_array[j].toString());
                }
            }

            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", user_login_type);
            builder.addFormDataPart("se_pat_name", get_name);
            builder.addFormDataPart("se_gender", get_gender);
            builder.addFormDataPart("se_city", get_city);
            builder.addFormDataPart("se_country", get_country_name);
            builder.addFormDataPart("se_countryNameCode", get_country_name_code);
            builder.addFormDataPart("se_countryCode", get_country_code);
            builder.addFormDataPart(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);

            if(get_mobile != null){
                builder.addFormDataPart("se_phone_no", get_mobile);
            }
            if(get_age != null){
                builder.addFormDataPart("se_pat_age", get_age);
            }
            if(get_weight != null) {
                builder.addFormDataPart("se_weight", get_weight);
            }
            if(get_city != null) {
                builder.addFormDataPart("txtCity", get_city);
            }
            if(get_hypertension != null) {
                builder.addFormDataPart("se_hyper", get_hypertension);
            }
            if(get_diabetes != null) {
                builder.addFormDataPart("se_diabets", get_diabetes);
            }
            if(get_email != null) {
                builder.addFormDataPart("se_email", get_email);
            }
            if(get_address != null) {
                builder.addFormDataPart("se_address", get_address);
            }
            if(GET_MEDICAL_COMPLAINT != null) {
                builder.addFormDataPart("episode_medical_complaint", GET_MEDICAL_COMPLAINT);
            }
            if(GET_DIAGNOSYS != null) {
                builder.addFormDataPart("episode_diagnosys", GET_DIAGNOSYS);
            }
            if(GET_TREATMENT != null) {
                builder.addFormDataPart("episode_treatment", GET_TREATMENT);
            }

            if(GET_LIDS != null) {
                builder.addFormDataPart("episode_lids", GET_LIDS);
            }
            if(GET_CONJUCTIVA != null) {
                builder.addFormDataPart("episode_conjuctiva", GET_CONJUCTIVA);
            }
            if(GET_SCLERA != null) {
                builder.addFormDataPart("episode_sclera", GET_SCLERA);
            }
            if(GET_CORNEA != null) {
                builder.addFormDataPart("episode_cornea", GET_CORNEA);
            }
            if(GET_AC != null) {
                builder.addFormDataPart("episode_ac", GET_AC);
            }
            if(GET_IRIS != null) {
                builder.addFormDataPart("episode_iris", GET_IRIS);
            }
            if(GET_LENS != null) {
                builder.addFormDataPart("episode_lens", GET_LENS);
            }
            if(GET_VITEROUS != null) {
                builder.addFormDataPart("episode_viterous", GET_VITEROUS);
            }
            if(GET_SPECIAL_INVESTIGATION != null) {
                builder.addFormDataPart("episode_spec_investigation", GET_SPECIAL_INVESTIGATION);
            }

            if(GET_TENSION_RIGHT != null) {
                builder.addFormDataPart("episode_tension_right", GET_TENSION_RIGHT);
            }
            if(GET_TENSION_LEFT != null) {
                builder.addFormDataPart("episode_tension_left", GET_TENSION_LEFT);
            }
            if(GET_LACRIMAL != null) {
                builder.addFormDataPart("episode_lacrimal", GET_LACRIMAL);
            }
            if(GET_BP != null) {
                builder.addFormDataPart("episode_bp", GET_BP);
            }
            if(GET_SUGAR != null) {
                builder.addFormDataPart("episode_sugar", GET_SUGAR);
            }
            if(GET_DV_SPHERE_RIGHT != null) {
                builder.addFormDataPart("episode_dvsphere_right", GET_DV_SPHERE_RIGHT);
            }
            if(GET_DV_CYL_RIGHT != null) {
                builder.addFormDataPart("episode_dvcyl_right", GET_DV_CYL_RIGHT);
            }
            if(GET_DV_AXIS_RIGHT != null) {
                builder.addFormDataPart("episode_dvaxis_right", GET_DV_AXIS_RIGHT);
            }
            if(GET_NV_SPHERE_RIGHT != null) {
                builder.addFormDataPart("episode_nvsphere_right", GET_NV_SPHERE_RIGHT);
            }
            if(GET_NV_CYL_RIGHT != null) {
                builder.addFormDataPart("episode_nvcyl_right", GET_NV_CYL_RIGHT);
            }
            if(GET_NV_AXIS_RIGHT != null) {
                builder.addFormDataPart("episode_nvaxis_right", GET_NV_AXIS_RIGHT);
            }
            if(GET_IPD_RIGHT != null) {
                builder.addFormDataPart("episode_ipd_right", GET_IPD_RIGHT);
            }
            if(GET_DV_SPHERE_LEFT != null) {
                builder.addFormDataPart("episode_dvsphere_left", GET_DV_SPHERE_LEFT);
            }
            if(GET_DV_CYL_LEFT != null) {
                builder.addFormDataPart("episode_dvcyl_left", GET_DV_CYL_LEFT);
            }
            if(GET_DV_AXIS_LEFT != null) {
                builder.addFormDataPart("episode_dvaxis_left", GET_DV_AXIS_LEFT);
            }
            if(GET_NV_SPHERE_LEFT != null) {
                builder.addFormDataPart("episode_nvsphere_left", GET_NV_SPHERE_LEFT);
            }
            if(GET_NV_CYL_LEFT != null) {
                builder.addFormDataPart("episode_nvcyl_left", GET_NV_CYL_LEFT);
            }
            if(GET_NV_AXIS_LEFT != null) {
                builder.addFormDataPart("episode_nvaxis_left", GET_NV_AXIS_LEFT);
            }
            if(GET_IPD_LEFT != null) {
                builder.addFormDataPart("episode_ipd_left", GET_IPD_LEFT);
            }
            if(GET_BIFOCAL != null) {
                builder.addFormDataPart("episode_bifocal", GET_BIFOCAL);
            }
            if(GET_COLOUR != null) {
                builder.addFormDataPart("episode_colour", GET_COLOUR);
            }
            if(GET_REMARKS != null) {
                builder.addFormDataPart("episode_remarks", GET_REMARKS);
            }

            RequestBody requestBody = builder.build();


            Request request = new Request.Builder()
                    .url(APIClass.DRS_PRACTICE_CREATE_MYPATIENT)
                    .post(requestBody)
                    .build();

            //   OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " create: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }


        return null;

    }

    public static JSONObject addMyPatientEpisodesOptho(String patient_id, List<EpisodesOpthoList> get_episode_list,
                                                       int user_id, String loginType) {
        String GET_MEDICAL_COMPLAINT = "", GET_DIAGNOSYS = "", GET_TREATMENT ="";
        String GET_LIDS = "", GET_CONJUCTIVA = "", GET_SCLERA = "", GET_CORNEA = "", GET_AC = "", GET_IRIS = "", GET_LENS = "", GET_VITEROUS = "", GET_SPECIAL_INVESTIGATION = "";
        String GET_TENSION_RIGHT = "", GET_TENSION_LEFT = "", GET_LACRIMAL = "", GET_BP = "", GET_SUGAR = "";
        String GET_DV_SPHERE_RIGHT = "", GET_DV_CYL_RIGHT = "", GET_DV_AXIS_RIGHT = "";
        String GET_NV_SPHERE_RIGHT = "", GET_NV_CYL_RIGHT = "", GET_NV_AXIS_RIGHT = "";
        String GET_IPD_RIGHT = "";
        String GET_DV_SPHERE_LEFT = "", GET_DV_CYL_LEFT = "", GET_DV_AXIS_LEFT = "";
        String GET_NV_SPHERE_LEFT = "", GET_NV_CYL_LEFT = "", GET_NV_AXIS_LEFT = "";
        String GET_IPD_LEFT = "";
        String GET_BIFOCAL = "", GET_COLOUR = "", GET_REMARKS = "";

        Log.d(Utils.TAG, " patient_id: "+patient_id);

        try {

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            ArrayList<String> tradeNameArrayList = new ArrayList<String>();
            ArrayList<String> genericNameArrayList = new ArrayList<String>();
            ArrayList<String> dosageArrayList = new ArrayList<String>();
            ArrayList<String> routeArrayList = new ArrayList<String>();
            ArrayList<String> frequencyArrayList = new ArrayList<String>();
            ArrayList<String> instructionArrayList = new ArrayList<String>();
            ArrayList<String> episode_attachment_photos = new ArrayList<String>();
            for (int i = 0; i < get_episode_list.size(); i++) {
                GET_MEDICAL_COMPLAINT = get_episode_list.get(i).getEpisodeChiefMedComplaint().toString().trim();
                GET_DIAGNOSYS = get_episode_list.get(i).getEpDiagnosys().toString().trim();
                GET_TREATMENT = get_episode_list.get(i).getEpTreatment().toString().trim();
                GET_LIDS = get_episode_list.get(i).getEpLids().toString().trim();
                GET_CONJUCTIVA = get_episode_list.get(i).getEpConjuctiva().toString().trim();
                GET_SCLERA = get_episode_list.get(i).getEpSclera().toString().trim();
                GET_CORNEA = get_episode_list.get(i).getEpCornea().toString().trim();
                GET_AC = get_episode_list.get(i).getEpAc().toString().trim();
                GET_IRIS = get_episode_list.get(i).getEpIris().toString().trim();
                GET_LENS = get_episode_list.get(i).getEpLens().toString().trim();
                GET_VITEROUS = get_episode_list.get(i).getEpViterous().toString().trim();
                GET_SPECIAL_INVESTIGATION = get_episode_list.get(i).getEpSpecialInvestigation().toString().trim();
                GET_TENSION_RIGHT = get_episode_list.get(i).getEpTensionRight().toString().trim();
                GET_TENSION_LEFT = get_episode_list.get(i).getEpTensionLeft().toString().trim();
                GET_LACRIMAL = get_episode_list.get(i).getEpLacrimal().toString().trim();
                GET_BP = get_episode_list.get(i).getEpBp().toString().trim();
                GET_SUGAR = get_episode_list.get(i).getEpSugar().toString().trim();
                GET_DV_SPHERE_RIGHT = get_episode_list.get(i).getEpDVSphereRight().toString().trim();
                GET_DV_CYL_RIGHT  = get_episode_list.get(i).getEpDVCylRight().toString().trim();
                GET_DV_AXIS_RIGHT = get_episode_list.get(i).getEpDVAxisRight().toString().trim();
                GET_NV_SPHERE_RIGHT = get_episode_list.get(i).getEpNVSphereRight().toString().trim();
                GET_NV_CYL_RIGHT = get_episode_list.get(i).getEpNVCylRight().toString().trim();
                GET_NV_AXIS_RIGHT = get_episode_list.get(i).getEpNVAxisRight().toString().trim();
                GET_IPD_RIGHT = get_episode_list.get(i).getEpIpdRight().toString().trim();
                GET_DV_SPHERE_LEFT = get_episode_list.get(i).getEpDVSphereLeft().toString().trim();
                GET_DV_CYL_LEFT = get_episode_list.get(i).getEpDVCylLeft().toString().trim();
                GET_DV_AXIS_LEFT = get_episode_list.get(i).getEpDVAxisLeft().toString().trim();
                GET_NV_SPHERE_LEFT = get_episode_list.get(i).getEpNVSphereLeft().toString().trim();
                GET_NV_CYL_LEFT = get_episode_list.get(i).getEpNVCylLeft().toString().trim();
                GET_NV_AXIS_LEFT = get_episode_list.get(i).getEpNVAxisLeft().toString().trim();
                GET_IPD_LEFT  = get_episode_list.get(i).getEpIpdLeft().toString().trim();
                GET_BIFOCAL  = get_episode_list.get(i).getEpBifocal().toString().trim();
                GET_COLOUR = get_episode_list.get(i).getEpColour().toString().trim();
                GET_REMARKS = get_episode_list.get(i).getEpRemarks().toString().trim();

                Log.d(Utils.TAG + " PRESC: ", String.valueOf(get_episode_list.get(i).getPrescriptionList().size()));
                for(int j=0;j<get_episode_list.get(i).getPrescriptionList().size();j++) {
                    tradeNameArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescTradeName());
                    genericNameArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescGenericName());
                    dosageArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescDosage());
                    routeArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescRoute());
                    frequencyArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescFrequency());
                    instructionArrayList.add(get_episode_list.get(i).getPrescriptionList().get(j).getPrescInstruction());
                }

                if(get_episode_list.get(i).getEpisodePhotos() != null && !get_episode_list.get(i).getEpisodePhotos().isEmpty())
                {
                    for(int k=0;k<get_episode_list.get(i).getEpisodePhotos().size();k++) {
                        episode_attachment_photos.add(get_episode_list.get(i).getEpisodePhotos().get(k).toString());
                        //  builder.addFormDataPart("episode_photos[]", get_episode_list.get(i).getEpisodePhotos().get(k).toString());

                        File file = new File(get_episode_list.get(i).getEpisodePhotos().get(k));
                        if (file.exists()) {
                            final MediaType MEDIA_TYPE = MediaType.parse("image/*");
                            builder.addFormDataPart("txtPhoto[]", file.getName(), RequestBody.create(MEDIA_TYPE, file));
                        } else {
                            Log.d(Utils.TAG, "file not exist ");
                        }
                    }
                }
               /* for(int k=0;k<get_episode_list.get(i).getEpisodePhotos().size();k++) {
                    episode_attachment_photos.add(get_episode_list.get(i).getEpisodePhotos().get(k).toString());
                    //  builder.addFormDataPart("episode_photos[]", get_episode_list.get(i).getEpisodePhotos().get(k).toString());

                    File file = new File(get_episode_list.get(i).getEpisodePhotos().get(k));
                    if (file.exists()) {
                        final MediaType MEDIA_TYPE = MediaType.parse("image*//*");
                        builder.addFormDataPart("txtPhoto[]", file.getName(), RequestBody.create(MEDIA_TYPE, file));
                    } else {
                        Log.d(Utils.TAG, "file not exist ");
                    }
                }*/
            }

            String[] tradeName_array = new String[tradeNameArrayList.size()];
            String[] genericName_array = new String[genericNameArrayList.size()];
            String[] dosage_array = new String[dosageArrayList.size()];
            String[] route_array = new String[routeArrayList.size()];
            String[] frequency_array = new String[frequencyArrayList.size()];
            String[] instruction_array = new String[instructionArrayList.size()];

            if(tradeNameArrayList.size() > 0) {
                for (int j = 0; j < tradeNameArrayList.size(); j++) {
                    tradeName_array[j] = tradeNameArrayList.get(j);
                    genericName_array[j] = genericNameArrayList.get(j);
                    dosage_array[j]  = dosageArrayList.get(j);
                    route_array[j]  = routeArrayList.get(j);
                    frequency_array[j]  = frequencyArrayList.get(j);
                    instruction_array[j]  = instructionArrayList.get(j);
                    Log.d(Utils.TAG + " inst: ", String.valueOf(tradeName_array[j].toString()));
                    builder.addFormDataPart("prescription_trade_name[]", tradeName_array[j].toString());
                    builder.addFormDataPart("prescription_generic_name[]", genericName_array[j].toString());
                    builder.addFormDataPart("prescription_dosage_name[]", dosage_array[j].toString());
                    builder.addFormDataPart("prescription_route[]", route_array[j].toString());
                    builder.addFormDataPart("prescription_frequency[]", frequency_array[j].toString());
                    builder.addFormDataPart("prescription_instruction[]", instruction_array[j].toString());
                }
            }

            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", loginType);
            builder.addFormDataPart("se_patient_id", patient_id);
            builder.addFormDataPart(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);

            if(GET_MEDICAL_COMPLAINT != null) {
                builder.addFormDataPart("episode_medical_complaint", GET_MEDICAL_COMPLAINT);
            }
            if(GET_DIAGNOSYS != null) {
                builder.addFormDataPart("episode_diagnosys", GET_DIAGNOSYS);
            }
            if(GET_TREATMENT != null) {
                builder.addFormDataPart("episode_treatment", GET_TREATMENT);
            }

            if(GET_LIDS != null) {
                builder.addFormDataPart("episode_lids", GET_LIDS);
            }
            if(GET_CONJUCTIVA != null) {
                builder.addFormDataPart("episode_conjuctiva", GET_CONJUCTIVA);
            }
            if(GET_SCLERA != null) {
                builder.addFormDataPart("episode_sclera", GET_SCLERA);
            }
            if(GET_CORNEA != null) {
                builder.addFormDataPart("episode_cornea", GET_CORNEA);
            }
            if(GET_AC != null) {
                builder.addFormDataPart("episode_ac", GET_AC);
            }
            if(GET_IRIS != null) {
                builder.addFormDataPart("episode_iris", GET_IRIS);
            }
            if(GET_LENS != null) {
                builder.addFormDataPart("episode_lens", GET_LENS);
            }
            if(GET_VITEROUS != null) {
                builder.addFormDataPart("episode_viterous", GET_VITEROUS);
            }
            if(GET_SPECIAL_INVESTIGATION != null) {
                builder.addFormDataPart("episode_spec_investigation", GET_SPECIAL_INVESTIGATION);
            }

            if(GET_TENSION_RIGHT != null) {
                builder.addFormDataPart("episode_tension_right", GET_TENSION_RIGHT);
            }
            if(GET_TENSION_LEFT != null) {
                builder.addFormDataPart("episode_tension_left", GET_TENSION_LEFT);
            }
            if(GET_LACRIMAL != null) {
                builder.addFormDataPart("episode_lacrimal", GET_LACRIMAL);
            }
            if(GET_BP != null) {
                builder.addFormDataPart("episode_bp", GET_BP);
            }
            if(GET_SUGAR != null) {
                builder.addFormDataPart("episode_sugar", GET_SUGAR);
            }
            if(GET_DV_SPHERE_RIGHT != null) {
                builder.addFormDataPart("episode_dvsphere_right", GET_DV_SPHERE_RIGHT);
            }
            if(GET_DV_CYL_RIGHT != null) {
                builder.addFormDataPart("episode_dvcyl_right", GET_DV_CYL_RIGHT);
            }
            if(GET_DV_AXIS_RIGHT != null) {
                builder.addFormDataPart("episode_dvaxis_right", GET_DV_AXIS_RIGHT);
            }
            if(GET_NV_SPHERE_RIGHT != null) {
                builder.addFormDataPart("episode_nvsphere_right", GET_NV_SPHERE_RIGHT);
            }
            if(GET_NV_CYL_RIGHT != null) {
                builder.addFormDataPart("episode_nvcyl_right", GET_NV_CYL_RIGHT);
            }
            if(GET_NV_AXIS_RIGHT != null) {
                builder.addFormDataPart("episode_nvaxis_right", GET_NV_AXIS_RIGHT);
            }
            if(GET_IPD_RIGHT != null) {
                builder.addFormDataPart("episode_ipd_right", GET_IPD_RIGHT);
            }
            if(GET_DV_SPHERE_LEFT != null) {
                builder.addFormDataPart("episode_dvsphere_left", GET_DV_SPHERE_LEFT);
            }
            if(GET_DV_CYL_LEFT != null) {
                builder.addFormDataPart("episode_dvcyl_left", GET_DV_CYL_LEFT);
            }
            if(GET_DV_AXIS_LEFT != null) {
                builder.addFormDataPart("episode_dvaxis_left", GET_DV_AXIS_LEFT);
            }
            if(GET_NV_SPHERE_LEFT != null) {
                builder.addFormDataPart("episode_nvsphere_left", GET_NV_SPHERE_LEFT);
            }
            if(GET_NV_CYL_LEFT != null) {
                builder.addFormDataPart("episode_nvcyl_left", GET_NV_CYL_LEFT);
            }
            if(GET_NV_AXIS_LEFT != null) {
                builder.addFormDataPart("episode_nvaxis_left", GET_NV_AXIS_LEFT);
            }
            if(GET_IPD_LEFT != null) {
                builder.addFormDataPart("episode_ipd_left", GET_IPD_LEFT);
            }
            if(GET_BIFOCAL != null) {
                builder.addFormDataPart("episode_bifocal", GET_BIFOCAL);
            }
            if(GET_COLOUR != null) {
                builder.addFormDataPart("episode_colour", GET_COLOUR);
            }
            if(GET_REMARKS != null) {
                builder.addFormDataPart("episode_remarks", GET_REMARKS);
            }

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_PRACTICE_MYPATIENT_ADDEPISODE)
                    .post(requestBody)
                    .build();

            //   OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " create: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }


        return null;
    }

    public static JSONObject updateMyPatient(String patient_name, String patient_gender, String patient_mobile, String patient_location,
                                             String patient_age, String patient_weight, String patient_email,
                                             String get_country_code, String get_country_name, String get_country_name_code,
                                             String patient_address, int user_id, String user_login_type, String patient_id,
                                             String patient_hypercondition, String patient_diabetescondition) {


        try {

            Log.d(Utils.TAG, " patient_location: "+patient_location);
            Log.d(Utils.TAG, " patient_gender: "+patient_gender);
            Log.d(Utils.TAG, " patient_id: "+patient_id);

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", user_login_type);
            builder.addFormDataPart("se_pat_name", patient_name);
            builder.addFormDataPart("se_gender", patient_gender);
            builder.addFormDataPart("se_city", patient_location);
            builder.addFormDataPart("se_country", get_country_name);
            builder.addFormDataPart("se_countryNameCode", get_country_name_code);
            builder.addFormDataPart("se_countryCode", get_country_code);
            builder.addFormDataPart("se_patientID", patient_id);
            builder.addFormDataPart(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);

            if(patient_mobile != null){
                builder.addFormDataPart("se_phone_no", patient_mobile);
            }
            if(patient_age != null){
                builder.addFormDataPart("se_pat_age", patient_age);
            }
            if(patient_weight != null) {
                builder.addFormDataPart("se_weight", patient_weight);
            }
            if(patient_location != null) {
                builder.addFormDataPart("se_city", patient_location);
            }
            if(patient_hypercondition != null) {
                builder.addFormDataPart("se_hyper", patient_hypercondition);
            }
            if(patient_diabetescondition != null) {
                builder.addFormDataPart("se_diabets", patient_diabetescondition);
            }
            if(patient_email != null) {
                builder.addFormDataPart("se_email", patient_email);
            }
            if(patient_address != null) {
                builder.addFormDataPart("se_address", patient_address);
            }


            RequestBody requestBody = builder.build();


            Request request = new Request.Builder()
                    .url(APIClass.DRS_PRACTICE_CREATE_MYPATIENT)
                    .post(requestBody)
                    .build();

            //   OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " update: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }


        return null;
    }

    public static JSONObject blog_post(String blog_title, String blog_description, String blog_tags,
                                       String blogImagePath, int user_id, String user_login_type) {

        File blogImageFile = null;
        String profileName = "";
        MediaType MEDIA_TYPE_PNG = null;

        try {

            if(blogImagePath != null) {
                blogImageFile = new File(blogImagePath);
                Log.d(Utils.TAG, "Prof. File...::::" + blogImageFile + " : " + blogImageFile.exists());
                MEDIA_TYPE_PNG = MediaType.parse("image/*");
                profileName = blogImagePath.substring(blogImagePath.lastIndexOf("/")+1);
            }

//            RequestBody requestBody = new MultipartBody.Builder()
            RequestBody requestBody;
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(APIClass.KEY_API_KEY,APIClass.API_KEY)
                    .addFormDataPart(APIClass.KEY_BLOG_POST_TITLE, blog_title)
                    .addFormDataPart(APIClass.KEY_BLOG_POST_DESCRIPTION, blog_description)
                    .addFormDataPart(APIClass.KEY_BLOG_POST_TAGS, blog_tags)
                    .addFormDataPart(APIClass.KEY_USERID, String.valueOf(user_id))
                    .addFormDataPart(APIClass.KEY_LOGINTYPE, user_login_type)
                    .addFormDataPart(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);

            if (blogImagePath != null) {
                builder.addFormDataPart(APIClass.KEY_BLOG_POST_PHOTO, profileName, RequestBody.create(MEDIA_TYPE_PNG, blogImageFile));
            }
            else {
                builder.addFormDataPart(APIClass.KEY_BLOG_POST_PHOTO, "", RequestBody.create(MEDIA_TYPE_PNG, ""));
            }


            //  builder.build();
            requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_BLOG_POST_UPLOAD)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, res.toString());
            Log.e(Utils.TAG, "Error: " + res);
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject video_post(String blog_title, String blog_description, String blog_tags,
                                       String blog_url, int user_id, String user_login_type) {

        File blogImageFile = null;
        String profileName = "";
        MediaType MEDIA_TYPE_PNG = null;

        try {

            RequestBody requestBody;
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(APIClass.KEY_API_KEY,APIClass.API_KEY)
                    .addFormDataPart(APIClass.KEY_VIDEO_POST_TITLE, blog_title)
                    .addFormDataPart(APIClass.KEY_VIDEO_POST_DESCRIPTION, blog_description)
                    .addFormDataPart(APIClass.KEY_VIDEO_POST_TAGS, blog_tags)
                    .addFormDataPart(APIClass.KEY_VIDEO_POST_URL, blog_url)
                    .addFormDataPart(APIClass.KEY_USERID, String.valueOf(user_id))
                    .addFormDataPart(APIClass.KEY_LOGINTYPE, user_login_type)
                    .addFormDataPart(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);

            requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_VIDEO_POST_UPLOAD)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, res.toString());
            Log.e(Utils.TAG, "Error: " + res);
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject uploadSignUpNew(String get_name, String get_mobile, String get_email, String get_hospital_name, String get_city, int get_specializationId, String get_specializationName) {
        try {

            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);

            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("txtName", get_name);
            builder.addFormDataPart("txtCity", get_city);
            builder.addFormDataPart("txtHospitalName", get_hospital_name);
            builder.addFormDataPart("txtMobile", get_mobile);
            builder.addFormDataPart("txtEmail", get_email);
            builder.addFormDataPart("txtSpecId", String.valueOf(get_specializationId));
            builder.addFormDataPart("txtSpecName", get_specializationName);
            builder.addFormDataPart(APIClass.KEY_DEVICE_KEY,APIClass.KEY_DEVICE_VALUE);

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_SIGNUP_NEW)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " regNew: " + res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject createMyPatientNew(String patient_name, String patient_age, String patient_gender,
                                                String patient_mobile, String patient_email, String patient_city,
                                                String patient_address, String patient_country_code, String patient_country_name,
                                                String patient_country_name_code, String patient_height, String patient_weight, int user_id, String user_login_type) {
        try {

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", user_login_type);
            builder.addFormDataPart("se_pat_name", patient_name);
            builder.addFormDataPart("se_gender", patient_gender);
            builder.addFormDataPart("se_city", patient_city);
            builder.addFormDataPart("se_country", patient_country_name);
            builder.addFormDataPart("se_countryNameCode", patient_country_name_code);
            builder.addFormDataPart("se_countryCode", patient_country_code);

            if(patient_height != null){
                builder.addFormDataPart("se_pat_height", patient_height);
            }

            if(patient_weight != null){
                builder.addFormDataPart("se_pat_weight", patient_weight);
            }

            if(patient_age != null){
                builder.addFormDataPart("se_pat_age", patient_age);
            }
            if(patient_mobile != null){
                builder.addFormDataPart("se_phone_no", patient_mobile);
            }

            if(patient_email != null) {
                builder.addFormDataPart("se_email", patient_email);
            }

            if(patient_address != null) {
                builder.addFormDataPart("se_address", patient_address);
            }


            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_MYPATIENT_CREATE)
                    .post(requestBody)
                    .build();

            //   OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " create: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }

        return null;
    }

    public static JSONObject createMyPatientSubmit(String patient_name, String patient_age, String patient_gender,
                                                   String patient_mobile, String patient_email, String patient_address,
                                                   String city, String state, String country, int member_id, int pat_user_id,
                                                   int user_id, String user_login_type, String height, String weight ) {

        try {

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", user_login_type);
            builder.addFormDataPart("se_pat_name", patient_name);
            builder.addFormDataPart("se_gender", patient_gender);
            builder.addFormDataPart("se_member_id", String.valueOf(member_id));
            builder.addFormDataPart("se_patient_login_id", String.valueOf(pat_user_id));

            if(country != null){
                builder.addFormDataPart("se_country", country);
            }

            if(state != null){
                builder.addFormDataPart("se_state", state);
            }

            if(city != null){
                builder.addFormDataPart("se_city", city);
            }

            if(patient_age != null){
                builder.addFormDataPart("se_pat_age", patient_age);
            }
            if(patient_mobile != null){
                builder.addFormDataPart("se_phone_no", patient_mobile);
            }

            if(patient_email != null) {
                builder.addFormDataPart("se_email", patient_email);
            }

            if(patient_address != null) {
                builder.addFormDataPart("se_address", patient_address);
            }

            if(height != null){
                builder.addFormDataPart("se_pat_height", height);
            }

            if(weight != null){
                builder.addFormDataPart("se_pat_weight", weight);
            }

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_MYPATIENT_CREATE_MEMBER)
                    .post(requestBody)
                    .build();

            //   OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " create: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }

        return null;
    }

    public static JSONObject updateSettings(String get_payment_status, String get_preprint, String get_consult_fees,
                                            String get_imagepath, String flash_message,
                                            String header_height,String footer_height, int user_id, String user_login_type) {

        try {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);

            File file = new File(get_imagepath);
            if(file.exists()) {
                final MediaType MEDIA_TYPE = MediaType.parse("image/*");
                builder.addFormDataPart("txtLogo",file.getName(),RequestBody.create(MEDIA_TYPE,file));
                // Log.d(Utils.TAG, " txtLogo: "+file.getName());
            }
            else {
                Log.d(Utils.TAG, "file not exist ");
            }

            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart(APIClass.KEY_USERID, String.valueOf(user_id));
            builder.addFormDataPart(APIClass.KEY_LOGINTYPE, user_login_type);

            if(get_payment_status != null) {
                builder.addFormDataPart("txtPaymentStatus", get_payment_status);
            }
            if(get_preprint != null) {
                builder.addFormDataPart("txtPrePrint", get_preprint);
            }
            if(get_consult_fees != null) {
                builder.addFormDataPart("txtConsulFees", get_consult_fees);
            }
            if(flash_message != null) {
                builder.addFormDataPart("txtFlashMessage", flash_message);
            }
            if(header_height != null) {
                builder.addFormDataPart("txtHeaderHeight", header_height);
            }
            if(footer_height != null) {
                builder.addFormDataPart("txtFooterHeight", footer_height);
            }

            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
                    .url(APIClass.DRS_SETTINGS_UPDATE)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, "updateSettings: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject addHospital(String hosp_name, String hosp_email, String hosp_mobile, String hosp_address,
                                         String hosp_city, String get_state, String get_country_name, int user_id,
                                         String user_login_type) {
        Log.d(Utils.TAG, " *************** addHospital *************************** ");
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", user_login_type);
            builder.addFormDataPart("hosp_name", hosp_name);
            builder.addFormDataPart("hosp_email", hosp_email);
            builder.addFormDataPart("hosp_mobile", hosp_mobile);
            builder.addFormDataPart("hosp_address", hosp_address);
            builder.addFormDataPart("hosp_city", hosp_city);
            builder.addFormDataPart("hosp_state", get_state);
            builder.addFormDataPart("hosp_country", get_country_name);

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_ADD_HOAPITAL)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " addHospital: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject updateHospital(int hospitalId, String hosp_name, String hosp_address,
                                            String hosp_city, String get_state, String get_country_name,
                                            int user_id, String user_login_type) {
        Log.d(Utils.TAG, " *************** updateHospital *************************** ");
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", user_login_type);
            builder.addFormDataPart("hosp_id", String.valueOf(hospitalId));
            builder.addFormDataPart("hosp_name", hosp_name);
            builder.addFormDataPart("hosp_address", hosp_address);
            builder.addFormDataPart("hosp_city", hosp_city);
            builder.addFormDataPart("hosp_state", get_state);
            builder.addFormDataPart("hosp_country", get_country_name);

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_SETTINGS_HOSP_UPDATE)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " updateHospital: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject createMyPatientTemplateGeneral(String template_name, List<FrequentPrescription> GET_PRECRIPTION_LIST, int USER_ID, String USER_LOGIN_TYPE) {

        try {

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            ArrayList<String> PrescriptionIDArrayList = new ArrayList<String>();
            ArrayList<String> tradeNameArrayList = new ArrayList<String>();
            ArrayList<String> genericNameArrayList = new ArrayList<String>();
            ArrayList<String> dosageArrayList = new ArrayList<String>();
            ArrayList<String> routeArrayList = new ArrayList<String>();
            ArrayList<String> frequencyArrayList = new ArrayList<String>();
            ArrayList<String> instructionArrayList = new ArrayList<String>();
            ArrayList<String> timingsArrayList = new ArrayList<String>();
            ArrayList<String> durationArrayList = new ArrayList<String>();

            String[] pp_array = new String[PrescriptionIDArrayList.size()];
            String[] tradeName_array = new String[tradeNameArrayList.size()];
            String[] genericName_array = new String[genericNameArrayList.size()];
            String[] dosage_array = new String[dosageArrayList.size()];
            //  String[] route_array = new String[routeArrayList.size()];
            //  String[] frequency_array = new String[frequencyArrayList.size()];
            //   String[] instruction_array = new String[instructionArrayList.size()];
            String[] timings_array = new String[timingsArrayList.size()];
            String[] duration_array = new String[durationArrayList.size()];

            if(tradeNameArrayList.size() > 0) {
                for (int j = 0; j < tradeNameArrayList.size(); j++) {
                    pp_array[j] = PrescriptionIDArrayList.get(j);
                    tradeName_array[j] = tradeNameArrayList.get(j);
                    genericName_array[j] = genericNameArrayList.get(j);
                    dosage_array[j]  = dosageArrayList.get(j);
                    timings_array[j]  = timingsArrayList.get(j);
                    duration_array[j]  = durationArrayList.get(j);
                    // instruction_array[j]  = instructionArrayList.get(j);
                    Log.d(Utils.TAG + " TRADENAME: ", String.valueOf(tradeName_array[j].toString()));
                    builder.addFormDataPart("prescription_pp_id[]", pp_array[j].toString());
                    builder.addFormDataPart("prescription_trade_name[]", tradeName_array[j].toString());
                    builder.addFormDataPart("prescription_generic_name[]", genericName_array[j].toString());
                    builder.addFormDataPart("prescription_dosage_name[]", dosage_array[j].toString());
                    builder.addFormDataPart("prescription_timings[]", timings_array[j].toString());
                    builder.addFormDataPart("prescription_duration[]", duration_array[j].toString());
//                    builder.addFormDataPart("prescription_route[]", route_array[j].toString());
//                    builder.addFormDataPart("prescription_frequency[]", frequency_array[j].toString());
//                    builder.addFormDataPart("prescription_instruction[]", instruction_array[j].toString());
                }
            }

            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("userid", String.valueOf(USER_ID));
            builder.addFormDataPart("login_type", USER_LOGIN_TYPE);
            builder.addFormDataPart(APIClass.KEY_MY_PATIENT_TEMPLATE_INAME, template_name);

            RequestBody requestBody = builder.build();


            Request request = new Request.Builder()
                    .url(APIClass.DRS_PRACTICE_CREATE_MYPATIENT_TEMPLATE)
                    .post(requestBody)
                    .build();

            //   OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " create: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }


        return null;
    }


    public static JSONObject ophthalAddVisit(int patient_id, String patient_name,
                                             List<ChiefMedicalComplaint> patient_chief_medcomplaint_array,
                                             final String distanceVision_RE, final String distanceVision_LE, final String nearVision_RE, final String nearVision_LE,
                                             List<Lids> patient_lids_array, List<OphthalConjuctiva> patient_conjuctiva_array,
                                             List<OphthalSclera> patient_sclera_array, List<OphthalCornearAnteriorSurface> patient_cornea_anterior_array,
                                             List<OphthalCornearPosteriorSurface> patient_cornea_posterior_array,
                                             List<OphthalAnteriorChamber> patient_anterior_chamber_array,
                                             List<OphthalIris> patient_iris_array, List<OphthalPupil> patient_pupil_array,
                                             List<OphthalAngleAnteriorChamber> patient_angle_array,
                                             List<OphthalLens> patient_lens_array, List<OphthalViterous> patient_viterous_array,
                                             List<OphthalFundus> patient_fundus_array, String refraction_RE_top, String refraction_RE_bottom,
                                             String refraction_LE_top, String refraction_LE_bottom,
                                             final List<Investigations> patient_investigation_array, final List<Diagnosis> patient_daignosis_array,
                                             final List<Treatments> patient_treatment_array, final List<FrequentPrescription> patient_prescription_array,
                                             final String conultationFees, final String followupdate, final String diagnosis_details, final String treatment_details,
                                             final String prescription_note, final String visit_entry_date, final String get_dv_sphere_right, final String get_dv_cyl_right,
                                             final String get_dv_axis_right, final String get_nv_sphere_right, final String get_nv_cyl_right, final String get_nv_axis_right,
                                             final String get_ipd_right, final String get_dv_sphere_left, final String get_dv_cyl_left, final String get_dv_axis_left,
                                             final String get_nv_sphere_left, final String get_nv_cyl_left, final String get_nv_axis_left, final String get_ipd_left,
                                             int user_id, String user_login_type, int HOSPITAL_ID, String visit_chiefMedComplaint_sufferings,
                                             int investigation_template_save, String investigation_template_name, int patient_education,
                                             String distanceVision_RE_Unaided, String distanceVision_LE_Unaided, String nearVision_RE_Unaided,String nearVision_LE_Unaided,
                                             String iop_RE, String iop_LE) {

        Log.d(Utils.TAG, " *************** ophthalAddVisit *************************** ");
        Log.d(Utils.TAG, " patient_id: "+patient_id);
        Log.d(Utils.TAG, " patient_name: "+patient_name);
        Log.d(Utils.TAG, " userid: "+user_id);
        Log.d(Utils.TAG, " login_type: "+user_login_type);
        Log.d(Utils.TAG, " followupdate: "+followupdate);
        Log.d(Utils.TAG, " diagnosis_details: "+diagnosis_details);
        Log.d(Utils.TAG, " treatment_details: "+treatment_details);
        Log.d(Utils.TAG, " conultationFees: "+conultationFees);
        Log.d(Utils.TAG, " prescription_note: "+prescription_note);
        Log.d(Utils.TAG, " visit_entry_date: "+visit_entry_date);

        Log.d(Utils.TAG, " patient_chief_complaint_array: "+patient_chief_medcomplaint_array.size());
        Log.d(Utils.TAG, " patient_lids_array: "+patient_lids_array.size());
        Log.d(Utils.TAG, " patient_conjuctiva_array: "+patient_conjuctiva_array.size());
        Log.d(Utils.TAG, " patient_sclera_array: "+patient_sclera_array.size());
        Log.d(Utils.TAG, " patient_cornea_anterior_array: "+patient_cornea_anterior_array.size());
        Log.d(Utils.TAG, " patient_cornea_posterior_array: "+patient_cornea_posterior_array.size());
        Log.d(Utils.TAG, " patient_anterior_chamber_array: "+patient_anterior_chamber_array.size());
        Log.d(Utils.TAG, " patient_iris_array: "+patient_iris_array.size());
        Log.d(Utils.TAG, " patient_pupil_array: "+patient_pupil_array.size());
        Log.d(Utils.TAG, " patient_angle_array: "+patient_angle_array.size());
        Log.d(Utils.TAG, " patient_lens_array: "+patient_lens_array.size());
        Log.d(Utils.TAG, " patient_viterous_array: "+patient_viterous_array.size());
        Log.d(Utils.TAG, " patient_fundus_array: "+patient_fundus_array.size());
        Log.d(Utils.TAG, " patient_investigation_array: "+patient_investigation_array.size());
        Log.d(Utils.TAG, " patient_daignosis_array: "+patient_daignosis_array.size());
        Log.d(Utils.TAG, " patient_treatment_array: "+patient_treatment_array.size());
        Log.d(Utils.TAG, " patient_prescription_array: "+patient_prescription_array.size());

        try {

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("patient_id", String.valueOf(patient_id));
            builder.addFormDataPart("patient_name", patient_name);
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", user_login_type);
            builder.addFormDataPart("hosp_id", String.valueOf(HOSPITAL_ID));

            if(patient_chief_medcomplaint_array.size() > 0) {

                ArrayList<String> compIDArrayList = new ArrayList<String>();
                ArrayList<String> compNameArrayList = new ArrayList<String>();
                ArrayList<String> compDocIDArrayList = new ArrayList<String>();
                ArrayList<String> compDocTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_chief_medcomplaint_array.size(); i++) {
                    compIDArrayList.add(String.valueOf(patient_chief_medcomplaint_array.get(i).getComplaintId()));
                    compNameArrayList.add(patient_chief_medcomplaint_array.get(i).getSymptomsName().trim());
                    compDocIDArrayList.add(String.valueOf(patient_chief_medcomplaint_array.get(i).getDocId()));
                    compDocTypeArrayList.add(String.valueOf(patient_chief_medcomplaint_array.get(i).getDocType()));
                }

                String[] compID_array = new String[compIDArrayList.size()];
                String[] compName_array = new String[compNameArrayList.size()];
                String[] compDocID_array = new String[compDocIDArrayList.size()];
                String[] compDocName_array = new String[compDocTypeArrayList.size()];

                if(compIDArrayList.size() > 0) {
                    for (int j = 0; j < compIDArrayList.size(); j++) {
                        compID_array[j] = compIDArrayList.get(j);
                        compName_array[j] = compNameArrayList.get(j);
                        compDocID_array[j]  = compDocIDArrayList.get(j);
                        compDocName_array[j]  = compDocTypeArrayList.get(j);
                        // Log.d(Utils.TAG, " compID: "+ compID_array[j].toString());
                        builder.addFormDataPart("chiefcomplaint_id[]", compID_array[j].toString());
                        builder.addFormDataPart("chiefcomplaint_name[]", compName_array[j].toString());
                        builder.addFormDataPart("chiefcomplaint_docid[]", compDocID_array[j].toString());
                        builder.addFormDataPart("chiefcomplaint_doctype[]", compDocName_array[j].toString());
                    }
                }

            }

            if(patient_lids_array.size() > 0) {
                ArrayList<String> lidsIDArrayList = new ArrayList<String>();
                ArrayList<String> lidsNameArrayList = new ArrayList<String>();
                ArrayList<String> lidsDocIDArrayList = new ArrayList<String>();
                ArrayList<String> lidsDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> lidsLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> lidsRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> lidsUserIDArrayList = new ArrayList<String>();
                ArrayList<String> lidsLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_lids_array.size(); i++) {
                    lidsIDArrayList.add(String.valueOf(patient_lids_array.get(i).getLidsId()));
                    lidsNameArrayList.add(patient_lids_array.get(i).getLidsName().trim());
                    lidsDocIDArrayList.add(String.valueOf(patient_lids_array.get(i).getDocId()));
                    lidsDocTypeArrayList.add(String.valueOf(patient_lids_array.get(i).getDocType()));
                    lidsLeftEyeArrayList.add(patient_lids_array.get(i).getLeftEye());
                    lidsRightEyeArrayList.add(String.valueOf(patient_lids_array.get(i).getRightEye()));
                    lidsUserIDArrayList.add(String.valueOf(patient_lids_array.get(i).getUserId()));
                    lidsLoginTypeArrayList.add(String.valueOf(patient_lids_array.get(i).getLoginType()));
                }

                String[] lidsID_array = new String[lidsIDArrayList.size()];
                String[] lidsName_array = new String[lidsNameArrayList.size()];
                String[] lidsDocID_array = new String[lidsDocIDArrayList.size()];
                String[] lidsDocType_array = new String[lidsDocTypeArrayList.size()];
                String[] lidsLeftEye_array = new String[lidsLeftEyeArrayList.size()];
                String[] lidsRightEye_array = new String[lidsRightEyeArrayList.size()];
                String[] lidsUserID_array = new String[lidsUserIDArrayList.size()];
                String[] lidsLoginType_array = new String[lidsLoginTypeArrayList.size()];

                if(lidsIDArrayList.size() > 0) {
                    for (int j = 0; j < lidsIDArrayList.size(); j++) {
                        lidsID_array[j] = lidsIDArrayList.get(j);
                        lidsName_array[j] = lidsNameArrayList.get(j);
                        lidsDocID_array[j]  = lidsDocIDArrayList.get(j);
                        lidsDocType_array[j]  = lidsDocTypeArrayList.get(j);
                        lidsLeftEye_array[j] = lidsLeftEyeArrayList.get(j);
                        lidsRightEye_array[j]  = lidsRightEyeArrayList.get(j);
                        lidsUserID_array[j]  = lidsUserIDArrayList.get(j);
                        lidsLoginType_array[j]  = lidsLoginTypeArrayList.get(j);
                        builder.addFormDataPart("lids_id[]", lidsID_array[j].toString());
                        builder.addFormDataPart("lids_name[]", lidsName_array[j].toString());
                        builder.addFormDataPart("lids_docid[]", lidsDocID_array[j].toString());
                        builder.addFormDataPart("lids_doctype[]", lidsDocType_array[j].toString());
                        builder.addFormDataPart("lids_leftEye[]", lidsLeftEye_array[j].toString());
                        builder.addFormDataPart("lids_rightEye[]", lidsRightEye_array[j].toString());
                        builder.addFormDataPart("lids_userid[]", lidsUserID_array[j].toString());
                        builder.addFormDataPart("lids_loginType[]", lidsLoginType_array[j].toString());
                    }
                }
            }

            if(patient_conjuctiva_array.size() > 0) {
                ArrayList<String> conjuctivaIDArrayList = new ArrayList<String>();
                ArrayList<String> conjuctivaNameArrayList = new ArrayList<String>();
                ArrayList<String> conjuctivaDocIDArrayList = new ArrayList<String>();
                ArrayList<String> conjuctivaDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> conjuctivaLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> conjuctivaRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> conjuctivaUserIDArrayList = new ArrayList<String>();
                ArrayList<String> conjuctivasLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_conjuctiva_array.size(); i++) {
                    conjuctivaIDArrayList.add(String.valueOf(patient_conjuctiva_array.get(i).getConjuctivaId()));
                    conjuctivaNameArrayList.add(patient_conjuctiva_array.get(i).getConjuctivaName().trim());
                    conjuctivaDocIDArrayList.add(String.valueOf(patient_conjuctiva_array.get(i).getDocId()));
                    conjuctivaDocTypeArrayList.add(String.valueOf(patient_conjuctiva_array.get(i).getDocType()));
                    conjuctivaLeftEyeArrayList.add(patient_conjuctiva_array.get(i).getLeftEye());
                    conjuctivaRightEyeArrayList.add(String.valueOf(patient_conjuctiva_array.get(i).getRightEye()));
                    conjuctivaUserIDArrayList.add(String.valueOf(patient_conjuctiva_array.get(i).getUserId()));
                    conjuctivasLoginTypeArrayList.add(String.valueOf(patient_conjuctiva_array.get(i).getLoginType()));
                }

                String[] conjuctivaID_array = new String[conjuctivaIDArrayList.size()];
                String[] conjuctivaName_array = new String[conjuctivaNameArrayList.size()];
                String[] conjuctivaDocID_array = new String[conjuctivaDocIDArrayList.size()];
                String[] conjuctivaDocType_array = new String[conjuctivaDocTypeArrayList.size()];
                String[] conjuctivaLeftEye_array = new String[conjuctivaLeftEyeArrayList.size()];
                String[] conjuctivaRightEye_array = new String[conjuctivaRightEyeArrayList.size()];
                String[] conjuctivaUserID_array = new String[conjuctivaUserIDArrayList.size()];
                String[] conjuctivaLoginType_array = new String[conjuctivasLoginTypeArrayList.size()];

                if(conjuctivaIDArrayList.size() > 0) {
                    for (int j = 0; j < conjuctivaIDArrayList.size(); j++) {
                        conjuctivaID_array[j] = conjuctivaIDArrayList.get(j);
                        conjuctivaName_array[j] = conjuctivaNameArrayList.get(j);
                        conjuctivaDocID_array[j]  = conjuctivaDocIDArrayList.get(j);
                        conjuctivaDocType_array[j]  = conjuctivaDocTypeArrayList.get(j);
                        conjuctivaLeftEye_array[j] = conjuctivaLeftEyeArrayList.get(j);
                        conjuctivaRightEye_array[j]  = conjuctivaRightEyeArrayList.get(j);
                        conjuctivaUserID_array[j]  = conjuctivaUserIDArrayList.get(j);
                        conjuctivaLoginType_array[j]  = conjuctivasLoginTypeArrayList.get(j);
                        builder.addFormDataPart("conjuctiva_id[]", conjuctivaID_array[j].toString());
                        builder.addFormDataPart("conjuctiva_name[]", conjuctivaName_array[j].toString());
                        builder.addFormDataPart("conjuctiva_docid[]", conjuctivaDocID_array[j].toString());
                        builder.addFormDataPart("conjuctiva_doctype[]", conjuctivaDocType_array[j].toString());
                        builder.addFormDataPart("conjuctiva_leftEye[]", conjuctivaLeftEye_array[j].toString());
                        builder.addFormDataPart("conjuctiva_rightEye[]", conjuctivaRightEye_array[j].toString());
                        builder.addFormDataPart("conjuctiva_userid[]", conjuctivaUserID_array[j].toString());
                        builder.addFormDataPart("conjuctiva_loginType[]", conjuctivaLoginType_array[j].toString());
                    }
                }
            }

            if(patient_sclera_array.size() > 0) {
                ArrayList<String> scleraIDArrayList = new ArrayList<String>();
                ArrayList<String> scleraNameArrayList = new ArrayList<String>();
                ArrayList<String> scleraDocIDArrayList = new ArrayList<String>();
                ArrayList<String> scleraDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> scleraLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> scleraRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> scleraUserIDArrayList = new ArrayList<String>();
                ArrayList<String> scleraLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_sclera_array.size(); i++) {
                    scleraIDArrayList.add(String.valueOf(patient_sclera_array.get(i).getScleraId()));
                    scleraNameArrayList.add(patient_sclera_array.get(i).getScleraName().trim());
                    scleraDocIDArrayList.add(String.valueOf(patient_sclera_array.get(i).getDocId()));
                    scleraDocTypeArrayList.add(String.valueOf(patient_sclera_array.get(i).getDocType()));
                    scleraLeftEyeArrayList.add(patient_sclera_array.get(i).getLeftEye());
                    scleraRightEyeArrayList.add(String.valueOf(patient_sclera_array.get(i).getRightEye()));
                    scleraUserIDArrayList.add(String.valueOf(patient_sclera_array.get(i).getUserId()));
                    scleraLoginTypeArrayList.add(String.valueOf(patient_sclera_array.get(i).getLoginType()));
                }

                String[] scleraID_array = new String[scleraIDArrayList.size()];
                String[] scleraName_array = new String[scleraNameArrayList.size()];
                String[] scleraDocID_array = new String[scleraDocIDArrayList.size()];
                String[] scleraDocType_array = new String[scleraDocTypeArrayList.size()];
                String[] scleraLeftEye_array = new String[scleraLeftEyeArrayList.size()];
                String[] scleraRightEye_array = new String[scleraRightEyeArrayList.size()];
                String[] scleraUserID_array = new String[scleraUserIDArrayList.size()];
                String[] scleraLoginType_array = new String[scleraLoginTypeArrayList.size()];

                if(scleraIDArrayList.size() > 0) {
                    for (int j = 0; j < scleraIDArrayList.size(); j++) {
                        scleraID_array[j] = scleraIDArrayList.get(j);
                        scleraName_array[j] = scleraNameArrayList.get(j);
                        scleraDocID_array[j]  = scleraDocIDArrayList.get(j);
                        scleraDocType_array[j]  = scleraDocTypeArrayList.get(j);
                        scleraLeftEye_array[j] = scleraLeftEyeArrayList.get(j);
                        scleraRightEye_array[j]  = scleraRightEyeArrayList.get(j);
                        scleraUserID_array[j]  = scleraUserIDArrayList.get(j);
                        scleraLoginType_array[j]  = scleraLoginTypeArrayList.get(j);
                        builder.addFormDataPart("sclera_id[]", scleraID_array[j].toString());
                        builder.addFormDataPart("sclera_name[]", scleraName_array[j].toString());
                        builder.addFormDataPart("sclera_docid[]", scleraDocID_array[j].toString());
                        builder.addFormDataPart("sclera_doctype[]", scleraDocType_array[j].toString());
                        builder.addFormDataPart("sclera_leftEye[]", scleraLeftEye_array[j].toString());
                        builder.addFormDataPart("sclera_rightEye[]", scleraRightEye_array[j].toString());
                        builder.addFormDataPart("sclera_userid[]", scleraUserID_array[j].toString());
                        builder.addFormDataPart("sclera_loginType[]", scleraLoginType_array[j].toString());
                    }
                }
            }

            if(patient_cornea_anterior_array.size() > 0) {
                ArrayList<String> anteriorIDArrayList = new ArrayList<String>();
                ArrayList<String> anteriorNameArrayList = new ArrayList<String>();
                ArrayList<String> anteriorDocIDArrayList = new ArrayList<String>();
                ArrayList<String> anteriorDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> anteriorLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> anteriorRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> anteriorUserIDArrayList = new ArrayList<String>();
                ArrayList<String> anteriorLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_cornea_anterior_array.size(); i++) {
                    anteriorIDArrayList.add(String.valueOf(patient_cornea_anterior_array.get(i).getCorneaAnteriorId()));
                    anteriorNameArrayList.add(patient_cornea_anterior_array.get(i).getCorneaAnteriorName().trim());
                    anteriorDocIDArrayList.add(String.valueOf(patient_cornea_anterior_array.get(i).getDocId()));
                    anteriorDocTypeArrayList.add(String.valueOf(patient_cornea_anterior_array.get(i).getDocType()));
                    anteriorLeftEyeArrayList.add(patient_cornea_anterior_array.get(i).getLeftEye());
                    anteriorRightEyeArrayList.add(String.valueOf(patient_cornea_anterior_array.get(i).getRightEye()));
                    anteriorUserIDArrayList.add(String.valueOf(patient_cornea_anterior_array.get(i).getUserId()));
                    anteriorLoginTypeArrayList.add(String.valueOf(patient_cornea_anterior_array.get(i).getLoginType()));
                }

                String[] anteriorID_array = new String[anteriorIDArrayList.size()];
                String[] anteriorName_array = new String[anteriorNameArrayList.size()];
                String[] anteriorDocID_array = new String[anteriorDocIDArrayList.size()];
                String[] anteriorDocType_array = new String[anteriorDocTypeArrayList.size()];
                String[] anteriorLeftEye_array = new String[anteriorLeftEyeArrayList.size()];
                String[] anteriorRightEye_array = new String[anteriorRightEyeArrayList.size()];
                String[] anteriorUserID_array = new String[anteriorUserIDArrayList.size()];
                String[] anteriorLoginType_array = new String[anteriorLoginTypeArrayList.size()];

                if(anteriorIDArrayList.size() > 0) {
                    for (int j = 0; j < anteriorIDArrayList.size(); j++) {
                        anteriorID_array[j] = anteriorIDArrayList.get(j);
                        anteriorName_array[j] = anteriorNameArrayList.get(j);
                        anteriorDocID_array[j]  = anteriorDocIDArrayList.get(j);
                        anteriorDocType_array[j]  = anteriorDocTypeArrayList.get(j);
                        anteriorLeftEye_array[j] = anteriorLeftEyeArrayList.get(j);
                        anteriorRightEye_array[j]  = anteriorRightEyeArrayList.get(j);
                        anteriorUserID_array[j]  = anteriorUserIDArrayList.get(j);
                        anteriorLoginType_array[j]  = anteriorLoginTypeArrayList.get(j);
                        builder.addFormDataPart("cornea_anterior_id[]", anteriorID_array[j].toString());
                        builder.addFormDataPart("cornea_anterior_name[]", anteriorName_array[j].toString());
                        builder.addFormDataPart("cornea_anterior_docid[]", anteriorDocID_array[j].toString());
                        builder.addFormDataPart("cornea_anterior_doctype[]", anteriorDocType_array[j].toString());
                        builder.addFormDataPart("cornea_anterior_leftEye[]", anteriorLeftEye_array[j].toString());
                        builder.addFormDataPart("cornea_anterior_rightEye[]", anteriorRightEye_array[j].toString());
                        builder.addFormDataPart("cornea_anterior_userid[]", anteriorUserID_array[j].toString());
                        builder.addFormDataPart("cornea_anterior_loginType[]", anteriorLoginType_array[j].toString());
                    }
                }
            }

            if(patient_cornea_posterior_array.size() > 0) {
                ArrayList<String> posteriorIDArrayList = new ArrayList<String>();
                ArrayList<String> posteriorNameArrayList = new ArrayList<String>();
                ArrayList<String> posteriorDocIDArrayList = new ArrayList<String>();
                ArrayList<String> posteriorDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> posteriorLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> posteriorRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> posteriorUserIDArrayList = new ArrayList<String>();
                ArrayList<String> posteriorLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_cornea_posterior_array.size(); i++) {
                    posteriorIDArrayList.add(String.valueOf(patient_cornea_posterior_array.get(i).getCorneaPosteriorId()));
                    posteriorNameArrayList.add(patient_cornea_posterior_array.get(i).getCorneaPosteriorName().trim());
                    posteriorDocIDArrayList.add(String.valueOf(patient_cornea_posterior_array.get(i).getDocId()));
                    posteriorDocTypeArrayList.add(String.valueOf(patient_cornea_posterior_array.get(i).getDocType()));
                    posteriorLeftEyeArrayList.add(patient_cornea_posterior_array.get(i).getLeftEye());
                    posteriorRightEyeArrayList.add(String.valueOf(patient_cornea_posterior_array.get(i).getRightEye()));
                    posteriorUserIDArrayList.add(String.valueOf(patient_cornea_posterior_array.get(i).getUserId()));
                    posteriorLoginTypeArrayList.add(String.valueOf(patient_cornea_posterior_array.get(i).getLoginType()));
                }

                String[] posteriorID_array = new String[posteriorIDArrayList.size()];
                String[] posteriorName_array = new String[posteriorNameArrayList.size()];
                String[] posteriorDocID_array = new String[posteriorDocIDArrayList.size()];
                String[] posteriorDocType_array = new String[posteriorDocTypeArrayList.size()];
                String[] posteriorLeftEye_array = new String[posteriorLeftEyeArrayList.size()];
                String[] posteriorRightEye_array = new String[posteriorRightEyeArrayList.size()];
                String[] posteriorUserID_array = new String[posteriorUserIDArrayList.size()];
                String[] posteriorLoginType_array = new String[posteriorLoginTypeArrayList.size()];

                if(posteriorIDArrayList.size() > 0) {
                    for (int j = 0; j < posteriorIDArrayList.size(); j++) {
                        posteriorID_array[j] = posteriorIDArrayList.get(j);
                        posteriorName_array[j] = posteriorNameArrayList.get(j);
                        posteriorDocID_array[j]  = posteriorDocIDArrayList.get(j);
                        posteriorDocType_array[j]  = posteriorDocTypeArrayList.get(j);
                        posteriorLeftEye_array[j] = posteriorLeftEyeArrayList.get(j);
                        posteriorRightEye_array[j]  = posteriorRightEyeArrayList.get(j);
                        posteriorUserID_array[j]  = posteriorUserIDArrayList.get(j);
                        posteriorLoginType_array[j]  = posteriorLoginTypeArrayList.get(j);
                        builder.addFormDataPart("cornea_posterior_id[]", posteriorID_array[j].toString());
                        builder.addFormDataPart("cornea_posterior_name[]", posteriorName_array[j].toString());
                        builder.addFormDataPart("cornea_posterior_docid[]", posteriorDocID_array[j].toString());
                        builder.addFormDataPart("cornea_posterior_doctype[]", posteriorDocType_array[j].toString());
                        builder.addFormDataPart("cornea_posterior_leftEye[]", posteriorLeftEye_array[j].toString());
                        builder.addFormDataPart("cornea_posterior_rightEye[]", posteriorRightEye_array[j].toString());
                        builder.addFormDataPart("cornea_posterior_userid[]", posteriorUserID_array[j].toString());
                        builder.addFormDataPart("cornea_posterior_loginType[]", posteriorLoginType_array[j].toString());
                    }
                }
            }

            if(patient_anterior_chamber_array.size() > 0) {
                ArrayList<String> chamberIDArrayList = new ArrayList<String>();
                ArrayList<String> chamberNameArrayList = new ArrayList<String>();
                ArrayList<String> chamberDocIDArrayList = new ArrayList<String>();
                ArrayList<String> chamberDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> chamberLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> chamberRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> chamberUserIDArrayList = new ArrayList<String>();
                ArrayList<String> chamberLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_anterior_chamber_array.size(); i++) {
                    chamberIDArrayList.add(String.valueOf(patient_anterior_chamber_array.get(i).getAnteriorChamberId()));
                    chamberNameArrayList.add(patient_anterior_chamber_array.get(i).getAnteriorChamberName().trim());
                    chamberDocIDArrayList.add(String.valueOf(patient_anterior_chamber_array.get(i).getDocId()));
                    chamberDocTypeArrayList.add(String.valueOf(patient_anterior_chamber_array.get(i).getDocType()));
                    chamberLeftEyeArrayList.add(patient_anterior_chamber_array.get(i).getLeftEye());
                    chamberRightEyeArrayList.add(String.valueOf(patient_anterior_chamber_array.get(i).getRightEye()));
                    chamberUserIDArrayList.add(String.valueOf(patient_anterior_chamber_array.get(i).getUserId()));
                    chamberLoginTypeArrayList.add(String.valueOf(patient_anterior_chamber_array.get(i).getLoginType()));
                }

                String[] chamberID_array = new String[chamberIDArrayList.size()];
                String[] chamberName_array = new String[chamberNameArrayList.size()];
                String[] chamberDocID_array = new String[chamberDocIDArrayList.size()];
                String[] chamberDocType_array = new String[chamberDocTypeArrayList.size()];
                String[] chamberLeftEye_array = new String[chamberLeftEyeArrayList.size()];
                String[] chamberRightEye_array = new String[chamberRightEyeArrayList.size()];
                String[] chamberUserID_array = new String[chamberUserIDArrayList.size()];
                String[] chamberLoginType_array = new String[chamberLoginTypeArrayList.size()];

                if(chamberIDArrayList.size() > 0) {
                    for (int j = 0; j < chamberIDArrayList.size(); j++) {
                        chamberID_array[j] = chamberIDArrayList.get(j);
                        chamberName_array[j] = chamberNameArrayList.get(j);
                        chamberDocID_array[j]  = chamberDocIDArrayList.get(j);
                        chamberDocType_array[j]  = chamberDocTypeArrayList.get(j);
                        chamberLeftEye_array[j] = chamberLeftEyeArrayList.get(j);
                        chamberRightEye_array[j]  = chamberRightEyeArrayList.get(j);
                        chamberUserID_array[j]  = chamberUserIDArrayList.get(j);
                        chamberLoginType_array[j]  = chamberLoginTypeArrayList.get(j);
                        builder.addFormDataPart("anterior_chamber_id[]", chamberID_array[j].toString());
                        builder.addFormDataPart("anterior_chamber_name[]", chamberName_array[j].toString());
                        builder.addFormDataPart("anterior_chamber_docid[]", chamberDocID_array[j].toString());
                        builder.addFormDataPart("anterior_chamber_doctype[]", chamberDocType_array[j].toString());
                        builder.addFormDataPart("anterior_chamber_leftEye[]", chamberLeftEye_array[j].toString());
                        builder.addFormDataPart("anterior_chamber_rightEye[]", chamberRightEye_array[j].toString());
                        builder.addFormDataPart("anterior_chamber_userid[]", chamberUserID_array[j].toString());
                        builder.addFormDataPart("anterior_chamber_loginType[]", chamberLoginType_array[j].toString());
                    }
                }
            }

            if(patient_iris_array.size() > 0) {
                ArrayList<String> irisIDArrayList = new ArrayList<String>();
                ArrayList<String> irisNameArrayList = new ArrayList<String>();
                ArrayList<String> irisDocIDArrayList = new ArrayList<String>();
                ArrayList<String> irisDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> irisLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> irisRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> irisUserIDArrayList = new ArrayList<String>();
                ArrayList<String> irisLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_iris_array.size(); i++) {
                    irisIDArrayList.add(String.valueOf(patient_iris_array.get(i).getIrisId()));
                    irisNameArrayList.add(patient_iris_array.get(i).getIrisName().trim());
                    irisDocIDArrayList.add(String.valueOf(patient_iris_array.get(i).getDocId()));
                    irisDocTypeArrayList.add(String.valueOf(patient_iris_array.get(i).getDocType()));
                    irisLeftEyeArrayList.add(patient_iris_array.get(i).getLeftEye());
                    irisRightEyeArrayList.add(String.valueOf(patient_iris_array.get(i).getRightEye()));
                    irisUserIDArrayList.add(String.valueOf(patient_iris_array.get(i).getUserId()));
                    irisLoginTypeArrayList.add(String.valueOf(patient_iris_array.get(i).getLoginType()));
                }

                String[] irisID_array = new String[irisIDArrayList.size()];
                String[] irisName_array = new String[irisNameArrayList.size()];
                String[] irisDocID_array = new String[irisDocIDArrayList.size()];
                String[] irisDocType_array = new String[irisDocTypeArrayList.size()];
                String[] irisLeftEye_array = new String[irisLeftEyeArrayList.size()];
                String[] irisRightEye_array = new String[irisRightEyeArrayList.size()];
                String[] irisUserID_array = new String[irisUserIDArrayList.size()];
                String[] irisLoginType_array = new String[irisLoginTypeArrayList.size()];

                if(irisIDArrayList.size() > 0) {
                    for (int j = 0; j < irisIDArrayList.size(); j++) {
                        irisID_array[j] = irisIDArrayList.get(j);
                        irisName_array[j] = irisNameArrayList.get(j);
                        irisDocID_array[j]  = irisDocIDArrayList.get(j);
                        irisDocType_array[j]  = irisDocTypeArrayList.get(j);
                        irisLeftEye_array[j] = irisLeftEyeArrayList.get(j);
                        irisRightEye_array[j]  = irisRightEyeArrayList.get(j);
                        irisUserID_array[j]  = irisUserIDArrayList.get(j);
                        irisLoginType_array[j]  = irisLoginTypeArrayList.get(j);
                        builder.addFormDataPart("iris_id[]", irisID_array[j].toString());
                        builder.addFormDataPart("iris_name[]", irisName_array[j].toString());
                        builder.addFormDataPart("iris_docid[]", irisDocID_array[j].toString());
                        builder.addFormDataPart("iris_doctype[]", irisDocType_array[j].toString());
                        builder.addFormDataPart("iris_leftEye[]", irisLeftEye_array[j].toString());
                        builder.addFormDataPart("iris_rightEye[]", irisRightEye_array[j].toString());
                        builder.addFormDataPart("iris_userid[]", irisUserID_array[j].toString());
                        builder.addFormDataPart("iris_loginType[]", irisLoginType_array[j].toString());
                    }
                }
            }

            if(patient_pupil_array.size() > 0) {
                ArrayList<String> pupilIDArrayList = new ArrayList<String>();
                ArrayList<String> pupilNameArrayList = new ArrayList<String>();
                ArrayList<String> pupilDocIDArrayList = new ArrayList<String>();
                ArrayList<String> pupilDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> pupilLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> pupilRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> pupilUserIDArrayList = new ArrayList<String>();
                ArrayList<String> pupilLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_pupil_array.size(); i++) {
                    pupilIDArrayList.add(String.valueOf(patient_pupil_array.get(i).getPupilId()));
                    pupilNameArrayList.add(patient_pupil_array.get(i).getPupilName().trim());
                    pupilDocIDArrayList.add(String.valueOf(patient_pupil_array.get(i).getDocId()));
                    pupilDocTypeArrayList.add(String.valueOf(patient_pupil_array.get(i).getDocType()));
                    pupilLeftEyeArrayList.add(patient_pupil_array.get(i).getLeftEye());
                    pupilRightEyeArrayList.add(String.valueOf(patient_pupil_array.get(i).getRightEye()));
                    pupilUserIDArrayList.add(String.valueOf(patient_pupil_array.get(i).getUserId()));
                    pupilLoginTypeArrayList.add(String.valueOf(patient_pupil_array.get(i).getLoginType()));
                }

                String[] pupilID_array = new String[pupilIDArrayList.size()];
                String[] pupilName_array = new String[pupilNameArrayList.size()];
                String[] pupilDocID_array = new String[pupilDocIDArrayList.size()];
                String[] pupilDocType_array = new String[pupilDocTypeArrayList.size()];
                String[] pupilLeftEye_array = new String[pupilLeftEyeArrayList.size()];
                String[] pupilRightEye_array = new String[pupilRightEyeArrayList.size()];
                String[] pupilUserID_array = new String[pupilUserIDArrayList.size()];
                String[] pupilLoginType_array = new String[pupilLoginTypeArrayList.size()];

                if(pupilIDArrayList.size() > 0) {
                    for (int j = 0; j < pupilIDArrayList.size(); j++) {
                        pupilID_array[j] = pupilIDArrayList.get(j);
                        pupilName_array[j] = pupilNameArrayList.get(j);
                        pupilDocID_array[j]  = pupilDocIDArrayList.get(j);
                        pupilDocType_array[j]  = pupilDocTypeArrayList.get(j);
                        pupilLeftEye_array[j] = pupilLeftEyeArrayList.get(j);
                        pupilRightEye_array[j]  = pupilRightEyeArrayList.get(j);
                        pupilUserID_array[j]  = pupilUserIDArrayList.get(j);
                        pupilLoginType_array[j]  = pupilLoginTypeArrayList.get(j);
                        builder.addFormDataPart("pupil_id[]", pupilID_array[j].toString());
                        builder.addFormDataPart("pupil_name[]", pupilName_array[j].toString());
                        builder.addFormDataPart("pupil_docid[]", pupilDocID_array[j].toString());
                        builder.addFormDataPart("pupil_doctype[]", pupilDocType_array[j].toString());
                        builder.addFormDataPart("pupil_leftEye[]", pupilLeftEye_array[j].toString());
                        builder.addFormDataPart("pupil_rightEye[]", pupilRightEye_array[j].toString());
                        builder.addFormDataPart("pupil_userid[]", pupilUserID_array[j].toString());
                        builder.addFormDataPart("pupil_loginType[]", pupilLoginType_array[j].toString());
                    }
                }
            }

            if(patient_angle_array.size() > 0) {
                ArrayList<String> angleIDArrayList = new ArrayList<String>();
                ArrayList<String> angleNameArrayList = new ArrayList<String>();
                ArrayList<String> angleDocIDArrayList = new ArrayList<String>();
                ArrayList<String> angleDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> angleLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> angleRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> angleUserIDArrayList = new ArrayList<String>();
                ArrayList<String> angleLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_angle_array.size(); i++) {
                    angleIDArrayList.add(String.valueOf(patient_angle_array.get(i).getAngleId()));
                    angleNameArrayList.add(patient_angle_array.get(i).getAngleName().trim());
                    angleDocIDArrayList.add(String.valueOf(patient_angle_array.get(i).getDocId()));
                    angleDocTypeArrayList.add(String.valueOf(patient_angle_array.get(i).getDocType()));
                    angleLeftEyeArrayList.add(patient_angle_array.get(i).getLeftEye());
                    angleRightEyeArrayList.add(String.valueOf(patient_angle_array.get(i).getRightEye()));
                    angleUserIDArrayList.add(String.valueOf(patient_angle_array.get(i).getUserId()));
                    angleLoginTypeArrayList.add(String.valueOf(patient_angle_array.get(i).getLoginType()));
                }

                String[] angleID_array = new String[angleIDArrayList.size()];
                String[] angleName_array = new String[angleNameArrayList.size()];
                String[] angleDocID_array = new String[angleDocIDArrayList.size()];
                String[] angleDocType_array = new String[angleDocTypeArrayList.size()];
                String[] angleLeftEye_array = new String[angleLeftEyeArrayList.size()];
                String[] angleRightEye_array = new String[angleRightEyeArrayList.size()];
                String[] angleUserID_array = new String[angleUserIDArrayList.size()];
                String[] angleLoginType_array = new String[angleLoginTypeArrayList.size()];

                if(angleIDArrayList.size() > 0) {
                    for (int j = 0; j < angleIDArrayList.size(); j++) {
                        angleID_array[j] = angleIDArrayList.get(j);
                        angleName_array[j] = angleNameArrayList.get(j);
                        angleDocID_array[j]  = angleDocIDArrayList.get(j);
                        angleDocType_array[j]  = angleDocTypeArrayList.get(j);
                        angleLeftEye_array[j] = angleLeftEyeArrayList.get(j);
                        angleRightEye_array[j]  = angleRightEyeArrayList.get(j);
                        angleUserID_array[j]  = angleUserIDArrayList.get(j);
                        angleLoginType_array[j]  = angleLoginTypeArrayList.get(j);
                        builder.addFormDataPart("angle_id[]", angleID_array[j].toString());
                        builder.addFormDataPart("angle_name[]", angleName_array[j].toString());
                        builder.addFormDataPart("angle_docid[]", angleDocID_array[j].toString());
                        builder.addFormDataPart("angle_doctype[]", angleDocType_array[j].toString());
                        builder.addFormDataPart("angle_leftEye[]", angleLeftEye_array[j].toString());
                        builder.addFormDataPart("angle_rightEye[]", angleRightEye_array[j].toString());
                        builder.addFormDataPart("angle_userid[]", angleUserID_array[j].toString());
                        builder.addFormDataPart("angle_loginType[]", angleLoginType_array[j].toString());
                    }
                }
            }

            if(patient_lens_array.size() > 0) {
                ArrayList<String> lensIDArrayList = new ArrayList<String>();
                ArrayList<String> lensNameArrayList = new ArrayList<String>();
                ArrayList<String> lensDocIDArrayList = new ArrayList<String>();
                ArrayList<String> lensDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> lensLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> lensRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> lensUserIDArrayList = new ArrayList<String>();
                ArrayList<String> lensLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_lens_array.size(); i++) {
                    lensIDArrayList.add(String.valueOf(patient_lens_array.get(i).getLensId()));
                    lensNameArrayList.add(patient_lens_array.get(i).getLensName().trim());
                    lensDocIDArrayList.add(String.valueOf(patient_lens_array.get(i).getDocId()));
                    lensDocTypeArrayList.add(String.valueOf(patient_lens_array.get(i).getDocType()));
                    lensLeftEyeArrayList.add(patient_lens_array.get(i).getLeftEye());
                    lensRightEyeArrayList.add(String.valueOf(patient_lens_array.get(i).getRightEye()));
                    lensUserIDArrayList.add(String.valueOf(patient_lens_array.get(i).getUserId()));
                    lensLoginTypeArrayList.add(String.valueOf(patient_lens_array.get(i).getLoginType()));
                }

                String[] lensID_array = new String[lensIDArrayList.size()];
                String[] lensName_array = new String[lensNameArrayList.size()];
                String[] lensDocID_array = new String[lensDocIDArrayList.size()];
                String[] lensDocType_array = new String[lensDocTypeArrayList.size()];
                String[] lensLeftEye_array = new String[lensLeftEyeArrayList.size()];
                String[] lensRightEye_array = new String[lensRightEyeArrayList.size()];
                String[] lensUserID_array = new String[lensUserIDArrayList.size()];
                String[] lensLoginType_array = new String[lensLoginTypeArrayList.size()];

                if(lensIDArrayList.size() > 0) {
                    for (int j = 0; j < lensIDArrayList.size(); j++) {
                        lensID_array[j] = lensIDArrayList.get(j);
                        lensName_array[j] = lensNameArrayList.get(j);
                        lensDocID_array[j]  = lensDocIDArrayList.get(j);
                        lensDocType_array[j]  = lensDocTypeArrayList.get(j);
                        lensLeftEye_array[j] = lensLeftEyeArrayList.get(j);
                        lensRightEye_array[j]  = lensRightEyeArrayList.get(j);
                        lensUserID_array[j]  = lensUserIDArrayList.get(j);
                        lensLoginType_array[j]  = lensLoginTypeArrayList.get(j);
                        builder.addFormDataPart("lens_id[]", lensID_array[j].toString());
                        builder.addFormDataPart("lens_name[]", lensName_array[j].toString());
                        builder.addFormDataPart("lens_docid[]", lensDocID_array[j].toString());
                        builder.addFormDataPart("lens_doctype[]", lensDocType_array[j].toString());
                        builder.addFormDataPart("lens_leftEye[]", lensLeftEye_array[j].toString());
                        builder.addFormDataPart("lens_rightEye[]", lensRightEye_array[j].toString());
                        builder.addFormDataPart("lens_userid[]", lensUserID_array[j].toString());
                        builder.addFormDataPart("lens_loginType[]", lensLoginType_array[j].toString());
                    }
                }
            }

            if(patient_viterous_array.size() > 0) {
                ArrayList<String> viterousIDArrayList = new ArrayList<String>();
                ArrayList<String> viterousNameArrayList = new ArrayList<String>();
                ArrayList<String> viterousDocIDArrayList = new ArrayList<String>();
                ArrayList<String> viterousDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> viterousLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> viterousRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> viterousUserIDArrayList = new ArrayList<String>();
                ArrayList<String> viterousLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_viterous_array.size(); i++) {
                    viterousIDArrayList.add(String.valueOf(patient_viterous_array.get(i).getViterousId()));
                    viterousNameArrayList.add(patient_viterous_array.get(i).getViterousName().trim());
                    viterousDocIDArrayList.add(String.valueOf(patient_viterous_array.get(i).getDocId()));
                    viterousDocTypeArrayList.add(String.valueOf(patient_viterous_array.get(i).getDocType()));
                    viterousLeftEyeArrayList.add(patient_viterous_array.get(i).getLeftEye());
                    viterousRightEyeArrayList.add(String.valueOf(patient_viterous_array.get(i).getRightEye()));
                    viterousUserIDArrayList.add(String.valueOf(patient_viterous_array.get(i).getUserId()));
                    viterousLoginTypeArrayList.add(String.valueOf(patient_viterous_array.get(i).getLoginType()));
                }

                String[] viterousID_array = new String[viterousIDArrayList.size()];
                String[] viterousName_array = new String[viterousNameArrayList.size()];
                String[] viterousDocID_array = new String[viterousDocIDArrayList.size()];
                String[] viterousDocType_array = new String[viterousDocTypeArrayList.size()];
                String[] viterousLeftEye_array = new String[viterousLeftEyeArrayList.size()];
                String[] viterousRightEye_array = new String[viterousRightEyeArrayList.size()];
                String[] viterousUserID_array = new String[viterousUserIDArrayList.size()];
                String[] viterousLoginType_array = new String[viterousLoginTypeArrayList.size()];

                if(viterousIDArrayList.size() > 0) {
                    for (int j = 0; j < viterousIDArrayList.size(); j++) {
                        viterousID_array[j] = viterousIDArrayList.get(j);
                        viterousName_array[j] = viterousNameArrayList.get(j);
                        viterousDocID_array[j]  = viterousDocIDArrayList.get(j);
                        viterousDocType_array[j]  = viterousDocTypeArrayList.get(j);
                        viterousLeftEye_array[j] = viterousLeftEyeArrayList.get(j);
                        viterousRightEye_array[j]  = viterousRightEyeArrayList.get(j);
                        viterousUserID_array[j]  = viterousUserIDArrayList.get(j);
                        viterousLoginType_array[j]  = viterousLoginTypeArrayList.get(j);
                        builder.addFormDataPart("viterous_id[]", viterousID_array[j].toString());
                        builder.addFormDataPart("viterous_name[]", viterousName_array[j].toString());
                        builder.addFormDataPart("viterous_docid[]", viterousDocID_array[j].toString());
                        builder.addFormDataPart("viterous_doctype[]", viterousDocType_array[j].toString());
                        builder.addFormDataPart("viterous_leftEye[]", viterousLeftEye_array[j].toString());
                        builder.addFormDataPart("viterous_rightEye[]", viterousRightEye_array[j].toString());
                        builder.addFormDataPart("viterous_userid[]", viterousUserID_array[j].toString());
                        builder.addFormDataPart("viterous_loginType[]", viterousLoginType_array[j].toString());
                    }
                }
            }

            if(patient_fundus_array.size() > 0) {
                ArrayList<String> fundusIDArrayList = new ArrayList<String>();
                ArrayList<String> fundusNameArrayList = new ArrayList<String>();
                ArrayList<String> fundusDocIDArrayList = new ArrayList<String>();
                ArrayList<String> fundusDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> fundusLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> fundusRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> fundusUserIDArrayList = new ArrayList<String>();
                ArrayList<String> fundusLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_fundus_array.size(); i++) {
                    fundusIDArrayList.add(String.valueOf(patient_fundus_array.get(i).getFundusId()));
                    fundusNameArrayList.add(patient_fundus_array.get(i).getFundusName().trim());
                    fundusDocIDArrayList.add(String.valueOf(patient_fundus_array.get(i).getDocId()));
                    fundusDocTypeArrayList.add(String.valueOf(patient_fundus_array.get(i).getDocType()));
                    fundusLeftEyeArrayList.add(patient_fundus_array.get(i).getLeftEye());
                    fundusRightEyeArrayList.add(String.valueOf(patient_fundus_array.get(i).getRightEye()));
                    fundusUserIDArrayList.add(String.valueOf(patient_fundus_array.get(i).getUserId()));
                    fundusLoginTypeArrayList.add(String.valueOf(patient_fundus_array.get(i).getLoginType()));
                }

                String[] fundusID_array = new String[fundusIDArrayList.size()];
                String[] fundusName_array = new String[fundusNameArrayList.size()];
                String[] fundusDocID_array = new String[fundusDocIDArrayList.size()];
                String[] fundusDocType_array = new String[fundusDocTypeArrayList.size()];
                String[] fundusLeftEye_array = new String[fundusLeftEyeArrayList.size()];
                String[] fundusRightEye_array = new String[fundusRightEyeArrayList.size()];
                String[] fundusUserID_array = new String[fundusUserIDArrayList.size()];
                String[] fundusLoginType_array = new String[fundusLoginTypeArrayList.size()];

                if(fundusIDArrayList.size() > 0) {
                    for (int j = 0; j < fundusIDArrayList.size(); j++) {
                        fundusID_array[j] = fundusIDArrayList.get(j);
                        fundusName_array[j] = fundusNameArrayList.get(j);
                        fundusDocID_array[j]  = fundusDocIDArrayList.get(j);
                        fundusDocType_array[j]  = fundusDocTypeArrayList.get(j);
                        fundusLeftEye_array[j] = fundusLeftEyeArrayList.get(j);
                        fundusRightEye_array[j]  = fundusRightEyeArrayList.get(j);
                        fundusUserID_array[j]  = fundusUserIDArrayList.get(j);
                        fundusLoginType_array[j]  = fundusLoginTypeArrayList.get(j);
                        builder.addFormDataPart("fundus_id[]", fundusID_array[j].toString());
                        builder.addFormDataPart("fundus_name[]", fundusName_array[j].toString());
                        builder.addFormDataPart("fundus_docid[]", fundusDocID_array[j].toString());
                        builder.addFormDataPart("fundus_doctype[]", fundusDocType_array[j].toString());
                        builder.addFormDataPart("fundus_leftEye[]", fundusLeftEye_array[j].toString());
                        builder.addFormDataPart("fundus_rightEye[]", fundusRightEye_array[j].toString());
                        builder.addFormDataPart("fundus_userid[]", fundusUserID_array[j].toString());
                        builder.addFormDataPart("fundus_loginType[]", fundusLoginType_array[j].toString());
                    }
                }
            }

            if(patient_investigation_array.size() >0) {

                ArrayList<String> investIDArrayList = new ArrayList<String>();
                ArrayList<String> testIDArrayList = new ArrayList<String>();
                ArrayList<String> grouptestIDArrayList = new ArrayList<String>();
                ArrayList<String> testNameArrayList = new ArrayList<String>();
                ArrayList<String> groupTestArrayList = new ArrayList<String>();
                ArrayList<String> mfRangeArrayList = new ArrayList<String>();
                ArrayList<String> normalRangeArrayList = new ArrayList<String>();
                ArrayList<String> actualRangeArrayList = new ArrayList<String>();
                ArrayList<String> rightEyeArrayList = new ArrayList<String>();
                ArrayList<String> leftEyeArrayList = new ArrayList<String>();
                ArrayList<String> departmentArrayList = new ArrayList<String>();

                for(int j = 0; j < patient_investigation_array.size(); j++) {
                    investIDArrayList.add(String.valueOf(patient_investigation_array.get(j).getInvestigationId()));
                    testIDArrayList.add(patient_investigation_array.get(j).getTestId());
                    grouptestIDArrayList.add(patient_investigation_array.get(j).getGroupTestId());
                    testNameArrayList.add(String.valueOf(patient_investigation_array.get(j).getTestName()));
                    groupTestArrayList.add(String.valueOf(patient_investigation_array.get(j).getGroupTest()));
                    mfRangeArrayList.add(patient_investigation_array.get(j).getMFRange());
                    normalRangeArrayList.add(String.valueOf(patient_investigation_array.get(j).getNormalRange()));
                    actualRangeArrayList.add(String.valueOf(patient_investigation_array.get(j).getTestActualValue()));
                    rightEyeArrayList.add(String.valueOf(patient_investigation_array.get(j).getRightEye()));
                    leftEyeArrayList.add(String.valueOf(patient_investigation_array.get(j).getLeftEye()));
                    departmentArrayList.add(String.valueOf(patient_investigation_array.get(j).getInvestDepartment()));
                }

                String[] investID_array = new String[investIDArrayList.size()];
                String[] testID_array = new String[testIDArrayList.size()];
                String[] grouptestID_array = new String[grouptestIDArrayList.size()];
                String[] testName_array = new String[testNameArrayList.size()];
                String[] groupTest_array = new String[groupTestArrayList.size()];
                String[] mfRange_array = new String[mfRangeArrayList.size()];
                String[] normalRange_array = new String[normalRangeArrayList.size()];
                String[] actualRange_array = new String[actualRangeArrayList.size()];
                String[] rightEyeRange_array = new String[rightEyeArrayList.size()];
                String[] leftEyeRange_array = new String[leftEyeArrayList.size()];
                String[] departmentRange_array = new String[departmentArrayList.size()];

                if(investIDArrayList.size() > 0) {
                    for (int k = 0; k < investIDArrayList.size(); k++) {
                        investID_array[k] = investIDArrayList.get(k);
                        testID_array[k] = testIDArrayList.get(k);
                        grouptestID_array[k] = grouptestIDArrayList.get(k);
                        testName_array[k]  = testNameArrayList.get(k);
                        groupTest_array[k]  = groupTestArrayList.get(k);
                        mfRange_array[k] = mfRangeArrayList.get(k);
                        normalRange_array[k]  = normalRangeArrayList.get(k);
                        actualRange_array[k]  = actualRangeArrayList.get(k);
                        rightEyeRange_array[k]  = rightEyeArrayList.get(k);
                        leftEyeRange_array[k]  = leftEyeArrayList.get(k);
                        departmentRange_array[k]  = departmentArrayList.get(k);
                        Log.d(Utils.TAG, " investID: "+investID_array[k].toString());
                        Log.d(Utils.TAG, " testID: "+testID_array[k].toString());
                        Log.d(Utils.TAG, " grouptestID: "+ grouptestID_array[k].toString());
                        builder.addFormDataPart("investigation_id[]", investID_array[k].toString());
                        builder.addFormDataPart("test_id[]", testID_array[k].toString());
                        builder.addFormDataPart("grouptest_id[]", grouptestID_array[k].toString());
                        builder.addFormDataPart("test_name[]", testName_array[k].toString());

                        if(groupTest_array[k] != null) {
                            builder.addFormDataPart("group_test[]", groupTest_array[k].toString());
                        }

                        if(mfRange_array[k] != null) {
                            builder.addFormDataPart("mfRange[]", mfRange_array[k].toString());
                        }

                        if(normalRange_array[k] != null) {
                            builder.addFormDataPart("normalRange[]", normalRange_array[k].toString());
                        }

                        if(actualRange_array[k] != null) {
                            builder.addFormDataPart("actualRange[]", actualRange_array[k].toString());
                        }

                        if(rightEyeRange_array[k] != null) {
                            builder.addFormDataPart("rightEyeRange[]", rightEyeRange_array[k].toString());
                        }

                        if(leftEyeRange_array[k] != null) {
                            builder.addFormDataPart("leftEyeRange[]", leftEyeRange_array[k].toString());
                        }

                        if(departmentRange_array[k] != null) {
                            builder.addFormDataPart("departmentRange[]", departmentRange_array[k].toString());
                        }
                    }
                }

            }

            if(patient_daignosis_array.size() > 0) {

                ArrayList<String> freqDiagnoIDArrayList = new ArrayList<String>();
                ArrayList<String> icdIDArrayList = new ArrayList<String>();
                ArrayList<String> icdNameArrayList = new ArrayList<String>();
                ArrayList<String> diagnoDocIDArrayList = new ArrayList<String>();
                ArrayList<String> diagnoDocTypeArrayList = new ArrayList<String>();

                for (int i = 0; i < patient_daignosis_array.size(); i++) {
                    freqDiagnoIDArrayList.add(String.valueOf(patient_daignosis_array.get(i).getDiagnoFreqId()));
                    icdIDArrayList.add(String.valueOf(patient_daignosis_array.get(i).getICDId()));
                    icdNameArrayList.add(String.valueOf(patient_daignosis_array.get(i).getICDName()));
                    diagnoDocIDArrayList.add(String.valueOf(patient_daignosis_array.get(i).getDocId()));
                    diagnoDocTypeArrayList.add(String.valueOf(patient_daignosis_array.get(i).getDocType()));
                }

                String[] freqDiagnoID_array = new String[freqDiagnoIDArrayList.size()];
                String[] icdID_array = new String[icdIDArrayList.size()];
                String[] icdName_array = new String[icdNameArrayList.size()];
                String[] diagnoDoc_array = new String[diagnoDocIDArrayList.size()];
                String[] diagnoDocType_array = new String[diagnoDocTypeArrayList.size()];

                if (freqDiagnoIDArrayList.size() > 0) {
                    for (int j = 0; j < freqDiagnoIDArrayList.size(); j++) {
                        freqDiagnoID_array[j] = freqDiagnoIDArrayList.get(j);
                        icdID_array[j] = icdIDArrayList.get(j);
                        icdName_array[j] = icdNameArrayList.get(j);
                        diagnoDoc_array[j] = diagnoDocIDArrayList.get(j);
                        diagnoDocType_array[j] = diagnoDocTypeArrayList.get(j);
                        builder.addFormDataPart("diagno_frequent_id[]", freqDiagnoID_array[j].toString());
                        builder.addFormDataPart("diagno_icdID[]", icdID_array[j].toString());
                        builder.addFormDataPart("diagno_icdName[]", icdName_array[j].toString());
                        builder.addFormDataPart("diagno_docID[]", diagnoDoc_array[j].toString());
                        builder.addFormDataPart("diagno_doctype[]", diagnoDocType_array[j].toString());
                    }
                }
            }

            if(patient_treatment_array.size() > 0 ) {
                ArrayList<String> treatmentIDArrayList = new ArrayList<String>();
                ArrayList<String> treatmentNameArrayList = new ArrayList<String>();
                ArrayList<String> treatmentDocIDArrayList = new ArrayList<String>();
                ArrayList<String> treatmentDocTypeArrayList = new ArrayList<String>();

                for (int i = 0; i < patient_treatment_array.size(); i++) {
                    treatmentIDArrayList.add(String.valueOf(patient_treatment_array.get(i).getTreatmentID()));
                    treatmentNameArrayList.add(patient_treatment_array.get(i).getTreatmentName().trim());
                    treatmentDocIDArrayList.add(String.valueOf(patient_treatment_array.get(i).getTreatmentDocID()));
                    treatmentDocTypeArrayList.add(String.valueOf(patient_treatment_array.get(i).getTreatmentDocType()));
                }

                String[] treatmentID_array = new String[treatmentIDArrayList.size()];
                String[] treatmentName_array = new String[treatmentNameArrayList.size()];
                String[] treatmentDocID_array = new String[treatmentDocIDArrayList.size()];
                String[] treatmentDocType_array = new String[treatmentDocTypeArrayList.size()];

                if (treatmentIDArrayList.size() > 0) {
                    for (int j = 0; j < treatmentIDArrayList.size(); j++) {
                        treatmentID_array[j] = treatmentIDArrayList.get(j);
                        treatmentName_array[j] = treatmentNameArrayList.get(j);
                        treatmentDocID_array[j] = treatmentDocIDArrayList.get(j);
                        treatmentDocType_array[j] = treatmentDocTypeArrayList.get(j);
                        builder.addFormDataPart("treatment_id[]", treatmentID_array[j].toString());
                        builder.addFormDataPart("treatment_name[]", treatmentName_array[j].toString());
                        builder.addFormDataPart("treatment_docid[]", treatmentDocID_array[j].toString());
                        builder.addFormDataPart("treatment_doctype[]", treatmentDocType_array[j].toString());
                    }
                }
            }

            if(patient_prescription_array.size() > 0 ) {

                ArrayList<String> freqPrescIDArrayList = new ArrayList<String>();
                ArrayList<String> prescriptionIDArrayList = new ArrayList<String>();
                ArrayList<String> tradeNameArrayList = new ArrayList<String>();
                ArrayList<String> genericIDArrayList = new ArrayList<String>();
                ArrayList<String> genericNameArrayList = new ArrayList<String>();
                ArrayList<String> dosageArrayList = new ArrayList<String>();
                ArrayList<String> timingsArrayList = new ArrayList<String>();
                ArrayList<String> durationArrayList = new ArrayList<String>();

                for (int i = 0; i < patient_prescription_array.size(); i++) {
                    freqPrescIDArrayList.add(String.valueOf(patient_prescription_array.get(i).getPrescFreqId()));
                    prescriptionIDArrayList.add(String.valueOf(patient_prescription_array.get(i).getPrescriptionId()));
                    tradeNameArrayList.add(String.valueOf(patient_prescription_array.get(i).getTradeName()));
                    genericIDArrayList.add(String.valueOf(patient_prescription_array.get(i).getGenericId()));
                    genericNameArrayList.add(patient_prescription_array.get(i).getGenericName().trim());
                    dosageArrayList.add(String.valueOf(patient_prescription_array.get(i).getDosage()));
                    timingsArrayList.add(String.valueOf(patient_prescription_array.get(i).getTimings()));
                    durationArrayList.add(String.valueOf(patient_prescription_array.get(i).getDuration()));
                }

                String[] freqPrescID_array = new String[freqPrescIDArrayList.size()];
                String[] prescriptionID_array = new String[prescriptionIDArrayList.size()];
                String[] tradeName_array = new String[tradeNameArrayList.size()];
                String[] genericID_array = new String[genericIDArrayList.size()];
                String[] genericName_array = new String[genericNameArrayList.size()];
                String[] dosage_array = new String[dosageArrayList.size()];
                String[] timings_array = new String[timingsArrayList.size()];
                String[] duration_array = new String[durationArrayList.size()];

                if (freqPrescIDArrayList.size() > 0) {
                    for (int j = 0; j < freqPrescIDArrayList.size(); j++) {
                        freqPrescID_array[j] = freqPrescIDArrayList.get(j);
                        prescriptionID_array[j] = prescriptionIDArrayList.get(j);
                        tradeName_array[j] = tradeNameArrayList.get(j);
                        genericID_array[j] = genericIDArrayList.get(j);
                        genericName_array[j] = genericNameArrayList.get(j);
                        dosage_array[j] = dosageArrayList.get(j);
                        timings_array[j] = timingsArrayList.get(j);
                        duration_array[j] = durationArrayList.get(j);
                        builder.addFormDataPart("prescription_frequent_id[]", freqPrescID_array[j].toString());
                        builder.addFormDataPart("prescription_ppID[]", prescriptionID_array[j].toString());
                        builder.addFormDataPart("prescription_tradeName[]", tradeName_array[j].toString());
                        builder.addFormDataPart("prescription_genericID[]", genericID_array[j].toString());
                        builder.addFormDataPart("prescription_genericName[]", genericName_array[j].toString());
                        builder.addFormDataPart("prescription_dosage[]", dosage_array[j].toString());
                        builder.addFormDataPart("prescription_timings[]", timings_array[j].toString());
                        builder.addFormDataPart("prescription_duration[]", duration_array[j].toString());
                    }
                }
            }

            if(visit_chiefMedComplaint_sufferings != null) {
                builder.addFormDataPart("chiefMedComplaint_sufferings", visit_chiefMedComplaint_sufferings);
            }

            if(patient_education != 0) {
                builder.addFormDataPart("patient_education", String.valueOf(patient_education));
            }

            if(investigation_template_name != null) {
                builder.addFormDataPart("investigation_template_name", investigation_template_name);
            }

            if(investigation_template_save != 0) {
                builder.addFormDataPart("investigation_template_save", String.valueOf(investigation_template_save));
            }
            else {
                builder.addFormDataPart("investigation_template_save", String.valueOf(investigation_template_save));
            }

            if(distanceVision_RE != null) {
                builder.addFormDataPart("slctDistVisionRE", distanceVision_RE);
            }

            if(distanceVision_LE != null) {
                builder.addFormDataPart("slctDistVisionLE", distanceVision_LE);
            }

            if(nearVision_RE != null) {
                builder.addFormDataPart("slctNearVisionRE", nearVision_RE);
            }

            if(nearVision_LE != null) {
                builder.addFormDataPart("slctNearVisionLE", nearVision_LE);
            }

            if(refraction_RE_top != null) {
                builder.addFormDataPart("se_refractionRE_value1", refraction_RE_top);
            }

            if(refraction_RE_bottom != null) {
                builder.addFormDataPart("se_refractionRE_value2", refraction_RE_bottom);
            }

            if(refraction_LE_top != null) {
                builder.addFormDataPart("se_refractionLE_value1", refraction_LE_top);
            }

            if(refraction_LE_bottom != null) {
                builder.addFormDataPart("se_refractionLE_value2", refraction_LE_bottom);
            }

            if(conultationFees != null) {
                builder.addFormDataPart("consultation_fees", conultationFees);
            }

            if(followupdate != null) {
                builder.addFormDataPart("followup_dates", followupdate);
            }

            if(diagnosis_details != null) {
                builder.addFormDataPart("diagnosis_details", diagnosis_details);
            }

            if(treatment_details != null) {
                builder.addFormDataPart("treatment_details", treatment_details);
            }

            if(prescription_note != null) {
                builder.addFormDataPart("prescription_note", prescription_note);
            }

            if(visit_entry_date != null) {
                builder.addFormDataPart("visit_entry_date", visit_entry_date);
            }

            if(get_dv_sphere_right != null) {
                builder.addFormDataPart("DvSpeherRE", get_dv_sphere_right);
            }

            if(get_dv_cyl_right != null) {
                builder.addFormDataPart("DvCylRE", get_dv_cyl_right);
            }

            if(get_dv_axis_right != null) {
                builder.addFormDataPart("DvAxisRE", get_dv_axis_right);
            }

            if(get_nv_sphere_right != null) {
                builder.addFormDataPart("NvSpeherRE", get_nv_sphere_right);
            }

            if(get_nv_cyl_right != null) {
                builder.addFormDataPart("NvCylRE", get_nv_cyl_right);
            }

            if(get_nv_axis_right != null) {
                builder.addFormDataPart("NvAxisRE", get_nv_axis_right);
            }

            if(get_ipd_right != null) {
                builder.addFormDataPart("IpdRE", get_ipd_right);
            }

            if(get_dv_sphere_left != null) {
                builder.addFormDataPart("DvSpeherLE", get_dv_sphere_left);
            }

            if(get_dv_cyl_left != null) {
                builder.addFormDataPart("DvCylLE", get_dv_cyl_left);
            }

            if(get_dv_axis_left != null) {
                builder.addFormDataPart("DvAxisLE", get_dv_axis_left);
            }

            if(get_nv_sphere_left != null) {
                builder.addFormDataPart("NvSpeherLE", get_nv_sphere_left);
            }

            if(get_nv_cyl_left != null) {
                builder.addFormDataPart("NvCylLE", get_nv_cyl_left);
            }

            if(get_nv_axis_left != null) {
                builder.addFormDataPart("NvAxisLE", get_nv_axis_left);
            }

            if(get_ipd_left != null) {
                builder.addFormDataPart("IpdLE", get_ipd_left);
            }

            if(distanceVision_RE_Unaided != null) {
                builder.addFormDataPart("slctDistVisionUnaidedRE", distanceVision_RE_Unaided);
            }

            if(distanceVision_LE_Unaided != null) {
                builder.addFormDataPart("slctDistVisionUnaidedLE", distanceVision_LE_Unaided);
            }

            if(nearVision_RE_Unaided != null) {
                builder.addFormDataPart("slctNearVisionUnaidedRE", nearVision_RE_Unaided);
            }

            if(nearVision_LE_Unaided != null) {
                builder.addFormDataPart("slctNearVisionUnaidedLE", nearVision_LE_Unaided);
            }

            if(iop_RE != null) {
                builder.addFormDataPart("slctIop_RE", iop_RE);
            }

            if(iop_LE != null) {
                builder.addFormDataPart("slctIop_LE", iop_LE);
            }



            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_PATIENT_ADD_VISIT_OPHTHAL)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " addVisitOphthal: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }

        return null;
    }

    public static JSONObject updateMedicalHistory(int patient_id, String patient_name, String weight, String height,
                                                  String bmi, String patient_hypertension, String patient_diabetes,
                                                  String patient_smoking, String patient_alcohol, String prev_intervention,
                                                  String stroke, String kidney_issue, String other_details,
                                                  List<DrugAllery> patient_drug_allerty_array,
                                                  List<DrugAbuse> patient_drug_abuse_array,
                                                  List<FamilyHistory> patient_family_history_array,
                                                  String PATIENT_CHOLESTEROL,  String PATIENT_ARTHRITIS,
                                                  String PATIENT_IHD, String PATIENT_ASTHAMA,  String PATIENT_THY,
                                                  int user_id, String user_login_type) {

        Log.d(Utils.TAG, " ************  update Medical History  *************** ");
        Log.d(Utils.TAG, " user_login_type: "+user_login_type);

        try {

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("patient_id", String.valueOf(patient_id));
            builder.addFormDataPart("patient_name", patient_name);
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", user_login_type);

            if(patient_drug_allerty_array.size() > 0) {

                ArrayList<String> allergyIDArrayList = new ArrayList<String>();
                ArrayList<String> allergyGenIDArrayList = new ArrayList<String>();
                ArrayList<String> allergyGenNameArrayList = new ArrayList<String>();
                ArrayList<String> allergyPatientArrayList = new ArrayList<String>();
                ArrayList<String> allergyDocIDArrayList = new ArrayList<String>();
                ArrayList<String> allergyDocTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_drug_allerty_array.size(); i++) {
                    allergyIDArrayList.add(String.valueOf(patient_drug_allerty_array.get(i).getGenericId()));
                    allergyGenIDArrayList.add(String.valueOf(patient_drug_allerty_array.get(i).getGenericId()));
                    allergyGenNameArrayList.add(patient_drug_allerty_array.get(i).getGenericName().trim());
                    allergyPatientArrayList.add(String.valueOf(patient_drug_allerty_array.get(i).getPatientId()));
                    allergyDocIDArrayList.add(String.valueOf(patient_drug_allerty_array.get(i).getDocId()));
                    allergyDocTypeArrayList.add(String.valueOf(patient_drug_allerty_array.get(i).getDocType()));
                }

                String[] allergyID_array = new String[allergyIDArrayList.size()];
                String[] allergyGenID_array = new String[allergyGenIDArrayList.size()];
                String[] allergyGenName_array = new String[allergyGenNameArrayList.size()];
                String[] compPatientID_array = new String[allergyPatientArrayList.size()];
                String[] allergyDocID_array = new String[allergyDocIDArrayList.size()];
                String[] allergyDocName_array = new String[allergyDocTypeArrayList.size()];

                if(allergyIDArrayList.size() > 0) {
                    for (int j = 0; j < allergyIDArrayList.size(); j++) {
                        allergyID_array[j] = allergyIDArrayList.get(j);
                        allergyGenID_array[j] = allergyGenIDArrayList.get(j);
                        allergyGenName_array[j] = allergyGenNameArrayList.get(j);
                        compPatientID_array[j]  = allergyPatientArrayList.get(j);
                        allergyDocID_array[j]  = allergyDocIDArrayList.get(j);
                        allergyDocName_array[j]  = allergyDocTypeArrayList.get(j);

                        builder.addFormDataPart("allergy_id[]", allergyID_array[j].toString());
                        builder.addFormDataPart("allergy_generic_id[]", allergyGenID_array[j].toString());
                        builder.addFormDataPart("allergy_generic_name[]", allergyGenName_array[j].toString());
                        builder.addFormDataPart("allergy_docid[]", allergyDocID_array[j].toString());
                        builder.addFormDataPart("allergy_doctype[]", allergyDocName_array[j].toString());
                    }
                }

            }

            if(patient_drug_abuse_array.size() > 0) {
                ArrayList<String> abuseIDArrayList = new ArrayList<String>();
                ArrayList<String> abuseNameArrayList = new ArrayList<String>();
                ArrayList<String> abuseDocIDArrayList = new ArrayList<String>();
                ArrayList<String> abuseDocTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_drug_abuse_array.size(); i++) {
                    abuseIDArrayList.add(String.valueOf(patient_drug_abuse_array.get(i).getAbuseId()));
                    abuseNameArrayList.add(patient_drug_abuse_array.get(i).getAbuseName().trim());
                    abuseDocIDArrayList.add(String.valueOf(patient_drug_abuse_array.get(i).getDocId()));
                    abuseDocTypeArrayList.add(String.valueOf(patient_drug_abuse_array.get(i).getDocType()));
                }

                String[] abuseID_array = new String[abuseIDArrayList.size()];
                String[] abuseName_array = new String[abuseNameArrayList.size()];
                String[] abuseDocID_array = new String[abuseDocIDArrayList.size()];
                String[] abuseDocName_array = new String[abuseDocTypeArrayList.size()];

                if(abuseIDArrayList.size() > 0) {
                    for (int j = 0; j < abuseIDArrayList.size(); j++) {
                        abuseID_array[j] = abuseIDArrayList.get(j);
                        abuseName_array[j] = abuseNameArrayList.get(j);
                        abuseDocID_array[j]  = abuseDocIDArrayList.get(j);
                        abuseDocName_array[j]  = abuseDocTypeArrayList.get(j);

                        builder.addFormDataPart("abuse_id[]", abuseID_array[j].toString());
                        builder.addFormDataPart("abuse_name[]", abuseName_array[j].toString());
                        builder.addFormDataPart("abuse_docid[]", abuseDocID_array[j].toString());
                        builder.addFormDataPart("abuse_doctype[]", abuseDocName_array[j].toString());
                    }
                }
            }

            if(patient_family_history_array.size() > 0) {
                ArrayList<String> familyIDArrayList = new ArrayList<String>();
                ArrayList<String> familyNameArrayList = new ArrayList<String>();
                ArrayList<String> familyDocIDArrayList = new ArrayList<String>();
                ArrayList<String> familyDocTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_family_history_array.size(); i++) {
                    familyIDArrayList.add(String.valueOf(patient_family_history_array.get(i).getFamilyHistoryId()));
                    familyNameArrayList.add(patient_family_history_array.get(i).getFamilyHistoryName().trim());
                    familyDocIDArrayList.add(String.valueOf(patient_family_history_array.get(i).getDocId()));
                    familyDocTypeArrayList.add(String.valueOf(patient_family_history_array.get(i).getDocType()));
                }

                String[] familyID_array = new String[familyIDArrayList.size()];
                String[] familyName_array = new String[familyNameArrayList.size()];
                String[] familyDocID_array = new String[familyDocIDArrayList.size()];
                String[] familyDocName_array = new String[familyDocTypeArrayList.size()];

                if(familyIDArrayList.size() > 0) {
                    for (int j = 0; j < familyIDArrayList.size(); j++) {
                        familyID_array[j] = familyIDArrayList.get(j);
                        familyName_array[j] = familyNameArrayList.get(j);
                        familyDocID_array[j]  = familyDocIDArrayList.get(j);
                        familyDocName_array[j]  = familyDocTypeArrayList.get(j);

                        builder.addFormDataPart("family_id[]", familyID_array[j].toString());
                        builder.addFormDataPart("family_name[]", familyName_array[j].toString());
                        builder.addFormDataPart("family_docid[]", familyDocID_array[j].toString());
                        builder.addFormDataPart("family_doctype[]", familyDocName_array[j].toString());
                    }
                }
            }

            if(weight != null) {
                builder.addFormDataPart("patient_weight", weight);
            }

            if(height != null) {
                builder.addFormDataPart("patient_height", height);
            }

            if(bmi != null) {
                builder.addFormDataPart("patient_bmi", bmi);
            }

            if(patient_hypertension != null) {
                builder.addFormDataPart("patient_hypertension", patient_hypertension);
            }

            if(patient_diabetes != null) {
                builder.addFormDataPart("patient_diabetes", patient_diabetes);
            }

            if(patient_smoking != null) {
                builder.addFormDataPart("patient_smoking", patient_smoking);
            }

            if(patient_alcohol != null) {
                builder.addFormDataPart("patient_alcohol", patient_alcohol);
            }

            if(prev_intervention != null) {
                builder.addFormDataPart("prev_intervention", prev_intervention);
            }

            if(stroke != null) {
                builder.addFormDataPart("stroke", stroke);
            }

            if(kidney_issue != null) {
                builder.addFormDataPart("kidney_issue", kidney_issue);
            }

            if(other_details != null) {
                builder.addFormDataPart("other_details", other_details);
            }

            if(PATIENT_CHOLESTEROL != null) {
                builder.addFormDataPart("patient_cholesterol", PATIENT_CHOLESTEROL);
            }

            if(PATIENT_ARTHRITIS != null) {
                builder.addFormDataPart("patient_arthritis", PATIENT_ARTHRITIS);
            }

            if(PATIENT_IHD != null) {
                builder.addFormDataPart("patient_ihd", PATIENT_IHD);
            }

            if(PATIENT_ASTHAMA != null) {
                builder.addFormDataPart("patient_asthama", PATIENT_ASTHAMA);
            }

            if(PATIENT_THY != null) {
                builder.addFormDataPart("patient_thy", PATIENT_THY);
            }

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_PATIENT_MEDICAL_HISTORY_UPDATE)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " updateMedicalHistory: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }


        return null;
    }

    public static JSONObject addTrendAnalysis(int patient_id, String trend_add_date, String dvSphereRE_val, String dvCylRE_val,
                                              String dvAxisRE_val, String dvSpeherLE_val, String dvCylLE_val,
                                              String dvAxisLE_val, String nvSpeherRE_val, String nvCylRE_val,
                                              String nvAxisRE_val, String nvSpeherLE_val, String nvCylLE_val,
                                              String nvAxisLE_val, String ipdRE_val, String ipdLE_val, int user_id,
                                              String user_login_type) {

        try {

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", user_login_type);
            builder.addFormDataPart("se_patient_id", String.valueOf(patient_id));

            if(trend_add_date != null){
                DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = inputFormat.parse(trend_add_date);
                    String trend_add_date_new = outputFormat.format(date);
                    builder.addFormDataPart("trend_add_date", trend_add_date_new);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if(dvSphereRE_val != null){
                builder.addFormDataPart("dvSphereRE", dvSphereRE_val);
            }
            if(dvCylRE_val != null){
                builder.addFormDataPart("dvCylRE", dvCylRE_val);
            }

            if(dvAxisRE_val != null) {
                builder.addFormDataPart("dvAxisRE", dvAxisRE_val);
            }

            if(dvSpeherLE_val != null) {
                builder.addFormDataPart("dvSpeherLE", dvSpeherLE_val);
            }

            if(dvCylLE_val != null) {
                builder.addFormDataPart("dvCylLE", dvCylLE_val);
            }

            if(dvAxisLE_val != null) {
                builder.addFormDataPart("dvAxisLE", dvAxisLE_val);
            }

            if(nvSpeherRE_val != null) {
                builder.addFormDataPart("nvSpeherRE", nvSpeherRE_val);
            }

            if(nvCylRE_val != null) {
                builder.addFormDataPart("nvCylRE", nvCylRE_val);
            }

            if(nvAxisRE_val != null) {
                builder.addFormDataPart("nvAxisRE", nvAxisRE_val);
            }

            if(nvSpeherLE_val != null) {
                builder.addFormDataPart("nvSpeherLE", nvSpeherLE_val);
            }

            if(nvCylLE_val != null) {
                builder.addFormDataPart("nvCylLE", nvCylLE_val);
            }

            if(nvAxisLE_val != null) {
                builder.addFormDataPart("nvAxisLE", nvAxisLE_val);
            }

            if(ipdRE_val != null) {
                builder.addFormDataPart("ipdRE", ipdRE_val);
            }

            if(ipdLE_val != null) {
                builder.addFormDataPart("ipdLE", ipdLE_val);
            }


            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_MYPATIENT_ADD_TRENDS_OPHTHAL)
                    .post(requestBody)
                    .build();

            //   OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " create: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }

        return null;
    }

    public static JSONObject uploadVisitReports(int patient_id, ArrayList<String> report_photos,
                                                int user_id, String user_login_type) {
        try {

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            ArrayList<String> attachment_photos = new ArrayList<String>();

            for(int k=0;k<report_photos.size();k++) {
                attachment_photos.add(report_photos.get(k).toString());
                Log.d(Utils.TAG + " txtPhoto: ", String.valueOf(report_photos.get(k).toString()));

                File file = new File(report_photos.get(k));
                if (file.exists()) {
                    final MediaType MEDIA_TYPE = MediaType.parse("image/*");
                    builder.addFormDataPart("file-5[]", file.getName(), RequestBody.create(MEDIA_TYPE, file));
                } else {
                    Log.d(Utils.TAG, "file not exist ");
                }
            }


            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", user_login_type);
            builder.addFormDataPart("patient_id", String.valueOf(patient_id));

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_MYPATIENT_UPLOAD_REPORTS)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " uploadVisitReports: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }


        return null;
    }

    public static JSONObject updateInvestigations(int patient_id, List<Investigations> patient_investigation_array,
                                                  int user_id, String user_login_type) {
        Log.d(Utils.TAG, " ************  updateInvestigations  *************** ");
        try {

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("patient_id", String.valueOf(patient_id));
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", user_login_type);



            if(patient_investigation_array.size() >0) {

                ArrayList<String> investIDArrayList = new ArrayList<String>();
                ArrayList<String> testIDArrayList = new ArrayList<String>();
                ArrayList<String> grouptestIDArrayList = new ArrayList<String>();
                ArrayList<String> testNameArrayList = new ArrayList<String>();
                ArrayList<String> groupTestArrayList = new ArrayList<String>();
                ArrayList<String> mfRangeArrayList = new ArrayList<String>();
                ArrayList<String> normalRangeArrayList = new ArrayList<String>();
                ArrayList<String> actualRangeArrayList = new ArrayList<String>();
                ArrayList<String> rightEyeArrayList = new ArrayList<String>();
                ArrayList<String> leftEyeArrayList = new ArrayList<String>();
                ArrayList<String> departmentArrayList = new ArrayList<String>();

                for(int j = 0; j < patient_investigation_array.size(); j++) {
                    investIDArrayList.add(String.valueOf(patient_investigation_array.get(j).getInvestigationId()));
                    testIDArrayList.add(patient_investigation_array.get(j).getTestId());
                    grouptestIDArrayList.add(patient_investigation_array.get(j).getGroupTestId());
                    testNameArrayList.add(String.valueOf(patient_investigation_array.get(j).getTestName()));
                    groupTestArrayList.add(String.valueOf(patient_investigation_array.get(j).getGroupTest()));
                    mfRangeArrayList.add(patient_investigation_array.get(j).getMFRange());
                    normalRangeArrayList.add(String.valueOf(patient_investigation_array.get(j).getNormalRange()));
                    actualRangeArrayList.add(String.valueOf(patient_investigation_array.get(j).getTestActualValue()));
                    rightEyeArrayList.add(String.valueOf(patient_investigation_array.get(j).getRightEye()));
                    leftEyeArrayList.add(String.valueOf(patient_investigation_array.get(j).getLeftEye()));
                    departmentArrayList.add(String.valueOf(patient_investigation_array.get(j).getInvestDepartment()));
                }

                String[] investID_array = new String[investIDArrayList.size()];
                String[] testID_array = new String[testIDArrayList.size()];
                String[] grouptestID_array = new String[grouptestIDArrayList.size()];
                String[] testName_array = new String[testNameArrayList.size()];
                String[] groupTest_array = new String[groupTestArrayList.size()];
                String[] mfRange_array = new String[mfRangeArrayList.size()];
                String[] normalRange_array = new String[normalRangeArrayList.size()];
                String[] actualRange_array = new String[actualRangeArrayList.size()];
                String[] rightEyeRange_array = new String[rightEyeArrayList.size()];
                String[] leftEyeRange_array = new String[leftEyeArrayList.size()];
                String[] departmentRange_array = new String[departmentArrayList.size()];

                if(investIDArrayList.size() > 0) {
                    for (int k = 0; k < investIDArrayList.size(); k++) {
                        investID_array[k] = investIDArrayList.get(k);
                        testID_array[k] = testIDArrayList.get(k);
                        grouptestID_array[k] = grouptestIDArrayList.get(k);
                        testName_array[k]  = testNameArrayList.get(k);
                        groupTest_array[k]  = groupTestArrayList.get(k);
                        mfRange_array[k] = mfRangeArrayList.get(k);
                        normalRange_array[k]  = normalRangeArrayList.get(k);
                        actualRange_array[k]  = actualRangeArrayList.get(k);
                        rightEyeRange_array[k]  = rightEyeArrayList.get(k);
                        leftEyeRange_array[k]  = leftEyeArrayList.get(k);
                        departmentRange_array[k]  = departmentArrayList.get(k);
                        Log.d(Utils.TAG, " investID: "+investID_array[k].toString());
                        Log.d(Utils.TAG, " testID: "+testID_array[k].toString());
                        Log.d(Utils.TAG, " grouptestID: "+ grouptestID_array[k].toString());
                        builder.addFormDataPart("investigation_id[]", investID_array[k].toString());
                        builder.addFormDataPart("test_id[]", testID_array[k].toString());
                        builder.addFormDataPart("grouptest_id[]", grouptestID_array[k].toString());
                        builder.addFormDataPart("test_name[]", testName_array[k].toString());

                        if(groupTest_array[k] != null) {
                            builder.addFormDataPart("group_test[]", groupTest_array[k].toString());
                        }

                        if(mfRange_array[k] != null) {
                            builder.addFormDataPart("mfRange[]", mfRange_array[k].toString());
                        }

                        if(normalRange_array[k] != null) {
                            builder.addFormDataPart("normalRange[]", normalRange_array[k].toString());
                        }

                        if(actualRange_array[k] != null) {
                            builder.addFormDataPart("actualRange[]", actualRange_array[k].toString());
                        }

                        if(rightEyeRange_array[k] != null) {
                            builder.addFormDataPart("rightEyeRange[]", rightEyeRange_array[k].toString());
                        }

                        if(leftEyeRange_array[k] != null) {
                            builder.addFormDataPart("leftEyeRange[]", leftEyeRange_array[k].toString());
                        }

                        if(departmentRange_array[k] != null) {
                            builder.addFormDataPart("departmentRange[]", departmentRange_array[k].toString());
                        }
                    }
                }
            }

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_UPDATE_INVESTIGATIONS)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " updateInvestigations: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }

        return null;
    }

    public static JSONObject sendDiagnosticReferal(String patient_id, String episode_id,
                                                   List<DiagnosticCentreList> selectedDiagnoCentreArraylist,
                                                   int user_id, String user_login_type) {

        try {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            ArrayList<String> diagnosticArrayList = new ArrayList<String>();
            for(int i = 0; i < selectedDiagnoCentreArraylist.size(); i++) {
                diagnosticArrayList.add(String.valueOf(selectedDiagnoCentreArraylist.get(i).getDiagnoId()));

            }
            String[] diagnotics_array = new String[diagnosticArrayList.size()];
            if(diagnosticArrayList.size() > 0) {
                for (int j = 0; j < diagnosticArrayList.size(); j++) {
                    diagnotics_array[j] = diagnosticArrayList.get(j);
                    builder.addFormDataPart("se_diagnostic_ID[]", diagnotics_array[j].toString());
                }
            }

            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", user_login_type);
            builder.addFormDataPart("patient_id", String.valueOf(patient_id));
            builder.addFormDataPart("episode_id", String.valueOf(episode_id));

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_DIAGNOSTICS_REFER)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " refer: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }


        return null;
    }

    public static JSONObject sendPharmaReferal(String patient_id, String episode_id,
                                               List<PharmaCentreList> selectedPharmaArraylist,
                                               int user_id, String user_login_type) {

        try {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            ArrayList<String> pharmaArrayList = new ArrayList<String>();
            for(int i = 0; i < selectedPharmaArraylist.size(); i++) {
                pharmaArrayList.add(String.valueOf(selectedPharmaArraylist.get(i).getPharmaId()));

            }
            String[] pharma_array = new String[pharmaArrayList.size()];
            if(pharmaArrayList.size() > 0) {
                for (int j = 0; j < pharmaArrayList.size(); j++) {
                    pharma_array[j] = pharmaArrayList.get(j);
                    builder.addFormDataPart("se_pharma_ID[]", pharma_array[j].toString());
                }
            }

            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", user_login_type);
            builder.addFormDataPart("patient_id", String.valueOf(patient_id));
            builder.addFormDataPart("episode_id", String.valueOf(episode_id));

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_PHARMA_REFER)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " add: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }


        return null;
    }

    public static JSONObject sendPrescToPatient(int patient_id, int episode_id, int user_id, String user_login_type,
                                                int send_presc_type, String USER_NAME) {
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", user_login_type);
            builder.addFormDataPart("patient_id", String.valueOf(patient_id));
            builder.addFormDataPart("episode_id", String.valueOf(episode_id));
            builder.addFormDataPart("send_type", String.valueOf(send_presc_type));
            builder.addFormDataPart("doctor_name", USER_NAME);

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_SEND_PRESCRIPTION)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " sendPrescToPatient: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject ophthalEditVisit(int patient_id, int episode_id,
                                              List<ChiefMedicalComplaint> patient_chief_medcomplaint_array,
                                              final String distanceVision_RE, final String distanceVision_LE, final String nearVision_RE, final String nearVision_LE,
                                              List<Lids> patient_lids_array, List<OphthalConjuctiva> patient_conjuctiva_array,
                                              List<OphthalSclera> patient_sclera_array, List<OphthalCornearAnteriorSurface> patient_cornea_anterior_array,
                                              List<OphthalCornearPosteriorSurface> patient_cornea_posterior_array,
                                              List<OphthalAnteriorChamber> patient_anterior_chamber_array,
                                              List<OphthalIris> patient_iris_array, List<OphthalPupil> patient_pupil_array,
                                              List<OphthalAngleAnteriorChamber> patient_angle_array,
                                              List<OphthalLens> patient_lens_array, List<OphthalViterous> patient_viterous_array,
                                              List<OphthalFundus> patient_fundus_array, String refraction_RE_top, String refraction_RE_bottom,
                                              String refraction_LE_top, String refraction_LE_bottom,
                                              final List<Investigations> patient_investigation_array, final List<Diagnosis> patient_daignosis_array,
                                              final List<Treatments> patient_treatment_array, final List<FrequentPrescription> patient_prescription_array,
                                              final String conultationFees, final String followupdate, final String diagnosis_details, final String treatment_details,
                                              final String prescription_note, final String visit_entry_date, final String get_dv_sphere_right, final String get_dv_cyl_right,
                                              final String get_dv_axis_right, final String get_nv_sphere_right, final String get_nv_cyl_right, final String get_nv_axis_right,
                                              final String get_ipd_right, final String get_dv_sphere_left, final String get_dv_cyl_left, final String get_dv_axis_left,
                                              final String get_nv_sphere_left, final String get_nv_cyl_left, final String get_nv_axis_left, final String get_ipd_left,
                                              int user_id, String user_login_type, int HOSPITAL_ID, String visit_chiefMedComplaint_sufferings,
                                              int investigation_template_save, String investigation_template_name, int patient_education,
                                              String distanceVision_Unaided_RE, String distanceVision_Unaided_LE, String NearVision_Unaided_RE, String NearVision_Unaided_LE,
                                              String iop_RE, String iop_LE) {
        Log.d(Utils.TAG, " *************** ophthalAddVisit *************************** ");
        Log.d(Utils.TAG, " patient_id: "+patient_id);
        Log.d(Utils.TAG, " episode_id: "+episode_id);
        Log.d(Utils.TAG, " userid: "+user_id);
        Log.d(Utils.TAG, " login_type: "+user_login_type);
        Log.d(Utils.TAG, " followupdate: "+followupdate);
        Log.d(Utils.TAG, " diagnosis_details: "+diagnosis_details);
        Log.d(Utils.TAG, " treatment_details: "+treatment_details);
        Log.d(Utils.TAG, " conultationFees: "+conultationFees);
        Log.d(Utils.TAG, " prescription_note: "+prescription_note);
        Log.d(Utils.TAG, " visit_entry_date: "+visit_entry_date);

        Log.d(Utils.TAG, " patient_chief_complaint_array: "+patient_chief_medcomplaint_array.size());
        Log.d(Utils.TAG, " patient_lids_array: "+patient_lids_array.size());
        Log.d(Utils.TAG, " patient_conjuctiva_array: "+patient_conjuctiva_array.size());
        Log.d(Utils.TAG, " patient_sclera_array: "+patient_sclera_array.size());
        Log.d(Utils.TAG, " patient_cornea_anterior_array: "+patient_cornea_anterior_array.size());
        Log.d(Utils.TAG, " patient_cornea_posterior_array: "+patient_cornea_posterior_array.size());
        Log.d(Utils.TAG, " patient_anterior_chamber_array: "+patient_anterior_chamber_array.size());
        Log.d(Utils.TAG, " patient_iris_array: "+patient_iris_array.size());
        Log.d(Utils.TAG, " patient_pupil_array: "+patient_pupil_array.size());
        Log.d(Utils.TAG, " patient_angle_array: "+patient_angle_array.size());
        Log.d(Utils.TAG, " patient_lens_array: "+patient_lens_array.size());
        Log.d(Utils.TAG, " patient_viterous_array: "+patient_viterous_array.size());
        Log.d(Utils.TAG, " patient_fundus_array: "+patient_fundus_array.size());
        Log.d(Utils.TAG, " patient_investigation_array: "+patient_investigation_array.size());
        Log.d(Utils.TAG, " patient_daignosis_array: "+patient_daignosis_array.size());
        Log.d(Utils.TAG, " patient_treatment_array: "+patient_treatment_array.size());
        Log.d(Utils.TAG, " patient_prescription_array: "+patient_prescription_array.size());

        try {

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("patient_id", String.valueOf(patient_id));
            builder.addFormDataPart("episode_id", String.valueOf(episode_id));
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", user_login_type);
            builder.addFormDataPart("hosp_id", String.valueOf(HOSPITAL_ID));

            if(patient_chief_medcomplaint_array.size() > 0) {

                ArrayList<String> compIDArrayList = new ArrayList<String>();
                ArrayList<String> compNameArrayList = new ArrayList<String>();
                ArrayList<String> compDocIDArrayList = new ArrayList<String>();
                ArrayList<String> compDocTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_chief_medcomplaint_array.size(); i++) {
                    compIDArrayList.add(String.valueOf(patient_chief_medcomplaint_array.get(i).getComplaintId()));
                    compNameArrayList.add(patient_chief_medcomplaint_array.get(i).getSymptomsName().trim());
                    compDocIDArrayList.add(String.valueOf(patient_chief_medcomplaint_array.get(i).getDocId()));
                    compDocTypeArrayList.add(String.valueOf(patient_chief_medcomplaint_array.get(i).getDocType()));
                }

                String[] compID_array = new String[compIDArrayList.size()];
                String[] compName_array = new String[compNameArrayList.size()];
                String[] compDocID_array = new String[compDocIDArrayList.size()];
                String[] compDocName_array = new String[compDocTypeArrayList.size()];

                if(compIDArrayList.size() > 0) {
                    for (int j = 0; j < compIDArrayList.size(); j++) {
                        compID_array[j] = compIDArrayList.get(j);
                        compName_array[j] = compNameArrayList.get(j);
                        compDocID_array[j]  = compDocIDArrayList.get(j);
                        compDocName_array[j]  = compDocTypeArrayList.get(j);
                        // Log.d(Utils.TAG, " compID: "+ compID_array[j].toString());
                        builder.addFormDataPart("chiefcomplaint_id[]", compID_array[j].toString());
                        builder.addFormDataPart("chiefcomplaint_name[]", compName_array[j].toString());
                        builder.addFormDataPart("chiefcomplaint_docid[]", compDocID_array[j].toString());
                        builder.addFormDataPart("chiefcomplaint_doctype[]", compDocName_array[j].toString());
                    }
                }

            }

            if(patient_lids_array.size() > 0) {
                ArrayList<String> lidsIDArrayList = new ArrayList<String>();
                ArrayList<String> lidsNameArrayList = new ArrayList<String>();
                ArrayList<String> lidsDocIDArrayList = new ArrayList<String>();
                ArrayList<String> lidsDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> lidsLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> lidsRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> lidsUserIDArrayList = new ArrayList<String>();
                ArrayList<String> lidsLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_lids_array.size(); i++) {
                    lidsIDArrayList.add(String.valueOf(patient_lids_array.get(i).getLidsId()));
                    lidsNameArrayList.add(patient_lids_array.get(i).getLidsName().trim());
                    lidsDocIDArrayList.add(String.valueOf(patient_lids_array.get(i).getDocId()));
                    lidsDocTypeArrayList.add(String.valueOf(patient_lids_array.get(i).getDocType()));
                    lidsLeftEyeArrayList.add(patient_lids_array.get(i).getLeftEye());
                    lidsRightEyeArrayList.add(String.valueOf(patient_lids_array.get(i).getRightEye()));
                    lidsUserIDArrayList.add(String.valueOf(patient_lids_array.get(i).getUserId()));
                    lidsLoginTypeArrayList.add(String.valueOf(patient_lids_array.get(i).getLoginType()));
                }

                String[] lidsID_array = new String[lidsIDArrayList.size()];
                String[] lidsName_array = new String[lidsNameArrayList.size()];
                String[] lidsDocID_array = new String[lidsDocIDArrayList.size()];
                String[] lidsDocType_array = new String[lidsDocTypeArrayList.size()];
                String[] lidsLeftEye_array = new String[lidsLeftEyeArrayList.size()];
                String[] lidsRightEye_array = new String[lidsRightEyeArrayList.size()];
                String[] lidsUserID_array = new String[lidsUserIDArrayList.size()];
                String[] lidsLoginType_array = new String[lidsLoginTypeArrayList.size()];

                if(lidsIDArrayList.size() > 0) {
                    for (int j = 0; j < lidsIDArrayList.size(); j++) {
                        lidsID_array[j] = lidsIDArrayList.get(j);
                        lidsName_array[j] = lidsNameArrayList.get(j);
                        lidsDocID_array[j]  = lidsDocIDArrayList.get(j);
                        lidsDocType_array[j]  = lidsDocTypeArrayList.get(j);
                        lidsLeftEye_array[j] = lidsLeftEyeArrayList.get(j);
                        lidsRightEye_array[j]  = lidsRightEyeArrayList.get(j);
                        lidsUserID_array[j]  = lidsUserIDArrayList.get(j);
                        lidsLoginType_array[j]  = lidsLoginTypeArrayList.get(j);
                        builder.addFormDataPart("lids_id[]", lidsID_array[j].toString());
                        builder.addFormDataPart("lids_name[]", lidsName_array[j].toString());
                        builder.addFormDataPart("lids_docid[]", lidsDocID_array[j].toString());
                        builder.addFormDataPart("lids_doctype[]", lidsDocType_array[j].toString());
                        builder.addFormDataPart("lids_leftEye[]", lidsLeftEye_array[j].toString());
                        builder.addFormDataPart("lids_rightEye[]", lidsRightEye_array[j].toString());
                        builder.addFormDataPart("lids_userid[]", lidsUserID_array[j].toString());
                        builder.addFormDataPart("lids_loginType[]", lidsLoginType_array[j].toString());
                    }
                }
            }

            if(patient_conjuctiva_array.size() > 0) {
                ArrayList<String> conjuctivaIDArrayList = new ArrayList<String>();
                ArrayList<String> conjuctivaNameArrayList = new ArrayList<String>();
                ArrayList<String> conjuctivaDocIDArrayList = new ArrayList<String>();
                ArrayList<String> conjuctivaDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> conjuctivaLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> conjuctivaRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> conjuctivaUserIDArrayList = new ArrayList<String>();
                ArrayList<String> conjuctivasLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_conjuctiva_array.size(); i++) {
                    conjuctivaIDArrayList.add(String.valueOf(patient_conjuctiva_array.get(i).getConjuctivaId()));
                    conjuctivaNameArrayList.add(patient_conjuctiva_array.get(i).getConjuctivaName().trim());
                    conjuctivaDocIDArrayList.add(String.valueOf(patient_conjuctiva_array.get(i).getDocId()));
                    conjuctivaDocTypeArrayList.add(String.valueOf(patient_conjuctiva_array.get(i).getDocType()));
                    conjuctivaLeftEyeArrayList.add(patient_conjuctiva_array.get(i).getLeftEye());
                    conjuctivaRightEyeArrayList.add(String.valueOf(patient_conjuctiva_array.get(i).getRightEye()));
                    conjuctivaUserIDArrayList.add(String.valueOf(patient_conjuctiva_array.get(i).getUserId()));
                    conjuctivasLoginTypeArrayList.add(String.valueOf(patient_conjuctiva_array.get(i).getLoginType()));
                }

                String[] conjuctivaID_array = new String[conjuctivaIDArrayList.size()];
                String[] conjuctivaName_array = new String[conjuctivaNameArrayList.size()];
                String[] conjuctivaDocID_array = new String[conjuctivaDocIDArrayList.size()];
                String[] conjuctivaDocType_array = new String[conjuctivaDocTypeArrayList.size()];
                String[] conjuctivaLeftEye_array = new String[conjuctivaLeftEyeArrayList.size()];
                String[] conjuctivaRightEye_array = new String[conjuctivaRightEyeArrayList.size()];
                String[] conjuctivaUserID_array = new String[conjuctivaUserIDArrayList.size()];
                String[] conjuctivaLoginType_array = new String[conjuctivasLoginTypeArrayList.size()];

                if(conjuctivaIDArrayList.size() > 0) {
                    for (int j = 0; j < conjuctivaIDArrayList.size(); j++) {
                        conjuctivaID_array[j] = conjuctivaIDArrayList.get(j);
                        conjuctivaName_array[j] = conjuctivaNameArrayList.get(j);
                        conjuctivaDocID_array[j]  = conjuctivaDocIDArrayList.get(j);
                        conjuctivaDocType_array[j]  = conjuctivaDocTypeArrayList.get(j);
                        conjuctivaLeftEye_array[j] = conjuctivaLeftEyeArrayList.get(j);
                        conjuctivaRightEye_array[j]  = conjuctivaRightEyeArrayList.get(j);
                        conjuctivaUserID_array[j]  = conjuctivaUserIDArrayList.get(j);
                        conjuctivaLoginType_array[j]  = conjuctivasLoginTypeArrayList.get(j);
                        builder.addFormDataPart("conjuctiva_id[]", conjuctivaID_array[j].toString());
                        builder.addFormDataPart("conjuctiva_name[]", conjuctivaName_array[j].toString());
                        builder.addFormDataPart("conjuctiva_docid[]", conjuctivaDocID_array[j].toString());
                        builder.addFormDataPart("conjuctiva_doctype[]", conjuctivaDocType_array[j].toString());
                        builder.addFormDataPart("conjuctiva_leftEye[]", conjuctivaLeftEye_array[j].toString());
                        builder.addFormDataPart("conjuctiva_rightEye[]", conjuctivaRightEye_array[j].toString());
                        builder.addFormDataPart("conjuctiva_userid[]", conjuctivaUserID_array[j].toString());
                        builder.addFormDataPart("conjuctiva_loginType[]", conjuctivaLoginType_array[j].toString());
                    }
                }
            }

            if(patient_sclera_array.size() > 0) {
                ArrayList<String> scleraIDArrayList = new ArrayList<String>();
                ArrayList<String> scleraNameArrayList = new ArrayList<String>();
                ArrayList<String> scleraDocIDArrayList = new ArrayList<String>();
                ArrayList<String> scleraDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> scleraLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> scleraRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> scleraUserIDArrayList = new ArrayList<String>();
                ArrayList<String> scleraLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_sclera_array.size(); i++) {
                    scleraIDArrayList.add(String.valueOf(patient_sclera_array.get(i).getScleraId()));
                    scleraNameArrayList.add(patient_sclera_array.get(i).getScleraName().trim());
                    scleraDocIDArrayList.add(String.valueOf(patient_sclera_array.get(i).getDocId()));
                    scleraDocTypeArrayList.add(String.valueOf(patient_sclera_array.get(i).getDocType()));
                    scleraLeftEyeArrayList.add(patient_sclera_array.get(i).getLeftEye());
                    scleraRightEyeArrayList.add(String.valueOf(patient_sclera_array.get(i).getRightEye()));
                    scleraUserIDArrayList.add(String.valueOf(patient_sclera_array.get(i).getUserId()));
                    scleraLoginTypeArrayList.add(String.valueOf(patient_sclera_array.get(i).getLoginType()));
                }

                String[] scleraID_array = new String[scleraIDArrayList.size()];
                String[] scleraName_array = new String[scleraNameArrayList.size()];
                String[] scleraDocID_array = new String[scleraDocIDArrayList.size()];
                String[] scleraDocType_array = new String[scleraDocTypeArrayList.size()];
                String[] scleraLeftEye_array = new String[scleraLeftEyeArrayList.size()];
                String[] scleraRightEye_array = new String[scleraRightEyeArrayList.size()];
                String[] scleraUserID_array = new String[scleraUserIDArrayList.size()];
                String[] scleraLoginType_array = new String[scleraLoginTypeArrayList.size()];

                if(scleraIDArrayList.size() > 0) {
                    for (int j = 0; j < scleraIDArrayList.size(); j++) {
                        scleraID_array[j] = scleraIDArrayList.get(j);
                        scleraName_array[j] = scleraNameArrayList.get(j);
                        scleraDocID_array[j]  = scleraDocIDArrayList.get(j);
                        scleraDocType_array[j]  = scleraDocTypeArrayList.get(j);
                        scleraLeftEye_array[j] = scleraLeftEyeArrayList.get(j);
                        scleraRightEye_array[j]  = scleraRightEyeArrayList.get(j);
                        scleraUserID_array[j]  = scleraUserIDArrayList.get(j);
                        scleraLoginType_array[j]  = scleraLoginTypeArrayList.get(j);
                        builder.addFormDataPart("sclera_id[]", scleraID_array[j].toString());
                        builder.addFormDataPart("sclera_name[]", scleraName_array[j].toString());
                        builder.addFormDataPart("sclera_docid[]", scleraDocID_array[j].toString());
                        builder.addFormDataPart("sclera_doctype[]", scleraDocType_array[j].toString());
                        builder.addFormDataPart("sclera_leftEye[]", scleraLeftEye_array[j].toString());
                        builder.addFormDataPart("sclera_rightEye[]", scleraRightEye_array[j].toString());
                        builder.addFormDataPart("sclera_userid[]", scleraUserID_array[j].toString());
                        builder.addFormDataPart("sclera_loginType[]", scleraLoginType_array[j].toString());
                    }
                }
            }

            if(patient_cornea_anterior_array.size() > 0) {
                ArrayList<String> anteriorIDArrayList = new ArrayList<String>();
                ArrayList<String> anteriorNameArrayList = new ArrayList<String>();
                ArrayList<String> anteriorDocIDArrayList = new ArrayList<String>();
                ArrayList<String> anteriorDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> anteriorLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> anteriorRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> anteriorUserIDArrayList = new ArrayList<String>();
                ArrayList<String> anteriorLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_cornea_anterior_array.size(); i++) {
                    anteriorIDArrayList.add(String.valueOf(patient_cornea_anterior_array.get(i).getCorneaAnteriorId()));
                    anteriorNameArrayList.add(patient_cornea_anterior_array.get(i).getCorneaAnteriorName().trim());
                    anteriorDocIDArrayList.add(String.valueOf(patient_cornea_anterior_array.get(i).getDocId()));
                    anteriorDocTypeArrayList.add(String.valueOf(patient_cornea_anterior_array.get(i).getDocType()));
                    anteriorLeftEyeArrayList.add(patient_cornea_anterior_array.get(i).getLeftEye());
                    anteriorRightEyeArrayList.add(String.valueOf(patient_cornea_anterior_array.get(i).getRightEye()));
                    anteriorUserIDArrayList.add(String.valueOf(patient_cornea_anterior_array.get(i).getUserId()));
                    anteriorLoginTypeArrayList.add(String.valueOf(patient_cornea_anterior_array.get(i).getLoginType()));
                }

                String[] anteriorID_array = new String[anteriorIDArrayList.size()];
                String[] anteriorName_array = new String[anteriorNameArrayList.size()];
                String[] anteriorDocID_array = new String[anteriorDocIDArrayList.size()];
                String[] anteriorDocType_array = new String[anteriorDocTypeArrayList.size()];
                String[] anteriorLeftEye_array = new String[anteriorLeftEyeArrayList.size()];
                String[] anteriorRightEye_array = new String[anteriorRightEyeArrayList.size()];
                String[] anteriorUserID_array = new String[anteriorUserIDArrayList.size()];
                String[] anteriorLoginType_array = new String[anteriorLoginTypeArrayList.size()];

                if(anteriorIDArrayList.size() > 0) {
                    for (int j = 0; j < anteriorIDArrayList.size(); j++) {
                        anteriorID_array[j] = anteriorIDArrayList.get(j);
                        anteriorName_array[j] = anteriorNameArrayList.get(j);
                        anteriorDocID_array[j]  = anteriorDocIDArrayList.get(j);
                        anteriorDocType_array[j]  = anteriorDocTypeArrayList.get(j);
                        anteriorLeftEye_array[j] = anteriorLeftEyeArrayList.get(j);
                        anteriorRightEye_array[j]  = anteriorRightEyeArrayList.get(j);
                        anteriorUserID_array[j]  = anteriorUserIDArrayList.get(j);
                        anteriorLoginType_array[j]  = anteriorLoginTypeArrayList.get(j);
                        builder.addFormDataPart("cornea_anterior_id[]", anteriorID_array[j].toString());
                        builder.addFormDataPart("cornea_anterior_name[]", anteriorName_array[j].toString());
                        builder.addFormDataPart("cornea_anterior_docid[]", anteriorDocID_array[j].toString());
                        builder.addFormDataPart("cornea_anterior_doctype[]", anteriorDocType_array[j].toString());
                        builder.addFormDataPart("cornea_anterior_leftEye[]", anteriorLeftEye_array[j].toString());
                        builder.addFormDataPart("cornea_anterior_rightEye[]", anteriorRightEye_array[j].toString());
                        builder.addFormDataPart("cornea_anterior_userid[]", anteriorUserID_array[j].toString());
                        builder.addFormDataPart("cornea_anterior_loginType[]", anteriorLoginType_array[j].toString());
                    }
                }
            }

            if(patient_cornea_posterior_array.size() > 0) {
                ArrayList<String> posteriorIDArrayList = new ArrayList<String>();
                ArrayList<String> posteriorNameArrayList = new ArrayList<String>();
                ArrayList<String> posteriorDocIDArrayList = new ArrayList<String>();
                ArrayList<String> posteriorDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> posteriorLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> posteriorRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> posteriorUserIDArrayList = new ArrayList<String>();
                ArrayList<String> posteriorLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_cornea_posterior_array.size(); i++) {
                    posteriorIDArrayList.add(String.valueOf(patient_cornea_posterior_array.get(i).getCorneaPosteriorId()));
                    posteriorNameArrayList.add(patient_cornea_posterior_array.get(i).getCorneaPosteriorName().trim());
                    posteriorDocIDArrayList.add(String.valueOf(patient_cornea_posterior_array.get(i).getDocId()));
                    posteriorDocTypeArrayList.add(String.valueOf(patient_cornea_posterior_array.get(i).getDocType()));
                    posteriorLeftEyeArrayList.add(patient_cornea_posterior_array.get(i).getLeftEye());
                    posteriorRightEyeArrayList.add(String.valueOf(patient_cornea_posterior_array.get(i).getRightEye()));
                    posteriorUserIDArrayList.add(String.valueOf(patient_cornea_posterior_array.get(i).getUserId()));
                    posteriorLoginTypeArrayList.add(String.valueOf(patient_cornea_posterior_array.get(i).getLoginType()));
                }

                String[] posteriorID_array = new String[posteriorIDArrayList.size()];
                String[] posteriorName_array = new String[posteriorNameArrayList.size()];
                String[] posteriorDocID_array = new String[posteriorDocIDArrayList.size()];
                String[] posteriorDocType_array = new String[posteriorDocTypeArrayList.size()];
                String[] posteriorLeftEye_array = new String[posteriorLeftEyeArrayList.size()];
                String[] posteriorRightEye_array = new String[posteriorRightEyeArrayList.size()];
                String[] posteriorUserID_array = new String[posteriorUserIDArrayList.size()];
                String[] posteriorLoginType_array = new String[posteriorLoginTypeArrayList.size()];

                if(posteriorIDArrayList.size() > 0) {
                    for (int j = 0; j < posteriorIDArrayList.size(); j++) {
                        posteriorID_array[j] = posteriorIDArrayList.get(j);
                        posteriorName_array[j] = posteriorNameArrayList.get(j);
                        posteriorDocID_array[j]  = posteriorDocIDArrayList.get(j);
                        posteriorDocType_array[j]  = posteriorDocTypeArrayList.get(j);
                        posteriorLeftEye_array[j] = posteriorLeftEyeArrayList.get(j);
                        posteriorRightEye_array[j]  = posteriorRightEyeArrayList.get(j);
                        posteriorUserID_array[j]  = posteriorUserIDArrayList.get(j);
                        posteriorLoginType_array[j]  = posteriorLoginTypeArrayList.get(j);
                        builder.addFormDataPart("cornea_posterior_id[]", posteriorID_array[j].toString());
                        builder.addFormDataPart("cornea_posterior_name[]", posteriorName_array[j].toString());
                        builder.addFormDataPart("cornea_posterior_docid[]", posteriorDocID_array[j].toString());
                        builder.addFormDataPart("cornea_posterior_doctype[]", posteriorDocType_array[j].toString());
                        builder.addFormDataPart("cornea_posterior_leftEye[]", posteriorLeftEye_array[j].toString());
                        builder.addFormDataPart("cornea_posterior_rightEye[]", posteriorRightEye_array[j].toString());
                        builder.addFormDataPart("cornea_posterior_userid[]", posteriorUserID_array[j].toString());
                        builder.addFormDataPart("cornea_posterior_loginType[]", posteriorLoginType_array[j].toString());
                    }
                }
            }

            if(patient_anterior_chamber_array.size() > 0) {
                ArrayList<String> chamberIDArrayList = new ArrayList<String>();
                ArrayList<String> chamberNameArrayList = new ArrayList<String>();
                ArrayList<String> chamberDocIDArrayList = new ArrayList<String>();
                ArrayList<String> chamberDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> chamberLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> chamberRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> chamberUserIDArrayList = new ArrayList<String>();
                ArrayList<String> chamberLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_anterior_chamber_array.size(); i++) {
                    chamberIDArrayList.add(String.valueOf(patient_anterior_chamber_array.get(i).getAnteriorChamberId()));
                    chamberNameArrayList.add(patient_anterior_chamber_array.get(i).getAnteriorChamberName().trim());
                    chamberDocIDArrayList.add(String.valueOf(patient_anterior_chamber_array.get(i).getDocId()));
                    chamberDocTypeArrayList.add(String.valueOf(patient_anterior_chamber_array.get(i).getDocType()));
                    chamberLeftEyeArrayList.add(patient_anterior_chamber_array.get(i).getLeftEye());
                    chamberRightEyeArrayList.add(String.valueOf(patient_anterior_chamber_array.get(i).getRightEye()));
                    chamberUserIDArrayList.add(String.valueOf(patient_anterior_chamber_array.get(i).getUserId()));
                    chamberLoginTypeArrayList.add(String.valueOf(patient_anterior_chamber_array.get(i).getLoginType()));
                }

                String[] chamberID_array = new String[chamberIDArrayList.size()];
                String[] chamberName_array = new String[chamberNameArrayList.size()];
                String[] chamberDocID_array = new String[chamberDocIDArrayList.size()];
                String[] chamberDocType_array = new String[chamberDocTypeArrayList.size()];
                String[] chamberLeftEye_array = new String[chamberLeftEyeArrayList.size()];
                String[] chamberRightEye_array = new String[chamberRightEyeArrayList.size()];
                String[] chamberUserID_array = new String[chamberUserIDArrayList.size()];
                String[] chamberLoginType_array = new String[chamberLoginTypeArrayList.size()];

                if(chamberIDArrayList.size() > 0) {
                    for (int j = 0; j < chamberIDArrayList.size(); j++) {
                        chamberID_array[j] = chamberIDArrayList.get(j);
                        chamberName_array[j] = chamberNameArrayList.get(j);
                        chamberDocID_array[j]  = chamberDocIDArrayList.get(j);
                        chamberDocType_array[j]  = chamberDocTypeArrayList.get(j);
                        chamberLeftEye_array[j] = chamberLeftEyeArrayList.get(j);
                        chamberRightEye_array[j]  = chamberRightEyeArrayList.get(j);
                        chamberUserID_array[j]  = chamberUserIDArrayList.get(j);
                        chamberLoginType_array[j]  = chamberLoginTypeArrayList.get(j);
                        builder.addFormDataPart("anterior_chamber_id[]", chamberID_array[j].toString());
                        builder.addFormDataPart("anterior_chamber_name[]", chamberName_array[j].toString());
                        builder.addFormDataPart("anterior_chamber_docid[]", chamberDocID_array[j].toString());
                        builder.addFormDataPart("anterior_chamber_doctype[]", chamberDocType_array[j].toString());
                        builder.addFormDataPart("anterior_chamber_leftEye[]", chamberLeftEye_array[j].toString());
                        builder.addFormDataPart("anterior_chamber_rightEye[]", chamberRightEye_array[j].toString());
                        builder.addFormDataPart("anterior_chamber_userid[]", chamberUserID_array[j].toString());
                        builder.addFormDataPart("anterior_chamber_loginType[]", chamberLoginType_array[j].toString());
                    }
                }
            }

            if(patient_iris_array.size() > 0) {
                ArrayList<String> irisIDArrayList = new ArrayList<String>();
                ArrayList<String> irisNameArrayList = new ArrayList<String>();
                ArrayList<String> irisDocIDArrayList = new ArrayList<String>();
                ArrayList<String> irisDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> irisLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> irisRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> irisUserIDArrayList = new ArrayList<String>();
                ArrayList<String> irisLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_iris_array.size(); i++) {
                    irisIDArrayList.add(String.valueOf(patient_iris_array.get(i).getIrisId()));
                    irisNameArrayList.add(patient_iris_array.get(i).getIrisName().trim());
                    irisDocIDArrayList.add(String.valueOf(patient_iris_array.get(i).getDocId()));
                    irisDocTypeArrayList.add(String.valueOf(patient_iris_array.get(i).getDocType()));
                    irisLeftEyeArrayList.add(patient_iris_array.get(i).getLeftEye());
                    irisRightEyeArrayList.add(String.valueOf(patient_iris_array.get(i).getRightEye()));
                    irisUserIDArrayList.add(String.valueOf(patient_iris_array.get(i).getUserId()));
                    irisLoginTypeArrayList.add(String.valueOf(patient_iris_array.get(i).getLoginType()));
                }

                String[] irisID_array = new String[irisIDArrayList.size()];
                String[] irisName_array = new String[irisNameArrayList.size()];
                String[] irisDocID_array = new String[irisDocIDArrayList.size()];
                String[] irisDocType_array = new String[irisDocTypeArrayList.size()];
                String[] irisLeftEye_array = new String[irisLeftEyeArrayList.size()];
                String[] irisRightEye_array = new String[irisRightEyeArrayList.size()];
                String[] irisUserID_array = new String[irisUserIDArrayList.size()];
                String[] irisLoginType_array = new String[irisLoginTypeArrayList.size()];

                if(irisIDArrayList.size() > 0) {
                    for (int j = 0; j < irisIDArrayList.size(); j++) {
                        irisID_array[j] = irisIDArrayList.get(j);
                        irisName_array[j] = irisNameArrayList.get(j);
                        irisDocID_array[j]  = irisDocIDArrayList.get(j);
                        irisDocType_array[j]  = irisDocTypeArrayList.get(j);
                        irisLeftEye_array[j] = irisLeftEyeArrayList.get(j);
                        irisRightEye_array[j]  = irisRightEyeArrayList.get(j);
                        irisUserID_array[j]  = irisUserIDArrayList.get(j);
                        irisLoginType_array[j]  = irisLoginTypeArrayList.get(j);
                        builder.addFormDataPart("iris_id[]", irisID_array[j].toString());
                        builder.addFormDataPart("iris_name[]", irisName_array[j].toString());
                        builder.addFormDataPart("iris_docid[]", irisDocID_array[j].toString());
                        builder.addFormDataPart("iris_doctype[]", irisDocType_array[j].toString());
                        builder.addFormDataPart("iris_leftEye[]", irisLeftEye_array[j].toString());
                        builder.addFormDataPart("iris_rightEye[]", irisRightEye_array[j].toString());
                        builder.addFormDataPart("iris_userid[]", irisUserID_array[j].toString());
                        builder.addFormDataPart("iris_loginType[]", irisLoginType_array[j].toString());
                    }
                }
            }

            if(patient_pupil_array.size() > 0) {
                ArrayList<String> pupilIDArrayList = new ArrayList<String>();
                ArrayList<String> pupilNameArrayList = new ArrayList<String>();
                ArrayList<String> pupilDocIDArrayList = new ArrayList<String>();
                ArrayList<String> pupilDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> pupilLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> pupilRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> pupilUserIDArrayList = new ArrayList<String>();
                ArrayList<String> pupilLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_pupil_array.size(); i++) {
                    pupilIDArrayList.add(String.valueOf(patient_pupil_array.get(i).getPupilId()));
                    pupilNameArrayList.add(patient_pupil_array.get(i).getPupilName().trim());
                    pupilDocIDArrayList.add(String.valueOf(patient_pupil_array.get(i).getDocId()));
                    pupilDocTypeArrayList.add(String.valueOf(patient_pupil_array.get(i).getDocType()));
                    pupilLeftEyeArrayList.add(patient_pupil_array.get(i).getLeftEye());
                    pupilRightEyeArrayList.add(String.valueOf(patient_pupil_array.get(i).getRightEye()));
                    pupilUserIDArrayList.add(String.valueOf(patient_pupil_array.get(i).getUserId()));
                    pupilLoginTypeArrayList.add(String.valueOf(patient_pupil_array.get(i).getLoginType()));
                }

                String[] pupilID_array = new String[pupilIDArrayList.size()];
                String[] pupilName_array = new String[pupilNameArrayList.size()];
                String[] pupilDocID_array = new String[pupilDocIDArrayList.size()];
                String[] pupilDocType_array = new String[pupilDocTypeArrayList.size()];
                String[] pupilLeftEye_array = new String[pupilLeftEyeArrayList.size()];
                String[] pupilRightEye_array = new String[pupilRightEyeArrayList.size()];
                String[] pupilUserID_array = new String[pupilUserIDArrayList.size()];
                String[] pupilLoginType_array = new String[pupilLoginTypeArrayList.size()];

                if(pupilIDArrayList.size() > 0) {
                    for (int j = 0; j < pupilIDArrayList.size(); j++) {
                        pupilID_array[j] = pupilIDArrayList.get(j);
                        pupilName_array[j] = pupilNameArrayList.get(j);
                        pupilDocID_array[j]  = pupilDocIDArrayList.get(j);
                        pupilDocType_array[j]  = pupilDocTypeArrayList.get(j);
                        pupilLeftEye_array[j] = pupilLeftEyeArrayList.get(j);
                        pupilRightEye_array[j]  = pupilRightEyeArrayList.get(j);
                        pupilUserID_array[j]  = pupilUserIDArrayList.get(j);
                        pupilLoginType_array[j]  = pupilLoginTypeArrayList.get(j);
                        builder.addFormDataPart("pupil_id[]", pupilID_array[j].toString());
                        builder.addFormDataPart("pupil_name[]", pupilName_array[j].toString());
                        builder.addFormDataPart("pupil_docid[]", pupilDocID_array[j].toString());
                        builder.addFormDataPart("pupil_doctype[]", pupilDocType_array[j].toString());
                        builder.addFormDataPart("pupil_leftEye[]", pupilLeftEye_array[j].toString());
                        builder.addFormDataPart("pupil_rightEye[]", pupilRightEye_array[j].toString());
                        builder.addFormDataPart("pupil_userid[]", pupilUserID_array[j].toString());
                        builder.addFormDataPart("pupil_loginType[]", pupilLoginType_array[j].toString());
                    }
                }
            }

            if(patient_angle_array.size() > 0) {
                ArrayList<String> angleIDArrayList = new ArrayList<String>();
                ArrayList<String> angleNameArrayList = new ArrayList<String>();
                ArrayList<String> angleDocIDArrayList = new ArrayList<String>();
                ArrayList<String> angleDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> angleLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> angleRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> angleUserIDArrayList = new ArrayList<String>();
                ArrayList<String> angleLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_angle_array.size(); i++) {
                    angleIDArrayList.add(String.valueOf(patient_angle_array.get(i).getAngleId()));
                    angleNameArrayList.add(patient_angle_array.get(i).getAngleName().trim());
                    angleDocIDArrayList.add(String.valueOf(patient_angle_array.get(i).getDocId()));
                    angleDocTypeArrayList.add(String.valueOf(patient_angle_array.get(i).getDocType()));
                    angleLeftEyeArrayList.add(patient_angle_array.get(i).getLeftEye());
                    angleRightEyeArrayList.add(String.valueOf(patient_angle_array.get(i).getRightEye()));
                    angleUserIDArrayList.add(String.valueOf(patient_angle_array.get(i).getUserId()));
                    angleLoginTypeArrayList.add(String.valueOf(patient_angle_array.get(i).getLoginType()));
                }

                String[] angleID_array = new String[angleIDArrayList.size()];
                String[] angleName_array = new String[angleNameArrayList.size()];
                String[] angleDocID_array = new String[angleDocIDArrayList.size()];
                String[] angleDocType_array = new String[angleDocTypeArrayList.size()];
                String[] angleLeftEye_array = new String[angleLeftEyeArrayList.size()];
                String[] angleRightEye_array = new String[angleRightEyeArrayList.size()];
                String[] angleUserID_array = new String[angleUserIDArrayList.size()];
                String[] angleLoginType_array = new String[angleLoginTypeArrayList.size()];

                if(angleIDArrayList.size() > 0) {
                    for (int j = 0; j < angleIDArrayList.size(); j++) {
                        angleID_array[j] = angleIDArrayList.get(j);
                        angleName_array[j] = angleNameArrayList.get(j);
                        angleDocID_array[j]  = angleDocIDArrayList.get(j);
                        angleDocType_array[j]  = angleDocTypeArrayList.get(j);
                        angleLeftEye_array[j] = angleLeftEyeArrayList.get(j);
                        angleRightEye_array[j]  = angleRightEyeArrayList.get(j);
                        angleUserID_array[j]  = angleUserIDArrayList.get(j);
                        angleLoginType_array[j]  = angleLoginTypeArrayList.get(j);
                        builder.addFormDataPart("angle_id[]", angleID_array[j].toString());
                        builder.addFormDataPart("angle_name[]", angleName_array[j].toString());
                        builder.addFormDataPart("angle_docid[]", angleDocID_array[j].toString());
                        builder.addFormDataPart("angle_doctype[]", angleDocType_array[j].toString());
                        builder.addFormDataPart("angle_leftEye[]", angleLeftEye_array[j].toString());
                        builder.addFormDataPart("angle_rightEye[]", angleRightEye_array[j].toString());
                        builder.addFormDataPart("angle_userid[]", angleUserID_array[j].toString());
                        builder.addFormDataPart("angle_loginType[]", angleLoginType_array[j].toString());
                    }
                }
            }

            if(patient_lens_array.size() > 0) {
                ArrayList<String> lensIDArrayList = new ArrayList<String>();
                ArrayList<String> lensNameArrayList = new ArrayList<String>();
                ArrayList<String> lensDocIDArrayList = new ArrayList<String>();
                ArrayList<String> lensDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> lensLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> lensRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> lensUserIDArrayList = new ArrayList<String>();
                ArrayList<String> lensLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_lens_array.size(); i++) {
                    lensIDArrayList.add(String.valueOf(patient_lens_array.get(i).getLensId()));
                    lensNameArrayList.add(patient_lens_array.get(i).getLensName().trim());
                    lensDocIDArrayList.add(String.valueOf(patient_lens_array.get(i).getDocId()));
                    lensDocTypeArrayList.add(String.valueOf(patient_lens_array.get(i).getDocType()));
                    lensLeftEyeArrayList.add(patient_lens_array.get(i).getLeftEye());
                    lensRightEyeArrayList.add(String.valueOf(patient_lens_array.get(i).getRightEye()));
                    lensUserIDArrayList.add(String.valueOf(patient_lens_array.get(i).getUserId()));
                    lensLoginTypeArrayList.add(String.valueOf(patient_lens_array.get(i).getLoginType()));
                }

                String[] lensID_array = new String[lensIDArrayList.size()];
                String[] lensName_array = new String[lensNameArrayList.size()];
                String[] lensDocID_array = new String[lensDocIDArrayList.size()];
                String[] lensDocType_array = new String[lensDocTypeArrayList.size()];
                String[] lensLeftEye_array = new String[lensLeftEyeArrayList.size()];
                String[] lensRightEye_array = new String[lensRightEyeArrayList.size()];
                String[] lensUserID_array = new String[lensUserIDArrayList.size()];
                String[] lensLoginType_array = new String[lensLoginTypeArrayList.size()];

                if(lensIDArrayList.size() > 0) {
                    for (int j = 0; j < lensIDArrayList.size(); j++) {
                        lensID_array[j] = lensIDArrayList.get(j);
                        lensName_array[j] = lensNameArrayList.get(j);
                        lensDocID_array[j]  = lensDocIDArrayList.get(j);
                        lensDocType_array[j]  = lensDocTypeArrayList.get(j);
                        lensLeftEye_array[j] = lensLeftEyeArrayList.get(j);
                        lensRightEye_array[j]  = lensRightEyeArrayList.get(j);
                        lensUserID_array[j]  = lensUserIDArrayList.get(j);
                        lensLoginType_array[j]  = lensLoginTypeArrayList.get(j);
                        builder.addFormDataPart("lens_id[]", lensID_array[j].toString());
                        builder.addFormDataPart("lens_name[]", lensName_array[j].toString());
                        builder.addFormDataPart("lens_docid[]", lensDocID_array[j].toString());
                        builder.addFormDataPart("lens_doctype[]", lensDocType_array[j].toString());
                        builder.addFormDataPart("lens_leftEye[]", lensLeftEye_array[j].toString());
                        builder.addFormDataPart("lens_rightEye[]", lensRightEye_array[j].toString());
                        builder.addFormDataPart("lens_userid[]", lensUserID_array[j].toString());
                        builder.addFormDataPart("lens_loginType[]", lensLoginType_array[j].toString());
                    }
                }
            }

            if(patient_viterous_array.size() > 0) {
                ArrayList<String> viterousIDArrayList = new ArrayList<String>();
                ArrayList<String> viterousNameArrayList = new ArrayList<String>();
                ArrayList<String> viterousDocIDArrayList = new ArrayList<String>();
                ArrayList<String> viterousDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> viterousLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> viterousRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> viterousUserIDArrayList = new ArrayList<String>();
                ArrayList<String> viterousLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_viterous_array.size(); i++) {
                    viterousIDArrayList.add(String.valueOf(patient_viterous_array.get(i).getViterousId()));
                    viterousNameArrayList.add(patient_viterous_array.get(i).getViterousName().trim());
                    viterousDocIDArrayList.add(String.valueOf(patient_viterous_array.get(i).getDocId()));
                    viterousDocTypeArrayList.add(String.valueOf(patient_viterous_array.get(i).getDocType()));
                    viterousLeftEyeArrayList.add(patient_viterous_array.get(i).getLeftEye());
                    viterousRightEyeArrayList.add(String.valueOf(patient_viterous_array.get(i).getRightEye()));
                    viterousUserIDArrayList.add(String.valueOf(patient_viterous_array.get(i).getUserId()));
                    viterousLoginTypeArrayList.add(String.valueOf(patient_viterous_array.get(i).getLoginType()));
                }

                String[] viterousID_array = new String[viterousIDArrayList.size()];
                String[] viterousName_array = new String[viterousNameArrayList.size()];
                String[] viterousDocID_array = new String[viterousDocIDArrayList.size()];
                String[] viterousDocType_array = new String[viterousDocTypeArrayList.size()];
                String[] viterousLeftEye_array = new String[viterousLeftEyeArrayList.size()];
                String[] viterousRightEye_array = new String[viterousRightEyeArrayList.size()];
                String[] viterousUserID_array = new String[viterousUserIDArrayList.size()];
                String[] viterousLoginType_array = new String[viterousLoginTypeArrayList.size()];

                if(viterousIDArrayList.size() > 0) {
                    for (int j = 0; j < viterousIDArrayList.size(); j++) {
                        viterousID_array[j] = viterousIDArrayList.get(j);
                        viterousName_array[j] = viterousNameArrayList.get(j);
                        viterousDocID_array[j]  = viterousDocIDArrayList.get(j);
                        viterousDocType_array[j]  = viterousDocTypeArrayList.get(j);
                        viterousLeftEye_array[j] = viterousLeftEyeArrayList.get(j);
                        viterousRightEye_array[j]  = viterousRightEyeArrayList.get(j);
                        viterousUserID_array[j]  = viterousUserIDArrayList.get(j);
                        viterousLoginType_array[j]  = viterousLoginTypeArrayList.get(j);
                        builder.addFormDataPart("viterous_id[]", viterousID_array[j].toString());
                        builder.addFormDataPart("viterous_name[]", viterousName_array[j].toString());
                        builder.addFormDataPart("viterous_docid[]", viterousDocID_array[j].toString());
                        builder.addFormDataPart("viterous_doctype[]", viterousDocType_array[j].toString());
                        builder.addFormDataPart("viterous_leftEye[]", viterousLeftEye_array[j].toString());
                        builder.addFormDataPart("viterous_rightEye[]", viterousRightEye_array[j].toString());
                        builder.addFormDataPart("viterous_userid[]", viterousUserID_array[j].toString());
                        builder.addFormDataPart("viterous_loginType[]", viterousLoginType_array[j].toString());
                    }
                }
            }

            if(patient_fundus_array.size() > 0) {
                ArrayList<String> fundusIDArrayList = new ArrayList<String>();
                ArrayList<String> fundusNameArrayList = new ArrayList<String>();
                ArrayList<String> fundusDocIDArrayList = new ArrayList<String>();
                ArrayList<String> fundusDocTypeArrayList = new ArrayList<String>();
                ArrayList<String> fundusLeftEyeArrayList = new ArrayList<String>();
                ArrayList<String> fundusRightEyeArrayList = new ArrayList<String>();
                ArrayList<String> fundusUserIDArrayList = new ArrayList<String>();
                ArrayList<String> fundusLoginTypeArrayList = new ArrayList<String>();

                for(int i = 0; i < patient_fundus_array.size(); i++) {
                    fundusIDArrayList.add(String.valueOf(patient_fundus_array.get(i).getFundusId()));
                    fundusNameArrayList.add(patient_fundus_array.get(i).getFundusName().trim());
                    fundusDocIDArrayList.add(String.valueOf(patient_fundus_array.get(i).getDocId()));
                    fundusDocTypeArrayList.add(String.valueOf(patient_fundus_array.get(i).getDocType()));
                    fundusLeftEyeArrayList.add(patient_fundus_array.get(i).getLeftEye());
                    fundusRightEyeArrayList.add(String.valueOf(patient_fundus_array.get(i).getRightEye()));
                    fundusUserIDArrayList.add(String.valueOf(patient_fundus_array.get(i).getUserId()));
                    fundusLoginTypeArrayList.add(String.valueOf(patient_fundus_array.get(i).getLoginType()));
                }

                String[] fundusID_array = new String[fundusIDArrayList.size()];
                String[] fundusName_array = new String[fundusNameArrayList.size()];
                String[] fundusDocID_array = new String[fundusDocIDArrayList.size()];
                String[] fundusDocType_array = new String[fundusDocTypeArrayList.size()];
                String[] fundusLeftEye_array = new String[fundusLeftEyeArrayList.size()];
                String[] fundusRightEye_array = new String[fundusRightEyeArrayList.size()];
                String[] fundusUserID_array = new String[fundusUserIDArrayList.size()];
                String[] fundusLoginType_array = new String[fundusLoginTypeArrayList.size()];

                if(fundusIDArrayList.size() > 0) {
                    for (int j = 0; j < fundusIDArrayList.size(); j++) {
                        fundusID_array[j] = fundusIDArrayList.get(j);
                        fundusName_array[j] = fundusNameArrayList.get(j);
                        fundusDocID_array[j]  = fundusDocIDArrayList.get(j);
                        fundusDocType_array[j]  = fundusDocTypeArrayList.get(j);
                        fundusLeftEye_array[j] = fundusLeftEyeArrayList.get(j);
                        fundusRightEye_array[j]  = fundusRightEyeArrayList.get(j);
                        fundusUserID_array[j]  = fundusUserIDArrayList.get(j);
                        fundusLoginType_array[j]  = fundusLoginTypeArrayList.get(j);
                        builder.addFormDataPart("fundus_id[]", fundusID_array[j].toString());
                        builder.addFormDataPart("fundus_name[]", fundusName_array[j].toString());
                        builder.addFormDataPart("fundus_docid[]", fundusDocID_array[j].toString());
                        builder.addFormDataPart("fundus_doctype[]", fundusDocType_array[j].toString());
                        builder.addFormDataPart("fundus_leftEye[]", fundusLeftEye_array[j].toString());
                        builder.addFormDataPart("fundus_rightEye[]", fundusRightEye_array[j].toString());
                        builder.addFormDataPart("fundus_userid[]", fundusUserID_array[j].toString());
                        builder.addFormDataPart("fundus_loginType[]", fundusLoginType_array[j].toString());
                    }
                }
            }

            if(patient_investigation_array.size() >0) {

                ArrayList<String> investIDArrayList = new ArrayList<String>();
                ArrayList<String> testIDArrayList = new ArrayList<String>();
                ArrayList<String> grouptestIDArrayList = new ArrayList<String>();
                ArrayList<String> testNameArrayList = new ArrayList<String>();
                ArrayList<String> groupTestArrayList = new ArrayList<String>();
                ArrayList<String> mfRangeArrayList = new ArrayList<String>();
                ArrayList<String> normalRangeArrayList = new ArrayList<String>();
                ArrayList<String> actualRangeArrayList = new ArrayList<String>();
                ArrayList<String> rightEyeArrayList = new ArrayList<String>();
                ArrayList<String> leftEyeArrayList = new ArrayList<String>();
                ArrayList<String> departmentArrayList = new ArrayList<String>();

                for(int j = 0; j < patient_investigation_array.size(); j++) {
                    investIDArrayList.add(String.valueOf(patient_investigation_array.get(j).getInvestigationId()));
                    testIDArrayList.add(patient_investigation_array.get(j).getTestId());
                    grouptestIDArrayList.add(patient_investigation_array.get(j).getGroupTestId());
                    testNameArrayList.add(String.valueOf(patient_investigation_array.get(j).getTestName()));
                    groupTestArrayList.add(String.valueOf(patient_investigation_array.get(j).getGroupTest()));
                    mfRangeArrayList.add(patient_investigation_array.get(j).getMFRange());
                    normalRangeArrayList.add(String.valueOf(patient_investigation_array.get(j).getNormalRange()));
                    actualRangeArrayList.add(String.valueOf(patient_investigation_array.get(j).getTestActualValue()));
                    rightEyeArrayList.add(String.valueOf(patient_investigation_array.get(j).getRightEye()));
                    leftEyeArrayList.add(String.valueOf(patient_investigation_array.get(j).getLeftEye()));
                    departmentArrayList.add(String.valueOf(patient_investigation_array.get(j).getInvestDepartment()));
                }

                String[] investID_array = new String[investIDArrayList.size()];
                String[] testID_array = new String[testIDArrayList.size()];
                String[] grouptestID_array = new String[grouptestIDArrayList.size()];
                String[] testName_array = new String[testNameArrayList.size()];
                String[] groupTest_array = new String[groupTestArrayList.size()];
                String[] mfRange_array = new String[mfRangeArrayList.size()];
                String[] normalRange_array = new String[normalRangeArrayList.size()];
                String[] actualRange_array = new String[actualRangeArrayList.size()];
                String[] rightEyeRange_array = new String[rightEyeArrayList.size()];
                String[] leftEyeRange_array = new String[leftEyeArrayList.size()];
                String[] departmentRange_array = new String[departmentArrayList.size()];

                if(investIDArrayList.size() > 0) {
                    for (int k = 0; k < investIDArrayList.size(); k++) {
                        investID_array[k] = investIDArrayList.get(k);
                        testID_array[k] = testIDArrayList.get(k);
                        grouptestID_array[k] = grouptestIDArrayList.get(k);
                        testName_array[k]  = testNameArrayList.get(k);
                        groupTest_array[k]  = groupTestArrayList.get(k);
                        mfRange_array[k] = mfRangeArrayList.get(k);
                        normalRange_array[k]  = normalRangeArrayList.get(k);
                        actualRange_array[k]  = actualRangeArrayList.get(k);
                        rightEyeRange_array[k]  = rightEyeArrayList.get(k);
                        leftEyeRange_array[k]  = leftEyeArrayList.get(k);
                        departmentRange_array[k]  = departmentArrayList.get(k);
                        Log.d(Utils.TAG, " investID: "+investID_array[k].toString());
                        Log.d(Utils.TAG, " testID: "+testID_array[k].toString());
                        Log.d(Utils.TAG, " grouptestID: "+ grouptestID_array[k].toString());
                        builder.addFormDataPart("investigation_id[]", investID_array[k].toString());
                        builder.addFormDataPart("test_id[]", testID_array[k].toString());
                        builder.addFormDataPart("grouptest_id[]", grouptestID_array[k].toString());
                        builder.addFormDataPart("test_name[]", testName_array[k].toString());

                        if(groupTest_array[k] != null) {
                            builder.addFormDataPart("group_test[]", groupTest_array[k].toString());
                        }

                        if(mfRange_array[k] != null) {
                            builder.addFormDataPart("mfRange[]", mfRange_array[k].toString());
                        }

                        if(normalRange_array[k] != null) {
                            builder.addFormDataPart("normalRange[]", normalRange_array[k].toString());
                        }

                        if(actualRange_array[k] != null) {
                            builder.addFormDataPart("actualRange[]", actualRange_array[k].toString());
                        }

                        if(rightEyeRange_array[k] != null) {
                            builder.addFormDataPart("rightEyeRange[]", rightEyeRange_array[k].toString());
                        }

                        if(leftEyeRange_array[k] != null) {
                            builder.addFormDataPart("leftEyeRange[]", leftEyeRange_array[k].toString());
                        }

                        if(departmentRange_array[k] != null) {
                            builder.addFormDataPart("departmentRange[]", departmentRange_array[k].toString());
                        }
                    }
                }

            }

            if(patient_daignosis_array.size() > 0) {

                ArrayList<String> freqDiagnoIDArrayList = new ArrayList<String>();
                ArrayList<String> icdIDArrayList = new ArrayList<String>();
                ArrayList<String> icdNameArrayList = new ArrayList<String>();
                ArrayList<String> diagnoDocIDArrayList = new ArrayList<String>();
                ArrayList<String> diagnoDocTypeArrayList = new ArrayList<String>();

                for (int i = 0; i < patient_daignosis_array.size(); i++) {
                    freqDiagnoIDArrayList.add(String.valueOf(patient_daignosis_array.get(i).getDiagnoFreqId()));
                    icdIDArrayList.add(String.valueOf(patient_daignosis_array.get(i).getICDId()));
                    icdNameArrayList.add(String.valueOf(patient_daignosis_array.get(i).getICDName()));
                    diagnoDocIDArrayList.add(String.valueOf(patient_daignosis_array.get(i).getDocId()));
                    diagnoDocTypeArrayList.add(String.valueOf(patient_daignosis_array.get(i).getDocType()));
                }

                String[] freqDiagnoID_array = new String[freqDiagnoIDArrayList.size()];
                String[] icdID_array = new String[icdIDArrayList.size()];
                String[] icdName_array = new String[icdNameArrayList.size()];
                String[] diagnoDoc_array = new String[diagnoDocIDArrayList.size()];
                String[] diagnoDocType_array = new String[diagnoDocTypeArrayList.size()];

                if (freqDiagnoIDArrayList.size() > 0) {
                    for (int j = 0; j < freqDiagnoIDArrayList.size(); j++) {
                        freqDiagnoID_array[j] = freqDiagnoIDArrayList.get(j);
                        icdID_array[j] = icdIDArrayList.get(j);
                        icdName_array[j] = icdNameArrayList.get(j);
                        diagnoDoc_array[j] = diagnoDocIDArrayList.get(j);
                        diagnoDocType_array[j] = diagnoDocTypeArrayList.get(j);
                        builder.addFormDataPart("diagno_frequent_id[]", freqDiagnoID_array[j].toString());
                        builder.addFormDataPart("diagno_icdID[]", icdID_array[j].toString());
                        builder.addFormDataPart("diagno_icdName[]", icdName_array[j].toString());
                        builder.addFormDataPart("diagno_docID[]", diagnoDoc_array[j].toString());
                        builder.addFormDataPart("diagno_doctype[]", diagnoDocType_array[j].toString());
                    }
                }
            }

            if(patient_treatment_array.size() > 0 ) {
                ArrayList<String> treatmentIDArrayList = new ArrayList<String>();
                ArrayList<String> treatmentNameArrayList = new ArrayList<String>();
                ArrayList<String> treatmentDocIDArrayList = new ArrayList<String>();
                ArrayList<String> treatmentDocTypeArrayList = new ArrayList<String>();

                for (int i = 0; i < patient_treatment_array.size(); i++) {
                    treatmentIDArrayList.add(String.valueOf(patient_treatment_array.get(i).getTreatmentID()));
                    treatmentNameArrayList.add(patient_treatment_array.get(i).getTreatmentName().trim());
                    treatmentDocIDArrayList.add(String.valueOf(patient_treatment_array.get(i).getTreatmentDocID()));
                    treatmentDocTypeArrayList.add(String.valueOf(patient_treatment_array.get(i).getTreatmentDocType()));
                }

                String[] treatmentID_array = new String[treatmentIDArrayList.size()];
                String[] treatmentName_array = new String[treatmentNameArrayList.size()];
                String[] treatmentDocID_array = new String[treatmentDocIDArrayList.size()];
                String[] treatmentDocType_array = new String[treatmentDocTypeArrayList.size()];

                if (treatmentIDArrayList.size() > 0) {
                    for (int j = 0; j < treatmentIDArrayList.size(); j++) {
                        treatmentID_array[j] = treatmentIDArrayList.get(j);
                        treatmentName_array[j] = treatmentNameArrayList.get(j);
                        treatmentDocID_array[j] = treatmentDocIDArrayList.get(j);
                        treatmentDocType_array[j] = treatmentDocTypeArrayList.get(j);
                        builder.addFormDataPart("treatment_id[]", treatmentID_array[j].toString());
                        builder.addFormDataPart("treatment_name[]", treatmentName_array[j].toString());
                        builder.addFormDataPart("treatment_docid[]", treatmentDocID_array[j].toString());
                        builder.addFormDataPart("treatment_doctype[]", treatmentDocType_array[j].toString());
                    }
                }
            }

            if(patient_prescription_array.size() > 0 ) {

                ArrayList<String> freqPrescIDArrayList = new ArrayList<String>();
                ArrayList<String> prescriptionIDArrayList = new ArrayList<String>();
                ArrayList<String> tradeNameArrayList = new ArrayList<String>();
                ArrayList<String> genericIDArrayList = new ArrayList<String>();
                ArrayList<String> genericNameArrayList = new ArrayList<String>();
                ArrayList<String> dosageArrayList = new ArrayList<String>();
                ArrayList<String> timingsArrayList = new ArrayList<String>();
                ArrayList<String> durationArrayList = new ArrayList<String>();

                for (int i = 0; i < patient_prescription_array.size(); i++) {
                    freqPrescIDArrayList.add(String.valueOf(patient_prescription_array.get(i).getPrescFreqId()));
                    prescriptionIDArrayList.add(String.valueOf(patient_prescription_array.get(i).getPrescriptionId()));
                    tradeNameArrayList.add(String.valueOf(patient_prescription_array.get(i).getTradeName()));
                    genericIDArrayList.add(String.valueOf(patient_prescription_array.get(i).getGenericId()));
                    genericNameArrayList.add(patient_prescription_array.get(i).getGenericName().trim());
                    dosageArrayList.add(String.valueOf(patient_prescription_array.get(i).getDosage()));
                    timingsArrayList.add(String.valueOf(patient_prescription_array.get(i).getTimings()));
                    durationArrayList.add(String.valueOf(patient_prescription_array.get(i).getDuration()));
                }

                String[] freqPrescID_array = new String[freqPrescIDArrayList.size()];
                String[] prescriptionID_array = new String[prescriptionIDArrayList.size()];
                String[] tradeName_array = new String[tradeNameArrayList.size()];
                String[] genericID_array = new String[genericIDArrayList.size()];
                String[] genericName_array = new String[genericNameArrayList.size()];
                String[] dosage_array = new String[dosageArrayList.size()];
                String[] timings_array = new String[timingsArrayList.size()];
                String[] duration_array = new String[durationArrayList.size()];

                if (freqPrescIDArrayList.size() > 0) {
                    for (int j = 0; j < freqPrescIDArrayList.size(); j++) {
                        freqPrescID_array[j] = freqPrescIDArrayList.get(j);
                        prescriptionID_array[j] = prescriptionIDArrayList.get(j);
                        tradeName_array[j] = tradeNameArrayList.get(j);
                        genericID_array[j] = genericIDArrayList.get(j);
                        genericName_array[j] = genericNameArrayList.get(j);
                        dosage_array[j] = dosageArrayList.get(j);
                        timings_array[j] = timingsArrayList.get(j);
                        duration_array[j] = durationArrayList.get(j);
                        builder.addFormDataPart("prescription_frequent_id[]", freqPrescID_array[j].toString());
                        builder.addFormDataPart("prescription_ppID[]", prescriptionID_array[j].toString());
                        builder.addFormDataPart("prescription_tradeName[]", tradeName_array[j].toString());
                        builder.addFormDataPart("prescription_genericID[]", genericID_array[j].toString());
                        builder.addFormDataPart("prescription_genericName[]", genericName_array[j].toString());
                        builder.addFormDataPart("prescription_dosage[]", dosage_array[j].toString());
                        builder.addFormDataPart("prescription_timings[]", timings_array[j].toString());
                        builder.addFormDataPart("prescription_duration[]", duration_array[j].toString());
                    }
                }
            }

            if(visit_chiefMedComplaint_sufferings != null) {
                builder.addFormDataPart("chiefMedComplaint_sufferings", visit_chiefMedComplaint_sufferings);
            }

            if(patient_education != 0) {
                builder.addFormDataPart("patient_education", String.valueOf(patient_education));
            }

            if(investigation_template_name != null) {
                builder.addFormDataPart("investigation_template_name", investigation_template_name);
            }

            if(investigation_template_save != 0) {
                builder.addFormDataPart("investigation_template_save", String.valueOf(investigation_template_save));
            }
            else {
                builder.addFormDataPart("investigation_template_save", String.valueOf(investigation_template_save));
            }

            if(distanceVision_RE != null) {
                builder.addFormDataPart("slctDistVisionRE", distanceVision_RE);
            }

            if(distanceVision_LE != null) {
                builder.addFormDataPart("slctDistVisionLE", distanceVision_LE);
            }

            if(nearVision_RE != null) {
                builder.addFormDataPart("slctNearVisionRE", nearVision_RE);
            }

            if(nearVision_LE != null) {
                builder.addFormDataPart("slctNearVisionLE", nearVision_LE);
            }

            if(refraction_RE_top != null) {
                builder.addFormDataPart("se_refractionRE_value1", refraction_RE_top);
            }

            if(refraction_RE_bottom != null) {
                builder.addFormDataPart("se_refractionRE_value2", refraction_RE_bottom);
            }

            if(refraction_LE_top != null) {
                builder.addFormDataPart("se_refractionLE_value1", refraction_LE_top);
            }

            if(refraction_LE_bottom != null) {
                builder.addFormDataPart("se_refractionLE_value2", refraction_LE_bottom);
            }

            if(conultationFees != null) {
                builder.addFormDataPart("consultation_fees", conultationFees);
            }

            if(followupdate != null) {
                builder.addFormDataPart("followup_dates", followupdate);
            }

            if(diagnosis_details != null) {
                builder.addFormDataPart("diagnosis_details", diagnosis_details);
            }

            if(treatment_details != null) {
                builder.addFormDataPart("treatment_details", treatment_details);
            }

            if(prescription_note != null) {
                builder.addFormDataPart("prescription_note", prescription_note);
            }

            if(visit_entry_date != null) {
                builder.addFormDataPart("visit_entry_date", visit_entry_date);
            }

            if(get_dv_sphere_right != null) {
                builder.addFormDataPart("DvSpeherRE", get_dv_sphere_right);
            }

            if(get_dv_cyl_right != null) {
                builder.addFormDataPart("DvCylRE", get_dv_cyl_right);
            }

            if(get_dv_axis_right != null) {
                builder.addFormDataPart("DvAxisRE", get_dv_axis_right);
            }

            if(get_nv_sphere_right != null) {
                builder.addFormDataPart("NvSpeherRE", get_nv_sphere_right);
            }

            if(get_nv_cyl_right != null) {
                builder.addFormDataPart("NvCylRE", get_nv_cyl_right);
            }

            if(get_nv_axis_right != null) {
                builder.addFormDataPart("NvAxisRE", get_nv_axis_right);
            }

            if(get_ipd_right != null) {
                builder.addFormDataPart("IpdRE", get_ipd_right);
            }

            if(get_dv_sphere_left != null) {
                builder.addFormDataPart("DvSpeherLE", get_dv_sphere_left);
            }

            if(get_dv_cyl_left != null) {
                builder.addFormDataPart("DvCylLE", get_dv_cyl_left);
            }

            if(get_dv_axis_left != null) {
                builder.addFormDataPart("DvAxisLE", get_dv_axis_left);
            }

            if(get_nv_sphere_left != null) {
                builder.addFormDataPart("NvSpeherLE", get_nv_sphere_left);
            }

            if(get_nv_cyl_left != null) {
                builder.addFormDataPart("NvCylLE", get_nv_cyl_left);
            }

            if(get_nv_axis_left != null) {
                builder.addFormDataPart("NvAxisLE", get_nv_axis_left);
            }

            if(get_ipd_left != null) {
                builder.addFormDataPart("IpdLE", get_ipd_left);
            }

            if(distanceVision_Unaided_RE != null) {
                builder.addFormDataPart("slctDistVisionUnaidedRE", distanceVision_Unaided_RE);
            }

            if(distanceVision_Unaided_LE != null) {
                builder.addFormDataPart("slctDistVisionUnaidedLE", distanceVision_Unaided_LE);
            }

            if(NearVision_Unaided_RE != null) {
                builder.addFormDataPart("slctNearVisionUnaidedRE", NearVision_Unaided_RE);
            }

            if(NearVision_Unaided_LE != null) {
                builder.addFormDataPart("slctNearVisionUnaidedLE", NearVision_Unaided_LE);
            }

            if(iop_RE != null) {
                builder.addFormDataPart("slctIOP_RE", iop_RE);
            }

            if(iop_LE != null) {
                builder.addFormDataPart("slctIOP_LE", iop_LE);
            }


            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_PATIENT_EDIT_VISIT_OPHTHAL)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " editVisitOphthal: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }

        return null;
    }

    public static JSONObject updateEditProfile(String get_name, String get_mobile, String get_country_code,
                                               String get_country_name, String get_country_name_code,
                                               String get_state, String get_specialization_name, int get_spec_id,
                                               String get_city, String get_select_hospital,
                                               String get_qualification, String get_experience, String get_email,
                                               String get_website, String get_expertise, String get_contribution,
                                               String get_research, String get_publication, String get_imagepath,
                                               ArrayList<String> get_specialization_array,
                                               ArrayList<String> get_hospital_array, int user_id, String user_login_type,
                                               String get_mobile2, String get_online_opinion_cost, String get_consultation_fess,
                                               String secretary_phone, String secretary_email, String readdy_tele_op,
                                               String teleop_num, String ready_video_op, String video_op_num,
                                               String available_timings) {

        try {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);

            File file = new File(get_imagepath);
            if(file.exists()) {
                final MediaType MEDIA_TYPE = MediaType.parse("image/*");
                builder.addFormDataPart("txtPhoto",file.getName(),RequestBody.create(MEDIA_TYPE,file));
            }
            else {
                Log.d(Utils.TAG, "file not exist ");
            }

            ArrayList<String> specIDArrayList = new ArrayList<String>();
            if(get_specialization_array.size() > 0) {
                for (int i = 0; i < get_specialization_array.size(); i++) {
                    specIDArrayList.add(get_specialization_array.get(i).toString());
                }

                String[] specialization_array = new String[specIDArrayList.size()];
                for (int j = 0; j < specIDArrayList.size(); j++) {
                    specialization_array[j] = specIDArrayList.get(j);
                    builder.addFormDataPart("doc_specialization[]", specialization_array[j].toString());
                }
            }

            ArrayList<String> hospIDArrayList = new ArrayList<String>();
            if(get_hospital_array.size() > 0) {
                for (int i = 0; i < get_hospital_array.size(); i++) {
                    hospIDArrayList.add(get_hospital_array.get(i).toString());
                }

                String[] hospital_array = new String[hospIDArrayList.size()];
                for (int j = 0; j < hospIDArrayList.size(); j++) {
                    hospital_array[j] = hospIDArrayList.get(j);
                    builder.addFormDataPart("doc_hospital[]", hospital_array[j].toString());
                }
            }


            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart(APIClass.KEY_USERID, String.valueOf(user_id));
            builder.addFormDataPart(APIClass.KEY_LOGINTYPE, user_login_type);
            builder.addFormDataPart("txtDoc", get_name);
            builder.addFormDataPart("txtCountry", get_country_name);
            //  builder.addFormDataPart("txtCountryCode", get_country_code);
            //  builder.addFormDataPart("txtCountryNameCode", get_country_name_code);
            //  builder.addFormDataPart("slctSpecName", get_specialization_name);
            // builder.addFormDataPart("slctSpec", String.valueOf(get_spec_id));

            if(get_mobile != null){
                builder.addFormDataPart("txtMobile", get_mobile);
            }
            if(get_state != null) {
                builder.addFormDataPart("slctState", get_state);
            }
            if(get_city != null) {
                builder.addFormDataPart("txtCity", get_city);
            }
            if(get_select_hospital != null) {
                builder.addFormDataPart("selectHosp", get_select_hospital);
            }
            if(get_qualification != null) {
                builder.addFormDataPart("txtQual", get_qualification);
            }
            if(get_experience != null) {
                builder.addFormDataPart("txtExp", get_experience);
            }
            if(get_email != null) {
                builder.addFormDataPart("txtEmail", get_email);
            }
            if(get_website != null) {
                builder.addFormDataPart("txtWebsite", get_website);
            }
            if(get_expertise != null) {
                builder.addFormDataPart("txtInterest", get_expertise);
            }
            if(get_contribution != null) {
                builder.addFormDataPart("txtContribute", get_contribution);
            }
            if(get_research != null) {
                builder.addFormDataPart("txtResearch", get_research);
            }
            if(get_publication != null) {
                builder.addFormDataPart("txtPublication", get_publication);
            }

            if(get_mobile2 != null) {
                builder.addFormDataPart("txtMobile2", get_mobile2);
            }

            if(get_online_opinion_cost != null) {
                builder.addFormDataPart("txtOpinionCost", get_online_opinion_cost);
            }

            if(get_consultation_fess != null) {
                builder.addFormDataPart("txtConsultationFees", get_consultation_fess);
            }

            if(secretary_phone != null) {
                builder.addFormDataPart("txtSecretaryPhone", secretary_phone);
            }

            if(secretary_email != null) {
                builder.addFormDataPart("txtSecretaryEmail", secretary_email);
            }

            if(readdy_tele_op != null) {
                builder.addFormDataPart("txtReadyTeleOp", readdy_tele_op);
            }

            if(teleop_num != null) {
                builder.addFormDataPart("txtTeleOpNum", teleop_num);
            }

            if(ready_video_op != null) {
                builder.addFormDataPart("txtReadyVideoOp", ready_video_op);
            }

            if(video_op_num != null) {
                builder.addFormDataPart("txtVideoOpNum", video_op_num);
            }

            if(available_timings != null) {
                builder.addFormDataPart("txtAvailableTimings", available_timings);
            }


            RequestBody requestBody = builder.build();


            Request request = new Request.Builder()
                    .url(APIClass.DRS_EDIT_PROFILE_UPDATES)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, "edit: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject sendOpticalsReferal(String patient_id, String episode_id,
                                                 List<OpticalCentreList> selectedOpticalCentreArraylist,
                                                 int user_id, String user_login_type) {

        try {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            ArrayList<String> opticalsArrayList = new ArrayList<String>();
            for(int i = 0; i < selectedOpticalCentreArraylist.size(); i++) {
                opticalsArrayList.add(String.valueOf(selectedOpticalCentreArraylist.get(i).getOpticalId()));

            }
            String[] opticals_array = new String[opticalsArrayList.size()];
            if(opticalsArrayList.size() > 0) {
                for (int j = 0; j < opticalsArrayList.size(); j++) {
                    opticals_array[j] = opticalsArrayList.get(j);
                    builder.addFormDataPart("se_opticals_ID[]", opticals_array[j].toString());
                }
            }

            builder.addFormDataPart(APIClass.KEY_API_KEY, APIClass.API_KEY);
            builder.addFormDataPart("userid", String.valueOf(user_id));
            builder.addFormDataPart("login_type", user_login_type);
            builder.addFormDataPart("patient_id", String.valueOf(patient_id));
            builder.addFormDataPart("episode_id", String.valueOf(episode_id));

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(APIClass.DRS_OPTICALS_REFER)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.d(Utils.TAG, " refer: "+ res.toString());
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(Utils.TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(Utils.TAG, "Other Error: " + e.getLocalizedMessage());
        }


        return null;
    }
}
