package io.github.ClassSyncCSS.ClassSync.UI;

import io.github.ClassSyncCSS.ClassSync.UI.Controls.SearchableDropdown;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

public class ClassSync extends Application {

    @Override
    public void start(Stage primaryStage) {
        MenuBar fileMenuBar = new MenuBar();

        // Create a File Menu
        Menu fileMenu = new Menu("_File"); // Underscore for mnemonic
        fileMenu.setMnemonicParsing(true); // Enable mnemonic parsing

        // Create MenuItems for the File Menu
        MenuItem newItem = new MenuItem("_New");
        newItem.setMnemonicParsing(true);
        newItem.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));

        MenuItem openItem = new MenuItem("_Open...");
        openItem.setMnemonicParsing(true);
        openItem.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));

        MenuItem saveItem = new MenuItem("_Save");
        saveItem.setMnemonicParsing(true);
        saveItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));

        MenuItem saveAsItem = new MenuItem("Save _As..."); // Mnemonic on 'A'

        MenuItem exitItem = new MenuItem("E_xit"); // Mnemonic on 'x'
        exitItem.setMnemonicParsing(true);
        exitItem.setAccelerator(KeyCombination.keyCombination("Alt+F4")); // Common shortcut

        // Add MenuItems to the File Menu
        fileMenu.getItems().addAll(newItem, openItem, saveItem, saveAsItem, new SeparatorMenuItem(), exitItem);

        // Add the File Menu to the MenuBar
        fileMenuBar.getMenus().add(fileMenu);

        // Add event handlers to the MenuItems
        newItem.setOnAction(e -> System.out.println("New clicked"));
        openItem.setOnAction(e -> System.out.println("Open clicked"));
        saveItem.setOnAction(e -> System.out.println("Save clicked"));
        saveAsItem.setOnAction(e -> System.out.println("Save As clicked"));
        exitItem.setOnAction(e -> {
            System.out.println("Exit clicked");
            Platform.exit(); // Exit the application
        });

        ObservableList<String> professors_start = FXCollections.observableArrayList(
                "Option A",
                "Option B",
                "Option C",
                "Another Option",
                "Yet Another Option",
                "Different Option"
        );
        SearchableDropdown<String> teachers = new SearchableDropdown<String>();
        teachers.setItems(professors_start);

        ObservableList<String> group_start = FXCollections.observableArrayList(
                "Option A",
                "Option B",
                "Option C",
                "Another Option",
                "Yet Another Option",
                "Different Option"
        );
        SearchableDropdown<String> groups = new SearchableDropdown<String>();
        groups.setItems(group_start);

        HBox top_filter = new HBox();
        top_filter.getChildren().add(teachers);
        top_filter.getChildren().add(groups);



        // Create a label
        Label label = new Label("Orar AICI");

        // Create a layout pane to hold the label
        BorderPane root = new BorderPane();
        root.setTop(fileMenuBar);

        BorderPane content = new BorderPane();
        content.setTop(top_filter);
        content.setCenter(label);

        root.setCenter(content);



        // Create a scene
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, primaryScreenBounds.getWidth() * 0.75, primaryScreenBounds.getHeight() * 0.75);

        // Set the scene on the primary stage
        primaryStage.setTitle("ClassSync");
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
