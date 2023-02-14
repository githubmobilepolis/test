module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}