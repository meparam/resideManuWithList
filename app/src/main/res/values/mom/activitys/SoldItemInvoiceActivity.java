package vp.mom.activitys;

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
import android.widget.ImageView;
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
import vp.mom.api.AppConfig;
import vp.mom.app.AppController;


/**
 * ,
 * Created by pallavi.b on 30-Jan-16.
 */
public class SoldItemInvoiceActivity extends AppCompatActivity {
    private TextView txtorderid, txtTotalAmount, txtFinalAmount, txtdeliverydate, txtshippingfname, txtshippinglastname, txtShippingAddress, txtShippingCity,
            txtShippingState, txtdeliverystatus,txtShippingcharges, txtorderdate, txtShippingCountry, txtShippingZipcode, txtshippingContact, txtnameofproduct, txtproductcompany, txtproductbrand, txtproductdescription, txtdeliverytype,
            txtProductColor;
    vp.mom.api.SessionManager session;
    String orderid,productid;
    TextView tooltitle;
    ArrayList<String> imglist;
    ViewPager viewPager;
     vp.mom.circleindicator.CirclePageIndicator circleIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sold_item_invoice);
        session = new vp.mom.api.SessionManager(this);

        setCustomActionBar();
        init();
        Intent intent=getIntent();
        if (intent != null){
            productid=intent.getStringExtra("product_id");
            orderid=intent.getStringExtra("ord_det_id");
            getSoldItemInvoice();

        }else{
            Toast.makeText(vp.mom.activitys.SoldItemInvoiceActivity.this, "Something wents wrong..", Toast.LENGTH_SHORT).show();
        }

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
    private void getSoldItemInvoice() {

        String tag_string_req = "getSoldItemInvoice";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.SOLD_ITEM_INVOICE, new Response.Listener<String>() {


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

                      //  if(!jsonArray.getJSONObject(0).isNull("productimages")) {
                            txtorderid.setText(jsonArray.getJSONObject(0).getString("order_id"));
                            Log.v("input name", "" + txtorderid);
                            txtTotalAmount.setText(jsonArray.getJSONObject(0).getString("total_amount"));
                            txtFinalAmount.setText(jsonArray.getJSONObject(0).getString("final_amount"));
                        if(jsonArray.getJSONObject(0).getString("delivery_date").equals("null"))
                            txtdeliverydate.setText("-");
                        else
                            txtdeliverydate.setText(jsonArray.getJSONObject(0).getString("delivery_date"));
                            txtorderdate.setText(jsonArray.getJSONObject(0).getString("order_date"));
                        //    txtshippingfname.setText(jsonArray.getJSONObject(0).getString("shipping_fname") + " " + jsonArray.getJSONObject(0).getString("shipping_lname"));
                            txtShippingcharges.setText(jsonArray.getJSONObject(0).getString("shipping_charges"));
                            txtShippingAddress.setText(jsonArray.getJSONObject(0).getString("shipping_address") + " , " + jsonArray.getJSONObject(0).getString("shipping_city") + " , " + jsonArray.getJSONObject(0).getString("shipping_state") + " , " + jsonArray.getJSONObject(0).getString("shipping_country") + " , " + jsonArray.getJSONObject(0).getString("shipping_zip") + ",\n mob_no : " + jsonArray.getJSONObject(0).getString("shipping_contact"));
                            txtnameofproduct.setText(jsonArray.getJSONObject(0).getString("product_name"));
                            txtproductcompany.setText(jsonArray.getJSONObject(0).getString("company"));
                            txtproductbrand.setText(jsonArray.getJSONObject(0).getString("brand"));
                            txtproductdescription.setText(jsonArray.getJSONObject(0).getString("description"));
                            txtProductColor.setText(jsonArray.getJSONObject(0).getString("colour"));
                            txtdeliverystatus.setText(jsonArray.getJSONObject(0).getString("delivery_status"));

        txtshippingfname.setText(jsonArray.getJSONObject(0).getString("first_name")+" "+jsonArray.getJSONObject(0).getString("last_name"));



                            imglist =new ArrayList<>();

                            JSONArray imageArray = jsonArray.getJSONObject(0).getJSONArray("product_image");
                            for (int j = 0; j < imageArray.length(); j++) {
                                JSONObject imagefeed = (JSONObject) imageArray.get(j);
                                imglist.add(imagefeed.getString("path").replace("th_", ""));
                            }
                                ImagePagerAdapter adapter =new ImagePagerAdapter(imglist);

                                viewPager.setAdapter(adapter);


                        circleIndicator.setViewPager(viewPager);

                                // Log.e(TAG, "" + imagefeed.getString("path"));
                          //  }
                          //  item.setImagearray(img);

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(vp.mom.activitys.SoldItemInvoiceActivity.this,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(vp.mom.activitys.SoldItemInvoiceActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //   Log.e("ReportProduct", "ReportProduct Error: " + error.getMessage());
                Toast.makeText(vp.mom.activitys.SoldItemInvoiceActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //	hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_id", orderid);
                params.put("product_id",productid);

             //   Log.e("iDDD",""+orderid+" "+productid);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

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
        txtTotalAmount = (TextView) findViewById(R.id.txtTotalAmount);
        txtFinalAmount = (TextView) findViewById(R.id.txtFinalAmount);
        txtproductdescription = (TextView) findViewById(R.id.txtproductdescription);
        txtShippingAddress = (TextView) findViewById(R.id.txtShippingAddress);
        txtdeliverystatus= (TextView) findViewById(R.id.txtdeliverystatus);

        viewPager= (ViewPager) findViewById(R.id.view_pagerlist_invoice);

        circleIndicator = (vp.mom.circleindicator.CirclePageIndicator) findViewById(R.id.indicatorlistinvoice);
    }


    //	private void showDialog() {
//		if (!pDialog.isShowing())
//			pDialog.show();
//	}
//
//	private void hideDialog() {
//		if (pDialog.isShowing())
//			pDialog.dismiss();
//	}
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
            final Context context = vp.mom.activitys.SoldItemInvoiceActivity.this;
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
