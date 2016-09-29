package vp.mom.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vp.mom.R;
import vp.mom.adapters.NotificationNewAdapter;
import vp.mom.app.AppController;
import vp.mom.data.NotificationPojo;

/**
 * Created by ApkDev2 on 07-11-2015.
 */
    public class NotoficationFragment extends Fragment {
    private SwipeMenuListView mListView;
    private NotificationNewAdapter mAdapter;
    ArrayList<NotificationPojo> bookMArkList;
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
        mAdapter = new NotificationNewAdapter(getActivity(),bookMArkList);
        mListView.setAdapter(mAdapter);
        getData();
        return rootView;
    }
    private void getData() {
        AppController.getInstance().getPrefManager().clear();
        String tag_string_req = "NOTIFICATION_LIST";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.NOTIFICATION_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             //   Log.e("NOTIFICATION Response:", "NOTIFICATION Response: " + response.toString());
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
                Log.e("param", "Login Error: " + error.getMessage());

                //  hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid",session.getStringSessionData("uid"));
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

       Log.e("BookMarkFragment",""+response);
        try {
            JSONArray feedArray = response.getJSONArray("items");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                JSONArray otheruserArray = feedObj.getJSONArray("From_user");
                JSONObject otherUsrrData = (JSONObject) otheruserArray.get(0);

                NotificationPojo feedItem=new NotificationPojo();

                if(!feedObj.getString("product_id").equalsIgnoreCase("0"))
                {

                    JSONObject imageObj= feedObj.getJSONObject("product_details");
                    feedItem.setProductID(feedObj.getString("product_id"));

                    feedItem.setProducrImage(imageObj.getString("path"));
                }
                else
                {
                    feedItem.setProductID(feedObj.getString("product_id"));
                }


                feedItem.setNotificationMsg(feedObj.getString("PNMsg"));
               // feedItem.setTime(feedObj.getString("NDT"));
                feedItem.setUserImage(otherUsrrData.getString("photo"));
                feedItem.setUserName(otherUsrrData.getString("first_name") + " " + otherUsrrData.getString("last_name"));

                feedItem.setUserId(feedObj.getString("Ouid"));
                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                        Long.parseLong(feedObj.getString("timeStamp")),
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                feedItem.setTime("" + timeAgo);

//                BookMarkPojo feeditem=new BookMarkPojo();
//
//                feeditem.setProductId(feedObj.getString("product_id"));
//                feeditem.setProductTime(feedObj.getString("created"));
//                feeditem.setProductDisc(feedObj.getString("description"));
//                feeditem.setProductUrl(feedObj.getString("path"));

                bookMArkList.add(feedItem);
              //  Log.e("BookMarkFragment", "" + feedObj.getString("product_id"));
            }
            // notify data changes to list adapater

        } catch (JSONException e) {

            Log.e("BookMarkFragment",""+e.toString());

            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
        // swipeRefreshLayout.setRefreshing(false);
    }
}


