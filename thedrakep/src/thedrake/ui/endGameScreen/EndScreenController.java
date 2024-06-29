package thedrake.ui.endGameScreen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import thedrake.GameResult;

import java.net.URL;
import java.util.ResourceBundle;

public class EndScreenController implements Initializable {

    @FXML
    private Label winnerLabel;

    public void initialize(URL location, ResourceBundle resources) {}

    public void playAgainAction(ActionEvent actionEvent) {
        GameResult.setGameResult(GameResult.IN_PLAY);
    }
    public void backToMainMenuAction(ActionEvent actionEvent) {
        GameResult.setGameResult(GameResult.MAIN_MENU);
    }

    //we will call this function in TheDrakeApp
    @FXML
    public void setWinnerLabel(String text) {
        winnerLabel.setText(text);
    }
}
