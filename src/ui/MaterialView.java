package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class MaterialView extends HBox {
    private final Label label = new Label();

    public MaterialView() {
        setAlignment(Pos.CENTER);
        label.setStyle("-fx-font-size: 16px;");
        getChildren().add(label);
        update(0);
    }

    // balance: positive = White ahead, negative = Black ahead
    public void update(int balance) {
        if (balance == 0) {
            label.setText("Material: even");
        } else if (balance > 0) {
            label.setText("White +" + balance);
        } else {
            label.setText("Black +" + (-balance));
        }
    }
}