package thedrake.ui.middleGameScreen;

import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import thedrake.*;

public class BoardView extends GridPane implements TileViewContext {

    private GameState gameState;

    private ValidMoves validMoves;

    private TileView selectedTileView;

    private StackView stackViewBlue;

    private StackView stackViewOrange;

    private Label playerOnTurn = new Label();

    private Label catchedTroop = new Label();


    public BoardView(GameState gameState) {
        this.gameState = gameState;
        this.validMoves = new ValidMoves(gameState);

        PositionFactory positionFactory = gameState.board().positionFactory();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                BoardPos boardPos = positionFactory.pos(x, 3 - y);
                add(new TileView(boardPos, gameState.tileAt(boardPos), this), x, y);
            }
        }

        setHgap(5);
        setVgap(5);
        setPadding(new Insets(15));
        setAlignment(Pos.CENTER);

        this.stackViewBlue = new StackView(this, PlayingSide.BLUE);
        //this.stackViewBlue.setMaxSize(100, 100);
        this.stackViewOrange = new StackView(this, PlayingSide.ORANGE);
        //this.stackViewOrange.setMaxSize(100, 100);


        this.playerOnTurn.setText("Side on turn: " + gameState.sideOnTurn().toString());
        this.catchedTroop.setText("Captured: " + gameState.armyOnTurn().string());


    }



    @Override
    public void tileViewSelected(TileView tileView) {
        if (selectedTileView != null && selectedTileView != tileView)
            selectedTileView.unselect();

        stackViewBlue.unselect();
        stackViewOrange.unselect();

        selectedTileView = tileView;

        clearMoves();

        if (validMoves.allMoves().isEmpty()){
            GameResult.setGameResult(GameResult.VICTORY);
        }
        showMoves(validMoves.boardMoves(tileView.position()));
    }

    @Override
    public void executeMove(Move move) {

        if (selectedTileView != null) {
            selectedTileView.unselect();
            selectedTileView = null;
        }

        stackViewBlue.unselect();
        stackViewOrange.unselect();

        clearMoves();
        gameState = move.execute(gameState);
        validMoves = new ValidMoves(gameState);
        updateTiles();

    }

    private void updateTiles() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.setTile(gameState.tileAt(tileView.position()));
            tileView.update();
        }

        //update Side on turn and Captured text
        this.playerOnTurn.setText("Side on turn: " + gameState.sideOnTurn().toString());
        this.catchedTroop.setText("Captured: " + gameState.armyOnTurn().string());

        //update for stacks
        this.stackViewBlue.update();
        this.stackViewOrange.update();

        GameResult.setGameResult(gameState.result());
    }

    private void clearMoves() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.clearMove();
        }
    }

    private void showMoves(List<Move> moveList) {
        for (Move move : moveList)
            tileViewAt(move.target()).setMove(move);
    }

    private TileView tileViewAt(BoardPos target) {
        int index = (3 - target.j()) * 4 + target.i();
        return (TileView) getChildren().get(index);
    }


    public void stackViewSelected(StackView stackView) {
        if (selectedTileView != null) {
            selectedTileView.unselect();
        }

        if (stackView.side() == PlayingSide.BLUE){
            stackViewBlue = stackView;
            stackViewOrange.unselect();
        } else {
            stackViewOrange = stackView;
            stackViewBlue.unselect();
        }

        clearMoves();
        if (stackView.side() == gameState.sideOnTurn()) {

            if (gameState.armyOnTurn().boardTroops().isPlacingGuards() && validMoves.movesFromStack().isEmpty()){
                GameResult.setGameResult(GameResult.VICTORY);
            }
            showMoves(validMoves.movesFromStack());
        }
    }

    public GameState getGameState(){
        return gameState;
    }

    public StackView getStackViewBlue() {
        return stackViewBlue;
    }

    public StackView getStackViewOrange() {
        return stackViewOrange;
    }

    public Label getPlayerOnTurn() {
        return playerOnTurn;
    }

    public Label getCatchedTroop() {
        return catchedTroop;
    }

}
