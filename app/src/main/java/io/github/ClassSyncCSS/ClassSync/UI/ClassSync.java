package io.github.ClassSyncCSS.ClassSync.UI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ClassSync extends Application {

    @Override
    public void start(Stage primaryStage) {
        MenuBar menuBar = new MenuBar();

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
        menuBar.getMenus().add(fileMenu);

        // Add event handlers to the MenuItems
        newItem.setOnAction(e -> System.out.println("New clicked"));
        openItem.setOnAction(e -> System.out.println("Open clicked"));
        saveItem.setOnAction(e -> System.out.println("Save clicked"));
        saveAsItem.setOnAction(e -> System.out.println("Save As clicked"));
        exitItem.setOnAction(e -> {
            System.out.println("Exit clicked");
            Platform.exit(); // Exit the application
        });

        // Create a label
        Label label = new Label("Hello, JavaFX!");

        // Create a layout pane to hold the label
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.getChildren().add(label);


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
