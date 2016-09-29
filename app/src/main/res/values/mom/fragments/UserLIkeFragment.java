package vp.mom.fragments;

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
import java.util.Map;

import vp.mom.R;
import vp.mom.adapters.UserLikeAdapter;
import vp.mom.app.AppController;
import vp.mom.data.UserLikePojo;


public class UserLIkeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    UserLikeAdapter mAdapter;
    JSONObject data;
//ArrayList<SellingItem> feeeitem;

    ArrayList<UserLikePojo> userSellingArray;
    View rootview;
   // private ProgressDialog pDialog;
    String userId;
    public UserLIkeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview= inflater.inflate(R.layout.items_fragment, container, false);

        Bundle bundle = getActivity().getIntent().getExtras();

        userId = bundle.getString("userId");
        userSellingArray=new ArrayList<>();
        mRecyclerView = (RecyclerView)rootview.findViewById(R.id.sellinglist);

        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);



        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        //   mAdapter = new Paramadapter(getActivity());
        mStaggeredLayoutManager.setSpanCount(3);
     //   mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new UserLikeAdapter(getActivity(),userSellingArray);
        mRecyclerView.setAdapter(mAdapter);
        getData();
      //  parseJsonData(data);
        return  rootview;
    }

    private void getData() {
        String tag_string_req = "suggested_user";

        //pDialog.setMessage("Loading ...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.GET_USER_LIKED_PRODUCT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
             //   Log.e("", "GET_USER_SELLING_PRODUCT Response: " + response.toString());
             //   hideDialog();

                if (response != null) {
                    JSONObject jsonresponse= null;
                    try {
                        jsonresponse = new JSONObject(response.toString());
                        parseJsonData(jsonresponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
             ///   Log.e("param", "GET_USER_SELLING_PRODUCT Error: " + error.getMessage());
//				Toast.makeText(getActivity(),
//						error.getMessage(), Toast.LENGTH_LONG).show();
             //   hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                //                params.put("email", email);
                //                params.put("password", password);

                params.put("id",userId);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    private void parseJsonData(JSONObject  response) {

        try {
            JSONArray feedArray = response.getJSONArray("items");
            for (int i=0;i<feedArray.length();i++)
            {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                UserLikePojo pojo=new UserLikePojo();
                pojo.setUserID(feedObj.getString("user_id"));
                pojo.setProductID(feedObj.getString("product_id"));
                pojo.setImageUrl(feedObj.getString("path").replace("th_", "").replaceAll(" ", ""));
                pojo.setIsSold(feedObj.getString("is_sold"));
                userSellingArray.add(pojo);
            }

        }
        catch (Exception e){}

        mAdapter.notifyDataSetChanged();
    }
//    private void showDialog() {
//        if (!pDialog.isShowing())
//            pDialog.show();
//    }
//
//    private void hideDialog() {
//        if (pDialog.isShowing())
//            pDialog.dismiss();
//    }
}