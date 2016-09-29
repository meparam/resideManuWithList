package vp.mom.data;

public class NotificationItem {

	private String  url;
	private String description;
	private String time;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public NotificationItem(String url, String description, String time) {
		this.url = url;
		this.description = description;
		this.time = time;
	}



}
