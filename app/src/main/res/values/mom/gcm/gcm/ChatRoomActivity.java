package vp.mom.gcm.gcm;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import vp.mom.R;
import vp.mom.api.AppConfig;
import vp.mom.api.SessionManager;
import vp.mom.app.AppController;
import vp.mom.app.Config;
import vp.mom.app.SendCommentButton;
import vp.mom.data.PrivateChatPojo;


public class ChatRoomActivity extends AppCompatActivity implements SendCommentButton.OnSendClickListener {
    String msg,userName;
    private NotificationUtils notificationUtils;
    private String TAG = vp.mom.gcm.gcm.ChatRoomActivity.class.getSimpleName();

    private String chatRoomId;
    private RecyclerView recyclerView;
    private ChatRoomThreadAdapter mAdapter;
    private ArrayList<PrivateChatPojo> messageArrayList;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private EditText inputMessage;
    private SendCommentButton btnSend;
    SessionManager session;
    String selfUserId,stringMsg;
    private ProgressDialog pDialog;
    TextView textTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gcm_private_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarcomment);
        setSupportActionBar(toolbar);
        textTitle= (TextView) toolbar.findViewById(R.id.titletool);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        session=new SessionManager(this);
        inputMessage = (EditText) findViewById(R.id.inputMsg);
        btnSend = (SendCommentButton) findViewById(R.id.btnSendmsgchat);
        btnSend.setOnSendClickListener(this);
        pDialog = new ProgressDialog(vp.mom.gcm.gcm.ChatRoomActivity.this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);

        Intent intent = getIntent();
        chatRoomId = intent.getStringExtra("UserToChat");
     //   String title = intent.getStringExtra("name");

        textTitle.setText("Private chat");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        if (chatRoomId == null) {
            Toast.makeText(getApplicationContext(), "Chat room not found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        recyclerView = (RecyclerView) findViewById(R.id.rvChatMsg);

        messageArrayList = new ArrayList<>();

        // self user id is to identify the message owner
        selfUserId =   session.getStringSessionData("uid");

        mAdapter = new ChatRoomThreadAdapter(this, messageArrayList, selfUserId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push message is received
                    handlePushNotification(intent);
                }
            }
        };
        getChat();


    }

