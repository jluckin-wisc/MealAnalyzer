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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.application.Application;
import javafx.collections.FXCollections;
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
	
	@Override
	public void start(Stage primaryStage) {

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

	// Header
	public static HBox headerHBox(String appTitle) {
		
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
		Button btn1 = new Button(BTN_EXIT);
		btn1.setId("tallbtn");
		
		header.getChildren().addAll(title, btn1);
		header.getStyleClass().add("dark");
		return header;
	}
	
/* Left Border Pane */
	public static BorderPane setupLeftPanes() {
		leftPanes = new BorderPane();
		
		leftPanes.setTop(leftTopPane());
		
		leftPanes.setLeft(vertPadding());
		leftPanes.setCenter(leftCenterList());
		leftPanes.setRight(vertPadding());
		
		leftPanes.setBottom(leftBottomPane());
		
		leftPanes.getStyleClass().add("pane");
		return leftPanes;
		
	}
	// Left Top: Load/Add Buttons
	public static HBox leftTopPane() {
		HBox buttons = new HBox(25);
		buttons.setPadding(new Insets(5, 5, 5, 5));
		Button btn1 = new Button(BTN_LOAD_FILE);
		btn1.setTextAlignment(TextAlignment.CENTER);
		Button btn2 = new Button(BTN_ADD_ITEM);
		btn2.setTextAlignment(TextAlignment.CENTER);
		Button btn3 = new Button(BTN_SAVE_LIST);
		btn3.setTextAlignment(TextAlignment.CENTER);
		btn1.setId("tallbtn");
		btn2.setId("tallbtn");
		btn3.setId("tallbtn");
		buttons.setAlignment(Pos.TOP_CENTER);
		
		// Load File Handler
        btn1.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    File file = fileChooser.showOpenDialog(stage);
                    if (file != null) {
                    	// TODO: replace me
                        System.out.println("Opening File: " + file.getName());
                    }
                }
            });
        
        // Add Item Handler
        
        // Save List Handler
        btn3.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = fileChooser.showSaveDialog(stage);
                        if (file != null) {
                        	// TODO: replace me
                            System.out.println("Saving File: " + file.getName());
                        }
                    }
                });
		
		buttons.getChildren().addAll(btn1, btn2, btn3);
		buttons.setId("hbox");
		buttons.getStyleClass().add("pane");
		return buttons;
	}
	
	// Left Center: Menu List
	
	public static VBox leftCenterList() {
		VBox vbLeftCenter = new VBox(5);
		Text text = new Text(TITLE_LEFT);
		text.setId("textstyle");
		vbLeftCenter.setId("vbox");
		vbLeftCenter.getChildren().add(text);
		vbLeftCenter.setPrefWidth(500);

		ListView<String> nameList = new ListView<>();
		nameList.setItems(BackEnd.getTestData().sorted());
		vbLeftCenter.getChildren().add(nameList);
		
		HBox buttons = new HBox(25);
		Button btn1 = new Button(BTN_CLR_FLTRS);
		Button btn2 = new Button(BTN_APPLY_FLTRS);
		
		// Clear Filters
        btn1.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                    	// TODO: clear data structure?
                        leftPanes.setBottom(leftBottomPane());
                    }
                });
		
		buttons.setAlignment(Pos.TOP_CENTER);
		buttons.getChildren().addAll(btn1, btn2);
		buttons.setId("hbox");
		vbLeftCenter.getChildren().add(buttons);
		
		vbLeftCenter.getStyleClass().add("pane");
		return vbLeftCenter;
	}
	
	// Left Center: Add
	
