package vp.mom.searchtab;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import vp.mom.data.ExplorePeopleItem;
import vp.mom.quickreturn.QuickReturnRecyclerViewOnScrollListener;
import vp.mom.quickreturn.QuickReturnViewType;

/**
 * Created by param on 15/01/16.
 */
    public class ItemSerachResult extends AppCompatActivity {
    View rootView;
    vp.mom.adapters.ExploreAdapter mAdapter;;
    private List<ExplorePeopleItem> feedItems;
    private ProgressDialog pDialog;
    private vp.mom.api.SessionManager session;
    private QuickReturnRecyclerViewOnScrollListener mScrollListener;
    RadioGroup used_radio_group;
    String type="both",color="All",cat="",subcat="",size="";

    private String activityAssignedValue ="";
    int MY_CHILD_ACTIVITY=2512;
    Button itemfilter;
    RecyclerView mRecyclerView;
    LinearLayout mQuickReturnFooterLinearLayout;
  //  TextView mQuickReturnHeaderTextView;
    TextView tooltitle;
    String serchKey,itemType;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tooltitle=(TextView)findViewById(R.id.tooltitle);
        Intent intent=getIntent();
        if(intent!=null)
        {
            serchKey=intent.getStringExtra("serachItemName");
            itemType=intent.getStringExtra("itemType");
            tooltitle.setText(serchKey);
        }

        mRecyclerView=(RecyclerView)findViewById(R.id.itemsearchrv);
        mQuickReturnFooterLinearLayout=(LinearLayout)findViewById(R.id.quick_return_footer_ll_item_search);
     //   mQuickReturnHeaderTextView=(TextView)findViewById(R.id.quick_return_header_tv_item_search);

        itemfilter=(Button)findViewById(R.id.itemfilter_search);
        itemfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(vp.mom.searchtab.ItemSerachResult.this, vp.mom.activitys.ItemFilterActivity.class);
                startActivityForResult(i, MY_CHILD_ACTIVITY);
                //startActivityForResult(i, MY_CHILD_ACTIVITY);
            }
        });


        pDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        session=new vp.mom.api.SessionManager(this);
        feedItems = new ArrayList<ExplorePeopleItem>();
        mAdapter = new vp.mom.adapters.ExploreAdapter(vp.mom.searchtab.ItemSerachResult.this,feedItems);

        if((serchKey != null && serchKey.length() > 0))
        getData();

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(vp.mom.searchtab.ItemSerachResult.this,3);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mAdapter);
        //  mRecyclerView.addItemDecoration(new SpacesItemDecoration(QuickReturnUtils.dp2px(getActivity(), 8)));

        int headerHeight = getResources().getDimensionPixelSize(R.dimen.facebook_header_height);
        int footerHeight = getResources().getDimensionPixelSize(R.dimen.facebook_footer_height);

        int headerTranslation = -headerHeight;
        int footerTranslation = -footerHeight;

        mScrollListener = new QuickReturnRecyclerViewOnScrollListener.Builder(QuickReturnViewType.BOTH)

                .minHeaderTranslation(headerTranslation)
                .footer(mQuickReturnFooterLinearLayout)
                .minFooterTranslation(-footerTranslation)
                .isSnappable(true)
                .build();
        mRecyclerView.addOnScrollListener(mScrollListener);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent pData) {
       // super.onActivityResult(requestCode, resultCode, pData);
        super.onActivityResult(requestCode, resultCode, pData);
     //   Toast.makeText(ItemSerachResult.this, "hello", 1000).show();
                if (resultCode == Activity.RESULT_OK&&requestCode==MY_CHILD_ACTIVITY) {
                   // Toast.makeText(ItemSerachResult.this,"ok fine",1000).show();
                    type= vp.mom.utils.StaticData.itemType;
                    color= vp.mom.utils.StaticData.itemcolor;
                    cat= vp.mom.utils.StaticData.itemCategory;
                    subcat= vp.mom.utils.StaticData.itemSubCategory;
                    size= vp.mom.utils.StaticData.itemsize;

                    if(vp.mom.utils.StaticData.itemFilterFlag)
                    {
                        getData();
                    }
                    Log.e("onActivityResult","itemCategory"+ vp.mom.utils.StaticData.itemCategory);
                    Log.e("onActivityResult","itemSubCategory"+ vp.mom.utils.StaticData.itemSubCategory);
                    Log.e("onActivityResult","itemcolor"+ vp.mom.utils.StaticData.itemcolor);
                    Log.e("onActivityResult","itemsize"+ vp.mom.utils.StaticData.itemsize);
                    Log.e("onActivityResult","itemType"+ vp.mom.utils.StaticData.itemType);

                       }
           }



    @Override
    public void onResume() {
        super.onResume();
      //  Toast.makeText(getActivity(), ""+StaticData.itemCategory,Toast.LENGTH_SHORT).show();


//        type=StaticData.itemType;
//        color=StaticData.itemcolor;
//        cat=StaticData.itemCategory;
//        subcat=StaticData.itemSubCategory;
//        size=StaticData.itemsize;
//
//        if(StaticData.itemFilterFlag)
//        {
//            getData();
//        }

    }
