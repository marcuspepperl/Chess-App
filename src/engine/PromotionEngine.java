package engine;

import board.GameBoard;
import board.Coordinates;
import piece.APiece;

public class PromotionEngine {

    private final GameBoard gameBoard;

    public PromotionEngine(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public boolean validMove(Coordinates firstSquare, Coordinates secondSquare);

    public boolean executeMove(Coordinates firstSquare, Coordinates secondPiece, APiece newPiece);
}
