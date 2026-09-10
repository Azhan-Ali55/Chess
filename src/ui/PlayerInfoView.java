package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class PlayerInfoView extends HBox {
    private final Label nameLabel;
    private final Label clockLabel;
    private final Label materialLabel;

    public PlayerInfoView(String displayName) {
        getStyleClass().add("player-panel");
        setSpacing(20);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(10));

        nameLabel = new Label(displayName);
        nameLabel.getStyleClass().add("player-name");

        clockLabel = new Label("0:00");
        clockLabel.getStyleClass().add("player-clock");

        materialLabel = new Label("");
        materialLabel.getStyleClass().add("player-material");

        getChildren().addAll(nameLabel, clockLabel, materialLabel);
    }

    public void setClockText(String text, boolean active) {
        clockLabel.setText(text);
        clockLabel.getStyleClass().removeAll("player-clock", "player-clock-active");
        clockLabel.getStyleClass().add(active ? "player-clock-active" : "player-clock");
    }

    public void setMaterialAdvantage(int advantage) {
        materialLabel.setText(advantage > 0 ? "+" + advantage : "");
    }
}