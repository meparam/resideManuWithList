package vp.mom.activitys;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vp.mom.R;
import vp.mom.adapters.ChatRoomsAdapter;
import vp.mom.api.AppConfig;
import vp.mom.app.AppController;
import vp.mom.app.Config;
import vp.mom.gcm.gcm.NotificationUtils;
import vp.mom.gcm.gcm.SimpleDividerItemDecoration;


public class UserChatList extends AppCompatActivity {

    private String TAG = vp.mom.activitys.UserChatList.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ArrayList<vp.mom.data.UserChatListPojo> chatRoomArrayList;
    private ChatRoomsAdapter mAdapter;
    private RecyclerView recyclerView;
    vp.mom.api.SessionManager session;
    private ProgressDialog pDialog;
    TextView tooltitle;
    private NotificationUtils notificationUtils;
    String msg,chatRoomId,userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session=new vp.mom.api.SessionManager(this);
        setContentView(R.layout.user_chat);
        setCustomActionBar();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(vp.mom.activitys.UserChatList.this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_userList);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                  handlePushNotification(intent);
                }
            }
        };

        chatRoomArrayList = new ArrayList<>();
        mAdapter = new ChatRoomsAdapter(this, chatRoomArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

//        recyclerView.addOnItemTouchListener(new ChatRoomsAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new ChatRoomsAdapter.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                // when chat is clicked, launch full chat thread activity
//
//
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));
        fetchChatRooms();
    }

    private void setCustomActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tooltitle = (TextView) findViewById(R.id.tooltitle);
        tooltitle.setText("Messages");

    }


    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);

        // if the push is of chat room message
        // simply update the UI unread messages count
        if (type == Config.PUSH_TYPE_CHATROOM) {
            msg = (String)intent.getSerializableExtra("message");

            chatRoomId  = intent.getStringExtra("UserToChat");

            userName=intent.getStringExtra("userName");

            vp.mom.data.UserChatListPojo feeditem=new vp.mom.data.UserChatListPojo();

            feeditem.setUserName(userName);
            feeditem.setUserId(chatRoomId);
            feeditem.setLastMessage(msg);
            feeditem.setUserImage("http://marketofmums.com/images/users/");

            if (msg != null && chatRoomId != null) {
                updateRow(chatRoomId, feeditem);
            }
        }
        long when = System.currentTimeMillis();
        // app is in background. show the message in notification try
        Intent resultIntent = new Intent(getApplicationContext(), vp.mom.gcm.gcm.ChatRoomActivity.class);
        resultIntent.putExtra("UserToChat", chatRoomId);
        resultIntent.putExtra("name", userName);
        showNotificationMessage(getApplicationContext(), userName, msg,
                ""+when, resultIntent);

    }
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }
//    /**
//     * Updates the chat list unread count and the last message
//     */
    private void updateRow(String chatRoomId, vp.mom.data.UserChatListPojo message) {
        for (vp.mom.data.UserChatListPojo cr : chatRoomArrayList) {
            if (cr.getUserId().equals(chatRoomId)) {
                int index = chatRoomArrayList.indexOf(cr);
                cr.setLastMessage(message.getLastMessage());
                cr.setUnreadCount(cr.getUnreadCount() + 1);
                chatRoomArrayList.remove(index);
                chatRoomArrayList.add(index, cr);
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    private void fetchChatRooms() {
        String tag_string_req = "ExploreItemFragment";

        pDialog.setMessage("Loading ...");
        // showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_Chat_User_List, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  Log.e("Login Response:", "Login Response: " + response.toString());
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
                params.put("user_id",session.getStringSessionData("uid"));
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    private void parseJsonFeed(JSONObject response) {
        chatRoomArrayList.clear();

      //  Log.e(TAG,""+response);
        try {
            JSONArray feedArray = response.getJSONArray("items");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);


                vp.mom.data.UserChatListPojo feeditem=new vp.mom.data.UserChatListPojo();

                feeditem.setUserName(feedObj.getString("first_name"));
                feeditem.setUserId(feedObj.getString("user_id"));
                feeditem.setUserImage("http://marketofmums.com/images/users/" + feedObj.getString("photo"));

                feeditem.setLastMessage("");
                feeditem.setUnreadCount(0);

                chatRoomArrayList.add(feeditem);


            }
            // notify data changes to list adapater

        } catch (JSONException e) {

         //   Log.e(TAG,""+e.toString());

            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
        // swipeRefreshLayout.setRefreshing(false);
    }
    @Override
    protected void onResume() {
        super.onResume();

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clearing the notification tray
        NotificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
//            case R.id.action_logout:
//                MyApplication.getInstance().logout();
//                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }


}
