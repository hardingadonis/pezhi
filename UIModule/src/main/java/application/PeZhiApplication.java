package application;

import controller.MainController;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;


public class PeZhiApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
//        setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());

        Parent root = new FXMLLoader(MainController.class.getResource("MainController.fxml")).load();

        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.setTitle("PeZhi");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}