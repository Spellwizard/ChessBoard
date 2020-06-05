package sample;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Random;

/**
 * 9/9/19
 * Currently a bug is causing the program to not draw the miners
 *
 * the current progject is to implement working buildings and the miners is the first of said objects,
 * the next building to implement will be the conveyors then inserters then furnace, then assembler,
 *
 * next an integration of power for the power lines and the generator should be implented along with the pipe system
 */

import static java.lang.Math.*;

        public class Main {

            public static JFrame frame = new JFrame("Frog Version: July 31, 2019");


            public Main(){

                Container c = frame.getContentPane();
                c.setBackground(Color.red);

                frame.setBackground(Color.yellow);


                frame.setLocationRelativeTo(null);
                GameCanvas program = new GameCanvas();
                frame.add(program);
                frame.setVisible(true);

                //On Close of game window go back to the menu window
                frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Closing Window");

                FileReader ouputData = new FileReader("DataOutput.txt");

                for(String line: program.getDateOutput()){
                    ouputData.addLine(line);
                    ouputData.addLine("FUnnny");
                }

                Menu.makeVisible();
            }
        });
    }
}
    class GameCanvas extends JComponent {

        private Assembler defaultAssembler;
        private Conveyor defaultConveyor;
        private EightBuilding defaultEightBuilding;
        private ElectricPole defaultElectricPole;
        private MinerBuilding defaultMinerBuilding;
        private NinthBuilding defaultNinthBuilding;
        private Pipe defaultPipe;
        private SeventhBuilding defaultSeventhBuilding;
        private SteamGenerator defaultSteamGenerator;
        private Player defaultPlayer;

        private SolidObject defaultSolidObject;
        private MovingObject defaultMovingObject;
        private Plane defaultPlane;
        private Frog defaultFrog;

        //default list of ores
        private CopperOre defaultCopper;
        private IronOre defaultIron;
        private CoalOre defaultCoal;

        //General World Settings
        private boolean gamePaused = false; // this is a toggle for the 'p' button to pause all movement players and arrows at the time of creation but potentially enemies

        private boolean graphicsOn = true;

        private int initPopulationSize;//used as the beginning population of the frogs




        private int GameSpeed = 1;//each increase is in every loop is how many times it per count eg: if 2 then every 10 millseconds all calculations are run twice

        private boolean isDebug; //if true the score board function will display a lot of information

        private int pelletCount = 1; // the number of pellets added per round

        //framecount using maths can sorta be used to get seconds / minutes ect but can be out of sync due to program / hardware lag
        private int framecount=1; //the total count of all frames for the duration of the program running

        private int roundDuration= 2;//this is in minutes
        private int roundCount = 0;//this is the current round count

        private int tempRoundCount = 0;

        private Random random = new Random(); // called in various places; mostly used to get a random nuber in a range using nextInt()

        //use a variable size player list to allow for more players later on / to allow for some to die
        private  ArrayList<Frog> frogList;

        private ArrayList<Player> playerList;

        //this is the list of the 'food' items
        private ArrayList<Food> pelletList;

        /**
         * Dynamic arraylist of all the buildings that are currently built
         */
        private ArrayList<Building> buildingsList  = new ArrayList<>();

        private ArrayList<MinerBuilding> minerBuildingArrayList = new ArrayList<>();

        private ArrayList<Ore> oreList = new ArrayList<>();

        private ArrayList<DroppedItem> droppedItemsList;

        public ArrayList<String> getDateOutput() {
            return dateOutput;
        }

        public ArrayList<String> dateOutput = new ArrayList<String>();


        private ArrayList<BackgroundImage> backgroundImageList = new ArrayList<BackgroundImage>();

        //IMAGES FILE PATHS

        //The background images
        private  String backgroundFilePath;

        private BufferedImage BACKGROUNDIMAGE;

        //THE ACTUAL IMAGE OBJECT

        private BufferedImage currentBackground;

        private Map gameMap;

        private devTools developerTool = new devTools();

        /**
         * The mouse listener is used to activate various actions when the player / user should use the mouse
         */
        MouseListener ratListner = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                Building output = playerList.get(0).calcMouseClick(e, gameMap);

                if(output!=null
                        &&output.getR_Image()!=null
                ) {

                    if (buildingsList != null) {//always safety check

                        //only add the selected tile if the button click is the primary click
                        if (e.getButton() == 1) {
                            boolean isOverlapping = false;

                            //Loop through each existeing building to ensure no overlap
                            if(buildingsList!=null)for (Building a : buildingsList) {

                                //Create sam and BuildingA with the width and length not in pixels but in tiles
                                /**
                                 * Can't use collision becuase the collision detected secretly as a 1 pixel additional distance which
                                 * overlaps b/c the tiles are directly adjacent
                                 */
                                if (output.isCollision(a, gameMap)) {
                                    isOverlapping = true;
                                }
                            }
                            //only add the new building in the event that it doesn't overlap with any other buildings
                            if (!isOverlapping) {

                                buildingsList.add(output);

                                MinerBuilding SecondaryOutput = playerList.get(0).calcMouseMinerClick(e, gameMap);

                                if(SecondaryOutput!=null)minerBuildingArrayList.add(SecondaryOutput);

                            }
                        }
                    }
                }


                repaint();

            }

            /**
             * ToDo
             * use the functions mouse pressed and mouse released to store a starting tile and ending tile
             * from these two tiles attempt to place from the top left the maximum of the player selected building
             * such buildings list should then indidually be compared for the saefty functions eg: collision of buildings
             *
             * This should allow the player to place a set of objects in a rectangular space
             *
             * adding a player alt button possibly shift might allow for additional safety
             *
             * eg: draggin and relaseing the mouse will create either along the columns or rows
             * but if the shift key is currenlty pressed then the mouse should default as described above wherin it goes along both the columns and rows
             *
             * this may help faster building and more intutive design
             *
             * such design should attempt to on some level provide a visual output of the currenlty selected tiles first by showing
             * the rectangle and then later by showing graphically but softened to show they haven't been placed images of the selected object as they will be placed
             *
             */
            @Override
            /**
             * This function will activate when a mouse button is pressed
             */
            public void mousePressed(MouseEvent e) {


                Building output = playerList.get(0).calcMouseClick(e, gameMap);
                //otherwise if the button click was the 'left' or alt click then remove any items at the selected position
                if (e.getButton() == 3) {
                    //Stop stupid stuff and just set the widht and lenght to 1 pixel
                    output.setObjHeight(1);
                    output.setObjWidth(1);
                    if(buildingsList!=null)for (int i = 0; i < buildingsList.size(); i++) {

                        //remove any colliding objects from the building list that collide with the slected tile
                        /**
                         * Can't use collision becuase the collision detected secretly as a 1 pixel additional distance which
                         * overlaps b/c the tiles are directly adjacent
                         */
                        if (output.isCollision(buildingsList.get(i),gameMap)) {
                            buildingsList.remove(i);

                            //safety checks to stop index out of bounds errors
                            if (i < 0) i = 0;
                            if (i > buildingsList.size()) break;
                        }
                    }

                    if(minerBuildingArrayList!=null)for (int i = 0; i < minerBuildingArrayList.size(); i++) {

                        //remove any colliding objects from the building list that collide with the slected tile
                        /**
                         * Can't use collision becuase the collision detected secretly as a 1
                         * pixel additional distance which
                         * overlaps b/c the tiles are directly adjacent
                         */
                        if (output.isCollision(minerBuildingArrayList.get(i),gameMap)) {
                            minerBuildingArrayList.remove(i);

                            //safety checks to stop index out of bounds errors
                            if (i < 0) i = 0;
                            if (i > minerBuildingArrayList.size()) break;
                        }


                    }
                }
            }

            @Override
            /**
             * Activates whenever a mouse button is released
             */
            public void mouseReleased(MouseEvent e) {

                Building output = playerList.get(0).calcMouseClick(e, gameMap);
                //otherwise if the button click was the 'left' or alt click then remove any items at the selected position
                if (e.getButton() == 3) {
                    //Stop stupid stuff and just set the widht and lenght to 1 pixel
                    output.setObjHeight(1);
                    output.setObjWidth(1);
                    if(buildingsList!=null)for (int i = 0; i < buildingsList.size(); i++) {

                        //remove any colliding objects from the building list that collide with the slected tile
                        /**
                         * Can't use collision becuase the collision detected secretly as a 1 pixel additional distance which
                         * overlaps b/c the tiles are directly adjacent
                         */
                        if (output.isCollision(buildingsList.get(i),gameMap)) {
                            buildingsList.remove(i);

                            //safety checks to stop index out of bounds errors
                            if (i < 0) i = 0;
                            if (i > buildingsList.size()) break;
                        }
                    }

                    if(minerBuildingArrayList!=null)for (int i = 0; i < minerBuildingArrayList.size(); i++) {

                        //remove any colliding objects from the building list that collide with the slected tile
                        /**
                         * Can't use collision becuase the collision detected secretly as a 1
                         * pixel additional distance which
                         * overlaps b/c the tiles are directly adjacent
                         */
                        if (output.isCollision(minerBuildingArrayList.get(i),gameMap)) {
                            minerBuildingArrayList.remove(i);

                            //safety checks to stop index out of bounds errors
                            if (i < 0) i = 0;
                            if (i > minerBuildingArrayList.size()) break;
                        }


                    }
                }


                output = playerList.get(0).calcMouseClick(e, gameMap);

                if(output!=null
                        &&output.getR_Image()!=null
                ) {

                    if (buildingsList != null) {//always safety check

                        //only add the selected tile if the button click is the primary click
                        if (e.getButton() == 1) {
                            boolean isOverlapping = false;

                            //Loop through each existeing building to ensure no overlap
                            if(buildingsList!=null)for (Building a : buildingsList) {

                                //Create sam and BuildingA with the width and length not in pixels but in tiles
                                /**
                                 * Can't use collision becuase the collision detected secretly as a 1 pixel additional distance which
                                 * overlaps b/c the tiles are directly adjacent
                                 */
                                if (output.isCollision(a, gameMap)) {
                                    isOverlapping = true;
                                }
                            }
                            //only add the new building in the event that it doesn't overlap with any other buildings
                            if (!isOverlapping) {

                                buildingsList.add(output);
                            }
                        }
                    }
                }


                repaint();
            }

            @Override
            /**
             * This function will trigger with the first position of the mouse as it enters the window
             */
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            /**
             * This function will trigger with the last position of the mouse as it exits the window
             */
            public void mouseExited(MouseEvent e) {
            }
        };

        /**
         * The InputTracker is used to track keyboard actions as both listed under the developer commands and the
         * various commands of the players in the player lists
         * ToDo
         * I would like to see a comination of commands eg: shift + r to reverese the direction of rotation
         *
         */
        KeyListener InputTracker = new KeyListener() {

            public void keyPressed(KeyEvent e) {
                calcPlayerInput(e);

                /**
                 * This function is given e
                 * @param e - a keyboard input
                 * Then test the keyboard button against the dev buttons and activate various commands as needed
                 * @param graphicsOn
                 * @param gameMap
                 * @param gamePaused
                 * @param Keycmd_PauseGame
                 * @param Keycmd_repopulateFood
                 * @param Keycmd_ToggleGraphics
                 * @param Keycmd_IncreaseSpeed
                 * @param Keycmd_DecreaseSpeed
                 */
                developerTool.calcCommands(e,graphicsOn,gamePaused,gameMap, playerList);


                int key = e.getKeyCode();
            }
            public void keyTyped(KeyEvent e){


            }

            public void keyReleased(KeyEvent e) {
                Player.calcPlayerReleasedInput(e,playerList);
            }

        };

        private void InitializeDefaultValues() {
            Color defaultC = new Color(50,50,50);

            System.out.println("InitializeDefaultValues");

            defaultAssembler = new Assembler(0,0,0,0,defaultC,
                    gameMap, null,null,null,null, false);

            defaultConveyor = new Conveyor(0,0,0,0,defaultC,
                    gameMap, null,null,null,null, false);

             defaultEightBuilding = new EightBuilding(0,0,0,0,defaultC,
                     gameMap, null,null,null,null, false);

              defaultElectricPole = new ElectricPole(0,0,0,0,defaultC,
                      gameMap, null,null,null,null, false);

             defaultMinerBuilding = new MinerBuilding(0,0,0,0,defaultC,
                     gameMap, null,null,null,null, false);

             defaultNinthBuilding = new NinthBuilding(0,0,0,0,defaultC,
                     gameMap, null,null,null,null, false);

             defaultPipe = new Pipe(0,0,0,0,defaultC,
                     gameMap, null,null,null,null, false);

             defaultSeventhBuilding = new SeventhBuilding(0,0,0,0,defaultC,
                     gameMap, null,null,null,null, false);

             defaultSteamGenerator = new SteamGenerator(0,0,0,0,defaultC,
                     gameMap, null,null,null,null, false);

             defaultSolidObject = new SolidObject(0,0,0,0,defaultC);
             defaultMovingObject = new MovingObject(0,0,0,0,0,0);

            defaultPlayer = new Player(0,0,0,0,0,0,
                    0,0,0,0,0,0,0,
                    "",0,0,0,0,null);

            //default ORE initalizations prior to reading files
            defaultCoal = new CoalOre(0,0,0,0,defaultC,
                    null, null,null,null);
            defaultIron = new IronOre(0,0,0,0,defaultC,
                    null, null,null,null);
            defaultCopper = new CopperOre(0,0,0,0,defaultC,
                    null, null,null,null);


            //THE BLUE PLAYER FILE PATH
           // blueJetFilePath = "BlueJet.png";
            //blueJetFlipPath = "BlueJetFlipped.png";

            //The background image
            backgroundFilePath = "";

            //General World Settings
            gamePaused = false; // this is a toggle for the 'p' button to pause all movement players and arrows at the time of creation but potentially enemies

            initPopulationSize = 1;

            //the following must have additional lines for additional built players



            isDebug = false;

            //GROUND TROOP VALUES
            //use a variable size player list to allow for more players later on / to allow for some to die
            frogList = new ArrayList<>();

            playerList = new ArrayList<>();

            buildingsList = new ArrayList<>();

            pelletList = new ArrayList<>();

            droppedItemsList = new ArrayList<>();

            graphicsOn = true;

            int roundDuration= 2;//this is in minutes
        }

        /**
         *
         */
        protected GameCanvas() {

        gameMap = new Map(0,0,400,400,
                10000,10000, 40,40
                , new Color(150, 75, 0),
                new Color(160, 75, 0)
        );

        InitializeDefaultValues();

        populateDefaultOres();
        System.out.println("GameCanvas: "+(defaultCoal.getUp_Image()!=null));

        //inialize the ore for the gamemap
        for(Ore a: gameMap.populateOre(defaultCoal, 10, 10, 20, 20))oreList.add(a);

        for(Ore a: gameMap.populateOre(defaultCopper, 100, 10, 20, 20))oreList.add(a);

        for(Ore a: gameMap.populateOre(defaultIron, 10, 100, 20, 20))oreList.add(a);

        System.out.println("GameCanvas");

        gamePaused = false;

        populateDefaultVariables();

        firstTimeinitialization();
    }


