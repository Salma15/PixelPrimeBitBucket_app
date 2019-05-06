package com.medisensehealth.fdccontributor.DataModel;

import java.util.List;

/**
 * Created by SALMA on 15-09-2018.
 */
public class OldVisitsOphthalList {

    int episode_id, admin_id, patient_id, patient_education;
    String followup_date,  diagnosis_details, treatment_details, episode_created_date, consultation_fees, prescription_note, ChiefMedComplaint_sufferings;
    List<ChiefMedicalComplaint> PATIENT_CHIEF_MEDCOMPLAINT_ARRAY;
    List<Investigations> PATIENT_INVESTIGATION_GENERAL_ARRAY, PATIENT_INVESTIGATION_OPHTHAL_ARRAY, PATIENT_INVESTIGATION_RADIOLOGY_ARRAY, PATIENT_INVESTIGATION_CLINICAL_ARRAY, PATIENT_INVESTIGATION_OTHER_ARRAY;
    List<Diagnosis> PATIENT_DAIGNOSIS_ARRAY;
    List<Treatments> PATIENT_TREATMENT_ARRAY;
    List<FrequentPrescription> PATIENT_PRESCRIPTION_ARRAY;
    List<Lids> PATIENT_LIDS_ARRAY;
    List<OphthalConjuctiva> PATIENT_CONJUCTIVA_ARRAY;
    List<OphthalSclera> PATIENT_SCLERA_ARRAY;
    List<OphthalCornearAnteriorSurface> PATIENT_CORNEA_ANTERIOR_ARRAY;
    List<OphthalCornearPosteriorSurface> PATIENT_CORNEA_POSTERIOR_ARRAY;
    List<OphthalAnteriorChamber> PATIENT_ANTERIOR_CHAMBER_ARRAY;
    List<OphthalIris> PATIENT_IRIS_ARRAY;
    List<OphthalPupil> PATIENT_PUPIL_ARRAY;
    List<OphthalAngleAnteriorChamber> PATIENT_ANGLE_ARRAY;
    List<OphthalLens> PATIENT_LENS_ARRAY;
    List<OphthalViterous> PATIENT_VITEROUS_ARRAY;
    List<OphthalFundus> PATIENT_FUNDUS_ARRAY;

    String GET_DV_SPHERE_RIGHT, GET_DV_CYCLE_RIGHT, GET_DV_AXIS_RIGHT;
    String GET_NV_SPHERE_RIGHT, GET_NV_CYCLE_RIGHT, GET_NV_AXIS_RIGHT;
    String GET_IPD_RIGHT;
    String GET_DV_SPHERE_LEFT, GET_DV_CYCLE_LEFT, GET_DV_AXIS_LEFT;
    String GET_NV_SPHERE_LEFT, GET_NV_CYCLE_LEFT, GET_NV_AXIS_LEFT;
    String GET_IPD_LEFT;
    String DISTANCE_VISION_RE, DISTANCE_VISION_LE, NEAR_VISION_RE, NEAR_VISION_LE;
    String REFRACTION_RE_VALUE1, REFRACTION_RE_VALUE2, REFRACTION_LE_VALUE1, REFRACTION_LE_VALUE2;
    String DISTANCE_VISION_UNAIDED_RE, DISTANCE_VISION_UNAIDED_LE, NEAR_VISION_UNAIDED_RE, NEAR_VISION_UNAIDED_LE, IOP_RE, IOP_LE;

