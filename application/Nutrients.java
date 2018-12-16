package application;

public enum Nutrients {
	CALORIES ("Calories"),
	CARBS ("Carbohydrate"),
	FAT ("Fat"),
	PROTEIN ("Protein"),
	FIBER ("Fiber");
	
	private final String name;
	Nutrients(String name) {
		this.name = name;
	}
	String getName() { return name; }
	
	public static String getFromText(String text) {
		for (Nutrients n : Nutrients.values()) {
			if (n.getName().toUpperCase().equals(text.toUpperCase())) {
				return n.getName();
			}
		}
		return null;
	}
}
