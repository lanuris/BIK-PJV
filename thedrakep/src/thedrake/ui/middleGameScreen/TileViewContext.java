package thedrake.ui.middleGameScreen;

import thedrake.GameState;
import thedrake.Move;

public interface TileViewContext {

    //for TileView
    void tileViewSelected(TileView tileView);

    void executeMove(Move move);

    GameState getGameState();

    //for StackView
    void stackViewSelected(StackView stackView);



}
