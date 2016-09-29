package vp.mom.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vp.mom.R;


/**
 * Created by ApkDev2 on 05-11-2015.
 */
public class purchasedItemsAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;

    ArrayList<vp.mom.data.SalePurchaseItemPojo> soldItemarray;
    public purchasedItemsAdapter(Context mContext, ArrayList<vp.mom.data.SalePurchaseItemPojo> mSoldItemarray) {
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
            view = layoutInflater.inflate(R.layout.custom_sold_items_xml, null);
        holder.sold_item_title = (TextView) view.findViewById(R.id.sold_item_title);
      //  TextView sold_item_disc= (TextView) view.findViewById(R.id.sold_item_disc);
      holder.sold_item_price = (TextView) view.findViewById(R.id.sold_item_price);
       holder.soldImage= (ImageView) view.findViewById(R.id.sold_item_image);
        holder.itemStatusbtn = (Button) view.findViewById(R.id.itemStatusbtn);
        holder.itemStatusbtn.setVisibility(View.GONE);

        holder.solditemview=(LinearLayout)view.findViewById(R.id.solditemview);
        holder.mainllpurchase=(LinearLayout)view.findViewById(R.id.mainllpurchase);

        holder.sold_item_rating= (RatingBar) view.findViewById(R.id.sold_item_rating);

        final vp.mom.data.SalePurchaseItemPojo data=soldItemarray.get(position);
        holder.sold_item_title.setText(data.getItemName());
      //  sold_item_disc.setText(data.getItemDesc());
        holder.sold_item_price.setText("£" + data.getItemPrice());

        holder.mainllpurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, vp.mom.activitys.PurchaseItemInvoiceActivity.class);
                intent.putExtra("product_id",data.getProduct_id());
                intent.putExtra("ord_det_id",data.getOrder_id());

                mContext.startActivity(intent);
            }
        });

        holder.sold_item_rating.setRating(data.getProductRating());


//        Target target = new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                holder.soldImage.setImageBitmap(bitmap);
//                //   Bitmap bitmap = ((BitmapDrawable) viewHolder.soldImage.getDrawable()).getBitmap();
//                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                    @Override
//                    public void onGenerated(Palette palette) {
//                        int bgColor = palette.getVibrantColor(mContext.getResources().getColor(R.color.ColorPrimary));
//                        holder.solditemview.setBackgroundColor(bgColor);
//                        if (StaticData.isColorDark(bgColor)) {
//                            holder.sold_item_title.setTextColor(mContext.getResources().getColor(R.color.white));
//                          //  viewHolder.sold_item_disc.setTextColor(mContext.getResources().getColor(R.color.white));
//                            holder.sold_item_price.setTextColor(mContext.getResources().getColor(R.color.white));
//                        } else {
//                            holder.sold_item_title.setTextColor(mContext.getResources().getColor(R.color.black));
//                            //holder.sold_item_disc.setTextColor(mContext.getResources().getColor(R.color.black));
//                            holder.sold_item_price.setTextColor(mContext.getResources().getColor(R.color.black));
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//            }
//
//
//        };
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
        TextView sold_item_title,sold_item_price;
        ImageView soldImage;
        LinearLayout solditemview,mainllpurchase;
        RatingBar sold_item_rating;
        Button itemStatusbtn;
    }
}
