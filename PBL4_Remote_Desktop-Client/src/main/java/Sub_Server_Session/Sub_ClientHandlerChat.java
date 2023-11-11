package Sub_Server_Session;

import Client_Session.ChatViewController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Sub_ClientHandlerChat extends Thread {
    private Socket clientSocket;

    private ChatViewController chatViewController;
    private VBox vbox_message;

    private ServerSocket server;

    @FXML
    private Button button_send;

    @FXML
    private TextField tf_message;




    public Sub_ClientHandlerChat(ChatViewController chatViewController,VBox vbox_message,Button button_send,TextField tf_message) {
        this.chatViewController = chatViewController;
        this.vbox_message = vbox_message;
        this.button_send = button_send;
        this.tf_message = tf_message;
    }
    @Override
    public void run() {
        Chatting();
    }

    public void Chatting() {
        //Ghép chat vô đây
        try {
            server = new ServerSocket(9999);
            while(true)
            {
                clientSocket = server.accept();
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
//                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
                // Tạo một luồng để gửi tin nhắn
                Thread senderThread = new Thread(() -> {
                    while (true)
                    {
                        button_send.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                try{
                                    String message;
                                    message = tf_message.getText();
                                    out.writeUTF( message);
                                    chatViewController.addLabelSend(message,vbox_message);
                                    out.flush();
                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                });

                Thread receiverThread = new Thread(() -> {
                    try {
                        String message;
                        while (true) {
                            message = in.readUTF();
                            System.out.println(message);
                            this.chatViewController.addLabelReceive(message,vbox_message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });


                senderThread.start();
                receiverThread.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public void File_Transfer() {
        //Ghép truyền file vô đây
    }
}
