package thedrake.ui.middleGameScreen;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import thedrake.*;


public class StackView extends Pane {

    private Tile tile;
    private final TileBackgrounds tileBackground = new TileBackgrounds();
    private final TileViewContext tileViewContext;
    private final PlayingSide side;
    private final Border selectBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));


    public StackView(TileViewContext context, PlayingSide side) {

        this.setPrefSize(100, 100);

        setOnMouseClicked(e -> onClick());

        this.tileViewContext = context;
        this.side = side;

        update();
    }

    public void update() {
        if (!tileViewContext.getGameState().army(side).stack().isEmpty())
            setTile(new TroopTile(tileViewContext.getGameState().army(side).stack().get(0), side, TroopFace.AVERS));
        else
            setTile(BoardTile.EMPTY);

        this.setBackground(tileBackground.get(tile));
    }
//
    public void select() {
        this.setBorder(selectBorder);
        tileViewContext.stackViewSelected(this);
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void unselect() {
        this.setBorder(null);
    }

    //only click on stack
    public void onClick() {
            select();
    }

    public PlayingSide side(){
        return this.side;
    }
}
