package TentsAndTrees.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class TentsController {
    @FXML
    private GridPane gridPane;

    @FXML
    private AnchorPane tentsap;

    public void initialize(){
        tentsap.getChildren().add(new Button("slo"));
    }


    @FXML
    private void getCord(){
        System.out.println();
    }


}
