package thedrake;

import java.util.List;

// představuje akci jednotky (krok, posun, úder apod.).
// Každá instance této třídy obsahuje offset, který říká, kde se akce nachází vzhledem k pivotu jednotky.
// Všimněte si metody movesFrom. Ta obdrží pozici jednotky na hrací ploše, stranu za kterou jednotka hraje a stav hry.
// Na základě těchto udajů vygeneruje všechny možné tahy, které může jednotka pomocí této akce provést v zadaném stavu hry.
public abstract class TroopAction {
    private final Offset2D offset;

    protected TroopAction(int offsetX, int offsetY) {
        this(new Offset2D(offsetX, offsetY));
    }

    public TroopAction(Offset2D offset) {
        this.offset = offset;
    }

    public Offset2D offset() {
        return offset;
    }

    public abstract List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state);
}
