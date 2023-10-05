package com.example.pbl4_remote_desktopclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Authentication {
    private String status = "RequestToConnect";
    private Socket socketClient;
    private String ip;
    private String pwd;
    private DataOutputStream authentication;
    private DataInputStream resCheck;

    private String verify = "";

    public Authentication(Socket socketClient, String ip, String pwd) {
        this.socketClient = socketClient;
        this.ip = ip;
        this.pwd = pwd;

        String combinedString = status + "," + ip + "," + pwd;
        try {
            authentication = new DataOutputStream(socketClient.getOutputStream());
            resCheck = new DataInputStream(socketClient.getInputStream());
            authentication.writeUTF(combinedString);
            authentication.flush();
            verify = resCheck.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean validation() {
        System.out.println(verify);
        if (verify.equals("True")) {
            System.out.println("Kết nối đi e");
            return true;
        }
        else {
            System.out.println("không có mà kết nối");
            return false;
        }
    }
}