package vp.mom.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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
import vp.mom.adapters.purchasedItemsAdapter;
import vp.mom.app.AppController;
import vp.mom.data.SalePurchaseItemPojo;

/**
 * Created by ApkDev2 on 06-11-2015.
 */
public class PurchaseItemsFragment extends Fragment {

    private GridView mGridView;
    private purchasedItemsAdapter mAdapter;
    ArrayList<SalePurchaseItemPojo> soldItemarray;
    private vp.mom.api.SessionManager session;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.purchase_item, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.gridviewpurchase);
        soldItemarray=new ArrayList<>();
        session=new vp.mom.api.SessionManager(getActivity());
        //  rb.setRating(3.5f);

        mAdapter = new purchasedItemsAdapter(getActivity(),soldItemarray);
        mGridView.setAdapter(mAdapter);

        getData();

        return  rootView;
    }
    private void getData() {
        String tag_string_req = "get_purchase_item";

        //   pDialog.setMessage("Loading ...");
        // showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.GET_PURCHASE_ITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
           //     Log.e("get_purchase_item :", "get_purchase_item : " + response.toString());
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
                Log.e("get_purchase_item", "get_purchase_item Error: " + error.getMessage());

                //  hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",session.getStringSessionData("uid"));
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    @Override
    public void onResume() {
        super.onResume();

    }
    private void parseJsonFeed(JSONObject response) {

    //    Log.e("BookMarkFragment",""+response);
        try {

         if( response.getBoolean("status")) {
             JSONArray feedArray = response.getJSONArray("items");
             for (int i = 0; i < feedArray.length(); i++) {
                 JSONObject feedObj = (JSONObject) feedArray.get(i);


                 SalePurchaseItemPojo feeditem = new SalePurchaseItemPojo();

                 if (!feedObj.isNull("product_image")) {

                     feeditem.setProduct_id(feedObj.getString("product_id"));
                     feeditem.setOrder_id(feedObj.getString("order_id"));

                     feeditem.setItemName(feedObj.getString("product_name"));
                     //   feeditem.setItemDesc(feedObj.getString("product_descr"));
                     feeditem.setItemPrice(feedObj.getString("tot_amt"));

                     if(feedObj.isNull("product_ratings"))
                         feeditem.setProductRating(0);
                     else
                     feeditem.setProductRating(Float.valueOf(feedObj.getString("product_ratings")));


                     JSONArray prodImageAarray = feedObj.getJSONArray("product_image");
                     JSONObject feedimg = (JSONObject) prodImageAarray.get(0);
                     feeditem.setItemImage(feedimg.getString("path").replace("th_", ""));

                     soldItemarray.add(feeditem);
                     //  Log.e("BookMarkFragment", "" + feedObj.getString("product_id"));
                 }
             }
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
