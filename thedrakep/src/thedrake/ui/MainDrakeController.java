package thedrake.ui;


import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import thedrake.*;
import thedrake.ui.endGameScreen.EndScreenController;
import thedrake.ui.middleGameScreen.MiddleGameView;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class MainDrakeController implements Initializable {

    private Stage primaryStage;

    private Scene mainGameScene;
    private Scene endGameScene;

    private EndScreenController endScreenController;
    private MiddleGameView middleGameView;


    public void initialize(URL location, ResourceBundle resources) {
    }

    public MainDrakeController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    public void setMiddleScene() {
        middleGameView = new MiddleGameView(createSampleGameState());
        Scene middleGameScene = new Scene(middleGameView);
        primaryStage.setScene(middleGameScene);


//        Line line = new Line(0, 0, middleGameView.getWidth(), 0);
//        line.setStroke(Color.BLUE);
//        line.setStrokeWidth(2);
//
//        // Position the line at the bottom of the screen
//        line.setLayoutY(middleGameView.getHeight() - 2);
//
//        // Add the line to the root pane
//        middleGameView.getChildren().add(line);
//
//        // Create a timeline animation to periodically adjust the opacity of the line
//        Timeline timeline = new Timeline(
//                new KeyFrame(Duration.seconds(0), e -> line.setOpacity(1)),
//                new KeyFrame(Duration.seconds(1), e -> line.setOpacity(0.5))
//        );
//        timeline.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
//        timeline.play();
    }

    public void setStateListener() {

        GameResult.getGameResult().addListener((observable, oldValue, newValue) -> {
            System.out.println("Game result changed: " + oldValue + " -> " + newValue);

            switch (newValue) {
                case IN_PLAY:
                    setMiddleScene();
                    break;

                case VICTORY:
                    endScreenController.setWinnerLabel("Winner: "
                            + middleGameView.getBoardView().getGameState().armyNotOnTurn().side() + "!");

                    primaryStage.setScene(this.endGameScene);
                    break;

                case DRAW:
                    endScreenController.setWinnerLabel("Draw!");
                    primaryStage.setScene(this.endGameScene);
                    break;

                case MAIN_MENU:
                    primaryStage.setScene(this.mainGameScene);
                    break;

            }
        });
    }

    public void loadAndSetMainGameScreen () throws IOException {

        //begining
        //or getClass()
        FXMLLoader fxmlLoader = new FXMLLoader(TheDrakeApp.class.getResource("/thedrake/ui/mainGameScreen/mainScreen.fxml"));
        mainGameScene = new Scene(fxmlLoader.load(), 720, 405);

        GameResult.getGameResult().set(GameResult.MAIN_MENU);
        primaryStage.setTitle("The Drake");
        primaryStage.setScene(mainGameScene);
        primaryStage.show();

    }
    public void loadEndGameScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/thedrake/ui/endGameScreen/endScreen.fxml"));
        //AnchorPane endGameView = fxmlLoader.load();
        endGameScene = new Scene(fxmlLoader.load(), 720, 405);
        endScreenController = fxmlLoader.getController();
    }

    private static GameState createSampleGameState() {
        Random random = new Random();
        Board board = new Board(4);
        PositionFactory positionFactory = board.positionFactory();
//        board = board.withTiles(new Board.TileAt(positionFactory.pos(0, 1), BoardTile.MOUNTAIN));
        board = board.withTiles(new Board.TileAt(positionFactory.pos(random.nextInt(4), random.nextInt(1,3)), BoardTile.MOUNTAIN),
                new Board.TileAt(positionFactory.pos(random.nextInt(4), random.nextInt(1,3)), BoardTile.MOUNTAIN));
        return new StandardDrakeSetup().startState(board);
    }

    //    public static GameState createSampleGameState() {
//        Board board = new Board(4);
//        PositionFactory positionFactory = board.positionFactory();
//        board = board.withTiles(new Board.TileAt(positionFactory.pos(1, 1), BoardTile.MOUNTAIN));
//        return new StandardDrakeSetup().startState(board)
//            .placeFromStack(positionFactory.pos(0, 0))
//            .placeFromStack(positionFactory.pos(3, 3))
//            .placeFromStack(positionFactory.pos(0, 1))
//            .placeFromStack(positionFactory.pos(3, 2))
//            .placeFromStack(positionFactory.pos(1, 0))
//            .placeFromStack(positionFactory.pos(2, 3));
//    }
}
