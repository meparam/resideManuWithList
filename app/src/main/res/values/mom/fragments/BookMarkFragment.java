package vp.mom.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import vp.mom.activitys.ProductDetails;
import vp.mom.adapters.BookMarkAdapter;
import vp.mom.app.AppController;

/**
 * Created by ApkDev2 on 07-11-2015.
 */
public class BookMarkFragment extends Fragment {

    private SwipeMenuListView mListView;
    private BookMarkAdapter mAdapter;
ArrayList<vp.mom.data.BookMarkPojo> bookMArkList;
    private vp.mom.api.SessionManager session;
    View rootView;
    RelativeLayout errorLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.bookmarks_xml, container, false);
        bookMArkList=new ArrayList<>();
        session=new vp.mom.api.SessionManager(getActivity());
        mListView = (SwipeMenuListView) rootView.findViewById(R.id.bookmarklistview);

        errorLayout= (RelativeLayout) rootView.findViewById(R.id.error_layout);
        errorLayout.setVisibility(View.GONE);

        mAdapter = new BookMarkAdapter(getActivity(),bookMArkList);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vp.mom.data.BookMarkPojo feeditem = bookMArkList.get(position);
//                Intent seelingintent = new Intent(getActivity(), ProductDetails.class);
//                seelingintent.putExtra("productId", feeditem.getProductId());
//                seelingintent.putExtra("calledUserId", item.getUserID());
//             getActivity().startActivity(seelingintent);

            }
        });


        getData();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                vp.mom.data.BookMarkPojo bookmarkData = bookMArkList.get(position);
                Intent seelingintent = new Intent(getActivity(), ProductDetails.class);
                seelingintent.putExtra("productId", bookmarkData.getProductId());
                seelingintent.putExtra("calledUserId",bookmarkData.getUserID());
                startActivity(seelingintent);

            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity());
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

                // create "delete" item
//                SwipeMenuItem deleteItem = new SwipeMenuItem(
//                        getActivity());
                // set item background
//                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                        0x3F, 0x25)));
//                // set item width
//                deleteItem.setWidth(dp2px(60));
//                // set a icon
//                deleteItem.setIcon(R.drawable.ic_delete);
//                // add to menu
//                menu.addMenuItem(deleteItem);
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


                        Remove_Bookmark(position);

                        break;

                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        return rootView;
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
    private void getData() {
        String tag_string_req = "ExploreItemFragment";

     //   pDialog.setMessage("Loading ...");
        // showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.GET_BOOKMARK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  Log.e("GET_BOOKMARK Response:", "GET_BOOKMARK Response: " + response.toString());
                //   hideDialog();
                if (response != null) {

                    // commentData.clear();
                    JSONObject jsonresponse= null;
                    try {
                        jsonresponse = new JSONObject(response.toString());

                        if(jsonresponse.getBoolean("status"))
                        {
                            mListView.setVisibility(View.VISIBLE);
                            errorLayout.setVisibility(View.GONE);
                            parseJsonFeed(jsonresponse);

                        }
                        else
                        {
                            mListView.setVisibility(View.GONE);
                            errorLayout.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
              //  Log.e("param", "/in Error: " + error.getMessage());

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
  /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {

     //   Log.e("BookMarkFragment",""+response);
        try {
            JSONArray feedArray = response.getJSONArray("items");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                vp.mom.data.BookMarkPojo feeditem=new vp.mom.data.BookMarkPojo();
                feeditem.setProductId(feedObj.getString("product_id"));
                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                        Long.parseLong(feedObj.getString("timeStamp")),
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                feeditem.setProductTime("" + timeAgo);
              //  feeditem.setProductTime(feedObj.getString("created"));
                feeditem.setUserID(feedObj.getString("user_id"));
                feeditem.setProductDisc(feedObj.getString("name"));
                feeditem.setProductUrl(feedObj.getString("path").replace("th_", ""));

                feeditem.setBookMarkId(feedObj.getString("bookmark_id"));
                bookMArkList.add(feeditem);
              //  Log.e("BookMarkFragment", "" + feedObj.getString("product_id"));
            }
            // notify data changes to list adapater

        } catch (JSONException e) {

         //   Log.e("BookMarkFragment",""+e.toString());

            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
        // swipeRefreshLayout.setRefreshing(false);
    }

    private void Remove_Bookmark(final int position) {
        String tag_string_req = "Remove_Bookmark";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.REMOVE_BOOKMARK_BY_ID, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
           //     Log.e("Remove_Bookmark", "Remove_Bookmark Response: " + response.toString());
                //	hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {

                        bookMArkList.remove(position);
                        mAdapter.notifyDataSetChanged();

                        // user successfully logged in
                     //   Log.e("Remove_comment", "Remove_comment Response: " + response.toString());

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("items");
                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_LONG).show();

//                        commentData.remove(position);
//                        notifyItemRemoved(position);
//                        notifyItemRangeChanged(position, commentData.size());
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
             //   Log.e("Remove_Bookmark", "Remove_comment Error: " + error.getMessage());
                Toast.makeText(getActivity(),
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



                vp.mom.data.BookMarkPojo feeditem=bookMArkList.get(position);

                params.put("user_id",session.getStringSessionData("uid"));
                params.put("bookmark_id",feeditem.getBookMarkId());



                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}


