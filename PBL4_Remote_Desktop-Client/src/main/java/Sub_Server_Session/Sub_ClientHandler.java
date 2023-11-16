package Sub_Server_Session;


import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
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

        try {
            Thread thread = new Thread(() -> {
                new SendScreen(robot, clientSocket);
            });
            thread.start();
            new ReceivingEvents(clientSocket, robot);
        }
        catch (Exception e) {
            try {
                System.out.println("Đóng kết nối");
                clientSocket.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        }
    }




    public void Chatting() {
        //Ghép chat vô đây
//        System.out.println("Cho chat rồi");
//        try {
//            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
//            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
//            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
//            // Tạo một luồng để gửi tin nhắn
//            Thread senderThread = new Thread(() -> {
//                try {
//                    String message;
//                    while (true) {
//                        message = consoleReader.readLine();
//                        out.writeUTF("192.168.1.69" + " "  + message);
//                        ChatViewController.addLabelSend(message,ChatViewController.getVbox_messages());
//                        out.flush();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//
//
//            Thread receiverThread = new Thread(() -> {
//                try {
//                    String message;
//                    while (true) {
//                        message = in.readUTF();
//                        System.out.println(message);
//                        ChatViewController.addLabelReceive(message,ChatViewController.getVbox_messages());
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//
//
//
//
//            senderThread.start();
//            receiverThread.start();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }




    public void File_Transfer() {
        //Ghép truyền file vô đây
    }
}



