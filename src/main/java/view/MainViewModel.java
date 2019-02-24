package view;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.io.OutputStream;

@Getter
public class MainViewModel {

    private final Stage stage;

    public MainViewModel(Stage primaryStage) {
        this.stage = primaryStage;
    }
}

class TextAreaOutputStream extends OutputStream {

    private TextArea textArea;

    public TextAreaOutputStream(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        Platform.runLater(() -> {
            textArea.appendText(String.valueOf((char) b));
        });
    }
}
