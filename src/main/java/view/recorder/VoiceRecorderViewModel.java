package view.recorder;

import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class VoiceRecorderViewModel {
    private int attempt;
    private Stage stage;

    public VoiceRecorderViewModel(Stage stage, int voiceNum) {
        this.attempt = voiceNum;
        this.stage = stage;
    }

    public void increaseAttempt() {
        attempt++;
    }
}
