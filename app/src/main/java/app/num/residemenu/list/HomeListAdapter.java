package app.num.residemenu.list;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.num.residemenu.R;
import app.num.residemenu.circleindicator.CirclePageIndicator;
import app.num.residemenu.circleindicator.CircleTransformation;

public class HomeListAdapter extends BaseAdapter {
ImageView img;
private Context activity;
private LayoutInflater inflater;
private List<FeedItem> feedItems;
private int avatarSize;
String TYPE;
//	private ProgressDialog pDialog;
private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
ImageView imageView;
private final ArrayList<Integer> likedPositions = new ArrayList<>();
private HashTagHelper mTextHashTagHelper;
boolean add_bookmar_flag=false;
private final Map<View, AnimatorSet> likeAnimations = new HashMap<>();
boolean likeFlag=false;
static  int likeClick=0;
//ImageLoader imageLoader = AppController.getInstance().getImageLoader();
public HomeListAdapter(Context activity, List<FeedItem> feedItems) {
    this.activity = activity;
    this.feedItems = feedItems;
    this.avatarSize = activity.getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);


}

@Override
public int getCount() {
    return feedItems.size();
}

@Override
public Object getItem(int location) {
    return feedItems.get(location);
}

@Override
public long getItemId(int position) {
    return position;
}

static private int lastPosition = -1;
ImageView homelistoption, listcomment, listlike;
PopupMenu popup;
Button listBuy;

public class MyViewHolder {
    TextSwitcher tsLikesCounter;
    ImageView like;
    TextView likeCount;
}

int likeCounter = 0;

@Override
public View getView(final int position, View convertView, ViewGroup parent) {
    //ImageView like;
    //	final CellFeedViewHolder cellFeedViewHolder = new CellFeedViewHolder(view);
    final MyViewHolder holder = new MyViewHolder();
    if (inflater == null)
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
        convertView = inflater.inflate(R.layout.home_list_row_item, null);

//    TextView textView5 = (TextView) convertView.findViewById(R.id.textView5);
//  textView5.setText(""+position);
    TextView name = (TextView) convertView.findViewById(R.id.name);
    TextView	prodSize= (TextView) convertView.findViewById(R.id.prodSize);
    TextView	timestamp= (TextView) convertView.findViewById(R.id.hoelisttimestamp);

    holder.likeCount  = (TextView) convertView.findViewById(R.id.likeCount);

    holder.tsLikesCounter= (TextSwitcher) convertView.findViewById(R.id.tsLikesCounter);


    TextView productName = (TextView) convertView
            .findViewById(R.id.productName);
    TextView txtdescMsg = (TextView) convertView
            .findViewById(R.id.txtdescMsg);
    TextView homelistbaseprice = (TextView) convertView.findViewById(R.id.homelistbaseprice);
    ImageView	homelistshare= (ImageView) convertView.findViewById(R.id.homelistshare);
    homelistoption	= (ImageView) convertView.findViewById(R.id.homelistoption);

    listcomment	= (ImageView) convertView.findViewById(R.id.listcomment);
    holder.like= (ImageView) convertView.findViewById(R.id.listlike);
    listBuy= (Button) convertView.findViewById(R.id.listBuy);

    final FeedItem item = feedItems.get(position);

    timestamp.setText(item.getTimeStamp());
    holder.likeCount.setText(item.getLikeCount() + " People Liked this");

    holder.tsLikesCounter.setCurrentText(activity.getResources().getQuantityString(
R.plurals.likes_count, item.getLikeCount(), item.getLikeCount()
));

    prodSize.setText(item.getProdSize());
    homelistoption.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(activity, v);
            //	popupMenu.setOnMenuItemClickListener(HomeListAdapter.this);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.show();

            if(feedItems.get(position).isBookmark_status())
                popupMenu.getMenu().getItem(0).setTitle("Remove bookmark");

            else
                popupMenu.getMenu().getItem(0).setTitle("Add bookmark");



            //listlike.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.shake_error));
        }
    });




    holder.like.setTag(position);

    if(!feedItems.get(position).isLike()){
        holder.like.setImageResource(R.drawable.ic_heart_outline_grey);

    }else {
        holder.like.setImageResource(R.drawable.ic_heart_red);
    }
    holder.like.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TYPE = "3";
            //	feedItems.get((Integer)holder.like.getTag()).setIsLike(true);
            if (!feedItems.get(position).isLike()){
                lastPosition = 19999999;
                updateHeartButton(holder, true, (Integer) holder.like.getTag());
                feedItems.get((Integer)holder.like.getTag()).setIsLike(true);
                likedPositions.add((Integer) holder.like.getTag());


                feedItems.get((Integer)holder.like.getTag()).setLikeCount(item.getLikeCount() + 1);

                notifyDataSetChanged();
                updateLikesCounter(holder, item.getLikeCount());


            //	Log.e("like", "set to like");
                likeFlag=true;



            }
            else{
                lastPosition = 19999999;
            //	Log.e("like", "set to unlike");
                feedItems.get(position).setIsLike(false);
                holder.like.setImageResource(R.drawable.ic_heart_outline_grey);

                if(likedPositions.contains((Integer) holder.like.getTag()));
                likedPositions.remove((Integer) holder.like.getTag());
                feedItems.get((Integer)holder.like.getTag()).setIsLike(false);

                feedItems.get((Integer)holder.like.getTag()).setLikeCount(item.getLikeCount() - 1);

                notifyDataSetChanged();
                updateLikesCounter(holder, item.getLikeCount());
                likeFlag=true;


            }


        }
    });


    final ImageView profilePic = (ImageView) convertView
            .findViewById(R.id.homelist_profilepic);
    profilePic.setClickable(true);



