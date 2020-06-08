package sample;

public class OverridingValuesClass {

    /*
     * The purpose of this class is two fold:
     * To help with clutter in 'Main.java' class
     * and in general to be the class that actually overrides values as provided by files
     * eg: getting pictures of objects and reading object settings
     * Additionally storing a default value of all objects will be useful b/c it will stop unneccessary calls for pictures / reading files
     * Which can have significant effect on performance especially if the user is repeatitly creating objects
     */

    /**
     * SolidObjects
     *  posX - left @ 0
     *  posY - left @ 0
     *  objWidth - Override by looking for 'objWidth'
     *  objHeight - Override by looking for 'objHeight'
     *  objColour - not yet able to override, will implement feature in feature
     *  UpModel - override object image looking up
     *  DownModel - override object image looking up
     *  LeftModel - override object image looking up
     *  RightModel - override object image looking up
     *
     * @param nut given a SolidObject or child read from the filereader
     * @param fileReader read the file reader for certain values as documented above and override values
     * @return false if either nut or filereader was not initalized
     */
    protected static boolean OverrideSolidObject(SolidObject nut , FileReader fileReader   ){
        String temp;
        if(nut!=null&&fileReader!=null) {

            temp = "objWidth";

            if (!fileReader.findValue(temp).equals("")
                    && fileReader.convertStringToInt(fileReader.findValue(temp)) != -1)
                nut.setObjWidth(
                        fileReader.convertStringToInt(fileReader.findValue(temp))
                );

            System.out.println("objWidth: "+fileReader.convertStringToInt(fileReader.findValue(temp)));

            temp = "objHeight";

            if (!fileReader.findValue(temp).equals("")
                    && fileReader.convertStringToInt(fileReader.findValue(temp)) != -1)
                nut.setObjHeight(
                        fileReader.convertStringToInt(fileReader.findValue(temp))
                );

            System.out.println("objHeight: "+fileReader.convertStringToInt(fileReader.findValue(temp)));

            //This is prone to returning errors

            temp = "UpModel";
            System.out.println(fileReader.getFileFolder()+fileReader.findActualValue(temp));

            if (!fileReader.findActualValue(temp).equals(""))
                nut.setUpImage(fileReader.getFileFolder()+fileReader.findActualValue(temp));

            System.out.println("OverrideSolidObject UpModel: '"+  fileReader.getFileFolder()+fileReader.findActualValue(temp)+"';");

            temp = "DownModel";
            System.out.println(fileReader.getFileFolder()+fileReader.findValue(temp));
            if (!fileReader.findValue(temp).equals(""))
                nut.setDownImage(fileReader.getFileFolder()+fileReader.findValue(temp));
            System.out.println("OverrideSolidObject DownModel: "+fileReader.getFileFolder()+fileReader.findValue(temp));

            temp = "LeftModel";
            System.out.println(fileReader.getFileFolder()+fileReader.findValue(temp));
            if (!fileReader.findValue(temp).equals(""))
                nut.setLeftImage(fileReader.getFileFolder()+fileReader.findValue(temp));
            System.out.println("OverrideSolidObject LeftModel: "+fileReader.getFileFolder()+fileReader.findValue(temp));

            temp = "RightModel";
            System.out.println(fileReader.getFileFolder()+fileReader.findValue(temp));
            if (!fileReader.findValue(temp).equals(""))
                nut.setRightImage(fileReader.getFileFolder()+fileReader.findValue(temp));
            System.out.println("OverrideSolidObject RightModel: "+fileReader.getFileFolder()+fileReader.findValue(temp));

            return true;//overriding worked
        }
        else{
            return false;//overriding failed due to non instatiated object / filereader
        }
    }
    /**
     * SolidObjects
     *  posX - left @ 0
     *  posY - left @ 0
     *  objWidth - Override by looking for 'objWidth'
     *  objHeight - Override by looking for 'objHeight'
     *  objColour - not yet able to override, will implement feature in feature
     *
     * @param nut given a SolidObject or child read from the filereader
     * @param defaultObject - override 'nut's values with the default values of the default object
     * @return false if either nut or filereader was not initalized
     */
    protected static boolean OverrideSolidObject(SolidObject nut , SolidObject defaultObject   ){

        if(nut!=null&&defaultObject!=null) {

            nut.setObjWidth(defaultObject.getObjWidth());

            nut.setObjHeight(defaultObject.getObjHeight());

            nut.setObjColour(defaultObject.getObjColour());
/**
            nut.setPosX(defaultObject.getPosX());

            nut.setPosY(defaultObject.getPosY());
 */

                //This is prone to errors
            if(defaultObject.getUp_Image()!=null){
                    nut.setUp_Image(defaultObject.getUp_Image());
                }

            if(defaultObject.getDown_Image()!=null){
                nut.setDown_Image(defaultObject.getDown_Image());
            }

            if(defaultObject.getL_Image()!=null){
                nut.setL_Image(defaultObject.getL_Image());
            }

            if(defaultObject.getR_Image()!=null){
                nut.setR_Image(defaultObject.getR_Image());
            }

            return true;

        }
        return false;
    }

