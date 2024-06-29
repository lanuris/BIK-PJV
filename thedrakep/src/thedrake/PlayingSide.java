package thedrake;

import java.io.PrintWriter;

// představuje barvu, za kterou hráč hraje.
public enum PlayingSide implements JSONSerializable {
    ORANGE, BLUE;

    @Override
    public void toJSON(PrintWriter writer) {
       // writer.printf("\"side\":" + "\"" +  this.name() + "\",");
        writer.printf("\"" +  this.name() + "\"");
    }
}
