package Client_Session;


import Sub_Server_Session.Sub_Server;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class StartWindowController implements Initializable {

    private Socket clientSocket;

    private DataInputStream in;

    private DataOutputStream out;

    private Sub_Server sub_server = null;

    private RemoteDesktop controller = new RemoteDesktop();

    private ChatViewController controllerChat = new ChatViewController();
    private TransferFileController controllerFile = new TransferFileController();

    private Node content;

    private FXMLLoader loader;

    public StackPane contentArea;

    public void  setClientSocket(Socket clientSocket, DataInputStream in, DataOutputStream out, Sub_Server sub_server) {
        this.sub_server = sub_server;
        this.clientSocket = clientSocket;
        this.in = in;
        this.out = out;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void exit(MouseEvent mouseEvent) {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void RemotePage(MouseEvent mouseEvent) {
        try {
            loader = new FXMLLoader(getClass().getResource("RemoteDesktop.fxml"));
            content = loader.load();
            contentArea.getChildren().setAll(content);
            controller = loader.getController();
            String pwd = randomNumber();
            controller.setValue(pwd);
            controller.setSocketClient(clientSocket, out, in, sub_server);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ChatPage(MouseEvent mouseEvent) {
        try {
            loader = new FXMLLoader(getClass().getResource("Chat.fxml"));
            content = loader.load();
            contentArea.getChildren().setAll(content);
            controllerChat = loader.getController();
            controllerChat.setValue();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleClickTransFile(MouseEvent event) {

        try {
            loader = new FXMLLoader(getClass().getResource("TransferFile.fxml"));
            content = loader.load();
            contentArea.getChildren().setAll(content);
            controllerFile = loader.getController();
            controllerFile.setValue();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String randomNumber() {
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000); // Generates a number between 100000 and 999999

        return String.valueOf(randomNumber);
    }


    public void HomePage(MouseEvent mouseEvent) {

        loader = new FXMLLoader(getClass().getResource("Homepage.fxml"));
        content = null;
        try {
            content = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        contentArea.getChildren().setAll(content);

    }
}
