package vp.mom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import vp.mom.activitys.SearchActivity;
import vp.mom.fragments.SettingFragment;
import vp.mom.searchtab.UserProfileDetailActivity;

public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String msg,chatRoomId,userName;
    private vp.mom.gcm.gcm.NotificationUtils notificationUtils;
    String TITLES[] = {"Home","My profile", "Explore", "Sell an item","Notification","Bookmarks","Sold Items","Purchased Item","Invite Friend","Help","Settings"};
    int ICONS[] = {R.drawable.nav_home,R.drawable.profile_icon ,R.drawable.nav_search, R.drawable.nav_sell,R.drawable.nav_notifications,
            R.drawable.nav_bookmarks,R.drawable.nav_solditems,R.drawable.purchaseditems,R.drawable.facebook_icon,R.drawable.nav_help,
            R.drawable.nav_settings};

    String NAME = "Sarah Parker";
    String EMAIL = "paramvir.s@edreamz.in";
    String WEBSITE = "Sarah@gmail.com";
    int PROFILE = R.drawable.app_logo;
    private Toolbar toolbar;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;
    int lastMenu = -1;
    ActionBarDrawerToggle mDrawerToggle;
    ImageView mainmessge,mainsearch;
    private vp.mom.api.SessionManager session;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        title  = getString(R.string.app_name);
        setContentView(R.layout.activity_main);
        session=new vp.mom.api.SessionManager(this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mainmessge=(ImageView)toolbar.findViewById(R.id.mainmessge);
        mainsearch=(ImageView)toolbar.findViewById(R.id.mainsearch);
        mainmessge.setClickable(true);
        mainsearch.setClickable(true);
        mainsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent msg = new Intent(vp.mom.MainActivity.this, SearchActivity.class);
                startActivity(msg);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            }

        });

        mainmessge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent msg=new Intent(vp.mom.MainActivity.this, vp.mom.activitys.UserChatList.class);
                startActivity(msg);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            }

        });

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new vp.mom.adapters.MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE,WEBSITE,this);

        mRecyclerView.setAdapter(mAdapter);

        // get the fadein animation
        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        final ImageView bgImage = (ImageView)findViewById(R.id.bg_image);

        // fade in the background image
        bgImage.startAnimation(animationFadeIn);

        final GestureDetector mGestureDetector = new GestureDetector(vp.mom.MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());

                if(child != null && mGestureDetector.onTouchEvent(motionEvent)){
                    Drawer.closeDrawers();
//                    Toast.makeText(FacebookList.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();

                    onTouchDrawer(recyclerView.getChildPosition(child));
                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);

        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        Intent intent=getIntent();

    if(intent!=null)
    {
        if(intent.getBooleanExtra("soldItemCalled",false))
        {
            onTouchDrawer(7);
        }
        else if(intent.getBooleanExtra("NotificationCalled",false))
            onTouchDrawer(5);
        else
            onTouchDrawer(1);
    }
            else
        onTouchDrawer(1);
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
        }
        long when = System.currentTimeMillis();
        // app is in background. show the message in notification try
        Intent resultIntent = new Intent(getApplicationContext(), vp.mom.gcm.gcm.ChatRoomActivity.class);
        resultIntent.putExtra("UserToChat", chatRoomId);
        resultIntent.putExtra("name", userName);
        showNotificationMessage(getApplicationContext(), userName, msg,
                "" + when, resultIntent);

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openFragment(final Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        getSupportActionBar().setTitle(title);
    }
    public void onTouchDrawer(final int position) {
       // if (lastMenu == position) return;

        switch (lastMenu = position) {
            case 0:
                Intent uerpupdate=new Intent(vp.mom.MainActivity.this, vp.mom.activitys.UserProfileUpdate.class);
                startActivity(uerpupdate);

                break;
            case 1:
                title="Home";
                openFragment(new vp.mom.fragments.HomeListView());

                break;
            case 2:
                Intent msg=new Intent(this, UserProfileDetailActivity.class);
                msg.putExtra("userId",session.getStringSessionData("uid"));
                msg.putExtra("userItself",true);
                startActivity(msg);
                break;
            case 3:
                title="Explore";
                openFragment(new vp.mom.fragments.ExploreMainFragment());

                break;
            case 4:
                Intent seelingintent=new Intent(vp.mom.MainActivity.this, vp.mom.activitys.SellingActivity.class);
                startActivity(seelingintent);
                break;
            case 5:
                title="Notification";
                openFragment(new vp.mom.fragments.NotoficationFragment());

                break;
            case 6:
                title="BookMarks";
                openFragment(new vp.mom.fragments.BookMarkFragment());

                break;
            case 7:
                title="Sold items";
                openFragment(new vp.mom.fragments.SoldItemsFragment());

                break;
            case 8:
                title="Purchased items";
                openFragment(new vp.mom.fragments.PurchaseItemsFragment());

                break;
            case 9:
                title="Invite friend";
                try
                { Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Market of mums");
                    String sAux = "\n Hi i  found the interesting app \n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=vp.mom \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Share via..."));
                }
                catch(Exception e)
                { //e.toString();
                }

                break;
            case 10:
                title="Help";
                openFragment(new vp.mom.fragments.HelpFragment());

                break;
            case 11:
             //   Toast.makeText(FacebookList.this,"setting",1000).show();
                Intent setting=new Intent(vp.mom.MainActivity.this, SettingFragment.class);
                startActivity(setting);
                break;
            default: break;
        }
    }
}
