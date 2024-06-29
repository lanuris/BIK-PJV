package thedrake.ui.mainGameScreen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import thedrake.GameResult;
import thedrake.ui.MainDrakeController;

public class MainScreenController {

    MainDrakeController mainDrakeController;

    @FXML
    private Button exitButton;
    @FXML
    private Button localeMultiButton;

    public void setMainController(MainDrakeController mainDrakeController){
        this.mainDrakeController = mainDrakeController;
    }
    public void localeMultiAction(ActionEvent actionEvent) {
        GameResult.setGameResult(GameResult.IN_PLAY);
//        mainDrakeController.setMiddleView();
    }

    public void singlePlayerAction(ActionEvent actionEvent) {
    }

    public void onlineMultiAction(ActionEvent actionEvent) {

    }

    public void exitAction(ActionEvent actionEvent) {
        ((Stage) exitButton.getScene().getWindow()).close();
    }

}
