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
import vp.mom.adapters.SearchPeopleHintListAdapter;


public class PeopleSearchFragment extends Fragment {
    View rootview;
    SearchDataBase db;
    ArrayList<String> feeeitem;
    ListView list;
    SearchPeopleHintListAdapter adapter;
    public PeopleSearchFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootview= inflater.inflate(R.layout.item_list_search, container, false);
        db = new SearchDataBase(getActivity());
        vp.mom.utils.StaticData.serachTab="people";
        list= (ListView) rootview.findViewById(R.id.itemsearch_list);

        feeeitem=new ArrayList<>();

        feeeitem = db.getAllSearchPeople();

        adapter=new SearchPeopleHintListAdapter(getActivity(),feeeitem);


        list.setAdapter(adapter);


        return  rootview;
    }
    @Override
    public void onResume() {
        super.onResume();
        feeeitem = db.getAllSearchPeople();


        adapter=new SearchPeopleHintListAdapter(getActivity(),feeeitem);
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
    }
}