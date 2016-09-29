package vp.mom.fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vp.mom.R;

/**
 * Created by ApkDev2 on 07-11-2015.
 */
public class SettingFragment extends AppCompatActivity {
    //   ImageView back;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String msg,chatRoomId,userName;
    private vp.mom.gcm.gcm.NotificationUtils notificationUtils;
    TextView tool_title;
    TextView txtNotificationClick, txtsetBillingAddress,txtPaypal;
    Button btnLogoutsetting;
    private vp.mom.api.SessionManager session;
    TextView editprofile,btndeleteAccount,resetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_xml);
        session = new vp.mom.api.SessionManager(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        back=(ImageView)toolbar.findViewById(R.id.ic_back);
//        back.setClickable(true);
        tool_title = (TextView) toolbar.findViewById(R.id.tootlbar_title);
        tool_title.setText("Settings");
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        txtNotificationClick = (TextView) findViewById(R.id.txtNotificationClick);
        txtsetBillingAddress = (TextView) findViewById(R.id.txtsetBillingAddress);
        txtPaypal= (TextView) findViewById(R.id.txtPaypal);
        editprofile = (TextView) findViewById(R.id.editprofile);
        resetPassword = (TextView) findViewById(R.id.resetPassword);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(vp.mom.fragments.SettingFragment.this, vp.mom.activitys.UserProfileUpdate.class);
                startActivity(intent);
              //  finish();
            }
        });

        txtsetBillingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(), "Message", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(vp.mom.fragments.SettingFragment.this, vp.mom.activitys.BillingAddress.class);
                startActivity(intent);
             //   finish();

            }
        });
        txtNotificationClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(), "Message", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(vp.mom.fragments.SettingFragment.this, vp.mom.activitys.NotificationSettingActivity.class);
                startActivity(intent);
               // finish();

            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(vp.mom.fragments.SettingFragment.this, vp.mom.activitys.ResetPAssword.class);
                startActivity(intent);
             //   finish();
            }
        });

        txtPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(vp.mom.fragments.SettingFragment.this, vp.mom.activitys.ManagePaypal.class);
                startActivity(intent);
             //   finish();
            }
        });

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnLogoutsetting = (Button) findViewById(R.id.btnLogoutsetting);
        btndeleteAccount = (TextView) findViewById(R.id.btndeleteAccount);

        btnLogoutsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerGCM();


                session.setBooleanSessionData("isLoggedin", false);
                SharedPreferences pref = getSharedPreferences(vp.mom.api.SessionManager.PREF_NAME, 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();


                SharedPreferences pref1 = getSharedPreferences(vp.mom.app.MyPreferenceManager.PREF_NAME, 0);
                SharedPreferences.Editor editor1 = pref1.edit();
                editor1.clear();
                editor1.commit();

                Intent intent = new Intent(vp.mom.fragments.SettingFragment.this, vp.mom.LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });


        btndeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(vp.mom.fragments.SettingFragment.this);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want to delete your account?");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.app_logo);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        deleteAccount();

                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

            }
        });




        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(vp.mom.app.Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    handlePushNotification(intent);
                }
            }
        };
    }

    private void deleteAccount() {
        String tag_string_req = "deleteAccount";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.DELETE_ACCOUNT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("deleteAccount", "deleteAccount Response: " + response.toString());
                //	hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {


                        session.setBooleanSessionData("isLoggedin", false);
                        SharedPreferences pref = getSharedPreferences("MOMSession", 0);

                        SharedPreferences.Editor editor = pref.edit();

                        editor.clear();

                        Intent intent = new Intent(vp.mom.fragments.SettingFragment.this, vp.mom.LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(vp.mom.fragments.SettingFragment.this,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(vp.mom.fragments.SettingFragment.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("deleteAccount", "delete prod Error: " + error.getMessage());
                Toast.makeText(vp.mom.fragments.SettingFragment.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //	hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
//                params.put("email", email);
//                params.put("password", password);

                params.put("user_id",session.getStringSessionData("uid"));

                return params;
            }

        };
        // Adding request to request queue
        vp.mom.app.AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    // starting the service to register with GCM
    private void registerGCM() {
        Intent intent = new Intent(this, vp.mom.gcm.gcm.GcmIntentService.class);
        intent.putExtra("key", "logout");
        startService(intent);
    }
    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);

        // if the push is of chat room message
        // simply update the UI unread messages count
        if (type == vp.mom.app.Config.PUSH_TYPE_CHATROOM) {
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
        notificationUtils = new vp.mom.gcm.gcm.NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }
    @Override
    protected void onResume() {
        super.onResume();

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(vp.mom.app.Config.PUSH_NOTIFICATION));

        // clearing the notification tray
        // NotificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
