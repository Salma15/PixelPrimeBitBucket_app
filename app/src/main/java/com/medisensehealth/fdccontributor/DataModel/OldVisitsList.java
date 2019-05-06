package com.medisensehealth.fdccontributor.DataModel;

import java.util.List;

/**
 * Created by HP on 12-07-2018.
 */

public class OldVisitsList {

    int episode_id, admin_id, patient_id, patient_education;
    String followup_date,  diagnosis_details, treatment_details, episode_created_date, consultation_fees, prescription_note, ChiefMedComplaint_sufferings;
    List<ChiefMedicalComplaint> PATIENT_CHIEF_MEDCOMPLAINT_ARRAY;
    List<Investigations> PATIENT_INVESTIGATION_GENERAL_ARRAY, PATIENT_INVESTIGATION_OPHTHAL_ARRAY;
    List<Examinations> PATIENT_EXAMINATION_ARRAY;
    List<Diagnosis> PATIENT_DAIGNOSIS_ARRAY;
    List<Treatments> PATIENT_TREATMENT_ARRAY;
    List<FrequentPrescription> PATIENT_PRESCRIPTION_ARRAY;


    public OldVisitsList(int episode_id, int admin_id, int patient_id, String next_followup_date, String diagnosis_details,
                         String treatment_details, String prescription_note, String date_time, String consultation_fees,
                         int patient_education, String episode_medical_complaint,
                         List<ChiefMedicalComplaint> patient_chief_medcomplaint_array,
                         List<Investigations> patient_investigation_general_array,
                         List<Investigations> patient_investigation_ophthal_array, List<Examinations> patient_examination_array,
                         List<Diagnosis> patient_daignosis_array, List<Treatments> patient_treatment_array,
                         List<FrequentPrescription> patient_prescription_array) {

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
        this.PATIENT_EXAMINATION_ARRAY = patient_examination_array;
        this.PATIENT_DAIGNOSIS_ARRAY = patient_daignosis_array;
        this.PATIENT_TREATMENT_ARRAY = patient_treatment_array;
        this.PATIENT_PRESCRIPTION_ARRAY = patient_prescription_array;

    }

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

    public  List<Examinations> getExaminationList() { return PATIENT_EXAMINATION_ARRAY; }
    public void setExaminationList( List<Examinations> PATIENT_EXAMINATION_ARRAY) { this.PATIENT_EXAMINATION_ARRAY = PATIENT_EXAMINATION_ARRAY;  }

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