    public OldVisitsOphthalList(int episode_id, int admin_id, int patient_id, String next_followup_date, String diagnosis_details,
                                String treatment_details, String prescription_note, String date_time, String consultation_fees,
                                int patient_education, String episode_medical_complaint,
                                List<ChiefMedicalComplaint> patient_chief_medcomplaint_array, List<Investigations> patient_investigation_general_array,
                                List<Investigations> patient_investigation_ophthal_array,
                                List<Investigations> patient_investigation_radiology_array,
                                List<Investigations> patient_investigation_clinical_array, List<Investigations> patient_investigation_other_array,
                                List<Diagnosis> patient_daignosis_array,
                                List<Treatments> patient_treatment_array, List<FrequentPrescription> patient_prescription_array,
                                List<Lids> patient_lids_array, List<OphthalConjuctiva> patient_conjuctiva_array, List<OphthalSclera> patient_sclera_array,
                                List<OphthalCornearAnteriorSurface> patient_cornea_anterior_array, List<OphthalCornearPosteriorSurface> patient_cornea_posterior_array,
                                List<OphthalAnteriorChamber> patient_anterior_chamber_array, List<OphthalIris> patient_iris_array,
                                List<OphthalPupil> patient_pupil_array, List<OphthalAngleAnteriorChamber> patient_angle_array,
                                List<OphthalLens> patient_lens_array, List<OphthalViterous> patient_viterous_array,
                                List<OphthalFundus> patient_fundus_array, String refractionRE_value1, String refractionRE_value2,
                                String refractionLE_value1, String refractionLE_value2, String distVisionRE, String distVisionLE,
                                String nearVisionRE, String nearVisionLE, String dvSphereRE, String dvCylRE, String dvAxisRE,
                                String dvSpeherLE, String dvCylLE, String dvAxisLE, String nvSpeherRE, String nvCylRE,
                                String nvAxisRE, String nvSpeherLE, String nvCylLE, String nvAxisLE, String ipdRE, String ipdLE,
                                String distVisionUnaidedRE,  String distVisionUnaidedLE,  String nearVisionUnaidedRE,
                                String nearVisionUnaidedLE, String iopRE, String iopLE) {
        this.episode_id = episode_id;
        this.admin_id = admin_id;
        this.patient_id = patient_id;
        this.followup_date = next_followup_date;
        this.diagnosis_details = diagnosis_details;
        this.treatment_details = treatment_details;
        this.prescription_note = prescription_note;
        this.episode_created_date = date_time;
        this.consultation_fees = consultation_fees;
        this.patient_education = patient_education;
        this.ChiefMedComplaint_sufferings = episode_medical_complaint;
        this.PATIENT_CHIEF_MEDCOMPLAINT_ARRAY = patient_chief_medcomplaint_array;
        this.PATIENT_INVESTIGATION_GENERAL_ARRAY = patient_investigation_general_array;
        this.PATIENT_INVESTIGATION_OPHTHAL_ARRAY = patient_investigation_ophthal_array;
        this.PATIENT_INVESTIGATION_RADIOLOGY_ARRAY = patient_investigation_radiology_array;
        this.PATIENT_INVESTIGATION_CLINICAL_ARRAY = patient_investigation_clinical_array;
        this.PATIENT_INVESTIGATION_OTHER_ARRAY = patient_investigation_other_array;
        this.PATIENT_DAIGNOSIS_ARRAY = patient_daignosis_array;
        this.PATIENT_TREATMENT_ARRAY = patient_treatment_array;
        this.PATIENT_PRESCRIPTION_ARRAY = patient_prescription_array;
        this.PATIENT_LIDS_ARRAY = patient_lids_array;
        this.PATIENT_CONJUCTIVA_ARRAY = patient_conjuctiva_array;
        this.PATIENT_SCLERA_ARRAY = patient_sclera_array;
        this.PATIENT_CORNEA_ANTERIOR_ARRAY = patient_cornea_anterior_array;
        this.PATIENT_CORNEA_POSTERIOR_ARRAY = patient_cornea_posterior_array;
        this.PATIENT_ANTERIOR_CHAMBER_ARRAY = patient_anterior_chamber_array;
        this.PATIENT_IRIS_ARRAY = patient_iris_array;
        this.PATIENT_PUPIL_ARRAY = patient_pupil_array;
        this.PATIENT_ANGLE_ARRAY = patient_angle_array;
        this.PATIENT_LENS_ARRAY = patient_lens_array;
        this.PATIENT_VITEROUS_ARRAY = patient_viterous_array;
        this.PATIENT_FUNDUS_ARRAY = patient_fundus_array;
        this.REFRACTION_RE_VALUE1 = refractionRE_value1;
        this.REFRACTION_RE_VALUE2 = refractionRE_value2;
        this.REFRACTION_LE_VALUE1 = refractionLE_value1;
        this.REFRACTION_LE_VALUE2 = refractionLE_value2;
        this.DISTANCE_VISION_RE = distVisionRE;
        this.DISTANCE_VISION_LE = distVisionLE;
        this.NEAR_VISION_RE = nearVisionRE;
        this.NEAR_VISION_LE = nearVisionLE;
        this.GET_DV_SPHERE_RIGHT = dvSphereRE;
        this.GET_DV_CYCLE_RIGHT = dvCylRE;
        this.GET_DV_AXIS_RIGHT = dvAxisRE;
        this.GET_NV_SPHERE_RIGHT = nvSpeherRE;
        this.GET_NV_CYCLE_RIGHT = nvCylRE;
        this.GET_NV_AXIS_RIGHT = nvAxisRE;
        this.GET_IPD_RIGHT = ipdRE;
        this.GET_DV_SPHERE_LEFT = dvSpeherLE;
        this.GET_DV_CYCLE_LEFT = dvCylLE;
        this.GET_DV_AXIS_LEFT = dvAxisLE;
        this.GET_NV_SPHERE_LEFT = nvSpeherLE;
        this.GET_NV_CYCLE_LEFT = nvCylLE;
        this.GET_NV_AXIS_LEFT = nvAxisLE;
        this.GET_IPD_LEFT = ipdLE;
        this.DISTANCE_VISION_UNAIDED_RE = distVisionUnaidedRE;
        this.DISTANCE_VISION_UNAIDED_LE = distVisionUnaidedLE;
        this.NEAR_VISION_UNAIDED_RE = nearVisionUnaidedRE;
        this.NEAR_VISION_UNAIDED_LE = nearVisionUnaidedLE;
        this.IOP_RE = iopRE;
        this.IOP_LE = iopLE;
    }

