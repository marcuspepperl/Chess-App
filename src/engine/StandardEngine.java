package engine;

import board.GameBoard;
import board.Coordinates;

public abstract class StandardEngine {

    protected final GameBoard gameBoard;

    public AMoveEngine(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public abstract boolean handleMove(Coordinates firstSquare, Coordinates secondSquare);

    public abstract boolean validMove(Coordinates firstSquare, Coordinates secondSquare);

    protected abstract boolean simulateMove(Coordinates firstSquare, Coordinates secondSquare);

    protected abstract boolean executeMove(Coordinates firstSquare, Coordinates secondSquare);

    protected abstract boolean executeHelper(Coordinates firstSquare, Coordinates secondSquare);

}
