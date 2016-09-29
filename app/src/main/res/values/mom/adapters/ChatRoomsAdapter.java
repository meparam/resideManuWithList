package vp.mom.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import vp.mom.R;
import vp.mom.api.CircleTransformation;
import vp.mom.searchtab.UserProfileDetailActivity;


public class ChatRoomsAdapter extends RecyclerView.Adapter<vp.mom.adapters.ChatRoomsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<vp.mom.data.UserChatListPojo> chatRoomArrayList;
    private static String today;
    private int avatarSize;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, message,count;
ImageView imageuserchatlist,ChatprodImage;
        LinearLayout chatLL,userProfile;
        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.chatuserName);
            message = (TextView) view.findViewById(R.id.chatmessage);
        //    timestamp = (TextView) view.findViewById(R.id.timestamp);
        //    count = (TextView) view.findViewById(R.id.count);
            imageuserchatlist= (ImageView) view.findViewById(R.id.imageuserchatlist);
            ChatprodImage= (ImageView) view.findViewById(R.id.ChatprodImage);
            count = (TextView) view.findViewById(R.id.count);

            chatLL= (LinearLayout) view.findViewById(R.id.chatLL);
            userProfile= (LinearLayout) view.findViewById(R.id.userProfile);

        }
    }


    public ChatRoomsAdapter(Context mContext, ArrayList<vp.mom.data.UserChatListPojo> chatRoomArrayList) {
        this.mContext = mContext;
        this.chatRoomArrayList = chatRoomArrayList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        this.avatarSize = mContext.getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_rooms_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        vp.mom.data.UserChatListPojo chatRoom = chatRoomArrayList.get(position);
        holder.name.setText(chatRoom.getUserName());

        Picasso.with(mContext)
                .load(chatRoom.getUserImage())
                .placeholder(R.drawable.img_circle_placeholder).transform(new CircleTransformation())
                .into(holder.imageuserchatlist);




        if (chatRoom.getUnreadCount() > 0) {
            holder.count.setText(String.valueOf(chatRoom.getUnreadCount()));
            holder.count.setVisibility(View.VISIBLE);
        } else {
            holder.count.setVisibility(View.GONE);
        }
        holder.message.setText(chatRoom.getLastMessage());


        holder.chatLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vp.mom.data.UserChatListPojo chatRoom = chatRoomArrayList.get(position);
                chatRoom.setUnreadCount(0);
                notifyDataSetChanged();

                Intent intent = new Intent(mContext, vp.mom.gcm.gcm.ChatRoomActivity.class);
                intent.putExtra("UserToChat", chatRoom.getUserId());
                intent.putExtra("name", chatRoom.getUserName());
                //  intent.putExtra("userImage", chatRoom.getUserImage());
                mContext.startActivity(intent);
            }
        });

        holder.userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent msg=new Intent(mContext, UserProfileDetailActivity.class);

                msg.putExtra("userId",chatRoom.getUserId());
                msg.putExtra("userItself",false);
                mContext.startActivity(msg);
            }
        });

     //   ChatprodImage

        //    holder.message.setText(chatRoom.getLastMessage());
//        if (chatRoom.getUnreadCount() > 0) {
//            holder.count.setText(String.valueOf(chatRoom.getUnreadCount()));
//            holder.count.setVisibility(View.VISIBLE);
//        } else {
//            holder.count.setVisibility(View.GONE);
//        }

      //  holder.timestamp.setText(getTimeStamp(chatRoom.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return chatRoomArrayList.size();
    }

    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private vp.mom.adapters.ChatRoomsAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final vp.mom.adapters.ChatRoomsAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
