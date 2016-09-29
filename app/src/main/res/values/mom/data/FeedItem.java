package vp.mom.data;

import java.util.ArrayList;

public class FeedItem {

boolean bookmark_status;
String productBrand,paypalEmail;

	public String getPaypalEmail() {
		return paypalEmail;
	}

	public void setPaypalEmail(String paypalEmail) {
		this.paypalEmail = paypalEmail;
	}

	public String getProductBrand() {
		return productBrand;
	}

	public void setProductBrand(String productBrand) {
		this.productBrand = productBrand;
	}

	public boolean isBookmark_status() {
		return bookmark_status;
	}

	public void setBookmark_status(boolean bookmark_status) {
		this.bookmark_status = bookmark_status;
	}

	String productName,shippingCost;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(String shippingCost) {
		this.shippingCost = shippingCost;
	}

	private String id;
	private boolean isLike;

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	String deliveryType;
String prodSize;

//	public boolean isMeetInperson() {
//		return meetInperson;
//	}
//
//	public void setMeetInperson(boolean meetInperson) {
//		this.meetInperson = meetInperson;
//	}

	String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProdSize() {
		return prodSize;
	}

	public void setProdSize(String prodSize) {
		this.prodSize = prodSize;
	}

	public boolean isLike() {
		return isLike;
	}

	public void setIsLike(boolean isLike) {
		this.isLike = isLike;
	}

	public String getProdDesc() {
		return prodDesc;
	}

	public void setProdDesc(String prodDesc) {
		this.prodDesc = prodDesc;
	}

	private String name, email, image, profilePic, timeStamp,base_price,prodDesc;

	int likeCount;

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	ArrayList<String> imagearray;
	public FeedItem() {
	}

	public ArrayList<String> getImagearray() {
		return imagearray;
	}

	public void setImagearray(ArrayList<String> imagearray) {
		this.imagearray = imagearray;
	}

	public FeedItem(String id, String name, ArrayList<String>  imagearray, String email,
			String profilePic, String timeStamp, String base_price) {
		super();
		this.id = id;
		this.name = name;
		this.imagearray = imagearray;
		this.email = email;
		this.profilePic = profilePic;
		this.timeStamp = timeStamp;
		this.base_price = base_price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImge() {
		return image;
	}

	public void setImge(String image) {
		this.image = image;
	}

	public String getemail() {
		return email;
	}

	public void setemail(String email) {
		this.email = email;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getbase_price() {
		return base_price;
	}

	public void setbase_price(String base_price) {
		this.base_price = base_price;
	}
}
