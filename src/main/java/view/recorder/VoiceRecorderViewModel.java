package view.recorder;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoiceRecorderViewModel {
    private BooleanProperty isRecording = new SimpleBooleanProperty(false);
    private int attempt;

    public VoiceRecorderViewModel(int attempt) {
        this.attempt = attempt;
    }

    public void setIsRecording(boolean isRecording) {
        this.isRecording.set(isRecording);
    }
}
