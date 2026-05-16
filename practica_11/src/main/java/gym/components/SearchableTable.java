package gym.components;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.function.BiPredicate;

/**
 * Componente personalizado: combina un TextField de búsqueda con un TableView.
 * Usa FilteredList + SortedList para filtrar en tiempo real sin modificar la
 * colección original.
 *
 * @param <T> tipo de objeto mostrado en la tabla
 */
public class SearchableTable<T> extends VBox {

    private final TextField  searchField;
    private final TableView<T> tableView;
    private FilteredList<T>  filteredData;

    public SearchableTable(String promptText) {
        super(8);
        setPadding(new Insets(0));
        getStyleClass().add("searchable-table-container");

        searchField = new TextField();
        searchField.setPromptText(promptText);
        searchField.getStyleClass().add("search-field");
        searchField.setPrefHeight(36);

        tableView = new TableView<>();
        tableView.setPlaceholder(new Label("No hay registros."));
        tableView.getStyleClass().add("data-table");
        VBox.setVgrow(tableView, Priority.ALWAYS);

        getChildren().addAll(searchField, tableView);
    }

    /**
     * Enlaza la tabla con los datos y un predicado de filtrado.
     * El predicado recibe el objeto y el texto de búsqueda en minúsculas.
     */
    public void setItems(ObservableList<T> items, BiPredicate<T, String> predicate) {
        filteredData = new FilteredList<>(items, p -> true);

        // Cada vez que cambia el texto se re-evalúa el predicado
        searchField.textProperty().addListener((obs, oldVal, newVal) ->
            filteredData.setPredicate(item -> {
                if (newVal == null || newVal.isBlank()) return true;
                return predicate.test(item, newVal.toLowerCase().trim());
            })
        );

        SortedList<T> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }

    public TableView<T>  getTableView()   { return tableView; }
    public TextField     getSearchField() { return searchField; }
    public T             getSelected()    { return tableView.getSelectionModel().getSelectedItem(); }

    @SuppressWarnings("unchecked")
    public javafx.collections.ObservableList<javafx.scene.control.TableColumn<T, ?>> getColumns() {
        return tableView.getColumns();
    }

    public void limpiarBusqueda() {
        searchField.clear();
    }
}
