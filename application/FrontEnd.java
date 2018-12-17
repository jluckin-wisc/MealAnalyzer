/**
 * Filename:   FrontEnd.java
 * Project:    Final - GUI
 * Course:     cs400 
 * Authors:    Gavin Lefebvre, Patrick Sommer, Jonathan Luckin
 * Due Date:   2018-12-02
 * 
 *
 * Additional credits: none
 *
 * Bugs or other notes: none known
 * **/

package application;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class FrontEnd extends Application {

	static BorderPane mainPanes = new BorderPane();
	static BorderPane leftPanes = new BorderPane();
	static BorderPane rightPanes = new BorderPane();
	static Stage stage = new Stage();
	
	static  FileChooser fileChooser = new FileChooser();
	
	static int filterCnt = 0;
	
	static VBox filterPane;
	
	final static String TITLE_HEADER 		= "        Awesome Foodie Dietplan App!";
	final static String TITLE_LEFT 			= "Current Food List";
	final static String TITLE_RIGHT 		= "Current Menu";
	final static String TITLE_RESULTS		= "Analysis Results:";
	
	final static String BTN_EXIT			= "EXIT";
	
	final static String BTN_LOAD_FILE 		= "Load List\nfrom File";
	final static String BTN_ADD_ITEM 		= "Add Item\nto List";
	final static String BTN_SAVE_LIST		= "Save List\nto File";
	
	final static String BTN_NEW_ITEM		= "Add New Item";
	
	final static String BTN_APPLY_FLTRS		= "Apply Filters";
	final static String BTN_CLR_FLTRS		= "Clear Filters";
	
	final static String BTN_ADD_SEL			= ">>\nAdd\nSelected\nFood\nItem\n>>";
	
	final static String BTN_REM_SEL			= "Remove Selected Item";
	final static String BTN_CALC_MENU		= "Analyze Menu Items";
	
	final static String PROMPT_SEARCH		= "Search Items by name...";
	final static String PROMPT_ENTER_VAL	= "Enter Value...";

	final static String FTR_1				= "v0.1";
	final static String FTR_2				= "� 2018 Group Twelve";
	final static String FTR_3				= "Online Help";
	final static String FTR_4				= "Debug";
	

	final static String LBL_TXT_ASSEMBLE	= "Assemble your menu to learn its nutrition!";
	final static String TIP_ADD_FILTER		= "Click to add another filter!";
	
	final static String DEFAULT_NUTRIENT	= "Nutrient";
	final static String DEFAULT_COMP		= "Comparator";
	
	// ints to use in GUI
	final static int LIST_WIDTH				= 500;
	final static int ADD_ITEM_SPACING		= 20;
	
	private static FoodData foodData;
	//All food items
	private ObservableList<FoodItem> foodItems;
	//Food items that match filters
	private ObservableList<FoodItem> filteredFoodItemsList;
	
	private Label lblNutrition;
	
	private List<HBox> filterRows;
	private TextField txtSearchField = new TextField();
	
	//TODO Add this to GUI
	private int filteredFoodItemsListCount;
	private ListView<FoodItem> menuList;
	private ObservableList<FoodItem> names;
	private ListView<FoodItem> nameList;
	
	@Override
	public void start(Stage primaryStage) {
		foodData = new FoodData();
		foodItems = FXCollections.observableArrayList();
		filteredFoodItemsList = FXCollections.observableArrayList();
		int filteredFoodItemsListCount=0;
		menuList = new ListView<>();
		names = FXCollections.observableArrayList();
		
		// start with some data
		try {
			// open default file
			File file = new File("foodItems.csv");
			System.out.println(file.toString());
			
			foodData.loadFoodItems(file.toPath().toString());
			sortFoodData();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Scene scene = new Scene(mainPanes, 1600, 900);
		
		
		// Create Initial Children
		HBox headerPane = headerHBox(TITLE_HEADER);
		leftPanes = setupLeftPanes();
		rightPanes = getRightPane();
		
		mainPanes.setTop(headerPane);
		
		mainPanes.setLeft(leftPanes);
		
		mainPanes.setCenter(addCenter());
		
		mainPanes.setRight(rightPanes);
		
		mainPanes.setBottom(addHBox("Version, Copyright, Help, Debug, etc"));

		scene.getStylesheets().add("application/textStyles.css");

		primaryStage.setTitle(TITLE_HEADER);
		primaryStage.setScene(scene);
		primaryStage.show();
		stage = primaryStage;
	}

	private Button btnExit;
	
	private void sortFoodData() {
        foodItems.clear();
        foodItems.addAll(foodData.getAllFoodItems());
        
        //Comparator to sort the list in alpha order
        Comparator<FoodItem> alphaOrder = (f1, f2) -> {
        	return f1.getName().compareTo(f2.getName());};

        //Sorting list in alpha order
        FXCollections.sort(foodItems, alphaOrder);
	}
	
	// Header
	public HBox headerHBox(String appTitle) {
		
		HBox header = new HBox();
		header.setPadding(new Insets(5, 5, 5, 5));
		HBox title = new HBox();
		
		// Title
		Text headerText = new Text(appTitle);
		//title.setId("hbox");
		title.setPrefWidth(1400);
		headerText.setId("textstyle");
		title.setAlignment(Pos.CENTER);
		title.getChildren().add(headerText);
		
		// Exit Button
		btnExit = new Button(BTN_EXIT);
		btnExit.setId("tallbtn");
		btnExit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Platform.exit();
			}
			
		});
		
		header.getChildren().addAll(title, btnExit);
		header.getStyleClass().add("dark");
		return header;
	}
	