private void overrideGameValues(String fileName) {

    System.out.println("\noverrideGameValues\n");

    FileReader file = new FileReader(fileName);

    //Overrride all the player values
    OverrideAllPlayerValues(playerList);

    //Finally handle overriding the game cmd buttons
    OverridingValuesClass.OverrideGameCmds(file, pelletCount, gamePaused, isDebug,
            graphicsOn, roundDuration, initPopulationSize, developerTool.getKeycmd_IncreaseSpeed(),
            developerTool.getKeycmd_DecreaseSpeed(),developerTool.getKeycmd_repopulateFood(),
            developerTool.getKeycmd_ToggleGraphics(),developerTool.getKeycmd_StepRound(),
            developerTool.getKeycmd_repopulateFood()
            );
}


        /**
         * @param list This is at time of creation the player list which is referenced against internal values:
         * KeyBoard Inputs: buttonUP, buttonDown, buttonLeft, buttonRight ,buttonFire, buttonAltFire
         * Player Values: Height, Width, VSpeed (and sets the default), HSpeed (and sets the default), health, name (entirely for role play)
         *  Player reference values: FIRECOOLDOWN, BOMBCOOLDOWN, DefaultProjectileHeight, DefaultProjectileWidth
         *
         *  and will safely loop through and override if any such values are found
         *
         *  This looping allows for easy changes to the numbers of players without having to add additional code but
         *             means that to set a value it will look for buttonUp = 'Player_' + (the position, yes from 0)+'buttonUp'
         *             so as a whole it might set the 3rd players buttonAltFire = 'Player_2_buttonAltFire'
         */

        private void OverrideAllPlayerValues(ArrayList<Player> list) {

            System.out.println("OverrideAllPlayerValues");


            FileReader file = new FileReader("Players Model\\Player_0\\playersettings.txt");
            String temp = "";
            String players_folder = "Players Model\\Player_";
            String fileName = "playersettings.txt";
            String type = "Player";



            for (int position = 0; position< list.size();position++) {

                Player self = list.get(position);

               // file.setFileName(players_folder+position+"/"+fileName);
               // file.setFileName(fileName+position+"\\"+fileName);

                file.setFileName("Players Model\\Player_0\\playersettings.txt");

                file.setFileFolder("Players Model\\Player_0\\");

                System.out.println("OverridingPlayer: "+file.getFileName());
            }


            basePopulatePlayers(1,playerList,"OverrideAllPlayerValues");
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
        private void calcPlayerInput(KeyEvent e){

            int key = e.getKeyCode();


            /**
             * Loop through each plane in the arraylist and handle the relevant action if any match each individuals list of actions
             */

            if(playerList!=null)for(Player self: playerList) {

                if (self != null) {

                    //calcplayer belt selection changes
                    self.BeltItemKeyEvent(e);

                    //UP Key
                    if (self.getButtonUp() == key) {
                        //handle moving the plane / player in the requested direction
                        //Move the player up by negativify an absolute of the default value

                        self.setObjVSpeed(
                                -abs(self.getDefaultVSpeed()
                                ));

                    } else //DOWN Key
                        if (self.getButtonDown() == key) {
                            //handle moving the plane / player in the requested direction
                            //Move the player up by absolute of the default value

                            self.setObjVSpeed(
                                    abs(self.getDefaultVSpeed()
                                    ));
                        } else //LEFT Key
                            if (self.getButtonLeft() == key) {
                                //handle moving the plane / player in the requested direction
                                //Move the player left by negativify an absolute of the default value

                                self.setObjHSpeed(
                                        -abs(self.getDefaultHSpeed()
                                        ));
                            } else //RIGHT Key
                                if (self.getButtonRight() == key) {
                                    //handle moving the plane / player in the requested direction
                                    //Move the player right an absolute of the default speed value

                                    self.setObjHSpeed(
                                            abs(self.getDefaultHSpeed()
                                            ));
                                } else //FIRE Key
                                    if (self.getButtonFire() == key) {
                                        self.rotateRight();

                                    } else //ALT FIRE Key
                                        if (self.getButtonAltFire() == key) {

                                        }
                    self.calcMovement();
                }
            }
        }




        private void intializeImages() {

            BACKGROUNDIMAGE = null;
            currentBackground = null;
        }

        private BufferedImage imageGetter(String filePathName) {
            try {

                return ImageIO.read(new File(filePathName));
            } catch (IOException e) {


                System.out.println(e.toString());
            }
            return null;
        }

        private void firstTimeinitialization() {

            System.out.println("firstTimeinitialization");


            //use prebuilt values, make players and put them into the frogList arrayList

            String temp = "PlayerCount";
            FileReader file = new FileReader("GameSettings.txt");

            int value = 0;

            if (!file.findValue(temp).equals("")
                    && file.convertStringToInt(file.findValue(temp)) != -1)
                value =
                        file.  convertStringToInt(file.findValue(temp))
                ;


            overrideGameValues("GameSettings.txt");

            intializeImages();

            //make sure that the window will actually listen for inputs
            initListeners();

           Thread animationThread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        repaint();
                        try {
                            Thread.sleep(10);
                        } catch (Exception ex) {
                        }
                    }
                }
            });
            animationThread.start();
        }

        public void initListeners() {
            this.addKeyListener(InputTracker);
            this.addMouseListener(ratListner);

            this.setFocusable(true);
        }




        public void paintComponent(Graphics g) {

            int frameSeconds =0; //calculates the seconds per the frame count
            int frameMinutes = 0; // calculates the minutes based on the seconds which come from the frame count

            Graphics2D gg = (Graphics2D) g;

            gameMap.setViewHeight(this.getHeight());
            gameMap.setViewWidth(this.getWidth());

            gameMap.updatePosition(playerList.get(0));

            gameMap.drawCheckerboard(gg);

            gameMap.drawBackgroundImages(gg, backgroundImageList);

            //stop the program doing anything when the program is paused

                //loop the game per regular cycle timers the game speed which means that there is a functional fast forward button
                for(int i = 0;i<GameSpeed;i++) {

                    //Only draw each object if the graphics are on and only calculate the movmenet if the game is not paused

                    if(graphicsOn) {
                        /**
                         * The order that these following lines are very important
                         * this is the order that things are drawn,
                         * the last on on the list gets to draw over everyone else and thus will appear if overlapping with another object
                         */

                        Ore.drawOre(gg,oreList,gameMap);

                        Food.drawFood(gg, pelletList, gameMap);
                        Building.drawBuildings(gg, buildingsList, gameMap);

                        Frog.drawFrog(gg, frogList, gameMap, !gamePaused);

                        Player.drawPlayers(gg, playerList, gameMap, !gamePaused);

                        playerList.get(i).drawUI(gg, gameMap);

                        if(!isDebug)developerTool.drawScorebaord(gg, framecount, frameSeconds, frameMinutes
                        , roundCount, graphicsOn, gameMap, minerBuildingArrayList, buildingsList,
                                oreList, playerList, frogList
                                ,defaultMinerBuilding

                        );
                    }

                    framecount++;

                    frameSeconds = (framecount / 60); //calculates the seconds per the frame count
                    frameMinutes = frameSeconds / 60; // calculates the minutes based on the seconds which come from the frame count

                    //NEW ROUND
                    /**
                     * Given RoundDuration calculate when a new round occurs
                     *
                     * eg: round duration = 2 //it's in minutes
                     * everytime the frame minutes is evenly divisble into it then start a roun
                     */
                    try {

                        if (roundCount < frameMinutes / roundDuration
                            //|| noMovesLeft
                        ) {
                            roundCount++;
                            calcNewRound();
                        }
                    } catch (Exception e) {
                    }

                    //this function along with subfunctions handles collisions between most objects
                    calcCollisions();

                    repaint();
    }


            }



        public void calcNewRound(){
     //this is run to populate a new round
    }