    public String getEpisodeIOPLE() { return IOP_LE; }
    public void getEpisodeIOPLE(String IOP_LE) { this.IOP_LE = IOP_LE;  }

    public String getEpisodeIOPRE() { return IOP_RE; }
    public void getEpisodeIOPRE(String IOP_RE) { this.IOP_RE = IOP_RE;  }

    public String getEpisodeNearVisionUnaidedLE() { return NEAR_VISION_UNAIDED_LE; }
    public void getEpisodeNearVisionUnaidedLE(String NEAR_VISION_UNAIDED_LE) { this.NEAR_VISION_UNAIDED_LE = NEAR_VISION_UNAIDED_LE;  }

    public String getEpisodeNearVisionUnaidedRE() { return NEAR_VISION_UNAIDED_RE; }
    public void setEpisodeNearVisionUnaidedRE(String NEAR_VISION_UNAIDED_RE) { this.NEAR_VISION_UNAIDED_RE = NEAR_VISION_UNAIDED_RE;  }

    public String getEpisodeDistanceVisionUnaidedLE() { return DISTANCE_VISION_UNAIDED_LE; }
    public void setEpisodeDistanceVisionUnaidedLE(String DISTANCE_VISION_UNAIDED_LE) { this.DISTANCE_VISION_UNAIDED_LE = DISTANCE_VISION_UNAIDED_LE;  }

    public String getEpisodeDistanceVisionUnaidedRE() { return DISTANCE_VISION_UNAIDED_RE; }
    public void setEpisodeDistanceVisionUnaidedRE(String DISTANCE_VISION_UNAIDED_RE) { this.DISTANCE_VISION_UNAIDED_RE = DISTANCE_VISION_UNAIDED_RE;  }

