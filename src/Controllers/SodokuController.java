package Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import Sodoku.Model.SodokuConfiguration;

public class SodokuController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private int[][] sodokuGrid = new int[9][9];
    @FXML
    private GridPane sodokuGridPane;
    @FXML
    private Label messageLabel;



    /**
     * get text in cell
     * @param id id of the cell
     * @return the text in cell
     */
    public String getTextField(TextField id){
        return id.getText();
    }

    public void getGridPane(){
        for (int i = 0; i < sodokuGridPane.getChildren().size()-1; i++) {
            try {
                int numInGrid = Integer.parseInt(getTextField((TextField) sodokuGridPane.getChildren().get(i)));
                if(numInGrid >= 1 && numInGrid <= 9) {
                    if(validCheck(numInGrid, Math.floorDiv(i, 9), i % 9)) {
                        sodokuGrid[Math.floorDiv(i, 9)][(i % 9)] = numInGrid;
                    }
                    else{
                        messageLabel.setText("this isn't valid");
                    }
                }
                else {
                    sodokuGrid[Math.floorDiv(i, 9)][(i % 9)] = 0;
                }
            }
            catch (Exception e){
                sodokuGrid[Math.floorDiv(i, 9)][(i % 9)] = 0;
            }
        }
        updateGrid();
    }

    public void solveSodoku(){
        try {
            SodokuConfiguration sodokuConfiguration = new SodokuConfiguration(sodokuGrid);
            this.sodokuGrid = sodokuConfiguration.getSolvedBoard(sodokuGrid);
            updateGrid();
        }
        catch (IndexOutOfBoundsException e){
            messageLabel.setText("Can't solve this puzzle");
        }
    }

    /**
     * update to the grid pane
     */
    public void updateGrid(){
        for (int i = 0 ; i < sodokuGridPane.getChildren().size()-1; i++) {
            if (sodokuGrid[Math.floorDiv(i, 9)][(i%9)] == 0){
                ((TextField)sodokuGridPane.getChildren().get(i)).setText("");
            }
            else {
                ((TextField) sodokuGridPane.getChildren().get(i)).setText(String.valueOf(sodokuGrid[Math.floorDiv(i, 9)][(i % 9)]));
            }
        }
    }

    /**
     * switch to main scene
     * @param event click home button
     * @throws IOException throw exception if cannot open source
     */
    public void switchToMain(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Resources/MainStage.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * check row if there is duplicate
     * @return false if there is, true if there is not
     */
    private boolean rowCheck(int num, int rowCords, int colCords){
        for (int i = 0; i < 9; i++){
            if (num == this.sodokuGrid[rowCords][i] && colCords != i){
                return false;
            }
        }
        return true;
    }

    /**
     * check column if there is duplicate
     * @return false if there is, true if there is not
     */
    private boolean colCheck(int num, int rowCords, int colCords){
        for (int i = 0; i < 9; i++){
            if (num == this.sodokuGrid[i][colCords] && rowCords != i){
                return false;
            }
        }
        return true;
    }

    /**
     * check subbox if there is duplicate
     * @param num
     * @param rowCords
     * @param colCords
     * @return false if there is, true if there is not
     */
    private boolean boxCheck(int num, int rowCords, int colCords){
        int boxCol = Math.floorDiv(colCords,3);
        int boxRow = Math.floorDiv(rowCords,3);
        for(int i = boxRow*3; i < boxRow*3+3; i++){
            for(int j = boxCol*3; j < boxCol*3+3; j++){
                if(num == this.sodokuGrid[i][j]&&rowCords!=i &&colCords!=j){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validCheck(int num, int rowCords, int colCords){
        return rowCheck(num,rowCords,colCords)&&colCheck(num,rowCords,colCords)&&boxCheck(num,rowCords,colCords);
    }
}