/* Left Border Pane */
	public BorderPane setupLeftPanes() {
		leftPanes = new BorderPane();
		
		leftPanes.setTop(leftTopPane());
		
		leftPanes.setLeft(vertPadding());
		leftPanes.setCenter(leftCenterList());
		leftPanes.setRight(vertPadding());
		
		leftPanes.setBottom(leftBottomPane());
		
		leftPanes.getStyleClass().add("pane");
		return leftPanes;
		
	}
	
	private Button btnLoadFile;
	private Button btnAddItem;
	private Button btnSaveList;
	
	// Left Top: Load/Add Buttons
	public HBox leftTopPane() {
		HBox buttons = new HBox(25);
		buttons.setPadding(new Insets(5, 5, 5, 5));
		Button btnLoadFile = new Button(BTN_LOAD_FILE);
		btnLoadFile.setTextAlignment(TextAlignment.CENTER);
		Button btnAddItem = new Button(BTN_ADD_ITEM);
		btnAddItem.setTextAlignment(TextAlignment.CENTER);
		Button btnSaveList = new Button(BTN_SAVE_LIST);
		btnSaveList.setTextAlignment(TextAlignment.CENTER);
		btnLoadFile.setId("tallbtn");
		btnAddItem.setId("tallbtn");
		btnSaveList.setId("tallbtn");
		buttons.setAlignment(Pos.TOP_CENTER);
		
		// Load File Handler
		btnLoadFile.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    File file = fileChooser.showOpenDialog(stage);
                    if (file != null) {
                    	foodData.loadFoodItems(file.getPath());
                      	sortFoodData();
                    }
                }
            });
        
        // Add Item Handler
		btnAddItem.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                    	VBox addItemVBox = new VBox(10);
                    	addItemVBox.setPrefWidth(500);
                    	addItemVBox.setAlignment(Pos.CENTER);
                    	Button btnNewItem = new Button(BTN_NEW_ITEM);
                    	
                    	Label label1 = new Label("       ID:");
                    	TextField tF1 = new TextField ();
                    	HBox hb1 = new HBox(ADD_ITEM_SPACING);
                    	hb1.setAlignment(Pos.CENTER);
                    	hb1.getChildren().addAll(label1, tF1);
                    	
                    	Label label2 = new Label("    Name:");
                    	TextField tF2 = new TextField ();
                    	HBox hb2 = new HBox(ADD_ITEM_SPACING);
                    	hb2.setAlignment(Pos.CENTER);
                    	hb2.getChildren().addAll(label2, tF2);
                    	
                    	Label label3 = new Label("Calories:");
                    	TextField tF3 = new TextField ();
                    	HBox hb3 = new HBox(ADD_ITEM_SPACING);
                    	hb3.setAlignment(Pos.CENTER);
                    	hb3.getChildren().addAll(label3, tF3);
                    
                    	Label label4 = new Label("   Carbs:");
                    	TextField tF4 = new TextField ();
                    	HBox hb4 = new HBox(ADD_ITEM_SPACING);
                    	hb4.setAlignment(Pos.CENTER);
                    	hb4.getChildren().addAll(label4, tF4);
                    	
                    	Label label5 = new Label("      Fat:");
                    	TextField tF5 = new TextField ();
                    	HBox hb5 = new HBox(ADD_ITEM_SPACING);
                    	hb5.setAlignment(Pos.CENTER);
                    	hb5.getChildren().addAll(label5, tF5);
                    	
                    	Label label6 = new Label(" Protein:");
                    	TextField tF6 = new TextField ();
                    	HBox hb6 = new HBox(ADD_ITEM_SPACING);
                    	hb6.setAlignment(Pos.CENTER);
                    	hb6.getChildren().addAll(label6, tF6);
                    	
                    	Label label7 = new Label("   Fiber:");
                    	TextField tF7 = new TextField ();
                    	HBox hb7 = new HBox(ADD_ITEM_SPACING);
                    	hb7.setAlignment(Pos.CENTER);
                    	hb7.getChildren().addAll(label7, tF7);
                    	
                		btnNewItem.setOnAction(
                            new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(final ActionEvent e) {
                                	ArrayList<TextField> values = new ArrayList<TextField>();
                                	values.add(tF1);
                                	values.add(tF2);
                                	values.add(tF3);
                                	values.add(tF4);
                                	values.add(tF5);
                                	values.add(tF6);
                                	values.add(tF7);
                                	
                                	//TextField[] values = { tF1, tF2, tF3, tF4, tF5, tF6, tF7 };
                                	boolean validNut = true;
                                	
                                	for(int j = 2; j < values.size(); j++) {
                                		TextField tRef = values.get(j);
                                		String foundVal = tRef.getText();
                                		if(!foundVal.matches("[0-9]*")) {
                                			System.out.println(foundVal);
                                			validNut = false;
                                		}
                                	}
                                	if(validNut && (values.get(0) != null) && (values.get(1) != null)) {
                                		// don't create a food item if there isn't an id or a name
                                		if (!(values.get(0).getText().equals("") || values.get(1).getText().equals(""))) {
                                			// create foodItem from data
	                                    	FoodItem addFood = new FoodItem(values.get(0).getText(), values.get(1).getText());
	                                    	int n = Nutrients.values().length;
	                                    	for(int i = 0; i < n; i++) {
	                                    		double dblVal = 0;
	                                    		try {
	                                    			dblVal = Double.parseDouble(values.get(i+2).getText());
	                                    		} catch (NumberFormatException numberException) {
	                                    			dblVal = 0;
	                                    		}
	                                    		addFood.addNutrient(Nutrients.values()[i].getName(), dblVal);
	                                    	}
	                                    	//System.out.println(addFood.getNutrients().toString());
	                                    	
	                                    	// add foodItem to the foodData object & sort list again
	                                    	foodData.addFoodItem(addFood);
	                                    	sortFoodData();
                                		}
                                		
                                    	// return to the view of the list
                                    	leftPanes.setCenter(leftCenterList());
                                	}
                                }
                            });
                		addItemVBox.getChildren().addAll(hb1, hb2, hb3, hb4, hb5, hb6, hb7, btnNewItem);
                		leftPanes.setCenter(addItemVBox);
                    }
                });
		
        // Save List Handler
		btnSaveList.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                	FileChooser fileChooser = new FileChooser();
                    File file = fileChooser.showSaveDialog(stage);
                    if (file != null) {
                    	// TODO: replace me
                        System.out.println("Saving File: " + file.getName());
                        foodData.saveFoodItems(file.getPath());
                    }
                }
            });
		
		buttons.getChildren().addAll(btnLoadFile, btnAddItem, btnSaveList);
		buttons.setId("hbox");
		buttons.getStyleClass().add("pane");
		return buttons;
	}
	
	
	// Left Center: Menu List
	public VBox leftCenterList() {
		VBox vbLeftCenter = new VBox(5);
		Text text = new Text(TITLE_LEFT);
		text.setId("textstyle");
		vbLeftCenter.setId("vbox");
		vbLeftCenter.getChildren().add(text);
		vbLeftCenter.setPrefWidth(500);

		nameList = new ListView<>();
		nameList.setItems(foodItems);
		vbLeftCenter.getChildren().add(nameList);
		
		HBox buttons = new HBox(25);
		Button btnClearFilters = new Button(BTN_CLR_FLTRS);
		Button btnApplyFilters = new Button(BTN_APPLY_FLTRS);
		
		// Clear Filters
		btnClearFilters.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                    	foodItems.setAll(foodData.getAllFoodItems());
                    	FXCollections.sort(foodItems, (f1, f2) -> {
                        	return f1.getName().compareTo(f2.getName()); });
                    	leftPanes.setBottom(leftBottomPane());
                    }
                });
		
        // Apply Filters
		btnApplyFilters.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				HashSet<FoodItem> foodItemsFilteredByText = new HashSet<FoodItem>();
				HashSet<FoodItem> foodItemsFilteredByNutrients = new HashSet<FoodItem>();
				HashSet<FoodItem> foodItemsFiltered = new HashSet<FoodItem>();
				String textFieldSearchName = txtSearchField.getText();
				List<String> rules = new ArrayList<String>();
				boolean filteredByNutrients = false;
				boolean filteredByText = false;
				
				if (!textFieldSearchName.equals("") && textFieldSearchName != null) {
					foodItemsFilteredByText.addAll(foodData.filterByName(textFieldSearchName));
					filteredByText = true;
				}
				
				for (HBox filterRow : filterRows) {
					String strNutrient = ((ChoiceBox<String>) filterRow.getChildren().get(1)).getValue();
					String strComparator = ((ChoiceBox<String>) filterRow.getChildren().get(2)).getValue();
					String strCompValue = ((TextField) filterRow.getChildren().get(3)).getText();
					
					strNutrient = Nutrients.getFromText(strNutrient);
					strComparator = Comparators.getFromText(strComparator);
					
					if (strNutrient != null && strComparator != null) {
						try {
							double compValue = Double.parseDouble(strCompValue);
							rules.add(strNutrient + " " + strComparator + " " + strCompValue);
						} catch (Exception e) {
							// only need the try/catch to make sure the string double is parse-able
						}
					}
				}
				
				if (rules.size() > 0) {
					foodItemsFilteredByNutrients.addAll(foodData.filterByNutrients(rules));
					filteredByNutrients = true;
					
					if (filteredByText) {
						foodItemsFilteredByText.retainAll(foodItemsFilteredByNutrients);
						foodItemsFiltered = foodItemsFilteredByText;
					}
					else {
						foodItemsFiltered = foodItemsFilteredByNutrients;
					}
				}
				else if (filteredByText) {
					foodItemsFiltered = foodItemsFilteredByText;
				}
				
				if (filteredByText || filteredByNutrients) {
					foodItems.setAll(foodItemsFiltered);
					FXCollections.sort(foodItems, (f1, f2) -> {
                    	return f1.getName().compareTo(f2.getName()); });
				}
				
				
				//TODO We need to add the size of filteredFoodItemsList
				//to the GUI
			}
        	
        });
        
		buttons.setAlignment(Pos.TOP_CENTER);
		buttons.getChildren().addAll(btnClearFilters, btnApplyFilters);
		buttons.setId("hbox");
		vbLeftCenter.getChildren().add(buttons);
		
		vbLeftCenter.getStyleClass().add("pane");
		return vbLeftCenter;
	}
	
	
	// Left Left & Right: Just Padding
	public VBox vertPadding() {
		VBox vbPad = new VBox();
		vbPad.setPrefWidth(100);
		vbPad.getStyleClass().add("pane");
		return vbPad;
	}
	
	// Left Bottom: Filters
	public ScrollPane leftBottomPane() {
		ScrollPane scrollPane = new ScrollPane();
		filterCnt = 0;
		filterPane = new VBox(10);
		filterPane.setPrefHeight(200);
		
		// Search by Item Name
		HBox searchBox = new HBox();
		searchBox.setId("searchpad");
		
		//final TextField searchField = new TextField();
		txtSearchField = new TextField();
		txtSearchField.setPromptText(PROMPT_SEARCH);
		txtSearchField.setPrefColumnCount(30);
		searchBox.getChildren().add(txtSearchField);
		
		filterRows = new ArrayList<HBox>();
		filterRows.add(getFilterRow());
		
		filterPane.getChildren().addAll(searchBox, filterRows.get(0));

		
		filterPane.getStyleClass().add("pane");
		scrollPane.getStyleClass().add("pane");
		scrollPane.setContent(filterPane);
		return scrollPane;
	}
	
	// Filter Row instance
	public HBox getFilterRow() {
		filterCnt += 1;
		
		HBox filters = new HBox(40);
		filters.setAlignment(Pos.CENTER);
		Button btn1 = new Button("+");
		btn1.setTooltip(new Tooltip("Click to add another filter!"));
		btn1.setId("plusbtn");
        
		// Add another row
        btn1.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                		HBox newRow = getFilterRow();
                		filterRows.add(newRow);
                		filterPane.getChildren().add(newRow);
                    }
                });
		
		filters.getChildren().add(btn1);
		
		
		ChoiceBox<String> cb1 = new ChoiceBox<String>();
		List<String> nutNames = Stream.of(Nutrients.values())
                .map(Nutrients::getName)
                .collect(Collectors.toList());
		nutNames.add(0, "Nutrient");
		cb1.getItems().setAll(nutNames);
		cb1.setValue("Nutrient");
		
		ChoiceBox<String> cb2 = new ChoiceBox<String>();
		List<String> comps = Stream.of(Comparators.values())
                .map(Comparators::getName)
                .collect(Collectors.toList());
		comps.add(0, "Comparator");
		cb2.getItems().setAll(comps);
		cb2.setValue("Comparator");
		
		TextField compValue = new TextField();
		compValue.setPromptText(PROMPT_ENTER_VAL);
		compValue.setPrefColumnCount(10);
		
		filters.getChildren().addAll(cb1, cb2, compValue);
		
		return filters;
	}
	
