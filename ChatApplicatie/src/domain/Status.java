package domain;

public enum Status {
	
	ONLINE ("Online"),
	AWAY ("Away"),
	DONOTDISTURB ("Do not disturb"),
	OFFLINE ("Offline");
	
	private String statusString;
	
	private Status(String statusString) {
		this.statusString = statusString;
	}
	
	public String getStatusString() {
		return this.statusString;
	}

}
