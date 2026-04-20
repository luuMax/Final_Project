import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Label label = new Label("Hello JavaFX!");
        Scene scene = new Scene(label, 300, 200);

        stage.setTitle("My App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

//cd ~/Documents/GitHub/Final_Project
//javac --module-path javafx-sdk-26/lib --add-modules javafx.controls,javafx.graphics *.java
//java --module-path javafx-sdk-26/lib --add-modules javafx.controls,javafx.graphics Main