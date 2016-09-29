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
import vp.mom.data.SalePurchaseItemPojo;


/**
 * Created by ApkDev2 on 05-11-2015.
 */
public class PurchaseReportAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;

    ArrayList<SalePurchaseItemPojo> soldItemarray;
    public PurchaseReportAdapter(Context mContext, ArrayList<SalePurchaseItemPojo> mSoldItemarray) {
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

  final      ViewHolder holder=new ViewHolder();

        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null)
            view = layoutInflater.inflate(R.layout.report_purchase_items_xml, null);
        holder.sold_item_title = (TextView) view.findViewById(R.id.sold_item_title);
        holder.sold_item_disc= (TextView) view.findViewById(R.id.sold_item_disc);
      holder.sold_item_price = (TextView) view.findViewById(R.id.sold_item_price);
       holder.soldImage= (ImageView) view.findViewById(R.id.sold_item_image);

        holder.solditemview=(LinearLayout)view.findViewById(R.id.solditemview);
        holder.mainllpurchase=(LinearLayout)view.findViewById(R.id.mainllpurchase);

    final SalePurchaseItemPojo data=soldItemarray.get(position);
        holder.sold_item_title.setText(data.getItemName());
        holder.sold_item_disc.setText("Purchased:"+data.getPurchaseDate());
        holder.sold_item_price.setText("£" + data.getItemPrice());

        holder.mainllpurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, vp.mom.activitys.PurchaseReport.class);
                intent.putExtra("product_id", data.getProduct_id());


                mContext.startActivity(intent);
            }
        });


        Picasso.with(mContext)
                .load(data.getItemImage())
//                    .placeholder(R.drawable.ic_facebook)

                .resize(180,
                        177)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.soldImage);

        return view;
    }

    public class ViewHolder
    {
        TextView sold_item_title,sold_item_price,sold_item_disc;
        ImageView soldImage;
        LinearLayout solditemview,mainllpurchase;


    }
}
