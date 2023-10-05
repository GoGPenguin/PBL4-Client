package com.example.pbl4_remote_desktopclient;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.Socket;

public class StartWindow extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        String ip = "192.168.1.14";
        int port = 6003;
        int maxAttempts = 10; // Maximum number of connection attempts

        boolean connected = false;
        Socket client = null;

        // Attempt to connect to the server with retries
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                client = new Socket(ip, port);
                System.out.println("Connected...");
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

            StartWindowController controller = fxmlLoader.getController();
            controller.setClientSocket(client);

            Scene scene = new Scene(pane);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Failed to connect to the server after " + maxAttempts + " attempts.");
        }
    }
}
