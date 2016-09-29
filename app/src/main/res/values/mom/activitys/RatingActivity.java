package vp.mom.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
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
import java.util.Map;

import vp.mom.R;
import vp.mom.adapters.RatingAdapter;
import vp.mom.api.AppConfig;
import vp.mom.app.AppController;

/**
 * Created by ApkDev2 on 07-11-2015.
 */
    public class RatingActivity extends AppCompatActivity {

    private ListView mListView;
    private RatingAdapter mAdapter;
    ArrayList<vp.mom.data.RatingPojo> ratingList;
    private vp.mom.api.SessionManager session;
  //  RelativeLayout errorLayout;
    private  String calledUserID;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_view);
        ratingList=new ArrayList<>();
        session=new vp.mom.api.SessionManager(this);
        initActionBar();
        mListView = (ListView)findViewById(R.id.ratinglistview);
//        errorLayout= (RelativeLayout) findViewById(R.id.error_layout);
//        errorLayout.setVisibility(View.GONE);
        mAdapter = new RatingAdapter(this,ratingList);
        mListView.setAdapter(mAdapter);

        Intent intent=getIntent();
        if(intent!=null)
        {
            calledUserID=intent.getStringExtra("userID");
            getData();
        }
        else
        {
         finish();
        }
    }
    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title= (TextView) toolbar.findViewById(R.id.tooltitle);
        title.setText("Reviews");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getData() {
        String tag_string_req = "RATING_LIST";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.RATING_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             //   Log.e("RATING_LIST Response:", "RATING_LIST Response: " + response.toString());
                if (response != null) {

                    JSONObject jsonresponse= null;
                    try {
                        jsonresponse = new JSONObject(response.toString());

                        if(jsonresponse.getBoolean("status"))
                        {
                            mListView.setVisibility(View.VISIBLE);
                           // errorLayout.setVisibility(View.GONE);
                            parseJsonFeed(jsonresponse);

                        }
                        else
                        {
                            mListView.setVisibility(View.GONE);
                         //   errorLayout.setVisibility(View.VISIBLE);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
              //  Log.e("RATING_LIST", "RATING_LIST Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid",calledUserID);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
  /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {

     //   Log.e("BookMarkFragment", "" + response);
        try {
            JSONArray feedArray = response.getJSONArray("items");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                vp.mom.data.RatingPojo data=new vp.mom.data.RatingPojo();

                data.setUserID(feedObj.getString("user_id"));
                data.setUserName(feedObj.getString("username"));
                data.setUserImage(feedObj.getString("photo"));
                data.setProductImage(feedObj.getString("images"));
                data.setProductId(feedObj.getString("product_id"));
                data.setRating(feedObj.getString("ratings"));
                data.setRatingDisc(feedObj.getString("review"));

                ratingList.add(data);
            }

        } catch (JSONException e) {

        //    Log.e("BookMarkFragment",""+e.toString());

            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
    }
}


