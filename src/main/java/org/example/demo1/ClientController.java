package org.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;

public class ClientController {

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtMessage;
    @FXML
    private ImageView imageView;

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
    @FXML
    void sendImageOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file=fileChooser.showOpenDialog(new Stage());
        if (file!=null){
            try {
                byte[] imageBytes=
                        Files.readAllBytes(file.toPath());
                dataOutputStream=new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF("IMAGE");
                dataOutputStream.writeInt(imageBytes.length);
                dataOutputStream.write(imageBytes);
                dataOutputStream.flush();
                txtArea.appendText(file.getName()+"\n");
                txtArea.appendText(file.getAbsolutePath()+"\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
