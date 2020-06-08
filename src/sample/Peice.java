package sample;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

/**
 * Parent Class for all Pieces
 */
public class Peice extends MovingObject {

    //FINAL WHITE COLORS
    protected static Color FINALWHITE = new Color(0,255,0);

    //FINAL BLACK COLORS
    protected static Color FINALBLACK = new Color(0,0,255);

    //BOOLEAN PIECE TYPE WHITE / BLACK
    private boolean isWhite;

    /**
     *
     * @param posX the top left corner of the object's x value
     * @param posY the top left corner y value
     * @param objWidth the width of the object
     * @param objHeight the height of the object
     * @param objHSpeed and sets defaultHSpeed as sets the current H speed and stores the original H speed as default for calling later
     * @param objVSpeed and sets defaultVSpeed sets the current V speed and stores the oringinal V speed as defualt for caling later
     * @param health
     * @param Image
     * @param objColour
     * @param isWhite tracks if the player is white or black
     */
    public Peice(
            int objWidth, int objHeight,
            int posX, int posY,
                 Color objColour, BufferedImage Image,
                 int objHSpeed, int objVSpeed,
            int health, boolean isWhite
    ) {
        super(objWidth, objHeight, posX, posY, objColour,
                Image, Image, Image, Image,
                objHSpeed, objVSpeed, health);

        this.isWhite = isWhite;
    }

    /**
     * Return a populated Board with the peices where they should be
     * @return
     */
    protected static ArrayList<Peice> newCheckerBoard(Map map){

        BufferedImage image_1 = null;

        ArrayList<Peice> result = new ArrayList<>();



        //WHITE
            image_1 = FileReader.imageGetter(
                    "Pawn_White.png");

        //First Row
        alternatingRow(0,0, Peice.FINALWHITE, 1,1,
                image_1, true
                , result, map);
        //Second Row
        alternatingRow(1,1, Peice.FINALWHITE, 1,1,
                image_1, true
                , result, map);

        alternatingRow(0,2, Peice.FINALWHITE, 1,1,
                image_1, true
                , result, map);


        //BLACK
        image_1 = FileReader.imageGetter(
                "Pawn_Black.png");

        //First Row
        alternatingRow(1,5, Peice.FINALBLACK, 1,1,
                image_1, false
                , result, map);

        //Second Row
        alternatingRow(0,6, Peice.FINALBLACK, 1,1,
                image_1, false
                , result, map);

        //Third Row

        alternatingRow(1,7, Peice.FINALBLACK, 1,1,
                image_1, false
                , result, map);

        return result;

    }

    /**
     * Reset the colour to the correct colour
     */
    protected void resetColor(){
        if(isWhite)super.setObjColour(Peice.FINALBLACK);
        else{
            super.setObjColour(Peice.FINALWHITE);
        }
    }

    /**
     *
     * @param row
     * @param c
     * @param HSpeed
     * @param VSpeed
     */
    private static void addRow(int row, Color c, int HSpeed, int VSpeed,
                               BufferedImage image,boolean isWhite, ArrayList<Peice> list, Map map){

        for(int i = map.getTileList().get(0).size(); i!= -1; i--){

            list.add(
                    new Peice(
                            1,1,i,row, c, image,  HSpeed, VSpeed,1, isWhite
                    )

            );

        }

    }


    /**
     *
     * @param row
     * @param c
     * @param HSpeed
     * @param VSpeed
     */
    private static void alternatingRow(int start,int row, Color c, int HSpeed, int VSpeed,
                               BufferedImage image,boolean isWhite, ArrayList<Peice> list, Map map){

        System.out.println("Start: "+start+" >= " + map.getTileList().get(0).size());

        for(int i = start; i< map.getTileList().get(0).size(); i+=2){

            System.out.println("Row: "+row+" column: "+i);

            list.add(
                    new Peice(
                            1,1,i,row, c, image,  HSpeed, VSpeed,1, isWhite
                    )

            );

        }

    }

