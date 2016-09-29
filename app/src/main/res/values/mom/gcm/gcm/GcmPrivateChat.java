//package vp.mom.gcm.gcm;
//
//import android.content.Context;
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.InputType;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.view.animation.AnimationUtils;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import vp.mom.R;
//import vp.mom.adapters.CommentsAdapter;
//import vp.mom.adapters.PrivateChatAadapter;
//import vp.mom.api.AppConfig;
//import vp.mom.api.SessionManager;
//import vp.mom.app.AppController;
//import vp.mom.app.SendCommentButton;
//import vp.mom.chat.Conversation;
//import vp.mom.data.CommentPojo;
//import vp.mom.data.PrivateChatPojo;
//
//public class GcmPrivateChat extends AppCompatActivity implements SendCommentButton.OnSendClickListener{
//    SendCommentButton btnSendComment;
//    SessionManager session;
//   String to_user,commentText,MyUserID;
//    EditText etComment;
//    PrivateChatAadapter commentsAdapter;
//  RecyclerView rvChatMsg;
//    ArrayList<PrivateChatPojo> chattData;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.gcm_private_chat);
//        session=new SessionManager(this);
//        MyUserID=session.getStringSessionData("uid");
//        Intent intentMsg=getIntent();
//        if(intentMsg!=null)
//        {
//            to_user=intentMsg.getStringExtra("UserToChat");
//        }
//        btnSendComment= (SendCommentButton) findViewById(R.id.btnSendmsg);
//        btnSendComment.setOnSendClickListener(this);
//        etComment = (EditText) findViewById(R.id.inputMsg);
//        etComment.setInputType(InputType.TYPE_CLASS_TEXT
//                | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//        rvChatMsg= (RecyclerView) findViewById(R.id.rvChatMsg);
//        setupComments();
//    }
//
//    private void setupComments() {
//
//        chattData=new ArrayList<>();
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        rvChatMsg.setLayoutManager(linearLayoutManager);
//        rvChatMsg.setHasFixedSize(true);
//
//        commentsAdapter = new PrivateChatAadapter(this,chattData);
//        rvChatMsg.setAdapter(commentsAdapter);
//        rvChatMsg.setOverScrollMode(View.OVER_SCROLL_NEVER);
//        rvChatMsg.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
//                    commentsAdapter.setAnimationsLocked(true);
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onSendClickListener(View v) {
//
//        if (validateComment()) {
//            commentText=etComment.getText().toString();
//
//            PrivateChatPojo chatItem=new PrivateChatPojo();
//            chatItem.setFromUser(MyUserID);
//            chatItem.setToUser(to_user);
//            chatItem.setChatMessage(commentText);
//
//            chattData.add(chatItem);
//            // feeditem1.setCommentText(commentText);
//            //  commentData.add(feeditem1);
//            //   commentsAdapter.notifyDataSetChanged();
//          //  commentsAdapter.setAnimationsLocked(false);
//          //  commentsAdapter.setDelayEnterAnimation(false);
//
//            //  rvComments.smoothScrollBy(0, rvComments.getChildAt(0).getHeight() * commentsAdapter.getItemCount());
//            etComment.setText(null);
//            btnSendComment.setCurrentState(SendCommentButton.STATE_DONE);
//
//            commentsAdapter.notifyDataSetChanged();
//            sendMessage();
//        }
//    }
//    private boolean validateComment() {
//        if (TextUtils.isEmpty(etComment.getText())) {
//            btnSendComment.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_error));
//            return false;
//        }
//
//        return true;
//    }
//    private void sendMessage() {
//        String tag_string_req = "AddComments";
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                AppConfig.Send_Private_Chat, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d("ADD_COMMENTS", "ADD_COMMENTS Response: " + response.toString());
//                //	hideDialog();
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    boolean status = jObj.getBoolean("status");
//                    // Check for error node in json
//                    if (status) {
//
//                    //    getData();
//                        // user successfully logged in
//                        Log.e("ADD_COMMENTS", "ADD_COMMENTS Response: " + response.toString());
//
//                    } else {
//                        // Error in login. Get the error message
//                        String errorMsg = jObj.getString("message");
//                        Toast.makeText(GcmPrivateChat.this,
//                                errorMsg, Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    // JSON error
//                    e.printStackTrace();
//                    Toast.makeText(GcmPrivateChat.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("ADD_COMMENTS", "ADD_COMMENTS Error: " + error.getMessage());
//                Toast.makeText(GcmPrivateChat.this,
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                //	hideDialog();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
////                params.put("email", email);
////                params.put("password", password);
//
//                params.put("from_user",MyUserID);
//                params.put("to_user",to_user);
//                params.put("message",commentText);
//
//
//                return params;
//            }
//
//        };
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//    }
//
////    private void getData() {
////        String tag_string_req = "ExploreItemFragment";
////
////     //   pDialog.setMessage("Loading ...");
////        // showDialog();
////
////        StringRequest strReq = new StringRequest(Request.Method.POST,
////                AppConfig.GET_COMMENT, new Response.Listener<String>() {
////            @Override
////            public void onResponse(String response) {
////                Log.e("Login Response:", "Login Response: " + response.toString());
////                //   hideDialog();
////                if (response != null) {
////
////                    // commentData.clear();
////                    JSONObject jsonresponse= null;
////                    try {
////                        jsonresponse = new JSONObject(response.toString());
////
////                        parseJsonFeed(jsonresponse);
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////                }
////            }
////        }, new Response.ErrorListener() {
////
////            @Override
////            public void onErrorResponse(VolleyError error) {
////                Log.e("param", "Login Error: " + error.getMessage());
////
////                //  hideDialog();
////            }
////        }) {
////
////            @Override
////            protected Map<String, String> getParams() {
////                // Posting parameters to login url
////                Map<String, String> params = new HashMap<String, String>();
////                params.put("product_id",productId);
////                params.put("user_id",session.getStringSessionData("uid"));
////                return params;
////            }
////
////        };
////
////        // Adding request to request queue
////        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
////
////    }
//}
