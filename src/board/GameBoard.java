package board;

import piece.*;
import engine.*;

import java.util.Set;
import java.util.HashSet;

public class GameBoard {

    private final int xSize;
    private final int ySize;
    private BoardWrapper<APiece> pieceBoard;
    private BoardWrapper<Integer> colorBoard;

    private Set<APiece> whitePieces;
    private Set<APiece> blackPieces;
    private Set<APiece> takenWhitePieces;
    private Set<APiece> takenBlackPieces;
    private King whiteKing;
    private King blackKing;

    private StandardEngine standardEngine;
    private EnPassantEngine enPassantEngine;
    private CastlesEngine castlesEngine;
    private PromotionEngine promotionEngine;

    private int moveCount;
    private APiece prevPiece;


    public GameBoard(int xSize, int ySize) throws InvalidSetUpException {
        if (xSize < 4 || ySize < 4) {
            throw new InvalidSetUpException("Board size too small");
        }
        this.xSize = xSize;
        this.ySize = ySize;
        this.pieceBoard = this.initializePieceBoard(xSize, ySize);
        this.colorBoard = this.initializeColorBoard(xSize, ySize);
        this.whitePieces = new HashSet<>();
        this.blackPieces = new HashSet<>();
        this.takenWhitePieces = new HashSet<>();
        this.takenBlackPieces = new HashSet<>();
        this.standardEngine = new StandardEngine(this);
        this.enPassantEngine = new EnPassantEngine(this);
        this.castlesEngine = new CastlesEngine(this);
        this.promotionEngine = new PromotionEngine(this);
    }

    public void setPieces(Set<APiece> allPieces) throws
        InvalidSetUpException {

        int whiteKingCount = 0, blackKingCount = 0;
        int color;

        for (APiece piece : allPieces) {
            Coordinates coordinates = piece.getCoordinates();
            color = piece.getColor();

            if (!inBounds(coordinates) || this.getColor(coordinates) != 0) {
                throw new InvalidSetUpException("Invalid white piece");
            }
            if (piece instanceof King) {
                if (color == 1) {
                    whiteKingCount++;
                    this.whiteKing = (King) piece;
                } else {
                    blackKingCount++;
                    this.blackKing = (King) piece;
                }
            }
            else if (piece instanceof Pawn) {
                if (color == 1 && coordinates.getYPos() == ySize - 1) {
                    throw new InvalidSetUpException("White pawn on promotion rank");
                } else if (color == -1 && coordinates.getYPos() == 0) {
                    throw new InvalidSetUpException("Black pawn on promotion rank");
                }
            }
            this.addPiece(piece);
        }

        if (whiteKingCount != 1 || blackKingCount != 1) {
            throw new InvalidSetUpException("Invalid number of kings");
        }
    }

    public boolean isCheck(int color) {
        return !this.checkBy(color).isEmpty();
    }

    public boolean isCheckMate(int color) {
        return isCheck(color) && !hasValidMove(color);
    }

    public boolean isStaleMate(int color) {
        return !isCheck(color) && !hasValidMove(color);
    }

    public Set<Coordinates> checkBy(int color) {

        Set<Coordinates> checkSet = new HashSet<>();
        Set<APiece> opposingPieces;
        APiece defendingKing;
        if (color == 1) {
            opposingPieces = this.blackPieces;
            defendingKing = this.whiteKing;
        } else {
            opposingPieces = this.whitePieces;
            defendingKing = this.blackKing;
        }
        Coordinates kingCoordinates = defendingKing.getCoordinates();
        for (APiece opposingPiece : opposingPieces) {
            if (opposingPiece.isValidMove(kingCoordinates, this.colorBoard)) {
                checkSet.add(opposingPiece.getCoordinates());
            }
        }
        return checkSet;
    }

    // returns true if promotion is requested
    public boolean handleMove(Coordinates[] request, int color) throws
        InvalidMoveException {
        if (request.length != 2) {
            throw new InvalidMoveException("Move request has invalid number of coordinates");
        }
        Coordinates startSquare = request[0];
        Coordinates endSquare = request[1];

        if (!inBounds(startSquare) || !inBounds(endSquare)) {
            throw new InvalidMoveException("Move coordinates are out of bounds");
        }
        if (this.getColor(startSquare) != color) {
            throw new InvalidMoveException("First square does not contain player's piece");
        }
        if (this.validPromotion(startSquare, endSquare)) {
            return true;
        }

        if (this.handleStandard(startSquare, endSquare) ||
            this.handleCastles(startSquare, endSquare) ||
            this.handleEnPassant(startSquare, endSquare)) {
            this.endMove(this.getPiece(endSquare));
            return false;
        }
        throw new InvalidMoveException("The move is not possible");
    }

    public void executePromotion(Coordinates startSquare, Coordinates endSquare, APiece
        newPiece) {
        APiece movePiece = this.getPiece(startSquare);
        this.executeStandard(startSquare, endSquare);
        this.takePiece(movePiece);
        this.addPiece(newPiece);
        this.endMove(this.getPiece(endSquare));
    }

