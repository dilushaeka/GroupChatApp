package lk.dil.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Login1FormController {

    public JFXTextField txtName;
    public JFXButton strtBtn;
    public AnchorPane loginContext;

    public static String name;


    public void StratBtnOnACtion(ActionEvent actionEvent) throws IOException {
        name=txtName.getText();
        loginContext.getChildren().clear();
        Stage stage=(Stage) loginContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/client1Form.fxml"))));

    }
}
