package view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logging.MyStaticOutputStreamAppender;
import org.deeplearning4j.examples.feedforward.classification.CreateDataSet;
import org.deeplearning4j.examples.feedforward.classification.MLPClassifierLinear;
import view.recorder.VoiceRecorderView;
import view.recorder.VoiceRecorderViewModel;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

import static recorder.Constants.PATH_TO_CSV_TRAIN;
import static recorder.Constants.PATH_TO_WAV_TRAIN;

public class MainView extends GridPane {
    private final MainViewModel viewModel;

    private Button training;
    private Button checking;

    private HBox panel;

    private ToggleGroup radioButtonsToggle;
    private RadioButton rb1;
    private RadioButton rb2;
    private Button plusBtn;
    private Button minusBtn;
    private VBox radioButtons1 = new VBox();
    private VBox radioButtons2 = new VBox();

    private TextArea logPanel;

    private VoiceRecorderViewModel voiceRecorderViewModel;
    private VoiceRecorderView voiceRecorderView;

    public MainView(MainViewModel model) {
        this.viewModel = model;
        voiceRecorderViewModel = new VoiceRecorderViewModel(1);
        voiceRecorderView = new VoiceRecorderView(voiceRecorderViewModel);

        setPadding(new Insets(20));
        setHgap(25);
        setVgap(15);

        initLogPanel();
        initButtonPanel();

        add(voiceRecorderView, 0, 0);

        add(panel, 0, 1);

        initUserRadioButton();

        HBox controlRadio = new HBox();
        controlRadio.getChildren().addAll(plusBtn, minusBtn);
        add(controlRadio, 0, 2);

        radioButtons1.setSpacing(10);
        radioButtons2.setSpacing(10);
        HBox radioButtons = new HBox(radioButtons1, radioButtons2);
        radioButtons.setSpacing(25);
        radioButtons1.getChildren().addAll(rb1, rb2);
        add(radioButtons, 0, 3);

        AnchorPane logPane = new AnchorPane(logPanel);
        add(logPane, 4, 0, 5, 5);
    }

    private void initLogPanel() {
        logPanel = new TextArea();

        OutputStream os = new TextAreaOutputStream(logPanel);
        MyStaticOutputStreamAppender.setStaticOutputStream(os);
    }

    private void initUserRadioButton() {
        radioButtonsToggle = new ToggleGroup();

        plusBtn = new Button("+");
        minusBtn = new Button("-");

        rb1 = new RadioButton("Голос 1");
        rb1.setId("1");
        rb1.setToggleGroup(radioButtonsToggle);
        rb1.setSelected(true);

        rb2 = new RadioButton("Голос 2");
        rb2.setId("2");
        rb2.setToggleGroup(radioButtonsToggle);

        AtomicInteger i = new AtomicInteger(3);
        plusBtn.setOnAction(e -> {
            if (i.get() <= 6) {
                RadioButton rb = new RadioButton("Голос " + i);
                rb.setId(String.valueOf(i.getAndIncrement()));
                rb.setToggleGroup(radioButtonsToggle);
                (i.get() > 4 ? radioButtons2 : radioButtons1).getChildren().add(rb);
            }
        });

        minusBtn.setOnAction(e -> {
            if (i.get() > 3) {
                radioButtonsToggle.getToggles().remove(i.get() - 2);
                (i.get() > 4 ? radioButtons2 : radioButtons1).getChildren()
                        .remove(i.get() - (i.get() > 4 ? 5 : 2));
                i.getAndDecrement();
            }
        });


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

        training.disableProperty().bind(voiceRecorderViewModel.getIsRecording());
        checking.disableProperty().bind(voiceRecorderViewModel.getIsRecording());



        training.setOnAction(e -> {
            try {
                CreateDataSet.createCSVFile(PATH_TO_CSV_TRAIN, PATH_TO_WAV_TRAIN);
            } catch (IOException | UnsupportedAudioFileException e1) {
                e1.printStackTrace();
            }
        });

        checking.setOnAction(e -> {
            try {
                if (CreateDataSet.getNumOutputs() != 0) {
                    new MLPClassifierLinear(CreateDataSet.getNumOutputs());
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        panel = new HBox();
        panel.getChildren().addAll(training, checking);
        panel.setSpacing(10);
    }
}
