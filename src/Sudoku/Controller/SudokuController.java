package Sudoku.Controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import Sudoku.Model.SudokuConfiguration;
import java.io.IOException;

/**
 * @author Quan Quy
 * Controller for sodoku window
 */
public class SudokuController {
    //board
    private int[][] sudokuGrid = new int[9][9];
    //data to make board
    private final String sudokuData = "src/Sudoku/Resources/SudokuData.txt";
    //flag if one of the cells is invalid
    private boolean isValid = true;
    //grid pane that contains cells in sudoku
    @FXML
    private GridPane sudokuGridPane;
    //message for client
    @FXML
    private Label messageLabel;
    //check box for random sudoku mode
    @FXML
    private CheckBox randomBox;
    //check box for solver mode
    @FXML
    private CheckBox customBox;
    //reset button, or new puzzle button
    @FXML
    private Button resetButton;

    /**
     * initialize Sudoku window
     * custom box is selected
     */
    public void initialize(){
        customBox.setSelected(true);
        for (int i = 0; i < sudokuGridPane.getChildren().size()-1; i++) {
            sudokuGridPane.getChildren().get(i).setStyle("-fx-background-color: white");
        }
    }

    /**
     * when random mode box is clicked, disable solver mode
     */
    @FXML
    private void handleRandomBox(){
        if(randomBox.isSelected()){
            customBox.setSelected(false);
            resetButton.setText("New Puzzle");
        }
    }

    /**
     * when solver mode is clicked, disable random mode
     */
    @FXML
    private void handleCustomBox(){
        if(customBox.isSelected()){
            randomBox.setSelected(false);
            resetButton.setText("Reset Board");
        }
    }

    /**
     * when the reset button is clicked, reset the puzzle in solver mode, or make new puzzle in random mode
     */
    @FXML
    private void handleResetButton(){
        for (int i = 0; i < sudokuGridPane.getChildren().size()-1; i++) {
            //reset everything to changeable and change background to white
            sudokuGridPane.getChildren().get(i).setDisable(false);
            sudokuGridPane.getChildren().get(i).setStyle("-fx-background-color: white");
        }
        //in solver mode, make new board and update it
        if(customBox.isSelected()){
            this.sudokuGrid = new int[9][9];
            messageLabel.setText("Puzzle cleared");
        }
        //in random mode, get random sodoku and update it
        else if (randomBox.isSelected()){
            makeRandomSudoku();
            messageLabel.setText("New puzzle assigned");
        }
        updateGrid();
    }

