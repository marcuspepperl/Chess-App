package engine;

import board.GameBoard;
import board.Coordinates;

public class CastlesEngine extends AMoveEngine {

    public CastlesEngine(GameBoard gameBoard) {
        super(gameBoard);
    }

    public boolean handleMove(Coordinates firstSquare, Coordinates secondSquare);

    public boolean validMove(Coordinates firstSquare, Coordinates secondSquare);

    protected boolean simulateMove(Coordinates firstSquare, Coordinates secondSquare);

    protected boolean executeMove(Coordinates firstSquare, Coordinates secondSquare);

    protected boolean executeHelper(Coordinates firstSquare, Coordinates secondSquare);
}
