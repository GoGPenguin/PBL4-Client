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
            GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gDev = gEnv.getDefaultScreenDevice();
            this.robot = new Robot(gDev);
            this.serverSocket = new ServerSocket(port);
            Sub_ClientHandler remoteServer = null;
            while (true) {
                if (remoteServer == null || !remoteServer.isAlive()) {
                    Socket clientSocket = serverSocket.accept();
                    DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                    String opt = in.readUTF();
                    remoteServer = new Sub_ClientHandler(clientSocket, robot, opt);
                    remoteServer.start();
                }
            }
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
