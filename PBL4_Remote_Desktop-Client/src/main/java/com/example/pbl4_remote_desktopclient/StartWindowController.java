package com.example.pbl4_remote_desktopclient;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;


import java.awt.*;

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

    private void loadContent(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Node content = loader.load();
            contentArea.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
