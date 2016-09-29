package vp.mom.activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import vp.mom.R;

/**
 * Created by pallavi.b approved by param on 23-Jan-16.
 */
    public class PrivacyPolicy extends AppCompatActivity {
    final Context context = this;
    private TextView tooltitle,txtDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_conditions);
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
        tooltitle.setText("Privacy Policy");
        txtDetails = (TextView) findViewById(R.id.termstxtTitle21);

        String linkText = "<a href=\"" + "http://www.marketofmums.com/privacy_policy.php" + "\">"
                + "http://www.marketofmums.com/privacy_policy.php" + "</a> ";
        txtDetails.setText(Html.fromHtml(linkText));
        txtDetails.setMovementMethod(LinkMovementMethod.getInstance());
    }

//    private void TermsAndConditions() {
//        String tag_string_req = "privacypolicy";
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                AppConfig.PRIVACY_POLICY, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                //   Log.e("reportProduct", "reportProduct Response: " + response.toString());
//                //	hideDialog();
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    boolean status = jObj.getBoolean("status");
//                    // Check for error node in json
//                    if (status) {
//                        //Toast.makeText(PrivacyPolicy.this, "Your concern about this product is recorded", Toast.LENGTH_SHORT).show();
//                        Log.v("res", "" + jObj);
//
//                        JSONArray jsonArray=jObj.getJSONArray("items");
//
//                        txtTitle.setText(jsonArray.getJSONObject(0).getString("page_title"));
//                        txtDetails.setText(jsonArray.getJSONObject(0).getString("content"));
//
//                        // user successfully logged in
//                        //   Log.e("reportProduct", "reportProduct Response: " + response.toString());
//                        // finish();
//
//                    } else {
//                        // Error in login. Get the error message
//                        String errorMsg = jObj.getString("message");
//                        Toast.makeText(PrivacyPolicy.this,
//                                errorMsg, Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    // JSON error
//                    e.printStackTrace();
//                    Toast.makeText(PrivacyPolicy.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //   Log.e("ReportProduct", "ReportProduct Error: " + error.getMessage());
//                Toast.makeText(PrivacyPolicy.this,
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                //	hideDialog();
//            }
//        }) {
//
//
//        };
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//    }


//    private void init() {
//        mLinearLayoutVisibility = (LinearLayout) findViewById(R.id.linearlayout);
//        txtTitle = (TextView) findViewById(R.id.txtTitle);
//        txtDetails = (TextView) findViewById(R.id.txtDescription);
//    }



}
