package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by salma on 05/02/18.
 */

public class EpisodesOpthoList implements Serializable {
    String episode_id;
    List<PrescriptionList> GET_PRECRIPTION_LIST;
    public ArrayList<String> GET_EPISODES_PHOTOS = new ArrayList<String>();

    String GET_MEDICAL_COMPLAINT, GET_DIAGNOSYS, GET_TREATMENT;
    String GET_LIDS, GET_CONJUCTIVA, GET_SCLERA, GET_CORNEA, GET_AC, GET_IRIS, GET_LENS, GET_VITEROUS, GET_SPECIAL_INVESTIGATION;
    String GET_TENSION_RIGHT, GET_TENSION_LEFT, GET_LACRIMAL, GET_BP, GET_SUGAR;
    String GET_DV_SPHERE_RIGHT, GET_DV_CYL_RIGHT, GET_DV_AXIS_RIGHT;
    String GET_NV_SPHERE_RIGHT, GET_NV_CYL_RIGHT, GET_NV_AXIS_RIGHT;
    String GET_IPD_RIGHT;
    String GET_DV_SPHERE_LEFT, GET_DV_CYL_LEFT, GET_DV_AXIS_LEFT;
    String GET_NV_SPHERE_LEFT, GET_NV_CYL_LEFT, GET_NV_AXIS_LEFT;
    String GET_IPD_LEFT;
    String GET_BIFOCAL, GET_COLOUR, GET_REMARKS;

    String patient_id, special_instruct, episode_date;

    public EpisodesOpthoList(String datetime, List<PrescriptionList> get_precription_list, ArrayList<String> get_episodes_photos,
                             String get_medical_complaint, String get_diagnosys, String get_treatment, String get_lids,
                             String get_conjuctiva, String get_sclera, String get_cornea, String get_ac, String get_iris,
                             String get_lens, String get_viterous, String get_special_investigation, String get_tension_right,
                             String get_tension_left, String get_lacrimal, String get_bp, String get_sugar,
                             String get_dv_sphere_right, String get_dv_cyl_right, String get_dv_axis_right,
                             String get_nv_sphere_right, String get_nv_cyl_right, String get_nv_axis_right, String get_ipd_right,
                             String get_dv_sphere_left, String get_dv_cyl_left, String get_dv_axis_left, String get_nv_sphere_left,
                             String get_nv_cyl_left, String get_nv_axis_left, String get_ipd_left, String get_bifocal,
                             String get_colour, String get_remarks) {
        this.episode_id = datetime;
        this.GET_PRECRIPTION_LIST = get_precription_list;
        this.GET_EPISODES_PHOTOS = get_episodes_photos;
        this.GET_MEDICAL_COMPLAINT = get_medical_complaint;
        this.GET_DIAGNOSYS = get_diagnosys;
        this.GET_TREATMENT = get_treatment;
        this.GET_LIDS = get_lids;
        this.GET_CONJUCTIVA = get_conjuctiva;
        this.GET_SCLERA = get_sclera;
        this.GET_CORNEA = get_cornea;
        this.GET_AC = get_ac;
        this.GET_IRIS = get_iris;
        this.GET_LENS = get_lens;
        this.GET_VITEROUS = get_viterous;
        this.GET_SPECIAL_INVESTIGATION = get_special_investigation;
        this.GET_TENSION_RIGHT = get_tension_right;
        this.GET_TENSION_LEFT = get_tension_left;
        this.GET_LACRIMAL = get_lacrimal;
        this.GET_BP = get_bp;
        this.GET_SUGAR = get_sugar;
        this.GET_DV_SPHERE_RIGHT = get_dv_sphere_right;
        this.GET_DV_CYL_RIGHT = get_dv_cyl_right;
        this.GET_DV_AXIS_RIGHT = get_dv_axis_right;
        this.GET_NV_SPHERE_RIGHT = get_nv_sphere_right;
        this.GET_NV_CYL_RIGHT = get_nv_cyl_right;
        this.GET_NV_AXIS_RIGHT = get_nv_axis_right;
        this.GET_IPD_RIGHT = get_ipd_right;
        this.GET_DV_SPHERE_LEFT = get_dv_sphere_left;
        this.GET_DV_CYL_LEFT = get_dv_cyl_left;
        this.GET_DV_AXIS_LEFT = get_dv_axis_left;
        this.GET_NV_SPHERE_LEFT = get_nv_sphere_left;
        this.GET_NV_CYL_LEFT = get_nv_cyl_left;
        this.GET_NV_AXIS_LEFT = get_nv_axis_left;
        this.GET_IPD_LEFT = get_ipd_left;
        this.GET_BIFOCAL = get_bifocal;
        this.GET_COLOUR = get_colour;
        this.GET_REMARKS = get_remarks;
    }