    // returns true if promotion is valid
    private boolean validPromotion(Coordinates startSquare, Coordinates endSquare) {

        APiece movePiece = this.getPiece(startSquare);
        if (!(movePiece instanceof Pawn)) {
            return false;
        }
        int newYPos = endSquare.getYPos();
        int color = movePiece.getColor();
        if (!((color == 1 && newYPos == this.ySize - 1) || (color == -1 && newYPos == 0))) {
            return false;
        }
        return this.simulateStandard(startSquare, endSquare);
    }

    private boolean handleStandard(Coordinates startSquare, Coordinates endSquare) {

        if (!validStandard(startSquare, endSquare)) {
            return false;
        }
        this.executeStandard(startSquare, endSquare);
        return true;
    }

    private boolean validStandard(Coordinates startSquare, Coordinates endSquare) {
        return this.getPiece(startSquare).isValidMove(endSquare, this.colorBoard)
            && this.simulateStandard(startSquare, endSquare);
    }

    private boolean simulateStandard(Coordinates startSquare, Coordinates endSquare) {

        APiece movePiece = this.getPiece(startSquare), takePiece = this.getPiece(endSquare);
        this.moveStandard(startSquare, endSquare);
        boolean validMove = this.isCheck(movePiece.getColor());

        this.setPiece(startSquare, movePiece);
        this.setColor(startSquare, movePiece.getColor());
        this.setPiece(endSquare, takePiece);
        this.setColor(endSquare, takePiece == null ? 0 : takePiece.getColor());

        return validMove;
    }

    private void executeStandard(Coordinates startSquare, Coordinates endSquare) {

        APiece movePiece = this.getPiece(startSquare);
        this.takePiece(this.getPiece(endSquare));
        this.moveStandard(startSquare, endSquare);
        movePiece.move(startSquare);
    }

    private void moveStandard(Coordinates startSquare, Coordinates endSquare) {

        APiece movePiece = this.getPiece(startSquare);
        this.setPiece(startSquare, null);
        this.setColor(startSquare, 0);
        this.setPiece(endSquare, movePiece);
        this.setColor(endSquare, movePiece.getColor());
    }

    private boolean handleEnPassant(Coordinates startSquare, Coordinates endSquare) {
        if (!this.validEnPassant(startSquare, endSquare)) {
            return false;
        }
        this.executeEnPassant(startSquare, endSquare);
        return true;
    }

    private boolean validEnPassant(Coordinates startSquare, Coordinates endSquare) {

        APiece movePiece = this.getPiece(startSquare);
        if (!(movePiece instanceof Pawn)) {
            return false;
        }
        int xPos = startSquare.getXPos(), yPos = startSquare.getYPos();
        int newXPos = endSquare.getXPos(), newYPos = endSquare.getYPos();
        int color = movePiece.getColor();

        if (!(newYPos == yPos + color && (newXPos == xPos + 1 || newXPos == xPos - 1))) {
            return false;
        }
        if (!((color == 1 && yPos == this.ySize - 3) || (color == -1 && yPos == 3))) {
            return false;
        }
        if (!(this.prevPiece != null && this.prevPiece instanceof Pawn && this.prevPiece.getColor() != color)) {
            return false;
        }
        int takeXPos = this.prevPiece.getCoordinates().getXPos();
        int takeYPos = this.prevPiece.getCoordinates().getYPos();

        if (!(this.prevPiece.getMoveCount() == 1 && takeYPos == yPos && takeXPos == newXPos)) {
            return false;
        }
        return this.simulateEnPassant(startSquare, endSquare);
    }

    private boolean simulateEnPassant(Coordinates startSquare, Coordinates endSquare) {

        APiece movePiece = this.getPiece(startSquare);
        this.moveEnPassant(startSquare, endSquare);
        boolean validMove = this.isCheck(movePiece.getColor());

        this.setPiece(endSquare, null);
        this.setColor(endSquare, 0);
        this.setPiece(startSquare, movePiece);
        this.setColor(startSquare, movePiece.getColor());
        this.setPiece(this.prevPiece.getCoordinates(), this.prevPiece);
        this.setColor(this.prevPiece.getCoordinates(), this.prevPiece.getColor());

        return validMove;
    }

    private void executeEnPassant(Coordinates startSquare, Coordinates endSquare) {

        this.moveEnPassant(startSquare, endSquare);
        this.takePiece(this.prevPiece);
        this.getPiece(startSquare).move(endSquare);
    }

    private void moveEnPassant(Coordinates startSquare, Coordinates endSquare) {

        APiece movePiece = this.getPiece(startSquare);
        this.setPiece(startSquare, null);
        this.setColor(startSquare, 0);
        this.setPiece(endSquare, movePiece);
        this.setColor(endSquare, movePiece.getColor());
        this.setPiece(this.prevPiece.getCoordinates(), null);
        this.setColor(this.prevPiece.getCoordinates(), 0);
    }

    private boolean handleCastles(Coordinates startSquare, Coordinates endSquare) {
        if (!this.validCastles(startSquare, endSquare)) {
            return false;
        }
        this.executeCastles(startSquare, endSquare);
        return true;
    }

