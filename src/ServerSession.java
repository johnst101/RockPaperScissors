/**
 * Started as a new thread from the RPSServer class to run the
 * server session and manage the client connections and start
 * the game.
 *
 * @author Tyler Johnson (tjohson)
 * @version 1.0 Apr 13, 2024
 */

// Packages //
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;

public class ServerSession implements Runnable, RPSConstants {
    /**Which session or game is being connected/played.*/
    private int gameNumber = 1;
    /**The server log visualization element*/
    private TextArea log;
    /**Connection to Player 1's client to send connection messages.*/
    private DataOutputStream toPlayer1;
    /**Connection to Player 2's client to send connection messages.*/
    private DataOutputStream toPlayer2;

    public ServerSession(TextArea log) {
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
        try {
            // Create server socket
            ServerSocket serverSocket = new ServerSocket(8000);
            Platform.runLater(() -> log.appendText(new Date() + ": Server started at port 8000\n"));

            // Infinite loop to create new GameSession for every 2 players
            while (true) {
                Platform.runLater(() -> log.appendText(new Date() + ": Waiting for players to join session " + gameNumber + "\n"));
                // Connect first player
                Socket player1 = serverSocket.accept();
                toPlayer1 = new DataOutputStream(player1.getOutputStream());
                // Log first player information
                Platform.runLater(() -> {
                    log.appendText(new Date() + ": Player 1 has joined game " + gameNumber + "\n");
                    log.appendText("Player 1's IP address is " + player1.getInetAddress().getHostAddress() + "\n");
                });
                // Notify first player they are connected and 'Player 1'
                toPlayer1.writeInt(PLAYER1);

                // Connect second player
                Socket player2 = serverSocket.accept();
                toPlayer2 = new DataOutputStream(player2.getOutputStream());
                // Log second player information
                Platform.runLater(() -> {
                    log.appendText(new Date() + ": Player 2 has joined game " + gameNumber + "\n");
                    log.appendText("Player 2's IP address is " + player2.getInetAddress().getHostAddress() + "\n");
                });
                // Notify second player they are connected and 'Player 2'
                toPlayer2.writeInt(PLAYER2);

                // Log that GameSession x is starting
                Platform.runLater(() -> log.appendText(new Date() + ": Starting thread for game " + gameNumber + "\n"));
                gameNumber++;
                // Start new GameSession on new thread
                new Thread(new ServerGameSession(toPlayer1, toPlayer2, player1, player2, log)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.appendText(new Date() + ": \n" + "\t" + Arrays.toString(e.getStackTrace()));
        }
    }
}
