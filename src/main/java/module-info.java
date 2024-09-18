module seproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.swing;

    opens seproject to javafx.fxml;
    exports seproject;
    opens seproject.controller to javafx.fxml;
    exports seproject.controller;
}
