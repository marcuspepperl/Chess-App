package game;

import board.*;
import player.*;
import piece.*;

import java.util.Set;
import java.util.Map;

public class ChessGame {

    private static int gameCount = 0;

    public void runStandardGame(Player whitePlayer, Player blackPlayer) throws
        InvalidSetUpException {
        GameBoard board = new GameBoard(8, 8);
        Set<APiece> allPieces = PieceSetBuilder.getStandardWhite();
        allPieces.addAll(PieceSetBuilder.getStandardBlack());
        board.setPieces(allPieces);
        this.runGame(board, whitePlayer, blackPlayer);
     }

     public void runGame(GameBoard board, Player whitePlayer, Player blackPlayer) throws
         InvalidSetUpException {
         gameCount++;
         Map<Integer, Player> players = Map.of(1, whitePlayer, -1, blackPlayer);
         int turn = 1;

         if (!board.isCheck(1)) {
             throw new InvalidSetUpException("Impossible set up position");
         }

         while (true) {
             if (board.isCheckMate(turn)) {
                 this.endGame(board, players.get(1), players.get(-1), -1 * turn);
                 break;
             }
             if (board.isStaleMate(turn)) {
                 this.endGame(board, players.get(1), players.get(-1), 0);
                 break;
             }

             Player player = players.get(turn);
            this.runMove(board, player, turn);
            turn *= -1;
         }
     }

     public void runMove(GameBoard board, Player player, int turn) {
        while (true) {
             try {
                 Coordinates[] request = player.requestMove();
                 if (board.handleMove(request, turn)) {
                     this.runPromotion(board, player, request, turn);
                 }
                 break;
             } catch (InvalidMoveException ignored) {
             }
        }
     }

     public void runPromotion(GameBoard board, Player player, Coordinates[]
         request, int turn) {
        APiece promotionPiece;
         do {
             promotionPiece = this.createPiece(player.requestPromotion(), turn);
             } while (promotionPiece == null);
         board.executePromotion(request[0], request[1], promotionPiece);
     }

     public void endGame(GameBoard board, Player whitePlayer, Player blackPlayer, int outcome) {
        if (outcome == 1) {
            whitePlayer.addWin();
            blackPlayer.addLoss();
        } else if (outcome == -1) {
            whitePlayer.addLoss();
            blackPlayer.addWin();
        } else {
            whitePlayer.addDraw();
            blackPlayer.addDraw();
        }
     }

    public int getGameCount() {
        return gameCount;
    }

     private APiece createPiece(int pieceType, int color) {
        if (pieceType == 0 || pieceType == 5) {
            return null;
         }
        return PieceSetBuilder.createPiece(pieceType, color);
     }
}