    private boolean validCastles(Coordinates startSquare, Coordinates endSquare) {

        APiece movePiece = this.getPiece(startSquare);
        if (!(movePiece instanceof King) || this.isCheck(movePiece.getColor()) ||
            movePiece.getMoveCount() != 0) {
            return false;
        }
        int xPos = startSquare.getXPos(), yPos = startSquare.getYPos();
        int newXPos = endSquare.getXPos(), newYPos = endSquare.getYPos();
        int color = movePiece.getColor();

        if (!((color == 1 && yPos == 0 && newYPos == 0) || (color == -1 && yPos == this.ySize - 1
            && newYPos == this.ySize - 1))) {
            return false;
        }
        boolean kingSide = newXPos > xPos;

        // trying to castle to correct place
        if (!((kingSide && newXPos == this.xSize - 2) || (!kingSide && newXPos == 2))) {
            return false;
        }

        Coordinates rookSquare = kingSide ? new Coordinates(this.xSize - 1, yPos) :
            new Coordinates(0, yPos);
        APiece rookPiece = this.getPiece(rookSquare);

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
            if (!simulateStandard(startSquare, new Coordinates(newXPos, newYPos))) {
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
        Coordinates rookSquare = kingSide ? new Coordinates(this.xSize - 1, yPos) :
            new Coordinates(0, yPos);
        Coordinates newRookSquare = kingSide ? new Coordinates(newXPos - 1, yPos) :
            new Coordinates(newXPos + 1, yPos);

        this.executeStandard(startSquare, endSquare);
        this.executeStandard(rookSquare, newRookSquare);
    }

    // standard moves and en passant
    private boolean hasValidMove(int color) {
        Set<APiece> allPieces;
        if (color == 1) {
            allPieces = this.whitePieces;
        } else {
            allPieces = this.blackPieces;
        }
        Coordinates coordinates, leftSquare, rightSquare;
        for (APiece piece : allPieces) {
            coordinates = piece.getCoordinates();
            for (Coordinates newSquare : piece
                .validMoves(this.colorBoard)) {
                if (this.validStandard(coordinates, newSquare)) {
                    return true;
                }
            }
            if (piece instanceof Pawn) {
                leftSquare = new Coordinates(coordinates.getXPos() - 1,
                    coordinates.getYPos() + color);
                rightSquare = new Coordinates(coordinates.getXPos() + 1,
                    coordinates.getYPos() + color);
                if ((this.inBounds(leftSquare) && this
                    .validEnPassant(coordinates, leftSquare))
                    || (this.inBounds(rightSquare) && this
                    .validEnPassant(coordinates, rightSquare))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean takePiece(APiece piece) {
        if (piece != null) {
            if (piece.getColor() == 1) {
                this.whitePieces.remove(piece);
                this.takenWhitePieces.add(piece);
            } else {
                this.blackPieces.remove(piece);
                this.takenBlackPieces.add(piece);
            }
            return true;
        }
        return false;
    }

    private void addPiece(APiece piece) {

        this.setColor(piece.getCoordinates(), piece.getColor());
        this.setPiece(piece.getCoordinates(), piece);

        if (piece.getColor() == 1) {
            this.whitePieces.add(piece);
        } else {
            this.blackPieces.add(piece);
        }
    }

    private void endMove(APiece movePiece) {
        this.prevPiece = movePiece;
        this.moveCount++;
    }

    private BoardWrapper<APiece> initializePieceBoard(int xSize, int ySize) {
        APiece[][] pieceBoard = new APiece[xSize][ySize];
        for (int xPos = 0; xPos < xSize; xPos++) {
            for (int yPos = 0; yPos < ySize; yPos++) {
                pieceBoard[xPos][yPos] = null;
            }
        }
        return new BoardWrapper<>(pieceBoard);
    }

    private BoardWrapper<Integer> initializeColorBoard(int xSize, int ySize) {
        Integer[][] colorBoard = new Integer[xSize][ySize];
        for (int xPos = 0; xPos < xSize; xPos++) {
            for (int yPos = 0; yPos < ySize; yPos++) {
                colorBoard[xPos][yPos] = 0;
            }
        }
        return new BoardWrapper<>(colorBoard);
    }

    private APiece getPiece(Coordinates coordinates) {
        return this.pieceBoard.getValue(coordinates.getXPos(), coordinates.getYPos());
    }

    private int getColor(Coordinates coordinates) {
        return this.colorBoard.getValue(coordinates.getXPos(), coordinates.getYPos());
    }

    private void setPiece(Coordinates coordinates, APiece piece) {
        this.pieceBoard.setValue(coordinates.getXPos(), coordinates.getYPos(), piece);
    }

    private void setColor(Coordinates coordinates, int color) {
        this.colorBoard.setValue(coordinates.getXPos(), coordinates.getYPos(), color);
    }

    private boolean inBounds(Coordinates coordinates) {
        return coordinates.getXPos() > 0 && coordinates.getXPos() < this.xSize &&
            coordinates.getYPos() > 0 && coordinates.getYPos() < this.ySize;
    }
}

