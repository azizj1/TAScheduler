import java.util.ArrayList;
import java.util.HashMap;

public class TA {
	private String name, id;
	private int numSec;
	public final static int MIN_NUMSEC = 1, MAX_NUMSEC = 3;
	private TAFile myFile;
	/*
	 * availabilityPerPrecedent has three entries with keys 1, 2, and 3. 
	 * list of labs under key 1 contains all preferred labs.
	 * list of labs under key 2 contains all labs that don't conflict with classes
	 * and alibis
	 * labs that don't conflict with classes
	 */
	private HashMap<Integer, ArrayList<Lab>> availabilityPerPrecedent;
	private int precedentLvl;
	
	private ArrayList<Integer> labsEmployedTo;
	
	
	public TA(String name, String id, int numSec) {
		id = id.trim();
		if (!name.matches("[a-zA-Z ]+"))
			throw new IllegalArgumentException(name + " is an invalid TA name");
		if (!id.matches("[a-zA-Z][a-zA-Z0-9]*"))
			throw new IllegalArgumentException(id + " is an invalid ePantherID");
		if (numSec > MAX_NUMSEC || numSec < MIN_NUMSEC)
			throw new IllegalArgumentException("numSec = " + numSec + 
					". It must be between " + MIN_NUMSEC + " and " + MAX_NUMSEC + " inclusively.");
		this.name = properNoun(name);
		this.id = properNoun(id);
		this.numSec = numSec;
		availabilityPerPrecedent = new HashMap<Integer, ArrayList<Lab>>(4, 1);
		precedentLvl = 0;
		labsEmployedTo = new ArrayList<Integer>(numSec*2);
	}

	public static String properNoun(String s) {
		String[] segments = s.toLowerCase().split("\\s+");
		StringBuilder result = new StringBuilder();
		for (String a : segments) {
			if (a.length() != 0) {
				result.append(a.substring(0, 1).toUpperCase());
				result.append(a.substring(1));
				result.append(" ");
			}
		}
		return result.deleteCharAt(result.length()-1).toString();
	}
	
	public int numSec() {
		return numSec;
	}
	public String name() {
		return name;
	}
	public String ePantherID() {
		return id;
	}
	public boolean equals(Object obj) {
		return (obj instanceof TA) && ((TA) obj).id.equals(id);
	}
	public String toString() {
		return String.format("Name: %s; ePantherID: %s", name, id);
	}
	public boolean importTAFile(TAFile f) {
		if (!f.isFileOf(this))
			return false;
		myFile = f;
		return true;
	}
	public boolean isIntegratedWithFile() {
		return myFile != null;
	}
	public TAFile getTAFile() {
		return myFile;
	}
	
	public void setPreferredLabs(ArrayList<Lab> labs) {
		availabilityPerPrecedent.put(1, labs);
	}
	public void generateAvailability(ArrayList<Lab> labs) {
		if (!isIntegratedWithFile())
			throw new IllegalArgumentException(this + " is not linked to a TAfile.");
		//precedent = 1 is done during integrateTAs() in Course.
		ArrayList<Lab> prec2 = new ArrayList<Lab>(), prec3 = new ArrayList<Lab>();
		for (Lab lab : labs) {
			if (myFile.conflictsWithClasses(lab) == null)
				prec3.add(lab);
			if (myFile.conflictsWith(lab) == null)
				prec2.add(lab);
		}
		availabilityPerPrecedent.put(2, prec2);
		availabilityPerPrecedent.put(3, prec3);
		precedentLvl = 1;
	}
	public boolean isIntegratedWithLabs() {
		return precedentLvl > 0;
	}
	public ArrayList<Lab> getAvailability() {
		if (!isIntegratedWithLabs())
			throw new IllegalArgumentException("A list of available labs was not " +
					"assigned. Use the generateAvailability(List<Lab> labs) method.");
		//already found availability, so no need to find a list of labs that satisfy
		//numSec
		if (precedentLvl > 1 && precedentLvl <= 3)
			return availabilityPerPrecedent.get(precedentLvl);
		//returns the list of labs that satisfies the number of labs required.
		for (; precedentLvl <= 3; precedentLvl++)
			if (availabilityPerPrecedent.get(precedentLvl).size() >= numSec)
				return availabilityPerPrecedent.get(precedentLvl);
		return null;
	}
	public ArrayList<Lab> loosenAvailability() {
		if (!isIntegratedWithLabs())
			throw new IllegalArgumentException("A list of available labs was not " +
					"assigned. Used the generateAvailability(List<Lab> labs) method.");
		if (precedentLvl >= 3)
			return null;
		precedentLvl++;
		return availabilityPerPrecedent.get(precedentLvl);
	}
	
	public void assignLab(Lab l) {
		if (labsEmployedTo.size() == numSec)
			throw new IllegalArgumentException(this + " cannot be employed more than " + numSec + " labs.");
		labsEmployedTo.add(l.getSecNum());
	}
	public ArrayList<Integer> getLabsEmployedTo() {
		if (labsEmployedTo.size() == 0)
			throw new IllegalArgumentException("No labs have been employed to " + this);
		return labsEmployedTo;
	}
	
	
}
