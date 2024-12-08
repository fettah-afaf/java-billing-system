package com.example.login;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.control.Alert;


import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ViewBills implements Initializable {

    private String username;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    // Getter and setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @FXML
    private TableColumn<com.example.login.Bill, Integer> bill_idcol;

    @FXML
    private TableView<com.example.login.Bill> bill_table;

    @FXML
    private TableColumn<com.example.login.Bill, Timestamp> date_idcol;

    @FXML
    private TableColumn<com.example.login.Bill, Integer> id_plancol;

    @FXML
    private TableColumn<com.example.login.Bill, String> statuscol;

    @FXML
    private TableColumn<com.example.login.Bill, Double> total_amountcol;

    private ObservableList<com.example.login.Bill> billData = FXCollections.observableArrayList();


    @FXML
    private TextField usernameField;

    @FXML
    private Label messageLabel4;

    private void loadDataFromDatabase(String username) {
        try {
            billData.clear();

            String query = "SELECT id_bill, created_at, plan_id, status, total_amount " +
                    "FROM bill " +
                    "WHERE username = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Bill bill = new Bill(
                        resultSet.getInt("id_bill"),
                        resultSet.getTimestamp("created_at"),
                        resultSet.getInt("plan_id"),
                        resultSet.getString("status"),
                        resultSet.getDouble("total_amount")
                );
                billData.add(bill);
            }

            // Set the items in the TableView
            bill_table.setItems(billData);
        } catch (SQLException ex) {
            Logger.getLogger(ViewBills.class.getName()).log(Level.SEVERE, null, ex);
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
        bill_idcol.setCellValueFactory(cellData -> cellData.getValue().idBillProperty().asObject());
        date_idcol.setCellValueFactory(cellData -> cellData.getValue().dateCreatedProperty());
        id_plancol.setCellValueFactory(cellData -> cellData.getValue().idPlanProperty().asObject());
        statuscol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        total_amountcol.setCellValueFactory(cellData -> cellData.getValue().totalAmountProperty().asObject());

        // Populate data from the database with a specific username
        loadDataFromDatabase(username);
    }
    @FXML
    void loadBillsButtonOnAction() {
        String username = usernameField.getText();

        if (!username.isEmpty()) {
            loadDataFromDatabase(username);
            messageLabel4.setText("Bills loaded successfully.");
        } else {
            messageLabel4.setText("Please enter a username.");
        }
    }

}




