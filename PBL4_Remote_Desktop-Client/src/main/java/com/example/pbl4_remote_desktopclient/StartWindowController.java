package com.example.pbl4_remote_desktopclient;


import javafx.application.Platform;
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

    public void  setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    public StackPane contentArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void RemotePage(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RemoteDesktop.fxml"));
            Node content = loader.load();
            contentArea.getChildren().setAll(content);
            RemoteDesktop controller = loader.getController();
            String pwd = randomNumber();
            controller.setValue(pwd);
            controller.setSocketClient(clientSocket);

            // Create a background thread to handle data communication
            Thread dataThread = new Thread(() -> {
                try {
                    // Create a DataInputStream to receive data from the client
                    DataInputStream clientInput = new DataInputStream(clientSocket.getInputStream());

                    // Create a DataOutputStream to send data to the server
                    DataOutputStream serverOutput = new DataOutputStream(clientSocket.getOutputStream());

                    while (true) {
                        // Read data from the client
                        String data = clientInput.readUTF();

                        // Check if the received data matches the generated pwd
                        if (data.equals(pwd)) {
                            System.out.println("Đúng pass");
                            // Data matches pwd; you can take action accordingly
                            // For example, you can send a response to the server
                            String response = "True";
                            serverOutput.writeUTF(response);
                            serverOutput.flush(); // Flush the output stream to ensure the message is sent immediately
                        } else {
                            System.out.println("sai pass");
                            serverOutput.writeUTF("False");
                            serverOutput.flush();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Start the background thread
            dataThread.setDaemon(true); // Set the thread as a daemon to allow it to exit when the application exits
            dataThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ChatPage(MouseEvent mouseEvent) {
        loadContent("Chat.fxml");
    }
    public void handleClickTransFile(MouseEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TransferFile.fxml"));
            Node content = loader.load();
            contentArea.getChildren().setAll(content);
            TransferFileController controller = loader.getController();
            controller.setValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadContent(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Node content = loader.load();
            contentArea.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String randomNumber() {
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000); // Generates a number between 100000 and 999999

        return String.valueOf(randomNumber);
    }


}
