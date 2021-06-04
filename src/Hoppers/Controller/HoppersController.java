package Hoppers.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import BFS.Observer;
import Hoppers.Model.HoppersClientData;
import Hoppers.Model.HoppersModel;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class HoppersController implements Observer<HoppersModel, HoppersClientData> {
    private final static String RESOURCES_DIR = "Images/";
    private HoppersModel model;
    private ArrayList<Button> buttonsList = new ArrayList<>();

    private final Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"red_frog.png"));
    private final Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green_frog.png"));
    private final Image lilyPad = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"lily_pad.png"));
    private final Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"water.png"));

    @FXML
    private BorderPane hoppersBorderPane;

    @FXML
    private Label topLabel;

    @FXML
    private ComboBox<String> puzzleChoices;


    public void initialize(){
        hoppersBorderPane.setStyle("-fx-background-color: #0091ea;");
        ObservableList<String> list = FXCollections.observableArrayList("Puzzle 1", "Puzzle 2", "Puzzle 3",
                "Puzzle 4", "Puzzle 5", "Puzzle 6", "Puzzle 7");
        puzzleChoices.setItems(list);
        String filename = "data/hoppers/hoppers-7.txt";
        try {
            this.model = new HoppersModel(filename);
            model.addObserver(this);
        }
        catch (IOException e){
            System.out.println("Can't open file");
        }
        //clear the buttonsList for the new stage and make the new stage
        this.buttonsList.clear();

        //create layouts and buttons
        this.topLabel.setText("Loaded puzzle");
        GridPane gridPane = new GridPane();

        //center
        for (int row=0; row<this.model.getCurrentConfig().getNumRows(); ++row) {
            for (int col=0; col<this.model.getCurrentConfig().getNumColumns(); ++col) {
                int tempRow = row;
                int tempCol = col;
                Button button = new Button();
                if(this.model.getCurrentConfig().getBoard()[row][col]=='.'){
                    button.setGraphic(new ImageView(lilyPad));
                }
                else if(this.model.getCurrentConfig().getBoard()[row][col]=='G'){
                    button.setGraphic(new ImageView(greenFrog));
                }
                else if(this.model.getCurrentConfig().getBoard()[row][col]=='R'){
                    button.setGraphic(new ImageView(redFrog));
                }
                else if(this.model.getCurrentConfig().getBoard()[row][col]=='*'){
                    button.setGraphic(new ImageView(water));
                }
                button.setMinSize(75, 75);
                button.setMaxSize(75, 75);
                this.buttonsList.add(button);
                button.setOnAction(event -> model.select(tempRow, tempCol));
                gridPane.add(button, col, row);
            }
        }
        //set main border
        gridPane.setAlignment(Pos.CENTER);
        hoppersBorderPane.setCenter(gridPane);
    }

    @FXML
    private void handleResetButton(){
        model.reset();
    }

    @FXML
    private void handleHintButton(){
        model.hint();
    }

    @FXML
    private void handleHomeButton(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("MainResources/MainStage.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private void handlePuzzleChoices(){
        switch (puzzleChoices.getValue()) {
            case "Puzzle 1" -> model.load("data/hoppers/hoppers-1.txt");
            case "Puzzle 2" -> model.load("data/hoppers/hoppers-2.txt");
            case "Puzzle 3" -> model.load("data/hoppers/hoppers-3.txt");
            case "Puzzle 4" -> model.load("data/hoppers/hoppers-4.txt");
            case "Puzzle 5" -> model.load("data/hoppers/hoppers-5.txt");
            case "Puzzle 6" -> model.load("data/hoppers/hoppers-6.txt");
            case "Puzzle 7" -> model.load("data/hoppers/hoppers-7.txt");
        }
    }



    @Override
    public void update(HoppersModel hoppersModel, HoppersClientData hoppersClientData) {
        GridPane gridPane = new GridPane();
        this.topLabel.setText(hoppersClientData.toString());
        for (int row=0; row<this.model.getCurrentConfig().getNumRows(); ++row) {
            for (int col=0; col<this.model.getCurrentConfig().getNumColumns(); ++col) {
                int tempRow = row;
                int tempCol = col;
                if(this.model.getCurrentConfig().getBoard()[row][col]=='.'){
                    buttonsList.get(row*this.model.getCurrentConfig().getNumColumns()+col).setGraphic(new ImageView(lilyPad));
                }
                else if(this.model.getCurrentConfig().getBoard()[row][col]=='G'){
                    buttonsList.get(row*this.model.getCurrentConfig().getNumColumns()+col).setGraphic(new ImageView(greenFrog));
                }
                else if(this.model.getCurrentConfig().getBoard()[row][col]=='R'){
                    buttonsList.get(row*this.model.getCurrentConfig().getNumColumns()+col).setGraphic(new ImageView(redFrog));
                }
                else if(this.model.getCurrentConfig().getBoard()[row][col]=='*'){
                    buttonsList.get(row*this.model.getCurrentConfig().getNumColumns()+col).setGraphic(new ImageView(water));
                }
                gridPane.add(buttonsList.get(row*this.model.getCurrentConfig().getNumColumns()+col), col, row);
                this.buttonsList.add(buttonsList.get(row*this.model.getCurrentConfig().getNumColumns()+col));
                buttonsList.get(row*this.model.getCurrentConfig().getNumColumns()+col).setOnAction(event -> model.select(tempRow, tempCol));
            }
        }
        //set main border
        gridPane.setAlignment(Pos.CENTER);
        hoppersBorderPane.setCenter(gridPane);
    }
}