    public  List<OphthalFundus> getFundusList() { return PATIENT_FUNDUS_ARRAY; }
    public void setFundusList( List<OphthalFundus> PATIENT_FUNDUS_ARRAY) { this.PATIENT_FUNDUS_ARRAY = PATIENT_FUNDUS_ARRAY;  }

    public  List<OphthalViterous> getViterousList() { return PATIENT_VITEROUS_ARRAY; }
    public void setViterousList( List<OphthalViterous> PATIENT_VITEROUS_ARRAY) { this.PATIENT_VITEROUS_ARRAY = PATIENT_VITEROUS_ARRAY;  }

    public  List<OphthalLens> getLensList() { return PATIENT_LENS_ARRAY; }
    public void setLensList( List<OphthalLens> PATIENT_LENS_ARRAY) { this.PATIENT_LENS_ARRAY = PATIENT_LENS_ARRAY;  }

    public  List<OphthalAngleAnteriorChamber> getAngleList() { return PATIENT_ANGLE_ARRAY; }
    public void setAngleLList( List<OphthalAngleAnteriorChamber> PATIENT_ANGLE_ARRAY) { this.PATIENT_ANGLE_ARRAY = PATIENT_ANGLE_ARRAY;  }

    public  List<OphthalPupil> getPupilList() { return PATIENT_PUPIL_ARRAY; }
    public void setPupilLList( List<OphthalPupil> PATIENT_PUPIL_ARRAY) { this.PATIENT_PUPIL_ARRAY = PATIENT_PUPIL_ARRAY;  }

    public  List<OphthalIris> getIrisList() { return PATIENT_IRIS_ARRAY; }
    public void setIrisLList( List<OphthalIris> PATIENT_IRIS_ARRAY) { this.PATIENT_IRIS_ARRAY = PATIENT_IRIS_ARRAY;  }

    public  List<OphthalAnteriorChamber> getAnteriorChamberList() { return PATIENT_ANTERIOR_CHAMBER_ARRAY; }
    public void setAnteriorChamberList( List<OphthalAnteriorChamber> PATIENT_ANTERIOR_CHAMBER_ARRAY) { this.PATIENT_ANTERIOR_CHAMBER_ARRAY = PATIENT_ANTERIOR_CHAMBER_ARRAY;  }

    public  List<OphthalCornearPosteriorSurface> getCorneaPosteriorList() { return PATIENT_CORNEA_POSTERIOR_ARRAY; }
    public void setCorneaPosteriorList( List<OphthalCornearPosteriorSurface> PATIENT_CORNEA_POSTERIOR_ARRAY) { this.PATIENT_CORNEA_POSTERIOR_ARRAY = PATIENT_CORNEA_POSTERIOR_ARRAY;  }

    public  List<OphthalCornearAnteriorSurface> getCorneaAnteriorList() { return PATIENT_CORNEA_ANTERIOR_ARRAY; }
    public void setCorneaAnteriorList( List<OphthalCornearAnteriorSurface> PATIENT_CORNEA_ANTERIOR_ARRAY) { this.PATIENT_CORNEA_ANTERIOR_ARRAY = PATIENT_CORNEA_ANTERIOR_ARRAY;  }

    public  List<OphthalSclera> getScleraList() { return PATIENT_SCLERA_ARRAY; }
    public void setScleraList( List<OphthalSclera> PATIENT_SCLERA_ARRAY) { this.PATIENT_SCLERA_ARRAY = PATIENT_SCLERA_ARRAY;  }

    public  List<OphthalConjuctiva> getConjuctivaList() { return PATIENT_CONJUCTIVA_ARRAY; }
    public void setConjuctivaList( List<OphthalConjuctiva> PATIENT_CONJUCTIVA_ARRAY) { this.PATIENT_CONJUCTIVA_ARRAY = PATIENT_CONJUCTIVA_ARRAY;  }

