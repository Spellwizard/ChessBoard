package sample;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.lang.Math.abs;


/**
 * This is a class to be used as a parent class for sub classes such as
 * for a 'player', 'projectile', and any other moving objects in the game
 */
public class MovingObject extends SolidObject{

    private ArrayList<MovingObject> friendly; // list of friendly objects to this object stop friendly fire

    //default speed of the plane
    private int defaultHSpeed; //horizontally
    private int defaultVSpeed; //vertically

    //the current actual value of the object speed
    private int objHSpeed; //the HORIZONTAL OR THE LEFT / RIGHT MOTION OF THE CRAFT
    private int objVSpeed; //THE VERTICAL OR THE UP / DOWN MOTION OF THE CRAFT

    // used to track the previous position of the x value of the plane
    private int lastX;
    private int lastY;

    //More logical constructor only asking for some values
    //Full constructor that on construction sets all values for the player

    /**
     *
     * @param posX the top left corner of the object's x value
     * @param posY the top left corner y value
     * @param objWidth the width of the object
     * @param objHeight the height of the object
     * @param defaultHSpeed sets the current H speed and stores the original H speed as default for calling later
     * @param defaultVSpeed sets the current V speed and stores the oringinal V speed as defualt for caling later
     */
    public MovingObject(int posX, int posY, int objWidth, int objHeight,
                        int defaultHSpeed, int defaultVSpeed) {
        super(posX, posY, objWidth, objHeight, Color.BLACK);

        this.defaultHSpeed = defaultHSpeed;
        this.defaultVSpeed = defaultVSpeed;

        this.objHSpeed = defaultHSpeed;
        this.objVSpeed = defaultVSpeed;

    }

    /**
     *
     * @param posX the top left corner of the object's x value
      * @param posY the top left corner y value
     */
    public MovingObject(int posX, int posY) {
        super(posX, posY, 50, 50, Color.BLACK);

        this.defaultHSpeed = 0;
        this.defaultVSpeed = 0;

        this.objHSpeed = 0;
        this.objVSpeed = 0;
        this.lastX = 0;
        this.lastY = 0;
    }

    /**
     *
     * @param posX the top left corner of the object's x value
     * @param posY the top left corner y value
     * @param objWidth the width of the object
     * @param objHeight the height of the object
     * @param objHSpeed and sets defaultHSpeed as sets the current H speed and stores the original H speed as default for calling later
     * @param objVSpeed and sets defaultVSpeed sets the current V speed and stores the oringinal V speed as defualt for caling later
     */
    public MovingObject(int objWidth, int objHeight, int posX, int posY, Color objColour,
                        BufferedImage r_Image, BufferedImage l_Image, BufferedImage up_Image, BufferedImage down_Image,
                        int objHSpeed, int objVSpeed, int health) {
        super(objWidth, objHeight, posX, posY, objColour, r_Image, l_Image, up_Image, down_Image);
        this.defaultHSpeed = objHSpeed;
        this.defaultVSpeed = objVSpeed;
        this.objHSpeed = objHSpeed;
        this.objVSpeed = objVSpeed;
        this.health = health;
    }

    /**based on current values of movement calculate the new values for the x,y
    and save the old x, y to the lastX, lastY respecitvely
     */
    public void calcMovement(){
        //save the x & y values in the lastX, last Y value spots
        lastX = super.getPosX();
        lastY = super.getPosY();

        //finally update the values of the x & y using the resepctive speeds in those directions
        super.setPosX(objHSpeed +super.getPosX());
        super.setPosY(objVSpeed + super.getPosY());
    }

    /**
     * @param newfriend add a movingObject as a friend to the arraylist
     */
    public void addFriendly(MovingObject newfriend){
        if(friendly == null){friendly = new ArrayList<MovingObject>();}
        this.friendly.add(newfriend);
    }

    /**
     * given a movingobject return true if it is on the friend list
     * @param sam - determine if sam is friends with 'this'
     * @return true if they are friends
     */
    public boolean isFriend(MovingObject sam){
        try{
            for(MovingObject item: friendly){
                if(item.equals(sam)) {
                    return true;

                }
            }
        }
        catch(Exception e){
            System.out.println("Error finding friends");
            return false;
        }

        return false;
    }


    public int getEnergy() {
        return health;
    }

    public void setEnergy(int energy) {
        this.health = energy;
    }

    private int health;



    //Basic Getter's / Setters that simply set and or get the variable


    public int getDefaultHSpeed() {
        int safe = defaultHSpeed;
        return safe;
    }

    public void setDefaultHSpeed(int defaultHSpeed) {
        this.defaultHSpeed = defaultHSpeed;
    }

    public int getDefaultVSpeed() {
        int safe = defaultVSpeed;
        return safe;
    }

    public void setDefaultVSpeed(int defaultVSpeed) {
        this.defaultVSpeed = defaultVSpeed;
    }

    public int getObjHSpeed() {
        int safe = objHSpeed;
        return safe;
    }

    public void setObjHSpeed(int objHSpeed) {
        this.objHSpeed = objHSpeed;
    }

    public int getObjVSpeed() {
        int safe = objVSpeed;
        return safe;
    }

    public void setObjVSpeed(int objVSpeed) {
        this.objVSpeed = objVSpeed;
    }

    public int getLastX() {
        return lastX;
    }

    public void setLastX(int lastX) {
        this.lastX = lastX;
    }

    public int getLastY() {
        return lastY;
    }

    public void setLastY(int lastY) {
        this.lastY = lastY;
    }



    public ArrayList<MovingObject> getFriendly() {
        return friendly;
    }

    public void setFriendly(ArrayList<MovingObject> friendly) {
        this.friendly = friendly;
    }

    /**
     * Reverse the direction of the object both VSpeed and HSpeed using the default values
     */
    public void reverseDirection(){
        if(objVSpeed>0){
            objVSpeed = -abs(defaultVSpeed);
        }
        else if(objVSpeed< 0){
            objVSpeed = abs(defaultVSpeed);
        }

        if(objHSpeed>0){
            objHSpeed = -abs(defaultHSpeed);
        }
        else if(objVSpeed< 0){
            objHSpeed = abs(defaultHSpeed);
        }

    }

}
