import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Scheduler {
	private static Course cs201;
	
	/**
	 * Scheduler <output file> <TA or instructor file> ... <TA or instructor file>
	 * 
	 * @param args[0] is output file, where args[1]...args[args.length-1] are
	 * instructor/TA files. 
	 */
	public static void main(String[] args) {
		FileFormat[] files = new FileFormat[args.length-1];
		try {
			for (int i = 1; i < args.length; i++) {
				IniFile iniFile = new IniFile(new File(args[i]));
				
				if (iniFile.containsSection("info")) {
					//System.out.println("Unit testing TA file " + iniFile.getFile() + " ...");
					files[i-1] = new TAFile(iniFile);
				}
				else if (iniFile.containsSection("lab1")) {
					//System.out.println("Unit testing Instructor file " + iniFile.getFile() + " ...");
					files[i-1] = new InstructorFile(iniFile);
				}
				else 
					throw new IllegalArgumentException("Not a valid TA or instructor file");
			}
			cs201 = new Course("CompSci 201", files);
			cs201.createSchedule();
			System.out.println("PASS");
			outputToFile(new File(args[0]));
		}
		catch (Exception e) {
			System.out.println("FAIL: " + e.getMessage());
		}
	}
	public static void outputToFile(File f) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(f));
			for (TA t : cs201.getTAs()) {
				String list = t.getLabsEmployedTo().toString();
				writer.write(t.ePantherID() + ": " + list.substring(1, list.length()-1) + "\n");
			}
		
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		finally {
			try {
				if (writer != null)
					writer.close();

			} catch (Exception e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		}

	}
}
