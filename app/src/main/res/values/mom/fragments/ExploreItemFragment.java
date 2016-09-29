package vp.mom.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import vp.mom.adapters.ExploreAdapter;
import vp.mom.api.AppConfig;
import vp.mom.app.AppController;
import vp.mom.data.ExplorePeopleItem;

/**
 * Created by ApkDev2 on 05-11-2015.
 */
public class ExploreItemFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    View rootView;
    ExploreAdapter mAdapter;;
    private List<ExplorePeopleItem> feedItems;
    private ProgressDialog pDialog;
    private vp.mom.api.SessionManager session;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.param, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        //   mAdapter = new Paramadapter(getActivity());
        mStaggeredLayoutManager.setSpanCount(3);
        pDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        session=new vp.mom.api.SessionManager(getActivity());
        feedItems = new ArrayList<ExplorePeopleItem>();
        // initialize image adapter
        mAdapter = new ExploreAdapter(getActivity(),feedItems);

        // set image adapter to the GridView
        mRecyclerView.setAdapter(mAdapter);
        getData();
        return  rootView;
    }

    private void getData() {
        String tag_string_req = "ExploreItemFragment";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_ALL_PRODUCT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
              //  Log.d("", "Login Response: " + response.toString());
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

             //   params.put("id",session.getStringSessionData("uid") );

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

                if(!feedObj.isNull("images")) {

                    ExplorePeopleItem item=new ExplorePeopleItem();
                    item.setId(feedObj.getString("id"));
                      ArrayList<String> img=new ArrayList<>();
                    JSONArray imageArray = feedObj.getJSONArray("images");
                    for (int j = 0; j < imageArray.length(); j++) {

                        JSONObject imagefeed = (JSONObject) imageArray.get(j);
                        img.add(imagefeed.getString("path"));
                    }
                    item.setProduct_Image(img);
                    feedItems.add(item);
                }

            }
            // notify data changes to list adapater

        } catch (JSONException e) {

          //  Log.e("ExploreItemFragment", "" + e.toString());

            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
    }




}
