import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;

public class MainWindow extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        try {
            stage.setTitle("Puzzle games");
            Parent primaryRoot = FXMLLoader.load(getClass().getResource("Resources/MainStage.fxml"));
            Scene primaryScene = new Scene(primaryRoot);
            stage.setScene(primaryScene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * main
     * @param args user input
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
