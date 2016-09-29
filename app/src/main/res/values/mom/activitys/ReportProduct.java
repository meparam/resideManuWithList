package vp.mom.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vp.mom.R;
import vp.mom.app.AppController;

public class ReportProduct extends AppCompatActivity {
    vp.mom.api.SessionManager session;
    String productId="00";
    EditText reportProductInput;
    String productCpmment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_product_view);
        session=new vp.mom.api.SessionManager(this);
        try
        {
            productId=getIntent().getStringExtra("ProducID");
        }
        catch (Exception e)
        {}
        reportProductInput= (EditText) findViewById(R.id.reportProductInput);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView seeling_acccept = (ImageView) toolbar.findViewById(R.id.seeling_acccept);
        TextView saletext= (TextView) findViewById(R.id.saletext);
        saletext.setText("Report item");
        seeling_acccept.setClickable(true);
        ImageView seeling_cancel = (ImageView) toolbar.findViewById(R.id.seeling_cancel);

        seeling_cancel.setClickable(true);

        seeling_acccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                productCpmment = reportProductInput.getText().toString().trim();
                if(!productCpmment.isEmpty())
                {
                  //  Toast.makeText(ReportProduct.this,"reportProduct",Toast.LENGTH_SHORT).show();
                    reportProduct();
                }
                else
                {
                    Toast.makeText(vp.mom.activitys.ReportProduct.this,"Please add your comment",Toast.LENGTH_SHORT).show();
                }
                //  Toast.makeText(SellingActivity.this, "seeling_acccept", Toast.LENGTH_SHORT).show();

//                if (getValidation() && lat != null && lng != null) {
//
//
//
//                }
            }
        });

        seeling_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void reportProduct() {
        String tag_string_req = "reportProduct";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.REPORT_PRODUCT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
             //   Log.e("reportProduct", "reportProduct Response: " + response.toString());
                //	hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {
                        Toast.makeText(vp.mom.activitys.ReportProduct.this,"Your concern about this product is recorded",Toast.LENGTH_SHORT).show();
                        // user successfully logged in
                     //   Log.e("reportProduct", "reportProduct Response: " + response.toString());
                        finish();

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(vp.mom.activitys.ReportProduct.this,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(vp.mom.activitys.ReportProduct.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
             //   Log.e("ReportProduct", "ReportProduct Error: " + error.getMessage());
                Toast.makeText(vp.mom.activitys.ReportProduct.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //	hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
//                params.put("email", email);
//                params.put("password", password);

                params.put("from_user",session.getStringSessionData("uid"));
                params.put("product_id",productId);
                params.put("reason",productCpmment);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
