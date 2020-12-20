package board;

import piece.*;

public class MoveEngine {

    private final GameBoard gameBoard;
    private final StandardEngine standardEngine;
    private final EnPassantEngine enPassantEngine;
    private final CastlesEngine castlesEngine;
    private final PromotionEngine promotionEngine;

    public MoveEngine(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.standardEngine = new StandardEngine(this.gameBoard);
        this.enPassantEngine = new EnPassantEngine(this.gameBoard);
        this.castlesEngine = new CastlesEngine(this.gameBoard);
        this.promotionEngine = new PromotionEngine(this.gameBoard);
    }

    public boolean handleMove(Coordinates[] request, int color) throws InvalidMoveException {
        if (request.length != 2) {
            throw new InvalidMoveException("Move request has invalid number of coordinates");
        }
        Coordinates startSquare = request[0];
        Coordinates endSquare = request[1];

        if (!gameBoard.inBounds(startSquare) || !gameBoard.inBounds(endSquare)) {
            throw new InvalidMoveException("Move coordinates are out of bounds");
        }
        if (gameBoard.getColor(startSquare) != color) {
            throw new InvalidMoveException("First square does not contain player's piece");
        }
        if (promotionEngine.validPromotion(startSquare, endSquare)) {
            return true;
        }
        if (standardEngine.handleStandard(startSquare, endSquare) ||
            castlesEngine.handleCastles(startSquare, endSquare) ||
            enPassantEngine.handleEnPassant(startSquare, endSquare)) {
            gameBoard.endMove(gameBoard.getPiece(endSquare));
            return false;
        }
        throw new InvalidMoveException("The move is not possible");
    }

    public void handlePromotion(Coordinates startSquare, Coordinates endSquare, APiece
        newPiece) {
        promotionEngine.executePromotion(startSquare, endSquare, newPiece);
        gameBoard.endMove(gameBoard.getPiece(endSquare));
    }

    boolean validStandard(Coordinates startSquare, Coordinates endSquare) {
        return this.standardEngine.validStandard(startSquare, endSquare);
    }

    boolean validEnPassant(Coordinates startSquare, Coordinates endSquare) {
        return this.enPassantEngine.validEnPassant(startSquare, endSquare);
    }


    private static class StandardEngine {

        protected final GameBoard gameBoard;

        protected StandardEngine(GameBoard gameBoard) {
            this.gameBoard = gameBoard;
        }

        protected boolean handleStandard(Coordinates startSquare, Coordinates endSquare) {
            if (!this.validStandard(startSquare, endSquare)) {
                return false;
            }
            this.executeStandard(startSquare, endSquare);
            return true;
        }

        protected boolean validStandard(Coordinates startSquare, Coordinates endSquare) {
            return gameBoard.getPiece(startSquare).isValidMove(endSquare, gameBoard.getColorBoard())
                && this.simulateStandard(startSquare, endSquare);
        }

        protected boolean simulateStandard(Coordinates startSquare, Coordinates endSquare) {
            APiece movePiece = gameBoard.getPiece(startSquare), takePiece = gameBoard.getPiece(endSquare);
            this.executeHelperStandard(startSquare, endSquare);
            boolean validMove = gameBoard.isCheck(movePiece.getColor());

            gameBoard.setPiece(startSquare, movePiece);
            gameBoard.setColor(startSquare, movePiece.getColor());
            gameBoard.setPiece(endSquare, takePiece);
            gameBoard.setColor(endSquare, takePiece == null ? 0 : takePiece.getColor());

            return validMove;
        }

        protected void executeStandard(Coordinates startSquare, Coordinates endSquare) {
            APiece movePiece = gameBoard.getPiece(startSquare);
            gameBoard.takePiece(gameBoard.getPiece(endSquare));
            this.executeHelperStandard(startSquare, endSquare);
            movePiece.move(startSquare);
        }

