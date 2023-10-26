package com.example.pbl4_remote_desktopclient;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;

public class ClientChatPage {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public ClientChatPage(Socket socket)
    {
      try{
          this.socket = socket;
          this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

      }
      catch (IOException e)
      {
          e.printStackTrace();
          closeEverything(socket,bufferedReader,bufferedWriter);
      }
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
        try{
            bufferedReader.close();
            bufferedWriter.close();
            socket.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public void sendMessageToServer(String messageToServer)
    {
        try{
            bufferedWriter.write(messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }
    public void receiveMessageFromServer(VBox vBox)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected())
                {
                    try {
                        String msg = bufferedReader.readLine();
                        ChatViewController.addLabelForClient(msg,vBox);
                    } catch (IOException e) {
                        e.printStackTrace();
                        closeEverything(socket,bufferedReader,bufferedWriter);
                    }

                }
            }
        }).start();
    }
}
