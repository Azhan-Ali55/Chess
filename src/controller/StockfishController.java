package controller;

import javafx.concurrent.Task;
import model.Difficulty;
import model.Game;
import model.Move;
import util.FenConverter;
import util.StockfishEngine;
import java.io.IOException;
import java.util.function.Consumer;

public class StockfishController {
    private final Game game;
    private final StockfishEngine stockfish;
    private final Difficulty difficulty;
    private final int thinkTimeMs;

    public StockfishController(Game game, Difficulty difficulty, int thinkTimeMs) {
        this.game = game;
        this.difficulty = difficulty;
        this.thinkTimeMs = thinkTimeMs;
        this.stockfish = new StockfishEngine();

        try {
            stockfish.start();
            stockfish.setDifficulty(difficulty);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Requests move from stockfish
    public void requestMove(Consumer<Move> onMoveReady) {
        String fen = FenConverter.boardToFen(game);

        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                return stockfish.getBestMove(fen, thinkTimeMs, difficulty.getBlunderChance());
            }
        };

        task.setOnSucceeded(event -> {
            String bestMove = task.getValue();
            System.out.println("Stockfish best move: " + bestMove);
            onMoveReady.accept(stockfish.convertToMove(bestMove));
        });

        task.setOnFailed(event -> {
            System.out.println("Stockfish failed.");
            task.getException().printStackTrace();
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}