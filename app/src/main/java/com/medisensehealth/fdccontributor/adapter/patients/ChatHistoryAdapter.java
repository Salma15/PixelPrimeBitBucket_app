package com.medisensehealth.fdccontributor.adapter.patients;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.medisensehealth.fdccontributor.DataModel.ChatHistory;
import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.patients.ChatWindowFragment;
import com.medisensehealth.fdccontributor.network.APIClass;
import com.medisensehealth.fdccontributor.utils.Utils;
import com.medisensehealth.fdccontributor.views.CustomTextView;
import com.medisensehealth.fdccontributor.views.CustomTextViewSemiBold;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lenovo on 10-03-2017.
 */

public class ChatHistoryAdapter extends BaseAdapter {

    ArrayList<String> result;
    private List<ChatHistory> messageList;
    Context context;
    FragmentManager fragManager;
    private static LayoutInflater inflater=null;
    boolean isOutgoing = false;
    String USER_LOGIN_TYPE;


    public ChatHistoryAdapter(Context context, List<ChatHistory> msgList, String user_logintype) {
        this.messageList = msgList;
        this.context = context;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fragManager = ((AppCompatActivity) context).getSupportFragmentManager();
        USER_LOGIN_TYPE = user_logintype;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {

        public CustomTextView txtMessage;
        public CustomTextViewSemiBold txtInfo;
        public LinearLayout content;
        public LinearLayout contentWithBG;
        public ImageView doc_profile;
        public CustomTextView txtTimings;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ChatHistory chatMessage = messageList.get(position);
        System.out.println("chatMessage :" + chatMessage.getChatMessages());

        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.chatlist_item_message, null);

        holder.txtMessage = (CustomTextView) rowView.findViewById(R.id.txtMessage);
        holder.content = (LinearLayout) rowView.findViewById(R.id.content);
        holder.contentWithBG = (LinearLayout) rowView.findViewById(R.id.contentWithBackground);
        holder.txtInfo = (CustomTextViewSemiBold) rowView.findViewById(R.id.txtInfo);
        holder.doc_profile = (ImageView) rowView.findViewById(R.id.chat_profile);
        holder.txtTimings = (CustomTextView) rowView.findViewById(R.id.txtTimings);


        String DOWNLOAD_PROFILE;
        if(chatMessage.getChatDocImage().equals("")) {
            DOWNLOAD_PROFILE = APIClass.DRS_PROFILE_URL;
            Picasso.with(context).load(DOWNLOAD_PROFILE).fit().into(holder.doc_profile);
        }
        else {
            DOWNLOAD_PROFILE = APIClass.DRS_DOCTOR_PROFILE_URL+String.valueOf(chatMessage.getChatDocId())+"/"+chatMessage.getChatDocImage().trim();
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

            Picasso.with(context).load(String.valueOf(chatMessage.getChatDocImage()))
                    .placeholder(R.drawable.user_profile)
                    .error(R.drawable.user_profile)
                    .fit()
                    .into(holder.doc_profile, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }



        holder.txtMessage.setText(messageList.get(position).getChatMessages().trim());
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String current_date = sdf.format(c.getTime());


        if(USER_LOGIN_TYPE.equals("1")) {

            System.out.println(Utils.TAG +" MSG :" + messageList.get(position).getChatDocId());
            System.out.println(Utils.TAG +" MSG :" + messageList.get(position).getPartnerId());

            if((messageList.get(position).getChatDocId() !=0 ) && (messageList.get(position).getPartnerId() == 0)) {
                isOutgoing = false;
                setAlignment(holder, isOutgoing);
                Log.d(Utils.TAG, " out going ");
                holder.txtInfo.setText(messageList.get(position).getChatDocName().trim());
                holder.txtTimings.setText(messageList.get(position).getChatTime().toString());
            }
            else {
                isOutgoing = true;
                setAlignment(holder, isOutgoing);       // incoming
                Log.d(Utils.TAG, " in coming ");
                holder.txtInfo.setText(messageList.get(position).getChatDocName().trim());
                holder.txtTimings.setText(messageList.get(position).getChatTime().toString());
            }
        }
        else if(USER_LOGIN_TYPE.equals("2")) {

            if(messageList.get(position).getPartnerId() == ChatWindowFragment.ChatUserAccount) {
                isOutgoing = false;
                setAlignment(holder, isOutgoing);
                holder.txtInfo.setText(ChatWindowFragment.USER_NAME);
                holder.txtTimings.setText(current_date);
            }
            else {
                isOutgoing = true;
                setAlignment(holder, isOutgoing);
                holder.txtInfo.setText(messageList.get(position).getChatDocName().trim());
                holder.txtTimings.setText(messageList.get(position).getChatTime().toString());
            }
        }
        else if(USER_LOGIN_TYPE.equals("3")) {
            if(messageList.get(position).getPartnerId() == ChatWindowFragment.ChatUserAccount) {
                isOutgoing = false;
                setAlignment(holder, isOutgoing);
                holder.txtInfo.setText(ChatWindowFragment.USER_NAME);
                holder.txtTimings.setText(current_date);
            }
            else {
                isOutgoing = true;
                setAlignment(holder, isOutgoing);
                holder.txtInfo.setText(messageList.get(position).getChatDocName().trim());
                holder.txtTimings.setText(messageList.get(position).getChatTime().toString());
            }
        }

        return rowView;
    }

    private void setAlignment(Holder holder, boolean isOutgoing) {
        // TODO Auto-generated method stub
        if (!isOutgoing) {
            holder.contentWithBG.setBackgroundResource(R.drawable.chat_drawable);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setLayoutParams(layoutParams);
            holder.txtMessage.setTextColor(Color.parseColor("#FFFFFF"));

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.doc_profile.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.doc_profile.setLayoutParams(layoutParams);
            holder.doc_profile.setVisibility(View.GONE);

            layoutParams = (LinearLayout.LayoutParams) holder.txtTimings.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtTimings.setLayoutParams(layoutParams);
        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.chat_drawable_incoming);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.doc_profile.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.doc_profile.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtTimings.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtTimings.setLayoutParams(layoutParams);

        }
    }
}
