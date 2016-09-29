package vp.mom.adapters;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vp.mom.R;
import vp.mom.api.CircleTransformation;
import vp.mom.app.AppController;
import vp.mom.searchtab.UserProfileDetailActivity;

public class HomeListAdapter extends BaseAdapter {
ImageView img;
private Context activity;
private LayoutInflater inflater;
private List<vp.mom.data.FeedItem> feedItems;
private int avatarSize;
private vp.mom.api.SessionManager session;
String TYPE;
//	private ProgressDialog pDialog;
private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
ImageView imageView;
private final ArrayList<Integer> likedPositions = new ArrayList<>();
private HashTagHelper mTextHashTagHelper;
boolean add_bookmar_flag=false;
vp.mom.SearchDataBase db;
private final Map<View, AnimatorSet> likeAnimations = new HashMap<>();
boolean likeFlag=false;
static  int likeClick=0;
//ImageLoader imageLoader = AppController.getInstance().getImageLoader();
public HomeListAdapter(Context activity, List<vp.mom.data.FeedItem> feedItems) {
    this.activity = activity;
    this.feedItems = feedItems;
    this.avatarSize = activity.getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);
    session=new vp.mom.api.SessionManager(activity);
    db = new vp.mom.SearchDataBase(activity);

}

@Override
public int getCount() {
    return feedItems.size();
}

@Override
public Object getItem(int location) {
    return feedItems.get(location);
}

@Override
public long getItemId(int position) {
    return position;
}

static private int lastPosition = -1;
ImageView homelistoption, listcomment, listlike;
PopupMenu popup;
Button listBuy;

public class MyViewHolder {
    TextSwitcher tsLikesCounter;
    ImageView like;
    TextView likeCount;
}

int likeCounter = 0;

