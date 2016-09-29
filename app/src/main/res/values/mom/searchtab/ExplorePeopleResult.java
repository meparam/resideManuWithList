package vp.mom.searchtab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import vp.mom.api.AppConfig;
import vp.mom.app.AppController;
import vp.mom.data.ExplorePeoplePojo;

/**
 * Created by shivkanya.i on 30-11-2015.
 */
public class ExplorePeopleResult extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
   // View rootView;
    vp.mom.adapters.ExplorePeople mAdapter;;
    private List<ExplorePeoplePojo> feedItems;
    private ProgressDialog pDialog;
    private vp.mom.api.SessionManager session;
    //  private QuickReturnRecyclerViewOnScrollListener mScrollListener;
  //  LinearLayout mQuickReturnFooterLinearLayout;
    TextView tooltitle;
    String serchKey,itemType;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_serach_result);

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
      //  tooltitle.setText("people");

        Intent intent=getIntent();
        if(intent!=null)
        {
            serchKey=intent.getStringExtra("serachItemName");
            itemType=intent.getStringExtra("itemType");
            tooltitle.setText(serchKey);
        }


        mRecyclerView = (RecyclerView) findViewById(R.id.peopleserachlist);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        mStaggeredLayoutManager.setSpanCount(3);
        pDialog = new ProgressDialog(vp.mom.searchtab.ExplorePeopleResult.this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        session=new vp.mom.api.SessionManager(vp.mom.searchtab.ExplorePeopleResult.this);

        feedItems = new ArrayList<ExplorePeoplePojo>();
        // initialize image adapter
        mAdapter = new vp.mom.adapters.ExplorePeople(vp.mom.searchtab.ExplorePeopleResult.this,feedItems);
        mRecyclerView.setAdapter(mAdapter);

        if((serchKey != null && serchKey.length() > 0))
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
                AppConfig.SEARCH_ITEM_PEOPLE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
             //   Log.d("", "ExplorePeopleFragmentNew: " + response.toString());
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

                params.put("id", session.getStringSessionData("uid"));

             //   Log.e("pepolpe",""+session.getStringSessionData("uid"));
                params.put("type",itemType);
                params.put("searchkey", serchKey);
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
            if(response.getBoolean("status")) {
                JSONArray feedArray = response.getJSONArray("items");
                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);


                    ExplorePeoplePojo item = new ExplorePeoplePojo();
                    item.setPeopleId(feedObj.getString("id"));
                    item.setPeopleProfilePic(feedObj.getString("photo"));
                    item.setPeopleName(feedObj.getString("username"));

                    feedItems.add(item);
                }
            }
            // notify data changes to list adapater
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
