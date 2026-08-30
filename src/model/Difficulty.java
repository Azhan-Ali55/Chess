package model;

public enum Difficulty {
    BEGINNER(500),
    INTERMEDIATE(1000),
    ADVANCED(1500),
    EXPERT(2000),
    GRANDMASTER(3000);

    private final int elo;

    Difficulty(int elo) {
        this.elo = elo;
    }

    public int getElo() {
        return elo;
    }
}