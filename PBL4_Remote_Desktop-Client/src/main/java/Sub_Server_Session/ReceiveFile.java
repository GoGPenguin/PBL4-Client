package Sub_Server_Session;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ReceiveFile {
    private Socket socket;
    private DataInputStream inputStream;
    private FileOutputStream fileOutputStream;


    @FXML
    private TextArea taYourPartner;


    @FXML
    private Button btnFastDownload;


    public byte[] fileData = null;
    private List<FileData> receivedFiles = new ArrayList<>();
    @FXML
    private VBox vBoxDownload;
    @FXML
    private VBox vBoxSend;






    public ReceiveFile(Socket socket, TextArea taYourPartner, Button btnFastDownload,VBox vBoxDownload,VBox vBoxSend) {
        Thread receiverThread = new Thread(() -> {
            try {
                this.socket = socket;
                this.inputStream = new DataInputStream(socket.getInputStream());
                this.taYourPartner = taYourPartner;
                this.btnFastDownload = btnFastDownload;
                this.vBoxDownload = vBoxDownload;
                this.vBoxSend = vBoxSend;
                boolean isReceivingFile = false;


                while (true) {
                    try {
                        int fileNameLength = inputStream.readInt();
                        byte[] fileNameBytes = new byte[fileNameLength];
                        inputStream.readFully(fileNameBytes);
                        String fileName = new String(fileNameBytes, "UTF-8");
                        System.out.println(fileName);
                        if (!"endofname".equals(fileName)) {
                            Platform.runLater(() ->
                                    {
                                        addLabelReceive(fileName,vBoxDownload,receivedFiles);


                                    }
                            );
                        }
                        String mess = inputStream.readUTF();
                        if ("endofname".equals(mess)) {
                            isReceivingFile = true;
                            fileData = new byte[0];
                        }
                        if (isReceivingFile) {
                            long size = inputStream.readLong();
                            byte[] buffer = new byte[1024];
                            int bytesRead;


                            while (size > 0 && (bytesRead = inputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                                byte[] newFileData = new byte[fileData.length + bytesRead];
                                System.arraycopy(fileData, 0, newFileData, 0, fileData.length);
                                System.arraycopy(buffer, 0, newFileData, fileData.length, bytesRead);
                                fileData = newFileData;
                                size -= bytesRead;
                            }
                            System.out.println(fileData.length);
                            if (size == 0) {
                                FileData receivedFile = new FileData(fileName, fileData);
                                receivedFiles.add(receivedFile);
                                fileData = null;
                            }
                        }
                        String interrupt = inputStream.readUTF();
                        System.out.println(interrupt);
                        if("Connect is closed by partner".equals(interrupt))
                        {
                            clearViewTransfer();
                            showErrorAlert("Alert","Connect is closed by partner!");
                            closeResources();
                        }
                        btnFastDownload.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(!receivedFiles.isEmpty())
                                {
                                    DirectoryChooser directoryChooser = new DirectoryChooser();
                                    directoryChooser.setTitle("Chọn thư mục lưu trữ tệp");
                                    File selectedDirectory = directoryChooser.showDialog(null);


                                    if (selectedDirectory != null) {
                                        for (FileData receivedFile : receivedFiles) {
                                            String outputFileName = selectedDirectory.getAbsolutePath() + File.separator + receivedFile.getFileName();
                                            try (FileOutputStream fileOutputStream = new FileOutputStream(outputFileName)) {
                                                fileOutputStream.write(receivedFile.getData());
                                                System.out.println("File " + receivedFile.getFileName() + " received and saved successfully.");
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        });


                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        receiverThread.start();


    }
    public static void addLabelReceive(String fileName, VBox vBox, List<FileData> receivedFiles) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BASELINE_RIGHT);
        hBox.setPadding(new Insets(0, 5, 5, 10));
        Text text = new Text(fileName);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-color:rgb(239,242,255);" + "-fx-background-color: rgb(255,255,255);" + "-fx-border-color: black;" + " -fx-border-width: 1px;" + "-fx-border-radius: 5px;" + "-fx-cursor:pointer;");
        textFlow.setPadding(new Insets(2, 5, 2, 5));
        hBox.getChildren().add(textFlow);
        Platform.runLater(() -> {
            vBox.getChildren().add(hBox);
            hBox.setOnMouseClicked(event -> {
                String text2 = ((Text) textFlow.getChildren().get(0)).getText();
                System.out.println("Clicked: " + text2);
                for (FileData receivedFile : receivedFiles) {
                    if (receivedFile.getFileName().equals(text2)) {
                        try {
                            DirectoryChooser directoryChooser = new DirectoryChooser();
                            directoryChooser.setTitle("Chọn thư mục lưu trữ tệp");
                            File selectedDirectory = directoryChooser.showDialog(null);


                            if (selectedDirectory != null) {
                                String outputFileName = selectedDirectory.getAbsolutePath() + File.separator + receivedFile.getFileName();
                                try (FileOutputStream fileOutputStream = new FileOutputStream(outputFileName)) {
                                    fileOutputStream.write(receivedFile.getData());
                                    System.out.println("File " + receivedFile.getFileName() + " received and saved successfully.");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            });
        });
    }
    private void closeResources() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void clearViewTransfer() {
        Platform.runLater(() -> {
            vBoxSend.getChildren().clear();
            vBoxDownload.getChildren().clear();
        });
    }
    private void showErrorAlert(String title, String header) {
        Platform.runLater(() -> {
            Stage dialogStage = new Stage();
            dialogStage.initStyle(StageStyle.UTILITY);
            dialogStage.initModality(Modality.APPLICATION_MODAL);


            Label label = new Label(header);
            label.setWrapText(true);


            StackPane root = new StackPane();
            root.getChildren().add(label);


            Scene scene = new Scene(root, 300, 100);


            dialogStage.setTitle(title);
            dialogStage.setScene(scene);


            dialogStage.showAndWait();
        });
    }










}







