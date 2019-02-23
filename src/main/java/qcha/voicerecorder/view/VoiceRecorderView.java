package qcha.voicerecorder.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import qcha.voicerecorder.AudioController;

import javax.sound.sampled.LineUnavailableException;
import java.io.File;

import static qcha.voicerecorder.Constants.TIMER;

@Slf4j
public class VoiceRecorderView extends BorderPane {
    private Button recordBtn;
    private Button stopBtn;
    private AudioController controller;
    private Label recordingLabel;
    private VoiceRecorderViewModel voiceRecorderViewModel;

    private DoubleProperty timeSeconds = new SimpleDoubleProperty();
    private Duration time = Duration.ZERO;
    private Timeline timeline;
    private Label timerLabel;

    private double length = 125;

    public VoiceRecorderView(VoiceRecorderViewModel viewModel) {
        this.voiceRecorderViewModel = viewModel;

        initButtons();
        initLabel();
        initTimer();

        setTop(recordingLabel);
        setCenter(new HBox(recordBtn, stopBtn));
        setBottom(timerLabel);
        timerLabel.setAlignment(Pos.BOTTOM_RIGHT);
    }

    private void initTimer() {
        timerLabel = new Label();
        timerLabel.textProperty().bind(timeSeconds.asString("%s sec"));
        timerLabel.setFont(new Font("Cambria", 30));
        timeline = new Timeline(new KeyFrame(Duration.millis(100), t -> {
            Duration duration = ((KeyFrame) t.getSource()).getTime();
            time = time.add(duration);
            timeSeconds.set(time.toSeconds());
            if (Math.abs(time.toSeconds() - TIMER) < 1E-6){ // compare doubles
                log.debug("Timer over.");
                stopBtn.fire();
            }
        })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void initLabel() {
        recordingLabel = new Label("Идет запись...");
        recordingLabel.setFont(new Font("Cambria", 30));
        recordingLabel.setVisible(false);
    }

    private void initButtons() {
        Image start = new Image(getClass().getResourceAsStream("/start.png"));
        Image stop = new Image(getClass().getResourceAsStream("/stop.png"));

        recordBtn = new Button() {
            {
                setPrefSize(length, length);
                setGraphic(new ImageView(start));
                setOnAction(event -> {
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    File directory = directoryChooser.showDialog(voiceRecorderViewModel.getStage());

                    if (directory != null) {
                        setDisable(true);
                        stopBtn.setDisable(false);

                        try {
                            controller = new AudioController(voiceRecorderViewModel.getAttempt(),
                                    directory.getAbsolutePath());
                        } catch (LineUnavailableException ex) {
                            log.error("Error while initializing audio controller: {}", ex);

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Ошибка инициализации микрофона");

                            alert.setHeaderText("Невозможно начать запись. Возможно, у вас не настроен микрофон?");

                            alert.showAndWait();
                            System.exit(-1);
                        }

                        recordingLabel.setVisible(true);
                        controller.startRecord();
                        voiceRecorderViewModel.increaseAttempt();
                        log.debug("Start timer.");
                        timeline.play();
                    }
                });
            }
        };

        stopBtn = new Button() {
            {
                setPrefSize(length, length);
                setGraphic(new ImageView(stop));
                setDisable(true);
                setOnAction(e -> {
                    setDisable(true);
                    recordBtn.setDisable(false);
                    recordingLabel.setVisible(false);
                    controller.stopRecord();
                    time = Duration.ZERO;
                    log.debug("Stop timer.");
                    timeline.stop();
                });
            }
        };
    }


}
