package me.phantam.org.quizizzgames;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuizizzController1 {

    public TinhToan tinhToan;
    public Timeline timeline;
    private boolean answerSelected = false;
    private List<WriterReader.Question> questions;
    public ArrayList<String> dem = new ArrayList<>();

    @FXML
    public Button btnA;
    @FXML
    public Button btnB;
    @FXML
    public Button btnC;
    @FXML
    public Button btnD;
    @FXML
    public Label title;
    @FXML
    public Label time;
    @FXML
    public Label cau;

    @FXML
    public void initialize() {
        tinhToan = new TinhToan(this);
        tinhToan.load();
        startTimer();
    }

    public void changeSceneQuizizzMain(Node node) throws IOException {
        Stage stage = (Stage) node.getScene().getWindow();
        if (stage == null) {
            System.err.println("Stage is null. Node does not have a window associated with it.");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-quizizz-games.fxml"));
        Parent playView = loader.load();
        Scene scene = new Scene(playView);
        stage.setScene(scene);
    }

    @FXML
    protected void onXButtonClick(ActionEvent event) throws IOException {
        Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
        warning.setTitle("Confirmation");
        warning.setHeaderText("Do you want to exit?");
        warning.setContentText(null);

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.NO);
        warning.getButtonTypes().setAll(yes, no);
        Optional<ButtonType> confirm = warning.showAndWait();

        if (confirm.isPresent() && confirm.get().getButtonData() == ButtonBar.ButtonData.YES) {
            Alert res = new Alert(Alert.AlertType.CONFIRMATION);
            res.setTitle("Result");
            res.setHeaderText(null);
            res.setContentText("result: " + tinhToan.checkResult());
            ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.YES);
            res.getButtonTypes().setAll(ok);
            Optional<ButtonType> result = res.showAndWait();
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.YES) {
                res.close();
            }
            Node node = (Node) event.getSource();
            changeSceneQuizizzMain(node);
        } else {
            warning.close();
        }
    }

    @FXML
    protected void onAButtonClick(ActionEvent event) {
        handleAnswer("A");
    }

    @FXML
    protected void onBButtonClick(ActionEvent event) {
        handleAnswer("B");
    }

    @FXML
    protected void onCButtonClick(ActionEvent event) {
        handleAnswer("C");
    }

    @FXML
    protected void onDButtonClick(ActionEvent event) {
        handleAnswer("D");
    }

    private void handleAnswer(String answer) {
        answerSelected = true;
        System.out.println("Bạn đã chọn: " + answer);
        dem.add(answer);
        timeline.stop();
        tinhToan.nextQuestion();
        if (!tinhToan.isQuizFinished()) {
            startTimer();
        }
    }

    private void handleTimeout() {
        if (!answerSelected) {
            System.out.println("Bạn đã nhập: X");
            dem.add("X");
            tinhToan.nextQuestion();
            if (!tinhToan.isQuizFinished()) {
                startTimer();
            }
        }
        answerSelected = false;
    }

    private void startTimer() {
        if (tinhToan.isQuizFinished()) {
            if (timeline != null) {
                timeline.stop();
            }
            return;
        }

        if (timeline != null) {
            timeline.stop();
        } else {
            final int[] timeLeft = {15};
            time.setText(String.valueOf(timeLeft[0]));
            answerSelected = false;
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                timeLeft[0]--;
                time.setText(String.valueOf(timeLeft[0]));
                if (timeLeft[0] <= 0) {
                    timeline.stop();
                    handleTimeout();
                }
            }));
            timeline.setCycleCount(15);
            timeline.play();
        }
    }


    public void setQuestions(List<WriterReader.Question> questions) {
        this.questions = questions;
    }
}
