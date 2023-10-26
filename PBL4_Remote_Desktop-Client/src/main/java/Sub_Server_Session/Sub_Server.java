package Sub_Server_Session;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Sub_Server extends Thread{
    private Robot robot = null;

    private Socket clientSocket = null;
    private boolean initialized = false;

    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private ServerSocket serverSocket = null;
    private final int port = 6004;
    private double clientWidthCm = dim.getWidth();
    private double clientHeightCm = dim.getHeight();
    private int clientWidthPixels = 1920;
    private int clientHeightPixels = 1080;
    private double pixelsPerCmWidth = clientWidthPixels / clientWidthCm;
    private double pixelsPerCmHeight = clientHeightPixels / clientHeightCm;

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
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                out.writeUTF(String.valueOf(clientWidthCm));
                out.flush();
                out.writeUTF(String.valueOf(clientHeightCm));
                out.flush();
                new Sub_ClientHandler(clientSocket, robot, opt).start();
            }
        } catch (IOException | AWTException e) {
            e.printStackTrace();
        }
    }


}
