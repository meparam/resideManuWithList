package vp.mom.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vp.mom.R;
import vp.mom.adapters.FollowingAdapter;
import vp.mom.app.AppController;
import vp.mom.data.FollowerPojo;
import vp.mom.searchtab.UserProfileDetailActivity;

/**
 * Created by ApkDev2 on 07-11-2015.
 */
public class FollowingListActivity extends AppCompatActivity {

    private SwipeMenuListView mListView;
    private FollowingAdapter mAdapter;
ArrayList<FollowerPojo> bookMArkList;
    private vp.mom.api.SessionManager session;
   // View rootView;
   TextView tool_title;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follower_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        back=(ImageView)toolbar.findViewById(R.id.ic_back);
//        back.setClickable(true);
        tool_title = (TextView) toolbar.findViewById(R.id.tootlbar_title);
        tool_title.setText("Followed People");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bookMArkList=new ArrayList<>();
        session=new vp.mom.api.SessionManager(vp.mom.activitys.FollowingListActivity.this);
        mListView = (SwipeMenuListView) findViewById(R.id.Followerlistview);


        mAdapter = new FollowingAdapter(this,bookMArkList);
        mListView.setAdapter(mAdapter);

        Intent myIntent=getIntent();

        if(myIntent!=null)
        {

            userID=myIntent.getStringExtra("userId");

            getData();


        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FollowerPojo followeData = bookMArkList.get(position);
                Intent msg=new Intent(vp.mom.activitys.FollowingListActivity.this, UserProfileDetailActivity.class);
                msg.putExtra("userId",followeData.getUserId());
                msg.putExtra("userItself",false);
                startActivity(msg);

            }
        });
      //  getData();
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        vp.mom.activitys.FollowingListActivity.this);
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(100));
                // set item title
                openItem.setTitle("Remove");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);


            }
        };

// set creator
        mListView.setMenuCreator(creator);
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        mListView.setOnMenuItemClickListener(new  SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:


                        //  Remove_Bookmark(position);

                        break;

                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });


        }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        rootView = inflater.inflate(R.layout.follower_list, container, false);
//
//        return rootView;
//    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
    private void getData() {
        String tag_string_req = "ExploreItemFragment";

     //   pDialog.setMessage("Loading ...");
        // showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.GET_FOLLOWING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
           //     Log.e("GET_BOOKMARK Response:", "GET_BOOKMARK Response: " + response.toString());
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
             //   Log.e("param", "Login Error: " + error.getMessage());

                //  hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",userID);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
  /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {

      //  Log.e("BookMarkFragment",""+response);
        try {

            if(response.getBoolean("status")) {
                JSONArray feedArray = response.getJSONArray("items");
                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);


                    FollowerPojo feedItem = new FollowerPojo();

                    feedItem.setUserId(feedObj.getString("to_user"));
                    feedItem.setUserName(feedObj.getString("first_name") + " " + feedObj.getString("last_name"));
                    feedItem.setImageUrl(feedObj.getString("photo"));
                    feedItem.setTimeStamp(feedObj.getString("timeStamp"));


                    bookMArkList.add(feedItem);
                  //  Log.e("BookMarkFragment", "" + feedObj.getString("product_id"));
                }
            }
            // notify data changes to list adapater

        } catch (JSONException e) {

        //    Log.e("BookMarkFragment",""+e.toString());

            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
        // swipeRefreshLayout.setRefreshing(false);
    }

//    private void Remove_Bookmark(final int position) {
//        String tag_string_req = "Remove_Bookmark";
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                AppConfig.REMOVE_BOOKMARK_BY_ID, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.e("Remove_Bookmark", "Remove_Bookmark Response: " + response.toString());
//                //	hideDialog();
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    boolean status = jObj.getBoolean("status");
//                    // Check for error node in json
//                    if (status) {
//
//                        bookMArkList.remove(position);
//                        mAdapter.notifyDataSetChanged();
//
//                        // user successfully logged in
//                        Log.e("Remove_comment", "Remove_comment Response: " + response.toString());
//
//                    } else {
//                        // Error in login. Get the error message
//                        String errorMsg = jObj.getString("items");
//                        Toast.makeText(getActivity(),
//                                errorMsg, Toast.LENGTH_LONG).show();
//
////                        commentData.remove(position);
////                        notifyItemRemoved(position);
////                        notifyItemRangeChanged(position, commentData.size());
//                    }
//                } catch (JSONException e) {
//                    // JSON error
//                    e.printStackTrace();
//                    Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("Remove_Bookmark", "Remove_comment Error: " + error.getMessage());
//                Toast.makeText(getActivity(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                //	hideDialog();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
////                params.put("email", email);
////                params.put("password", password);
//
//
//
//                BookMarkPojo feeditem=bookMArkList.get(position);
//
//                params.put("user_id",session.getStringSessionData("uid"));
//                params.put("bookmark_id",feeditem.getBookMarkId());
//
//
//
//                return params;
//            }
//
//        };
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//    }
}


