package sample;


import java.awt.*;

/**
 * a set of functions to compare a piece to a desired move and return the various legality
 *
 * For example if a piece is set to 'diagonal_move = true' then it would call the diagonal move prior to making a move to check for legality
 */
public class LEGALMOVE {

    /**
     * IMPORTANT CONSIDERATIONS:
     * WHITE Forward is up and towards y -> 0
     *
     * BLACK Forward is down and towards y -> +∞ or y -> windowHeight
     *
     * NO PIECE is ALLOWED:
     *
     * TOP Bounds
     * Y < 0 (partially or entirely off the top of the screen)
     *
     * LEFT Bounds
     * X < 0 (partially or entirely off the left of the screen)
     *
     * BOTTOM Bounds
     * Y + Piece Height > MapView Height (partially or entirely off the bottom of the screen)
     *
     * RIGHT Bounds
     * X + Piece Height > MapView Width (partially or entirely off the right of the screen)
     */

    /**
     *
     * @param newMoveTile object describing the new tile the piece is being asked towards
     * @param piece - the piece that is the object wanting to be moved to the new position
     * @param map - The Map that the pieces are currently all on and describes the entire size of the game board
     * @return true when the movement described is of diagonal from the piece position
     */
    protected static boolean Diagonal_Movement(
            SolidObject newMoveTile, Piece piece, Map map){

        //basic safety check to ensure all objects provided are safe to use
        if(newMoveTile==null|| piece==null || map == null) return false;

        Piece currentPosPiece = piece.convertRowCol_XY(map);

        //ROWS
        int row_actualRange = Math.abs( newMoveTile.getPosX()) - Math.abs(currentPosPiece.getPosX() );
        int rowdistance = Math.abs(row_actualRange / map.getTileWidth());


        //COL
        int actualRange = newMoveTile.getPosY() - currentPosPiece.getPosY();
        int coldistance = Math.abs(actualRange / map.getTileHeight());


        if(rowdistance == coldistance){

            return true;
        }

        return false;
    }

    /**
     *
     * @param newMoveTile object describing the new tile the piece is being asked towards
     * @param piece - the piece that is the object wanting to be moved to the new position; Note the X/Y must be actual on the screen
     *              basically ensure the object position isn't described via col/rows
     * @param map - The Map that the pieces are currently all on and describes the entire size of the game board
     * @return true
     */
    protected static boolean Diagonal_MovementRanged(
            SolidObject newMoveTile, Piece piece, Map map, int range){

        //basic safety check to ensure all objects provided are safe to use
        if(newMoveTile==null|| piece==null || map == null) return false;

        piece.printXY("PC");

        //Create a converted Piece with an X,Y representing the col/row
        SolidObject currentPosPiece = map.convertSolidObjectRowCol_to_XY_Tiles(piece);

        //ROWS
        //Row Range = the absolute value distance from the new position - the current position
        int row_actualRange = Math.abs( newMoveTile.getPosX()) - Math.abs(currentPosPiece.getPosX() );

        int rowdistance = Math.abs(row_actualRange / map.getTileWidth());


        //COL
        int actualRange = newMoveTile.getPosY() - currentPosPiece.getPosY();

        int coldistance = Math.abs(actualRange / map.getTileHeight());

        //Once we have converted the movement to a number of columns+- and rows+-
        //All we have to do to ensure it is a diagonal movement is make sure that the column movement matches the row movement
        //and then the other thing is to dbl check the range and ensure we aren't exceeding
        //Now the b/c col = row we only have to check one is within the range and then both are


        if(rowdistance == coldistance
                &&
                (rowdistance <=range
                )
        ){

            return true;
        }

        return false;


    }


