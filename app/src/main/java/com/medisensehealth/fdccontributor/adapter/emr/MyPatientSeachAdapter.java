package com.medisensehealth.fdccontributor.adapter.emr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.medisensehealth.fdccontributor.DataModel.MyPatientsList;
import com.medisensehealth.fdccontributor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 26-03-2018.
 */

public class MyPatientSeachAdapter extends ArrayAdapter<MyPatientsList> {
    Context context;
    int resource, textViewResourceId;
    List<MyPatientsList> items, tempItems, suggestions;

    public MyPatientSeachAdapter(Context context, int resource, int textViewResourceId, List<MyPatientsList> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<MyPatientsList>(items); // this makes the difference.
        suggestions = new ArrayList<MyPatientsList>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_search, parent, false);
        }
        MyPatientsList people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(people.getPatientName());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((MyPatientsList) resultValue).getPatientName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (MyPatientsList people : tempItems) {
                    if (people.getPatientName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<MyPatientsList> filterList = (ArrayList<MyPatientsList>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (MyPatientsList people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
