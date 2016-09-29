package vp.mom.activitys;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vp.mom.R;
import vp.mom.app.AppController;


/**
 * Created by pallavi.b on 30-Jan-16.
 */
public class PurchaseItemInvoiceActivity extends AppCompatActivity {

    private TextView txtorderid,txtnameofproduct,txtdeliverystatus, txtProductColor,txtshippingfname, txtproductcompany,txtproductbrand,txtorderdate, txtShippingAddress,txtdeliverydate,txtproductdescription,txtFinalAmount,txtTotalAmount,txtShippingcharges, txttotalamount, txtshippingcharges, txtfinalamount,txtbilling_address;
    vp.mom.api.SessionManager session;
    String orderid,productid;
Button rateProduct;
    ArrayList<String> imglist;
    ViewPager viewPager;
    vp.mom.circleindicator.CirclePageIndicator circleIndicator;
    TextView tooltitle;
    String feedbackMsg=" abc";
    String prodrating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_item_invoice);

        session = new vp.mom.api.SessionManager(this);
        setCustomActionBar();
        init();

        Intent intent=getIntent();
        if (intent != null){
            productid=intent.getStringExtra("product_id");
            orderid=intent.getStringExtra("ord_det_id");

          //  Log.e("productid","productid"+productid+" "+orderid);

            getPurchaseItemInvoice();

        }else{
            Toast.makeText(vp.mom.activitys.PurchaseItemInvoiceActivity.this, "Something wents wrong..", Toast.LENGTH_SHORT).show();
        }

        rateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(vp.mom.activitys.PurchaseItemInvoiceActivity.this);
                dialog.setContentView(R.layout.product_rating);
              //  dialog.setTitle("Rating...");

             //   dialog.getWindow().setBackgroundDrawable(null);
                // set the custom dialog components - text, image and button
                final EditText text = (EditText) dialog.findViewById(R.id.edtGetCommnets);
            //    text.setText("The product is very good I liked the quality..the service is also good..!");
            //    text.setTextColor(Color.GREEN);

              //  RatingBar purchase_ratingbar= (RatingBar) dialog.findViewById(R.id.purchase_ratingbar);

                final RatingBar rating= ((RatingBar)dialog.findViewById(R.id.starRatingBar));


                rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating,
                                                boolean fromUser) {
                        // TODO Auto-generated method stub

                        prodrating=String.valueOf(rating);
                    }
                });


               // prodrating =String.valueOf(rating.getRating());
             //   prodrating=String.valueOf(rating.getRating());
               /* ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageResource(R.drawable.ic_launcher);
*/
                Button dialogButton = (Button) dialog.findViewById(R.id.btnSendFeedback);
                Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                      //  Log.e("rating ","rating "+rating);

                        feedbackMsg=text.getText().toString().trim();


                        if(getValidation())

                        {
                            Log.e("prodrating ","prodrating "+prodrating);
                            dialog.dismiss();
                            AddRating(prodrating,feedbackMsg);
                        }

                    }
                });
                dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();


                    }
                });

                dialog.show();
            }
        });

    }

    private boolean getValidation() {
        if(!(prodrating!=null&&!(prodrating.isEmpty())))
        {
           Toast.makeText(vp.mom.activitys.PurchaseItemInvoiceActivity.this,"Please add some rating",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(feedbackMsg.length()<=0)
        {
            Toast.makeText(vp.mom.activitys.PurchaseItemInvoiceActivity.this,"Please write some feedback",Toast.LENGTH_SHORT).show();
            return false;
        }

    return true;

    }

    private void AddRating(final String rating, final String feedback) {
        String tag_string_req = "AddRating";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.PURCHASE_RATING, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("AddRating", "AddRating Response: " + response.toString());
                //	hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {

                        Toast.makeText(vp.mom.activitys.PurchaseItemInvoiceActivity.this,
                               "Successfuly rating added to product", Toast.LENGTH_LONG).show();
                        // user successfully logged in
                     //   Log.e("AddRating", "AddRating Response: " + response.toString());

                    } else {
                        // Error in login. Get the error message
                   //     String errorMsg = jObj.getString("message");
//                        Toast.makeText(PurchaseItemInvoiceActivity.this,
//                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(vp.mom.activitys.PurchaseItemInvoiceActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
              //  Log.e("ADD_COMMENTS", "ADD_COMMENTS Error: " + error.getMessage());
                Toast.makeText(vp.mom.activitys.PurchaseItemInvoiceActivity.this,
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
                params.put("product_id",productid);
                params.put("ratings",rating);
                params.put("review",feedback);
                params.put("orderid",orderid);
              //  params.put("comment",commentText);
           //     Log.e("params ","params "+params);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void setCustomActionBar() {
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
        tooltitle = (TextView) findViewById(R.id.tooltitle);
        tooltitle.setText("Invoice");

    }
    private void init() {
        txtorderid= (TextView) findViewById(R.id.txtorderid);
        txtnameofproduct = (TextView) findViewById(R.id.txtnameofproduct);
        txtProductColor= (TextView) findViewById(R.id.txtProductColor);
        txtshippingfname = (TextView) findViewById(R.id.txtshippingfname);
        txtproductcompany = (TextView) findViewById(R.id.txtproductcompany);
        txtproductbrand = (TextView) findViewById(R.id.txtproductbrand);
        txtorderdate = (TextView) findViewById(R.id.txtorderdate);
        txtdeliverydate = (TextView) findViewById(R.id.txtdeliverydate);
        txtShippingcharges = (TextView) findViewById(R.id.txtShippingcharges);
        txttotalamount = (TextView) findViewById(R.id.txtTotalAmount);
        txtFinalAmount = (TextView) findViewById(R.id.txtFinalAmount);
        txtproductdescription = (TextView) findViewById(R.id.txtproductdescription);
        txtShippingAddress = (TextView) findViewById(R.id.txtShippingAddress);
        txtdeliverystatus= (TextView) findViewById(R.id.txtdeliverystatus);
        rateProduct= (Button) findViewById(R.id.rateProduct);

        viewPager= (ViewPager) findViewById(R.id.view_pagerlist_invoice);
        circleIndicator= (vp.mom.circleindicator.CirclePageIndicator) findViewById(R.id.indicatorlistinvoice);
    }

    private void getPurchaseItemInvoice() {

        String tag_string_req = "getPurchaseItemInvoice";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.PURCHASE_ITEM_INVOICE, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                //   Log.e("reportProduct", "reportProduct Response: " + response.toString());
                //	hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {
                        //Toast.makeText(PrivacyPolicy.this, "Your concern about this product is recorded", Toast.LENGTH_SHORT).show();
                        Log.v("res", "" + jObj);

                        JSONArray jsonArray = jObj.getJSONArray("items");
                        txtorderid.setText(jsonArray.getJSONObject(0).getString("order_id"));
                        txtnameofproduct.setText(jsonArray.getJSONObject(0).getString("product_name"));
                        Log.v("input name", "" + txtnameofproduct);
                        txttotalamount.setText(jsonArray.getJSONObject(0).getString("tot_amt"));
                        txtFinalAmount.setText(jsonArray.getJSONObject(0).getString("final_amt"));
                      //  txtdeliverydate.setText(jsonArray.getJSONObject(0).getString("delivery_date"));

                        if(jsonArray.getJSONObject(0).getString("delivery_date").equals("null"))
                            txtdeliverydate.setText("-");
                        else
                            txtdeliverydate.setText(jsonArray.getJSONObject(0).getString("delivery_date"));

                        txtorderdate.setText(jsonArray.getJSONObject(0).getString("order_date"));
                   //     txtshippingfname.setText(jsonArray.getJSONObject(0).getString("billing_fname") + " " + jsonArray.getJSONObject(0).getString("billing_lname"));
                        txtShippingcharges.setText(jsonArray.getJSONObject(0).getString("shipping_charges"));

                        txtShippingAddress.setText(jsonArray.getJSONObject(0).getString("shipping_fname") + " , "+
                                jsonArray.getJSONObject(0).getString("shipping_lname")+" , "
                                +jsonArray.getJSONObject(0).getString("shipping_address")+" , "+
                                 jsonArray.getJSONObject(0).getString("shipping_city") + " , " +
                                        jsonArray.getJSONObject(0).getString("shipping_state") + " , " +
                                        jsonArray.getJSONObject(0).getString("shipping_country") + " , " +
                                        jsonArray.getJSONObject(0).getString("shipping_zip") + ",\n mob_no : "
                                        + jsonArray.getJSONObject(0).getString("shipping_contact"));


                        txtnameofproduct.setText(jsonArray.getJSONObject(0).getString("product_name"));
                        txtproductcompany.setText(jsonArray.getJSONObject(0).getString("company"));
                        txtproductbrand.setText(jsonArray.getJSONObject(0).getString("product_brand"));
                        txtproductdescription.setText(jsonArray.getJSONObject(0).getString("product_description"));
                        txtProductColor.setText(jsonArray.getJSONObject(0).getString("product_colour"));
                        txtdeliverystatus.setText(jsonArray.getJSONObject(0).getString("delivery_status"));

                        imglist =new ArrayList<>();

                        JSONObject myobj=jsonArray.getJSONObject(0);
                        JSONArray sellerArry = myobj.getJSONArray("seller_details");

                        JSONObject sellorobj=sellerArry.getJSONObject(0);


                        txtshippingfname.setText(sellorobj.getString("first_name")+" "+sellorobj.getString("last_name"));

                        JSONArray imageArray = jsonArray.getJSONObject(0).getJSONArray("product_image");
                        for (int j = 0; j < imageArray.length(); j++) {
                            JSONObject imagefeed = (JSONObject) imageArray.get(j);
                            imglist.add(imagefeed.getString("path").replace("th_", ""));
                        }
                        ImagePagerAdapter adapter =new ImagePagerAdapter(imglist);

                        viewPager.setAdapter(adapter);


                        circleIndicator.setViewPager(viewPager);


                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                       /* Toast.makeText(PurchaseItemInvoiceActivity.this,
                                errorMsg, Toast.LENGTH_LONG).show();*/
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                  //  Toast.makeText(PurchaseItemInvoiceActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //   Log.e("ReportProduct", "ReportProduct Error: " + error.getMessage());
               /* Toast.makeText(PurchaseItemInvoiceActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();*/
                //	hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
//                params.put("order_id",orderid);
//                params.put("product_id",productid);

                params.put("order_id",orderid);
                params.put("product_id",productid);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    private class ImagePagerAdapter extends PagerAdapter {
        ArrayList<String> imagearry;

        public  ImagePagerAdapter(ArrayList<String> mimagearry)
        {
            this.imagearry=mimagearry;

            //	Log.e("size",""+imagearry.size());
        }
        @Override
        public void destroyItem(final ViewGroup container, final int position, final Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }

        @Override
        public int getCount() {
            return this.imagearry.size();
        }
        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            final Context context = vp.mom.activitys.PurchaseItemInvoiceActivity.this;
            final ImageView imageView = new ImageView(context);
            final int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_top);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Picasso.with(context)
                    .load(imagearry.get(position))
//                    .placeholder(R.drawable.ic_facebook)
                    .centerCrop()
                    .resize( 346,
                            320)
                    .error(android.R.drawable.stat_notify_error)
                    .into(imageView);


//            Picasso.with(SoldItemInvoiceActivity.this)
//                    .load(imagearry.get(position)).networkPolicy(NetworkPolicy.OFFLINE)
//                    .into(imageView);

            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(final View view, final Object object) {
            return view == ((ImageView) object);
        }
    }

}
