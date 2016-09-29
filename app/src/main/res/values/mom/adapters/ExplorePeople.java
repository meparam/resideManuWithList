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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import vp.mom.R;
import vp.mom.data.ExplorePeoplePojo;
import vp.mom.searchtab.UserProfileDetailActivity;


public class ExplorePeople extends RecyclerView.Adapter<vp.mom.adapters.ExplorePeople.ViewHolder> {
   OnItemClickListener mItemClickListener;
    Context mContext;
    private List<ExplorePeoplePojo> suggestedItems;
  //  ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private Activity activity;
    // 2
    public ExplorePeople(Activity activity,List<ExplorePeoplePojo> msuggestedItems) {

        this.suggestedItems=msuggestedItems;
        this.mContext = activity;
    }

    // 3
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public LinearLayout placeHolder;
        public LinearLayout placeNameHolder;
        public TextView placeName;
        public ImageView placeImage;

        public ViewHolder(View itemView) {
            super(itemView);
            placeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            placeName = (TextView) itemView.findViewById(R.id.placeName);
            placeNameHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
            placeImage = (ImageView) itemView.findViewById(R.id.placeImage);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.aaaaa, parent, false);
        return new ViewHolder(view);
    }

    // 3
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final   ExplorePeoplePojo item = suggestedItems.get(position);
        holder.placeName.setText(item.getPeopleName());
       Picasso.with(mContext)
    .load(item.getPeopleProfilePic())
    .into(holder.placeImage);

        holder.placeHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(mContext, "Recycle Click" + position, Toast.LENGTH_SHORT).show();


                Intent msg=new Intent(mContext, UserProfileDetailActivity.class);

                msg.putExtra("userId",item.getPeopleId());
                msg.putExtra("userItself",false);
                mContext.startActivity(msg);


            }
        });
    }
}