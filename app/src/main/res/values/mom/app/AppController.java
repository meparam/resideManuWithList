package vp.mom.app;


import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {
	private vp.mom.app.MyPreferenceManager pref;
	public static final String TAG = vp.mom.app.AppController.class.getSimpleName();
//	public static Typeface helveticaFaceNormal;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	vp.mom.volley.LruBitmapCache mLruBitmapCache;
	//private static AppController sCurrentApplication = null;
	private static vp.mom.app.AppController mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;

	//	sCurrentApplication = this;
		//helveticaFaceNormal = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Bold.ttf");
	}

	public vp.mom.app.MyPreferenceManager getPrefManager() {
		if (pref == null) {
			pref = new vp.mom.app.MyPreferenceManager(this);
		}

		return pref;
	}
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
	public static synchronized vp.mom.app.AppController getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}
	// region Getters

	// endregion
	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			getLruBitmapCache();
			mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
		}

		return this.mImageLoader;
	}

	public vp.mom.volley.LruBitmapCache getLruBitmapCache() {
		if (mLruBitmapCache == null)
			mLruBitmapCache = new vp.mom.volley.LruBitmapCache();
		return this.mLruBitmapCache;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

//	public static void applyFonts(View v) {
//		try {
//			if (v instanceof ViewGroup) {
//				ViewGroup vg = (ViewGroup) v;
//				for (int i = 0; i < vg.getChildCount(); i++) {
//					View child = vg.getChildAt(i);
//					applyFonts(child);
//				}
//			} else if (v instanceof TextView) {
//
//				((TextView) v).setTypeface(helveticaFaceNormal, Typeface.BOLD);
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
