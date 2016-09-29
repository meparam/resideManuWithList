package vp.mom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vp.mom.R;
import vp.mom.data.SalePurchaseItemPojo;


/**
 * Created by ApkDev2 on 05-11-2015.
 */
public class SoldItemsAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;

    ArrayList<SalePurchaseItemPojo> soldItemarray;
    public SoldItemsAdapter(Context mContext,ArrayList<SalePurchaseItemPojo> mSoldItemarray) {
        this.mContext = mContext;
this.soldItemarray=mSoldItemarray;
    }

    @Override
    public int getCount() {
        return soldItemarray.size();
    }

    @Override
    public Object getItem(int location) {
        return soldItemarray.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null)
            view = layoutInflater.inflate(R.layout.custom_sold_items_xml, null);
     TextView sold_item_title= (TextView) view.findViewById(R.id.sold_item_title);
        TextView sold_item_disc= (TextView) view.findViewById(R.id.sold_item_disc);
        TextView sold_item_price= (TextView) view.findViewById(R.id.sold_item_price);
        ImageView soldImage= (ImageView) view.findViewById(R.id.sold_item_image);

        SalePurchaseItemPojo data=soldItemarray.get(position);
        sold_item_title.setText(data.getItemName());
        sold_item_disc.setText(data.getItemDesc());
        sold_item_price.setText("$" + data.getItemPrice());

        Picasso.with(mContext)
                .load(data.getItemImage())
//                    .placeholder(R.drawable.ic_facebook)
                .centerCrop()
                .resize(180,
                        177)
                .error(android.R.drawable.stat_notify_error)
                .into(soldImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });

        return view;
    }
}
