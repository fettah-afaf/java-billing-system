package com.example.login;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;

public class Complaint {
    private final SimpleIntegerProperty id;
    private final SimpleObjectProperty<Timestamp> date;
    private final SimpleStringProperty details;

    public Complaint(int id, Timestamp date, String details) {
        this.id = new SimpleIntegerProperty(id);
        this.date = new SimpleObjectProperty<>(date);
        this.details = new SimpleStringProperty(details);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty getIdProperty() {
        return id;
    }

    public Timestamp getDate() {
        return date.get();
    }

    public SimpleObjectProperty<Timestamp> getDateProperty() {
        return date;
    }

    public String getDetails() {
        return details.get();
    }

    public SimpleStringProperty getDetailsProperty() {
        return details;
    }
}