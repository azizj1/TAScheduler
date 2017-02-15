import java.util.Map;

public class Entry implements Map.Entry<String, String> {
	
	private String key, value;
	
	public Entry(String assignment) {
		String[] statement = assignment.split("\\s?=\\s?");
		if (statement.length != 2)
			throw new IllegalArgumentException("Illegal Assignment: " + assignment);
		if (!isValidIdentifier(statement[0]))
			throw new IllegalArgumentException("Invalid Identifer: " + statement[0]);
		key = statement[0].trim().toLowerCase();
		value = statement[1].trim();
	}
	public Entry(String identifier, String value) {
		if (!isValidIdentifier(identifier))
			throw new IllegalArgumentException("Invalid Identifer: " + identifier);
		this.key = identifier.trim().toLowerCase();
		this.value = value.trim();
	}
	
	public String getKey() {
		return key;
	}
	public String getValue() {
		return value;
	}
	public String setValue(String value) {
		String oldValue = this.value;
		this.value = value;
		return oldValue;
	}
	public String toString() {
		return key + "=" + value;
	}
	
	private boolean isValidIdentifier(String val) {
		if (!Character.isJavaIdentifierStart(val.charAt(0)))
			return false;
		for (int i = 1; i < val.length(); i++)
			if (!Character.isJavaIdentifierPart(val.charAt(i)))
				return false;
		return true;
	}
}
