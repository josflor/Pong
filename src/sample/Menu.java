package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Menu {

    @FXML
    private AnchorPane main_anchor;

    @FXML
    private Button button1p;

    @FXML
    private Button button2p;

    public void onSingleplayer(Event event) {
        Stage stage;
        stage = (Stage) getScene().getWindow();
        PongSinglePlayer getSingleplayer = Main.getSingleplayer();
        stage.setScene(getSingleplayer.getScene());

    }

    public void onMultiplayer(Event event)  {
        Stage stage;
        stage = (Stage) getScene().getWindow();
        PongMultiPlayer getMultiplayer = Main.getMulitplayer();
        stage.setScene(getMultiplayer.getScene());
    }


    public Scene getScene(){
        return button2p.getScene();
    }

}