@Override
public View getView(final int position, View convertView, ViewGroup parent) {
    //ImageView like;
    //	final CellFeedViewHolder cellFeedViewHolder = new CellFeedViewHolder(view);
    final MyViewHolder holder = new MyViewHolder();
    if (inflater == null)
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
        convertView = inflater.inflate(R.layout.home_list_row_item, null);

//    TextView textView5 = (TextView) convertView.findViewById(R.id.textView5);
//  textView5.setText(""+position);
    TextView name = (TextView) convertView.findViewById(R.id.name);
    TextView	prodSize= (TextView) convertView.findViewById(R.id.prodSize);
    TextView	timestamp= (TextView) convertView.findViewById(R.id.hoelisttimestamp);

    holder.likeCount  = (TextView) convertView.findViewById(R.id.likeCount);

    holder.tsLikesCounter= (TextSwitcher) convertView.findViewById(R.id.tsLikesCounter);


    TextView productName = (TextView) convertView
            .findViewById(R.id.productName);
    TextView txtdescMsg = (TextView) convertView
            .findViewById(R.id.txtdescMsg);
    TextView homelistbaseprice = (TextView) convertView.findViewById(R.id.homelistbaseprice);
    ImageView	homelistshare= (ImageView) convertView.findViewById(R.id.homelistshare);
    homelistoption	= (ImageView) convertView.findViewById(R.id.homelistoption);

    listcomment	= (ImageView) convertView.findViewById(R.id.listcomment);
    holder.like= (ImageView) convertView.findViewById(R.id.listlike);
    listBuy= (Button) convertView.findViewById(R.id.listBuy);

    final vp.mom.data.FeedItem item = feedItems.get(position);

    timestamp.setText(item.getTimeStamp());
    holder.likeCount.setText(item.getLikeCount() + " People Liked this");

    holder.tsLikesCounter.setCurrentText(activity.getResources().getQuantityString(
R.plurals.likes_count, item.getLikeCount(), item.getLikeCount()
));

    prodSize.setText(item.getProdSize());
    homelistoption.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(activity, v);
            //	popupMenu.setOnMenuItemClickListener(HomeListAdapter.this);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.show();

            if(feedItems.get(position).isBookmark_status())
                popupMenu.getMenu().getItem(0).setTitle("Remove bookmark");

            else
                popupMenu.getMenu().getItem(0).setTitle("Add bookmark");

            popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuitem) {


                    TYPE="2";
                    switch (menuitem.getItemId()) {

                        case R.id.item_bookmark:
                            setLIkeProductToUser("" + feedItems.get(position).getId(), TYPE);

                            if(add_bookmar_flag)
                                feedItems.get((Integer)holder.like.getTag()).setBookmark_status(false);
                            else
                                feedItems.get((Integer)holder.like.getTag()).setBookmark_status(true);

                            notifyDataSetChanged();


                            return true;
                        case R.id.item_report_product:
                            Intent commentIntent = new Intent(activity, vp.mom.activitys.ReportProduct.class);

                            commentIntent.putExtra("ProducID",""+feedItems.get(position).getId());
                            activity.startActivity(commentIntent);
                            return true;


                        case R.id.item_chat:

                            activity.startActivity(new Intent(activity,
                                    vp.mom.gcm.gcm.ChatRoomActivity.class).putExtra(
                                    "UserToChat", item.getUserId()).putExtra("name",item.getName()));

                            return true;

                    }

                    return false;
                }
            });

            //listlike.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.shake_error));
        }
    });


    listBuy.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            session.setBooleanSessionData("doPayment",true);
            Intent chechoutIntent = new Intent(activity, vp.mom.NewCheckoutActivity.class);
            chechoutIntent.putExtra("productId",item.getId());
            chechoutIntent.putExtra("productSize",item.getProdSize());
            chechoutIntent.putExtra("productDisc",item.getProdDesc());
            chechoutIntent.putExtra("productPrice",item.getbase_price());
            chechoutIntent.putExtra("product_name",item.getProductName());
            chechoutIntent.putExtra("shippingcost",item.getShippingCost());
            chechoutIntent.putExtra("deliveryType",item.getDeliveryType());
            chechoutIntent.putExtra("paypalEmail",item.getPaypalEmail());
            activity.startActivity(chechoutIntent);
            //((Activity)activity).finish();

        }
    });

    holder.like.setTag(position);

    if(!feedItems.get(position).isLike()){
        holder.like.setImageResource(R.drawable.ic_heart_outline_grey);

    }else {
        holder.like.setImageResource(R.drawable.ic_heart_red);
    }
    holder.like.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TYPE = "3";
            //	feedItems.get((Integer)holder.like.getTag()).setIsLike(true);
            if (!feedItems.get(position).isLike()){
                lastPosition = 19999999;
                updateHeartButton(holder, true, (Integer) holder.like.getTag());
                feedItems.get((Integer)holder.like.getTag()).setIsLike(true);
                likedPositions.add((Integer) holder.like.getTag());


                feedItems.get((Integer)holder.like.getTag()).setLikeCount(item.getLikeCount() + 1);

                notifyDataSetChanged();
                updateLikesCounter(holder, item.getLikeCount());

                setLIkeProductToUser("" + feedItems.get(position).getId(), TYPE);

            //	Log.e("like", "set to like");
                likeFlag=true;



            }
            else{
                lastPosition = 19999999;
            //	Log.e("like", "set to unlike");
                feedItems.get(position).setIsLike(false);
                holder.like.setImageResource(R.drawable.ic_heart_outline_grey);

                if(likedPositions.contains((Integer) holder.like.getTag()));
                likedPositions.remove((Integer) holder.like.getTag());
                feedItems.get((Integer)holder.like.getTag()).setIsLike(false);

                feedItems.get((Integer)holder.like.getTag()).setLikeCount(item.getLikeCount() - 1);

                notifyDataSetChanged();
                updateLikesCounter(holder, item.getLikeCount());
                setLIkeProductToUser("" + feedItems.get(position).getId(), TYPE);
                likeFlag=true;


            }


        }
    });

    listcomment.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent commentIntent = new Intent(activity, vp.mom.activitys.CommentsActivity.class);

            commentIntent.putExtra("ProducID",""+feedItems.get(position).getId());
            activity.startActivity(commentIntent);
        }
    });
    final ImageView profilePic = (ImageView) convertView
            .findViewById(R.id.homelist_profilepic);
    profilePic.setClickable(true);

    profilePic.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent msg=new Intent(activity, UserProfileDetailActivity.class);

            msg.putExtra("userId",item.getUserId());

            activity.startActivity(msg);

        }
    });

//		ImageView feedImageView = (ImageView) convertView
//				.findViewById(R.id.homefeedImage);
    name.setText(item.getName());
    productName.setText(item.getProductBrand()+": "+item.getProductName());
    homelistbaseprice.setText("£" + item.getbase_price());

    Picasso.with(activity)
            .load(item.getProfilePic())
            .placeholder(R.drawable.img_circle_placeholder)
            .resize(avatarSize, avatarSize)
            .centerCrop()
            .transform(new CircleTransformation())
            .into(profilePic);
    txtdescMsg.setText(item.getProdDesc());

    mTextHashTagHelper = HashTagHelper.Creator.create(activity.getResources().getColor(R.color.blue),
            new HashTagHelper.OnHashTagClickListener() {
                @Override
                public void onHashTagClicked(String hashTag) {

                    try
                    {
                        db.addseracheItem(hashTag);

                    }
                    catch (Exception exp){

                    }

                    Intent msg = new Intent(activity, vp.mom.searchtab.ItemSerachResult.class);
                    msg.putExtra("serachItemName",hashTag);
                    msg.putExtra("itemType", "1");
                    activity.startActivity(msg);
                    //	activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
//Toast.makeText(activity,""+hashTag,1000).show();
                }
            });

    mTextHashTagHelper.handle(txtdescMsg);

    ImagePagerAdapter adapter =new ImagePagerAdapter(item.getImagearray());
