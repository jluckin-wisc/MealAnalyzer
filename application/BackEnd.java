/**
 * Filename:   BackEnd.java
 * Project:    Final - GUI
 * Course:     cs400 
 * Authors:    Gavin Lefebvre, Patrick Sommer, Jonathan Luckin
 * Due Date:   
 * 
 *
 * Additional credits: none
 *
 * Bugs or other notes: none known
 * **/

package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BackEnd {
	
	public static ObservableList<String> getTestData() {
        // testing adding to our left pane object
        ObservableList<String> names = FXCollections.observableArrayList();
        names.add("Cheese");
        names.add("Bread");
        names.add("Soup");
        names.add("Pizza");
        return names;
	}
	
	public static ObservableList<String> getNutrients() {
		ObservableList<String> names = FXCollections.observableArrayList();
		names.add("Nutrients");
		names.add("Calories");
		names.add("Carbs");
		names.add("Fat");
		names.add("Protein");
		names.add("Fiber");
		return names;
	}
	
	public static ObservableList<String> getComparators() {
		ObservableList<String> names = FXCollections.observableArrayList();
		names.add("Comparators");
		names.add(">=");
		names.add("==");
		names.add("<=");
		return names;
	}
}

