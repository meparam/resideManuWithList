package vp.mom.activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import vp.mom.api.AppConfig;
import vp.mom.api.SessionManager;
import vp.mom.app.AppController;

public class PurchaseReport extends AppCompatActivity {
Intent  intent;
    String productId,title,description;
    Button selectIssues,doneButton;
 SessionManager session;
    EditText input_textReport;
    String[] problem_array={ "Item not as described","Item didn't arrive"};
    TextView tool_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_report);
        session=new SessionManager(vp.mom.activitys.PurchaseReport.this);

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
        tool_title.setText("Transaction");


        intent=getIntent();
        if(intent!=null)
        {
            productId=intent.getStringExtra("product_id");
       }
        else
            finish();

        selectIssues= (Button) findViewById(R.id.selectIssues);
        doneButton= (Button) findViewById(R.id.btnDoneproblm);
        input_textReport= (EditText) findViewById(R.id.input_textReport);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getValidation())
                {
                    Report_Transation_problem();
                }
            }
        });

        selectIssues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog_area = new AlertDialog.Builder(
                        vp.mom.activitys.PurchaseReport.this);

                // Set the dialog title

                dialog_area.setTitle("Select issue! ")

                        .setSingleChoiceItems(problem_array, 0,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        selectIssues.setText(problem_array[item]);

                                        title=problem_array[item].toString().trim();
                                        dialog.dismiss();

                                    }
                                });

                dialog_area.show();
            }
        });


    }

    private boolean getValidation() {

        if(!(title != null && title.length() > 0))
        {
            Toast.makeText(getApplicationContext(), "Please select reason", Toast.LENGTH_SHORT).show();
            return  false;
        }
        description= input_textReport.getText().toString().trim();
        if(!(description != null && description.length() > 0))
        {

            Toast.makeText(getApplicationContext(), "Please provide some description", Toast.LENGTH_SHORT).show();
            return  false;
        }
        return true;
    }

    private void Report_Transation_problem() {
        String tag_string_req = "Report_problem";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.REPORT_TRANSATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Report_problem", "Report_problem Response: " + response.toString());
                //	hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {
                        showAlertMessage("Thank you, your problem is recorded",true);

                        // user successfully logged in
                    //    Log.e("Report_problem", "Report_problem Response: " + response.toString());

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        showAlertMessage(errorMsg, false);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(vp.mom.activitys.PurchaseReport.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
              //  Log.e("Report_problem", "Report_problem Error: " + error.getMessage());
                Toast.makeText(vp.mom.activitys.PurchaseReport.this,
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
                params.put("reason",title);
                params.put("message",description);
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
