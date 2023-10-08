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

    opens com.example.pbl4_remote_desktopclient to javafx.fxml;
    exports com.example.pbl4_remote_desktopclient;
}