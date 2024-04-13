/**
 * Starts a new server side connection for a Rock, Paper, Scissors
 * game and allow connections from a client to play a game against
 * another client.
 *
 * @author Tyler Johnson (tjohson)
 * @version 1.0 Apr 13, 2024
 */

// Packages //
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class RPSServer extends Application implements RPSConstants {
    /**
     * Acts as the main method in the application. Sets up the visual
     * server log and creates a new server session thread to after
     * the application is available.
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        // Create the simple GUI setting
        TextArea log = new TextArea();
        Scene scene = new Scene(new ScrollPane(log), 500, 200);
        primaryStage.setTitle("Rock, Paper, Scissors Server");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Create server session on new thread
        Thread serverThread = new Thread(new ServerSession(log));
        serverThread.start();
    }

    /**
     * Placeholder for arguments as the start() method runs
     * the application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}