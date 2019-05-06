package com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.medisensehealth.fdccontributor.DataModel.FrequentPrescription;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by HP on 04-06-2018.
 */

public class CustomPrescriptionFilterAdapter extends ArrayAdapter<FrequentPrescription> {

    Context mContext;
    private List<FrequentPrescription> animalNamesList = null;
    private ArrayList<FrequentPrescription> arraylist;
    LayoutInflater inflater;

    public CustomPrescriptionFilterAdapter(Context context, int resourceId,
                                        List<FrequentPrescription> items) {
        super(context, resourceId, items);
        this.mContext = context;
        this.animalNamesList = items;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<FrequentPrescription>();
        this.arraylist.addAll(animalNamesList);
    }

    /*private view holder class*/
    public class ViewHolder {
        CustomTextViewBold m_pic;
        CustomTextView m_title;
        LinearLayout m_layout;
    }

    public View getView(final int position, View convertView, ViewGroup parent)    {
        ViewHolder holder = null;
        FrequentPrescription rowItem = getItem(position);

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.specialization_filter_listitem, null);
            holder = new ViewHolder();
            holder.m_title = (CustomTextView) convertView.findViewById(R.id.m_textview);
            holder.m_pic = (CustomTextViewBold) convertView.findViewById(R.id.m_imageview);
            holder.m_layout = (LinearLayout)  convertView.findViewById(R.id.m_btn);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        holder.m_title.setText(rowItem.getTradeName());

        if(rowItem.getPrescPriority() == 1) {
            holder.m_pic.setText("F");
         //   holder.m_pic.setBackgroundColor(mContext.getResources().getColor(R.color.status_green));
        }
        else {
            holder.m_pic.setText("O");
        //    holder.m_pic.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

//        String test = rowItem.getTradeName().trim();
//        String spec_image_text = String.valueOf(test.charAt(0));
//        holder.m_pic.setText(spec_image_text);

        return convertView;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        animalNamesList.clear();
        if (charText.length() == 0) {
            animalNamesList.addAll(arraylist);
        } else {
            for (FrequentPrescription wp : arraylist) {
                if (wp.getTradeName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    animalNamesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();

    }
}
