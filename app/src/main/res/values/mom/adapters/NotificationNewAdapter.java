package vp.mom.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vp.mom.R;
import vp.mom.api.CircleTransformation;
import vp.mom.data.NotificationPojo;
import vp.mom.searchtab.UserProfileDetailActivity;


/**
 * Created by ApkDev2 on 07-11-2015.
 */
public class NotificationNewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    ArrayList<NotificationPojo> bookMArkList;
    private int avatarSize;
   // String productId;
   private vp.mom.api.SessionManager session;
    public NotificationNewAdapter(Context mContext, ArrayList<NotificationPojo> mbookMArkList) {
        this.mContext = mContext;
        this.bookMArkList=mbookMArkList;
        this.avatarSize = mContext.getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);
        session=new vp.mom.api.SessionManager(mContext);
    }

    @Override
    public int getCount() {
        return bookMArkList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookMArkList.get(position);
    }

    @Override
    public long getItemId( int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null)
            view = layoutInflater.inflate(R.layout.custom_bookmarks_xml, null);
        TextView bookmarkdesc= (TextView) view.findViewById(R.id.bookmarkdesc);
        TextView bookmarktime= (TextView) view.findViewById(R.id.bookmarktime);
        ImageView bookmark_product_image= (ImageView) view.findViewById(R.id.bookmark_product_image);
        ImageView productimage= (ImageView) view.findViewById(R.id.productimage);

   LinearLayout     mainllNotification= (LinearLayout) view.findViewById(R.id.mainllNotification);

        LinearLayout     productcomment= (LinearLayout) view.findViewById(R.id.productcomment);


        final NotificationPojo item = bookMArkList.get(position);
    //  LinearLayout mainllbookmark=(LinearLayout)view.findViewById(R.id.mainllbookmark);


        Picasso.with(mContext)
                .load(item.getUserImage())
                .placeholder(R.drawable.img_circle_placeholder).transform(new CircleTransformation())
                .into(bookmark_product_image);
        bookmarkdesc.setText(item.getNotificationMsg());
        bookmarktime.setText(item.getTime());

        mainllNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msg=new Intent(mContext, UserProfileDetailActivity.class);
                msg.putExtra("userId",item.getUserId());
                msg.putExtra("userItself",false);
                mContext.startActivity(msg);
            }
        });


        try
        {
            if(!item.getProductID().equalsIgnoreCase("0"))
            {
                productcomment.setVisibility(View.VISIBLE);



                Picasso.with(mContext)
                        .load(item.getProducrImage())
                        .placeholder(R.drawable.img_circle_placeholder).transform(new CircleTransformation())
                        .into(bookmark_product_image);

            }
            else
            {
                productcomment.setVisibility(View.GONE);
            }

        }
        catch (Exception except)
        {

        }
        productcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent commentIntent = new Intent(mContext, vp.mom.activitys.CommentsActivity.class);
                commentIntent.putExtra("calledUserId",session.getStringSessionData("uid"));
                commentIntent.putExtra("productId",""+item.getProductID());
                mContext.startActivity(commentIntent);
            }
        });

        return view;
    }
}
