package vp.mom.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vp.mom.R;
import vp.mom.adapters.NotificationSettingAadapter;
import vp.mom.api.AppConfig;
import vp.mom.api.SessionManager;
import vp.mom.app.AppController;
import vp.mom.app.Config;
import vp.mom.data.NotificationSettingPojo;
import vp.mom.gcm.gcm.NotificationUtils;


public class NotificationSettingActivity extends AppCompatActivity {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String msg,chatRoomId,userName;
    private NotificationUtils notificationUtils;
    NotificationSettingAadapter mAdapter;
    ArrayList<NotificationSettingPojo> mArrayList=new ArrayList<NotificationSettingPojo>();
    ListView listview;
   // Context context;
    private SessionManager session;
    TextView tooltitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
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
        tooltitle=(TextView)findViewById(R.id.tooltitle);
        tooltitle.setText("Notification setting");

        session=new SessionManager(vp.mom.activitys.NotificationSettingActivity.this);

        listview= (ListView) findViewById(R.id.listview);

        mAdapter = new NotificationSettingAadapter(mArrayList, vp.mom.activitys.NotificationSettingActivity.this);
        listview.setAdapter(mAdapter);
        getData();
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    handlePushNotification(intent);
                }
            }
        };

    }
    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);

        // if the push is of chat room message
        // simply update the UI unread messages count
        if (type == Config.PUSH_TYPE_CHATROOM) {
            msg = (String)intent.getSerializableExtra("message");

            chatRoomId  = intent.getStringExtra("UserToChat");

            userName=intent.getStringExtra("userName");

//            UserChatListPojo feeditem=new UserChatListPojo();
//
//            feeditem.setUserName(userName);
//            feeditem.setUserId(chatRoomId);
//            feeditem.setLastMessage(msg);
//            feeditem.setUserImage("http://marketofmums.com/images/users/");
//
//            if (msg != null && chatRoomId != null) {
//                updateRow(chatRoomId, feeditem);
//            }
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
    @Override
    protected void onResume() {
        super.onResume();

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clearing the notification tray
        // NotificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    private void getData() {
        String tag_string_req = "get_purchase_item";

        //   pDialog.setMessage("Loading ...");
        // showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.NOTIFICATION_ACTIVITY+"?"+"user_id="+session.getStringSessionData("uid"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             //   Log.e("get_purchase_item Response:", "get_purchase_item Response: " + response.toString());
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
              //  Log.e("get_purchase_item", "get_purchase_item Error: " + error.getMessage());

                //  hideDialog();
            }
        }) {


        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


}
    private void parseJsonFeed(JSONObject response) {

   //     Log.e("BookMarkFragment",""+response);
        try {

            if(response.getBoolean("status")) {
                JSONArray feedArray = response.getJSONArray("items");
                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);

                    NotificationSettingPojo pojo = new NotificationSettingPojo();
                    pojo.setNotification(feedObj.getString("Temp"));
                    pojo.setSettingStatus(feedObj.getBoolean("status"));
                    pojo.setNotificatiId(feedObj.getString("NTId"));
                    mArrayList.add(pojo);
                    //  Log.e("BookMarkFragment", "" + feedObj.getString("product_id"));
                }
            }
            // notify data changes to list adapater

        } catch (JSONException e) {

         //   Log.e("BookMarkFragment",""+e.toString());

            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
        // swipeRefreshLayout.setRefreshing(false);
    }
}
