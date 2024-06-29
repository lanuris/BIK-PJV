module thedrake {

    //These lines specify dependencies on other modules.
    requires javafx.fxml;
    requires javafx.controls;

    //The opens directive is used to make the specified packages accessible at runtime for deep reflection.
    opens thedrake.ui;
    opens thedrake.ui.middleGameScreen;
    opens thedrake.ui.mainGameScreen;
    opens thedrake.ui.endGameScreen;

    //The exports directive makes the thedrake package available to other modules.
    exports thedrake;
}