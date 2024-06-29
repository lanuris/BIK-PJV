package thedrake;

import java.io.PrintWriter;

//Tento typ určuje lícovou a rubovou stranu jednotky.
public enum TroopFace implements JSONSerializable {
    AVERS, REVERS;

    @Override
    public void toJSON(PrintWriter writer) {
        //writer.printf("\"face\":" + "\"" +  this.name() + "\",");
        writer.printf("\"" +  this.name() + "\"");
    }
}
