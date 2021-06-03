package Hoppers.Controller;

import Hoppers.GUI.HoppersGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import BFS.Observer;
import Hoppers.Model.HoppersClientData;
import Hoppers.Model.HoppersModel;
import javafx.stage.FileChooser;
import java.io.File;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class HoppersController implements Observer<HoppersModel, HoppersClientData> {
    private final static String RESOURCES_DIR = "Resources/";
    private HoppersModel model;
    private ArrayList<Button> buttonsList = new ArrayList<>();
    private boolean rebuildGui = false;
    private Stage stage;

    private final Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"red_frog.png"));
    private final Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green_frog.png"));
    private final Image lilyPad = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"lily_pad.png"));
    private final Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"water.png"));

    @FXML
    private BorderPane hoppersBorderPane;

    @FXML
    private Label topLabel;

    @FXML
    private Button resetButton;

//    @FXML
//    private Button loadButton;

    @FXML
    private Button hintButton;





    public void initialize(){
        try {
//            String filename = getParameters().getRaw().get(0);
            String filename = "data/hoppers/hoppers-4.txt";
            this.model = new HoppersModel(filename);
            model.addObserver(this);
        }
        catch (IOException e){
//            System.out.println("Can't open file: " + getParameters().getRaw().get(0));
        }
//        HoppersGUI hoppersGUI = new HoppersGUI();
//        BorderPane borderPane = hoppersGUI.getBorderPane();
        //clear the buttonsList for the new stage and make the new stage
        this.buttonsList.clear();
//        this.stage = stage;

        //create file chooser
        final FileChooser fileChooser = new FileChooser();

        //create layouts and buttons
        BorderPane borderPane = new BorderPane();
//        this.topLabel = new Label("Loaded: " + getParameters().getRaw().get(0));
        this.topLabel.setText("Loaded puzzle");
        GridPane gridPane = new GridPane();

//        HBox bottomHbox = new HBox();
//        Button loadButton = new Button("Load");
//        Button resetButton = new Button("Reset");
//        Button hintButton = new Button("Hint");
//        BorderPane bottomBorderPane = new BorderPane();

//        //bottom buttons
//        bottomHbox.getChildren().addAll(loadButton,resetButton,hintButton);
//        bottomHbox.setAlignment(Pos.CENTER);
//
//        //bottom scene
//        bottomBorderPane.setCenter(bottomHbox);

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

//        //buttons get pressed
//        loadButton.setOnAction(event -> {
//            File file = fileChooser.showOpenDialog(stage);
//            if (file != null) {
//                this.rebuildGui = true;
//                this.model.load(file.toString());
//            }
//        });

//        resetButton.setOnAction(event -> {
//            model.reset();
//        });
//        hintButton.setOnAction(event -> {
//            model.hint();
//        });
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



    @Override
    public void update(HoppersModel hoppersModel, HoppersClientData hoppersClientData) {
        if (this.rebuildGui){
//            start(this.stage);
            this.rebuildGui = false;
        }
        this.topLabel.setText(hoppersClientData.toString());
        for (int row=0; row<this.model.getCurrentConfig().getNumRows(); ++row) {
            for (int col=0; col<this.model.getCurrentConfig().getNumColumns(); ++col) {
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
            }
        }
    }


}
