package application;

public enum Nutrients {
	CALORIES ("calories"),
	CARBS ("carbohydrate"),
	FAT ("fat"),
	PROTEIN ("protein"),
	FIBER ("fiber");
	
	private final String name;
	Nutrients(String name) {
		this.name = name;
	}
	String getName() { return name; }
}
