package Client_Session;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTransferFile {
    private ServerSocket server;
    private FileOutputStream fileOutputStream;
    private OutputStream outputStream;
    private Socket clientSocket;
    private InputStream inputStream;

    public ServerTransferFile(ServerSocket server)
    {
        this.server = server;
    }
    public void receiveFileFromClient(File file)
    {
        try {
            // Chấp nhận kết nối từ máy khách
            clientSocket = server.accept();
            System.out.println("Đã kết nối với máy khách.");

            // Mở luồng đầu vào để đọc dữ liệu từ máy khách
            inputStream = clientSocket.getInputStream();
            fileOutputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            System.out.println("Đã nhận xong tệp.");
        } catch ( IOException e) {
            e.printStackTrace();
        }

    }

}
