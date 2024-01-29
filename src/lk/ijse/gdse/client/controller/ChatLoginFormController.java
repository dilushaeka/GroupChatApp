package lk.ijse.gdse.client.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatLoginFormController {


    public AnchorPane pane;
    public JFXTextField txtUserName;

    public void btnLoginOnAction(ActionEvent actionEvent) {

        if (txtUserName.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Name is empty !", ButtonType.OK).show();
            txtUserName.setFocusColor(Paint.valueOf("Red"));
            txtUserName.requestFocus();
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/lk/ijse/gdse/client/view/ChatForm.fxml"));
        Parent load = null;
        try {
            load = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ChatFormController controller = fxmlLoader.getController();
        controller.setUserName(txtUserName.getText());
        Stage stage = new Stage();
        stage.setTitle("Group Chat Application");
        stage.setScene(new Scene(load));
        controller.setStage(stage);
        stage.centerOnScreen();
        //stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        txtUserName.clear();
    }
}
