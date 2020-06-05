package sample;

import java.awt.*;

import static java.lang.Math.abs;

public class Plane extends MovingObject {

    //these track the players cooldown to when they can next fire
    private int fireCooldown =0;

    private int FIRECOOLDOWN = 0;

    private int altFireCooldown =0;
    private int ALTFIRECOOLDOWN = 0;

    private int defaultProjectileHeight=25;
    private int DefaultProjectileWidth =15;

    private int health = 100;

    private String name;


    /**
     * @param posX - the X position of the top left of the square
     * @param posY - the Y position of the top left of the square
     * @param objWidth - the width
     * @param objHeight height
     * @param defaultHSpeed the H value that is referenced as how fast it can/ should be going
     * @param defaultVSpeed the V value that is referenced as how fast it can/ should be going
     *
     * @param health the health of the player
     * @param name the name of the player
     *
     * @param FIRECOOLDOWN The default value reference to stop the player from spamming the fire button
     * @param ALTFIRECOOLDOWN the default value reference to stop the player from spamming the alt fire button
     * @param defaultProjectileHeight the reference height for when the plane fires a projectile
     * @param defaultProjectileWidth the reference width of a fired projectile
     */
    public Plane(int posX, int posY, int objWidth, int objHeight, int defaultHSpeed, int defaultVSpeed,
                 int health, String name,

                 int FIRECOOLDOWN, int ALTFIRECOOLDOWN, int defaultProjectileHeight, int defaultProjectileWidth) {
        super(posX, posY, objWidth, objHeight, defaultHSpeed, defaultVSpeed);

        this.FIRECOOLDOWN = FIRECOOLDOWN;
        this.ALTFIRECOOLDOWN = ALTFIRECOOLDOWN;
        this.defaultProjectileHeight = defaultProjectileHeight;
        this.DefaultProjectileWidth = defaultProjectileWidth;
        this.health = health;
        this.name = name;
    }


    //Full constructor that on construction sets all values for the player

    /**
     *
     * @param posX - the X position of the top left of the square
     * @param posY - the Y position of the top left of the square
     * @param objWidth - the width
     * @param objHeight height
     * @param defaultHSpeed the H value that is referenced as how fast it can/ should be going
     * @param defaultVSpeed the V value that is referenced as how fast it can/ should be going
     */
    public Plane(int posX, int posY, int objWidth, int objHeight,
                 int defaultHSpeed, int defaultVSpeed,
                 Color c, String name

    ) {

        super(posX,posY, objWidth, objHeight, defaultHSpeed, defaultVSpeed);
        super.setObjColour(c);

        this.name = name;
    }


    /**
     *  //Lazy constructor with some default values
     * @param posX - the X position of the top left of the square
     * @param posY - the Y position of the top left of the square
     *
     * @param buttonUp The KeyBoard numerical reference key for input
     * @param buttonDown The KeyBoard numerical reference key for input
     * @param buttonLeft The KeyBoard numerical reference key for input
     *                   */
    public Plane(int posX, int posY, int buttonUp, int buttonDown,
                 int buttonLeft, int buttonRight) {

        super(50,50, 1, 1, 0, 0);
    }

    /**
     *
     * @param posX - the X position of the top left of the square
     * @param posY - the Y position of the top left of the square
     * @param objWidth - the width
     * @param objHeight height
     * @param defaultHSpeed the H value that is referenced as how fast it can/ should be going
     * @param defaultVSpeed the V value that is referenced as how fast it can/ should be going
     *
     */
    public Plane(int posX, int posY, int objWidth, int objHeight,
                 int defaultHSpeed, int defaultVSpeed, int defaultProjectileHeight, int defaultProjectileWidth,
                 int health, String name) {
        super(posX, posY, objWidth, objHeight, defaultHSpeed, defaultVSpeed);
        this.defaultProjectileHeight = defaultProjectileHeight;
        DefaultProjectileWidth = defaultProjectileWidth;
        this.health = health;
        this.name = name;
    }

    /**
     * this is called when this player's fire button is pressed
     * @return a created projectilee that the player fired
     */
    public Projectile createProjectile(){

        int proX = getPosX()+ this.getObjWidth()/2;
        int proY = (getPosY() +(this.getObjHeight()/2));

        int speedH = getDefaultHSpeed()*2;
        int speedV = getDefaultVSpeed()/4;

        //to ensure the projectile will always shoot in relative to the fastest speed of the player check to see what direction and set speed accordingly
        if(getObjHSpeed() < 0){
            speedH = -speedH;
        }
        if(getObjVSpeed() <0){
            speedV = -speedV;
        }

        if(getObjHSpeed() == 0){
            speedH = 0;
        }
        if(getObjVSpeed() ==0){
            speedV = 0;
        }
//System.out.println(getDefaultVSpeed()+" : "+speedV);

        Projectile ouchy = new Projectile(
                proX,proY, defaultProjectileHeight, DefaultProjectileWidth, speedH, speedV
        );

        ouchy.setObjColour(getObjColour());

        //fish are friends not food
        ouchy.addFriendly(this);
        this.addFriendly(ouchy);
        return ouchy;
    }


