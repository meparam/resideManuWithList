package vp.mom.activitys;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import vp.mom.R;

public class PaymentdoneMessageActivity extends AppCompatActivity {
Button paydone;
    TextView mesagePayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentdone_message);


        paydone=(Button)findViewById(R.id.paydone);

        mesagePayment=(TextView)findViewById(R.id.mesagePayment);

        mesagePayment.setText(getIntent().getStringExtra("pName"));
        paydone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
