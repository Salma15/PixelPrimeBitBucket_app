package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by salma on 21/02/18.
 */

public class PhraseCollection {

    String trade_name;
    String gen_name;

    public PhraseCollection(String phrase, String meaning) {
        this.trade_name = phrase;
        this.gen_name = meaning;
    }

    public String getTradeName() {
        return trade_name;
    }
    public void setTradeName(String trade_name) {
        this.trade_name = trade_name;
    }

    public String getGenericName() {
        return gen_name;
    }
    public void setGenericName(String gen_name) {
        this.gen_name = gen_name;
    }

}
