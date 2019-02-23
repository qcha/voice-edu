package view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import view.recorder.VoiceRecorderView;
import view.recorder.VoiceRecorderViewModel;

public class MainView extends GridPane {
    private final MainViewModel viewModel;

    private HBox panel;

    private ToggleGroup radioButtonsToggle;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;

    private TextArea logPanel;

    public MainView(MainViewModel model) {
        this.viewModel = model;

        setPadding(new Insets(20));
        setHgap(25);
        setVgap(15);

        initUserRadioButton();

        add(rb1, 0, 0);
        add(rb2, 0, 1);
        add(rb3, 0, 2);

        initButtonPanel();

        add(panel, 6, 0);

        initLogPanel();

        add(logPanel, 0, 6, 10, 10);

        VoiceRecorderView voiceRecorderView = new VoiceRecorderView(new VoiceRecorderViewModel(viewModel.getStage(), 0));

        add(voiceRecorderView, 6, 1);
    }

    private void initLogPanel() {
        logPanel = new TextArea("A lot of text");
    }

    private void initUserRadioButton() {
        radioButtonsToggle = new ToggleGroup();

        rb1 = new RadioButton("Голос 1");
        rb1.setToggleGroup(radioButtonsToggle);
        rb1.setSelected(true);

        rb2 = new RadioButton("Голос 2");
        rb2.setToggleGroup(radioButtonsToggle);

        rb3 = new RadioButton("Голос 3");
        rb3.setToggleGroup(radioButtonsToggle);

        radioButtonsToggle.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (radioButtonsToggle.getSelectedToggle() != null) {
                RadioButton button = (RadioButton) radioButtonsToggle.getSelectedToggle();
                System.out.println("Button: " + button.getText());
            }
        });
    }

    private void initButtonPanel() {
        Button recording = new Button("Запись");
        Button training = new Button("Обучение");
        Button checking = new Button("Проверка");

        panel = new HBox();
        panel.getChildren().addAll(recording, training, checking);
        panel.setSpacing(20);
    }
}
