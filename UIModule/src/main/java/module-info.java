module pezhi.ui {
    requires pezhi.core;

    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;

    opens controller to javafx.fxml;

    exports application;
}