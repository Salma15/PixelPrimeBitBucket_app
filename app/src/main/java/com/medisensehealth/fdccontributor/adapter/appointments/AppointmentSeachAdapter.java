package com.medisensehealth.fdccontributor.adapter.appointments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.medisensehealth.fdccontributor.DataModel.AppointmentList;
import com.medisensehealth.fdccontributor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by medisense on 29/01/18.
 */

public class AppointmentSeachAdapter extends ArrayAdapter<AppointmentList> {
    Context context;
    int resource, textViewResourceId;
    List<AppointmentList> items, tempItems, suggestions;

    public AppointmentSeachAdapter(Context context, int resource, int textViewResourceId, List<AppointmentList> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<AppointmentList>(items); // this makes the difference.
        suggestions = new ArrayList<AppointmentList>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_search, parent, false);
        }
        AppointmentList people = items.get(position);
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
            String str = ((AppointmentList) resultValue).getPatientName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (AppointmentList people : tempItems) {
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
            List<AppointmentList> filterList = (ArrayList<AppointmentList>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (AppointmentList people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
