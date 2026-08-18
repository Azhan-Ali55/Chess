package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import model.Game;
import controller.GameController;

public class ChessApplication extends Application {
    @Override
    public void start(Stage stage) {
        Game game = new Game();
        ChessBoardView boardView = new ChessBoardView(game);
        PromotionView promotionView = new PromotionView();
        promotionView.setVisible(false);
        GameOverView gameOverView = new GameOverView();
        gameOverView.setVisible(false);
        StackPane root = new StackPane();
        root.getChildren().addAll(boardView, promotionView, gameOverView);
        StackPane.setAlignment(promotionView, javafx.geometry.Pos.TOP_LEFT);
        new GameController(game, boardView, promotionView, gameOverView);
        Scene scene = new Scene(root);
        stage.setTitle("Chess");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}