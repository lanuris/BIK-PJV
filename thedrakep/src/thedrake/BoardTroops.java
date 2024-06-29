
package thedrake;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

//se stará o jednotky nacházející se na hracím plánu.
//Stav hry si udržuje instanci této třídy pro každého hráče.
//Tedy jedna instance této třídy drží jednotky pouze jedné strany (modré nebo oranžové).
public class BoardTroops implements JSONSerializable {
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;

    public BoardTroops(PlayingSide playingSide) {
        this.playingSide = playingSide;
        this.troopMap = Collections.emptyMap();
        this.leaderPosition = TilePos.OFF_BOARD;
        this.guards = 0;
    }

    public BoardTroops(
            PlayingSide playingSide,
            Map<BoardPos, TroopTile> troopMap,
            TilePos leaderPosition,
            int guards) {

        this.playingSide = playingSide;
        this.troopMap = troopMap;
        this.leaderPosition = leaderPosition;
        this.guards = guards;
    }


    //Vrací dlaždici na zadané pozici, nebo Optional.empty(), pokud na této pozici žádné dlaždice není.
    public Optional<TroopTile> at(TilePos pos) {

        return Optional.ofNullable(troopMap.get(pos));
    }

    //Vrací barvu hráče
    public PlayingSide playingSide() {
        return this.playingSide;
    }

    //Vrací aktální pozici vůdce. Pokud vůdce ještě není nasazen, vrací TilePos.OFF_BOARD.
    public TilePos leaderPosition() {
        return this.leaderPosition;
    }

    //Vrací počet nasazených stráží. Stráží je vždy buď 0, 1 nebo 2.
    public int guards() {
        return this.guards;
    }

    //Vrací True pokud je vůdce již nasazen, jinak False.
    public boolean isLeaderPlaced() {
        return this.leaderPosition != TilePos.OFF_BOARD;
    }

    //Vrací True pokud se zrovna nasazují stráže. Stráže se nasazují, pokud je již nasazen vůdce.
    //Vůdce je vždy první jednotka nasazená do hry, druhá a třetí jsou stráže.
    //Jakmile je nasazena třetí jednotka, fáze nasazování stráží končí.
    public boolean isPlacingGuards() {
        if (this.leaderPosition == TilePos.OFF_BOARD){
            return false;
        }
        return this.guards < 2;
    }

    //Vrací množinu všech pozic, které jsou obsazené nějakou dlaždicí s jednotkou.
    public Set<BoardPos> troopPositions() {
       Set<BoardPos> positions = new HashSet<>();

//        for (Map.Entry<BoardPos, TroopTile> first : this.troopMap.entrySet()){
//            positions.add(first.getKey());
//        }
        this.troopMap.forEach((key, value) -> {
                positions.add(key);
            });
        return positions;
    }

    //Vrací novou instanci BoardTroops s novou dlaždicí TroopTile na pozici target
    //obsahující jednotku troop lícovou stranou nahoru.
    // Tato metoda vyhazuje výjimku IllegalArgumentException, pokud je již zadaná pozice obsazena jinou dlaždicí.
    //Pokud pomocí této metody stavíme úplně první jednotku, bere se tato jednotka jako vůdce a je potřeba nastavit pozici leaderPosition.
    //Tímto hra přechází do fáze stavění stráží.
    //Pokud pomocí této metody stavíme druhou a třetí jednotku, jsme ve fázi stavění stráží.
    // Tato fáze konči ve chvíli, kdy jsou obě stráže postaveny.
    public BoardTroops placeTroop(Troop troop, BoardPos target) {

        if (this.troopMap.get(target) != null){
            throw new IllegalArgumentException();
        }

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(this.troopMap);
        newTroops.put(target, new TroopTile(troop, this.playingSide, TroopFace.AVERS));
        //this.troopMap.put(target, new TroopTile(troop, this.playingSide, TroopFace.AVERS));

        if (!isLeaderPlaced()){
            return new BoardTroops(this.playingSide, newTroops, target, this.guards);
        }

        if (isPlacingGuards()) {
            if (this.guards == 0) {
                return new BoardTroops(this.playingSide, newTroops, this.leaderPosition, 1);
            }
            else {
                return new BoardTroops(this.playingSide, newTroops, this.leaderPosition, 2);
            }
        }
        return new BoardTroops(this.playingSide, newTroops, this.leaderPosition, this.guards);
    }

