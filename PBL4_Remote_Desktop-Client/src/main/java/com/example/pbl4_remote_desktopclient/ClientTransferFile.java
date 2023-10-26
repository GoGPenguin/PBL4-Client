package com.example.pbl4_remote_desktopclient;

import java.io.*;
import java.net.Socket;

public class ClientTransferFile {
    private Socket client;
    private FileInputStream fileInputStream;
    private OutputStream outputStream;

    public ClientTransferFile(Socket client) throws IOException {
        this.client = client;
        outputStream = new DataOutputStream(client.getOutputStream());
    }

    public void sendFileToServer(File fileToSend) throws IOException {
        fileInputStream = new FileInputStream(fileToSend);
        byte[] buffer = new byte[1024];
        int bytesRead;
        String nameFile = fileToSend.getName();
        byte[] nameBytes = nameFile.getBytes("UTF-8");  // Convert the file name to bytes using UTF-8 encoding
        outputStream.write(nameBytes);

        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        System.out.println("Send file successfully");
        outputStream.close();
        fileInputStream.close();
    }

}
