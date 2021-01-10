package main.game;

import main.board.*;
import main.player.*;
import main.piece.*;
import main.display.*;

import java.util.Set;
import java.util.Map;

public class ChessGame {

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

         Map<Integer, Player> players = Map.of(1, whitePlayer, -1, blackPlayer);
         ConsoleDisplay display = new ConsoleDisplay(board);
         int turn = 1;
         board.updateValidMoves();

         if (board.isCheck(-1)) {
             throw new InvalidSetUpException("Impossible set up position");
         }

         while (true) {
             display.displayStringBoard(turn);

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
             board.updateValidMoves();
             turn *= -1;
         }
     }

     public void runMove(GameBoard board, Player player, int turn) {
        while (true) {
             try {
                 if (turn == 1) {
                     System.out.println("White to move");
                 } else {
                     System.out.println("Black to move");
                 }
                 Coordinates[] request = player.requestMove();
                 if (board.handleMove(request, turn)) {
                     this.runPromotion(board, player, request, turn);
                 }
                 System.out.println("Move accepted");
                 break;
             } catch (InvalidMoveException e) {
                 System.out.println(e.getMessage());
             }
        }
     }

     public void runPromotion(GameBoard board, Player player, Coordinates[]
         request, int turn) {
        APiece promotionPiece;
         do {
             promotionPiece = this.createPiece(player.requestPromotion(), turn);
             } while (promotionPiece == null);
         promotionPiece.setCoordinates(request[1]);
         board.handlePromotion(request[0], request[1], promotionPiece);
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
         System.out.println("Game Ended");
     }

     private APiece createPiece(int pieceType, int color) {
        if (pieceType == 0 || pieceType == 5) {
            return null;
         }
        return PieceSetBuilder.createPiece(pieceType, color);
     }
}