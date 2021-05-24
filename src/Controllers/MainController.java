package Controllers;

import java.io.IOException;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import Sodoku.Model.SodokuConfiguration;

public class MainController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private int[][] sodokuGrid = new int[9][9];
    @FXML
    private GridPane sodokuGridPane;


    public int getTextField(TextField id){
        try {
            return Integer.parseInt(id.getText());
        }
        catch (Exception e){
            return 0;
        }
    }

    public void getGridPane(){
        for (int i = 0; i < sodokuGridPane.getChildren().size()-1; i++) {
            int text = getTextField((TextField) sodokuGridPane.getChildren().get(i));
            sodokuGrid[Math.floorDiv(i, 9)][(i%9)] = text;
        }
        System.out.println(sodokuGrid);
        SodokuConfiguration sodokuConfiguration = new SodokuConfiguration(sodokuGrid);
        int[][] solvedBoard = sodokuConfiguration.getSolvedBoard(sodokuGrid);
        this.sodokuGrid = solvedBoard;
        updateGrid();
    }

    public void updateGrid(){
        for (int i = 0 ; i < sodokuGridPane.getChildren().size()-1; i++) {
            ((TextField)sodokuGridPane.getChildren().get(i)).setText(String.valueOf(sodokuGrid[Math.floorDiv(i, 9)][(i%9)]));
        }
    }

    public void switchToSodoku(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Resources/SodokuStage.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMain(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Resources/MainStage.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void clickButton(ActionEvent event) throws IOException {
        System.out.println("alo");
    }

}
