package com.example.login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class settingsController {

    @FXML
    private Button changeemailbutton;

    @FXML
    private Button changepasswordbutton;

    @FXML
    private Button changeplanbutton;


    public void changepasswordbuttonOnAction(ActionEvent event){
        System.out.println("change password ");
        openNewStage("changepassword.fxml");

    }
   public void changeemailbuttonOnAction(ActionEvent event){
        System.out.println("change password ");
        openNewStage("changeemail.fxml");

    }
    public void changeplanbuttonOnAction(ActionEvent event){
        System.out.println("change password ");
        openNewStage("changeplan.fxml");

    }

    private void openNewStage(String name) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(name));
            Parent root = loader.<Parent>load();


            // Get the controller of the new stage


            // Pass the data to the new stage controller

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.initStyle(StageStyle.UNDECORATED);
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
