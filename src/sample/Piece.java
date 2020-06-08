package sample;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

/**
 * Parent Class for all Pieces
 */
public class Piece extends MovingObject {

    //FINAL WHITE COLORS
    protected static Color FINALWHITE = new Color(0,255,0);

    //FINAL BLACK COLORS
    protected static Color FINALBLACK = new Color(0,0,255);

    //BOOLEAN PIECE TYPE WHITE / BLACK
    private boolean isWhite;

    //The Control Movement Variables
    //Basically control which way and how much a piece can move

    //DIAGONAL MOVEMENT
    private int diagonal_move_range;

    //is Horsey 'Knight' - LEAVE ALL OTHER MOVEMENT VALUES BLANK IF THIS IS TRUE
    private boolean isKnight;

    //Only moveTowards opposite side
    private boolean onlyTowardsEnemy;

    /**
     * @param objWidth the width of the object
     * @param objHeight the height of the object
     * @param posX the top left corner of the object's x value
     * @param posY the top left corner y value
     * @param objColour
     * @param Image
     * @param objHSpeed and sets defaultHSpeed as sets the current H speed and stores the original H speed as default for calling later
     * @param objVSpeed and sets defaultVSpeed sets the current V speed and stores the oringinal V speed as defualt for caling later
     * @param health
     * @param isWhite tracks if the player is white or black
     * @param diagonal_move_range
     * @param isKnight
     * @param onlyTowardsEnemy
     */
    public Piece(
            int objWidth, int objHeight,
            int posX, int posY,
            Color objColour, BufferedImage Image,
            int objHSpeed, int objVSpeed,
            int health,
            boolean isWhite,
            int diagonal_move_range,
                 boolean isKnight, boolean onlyTowardsEnemy
    ) {
        super(objWidth, objHeight, posX, posY, objColour,
                Image, Image, Image, Image,
                objHSpeed, objVSpeed, health);

        this.isWhite = isWhite;
        this.diagonal_move_range = diagonal_move_range;
        this.isKnight = isKnight;
        this.onlyTowardsEnemy = onlyTowardsEnemy;
    }

    /**
     * Return a populated Board with the peices where they should be
     * @return
     */
    protected static ArrayList<Piece> newCheckerBoard(Map map){

        BufferedImage image_1 = null;

        ArrayList<Piece> result = new ArrayList<>();

        /*
         * @param diagonal_move_range
         * @param isKnight
         * @param onlyTowardsEnemy
         */

        int DX_Range =1;
        boolean isHorsey = false;
        boolean onward = true;


        //WHITE
            image_1 = FileReader.imageGetter(
                    "Pawn_White.png");

        //First Row
        alternatingRow(0,0, Piece.FINALBLACK, 0,0,
                image_1, false
                , result, map, DX_Range, isHorsey, onward);
        //Second Row
        alternatingRow(1,1, Piece.FINALBLACK, 0,0,
                image_1, false
                , result, map, DX_Range, isHorsey, onward);

        alternatingRow(0,2, Piece.FINALBLACK, 0,0,
                image_1, false
                , result, map, DX_Range, isHorsey, onward);


        //BLACK
        image_1 = FileReader.imageGetter(
                "Pawn_Black.png");

        //First Row
        alternatingRow(1,5, Piece.FINALWHITE, 0,0,
                image_1, true
                , result, map, DX_Range, isHorsey, onward);

        //Second Row
        alternatingRow(0,6, Piece.FINALWHITE, 0,0,
                image_1, true
                , result, map, DX_Range, isHorsey, onward);

        //Third Row

        alternatingRow(1,7, Piece.FINALWHITE, 0,0,
                image_1, true
                , result, map, DX_Range, isHorsey, onward);

        return result;

    }



    /**
     * Return a populated Board with the peices where they should be
     * @return
     */
    protected static ArrayList<Piece> newChessBoard(Map map){

        BufferedImage image_1 = null;

        ArrayList<Piece> result = new ArrayList<>();

        /*
         * @param diagonal_move_range
         * @param isKnight
         * @param onlyTowardsEnemy
         */

        int DX_Range =1;
        boolean isHorsey = false;
        boolean onward = true;


        //WHITE
        image_1 = FileReader.imageGetter(
                "Pawn_White.png");

        //PAWN ROW
        addRow(1, Piece.FINALBLACK, 0,1,
                image_1, false
                , result, map, DX_Range, isHorsey, onward);






        //BLACK
        image_1 = FileReader.imageGetter(
                "Pawn_Black.png");

        //PAWN ROW
        addRow(6, Piece.FINALWHITE, 0,1,
                image_1, true
                , result, map, DX_Range, isHorsey, onward);


        return result;

    }



