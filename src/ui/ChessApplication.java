package ui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
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
                startGame(stage, difficulty);
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

    public void startGame(Stage stage, Difficulty difficulty) {
        Game game = new Game();
        ChessBoardView boardView = new ChessBoardView(game);
        PromotionView promotionView = new PromotionView();
        promotionView.setVisible(false);
        GameOverView gameOverView = new GameOverView();
        gameOverView.setVisible(false);
        StackPane root = new StackPane();
        root.getChildren().addAll(boardView, promotionView, gameOverView);
        StackPane.setAlignment(promotionView, javafx.geometry.Pos.TOP_LEFT);
        new GameController(game, boardView, promotionView, gameOverView, difficulty);
        Scene scene = new Scene(root);
        stage.setTitle("Chess");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}