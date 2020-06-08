package sample;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Map {
    /**
     * To point of this class to handle the playable area the 'map' if you will is larger than the visible area
     * Additionally this should handle providing the visible area and drawing it
     * <p>
     * The view will follow a moving object, typically a player and will compensate as the player in a given direction until the limit of the map size is met in any given direction and then the viewing port will stop in that direction
     */

    private int viewX; //The X Position of the top left of the viewing area
    private int viewY; //the Y position of the top left of the viewing area

    private int viewWidth;//the width of the viewing area, typically going to be the size of the graphical window
    private int viewHeight;//the width of the viewing area, typically going to be the size of the graphical window

    private int MapWidth;
    private int MapHeight;

    //Tiles will be used as the boxes to alow for a consistant grid system to allow for easy building
    private int tileWidth; // the width of the tiles
    private int tileHeight; //the height of the tiles

    //2D arraylist of the tiles
    private ArrayList<ArrayList<SolidObject>> tileList = new ArrayList<>();

    public Map(int viewX, int viewY, int viewWidth, int viewHeight, int mapWidth,
               int mapHeight, int tileWidth, int tileHeight, Color white, Color notWhite) {
        this.viewX = viewX;
        this.viewY = viewY;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        MapWidth = mapWidth;
        MapHeight = mapHeight;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        initTiles(white, notWhite);

    }

    private void initTiles(Color white, Color notWhite){

            //calculate the number of rows / columns by finding out how may of the size given fit
            int rows = MapWidth/tileWidth;
            int columns = MapHeight/tileHeight;

            //make a checkerboard pattern on the screen equally sized based on the window size

            int colSize = tileHeight; // calculate the actual size of the rows

            int rowSize = tileWidth; // calculates the actual size of the columns

            boolean isWhite = false; // used to change back and forth the value of squares color

            ArrayList<SolidObject> sub_list = new ArrayList<>();

            Color colour = Color.white;

            //loop through every row
            // within each row loop through every column
            //in each position make a box in the available space

            for (int r = 0; r < rows; r++) {

                sub_list = new ArrayList<>();//erase for new set

                for (int c = 0; c < columns; c++) {
                    //make a square at the current iterated position of the columns and rows of the size of those columns and rows divided by the window size
                    int X = (r * rowSize)-viewX;
                    // calculate the position of the top left of the rectangle

                    int Y = (c* colSize)-viewY;

                    //alternate what needs to be drawn where
                    if (isWhite) {
                        colour = white;
                        //flip it back to the other color
                        isWhite = !isWhite;
                    } else {
                        colour = notWhite;
                        //flip it back to the other color
                        isWhite = !isWhite;
                    }

                    sub_list.add(new SolidObject(X,Y,rowSize,colSize,colour));

                }

                tileList.add(sub_list);

                //end of a row looping through each column position

                if (columns == rows && (columns % 2) == 0) {
                    isWhite = !isWhite;
                } else if (rows < columns && (columns % 2) == 0) {
                    isWhite = !isWhite;
                }

            }


        }

    /**
     * Convert the window to a width and length and give it to the function FitBoardToView() to do the heavy work
     * @param window - the window to be fit to
     * Also note the View Width / Height are also updated to match the window's size
     */
    protected void FitBoardToJFrame(GameCanvas window){

        //update View Width / Height
        this.setViewHeight(  window.getHeight()  );

        this.setViewWidth(   window.getWidth()   );

        //Now call the FitBoardToView() to do the heavy lifting
        FitBoardToView(
                this.getViewWidth(),
                this.getViewHeight()
                         );

        }

    /**
     * This is a very dangerous function to call unless
     * every object described as a size of the tiles is
     * updated accordingly or called with the tile size every time on draw
     */
    protected void FitBoardToView(int Width, int Height){

        //Divide the Width by the number of tiles and use that number as the new tileWidth
        int _Tile_Width = Width / tileList.size();

        int _Tile_Height = Height / tileList.get(0).size();

        //Update the width / height

        this.setTileHeight(_Tile_Height);

        this.setTileWidth(_Tile_Width);

        //Now update the X, Y's of the tiles as needed

        int x = 0;
        int y = 0;

        for(ArrayList<SolidObject> rows: tileList){

            for(SolidObject column_: rows){

                //Update the X, Y with the looped position
                column_.setPosX(
                        x
                                );

                column_.setPosY(
                        y
                                );

                column_.setObjWidth(_Tile_Width);
                column_.setObjHeight(_Tile_Height);

                //Then at the next position update the Y position

                y+=_Tile_Height;
            }

            //Then at the end of the row update the X
            //Gotta reset the column, the iterate to the next row
            y= 0;
            x+=_Tile_Width;

        }

        }


    /**
     * Given an arraylist of BackgroundImage draw the images in accordance
     * to the relative position to the view
     */

    public void drawBackgroundImages(Graphics2D gg,ArrayList<BackgroundImage> imageList){

        /**
         * only draw images that 'colide' with the view map
         */

        for(BackgroundImage i: imageList){

            int width = i.getObjWidth();
            int height = i.getObjHeight();

            i.setObjWidth(i.getObjWidth()*4);
            i.setObjHeight(i.getObjHeight()*4);

            if(i.isCollision(viewX,viewY,viewWidth*4,viewHeight)
            ){
                i.setObjWidth(width);
                i.setObjHeight(height);
                gg.drawImage(i.getImage(),
                        i.getPosX()- viewX
                        , i.getPosY()  - viewY
                        , i.getObjWidth(), i.getObjHeight()
                        , null);
            }
            i.setObjWidth(width);
            i.setObjHeight(height);


        }


    }


    /**
     * Draw a checkerboard to help determining if the program is working as intended
     * @param gg - it's what's drawn on
     */

    /**
     * The class values of the col, row are used to distiished how many rows / columns are needed
     * The  WindowLength and WindowWidth are used to disginquished how many columns may be neded to fill the vailable space
     * <p>
     * This function should only be called by the paint class as anyone else may cause an endless loop
     */
    public void drawCheckerboard(Graphics2D gg) {
        gg.setBackground(Color.red);

        for(ArrayList<SolidObject> list: tileList){

            for(SolidObject john: list){


                gg.setColor(john.getObjColour());

                john.drawobj(gg,this);

            }

        }


    }

    /**
     * Given an X,Y coordinates return a SolidObject representing the tile position (x,y,width, length, and Color Orange)
     * @param  targetx - x coordinate (assumes positive)
     * @param targety - y coordinate (assumes positive)
     */
    protected static SolidObject CollidedTile(int targetx, int targety, Map gameMap) {
        SolidObject output = null;

        for(int i = 0; i< gameMap.getTileList().size(); i ++){

            for(int j = 0 ; j < gameMap.getTileList().get(i).size();j++){

                if(
                        gameMap.getTileList().get(i).get(j).
                                isCollision(targetx,targety,1,1)){

                    output =  gameMap.getTileList().get(i).get(j);
                }
            }


        }

        return output;
    }

    /**
     * @return true if the Building and the object collide tiles
     */
    public boolean calcTileCollision(SolidObject john, SolidObject sam){
        boolean isCollision = true;

        if(isAbove(sam, john)||isBelow(sam,john)){
            return false;
        }
        else if(isRight(sam,john)||isLeft(sam,john)){
            return false;
        }
        return true;
    }

    public boolean isBelow(SolidObject sam, SolidObject john){
        /**
         * this is true when 'this' object is entirely above sam
         */
        if((john.getPosY()+john.getObjHeight()<sam.getPosY())){
            return true;
        }
        return false;
    }
    public boolean isAbove(SolidObject sam, SolidObject john){
        /**
         * this is true when 'this' object is entirely below sam
         */
        if(john.getPosY()>sam.getPosY()+sam.getObjHeight()){
            return true;
        }
        return false;
    }

    public boolean isRight(SolidObject sam, SolidObject john){
        /**
         * this is true when 'this' object is entirely above sam
         */
        if((john.getPosX()+john.getObjHeight()<sam.getPosX())){
            return true;
        }
        return false;
    }
    public boolean isLeft(SolidObject sam, SolidObject john){
        /**
         * this is true when 'this' object is entirely below sam
         */
        if(john.getPosX()>sam.getPosX()+sam.getObjWidth()){
            return true;
        }
        return false;

    }

    protected void updateBoardSize(){
        int x = 0;
        int y = 0;
        for(ArrayList<SolidObject> a:tileList){

            y =tileList.indexOf(a)*tileWidth;

            for(SolidObject b: a){

                x = (a).indexOf(b)*tileHeight;


                b.setObjHeight(tileHeight);
                b.setObjWidth(tileWidth);

                b.setPosX(x);
                b.setPosY(y);

            }

        }

    }


    protected void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
        if(this.tileHeight<0)this.tileHeight=1;

        updateBoardSize();
    }

    protected void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
        if(this.tileWidth<0)this.tileWidth=1;
        updateBoardSize();
    }


    public int getViewX() {
        return viewX;
    }

    public void setViewX(int viewX) {
        this.viewX = viewX;
    }

    public int getViewY() {
        return viewY;
    }

    public void setViewY(int viewY) {
        this.viewY = viewY;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    public int getMapWidth() {
        return MapWidth;
    }

    public void setMapWidth(int mapWidth) {
        MapWidth = mapWidth;
    }

    public int getMapHeight() {
        return MapHeight;
    }

    public void setMapHeight(int mapHeight) {
        MapHeight = mapHeight;
    }

    public int getTileWidth() {
        return tileWidth;
    }


    public int getTileHeight() {
        return tileHeight;
    }


    public ArrayList<ArrayList<SolidObject>> getTileList() {
        return tileList;
    }

    public void setTileList(ArrayList<ArrayList<SolidObject>> tileList) {
        this.tileList = tileList;
    }
}