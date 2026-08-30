package util;

import model.Difficulty;
import model.Move;

import java.io.*;

public class StockfishEngine {
    private Process process;
    private BufferedWriter writer;
    private BufferedReader reader;

    public void start() throws IOException {
        ProcessBuilder processBuilder =
                new ProcessBuilder("stockfish/stockfish.exe");

        processBuilder.redirectErrorStream(true);
        process = processBuilder.start();

        writer = new BufferedWriter(
                new OutputStreamWriter(process.getOutputStream())
        );

        reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );

        sendCommand("uci");
        waitFor("uciok");

        sendCommand("isready");
        waitFor("readyok");

        System.out.println("Stockfish started successfully.");
    }

    private void sendCommand(String command) throws IOException {
        writer.write(command);
        writer.newLine();
        writer.flush();

        System.out.println(">> " + command);
    }

    private void waitFor(String expected) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("<< " + line);
            if (line.equals(expected)) return;
        }

        throw new IOException(
                "Stockfish stopped responding. Expected: " + expected
        );
    }

    public void setDifficulty(Difficulty difficulty) throws IOException {
        sendCommand(
                "setoption name UCI_LimitStrength value true"
        );

        sendCommand(
                "setoption name UCI_Elo value " + difficulty.getElo()
        );

        sendCommand("isready");
        waitFor("readyok");
    }

    public String getBestMove(String fen, int thinkTime) throws IOException {

        // Give Stockfish the current position
        sendCommand("position fen " + fen);

        // Ask Stockfish to calculate
        sendCommand("go movetime " + thinkTime);

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("<< " + line);

            if (line.startsWith("bestmove")) {
                String[] parts = line.split(" ");
                return parts[1];
            }
        }

        throw new IOException(
                "Stockfish stopped responding while calculating."
        );
    }

    public Move convertToMove(String uciMove) {
        if (uciMove == null || uciMove.length() < 4) {
            throw new IllegalArgumentException("Invalid Stockfish move: " + uciMove);
        }

        String from = uciMove.substring(0, 2);
        String to = uciMove.substring(2, 4);

        int fromCol = from.charAt(0) - 'a';
        int fromRow = 8 - Character.getNumericValue(from.charAt(1));

        int toCol = to.charAt(0) - 'a';
        int toRow = 8 - Character.getNumericValue(to.charAt(1));

        return new Move(fromRow, fromCol, toRow, toCol);
    }

    public void stop() throws IOException {

        if (process != null && process.isAlive()) {
            sendCommand("quit");
            process.destroy();
        }
    }
}