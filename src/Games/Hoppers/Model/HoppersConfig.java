package Games.Hoppers.Model;

import Solvers.BFS.BFS;
import Solvers.BFS.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Games.Hoppers configuration, build hoppers config based on user input
 *
 * @author Quan Quy
 */
public class HoppersConfig implements Configuration{
    private final int numRows;
    private final int numColumns;
    private final char[][] board;
    private int row;
    private int col;

    /**
     * read in the file and its information to create board
     * @param arg name file
     * @throws IOException throw exception if file cant be read
     */
    public HoppersConfig(String arg) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(arg));
        String inString = in.readLine();
        String[] stringsIn = inString.split(" ");
        this.numRows = Integer.parseInt(stringsIn[0]);
        this.numColumns = Integer.parseInt(stringsIn[1]);
        this.board = new char[numRows][numColumns];

        //create the board
        for (int row=0; row<numRows; ++row) {
            for (int col=0; col<numColumns; ++col) {
                char cell = (char) in.read();
                this.board[row][col] = cell;
                in.read();
            }
        }
        in.close();
        this.row = 0;
        this.col = 0;
    }

    /**
     * get this board
     * @return board[][]
     */
    public char[][] getBoard() {
        return this.board;
    }

    /**
     * get number of rows of the board
     * @return number if rows
     */
    public int getNumRows(){
        return this.numRows;
    }

    /**
     * get number of columns of the board
     * @return number of columns
     */
    public int getNumColumns(){
        return this.numColumns;
    }

    /**
     * create copy of hopper config, help make successors
     * @param other config
     * @param isRight make east move
     * @param isLeft west move
     * @param isUp north move
     * @param isDown south move
     * @param upLeft south west move
     * @param upRight north east
     * @param downLeft south west
     * @param downRight south east
     */
    private HoppersConfig(HoppersConfig other, boolean isRight, boolean isLeft, boolean isUp, boolean isDown,
                          boolean upLeft, boolean upRight, boolean downLeft, boolean downRight){
        this.row = other.row;
        this.col = other.col;
        this.numColumns = other.numColumns;
        this.numRows = other.numRows;
        this.board = new char[other.numRows][other.numColumns];
        //copy
        for (int r = 0; r < other.numRows; r++) {
            System.arraycopy(other.board[r], 0, this.board[r], 0, other.numColumns);
        }
        if (isRight){
            char temp = this.board[row][col];
            this.board[row][col] = '.';
            this.board[row][col+2] = '.';
            this.board[row][col+4] = temp;
        }
        else if (isLeft){
            char temp = this.board[row][col];
            this.board[row][col] = '.';
            this.board[row][col-2] = '.';
            this.board[row][col-4] = temp;
        }
        else if (isUp){
            char temp = this.board[row][col];
            this.board[row][col] = '.';
            this.board[row-2][col] = '.';
            this.board[row-4][col] = temp;
        }
        else if (isDown){
            char temp = this.board[row][col];
            this.board[row][col] = '.';
            this.board[row+2][col] = '.';
            this.board[row+4][col] = temp;
        }
        else if (upLeft){
            char temp = this.board[row][col];
            this.board[row][col] = '.';
            this.board[row-1][col-1] = '.';
            this.board[row-2][col-2] = temp;
        }
        else if (upRight){
            char temp = this.board[row][col];
            this.board[row][col] = '.';
            this.board[row-1][col+1] = '.';
            this.board[row-2][col+2] = temp;
        }
        else if (downLeft){
            char temp = this.board[row][col];
            this.board[row][col] = '.';
            this.board[row+1][col-1] = '.';
            this.board[row+2][col-2] = temp;
        }
        else if (downRight){
            char temp = this.board[row][col];
            this.board[row][col] = '.';
            this.board[row+1][col+1] = '.';
            this.board[row+2][col+2] = temp;
        }
    }


    /**
     * generate successors, 4 for odd coordinates and 8 for even coordinates
     * @return successors list
     */
    @Override
    public Collection<Configuration> getSuccessors(){
        List<Configuration> successors = new LinkedList<>();
        this.col=0;
        this.row=0;
        while (this.col != this.numColumns && this.row != this.numRows) {
            if (this.board[this.row][this.col]=='G' || this.board[this.row][this.col]=='R'){
                //ODD COORDINATES
                //look up left
                if((this.row >= 2) && (this.col >= 2) && this.board[row-2][col-2]=='.'&& (this.board[row-1][col-1] == 'G')){
                    successors.add(new HoppersConfig(this, false, false, false, false, true, false, false, false));
                }
                //look up right
                if((this.row >= 2) && (this.col < this.numColumns - 2) && this.board[row-2][col+2]=='.'&& (this.board[row-1][col+1] == 'G')){
                    successors.add(new HoppersConfig(this, false, false, false, false, false, true, false, false));
                }
                //look down left
                if((this.row < this.numRows - 2) && (this.col >= 2) && this.board[row+2][col-2]=='.'&& (this.board[row+1][col-1] == 'G')){
                    successors.add(new HoppersConfig(this, false, false, false, false, false, false, true, false));
                }
                //look down right
                if((this.row < this.numRows - 2) && (this.col < this.numColumns - 2) && this.board[row+2][col+2]=='.'&& (this.board[row+1][col+1] == 'G')){
                    successors.add(new HoppersConfig(this, false, false, false, false, false, false, false, true));
                }
                //IF EVEN COORDINATES
                if((this.row%2)==0 && (this.col%2)==0){
                    //look right
                    if((this.col < this.numColumns - 4) && this.board[row][col+4]=='.'&& (this.board[row][col+2]=='G')){
                        successors.add(new HoppersConfig(this, true, false, false, false, false, false, false, false));
                    }
                    //look left
                    if((this.col >= 4) && this.board[row][col-4]=='.'&& (this.board[row][col-2]=='G')){
                        successors.add(new HoppersConfig(this, false, true, false, false, false, false, false, false));
                    }
                    //look up
                    if((this.row >= 4) && this.board[row-4][col]=='.'&& (this.board[row-2][col]=='G')){
                        successors.add(new HoppersConfig(this, false, false, true, false, false, false, false, false));
                    }
                    //look down
                    if((this.row < this.numRows - 4) && this.board[row+4][col]=='.'&& (this.board[row+2][col]=='G')){
                        successors.add(new HoppersConfig(this, false, false, false, true, false, false, false, false));
                    }
                }
            }
            // advance cursor to next available
            this.col += 1;
            if (this.col == this.numColumns) {
                this.row += 1;
                this.col = 0;
            }
        }
        return successors;
    }

    /**
     * move the frog
     * @param coordinates 4 coordinates, first 2 are row and column for the frog started with,
     *                    last 2 are for the destination
     */
    public int move(int[] coordinates){
        //even coordinates
        if((coordinates[0]%2)==0 && (coordinates[1]%2)==0){
            //if they're on the same line horizontal or vertical
            if(((coordinates[0]==coordinates[2])||(coordinates[1]==coordinates[3]))
                    &&((Math.abs(coordinates[0]-coordinates[2])==4) || (Math.abs(coordinates[1]-coordinates[3])==4))
                    &&(this.board[coordinates[2]][coordinates[3]]=='.')
                    &&(this.board[(coordinates[0]+coordinates[2])/2][(coordinates[1]+coordinates[3])/2] == 'G')){
                char temp = this.board[coordinates[0]][coordinates[1]];
                this.board[coordinates[0]][coordinates[1]] = '.';
                this.board[(coordinates[0]+coordinates[2])/2][(coordinates[1]+coordinates[3])/2] = '.';
                this.board[coordinates[2]][coordinates[3]] = temp;
                return 1;
            }
        }
        //move diagonal
        if ((coordinates[0]!=coordinates[2])&&(coordinates[1]!=coordinates[3])
                &&(Math.abs(coordinates[0]-coordinates[2])==2)&&(Math.abs(coordinates[1]-coordinates[3])==2)
                &&(this.board[coordinates[2]][coordinates[3]]=='.')
                &&(this.board[(coordinates[0]+coordinates[2])/2][(coordinates[1]+coordinates[3])/2] == 'G')){
            char temp = this.board[coordinates[0]][coordinates[1]];
            this.board[coordinates[0]][coordinates[1]] = '.';
            this.board[(coordinates[0]+coordinates[2])/2][(coordinates[1]+coordinates[3])/2] = '.';
            this.board[coordinates[2]][coordinates[3]] = temp;
            return 1;
        }
        //cant jump
        else {
            return 0;
        }
    }

    /**
     * get the next moves, given a hopper config
     * @return next moves
     */
    public Configuration getHint() throws IndexOutOfBoundsException{
        BFS bfs = new BFS();
        Collection<Configuration> sol = bfs.getShortestPath(this);
        ArrayList<Configuration> hintList = new ArrayList<>(sol);
        return hintList.get(1);
    }

    /**
     * is goal, check board if there is 1 red and 0 green left
     * @return true if there is 1 red and 0 green, false otherwise
     */
    @Override
    public boolean isGoal() {
        int red = 0;
        int green =0;
        int lookingRow = 0;
        int lookingCol = 0;
        //count red and green
        while (lookingRow < numRows && lookingCol < numColumns) {
            if(this.board[lookingRow][lookingCol]=='G'){
                green += 1;
            }
            else if(this.board[lookingRow][lookingCol]=='R'){
                red += 1;
            }
            lookingCol += 1;
            if (lookingCol == numColumns) {
                lookingRow += 1;
                lookingCol = 0;
            }
        }
        return red == 1 && green == 0;
    }

    /**
     * The board
     * @return string builder with the board
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int row=0; row<numRows; row++) {
            result.append("\n");
            for (int col=0; col<numColumns; col++) {
                if (this.board[row][col] == 'R') {
                    result.append('R');
                }
                else if (this.board[row][col] == 'G') {
                    result.append('G');
                }
                else if (this.board[row][col] == '.') {
                    result.append('.');
                }
                else if (this.board[row][col] == '*') {
                    result.append("*");
                }
                result.append(" ");
            }
        }
        return result.toString();
    }

    /**
     * equal method
     * @param o things we are comparing
     * @return equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HoppersConfig that = (HoppersConfig) o;
        return Arrays.deepEquals(this.board, that.board);
    }

    /**
     * hashcode
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
