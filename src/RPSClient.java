/**
 * TODO
 *
 * @author Tyler Johnson (tjohson)
 * @version 1.0 Apr 11, 2024
 */

// Packages //
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RPSClient extends Application implements RPSConstants {
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
    private boolean waiting = true;
    /**TODO*/
    private boolean play = true;
    private Button rock = new Button("Rock");
    /**TODO*/
    private Button paper = new Button("Paper");
    /**TODO*/
    private Button scissors = new Button("Scissors");

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
        Image rockImg = new Image("Rock.png");
        ImageView rockImgView = new ImageView(rockImg);
        Image paperImg = new Image("Paper.png");
        ImageView paperImgView = new ImageView(paperImg);
        Image scissorsImg = new Image("Scissors.png");
        ImageView scissorsImgView = new ImageView(scissorsImg);
        // Rock button
        rock.setGraphic(rockImgView);
        // Paper button
        paper.setGraphic(paperImgView);
        // Scissors button
        scissors.setGraphic(scissorsImgView);

        // Hbox for move options
        HBox buttonContainer = new HBox(rock, paper, scissors);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setSpacing(10);

        // VBox for player1Score
        VBox player1Container = new VBox(player1ScoreTitle, player1Score);
        player1Container.setAlignment(Pos.CENTER);
        player1Container.setSpacing(20);
        // VBox for player2Score
        VBox player2Container = new VBox(player2ScoreTitle, player2Score);
        player2Container.setAlignment(Pos.CENTER);
        player2Container.setSpacing(20);

        // BorderPane to hold all content
        BorderPane pane = new BorderPane(buttonContainer, title, player2Container, status, player1Container);

        // Create Scene for pane
        Scene scene = new Scene(pane);
        // Set Scene
        primaryStage.setScene(scene);
        // Show stage
        primaryStage.show();

        // connectToServer
        connectToServer();
    }

    /**
     * TODO
     */
    private void connectToServer() {
        // TRY //
        try {
            // Create Socket
            Socket socket = new Socket(host, 8000);
            // Initialize input and output streams
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread newSession = new Thread(new ClientGameSession());
        newSession.start();
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
            try {
                // Receive player number connecting
                int player = fromServer.readInt();
                // If player1
                if (player == PLAYER1) {
                    Platform.runLater(() -> {
                        // Player number notification from server
                        title.setText("Player 2");
                        // Waiting for player 2 to join notification
                        status.setText("Waiting for Player 2 to join");
                    });

                    // Receive indicator of player2 join
                    fromServer.readInt();

                    // Display notification of player2 join
                    Platform.runLater(() -> status.setText("Player 2 joined the game. Make your pick for the round."));
                } else if (player == PLAYER2) { // Else If player2
                    // Make your move when ready
                    Platform.runLater(() -> {
                        title.setText("Player 2");
                        status.setText("All players joined the game. Make your pick for the round.");
                    });
                }
                while (play) { // Start loop while
                    if (player == PLAYER1) {
                        // TODO: need a better way to handle the button press
                        // button action for rock, paper or scissors
                        // Send move to server
                        sendMove();

                    }
                    // Wait for other player to also send move to server
                    // Receive outcome from server
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * TODO
     */
    private void waitForOtherPlayer() throws InterruptedException {
        // Sleep thread until waiting is false
        while (waiting) {
            Thread.sleep(100);
        }

        waiting = true;
    }

    /**
     * TODO
     */
    private void sendMove(int playerMove) throws IOException {
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
