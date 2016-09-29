package vp.mom.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vp.mom.R;

public class ExploreMainFragment extends Fragment {
    private FragmentTabHost mTabHost;
    //Mandatory Constructor
    public ExploreMainFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.exploremainfragment_view,container, false);
        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("Items").setIndicator("Items"),
                vp.mom.quickreturn.QuickReturnFacebookFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec("People").setIndicator("People"),
                ExplorePeopleFragmentNew.class, null);




        for (int tabIndex = 0 ; tabIndex < mTabHost.getTabWidget().getTabCount() ; tabIndex ++) {
            View tab = mTabHost.getTabWidget().getChildTabViewAt(tabIndex);
            TextView tv = (TextView)tab.findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
          //  tv.setAllCaps(false);
        }

        return rootView;
    }



//    public static void changeTab(int i) {
//
//        mTabHost.setCurrentTab(i);
//    }
}