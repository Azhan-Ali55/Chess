package controller;

import model.*;
import ui.*;
import util.SoundManager;

public class GameController {
    private final Game game;
    private final ChessBoardView boardView;
    private final PromotionView promotionView;
    private final GameOverView gameOverView;
    private final ControlPanelView controlPanelView;
    private final MaterialView materialView;
    private final Color stockfishColor;
    private final Color humanColor;
    private final int stockfishThinkTime = 3000;
    private final HighlightController highlightController;
    private final BoardInputController boardInputController;
    private final StockfishController stockfishController;
    private final ClockController clockController;

    public GameController(Game game, ChessBoardView boardView, PromotionView promotionView, GameOverView gameOverView,
                          ClockView clockView, Difficulty difficulty, int minutesPerSide, ControlPanelView controlPanelView,
                          MaterialView materialView, Color humanColor) {
        this.game = game;
        this.boardView = boardView;
        this.promotionView = promotionView;
        this.gameOverView = gameOverView;
        this.controlPanelView = controlPanelView;
        this.materialView = materialView;
        this.humanColor = humanColor;
        this.stockfishColor = (humanColor == Color.WHITE) ? Color.BLACK : Color.WHITE;

        highlightController = new HighlightController(game, boardView);
        stockfishController = new StockfishController(game, difficulty, stockfishThinkTime);
        Clock clock = new Clock(minutesPerSide);
        clockController = new ClockController(game, clock, clockView);
        clockController.setOnTimeout(this::handleTimeout);
        boardInputController = new BoardInputController(game, boardView, highlightController);
        boardInputController.setOnMoveAttempted(this::attemptMove);
        boardInputController.setup();

        setupPromotionHandlers();
        setupControlButtons();
        clockController.start();

        // White always moves first
        if (game.getCurrentTurn() == stockfishColor) requestStockfishMove();
    }

    private boolean attemptMove(Move move, boolean isCapture) {
        boolean successful = game.makeMove(move);
        if (successful) handleSuccessfulMove(isCapture);
        return successful;
    }

    private void setupPromotionHandlers() {
        promotionView.getQueenButton().setOnAction(event -> promote('Q'));
        promotionView.getRookButton().setOnAction(event -> promote('R'));
        promotionView.getBishopButton().setOnAction(event -> promote('B'));
        promotionView.getKnightButton().setOnAction(event -> promote('N'));
    }

    private void setupControlButtons() {
        controlPanelView.getResignButton().setOnAction(event -> handleResign());
        controlPanelView.getOfferDrawButton().setOnAction(event -> handleOfferDraw());
    }

    private void promote(char choice) {
        game.promotePawn(choice);
        promotionView.setVisible(false);
        refreshBoard();
        highlightController.highlightLastMove();

        if (game.isGameOver()) {
            if (game.isCurrentPlayerInCheck()) highlightController.highlightKingInCheck();
            handleGameOver();
            return;
        }

        if (game.isCurrentPlayerInCheck()) {
            SoundManager.playCheckSound();
        } else {
            SoundManager.playCaptureSound();
        }

        clockController.switchTo(game.getCurrentTurn());

        if (game.getCurrentTurn() == stockfishColor) requestStockfishMove();
    }

    private void handleSuccessfulMove(boolean isCapture) {
        System.out.println("Move successful");
        highlightController.clearMoveHighlights();

        if (game.isPromotionPending()) {
            promotionView.setColor(game.getPromotionColor());
            promotionView.positionAt(game.getPromotionRow(), game.getPromotionCol());
            promotionView.setVisible(true);
            refreshBoard();
            highlightController.highlightLastMove();
            return;
        }

        if (game.isGameOver()) {
            refreshBoard();
            highlightController.highlightLastMove();
            if (game.isCurrentPlayerInCheck()) highlightController.highlightKingInCheck();
            handleGameOver();
            return;
        }

        if (game.isCurrentPlayerInCheck()) {
            SoundManager.playCheckSound();
        } else if (isCapture) {
            SoundManager.playCaptureSound();
        } else {
            SoundManager.playMoveSound();
        }

        refreshBoard();
        highlightController.highlightLastMove();
        if (game.isCurrentPlayerInCheck()) highlightController.highlightKingInCheck();

        clockController.switchTo(game.getCurrentTurn());

        if (game.getCurrentTurn() == stockfishColor) requestStockfishMove();
    }

    private void requestStockfishMove() {
        stockfishController.requestMove(move -> {
            boolean isCapture = game.isCapture(move);
            boolean successful = game.makeMove(move);

            if (successful) {
                handleSuccessfulMove(isCapture);
            } else {
                System.out.println("Stockfish returned an illegal move!");
            }
        });
    }

    private void handleTimeout(Color colorThatRanOut) {
        game.loseOnTime(colorThatRanOut);
        refreshBoard();
        handleGameOver();
    }

    private void handleResign() {
        if (game.isGameOver() || game.isPromotionPending()) return;
        game.resign(humanColor);
        refreshBoard();
        handleGameOver();
    }

    private void handleOfferDraw() {
        if (game.isGameOver() || game.isPromotionPending()) return;

        int balance = game.getMaterialBalance();
        int balanceFromHumanPerspective = (humanColor == Color.WHITE) ? balance : -balance;
        boolean accepted = Math.abs(balanceFromHumanPerspective) <= 2;

        if (accepted) {
            game.declareDraw();
            refreshBoard();
            handleGameOver();
        } else {
            System.out.println("Stockfish declines the draw offer.");
        }
    }

    private void handleGameOver() {
        clockController.stop();
        controlPanelView.setButtonDisabled(true);

        if (game.isResigned()) {
            gameOverView.showResignation(game.getWinner());
        } else if (game.isDrawnGame()) {
            gameOverView.showDraw();
        } else if (game.getWinner() != null) {
            SoundManager.playCheckmateSound();
            gameOverView.showCheckmate(game.getWinner());
        } else {
            gameOverView.showStalemate();
        }
    }

    private void refreshBoard() {
        boardView.refresh();
        materialView.update(game.getMaterialBalance());
    }
}
