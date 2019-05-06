package com.medisensehealth.fdccontributor.adapter.feeds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.medisensehealth.fdccontributor.DataModel.BlogsList;
import com.medisensehealth.fdccontributor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by salma on 10/02/18.
 */

public class BlogSeachAdapter extends ArrayAdapter<BlogsList> {
    Context context;
    int resource, textViewResourceId;
    List<BlogsList> items, tempItems, suggestions;

    public BlogSeachAdapter(Context context, int resource, int textViewResourceId, List<BlogsList> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<BlogsList>(items); // this makes the difference.
        suggestions = new ArrayList<BlogsList>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_search, parent, false);
        }
        BlogsList people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(people.getBlogTitle());
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
            String str = ((BlogsList) resultValue).getBlogTitle();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (BlogsList people : tempItems) {
                    if (people.getBlogTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            List<BlogsList> filterList = (ArrayList<BlogsList>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (BlogsList people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
