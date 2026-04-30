import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


//currently broken, we using swing
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
 // (java sdk 26 for silicon mac): 

 //how to run main.java normally: (faster way below)
//cd ~/Documents/GitHub/Final_Project
//javac --module-path javafx-sdk-26/lib --add-modules javafx.controls,javafx.graphics *.java
//java --module-path javafx-sdk-26/lib --add-modules javafx.controls,javafx.graphics Main


//faster way: 
// echo 'javac --module-path javafx-sdk-26/lib --add-modules javafx.controls,javafx.graphics *.java && java --module-path javafx-sdk-26/lib --add-modules javafx.controls,javafx.graphics Main' > run.sh
// run the command, then run bash run.sh to run main.java (only for java sdk 26 computers)