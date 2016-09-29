package vp.mom.activitys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import vp.mom.adapters.CommentsAdapter;
import vp.mom.app.AppController;
import vp.mom.app.SendCommentButton;
import vp.mom.app.Utils;

public class CommentsActivity extends AppCompatActivity implements SendCommentButton.OnSendClickListener {
    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";
    String TAG= vp.mom.activitys.CommentsActivity.class.getSimpleName();
    vp.mom.data.CommentPojo feeditem;
    private ProgressDialog pDialog;
    LinearLayout contentRoot;
    RecyclerView rvComments;
    LinearLayout llAddComment;
    EditText etComment;
    SendCommentButton btnSendComment;
    Toolbar toolbar;
    private CommentsAdapter commentsAdapter;
    private int drawingStartLocation;
   // ImageView back;
    TextView titletool;
    ArrayList<vp.mom.data.CommentPojo> commentData;
    vp.mom.api.SessionManager session;
    String productId="00";
    String commentText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        toolbar= (Toolbar) findViewById(R.id.toolbarcomment);
        session=new vp.mom.api.SessionManager(this);
        pDialog = new ProgressDialog(vp.mom.activitys.CommentsActivity.this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        try
        {
            productId=getIntent().getStringExtra("ProducID");
        }
        catch (Exception e)
        {}


        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    ViewCompat.setElevation(toolbar, 0);
                    contentRoot.animate()
                            .translationY(Utils.getScreenHeight(vp.mom.activitys.CommentsActivity.this))
                            .setDuration(200)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    vp.mom.activitys.CommentsActivity.super.onBackPressed();
                                    overridePendingTransition(0, 0);
                                }
                            })
                            .start();
                }

                catch (Exception e){}

            }
        });



        titletool= (TextView) findViewById(R.id.titletool);
        titletool.setText("Comments");

        contentRoot= (LinearLayout) findViewById(R.id.contentRoot);
        rvComments= (RecyclerView) findViewById(R.id.rvComments);
        llAddComment= (LinearLayout) findViewById(R.id.llAddComment);
        etComment= (EditText) findViewById(R.id.etComment);
        btnSendComment= (SendCommentButton) findViewById(R.id.btnSendComment);
        setupComments();


        setupSendCommentButton();
        drawingStartLocation = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0);
        if (savedInstanceState == null) {
            contentRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    contentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    startIntroAnimation();
                    return true;
                }
            });
        }

        getData();
    }
    private void startIntroAnimation() {
        ViewCompat.setElevation(toolbar, 0);
        contentRoot.setScaleY(0.1f);
        contentRoot.setPivotY(drawingStartLocation);
        llAddComment.setTranslationY(200);

        contentRoot.animate()
                .scaleY(1)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ViewCompat.setElevation(toolbar, Utils.dpToPx(8));
                        animateContent();
                    }
                })
                .start();
    }
    private void animateContent() {
     //   commentsAdapter.updateItems();
        llAddComment.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(200)
                .start();
    }

    @Override
    public void onBackPressed() {
        ViewCompat.setElevation(toolbar, 0);
        contentRoot.animate()
                .translationY(Utils.getScreenHeight(this))
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        vp.mom.activitys.CommentsActivity.super.onBackPressed();
                        overridePendingTransition(0, 0);
                    }
                })
                .start();
    }
    private void setupComments() {

        commentData=new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.setHasFixedSize(true);

        commentsAdapter = new CommentsAdapter(this,commentData);
        rvComments.setAdapter(commentsAdapter);
        rvComments.setOverScrollMode(View.OVER_SCROLL_NEVER);
        rvComments.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    commentsAdapter.setAnimationsLocked(true);
                }
            }
        });
    }

    private void setupSendCommentButton() {
        btnSendComment.setOnSendClickListener(this);
    }

    @Override
    public void onSendClickListener(View v) {
        if (validateComment()) {
         //   CommentPojo        feeditem1=new CommentPojo();
         //   commentsAdapter.addItem();

            commentText=etComment.getText().toString();
           // feeditem1.setCommentText(commentText);
          //  commentData.add(feeditem1);
         //   commentsAdapter.notifyDataSetChanged();
           commentsAdapter.setAnimationsLocked(false);
            commentsAdapter.setDelayEnterAnimation(false);
          //  rvComments.smoothScrollBy(0, rvComments.getChildAt(0).getHeight() * commentsAdapter.getItemCount());
            etComment.setText(null);
            btnSendComment.setCurrentState(SendCommentButton.STATE_DONE);
            AddComments();
        }
    }

    private boolean validateComment() {
        if (TextUtils.isEmpty(etComment.getText())) {
            btnSendComment.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_error));
            return false;
        }

        return true;
    }
    private void getData() {
        String tag_string_req = "ExploreItemFragment";

        pDialog.setMessage("Loading ...");
       // showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.GET_COMMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             //   Log.e("Login Response:", "Login Response: " + response.toString());
             //   hideDialog();
                if (response != null) {

                   // commentData.clear();
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
            ///    Log.e("param", "Login Error: " + error.getMessage());

              //  hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("product_id",productId);
                params.put("user_id",session.getStringSessionData("uid"));
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
        commentData.clear();

     //   Log.e(TAG,""+response);
        try {
            JSONArray feedArray = response.getJSONArray("items");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);


                feeditem=new vp.mom.data.CommentPojo();

                feeditem.setCommentText(feedObj.getString("comment"));
                feeditem.setUserImage(feedObj.getString("photo"));
                feeditem.setCommentId(feedObj.getString("comment_id"));
                feeditem.setStatus(feedObj.getBoolean("status"));
                feeditem.setUserName(feedObj.getString("username"));
                commentData.add(feeditem);

            }
            // notify data changes to list adapater

        } catch (JSONException e) {

       //     Log.e(TAG,""+e.toString());

            e.printStackTrace();
        }
        commentsAdapter.notifyDataSetChanged();
       // swipeRefreshLayout.setRefreshing(false);
    }
    private void AddComments() {
        String tag_string_req = "AddComments";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.ADD_COMMENTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("ADD_COMMENTS", "ADD_COMMENTS Response: " + response.toString());
                //	hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {

                        getData();
                        // user successfully logged in
                      //  Log.e("ADD_COMMENTS", "ADD_COMMENTS Response: " + response.toString());

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(vp.mom.activitys.CommentsActivity.this,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                   // Toast.makeText(CommentsActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
             //   Log.e("ADD_COMMENTS", "ADD_COMMENTS Error: " + error.getMessage());
//                Toast.makeText(CommentsActivity.this,
//                        error.getMessage(), Toast.LENGTH_LONG).show();
                //	hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",session.getStringSessionData("uid"));
                params.put("product_id",productId);
                params.put("comment",commentText);


                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
