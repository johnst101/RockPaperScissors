/**
 * Runs each sever side game session on a different thread
 * and allows for clients to play the game seamlessly while other
 * clients are playing on a different thread.
 *
 * @author Tyler Johnson (tjohson)
 * @version 1.0 Apr 14, 2024
 */

// Packages //
import javafx.scene.control.TextArea;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerGameSession implements Runnable, RPSConstants {
    /**Connection to Player 1's client*/
    private Socket player1;
    /**Connection to Player 2's client*/
    private Socket player2;
    /**The server log visualization element*/
    private TextArea log;
    /**Connection to Player 1's client to send messages.*/
    private DataOutputStream toPlayer1;
    /**Connection to Player 1's client to receive messages.*/
    private DataInputStream fromPlayer1;
    /**Connection to Player 2's client to send messages.*/
    private DataOutputStream toPlayer2;
    /**Connection to Player 2's client to receive messages.*/
    private DataInputStream fromPlayer2;
    /**Number of wins for Player 1.*/
    private int player1Wins = 0;
    /**Number of wins for Player 2.*/
    private int player2Wins = 0;
    /**List of the client connections in the current session.*/
    private List<Socket> clients;

    /**
     * Creates a new game session with the player Sockets and log
     * to run the game.
     *
     * @param player1 The Socket for the first player connected.
     * @param player2 The Socket for the second player connected.
     * @param log The server log in case of any exceptions to write.
     */
    public ServerGameSession(Socket player1, Socket player2, TextArea log) {
        this.player1 = player1;
        this.player2 = player2;
        this.log = log;
        this.clients = new ArrayList<>();
        clients.add(player1);
        clients.add(player2);
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
        // TRY //
        try {
            fromPlayer1 = new DataInputStream(player1.getInputStream());
            fromPlayer2 = new DataInputStream(player2.getInputStream());
            toPlayer1 = new DataOutputStream(player1.getOutputStream());
            toPlayer2 = new DataOutputStream(player2.getOutputStream());

            // Infinite loop until win or draw state reached
            while(true) {
                // Receive move from player1
                int player1Move = fromPlayer1.readInt();
                // Receive move from player2
                int player2Move = fromPlayer2.readInt();
                // Check for round win or draw
                int result = compareMoves(player1Move, player2Move);
                // If player1 move > player2 move
                if (result == 1) {
                    // player1 round win
                    // notify players of results
                    toPlayer1.writeInt(PLAYER1_ROUND_WIN);
                    toPlayer2.writeInt(PLAYER1_ROUND_WIN);
                    player1Wins++;
                    // Send moves to each player and updated score
                    sendResults(toPlayer1, toPlayer2, player1Move, player2Move, player1Wins, player2Wins);

                } else if (result == 2) { // Else if player2 move > player1 move
                    // player2 round win
                    // notify players of results
                    toPlayer1.writeInt(PLAYER2_ROUND_WIN);
                    toPlayer2.writeInt(PLAYER2_ROUND_WIN);
                    player2Wins++;
                    // Send moves to each player and updated score
                    sendResults(toPlayer1, toPlayer2, player1Move, player2Move, player1Wins, player2Wins);
                } else { // Else
                    // round draw
                    // notify players of results
                    toPlayer1.writeInt(ROUND_DRAW);
                    toPlayer2.writeInt(ROUND_DRAW);
                    // Send moves to each player and updated score
                    sendResults(toPlayer1, toPlayer2, player1Move, player2Move, player1Wins, player2Wins);
                }
            }
        } catch (IOException e) {
            handleDisconnection();
        } catch (Exception e) {
            handleDisconnection();
        }
    }

    /**
     * Compares the moves from both players and returns the result
     * depending on who is the winner of the round or if it is a draw.
     *
     * @param player1Move The integer representation of player 1's move.
     * @param player2Move The integer representation of player 2's move.
     * @return The integer result for the round winner or a draw.
     */
    private int compareMoves(int player1Move, int player2Move) throws Exception {
        // If player1Move equals Rock
        if (player1Move == ROCK) {
            // If player2Move equals Rock
            if (player2Move == ROCK) {
                // Then return 3
                return 3;
            }
            // If player2Move equals Paper
            if (player2Move == PAPER) {
                // Then return 2
                return 2;
            }
            // If player2Move equals Scissors
            if (player2Move == SCISSORS) {
                // Then return 1
                return 1;
            }
        }
        // If player1Move equals Paper
        if (player1Move == PAPER) {
            // If player2Move equals Rock
            if (player2Move == ROCK) {
                // Then return 1
                return 1;
            }
            // If player2Move equals Paper
            if (player2Move == PAPER) {
                // Then return 3
                return 3;
            }
            // If player2Move equals Scissors
            if (player2Move == SCISSORS) {
                // Then return 2
                return 2;
            }
        }
        // If player1Move equals Scissors
        if (player1Move == SCISSORS) {
            // If player2Move equals Rock
            if (player2Move == ROCK) {
                // Then return 2
                return 2;
            }
            // If player2Move equals Paper
            if (player2Move == PAPER) {
                // Then return 1
                return 1;
            }
            // If player2Move equals Scissors
            if (player2Move == SCISSORS) {
                // Then return 3
                return 3;
            }
        }
        throw new Exception();
    }

    /**
     * Writes all the integer values of Player 1 and Player 2 for their
     * respective moves, and win count.
     *
     * @param toPlayer1 The data output connection to Player 1.
     * @param toPlayer2 The data output connection to Player 2.
     * @param player1Move The integer representation of Player 1's move.
     * @param player2Move The integer representation of Player 2's move.
     * @param player1Wins The integer representation of Player 1's wins.
     * @param player2Wins The integer representation of Player 2's wins.
     * @throws IOException Throws an IOException if there are issues with sending the results.
     */
    private void sendResults(DataOutputStream toPlayer1, DataOutputStream toPlayer2, int player1Move, int player2Move, int player1Wins, int player2Wins) throws IOException {
        toPlayer1.writeInt(player1Wins);
        toPlayer1.writeInt(player2Wins);
        toPlayer2.writeInt(player1Wins);
        toPlayer2.writeInt(player2Wins);
        toPlayer1.writeInt(player1Move);
        toPlayer1.writeInt(player2Move);
        toPlayer2.writeInt(player1Move);
        toPlayer2.writeInt(player2Move);
    }

    /**
     * In case of disconnection during the game, this method should
     * notify the user still connected that the other player disconnected.
     */
    private void handleDisconnection() {
        if (!player1.isClosed()) {

        } else {
            clients.remove(player1);
            for (Socket client : clients) {
                if (client == player2) {
                    try {
                        toPlayer2.writeInt(9);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (!player2.isClosed()) {

        } else {
            clients.remove(player2);
            for (Socket client : clients) {
                if (client == player1) {
                    try {
                        toPlayer1.writeInt(9);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
