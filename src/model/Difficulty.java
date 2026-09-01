package model;

public enum Difficulty {
    BEGINNER(500, 0.55, 6),
    INTERMEDIATE(1000, 0.30, 4),
    ADVANCED(1500, 0.0, 1),
    EXPERT(2000, 0.0, 1),
    GRANDMASTER(3000, 0.0, 1);

    private final int elo;
    private final double blunderChance; // probability of NOT playing the best move
    private final int multiPv;          // how many candidate moves Stockfish reports

    Difficulty(int elo, double blunderChance, int multiPv) {
        this.elo = elo;
        this.blunderChance = blunderChance;
        this.multiPv = multiPv;
    }

    public int getElo() { return elo; }
    public double getBlunderChance() { return blunderChance; }
    public int getMultiPv() { return multiPv; }
}