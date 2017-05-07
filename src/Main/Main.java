package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Created by dimav on 10/6/2016.
 */
public class Main extends Application {

    public static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/Views/sample.fxml"));
        primaryStage.getIcons().add(new Image("/icon.png"));
        primaryStage.setTitle("Транслятор Врадій Дмитро TР-42");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> System.exit(0));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
