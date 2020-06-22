package sample;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Parent function for all solid objects
 */
public class SolidObject {

    private int objWidth; //the actual 'hitbox' or the width of the object in pixels
    private int objHeight; //the actual 'hitbox' or the height of the object in pixels


    //The current position of the plane
    private int posX;
    private int posY;

    private Color objColour = Color.BLACK; // the object colour instatiated incase someone is an idiot..

    //This object's image as it moves in different directions
    private BufferedImage R_Image = null;
    private BufferedImage L_Image = null;
    private BufferedImage Up_Image = null;
    private BufferedImage Down_Image = null;


    /**
     * @param posX - the interger position in pixels of the object - X axis
     * @param posY - the interger position in pixels of the object - Y axis
     * @param objWidth  - the interger width in pixels of the object
     * @param objHeight  - the interger height in pixels of the object
     * @param objColour the color of the object in the class 'Color'
     */
    protected SolidObject(int posX, int posY, int objWidth, int objHeight, Color objColour) {
        this.objWidth = objWidth;
        this.objHeight = objHeight;
        this.posX = posX;
        this.posY = posY;
        this.objColour = objColour;
    }

    /**
     *
     * @param posX - the interger position in pixels of the object - X axis
     * @param posY - the interger position in pixels of the object - Y axis
     * @param objWidth  - the interger width in pixels of the object
     * @param objHeight  - the interger height in pixels of the object
     * @param objColour the color of the object in the class 'Color'
     * @param r_Image - the image to draw when the object is moving / facing right
     * @param l_Image - the image to draw when the object is moving / facing left
     * @param up_Image - the image to draw when the object is moving / facing up
     * @param down_Image - the image to draw when the object is moving / facing down
     */
    protected SolidObject(int objWidth, int objHeight, int posX, int posY, Color objColour,
                       BufferedImage r_Image, BufferedImage l_Image, BufferedImage up_Image, BufferedImage down_Image) {
        this.objWidth = objWidth;
        this.objHeight = objHeight;
        this.posX = posX;
        this.posY = posY;
        this.objColour = objColour;
        R_Image = r_Image;
        L_Image = l_Image;
        Up_Image = up_Image;
        Down_Image = down_Image;
    }

    /**given a 2d Graphics graw the object in reference to viewX and viewY of the MapView
    * Eg: object 0,0 and the mapview 1,1
    * draw the object @ -1,-1
    * Additionally scale the object in comparision to the width and height of the map view to allow objects to appear
    * to get bigger / smaller as the zoom changes
     * @param gg - the Graphics 2D that the object is drawn on
     * @param maps - the Map that is used to draw the object relatively to the view X, Y positions
     */
    protected void drawobj(Graphics2D gg, Map maps){
        gg.setColor(objColour);

        gg.fillRect(posX-maps.getViewX(),
                posY- maps.getViewY(),
                objWidth, objHeight);
    }

    protected void printXY(){
        System.out.println("SolidObject ("+this.getPosX()+", "+this.getPosY()+")");
    }

    protected void printXY(String name){
        System.out.println(name+"("+this.getPosX()+", "+this.getPosY()+")");
    }

    /**
     *
     *@param gg - the Graphics 2D that the object is drawn on
     */
    protected void unCompensateddrawobj(Graphics2D gg){
        gg.setColor(objColour);

        gg.fillRect(posX,
                posY,
                objWidth, objHeight);
    }

    /**
     * @param sam the SolidObject to compare to to determine if they are colliding
     * @return true when 'this' object is entirely above sam
     */
    protected boolean isBelow(SolidObject sam){
        /**
         * this is true when 'this' object is entirely above sam
         */
        if((this.getPosY()+this.getObjHeight()
                < sam.getPosY()+1)){
            return true;
        }
        return false;
    }

    /**
     * @param sam the SolidObject to compare to to determine if they are colliding
     * @return true when 'this' object is entirely above sam
     */
    protected boolean isBelow(SolidObject sam, SolidObject john){
        /**
         * this is true when 'this' object is entirely above sam
         */
        if((john.getPosY()+john.getObjHeight()
                < sam.getPosY()+1)){
            return true;
        }
        return false;
    }


    /**
     * @param sam the SolidObject to compare to to determine if they are colliding
     * @return  true when 'this' object is entirely below of sam
     */
    protected boolean isAbove(SolidObject sam){
        if(this.getPosY()+1
                >
                sam.getPosY()+sam.getObjHeight()){
            return true;
        }
        return false;
    }

    /**
     * @param sam the SolidObject to compare to to determine if they are colliding
     * @return  true when 'this' object is entirely below of sam
     */
    protected boolean isAbove(SolidObject sam, SolidObject john){
        if(john.getPosY()+1
                >
                sam.getPosY()+sam.getObjHeight()){
            return true;
        }
        return false;
    }

    /**
     * @param sam the SolidObject to compare to to determine if they are colliding
     * @return  true when 'this' object is entirely right of sam
     */
    protected boolean isRight(SolidObject sam){
        if(

        (sam.getPosX()+1)>
                (   this.getPosX()  + this.getObjWidth() )

        ){
            return true;
        }
        return false;
    }

    /**
     * @param sam the SolidObject to compare to to determine if they are colliding
     * @return  true when 'this' object is entirely right of sam
     */
    protected boolean isRight(SolidObject sam, SolidObject john){
        if(

                (sam.getPosX()+1)>
                        (   john.getPosX()  + john.getObjWidth() )

        ){
            return true;
        }
        return false;
    }

