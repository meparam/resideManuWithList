package vp.mom.adapters;

/**
 * Created by yaron on 24/02/15.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import vp.mom.R;
import vp.mom.api.CircleTransformation;

public class MyAdapter extends RecyclerView.Adapter<vp.mom.adapters.MyAdapter.ViewHolder> {
    private int avatarSize;
    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[];
    private int mIcons[];

    private String name;
    private int profile;
    private String email;
    private String website;
    Context context;
    private vp.mom.api.SessionManager session;

    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int Holderid;

        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView Name;
        TextView email;
        TextView website;
        Context contxt;


        public ViewHolder(View itemView, int ViewType, Context c) {  // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);

            contxt = c;
            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created
            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
            }
            else{

                Name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
//                email = (TextView) itemView.findViewById(R.id.email);
                website = (TextView) itemView.findViewById(R.id.website);// Creating Text View object from header.xml for email
                profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }

        @Override
        public void onClick(View v) {

          //  Toast.makeText(contxt,"The Item Clicked is: "+getPosition(),Toast.LENGTH_SHORT).show();

        }
    }



    public MyAdapter(String Titles[], int Icons[], String Name, String Email, int Profile, String Website, Context passedContext){ // MyAdapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we
        mNavTitles = Titles;                //have seen earlier
        mIcons = Icons;
        name = Name;
        email = Email;
        website = Website;
        profile = Profile;
        this.context = passedContext;

        session = new vp.mom.api.SessionManager(context);
        this.avatarSize = context.getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);
    }

    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public vp.mom.adapters.MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v, viewType, context); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v, viewType, context); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created


        }
        return null;

    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(vp.mom.adapters.MyAdapter.ViewHolder holder, int position) {
        if(holder.Holderid == 1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
            holder.imageView.setImageResource(mIcons[position -1]);// Settimg the image with array of our icons
        }
        else{


            try {
                JSONObject jObj = new JSONObject(session.getStringSessionData(vp.mom.api.AppConfig.USER_DATA));
                String fullname=jObj.getJSONObject("items").getString("first_name")+" "+jObj.getJSONObject("items").getString("last_name");
                holder.Name.setText(fullname);
                holder.website.setText("@" + jObj.getJSONObject("items").getString("username"));

                Picasso.with(context)
                        .load(jObj.getJSONObject("items").getString("photo"))
                        .placeholder(R.drawable.img_circle_placeholder).transform(new CircleTransformation())
                        .into(holder.profile);

//                Picasso.with(context)
//                        .load()
//                        .placeholder(R.drawable.img_circle_placeholder)
//                        .resize(avatarSize, avatarSize)
//                        .centerCrop()
//                        .transform(new CircleTransformation())
//                        .into();

             //   Log.e("Picasso","Picasso"+session.getStringSessionData(AppConfig.USER_DATA));

              //  http://dev8.edreamz3.com/images/users/1447076232ds.jpg

            }
         catch (JSONException e) {
            // JSON error
            e.printStackTrace();

        }


        }
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return mNavTitles.length + 1; // the number of items in the list will be +1 the titles including the header view.
    }


    // With the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}