    public EpisodesOpthoList(String episode_id, String patient_id, String episode_medical_complaint,
                             String episode_diagnosys, String episode_treatment, String episode_lids,
                             String episode_conjuctiva, String episode_sclera, String episode_cornea,
                             String episode_ac, String episode_iris, String episode_lens, String episode_viterous,
                             String episode_spec_investigation, String episode_tension_right,
                             String episode_tension_left, String episode_lacrimal, String episode_bp,
                             String episode_sugar, String episode_dvsphere_right, String episode_dvcyl_right,
                             String episode_dvaxis_right, String episode_nvsphere_right, String episode_nvcyl_right,
                             String episode_nvaxis_right, String episode_ipd_right, String episode_dvsphere_left,
                             String episode_dvcyl_left, String episode_dvaxis_left, String episode_nvsphere_left,
                             String episode_nvcyl_left, String episode_nvaxis_left, String episode_ipd_left,
                             String episode_bifocal, String episode_colour, String episode_remarks, String date_time) {
        this.episode_id = episode_id;
        this.patient_id = patient_id;
        this.episode_date = date_time;
        this.GET_MEDICAL_COMPLAINT = episode_medical_complaint;
        this.GET_DIAGNOSYS = episode_diagnosys;
        this.GET_TREATMENT = episode_treatment;
        this.GET_LIDS = episode_lids;
        this.GET_CONJUCTIVA = episode_conjuctiva;
        this.GET_SCLERA = episode_sclera;
        this.GET_CORNEA = episode_cornea;
        this.GET_AC = episode_ac;
        this.GET_IRIS = episode_iris;
        this.GET_LENS = episode_lens;
        this.GET_VITEROUS = episode_viterous;
        this.GET_SPECIAL_INVESTIGATION = episode_spec_investigation;
        this.GET_TENSION_RIGHT = episode_tension_right;
        this.GET_TENSION_LEFT = episode_tension_left;
        this.GET_LACRIMAL = episode_lacrimal;
        this.GET_BP = episode_bp;
        this.GET_SUGAR = episode_sugar;
        this.GET_DV_SPHERE_RIGHT = episode_dvsphere_right;
        this.GET_DV_CYL_RIGHT = episode_dvcyl_right;
        this.GET_DV_AXIS_RIGHT = episode_dvaxis_right;
        this.GET_NV_SPHERE_RIGHT = episode_nvsphere_right;
        this.GET_NV_CYL_RIGHT = episode_nvcyl_right;
        this.GET_NV_AXIS_RIGHT = episode_nvaxis_right;
        this.GET_IPD_RIGHT = episode_ipd_right;
        this.GET_DV_SPHERE_LEFT = episode_dvsphere_left;
        this.GET_DV_CYL_LEFT = episode_dvcyl_left;
        this.GET_DV_AXIS_LEFT = episode_dvaxis_left;
        this.GET_NV_SPHERE_LEFT = episode_nvsphere_left;
        this.GET_NV_CYL_LEFT = episode_nvcyl_left;
        this.GET_NV_AXIS_LEFT = episode_nvaxis_left;
        this.GET_IPD_LEFT = episode_ipd_left;
        this.GET_BIFOCAL = episode_bifocal;
        this.GET_COLOUR = episode_colour;
        this.GET_REMARKS = episode_remarks;
    }


    public String getEpRemarks() { return GET_REMARKS; }
    public void setEpRemarks(String GET_REMARKS) { this.GET_REMARKS = GET_REMARKS;  }

    public String getEpColour() { return GET_COLOUR; }
    public void setEpColour(String GET_COLOUR) { this.GET_COLOUR = GET_COLOUR;  }

