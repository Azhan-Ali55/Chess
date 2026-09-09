package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ControlPanelView extends VBox {
    private final Button resignButton = new Button("Resign");
    private final Button offerDrawButton = new Button("Offer Draw");

    public ControlPanelView() {
        setSpacing(10);
        setAlignment(Pos.CENTER);
        resignButton.setPrefWidth(140);
        offerDrawButton.setPrefWidth(140);
        getChildren().addAll(resignButton, offerDrawButton);
    }

    public Button getResignButton() { return resignButton; }
    public Button getOfferDrawButton() { return offerDrawButton; }

    public void setButtonDisabled(boolean disabled) {
        resignButton.setDisable(disabled);
        offerDrawButton.setDisable(disabled);
    }
}