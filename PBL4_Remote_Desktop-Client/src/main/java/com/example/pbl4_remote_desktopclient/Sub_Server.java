package com.example.pbl4_remote_desktopclient;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Sub_Server extends Thread{
    private Robot robot = null;
    private final int port = 6004;
    private double clientWidthCm = 34.0;
    private double clientHeightCm = 19.0;
    private int clientWidthPixels = 1920;
    private int clientHeightPixels = 1080;
    private double pixelsPerCmWidth = clientWidthPixels / clientWidthCm;
    private double pixelsPerCmHeight = clientHeightPixels / clientHeightCm;

    public Sub_Server () {
        System.out.println("Server chay roi ne");
        start();
    }

    @Override
    public void run() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            Thread thread = new Thread(() -> {
                new SendScreen(robot, clientSocket, pixelsPerCmWidth, pixelsPerCmHeight);
            });
            thread.start();
            new ReceiveEvents(clientSocket, robot);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
