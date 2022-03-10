module com.example.map223rebecadomocosmaragheorghe {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;
    requires org.apache.pdfbox;

    opens com.example.map223rebecadomocosmaragheorghe to javafx.fxml;
    opens com.example.map223rebecadomocosmaragheorghe.service.DTO to javafx.fxml;
    exports com.example.map223rebecadomocosmaragheorghe;
    exports com.example.map223rebecadomocosmaragheorghe.controller;
    exports com.example.map223rebecadomocosmaragheorghe.service.DTO;
    opens com.example.map223rebecadomocosmaragheorghe.controller to javafx.fxml;
    exports com.example.map223rebecadomocosmaragheorghe.controller.cellFactory;
    opens com.example.map223rebecadomocosmaragheorghe.controller.cellFactory to javafx.fxml;

    exports com.example.map223rebecadomocosmaragheorghe.utils.design;
    opens com.example.map223rebecadomocosmaragheorghe.utils.design to javafx.fxml;

}