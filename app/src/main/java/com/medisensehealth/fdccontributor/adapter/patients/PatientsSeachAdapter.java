package com.medisensehealth.fdccontributor.adapter.patients;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.medisensehealth.fdccontributor.DataModel.PatientsList;
import com.medisensehealth.fdccontributor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by medisense on 30/01/18.
 */

public class PatientsSeachAdapter  extends ArrayAdapter<PatientsList> {
    Context context;
    int resource, textViewResourceId;
    List<PatientsList> items, tempItems, suggestions;

    public PatientsSeachAdapter(Context context, int resource, int textViewResourceId, List<PatientsList> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<PatientsList>(items); // this makes the difference.
        suggestions = new ArrayList<PatientsList>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_search, parent, false);
        }
        PatientsList people = items.get(position);
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
            String str = ((PatientsList) resultValue).getPatientName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (PatientsList people : tempItems) {
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
            List<PatientsList> filterList = (ArrayList<PatientsList>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (PatientsList people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
