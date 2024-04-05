package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    private static Menu menu;
    private static PongMultiPlayer pong_multi;
    private static PongSinglePlayer pong_single;

    @Override
    public void start(Stage primaryStage) throws Exception {


        URL menu_location;
        menu_location = new File("src/sample/Menu.fxml").toURI().toURL();
        FXMLLoader loader;
        loader = new FXMLLoader(menu_location);
        loader.load();
        this.menu = loader.getController();
        new Scene(loader.getRoot());


        URL pong_location;
        pong_location = new File("src/sample/Pong2P.fxml").toURI().toURL();
        loader = new FXMLLoader(pong_location);
        loader.load();
        this.pong_multi = loader.getController();
        new Scene(loader.getRoot());

        PongMultiPlayer controller = loader.getController();
        controller.onLoad();


        URL pong_single_location;
        pong_single_location = new File("src/sample/Pong1P.fxml").toURI().toURL();
        loader = new FXMLLoader(pong_single_location);
        loader.load();
        this.pong_single = loader.getController();
        new Scene(loader.getRoot());

        PongSinglePlayer controller2 = loader.getController();
        controller2.onLoad();


        primaryStage.setScene(menu.getScene());
        primaryStage.show();
    }


    public static Menu getMenu(){
        return menu;
    }


    public static PongMultiPlayer getMulitplayer(){
        return pong_multi;
    }


    public static PongSinglePlayer getSingleplayer(){
        return pong_single;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
