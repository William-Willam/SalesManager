module br.com.salesmanager.desktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens br.com.salesmanager.desktop to javafx.fxml;
    opens br.com.salesmanager.desktop.dto to com.fasterxml.jackson.databind;
    opens br.com.salesmanager.desktop.controller to javafx.fxml;

    exports br.com.salesmanager.desktop;
}