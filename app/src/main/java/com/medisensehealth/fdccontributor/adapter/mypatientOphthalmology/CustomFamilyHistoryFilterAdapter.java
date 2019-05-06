package com.medisensehealth.fdccontributor.adapter.mypatientOphthalmology;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.medisensehealth.fdccontributor.DataModel.FamilyHistory;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by HP on 11-07-2018.
 */

public class CustomFamilyHistoryFilterAdapter  extends ArrayAdapter<FamilyHistory> {
    Context mContext;
    private List<FamilyHistory> animalNamesList = null;
    private ArrayList<FamilyHistory> arraylist;
    LayoutInflater inflater;

    public CustomFamilyHistoryFilterAdapter(Context context, int resourceId,
                                        List<FamilyHistory> items) {
        super(context, resourceId, items);
        this.mContext = context;
        this.animalNamesList = items;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<FamilyHistory>();
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
        FamilyHistory rowItem = getItem(position);

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

        holder.m_title.setText(rowItem.getFamilyHistoryName());


        return convertView;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        animalNamesList.clear();
        if (charText.length() == 0) {
            animalNamesList.addAll(arraylist);
        } else {
            for (FamilyHistory wp : arraylist) {
                if (wp.getFamilyHistoryName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    animalNamesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();

    }

}
