/**
 * TODO
 *
 * @author Tyler Johnson (tjohson)
 * @version 1.0 Apr 11, 2024
 */

// Packages //
import javafx.scene.control.TextArea;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;

public class ServerGameSession implements Runnable, RPSConstants {
    /**TODO*/
    private Socket player1;
    /**TODO*/
    private Socket player2;
    /**TODO*/
    private TextArea log;
    /**TODO*/
    private int gameNumber;
    /**TODO*/
    private DataOutputStream toPlayer1;
    /**TODO*/
    private DataInputStream fromPlayer1;
    /**TODO*/
    private DataOutputStream toPlayer2;
    /**TODO*/
    private DataInputStream fromPlayer2;
    private int player1Wins = 0;
    private int player2Wins = 0;

    /**
     * TODO
     */
    public ServerGameSession(int gameNumber, DataOutputStream toPlayer1, DataOutputStream toPlayer2, Socket player1, Socket player2, TextArea log) {
        this.gameNumber = gameNumber;
        this.toPlayer1 = toPlayer1;
        this.toPlayer2 = toPlayer2;
        this.player1 = player1;
        this.player2 = player2;
        this.log = log;
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
            // Infinite loop until win or draw state reached
            while(true) {
                // Notify players to choose move
                toPlayer1.writeInt(1);
                toPlayer2.writeInt(1);
                // Receive move from player1
                int player1Move = fromPlayer1.readInt();
                // TODO: Notify waiting for player2 to make move???
                // Receive move from player2
                int player2Move = fromPlayer2.readInt();
                // TODO: Notify waiting for player1 to make move???
                // Check for round win or draw
                int result = compareMoves(player1Move, player2Move);
                // If player1 move > player2 move
                if (result == 1) {
                    // player1 round win
                    // notify players of results
                    toPlayer1.writeInt(PLAYER1_ROUND_WIN);
                    toPlayer2.writeInt(PLAYER1_ROUND_WIN);
                    // Send moves to each player and updated score
                    sendResults(toPlayer1, toPlayer2, player1Move, player2Move);

                } else if (result == 2) { // Else if player2 move > player1 move
                    // player2 round win
                    // notify players of results
                    toPlayer1.writeInt(PLAYER2_ROUND_WIN);
                    toPlayer2.writeInt(PLAYER2_ROUND_WIN);
                    // Send moves to each player and updated score
                    sendResults(toPlayer1, toPlayer2, player1Move, player2Move);
                } else { // Else
                    // round draw
                    // notify players of results
                    toPlayer1.writeInt(ROUND_DRAW);
                    toPlayer2.writeInt(ROUND_DRAW);
                    // Send moves to each player and updated score
                    sendResults(toPlayer1, toPlayer2, player1Move, player2Move);
                    // continue
                    continue;
                }
                // Check for full game win
                // If player1Wins equals 5 AND player2Wins less than 5
                if (player1Wins == 5 && player2Wins < 5) {
                    // Notify players that player1 won the game
                    toPlayer1.writeInt(PLAYER1_TOTAL_WIN);
                    toPlayer2.writeInt(PLAYER1_TOTAL_WIN);
                    // Send moves to each player and updated score
                    sendResults(toPlayer1, toPlayer2, player1Move, player2Move);
                }
                // If player2Wins equals 5 AND player1Wins less than 5
                if (player2Wins == 5 && player1Wins < 5) {
                    // Notify players that player2 won the game
                    toPlayer1.writeInt(PLAYER2_TOTAL_WIN);
                    toPlayer2.writeInt(PLAYER2_TOTAL_WIN);
                    // Send moves to each player and updated score
                    sendResults(toPlayer1, toPlayer2, player1Move, player2Move);
                }
            }
        } catch (IOException e) {
             e.printStackTrace();
             log.appendText(new Date() + ": \n" + "\t" + Arrays.toString(e.getStackTrace()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO
     *
     * @param player1Move
     * @param player2Move
     * @return
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
     * TODO
     */
    private void sendResults(DataOutputStream toPlayer1, DataOutputStream toPlayer2, int player1Move, int player2Move) throws IOException {
        toPlayer1.writeInt(player1Move);
        toPlayer2.writeInt(player2Move);
    }
}