    /**
     * MovingObjects
     *  defaultHSpeed & HSpeed sets the current H speed and stores the original H speed as default for calling later
     *  defaultVSpeed & VSpeed sets the current V speed and stores the oringinal V speed as defualt for caling later
     *
     * @param nut given a SolidObject or child read from the filereader
     * @param defaultObject - override 'nut's values with the default values of the default object
     * @return false if either nut or defaultObject was not initalized
     */
    protected static boolean OverrideMovingObject(MovingObject nut , MovingObject defaultObject   ){

        if(OverrideSolidObject(nut , defaultObject    )) {

            nut.setObjVSpeed(defaultObject.getDefaultVSpeed());
            nut.setDefaultVSpeed(defaultObject.getDefaultVSpeed());

            nut.setObjHSpeed(defaultObject.getDefaultHSpeed());
            nut.setDefaultHSpeed(defaultObject.getDefaultHSpeed());

            return true;
        }
        return false;
    }

    /**
     * MovingObjects
     *  defaultHSpeed & HSpeed sets the current H speed and stores the original H speed as default for calling later
     *  defaultVSpeed & VSpeed sets the current V speed and stores the oringinal V speed as defualt for caling later
     *
     * @param nut given an object or child read from the filereader
     * @param fileReader read the file reader for certain values as documented above and override values
     * @return false if either nut or filereader was not initalized
     */
    protected static boolean OverrideMovingObject(MovingObject nut , FileReader fileReader   ){
        String temp;
        if(OverrideSolidObject(nut , fileReader    )) {

            temp = "VSpeed";

            if (!fileReader.findValue(temp).equals("")
                    && fileReader.convertStringToInt(fileReader.findValue(temp)) != -1) {
                nut.setObjVSpeed(
                        fileReader.convertStringToInt(fileReader.findValue(temp))
                );
                nut.setDefaultVSpeed(
                        fileReader.convertStringToInt(fileReader.findValue(temp))
                );
            }

            temp = "HSpeed";

            if (!fileReader.findValue(temp).equals("")
                    && fileReader.convertStringToInt(fileReader.findValue(temp)) != -1) {
                nut.setObjHSpeed(
                        fileReader.convertStringToInt(fileReader.findValue(temp))
                );
                nut.setDefaultHSpeed(
                        fileReader.convertStringToInt(fileReader.findValue(temp))
                );
            }

            return true;
        }

        return false;

    }


    /**
     * Look from the file and override some game cmd values like pausing
     * @param file - File that the values are looked for
     * @param gamePaused
     * @param isDebug
     */
    protected static void OverrideGameCmds(FileReader file
                                , boolean gamePaused,
                                  boolean isDebug

    ) {

        String temp = "";

        //BOOLEAN OVERRIDE VALUES

        //gamePaused
        temp = file.findValue("gamePaused");
        //check to make sure that there is a value
        if (!temp.equals("")) {

            //check to see if the value is true
            if (temp.equals("true")) {
                gamePaused = true;
            }

            //check to see if the value is false
            if (temp.equals("false")) {
                gamePaused = false;
            }
        }

        //isdebug
        temp = file.findValue("isdebug");
        //check to make sure that there is a value
        if (!temp.equals("")) {

            //check to see if the value is true
            if (temp.equals("true")) {
                isDebug = true;
            }

            //check to see if the value is false
            if (temp.equals("false")) {
                isDebug = false;
            }
        }

    }


}
