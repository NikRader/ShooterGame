module com.example.shootergame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.shootergame to javafx.fxml;
    exports com.example.shootergame;
}