    public  List<Lids> getLidsList() { return PATIENT_LIDS_ARRAY; }
    public void setLidsList( List<Lids> PATIENT_LIDS_ARRAY) { this.PATIENT_LIDS_ARRAY = PATIENT_LIDS_ARRAY;  }

    public String getEpisodeRefractionLE2() { return REFRACTION_LE_VALUE2; }
    public void setEpisodeRefractionLE2(String REFRACTION_LE_VALUE2) { this.REFRACTION_LE_VALUE2 = REFRACTION_LE_VALUE2;  }

    public String getEpisodeRefractionLE1() { return REFRACTION_LE_VALUE1; }
    public void setEpisodeRefractionLE1(String REFRACTION_LE_VALUE1) { this.REFRACTION_LE_VALUE1 = REFRACTION_LE_VALUE1;  }

    public String getEpisodeRefractionRE2() { return REFRACTION_RE_VALUE2; }
    public void setEpisodeRefractionRE2(String REFRACTION_RE_VALUE2) { this.REFRACTION_RE_VALUE2 = REFRACTION_RE_VALUE2;  }

    public String getEpisodeRefractionRE1() { return REFRACTION_RE_VALUE1; }
    public void setEpisodeRefractionRE1(String REFRACTION_RE_VALUE1) { this.REFRACTION_RE_VALUE1 = REFRACTION_RE_VALUE1;  }

    public String getEpisodeNearVisionLE() { return NEAR_VISION_LE; }
    public void setEpisodeNearVisionLE(String NEAR_VISION_LE) { this.NEAR_VISION_LE = NEAR_VISION_LE;  }

    public String getEpisodeNearVisionRE() { return NEAR_VISION_RE; }
    public void setEpisodeNearVisionRE(String NEAR_VISION_RE) { this.NEAR_VISION_RE = NEAR_VISION_RE;  }

    public String getEpisodeDistanceVisionLE() { return DISTANCE_VISION_LE; }
    public void setEpisodeDistanceVisionLE(String DISTANCE_VISION_LE) { this.DISTANCE_VISION_LE = DISTANCE_VISION_LE;  }

    public String getEpisodeDistanceVisionRE() { return DISTANCE_VISION_RE; }
    public void setEpisodeDistanceVisionRE(String DISTANCE_VISION_RE) { this.DISTANCE_VISION_RE = DISTANCE_VISION_RE;  }

    public String getEpisodeIPDLE() { return GET_IPD_LEFT; }
    public void setEpisodeIPDLE(String GET_IPD_LEFT) { this.GET_IPD_LEFT = GET_IPD_LEFT;  }

    public String getEpisodeNvAxisLE() { return GET_NV_AXIS_LEFT; }
    public void setEpisodeNvAxisLE(String GET_NV_AXIS_LEFT) { this.GET_NV_AXIS_LEFT = GET_NV_AXIS_LEFT;  }

    public String getEpisodeNvCycleLE() { return GET_NV_CYCLE_LEFT; }
    public void setEpisodeNvCycleLE(String GET_NV_CYCLE_LEFT) { this.GET_NV_CYCLE_LEFT = GET_NV_CYCLE_LEFT;  }

    public String getEpisodeNvSphereLE() { return GET_NV_SPHERE_LEFT; }
    public void setEpisodeNvSphereLE(String GET_NV_SPHERE_LEFT) { this.GET_NV_SPHERE_LEFT = GET_NV_SPHERE_LEFT;  }

    public String getEpisodeDvAxisLE() { return GET_DV_AXIS_LEFT; }
    public void setEpisodeDvAxisLE(String GET_DV_AXIS_LEFT) { this.GET_DV_AXIS_LEFT = GET_DV_AXIS_LEFT;  }

    public String getEpisodeDvCycleLE() { return GET_DV_CYCLE_LEFT; }
    public void setEpisodeDvCycleLE(String GET_DV_CYCLE_LEFT) { this.GET_DV_CYCLE_LEFT = GET_DV_CYCLE_LEFT;  }

