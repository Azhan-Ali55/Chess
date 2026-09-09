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
    @Override
    public void start(Stage stage) {
        Label title = new Label("Choose Difficulty");

        VBox difficultyBox = new VBox(10);
        difficultyBox.setAlignment(Pos.CENTER);

        for (Difficulty difficulty : Difficulty.values()) {
            Button button = new Button(difficulty.name());
            button.setPrefWidth(180);
            button.setPrefHeight(40);
            button.setOnAction(event -> {
                showColorSelection(stage, difficulty);;
            });

            difficultyBox.getChildren().add(button);
        }

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, difficultyBox);

        Scene scene = new Scene(root, 400, 400);
        stage.setTitle("Chess");
        stage.setScene(scene);
        stage.show();
    }

    private void showColorSelection(Stage stage, Difficulty difficulty) {
        Label title = new Label("Choose Your Color");

        Button whiteButton = new Button("White");
        whiteButton.setPrefWidth(180);
        whiteButton.setPrefHeight(40);
        whiteButton.setOnAction(event -> startGame(stage, difficulty, Color.WHITE));
        Button blackButton = new Button("Black");
        blackButton.setPrefWidth(180);
        blackButton.setPrefHeight(40);
        blackButton.setOnAction(event -> startGame(stage, difficulty, Color.BLACK));

        VBox colorBox = new VBox(10, whiteButton, blackButton);
        colorBox.setAlignment(Pos.CENTER);
        VBox root = new VBox(20, title, colorBox);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 400, 400);
        stage.setTitle("Chess");
        stage.setScene(scene);
        stage.show();
    }

    public void startGame(Stage stage, Difficulty difficulty, Color humanColor) {
        Game game = new Game();
        ChessBoardView boardView = new ChessBoardView(game);
        PromotionView promotionView = new PromotionView();
        promotionView.setVisible(false);
        GameOverView gameOverView = new GameOverView();
        gameOverView.setVisible(false);
        ClockView clockView = new ClockView();
        ControlPanelView controlPanelView = new ControlPanelView();
        int minutesPerSide = 1;
        StackPane boardStack = new StackPane();
        boardStack.getChildren().addAll(boardView, promotionView, gameOverView);
        StackPane.setAlignment(promotionView, Pos.TOP_LEFT);

        // Clock sits above the board, not overlapping it
        BorderPane root = new BorderPane();
        root.setTop(clockView);
        root.setCenter(boardStack);
        root.setRight(controlPanelView);
        BorderPane.setAlignment(clockView, Pos.CENTER);
        BorderPane.setAlignment(controlPanelView, Pos.CENTER);

        new GameController(game, boardView, promotionView, gameOverView, clockView,
                difficulty, minutesPerSide, controlPanelView, humanColor);
        Scene scene = new Scene(root);
        stage.setTitle("Chess");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}