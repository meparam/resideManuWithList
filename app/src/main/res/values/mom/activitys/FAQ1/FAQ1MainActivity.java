package vp.mom.activitys.FAQ1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

import vp.mom.R;

/**
 * Created by pallavi  approved by param on 22-Jan-16.
 */
public class FAQ1MainActivity  extends AppCompatActivity {

    ExpandableListView listView;
    vp.mom.activitys.FAQ1.QuestionDataAdapter dataAdapter;
    ArrayList<Question> list;
    TextView tooltitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tooltitle=(TextView)findViewById(R.id.tooltitle);
        tooltitle.setText("FAQ");
        listView = (ExpandableListView) findViewById(R.id.listData);
        //start a Thread
        new QuestionThread(vp.mom.activitys.FAQ1.FAQ1MainActivity.this,new QuestionHandler()).execute();
    }
    class QuestionHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            list = new ArrayList<>();
            list = (ArrayList<Question>) msg.obj;

            dataAdapter = new vp.mom.activitys.FAQ1.QuestionDataAdapter(vp.mom.activitys.FAQ1.FAQ1MainActivity.this,list);
            listView.setAdapter(dataAdapter);
        }
    }



}
