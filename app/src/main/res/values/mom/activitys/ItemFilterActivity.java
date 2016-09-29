package vp.mom.activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vp.mom.R;
import vp.mom.api.AppConfig;
import vp.mom.app.AppController;

public class ItemFilterActivity extends AppCompatActivity {
    private static final String TAG = vp.mom.activitys.ItemFilterActivity.class.getSimpleName();
    ImageView seeling_acccept;
    ImageView seeling_cancel;
    RadioGroup radioitemType;
    Button btnCategory,btnSubCategory,btnSize,btnColor,btnclearItem;
    private ProgressDialog pDialog;
    String[] allcategory_array, allcategory_id_array, allsubcategory_array, all_sub_category_id_array;
    String[]all_sizes_array,all_sizes_id_arry;
    String[] color_array={ "Black","Blue","Brown","Green","Multi colour","Orange","Beige","pink" ,
            " Purple","Red","Silver","White" ,"Gold","Non Specific"};
    String categoryId,subcategoryId,itemType,itemSize,itemColor;
    vp.mom.api.SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_filter_view);
        session=new vp.mom.api.SessionManager(vp.mom.activitys.ItemFilterActivity.this);
        pDialog = new ProgressDialog(vp.mom.activitys.ItemFilterActivity.this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        setUpActionBar();
        initView();
         seeling_cancel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 finish();
             }
         });
        seeling_acccept.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent data = new Intent();


        setResult(RESULT_OK, data);
        vp.mom.utils.StaticData.itemFilterFlag=true;
        finish();
    }
        });
        btnclearItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCategory.setText("Category");
                btnSubCategory.setText("Sub Category");
                btnSize.setText("Size");
                btnColor.setText("Color");
                vp.mom.utils.StaticData.itemCategory="";
                vp.mom.utils.StaticData.itemSubCategory="";
                vp.mom.utils.StaticData.itemcolor="";
                vp.mom.utils.StaticData.itemsize="";
                vp.mom.utils.StaticData.itemType="";


            }
        });

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog_area = new AlertDialog.Builder(
                        vp.mom.activitys.ItemFilterActivity.this);

                // Set the dialog title

                dialog_area.setTitle("Select Cataegory! ")

                        .setSingleChoiceItems(allcategory_array, session.getIntegerSessionData("itemcat"),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        btnCategory.setText(allcategory_array[item]);
                                        categoryId=allcategory_id_array[item].toString().trim();

//                                        Log.e("value after select", " : "
//                                                + categoryId);

                                        vp.mom.utils.StaticData.itemCategory=allcategory_array[item].toString();

                                        getAllSUbCategory(categoryId);
                                    session.setIntegerSessionData("itemcat",item);
                                        dialog.dismiss();

                                    }
                                });

                dialog_area.show();
            }
        });

        btnSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder dialog_area = new AlertDialog.Builder(
                        vp.mom.activitys.ItemFilterActivity.this);

                // Set the dialog title

                dialog_area.setTitle("Select Sub Cataegory! ")

                        .setSingleChoiceItems(allsubcategory_array, session.getIntegerSessionData("itemsubcat"),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {

//                                        Log.e("value after select", " : "
//                                                + all_sub_category_id_array[item]);
                                        btnSubCategory.setText(allsubcategory_array[item]);

                                        vp.mom.utils.StaticData.itemSubCategory=allsubcategory_array[item].toString();

                                        subcategoryId = all_sub_category_id_array[item].toString().trim();
                                        getAllSizes(subcategoryId);
                                        session.setIntegerSessionData("itemsubcat",item);
                                        dialog.dismiss();

                                    }
                                });

                dialog_area.show();
            }
        });

        btnSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (all_sizes_array != null && all_sizes_array.length > 0) {


                final AlertDialog.Builder dialog_area = new AlertDialog.Builder(
                        vp.mom.activitys.ItemFilterActivity.this);
                dialog_area.setTitle("Select Size! ")

                        .setSingleChoiceItems(all_sizes_array,session.getIntegerSessionData("itemsize"),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        btnSize.setText(all_sizes_array[item]);

                                        itemSize=all_sizes_array[item].toString().trim();

//                                        Log.e("value after select", " : "
//                                                + all_sizes_array);

                                        vp.mom.utils.StaticData.itemsize=itemSize;

                                        session.setIntegerSessionData("itemsize",item);
                                        dialog.dismiss();

                                    }
                                });

                dialog_area.show();
                }
                else
                    Toast.makeText(vp.mom.activitys.ItemFilterActivity.this, "Size is not available", Toast.LENGTH_SHORT).show();
            }
        });
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder dialog_area = new AlertDialog.Builder(
                        vp.mom.activitys.ItemFilterActivity.this);
                dialog_area.setTitle("Select Colour! ")

                        .setSingleChoiceItems(color_array,session.getIntegerSessionData("itemcolor"),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        btnColor.setText(color_array[item]);

                                        itemColor=color_array[item].toString().trim();

//                                        Log.e("value after select", " : "
//                                                + color_array);
                                        session.setIntegerSessionData("itemcolor",item);
                                        vp.mom.utils.StaticData.itemcolor=itemColor;
                                        dialog.dismiss();

                                    }
                                });

                dialog_area.show();
            }
        });
        radioitemType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb = (RadioButton) findViewById(checkedId);
                itemType = rb.getText().toString();

                if (itemType.equalsIgnoreCase("new"))
                    itemType = "new";
                else if (itemType.equalsIgnoreCase("new"))
                    itemType = "used";
                else
                    itemType = "both";

                //  showMessage(""+checkedId);

                vp.mom.utils.StaticData.itemType=itemType;
            }
        });
        getAllCategory();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        btnCategory= (Button) findViewById(R.id.btnCategoryItem);
        btnSubCategory= (Button) findViewById(R.id.btnSubCategoryItem);
        btnSize= (Button) findViewById(R.id.btnSizeItem);
        btnColor= (Button) findViewById(R.id.btnColorItem);
        radioitemType= (RadioGroup) findViewById(R.id.radio_group_item_type);
        btnclearItem= (Button) findViewById(R.id.btnclearItem);

    }

    private void setUpActionBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
         seeling_acccept = (ImageView) toolbar.findViewById(R.id.seeling_acccept);
        seeling_acccept.setClickable(true);
        seeling_cancel= (ImageView) toolbar.findViewById(R.id.seeling_cancel);

        seeling_cancel.setClickable(true);
        radioitemType=(RadioGroup)findViewById(R.id.fancy_radio_group);



    }
    //getall category
    private void getAllCategory() {

        String tag_string_req = "getAllCharity";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_ALL_CATEGORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
             //   Log.e(TAG, "cHARITY Response: " + response);
                hideDialog();

                ArrayList<String> stringArrayListID = new ArrayList<String>();
                ArrayList<String> stringArrayListNAME = new ArrayList<String>();
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    if (status) {

                        JSONArray jsonArray = jObj.getJSONArray("items");
                        for (int i=0;i<jsonArray.length();i++)
                        {

                            JSONObject  jsonObject = jsonArray.getJSONObject(i);

                            stringArrayListID.add(jsonObject.getString("id"));
                            stringArrayListNAME.add(jsonObject.getString("category"));

                        }

                        allcategory_array=stringArrayListNAME.toArray(new String[stringArrayListNAME.size()]);
                        allcategory_id_array=stringArrayListID.toArray(new String[stringArrayListID.size()]);
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                      //  String errorMsg = jObj.getString("status");
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
              //  Log.e(TAG, "getAllCharity Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid",session.getStringSessionData("uid"));
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    //getall SUB category
    private void getAllSUbCategory(final String mId) {
        String tag_string_req = "getAllSUbCategory";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_ALL_SUB_CATEGORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
            //    Log.e(TAG, "cHARITY Response: " + response);
                hideDialog();

                ArrayList<String> stringArrayListID = new ArrayList<String>();
                ArrayList<String> stringArrayListNAME = new ArrayList<String>();
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    if (status) {

                        JSONArray jsonArray = jObj.getJSONArray("items");
                        for (int i=0;i<jsonArray.length();i++)
                        {

                            JSONObject  jsonObject = jsonArray.getJSONObject(i);

                            stringArrayListID.add(jsonObject.getString("id"));
                            stringArrayListNAME.add(jsonObject.getString("sub_category"));

                        }

                        allsubcategory_array=stringArrayListNAME.toArray(new String[stringArrayListNAME.size()]);
                        all_sub_category_id_array=stringArrayListID.toArray(new String[stringArrayListID.size()]);
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                      //  String errorMsg = jObj.getString("status");
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
              //  Log.e(TAG, "getAllCharity Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                params.put("cid",mId);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    //getall get All Sizes
    private void getAllSizes(final String sId) {

        String tag_string_req = "getAllSizes";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_ALL_SIZES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
              //  Log.e(TAG, "cHARITY Response: " + response);
                hideDialog();

                ArrayList<String> stringArrayListSizeID = new ArrayList<String>();
                ArrayList<String> stringArrayListSizeNAME = new ArrayList<String>();
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    if (status) {

                        JSONArray jsonArray = jObj.getJSONArray("items");
                        for (int i=0;i<jsonArray.length();i++)
                        {

                            JSONObject  jsonObject = jsonArray.getJSONObject(i);

                            stringArrayListSizeID.add(jsonObject.getString("id"));
                            stringArrayListSizeNAME.add(jsonObject.getString("size"));

                        }
                        all_sizes_array=stringArrayListSizeNAME.toArray(new String[stringArrayListSizeNAME.size()]);
                        all_sizes_id_arry=stringArrayListSizeID.toArray(new String[stringArrayListSizeID.size()]);
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                     //   String errorMsg = jObj.getString("status");
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
             //   Log.e(TAG, "getAllCharity Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                params.put("scid",sId);
                return params;
            }

        };
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
