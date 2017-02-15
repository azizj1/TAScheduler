import java.io.File;
import java.util.ArrayList;
public class TAFile implements FileFormat {
	
	private ArrayList<Integer> prefs;
	private ArrayList<Rendezvous> circumventions;
	private String name, id;
	private File source;
	
	public TAFile(IniFile file) {
		prefs = new ArrayList<Integer>();
		circumventions = new ArrayList<Rendezvous>();
		source = file.getFile();
		add(file);
	}
	
	public void add(IniFile file) {
		for (String sec : file.sectionSet()) {
			if (sec.matches("class\\d+") || sec.matches("avoid\\d+"))
				addRendezvous(file.getSection(sec), sec);
			if (sec.equals("pref"))
				addPrefs(file.getSection(sec));
			if (sec.equals("info"))
				addInfo(file.getSection(sec));
		}
	}
	
	private void addInfo(ArrayList<Entry> entries) {
		String name = "", id = "";
		for (Entry e : entries) {
			if (e.getKey().equals(TA_NAME_IDENTIFIER))
				name = e.getValue();
			if (e.getKey().equals(ID_IDENTIFIER))
				id = e.getValue();
		}
		if (name.length() == 0 || id.length() == 0)
			throw new IllegalArgumentException("Not all of the parameters " + 
					"(name, id) of info are specified.");
		TA dummy = new TA(name, id, TA.MIN_NUMSEC);
		this.name = dummy.name();
		this.id = dummy.ePantherID();
	}
	
	private void addRendezvous(ArrayList<Entry> entries, String header) {
		String reason = "", startTime = "", endTime = "", days = "";
		Rendezvous r;
		for (Entry e : entries) {
			if (e.getKey().equals(STARTTIME_IDENTIFIER))
				startTime = e.getValue();
			if (e.getKey().equals(ENDTIME_IDENTIFIER))
				endTime = e.getValue();
			if (e.getKey().matches(DAY_REGEX))
				days = e.getValue();
			if (e.getKey().equals(REASON_IDENTIFIER))
				reason = e.getValue();
		}
		if (startTime.length() == 0 || endTime.length() == 0 || days.length() == 0)
			throw new IllegalArgumentException("Not all of the parameters " +
					"(startTime, endTime, day) of a class are specified.");
		if (header.matches("avoid\\d+")) {
			if (reason.length() == 0)
				throw new IllegalArgumentException("Not all of the parameters " +
				"(startTime, endTime, day, reason) of a circumvention are specified.");
			r = new Alibi(startTime, endTime, days, reason);  
		}
		else
			r = new Rendezvous(startTime, endTime, days);
		Rendezvous rTemp = conflictsWith(r);
		if (rTemp != null)
			throw new IllegalArgumentException(r + " conflicts with " + rTemp);
		
		circumventions.add(r);
	}
	
	private void addPrefs(ArrayList<Entry> entries) {
		int prefSec = 0;
		for (Entry e : entries)
			if (e.getKey().matches(PREF_REGEX)) {
				try {
					prefSec = Integer.parseInt(e.getValue());
				} catch (Exception ex) {
					throw new IllegalArgumentException(e.getValue() + 
					" is an invalid lab section number.");
				}
				if (!Lab.isValidSecNum(prefSec))
					throw new IllegalArgumentException(prefSec + " is not between 801-820");
				if (prefs.contains(prefSec))
					throw new IllegalArgumentException("preference " + prefSec +
							" appears more than once.");
				
				prefs.add(prefSec);
			}
	}
	
	public Rendezvous conflictsWith(Rendezvous r) {
		for (Rendezvous r1 : circumventions)
			if (r.interferesWith(r1))
				return r1;
		return null;
	}
	public Rendezvous conflictsWithClasses(Rendezvous r) {
		for (Rendezvous r1 : circumventions)
			if (!(r1 instanceof Alibi) && r.interferesWith(r1))
				return r1;
		return null;
	}
	
	public boolean equals(Object obj) {
		return (obj instanceof TAFile) && ((TAFile) obj).name.equals(name) &&
			((TAFile) obj).id.equals(id);
	}
	public String toString() {
		String result = String.format("Name: %s; ePantherID: %s%nClasses: ", name, id);
		for (Rendezvous c : circumventions)
			if (!(c instanceof Alibi))
				result = result + c + "; ";
		result = result + "\nAvoid: ";
		for (Rendezvous a : circumventions)
			if (a instanceof Alibi)
				result = result + a + "; ";
		result = result + "\nPreferences: ";
		for (Integer s : prefs)
			result = result + s + ", ";
		return result;
	}
	public File getFile() {
		return source;
	}
	public String name() {
		return name;
	}
	public String ePantherID() {
		return id;
	}
	public ArrayList<Integer> prefs() {
		return prefs;
	}
	public boolean isFileOf(TA t) {
		return name.equals(t.name()) && id.equals(t.ePantherID());
	}

}
