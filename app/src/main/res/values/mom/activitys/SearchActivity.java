package vp.mom.activitys;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import vp.mom.R;
import vp.mom.SearchDataBase;
import vp.mom.searchtab.ItemSearchFragment;


public class SearchActivity extends AppCompatActivity {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String msg,chatRoomId,userName;
    private vp.mom.gcm.gcm.NotificationUtils notificationUtils;
  //  private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
   // ImageView back;
  //  TextView tool_title;
   SearchDataBase db;
   ArrayList<String> autotextHintArray;
//  AutoCompleteTextView myAutoComplete;
    SearchView search;
    ArrayAdapter<String> My_arr_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view);
         db = new SearchDataBase(vp.mom.activitys.SearchActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
     //   back=(ImageView)toolbar.findViewById(R.id.ic_back);

        search=(SearchView) findViewById(R.id.searchView1);
        search.setQueryHint("Search");
        search.setIconified(false);
        //*** setOnQueryTextFocusChangeListener ***
//        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // TODO Auto-generated method stub
//
//
//            }
//        });

        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

            int pos=    viewPager.getCurrentItem();

                if(pos==0)
                {

                    try
                    {
                        db.addseracheItem(query);

                    }
                    catch (Exception exp){

                    }

                    Intent msg = new Intent(vp.mom.activitys.SearchActivity.this, vp.mom.searchtab.ItemSerachResult.class);
                    msg.putExtra("serachItemName",query);
                    msg.putExtra("itemType","1");
                    startActivity(msg);
                    overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                }
                else
                {
                    try
                    {
                        db.addserachePeople(query);

                    }
                    catch (Exception exp){

                    }
                    Intent msg = new Intent(vp.mom.activitys.SearchActivity.this, vp.mom.searchtab.ExplorePeopleResult.class);
                    msg.putExtra("serachItemName",query);
                    msg.putExtra("itemType","2");
                    startActivity(msg);
                    overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                }

                search.setQuery("", false);
                search.clearFocus();
//                Toast.makeText(getBaseContext(),query+" "+pos,
//                        Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub

//                	Toast.makeText(getBaseContext(), newText,
//                Toast.LENGTH_SHORT).show();
                return false;
            }
        });

//        back.setClickable(true);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.search_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.searchtabs);
        tabLayout.setupWithViewPager(viewPager);

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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ItemSearchFragment(), "Item");
        adapter.addFragment(new vp.mom.searchtab.PeopleSearchFragment(), "People");
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
}
