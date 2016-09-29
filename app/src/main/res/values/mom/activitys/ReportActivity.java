package vp.mom.activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by pallavi.b on 23-Jan-16.
 */
public class ReportActivity extends AppCompatActivity {
    private EditText input_textReport;
    String textReport;
    private TextInputLayout input_layout_name;
    private Button btnSubmit;
    TextView tool_title;
    vp.mom.api.SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);
        session=new vp.mom.api.SessionManager(this);

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
        tool_title=(TextView)findViewById(R.id.tooltitle);
        tool_title.setText("Report a Problem");
        init();

        input_textReport.addTextChangedListener(new MyTextWatcher(input_textReport));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }

            private void submitForm() {

                if (validateName()) {
                    Report_problem();

                }

            }
        });
    }

    private boolean validateName() {

        textReport=input_textReport.getText().toString().trim();

        if (textReport.isEmpty()) {
            input_layout_name.setError(getString(R.string.in_appropriate_text));
            requestFocus(input_textReport);
            return false;
        } else {
            input_layout_name.setErrorEnabled(false);
            return true;
        }


    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private void init() {
        input_textReport = (EditText) findViewById(R.id.input_textReport);
        input_layout_name = (TextInputLayout) findViewById(R.id.input_layout_name);
        btnSubmit= (Button) findViewById(R.id.btnDone);
    }

    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();

            }
        }
    }


    private void Report_problem() {
        String tag_string_req = "Report_problem";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.REPORT_PROBLEM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Report_problem", "Report_problem Response: " + response.toString());
                //	hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {
                        showAlertMessage("Thank you your problem is recorded",true);

                        // user successfully logged in
                      //  Log.e("Report_problem", "Report_problem Response: " + response.toString());

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        showAlertMessage(errorMsg, false);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(vp.mom.activitys.ReportActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
             //   Log.e("Report_problem", "Report_problem Error: " + error.getMessage());
                Toast.makeText(vp.mom.activitys.ReportActivity.this,
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

                params.put("user_id",session.getStringSessionData("uid"));
                          params.put(" reason",textReport);
              return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private  void showAlertMessage(String alertMsg, final boolean flag)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Alert !");
        builder.setCancelable(false);
        builder.setMessage(alertMsg);
        //  builder.setPositiveButton("OK", null);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (flag) {

                    finish();
                } else
                    dialog.dismiss();


                //  dialog.dismiss();
            }
        });

        //  builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}