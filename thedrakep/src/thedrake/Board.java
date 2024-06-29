package thedrake;

//představující hrací plán.
//Ten sestává z dvourozměrného pole dlaždic typu BoardTile.
// Vzhledem k tomu, že design celé hry je immutable, i třída Board je immutable.
// Chceme-li tedy změnit nějakou dlaždici na jinou,
// musíme vytvořit novou kopii této třídy s touto novou dlaždicí na správném místě.
// Dlaždice budeme uchovávat ve dvourozměrném poli, které tedy bude potřeba vždy celé zkopírovat.
// Zde se může hodit metoda Object.clone(), která umí naklonovat jednorozměrné pole.

import java.io.PrintWriter;

//Šablona obsahuje jednu vnitřní třídu Board.TileAt.
//Je to pomůcka, pomocí které říkáme na jaké pozici je jaká dlaždice, protože dlaždice si samy svoji pozici nepamatují.
//Toto se nám hodí pro metodu withTiles viz níže.
public class Board implements JSONSerializable{

    private final int dimension;
    private final BoardTile[][] tiles;

    // Konstruktor. Vytvoří čtvercovou hrací desku zadaného rozměru, kde všechny dlaždice jsou prázdné, tedy BoardTile.EMPTY
    public Board(int dimension) {
        this.dimension = dimension;
        this.tiles = new BoardTile[dimension][dimension];
        for (int i = 0; i < dimension; i++){
            for (int j = 0; j < dimension; j++){
                this.tiles[i][j] = BoardTile.EMPTY;
            }
        }
    }

    // Rozměr hrací desky
    public int dimension() {
        return this.dimension;
    }

    // Vrací dlaždici na zvolené pozici.
    public BoardTile at(TilePos pos) {

        return this.tiles[pos.i()][pos.j()];
    }

    // Vytváří novou hrací desku s novými dlaždicemi. Všechny ostatní dlaždice zůstávají stejné
    public Board withTiles(TileAt... ats) {

        Board newBoard = new Board(this.dimension);

        for (int i= 0; i < dimension; i++){
           newBoard.tiles[i] = this.tiles[i].clone();
           //System.arraycopy(this.tiles[i], 0, newBoard.tiles[i], 0, dimension);
        }

        for ( TileAt first : ats){
            newBoard.tiles[first.pos.i()][first.pos.j()] = first.tile;
        }
        return newBoard;
    }

    // Vytvoří instanci PositionFactory pro výrobu pozic na tomto hracím plánu
    public PositionFactory positionFactory() {
        return new PositionFactory(this.dimension);
    }

//    public void printBoard(PrintWriter writer) {
//        for (BoardTile[] tile : tiles) {
//            for (BoardTile boardTile : tile) {
//                writer.printf(boardTile + ","); // Assuming BoardTile has overridden toString() method
//            }
//        }
//    }

//    public void printBoard(PrintWriter writer) {
//        for (int i = 0; i < tiles.length; i++) {
//            for (int j = 0; j < tiles[i].length; j++) {
//                tiles[i][j].toJSON(writer);
//                if (!(i == tiles.length - 1 && j == tiles[i].length - 1)) {
//                    writer.print(",");
//                }
//                else{
//                    writer.printf("]}");
//                }
//            }
//        }
//    }
//            public void printBoard(PrintWriter writer) {
//                for (int i = 0; i < this.dimension; ++i) {
//                    for (int j = 0; j < this.dimension; ++j) {
//                        tiles[j][i].toJSON(writer);
//                        }
//                    }
//            }


    public void printBoard(PrintWriter writer) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[j][i].toJSON(writer);
                if (j < tiles[i].length - 1) {
                    writer.print(",");
                }
            }
            if (i < tiles.length - 1) {
                writer.print(",");
            } else {
                writer.print("]}");
            }
        }
    }


    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("{\"dimension\":" + this.dimension + ",\"tiles\":[");
        printBoard(writer);
    }

    public static class TileAt {
        public final BoardPos pos;
        public final BoardTile tile;

        public TileAt(BoardPos pos, BoardTile tile) {
            this.pos = pos;
            this.tile = tile;
        }
    }
}