    /**
     *
     *
     *
     *
     * @param selected_piece  piece - the piece that is the object wanting to be moved to the new position
     * @param target_Move  object describing the new tile the piece is being asked towards
     * @param map - The Map that the pieces are currently all on and describes the entire size of the game board
     * @return true if the movement taken is 'up' relative to colour eg: Black is down, White is up
     */
    protected static boolean moveTowardsEnemyLine(
            SolidObject target_Move, Piece selected_piece, Map map){

        //basic safety check to ensure all objects provided are safe to use
        if(target_Move==null|| selected_piece==null || map == null) return false;


        Piece piece = selected_piece;
        piece.setPosX(selected_piece.getPosX() / map.getTileWidth());
        piece.setPosY(selected_piece.getPosY() / map.getTileHeight());



        SolidObject newMoveTile = new SolidObject(1,1,
                target_Move.getObjWidth(),target_Move.getObjHeight(), target_Move.getObjColour());

        newMoveTile.setPosX(target_Move.getPosX() / map.getTileWidth());
        newMoveTile.setPosY(target_Move.getPosY() / map.getTileHeight());

        //WHITE

        System.out.println("Pc: "+piece.getPosY()+" : "+ newMoveTile.getPosY());

        if(piece.isWhite()){

            if(
                    piece.getPosY() > newMoveTile.getPosY()
            ){

                return true;

            }

        }

        //BLACK
        else{

            if(
                    piece.getPosY() < newMoveTile.getPosY()){

                return true;

            }


        }

        return false;
    }

    /**
     *HORIZONTAL : LEFT / RIGHT
     * @param newMoveTile object describing the new tile the piece is being asked towards
     * @param piece - the piece that is the object wanting to be moved to the new position
     * @param map - The Map that the pieces are currently all on and describes the entire size of the game board
     * @param range the by grid position distance the piece is allowed to move eg: up to 3 columns left/right
     * @return true if the described new position is left / right of the current and within the move distance
     */
    protected static boolean Lateral_HorizontalMovement_Ranged(
            SolidObject newMoveTile, Piece piece, Map map, int range){

        //Use the Lateral Horizontal move to ensure everything is a legal (safe) function
        //along with a check to ensure we are only reviewing a move that is horizonally sound
        if(!LEGALMOVE.Lateral_HorizontalMovement(newMoveTile,piece,map))return false;

        //I had comments -
        //Basically take the new position and the old X's and divide then divide by map width
        //the take that absolute value compared to the range given

        int actualRange = Math.abs( newMoveTile.getPosX()) - Math.abs(piece.getPosX() );

        int rowdistance = actualRange / map.getTileWidth();

        if(
                Math.abs(rowdistance) <= range

        ){
            return true;
        }

        //This line is only met when the range is violated
        return false;
    }

    /**
     *VERTICAL : UP / DOWN
     * @param newMoveTile object describing the new tile the piece is being asked towards
     * @param piece - the piece that is the object wanting to be moved to the new position
     * @param map - The Map that the pieces are currently all on and describes the entire size of the game board
     * @param range the by grid position distance the piece is allowed to move eg: up to 3 columns left/right
     * @return true if the described new position is left / right of the current and within the move distance
     */
    protected static boolean Lateral_VerticalMovement_Ranged(
            SolidObject newMoveTile, Piece piece, Map map, int range){

        //Use the Lateral Horizontal move to ensure everything is a legal (safe) function
        //along with a check to ensure we are only reviewing a move that is vertically sound
        if(!LEGALMOVE.Lateral_VerticalMovement(newMoveTile,piece,map))return false;

        //Basically take the new position and the old Y's and divide then divide by map height
        //the take that absolute value compared to the range given

        int actualRange = newMoveTile.getPosY() - piece.getPosY();

        int coldistance = actualRange / map.getTileHeight();

        if(
                Math.abs(coldistance) <= range

        ){
            return true;
        }

        //This line is only met when the range is violated
        return false;
    }

    //LATERAL MOVEMENT -