    @Override
    /**This is where some magic happens
     * We use the super.drawobj but change the width, height, x & y
     * so that every time on draw we are calling the tile's width's / heights
     * to ensure proper actual drawing position but still using the actual X, y's to describe the column / row
     *
     * given a 2d Graphics graw the object in reference to viewX and viewY of the MapView
     *     * Eg: object 0,0 and the mapview 1,1
     *     * draw the object @ -1,-1
     *     * Additionally scale the object in comparision to the width and height of the map view to allow objects to appear
     *     * to get bigger / smaller as the zoom changes
     *      * @param gg - the Graphics 2D that the object is drawn on
     *      * @param maps - the Map that is used to draw the object relatively to the view X, Y positions
     *
     */
    protected void drawobj(Graphics2D gg, Map maps){

        //Save the values temporarily
        int x = this.getPosX();
        int y = this.getPosY();

        //Now update the width / height to the maps tiles width / height
        this.setObjWidth(maps.getTileWidth());
        this.setObjHeight(maps.getTileHeight());

        //now update the x & y's to the actual position
        this.setPosX(maps.getTileWidth()*x);
        this.setPosY(maps.getTileHeight()*y);



        if(super.getUp_Image()!=null){
            gg.drawImage(
                    super.getUp_Image(),

                    this.getPosX()
                    ,this.getPosY(),

                    getObjWidth(),
                    getObjHeight(),

                    null);

        }
        else{
            super.drawobj(gg,maps);
        }

        //and convert back to the column / row position
        this.setPosX(x);
        this.setPosY(y);
    }

    /**This is where some magic happens
     * We use the super.drawobj but change the width, height, x & y
     * so that every time on draw we are calling the tile's width's / heights
     * to ensure proper actual drawing position but still using the actual X, y's to describe the column / row
     *
     * given a 2d Graphics graw the object in reference to viewX and viewY of the MapView
     *     * Eg: object 0,0 and the mapview 1,1
     *     * draw the object @ -1,-1
     *     * Additionally scale the object in comparision to the width and height of the map view to allow objects to appear
     *     * to get bigger / smaller as the zoom changes
     *      * @param gg - the Graphics 2D that the object is drawn on
     *      * @param maps - the Map that is used to draw the object relatively to the view X, Y positions
     *
     */
    protected void highlighted_drawobj(Graphics2D gg, Map maps, Color highlight){

        this.setObjColour(highlight);

        //Save the values temporarily
        int x = this.getPosX();
        int y = this.getPosY();

        //Now update the width / height to the maps tiles width / height
        this.setObjWidth(maps.getTileWidth());
        this.setObjHeight(maps.getTileHeight());

        //now update the x & y's to the actual position
        this.setPosX(maps.getTileWidth()*x);
        this.setPosY(maps.getTileHeight()*y);

        super.drawobj(gg,maps);

        if(super.getUp_Image()!=null){
            gg.drawImage(
                    super.getUp_Image(),

                    this.getPosX()
                    ,this.getPosY(),

                    getObjWidth(),
                    getObjHeight(),

                    null);
        }
        //and convert back to the column / row position
        this.setPosX(x);
        this.setPosY(y);
    }

    /**
     * Basic for loop using the drawobj() to do the actual lifting
     * @param list an array of pc's that each call upon the drawobj function
     * given a 2d Graphics graw the object in reference to viewX and viewY of the MapView
     *     * Eg: object 0,0 and the mapview 1,1
     *     * draw the object @ -1,-1
     *     * Additionally scale the object in comparision to the width and height of the map view to allow objects to appear
     *     * to get bigger / smaller as the zoom changes
     *      * @param gg - the Graphics 2D that the object is drawn on
     *      * @param maps - the Map that is used to draw the object relatively to the view X, Y positions
     */
    protected static void drawArray(ArrayList<Peice> list, Graphics2D gg, Map maps){

        for(Peice item: list){
            item.drawobj(gg,maps);
        }

    }


    /**
     * Called when a movement is wanted but
     * we still need to confirm if it is a valid movement
     */
    protected void testMovement(MouseEvent e, Map maps){

        SolidObject obj = Map.CollidedTile(e.getX(),e.getY(),maps);

        this.setPosY(obj.getPosY()/maps.getTileHeight());
        this.setPosX(obj.getPosX()/maps.getTileWidth());

        this.setObjWidth(obj.getObjWidth());

        this.setObjHeight(obj.getObjHeight());

    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }
}
