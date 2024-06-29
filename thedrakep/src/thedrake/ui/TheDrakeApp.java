package thedrake.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import thedrake.*;
import java.util.Random;

public class TheDrakeApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        MainDrakeController mainController = new MainDrakeController (primaryStage);

        mainController.loadAndSetMainGameScreen();
        mainController.loadEndGameScreen();
        mainController.setStateListener();

    }
}
