package com.medisensehealth.fdccontributor.adapter.doctors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.medisensehealth.fdccontributor.DataModel.SpecializationList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewBold;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by lenovo on 08/09/2017.
 */
public class CustomSpecFilterAdapter extends ArrayAdapter<SpecializationList> {

    Context mContext;
    private List<SpecializationList> animalNamesList = null;
    private ArrayList<SpecializationList> arraylist;
    LayoutInflater inflater;

    public CustomSpecFilterAdapter(Context context, int resourceId,
                                 List<SpecializationList> items) {
        super(context, resourceId, items);
        this.mContext = context;
        this.animalNamesList = items;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SpecializationList>();
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
        SpecializationList rowItem = getItem(position);

      //  LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
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

        holder.m_title.setText(rowItem.getSpecializationName());

        String test = rowItem.getSpecializationName().trim();
        String spec_image_text = String.valueOf(test.charAt(0));
        holder.m_pic.setText(spec_image_text);

        return convertView;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        animalNamesList.clear();
        if (charText.length() == 0) {
            animalNamesList.addAll(arraylist);
        } else {
            for (SpecializationList wp : arraylist) {
                if (wp.getSpecializationName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    animalNamesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();

    }

}
