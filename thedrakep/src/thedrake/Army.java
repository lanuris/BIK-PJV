package thedrake;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

//Třída Army představuje celou armádu jednoho hráče, tedy nejen jednotky na hracím plánu,
//ale též zajaté jednotky a jednotky na zásobníku.
//Zajaté jednotky a zásobník jsou reprezentované jako seznamy.
//Jednotky na hracím plánu spravuje instance třídy BoardTroops.
public class Army implements JSONSerializable{
    private final BoardTroops boardTroops;
    private final List<Troop> stack;
    private final List<Troop> captured;

    public Army(PlayingSide playingSide, List<Troop> stack) {
        this(
                new BoardTroops(playingSide),
                stack,
                Collections.emptyList());
    }

    public Army(
            BoardTroops boardTroops,
            List<Troop> stack,
            List<Troop> captured) {
        this.boardTroops = boardTroops;
        this.stack = stack;
        this.captured = captured;
    }

    public PlayingSide side() {
        return boardTroops.playingSide();
    }

    public BoardTroops boardTroops() {
        return boardTroops;
    }

    public List<Troop> stack() {
        return stack;
    }

    public List<Troop> captured() {
        return captured;
    }

    public Army placeFromStack(BoardPos target) {
        if (target == TilePos.OFF_BOARD)
            throw new IllegalArgumentException();

        if (stack.isEmpty())
            throw new IllegalStateException();

        if (boardTroops.at(target).isPresent())
            throw new IllegalStateException();

        List<Troop> newStack = new ArrayList<Troop>(
                stack.subList(1, stack.size()));

        return new Army(
                boardTroops.placeTroop(stack.get(0), target),
                newStack,
                captured);
    }

    public Army troopStep(BoardPos origin, BoardPos target) {
        return new Army(boardTroops.troopStep(origin, target), stack, captured);
    }

    public Army troopFlip(BoardPos origin) {
        return new Army(boardTroops.troopFlip(origin), stack, captured);
    }

    public Army removeTroop(BoardPos target) {
        return new Army(boardTroops.removeTroop(target), stack, captured);
    }

    public Army capture(Troop troop) {
        List<Troop> newCaptured = new ArrayList<Troop>(captured);
        newCaptured.add(troop);

        return new Army(boardTroops, stack, newCaptured);
    }

    private void printList(PrintWriter writer, List<Troop> list) {
//        for (var i : list){
//            i.toJSON(writer);
//            if (i != list.getLast()) {
//                writer.printf(",");
//            }
//
//            }
        Iterator<Troop> iterator = list.iterator();
        while (iterator.hasNext()) {
            Troop troop = iterator.next();
            troop.toJSON(writer);
            if (iterator.hasNext()) {
                writer.print(",");
            }
        }
        writer.printf("]");
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("{\"boardTroops\":");
        this.boardTroops.toJSON(writer);
        //writer.printf("}");
        writer.printf(",\"stack\":[");

        printList(writer, this.stack);


        writer.printf(",\"captured\":[");
        printList(writer, this.captured);
        writer.printf("}");

    }

    public StringBuilder string() {
        StringBuilder c = new StringBuilder();
        Iterator<Troop> iterator = this.captured.iterator();
        while (iterator.hasNext()) {
            Troop troop = iterator.next();
            c.append(troop.name);
            if (iterator.hasNext()) {
                c.append(", ");
            }
        }
        return  c;
    }
}
