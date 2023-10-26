module com.example.pbl4_remote_desktopclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
//    requires eu.hansolo.tilesfx;
    requires java.logging;
    requires java.desktop;

    opens Client_Session to javafx.fxml;
    exports Client_Session;
    exports Sub_Server_Session;
    opens Sub_Server_Session to javafx.fxml;
}