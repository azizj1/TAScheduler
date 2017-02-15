public class Rendezvous {
	
	private TimeInterval interval;
	private Days meets;
	
	public Rendezvous(TimeInterval time, Days days) {
		interval = time;
		meets = days;
	}
	public Rendezvous(String startTime, String endTime, String days) {
		meets = new Days(days);
		interval = new TimeInterval(new DigitalTime(startTime), new DigitalTime(endTime));
	}
	public TimeInterval interval() {
		return interval;
	}
	public Days daysOfWeek() {
		return meets;
	}
	public String toString() {
		return  meets + " " + interval;
	}
	public boolean interferesWith(Rendezvous r) {
		return meets.interferesWith(r.meets) && interval.intereferesWith(r.interval);
	}
}
