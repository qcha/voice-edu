package view.recorder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoiceRecorderViewModel {
    private int attempt;

    public VoiceRecorderViewModel(int attempt) {
        this.attempt = attempt;
    }
}
