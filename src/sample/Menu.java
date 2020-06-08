package sample;

import javax.swing.*;

//This is the class doing the heavy lifting so i can have a window
import java.awt.*;

    //This program is used to listen for mouse / key clicks and respond
    import java.awt.event.*;

    //This is mostly used once to show the Menu's Background Image but it helps for polish
    import java.awt.image.BufferedImage;


public class Menu{

    //make the class and start the initial sheet construction needed

    public static MenuSub frame = new MenuSub("Game Menu");

    /**
     * Program used to help with the transition in weird flip back when closing out another window,
     * Mostly a failed experiement a couple times and left for sentimental value
     */
   protected static void makeVisible(){

       frame.betterVisible();
   }

    public static void main(String[]args){

        //Set the Menu Window's Name that appears on the top of the screen
        frame.setTitle("Program Main Menu");

        //make sure the window will stop program on closing window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //make the window visible
        frame.setVisible(true);

        }

    }

/**
 *
 */
class MenuSub
        extends JFrame
        implements ActionListener
                {

    //The Window Object for the Options Menu
    OptionsMenu options_Menu = null;

    //The Window Object for the Options Menu
    AboutWindow About_Menu = null;

    //JFrame program for the important program
    JFrame GameJFrame = null;


    private Button SGame = new Button("Start Program");

    String BackgroundPath = "MenuBackground.jpg";

    private BufferedImage BackgroundImage;


    //make a new container to have the subitems
    Container MenuButtonsContainer = new Container();

    public void makeVisible()
        {this.setVisible(true);}


    public MenuSub(String windowName){
        //This is def important
        BackgroundImage=FileReader.imageGetter(BackgroundPath);

        //Gotta set the window Name
        this.setName(windowName);

        //Def gotta init, super important (?)
        this.init();

    }
    public void betterVisible(){
        this.makeVisible();
    }

    /**
     * used on making a new sheet class object
     */
    private void init(){

        //Set the Menu Window's Background Colour
        this.setBackground(Color.BLACK);

        //Set the Menu's Width / Length
        this.setSize(800,800);

        //funciton used to add the various buttons, listeners, ect
        MainMenu();

        //Redraw the elements on the screen to ensure the added elements show up
        repaint();

    }

    @Override
    public void paint(Graphics g){

        //i don't remember but StackOverflow said this would solve an issue that i can't remember..
        super.paintComponents(g);

        super.paint(g);

        //We do want to ensure the background Menu Image is still their.
        if(BackgroundImage!=null) {
            g.drawImage(BackgroundImage, 0, 0, null);
        }

    }

    /**
     * Actually add the various components needed but mostly buttons
     * it should clear the window and set the mainmenu up with its buttons and the varoius addons like listeners
    */
    private void MainMenu(){

        //the result should be the remaining space that won't be taken up by the buttons and thus can be divided by the number of buttons
        //and used as the y offset
        int ButtonYOffset = 40;


        //This is used for some 'fancy' math but must be updated if additional buttons are added/ removed
        int MainMenuButtonCount = 4;

        //The Button's width in pixels
        int ButtonWidth = 250;

        //the buttons height in pixels
        int ButtonHeight = 60;

        //The starting Y position of the Buttons
        int ButtonYPos = (
                getHeight() -   (   ButtonHeight    +   ButtonHeight)   )
                                    -
                (MainMenuButtonCount * (ButtonHeight + ButtonYOffset))
                ;

        //The X coordinate for all the button positions below
        int ButtonXPos = (570)
        -ButtonWidth;

        //Button Font
        Font bFont = new Font("TimesRoman", Font.PLAIN, 25);

        //Menu buttons

        //Create each Button along with setting certain values -
        //visibility, background Color, the actionlistener and the font

        //Start Game button 'Play'
        //set a code to check when any buttons are clicked

        SGame.setActionCommand("SGame");
        SGame.setVisible(true);
        SGame.setBackground(Color.GREEN); // set the background color of the button
        // Bconnect.setForeground(Color.WHITE); // set the text color of the button
        SGame.addActionListener(this);
        SGame.setFont(bFont);



        //Program Options
        Button BOptions = new Button("Programs Options");

        BOptions.setActionCommand("option");
        BOptions.setVisible(true);
        BOptions.setBackground(Color.orange); // set the background color of the button
        BOptions.addActionListener(this);
        BOptions.setFont(bFont);



        //Program About
        Button BAbout = new Button("About Programs");

        BAbout.setActionCommand("BAbout");
        BAbout.setVisible(true);
        BAbout.setBackground(Color.blue); // set the background color of the button
        BAbout.setForeground(Color.white); // set the text color of the button
        BAbout.addActionListener(this);
        BAbout.setFont(bFont);



        //Program Quit
        //make the quit button
        Button BQuit = new Button("Quit Program");
        //set a code to check when any buttons are clicked
        BQuit.setActionCommand("BQuit");
        BQuit.addActionListener(this);
        BQuit.setVisible(true);
        BQuit.setBackground(Color.RED);
        BQuit.setForeground(Color.WHITE);
        BQuit.setFont(bFont);


        //Set the Button's Width + Height along with the X axis
        //Then through each Button increment along with the ButtonYOffset + ButtonHeight
            //to ensure aligned positioning and reasonably spaced

        //Start Game
        SGame.setBounds(
                ButtonXPos,ButtonYPos,
                ButtonWidth, ButtonHeight);

            //Increment the Button Position Y to the next button position
            ButtonYPos=(ButtonYPos+ButtonYOffset+ButtonHeight);


        //Program options
        BOptions.setBounds(
                ButtonXPos, ButtonYPos,
                ButtonWidth, ButtonHeight);

            //Increment the Button Position Y to the next button position
            ButtonYPos=(ButtonYPos+ButtonYOffset+ButtonHeight);


        BAbout.setBounds(
                ButtonXPos, ButtonYPos,
                ButtonWidth, ButtonHeight);

            //Increment the Button Position Y to the next button position
            ButtonYPos=(ButtonYPos+ButtonYOffset+ButtonHeight);


        //Exit Program
        BQuit.setBounds(ButtonXPos, ButtonYPos,
                ButtonWidth, ButtonHeight);



        //add all the buttons to the Container
        MenuButtonsContainer.add(SGame);
        MenuButtonsContainer.add(BOptions);
        MenuButtonsContainer.add(BAbout);
        MenuButtonsContainer.add(BQuit);

        //Make sure the container will be visible
        MenuButtonsContainer.setVisible(true);

        //Set a background for the container
        MenuButtonsContainer.setForeground(Color.black);


        //And Actually add the container to the Menu Window for display
        this.add(MenuButtonsContainer);

    }


    //this can be used to check if a button, check box or the like is used
    public void actionPerformed(ActionEvent ae){

        //Get the string related to the action performed
        String action = ae.getActionCommand();

        //QUIT
        if(action.equals("BQuit")){

            //QUIT PROGRAM
            System.exit(0);

        }

        //START
        else if(action.equals("SGame")){

            //Set the GameJFrame to a new ProgramWindow
            //Based on present design at this hand off the Java File 'Main' takes primary control until program is ended
            GameJFrame = makeNewGameWindow();

            this.setVisible(false); // hides the menu window

        }

        //OPTIONS
        else if(action.equals("option")){

            //instantiate a new options menu program
            options_Menu = new OptionsMenu("Options Window");

            //Setup Closure to flip back to the MainMenu
            options_Menu.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {

                    //Make the Menu Visible again
                    Menu.makeVisible();

                    //And close out the options menu entirely
                    //This is important as options Menu deals a lot with files and we don't want things left messy
                    options_Menu= null;

                }
            });

                //make the Options Window visible
                options_Menu.setVisible(true);

                //Hide the Menu, for now :)
                this.setVisible(false);
        }

        //ABOUT
        else if(action.equals("BAbout")){

            //instantiate an about menu
            About_Menu = new AboutWindow("About Window");

            //On Close of game window go back to the menu window
            About_Menu.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {

                    //Make the Menu Visible again
                    Menu.makeVisible();

                    //And close out the options menu entirely
                    //This is important as options Menu deals a lot with files and we don't want things left messy
                    options_Menu= null;

                }
            });

            //make the window visible
            About_Menu.setVisible(true);

            //Hide the Menu, for now :)
            this.setVisible(false);
        }

        }//END OF ACTION PERFORMED

    /**
     * Setup the Game Window and return it as a JFrame construct
     * @return the game window
     */
        public JFrame makeNewGameWindow(){

                //Always gotta start with a new program
                JFrame frame = new JFrame();

                //Get your basic container
                Container c = frame.getContentPane();

                //Set background colors b/c i'm an interior decorator(?)
                c.setBackground(Color.lightGray);

                //Make sure to set a basic window size to ensure it's obvious a window was opened
                frame.setSize(900,900);

                //wtf does this do?
                frame.setLocationRelativeTo(null);

                //gotta add this container, for reasons? i'm not sure this container is used
                frame.add(new GameCanvas());

                //I keep making everything visible like i want my windows naked? confused
                frame.setVisible(true);

                //On Close of game window go back to the menu window
                frame.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent e) {

                        //Gotta trade back to the Menu
                        Menu.makeVisible();

                    }

                });

                //Finally return the frame for fiendish purposes only
                return frame;
                }


    }




