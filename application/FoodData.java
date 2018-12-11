package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents the backend for managing all 
 * the operations associated with FoodItems
 * 
 * @author sapan (sapan@cs.wisc.edu)
 */
public class FoodData implements FoodDataADT<FoodItem> {
    
    // List of all the food items.
    private List<FoodItem> foodItemList;

    // Map of nutrients and their corresponding index
    private HashMap<String, BPTree<Double, FoodItem>> indexes;
    
    
    /**
     * Public constructor
     */
    public FoodData() {
        foodItemList = new ArrayList<FoodItem>();
        indexes = new HashMap<String, BPTree<Double, FoodItem>>();
        
        for (Nutrients n : Nutrients.values()) {
        	indexes.put(n.getName(), new BPTree<Double, FoodItem>(5));
        }
    }
    
    
    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#loadFoodItems(java.lang.String)
     */
    @Override
    public void loadFoodItems(String filePath) {
    	try {
			Files.lines(Paths.get(filePath)).forEach(thing -> {
				String[] parts = thing.split(",");
				if (parts.length >= 2) { // make sure there is at least a name and id
					String id = parts[0];
					String name = parts[1];
					FoodItem foodItem = new FoodItem(id, name);
					
					for (int i = 3; i < parts.length; i += 2) {
						String nutrient = parts[i - 1].toLowerCase();
						double value = Double.parseDouble(parts[i]);
						foodItem.addNutrient(nutrient, value);
					}
					addFoodItem(foodItem);
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByName(java.lang.String)
     */
    @Override
    public List<FoodItem> filterByName(String substring) {
        return foodItemList.stream().filter(foodItem -> foodItem.getName().toLowerCase().contains(substring)).collect(Collectors.toList());
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByNutrients(java.util.List)
     */
    @Override
    public List<FoodItem> filterByNutrients(List<String> rules) {
    	HashSet<FoodItem> queryResult = new HashSet<FoodItem>();
    	
        for (String rule : rules) {
        	queryResult = filterByRule(rule, queryResult);
        }
        
        return new ArrayList<FoodItem>(queryResult);
    }
    
    private HashSet<FoodItem> filterByRule(String rule, HashSet<FoodItem> foodItems) {
    	String[] ruleParts = rule.split(" ");
    	List<FoodItem> resultFromRule = indexes.get(ruleParts[0]).rangeSearch(Double.valueOf(ruleParts[1]), ruleParts[2]);
    	foodItems.addAll(resultFromRule);
    	return foodItems;
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#addFoodItem(skeleton.FoodItem)
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {
        foodItemList.add(foodItem);
        for (Nutrients n : Nutrients.values()) {
        	indexes.get(n.getName()).insert(foodItem.getNutrientValue(n.getName()), foodItem);
        }
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#getAllFoodItems()
     */
    @Override
    public List<FoodItem> getAllFoodItems() {
        return foodItemList;
    }


	@Override
	public void saveFoodItems(String filename) {
		// Sort food list by name ascending
		Collections.sort(foodItemList, new Comparator<FoodItem>() {
			
			@Override
			public int compare(FoodItem arg0, FoodItem arg1) {
				return arg0.getName().compareTo(arg1.getName());
			}
			
		});
		
		// Write out to a file
		PrintWriter pw;
		try {
			pw = new PrintWriter(filename);
			for (FoodItem foodItem : foodItemList) {
				pw.println(foodItem.toString());
			}
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
