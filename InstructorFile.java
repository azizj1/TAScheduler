import java.io.File;
import java.util.ArrayList;

public class InstructorFile implements FileFormat {
	
	private ArrayList<Lab> labs;
	private ArrayList<TA> TAs;
	private int minPrefSecs;
	private File source;

	public InstructorFile(IniFile file) {
		labs = new ArrayList<Lab>();
		TAs = new ArrayList<TA>();
		minPrefSecs = 0;
		source = file.getFile();
		
		add(file);
	}
	
	public void add(IniFile file) {
		for (String sec : file.sectionSet()) {
			if (sec.matches("lab\\d+"))
				addLab(file.getSection(sec));
			else if (sec.matches("ta\\d+"))
				addTA(file.getSection(sec));
			else if (sec.equals("constraint"))
				addConstraints(file.getSection(sec));
		}
		int grossNumSum = 0;
		for (TA t : TAs)
			grossNumSum+= t.numSec();
		if (grossNumSum != labs.size())
			throw new IllegalArgumentException(grossNumSum > labs.size() ? 
					"There are more requested number of sections than number of labs" 
					: "There are more labs than TAs that can fill them");
		if (minPrefSecs > labs.size())
			throw new IllegalArgumentException("Minimum preferred number of labs per TA " +
					"is greater than the number of labs.");
	}
	
	private void addLab(ArrayList<Entry> lab) {
		int secNum = Integer.MIN_VALUE;
		String startTime = "", endTime = "", day = "";
		for (Entry e : lab) {
			if (e.getKey().equals(LAB_SEC_IDENTIFIER)) 
				try {
					secNum = Integer.parseInt(e.getValue());
				} catch (Exception ex) {
					throw new IllegalArgumentException(e.getValue() + 
							" is an invalid lab section number.");
				}
			if (e.getKey().equals(STARTTIME_IDENTIFIER))
				startTime = e.getValue();
			if (e.getKey().equals(ENDTIME_IDENTIFIER))
				endTime = e.getValue();
			if (e.getKey().matches(DAY_REGEX))
				day = e.getValue();
		}
		if (secNum == Integer.MIN_VALUE || startTime.length() == 0 || endTime.length() == 0 || 
				day.length() == 0)
			throw new IllegalArgumentException("Not all of the parameters " +
					"(section, startTime, endTime, day) of a lab are specified.");
		Lab newLab = new Lab(secNum, startTime, endTime, day);
		if (labs.contains(newLab))
			throw new IllegalArgumentException("lab " + secNum + " appears more than once");
		if (!newLab.daysOfWeek().isOneDay())
			throw new IllegalArgumentException("labs can only meet once a week");
		
		labs.add(newLab);
	}
	
	private void addTA(ArrayList<Entry> ta) {
		String name = "", id = "";
		int numSec = Integer.MIN_VALUE;
		for (Entry e : ta) {
			if (e.getKey().equals(NUMSEC_IDENTIFIER)) 
				try {
					numSec = Integer.parseInt(e.getValue());
				} catch (Exception ex) {
					throw new IllegalArgumentException(e.getValue() + 
							" is an invalid number of sections for TA " + name);
				}
			if (e.getKey().equals(TA_NAME_IDENTIFIER))
				name = e.getValue();
			if (e.getKey().equals(ID_IDENTIFIER))
				id = e.getValue();
		}
		if (numSec == Integer.MIN_VALUE || name.length() == 0 || id.length() == 0)
			throw new IllegalArgumentException("Not all of the parameters " +
					"(numSec, name, id) of a TA are specified.");
		TA newTA = new TA(name, id, numSec);
		if (TAs.contains(newTA))
			throw new IllegalArgumentException("TA with ePantherID " + id + " already exists");
		
		TAs.add(newTA);
	}

	private void addConstraints(ArrayList<Entry> constraints) {
		for (Entry e : constraints) {
			if (e.getKey().equals(MIN_PREF_SEC_IDENTIFIER)) 
				try {
					minPrefSecs = Integer.parseInt(e.getValue());
				} catch (Exception ex) {
					throw new IllegalArgumentException(e.getValue() + 
							" is an invalid number of minimum preferred sections");
				}
		}
		if (minPrefSecs < 0)
			throw new IllegalArgumentException (minPrefSecs + " is an invalid " +
					"number of minimum preferred sections.");
	}

	public String toString() {
		String result = "";
		for (Lab l : labs)
			result = result + l + "\n";
		for (TA t : TAs)
			result = result + t + "\n";
		return result + "Minimum preferred number of section per TA: " + minPrefSecs;
	}
	public int minPrefSecs() {
		return minPrefSecs;
	}
	public File getFile() {
		return source;
	}
	public ArrayList<TA> getTAs() {
		return TAs;
	}
	public ArrayList<Lab> getLabs() {
		return labs;
	}
	public boolean containsTA(String name, String ePantherID) {
		TA dummy = new TA(name, ePantherID, TA.MAX_NUMSEC);
		int index = TAs.indexOf(dummy);
		if (index != -1 && dummy.name().equals(TAs.get(index)))
				return true;
		return false;
	}
	public Lab getLab(int labNum) {
		for (Lab l : labs)
			if (l.getSecNum() == labNum)
				return l;
		return null;
	}
}
