package vp.mom.activitys;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vp.mom.R;

public class ReportTransactionProblem extends AppCompatActivity {
TextView title;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_transaction_problem);
        initActionBar();
        viewPager = (ViewPager) findViewById(R.id.reportviewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.reportTabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabTextColors(getResources().getColorStateList(R.color.white));

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

//        Bundle bundle = new Bundle();
//        bundle.putString("userId", userId);
        vp.mom.fragments.ReportPurchasedItem userseling= new vp.mom.fragments.ReportPurchasedItem();
      //  userseling.setArguments(bundle);
        adapter.addFragment(userseling, "PURCHASED ITEMS");

//        Bundle bundle1 = new Bundle();
//        bundle1.putString("userId",userId);
//        ReportPurchasedItem userseling1= new ReportPurchasedItem();
//       // userseling.setArguments(bundle1);
//        adapter.addFragment(userseling1,"SOLD ITEMS");
//
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title= (TextView) toolbar.findViewById(R.id.tooltitle);
        title.setText("Transactions");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
