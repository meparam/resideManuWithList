//package vp.mom.activitys;
//
//import android.app.ListActivity;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.CheckBox;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import vp.mom.R;
//
//public class ColorActivity extends ListActivity {
//
//
//    //ArrayList that will hold the original Data
//    ArrayList<HashMap<String, Object>> players;
//    LayoutInflater inflater;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//
//        //get the LayoutInflater for inflating the customomView
//        //this will be used in the custom adapter
//        inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        //these arrays are just the data that
//        //I'll be using to populate the ArrayList
//        //You can use our own methods to get the data
//        String names[]={"Beige","Black","Blue","Brown",
//                "Gold","Green","Orange","Pink","Purple","Red","Silver"};
//
//      /*  String teams[]={"Real Madrid","Barcelona","Chelsea",
//                "Barcelona","Chelsea","Liverpool",
//                "ManU","Barcelona"};*/
//        Integer[] photos={R.drawable.ic_beige,R.drawable.ic_black,
//                R.drawable.ic_blue,R.drawable.ic_brown,
//                R.drawable.ic_gold,R.drawable.ic_green,
//                R.drawable.ic_orange,R.drawable.ic_pink,R.drawable.ic_purple,R.drawable.ic_red,R.drawable.ic_silver};
//
//        players=new ArrayList<HashMap<String,Object>>();
//
//        //temporary HashMap for populating the
//        //Items in the ListView
//        HashMap<String , Object> temp;
//
//        //total number of rows in the ListView
//        int noOfPlayers=names.length;
//
//        //now populate the ArrayList players
//        for(int i=0;i<noOfPlayers;i++)
//        {
//            temp=new HashMap<String, Object>();
//
//            temp.put("name", names[i]);
//           // temp.put("team", teams[i]);
//            temp.put("photo", photos[i]);
//
//            //add the row to the ArrayList
//            players.add(temp);
//        }
//
// /*create the adapter
// *first param-the context
// *second param-the id of the layout file
//  you will be using to fill a row
// *third param-the set of values that
//   will populate the ListView */
//        final CustomAdapter adapter=new CustomAdapter(this, R.layout.custom_subview,players);
//
//        //finally,set the adapter to the default ListView
//        setListAdapter(adapter);
//
//
//    }
//
//
//    //define your custom adapter
//    private class CustomAdapter extends ArrayAdapter<HashMap<String, Object>>
//    {
//        // boolean array for storing
//        //the state of each CheckBox
//        boolean[] checkBoxState;
//
//
//        ViewHolder viewHolder;
//
//        public CustomAdapter(Context context, int textViewResourceId,
//                             ArrayList<HashMap<String, Object>> players) {
//
//            //let android do the initializing :)
//            super(context, textViewResourceId, players);
//
//            //create the boolean array with
//            //initial state as false
//            checkBoxState=new boolean[players.size()];
//        }
//
//
//        //class for caching the views in a row
//        private class ViewHolder
//        {
//            ImageView photo;
//            TextView name,team;
//            CheckBox checkBox;
//        }
//
//
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//
//            if(convertView==null)
//            {
//                convertView=inflater.inflate(R.layout.custom_subview, null);
//                viewHolder=new ViewHolder();
//
//                //cache the views
//                viewHolder.photo=(ImageView) convertView.findViewById(R.id.photo);
//                viewHolder.name=(TextView) convertView.findViewById(R.id.name);
//              //  viewHolder.team=(TextView) convertView.findViewById(R.id.team);
//                viewHolder.checkBox=(CheckBox) convertView.findViewById(R.id.checkBox);
//
//                //link the cached views to the convertview
//                convertView.setTag( viewHolder);
//
//
//            }
//            else
//                viewHolder=(ViewHolder) convertView.getTag();
//
//
//            int photoId=(Integer) players.get(position).get("photo");
//
//            //set the data to be displayed
//            viewHolder.photo.setImageDrawable(getResources().getDrawable(photoId));
//            viewHolder.name.setText(players.get(position).get("name").toString());
//            //viewHolder.team.setText(players.get(position).get("team").toString());
//
//            //VITAL PART!!! Set the state of the
//            //CheckBox using the boolean array
//            viewHolder.checkBox.setChecked(checkBoxState[position]);
//
//
//            //for managing the state of the boolean
//            //array according to the state of the
//            //CheckBox
//
//            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
//
//                public void onClick(View v) {
//                    if(((CheckBox)v).isChecked())
//                        checkBoxState[position]=true;
//                    else
//                        checkBoxState[position]=false;
//
//                }
//            });
//
//            //return the view to be displayed
//            return convertView;
//        }
//
//    }
//}