/*
public class ViewUnpaidBills extends Application {

    // Informations de connexion à la base de données (à adapter)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/facturation";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private TextField usernameField;
    private TableView<Bill> tableView;
    private ObservableList<Bill> unpaidBills;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Interface de Saisie d'Utilisateur");

        // Interface pour saisir le nom d'utilisateur
        VBox usernameInputLayout = new VBox();
        usernameInputLayout.setPadding(new Insets(20, 20, 20, 20));
        usernameInputLayout.setSpacing(10);

        // Label pour le champ utilisateur
        Label usernameLabel = new Label("Nom d'utilisateur:");
        usernameField = new TextField();
        usernameField.setPromptText("Saisir le nom d'utilisateur");

        Button viewBillsButton = new Button("Voir les Factures Non Payées");
        viewBillsButton.setOnAction(e -> showUnpaidBills(usernameField.getText()));

        usernameInputLayout.getChildren().addAll(usernameLabel, usernameField, viewBillsButton);

        Scene usernameScene = new Scene(usernameInputLayout, 300, 150);

        // Afficher la première interface
        primaryStage.setScene(usernameScene);
        primaryStage.show();
    }

    private void showUnpaidBills(String username) {
        // Interface pour afficher les factures non payées
        Stage billStage = new Stage();
        billStage.setTitle("Liste des Factures Non Payées");

        // Création de la table
        tableView = new TableView<>();
        unpaidBills = FXCollections.observableArrayList();

        // Création des colonnes
        TableColumn<Bill, Integer> billIdColumn = new TableColumn<>("ID Facture");
        billIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Bill, String> createdAtColumn = new TableColumn<>("Date de Création");
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        TableColumn<Bill, Double> totalAmountColumn = new TableColumn<>("Montant Total");
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        // Ajout des colonnes à la table
        tableView.getColumns().addAll(billIdColumn, createdAtColumn, totalAmountColumn);

        // Chargement des données depuis la base de données
        refreshUnpaidBills(username);

        // Mise en forme de la scène
        VBox layout = new VBox(tableView);
        layout.setPadding(new Insets(20, 20, 20, 20));

        Scene billScene = new Scene(layout, 600, 400);

        billStage.setScene(billScene);
        billStage.show();

        Button closeButton = new Button("Return");
        closeButton.setOnAction(e -> billStage.close());

        layout = new VBox(tableView, closeButton);
        layout.setPadding(new Insets(20, 20, 20, 20));

        billScene = new Scene(layout, 600, 400);

        billStage.setScene(billScene);
        billStage.show();
    }

    // Méthode pour actualiser les factures non payées pour un utilisateur spécifique
    private void refreshUnpaidBills(String username) {
        unpaidBills.clear();
        unpaidBills.addAll(fetchUnpaidBills(username));
        tableView.setItems(unpaidBills);
    }

    // Méthode pour récupérer les factures non payées depuis la base de données
    private ObservableList<Bill> fetchUnpaidBills(String username) {
        ObservableList<Bill> unpaidBills = FXCollections.observableArrayList();

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String sql = "SELECT id_bill, created_at, total_amount FROM bill WHERE username = ? AND status = 'Pending'";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id_bill");
                String createdAt = resultSet.getString("created_at");
                double totalAmount = resultSet.getDouble("total_amount");

                Bill bill = new Bill(id, createdAt, totalAmount);
                unpaidBills.add(bill);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return unpaidBills;
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Modèle de données pour représenter une facture
    public static class Bill {
        private int id;
        private String createdAt;
        private double totalAmount;

        public Bill(int id, String createdAt, double totalAmount) {
            this.id = id;
            this.createdAt = createdAt;
            this.totalAmount = totalAmount;
        }

        public int getId() {
            return id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public double getTotalAmount() {
            return totalAmount;
        }
    }
}











































  private static final String DB_URL = "jdbc:mysql://localhost:3306/facturation";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    private TextField usernameField;
    @FXML
    private TableView<Bill> tableView;
    private ObservableList<Bill> bills;

    @FXML
    private TableColumn<Bill, Integer> billIdColumn;

    @FXML
    private TableColumn<Bill, String> createdAtColumn;

    @FXML
    private TableColumn<Bill, Integer> planIdColumn;

    @FXML
    private TableColumn<Bill, String> statusColumn;

    @FXML
    private TableColumn<Bill, Double> totalAmountColumn;


    @FXML
    private Label usernameLabel;

    @FXML
    private Button viewBillsButton;

    @FXML
    void viewBillsButtonOnAction(ActionEvent event) {
        String username = usernameField.getText();
        if (!username.isEmpty()) {
            showAllBills(username);
        } else {
            // Handle case where username is empty
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Username");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a username.");
            alert.showAndWait();
        }
    }


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Interface de Saisie d'Utilisateur");

        VBox layout = new VBox();
        layout.setPadding(new Insets(20, 20, 20, 20));

        HBox usernameBox = new HBox();
        Label usernameLabel = new Label("Entrer utilisateur:");
        usernameField = new TextField();
        usernameField.setPromptText("Saisir le nom d'utilisateur");
        usernameBox.getChildren().addAll(usernameLabel, usernameField);

        Button viewBillsButton = new Button("Voir toutes les Factures");



        tableView = new TableView<>();
        bills = FXCollections.observableArrayList();

        TableColumn<Bill, Integer> billIdColumn = new TableColumn<>("ID Facture");
        billIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Bill, String> createdAtColumn = new TableColumn<>("Date de Création");
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        TableColumn<Bill, Double> totalAmountColumn = new TableColumn<>("Montant Total");
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        TableColumn<Bill, Integer> planIdColumn = new TableColumn<>("ID Plan");
        planIdColumn.setCellValueFactory(new PropertyValueFactory<>("planId"));

        TableColumn<Bill, String> statusColumn = new TableColumn<>("Statut");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.getColumns().addAll(billIdColumn, createdAtColumn, totalAmountColumn, planIdColumn, statusColumn);

        layout.getChildren().addAll(usernameBox, viewBillsButton, tableView);

        Scene scene = new Scene(layout, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAllBills(String username) {
        refreshAllBills(username);
    }

    private void refreshAllBills(String username) {
        bills.clear();
        bills.addAll(fetchAllBills(username));
        tableView.setItems(bills);
    }

    private ObservableList<Bill> fetchAllBills(String username) {
        ObservableList<Bill> bills = FXCollections.observableArrayList();

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String sql = "SELECT id_bill, created_at, total_amount, plan_id, status FROM bill WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id_bill");
                String createdAt = resultSet.getString("created_at");
                double totalAmount = resultSet.getDouble("total_amount");
                int planId = resultSet.getInt("plan_id");
                String status = resultSet.getString("status");

                Bill bill = new Bill(id, createdAt, totalAmount, planId, status);
                bills.add(bill);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return bills;
    }



    public static class Bill {
        private int id;
        private String createdAt;
        private double totalAmount;
        private int planId;
        private String status;

        public Bill(int id, String createdAt, double totalAmount, int planId, String status) {
            this.id = id;
            this.createdAt = createdAt;
            this.totalAmount = totalAmount;
            this.planId = planId;
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public int getPlanId() {
            return planId;
        }

        public String getStatus() {
            return status;
        }
    }









    private String username;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    // Getter and setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }








*/