package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.Color;

public class GameOverView extends StackPane {
    private final Label resultLabel;
    private final Button newGameButton;

    public GameOverView() {
        getStyleClass().add("game-over-overlay");
        setAlignment(Pos.CENTER);

        resultLabel = new Label();
        resultLabel.getStyleClass().add("result-label");

        newGameButton = new Button("New Game");
        newGameButton.setPrefWidth(160);

        VBox card = new VBox(resultLabel, newGameButton);
        card.getStyleClass().add("game-over-card");
        card.setAlignment(Pos.CENTER);

        getChildren().add(card);
    }

    public void showCheckmate(Color winner) {
        resultLabel.setText("Checkmate — " + winner + " wins!");
        setVisible(true);
    }

    public void showStalemate() {
        resultLabel.setText("Stalemate — Draw");
        setVisible(true);
    }

    public void showResignation(Color winner) {
        resultLabel.setText(winner + " wins by resignation");
        setVisible(true);
    }

    public void showDraw() {
        resultLabel.setText("Draw agreed");
        setVisible(true);
    }

    public Button getNewGameButton() {
        return newGameButton;
    }
}