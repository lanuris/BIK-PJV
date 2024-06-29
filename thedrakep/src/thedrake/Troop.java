package thedrake;

import java.io.PrintWriter;
import java.util.List;

//třída Troop, která bude představovat bojovou jednotku v naší hře
public class Troop implements JSONSerializable {

    String name;
    Offset2D aversPivot;
    Offset2D reversPivot;

    List<TroopAction> aversActions;
    List<TroopAction> reversActions;

    // Hlavní konstruktor
    public Troop(String name, Offset2D aversPivot, Offset2D reversPivot, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this.name = name;
        this.aversPivot = aversPivot;
        this.reversPivot = reversPivot;
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }

    // Konstruktor, který nastavuje oba pivoty na stejnou hodnotu
    public Troop(String name, Offset2D pivot, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this.name = name;
        this.aversPivot = pivot;
        this.reversPivot = pivot;
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }

    // Konstruktor, který nastavuje oba pivoty na hodnotu [1, 1]
    public Troop(String name, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this.name = name;
        this.aversPivot = new Offset2D(1,1);
        this.reversPivot = new Offset2D(1,1);
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }

    public String name() {
        return name;
    }

    // Vrací pivot na zadané straně jednotky
    public Offset2D pivot(TroopFace face){

        if (TroopFace.AVERS == face){
           return this.aversPivot;
        }
        return this.reversPivot;
    }
    //Vrací seznam akcí pro zadanou stranu jednotky
    public List<TroopAction> actions(TroopFace face){

        if (TroopFace.AVERS == face){
           return this.aversActions;
        }
        return this.reversActions;
    }


    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("\"" + this.name + "\"");
    }
}
