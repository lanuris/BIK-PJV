package thedrake;

import java.util.List;

// To představuje dlaždici na hracím plánu. Každá dlaždice umí zodpovědět, zda se na ni dá vstoupit nebo zda obsahuje jednotku.
public interface Tile {

    // Vrací True, pokud je tato dlaždice volná a lze na ni vstoupit.
    public boolean canStepOn();

    // Vrací True, pokud tato dlaždice obsahuje jednotku
    public boolean hasTroop();

    //vrací všechny tahy, které lze z této dlaždice provést
    public List<Move> movesFrom(BoardPos pos, GameState state);
}
