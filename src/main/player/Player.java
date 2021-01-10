package main.player;

import main.board.Coordinates;
import java.util.Scanner;

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
        return this.basicInput();
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

    private Coordinates[] basicInput() {
        Scanner scanner = new Scanner(System.in);
        Coordinates[] request = new Coordinates[2];
        int count = 0;
        int xPos, yPos;
        while (count < 2) {
            try {
                System.out.println("Enter X Position: ");
                xPos = scanner.nextInt();
                System.out.println("Enter Y Position: ");
                yPos = scanner.nextInt();
                request[count] = new Coordinates(xPos, yPos);
                count++;
            }
            catch (Exception ignored) {
                System.out.println("Must enter a valid integer.");
            }
        }
        System.out.println("Returned Move");
        return request;
    }
}
