package sample;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

        private ArrayList<BackgroundImage> backgroundImageList = new ArrayList<BackgroundImage>();

        //IMAGES FILE PATHS

        //THE ACTUAL IMAGE OBJECT

        private Map gameMap;

        //private devTools developerTool = new devTools();

        /**
         * The mouse listener is used to activate various actions when the player / user should use the mouse
         */
        MouseListener ratListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                repaint();

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
                new Color(255, 255, 255),
                new Color(0, 0, 0)
        );

        firstTimeinitialization();
    }


private void overrideGameValues(String fileName) {

    FileReader file = new FileReader(fileName);

    //Finally handle overriding the game cmd buttons
    OverridingValuesClass.OverrideGameCmds(file, false, false);

        }

        private void firstTimeinitialization() {

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

            Graphics2D gg = (Graphics2D) g;

            gameMap.setViewHeight(this.getHeight());
            gameMap.setViewWidth(this.getWidth());

            gameMap.drawCheckerboard(gg);

            gameMap.drawBackgroundImages(gg, backgroundImageList);


            }


        }