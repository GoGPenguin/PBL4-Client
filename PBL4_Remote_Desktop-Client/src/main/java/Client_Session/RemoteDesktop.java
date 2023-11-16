package Client_Session;


import Sub_Server_Session.Sub_Server;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class RemoteDesktop  {
    @FXML
    private TextField localIp;

    @FXML
    private TextField localPwd;

    private Sub_Server sub_server = null;
    @FXML
    private TextField remoteIp;

    @FXML
    private PasswordField remotePwd;

    private Socket socketClient = null;

    private DataOutputStream out;

    private DataInputStream in;

    private String message ="";

    private int activated = 0;

    public void setSocketClient(Socket socketClient, DataOutputStream out, DataInputStream in, Sub_Server sub_server) {
        this.sub_server = sub_server;
        this.socketClient = socketClient;
        this.in = in;
        this.out = out;
        if (activated == 0) {
            Init();
            activated = 1;
        }
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

    private void Init() {


        // Tạo một luồng để nhận tin nhắn
        Thread receiverThread = new Thread(() -> {
            try {
                while (true) {
                    message = in.readUTF();
                    System.out.println(message);
                    //Class xử lý tin nhắn
                    MessageHandler msg = new MessageHandler(message);
                    String data = msg.getData();
                    if (!data.equals("True") && !data.equals("False") && !message.isEmpty()) {
                        data = data.equals(localPwd.getText()) ? "True" : "False";
                        message = msg.getReceiver() + "," + msg.getStatus() + "," + data;
                        out.writeUTF(message);
                        out.flush();
                    }
                    //True thì tạo luồng khác để remote
                    else if(data.equals("True")) {
                        Platform.runLater(() -> {
                            Stage remoteStage = new Stage();
                            FXMLLoader remoteLoader = new FXMLLoader(getClass().getResource("RemoteWindow.fxml"));
                            Pane remoteRoot = null;
                            try {
                                remoteRoot = remoteLoader.load();
                            } catch (IOException e) {
                                throw new RuntimeException();
                            }

                            Scene remoteScene = new Scene(remoteRoot);

                            Screen screen = Screen.getPrimary();
                            Rectangle2D bounds = screen.getVisualBounds();

                            double screenWidth = bounds.getWidth();
                            double screenHeight = bounds.getHeight();

                            remoteStage.setWidth(screenWidth);
                            remoteStage.setHeight(screenHeight);

                            remoteStage.setScene(remoteScene);

                            remoteStage.show();

                            RemoteWindow remoteController = remoteLoader.getController();
                            remoteController.getIp(remoteIp.getText(), remoteScene, remoteStage);
                            remoteController.start();
                        });
                        message = "";
                        out.writeUTF(message);
                        out.flush();
                    }
                    else {
                        message = "";
                        out.writeUTF(message);
                        out.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        receiverThread.start();
    }
    public void connectBtn(MouseEvent mouseEvent) {
        try {
            out.writeUTF(remoteIp.getText() + ",abc,"+ remotePwd.getText());
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
