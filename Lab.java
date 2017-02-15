public class Lab extends Rendezvous {
	
	private int secNum;
	private static final int MIN_SEC_NUM = 800, MAX_SEC_NUM = 820;
	
	public Lab(int secNum, TimeInterval time, Days day) {
		super(time, day);
		if (!isValidSecNum(secNum))
			throw new IllegalArgumentException(secNum + " is not between " + 
					MIN_SEC_NUM + "-" + MAX_SEC_NUM);
		this.secNum = secNum;
	}
	public Lab(int secNum, String startTime, String endTime, String days) {
		super(startTime, endTime, days);
		if (!isValidSecNum(secNum))
			throw new IllegalArgumentException(secNum + " is not between " + 
					MIN_SEC_NUM + "-" + MAX_SEC_NUM);
		this.secNum = secNum;
		
	}
	public static boolean isValidSecNum(int secNum) {
		return secNum >= MIN_SEC_NUM && secNum <= MAX_SEC_NUM;
	}
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Lab) && ((Lab) obj).secNum == secNum;
	}
	@Override
	public String toString() {
		return "Lab " + secNum + ": " + super.toString();
	}
	
	public int getSecNum() {
		return secNum;
	}

}