package com.example.login;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MonthlyConsumptionStatistics implements Initializable {

    // Informations de connexion à la base de données (à adapter)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/facturation";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private ObservableList<XYChart.Data<String, Number>> dataList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataList = FXCollections.observableArrayList();
        getDataFromDatabase();
        configureChart();
    }

    private void getDataFromDatabase() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Modification de la requête pour obtenir la somme des total_consumption par mois_year
            String sql = "SELECT month_year, SUM(total_consumption) AS total_consumption FROM monthly_summary GROUP BY month_year";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String monthYear = resultSet.getString("month_year");
                double totalConsumption = resultSet.getDouble("total_consumption");

                // Ajout des données à la liste observable
                dataList.add(new XYChart.Data<>(monthYear, totalConsumption));
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    private void configureChart() {
        xAxis.setLabel("Mois");
        yAxis.setLabel("Consommation totale");

        XYChart.Series<String, Number> series = new XYChart.Series<>(dataList);
        barChart.getData().add(series);
        series.getData().forEach(data -> {
            data.getNode().setStyle("-fx-bar-fill: #ff7171;");
        });
    }
}