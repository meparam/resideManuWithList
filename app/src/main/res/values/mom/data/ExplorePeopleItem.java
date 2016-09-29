package vp.mom.data;

import java.util.ArrayList;

/**
 * Created by shivkanya.i on 01-12-2015.
 */
public class ExplorePeopleItem {

    String Id,userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    ArrayList<String> product_Image;

    public ArrayList<String> getProduct_Image() {
        return product_Image;
    }

    public void setProduct_Image(ArrayList<String> product_Image) {
        this.product_Image = product_Image;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }


    public ExplorePeopleItem() {

    }
}
