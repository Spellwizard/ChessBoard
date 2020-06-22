package sample;

import javax.swing.*;

import java.awt.*;

import java.awt.event.*; //The class called to manage input that is then converted to output via awt

import java.util.ArrayList;


public class Main {

    public static JFrame frame = new JFrame();


    public Main(){

        Container c = frame.getContentPane();

        c.setBackground(Color.red);

        frame.setBackground(Color.yellow);

        frame.setLocationRelativeTo(null);

        GameCanvas program = new GameCanvas();

        frame.add(program);

        frame.setVisible(true);


    }
}
    class GameCanvas extends JComponent {

        private ArrayList<BackgroundImage> backgroundImageList = new ArrayList<>();

        //The Peice ArrayList eg: where the pawns, queens, knight objects are
        private ArrayList<Piece> peices_A = new ArrayList<>();

        //The GameMap Object is used and called a lot -
        //This object tracks the user's View, the grid, the tile sizes and counts
        //and depending on alterations underlying areas as well
        private Map gameMap;

        //The pc selected
        private Piece selected_Peice;

        /**
         * The mouse listener is used to activate various actions when the user should use the mouse
         */
        MouseListener ratListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            /**
             * This function will activate when a mouse button is pressed
             */
            public void mousePressed(MouseEvent e) {

            }

            @Override
            /**
             * Activates whenever a mouse button is released
             */
            public void mouseReleased(MouseEvent e) {


                //LEFT CLICK
                if(e.getButton() == 1) {

                    Piece removed = null;

                    SolidObject targetSquare;

                    //Determine if the click is on a peice
                    for (Piece item : peices_A) {

                        //Make a test object to simulate the actual dimensions on the window of
                        //where we are looking for mouse collision
                        targetSquare = new SolidObject(
                                item.getPosX() * gameMap.getTileWidth(),
                                item.getPosY() * gameMap.getTileHeight(),
                                item.getObjWidth(),
                                item.getObjHeight(),
                                null
                        );

                        //Use my fancy collision function to test for a simulation collision/ overlap
                        if (targetSquare.isCollision
                                (e.getX(), e.getY(), 1, 1)) {

                            //set the targeted peice as a new selected object if there isn't a current one
                            //or that the current selected_Piece and the targeted one share a isWhite value

                            if(selected_Peice==null ||
                                    (selected_Peice.isWhite()==item.isWhite())
                            ) {

                                //a new selected object
                                selected_Peice = item;

                                selected_Peice.resetColor();

                            }
                            else{
                                removed = item;
                            }
                        }


                    }

                    //Move Selected PC if it's on an empty spot
                    if(selected_Peice!=null){

                        //only remove the target peice if we
                        //1. already have a selected object
                        //2. the target object and the selected aren't the same (self destruction)
                        //3. the target object and selected aren't the same colour
                        if(
                                removed!=null
                                        &&
                                        removed!=selected_Peice
                                        &&
                                        selected_Peice.isWhite()!=removed.isWhite()
                        ){
                            peices_A.remove(removed);
                        }

                        selected_Peice.testMovement(e,gameMap);

                    }
                }

                //RIGHT CLICK
                if(e.getButton() == 3){

                    //Clear the selected Pc

                    if(selected_Peice!=null){
                        selected_Peice.resetColor();
                    }

                    selected_Peice = null;
                }


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

        KeyListener InputTracker = new KeyListener() {

            public void keyPressed(KeyEvent e) {

            }


            public void keyTyped(KeyEvent e){


            }

            public void keyReleased(KeyEvent e) {
            }

        };


        /**
         *
         */
        protected GameCanvas() {


            //The Game Map is used to describe the positions of everything, limits and the underlying grid

        gameMap = new Map(
                0,0,

                800,800,

                800,800,

                //Set the Tiles Sizes to an 8th of the window size
                100,
                100

                ,
                new Color(97, 77, 46, 255),
                new Color(25, 71, 28)
        );

        firstTimeinitialization();
    }


private void overrideGameValues(String fileName) {

    FileReader file = new FileReader(fileName);

    //Finally handle overriding the game cmd buttons
    OverridingValuesClass.OverrideGameCmds(file, false, false);

        }

        private void firstTimeinitialization() {

            //Initalize the ChessBoard
            peices_A = Piece.newCheckerBoard(gameMap);

            overrideGameValues("GameSettings.txt");

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
            this.addMouseListener(ratListener);

            this.setFocusable(true);
        }

        public void paintComponent(Graphics g) {



            if( this.getHeight()    !=  gameMap.getViewHeight()
                    ||
                    this.getWidth() !=  gameMap.getViewWidth()
                )
            gameMap.FitBoardToJFrame(this);

            Graphics2D gg = (Graphics2D) g;

            //Draw the background Grid
            gameMap.drawCheckerboard(gg);

            //Draw the Background Image
            gameMap.drawBackgroundImages(gg, backgroundImageList);

            //Special Draw the Highlighted player again
            if(selected_Peice!=null) {
                selected_Peice.resetColor();
                selected_Peice.highlighted_drawobj(gg,gameMap,selected_Peice.getObjColour());

            }

            //Draw the Board Peices
            Piece.drawArray(peices_A,gg,gameMap);


            if(selected_Peice!=null)devTools.drawScorebaord(gg, selected_Peice);

            }


        }