    //This is used to move the cooldowns slowly to 0
    public void calcCooldowns(){
        if(fireCooldown>0)fireCooldown --;
        if(altFireCooldown >0) altFireCooldown--;
    }

    //start the fire button cooldown
    public void fireCooldownStart(){
        fireCooldown = FIRECOOLDOWN;
    }
    //start the bomb button cooldown
    public void bombCooldownStart(){
        altFireCooldown = ALTFIRECOOLDOWN;
    }


    /**
     * @param john the movingobject that is tested
     * @param reverseDirection if true the object will be set to reverse direction
     * @return if a border was reached
     *
     * //use the given player object and calculate and adjust if the player is going to exceed the borders and then move the player to the
     *         //oppisite side of the window
     */

    protected boolean PlaneBorderTest(MovingObject john, boolean reverseDirection, Map gameMap){
        if(john==null)return false;
        boolean isBorderReached = false;

        if(RightBottomPlaneBorderTest(john, reverseDirection,gameMap)){
            return true;
        }


        if (john.getPosX() <= 0) { // resets the object position if it exceeds the left border
            john.setObjHSpeed(0);
            //If the reverse direction boolean is true then reverse the direction of the moving object 'john'
            if(reverseDirection){
                int XSpeed = abs(john.getDefaultHSpeed());
                john.setObjHSpeed(XSpeed);
            }

            john.setPosX(
                    1
            );
            isBorderReached = true;
        }

        else if (john.getPosY() <= 0) {
            // resets the object position if it exceeds the TOP border
            john.setObjVSpeed(0);

            if(reverseDirection){
                int YSpeed = abs(john.getDefaultVSpeed());
                john.setObjVSpeed(YSpeed);
            }

            john.setPosY(
                    1
            );
            isBorderReached = true;
        }


        return isBorderReached;
    }

    private boolean RightBottomPlaneBorderTest(MovingObject john, boolean reverseDirection, Map gameMap){
        boolean isBorderReached=false;


        //Stop Vertical exceeding of borders by moving the player back by 1 pixel above the detection point
        if (john.getPosY() +john.getObjHeight() > gameMap.getMapHeight()) { // resets the object position if it exceeds the BOTTOM border


            john.setObjVSpeed(0);

            if(reverseDirection){
                int YSpeed = abs(john.getDefaultVSpeed());
                john.setObjVSpeed(-YSpeed);
            }
            john.setPosY(
                    gameMap.getMapHeight()
                            -
                            (1+john.getObjHeight())
            );
            isBorderReached = true;
        }

        //Stop Horizontal exceeding of borders by moving the player back by 1 pixel above the detection point
        if (john.getPosX() +john.getObjWidth() > gameMap.getMapWidth()) { // resets the object position if it exceeds the right border
            john.setObjHSpeed(0);
            //If the reverse direction boolean is true then reverse the direction of the moving object 'john'
            if(reverseDirection){
                int XSpeed = abs(john.getDefaultHSpeed());
                john.setObjHSpeed(-XSpeed);
            }
            john.setPosX(
                    gameMap.getMapWidth()
                            -
                            (1+john.getObjWidth())
            );
            isBorderReached = true;
        }



        return isBorderReached;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }



    public int getFireCooldown() {
        return fireCooldown;
    }

    public void setFireCooldown(int fireCooldown) {
        this.fireCooldown = fireCooldown;
    }

    public void setFIRECOOLDOWN(int FIRECOOLDOWN) {
        this.FIRECOOLDOWN = FIRECOOLDOWN;
    }

    public int getAltFireCooldown() {
        return altFireCooldown;
    }

    public void setAltFireCooldown(int altFireCooldown) {
        this.altFireCooldown = altFireCooldown;
    }


    public void setALTFIRECOOLDOWN(int ALTFIRECOOLDOWN) {
        this.ALTFIRECOOLDOWN = ALTFIRECOOLDOWN;
    }

    public int getDefaultProjectileHeight() {
        return defaultProjectileHeight;
    }

    public void setDefaultProjectileHeight(int defaultProjectileHeight) {
        this.defaultProjectileHeight = defaultProjectileHeight;
    }

    public int getDefaultProjectileWidth() {
        return DefaultProjectileWidth;
    }

    public void setDefaultProjectileWidth(int defaultProjectileWidth) {
        DefaultProjectileWidth = defaultProjectileWidth;
    }
}
