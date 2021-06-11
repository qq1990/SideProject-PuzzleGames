package MemoryMatching.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import MemoryMatching.Model.Card;
import MemoryMatching.Model.ConcentrationModel;
import MemoryMatching.Model.Observer;

import java.io.IOException;
import java.util.*;

/**
 * The ConcentrationGUI application is the UI for Concentration.
 *
 * @author Quan Quy
 */
public class MemoryMatchingController implements Observer< ConcentrationModel, Object > {
    private final int DIM=4;
    private ConcentrationModel model;
    private Label bottomLabel;
    private final Image abra = new Image(getClass().getResourceAsStream("Images/abra.png"));
    private final Image bulbasaur = new Image(getClass().getResourceAsStream("Images/bulbasaur.png"));
    private final Image charmander = new Image(getClass().getResourceAsStream("Images/charmander.png"));
    private final Image jigglypuff = new Image(getClass().getResourceAsStream("Images/jigglypuff.png"));
    private final Image meowth = new Image(getClass().getResourceAsStream("Images/meowth.png"));
    private final Image pikachu = new Image(getClass().getResourceAsStream("Images/pikachu.png"));
    private final Image pokeball = new Image(getClass().getResourceAsStream("Images/pokeball.png"));
    private final Image squirtle = new Image(getClass().getResourceAsStream("Images/squirtle.png"));
    private final Image venomoth = new Image(getClass().getResourceAsStream("Images/venomoth.png"));
    private final List<Button> buttonList = new ArrayList<>();
    private final Stage cheatStage = new Stage();
    private GridPane pokeGridPane = new GridPane();

    @FXML
    private BorderPane memoryBorderPane;

    @FXML
    private Label topLabel;

    @FXML
    private Button resetButton;

    @FXML
    private Button undoButton;

    @FXML
    private Button cheatButton;

    /**
     * Pokemons
     */
    private enum Pokemon {
        ABRA,
        BULBASAUR,
        CHARMADER,
        JIGGLYPUFF,
        MEOWTH,
        PIKACHU,
        SQUIRTLE,
        VENOMOTH,
        POKEBALL
    }

    /**
     * create image from type of pokemon
     * @param pokemon type of pokemon
     * @return image
     */
    private Image getImageView(Pokemon pokemon){
        return switch (pokemon) {
            case ABRA -> abra;
            case BULBASAUR -> bulbasaur;
            case CHARMADER -> charmander;
            case JIGGLYPUFF -> jigglypuff;
            case MEOWTH -> meowth;
            case PIKACHU -> pikachu;
            case SQUIRTLE -> squirtle;
            case VENOMOTH -> venomoth;
            default -> pokeball;
        };
    }

    /**
     * initialize the game
     */
    public void initialize() {
        this.model = new ConcentrationModel();
        model.addObserver(this);
        memoryBorderPane.setStyle("-fx-background-color:  #d6f5f5;");
        this.topLabel.setText("Select the first card.");
        this.bottomLabel = new Label("Moves: 0");
        BorderPane bottomBorderPane = new BorderPane();


        //bottom scene
        bottomBorderPane.setRight(bottomLabel);

        //center
        for (int row=0; row<DIM; ++row) {
            for (int col=0; col<DIM; ++col) {
                int tempRow = row;
                int tempCol = col;
                Button button = new Button();
                button.setGraphic(new ImageView(getImageView(Pokemon.POKEBALL)));
                buttonList.add(button);
                button.setOnAction(event -> model.selectCard(DIM*tempRow+tempCol));
                pokeGridPane.add(button, col, row);
            }
        }
        //set main border
        memoryBorderPane.setCenter(pokeGridPane);
        pokeGridPane.setAlignment(Pos.CENTER);
        memoryBorderPane.setBottom(bottomBorderPane);

        //buttons get pressed
        resetButton.setOnAction(event -> model.reset());
        undoButton.setOnAction(event -> model.undo());
        cheatButton.setOnAction(event -> model.cheat());
    }

    @FXML
    private void handleHomeButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("MainResources/MainStage.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * update the game
     *
     * @param concentrationModel the model object that knows the current board state
     * @param o null ⇒ non-cheating mode; non-null ⇒ cheating mode
     */
    @Override
    public void update( ConcentrationModel concentrationModel, Object o ) {
        ArrayList<Card> cards = model.getCards();
        ArrayList<Card> cheatCards = model.getCheat();
        Pokemon[] pokemon = Pokemon.values();
        for (int i = 0; i < buttonList.size(); i++){
            int id = cards.get(i).getNumber();
            Button currentButton = buttonList.get(i);
            if (cards.get(i).isFaceUp()){
                currentButton.setGraphic(new ImageView(getImageView(pokemon[id])));
            }
            else {
                currentButton.setGraphic(new ImageView(getImageView(Pokemon.POKEBALL)));
            }
        }
        //top label message
        if (this.model.howManyCardsUp() == 0){
            topLabel.setText("Select the first card.");
        }
        else if (this.model.howManyCardsUp() == 1){
            topLabel.setText("Select the second card.");
        }
        else if (this.model.howManyCardsUp() == 2){
            topLabel.setText("No Match: Undo or select a card.");
        }

        //bottom label message
        bottomLabel.setText("Moves: " + this.model.getMoveCount());

        //top label prints Win message if all cards match
        if ( this.model.getCards().stream()
                .allMatch(Card::isFaceUp) ) {
            topLabel.setText( "YOU WIN!" );
        }

        //cheat button is pressed
        if (o != null){
            GridPane cheatGridPane = new GridPane();
            int i = 0;
            for (int row=0; row<DIM; ++row) {
                for (int col=0; col<DIM; ++col) {
                    int id = cheatCards.get(i).getNumber();
                    Button button = new Button();
                    button.setGraphic(new ImageView(getImageView(pokemon[id])));
                    cheatGridPane.add(button, col, row);
                    i++;
                }
            }
            cheatStage.setScene(new Scene(cheatGridPane));
            cheatStage.setTitle("Cheat window");
            cheatStage.show();
        }
    }
}