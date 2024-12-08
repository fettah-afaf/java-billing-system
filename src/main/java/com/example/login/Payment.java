package com.example.login;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.Date;
import java.sql.Timestamp;

public class Payment {
    private SimpleIntegerProperty idPayment;
    private SimpleObjectProperty<Timestamp> date;
    private SimpleIntegerProperty idBill;
    private SimpleDoubleProperty amountPaid;

    // Constructors

    public Payment(int idPayment, Timestamp date, int idBill, double amountPaid) {
        this.idPayment = new SimpleIntegerProperty(idPayment);
        this.date = new SimpleObjectProperty<>(date);
        this.idBill = new SimpleIntegerProperty(idBill);
        this.amountPaid = new SimpleDoubleProperty(amountPaid);
    }

    // Getter and Setter methods
    public int getIdPayment() {
        return idPayment.get();
    }

    public SimpleIntegerProperty idPaymentProperty() {
        return idPayment;
    }

    public void setIdPayment(int idPayment) {
        this.idPayment.set(idPayment);
    }

    public Timestamp getDate() {
        return date.get();
    }

    public SimpleObjectProperty<Timestamp> dateProperty() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date.set(date);
    }

    public int getIdBill() {
        return idBill.get();
    }

    public SimpleIntegerProperty idBillProperty() {
        return idBill;
    }

    public void setIdBill(int idBill) {
        this.idBill.set(idBill);
    }

    public double getAmountPaid() {
        return amountPaid.get();
    }

    public SimpleDoubleProperty amountPaidProperty() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid.set(amountPaid);
    }
}