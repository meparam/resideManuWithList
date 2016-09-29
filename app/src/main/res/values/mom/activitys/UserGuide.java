package vp.mom.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import vp.mom.R;

public class UserGuide extends AppCompatActivity {

    TextView title;
    WebView textGuide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);
        initActionBar();
        textGuide= (WebView) findViewById(R.id.textGuide);
        textGuide.getSettings().setJavaScriptEnabled(true);
        textGuide.loadUrl("file:///android_asset/userguide.html");
    }
    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title= (TextView) toolbar.findViewById(R.id.tooltitle);
        title.setText("User Guide");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
