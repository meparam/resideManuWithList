package vp.mom.quickreturn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import vp.mom.activitys.ItemFilterActivity;
import vp.mom.adapters.ExploreAdapter;
import vp.mom.api.AppConfig;
import vp.mom.app.AppController;
import vp.mom.data.ExplorePeopleItem;

/**
 * Created by param on 15/01/16.
 */
public class QuickReturnFacebookFragment extends Fragment {
    View rootView;
    ExploreAdapter mAdapter;;
    private List<ExplorePeopleItem> feedItems;
    private ProgressDialog pDialog;
    private vp.mom.api.SessionManager session;
    String[] color_array={"All", "Black","Blue","Brown","Green","Multi color","Orange","Beige","pink" ,
            " Purple","Red","Silver","White" ,"Gold","Non Specific"};
    private QuickReturnRecyclerViewOnScrollListener mScrollListener;
    String type="",color="",cat="",subcat="",size="";

    int MY_CHILD_ACTIVITY=2512;
    Button itemfilter;
    RecyclerView mRecyclerView;
    LinearLayout mQuickReturnFooterLinearLayout;
   // TextView mQuickReturnHeaderTextView;
    public QuickReturnFacebookFragment() {
    }

    public static vp.mom.quickreturn.QuickReturnFacebookFragment newInstance() {
        vp.mom.quickreturn.QuickReturnFacebookFragment fragment = new vp.mom.quickreturn.QuickReturnFacebookFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    // endregion

    // region Lifecycle Methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        pDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        session=new vp.mom.api.SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quick_return_facebook, container, false);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.rv);
        mQuickReturnFooterLinearLayout=(LinearLayout)view.findViewById(R.id.quick_return_footer_ll);
        //mQuickReturnHeaderTextView=(TextView)view.findViewById(R.id.quick_return_header_tv);

        itemfilter=(Button)view.findViewById(R.id.itemfilter);
        itemfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), ItemFilterActivity.class);
                startActivity(i);
                //startActivityForResult(i, MY_CHILD_ACTIVITY);
            }
        });


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
      //  Toast.makeText(getActivity(), ""+StaticData.itemCategory,Toast.LENGTH_SHORT).show();

        Log.e("onResume","itemCategory"+ vp.mom.utils.StaticData.itemCategory);
        Log.e("onResume","itemSubCategory"+ vp.mom.utils.StaticData.itemSubCategory);
        Log.e("onResume","itemcolor"+ vp.mom.utils.StaticData.itemcolor);
        Log.e("onResume","itemsize"+ vp.mom.utils.StaticData.itemsize);
        Log.e("onResume","itemType"+ vp.mom.utils.StaticData.itemType);

        type= vp.mom.utils.StaticData.itemType;
        color= vp.mom.utils.StaticData.itemcolor;
        cat= vp.mom.utils.StaticData.itemCategory;
        subcat= vp.mom.utils.StaticData.itemSubCategory;
        size= vp.mom.utils.StaticData.itemsize;

        if(vp.mom.utils.StaticData.itemFilterFlag)
        {
            getFilterData();
        }
vp.mom.utils.StaticData.itemFilterFlag=false;

        vp.mom.utils.StaticData.itemCategory="";
        vp.mom.utils.StaticData.itemSubCategory="";
        vp.mom.utils.StaticData.itemcolor="";
        vp.mom.utils.StaticData.itemsize="";
        vp.mom.utils.StaticData.itemType="";
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        feedItems = new ArrayList<ExplorePeopleItem>();
        // initialize image adapter
      //  mAdapter = new Paramadapter(getActivity(),feedItems);

        mAdapter = new ExploreAdapter(getActivity(),feedItems);

        getAllData();

        mRecyclerView.setHasFixedSize(true);
  RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),3);



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
    public void onDestroyView() {
        super.onDestroyView();

        removeListeners();

    }
    private void removeListeners() {
        mRecyclerView.removeOnScrollListener(mScrollListener);
    }

    private void getAllData() {
        String tag_string_req = "ExploreItemFragment";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_ACTIVE_PRODUCT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Login Response:", "Login Response: " + response.toString());
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

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("param", "Login Error: " + error.getMessage());
//				Toast.makeText(getActivity(),
//						error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
//                params.put("is_used",type);
//                params.put("colour",color);
//                params.put("cat",cat);
//                params.put("subcat",subcat);
//                params.put("size",size);

                params.put("id", session.getStringSessionData("uid") );
            //    Log.e("color", "color color: " +color+"   "+mtype);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void getFilterData() {
        String tag_string_req = "ExploreItemFragment";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.getAllProduct_explre, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
           //     Log.e("Login Response:", "Login Response: " + response.toString());
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

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("param", "Login Error: " + error.getMessage());
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
                //    Log.e("color", "color color: " +color+"   "+mtype);
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
                    item.setUserID(feedObj.getString("user_id"));
                    ArrayList<String> img=new ArrayList<>();
                    JSONArray imageArray = feedObj.getJSONArray("images");
                    for (int j = 0; j < imageArray.length(); j++) {

                        JSONObject imagefeed = (JSONObject) imageArray.get(j);
                        img.add(imagefeed.getString("path").replace("th_", ""));
                    }
                    item.setProduct_Image(img);
                    feedItems.add(item);
                }

            }
            // notify data changes to list adapater

        } catch (JSONException e) {

            Log.e("ExploreItemFragment", "" + e.toString());

            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
    }

}
