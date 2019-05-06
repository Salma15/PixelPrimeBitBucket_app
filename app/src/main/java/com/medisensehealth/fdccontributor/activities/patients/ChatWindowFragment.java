package com.medisensehealth.fdccontributor.activities.patients;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medisensehealth.fdccontributor.DataModel.ChatHistory;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.DashboardActivity;
import com.medisensehealth.fdccontributor.adapter.patients.ChatHistoryAdapter;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.network.NetworkUtil;
import com.medisensehealth.fdccontributor.utils.AppController;
import com.medisensehealth.fdccontributor.utils.AppUtils;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomEditText;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 10-03-2017.
 */
public class ChatWindowFragment extends Fragment {
    int Patient_ID;
    int DOCREF_ID;
    String Patient_View;
    FragmentManager fragManager;
    FragmentTransaction fragTransaction;
    Toolbar toolbar;

    ProgressBar progress_bar;
    RecyclerView chat_history_recyclerview;
    ArrayList<ChatHistory> chathistoryArraylist;
    ArrayList<String> ChatMessageArray;
    private ListView messagesContainer;
    private ImageView sendButton;
    private CustomEditText messageEditText;
    public static int ChatUserAccount;
    public static String USER_NAME, USER_LOGIN_TYPE;
    int USER_ID;
    String CHAT_MESSAGE;
    SwipyRefreshLayout swipeRefreshLayout_patient;

    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;