    public String getEpBifocal() { return GET_BIFOCAL; }
    public void setEpBifocal(String GET_BIFOCAL) { this.GET_BIFOCAL = GET_BIFOCAL;  }

    public String getEpIpdLeft() { return GET_IPD_LEFT; }
    public void setEpIpdLeft(String GET_IPD_LEFT) { this.GET_IPD_LEFT = GET_IPD_LEFT;  }

    public String getEpNVAxisLeft() { return GET_NV_AXIS_LEFT; }
    public void setEpNVAxisLeft(String GET_NV_AXIS_LEFT) { this.GET_NV_AXIS_LEFT = GET_NV_AXIS_LEFT;  }

    public String getEpNVCylLeft() { return GET_NV_CYL_LEFT; }
    public void setEpNVCylLeft(String GET_NV_CYL_LEFT) { this.GET_NV_CYL_LEFT = GET_NV_CYL_LEFT;  }

    public String getEpNVSphereLeft() { return GET_NV_SPHERE_LEFT; }
    public void setEpNVSphereLeft(String GET_NV_SPHERE_LEFT) { this.GET_NV_SPHERE_LEFT = GET_NV_SPHERE_LEFT;  }

    public String getEpDVAxisLeft() { return GET_DV_AXIS_LEFT; }
    public void setEpDVAxisLeft(String GET_DV_AXIS_LEFT) { this.GET_DV_AXIS_LEFT = GET_DV_AXIS_LEFT;  }

    public String getEpDVCylLeft() { return GET_DV_CYL_LEFT; }
    public void setEpDVCylLeft(String GET_DV_CYL_LEFT) { this.GET_DV_CYL_LEFT = GET_DV_CYL_LEFT;  }

    public String getEpDVSphereLeft() { return GET_DV_SPHERE_LEFT; }
    public void setEpDVSphereLeft(String GET_DV_SPHERE_LEFT) { this.GET_DV_SPHERE_LEFT = GET_DV_SPHERE_LEFT;  }

    public String getEpIpdRight() { return GET_IPD_RIGHT; }
    public void setEpIpdRight(String GET_IPD_RIGHT) { this.GET_IPD_RIGHT = GET_IPD_RIGHT;  }

    public String getEpNVAxisRight() { return GET_NV_AXIS_RIGHT; }
    public void setEpNVAxisRight(String GET_NV_AXIS_RIGHT) { this.GET_NV_AXIS_RIGHT = GET_NV_AXIS_RIGHT;  }

    public String getEpNVCylRight() { return GET_NV_CYL_RIGHT; }
    public void setEpNVCylRight(String GET_NV_CYL_RIGHT) { this.GET_NV_CYL_RIGHT = GET_NV_CYL_RIGHT;  }

    public String getEpNVSphereRight() { return GET_NV_SPHERE_RIGHT; }
    public void setEpNVSphereRight(String GET_NV_SPHERE_RIGHT) { this.GET_NV_SPHERE_RIGHT = GET_NV_SPHERE_RIGHT;  }

    public String getEpDVAxisRight() { return GET_NV_AXIS_RIGHT; }
    public void setEpDVAxisRight(String GET_NV_AXIS_RIGHT) { this.GET_NV_AXIS_RIGHT = GET_NV_AXIS_RIGHT;  }

    public String getEpDVCylRight() { return GET_DV_CYL_RIGHT; }
    public void setEpDVCylRight(String GET_DV_CYL_RIGHT) { this.GET_DV_CYL_RIGHT = GET_DV_CYL_RIGHT;  }

    public String getEpDVSphereRight() { return GET_DV_SPHERE_RIGHT; }
    public void setEpDVSphereRight(String GET_DV_SPHERE_RIGHT) { this.GET_DV_SPHERE_RIGHT = GET_DV_SPHERE_RIGHT;  }

    public String getEpSugar() { return GET_SUGAR; }
    public void setEpSugar(String GET_SUGAR) { this.GET_SUGAR = GET_SUGAR;  }

    public String getEpBp() { return GET_BP; }
    public void setEpBp(String GET_BP) { this.GET_BP = GET_BP;  }

