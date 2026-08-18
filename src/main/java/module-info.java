module com.example.crud {
    requires java.sql;
    requires org.mariadb.jdbc;
    requires javafx.controls;
    requires javafx.fxml;

    exports com.crud.GUi;

    opens com.crud.GUi to javafx.fxml;
    opens com.example.crud to javafx.fxml;
    opens com.crud.Logica to javafx.base;
}