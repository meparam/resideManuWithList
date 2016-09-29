package vp.mom.gcm.gcm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import vp.mom.R;

public class ChatRoomThreadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = vp.mom.gcm.gcm.ChatRoomThreadAdapter.class.getSimpleName();

    private String userId;
    private int SELF = 100;
    private static String today;

    private Context mContext;
    private ArrayList<vp.mom.data.PrivateChatPojo> messageArrayList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView messageStatus;
        public TextView messageTextView;
        public TextView timeTextView;

        public ViewHolder(View view) {
            super(view);

            messageTextView = (TextView) itemView.findViewById(R.id.message_text);
            timeTextView = (TextView) itemView.findViewById(R.id.time_text);
            messageStatus = (ImageView) itemView.findViewById(R.id.user_reply_status);
          //  userName= (TextView) itemView.findViewById(R.id.chat_company_reply_author);

        }
    }


    public ChatRoomThreadAdapter(Context mContext, ArrayList<vp.mom.data.PrivateChatPojo> messageArrayList, String userId) {
        this.mContext = mContext;
        this.messageArrayList = messageArrayList;
        this.userId = userId;
        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        // view type is to identify where to render the chat message
        // left or right
        if (viewType == SELF) {
            // self message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_user2_item, parent, false);

        } else {
            // others message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_user1_item, parent, false);
        }


        return new ViewHolder(itemView);
    }


    @Override
    public int getItemViewType(int position) {
        vp.mom.data.PrivateChatPojo message = messageArrayList.get(position);
        if (message.getFromUser().equals(userId)) {
            return SELF;
        }

        return position;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        vp.mom.data.PrivateChatPojo message = messageArrayList.get(position);

        ((ViewHolder) holder).messageTextView.setText(message.getChatMessage());





        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(message.getMsgTime()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        ((ViewHolder) holder).timeTextView.setText("" + timeAgo);

    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
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

}

