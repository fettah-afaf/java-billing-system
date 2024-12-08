package com.example.login;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;

public class Bill {
    private SimpleIntegerProperty idBill;
    private SimpleObjectProperty<Timestamp> dateCreated;
    private SimpleIntegerProperty idPlan;
    private SimpleStringProperty status;
    private SimpleDoubleProperty totalAmount;

    // Constructors
    public Bill() {
        this.idBill = new SimpleIntegerProperty();
        this.dateCreated = new SimpleObjectProperty<>();
        this.idPlan = new SimpleIntegerProperty();
        this.status = new SimpleStringProperty();
        this.totalAmount = new SimpleDoubleProperty();
    }

    public Bill(int idBill, Timestamp dateCreated, int idPlan, String status, double totalAmount) {
        this.idBill = new SimpleIntegerProperty(idBill);
        this.dateCreated = new SimpleObjectProperty<>(dateCreated);
        this.idPlan = new SimpleIntegerProperty(idPlan);
        this.status = new SimpleStringProperty(status);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
    }

    // Getter and Setter methods
    public int getIdBill() {
        return idBill.get();
    }

    public SimpleIntegerProperty idBillProperty() {
        return idBill;
    }

    public void setIdBill(int idBill) {
        this.idBill.set(idBill);
    }

    public Timestamp getDateCreated() {
        return dateCreated.get();
    }

    public SimpleObjectProperty<Timestamp> dateCreatedProperty() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public int getIdPlan() {
        return idPlan.get();
    }

    public SimpleIntegerProperty idPlanProperty() {
        return idPlan;
    }

    public void setIdPlan(int idPlan) {
        this.idPlan.set(idPlan);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public double getTotalAmount() {
        return totalAmount.get();
    }

    public SimpleDoubleProperty totalAmountProperty() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount.set(totalAmount);
    }
}
