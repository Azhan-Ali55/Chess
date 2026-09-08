package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import model.Clock;
import model.Color;

public class ClockView extends HBox {
    private final Label whiteLabel = new Label();
    private final Label blackLabel = new Label();

    public ClockView() {
        setSpacing(40);
        setAlignment(Pos.CENTER);
        whiteLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        blackLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        getChildren().addAll(whiteLabel, blackLabel);
    }

    public void update(Clock clock) {
        whiteLabel.setText("White: " + Clock.formatTime(clock.getWhiteSecondsLeft()));
        blackLabel.setText("Black: " + Clock.formatTime(clock.getBlackSecondsLeft()));

        // Highlight whichever clock is currently running
        whiteLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;" +
                (clock.getActiveColor() == Color.WHITE && clock.isRunning() ? " -fx-text-fill: green;" : ""));
        blackLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;" +
                (clock.getActiveColor() == Color.BLACK && clock.isRunning() ? " -fx-text-fill: green;" : ""));
    }
}