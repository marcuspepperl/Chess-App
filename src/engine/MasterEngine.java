package engine;

import board.*;


public class MasterEngine {

    private final GameBoard gameBoard;
    private final StandardEngine standardEngine;
    private final EnPassantEngine enPassantEngine;
    private final CastlesEngine castlesEngine;
    private final PromotionEngine promotionEngine;


    public MasterEngine(GameBoard gameBoard) {
        this.gameBoard = gameBoard;

    }

    public boolean handleMove(Coordinates firstSquare, Coordinates secondSquare);



}
