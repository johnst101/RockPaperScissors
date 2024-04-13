/**
 * TODO
 *
 * @author Tyler Johnson (tjohson)
 * @version 1.0 Apr 11, 2024
 */

// Packages //
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
    /**TODO*/
    private int move;

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
        disableButtons();

        // Hbox for move options
        HBox buttonContainer = new HBox(rock, paper, scissors);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setSpacing(10);

        // Title and Status containers
        VBox titleContainer = new VBox(title);
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.setPadding(new Insets(10));
        title.setFont(new Font("Arial", 24));
        VBox statusContainer = new VBox(status);
        statusContainer.setAlignment(Pos.CENTER);
        statusContainer.setPadding(new Insets(10));
        status.setFont(new Font("Arial", 16));

        // VBox for player1Score
        VBox player1Container = new VBox(player1ScoreTitle, player1Score);
        player1Container.setAlignment(Pos.CENTER);
        player1Container.setSpacing(20);
        player1Container.setPadding(new Insets(10));
        player1ScoreTitle.setFont(new Font("Arial", 16));
        player1Score.setFont(new Font("Arial", 16));
        // VBox for player2Score
        VBox player2Container = new VBox(player2ScoreTitle, player2Score);
        player2Container.setAlignment(Pos.CENTER);
        player2Container.setSpacing(20);
        player2Container.setPadding(new Insets(10));
        player2ScoreTitle.setFont(new Font("Arial", 16));
        player2Score.setFont(new Font("Arial", 16));

        // BorderPane to hold all content
        BorderPane pane = new BorderPane(buttonContainer, titleContainer, player2Container, statusContainer, player1Container);

        // Create Scene for pane
        Scene scene = new Scene(pane, 1200, 700);
        primaryStage.setTitle("Rock, Paper, Scissors");
        // Set Scene
        primaryStage.setScene(scene);
        // Show stage
        primaryStage.show();

        rock.setOnAction(e -> {
            move = ROCK;
            status.setText("Waiting for the other player to make their move");
            waiting = false;
        });

        paper.setOnAction(e -> {
            move = PAPER;
            status.setText("Waiting for the other player to make their move");
            waiting = false;
        });

        scissors.setOnAction(e -> {
            move = SCISSORS;
            status.setText("Waiting for the other player to make their move");
            waiting = false;
        });

        // connectToServer
        connectToServer(primaryStage);
    }

    /**
     * TODO
     */
    private void connectToServer(Stage primaryStage) {
        // TRY //
        try {
            // Create Socket
            Socket socket = new Socket(host, 8000);
            // Initialize input and output streams
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            status.setText("Error occurred when connecting. Closing the game.");
            primaryStage.close();
        }

        Thread newSession = new Thread(new ClientGameSession());
        newSession.start();
    }

    /**
     * TODO
     */
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
                        title.setText("Player 1");
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
                while (play) {
                    enableButtons();
                    waitForPlayer();
                    toServer.writeInt(move);
                    disableButtons();
                    receiveOutcome(player);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * TODO
     */
    private void waitForPlayer() throws InterruptedException {
        // Sleep thread until waiting is false
        while (waiting) {
            Thread.sleep(100);
        }

        waiting = true;
    }

    /**
     * TODO
     */
    private void receiveOutcome(int player) throws IOException, Exception {
        // Receive round winner
        int roundWinner = fromServer.readInt();
        // Receive scores
        int player1Wins = fromServer.readInt();
        int player2Wins = fromServer.readInt();
        // update score labels
        Platform.runLater(() -> {
            player1Score.setText(String.valueOf(player1Wins));
            player2Score.setText(String.valueOf(player2Wins));
        });
        // Receive moves
        int player1Move = fromServer.readInt();
        String move1 = moveAsString(player1Move);
        int player2Move = fromServer.readInt();
        String move2 = moveAsString(player2Move);
        Platform.runLater(() -> {
            player1ScoreTitle.setText("Player 1: " + move1);
            player2ScoreTitle.setText("Player 2: " + move2);
        });

        if (roundWinner == PLAYER1_ROUND_WIN) {
            if (player == PLAYER1) {
                Platform.runLater(() -> {
                    status.setText("You won that round!");
                });
            } else {
                Platform.runLater(() -> {
                    status.setText("Player 1 won that round.");
                });
            }
        } else if (roundWinner == PLAYER2_ROUND_WIN) {
            if (player == PLAYER1) {
                Platform.runLater(() -> {
                    status.setText("Player 2 won that round.");
                });
            } else {
                Platform.runLater(() -> {
                    status.setText("You won that round!");
                });
            }
        } else {
            Platform.runLater(() -> {
                status.setText("That round was a draw.");
            });
        }
        // If a player reached 5 points
        if (player1Wins == 5 || player2Wins == 5) {
            if (player1Wins == 5) {
                // play goes to false
                play = false;
                // If this client is player1
                if (player == PLAYER1) {
                    // Notify, "You Won!"
                    Platform.runLater(() -> {
                        status.setText("You Won!");
                    });
                } else { // else if client is player2
                    // Notify, "Player 1 Won."
                    Platform.runLater(() -> {
                        status.setText("Player 1 Won.");
                    });
                }
            } else if (player2Wins == 5) {
                // play goes to false
                play = false;
                // If this client is player1
                if (player == PLAYER1) {
                    // Notify, "Player 2 Won."
                    Platform.runLater(() -> {
                        status.setText("Player 2 Won.");
                    });
                } else { // else if client is player2
                    // Notify, "You Won!"
                    Platform.runLater(() -> {
                        status.setText("You Won!");
                    });
                }
            }
        }
    }

    /**
     * TODO
     */
    private void enableButtons() {
        rock.setDisable(false);
        paper.setDisable(false);
        scissors.setDisable(false);
    }

    /**
     * TODO
     */
    private void disableButtons() {
        rock.setDisable(true);
        paper.setDisable(true);
        scissors.setDisable(true);
    }

    // TODO
    private String moveAsString(int move) throws Exception {
        if (move == ROCK) {
            return "Rock";
        }
        if (move == PAPER) {
            return "Paper";
        }
        return "Scissors";
    }

    /**
     * TODO
     */
    public static void main(String[] args) {
        launch(args);
    }
}
