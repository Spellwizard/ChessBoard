package sample;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Player extends Plane {

    //the keyboard value associated with each of the following movment / input types
    private int buttonUp; //UP
    private int buttonDown; //DOWN
    private int buttonLeft; //LEFT
    private int buttonRight; //RIGHT

    private int buttonFire; //the fire button

    private int buttonAltFire;//either the secondary fire or the bomb button


    //0 - Up
    //1 - Right
    //2 - Down
    //3 - Left
    private int direction;//this defines which way the building's will be placed in


    /**
     * Used to track the various items in the player bar
     */
    private ArrayList<BeltSlot> beltList;

    /**
     * This is default to te first item in an instatiated beltlist but is the referenced tile when the player will
     * select to make a tile
     * if the player uses one of the keycmds in the beltlist to change the selected item then it should change to that different ite
     */
    private BeltSlot selectedItem;

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

     * @param buttonUp
     * @param buttonDown
     * @param buttonLeft
     * @param buttonRight
     * @param buttonFire
     * @param buttonAltFire
     */
    public Player(int posX, int posY, int objWidth, int objHeight, int defaultHSpeed,
                  int defaultVSpeed, int buttonUp, int buttonDown, int buttonLeft, int buttonRight,
                  int buttonFire, int buttonAltFire, int health, String name, int FIRECOOLDOWN,
                  int ALTFIRECOOLDOWN, int defaultProjectileHeight, int defaultProjectileWidth
                  ,ArrayList<BeltSlot> beltList
    ){

        super(posX, posY, objWidth, objHeight, defaultHSpeed, defaultVSpeed, health, name, FIRECOOLDOWN,
                ALTFIRECOOLDOWN, defaultProjectileHeight, defaultProjectileWidth);
        this.buttonUp = buttonUp;
        this.buttonDown = buttonDown;
        this.buttonLeft = buttonLeft;
        this.buttonRight = buttonRight;
        this.buttonFire = buttonFire;
        this.buttonAltFire = buttonAltFire;

        this.beltList = beltList;
        selectedItem = null;
    }

    /**
     * @param m - a mouse input
     *          <p>
     *          properly handle a mouse click
     */
    public Building calcMouseClick(MouseEvent m, Map maps) {

        //compensate the map view position to get the actual position of the mouse click in connection to the view
        int posX = m.getX() + maps.getViewX();
        int posY = m.getY() + maps.getViewY();

        //determine what checkerboard position was ch
        SolidObject sub_output = maps.CollidedTile(posX, posY, maps);
        Building output = null;

        //then convert the output to an object
        if (selectedItem != null ) {
            output = selectedItem.getDefaultBuilding();

            output.setPosX(sub_output.getPosX());
            output.setPosY(sub_output.getPosY());

            output.setObjWidth(
                    selectedItem.getDefaultBuilding().getObjWidth());

            output.setObjHeight(
                    selectedItem.getDefaultBuilding().getObjHeight());

            output.setObjColour(selectedItem.getDefaultBuilding().getObjColour());


            //1 - Right
            if(direction==1){
                output.setObjVSpeed(0);
                output.setObjHSpeed(1);
            }
            //2 - Down
            else if(direction==2){
                output.setObjVSpeed(1);
                output.setObjHSpeed(0);
            }
            //3 - Left
            else if(direction==3){
                output.setObjVSpeed(0);
                output.setObjHSpeed(-1);
            }

            //Set the direction of the building
            //0 - Up
            else {
                output.setObjVSpeed(-1);
                output.setObjHSpeed(0);
            }
        }

        return output;
    }
    protected MinerBuilding calcMouseMinerClick(MouseEvent m, Map maps){

        //compensate the map view position to get the actual position of the mouse click in connection to the view
        int posX = m.getX() + maps.getViewX();
        int posY = m.getY() + maps.getViewY();

        //determine what checkerboard position was ch
        SolidObject sub_output = maps.CollidedTile(posX, posY, maps);
        Building output = null;

        //then convert the output to an object
        if (selectedItem != null &&selectedItem.isMiner()) {

            output = selectedItem.getDefaultBuilding();

            output.setPosX(sub_output.getPosX());
            output.setPosY(sub_output.getPosY());

            output.setObjWidth(
                    selectedItem.getDefaultBuilding().getObjWidth());

            output.setObjHeight(
                    selectedItem.getDefaultBuilding().getObjHeight());

            output.setObjColour(selectedItem.getDefaultBuilding().getObjColour());


            //1 - Right
            if(direction==1){
                output.setObjVSpeed(0);
                output.setObjHSpeed(1);
            }
            //2 - Down
            else if(direction==2){
                output.setObjVSpeed(1);
                output.setObjHSpeed(0);
            }
            //3 - Left
            else if(direction==3){
                output.setObjVSpeed(0);
                output.setObjHSpeed(-1);
            }

            //Set the direction of the building
            //0 - Up
            else {
                output.setObjVSpeed(-1);
                output.setObjHSpeed(0);
            }
        }

        //convert the output to a miner
        MinerBuilding converted_output = null;
        if(output!=null) {

              converted_output = new MinerBuilding(0, 0, 0, 0, Color.white,
                  maps, null, null, null, null, false);

             OverridingValuesClass.OverrideBuilding(converted_output, output);

        }

        return converted_output;
    }


    protected void UpdateBelt(Map map){
        if(beltList!=null)for(BeltSlot a: beltList)a.getDefaultBuilding().setGameMap(map);
    }


    /**
     * Given a
     * @param gg - to draw on
     * Draw relevant UI elements
     */
    public void drawUI(Graphics2D gg, Map maps){
        drawHealthBar(gg, maps);
        drawBelt(gg, maps); }
    /**
     * Draw on the
     * @param gg - 2d Graphics that the belt is drawn on
     */
    public void drawBelt(Graphics2D gg, Map maps){

        int beltItems = beltList.size();

        int beltheight = 55;
        int beltwidth = beltItems*80;

        int beltx =  (maps.getViewWidth()/2) - (beltwidth/2);
        int belty = maps.getViewHeight()-85;

        //draw each indidual box in the belt

        int inset = 5;//this is used to have something of a 'shadow' for the belt

        int itemWidth = beltwidth / beltItems;

        int itemX = beltx;

        int position =0;

        //draw up to 9 boxes that should each have a keyboard listener
        //and a cooresponding action in arraylists

        gg.setColor(new Color(
                64, 17, 0
                ));

        gg.fillRect(itemX - (inset)
                , belty - (inset),
                beltwidth + (inset * 2), beltheight + (inset * 2));


        gg.setColor(new Color(0,0,0));

        gg.fillRect(beltx,belty,beltwidth,beltheight);



        if(beltList!=null){
            for(BeltSlot a: beltList){

                if(a!=selectedItem) {
                    gg.setColor(a.getDefaultBuilding().getObjColour());

                    gg.fillRect(itemX + inset, belty + inset,
                            itemWidth - (inset * 2), beltheight - (inset * 2));

                    //Try to draw the image directionally but if fails check to ensure that up is initalized and default to up
                    //Draw the object going Down
                    if(a.getDefaultBuilding().getDown_Image()!=null&&
                            direction ==2
                    )
                        gg.drawImage(a.getDefaultBuilding().getDown_Image(),itemX + inset, belty + inset,
                                itemWidth - (inset * 2), beltheight - (inset * 2), null);

                        //Draw the object going Right
                    else if(a.getDefaultBuilding().getR_Image()!=null&&
                            direction == 1
                    ) {
                        gg.drawImage(a.getDefaultBuilding().getR_Image(), itemX + inset, belty + inset,
                                itemWidth - (inset * 2), beltheight - (inset * 2), null);
                    }
                    //Draw the object going LEFT
                    else if(a.getDefaultBuilding().getL_Image()!=null&&
                            direction==3
                    ) {
                        gg.drawImage(a.getDefaultBuilding().getL_Image(), itemX + inset, belty + inset,
                                itemWidth - (inset * 2), beltheight - (inset * 2), null);
                    }


                    //Draw the image inside the box as a representative
                    else if(a.getDefaultBuilding().getUp_Image()!=null){
                        gg.drawImage(a.getDefaultBuilding().getUp_Image(),itemX + inset, belty + inset,
                                itemWidth - (inset * 2), beltheight - (inset * 2), null);
                    }


                }
                else{
                    //graphically show which item is the selected item
                    gg.setColor(Color.yellow);
                    gg.fillRect(itemX, belty,
                            itemWidth, beltheight);

                    gg.setColor(a.getDefaultBuilding().getObjColour());
                    a.DrawBeltItem(gg,itemX + inset, belty + inset,
                            itemWidth - (inset * 2), beltheight - (inset * 2));

                    //Try to draw the image directionally but if fails check to ensure that up is initalized and default to up
                    //Draw the object going Down
                    if(a.getDefaultBuilding().getDown_Image()!=null&&
                            direction ==2
                    )
                        gg.drawImage(a.getDefaultBuilding().getDown_Image(),itemX + inset, belty + inset,
                                itemWidth - (inset * 2), beltheight - (inset * 2), null);

                        //Draw the object going Right
                    else if(a.getDefaultBuilding().getR_Image()!=null&&
                            direction == 1
                    ) {
                        gg.drawImage(a.getDefaultBuilding().getR_Image(), itemX + inset, belty + inset,
                                itemWidth - (inset * 2), beltheight - (inset * 2), null);
                    }
                    //Draw the object going LEFT
                    else if(a.getDefaultBuilding().getL_Image()!=null&&
                            direction==3
                    ) {
                        gg.drawImage(a.getDefaultBuilding().getL_Image(), itemX + inset, belty + inset,
                                itemWidth - (inset * 2), beltheight - (inset * 2), null);
                    }


                    //Draw the image inside the box as a representative
                    else if(a.getDefaultBuilding().getUp_Image()!=null){
                        gg.drawImage(a.getDefaultBuilding().getUp_Image(),itemX + inset, belty + inset,
                                itemWidth - (inset * 2), beltheight - (inset * 2), null);
                    }

                }
                    position++;

                itemX+=itemWidth;
            }
        }

        /**
         * This line only runs in the event that at the end of the belt list thtere is unfilled items in comparision to the
         * belt size
         */
        for(int i = position; i < beltItems; i++){

                gg.setColor(Color.lightGray);
                gg.fillRect(itemX+inset, belty+inset,
                        itemWidth-(inset*2),beltheight-(inset*2));
            itemX+=itemWidth;
        }
    }


    //given a 2d Graphics graw the object in reference to viewX and viewY of the MapView
    //Eg: object 0,0 and the mapview 1,1
    //draw the object @ -1,-1
    //Additionally scale the object in comparision to the width and height of the map view to allow objects to appear
    //to get bigger / smaller as the zoom changes
    @Override
    public void drawobj(Graphics2D gg, Map maps){
       //default to the regular drawobj if the desired image is not initalized

        if(super.getUp_Image()!=null && super.getDown_Image()!=null
                &&super.getL_Image()!=null&&super.getR_Image()!=null){

            //draw the object moving up if the movement is upwards
            if(super.getObjVSpeed()<0){

                    gg.drawImage(super.getUp_Image(),super.getPosX()-maps.getViewX(),
                            super.getPosY()- maps.getViewY(),
                            super.getObjWidth(), super.getObjHeight(),null);
            }
            //Draw the object downards if moving down
            else if(super.getObjVSpeed()>0){

                gg.drawImage(super.getDown_Image(),super.getPosX()-maps.getViewX(),
                        super.getPosY()- maps.getViewY(),
                        super.getObjWidth(), super.getObjHeight(),null);
            }

            //ONLY draw the image moving left/Right if the up/down motion is 0
            if(super.getObjVSpeed()==0){

                //Draw the image moving right
                if(super.getObjHSpeed()>0){
                    gg.drawImage(super.getR_Image(),super.getPosX()-maps.getViewX(),
                            super.getPosY()- maps.getViewY(),
                            super.getObjWidth(), super.getObjHeight(),null);
                }
                else //Draw the image moving left
                        gg.drawImage(super.getL_Image(),super.getPosX()-maps.getViewX(),
                                super.getPosY()- maps.getViewY(),
                                super.getObjWidth(), super.getObjHeight(),null);

            }

        }
        else{
            super.drawobj(gg,maps);
        }

    }


    /**
     * @param gg - graphics to draw on
     * Draw the health of the player
     */
    private void drawHealthBar(Graphics2D gg, Map maps){

        //this is the assumption of how large the player health can be
        int maxhealth = 100;
        int healthSizeIncreaser = 2;

        //The position of the health bar
        int x =  maps.getViewWidth() - (50+(maxhealth*healthSizeIncreaser));
        int y = 75;

        int height = 25;

        //draw the background for the health bar
        gg.setColor(Color. black);
        gg.drawRect(x,y,maxhealth*healthSizeIncreaser,height);

        //Draw the actual health
        gg.setColor(Color.red);
        gg.fillRect(x,y,super.getHealth()*healthSizeIncreaser,height);

        if(super.getObjColour().equals(Color.black))gg.setColor(Color.white);
        else{
            super.setObjColour(Color.black);
        }

        gg.setFont(new Font("TimesRoman", Font.PLAIN,
                (height/
                        (2*healthSizeIncreaser)
                )
                        +height
        ));

        gg.drawString(super.getName(), x, y+height);


    }

    /**
     * This is used to calculate if and to process the updating of the selcted belt item
     * @param e
     */
    public void BeltItemKeyEvent(KeyEvent e){
        if(e!=null) {
            int key = e.getKeyCode();

            if (beltList != null) {

                for (BeltSlot a : beltList) {

                    if (a != null
                            && key == a.getKeyCmd()) {
                        selectedItem = a;

                    }
                }
            }
        }
    }


    protected static void drawPlayers(Graphics2D gg, ArrayList<Player> list, Map maps, boolean calcmovment){

        //safety check
        if(list!=null){
            //Loop through each object in the arraylist
            for(Player a: list){
                //  a.setUp_Image(defaultCoal.getUp_Image());
                //only draw the object if it collides with the map to prevent unneccessary clutter
                if(a!=null&&
                        a.isCollision(maps.getViewX(),maps.getViewY(),maps.getMapWidth(),maps.getMapHeight())) {
                    int w = a.getObjWidth();
                    int h = a.getObjHeight();

                    if(calcmovment)a.calcMovement();

                    a.setObjHeight(h*maps.getTileHeight());
                    a.setObjWidth(w*maps.getTileWidth());

                    //then if they exceeded any border then loop them around the value
                    a.PlaneBorderTest(a, true, maps);

                    a.drawobj(gg, maps);

                    a.setObjWidth(w);
                    a.setObjHeight(h);
                }
            }
        }
    }



    /**
     * Given e handle any relevant action that should occur with the players
     * @param e - Keyevent
     *
     * calls ArrayList PlayerList<Plane>
     *
     * This will move the plane on a speed fom the planes default speed value
     *
     */
    public static void calcPlayerReleasedInput(KeyEvent e, ArrayList<Player> playerList){

        int key = e.getKeyCode();

        /**
         * Loop through each plane in the arraylist and handle the relevant action if any match each individuals list of actions
         */

        for(Player self: playerList){

            //UP Key
            if(self.getButtonUp()==key){
                //handle moving the plane / player in the requested direction
                //Move the player up by negativify an absolute of the default value
                self.setObjVSpeed(0);
            }

            else //DOWN Key
                if(self.getButtonDown()==key){
                    //handle moving the plane / player in the requested direction
                    //Move the player up by absolute of the default value

                    self.setObjVSpeed(0);
                }


                else //LEFT Key
                    if(self.getButtonLeft()==key){
                        //handle moving the plane / player in the requested direction
                        //Move the player left by negativify an absolute of the default value

                        self.setObjHSpeed(0);
                    }
                    else //RIGHT Key
                        if(self.getButtonRight()==key){
                            //handle moving the plane / player in the requested direction
                            //Move the player right an absolute of the default speed value

                            self.setObjHSpeed(0);
                        }

            self.calcMovement();
        }

    }



    public void setDirection(int direction) {
        this.direction = direction;

        if(direction>3)direction=0;//loop direction back around

    }

    /**
     * moves direction to the right
     */
    public void rotateRight(){
        direction++;
        if(direction>3)direction=0;
    }


    public ArrayList<BeltSlot> getBeltList() {
        return beltList;
    }

    public void setBeltList(ArrayList<BeltSlot> beltList) {
        this.beltList = beltList;
    }

    public BeltSlot getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(BeltSlot selectedItem) {
        this.selectedItem = selectedItem;
    }

    public int getButtonUp() {
        return buttonUp;
    }

    public void setButtonUp(int buttonUp) {
        this.buttonUp = buttonUp;
    }

    public int getButtonDown() {
        return buttonDown;
    }

    public void setButtonDown(int buttonDown) {
        this.buttonDown = buttonDown;
    }

    public int getButtonLeft() {
        return buttonLeft;
    }

    public void setButtonLeft(int buttonLeft) {
        this.buttonLeft = buttonLeft;
    }

    public int getButtonRight() {
        return buttonRight;
    }

    public void setButtonRight(int buttonRight) {
        this.buttonRight = buttonRight;
    }

    public int getButtonFire() {
        return buttonFire;
    }

    public void setButtonFire(int buttonFire) {
        this.buttonFire = buttonFire;
    }

    public int getButtonAltFire() {
        return buttonAltFire;
    }

    public void setButtonAltFire(int buttonAltFire) {
        this.buttonAltFire = buttonAltFire;
    }

    public int getDirection() {
        return direction;
    }
}
