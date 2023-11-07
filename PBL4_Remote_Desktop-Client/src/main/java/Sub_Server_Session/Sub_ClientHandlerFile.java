package Sub_Server_Session;


import Client_Session.TransferFileController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Sub_ClientHandlerFile extends Thread {
    private Socket clientSocket;

    private TransferFileController transferFileController;


    private ServerSocket server;

    @FXML
    private TextArea taYourPartner;

    @FXML
    private Button btnFastDownload;

    @FXML
    private Button btnOpenFile;

    @FXML
    private TextArea taYourFile;


    @FXML
    private Button btnOpenFolder;

    @FXML
    private VBox vBoxDownload;

    @FXML
    private VBox vBoxSend;

    public Sub_ClientHandlerFile(TransferFileController transferFileController, TextArea taYourPartner,Button btnFastDownload,Button btnOpenFile,TextArea taYourFile,Button btnOpenFolder,VBox vBoxDownload,VBox vBoxSend) {
        this.transferFileController = transferFileController;
        this.taYourPartner = taYourPartner;
        this.btnFastDownload = btnFastDownload;
        this.btnOpenFile = btnOpenFile;
        this.taYourFile = taYourFile;

        this.btnOpenFolder = btnOpenFolder;
        this.vBoxDownload = vBoxDownload;
        this.vBoxSend = vBoxSend;}
    @Override
    public void run() {
        File_Transfer();
    }



    public void File_Transfer() {

        //Ghép truyền file vô đây
        try {
            server = new ServerSocket(9090);
            System.out.println("Tao server thành công!");
            while(true)
            {
                clientSocket = server.accept();
                System.out.println("Cho truyen file rồi");
                OutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());

                // Tạo một luồng để gửi tin nhắn
                Thread senderThread = new Thread(() -> {
                    btnOpenFile.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            new SendFile(clientSocket,btnOpenFile,taYourFile,vBoxSend);
                        }
                    });

                });

                Thread receiverThread = new Thread(() -> {
                        new ReceiveFile(clientSocket,taYourPartner,btnFastDownload,vBoxDownload);

                });

                senderThread.start();
                receiverThread.start();


                Thread sendFolder = new Thread(() ->{
                    btnOpenFolder.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            new sendFolder(clientSocket,btnOpenFolder,taYourFile,vBoxSend);
                        }
                    });
                });

                sendFolder.start();

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