/**
 * This function calculates and handles the                    collisions for the following arrayLists:
 *
 * This function should be called before any objects are drwawn
 * Explosions: explosionList
* Frog: frogList
* GroundFighters: groundFighters
* Projectiles: projectileList
*/
protected void calcCollisions(){
    //calcPlaneOnPlaneCollision(frogList, frogList);
    if(pelletList!=null&& frogList!=null)calcPlaneOnFoodCollision(frogList,pelletList);
    calcBuildingCollisions();
}

        protected void calcBuildingCollisions(){
    if(buildingsList!=null&&droppedItemsList!=null){
    //Compare each building against the list of moving objects
    //this is going to typically be any dropped items (dropped items do include items on conveyors)
    //additionally this will be the player too
    for(Building building: buildingsList){

        //Calc collisions with dropped items
        //this is primarly (entirely) used by the conveyor class
        for(DroppedItem droppedItem: droppedItemsList){

            if(building.isCollision(droppedItem,gameMap)){
                building.calcCollsion(droppedItem);
            }
        }

        /**
         * Calculate a collision of a building against the player
         * When this heppens the player should bounce off the building unless it is a solid building
         */
        for(Player player: playerList){
            if(building.isCollision(player,gameMap)){

                if(building.isSolid()) {
                    //TOP
                    if (player.isCollision(
                            building.getPosX(), building.getPosY(), building.getObjWidth(), 1, gameMap
                    )) {
                        player.setPosY(
                                building.getPosY() -
                                        player.getObjHeight()
                        );
                    } else if (
                        //BOTTOM
                            player.isCollision(
                                    building.getPosX(),
                                    (building.getPosY()+building.getObjWidth())-1,
                                    building.getObjWidth(),
                                    1, gameMap
                            )
                    ) {
                        player.setPosY(
                                building.getPosY() + building.getObjHeight()

                        );
                    }
                    //LEFT
                    if (player.isCollision(
                            building.getPosX(),
                            building.getPosY(),
                            1,
                            building.getObjHeight(), gameMap
                    )) {
                        player.setPosX(
                                building.getPosX()
                                        -
                                        player.getObjWidth()
                        );
                    } else if (
                        //RIGHT
                            player.isCollision(
                                    (building.getPosX()+building.getObjWidth())-1,
                                    building.getPosY(),
                                    1,
                                    building.getObjHeight(), gameMap
                            )
                    ) {

                        player.setPosX(
                                building.getPosX() + building.getObjWidth()+1
                        );

                    }

                }

                else{
                    building.calcCollsion(player);
                }

            }
        }
    }
  }
    calcMinerCollisions();
}

