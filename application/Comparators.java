package application;

public enum Comparators {
	COMPARATOR("Comparator"),
	LESSOREQUAL("<="),
	EQUAL("=="),
	GREATEROREQUAL(">=");
	
   // Member to hold the name
   private String string;

   // constructor to set the string
   Comparators(String name){string = name;}

   // the toString just returns the given name
   @Override
   public String toString() {
       return string;
   }
}

