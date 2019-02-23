package qcha.voicerecorder.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class MainApplication extends Application {

    private Scene mainScene;
    private VoiceRecorderView voiceRecorderView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        voiceRecorderView = new VoiceRecorderView(new VoiceRecorderViewModel(primaryStage));

        mainScene = new Scene(voiceRecorderView);

        primaryStage.setResizable(false);
        primaryStage.setScene(mainScene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> System.exit(0));
    }
}
