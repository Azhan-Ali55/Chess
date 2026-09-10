package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import model.Clock;
import model.Color;
import model.Game;
import ui.PlayerInfoView;
import java.util.function.Consumer;

public class ClockController {
    private final Game game;
    private final Clock clock;
    private final PlayerInfoView whitePanel;
    private final PlayerInfoView blackPanel;
    private Timeline timeline;
    private Consumer<Color> onTimeout;

    public ClockController(Game game, Clock clock, PlayerInfoView whitePanel, PlayerInfoView blackPanel) {
        this.game = game;
        this.clock = clock;
        this.whitePanel = whitePanel;
        this.blackPanel = blackPanel;
    }

    public void setOnTimeout(Consumer<Color> onTimeout) {
        this.onTimeout = onTimeout;
    }

    public void start() {
        updatePanels();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> tick()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        clock.start(Color.WHITE);
        timeline.play();
    }

    private void tick() {
        if (game.isGameOver() || game.isPromotionPending()) return;
        boolean timedOut = clock.tick();
        updatePanels();

        if (timedOut) {
            Color colorThatRanOut = clock.getActiveColor();
            stop();
            if (onTimeout != null) onTimeout.accept(colorThatRanOut);
        }
    }

    private void updatePanels() {
        whitePanel.setClockText(Clock.formatTime(clock.getWhiteSecondsLeft()),
                clock.getActiveColor() == Color.WHITE && clock.isRunning());
        blackPanel.setClockText(Clock.formatTime(clock.getBlackSecondsLeft()),
                clock.getActiveColor() == Color.BLACK && clock.isRunning());
    }

    public void switchTo(Color color) {
        clock.switchTo(color);
        updatePanels();
    }

    public void stop() {
        if (timeline != null) timeline.stop();
    }
}