        protected void executeHelperStandard(Coordinates startSquare, Coordinates endSquare) {
            APiece movePiece = gameBoard.getPiece(startSquare);
            gameBoard.setPiece(startSquare, null);
            gameBoard.setColor(startSquare, 0);
            gameBoard.setPiece(endSquare, movePiece);
            gameBoard.setColor(endSquare, movePiece.getColor());
        }
    }

    private static class PromotionEngine extends StandardEngine {

        private PromotionEngine(GameBoard gameBoard) {
            super(gameBoard);
        }

        private boolean validPromotion(Coordinates startSquare, Coordinates endSquare) {

            APiece movePiece = gameBoard.getPiece(startSquare);
            if (!(movePiece instanceof Pawn)) {
                return false;
            }
            int newYPos = endSquare.getYPos();
            int color = movePiece.getColor();
            if (!((color == 1 && newYPos == gameBoard.getYSize() - 1) || (color == -1
                && newYPos == 0))) {
                return false;
            }
            return this.simulateStandard(startSquare, endSquare);
        }

        private void executePromotion(Coordinates startSquare, Coordinates endSquare,
            APiece
                newPiece) {
            APiece movePiece = gameBoard.getPiece(startSquare);
            this.executeStandard(startSquare, endSquare);
            gameBoard.takePiece(movePiece);
            gameBoard.addPiece(newPiece);
            gameBoard.endMove(gameBoard.getPiece(endSquare));
        }
    }

    private static class CastlesEngine extends StandardEngine {

        private CastlesEngine(GameBoard gameBoard) {
            super(gameBoard);
        }

        private boolean handleCastles(Coordinates startSquare, Coordinates endSquare) {
            if (!this.validCastles(startSquare, endSquare)) {
                return false;
            }
            this.executeCastles(startSquare, endSquare);
            return true;
        }

        private boolean validCastles(Coordinates startSquare, Coordinates endSquare) {
            APiece movePiece = gameBoard.getPiece(startSquare);
            if (!(movePiece instanceof King) || gameBoard.isCheck(movePiece.getColor()) ||
                movePiece.getMoveCount() != 0) {
                return false;
            }
            int xPos = startSquare.getXPos(), yPos = startSquare.getYPos();
            int newXPos = endSquare.getXPos(), newYPos = endSquare.getYPos();
            int color = movePiece.getColor();

            if (!((color == 1 && yPos == 0 && newYPos == 0) || (color == -1 && yPos ==
                gameBoard.getYSize() - 1 && newYPos == gameBoard.getYSize() - 1))) {
                return false;
            }
            boolean kingSide = newXPos > xPos;

            // trying to castle to correct place
            if (!((kingSide && newXPos == gameBoard.getXSize() - 2) || (!kingSide && newXPos == 2))) {
                return false;
            }

            Coordinates rookSquare = kingSide ? new Coordinates(gameBoard.getXSize() - 1, yPos) :
                new Coordinates(0, yPos);
            APiece rookPiece = gameBoard.getPiece(rookSquare);

            if (!(rookPiece instanceof Rook && rookPiece.getColor() == movePiece.getColor() &&
                rookPiece.getMoveCount() != 0)) {
                return false;
            }
            return this.simulateCastles(startSquare, endSquare);
        }

        private boolean simulateCastles(Coordinates startSquare, Coordinates endSquare) {
            boolean kingSide = endSquare.getXPos() > startSquare.getXPos();
            int newYPos = endSquare.getYPos();
            int lowerBound, upperBound;
            if (kingSide) {
                lowerBound = startSquare.getXPos();
                upperBound = endSquare.getXPos();
            } else {
                lowerBound = endSquare.getXPos();
                upperBound = startSquare.getXPos();
            }
            boolean validMove = true;
            for (int newXPos = lowerBound; newXPos <= upperBound; newXPos++) {
                if (!super.simulateStandard(startSquare, new Coordinates(newXPos, newYPos))) {
                    validMove = false;
                    break;
                }
            }
            return validMove;
        }

