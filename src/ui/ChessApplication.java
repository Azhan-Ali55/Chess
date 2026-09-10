package ui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import model.Color;
import model.Difficulty;
import model.Game;
import controller.GameController;


public class ChessApplication extends Application {
    private static final String STYLESHEET = "/styles/chess.css";

    @Override
    public void start(Stage stage) {
        Label title = new Label("Choose Difficulty");
        title.getStyleClass().add("title-label");

        VBox difficultyBox = new VBox(10);
        difficultyBox.setAlignment(Pos.CENTER);

        for (Difficulty difficulty : Difficulty.values()) {
            Button button = new Button(difficulty.name());
            button.setPrefWidth(180);
            button.setPrefHeight(40);
            button.setOnAction(event -> showColorSelection(stage, difficulty));
            difficultyBox.getChildren().add(button);
        }

        VBox root = new VBox(20, title, difficultyBox);
        root.setAlignment(Pos.CENTER);
        showScene(stage, root, 400, 400);
    }

    private void showColorSelection(Stage stage, Difficulty difficulty) {
        Label title = new Label("Choose Your Color");
        title.getStyleClass().add("title-label");
        Button whiteButton = new Button("White");
        whiteButton.setPrefWidth(180);
        whiteButton.setPrefHeight(40);
        whiteButton.setOnAction(event -> showTimeSelection(stage, difficulty, Color.WHITE));

        Button blackButton = new Button("Black");
        blackButton.setPrefWidth(180);
        blackButton.setPrefHeight(40);
        blackButton.setOnAction(event -> showTimeSelection(stage, difficulty, Color.BLACK));

        VBox colorBox = new VBox(10, whiteButton, blackButton);
        colorBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, title, colorBox);
        root.setAlignment(Pos.CENTER);

        showScene(stage, root, 400, 400);
    }

    private void showTimeSelection(Stage stage, Difficulty difficulty, Color humanColor) {
        Label title = new Label("Choose Time Control");
        title.getStyleClass().add("title-label");

        int[] options = {1, 3, 5, 10, 15, 30};
        VBox timeBox = new VBox(10);
        timeBox.setAlignment(Pos.CENTER);

        for (int minutes : options) {
            Button button = new Button(minutes + " min");
            button.setPrefWidth(180);
            button.setPrefHeight(40);
            button.setOnAction(event -> startGame(stage, difficulty, humanColor, minutes));
            timeBox.getChildren().add(button);
        }

        VBox root = new VBox(20, title, timeBox);
        root.setAlignment(Pos.CENTER);
        showScene(stage, root, 400, 400);
    }

    public void startGame(Stage stage, Difficulty difficulty, Color humanColor, int minutesPerSide) {
        Game game = new Game();
        ChessBoardView boardView = new ChessBoardView(game);
        PromotionView promotionView = new PromotionView();
        promotionView.setVisible(false);
        GameOverView gameOverView = new GameOverView();
        gameOverView.setVisible(false);
        ControlPanelView controlPanelView = new ControlPanelView();
        PlayerInfoView whitePanel = new PlayerInfoView("White");
        PlayerInfoView blackPanel = new PlayerInfoView("Black");

        StackPane boardStack = new StackPane();
        boardStack.getChildren().addAll(boardView, promotionView, gameOverView);
        StackPane.setAlignment(promotionView, Pos.TOP_LEFT);

        // Frame the board with padding + shadow so it doesn't float bare in a big window
        StackPane boardWrapper = new StackPane(boardStack);
        boardWrapper.getStyleClass().add("board-wrapper");
        boardWrapper.setMaxSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE);

        BorderPane root = new BorderPane();
        if (humanColor == Color.WHITE) {
            root.setTop(blackPanel);
            root.setBottom(whitePanel);
        } else {
            root.setTop(whitePanel);
            root.setBottom(blackPanel);
        }
        BorderPane.setAlignment(blackPanel, Pos.CENTER);
        BorderPane.setAlignment(whitePanel, Pos.CENTER);
        root.setCenter(boardWrapper);
        root.setRight(controlPanelView);

        new GameController(game, boardView, promotionView, gameOverView, difficulty, minutesPerSide,controlPanelView,
                whitePanel, blackPanel, humanColor);

        showScene(stage, root, 900, 900);
    }

    private void showScene(Stage stage, javafx.scene.Parent root, double width, double height) {
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(getClass().getResource(STYLESHEET).toExternalForm());
        stage.setTitle("Chess");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}