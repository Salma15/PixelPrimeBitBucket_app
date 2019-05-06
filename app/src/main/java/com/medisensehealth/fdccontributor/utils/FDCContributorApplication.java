package com.medisensehealth.fdccontributor.utils;

import android.app.Application;

/**
 * Created by hp on 05/01/2018.
 */

public class FDCContributorApplication extends Application {
    private static FDCContributorApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static FDCContributorApplication getInstance(){
        return mInstance;
    }
}
