package me.phantam.org.quizizzgames;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TinhToan {

    private List<WriterReader.Question> questions;
    private Map<Integer, String> questionToCorrectAnswer = new HashMap<>();
    private String[] correctAnswers;
    private int currentQuestionIndex = 0;
    private QuizizzController1 quizizzController1;
    private WriterReader writerReader;

    public TinhToan(QuizizzController1 controller) {
        this.quizizzController1 = controller;
        this.writerReader = new WriterReader();
        try {
            this.questions = writerReader.readQuestions();
            this.correctAnswers = writerReader.getCorrectAnswers().toArray(new String[0]);
            for (int i = 0; i < questions.size(); i++) {
                questionToCorrectAnswer.put(i, questions.get(i).getCorrectAnswer());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCorrectAnswer(int questionIndex) {
        return questionToCorrectAnswer.get(questionIndex);
    }

    public boolean isQuizFinished() {
        return currentQuestionIndex >= questions.size();
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

    public void load() {
        if (!questions.isEmpty()) {
            WriterReader.Question currentQuestion = questions.get(currentQuestionIndex);
            quizizzController1.title.setText(currentQuestion.getQuestion());
            String[] answers = currentQuestion.getAnswers();
            for (int i = 0; i < answers.length; i++) {
                answers[i] = answers[i].replace("*", "");
            }
            quizizzController1.btnA.setText(answers[0]);
            quizizzController1.btnB.setText(answers[1]);
            quizizzController1.btnC.setText(answers[2]);
            quizizzController1.btnD.setText(answers[3]);
            quizizzController1.cau.setText((currentQuestionIndex + 1) + "/" + questions.size());
        }
    }

    public void nextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            load();
        } else {
            showQuizFinishedDialog();
        }
    }

    private void showQuizFinishedDialog() {
        quizizzController1.title.setText("Quiz finished!");
        quizizzController1.btnA.setDisable(true);
        quizizzController1.btnB.setDisable(true);
        quizizzController1.btnC.setDisable(true);
        quizizzController1.btnD.setDisable(true);

        if (quizizzController1.timeline != null) {
            quizizzController1.timeline.stop();
        }

        quizizzController1.time.setText("Finished!");
        System.out.println(quizizzController1.dem);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Platform.runLater(() -> {
            Alert result = new Alert(Alert.AlertType.CONFIRMATION);
            result.setTitle("Result");
            result.setHeaderText(null);
            result.setContentText("Your result: " + checkResult() + " points");
            ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.YES);
            result.getButtonTypes().setAll(ok);
            Optional<ButtonType> confirm = result.showAndWait();
            if (confirm.isPresent() && confirm.get().getButtonData() == ButtonBar.ButtonData.YES) {
                try {
                    changeSceneQuizizzMain(quizizzController1.title);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public String checkResult() {
        int score = 0;
        List<String> userAnswers = quizizzController1.dem;
        for (int i = 0; i < userAnswers.size(); i++) {
            if (i < correctAnswers.length && userAnswers.get(i).equals(getCorrectAnswer(i))) {
                score++;
            }
        }
        return String.valueOf(score * 1000);
    }
}
