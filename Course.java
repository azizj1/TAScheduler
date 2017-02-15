import java.util.ArrayList;

public class Course {
	
	private InstructorFile instFile;
	private String courseName;
	
	public Course(String name, FileFormat[] files) {
		instFile = null;
		courseName = name;
		ArrayList<TAFile> taFiles = new ArrayList<TAFile>();
		for (FileFormat f: files)
			addFile(f, taFiles);
		integrateTAs(taFiles);
	}
	
	public void addFile(FileFormat file, ArrayList<TAFile> taFiles) {
		if (file instanceof InstructorFile) {
			if (instFile != null)
				System.out.println("Replacing " + instFile.getFile() + " with " + file.getFile());
			instFile = (InstructorFile) file;
		}
		else if (file instanceof TAFile) {
			TAFile taFile = (TAFile) file;
			int indexOldFile = taFiles.indexOf(taFile);
			if (indexOldFile != -1) {
				System.out.println("Replacing " + taFiles.get(indexOldFile).getFile()
						+ " with " + file.getFile());
				taFiles.remove(indexOldFile);
			}
			taFiles.add(taFile);
			checkDuplicateIDs(taFile, taFiles);
		}
	}
	private void checkDuplicateIDs(TAFile taFile, ArrayList<TAFile> taFiles) {
		for (TAFile t : taFiles)
			if (taFile.ePantherID().equals(t.ePantherID()) && (!taFile.name().equals(t.name())))
				throw new IllegalArgumentException("Files " + taFile.getFile() + " and " +
						t.getFile() + " contain same ePantherID but different names.");
	}
	/**
	 * @Postcondition all TAs in the instructor file are integrated. TAFile
	 */
	private void integrateTAs(ArrayList<TAFile> taFiles) {
		if (instFile == null)
			throw new IllegalArgumentException("No instructor file found.");
		ArrayList<Integer> prefs;
		ArrayList<Lab> prefLabs;
		//all ta in the instructor file should have a ta file
		for (TA t : instFile.getTAs()) {
			TAFile tf = TAFileOf(t, taFiles);
			if (tf == null)
				throw new IllegalArgumentException("TA " + t + " specified in instructor file " +
				"does not have a TA file.");
			t.importTAFile(tf);
			taFiles.remove(tf);
			
			//check if TA t satisfies the min. preferred labs, that they all exist
			//in instructor's list of labs, and that the preferred labs do not interfere
			//with circumventions.
			prefs = t.getTAFile().prefs();
			if (instFile.minPrefSecs() > prefs.size())
				throw new IllegalArgumentException("TA " + t + " does not satisfy " +
						"the minimum preferred sections per TA.");
			
			prefLabs = new ArrayList<Lab>(prefs.size());
			for (Integer s : prefs) {
				Lab l = instFile.getLab(s);
				if (l == null)
					throw new IllegalArgumentException("Lab " + s + " was not specified " +
							"by instructor.");
				Rendezvous rTemp = t.getTAFile().conflictsWith(l);
				if (rTemp != null)
					throw new IllegalArgumentException("Lab " + s + " conflicts with " +
							rTemp);
				prefLabs.add(l);
			}
			t.setPreferredLabs(prefLabs);
		}
		//and all ta files should be listed in the instructor file.
		if (taFiles.size() != 0) {
			String files = "";
			for (TAFile tf : taFiles)
				files = files + tf.getFile() + "; ";
			throw new IllegalArgumentException("The following TAs in TA files are not " +
					"listed in the instructor file: " + files);
		}
	}
	private TAFile TAFileOf(TA t, ArrayList<TAFile> taFiles) {
		for (TAFile f : taFiles)
			if (f.isFileOf(t))
				return f;
		return null;
	}
	/**
	 * 
	 * @return an Arraylist of TAs after the TAfiles and instructor files have
	 * validated.
	 */
	public ArrayList<TA> getTAs() {
		return instFile.getTAs();
	}
	public ArrayList<Lab> getLabs() {
		return instFile.getLabs();
	}
	public String toString() {
		return courseName;
	}
	
