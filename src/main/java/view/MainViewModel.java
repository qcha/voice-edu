package view;

import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class MainViewModel {

    private final Stage stage;

    public MainViewModel(Stage primaryStage) {
        this.stage = primaryStage;
    }
}
