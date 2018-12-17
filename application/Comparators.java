package application;

public enum Comparators {
	LESS_THAN_OR_EQUALS ("<="),
	EQUALS ("=="),
	GREATER_THAN_OR_EQUALS (">=");
	
	private final String name;
	Comparators(String name) {
		this.name = name;
	}
	String getName() { return name; }
	
	public static String getFromText(String text) {
		for (Comparators n : Comparators.values()) {
			if (n.getName().toUpperCase().equals(text.toUpperCase())) {
				return n.getName();
			}
		}
		return null;
	}
}

