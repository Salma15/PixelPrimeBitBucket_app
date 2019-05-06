package com.medisensehealth.fdccontributor.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.medisensehealth.fdccontributor.DataModel.FrequentPrescription;
import com.medisensehealth.fdccontributor.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HP on 03-04-2018.
 */

public class CustomMarkerView extends MarkerView {

    private TextView tvContent;
    private IAxisValueFormatter xAxisValueFormatter;
    List<FrequentPrescription> prescriptionListArraylist = new ArrayList<>();
    ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

    private DecimalFormat format;

    public CustomMarkerView(Context context, IAxisValueFormatter xAxisValueFormatter, List<FrequentPrescription> prescriptionListArraylist, ArrayList<ILineDataSet> dataSets) {
        super(context, R.layout.custom_marker_view_layout);

        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = (TextView) findViewById(R.id.tvContent);
        format = new DecimalFormat("###.0");
        this.prescriptionListArraylist = prescriptionListArraylist;
        this.dataSets = dataSets;
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        StringBuilder sb = new StringBuilder();
        if(prescriptionListArraylist.size() > 0) {
            Log.d(Utils.TAG, "size : " + prescriptionListArraylist.size());
            for(int i=0; i<prescriptionListArraylist.size();i++) {
                String[] status_date_arrray = prescriptionListArraylist.get(i).getPrescGivenDate().split(" ");
                Log.d(Utils.TAG, "inputDateStr : " +status_date_arrray[0]);
                Log.d(Utils.TAG, "dat : " + xAxisValueFormatter.getFormattedValue(e.getX(), null));
                if (status_date_arrray[0].equals( xAxisValueFormatter.getFormattedValue(e.getX(), null))) {
                    sb.append("\nName: "+prescriptionListArraylist.get(i).getTradeName());
                    sb.append("\nFrequency: "+prescriptionListArraylist.get(i).getDosage());
                    sb.append("\n");
                }
            }
        }

        String label = dataSets.get(highlight.getDataSetIndex()).getLabel(); //add this
        tvContent.setText("Date: " + xAxisValueFormatter.getFormattedValue(e.getX(), null) + " , "+label+": " + e.getY() + " \n\nPrescription List: \n"+sb);

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}