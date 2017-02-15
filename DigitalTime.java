public class DigitalTime {
	private int hour, minute;
	
	public DigitalTime(int hr, int min) {
		set(hr, min);
	}
	public DigitalTime(String time) {
		String[] hrMin = time.split("\\s?:\\s?");
		if (hrMin.length != 2)
			throw new IllegalArgumentException("Illegal time: " + time);
		int hr = 0, min = 0;
		try {
			hr = Integer.parseInt(hrMin[0].trim());
			min = Integer.parseInt(hrMin[1].trim());
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid time: " + time);
		}
		set(hr, min);
	}

	public DigitalTime() {
		this(0,0);
	}
	public boolean equals(Object obj) {
		if (obj instanceof DigitalTime)
			return (this.getHour() == ((DigitalTime) obj).getHour()) && 
				(this.getMinute() == ((DigitalTime) obj).getMinute());
		return false;
	}

	public boolean isLess(DigitalTime time1) {
		return (hour*60 + minute) < (time1.hour*60 + time1.minute);
	}
	
	public int getHour() {
		return hour;
	}
	public int getMinute() {
		return minute;
	}
	public void set(int hr, int min) {
		if (isInvalid(hr, min))
			throw new IllegalArgumentException("Incorrect time specified: " + hr + ":" + min);
		hour = hr;
		minute = min;
	} 
	public String toString() {
		return String.format("%d:%02d", hour, minute);
	}
	
	private boolean isInvalid(int hr, int min) {
		return (hr < 0 || hr > 23 || min < 0 || min > 59);
	}
	
}
