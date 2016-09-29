package vp.mom.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vp.mom.R;
import vp.mom.api.CircleTransformation;
import vp.mom.app.AppController;
import vp.mom.data.SuggestedItem;


/**
 * Created by ApkDev2 on 07-11-2015.
 */
public class SuggestedUserAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ProgressDialog pDialog;
    private vp.mom.api.SessionManager session;
    private Activity activity;
    private List<SuggestedItem> suggestedItems;
    private static final String TAG = vp.mom.adapters.SuggestedUserAdapter.class.getSimpleName();
  //  ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private int avatarSize;
    public SuggestedUserAdapter(Activity activity,List<SuggestedItem> msuggestedItems) {
        this.activity = activity;
        this.suggestedItems=msuggestedItems;
        this.avatarSize = activity.getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);

        pDialog = new ProgressDialog(activity,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        session = new vp.mom.api.SessionManager(activity);
    }

    @Override
    public int getCount() {
        return suggestedItems.size();
    }

    @Override
    public Object getItem(int position) {
        return suggestedItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    public  class  ViewHolder
//    {
//
//        LinearLayout layout;
//    }

    @Override


    public View getView(final int position, View convertView, ViewGroup viewGroup) {

      //  ViewHolder holder=new ViewHolder();
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.custom_suggested_user, null);

//        if (imageLoader == null)
//            imageLoader = AppController.getInstance().getImageLoader();

        TextView name = (TextView) convertView.findViewById(R.id.suggested_user_name);
      //  TextView location = (TextView) convertView.findViewById(R.id.suggested_user_email);
        ImageView profile_img= (ImageView) convertView.findViewById(R.id.suggested_user_image);
      //  holder.layout   = (LinearLayout) convertView.findViewById(R.id.linear);
        TextView  suggestuserdesc= (TextView) convertView.findViewById(R.id.suggestuserdesc);

     final    Button follow= (Button) convertView.findViewById(R.id.btnfollow);

        final SuggestedItem item = suggestedItems.get(position);

        suggestuserdesc.setText(item.getItemDisc());

//        for (int i = 0; i<item.getImagearray().size(); i++) {
//            ImageView imageView = new ImageView(activity);
//          //  imageView.setId(i);
//            imageView.setPadding(2, 2, 2, 2);
//            Picasso.with(activity)
//				.load(item.getImagearray().get(i))
//                    //.resize(250, 300)
//				.into(imageView);
//            //  imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            holder.layout.addView(imageView);
//        }
        name.setText(item.getFname()+" "+item.getLname());
     //   location.setText(item.getLocation());
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addFollwing(item.getUserid(),follow);
            }
        });
//        imageLoader.get(item.getProfile_image(), ImageLoader.getImageListener(
//                profile_img, R.drawable.baby, R.drawable.ic_launcher));

        Picasso.with(activity)
                .load(item.getProfile_image())
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .into(profile_img);

        ImagePagerAdapter adapter =new ImagePagerAdapter(item.getImagearray());

        //	Log.e(" in adapter size",""+item.getImagearray().size());
        final ViewPager viewPager = (ViewPager) convertView.findViewById(R.id.view_pagerlist_suggest);
        //adapter = new ImagePagerAdapter(imagearra);
        viewPager.setAdapter(adapter);

        final vp.mom.circleindicator.CirclePageIndicator circleIndicator = (vp.mom.circleindicator.CirclePageIndicator) convertView.findViewById(R.id.indicatorlist_suggest);
        circleIndicator.setViewPager(viewPager);



        return convertView;
    }
    private class ImagePagerAdapter extends PagerAdapter {
        ArrayList<String> imagearry;

        public  ImagePagerAdapter(ArrayList<String> mimagearry)
        {
            this.imagearry=mimagearry;

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
            final Context context =activity;
            final ImageView imageView = new ImageView(context);
            final int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_top);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(context)
                    .load(imagearry.get(position))
//                    .placeholder(R.drawable.ic_facebook)
                    .centerCrop()
                    .resize( 346,
                            320)
                    .error(android.R.drawable.stat_notify_error)
                    .into(imageView);


            Picasso.with(activity)
                    .load(imagearry.get(position))
                    .into(imageView);

            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(final View view, final Object object) {
            return view == ((ImageView) object);
        }
    }
    private void addFollwing(final String userid, final Button follow) {



        String tag_string_req = "addFollwing";

        pDialog.setMessage("");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.USER_Follow, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
              //  Log.e(TAG, "addFollwing Response: " + response);
                hideDialog();
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    if (status) {

                        follow.setText("Following");

                        follow.setClickable(false);

                        Toast.makeText(activity,"Added to your following list",Toast.LENGTH_SHORT).show();
//                        SuggestedUserAdapter.this.remove(item);
//
//                        adapter.notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(activity,"Already you are following",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
             //   Log.e(TAG, "addFollwing Error: " + error.getMessage());
                Toast.makeText(activity,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("to_user", userid);
                params.put("from_user", session.getStringSessionData("uid"));
                  return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