    /**
     *HORIZONTAL : LEFT / RIGHT
     * @param newMoveTile object describing the new tile the piece is being asked towards
     * @param piece - the piece that is the object wanting to be moved to the new position
     * @param map - The Map that the pieces are currently all on and describes the entire size of the game board
     *
     * @return true if the described new position is left / right of the current piece
     */
    protected static boolean Lateral_HorizontalMovement(
            SolidObject newMoveTile, Piece piece, Map map){

        //basic safety check to ensure all objects provided are safe to use
        if(newMoveTile==null|| piece==null || map == null) return false;

        Piece currentPosPiece = piece.convertRowCol_XY(map);

        //Just use the SolidObject inbuilt function to ensure the movement is valid

            //and double check they aren't on different rows
            if(
                            newMoveTile.getPosY() == currentPosPiece.getPosY()
            ){

                return true;
            }


        //else its not
        return false;
    }

    /**
     *VERTICAL : LEFT / RIGHT
     * @param newMoveTile object describing the new tile the piece is being asked towards
     * @param piece - the piece that is the object wanting to be moved to the new position
     * @param map - The Map that the pieces are currently all on and describes the entire size of the game board
     *
     * @return true if the described new position is left / right of the current piece
     */
    protected static boolean Lateral_VerticalMovement( SolidObject newMoveTile, Piece piece, Map map){

        //basic safety check to ensure all objects provided are safe to use
        if(newMoveTile==null|| piece==null || map == null) return false;

        Piece currentPosPiece = piece.convertRowCol_XY(map);

        //Then it's as easy as comparing coords

        if(
                newMoveTile.getPosX() == currentPosPiece.getPosX()
        ){

            return true;
        }


        //else its not
        return false;
    }

    /**
     *
     * @param newMoveTile object describing the new tile the piece is being asked towards
     * @param piece - the piece that is the object wanting to be moved to the new position
     * @param map - The Map that the pieces are currently all on and describes the entire size of the game board
     * @return true only if the piece has reached the opposite end row corresponding to color
     *  EG: White wants the top row, Black wants the bottom row
     * @return also return false if anything is null or if the mouse x or y are below 0
     *
     */
    protected static boolean endLine_Transform(SolidObject newMoveTile, Piece piece, Map map){

        //basic safety check to ensure all objects provided are safe to use
        if(newMoveTile==null|| piece==null || map == null) return false;

        //Now differentiate by color

        if(piece.isWhite()){
            //WHITE

            //assuming the top row is on 0, we can do a seemingly simple check for a y =0

            if(newMoveTile.getPosY() ==0){

                return true;

            }

        }
        else{
            //BLACK

            //With the assumption the TileList in order lists a 2D grid,
            //Compare the postion -1 of that array's first object Y position
            //To the desired position and if they match then the final row has been met
            if(
                    (   newMoveTile.getPosY() + newMoveTile.getObjHeight()  )
                    ==
                            (
                                    map.getTileHeight() * map.getTileList().get(
                                            map.getTileList().size()-1
                                    ).size()
                                    )
            ){
                return true;
            }


        }

        return false;
    }


    //TODO KNIGHT PRANCE

    /**
     *HORIZONTAL : LEFT / RIGHT
     * @param newMoveTile object describing the new tile the piece is being asked towards
     * @param piece - the piece that is the object wanting to be moved to the new position
     * @param map - The Map that the pieces are currently all on and describes the entire size of the game board
     *
     * @return true if the move is valid for a horse 'Knight'
     */
    protected static boolean HorseyPrance_Knight(

            SolidObject newMoveTile, Piece piece, Map map) {

        //basic safety check to ensure all objects provided are safe to use
        if (newMoveTile == null || piece == null || map == null) return false;

        Piece currentPosPiece = piece.convertRowCol_XY(map);

        //ROWS
        int row_actualRange = Math.abs(newMoveTile.getPosX()) - Math.abs(currentPosPiece.getPosX());
        int rowdistance = Math.abs(row_actualRange / map.getTileWidth());


        //COL
        int actualRange = newMoveTile.getPosY() - currentPosPiece.getPosY();
        int coldistance = Math.abs(actualRange / map.getTileHeight());

        //Now the heart and soul - make sure that either column
        //movement is +- 2 and row is +-1 or col is +-1 and row is +-2

        if (coldistance == 2 && rowdistance == 1) return true;
        if (coldistance == 1 && rowdistance == 2) return true;


        //else its not
        return false;
    }


}
