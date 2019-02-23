package view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.deeplearning4j.examples.feedforward.classification.CreateDataSet;
import view.recorder.VoiceRecorderView;
import view.recorder.VoiceRecorderViewModel;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class MainView extends GridPane {
    private final MainViewModel viewModel;

    private Button training;
    private Button checking;

    private HBox panel;

    private ToggleGroup radioButtonsToggle;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;

    private TextArea logPanel;

    private VoiceRecorderViewModel voiceRecorderViewModel;
    private VoiceRecorderView voiceRecorderView;

    public MainView(MainViewModel model) {
        this.viewModel = model;

        setPadding(new Insets(20));
        setHgap(25);
        setVgap(15);

        initUserRadioButton();

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.getChildren().addAll(rb1, rb2, rb3);
        add(vBox, 0, 0);

        initButtonPanel();

        add(panel, 6, 0);

        initLogPanel();

        add(logPanel, 0, 4, 15, 10);

        voiceRecorderViewModel = new VoiceRecorderViewModel(1);
        voiceRecorderView = new VoiceRecorderView(voiceRecorderViewModel);

        add(voiceRecorderView, 0, 1);
    }

    private void initLogPanel() {
        logPanel = new TextArea("A lot of text");
    }

    private void initUserRadioButton() {
        radioButtonsToggle = new ToggleGroup();

        rb1 = new RadioButton("Голос 1");
        rb1.setId("1");
        rb1.setToggleGroup(radioButtonsToggle);
        rb1.setSelected(true);

        rb2 = new RadioButton("Голос 2");
        rb2.setId("2");
        rb2.setToggleGroup(radioButtonsToggle);

        rb3 = new RadioButton("Голос 3");
        rb3.setId("3");
        rb3.setToggleGroup(radioButtonsToggle);

        radioButtonsToggle.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (radioButtonsToggle.getSelectedToggle() != null) {
                RadioButton button = (RadioButton) radioButtonsToggle.getSelectedToggle();
                voiceRecorderViewModel.setAttempt(Integer.valueOf(button.getId()));
            }
        });
    }

    private void initButtonPanel() {
        training = new Button("Обучение");
        checking = new Button("Проверка");

        training.setOnAction(e -> {
            try {
                new CreateDataSet();
            } catch (IOException | UnsupportedAudioFileException e1) {
                e1.printStackTrace();
            }
        });

        panel = new HBox();
        panel.getChildren().addAll(training, checking);
        panel.setSpacing(20);
    }
}