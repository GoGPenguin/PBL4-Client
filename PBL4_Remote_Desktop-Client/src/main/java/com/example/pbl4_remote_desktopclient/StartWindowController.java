package com.example.pbl4_remote_desktopclient;


import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartWindowController implements Initializable {


    public StackPane contentArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void RemotePage(MouseEvent mouseEvent) {
        loadContent("RemoteDesktop.fxml");
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


}