//
//	//	Log.e(" in adapter size",""+item.getImagearray().size());
    final ViewPager viewPager = (ViewPager) convertView.findViewById(R.id.view_pagerlist);
//		//adapter = new ImagePagerAdapter(imagearra);
    viewPager.setAdapter(adapter);
//
    final vp.mom.circleindicator.CirclePageIndicator circleIndicator = (vp.mom.circleindicator.CirclePageIndicator) convertView.findViewById(R.id.indicatorlist);
    circleIndicator.setViewPager(viewPager);

    homelistshare.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        new 	ShareOperation(item.getImagearray().get(0),item.getId()).execute();
}
});

likeFlag=false;
    return convertView;
}

    private class ShareOperation extends AsyncTask<String, Void, Bitmap> {
        private ProgressDialog pDialog;

        String imgUrl,itemId;

        ShareOperation(String mimgUrl,String mitemId)
        {
            this.imgUrl=mimgUrl;
            this.itemId=mitemId;
        }

        @Override
        protected Bitmap doInBackground(String... params) {


            URL url = null;
            try {
                url = new URL(imgUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap image = null;
            try {
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bm) {

            if (pDialog.isShowing())
                pDialog.dismiss();

            if (bm != null)

            {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                        bm, "Title", null);
                Uri imageUri = Uri.parse(path);
                share.putExtra(Intent.EXTRA_STREAM, imageUri);
                String sAux = "\nLook what i found on Market of mums \n\n";
                sAux = sAux + "http://www.marketofmums.com/product/product_details.php?prod_id=\""+itemId+"\n\n";
                share.putExtra(Intent.EXTRA_TEXT, sAux );

                activity.startActivity(Intent.createChooser(share, "Select"));

            } else Toast.makeText(activity, "Please try Again", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(activity,
                    R.style.AppTheme_Dark_Dialog);
            pDialog.setCancelable(false);
            pDialog.setMessage("");
            if (!pDialog.isShowing())
                pDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }


private void updateLikesCounter(final MyViewHolder holder, int toValue) {
//		String likesCountTextFrom = holder.tsLikesCounter.getResources().getQuantityString(
//				R.plurals.likes_count, toValue - 1, toValue - 1
//		);
//		holder.tsLikesCounter.setCurrentText(likesCountTextFrom);

    String likesCountTextTo = holder.tsLikesCounter.getResources().getQuantityString(
            R.plurals.likes_count, toValue, toValue
    );
    holder.tsLikesCounter.setText(likesCountTextTo);
}

//	public void setupShareIntent(ImageView ivImage) {
//		// Fetch Bitmap Uri locally
//		// ImageView ivImage = (ImageView) findViewById(R.id.ivResult);
//		Uri bmpUri = getLocalBitmapUri(img); // see previous remote images section
//		Log.v("bmpUi", "" + bmpUri);
//		// Create share intent as described above
//
//
//		Intent share = new Intent(android.content.Intent.ACTION_SEND);
//		share.setType("text/plain");
//		share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//
//		// Add data to the intent, the receiving app will decide
//		// what to do with it.
//		share.putExtra(Intent.EXTRA_SUBJECT, "Hello please download the app and enjoy online shopping..");
//		share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=vp.mom");
//
//		activity.startActivity(Intent.createChooser(share, "Share link!"));
//
//
//	}

public Uri getLocalBitmapUri(ImageView imageView) {

    // Extract Bitmap from ImageView drawable
    Drawable drawable = imageView.getDrawable();
    Bitmap bmp = null;

    try {
        if (drawable instanceof BitmapDrawable) {

            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        } else {

            return null;

        }
    } catch (Exception exc) {

        Log.e("BitmapDrawable", "BitmapDrawable" + exc.toString());
    }

    // Store image to default external storage directory

    Uri bmpUri = null;

    try {

        File file = new File(Environment.getExternalStoragePublicDirectory(

                Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");

        file.getParentFile().mkdirs();

        FileOutputStream out = new FileOutputStream(file);

        bmp.compress(Bitmap.CompressFormat.PNG, 90, out);

        out.close();

        bmpUri = Uri.fromFile(file);

    } catch (IOException e) {
        Log.e("IOException", "IOException" + e.toString());
        e.printStackTrace();

    }

    return bmpUri;

}

private void setLIkeProductToUser(final String productId, final String type) {

    // Tag used to cancel the request
    String tag_string_req = "setLIkeProductToUser";

    //pDialog.setMessage(" in ...");
    //showDialog();

    StringRequest strReq = new StringRequest(Request.Method.POST,
            vp.mom.api.AppConfig.PRODUCT_LIKE, new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
        //	Log.e("Homelistadapter", "Homelistadapter Response: " + response.toString());
            //	hideDialog();
            try {
                JSONObject jObj = new JSONObject(response);
                boolean status = jObj.getBoolean("status");
                // Check for error node in json
                if (status) {
                    if (type.equalsIgnoreCase("2"))

                    {
                        add_bookmar_flag=true;
                        Toast.makeText(activity,
                                "Bookmark added", Toast.LENGTH_LONG).show();

                    }

                } else {
                    if (type.equalsIgnoreCase("2"))
                    {
                        add_bookmar_flag=false;
                        Toast.makeText(activity,
                                "Bookmark removed", Toast.LENGTH_LONG).show();
                    }

                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
            //	Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
        //	Log.e("Homelistadapter", "Login Error: " + error.getMessage());
//				Toast.makeText(activity,
//						error.getMessage(), Toast.LENGTH_LONG).show();
            //	hideDialog();
        }
    }) {

        @Override
        protected Map<String, String> getParams() {
            // Posting parameters to login url
            Map<String, String> params = new HashMap<String, String>();
//                params.put("email", email);
//                params.put("password", password);

            params.put("user_id", session.getStringSessionData("uid"));
            params.put("product_id", productId);
            params.put("type", type);
            return params;
        }

    };
    // Adding request to request queue
    AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
}

private class ImagePagerAdapter extends PagerAdapter {
    ArrayList<String> imagearry;

    public ImagePagerAdapter(ArrayList<String> mimagearry) {
        this.imagearry = mimagearry;

        //	Log.e("size",""+imagearry.size());
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return this.imagearry.size();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Context context = activity;
        imageView = new ImageView(context);
        final int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_top);
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //	imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(context)
                .load(imagearry.get(position))
.fit().centerCrop()
                .error(android.R.drawable.stat_notify_error)
                .into(imageView);


        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }


    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view == ((ImageView) object);
    }
}

private void updateHeartButton(final MyViewHolder holder, boolean animated, int position) {
    if (animated) {
        if (!likeAnimations.containsKey(holder)) {
            AnimatorSet animatorSet = new AnimatorSet();
            likeAnimations.put(holder.like, animatorSet);

            ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder.like, "rotation", 0f, 360f);
            rotationAnim.setDuration(300);
            rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

            ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.like, "scaleX", 0.2f, 1f);
            bounceAnimX.setDuration(300);
            bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

            ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.like, "scaleY", 0.2f, 1f);
            bounceAnimY.setDuration(300);
            bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
            bounceAnimY.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    holder.like.setImageResource(R.drawable.ic_heart_red);
                }
            });

            animatorSet.play(rotationAnim);
            animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    resetLikeAnimationState(holder);
                }
            });

            animatorSet.start();
        }
    } else {
        if (likedPositions.contains(position)) {
            holder.like.setImageResource(R.drawable.ic_heart_red);
        } else {
            holder.like.setImageResource(R.drawable.ic_heart_outline_grey);
        }
    }
}