    /**
     * @param sam the SolidObject to compare to to determine if they are colliding
     * @return  true when 'this' object is entirely left of sam
     */
    protected boolean isLeft(SolidObject sam){
        if(this.getPosX()+1
                >
                sam.getPosX()+sam.getObjWidth()){
            return true;
        }
        return false;

    }

    /**
     * @param sam the SolidObject to compare to to determine if they are colliding
     * @return  true when 'this' object is entirely left of sam
     */
    protected boolean isLeft(SolidObject sam, SolidObject john){
        if(john.getPosX()+1
                >
                sam.getPosX()+sam.getObjWidth()){
            return true;
        }
        return false;

    }



    /**
     * calulate given another object to see if collision has occured
     * @param sam compare sam and 'this' for collisions
     * @return true if the objects are colliding
     */
    protected boolean isCollision(SolidObject sam){

        if(isAbove(sam)||isBelow(sam)){
            return false;
        }
        else if(isRight(sam)||isLeft(sam)){
            return false;
        }
        return true;
    }



    /**
     * calulate given another object to see if collision has occured
     * @param x - the x position in pixels of sam
     * @param y - the y position in pixels of sam
     * @param h the height of of sam
     * @param w the width of sam
     * //create sam (SolidObject) from the given values to simulate a solidobject to compare for collisions
     * //and compare sam and 'this' for collisions
     *
     * @return true if the objects are colliding
     */
    protected boolean isCollision(int x, int y , int w, int h){

        SolidObject sam = new SolidObject(x,y,w,h,Color.white);

        if(isAbove(sam)||isBelow(sam)){
            return false;
        }
        else if(isRight(sam)||isLeft(sam)){
            return false;
        }

        return true;

    }

    protected boolean isCollision(SolidObject sam, Map gameMap){
        return isCollision(
                sam.getPosX(),sam.getPosY(), sam.getObjWidth()*gameMap.getTileWidth(), sam.getObjHeight()*gameMap.getTileHeight(),

                this.getPosX(),this.getPosY(),this.getObjWidth()*gameMap.getTileWidth(),this.getObjHeight()*gameMap.getTileHeight()
                );
    }

    protected boolean isCollision(int SamX, int SamY , int SamW, int SamH, Map gameMap){
        SolidObject sam = new SolidObject(SamX,SamY,SamW,SamH,Color.white);

        return isCollision(sam,gameMap);

    }

    /**
     * calulate given another object to see if collision has occured
     * //create sam (SolidObject) from the given values to simulate a solidobject to compare for collisions
     * //and compare sam and 'this' for collisions
     * @return true if the objects are colliding
     *
     * @param SamX
     * @param SamY
     * @param SamW
     * @param SamH
     * @param JohnX
     * @param JohnY
     * @param JohnW
     * @param JohnH
     * @return
     */
    protected boolean isCollision(int SamX, int SamY , int SamW, int SamH,
                               int JohnX, int JohnY, int JohnW, int JohnH
                               ){

        SolidObject sam = new SolidObject(SamX,SamY,SamW,SamH,Color.white);

        SolidObject john = new SolidObject(JohnX,JohnY,JohnW,JohnH,Color.white);

        if(isAbove(sam,john)||isBelow(sam,john)){
            return false;
        }
        else if(isRight(sam,john)||isLeft(sam,john)){
            return false;
        }

        return true;

    }


    protected int getPosX() {
        return posX;
    }

    protected void setPosX(int posX) {
        this.posX = posX;
    }

    protected int getPosY() {
        return posY;
    }

    protected void setPosY(int posY) {
        this.posY = posY;
    }


    protected Color getObjColour() {
        return objColour;
    }

    protected void setObjColour(Color objColour) {
        this.objColour = objColour;
    }


    protected int getObjWidth() {
        return objWidth;
    }

    protected void setObjWidth(int objWidth) {
        this.objWidth = objWidth;
    }

    protected int getObjHeight() {
        return objHeight;
    }

    protected void setObjHeight(int objHeight) {
        this.objHeight = objHeight;
    }



    protected BufferedImage getR_Image() {
        return R_Image;
    }

    protected BufferedImage getL_Image() {
        return L_Image;
    }

    protected BufferedImage getUp_Image() {
        return Up_Image;
    }

    protected BufferedImage getDown_Image() {
        return Down_Image;
    }


    protected void setR_Image(BufferedImage r_Image) {
        R_Image = r_Image;
    }

    protected void setL_Image(BufferedImage l_Image) {
        L_Image = l_Image;
    }

    protected void setUp_Image(BufferedImage up_Image) {
        Up_Image = up_Image;
    }

    protected void setDown_Image(BufferedImage down_Image) {
        Down_Image = down_Image;
    }

    /**
     * @param filePath Given a file path save the image to the respective directional movment
     */
    protected void setRightImage(String filePath) {
        {
            try {

                R_Image = ImageIO.read(new File(filePath));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * @param filePath Given a file path save the image to the respective directional movment
     */
    protected void setLeftImage(String filePath) {
        {
            try {

                L_Image = ImageIO.read(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * @param filePath Given a file path save the image to the respective directional movment
     */
    protected void setUpImage(String filePath) {
        try {

            Up_Image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * @param filePath Given a file path save the image to the respective directional movment
     */
    protected void setDownImage(String filePath) {

        try {

            Down_Image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
