package thedrake.ui.middleGameScreen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import thedrake.GameState;
import thedrake.PlayingSide;

public class MiddleGameView extends BorderPane {

    private BoardView boardView;

    private final VBox stackBox = new VBox();

    private final VBox infoBox = new VBox();


    public MiddleGameView (GameState gameState){

        this.boardView = new BoardView(gameState);

        stackBox.getChildren().add(new Label("Orange stack:"));
        stackBox.getChildren().add(boardView.getStackViewOrange());
        stackBox.getChildren().add(new Label("Blue stack:"));
        stackBox.getChildren().add(boardView.getStackViewBlue());
        stackBox.setAlignment(Pos.CENTER);
        stackBox.setSpacing(5);
        stackBox.setPadding(new Insets(10, 15, 0, 0));


        infoBox.getChildren().add(boardView.getPlayerOnTurn());
        infoBox.getChildren().add(boardView.getCatchedTroop());

        this.setBottom(infoBox);
        this.setLeft(boardView);
        this.setRight(stackBox);
    }


    public BoardView getBoardView() {
        return boardView;
    }

    public VBox getInfoBox() {
        return infoBox;
    }
}
