package webmonopoly.backend.boardmanagement;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MonopolyBoardServer {

    private final String gameCode;
    private final int portNumber;
    private Socket socket = null;
    private OutputStreamWriter out = null;
    private BufferedReader in = null;

    public MonopolyBoardServer(String gameCode, int portNumber, GameStarter starter) throws Exception {
        this.gameCode = gameCode;
        this.portNumber = portNumber;
        // Setup ServerSocket
        final ServerSocket serverSocket = new ServerSocket(portNumber);
        // Wait for device to contact (on different thread) and send game code
        Thread gameCodeThread = new Thread(() -> {
            try {
                socket = serverSocket.accept();
                out = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

                // Wait for board to say that game is ready to begin
                JSONObject beginJson = new JSONObject(in.readLine());
                if (beginJson.has("begin") && (Boolean) beginJson.get("begin")) {
                    starter.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        gameCodeThread.setDaemon(true);
        gameCodeThread.start();
    }

    public void sendJson(JSONObject jsonObject) throws IOException {
        boolean sent = false;
        for (int i = 0; i < 100 && !sent; i++) {
            try {
                out.write(jsonObject.toString());
                sent = true;
            } catch (IOException ignored) { }
        }
        throw new IOException("Failed to send JSON after 100 attempts");
    }
}
