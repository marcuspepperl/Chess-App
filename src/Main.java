import board.InvalidSetUpException;
import game.ChessGame;
import player.Player;

public class Main {
    public static void main(String[] args) throws InvalidSetUpException {

        Player player1 = new Player(), player2 = new Player();
        ChessGame chessGame = new ChessGame();
        chessGame.runStandardGame(player1, player2);
    }
}

