package com.example.login;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class UpdateUserApp implements Initializable {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/facturation";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    private TextField cinInput;

    @FXML
    private Label cinLabel;

    @FXML
    private Button clearButton;

    @FXML
    private TextField emailInput;

    @FXML
    private Label emailLabel;

    @FXML
    private Button fetchButton;

    @FXML
    private TextField firstNameInput;

    @FXML
    private Label firstNameLabel;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private Label genderLabel;

    @FXML
    private TextField lastNameInput;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Button updateButton;

    @FXML
    private TextField usernameInput;

    @FXML
    private Label usernameLabel;
    @FXML
    private Label messageLabel2;

    @FXML
    void clearButtonOnAction(ActionEvent event) {
        lastNameInput.clear();
        firstNameInput.clear();
        genderComboBox.setValue(null);
        emailInput.clear();
        cinInput.clear();
        setFieldsEditable(false);
    }

    @FXML
    void fetchButtonOnAction(ActionEvent event) {
        String username = usernameInput.getText();
        copyValuesToEditableFields(username);
        setFieldsEditable(true);
    }

    @FXML
    void updateButtonOnAction(ActionEvent event) {
        // Récupérer le nom d'utilisateur pour la mise à jour
        String username = usernameInput.getText();

        // Récupérer les nouvelles valeurs
        String lastName = lastNameInput.getText();
        String firstName = firstNameInput.getText();
        String gender = genderComboBox.getValue();
        String email = emailInput.getText();
        String cin = cinInput.getText();

        // Appeler la méthode de mise à jour
        boolean success = updateUserDetails(username, lastName, firstName, gender, email, cin);

        if (success) {
            // Afficher un message de succès dans l'étiquette
            updateMessageLabel("Informations de l'utilisateur mises à jour avec succès.");
        } else {
            // Afficher un message si les informations n'ont pas été modifiées
            updateMessageLabel("Aucune modification apportée aux informations de l'utilisateur.");
        }
    }

    private void updateMessageLabel(String messageText) {
        messageLabel2.setText(messageText);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeGenderComboBox();
    }




    private void initializeGenderComboBox() {
        genderComboBox.getItems().addAll("FEMALE", "MALE");
        genderComboBox.setPromptText("Sélectionnez le genre");
    }

    private void copyValuesToEditableFields(String username) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String sql = "SELECT * FROM users WHERE username=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String lastName = resultSet.getString("last_name");
                String firstName = resultSet.getString("first_name");
                String gender = resultSet.getString("gender");
                String email = resultSet.getString("email");
                String cin = resultSet.getString("cin");

                lastNameInput.setText(lastName);
                firstNameInput.setText(firstName);
                genderComboBox.setValue(gender);
                emailInput.setText(email);
                cinInput.setText(cin);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void setFieldsEditable(boolean editable) {
        firstNameInput.setEditable(editable);
        lastNameInput.setEditable(editable);
        genderComboBox.setDisable(!editable);
        emailInput.setEditable(editable);
        cinInput.setEditable(editable);
    }

    private boolean updateUserDetails(String username,String firstName ,String lastName , String gender, String email, String cin) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String sql = "UPDATE users SET first_name=?, last_name=?,  gender=?, email=?, cin=? WHERE username=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,firstName );
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, gender);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, cin);
            preparedStatement.setString(6, username);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }



}