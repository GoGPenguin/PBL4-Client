package com.example.pbl4_remote_desktopclient;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class RemoteDesktop  {
    @FXML
    private TextField localIp;

    @FXML
    private TextField localPwd;

    @FXML
    private TextField remoteIp;

    @FXML
    private PasswordField remotePwd;

    Socket socketClient = null;

    public void setSocketClient(Socket socketClient) {
        this.socketClient = socketClient;
    }

    public void setValue(String pwd)
    {
        try {
            // Lấy địa chỉ IP của máy
            String ipAddress = InetAddress.getLocalHost().getHostAddress();

            // Đặt giá trị của tfYourID bằng địa chỉ IP
            localIp.setText(ipAddress);

            //Password default
            localPwd.setText(pwd);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }




    public void connectBtn(MouseEvent mouseEvent) {
        String ip = "", pwd = "";
        try {
            ip = remoteIp.getText();
            pwd = remotePwd.getText();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (!ip.equals("") && !pwd.equals("")) {
            Authentication auth = new Authentication(socketClient, ip, pwd);
            auth.validation();
        }
    }
}

