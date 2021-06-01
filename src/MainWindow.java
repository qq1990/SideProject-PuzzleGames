import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class MainWindow extends Application {

    @Override
    public void start(Stage stage){
        try {
            stage.setTitle("Puzzle games");
            Parent primaryRoot = FXMLLoader.load(getClass().getResource("/Resources/MainStage.fxml"));
            Scene primaryScene = new Scene(primaryRoot);
            stage.setResizable(false);
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
