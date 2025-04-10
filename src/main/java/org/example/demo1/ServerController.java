package org.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtMessage;

    @FXML
    private ImageView imageView;

    ServerSocket serverSocket;
    Socket socket;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    String message="";
    public void initialize() {
       new Thread(() -> {
           try {
               serverSocket = new ServerSocket(4000);
               txtArea.appendText("Server started\n");
               socket = serverSocket.accept();
               txtArea.appendText("Client connected\n");
               dataInputStream = new DataInputStream(socket.getInputStream());
               while (true) {
                   message=dataInputStream.readUTF();
                   if (message.equals("IMAGE")) {
                       int length=dataInputStream.readInt();
                       byte[] imageBytes=new byte[length];
                       dataInputStream.readFully(imageBytes);
                       ByteArrayInputStream bais=
                               new ByteArrayInputStream(imageBytes);
                       Image image=new Image(bais);
                       imageView.setImage(image);
                   }
                   txtArea.appendText("Client :"+message+"\n");
               }
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
       }).start();
    }

    @FXML
    void sendOnAction(ActionEvent event) throws IOException {
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(txtMessage.getText());
        dataOutputStream.flush();
    }

}