private void resetLikeAnimationState(MyViewHolder holder) {
    likeAnimations.remove(holder);
//		holder.like.setVisibility(View.GONE);
//		holder.like.setVisibility(View.GONE);
}

//	private class ImageShareOperation extends AsyncTask<Bitmap, Void, Bitmap> {
//		Uri bmpUri;
//		String ImgUrl;
//
//		ImageShareOperation(String url) {
//			this.ImgUrl = url;
//
//		}
//
//		@Override
//		protected Bitmap doInBackground(Bitmap... params) {
//
//
//			try {
//				URL url = new URL(ImgUrl);
//				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//				connection.setDoInput(true);
//				connection.connect();
//				InputStream input = connection.getInputStream();
//				Bitmap myBitmap = BitmapFactory.decodeStream(input);
//				return myBitmap;
//			} catch (IOException e) {
//				// Log exception
//				return null;
//			}
//
//
//			//	return "Executed";
//		}
//
//		@Override
//		protected void onPostExecute(Bitmap result) {
//			Intent shareIntent = new Intent();
//
//			shareIntent.setAction(Intent.ACTION_SEND);
//
//			shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
//
//			shareIntent.setType("image/*");
//
//			// Launch sharing dialog for image
//
//			activity.startActivity(Intent.createChooser(shareIntent, "Share Image"));
//		}
//
//		@Override
//		protected void onPreExecute() {
//		}
//
//
//	}
}
