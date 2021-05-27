package Controllers;

import java.io.IOException;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import Sodoku.Model.SodokuConfiguration;

public class MainController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ChoiceBox<String> gameChoices;

    public void initialize(){
        setGameChoice();
        gameChoices.setValue("Sodoku");
    }

    public void setGameChoice(){
        gameChoices.getItems().add("Sodoku");
        gameChoices.getItems().add("Tents");
    }

    public void switchToGame(ActionEvent event) throws IOException{
        if(gameChoices.getValue().equals("Sodoku")){
            switchToSodoku(event);
        }
        else if (gameChoices.getValue().equals("Tents")){
            switchToTents(event);
        }
    }


    public void switchToTents(ActionEvent event) throws IOException{
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Resources/TentsStage.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSodoku(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Resources/SodokuStage.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
