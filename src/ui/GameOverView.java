package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.Color;

public class GameOverView extends VBox {
    private final Label resultLabel;
    private final Button newGameButton;

    public GameOverView() {
        setSpacing(15);
        setAlignment(Pos.CENTER);
        resultLabel = new Label();
        newGameButton = new Button("New Game");
        getChildren().addAll(resultLabel, newGameButton);
    }

    public void showCheckmate(Color winner) {
        resultLabel.setText("Checkmate — " + winner + " wins!");
        setVisible(true);
    }

    public void showStalemate() {
        resultLabel.setText("Stalemate — Draw");
        setVisible(true);
    }

    public Button getNewGameButton() {
        return newGameButton;
    }
}