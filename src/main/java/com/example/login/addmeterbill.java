package com.example.login;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class addmeterbill implements Initializable {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/facturation";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    @FXML
    private Label message;

    @FXML
    private Label hellousername;

    @FXML
    private Button insertButton;

    @FXML
    private TextField monthlyConsumptionField;

    @FXML
    private Label monthlyConsumptionLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label Label;
    @FXML
    private Label dateLabel;
    @FXML
    private DatePicker dateField;

    @FXML
    void insertButtonOnAction(ActionEvent event) {
        String username = usernameField.getText();
        double monthlyConsumption = 0.0;
        LocalDate consumptionDate = dateField.getValue();

        try {
            monthlyConsumption = Double.parseDouble(monthlyConsumptionField.getText());
        } catch (NumberFormatException e) {
            updateMessageLabel("Erreur : Veuillez saisir une valeur de consommation valide.");
            return;  // Sortir de la méthode si la saisie n'est pas valide
        }

        double cumulativeConsumption = calculateCumulativeConsumption(username, monthlyConsumption);

        if (usernameExists(username)) {
            if (isConsumptionExists(username, consumptionDate)) {
                updateMessageLabel("Erreur : Une consommation existe déjà pour cette date.");
            } else {
                // Utiliser une transaction pour garantir l'atomicité des opérations
                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    connection.setAutoCommit(false);

                    // Vérifier si le mois et l'année sont uniques dans la table bill pour cet utilisateur
                    if (isMonthYearUniqueInMeter(username, consumptionDate)) {
                        // Insérer dans la table meter
                        insertIntoMeter(username, cumulativeConsumption, monthlyConsumption, consumptionDate);

                        // Calculer le total et insérer dans la table bill
                        double totalAmount = calculateTotalAmount(username, cumulativeConsumption);
                        insertIntoBill(username, totalAmount);

                        // Commit si tout s'est bien passé
                        connection.commit();

                        updateMessageLabel("Succès : Informations ajoutées avec succès dans les deux tables.");
                    } else {
                        updateMessageLabel("Erreur : Le mois et l'année doivent être uniques dans la table Bill pour cet utilisateur.");
                    }
                } catch (SQLException e) {
                    // Rollback en cas d'échec
                    updateMessageLabel("Erreur : Échec de l'opération. Veuillez réessayer.");
                    throw new RuntimeException(e);
                }
            }
        } else {
            updateMessageLabel("Erreur : Le nom d'utilisateur n'existe pas dans la table des utilisateurs.");
        }
    }


    private boolean isConsumptionExists(String username, LocalDate consumptionDate) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "SELECT COUNT(*) FROM meter WHERE username = ? AND consumptionDate = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setDate(2, Date.valueOf(consumptionDate));

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    private boolean usernameExists(String username) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT COUNT(*) AS count FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt("count");
                        return count > 0;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
        return false;
    }

    private double calculateCumulativeConsumption(String username, double monthlyConsumption) {
        double cumulativeConsumption = 0.0;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT cumulative_consumption FROM meter WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        cumulativeConsumption = resultSet.getDouble("cumulative_consumption");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
        return cumulativeConsumption + monthlyConsumption;
    }

    private double calculateTotalAmount(String username, double cumulativeConsumption) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        String query = "SELECT u.username, c.cumulative_consumption, " +
                "CASE " +
                "   WHEN c.cumulative_consumption <= p.tier_1_limit THEN c.monthly_consumption * p.tier_1_rate * (1 + p.tva + p.tcc) " +
                "   WHEN c.cumulative_consumption <= p.tier_2_limit THEN (p.tier_1_limit * p.tier_1_rate + (c.monthly_consumption - p.tier_1_limit) * p.tier_2_rate) * (1 + p.tva + p.tcc) " +
                "   ELSE ((p.tier_1_limit * p.tier_1_rate) + (p.tier_2_limit - p.tier_1_limit) * p.tier_2_rate + (c.monthly_consumption - p.tier_2_limit) * p.tier_3_rate) * (1 + p.tva + p.tcc) " +
                "END AS total_cost_with_tax " +
                "FROM meter c " +
                "JOIN users u ON c.username = u.username " +
                "JOIN plan p ON u.plan_id = p.plan_id " +
                "WHERE u.username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);

        double totalAmount = 0.0;
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                totalAmount = resultSet.getDouble("total_cost_with_tax");
            }
        }
        preparedStatement.close();
        connection.close();
        return totalAmount;
    }

    private void insertIntoMeter(String username, double cumulativeConsumption, double monthlyConsumption, LocalDate consumptionDate) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Vérifier si l'utilisateur existe déjà dans la table meter
            String checkQuery = "SELECT meter_id FROM meter WHERE username = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, username);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    // Si l'utilisateur  existent déjà, mettre à jour les colonnes monthly_consumption et consumptionDate
                    int idMeter = resultSet.getInt("meter_id");
                    String updateQuery = "UPDATE meter SET monthly_consumption = ?,cumulative_consumption = ?, consumptionDate = ? WHERE meter_id = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setDouble(1, monthlyConsumption);
                        updateStatement.setDouble(2, cumulativeConsumption);
                        updateStatement.setDate(3, Date.valueOf(consumptionDate));
                        updateStatement.setInt(4, idMeter);
                        updateStatement.executeUpdate();
                    }
                } else {
                    // Si l'utilisateur et la date n'existent pas, insérer une nouvelle ligne
                    String insertQuery = "INSERT INTO meter (username, cumulative_consumption, monthly_consumption, consumptionDate) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setString(1, username);
                        insertStatement.setDouble(2, cumulativeConsumption);
                        insertStatement.setDouble(3, monthlyConsumption);
                        insertStatement.setDate(4, Date.valueOf(consumptionDate));
                        insertStatement.executeUpdate();
                    }
                }
            }

            // Insérer dans la table monthly_summary
            String monthYear = consumptionDate.getYear() + "-" + consumptionDate.getMonthValue();
            insertIntoMonthlySummary(monthYear, monthlyConsumption, username);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    private void insertIntoMonthlySummary(String monthYear, double totalConsumption,String username) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO monthly_summary (month_year, total_consumption,username) VALUES (?, ?,?)" +
                    "ON DUPLICATE KEY UPDATE total_consumption = total_consumption + VALUES(total_consumption)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, monthYear);
                preparedStatement.setDouble(2, totalConsumption);
                preparedStatement.setString(3, username);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    private void insertIntoBill(String username, double totalAmount) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            int idPlan = getIdPlanFromUsers(username);
            String insertQuery = "INSERT INTO bill ( plan_id, status, total_amount,username) VALUES (?, 'UNPAID',?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setInt(1, idPlan);
                insertStatement.setDouble(2, totalAmount);
                insertStatement.setString(3, username);
                insertStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }

    private int getIdPlanFromUsers(String username) {
        int idPlan = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String selectQuery = "SELECT plan_id FROM users WHERE username = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setString(1, username);
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        idPlan = resultSet.getInt("plan_id");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
        return idPlan;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    private boolean isMonthYearUniqueInMeter(String username, LocalDate consumptionDate) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT COUNT(*) FROM meter WHERE username = ? AND MONTH(consumptionDate) = ? AND YEAR(consumptionDate) = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, consumptionDate.getMonthValue());
                preparedStatement.setInt(3, consumptionDate.getYear());

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count == 0;  // Retourne vrai si le mois et l'année sont uniques
                }
            }
        }

        return false;
    }


    private void updateMessageLabel(String messageText) {
        message.setText(messageText);
    }
}