/* Center Border Pane (buttons to add/remove) */

    public AnchorPane addCenter() {
    	AnchorPane center = new AnchorPane();
    	
    	Button btn1 = new Button(BTN_ADD_SEL);
    	btn1.setWrapText(true);
    	btn1.setTextAlignment(TextAlignment.CENTER);
    	btn1.setId("supertallbtn");
    	
    	btn1.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                    	FoodItem selectedItem = nameList.getSelectionModel().getSelectedItem();
                    	FoodItem copy = new FoodItem(selectedItem.getID(), selectedItem.getName(), selectedItem.getNutrients());
                    	names.addAll(copy);
                    	
                    	//Adding the window
                    	menuList.setItems(names);
                    }
                });
    	AnchorPane.setTopAnchor(btn1, 160.0);
    	AnchorPane.setLeftAnchor(btn1, 30.0);
    	
    	center.getChildren().add(btn1);

    	return center;
    }

/* Right Border Pane */
    
    public BorderPane getRightPane() {
    	BorderPane rightPane = new BorderPane();
    	
    	// Header
    	HBox rightHeader = new HBox();
    	rightHeader.setId("hbox");
    	Text text = new Text(TITLE_RIGHT);
    	text.setId("textstyle");
    	rightHeader.getChildren().add(text);
    	rightPane.setTop(rightHeader);
    	
    
    	// Center
    	VBox rightCenter = new VBox(15);
    	rightCenter.setAlignment(Pos.TOP_CENTER);
    	rightCenter.setPrefWidth(500);
    	
    	
		menuList = new ListView<>();
		names = FXCollections.observableArrayList();
		menuList.setItems(names);
		rightCenter.getChildren().add(menuList);
		
		HBox btnBox = new HBox(50);
		btnBox.setAlignment(Pos.TOP_CENTER);
		
		
		Button btn1 = new Button(BTN_REM_SEL);
    	btn1.setWrapText(true);
    	btn1.setTextAlignment(TextAlignment.CENTER);
    	btn1.setId("tallbtn");
    	btn1.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                    	
                    	names.removeAll(menuList.getSelectionModel().
                    			getSelectedItem());
                    	                    	
                    	//Adding the window
                    	menuList.setItems(names);
                    	
                    	if (names.size() == 0) {
                    		setMenuNutritionLabel(lblNutrition, "Assemble your menu to learn its nutrition!", false);
                    	}
                    }
                });
    	
		Button btn2 = new Button(BTN_CALC_MENU);
    	btn2.setWrapText(true);
    	btn2.setTextAlignment(TextAlignment.CENTER);
    	btn2.setId("tallbtn");
		btn2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				double[] mealData = new double[Nutrients.values().length]; 
				for (FoodItem foodItem : names) {
					for (Nutrients n : Nutrients.values()) {
						mealData[n.ordinal()] += foodItem.getNutrientValue(n.getName());
					}
				}
				setMenuNutritionLabel(lblNutrition, getNutritionTextFromData(mealData), true);
			}
    		
    	});
		
    	btnBox.getChildren().addAll(btn1, btn2);
    	rightCenter.getChildren().add(btnBox);
    	rightPane.setCenter(rightCenter);
    	
    	// Padding
    	rightPane.setLeft(vertPadding());
    	rightPane.setRight(vertPadding());
    	
    	// Bottom
    	// Title
    	VBox bottomBox = new VBox();
    	bottomBox.setAlignment(Pos.TOP_CENTER);
    	bottomBox.setPrefHeight(220);
    	Text textFoot = new Text(TITLE_RESULTS);
    	textFoot.setId("textstyle");
    	bottomBox.getChildren().add(textFoot);
    	// Label
    	lblNutrition = new Label();
    	setMenuNutritionLabel(lblNutrition, "Assemble your menu to learn its nutrition!", false);

    	bottomBox.getChildren().add(lblNutrition);
    	
    	rightPane.setBottom(bottomBox);
    	
    	
    	return rightPane;
    }

	private void setMenuNutritionLabel(Label lblNutrition, String text, boolean nutritionData) {
    	lblNutrition.setText(text);
    	lblNutrition.setWrapText(true);
    	lblNutrition.getStyleClass().add("gray");
    	lblNutrition.setStyle("-fx-font-family:Consolas");
    	lblNutrition.setTextFill(Color.web("#ffffff"));
    	if (nutritionData) {
    		lblNutrition.setTextAlignment(TextAlignment.LEFT);
    	}
    	else {
    		lblNutrition.setTextAlignment(TextAlignment.CENTER);
    	}
    	lblNutrition.setPrefHeight(140);
    	lblNutrition.setPrefWidth(450);
    }
    
    private String getNutritionTextFromData(double[] nutrition) {
    	String result = "";
    	if (nutrition.length != Nutrients.values().length) {
    		return "Error - problem assembling analysis";
    	}
    	else {
    		ArrayList<String> strNutrition = new ArrayList<String>();
    		for (int i = 0; i < nutrition.length; i++) {
    			String strDouble = String.format("%.2f", nutrition[i]);
    			strNutrition.add(strDouble);
    		}
    		result += String.format("%-15s%15s", Nutrients.values()[0].toString(), strNutrition.get(0));
    		for (int i = 1; i < nutrition.length; i++) {
    			result += "\n" + String.format("%-15s%15s", Nutrients.values()[i].toString(), strNutrition.get(i));
    		}
    		return result;
    	}
    }

	public HBox addHBox(String str)
	{
		HBox hbox = new HBox(300);
		hbox.setAlignment(Pos.CENTER);
		Text text1 = new Text(FTR_1);
		text1.setId("textitalics");
		
		Text text2 = new Text(FTR_2);
		text2.setId("textitalics");
		
		Text text3 = new Text(FTR_3);
		text3.setId("textitalics");
		
		Text text4 = new Text(FTR_4);
		text4.setId("textitalics");
		
		hbox.getStyleClass().add("dark");
		hbox.getChildren().addAll(text1, text2, text3, text4);
		return hbox;
	}

	


	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		
		launch(args);
	}

}
