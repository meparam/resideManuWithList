package vp.mom.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import vp.mom.R;
import vp.mom.activitys.ProductDetails;


public class UserLikeAdapter extends RecyclerView.Adapter<vp.mom.adapters.UserLikeAdapter.ViewHolder> {

    private List<vp.mom.data.UserLikePojo> countries;
    private int rowLayout;
    private Context mContext;
    vp.mom.api.SessionManager session;
    private Activity activity;
    // 2
    protected boolean animated = false;
    private static final int SCALE_DELAY = 30;
    public UserLikeAdapter(Activity activity, List<vp.mom.data.UserLikePojo> msuggestedItems) {

        this.countries=msuggestedItems;
        this.mContext = activity;
        session = new vp.mom.api.SessionManager(mContext);
    //    Log.e("Explore Item ", "" + countries.size());
    }

    // 3
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView placeImage;
        RelativeLayout mainLLselling;
        LinearLayout placeHolder;
        View view;
        TextView placeName;

        public ViewHolder(View itemView) {
            super(itemView);

             this.view = itemView;
            placeHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
            mainLLselling= (RelativeLayout) itemView.findViewById(R.id.mainLLselling);
            placeHolder.setVisibility(View.GONE);
                 placeName = (TextView) itemView.findViewById(R.id.placeName);
            //    placeNameHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
            placeImage = (ImageView) itemView.findViewById(R.id.sellingImage);
            mainLLselling.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    // 2
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selling_row_item, parent, false);
        return new ViewHolder(view);
    }

    // 3
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final vp.mom.data.UserLikePojo item= countries.get(position);

       Picasso.with(mContext)
                .load(item.getImageUrl())
                .into(holder.placeImage);

        if(item.getIsSold().equalsIgnoreCase("1"))

            holder.placeHolder.setVisibility(View.VISIBLE);
        else
            holder.placeHolder.setVisibility(View.GONE);

//        holder.view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//              //  Toast.makeText(mContext, "hello", 1000).show();
//            }
//        });
        holder.mainLLselling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   Toast.makeText(mContext, "Recycle Click" + position, Toast.LENGTH_SHORT).show();
                //    Log.e("Explore Item ID ", "" + item.getId());

                Intent seelingintent = new Intent(mContext, ProductDetails.class);
                seelingintent.putExtra("productId", item.getProductID());
                seelingintent.putExtra("calledUserId","0");
                if(item.getIsSold().equalsIgnoreCase("1"))
                    seelingintent.putExtra("isSoldProduct",true);

                mContext.startActivity(seelingintent);
               // ((Activity)mContext).finish();

            }
        });

//        Target target = new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                holder.placeImage.setImageBitmap(bitmap);
//                //   Bitmap bitmap = ((BitmapDrawable) viewHolder.soldImage.getDrawable()).getBitmap();
//                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                    @Override
//                    public void onGenerated(Palette palette) {
//                        int bgColor = palette.getVibrantColor(mContext.getResources().getColor(R.color.ColorPrimary));
//                        holder.placeHolder.setBackgroundColor(bgColor);
//                        if (StaticData.isColorDark(bgColor)) {
//                            holder.placeName.setTextColor(mContext.getResources().getColor(R.color.white));
//
//                        } else {
//                            holder.placeName.setTextColor(mContext.getResources().getColor(R.color.black));
//
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
//        Picasso.with(mContext).load(item.getImageUrl()).networkPolicy(NetworkPolicy.OFFLINE)
//                .resize(100, 90).into(target);
    }

}
