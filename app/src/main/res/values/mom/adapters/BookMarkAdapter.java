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
import vp.mom.activitys.ProductDetails;
import vp.mom.api.CircleTransformation;


/**
 * Created by ApkDev2 on 07-11-2015.
 */
public class BookMarkAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    ArrayList<vp.mom.data.BookMarkPojo> bookMArkList;
    private int avatarSize;
   // String productId;
    public BookMarkAdapter(Context mContext,ArrayList<vp.mom.data.BookMarkPojo> mbookMArkList) {
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
            view = layoutInflater.inflate(R.layout.custom_bookmarks_xml, null);
        TextView bookmarkdesc= (TextView) view.findViewById(R.id.bookmarkdesc);
        TextView bookmarktime= (TextView) view.findViewById(R.id.bookmarktime);
        ImageView bookmark_product_image= (ImageView) view.findViewById(R.id.bookmark_product_image);
        final vp.mom.data.BookMarkPojo item = bookMArkList.get(position);
      LinearLayout mainllbookmark=(LinearLayout)view.findViewById(R.id.mainllNotification);


        Picasso.with(mContext)
                .load(item.getProductUrl())
                .placeholder(R.drawable.img_circle_placeholder).transform(new CircleTransformation())
                .into(bookmark_product_image);


        bookmarkdesc.setText(item.getProductDisc());
        bookmarktime.setText(item.getProductTime());
      //  productId=item.getProductId();
        mainllbookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  Log.e("mainllbookmark",""+productId);

                Intent seelingintent = new Intent(mContext, ProductDetails.class);
                seelingintent.putExtra("productId",item.getProductId());
                seelingintent.putExtra("calledUserId", "00000000");
                mContext.startActivity(seelingintent);

            }
        });

        return view;
    }
}
