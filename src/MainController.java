import java.io.IOException;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

/**
 * @author Quan Quy
 * Control the main window
 */
public class MainController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Choose the game to play
     */
    @FXML
    private ChoiceBox<String> gameChoices;

    /**
     * Choose sudoku as initial game
     */
    public void initialize(){
        setGameChoice();
        gameChoices.setValue("Sudoku");
    }

    /**
     * add game choices in choice box
     */
    public void setGameChoice(){
        gameChoices.getItems().add("Sudoku");
        gameChoices.getItems().add("Tents");
        gameChoices.getItems().add("Hoppers");
        gameChoices.getItems().add("Memory Matching");
    }

    /**
     * this is called when start button is pushed
     * @param event action event
     */
    public void switchToGame(ActionEvent event){
        try {
            switch (gameChoices.getValue()) {
                case "Sudoku" -> switchToSudoku(event);
                case "Tents" -> switchToTents(event);
                case "Hoppers" -> switchToHoppers(event);
                case "Memory Matching" -> switchToMemoryMatching(event);
            }
        }
        catch (IOException e){
            System.out.println("Resources files can't be read");
        }
    }

    public void playGame(Parent root, ActionEvent event){
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToHoppers(ActionEvent event) throws IOException{
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Games/Hoppers/GUI/HoppersStage.fxml")));
        playGame(root, event);
    }

    /**
     * switch to Tents puzzle
     * @param event action event
     * @throws IOException throw exception if file can't be read
     */
    public void switchToTents(ActionEvent event) throws IOException{
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Games/TentsAndTrees/GUI/TentsStage.fxml")));
        playGame(root, event);
    }

    /**
     * switch to sudoku puzzle
     * @param event action event
     * @throws IOException throw exception if file can't be read
     */
    public void switchToSudoku(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Games/Sudoku/GUI/SudokuStage.fxml")));
        playGame(root, event);
    }

    /**
     * switch to memory matching puzzle
     * @param event action event
     * @throws IOException throw exception if file can't be read
     */
    public void switchToMemoryMatching(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Games/MemoryMatching/GUI/MemoryMatchingStage.fxml")));
        playGame(root, event);
    }
}
