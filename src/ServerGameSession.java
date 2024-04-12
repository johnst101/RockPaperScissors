/**
 * TODO
 *
 * @author Tyler Johnson (tjohson)
 * @version 1.0 Apr 11, 2024
 */

// Packages //
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerGameSession implements Runnable {
    /**TODO*/
    private Socket player1;
    /**TODO*/
    private Socket player2;
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
    private int player1Wins;
    private int player2Wins;
    /**TODO*/
    private enum Move {
        ROCK,
        PAPER,
        SCISSORS
    }

    /**
     * TODO
     */
    public ServerGameSession(int gameNumber, DataOutputStream toPlayer1, DataOutputStream toPlayer2) {

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
        // Infinite loop until win or draw state reached
            // Notify players to choose move
            // Receive move from player1
                // Notify waiting for player2 to make move???
            // Receive move from player2
                // Notify waiting for player1 to make move???
            // Check for round win or draw
                // If player1 move > player2 move
                    // player1 round win
                    // notify players of results
                        // Send moves to each player and updated score
                // Else if player2 move > player1 move
                    // player2 round win
                    // notify players of results
                // Else
                    // round draw
                    // notify players of results
                    // continue
            // Check for full game win
                // If player1Wins equals 5 AND player2Wins less than 5
                    // Notify players that player1 won the game
                // If player2Wins equals 5 AND player1Wins less than 5
                    // Notify players that player2 won the game

        // CATCH //
    }

    /**
     * TODO
     *
     * @param player1Move
     * @param player2Move
     * @return
     */
    private int compareMoves(Move player1Move, Move player2Move) {
        // If player1Move equals Rock
            // If player2Move equals Rock
                // Then return 3
            // If player2Move equals Paper
                // Then return 2
            // If player2Move equals Scissors
                // Then return 1
        // If player1Move equals Paper
            // If player2Move equals Rock
                // Then return 1
            // If player2Move equals Paper
                // Then return 3
            // If player2Move equals Scissors
                // Then return 2
        // If player1Move equals Scissors
            // If player2Move equals Rock
                // Then return 2
            // If player2Move equals Paper
                // Then return 1
            // If player2Move equals Scissors
                // Then return 3
    }

    /**
     * TODO
     */
    private void sendResults() throws IOException {

    }
}
