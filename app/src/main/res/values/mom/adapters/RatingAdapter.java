package vp.mom.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vp.mom.R;
import vp.mom.activitys.ProductDetails;
import vp.mom.api.CircleTransformation;
import vp.mom.searchtab.UserProfileDetailActivity;


/**
 * Created by ApkDev2 on 07-11-2015.
 */
public class RatingAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    ArrayList<vp.mom.data.RatingPojo> bookMArkList;
    private int avatarSize;
    private vp.mom.api.SessionManager session;
    public RatingAdapter(Context mContext, ArrayList<vp.mom.data.RatingPojo> mbookMArkList) {
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
            view = layoutInflater.inflate(R.layout.rating_list_item, null);
       // Float.valueOf(data.getString("ratings")
        TextView userName= (TextView) view.findViewById(R.id.ratinguserName);
        TextView prodDisc= (TextView) view.findViewById(R.id.prodDisc);
        ImageView product_image= (ImageView) view.findViewById(R.id.productImage);
        ImageView user_image= (ImageView) view.findViewById(R.id.userImage);
        RatingBar   rating= (RatingBar) view.findViewById(R.id.sold_item_rating);


    //    LinearLayout     mainllNotification= (LinearLayout) view.findViewById(R.id.mainllRating);

        final vp.mom.data.RatingPojo item = bookMArkList.get(position);

        rating.setRating(Float.valueOf(item.getRating()));

        Picasso.with(mContext)
                .load(item.getUserImage())
                .placeholder(R.drawable.img_circle_placeholder).transform(new CircleTransformation())
                .into(user_image);



        Picasso.with(mContext)
                .load(item.getProductImage())
                .placeholder(R.drawable.img_circle_placeholder).transform(new CircleTransformation())
                .into(product_image);

      userName.setText(item.getUserName());

        prodDisc.setText(item.getRatingDisc());

        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msg = new Intent(mContext, UserProfileDetailActivity.class);
                msg.putExtra("userId", item.getUserID());
                msg.putExtra("userItself", false);
                mContext.startActivity(msg);
            }
        });

        product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msg=new Intent(mContext, ProductDetails.class);
                msg.putExtra("productId",item.getProductId());
                msg.putExtra("calledUserId",session.getStringSessionData("uid"));

                mContext.startActivity(msg);
            }
        });

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
