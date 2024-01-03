package Sub_Server_Session;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class SendFile {
    private Socket socket;
    private DataOutputStream outputStream;

    @FXML
    private Button btnOpenFile;

    @FXML
    private TextArea taYourFile;

    @FXML
    private VBox vBoxSend;

    public SendFile(Socket socket, Button btnOpenFile, TextArea taYourFile,VBox vBoxSend) {

            try {
                this.socket = socket;
                this.outputStream = new DataOutputStream(socket.getOutputStream());
                this.btnOpenFile = btnOpenFile;
                this.taYourFile = taYourFile;
                this.vBoxSend = vBoxSend;

                FileChooser fileChooser = new FileChooser();
                File selectedFile = fileChooser.showOpenDialog(btnOpenFile.getScene().getWindow());

                if (selectedFile != null) {
                    FileInputStream fileInputStream = new FileInputStream(selectedFile);
                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    String nameFile = selectedFile.getName();
                    byte[] nameBytes = nameFile.getBytes("UTF-8");

                    outputStream.writeInt(nameBytes.length);
                    outputStream.write(nameBytes);
                    outputStream.writeUTF("endofname");
                    outputStream.writeLong(selectedFile.length());

                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                        outputStream.flush();
                    }


                    System.out.println("Send file successfully");
                    fileInputStream.close();

                    Platform.runLater(() -> {
                        addLabelSend(selectedFile.getName(),vBoxSend);});
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void addLabelSend(String fileName, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BASELINE_LEFT);
        hBox.setPadding(new Insets(0, 5, 5, 10));
        Text text = new Text(fileName);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-color:rgb(239,242,255);" + "-fx-background-color: rgb(255,255,255);" + "-fx-border-color: black;" + " -fx-border-width: 1px;" + "-fx-border-radius: 5px;" + "-fx-cursor:pointer;");
        textFlow.setPadding(new Insets(2, 5, 2, 5));
        hBox.getChildren().add(textFlow);
        Platform.runLater(() -> {
            vBox.getChildren().add(hBox);

        });
    }
}
