public class Days {
	
	private String days;
	
	public Days(String d) {
		days = d.toUpperCase().trim();
		if (!days.matches("[MTWRFS]+"))
			throw new IllegalArgumentException(days + " contains invalid days");
		if (days.replaceFirst("([MTWRFS])[MTWRFS]*\\1", "$1").length() != days.length())
			throw new IllegalArgumentException(days + " contains duplicate days");
	}
	public String toString() {
		return days;
	}
	public boolean equals(Object obj) {
		return obj instanceof Days && ((Days) obj).toString().length() == days.length() 
			&& ((Days) obj).days.replaceAll("[" + days + "]", "").length() == 0;
	}
	public boolean isOneDay() {
		return days.length() == 1;
	}
	public boolean interferesWith(Days d) {
		for (int i = 0; i < days.length(); i++)
			if (d.days.contains(days.subSequence(i, i+1)))
				return true;
		return false;
	}
}


