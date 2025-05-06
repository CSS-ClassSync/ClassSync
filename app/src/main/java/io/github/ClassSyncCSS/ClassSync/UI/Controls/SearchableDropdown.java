package io.github.ClassSyncCSS.ClassSync.UI.Controls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class SearchableDropdown<T> extends ComboBox<T> {

    private ObservableList<T> originalItems;
    private FilteredList<T> filteredItems;
    private StringConverter<T> stringConverter; // To handle conversion for filtering

    // Constructor
    public SearchableDropdown() {
        this.setEditable(true);

        this.originalItems = FXCollections.observableArrayList();
        this.filteredItems = new FilteredList<>(originalItems, p -> true);
        this.setItems(filteredItems);

        // Default StringConverter (uses toString())
        this.stringConverter = new StringConverter<T>() {
            @Override
            public String toString(T object) {
                return (object == null) ? null : object.toString();
            }

            @Override
            public T fromString(String string) {
                // This is for converting the user's input back to an object,
                // which might not be needed for simple filtering.
                return null;
            }
        };
        super.setConverter(this.stringConverter); // Set the default converter

        // Add listener for filtering
        this.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            filteredItems.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String itemString = (item == null) ? "" : stringConverter.toString(item);
                String lowerCaseFilter = newValue.toLowerCase();

                return itemString.toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

//    public void setItems(ObservableList<T> value) {
//        this.originalItems = value;
//        this.filteredItems = new FilteredList<>(originalItems, p -> true);
//        super.setItems(filteredItems);
//        filteredItems.setPredicate(p -> true);
//    }

    // Override setStringConverter and update our internal converter
//    @Override
    public void setStringConverter(StringConverter<T> converter) {
        this.stringConverter = converter;
        super.setConverter(converter);
        // Re-apply the predicate to use the new converter
//        this.getEditor().textProperty().fireEvent(new javafx.event.ActionEvent()); // Trigger filtering
    }
}
