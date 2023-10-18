package com.example.pbl4_remote_desktopclient;

import java.awt.*;
import java.net.Socket;

public class Sub_ClientHandler extends Thread{
    private Socket clientSocket;
    private Robot robot;
    private double pixelsPerCmWidth;
    private double pixelsPerCmHeight;

    private String opt = "";

    public Sub_ClientHandler(Socket clientSocket, Robot robot, double pixelsPerCmWidth, double pixelsPerCmHeight, String opt) {
        this.opt = opt;
        this.clientSocket = clientSocket;
        this.robot = robot;
        this.pixelsPerCmWidth = pixelsPerCmWidth;
        this.pixelsPerCmHeight = pixelsPerCmHeight;
    }

    @Override
    public void run() {
        getControl(opt);
    }

    public void getControl(String control) {
        switch (control) {
            case "1":
                Remote_Desktop();
                break;
            case "2":
                Chatting();
                break;
            case "3":
                File_Transfer();
                break;
        }
    }


    public void Remote_Desktop() {
        //waitToConnect();
        try {
            Thread thread = new Thread(() -> {
                new SendScreen(robot, clientSocket, pixelsPerCmWidth, pixelsPerCmHeight);
            });
            thread.start();
            new ReceiveEvents(clientSocket, robot);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Chatting() {
        //Ghép chat vô đây
    }

    public void File_Transfer() {
        //Ghép truyền file vô đây
    }
}
