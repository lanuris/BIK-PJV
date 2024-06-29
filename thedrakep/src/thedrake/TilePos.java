package thedrake;

import java.io.PrintWriter;
import java.util.List;

//představuje pozici dlaždice na hracím plánu.
//Souřadnice dlaždice se interně reprezentují jako dvě celá čísla i (číslo sloupce) a j (číslo řádku).
public interface TilePos extends JSONSerializable {

    //představuje pozici, která se nachází mimo hrací desku.
    public static final TilePos OFF_BOARD = new TilePos() {

        @Override
        public void toJSON(PrintWriter writer) {
            writer.printf("\"" + toString() + "\"");
        }

        @Override
        public int i() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int j() {
            throw new UnsupportedOperationException();
        }

        @Override
        public char column() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int row() {
            throw new UnsupportedOperationException();
        }

        @Override
        public TilePos step(int columnStep, int rowStep) {
            throw new UnsupportedOperationException();
        }

        @Override
        public TilePos step(Offset2D step) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<TilePos> neighbours() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isNextTo(TilePos pos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public TilePos stepByPlayingSide(Offset2D dir, PlayingSide side) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equalsTo(int i, int j) {
            return false;
        }

        @Override
        public String toString() {
            return "off-board";
        }
    };

    public int i();

    public int j();

    public char column();

    public int row();

    public TilePos step(int columnStep, int rowStep);

    //vytvoří novou souřadnici posunutou ve směru zadaného offsetu
    public TilePos step(Offset2D step);

    public List<? extends TilePos> neighbours();

    public boolean isNextTo(TilePos pos);

    //posune tuto souřadnici podle barvy hráče. Pokud hraje modrý, funguje jako metoda step().
    //Pokud hraje oranžový, udělá posun s obrácenou y souřadnicí.
    public TilePos stepByPlayingSide(Offset2D dir, PlayingSide side);

    //Metoda isNextTo() vrátí true, pokud tato pozice sousedí se zadanou pozicí.
    public boolean equalsTo(int i, int j);
}