protected void calcMinerCollisions(){
    if(minerBuildingArrayList!=null&&oreList!=null){
        //Compare each building against the list of moving objects
        //this is going to typically be any dropped items (dropped items do include items on conveyors)
        //additionally this will be the player too
        for(MinerBuilding miner: minerBuildingArrayList) {


            //calculate collisions with ores
            //used by the miners
            //Compares the default Miner Building against the current iterated object to ensure only miner class objects are called
            for (Ore a : oreList) {
                if (miner.isCollision(a,gameMap)
                ) {
                    if (miner.calcMining()) {
                        System.out.println("New Ore");
                    }
                }
            }
        }
    }
}


protected void calcPlaneOnFoodCollision(ArrayList<Frog> list, ArrayList<Food> gList) {

    //loop through list safely
    for (int a = 0; a < list.size(); a++) {

        //loop through gList safely to allow for deletions in looping
        for (int b = 0; b < gList.size(); b++) {

//use the isCollision function to find if these two objects in the two array list are colliding
if (
                list.get(a).isCollision(gList.get(b),gameMap)) {

    //Handle when a collision is found with list.get(a) & gList.get(b)

    list.get(a).increaseFoodSupply(1);//add a food counter to the frog

    gList.remove(b);//remove the food pellet
    b--; //offset for the removal

    //finally safety check to break looping if the marker has exceeded the usable positions
    if (b < 0 || b > gList.size()) break;
}
}
}}


        /**
         * This is typically called when the gamemap size needs to be updated
         * typically the gamemap tiles sizes are being updated
         */
        public void updateSets(){
            defaultMinerBuilding.setGameMap(gameMap);

              defaultAssembler.setGameMap(gameMap);
              defaultConveyor.setGameMap(gameMap);
              defaultEightBuilding.setGameMap(gameMap);
              defaultElectricPole.setGameMap(gameMap);

              defaultMinerBuilding.setGameMap(gameMap);
             defaultNinthBuilding.setGameMap(gameMap);

           defaultSeventhBuilding.setGameMap(gameMap);
            defaultSteamGenerator.setGameMap(gameMap);
/*
            defaultFrog.setGameMap(gameMap);
            defaultPipe;.setGameMap(gameMap);
            defaultSolidObject.setGameMap(gameMap);
            defaultMovingObject.setGameMap(gameMap);
            defaultPlane.setGameMap(gameMap);
            defaultPlayer.setGameMap(gameMap);*/

            //default list of ores
              defaultCopper.setGameMap(gameMap);
              defaultIron.setGameMap(gameMap);
              defaultCoal.setGameMap(gameMap);




        }



        /**
         *
         * @return an arraylist containing the defaulted belt list of the player's belt
         */
        private ArrayList<BeltSlot> intializeBelt(boolean popdefaultvalues){

            System.out.println("intializeBelt");

        if(popdefaultvalues)populateDefaultVariables();

        ArrayList<BeltSlot> list = new ArrayList<>();

        list.add(new BeltSlot( 49, new Color(255, 15, 0), defaultAssembler,false));

        list.add(new BeltSlot(50, new Color(57, 255, 0), defaultMinerBuilding,true));

        list.add(new BeltSlot(51, new Color(255, 24, 217) , defaultConveyor,false));

        list.add(new BeltSlot(52, new Color(70, 255, 184),defaultSteamGenerator,false ));

        list.add(new BeltSlot(53, new Color(109, 121, 255), defaultElectricPole,false));

        list.add(new BeltSlot(54, new Color(37, 0, 255),defaultPipe,false));

        list.add(new BeltSlot(55, new Color(179, 245, 255),defaultSeventhBuilding,false));

        list.add(new BeltSlot(56, new Color(255, 255, 0),defaultEightBuilding,false));

        list.add(new BeltSlot(57, new Color(255, 114, 44), defaultNinthBuilding,false));

        populateDefaultVariables();

        return list;
        }

    /**
     KeyBoard Inputs: buttonUP, buttonDown, buttonLeft, buttonRight ,buttonFire, buttonAltFire
     Player Values: Height, Width, VSpeed (and sets the default), HSpeed (and sets the default), health, name (entirely for role play)
     Player reference values: FIRECOOLDOWN, BOMBCOOLDOWN, DefaultProjectileHeight, DefaultProjectileWidth
     * @param count - the amount of players to be added
     * @param list - the list of which to add them to
     */
    private void basePopulatePlayers(int count, ArrayList<Player> list, String calledby){
        System.out.println("basePopulatePlayers: "+ calledby);

        System.out.println("Playervalue successful: "+ (defaultPlayer!=null));


        int x = gameMap.getMapWidth()/2;
        int y = gameMap.getMapHeight()/2;

        String name = "PLAYER";

        //Default KeyBoard values
        int defaultUp = 0;
        int defaultDown = 0;
        int defaultLeft =0;
        int defaultRight = 0;
        int defaultFire = 69;
        int defaultAltFire = 81;

        //Default Plane values
        int width = 1;
        int height = 3;
        int VSpeed = 1;
        int HSpeed = 1;
         int health = 50;

        //Reference Values
        int fireCool = 0;
        int bombCool = 0;

        int projHeight = 100;
        int projWidth = 100;

        ArrayList<BeltSlot> beltList = intializeBelt(true);

            Player player = new Player(
                    x,y,width,height,HSpeed,VSpeed,
                    defaultUp,defaultDown,defaultLeft,defaultRight
                        ,defaultFire,defaultAltFire,health, name
                    ,fireCool,bombCool, projHeight,projWidth
                    ,beltList);

            FileReader file = new FileReader("Players Model\\Player_0\\playersettings.txt");
            file.setFileFolder("Players Model\\Player_0\\");


            OverridingValuesClass.OverridePlayer(defaultPlayer,file);

            System.out.println("\nDefaultOverClass: "+(defaultPlayer!=null)+"\n");
            System.out.println("Safely overriden player values: "+OverridingValuesClass.OverridePlayer(player,defaultPlayer));

            playerList.add(player);

}



