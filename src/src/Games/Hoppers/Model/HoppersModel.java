package Games.Hoppers.Model;

import Solvers.BFS.Observer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Games.Hoppers Model, create model for the game
 *
 * @author Quan Quy
 */
public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, HoppersClientData>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;
    private String fileRunning;

    private int numSelections = 0;
    private int[] coordinates = new int[4];

    /**
     * Construct a ConcentrationModel; there is only one configuration.
     */
    public HoppersModel(String arg) throws IOException {
        this.currentConfig = new HoppersConfig(arg);
        alertObservers(new HoppersClientData("Loaded: " + arg));
        this.fileRunning = arg;
    }

    /**
     * print this current config with coordinates north and west
     * @return this board in string builder
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append("   ");
        for(int col=0; col<this.currentConfig.getNumColumns(); col++){
            result.append(col);
            result.append(" ");
        }
        result.append("\n");
        result.append("   ");
        result.append("-".repeat(Math.max(0, this.currentConfig.getNumColumns() * 2 - 1)));
        for (int row=0; row<this.currentConfig.getNumRows(); row++) {
            result.append("\n");
            result.append(row);
            result.append("| ");
            for (int col=0; col<this.currentConfig.getNumColumns(); col++) {
                if (this.currentConfig.getBoard()[row][col] == 'R') {
                    result.append('R');
                }
                else if (this.currentConfig.getBoard()[row][col] == 'G') {
                    result.append('G');
                }
                else if (this.currentConfig.getBoard()[row][col] == '.') {
                    result.append('.');
                }
                else if (this.currentConfig.getBoard()[row][col] == '*') {
                    result.append("*");
                }
                result.append(" ");
            }
            result.deleteCharAt(result.length()-1);
        }
        return result.toString();
    }

    /**
     * return the current configuration
     * @return current config
     */
    public HoppersConfig getCurrentConfig(){
        return this.currentConfig;
    }


    /**
     * select a coordinate to move it
     * @param r selected row coords
     * @param c selected column coords
     */
    public void select( int r, int c ) {
        if ( r >= 0 && c >= 0 && r < currentConfig.getNumRows() && c < currentConfig.getNumColumns()) {
            if (numSelections == 0 && (currentConfig.getBoard()[r][c]=='R'||currentConfig.getBoard()[r][c]=='G')) {
                //first time select
                coordinates[0] = r;
                coordinates[1] = c;
                alertObservers(new HoppersClientData("Selected (" + r + "," + c + ")"));
                numSelections += 1;
            }
            else if (numSelections == 1 && (currentConfig.getBoard()[r][c] =='G'||currentConfig.getBoard()[r][c] =='R' || currentConfig.getBoard()[r][c] =='.')) {
                //second time select
                coordinates[2] = r;
                coordinates[3] = c;
                //if it can move
                if (this.currentConfig.move(this.coordinates) == 1) {
                    alertObservers(new HoppersClientData("Jump from (" + coordinates[0] + "," + coordinates[1]
                            + ") to (" + coordinates[2] + "," + coordinates[3] + ")"));
                }
                //it cannot move
                else {
                    alertObservers(new HoppersClientData("Can't jump from (" + coordinates[0] + "," + coordinates[1]
                            + ") to (" + coordinates[2] + "," + coordinates[3] + ")"));
                }
                //reset number of selections and coordinates saved
                this.numSelections = 0;
                this.coordinates = new int[4];
            }
            else {
                alertObservers(new HoppersClientData("Invalid selection (" + r + "," + c + ")"));
                //reset number of selections and coordinates saved
                this.numSelections = 0;
                this.coordinates = new int[4];
            }
        }
        else {
            alertObservers(new HoppersClientData("Invalid selection (" + r + "," + c + ")"));
            //reset number of selections and coordinates saved
            this.numSelections = 0;
            this.coordinates = new int[4];
        }
    }

    /**
     * hint
     */
    public void hint(){
        try {
            if (this.currentConfig.isGoal()){
                alertObservers(new HoppersClientData("Already solved!"));
            }
            else {
                this.currentConfig = (HoppersConfig) this.currentConfig.getHint();
                alertObservers(new HoppersClientData("Next Step!"));
            }
        }
        catch (IndexOutOfBoundsException e){
            alertObservers(new HoppersClientData("No solution!"));
        }
    }

    /**
     * reset the game
     */
    public void reset(){
        try {
            this.currentConfig = new HoppersConfig(fileRunning);
            this.numSelections = 0;
            alertObservers(new HoppersClientData("Loaded: " + fileRunning));
            alertObservers(new HoppersClientData("Puzzle reset!"));
        }
        catch (Exception e){
            alertObservers(new HoppersClientData("Trouble resetting game (check file)"));
        }
    }

    /**
     * load a file to play
     * @param file list of string
     */
    public void load(String file){
        try {
            this.currentConfig = new HoppersConfig(file);
            this.fileRunning = file;
            this.numSelections = 0;
            alertObservers(new HoppersClientData("Loaded: " + fileRunning));
        }
        catch (IOException e){
            alertObservers(new HoppersClientData("Failed to load: " + file));
        }
    }
    
    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, HoppersClientData> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(HoppersClientData data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }
}
