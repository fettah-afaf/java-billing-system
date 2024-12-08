package com.example.login;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;


public class loginsucController implements Initializable{

    @FXML
    public Label usernamelabelfx;
    private String username;
    @FXML
    private ImageView mode;






    @FXML
    private BorderPane mainpane;
    @FXML
    private Button changepassword;
    @FXML
    private Button print;

    @FXML
    private Button consultprofile;

    @FXML
    private Button logout;
    @FXML
    private Button paybills;

    @FXML
    private Button statics;
    @FXML
    private AnchorPane panetoswitch;
    @FXML
    private Label usernamelabel;
    public void modeOnMouseClick(){
        if (mainpane.getStylesheets().contains("styles.css")) {
            mainpane.getStylesheets().clear();
            mainpane.getStylesheets().add(getClass().getResource("nightmode.css").toExternalForm());
            System.out.println("hrlo");
        } else {
            mainpane.getStylesheets().clear();
            mainpane.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        }

    }


    @FXML
    public void billviewOnAction(){
        fxmloader object=new fxmloader();
        Pane view;
        view = object.getPage("billview");
        mainpane.setCenter(view);
    }
    @FXML
    public void logoutOnAction(ActionEvent event) {
        // Close the current (signup) window
        Stage stage = (Stage) logout.getScene().getWindow();
        stage.close();

        try {
            // Load the login window
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 520, 400);

            // Create a new stage for the login window
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.setScene(scene);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately (log it, show an error message, etc.)
        }
    }
    @FXML
    public  void addcomplaintOnAction(ActionEvent event) {
        System.out.println("Adding complaint");
        fxmloader object=new fxmloader();
        Pane view=object.getPage("complaint");
        mainpane.setCenter(view);
    }




    @FXML
    void settingsOnAction(ActionEvent event) {
        fxmloader object=new fxmloader();
        Pane view=object.getPage("settings");
        mainpane.setCenter(view);
    }


    @FXML
   public void consultprofileOnAction(ActionEvent event) {
            fxmloader object=new fxmloader();
            Pane view=object.getPage("profile");
            mainpane.setCenter(view);
            }


    public void paybillsOnAction(ActionEvent event){
        fxmloader object=new fxmloader();
        Pane view=object.getPage("paybills");
        mainpane.setCenter(view);
    }
    public void staticsonAction(ActionEvent event) {
        fxmloader object=new fxmloader();
        Pane view=object.getPage("statistics");
        mainpane.setCenter(view);

    }
    @FXML
    void printOnAction(ActionEvent event) {
        fxmloader object=new fxmloader();
        Pane view=object.getPage("printbill");
        mainpane.setCenter(view);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usernamelabelfx.setText(UserModel.getUsername());
        System.out.println(usernamelabelfx.getText());
        billviewOnAction();
    }




}




