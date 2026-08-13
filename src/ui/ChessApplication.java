package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Game;

public class ChessApplication extends Application {
    @Override
    public void start(Stage stage) {
        Game game = new Game();
        ChessBoardView boardView = new ChessBoardView(game);
        Scene scene = new Scene(boardView);
        stage.setTitle("Chess");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}