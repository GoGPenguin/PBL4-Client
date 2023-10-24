package Client_Session;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TransferFileController {
    @FXML
    private Button btnFastDownload;

    @FXML
    private Button btnMediumDownload;

    @FXML
    private Button btnOpenFile;

    @FXML
    private Button btnRemoveFile;

    @FXML
    private Button btnSlowDownload;

    @FXML
    private Label labelPartnerFile;

    @FXML
    private Label labelYourFile;

    @FXML
    private Label labelYourID;

    @FXML
    private Label labelYourPassword;

    @FXML
    private TextArea taYourFile;

    @FXML
    private TextArea taYourPartner;

    @FXML
    private TextField tfYourID;

    @FXML
    private TextField tfYourPassword;
    @FXML
    private TextField tfPartnerID;

    @FXML
    private TextField tfPartnerPassword;
    public void setValue()
    {
        try {
            // Lấy địa chỉ IP của máy
            String ipAddress = InetAddress.getLocalHost().getHostAddress();

            // Đặt giá trị của tfYourID bằng địa chỉ IP
            tfYourID.setText(ipAddress);

            //Password default
            tfYourPassword.setText("123456");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void handleClickConnect(MouseEvent event) {
        String partnerID = tfPartnerID.getText();
        String partnerPassword = tfPartnerPassword.getText();
        if(partnerID != null && partnerPassword != null)
        {
            try {
                    Socket socketClient = new Socket(partnerID, Integer.parseInt(partnerPassword));
                DataOutputStream os = new DataOutputStream(socketClient.getOutputStream());
                DataInputStream is = new DataInputStream(socketClient.getInputStream());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }
}

