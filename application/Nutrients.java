package application;

public enum Nutrients {
	NUTRIENT ("Nutrient"),
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
}
