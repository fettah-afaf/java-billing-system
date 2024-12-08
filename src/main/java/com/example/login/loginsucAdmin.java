package com.example.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class loginsucAdmin implements Initializable {


    private String username;

    @FXML
    private Button Complaint;
    @FXML
    private Button addmeterbill;
    @FXML
    private Button ViewBills;
    @FXML
    private Button UpdateUserApp;
    @FXML
    private Button MonthlyConsumptionStatistics;
    @FXML
    private Button ViewUsersAndSearch;
    @FXML
    private Button ViewPayments;


    @FXML
    private BorderPane mainpane;
    @FXML
    private Button changepassword;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane panetoswitch;
    @FXML
    private Label usernamelabel;


    @FXML
    public void addmeterbillOnAction(){
        fxmloader object=new fxmloader();
        Pane view;
        view = object.getPage("addmeterbill");
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
    public  void viewcomplaintOnAction(ActionEvent event) {
        System.out.println("View complaint");
        fxmloader object=new fxmloader();
        Pane view=object.getPage("ViewComplaints");
        mainpane.setCenter(view);
    }




    @FXML
    void changepasswordOnAction(ActionEvent event) {
        fxmloader object=new fxmloader();
        Pane view=object.getPage("changepassAdmin");
        mainpane.setCenter(view);
    }


    @FXML
    public void viewbillsOnAction(ActionEvent event) {
        fxmloader object=new fxmloader();
        Pane view=object.getPage("ViewBills");
        mainpane.setCenter(view);
    }

    @FXML
    public void viewpaymentsOnAction(ActionEvent event) {
        fxmloader object=new fxmloader();
        Pane view=object.getPage("ViewPayments");
        mainpane.setCenter(view);
    }

    @FXML
    public void viewusersandsearchOnAction(ActionEvent event){
        fxmloader object=new fxmloader();
        Pane view=object.getPage("ViewAndSearchUsers");
        mainpane.setCenter(view);
    }
    @FXML
    public void MonthlyConsumptionStatisticsOnAction(ActionEvent event) {
        fxmloader object=new fxmloader();
        Pane view=object.getPage("MonthlyConsumptionStatistics");
        mainpane.setCenter(view);

    }

    @FXML
    public void UpdateUserAppOnAction(ActionEvent event) {
        fxmloader object=new fxmloader();
        Pane view=object.getPage("UpdateUserApp");

        mainpane.setCenter(view);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addmeterbillOnAction();


    }


}