/**
 * This function will call various sub functions to populate the default objects
 * the default objects are pre populated to store the various values of images and sizes, colours, ect
 * to prevent serious load issues on calling each objet as the function caries out its duties
 */
    private void populateDefaultVariables(){
    populateDefaultBuildings();
    populateDefaultOres();
    }


/**
 *         private Assembler defaultAssembler;
 *         private Conveyor defaultConveyor;
 *         private EightBuilding defaultEightBuilding;
 *         private ElectricPole defaultElectricPole;
 *         private MinerBuilding defaultMinerBuilding;
 *         private NinthBuilding defaultNinthBuilding;
 *         private Pipe defaultPipe;
 *         private SeventhBuilding defaultSeventhBuilding;
 *         private SteamGenerator defaultSteamGenerator;
 *
 *         private SolidObject defaultSolidObject;
 *         private MovingObject defaultMovingObject;
 *
 *         private Plane defaultPlane;
 *         private Frog defaultFrog;
 */
protected void populateDefaultBuildings(){
    System.out.println("populateDefaultBuildings");

    FileReader file = new FileReader("");

    String fileFolder = "";

    String fileName = "buildingSettings.txt";

    String fileType = "";

    //Buildings
    fileFolder="Buildings\\";

    //Assembler
    fileType = "Assembler\\";

    file = new FileReader("Buildings\\Assembler\\buildingSettings.txt");
    file.setFileFolder("Buildings\\Assembler\\");
    OverridingValuesClass.OverrideAssembler(defaultAssembler,file);
    defaultAssembler.setGameMap(gameMap);

    //Conveyor
    fileType = "Conveyor\\";

    System.out.println(fileFolder+fileType+fileName);
    file.setFileFolder(fileFolder+fileType);
    file.setFileName(fileFolder+fileType+fileName);
    OverridingValuesClass.OverrideConveyor(defaultConveyor,file);
    defaultConveyor.setGameMap(gameMap);

    //defaultElectricPole
    fileType = "ElectricPole\\";

    file.setFileName(fileFolder+fileType+fileName);
    file.setFileFolder(fileFolder+fileType);
    OverridingValuesClass.OVerrideElectricPole(defaultElectricPole,file);
    defaultElectricPole.setGameMap(gameMap);

    //defaultMinerBuilding
    fileType = "MinerBuilding\\";

    file.setFileName(fileFolder+fileType+fileName);
    file.setFileFolder(fileFolder+fileType);
    OverridingValuesClass.OverrideMinerBuilding(defaultMinerBuilding,file);
    defaultMinerBuilding.setGameMap(gameMap);

    System.out.println("MinerBuilding Class: "+ defaultMinerBuilding.getClass());

    //defaultPipe
    fileType = "Pipe\\";

    file.setFileName(fileFolder+fileType+fileName);
    file.setFileFolder(fileFolder+fileType);
    OverridingValuesClass.OverridePipe(defaultPipe,file);
    defaultPipe.setGameMap(gameMap);

    //defaultSteamGenerator
    fileType = "SteamGenerator\\";

    file.setFileName(fileFolder+fileType+fileName);
    file.setFileFolder(fileFolder+fileType);
    OverridingValuesClass.OVerrideSteamGenerator(defaultSteamGenerator,file);
    defaultSteamGenerator.setGameMap(gameMap);


    //defaultSeventhBuilding
    fileType = "SeventhBuilding\\";

    file.setFileName(fileFolder+fileType+fileName);
    file.setFileFolder(fileFolder+fileType);
    OverridingValuesClass.OverrideSeventBuilding(defaultSeventhBuilding,file);
    defaultSeventhBuilding.setGameMap(gameMap);

    //EightBuilding
    fileType = "EightBuilding\\";

    System.out.println(fileFolder+fileType+fileName);
    file.setFileFolder(fileFolder+fileType);
    file.setFileName(fileFolder+fileType+fileName);
    OverridingValuesClass.OverrideEightBuilding(defaultEightBuilding,file);
    defaultEightBuilding.setGameMap(gameMap);

    //defaultNinthBuilding
    fileType = "NinthBuilding\\";

    file.setFileName(fileFolder+fileType+fileName);
    file.setFileFolder(fileFolder+fileType);
    OverridingValuesClass.OverrideNinthBuilding(defaultNinthBuilding,file);
    defaultNinthBuilding.setGameMap(gameMap);

}

/**
 * GameMap MUST be initalized before this is called
 * && the default Ores MUST BE inialized
 */
private void populateDefaultOres() {
    System.out.println("populateDefaultOres");

    FileReader Ofile = null;
    /*
    String fileType = "";
    String fileFolder = "ore\\";
    String fileName = "OreSettings.txt";
    */
    //COAL
    Ofile = new FileReader("ore\\coal\\OreSettings.txt");
    Ofile.setFileFolder("ore\\coal\\");
    OverridingValuesClass.OverrideSolidObject(defaultCoal, Ofile);
    defaultCoal.setGameMap(gameMap);

    //COPPER
    Ofile = new FileReader("ore\\copper\\OreSettings.txt");
    Ofile.setFileFolder("ore\\copper\\");
    OverridingValuesClass.OverrideSolidObject(defaultCopper, Ofile);
    defaultCopper.setGameMap(gameMap);

    //IRON
    Ofile = new FileReader("ore\\iron\\OreSettings.txt");
    Ofile.setFileFolder("ore\\iron\\");
    OverridingValuesClass.OverrideSolidObject(defaultIron, Ofile);
    defaultIron.setGameMap(gameMap);

}


        }