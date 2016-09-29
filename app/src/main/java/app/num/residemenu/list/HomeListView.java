package app.num.residemenu.list;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.num.residemenu.R;


public class HomeListView extends Fragment
{
//	long time1;
	private static final String TAG = HomeListView.class.getSimpleName();
	private ListView listView;
	private HomeListAdapter listAdapter;
	private List<FeedItem> feedItems;
	//private ProgressBar mProgress;
	int start = 0;
	int limit = 10;
	boolean loadingMore = false;
    int pageCount=1;
	//boolean flag=true;
	//private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
	//private SwipeRefreshLayout swipeRefreshLayout;

	ListView swipeRefreshLayout;
	ProgressBar listProgress;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.homelistview, container, false);
		listView = (ListView) rootView.findViewById(R.id.list);
		swipeRefreshLayout = (ListView) rootView.findViewById(R.id.list);
		feedItems = new ArrayList<FeedItem>();
		//mProgress = (ProgressBar) rootView.findViewById(R.id.progressBar);
		listProgress= (ProgressBar) rootView.findViewById(R.id.listProgress);
		//mProgress.getIndeterminateDrawable.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
		listAdapter = new HomeListAdapter(getActivity(), feedItems);
		listView.setAdapter(listAdapter);
		//Here is where the magic happens
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,

                                 int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem==18)
                {
                    pageCount+=1;
                   getHomeListData();
                }
            }

        });
		getHomeListData();
		return rootView;
	}




	private void getHomeListData() {
		String NEW_URL="http://marketofmums.com/API/productmgt.php/homedetails1/"+"?user_id="+"0000"+"&"+"page_no="+pageCount;

			AndroidNetworking.get(NEW_URL)
				.addPathParameter("pageNumber", "0")
				.addQueryParameter("limit", "3")
				.addHeaders("token", "1234")
				.setTag("test")
				.setPriority(Priority.LOW)
				.build()
				.getAsJSONObject(new JSONObjectRequestListener() {
					@Override
					public void onResponse(JSONObject response) {
						parseJsonFeed(response);
						listProgress.setVisibility(View.INVISIBLE);
					}
					@Override
					public void onError(ANError error) {
						// handle error
						Toast.makeText(getActivity(),"helo"+error,Toast.LENGTH_SHORT).show();
					}
				});

	}

	/**
	 * Parsing json reponse and passing the data to feed view list adapter
	 * */
	private void parseJsonFeed(JSONObject response) {
		//Log.e("System.currentTimeMillis()","System"+(System.currentTimeMillis()-time1));
		//Log.e(TAG, "" + response);
		try {
			JSONArray feedArray = response.getJSONArray("items");
			for (int i = 0; i < feedArray.length(); i++) {

                JSONObject feedObj = (JSONObject) feedArray.get(i);

					if(!feedObj.isNull("productimages")) {
						FeedItem item = new FeedItem();
						item.setBookmark_status(feedObj.getBoolean("Bookmark_status"));
						item.setUserId(feedObj.getString("id"));
						item.setId(feedObj.getString("product_id"));
						item.setName(feedObj.getString("username"));
						item.setemail(feedObj.getString("location"));
						item.setProfilePic(feedObj.getString("photo"));
						item.setbase_price(feedObj.getString("base_price"));
						item.setIsLike(feedObj.getBoolean("like_status"));
						item.setProductBrand(feedObj.getString("brand"));
						item.setLikeCount(Integer.parseInt(feedObj.getString("prod_like")));
						item.setProdDesc(feedObj.getString("description"));
						item.setTimeStamp(feedObj.getString("created"));
						item.setProdSize(feedObj.getString("size"));
						item.setProductName(feedObj.getString("name"));
						item.setShippingCost(feedObj.getString("delivery_cost"));
						item.setDeliveryType(feedObj.getString("delivery_type"));
						item.setPaypalEmail(feedObj.getString("paypal_registered_email"));

						CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
								Long.parseLong(feedObj.getString("timeStamp")),
								System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
						item.setTimeStamp(""+timeAgo);
                ArrayList<String> img=new ArrayList<>();

                JSONArray imageArray = feedObj.getJSONArray("productimages");
					for (int j = 0; j < imageArray.length(); j++) {
						JSONObject imagefeed = (JSONObject) imageArray.get(j);
						img.add(imagefeed.getString("path").replace("th_", ""));
						// Log.e(TAG, "" + imagefeed.getString("path"));
					}

					//	Log.e(" in api size",""+img.size());
							item.setImagearray(img);
					feedItems.add(item);
				}
		}
			// notify data changes to list adapater

		} catch (JSONException e) {

            Log.e(TAG,""+e.toString());

			e.printStackTrace();
		}
		listAdapter.notifyDataSetChanged();

		loadingMore = false;
	}

}
