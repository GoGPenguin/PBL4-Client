package com.example.pbl4_remote_desktopclient;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class RemoteWindow extends Thread{
    private Scene scene;
    @FXML
    private BorderPane borderPane;
    @FXML
    private ImageView imageView;
    private String ip;

    public void getIp(String ip, Scene scene) {
        this.ip = ip;
        this.scene = scene;
        System.out.println(ip);
    }
    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = new Socket(ip, 6004);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InputStream in = null;
        try {
            in = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new ReceivingScreen(in, imageView);
        new SendEvents(socket, borderPane, "1920", "1280", scene);
    }
}
