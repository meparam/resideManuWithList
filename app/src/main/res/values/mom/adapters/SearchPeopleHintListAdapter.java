package vp.mom.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vp.mom.R;

/**
 * Created by shivkanya.i on 21-01-2016.
 */
public class SearchPeopleHintListAdapter extends BaseAdapter {
  ArrayList<String> arrayhint;
    Context context;
    private LayoutInflater inflater;
    public SearchPeopleHintListAdapter(Context mcontaxt, ArrayList<String> marrayhint)
    {
    this.arrayhint=marrayhint;
        this.context=mcontaxt;
    }

    @Override
    public int getCount() {
        return arrayhint.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayhint.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.search_hint_list, null);

        TextView texthint= (TextView) convertView.findViewById(R.id.serachhint);
        texthint.setText(arrayhint.get(position));

        LinearLayout serchmainll= (LinearLayout) convertView.findViewById(R.id.serchmainll);
        serchmainll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent msg = new Intent(context, vp.mom.searchtab.ExplorePeopleResult.class);
                msg.putExtra("serachItemName",arrayhint.get(position));
                msg.putExtra("itemType","2");
                context.startActivity(msg);
                ((Activity)context).finish();
                //((Activity)context).finish().overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);


            }
        });

        return convertView;
    }
}
