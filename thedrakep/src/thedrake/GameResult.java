package thedrake;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.PrintWriter;

public enum GameResult implements JSONSerializable {
    VICTORY, DRAW, IN_PLAY, MAIN_MENU;

    private static ObjectProperty<GameResult> gameResult = new SimpleObjectProperty<>();


    public static void setGameResult (GameResult newGameResult){

        //because I have loop which is checking result
        if (gameResult.get() == newGameResult) {
            return;
        }
        gameResult.set(newGameResult);
    }
    public static ObjectProperty<GameResult> getGameResult() {
        return gameResult;
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("\"" + this.name() + "\"");
    }
}
