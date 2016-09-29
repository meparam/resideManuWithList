package app.num.residemenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.special.ResideMenu.ResideMenu;


public class HomeFragment extends Fragment {

    private View parentView;
    private ResideMenu resideMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.home, container, false);

//        AndroidNetworking.get("http://api.androidhive.info/feed/feed.json")
//                .addPathParameter("pageNumber", "0")
//                .addQueryParameter("limit", "3")
//                .addHeaders("token", "1234")
//                .setTag("test")
//                .setPriority(Priority.LOW)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // do anything with response
//                        Toast.makeText(getActivity(),"helo"+response,1000).show();
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                        Toast.makeText(getActivity(),"helo"+error,1000).show();
//                    }
//                });

        return parentView;
    }


}
