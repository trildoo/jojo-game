module com.example.game2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.game2 to javafx.fxml;
    exports com.example.game2;
}