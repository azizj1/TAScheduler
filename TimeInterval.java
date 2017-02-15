public class TimeInterval {
	private DigitalTime startTime, endTime;
	
	public TimeInterval(DigitalTime a, DigitalTime b) {
		setInterval(a,b);
	}
	
	public void setInterval(DigitalTime a, DigitalTime b) {
		if (b.isLess(a))
			throw new IllegalArgumentException("Invalid time interval: " + a + "-" + b);
		if (b.equals(a))
			throw new IllegalArgumentException("Start and end time are the same: " + a + "-" + b);
		startTime = a;
		endTime = b;
	}

	public DigitalTime getStart() {
		return startTime;
	}
	public DigitalTime getEnd() {
		return endTime;
	}

	public boolean isOverlap(DigitalTime a) {
		return startTime.isLess(a) && a.isLess(endTime);
	}
	public boolean intereferesWith(TimeInterval t) {
		return equals(t) || isOverlap(t.startTime) || isOverlap(t.endTime) || 
			t.isOverlap(startTime) || t.isOverlap(endTime);
	}
	
	public String toString() {
		return startTime + "-" + endTime;
	}
	public boolean equals(Object obj) {
		return (obj instanceof TimeInterval) && ((TimeInterval) obj).startTime.equals(startTime)
			&& ((TimeInterval) obj).endTime.equals(endTime);
	}
}