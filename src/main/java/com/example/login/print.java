package com.example.login;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class print  implements Initializable {
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
    private TableColumn<Bill, Integer>  bill_idcolp;

    @FXML
    private TableView<Bill> bill_tableprint;

    @FXML
    private Label bonjourusername12;

    @FXML
    private TableColumn<Bill, Timestamp> date_idcolp;

    @FXML
    private TableColumn<Bill, Integer> id_plancolp;

    @FXML
    private TableColumn<Bill, String> statuscolp;

    @FXML
    private TableColumn<Bill, Double> total_amountcolp;

    private ObservableList<Bill> billDataprint = FXCollections.observableArrayList();

    private void loadDataFromDataprint() {
        try {
            billDataprint.clear();
            setUsername(UserModel.getUsername());
            bonjourusername12.setText("hello "+username);
            System.out.println(username);


            String query = "SELECT id_bill, created_at, plan_id, status, total_amount " +
                    "FROM bill " +
                    "WHERE username = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, getUsername());

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Bill bill = new Bill(
                        resultSet.getInt("id_bill"),
                        resultSet.getTimestamp("created_at"),
                        resultSet.getInt("plan_id"),
                        resultSet.getString("status"),
                        resultSet.getDouble("total_amount")
                );
                billDataprint.add(bill);
            }

            // Set the items in the TableView
            bill_tableprint.setItems(billDataprint);
        } catch (SQLException ex) {
            Logger.getLogger(billviewController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Close resources in a finally block
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
        bill_idcolp.setCellValueFactory(cellData -> cellData.getValue().idBillProperty().asObject());
        date_idcolp.setCellValueFactory(cellData -> cellData.getValue().dateCreatedProperty());
        id_plancolp.setCellValueFactory(cellData -> cellData.getValue().idPlanProperty().asObject());
        statuscolp.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        total_amountcolp.setCellValueFactory(cellData -> cellData.getValue().totalAmountProperty().asObject());

        // Populate data from the database with a specific username
        loadDataFromDataprint();
        bill_tableprint.setRowFactory(tv -> {
            TableRow<Bill> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    Bill selectedBill = row.getItem();
                    openNewStage(selectedBill);
                }
            });
            return row;
        });
    }
    private void openNewStage(Bill selectedBill) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("choice.fxml"));
            Parent root = loader.<Parent>load();


            // Get the controller of the new stage
            choice newStageController = loader.getController();

            // Pass the data to the new stage controller
            newStageController.setData(selectedBill);
            newStageController.setIdBill(selectedBill.getIdBill());

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.initStyle(StageStyle.UNDECORATED);
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

