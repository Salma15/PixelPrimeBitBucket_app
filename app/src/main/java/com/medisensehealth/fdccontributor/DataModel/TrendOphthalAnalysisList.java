package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by SALMA on 17-09-2018.
 */
public class TrendOphthalAnalysisList {

    int trend_id, user_id, patient_id;
    String trend_date, patient_type, loginType, DvSphereRE, DvCylRE, DvAxisRE, DvSpeherLE, DvCylLE, DvAxisLE, NvSpeherRE, NvCylRE, 	NvAxisRE,
            NvSpeherLE, NvCylLE, 	NvAxisLE, 	IpdRE, IpdLE;

    public TrendOphthalAnalysisList(int trend_id, String date_added, String dvSphereRE, String dvCylRE,
                                    String dvAxisRE, String dvSpeherLE, String dvCylLE, String dvAxisLE, String nvSpeherRE,
                                    String nvCylRE, String nvAxisRE, String nvSpeherLE, String nvCylLE, String nvAxisLE,
                                    String ipdRE, String ipdLE, int patient_id, String patient_type, int user_id, String user_login_type) {
        this.trend_id = trend_id;
        this.trend_date = date_added;
        this.DvSphereRE = dvSphereRE;
        this.DvCylRE = dvCylRE;
        this.DvAxisRE = dvAxisRE;
        this.DvSpeherLE = dvSpeherLE;
        this.DvCylLE = dvCylLE;
        this.DvAxisLE = dvAxisLE;
        this.NvSpeherRE = nvSpeherRE;
        this.NvCylRE = nvCylRE;
        this.NvAxisRE = nvAxisRE;
        this.NvSpeherLE = nvSpeherLE;
        this.NvCylLE = nvCylLE;
        this.NvAxisLE = nvAxisLE;
        this.IpdRE = ipdRE;
        this.IpdLE = ipdLE;
        this.patient_id = patient_id;
        this.patient_type = patient_type;
        this.user_id = user_id;
        this.loginType = user_login_type;
    }

    public String getIpdLE() {
        return IpdLE;
    }
    public void setIpdLE(String IpdLE) {
        this.IpdLE = IpdLE;
    }

    public String getIpdRE() {
        return IpdRE;
    }
    public void setIpdRE(String IpdRE) {
        this.IpdRE = IpdRE;
    }

    public String getNvAxisLE() {
        return NvAxisLE;
    }
    public void setNvAxisLE(String NvAxisLE) {
        this.NvAxisLE = NvAxisLE;
    }

    public String getNvCylLE() {
        return NvCylLE;
    }
    public void setNvCylLE(String NvCylLE) {
        this.NvCylLE = NvCylLE;
    }

    public String getNvSpeherLE() {
        return NvSpeherLE;
    }
    public void setNvSpeherLE(String NvSpeherLE) {
        this.NvSpeherLE = NvSpeherLE;
    }

    public String getNvAxisRE() {
        return NvAxisRE;
    }
    public void setNvAxisRE(String NvAxisRE) {
        this.NvAxisRE = NvAxisRE;
    }

    public String getNvCylRE() {
        return NvCylRE;
    }
    public void setNvCylRE(String NvCylRE) {
        this.NvCylRE = NvCylRE;
    }

    public String getNvSpeherRE() {
        return NvSpeherRE;
    }
    public void setNvSpeherRE(String NvSpeherRE) {
        this.NvSpeherRE = NvSpeherRE;
    }

    public String getDvAxisLE() {
        return DvAxisLE;
    }
    public void setDvAxisLE(String DvAxisLE) {
        this.DvAxisLE = DvAxisLE;
    }

    public String getDvCylLE() {
        return DvCylLE;
    }
    public void setDvCylLE(String DvCylLE) {
        this.DvCylLE = DvCylLE;
    }

    public String getDvSpeherLE() {
        return DvSpeherLE;
    }
    public void setDvSpeherLE(String DvSpeherLE) {
        this.DvSpeherLE = DvSpeherLE;
    }

    public String getDvAxisRE() {
        return DvAxisRE;
    }
    public void setDvAxisRE(String DvAxisRE) {
        this.DvAxisRE = DvAxisRE;
    }

    public String getDvCylRE() {
        return DvCylRE;
    }
    public void setDvCylRE(String DvCylRE) {
        this.DvCylRE = DvCylRE;
    }

    public String getDvSphereRE() {
        return DvSphereRE;
    }
    public void setDvSphereRE(String DvSphereRE) {
        this.DvSphereRE = DvSphereRE;
    }

    public String getLoginType() {
        return loginType;
    }
    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public int getUserId() {
        return user_id;
    }
    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getPatientType() {
        return patient_type;
    }
    public void setPatientType(String patient_type) {
        this.patient_type = patient_type;
    }

    public int getTrendID() {
        return trend_id;
    }
    public void setTrendID(int trend_id) {
        this.trend_id = trend_id;
    }

    public String getTrendDate() {
        return trend_date;
    }
    public void setTrendDate(String trend_date) {
        this.trend_date = trend_date;
    }


}
