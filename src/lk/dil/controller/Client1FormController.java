package lk.dil.controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class Client1FormController {

    public TextField txtMessage;
    public ImageView btnCamera;
    public ImageView btnAssets;
    public ImageView btnSend;
    public TextArea txtArea;

    public AnchorPane emojiPane;

    public Label lblClient;

    final int PORT=5000;

    Socket socket;

    DataInputStream dataInputStream;

    DataOutputStream dataOutputStream;

    String path="";

    String message="";

    int i=10;

    public AnchorPane context = new AnchorPane();

    File file;

    public static boolean isImageChoose=false;

    boolean isUsed=false;
    public void initialize(){
        lblClient.setText(Login1FormController.name);

        new Thread(() ->{
            try {
                socket= new Socket("localhost",PORT);
                while (true){
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    message = dataInputStream.readUTF();
                    System.out.println(message);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (message.startsWith("/")){
                                BufferedImage sendImage=null;

                                try {
                                    sendImage= ImageIO.read(new File(message));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                Image img= SwingFXUtils.toFXImage(sendImage,null);
                                ImageView imageView=new ImageView(img);
                                imageView.setFitHeight(150);
                                imageView.setFitWidth(150);
                                imageView.setLayoutY(i);
                                context.getChildren().add(imageView);
                                i+=150;
                            } else if (message.startsWith(Login1FormController.name)) {
                                message = message.replace(Login1FormController.name,"You");
                                Label label=new Label(message);
                                label.setStyle(" -fx-font-family: Ubuntu; -fx-font-size: 20px; -fx-background-color: #85b6ff; -fx-text-fill: #5c5c5c");
                                label.setLayoutY(i);
                                context.getChildren().add(label);
                            }else {
                                Label label= new Label(message);
                                label.setStyle(" -fx-font-family: Ubuntu; -fx-font-size: 20px; -fx-background-color: #CDB4DB; -fx-text-fill: #5c5c5c");
                                label.setLayoutY(i);
                                context.getChildren().add(label);
                            }
                            i+=30;
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        txtMessage.setOnAction(event -> {
            try {
                sendMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendMessage() throws IOException {
        if (isImageChoose){
          dataOutputStream.writeUTF(path.trim());
          dataOutputStream.flush();
          isImageChoose=false;
        } else {
            dataOutputStream.writeUTF(lblClient.getText()+":"+txtMessage.getText().trim());
            dataOutputStream.flush();
        }
        txtMessage.clear();
    }


    public void asssetsOnAction(MouseEvent mouseEvent) {
        FileChooser chooser= new FileChooser();
        Stage stage=new Stage();
        file =chooser.showOpenDialog(stage);

        if (file!=null){
            path= file.getPath();
            System.out.println("Selected");
            System.out.println(file.getPath());
            isImageChoose=true;
        }
    }

    public void sendOnAction(MouseEvent mouseEvent) throws IOException {
        sendMessage();
    }

    public void cameraOnAction(MouseEvent mouseEvent) {
        if (isUsed){
            emojiPane.getChildren().clear();
            isUsed=false;
            return;
        }
        isUsed=true;
        VBox dialoVbox= new VBox(20);
        ImageView smile =new ImageView((new Image("lk/dil/assets/icons/smile.png")));
        smile.setFitWidth(30);
        smile.setFitHeight(30);
        dialoVbox.getChildren().add(smile);

        ImageView heart =new ImageView((new Image("lk/dil/assets/icons/heart.png.png")));
        smile.setFitWidth(30);
        smile.setFitHeight(30);
        dialoVbox.getChildren().add(heart);

        ImageView sadFace =new ImageView((new Image("lk/dil/assets/icons/sad-face.png.png")));
        smile.setFitWidth(30);
        smile.setFitHeight(30);
        dialoVbox.getChildren().add(sadFace);
        smile.setOnMouseClicked(event -> {
           txtMessage.setText(txtMessage.getText()+"☺");
        });
        smile.setOnMouseClicked(event -> {
            txtMessage.setText(txtMessage.getText()+"♥");
        });
        smile.setOnMouseClicked(event -> {
            txtMessage.setText(txtMessage.getText()+"☹");
        });
        emojiPane.getChildren().add(dialoVbox);
    }
}
