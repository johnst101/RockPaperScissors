/**
 * TODO
 *
 * @author Tyler Johnson (tjohson)
 * @version 1.0 Apr 11, 2024
 */

// Packages //
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RPSClient extends Application {
    /**TODO*/
    private Label title = new Label();
    /**TODO*/
    private Label status = new Label();
    /**TODO*/
    private Label player1ScoreTitle = new Label();
    /**TODO*/
    private Label player1Score = new Label();
    /**TODO*/
    private Label player2ScoreTitle = new Label();
    /**TODO*/
    private Label player2Score = new Label();
    /**TODO*/
    private String host = "localhost";
    /**TODO*/
    private DataInputStream fromServer;
    /**TODO*/
    private DataOutputStream toServer;
    /**TODO*/
    private boolean waiting = false;
    /**TODO*/
    private boolean play = true;
    private Button rock = new Button("Rock");
    /**TODO*/
    private Button paper = new Button("Paper");
    /**TODO*/
    private Button scissors = new Button("Scissors");
    /**TODO*/
    private enum Move {
        ROCK,
        PAPER,
        SCISSORS
    }

    /**
     * TODO
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Buttons customized to be the symbols
            // Rock button
            // Paper button
            // Scissors button
        // Hbox for move options
        // BorderPane to hold all content
            // title at top
            // hbox in center
            // status on bottom
            // player1 score title and number on left
            // player2 score title and number on right
        // Create Scene for pane
        // Set Scene
        // Show stage

        // connectToServer
    }

    /**
     * TODO
     */
    private void connectToServer() {
        // TRY //
        // Create Socket
        // Initialize input and output streams
        // CATCH //
        // print log or stack trace
        // New thread for ClientGameSession
    }

    private class ClientGameSession implements Runnable {
        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            // Receive player number connecting
            // If player1
                // Waiting for player 2 to join notification
                // Player number notification from server
                // Receive indicator of player2 join
                // Display notification of player2 join
                // Display notification to make move when ready
            // Else If player2
                // Make your move when ready
            // Start loop while
                // Send move to server
                    // button action for rock, paper or scissors
                // Wait for other player to also send move to server
                // Receive outcome from server
        }
    }

    /**
     * TODO
     */
    private void waitForOtherPlayer() throws InterruptedException {
        // Sleep thread until waiting is false
    }

    /**
     * TODO
     */
    private void sendMove(Move playerMove) throws IOException {
        // write enum selection based on button click action to server
    }

    /**
     * TODO
     */
    private void receiveOutcome() throws IOException {
        // Receive scores
            // update score labels
        // If a player reached 5 points
            // If player1 wins
                // play goes to false
                // If this client is player1
                    // Notify, "You Won!"
                // else if client is player2
                    // Notify, "Player 1 Won."
            // Else if player2 wins
                // play goes to false
                // If this client is player1
                    // Notify, "Player 2 Won!"
                // else if client is player2
                    // Notify, "You Won!"
        // Else If player1 wins round
            // play goes to false
            // If this client is player1
                // Notify, "You won this round!"
            // else if client is player2
                // Notify, "Player 1 Won this round."
        // Else If player2 wins round
            // play goes to false
            // If this client is player1
                // Notify, "Player 2 Won this round."
            // else if client is player2
                // Notify, "You Won this round!"
        // Else round draw
            // notify both players of round draw and pick again
    }

    /**
     * TODO
     */
    public static void main(String[] args) {
        launch(args);
    }
}
