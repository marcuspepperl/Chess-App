package main.display;

import main.board.*;

public class ConsoleDisplay {

    private final GameBoard gameBoard;

    public ConsoleDisplay(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void displayColorBoard(int color) {
        if (color == 1) {
            displayColorBoardWhite();
        } else {
            displayColorBoardBlack();
        }
    }

    public void displayStringBoard(int color) {
        if (color == 1) {
            displayStringBoardWhite();
        } else {
            displayStringBoardBlack();
        }
    }

    private void displayColorBoardWhite() {
        StringBuilder singleRow;
        Coordinates coordinates;
        for (int yPos = gameBoard.getYSize() - 1; yPos > -1; yPos--) {
            singleRow = new StringBuilder();
            for (int xPos = 0; xPos < gameBoard.getXSize(); xPos++) {
                coordinates = new Coordinates(xPos, yPos);
                if (gameBoard.getColor(coordinates) == -1) {
                    singleRow.append("-1 ");
                } else {
                    singleRow.append(' ').append(gameBoard.getColor(coordinates).toString()).append(' ');
                }
            }
            System.out.println(singleRow.toString());
        }
    }

    private void displayColorBoardBlack() {
        StringBuilder singleRow;
        Coordinates coordinates;
        for (int yPos = 0; yPos < gameBoard.getYSize(); yPos++) {
            singleRow = new StringBuilder();
            for (int xPos = gameBoard.getXSize() - 1; xPos > -1; xPos--) {
                coordinates = new Coordinates(xPos, yPos);
                if (gameBoard.getColor(coordinates) == -1) {
                    singleRow.append("-1 ");
                } else {
                    singleRow.append(' ').append(gameBoard.getColor(coordinates).toString()).append(' ');
                }
            }
            System.out.println(singleRow.toString());
        }
    }

    private void displayStringBoardWhite() {
        StringBuilder singleRow;
        Coordinates coordinates;
        for (int yPos = gameBoard.getYSize() - 1; yPos > -1; yPos--) {
            singleRow = new StringBuilder();
            for (int xPos = 0; xPos < gameBoard.getXSize(); xPos++) {
                coordinates = new Coordinates(xPos, yPos);
                if (gameBoard.getColor(coordinates) == -1) {
                    singleRow.append('-').append(gameBoard.getPiece(coordinates).getType()).append(' ');
                } else if (gameBoard.getColor(coordinates) == 1){
                    singleRow.append(' ').append(gameBoard.getPiece(coordinates).getType()).append(' ');
                } else {
                    singleRow.append(" _ ");
                }
            }
            System.out.println(singleRow.toString());
        }
    }

    private void displayStringBoardBlack() {
        StringBuilder singleRow;
        Coordinates coordinates;
        for (int yPos = 0; yPos < gameBoard.getYSize(); yPos++) {
            singleRow = new StringBuilder();
            for (int xPos = gameBoard.getXSize() - 1; xPos > -1; xPos--) {
                coordinates = new Coordinates(xPos, yPos);
                if (gameBoard.getColor(coordinates) == -1) {
                    singleRow.append('-').append(gameBoard.getPiece(coordinates).getType()).append(' ');
                } else if (gameBoard.getColor(coordinates) == 1){
                    singleRow.append(' ').append(gameBoard.getPiece(coordinates).getType()).append(' ');
                } else {
                    singleRow.append(" _ ");
                }
            }
            System.out.println(singleRow.toString());
        }
    }
}
