package Client_Session;
import Sub_Server_Session.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.*;
import java.util.ResourceBundle;

public class TransferFileController implements Initializable {
    @FXML
    private Button btnFastDownload;



    @FXML
    private Button btnOpenFile;

    @FXML
    private Button btnOpenFolder;



    @FXML
    private Label labelPartnerFile;

    @FXML
    private Label labelYourFile;

    @FXML
    private Label labelYourID;



    @FXML
    private TextArea taYourFile;

    @FXML
    private TextArea taYourPartner;

    @FXML
    private TextField tfYourID;


    @FXML
    private TextField tfPartnerID;



    @FXML
    private VBox vBoxDownload;

    @FXML
    private VBox vBoxSend;


    private ServerSocket serverSocket;
    private Socket socket;
    private OutputStream outputStream;
    private DataInputStream inputStream;

    public void setValue()
    {
        try {

            String ipAddress = InetAddress.getLocalHost().getHostAddress();

            tfYourID.setText(ipAddress);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void handleClickConnect(MouseEvent event) {
        String partnerID = tfPartnerID.getText();
        if(partnerID != null)
        {
            try {
                socket = new Socket(partnerID,9090);
                outputStream = new DataOutputStream(socket.getOutputStream());
                Thread senderThread = new Thread(() -> {
                    btnOpenFile.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            new SendFile(socket,btnOpenFile,taYourFile,vBoxSend);
                        }
                    });
                });


                Thread receiverThread = new Thread(() -> {
                    new ReceiveFile(socket,taYourPartner,btnFastDownload,vBoxDownload);
                });


                receiverThread.start();
                senderThread.start();


                Thread sendFolder = new Thread(() ->{
                    btnOpenFolder.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            new sendFolder(socket,btnOpenFolder,taYourFile,vBoxSend);
                        }
                    });
                });

                sendFolder.start();



            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       new Sub_ClientHandlerFile(this,taYourPartner,btnFastDownload,btnOpenFile,taYourFile,btnOpenFolder,vBoxDownload,vBoxSend).start();
    }
}

