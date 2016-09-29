package vp.mom.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vp.mom.R;
import vp.mom.api.CircleTransformation;
import vp.mom.app.AppController;

/**
 * Created by froger_mcs on 11.11.14.
 */
public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int itemsCount = 0;
    private int lastAnimatedPosition = -1;
    private int avatarSize;
    vp.mom.api.SessionManager session;
    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    ArrayList<vp.mom.data.CommentPojo> commentData;
    public CommentsAdapter(Context context,ArrayList<vp.mom.data.CommentPojo> mcommentData) {
        this.context = context;
        avatarSize = context.getResources().getDimensionPixelSize(R.dimen.comment_avatar_size);
        this.commentData=mcommentData;
        session=new vp.mom.api.SessionManager(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        runEnterAnimation(viewHolder.itemView, position);
        CommentViewHolder holder = (CommentViewHolder) viewHolder;

        final vp.mom.data.CommentPojo commentItem=commentData.get(position);

        holder.tvComment.setText(commentItem.getCommentText());


        Picasso.with(context)
                .load(commentItem.getUserImage())
                .placeholder(R.drawable.img_circle_placeholder).transform(new CircleTransformation())
                .into(holder.ivUserAvatar);

        if(commentItem.isStatus())
            holder.removeComment.setVisibility(View.VISIBLE);
        else
            holder.removeComment.setVisibility(View.INVISIBLE);

        holder.removeCommentll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //    Toast.makeText(context,""+commentItem.getCommentId(),1000).show();
                Remove_comment(commentItem.getCommentId(), position);

            }
        });

        if(commentItem.isStatus())
            holder.commentUserName.setText("Me");

        else
            holder.commentUserName.setText("@ " + commentItem.getUserName());



   }
    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    })
                    .start();
        }
    }

    @Override
    public int getItemCount() {
        return commentData.size();
    }

//    public void updateItems() {
//        commentData.size();
//        notifyDataSetChanged();
//    }
//
//    public void addItem() {
//
//        notifyItemInserted(commentData.size() - 1);
//    }

    public void setAnimationsLocked(boolean animationsLocked) {
        this.animationsLocked = animationsLocked;
    }

    public void setDelayEnterAnimation(boolean delayEnterAnimation) {
        this.delayEnterAnimation = delayEnterAnimation;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
       // @Bind(R.id.ivUserAvatar)
        ImageView ivUserAvatar;
        ImageView removeComment;
        LinearLayout removeCommentll;
      //  @Bind(R.id.tvComment)
        TextView tvComment,commentUserName;
     public CommentViewHolder(View view) {
            super(view);
         removeComment= (ImageView) view.findViewById(R.id.removeComment);
            tvComment= (TextView) view.findViewById(R.id.tvComment);
            ivUserAvatar= (ImageView) view.findViewById(R.id.ivUserAvatar);
         commentUserName= (TextView) view.findViewById(R.id.commentUserName);

         removeCommentll= (LinearLayout) view.findViewById(R.id.removeCommentll);

         removeCommentll.setClickable(true);


    }
    }


    private void Remove_comment(final String commentId, final int position) {
        String tag_string_req = "Remove_comment";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.REMOVE_COMMENT_BY_ID, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
             //   Log.e("Remove_comment", "Remove_comment Response: " + response.toString());
                //	hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {

                        commentData.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, commentData.size());

                        // user successfully logged in
                   //     Log.e("Remove_comment", "Remove_comment Response: " + response.toString());

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("items");
                        Toast.makeText(context,
                                errorMsg, Toast.LENGTH_LONG).show();

//                        commentData.remove(position);
//                        notifyItemRemoved(position);
//                        notifyItemRangeChanged(position, commentData.size());
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(context, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
              //  Log.e("Remove_comment", "Remove_comment Error: " + error.getMessage());
                Toast.makeText(context,
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

                params.put("user_id",session.getStringSessionData("uid"));
                params.put("comment_id",commentId);



                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
