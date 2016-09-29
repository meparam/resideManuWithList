package app.num.residemenu.facebook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

public class FacebookList extends Fragment {
	private static final String TAG = FacebookList.class.getSimpleName();
	private ListView listView;
	private FeedListAdapter listAdapter;
	private List<FeedItem> feedItems;
	private String URL_FEED = "http://api.androidhive.info/feed/feed.json";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.listmain, container, false);

		listView = (ListView) rootView.findViewById(R.id.list);
		feedItems = new ArrayList<FeedItem>();

		listAdapter = new FeedListAdapter(getActivity(), feedItems);
		listView.setAdapter(listAdapter);

		AndroidNetworking.get(URL_FEED)
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
					}
					@Override
					public void onError(ANError error) {
						// handle error
						Toast.makeText(getActivity(),"helo"+error,Toast.LENGTH_SHORT).show();
					}
				});
		return rootView;
	}

	/**
	 * Parsing json reponse and passing the data to feed view list adapter
	 * */
	private void parseJsonFeed(JSONObject response) {
		try {
			JSONArray feedArray = response.getJSONArray("feed");

			for (int i = 0; i < feedArray.length(); i++) {
				JSONObject feedObj = (JSONObject) feedArray.get(i);

				FeedItem item = new FeedItem();
				item.setId(feedObj.getInt("id"));
				item.setName(feedObj.getString("name"));

				// Image might be null sometimes
				String image = feedObj.isNull("image") ? null : feedObj
						.getString("image");
				item.setImge(image);
				item.setStatus(feedObj.getString("status"));
				item.setProfilePic(feedObj.getString("profilePic"));
				item.setTimeStamp(feedObj.getString("timeStamp"));

				// url might be null sometimes
				String feedUrl = feedObj.isNull("url") ? null : feedObj
						.getString("url");
				item.setUrl(feedUrl);

				feedItems.add(item);
			}

			// notify data changes to list adapater
			listAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
