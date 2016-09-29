package vp.mom.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vp.mom.R;


/**
 * Created by ApkDev2 on 07-11-2015.
 */
public class FollowerAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    ArrayList<vp.mom.data.FollowerPojo> bookMArkList;
    private int avatarSize;
   // String productId;
    public FollowerAdapter(Context mContext, ArrayList<vp.mom.data.FollowerPojo> mbookMArkList) {
        this.mContext = mContext;
        this.bookMArkList=mbookMArkList;
        this.avatarSize = mContext.getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);
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
            view = layoutInflater.inflate(R.layout.follower_list_row, null);
        TextView bookmarkdesc= (TextView) view.findViewById(R.id.follower_name);
        TextView bookmarktime= (TextView) view.findViewById(R.id.follower_timestamp);
        ImageView bookmark_product_image= (ImageView) view.findViewById(R.id.follower_product_image);
        final vp.mom.data.FollowerPojo item = bookMArkList.get(position);
  //    LinearLayout mainllbookmark=(LinearLayout)view.findViewById(R.id.mainllbookmark);


        Picasso.with(mContext)
                .load(item.getImageUrl())
                .placeholder(R.drawable.img_circle_placeholder).transform(new vp.mom.api.CircleTransformation())
                .into(bookmark_product_image);
        bookmarkdesc.setText(item.getUserName());

        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(item.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        bookmarktime.setText(timeAgo);
      //  productId=item.getProductId();
//        mainllbookmark.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//              //  Log.e("mainllbookmark",""+productId);
//
//                Intent seelingintent = new Intent(mContext, ProductDetails.class);
//                seelingintent.putExtra("productId",item.getProductId());
//                mContext.startActivity(seelingintent);
//
//            }
//        });

        return view;
    }
}
