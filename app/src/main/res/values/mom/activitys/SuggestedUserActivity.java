package vp.mom.activitys;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vp.mom.R;


/**
 * Created by param on 07-11-2015.
 */
public class SuggestedUserActivity extends AppCompatActivity {

    private static final String TAG = vp.mom.activitys.SuggestedUserActivity.class.getSimpleName();
    private ListView listView;
    private vp.mom.adapters.SuggestedUserAdapter listAdapter;
    private List<vp.mom.data.SuggestedItem> feedItems;
  //  private String URL_FEED = AppConfig.GET_ALL_MEMBER;
    private ProgressDialog pDialog;
    private vp.mom.api.SessionManager session;
    LinearLayout sugesstmaill;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    RelativeLayout error_layout;
    LinearLayout suggestedmainll;
    int pageCount=1;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suggested_user);

        pDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        session=new vp.mom.api.SessionManager(vp.mom.activitys.SuggestedUserActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

      //  TextView done= (TextView) toolbar.findViewById(R.id.txtsuggestuserDone);

        sugesstmaill= (LinearLayout) toolbar.findViewById(R.id.sugesstmaill);

        listView = (ListView) findViewById(R.id.suggestedlistview);

        error_layout= (RelativeLayout) findViewById(R.id.error_layout);
        error_layout.setVisibility(View.GONE);
        suggestedmainll= (LinearLayout) findViewById(R.id.suggestedmainll);

        feedItems = new ArrayList<vp.mom.data.SuggestedItem>();

        listAdapter = new vp.mom.adapters.SuggestedUserAdapter(this, feedItems);
        listView.setAdapter(listAdapter);

        sugesstmaill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seelingintent = new Intent(vp.mom.activitys.SuggestedUserActivity.this, vp.mom.MainActivity.class);
                startActivity(seelingintent);
                finish();
            }
        });
    getData();


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,

                                 int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;

                Log.e("firstVisibleItem", "firstVisibleItem" + firstVisibleItem);
                Log.e("visibleItemCount", "visibleItemCount" + visibleItemCount);
                Log.e("totalItemCount", "totalItemCount" + totalItemCount);

                if (firstVisibleItem == 18) {
                    pageCount += 1;
                    getData();
                }


//                if ((lastInScreen == totalItemCount) && !(loadingMore)) {
//
//
//                    pageCount+=1;
//                    getHomeListData();
//                }

            }

        });
//        Cache cache = AppController.getInstance().getRequestQueue().getCache();
//        Cache.Entry entry = cache.get(URL_FEED);
//        if (entry != null) {
//            // fetch the data from cache
//            try {
//                String data = new String(entry.data, "UTF-8");
//                try {
//                    parseJsonFeed(new JSONObject(data));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            // making fresh volley request and getting json
//            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST,
//                    URL_FEED, null, new Response.Listener<JSONObject>() {
//
//                @Override
//                public void onResponse(JSONObject response) {
//                    VolleyLog.d(TAG, "Response: " + response.toString());
//                    if (response != null) {
//                        parseJsonFeed(response);
//                    }
//                }
//            }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    VolleyLog.d(TAG, "Error: " + error.getMessage());
//                }
//            }){
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
////                params.put("email", email);
////                params.put("password", password);
//
//
//
//
//                return params;
//            }
//            };
//
//
//            // Adding request to volley request queue
//            AppController.getInstance().addToRequestQueue(jsonReq);
//        }

//        if (checkPlayServices()) {
//            registerGCM();
//            fetchChatRooms();
//        }


        if (checkPlayServices()) {
            registerGCM();

        }
    }

    // starting the service to register with GCM
    private void registerGCM() {
        Intent intent = new Intent(this, vp.mom.gcm.gcm.GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }

    private void getData() {
        String tag_string_req = "suggested_user";

        pDialog.setMessage("");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
        vp.mom.api.AppConfig.SUGGESTED_USER_PAGINATED, new Response.Listener<String>() {

    @Override
    public void onResponse(String response) {
    //    Log.e(TAG, "Login Response: " + response.toString());
        hideDialog();

    if (response != null) {


        JSONObject jsonresponse= null;
        try {
            jsonresponse = new JSONObject(response.toString());
            if (jsonresponse.getBoolean("status")) {

                suggestedmainll.setVisibility(View.VISIBLE);
                error_layout.setVisibility(View.GONE);
                parseJsonFeed(jsonresponse);


            }
            else

            {
                suggestedmainll.setVisibility(View.GONE);
                error_layout.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
        }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
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
            params.put("page_no",""+pageCount);
        return params;
        }

        };

        // Adding request to request queue
        vp.mom.app.AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {

     //   Log.e(TAG, "suggest User: " + response);
        try {


                JSONArray feedArray = response.getJSONArray("items");

                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);

                    if (!feedObj.isNull("product")) {
                        vp.mom.data.SuggestedItem item = new vp.mom.data.SuggestedItem();
                        item.setUserid(feedObj.getString("id"));
                        item.setFname(feedObj.getString("first_name"));
                        item.setLname(feedObj.getString("last_name"));
                        item.setLocation(feedObj.getString("location"));
                        item.setProfile_image(feedObj.getString("photo"));
                        item.setItemDisc(feedObj.getString("description"));
                        ArrayList<String> img = new ArrayList<>();

                        JSONArray imageArray = feedObj.getJSONArray("product");
                        // if(!feedObj.isNull("productimages"))

                        for (int j = 0; j < imageArray.length(); j++) {

                            JSONObject imagefeed = (JSONObject) imageArray.get(j);
                            img.add(imagefeed.getString("path").replace("th_", ""));
                            // Log.e(TAG, "" + imagefeed.getString("path"));

                        }
                        item.setImagearray(img);
                        feedItems.add(item);
                    }


                }
                // notify data changes to list adapater
        listAdapter.notifyDataSetChanged();
            }catch(JSONException e){

                Log.e("JSONException", "JSONException" + e.toString());
                e.printStackTrace();
            }

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }
}


