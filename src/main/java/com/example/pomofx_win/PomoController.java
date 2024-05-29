package com.example.pomofx_win;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import static java.time.Duration.ofSeconds;

public class PomoController implements Initializable {
    @FXML
    private Label myLabel = new Label();

    @FXML
    private Button myPlayButton, myPauseButton, myResetButton, mySubmitButton;

    @FXML
    private TextField myTextField;

    private int tSeconds = 1500; // time
    private int tMinutes = 25;

    private ObjectProperty<java.time.Duration> remainingDuration = new SimpleObjectProperty<>(java.time.Duration.ofSeconds(tSeconds));

    private Timeline countDownTimeLine = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event) ->
            remainingDuration.setValue(remainingDuration.get().minus(1, ChronoUnit.SECONDS))));

    @Override
    public void initialize(URL argo0, ResourceBundle arg1) {
        myLabel.textProperty().bind(Bindings.createStringBinding(() ->
                        String.format("%02d:%02d",
                                remainingDuration.get().toMinutesPart(),
                                remainingDuration.get().toSecondsPart()),
                remainingDuration));

        // Show number of cycles (remaining duration in seconds)
        countDownTimeLine.setCycleCount((int) remainingDuration.get().toSeconds());

        // show alert when time is up
        countDownTimeLine.setOnFinished(event -> new Alert(Alert.AlertType.INFORMATION).show());

    }

    public void playTimer() {
        if (remainingDuration.get().toMinutes() != 0 || remainingDuration.get().toSeconds() != 0) {
            countDownTimeLine.play();
        } else {
            System.out.println(remainingDuration.get().toSeconds() + " seconds");
        }
    }

    public void pauseTimer() {
        countDownTimeLine.pause();
    }

    public void resetTimer() {
        countDownTimeLine.stop();
        remainingDuration.set(java.time.Duration.ofMinutes(tMinutes));
    }

    public void submitTimer(ActionEvent event) {
        try {
            tMinutes = Integer.parseInt(myTextField.getText());
            if (tMinutes <= 59) {
                remainingDuration.set(java.time.Duration.ofMinutes(tMinutes));
                countDownTimeLine.setCycleCount((int) remainingDuration.get().getSeconds()); // Recalculate the cycle count
            } else {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Error");
                alert1.setContentText("Minutes exceeds 59");
                alert1.show();
            }
        } catch (NumberFormatException e) {
            // handle invalid input format Not a number
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid number.");
            alert.show();
        }

    }
}