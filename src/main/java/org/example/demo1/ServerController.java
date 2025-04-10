package org.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtMessage;

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
