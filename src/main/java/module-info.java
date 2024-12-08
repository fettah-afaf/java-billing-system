module com.example.electricity_billing_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires mysql.connector.j;

    requires java.desktop;

    requires jasperreports;


    opens com.example.login to javafx.fxml;
    exports com.example.login;
}