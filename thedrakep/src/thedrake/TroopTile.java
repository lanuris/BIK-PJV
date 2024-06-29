package thedrake;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Tato immutable třída bude představovat dlaždici, na které stojí jednotka.
// Tato dlaždice si pamatuje jednotku, která na ní stojí, barvu, za kterou jednotka hraje, a stranu,
// na kterou je jednotka otočena (rub nebo líc).
// Pozor tedy na to, že náš návrh funguje tak, že barvu a stranu si nepamatuje jednotka sama,
// ale až dlaždice, na které stojí. To nám v budoucnu ušetří hodně práce.
public final class TroopTile implements Tile, JSONSerializable{

    Troop troop;
    PlayingSide side;
    TroopFace face;

    public TroopTile(Troop troop, PlayingSide side, TroopFace face) {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    // Vrací barvu, za kterou hraje jednotka na této dlaždici
    public PlayingSide side(){
        return this.side;
    }

    // Vrací stranu, na kterou je jednotka otočena
    public TroopFace face(){
        return this.face;
    }

    // Jednotka, která stojí na této dlaždici
    public Troop troop(){
        return troop;
    }

    // Vrací False, protože na dlaždici s jednotkou se nedá vstoupit
    public boolean canStepOn(){
        return false;
    }

    // Vrací True
    public boolean hasTroop(){
        return true;
    }

    // Vytvoří novou dlaždici, s jednotkou otočenou na opačnou stranu
    // (z rubu na líc nebo z líce na rub)
    public TroopTile flipped(){
        if (face == TroopFace.AVERS){
            return new TroopTile(this.troop, this.side, TroopFace.REVERS);
        }
        return new TroopTile(this.troop, this.side, TroopFace.AVERS);
    }

    //aby implementovala metodu movesFrom tak,
    //že projde všechny akce na správné straně jednotky a sjednotí jimi vrácené tahy do jednoho seznamu.
    @Override
    public List<Move> movesFrom(BoardPos pos, GameState state) {

        List<Move> result = new ArrayList<>();
        List<TroopAction> actionList = troop.actions(this.face());

        for(TroopAction first : actionList){
            result.addAll(first.movesFrom(pos, this.side, state));
        }
        return result;
    }

    @Override
    public void toJSON(PrintWriter writer) {
        //"{\"troop\":\"Drake\",\"side\":\"ORANGE\",\"face\":\"AVERS\"}"
        writer.printf("{\"troop\":");
        this.troop.toJSON(writer);
        writer.printf(",\"side\":");
        this.side.toJSON(writer);
        writer.printf(",\"face\":");
        this.face.toJSON(writer);
        writer.printf("}");
    }
}
