package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 14/09/2017.
 */

public class EpisodesList implements Serializable {
    String episode_id, admin_id, episode_chiefMed, episode_Description, examination, treatment, followup_date;
    public ArrayList<String> GET_EPISODE_PHOTOS = new ArrayList<String>();

    String patient_id, special_instruct, episode_date;
    List<SelectedTestLists> INVESTIGATION_LIST = new ArrayList<>();
    List<DiagnosisTestLists> IDC_LIST = new ArrayList<>();
    ArrayList<String> REPORT_PHOTOS = new ArrayList<String>();
    List<PrescriptionList> PRESCRIPTION_LIST = new ArrayList<>();

    public EpisodesList(String datetime, List<PrescriptionList> get_precription_list, String chiefMedCom, String description, ArrayList<String> GET_EPISODE_PHOTOS) {
        this.episode_id = datetime;
        this.episode_chiefMed = chiefMedCom;
        this.episode_Description = description;
        this.GET_EPISODE_PHOTOS = GET_EPISODE_PHOTOS;
    }

    public EpisodesList(String episode_id, String patient_id, String episode_medical_complaint, String episode_desc, String episode_special_instruction, String date_time) {
        this.episode_id = episode_id;
        this.patient_id = patient_id;
        this.episode_chiefMed = episode_medical_complaint;
        this.episode_Description = episode_desc;
        this.special_instruct = episode_special_instruction;
        this.episode_date = date_time;
    }


    public EpisodesList(String episode_id, String admin_id, String patient_id, String episode_medical_complaint,
                        String examination, String treatment, String next_followup_date, String date_time,
                        List<SelectedTestLists> investigation_list, List<DiagnosisTestLists> idc_list,
                        List<PrescriptionList> prescription_list, ArrayList<String> report_photos) {
        this.episode_id = episode_id;
        this.admin_id = admin_id;
        this.patient_id = patient_id;
        this.episode_chiefMed = episode_medical_complaint;
        this.examination = examination;
        this.treatment = treatment;
        this.followup_date = next_followup_date;
        this.episode_date = date_time;
        this.INVESTIGATION_LIST = investigation_list;
        this.IDC_LIST = idc_list;
        this.PRESCRIPTION_LIST = prescription_list;
        this.REPORT_PHOTOS = report_photos;
    }

    public String getEpisodeUserID() { return admin_id; }
    public void setEpisodeUserID(String admin_id) { this.admin_id = admin_id;  }

    public  ArrayList<String> getReportsList() { return REPORT_PHOTOS; }
    public void setReportsList( ArrayList<String> REPORT_PHOTOS) { this.REPORT_PHOTOS = REPORT_PHOTOS;  }

    public  List<PrescriptionList> getPriscriptionList() { return PRESCRIPTION_LIST; }
    public void setPriscriptionList( List<PrescriptionList> PRESCRIPTION_LIST) { this.PRESCRIPTION_LIST = PRESCRIPTION_LIST;  }

    public  List<DiagnosisTestLists> getDiagnosticsList() { return IDC_LIST; }
    public void setDiagnosticsList( List<DiagnosisTestLists> IDC_LIST) { this.IDC_LIST = IDC_LIST;  }

    public  List<SelectedTestLists> getInvestigationList() { return INVESTIGATION_LIST; }
    public void setInvestigationList( List<SelectedTestLists> INVESTIGATION_LIST) { this.INVESTIGATION_LIST = INVESTIGATION_LIST;  }

    public String getEpisodeFollowupDate() { return followup_date; }
    public void setEpisodeFollowupDate(String followup_date) { this.followup_date = followup_date;  }

    public String getEpisodeTreatment() { return treatment; }
    public void setEpisodeTreatment(String treatment) { this.treatment = treatment;  }

    public String getEpisodeExamination() { return examination; }
    public void setEpisodeExamination(String examination) { this.examination = examination;  }

    public ArrayList<String>  getEpisodePhotos() { return GET_EPISODE_PHOTOS; }
    public void setEpisodePhotos(ArrayList<String>  GET_EPISODE_PHOTOS) { this.GET_EPISODE_PHOTOS = GET_EPISODE_PHOTOS;  }


    public String getEpisodeID() { return episode_id; }
    public void setEpisodeID(String episode_id) { this.episode_id = episode_id;  }

    public String getEpisodeChiefMedComplaint() { return episode_chiefMed; }
    public void setEpisodeChiefMedComplaint(String episode_chiefMed) { this.episode_chiefMed = episode_chiefMed;  }

    public String getEpisodeDescription() { return episode_Description; }
    public void setEpisodeDescription(String episode_Description) { this.episode_Description = episode_Description;  }

    public String getEpisodePatientID() { return patient_id; }
    public void setEpisodePatientID(String patient_id) { this.patient_id = patient_id;  }

    public String getEpisodeSpecialInstr() { return special_instruct; }
    public void setEpisodeSpecialInstr(String special_instruct) { this.special_instruct = special_instruct;  }

    public String getEpisodeDate() { return episode_date; }
    public void setEpisodeDate(String episode_date) { this.episode_date = episode_date;  }

}
