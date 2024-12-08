package com.example.login;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewAndSearchUsers implements Initializable {

    private String username;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @FXML
    private TableColumn<UserProfile, String> usernameCol;

    @FXML
    private TableColumn<UserProfile, String> lastNameCol;

    @FXML
    private TableColumn<UserProfile, String> firstNameCol;

    @FXML
    private TableColumn<UserProfile, String> genderCol;

    @FXML
    private TableColumn<UserProfile, String> emailCol;

    @FXML
    private TableView<UserProfile> userTable;

    private ObservableList<UserProfile> userData = FXCollections.observableArrayList();

    @FXML
    private Label usernamelabel;
    @FXML
    private TextField usernameField;

    @FXML
    private Label messageLabel3;

    private void loadDataFromDatabase(String username) {
        try {
            userData.clear();

            // Modify the query to fetch user information
            String query = "SELECT username, last_name, first_name, gender, email " +
                    "FROM users " +
                    "WHERE username = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UserProfile user = new UserProfile(
                        resultSet.getString("username"),
                        resultSet.getString("last_name"),
                        resultSet.getString("first_name"),
                        resultSet.getString("gender"),
                        resultSet.getString("email")
                );
                userData.add(user);
            }

            // Set the items in the TableView
            userTable.setItems(userData);
        } catch (SQLException ex) {
            Logger.getLogger(ViewAndSearchUsers.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the database connection using your db class
        connection = db.getConnection();

        // Bind columns with corresponding properties
        usernameCol.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        lastNameCol.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        firstNameCol.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        genderCol.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        loadDataFromDatabase(username);
    }

    @FXML
    void loadUserButtonOnAction() {
        String username = usernameField.getText();

        if (!username.isEmpty()) {
            loadDataFromDatabase(username);
            messageLabel3.setText("User information loaded successfully.");
        } else {
            messageLabel3.setText("Please enter a username.");
        }
    }
}






















/*package com.example.login;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class ViewAndSearchUsers implements Initializable {

    // Informations de connexion à la base de données (à adapter)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/facturation";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    private Label usernamelabel;
    @FXML
    private TableColumn<UserProfile, String> emailColumn;

    @FXML
    private TableColumn<UserProfile, String> firstNameColumn;

    @FXML
    private TableColumn<UserProfile, String> genderColumn;

    @FXML
    private TableColumn<UserProfile, String> lastNameColumn;

    @FXML
    private TableView<UserProfile> tableView;

    @FXML
    private TableColumn<UserProfile, String> usernameColumn;

    @FXML
    private ObservableList<UserProfile> userProfiles;

    @FXML
    private TextField searchField;

    @FXML
    private Button refreshButton;
    @FXML
    private Button searchButton;
    @FXML
    private void searchButtonOnAction() {
        String username = searchField.getText().toLowerCase();
        ObservableList<UserProfile> searchResults = FXCollections.observableArrayList();

        for (UserProfile userProfile : userProfiles) {
            if (userProfile.getUsername().toLowerCase().contains(username)) {
                searchResults.add(userProfile);
            }
        }

        tableView.setItems(searchResults);
    }

    @FXML
    public void refreshButtonOnAction(ActionEvent event) {
        refreshUserProfiles();
    }



    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Liste des Profils Utilisateurs");

        // Création de la table
        tableView = new TableView<UserProfile>();
        userProfiles = FXCollections.observableArrayList();

        // Création des colonnes
        TableColumn<UserProfile, String> usernameColumn = new TableColumn<>("Nom d'utilisateur");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<UserProfile, String> lastNameColumn = new TableColumn<>("Nom");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<UserProfile, String> firstNameColumn = new TableColumn<>("Prénom");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<UserProfile, String> genderColumn = new TableColumn<>("Genre");
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

        TableColumn<UserProfile, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Ajout des colonnes à la table
        tableView.getColumns().addAll(usernameColumn, lastNameColumn, firstNameColumn, genderColumn, emailColumn);

        // Chargement des données depuis la base de données
        refreshUserProfiles();

        // Création des champs de recherche
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher par nom d'utilisateur");

        Button searchButton = new Button("Rechercher");
        Button refreshButton = new Button("Actualiser");


        HBox searchBox = new HBox(searchField, searchButton, refreshButton);
        searchBox.setSpacing(10);

        // Mise en forme de la scène
        VBox vbox = new VBox(searchBox, tableView);
        vbox.setPadding(new Insets(20, 20, 20, 20));

        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    // Méthode pour récupérer les profils utilisateurs depuis la base de données
    private void refreshUserProfiles() {
        userProfiles.clear();
        userProfiles.addAll(fetchUserProfiles());
        tableView.setItems(userProfiles);
    }

    // Méthode pour récupérer les profils utilisateurs depuis la base de données
    private ObservableList<UserProfile> fetchUserProfiles() {
        ObservableList<UserProfile> userProfiles = FXCollections.observableArrayList();

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String sql = "SELECT username, last_name, first_name, gender, email FROM users";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String lastName = resultSet.getString("last_name");
                String firstName = resultSet.getString("first_name");
                String gender = resultSet.getString("gender");
                String email = resultSet.getString("email");

                UserProfile userProfile = new UserProfile(username, lastName, firstName, gender, email);
                userProfiles.add(userProfile);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return userProfiles;
    }

    // Méthode pour rechercher un utilisateur par nom d'utilisateur
    private void searchUser(String username) {
        ObservableList<UserProfile> searchResults = FXCollections.observableArrayList();

        for (UserProfile userProfile : userProfiles) {
            if (userProfile.getUsername().toLowerCase().contains(username.toLowerCase())) {
                searchResults.add(userProfile);
            }
        }

        tableView.setItems(searchResults);
    }


    // Modèle de données pour représenter un profil utilisateur
    public static class UserProfile {
        private String username;
        private String lastName;
        private String firstName;
        private String gender;
        private String email;

        public UserProfile(String username, String lastName, String firstName, String gender, String email) {
            this.username = username;
            this.lastName = lastName;
            this.firstName = firstName;
            this.gender = gender;
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public String getLastName() {
            return lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getGender() {
            return gender;
        }

        public String getEmail() {
            return email;
        }
    }
}
*/