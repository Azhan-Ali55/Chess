package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.Difficulty;

import java.util.function.Consumer;

public class DifficultyView extends VBox {
    private final Consumer<Difficulty> onDifficultySelected;

    public DifficultyView(Consumer<Difficulty> onDifficultySelected) {
        this.onDifficultySelected = onDifficultySelected;
        setSpacing(15);
        setAlignment(Pos.CENTER);
        Label title = new Label("Select Difficulty");
        Button beginnerButton = new Button("Beginner");
        Button intermediateButton = new Button("Intermediate");
        Button advancedButton = new Button("Advanced");
        Button expertButton = new Button("Expert");
        Button grandmasterButton = new Button("Grandmaster");

        beginnerButton.setOnAction(event ->
                selectDifficulty(Difficulty.BEGINNER));

        intermediateButton.setOnAction(event ->
                selectDifficulty(Difficulty.INTERMEDIATE));

        advancedButton.setOnAction(event ->
                selectDifficulty(Difficulty.ADVANCED));

        expertButton.setOnAction(event ->
                selectDifficulty(Difficulty.EXPERT));

        grandmasterButton.setOnAction(event ->
                selectDifficulty(Difficulty.GRANDMASTER));

        getChildren().addAll(title, beginnerButton, intermediateButton, advancedButton, expertButton, grandmasterButton);
    }

    private void selectDifficulty(Difficulty difficulty) {
        onDifficultySelected.accept(difficulty);
    }
}