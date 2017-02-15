import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

public class IniFile {
	private File iniFile;
	private TreeMap<String, ArrayList<Entry>> sections;
	
	public IniFile(File iniFile) {
		sections = new TreeMap<String, ArrayList<Entry>>();
		this.iniFile = iniFile;
		importContent();
	}
	
	private void importContent() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(iniFile));
			String strLine, header = "";
			while ((strLine = reader.readLine()) != null) {
				if (strLine.matches("\\[\\w+\\]")) {
					header = strLine.substring(1, strLine.length()-1).toLowerCase();
					if (sections.containsKey(header))
						throw new IllegalArgumentException("More than one \"" +
								header + "\" header");
					sections.put(header, new ArrayList<Entry>());
				}
				else if (!strLine.equals("")) {
					sections.get(header).add(new Entry(strLine));
				}
			}
		
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		finally {
			try {
				if (reader != null)
					reader.close();

			} catch (Exception e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		}
	}
	
	public String toString() {
		String result = "";
		for (String a : sections.keySet()) {
			result = result + "header: " + a + "\n";
			for (Entry e : sections.get(a))
				result = result + "\t" + e + "\n";

		}
		return result;
	}
	
	public ArrayList<Entry> getSection(String section) {
		return sections.get(section);
	}
	
	public Set<String> sectionSet() {
		return sections.keySet();
	}
	public boolean containsSection(String section) {
		return sections.containsKey(section);
	}
	public File getFile() {
		return iniFile;
	}
}