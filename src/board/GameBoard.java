package board;

import piece.*;
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
        this.pieceBoard = this.initializePieceBoard(xSize, ySize);
        this.colorBoard = this.initializeColorBoard(xSize, ySize);
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
        return this.moveEngine.handleMove(request, color);
    }

    public void handlePromotion(Coordinates startSquare, Coordinates endSquare, APiece
        newPiece) {
        this.moveEngine.handlePromotion(startSquare, endSquare, newPiece);
    }

    boolean takePiece(APiece piece) {
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

    void addPiece(APiece piece) {

        this.setColor(piece.getCoordinates(), piece.getColor());
        this.setPiece(piece.getCoordinates(), piece);

        if (piece.getColor() == 1) {
            this.whitePieces.add(piece);
        } else {
            this.blackPieces.add(piece);
        }
    }

    void endMove(APiece movePiece) {
        this.prevPiece = movePiece;
        this.moveCount++;
    }

    APiece getPiece(Coordinates coordinates) {
        return this.pieceBoard.getValue(coordinates.getXPos(), coordinates.getYPos());
    }

    int getColor(Coordinates coordinates) {
        return this.colorBoard.getValue(coordinates.getXPos(), coordinates.getYPos());
    }

    void setPiece(Coordinates coordinates, APiece piece) {
        this.pieceBoard.setValue(coordinates.getXPos(), coordinates.getYPos(), piece);
    }

    void setColor(Coordinates coordinates, int color) {
        this.colorBoard.setValue(coordinates.getXPos(), coordinates.getYPos(), color);
    }

    boolean inBounds(Coordinates coordinates) {
        return coordinates.getXPos() > 0 && coordinates.getXPos() < this.xSize &&
            coordinates.getYPos() > 0 && coordinates.getYPos() < this.ySize;
    }

    BoardWrapper<Integer> getColorBoard() {
        return this.colorBoard;
    }

    APiece getPrevPiece() {
        return this.prevPiece;
    }

    int getXSize() {
        return this.xSize;
    }

    int getYSize() {
        return this.ySize;
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
                .validMoves(this.getColorBoard())) {
                if (this.moveEngine.validStandard(coordinates, newSquare)) {
                    return true;
                }
            }
            if (piece instanceof Pawn) {
                leftSquare = new Coordinates(coordinates.getXPos() - 1,
                    coordinates.getYPos() + color);
                rightSquare = new Coordinates(coordinates.getXPos() + 1,
                    coordinates.getYPos() + color);
                if ((this.inBounds(leftSquare) && this.moveEngine.
                    validEnPassant(coordinates, leftSquare))
                    || (this.inBounds(rightSquare) && this.moveEngine.
                    validEnPassant(coordinates, rightSquare))) {

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

