package dev.micellok.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("meetings-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Planner App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
