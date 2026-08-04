package model;

public class Game {
    private final Board board;
    private Color currentTurn;

    public Game() {
        board = new Board();
        currentTurn = Color.WHITE;
    }

    // Getters
    public Board getBoard() { return board; }
    public Color getCurrentTurn() { return currentTurn; }
}
