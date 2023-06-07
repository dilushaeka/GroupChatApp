package lk.dil.controller;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.dil.model.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFormController {

    public TextField txtMessage;
    public ImageView btnCamera;
    public ImageView btnAssets;
    public ImageView btnSend;
    public TextArea txtArea;
    public Label lblClient;

    Socket socket;

    DataInputStream dataInputStream0;

    DataOutputStream dataOutputStream0;

    final int PORT=6500;

    final static int PORT1= 5000;

    final int PORT2=5500;

    final int PORT3=6000;
    public AnchorPane emojiPane;

    String message="";

    public AnchorPane context= new AnchorPane();

    Client serverClient;

    Client client;

    Client client2;

    Client client3;



    public void initialize(){


        new Thread(()->{
            while (true){
                serverClient = new Client(PORT);
                try {
                    serverClient.setName("You");
                    serverClient.acceptConnection();
                    serverClient.setInputOutput();
                    procesTextMessage(serverClient,serverClient.getDataInputStream());
                    client=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void cameraOnAction(MouseEvent mouseEvent) {
    }

    public void asssetsOnAction(MouseEvent mouseEvent) {
    }

    public void sendOnAction(MouseEvent mouseEvent) throws IOException {
        dataOutputStream0.writeUTF("Owner : "+ txtMessage.getText().trim());
        dataOutputStream0.flush();
        txtMessage.clear();
    }

    public void procesTextMessage(Client client,DataInputStream dataInputStream) throws IOException {
        if (dataOutputStream0 !=null){
            dataOutputStream0.writeUTF("👋\t\t\t"+client.getName() +"   Joined Here \t\t\t👋".trim());
            dataOutputStream0.flush();
        }

        while (true){
            message = dataInputStream.readUTF();
            System.out.println(message);
            if (message.equals("exit")){
                client.setAccept(null);
                dataOutputStream0.writeUTF("👋\t\t\t"+client.getName()+"left\t\t\t👋".trim());
                dataOutputStream0.flush();
                client.setServerSocket(null);
                return;
            }
            sendTextMessage(message);
        }
    }
    private void sendTextMessage(String message) throws IOException {
        if (serverClient.getAccept()!=null){
            serverClient.getDataOutputStream().writeUTF(message.trim());
            serverClient.getDataOutputStream().flush();
        }
        if (client.getAccept()!=null){
            client.getDataOutputStream().writeUTF(message.trim());
            client.getDataOutputStream().flush();
        }
        if (client2.getAccept()!=null){
            client2.getDataOutputStream().writeUTF(message.trim());
            client2.getDataOutputStream().flush();
        }
        if (client3.getAccept()!=null){
            client3.getDataOutputStream().writeUTF(message.trim());
            client3.getDataOutputStream().flush();
        }
    }
}

