package thedrake.ui.middleGameScreen;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import thedrake.*;

public class CapturedView extends Pane {

    private Tile tile;
    private final TileBackgrounds tileBackground = new TileBackgrounds();
    private final TileViewContext tileViewContext;
    private final PlayingSide side;


    public CapturedView(TileViewContext context, PlayingSide side) {

        this.setPrefSize(100, 100);

//        setOnMouseClicked(e -> onClick());

        this.tileViewContext = context;
        this.side = side;

        update();
    }

    public void update() {
        if (!tileViewContext.getGameState().army(side).captured().isEmpty())
            setTile(new TroopTile(tileViewContext.getGameState().army(side).captured().get(0), side, TroopFace.AVERS));
        else
            setTile(BoardTile.EMPTY);

        this.setBackground(tileBackground.get(tile));
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }
}
