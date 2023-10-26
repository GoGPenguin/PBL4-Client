package Sub_Server_Session;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Sub_Server extends Thread{
    private Robot robot = null;


    private ServerSocket serverSocket = null;
    private final int port = 6004;



    public Sub_Server () {
        System.out.println("Server chay roi ne");
        start();
    }

    @Override
    public void run() {
        try {
            this.robot = new Robot();
            this.serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                String opt = in.readUTF();
                new Sub_ClientHandler(clientSocket, robot, opt).start();
            }
        } catch (IOException | AWTException e) {
            e.printStackTrace();
        }
    }


}
