package com.medisensehealth.fdccontributor.adapter.doctors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medisensehealth.fdccontributor.DataModel.DoctorList;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.doctors.DoctorDetailsActivity;
import com.medisensehealth.fdccontributor.database.MedisensePracticeDB;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 11-03-2017.
 */

public class DoctorsListAdapter  extends RecyclerView.Adapter<DoctorsListAdapter.MyViewHolder> {
    private List<DoctorList> doctorList;
    Context mContext;
    FragmentManager fragManager;
    FragmentTransaction fragTransaction;
    String DOWNLOAD_PROFILE;
    int DOCTOR_FAVOUTITE;
    AsyncDFavouriteUpdate asynFavouriteUpdate;
    int USER_ID;
    String USER_NAME;
    String USER_LOGIN_TYPE;
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int favourite_usertype;
   // DoctorDetailFragment doctorDetail;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView doc_name;
        public CustomTextView doc_exp, doc_spec, doc_qualification, doc_address;
        public ImageView doc_favourite, doc_profile;
        RelativeLayout doclist;
        CardView cardlist;

        public MyViewHolder(View view) {
            super(view);
            doc_name = (CustomTextView) view.findViewById(R.id.doclist_name);
            doc_address = (CustomTextView) view.findViewById(R.id.doc_address);
            //  doc_exp = (CustomTextView) view.findViewById(R.id.doclist_experience);
            doc_spec = (CustomTextView) view.findViewById(R.id.doclist_specialization);
            //  doc_qualification = (CustomTextView) view.findViewById(R.id.doclist_qualification);
            // doclist = (RelativeLayout)view.findViewById(R.id.relativeLayout_doclist);
            cardlist = (CardView) view.findViewById(R.id.doctor_list_cardview);
            doc_profile = (ImageView)view.findViewById(R.id.doclist_profile);
            doc_favourite = (ImageView)view.findViewById(R.id.doclist_favourite);
        }
    }

    public DoctorsListAdapter(Context context, List<DoctorList> docList) {
        this.doctorList = docList;
        this.mContext = context;
        fragManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
        shareadPreferenceClass = new ShareadPreferenceClass(context);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(context);
      //  doctorDetail = new DoctorDetailFragment();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctors_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final DoctorList doctor = doctorList.get(position);

        if(position % 2==0)
            holder.cardlist.setCardBackgroundColor(mContext.getResources().getColor(R.color.row_even_color));
        else
            holder.cardlist.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));


        holder.doc_name.setText(doctor.getDoctorName().trim());
        // holder.doc_exp.setText(doctor.getDoctorExperience().trim() + " Yrs");
        holder.doc_spec.setText(doctor.getDoctorSpecificationName().trim());
        holder.doc_address.setText(doctor.getDoctorAddress().trim());


        Log.d(Utils.TAG, "Pic: " + doctor.getDoctorPhoto().trim());
        if(doctor.getDoctorPhoto().trim().equals("")) {
            DOWNLOAD_PROFILE = APIClass.DRS_PROFILE_URL;
            Picasso.with(mContext).load(DOWNLOAD_PROFILE).fit().into(holder.doc_profile);
        }
        else {
             DOWNLOAD_PROFILE = APIClass.DRS_DOCTOR_PROFILE_URL+String.valueOf(doctor.getDoctorId())+"/"+doctor.getDoctorPhoto().trim();
            String urlStr = DOWNLOAD_PROFILE;
            URL url = null;
            try {
                url = new URL(urlStr);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                url = uri.toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            Picasso.with(mContext).load(String.valueOf(url))
                    .placeholder(R.drawable.user_profile)
                    .error(R.drawable.user_profile)
                    .resize(200, 200)
                    .centerInside()
                    .into(holder.doc_profile, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");
        }

        if(doctor.getDoctorLoginType().equals("1")) {
            holder.doc_favourite.setVisibility(View.INVISIBLE);
        }
        else  if(doctor.getDoctorLoginType().equals("2")) {
            holder.doc_favourite.setVisibility(View.VISIBLE);
            favourite_usertype = 1;
        }
        else  if(doctor.getDoctorLoginType().equals("3")) {
            holder.doc_favourite.setVisibility(View.INVISIBLE);
        }

        if(doctor.getDoctorFavoutite() == 0) {
              holder.doc_favourite.setImageResource(R.mipmap.like_empty);
              holder.doc_favourite.setTag(R.mipmap.like_empty);
        }
        else  if(doctor.getDoctorFavoutite() == 1) {
            holder.doc_favourite.setImageResource(R.mipmap.like_select);
            holder.doc_favourite.setTag(R.mipmap.like_select);
        }

        holder.doc_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DOCTOR_FAVOUTITE = 1;
                MedisensePracticeDB.updateDoctorFavourite(mContext, doctor.getDoctorId(), DOCTOR_FAVOUTITE,doctor.getDoctorLoginType(),doctor.getDoctorLoginUserId());
                asynFavouriteUpdate = new AsyncDFavouriteUpdate();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    asynFavouriteUpdate.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, USER_ID, doctor.getDoctorId(),favourite_usertype,DOCTOR_FAVOUTITE);
                } else {
                    asynFavouriteUpdate.execute(USER_ID, doctor.getDoctorId(),favourite_usertype,DOCTOR_FAVOUTITE);
                }

                if ((Integer)v.getTag() == R.mipmap.like_empty) {
                    holder.doc_favourite.setImageResource(R.mipmap.like_select);
                    holder.doc_favourite.setTag(R.mipmap.like_select);
                }
                holder.doc_favourite.setEnabled(false);
                Toast.makeText(mContext, doctor.getDoctorName()+" has been added to your favourite list", Toast.LENGTH_SHORT).show();
            }
        });


        holder.cardlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.getConnectivityStatusString(mContext).equalsIgnoreCase("enabled")) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("DOCTOR_ID", doctor.getDoctorId());
                    bundle.putString("DOCTOR_NAME", doctor.getDoctorName());
                    bundle.putInt("DOCTOR_SPECIALIZATION_ID", doctor.getDoctorSpecializationId());
                  /*  fragTransaction = fragManager.beginTransaction();
                    doctorDetail.setArguments(bundle);
                    fragTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragTransaction.replace(R.id.dashboard_fragment,doctorDetail,"DOCTOR_DETAILS");
                   // fragTransaction.addToBackStack("DOCTOR_DETAILS");
                    fragTransaction.commit();*/

                    Intent i1 = new Intent(mContext, DoctorDetailsActivity.class);
                    i1.putExtra("title","Doctor Details");
                    i1.putExtra("DOCTOR_ID", doctor.getDoctorId());
                    i1.putExtra("DOCTOR_NAME", doctor.getDoctorName());
                    i1.putExtra("DOCTOR_SPECIALIZATION_ID", doctor.getDoctorSpecializationId());
                    mContext.startActivity(i1);

                } else {
                    AppUtils.showCustomAlertMessage(mContext, HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    private class AsyncDFavouriteUpdate extends AsyncTask<Integer, Object, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Integer doInBackground(Integer... params) {
            Log.d(Utils.TAG, "DO IN BACKGROUND");
            final int fav_userid = params[0];
            final int fav_doc_id = params[1];
            final int fav_type = params[2];
            final int fav_status = params[3];

            Log.d(Utils.TAG, "fav_userid: " + fav_userid);
            Log.d(Utils.TAG, "fav_doc_id: " + fav_doc_id);
            Log.d(Utils.TAG, "fav_type: " + fav_type);
            Log.d(Utils.TAG, "fav_status: " + fav_status);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_ADD_DOCTOR_FAVOURITE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(Utils.TAG, " fav insert response: " + response.toString());
                            if (response != null) {

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            AppUtils.showCustomErrorMessage(mContext, "Failed", "Unable to Add Favourite", "OK", null, null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("Content-Type", "application/json; charset=utf-8");
                    map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                    map.put(APIClass.KEY_FAVOURITE_USERID, String.valueOf(fav_userid));
                    map.put(APIClass.KEY_FAVOURITE_DOCID, String.valueOf(fav_doc_id));
                    map.put(APIClass.KEY_FAVOURITE_TYPE, String.valueOf(fav_type));
                  //  map.put(APIClass.KEY_FAVOURITE_STATUS, String.valueOf(fav_status));
                    return map;
                }
            };

            RequestQueue requestQueue = AppController.getInstance(mContext).
                    getRequestQueue();
            AppController.getInstance(mContext).addToRequestQueue(stringRequest);

            return 1;
        }

        @Override
        protected void onPostExecute(Integer docLists) {
            super.onPostExecute(docLists);

            }
        }

}
