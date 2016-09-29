package vp.mom.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;

public class MultipartRequest extends Request<String> {

    private MultipartEntity entity = new MultipartEntity();

    private String key_name;

    private final Listener<String> mListener;


  //   private final File file;
    String path;
    private final HashMap<String, String> params;

    public MultipartRequest(String key_name, String url, Listener<String> listener,
                            ErrorListener errorListener, String path,
                            HashMap<String, String> params) {
        super(Method.POST, url, errorListener);

        mListener = listener;
        this.path = path;
        this.params = params;
        this.key_name = key_name;
        buildMultipartEntity();
    }

    @SuppressWarnings("deprecation")
    private void buildMultipartEntity() {
try {

    InputStream is = new URL( path ).openStream();
    Bitmap bitmap = BitmapFactory.decodeStream( is );
  //  Bitmap bitmap = BitmapFactory.decodeFile(path.toString());
  //  Bitmap bitmap = decodeFile(path.toString());
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bos);
    byte[] data = bos.toByteArray();


    entity.addPart("userfile", new ByteArrayBody(data,
            "image/jpeg", ""+System.currentTimeMillis()));
    bitmap.recycle();
}
    catch (Exception e)
    {
       // entity.addPart("userfile", new FileBody(file));
    }
        try {
            for (String key : params.keySet()) {
                entity.addPart(key, new StringBody(params.get(key)));
            }
        } catch (UnsupportedEncodingException e) {
            VolleyLog.e("UnsupportedEncodingException");
        }
    }
//    public Bitmap decodeFile(String filePath) {
//        // Decode image size
//        BitmapFactory.Options o = new BitmapFactory.Options();
//        o.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(filePath, o);
//        // The new size we want to scale to
//        final int REQUIRED_SIZE = 1024;
//        // Find the correct scale value. It should be the power of 2.
//        int width_tmp = o.outWidth, height_tmp = o.outHeight;
//        int scale = 1;
//        while (true) {
//            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
//                break;
//            width_tmp /= 2;
//            height_tmp /= 2;
//            scale *= 2;
//        }
//        BitmapFactory.Options o2 = new BitmapFactory.Options();
//        o2.inSampleSize = scale;
//        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
//        return bitmap;
//    }
    @Override
    public String getBodyContentType() {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    /**
     * copied from Android StringRequest class
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed,
                HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}
