package lk.dil.controller;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFormController {

    public TextField txtMessage;
    public ImageView btnCamera;
    public ImageView btnAssets;
    public ImageView btnSend;
    public TextArea txtArea;

    ServerSocket serverSocket;

    Socket socket;

    public void initialize(){
        new Thread(()->{

            try {
                serverSocket = new ServerSocket(3000);
                txtArea.appendText("Server Is Started");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

    }

    public void cameraOnAction(MouseEvent mouseEvent) {
    }

    public void asssetsOnAction(MouseEvent mouseEvent) {
    }

    public void sendOnAction(MouseEvent mouseEvent) {
    }
}
