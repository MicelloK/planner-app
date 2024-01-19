module dev.micellok.gui {
    requires javafx.controls;
    requires javafx.fxml;


    opens dev.micellok.gui to javafx.fxml;
    exports dev.micellok.gui;
}