    public String getEpLacrimal() { return GET_LACRIMAL; }
    public void setEpLacrimal(String GET_LACRIMAL) { this.GET_LACRIMAL = GET_LACRIMAL;  }

    public String getEpTensionLeft() { return GET_TENSION_LEFT; }
    public void setEpTensionLeft(String GET_TENSION_LEFT) { this.GET_TENSION_LEFT = GET_TENSION_LEFT;  }

    public String getEpTensionRight() { return GET_TENSION_RIGHT; }
    public void setEpTensionRight(String GET_TENSION_RIGHT) { this.GET_TENSION_RIGHT = GET_TENSION_RIGHT;  }

    public String getEpSpecialInvestigation() { return GET_SPECIAL_INVESTIGATION; }
    public void setEpSpecialInvestigation(String GET_SPECIAL_INVESTIGATION) { this.GET_SPECIAL_INVESTIGATION = GET_SPECIAL_INVESTIGATION;  }

    public String getEpViterous() { return GET_VITEROUS; }
    public void setEpViterous(String GET_VITEROUS) { this.GET_VITEROUS = GET_VITEROUS;  }

    public String getEpLens() { return GET_LENS; }
    public void setEpLens(String GET_LENS) { this.GET_LENS = GET_LENS;  }

    public String getEpIris() { return GET_IRIS; }
    public void setEpIris(String GET_IRIS) { this.GET_IRIS = GET_IRIS;  }

    public String getEpAc() { return GET_AC; }
    public void setEpAc(String GET_AC) { this.GET_AC = GET_AC;  }

    public String getEpCornea() { return GET_CORNEA; }
    public void setEpCornea(String GET_CORNEA) { this.GET_CORNEA = GET_CORNEA;  }

    public String getEpSclera() { return GET_SCLERA; }
    public void setEpSclera(String GET_SCLERA) { this.GET_SCLERA = GET_SCLERA;  }

    public String getEpConjuctiva() { return GET_CONJUCTIVA; }
    public void setEpConjuctiva(String GET_CONJUCTIVA) { this.GET_CONJUCTIVA = GET_CONJUCTIVA;  }

    public String getEpLids() { return GET_LIDS; }
    public void setEpLids(String GET_LIDS) { this.GET_LIDS = GET_LIDS;  }

    public String getEpTreatment() { return GET_TREATMENT; }
    public void setEpTreatment(String GET_TREATMENT) { this.GET_TREATMENT = GET_TREATMENT;  }

    public String getEpDiagnosys() { return GET_DIAGNOSYS; }
    public void setEpDiagnosys(String GET_DIAGNOSYS) { this.GET_DIAGNOSYS = GET_DIAGNOSYS;  }

    public String getEpisodeChiefMedComplaint() { return GET_MEDICAL_COMPLAINT; }
    public void setEpisodeChiefMedComplaint(String GET_MEDICAL_COMPLAINT) { this.GET_MEDICAL_COMPLAINT = GET_MEDICAL_COMPLAINT;  }

    public ArrayList<String>  getEpisodePhotos() { return GET_EPISODES_PHOTOS; }
    public void setEpisodePhotos(ArrayList<String> GET_EPISODES_PHOTOS) { this.GET_EPISODES_PHOTOS = GET_EPISODES_PHOTOS;  }


    public String getEpisodeID() { return episode_id; }
    public void setEpisodeID(String episode_id) { this.episode_id = episode_id;  }

    public  List<PrescriptionList> getPrescriptionList() { return GET_PRECRIPTION_LIST; }
    public void setPrescriptionList( List<PrescriptionList> GET_PRECRIPTION_LIST) { this.GET_PRECRIPTION_LIST = GET_PRECRIPTION_LIST;  }

    public String getEpisodePatientID() { return patient_id; }
    public void setEpisodePatientID(String patient_id) { this.patient_id = patient_id;  }

    public String getEpisodeSpecialInstr() { return special_instruct; }
    public void setEpisodeSpecialInstr(String special_instruct) { this.special_instruct = special_instruct;  }

    public String getEpisodeDate() { return episode_date; }
    public void setEpisodeDate(String episode_date) { this.episode_date = episode_date;  }

}