//    private void setupComments() {
//
//        messageArrayList=new ArrayList<>();
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setHasFixedSize(true);
//
//        mAdapter = new ChatRoomThreadAdapter(this,messageArrayList,);
//        recyclerView.setAdapter(mAdapter);
//        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
//
////        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
////            @Override
////            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
////                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
////                    mAdapter.setAnimationsLocked(true);
////                }
////            }
////        });
//    }

    private boolean validateComment() {
        if (TextUtils.isEmpty(inputMessage.getText())) {
            btnSend.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_error));
            return false;
        }

        return true;
    }

    @Override
    public void onSendClickListener(View v) {
        if (validateComment()) {
            stringMsg=inputMessage.getText().toString();
            inputMessage.setText(null);
            PrivateChatPojo chatItem=new PrivateChatPojo();

            chatItem.setFromUser(selfUserId);
            chatItem.setToUser(chatRoomId);
            chatItem.setChatMessage(stringMsg);
            Calendar calendar = Calendar.getInstance();
            long startTime = calendar.getTimeInMillis();
            chatItem.setMsgTime(""+startTime);
            messageArrayList.add(chatItem);
            // feeditem1.setCommentText(commentText);
            //  commentData.add(feeditem1);
            //   commentsAdapter.notifyDataSetChanged();
            //  commentsAdapter.setAnimationsLocked(false);
            //  commentsAdapter.setDelayEnterAnimation(false);

            //  rvComments.smoothScrollBy(0, rvComments.getChildAt(0).getHeight() * commentsAdapter.getItemCount());
           // inputMessage.setText(null);
            btnSend.setCurrentState(SendCommentButton.STATE_DONE);

            mAdapter.notifyDataSetChanged();
            if (mAdapter.getItemCount() > 1) {
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
            }
            sendMessage();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // registering the receiver for new notification
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        NotificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Handling new push message, will add the message to
     * recycler view and scroll it to bottom
     * */
    private void handlePushNotification(Intent intent) {
     //  PrivateChatPojo message = (PrivateChatPojo) intent.getSerializableExtra("message");
       String chatId = intent.getStringExtra("UserToChat");

        if(chatId.equals(chatRoomId))
        {
            String msg= (String)intent.getSerializableExtra("message");
            String msgTimeStamp= (String)intent.getSerializableExtra("msgTimeStamp");

            PrivateChatPojo chatItem=new PrivateChatPojo();
            chatItem.setFromUser(chatId);
            chatItem.setToUser(selfUserId);
            chatItem.setMsgTime(msgTimeStamp);
            chatItem.setChatMessage(msg);

            //  messageArrayList.add(chatItem);

            if (chatItem != null && chatId != null) {
                messageArrayList.add(chatItem);
                mAdapter.notifyDataSetChanged();
                if (mAdapter.getItemCount() > 1) {
                    recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                }
            }
        }
        else {
            int type = intent.getIntExtra("type", -1);

            // if the push is of chat room message
            // simply update the UI unread messages count
            if (type == Config.PUSH_TYPE_CHATROOM) {
                msg = (String)intent.getSerializableExtra("message");

                chatRoomId  = intent.getStringExtra("UserToChat");

                userName=intent.getStringExtra("userName");

            }
            long when = System.currentTimeMillis();
            // app is in background. show the message in notification try
            Intent resultIntent = new Intent(getApplicationContext(), vp.mom.gcm.gcm.ChatRoomActivity.class);
            resultIntent.putExtra("UserToChat", chatRoomId);
            resultIntent.putExtra("name", userName);
            showNotificationMessage(getApplicationContext(), userName, msg,
                    "" + when, resultIntent);
        }

    }
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    private void getChat() {
        String tag_string_req = "getChat";

        pDialog.setMessage("Loading ...");
        // showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_MESSAGE_BY_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  Log.e("getChat:", "getChat: " + response.toString());
                //   hideDialog();
                if (response != null) {

                    // commentData.clear();
                    JSONObject jsonresponse= null;
                    try {
                        jsonresponse = new JSONObject(response.toString());

                        parseJsonFeed(jsonresponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("param", "Login Error: " + error.getMessage());

                //  hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("from_user",session.getStringSessionData("uid"));
                params.put("to_user",chatRoomId);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    private void parseJsonFeed(JSONObject response) {
        messageArrayList.clear();

     //   Log.e(TAG,""+response);
        try {
            JSONArray feedArray = response.getJSONArray("items");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);


                PrivateChatPojo chatItem=new PrivateChatPojo();

                chatItem.setFromUser(feedObj.getString("from_user"));
                chatItem.setToUser(feedObj.getString("to_user"));
                chatItem.setChatMessage(feedObj.getString("message"));
                chatItem.setMsgTime(feedObj.getString("message_date"));
                messageArrayList.add(chatItem);



            }
            // notify data changes to list adapater

        } catch (JSONException e) {

            Log.e(TAG,""+e.toString());

            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();

        if (mAdapter.getItemCount() > 1) {
            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
        }
    }

    private void sendMessage() {
        String tag_string_req = "AddComments";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.Send_Private_Chat, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
            ///    Log.d("ADD_COMMENTS", "ADD_COMMENTS Response: " + response.toString());
                //	hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {

                        //    getData();
                        // user successfully logged in
                     //   Log.e("ADD_COMMENTS", "ADD_COMMENTS Response: " + response.toString());

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(vp.mom.gcm.gcm.ChatRoomActivity.this,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                  //  Toast.makeText(ChatRoomActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ADD_COMMENTS", "ADD_COMMENTS Error: " + error.getMessage());
//                Toast.makeText(ChatRoomActivity.this,
//                        error.getMessage(), Toast.LENGTH_LONG).show();
                //	hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
//                params.put("email", email);
//                params.put("password", password);

                params.put("from_user",selfUserId);
                params.put("to_user",chatRoomId);
                params.put("message",stringMsg);


                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}
