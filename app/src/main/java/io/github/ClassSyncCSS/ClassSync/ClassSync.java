package io.github.ClassSyncCSS.ClassSync;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ClassSync extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a label
        Label label = new Label("Hello, JavaFX!");

        // Create a layout pane to hold the label
        StackPane root = new StackPane();
        root.getChildren().add(label);

        // Create a scene
        Scene scene = new Scene(root, 300, 200); // width, height

        // Set the scene on the primary stage
        primaryStage.setTitle("My First JavaFX App");
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
