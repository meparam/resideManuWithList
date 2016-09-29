package vp.mom.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.Map;

import vp.mom.R;
import vp.mom.adapters.ChartsAdapter;
import vp.mom.app.AppController;
import vp.mom.data.SalePurchaseItemPojo;

/**
 * Created by ApkDev2 on 05-11-2015.
 */
public class SoldItemsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ChartsAdapter mAdapter;
    View rootView;
    ArrayList<SalePurchaseItemPojo> soldItemarray;
    private vp.mom.api.SessionManager session;
  //  private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.sold_items_xml, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.grid_view);
        soldItemarray = new ArrayList<>();
        session = new vp.mom.api.SessionManager(getActivity());

        GridLayoutManager glm = new GridLayoutManager(getActivity(), 2);
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        glm.setSpanCount(screenWidth / (getResources().getDimensionPixelOffset(R.dimen.column_width_main_recyclerview)));
        mRecyclerView.setLayoutManager(glm);
        getData();

        return rootView;
    }

    private void getData() {
        String tag_string_req = "get_sold_item";

        //   pDialog.setMessage("Loading ...");
        // showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.GET_SOLD_ITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            //    Log.e("get_sold_item Response:", "get_sold_item Response: " + response.toString());
                //   hideDialog();
                if (response != null) {

                    // commentData.clear();
                    JSONObject jsonresponse = null;
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
             //   Log.e("get_sold_item", "get_sold_item Error: " + error.getMessage());

                //  hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", session.getStringSessionData("uid"));
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void parseJsonFeed(JSONObject response) {

        Log.e("get_sold_item", "" + response);
        try {
            JSONArray feedArray = response.getJSONArray("items");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);


                SalePurchaseItemPojo feeditem = new SalePurchaseItemPojo();

                if (!feedObj.isNull("product_image")) {

                    feeditem.setProductRating(feedObj.getInt("product_ratings"));
                    //  feeditem.setProductRating(2);

                    feeditem.setItemName(feedObj.getString("product_name"));
                    //   feeditem.setItemDesc(feedObj.getString("disc"));
                    feeditem.setItemPrice(feedObj.getString("tot_amt"));

                    JSONArray prodImageAarray = feedObj.getJSONArray("product_image");
                    JSONObject feedimg = (JSONObject) prodImageAarray.get(0);
                    feeditem.setItemImage(feedimg.getString("path").replace("th_", ""));
                    feeditem.setProduct_id(feedObj.getString("product_id"));

                    feeditem.setOrder_id(feedObj.getString("order_id"));
                    feeditem.setOrder_detailed_Id(feedObj.getString("ord_det_id"));

                    feeditem.setDelivery_status(feedObj.getString("delivery_status"));

                    soldItemarray.add(feeditem);
                    //    Log.e("BookMarkFragment", "" + feedObj.getString("product_id"));
                }
            }
            // notify data changes to list adapater

        } catch (JSONException e) {

            Log.e("BookMarkFragment", "" + e.toString());

            e.printStackTrace();
        }
        mAdapter = new ChartsAdapter(getActivity(), soldItemarray);
        mRecyclerView.setAdapter(mAdapter);
        // swipeRefreshLayout.setRefreshing(false);
    }
}