    public String getEpisodeDvSphereLE() { return GET_DV_SPHERE_LEFT; }
    public void setEpisodeDvSphereLE(String GET_DV_SPHERE_LEFT) { this.GET_DV_SPHERE_LEFT = GET_DV_SPHERE_LEFT;  }

    public String getEpisodeIPDRE() { return GET_IPD_RIGHT; }
    public void setEpisodeIPDRE(String GET_IPD_RIGHT) { this.GET_IPD_RIGHT = GET_IPD_RIGHT;  }

    public String getEpisodeNvAxisRE() { return GET_NV_AXIS_RIGHT; }
    public void setEpisodeNvAxisRE(String GET_NV_AXIS_RIGHT) { this.GET_NV_AXIS_RIGHT = GET_NV_AXIS_RIGHT;  }

    public String getEpisodeNvCycleRE() { return GET_NV_CYCLE_RIGHT; }
    public void setEpisodeNvCycleRE(String GET_NV_CYCLE_RIGHT) { this.GET_NV_CYCLE_RIGHT = GET_NV_CYCLE_RIGHT;  }

    public String getEpisodeNvSphereRE() { return GET_NV_SPHERE_RIGHT; }
    public void setEpisodeNvSphereRE(String GET_NV_SPHERE_RIGHT) { this.GET_NV_SPHERE_RIGHT = GET_NV_SPHERE_RIGHT;  }

    public String getEpisodeDvAxisRE() { return GET_DV_AXIS_RIGHT; }
    public void setEpisodeDvAxisRE(String GET_DV_AXIS_RIGHT) { this.GET_DV_AXIS_RIGHT = GET_DV_AXIS_RIGHT;  }

    public String getEpisodeDvCycleRE() { return GET_DV_CYCLE_RIGHT; }
    public void setEpisodeDvCycleRE(String GET_DV_CYCLE_RIGHT) { this.GET_DV_CYCLE_RIGHT = GET_DV_CYCLE_RIGHT;  }

    public String getEpisodeDvSphereRE() { return GET_DV_SPHERE_RIGHT; }
    public void setEpisodeDvSphereRE(String GET_DV_SPHERE_RIGHT) { this.GET_DV_SPHERE_RIGHT = GET_DV_SPHERE_RIGHT;  }

    public int getEpisodeID() { return episode_id; }
    public void setEpisodeID(int episode_id) { this.episode_id = episode_id;  }

    public int getEpisodeUserID() { return admin_id; }
    public void setEpisodeUserID(int admin_id) { this.admin_id = admin_id;  }

    public int getEpisodePatientID() { return patient_id; }
    public void setEpisodePatientID(int patient_id) { this.patient_id = patient_id;  }

    public String getEpisodeFollowupDate() { return followup_date; }
    public void setEpisodeFollowupDate(String followup_date) { this.followup_date = followup_date;  }

    public String getEpisodeDiagnosisDetails() { return diagnosis_details; }
    public void setEpisodeDiagnosisDetails(String diagnosis_details) { this.diagnosis_details = diagnosis_details;  }

    public String getEpisodeTreatmentDetails() { return treatment_details; }
    public void setEpisodeTreatmentDetails(String treatment_details) { this.treatment_details = treatment_details;  }

    public String getEpisodeCreatedDate() { return episode_created_date; }
    public void setEpisodeCreatedDate(String episode_created_date) { this.episode_created_date = episode_created_date;  }

    public String getEpisodeConsultaionFees() { return consultation_fees; }
    public void setEpisodeConsultaionFees(String consultation_fees) { this.consultation_fees = consultation_fees;  }

    public  List<ChiefMedicalComplaint> getChiefMedicalList() { return PATIENT_CHIEF_MEDCOMPLAINT_ARRAY; }
    public void setChiefMedicalList( List<ChiefMedicalComplaint> PATIENT_CHIEF_MEDCOMPLAINT_ARRAY) { this.PATIENT_CHIEF_MEDCOMPLAINT_ARRAY = PATIENT_CHIEF_MEDCOMPLAINT_ARRAY;  }

