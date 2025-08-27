module org.example.hangman {
    requires javafx.controls;
    requires javafx.fxml;
//    requires jdk.unsupported.desktop;


    opens org.example.hangman to javafx.fxml;
    exports org.example.hangman;
}