//package yv.trip.fragments;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.StringRequest;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import yv.trip.R;
//import yv.trip.api.AppConfig;
//import yv.trip.api.SessionManager;
//import yv.trip.app.AppController;
//import yv.trip.data.SuggestedItem;
//import yv.trip.searchtab.UserProfileDetailActivity;
//
//public class ExplorePeopleFragment extends Fragment {
//	private static final String TAG = ExplorePeopleFragment.class.getSimpleName();
//	private GridView photoGrid;
//	private int mPhotoSize, mPhotoSpacing;
//	private ImageAdapter imageAdapter;
//	private ProgressDialog pDialog;
//	private SessionManager session;
//	private List<SuggestedItem> feedItems;
//	// Some items to add to the GRID
////	private static final String[] CONTENT = new String[] { "Akon", "Justin Bieber", "AlRight", "Big Sean",
////			"Britney Spears", "Hilary", "Micheal Buble", "Akon", "Justin Bieber", "AlRight", "Big Sean",
////			"Britney Spears", "Hilary", "Micheal Buble", "Britney Spears", "Hilary", "Micheal Buble", "Akon",
////			"Justin Bieber", "AlRight", "Big Sean", "Britney Spears", "Hilary", "Micheal Buble", "Akon",
////			"Justin Bieber", "AlRight", "Big Sean", "Britney Spears", "Hilary", "Micheal Buble", "Akon",
////			"Justin Bieber", "AlRight", "Big Sean", "Britney Spears", "Hilary", "Micheal Buble", "Britney Spears",
////			"Hilary", "Micheal Buble", "Akon", "Justin Bieber", "AlRight", "Big Sean", "Britney Spears", "Hilary",
////			"Micheal Buble" };
////	private static final int[] ICONS = new int[] { R.drawable.cover_big_sean, R.drawable.cover_hilary,
////			 R.drawable.cover_akon, R.drawable.cover_hilary, R.drawable.cover_akon,
////			R.drawable.cover_hilary, R.drawable.cover_akon, R.drawable.cover_hilary,
////			R.drawable.cover_akon, R.drawable.cover_big_sean, R.drawable.cover_akon,
////			R.drawable.cover_hilary, R.drawable.cover_big_sean, R.drawable.cover_hilary, R.drawable.cover_akon,
////			R.drawable.cover_big_sean, R.drawable.cover_akon, R.drawable.cover_big_sean,
////			R.drawable.cover_big_sean, R.drawable.cover_akon, R.drawable.cover_akon,
////			R.drawable.cover_hilary, R.drawable.cover_big_sean, R.drawable.cover_hilary, R.drawable.cover_akon,
////			R.drawable.cover_akon, R.drawable.cover_akon, R.drawable.cover_hilary,
////			R.drawable.cover_big_sean, R.drawable.cover_big_sean, R.drawable.cover_hilary,
////			R.drawable.cover_akon, R.drawable.cover_hilary, R.drawable.cover_akon, R.drawable.cover_akon,
////			R.drawable.cover_hilary, R.drawable.cover_big_sean, R.drawable.cover_big_sean,
////			R.drawable.cover_akon, R.drawable.cover_big_sean };
//
//
//	View rootView;
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		rootView= inflater.inflate(R.layout.explore_people_fragment, container, false);
//		pDialog = new ProgressDialog(getActivity());
//		pDialog.setCancelable(false);
//		session=new SessionManager(getActivity());
//		// get the photo size and spacing
//		mPhotoSize = getResources().getDimensionPixelSize(R.dimen.photo_size);
//		mPhotoSpacing = getResources().getDimensionPixelSize(R.dimen.photo_spacing);
//
//
//		photoGrid = (GridView) rootView.findViewById(R.id.albumGrid);
//
//		feedItems = new ArrayList<SuggestedItem>();
//		// initialize image adapter
//		imageAdapter = new ImageAdapter(getActivity(),feedItems);
//
//		// set image adapter to the GridView
//		photoGrid.setAdapter(imageAdapter);
//
//
//		photoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//				Intent msg=new Intent(getActivity(), UserProfileDetailActivity.class);
//				startActivity(msg);
//			}
//		});
//
//
////		// get the view tree observer of the grid and set the height and numcols dynamically
////		photoGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
////			@Override
////			public void onGlobalLayout() {
////				if (imageAdapter.getNumColumns() == 0) {
////					final int numColumns = (int) Math.floor(photoGrid.getWidth() / (mPhotoSize + mPhotoSpacing));
////					if (numColumns > 0) {
////						final int columnWidth = (photoGrid.getWidth() / numColumns) - mPhotoSpacing;
////						imageAdapter.setNumColumns(numColumns);
////						imageAdapter.setItemHeight(columnWidth);
////
////					}
////				}
////			}
////		});
//
//
//		getData();
//
//
//
//		return  rootView;
//	}
//
//	private void getData() {
//		String tag_string_req = "suggested_user";
//
//		pDialog.setMessage("Loading ...");
//		showDialog();
//
//		StringRequest strReq = new StringRequest(Request.Method.POST,
//				AppConfig.GET_ALL_MEMBER, new Response.Listener<String>() {
//
//			@Override
//			public void onResponse(String response) {
//				Log.d(TAG, "Login Response: " + response.toString());
//				hideDialog();
//
//				if (response != null) {
//
//
//					JSONObject jsonresponse= null;
//					try {
//						jsonresponse = new JSONObject(response.toString());
//
//						parseJsonFeed(jsonresponse);
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//
//				}
//
//			}
//		}, new Response.ErrorListener() {
//
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				Log.e(TAG, "Login Error: " + error.getMessage());
////				Toast.makeText(getActivity(),
////						error.getMessage(), Toast.LENGTH_LONG).show();
//				hideDialog();
//			}
//		}) {
//
//			@Override
//			protected Map<String, String> getParams() {
//				// Posting parameters to login url
//				Map<String, String> params = new HashMap<String, String>();
//				//                params.put("email", email);
//				//                params.put("password", password);
//
//				params.put("id",session.getStringSessionData("uid") );
//
//				return params;
//			}
//
//		};
//
//		// Adding request to request queue
//		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//
//	}
//	/**
//	 * Parsing json reponse and passing the data to feed view list adapter
//	 * */
//	private void parseJsonFeed(JSONObject response) {
//		try {
//			JSONArray feedArray = response.getJSONArray("items");
//
//			for (int i = 0; i < feedArray.length(); i++) {
//				JSONObject feedObj = (JSONObject) feedArray.get(i);
//
//				SuggestedItem item=new  SuggestedItem();
//				item.setUserid(feedObj.getString("id"));
//				item.setFname(feedObj.getString("first_name"));
//				item.setLname(feedObj.getString("last_name"));
//				item.setEmail(feedObj.getString("email"));
//				item.setProfile_image(feedObj.getString("photo"));
//
//				feedItems.add(item);
//
//
//
//			}
//
//			// notify data changes to list adapater
//			imageAdapter.notifyDataSetChanged();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
//	// ///////// ImageAdapter class /////////////////
//	public class ImageAdapter extends BaseAdapter {
//		private LayoutInflater mInflater;
//		private int mItemHeight = 0;
//		private int mNumColumns = 0;
//		private RelativeLayout.LayoutParams mImageViewLayoutParams;
//
//
//		private Activity activity;
//		private List<SuggestedItem> suggestedItems;
//		ImageLoader imageLoader = AppController.getInstance().getImageLoader();
//
//		public ImageAdapter(Activity activity,List<SuggestedItem> msuggestedItems) {
//			this.activity = activity;
//			this.suggestedItems=msuggestedItems;
//			mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			mImageViewLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//					LayoutParams.MATCH_PARENT);
//		}
//
//		public int getCount() {
//			return suggestedItems.size();
//		}
//
//		// set numcols
//		public void setNumColumns(int numColumns) {
//			mNumColumns = numColumns;
//		}
//
//		public int getNumColumns() {
//			return mNumColumns;
//		}
//
//		// set photo item height
//		public void setItemHeight(int height) {
//			if (height == mItemHeight) {
//				return;
//			}
//			mItemHeight = height;
//			mImageViewLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
//			notifyDataSetChanged();
//		}
//
//		public Object getItem(int position) {
//			return suggestedItems.get(position);
//		}
//
//		public long getItemId(int position) {
//			return position;
//		}
//
//		public View getView(final int position, View view, ViewGroup parent) {
//			SuggestedItem item = suggestedItems.get(position);
//			if (view == null)
//				view = mInflater.inflate(R.layout.explore_people_grid_view, null);
//
//			ImageView cover = (ImageView) view.findViewById(R.id.cover);
//			TextView title = (TextView) view.findViewById(R.id.title);
//
//			cover.setLayoutParams(mImageViewLayoutParams);
//
//			// Check the height matches our calculated column width
//			if (cover.getLayoutParams().height != mItemHeight) {
//				cover.setLayoutParams(mImageViewLayoutParams);
//			}
//
//
//
//			imageLoader.get(item.getProfile_image(), ImageLoader.getImageListener(
//					cover, R.drawable.baby, R.drawable.ic_launcher));
//
//				title.setText(item.getFname());
//
//			return view;
//		}
//	}
//	private void showDialog() {
//		if (!pDialog.isShowing())
//			pDialog.show();
//	}
//
//	private void hideDialog() {
//		if (pDialog.isShowing())
//			pDialog.dismiss();
//	}
//}
