package util;

import model.Difficulty;
import model.Move;

import java.io.*;
import java.util.*;

public class StockfishEngine {
    private Process process;
    private BufferedWriter writer;
    private BufferedReader reader;
    private final Random random = new Random();

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
        int elo = difficulty.getElo();

        if (elo >= 1320) {
            // Within Stockfish's supported range — use the real ELO limiter.
            sendCommand("setoption name UCI_LimitStrength value true");
            sendCommand("setoption name UCI_Elo value " + elo);
        } else {
            // Below Stockfish's floor: the ELO limiter can't go this low,
            // so turn it off and rely on Skill Level + blunder injection instead.
            sendCommand("setoption name UCI_LimitStrength value false");
            sendCommand("setoption name Skill Level value 0");
        }

        // How many candidate moves we ask for; used later to pick a
        // deliberately weaker move for low difficulties.
        sendCommand("setoption name MultiPV value " + difficulty.getMultiPv());

        sendCommand("isready");
        waitFor("readyok");
    }

    /**
     * Gets a move from Stockfish, optionally "blundering" by picking a
     * worse move than the engine's best one.
     *
     * @param blunderChance probability (0.0–1.0) of NOT playing the best move
     */
    public String getBestMove(String fen, int thinkTime, double blunderChance) throws IOException {
        sendCommand("position fen " + fen);
        sendCommand("go movetime " + thinkTime);

        // rank -> move, e.g. 1 -> "e2e4", 2 -> "d2d4", ...
        Map<Integer, String> rankedMoves = new TreeMap<>();
        String bestMove = null;

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("<< " + line);

            if (line.startsWith("info") && line.contains(" multipv ")) {
                String[] tokens = line.split(" ");
                int pvIndex = -1;
                String move = null;

                for (int i = 0; i < tokens.length; i++) {
                    if (tokens[i].equals("multipv") && i + 1 < tokens.length) {
                        pvIndex = Integer.parseInt(tokens[i + 1]);
                    }
                    if (tokens[i].equals("pv") && i + 1 < tokens.length) {
                        move = tokens[i + 1];
                    }
                }

                if (pvIndex != -1 && move != null) {
                    rankedMoves.put(pvIndex, move);
                }
            }

            if (line.startsWith("bestmove")) {
                String[] parts = line.split(" ");
                bestMove = parts[1];
                break;
            }
        }

        if (bestMove == null) {
            throw new IOException("Stockfish stopped responding while calculating.");
        }

        List<String> candidates = new ArrayList<>(rankedMoves.values());
        if (candidates.isEmpty()) {
            candidates.add(bestMove);
        }

        return pickMove(candidates, blunderChance);
    }

    private String pickMove(List<String> rankedCandidates, double blunderChance) {
        if (rankedCandidates.size() <= 1) {
            return rankedCandidates.get(0);
        }

        if (random.nextDouble() < blunderChance) {
            // Pick uniformly among ALL candidates (including weaker ones).
            int index = random.nextInt(rankedCandidates.size());
            System.out.println("Blundering: playing rank " + (index + 1) + " move instead of best.");
            return rankedCandidates.get(index);
        }

        return rankedCandidates.get(0); // best move
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