    /**
     * Reset the colour to the correct colour
     */
    protected void resetColor(){
        if(isWhite)super.setObjColour(Piece.FINALBLACK);
        else{
            super.setObjColour(Piece.FINALWHITE);
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
                               BufferedImage image,boolean isWhite, ArrayList<Piece> list, Map map,
    int diagonal_Range, boolean isHorsey, boolean onward
    ){

        for(int i = map.getTileList().get(0).size(); i!= -1; i--){

            list.add(
                    new Piece(
                            1,1,i,row, c, image,  HSpeed, VSpeed,1, isWhite
                            ,diagonal_Range,isHorsey, onward

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
    private static void addChessBackRow(int row, Color c, int HSpeed, int VSpeed,
                               BufferedImage RookImage, BufferedImage KnightImage,
                                        BufferedImage BishopImage, BufferedImage QueenImage,
                                        BufferedImage KingImage,
                                        boolean isWhite, ArrayList<Piece> list, Map map
    ){

        //ROOKS

        list.add(new Piece(1,1,0,row,c,RookImage,
                10,10,1, isWhite,
                0, false, false));

        list.add(new Piece(1,1,7,row,c,RookImage,
                10,10,1, isWhite,
                0, false, false));

        //KNIGHTS

        list.add(new Piece(1,1,1,row,c,KnightImage,
                10,10,1, isWhite,
                0, false, false));

        list.add(new Piece(1,1,6,row,c,KnightImage,
                0,0,1, isWhite,
                0, true, false));

        //BISHOPS

        list.add(new Piece(1,1,2,row,c,BishopImage,
                0,0,1, isWhite,
                10, false, false));

        list.add(new Piece(1,1,5,row,c,BishopImage,
                0,0,1, isWhite,
                10, false, false));

        //QUEEN

        list.add(new Piece(1,1,3,row,c,QueenImage,
                10,10,1, isWhite,
                10, false, false));

        //KING
        list.add(new Piece(1,1,4,row,c,KingImage,
                1,1,1, isWhite,
                1, false, false));



    }


    /**
     *
     * @param row
     * @param c
     * @param HSpeed
     * @param VSpeed
     */
    private static void alternatingRow(int start,int row, Color c, int HSpeed, int VSpeed,
                               BufferedImage image,boolean isWhite,
                                       ArrayList<Piece> list, Map map,
                                       int diagonal_Range, boolean isHorsey, boolean onward){


        for(int i = start; i< map.getTileList().get(0).size(); i+=2){

            list.add(
                    new Piece(
                            1,1,i,row, c, image,  HSpeed, VSpeed,1, isWhite
                            ,diagonal_Range,isHorsey, onward
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
    protected static void drawArray(ArrayList<Piece> list, Graphics2D gg, Map maps){

        for(Piece item: list){
            item.drawobj(gg,maps);
        }

    }


    /**
     * Called when a movement is wanted but
     * we still need to econfirm if it is a valid movement
     */
    protected void testMovement(MouseEvent e, Map maps){

        System.out.println("** New Move");

        boolean legalMove = true; //inoccent before guilty yada yada

        SolidObject obj = Map.CollidedTile(e.getX(),e.getY(),maps);

        //HorizontalMovment
        if(this.getObjHSpeed() != 0 && legalMove){

            legalMove =
                    LEGALMOVE.Lateral_HorizontalMovement_Ranged(obj, this, maps, this.getObjHSpeed());

            System.out.println("LEGALMOVE.Lateral_HorizontalMovement_Ranged: "+legalMove);
        }

        //VERTICAL
        if(this.getObjVSpeed()!=0 && legalMove){

            legalMove =
                    LEGALMOVE.Lateral_VerticalMovement_Ranged(obj, this, maps, this.getObjVSpeed());

            System.out.println("LEGALMOVE.Lateral_VerticalMovement_Ranged: "+legalMove);
        }

        //HORSEY!!! :)

        if(this.isKnight() && legalMove){

            legalMove =
            LEGALMOVE.HorseyPrance_Knight(obj, this, maps);

            System.out.println("LEGALMOVE.HorseyPrance_Knight: "+legalMove);

        }

        //DIAGONAL
        if(this.getDiagonal_move_range()!=0 && legalMove){

            legalMove =
                    LEGALMOVE.Diagonal_MovementRanged(obj, this, maps, this.getDiagonal_move_range());

            System.out.println("LEGALMOVE.Diagonal_MovementRanged: "+legalMove);
        }

        if(this.isOnlyTowardsEnemy() && legalMove){
            legalMove = LEGALMOVE.moveTowardsEnemyLine(obj, this, maps);

            System.out.println("LEGALMOVE.moveTowardsEnemyLine: "+legalMove);
        }

        System.out.println("Move Made was Legal: "+legalMove);

        if(!legalMove)this.setObjColour(new Color(255,0,0));

        this.setPosY(obj.getPosY() / maps.getTileHeight());
        this.setPosX(obj.getPosX() / maps.getTileWidth());

        this.setObjWidth(maps.getTileWidth());
        this.setObjHeight(maps.getTileHeight());

    }


    /**
     * hastily moved from the map class to the piece class and didn't feel like redoing what i just did...
     * Given a solidobject convert the X Y of that describe the row / column
     * @param maps a solidobject that describes it's x, y as rows/ columns not as actual x,y
     *            also convert the width + height
     * @return a converted solid object with the X / Y that sam described rows and columns to the maps column, rows
     */
    protected Piece convertRowCol_XY(Map maps){
        Piece result = this;

        //Convert the Row to X
        result.setPosX(
                result.getPosX() * maps.getTileWidth()
        );

        //Column to Y
        result.setPosY(
                result.getPosY() * maps.getTileHeight()
        );

        //Now the width's + Length
        result.setObjWidth(
                result.getObjWidth() * maps.getTileWidth()
        );

        result.setObjHeight(
                result.getObjHeight() * maps.getTileHeight()
        );


        return result;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }

    public int getDiagonal_move_range() {
        return diagonal_move_range;
    }

    public void setDiagonal_move_range(int diagonal_move_range) {
        this.diagonal_move_range = diagonal_move_range;
    }

    public boolean isKnight() {
        return isKnight;
    }

    public void setKnight(boolean knight) {
        isKnight = knight;
    }

    public boolean isOnlyTowardsEnemy() {
        return onlyTowardsEnemy;
    }

    public void setOnlyTowardsEnemy(boolean onlyTowardsEnemy) {
        this.onlyTowardsEnemy = onlyTowardsEnemy;
    }
}
