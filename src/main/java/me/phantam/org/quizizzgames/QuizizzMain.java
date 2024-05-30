package me.phantam.org.quizizzgames;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class QuizizzMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("main-quizizz-games.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Quizizz Games | phantam");
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/me/phantam/org/quizizzgames/img/quizizzgame.png")));
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