//	public static VBox leftCenterAdd() {
//		VBox vbLeftCenter = new VBox(5);
//		Text text = new Text(TITLE_LEFT);
//		text.setId("textstyle");
//		vbLeftCenter.setId("vbox");
//		vbLeftCenter.getChildren().add(text);
//		vbLeftCenter.setPrefWidth(500);
//
//		ListView<String> nameList = new ListView<>();
//		nameList.setItems(BackEnd.getTestData().sorted());
//		vbLeftCenter.getChildren().add(nameList);
//		
//		HBox buttons = new HBox(25);
//		btnClear = new Button(BTN_CLR_FLTRS);
//		Button btn2 = new Button(BTN_APPLY_FLTRS);
//		
//		// Clear Filters
//        btnClear.setOnAction(
//                new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(final ActionEvent e) {
//                    	// TODO: replace me
//                        System.out.println("Clearing Filter");
//                        leftPanes.setBottom(leftBottomPane());
//                    }
//                });
//		buttons.setAlignment(Pos.TOP_CENTER);
//		buttons.getChildren().addAll(btnClear, btn2);
//		buttons.setId("hbox");
//		vbLeftCenter.getChildren().add(buttons);
//		
//		vbLeftCenter.getStyleClass().add("pane");
//		return vbLeftCenter;
//	}
	
	
	// Left Left & Right: Just Padding
	public static VBox vertPadding() {
		VBox vbPad = new VBox();
		vbPad.setPrefWidth(100);
		vbPad.getStyleClass().add("pane");
		return vbPad;
	}
	
	// Left Bottom: Filters
	public static VBox leftBottomPane() {
		filterCnt = 0;
		filterPane = new VBox(10);
		filterPane.setPrefHeight(200);
		
		// Search by Item Name
		HBox searchBox = new HBox();
		searchBox.setId("searchpad");
		
		final TextField searchField = new TextField();
		searchField.setPromptText(PROMPT_SEARCH);
		searchField.setPrefColumnCount(30);
		searchBox.getChildren().add(searchField);
		
		filterPane.getChildren().addAll(searchBox, getFilterRow());

		
		filterPane.getStyleClass().add("pane");
		return filterPane;
	}
	
	// Filter Row instance
	public static HBox getFilterRow() {
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
                    	if(filterCnt <= 2) {
                        	// TODO: check for valid values
                            filterPane.getChildren().add(getFilterRow());
                    	}
                    }
                });
		
		filters.getChildren().add(btn1);
		
		
		ChoiceBox<String> cb1 = new ChoiceBox<String>();
		List<String> nutNames = Stream.of(Nutrients.values())
                .map(Nutrients::getName)
                .collect(Collectors.toList());
		cb1.getItems().setAll(nutNames);
		cb1.setValue("Nutrient");
		
		ChoiceBox<String> cb2 = new ChoiceBox<String>();
		List<String> comps = Stream.of(Comparators.values())
                .map(Comparators::toString)
                .collect(Collectors.toList());
		cb2.getItems().setAll(comps);
		cb2.setValue("Comparator");
		
		TextField compValue = new TextField();
		compValue.setPromptText(PROMPT_ENTER_VAL);
		compValue.setPrefColumnCount(10);
		
		filters.getChildren().addAll(cb1, cb2, compValue);
		
		return filters;
	}
	
/* Center Border Pane (buttons to add/remove) */

    public static AnchorPane addCenter() {
    	AnchorPane center = new AnchorPane();
    	
    	Button btn1 = new Button(BTN_ADD_SEL);
    	btn1.setWrapText(true);
    	btn1.setTextAlignment(TextAlignment.CENTER);
    	btn1.setId("supertallbtn");
    	
    	AnchorPane.setTopAnchor(btn1, 160.0);
    	AnchorPane.setLeftAnchor(btn1, 30.0);
    	
    	center.getChildren().add(btn1);

    	return center;
    }

/* Right Border Pane */
    
    public static BorderPane getRightPane() {
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
    	
		ListView<String> menuList = new ListView<>();
		ObservableList<String> names = FXCollections.observableArrayList();
		menuList.setItems(names);
		rightCenter.getChildren().add(menuList);
		
		HBox btnBox = new HBox(50);
		btnBox.setAlignment(Pos.TOP_CENTER);
		
		Button btn1 = new Button(BTN_REM_SEL);
    	btn1.setWrapText(true);
    	btn1.setTextAlignment(TextAlignment.CENTER);
    	btn1.setId("tallbtn");
    	
		Button btn2 = new Button(BTN_CALC_MENU);
    	btn2.setWrapText(true);
    	btn2.setTextAlignment(TextAlignment.CENTER);
    	btn2.setId("tallbtn");
		
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
    	Label label = new Label("Assemble your menu to learn its nutrition!");
    	label.setWrapText(true);
    	label.getStyleClass().add("gray");
    	label.setFont(new Font("Arial Black", 18));
    	label.setTextFill(Color.web("#ffffff"));
    	label.setTextAlignment(TextAlignment.CENTER);
    	label.setPrefHeight(140);
    	label.setPrefWidth(450);


    	bottomBox.getChildren().add(label);
    	
    	rightPane.setBottom(bottomBox);
    	
    	
    	return rightPane;
    }

	public static HBox addHBox(String str)
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
