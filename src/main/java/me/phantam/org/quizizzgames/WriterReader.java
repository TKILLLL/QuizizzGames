package me.phantam.org.quizizzgames;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WriterReader {
    public static List<String> charCorrectAnswer = new ArrayList<>();
    public List<Question> readQuestions() throws IOException {
        charCorrectAnswer.clear();
        List<Question> questions = new ArrayList<>();
        Path filePath = Path.of("C:\\Users\\Admin\\Documents\\project\\javafx Course\\Quizizz Games\\src\\main\\resources\\me\\phantam\\org\\quizizzgames\\questions\\question.txt");

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            Question question = null;
            List<String> answers = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (question != null) {
                        question.setAnswers(answers.toArray(new String[0]));
                        questions.add(question);
                    }
                    question = null;
                    answers = new ArrayList<>();
                } else if (question == null) {
                    question = new Question();
                    question.setQuestion(line);
                } else {
                    if (line.startsWith("*")) {
                        question.setCorrectAnswer(line.substring(1, 2));
                        charCorrectAnswer.add(line.substring(2));
                    }
                    answers.add(line);
                }
            }

            if (question != null) {
                question.setAnswers(answers.toArray(new String[0]));
                questions.add(question);
            }
        }

        return questions;
    }


    public static class Question {
        private String question;
        private String[] answers;
        private String correctAnswer;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String[] getAnswers() {
            return answers;
        }

        public void setAnswers(String[] answers) {
            this.answers = answers;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public void setCorrectAnswer(String correctAnswer) {
            this.correctAnswer = correctAnswer;
        }
    }

    public static List<String> getCorrectAnswers() {
        return new  ArrayList<String>(charCorrectAnswer);
    }
}
