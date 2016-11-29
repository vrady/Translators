package Main;

import Analyzers.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Arrays;


public class lab4 extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Group root = new Group();
        primaryStage.setTitle("Відношення передування Врадій Дмитро ТР-42");
        //getGrammar
        GrammarTree tree = new GrammarTree("C:\\Users\\dimav\\Translators\\src\\grammarTable.txt");
        String[][] grammarTable = tree.getPrecedenceTable();

        //createTable
        TableView table = new TableView();
        table.setEditable(true);

        for (int i = 0; i < grammarTable[0].length; i++) {
            TableColumn tableColumn = new TableColumn(grammarTable[0][i]);
            final int colNum = i;
            tableColumn.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[colNum]));
                }
            });
            table.getColumns().add(tableColumn);
        }
        table.getColumns().add(new TableColumn(grammarTable[0][67]));
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(grammarTable));
        data.remove(0);
        table.setItems(data);


        //createScene
        table.setPrefHeight(700);
        table.setPrefWidth(1366);

        root.getChildren().addAll(table);
        Scene scene = new Scene(root, 1366, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
