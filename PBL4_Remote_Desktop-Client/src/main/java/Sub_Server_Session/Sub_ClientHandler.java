package Sub_Server_Session;


import Client_Session.ChatViewController;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.*;
import java.net.Socket;


public class Sub_ClientHandler extends Thread{
    private Socket clientSocket;
    private Robot robot;




    private String opt = "";




    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();




    private double clientWidthCm = dim.getWidth();
    private double clientHeightCm = dim.getHeight();




    public Sub_ClientHandler(Socket clientSocket, Robot robot, String opt) {
        this.opt = opt;
        this.clientSocket = clientSocket;
        this.robot = robot;

    }




    @Override
    public void run() {
        getControl(opt);
    }




    public void getControl(String control) {
        switch (control) {
            case "1":
                Remote_Desktop();
                break;
            case "2":
                Chatting();
                break;
            case "3":
                File_Transfer();
                break;
        }
    }








    public void Remote_Desktop() {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeUTF(String.valueOf(clientWidthCm));
            out.flush();
            out.writeUTF(String.valueOf(clientHeightCm));
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //waitToConnect();
        try {
            Thread thread = new Thread(() -> {
                new SendScreen(robot, clientSocket);
            });
            thread.start();
            new ReceivingEvents(clientSocket, robot);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void Chatting() {
        //Ghép chat vô đây

    }


    public void File_Transfer() {
        //Ghép truyền file vô đây
    }
}

