package com.medisensehealth.fdccontributor.DataModel;

import java.io.Serializable;

/**
 * Created by lenovo on 14/09/2017.
 */

public class PrescriptionList implements Serializable {

    int episode_id;
    String trade_name, generic_name, dosage, route, frequency, instruction, sequence, presc_date;

    public PrescriptionList(String get_trade_name, String get_generic_name, String get_dosage, String get_route, String get_frequency,
                            String get_instruction) {
        this.trade_name = get_trade_name;
        this.generic_name = get_generic_name;
        this.dosage = get_dosage;
        this.route = get_route;
        this.frequency = get_frequency;
        this.instruction = get_instruction;
    }

    public int getPrescEpisodeID() {
        return episode_id;
    }
    public void setPrescEpisodeID(int episode_id) {
        this.episode_id = episode_id;
    }

    public String getPrescTradeName() {
        return trade_name;
    }
    public void setPrescTradeName(String trade_name) {
        this.trade_name = trade_name;
    }

    public String getPrescGenericName() {
        return generic_name;
    }
    public void setPrescGenericName(String generic_name) {
        this.generic_name = generic_name;
    }

    public String getPrescDosage() {
        return dosage;
    }
    public void setPrescDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getPrescRoute() {
        return route;
    }
    public void setPrescRoute(String route) {
        this.route = route;
    }

    public String getPrescFrequency() {
        return frequency;
    }
    public void setPrescFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getPrescInstruction() {
        return instruction;
    }
    public void setPrescInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getPrescSequency() {
        return sequence;
    }
    public void setPrescSequency(String sequence) {
        this.sequence = sequence;
    }

    public String getPrescDate() {
        return presc_date;
    }
    public void setPrescDate(String presc_date) {
        this.presc_date = presc_date;
    }

}
