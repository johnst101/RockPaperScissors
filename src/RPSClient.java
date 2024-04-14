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
    private Button rock = new Button();
    /**TODO*/
    private Button paper = new Button();
    /**TODO*/
    private Button scissors = new Button();
    /**TODO*/
    private int move;
    /**TODO*/
    private Socket socket;
    /**TODO*/
    private Scene scene;
    /**TODO*/
    private Scene scene2;

    /**
     * TODO
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // PLAY SCENE CONTENT //
        // Rock button
        rock.setGraphic(new ImageView(new Image("Rock.png")));
        // Paper button
        paper.setGraphic(new ImageView(new Image("Paper.png")));
        // Scissors button
        scissors.setGraphic(new ImageView(new Image("Scissors.png")));
        disableButtons();

        Button quit = new Button("Quit Game");

        // Hbox for move options
        HBox buttonContainer = new HBox(rock, paper, scissors);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setSpacing(10);
        buttonContainer.setPadding(new Insets(10));

        // Title and Status containers
        VBox titleContainer = new VBox(title);
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.setPadding(new Insets(10));
        title.setFont(new Font("Arial", 24));
        VBox statusContainer = new VBox(status, quit);
        statusContainer.setSpacing(20);
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

        // END SCENE //
        Button quit1 = new Button("Quit Game");
        Button playAgain = new Button("Play Again");
        HBox endContainer = new HBox(quit1, playAgain);

        // BorderPane to hold all content
        BorderPane pane1 = new BorderPane(buttonContainer, titleContainer, player2Container, statusContainer, player1Container);
        BorderPane pane2 = new BorderPane();
        pane2.setCenter(endContainer);

        // Create Scene for pane
        scene = new Scene(pane1, 1200, 700);
        scene2 = new Scene(pane2, 500, 700);
        primaryStage.setTitle("Rock, Paper, Scissors");
        // Set Scene
        primaryStage.setScene(scene);
        primaryStage.minWidthProperty().bind(scene.widthProperty());
        primaryStage.minHeightProperty().bind(scene.heightProperty());
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

        quit.setOnAction(e -> {
            play = false;
            try {
                socket.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            primaryStage.close();
            System.exit(0);
        });

        quit1.setOnAction(e -> {
            play = false;
            try {
                socket.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            primaryStage.close();
            System.exit(0);
        });

        playAgain.setOnAction(e -> {
            try {
                socket.close();
                primaryStage.close();
                Platform.runLater(() -> {
                    try {
                        RPSClient newGame = RPSClient.class.newInstance();
                        newGame.start(new Stage());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
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
            socket = new Socket(host, 8000);
            // Initialize input and output streams
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            status.setText("Error occurred when connecting. Closing the game.");
            primaryStage.close();
            try {
                socket.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        Thread newSession = new Thread(new ClientGameSession(primaryStage));
        newSession.start();
    }

    /**
     * TODO
     */
    private class ClientGameSession implements Runnable {
        private Stage primaryStage;

        public ClientGameSession(Stage primaryStage) {
            this.primaryStage = primaryStage;
        }

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
                    receiveOutcome(player, primaryStage);
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
            try {
                if (fromServer.available() > 0) {
                    //TODO: handle ending game and skipping to final screen
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

        waiting = true;
    }

    /**
     * TODO
     */
    private void receiveOutcome(int player, Stage primaryStage) throws IOException, Exception {
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
                        primaryStage.setScene(scene2);
                    });
                } else { // else if client is player2
                    // Notify, "Player 1 Won."
                    Platform.runLater(() -> {
                        status.setText("Player 1 Won.");
                        primaryStage.setScene(scene2);
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
                        primaryStage.setScene(scene2);
                    });
                } else { // else if client is player2
                    // Notify, "You Won!"
                    Platform.runLater(() -> {
                        status.setText("You Won!");
                        primaryStage.setScene(scene2);
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
