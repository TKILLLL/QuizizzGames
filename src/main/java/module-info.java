module me.phantam.org.quizizzgames {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens me.phantam.org.quizizzgames to javafx.fxml;
    exports me.phantam.org.quizizzgames;
}