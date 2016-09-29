package vp.mom.searchtab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import vp.mom.R;
import vp.mom.SearchDataBase;


public class ItemSearchFragment extends Fragment {
    ArrayList<String> feeeitem;
    View rootview;
    SearchDataBase db;
    ListView list;
    vp.mom.adapters.SearchItemHintList adapter;
    public ItemSearchFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview= inflater.inflate(R.layout.item_list_search, container, false);
        db = new SearchDataBase(getActivity());

        vp.mom.utils.StaticData.serachTab="item";
        list= (ListView) rootview.findViewById(R.id.itemsearch_list);

        feeeitem=new ArrayList<>();

        feeeitem = db.getAllSearchItem();

        adapter=new vp.mom.adapters.SearchItemHintList(getActivity(),feeeitem);


      list.setAdapter(adapter);

        return  rootview;
    }
    @Override
    public void onResume() {
        super.onResume();
        feeeitem = db.getAllSearchItem();


        adapter=new vp.mom.adapters.SearchItemHintList(getActivity(),feeeitem);
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
    }
}