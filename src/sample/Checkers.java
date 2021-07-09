package sample;

import java.util.Scanner;

public class Checkers {

    //The arraysize and secondary to describe a 2d array for the board
    protected static int board_width = 8;
    protected static int board_height = 8;

    //A 2d board used to describe every position on the board with a character
    private char[][] board = new char[board_width][board_height];

    private char white = 'X';
    private char black = 'O';

    private char neutral = '-';

    private char[] alphabet = new char[26];

    //populate the char that will be used to populate the side positions
    protected void populateAlphabet(){

        int i = 0;
        alphabet[i++] = 'A';

        alphabet[i++] = 'B';
        alphabet[i++] = 'C';
        alphabet[i++] = 'D';
        alphabet[i++] = 'E';
        alphabet[i++] = 'F';
        alphabet[i++] = 'G';
        alphabet[i++] = 'H';
        alphabet[i++] = 'I';
        alphabet[i++] = 'J';
        alphabet[i++] = 'K';
        alphabet[i++] = 'L';
        alphabet[i++] = 'M';
        alphabet[i++] = 'N';
        alphabet[i++] = 'O';
        alphabet[i++] = 'P';
        alphabet[i++] = 'Q';
        alphabet[i++] = 'R';
        alphabet[i++] = 'S';
        alphabet[i++] = 'T';
        alphabet[i++] = 'U';
        alphabet[i++] = 'V';
        alphabet[i++] = 'W';
        alphabet[i++] = 'x';
        alphabet[i++] = 'Y';
        alphabet[i++] = 'Z';

    }

    //populate / reset the board
    protected void initBoard(){

        //initialize if it wasn't before
        board = new char[board_width][board_height];

        for(int i = 0; i <board_width; i++){

            for(int P = 0; P < board_height; P++){

                board[i][P] = neutral;

            }
        }

        //White
        addSet(0, true, 3, 'X');


        addSet(5, true, 3, 'O');
    }

    /**
     *
     * @param startingRow
     * @param startAt0
     * @param rowCount
     * @param piece
     */
    private void addSet(int startingRow, boolean startAt0,int rowCount,char piece){

        boolean start0 = startAt0;

        int starting_column = 0;


        for(int i = 0; i< rowCount; i++){

            if(start0)starting_column=0;
            else{
                starting_column=1;
            }

            addAltrow(starting_column, startingRow++, piece);

            start0 = !start0;
        }

    }


    /**
     *
     * @param starting_column the first starting position, generally to get alternating rows for each row use 0 or 1
     * @param row the row in the board to be updating
     * @param piece and finally the character representing the piece
     */
    private void addAltrow(int starting_column, int row, char piece){

        for(int i = starting_column; i< board_width; i+=2){

            board[row][i] = piece;

        }


    }


    /**
     * as safely as possible print the board row, and column
     */
    protected void printBoard(){

        int iterated_row = -1;

        if(board!=null){

            //print the column letters
            //and populate the char alphabet if not already done
            if(alphabet==null)populateAlphabet();

            System.out.print("  ");

            for(int i = 0; i<board_width; i++){
                System.out.print(" "+alphabet[i]);
            }

            System.out.println();

            //iterated through each row in the board
            for(char[] row : board){

                if(row!=null){

                    iterated_row++;
                    System.out.print(" "+iterated_row);

                    for(char position : row){

                        System.out.print(" "+position);

                    }


                }
                //now move the printing to a new row
                System.out.println();

            }


        }

    }

    /**
     * Use the terminal to get input information and translate to the pieces movement
     */
    protected String getPlayerInput(String Message){

        System.out.print(Message+" : ");

        Scanner response = new Scanner(System.in);

        return response.nextLine();

    }

    protected void getPlayerMove(boolean isWhiteTurn){

        System.out.println("Select Piece");
        String selectedRow = getPlayerInput("Row");
        String selectedColumn = getPlayerInput("Column");


        System.out.println("\nMove to square");
        String RowSquare = getPlayerInput("Row");
        String ColSquare = getPlayerInput("Column");

        System.out.println(
                calcPieceMove(
                FileReader.convertStringToInt(selectedRow),
                FileReader.convertStringToInt(selectedColumn),
                FileReader.convertStringToInt(RowSquare),
                FileReader.convertStringToInt(ColSquare)
        ));

        if(selectedRow.equals("break")){

        }
        else{

            printBoard();
            getPlayerMove(isWhiteTurn);

        }

    }

    /**
     *
     * @param selectedRow
     * @param selectedCol
     * @param targetRow
     * @param targetCol
     * @return false when an error with the input occurs or processing
     */
    protected boolean calcPieceMove(int selectedRow,int selectedCol,
                               int targetRow, int targetCol
                               ){

        //Do a bounds check on all the given variables to ensure we wont break anything
        if(
                (   selectedRow < 0 || selectedRow > board_width ) ||
                        (   selectedCol < 0 || selectedCol > board_height ) ||

                (   targetRow < 0 || targetRow > board_width ) ||
                     (   targetCol < 0 || targetCol > board_height )
        ){
            return false;
        }

        //Now we know the values are valid and viable for use

        //gotta shuffle some things but will end with an empty spot where the piece was an in the new spot
        board[targetCol][targetRow] = board[selectedCol][selectedRow];

        board[selectedCol][selectedRow] = neutral;

        return true;
    }

    public static int getBoard_width() {
        return board_width;
    }

    public static void setBoard_width(int board_width) {
        Checkers.board_width = board_width;
    }

    public static int getBoard_height() {
        return board_height;
    }

    public static void setBoard_height(int board_height) {
        Checkers.board_height = board_height;
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public char getWhite() {
        return white;
    }

    public void setWhite(char white) {
        this.white = white;
    }

    public char getBlack() {
        return black;
    }

    public void setBlack(char black) {
        this.black = black;
    }

    public char getNeutral() {
        return neutral;
    }

    public void setNeutral(char neutral) {
        this.neutral = neutral;
    }
}