    public  List<Investigations> getInvestigationGeneralList() { return PATIENT_INVESTIGATION_GENERAL_ARRAY; }
    public void setInvestigationGeneralList( List<Investigations> PATIENT_INVESTIGATION_GENERAL_ARRAY) { this.PATIENT_INVESTIGATION_GENERAL_ARRAY = PATIENT_INVESTIGATION_GENERAL_ARRAY;  }

    public  List<Investigations> getInvestigationOphthalList() { return PATIENT_INVESTIGATION_OPHTHAL_ARRAY; }
    public void setInvestigationOphthalList( List<Investigations> PATIENT_INVESTIGATION_OPHTHAL_ARRAY) { this.PATIENT_INVESTIGATION_OPHTHAL_ARRAY = PATIENT_INVESTIGATION_OPHTHAL_ARRAY;  }

    public  List<Investigations> getInvestigationRadiologyList() { return PATIENT_INVESTIGATION_RADIOLOGY_ARRAY; }
    public void setInvestigationRadiologyList( List<Investigations> PATIENT_INVESTIGATION_RADIOLOGY_ARRAY) { this.PATIENT_INVESTIGATION_RADIOLOGY_ARRAY = PATIENT_INVESTIGATION_RADIOLOGY_ARRAY;  }

    public  List<Investigations> getInvestigationClinicalList() { return PATIENT_INVESTIGATION_CLINICAL_ARRAY; }
    public void setInvestigationClinicalList( List<Investigations> PATIENT_INVESTIGATION_CLINICAL_ARRAY) { this.PATIENT_INVESTIGATION_CLINICAL_ARRAY = PATIENT_INVESTIGATION_CLINICAL_ARRAY;  }

    public  List<Investigations> getInvestigationOtherList() { return PATIENT_INVESTIGATION_OTHER_ARRAY; }
    public void setInvestigationOtherList( List<Investigations> PATIENT_INVESTIGATION_OTHER_ARRAY) { this.PATIENT_INVESTIGATION_OTHER_ARRAY = PATIENT_INVESTIGATION_OTHER_ARRAY;  }

    public  List<Diagnosis> getDiagnosisList() { return PATIENT_DAIGNOSIS_ARRAY; }
    public void setDiagnosisList( List<Diagnosis> PATIENT_DAIGNOSIS_ARRAY) { this.PATIENT_DAIGNOSIS_ARRAY = PATIENT_DAIGNOSIS_ARRAY;  }

    public  List<Treatments> getTreatmentList() { return PATIENT_TREATMENT_ARRAY; }
    public void setTreatmentList( List<Treatments> PATIENT_TREATMENT_ARRAY) { this.PATIENT_TREATMENT_ARRAY = PATIENT_TREATMENT_ARRAY;  }

    public  List<FrequentPrescription> getPrescriptionList() { return PATIENT_PRESCRIPTION_ARRAY; }
    public void setPrescriptionList( List<FrequentPrescription> PATIENT_PRESCRIPTION_ARRAY) { this.PATIENT_PRESCRIPTION_ARRAY = PATIENT_PRESCRIPTION_ARRAY;  }

    public String getEpisodePrescriptionNotes() { return prescription_note; }
    public void setEpisodePrescriptionNotes(String prescription_note) { this.prescription_note = prescription_note;  }

    public int getEpisodePatientEducationID() { return patient_education; }
    public void setEpisodePatientEducationID(int patient_education) { this.patient_education = patient_education;  }

    public String getEpisodeChiefMedComplaintSufferings() { return ChiefMedComplaint_sufferings; }
    public void setEpisodeChiefMedComplaintSufferings(String ChiefMedComplaint_sufferings) { this.ChiefMedComplaint_sufferings = ChiefMedComplaint_sufferings;  }

}
