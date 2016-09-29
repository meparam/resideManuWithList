package vp.mom.searchtab;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vp.mom.R;
import vp.mom.api.CircleTransformation;
import vp.mom.app.AppController;

public class UserProfileDetailActivity extends AppCompatActivity {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String msg,chatRoomId,ChatuserName;
    private vp.mom.gcm.gcm.NotificationUtils notificationUtils;
    ImageView profilepic;
   // TextView name;
    String userId;
    private ProgressDialog pDialog;
  //  private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int avatarSize;
    Button btnFollow,sellerMSg;
    RatingBar rating;
    vp.mom.api.SessionManager session;
    LinearLayout followerll,follwingll;
    LinearLayout ratingLayout;
    TextView tool_title,userFollower,userFollowing,userName,name,peopleCount;
    boolean userItself=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        session = new vp.mom.api.SessionManager(vp.mom.searchtab.UserProfileDetailActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tool_title=(TextView)toolbar.findViewById(R.id.tootlbar_title);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tool_title.setText("User Profile");

        this.avatarSize = getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);
        pDialog = new ProgressDialog(vp.mom.searchtab.UserProfileDetailActivity.this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        Intent intent=getIntent();
        if(intent!=null)
        {
            userId=intent.getStringExtra("userId");
            userItself=intent.getBooleanExtra("userItself",false);
        }

        Log.e("iD OF uSer",""+userId);
        init();

        if(userItself||userId.equals(session.getStringSessionData("uid")))
        {
            btnFollow.setVisibility(View.GONE);
            sellerMSg.setVisibility(View.GONE);
        }


        getData();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFollwing(userId);
            }
        });

        followerll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uerpupdate=new Intent(vp.mom.searchtab.UserProfileDetailActivity.this, vp.mom.activitys.FollowerListActivity.class);

                uerpupdate.putExtra("userId",userId);
                startActivity(uerpupdate);
            }
        });
        follwingll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uerpupdate=new Intent(vp.mom.searchtab.UserProfileDetailActivity.this, vp.mom.activitys.FollowingListActivity.class);
                uerpupdate.putExtra("userId",userId);
                startActivity(uerpupdate);
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

        sellerMSg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(vp.mom.searchtab.UserProfileDetailActivity.this, vp.mom.gcm.gcm.ChatRoomActivity.class);
                resultIntent.putExtra("UserToChat", userId);
                resultIntent.putExtra("name", "Chat");
                startActivity(resultIntent);
            }
        });

        ratingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(vp.mom.searchtab.UserProfileDetailActivity.this, vp.mom.activitys.RatingActivity.class);
                resultIntent.putExtra("userID",userId);
                startActivity(resultIntent);
            }
        });

    }

    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);

        // if the push is of chat room message
        // simply update the UI unread messages count
        if (type == vp.mom.app.Config.PUSH_TYPE_CHATROOM) {
            msg = (String)intent.getSerializableExtra("message");

            chatRoomId  = intent.getStringExtra("UserToChat");

            ChatuserName=intent.getStringExtra("userName");

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
        resultIntent.putExtra("name", ChatuserName);
        showNotificationMessage(getApplicationContext(), ChatuserName, msg,
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
    private void init() {
        profilepic= (ImageView) findViewById(R.id.imgprofile);
        name=(TextView) findViewById(R.id.txtName);
        userFollower=(TextView) findViewById(R.id.userFollower);
        userFollowing=(TextView) findViewById(R.id.userFollowing);
        userName=(TextView) findViewById(R.id.userName);
        btnFollow= (Button) findViewById(R.id.btnFollow);
        followerll= (LinearLayout) findViewById(R.id.followerll);
        follwingll= (LinearLayout) findViewById(R.id.follwingll);
        rating= (RatingBar) findViewById(R.id.MyRating);
        followerll.setClickable(true);
        follwingll.setClickable(true);
        sellerMSg= (Button) findViewById(R.id.sellerMSg);
        ratingLayout= (LinearLayout) findViewById(R.id.ratingLayout);
        ratingLayout.setClickable(true);
        peopleCount=(TextView) findViewById(R.id.peopleCount);
    }

    private void getData() {
        String tag_string_req = "suggested_user";

        pDialog.setMessage("Loading ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.GET_MEMBER_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            //    Log.e("GET_MEMBER_PROFILE", "GET_MEMBER_PROFILE Response: " + response.toString());
                hideDialog();

                if (response != null) {
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
//				Toast.makeText(getActivity(),
//						error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",userId);
                params.put("uid",session.getStringSessionData("uid"));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putString("userId",userId);
        vp.mom.fragments.UserSellingFragment userseling= new vp.mom.fragments.UserSellingFragment();
        userseling.setArguments(bundle);
       adapter.addFragment(userseling, "SELLING");

        Bundle bundle1 = new Bundle();
        bundle1.putString("userId",userId);
        vp.mom.fragments.UserLIkeFragment userseling1= new vp.mom.fragments.UserLIkeFragment();
        userseling.setArguments(bundle1);
        adapter.addFragment(userseling1,"LIKES");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void parseJsonFeed(JSONObject mresponse) {
        try {

            Log.e("","");


            JSONArray feedArray = mresponse.getJSONArray("items");
                JSONObject data = (JSONObject) feedArray.get(0);
            Picasso.with(this)
                    .load(data.getString("photo"))
                    .placeholder(R.drawable.img_circle_placeholder)
                    .resize(avatarSize, avatarSize)
                    .centerCrop()
                    .transform(new CircleTransformation())
                    .into(profilepic);
            name.setText(data.getString("first_name") + " " + data.getString("last_name"));

         ///   tool_title.setText(data.getString("first_name") + " " + data.getString("last_name"));

            if(data.getBoolean("follow_status"))
                btnFollow.setText("Unfolllow");

            else
                btnFollow.setText("Folllow");


            userFollower.setText(data.getString("followers"));
            userFollowing.setText(data.getString("following"));
            peopleCount.setText(data.getString("review")+" reviews");
            rating.setRating(Float.valueOf(data.getString("ratings")));

        String username;
            if(data.isNull("username")) {
                username = "User";
            } else {
                username = data.getString("username");
            }
    try {
    userName.setText("@"+username);
    }
    catch (Exception e)
    {}

       //     }
        } catch (JSONException e) {

            Log.e("param", "Login Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addFollwing(final String userid) {



        String tag_string_req = "addFollwing";

        pDialog.setMessage("");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.USER_Follow, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("userp", "addFollwing Response: " + response);
                hideDialog();
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    if (status) {

                        btnFollow.setText(jObj.getString("btn_txt"));

                        Toast.makeText(vp.mom.searchtab.UserProfileDetailActivity.this, "Added to your following list", Toast.LENGTH_SHORT).show();
//                        SuggestedUserAdapter.this.remove(item);
//
//                        adapter.notifyDataSetChanged();
                    }
                    else{
                        btnFollow.setText(jObj.getString("btn_txt"));

                        Toast.makeText(vp.mom.searchtab.UserProfileDetailActivity.this,"Unfollowed successfully",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("addFollwing", "addFollwing Error: " + error.getMessage());
                Toast.makeText(vp.mom.searchtab.UserProfileDetailActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("to_user", userid);
                params.put("from_user", session.getStringSessionData("uid"));
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }

}