        private void executeCastles(Coordinates startSquare, Coordinates endSquare) {
            int xPos = startSquare.getXPos();
            int yPos = startSquare.getYPos();
            int newXPos = endSquare.getXPos();

            boolean kingSide = newXPos > xPos;
            Coordinates rookSquare = kingSide ? new Coordinates(gameBoard.getXSize() - 1, yPos) :
                new Coordinates(0, yPos);
            Coordinates newRookSquare = kingSide ? new Coordinates(newXPos - 1, yPos) :
                new Coordinates(newXPos + 1, yPos);

            super.executeStandard(startSquare, endSquare);
            super.executeStandard(rookSquare, newRookSquare);
        }
    }

    private static class EnPassantEngine {

        private final GameBoard gameBoard;

        private EnPassantEngine(GameBoard gameBoard) {
            this.gameBoard = gameBoard;
        }

        private boolean handleEnPassant(Coordinates startSquare, Coordinates endSquare) {
            if (!this.validEnPassant(startSquare, endSquare)) {
                return false;
            }
            this.executeEnPassant(startSquare, endSquare);
            return true;
        }

        private boolean validEnPassant(Coordinates startSquare, Coordinates endSquare) {
            APiece movePiece = gameBoard.getPiece(startSquare);
            if (!(movePiece instanceof Pawn)) {
                return false;
            }
            int xPos = startSquare.getXPos(), yPos = startSquare.getYPos();
            int newXPos = endSquare.getXPos(), newYPos = endSquare.getYPos();
            int color = movePiece.getColor();

            if (!(newYPos == yPos + color && (newXPos == xPos + 1 || newXPos == xPos - 1))) {
                return false;
            }
            if (!((color == 1 && yPos == gameBoard.getYSize() - 3) || (color == -1 && yPos == 3))) {
                return false;
            }
            APiece prevPiece = gameBoard.getPrevPiece();

            if (!(prevPiece instanceof Pawn && prevPiece.getColor() != color)) {
                return false;
            }
            int takeXPos = prevPiece.getCoordinates().getXPos();
            int takeYPos = prevPiece.getCoordinates().getYPos();

            if (!(prevPiece.getMoveCount() == 1 && takeYPos == yPos && takeXPos == newXPos)) {
                return false;
            }
            return this.simulateEnPassant(startSquare, endSquare);
        }

        private boolean simulateEnPassant(Coordinates startSquare, Coordinates endSquare) {
            APiece movePiece = gameBoard.getPiece(startSquare);
            APiece prevPiece = gameBoard.getPrevPiece();
            this.executeHelperEnPassant(startSquare, endSquare);
            boolean validMove = gameBoard.isCheck(movePiece.getColor());

            gameBoard.setPiece(endSquare, null);
            gameBoard.setColor(endSquare, 0);
            gameBoard.setPiece(startSquare, movePiece);
            gameBoard.setColor(startSquare, movePiece.getColor());
            gameBoard.setPiece(prevPiece.getCoordinates(), prevPiece);
            gameBoard.setColor(prevPiece.getCoordinates(), prevPiece.getColor());

            return validMove;
        }

        private void executeEnPassant(Coordinates startSquare, Coordinates endSquare) {
            this.executeHelperEnPassant(startSquare, endSquare);
            gameBoard.takePiece(gameBoard.getPrevPiece());
            gameBoard.getPiece(startSquare).move(endSquare);
        }

        private void executeHelperEnPassant(Coordinates startSquare, Coordinates endSquare) {
            APiece movePiece = gameBoard.getPiece(startSquare);
            Coordinates prevCoordinates = gameBoard.getPrevPiece().getCoordinates();
            gameBoard.setPiece(startSquare, null);
            gameBoard.setColor(startSquare, 0);
            gameBoard.setPiece(endSquare, movePiece);
            gameBoard.setColor(endSquare, movePiece.getColor());
            gameBoard.setPiece(prevCoordinates, null);
            gameBoard.setColor(prevCoordinates, 0);
        }
    }



}
