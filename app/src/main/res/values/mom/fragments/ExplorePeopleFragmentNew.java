package vp.mom.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vp.mom.R;
import vp.mom.app.AppController;
import vp.mom.data.ExplorePeoplePojo;

/**
 * Created by shivkanya.i on 30-11-2015.
 */
public class ExplorePeopleFragmentNew extends Fragment {
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    View rootView;
    vp.mom.adapters.ExplorePeople mAdapter;;
    private List<ExplorePeoplePojo> feedItems;
    private ProgressDialog pDialog;
    private vp.mom.api.SessionManager session;
  //  private QuickReturnRecyclerViewOnScrollListener mScrollListener;
    LinearLayout mQuickReturnFooterLinearLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.param, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
     //   mAdapter = new Paramadapter(getActivity());
        mStaggeredLayoutManager.setSpanCount(3);

        mQuickReturnFooterLinearLayout= (LinearLayout) rootView.findViewById(R.id.quick_return_footer_ll);

        pDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        session=new vp.mom.api.SessionManager(getActivity());

        return  rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        feedItems = new ArrayList<ExplorePeoplePojo>();
        // initialize image adapter
        mAdapter = new vp.mom.adapters.ExplorePeople(getActivity(),feedItems);

        // set image adapter to the GridView
        mRecyclerView.setAdapter(mAdapter);
        getData();


        int headerHeight = getResources().getDimensionPixelSize(R.dimen.facebook_header_height);
        int footerHeight = getResources().getDimensionPixelSize(R.dimen.facebook_footer_height);

        int headerTranslation = -headerHeight;
        int footerTranslation = -footerHeight;


    }
    private void getData() {
        String tag_string_req = "suggested_user";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.GET_ALL_ACTIVE_MEMBER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
             //  Log.d("", "ExplorePeopleFragmentNew: " + response.toString());
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
             //   Log.e("param", "Login Error: " + error.getMessage());
//				Toast.makeText(getActivity(),
//						error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                //                params.put("email", email);
                //                params.put("password", password);

                params.put("id",session.getStringSessionData("uid"));

             //   Log.e("pepolpe",""+session.getStringSessionData("uid"));
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("items");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
//
//                FeedItem item = new FeedItem();
//                item.setId(feedObj.getInt("id"));
//                item.setName(feedObj.getString("name"));
//
//                // Image might be null sometimes
//                String image = feedObj.isNull("image") ? null : feedObj
//                        .getString("image");
//                item.setImge(image);
//                item.setStatus(feedObj.getString("status"));
//                item.setProfilePic(feedObj.getString("profilePic"));
//                item.setTimeStamp(feedObj.getString("timeStamp"));

//				// url might be null sometimes
//				String feedUrl = feedObj.isNull("url") ? null : feedObj
//						.getString("url");
//
//
                //         item.setUrl("sarah@gmail.com");

                ExplorePeoplePojo item=new  ExplorePeoplePojo();
                item.setPeopleId(feedObj.getString("id"));
                item.setPeopleProfilePic(feedObj.getString("photo").replace("th_", ""));
                item.setPeopleName(feedObj.getString("username"));

                feedItems.add(item);
            }

            // notify data changes to list adapater
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
