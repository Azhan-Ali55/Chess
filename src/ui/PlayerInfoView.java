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
        setSpacing(20);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(10));

        nameLabel = new Label(displayName);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        clockLabel = new Label("0:00");
        clockLabel.setStyle("-fx-font-size: 20px; -fx-font-family: 'Consolas';");

        materialLabel = new Label("");
        materialLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #2e7d32;");

        getChildren().addAll(nameLabel, clockLabel, materialLabel);
    }

    public void setClockText(String text, boolean active) {
        clockLabel.setText(text);
        String base = "-fx-font-size: 20px; -fx-font-family: 'Consolas';";
        clockLabel.setStyle(active ? base + " -fx-text-fill: #2e7d32; -fx-font-weight: bold;" : base);
    }

    public void setMaterialAdvantage(int advantage) {
        materialLabel.setText(advantage > 0 ? "+" + advantage : "");
    }
}