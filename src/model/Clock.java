package model;

public class Clock {
    private int whiteSecondsLeft;
    private int blackSecondsLeft;
    private Color activeColor;
    private boolean running = false;

    public Clock(int startingMinutes) {
        this.whiteSecondsLeft = startingMinutes * 60;
        this.blackSecondsLeft = startingMinutes * 60;
        this.activeColor = Color.WHITE;
    }

    public void start(Color color) {
        this.activeColor = color;
        this.running = true;
    }

    public void stop() {
        this.running = false;
    }

    public void switchTo(Color color) {
        this.activeColor = color;
    }

    // Called once per second while running. Returns true if the active player just ran out of time
    public boolean tick() {
        if (!running) return false;

        if (activeColor == Color.WHITE) {
            whiteSecondsLeft--;
            if (whiteSecondsLeft <= 0) {
                whiteSecondsLeft = 0;
                running = false;
                return true;
            }
        } else {
            blackSecondsLeft--;
            if (blackSecondsLeft <= 0) {
                blackSecondsLeft = 0;
                running = false;
                return true;
            }
        }
        return false;
    }

    public int getWhiteSecondsLeft() { return whiteSecondsLeft; }
    public int getBlackSecondsLeft() { return blackSecondsLeft; }
    public Color getActiveColor() { return activeColor; }
    public boolean isRunning() { return running; }

    public static String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}