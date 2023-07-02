import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Appinizializer extends Application {

    public static void main(String[] args) { launch(args); }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("view/loging.fxml")))));
        primaryStage.setTitle("Chat App");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
