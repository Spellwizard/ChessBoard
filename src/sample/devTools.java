package sample;

import java.awt.*;

public class devTools {

    protected static void drawScorebaord(
            Graphics2D gg, Piece john) {
        //  SCOREBOARD

        gg.setColor(Color.white);

        int posx = 60;

        int posy = 35;

        gg.setFont(new Font("TimesRoman", Font.PLAIN, 12));

        gg.drawString(

                "Coords: ("+john.getPosX() + ", " + john.getPosY()+")\t"+
                        "Width: "+john.getObjWidth()+" Height: " + (john.getObjHeight())
                , 50, (50+posy));

    }

}