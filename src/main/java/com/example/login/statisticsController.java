package com.example.login;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class statisticsController implements Initializable {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    private LineChart<String, Number> chart; // Corrected the type

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("statics");
        setUsername(UserModel.getUsername());
        System.out.println(username);
        // Create X and Y axes


        NumberAxis chartYAxis = (NumberAxis) chart.getYAxis();
        chartYAxis.setTickUnit(50);
        chartYAxis.setLowerBound(0);

        chart.setTitle("Monthly Consumption Chart");
        System.out.println("statics");

        // Retrieve data from the database for the specific username ('afaf' in this case)
        XYChart.Series<String, Number> series = fetchDataFromDatabase(username);

        System.out.println("statics");
        

        // Add data series to the chart
        chart.getData().add(series);
        System.out.println("statics");
    }

    private XYChart.Series<String, Number> fetchDataFromDatabase(String username) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        try (Connection connection = db.getConnection()) {
            String query = "SELECT month_year, total_consumption FROM monthly_summary WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String monthYear = resultSet.getString("month_year");
                    double totalConsumption = resultSet.getDouble("total_consumption");
                    series.getData().add(new XYChart.Data<>(monthYear, totalConsumption));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return series;
    }


}
