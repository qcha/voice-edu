package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindow extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        MainViewModel viewModel = new MainViewModel(primaryStage);
        MainView view = new MainView(viewModel);

        Scene scene = new Scene(view);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.show();
    }
}
