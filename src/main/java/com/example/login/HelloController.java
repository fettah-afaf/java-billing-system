package com.example.login;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    public Button signinbutton;
    @FXML
    private Button cancelbutton;
    @FXML
    private Button signupbutton;







    @FXML
    private Label signinmessagelabel;
    @FXML
    private TextField usernametextfield;
    @FXML
    private PasswordField passwordpasswordfield;
    @FXML
    private ComboBox<String> combobox;
    ObservableList<String> list = FXCollections.observableArrayList("ADMIN", "USER");

    public void singinbuttonOnAction(){


        if (!usernametextfield.getText().isBlank() && !passwordpasswordfield.getText().isBlank()) {
            signinmessagelabel.setText("you are trying to sign in!");
            validateSignIn();


        }
        else {
            signinmessagelabel.setText("please enter the username and password");
        }
    }
    public void cancelbuttonOnAction(ActionEvent e){
        Stage stage=(Stage) cancelbutton.getScene().getWindow();
        stage.close();
    }
    public void validateSignIn() {
        db connectNow = new db();
        Connection connectDb = connectNow.getConnection();
        if(combobox.getValue()=="USER") {
            UserModel.getInstance(usernametextfield.getText());
        String query = "SELECT COUNT(1) FROM users WHERE username=? AND password=?";
        try (Connection con = connectDb;
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setString(1, usernametextfield.getText());
            preparedStatement.setString(2, passwordpasswordfield.getText());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                   if(resultSet.getInt(1)==1){
                       signinmessagelabel.setText("welcome");
                       try {
                           // Load the signup.fxml file
                           FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("loginsuc.fxml"));
                           Parent root = loader.load();


                           // Set the username in the loginsucController


                           // Create a new stage
                           Stage stage = new Stage();
                           stage.setScene(new Scene(root));

                           // Show the new stage
                           stage.show();

                           // Close the current stage (optional)
                            ((Stage) signupbutton.getScene().getWindow()).close();

                       } catch (IOException e) {
                           e.printStackTrace();
                           System.out.println("Error loading loginsuc.fxml: " + e.getMessage());
                       }
                   }else {
                       signinmessagelabel.setText("invalid login ,please try again");
                   }
                }
            }
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace();
        }}
        else if(combobox.getValue()=="ADMIN") {
            adminmodel.getInstance(Integer.valueOf(usernametextfield.getText()));
            System.out.println(adminmodel.getId_admin());
            String query = "SELECT COUNT(1) FROM admin WHERE admin_id=? AND password=?";
            try (Connection con = connectDb;
                 PreparedStatement preparedStatement = con.prepareStatement(query)) {

                preparedStatement.setString(1, usernametextfield.getText());
                preparedStatement.setString(2, passwordpasswordfield.getText());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while(resultSet.next()) {
                        if(resultSet.getInt(1)==1){
                            //signinmessagelabel.setText("welcome");
                            try {
                                // Load the signup.fxml file
                                FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("loginsucAdmin.fxml"));


                                // Create a new stage
                                Parent root = loader.load();


                                // Set the username in the loginsucController


                                // Create a new stage
                                Stage stage = new Stage();
                                stage.setScene(new Scene(root));

                                // Show the new stage
                                stage.show();
                                ((Stage) signupbutton.getScene().getWindow()).close();

                                // Close the current stage (optional)
                                // ((Stage) signupButton.getScene().getWindow()).close();

                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("Error loading loginsucAdmin.fxml: " + e.getMessage());
                            }
                        }else {
                            signinmessagelabel.setText("invalid login ,please try again");
                        }
                    }
                }
            } catch (SQLException e) {
                // Handle exceptions
                e.printStackTrace();
            }}
        else {
            signinmessagelabel.setText("User or Admin? Select Login type");
        }
    }
    public void signupbuttonOnAction(){
       /* if(e.getSource()==signupbutton){
           sigup.setVisible(true);
           logine.setVisible(false);

        }*/

        try {
        // Load the signup.fxml file
        FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("signup.fxml"));

        Parent root = loader.load();

        // Create a new stage
        Stage stage1 = new Stage();
        stage1.setScene(new Scene(root));
            stage1.initStyle(StageStyle.UNDECORATED);

        // Show the new stage
        stage1.show();

        // Close the current stage (optional)
         ((Stage) signupbutton.getScene().getWindow()).close();

    } catch (IOException ex) {
        ex.printStackTrace();
        System.out.println("Error loading signup.fxml: " + ex.getMessage());
    }
    }
@Override
    public void initialize(URL arg0, ResourceBundle arg1) {



        combobox.setItems(list);
    }

}





