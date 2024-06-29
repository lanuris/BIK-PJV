package thedrake;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

//představuje dlaždice, které neobsahují jednotky. To jsou prázdné dlaždice nebo hory.
public interface BoardTile extends Tile, JSONSerializable {
    BoardTile EMPTY = new BoardTile() {

        @Override
        public void toJSON(PrintWriter writer) {
            writer.printf("\"" + toString() + "\"");
        }

        @Override
        public boolean canStepOn() {
            return true;
        }

        @Override
        public boolean hasTroop() {
            return false;
        }

        @Override
        public List<Move> movesFrom(BoardPos pos, GameState state) {
            return Collections.emptyList();
        }

        @Override
        public String toString() {
            return "empty";
        }


    };

    BoardTile MOUNTAIN = new BoardTile() {
        @Override
        public void toJSON(PrintWriter writer) {
            writer.printf("\"" + toString() + "\"");
        }

        @Override
        public boolean canStepOn() {
            return false;
        }

        @Override
        public boolean hasTroop() {
            return false;
        }

        @Override
        public List<Move> movesFrom(BoardPos pos, GameState state) {
            return Collections.emptyList();
        }

        @Override
        public String toString() {
            return "mountain";
        }
    };
}
