package lk.dil.controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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

public class Client2FormController {
    public ImageView btnSend;
    public ImageView btnAssets;
    public TextField txtMessage;
    public ImageView btnCamera;
    public AnchorPane emojiPane;
    public Label lblClient;

    public int PORT = 5500;
    public ScrollPane msgContext;

    Socket socket;

    DataInputStream dataInputStream;

    DataOutputStream dataOutputStream;

    String path = "";

    String message = "";

    int i = 10;

    public AnchorPane context = new AnchorPane();


    File file;

    public static boolean isImageChoose = false;

    boolean isUsed = false;

    public static String name;

    public void initialize() {
        Platform.setImplicitExit(false);
        msgContext.setContent(context);
        msgContext.vvalueProperty().bind(context.heightProperty());
        msgContext.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        msgContext.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        lblClient.setText(Login2FormController.name);
        name = lblClient.getText();

        new Thread(() -> {
            try {
                socket = new Socket("localhost", PORT);

                while (true) {
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    message = dataInputStream.readUTF();
                    System.out.println(message);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (message.startsWith("/")) {
                                BufferedImage sendImage = null;
                                try {
                                    sendImage = ImageIO.read(new File(message));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Image img = SwingFXUtils.toFXImage(sendImage, null);
                                ImageView imageView = new ImageView(img);
                                imageView.setFitHeight(150);
                                imageView.setFitWidth(150);
                                imageView.setLayoutY(i);
                                context.getChildren().add(imageView);
                                i += 150;
                            } else if (message.startsWith(Login2FormController.name)) {
                                message = message.replace(Login2FormController.name, "You");
                                Label label = new Label(message);
                                label.setStyle(" -fx-font-family: Ubuntu; -fx-font-size: 20px; -fx-background-color: #85b6ff; -fx-text-fill: #5c5c5c");
                                label.setLayoutY(i);
                                context.getChildren().add(label);
                            } else {
                                Label label = new Label(message);
                                label.setStyle(" -fx-font-family: Ubuntu; -fx-font-size: 20px; -fx-background-color: #CDB4DB; -fx-text-fill: #5c5c5c");
                                label.setLayoutY(i);
                                context.getChildren().add(label);
                            }
                            i += 30;
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendMessage() throws IOException {
        if (isImageChoose) {
            dataOutputStream.writeUTF(path.trim());
            dataOutputStream.flush();
            isImageChoose = false;
        } else {
            dataOutputStream.writeUTF(lblClient.getText() + ":" + txtMessage.getText().trim());
            dataOutputStream.flush();
        }
        txtMessage.clear();
    }


    public void sendOnAction(MouseEvent mouseEvent) throws IOException {
        sendMessage();
    }

    public void asssetsOnAction(MouseEvent mouseEvent) {
        FileChooser chooser = new FileChooser();
        Stage stage = new Stage();
        file = chooser.showOpenDialog(stage);

        if (file != null) {
            path = file.getPath();
            System.out.println("Selected image");
            System.out.println(file.getPath());
            isImageChoose = true;
        }
    }

    public void cameraOnAction(MouseEvent mouseEvent) {
        if (isUsed) {
            emojiPane.getChildren().clear();
            isUsed = false;
            return;
        }
        isUsed = true;
        VBox dialoVbox = new VBox(20);
        ImageView smile = new ImageView((new Image("lk/dil/assets/icons/smile.png")));
        smile.setFitWidth(30);
        smile.setFitHeight(30);
        dialoVbox.getChildren().add(smile);

        ImageView heart = new ImageView((new Image("lk/dil/assets/icons/heart.png")));
        heart.setFitWidth(30);
        heart.setFitHeight(30);
        dialoVbox.getChildren().add(heart);

        ImageView sadFace = new ImageView((new Image("lk/dil/assets/icons/sad-face.png")));
        sadFace.setFitWidth(30);
        sadFace.setFitHeight(30);
        dialoVbox.getChildren().add(sadFace);
        smile.setOnMouseClicked(event -> {
            txtMessage.setText(txtMessage.getText() + "☺");
        });
        smile.setOnMouseClicked(event -> {
            txtMessage.setText(txtMessage.getText() + "♥");
        });
        smile.setOnMouseClicked(event -> {
            txtMessage.setText(txtMessage.getText() + "☹");
        });
        emojiPane.getChildren().add(dialoVbox);
    }

    public void exitOnAction(ActionEvent actionEvent) throws IOException {
        if (socket != null) {
            dataOutputStream.writeUTF("exit".trim());
            dataOutputStream.flush();
            System.exit(0);
        }
        System.exit(0);
    }
}