//    @Override
//    public void onDestroyView() {
//
//
//        removeListeners();
//
//    }
    private void removeListeners() {
        mRecyclerView.removeOnScrollListener(mScrollListener);
    }

    private void getData() {
        String tag_string_req = "SEARCH_ITEM_PEOPLE";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.SEARCH_ITEM_PEOPLE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
            //    Log.e("SEARCH_ITEM Response:", "SEARCH_ITEM Response: " + response.toString());
                hideDialog();

                if (response != null) {

                    feedItems.clear();
                    JSONObject jsonresponse= null;
                    try {
                        jsonresponse = new JSONObject(response.toString());

                        parseJsonFeed(jsonresponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    vp.mom.utils.StaticData.itemFilterFlag=false;

                    vp.mom.utils.StaticData.itemCategory="";
                    vp.mom.utils.StaticData.itemSubCategory="";
                    vp.mom.utils.StaticData.itemcolor="";
                    vp.mom.utils.StaticData.itemsize="";
                    vp.mom.utils.StaticData.itemType="";
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
             //   Log.e("SEARCH_ITEM_PEOPLE", "SEARCH_ITEM_PEOPLE Error: " + error.getMessage());
//				Toast.makeText(getActivity(),
//						error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("is_used",type);
                params.put("colour",color);
                params.put("cat",cat);
                params.put("subcat",subcat);
                params.put("size",size);
                params.put("id", session.getStringSessionData("uid") );

                params.put("type",itemType);
                params.put("searchkey",serchKey);
            //    Log.e("color", "color color: " +color+"   "+mtype);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    //    private void getData() {
//        String tag_string_req = "suggested_user";
//
//        pDialog.setMessage("Loading ...");
//        showDialog();
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                AppConfig.GET_ALL_MEMBER, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                //  Log.d("", "Login Response: " + response.toString());
//                hideDialog();
//
//                if (response != null) {
//
//
//                    JSONObject jsonresponse= null;
//                    try {
//                        jsonresponse = new JSONObject(response.toString());
//
//                        parseJsonFeed(jsonresponse);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("param", "Login Error: " + error.getMessage());
////				Toast.makeText(getActivity(),
////						error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
//                //                params.put("email", email);
//                //                params.put("password", password);
//
//                params.put("id",session.getStringSessionData("uid"));
//
//                Log.e("pepolpe",""+session.getStringSessionData("uid"));
//                return params;
//            }
//
//        };
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//
//    }
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
     //   Log.e("SEARCH_ITEM:", "SEARCH_ITEM Response: " + response.toString());
        try {

            if(response.getBoolean("status")) {
                JSONArray feedArray = response.getJSONArray("items");
                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);

                    if (!feedObj.isNull("product_image")) {

                        ExplorePeopleItem item = new ExplorePeopleItem();
                        item.setId(feedObj.getString("id"));
                        item.setUserID(feedObj.getString("user_id"));
                        ArrayList<String> img = new ArrayList<>();
                        JSONArray imageArray = feedObj.getJSONArray("product_image");
                        for (int j = 0; j < imageArray.length(); j++) {

                            JSONObject imagefeed = (JSONObject) imageArray.get(j);
                            img.add(imagefeed.getString("path").replace("th_", ""));
                        }
                        item.setProduct_Image(img);
                        feedItems.add(item);
                    }

                }
                // notify data changes to list adapater
            }
        } catch (JSONException e) {

            Log.e("ExploreItemFragment", "" + e.toString());

            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
    }



    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
//    private void parseJsonFeed(JSONObject response) {
//        try {
//            JSONArray feedArray = response.getJSONArray("items");
//
//            for (int i = 0; i < feedArray.length(); i++) {
//                JSONObject feedObj = (JSONObject) feedArray.get(i);
//
//                SuggestedItem item=new  SuggestedItem();
//                item.setUserid(feedObj.getString("id"));
//                item.setFname(feedObj.getString("first_name"));
//                item.setLname(feedObj.getString("last_name"));
//                item.setEmail(feedObj.getString("email"));
//                item.setProfile_image(feedObj.getString("photo"));
//
//                feedItems.add(item);
//            }
//
//            // notify data changes to list adapater
//            mAdapter.notifyDataSetChanged();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
    // endregion
}
