package Main;

import Analyzers.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Arrays;
import java.util.Objects;


public class lab4 extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Stage stage = new Stage();
        stage.setTitle("Конфлікти");
        Group root = new Group();
        Group root1 = new Group();
        primaryStage.setTitle("Відношення передування Врадій Дмитро ТР-42");

        GrammarTree tree = new GrammarTree("C:\\Users\\dimav\\Documents\\Translators\\src\\grammarTable.txt");
        String[][] grammarTable = tree.getPrecedenceTable();
        String errors = tree.getErrors();
        Text errorsText = new Text();
        if (!Objects.equals(errors, "")){
            errorsText.setText(errors);
        }else {
            errorsText.setText("Конфліктів не виявлено");
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(errorsText);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefWidth(400);
        scrollPane.setPrefHeight(200);

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
        table.getColumns().add(new TableColumn(grammarTable[0][grammarTable.length - 1]));
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(grammarTable));
        data.remove(0);
        table.setItems(data);


        //createScene
        table.setPrefHeight(700);
        table.setPrefWidth(1366);

        root.getChildren().addAll(table);
        root1.getChildren().addAll(scrollPane);
        Scene scene = new Scene(root, 1366, 700);
        Scene scene1 = new Scene(root1, 400,200);
        stage.setScene(scene1);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
