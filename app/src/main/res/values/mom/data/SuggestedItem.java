package vp.mom.data;

import java.util.ArrayList;

/**
 * Created by shivkanya.i on 23-11-2015.
 */
public class SuggestedItem {

String userid;
    String lname;
  //  String email;
    String fname;
    String profile_image;
    String location;
String itemDisc;

    public String getItemDisc() {
        return itemDisc;
    }

    public void setItemDisc(String itemDisc) {
        this.itemDisc = itemDisc;
    }

    public ArrayList<String> getImagearray() {
        return imagearray;
    }

    public void setImagearray(ArrayList<String> imagearray) {
        this.imagearray = imagearray;
    }

    ArrayList<String> imagearray;
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public  SuggestedItem(){}
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }


}
