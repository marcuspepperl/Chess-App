package player;

import board.Coordinates;

public class Player {

    private int wins;
    private int losses;
    private int draws;

    public Player() {
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
    }

    public Coordinates[] requestMove() {
        return new Coordinates[]{new Coordinates(1, 2), new Coordinates(2, 3)};
    }

    public int requestPromotion() {
        return 0;
    }

    public int getWins() {
        return this.wins;
    }

    public int getLosses() {
        return this.losses;
    }

    public int getDraws() {
        return this.draws;
    }

    public void addWin() {
        this.wins++;
    }

    public void addLoss() {
        this.losses++;
    }

    public void addDraw() {
        this.draws++;
    }
}
