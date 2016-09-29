package vp.mom.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import vp.mom.R;

public class MessageActivity extends AppCompatActivity {
   // ImageView back;
    TextView tool_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



     //   back=(ImageView)toolbar.findViewById(R.id.ic_back);
        tool_title=(TextView)toolbar.findViewById(R.id.tootlbar_title);

        tool_title.setText("Messages");

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