    AyncChatpdate asyncChatMessage;
    int PATIENT_RESPONSE_SELECT;

    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_chatwindow, vg, false);

        root.setFocusableInTouchMode(true);
        root.requestFocus();

        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if(Patient_View.equals("PUSHVIEW")) {
                            Intent intent = new Intent(getActivity(), DashboardActivity.class);
                            intent.putExtra("LOGIN_TYPE", USER_LOGIN_TYPE);
                            intent.putExtra("USER_ID", USER_ID);
                            intent.putExtra("ENTRY_TYPE", "NORMAL");
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else {
                            getActivity().finish();
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fragManager = getActivity().getSupportFragmentManager();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Patient_ID = bundle.getInt("PATIENT_ID", 0);
            DOCREF_ID = bundle.getInt("DOCREF_ID", 0);
            Patient_View = bundle.getString("PATIENT_VIEW", "VIEW");
            Log.d(Utils.TAG + " Chat Pat Id:", String.valueOf(Patient_ID));
            Log.d(Utils.TAG + " Chat Ref Id:", String.valueOf(DOCREF_ID));
            Log.d(Utils.TAG + " Chat Pat:", String.valueOf(Patient_View));
        }

        shareadPreferenceClass = new ShareadPreferenceClass(getActivity());
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(getActivity());
        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {

            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        initialization();
    }

    private void initialization() {
        progress_bar = (ProgressBar) getActivity().findViewById(R.id.progress_bar);
       // chat_history_recyclerview = (RecyclerView) getActivity().findViewById(R.id.chat_history_recyclerview);
        swipeRefreshLayout_patient = (SwipyRefreshLayout) getActivity().findViewById(R.id.chat_swipeRefreshLayout);
        swipeRefreshLayout_patient.setDistanceToTriggerSync(200);

        messagesContainer = (ListView)  getActivity().findViewById(R.id.messagesContainer);
        messageEditText = (CustomEditText)  getActivity().findViewById(R.id.messageEdit);
        sendButton = (ImageView)  getActivity().findViewById(R.id.chatSendButton);

      //  getChatHistoryMeassagesfromServer();


        ChatUserAccount = USER_ID;
        Log.d(Utils.TAG + " Chat UserId:", String.valueOf(ChatUserAccount));


        loadChatHistory();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SendMessagetoServer();
                Dialog dialog_checkbox;
                PATIENT_RESPONSE_SELECT = 0;
                final String[] items = {" YES"};
                final ArrayList itemsSelected = new ArrayList();

                if(messageEditText.getText().toString().equals("")) {
                    AppUtils.showCustomAlertMessage(getActivity(),  "CHAT", "Please enter message to send", "OK", null, null);
                }
                else {
                    PATIENT_RESPONSE_SELECT = 0;
                    SendMessagetoServer();
                 /*   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Would you like to send this response to patient too?");
                    builder.setMultiChoiceItems(items, null,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int selectedItemId,
                                                    boolean isSelected) {
                                    if (isSelected) {
                                        itemsSelected.add(selectedItemId);
                                        //   Log.d(Utils.TAG + " isSelected:", String.valueOf(selectedItemId));
                                        PATIENT_RESPONSE_SELECT = 1;
                                    } else {
                                        Log.d(Utils.TAG + " notSelected:", String.valueOf(0));
                                        PATIENT_RESPONSE_SELECT = 0;
                                    }
                                }
                            })
                            .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    Log.d(Utils.TAG + " DONE:", String.valueOf(PATIENT_RESPONSE_SELECT));
                                    SendMessagetoServer();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    Log.d(Utils.TAG + " Cancel:", String.valueOf(PATIENT_RESPONSE_SELECT));
                                    SendMessagetoServer();
                                }
                            });
                    dialog_checkbox = builder.create();
                    dialog_checkbox.show();*/
                }
            }
        });

        messagesContainer.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (listView == null || listView.getChildCount() == 0) ?
                        0 : messagesContainer.getChildAt(0).getTop();
                swipeRefreshLayout_patient.setEnabled((topRowVerticalPosition >= 0));
            }
        });

        swipeRefreshLayout_patient.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                Log.d(Utils.TAG, "Refresh triggered at "+ (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
                if(direction == SwipyRefreshLayoutDirection.TOP) {
                    refreshItems();
                }

            }
        });
    }

    void refreshItems() {
        if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
            loadChatHistory();
        } else {
            AppUtils.showCustomAlertMessage(getActivity(), HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
        }

        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...
        // Stop refresh animation
        swipeRefreshLayout_patient.setRefreshing(false);
    }

    private void SendMessagetoServer() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String current_date = sdf.format(c.getTime());
        //Log.d(Utils.TAG, "current_date: " +current_date);

        CHAT_MESSAGE = messageEditText.getText().toString();
        if (CHAT_MESSAGE.length()>0)
        {
          //  chathistoryArraylist.add(new ChatHistory(Patient_ID,DOCREF_ID,USER_ID,USER_NAME, CHAT_MESSAGE,USER_NAME,"",current_date));
            chathistoryArraylist.add(new ChatHistory(Patient_ID,0,0,DOCREF_ID,USER_NAME,CHAT_MESSAGE,current_date, ""));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ChatMessageArray);
            messagesContainer.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            messagesContainer.setAdapter(adapter);
            messagesContainer.setAdapter(new ChatHistoryAdapter(getActivity(), chathistoryArraylist, USER_LOGIN_TYPE));
            messageEditText.setText("");

            if (NetworkUtil.getConnectivityStatusString(getActivity()).equalsIgnoreCase("enabled")) {
                asyncChatMessage = new AyncChatpdate();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    asyncChatMessage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, chathistoryArraylist);
                } else {
                    asyncChatMessage.execute(chathistoryArraylist);
                }
            } else {
                AppUtils.showCustomAlertMessage(getActivity(), HCConstants.INTERNET, HCConstants.INTERNET_CHECK, "OK", null, null);
            }

        }


    }

    private void loadChatHistory() {
        progress_bar.setVisibility(View.VISIBLE);
        // TODO Auto-generated method stub
        MyTask objMyTask = new MyTask();
        objMyTask.execute();
    }

    class MyTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_CHATHISTORY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // progress_bar.setVisibility(View.GONE);
                            Log.d(Utils.TAG, response.toString());
                            if (response != null) {
                                chathistoryArraylist = new ArrayList<>();
                                ChatMessageArray = new ArrayList<String>();
                                try {
                                    String response_filter =  stripHtml(response);
                                    JSONObject jsonObject = new JSONObject(response_filter);
                                    String staus_res = jsonObject.getString("status");
                                    Log.d(Utils.TAG, " chat: "+  staus_res);

                                    if(staus_res.equals("false"))
                                    {
                                     //   AppUtil.showCustomAlert(getContext(), "Chat","No Messages available !!", "OK", null);
                                        AppUtils.showCustomSuccessMessage(getContext(), "Chat","No Messages available !!!", "OK", null, null);
                                    }
                                    else {
                                        JSONArray jsonArray = jsonObject.getJSONArray("chat_detail");

                                      //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); sdf.parse(dateStr),
                                        if (jsonArray.length() > 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
//                                                chathistoryArraylist.add(new ChatHistory(jsonArray.getJSONObject(i).getInt("patient_id"),jsonArray.getJSONObject(i).getInt("ref_id"),
//                                                        jsonArray.getJSONObject(i).getInt("chat_id"),jsonArray.getJSONObject(i).getInt("partner_id"), jsonArray.getJSONObject(i).getString("chat_note"), jsonArray.getJSONObject(i).getString("ref_name"),
//                                                        jsonArray.getJSONObject(i).getString("doc_photo"), jsonArray.getJSONObject(i).getString("TImestamp")));

                                                chathistoryArraylist.add(new ChatHistory(jsonArray.getJSONObject(i).getInt("patient_id"),jsonArray.getJSONObject(i).getInt("chat_id"),jsonArray.getJSONObject(i).getInt("partner_id"),
                                                        jsonArray.getJSONObject(i).getInt("doc_id"),jsonArray.getJSONObject(i).getString("doc_name"),jsonArray.getJSONObject(i).getString("chat_note"),
                                                        jsonArray.getJSONObject(i).getString("time_stamp"), jsonArray.getJSONObject(i).getString("doc_photo")));

                                            }


                                        }

                                        if (chathistoryArraylist.size() > 0) {
                                            for (int i = 0; i < chathistoryArraylist.size(); i++) {
                                                ChatMessageArray.add(chathistoryArraylist.get(i).getChatMessages());

                                            //    ChatUserAccount = chathistoryArraylist.get(i).getChatPatId();
                                            //    ChatUserAccount = chathistoryArraylist.get(i).getPartnerId();
                                            }

                                            loadchatHistoryMessages();

                                        }

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }else {
                                progress_bar.setVisibility(View.GONE);
                                //	AppUtil.showCustomAlert(getContext(), "Server Error!!!","You Can Try it later", "OK", null);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progress_bar.setVisibility(View.GONE);
                          //  AppUtil.showCustomAlert(getContext(), "Server!!!","You Can Try it later", "OK", null);
                            //   Log.d(Utils.TAG+"ERR",error.toString());
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                    map.put(APIClass.KEY_PATIENTID, String.valueOf(Patient_ID));
                    map.put(APIClass.KEY_REFID, String.valueOf(DOCREF_ID));
                    return map;
                }
            };

            RequestQueue requestQueue = AppController.getInstance(getContext()).
                    getRequestQueue();
            AppController.getInstance(getContext()).addToRequestQueue(stringRequest);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            progress_bar.setVisibility(View.GONE);

        }
    }

    private String stripHtml(String response) {
        return Html.fromHtml(response).toString();
    }

    private void loadchatHistoryMessages() {
        Log.d(Utils.TAG, "loadchatHistoryMessages");
      //  Log.d(Utils.TAG, String.valueOf(ChatMessageArray.size()));

        if(ChatMessageArray.size() > 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ChatMessageArray);
            messagesContainer.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            messagesContainer.setAdapter(adapter);

            messagesContainer.setAdapter(new ChatHistoryAdapter(getActivity(), chathistoryArraylist, USER_LOGIN_TYPE));
        }

    }

    private void getChatHistoryMeassagesfromServer() {

        progress_bar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_CHATHISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // progress_bar.setVisibility(View.GONE);
                        Log.d(Utils.TAG, response.toString());
                        if (response != null) {
                            GetJsonArrayResponse(response);
                        }else {
                            progress_bar.setVisibility(View.GONE);
                            //	AppUtil.showCustomAlert(getContext(), "Server Error!!!","You Can Try it later", "OK", null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_bar.setVisibility(View.GONE);
                      //  AppUtil.showCustomAlert(getContext(), "Server Error!!!","You Can Try it later", "OK", null);
                        //   Log.d(Utils.TAG+"ERR",error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(APIClass.KEY_API_KEY,APIClass.API_KEY);
                map.put(APIClass.KEY_PATIENTID, String.valueOf(Patient_ID));
                map.put(APIClass.KEY_REFID, String.valueOf(DOCREF_ID));
                return map;
            }
        };

        RequestQueue requestQueue = AppController.getInstance(getContext()).
                getRequestQueue();
        AppController.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void GetJsonArrayResponse(String response) {
        Log.d(Utils.TAG, "GetJsonArrayResponse");
        progress_bar.setVisibility(View.GONE);
    }

    class AyncChatpdate extends AsyncTask<Object, Object, Void> {
        String response_result;
        JSONObject jsonObject;
        JSONArray jsonArray;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(Utils.TAG, "PreExec:");

        }

        @Override
        protected Void doInBackground(Object... params) {
            Log.d(Utils.TAG, "BackgroundExec:");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIClass.DRS_CHATMESSAGEINSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                               Log.d(Utils.TAG, "insert response: " + response.toString());
                            if (response != null) {
                                GetJsonArrayResponseMessage(response);
                            }else {
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                          //  AppUtil.showCustomAlert(getActivity(), "Server Error!!!",error.toString(), "OK", null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("Content-Type", "application/json; charset=utf-8");
                    map.put(APIClass.KEY_API_KEY, APIClass.API_KEY);
                    map.put(APIClass.KEY_CHAT_PATIENTID, String.valueOf(Patient_ID));
                    map.put(APIClass.KEY_CHAT_DOCTORID, String.valueOf(DOCREF_ID) );
                    map.put(APIClass.KEY_CHAT_PARTNER_ID, String.valueOf(0));
                    map.put(APIClass.KEY_CHAT_MESSAGE, CHAT_MESSAGE);
                    map.put(APIClass.KEY_LOGINTYPE, USER_LOGIN_TYPE);
                    map.put(APIClass.KEY_CHAT_PATRESPONSE_SEND, String.valueOf(PATIENT_RESPONSE_SELECT));

                    return map;
                }
            };

            RequestQueue requestQueue = AppController.getInstance(getActivity()).
                    getRequestQueue();
            AppController.getInstance(getActivity()).addToRequestQueue(stringRequest);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private void GetJsonArrayResponseMessage(String response) {
        Log.d(Utils.TAG, "GetJsonArrayResponseMessage");
        try {
            JSONObject jsonObject = new JSONObject(response);
            String staus_res_chat = jsonObject.getString("status");
            Log.d(Utils.TAG, staus_res_chat);

            if( (staus_res_chat.equals("true")))
            {
                Log.d(Utils.TAG, "chat message inserted successfully");
            }
            else if( (staus_res_chat.equals("false")))
            {
                Log.d(Utils.TAG, "failed to insert");
           //   AppUtil.showCustomAlert(getContext(), "Failed","Server Error !!!", "OK", null);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
