public class Alibi extends Rendezvous {
	
	private String reason;

	public Alibi(String startTime, String endTime, String days, String reason) {
		super(startTime, endTime, days);
		this.reason = reason;
	}
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String toString() {
		return super.toString() + "; Reason: " + reason;
	}
	

}
