package com.example.login;





import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;



import java.net.URL;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;

import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class choice implements Initializable {


    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    // Store the id_bill value
    public int idBill;
    public void setIdBill(int idBill) {
        this.idBill = idBill;
        idLabel.setText(String.valueOf(idBill));
    }
    public void setData(Bill selectedBill) {
        idLabel.setText(String.valueOf(selectedBill.getIdBill()));
        dateLabel.setText(selectedBill.getDateCreated().toString()); // Assuming DateCreated is a Timestamp
        totalAmountLabel.setText(String.valueOf(selectedBill.getTotalAmount()));

    }



    @FXML
    private Label dateLabel;

    @FXML
    private Label idLabel;

    @FXML
    private Label messageprint;

    @FXML
    private Button printbutton;

    @FXML
    private Button savepdf;

    @FXML
    private Label totalAmountLabel;




    @FXML
    void printOnAction(ActionEvent event) {
        // Declare the variable outside the try block
        try {
            System.out.println("Before loading JRXML");
            JasperDesign reportStream = JRXmlLoader.load("src/main/resources/com/example/login/bill.jrxml");
            System.out.println("After loading JRXML");
            System.out.println("aaaaa");


            try (Connection conn = db.getConnection()) {
                System.out.println("aaaaa3");

                String sqlQuery = "SELECT B.id_bill, B.created_at, B.plan_id, B.status, B.total_amount, " +
                        "U.first_name, U.last_name, U.cin, B.username, P.name_plan " +
                        "FROM bill B, plan P, users U " +
                        "WHERE U.username = B.username AND P.plan_id = B.plan_id AND B.id_bill = ?";

// ... (inside the try block)
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
                    pstmt.setInt(1, idBill);



                System.out.println("SQL Query: " + sqlQuery);


                JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
                System.out.println("aaaaa9999");
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("idBill", idBill);
                ResultSet resultSet = pstmt.executeQuery();
                JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(resultSet);


                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, resultSetDataSource);

                System.out.println("aaaaa5");
                System.out.println("Number of pages in JasperPrint: " + jasperPrint.getPages().size());

                if (jasperPrint != null) {
                    JasperViewer.viewReport(jasperPrint, false);
                } else {
                    System.out.println("JasperPrint is null. Check for errors in the console output.");
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception appropriately
                System.out.println("Failed to fill the report. Check database connection and query.");
            }



        } catch (JRException e) {
            e.printStackTrace(); // Handle the exception appropriately
            System.out.println("Failed to generate JasperPrint. Check for errors in the console output.");
        }
    }








    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void cancelimageOnMouseClicked(MouseEvent event) {
        System.out.println("Cancel image clicked");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }









}
