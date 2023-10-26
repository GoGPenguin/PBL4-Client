package Client_Session;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.*;
import java.util.ResourceBundle;

public class TransferFileController implements Initializable {
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
    private ClientTransferFile clientTransferFile;
    private  ServerTransferFile server;
    private ServerSocket serverSocket = null;
    private Socket client = null;
    private String Role;
    private InputStream inputStream;
    private FileOutputStream fileOutputStream = null;
    public void setValue()
    {
        try {
            // Lấy địa chỉ IP của máy
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            // Đặt giá trị của tfYourID bằng địa chỉ IP
            tfYourID.setText(ipAddress);
            this.Role = "server";
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
                this.Role = "client";
                //verify chỗ ni
                Boolean validate = true;
                if(validate)
                {
                    clientTransferFile = new ClientTransferFile(new Socket(partnerID,9090));
                    System.out.println("Connected to Server");
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }
    @FXML
    void handleClickOpenFile(MouseEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(btnOpenFile.getScene().getWindow());
        if (selectedFile != null) {
            // Đã chọn tệp, thực hiện các thao tác bạn muốn ở đây
            clientTransferFile.sendFileToServer(selectedFile);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    taYourFile.appendText(selectedFile.getName());
                    System.out.println(selectedFile.getAbsolutePath()); // Hiển thị đường dẫn của tệp đã chọn trong TextArea
                }
            });

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            try {

//                serverSocket = new ServerSocket(9090);
                System.out.println("Waiting Client Connect.");
                if(this.Role == "server")
                {
                    client = serverSocket.accept(); // Accept a connection from a client
                    System.out.println("Connected to the client.");

                    // Create input and output streams for file transfer
                    if(client != null)
                    {
                        inputStream = client.getInputStream();
                        // Name of the file to be stored on the server


                        //Read File Name
                        byte[] fileNameBytes = new byte[1024];  // Adjust the buffer size as needed
                        int fileNameLength = inputStream.read(fileNameBytes);
                        if (fileNameLength == -1) {
                            // Handle the case where no file name is sent or an error occurred
                            return;
                        }

                        String fileName = new String(fileNameBytes, 0, fileNameLength, "UTF-8");
                        System.out.println(fileName);


                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                taYourPartner.appendText(fileName);
                            }
                        });

                        btnFastDownload.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                DirectoryChooser directoryChooser = new DirectoryChooser();
                                directoryChooser.setTitle("Chọn thư mục lưu trữ tệp");

                                // Hiển thị hộp thoại và lấy đường dẫn đến thư mục từ người dùng
                                File selectedDirectory = directoryChooser.showDialog(null);  // null: use default owner window

                                if (selectedDirectory != null) {
                                    try {
                                        String outputFileName = selectedDirectory.getAbsolutePath() + File.separator + fileName;
                                        fileOutputStream = new FileOutputStream(outputFileName);
                                        byte[] buffer = new byte[1024];
                                        int bytesRead;
                                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                                            fileOutputStream.write(buffer, 0, bytesRead);
                                        }

                                        // Đảm bảo rằng việc ghi dữ liệu vào tệp đã hoàn thành trước khi đóng
                                        fileOutputStream.close();
                                        System.out.println("File received and saved successfully.");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        btnOpenFile.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                try{
                                    FileChooser fileChooser = new FileChooser();
                                    File selectedFile = fileChooser.showOpenDialog(btnOpenFile.getScene().getWindow());
                                    if (selectedFile != null) {
//                                        clientTransferFile.sendFileToServer(selectedFile);
                                        FileInputStream fileInputStream = new FileInputStream(selectedFile);
                                        byte[] buffer = new byte[1024];
                                        int bytesRead;
                                        String nameFile = selectedFile.getName();
                                        byte[] nameBytes = nameFile.getBytes("UTF-8");  // Convert the file name to bytes using UTF-8 encoding
                                        OutputStream outputStream = new DataOutputStream(client.getOutputStream());
                                        outputStream.write(nameBytes);

                                        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                                            outputStream.write(buffer, 0, bytesRead);
                                        }
                                        System.out.println("Send file successfully");
                                        outputStream.close();
                                        fileInputStream.close();
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                taYourFile.appendText(selectedFile.getName());
                                                System.out.println(selectedFile.getAbsolutePath());
                                            }
                                        });

                                    }
                                }catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });



//                // Close streams and the client socket (serverSocket should not be closed here)
//                fileOutputStream.close();
//                inputStream.close();
//                client.close();
                    }
                }
                else if(this.Role == "client")
                {
                    btnOpenFile.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                          try{
                              FileChooser fileChooser = new FileChooser();
                              File selectedFile = fileChooser.showOpenDialog(btnOpenFile.getScene().getWindow());
                              if (selectedFile != null) {
                                  // Đã chọn tệp, thực hiện các thao tác bạn muốn ở đây
                                  clientTransferFile.sendFileToServer(selectedFile);
                                  Platform.runLater(new Runnable() {
                                      @Override
                                      public void run() {
                                          taYourFile.appendText(selectedFile.getName());
                                          System.out.println(selectedFile.getAbsolutePath()); // Hiển thị đường dẫn của tệp đã chọn trong TextArea
                                      }
                                  });

                              }
                          }catch (IOException e)
                          {
                              e.printStackTrace();
                          }
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

