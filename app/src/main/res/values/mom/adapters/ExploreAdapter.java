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

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

import vp.mom.R;
import vp.mom.activitys.ProductDetails;
import vp.mom.app.AppController;
import vp.mom.data.ExplorePeopleItem;


public class ExploreAdapter extends RecyclerView.Adapter<vp.mom.adapters.ExploreAdapter.ViewHolder> {
OnItemClickListener mItemClickListener;
Context mContext;
private List<ExplorePeopleItem> suggestedItems;
ImageLoader imageLoader = AppController.getInstance().getImageLoader();
private Activity activity;
// 2
protected boolean animated = false;
private static final int SCALE_DELAY = 30;
public ExploreAdapter(Activity activity,List<ExplorePeopleItem> msuggestedItems) {

    this.suggestedItems=msuggestedItems;
    this.mContext = activity;

  //  Log.e("Explore Item ", "" + suggestedItems.size());
}

// 3
public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public LinearLayout placeHolder;
  //  public LinearLayout placeNameHolder;
  //  public TextView placeName;
    public ImageView placeImage;

    public ViewHolder(View itemView) {
        super(itemView);
        placeHolder = (LinearLayout) itemView.findViewById(R.id.itemmainHolder);
   //     placeName = (TextView) itemView.findViewById(R.id.placeName);
    //    placeNameHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
        placeImage = (ImageView) itemView.findViewById(R.id.itemImage);
        placeHolder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

}
public interface OnItemClickListener {
    void onItemClick(View view, int position);
}
public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
    this.mItemClickListener = mItemClickListener;
}
@Override
public int getItemCount() {
    return suggestedItems.size();
}

// 2
@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_item_row_view, parent, false);
    return new ViewHolder(view);
}

// 3
@Override
public void onBindViewHolder(final ViewHolder holder, final int position) {
    final   ExplorePeopleItem item = suggestedItems.get(position);

    Picasso.with(mContext)
            .load(item.getProduct_Image().get(0))
            .into(holder.placeImage);
 //   Log.e("Explore Item ", "" + item.getProduct_Image().get(0));

    holder.placeHolder.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent seelingintent = new Intent(mContext, ProductDetails.class);
            seelingintent.putExtra("productId", item.getId());
            seelingintent.putExtra("calledUserId", item.getUserID());
            mContext.startActivity(seelingintent);


        }
    });

  //  amimateCell(holder);
}
private void amimateCell(ViewHolder booksViewHolder) {

    int cellPosition = booksViewHolder.getPosition();

    if (!animated) {

        animated= true;
        booksViewHolder.placeHolder.setScaleY(0);
        booksViewHolder.placeHolder.setScaleX(0);
        booksViewHolder.placeHolder.animate()
                .scaleY(1).scaleX(1)
                .setDuration(200)
                .setStartDelay(SCALE_DELAY * cellPosition)
                .start();
    }

}
}