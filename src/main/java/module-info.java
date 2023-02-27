module com.ds.dscounter {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ds.dscounter to javafx.fxml;
    exports com.ds.dscounter;
}