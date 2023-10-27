package Client_Session;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerChatPage {
   private ServerSocket serverSocket;
   private Socket socket;
   private BufferedWriter bufferedWriter;
   private BufferedReader bufferedReader;
   public ServerChatPage(ServerSocket serverSocket)
   {
       try{
           this.serverSocket = serverSocket;
           this.socket = serverSocket.accept();
           this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
       }
       catch (IOException e)
       {
           e.printStackTrace();
           closeEverything(socket,bufferedReader,bufferedWriter);
       }
   }
   public void sendMessageToClient(String messageToClient)
   {
       try{
           bufferedWriter.write(messageToClient);
           bufferedWriter.newLine();
           bufferedWriter.flush();
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
   public void receiveMessageFromClient(VBox vBox)
   {
       new Thread(new Runnable() {
           @Override
           public void run() {
               while(socket.isConnected())
               {
                   try {
                       String msg = bufferedReader.readLine();
//                       ChatViewController.addLabelForServer(msg,vBox);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }

               }
           }
       }).start();
   }
}
