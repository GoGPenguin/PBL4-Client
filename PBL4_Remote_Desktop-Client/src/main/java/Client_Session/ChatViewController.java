

package Client_Session;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
public class ChatViewController implements Initializable {
    @FXML
    private Button btnConnect;
    @FXML
    private Button button_send;
    @FXML
    private Label labelPartnerID;

    @FXML
    private Label labelYourID;

    @FXML
    private ScrollPane sp_main;
    @FXML
    private TextField tfPartnerID;

    @FXML
    private TextField tfYourID;


    @FXML
    private TextField tf_message;
    @FXML
    private VBox vbox_messages;
    private ClientChatPage client;
    private ServerChatPage server;
    public String Role = "client";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
//            try{
////                tao server để client có thể chat
//                server = new ServerChatPage(new ServerSocket(8082));
//                System.out.println("Đã tạo server thành công");
                if("server".equals(this.Role))
                {
                    vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                            sp_main.setVvalue((Double) t1);
                        }
                    });

                    server.receiveMessageFromClient(vbox_messages);

                    button_send.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            String msg = tf_message.getText();
                            if(!msg.isEmpty())
                            {

                                //Test
                                LocalDateTime currentDateTime = LocalDateTime.now();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // Định dạng ngày và giờ
                                String formattedDateTime = currentDateTime.format(formatter);
                                HBox hBox2 = new HBox();
                                hBox2.setAlignment(Pos.CENTER_RIGHT);
                                hBox2.setPadding(new Insets(-5,5,-5,10));
                                Text text2 = new Text("192.168.1.3 ( " + formattedDateTime + ")");
                                TextFlow textFlow2 = new TextFlow(text2);
                                textFlow2.setPadding(new Insets(5,5,5,5));
                                hBox2.getChildren().add(textFlow2);
                                //


                                HBox hBox = new HBox();
                                hBox.setAlignment(Pos.CENTER_RIGHT);
                                hBox.setPadding(new Insets(0,5,5,10));
                                Text text = new Text(msg);
                                TextFlow textFlow = new TextFlow(text);
                                textFlow.setStyle("-fx-color:rgb(239,242,255);" + "-fx-background-color: rgb(15,125,242);" + "-fx-background-radius: 10px;");
                                textFlow.setPadding(new Insets(5,10,5,10));
                                text.setFill(Color.color(0.934,0.945,0.996));
                                hBox.getChildren().add(textFlow);
                                vbox_messages.getChildren().add(hBox2);
                                vbox_messages.getChildren().add(hBox);
                                server.sendMessageToClient(msg);
                                System.out.println("gửi message đến client thành công");
                                tf_message.clear();
                            }
                        }
                    });
                }
                else if("client".equals(this.Role)){
                    vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                            sp_main.setVvalue((Double) t1);
                        }
                    });

                    button_send.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            String messageToSend = tf_message.getText();
                            if(!messageToSend.isEmpty() && client != null)
                            {
                                String yourID = tfYourID.getText();
                                //Test
                                LocalDateTime currentDateTime = LocalDateTime.now();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // Định dạng ngày và giờ
                                String formattedDateTime = currentDateTime.format(formatter);
                                HBox hBox2 = new HBox();
                                hBox2.setAlignment(Pos.CENTER_RIGHT);
                                hBox2.setPadding(new Insets(-5,5,-5,10));
                                Text text2 = new Text( "(" + formattedDateTime + ")");
                                TextFlow textFlow2 = new TextFlow(text2);
                                textFlow2.setPadding(new Insets(5,5,5,5));
                                hBox2.getChildren().add(textFlow2);
                                //


                                HBox hBox = new HBox();
                                hBox.setAlignment(Pos.CENTER_RIGHT);
                                hBox.setPadding(new Insets(0,5,5,10));
                                Text text = new Text(messageToSend);
                                TextFlow textFlow = new TextFlow(text);
                                textFlow.setStyle("-fx-color:rgb(239,242,255);" + "-fx-background-color: rgb(15,125,242);" + "-fx-background-radius: 10px;");
                                textFlow.setPadding(new Insets(5,10,5,10));
                                text.setFill(Color.color(0.934,0.945,0.996));
                                hBox.getChildren().add(textFlow);
                                vbox_messages.getChildren().add(hBox2);
                                vbox_messages.getChildren().add(hBox);
                                client.sendMessageToServer(messageToSend);
                                tf_message.clear();

                            }
                        }
                    });
                }

//            }
//            catch(IOException e)
//            {
//                e.printStackTrace();
//            }
        }).start();


    }
    public static void addLabelForClient(String msgFromServer,VBox vBox)
    {
        //Test
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // Định dạng ngày và giờ
        String formattedDateTime = currentDateTime.format(formatter);
        HBox hBox2 = new HBox();
        hBox2.setAlignment(Pos.CENTER_LEFT);
        hBox2.setPadding(new Insets(-5,5,-5,10));
        Text text2 = new Text("(" + formattedDateTime + ")");
        TextFlow textFlow2 = new TextFlow(text2);
        textFlow2.setPadding(new Insets(5,5,5,5));
        hBox2.getChildren().add(textFlow2);
        //
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(0,5,5,10));
        Text text = new Text(msgFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-color:rgb(239,242,255);" + "-fx-background-color: rgb(15,125,242);" + "-fx-background-radius: 10px;");
        textFlow.setPadding(new Insets(5,10,5,10));
        hBox.getChildren().add(textFlow);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox2);
                vBox.getChildren().add(hBox);

            }
        });
    }
    public static void addLabelForServer(String messageFromClient,VBox vbox)
    {
        //Test
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // Định dạng ngày và giờ
        String formattedDateTime = currentDateTime.format(formatter);
        HBox hBox2 = new HBox();
        hBox2.setAlignment(Pos.CENTER_LEFT);
        hBox2.setPadding(new Insets(-5,5,-5,10));
        Text text2 = new Text("( " + formattedDateTime + ")");
        TextFlow textFlow2 = new TextFlow(text2);
        textFlow2.setPadding(new Insets(5,5,5,5));
        hBox2.getChildren().add(textFlow2);
        //
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(0,5,5,10));
        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-color:rgb(239,242,255);" + "-fx-background-color: rgb(15,125,242);" + "-fx-background-radius: 10px;");
        textFlow.setPadding(new Insets(5,10,5,10));
        hBox.getChildren().add(textFlow);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().add(hBox2);
                vbox.getChildren().add(hBox);
            }
        });
    }
    public void setValue()
    {
        try {
            // Lấy địa chỉ IP của máy
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            // Đặt giá trị của tfYourID bằng địa chỉ IP
            tfYourID.setText(ipAddress);
            this.Role = "client";
            //Password default


        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void onClickConnect(MouseEvent event) {
        String partnerID = tfPartnerID.getText();

        if(partnerID != null)
        {
            try {
                this.Role = "client";
               //verify chỗ ni
                Boolean validate = true;
                if(validate)
                {
                    client = new ClientChatPage(new Socket(partnerID,8082));
                    client.receiveMessageFromServer(vbox_messages);
                    System.out.println("Connected to Server");


                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }


}