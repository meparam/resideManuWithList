package vp.mom.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vp.mom.R;
import vp.mom.app.AppController;
import vp.mom.data.SalePurchaseItemPojo;
import vp.mom.utils.VolleySingleton;

public class ChartsAdapter extends RecyclerView.Adapter<vp.mom.adapters.ChartsAdapter.ViewHolder> {
    ArrayList<SalePurchaseItemPojo> soldItemarray;
    private ImageLoader mImageLoader;
    // private JSONArray mDataset;
    private Context mContext;
    String[] prod_status_array ={"Delivered","Dispatched","Canceled"};
    private ProgressDialog pDialog;
    public ChartsAdapter(Context mContext, ArrayList<SalePurchaseItemPojo> mSoldItemarray) {
        mImageLoader = VolleySingleton.getInstance(mContext).getImageLoader();
        this.mContext = mContext;
        this.soldItemarray = mSoldItemarray;
        pDialog = new ProgressDialog(mContext,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_sold_items_xml, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {


        final SalePurchaseItemPojo data = soldItemarray.get(position);
        viewHolder.sold_item_title.setText(data.getItemName());
        //  viewHolder.sold_item_disc.setText(data.getItemDesc());
        viewHolder.sold_item_price.setText("£" + data.getItemPrice());

        viewHolder.sold_item_rating.setRating(data.getProductRating());

        if(!data.getDelivery_status().equalsIgnoreCase("Delivered"))
            viewHolder.itemStatusbtn.setBackgroundColor(Color.parseColor("#ffff0000"));
            else
            viewHolder.itemStatusbtn.setBackgroundColor(Color.parseColor("#4CAF50"));

        viewHolder.itemStatusbtn.setText(data.getDelivery_status());

        Picasso.with(mContext)
                .load(data.getItemImage()).resize(180,
                177)
//                    .placeholder(R.drawable.ic_facebook)
                        //	.centerCrop()
//					.resize( 346,
//							 320)
                .error(android.R.drawable.stat_notify_error)
                .into( viewHolder.soldImage);

//        Picasso.with(mContext).load(data.getItemImage())
//                .resize(180,
//                        177).into(target);


        viewHolder.itemStatusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog_area = new AlertDialog.Builder(
                        mContext);
                // Set the dialog title
            int pos=0;

               if(data.getDelivery_status().equalsIgnoreCase("Delivered"))
                   pos=0;
                 else if(data.getDelivery_status().equalsIgnoreCase("Dispatched"))
                   pos=1;
                    else
                   pos=2;


                dialog_area.setTitle("status of product")

                        .setSingleChoiceItems(prod_status_array,pos,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
//                                        btnCategory.setText(allcategory_array[item]);
//                                        categoryId = allcategory_id_array[item].toString().trim();
//
//                                        getAllSUbCategory(categoryId);
                                        viewHolder.itemStatusbtn.setText(prod_status_array[item].toString());

                                        AddStatusToSoldProduct(viewHolder.itemStatusbtn,data.getOrder_id(), data.getProduct_id(), prod_status_array[item].toString());

                                        dialog.dismiss();

                                    }
                                });

                dialog_area.show();
            }
        });

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(mContext, "hello", 1000).show();

                Intent intent = new Intent(mContext, vp.mom.activitys.SoldItemInvoiceActivity.class);
                intent.putExtra("product_id",data.getProduct_id());
                intent.putExtra("ord_det_id",data.getOrder_detailed_Id());

                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return soldItemarray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sold_item_title, sold_item_disc, sold_item_price;
        ImageView soldImage;
        LinearLayout solditemview;
        View view;
        RatingBar sold_item_rating;
        Button itemStatusbtn;
        public ViewHolder(LinearLayout v) {
            super(v);
            this.view = v;
            this.sold_item_title = (TextView) v.findViewById(R.id.sold_item_title);
            this.sold_item_disc = (TextView) v.findViewById(R.id.sold_item_disc);
            this.sold_item_price = (TextView) v.findViewById(R.id.sold_item_price);
            this.soldImage = (ImageView) v.findViewById(R.id.sold_item_image);
            this.solditemview = (LinearLayout) v.findViewById(R.id.solditemview);
            this.itemStatusbtn = (Button) v.findViewById(R.id.itemStatusbtn);
            this.sold_item_rating = (RatingBar) v.findViewById(R.id.sold_item_rating);

        }
    }
    private void AddStatusToSoldProduct(final Button viewHolder,final String orderID, final String productId, final String statusOfProduct) {
        String tag_string_req = "AddComments";

        pDialog.setMessage("");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                vp.mom.api.AppConfig.SOLD_ITEM_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            //    Log.e("ADD_COMMENTS", "ADD_COMMENTS Response: " + response);
                	hideDialog();
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {

                        if(statusOfProduct.equalsIgnoreCase("Delivered"))
                              viewHolder.setBackgroundColor(Color.parseColor("#4CAF50"));
                        else
                            viewHolder.setBackgroundColor(Color.parseColor("#ffff0000"));

                        viewHolder.setText(statusOfProduct);
                    } else {
                       // viewHolder.setBackgroundColor(Color.parseColor("#ffff0000"));
                      //  Log.e("ADD_COMMENTS", "ADD_COMMENTS Response: " + response.toString());
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(mContext,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(mContext, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
             //   Log.e("ADD_COMMENTS", "ADD_COMMENTS Error: " + error.getMessage());
                Toast.makeText(mContext,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
//                params.put("email", email);
//                params.put("password", password);

                params.put("order_id",orderID);
                params.put("product_id",productId);
                params.put("delivery_status",statusOfProduct);

              //  Log.e("params", "params : " + params);
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
