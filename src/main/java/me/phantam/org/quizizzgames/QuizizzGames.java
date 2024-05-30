package me.phantam.org.quizizzgames;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class QuizizzGames {

    private List<WriterReader.Question> questions;

    @FXML
    private Label cau;

    @FXML
    public void initialize() {
        try {
            WriterReader writerReader = new WriterReader();
            questions = writerReader.readQuestions();
            cau.setText("Số lượng câu: " + questions.size());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load questions: " + e.getMessage());
        }
    }

    public void changeSceneQuizizzGames(Node node) throws IOException {
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("play-quizizz-games.fxml"));
        Parent playView = loader.load();
        Scene scene = new Scene(playView);
        QuizizzController1 controller = loader.getController();
        controller.setQuestions(questions);
        stage.setScene(scene);
    }

    @FXML
    protected void onStartButtonClick(ActionEvent event) throws IOException {
        Alert xacNhan = new Alert(Alert.AlertType.CONFIRMATION);
        xacNhan.setTitle("Confirmation");
        xacNhan.setHeaderText(null);
        xacNhan.setContentText("Do you want to start the game?");

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.NO);
        xacNhan.getButtonTypes().setAll(yes, no);
        Optional<ButtonType> confirm = xacNhan.showAndWait();

        if (confirm.isPresent() && confirm.get().getButtonData() == ButtonBar.ButtonData.YES) {
            Node node = (Node) event.getSource();
            changeSceneQuizizzGames(node);
        } else {
            xacNhan.close();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
