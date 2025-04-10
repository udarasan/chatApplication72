package org.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientController {

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtMessage;

    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String message="";

    public void initialize() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost",4000);
                dataInputStream = new DataInputStream(socket.getInputStream());
                while (true){
                    message=dataInputStream.readUTF();
                    txtArea.appendText("Server :"+message+"\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @FXML
    void sendOnAction(ActionEvent event) throws IOException {
        dataOutputStream=new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(txtMessage.getText());
        dataOutputStream.flush();

    }

}
