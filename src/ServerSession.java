/**
 * TODO
 *
 * @author Tyler Johnson (tjohson)
 * @version 1.0 Apr 11, 2024
 */

// Packages //
import java.io.DataOutputStream;

public class ServerSession implements Runnable {
    /**TODO*/
    private int gameNumber = 1;
    /**TODO*/
    private DataOutputStream toPlayer1;
    /**TODO*/
    private DataOutputStream toPlayer2;

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
        // Create server socket

        // Infinite loop to create new GameSession for every 2 players
            // Connect first player
                // Log first player information
                // Notify first player they are connected and 'Player 1'
            // Connect second player
                // Log second player information
                // Notify second player they are connected and 'Player 2'
            // Log that GameSession x is starting
            // Start new GameSession on new thread
        // CATCH IO //
        // Print log or stack trace
    }
}
