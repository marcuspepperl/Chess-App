package main.board;

import main.piece.*;
import java.util.Set;
import java.util.HashSet;

public class GameBoard {

    private final int xSize;
    private final int ySize;
    private final BoardWrapper<APiece> pieceBoard;
    private final BoardWrapper<Integer> colorBoard;

    private final Set<APiece> whitePieces;
    private final Set<APiece> blackPieces;
    private final Set<APiece> takenWhitePieces;
    private final Set<APiece> takenBlackPieces;
    private King whiteKing;
    private King blackKing;

    private final MoveEngine moveEngine;

    private int moveCount;
    private APiece prevPiece;


    public GameBoard(int xSize, int ySize) throws InvalidSetUpException {
        if (xSize < 4 || ySize < 4) {
            throw new InvalidSetUpException("Board size too small");
        }
        this.xSize = xSize;
        this.ySize = ySize;
        this.pieceBoard = initializePieceBoard(xSize, ySize);
        this.colorBoard = initializeColorBoard(xSize, ySize);
        this.whitePieces = new HashSet<>();
        this.blackPieces = new HashSet<>();
        this.takenWhitePieces = new HashSet<>();
        this.takenBlackPieces = new HashSet<>();
        this.moveEngine = new MoveEngine(this);
    }

    public void setPieces(Set<APiece> allPieces) throws
        InvalidSetUpException {

        int whiteKingCount = 0, blackKingCount = 0;
        int color;

        for (APiece piece : allPieces) {
            Coordinates coordinates = piece.getCoordinates();
            color = piece.getColor();

            if (!inBounds(coordinates)) {
                throw new InvalidSetUpException("Piece out of bounds");
            }
            if (piece instanceof King) {
                if (color == 1) {
                    whiteKingCount++;
                    whiteKing = (King) piece;
                } else {
                    blackKingCount++;
                    blackKing = (King) piece;
                }
            }
            else if (piece instanceof Pawn) {
                if (color == 1 && coordinates.getYPos() == ySize - 1) {
                    throw new InvalidSetUpException("White pawn on promotion rank");
                } else if (color == -1 && coordinates.getYPos() == 0) {
                    throw new InvalidSetUpException("Black pawn on promotion rank");
                }
            }
            addPiece(piece);
        }

        if (whiteKingCount != 1 || blackKingCount != 1) {
            throw new InvalidSetUpException("Invalid number of kings");
        }
    }

    public void updateValidMoves() {
        for (APiece whitePiece : this.whitePieces) {
            whitePiece.updateValidMoves(colorBoard);
        }
        for (APiece blackPiece : this.blackPieces) {
            blackPiece.updateValidMoves(colorBoard);
        }
    }

    public boolean isCheckMate(int color) {
        return isCheck(color) && !hasValidMove(color);
    }

    public boolean isStaleMate(int color) {
        return !isCheck(color) && !hasValidMove(color);
    }

    public boolean isCheck(int color) {
        APiece defendingKing;
        if (color == 1) {
            defendingKing = whiteKing;
        } else {
            defendingKing = blackKing;
        }
        Coordinates kingCoordinates = defendingKing.getCoordinates(), attackingCoordinates;
        System.out.println(kingCoordinates);
        for (int xPos = 0; xPos < this.xSize; xPos++) {
            for (int yPos = 0; yPos < this.ySize; yPos++) {
                attackingCoordinates = new Coordinates(xPos, yPos);
                if (this.getColor(attackingCoordinates) == -1 * color &&
                    this.getPiece(attackingCoordinates).isValidMove(kingCoordinates)) {
                    return true;
                }
            }
        }
        return false;
    }

    // returns true if promotion is requested
    public boolean handleMove(Coordinates[] request, int color) throws
        InvalidMoveException {
        return moveEngine.handleMove(request, color);
    }

    public void handlePromotion(Coordinates startSquare, Coordinates endSquare, APiece
        newPiece) {
        moveEngine.handlePromotion(startSquare, endSquare, newPiece);
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public APiece getPrevPiece() {
        return prevPiece;
    }

    public APiece getPiece(Coordinates coordinates) {
        return pieceBoard.getValue(coordinates.getXPos(), coordinates.getYPos());
    }

    public Integer getColor(Coordinates coordinates) {
        return colorBoard.getValue(coordinates.getXPos(), coordinates.getYPos());
    }

    boolean takePiece(APiece piece) {
        if (piece != null) {
            if (piece.getColor() == 1) {
                whitePieces.remove(piece);
                takenWhitePieces.add(piece);
            } else {
                blackPieces.remove(piece);
                takenBlackPieces.add(piece);
            }
            return true;
        }
        return false;
    }

    void addPiece(APiece piece) {

        setPiece(piece.getCoordinates(), piece);
        if (piece.getColor() == 1) {
            whitePieces.add(piece);
        } else {
            blackPieces.add(piece);
        }
    }

    void setPiece(Coordinates coordinates, APiece piece) {
        pieceBoard.setValue(coordinates.getXPos(), coordinates.getYPos(), piece);
        if (piece == null) {
            colorBoard.setValue(coordinates.getXPos(), coordinates.getYPos(), 0);
        } else {
            colorBoard.setValue(coordinates.getXPos(), coordinates.getYPos(), piece.getColor());
        }
    }

    void endMove(APiece movePiece) {
        prevPiece = movePiece;
        moveCount++;
    }

    boolean inBounds(Coordinates coordinates) {
        return coordinates.getXPos() >= 0 && coordinates.getXPos() < xSize &&
            coordinates.getYPos() >= 0 && coordinates.getYPos() < ySize;
    }

    // standard moves and en passant
    private boolean hasValidMove(int color) {
        Set<APiece> allPieces;
        if (color == 1) {
            allPieces = whitePieces;
        } else {
            allPieces = blackPieces;
        }
        Coordinates coordinates, leftSquare, rightSquare;
        for (APiece piece : allPieces) {
            coordinates = piece.getCoordinates();
            for (Coordinates newSquare : piece.getValidMoves()) {
                if (moveEngine.validStandard(coordinates, newSquare)) {
                    System.out.println(coordinates.toString() + newSquare.toString());
                    return true;
                }
            }
            if (piece instanceof Pawn) {
                leftSquare = new Coordinates(coordinates.getXPos() - 1,
                    coordinates.getYPos() + color);
                rightSquare = new Coordinates(coordinates.getXPos() + 1,
                    coordinates.getYPos() + color);
                if ((inBounds(leftSquare) && moveEngine.validEnPassant(coordinates, leftSquare))
                    || (inBounds(rightSquare) && moveEngine.validEnPassant(coordinates, rightSquare))) {
                    return true;
                }
            }
        }
        return false;
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
}

