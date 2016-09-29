package vp.mom.activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vp.mom.R;
import vp.mom.api.CircleTransformation;
import vp.mom.app.AppController;
import vp.mom.searchtab.UserProfileDetailActivity;

public class ProductDetails extends AppCompatActivity {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String msg, chatRoomId, userName;
    private vp.mom.gcm.gcm.NotificationUtils notificationUtils;
    private ProgressDialog pDialog;
    ArrayList<String> imagearra;
    ImageView profilepic, productdetailsimg;
    ImagePagerAdapter adapter;
    TextView email, name, desc, price, size, timeStamp, productNameDet;
    TextSwitcher noOfLike;
    private int avatarSize;
    boolean likeStatus;
    ImageView likedImage, imageoptions, commentview, imageshare;
    private vp.mom.api.SessionManager session;
    // String userId;
    Button productBuy, productEdit, productDelete;
    LinearLayout likedImagell, productll, productEditLL;
    private HashTagHelper mTextHashTagHelper;
    boolean isSoldProduct = false, likeFlag = false;
    int likeCount;
    vp.mom.SearchDataBase db;

    String productId, userId, productSize, productDisc, productprice, productname, shippingCost, meetInperson;
    String calledUserId, paypalEmail;
    boolean bookMArkStatus = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        session = new vp.mom.api.SessionManager(vp.mom.activitys.ProductDetails.this);
        userId = session.getStringSessionData("uid");
        pDialog = new ProgressDialog(vp.mom.activitys.ProductDetails.this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        this.avatarSize = getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);
        db = new vp.mom.SearchDataBase(vp.mom.activitys.ProductDetails.this);
        init();
        Intent myintent = getIntent();
        if (myintent != null) {
            productId = myintent.getStringExtra("productId");
            isSoldProduct = myintent.getBooleanExtra("isSoldProduct", false);
            calledUserId = myintent.getStringExtra("calledUserId");
        }
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

        getData();

        if (calledUserId.equals(userId)) {
            productll.setVisibility(View.GONE);
            productEditLL.setVisibility(View.VISIBLE);
        }
        if (isSoldProduct) {
            productll.setVisibility(View.GONE);
            //   productEditLL.setVisibility(View.GONE);
        }


        productBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chechoutIntent = new Intent(vp.mom.activitys.ProductDetails.this, vp.mom.NewCheckoutActivity.class);
                chechoutIntent.putExtra("productId", productId);
                chechoutIntent.putExtra("productSize", productSize);
                chechoutIntent.putExtra("productDisc", productDisc);
                chechoutIntent.putExtra("productPrice", productprice);
                chechoutIntent.putExtra("product_name", productname);
                chechoutIntent.putExtra("shippingcost", shippingCost);
                chechoutIntent.putExtra("deliveryType", meetInperson);
                chechoutIntent.putExtra("paypalEmail", paypalEmail);
                startActivity(chechoutIntent);
            }
        });

        productEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProduct = new Intent(vp.mom.activitys.ProductDetails.this, vp.mom.activitys.SellingActivityEditProfile.class);
                editProduct.putExtra("product_id", productId);
                startActivity(editProduct);
            }
        });

        productDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(vp.mom.activitys.ProductDetails.this);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete this?");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.app_logo);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        deleteProduct();

                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

            }
        });

        likedImagell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Type = "3";
                if (likeStatus) {
                    likeCount = likeCount - 1;

                    updateLikesCounter(likeCount);
                    likedImage.setImageResource(R.drawable.ic_heart_outline_grey);
                    likeStatus = false;
                } else {
                    likeCount = likeCount + 1;
                    updateLikesCounter(likeCount);

                    likedImage.setImageResource(R.drawable.ic_heart_red);
                    likeStatus = true;
                }
                setLIkeProductToUser(productId, Type);


            }
        });
        commentview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(vp.mom.activitys.ProductDetails.this, vp.mom.activitys.CommentsActivity.class);

                commentIntent.putExtra("ProducID", productId);
                startActivity(commentIntent);
            }
        });
        imageshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        imageoptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(vp.mom.activitys.ProductDetails.this, v);
                //	popupMenu.setOnMenuItemClickListener(HomeListAdapter.this);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.show();

                if (bookMArkStatus)
                    popupMenu.getMenu().getItem(0).setTitle("Remove bookmark");

                else
                    popupMenu.getMenu().getItem(0).setTitle("Add bookmark");

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String TYPE = "2";
                        switch (item.getItemId()) {
//			case R.id.item_comedy:
//				Toast.makeText(activity, "Comedy Clicked", Toast.LENGTH_SHORT).show();
//				return true;
                            case R.id.item_bookmark:

                                setLIkeProductToUser(productId, TYPE);
                                //	Toast.makeText(activity, "item_bookmark Clicked", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.item_report_product:
                                Intent commentIntent = new Intent(vp.mom.activitys.ProductDetails.this, vp.mom.activitys.ReportProduct.class);

                                commentIntent.putExtra("ProducID", productId);
                                startActivity(commentIntent);
                                return true;

                            case R.id.item_chat:

                                startActivity(new Intent(vp.mom.activitys.ProductDetails.this,
                                        vp.mom.gcm.gcm.ChatRoomActivity.class).putExtra(
                                        "UserToChat", calledUserId).putExtra("name", "Chat"));

                                return true;

                        }

                        return false;
                    }
                });
            }
        });

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!calledUserId.equals(userId)) {

                    Intent msg = new Intent(vp.mom.activitys.ProductDetails.this, UserProfileDetailActivity.class);
                    msg.putExtra("userId", calledUserId);
                    startActivity(msg);
                    finish();
                }
            }
        });
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(vp.mom.app.Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    handlePushNotification(intent);
                }
            }
        };
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void deleteProduct() {
        String tag_string_req = "delete prod";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.DELETE_PRODUCT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("delete prod", "delete prod Response: " + response.toString());
                //	hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {
                        Toast.makeText(getApplicationContext(), "Your product is succesfully deleted ", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(vp.mom.activitys.ProductDetails.this,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(vp.mom.activitys.ProductDetails.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //   Log.e("delete prod", "delete prod Error: " + error.getMessage());
                Toast.makeText(vp.mom.activitys.ProductDetails.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //	hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
//                params.put("email", email);
//                params.put("password", password);

                params.put("product_id", productId);


                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);

        // if the push is of chat room message
        // simply update the UI unread messages count
        if (type == vp.mom.app.Config.PUSH_TYPE_CHATROOM) {
            msg = (String) intent.getSerializableExtra("message");

            chatRoomId = intent.getStringExtra("UserToChat");

            userName = intent.getStringExtra("userName");

//            UserChatListPojo feeditem=new UserChatListPojo();
//
//            feeditem.setUserName(userName);
//            feeditem.setUserId(chatRoomId);
//            feeditem.setLastMessage(msg);
//            feeditem.setUserImage("http://marketofmums.com/images/users/");
//
//            if (msg != null && chatRoomId != null) {
//                updateRow(chatRoomId, feeditem);
//            }
        }
        long when = System.currentTimeMillis();
        // app is in background. show the message in notification try
        Intent resultIntent = new Intent(getApplicationContext(), vp.mom.gcm.gcm.ChatRoomActivity.class);
        resultIntent.putExtra("UserToChat", chatRoomId);
        resultIntent.putExtra("name", userName);
        showNotificationMessage(getApplicationContext(), userName, msg,
                "" + when, resultIntent);

    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new vp.mom.gcm.gcm.NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(vp.mom.app.Config.PUSH_NOTIFICATION));

        // clearing the notification tray
        // NotificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void init() {

        productEditLL = (LinearLayout) findViewById(R.id.productEditLL);
        productll = (LinearLayout) findViewById(R.id.productll);
        productBuy = (Button) findViewById(R.id.productBuy);
        profilepic = (ImageView) findViewById(R.id.product_details_profilePic);
        name = (TextView) findViewById(R.id.userName_Product_Details);
        email = (TextView) findViewById(R.id.product_details_email);
        desc = (TextView) findViewById(R.id.product_details_desc);
        price = (TextView) findViewById(R.id.product_details_price);
        size = (TextView) findViewById(R.id.product_details_size);
        noOfLike = (TextSwitcher) findViewById(R.id.product_details_likes);
        likedImage = (ImageView) findViewById(R.id.likedimage);
        imageoptions = (ImageView) findViewById(R.id.imageoptions);
        commentview = (ImageView) findViewById(R.id.imageComment);
        imageshare = (ImageView) findViewById(R.id.imageshare);
        likedImagell = (LinearLayout) findViewById(R.id.likedImagell);
        timeStamp = (TextView) findViewById(R.id.prodtimestamp);
        productDelete = (Button) findViewById(R.id.productDelete);
        productEdit = (Button) findViewById(R.id.productEdit);
        productNameDet = (TextView) findViewById(R.id.productNameDet);
        imagearra = new ArrayList<>();
        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new ImagePagerAdapter(imagearra);
        viewPager.setAdapter(adapter);

        final vp.mom.circleindicator.CirclePageIndicator circleIndicator = (vp.mom.circleindicator.CirclePageIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(viewPager);
    }

    private void getData() {
        String tag_string_req = "GET_SPECIFIC_PRODUCT";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.GET_SPECIFIC_PRODUCT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //   //("", "ProductDetails: " + response.toString());
                hideDialog();

                if (response != null) {
                    JSONObject jsonresponse = null;
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
                //   Log.e("param", "ProductDetails Error: " + error.getMessage());
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                //  Log.e("productId",""+productId);
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("pid", productId);
                params.put("user_id", userId);

                return params;

            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */
    private void parseJsonFeed(JSONObject response) {

        try {

               Log.e("item click res", "" + response);


            if (response.getBoolean("status") && !response.isNull("items")) {
                JSONArray feedArrayProduct = response.getJSONArray("items");
                JSONObject feedObj = (JSONObject) feedArrayProduct.get(0);
                Picasso.with(this)
                        .load(feedObj.getString("photo"))
                        .placeholder(R.drawable.img_circle_placeholder)
                        .resize(avatarSize, avatarSize)
                        .centerCrop()
                        .transform(new CircleTransformation())
                        .into(profilepic);
                name.setText(feedObj.getString("username"));
                email.setText(feedObj.getString("product_location"));
                productNameDet.setText(feedObj.getString("brand") + ": " + feedObj.getString("name"));
                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                        Long.parseLong(feedObj.getString("timeStamp")),
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

                timeStamp.setText(timeAgo);
                bookMArkStatus = feedObj.getBoolean("Bookmark_status");
                //for image arry
                JSONArray feedArrayimages = feedObj.getJSONArray("images");

                for (int i = 0; i < feedArrayimages.length(); i++) {
                    JSONObject feedimage = (JSONObject) feedArrayimages.get(i);

                    imagearra.add(feedimage.getString("path").replace("th_", ""));
                }

                likeCount = Integer.parseInt(feedObj.getString("product_like"));

                noOfLike.setCurrentText(vp.mom.activitys.ProductDetails.this.getResources().getQuantityString(
                        R.plurals.likes_count, likeCount, likeCount
                ));
                desc.setText(feedObj.getString("description"));
                size.setText(feedObj.getString("size"));
                price.setText("£" + feedObj.getString("base_price"));
                likeStatus = feedObj.getBoolean("like_status");
                productId = feedObj.getString("product_id");
                productSize = feedObj.getString("size");
                productDisc = feedObj.getString("description");
                productprice = feedObj.getString("base_price");
                productname = feedObj.getString("name");
                shippingCost = feedObj.getString("delivery_cost");
                meetInperson = feedObj.getString("delivery_type");
                paypalEmail = feedObj.getString("paypal_registered_email");
                mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.blue),
                        new HashTagHelper.OnHashTagClickListener() {
                            @Override
                            public void onHashTagClicked(String hashTag) {

                                try {
                                    db.addseracheItem(hashTag);

                                } catch (Exception exp) {

                                }

                                Intent msg = new Intent(vp.mom.activitys.ProductDetails.this, vp.mom.searchtab.ItemSerachResult.class);
                                msg.putExtra("serachItemName", hashTag);
                                msg.putExtra("itemType", "1");
                                startActivity(msg);
                                //	activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
//Toast.makeText(activity,""+hashTag,1000).show();
                            }
                        });

                mTextHashTagHelper.handle(desc);


                if (likeStatus)
                    likedImage.setImageResource(R.drawable.ic_heart_red);
                else
                    likedImage.setImageResource(R.drawable.ic_heart_outline_grey);


            }
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {

            //  Log.e("ProductDetails", "" + e.toString());

            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();

    }

    private void updateLikesCounter(int toValue) {

        String likesCountTextTo = this.getResources().getQuantityString(
                R.plurals.likes_count, toValue, toValue
        );
        noOfLike.setText(likesCountTextTo);
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
                //  Log.e("Homelistadapter", "Homelistadapter Response: " + response.toString());
                //	hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {
                        if (type.equalsIgnoreCase("2")) {
                            bookMArkStatus = true;
                            Toast.makeText(vp.mom.activitys.ProductDetails.this,
                                    "Bookmark added", Toast.LENGTH_LONG).show();
                        }

//else


                    }
//                    else if(type.equalsIgnoreCase("3"))
//                    {
//
//                       // likeStatus=true;
//                    }
                    else {
                        bookMArkStatus = false;
                        if (type.equalsIgnoreCase("2")) {
                            Toast.makeText(vp.mom.activitys.ProductDetails.this,
                                    "Bookmark removed", Toast.LENGTH_LONG).show();
                        }

                        //    Log.e("Homelistadapter", "Login Response: " + response.toString());
                        // Error in login. Get the error message
//						String errorMsg = jObj.getString("message");
//						Toast.makeText(activity,
//								errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(vp.mom.activitys.ProductDetails.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //  Log.e("Homelistadapter", "Login Error: " + error.getMessage());
                Toast.makeText(vp.mom.activitys.ProductDetails.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //	hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
//                params.put("email", email);
//                params.put("password", password);
                //   Log.e("getStringSessionData", session.getStringSessionData("uid"));

                params.put("user_id", userId);
                params.put("product_id", productId);
                params.put("type", type);
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

    @Override
    public void onStart() {
        super.onStart();

        
    }


    private class ImagePagerAdapter extends PagerAdapter {
        ArrayList<String> imagearry;

        public ImagePagerAdapter(ArrayList<String> mimagearry) {
            this.imagearry = mimagearry;
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
            final Context context = vp.mom.activitys.ProductDetails.this;
            final ImageView imageView = new ImageView(context);
            final int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_top);
            imageView.setPadding(padding, padding, padding, padding);
            //    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            //   imageView.setImageResource(this.mImages[position]);

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
}