    //Vrací novou instanci BoardTroops s dlaždicí TroopTile na pozici origin posunutou na pozici target a otočenou na opačnou stranu.
    //Tato metoda vyhazuje výjimku IllegalStateException pokud jsme ve stavu stavění vůdce, nebo stavění stráží,
    //neboť v těchto fázích nelze ješte s dlaždicemi pohybovat.
    //Metoda dále vyhazuje výjimku IllegalArgumentException pokud je pozice origin prázdná nebo pozice target již obsazená.
    //Pozor, že pokud pomocí této metody pohybujeme s vůdce, je třeba aktualizovat jeho pozici.
    public BoardTroops troopStep(BoardPos origin, BoardPos target) {

        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (at(origin).isEmpty() || at(target).isPresent() ){
            throw new IllegalArgumentException();
        }

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(this.troopMap);
        newTroops.put(target, this.troopMap.get(origin).flipped());
        newTroops.remove(origin);
        //this.troopMap.put(target, this.troopMap.get(origin).flipped());
        //this.troopMap.remove(origin);

        if (origin.equals(this.leaderPosition)){
            return new BoardTroops(this.playingSide, newTroops, target, this.guards);
        }
        return new BoardTroops(this.playingSide, newTroops, this.leaderPosition, this.guards);
    }

    //Vrací novou instanci BoardTroops s novou dlaždicí TroopTile na pozici origin otočenou na obrácenou stranu.
    //Tato metoda je již implementována pro vaši inspiraci.
    //Podobně jako metoda troopStep vyhazuje výjimku IllegalStateException,
    //pokud jsme ve stavu stavění vůdce, nebo stavění střáží.
    //Dále vyhazuje výjimku IllegalArgumentException pokud je pozice origin prázdná.
    public BoardTroops troopFlip(BoardPos origin) {

        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (!at(origin).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }

    //Vrací novou instanci BoardTroops s odstraněnou dlaždicí z pozice target
    //Podobně jako metoda troopStep vyhazuje výjimku IllegalStateException,
    //pokud jsme ve stavu stavění vůdce, nebo stavění střáží.
    //Dále vyhazuje výjimku IllegalArgumentException pokud je pozice target prázdná.
    //Pozor, pokud pomocí této metody odstraňujeme vůdce, je třeba jeho pozici nastavit zpět na TilePos.OFF_BOARD.
    public BoardTroops removeTroop(BoardPos target) {

        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot remove troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot remove troops before guards are placed.");
        }

        if (at(target).isEmpty())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        newTroops.remove(target);

        if (this.leaderPosition.equals(target)){
            return new BoardTroops(this.playingSide, newTroops, TilePos.OFF_BOARD, this.guards);
        }
        return new BoardTroops(this.playingSide, newTroops, this.leaderPosition, this.guards);
    }

    private void printSortedTroopTiles(Map<BoardPos, TroopTile> troopMap, PrintWriter writer) {

        // Step 1: Extract the keys and Sort the keys
        List<BoardPos> sortedKeys = troopMap.keySet().stream()
                .sorted(Comparator.comparing(BoardPos::toString))
                .toList();

        for (BoardPos key : sortedKeys) {
            TroopTile troopTile = troopMap.get(key);
            key.toJSON(writer);
            writer.printf(":");
            troopTile.toJSON(writer);

            //two options
            //PrintWriter printWriter = key != sortedKeys.getLast() ? writer.printf(",") : writer.printf("}");
            if (key != sortedKeys.get(sortedKeys.size()-1)) {
                writer.printf(",");
            }
        }
        writer.printf("}");
    }



    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("{\"side\":");
        this.playingSide.toJSON(writer);
        writer.printf(",\"leaderPosition\":");
        this.leaderPosition.toJSON(writer);
        writer.printf(",\"guards\":" + this.guards);
        writer.printf(",\"troopMap\":{");
        printSortedTroopTiles(this.troopMap, writer);
        writer.printf("}");

    }
}