    /**
     * get a random puzzle from data file
     */
    private void makeRandomSudoku(){
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(sudokuData));
        } catch (IOException e) {
            System.out.println("Trouble reading data file");
        }
        Random random = new Random();
        String randomSodoku = lines.get(random.nextInt(lines.size()));
        for (int i = 0; i < randomSodoku.length(); i++){
            sudokuGrid[Math.floorDiv(i, 9)][(i % 9)] = Character.getNumericValue(randomSodoku.charAt(i));
            //disable the initial numbers from the random sodoku
            if(sudokuGrid[Math.floorDiv(i, 9)][(i % 9)]!=0) {
                sudokuGridPane.getChildren().get(i).setDisable(true);
            }
        }
        messageLabel.setText("Reset a new Sudoku Puzzle");
    }

    /**
     * get text in cell by id
     * @param id id of the cell
     * @return the text in cell
     */
    private String getTextField(TextField id){
        return id.getText();
    }

    /**
     * this is called when a cell changes it variable
     * get data from Sudoku's gridpane and put them in a 2D array grid
     * put 0 if invalid input
     * put number on grid if valid input
     */
    @FXML
    private void getGridPane(){
        this.isValid = true;
        for (int i = 0; i < sudokuGridPane.getChildren().size()-1; i++) {
            try {
                //get number in the cell
                int numInGrid = Integer.parseInt(getTextField((TextField) sudokuGridPane.getChildren().get(i)));
                //if it's valid number 1-9
                if(numInGrid >= 1 && numInGrid <= 9) {
                    sudokuGrid[Math.floorDiv(i, 9)][(i % 9)] = numInGrid;
                    //if it's valid, set background color to white
                    if(validCheck(numInGrid, Math.floorDiv(i, 9), i % 9)) {
                        sudokuGridPane.getChildren().get(i).setStyle("-fx-background-color: white");
                    }
                    //if it's invalid, set background color to red
                    else{
                        sudokuGridPane.getChildren().get(i).setStyle("-fx-background-color: red");
                        messageLabel.setText("Oof...Something is wrong... Check rules");
                        //if one of the cells is invalid, flag it
                        this.isValid = false;
                    }
                }
                //if it's not valid, it's empty, or 0 in grid
                else {
                    sudokuGrid[Math.floorDiv(i, 9)][(i % 9)] = 0;
                }
            }
            //if not valid, it's empty, or 0 in grid
            catch (Exception e){
                sudokuGrid[Math.floorDiv(i, 9)][(i % 9)] = 0;
            }
        }
        //if every cells is valid, print valid
        if (this.isValid) {
            messageLabel.setText("Valid input");
        }
        //if one of the cells is invalid, print the message
        else{
            messageLabel.setText("Oof...Something is wrong... Check rules");
        }
        //update the grid
        updateGrid();
    }

    /**
     * When the solve button is push, solve the current sodoku on the board
     */
    @FXML
    private void solveSodoku(){
        try {
            if(isValid) {
                SudokuConfiguration sudokuConfiguration = new SudokuConfiguration(sudokuGrid);
                this.sudokuGrid = sudokuConfiguration.getSolvedBoard(sudokuGrid);
                messageLabel.setText("Puzzle solved!");
                updateGrid();
            }
            else {
                messageLabel.setText("Can't solve this puzzle!");
            }
        }
        catch (IndexOutOfBoundsException e){
            messageLabel.setText("Can't solve this puzzle!");
        }
    }

    /**
     * update to the grid pane
     */
    private void updateGrid(){
        for (int i = 0 ; i < sudokuGridPane.getChildren().size()-1; i++) {
            if (sudokuGrid[Math.floorDiv(i, 9)][(i%9)] == 0){
                ((TextField)sudokuGridPane.getChildren().get(i)).setText("");
            }
            else {
                ((TextField) sudokuGridPane.getChildren().get(i)).setText(String.valueOf(sudokuGrid[Math.floorDiv(i, 9)][(i % 9)]));
            }
        }
    }

    /**
     * when the home button is push. Switch to main scene
     * @param event click home button
     * @throws IOException throw exception if cannot open source
     */
    @FXML
    private void switchToMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("MainResources/MainStage.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * check row if there is duplicate
     * @param num number checking
     * @param rowCords row checking
     * @param colCords column checking
     * @return false if there is, true if there is not
     */
    private boolean rowCheck(int num, int rowCords, int colCords){
        for (int i = 0; i < 9; i++){
            if (num == this.sudokuGrid[rowCords][i] && colCords != i){
                return false;
            }
        }
        return true;
    }

    /**
     * check column if there is duplicate
     * @param num number checking
     * @param rowCords row checking
     * @param colCords column checking
     * @return false if there is, true if there is not
     */
    private boolean colCheck(int num, int rowCords, int colCords){
        for (int i = 0; i < 9; i++){
            if (num == this.sudokuGrid[i][colCords] && rowCords != i){
                return false;
            }
        }
        return true;
    }

    /**
     * check subbox if there is duplicate
     * @param num number checking
     * @param rowCords row checking
     * @param colCords column checking
     * @return false if there is, true if there is not
     */
    private boolean boxCheck(int num, int rowCords, int colCords){
        int boxCol = Math.floorDiv(colCords,3);
        int boxRow = Math.floorDiv(rowCords,3);
        for(int i = boxRow*3; i < boxRow*3+3; i++){
            for(int j = boxCol*3; j < boxCol*3+3; j++){
                if(num == this.sudokuGrid[i][j]&&rowCords!=i &&colCords!=j){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * check valid for row, col, box to see if there is duplicate
     * @param num number pointing at
     * @param rowCords row checking
     * @param colCords column checking
     * @return false if there is, true if there is not
     */
    private boolean validCheck(int num, int rowCords, int colCords){
        return rowCheck(num,rowCords,colCords)&&colCheck(num,rowCords,colCords)&&boxCheck(num,rowCords,colCords);
    }


}
