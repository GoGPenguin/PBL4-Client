package Client_Session;

import Sub_Server_Session.Sub_Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class StartWindow extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        String ip = "192.168.60.140";
        int port = 6003;
        int maxAttempts = 10; // Maximum number of connection attempts

        // test
        boolean connected = true;
        Socket client = null;
        DataInputStream in = null;
        DataOutputStream out = null;

        // Attempt to connect to the server with retries
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                client = new Socket(ip, port);
                System.out.println("Connected...");
                in = new DataInputStream(client.getInputStream());
                out = new DataOutputStream(client.getOutputStream());
                out.writeUTF(InetAddress.getLocalHost().getHostAddress());
                connected = true;
                break; // Connection successful, exit the loop
            } catch (IOException e) {
                System.out.println("Connection attempt " + attempt + " failed. Retrying...");
                try {
                    Thread.sleep(1000); // Wait for 1 second before retrying
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        if (connected) {
            FXMLLoader fxmlLoader = new FXMLLoader(StartWindow.class.getResource("StartWindow.fxml"));
            Pane pane = fxmlLoader.load();
            Sub_Server sub_server = new Sub_Server();
            StartWindowController controller = fxmlLoader.getController();
            controller.setClientSocket(client, in, out, sub_server);
            Scene scene = new Scene(pane);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Failed to connect to the server after " + maxAttempts + " attempts.");
        }
    }
}
