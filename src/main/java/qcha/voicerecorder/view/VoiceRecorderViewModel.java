package qcha.voicerecorder.view;

import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class VoiceRecorderViewModel {
    private Stage stage;
    private int attempt = 1;

    VoiceRecorderViewModel(Stage stage) {
        this.stage = stage;
    }

    public void increaseAttempt() {
        attempt++;
    }
}
