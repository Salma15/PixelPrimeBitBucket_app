package com.medisensehealth.fdccontributor.DataModel;

/**
 * Created by lenovo on 03/02/2018.
 */

public class FundusImage {

    private String title;
    private int image;
    private String fundus_image;

    public FundusImage(String title,  int image) {
        this.title = title;
        this.image = image;
    }

    public FundusImage(int image) {
        this.image = image;
    }

    public FundusImage(String images) {
        this.fundus_image = images;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }
    public String getFundusImage() {
        return fundus_image;
    }
}