//		ImageView feedImageView = (ImageView) convertView
//				.findViewById(R.id.homefeedImage);
    name.setText(item.getName());
    productName.setText(item.getProductBrand()+": "+item.getProductName());
    homelistbaseprice.setText("£" + item.getbase_price());

    Picasso.with(activity)
            .load(item.getProfilePic())
            .placeholder(R.drawable.img_circle_placeholder)
            .resize(avatarSize, avatarSize)
            .centerCrop()
            .transform(new CircleTransformation())
            .into(profilePic);
    txtdescMsg.setText(item.getProdDesc());



  //  mTextHashTagHelper.handle(txtdescMsg);

    ImagePagerAdapter adapter =new ImagePagerAdapter(item.getImagearray());
//
//	//	Log.e(" in adapter size",""+item.getImagearray().size());
    final ViewPager viewPager = (ViewPager) convertView.findViewById(R.id.view_pagerlist);
//		//adapter = new ImagePagerAdapter(imagearra);
    viewPager.setAdapter(adapter);
//
    final CirclePageIndicator circleIndicator = (CirclePageIndicator) convertView.findViewById(R.id.indicatorlist);
    circleIndicator.setViewPager(viewPager);



likeFlag=false;
    return convertView;
}




private void updateLikesCounter(final MyViewHolder holder, int toValue) {
//		String likesCountTextFrom = holder.tsLikesCounter.getResources().getQuantityString(
//				R.plurals.likes_count, toValue - 1, toValue - 1
//		);
//		holder.tsLikesCounter.setCurrentText(likesCountTextFrom);

    String likesCountTextTo = holder.tsLikesCounter.getResources().getQuantityString(
            R.plurals.likes_count, toValue, toValue
    );
    holder.tsLikesCounter.setText(likesCountTextTo);
}



private class ImagePagerAdapter extends PagerAdapter {
    ArrayList<String> imagearry;

    public ImagePagerAdapter(ArrayList<String> mimagearry) {
        this.imagearry = mimagearry;

        //	Log.e("size",""+imagearry.size());
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return this.imagearry.size();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Context context = activity;
        imageView = new ImageView(context);
        final int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_top);
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //	imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(context)
                .load(imagearry.get(position))
.fit().centerCrop()
                .error(android.R.drawable.stat_notify_error)
                .into(imageView);


        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }


    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view == ((ImageView) object);
    }
}

private void updateHeartButton(final MyViewHolder holder, boolean animated, int position) {
    if (animated) {
        if (!likeAnimations.containsKey(holder)) {
            AnimatorSet animatorSet = new AnimatorSet();
            likeAnimations.put(holder.like, animatorSet);

            ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder.like, "rotation", 0f, 360f);
            rotationAnim.setDuration(300);
            rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

            ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.like, "scaleX", 0.2f, 1f);
            bounceAnimX.setDuration(300);
            bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

            ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.like, "scaleY", 0.2f, 1f);
            bounceAnimY.setDuration(300);
            bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
            bounceAnimY.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    holder.like.setImageResource(R.drawable.ic_heart_red);
                }
            });

            animatorSet.play(rotationAnim);
            animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    resetLikeAnimationState(holder);
                }
            });

            animatorSet.start();
        }
    } else {
        if (likedPositions.contains(position)) {
            holder.like.setImageResource(R.drawable.ic_heart_red);
        } else {
            holder.like.setImageResource(R.drawable.ic_heart_outline_grey);
        }
    }
}

private void resetLikeAnimationState(MyViewHolder holder) {
    likeAnimations.remove(holder);
//		holder.like.setVisibility(View.GONE);
//		holder.like.setVisibility(View.GONE);
}


}