	public void createSchedule() {
		for (TA t: getTAs()) {
			t.generateAvailability(getLabs());
			if (t.getAvailability() == null)
				throw new IllegalArgumentException("Because of conflictions with "
						+ "classes, " + t + " cannot be assigned " + t.numSec() + " labs.");
		}
			SubsidiaryTAMatrix matrixRep = new SubsidiaryTAMatrix(auxiliaryMatrix());
			int loosenedPerTAMin = 0, badTAIndex = -2;
			int[] loosened = new int[getTAs().size()];
			while ((badTAIndex = matrixRep.solve()) != -1) {
				FindNextTA nextTA = new FindNextTA(loosened, loosenedPerTAMin, badTAIndex);
				
				if (loosened[badTAIndex] > loosenedPerTAMin) {
					nextTA.find();
					badTAIndex = nextTA.index;
					loosenedPerTAMin = nextTA.min;
					loosened = nextTA.a;
				}
				else {
					loosened[badTAIndex]++;
					if (getTAs().get(badTAIndex).loosenAvailability() == null) {
						loosened[badTAIndex] = 2;
						nextTA = new FindNextTA(loosened, loosenedPerTAMin, badTAIndex);
						nextTA.find();
						badTAIndex = nextTA.index;
						loosenedPerTAMin = nextTA.min;
						loosened = nextTA.a;
					}					
				}
				if (badTAIndex == -2)
					throw new IllegalArgumentException("No solution.");
				matrixRep.setTARow(decodeToArray(badTAIndex), badTAIndex);
			}
			decipherMatrix(matrixRep.getSolution());
	}
	private void decipherMatrix(int[][] A) {
		for (int i = 0; i < A.length; i++)
			for (int j = 1; j < A[i].length; j++)
				if (A[i][j] == 1)
					getTAs().get(A[i][0]).assignLab(getLabs().get(j-1));
	}
	private class FindNextTA {
		int[] a;
		int min, index;
		FindNextTA(int[] array, int minimum, int indexOfTA) {
			a = new int[array.length];
			System.arraycopy(array, 0, a, 0, array.length); 
			min = minimum; index = indexOfTA;
		}
		void find() {
			int i = (index+1) % a.length;
			while (min < 2) {
				if (i == index) {
					min++;
					a[i]++;
					if (getTAs().get(i).loosenAvailability() != null)
						return;
					a[i] = 2;
				}
				else if (a[i] <= min && a[i] != 2) {
					if (getTAs().get(i).loosenAvailability() == null)
						a[i] = 2;
					else {
						a[i]++;
						index = i;
						return;
					}
				}
				i = (i+1) % a.length;
			}
			index = -2;
		}
	}
	private int[] decodeToArray(int taIndex) {
		if (taIndex < 0 || taIndex >= getTAs().size())
			throw new IllegalArgumentException(taIndex + " is an invalid index.");
		int[] m = new int[getLabs().size()+1];
		m[0] = taIndex;
		for (int j = 1; j < m.length; j++) {
			if (getTAs().get(taIndex).getAvailability().contains(getLabs().get(j-1)))
				m[j] = 1;
			else
				m[j] = 0;
		}
		return m;
	}
	
	private int[][] auxiliaryMatrix() {
		int[][] matrix = new int[getLabs().size()][getLabs().size()+1];
		if (getTAs().size() == 0) 
			return null; 
		int TAindex = 0, numOfRows = getTAs().get(TAindex).numSec();
		for (int i = 0; i < matrix.length; i++) {
			if (numOfRows == 0) {
				TAindex++;
				numOfRows = getTAs().get(TAindex).numSec();
			}
			matrix[i][0] = TAindex;
			numOfRows--;
			for (int j = 1; j < matrix[i].length; j++) {
				if (getTAs().get(matrix[i][0]).getAvailability().contains(getLabs().get(j-1)))
					matrix[i][j] = 1;
				else
					matrix[i][j] = 0;
			}
		}
		return matrix;
	}
}
