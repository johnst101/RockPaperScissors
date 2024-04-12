/**
 * TODO
 *
 * @author Tyler Johnson (tjohson)
 * @version 1.0 Apr 11, 2024
 */

// Packages //
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class RPSServer extends Application {
    /**
     * TODO
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create the simple GUI setting
        TextArea log = new TextArea();
        Scene scene = new Scene(new ScrollPane(log), 450, 200);
        primaryStage.setTitle("Rock, Paper, Scissors Server");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Create server session on new thread
        Thread serverThread = new Thread(new ServerSession());
        serverThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}