package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import model.Clock;
import model.Color;
import model.Game;
import ui.ClockView;
import java.util.function.Consumer;

public class ClockController {
    private final Game game;
    private final Clock clock;
    private final ClockView clockView;
    private Timeline timeline;
    private Consumer<Color> onTimeout;

    public ClockController(Game game, Clock clock, ClockView clockView) {
        this.game = game;
        this.clock = clock;
        this.clockView = clockView;
    }

    public void setOnTimeout(Consumer<Color> onTimeout) {
        this.onTimeout = onTimeout;
    }

    public void start() {
        clockView.update(clock);
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> tick()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        clock.start(Color.WHITE);
        timeline.play();
    }

    private void tick() {
        if (game.isGameOver() || game.isPromotionPending()) return;
        boolean timedOut = clock.tick();
        clockView.update(clock);

        if (timedOut) {
            Color colorThatRanOut = clock.getActiveColor();
            stop();
            if (onTimeout != null) onTimeout.accept(colorThatRanOut);
        }
    }

    public void switchTo(Color color) {
        clock.switchTo(color);
        clockView.update(clock);
    }

    public void stop() {
        if (timeline != null) timeline.stop();
    }
}