package thedrake;

import java.io.PrintWriter;
import java.util.Optional;

//zabaluje celkový stav hry, tedy armády obou hráčů a výsledek hry,
//tedy jestli se ještě hraje nebo už někdo vyhrál, nebo nastala remíza.
public class GameState implements  JSONSerializable {
    private final Board board;
    private final PlayingSide sideOnTurn;
    private final Army blueArmy;
    private final Army orangeArmy;
    private final GameResult result;

    public GameState(
            Board board,
            Army blueArmy,
            Army orangeArmy) {
        this(board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
    }

    public GameState(
            Board board,
            Army blueArmy,
            Army orangeArmy,
            PlayingSide sideOnTurn,
            GameResult result) {
        this.board = board;
        this.sideOnTurn = sideOnTurn;
        this.blueArmy = blueArmy;
        this.orangeArmy = orangeArmy;
        this.result = result;
    }

    public Board board() {
        return board;
    }

    public PlayingSide sideOnTurn() {
        return sideOnTurn;
    }

    public GameResult result() {
        return result;
    }

    public Army army(PlayingSide side) {
        if (side == PlayingSide.BLUE) {
            return blueArmy;
        }

        return orangeArmy;
    }

    public Army armyOnTurn() {
        return army(sideOnTurn);
    }

    public Army armyNotOnTurn() {
        if (sideOnTurn == PlayingSide.BLUE)
            return orangeArmy;

        return blueArmy;
    }

    // Vrátí dlaždici, která se nachází na hrací desce na pozici pos.
    // Musí tedy zkontrolovat, jestli na této pozici není jednotka z
    // armády nějakého hráče a pokud ne, vrátí dlaždici z objektu board
    public Tile tileAt(TilePos pos) {

       if (pos != TilePos.OFF_BOARD &&
           this.blueArmy.boardTroops().at(pos).isEmpty() &&
           this.orangeArmy.boardTroops().at(pos).isEmpty()){
           return this.board.at(pos);
       }
       if (this.blueArmy.boardTroops().at(pos).isPresent()){
           return this.blueArmy.boardTroops().at(pos).get();
       }

       if (this.orangeArmy.boardTroops().at(pos).isPresent()){
            return this.orangeArmy.boardTroops().at(pos).get();
       }
       //prepsat
       return BoardTile.MOUNTAIN;
    }

    // Vrátí true, pokud je možné ze zadané pozice začít tah nějakou
    // jednotkou. Vrací false, pokud stav hry není IN_PLAY, pokud
    // na dané pozici nestojí žádná jednotka nebo pokud na pozici
    // stojí jednotka hráče, který zrovna není na tahu.
    // Při implementaci vemte v úvahu zahájení hry. Dokud nejsou
    // postaveny stráže, žádné pohyby jednotek po desce nejsou možné.
    private boolean canStepFrom(TilePos origin) {

        if (origin == TilePos.OFF_BOARD){
            return false;
        }

        return this.result == GameResult.IN_PLAY &&
                armyOnTurn().boardTroops().at(origin).isPresent() &&
                !armyOnTurn().boardTroops().isPlacingGuards() &&
                armyOnTurn().boardTroops().isLeaderPlaced();
    }

    // Vrátí true, pokud je možné na zadanou pozici dokončit tah nějakou
    // jednotkou. Vrací false, pokud stav hry není IN_PLAY nebo pokud
    // na zadanou dlaždici nelze vstoupit (metoda Tile.canStepOn).
    private boolean canStepTo(TilePos target) {

        if (target == TilePos.OFF_BOARD){
            return false;
        }

        return this.result == GameResult.IN_PLAY &&
                armyOnTurn().boardTroops().at(target).isEmpty() &&
                armyNotOnTurn().boardTroops().at(target).isEmpty() &&
                this.board.at(target).canStepOn();
    }

    // Vrátí true, pokud je možné na zadané pozici vyhodit soupeřovu jednotku.
    // Vrací false, pokud stav hry není IN_PLAY nebo pokud
    // na zadané pozici nestojí jednotka hráče, který zrovna není na tahu.
    private boolean canCaptureOn(TilePos target) {

        return this.result == GameResult.IN_PLAY &&
                armyNotOnTurn().boardTroops().at(target).isPresent();
    }

    public boolean canStep(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canStepTo(target);
    }

    public boolean canCapture(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canCaptureOn(target);
    }

    // Vrátí true, pokud je možné na zadanou pozici položit jednotku ze
    // zásobníku.. Vrací false, pokud stav hry není IN_PLAY, pokud je zásobník
    // armády, která je zrovna na tahu prázdný, pokud není možné na danou
    // dlaždici vstoupit. Při implementaci vemte v úvahu zahájení hry, kdy
    // se vkládání jednotek řídí jinými pravidly než ve střední hře.
    public boolean canPlaceFromStack(TilePos target) {

        if (target == TilePos.OFF_BOARD) {
            return false;
        }

        //zakladni podminka
        if (this.result == GameResult.IN_PLAY &&
                !armyOnTurn().stack().isEmpty() &&
                tileAt(target).canStepOn() &&
                armyOnTurn().boardTroops().at(target).isEmpty() &&
                armyNotOnTurn().boardTroops().at(target).isEmpty()) {

            //stavime vudce
            if (!armyOnTurn().boardTroops().isLeaderPlaced()) {

                if (sideOnTurn == PlayingSide.BLUE && target.row() == 1) {
                    return true;
                } else if (sideOnTurn == PlayingSide.ORANGE && target.row() == this.board.dimension()) {
                    return true;
                }
                return false;
            }

            //stavime straze
            if (armyOnTurn().boardTroops().isPlacingGuards()) {
                return target.neighbours().contains(armyOnTurn().boardTroops().leaderPosition());
            }

            //stredni hra
            boolean boolNeighbor = false;
            for (var first : target.neighbours()) {
                if (armyOnTurn().boardTroops().troopPositions().contains(first)) {
                    boolNeighbor = true;
                    break;
                }

            }
            return boolNeighbor;

        }
        return false;
    }

    public GameState stepOnly(BoardPos origin, BoardPos target) {
        if (canStep(origin, target))
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);

        throw new IllegalArgumentException();
    }

    public GameState stepAndCapture(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopStep(origin, target).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState captureOnly(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopFlip(origin).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState placeFromStack(BoardPos target) {
        if (canPlaceFromStack(target)) {
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().placeFromStack(target),
                    GameResult.IN_PLAY);
        }

        throw new IllegalArgumentException();
    }

    public GameState resign() {
        return createNewGameState(
                armyNotOnTurn(),
                armyOnTurn(),
                GameResult.VICTORY);
    }

    public GameState draw() {
        return createNewGameState(
                armyOnTurn(),
                armyNotOnTurn(),
                GameResult.DRAW);
    }

    private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
        if (armyOnTurn.side() == PlayingSide.BLUE) {
            return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
        }

        return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("{\"result\":");
        this.result.toJSON(writer);
        writer.printf(",\"board\":");
        this.board.toJSON(writer);
        writer.printf(",\"blueArmy\":");
        this.blueArmy.toJSON(writer);
        writer.printf(",\"orangeArmy\":");
        this.orangeArmy.toJSON(writer);
        writer.printf("}");

    }
}
