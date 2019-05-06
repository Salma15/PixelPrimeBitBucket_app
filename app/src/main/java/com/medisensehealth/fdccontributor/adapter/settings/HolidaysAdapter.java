package com.medisensehealth.fdccontributor.adapter.settings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medisensehealth.fdccontributor.DataModel.Holidays;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextViewSemiBold;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 20-07-2018.
 */

public class HolidaysAdapter  extends RecyclerView.Adapter<HolidaysAdapter.MyViewHolder> {

    Context mContext;
    private List<Holidays> holidayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextViewSemiBold holiday_date, holiday_reason;
        public CardView holiday_item;
        public ImageView holiday_cancel;

        public MyViewHolder(View view) {
            super(view);
            holiday_date = (CustomTextViewSemiBold) view.findViewById(R.id.holid_date);
            holiday_reason = (CustomTextViewSemiBold) view.findViewById(R.id.holid_reason);
            holiday_item = (CardView) view.findViewById(R.id.holid_item);
            holiday_cancel = (ImageView) view.findViewById(R.id.holid_cancel);
        }
    }

    public HolidaysAdapter(Context context, List<Holidays> holidayList) {
        this.mContext = context;
        this.holidayList = holidayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holiday_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Holidays holidays = holidayList.get(position);

        if(position % 2==0)
            holder.holiday_item.setCardBackgroundColor(mContext.getResources().getColor(R.color.row_even_color));
        else
            holder.holiday_item.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));

        if(holidays.getHolidayDate() != null){
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = inputFormat.parse(holidays.getHolidayDate());
                String holiday_date_new = outputFormat.format(date);
                holder.holiday_date.setText(holiday_date_new);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        holder.holiday_reason.setText(holidays.getHolidayReason());

        holder.holiday_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 AlertDialog.Builder dialog = new AlertDialog.Builder(mContext, R.style.CustomDialog);
                dialog.setTitle("Remove from holiday's list?");
                dialog.setMessage("Do you want to remove from holiday's list?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        new setJsonDeleteHoliday(String.valueOf(holidays.getHolidayId()),String.valueOf(holidays.getDocId()),String.valueOf(holidays.getDocType())).execute();
                        onItemDismiss(position);
                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Action for "Cancel".
                                dialog.cancel();
                            }
                        });

                final AlertDialog alert = dialog.create();
                alert.show();
            }
        });
    }

    public void onItemDismiss(int position) {
        if (position != -1 && position < holidayList.size()) {
            holidayList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    @Override
    public int getItemCount() {
        return holidayList.size();
    }

    class setJsonDeleteHoliday extends AsyncTask<String,String,String> {
        String holiday_id, holiday_docid, holiday_doctype;
        ProgressDialog pd1;

        public setJsonDeleteHoliday(String holid_id, String docid, String doctype) {
            holiday_id = holid_id;
            holiday_docid = docid;
            holiday_doctype = doctype;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd1 = new ProgressDialog(mContext);
            pd1.setMessage("Cancelling holiday...");
            //   pd1.show();
        }

        @Override
        protected String doInBackground(String... key) {


            Log.d(Utils.TAG, " holiday_id: " + holiday_id);
            Log.d(Utils.TAG, " holiday_docid: " + holiday_docid);
            Log.d(Utils.TAG, " holiday_doctype: " + holiday_doctype);

            int socketTimeout1 = 10000; // 10 seconds.
            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, APIClass.DRS_CANCEL_HOLIDAYS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {

                            ((Activity)mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(Utils.TAG, " resStatus: " + response.toString());
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response);
                                        String staus_res = jsonObject.getString("result");
                                        if(staus_res.equals("success")) {
                                            Toast.makeText(mContext, "Holiday cancelled !!!", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(mContext, "Failed to cancel holiday date. ", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    if(pd1 != null && pd1.isShowing()){
                                        pd1.dismiss();
                                    }
                                }
                            });
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(pd1 != null && pd1.isShowing()){
                                pd1.dismiss();
                            }
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                    map.put(APIClass.KEY_USERID, holiday_docid);
                    map.put(APIClass.KEY_LOGINTYPE, holiday_doctype);
                    map.put(APIClass.KEY_HOLIDAY_ID, holiday_id);
                    return map;
                }
            };
            stringRequest1.setRetryPolicy(policy1);
            RequestQueue requestQueue1 = AppController.getInstance(mContext).
                    getRequestQueue();
            AppController.getInstance(mContext).addToRequestQueue(stringRequest1);

            return null;
        }
    }
}
